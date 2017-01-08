package com.dept.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.dept.web.dao.SmsJopDao;
import com.dept.web.dao.model.SmsJop;
import com.dept.web.dao.model.User;
import com.dept.web.general.util.Constants;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.MD5;
import com.dept.web.general.util.TimeUtil;
import com.dept.web.general.util.mmsg.SendMessageUtil;
import com.dept.web.general.util.tools.iphelper.IPUtils;
import com.dept.web.service.UserService;
import com.sendinfo.common.lang.StringUtil;

/**
 * 用户部分
 * 
 * @ClassName: UserController.java
 * @Description:
 * 
 * @author cannavaro
 * @version V1.0
 * @Date 2014-9-24 下午2:36:55 <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class UserController extends WebController {
	@Autowired
	private UserService userService;
	@Autowired
	private SmsJopDao smsJopDao;
	/**
	 * 注册
	 * 
	 * @Description:
	 * @param: @param map
	 * @param: @param request
	 * @param: @param response
	 * @param: @return
	 * @param: @throws NumberFormatException
	 * @param: @throws Exception
	 * @return: String
	 * @throws
	 */
	@RequestMapping("/signup")
	public String signup(ModelMap map, HttpServletRequest request,HttpServletResponse response, SessionStatus status,HttpSession session)throws NumberFormatException, Exception {

		Map<String, String> params = getParamMap(request);
		
		String ref  = params.get("ref"); //ref有值代表是用户推荐过来注册的
		if(!StringUtil.isEmpty(ref)) {
			//存入cookies 保存30天。
			Cookie cookie = new Cookie("inviteid",ref);
			cookie.setMaxAge(2592000);
			//设置路径，这个路径即该工程下都可以访问该cookie 如果不设置路径，那么只有设置该cookie路径及其子路径可以访问
			cookie.setPath("/");
			response.addCookie(cookie);
			
			
			User user = userService.queryByUserId(Long.valueOf(ref));
			if (user != null) {
				map.addAttribute("invite", user.getUsername());
			}
		}
		
		//读cookie     
		 String inviteid = "";     
		Cookie[] cookies = request.getCookies();     
		if(cookies!=null)     
		{     
		    for (int i = 0; i < cookies.length; i++)      
		    {     
		       Cookie c = cookies[i];          
		       if(c.getName().equalsIgnoreCase("inviteid"))     
		       {     
		    	   inviteid = c.getValue();     
		        }     
		    }      
		 }
		
		if (StringUtil.isNotEmpty(inviteid)) {
			try {
				User user = userService.queryByUserId(Long.valueOf(inviteid));
				if (user != null) {
					map.addAttribute("invite", user.getUsername());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
		//判断是跳转到注册页面还是注册
		if (params.get("type") == null) {
			return "signup";
		}

		response.setContentType("text/html;charset=UTF-8");

		try {
			String username = params.get("telphone");
			String password = params.get("password");
			String mobile = params.get("telphone");
			String repassword = params.get("repassword");
			String code = params.get("code");
			String referee = params.get("referee");
			
			if (username == null || "".equals(username)) {
				username = mobile;
			}
			
			session = request.getSession();

			
			if (!password.equals(repassword)) {
				map.addAttribute("msg", "两次输入的密码不一致");
				return "signup";
			}

			if (!userService.isRightMobile(mobile)) {
				map.addAttribute("msg", "你输入的用户名已经被注册为认证手机号，请更换");
				return "signup";
			}

			if(referee!=null&&!"".equals(referee)){
				User invite = userService.queryUserByName(referee);
				if(invite==null){
					map.addAttribute("msg", "你输入的推荐人不存在！");
					return "signup";
				}
			}
			
			
			
			String imgcode = params.get("verify");
			if(StringUtils.isEmpty(imgcode) || !imgcode.equals(session.getAttribute("captchaToken"))){
				map.addAttribute("msg", "你输入的图形验证码不正确！");
				return "signup";
			}
			
			// 获取session中验证码
			String sessionVcrand = (String) session.getAttribute("vcrand");
			String seesionPhone = (String) session.getAttribute("moblienum");

			if (StringUtils.isEmpty(sessionVcrand) || StringUtils.isEmpty(seesionPhone)) {

				map.addAttribute("msg", "手机验证码错误");

				return "signup";
			} else {
				if (!(sessionVcrand.equals(code) && seesionPhone.equals(mobile))) {
					map.addAttribute("msg", "手机验证码错误");
					return "signup";
				}
			}

			int vflag = regValidate(username, password, mobile);

			switch (vflag) {
			case 0: // 可以注册

				String ipadress = IPUtils.getRemortIP(request);

				User user = new User();

				user.setUsername(username);
				user.setMobile(mobile);
				user.setMobileBindingStatus(1);
				user.setPasswordHash(password);
				user.setCreatedIp(ipadress);
				//推荐人id
				User inviteUser = userService.queryUserByName(referee);
				if (inviteUser != null) {
					user.setInviteUserId(inviteUser.getId().toString());

				}

				user = userService.createNewUser(user);
				
				rememberMe(request, response, status, username,user.getPasswordHash(), user.getId(), "off");
				session.setAttribute("userInSession", user);
				map.addAttribute("successType", "注册成功");
				return "signup";

			case 1: // 用户名为空
				map.addAttribute("msg", "请输入4-16位用户名");
			case 2: // 密码为空
				map.addAttribute("msg", "请输入6-15位密码");
			case 3: // 用户名不能少于4位
				map.addAttribute("msg", "用户名不能少于4位");
			case 4: // 用户名不能大于16位
				map.addAttribute("msg", "用户名不能超过16位");
			case 5: // 密码长度不能少于6位
				map.addAttribute("msg", "请输入6-15位密码");
			case 6: // 密码长度不能多于15位
				map.addAttribute("msg", "请输入6-15位密码");
			case 7: // 密码不能为汉字
				map.addAttribute("msg", "密码请勿包含中文");
			case 8: // 用户名已被注册
				map.addAttribute("msg", "该用户名已经被注册");
				return "signup";
			case 9: // 用户名含特殊字符
				map.addAttribute("msg", "用户名不能含特殊字符");
			case 10: // 手机号码错误
				map.addAttribute("msg", "请输入正确的手机号码");
			case 11: // 手机号码已绑定
				map.addAttribute("msg", "该手机号码已经绑定");
			default:
				break;
			}
			return "signup";
		} catch (Exception e) {
			e.printStackTrace();
			map.addAttribute("msg", Constants.MSG_FAILURE);
			return "500";	
		}

	}

	/**
	 * 转到登录页面
	 * 
	 * @Title: 跳转到登录页
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("/tologin")
	public String toLogin(ModelMap map, HttpServletRequest request,HttpServletResponse response) throws Exception {

		String url = request.getHeader("Referer");
		map.addAttribute("returnUrl", url);

		return "login";
	}

	/**
	 * 登录
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @param status
	 * @param telphone
	 * @param password
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value = "/login")
	public String login(ModelMap map, HttpServletRequest request,HttpServletResponse response, SessionStatus status,HttpSession session) throws Exception {

		Map<String, String> params = getParamMap(request);
		String username = params.get("telphone");
		String password = params.get("password");
		String imgCode=params.get("imgCode");
		
		String msg = "";

		MD5 md5 = new MD5();
		if(StringUtils.isEmpty(imgCode) || !imgCode.equals(session.getAttribute("captchaToken"))){
			msg = "验证码不存在或错误！";
			map.addAttribute("msg", msg);
			return "login";
		}
		
		User user = userService.geyByLogInfo(username,md5.getMD5ofStr(password));
		if (user == null) {
			msg = "用户不存在或密码错误！";
			map.addAttribute("msg", msg);
			return "login";
		} else {
			if (user.getStatus() == 1) {
				msg = "该账户" + user.getUsername() + "已经被关闭！";
				map.addAttribute("msg", msg);
				return "login";
			} else {// 用户正常登陆
				// 用户信息
				this.putCurrUser(request, response, user);
				// 获取上次登录时间相关的信息,并修改lastLogintime为最新时间
				Long lastLogintime = userService.getlastLogintime(user);
				// 把获取到的上次登录时间相关的信息放到session中
				session.setAttribute("lastLogintime", lastLogintime);
				session.setAttribute("userInSession",userService.queryByUserId(user.getId()));
				rememberMe(request, response, status, username,user.getPasswordHash(), user.getId(),params.get("rememberme"));
				
				//设置头像
				if (user.getLitpic() != null && !user.getLitpic().equals("")) {
					user.setBigPic(user.getLitpic().substring(0,user.getLitpic().lastIndexOf(".")) + "_120x120.jpg");
					user.setOnPic(user.getLitpic().substring(0,user.getLitpic().lastIndexOf(".")) + "_100x100.jpg");
					user.setSmallPic(user.getLitpic().substring(0,user.getLitpic().lastIndexOf(".")) + "_80x80.jpg");
				} else {
					user.setOnPic(user.getLitpic());
				}
				//更新session中的userInSession信息
				session.setAttribute("userInSession", user);
				
				String returnUrl = params.get("returnUrl");
				
				if(returnUrl!=null && !returnUrl.equals("")){
					//如果登录之前的页面在注册页，则登录之后跳转到用户中心
					if(returnUrl.contains("signup.html")) {
						return "redirect:myhome/user-index.html";
					}
					return "redirect:"+returnUrl;
				}
				
				return "redirect:/myhome/user-index.html";
			}
		}
	}
	/**
	 * 登录
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @param status
	 * @param telphone
	 * @param password
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value = "/logins")
	public String logins(ModelMap map, HttpServletRequest request,HttpServletResponse response, SessionStatus status,HttpSession session) throws Exception {

		Map<String, String> params = getParamMap(request);
		String username = params.get("telphone");
		String password = params.get("password");
		String imgCode=params.get("imgCode");
		
		String msg = "";
		if(username==null||username.equals("")){
			if(password==null||password.equals("")){
				if(imgCode==null||imgCode.equals("")){
					return "login";
				}
			}
		}else{
		MD5 md5 = new MD5();
		if(StringUtils.isEmpty(imgCode) || !imgCode.equals(session.getAttribute("captchaToken"))){
			msg = "验证码不存在或错误！";
			map.addAttribute("msg", msg);
			return "index";
		}
		
		User user = userService.geyByLogInfo(username,md5.getMD5ofStr(password));
		if (user == null) {
			msg = "用户不存在或密码错误！";
			map.addAttribute("msg", msg);
			return "index";
		} else {
			if (user.getStatus() == 1) {
				msg = "该账户" + user.getUsername() + "已经被关闭！";
				map.addAttribute("msg", msg);
				return "index";
			} else {// 用户正常登陆
				// 用户信息
				this.putCurrUser(request, response, user);
				// 获取上次登录时间相关的信息,并修改lastLogintime为最新时间
				Long lastLogintime = userService.getlastLogintime(user);
				// 把获取到的上次登录时间相关的信息放到session中
				session.setAttribute("lastLogintime", lastLogintime);
				session.setAttribute("userInSession",userService.queryByUserId(user.getId()));
				rememberMe(request, response, status, username,user.getPasswordHash(), user.getId(),params.get("rememberme"));
				
				//设置头像0
				if (user.getLitpic() != null && !user.getLitpic().equals("")) {
					user.setBigPic(user.getLitpic().substring(0,user.getLitpic().lastIndexOf(".")) + "_120x120.jpg");
					user.setOnPic(user.getLitpic().substring(0,user.getLitpic().lastIndexOf(".")) + "_100x100.jpg");
					user.setSmallPic(user.getLitpic().substring(0,user.getLitpic().lastIndexOf(".")) + "_80x80.jpg");
				} else {
					user.setOnPic(user.getLitpic());
				}
				//更新session中的userInSession信息
				session.setAttribute("userInSession", user);
				
				String returnUrl = params.get("returnUrl");
				
				if(returnUrl!=null && !returnUrl.equals("")){
					//如果登录之前的页面在注册页，则登录之后跳转到用户中心
					if(returnUrl.contains("signup.html")) {
						return "redirect:myhome/user-index.html";
					}
					return "redirect:"+returnUrl;
				}
				
				return "redirect:/myhome/user-index.html";
				}
			}
		}
		return "index";
	}

	/**
	 * 注册验证
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public int regValidate(String username, String password, String mobile) {

		// 检查用户名和密码是否为空
		if (StringUtil.isEmpty(username)) {
			return 1;
		}
		if (StringUtil.isEmpty(password)) {
			return 2;
		}

		// 用户名是否达标
		if (username.length() < 4) {
			return 3;
		}
		if (username.length() > 16) {
			return 4;
		}

		// 密码是否达标
		if (password.length() < 6) {
			return 5;
		}
		if (password.length() > 15) {
			return 6;
		}

		// 密码是否含有汉字
		int count = 0;
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(password);
		while (m.find()) {
			count = 1;
		}
		if (count == 1) {
			return 7;
		}

		// 检查用户名是否已经注册
		if (!userService.isRightUserName(username)) {
			return 8;
		}

		// 用户是否含有特殊字符
		regEx = "[~!/@#$%^&*()-_=+\\[{}];:\'\",<.>/?]+";
		count = 0;

		p = Pattern.compile(regEx);

		m = p.matcher(username);

		while (m.find()) {
			count = 1;
		}
		if (count == 1) {
			return 9;
		}

		/** 必须手机号 */
		// if(!RegExUtil.isMobileNO(mobile)){
		//
		// return 10;
		// }

		// 检查手机号码是否绑定过
		if (!userService.isRightMobile(mobile)) {

			return 11;
		}

		return 0;
	}

	/**
	 * 用户退出
	 * 
	 * @param request
	 * @param response
	 * @param status
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/loginout")
	public String off(HttpServletRequest request, HttpServletResponse response,SessionStatus status,HttpSession session) throws UnsupportedEncodingException {

		rememberMe(request, response, status, null, null, 0, "off");
		request.getSession().invalidate();
		return "redirect:/index.html";
	}
	public Long getNowCreateTime1() {
	    
		long datetime = (System.currentTimeMillis() / 1000);
	        
	        return datetime;
	    }

	/**
	 * 发送手机验证码(手机端webApp)
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @param mn
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("sendcode")
	public void sendMobileVCode(ModelMap map, HttpServletRequest request,HttpServletResponse response) throws IOException {
		Map<String, String> params = getParamMap(request);
		String phone = params.get("phone");

		String updatepassword = params.get("updatepassword");// 修改密码传入updatepassword
//		String reg = params.get("reg");// 注册传入reg
		String forgetname = params.get("forgetname");// 忘记密码传入forgetname
		String jymm =  params.get("jyname");
		if (StringUtils.isNotEmpty(phone)) {
			phone = new String(phone.getBytes("iso-8859-1"), "UTF-8");
		}
		

		SmsJop sms = 	smsJopDao.selectSmsJopByPhone(phone);
		if(sms!=null){
			this.out(response, 99);
		}else{
			String imgcode = params.get("imgcode");
			
			HttpSession session = request.getSession();
		
			if(StringUtils.isEmpty(imgcode) || !imgcode.equals(session.getAttribute("captchaToken"))){
		    
				this.out(response, 98);
			}else{
				// 修改密码
				if (StringUtils.isNotEmpty(jymm)) {
	
					User user = userService.queryUserByName(phone);
					if(user==null){
						this.out(response, 4);//用户不存在
					} else {
					// 生成随机码
					Random random = new Random();
					String rand = "";
					for (int i = 0; i < 6; i++) {
						String r = String.valueOf(random.nextInt(10));
						rand += r;
					}
					session.setAttribute("vcrand", rand);
					session.setAttribute("moblienum", phone);
	
					String content = "【钱功夫】尊敬的用户您好，您的验证码为"+rand+",请及时验证。";
					System.out.println(content);
					try {
	
						SendMessageUtil.sendSMS(phone, content);
	
						session.setAttribute("seuser", user);
						
						
						   
                        //防短信轰炸。
    					SmsJop smsJop = new SmsJop(); 
    					smsJop.setAddtime(TimeUtil.getNowTimeStr());
    					String  endtime =TimeUtil.getEndTimeHMS(String.valueOf(getNowCreateTime1()), 0, 0, 0, 1, 0);
    					smsJop.setEndTime(String.valueOf(DateUtils.getTime(endtime)));
    					smsJop.setType("1");
    					smsJop.setPhone(phone);
    					smsJopDao.insertSmsJop(smsJop);
    					//防短信轰炸结束。
	
					} catch (Exception e) {
	
						e.printStackTrace();
					}
	
					this.out(response, 1);
	
				}
	
				// 注册时的手机验证码
				} else if(StringUtils.isEmpty(updatepassword) && StringUtils.isEmpty(forgetname)){
		
					boolean isReg = userService.isRightMobile(phone);
		
					if (isReg) {
						// 生成随机码
						Random random = new Random();
						String rand = "";
						for (int i = 0; i < 6; i++) {
							String r = String.valueOf(random.nextInt(10));
							rand += r;
						}
						session.setAttribute("vcrand", rand);
						session.setAttribute("moblienum", phone);
		
						String content = "【钱功夫】尊敬的用户您好，您的验证码为"+rand+",请及时验证。";
						System.out.println(content);
						try {
							SendMessageUtil.sendSMS(phone, content);
							
							
							   
	                        //防短信轰炸。
	    					SmsJop smsJop = new SmsJop(); 
	    					smsJop.setAddtime(TimeUtil.getNowTimeStr());
	    					String  endtime =TimeUtil.getEndTimeHMS(String.valueOf(getNowCreateTime1()), 0, 0, 0, 1, 0);
	    					smsJop.setEndTime(String.valueOf(DateUtils.getTime(endtime)));
	    					smsJop.setType("1");
	    					smsJop.setPhone(phone);
	    					smsJopDao.insertSmsJop(smsJop);
	    					//防短信轰炸结束。
						} catch (Exception e) {
		
							e.printStackTrace();
						}
						System.out.println(content);
						this.out(response, 1);
		
					} else {
						User user = userService.queryUserByName(phone);
						if (user == null && user.getStatus() == 0) {
							Random random = new Random();
							String rand = "";
							for (int i = 0; i < 6; i++) {
								String r = String.valueOf(random.nextInt(10));
								rand += r;
							}
							session.setAttribute("vcrand", rand);
							session.setAttribute("moblienum", phone);
		
							String content = "【钱功夫】尊敬的用户您好，您的验证码为"+rand+",请及时验证。";
							System.out.println(content);
							try {
		
								SendMessageUtil.sendSMS(phone, content);
								
								
								   
	                            //防短信轰炸。
		    					SmsJop smsJop = new SmsJop(); 
		    					smsJop.setAddtime(TimeUtil.getNowTimeStr());
		    					String  endtime =TimeUtil.getEndTimeHMS(String.valueOf(getNowCreateTime1()), 0, 0, 0, 1, 0);
		    					smsJop.setEndTime(String.valueOf(DateUtils.getTime(endtime)));
		    					smsJop.setType("1");
		    					smsJop.setPhone(phone);
		    					smsJopDao.insertSmsJop(smsJop);
		    					//防短信轰炸结束。
							} catch (Exception e) {
		
								e.printStackTrace();
							}
							System.out.println(content);
							this.out(response, 1);
						} else {
							this.out(response, 2); // 用户被注册
						}
					}
		
					// 找回密码发送验证码
				} else if (StringUtils.isNotEmpty(forgetname)) {
					
					User user = userService.queryUserByName(phone);
					if(user==null){
						this.out(response, 4);//用户不存在
					}else{
						// 生成随机码
						Random random = new Random();
						String rand = "";
						for (int i = 0; i < 6; i++) {
							String r = String.valueOf(random.nextInt(10));
							rand += r;
						}
						session.setAttribute("vcrand", rand);
						session.setAttribute("moblienum", phone);
			
						String content = "【钱功夫】尊敬的用户您好，您的验证码为"+rand+",请及时验证。";
						System.out.println(content);
			
						try {
			
							SendMessageUtil.sendSMS(phone, content);
							
							   
		                    //防短信轰炸。
							SmsJop smsJop = new SmsJop(); 
							smsJop.setAddtime(TimeUtil.getNowTimeStr());
							String  endtime =TimeUtil.getEndTimeHMS(String.valueOf(getNowCreateTime1()), 0, 0, 0, 1, 0);
							smsJop.setEndTime(String.valueOf(DateUtils.getTime(endtime)));
							smsJop.setType("1");
							smsJop.setPhone(phone);
							smsJopDao.insertSmsJop(smsJop);
							//防短信轰炸结束。
						} catch (Exception e) {
			
							e.printStackTrace();
						}
						this.out(response, 1);
			
					}
				}
			}
		}
	}
}