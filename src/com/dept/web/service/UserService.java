package com.dept.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.HongbaoDao;
import com.dept.web.dao.HongbaoLogDao;
import com.dept.web.dao.HongbaoPlanDao;
import com.dept.web.dao.UserAccountDao;
import com.dept.web.dao.UserDao;
import com.dept.web.dao.model.Bank;
import com.dept.web.dao.model.HongbaoLog;
import com.dept.web.dao.model.Hongbao;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.general.util.MD5;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserAccountDao userAccountDao;
	
    @Autowired
    private HongbaoLogDao hongbaoLogDao;
    
    @Autowired
    private HongbaoPlanDao hongbaoPlanDao;
    @Autowired
    private HongbaoDao hongbaoDao;
    
   
	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public User geyByLogInfo(String username, String password) {

		return userDao.getByUserNamePassword(username, password);

	}
	
	/**
	 * 最新客户
	 * @return
	 */
	public List<User> queryZxuser(){
		return userDao.queryZxuser();
	}
	
	 /**
		 * 
		 * @Description:  更改密码
		 * @param:        @param userid
		 * @param:        @param newpassword
		 * @param:        @param type
		 * @param:        @return   
		 * @return:       int   
		 * @throws
		 */
		public int updatePassword(long userid, String newpassword, int type){
			
			return userDao.updatePassword(userid, newpassword, type);
		}
	/**
	 * @param valueOf
	 * @return
	 */
	public User queryByUserId(Long userId) {
		return userDao.queryByUserId(userId);
	}

	/**
	 * @param username
	 * @return
	 */
	public User queryUserByName(String username) {
		return userDao.queryUserByName(username);
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	public boolean isRightUserName(String username) {

		User user = userDao.queryUserByName(username);

		if (user == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查询该身份证是否已经被实名认证过
	 * 
	 * @param cardId
	 * @return
	 */
	public int findCardId(String cardId) {
		return userDao.findCardId(cardId);
	}

	/**
	 * 检查手机号是否可用
	 * 
	 * @param mobile
	 * @return
	 */
	public boolean isRightMobile(String mobile) {

		User user = userDao.queryUserByMobile(mobile);

		if (user == null) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * @Description: 注册用户
	 * @param: @param username
	 * @param: @param password
	 * @param: @param email
	 * @param: @param realname
	 * @param: @param realip
	 * @param: @return
	 * @return: User
	 * @throws
	 */
	public User createNewUser(User newuser) {
		MD5 md5 = new MD5();
		String p = newuser.getPasswordHash();
		newuser.setPasswordHash(md5.getMD5ofStr(p));
		// 设置初始交易密码为登录密码
		newuser.setPasswordPayHash(md5.getMD5ofStr(p));
		newuser.setCreatedAt(System.currentTimeMillis() / 1000);
		newuser.setStatus(0); // 默认用户状态是0
		//设置上次登录时间为当前时间，以便下次登录使用
		newuser.setLastLogintime(System.currentTimeMillis() / 1000);
		
		Long userid = userDao.insertUser(newuser);

		// 初始化用户account
		UserAccount account = new UserAccount(userid, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 0.0, System.currentTimeMillis() / 1000, null);
		userAccountDao.insertUserAmount(account);

		newuser = queryByUserId(userid);

		return newuser;
	}

	public int getAllUserCount() {

		return userDao.getAllUserCount();

	}

	/**
	 * 获取上次登录时间相关的信息 并修改lastLogintime为最新时间
	 * 
	 * @return
	 */
	public Long getlastLogintime(User user) {
		Long lastLogintime = null;
		lastLogintime = user.getLastLogintime();
		// 修改lastLogintime
		user.setLastLogintime(System.currentTimeMillis() / 1000);
		userDao.updateUser(user);
		return lastLogintime;
	}

	public int userUpdate(User user) {
		return userDao.updateUser(user);
	}

	public int getEmailForUser(String email) {

		return userDao.getEmailForUser(email);
	}

	/**
	 * 通过手机号查找对应的用户
	 * 
	 * @param mobile
	 * @return
	 */
	public User queryUserByMobile(String mobile) {
		return userDao.queryUserByMobile(mobile);
	}
	
	/**
	 * 通过平台标识查询用户
	 * 
	 * @param mobile
	 * @return
	 */
	public User queryUserByMid(String mid) {
		return userDao.queryUserByMid(mid);
	}

	/**
	 * 查询我的推荐用户总个数
	 * 
	 * @param inviteUserId
	 * @return
	 */
	public int queryByInviteUserid(String inviteUserId) {
		List<User> userList = new ArrayList<User>();
		userList = userDao.queryByInviteUserid(inviteUserId);
		if (null != userList && userList.size() > 0) {
			return userList.size();
		} else {
			return 0;
		}
	}

	/**
	 * 查询我的推荐中的实名认证的人数
	 * 
	 * @param status
	 * @return
	 */
	public int queryByShiMing(String invite) {
		List<User> userList = new ArrayList<User>();
		userList = userDao.queryByShiMing(invite);
		if (null != userList && userList.size() > 0) {
			return userList.size();
		} else {
			return 0;
		}
	}

	/**
	 * 分页查询我所推荐的用户
	 * 
	 * @param map
	 * @return
	 */
	public Page<User> getUserByInviteUserid(PageRequest<Map<String, String>> map) {
		return userDao.queryUserByInviteUserid(map);
	}
	
	/**
	 * 查询我所推荐的用户列表
	 * 
	 * @param map
	 * @return
	 */
	public List<User> queryListOfInvite(String userId) {
		return userDao.queryListOfInvite(userId);
	}
	
	/**  add by cannavaro **/
	
    /**
    * 根据状态和用户ID 获取用户的红包ID 
     * @param userId
     * @return
     */
    public List<HongbaoLog>  getListHongbaoLogByIDAndStatus(Map  map){
        
        return hongbaoLogDao.queryListHongbaoLogByIDAndStatus(map);
    }
    
    /**
    * 根据状态和用户ID 查询对应红包信息
     * @param userId
     * @return
     */
    public HongbaoLog  getHongbaoLogById(Long   hongbaoId){
        
        return hongbaoLogDao.queryHongbaoLogById(hongbaoId);
    }
    
    /**
    * 更新红包使用状态
     * @param status 
     * usrId
     * @return
     */
    public int updateHongbaoLog(HongbaoLog hongbao){
        return hongbaoLogDao.updateHongbaoLog(hongbao);
    }
    
    /**
     * 添加用户红包使用计划
     */
    public Long createNewPlan(Hongbao hongPlan){
        return (Long)hongbaoPlanDao.save(hongPlan);
    }
    
    public Page<Hongbao>  getHongbaoBypage(PageRequest<Map<String, String>> map){
    	return hongbaoDao.queryHongbaoByUserId(map);
    } 
}
