package com.dept.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.dept.web.dao.HongbaoDao;
import com.dept.web.dao.model.Bank;
import com.dept.web.dao.model.BankCard;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowRepayment;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.City;
import com.dept.web.dao.model.Hongbao;
import com.dept.web.dao.model.PlanAppendInsure;
import com.dept.web.dao.model.PlanRecord;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.dao.model.UserBorrowModel;
import com.dept.web.general.javamail.Mail;
import com.dept.web.general.util.MD5;
import com.dept.web.general.util.NumberUtil;
import com.dept.web.general.util.TextUtil;
import com.dept.web.general.util.UploadUtil;
import com.dept.web.service.BankCardService;
import com.dept.web.service.BankService;
import com.dept.web.service.BorrowCollectionService;
import com.dept.web.service.BorrowService;
import com.dept.web.service.CityService;
import com.dept.web.service.PlanRecordService;
import com.dept.web.service.PlanService;
import com.dept.web.service.UserAccountLogService;
import com.dept.web.service.UserAccountService;
import com.dept.web.service.UserService;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;
import com.sendinfo.xspring.ibatis.page.PageUtils;

/**
 * 用户中心
 * 
 * @ClassName: CenterController.java
 * @Description:
 * 
 * @author cannavaro
 * @version V1.0
 * @Date 2014-9-2 上午11:24:26 <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class CenterController extends WebController {
	@Autowired
	private UserService userService;
	@Autowired
	private UserAccountService accountService;
	@Autowired
	private BankCardService bankCardService;
	@Autowired
	private UserAccountLogService userAccountLogService;
	@Autowired
	private PlanRecordService planRecordService;
	@Autowired
	private BorrowService borrowService;
	@Autowired
	private BankService bankService;
	@Autowired
	private BorrowCollectionService borrowCollectionService;
	@Autowired
	private PlanService planService;
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private HongbaoDao hongbaoDao;
	
	@Autowired
	private UserAccountService userAccountService;
	/**
	 * 设置 一个成员变量用来保存要绑定的邮箱
	 */
	private String emailBind;

	   
	/**      
	 * @desc 用途描述: 实名认证页面
	 * @param map
	 * @param request
	 * @param response
	 * @return 返回说明:
	 * @exception 内部异常说明:
	 * @throws 抛出异常说明:
	 * @author gwx
	 * @version 1.0      
	 * @created 2016-2-19 上午10:44:20 
	 * @mod 修改描述:
	 * @modAuthor 修改人:
	    
	 */
	@RequestMapping("myhome/realname")
	public String toRealname(ModelMap map, HttpServletRequest request,HttpServletResponse response){
		User user=this.getCurrUser(request, response);
		if(user!=null){
			map.addAttribute("user", user);
			map.addAttribute("msg", request.getParameter("msg"));
			if(user.getUserType()==null||user.getUserType()==1)
			{
				return "myhome/realname";
			}
			else
			{
				return "myhome/company-auth";
			}
		}else{
			return "redirect:/tologin.html";
		}
	}
	
	/**
	 * 用户中心首页
	 * 
	 * @Description:
	 * @param: @param map
	 * @param: @param request
	 * @param: @param response
	 * @param: @param status
	 * @param: @return
	 * @param: @throws NumberFormatException
	 * @param: @throws Exception
	 * @return: String
	 * @throws
	 */
	@RequestMapping("myhome/user-index")
	public String signup(ModelMap map, HttpServletRequest request,HttpServletResponse response, HttpSession session) throws NumberFormatException, Exception {
		try {
			// 获取登录的用户信息
			User user = new User();
			user = this.getCurrUser(request, response);
			// 获取用户账户信息
			UserAccount ua = accountService.getUserAccount(user.getId());
			//冻结金额
			ua.setTtdj(ua.getMoneyTenderFreeze()+ua.getMoneyWithdraw());
			map.addAttribute("ua", ua);
			System.out.println(ua.getTtdj());
			
			//获取我的收益
			double myinterest = 	borrowService.getInterestByBorrowTenderForUserId(user.getId());
			map.addAttribute("myinterest", myinterest);
			//最近待收金额,最近待收时间
			BorrowCollection collection  = borrowCollectionService.getCollectionForAccount(user.getId());
			map.addAttribute("collection", collection);
			
//			double collotionInterest =borrowService.getCount(user.getId());
//			map.addAttribute("collotionInterest", collotionInterest);
			//代收金额。
//			double collotionMoney = borrowService.getDSmoney(user.getId());
//			map.addAttribute("collotionMoney", collotionMoney);
			
			
            //获取推荐理财
			Map<String,Object> filterMap = new HashMap<String, Object>();
	    	filterMap.put("status", 1);
	    	filterMap.put("count", 2);
            List<Borrow> borrowList = borrowService.queryblist(filterMap);
            if(borrowList != null && borrowList.size() > 0) {
				map.addAttribute("borrowList", borrowList);
			}
            
            //获取红包详情
            Hongbao hb=hongbaoDao.queryHongbaoForAccount(user.getId());
            map.addAttribute("hb", hb);
            
            //获取已赚奖励
            Map<String,Object> filterMap1 = new HashMap<String, Object>();
            filterMap1.put("userId", user.getId());
            filterMap1.put("type", 32);
            double awardMoney = userAccountLogService.queryByTypeAndUserId(filterMap1);
            map.addAttribute("awardMoney", awardMoney);
			// 把登录或注册成功的user对象放到session中
			session.setAttribute("userInSession", user);
			
			return "myhome/account";
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 查看用户的借款情况
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping("myhome/jieBorrow")
	public String jieBorrow(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			long user_id=user.getId();
			String type = request.getParameter("type");
			if(StringUtils.isBlank(type)){
				type="publish";
			}
			// 查询条件
			String dotime1 =request.getParameter("dotime1");
			String dotime2 = request.getParameter("dotime2");
			String keywords = request.getParameter("keywords");
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);
			Map<String, String> params = getParamMap(request);
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			params.put("keywords",keywords);
			params.put("dotime1",dotime1);
			params.put("dotime2",dotime2);
			params.put("type", type);
			params.put("userId",String.valueOf(user_id));
			pageRequest.setFilters(params);
			Page<UserBorrowModel> borrowpage = borrowService.getUserCenterBorrowList(pageRequest);
			map.addAttribute("page",pageRequest.getPageNumber());
			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
			if(type.equals("unpublish")){
				map.addAttribute("mt",42);   //尚未发布的项目
			}else if(type.equals("repayment")){
				map.addAttribute("mt",43);   //正在还款的项目
			}else if(type.equals("repaymentyes")){
				map.addAttribute("mt",44);   //已还款的项目
			}else{
				map.addAttribute("mt",41);   //正在招标的项目
			}
			map.addAttribute("type",type);
		}
		map.addAttribute("errmsg", request.getParameter("errmsg"));
		return "myhome/loanRecord";
	}
	
	/**
	 * 查看用户还款明细帐
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping("myhome/repinfo")
	public String repaymentdetail(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			long user_id=user.getId();
			long borrowId=NumberUtil.getLong(request.getParameter("borrowId"));
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			//String numberstr =(String) request.getParameter("topNext");
			pageRequest.setPageSize(10);
			Map<String, String> params = getParamMap(request);
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			Page<BorrowRepayment> borrowpage;
			params.put("userId",String.valueOf(user_id));
			if(borrowId>0){
				params.put("borrowId",String.valueOf(borrowId));
				pageRequest.setFilters(params);
				borrowpage = borrowService.getUserCenterRepaymentdetailByborrowId(pageRequest);
			}else{
				pageRequest.setFilters(params);
				borrowpage = borrowService.getUserCenterRepaymentdetail(pageRequest);
			}
			map.addAttribute("page",pageRequest.getPageNumber());
			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
			Date date = Calendar.getInstance().getTime();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        map.addAttribute("currentTime",sdf.format(date)); 
	        map.addAttribute("today",date.getTime());
	        map.addAttribute("mt", 45); //还款明细帐
		}
		
		map.addAttribute("errmsg", request.getParameter("errmsg"));
		
		return "myhome/repinfo";
	}

	/**
	 * 邮箱认证
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("myhome/email")
	public String email(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws MalformedURLException{
		User user = getCurrUser(request, response);
		map.addAttribute("user", user);
		map.addAttribute("msg",request.getParameter("msg"));
		return "myhome/user-youxiang";
	}

	/**
	 * 跳转到修改交易密码页
	 * 
	 * @return
	 */
	@RequestMapping(value = "myhome/toUpdatePaySecurityPage")
	public String toUpdatePaySecurityPage() {
		return "myhome/update_paysecurity";
	}
	/**
	 * 修改密码页面
	 * @param map
	 * @param request
	 * @param response
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("myhome/xiugai")
	public String xiugai(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		return "myhome/user-security";
	}
	
	/**
	 * 修改交易密码页面
	 * @param map
	 * @param request
	 * @param response
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("myhome/xiugaijy")
	public String xiugaijy(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		return "myhome/pay-security";
	}
	
	
	/**
	 * 用户中心检查密码
	 * @Description:  TODO
	 * @param:        @param oldpassword
	 * @param:        @param newpassword
	 * @param:        @param user
	 * @param:        @return   
	 * @return:       String   
	 * @throws
	 */
	public String checkUserpwd(String oldpassword, String newpassword, String renewpassword, User user, int type,HttpServletRequest request) {	
		User vailduser = new User();
		
		vailduser = userService.queryByUserId(user.getId());
		MD5 md5 = new MD5();
		
		HttpSession session = request.getSession();
		String seesionPhone = (String) session.getAttribute("vcrand");
		if(type==1){
			
			if(!vailduser.getPasswordHash().toLowerCase().equals(md5.getMD5ofStr(oldpassword).toLowerCase())){
				
				return "原密码不正确，请输入您的旧密码";
				
			}else if (newpassword.length() < 6 || newpassword.length() > 16) {
				
				return "新密码长度要在6到16之间";
				
			}else if (!newpassword.equals(renewpassword)){
				
				return "两次密码不一致";
				
			}else if (renewpassword.length() < 6 || renewpassword.length() > 16){
				
				return "确认密码长度要在6到16之间";
			}
			
		}else{
			
			if(!vailduser.getPasswordPayHash().equals(md5.getMD5ofStr(oldpassword).toLowerCase())){
				
				return "密码不正确，请输入您的旧密码";
				
			}else if (newpassword.length() < 6 || newpassword.length() > 15) {
				
				return "新密码长度在6到15之间";
				
			}
		}
		return "";
	}
	
	   
	/**      
	 * @desc 用途描述: 跳转至修改密码页面
	 * @param map
	 * @param request
	 * @param response
	 * @return 返回说明:
	 * @exception 内部异常说明:
	 * @throws 抛出异常说明:
	 * @author gwx
	 * @version 1.0      
	 * @created 2016-2-19 上午11:30:12 
	 * @mod 修改描述:
	 * @modAuthor 修改人:
	    
	 */
	@RequestMapping("myhome/toChangePwd")
	public String toChangePwd(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		User user=this.getCurrUser(request, response);
		if(user!=null){
			map.addAttribute("user", user);
			map.addAttribute("msg", request.getParameter("msg"));
		}else{
			return "redirect:/tologin.html";
		}
		return "myhome/changePwd";
	}
	
	/**
	 * 用户中心修改密码
	 * @Description:  TODO
	 * @param:        @param map
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param nid
	 * @param:        @return
	 * @param:        @throws Exception   
	 * @return:       String   
	 * @throws
	 */
	@RequestMapping("myhome/security")
	@Transactional(rollbackFor={Exception.class})
	public String security(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam int type) throws Exception{
		
		String oldpassword = request.getParameter("oldpassword");
		String newpassword = request.getParameter("password");//新密码
		String renewpassword = request.getParameter("repassword");//确认新密码
		
		User user = this.getCurrUser(request, response);
		String msg = "";
		
		if(!StringUtils.isEmpty(oldpassword)){
			
			MD5 md5 = new MD5();
			
			msg = checkUserpwd(oldpassword, newpassword, renewpassword, user, type,request);
			
			if(StringUtils.isEmpty(msg)){
				
				try {
					if(oldpassword.equals(newpassword)){
						map.addAttribute("msg", "新旧密码不能相同");
					}else{
						int count = userService.updatePassword(user.getId(), md5.getMD5ofStr(newpassword).toLowerCase(), type);
						if(count>0){
							map.addAttribute("msg", "修改成功");
						}else{
							map.addAttribute("msg", "修改失败");
						}
					}
				} catch (Exception e) {
					map.addAttribute("msg", "修改失败，请联系管理员");
					e.printStackTrace();     
			        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
					
			}else{
				map.addAttribute("msg", msg);
			}
		}else{
			map.addAttribute("msg", "请输入正确的旧密码");
		}
		return "redirect:/myhome/toChangePwd.html";
		
	}

	/**
	 * 修改交易密码
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "myhome/updatePayPassword", method = RequestMethod.POST)
	@Transactional(rollbackFor = { Exception.class })
	public String updatePayPassword(
			HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session,
			ModelMap map,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "paypassword", required = false) String paypassword) {

		User user = getCurrUser(request, response);
		MD5 md5 = new MD5();

		String sessionVcrand = (String) session.getAttribute("vcrand");

		String msg = "";
		if (!"".equals(paypassword) && !"".equals(code)) {
			if (user.getPasswordPayHash().toLowerCase()
					.equals(md5.getMD5ofStr(paypassword).toLowerCase())) {
				msg = "新旧交易密码相同，请重新输入新交易密码!";
			}  else if (StringUtils.isEmpty(sessionVcrand)
					|| !(sessionVcrand.equals(code))) {
				msg = "验证码有误，请重新输入!";
			} else if (paypassword.length() < 6 || paypassword.length() > 15) {
				msg = "新交易密码长度在6到15之间";
			} else if (user.getPasswordHash().toLowerCase()
					.equals(md5.getMD5ofStr(paypassword).toLowerCase())) {
				msg = "交易密码不能和登录密码一致，请重新输入!";
			} else {// 验证都通过，可以修改
				try {
					user.setPasswordPayHash(md5.getMD5ofStr(paypassword));
					int count = userService.userUpdate(user);
					if (count > 0) {
						msg = "修改成功!";
					} else {
						msg = "修改失败!";
					}
				} catch (Exception e) {
					map.put("msg", msg);
					e.printStackTrace();
					TransactionAspectSupport.currentTransactionStatus()
							.setRollbackOnly();
				}
			}
		} else {
			msg = "信息填写不完整，请检查!";
		}
		map.put("msg", msg);
		map.put("type", request.getParameter("type"));
		return "redirect:/myhome/aqzx.html";
	}

	/**
	 * 发送邮箱验证信息
	 */
	@RequestMapping("myhome/sendEmail")
	public String sendEmail(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@RequestParam(value = "email", required = false) String email)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		User user = getCurrUser(request, response);
		String msg = "";
		if (StringUtil.isEmpty(email)) {
			msg = "email 不存在";// email 不存在
		}
		// 添加邮箱正则表达式验证
		Pattern pattern = Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

		Matcher matcher = pattern.matcher(email);

		if (matcher.matches()) {
			if (user != null) {
				// 查询邮箱是否已经被绑定过
				int count = userService.getEmailForUser(email);
				if (count == 0) {
					// 把email赋值给成员变量
					emailBind = email;
					// 发送邮箱
					user.setEmail(email);
					sendMSG(user);
					String flag = "邮件已发送成功,请登录邮箱确认!";
					msg = "邮件已发送成功,请登录邮箱确认!";
					map.put("flag", flag);
				} else {
					msg = "邮箱已存在";
				}
			} else {
				msg = "用户不存在";
			}
		} else {
			msg = "邮箱格式不正确";
		}

		map.put("msg", msg);
		map.addAttribute("msg", msg);
		return "redirect:/myhome/email.html";
	}

	public void sendMSG(User user) throws Exception {
		String to = user.getEmail();
		Mail m = Mail.getInstance();
		m.setTo(to);
		m.readActiveMailMsg();
		String signStr = new MD5().getMD5ofStr(user.getId()
				+ user.getUsername() + "2");
		m.replace(user.getUsername(), to,"/call_backEmail.html?id=" + user.getId() + "&type=" + 2 + "&sign=" + signStr);
		m.sendMail();
	}

	/**
	 * 
	 * @Description: 验证邮箱返回
	 * @param: @param map
	 * @param: @param request
	 * @param: @param response
	 * @param: @return
	 * @param: @throws NumberFormatException
	 * @param: @throws Exception
	 * @return: String
	 * @throws
	 */
	@RequestMapping("call_backEmail")
	public String call_backEmail(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, SessionStatus status,
			HttpSession session) throws NumberFormatException, Exception {
		Map<String, String> params = getParamMap(request);
		String id = params.get("id");
		String type = params.get("type");
		String sign = params.get("sign");
		User user = userService.queryByUserId(Long.parseLong(id));
		String signStr = new MD5().getMD5ofStr(user.getId() + user.getUsername() + type);
		if (!sign.equals(signStr)) {
			map.addAttribute("msg", "邮箱发送连接错误，请重新发送");
			return "redirect:user-youxiang.html?ctype=email";
		}
		// 更新user对象
		user.setEmail(emailBind);
		user.setEmailBindingStatus(1);
		userService.userUpdate(user);
		//头像
		if (user.getLitpic() != null && !user.getLitpic().equals("")) {
			user.setBigPic(user.getLitpic().substring(0,user.getLitpic().lastIndexOf(".")) + "_120x120.jpg");
			user.setOnPic(user.getLitpic().substring(0,user.getLitpic().lastIndexOf(".")) + "_100x100.jpg");
			user.setSmallPic(user.getLitpic().substring(0,user.getLitpic().lastIndexOf(".")) + "_80x80.jpg");
		} else {
			user.setOnPic(user.getLitpic());
		}
		// 更新session中的user
		session.setAttribute("userInSession", user);
		return "redirect:myhome/user-youxiang.html";
	}

	/**
	 * 验证忘记密码页面提交过来的参数
	 * 
	 * @return
	 */
	@RequestMapping(value = "checkforgetpasswordparam", method = RequestMethod.POST)
	public String forgetPasword(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap map) {
		String msg = "";
		// 获取提交的参数
		String mobile = request.getParameter("telphone");
		String password = request.getParameter("password");
		String repassword = request.getParameter("repassword");

		String verify = request.getParameter("imgCode");// 提交过来的验证码
		String code = request.getParameter("code");// 提交过来的手机短信验证码

		// 从session里获取验证码
		String verifyFromSession = session.getAttribute("captchaToken")
				.toString();
		// 从sesion里获取手机验证码
		String sessionVcrand = (String) session.getAttribute("vcrand");

		// 校验
		if (password == null || repassword == null || verify == null) {
			msg = "信息填写不完整，请确认！";
		} else if (userService.queryUserByMobile(mobile) == null) {// 绑定该手机号的账户的不存在
			msg = "绑定该手机号的帐号为空，请核对!";
		} else {
			if (!password.equals(repassword)) {
				msg = "两次密码不一致!";
			} else if (!verify.equals(verifyFromSession)) {
				msg = "图片验证码有误!";
			} else if (!code.equals(sessionVcrand)) {
				msg = "手机验证码有误!";
			} else {// 验证都通过，可以修改
				try {
					User user = userService.queryUserByMobile(mobile);
					MD5 md5 = new MD5();
					user.setPasswordHash(md5.getMD5ofStr(password));
					int count = userService.userUpdate(user);
					if (count > 0) {
						msg = "忘记密码修改成功!";
					} else {
						msg = "忘记密码修改失败!";
					}

				} catch (Exception e) {
					map.put("msg", "系统出错，忘记密码修改失败");
					e.printStackTrace();
					TransactionAspectSupport.currentTransactionStatus()
							.setRollbackOnly();
					return "forgetpassword";
				}

			}
		}
		map.put("msg", msg);
		return "forgetpassword";
	}
	
	@RequestMapping("forgetpassword")
	public String forget(ModelMap map, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		
		return "forgetpassword";
	}
	
	@RequestMapping("forgetPaypassword")
	public String forgetPay(ModelMap map, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		
		return "forgetPaypassword";
	}
	
	private String md5(String s){
		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	
	private String url2string(String url){
		StringBuffer sb=new StringBuffer();
		try {
			InputStream is=new URL(url).openStream();
			byte[] buf=new byte[1024*10];
			int len=0;
			while((len=is.read(buf, 0, 1024*10))>0){
				sb.append(new String(buf,0,len));
			}
			is.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 绑定银行卡
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "bankBind")
	public String bankBind(HttpServletRequest request,HttpServletResponse response, HttpSession session, ModelMap map) throws SQLException {
		String bankId = request.getParameter("bankName");
		String bankDetail = request.getParameter("bankDetail");
		String cardNo = request.getParameter("cardNo");
		String recardNo = request.getParameter("recardNo");
		String province = request.getParameter("province");
		String city = request.getParameter("city");
		map.put("msg", request.getParameter("msg"));
		User user = getCurrUser(request, response);
		if(user!=null){
		map.put("user", user);
		if(user.getRealVerifyStatus()==null||user.getRealVerifyStatus()!=1){
			map.put("msg", "请先进行实名认证");
			return "redirect:/myhome/realname.html";
		}
		//查询所有的银行卡
		List<Bank> bankList = bankService.queryAllBank();
		map.put("bankList", bankList);

		//查询省份
		List<City> CityList = cityService.queryCityListByParent(0L); 
		map.put("provinceList", CityList);
		if(!CityList.isEmpty()){
			List<City> CityList1 = cityService.queryCityListByParent(CityList.get(0).getCityId()); 
			map.put("cityList", CityList1);
		}
		
		String flag = request.getParameter("flag");
		String msg = "";
		if (flag == null) {// 查询已经绑定的银行卡(跳转到绑定银行卡页面)
			List<BankCard> list = bankCardService.selectAllBankCardByUser(user.getId());
			if(!list.isEmpty()){
				//list.get(0).setCardNo(TextUtil.hideUsernameChar(list.get(0).getCardNo()));
				map.put("bankCard", list.get(0));
			}
//			session.setAttribute("bankCardList", list);
		}else if(flag.equals("add")) {// 添加绑定 银行卡
				if ((bankId == null || "".equals(bankId.trim()))
						|| (bankDetail == null || "".equals(bankDetail.trim()))
						|| (cardNo == null || "".equals(cardNo.trim()))
						|| (recardNo == null || "".equals(recardNo.trim()))
						|| (province == null || "".equals(province.trim()))
						|| (city == null || "".equals(city.trim()))) {
					msg = "信息填写不完整";
				} else {
					// 输入的两次卡号不一致
					if (!cardNo.equals(recardNo)) {
						msg = "输入的两次卡号不一致";
						map.put("msg", msg);
						return "myhome/bindCard";
					}

					// 银行卡号正则验证：16-19位数字
					Pattern pattern = Pattern.compile("^(\\d{16}|\\d{17}|\\d{18}|\\d{19})$");
					Matcher matcher = pattern.matcher(cardNo);
					if (!matcher.matches()) {
						msg = "请输入正确的银行卡号";
						map.put("msg", msg);
						return "myhome/bindCard";
					}

					// 查询该银行卡是否已经被别人绑定
					if (bankCardService.getBankCardByCardNo(cardNo) != null) {
						msg = "该银行卡已经被绑定,请核对";
						map.put("msg", msg);
						return "myhome/bindCard";
					}
					
					// 查询该用户是否已经绑定银行卡
					if (bankCardService.getUserBankCard(user.getId()) != null) {
						msg = "您已绑定过一张银行卡";
						map.put("msg", msg);
						return "myhome/bindCard";
					}
					
					if(bankList.get(0).getBankName()!=null){
						Bank b =bankService.queryAllBank(Long.valueOf(bankId));
						map.put("b", b);
						
						BankCard bankCard = new BankCard();
						bankCard.setBankId(b.getId());
						bankCard.setBankName(b.getBankName());
						bankCard.setBankDetail(bankDetail);
						bankCard.setCardNo(cardNo);
						bankCard.setStatus(0);
						bankCard.setProvince(province);
						bankCard.setCity(city);
						bankCard.setUserId(user.getId());
						bankCard.setCreatedAt(System.currentTimeMillis() / 1000);
						
						bankCardService.addBankCardInTransaction(user,bankCard);
					}
					user.setCardBindingStatus(1);
					// 更新session里的user
					session.setAttribute("userInSession", user);
					// 返回该用户绑定的所有银行卡列表
					List<BankCard> list = bankCardService.selectAllBankCardByUser(user.getId());
					if(!list.isEmpty()){
						//list.get(0).setCardNo(TextUtil.hideUsernameChar(list.get(0).getCardNo()));
						map.put("bankCard", list.get(0));
					}
					
					msg = "添加成功";
					map.put("msg", msg);
				}
			}else if(flag.equals("upd")){
				// 返回该用户绑定的所有银行卡列表
//				List<BankCard> listT = bankCardService.selectAllBankCardByUser(user.getId());
				String id=request.getParameter("id");
				BankCard bankcard=bankCardService.queryBankCardById(id);
				if(bankcard!=null){
				if ((bankId == null || "".equals(bankId.trim()))
						|| (bankDetail == null || "".equals(bankDetail.trim()))
						|| (cardNo == null || "".equals(cardNo.trim()))
						|| (recardNo == null || "".equals(recardNo.trim()))
						|| (province == null || "".equals(province.trim()))
						|| (city == null || "".equals(city.trim()))
						) {
					msg = "信息填写不完整";
				} else {
					// 输入的两次卡号不一致
					if (!cardNo.equals(recardNo)) {
						msg = "输入的两次卡号不一致";
						map.put("msg", msg);
						map.put("bankCard", bankcard);
						map.put("flag", "toUpd");
						return "myhome/bindCard";
					}

					// 银行卡号正则验证：16或19位数字
					Pattern pattern = Pattern.compile("^(\\d{16}|\\d{19})$");
					Matcher matcher = pattern.matcher(cardNo);
					if (!matcher.matches()) {
						msg = "请输入正确的银行卡号";
						map.put("msg", msg);
						map.put("bankCard", bankcard);
						map.put("flag", "toUpd");
						return "myhome/bindCard";
					}

					// 查询该银行卡是否已经被别人绑定
					if(!cardNo.equals(bankcard.getCardNo())){
						if (bankCardService.getBankCardByCardNo(cardNo) != null) {
							msg = "该银行卡已经被绑定,请核对";
							map.put("msg", msg);
							map.put("bankCard", bankcard);
							map.put("flag", "toUpd");
							return "myhome/bindCard";
						}
					}
					
					if(bankList.get(0).getBankName()!=null){
						Bank b =bankService.queryAllBank(Long.valueOf(bankId));
						map.put("b", b);
//						BankCard bankCard = new BankCard();
						bankcard.setBankId(b.getId());
						bankcard.setBankName(b.getBankName());
						bankcard.setBankDetail(bankDetail);
						bankcard.setCardNo(cardNo);
						bankcard.setStatus(0);
						bankcard.setProvince(province);
						bankcard.setCity(city);
						bankcard.setUserId(user.getId());
						bankcard.setUpdatedAt(System.currentTimeMillis() / 1000);
						bankCardService.updateBankCard(bankcard);
					}
				}

					// 返回该用户绑定的所有银行卡列表
					List<BankCard> list = bankCardService.selectAllBankCardByUser(user.getId());
					if(!list.isEmpty()){
						//list.get(0).setCardNo(TextUtil.hideUsernameChar(list.get(0).getCardNo()));
						map.put("bankCard", list.get(0));
					}
					
					msg = "修改成功";
					map.put("msg", msg);
				}
			}else if(flag.equals("del")){
				// 查询该用户是否已经绑定银行卡
				if (bankCardService.getUserBankCard(user.getId()) != null) {
					bankCardService.delCardByUserIdInTransaction(user.getId(), session, user);
				}
			}else if(flag.equals("toUpd")){
				// 返回该用户绑定的所有银行卡列表
				List<BankCard> list = bankCardService.selectAllBankCardByUser(user.getId());
				if(!list.isEmpty()){
					//list.get(0).setCardNo(TextUtil.hideUsernameChar(list.get(0).getCardNo()));
					map.put("bankCard", list.get(0));
					map.put("flag", flag);
				}
			}
			return "myhome/bindCard";
		}else{
			return "redirect:/tologin.html";
		}
	}
	
	   
	/**      
	 * @desc 用途描述: 跳转至头像上传页面
	 * @param request
	 * @param response
	 * @param map
	 * @return 返回说明:
	 * @exception 内部异常说明:
	 * @throws 抛出异常说明:
	 * @author gwx
	 * @version 1.0      
	 * @created 2016-2-19 下午2:44:50 
	 * @mod 修改描述:
	 * @modAuthor 修改人:
	    
	 */
	@RequestMapping("myhome/toUploadPic")
	public String toUploadPic(HttpServletRequest request,
			HttpServletResponse response, ModelMap map){
		User user = this.getCurrUser(request, response);
		if(user!=null){
			map.addAttribute("user", user);
		}else{
			return "redirect:/tologin.html";
		}
		return "myhome/avatar_upload";
	}
	

	/**
	 * 上传头像
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "uploadPic")
	public String uploadPic(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap map)
			throws Exception {
		User user = getCurrUser(request, response);
		if (user != null) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获得第一张图片（根据前台的file name属性得到上传的文件）
		MultipartFile imgFile = multipartRequest.getFile("imgFile");
        if (imgFile != null && imgFile.getSize() > 2097152) {
        	map.addAttribute("msg", "图片大小大于2M");
        	return "myhome/avatar_upload";
        }
		// 定义一个数组用于保存可上传的文件类型
		List<String> fileTypes = new ArrayList<String>();
		fileTypes.add("jpg");
		fileTypes.add("jpeg");
		fileTypes.add("png");

		// 保存第一张图片
		if (!(imgFile.getOriginalFilename() == null || "".equals(imgFile.getOriginalFilename()))) {
			UploadUtil uploadUtil = new UploadUtil();

			String path = request.getRealPath("/");

			File file = uploadUtil.getFile(imgFile, fileTypes, path, "", "");
			if(file != null ) {
				String strurl = file.getPath();
				String str = strurl.substring(0, strurl.lastIndexOf('.'));
				String strEnd=strurl.substring(strurl.lastIndexOf('.')+1,strurl.length());
				String bigUrl="";
				String smlUrl="";
				if("jpg".equals(strEnd.trim().toLowerCase())){
					bigUrl=UploadUtil.saveImageAsJpg(file.getPath(), str + "_120x120.jpg",
							120, 120, true);
					smlUrl=UploadUtil.saveImageAsJpg(file.getPath(), str + "_80x80.jpg", 80,
							80, true);
				}else if("png".equals(strEnd.trim().toLowerCase())){
					bigUrl=UploadUtil.saveImageAsJpg(file.getPath(), str + "_120x120.png",
							120, 120, true);
					smlUrl=UploadUtil.saveImageAsJpg(file.getPath(), str + "_80x80.png", 80,
							80, true);
				}else if("jpeg".equals(strEnd.trim().toLowerCase())){
					bigUrl=UploadUtil.saveImageAsJpg(file.getPath(), str + "_120x120.jpeg",
							120, 120, true);
					smlUrl=UploadUtil.saveImageAsJpg(file.getPath(), str + "_80x80.jpeg", 80,
							80, true);
				}
				if(file.exists()){
					file.delete();
				}
				
				
					if(!"".equals(bigUrl)){
						bigUrl = bigUrl.replaceAll("\\\\", "/");
						int index = bigUrl.indexOf("/data");
						bigUrl = bigUrl.substring(index);
					}
					if(!"".equals(smlUrl)){
						smlUrl = smlUrl.replaceAll("\\\\", "/");
						int index = smlUrl.indexOf("/data");
						smlUrl = smlUrl.substring(index);
					}
//					String filePath = file.getPath();
					user.setLitpic(smlUrl);
					userService.userUpdate(user);
					user.setBigPic(bigUrl);
//					user.setOnPic(user.getLitpic().substring(0,
//							user.getLitpic().lastIndexOf("."))
//							+ "_100x100.jpg");
					user.setSmallPic(smlUrl);
					session.setAttribute("userInSession", user);
					map.addAttribute("msg", "图像上传成功");
				}
			} else {
				map.addAttribute("msg", "请上传头像图片");
			}
		}else{
			return "redirect:/tologin.html";
		}
		return "myhome/avatar_upload";
	}

	/**
	 * 在绑定银行卡的时候未实名认证，则跳转到实名认证页
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "toRealNamePage")
	public String toRealNamePage(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		return "myhome/user-auth";
	}


	/**
	 * 跳转到提现页
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value = "myhome/withdraw")
	public String toWithDrawPage(HttpServletRequest request,HttpServletResponse response, HttpSession session, ModelMap map) throws SQLException {

		User user = getCurrUser(request, response);
		// 拿到登录用户的账户，在提现页面显示 用
		UserAccount userAccount = accountService.getUserAccount(user.getId());
		map.put("userAccount", userAccount);
		map.put("user", user);
		List<BankCard> bclist=bankCardService.selectAllBankCardByUser(user.getId());
		map.put("msg", request.getParameter("msg"));
		if(user.getCardBindingStatus()==null||user.getCardBindingStatus()!=1||bclist.isEmpty()){
				map.put("msg", "请先绑定银行卡");
				return "redirect:/bankBind.html";
		}else if(user.getRealVerifyStatus()==null||user.getRealVerifyStatus()!=1){
				map.put("msg", "请先进行实名认证");
				return "redirect:/myhome/realname.html";
		}else if(!bclist.isEmpty()){
			bclist.get(0).setCardNo(TextUtil.hideUsernameChar(bclist.get(0).getCardNo()));
			map.put("bankCard", bclist.get(0));
		}
		return "myhome/withdraw";
	}
	
	
	/**
	 * 用户中心充值
	 * @Description:  TODO
	 * @param:        @param map
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param nid
	 * @param:        @return
	 * @param:        @throws Exception   
	 * @return:       String   
	 * @throws
	 */
	@RequestMapping("myhome/charge")
	@Transactional(rollbackFor={Exception.class})
	public String charge(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		User user = getCurrUser(request, response);
		if(user!=null){
			if(user.getRealVerifyStatus()==null||user.getRealVerifyStatus()!=1){
				map.addAttribute("msg", "请先进行实名认证");
				return "redirect:/myhome/realname.html";
			}
			map.addAttribute("user", user);
			UserAccount ua = accountService.getUserAccount(user.getId());
			map.addAttribute("msg", request.getParameter("msg"));
			map.addAttribute("ua", ua);
			return "myhome/charge";
		}else{
			return "redirect:/tologin.html";
		}
	}
	

	/**
	 * 资金记录
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param map
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value = "moneyLog")
	public String moneyLog(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap map)
			throws Exception {
		User user = getCurrUser(request, response);

		// 获取提交过来的参数
		String type = request.getParameter("type");// 资金类型
		String startTime = request.getParameter("startTime");// 开始时间
		String endTime = request.getParameter("endTime");// 结束时间
		String page = request.getParameter("page");// 当前第几页
		// 把日期转换为时间戳
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String start;
		String end;
		Date startDate;
		Date endDate;
		Long startLong;
		Long endLong;

		if (StringUtils.isNotEmpty(startTime)) {
			start = startTime + " 00:00:00";
			startDate = format.parse(start);
			startLong = startDate.getTime();
		} else {
			startLong = 0L;// 如果startTime为空则默认为0
		}
		if (StringUtils.isNotEmpty(endTime)) {
			end = endTime + " 23:59:59"; // 结束时间为结束日期的晚上23:59:59
			endDate = format.parse(end);
			endLong = endDate.getTime();
		} else {
			endLong = System.currentTimeMillis();// 如果endTime为空则默认为当前时间
		}
		// 用户前台回显
		map.addAttribute("startTime", startTime);
		map.addAttribute("endTime", endTime);

		if (null != user) {
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);// 设置分页大小PageSize

			Map<String, String> params = getParamMap(request);
			if (StringUtil.isNotEmpty(page)) {
				pageRequest.setPageNumber(Integer.valueOf(page));// 当前第几页PageNumber
			}
			if (StringUtil.isNotEmpty(type)) {
				params.put("type", type);
			}
			if (StringUtils.isNotEmpty(startTime)) {
				params.put("startTime", startLong.toString());
			}
			if (StringUtils.isNotEmpty(endTime)) {
				params.put("endTime", endLong.toString());
			}

			params.put("userId", String.valueOf(user.getId()));
			pageRequest.setFilters(params);
			Page<UserAccountLog> accountLog = userAccountLogService
					.queryUserAccountLogByUserId(pageRequest);
			map.addAttribute("accountLogs", accountLog);// 结果集
			map.addAttribute("page", pageRequest.getPageNumber());// 当前第几页
		}
		return "moneyLog";
	}
	
	/**
	 * 交易明细
	 * @Title: moneyLog 
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("myhome/moneyLog")
	public String moneyLog(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception {
		 User user = new User();
	        user = this.getCurrUser(request, response);
	        if(user!=null){
	            long user_id=user.getId();
	            PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
	            populate(pageRequest, request);
	            pageRequest.setPageSize(10);
	            Map<String, String> params = getParamMap(request);
	            String type=params.get("type");
	            if(StringUtil.isNotEmpty(params.get("page"))){
	                pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
	            }
	            if(StringUtil.isNotEmpty(params.get("type"))){
	                params.put("type", params.get("type"));
	            }
	            params.put("userId",String.valueOf(user_id));
	            pageRequest.setFilters(params);
	            Page<UserAccountLog> accountpage=borrowService.getAccountLogByUserId(pageRequest);
	            map.addAttribute("type",type);
	            map.addAttribute("page",pageRequest.getPageNumber());
	            map.addAttribute("borrowPage", accountpage);
	            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(accountpage.getTotalCount(), accountpage.getPageSize()));
	            if(StringUtil.isNotEmpty(params.get("type"))){
	                    if(params.get("type").equals("1")){
	                        map.addAttribute("mt", 21);//充值记录
	                    }else{
	                        map.addAttribute("mt", 22);//提现记录
	                    }
	            }else{
	                map.addAttribute("mt", 23);//交易明细
	            }
	        }
		return "myhome/moneyLog";
	}

	/**
	 * 推广联盟
	 * @param request
	 * @param response
	 * @param session
	 * @param map
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping(value = "myhome/tuiguang")
	public String tuiguang(HttpServletRequest request,HttpServletResponse response, HttpSession session, ModelMap map) throws Exception {
		User user = getCurrUser(request, response);
		int count = 0;// 推荐总人数
		int shiming = 0;// 查询实名认证人数
		int sumCount = 0;// 已充值人数
		int touziCount = 0;// 已投资人数
		Double money=0d;
		
		if (null != user) {
			//查询总奖励金额
			money = hongbaoDao.queryMoneyByTuiguang(user.getId());
			
			count = userService.queryByInviteUserid(String.valueOf(user.getId()));
			map.addAttribute("count", count);
			// 查询实名认证人数
			shiming = userService.queryByShiMing(String.valueOf(user.getId()));
			map.addAttribute("shiming", shiming);
			
			//查询该用户邀请的所有用户
			List<User> users1 = userService.queryListOfInvite(String.valueOf(user.getId()));
			for (User user0 : users1) {
				int sumCountonece = accountService.getUserRechargeCount(Integer.valueOf(user0.getId().toString()));
				if (sumCountonece > 0) {
					sumCount++;
				} 
				int touziCountonece = userAccountLogService.getAccountLogCountByUserId(Integer.valueOf(user0.getId().toString()));
				if (touziCountonece > 0) {
					touziCount++;
				}
			}
			
			//分页查询
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(5);
			Map<String, String> params = getParamMap(request);
			if (StringUtil.isNotEmpty(params.get("page"))) {
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			params.put("userId", String.valueOf(user.getId()));
			pageRequest.setFilters(params);
			Page<User> usePage = userService.getUserByInviteUserid(pageRequest);
			List<User> users = usePage.getResult();
			for (User user2 : users) {
				int sumCountonece = accountService.getUserRechargeCount(Integer.valueOf(user2.getId().toString()));
				if (sumCountonece > 0) {
					user2.setIsRecharge(1);
				} else {
					user2.setIsRecharge(0);
				}
				int touziCountonece = userAccountLogService.getAccountLogCountByUserId(Integer.valueOf(user2.getId().toString()));
				if (touziCountonece > 0) {
					user2.setIsTouzi(1);
				} else {
					user2.setIsTouzi(0);
				}
			}
			usePage.setResult(users);

			map.addAttribute("money", money);
			map.addAttribute("sumCount", sumCount);
			map.addAttribute("touziCount", touziCount);
			map.addAttribute("user", user);
			map.addAttribute("page", pageRequest.getPageNumber());
			map.addAttribute("borrowPage", usePage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(usePage.getTotalCount(), usePage.getPageSize()));
		}
		return "myhome/tuiguang";
	}
	
    /**
     * 配资记录列表
     * @param map
     * @param request
     * @param response
     * @return
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
   @RequestMapping("myhome/recordlog")
   public String planlog(ModelMap map,HttpServletRequest request,HttpServletResponse response) throws Exception{
       User user = new User();
        user = this.getCurrUser(request, response);
        Map<String,String> param = getParamMap(request);
        String type=param.get("type");//资金类型
        String startTime=param.get("startTime");//开始时间
        String endTime=param.get("endTime");//结束时间
        map.addAttribute("startTime", startTime);
        map.addAttribute("endTime", endTime);
        map.addAttribute("type", type);
        
        map.addAttribute("msg", param.get("msg"));
        
        if(null!=user){
            PageRequest<Map<String, String>>  pageRequest=new PageRequest<Map<String,String>>();
            populate(pageRequest, request);
            pageRequest.setPageSize(10);
            Map<String, String> params = getParamMap(request);
            if(StringUtil.isNotEmpty(params.get("page"))){
                pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
            }
            if(StringUtil.isNotEmpty(type)){
                params.put("type", type);
            }
            if(StringUtils.isNotEmpty(startTime)){
                params.put("startTime", startTime);
            }
            if(StringUtils.isNotEmpty(endTime)){
                params.put("endTime", endTime);
            }
            params.put("userId",String.valueOf(user.getId()));
            pageRequest.setFilters(params);
            Page<PlanRecord> planRecordList=planRecordService.getPageRecordList(pageRequest);
            map.addAttribute("page",pageRequest.getPageNumber());
            map.addAttribute("borrowPage", planRecordList);
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(planRecordList.getTotalCount(), planRecordList.getPageSize()));
        }
        return "myhome/capitalList";
   }
   
   
   /**
    * 追加保证金记录列表
    * @Title: appendInsurelog 
    * @Description: TODO
    * @param @param map
    * @param @param request
    * @param @param response
    * @param @return
    * @param @throws Exception 设定文件 
    * @return String 返回类型 
    * @throws
    */
  @RequestMapping("myhome/appendInsurelog")
  public String appendInsurelog(ModelMap map,HttpServletRequest request,HttpServletResponse response) throws Exception{
       User user = this.getCurrUser(request, response);
       Map<String,String> param = getParamMap(request);

       String startTime=param.get("startTime");//开始时间
       String endTime=param.get("endTime");//结束时间
       map.addAttribute("startTime", startTime);
       map.addAttribute("endTime", endTime);
       
       map.addAttribute("msg", param.get("msg"));

       PageRequest<Map<String, String>>  pageRequest=new PageRequest<Map<String,String>>();
       populate(pageRequest, request);
       pageRequest.setPageSize(10);
       Map<String, String> params = getParamMap(request);
       if(StringUtil.isNotEmpty(params.get("page"))){
           pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
       }

       params.put("userId",String.valueOf(user.getId()));
       pageRequest.setFilters(params);
       try {
           Page<PlanAppendInsure> appendpage=planService.getPlanAppendInsurePage(pageRequest);
           map.addAttribute("page",pageRequest.getPageNumber());
           map.addAttribute("appendpage", appendpage);
           map.addAttribute("totalPage", PageUtils.computeLastPageNumber(appendpage.getTotalCount(), appendpage.getPageSize()));           
    } catch (Exception e) {
        e.printStackTrace();
    }

       return "myhome/appendInsurelog";
  }
  /**
   * 删除配资记录 根据ID(将status修改为-1)
   * @param map
   * @param request
   * @param response
   * @return
   */
  @RequestMapping("myhome/deleteRecordLog")
  public String deleteRecordLog(ModelMap map,HttpServletRequest request,HttpServletResponse response){
      Map<String,String> param = getParamMap(request);
      String prid=param.get("prid");
      if(null==prid||"".equals(prid)){
          map.addAttribute("msg", "此记录不存在");
          
      }else{
      PlanRecord record=planService.queryRecordById(Long.parseLong(prid));
      if(null==record){
          map.addAttribute("msg", "此记录不存在");
      }
      else if(record.getStatus()==0||record.getStatus()==1){
          map.addAttribute("msg", "此记录不能被删除");
      }else{
          planService.deletePlanRecordLog(Integer.parseInt(prid)); 
      }
      }
      return "redirect:recordlog.html";
  }	
  /**
	 * 正在投标的记录，但是没有满标审核
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping("myhome/tblist")
	public String tblist(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			long user_id=user.getId();
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);
			Map<String, String> params = getParamMap(request);
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			params.put("userId",String.valueOf(user_id));
			pageRequest.setFilters(params);
			Page<BorrowTender> borrowpage=borrowService.getInvestTenderingListByUserid(pageRequest);
			map.addAttribute("page",pageRequest.getPageNumber());
			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
			 //正在投标的项目
			map.addAttribute("mt", 31);
		}
		return "myhome/integration";
	}
	
	/**
	 * 收益明细toubiao
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("myhome/toubiao")
	public String toubiao(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			long user_id=user.getId();

			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			String numberstr =(String) request.getParameter("topNext");
			pageRequest.setPageSize(10);
			Map<String, String> params = getParamMap(request);
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			params.put("userId",String.valueOf(user_id));
			pageRequest.setFilters(params);
			Page<BorrowTender> borrowpage=borrowService.getInvestTenderListByUserid(pageRequest);
			map.addAttribute("page",pageRequest.getPageNumber());
			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
			map.addAttribute("mt", 35);//借出明细帐
		}
		return "myhome/user-table-toubiao";
	}
	/**
	 * 正在收款的项目，已还清项目
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("myhome/sklist")
	public String sklist(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			long user_id=user.getId();
			String type=request.getParameter("type");
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			String numberstr =(String) request.getParameter("topNext");
			pageRequest.setPageSize(10);
			Map<String, String> params = getParamMap(request);
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			params.put("userId",String.valueOf(user_id));
			params.put("type", type);
			pageRequest.setFilters(params);
			Page<BorrowTender> borrowpage=borrowService.getSuccessListByUserid(pageRequest);
			map.addAttribute("page",pageRequest.getPageNumber());
			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
			if(type.equals("1")){
				 //正在收款的项目
				map.addAttribute("mt", 32);
			}else if(type.equals("2")){
				 //已还清项目
				map.addAttribute("mt", 36);
			}
			map.addAttribute("type", type);
		}
		
		return "myhome/user-table-sklist";
	}
	/**
	 * 用户中心未收款列表
	 * @Title: collect 
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws IllegalAccessException
	 * @param @throws InvocationTargetException 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("myhome/wsklist")
	public String collect(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			long user_id=user.getId();
			int status=NumberUtil.getInt(request.getParameter("status"));
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);
			Map<String, String> params = getParamMap(request);
			params.put("userId",String.valueOf(user_id));
			params.put("status", String.valueOf(status));
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			
			pageRequest.setFilters(params);
			Page<BorrowCollection> borrowpage=borrowService.getCollectionList(pageRequest);
			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
			if(status==0){
				//未收款明细帐
				map.addAttribute("mt", 33);
			}else if(status==2){
			//已收款明细帐
				map.addAttribute("mt", 34);
			}
			map.addAttribute("status", status);
		}
		return "myhome/user-table-grid";
	}
	
	/**
	 * 我的融资记录
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("myhome/tender_list")
	public String tenderList(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			
		}
		return "tender-list";
	}
	
	
	
	/**
	 * 还款操作
	 * @return
	 */
	@RequestMapping("myhome/repay")
	public String repay(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		boolean isOk=true;
		String checkMsg="";
		long repayid=NumberUtil.getLong(request.getParameter("id"));
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			long user_id=user.getId();
			BorrowRepayment repay=borrowService.getRepayment(repayid);
			UserAccount act=userAccountService.getAccount(user_id);
			if(repay==null){
				map.addAttribute("errmsg","还款失败，订单不存在!");
				return "redirect:jieBorrow.html?type=repayment";
			}
			if(repay.getRepaymentAccount()>act.getMoneyUsable()){
				map.addAttribute("errmsg","可用余额不足！");
				return "redirect:repinfo.html";
			}
			UserAccountLog log=new UserAccountLog();
			log.setUserId(user_id);
			//log.setType(Constant.FREEZE);
			
			
			try {
			String message = borrowService.doRepay(repay,act,log);
			map.addAttribute("errmsg",message);
   
			
			} catch (Exception e) {
				isOk=false;
				checkMsg=e.getMessage();
				e.getMessage();
				e.printStackTrace();    
		        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			if (isOk) {
				return "redirect:jieBorrow.html?type=repayment";
			}else{
			}
		}
		return "redirect:repinfo.html";
	}
	/**
	 * 我的红包
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping("myhome/hongbao")
	public String hongbao(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException{
		
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);
			Map<String, String> params = getParamMap(request);
			params.put("userId",String.valueOf(user.getId()));
//			params.put("status", params.get("status"));
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			pageRequest.setFilters(params);
			Page<Hongbao> borrowpage=userService.getHongbaoBypage(pageRequest);
			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
//			map.addAttribute("status", params.get("status"));
		}
		return "myhome/hongbao";
	}
	
	@RequestMapping("getCityByParent")
	public void getCityByParent(Long parentId,HttpServletResponse response) throws IOException {
        response.setContentType("text/json"); 
        response.setCharacterEncoding("UTF-8"); 
		PrintWriter out = response.getWriter();
		//查询城市
		List<City> CityList = cityService.queryCityListByParent(parentId);
		String json = JSONArray.toJSONString(CityList);
		out.print(json);
		out.flush();
		out.close();
     }
	
	/**
	 * 实名认证
	 * @Description:  
	 * @param:        @param map
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param nid
	 * @param:        @return
	 * @param:        @throws Exception   
	 * @return:       String   
	 * @throws
	 */
	@RequestMapping("myhome/companyAuth")
	public String companyAuth(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws MalformedURLException{
		
		User user = getCurrUser(request, response);
		if(user==null){
			return "redirect:tologin.html";
		}
		map.addAttribute("user", user);
		
		return "myhome/company-auth";

	}
}