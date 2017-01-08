package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 用户提现申请
 * 
 * @ClassName:     UserWithdraw
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午11:01:09 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class UserWithdraw extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -5513008250575170443L;

    /**
     * 提现金额
     */
    private Double moneyWithdraw;
    
    private String remark;
    
    private Integer status;
    
    private Long createdBy;
    
    private Long createdAt;
    
    private Long updatedBy;
    
    private Long updatedAt;
    
    private String createdIp;
    
    private String useCard;
    
    private String cardName;
    
    private String cardNo;

    private String orderId;//订单流水号
    
    private String message;//第三方返回信息

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUseCard() {
		return useCard;
	}

	public void setUseCard(String useCard) {
		this.useCard = useCard;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Double getMoneyWithdraw() {
        return moneyWithdraw;
    }

    public void setMoneyWithdraw(Double moneyWithdraw) {
        this.moneyWithdraw = moneyWithdraw;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
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

	public UserWithdraw() {
		super();
	}

	public UserWithdraw(Double moneyWithdraw, String remark, Integer status,
			Long createdBy, Long createdAt, Long updatedBy, Long updatedAt,
			String createdIp) {
		super();
		this.moneyWithdraw = moneyWithdraw;
		this.remark = remark;
		this.status = status;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.updatedBy = updatedBy;
		this.updatedAt = updatedAt;
		this.createdIp = createdIp;
	}
    
}
