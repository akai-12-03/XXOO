package com.dept.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.CityDao;
import com.dept.web.dao.model.City;

@Service
@Transactional(rollbackFor=Exception.class)
public class CityService {
	@Autowired
	private CityDao cityDao;
	
	public List<City> queryCityListByParent(Long parent){
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("parentId", parent);
		return cityDao.queryCityListByParent(filters);
	}
}
