package com.dept.web.general.util.mmsg;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.util.WebUtils;

import com.dept.web.context.Global;
import com.jianzhou.sdk.BusinessService;


/**
 * 
 * @ClassName:     SendMessageUtil.java
 * @Description:   发送短信彩信工具
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-10-10 上午10:57:48
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class SendMessageUtil {
	protected static final Logger LOGGER = Logger.getLogger(SendMessageUtil.class);
	
	/**
	 * 
	 * @Description:  发送短信
	 * @param:        @param phone
	 * @param:        @param content
	 * @param:        @return
	 * @param:        @throws InterruptedException   
	 * @return:       boolean   
	 * @throws
	 */
	public static boolean sendSMS(String phone, String text) throws Exception{
		
		
		//String Text=URLEncoder.encode("您的验证码：8859【华信】","utf-8");
//		String Text="您的验证码：8859【华信】";
		
		String Url=Global.getValue("sendSMSUrl");
		
		HttpClient client=new HttpClient();
		PostMethod post=new PostMethod(Url);
		post.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		NameValuePair userid=new NameValuePair("userid","");
		NameValuePair account=new NameValuePair("account",Global.getValue("sendSMSName"));
		NameValuePair password=new NameValuePair("password",Global.getValue("sendSMSPwd"));
		NameValuePair mobile=new NameValuePair("mobile",phone);
		NameValuePair content=new NameValuePair("content",text);
		NameValuePair sendTime=new NameValuePair("sendTime","");
		NameValuePair extno=new NameValuePair("extno","");
		post.setRequestBody(new NameValuePair[]{userid,account,password,mobile,content,sendTime,extno});
		int statu=client.executeMethod(post);
		System.out.println("statu="+statu);
		String str=post.getResponseBodyAsString();
		
		System.out.println(str);
		
//		HttpMethod method=new PostMethod(Url);//带参数的Url
//		method.setRequestHeader("Content-type", "text/xml; charset=utf-8");
//		client.executeMethod(method);
//		String str = method.getResponseBodyAsString();
//		System.out.println("result="+str);
		
		try {
			//将字符转化为XML
			Document doc=DocumentHelper.parseText(str);
			//获取根节点
			Element rootElt=doc.getRootElement();
			//获取根节点下的子节点的值
			String returnstatus=rootElt.elementText("returnstatus").trim();
			String message=rootElt.elementText("message").trim();
			String remainpoint=rootElt.elementText("remainpoint").trim();
			String taskID=rootElt.elementText("taskID").trim();
			String successCounts=rootElt.elementText("successCounts").trim();
			
			LOGGER.debug("返回状态为："+returnstatus);
			LOGGER.debug("返回信息提示："+message);
			LOGGER.debug("返回余额："+remainpoint);
			LOGGER.debug("返回任务批次："+taskID);
			LOGGER.debug("返回成功条数："+successCounts);
		} catch (Exception e) {
			System.out.println(e);
		}
		return true;
	}
}
