package com.dept.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.Financing;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class FinancingDao extends IbatisBaseDaoImpl<Financing, Long>{
	public static final String NAME_SPACE_FINANCING= "Financing";

	public Financing queryById(long id){
	    return getObj(NAME_SPACE_FINANCING, id, "FINANCING_ID");
	}
	
	public List<Financing> queryFinancingList(Financing financing){
	    return getObjList(NAME_SPACE_FINANCING, financing, "FINANCING");
	}
	
	public List<Financing> queryFinancingByUserId(long  user_id){
	    return getObjList(NAME_SPACE_FINANCING, user_id, "FINANCING_USERID");
	}
	public void insertFinancing(Financing financing){
		  save(financing);
	}
	
	public  int updateFinancing(Financing financing){
		return update(financing);
	}
	public  int updateFinancingStatus(Financing financing){
		return update(NAME_SPACE_FINANCING,financing,"FINANCING_STATUS");
	}
	
	public  int updateFinancingCounts(Financing financing){
		return update(NAME_SPACE_FINANCING,financing,"FINANCING_COUNTS");
	}
	
	public Page<Financing> queryFinancingPage(PageRequest<Map<String, String>> pageRequest) {
        
        return pageQuery(NAME_SPACE_FINANCING, pageRequest, "SEARCH");
        
    }
	
	public List<Financing> queryFinancingListByIndex(Financing financing){
	    return getObjList(NAME_SPACE_FINANCING, financing, "FINANCING_INDEX");
	}
	
	
	 public Financing queryFinancingSumMessage(Financing financing){
		    return getObj(NAME_SPACE_FINANCING, financing, "FINANCING_INDEX_COUNT");
	} 
	 
	 public List<Financing> queryFinancing(){
		    return getObjList(NAME_SPACE_FINANCING, null, "FINANCINGLIST");
		}
	    
}
