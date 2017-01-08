package com.dept.web.general.interest;

import java.util.ArrayList;
import java.util.List;

public class EndInterestCalculator implements InterestCalculator {

	private double account;
	private double apr;
	private int period;
	private double moneyPerMonth;
	private String eachString;
	private List monthList;
	
	
	public EndInterestCalculator() {
		this(0.0,0.0,0);
	}
	
	public EndInterestCalculator(double account,double apr, int day) {
		super();
		this.account = account;
		this.apr =apr*day/360;
		this.period = 1;
		monthList=new ArrayList();
	}
	
	public EndInterestCalculator(double account,double apr, int period,int type) {
		super();
		this.account = account;
		if(type==InterestCalculator.TYPE_DAY_END){
			this.apr = apr*period/360;
		}else{
			this.apr = apr*period/12;
		}
		this.period = 1;
		monthList=new ArrayList();
	}

	@Override
	public double getTotalAccount() {
		moneyPerMonth=account*(1+apr);
		return moneyPerMonth;
	}

	@Override
	public List each() {
		getTotalAccount();
		double interest=moneyPerMonth-account;
		
		StringBuffer sb=new StringBuffer("");
		sb.append("Total Money:"+account);
		sb.append("\n");
		sb.append("Month Apr:"+apr);
		sb.append("\n");
		sb.append("Month Money:"+moneyPerMonth);
		sb.append("\n");

		MonthInterest mi=new MonthInterest( account,  interest,0);
		monthList.add(mi);
		
		sb.append("每期还钱:"+moneyPerMonth+" 期还款本金："+mi.getAccountPerMon()
				+" 利息："+mi.getInterest()+"  余额:"+mi.getTotalRemain());
		sb.append("\n");
		eachString=sb.toString();
		
		return monthList;
	}

	public double getAccount() {
		return account;
	}

	public void setAccount(double account) {
		this.account = account;
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

	public String getEachString() {
		return eachString;
	}

	public void setEachString(String eachString) {
		this.eachString = eachString;
	}

	public List getMonthList() {
		return monthList;
	}

	public void setMonthList(List monthList) {
		this.monthList = monthList;
	}

	@Override
	public String eachDay() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
