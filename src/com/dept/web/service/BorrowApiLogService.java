package com.dept.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dept.web.dao.BorrowApiLogDao;
import com.dept.web.dao.model.BorrowApiLog;

@Service
public class BorrowApiLogService {

	@Autowired
	private BorrowApiLogDao borrowApiLogDao;
	
	public BorrowApiLog getBorrowApiLogById(long id) {
		return borrowApiLogDao.getBorrowApiLogById(id);
	}
	
	public Long insertBorrowApiLog(BorrowApiLog borrowApiLog){
		return borrowApiLogDao.insertBorrowApiLog(borrowApiLog);
	}
	
	public void updateBorrowApiLog(long id){
		borrowApiLogDao.updateBorrowApiLog(id);
	}
}
