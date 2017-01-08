package com.dept.web.controller.qddapi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dept.web.context.Global;
import com.dept.web.controller.WebController;
import com.dept.web.controller.qddapi.model.LoanInfoBean;
import com.dept.web.controller.qddapi.model.LoanReturnInfoBean;
import com.dept.web.controller.qddapi.util.Common;
import com.dept.web.controller.qddapi.util.RsaHelper;
import com.dept.web.dao.BorrowCollectionDao;
import com.dept.web.dao.BorrowTenderDao;
import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowRepayment;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.Market;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.general.util.NumberUtil;
import com.dept.web.general.util.TimeUtil;
import com.dept.web.service.BorrowService;
import com.dept.web.service.TransferService;
import com.dept.web.service.UserAccountService;
import com.dept.web.service.UserService;
/**
 * 托管平台：钱多多<转账管理:还款>
 */
@Controller
public class QddRepaymentController extends WebController{

	@Autowired
	private BorrowService borrowService;
	@Autowired
	private UserService userService;
	@Autowired
	private BorrowCollectionDao borrowCollectionDao;
	@Autowired
	private BorrowTenderDao borrowTenderDao;
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
    private TransferService transferService;
	
	private static final Logger LOGGER = Logger.getLogger(QddRepaymentController.class);


	/**
	 * 生成批量转账信息 ----------用户向托管方服务器提交转账数据(转账方式：投标/还款/费用/红包)
	 * 
	 * @TODO:
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             String
	 */
	@RequestMapping("qddApi/toRepayment")
	public String toRepayment(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = this.getCurrUser(request, response);
		if (user != null) {
			long repayid = NumberUtil.getLong(request.getParameter("id"));

			long user_id = user.getId();
			BorrowRepayment repay = borrowService.getRepayment(repayid);
			UserAccount act = userAccountService.getAccount(user_id);
			if (repay == null) {
				map.addAttribute("errmsg", "还款失败，订单不存在!");
				return "redirect:/myhome/jieBorrow.html?type=repayment";
			}
			if (repay.getRepaymentAccount() > act.getMoneyUsable()) {
				map.addAttribute("errmsg", "可用余额不足！");
				return "redirect:/myhome/repinfo.html";
			}
			try {
				// 参数集合
				List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
				// 组装参数
				String LoanOutMoneymoremore1 = user.getMoneymoremoreId(); // 付款人(用户在第三方资金平台注册的ID)---必填
				String TransferName1 = "还款";
				List<BorrowCollection> collectList = borrowCollectionDao
						.getCollectionLlistByBorrow(repay.getBorrowId(),
								repay.getRepOrder());
				// 循环投资人还款
				BigDecimal surplusCapital =BigDecimal.ZERO;
				for (int i=0;i<collectList.size();i++) {
					BorrowCollection c= collectList.get(i);
//					BigDecimal cCapital  = BigDecimal.valueOf(c.getCapital()).setScale(2, BigDecimal.ROUND_DOWN); // 归还投资人本金
//					BigDecimal cInterest = BigDecimal.valueOf(c.getInterest()).setScale(2, BigDecimal.ROUND_DOWN);// 归还投资人利息
					
					BigDecimal allMoney = BigDecimal.valueOf(c.getRepayAccount()).setScale(2, BigDecimal.ROUND_DOWN);//总金额
					
					surplusCapital=surplusCapital.add(allMoney);
					BorrowTender tender = borrowTenderDao.getTenderById(c
							.getTenderId());
					double resultMoney = allMoney.doubleValue(); // 本金+利息+interestMoney
					User userIn = userService.queryByUserId(tender.getUserId());
					String LoanInMoneymoremore1 = String.valueOf(userIn
							.getMoneymoremoreId()); // 收款人---必填
					String Amount1 = String.valueOf(resultMoney); // 金额(投标)---必填
					
					long time=System.currentTimeMillis()/1000;
					OrderNo1 = repayid + "_" + c.getId().toString()+"_"+time; // 平台订单号(投标)---必填（repayid/id：更新平台数据用到）
					BatchNo1 = String.valueOf(repay.getBorrowId()); // 平台标号(投标)---必填
					LoanInfoBean mlib = new LoanInfoBean();
					mlib.setLoanOutMoneymoremore(LoanOutMoneymoremore1);
					mlib.setLoanInMoneymoremore(LoanInMoneymoremore1);
					mlib.setOrderNo(OrderNo1);
					mlib.setBatchNo(BatchNo1);
					mlib.setAmount(Amount1);
					mlib.setTransferName(TransferName1);
					mlib.setRemark(Remark1);
					if(i==collectList.size()-1){
						BigDecimal repAccount =BigDecimal.valueOf(repay.getRepaymentAccount());
						double surplusCapitalM=repAccount.subtract(surplusCapital).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
						if (surplusCapitalM > 0) {
							mlib.setAmount(String.valueOf(resultMoney+surplusCapitalM));
							LoanInfoBean Secondary = new LoanInfoBean();
							Secondary.setLoanInMoneymoremore(PlatformMoneymoremore);
							Secondary.setAmount(String.valueOf(surplusCapitalM));
							Secondary.setTransferName("还款利息差额");
							Secondary.setRemark("");
							SecondaryJsonList = Common.JSONEncode(Secondary);
							mlib.setSecondaryJsonList(SecondaryJsonList);
						}
					}
					listmlib.add(mlib);
				}
				
				// 参数转换为JSON字符串
				LoanJsonList = Common.JSONEncode(listmlib);

				PlatformMoneymoremore = Global
						.getValue("qdd_PlatformMoneymoremore");

				String myProject = Common.myProject;
				if (myProject.equals("test")) {
					SubmitURL = Global.getValue("qdd_submitUrl")
							+ "/loan/loan.action";
				} else {
					SubmitURL = "https://transfer."
							+ Global.getValue("qdd_submitUrl")
							+ "/loan/loan.action";
				}

				ReturnURL = Global.getValue("qdd_notifyUrl")
						+ "/qddApi/backToRepaymentReturn.html";
				NotifyURL = Global.getValue("qdd_notifyUrl")
						+ "/qddApi/backToRepaymentNotify.html";
				TransferAction = "2";
				Action = "1";
				TransferType = "2";

				// 组装签名信息
				String privatekey = Common.privateKeyPKCS8;

				String dataStr = LoanJsonList + PlatformMoneymoremore
						+ TransferAction + Action + TransferType + NeedAudit
						+ RandomTimeStamp + Remark1 + Remark2 + Remark3
						+ ReturnURL + NotifyURL;
				RsaHelper rsa = RsaHelper.getInstance();
				SignInfo = rsa.signData(dataStr, privatekey);

				// 参数编码
				LoanJsonList = Common.UrlEncoder(LoanJsonList, "utf-8");

				map.addAttribute("SubmitURL", SubmitURL);
				map.addAttribute("LoanJsonList", LoanJsonList);
				map.addAttribute("PlatformMoneymoremore", PlatformMoneymoremore);
				map.addAttribute("TransferAction", TransferAction);
				map.addAttribute("Action", Action);
				map.addAttribute("TransferType", TransferType);
				map.addAttribute("NeedAudit", NeedAudit);
				map.addAttribute("RandomTimeStamp", RandomTimeStamp);
				map.addAttribute("ReturnURL", ReturnURL);
				map.addAttribute("NotifyURL", NotifyURL);
				map.addAttribute("SignInfo", SignInfo);
				map.addAttribute("Remark1", Remark1);
				map.addAttribute("Remark2", Remark2);
				map.addAttribute("Remark3", Remark3);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "pay/qdd_tender";
	}

	/**
	 * 接收转账页面返回信息---------托管方返回平台
	 * 
	 * @TODO:
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("qddApi/backToRepaymentReturn")
	public String backToRepaymentReturn(ModelMap map,HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			request.setCharacterEncoding("UTF-8");
			PlatformMoneymoremore = request.getParameter("PlatformMoneymoremore");
			Action=request.getParameter("Action");
			RandomTimeStamp = request.getParameter("RandomTimeStamp");
			Remark1 = request.getParameter("Remark1");
			Remark2 = request.getParameter("Remark2");
			Remark3 = request.getParameter("Remark3");
			ResultCode = request.getParameter("ResultCode");
			Message = request.getParameter("Message");
			SignInfo = request.getParameter("SignInfo");
			ReturnTimes=request.getParameter("ReturnTimes");
			LoanJsonList=request.getParameter("LoanJsonList");
			LoanJsonList = Common.UrlDecoder(LoanJsonList, "utf-8");
			if (Action == null) {
				Action = "";
			}

			String publickey = Common.publicKey;

			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanJsonList + PlatformMoneymoremore + Action
					+ RandomTimeStamp + Remark1 + Remark2 + Remark3
					+ ResultCode;
			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,publickey);
			this.verifySignature = Boolean.toString(verifySignature);
			System.out.println("页面返回:" + this.verifySignature);
			System.out.println("返回码:" + ResultCode);
				
			//获取投标人id
			List<Object> list=Common.JSONDecodeList(LoanJsonList,LoanReturnInfoBean.class);
			LoanReturnInfoBean bean=(LoanReturnInfoBean)list.get(0);
			User user = userService.queryUserByMid(bean.getLoanOutMoneymoremore());
			Map<String, String> mapJOSN=new HashMap<String, String>();
			mapJOSN.put("OrderNo1", OrderNo1);
			mapJOSN.put("ResultCode", ResultCode);
			mapJOSN.put("Message", Message);
			mapJOSN.put("verifySignature", this.verifySignature);
			String errorMsg = "还款";
			if (user != null) {
				if(verifySignature&&"88".equals(ResultCode)){
					errorMsg = errorMsg + "成功";
				} else {
					errorMsg = errorMsg + "失败，"+Message;
				}
			}else{
				errorMsg = errorMsg + "失败，未找到相关平台帐户";
			}
			map.addAttribute("errorMsg", errorMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "errorMsg";
	}

	/**
	 * 接收转账后台通知信息-------托管方回调
	 * 
	 * @TODO:
	 * @param map
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("qddApi/backToRepaymentNotify")
	public void backToRepaymentNotify(ModelMap map,HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			request.setCharacterEncoding("UTF-8");
			PlatformMoneymoremore = request.getParameter("PlatformMoneymoremore");
			Action = request.getParameter("Action");
			RandomTimeStamp = request.getParameter("RandomTimeStamp");
			Remark1 = request.getParameter("Remark1");
			Remark2 = request.getParameter("Remark2");
			Remark3 = request.getParameter("Remark3");
			ResultCode = request.getParameter("ResultCode");
			Message = request.getParameter("Message");
			SignInfo = request.getParameter("SignInfo");
			ReturnTimes = request.getParameter("ReturnTimes");
			LoanJsonList = request.getParameter("LoanJsonList");
			LoanJsonList = Common.UrlDecoder(LoanJsonList, "utf-8");
			if (Action == null) {// 返回的参数Action
				Action = "";
			}

			String publickey = Common.publicKey;

			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanJsonList + PlatformMoneymoremore + Action
					+ RandomTimeStamp + Remark1 + Remark2 + Remark3 + ResultCode;

			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,publickey);
			this.verifySignature = Boolean.toString(verifySignature);
			LOGGER.debug("后台通知:" + this.verifySignature);
			LOGGER.debug("返回码:" + ResultCode);
			LOGGER.debug("返回次数:" + ReturnTimes);

			if(verifySignature&&"88".equals(ResultCode)){
			//获取借款人id
			List<Object> list=Common.JSONDecodeList(LoanJsonList,LoanReturnInfoBean.class);
			LoanReturnInfoBean bean=(LoanReturnInfoBean) list.get(0);
			User user = userService.queryUserByMid(bean.getLoanOutMoneymoremore());
			if (user != null) {
				long repayid=0;
				//保存第三方还款冻结时返回的流水号
				for(Object o : list){
					LoanReturnInfoBean b=(LoanReturnInfoBean) o;
					BorrowCollection collect=new BorrowCollection();
					if (b.getOrderNo().indexOf("_") != -1) {
						String[] orderArr = b.getOrderNo().split("_");
						repayid = Long.parseLong(orderArr[0]);
						collect.setId(Long.valueOf(orderArr[1]));
					}
					collect.setLoanNo(b.getLoanNo());
					borrowCollectionDao.modifyCollectionByQdd(collect);
				}
				BorrowRepayment repay = borrowService.getRepayment(repayid);
				UserAccount act = userAccountService.getAccount(user.getId());
				UserAccountLog log = new UserAccountLog();
				log.setUserId(user.getId());
					String message = borrowService.doRepay(repay, act, log);
					LOGGER.error(message);
					
					 //债权转让中还款后发生改变的，删除债权转让数据
	                List<Market> marketlist=transferService.findMarketByBorrowIdAndStatus(repay.getBorrowId(),0);//0为新建的债权转让
	                
	                for(Market market:marketlist)
	                {
	                	BorrowTender tender=borrowTenderDao.getBorrowTenderById(market.getTenderId());
	                	tender.setTransfer(0);
	                	borrowTenderDao.updateBorrowTender(tender);
	                	transferService.delMarketByTenderId(tender.getId());
	                }
			}
			}else{
				LOGGER.error("还款接口失败："+Message);
			}
		}catch (Exception e) {
			LOGGER.error("还款接口失败："+e.getMessage());
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus()
			.setRollbackOnly();
		}
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		response.getWriter().write("SUCCESS");
		response.getWriter().flush();
		response.getWriter().close();
	}

}
