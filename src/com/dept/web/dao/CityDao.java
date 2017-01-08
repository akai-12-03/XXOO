package com.dept.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.City;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class CityDao extends IbatisBaseDaoImpl<City, Long> {

	private static final String NAME_SPACE_CITY = "City";
	public List<City> queryCityListByParent(Map<String,Object> fiters) {
		
		return getObjList(NAME_SPACE_CITY, fiters, "SEARCH");
	}
}
