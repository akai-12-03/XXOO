package com.dept.web.dao.model;

import java.math.BigDecimal;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

public class HongbaoLog  extends BaseEntity{
	
  private static final long serialVersionUID = -767424146555556L;
  
  	private Long id;
	
    private Long userId;

    private BigDecimal money;

    private Integer status;

    private Integer htype;

    private Integer expiredDays;

    private Long createdAt;

    private Integer createdBy;

    private Long updateAt;

    private Integer updateBy;
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getHtype() {
        return htype;
    }

    public void setHtype(Integer htype) {
        this.htype = htype;
    }

    public Integer getExpiredDays() {
        return expiredDays;
    }

    public void setExpiredDays(Integer expiredDays) {
        this.expiredDays = expiredDays;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }
}