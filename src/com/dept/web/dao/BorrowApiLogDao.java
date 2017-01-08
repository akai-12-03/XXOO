package com.dept.web.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.BorrowApiLog;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
@Repository
public class BorrowApiLogDao extends IbatisBaseDaoImpl<BorrowApiLog, Long>{
	public static final String NAME_SPACE_BORROWAPILOG = "BorrowApiLog";
	
	
	public BorrowApiLog getBorrowApiLogById(long id) {
		return getObj(NAME_SPACE_BORROWAPILOG, id, "ID");
	}
	
	public Long insertBorrowApiLog(BorrowApiLog borrowApiLog){
		return (Long)save(NAME_SPACE_BORROWAPILOG, borrowApiLog, "BORROWAPILOG");
	}
	
	public int updateBorrowApiLog(long id){
		Map<String, Object> filters = new HashMap<String,Object>();
		filters.put("id", id);
		return update(NAME_SPACE_BORROWAPILOG, filters, "BORROWAPILOG");
	}
	
}
