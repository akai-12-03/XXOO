package com.dept.web.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.UserBorrowModel;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class UserBorrowModelDao extends IbatisBaseDaoImpl<UserBorrowModel, Long>{

	public static final String NAME_SPACE_BORROW = "UserBorrowModel";
		public Page<UserBorrowModel> getUserCenterBorrowList(PageRequest<Map<String, String>> pageRequest){
			return pageQuery(NAME_SPACE_BORROW, pageRequest, "SEARCH_USERCENTER_BORROW_LIST");
		}
		
		
		/**
		 * 今日待还
		 * @param pageRequest
		 * @return
		 */
		public UserBorrowModel getUserBorrow(long userId,long dhstime,long dhetime){
			UserBorrowModel ubm=new UserBorrowModel();
			ubm.setUserId(userId);
			ubm.setDhstime(dhstime);
			ubm.setDhetime(dhetime);
			
			return getObj(NAME_SPACE_BORROW, ubm, "USERBORROWDHMONEY");
		}
}
