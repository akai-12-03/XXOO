package com.dept.web.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.context.Constant;
import com.dept.web.dao.BorrowCollectionDao;
import com.dept.web.dao.BorrowDao;
import com.dept.web.dao.BorrowRepaymentDao;
import com.dept.web.dao.BorrowTenderDao;
import com.dept.web.dao.UserAccountDao;
import com.dept.web.dao.UserAccountLogDao;
import com.dept.web.dao.UserBorrowModelDao;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowRepayment;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.dao.model.UserBorrowModel;
import com.dept.web.general.util.TimeUtil;
import com.dept.web.general.util.Utils;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class BorrowService {

	@Autowired
	private BorrowDao borrowDao;
	@Autowired
	private BorrowCollectionDao borrowCollectionDao;
	
	@Autowired
	private BorrowRepaymentDao borrowRepaymentDao;
	
	@Autowired
	private UserBorrowModelDao userBorrowModelDao;
	
	@Autowired
	private UserAccountLogDao userAccountLogDao;
	
	@Autowired
	private UserAccountDao userAccountDao;
	
	@Autowired
	private BorrowTenderDao borrowTenderDao;

	public BorrowRepayment getRepayment(long userId){
		return borrowRepaymentDao.getRepayment(userId);
	}
	
	public Borrow getBorrowById(Long id) {
		Borrow borrow = new Borrow();
		borrow.setId(id);
		return getBorrowByBorrow(borrow);
	}

	public Borrow getBorrowByBorrow(Borrow borrow) {
		return borrowDao.getByBorrow(borrow);
	}

	public Page<Borrow> getBorrowPage(int pageNum, int pageSize,
			Map<String, Object> filterMap) {
		int count = borrowDao.getBorrowListCount(filterMap);
		Page<Borrow> page = new Page<Borrow>(pageNum, pageSize, count);
		if (count == 0) {
			return page;
		}
		page.setResult(getBorrowList(pageNum, pageSize, filterMap));
		return page;
	}
	public Page<UserBorrowModel> getUserCenterBorrowList(PageRequest<Map<String, String>> pageRequest){
		return userBorrowModelDao.getUserCenterBorrowList(pageRequest);
	}
	public Page<BorrowRepayment> getUserCenterRepaymentdetailByborrowId(PageRequest<Map<String, String>> pageRequest){
		return borrowRepaymentDao.getUserCenterRepaymentdetailByborrowId(pageRequest);
	}
	public Page<BorrowRepayment> getUserCenterRepaymentdetail(PageRequest<Map<String, String>> pageRequest){
		return borrowRepaymentDao.getUserCenterRepaymentdetail(pageRequest);
	}

	public List<Borrow> getBorrowList(int pageNum, int pageSize,
			Map<String, Object> filterMap) {
		List<Borrow> b=null;
		if (filterMap == null) {
			filterMap = new HashMap<String, Object>();
		}
		filterMap.put("pageSize", pageSize);
		filterMap.put("offset", pageNum > 1 ? (pageNum - 1) * pageSize : 0);
		
		try {
			b=borrowDao.getBorrowList(filterMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	/**
	 * 
	 * @param filterMap
	 * @return
	 */
	public List<Borrow> getBorrowList(Map<String, Object> filterMap) {
		return borrowDao.getBorrowList(filterMap);
	}
	public Page<BorrowTender> getInvestTenderingListByUserid(PageRequest<Map<String, String>> pageRequest){
		return borrowTenderDao.getInvestTenderingListByUserid(pageRequest);
	}
	public Page<BorrowTender> getInvestTenderingListByUserid1(PageRequest<Map<String, String>> pageRequest){
		return borrowTenderDao.getInvestTenderingListByUserid1(pageRequest);
	}
	public Page<BorrowTender> getSuccessListByUserid(PageRequest<Map<String, String>> pageRequest){
		return borrowTenderDao.getSuccessListByUserid(pageRequest);
	}
	public Page<BorrowCollection> getCollectionList(PageRequest<Map<String, String>> pageRequest){
		return borrowCollectionDao.getCollectionList(pageRequest);
	}
	public Page<BorrowTender> getInvestTenderListByUserid(PageRequest<Map<String, String>> pageRequest){
		return borrowTenderDao.getInvestTenderListByUserid(pageRequest);
	}

	/**
	 * 查询总条数
	 * 
	 * @param filterMap
	 * @return
	 */
	public Integer getBorrowListCount(Map<String, Object> filterMap) {

		return borrowDao.getBorrowListCount(filterMap);

	}
	
	/**
	 * 根据userId查询资金明细
	 */
	public Page<UserAccountLog> getAccountLogByUserId(PageRequest<Map<String, String>> pageRequest){
		return userAccountLogDao.getAccountLogByUserId(pageRequest);
	}

	/**
	 * 投标--更新投标金额和状态
	 * 
	 * @param borrowId
	 * @param tenderMoney 投标金额
	 * @param updateip 更新ip
	 * @return
	 * @throws RuntimeException
	 */
	@Transactional(propagation = Propagation.MANDATORY)
	public synchronized int updateTenderBorrowMoney(Long borrowId,
			double tenderMoney,String updateip) throws RuntimeException {
		Borrow borrow = getBorrowById(borrowId);

		double moneyLowest = Utils.getDouble(borrow.getLowestAccount(), 2);// 最少投资
		double moneyMost = Utils.getDouble(borrow.getMostAccount(), 2);// 最多投资
		double moneyAccount = Utils.getDouble(borrow.getAccount(), 2);// 总金额
		double moneyYes = Utils.getDouble(borrow.getAccountYes(), 2);// 已投金额
		BigDecimal d = new BigDecimal(String.valueOf(moneyAccount));// 未投金额
		double moneyNo = d.subtract(new BigDecimal(String.valueOf(moneyYes)))
				.doubleValue();// 可投
		if (borrow.getStatus().equals(Constant.BORROW_STATUS_CSTG)) {

			if (moneyNo <= 0) {
				borrow.setStatus(Constant.BORROW_STATUS_MBDFS);
				borrowDao.updateBorrowTenderMoney(borrow);
				throw new RuntimeException("此标已满");
			}
			if (tenderMoney > moneyMost) {
				throw new RuntimeException("投资金额不能大于最大限制金额");
			}

			if (moneyNo < moneyLowest) {
				// 可投金额小于最低投资金额,必须一次投完.
				if (moneyNo != tenderMoney) {
					throw new RuntimeException("投资金额不足");
				}
			} else {
				if (tenderMoney < moneyLowest) {
					throw new RuntimeException("投资金额不能小于最小限制金额");
				}
			}

			if (moneyNo < tenderMoney) {
				throw new RuntimeException("超出可投金额");
			} else if (moneyNo == tenderMoney) {
				borrow.setStatus(Constant.BORROW_STATUS_MBDFS);
			}
			borrow.setAccountYes(tenderMoney);
			borrow.setUpdatetime(System.currentTimeMillis()/1000);
			borrow.setUpdateip(updateip);

			return borrowDao.updateBorrowTenderMoney(borrow);

		} else {
			throw new RuntimeException("标已满");
		}

	}
	
	/**
	 * 
	 * @Description:  TODO
	 * @param:        @param type
	 * @param:        @param count
	 * @param:        @return   
	 * @return:       List<Borrow>   
	 * @throws
	 */
	public List<Borrow> queryIndexList( int count){
		
		Map<String, Object> filters = new HashMap<String, Object>();
		
		filters.put("count", count);
		
		return borrowDao.queryIndexDisplayList(filters);
	}
	/**
	 * 投资人数
	 * @param filterMap
	 * @return
	 */
	public int getTenderList(Long id) {

		return borrowTenderDao.getTenderList(id);

	}
	public List<BorrowTender> getTender() {
		List<BorrowTender> b=null;
		try {
			b=borrowTenderDao.getTender();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;

	}
	
	
	public Page<Borrow> searchBorrowList(PageRequest<Map<String, String>> pageRequest){
			
			return borrowDao.searchBorrow(pageRequest);
		}
	
	public Borrow getAprs(){
		return borrowDao.getAprs();
	}
	
	
	public List<Borrow> getBorrowAddtime(){
		return borrowDao.getBorrowAddtime();
	}
	public List<Borrow> getBorrowApr(){
		return borrowDao.getBorrowApr();
	}
	public List<Borrow> getBorrowAccount(){
		return borrowDao.getBorrowAccount();
	}
	
	public String doRepay(BorrowRepayment repay, UserAccount act) {
		String 	message="还款成功!";
		Borrow borrow=borrowDao.getBorrowById(repay.getBorrowId());
		if(borrow.getStatus()!=Constant.BORROW_STATUS_FSTG&&borrow.getStatus()!=Constant.BORROW_STATUS_HKZ){
			message="借款标的状态不允许进行还款！";
			return message;
			
		}
		BorrowRepayment dbRepay=borrowRepaymentDao.getRepayment(repay.getId());
		if(dbRepay==null||dbRepay.getStatus()==Constant.BORROW_REPAYMENT_STATUS_YH||dbRepay.getWebstatus()==1){
			message="该期借款已经还款,请不要重复操作！";
			return message;
		}
		
		boolean hasAhead = borrowRepaymentDao.hasRepaymentAhead(repay.getRepOrder(),
				repay.getBorrowId());
		if (hasAhead) {
			message="还有尚未还款的借款！";
			return message;
		}
		
		repay.setWebstatus(1);
	//	repay.setStatus(1);
		BigDecimal repayMoney = BigDecimal.valueOf(repay.getRepaymentAccount()).setScale(2, BigDecimal.ROUND_DOWN); 
		BigDecimal lateMoney = BigDecimal.valueOf(repay.getLateInterest()).setScale(2, BigDecimal.ROUND_DOWN);
		double freezeVal = repayMoney.add(lateMoney).setScale(2, BigDecimal.ROUND_DOWN).doubleValue(); // 本金+利息+interestMoney
//		double repayMoney= repay.getRepaymentAccount();
//		double lateMoney = repay.getLateInterest();
//		double freezeVal=repayMoney+lateMoney;
		userAccountDao.updateAccount(0, -freezeVal, freezeVal, act.getUserId());
		if(borrowRepaymentDao.modifyRepaymentStatusWithCheck(repay.getId(), repay.getStatus(), repay.getWebstatus())<1){
			message="该期借款已经还款,请不要重复操作！";
			return message;
		}
		return message;
	}
	
	
	public String doRepay(BorrowRepayment repay, UserAccount act,
			UserAccountLog log) {
			String message = doRepay(repay, act);
			if(!message.equals("还款成功!")){
			    return message;
			}
			
			Borrow borrow=borrowDao.getBorrowById(repay.getBorrowId());
			act = userAccountDao.queryByUserId(act.getUserId());
			BigDecimal repayMoney = BigDecimal.valueOf(repay.getRepaymentAccount()).setScale(2, BigDecimal.ROUND_DOWN); 
			BigDecimal lateMoney = BigDecimal.valueOf(repay.getLateInterest()).setScale(2, BigDecimal.ROUND_DOWN);
//			double repayMoney= repay.getRepaymentAccount();
//			double lateMoney = repay.getLateInterest();
//			double freezeVal=repayMoney+lateMoney;
			double freezeVal = repayMoney.add(lateMoney).setScale(2, BigDecimal.ROUND_DOWN).doubleValue(); // 本金+利息+interestMoney
			log.setMoneyOperate(freezeVal);
			log.setMoneyTotal(act.getMoneyTotal());
			log.setMoneyUsable(act.getMoneyUsable());
//			log.setMoneyWithdraw(act.getMoneyWithdraw());
			if(act.getMoneyWithdraw()==null){
				log.setMoneyWithdraw(0d);
			}else{
				log.setMoneyWithdraw(act.getMoneyWithdraw());
			}
			log.setMoneyInsure(act.getMoneyInsure());
			log.setType(44);
			log.setCreatedAt(System.currentTimeMillis()/1000);
			log.setCreatedIp("1.1.1.1");
			log.setMoneyTenderFreeze(act.getMoneyTenderFreeze());
			log.setMoneyCollection(act.getMoneyCollection());
			log.setRemark(logRemarkHtml(borrow)+"冻结进行还款的本息");
			userAccountLogDao.addAccountLog(log);
			return message;
		}
	
	public String logRemarkHtml(Borrow model){
		String s=model.getName();
		return s;
	}
	
	
	public List<Borrow> queryblist(Map<String, Object> filters){
		return borrowDao.queryblist(filters);
	}
	/**
	 * 用户总收益
	 */
	public double getInterestByBorrowTenderForUserId(Long userId){
		return borrowTenderDao.getInterestByBorrowTenderForUserId(userId);
	}
	
	
	/**
	 * 已赚取的钱
	 * @param userId
	 * @return
	 */
	public double getCount(long userId){
		return borrowCollectionDao.getCount(userId);
	}
	
	/**
	 * 待收金额
	 * @param userId
	 * @return
	 */
	public double getDSmoney(long userId){
		return borrowCollectionDao.getDSmoney(userId);
	}
	
	/**
	 * 首页按条件查询
	 */
	public List<Borrow> getBorrowListByborrowTypeForIndexStatus(Borrow b){
		return borrowDao.getBorrowListByborrowTypeForIndexStatus(b);
	}
	
	/**
	 * 获取最新列表
	 * @param pageRequest
	 * @return
	 */
	public Page<Borrow> getsearchBorrowList(PageRequest<Map<String, String>> pageRequest){
		
		return borrowDao.getsearchBorrow(pageRequest);
	}
	
	public List<Borrow> getIndexBorrowList(int limit){
		return borrowDao.getIndexBorrowList(limit);
	}
	
	public Borrow queryForLoan(){
		return borrowDao.queryForLoan();
	}
	
	/**
	 * 首页按条件查询
	 */
	public List<Borrow> getBorrowListBorrowIndex(Borrow b){
		return borrowDao.getBorrowListBorrowIndex(b);
	}

	  public BorrowTender queryTenderById(long tenderId){
		    
		    return borrowTenderDao.getBorrowTenderById(tenderId);
		    
		}
	  
	  public Borrow getBorrowById2(long id)
	  {
		  return borrowDao.getBorrowById(id);
	  }
	  
	  public List<BorrowRepayment> getRepaymentByBorrowId(long borrowId, int status)
	  {
		  return borrowRepaymentDao.getRepaymentByBorrowId(borrowId, status);
	  }
}
