package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 保证金增持纪录
 * 
 * @ClassName:     PlanAppendInsure
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午10:13:46 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class PlanAppendInsure extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = 4000449623130112098L;

    /**
     * 配资用户ID
     */
    private Long userId;
    
    /**
     * 配资纪录ID
     */
    private Long planRecordId;
    
    /**
     * 增持保证金
     */
    private Double moneyInsure;
    
    /**
     * 状态(-1 删除；0 新建；1 批准)
     */
    private Integer status;
    
    private Long userOperate;
    
    /**
     * 操作记录
     */
    private String opLog;
    
    private Long createdAt;
    
    private Long operatedAt;
    
    
    /**查询用*/
    private Long planType;
    
    private Double moneyOp;
    
    private Double oldMoneyInsure;
    
    private Double moneyFee;
    
    private Double rate;
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getPlanRecordId() {
        return planRecordId;
    }

    public void setPlanRecordId(Long planRecordId) {
        this.planRecordId = planRecordId;
    }
    
    public Double getMoneyInsure() {
        return moneyInsure;
    }

    public void setMoneyInsure(Double moneyInsure) {
        this.moneyInsure = moneyInsure;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserOperate() {
        return userOperate;
    }

    public void setUserOperate(Long userOperate) {
        this.userOperate = userOperate;
    }

    public String getOpLog() {
        return opLog;
    }

    public void setOpLog(String opLog) {
        this.opLog = opLog;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getOperatedAt() {
        return operatedAt;
    }

    public void setOperatedAt(Long operatedAt) {
        this.operatedAt = operatedAt;
    }

    public Long getPlanType() {
        return planType;
    }

    public void setPlanType(Long planType) {
        this.planType = planType;
    }

    public Double getMoneyOp() {
        return moneyOp;
    }

    public void setMoneyOp(Double moneyOp) {
        this.moneyOp = moneyOp;
    }

    public Double getOldMoneyInsure() {
        return oldMoneyInsure;
    }

    public void setOldMoneyInsure(Double oldMoneyInsure) {
        this.oldMoneyInsure = oldMoneyInsure;
    }

    public Double getMoneyFee() {
        return moneyFee;
    }

    public void setMoneyFee(Double moneyFee) {
        this.moneyFee = moneyFee;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
    
    
}
