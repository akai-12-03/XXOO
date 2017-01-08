package com.dept.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.PlanAppendInsureDao;
import com.dept.web.dao.PlanRateDao;
import com.dept.web.dao.PlanRecordDao;
import com.dept.web.dao.model.PlanAppendInsure;
import com.dept.web.dao.model.PlanRate;
import com.dept.web.dao.model.PlanRecord;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
@Transactional(rollbackFor=Exception.class)
public class PlanService {

	@Autowired
	private PlanRateDao planRateDao;
	
	@Autowired
	private PlanRecordDao planRecordDao;
	
	@Autowired
	private PlanAppendInsureDao planAppendInsureDao;
	
	/**
	 * 按天查询所有的配资信息
	 * @param type
	 * @return
	 */
	public List<PlanRate> queryPlanRateByType(int type){
		return planRateDao.queryPlanRateByType(type);
	}
	
	/**
	 * 创建新配资申请
	 * @Title: createPlanRate 
	 * @Description: TODO
	 * @param @param pr
	 * @param @return 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	public Long createPlanRate(PlanRate pr){
	    
	    return (Long) planRateDao.save(pr);
	}
	
	/**
	 * 创建配资记录
	 * @Title: createPlanRecord 
	 * @Description: TODO
	 * @param @param record
	 * @param @return 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	public Long createPlanRecord(PlanRecord record){
	    
	    return (Long) planRecordDao.save(record);
	}
	
	
	/**
	 * ID查找配资记录
	 * @Title: queryRecordById 
	 * @Description: TODO
	 * @param @param pid
	 * @param @return 设定文件 
	 * @return PlanRecord 返回类型 
	 * @throws
	 */
	public PlanRecord queryRecordById(long pid){
	    
	    PlanRecord pr = new PlanRecord();
	    
	    pr.setId(pid);
	    
	    return planRecordDao.getObj(pr);
	    
	}
	
	/**
	 * 查找用户的配资记录
	 * @Title: queryPlanRecordByUser 
	 * @Description: TODO
	 * @param @param userid
	 * @param @return 设定文件 
	 * @return List<PlanRecord> 返回类型 
	 * @throws
	 */
	public List<PlanRecord> queryPlanRecordByUser(long userid){
	    
	    return planRecordDao.getPlanRecordListByUserId(userid);
	}
	
	/**
	 * 
	 * @Title: queryFreeRecordByDate 
	 * @Description: TODO
	 * @param @param startTime
	 * @param @param endTime
	 * @param @return 设定文件 
	 * @return List<PlanRecord> 返回类型 
	 * @throws
	 */
    public List<PlanRecord> queryFreeRecordByDate(long startTime, long endTime){
        
        return planRecordDao.getFreeRecordByDate(startTime, endTime);
    }
    
    /**
     * 用户的免费体验
     * @Title: queryPlanRecordByUser 
     * @Description: TODO
     * @param @param userid
     * @param @return 设定文件 
     * @return List<PlanRecord> 返回类型 
     * @throws
     */
    public List<PlanRecord> getFreePlanByUser(long userid){
        
        return planRecordDao.getFreePlanByUser(userid);
    }
    
    /**
     * 追加保证金
     * @Title: appendInsureByPlan 
     * @Description: TODO
     * @param @param pai
     * @param @return 设定文件 
     * @return Long 返回类型 
     * @throws
     */
    public Long appendInsureByPlan(PlanAppendInsure pai){
        
        return (Long) planAppendInsureDao.save(pai);
    } 
    
    
    /**
     * 查询追加保证金记录列表
     * @Title: getPlanAppendInsurePage 
     * @Description: TODO
     * @param @param map
     * @param @return 设定文件 
     * @return Page<PlanAppendInsure> 返回类型 
     * @throws
     */
    public Page<PlanAppendInsure> getPlanAppendInsurePage(PageRequest<Map<String, String>> map){
        
        return planAppendInsureDao.getPlanAppendInsurePage(map);
    }
    /**
     * 根据配资记录ID删除 配资记录（将status修改为-1）
     * @param prid
     * @return
     */
    public Long deletePlanRecordLog(int prid){
    	return planRecordDao.deletePlanRecordLog(prid);
    }
}
