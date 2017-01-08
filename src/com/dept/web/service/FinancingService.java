package com.dept.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.context.Constant;
import com.dept.web.dao.BorrowTenderDao;
import com.dept.web.dao.FinancingDao;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.Financing;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
public class FinancingService {

	@Autowired
	private FinancingDao financingDao;

	public Financing queryById(long id){
	    return financingDao.queryById(id);
	}
	
	public List<Financing> queryFinancingList(Financing financing){
	    return financingDao.queryFinancingList(financing);
	}
	
	public List<Financing> queryFinancingByUserId(long  user_id){
	    return financingDao.queryFinancingByUserId(user_id);
	}
	public void insertFinancing(Financing financing){
		 financingDao.insertFinancing(financing);
	}
	
	public  int updateFinancing(Financing financing){
		return financingDao.updateFinancing(financing);
	}
	public  int updateFinancingStatus(Financing financing){
		return financingDao.updateFinancingStatus(financing);
	}
	
	public  int updateFinancingCounts(Financing financing){
		return financingDao.updateFinancingCounts(financing);
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
    public Page<Financing> queryFinancingPage(PageRequest<Map<String, String>> pageRequest){
        
        return financingDao.queryFinancingPage(pageRequest);
    }
    /**
     * 首页，查询三条最新的信息。
     */
    public List<Financing> queryFinancingListByIndex(Financing financing){
	    return financingDao.queryFinancingListByIndex(financing);
	} 
    /**
     * 统计参与人数，融资总额，平均利率
     * @param financing
     * @return
     */
    public Financing queryFinancingSumMessage(Financing financing){
	    return financingDao.queryFinancingSumMessage(financing);
	} 
    
    //首页最新的
    public List<Financing> queryFinancing(){
	    return financingDao.queryFinancing();
	}
    
}
