package com.dept.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.PlanRecordDao;
import com.dept.web.dao.model.PlanRecord;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
@Transactional(rollbackFor=Exception.class)
public class PlanRecordService {

	@Autowired
	private PlanRecordDao planRecordDao;
	/**
	 * 新增配资记录
	 * @param record
	 * @return
	 */
	public PlanRecord addPlanRecord(PlanRecord record){
		long planId=planRecordDao.addPlanRecord(record);
		record=planRecordDao.getPlanRecordById(planId);
		return record;
	}
	/**
	 * 分页查询配资记录列表
	 * @param map
	 * @return
	 */
	public Page<PlanRecord> getPageRecordList(PageRequest<Map<String, String>> map){
		return planRecordDao.getPageRecordList(map);
	}
    /**
     * 查询用户的所有配资记录
     * @param userId
     * @return
     */
    public List<PlanRecord> getPlanRecordListByUserId(long userId){
        return planRecordDao.getPlanRecordListByUserId(userId);
    }	
	
	/**
	 * 更新配资记录
	 * @Title: updateRecordStatus 
	 * @Description: TODO
	 * @param @param status
	 * @param @param id
	 * @param @return 设定文件 
	 * @return int 返回类型 
	 * @throws
	 */
	public int updateRecordStatus(int status, long id){
	    
	    return planRecordDao.updateRecordStatus(status, id);
	}
}
