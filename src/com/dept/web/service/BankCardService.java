package com.dept.web.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.BankCardDao;
import com.dept.web.dao.UserDao;
import com.dept.web.dao.model.Bank;
import com.dept.web.dao.model.BankCard;
import com.dept.web.dao.model.User;
import com.dept.web.general.util.TimeUtil;
import com.sendinfo.common.lang.StringUtil;

@Service
@Transactional(rollbackFor=Exception.class)
public class BankCardService {
	@Autowired
	private BankCardDao bankCardDao;
	@Autowired
	private UserDao userDao;
	/**
	 * 添加绑定银行卡
	 * @param user
	 * @return
	 */
	public Long addBankCard(BankCard bankCard){
		return bankCardDao.insertBankCard(bankCard);
	}
	public List<BankCard> queryAll() {
		return bankCardDao.queryAll();
	}
	/**
	 * 查询用户的所有银行卡信息
	 * @param userId
	 * @return
	 */
   public BankCard geyUserBank(long userId){
		
	   List<BankCard> banklist = bankCardDao.queryUserBankCardListByUserId(userId);
        
        if(banklist.size()>=1){
            
            return banklist.get(0);
            
        }else{
            
            return null;
        }
	}
	
	/**
	 * 查询用户所有绑定的银行卡
	 * @param user
	 * @return
	 * @throws SQLException 
	 */
	public List<BankCard> selectAllBankCardByUser(Long userId) throws SQLException {
		return bankCardDao.selectAllBankCardByUser(userId);
	}
	
	/**
	 * 查询该银行卡是否处于绑定状态
	 * @param cardNo
	 * @return
	 */
	public BankCard getBankCardByCardNo(String cardNo) {
		return bankCardDao.getBankCardByCardNo(cardNo);
	}
	
	/**
	 * 根据银行卡的id删除银行卡
	 * @param bankcardid
	 */
	public void deleteByBankCardId(int bankcardid) {
		bankCardDao.deleteByBankCardId(bankcardid);
	}
	
	/**
	 * 根据用户的id删除银行卡
	 * @param bankcardid
	 */
	public void deleteByUserId(Long userId) {
		bankCardDao.deleteByUserId(userId);
	}

	/**
	 * 通过银行卡id查询银行卡
	 * @param bankcardid
	 * @return
	 */
	public BankCard queryBankCardById(String bankcardid) {
		return bankCardDao.queryBankCardById(bankcardid);
	}

	/**
	 * 修改银行卡
	 * @param bankCard
	 */
	public int updateBankCard(BankCard bankCard) {
		return bankCardDao.updateBankCard(bankCard);
		
	}
	
	/**
	 * 根据userid修改银行卡
	 * @param bankCard
	 */
	public int updateBankCardByUserId(BankCard bankCard) {
		return bankCardDao.updateBankCardByUserId(bankCard);
		
	}
	
	/**
	 * 把添加银行卡的操作放到一个事务中
	 * @param user
	 * @param bankCard
	 */
	public void addBankCardInTransaction(User user,BankCard bankCard) {
		// 添加绑定的银行卡
		this.addBankCard(bankCard);
		// 修改user中card_bind_status字段为1
		user.setCardBindingStatus(1);
		userDao.updateUser(user);
	}
	
	/**
	 * 把删除银行卡的操作放到一个事务中
	 * @param bankcardid
	 * @param session
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public List<BankCard> deleteBankCardInTransaction(int bankcardid,HttpSession session,User user) throws SQLException {
		// 1.删除id为bankcarid的银行卡
		this.deleteByBankCardId(bankcardid);
		// 2.判断该用户是否还有绑定的银行卡
		List<BankCard> list = this.selectAllBankCardByUser(user.getId());
		// 2(1)如果有则user表中的cardBandingStatus字段不用变
		// 2(2)否则user表中的cardBandingStatus字段修改为0,并更新user对象，并更新session中userInSession为最新的user对象
		if (list.size() == 0) {
			user.setCardBindingStatus(0);
			userDao.updateUser(user);
			session.setAttribute("userInSession", user);
		}
		
		return list;
	}
	
	/**
	 * 把删除银行卡的操作放到一个事务中
	 * @param bankcardid
	 * @param session
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public List<BankCard> delCardByUserIdInTransaction(Long userId,HttpSession session,User user) throws SQLException {
		// 1.删除id为bankcarid的银行卡
		deleteByUserId(userId);
		// 2.判断该用户是否还有绑定的银行卡
		List<BankCard> list = this.selectAllBankCardByUser(user.getId());
		// 2(1)如果有则user表中的cardBandingStatus字段不用变
		// 2(2)否则user表中的cardBandingStatus字段修改为0,并更新user对象，并更新session中userInSession为最新的user对象
		if (list.size() == 0) {
			user.setCardBindingStatus(0);
			userDao.updateUser(user);
			session.setAttribute("userInSession", user);
		}
		
		return list;
	}
	
	/**
	 * 根据用户Id查询其所有银行卡
	 * @param userId
	 * @return
	 */
	public List<BankCard> getUserBankCard(long userId){
		List<BankCard> listBankCards=new ArrayList<BankCard>();
				listBankCards=bankCardDao.queryUserBankCardListByUserId(userId);
		if(null!=listBankCards&&listBankCards.size()>0){
			for (BankCard bankCard : listBankCards) {
				if(!StringUtil.isEmpty(bankCard.getCardNo())){
					//将1234567890000类型的银行卡转换成 1234*****0000
					bankCard.setCardNo(TimeUtil.changeBankNum(bankCard.getCardNo()));
				}
			}
			return listBankCards;
		}else{
			return null;
		}
	}
}
