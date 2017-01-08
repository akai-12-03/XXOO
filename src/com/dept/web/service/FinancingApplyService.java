package com.dept.web.service;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dept.web.dao.FinancingApplyDao;
import com.dept.web.dao.model.FinancingApply;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
public class FinancingApplyService {

	@Autowired
	private FinancingApplyDao financingApplyDao;


	public FinancingApply queryById(long id){
	    return financingApplyDao.queryById(id);
	}
	public int updateFinancingApplyById(FinancingApply fa){
	    return financingApplyDao.updateFinancingApplyById(fa);
	}
	
	public Long insertFinancingApply(FinancingApply fa){
		return financingApplyDao.insertFinancingApply(fa);
	}
	
	public FinancingApply queryBySeting(){
		return financingApplyDao.queryBySeting();
	}
	
	/**
     * 查询标
     * @Title: queryBorrowPage 
     * @Description: TODO
     * @param @param pageRequest
     * @param @return 设定文件 
     * @return Page<Borrow> 返回类型 
     * @throws
     */
    public Page<FinancingApply> queryFinancingApplyPage(PageRequest<Map<String, String>> pageRequest){
        
        return financingApplyDao.queryFinancingApplyPage(pageRequest);
    }
}
