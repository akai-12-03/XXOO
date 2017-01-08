package com.dept.web.controller.qddapi;

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
import com.dept.web.dao.HongbaoDao;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowApiLog;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.Hongbao;
import com.dept.web.dao.model.User;
import com.dept.web.general.util.HttpClientUtil;
import com.dept.web.general.util.NumberUtil;
import com.dept.web.general.util.TimeUtil;
import com.dept.web.service.BorrowApiLogService;
import com.dept.web.service.BorrowService;
import com.dept.web.service.BorrowTenderService;
import com.dept.web.service.UserService;

/**
 * 托管平台：钱多多<转账管理:投标>
 */
@Controller
public class QddTenderController extends WebController {
	private static final Logger LOGGER=Logger.getLogger(QddTenderController.class);
	@Autowired
	private BorrowService borrowService;
	@Autowired
	private UserService userService;
	@Autowired
	private BorrowApiLogService borrowApiLogService;
	
	@Autowired
    private HongbaoDao hongbaoDao;
	
	@Autowired
	private BorrowTenderService borrowTenderService;
	
	/**
	 * 生成批量转账信息----------用户向托管方服务器提交转账数据(转账方式：投标/还款/费用/红包)
	 * 
	 * @TODO:
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             String
	 */
	@RequestMapping("qddApi/tenderOrderNo")
	public String loan(ModelMap map, HttpServletRequest request,HttpServletResponse response) throws Exception {
		User user = getCurrUser(request, response);
		Map<String, String> params = getParamMap(request);
		long borrowId = Long.parseLong(params.get("id"));
		double money = NumberUtil.getDouble(params.get("tendermoney"));
		Remark1=params.get("tenderhongbao");//红包id
		Borrow borrow = borrowService.getBorrowById(borrowId);
		PlatformMoneymoremore = Global.getValue("qdd_PlatformMoneymoremore");
		if (user != null) {
			// 计算平台管理费和投资人应投金额
			double borrowMoney = money;// 投资人投资金额
			// 计算平台管理费和投资人应投金额
			double borrow_fee = borrow.getBorrow_fee();
		    double fee = money * (borrow_fee / 100); // 平台管理费用
			// 计算奖励
			double awardValue = 0;
			if (borrow.getAward() == 1) {// 按固定金额分摊奖励
				awardValue = borrow.getFunds() / borrow.getAccount() * money;
			} else if (borrow.getAward() == 2) {// 按投标金额比例
				awardValue = money * borrow.getPartAccount() / 100;
			} else {
				awardValue = 0;
			}
			awardValue=NumberUtil.format2(awardValue);
			borrowMoney=NumberUtil.format2(borrowMoney);
			// 投标记录 主要接收服务器回调时的money
			BorrowApiLog borrowApiLog = new BorrowApiLog();
			borrowApiLog.setBorrowId(borrow.getId());
			borrowApiLog.setMoney(NumberUtil.format2(borrowMoney));// 投资金额
			borrowApiLog.setUserId(user.getId());
			borrowApiLog.setRemark("投标");
			borrowApiLog.setStatus(0);
			borrowApiLog.setValid_account(String.valueOf(borrowMoney
					- awardValue));// 投资金额-管理费-奖励（和资金托管对应）
			borrowApiLog.setOperation_account("0"); // 平台管理费
			Long borrowApiLogId = borrowApiLogService
					.insertBorrowApiLog(borrowApiLog);

			// 转账(投标)
			String LoanOutMoneymoremore1 = String.valueOf(user
					.getMoneymoremoreId()); // 付款人(用户在第三方资金平台注册的ID)---必填
			User userIn =null;
			//垫资标
			if(borrow.getBorrowType()==1)
			{
				userIn = userService.queryByUserId(borrow.getReceivePerson());
			}
			else
			{
				userIn = userService.queryByUserId(borrow.getUserId());
			}
			String LoanInMoneymoremore1 = String.valueOf(userIn
					.getMoneymoremoreId()); // 收款人---必填
			OrderNo1 = String.valueOf(borrowApiLogId);// 平台订单号(投标)---必填
			BatchNo1 = String.valueOf(borrow.getId()); // 平台标号(投标)---必填
			String Amount1 = String.valueOf(NumberUtil.format2(money)); // 金额(投标)---必填
			String TransferName1 = "投标";
			// String mainRemark1 = "投标备注";

			try {
				// 参数集合
				List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();

				/** 投标 */
				LoanInfoBean mlib = new LoanInfoBean();
				mlib.setLoanOutMoneymoremore(LoanOutMoneymoremore1);
				mlib.setLoanInMoneymoremore(LoanInMoneymoremore1);
				mlib.setOrderNo(OrderNo1);
				mlib.setBatchNo(BatchNo1);
				mlib.setAmount(Amount1);
				mlib.setTransferName(TransferName1);
				mlib.setRemark(Remark1);
				mlib.setFullAmount(String.valueOf(borrow.getAccount()));

			
				
				/** 手续费或者奖励费用 */
				 if (fee>0||awardValue>0){
					 
				 LoanInfoBean  Secondary = new LoanInfoBean();
				 Secondary.setLoanInMoneymoremore(PlatformMoneymoremore);
				 Secondary.setAmount(String.valueOf(fee+awardValue));
				 Secondary.setTransferName("手续费|奖励费用");
				 Secondary.setRemark(Remark2);
				 SecondaryJsonList= Common.JSONEncode(Secondary);
				 mlib.setSecondaryJsonList(SecondaryJsonList);
				 }
				 
				listmlib.add(mlib);
				// 参数转换为JSON字符串
				LoanJsonList = Common.JSONEncode(listmlib);

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
						+ "/qddApi/backToTenderReturn.html";
				NotifyURL = Global.getValue("qdd_notifyUrl")
						+ "/qddApi/backToTendernotify.html";

				// 组装签名信息
				String privatekey = Common.privateKeyPKCS8;

				Action = "1";// 手动转账
				TransferAction = "1";
				TransferType = "2";// 1桥连，2直连
				String dataStr = LoanJsonList + PlatformMoneymoremore
						+ TransferAction + Action + TransferType + NeedAudit
						+ RandomTimeStamp + Remark1 + Remark2 + Remark3
						+ ReturnURL + NotifyURL;
				RsaHelper rsa = RsaHelper.getInstance();
				SignInfo = rsa.signData(dataStr, privatekey);

				// 参数编码
				LoanJsonList = Common.UrlEncoder(LoanJsonList, "utf-8");
				Map<String, String> paramap = new HashMap<String, String>();
				paramap.put("SubmitURL", SubmitURL);
				paramap.put("LoanJsonList", LoanJsonList);
				paramap.put("PlatformMoneymoremore", PlatformMoneymoremore);
				paramap.put("TransferAction", TransferAction);
				paramap.put("Action", Action);
				paramap.put("TransferType", TransferType);
				paramap.put("NeedAudit", NeedAudit);
				paramap.put("Remark1", Remark1);
				paramap.put("Remark2", Remark2);
				paramap.put("Remark3", Remark3);
				paramap.put("ReturnURL", ReturnURL);
				paramap.put("NotifyURL", NotifyURL);
				paramap.put("SignInfo", SignInfo);
				HttpClientUtil.sendHtml(SubmitURL, paramap, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return "redirect:/login.html";
		}
		return null;
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
	@RequestMapping("qddApi/backToTenderReturn")
	public String backToTenderReturn(ModelMap map,HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			request.setCharacterEncoding("UTF-8");
			PlatformMoneymoremore = request
					.getParameter("PlatformMoneymoremore");
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
			if (Action == null) {
				Action = "";
			}

			String publickey = Common.publicKey;
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanJsonList + PlatformMoneymoremore + Action
					+ RandomTimeStamp + Remark1 + Remark2 + Remark3
					+ ResultCode;

			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					publickey);

			this.verifySignature = Boolean.toString(verifySignature);
			LOGGER.info("投标页面返回verifySignature:" + this.verifySignature);
			LOGGER.info("投标返回码ResultCode:" + ResultCode);
			if (verifySignature && "88".equals(ResultCode)) {
				map.addAttribute("errorMsg", "投标成功");
			} else {
				map.addAttribute("errorMsg", "投标失败:"+Message);
			}
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
	@RequestMapping("qddApi/backToTendernotify")
	public void backToTendernotify(ModelMap map,HttpServletRequest request, HttpServletResponse response)throws Exception {
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
			
			if (Action == null) {//返回的参数Action
				Action = "";
			}

			String publickey = Common.publicKey;

			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanJsonList + PlatformMoneymoremore + Action
					+ RandomTimeStamp + Remark1 + Remark2 + Remark3
					+ ResultCode;
			
			List<Object> list=Common.JSONDecodeList(LoanJsonList,LoanReturnInfoBean.class);
			LoanReturnInfoBean bean=(LoanReturnInfoBean)list.get(0);

			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,publickey);
			System.out.println("verifySignature===="+verifySignature);
			this.verifySignature = Boolean.toString(verifySignature);
			LOGGER.info("投标后台通知verifySignature:" + this.verifySignature);
			LOGGER.info("投标返回码ResultCode:" + ResultCode);
			LOGGER.info("投标返回次数ReturnTimes:" + ReturnTimes);

			String errorMsg = "出错了，请联系客服";
			if (verifySignature && "88".equals(ResultCode)) {
				long apiLogId = NumberUtil.getLong(bean.getOrderNo());// OrderNo1提交时的单号
				String LoanNo = bean.getLoanNo();
				BorrowApiLog borrowApiLog = borrowApiLogService
						.getBorrowApiLogById(apiLogId);
				// 已处理的pass
				if (borrowApiLog != null && borrowApiLog.getStatus() != 1) {
					User user = userService.queryByUserId(borrowApiLog
							.getUserId());
					String content = "投标";
					if (user != null) {
						long borrowId = borrowApiLog.getBorrowId();
						Borrow borrow = borrowService.getBorrowById(borrowId);
						/** ---投标信息--- **/
						BorrowTender borrowTender = new BorrowTender();
						borrowTender.setBorrowId(borrowId);
						borrowTender.setApiLogId(apiLogId);
						borrowTender.setLoanNo(LoanNo);
						borrowTender.setUserId(user.getId());
						borrowTender.setMoney(borrowApiLog.getMoney());
						borrowTender.setAccount(borrowApiLog.getMoney());
						borrowTender.setAddip(getRequestIp(request));
						borrowTender.setAddtime(System.currentTimeMillis() / 1000);
						borrowTender.setStatus(0);

						// 是否使用了红包,如果红包不为空。则使用了红包
						String hongbaoId = Remark1;
						if (hongbaoId != null && !"".equals(hongbaoId.trim())
								&& !"null".equals(hongbaoId)) {
							Hongbao hb = hongbaoDao.queryHongbaoById(Long
									.valueOf(hongbaoId));
							if (hb != null) {
								borrowTender.setHongbao_id(hb.getId());
								borrowTender.setHongbao_money(hb.getMoney());
								// 修改红包记录状态为冻结。
								hb.setStatus(2);
								hb.setUsetime(TimeUtil.getNowTimeStr());
								hongbaoDao.updateHongbao(hb);
							}
						}
						try {
							borrowTenderService.addTender(borrowTender, borrow);
							map.addAttribute("errormsg", "投标成功");
						} catch (Exception e) {
							e.printStackTrace();
							map.addAttribute("errormsg", "投标失败");
							TransactionAspectSupport.currentTransactionStatus()
									.setRollbackOnly();
							errorMsg = content + "异常";
						}
						errorMsg = content + "成功";
					} else {
						errorMsg = content + "失败，投标记录表未找到相关交易信息";
					}
				}
			}else{
				map.addAttribute("errorMsg", errorMsg);
			}
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write("SUCCESS");
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			LOGGER.error("接收转账后台通知信息异常="+e);
			e.printStackTrace();
		}
	}
}
