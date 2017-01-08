package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 用户资金流水表
 * 
 * @ClassName:     UserAccountLog
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午10:50:56 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class UserAccountLog extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -4979042401585349080L;

    /**
     * 关联id
     */
    private Long idRelated;
    
    /**
     * 用户id
     */
    private Long userId;
    
    /**
     * 操作类型
     */
    private Integer type;
    
    /**
     * 操作金额
     */
    private Double moneyOperate;
    
    /**
     * 总金额
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
     * 配资保证金(冻结)
     */
    private Double moneyInsure;
    
    private String remark;
    
    private Long createdAt;
    
    private String createdIp;
    
    private Double moneyTenderFreeze;
    
    private Double moneyCollection;
    
    private String toUserName;
    
    

    public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public Long getIdRelated() {
        return idRelated;
    }

    public void setIdRelated(Long idRelated) {
        this.idRelated = idRelated;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getMoneyOperate() {
        return moneyOperate;
    }

    public void setMoneyOperate(Double moneyOperate) {
        this.moneyOperate = moneyOperate;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedIp() {
        return createdIp;
    }

    public void setCreatedIp(String createdIp) {
        this.createdIp = createdIp;
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
    
	public UserAccountLog() {
		super();
	}

	public UserAccountLog(Long userId, Integer type, Double moneyOperate,
			Double moneyTotal, Double moneyUsable, Double moneyWithdraw,
			String remark, Long createdAt, String createdIp, Double moneyInsure) {
		super();
		this.userId = userId;
		this.type = type;
		this.moneyOperate = moneyOperate;
		this.moneyTotal = moneyTotal;
		this.moneyUsable = moneyUsable;
		this.moneyWithdraw = moneyWithdraw;
		this.remark = remark;
		this.createdAt = createdAt;
		this.createdIp = createdIp;
		this.moneyInsure = moneyInsure;
	}
	
}
