package com.dept.web;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
//		BigDecimal repayMoney1 = new BigDecimal(3400.2200).setScale(2, BigDecimal.ROUND_DOWN);
//		BigDecimal repayMoney2 = BigDecimal.valueOf(3400.2200).setScale(2, BigDecimal.ROUND_DOWN);
//		
//		System.out.println(repayMoney1);
//		System.out.println(repayMoney2);
		
		String endTime="2020-06-30 23:23:59";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date endDate=sdf.parse(endTime);
		System.out.println(endDate.after(new Date()));
		
	}

}
