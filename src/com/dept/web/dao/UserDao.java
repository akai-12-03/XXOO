package com.dept.web.dao;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Bank;
import com.dept.web.dao.model.User;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class UserDao extends IbatisBaseDaoImpl<User, Long> {

	public static final String NAME_SPACE_USER = "User";

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public User getByUserNamePassword(String username, String password) {
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("username", username);
		filters.put("password", password);

		return getObj(NAME_SPACE_USER, filters, "LOGINFO");
	}

	 /**
		 * 
		 * @Description:  TODO
		 * @param:        @param userid
		 * @param:        @param newpassword
		 * @param:        @return   
		 * @return:       int   
		 * @throws
		 */
		public int updatePassword(long userid, String newpassword, int type){
			
			Map<String, Object> filters = new HashMap<String,Object>();
			filters.put("id", userid);
			filters.put("password", newpassword);
			if(type==1){
				return update(NAME_SPACE_USER, filters, "USER_PASSWORD");
			}else{
				return update(NAME_SPACE_USER, filters, "USER_PAYPASSWORD");
			}
		}
	/**
	 * @param userId
	 * @return
	 */
	public User queryByUserId(Long userId) {
		return getObj(NAME_SPACE_USER, userId, "USERID");
	}
	/***
	 * 最新客户
	 * @return
	 */
	public List<User> queryZxuser(){
		return getObjList(NAME_SPACE_USER, null, "ZXUSER");
	}
	
	/**
	 * @param username
	 * @return
	 */
	public User queryUserByName(String username) {
		List<User> userlist = getObjList(NAME_SPACE_USER, username, "USERNAME");
		if (userlist.size() > 0) {
			return userlist.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @Description: 手机绑定查找用户
	 * @param: @param mobile
	 * @param: @return
	 * @return: User
	 * @throws
	 */
	public User queryUserByMobile(String mobile) {
		List<User> userlist = getObjList(NAME_SPACE_USER, mobile, "MOBILE");
		if (userlist.size() > 0) {
			return userlist.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @Description: 根据平台标识查询用户
	 * @param: @param mobile
	 * @param: @return
	 * @return: User
	 * @throws
	 */
	public User queryUserByMid(String mid) {
		return getObj(NAME_SPACE_USER, mid, "MID");
	}
	
	/**
	 * 
	 * @Description:  TODO
	 * @param:        @param user
	 * @param:        @return   
	 * @return:       Long   
	 * @throws
	 */
	public Long insertUser(User user){
		return (Long) save(user);
	}

	
	public int getAllUserCount(){
		return getSelectCount(NAME_SPACE_USER, null, "ALLUSER");
	}
	
	public int updateUser(User user) {
		return update(user);
	}
	
	/**
	 * 验证邮箱是否唯一
	 */
	
	public int getEmailForUser(String email){
		return getSelectCount(NAME_SPACE_USER, email, "EMAIL_FOR_USER");
	}

	/**
	 * 验证身份证信息是否唯一
	 */
	public int findCardId(String cardId){
		return getSelectCount(NAME_SPACE_USER, cardId, "FIND_CARDID");
	}

	/**
	 * 查询我的所推荐的用户个数
	 * @param inviteUserId
	 * @return
	 */
	public List<User> queryByInviteUserid(String inviteUserId){
		List<User> userList=getObjList(NAME_SPACE_USER, inviteUserId, "FINDBY_INVITEUSERID");
		return userList;
	}

	/**
	 * 查询我的推荐中所有实名认证的人数
	 * @param status
	 * @return
	 */
	public List<User> queryByShiMing(String invite){
		List<User> userList=getObjList(NAME_SPACE_USER, invite, "FINDBY_SHIMING");
		return userList;
	}

	/**
	 * 分页查询我所推荐的用户
	 * @param map
	 * @return
	 */
	public Page<User> queryUserByInviteUserid(PageRequest<Map<String, String>> map){
		return pageQuery(NAME_SPACE_USER, map, "FIND_USER_BY_INVITEUSERID");
	}
	
	/**
	 * 查询我所推荐的用户
	 * @param map
	 * @return
	 */
	public List<User> queryListOfInvite(String userId) {
		return getObjList(NAME_SPACE_USER, userId, "QUERY_INVITE_LIST");
	}
	
}
