package com.dept.web.general.javamail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

import com.dept.web.context.Global;
import com.dept.web.dao.model.SystemInfo;
import com.dept.web.dao.model.User;
import com.dept.web.general.util.BASE64Encoder;
import com.dept.web.general.util.MD5;


public class Mail {
	
	private static Logger logger = Logger.getLogger(Mail.class);
	
	private String host;
	private String from;
	private String to ;
	private String subject ;
	private String body ;
	private EmailAutherticator auth;
	private static Mail mail;
	
	String username = "sxdp2p@163.com";

	String password = "evwkculnuhpbdtrp";
	
	String emailhost = "smtp.163.com";
	
	String email_from_name = "金生通财网邮箱认证";
	
	String weburl = "www.sxd.com";// 官网
	public String webname = "金生通财网";// 网站名称
	String fuwutel = "4001-537-538";// 服务热线
	
	 static {
		 mail = new Mail();
	 }
	
	private Mail() {
		init();
	}

	private void init(){
		EmailAutherticator auth = new EmailAutherticator(username, password);
		
		host=emailhost;
		from=auth.getUsername();
		logger.info("From："+from);
		subject=email_from_name;
		logger.info("数据库读取："+subject);
		this.setAuth(auth);
		this.setHost(host);
		this.setSubject(subject);
		this.setFrom(from);
	}
	
	public static Mail getInstance(){
		return mail;
	}
	
	public String transferChinese(String strText)   {   
		try{   
			strText = MimeUtility.encodeText(new String(strText.getBytes(), "utf-8"), "utf-8", "B");   
        }catch(Exception e){   
            e.printStackTrace();   
        }   
        return strText;   
    }
	
	public void sendMail() throws Exception {
			Properties props = new Properties();
//			Authenticator auth = new EmailAutherticator("312751313@qq.com","fuljia0717");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");
			System.out.println(props);
			Session session = Session.getDefaultInstance(props, auth);
			System.out.println("=================");
			session.setDebug(true);
			System.out.println("=================");
			MimeMessage message = new MimeMessage(session);
			message.setContent("Hello", "text/plain");
			logger.info(subject);
			//subject = transferChinese(subject);  
			//logger.info(subject);
			message.setSubject(subject,"utf-8");// 设置邮件主题   
			message.setSentDate(new Date());// 设置邮件发送时期
			Address address = new InternetAddress(from, subject,"utf-8");
			
			message.setFrom(address);// 设置邮件发送者的地址
			Address toaddress = new InternetAddress(to);// 设置邮件接收者的地址
			message.addRecipient(Message.RecipientType.TO, toaddress);
			// 创建一个包含HTML内容的MimeBodyPart    
			Multipart mainPart = new MimeMultipart();    
		    BodyPart html = new MimeBodyPart();     
		    html.setContent(body, "text/html; charset=utf-8");    
		    mainPart.addBodyPart(html);    
	    // 将MiniMultipart对象设置为邮件内容
		    message.setContent(mainPart);    
			
			System.out.println(message);
			logger.debug("TO:"+to);
			Transport.send(message);
			
			System.out.println("Send Mail Ok!");
	}
	
