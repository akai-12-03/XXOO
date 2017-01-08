package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 配资记录表
 * 
 * @ClassName:     PlanRecord
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午10:26:42 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class PlanRecord extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = 5142420642502311371L;

    /**
     * 配资用户ID
     */
    private Long userId;
    
    /**
     * 配资计划ID
     */
    private Long planId;
    
    /**
     * 配资类型 
     */
    private Long planType;
    
    
    /**
     * 保证金
     */
    private Double moneyInsure;
    
    
    /**
     * 配资倍比
     */
    private Double power;
    
    
    /**
     * 配资时间
     */
    private Integer interval;
    
    
    /**
     * 配资利率
     */
    private Double rate;
    
    
    /**
     * 管理费
     */
    private Double moneyFee;
    
    
    /**
     * 预警线
     */
    private Double moneyWarning;
    
    
    /**
     * 平仓线
     */
    private Double moneyForce;
    
    
    /**
     * 平仓线
     */
    private Double moneyOp;
    
    
    /**
     * 结算金
     */
    private Double moneySettlement;
    
    
    /**
     * 状态(-1 删除；0 新建；1 批准)
     */
    private Integer status;
    
    /**
     * Homs 账号
     */
    private String homsAccount;
    
    /**
     * homs 密码
     */
    private String homsPwd;
    
    /**
     * 操作人id
     */
    private Long userOperate;
    
    /**
     * 配资开始时间
     */
    private Long startTime;
    
    /**
     * 配资结束时间
     */
    private Long endTime;
    
    private String opLog;
    
    private Long createdAt;
    
    private Long operatedAt;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getPlanType() {
        return planType;
    }

    public void setPlanType(Long planType) {
        this.planType = planType;
    }

    public Double getMoneyInsure() {
        return moneyInsure;
    }

    public void setMoneyInsure(Double moneyInsure) {
        this.moneyInsure = moneyInsure;
    }

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getMoneyFee() {
        return moneyFee;
    }

    public void setMoneyFee(Double moneyFee) {
        this.moneyFee = moneyFee;
    }

    public Double getMoneyWarning() {
        return moneyWarning;
    }

    public void setMoneyWarning(Double moneyWarning) {
        this.moneyWarning = moneyWarning;
    }

    public Double getMoneyForce() {
        return moneyForce;
    }

    public void setMoneyForce(Double moneyForce) {
        this.moneyForce = moneyForce;
    }

    public Double getMoneyOp() {
        return moneyOp;
    }

    public void setMoneyOp(Double moneyOp) {
        this.moneyOp = moneyOp;
    }

    public Double getMoneySettlement() {
        return moneySettlement;
    }

    public void setMoneySettlement(Double moneySettlement) {
        this.moneySettlement = moneySettlement;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getHomsAccount() {
        return homsAccount;
    }

    public void setHomsAccount(String homsAccount) {
        this.homsAccount = homsAccount;
    }

    public String getHomsPwd() {
        return homsPwd;
    }

    public void setHomsPwd(String homsPwd) {
        this.homsPwd = homsPwd;
    }

    public Long getUserOperate() {
        return userOperate;
    }

    public void setUserOperate(Long userOperate) {
        this.userOperate = userOperate;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
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

    
}
