package com.dept.web.controller.qddapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dept.web.context.Global;
import com.dept.web.controller.WebController;
import com.dept.web.controller.qddapi.model.LoanInfoBean;
import com.dept.web.controller.qddapi.util.Common;
import com.dept.web.controller.qddapi.util.HttpClientUtil;
import com.dept.web.controller.qddapi.util.MD5;
import com.dept.web.controller.qddapi.util.RsaHelper;
import com.dept.web.dao.model.User;
import com.dept.web.service.UserService;
/**
 * 托管平台：钱多多<用户账号:余额查询>
 */
@Controller
public class LoanDirectAccountController extends WebController {
	protected String PlatformId = "";
	protected String PlatformType = "";
	
	@Autowired
	private UserService userService;
	
	//个人转账给平台
	@RequestMapping("qddApi/giveMoneyToCompany")
	public void giveMoneyToCompany(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/****************************************************************/
		String PlatformMoneymoremore=Global.getValue("qdd_PlatformMoneymoremore");
//		String inid=request.getParameter("inid");
//		User receiveUser=userService.queryByUserId(Long.valueOf(inid));
		String mmid=PlatformMoneymoremore;
		
		String money=request.getParameter("money");
		List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
		String outid = request.getParameter("outid");
		User outUser=userService.queryByUserId(Long.valueOf(outid));
//		String outMMid=outUser.getMoneymoremoreId();
		
		
			// 参数集合
			String uuid=UUID.randomUUID().toString();
			String uuid2=UUID.randomUUID().toString();
			/** 转账给另外一个用户 */
			LoanInfoBean mlib = new LoanInfoBean();
//			mlib.setLoanOutMoneymoremore(outMMid);
			mlib.setLoanInMoneymoremore(mmid);
			mlib.setOrderNo(uuid);
			mlib.setBatchNo(uuid2);
			mlib.setAmount(money);
			mlib.setTransferName("转账");
			mlib.setRemark("转账");
			// mlib.setSecondaryJsonList(SecondaryJsonList);
			listmlib.add(mlib);
			
		
		// 组装签名信息
		String privatekey = Common.privateKeyPKCS8;
		String publicKey=Common.publicKey;
		String LoanJsonList = Common.JSONEncode(listmlib);
		
		int TransferAction=2;
		int Action=2;
		int TransferType=2;
		int NeedAudit=1;
		String NotifyURL="";
		NotifyURL = Global.getValue("qdd_notifyUrl") + "/qddApi/backToMoneyNotify.html";
//		NotifyURL = "http://www.126.com";
//		String dataStr = LoanJsonList + PlatformMoneymoremore
//		+ TransferAction + Action + TransferType + NeedAudit
//		+ NotifyURL;
		
		String RandomTimeStamp="";
		String Remark1="";
		String Remark2="";
		String Remark3="";
		String ReturnURL=Global.getValue("qdd_notifyUrl") + "/qddApi/backToMoneyNotify.html";
		
//		String dataStr = LoanJsonList + PlatformMoneymoremore
//		+ TransferAction + Action + TransferType + NeedAudit
//		+ RandomTimeStamp + Remark1 + Remark2 + Remark3
//		+ ReturnURL + NotifyURL;
		
		String dataStr =LoanJsonList + PlatformMoneymoremore + TransferAction + Action + TransferType + NeedAudit + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ReturnURL + NotifyURL;
		
		MD5 md5 = new MD5();
		RsaHelper rsa = RsaHelper.getInstance();
		String SignInfo = rsa.signData(dataStr, privatekey);
		
		// 参数编码
		LoanJsonList = Common.UrlEncoder(LoanJsonList, "utf-8");
		String myProject=Common.myProject;
		if(myProject.equals("test"))
		{
			SubmitURL = Global.getValue("qdd_submitUrl")+"/loan/loan.action";
		}
		else
		{
			SubmitURL = "https://transfer."+Global.getValue("qdd_submitUrl")+"/loan/loan.action";
		}
		Map<String, String> paramsMap = new TreeMap<String, String>();
//		map.put("SubmitURL", SubmitURL);
		paramsMap.put("LoanJsonList", LoanJsonList);
		paramsMap.put("PlatformMoneymoremore", PlatformMoneymoremore);
		paramsMap.put("TransferAction", String.valueOf(TransferAction));
		paramsMap.put("Action", String.valueOf(Action));
		paramsMap.put("TransferType", String.valueOf(TransferType));
		paramsMap.put("NeedAudit", String.valueOf(NeedAudit));
		paramsMap.put("RandomTimeStamp", RandomTimeStamp);
		paramsMap.put("Remark1", Remark1);
		paramsMap.put("Remark2", Remark2);
		paramsMap.put("Remark3", Remark3);
		paramsMap.put("ReturnURL", ReturnURL);
		paramsMap.put("NotifyURL", NotifyURL);
		paramsMap.put("SignInfo", SignInfo);
		
		String[] resultarr = HttpClientUtil.doPostQueryCmd(SubmitURL, paramsMap);
		System.out.println(resultarr[1]);
		
		JSONArray arr = JSONArray.parseArray(resultarr[1]);
		JSONObject obj=arr.getJSONObject(0);
		
		String resultCode = obj.getString("ResultCode");
		if ("88".equals(resultCode)) {
			System.out.println("转账成功！");
			
//			Account outAccount=accountService.getAccount(Long.valueOf(outid));
//			outAccount.setUseMoney(outAccount.getUseMoney()-Double.parseDouble(money));
//			outAccount.setTotal(outAccount.getTotal()-Double.parseDouble(money));
//			accountService.updateAccount(outAccount);
			this.out(response, resultCode);
		} else {
			System.out.println("转账错误！");
			this.out(response, resultCode);
		}
		
		
		
		/****************************************************************/
	}
	
	
	@RequestMapping("qddApi/backToMoneyNotify")
	public void backToHongbaornotify(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
			System.out.println("----------------------------------------");
			System.out.println("---------------转账返回------------------");
			this.out(response, "SUCCESS");
			System.out.println("----------------------------------------");
	}
	
	
	//平台转账给个人
	@RequestMapping("qddApi/giveMoneyToUsers")
	public void giveMoneyToUsers(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/****************************************************************/
		String PlatformMoneymoremore=Global.getValue("qdd_PlatformMoneymoremore");
		String inid=request.getParameter("inid");
		
		JSONArray arr = JSONArray.parseArray(inid);
		List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
		for(int i=0;i<arr.size();i++)
		{
		JSONObject obj=arr.getJSONObject(i);
		String userId=obj.getString("userId");
		User receiveUser=userService.queryByUserId(Long.valueOf(userId));
//		String mmid=receiveUser.getMoneymoremoreId();
		String money=obj.getString("money");
		
		
			// 参数集合
			String uuid=UUID.randomUUID().toString();
			String uuid2=UUID.randomUUID().toString();
			/** 转账给另外一个用户 */
			LoanInfoBean mlib = new LoanInfoBean();
			mlib.setLoanOutMoneymoremore(PlatformMoneymoremore);
//			mlib.setLoanInMoneymoremore(mmid);
			mlib.setOrderNo(uuid);
			mlib.setBatchNo(uuid2);
			mlib.setAmount(money);
			mlib.setTransferName("转账");
			mlib.setRemark("转账");
			// mlib.setSecondaryJsonList(SecondaryJsonList);
			listmlib.add(mlib);
		}
			
		
		// 组装签名信息
		String privatekey = Common.privateKeyPKCS8;
		String publicKey=Common.publicKey;
		String LoanJsonList = Common.JSONEncode(listmlib);
		
		int TransferAction=2;
		int Action=2;
		int TransferType=2;
		int NeedAudit=1;
		String NotifyURL="";
		NotifyURL = Global.getValue("qdd_notifyUrl") + "/qddApi/backToUserMoneyNotify.html";
//		NotifyURL = "http://www.126.com";
//		String dataStr = LoanJsonList + PlatformMoneymoremore
//		+ TransferAction + Action + TransferType + NeedAudit
//		+ NotifyURL;
		
		String RandomTimeStamp="";
		String Remark1="";
		String Remark2="";
		String Remark3="";
		String ReturnURL=Global.getValue("qdd_notifyUrl") + "/qddApi/backUserToMoneyNotify.html";
		
//		String dataStr = LoanJsonList + PlatformMoneymoremore
//		+ TransferAction + Action + TransferType + NeedAudit
//		+ RandomTimeStamp + Remark1 + Remark2 + Remark3
//		+ ReturnURL + NotifyURL;
		
		String dataStr =LoanJsonList + PlatformMoneymoremore + TransferAction + Action + TransferType + NeedAudit + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ReturnURL + NotifyURL;
		
		MD5 md5 = new MD5();
		RsaHelper rsa = RsaHelper.getInstance();
		String SignInfo = rsa.signData(dataStr, privatekey);
		
		// 参数编码
		LoanJsonList = Common.UrlEncoder(LoanJsonList, "utf-8");
		String myProject=Common.myProject;
		if(myProject.equals("test"))
		{
			SubmitURL = Global.getValue("qdd_submitUrl")+"/loan/loan.action";
		}
		else
		{
			SubmitURL = "https://transfer."+Global.getValue("qdd_submitUrl")+"/loan/loan.action";
		}
		Map<String, String> paramsMap = new TreeMap<String, String>();
//		map.put("SubmitURL", SubmitURL);
		paramsMap.put("LoanJsonList", LoanJsonList);
		paramsMap.put("PlatformMoneymoremore", PlatformMoneymoremore);
		paramsMap.put("TransferAction", String.valueOf(TransferAction));
		paramsMap.put("Action", String.valueOf(Action));
		paramsMap.put("TransferType", String.valueOf(TransferType));
		paramsMap.put("NeedAudit", String.valueOf(NeedAudit));
		paramsMap.put("RandomTimeStamp", RandomTimeStamp);
		paramsMap.put("Remark1", Remark1);
		paramsMap.put("Remark2", Remark2);
		paramsMap.put("Remark3", Remark3);
		paramsMap.put("ReturnURL", ReturnURL);
		paramsMap.put("NotifyURL", NotifyURL);
		paramsMap.put("SignInfo", SignInfo);
		
		String[] resultarr = HttpClientUtil.doPostQueryCmd(SubmitURL, paramsMap);
		System.out.println(resultarr[1]);
		
		JSONArray resultArr = JSONArray.parseArray(resultarr[1]);
		JSONObject obj=resultArr.getJSONObject(0);
		
		String resultCode = obj.getString("ResultCode");
		if ("88".equals(resultCode)) {
			System.out.println("转账成功！");
			for(int i=0;i<arr.size();i++)
			{
				
				JSONObject jsonObj=arr.getJSONObject(i);
				String userId=jsonObj.getString("userId");
				String resultMoney=jsonObj.getString("money");
//				Account inAccount=accountService.getAccount(Long.valueOf(userId));
//				inAccount.setUseMoney(inAccount.getUseMoney()+Double.parseDouble(resultMoney));
//				inAccount.setTotal(inAccount.getTotal()+Double.parseDouble(resultMoney));
//				accountService.updateAccount(inAccount);
			}
			
			this.out(response, resultCode);
		} else {
			System.out.println("转账错误！");
			this.out(response, resultCode);
		}
		
		
		
		/****************************************************************/
	}
	
	
	@RequestMapping("qddApi/backToUserMoneyNotify")
	public void backToUserMoneyNotify(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
			System.out.println("----------------------------------------");
			System.out.println("---------------转账返回------------------");
			this.out(response, "SUCCESS");
			System.out.println("----------------------------------------");
	}
	
	
		//个人转账给个人
		@RequestMapping("qddApi/giveMoneyPersonToPerson")
		public void giveMoneyPersonToPerson(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			/****************************************************************/
			String PlatformMoneymoremore=Global.getValue("qdd_PlatformMoneymoremore");
			String inid=request.getParameter("inid");
			User receiveUser=userService.queryByUserId(Long.valueOf(inid));
			String mmid=receiveUser.getMoneymoremoreId();
			
			String money=request.getParameter("money");
			List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
			String outid = request.getParameter("outid");
			User outUser=userService.queryByUserId(Long.valueOf(outid));
			String outMMid=outUser.getMoneymoremoreId();
			
			
				// 参数集合
				String uuid=UUID.randomUUID().toString();
				String uuid2=UUID.randomUUID().toString();
				/** 转账给另外一个用户 */
				LoanInfoBean mlib = new LoanInfoBean();
				mlib.setLoanOutMoneymoremore(outMMid);
				mlib.setLoanInMoneymoremore(mmid);
				mlib.setOrderNo(uuid);
				mlib.setBatchNo(uuid2);
				mlib.setAmount(money);
				mlib.setTransferName("转账");
				mlib.setRemark("转账");
				// mlib.setSecondaryJsonList(SecondaryJsonList);
				listmlib.add(mlib);
				
			
			// 组装签名信息
			String privatekey = Common.privateKeyPKCS8;
			String publicKey=Common.publicKey;
			String LoanJsonList = Common.JSONEncode(listmlib);
			
			int TransferAction=2;
			int Action=2;
			int TransferType=2;
			int NeedAudit=1;
			String NotifyURL="";
			NotifyURL = Global.getValue("qdd_notifyUrl") + "/qddApi/backToMoneyNotify.html";
//			NotifyURL = "http://www.126.com";
//			String dataStr = LoanJsonList + PlatformMoneymoremore
//			+ TransferAction + Action + TransferType + NeedAudit
//			+ NotifyURL;
			
			String RandomTimeStamp="";
			String Remark1="";
			String Remark2="";
			String Remark3="";
			String ReturnURL=Global.getValue("qdd_notifyUrl") + "/qddApi/backToMoneyNotify.html";
			
//			String dataStr = LoanJsonList + PlatformMoneymoremore
//			+ TransferAction + Action + TransferType + NeedAudit
//			+ RandomTimeStamp + Remark1 + Remark2 + Remark3
//			+ ReturnURL + NotifyURL;
			
			String dataStr =LoanJsonList + PlatformMoneymoremore + TransferAction + Action + TransferType + NeedAudit + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ReturnURL + NotifyURL;
			
			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String SignInfo = rsa.signData(dataStr, privatekey);
			
			// 参数编码
			LoanJsonList = Common.UrlEncoder(LoanJsonList, "utf-8");
			String myProject=Common.myProject;
			if(myProject.equals("test"))
			{
				SubmitURL = Global.getValue("qdd_submitUrl")+"/loan/loan.action";
			}
			else
			{
				SubmitURL = "https://transfer."+Global.getValue("qdd_submitUrl")+"/loan/loan.action";
			}
			Map<String, String> paramsMap = new TreeMap<String, String>();
//			map.put("SubmitURL", SubmitURL);
			paramsMap.put("LoanJsonList", LoanJsonList);
			paramsMap.put("PlatformMoneymoremore", PlatformMoneymoremore);
			paramsMap.put("TransferAction", String.valueOf(TransferAction));
			paramsMap.put("Action", String.valueOf(Action));
			paramsMap.put("TransferType", String.valueOf(TransferType));
			paramsMap.put("NeedAudit", String.valueOf(NeedAudit));
			paramsMap.put("RandomTimeStamp", RandomTimeStamp);
			paramsMap.put("Remark1", Remark1);
			paramsMap.put("Remark2", Remark2);
			paramsMap.put("Remark3", Remark3);
			paramsMap.put("ReturnURL", ReturnURL);
			paramsMap.put("NotifyURL", NotifyURL);
			paramsMap.put("SignInfo", SignInfo);
			
			String[] resultarr = HttpClientUtil.doPostQueryCmd(SubmitURL, paramsMap);
			System.out.println(resultarr[1]);
			
			JSONArray arr = JSONArray.parseArray(resultarr[1]);
			JSONObject obj=arr.getJSONObject(0);
			
			String resultCode = obj.getString("ResultCode");
			if ("88".equals(resultCode)) {
				System.out.println("转账成功！");
//				Account inAccount=accountService.getAccount(Long.valueOf(inid));
//				inAccount.setUseMoney(inAccount.getUseMoney()+Double.parseDouble(money));
//				inAccount.setTotal(inAccount.getTotal()+Double.parseDouble(money));
//				accountService.updateAccount(inAccount);
				
//				Account outAccount=accountService.getAccount(Long.valueOf(outid));
//				outAccount.setUseMoney(outAccount.getUseMoney()-Double.parseDouble(money));
//				outAccount.setTotal(outAccount.getTotal()-Double.parseDouble(money));
//				accountService.updateAccount(outAccount);
				this.out(response, resultCode);
			} else {
				System.out.println("转账错误！");
				this.out(response, resultCode);
			}
			
			
			
			/****************************************************************/
		}
		
		
		@RequestMapping("qddApi/backToPersonNotify")
		public void backToPersonNotify(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
				System.out.println("----------------------------------------");
				System.out.println("---------------转账返回------------------");
				this.out(response, "SUCCESS");
				System.out.println("----------------------------------------");
		}
}
