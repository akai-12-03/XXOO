package com.dept.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.UserWithdrawDao;
import com.dept.web.dao.model.UserWithdraw;
@Service
@Transactional(rollbackFor=Exception.class)
public class UserWithdrawService {
	@Autowired
	private UserWithdrawDao userWithdrawDao;
	
	/**
	 * 添加
	 * @param userWithdraw
	 */
	public Long addUserWithdraw(UserWithdraw userWithdraw) {
		return userWithdrawDao.insertUserWithdraw(userWithdraw);
	}

}
