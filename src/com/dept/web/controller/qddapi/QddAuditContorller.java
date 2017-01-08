package com.dept.web.controller.qddapi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.dept.web.context.Global;
import com.dept.web.controller.WebController;
import com.dept.web.controller.qddapi.util.Common;
import com.dept.web.controller.qddapi.util.HttpClientUtil;
import com.dept.web.controller.qddapi.util.MD5;
import com.dept.web.controller.qddapi.util.RsaHelper;
import com.dept.web.dao.BorrowDao;
import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowRepayment;

/**
 * 托管平台：钱多多<转账审核管理:前台--撤标撤回资金/还款释放资金，后台--满标审核释放资金(代码在后台)>
 */
@Controller
public class QddAuditContorller extends WebController {

	/**
	 * 撤标审核
	 */
	@RequestMapping("qdd/loanTransferAudit")
	public void cancleBorrowAudit(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Map<String, String> params = getParamMap(request);
//			long borrowId = Long.parseLong(params.get("borrowId"));
//			long userId = Long.parseLong(params.get("userId"));
//			List<BorrowTender> borrowTenderList = borrowService
//					.getTenderList(borrowId);

			PlatformMoneymoremore = Global
					.getValue("qdd_PlatformMoneymoremore");
			SubmitURLPrefix = Global.getValue("qdd_submitUrl");

			String myProject = Common.myProject;
			if (myProject.equals("test")) {
				SubmitURL = SubmitURLPrefix
						+ "/loan/toloantransferaudit.action";
			} else {
				SubmitURL = "https://audit." + SubmitURLPrefix
						+ "/loan/toloantransferaudit.action";
			}

			// NotifyURL = Global.getString("qdd_notifyUrl") +
			// "/qdd/loantransferauditnotify.html";
			NotifyURL = "http://www.126.com";
			ReturnURL = Global.getValue("qdd_notifyUrl")
					+ "/qdd/loantransferauditreturn.html";
			AuditType = "2";

			StringBuffer sb = new StringBuffer();
//			for (BorrowTender tender : borrowTenderList) {
//				sb.append(tender.getBorrow_api_LoanNo()).append(",");
//			}
			if (sb.length() > 0) {
				LoanNoList = sb.substring(0, sb.length() - 1);

				String privatekey = Common.privateKeyPKCS8;

				String dataStr = LoanNoList + PlatformMoneymoremore + AuditType
						+ ReturnURL + NotifyURL;
				// 签名
				MD5 md5 = new MD5();
				RsaHelper rsa = RsaHelper.getInstance();
				SignInfo = rsa.signData(dataStr, privatekey);

				Map<String, String> reqmap = new HashMap<String, String>();
				reqmap.put("AuditType", AuditType);
				reqmap.put("PlatformMoneymoremore", PlatformMoneymoremore);
				reqmap.put("LoanNoList", LoanNoList);
				reqmap.put("LoanNoListFail", LoanNoList);
				reqmap.put("NotifyURL", NotifyURL);
				reqmap.put("SubmitURL", SubmitURL);
				reqmap.put("ReturnURL", ReturnURL);
				reqmap.put("SignInfo", SignInfo);
				String[] result = HttpClientUtil.doPostQueryCmd(SubmitURL,
						reqmap);
				// JSONObject obj=JSONObject.fromObject(result[1]);
				JSONObject obj = JSONObject.parseObject(result[1]);
				String resultCode = obj.getString("ResultCode");
				if ("88".equals(resultCode)) {
					System.out.println("撤标成功！");
				} else {
					System.out.println("撤标错误！");
				}

				this.out(response, Integer.parseInt(resultCode));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 还款审核
	 * 
	 * @TODO:
	 * @param map
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 */
	public String repaymentAudit(List<BorrowCollection> collectLists,
			BorrowRepayment repayment, BorrowDao borrowDao) throws Exception {
		String resultCode = "";
		try {
			PlatformMoneymoremore = Global
					.getValue("qdd_PlatformMoneymoremore");
			SubmitURLPrefix = Global.getValue("qdd_submitUrl");

			String myProject = Common.myProject;
			if (myProject.equals("test")) {
				SubmitURL = SubmitURLPrefix
						+ "/loan/toloantransferaudit.action";
			} else {
				SubmitURL = "https://audit." + SubmitURLPrefix
						+ "/loan/toloantransferaudit.action";
			}
			NotifyURL = Global.getValue("qdd_notifyUrl")
					+ "/qdd/loantransferauditnotify.html";
			
			
			ReturnURL = Global.getValue("weburl")
					+ "/qdd/loantransferauditreturn.html";
			AuditType = "1";

			System.out.println("NotifyURL=============" + NotifyURL);
			System.out.println("ReturnURL=============" + ReturnURL);

			StringBuffer sb = new StringBuffer();
			for (BorrowCollection borrowCollect : collectLists) {
				if (!StringUtils.isEmpty(borrowCollect.getLoanNo())) {
					sb.append(borrowCollect.getLoanNo()).append(",");
				}
			}
			if (sb.length() > 0) {
				LoanNoList = sb.substring(0, sb.length() - 1);
			}

			String privatekey = Common.privateKeyPKCS8;
			if (antistate == 1) {
				Date d = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				RandomTimeStamp = Common.getRandomNum(2) + sdf.format(d);
			}

			String dataStr = LoanNoList + PlatformMoneymoremore + AuditType
					+ ReturnURL + NotifyURL;
			// 签名
			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			if (antistate == 1) {
				dataStr = md5.getMD5Info(dataStr);
			}
			SignInfo = rsa.signData(dataStr, privatekey);

			Map<String, String> reqmap = new HashMap<String, String>();
			reqmap.put("AuditType", AuditType);
			reqmap.put("PlatformMoneymoremore", PlatformMoneymoremore);
			reqmap.put("LoanNoList", LoanNoList);
			reqmap.put("LoanNoListFail", LoanNoList);
			reqmap.put("NotifyURL", NotifyURL);
			reqmap.put("SubmitURL", SubmitURL);
			reqmap.put("ReturnURL", ReturnURL);
			reqmap.put("SignInfo", SignInfo);

			String[] result = HttpClientUtil.doPostQueryCmd(SubmitURL, reqmap);
			System.out.println("result0=====" + result[0]);
			System.out.println("result1=====" + result[1]);
			JSONObject obj = JSONObject.parseObject(result[1]);
			resultCode = obj.getString("ResultCode");
			if ("88".equals(resultCode)) {
				System.out.println("还款审核成功！");
			} else {
				System.out.println("还款提交钱多多资金托管有误！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultCode;
	}

	/**
	 * 接收审核页面返回信息
	 * 
	 * @return
	 */
	@RequestMapping("qdd/loantransferauditreturn")
	public String loantransferauditreturn(ModelMap map,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			request.setCharacterEncoding("UTF-8");
			PlatformMoneymoremore = request
					.getParameter("PlatformMoneymoremore");
			LoanNoList = request.getParameter("LoanNoList");
			LoanNoListFail = request.getParameter("LoanNoListFail");
			AuditType = request.getParameter("AuditType");
			ResultCode = request.getParameter("ResultCode");
			Message = request.getParameter("Message");
			SignInfo = request.getParameter("SignInfo");

			String publickey = Common.publicKey;

			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanNoList + LoanNoListFail
					+ PlatformMoneymoremore + AuditType + ResultCode;
			if (antistate == 1) {
				dataStr = md5.getMD5Info(dataStr);
			}

			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					publickey);
			this.verifySignature = Boolean.toString(verifySignature);
			System.out.println("页面返回:" + this.verifySignature);
			System.out.println("返回码:" + ResultCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:login.html?";
	}

	/**
	 * 接收审核后台通知信息
	 * 
	 * @return
	 */
	@RequestMapping("qdd/loantransferauditnotify")
	public void loantransferauditnotify(ModelMap map,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			request.setCharacterEncoding("UTF-8");
			PlatformMoneymoremore = request
					.getParameter("PlatformMoneymoremore");
			LoanNoList = request.getParameter("LoanNoList");
			LoanNoListFail = request.getParameter("LoanNoListFail");
			AuditType = request.getParameter("AuditType");
			ResultCode = request.getParameter("ResultCode");
			Message = request.getParameter("Message");
			SignInfo = request.getParameter("SignInfo");
			String publickey = Common.publicKey;

			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanNoList + LoanNoListFail
					+ PlatformMoneymoremore + AuditType + ResultCode;
			if (antistate == 1) {
				dataStr = md5.getMD5Info(dataStr);
			}
			// 签名
//			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
//					publickey);
			System.out.println("后台通知:" + verifySignature);
			System.out.println("返回码:" + ResultCode);
			System.out.println("返回次数:" + ReturnTimes);

			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write("SUCCESS");
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 还款审核
	 * 
	 * @TODO:
	 * @param map
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 */
	@RequestMapping("qdd/repaymentAuditByLN")
	public String repaymentAuditByLN(ModelMap map,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String resultCode = "";
		Map<String, String> params = getParamMap(request);
		String type=params.get("type");
		String loanList=params.get("loanList");
		try {
			PlatformMoneymoremore = Global
					.getValue("qdd_PlatformMoneymoremore");
			SubmitURLPrefix = Global.getValue("qdd_submitUrl");

			String myProject = Common.myProject;
			if (myProject.equals("test")) {
				SubmitURL = SubmitURLPrefix
						+ "/loan/toloantransferaudit.action";
			} else {
				SubmitURL = "https://audit." + SubmitURLPrefix
						+ "/loan/toloantransferaudit.action";
			}
			NotifyURL = Global.getValue("qdd_notifyUrl")
					+ "/qdd/loantransferauditnotify.html";
			
//			NotifyURL="http://www.126.com";
			
			ReturnURL = Global.getValue("weburl")
					+ "/qdd/loantransferauditreturn.html";
			AuditType = type;

			System.out.println("NotifyURL=============" + NotifyURL);
			System.out.println("ReturnURL=============" + ReturnURL);

//			StringBuffer sb = new StringBuffer();
			
			
			LoanNoList=loanList;
			
			String privatekey = Common.privateKeyPKCS8;

			String dataStr = LoanNoList + PlatformMoneymoremore + AuditType
					+ ReturnURL + NotifyURL;
			// 签名
			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			SignInfo = rsa.signData(dataStr, privatekey);

			Map<String, String> reqmap = new HashMap<String, String>();
			reqmap.put("AuditType", AuditType);
			reqmap.put("PlatformMoneymoremore", PlatformMoneymoremore);
			reqmap.put("LoanNoList", LoanNoList);
			reqmap.put("LoanNoListFail", LoanNoList);
			reqmap.put("NotifyURL", NotifyURL);
			reqmap.put("SubmitURL", SubmitURL);
			reqmap.put("ReturnURL", ReturnURL);
			reqmap.put("SignInfo", SignInfo);
			
			System.out.println("reqmap==="+reqmap);

			String[] result = HttpClientUtil.doPostQueryCmd(SubmitURL, reqmap);
			System.out.println("result0=====" + result[0]);
			System.out.println("result1=====" + result[1]);
			JSONObject obj = JSONObject.parseObject(result[1]);
			resultCode = obj.getString("ResultCode");
			if ("88".equals(resultCode)) {
				System.out.println("审核成功！");
				this.out(response, "审核成功！");
			} else {
				System.out.println("提交钱多多资金托管有误！");
				this.out(response, "提交钱多多资金托管有误！"+resultCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultCode;
	}
}
