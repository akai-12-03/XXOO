package com.dept.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.BankDao;
import com.dept.web.dao.model.Bank;

@Service
@Transactional(rollbackFor=Exception.class)
public class BankService {
	@Autowired
	private BankDao bankDao;
	
	/**
	 * 查询出所有的银行
	 * @return
	 */
	public List<Bank> queryAllBank() {
		return bankDao.queryAllBank();
	}
	
	public Bank queryAllBank(Long id) {
		Bank b=null;
		try {
			b=bankDao.queryAllBank(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
}
