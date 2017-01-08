package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 验证码
 * 
 * @ClassName:     UserCaptcha
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午10:53:05 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class UserCaptcha extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -7726308723869418322L;

    /**
     * 用户id，没有则0，比如注册时
     */
    private Long userId;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 验证码
     */
    private String captcha;
    
    /**
     * 类型，比如注册、找回密码等
     */
    private String type;
    
    /**
     * 生成时间
     */
    private Long generateTime;
    
    /**
     * 过期时间
     */
    private Long expireTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Long generateTime) {
        this.generateTime = generateTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }
    
}
