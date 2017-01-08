package com.dept.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.UserCaptchaDao;
import com.dept.web.dao.model.UserCaptcha;

@Service
@Transactional(rollbackFor=Exception.class)
public class UserCaptchaService {
 
	@Autowired
	private UserCaptchaDao userCaptchaDao;
	/**
	 * 新增验证码信息
	 * @param captcha
	 */
	public void createNewUserCaptcha(UserCaptcha captcha){
		userCaptchaDao.insertUser(captcha);
	}
}
