package com.dept.web.dao.model;


import com.sendinfo.xspring.ibatis.base.BaseEntity;

public class Hongbao extends BaseEntity {

	private static final long serialVersionUID = -7674246555556L;
	private Long id;
	private Long user_id;// 用户id
	private Double money;// 红包金额

	private String addtime;// 添加时间
	private String endtime;// 结束时间
	private Integer type;// 类型。 1:用户注册送。 2:推荐好友送。
	private Integer status;// 状态： 0 未使用。 1:已使用。 2:正在使用中。3:已过期
	private String usetime;//使用时间
	private String allAmont;
	private String yesAmont;
	private String freezeAmont;
	private String relAmont;
	
	

	public String getUsetime() {
		return usetime;
	}

	public void setUsetime(String usetime) {
		this.usetime = usetime;
	}

	public String getRelAmont() {
		return relAmont;
	}

	public void setRelAmont(String relAmont) {
		this.relAmont = relAmont;
	}

	public String getAllAmont() {
		return allAmont;
	}

	public void setAllAmont(String allAmont) {
		this.allAmont = allAmont;
	}

	public String getYesAmont() {
		return yesAmont;
	}

	public void setYesAmont(String yesAmont) {
		this.yesAmont = yesAmont;
	}

	public String getFreezeAmont() {
		return freezeAmont;
	}

	public void setFreezeAmont(String freezeAmont) {
		this.freezeAmont = freezeAmont;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
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

}