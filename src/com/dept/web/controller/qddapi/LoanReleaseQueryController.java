package com.dept.web.controller.qddapi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

@Controller
public class LoanReleaseQueryController extends WebController {
	protected String MoneymoremoreId = "";
	protected String ReleaseType = "";
	protected String OrderNo = "";
	protected String Amount = "";
	protected String Remark1 = "";
	protected String Remark2 = "";
	protected String Remark3 = "";
	protected String LoanNo = "";
	/**
	 * 生成资金释放信息
	 */
	@RequestMapping("qddApi/loanRelease")
	public String loanRelease(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		MoneymoremoreId = params.get("MoneymoremoreId");
		PlatformMoneymoremore = params.get("PlatformMoneymoremore");
		OrderNo = Common.getRandomNum(10);
		Amount = params.get("Amount");
		Remark1 = params.get("Remark1");
		Remark2 = params.get("Remark2");
		Remark3 = params.get("Remark3");
//		MoneymoremoreId = "m1";
//		PlatformMoneymoremore = "p1139";
//		OrderNo = Common.getRandomNum(10);
//		Amount = "10";
//		Remark1 = "1";
//		Remark2 = "1";
//		Remark3 = "1";
		User user = getCurrUser(request, response);
		if(user != null) {
			SubmitURLPrefix=Global.getValue("qdd_submitUrl");
			
			
			String myProject=Common.myProject;
			if(myProject.equals("test"))
			{
				SubmitURL = SubmitURLPrefix + "/loan/toloanrelease.action";
			}
			else
			{
				SubmitURL = "https://www."+SubmitURLPrefix + "/loan/toloanrelease.action";
			}
			
			
			ReturnURL = Global.getValue("qdd_notifyUrl") + "/qddApi/loanReleaseReturn.html";
			NotifyURL = Global.getValue("qdd_notifyUrl") + "/qddApi/loanReleaseNotify.html";
			
			String privatekey = Common.privateKeyPKCS8;
			if (antistate == 1)
			{
				Date d = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				RandomTimeStamp = Common.getRandomNum(2) + sdf.format(d);
			}
			
			String dataStr = MoneymoremoreId + PlatformMoneymoremore + OrderNo + Amount + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ReturnURL + NotifyURL;
			// 签名
//			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
//			if (antistate == 1)
//			{
//				dataStr = md5.getMD5Info(dataStr);
//			}
			SignInfo = rsa.signData(dataStr, privatekey);
			
			Map<String, String> req = new HashMap<String, String>();
			req.put("MoneymoremoreId", MoneymoremoreId);
			req.put("PlatformMoneymoremore", PlatformMoneymoremore);
			req.put("OrderNo", OrderNo);
			req.put("Amount", Amount);
			req.put("RandomTimeStamp", RandomTimeStamp);
			req.put("Remark1", Remark1);
			req.put("Remark2", Remark2);
			req.put("Remark3", Remark3);
			req.put("ReturnURL", ReturnURL);
			req.put("NotifyURL", NotifyURL);
			req.put("SignInfo", SignInfo);
			
			com.dept.web.general.util.HttpClientUtil.sendHtml(SubmitURL, req,response);
		} else {
			return "redirect:login.html";
		}
		return null;
	}
	
	/**
	 * 接收资金释放页面返回信息
	 * 
	 * @return
	 */
	@RequestMapping("qddApi/loanReleaseReturn")
	public String loanReleaseReturn(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		MoneymoremoreId = params.get("MoneymoremoreId");
		PlatformMoneymoremore = params.get("PlatformMoneymoremore");
		LoanNo = params.get("LoanNo");
		OrderNo = params.get("OrderNo");
		Amount = params.get("Amount");
		ReleaseType = params.get("ReleaseType");
		RandomTimeStamp = params.get("RandomTimeStamp");
		Remark1 = params.get("Remark1");
		Remark2 = params.get("Remark2");
		Remark3 = params.get("Remark3");
		ResultCode = params.get("ResultCode");
		
		String publickey = Common.publicKey;
		
//		MD5 md5 = new MD5();
		RsaHelper rsa = RsaHelper.getInstance();
		String dataStr = MoneymoremoreId + PlatformMoneymoremore + LoanNo + OrderNo + Amount + ReleaseType + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ResultCode;
//		if (antistate == 1)
//		{
//			dataStr = md5.getMD5Info(dataStr);
//		}
		
		// 签名
		boolean verifySignature = rsa.verifySignature(SignInfo, dataStr, publickey);
		System.out.println("页面返回:" + verifySignature);
		System.out.println("返回码:" + ResultCode);
		
		if(verifySignature == true) {
			map.addAttribute("errorMsg","资金释放成功");
		} else {
			map.addAttribute("errorMsg","资金释放失败");
		}
		return "errorMsg";
	}
	
	/**
	 * 接收资金释放后台通知信息
	 * 
	 * @return
	 */
	@RequestMapping("qddApi/loanReleaseNotify")
	public void loanReleaseNotify(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		MoneymoremoreId = params.get("MoneymoremoreId");
		PlatformMoneymoremore = params.get("PlatformMoneymoremore");
		LoanNo = params.get("LoanNo");
		OrderNo = params.get("OrderNo");
		Amount = params.get("Amount");
		ReleaseType = params.get("ReleaseType");
		RandomTimeStamp = params.get("RandomTimeStamp");
		Remark1 = params.get("Remark1");
		Remark2 = params.get("Remark2");
		Remark3 = params.get("Remark3");
		ResultCode = params.get("ResultCode");
		
		String publickey = Common.publicKey;
		
//		MD5 md5 = new MD5();
		RsaHelper rsa = RsaHelper.getInstance();
		String dataStr = MoneymoremoreId + PlatformMoneymoremore + LoanNo + OrderNo + Amount + ReleaseType + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ResultCode;
//		if (antistate == 1)
//		{
//			dataStr = md5.getMD5Info(dataStr);
//		}
		
		// 签名
		boolean verifySignature = rsa.verifySignature(SignInfo, dataStr, publickey);
		System.out.println("后台通知:" + verifySignature);
		System.out.println("返回码:" + ResultCode);
		System.out.println("返回次数:" + ReturnTimes);
		
		this.out(response, "SUCCESS");
	}
}
