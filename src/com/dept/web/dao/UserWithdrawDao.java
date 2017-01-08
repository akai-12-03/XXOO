package com.dept.web.dao;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.UserWithdraw;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class UserWithdrawDao extends IbatisBaseDaoImpl<UserWithdraw, Long>{
	public static final String NAME_SPACE_USERWITHDRAW= "UserWithdraw";
	
	/**
	 * 添加
	 * @param userWithdraw
	 */
	public Long insertUserWithdraw(UserWithdraw userWithdraw) {
		return (Long) save(userWithdraw);
	}
	
	/**
	 * ORDERID查找提现记录
	 * @Title: queryWithdrawById 
	 * @Description: TODO
	 * @param @param id
	 * @param @return 设定文件 
	 * @return UserWithdraw 返回类型 
	 * @throws
	 */
	public UserWithdraw queryWithdrawByOrderId(String id){
	    
	    return getObj(NAME_SPACE_USERWITHDRAW, id, "ORDER_ID");
	}

	public int qddUupdate(UserWithdraw uw){
		return update(NAME_SPACE_USERWITHDRAW, uw, "QDDUPDATE");
	}
}
