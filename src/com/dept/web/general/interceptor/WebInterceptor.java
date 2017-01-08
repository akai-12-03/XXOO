package com.dept.web.general.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dept.web.context.Global;
import com.dept.web.dao.model.SystemInfo;
import com.dept.web.general.util.HttpInclude;
import com.dept.web.service.SystemService;
import com.sendinfo.common.lang.StringUtil;

public class WebInterceptor implements HandlerInterceptor{
	
	private static String sysflag;
	
	private static Global global;
	
    @Autowired
    private SystemService systemService;
	
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
		
		if(StringUtil.isEmpty(sysflag)){
		    
		    SystemInfo info = systemService.getSystemInfo();
			
			global.SYSTEMINFO=info;
			
			sysflag = "true";
			
			setWebConfig(request,info);
			
		}else{
			
			setWebConfig(request,global.SYSTEMINFO);
			
		}
		
		return true;
	}
	/**
	 * 根据请求链接判断是否需要过滤 不需要登录就可以访问的页面
	 * 
	 * @param requestUri
	 * @return true:需要过滤，false：不需要过滤
	 */
	private boolean filterRequest(String requestUri) {
		boolean flag = true;
		String[] requestList = {"/404.htm", "/fail.htm",
		"/403.htm","/500.htm"};
		for (String string : requestList) {
			if (requestUri.endsWith(string)) {
				flag = false;
				break;
			}
		}
		return flag;
	} 
	
	private void setWebConfig(HttpServletRequest request,SystemInfo info){
		String[] webinfo=Global.SYSTEMNAME;
		for(String s:webinfo){
//          if(s.equals("weburl")){
//          request.setAttribute(s, "http://183.129.157.219:8020/sixiang");
//          }else if(s.equals("admurl")){
//              request.setAttribute(s, "http://183.129.157.219:8020/sixiangCms");
//          }else{
//              request.setAttribute(s, info.getValue(s));
//          }
//		    
			request.setAttribute(s, info.getValue(s));
		}
	}
	
}
