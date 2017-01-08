package com.dept.web.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dept.web.context.Constant;
import com.dept.web.dao.BorrowCollectionDao;
import com.dept.web.dao.BorrowDao;
import com.dept.web.dao.BorrowTenderDao;
import com.dept.web.dao.UserAccountDao;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.general.interest.EndInterestCalculator;
import com.dept.web.general.interest.InterestCalculator;
import com.dept.web.general.interest.MonthEqualCalculator;
import com.dept.web.general.interest.MonthInterest;
import com.dept.web.general.interest.MonthInterestCalculator;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.NumberUtil;
import com.dept.web.general.util.Utils;
import com.sendinfo.xspring.ibatis.page.Page;

@Service
public class BorrowTenderService {

	@Autowired
	private BorrowTenderDao borrowTenderDao;
	
	@Autowired
	private UserAccountLogService accountLogService;

	@Autowired
	private UserAccountDao userAccountDao;
	
	@Autowired
	private BorrowCollectionDao borrowCollectionDao;
	
	@Autowired
	private BorrowDao borrowDao;

	/**
	 * 查询标的投资记录
	 * 
	 * @param borrowId
	 * @return
	 */
	public List<BorrowTender> getTenderListByBorrowId(Long borrowId) {
		return borrowTenderDao.getBorrowTenderListByBorrowId(borrowId);
	}
	
	
	public List<BorrowTender> getTenderList() {
		return borrowTenderDao.getBorrowTenderList();
	}
	
	
	public List<BorrowTender> getBorrowTenderList(int pageNum, int pageSize,
			Map<String, Object> filterMap) {
		if (filterMap == null) {
			filterMap = new HashMap<String, Object>();
		}
		filterMap.put("pageSize", pageSize);
		filterMap.put("offset", pageNum > 1 ? (pageNum - 1) * pageSize : 0);
		
		return borrowTenderDao.getBorrowTenderList(filterMap);
	}
	
	public Page<BorrowTender> getBorrowTenderPage(int pageNum,
			int pageSize, Map<String, Object> filterMap) {
		int count = borrowTenderDao.getBorrowTenderListCount(filterMap);
		Page<BorrowTender> page = new Page<BorrowTender>(pageNum, pageSize, count);
		if (count == 0) {
			return page;
		}
		page.setResult(getBorrowTenderList(pageNum, pageSize, filterMap));
		return page;
	}

//	@Transactional(propagation = Propagation.REQUIRED)
//	public String borrowTender(BorrowTender borrowTender)
//			throws RuntimeException {
//		// 扣款(生成资金变动记录)
//		userAccountService.borrowTenderPayment(borrowTender.getUserId(),
//				borrowTender.getAccount());
//
//		// 资金变动记录
//		
//		UserAccount account  = userAccountService.getUserAccount(borrowTender.getUserId());
//		
//		accountLogService.addUserAccountLog(account,borrowTender.getUserId(),borrowTender.getAccount(),Constant.ACCOUNT_LOG_TYPE_TZCG,borrowTender.getAddip(),"扣除理财产品"+borrowTender.getBorrowId()+"的冻结金额"+borrowTender.getAccount()+"元");
//		
//		// 更新标的
//		borrowService.updateTenderBorrowMoney(borrowTender.getBorrowId(),
//				borrowTender.getAccount(),borrowTender.getAddip());
//
//		// 增加投标记录
//		long i= addTender(borrowTender);
//		
//		borrowTender.setId(i);
//		//生成收款纪录
//		borrowCollectionService.addBatchCollection(borrowCollectionService.makeCollection(borrowTender));
//
//		return "success";
//	}
	
