package com.dept.web.general.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NumberUtil {

	public static boolean isMoreZero(double num1) {

		if(num1>0){
			return true;
		}else{
			return false;
		}
		
	}
	
	
	public static int aMoreb(Double num1,Double num2) {
		
	    double a = Double.valueOf(num1);
	    double b = Double.valueOf(num2);
		
		if((a-b)>0){
			return 1;
		}else if((a-b)==0){
			return 0;
		}else{
			return -1;
		}
	}
	public static double format(double d,String format){
		DecimalFormat df = new DecimalFormat(format); 
		String ds=df.format(d);
		return Double.parseDouble(ds);
	}
	
	public static double format2(double d){
		DecimalFormat df = new DecimalFormat("0.00"); 
		String ds=df.format(d);
		return Double.parseDouble(ds);
	}
	
	public static String format2Str(double d){
		 DecimalFormat df = new DecimalFormat("#0.##");
		String ds=df.format(d);
		return ds;
	}
	/**
	 * 四舍五入，保留两位
	 * @param d
	 * @return
	 */
	public static String format3Str(String d){
		 DecimalFormat df = new DecimalFormat("#0.##");
		String ds=df.format(d);
		return ds;
	}
	
	/**
	 * 截取， 保留两位
	 * @param d
	 * @return
	 */
	public static String format2JQStr(String d){
		 DecimalFormat formater = new DecimalFormat();
		 formater.setMaximumFractionDigits(2);
		 formater.setGroupingSize(0);
		 formater.setRoundingMode(RoundingMode.FLOOR);
		String value= formater.format(Double.valueOf(d));
		if(value.indexOf(".")<=0){
			value=value+".00";
		}
		return value;
	}
	
	/**
	 * 截取， 保留两位
	 * @param d
	 * @return
	 */
	public static String format2JQStr(Double d){
		 DecimalFormat formater = new DecimalFormat();
		 formater.setMaximumFractionDigits(2);
		 formater.setGroupingSize(0);
		 formater.setRoundingMode(RoundingMode.FLOOR);
		 String value= formater.format(Double.valueOf(d));
			if(value.indexOf(".")<=0){
				value=value+".00";
			}
			return value;
	}
	
	
	
	public static double format4(double d){
		DecimalFormat df = new DecimalFormat("0.0000"); 
		String ds=df.format(d);
		return Double.parseDouble(ds);
	}
	
	public static double format6(double d){
		DecimalFormat df = new DecimalFormat("0.000000"); 
		String ds=df.format(d);
		return Double.parseDouble(ds);
	}
	
	
	public static String formatToString(String value){
	    String moeny = "";
	    moeny = format3Str(value);
	    if(moeny.contains("-")){
	    	moeny=moeny.replace("-", "");
	    }
	    return moeny;
	}
	
	public static String formatToString(double value){
	    String moeny = "";
	    moeny = format2Str(value);
	    if(moeny.contains("-")){
	    	moeny=moeny.replace("-", "");
	    }
	    return moeny;
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
	public static long getLong(String str){
		if(str==null||str.equals(""))
			return 0L;
		long ret=0;
		try {
			ret=Long.parseLong(str);
		} catch (NumberFormatException e) {
			ret=0;
		}
		return ret;
	}
	
	public static Long[] getLongs(String[] str){
		
		if(str==null||str.length<1)
			return new Long[]{0L};
		Long[] ret=new Long[str.length];
		for(int i=0;i<str.length;i++){
			ret[i]=getLong(str[i]);
		}
		return ret;
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
	
	public static int compare(double x,double y){
		BigDecimal val1=new BigDecimal(x);
		BigDecimal val2=new BigDecimal(y);
		return val1.compareTo(val2);
	}
	
	/**
	 * @param d
	 * @param len
	 * @return
	 */
	public static double ceil(double d,int len){
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
		return Double.parseDouble(str);
	}
	
	public static double ceil(double d){
		return ceil(d,2);
	}
	
	/**
	 * 显示成百分比
	 * @Title: percent 
	 * @Description: TODO
	 * @param @param scales
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	public static String percent(String scales){
	    
	    String resc = String.valueOf(ceil(Double.valueOf(scales), 2)*100);
	    
	    resc = resc.substring(0,resc.indexOf(".")) + "%";
	    
	    return resc;
	    
	}
	
	
	/**
	 * 显示成百分比不加％号
	 * @Title: percent 
	 * @Description: TODO
	 * @param @param scales
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	public static String percent2(String scales){
	    
	    String resc = String.valueOf(ceil(Double.valueOf(scales), 2)*100);
	    
	    resc = resc.substring(0,resc.indexOf("."));
	    
	    return resc;
	    
	}
	
	/***
	 * 效验字符串是否是正整数,是返回true,不是返回false
	 * @param num
	 * @return
	 */
	public static boolean isNumber(String num){
		String eL = "^\\d*[1-9]\\d*$";//整数  
        Pattern p = Pattern.compile(eL);  
        Matcher m = p.matcher(num);  
        return m.matches(); 
	}
	
	public static String format1SD(String dw){
		if(!StringUtils.isEmpty(dw)){
			Double 	d = Double.valueOf(dw);
			if(d>=10000){
				d =	d/10000;
			}
			DecimalFormat df = new DecimalFormat("0"); 
			String ds=df.format(d);
			return ds+"万";
		}else{
			return "";
		}
		
	}
	
	
	
	public static String format1SD2(Double dw){
		if(dw!=null){
			Double 	d = Double.valueOf(dw);
			if(d>=10000){
				d =	d/10000;
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
				return str+"万";
			}else{
				return d+"元";
			}
			
		}else{
			return null;
		}
		
	}
	
	
	public static void main(String[] args) {
	    
		String aaa="123456.01234";
		 System.out.println(format2JQStr(aaa));
	    
	    
	}
	
	public static String formatOneStr(double d){
//		if(d==0){
//			return "";
//		}
		int length = 1; //保留位数
		DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(1);
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
	
	
	public static String formatStr(double d){
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
	
	   
	/**      
	 * @desc 用途描述: 切割标年化收益
	 * @param apr
	 * @return 返回说明:
	 * @exception 内部异常说明:
	 * @throws 抛出异常说明:
	 * @author gwx
	 * @version 1.0      
	 * @created 2016-2-4 上午10:56:11 
	 * @mod 修改描述:
	 * @modAuthor 修改人:
	    
	 */
	public String subStrApr(Double apr){
		String aprStr=String.valueOf(apr);
		aprStr=aprStr.substring(0,aprStr.indexOf("."));
		return aprStr;
	}
	
	   
	/**      
	 * @desc 用途描述: 切割标年化收益
	 * @param apr
	 * @return 返回说明:
	 * @exception 内部异常说明:
	 * @throws 抛出异常说明:
	 * @author gwx
	 * @version 1.0      
	 * @created 2016-2-4 上午10:56:38 
	 * @mod 修改描述:
	 * @modAuthor 修改人:
	    
	 */
	public String subEndApr(Double apr){
		String aprEnd=String.valueOf(apr);
		aprEnd=aprEnd.substring(aprEnd.indexOf("."),aprEnd.length());
		int len=3-aprEnd.length();
		if(len>0){
			return aprEnd+String.format("%1$0"+len+"d", 0);
		}else{
			return aprEnd;
		}
	}
	
	   
	/**      
	 * @desc 用途描述: BigDecimal转换为String
	 * @param d
	 * @return 返回说明:
	 * @exception 内部异常说明:
	 * @throws 抛出异常说明:
	 * @author gwx
	 * @version 1.0      
	 * @created 2016-2-4 下午3:41:02 
	 * @mod 修改描述:
	 * @modAuthor 修改人:
	    
	 */
	public String decToStr8(Double d){
		BigDecimal big= new BigDecimal(d);
		DecimalFormat def= new DecimalFormat("#.########");
    	return def.format(big).toString();
	}
	public String decToStr4(Double d){
		BigDecimal big= new BigDecimal(d);
		DecimalFormat def= new DecimalFormat("#.####");
    	return def.format(big).toString();
	}
}
