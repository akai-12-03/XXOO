package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 还款计划
 * 
 * @ClassName:     BorrowRepayment
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年5月16日 下午3:36:23 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class BorrowRepayment extends BaseEntity{
    
    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = 2906882328617000546L;

    private Integer status;
    
    private Integer webstatus;
    
    private Integer repOrder;
    
    private Long borrowId;
    
    private Long repaymentTime;
    
    private Long repaymentYestime;
    
    private Double repaymentAccount;
    
    private Double repaymentYesaccount;
    
    private Integer lateDays;
    
    private Double lateInterest;
    
    private Double interest;
    
    private Double capital;
    
    private Double forfeit;
    
    private Double reminderFee;
    
    private Long addtime;
    
    private String addip;
    
    private Long updatetime;
    
    private String updateip;
    
    private String borrowName;
    private int borrowIsday;
    private int borrowTimeLimit;
    private int borrowRepaymentStyle;
    
    /**  新增 */
    private double notRepayTotal;
    
    private double repaidTotal;
    private Long userId;
    private String name;
    private String   borrow_name;
    private String  verify_time;
    private String   isday;
    private String   time_limit_day;
    private String  is_mb;
    private String  is_fast;
    private String  is_jin;
    private String  is_xin;
    private String  is_flow;
    private String   borrow_style;
    private String   time_limit;
    private String   username;
    private String   user_id;
    private String   mobile;
    private String   area ;
    private String   scales;
    private Integer boOrder;
    private Integer rorder;
    
    

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBorrow_name() {
		return borrow_name;
	}

	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}

	public String getVerify_time() {
		return verify_time;
	}

	public void setVerify_time(String verify_time) {
		this.verify_time = verify_time;
	}

	public String getIsday() {
		return isday;
	}

	public void setIsday(String isday) {
		this.isday = isday;
	}

	public String getTime_limit_day() {
		return time_limit_day;
	}

	public void setTime_limit_day(String time_limit_day) {
		this.time_limit_day = time_limit_day;
	}

	public String getIs_mb() {
		return is_mb;
	}

	public void setIs_mb(String is_mb) {
		this.is_mb = is_mb;
	}

	public String getIs_fast() {
		return is_fast;
	}

	public void setIs_fast(String is_fast) {
		this.is_fast = is_fast;
	}

	public String getIs_jin() {
		return is_jin;
	}

	public void setIs_jin(String is_jin) {
		this.is_jin = is_jin;
	}

	public String getIs_xin() {
		return is_xin;
	}

	public void setIs_xin(String is_xin) {
		this.is_xin = is_xin;
	}

	public String getIs_flow() {
		return is_flow;
	}

	public void setIs_flow(String is_flow) {
		this.is_flow = is_flow;
	}

	public String getBorrow_style() {
		return borrow_style;
	}

	public void setBorrow_style(String borrow_style) {
		this.borrow_style = borrow_style;
	}

	public String getTime_limit() {
		return time_limit;
	}

	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getScales() {
		return scales;
	}

	public void setScales(String scales) {
		this.scales = scales;
	}

	public Integer getBoOrder() {
		return boOrder;
	}

	public void setBoOrder(Integer boOrder) {
		this.boOrder = boOrder;
	}

	public Integer getRorder() {
		return rorder;
	}

	public void setRorder(Integer rorder) {
		this.rorder = rorder;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getWebstatus() {
        return webstatus;
    }

    public void setWebstatus(Integer webstatus) {
        this.webstatus = webstatus;
    }

    public Integer getRepOrder() {
        return repOrder;
    }

    public void setRepOrder(Integer repOrder) {
        this.repOrder = repOrder;
    }

    public Long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
    }

    public Long getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(Long repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public Long getRepaymentYestime() {
        return repaymentYestime;
    }

    public void setRepaymentYestime(Long repaymentYestime) {
        this.repaymentYestime = repaymentYestime;
    }

    public Double getRepaymentAccount() {
        return repaymentAccount;
    }

    public void setRepaymentAccount(Double repaymentAccount) {
        this.repaymentAccount = repaymentAccount;
    }

    public Double getRepaymentYesaccount() {
        return repaymentYesaccount;
    }

    public void setRepaymentYesaccount(Double repaymentYesaccount) {
        this.repaymentYesaccount = repaymentYesaccount;
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

    public Double getForfeit() {
        return forfeit;
    }

    public void setForfeit(Double forfeit) {
        this.forfeit = forfeit;
    }

    public Double getReminderFee() {
        return reminderFee;
    }

    public void setReminderFee(Double reminderFee) {
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

    public Long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Long updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdateip() {
        return updateip;
    }

    public void setUpdateip(String updateip) {
        this.updateip = updateip;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public int getBorrowIsday() {
        return borrowIsday;
    }

    public void setBorrowIsday(int borrowIsday) {
        this.borrowIsday = borrowIsday;
    }

    public int getBorrowTimeLimit() {
        return borrowTimeLimit;
    }

    public void setBorrowTimeLimit(int borrowTimeLimit) {
        this.borrowTimeLimit = borrowTimeLimit;
    }

    public int getBorrowRepaymentStyle() {
        return borrowRepaymentStyle;
    }

    public void setBorrowRepaymentStyle(int borrowRepaymentStyle) {
        this.borrowRepaymentStyle = borrowRepaymentStyle;
    }

    
}
