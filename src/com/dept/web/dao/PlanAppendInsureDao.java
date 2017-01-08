package com.dept.web.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.PlanAppendInsure;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class PlanAppendInsureDao extends IbatisBaseDaoImpl<PlanAppendInsure, Long>{

	public static final String NAME_SPACE_PLANAPPENDINSURE = "PlanAppendInsure";

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
        
        return pageQuery(NAME_SPACE_PLANAPPENDINSURE, map, "PAGE_LIST");
    }
	
}