	public void sendMail(String to,String subject,String content) throws Exception {
		this.setTo(to);
		this.setSubject(subject);
		this.setBody(content);
		this.sendMail();
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public EmailAutherticator getAuth() {
		return auth;
	}
	public void setAuth(EmailAutherticator auth) {
		this.auth = auth;
	}
	
	public void readActiveMailMsg(){
		this.readMsg("mail.msg");
	}
	
	public void readGetpwdMailMsg(){
		this.readMsg("getpwd.msg");
	}
	
	private void readMsg(String filename){
		StringBuffer sb=new StringBuffer();
		InputStream fis=Mail.class.getResourceAsStream(filename);
		InputStreamReader isr=null;
		try {
			isr=new InputStreamReader(fis,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("读取文件遇见不正确的文件编码！");
		}
		BufferedReader br=new BufferedReader(isr);
		String line="";
		try {
			while((line=br.readLine())!=null){
				sb.append(line);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		String msg=sb.toString();
		logger.info(msg);
		setBody(msg);
	}
	
	
	public String getdecodeIdStr(User user){
		String chars="0123456789qwertyuiopasdfghjklmnbvcxz-=~!@#$%^*+-._/*<>|";
		int length=chars.length();
		StringBuffer url=new StringBuffer();
		StringBuffer rancode=new StringBuffer();
		String timeStr=System.currentTimeMillis()/1000+"";
		String userIDAddtime = user.getId()+String.valueOf(user.getCreatedAt());
		MD5 md5=new MD5();
		userIDAddtime =  md5.getMD5ofStr(userIDAddtime);
		url.append(user.getId()).append(",").append(userIDAddtime).append(",").append(timeStr).append(",");
		for(int i=0;i<10;i++){
			int num=(int)(Math.random()*(length-2))+1;
			rancode.append(chars.charAt(num));
		}
		url.append(rancode);
		String idurl=url.toString();
		BASE64Encoder encoder=new BASE64Encoder();
		String s=encoder.encode(idurl.getBytes());
		return s;
	}
	
	public void replace(String webname,String host,String username,String email,String url){
		String msg=this.getBody();
		msg=msg.replace("$webname$", webname).replace("$email$", email).replace("$host$", host).replace("$username$", username)
				.replace("$url$", host+url);
		this.setBody(msg);
	}
	
	public void replace(String username,String email,String url){
		SystemInfo info=Global.SYSTEMINFO;
		String weburl=info.getValue("weburl");
		//String webname=info.getValue("webname");
		replace(webname,weburl,username,email,url);
	}
	public void readPayBorrowForBorrowerMsg() {
		this.readMsg("emailpayborrowforborrower.msg");
	}/*public String bodyReplace(Map<String, String> map) {
		SystemInfo info = Global.SYSTEMINFO;
		String weburl = info.getValue("weburl");// 官网
		String webname = info.getValue("webname");// 网站名称
		String fuwutel = info.getValue("fuwutel");// 服务热线

		String username = map.get("username") != null ? map.get("username")
				: "";// 用户名
		String no = map.get("no") != null ? map.get("no") : ""; //手机号或者身份证号
		if (no != "") {
			no = no.substring(0, no.length() - 4) + "****";
		}

		String status = map.get("status") != null ? map.get("status") : "";// 认证状态
		String type = map.get("type") != null ? map.get("type") : "";// “手机”或者“身份证”

		String money = map.get("money") != null ? map.get("money") : "";// 提现、充值金额
		String time = map.get("time") != null ? map.get("time") : "";// 提现充值时间
		String m = map.get("msg") != null ? map.get("msg") : "";// 提示信息
		String fee = map.get("fee") != null ? map.get("fee") : "";// 手续费

		String borrowname = map.get("borrowname") != null ? map
				.get("borrowname") : "";// 标名
		String account = map.get("account") != null ? map.get("account") : "";// 借款总额
		String apr = map.get("apr") != null ? map.get("apr") : "";// 预期年化收益

		String timelimit = map.get("timelimit") != null ? map.get("timelimit")
				: "";// 借款期限

		String order = map.get("order") != null ? map.get("order") : "";// 借款当前期

		String latedays = map.get("latedays") != null ? map.get("latedays")
				: "";// 逾期天数

		String lateInterest = map.get("lateInterest") != null ? map
				.get("lateInterest") : "";// 逾期利息
		String msg = this.getBody();
		msg = msg.replace("$webname$", webname).replace("$host$", weburl)
			.replace("$username$", username).replace("$NO$", no)
			.replace("$status$", status).replace("$type$", type)
				.replace("$money$", money).replace("$time$", time)
				.replace("$msg$", m).replace("$fee$", fee)
			.replace("$fuwutel$", fuwutel)
				.replace("$borrowname$", borrowname)
				.replace("$account$", account).replace("$apr$", apr)
				.replace("$timelimit$", timelimit).replace("$order$", order)
				.replace("$latedays$", latedays)
				.replace("$lateInterest$", lateInterest);

		return msg;
	}*/
}