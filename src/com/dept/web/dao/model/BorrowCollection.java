package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 待收计划
 * 
 * @ClassName:     BorrowCollection
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年5月16日 下午3:36:38 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class BorrowCollection extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = 1160787194850903853L;

    private Integer status;
    
    private Integer colOrder;
    
    private Long tenderId;
    
    private Long repayTime;
    
    private Long repayYestime;
    
    private Double repayAccount;
    
    private Double repayYesaccount;
    
    private Double interest;
    
    private Double capital;
    
    private Integer lateDays;
    
    private Double lateInterest;
    
    private Long addtime;
    
    private String addip;
    
    private String updateip;
    
    private Long updatetime;
    
    /** 新增*/
    private Integer borrowLimit;
    
    private Integer borrowIsDay;
    
    private Integer borrowRepayMentStyle;
    
    private String borrowName;
    
    private Long borrowNo;
    
    /** 新增 */
    private Double investTotal;
    
    private double investInterest;
    private String payTime;
    private String rePlayTime;;
    private String  borrow_name;
    private String  borrow_id;
    private String  time_limit;
    private String  style;
    private String borrow_style;
    private String  name;
    private String  loanNo;//还款转账钱多多返回的流水号
    
    private Double bonus;
    
    private Double dsmoney;
    private Long rtime;
    
    
    
    private Long t; //开始时间
    private Long tt; //结束时间
    private String mortgagor;
    

    public Double getInvestTotal() {
		return investTotal;
	}

	public void setInvestTotal(Double investTotal) {
		this.investTotal = investTotal;
	}

	public double getInvestInterest() {
		return investInterest;
	}

	public void setInvestInterest(double investInterest) {
		this.investInterest = investInterest;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getRePlayTime() {
		return rePlayTime;
	}

	public void setRePlayTime(String rePlayTime) {
		this.rePlayTime = rePlayTime;
	}

	public String getBorrow_name() {
		return borrow_name;
	}

	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}

	public String getBorrow_id() {
		return borrow_id;
	}

	public void setBorrow_id(String borrow_id) {
		this.borrow_id = borrow_id;
	}

	public String getTime_limit() {
		return time_limit;
	}

	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getBorrow_style() {
		return borrow_style;
	}

	public void setBorrow_style(String borrow_style) {
		this.borrow_style = borrow_style;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public Double getBonus() {
		return bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public Double getDsmoney() {
		return dsmoney;
	}

	public void setDsmoney(Double dsmoney) {
		this.dsmoney = dsmoney;
	}

	public Long getRtime() {
		return rtime;
	}

	public void setRtime(Long rtime) {
		this.rtime = rtime;
	}

	public Long getT() {
		return t;
	}

	public void setT(Long t) {
		this.t = t;
	}

	public Long getTt() {
		return tt;
	}

	public void setTt(Long tt) {
		this.tt = tt;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getColOrder() {
        return colOrder;
    }

    public void setColOrder(Integer colOrder) {
        this.colOrder = colOrder;
    }

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public Long getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(Long repayTime) {
        this.repayTime = repayTime;
    }

    public Long getRepayYestime() {
        return repayYestime;
    }

    public void setRepayYestime(Long repayYestime) {
        this.repayYestime = repayYestime;
    }

    public Double getRepayAccount() {
        return repayAccount;
    }

    public void setRepayAccount(Double repayAccount) {
        this.repayAccount = repayAccount;
    }

    public Double getRepayYesaccount() {
        return repayYesaccount;
    }

    public void setRepayYesaccount(Double repayYesaccount) {
        this.repayYesaccount = repayYesaccount;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Double getCapital() {
        return capital;
    }

    public void setCapital(Double capital) {
        this.capital = capital;
    }

    public Integer getLateDays() {
        return lateDays;
    }

    public void setLateDays(Integer lateDays) {
        this.lateDays = lateDays;
    }

    public Double getLateInterest() {
        return lateInterest;
    }

    public void setLateInterest(Double lateInterest) {
        this.lateInterest = lateInterest;
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

    public Integer getBorrowLimit() {
        return borrowLimit;
    }

    public void setBorrowLimit(Integer borrowLimit) {
        this.borrowLimit = borrowLimit;
    }

    public Integer getBorrowIsDay() {
        return borrowIsDay;
    }

    public void setBorrowIsDay(Integer borrowIsDay) {
        this.borrowIsDay = borrowIsDay;
    }

    public Integer getBorrowRepayMentStyle() {
        return borrowRepayMentStyle;
    }

    public void setBorrowRepayMentStyle(Integer borrowRepayMentStyle) {
        this.borrowRepayMentStyle = borrowRepayMentStyle;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public Long getBorrowNo() {
        return borrowNo;
    }

    public void setBorrowNo(Long borrowNo) {
        this.borrowNo = borrowNo;
    }

	public String getMortgagor() {
		return mortgagor;
	}

	public void setMortgagor(String mortgagor) {
		this.mortgagor = mortgagor;
	}
    
    

}
