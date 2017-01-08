package com.dept.web.dao;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Borrow;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class BorrowDao extends IbatisBaseDaoImpl<Borrow, Long> {

	private static final String NAME_SPACE_BORROW = "Borrow";

	public Borrow getByBorrow(Borrow borrow) {
		return getObj(NAME_SPACE_BORROW, borrow, "BORROW");
	}

	public List<Borrow> getBorrowList(Map<String, Object> map) {
		return getObjList(NAME_SPACE_BORROW, map, "ZXTZ_BORROW");
	}
	
	public Integer getBorrowListCount(Map<String, Object> filterMap) {
		return getSelectCount(NAME_SPACE_BORROW, filterMap, "BORROW");
	}

	public Borrow getBorrowById(long id) {
		Borrow borrow = new Borrow();
		borrow.setId(id);
		borrow=	 getObj(NAME_SPACE_BORROW, borrow, "BORROWBY_ID");
		return borrow;
	}
	
	/**
	 * 投标更新标的信息
	 * @param borrow
	 * @return
	 */
	public int updateBorrowTenderMoney(Borrow borrow) {
		
		return update(NAME_SPACE_BORROW, borrow, "BORROW_FOR_TENDER");
	}

	/**
	 * 
	 * @Description:  查询首页显示的借款标
	 * @param:        @param filters
	 * @param:        @return   
	 * @return:       List<Borrow>   
	 * @throws
	 */
	public List<Borrow> queryIndexDisplayList(Map<String, Object> filters) {
		
		//当前时间
		 Calendar cal1=Calendar.getInstance();
		 long datesub = cal1.getTimeInMillis();
		 filters.put("datesub", datesub);
		
		return getObjList(NAME_SPACE_BORROW, filters, "INDEX");
	}
	
	public Page<Borrow> searchBorrow(PageRequest<Map<String, String>> pageRequest){
			
			return pageQuery(NAME_SPACE_BORROW, pageRequest, "SEARCH");
		}
	
	public Borrow getAprs(){
		return getObj(NAME_SPACE_BORROW, null, "APRS");
	}
	
	
	public List<Borrow> getBorrowAddtime(){
		return getObjList(NAME_SPACE_BORROW, null, "ZGADDTIME");
	}
	
	public List<Borrow> getBorrowApr(){
		return getObjList(NAME_SPACE_BORROW, null, "ZGAPR");
	}
	public List<Borrow> getBorrowAccount(){
		return getObjList(NAME_SPACE_BORROW, null, "ZGACCOUNT");
	}
	
	public int updateBorrow(double account,int status,long id) {
		//String sql="update borrow set tender_times=tender_times+1,account_yes=account_yes+"+account+",status="+status+" " +
		//		"where id="+id+" and account_yes+"+account+"<=account+0";
		Borrow b = new Borrow();
		b.setAccount(Double.valueOf(account));
		b.setStatus(status);
		b.setId(id);
		update(NAME_SPACE_BORROW, b, "BORROWS");
		return 1;
		
	}
	
	public List<Borrow> queryblist(Map<String, Object> filters) {
		 return	getObjList(NAME_SPACE_BORROW, filters, "GETBORROW");
	}
	public List<Borrow> getBorrowListByborrowTypeForIndexStatus(Borrow b) {
		 return	getObjList(NAME_SPACE_BORROW, b, "BORROWLIST_BORROWTYPE_INDEXSTATUS");
	}

	/**
	 * 获取最新列表
	 * @param pageRequest
	 * @return
	 */
	public Page<Borrow> getsearchBorrow(PageRequest<Map<String, String>> pageRequest){
		
		return pageQuery(NAME_SPACE_BORROW, pageRequest, "GETLISTSEARCH");
	}
	
	
	public List<Borrow> getIndexBorrowList(int limit){
		return getObjList(NAME_SPACE_BORROW,limit,"GETINDEXBORROWLIST");
	}
	
	public Borrow queryForLoan(){
		return getObj(NAME_SPACE_BORROW, null, "BORROWFORLOAN");
	}
	
	public List<Borrow> getBorrowListBorrowIndex(Borrow b) {
		 return	getObjList(NAME_SPACE_BORROW, b, "BORROWINDEX");
	}
}
