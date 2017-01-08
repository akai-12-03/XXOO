package com.dept.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.UserAccountLog;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class UserAccountLogDao extends IbatisBaseDaoImpl<UserAccountLog, Long>{
	public static final String NAME_SPACE_ACCOUNTLOG = "UserAccountLog";

	
	public Object save(UserAccountLog accountLog){
		return save(NAME_SPACE_ACCOUNTLOG, accountLog, NAME_SPACE_ACCOUNTLOG);
	}
	
	public Page<UserAccountLog> getAccountLogByUserId(PageRequest<Map<String, String>> pageRequest){
		return pageQuery(NAME_SPACE_ACCOUNTLOG, pageRequest, "SEARCH_ACCOUNT_LOG_BY_USERID");
	}
	
	/**
	 * 通过用户id查询该用户的账户记录
	 * @param userId
	 * @return
	 */
	public List<UserAccountLog> queryByUserId(Long userId) {
		return getObjList(NAME_SPACE_ACCOUNTLOG, userId, "USERID");
	}
	
	/**
	 * 添加
	 * @param accountLog
	 * @return
	 */
	public Object insertUserAccountLog(UserAccountLog accountLog) {
		return save(NAME_SPACE_ACCOUNTLOG, accountLog, "USERACCOUNTLOG");
	}
	
	/**
	 * 查询用户所有的交易记录
	 * @param map
	 * @return
	 */
	public Page<UserAccountLog> queryUserAccountLogByUserId(PageRequest<Map<String, String>> map) {
		return pageQuery(NAME_SPACE_ACCOUNTLOG, map, "QUERYBYUSERID");
	}

	/**
	 * 查询我推荐的用户中有投资记录的人数
	 * @param userId
	 * @return
	 */
	public List<UserAccountLog> getAccountLogCountByUserId(int userId){
		return getObjList(NAME_SPACE_ACCOUNTLOG, userId, "ACCUNTLOG_COUNT_BY_USERID");
	}
	
	public void addAccountLog(UserAccountLog log) {
		//String sql="insert into account_log(" +
		//		"user_id,type,total,money,use_money,no_use_money,collection," +
		//		"to_user,remark,addtime,addip) values("+log.getUser_id()+",'"+log.getType()+"',"+log.getTotal()+","+log.getMoney()+","+log.getUse_money()+","+log.getNo_use_money()+","+log.getCollection()+","+log.getTo_user()+","+log.getRemark()+",'"+log.getAddtime()+"','"+log.getAddip()+"')";
		save(NAME_SPACE_ACCOUNTLOG, log, "USERACCOUNTLOG");
	}
	
	public double queryByTypeAndUserId(Map<String, Object> map){
		Double b;
		 b=(Double)getObjs(NAME_SPACE_ACCOUNTLOG, map, "ACCOUNTFORACCOUNT");
		 if(b==null){
			 return 0;
		 }
		 return b;
	}
}
