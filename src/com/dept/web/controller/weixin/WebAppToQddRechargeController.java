package com.dept.web.controller.weixin;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dept.web.context.Constant;
import com.dept.web.context.Global;
import com.dept.web.controller.WebController;
import com.dept.web.controller.qddapi.util.Common;
import com.dept.web.controller.qddapi.util.RsaHelper;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserRecharge;
import com.dept.web.general.util.HttpClientUtil;
import com.dept.web.general.util.NewDateUtils;
import com.dept.web.general.util.Utils;
import com.dept.web.service.UserAccountService;
import com.dept.web.service.UserRechargeService;

@Controller
public class WebAppToQddRechargeController extends WebController {
	
	private static final Logger LOGGER=Logger.getLogger(WebAppToQddRechargeController.class);
	
	@Autowired
	private UserRechargeService rechargeService;
	
	@Autowired
	private UserAccountService accountService;

	private String RechargeMoneymoremore = "";
	private String OrderNo = "";
	private String Amount = "";
	private String RechargeType="";
	private String FeeType="";
	private String CardNo = "";
	private String CardNoList = "";
	private String Fee = "";
	private String FeePlatform;
	private String LoanNo = "";
	/**
	 * 生成充值信息
	 */
	@RequestMapping("webapp/qddApi/toLoanRecharge")
	public String toLoanRecharge(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		RechargeMoneymoremore = params.get("moneymoremoreId");
		PlatformMoneymoremore =Global.getValue("qdd_PlatformMoneymoremore");
		String uuid=UUID.randomUUID().toString();
		OrderNo =uuid;
		Amount = request.getParameter("chargeMoney");
		double chargeMoney=0.0;
		RechargeType="2";
		FeeType="2";
		UserRecharge recharge = new UserRecharge();
		
		User user = getCurrUser(request, response);
		
		if (isNumber(Amount)) {
			if(StringUtils.isEmpty(Amount) || Double.valueOf(Amount) >= 5000000){
				map.addAttribute("msg","单笔最高充值金额不超过5000000元");
				return "redirect:/webapp/myhome/charge.html";
			}
			if(user != null) {
				if(StringUtils.isEmpty(user.getRealname())){
					map.addAttribute("msg","请先进行实名认证");
					return "redirect:/webapp/myhome/realname.html";
				}
				if (user.getUserType() == 2) {
					map.addAttribute("msg","企业用户请在PC端充值");
					return "redirect:/webapp/myhome/charge.html";
				} else {
					if(Double.valueOf(Amount) <= 2){
						map.addAttribute("msg","单笔最低金额必须大于2元");
						return "redirect:/webapp/myhome/charge.html";
					}
				}
			} else {
				return "redirect:/webapp/tologin.html";
			}

			chargeMoney = Utils.getDouble(Double.valueOf(Amount), 2);
			recharge.setMoneyRecharge(chargeMoney);
			recharge.setCreatedIp(getRequestIp(request));
			Amount = String.valueOf(chargeMoney);
		}else{
			map.addAttribute("msg","请输入充值金额");
			return "redirect:/webapp/myhome/charge.html";
		}
			if(user != null) {
				if(StringUtils.isEmpty(user.getRealname())){
					map.addAttribute("msg","请先进行实名认证");
					return "redirect:/webapp/myhome/realname.html";
				}
				recharge.setCreatedBy(user.getId());
				
				SubmitURLPrefix=Global.getValue("qdd_submitUrl");
				
				String myProject=Common.myProject;
				if(myProject.equals("test"))
				{
					SubmitURL = SubmitURLPrefix+"/loan/toloanrecharge.action";
				}
				else
				{
					SubmitURL = "https://recharge."+SubmitURLPrefix+"/loan/toloanrecharge.action";
				}
				
				
				ReturnURL = Global.getValue("qdd_notifyUrl") + "/webapp/qddApi/toLoanRechargeReturn.html";
				NotifyURL = Global.getValue("qdd_notifyUrl") + "/webapp/qddApi/toLoanRechargeNotify.html";
				String privatekey = Common.privateKeyPKCS8;
				String publickey = Common.publicKey;
				
				
				String dataStr = RechargeMoneymoremore + PlatformMoneymoremore + OrderNo + Amount + RechargeType + FeeType + CardNo + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ReturnURL + NotifyURL;
				// 签名
				RsaHelper rsa = RsaHelper.getInstance();
				SignInfo = rsa.signData(dataStr, privatekey);
	
				if (StringUtils.isNotBlank(CardNo))
				{
					CardNo = rsa.encryptData(CardNo, publickey);
				}
				
				Map<String, String> req = new TreeMap<String, String>();
				req.put("RechargeMoneymoremore", RechargeMoneymoremore);
				req.put("PlatformMoneymoremore", PlatformMoneymoremore);
				req.put("OrderNo", OrderNo);
				req.put("Amount", Amount);
				req.put("RechargeType", String.valueOf(RechargeType));
				req.put("FeeType", String.valueOf(FeeType));
				req.put("CardNo", CardNo);
				req.put("RandomTimeStamp", "");
				req.put("Remark1", "");
				req.put("Remark2", "");
				req.put("Remark3", "");
				req.put("ReturnURL", ReturnURL);
				req.put("NotifyURL", NotifyURL);
				req.put("SignInfo", SignInfo);
				HttpClientUtil.sendHtml(SubmitURL,req,response);
				recharge.setOrderId(OrderNo);//订单号
				recharge.setPaySource("Web");//支付来源（IOS，Android，Web，Wap）
				recharge.setAccount("网站支付");
				recharge.setCardNo("");//卡号
				recharge.setCreatedAt(System.currentTimeMillis()/1000);
				recharge.setPayResult("提交中");//默认支付成功
				recharge.setRemark("用户通过双乾充值"+Amount+"元");
				recharge.setStatus(-1);
				recharge.setThirdPlatform(2L);
				recharge.setThirdPlatformOrderId(0L);
				rechargeService.addCharge(recharge);//保存充值记录
			} else {
				return "redirect:/webapp/tologin.html";
			}
		return null;
	}
	
