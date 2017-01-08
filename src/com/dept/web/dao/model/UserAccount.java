package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 用户资金账户
 * 
 * @ClassName:     UserAccount
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午10:46:49 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class UserAccount extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -7893761745119818700L;

    /**
     * 用户id
     */
    private Long userId;
    
    /**
     * 总资产
     */
    private Double moneyTotal;
    
    /**
     * 可用余额
     */
    private Double moneyUsable;
    
    /**
     * 提现中金额(冻结)
     */
    private Double moneyWithdraw;
    
    /**
     * 配资保证金
     */
    private Double moneyInsure;
    
    /**
     * 累计收益(所有历史收益)
     */
    private Double profitsTotal;
    
    private Double moneyTenderFreeze;
    
    private Double moneyCollection;
    
    private Long createdAt;
    
    private Long updatedAt;
    
    private Double ttdj;
    

    public Double getTtdj() {
		return ttdj;
	}

	public void setTtdj(Double ttdj) {
		this.ttdj = ttdj;
	}

	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getMoneyTotal() {
        return moneyTotal;
    }

    public void setMoneyTotal(Double moneyTotal) {
        this.moneyTotal = moneyTotal;
    }
   
    public Double getMoneyUsable() {
        return moneyUsable;
    }

    public void setMoneyUsable(Double moneyUsable) {
        this.moneyUsable = moneyUsable;
    }

    public Double getMoneyWithdraw() {
        return moneyWithdraw;
    }

    public void setMoneyWithdraw(Double moneyWithdraw) {
        this.moneyWithdraw = moneyWithdraw;
    }

    public Double getMoneyInsure() {
        return moneyInsure;
    }

    public void setMoneyInsure(Double moneyInsure) {
        this.moneyInsure = moneyInsure;
    }

    public Double getProfitsTotal() {
        return profitsTotal;
    }

    public void setProfitsTotal(Double profitsTotal) {
        this.profitsTotal = profitsTotal;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getMoneyTenderFreeze() {
        return moneyTenderFreeze;
    }

    public void setMoneyTenderFreeze(Double moneyTenderFreeze) {
        this.moneyTenderFreeze = moneyTenderFreeze;
    }

    public Double getMoneyCollection() {
        return moneyCollection;
    }

    public void setMoneyCollection(Double moneyCollection) {
        this.moneyCollection = moneyCollection;
    }
    
	public UserAccount() {
		super();
	}

	public UserAccount(Long userId, Double moneyTotal, Double moneyUsable,
			Double moneyWithdraw, Double moneyInsure, Double profitsTotal,
			Double moneyTenderFreeze, Double moneyCollection, Long createdAt,
			Long updatedAt) {
		super();
		this.userId = userId;
		this.moneyTotal = moneyTotal;
		this.moneyUsable = moneyUsable;
		this.moneyWithdraw = moneyWithdraw;
		this.moneyInsure = moneyInsure;
		this.profitsTotal = profitsTotal;
		this.moneyTenderFreeze = moneyTenderFreeze;
		this.moneyCollection = moneyCollection;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
    
}
