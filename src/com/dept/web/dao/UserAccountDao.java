package com.dept.web.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.UserAccount;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class UserAccountDao extends IbatisBaseDaoImpl<UserAccount, Long>{

	public static final String NAME_SPACE_ACCOUNT = "UserAccount";

	public UserAccount queryByUserId(long userId) {
		return getObj(NAME_SPACE_ACCOUNT, userId, "USERID");
	}
	
	/**
	 * 创建UserAccount账户
	 * @param amount
	 */
	public void insertUserAmount(UserAccount userAccount) {
		save(userAccount);
	}

	
	public int updateUserAcount(Map<String,Object> userAccountMap){
		return update(NAME_SPACE_ACCOUNT,userAccountMap,"USERACCOUNT");
	}
	/**
	 * 修改UserAccount传入userAccount对象
	 * @param userAccount
	 * @return
	 */
	public int updateUserAccount(UserAccount userAccount) {
		return update(NAME_SPACE_ACCOUNT, userAccount, "USERACCOUNT2");
	}
	
	
	public void updateAccount(double totalVar,double useVar,double nouseVar,long user_id){
		//String sql="update account set total=total"+totalVar+",use_money=use_money+"+useVar+",no_use_money=no_use_money+"+nouseVar+" where user_id="+user_id;
		UserAccount a = new UserAccount();
		a.setMoneyTotal(totalVar);
		a.setMoneyUsable(useVar);
		a.setMoneyTenderFreeze(nouseVar);
		a.setUserId(user_id);
		update(NAME_SPACE_ACCOUNT, a, "UPDATE_ACCOUNT_BY_USERID");
	}
	
	
	public int updateAccountNotZero(double totalVar, double useVar,double nouseVar, long user_id) {
		//String sql="update account set total=total+"+totalVar+",use_money=use_money+"+useVar+",no_use_money=no_use_money+"+nouseVar+" where user_id="+user_id+" and use_money+"+useVar+">=0";
		UserAccount a = new UserAccount();
		a.setMoneyTotal(totalVar);
		a.setMoneyUsable(useVar);
		a.setMoneyTenderFreeze(nouseVar);
		a.setUserId(user_id);
		update(NAME_SPACE_ACCOUNT, a, "UPDATE_ACCOUNT_NOT_ZERO");
		return 1;
	}
}
