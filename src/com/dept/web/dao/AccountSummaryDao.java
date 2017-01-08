package com.dept.web.dao;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.UserAccountSummary;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class AccountSummaryDao extends IbatisBaseDaoImpl<UserAccountSummary, Long>{

	public static final String NAME_SPACE_ACCOUNT = "UserAccountSummary";
	
	public UserAccountSummary getForIndex(){
		
		return getObj(NAME_SPACE_ACCOUNT, null, "INDEX");
	}

	public UserAccountSummary getForIndexCount() {
		// TODO Auto-generated method stub
		return getObj(NAME_SPACE_ACCOUNT, null, "COUNT");
	}
	
	

}
