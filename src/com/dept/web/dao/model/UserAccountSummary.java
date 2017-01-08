package com.dept.web.dao.model;



/**
 * 
 * @ClassName:     UserAccountSummary.java
 * @Description:   用户资产
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-8-16 上午9:55:57
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class UserAccountSummary{

	private long userId;
	
	/** 账户总额 */
	private double accountTotal;
	
	/** 可用总额 */
	private double accountUseMoney;
	
	/** 冻结金额 */
	private double accountNoUseMoney;
	
	/** 红包 */
	private double accountHongbao;
	
	/** 净资产 */
	private double accountOwnMoney;
	
	/** 借款总额 */
	private double accountBorrowTotal;
	
	/** 借款利息总额 */
	private Double accountBorrowInterestTotal;
	
	/** 待收总额 */
	private double accountCollectTotal;
	
	/** 待收利息 */
	private double accountCollectInterest;
	
	/** 投资总额 */
	private double accountInvestTotal;
	
	/** 投资利息总额 */
	private double accountInvestInterestTotal;
	
	/** 充值总额 */
	private double accountRechargeTotal;

	/** 提现成功总额 */
	private double accountCashTotal;
	
	/** 已还总额 */
	private double accountRepaidTotal;
	
	/** 未还总额 */
	private double accountNotRepayTotal;
	
	/** 充值手续费总额 */
	private double rechargeFeeTotal;
	
	/**
	 * 投标中的待收金额
	 */
	private double tenderCollectionMoney;
	
	private String lastTime;
	
	/**
	 * 奖励总额
	 */
	private double awardTotal;
	
	private int usernums;
	private double tenderMoneyPast;//我的慈善
	private double tenderInterestPast;//累计收益
	
	private double withdrawNoUseMoney;//提现冻结金额
	
	private Double accountinteresttotal;  //累计客户收益
	private int zrs;
	private Double aprs;
	
	
	

	

	

	public Double getAprs() {
		return aprs;
	}

	public void setAprs(Double aprs) {
		this.aprs = aprs;
	}

	public int getZrs() {
		return zrs;
	}

	public void setZrs(int zrs) {
		this.zrs = zrs;
	}

	public Double getAccountinteresttotal() {
		return accountinteresttotal;
	}

	public void setAccountinteresttotal(Double accountinteresttotal) {
		this.accountinteresttotal = accountinteresttotal;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public double getAccountTotal() {
		return accountTotal;
	}

	public void setAccountTotal(double accountTotal) {
		this.accountTotal = accountTotal;
	}

	public double getAccountUseMoney() {
		return accountUseMoney;
	}

	public void setAccountUseMoney(double accountUseMoney) {
		this.accountUseMoney = accountUseMoney;
	}

	public double getAccountNoUseMoney() {
		return accountNoUseMoney;
	}

	public void setAccountNoUseMoney(double accountNoUseMoney) {
		this.accountNoUseMoney = accountNoUseMoney;
	}

	public double getAccountOwnMoney() {
		return accountOwnMoney;
	}

	public void setAccountOwnMoney(double accountOwnMoney) {
		this.accountOwnMoney = accountOwnMoney;
	}

	public double getAccountCollectTotal() {
		return accountCollectTotal;
	}

	public void setAccountCollectTotal(double accountCollectTotal) {
		this.accountCollectTotal = accountCollectTotal;
	}

	public double getAccountCollectInterest() {
		return accountCollectInterest;
	}

	public void setAccountCollectInterest(double accountCollectInterest) {
		this.accountCollectInterest = accountCollectInterest;
	}

	public double getAccountInvestTotal() {
		return accountInvestTotal;
	}

	public void setAccountInvestTotal(double accountInvestTotal) {
		this.accountInvestTotal = accountInvestTotal;
	}

	public double getAccountRechargeTotal() {
		return accountRechargeTotal;
	}

	public void setAccountRechargeTotal(double accountRechargeTotal) {
		this.accountRechargeTotal = accountRechargeTotal;
	}

	public double getAccountCashTotal() {
		return accountCashTotal;
	}

	public void setAccountCashTotal(double accountCashTotal) {
		this.accountCashTotal = accountCashTotal;
	}

	public double getAccountRepaidTotal() {
		return accountRepaidTotal;
	}

	public void setAccountRepaidTotal(double accountRepaidTotal) {
		this.accountRepaidTotal = accountRepaidTotal;
	}

	public double getAccountNotRepayTotal() {
		return accountNotRepayTotal;
	}

	public void setAccountNotRepayTotal(double accountNotRepayTotal) {
		this.accountNotRepayTotal = accountNotRepayTotal;
	}

	public double getAccountHongbao() {
		return accountHongbao;
	}

	public void setAccountHongbao(double accountHongbao) {
		this.accountHongbao = accountHongbao;
	}

	public double getAccountBorrowTotal() {
		return accountBorrowTotal;
	}

	public void setAccountBorrowTotal(double accountBorrowTotal) {
		this.accountBorrowTotal = accountBorrowTotal;
	}

	

	public Double getAccountBorrowInterestTotal() {
		return accountBorrowInterestTotal;
	}

	public void setAccountBorrowInterestTotal(Double accountBorrowInterestTotal) {
		this.accountBorrowInterestTotal = accountBorrowInterestTotal;
	}

	public double getAccountInvestInterestTotal() {
		return accountInvestInterestTotal;
	}

	public void setAccountInvestInterestTotal(double accountInvestInterestTotal) {
		this.accountInvestInterestTotal = accountInvestInterestTotal;
	}

	public double getRechargeFeeTotal() {
		return rechargeFeeTotal;
	}

	public void setRechargeFeeTotal(double rechargeFeeTotal) {
		this.rechargeFeeTotal = rechargeFeeTotal;
	}

	public int getUsernums() {
		return usernums;
	}

	public void setUsernums(int usernums) {
		this.usernums = usernums;
	}

	public double getTenderCollectionMoney() {
		return tenderCollectionMoney;
	}

	public void setTenderCollectionMoney(double tenderCollectionMoney) {
		this.tenderCollectionMoney = tenderCollectionMoney;
	}

	public double getAwardTotal() {
		return awardTotal;
	}

	public void setAwardTotal(double awardTotal) {
		this.awardTotal = awardTotal;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public double getTenderMoneyPast() {
		return tenderMoneyPast;
	}

	public void setTenderMoneyPast(double tenderMoneyPast) {
		this.tenderMoneyPast = tenderMoneyPast;
	}

	public double getTenderInterestPast() {
		return tenderInterestPast;
	}

	public void setTenderInterestPast(double tenderInterestPast) {
		this.tenderInterestPast = tenderInterestPast;
	}

	public double getWithdrawNoUseMoney() {
		return withdrawNoUseMoney;
	}

	public void setWithdrawNoUseMoney(double withdrawNoUseMoney) {
		this.withdrawNoUseMoney = withdrawNoUseMoney;
	}

	
	
}