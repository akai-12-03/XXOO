package com.dept.web.dao.model;

import java.io.Serializable;


public class UserBorrowModel extends Borrow implements Serializable{

	private static final long serialVersionUID = 6956108421611997455L;
	private int isqiye;
	private long fastid;
	private String username;
	private String user_area;
	private String kefu_username;
	private String qq;
	private int credit_jifen;
	private String credit_pic;
	private String add_area;
	private double Surplus;
	private String borrow_name;
	private String borrow_style;
	private String mobile;
	private String area;
	private String scales;
	private String use;
	 /**
     * 是否天标
     */
    private Integer isDay;
    
    private Double dhmoney;
    private Long dhstime;  //开始时间
    private Long dhetime; //结束时间
    
    private String endTime;
    
    
    
    
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Double getDhmoney() {
		return dhmoney;
	}
	public void setDhmoney(Double dhmoney) {
		this.dhmoney = dhmoney;
	}
	public Long getDhstime() {
		return dhstime;
	}
	public void setDhstime(Long dhstime) {
		this.dhstime = dhstime;
	}
	public Long getDhetime() {
		return dhetime;
	}
	public void setDhetime(Long dhetime) {
		this.dhetime = dhetime;
	}
	public Integer getIsDay() {
		return isDay;
	}
	public void setIsDay(Integer isDay) {
		this.isDay = isDay;
	}
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public String getScales() {
		return scales;
	}
	public void setScales(String scales) {
		this.scales = scales;
	}
	public String getBorrow_name() {
		return borrow_name;
	}
	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}
	public String getBorrow_style() {
		return borrow_style;
	}
	public void setBorrow_style(String borrow_style) {
		this.borrow_style = borrow_style;
	}
	

	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setArea(String area) {
		this.area = area;
	}


    /**
     * borrow_repayment.webstatus
     * 网站代还
     */
    private int webstatus;

    /**
     * borrow_repayment.order
     * 
     */
    private int bo_order;

    /**
     * borrow_repayment.borrow_id
     * 借款id
     */
    private long borrowId;

    /**
     * borrow_repayment.repayment_time
     * 估计还款时间
     */
    private String repaymentTime;

    /**
     * borrow_repayment.repayment_yestime
     * 已经还款时间
     */
    private String repaymentYestime;

    /**
     * borrow_repayment.repayment_account
     * 预还金额
     */
    private Double repaymentAccount;

    /**
     * borrow_repayment.repayment_yesaccount
     * 实还金额
     */
    private String repaymentYesaccount;

    /**
     * borrow_repayment.late_days
     * 
     */
    private int lateDays;

    /**
     * borrow_repayment.late_interest
     * 
     */
    private String lateInterest;

    /**
     * borrow_repayment.interest
     * 
     */
    private String interest;

    /**
     * borrow_repayment.capital
     * 
     */
    private String capital;

    /**
     * borrow_repayment.forfeit
     * 滞纳金
     */
    private String forfeit;

    /**
     * borrow_repayment.reminder_fee
     * 崔收费
     */
    private String reminderFee;

    /**
     * borrow_repayment.addtime
     * 
     */
    private Long addtime;

    /**
     * borrow_repayment.addip
     * 
     */
    private String addip;

    /**
     * borrow_repayment.bonus
     * 
     */
    private Double bonus;
    
    /**  新增 */
    private double notRepayTotal;
    
    private double repaidTotal;
	
	public double getSurplus() {
		return Surplus;
	}
	public void setSurplus(double surplus) {
		Surplus = surplus;
	}
	private String usetypename;
	private String realname;
	
	
	public int getIsqiye() {
		return isqiye;
	}
	public void setIsqiye(int isqiye) {
		this.isqiye = isqiye;
	}
	public long getFastid() {
		return fastid;
	}
	public void setFastid(long fastid) {
		this.fastid = fastid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getArea() {
		return user_area;
	}
	public void setUser_area(String user_area) {
		this.user_area = user_area;
	}
	public String getKefu_username() {
		return kefu_username;
	}
	public void setKefu_username(String kefu_username) {
		this.kefu_username = kefu_username;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public int getCredit_jifen() {
		return credit_jifen;
	}
	public void setCredit_jifen(int credit_jifen) {
		this.credit_jifen = credit_jifen;
	}
	public String getCredit_pic() {
		return credit_pic;
	}
	public void setCredit_pic(String credit_pic) {
		this.credit_pic = credit_pic;
	}
	public String getAdd_area() {
		return add_area;
	}
	public void setAdd_area(String add_area) {
		this.add_area = add_area;
	}
	public String getUsetypename() {
		return usetypename;
	}
	public void setUsetypename(String usetypename) {
		this.usetypename = usetypename;
	}
	public String getUser_area() {
		return user_area;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public int getWebstatus() {
		return webstatus;
	}
	public void setWebstatus(int webstatus) {
		this.webstatus = webstatus;
	}
	
	public int getBo_order() {
		return bo_order;
	}
	public void setBo_order(int bo_order) {
		this.bo_order = bo_order;
	}
	public long getBorrowId() {
		return borrowId;
	}
	public void setBorrowId(long borrowId) {
		this.borrowId = borrowId;
	}
	public String getRepaymentTime() {
		return repaymentTime;
	}
	public void setRepaymentTime(String repaymentTime) {
		this.repaymentTime = repaymentTime;
	}
	public String getRepaymentYestime() {
		return repaymentYestime;
	}
	public void setRepaymentYestime(String repaymentYestime) {
		this.repaymentYestime = repaymentYestime;
	}
	public Double getRepaymentAccount() {
		return repaymentAccount;
	}
	public void setRepaymentAccount(Double repaymentAccount) {
		this.repaymentAccount = repaymentAccount;
	}
	public String getRepaymentYesaccount() {
		return repaymentYesaccount;
	}
	public void setRepaymentYesaccount(String repaymentYesaccount) {
		this.repaymentYesaccount = repaymentYesaccount;
	}
	public int getLateDays() {
		return lateDays;
	}
	public void setLateDays(int lateDays) {
		this.lateDays = lateDays;
	}
	public String getLateInterest() {
		return lateInterest;
	}
	public void setLateInterest(String lateInterest) {
		this.lateInterest = lateInterest;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getCapital() {
		return capital;
	}
	public void setCapital(String capital) {
		this.capital = capital;
	}
	public String getForfeit() {
		return forfeit;
	}
	public void setForfeit(String forfeit) {
		this.forfeit = forfeit;
	}
	public String getReminderFee() {
		return reminderFee;
	}
	public void setReminderFee(String reminderFee) {
		this.reminderFee = reminderFee;
	}
	public Long getAddtime() {
		return addtime;
	}
	public void setAddtime(Long addtime) {
		this.addtime = addtime;
	}
	public String getAddip() {
		return addip;
	}
	public void setAddip(String addip) {
		this.addip = addip;
	}
	public Double getBonus() {
		return bonus;
	}
	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}
	public double getNotRepayTotal() {
		return notRepayTotal;
	}
	public void setNotRepayTotal(double notRepayTotal) {
		this.notRepayTotal = notRepayTotal;
	}
	public double getRepaidTotal() {
		return repaidTotal;
	}
	public void setRepaidTotal(double repaidTotal) {
		this.repaidTotal = repaidTotal;
	}
	
	

}
