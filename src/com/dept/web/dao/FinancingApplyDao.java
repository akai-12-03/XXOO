package com.dept.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.Financing;
import com.dept.web.dao.model.FinancingApply;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class FinancingApplyDao extends IbatisBaseDaoImpl<FinancingApply, Long>{
	
	public static final String NAME_SPACE_FINANCINGAPPLY= "FinancingApply";

	public FinancingApply queryById(long id){
	    return getObj(NAME_SPACE_FINANCINGAPPLY, id, "FINANCINGAPPLY_BY_ID");
	}
	public int updateFinancingApplyById(FinancingApply fa){
	    return update(NAME_SPACE_FINANCINGAPPLY, fa, "FINANCINGAPPLY_BY_ID");
	}
	
	public Long insertFinancingApply(FinancingApply fa){
		return (Long) save(fa);
	}
	
	
	public FinancingApply queryBySeting(){
		return getObj(NAME_SPACE_FINANCINGAPPLY, null, "QUERY_BY_SETING");
	}
public Page<FinancingApply> queryFinancingApplyPage(PageRequest<Map<String, String>> pageRequest) {
        
        return pageQuery(NAME_SPACE_FINANCINGAPPLY, pageRequest, "SEARCH");
        
    }
}
