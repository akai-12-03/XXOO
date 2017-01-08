package com.dept.web.controller.qddapi;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dept.web.context.Global;
import com.dept.web.controller.WebController;
import com.dept.web.controller.qddapi.model.LoanOrderQueryBean;
import com.dept.web.controller.qddapi.model.LoanOrderQuerySecondaryBean;
import com.dept.web.controller.qddapi.model.LoanRechargeOrderQueryBean;
import com.dept.web.controller.qddapi.model.LoanWithdrawsOrderQueryBean;
import com.dept.web.controller.qddapi.util.Common;
import com.dept.web.controller.qddapi.util.HttpClientUtil;
import com.dept.web.controller.qddapi.util.RsaHelper;

@Controller
public class LoanOrderQueryController extends WebController {
	protected String LoanNo = "";
	protected String BatchNo = "";
	protected String BeginTime = "";
	protected String EndTime = "";
	protected String Action = "";
	protected String OrderNo = "";
	/**
	 * 生成对账信息
	 */
	@RequestMapping("qddApi/loanOrderQuery")
	public String loanOrderQuery(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		PlatformMoneymoremore = params.get("PlatformMoneymoremore");
		Action = params.get("Action");
		LoanNo = params.get("LoanNo");
		OrderNo = params.get("OrderNo");
		BatchNo = params.get("BatchNo");
		BeginTime = params.get("BeginTime");
		EndTime = params.get("EndTime");
		
		
		SubmitURLPrefix=Global.getValue("qdd_submitUrl");
		
		
		String myProject=Common.myProject;
		if(myProject.equals("test"))
		{
			SubmitURL = SubmitURLPrefix + "loan/loanorderquery.action";
		}
		else
		{
			SubmitURL = "https://query."+SubmitURLPrefix + "loan/loanorderquery.action";
		}
		
		String privatekey = Common.privateKeyPKCS8;
		
		String dataStr = PlatformMoneymoremore + Action + LoanNo + OrderNo + BatchNo + BeginTime + EndTime;
		// 签名
//		MD5 md5 = new MD5();
		RsaHelper rsa = RsaHelper.getInstance();
//		if (antistate == 1)
//		{
//			dataStr = md5.getMD5Info(dataStr);
//		}
		SignInfo = rsa.signData(dataStr, privatekey);

		Map<String, String> req = new TreeMap<String, String>();
		req.put("PlatformMoneymoremore", PlatformMoneymoremore);
		req.put("Action", Action);
		req.put("LoanNo", LoanNo);
		req.put("OrderNo", OrderNo);
		req.put("BatchNo", BatchNo);
		req.put("BeginTime", BeginTime);
		req.put("EndTime", EndTime);
		req.put("SignInfo", SignInfo);
		
		String[] resultarr = HttpClientUtil.doPostQueryCmd(SubmitURL, req);
		System.out.println(resultarr[1]);
		
		if (StringUtils.isNotBlank(resultarr[1]) && (resultarr[1].startsWith("[") || resultarr[1].startsWith("{")))
		{
			if (StringUtils.isBlank(Action))
			{
				// 转账
				List<Object> loanobjectlist = Common.JSONDecodeList(resultarr[1], LoanOrderQueryBean.class);
				if (loanobjectlist != null && loanobjectlist.size() > 0)
				{
					for (int i = 0; i < loanobjectlist.size(); i++)
					{
						if (loanobjectlist.get(i) instanceof LoanOrderQueryBean)
						{
							LoanOrderQueryBean loqb = (LoanOrderQueryBean) loanobjectlist.get(i);
							System.out.println(loqb);
							// 二次分配列表
							if (StringUtils.isNotBlank(loqb.getSecondaryJsonList()))
							{
								List<Object> loansecondarylist = Common.JSONDecodeList(loqb.getSecondaryJsonList(), LoanOrderQuerySecondaryBean.class);
								if (loansecondarylist != null && loansecondarylist.size() > 0)
								{
									for (int j = 0; j < loansecondarylist.size(); j++)
									{
										if (loansecondarylist.get(j) instanceof LoanOrderQuerySecondaryBean)
										{
											LoanOrderQuerySecondaryBean loqsb = (LoanOrderQuerySecondaryBean) loansecondarylist.get(j);
											System.out.println(loqsb);
										}
									}
								}
							}
						}
					}
				}
			}
			else if (Action.equals("1"))
			{
				// 充值
				List<Object> loanobjectlist = Common.JSONDecodeList(resultarr[1], LoanRechargeOrderQueryBean.class);
				if (loanobjectlist != null && loanobjectlist.size() > 0)
				{
					for (int i = 0; i < loanobjectlist.size(); i++)
					{
						if (loanobjectlist.get(i) instanceof LoanRechargeOrderQueryBean)
						{
							LoanRechargeOrderQueryBean lroqb = (LoanRechargeOrderQueryBean) loanobjectlist.get(i);
							System.out.println(lroqb);
						}
					}
				}
			}
			else if (Action.equals("2"))
			{
				// 提现
				List<Object> loanobjectlist = Common.JSONDecodeList(resultarr[1], LoanWithdrawsOrderQueryBean.class);
				if (loanobjectlist != null && loanobjectlist.size() > 0)
				{
					for (int i = 0; i < loanobjectlist.size(); i++)
					{
						if (loanobjectlist.get(i) instanceof LoanWithdrawsOrderQueryBean)
						{
							LoanWithdrawsOrderQueryBean lwoqb = (LoanWithdrawsOrderQueryBean) loanobjectlist.get(i);
							System.out.println(lwoqb);
						}
					}
				}
			}
		}
		return null;
	}
}
