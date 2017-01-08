package com.dept.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.UserRechargeDao;
import com.dept.web.dao.model.UserRecharge;
@Service
@Transactional(rollbackFor=Exception.class)
public class UserRechargeService {

	
	@Autowired
	private UserRechargeDao rechargeDao;
	
	public void addCharge(UserRecharge recharge){
		
		rechargeDao.add(recharge);
		
	}

	public UserRecharge getRechargeByOrderNo(String string) {
		UserRecharge recharge = new UserRecharge();
		recharge.setOrderId(string);
		return getRechargeByRecharge(recharge);
	}
	
	public UserRecharge getRechargeByRecharge(UserRecharge recharge){
		
		return rechargeDao.getByRecharge(recharge);
	}

	public int updateRecharge(UserRecharge userRecharge) {
		return rechargeDao.updateByOrderId(userRecharge);
	}
	
	
}
