package com.dept.web.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.BorrowCollection;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class BorrowCollectionDao extends IbatisBaseDaoImpl<BorrowCollection, Long>{

    private static final String NAME_SPACE_BORROWCOLLECTION = "BorrowCollection";

	public List<BorrowCollection> getBorrowTenderList(
			Map<String, Object> filterMap) {
		return getObjList(NAME_SPACE_BORROWCOLLECTION, filterMap, "BORROWCOLLECTION_BORROW");
	}
	 public Page<BorrowCollection> getCollectionList(PageRequest<Map<String, String>> pageRequest){
			return pageQuery(NAME_SPACE_BORROWCOLLECTION, pageRequest, "SEARCH_COLLECTION_LIST");
		}

	public int getBorrowCollectionListCount(Map<String, Object> filterMap) {
		return getSelectCount(NAME_SPACE_BORROWCOLLECTION, filterMap, "BORROWCOLLECTION_BORROW");
	}
	
	
	@Override
	public Object save(BorrowCollection e) {
		return super.save(NAME_SPACE_BORROWCOLLECTION,e,NAME_SPACE_BORROWCOLLECTION);
	}
 
	
	public void addBatchCollection(List<BorrowCollection> list) {
		List<Object[]> listo = new ArrayList<Object[]>(); 
		if(list.size()>0){
			for (BorrowCollection c:list) {  
		      //  String sql="insert into borrow_collection(" +
		        //		"addtime,addip,tender_id,`order`,repay_time,repay_account,interest,capital) "+
				//		"values('"+c.getAddtime()+"','"+c.getAddip()+"',"+c.getOrder()+","+c.getOrder()+","+c.getRepay_time()+",'"+c.getRepay_account()+"','"+c.getInterest()+"','"+c.getCapital()+"')";
		        save(NAME_SPACE_BORROWCOLLECTION, c, "BORROWCOLLECTION");
			}  
			
		}
	}
	
	
	public double getCount(long userId){
		Double b;
		 b=(Double)getObjs(NAME_SPACE_BORROWCOLLECTION, userId, "GETCOUNTZQMONEY");
		 if(b==null){
			 return 0;
		 }
		 return b;
	}
	
	public double getDSmoney(long userId){
		Double b;
		 b=(Double)getObjs(NAME_SPACE_BORROWCOLLECTION, userId, "GETCOUNTDSMONEY");
		 if(b==null){
			 return 0;
		 }
		 return b;
	}
	
	public BorrowCollection getCollectionForAccount(Long userId){
		return getObj(NAME_SPACE_BORROWCOLLECTION, userId, "GETCOLLECTIONFORACCOUNT");
	}
	
	public List<BorrowCollection> getCollectionLlistByBorrow(long bid,int order){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("bid",bid);
		map.put("order",order);
		return getObjList(NAME_SPACE_BORROWCOLLECTION, map, "GET_COLLECTION_LIST_BY_BORROW");
	}
	
	/**
	 * 还款，钱多多返回的流水号
	 */
	public void modifyCollectionByQdd(BorrowCollection c) {
		update(NAME_SPACE_BORROWCOLLECTION, c, "QDD_MODIFY_BORROWCOLLECTION");
	}
	
	public List<BorrowCollection> queryBorrowCollectionByTenderId(long tenderId){
    	return getObjList(NAME_SPACE_BORROWCOLLECTION, tenderId, "USERID_AND_TENDERID");
    }
	
	/**
     * 查询未还款的待收记录当前期
     * @Title: queryPeriodByStatus 
     * @Description: TODO
     * @param @param borrowid
     * @param @param status
     * @param @return 设定文件 
     * @return BorrowCollection 返回类型 
     * @throws
     */
    public List<BorrowCollection> queryPeriodByStatus(long borrowid, int status){
        
        Map<String,String> params = new HashMap<String,String>();
        
        params.put("bid", String.valueOf(borrowid));
        params.put("status", String.valueOf(status));
        
        return getObjList(NAME_SPACE_BORROWCOLLECTION, params, "BORROW_FOR_PERIOD");
    }
    
}
