package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

public class BorrowApiLog extends BaseEntity{
	private static final long serialVersionUID = -931397715769075199L;
	private Long borrowId;//标id
	private Long userId;//用户Id
	private double money;//投的有效金额
	private String remark;//备注
	private int status; //状态
	private String valid_account; ////有效金额-管理费-奖励（和资金托管对应）
    private String operation_account;//平台管理费
    private int source;
    private Long tenderId;
    
    
	public String getValid_account() {
		return valid_account;
	}
	public void setValid_account(String valid_account) {
		this.valid_account = valid_account;
	}
	public String getOperation_account() {
		return operation_account;
	}
	public void setOperation_account(String operation_account) {
		this.operation_account = operation_account;
	}
	public Long getBorrowId() {
		return borrowId;
	}
	public void setBorrowId(Long borrowId) {
		this.borrowId = borrowId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public Long getTenderId() {
		return tenderId;
	}
	public void setTenderId(Long tenderId) {
		this.tenderId = tenderId;
	}
	
	
}
