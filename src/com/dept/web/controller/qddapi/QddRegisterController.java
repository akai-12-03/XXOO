package com.dept.web.controller.qddapi;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dept.web.context.Global;
import com.dept.web.controller.WebController;
import com.dept.web.controller.qddapi.util.Common;
import com.dept.web.dao.model.User;
import com.dept.web.general.util.HttpClientUtil;
import com.dept.web.general.util.RsaHelper;
import com.dept.web.service.UserService;
/**
 * 托管平台：钱多多<用户:认证开户>
 */
@Controller
public class QddRegisterController extends WebController {

	@Autowired
	private UserService userService;
//	@Autowired
//	private AccountService accountService;

	private static final Logger LOGGER = Logger.getLogger(QddRegisterController.class);
	protected String AccountType="";
	protected String RegisterType = "";
	protected String AccountNumber = "";
	protected String Mobile = "";
	protected String Email = "";
	protected String RealName = "";
	protected String IdentificationNo = "";
	protected String Image1 = "";
	protected String Image2 = "";
	protected String AuthFee = "";
	protected String AuthState = "";
	protected String LoanPlatformAccount;

	protected String Remark1 = "";
	protected String Remark2 = "";
	protected String Remark3 = "";

	// private final int antistate = 0;
	/**
	 * 注册提交
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("qddApi/toRegister")
	public String toRegister(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = getCurrUser(request, response);
		if (user != null) {
			Map<String, String> params = getParamMap(request);
			String realname = params.get("realname");
			String phone = user.getMobile();
			String cardId = params.get("cardId");
			String organization = params.get("organization");
			//String accountType=params.get("AccountType");
			String userType=params.get("userType");
//			user.setUserType(Integer.valueOf(userType));//1个人认证，2企业认证
			if(StringUtils.isNotEmpty(userType))
			{
				if ("2".equals(userType)) {
					AccountType="1";
					user.setOrganization(organization);
					IdentificationNo =organization;
				} else {
					IdentificationNo = cardId;
					AccountType="";
				}
			}
			else
			{
				IdentificationNo = cardId;
			}
			userService.userUpdate(user);

			PlatformMoneymoremore=Global.getValue("qdd_PlatformMoneymoremore");
			RegisterType = "2";
			Mobile = phone;
			RealName = realname;
			
			LoanPlatformAccount = String.valueOf(user.getUsername());
			RandomTimeStamp = "";

			Map<String, String> paramap = new HashMap<String, String>();
			paramap.put("RegisterType", RegisterType);
			paramap.put("AccountType", AccountType);

			paramap.put("Mobile", Mobile);

			paramap.put("RealName", RealName);
			paramap.put("IdentificationNo", IdentificationNo);

			paramap.put("Image1", Image1);
			paramap.put("Image2", Image2);

			paramap.put("LoanPlatformAccount", LoanPlatformAccount);
			paramap.put("PlatformMoneymoremore", PlatformMoneymoremore);


			paramap.put("RandomTimeStamp", RandomTimeStamp);

			paramap.put("Remark1", Remark1);
			paramap.put("Remark2", Remark2);
			paramap.put("Remark3", Remark3);
			ReturnURL = Global.getValue("qdd_notifyUrl") + "/qddApi/backRegister.html";
			NotifyURL = Global.getValue("qdd_notifyUrl") + "/qddApi/backRegisterFwq.html";
			
			SubmitURLPrefix=Global.getValue("qdd_submitUrl");

			paramap.put("ReturnURL", ReturnURL);
			paramap.put("NotifyURL", NotifyURL);

			String dataStr = RegisterType + AccountType + Mobile 
					+ RealName + IdentificationNo + Image1 + Image2
					+ LoanPlatformAccount + PlatformMoneymoremore
					+ RandomTimeStamp + Remark1 + Remark2 + Remark3 + ReturnURL
					+ NotifyURL;
			// 签名
			// MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String privatekey = Common.privateKeyPKCS8;
			SignInfo = rsa.signData(dataStr, privatekey);

			paramap.put("SignInfo", SignInfo);
			
			StringBuffer sb=new StringBuffer();
			for(Map.Entry<String,String> entry: paramap.entrySet())
			{
				sb.append("<input  type='hidden' name='"+entry.getKey()+"' value='"+entry.getValue()+"' />\n");
			}
			System.out.print(sb.toString());
			String myProject=Common.myProject;
			if(myProject.equals("test"))
			{
				SubmitURL = Global.getValue("qdd_submitUrl")+"/loan/toloanregisterbind.action";
			}
			else
			{
				SubmitURL = "https://register."+Global.getValue("qdd_submitUrl")+"/loan/toloanregisterbind.action";
			}
			
			
			HttpClientUtil.sendHtml(SubmitURL, paramap, response);
		} else {
			return "redirect:login.html";
		}
		return null;
	}

	/**
	 * 注册返回页面回跳
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("qddApi/backRegister")
	public String backRegister(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AccountType = request.getParameter("AccountType");
		AccountNumber = request.getParameter("AccountNumber");
		Mobile = request.getParameter("Mobile");
		Email = request.getParameter("Email");
		RealName = request.getParameter("RealName");
		IdentificationNo = request.getParameter("IdentificationNo");
		LoanPlatformAccount = request.getParameter("LoanPlatformAccount");
		MoneymoremoreId = request.getParameter("MoneymoremoreId");
		PlatformMoneymoremore = request.getParameter("PlatformMoneymoremore");
		AuthFee = request.getParameter("AuthFee");
		AuthState = request.getParameter("AuthState");
		RandomTimeStamp = request.getParameter("RandomTimeStamp");
		Remark1 = request.getParameter("Remark1");
		Remark2 = request.getParameter("Remark2");
		Remark3 = request.getParameter("Remark3");
		ResultCode = request.getParameter("ResultCode");
		Message = request.getParameter("Message");
		SignInfo = request.getParameter("SignInfo");
		ReturnTimes=request.getParameter("ReturnTimes");
		
		String errorMsg = "第三方资金托管开通";

		RsaHelper rsa = RsaHelper.getInstance();
		String publickey = Common.publicKey;
		String dataStr = AccountType + AccountNumber + Mobile + Email
				+ RealName + IdentificationNo + LoanPlatformAccount
				+ MoneymoremoreId + PlatformMoneymoremore + AuthFee + AuthState
				+ RandomTimeStamp + Remark1 + Remark2 + Remark3 + ResultCode;
	

		// 签名
		boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
				publickey);
		this.verifySignature = Boolean.toString(verifySignature);
		System.out.println(this.verifySignature);

		User user = userService.queryUserByName(LoanPlatformAccount);
		map.addAttribute("errorMsg", errorMsg);
		if (user != null) {
			String content = "第三方资金托管开通";
			if (verifySignature) {
				LOGGER.info("开户前台返回ResultCode="+ResultCode);
				if (("88").equals(ResultCode)) {
							errorMsg = content + "成功";
							map.addAttribute("MoneymoremoreId", MoneymoremoreId);
							return "redirect:loanAuthorize.html?MoneymoremoreId="+MoneymoremoreId;
						} else {
							errorMsg = content + "失败，"+Message;
						}
							map.addAttribute("errorMsg", errorMsg);
					}
		}else{
			map.addAttribute("errorMsg", "用户"+LoanPlatformAccount+"未找到");
		}
		return "errorMsg";
	}

	/**
	 * 注册返回服务器回跳
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("qddApi/backRegisterFwq")
	public void backRegisterFwq(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AccountType = request.getParameter("AccountType");
		AccountNumber = request.getParameter("AccountNumber");
		Mobile = request.getParameter("Mobile");
		Email = request.getParameter("Email");
		RealName = request.getParameter("RealName");
		IdentificationNo = request.getParameter("IdentificationNo");
		LoanPlatformAccount = request.getParameter("LoanPlatformAccount");
		MoneymoremoreId = request.getParameter("MoneymoremoreId");
		PlatformMoneymoremore = request.getParameter("PlatformMoneymoremore");
		AuthFee = request.getParameter("AuthFee");
		AuthState = request.getParameter("AuthState");
		RandomTimeStamp = request.getParameter("RandomTimeStamp");
		Remark1 = request.getParameter("Remark1");
		Remark2 = request.getParameter("Remark2");
		Remark3 = request.getParameter("Remark3");
		ResultCode = request.getParameter("ResultCode");
		Message = request.getParameter("Message");
		SignInfo = request.getParameter("SignInfo");
		ReturnTimes=request.getParameter("ReturnTimes");

		RsaHelper rsa = RsaHelper.getInstance();
		String publickey = Common.publicKey;
		String dataStr = AccountType + AccountNumber + Mobile + Email
				+ RealName + IdentificationNo + LoanPlatformAccount
				+ MoneymoremoreId + PlatformMoneymoremore + AuthFee + AuthState
				+ RandomTimeStamp + Remark1 + Remark2 + Remark3 + ResultCode;

		// 签名
		boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
				publickey);
		this.verifySignature = Boolean.toString(verifySignature);
		System.out.println(this.verifySignature);

		String errorMsg = "出错了，请联系客服";
		User user = userService
				.queryUserByName(LoanPlatformAccount);
		
		System.out.println("---------------------------userId=="+user.getId());

		if (user != null) {
			String content = "第三方资金托管开通";
			if (verifySignature) {
				LOGGER.info("开户后台返回ResultCode="+ResultCode);
				if (ResultCode.equals("88")) {
						errorMsg = content + "成功";
						//更新用户信息
	   	        	     user.setRealname(URLDecoder.decode(RealName,"UTF-8"));
	   	        	     user.setIdCard(IdentificationNo);
	                     user.setRealVerifyStatus(1);
	                     user.setMoneymoremoreId(MoneymoremoreId);
	                     user.setAccountNumber(AccountNumber);
	                     if(AccountType.equals("1"))//1是企业认证
	                     {
	                    	 user.setUserType(2);//企业
	                     }
	                     else
	                     {
	                    	 user.setUserType(1);//个人
	                     }
	                     //  更新用户信息
	                     userService.userUpdate(user);
	                     map.addAttribute("msg", "实名认证成功!");
					}else{
						errorMsg = content + "失败，请联系客服";
					}
				}
			System.out.println("ResultCode:" + ResultCode);
			System.out.println("errorMsg:" + errorMsg);
			this.out(response, "SUCCESS");
		}
	}
}
