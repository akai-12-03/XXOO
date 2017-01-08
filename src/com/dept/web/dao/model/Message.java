package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 系统信息表
 * 
 * @ClassName:     Message
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午10:09:11 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class Message extends BaseEntity{
    
    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -6219903669981087301L;

    private String name;
    
    private String content;
    
    private Integer type;
    
    private Integer status;
    
    private Long createdBy;
    
    private Long updatedBy;
    
    private Long createdAt;
    
    private Long updatedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
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
