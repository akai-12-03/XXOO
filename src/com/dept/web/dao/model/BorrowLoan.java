package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 申请借款
 * 
 * @ClassName:     BorrowLoan
 * @Description:   
 *
 * @author         gwx
 * @version        V1.0 
 * @Date           2016年2月24日 下午3:36:54 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class BorrowLoan extends BaseEntity{
    /**
	 * 描述:
	*/       
	private static final long serialVersionUID = 1L;
	private String realname;
    private String phone;
    private int status;
    private int carStatus;
    private long verifyUser;
    private String verifyTime;
    private String addtime;
    private long addUserid;
    private String createdIp;
    

	public String getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(String verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getRealname() {
		return realname;
	}
	
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCarStatus() {
		return carStatus;
	}
	public void setCarStatus(int carStatus) {
		this.carStatus = carStatus;
	}
	public long getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(long verifyUser) {
		this.verifyUser = verifyUser;
	}

	public long getAddUserid() {
		return addUserid;
	}

	public void setAddUserid(long addUserid) {
		this.addUserid = addUserid;
	}

	public String getCreatedIp() {
		return createdIp;
	}
	public void setCreatedIp(String createdIp) {
		this.createdIp = createdIp;
	}
    
}
