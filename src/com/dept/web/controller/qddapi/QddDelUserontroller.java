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
import com.dept.web.general.util.HttpClientUtil;
/**
 * 托管平台：钱多多<用户账号:余额查询>
 */
@Controller
public class QddDelUserontroller extends WebController {
	protected String PlatformId = "";
	protected String PlatformType = "";
	/**
	 * 生成余额查询信息
	 */
	@RequestMapping("qddApi/delUser")
	public String loanBalanceQuery(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		PlatformMoneymoremore = Global.getValue("qdd_PlatformMoneymoremore");
//		SubmitURLPrefix=Global.getString("qdd_submitUrl");
		SubmitURL ="http://218.4.234.150:88/main/loantest/loandeletetestinfo.action";
		
		
		String privatekey = Common.privateKeyPKCS8;
		
		String dataStr =PlatformMoneymoremore.replace("p", "");
		// 签名
//		MD5 md5 = new MD5();
		RsaHelper rsa = RsaHelper.getInstance();
//		if (antistate == 1)
//		{
//			dataStr = md5.getMD5Info(dataStr);
//		}
		SignInfo = rsa.signData(dataStr, privatekey);
		
		Map<String, String> req = new TreeMap<String, String>();
		req.put("p", dataStr);
		req.put("s", SignInfo);
		
		HttpClientUtil.sendHtml(SubmitURL, req, response);
		return null;
	}
	
	
}
