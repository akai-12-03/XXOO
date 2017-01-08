package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

public class SmsJop  extends BaseEntity{
	
	private String phone;
	
	private String addtime;
	
	private String type;

	
	private String startTime;
	
	private String endTime;
	
	
	
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
