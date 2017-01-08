package com.dept.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.UserAccountLogDao;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
@Transactional(rollbackFor=Exception.class)
public class UserAccountLogService {

	@Autowired
	private UserAccountLogDao accountLogDao;
	
	
	@Transactional(propagation = Propagation.MANDATORY)
	public void addUserAccountLog(UserAccountLog accountLog){
		 accountLogDao.save(accountLog);
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
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
		accountLogDao.save(accountLog);
	}
	
	/**
	 * 通过用户id查询该用户的账户记录
	 * @param userId
	 * @return
	 */
	public List<UserAccountLog> queryByUserId(Long userId) {
		return accountLogDao.queryByUserId(userId);
	}
	
	/**
	 * 添加
	 * @param accountLog
	 * @return
	 */
	public Object insertUserAccountLog(UserAccountLog accountLog){
		
		return accountLogDao.insertUserAccountLog(accountLog);
	}

	/**
	 * 查询用户的所有资金记录
	 * @param pageRequest
	 * @return
	 */
	public Page<UserAccountLog> queryUserAccountLogByUserId(PageRequest<Map<String, String>> map) {
		return accountLogDao.queryUserAccountLogByUserId(map);
	}

	/**
	 * 查询我推荐的用户中有投资记录的人数
	 * @param userId
	 * @return
	 */
	public int getAccountLogCountByUserId(int userId){
		List<UserAccountLog> list=new ArrayList<UserAccountLog>();
		list=accountLogDao.getAccountLogCountByUserId(userId);
		if(null!=list&&list.size()>0){
			return list.size();
		}else{
			return 0;
		}
	}
	
	public double queryByTypeAndUserId(Map<String,Object> filterMap1){
		return accountLogDao.queryByTypeAndUserId(filterMap1);
	}
	
}
