package com.dept.web.dao.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.dept.web.general.util.NumberUtil;
import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 借款标
 * 
 * @ClassName:     Borrow
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年5月16日 下午3:36:54 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class Borrow extends BaseEntity{
    
    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -7916132687465754579L;

    /**
     * 发布者ID
     */
    private Long userId; 
    
    /**
     * 标题
     */
    private String name;
    
    /**
     * 状态
     */
    private Integer status;
    
    /**
     * 排序
     */
    private Integer boOrder;
    
    
    /**
     * 点击数
     */
    private Integer hits;
    
    private String num;
    
    
   


	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}


	/**
     * 缩略图
     */
    private String litpic;
    
    
    /**
     * 借款标类型
     */
    private Integer borrowType;
    
    
    /**
     * 审核人
     */
    private Long verifyUser;
    
    
    /**
     * 审核时间
     */
    private Long verifyTime;
    
    
    /**
     * 审核备注
     */
    private String verifyRemark;
    
    
    /**
     * 还款金额
     */
    private Double repaymentAccount;
    
    
    /**
     * 已还款金额
     */
    private Double repaymentYesAccount;
    
    
    /**
     * 
     */
    private Double repaymentYesInterest;
    
    
    /**
     * 用途
     */
    private Integer borrowUse;
    
    
    /**
     * 借款期限
     */
    private Integer timeLimit;
    
    
    /**
     * 还款方式
     */
    private Integer repaymentStyle;
    
    
    /**
     * 借款总金额
     */
    private Double account;
    
    
    /**
     * 已募集金额
     */
    private Double accountYes;
    
    
    /**
     * 借款利率
     */
    private Double apr;
    
    
    /**
     * 最低投标金额
     */
    private Double lowestAccount;
    
    
    /**
     * 最高投标金额
     */
    private Double mostAccount;
    
    
    /**
     * 标有效时间
     */
    private Integer validTime;


    /**
     * 投标奖励 0没有2金额1比例
     */
    private Integer award;
    
    
    /**
     * 分摊奖励金额
     */
    private Double partAccount;
    

    /**
     * 比例奖励的比例
     */
    private Double funds;
    
    
    /**
     * 公开我的帐户资金情况
     */
    private String openAccount;
    
    
    /**
     * 公开我的借款资金情况
     */
    private String openBorrow;
    
    
    /**
     * 公开我的投标资金情况
     */
    private String openTender;
    
    
    /**
     * 公开我的信用额度情况
     */
    private String openCredit;
    
    
    /**
     * 详细说明
     */
    private String content;
    private String contents;
    
    
    /**
     * 添加时间
     */
    private Long addtime;
    
    
    /**
     * 添加IP
     */
    private String addip;
    
    
    /**
     * 定向标密码
     */
    private String pwd;
    
    
    /**
     * 是否天标
     */
    private Integer isDay;
    
    
    /**
     * 认证等级
     */
    private String trustLevel;
    
    
    /**
     * 借款手续费率
     */
    private String borrowFeeApr;
    
    
    /**
     * 版本
     */
    private Integer version;
    
    /**
     * 借款人ID
     */
    private Long borrowerUserId;
    
    
    private String updateip;
    
    private Long updatetime;
    
    private double scale;
    
    private String username;
    private String scales;
    private String persionSize;
    
    private String fxpj;  //风险评级
    private String cpxq;  //产品详情
    private String jyjg;  //交易结构
    private String fkcs;  //风控措施
    private String zggf;  //资管各方
    private String cpxqs;  //产品详情
    private String jyjgs;  //交易结构
    private String fkcss;  //风控措施
    private String zggfs;  //资管各方
    private Integer index_status;//推荐至首页

    private Integer limits;//条数
    
    private int count;
    
    private Double borrow_fee;//手续费
    
    private String mortgagor;//垫资标借款人
    private Long receivePerson;//垫资标收款人
    private Long repayPerson;//垫资标还款人
    
    private Double marketFee;//转让手续费
    
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getPersionSize() {
		return persionSize;
	}

	public void setPersionSize(String persionSize) {
		this.persionSize = persionSize;
	}

	public Integer getLimits() {
		return limits;
	}

	public void setLimits(Integer limits) {
		this.limits = limits;
	}

	public Integer getIndex_status() {
		return index_status;
	}

	public void setIndex_status(Integer index_status) {
		this.index_status = index_status;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getCpxqs() {
		return cpxqs;
	}

	public void setCpxqs(String cpxqs) {
		this.cpxqs = cpxqs;
	}

	public String getJyjgs() {
		return jyjgs;
	}

	public void setJyjgs(String jyjgs) {
		this.jyjgs = jyjgs;
	}

	public String getFkcss() {
		return fkcss;
	}

	public void setFkcss(String fkcss) {
		this.fkcss = fkcss;
	}

	public String getZggfs() {
		return zggfs;
	}

	public void setZggfs(String zggfs) {
		this.zggfs = zggfs;
	}


	private int zrs; 
    
    private Double aprs;
    

	

	

	public Double getAprs() {
		return aprs;
	}

	public void setAprs(Double aprs) {
		this.aprs = aprs;
	}

	public int getZrs() {
		return zrs;
	}

	public void setZrs(int zrs) {
		this.zrs = zrs;
	}

	public String getJyjg() {
		return jyjg;
	}

    public String getCpxq() {
		return cpxq;
	}


	public void setCpxq(String cpxq) {
		this.cpxq = cpxq;
	}


	public String getFxpj() {
		return fxpj;
	}


	public void setFxpj(String fxpj) {
		this.fxpj = fxpj;
	}



	public void setJyjg(String jyjg) {
		this.jyjg = jyjg;
	}


	public String getFkcs() {
		return fkcs;
	}


	public void setFkcs(String fkcs) {
		this.fkcs = fkcs;
	}


	public String getZggf() {
		return zggf;
	}


	public void setZggf(String zggf) {
		this.zggf = zggf;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getScales() {
		return scales;
	}

    
    private int tzcount; //投资人数
    
    private String lastTime;//倒计时结束时间
    
    
    public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
    
    public int getTzcount() {
		return tzcount;
	}


	public void setTzcount(int tzcount) {
		this.tzcount = tzcount;
	}

	private Double lastAccount;
    

	public void setScales(String scales) {
		this.scales = scales;
	}


    public Double getLastAccount() {
		return lastAccount;
	}


	public void setLastAccount(Double lastAccount) {
		this.lastAccount = lastAccount;
	}


	public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Integer getStatus() {
        return status;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }


    public Integer getBoOrder() {
        return boOrder;
    }


    public void setBoOrder(Integer boOrder) {
        this.boOrder = boOrder;
    }


    public Integer getHits() {
        return hits;
    }


    public void setHits(Integer hits) {
        this.hits = hits;
    }


    public String getLitpic() {
        return litpic;
    }


    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }


    public Integer getBorrowType() {
        return borrowType;
    }


    public void setBorrowType(Integer borrowType) {
        this.borrowType = borrowType;
    }


    public Long getVerifyUser() {
        return verifyUser;
    }


    public void setVerifyUser(Long verifyUser) {
        this.verifyUser = verifyUser;
    }


    public Long getVerifyTime() {
        return verifyTime;
    }


    public void setVerifyTime(Long verifyTime) {
        this.verifyTime = verifyTime;
    }


    public String getVerifyRemark() {
        return verifyRemark;
    }


    public void setVerifyRemark(String verifyRemark) {
        this.verifyRemark = verifyRemark;
    }


    public Double getRepaymentAccount() {
        return repaymentAccount;
    }


    public void setRepaymentAccount(Double repaymentAccount) {
        this.repaymentAccount = repaymentAccount;
    }


    public Double getRepaymentYesAccount() {
        return repaymentYesAccount;
    }


    public void setRepaymentYesAccount(Double repaymentYesAccount) {
        this.repaymentYesAccount = repaymentYesAccount;
    }


    public Double getRepaymentYesInterest() {
        return repaymentYesInterest;
    }


    public void setRepaymentYesInterest(Double repaymentYesInterest) {
        this.repaymentYesInterest = repaymentYesInterest;
    }

    
    public Integer getBorrowUse() {
        return borrowUse;
    }


    public void setBorrowUse(Integer borrowUse) {
        this.borrowUse = borrowUse;
    }


    public Integer getTimeLimit() {
        return timeLimit;
    }


    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }


    public Integer getRepaymentStyle() {
        return repaymentStyle;
    }


    public void setRepaymentStyle(Integer repaymentStyle) {
        this.repaymentStyle = repaymentStyle;
    }


    public Double getAccount() {
        return account;
    }


    public void setAccount(Double account) {
        this.account = account;
    }


    public Double getAccountYes() {
        return accountYes;
    }


    public void setAccountYes(Double accountYes) {
        this.accountYes = accountYes;
    }


    public Double getApr() {
        return apr;
    }


    public void setApr(Double apr) {
        this.apr = apr;
    }


    public Double getLowestAccount() {
        return lowestAccount;
    }


    public void setLowestAccount(Double lowestAccount) {
        this.lowestAccount = lowestAccount;
    }


    public Double getMostAccount() {
        return mostAccount;
    }


    public void setMostAccount(Double mostAccount) {
        this.mostAccount = mostAccount;
    }


    public Integer getValidTime() {
        return validTime;
    }


    public void setValidTime(Integer validTime) {
        this.validTime = validTime;
    }


    public Integer getAward() {
        return award;
    }


    public void setAward(Integer award) {
        this.award = award;
    }


    public Double getPartAccount() {
        return partAccount;
    }


    public void setPartAccount(Double partAccount) {
        this.partAccount = partAccount;
    }


    public Double getFunds() {
        return funds;
    }


    public void setFunds(Double funds) {
        this.funds = funds;
    }


    public String getOpenAccount() {
        return openAccount;
    }


    public void setOpenAccount(String openAccount) {
        this.openAccount = openAccount;
    }


    public String getOpenBorrow() {
        return openBorrow;
    }


    public void setOpenBorrow(String openBorrow) {
        this.openBorrow = openBorrow;
    }


    public String getOpenTender() {
        return openTender;
    }


    public void setOpenTender(String openTender) {
        this.openTender = openTender;
    }


    public String getOpenCredit() {
        return openCredit;
    }


    public void setOpenCredit(String openCredit) {
        this.openCredit = openCredit;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public Long getAddtime() {
        return addtime;
    }


    public void setAddtime(Long addtime) {
        this.addtime = addtime;
    }


    public String getAddip() {
        return addip;
    }


    public void setAddip(String addip) {
        this.addip = addip;
    }


    public String getPwd() {
        return pwd;
    }


    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    public Integer getIsDay() {
        return isDay;
    }


    public void setIsDay(Integer isDay) {
        this.isDay = isDay;
    }


    public String getTrustLevel() {
        return trustLevel;
    }


    public void setTrustLevel(String trustLevel) {
        this.trustLevel = trustLevel;
    }


    public String getBorrowFeeApr() {
        return borrowFeeApr;
    }


    public void setBorrowFeeApr(String borrowFeeApr) {
        this.borrowFeeApr = borrowFeeApr;
    }


    public Integer getVersion() {
        return version;
    }


    public void setVersion(Integer version) {
        this.version = version;
    }


    public Long getBorrowerUserId() {
        return borrowerUserId;
    }

    public void setBorrowerUserId(Long borrowerUserId) {
        this.borrowerUserId = borrowerUserId;
    }


    public String getUpdateip() {
        return updateip;
    }


    public void setUpdateip(String updateip) {
        this.updateip = updateip;
    }


    public Long getUpdatetime() {
        return updatetime;
    }


    public void setUpdatetime(Long updatetime) {
        this.updatetime = updatetime;
    }


    public double getScale() {

        double accyes = 0;
        
        if(getAccountYes()!=null){
            
            accyes = getAccountYes()/getAccount();
            
        }else{
            
            accyes = 0;
        }
        
        return NumberUtil.ceil(accyes, 2)*100;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

	public Double getBorrow_fee() {
		return borrow_fee;
	}

	public void setBorrow_fee(Double borrow_fee) {
		this.borrow_fee = borrow_fee;
	}

	public String getMortgagor() {
		return mortgagor;
	}

	public void setMortgagor(String mortgagor) {
		this.mortgagor = mortgagor;
	}

	public Long getReceivePerson() {
		return receivePerson;
	}

	public void setReceivePerson(Long receivePerson) {
		this.receivePerson = receivePerson;
	}

	public Long getRepayPerson() {
		return repayPerson;
	}

	public void setRepayPerson(Long repayPerson) {
		this.repayPerson = repayPerson;
	}

	public Double getMarketFee() {
		return marketFee;
	}

	public void setMarketFee(Double marketFee) {
		this.marketFee = marketFee;
	}
}