	/**
	 * 
	 * @Description:  投标
	 * @param:        @param tender
	 * @param:        @param borrow
	 * @param:        @param act
	 * @param:        @param log
	 * @param:        @return
	 * @param:        @throws Exception   
	 * @return:       BorrowTender   
	 * @throws
	 */
	public BorrowTender addTender(BorrowTender borrowTender, Borrow borrow) throws Exception {
		BorrowTender t = getValidTender(borrowTender, borrow);
		double validAccount =t.getAccount();

		// 按照等额还息算法得出每期的还款金额以及利息
		/*
		 * EqualInterestCalculator ic =new EqualInterestCalculator(validAccount,
		 * model.getApr()/100,NumberUtils.getInt(model.getTime_limit()));
		 */
		
		UserAccount act= userAccountDao.queryByUserId(borrowTender.getUserId());
		
		double repayment_account = 0;
		
		//天标
		
		if(borrow.getIsDay()==1){
			
			repayment_account = validAccount + validAccount*borrow.getApr()/100/365*borrow.getTimeLimit();
			
			repayment_account=BigDecimal.valueOf(repayment_account).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
			
			BorrowCollection collect = new BorrowCollection();
			
			
			collect = fillCollectionForDay(validAccount*borrow.getApr()/100/365*borrow.getTimeLimit(), t, validAccount);
		
			collect.setColOrder(1);
			Long repayTime;
			
			repayTime = DateUtils.rollDay(borrowTender.getAddtime(),
					borrow.getTimeLimit());
			
			collect.setRepayTime(repayTime);
			
			borrowCollectionDao.save(collect);
			
		}else{
			
			InterestCalculator ic = getInterestCalculator(validAccount,borrow.getApr()/100,Integer.valueOf(borrow.getTimeLimit()),borrow.getRepaymentStyle());
			
			List<MonthInterest> monthList = ic.getMonthList();
			// 拼装Collection对象 批量插入还款表
			List<BorrowCollection> collectList = new ArrayList<BorrowCollection>();
			int i = 1;
			for (MonthInterest mi : monthList) {
				BorrowCollection c = fillCollection(mi, t, ic);
				//统计整个应还金额
				repayment_account=repayment_account+c.getRepayAccount();
				
				repayment_account=BigDecimal.valueOf(repayment_account).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
				
				c.setColOrder(i++);
				if (borrow.getBorrowType() == 110) {
					Long repayTime;
					if (borrow.getIsDay() == 1) {
						repayTime = DateUtils.rollDay(borrowTender.getAddtime(),
								borrow.getTimeLimit());
					} else {
						repayTime = DateUtils.rollMonth(borrowTender.getAddtime(),
								borrow.getTimeLimit());
					}
					c.setRepayTime(repayTime);
				}
				collectList.add(c);
			}
			borrowCollectionDao.addBatchCollection(collectList);
			// 总共需要还款金额
			//repayment_account = ic.getTotalAccount();
		}
		
		double repayment_interest = repayment_account- t.getAccount();
		t.setRepaymentAccount(NumberUtil.format6(repayment_account));
		t.setInterest(NumberUtil.format6(repayment_interest));
		t.setWaitAccount(NumberUtil.format6(t.getAccount()));
		t.setWaitInterest(NumberUtil.format6(repayment_interest));
		// 将归还信息写进去
		t = borrowTenderDao.modifyTender(t);
		
		//修改borrow应还金额
		borrow.setRepaymentAccount(NumberUtil.format6(repayment_account));
		borrowDao.update(borrow);
		
		// 修改账户资金
		int row = 0;
		try {
			row = userAccountDao.updateAccountNotZero(0, -validAccount,
					validAccount, act.getUserId());
		} catch (Exception e) {
			e.getMessage();
		} finally {
			if (row < 1)
				throw new Exception("投资人冻结投资款失败！请注意您的可用余额。");
		}
		act = userAccountDao.queryByUserId(act.getUserId());
		UserAccountLog accountLog = new UserAccountLog();
		accountLog.setUserId(borrowTender.getUserId());
		accountLog.setMoneyOperate(borrowTender.getAccount());
		accountLog.setMoneyTotal(act.getMoneyTotal());
		accountLog.setMoneyCollection(act.getMoneyCollection());
		accountLog.setMoneyInsure(act.getMoneyInsure());
		accountLog.setMoneyTenderFreeze(act.getMoneyTenderFreeze());
		accountLog.setMoneyUsable(act.getMoneyUsable());
		accountLog.setMoneyWithdraw(act.getMoneyWithdraw());
		accountLog.setCreatedIp(borrowTender.getAddip());
		accountLog.setCreatedAt(System.currentTimeMillis()/1000);
		accountLog.setType(Constant.ACCOUNT_LOG_TYPE_TZCG);
		accountLog.setRemark("投资成功，冻结投资者的投标资金"+ NumberUtil.format4(validAccount)+"元");
		accountLogService.insertUserAccountLog(accountLog);
		
		return t;
	}
	
	
	/**
	 * 验证投标
	 * @Title: getValidTender 
	 * @Description: TODO
	 * @param @param tender
	 * @param @param borrow
	 * @param @param log
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return BorrowTender 返回类型 
	 * @throws
	 */
	public BorrowTender getValidTender(BorrowTender tender,Borrow borrow) throws Exception {

		double validAccount = 0;
		double tenderAccount = Utils.getDouble(Double.valueOf(tender.getMoney()), 2);
		double account_val = Utils.getDouble(Double.valueOf(borrow.getAccount()),2);
		double account_yes_val = Utils.getDouble(Double.valueOf(borrow.getAccountYes()),2);
		//long tender_times_val = Long.valueOf(borrow.getTenderTimes());
		// 判断是否超出有效投标金额,满标情况
		if (account_yes_val >= account_val) {
			throw new Exception("此标已满！");
		}
		if (tenderAccount + account_yes_val >= account_val){
			validAccount = Utils.getDouble(account_val - account_yes_val, 2);
			borrow.setStatus(2);  //设为满标
			
		} else {
			validAccount = tenderAccount;
		}
		
		borrow.setAccountYes(Double.valueOf(account_yes_val + validAccount + ""));
		//borrow.setTenderTimes((tender_times_val + 1) + "");
		UserAccount newAccount = userAccountDao.queryByUserId(tender.getUserId());

//		if (validAccount > newAccount.getMoneyUsable()) {
//			throw new Exception("投资金额大于您的可用金额，投标失败！");
//		}
		/*// 待收金额小于5000，无法进行投秒
		double mbTenderAccount = Double.valueOf(Global
				.getValue("index_money"));
		if (mbTenderAccount > 0
				&& NumberUtil.format2(newAccount.getMoneyCollection()) < mbTenderAccount) {
			throw new Exception("您的待收金额小于" + mbTenderAccount
					+ ",不能进行投秒标，投标失败！");
		}*/

		int count = borrowDao.updateBorrow(validAccount, borrow.getStatus(),
				borrow.getId());
		if (count < 1) {
			throw new Exception("投标失败！失败原因有可能是：此标已满！");
		}
		if (borrow.getBorrowType() == 110) {
			borrowDao.update(borrow);
		}
		// 借贷关系记录插入
		tender.setMoney(NumberUtil.format6(tenderAccount));
		tender.setAccount(NumberUtil.format6(validAccount));
		double most_account_num = borrow.getMostAccount();
		double hasTender = 0;
		try {
			hasTender = borrowTenderDao.hasTenderTotalPerBorrowByUserid(
					borrow.getId(), tender.getUserId());
		} catch (Exception e) {
//			e.getMessage();
//			throw new Exception("系统繁忙，投标失败,请稍后再试！");
			e.printStackTrace();
		}
		if (validAccount + hasTender > most_account_num && most_account_num > 0) {
			throw new Exception("投资金额不能大于最大限制金额额度! ");
		}
		
		tender.setInterest(0.0);
		tender.setRepaymentYesaccount(0.0);
		tender.setRepaymentAccount(0.0);
		Long Ids  = borrowTenderDao.addBorrowTender(tender);
		tender.setId(Ids);
		return tender;
	}
	
	
	
	
	/** 
	 * 
	 * @Description:  天标
	 * @param:        @param mi
	 * @param:        @param t
	 * @param:        @param ic
	 * @param:        @return   
	 * @return:       BorrowCollection   
	 * @throws
	 */
	public BorrowCollection fillCollectionForDay(Double interest, BorrowTender t,
			Double account) {
		
		interest=BigDecimal.valueOf(interest).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
		BorrowCollection c = new BorrowCollection();
		c.setTenderId(t.getId());
		c.setInterest(interest);
		c.setCapital(account);
		c.setRepayAccount((interest+account));
		c.setAddtime(new Date().getTime() / 1000);
		// 默认处理
		c.setStatus(0);
		c.setRepayYesaccount(0.0);
		c.setLateDays(0);
		c.setLateInterest(0.0);
		return c;
	}
	
