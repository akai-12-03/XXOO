package com.dept.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Site;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class SiteDao extends IbatisBaseDaoImpl<Site, Long>{

	public static final String NAME_SPACE_SITE = "Site";

	/**
	 * 
	 * @Description:  查询关于我们的列表
	 * @param:        @param nid
	 * @param:        @return   
	 * @return:       List<Site>   
	 * @throws
	 */
	public List<Site> queryAboutUsList(String code){
		return getObjList(NAME_SPACE_SITE, code, "ABOUTUS");
	}

}
