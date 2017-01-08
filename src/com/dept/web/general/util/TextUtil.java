package com.dept.web.general.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.sendinfo.xspring.util.log.LogProxy;

public class TextUtil {
	
	protected static final Logger log = LogProxy.getLog();
	
	public static String hideLastChar(String str,int len){
		if(str==null) return null;
		char[] chars=str.toCharArray();
		if(str.length()<=len){
			for(int i=0;i<chars.length;i++){
				chars[i]='*';
			}
		}else{
			for(int i=chars.length-1;i>chars.length-len-1;i--){
				chars[i]='*';
			}
		}
		str=new String(chars);
		return str;
	}
	
	public static String hideFirstChar(String str,int len){
		if(str==null) return null;
		char[] chars=str.toCharArray();
		if(str.length()<=len){
			for(int i=0;i<chars.length;i++){
				chars[i]='*';
			}
		}else{
			for(int i=0;i<1;i++){
				chars[i]='*';
			}
		}
		str=new String(chars);
		return str;
	}
	
	/**
	 * 
	 * @Description:  隐藏手机号码用
	 * @param:        @param str
	 * @param:        @param len
	 * @param:        @return   
	 * @return:       String   
	 * @throws
	 */
	public static String hidePhoneChar(String str,int len){
		if(str==null) return null;
		char[] chars=str.toCharArray();
		for(int i=3;i<chars.length-3;i++){
			chars[i]='*';
		}
		str=new String(chars);
		return str;
	}
	
	/**
	 * 
	 * @Description:  隐藏用户名
	 * @param:        @param username
	 * @param:        @return   
	 * @return:       String   
	 * @throws
	 */
	public static String hideUsernameChar(String username){
		
		if(username==null || username.equals("")){
			
			return null;
			
		}else{
			
			if(RegExUtil.isMobileNO(username)){
				
				return hidePhoneChar(username,4);
				
			}else{
			   	int length = username.length()<3?username.length():username.length()-1;
				String bb = username.substring(1,length);

			   	String cc = "";
			   	
			   	for (int i = 0; i < bb.length(); i++) {
			   		
			   		if(i==0){
			   			cc = bb.replace(String.valueOf(bb.charAt(i)), "*");	
			   		}else{
			   			cc = cc.replace(String.valueOf(bb.charAt(i)), "*");
			   		}
				}
			   	
				return username.replaceFirst(bb, cc);
			}

		}
	}
	
	public static String hideChar(String str,int len){
		if(str==null) return null;
		char[] chars=str.toCharArray();
		for(int i=1;i>chars.length-1;i++){
			if(i<len){
				chars[i]='*';
			}
		}
		str=new String(chars);
		return str;
	}
	
	/**
	 * 如果str为null，返回“”,否则返回str
	 * 
	 * @param str
	 * @return
	 */
	public static String isNull(String str) {
		if (str == null) {
			return "";
		}
		return str;
	}
	
	public static int getInt(String str){
		if(str==null||str.equals(""))
			return 0;
		int ret=0;
		try {
			ret=Integer.parseInt(str);
		} catch (NumberFormatException e) {
			ret=0;
		}
		return ret;
	}
	
	public static double getDouble(String str){
		if(str==null||str.equals(""))
			return 0.0;
		double ret=0.0;
		try {
			ret=Double.parseDouble(str);
		} catch (NumberFormatException e) {
			ret=0.0;
		}
		return format6(ret);
	}
	
	public static double format6(double d){
		DecimalFormat df = new DecimalFormat("0.000000"); 
		String ds=df.format(d);
		return Double.parseDouble(ds);
	}
	
	public static String format(double d){
//		if(d==0){
//			return "";
//		}
		int length = 2; //保留位数
		DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(2);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        String str = formater.format(d);
        if(str.indexOf(".")==-1){
        	str+=".";
        }
    	int strLenth = str.substring(str.indexOf(".")+1).length();
        if (strLenth < length) {
			for (int i = strLenth; i < length ; i++)
				str += "0";
		}
        return str;
	}
	
	public static String format(String str){
		if(null == str){
			return "";
		}
		Double d = new Double(str);
		return format(d);
	}
	
	
	
	/**
	 * 首字母大写
	 * 
	 * @param s
	 * @return
	 */
	public static String firstCharUpperCase(String s) {
		StringBuffer sb = new StringBuffer(s.substring(0, 1).toUpperCase());
		sb.append(s.substring(1, s.length()));
		return sb.toString();
	}
	
	
	/**
	 * 
	 * @Description:  去除HTML标签
	 * @param:        @param inputString
	 * @param:        @return   
	 * @return:       String   
	 * @throws
	 */
	public static String html2Text(String inputString) { 
		
		String htmlStr = inputString; //含html标签的字符串 
		String textStr =""; 
		java.util.regex.Pattern p_script; 
		java.util.regex.Matcher m_script; 
		java.util.regex.Pattern p_style; 
		java.util.regex.Matcher m_style; 
		java.util.regex.Pattern p_html; 
		java.util.regex.Matcher m_html; 
   
      try { 
       String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> } 
       String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> } 
          String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式 
      
          p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
          m_script = p_script.matcher(htmlStr); 
          htmlStr = m_script.replaceAll(""); //过滤script标签 

          p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
          m_style = p_style.matcher(htmlStr); 
          htmlStr = m_style.replaceAll(""); //过滤style标签 
      
          p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
          m_html = p_html.matcher(htmlStr); 
          htmlStr = m_html.replaceAll(""); //过滤html标签 
      
       textStr = htmlStr; 
      
      }catch(Exception e) { 
           e.printStackTrace();
      } 
   
      return textStr;//返回文本字符串 
    } 
	
	
	/**
	 * 
	 * @Description:  TODO
	 * @param:        @param date
	 * @param:        @param vmid
	 * @param:        @param userid
	 * @param:        @param type
	 * @param:        @return   
	 * @return:       String   
	 * @throws
	 */
	public synchronized static String wyTradeNO(String date, String vmid, long userid,String type){
		String s;
		Random rand = new Random((new Date()).getTime());
	    int tmp = Math.abs(rand.nextInt());
	    int retmp = tmp % (99999 - 10000 + 1) + 10000;
		s = date + vmid + type + String.valueOf(retmp);
		return s;
	}	

}
