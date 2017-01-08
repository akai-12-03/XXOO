package com.dept.web.service.pay.beifu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.mypay.merchantutil.Md5Encrypt;
import com.mypay.merchantutil.UrlHelper;
import com.mypay.merchantutil.timestamp.TimestampResponseParser;
import com.mypay.merchantutil.timestamp.TimestampResponseResult;
import com.mypay.merchantutil.timestamp.TimestampUtils;

/**
 * 贝付获取时间戳
 * @author Administrator
 *
 */
public class AskForTimestamp {

	/**
	 * 请求时间戳
	 * 
	 * @return
	 * @throws Exception
	 */
	public String run() throws Exception {
		// 请求参数排序
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("service",
				new String[] { BeiFuConstant.service_query_timestamp });
		params.put("partner", new String[] { BeiFuConstant.partner });
		params.put("input_charset",
				new String[] { BeiFuConstant.input_charset });
		params.put("sign_type", new String[] { BeiFuConstant.sign_type });
		String paramStr = UrlHelper.sortParamers(params);

		String plaintext = TimestampUtils.mergePlainTextWithMerKey(paramStr,
				BeiFuConstant.key);

		// 加密(MD5加签)，默认已取UTF-8字符集，如果需要更改，可调用Md5Encrypt.setCharset(inputCharset)
		String sign = Md5Encrypt.encrypt(plaintext);

		// 拼接请求
		String url = BeiFuConstant.ask_for_time_stamp_gateway + "?" + paramStr + "&sign=" + sign;
		// 通过HttpClient获取响应字符串
		HttpClient httpClient = new HttpClient();
		HttpMethod method = new GetMethod(url);

		String askForTimestampResponseString = null;

		try {
			httpClient.executeMethod(method);

			// 如果响应码为200，从响应中获取响应字符串
			if (method.getStatusCode() == 200) {

				askForTimestampResponseString = method.getResponseBodyAsString();

				System.out.println(askForTimestampResponseString);
			}

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection(); // 释放连接
		}
		// 验证时间戳有效性
		TimestampResponseResult result = TimestampResponseParser
				.parse(askForTimestampResponseString);

		String timestamp = null;

		if (result.isSuccess()) {
			timestamp = result.getTimestamp();
			String resultMd5 = result.getResultMd5();
			String timestampMergeWithMerKey = TimestampUtils.mergePlainTextWithMerKey(timestamp, BeiFuConstant.key);
			System.out.println("时间戳：" + timestamp);
			System.out.println("有效性：" + resultMd5.equals(Md5Encrypt.encrypt(timestampMergeWithMerKey)));
		}

		return timestamp;
	}
}
