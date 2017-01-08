package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 投标
 * 
 * @ClassName:     BorrowTender
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年5月16日 下午3:43:52 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class BorrowTender extends BaseEntity{
    
    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = 5956259325721463375L;

    private Long userId;
    
    private Integer status;
    
    private Long borrowId;
    
    private Double money;
    
    private Double account;
    
    private Double repaymentAccount;
    
    private Double interest;
    
    private Double repaymentYesaccount;
    
    private Double waitAccount;
    
    private Double waitInterest;
    
    private Double repaymentYesinterest;
    
    private Long addtime;
    
    private String addip;
    
    
    private String updateip;
    
    private Long updatetime;
    
    
    /**新增*/
    private Integer borrowType;
    
    private Double borrowApr;
    
    
    private Integer borrowLimit;
    
    private Long borrowNo;
    
    private Double borrowScales;
    
    
    private Integer borrowStatus;
    
    private int trustStatus;
    /** 新增 */
    private double collectTotal;
    
    private double collectInterest;
    
    private double investTotal;
    
    private double investInterest;
    
    private String username;
    
    
    private Double inter;
    private String borrowName;
    private double borrowAccount;
    private Integer timeLimit;
    private Integer isDay;
    private Integer timeLimitDay;
    private Double apr;
    private Integer credit;
    
    private Integer isTransfer;  //是否转让0不1是
    private String tender_time;
    private String   anum;
    private String    borrow_name;
    private String time_limit;
    private String isday;
//    private String time_limit_day;
    private String tender_account;
    private String tender_money;
    private String  borrow_userid;
    private String   op_username;
    private long  borrow_id;
    private String  borrow_account ;
    private String  borrow_account_yes;
    private String verify_time;
    private String  credit_jifen;
    private String credit_pic ;
    private String scales;
    
    private String realname;
    private String cardId;
    private String borrow_api_LoanNo;//钱多多返回的投标流水号
    private Double accountPast;//我的慈善
    private Double interestPast;//累计收益
    
    private String trustOrderNo;
    
    
    private String trustTrxId;
    private String trustFreezeOrdId;
    private String trustIsFreeze;
    private String trustFreezeTrxId;
    private Long  hongbao_id;
    
    private Double hongbao_money;
    
    private String uname;
    
    private String LoanNo;//钱多多流水号
    
    private long apiLogId;
    
    private Integer transfer;//转让标记
    
    private Integer remainingDays;//剩余天数
    
    private String repayOrder;//剩余期数
    
    private String mortgagor;//垫资标借款人
    public String getLoanNo() {
		return LoanNo;
	}

	public void setLoanNo(String loanNo) {
		LoanNo = loanNo;
	}

	public long getApiLogId() {
		return apiLogId;
	}

	public void setApiLogId(long apiLogId) {
		this.apiLogId = apiLogId;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public int getTrustStatus() {
		return trustStatus;
	}

	public void setTrustStatus(int trustStatus) {
		this.trustStatus = trustStatus;
	}

	public double getCollectTotal() {
		return collectTotal;
	}

	public void setCollectTotal(double collectTotal) {
		this.collectTotal = collectTotal;
	}

	public double getCollectInterest() {
		return collectInterest;
	}

	public void setCollectInterest(double collectInterest) {
		this.collectInterest = collectInterest;
	}

	public double getInvestTotal() {
		return investTotal;
	}

	public void setInvestTotal(double investTotal) {
		this.investTotal = investTotal;
	}

	public double getInvestInterest() {
		return investInterest;
	}

	public void setInvestInterest(double investInterest) {
		this.investInterest = investInterest;
	}

	public Double getInter() {
		return inter;
	}

	public void setInter(Double inter) {
		this.inter = inter;
	}

	public double getBorrowAccount() {
		return borrowAccount;
	}

	public void setBorrowAccount(double borrowAccount) {
		this.borrowAccount = borrowAccount;
	}

	public Integer getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}

	public Integer getTimeLimitDay() {
		return timeLimitDay;
	}

	public void setTimeLimitDay(Integer timeLimitDay) {
		this.timeLimitDay = timeLimitDay;
	}

	public Double getApr() {
		return apr;
	}

	public void setApr(Double apr) {
		this.apr = apr;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public Integer getIsTransfer() {
		return isTransfer;
	}

	public void setIsTransfer(Integer isTransfer) {
		this.isTransfer = isTransfer;
	}

	public String getTender_time() {
		return tender_time;
	}

	public void setTender_time(String tender_time) {
		this.tender_time = tender_time;
	}

	public String getAnum() {
		return anum;
	}

	public void setAnum(String anum) {
		this.anum = anum;
	}

	public String getBorrow_name() {
		return borrow_name;
	}

	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}

	public String getTime_limit() {
		return time_limit;
	}

	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}

	public String getIsday() {
		return isday;
	}

	public void setIsday(String isday) {
		this.isday = isday;
	}

	public String getTender_account() {
		return tender_account;
	}

	public void setTender_account(String tender_account) {
		this.tender_account = tender_account;
	}

	public String getTender_money() {
		return tender_money;
	}

	public void setTender_money(String tender_money) {
		this.tender_money = tender_money;
	}

	public String getBorrow_userid() {
		return borrow_userid;
	}

	public void setBorrow_userid(String borrow_userid) {
		this.borrow_userid = borrow_userid;
	}

	public String getOp_username() {
		return op_username;
	}

	public void setOp_username(String op_username) {
		this.op_username = op_username;
	}

	public long getBorrow_id() {
		return borrow_id;
	}

	public void setBorrow_id(long borrow_id) {
		this.borrow_id = borrow_id;
	}

	public String getBorrow_account() {
		return borrow_account;
	}

	public void setBorrow_account(String borrow_account) {
		this.borrow_account = borrow_account;
	}

	public String getBorrow_account_yes() {
		return borrow_account_yes;
	}

	public void setBorrow_account_yes(String borrow_account_yes) {
		this.borrow_account_yes = borrow_account_yes;
	}

	public String getVerify_time() {
		return verify_time;
	}

	public void setVerify_time(String verify_time) {
		this.verify_time = verify_time;
	}

	public String getCredit_jifen() {
		return credit_jifen;
	}

	public void setCredit_jifen(String credit_jifen) {
		this.credit_jifen = credit_jifen;
	}

	public String getCredit_pic() {
		return credit_pic;
	}

	public void setCredit_pic(String credit_pic) {
		this.credit_pic = credit_pic;
	}

	public String getScales() {
		return scales;
	}

	public void setScales(String scales) {
		this.scales = scales;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getBorrow_api_LoanNo() {
		return borrow_api_LoanNo;
	}

	public void setBorrow_api_LoanNo(String borrow_api_LoanNo) {
		this.borrow_api_LoanNo = borrow_api_LoanNo;
	}

	public Double getAccountPast() {
		return accountPast;
	}

	public void setAccountPast(Double accountPast) {
		this.accountPast = accountPast;
	}

	public Double getInterestPast() {
		return interestPast;
	}

	public void setInterestPast(Double interestPast) {
		this.interestPast = interestPast;
	}

	public String getTrustOrderNo() {
		return trustOrderNo;
	}

	public void setTrustOrderNo(String trustOrderNo) {
		this.trustOrderNo = trustOrderNo;
	}

	public String getTrustTrxId() {
		return trustTrxId;
	}

	public void setTrustTrxId(String trustTrxId) {
		this.trustTrxId = trustTrxId;
	}

	public String getTrustFreezeOrdId() {
		return trustFreezeOrdId;
	}

	public void setTrustFreezeOrdId(String trustFreezeOrdId) {
		this.trustFreezeOrdId = trustFreezeOrdId;
	}

	public String getTrustIsFreeze() {
		return trustIsFreeze;
	}

	public void setTrustIsFreeze(String trustIsFreeze) {
		this.trustIsFreeze = trustIsFreeze;
	}

	public String getTrustFreezeTrxId() {
		return trustFreezeTrxId;
	}

	public void setTrustFreezeTrxId(String trustFreezeTrxId) {
		this.trustFreezeTrxId = trustFreezeTrxId;
	}


	public Long getHongbao_id() {
		return hongbao_id;
	}

	public void setHongbao_id(Long hongbao_id) {
		this.hongbao_id = hongbao_id;
	}

	public Double getHongbao_money() {
		return hongbao_money;
	}

	public void setHongbao_money(Double hongbao_money) {
		this.hongbao_money = hongbao_money;
	}

	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }

    public Double getRepaymentAccount() {
        return repaymentAccount;
    }

    public void setRepaymentAccount(Double repaymentAccount) {
        this.repaymentAccount = repaymentAccount;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Double getRepaymentYesaccount() {
        return repaymentYesaccount;
    }

    public void setRepaymentYesaccount(Double repaymentYesaccount) {
        this.repaymentYesaccount = repaymentYesaccount;
    }

    public Double getWaitAccount() {
        return waitAccount;
    }

    public void setWaitAccount(Double waitAccount) {
        this.waitAccount = waitAccount;
    }

    public Double getWaitInterest() {
        return waitInterest;
    }

    public void setWaitInterest(Double waitInterest) {
        this.waitInterest = waitInterest;
    }

    public Double getRepaymentYesinterest() {
        return repaymentYesinterest;
    }

    public void setRepaymentYesinterest(Double repaymentYesinterest) {
        this.repaymentYesinterest = repaymentYesinterest;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUpdateip() {
        return updateip;
    }

    public void setUpdateip(String updateip) {
        this.updateip = updateip;
    }

    public Long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Long updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getBorrowType() {
        return borrowType;
    }

    public void setBorrowType(Integer borrowType) {
        this.borrowType = borrowType;
    }

    public Double getBorrowApr() {
        return borrowApr;
    }

    public void setBorrowApr(Double borrowApr) {
        this.borrowApr = borrowApr;
    }

    public Integer getIsDay() {
        return isDay;
    }

    public void setIsDay(Integer isDay) {
        this.isDay = isDay;
    }

    public Integer getBorrowLimit() {
        return borrowLimit;
    }

    public void setBorrowLimit(Integer borrowLimit) {
        this.borrowLimit = borrowLimit;
    }

    public Long getBorrowNo() {
        return borrowNo;
    }

    public void setBorrowNo(Long borrowNo) {
        this.borrowNo = borrowNo;
    }

    public Double getBorrowScales() {
        return borrowScales;
    }

    public void setBorrowScales(Double borrowScales) {
        this.borrowScales = borrowScales;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public Integer getBorrowStatus() {
        return borrowStatus;
    }

    public void setBorrowStatus(Integer borrowStatus) {
        this.borrowStatus = borrowStatus;
    }

	public Integer getTransfer() {
		return transfer;
	}

	public void setTransfer(Integer transfer) {
		this.transfer = transfer;
	}

	public Integer getRemainingDays() {
		return remainingDays;
	}

	public void setRemainingDays(Integer remainingDays) {
		this.remainingDays = remainingDays;
	}

	public String getMortgagor() {
		return mortgagor;
	}

	public void setMortgagor(String mortgagor) {
		this.mortgagor = mortgagor;
	}

	public String getRepayOrder() {
		return repayOrder;
	}

	public void setRepayOrder(String repayOrder) {
		this.repayOrder = repayOrder;
	}
	
	
	
    
}
