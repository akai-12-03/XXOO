package com.dept.web.controller.qddapi;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dept.web.context.Global;
import com.dept.web.controller.WebController;
import com.dept.web.controller.qddapi.util.Common;
import com.dept.web.controller.qddapi.util.RsaHelper;
import com.dept.web.dao.model.User;
import com.dept.web.general.util.HttpClientUtil;
/**
 * 托管平台：钱多多<用户账号:授权，手动转账<-->自动转账>
 */
@Controller
public class LoanAuthorizeController extends WebController{
	
	protected String MoneymoremoreId = "";
	protected String ReleaseType = "";
	protected String AuthorizeTypeOpen = "";
	protected String AuthorizeTypeClose = "";
	protected String Amount = "";
	protected String Remark1 = "";
	protected String Remark2 = "";
	protected String Remark3 = "";
	protected String AuthorizeType="";
	protected String Message;
	
	/**
	 * 生成授权信息
	 */
	@RequestMapping("qddApi/loanAuthorize")
	public String loanAuthorize(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);

		
			String moneymoremoreId=params.get("MoneymoremoreId");
			//MoneymoremoreId = "m26921";
			PlatformMoneymoremore = Global.getValue("qdd_PlatformMoneymoremore");
			//开通授权类型
			AuthorizeTypeOpen ="1,2,3";//1.投标2.还款3.二次分配审核(将所有数字用英文逗号,(,)连成一个字符串)
			
			
	
			//关闭授权类型
			AuthorizeTypeClose="";
		
			MoneymoremoreId=moneymoremoreId;
			SubmitURLPrefix=Global.getValue("qdd_submitUrl");
			
			String myProject=Common.myProject;
			if(myProject.equals("test"))
			{
				SubmitURL = SubmitURLPrefix + "/loan/toloanauthorize.action";
			}
			else
			{
				SubmitURL = "https://auth."+SubmitURLPrefix + "/loan/toloanauthorize.action";
//				SubmitURL="http://222.92.117.57/loan/toloanauthorize.action";
			}
			
			
			
			ReturnURL = Global.getValue("qdd_notifyUrl") + "/qddApi/authorizeReturn.html";
			NotifyURL = Global.getValue("qdd_notifyUrl") + "/qddApi/authorizeNotify.html";
			
			String privatekey = Common.privateKeyPKCS8;
			
			String dataStr = MoneymoremoreId + PlatformMoneymoremore + AuthorizeTypeOpen + AuthorizeTypeClose + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ReturnURL + NotifyURL;
			// 签名
//			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			SignInfo = rsa.signData(dataStr, privatekey);
			
			Map<String, String> req = new TreeMap<String, String>();
			req.put("SubmitURL", SubmitURL);
			req.put("MoneymoremoreId", MoneymoremoreId);
			req.put("PlatformMoneymoremore", PlatformMoneymoremore);
			req.put("AuthorizeTypeOpen", AuthorizeTypeOpen);
			req.put("AuthorizeTypeClose", AuthorizeTypeClose);
			req.put("RandomTimeStamp", RandomTimeStamp);
			req.put("Remark1", Remark1);
			req.put("Remark2", Remark2);
			req.put("Remark3", Remark3);
			req.put("ReturnURL", ReturnURL);
			req.put("NotifyURL", NotifyURL);
			req.put("SignInfo", SignInfo); 
			
			HttpClientUtil.sendHtml(SubmitURL,req,response);
	
			return null;
	}
	
	
	/**
	 * 接收授权页面返回信息
	 * 
	 * @return
	 */
	@RequestMapping("qddApi/authorizeReturn")
	public String authorizeReturn(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		MoneymoremoreId = params.get("MoneymoremoreId");
		PlatformMoneymoremore = params.get("PlatformMoneymoremore");
		AuthorizeTypeOpen = params.get("AuthorizeTypeOpen");
		AuthorizeTypeClose = params.get("AuthorizeTypeClose");
		AuthorizeType=params.get("AuthorizeType");
		RandomTimeStamp = params.get("RandomTimeStamp");
		Remark1 = params.get("Remark1");
		Remark2 = params.get("Remark2");
		Remark3 = params.get("Remark3");
		ResultCode = params.get("ResultCode");
		Message=params.get("Message");
		SignInfo=params.get("SignInfo");
		
		
		String publickey = Common.publicKey;
		
//		MD5 md5 = new MD5();
		RsaHelper rsa = RsaHelper.getInstance();
		String dataStr = MoneymoremoreId + PlatformMoneymoremore + AuthorizeTypeOpen  + AuthorizeTypeClose  + AuthorizeType + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ResultCode;

		
		// 签名
		boolean verifySignature = rsa.verifySignature(SignInfo, dataStr, publickey);
		System.out.println("页面返回:" + verifySignature);
		System.out.println("返回码:" + ResultCode);
		
		if(verifySignature == true&&ResultCode.equals("88")) {
			map.addAttribute("errorMsg","授权成功，可以进行投资理财了");
		} else {
			map.addAttribute("errorMsg",Message);
		}
		return "errorMsg";
	}
	
	/**
	 * 接收授权后台通知信息
	 * 
	 * @return
	 */
	@RequestMapping("qddApi/authorizeNotify")
	public void authorizeNotify(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		MoneymoremoreId = params.get("MoneymoremoreId");
		PlatformMoneymoremore = params.get("PlatformMoneymoremore");
		AuthorizeTypeOpen = params.get("AuthorizeTypeOpen");
		AuthorizeTypeClose = params.get("AuthorizeTypeClose");
		AuthorizeType=params.get("AuthorizeType");
		RandomTimeStamp = params.get("RandomTimeStamp");
		Remark1 = params.get("Remark1");
		Remark2 = params.get("Remark2");
		Remark3 = params.get("Remark3");
		ResultCode = params.get("ResultCode");
		Message=params.get("Message");
		SignInfo=params.get("SignInfo");
		
		String publickey = Common.publicKey;
		
//		MD5 md5 = new MD5();
		RsaHelper rsa = RsaHelper.getInstance();
		String dataStr = MoneymoremoreId + PlatformMoneymoremore + AuthorizeTypeOpen  + AuthorizeTypeClose  + AuthorizeType + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ResultCode;

		
		// 签名
		boolean verifySignature = rsa.verifySignature(SignInfo, dataStr, publickey);
		
		if(verifySignature)
		{
			if(ResultCode.equals("88"))
			{
				User user=userService.queryUserByMid(MoneymoremoreId);
				user.setAutoType(1);
				userService.userUpdate(user);
			}
		}
		System.out.println("后台通知:" + verifySignature);
		System.out.println("返回码:" + ResultCode);
		System.out.println("返回次数:" + ReturnTimes);
		
		this.out(response, "SUCCESS");
	}

}