	/**
	 * 接收充值页面返回信息
	 * @return
	 */
	@RequestMapping("webapp/qddApi/toLoanRechargeReturn")
	public String toLoanRechargeReturn(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		RechargeMoneymoremore = params.get("RechargeMoneymoremore");
		PlatformMoneymoremore = params.get("PlatformMoneymoremore");
		LoanNo = params.get("LoanNo");
		OrderNo = params.get("OrderNo");
		Amount = params.get("Amount");
//		Double feePlatform=0.00;
		FeePlatform=params.get("FeePlatform");
		Fee = params.get("Fee");
		String rechargeType=params.get("RechargeType");
		RechargeType = rechargeType;
		String feeType=params.get("FeeType");
		FeeType = feeType;
		CardNoList = params.get("CardNoList");
		RandomTimeStamp = params.get("RandomTimeStamp");
		Remark1 = params.get("Remark1");
		Remark2 = params.get("Remark2");
		Remark3 = params.get("Remark3");
		ResultCode = params.get("ResultCode");
		SignInfo = params.get("SignInfo");
		String publickey = Common.publicKey;
		String privatekey = Common.privateKeyPKCS8;
		
		RsaHelper rsa = RsaHelper.getInstance();
		
		if (StringUtils.isNotBlank(CardNoList))
		{
			CardNoList = rsa.decryptData(CardNoList, privatekey);
			if (StringUtils.isBlank(CardNoList))
			{
				CardNoList = "";
			}
		}
		String dataStr = RechargeMoneymoremore + PlatformMoneymoremore + LoanNo + OrderNo + Amount + Fee + FeePlatform + RechargeType + FeeType + CardNoList + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ResultCode;
		
		// 签名
		boolean verifySignature = rsa.verifySignature(SignInfo, dataStr, publickey);
		this.verifySignature = Boolean.toString(verifySignature);
		LOGGER.info("充值页面返回签名信息verifySignature=:" + verifySignature);
		LOGGER.info("充值页面返回码ResultCode=:" + ResultCode);
		if(verifySignature == true&&"88".equals(ResultCode)) {
			map.addAttribute("errorMsg","充值成功");
		} else {
			map.addAttribute("errorMsg","充值失败");
		}
		
 		return "qgfwebapp/errorMsg";
	}
	
