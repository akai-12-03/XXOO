package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 管理员基本信息表
 * 
 * @ClassName:     AdminUser
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午9:07:13 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class AdminUser extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = 1142434072009129907L;

    /**
     * 用户名
     */
    private String username;
    
    /**
     * 邮箱名
     */
    private String email;
    
    /**
     * 手机号
     */
    private String mobile;
    
    /**
     * 登录密码
     */
    private String passwordHash;
    
    /**
     * 重置密码token
     */
    private String passwordResetToken;
    
    /**
     * 记住我
     */
    private String authKey;
    
    /**
     * 用户状态 
     */
    private Integer status;
    
    /**
     * 上次登录时间
     */
    private Long lastVisitTime;
    
    /**
     * 创建用户时间
     */
    private Long createdAt;
    
    /**
     * 更新用户时间
     */
    private Long updatedAt;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(Long lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
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
