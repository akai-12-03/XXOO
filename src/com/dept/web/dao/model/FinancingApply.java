package com.dept.web.dao.model;

import java.math.BigDecimal;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 用户申请
 * 
 * @ClassName:     UserAccount
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午10:46:49 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class FinancingApply extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -7893761745619898700L;
    
  
    private String name	;//名称	
    private String name_py;//	拼音
    private String phone; //	联系方式
    private String address;	//地址
    private String company;//工作单位
    private String  company_phone; //工作单位联系电话
    private Integer year_money;//年收入 1：小于5万，2：5-10万。 3：10-15，15万已下
    private Integer house; //住房情况： 1： 自有，2：租房，3：与父母同住。	
    private String apply_money;//申请融资金额
    private Integer apply_type;//申请类别	1：信用卡，2：个人消费贷款 ，3：个人经营贷款	4活动资金贷款 5：经营性物业贷款 6：收益权转让
    private Integer apply_guarantee	; //担保方式：1房屋抵押，2：汽车抵押，3：无 4：固定资产抵押 5：第三方担保。6： 	应收账款质押 7：收益权质押
    private Integer money_use;//资金用途	1：消费 2：经营
    private String yyzz	;//营业执照
    private String company_title; //企业名称
    private String  sshy;//所属行业
    private Long user_id	;//用户id	
    private String addtime; //添加时间	
    private Integer  status	;//状态：	0:未付定金，1：已付定金，2：审核中，3：发标中，4：融资中，5：已还款。6：审核不通过				0	0
    private Integer types;// 类型 1：个人，2：企业	
    private String money;//定金金额
    
    private String setName;
    private String endtime; 
    private String starttime;
    
    
    
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getSetName() {
		return setName;
	}
	public void setSetName(String setName) {
		this.setName = setName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName_py() {
		return name_py;
	}
	public void setName_py(String name_py) {
		this.name_py = name_py;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompany_phone() {
		return company_phone;
	}
	public void setCompany_phone(String company_phone) {
		this.company_phone = company_phone;
	}
	
	public Integer getYear_money() {
		return year_money;
	}
	public void setYear_money(Integer year_money) {
		this.year_money = year_money;
	}
	public Integer getHouse() {
		return house;
	}
	public void setHouse(Integer house) {
		this.house = house;
	}
	public String getApply_money() {
		return apply_money;
	}
	public void setApply_money(String apply_money) {
		this.apply_money = apply_money;
	}
	public Integer getApply_type() {
		return apply_type;
	}
	public void setApply_type(Integer apply_type) {
		this.apply_type = apply_type;
	}
	public Integer getApply_guarantee() {
		return apply_guarantee;
	}
	public void setApply_guarantee(Integer apply_guarantee) {
		this.apply_guarantee = apply_guarantee;
	}
	public Integer getMoney_use() {
		return money_use;
	}
	public void setMoney_use(Integer money_use) {
		this.money_use = money_use;
	}
	public String getYyzz() {
		return yyzz;
	}
	public void setYyzz(String yyzz) {
		this.yyzz = yyzz;
	}
	public String getCompany_title() {
		return company_title;
	}
	public void setCompany_title(String company_title) {
		this.company_title = company_title;
	}
	public String getSshy() {
		return sshy;
	}
	public void setSshy(String sshy) {
		this.sshy = sshy;
	}
	
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getTypes() {
		return types;
	}
	public void setTypes(Integer types) {
		this.types = types;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

    
}
