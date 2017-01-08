package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

public class Homs extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -1178553973928142394L;

    /**
     * HOMS账户
     */
    private Long homsId;
    
    /**
     * 0-可使用 1-配资中 2-失效
     */
    private Integer status;
    
    private Long creatorId;
    
    private Long createdAt;
    
    private Long updatedAt;

    public Long getHomsId() {
        return homsId;
    }

    public void setHomsId(Long homsId) {
        this.homsId = homsId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
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
