package com.dept.web.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.BorrowTender;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class BorrowTenderDao extends IbatisBaseDaoImpl<BorrowTender, Long> {

	private static final String NAME_SPACE_BORROWTENDER = "BorrowTender";

	public List<BorrowTender> getBorrowTenderListByBorrowId(Long borrowId) {
		Map<String, Long> obj = new HashMap<String, Long>();
		obj.put("bid", borrowId);
		return getObjList(NAME_SPACE_BORROWTENDER, obj, "BORROW");
	}
	
	public List<BorrowTender> getBorrowTenderList() {
		return getObjList(NAME_SPACE_BORROWTENDER, "", "TENDERLIST");
	}
	

	public long addBorrowTender(BorrowTender borrowTender) {

		return (Long) save(NAME_SPACE_BORROWTENDER, borrowTender, "BORROWTENDER");
	}

	public List<BorrowTender> getBorrowTenderList(Map<String, Object> filterMap) {
		return getObjList(NAME_SPACE_BORROWTENDER, filterMap, "BORROWTENDER_BORROW");
	}

	public int getBorrowTenderListCount(Map<String, Object> filterMap) {
		return getSelectCount(NAME_SPACE_BORROWTENDER, filterMap, "BORROWTENDER_BORROW");
	}
	
	public int getTenderList(Long id) {
		return getSelectCount(NAME_SPACE_BORROWTENDER, id, "GETBORROWTENDER_COUNT");
	}
	public Page<BorrowTender> getInvestTenderListByUserid(PageRequest<Map<String, String>> pageRequest){
		return pageQuery(NAME_SPACE_BORROWTENDER, pageRequest, "SEARCH_INVEST_TENDER_LIST_BY_USERID");
	}
	public Page<BorrowTender> getInvestTenderingListByUserid(PageRequest<Map<String, String>> pageRequest){
		return pageQuery(NAME_SPACE_BORROWTENDER, pageRequest, "SEARCH_INVEST_TENDERING_LIST_BY_USERID");
	}
	public Page<BorrowTender> getInvestTenderingListByUserid1(PageRequest<Map<String, String>> pageRequest){
		return pageQuery(NAME_SPACE_BORROWTENDER, pageRequest, "SEARCH_INVEST_TENDERING_LIST_BY_USERID1");
	}
	public Page<BorrowTender> getSuccessListByUserid(PageRequest<Map<String, String>> pageRequest){
		return pageQuery(NAME_SPACE_BORROWTENDER, pageRequest, "SEARCH_SUCCESS_LIST_BY_USERID");
	}
	
	/**
	 * 最新投资记录
	 * @param id
	 * @return
	 */
	public List<BorrowTender> getTender() {
		return getObjList(NAME_SPACE_BORROWTENDER, null, "GETBORROWTENDER_LIST");
	}
	
	
	public BorrowTender modifyTender(BorrowTender borrowTender) {
		update(NAME_SPACE_BORROWTENDER, borrowTender, "MODIFY_TENDER");
		return getObj(NAME_SPACE_BORROWTENDER, borrowTender, "GETTENDERBY_ID");
	}
	
	
	/**
	 * 
	 * @Description:  查询当前标用户的总投资金额
	 * @param:        @param borrow_id
	 * @param:        @param user_id
	 * @param:        @return   
	 * @return:       double   
	 * @throws
	 */
	public double hasTenderTotalPerBorrowByUserid(long borrow_id,long user_id) {
		BorrowTender bt = new BorrowTender();
		bt.setBorrowId(borrow_id);
		bt.setUserId(user_id);
		bt = getObj(NAME_SPACE_BORROWTENDER, bt, "TENDER_TOTAL_PERBORROW_BY_USERID");
		if(bt.getAccount()==null){
			return 0;
		}else{
			return Double.valueOf(bt.getAccount());	
		}
		
	}	
	/**
	 * 查询当前用户的总投资收益
	 */
	public double getInterestByBorrowTenderForUserId(Long userId){
		Double b;
		 b=(Double)getObjs(NAME_SPACE_BORROWTENDER, userId, "INTEREST_BY_BORROWTENDER_FOR_USERID");
		 if(b==null){
			 return 0;
		 }
		 return b;
	}
	
	public int queryTenderCountByUserId(long userId){
		Object o=getObj(NAME_SPACE_BORROWTENDER, userId, "GETTENDERCOUNT");
		if(o!=null){
			return Integer.valueOf(o.toString());
		}else{
			return 0;
		}
	}
	
	public BorrowTender getTenderById(long tender_id) {
		BorrowTender t = new BorrowTender();
		t.setId(tender_id);
		return getObj(NAME_SPACE_BORROWTENDER, t, "GETTENDERBY_ID");
	}

	public BorrowTender getBorrowTenderById(long id){
		return getObj(NAME_SPACE_BORROWTENDER, id, "ID");
	}
	
	public void udpateborrowtenderbyId(long userId,long id,int transfer){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("id", id);
		map.put("transfer", transfer);
		update(NAME_SPACE_BORROWTENDER, map, "BY_USERID");
	}
	
	public void updateBorrowTender(BorrowTender borrowTender)
	{
		update(borrowTender);
	}
}	

