package com.dept.web.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.AccountSummaryDao;
import com.dept.web.dao.HongbaoDao;
import com.dept.web.dao.UserAccountDao;
import com.dept.web.dao.UserAccountLogDao;
import com.dept.web.dao.UserRechargeDao;
import com.dept.web.dao.UserWithdrawDao;
import com.dept.web.dao.model.Hongbao;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.dao.model.UserAccountSummary;
import com.dept.web.dao.model.UserWithdraw;
import com.dept.web.general.util.TimeUtil;
import com.dept.web.general.util.tools.iphelper.IPUtils;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserAccountService {
	@Autowired
	private UserAccountDao userAccountDao;
	@Autowired
	private UserRechargeDao userRechargeDao;
	@Autowired
	private UserAccountLogDao userAccountLogDao;
	@Autowired
	private UserWithdrawDao userWithdrawDao;
	
	@Autowired
	private AccountSummaryDao accountSummaryDao ;
	
	@Autowired
	private HongbaoDao hongbaoDao ;
	
	
	
	
	public UserAccount getAccount(long user_id) {
		return userAccountDao.queryByUserId(user_id);
	}
	
	/**
	 *注册并实名之后送红包10元
	 * @param userId
	 * @throws ParseException
	 */
	public void insertHongbao(Long userId) throws ParseException{
		Hongbao hb= new Hongbao();
		hb.setUser_id(userId);
		hb.setMoney(10.00);
		hb.setStatus(0);
		hb.setType(1);
		hb.setAddtime(TimeUtil.getNowTimeStr());
		String endtime = TimeUtil.getEndTimeHMS(TimeUtil.getNowTimeStr(), 0, 15, 0, 0, 0);
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");            
        Date date = fmt.parse(endtime);
		hb.setEndtime(String.valueOf(date.getTime()/1000));
		hongbaoDao.insertHonbao(hb);
	}
	
	
	
	/**
	 * 通过用户ID拿到用户账户
	 * 
	 * @param userId
	 * @return
	 */
	public UserAccount getUserAccount(long userId) {
		return userAccountDao.queryByUserId(userId);
	}

	/**
	 * 投标扣款
	 * 
	 * @param userId
	 * @param payMoney
	 *            投标扣款金额
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.MANDATORY)
	public int borrowTenderPayment(long userId, double payMoney)
			throws RuntimeException {
		
		double moneyTotal = 0;// 总金额变动
		double moneyUsable = 0; // 可用金额
		// double moneyWithdraw = 0; // 提现中金额(冻结)
		// double moneyInsure = 0;// 配资保证金
		// double profitsTotal = 0; // 累计收益(所有历史收益)
		// double moneyCollection = 0;// 待收金额
		//投标:总资产不变,[投标冻结金额增加],[可用余额减少],
		double moneyTender_freeze = payMoney;// 投标冻结金额
		moneyUsable = moneyUsable - payMoney;// 可用金额减少

		int count = updateUserAccountMoney(userId, 0, moneyUsable, 0, 0, 0, moneyTender_freeze, 0);
		if (count != 1) {
			throw new RuntimeException("扣款失败");
		}
		return count;
	}

	/**
	 * 充值
	 * 
	 * @param userId
	 * @param rechargeMoney 充值金额
	 * @return
	 */
	@Transactional(propagation = Propagation.MANDATORY)
	public int recharge(long userId, double rechargeMoney) {
		double moneyTotal = 0;// 总金额变动
		double moneyUsable = 0; // 可用金额
		// double moneyWithdraw = 0; // 提现中金额(冻结)
		// double moneyInsure = 0;// 配资保证金
		// double profitsTotal = 0; // 累计收益(所有历史收益)
//		double moneyTender_freeze = 0;// 投标冻结金额
		// double moneyCollection = 0;// 待收金额
		
		moneyTotal = rechargeMoney;// 总金额
		moneyUsable = rechargeMoney;// 可用金额
		
		int count = updateUserAccountMoney(userId, moneyTotal, moneyUsable, 0, 0, 0, 0, 0);
		if (count != 1) {
			throw new RuntimeException("操作失败");
		}
		return count;
	}
	
	
	/**
	 * 还款(借款人)
	 * 资金冻结,可用余额减少,总金额不变
	 * @param userId
	 * @param money 还款金额
	 * @return
	 */
	@Transactional(propagation = Propagation.MANDATORY)
	public int repayment(long userId, double money){
		double moneyUsable = 0; // 可用金额
		moneyUsable = moneyUsable - money;// 可用金额减少
		double moneyTender_freeze = money;// 冻结金额
		
		int count = updateUserAccountMoney(userId, 0, moneyUsable, 0, 0, 0, moneyTender_freeze, 0);
		if (count != 1) {
			throw new RuntimeException("扣款失败");
		}
		return count;
		
	}
	

	/**
	 * 用户资金操作(投标扣款/充值/提现/还款)
	 * 
	 * @param userId
	 * @param payMoney
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.MANDATORY)
	public synchronized int updateUserAccountMoney(long userId, // 用户id
			double moneyTotal,// 总金额变动
			double moneyUsable, // 可用金额
			double moneyWithdraw, // 提现中金额(冻结)
			double moneyInsure,// 配资保证金
			double profitsTotal, // 累计收益(所有历史收益)
			double moneyTenderFreeze,// 投标冻结金额
			double moneyCollection// 待收金额
	) throws RuntimeException {
		Map<String, Object> userAccountMap = new HashMap<String, Object>();
		UserAccount account = userAccountDao.queryByUserId(userId);
		if(account == null){
			throw new RuntimeException("资金账户不存在");
		}
		
		// 总金额
		if (moneyTotal != 0) {
			if (account.getMoneyTotal() + moneyTotal < 0) {
				throw new RuntimeException("金额不足");
			} else {
				userAccountMap.put("moneyTotal", moneyTotal);
			}
		}
		// 可用金额
		if (moneyUsable != 0) {
			if (account.getMoneyUsable() + moneyUsable < 0) {
				throw new RuntimeException("金额不足");
			} else {
				userAccountMap.put("moneyUsable", moneyUsable);
			}
		}
		// 提现中金额(冻结)
		if (moneyWithdraw != 0) {
			if (account.getMoneyWithdraw() + moneyWithdraw < 0) {
				throw new RuntimeException("数据异常");
			} else {
				userAccountMap.put("moneyWithdraw", moneyWithdraw);
			}
		}
		// 配资保证金
		if (moneyInsure != 0) {
			if (account.getMoneyInsure() + moneyInsure < 0) {
				throw new RuntimeException("数据异常");
			} else {
				userAccountMap.put("moneyInsure", moneyInsure);
			}
		}
		// 累计收益(所有历史收益)
		if (profitsTotal != 0) {
			if (account.getProfitsTotal() + profitsTotal < 0) {
				throw new RuntimeException("数据异常");
			} else {
				userAccountMap.put("profitsTotal", profitsTotal);
			}
		}
		// 投标冻结金额
		if (moneyTenderFreeze != 0) {
			if (account.getMoneyTenderFreeze() + moneyTenderFreeze < 0) {
				throw new RuntimeException("数据异常");
			} else {
				userAccountMap.put("moneyTenderFreeze", moneyTenderFreeze);
			}
		}
		// 待收金额
		if (moneyCollection != 0) {
			if (account.getMoneyCollection() + moneyCollection < 0) {
				throw new RuntimeException("数据异常");
			} else {
				userAccountMap.put("moneyCollection", moneyCollection);
			}
		}
		int c = 0;
		if (!userAccountMap.isEmpty()) {
			userAccountMap.put("userId", account.getUserId());
			userAccountMap.put("id", account.getId());
			userAccountMap.put("updatedAt", System.currentTimeMillis()/1000);
			c = userAccountDao.updateUserAcount(userAccountMap);
		}

		return c;
	}
	
	/**
	 * 修改UserAccount传入userAccount对象
	 * @param userAccount
	 * @return
	 */
	public int updateUserAccount(UserAccount userAccount) {
		return userAccountDao.updateUserAccount(userAccount);
	}

	/**
	 * 查询当前userID下 所有充值记录
	 * @param userId
	 * @return
	 */
	public int getUserRechargeCount(int userId){
		return userRechargeDao.getUserRechargeCount(userId);
	}
	
	/**
	 * 创建UserAccount账户
	 * @param amount
	 */
	public void insertUserAmount(UserAccount userAccount) {
		userAccountDao.insertUserAmount(userAccount);
	}
	
	/**
	 * 提现相关操作
	 * @param request
	 * @param userAccount
	 * @param tmoney
	 */
	public synchronized void tiXianMethods(HttpServletRequest request,UserAccount userAccount,double tmoney) {
		// 修改用户账户
		this.updateUserAccount(userAccount);
		// 添加资金记录
		userAccountLogDao.insertUserAccountLog(new UserAccountLog(userAccount.getUserId(), 3, tmoney, userAccount
						.getMoneyTotal(), userAccount.getMoneyUsable(), userAccount.getMoneyWithdraw(),
				"提现冻结" + tmoney + "元",
				System.currentTimeMillis() / 1000, IPUtils
						.getRemortIP(request), userAccount
						.getMoneyInsure()));
//		// 提现表添加记录
//		userWithdrawDao.insertUserWithdraw(new UserWithdraw(tmoney,
//				"", 0, userAccount.getUserId(), System
//						.currentTimeMillis() / 1000, userAccount
//						.getUserId(),
//				System.currentTimeMillis() / 1000, IPUtils
//						.getRemortIP(request)));
	}
	
	
	/** add by cannavaro **/
	
	
	/**
	 * 
	 * @Description:  首页统计
	 * @param:        @return   
	 * @return:       UserAccountSummary   
	 * @throws
	 */
	public UserAccountSummary getForIndexCount() {
		return accountSummaryDao.getForIndexCount();
	}
	 public void addUserAccountLog(UserAccount account,Long userId,double moneyOperate,int type,String createdIp,String remark){
			UserAccountLog accountLog = new UserAccountLog();
			
			accountLog.setUserId(userId);
			
			accountLog.setMoneyOperate(moneyOperate);
			
			accountLog.setMoneyTotal(account.getMoneyTotal());
			accountLog.setMoneyCollection(account.getMoneyCollection());
			accountLog.setMoneyInsure(account.getMoneyInsure());
			accountLog.setMoneyTenderFreeze(account.getMoneyTenderFreeze());
			accountLog.setMoneyUsable(account.getMoneyUsable());
			accountLog.setMoneyWithdraw(account.getMoneyWithdraw());
			
			accountLog.setCreatedIp(createdIp);
			accountLog.setCreatedAt(System.currentTimeMillis()/1000);
			accountLog.setType(type);
			accountLog.setRemark(remark);
			userAccountLogDao.save(accountLog);
		}
	 
	    /**
	     * 
	     * @Title: queryWithdrawById 
	     * @Description: TODO
	     * @param @param id
	     * @param @return 设定文件 
	     * @return UserWithdraw 返回类型 
	     * @throws
	     */
	    public UserWithdraw queryWithdrawByOrderId(String id){
	        
	        return userWithdrawDao.queryWithdrawByOrderId(id);
	    }
	    
	    /**
	     * 审核提现
	     * @Title: verifyWithDraw 
	     * @Description: TODO
	     * @param @param uw
	     * @param @return 设定文件 
	     * @return int 返回类型 
	     * @throws
	     */
	    public int qddVerifyWithDraw(UserWithdraw uw){
	        return userWithdrawDao.qddUupdate(uw);
	    }
}
