package com.dept.web.general.interest;

import java.util.ArrayList;
import java.util.List;

import com.dept.web.general.util.NumberUtil;

/**
 * 
 * @ClassName:     MonthInterestCalculator.java
 * @Description:   等额还息计算工具类
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-10-16 下午1:45:10
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class MonthInterestCalculator implements InterestCalculator{
	private double account;
	private double apr;
	private int period;
	private double moneyPerMonth;
	private String eachString;
	private List monthList;
	
	public MonthInterestCalculator() {
		this(0.0,0.0,0);
	}
	
	public MonthInterestCalculator(double account,double apr, int period) {
		super();
		this.account = account;
		this.apr = apr/12;
		this.period = period;
		monthList=new ArrayList();
	}
	
	@Override
	public double getTotalAccount() {
		return account*apr*period+account;
	}

	@Override
	public List each(){
		//总共需要还款金额
		double totalRemain=account*apr*period+account;
		//每期还款后剩余金额
		double remain=totalRemain-account*apr;
		//每期需要还款中的本金
		double accountPerMon=0.0;
		//每期需要还款中的利息
		double interest=0.0;
		//用于控制台输出的字符串
		StringBuffer sb=new StringBuffer("");
		sb.append("Total Money:"+totalRemain);
		sb.append("\n");
		sb.append("Month Apr:"+apr);
		sb.append("\n");
		
		//循环计算accountPerMon、interest、totalRemain
		for(int i=0;i<period;i++){	
			//计算每月需要支付的利息
			interest=NumberUtil.format6(account*apr);
			moneyPerMonth=interest;
			//用于计算利息的剩余金额
			remain=NumberUtil.format6(totalRemain-moneyPerMonth);
			//计算每月需要还款中的本金
			accountPerMon=0;
			//实际支付的金额扣除本月已经支付的金额
			totalRemain=NumberUtil.format6(totalRemain-moneyPerMonth);
			if(i==period-1){
				accountPerMon=account;
				totalRemain=totalRemain-account;
			}
			
			MonthInterest mi=new MonthInterest( accountPerMon,  interest,totalRemain);
			monthList.add(mi);
			
			
			sb.append("每月还钱:"+moneyPerMonth+" 月还款本金："+mi.getAccountPerMon()
					+" 利息："+mi.getInterest()+"  余额:"+mi.getTotalRemain());
			sb.append("\n");
		}
		eachString=sb.toString();
		return monthList;
	}
	
	@Override
	public String toString() {
		return this.eachString;
	}

	public double getApr() {
		return apr;
	}

	public void setApr(double apr) {
		this.apr = apr;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public double getMoneyPerMonth() {
		return moneyPerMonth;
	}

	public void setMoneyPerMonth(double moneyPerMonth) {
		this.moneyPerMonth = moneyPerMonth;
	}

	public List getMonthList() {
		return monthList;
	}

	public void setMonthList(List monthList) {
		this.monthList = monthList;
	}

	public String eachDay() {
		//总共需要还款金额
		double totalRemain=account*(apr/12/30)*period+account;
		System.out.println(totalRemain);
		StringBuffer sb=new StringBuffer("");
		sb.append("Total Money:"+totalRemain);
		sb.append("\n");
		sb.append("Month Apr:"+apr);
		sb.append("\n");
		return "借款额度为："+account+"  到期需偿还"+totalRemain;
	}
	
	
}
