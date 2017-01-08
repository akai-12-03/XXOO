package com.dept.web.general.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;


public class HttpClientUtil {
	private static final Logger LOGGER = Logger.getLogger(HttpClientUtil.class);
	public static String execute(String url, Map<String, String> map)  {
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		
		try {
			httpClient = new HttpClient();
			postMethod = new PostMethod(url);
			
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			
			for(Entry<String, String> e: map.entrySet()) {
				String key = "";
				String value = "";
				if(EncodeSwitch.getCurrentCode().equalsIgnoreCase("GBK")) {
					 key = new String(e.getKey().getBytes("GBK"));
					 value = new String(e.getValue().getBytes("GBK"));
				}else if(EncodeSwitch.getCurrentCode().equalsIgnoreCase("UTF-8")) {
					 key = new String(e.getKey().getBytes("UTF-8"));
					 value = new String(e.getValue().getBytes("UTF-8"));
				}else if(EncodeSwitch.getCurrentCode().equalsIgnoreCase("GB18030")) {
					key = new String(e.getKey().getBytes("GB18030"));
					 value = new String(e.getValue().getBytes("GB18030"));
				}
				postMethod.setParameter(key, value);
			}	
				
//			postMethod.setParameter("xml",map.get("xml"));
			
			// 执行postMethod
			int statusCode = 0;

			statusCode = httpClient.executeMethod(postMethod);
			
			

			// HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发 301或者302
			String resultStr = "";
			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
			} else {
				resultStr = postMethod.getResponseBodyAsString();
			}
			return resultStr;

		} catch (HttpException httpException) {
			httpException.printStackTrace();
             
		} catch (IOException ioeException) {
			ioeException.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				postMethod.releaseConnection(); // 关闭连接
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
			}
		}
		return null;
	}
	
	/**
	 * 发送请求
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws LotteryException
	 */
	public static void sendHtml(String url,Map<String,String> map,HttpServletResponse response) throws IOException {
//		String mac=DigestUtils.sha256Hex(r.toString()+KEY);
//		String url="http://218.4.234.150:88/main/loan/toloanregisterbind.action";
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		StringBuffer sb=new StringBuffer();
		sb.append("<form id=\"mainform\"  method=\"post\" action=\""+url+"\">\n" );
		for(Map.Entry<String,String> entry: map.entrySet())
		{
			sb.append("<input  type='hidden' name='"+entry.getKey()+"' value='"+entry.getValue()+"' />\n");
		}
		sb.append("</form>\n");
		sb.append("<script language=\"JavaScript\"  type=\"text/JavaScript\">\n");
		sb.append("document.getElementById('mainform').submit();\n");
		sb.append("</script>");
		LOGGER.info("接口发送报文sb===="+sb.toString());
		System.out.println("接口发送报文sb===="+sb.toString());
		out.print(sb.toString());
		out.flush();
		out.close();
     }
	
	
	public static String executeGet(String url)  {
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		
		try {
			httpClient = new HttpClient();
			postMethod = new PostMethod(url);
			
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK");
			
			 //设置 HttpClient 接收 Cookie,用与浏览器一样的策略
			postMethod.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			 //让服务器知道访问源为浏览器

			postMethod.getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; rv:8.0.1) Gecko/20100101 Firefox/8.0.1");
			
			//通过head对象来设置请求头参数
			List<Header> headers = new ArrayList<Header>(); 
			headers.add(new Header("Cookie", "lang=CH; load_balancer=dcd47a65-d505-4de2-a521-d9ca7145da5a; JSESSIONID=981453C8B9516E0DFCA183C1F7F15E0C.node16"));  
			            headers.add(new Header("Connection", "keep-alive"));  
			            postMethod.getHostConfiguration().getParams().setParameter(  
			                    "http.default-headers", headers); 
//			
//			for(Entry<String, String> e: map.entrySet()) {
//				String key = "";
//				String value = "";
//				if(EncodeSwitch.getCurrentCode().equalsIgnoreCase("GBK")) {
//					 key = new String(e.getKey().getBytes("GBK"));
//					 value = new String(e.getValue().getBytes("GBK"));
//				}else if(EncodeSwitch.getCurrentCode().equalsIgnoreCase("UTF-8")) {
//					 key = new String(e.getKey().getBytes("UTF-8"));
//					 value = new String(e.getValue().getBytes("UTF-8"));
//				}else if(EncodeSwitch.getCurrentCode().equalsIgnoreCase("GB18030")) {
//					key = new String(e.getKey().getBytes("GB18030"));
//					 value = new String(e.getValue().getBytes("GB18030"));
//				}
//				postMethod.setParameter(key, value);
//			}	
				
//			postMethod.setParameter("xml",map.get("xml"));
			
			// 执行postMethod
			int statusCode = 0;

			statusCode = httpClient.executeMethod(postMethod);
			
			

			// HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发 301或者302
			String resultStr = "";
			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
			} else {
				resultStr = postMethod.getResponseBodyAsString();
			}
			return resultStr;

		} catch (HttpException httpException) {
			httpException.printStackTrace();
             
		} catch (IOException ioeException) {
			ioeException.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				postMethod.releaseConnection(); // 关闭连接
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
			}
		}
		return null;
	}
	
	
	
}
