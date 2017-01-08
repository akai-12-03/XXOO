package com.dept.web.controller.qddapi;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

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
import com.dept.web.controller.qddapi.util.RsaHelper;
import com.dept.web.dao.model.Bank;
import com.dept.web.dao.model.BankCard;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.dao.model.UserWithdraw;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.HttpClientUtil;
import com.dept.web.general.util.NumberUtil;
import com.dept.web.general.util.tools.iphelper.IPUtils;
import com.dept.web.service.BankCardService;
import com.dept.web.service.BankService;
import com.dept.web.service.UserAccountService;
import com.dept.web.service.UserService;
import com.dept.web.service.UserWithdrawService;

@Controller
public class ToloanwithdrawsController extends WebController {
	
	private static final Logger LOGGER=Logger.getLogger(ToQddRechargeController.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private BankCardService bankCardService;
	@Autowired
	private UserAccountService accountService;
	@Autowired
	private UserWithdrawService userWithdrawService;
	@Autowired
	private BankService bankService;
	
	private String WithdrawMoneymoremore = "";
	private String OrderNo = "";
	private String Amount = "";
	private String FeePercent = "";
	private String FeeMax = "";
	private String FeeRate ="";
	private String CardNo = "";
	private String CardType = "";
	private String BankCode="";
	private String BranchBankName="";
	private String Province="";
	private String City="";
	private String CardNoList = "";
	private String Fee = "";
	private String LoanNo = "";
	private String FeeWithdraws="";
	private String FreeLimit="";
	private String FeeSplitting="";
	private String FeeQuota="";
	
	/**
	 * 生成提现信息
	 */
	@RequestMapping("qddApi/toloanwithdraws")
	public String toLoanRecharge(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = getCurrUser(request, response);
		if (user != null) {
			List<BankCard> list = bankCardService.selectAllBankCardByUser(user
					.getId());
			if (!list.isEmpty()) {
				Bank bank = bankService.queryAllBank(list.get(0).getBankId());
				if (bank != null) {
					FeePercent = "0";
					Map<String, String> params = getParamMap(request);
					PlatformMoneymoremore = Global
							.getValue("qdd_PlatformMoneymoremore");
					String uuid = UUID.randomUUID().toString();
					OrderNo = uuid;
					Amount = params.get("tmoney");
					FeeQuota="0";
					CardNo = list.get(0).getCardNo();
					BankCode = bank.getAbbreviation();
					Province = list.get(0).getProvince();
					City = list.get(0).getCity();
					CardType = "0";
					double douAmount;
					String msg = null;

					UserAccount userAccount = accountService
							.getUserAccount(user.getId());

					if (StringUtils.isNotEmpty(Amount)) {// 判断输入是否为空
						douAmount = Double.valueOf(Amount);
					} else {
						msg = "提现金额不能为空!";
						map.put("msg", msg);
						return "redirect:/myhome/withdraw.html";
					}
					if (douAmount <= 0) { // 判断提现金额是否大于零
						msg = "输入的金额必须大于0";
					} else if (douAmount > userAccount.getMoneyUsable()) {// 判断提现金额是否超过可用金额
						msg = "提现金额不能大于可用金额!";
					} else {
						// 如果是小数，则检查小数点后面有几位(提现金额最多精确到分,即小数点后两位)
						if (Amount.contains(".")) {
							String[] s = Amount.split("\\.");
							if (s[1].length() > 2) {
								msg = "提现金额的最小单位是分!";
							}
						}
					}
					if (!"".equals(msg)&&msg!=null) {
						map.put("msg", msg);
						return "redirect:/myhome/withdraw.html";
					}

					WithdrawMoneymoremore = user.getMoneymoremoreId();// 获取平台多多标识

					PlatformMoneymoremore = Global
							.getValue("qdd_PlatformMoneymoremore");
					SubmitURLPrefix = Global.getValue("qdd_submitUrl");

					String myProject = Common.myProject;
					if (myProject.equals("test")) {
						SubmitURL = SubmitURLPrefix
								+ "/loan/toloanwithdraws.action";
					} else {
						SubmitURL = "https://withdrawals." + SubmitURLPrefix
								+ "/loan/toloanwithdraws.action";
					}

					ReturnURL = Global.getValue("qdd_notifyUrl")
							+ "/qddApi/toloanwithdrawsRechargeReturn.html";
					NotifyURL = Global.getValue("qdd_notifyUrl")
							+ "/qddApi/toloanwithdrawsNotify.html";
					String privatekey = Common.privateKeyPKCS8;
					String publickey = Common.publicKey;

					douAmount=BigDecimal.valueOf(douAmount).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
					String dataStr = WithdrawMoneymoremore
							+ PlatformMoneymoremore + OrderNo + String.valueOf(douAmount) + FeeQuota
							+ FeeMax + FeeRate + CardNo + CardType
							+ BankCode + BranchBankName + Province + City
							+ RandomTimeStamp + Remark1 + Remark2 + Remark3
							+ ReturnURL + NotifyURL;
					LOGGER.info("提现明文dataStr=="+dataStr.toString());
					// 签名
					RsaHelper rsa = RsaHelper.getInstance();
					SignInfo = rsa.signData(dataStr, privatekey);
					if (StringUtils.isNotBlank(CardNo)) {
						CardNo = rsa.encryptData(CardNo, publickey);
					}
					Map<String, String> req = new TreeMap<String, String>();
					req.put("WithdrawMoneymoremore", WithdrawMoneymoremore);
					req.put("PlatformMoneymoremore", PlatformMoneymoremore);
					req.put("OrderNo", OrderNo);
					req.put("Amount", String.valueOf(douAmount));
					req.put("FeeQuota", FeeQuota);
					req.put("CardNo", CardNo);
					req.put("CardType", CardType);
					req.put("BankCode", BankCode);
					req.put("Province", Province);
					req.put("City", City);
					req.put("RandomTimeStamp", RandomTimeStamp);
					req.put("Remark1", Remark1);
					req.put("Remark2", Remark2);
					req.put("Remark3", Remark3);
					req.put("ReturnURL", ReturnURL);
					req.put("NotifyURL", NotifyURL);
					req.put("SignInfo", SignInfo);
					HttpClientUtil.sendHtml(SubmitURL, req, response);
				} else {
					map.put("msg", "没有相关银行信息，请联系管理员");// bank表数据对应不上
					return "redirect:/myhome/withdraw.html";
				}
			} else {
				map.put("msg", "请先绑定银行卡");
				return "redirect:/bankBind.htmll";
			}
		} else {
			return "redirect:/login.html";
		}
		return null;
	}
	
	/**
	 * 接收充值页面返回信息
	 * 
	 * @return
	 */
	@RequestMapping("qddApi/toloanwithdrawsRechargeReturn")
	public String toloanwithdrawsRechargeReturn(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		WithdrawMoneymoremore = params.get("WithdrawMoneymoremore");
		PlatformMoneymoremore = params.get("PlatformMoneymoremore");
		LoanNo = params.get("LoanNo");
		OrderNo = params.get("OrderNo");
		Amount = params.get("Amount");
		FeeMax = params.get("FeeMax");
		FeeWithdraws = params.get("FeeWithdraws");
		FeePercent = params.get("FeePercent");
		Fee=params.get("Fee");
		FreeLimit=params.get("FreeLimit");
		FeeRate=params.get("FeeRate");
		FeeSplitting=params.get("FeeSplitting");
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
		
//		MD5 md5 = new MD5();
		RsaHelper rsa = RsaHelper.getInstance();
		
		if (StringUtils.isNotBlank(CardNoList))
		{
			CardNoList = rsa.decryptData(CardNoList, privatekey);
			if (StringUtils.isBlank(CardNoList))
			{
				CardNoList = "";
			}
		}
		String dataStr = WithdrawMoneymoremore + PlatformMoneymoremore + LoanNo + OrderNo + Amount + FeeMax + FeeWithdraws + FeePercent + Fee + FreeLimit + FeeRate + FeeSplitting + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ResultCode;
		
		// 签名
		boolean verifySignature = rsa.verifySignature(SignInfo, dataStr, publickey);
		this.verifySignature = Boolean.toString(verifySignature);
		System.out.println(this.verifySignature);
		
		LOGGER.info("提现页面返回verifySignature:" + verifySignature);
		LOGGER.info("提现返回码ResultCode:" + ResultCode);
		if(verifySignature == true&&ResultCode.equals("90"))
		{
			map.addAttribute("errorMsg","提现申请成功，请等待管理员审核!");
		} else {
			map.addAttribute("errorMsg",Message);
		}
		
		return "errorMsg";
	}
	
	/**
	 * 接收提现后台通知信息
	 * 
	 * @return
	 */
	@RequestMapping("qddApi/toloanwithdrawsNotify")
	public void toloanwithdrawsNotify(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		WithdrawMoneymoremore = params.get("WithdrawMoneymoremore");
		PlatformMoneymoremore = params.get("PlatformMoneymoremore");
		LoanNo = params.get("LoanNo");
		OrderNo = params.get("OrderNo");
		Amount = params.get("Amount");
		FeeMax = params.get("FeeMax");
		FeeWithdraws = params.get("FeeWithdraws");
		FeePercent = params.get("FeePercent");
		Fee=params.get("Fee");
		FreeLimit=params.get("FreeLimit");
		FeeRate=params.get("FeeRate");
		FeeSplitting=params.get("FeeSplitting");
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
		String dataStr = WithdrawMoneymoremore + PlatformMoneymoremore + LoanNo + OrderNo + Amount + FeeMax + FeeWithdraws + FeePercent + Fee + FreeLimit + FeeRate + FeeSplitting + RandomTimeStamp + Remark1 + Remark2 + Remark3 + ResultCode;
		
		// 签名
		User user=null;
		boolean verifySignature = rsa.verifySignature(SignInfo, dataStr, publickey);
		this.verifySignature = Boolean.toString(verifySignature);
		if(verifySignature)
		{
			if(ResultCode.equals("90"))
			{
				if(WithdrawMoneymoremore!=null){
					user = userService.queryUserByMid(WithdrawMoneymoremore);//根据userid查询用户信息
				}
				if (user != null) {
					UserAccount userAccount = accountService.getUserAccount(user.getId());
					List<BankCard> list = bankCardService.selectAllBankCardByUser(user.getId());
					double tmoney=Double.valueOf(Amount);
					double moneyUsable = userAccount.getMoneyUsable() - tmoney;
					double moneyWithdraw = userAccount.getMoneyWithdraw() + tmoney;
					userAccount.setMoneyUsable(moneyUsable);
					userAccount.setMoneyWithdraw(moneyWithdraw);
					userAccount.setUpdatedAt(System.currentTimeMillis() / 1000);
					//提现相关操作
					accountService.tiXianMethods(request, userAccount, tmoney);
					// 更新request中的userAccount
					userAccount = accountService.getUserAccount(user.getId());
					
					UserWithdraw uw=new UserWithdraw();
					uw.setOrderId(LoanNo);
					uw.setMoneyWithdraw(tmoney);
					uw.setRemark("提现冻结金额:"+tmoney);
					uw.setStatus(0);
					uw.setCreatedBy(user.getId());
					uw.setCreatedAt(DateUtils.getTime(new Date()));
					uw.setUpdatedBy(user.getId());
					uw.setUpdatedAt(DateUtils.getTime(new Date()));  //审核时间  目前取当前时间 等审核 之后再修改
					uw.setCreatedIp(IPUtils.getRemortIP(request));
					uw.setUseCard(String.valueOf(list.get(0).getId()));
					userWithdrawService.addUserWithdraw(uw);
				}else{
					LOGGER.error("根据WithdrawMoneymoremore查找user对象为空，请检查数据库用户表");
				}
			} else if (ResultCode.equals("89")) {
				AuditFailure(LoanNo, Message, request);
			}
		}
		LOGGER.info("提现后台通知verifySignature:" + verifySignature);
		LOGGER.info("提现返回码ResultCode:" + ResultCode);
		LOGGER.info("提现返回次数ReturnTimes:" + ReturnTimes);
		this.out(response, "SUCCESS");
	}
	
	public void AuditFailure(String LoanNoList,String Message,HttpServletRequest request){
		UserWithdraw uw = accountService.queryWithdrawByOrderId(LoanNoList);
		if (uw != null) {
			uw.setStatus(2);
			uw.setMessage(Message);
			uw.setUpdatedAt(DateUtils.getNowTimeStr());
			accountService.qddVerifyWithDraw(uw);
			/**
			 * 更改资金账户
			 */
			UserAccount newua = accountService.getAccount(uw
					.getCreatedBy());

			newua.setMoneyTotal(newua.getMoneyTotal()
					+ uw.getMoneyWithdraw());
			newua.setMoneyUsable(newua.getMoneyUsable() + uw.getMoneyWithdraw());
			newua.setUpdatedAt(DateUtils.getNowTimeStr());

			accountService.updateUserAccount(newua);

			/**
			 * 添加资金记录
			 */
			UserAccount newua_hand = accountService.getAccount(uw
					.getCreatedBy());
			// 新建资金记录
			accountService.addUserAccountLog(newua_hand,uw.getCreatedBy(),uw.getMoneyWithdraw(),6,IPUtils.getRemortIP(request),Message+ "返还提现金额" + uw.getMoneyWithdraw() + "元");
		}else{
			LOGGER.error("提现审核返回接口异常：未找到当前流水号数据");
		}
	}
}
