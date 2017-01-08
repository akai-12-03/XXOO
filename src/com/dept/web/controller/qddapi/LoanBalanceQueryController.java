package com.dept.web.controller.qddapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

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
public class LoanBalanceQueryController extends WebController {
	protected String PlatformId = "";
	protected String PlatformType = "";
	
	@Autowired
	private UserService userService;
	
	/**
	 * 生成余额查询信息
	 */
	@RequestMapping("qddApi/loanBalanceQuery")
	public String loanBalanceQuery(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		PlatformMoneymoremore = Global.getValue("qdd_PlatformMoneymoremore");
		SubmitURLPrefix=Global.getValue("qdd_submitUrl");
		String userId=params.get("userId");
		String myProject=Common.myProject;
		if(myProject.equals("test"))
		{
			SubmitURL = SubmitURLPrefix + "/loan/balancequery.action";
		}
		else
		{
			SubmitURL = "https://query."+SubmitURLPrefix + "/loan/balancequery.action";
		}
		
		
		User user=userService.queryByUserId(Long.valueOf(userId));
		PlatformId=user.getMoneymoremoreId();
		PlatformType="1";
		
		String privatekey = Common.privateKeyPKCS8;
		
		String dataStr = PlatformId + PlatformType + PlatformMoneymoremore;
		// 签名
//		MD5 md5 = new MD5();
		RsaHelper rsa = RsaHelper.getInstance();
//		if (antistate == 1)
//		{
//			dataStr = md5.getMD5Info(dataStr);
//		}
		SignInfo = rsa.signData(dataStr, privatekey);
		
		Map<String, String> req = new TreeMap<String, String>();
		req.put("PlatformId", PlatformId);
		req.put("PlatformType", PlatformType);
		req.put("PlatformMoneymoremore", PlatformMoneymoremore);
		req.put("SignInfo", SignInfo);
		
		String[] resultarr = HttpClientUtil.doPostQueryCmd(SubmitURL, req);
		System.out.println(resultarr[1]);
		
		if (StringUtils.isNotBlank(resultarr[1]))
		{
			String[] balance = resultarr[1].split("\\|");
			if (balance != null && balance.length > 2)
			{
				System.out.println(balance[0]);
				System.out.println(balance[1]);
				System.out.println(balance[2]);
				
				map.addAttribute("balance", balance[0]);
				map.addAttribute("availableAmount", balance[1]);
				map.addAttribute("freezeAmount", balance[2]);
			}
		}
		return "pay/yibaoMoney";
	}
	
	@RequestMapping("qddApi/giveHongbao")
	public void giveHongbao(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//送红包
		/****************************************************************/
		String mid=request.getParameter("mid");
		String money=request.getParameter("money");
		List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
		String pid = Global.getValue("qdd_PlatformMoneymoremore");
			// 参数集合
			String uuid=UUID.randomUUID().toString();
			String uuid2=UUID.randomUUID().toString();
			/** 转账给投资返现红包 */
			LoanInfoBean mlib = new LoanInfoBean();
			mlib.setLoanOutMoneymoremore(pid);
			mlib.setLoanInMoneymoremore(mid);
			mlib.setOrderNo(uuid);
			mlib.setBatchNo(uuid2);
			mlib.setAmount(money);
			mlib.setTransferName("红包");
			mlib.setRemark("返现红包");
			// mlib.setSecondaryJsonList(SecondaryJsonList);
			listmlib.add(mlib);
			
		
		// 组装签名信息
		String privatekey = Common.privateKeyPKCS8;
		String publicKey=Common.publicKey;
		String LoanJsonList = Common.JSONEncode(listmlib);
		String PlatformMoneymoremore=pid;
		int TransferAction=2;
		int Action=2;
		int TransferType=2;
		int NeedAudit=1;
		String NotifyURL="";
//		NotifyURL = Global.getString("qdd_notifyUrl") + "/qddApi/backToHongbaornotify.html";
		NotifyURL = "http://www.126.com";
//		String dataStr = LoanJsonList + PlatformMoneymoremore
//		+ TransferAction + Action + TransferType + NeedAudit
//		+ NotifyURL;
		
		String RandomTimeStamp="";
		String Remark1="";
		String Remark2="";
		String Remark3="";
		String ReturnURL=Global.getValue("qdd_notifyUrl") + "/qddApi/backToHongbaornotify.html";
		
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
		paramsMap.put("PlatformMoneymoremore", pid);
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
		
		/****************************************************************/
	}
	
	
	@RequestMapping("qddApi/backToHongbaornotify")
	public void backToHongbaornotify(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
			System.out.println("----------------------------------------");
			System.out.println("---------------送红包返回----------------");
			System.out.println("----------------------------------------");
	}
	
	@RequestMapping("qddApi/updateHongbao")
	public void updateHongbao(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		hongBaoDao.updateHongbao(String.valueOf(33), String.valueOf(12),"0");
		
		try {
//			accountDao.updateAccountHongbao(0, 1000, -1000,0,10, 33);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
