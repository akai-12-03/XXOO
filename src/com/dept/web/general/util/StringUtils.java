package com.dept.web.general.util;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
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

	public static String isNull(Object o) {
		if (o == null) {
			return "";
		}
		String str="";
		if(o instanceof String){
			str=(String)o;
		}else{
			str=o.toString();
		}
		return str;
	}
	
	/**
	 * 检查email是否是邮箱格式，返回true表示是，反之为否
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		email = isNull(email);
		Pattern regex = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = regex.matcher(email);
		boolean isMatched = matcher.matches();
		return isMatched;
	}
	
	/**
	 * 检查身份证的格式，返回true表示是，反之为否
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isCard(String cardId) {
		cardId = isNull(cardId);
		//身份证正则表达式(15位) 
		Pattern isIDCard1=Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$"); 
		//身份证正则表达式(18位) 
		Pattern isIDCard2=Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$"); 
		Matcher matcher1= isIDCard1.matcher(cardId);
		Matcher matcher2= isIDCard2.matcher(cardId);
		boolean isMatched = matcher1.matches()||matcher2.matches();
		return isMatched;
	}

	/**
	 * 判断字符串是否为整数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		if (isEmpty(str)) {
			return false;
		}
		Pattern regex = Pattern.compile("\\d*");
		Matcher matcher = regex.matcher(str);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if (isEmpty(str)) {
			return false;
		}

		Pattern regex = Pattern.compile("\\d*(.\\d*)?");
		Matcher matcher = regex.matcher(str);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
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
	
	/**
	 * 
	 * @return
	 */
	public static String format(String str,int len){
		if(str==null) return "-";
		if(str.length()<=len){
			int pushlen=len-str.length();
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<pushlen;i++){
				sb.append("0");
			}
			sb.append(str);
			str=sb.toString();
		}else{
			String newStr=str.substring(0, len);
			str=newStr;
		}
		return str;
	}
	
	public static String contact(Object[] args){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<args.length;i++){
			sb.append(args[i]);
			if(i<args.length-1){
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 是否包含在以“，”隔开字符串内
	 * @param s
	 * @param type
	 * @return
	 */
	public static boolean isInSplit(String s,String type){
		if(isNull(s).equals("")){
			return false;
		}
		List<String> list=Arrays.asList(s.split(","));
		if(list.contains(type)){
			return true;
		}
		return false;
	}
	
	public static boolean isBlank(String str){
		return StringUtils.isNull(str).equals("");
	}
	

	
	public synchronized static String wyTradeNO(String date, String vmid, long userid,String type){
		String s;
		Random rand = new Random((new Date()).getTime());
	    int tmp = Math.abs(rand.nextInt());
	    int retmp = tmp % (99999 - 10000 + 1) + 10000;
		s = date + vmid + type + String.valueOf(retmp);
		return s;
	}	
	
	

	public static String array2Str(Object[] arr){
		StringBuffer s=new StringBuffer();
		for(int i=0;i<arr.length;i++){
			s.append(arr[i]);
			if(i<arr.length-1){
				s.append(",");
			}
		}
		return s.toString();
	}
	
	public static String array2Str(int[] arr){
		StringBuffer s=new StringBuffer();
		for(int i=0;i<arr.length;i++){
			s.append(arr[i]);
			if(i<arr.length-1){
				s.append(",");
			}
		}
		return s.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(StringUtils.isNumber("112"));
	}
}
