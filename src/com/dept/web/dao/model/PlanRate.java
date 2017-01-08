package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 配资利率设定表
 * 
 * @ClassName:     PlanRate
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午10:18:05 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class PlanRate extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = 3964028272934675839L;

    /**
     * 配资类型(1. 按天配资; 2. 按月配资;)
     */
    private Integer type;
    
    /**
     * 配资金额
     */
    private Double amount;
    
    /**
     * 时间跨度
     */
    private Integer interval;
    
    /**
     * 配资利率
     */
    private Double rate;
    
    /**
     * 利率状态(0 新建；1 批准)
     */
    private Integer status;
    
    /**
     * 创建人id
     */
    private Long userCreate;
    
    /**
     * 审核人id
     */
    private Long userOperate;
    
    private Long createdAt;
    
    private Long updatedAt;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(Long userCreate) {
        this.userCreate = userCreate;
    }

    public Long getUserOperate() {
        return userOperate;
    }

    public void setUserOperate(Long userOperate) {
        this.userOperate = userOperate;
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
    
    
    
}
