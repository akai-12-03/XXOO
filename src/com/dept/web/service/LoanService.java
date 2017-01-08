package com.dept.web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dept.web.dao.LoanDao;
import com.dept.web.dao.model.BorrowLoan;

@Service
public class LoanService {

	@Autowired
	private LoanDao loanDao;

	public void addLoan(BorrowLoan loan){
		loanDao.insertLoan(loan);
	}
}
