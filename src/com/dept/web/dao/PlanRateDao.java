package com.dept.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.PlanRate;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class PlanRateDao extends IbatisBaseDaoImpl<PlanRate, Long>{

	public static final String NAME_SPACE_USER = "PlanRate";
	/**
	 * 按天查询所有配资信息
	 * @param type
	 * @return
	 */
	public List<PlanRate> queryPlanRateByType(int type){
		return getObjList(NAME_SPACE_USER, type, "QUERYPLANRATE_BY_TYPE");
	}
}
