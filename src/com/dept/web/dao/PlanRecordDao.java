package com.dept.web.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.PlanRecord;
import com.dept.web.general.util.NewDateUtils;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class PlanRecordDao extends IbatisBaseDaoImpl<PlanRecord, Long>{

	public static final String NAME_SPACE_PLANRECORD = "PlanRecord";
	/**
	 * 新增配资记录
	 * @param record
	 * @return
	 */
	public Long addPlanRecord(PlanRecord record){
		return (Long) save(record);
	}
	/**
	 * 根据ID查找配资记录
	 * @param id
	 * @return
	 */
	public PlanRecord getPlanRecordById(long id){
		return getObj(NAME_SPACE_PLANRECORD, id, "ID");
	}
	/**
	 * 分页查询配资记录列表
	 * @param map
	 * @return
	 */
	public Page<PlanRecord> getPageRecordList(PageRequest<Map<String, String>> map){
		return pageQuery(NAME_SPACE_PLANRECORD, map, "PAGE_LIST");
	}
	
	/**
	 * 更新配资记录状态
	 * @Title: updateRecordStatus 
	 * @Description: TODO
	 * @param @param status
	 * @param @param id
	 * @param @return 设定文件 
	 * @return int 返回类型 
	 * @throws
	 */
	public int updateRecordStatus(int status, long id){
	    
	    Map<String, Object> params = new HashMap<String, Object>();
	    
	    params.put("status", status);
	    params.put("id", id);
	    params.put("operatedAt", NewDateUtils.getNowTimeStr());
	    
	    return update(NAME_SPACE_PLANRECORD, params, "STATUS_BY_ID");
	    
	}

	 /**
     * 获取用户的所有配资记录
     * @param userId
     * @return
     */
    public List<PlanRecord> getPlanRecordListByUserId(long userId){
        return getObjList(NAME_SPACE_PLANRECORD, userId, "QUERYPLANRECORD_LIST_BY_USERID");
    }
    
    /**
     * 
     * @Title: getFreeRecordByDate 
     * @Description: TODO
     * @param @param startTime
     * @param @param endTime
     * @param @return 设定文件 
     * @return List<PlanRecord> 返回类型 
     * @throws
     */
   public List<PlanRecord> getFreeRecordByDate(long startTime, long endTime){
       Map<String, String> params = new HashMap<String, String>();
       params.put("startTime", String.valueOf(startTime));
       params.put("endTime", String.valueOf(endTime));
       return getObjList(NAME_SPACE_PLANRECORD, params, "DATE_FOR_FREEPLAN");
   }    
   
   /**
    * 获取用户参与的免费体验
    * @Title: getPlanRecordListByUserId 
    * @Description: TODO
    * @param @param userId
    * @param @return 设定文件 
    * @return List<PlanRecord> 返回类型 
    * @throws
    */
   public List<PlanRecord> getFreePlanByUser(long userId){
       return getObjList(NAME_SPACE_PLANRECORD, userId, "USER_FOR_FREEPLAN");
   }
   /**
    * 根据ID删除配资记录（将status修改为-1）
    * @param prid
    * @return
    */
   public long deletePlanRecordLog(int prid){
	   return update(NAME_SPACE_PLANRECORD, prid, "PRID");
   }
}
