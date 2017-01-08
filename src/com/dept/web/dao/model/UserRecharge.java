package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 用户充值申请
 * 
 * @ClassName:     UserRecharge
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午10:59:00 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class UserRecharge extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = 767424148339715046L;

    /**
     * 支付订单ID
     */
    private String orderId;
    
    /**
     * 充值账号
     */
    private String account;
    
    /**
     * 银行卡号
     */
    private String cardNo;
    
    /**
     * 充值金额
     */
    private Double moneyRecharge;
    
    private String remark;
    
    /**
     * 第三方支付平台（联动，连连
     */
    private Long thirdPlatform;
    
    /**
     * 第三方平台对应的订单支付ID
     */
    private Long thirdPlatformOrderId;
    
    /**
     * 支付来源（IOS，Android，Web，Wap）
     */
    private String paySource;
    
    /**
     * 第三方平台回复的支付结果
     */
    private String payResult;
    
    /**
     * 充值状态
     */
    private Integer status;
    
    private Long createdBy;
    
    private Long updatedBy;
    
    private Long createdAt;
    
    private Long updatedAt;
    
    private String createdIp;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Double getMoneyRecharge() {
        return moneyRecharge;
    }

    public void setMoneyRecharge(Double moneyRecharge) {
        this.moneyRecharge = moneyRecharge;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getThirdPlatform() {
        return thirdPlatform;
    }

    public void setThirdPlatform(Long thirdPlatform) {
        this.thirdPlatform = thirdPlatform;
    }

    public Long getThirdPlatformOrderId() {
        return thirdPlatformOrderId;
    }

    public void setThirdPlatformOrderId(Long thirdPlatformOrderId) {
        this.thirdPlatformOrderId = thirdPlatformOrderId;
    }

    public String getPaySource() {
        return paySource;
    }

    public void setPaySource(String paySource) {
        this.paySource = paySource;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
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

    public String getCreatedIp() {
        return createdIp;
    }

    public void setCreatedIp(String createdIp) {
        this.createdIp = createdIp;
    }
    
    
}
