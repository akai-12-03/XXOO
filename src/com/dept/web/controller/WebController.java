package com.dept.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.support.SessionStatus;

import com.dept.web.dao.model.User;
import com.dept.web.general.context.HttpContext;
import com.dept.web.general.util.SessionHelper;
import com.dept.web.general.util.SessionHelper.SessionType;
import com.dept.web.general.util.tools.iphelper.IPSeeker;
import com.dept.web.general.util.tools.iphelper.IPUtils;
import com.dept.web.service.UserService;
import com.google.gson.GsonBuilder;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.controller.BaseController;

/**
 * 基础类
 * @ClassName:     WebController.java
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-8-10 上午11:09:38
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class WebController extends BaseController {
	
	/**
	 * 错误消息
	 */
	protected static final String ERRORMSG = "errormsg";

	/**
	 * 返回结果
	 */
	protected static final String CODE = "code";

	/**
	 * 返回结果信息
	 */
	protected static final String MSG = "msg";

	protected static final String SESSION_USER = "session_user";

	/**
	 * 最后签到时间
	 */
	protected static final String SESSION_SIGNIN_STATUS_LAST_TIME = "SIGNIN_LAST_TIME";

	/**
	 * 签到图标是否隐藏(true!false)
	 */
	protected static final String SESSION_SIGNIN_HIDDEN = "signin_hidden";
	
	/*** 乾多多资金托管用公共**/
	//公共参数
	protected String PlatformMoneymoremore = "";//平台标识---必填
	protected String SubmitURLPrefix = "";	//第三方根路径
	protected String SubmitURL = "";
	protected String basePath ="http://183.129.157.219:8082/ryd/";		//平台根路径
	protected String ReturnURL = "";		//页面返回网址(手动转账必填)
	protected String NotifyURL = "";		//后台通知网址---必填
	protected String SignInfo = "";			//签名信息---必填
	protected String ResultCode = "";		//返回码---必填
	protected String Message = "";			//返回信息---必填
	protected String ReturnTimes = "";
	protected String verifySignature = "";
	protected String RandomTimeStamp = "";//随机时间戳(启用防抵赖时必填格式为2位随机数加yyyyMMddHHmmssSSS格式的当前时间；未启用防抵赖时必须为空)
	protected final int antistate = 0;	  //防抵赖:0未启用,1启用
	protected String MoneymoremoreId="";
	protected String Remark1 = ""; 	// 备注
	protected String Remark2 = "";
	protected String Remark3 = "";
	
	//转账提交参数
	protected String LoanJsonList = ""; 	// 转账列表---必填
	protected String TransferAction = "";// 转账类型1.投标 2.还款 3.其他---必填
	protected String Action = ""; 		// 操作类型1.手动 2.自动---必填
	protected String TransferType = "";  // 转账方式1.桥连 2.直连---必填
	protected String NeedAudit = ""; 		// 是否需要托管账户审核(空.需要审核,即冻结 1.自动通过即及时)---选填
	protected String OrderNo1="";			//单号
	protected String BatchNo1="";			//批号
	protected String SecondaryJsonList="";  //二次分配
	
	
	//审核提交参数
	protected String LoanNoList = ""; // 乾多多流水号列表(投标返回的流水号LoanNo：LN12345678901234；用，隔开)
	protected String AuditType = ""; 	// 审核类型(1.通过 2.退回 3.二次分配同意 4.二次分配不同意 5.提现通过 6.提现退回)

	//审核返回参数
	protected String LoanNoListFail = ""; //有问题的乾多多流水号列表(投标返回的流水号LoanNo：LN12345678901234；用，隔开)
	/*** 乾多多资金托管用公共**/
    protected String base;
    
    @Autowired
    protected UserService userService;
    
    /**
     * 默认分页条数为每页10条
     */
    protected static final Integer DEFAULT_PAGE_SIZE = 10;
    
    /**
     * 把request参数封装到VO
     * 
     * @author jianjiang
     * @param bean
     * @param request
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void populate(Object bean, HttpServletRequest request) throws IllegalAccessException,
            InvocationTargetException {
        BeanUtils.populate(bean, request.getParameterMap());
    }
    
    public String getBase(HttpServletRequest request) {
        int port = request.getServerPort();
        if (port == 80) {
            return request.getScheme() + "://" + request.getServerName() + request.getContextPath() + "/";
        } else {
            return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath() + "/";
        }
    }
    
    /**
     * 获取参数map (把HttpServletRequest里的ParameterMap统一转换成HashMap)
     * 
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getParamMap(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> reqParamMap = new HashMap<String, Object>();
        reqParamMap.putAll(request.getParameterMap());
        for (String key : reqParamMap.keySet()) {
            paramMap.put(key, request.getParameter(key));
        }
        return paramMap;
    }
    

    
    final protected void out(HttpServletResponse response, Object target) {
        GsonBuilder gbuild = new GsonBuilder();
        try {
            response.setContentType("text/javascript;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write(gbuild.create().toJson(target));
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Response writing failure.", e);
        }
    }
    
    /**
     * 获得登录用户信息
     * 
     * <pre>
     * 返回用户信息
     * </pre>
     * 
     * @param request
     * @return
     */
    final protected User getCurrUser(HttpServletRequest request, HttpServletResponse response) {
        Long userId = this.getUserId(request, response);
        if (userId == null || userId.intValue() <= 0) {
            return null;
        }
        
       return this.userService.queryByUserId(userId);
        
    }
    
    final protected void putCurrUser(HttpServletRequest request, HttpServletResponse response, User user) {
        if (user == null)
            SessionHelper.setUserId(request, response, null);
        else
            SessionHelper.setUserId(request, response, user.getId());
    }
    
    private Long getUserId(HttpServletRequest aRequest, HttpServletResponse aResponse) {
        Long result = 0l;
        boolean flag = false;
        User al = null;
        String strLoginedUser = "";
        String strLoginedPassword = "";
        String jessionId = "";
        
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
                        if (HttpContext.SessionKey.JSESSIONID.toString().equalsIgnoreCase(item.getName()))
                            jessionId = item.getValue();                        
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

    
    /**
     * 获取http请求的实际IP
     * @return
     */
    protected String getRequestIp(HttpServletRequest request){
        String realip=IPUtils.getRemortIP(request);
        return realip;
    }
    
    /**
     * 获取IP所在地
     * @return
     */
    protected String getAreaByIp(HttpServletRequest request){
        String realip=getRequestIp(request);
        return getAreaByIp(realip);
    }
    protected String getAreaByIp(String ip){
        IPSeeker ipSeeker = IPSeeker.getInstance();
        String nowarea=ipSeeker.getArea(ip);
        return nowarea;
    }
    
    
    
    /**
     * 
     * @Description:  记住密码
     * @param:        @param request
     * @param:        @param response
     * @param:        @param status
     * @param:        @param username
     * @param:        @param password
     * @param:        @param userid
     * @param:        @param type on记住
     * @param:        @throws UnsupportedEncodingException   
     * @return:       void   
     * @throws
     */
    protected void rememberMe(HttpServletRequest request, HttpServletResponse response, SessionStatus status, 
            
            String username, String password, long userid, String type) throws UnsupportedEncodingException{
        
        if(StringUtil.isEmpty(type)|| type.equals("off")){
            this.putCurrUser(request, response, null);
            status.setComplete();
            request.getSession().removeAttribute("currUser");
            Cookie ckUsername, ckSessionid;
            ckUsername = new Cookie("logined_user", ""); // user是代表用户的bean
            
            ckUsername.setPath("/");
            response.addCookie(ckUsername);
            
            ckSessionid = new Cookie("logined_password", "");
            
            ckSessionid.setPath("/");
            response.addCookie(ckSessionid);
            
            SessionHelper.setSession(request, SessionType.USER_ID, userid);

        }else if (type.equals("on")) {
            
            /** cookies相关 start */
            Cookie ckUsername, ckSessionid;
            String value=URLEncoder.encode(username,"UTF-8");
            ckUsername = new Cookie("logined_user", value.replace("@", "#")); // user是代表用户的bean

            ckUsername.setPath("/");
            ckUsername.setMaxAge(60 * 60 * 24 * 14); // 设置Cookie有效期为14天
            response.addCookie(ckUsername);
            
            ckSessionid = new Cookie("logined_password", password.toUpperCase());
            ckSessionid.setPath("/");
            ckSessionid.setMaxAge(60 * 60 * 24 * 14);
            response.addCookie(ckSessionid);
            
            /** cookies相关 end */
        } else{
            this.putCurrUser(request, response, null);
            status.setComplete();
            request.getSession().removeAttribute("currUser");
            Cookie ckUsername, ckSessionid;
            ckUsername = new Cookie("logined_user", ""); // user是代表用户的bean
            
            ckUsername.setPath("/");
            response.addCookie(ckUsername);
            
            ckSessionid = new Cookie("logined_password", "");
            
            ckSessionid.setPath("/");
            response.addCookie(ckSessionid);
        }
        
    }
  
}