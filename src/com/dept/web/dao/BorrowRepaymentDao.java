package com.dept.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.BorrowRepayment;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class BorrowRepaymentDao extends IbatisBaseDaoImpl<BorrowRepayment, Long>{

	public static final String NAME_SPACE_BORROW_REPAYMENT = "BorrowRepayment";

	public BorrowRepayment getByRepayment(BorrowRepayment repayment) {
		return getObj(NAME_SPACE_BORROW_REPAYMENT, repayment, "BORROWREPAYMENT");
	}

	public List<BorrowRepayment> getBorrowRepaymentList(
			Map<String, Object> filterMap) {
		return getObjList(NAME_SPACE_BORROW_REPAYMENT, filterMap, "BORROWREPAYMENT_BORROW");
	}
	
	public Page<BorrowRepayment> getUserCenterRepaymentdetailByborrowId(PageRequest<Map<String, String>> pageRequest){
		return pageQuery(NAME_SPACE_BORROW_REPAYMENT, pageRequest, "SEARCH_USERCENTER_REPAYMENT_DETAIL_LIST_BORROWID");
	}

	public Page<BorrowRepayment> getUserCenterRepaymentdetail(PageRequest<Map<String, String>> pageRequest){
		return pageQuery(NAME_SPACE_BORROW_REPAYMENT, pageRequest, "SEARCH_USERCENTER_REPAYMENT_DETAIL_LIST");
	}


	public int getBorrowRepaymentListCount(Map<String, Object> filterMap) {
		return getSelectCount(NAME_SPACE_BORROW_REPAYMENT, filterMap, "BORROWREPAYMENT_BORROW");
	}

	public int updateRepayment(BorrowRepayment repayment) {
		
		return update(NAME_SPACE_BORROW_REPAYMENT, repayment, "BORROWREPAYMENT");
	}
	
	public BorrowRepayment getRepayment(long repay_id) {
		//String sql="select * from borrow_repayment where id="+repay_id;
		 BorrowRepayment r= new BorrowRepayment();
		 r.setId(repay_id);
		return getObj(NAME_SPACE_BORROW_REPAYMENT, r, "FIND_REPAYMENT_BY_ID");
	}

	
	public Boolean hasRepaymentAhead(int order,long borrowId) {
		BorrowRepayment repay = new BorrowRepayment();
		repay.setRepOrder(order);
		repay.setBorrowId(borrowId);
		//select * from qr_db_borrow_repayment where `order`<? and borrow_id=? and status=0"
		List list =	getObjList(NAME_SPACE_BORROW_REPAYMENT, repay, "HAS_REPAYMENT_AHEAD");
		
		if(list==null||list.size()<1){
			return false;
		}else{
			return true;
		}
	}
	
	
	public int modifyRepaymentStatusWithCheck(long id, int status, int webstatus) {
		//String sql="update qr_db_borrow_repayment set status=?,webstatus=? where id=? and webstatus=0";
		BorrowRepayment repay = new BorrowRepayment();
		repay.setId(id);
		repay.setStatus(status);
		repay.setWebstatus(webstatus);
		int count = update(NAME_SPACE_BORROW_REPAYMENT, repay, "MODIFY_REPAYMENT_STATUS_WITH_CHECK");
		return count;
	}
	
	public List<BorrowRepayment> getRepaymentByBorrowId(long borrowId, int status)
	{
		BorrowRepayment repay = new BorrowRepayment();
		repay.setStatus(status);
		repay.setBorrowId(borrowId);
		//select * from qr_db_borrow_repayment where `order`<? and borrow_id=? and status=0"
		List<BorrowRepayment> list =	getObjList(NAME_SPACE_BORROW_REPAYMENT, repay, "REPAYMENT_BORROWID");
		return list;
	}
}
