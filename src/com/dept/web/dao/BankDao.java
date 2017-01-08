package com.dept.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Bank;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class BankDao extends IbatisBaseDaoImpl<Bank, Long>{

	public static final String NAME_SPACE_USER = "Bank";
	
	/**
	 * 查询出所有的银行
	 * @return
	 */
	public List<Bank> queryAllBank() {
		return getObjList(NAME_SPACE_USER, null, "QUERYALLBAK");
	}
	
	public Bank queryAllBank(Long id) {
		return getObj(NAME_SPACE_USER, id, "QUERYALLBAKBANKNAME");
	}
}
