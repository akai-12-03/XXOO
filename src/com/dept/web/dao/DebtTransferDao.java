package com.dept.web.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.DebtTransfer;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class DebtTransferDao extends IbatisBaseDaoImpl<DebtTransfer, Long>{

	public static final String NAME_SPACE_DEBTTRANSFER = "DebtTransfer";

	public DebtTransfer getDebtTransferByid(long id){
		return getObj(NAME_SPACE_DEBTTRANSFER, id, "ID");
	}
	
	 public Page<DebtTransfer> searchDebtTransfer(PageRequest<Map<String, String>> pageRequest){
	        
	        return pageQuery(NAME_SPACE_DEBTTRANSFER, pageRequest, "SEARCH");
	    }
}
