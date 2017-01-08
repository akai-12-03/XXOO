package com.dept.web.dao;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Hongbao;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class HongbaoPlanDao extends IbatisBaseDaoImpl<Hongbao, Long>{

	private static final String NAME_SPACE_HONGBAOPLAN = "HongbaoPlan";
	
}
