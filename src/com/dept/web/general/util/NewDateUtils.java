package com.dept.web.general.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewDateUtils {
    
    public static String dateStr(Long date,String f) {
        if(date==null){
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(f);
        String str = format.format(getDate(date));
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


	public static long getTime(Date date) {
		return date.getTime() / 1000;
	}

	public static int getDay(Date d){
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * s - 表示 "yyyy-mm-dd" 形式的日期的 String 对象 
	 * @param f
	 * @return
	 */
	public static Date valueOf(String s){
		final int YEAR_LENGTH = 4;
        final int MONTH_LENGTH = 2;
        final int DAY_LENGTH = 2;
        final int MAX_MONTH = 12;
        final int MAX_DAY = 31;
        int firstDash;
        int secondDash;
        Date d = null;

        if (s == null) {
            throw new java.lang.IllegalArgumentException();
        }

        firstDash = s.indexOf('-');
        secondDash = s.indexOf('-', firstDash + 1);
        if ((firstDash > 0) && (secondDash > 0) && (secondDash < s.length()-1)) {
            String yyyy = s.substring(0, firstDash);
            String mm = s.substring(firstDash + 1, secondDash);
            String dd = s.substring(secondDash + 1);
            if (yyyy.length() == YEAR_LENGTH && mm.length() == MONTH_LENGTH &&
                dd.length() == DAY_LENGTH) {
                int year = Integer.parseInt(yyyy);
                int month = Integer.parseInt(mm);
                int day = Integer.parseInt(dd);
                if (month >= 1 && month <= MAX_MONTH) {
                    int maxDays = MAX_DAY;
                    switch (month) {
                        // February determine if a leap year or not
                        case 2:
                            if((year % 4 == 0 && !(year % 100 == 0)) || (year % 400 == 0)) {
                                maxDays = MAX_DAY-2; // leap year so 29 days in February
                            } else {
                                maxDays = MAX_DAY-3; //  not a leap year so 28 days in February 
                            }
                            break;
                        // April, June, Sept, Nov 30 day months
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            maxDays = MAX_DAY-1;
                            break;
                    }
                    if (day >= 1 && day <= maxDays) {
                        Calendar cal=Calendar.getInstance();
                        cal.set(year, month - 1, day,0,0,0);
                        cal.set(Calendar.MILLISECOND, 0);
                        d=cal.getTime();
                    }
                }
            }
        }
        if (d == null) {
            throw new java.lang.IllegalArgumentException();
        }
        return d;
	}
	
	/**
	 * @author lijie
	 * @param Begin
	 * @param end
	 * 传入开始时间 和 结束时间 格式如：2012-09-07
	 * @return
	 * 返回Map  获取相隔多少年 get("YEAR")及为俩个时间年只差，月 天，类推
	 *  Key ：
	 *  YEAR MONTH DAY
	 *  如果开始时间 晚于 结束时间 return null；
	 */
	
	public static Map getApartTime(String Begin, String end) {
		  String[] temp = Begin.split("-");
	        String[] temp2 = end.split("-");
	        if (temp.length > 1 && temp2.length > 1) {
	            Calendar ends = Calendar.getInstance();
	            Calendar begin = Calendar.getInstance();

	            begin.set(NumberUtil.getInt(temp[0]),
	                    NumberUtil.getInt(temp[1]), NumberUtil.getInt(temp[2]));
	            ends.set(NumberUtil.getInt(temp2[0]),
	                    NumberUtil.getInt(temp2[1]), NumberUtil.getInt(temp2[2]));
	            if (begin.compareTo(ends) < 0) {
	                Map map = new HashMap();
	                ends.add(Calendar.YEAR, -NumberUtil.getInt(temp[0]));
	                ends.add(Calendar.MONTH, -NumberUtil.getInt(temp[1]));
	                ends.add(Calendar.DATE, -NumberUtil.getInt(temp[2]));
	                map.put("YEAR", ends.get(Calendar.YEAR));
	                map.put("MONTH", ends.get(Calendar.MONTH) + 1);
	                map.put("DAY", ends.get(Calendar.DATE));
	                return map;
	            }
	        }
	        return null;
	}
	
	public static Date rollDay(Date d,int day){
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}
	public static Date rollMon(Date d,int mon){
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MONTH, mon);
		return cal.getTime();
	}
	public static Date rollYear(Date d,int year){
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.YEAR, year);
		return cal.getTime();
	}
	public static Date rollDate(Date d,int year,int mon,int day){
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, mon);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}
	
	
	/**
	 * 获取当前时间的秒数字符串
	 * @return
	 */
	public static Long getNowTimeStr(){
		return System.currentTimeMillis() / 1000;
	}
	public static String getTimeStr(Date time){
		long date = time.getTime();
		String str=Long.toString(date / 1000);
		return str;
	}
	public static String rollMonth(String addtime,String time_limit){
		Date t=NewDateUtils.rollDate(NewDateUtils.getDate(Long.valueOf(addtime)), 0, NumberUtil.getInt(time_limit),0);
		return t.getTime()/1000+"";
	}
	
	public static String rollDay(String addtime,String time_limit_day){
		Date t=NewDateUtils.rollDate(NewDateUtils.getDate(Long.valueOf(addtime)), 0, 0,NumberUtil.getInt(time_limit_day));
		return t.getTime()/1000+"";
	}
	
	public static Date getIntegralTime(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	public static Date getLastIntegralTime(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	public static Date getLastSecIntegralTime(Date d){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(d.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static long getTime(String format){
		long t=0;
		if(StringUtils.isBlank(format)) return t;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		try {
			date = sdf.parse(format);
			t=date.getTime()/1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * 从第二天开始算起 如计算5月1日到10月1日，计算从5月2日开始到10月1日的天数
	 * @Title: daysBetween 
	 * @Description: TODO
	 * @param @param smdate
	 * @param @param bdate
	 * @param @return
	 * @param @throws ParseException 设定文件 
	 * @return int 返回类型 
	 * @throws
	 */
    public static int daysBetween(String smdate,String bdate) throws ParseException{  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(sdf.parse(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));     
    } 
	
    /**
     * 获取当天开始时间
     * @Title: getStartTime 
     * @Description: TODO
     * @param @return 设定文件 
     * @return Long 返回类型 
     * @throws
     */
    public static Long getStartTime(){
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime()/1000;
    }
    
    /**
     * 获取当天结束时间
     * @Title: getEndTime 
     * @Description: TODO
     * @param @return 设定文件 
     * @return Long 返回类型 
     * @throws
     */
    public static Long getEndTime(){
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime()/1000;
    }    
    
    public static void main(String[] args) {
        System.out.println(getNowTimeStr());
        System.out.println(getStartTime());
        System.out.println(getStartTime()+60*60*10);
        System.out.println(getEndTime());
        System.out.println(dateStr(1431532799L, "yyyyMMddHHmmss"));
    }  
}
