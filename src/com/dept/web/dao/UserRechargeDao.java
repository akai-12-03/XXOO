package com.dept.web.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.UserRecharge;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class UserRechargeDao extends IbatisBaseDaoImpl<UserRecharge, Long>{

	public static final String NAME_SPACE_USERRECHARGE = "UserRecharge";

	public Object add(UserRecharge recharge){
		
		return save(NAME_SPACE_USERRECHARGE, recharge, "UserRecharge");
	}

	public UserRecharge getByRecharge(UserRecharge recharge) {
		return getObj(NAME_SPACE_USERRECHARGE, recharge, "UserRecharge");
	}
	
	/**
	 * 列表查询
	 * @param map
	 * @return
	 */
	public  List<UserRecharge> getUserRechargeList(Map<String, Object> map) {
		return getObjList(NAME_SPACE_USERRECHARGE, map, "UserRecharge");
	}

	public int updateByOrderId(UserRecharge userRecharge) {
		return update(NAME_SPACE_USERRECHARGE, userRecharge, "USERRECHARGE_BY_ORDER_ID");
	}

	/**
	 * 查询当前userid 下所有充值记录个数
	 * @param userId
	 * @return
	 */
	public int getUserRechargeCount(int userId){
		List<UserRecharge> list=new ArrayList<UserRecharge>();
		list=getObjList(NAME_SPACE_USERRECHARGE, userId, "CREATEBY");
		if(null!=list&&list.size()>0){
			return list.size();
		}else{
			return 0;
		}
	}

}
