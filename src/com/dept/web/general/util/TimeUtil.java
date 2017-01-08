package com.dept.web.general.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.sendinfo.common.lang.StringUtil;

/**
 * 
 * @ClassName:     TimeUtil.java
 * @Description:   TODO
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-8-15 上午11:08:20
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class TimeUtil {

	/**
	 * 将秒转换成时间
	 * @param times
	 * @return
	 */
	public static Date getDate(String times) {
		long time = Long.parseLong(times);
		return new Date(time*1000);
	}
	/**
	 * 将秒转换成   年-月-日   时：分：秒
	 * @param times
	 * @return
	 */
	public static String getStr(String times){
		if(times!=null && !times.equals("")){
		long time = Long.parseLong(times)*1000;
		  Timestamp ts = new Timestamp(time);  
          String tsStr = "";  
          DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
          tsStr = sdf.format(ts);  
		return tsStr;  
		}else{
			return "";
		}
	}
	/**
	 * 将秒转换成   年-月-日   时：分：秒
	 * @param times
	 * @return
	 */
	public static String getStr2(Long times){
		if(times!=null && !times.equals("")){
		long time = times*1000;
		  Timestamp ts = new Timestamp(time);  
          String tsStr = "";  
          DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
          tsStr = sdf.format(ts);  
		return tsStr;  
		}else{
			return "";
		}
	}
	/**
	 * 将秒转换成   年-月-日   时：分：秒
	 * @param times
	 * @return
	 */
	public static String getStrS(Long times){
		if(times!=null && !times.equals("")){
		long time = times*1000;
		  Timestamp ts = new Timestamp(time);  
          String tsStr = "";  
          DateFormat sdf = new SimpleDateFormat("HH:mm");  
          tsStr = sdf.format(ts);  
		return tsStr;  
		}else{
			return "";
		}
	}
	
	
	/**
	 * 将秒转换成   年-月-日   时：分：秒
	 * @param times
	 * @return
	 */
	public static String getStr3(Long times){
		if(times!=null && !times.equals("")){
		long time = times*1000;
		  Timestamp ts = new Timestamp(time);  
          String tsStr = "";  
          DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
          tsStr = sdf.format(ts);  
		return tsStr;  
		}else{
			return "";
		}
	}
	/**
	 * 将秒转换成  月-日
	 * @param times
	 * @return
	 */
	public static String getStr22(String times){
		if(times!=null && !times.equals("")){
		long time = Long.parseLong(times)*1000;
		  Timestamp ts = new Timestamp(time);  
          String tsStr = "";  
          DateFormat sdf = new SimpleDateFormat("MM-dd");  
          tsStr = sdf.format(ts);  
		return tsStr;  
		}else{
			return "";
		}
	}
	/**
	 * 将秒转换成   年-月-日  
	 * @param times
	 * @return
	 */
	public static String getNYRStr(String times){
		if(times!=null && !times.equals("")){
			long time = Long.parseLong(times)*1000;
			Timestamp ts = new Timestamp(time);  
			String tsStr = "";  
			DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");  
			tsStr = sdf.format(ts);  
			return tsStr;  
		}else{
			return "";
		}
	}
	
	/**
	 * 将秒转换成  yyyy年MM月dd日 HH:mm
	 * @param times
	 * @return
	 */
	public static String dateStr(String times) {
		return dateStr(getDate(times));
	}
	
	/**
	 * 将时间转换成 yyyy年MM月dd日 HH:mm
	 * @param date
	 * @return
	 */
	public static String dateStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		String str = format.format(date);
		return str;
	}
	/**
	 * 获取当前时间的 yyyy年MM月dd日 HH:mm
	 * @return
	 */
	public static String getNow(){
		
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		return dateStr(now);
		
	}
	/**
	 * 
	 * @return
	 */
	public static String getNowTimeStr(){
		String str=Long.toString(System.currentTimeMillis() / 1000);
		return str;
	}

	/**
	 * 
	 * @Description:  剩余时间
	 * @param:        @param time
	 * @param:        @param datevalue
	 * @param:        @return   
	 * @return:       String   
	 * @throws
	 */
	public static String getRemainTime(String time, String datevalue) {

		String tt="不详";
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		/** 系统时间 */
	    String str1=sdf.format(new java.util.Date());
	    Calendar cal1=Calendar.getInstance();
	    try {
			cal1.setTime(sdf.parse(str1));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    long l=Long.valueOf(time)*1000+(Integer.valueOf(datevalue)*1000*60*60*24)-cal1.getTimeInMillis();
	    
	    int days=new Long(l/(1000*60*60*24)).intValue()+1;
	   
	   
	    if(days==1){
	     tt="最后一天";
	    }
	    
	    else if(days==2){
	     tt="最后两天";
	    }

	    else if(days==3){
	     tt="最后三天";
	    }
	   
	   
	    else if(days<0){
	     tt="已结束";
	    }
	   
	    else{
	     tt="还剩"+days+"天";
	    }
		return tt;
		
	}
	
	/**
	 * 传入时间加上参数获得之后的时间 ---传入时只用传入一个即可，多传入不做计算,其他参数传入0
	 * @param timeStr   传入时间类型 1406703365 time 
	 * @param month     获取传入时间几个月后的时间。
	 * @param day       获取传入时间几个天后的时间。
	 * @param hour	        获取传入时间几个小时后的时间
	 * @param minute    获取传入时间几分钟后的时间
	 * @param second    获取传入时间几秒后的时间
	 * @return    返回参数类型 yyyy-MM-dd HH:mm:ss
	 */
	public static  String getEndTime(String timeStr,Integer month,Integer day,Integer hour,Integer minute,Integer second){
		String strDate="";
		try {
				Calendar calendar = Calendar.getInstance();
				String time =   TimeUtil.getStr(timeStr.trim());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");       
				Date date = sdf.parse(time);
				calendar.setTime(date);
				if(month>0 && day==0 && hour==0 && minute==0 && second==0){
					calendar.add(Calendar.MONTH, month);
				}else if(month==0 && day>0 && hour==0 && minute==0 && second==0){
					calendar.add(Calendar.DATE, day);
				}else if(month==0 && day==0 && hour>0 && minute==0 && second==0){
					calendar.add(Calendar.HOUR, day);
				}else if(month==0 && day==0 && hour==0 && minute>0 && second==0){
					calendar.add(Calendar.MINUTE, minute);
				}else if(month==0 && day==0 && hour==0 && minute==0 && second>0){
					calendar.add(Calendar.SECOND, second);
				}
				strDate = sdf.format(calendar.getTime()); 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}           
		return strDate;
	}
	
	
	/**
	 * 传入时间加上参数获得之后的时间 ---传入时只用传入一个即可，多传入不做计算,其他参数传入0
	 * @param timeStr   传入时间类型 1406703365 time 
	 * @param month     获取传入时间几个月后的时间。
	 * @param day       获取传入时间几个天后的时间。
	 * @param hour	        获取传入时间几个小时后的时间
	 * @param minute    获取传入时间几分钟后的时间
	 * @param second    获取传入时间几秒后的时间
	 * @return    返回参数类型 yyyy-MM-dd HH:mm:ss
	 */
	public static  String getEndTimeHMS(String timeStr,Integer month,Integer day,Integer hour,Integer minute,Integer second){
		String strDate="";
		try {
				Calendar calendar = Calendar.getInstance();
				String time =   TimeUtil.getStr(timeStr.trim());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
				Date date = sdf.parse(time);
				calendar.setTime(date);
				if(month>0 && day==0 && hour==0 && minute==0 && second==0){
					calendar.add(Calendar.MONTH, month);
				}else if(month==0 && day>0 && hour==0 && minute==0 && second==0){
					calendar.add(Calendar.DATE, day);
				}else if(month==0 && day==0 && hour>0 && minute==0 && second==0){
					calendar.add(Calendar.HOUR, day);
				}else if(month==0 && day==0 && hour==0 && minute>0 && second==0){
					calendar.add(Calendar.MINUTE, minute);
				}else if(month==0 && day==0 && hour==0 && minute==0 && second>0){
					calendar.add(Calendar.SECOND, second);
				}
				strDate = sdf.format(calendar.getTime()); 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}           
		return strDate;
	}
	
	
	/**
	 * +
	 * 
	 * @Description: 判断是否过期
	 * @param:        @param time
	 * @param:        @param datevalue
	 * @param:        @return   
	 * @return:       boolean   
	 * @throws
	 */
	public static boolean isOver(String time, String datevalue) {
		if(time.equals(""))
		{
			return true;
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		/** 系统时间 */
	    String str1=sdf.format(new java.util.Date());
	    Calendar cal1=Calendar.getInstance();
	    try {
			cal1.setTime(sdf.parse(str1));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    long l=Long.valueOf(time)*1000+(Integer.valueOf(datevalue)*1000*60*60*24)-cal1.getTimeInMillis();
	    
	    int days=new Long(l/(1000*60*60*24)).intValue()+1;
	    
	    if(days<0){
	    	return true;
	    }else{
	    	return false;
	    }
		
	}
	
	public static String changeBankNum(String bankNo){
		String strCount =bankNo;
		if(!StringUtil.isEmpty(bankNo)){
			if(bankNo.length()>8){
				String account1 = bankNo.substring(0, 4);
				String account2 = bankNo.substring(bankNo.length()-4, bankNo.length());
				String str="";
				if(account1.length()>0 & account2.length()>0){
					String	account3=bankNo.substring(account1.length(), bankNo.length()-4);
					if(account3.length()>0){
						for(int i=0;i<account3.length();i++){
							str+="*";
						}
						strCount = account1+str+account2;
					}
					
				}
				
			}
		}
		return strCount;
	}
	
	public static String dateStr2(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = format.format(date);
		return str;
	}
	
	
	

	/**
	 * 将秒转换成时间
	 * @param times
	 * @return
	 */
	public static Date getDate(long times) {
		return new Date(times*1000);
	}
	public static String dateStr2s(String date,String f){
		if(date==null){
			return null;
		}
		 SimpleDateFormat format = new SimpleDateFormat(f);
	        String str = format.format(getDate(Long.parseLong(date)));
	        
		return str;
	}

	
	public static void main(String[] args) throws ParseException {
//	    try {  
//            URL u = new URL("https://www.yeepay.com/app-merchant-proxy/command");  
//            try {  
//                HttpURLConnection uConnection = (HttpURLConnection) u.openConnection();  
//                try {  
//                    uConnection.connect();  
//                    System.out.println(uConnection.getResponseCode());  
//                    //connect = true;  
//                    InputStream is = uConnection.getInputStream();  
//                    BufferedReader br = new BufferedReader(new InputStreamReader(is));  
//                    StringBuilder sb = new StringBuilder();  
//                    while(br.read() != -1){  
//                        sb.append(br.readLine());  
//                    }  
//                    String content = new String(sb);  
//                    content = new String(content.getBytes("GBK"), "ISO-8859-1");  
//                    System.out.println(content);  
//                    br.close();  
//                } catch (Exception e) {  
//                    //connect = false;  
//                    e.printStackTrace();  
//                    System.out.println("connect failed");  
//                }  
//                  
//            } catch (IOException e) {  
//                System.out.println("build failed");  
//                e.printStackTrace();  
//            }  
//              
//        } catch (MalformedURLException e) {  
//            System.out.println("build url failed");  
//            e.printStackTrace();  
//        } 
		
		System.out.println(getStr("1450777395"));
		
		
//		
//		
//			Long time =1449575697L;
//			
//			Long times = time - Long.valueOf(TimeUtil.getNowTimeStr());
//			
//			if(times>=0){
//				System.out.println("1");
//			}else{
//				System.out.println("2");
//			}
//		
		
		
		
	
	}
	
	
	
	 /**
		 * +
		 * 
		 * @Description: 判断是否过期
		 * @param:        @param time
		 * @param:        @param datevalue
		 * @param:        @return   
		 * @return:       boolean   
		 * @throws
		 */
		public static boolean isOver2(String time, String endtime) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			Date date =null;
		    try {
		    	 if(StringUtils.isEmpty(endtime))
		    	 {
		    		 return true;
		    	 }
				 date = sdf.parse(endtime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Long endTime = date.getTime()/1000;
			Long times = endTime - Long.valueOf(TimeUtil.getNowTimeStr());
			
			if(times>=0){
				return false;
			}else{
				return true;
			}
			
		}
		
		
		public static String getNow2(){
			
			Date now = new Date();
			return dateStr3(now);
			
		}
		
		public static String dateStr3(Date date) {
			SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd");
			String str = format.format(date);
			return str;
		}
}
