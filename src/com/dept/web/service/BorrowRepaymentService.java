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
import com.dept.web.dao.BorrowRepaymentDao;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowRepayment;
import com.dept.web.dao.model.UserAccount;
import com.sendinfo.xspring.ibatis.page.Page;


@Service
public class BorrowRepaymentService {

	
	@Autowired
	private BorrowRepaymentDao repaymentDao;
	

	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private UserAccountLogService accountLogService;
	
	@Autowired
	private BorrowService borrowService;
	
	public BorrowRepayment getRepaymentById(Long id){
		BorrowRepayment repayment = new BorrowRepayment();
		repayment.setId(id);
		return getRepayment(repayment);
	}
	
	public BorrowRepayment getRepayment(BorrowRepayment repayment){
		
		return repaymentDao.getByRepayment(repayment);
	}
	
	public Page<BorrowRepayment> getBorrowRepaymentPage(int pageNum, int pageSize,
			Map<String, Object> filterMap) {
		int count = repaymentDao.getBorrowRepaymentListCount(filterMap);
		Page<BorrowRepayment> page = new Page<BorrowRepayment>(pageNum, pageSize, count);
		if (count == 0) {
			return page;
		}
		page.setResult(getBorrowRepaymentList(pageNum, pageSize, filterMap));
		return page;
	}

	public List<BorrowRepayment> getBorrowRepaymentList(int pageNum,
			int pageSize, Map<String, Object> filterMap) {
		if (filterMap == null) {
			filterMap = new HashMap<String, Object>();
		}
		filterMap.put("pageSize", pageSize);
		filterMap.put("offset", pageNum > 1 ? (pageNum - 1) * pageSize : 0);
		return repaymentDao.getBorrowRepaymentList(filterMap);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String repayment(Long userId, Long repaymentId,String ip) throws RuntimeException {
		
		BorrowRepayment repayment = doRepayment(repaymentId,ip);
		// 扣款(生成资金变动记录)
		userAccountService.borrowTenderPayment(userId, repayment.getRepaymentYesaccount());
		
		// 资金变动记录
		UserAccount account  = userAccountService.getUserAccount(userId);
		accountLogService.addUserAccountLog(account, userId, repayment.getRepaymentYesaccount(), Constant.ACCOUNT_LOG_TYPE_HK_KCBJ, ip, "还款资金冻结");
		
		return "还款成功";
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public synchronized BorrowRepayment doRepayment(Long repaymentId,String ip){
		
		BorrowRepayment repayment = getRepaymentById(repaymentId);
		
		Borrow borrow = borrowService.getBorrowById(repayment.getBorrowId());
		Integer status = borrow.getStatus();
		
		if(status.equals(Constant.BORROW_STATUS_FSTG) || status.equals(Constant.BORROW_STATUS_HKZ) || status.equals(Constant.BORROW_STATUS_YHK)){
			
			if(repayment.getStatus().equals(Constant.BORROW_REPAYMENT_STATUS_YH)){
				throw new RuntimeException("该笔金额已还");
			}
			
			Double account = repayment.getRepaymentAccount();//还款金额
			Double lateInterest = repayment.getLateInterest();//逾期利息
			Double reminderFee = repayment.getReminderFee();//催收费
//			Double forfeit = repayment.getForfeit();//滞纳金
			
			Double repaymentYesaccount = 0.0d;//实还金额
			
			repaymentYesaccount = new BigDecimal(account).add(new BigDecimal(lateInterest)).add(new BigDecimal(reminderFee)).doubleValue();
			
			
			/* 更新还款记录  */
			repayment.setRepaymentYesaccount(repaymentYesaccount);//
			repayment.setStatus(Constant.BORROW_REPAYMENT_STATUS_DDHK);//
			repayment.setWebstatus(Constant.BORROW_REPAYMENT_WEBSTATUS_FDH);//非网站代还
			repayment.setRepaymentYestime(System.currentTimeMillis()/1000);
			repayment.setUpdatetime(System.currentTimeMillis()/1000);
			repayment.setUpdateip(ip);
			
			
			int i = updateRepayment(repayment);
			if(i == 1){
				return repayment;
			}else{
				throw new RuntimeException("还款失败");
			}
			
		}else{
			throw new RuntimeException("该标目前的状态不能还款");
		}
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public int updateRepayment(BorrowRepayment repayment) {
		
		return repaymentDao.updateRepayment(repayment);
	}
	
}
