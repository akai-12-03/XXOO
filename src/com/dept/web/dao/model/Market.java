package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 转让市场
 * 
 * @ClassName:     Market
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年6月25日 上午11:52:14 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class Market extends BaseEntity{  
    
    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = 5316538125550526603L;

    /**
     * 借款标ID
     */
    private Long borrowId;
    
    /**
     * 投标记录ID
     */
    private Long tenderId;
    
    /**
     * 原投标记录用户
     */
    private Long tenderUserId;
    
    /**
     * 原投标价格
     */
    private Double tenderPrice;
    
    /**
     * 转让价格
     */
    private Double transferPrice;
    
    /**
     * 待收金额
     */
    private Double collectionMoney;
    
    /**
     * 当前还款期
     */
    private Integer repayOrder;
    
    /**
     * 总还款期
     */
    private Integer repayTotalOrder;
    
    
    /**
     * 借款利率
     */
    private Double borrowApr;
    
    /**
     * 借款标名
     */
    private String borrowName;
    
    
    /**
     * 状态 0新建 1已被接受 2已撤销
     */
    private Integer status; 
    
    /**
     * 剩余天数
     */
    private Integer remainingDays;
    
    private Long createdAt;
    
    private Long createdBy;
    
    private Long updateAt;
    
    private Long updateBy;
    
    /** 新增  ***/
    /**
     * 接收用户名
     */
    private String receUsername;
    /**
     * 项目描述
     */
    private String content;
    /**
     * 还款方式
     */
    private int repaymentStyle;
    /**
     * 项目借款期限
     */
    private String timeLimit;
    /**
     * 是否天标
     */
    private int isday;
    

    public Long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
    }

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public Long getTenderUserId() {
        return tenderUserId;
    }

    public void setTenderUserId(Long tenderUserId) {
        this.tenderUserId = tenderUserId;
    }

    public Double getTenderPrice() {
        return tenderPrice;
    }

    public void setTenderPrice(Double tenderPrice) {
        this.tenderPrice = tenderPrice;
    }

    public Double getTransferPrice() {
        return transferPrice;
    }

    public void setTransferPrice(Double transferPrice) {
        this.transferPrice = transferPrice;
    }

    public Double getCollectionMoney() {
        return collectionMoney;
    }

    public void setCollectionMoney(Double collectionMoney) {
        this.collectionMoney = collectionMoney;
    }

    public Integer getRepayOrder() {
        return repayOrder;
    }

    public void setRepayOrder(Integer repayOrder) {
        this.repayOrder = repayOrder;
    }

    public Integer getRepayTotalOrder() {
        return repayTotalOrder;
    }

    public void setRepayTotalOrder(Integer repayTotalOrder) {
        this.repayTotalOrder = repayTotalOrder;
    }

    public Double getBorrowApr() {
        return borrowApr;
    }

    public void setBorrowApr(Double borrowApr) {
        this.borrowApr = borrowApr;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(Integer remainingDays) {
        this.remainingDays = remainingDays;
    }

    public String getReceUsername() {
        return receUsername;
    }

    public void setReceUsername(String receUsername) {
        this.receUsername = receUsername;
    }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getRepaymentStyle() {
		return repaymentStyle;
	}

	public void setRepaymentStyle(int repaymentStyle) {
		this.repaymentStyle = repaymentStyle;
	}

	public String getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}

	public int getIsday() {
		return isday;
	}

	public void setIsday(int isday) {
		this.isday = isday;
	}

	
}
