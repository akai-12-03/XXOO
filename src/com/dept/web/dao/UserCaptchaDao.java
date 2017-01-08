package com.dept.web.dao;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.UserCaptcha;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
@Repository
public class UserCaptchaDao extends IbatisBaseDaoImpl<UserCaptcha, Long>{

	public static final String NAME_SPACE_USER = "UserCaptcha";
	/**
	 * 新增验证码信息
	 * @param captcha
	 * @return
	 */
	public Long insertUser(UserCaptcha captcha){
		return (Long) save(captcha);
	}
}
