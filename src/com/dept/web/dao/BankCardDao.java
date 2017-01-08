package com.dept.web.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.BankCard;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
@Repository
public class BankCardDao extends IbatisBaseDaoImpl<BankCard, Long>{

	public static final String NAME_SPACE_BANK= "BankCard";
	/**
	 * 添加绑定银行卡
	 * @param user
	 * @return
	 */
	public Long insertBankCard(BankCard bankCard){
		return (Long) save(bankCard);
	}
	
	public List<BankCard> queryAll() {
		return getObjList(NAME_SPACE_BANK, null, "QUERYALL");
	}
	/**
	 * 查询用户所以绑定的银行卡
	 * @param user
	 * @return
	 * @throws SQLException 
	 */
	public List<BankCard> selectAllBankCardByUser(Long userId) throws SQLException {
		return getObjList(NAME_SPACE_BANK, userId, "USERID");
	}
	
	/**
	 * 查询该银行卡是否处于绑定状态
	 * @param cardNo
	 * @return
	 */
	public BankCard getBankCardByCardNo(String cardNo) {
		return getObj(NAME_SPACE_BANK, cardNo, "CARDNO");
	}
	
	/**
	 * 根据银行卡id删除银行卡
	 * @param bankcardid
	 */
	public void deleteByBankCardId(int bankcardid) {
		delete(NAME_SPACE_BANK, bankcardid, "BY_ID");
	}
	
	/**
	 * 根据用户id删除银行卡
	 * @param bankcardid
	 */
	public void deleteByUserId(Long userid) {
		delete(NAME_SPACE_BANK, userid, "BY_USER_ID");
	}
	
	/**
	 * 通过银行卡id查询银行卡
	 * @param bankcardid
	 * @return
	 */
	public BankCard queryBankCardById(String bankcardid) {
		
		return getObj(NAME_SPACE_BANK, Integer.parseInt(bankcardid), "BANKCARDID");	
	}
	
	/**
	 * 修改银行卡
	 * @param bankCard
	 */
	public int updateBankCard(BankCard bankCard) {
		return update(bankCard);
	}
	
	/**
	 * 修改银行卡
	 * @param bankCard
	 */
	public int updateBankCardByUserId(BankCard bankCard) {
		return update(NAME_SPACE_BANK, bankCard, "BANKCARD_BY_USER");
	}
	/**
	 * 查询用户的所有银行卡信息
	 * @param userId
	 * @return
	 */
	public List<BankCard> queryUserBankCardListByUserId(long userId){
		return getObjList(NAME_SPACE_BANK, userId, "USER1");
	}
}
