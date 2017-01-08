package com.dept.web.dao;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.HongbaoLog;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class HongbaoLogDao  extends IbatisBaseDaoImpl<HongbaoLog, Long> {
	
	private static final String NAME_SPACE_HONGBAO = "HongbaoLog";
	
	public HongbaoLog queryHongbaoLogById(Long hongbaoId){
		return getObj(NAME_SPACE_HONGBAO, hongbaoId, "BYHONGBAOID");
	}
	
	public Page<HongbaoLog> queryHongbaoLogByUserId(PageRequest<Map<String, String>> map){
		return pageQuery(NAME_SPACE_HONGBAO, map, "FIND_HONGBAOLOG_BY_USERID");
	}
	/**
	 * 更新红包状态
	 * @param 
	 * @return
	 */
	public int updateHongbaoLog(HongbaoLog hongbaoLog){
		return update(NAME_SPACE_HONGBAO, hongbaoLog, "UPSTATUS");
	}
	/**
	 * 添加红包记录
	 * @param 
	 * @return
	 */
	public Long insertHonbaoLog(HongbaoLog hongbaoLog){
		
		return (Long) save(NAME_SPACE_HONGBAO, hongbaoLog, "HONGBAOLOG");
	}
	/**
	 * 根据ID查找未使用的红包总金额
	 * @param 
	 * @return
	 */
	public double getSumHongbaoLogById(Long userId){
		Object obj=getObjs(NAME_SPACE_HONGBAO, userId, "USERID");
		if(obj==null){
			return 0;
		}else{
		  return Double.parseDouble(obj.toString());
		}
	}
	public List<HongbaoLog>  queryListHongbaoLogByIDAndStatus(Map map){
  		
  		return getObjList(NAME_SPACE_HONGBAO, map, "STATUS");
  	}

	public Integer queryCountHongbaoLogByIDAndStatus(Map map){
  		
  		return getSelectCount(NAME_SPACE_HONGBAO, map, "IDSTATUS");
  	}

}
