package com.dept.web.general.interceptor;

import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dept.web.dao.model.User;
import com.dept.web.general.context.HttpContext;
import com.dept.web.general.util.HttpInclude;
import com.dept.web.general.util.SessionHelper;
import com.dept.web.service.UserService;

public class LoginInterceptor implements HandlerInterceptor{
	
	@Autowired
	private UserService userService;
	
	private static final String[] IGNORE_URI = {"/myhome"};
		
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
	throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object object, ModelAndView modelAndView) throws Exception {
		
		if (modelAndView != null) {
			String viewName = modelAndView.getViewName();
			if (viewName != null && !viewName.startsWith("redirect:")) {
				// 笔者扩展的httpInclude
				modelAndView.addObject("httpInclude", new HttpInclude(request,
						response));
			}
		}

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object object) throws Exception {
		
		boolean flag = false;
        String url = request.getRequestURL().toString();
        
        for (String s : IGNORE_URI) {
            if (url.contains(s)) {
                flag = true;
                break;
            }
        }
        if (flag) {
        			
        	Long userId = this.getUserId(request, response);
    		if (userId == null || userId.intValue() <= 0) {
    			
    			response.sendRedirect("../tologin.html");
    			
    			return false;
 
    		}
    		
    		return true;
         }
        
        return true;
	}
	
	
	private Long getUserId(HttpServletRequest aRequest, HttpServletResponse aResponse) {
		Long result = 0l;
		boolean flag = false;
		User al = null;
		String strLoginedUser = "";
		String strLoginedPassword = "";
		
		try {
			result = SessionHelper.getUserId(aRequest, aResponse);
			
			if (result == null || result > 0l) {
				Cookie ary[] = aRequest.getCookies();
				if (ary != null && ary.length > 0) {
					for (Cookie item : ary) {
						if (HttpContext.SessionKey.LOGINED_USER.toString().equalsIgnoreCase(item.getName()))
							strLoginedUser = URLDecoder.decode(item.getValue(),"utf-8").replace("#", "@");
							
						if (HttpContext.SessionKey.LOGINED_PASSWORD.toString().equalsIgnoreCase(item.getName()))
							strLoginedPassword = item.getValue();
					}
					
					al = userService.geyByLogInfo(strLoginedUser, strLoginedPassword);
					
					if (al != null)
						flag = true;
				}
			}
			
			if (flag) {
				return al.getId();
			} else {
				return result;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
