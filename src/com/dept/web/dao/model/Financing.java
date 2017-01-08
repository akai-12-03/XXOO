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
public class Financing extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -7893761745119898700L;

    
   private Long  user_id;//		用户id	
   private String name;//    标题	
   private Double money;//	金额	
   private String  num;//		编号	
   private Integer counts;//	浏览次数	
   private Integer trade;	//行业类型 1：贸易型2：房地产。3：机械制造。4：网络科技，5：其他				0	0
   private Integer city	;  //地区：1：杭州。2：上海				0	0
   private Integer time_limit;	//期限				0	0
   private Integer guarantee;	//担保方式：1：固定资产抵押,2:个人担保。3：第三方担保				0	0
   private Integer risk;	//风险评级：1：r1.2:r2,3:r3,:4:r4				0	0
   private Integer types;	//分类：1：p1 or c1  ;2：p2 or c2;3：p3 or c3, 				0	0
   private Integer types1;//类型：1:个人。2：企业				0	0
   private Integer status;	//状态 ：0：未审核。 1：融资中。2：审核失败。3：已成功。				0	0
   private String addtime;//添加时间	utf8	utf8_general_ci		0	0
   private String starttime;	//发布时间	utf8	utf8_general_ci		0	0
   private Double  apr	;	//年化率	
   private String content	;//项目详情	
   private String jdbg;		//尽调报告	
   private String fkxx;		//分控信息	
   private String hkjh;		//还款计划	
   private String  zjjy	;	//最近交易	
   private String contents	;//项目详情	
   private String jdbgs;		//尽调报告	
   private String fkxxs;		//分控信息	
   private String hkjhs;		//还款计划	
   private String  zjjys	;	//最近交易	

   private String moneyStr; //String类型的钱。
   
   private String username; // 用户名
   private String endTime; //结束时间
   private Integer totalCounts;//总参与融资人数
   private Double totalMoney;//总融资金额
   private Double aprs; // 平均融资利率
   
   private String totalMoneyStr;
   
   
   
   public String getTotalMoneyStr() {
	   
	   if(this.totalMoney !=null){
		   if(this.totalMoney>0){
		   		BigDecimal bd = new BigDecimal(this.totalMoney); 
		   		totalMoneyStr=bd.toPlainString();
		   	}
		   return totalMoneyStr;  
	   }else{
		   return "";
	   }
		
	}
	
	public String getContents() {
	return contents;
}

public void setContents(String contents) {
	this.contents = contents;
}

public String getJdbgs() {
	return jdbgs;
}

public void setJdbgs(String jdbgs) {
	this.jdbgs = jdbgs;
}

public String getFkxxs() {
	return fkxxs;
}

public void setFkxxs(String fkxxs) {
	this.fkxxs = fkxxs;
}

public String getHkjhs() {
	return hkjhs;
}

public void setHkjhs(String hkjhs) {
	this.hkjhs = hkjhs;
}

public String getZjjys() {
	return zjjys;
}

public void setZjjys(String zjjys) {
	this.zjjys = zjjys;
}

	public void setTotalMoneyStr(String totalMoneyStr) {
		this.totalMoneyStr = totalMoneyStr;
	}

	public Integer getTotalCounts() {
		return totalCounts;
	}
	
	public void setTotalCounts(Integer totalCounts) {
		this.totalCounts = totalCounts;
	}
	
	public Double getTotalMoney() {
		return totalMoney;
	}
	
	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
	
	public Double getAprs() {
		return aprs;
	}
	
	public void setAprs(Double aprs) {
		this.aprs = aprs;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getMoneyStr() {
		if(this.money!=null){
			if(this.money>0){
		   		BigDecimal bd = new BigDecimal(this.money); 
		   		moneyStr=bd.toPlainString();
		   	}
				return moneyStr;
		}else{
			return "";
		}
   	
	}

	public void setMoneyStr(String moneyStr) {
		this.moneyStr = moneyStr;
	}
	public String getUsername() {
	return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public Integer getCounts() {
		return counts;
	}
	public void setCounts(Integer counts) {
		this.counts = counts;
	}
	public Integer getTrade() {
		return trade;
	}
	public void setTrade(Integer trade) {
		this.trade = trade;
	}
	public Integer getCity() {
		return city;
	}
	public void setCity(Integer city) {
		this.city = city;
	}
	public Integer getTime_limit() {
		return time_limit;
	}
	public void setTime_limit(Integer time_limit) {
		this.time_limit = time_limit;
	}
	public Integer getGuarantee() {
		return guarantee;
	}
	public void setGuarantee(Integer guarantee) {
		this.guarantee = guarantee;
	}
	public Integer getRisk() {
		return risk;
	}
	public void setRisk(Integer risk) {
		this.risk = risk;
	}
	public Integer getTypes() {
		return types;
	}
	public void setTypes(Integer types) {
		this.types = types;
	}
	public Integer getTypes1() {
		return types1;
	}
	public void setTypes1(Integer types1) {
		this.types1 = types1;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	
	public Double getApr() {
		return apr;
	}

	public void setApr(Double apr) {
		this.apr = apr;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getJdbg() {
		return jdbg;
	}
	public void setJdbg(String jdbg) {
		this.jdbg = jdbg;
	}
	public String getFkxx() {
		return fkxx;
	}
	public void setFkxx(String fkxx) {
		this.fkxx = fkxx;
	}
	public String getHkjh() {
		return hkjh;
	}
	public void setHkjh(String hkjh) {
		this.hkjh = hkjh;
	}
	public String getZjjy() {
		return zjjy;
	}
	public void setZjjy(String zjjy) {
		this.zjjy = zjjy;
	}
    
}