	/**
	 * 接收充值后台通知信息
	 * 
	 * @return
	 */
	@RequestMapping("webapp/qddApi/toLoanRechargeNotify")
	@Transactional(rollbackFor={Exception.class})
	public void toLoanRechargeNotify(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		RechargeMoneymoremore = params.get("RechargeMoneymoremore");
		PlatformMoneymoremore = params.get("PlatformMoneymoremore");
		LoanNo = params.get("LoanNo");
		OrderNo = params.get("OrderNo");
		Amount = params.get("Amount");
//		Double feePlatform=0.00;
//		FeePlatform=Utils.getDouble(feePlatform, 2);
		FeePlatform=params.get("FeePlatform");
		Fee = params.get("Fee");
		String rechargeType= params.get("RechargeType");
		RechargeType =rechargeType;
		String feeType=params.get("FeeType");
		FeeType = feeType;
		CardNoList = params.get("CardNoList");
		RandomTimeStamp = params.get("RandomTimeStamp");
		Remark1 = params.get("Remark1");
		Remark2 = params.get("Remark2");
		Remark3 = params.get("Remark3");
		ResultCode = params.get("ResultCode");
		SignInfo = params.get("SignInfo");
		ReturnTimes = params.get("ReturnTimes");
		Message=params.get("Message");
		
		String publickey = Common.publicKey;
		String privatekey = Common.privateKeyPKCS8;
		
		RsaHelper rsa = RsaHelper.getInstance();
		
		if (StringUtils.isNotBlank(CardNoList))
		{
			CardNoList = rsa.decryptData(CardNoList, privatekey);
			if (StringUtils.isBlank(CardNoList))
			{
				CardNoList = "";
			}
		}
		String dataStr = RechargeMoneymoremore + PlatformMoneymoremore + LoanNo + OrderNo + Amount + Fee + FeePlatform + RechargeType + FeeType + CardNoList + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ResultCode;
		
		// 签名
		boolean verifySignature = rsa.verifySignature(SignInfo, dataStr, publickey);
		this.verifySignature = Boolean.toString(verifySignature);
		LOGGER.info("充值后台通知签名信息verifySignature=:" + verifySignature);
		LOGGER.info("充值后台通知返回码ResultCode=:" + ResultCode);
		LOGGER.info("充值后台通知返回次数ReturnTimes=:" + ResultCode);
		if(verifySignature)
		{
			if(ResultCode.equals("88"))
			{
				UserRecharge userRecharge = rechargeService
						.getRechargeByOrderNo(OrderNo);
				if (userRecharge!=null &&userRecharge.getStatus() == -1) {//回调
					// 用户账户增加充值金额
					try{
						accountService.recharge(userRecharge.getCreatedBy(),
								userRecharge.getMoneyRecharge());
						userRecharge.setStatus(1);
						userRecharge.setPayResult("充值成功");
						userRecharge.setUpdatedAt(NewDateUtils.getNowTimeStr());
						// 更新充值记录
						rechargeService.updateRecharge(userRecharge);
	
						// 生成资金变动记录
						UserAccount account = accountService
								.getUserAccount(userRecharge.getCreatedBy());
	
						accountService.addUserAccountLog(account,
								userRecharge.getCreatedBy(),
								userRecharge.getMoneyRecharge(),
								Constant.ACCOUNT_LOG_TYPE_CZCG,
								userRecharge.getCreatedIp(), userRecharge.getRemark());
					}catch (Exception e) {
			            e.printStackTrace();
			            LOGGER.info("充值异常信息:" + e);
			            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			        }
				}
			}else{
				UserRecharge userRecharge = new UserRecharge();
				userRecharge.setUpdatedAt(NewDateUtils.getNowTimeStr());
				userRecharge.setPayResult("充值失败");//充值失败
				userRecharge.setOrderId(OrderNo);
				// 更新充值记录
				rechargeService.updateRecharge(userRecharge);
			}
		} else {
			UserRecharge userRecharge = new UserRecharge();
			userRecharge.setUpdatedAt(NewDateUtils.getNowTimeStr());
			userRecharge.setPayResult("充值失败");//充值失败
			userRecharge.setOrderId(OrderNo);
			// 更新充值记录
			rechargeService.updateRecharge(userRecharge);
		}
		this.out(response, "SUCCESS");
	}
	
	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if (isEmpty(str)) {
			return false;
		}

		Pattern regex = Pattern.compile("\\d*(.\\d*)?");
		Matcher matcher = regex.matcher(str);
		boolean isMatched = matcher.matches();
		return isMatched;
	}
	
	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}
}
