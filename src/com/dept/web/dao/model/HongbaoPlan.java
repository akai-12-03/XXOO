package com.dept.web.dao.model;


import com.sendinfo.xspring.ibatis.base.BaseEntity;

public class HongbaoPlan  extends BaseEntity{
	
  private static final long serialVersionUID = -7674246555556L;
  
  	private Long id;
	
    private Long userId;

    private Long hongbaoId ;

    private Long  planRecordId;

    private Integer hongbaoStatus;
    
    private Integer planRecordStatus;
     
    private Long createdAt;

    private Integer createdBy;

    private Long updateAt;

    private Integer updateBy;

	public Long getId() {
		return id;
	}

	public Integer getHongbaoStatus() {
		return hongbaoStatus;
	}


	public void setHongbaoStatus(Integer hongbaoStatus) {
		this.hongbaoStatus = hongbaoStatus;
	}


	public Integer getPlanRecordStatus() {
		return planRecordStatus;
	}


	public void setPlanRecordStatus(Integer planRecordStatus) {
		this.planRecordStatus = planRecordStatus;
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

	public Long getHongbaoId() {
		return hongbaoId;
	}

	public void setHongbaoId(Long hongbaoId) {
		this.hongbaoId = hongbaoId;
	}

	public Long getPlanRecordId() {
		return planRecordId;
	}

	public void setPlanRecordId(Long planRecordId) {
		this.planRecordId = planRecordId;
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