package com.dept.web.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Market;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class MarketDao extends IbatisBaseDaoImpl<Market, Long> {

	public static final String NAME_SPACE_MARKET = "Market";

	public Page<Market> searchMarket(
			PageRequest<Map<String, String>> pageRequest) {

		return pageQuery(NAME_SPACE_MARKET, pageRequest, "SEARCH");
	}

	/**
	 * 
	 * @Title: queryByTenderId
	 * @Description: TODO
	 * @param @param tid
	 * @param @return 设定文件
	 * @return List<Market> 返回类型
	 * @throws
	 */
	public List<Market> queryByTenderId(Long tid) {

		return getObjList(NAME_SPACE_MARKET, tid, "TENDER");
	}

	/**
	 * 
	 * @Title: queryMarketById
	 * @Description: TODO
	 * @param @param id
	 * @param @return 设定文件
	 * @return Market 返回类型
	 * @throws
	 */
	public Market queryMarketById(long id) {

		return getObj(NAME_SPACE_MARKET, id, "ID");
	}

	public void updateMarket(long m) {
		update(NAME_SPACE_MARKET, m, "UPDATESTATUSBYID");
	}

	public void updateMarket2(long m) {
		update(NAME_SPACE_MARKET, m, "UPDATESTATUSBYID2");
	}

	public List<Market> getMardketListByStatus(int status) {
		return getObjList(NAME_SPACE_MARKET, status, "STATUS");
	}

	public void delMarketByTenderId(long tenderId) {
		delete(NAME_SPACE_MARKET, tenderId, "TENDERID");
	}

	public Page<Market> searchMarket2(
			PageRequest<Map<String, String>> pageRequest) {

		return pageQuery(NAME_SPACE_MARKET, pageRequest, "SEARCH2");
	}

	public List<Market> findMarketByBorrowIdAndStatus(Long borrowId, int status) {
		// TODO Auto-generated method stub
 Map<String,String> params = new HashMap<String,String>();
         
         params.put("borrowId", String.valueOf(borrowId));
         params.put("status", String.valueOf(status));
        
        return getObjList(NAME_SPACE_MARKET, params, "BORROWIDANDSTATUS");
	}
}