	/**
	 * 
	 * @Description:  计算还款利息
	 * @param:        @param money  借款金额
	 * @param:        @param apr    利率
	 * @param:        @param timelimit  期限
	 * @param:        @param style  还款类型
	 * @param:        @return   
	 * @return:       InterestCalculator   
	 * @throws
	 */
	public InterestCalculator getInterestCalculator(double money,double apr, int timelimit,int style){
		
		InterestCalculator ic = null;
		
		if(style==2){
			ic =new EndInterestCalculator(money,apr,timelimit,InterestCalculator.TYPE_MONTH_END);
			
		}else if(style==3){
			ic =new MonthInterestCalculator(money,apr,timelimit);
		}else{
			ic =new MonthEqualCalculator(money,apr,timelimit);
		}
		
		ic.each();
		
		return ic;
	}
	
	
	/**
	 * 
	 * @Title: fillCollection 
	 * @Description: TODO
	 * @param @param mi
	 * @param @param t
	 * @param @param ic
	 * @param @return 设定文件 
	 * @return BorrowCollection 返回类型 
	 * @throws
	 */
	public BorrowCollection fillCollection(MonthInterest mi, BorrowTender t,
			InterestCalculator ic) {
		BorrowCollection c = new BorrowCollection();
		c.setTenderId(t.getId());
		// c.setRepay_time(repay_time) 何时写入数据库
		c.setInterest(mi.getInterest());
		c.setCapital(mi.getAccountPerMon());
		c.setRepayAccount((mi.getInterest() + mi.getAccountPerMon()));
		c.setAddtime(new Date().getTime() / 1000);
		// 默认处理
		c.setStatus(0);
		c.setRepayYesaccount(0.00);
		c.setLateDays(0);
		c.setLateInterest(0.00);
		return c;
	}
	
	public int getTenderCountByUserId(long id){
		return borrowTenderDao.queryTenderCountByUserId(id);
	}


	public BorrowTender getUserBorrowTenderById(Long btId) {
		// TODO Auto-generated method stub
		return borrowTenderDao.getTenderById(btId);
	}
	
	public void udpateborrowtenderbyId(long userid,long id,int transfer){
		borrowTenderDao.udpateborrowtenderbyId(userid,id,transfer);
	}


	public void updateBorrowTender(BorrowTender tender) {
		// TODO Auto-generated method stub
		borrowTenderDao.update(tender);
	}

}
