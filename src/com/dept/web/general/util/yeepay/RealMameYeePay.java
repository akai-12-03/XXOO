package com.dept.web.general.util.yeepay;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.dept.web.controller.WebController;

public class RealMameYeePay extends WebController  {
	
	/**
	 * getCustomerId() : 取得商户编号方法
	 */
	public static String getCustomerId() {
		return Configuration.getInstance().getValue("customerId");
	}

	/**
	 * getKeyValue() : 取得商户密钥方法
	 */
	public static String getKeyValue() {
		return Configuration.getInstance().getValue("keyValue1");
	}

	/**
	 * getRealNameRequestURL() : 取得请求地址
	 */
	public static String getRealNameRequestURL() {
		return Configuration.getInstance().getValue("realNameRequestURL");
	}

	/**
	 * formatString(String text) : 字符串格式化方法
	 */
	public static String formatString(String text) {
		return (text == null ? "" : text.trim());
	}
	/**
	 * realNameVerify() : 实名认证方法
	 * @throws UnsupportedEncodingException 
	 */
	public static Map<String, String> realNameVerify(Map<String, String> params) throws UnsupportedEncodingException {

		System.out.println("##### RealNameVerification.realNameVerify() #####");

		String realNameRequestURL	= getRealNameRequestURL();
		String p0_Cmd				= "FastRealNameAuth";
		String customerId			= getCustomerId();
		String keyValue				= getKeyValue();

		String name             	= formatString(params.get("realname"));
		String idCardNumber     	= formatString(params.get("idcard"));
		String bankCode         	= formatString(params.get("bankCode"));
		String bankCardNumber		= formatString(params.get("bankCardNumber"));
		String bankName          	= formatString(params.get("bankName"));
		String province          	= formatString(params.get("province"));
		String city          		= formatString(params.get("city"));
		String pattern          	= formatString(params.get("pattern"));
		String res_desc         	= formatString(params.get("res_desc"));
		String busId            	= formatString(params.get("busId"));

		String[] strArr			= {p0_Cmd, customerId, name, idCardNumber, bankCode, bankCardNumber, bankName, 
								   province, city, pattern, res_desc};
		String hmac				= DigestUtil.getHmac(strArr, keyValue);

		Map<String, String> requestParams	= new HashMap<String, String>();
		requestParams.put("p0_Cmd",		 p0_Cmd);
		requestParams.put("customerId",  customerId);
		requestParams.put("name", 		 name);
		requestParams.put("idCardNumber",idCardNumber);
		requestParams.put("bankCode", 	 bankCode);
		requestParams.put("bankCardNumber",bankCardNumber);
		requestParams.put("bankName", 	 bankName);
		requestParams.put("province", 	 province);
		requestParams.put("city", 	 	 city);
		requestParams.put("pattern", 	 pattern);
		requestParams.put("res_desc", 	 res_desc);
		requestParams.put("busId",		 busId);
		requestParams.put("hmac", 		 hmac);

		System.out.println("requestParams : " + requestParams);
		System.out.println("realNameRequestURL : " + realNameRequestURL);

		Map<String, String> result 	= new HashMap<String, String>();
		String responseCode			= ""; 
        String command          	= ""; 
        String customerIdYeepay    	= ""; 
        String flowId           	= ""; 
        String authStatus       	= ""; 
        String patternYeepay    	= ""; 
        String auth_Amt         	= ""; 
        String res_descYeepay   	= ""; 
        String authMsg          	= ""; 
		String hmacYeepay			= "";
       	String customError		   	= ""; //自定义，非接口返回。

		List responseList		= null;
		try {
			responseList		= HttpUtils.URLGet(realNameRequestURL, requestParams);
			System.out.println("responseList : " + responseList);

			if(responseList == null) {
				customError	= "No data returned!";
				result.put("customError", customError);
				return (result);
			} else {
				Iterator iter	= responseList.iterator();
				while(iter.hasNext()) {
					String temp = formatString((String)iter.next());
					if(temp.equals("")) {
						continue;
					}
					int i = temp.indexOf("=");
					int j = temp.length();
					if(i >= 0) {
						String tempKey		= temp.substring(0, i);
						String tempValue	= URLDecoder.decode(temp.substring(i+1, j), "GBK");
						if("responseCode".equals(tempKey)) {
							responseCode	= tempValue;
						} else if("command".equals(tempKey)) {
							command			= tempValue;
						} else if("customerId".equals(tempKey)) {
							customerIdYeepay = tempValue;
						} else if("flowId".equals(tempKey)) {
							flowId			= tempValue;
						} else if("authStatus".equals(tempKey)) {
							authStatus		= tempValue;
						} else if("pattern".equals(tempKey)) {
							patternYeepay	= tempValue;
						} else if("auth_Amt".equals(tempKey)) {
							auth_Amt		= tempValue;
						} else if("res_desc".equals(tempKey)) {
							res_descYeepay	= tempValue;
						} else if("authMsg".equals(tempKey)) {
							authMsg			= tempValue;
						} else if("hmac".equals(tempKey)) {
							hmacYeepay	 	= tempValue;
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		String[] stringArr	= {command, customerId, flowId, responseCode, authStatus, pattern, 
							   auth_Amt, res_desc};
		String hmacLocal	= DigestUtil.getHmac(stringArr, keyValue);
		if(!hmacLocal.equals(hmacYeepay)) {
			customError		= "Hmac mismatch error.";
			result.put("customError",  customError);
		}

		result.put("responseCode",responseCode);
		result.put("command", 	command);
		result.put("customerId",customerIdYeepay);
		result.put("flowId", 	flowId);
		result.put("authStatus",authStatus);
		result.put("pattern", 	patternYeepay);
		result.put("auth_Amt", 	auth_Amt);
		result.put("res_desc", 	res_descYeepay);
		result.put("authMsg", 	authMsg);
		result.put("hmac", 		hmacYeepay);

		System.out.println("result : " + result);

		return result;
	}

	
	
}
