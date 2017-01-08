package com.dept.web.general.interest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.dept.web.general.util.NumberUtil;
import com.dept.web.general.util.Utils;

/**
 * 
 * @ClassName:     MonthEqualCalculator.java
 * @Description:   等额还息计算工具类
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-10-16 下午1:44:50
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class MonthEqualCalculator implements InterestCalculator{
	private double account;
	private double apr;
	private int period;
	private double moneyPerMonth;
	private String eachString;
	private List monthList;
	
	public MonthEqualCalculator() {
		this(0.0,0.0,0);
	}
	
	public MonthEqualCalculator(double account,double apr, int period) {
		super();
		this.account = account;
		this.apr = apr;
		this.period = period;
		monthList=new ArrayList();
	}

	@Override
	public double getTotalAccount() {
		return moneyPerMonth*period;
	}

	@Override
	public List each(){
		//计算平均每月还款
		moneyPerMonth=Utils.Mrpi(account, apr, period);
		
		//总共需要还款金额
		double totalRemain= BigDecimal.valueOf(moneyPerMonth).multiply(BigDecimal.valueOf(period)).setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();
		//浮点型格式化
		moneyPerMonth=NumberUtil.format2(moneyPerMonth);
		//每期还款后剩余金额
		double remain=account;
		//每期需要还款中的本金
		double accountPerMon=0.0;
		//每期需要还款中的利息
		double interest=0.0;
		
		//累计还款本金
		double remainCapital = 0.0;
		//用于控制台输出的字符串
		StringBuffer sb=new StringBuffer("");
		sb.append("Total Money:"+totalRemain);
		sb.append("\n");
		sb.append("Month Apr:"+apr);
		sb.append("\n");
		sb.append("Month Money:"+moneyPerMonth);
		sb.append("\n");
		
		//循环计算accountPerMon、interest、totalRemain
		for(int i=0;i<period;i++){	
			if(period - i>1){
				//计算每月需要支付的利息
				interest=NumberUtil.format2(remain*apr/12);
				
				//用于计算利息的剩余金额
				remain=NumberUtil.format6(remain+interest-moneyPerMonth);
				
				//计算每月需要还款中的本金
				accountPerMon=NumberUtil.format2(moneyPerMonth-interest);
				remainCapital = BigDecimal.valueOf(remainCapital).add(BigDecimal.valueOf(accountPerMon)).doubleValue();
				//实际支付的金额扣除本月已经支付的金额
				totalRemain=BigDecimal.valueOf(totalRemain).subtract(BigDecimal.valueOf(moneyPerMonth)).doubleValue();
			}else{
//				accountPerMon = remain
				//计算每月需要支付的利息
//				interest=NumberUtil.format6(remain*apr/12);
				
				//用于计算利息的剩余金额
				//remain=NumberUtil.format6(remain+interest-moneyPerMonth);
				
				//计算每月需要还款中的本金
//				accountPerMon=NumberUtil.format6(moneyPerMonth-interest);
				accountPerMon = BigDecimal.valueOf(account).subtract(BigDecimal.valueOf(remainCapital)).doubleValue();
				
				interest = BigDecimal.valueOf(totalRemain).subtract(BigDecimal.valueOf(accountPerMon)).doubleValue(); 
				moneyPerMonth = totalRemain;
				//实际支付的金额扣除本月已经支付的金额
				totalRemain=NumberUtil.format6(totalRemain-moneyPerMonth);
				
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

	@Override
	public String eachDay() {
		//总共需要还款金额
		double totalRemain=account*(apr/12)*period+account;
		StringBuffer sb=new StringBuffer("");
		sb.append("Total Money:"+totalRemain);
		sb.append("\n");
		sb.append("Month Apr:"+apr);
		sb.append("\n");
		return "借款额度为："+account+"  到期需偿还"+totalRemain;
	}
	
	
	public static void main(String[] args) {
		MonthEqualCalculator e=new MonthEqualCalculator(3000,0.12,6);
		e.each();
		System.out.println(e);
	}
	
	
}
