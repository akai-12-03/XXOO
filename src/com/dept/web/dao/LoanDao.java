package com.dept.web.dao;


import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.BorrowLoan;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class LoanDao  extends IbatisBaseDaoImpl<BorrowLoan, Long> {
	
	private static final String NAME_SPACE_LOAN = "Loan";
	
	/**
	 * 添加借款申请
	 * @param 
	 * @return
	 */
	public Long insertLoan(BorrowLoan loan){
		
		return (Long) save(NAME_SPACE_LOAN, loan, "LOAN");
	}
}
