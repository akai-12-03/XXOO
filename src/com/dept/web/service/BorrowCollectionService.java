package com.dept.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dept.web.context.Constant;
import com.dept.web.dao.BorrowCollectionDao;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.general.interest.EndInterestCalculator;
import com.dept.web.general.interest.InterestCalculator;
import com.dept.web.general.interest.MonthEqualCalculator;
import com.dept.web.general.interest.MonthInterest;
import com.dept.web.general.interest.MonthInterestCalculator;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.NumberUtil;
import com.sendinfo.xspring.ibatis.page.Page;

@Service
public class BorrowCollectionService {

	@Autowired
	private BorrowCollectionDao borrowCollectionDao;

	@Autowired
	private BorrowService borrowService;

	public List<BorrowCollection> getBorrowCollectionList(int pageNum,
			int pageSize, Map<String, Object> filterMap) {

		if (filterMap == null) {
			filterMap = new HashMap<String, Object>();
		}
		filterMap.put("pageSize", pageSize);
		filterMap.put("offset", pageNum > 1 ? (pageNum - 1) * pageSize : 0);

		return borrowCollectionDao.getBorrowTenderList(filterMap);

	}

	public Page<BorrowCollection> getBorrowCollectionPage(int pageNum,
			int pageSize, Map<String, Object> filterMap) {
		int count = borrowCollectionDao.getBorrowCollectionListCount(filterMap);
		Page<BorrowCollection> page = new Page<BorrowCollection>(pageNum,
				pageSize, count);
		if (count == 0) {
			return page;
		}
		page.setResult(getBorrowCollectionList(pageNum, pageSize, filterMap));
		return page;
	}

	public void addBatchCollection(List<BorrowCollection> collectionsList) {
		for (BorrowCollection collection : collectionsList) {
			borrowCollectionDao.save(collection);
		}
	}

	public List<BorrowCollection> makeCollection(BorrowTender tender) {
		List<BorrowCollection> borrowCollections = new ArrayList<BorrowCollection>();
		Borrow borrow = borrowService.getBorrowById(tender.getBorrowId());

		if (borrow.getIsDay() == 1) {
			BorrowCollection collection = new BorrowCollection();

			double interest = NumberUtil.ceil(
					borrow.getAccount() * borrow.getApr() / 100 / 365
							* borrow.getTimeLimit(), 2);
			double repaymentAccount = tender.getAccount() + interest;

			collection.setInterest(interest);
			collection.setRepayAccount(repaymentAccount);
			collection.setTenderId(tender.getId());
			collection.setCapital(tender.getAccount());
			collection.setRepayTime(DateUtils.rollDay(borrow.getVerifyTime(),
					borrow.getTimeLimit() + borrow.getValidTime()));
			collection.setColOrder(1);
			collection.setStatus(Constant.BORROW_COLLECTION_STATUS_XJ);

			collection.setRepayYesaccount(0d);
			collection.setLateDays(0);
			collection.setLateInterest(0d);
			collection.setAddip(tender.getAddip());
			collection.setAddtime(System.currentTimeMillis() / 1000);

			borrowCollections.add(collection);
		} else {
			InterestCalculator ic = null;
			Integer repaymentStyle = borrow.getRepaymentStyle();
			Double money = tender.getMoney();
			Double apr = borrow.getApr() / 100;
			Integer timelimit = borrow.getTimeLimit();
			if (repaymentStyle == Constant.BORROW_REPAYMENT_STYLE_DQHBHX) {
				ic = new EndInterestCalculator(money, apr, timelimit,
						InterestCalculator.TYPE_MONTH_END);
			} else if (repaymentStyle == Constant.BORROW_REPAYMENT_STYLE_MYHX) {
				ic = new MonthInterestCalculator(money, apr, timelimit);
			} else {
				ic = new MonthEqualCalculator(money, apr, timelimit);
			}
			ic.each();
			List<MonthInterest> monthList = ic.getMonthList();

			for (int i = 0; i < monthList.size();) {
				MonthInterest monthInterest = monthList.get(i);
				BorrowCollection collection = new BorrowCollection();

				double interest = NumberUtil.ceil(monthInterest.getInterest(),
						2);
				double accountPerMon = NumberUtil.ceil(
						monthInterest.getAccountPerMon(), 2);
				double repaymentAccount = NumberUtil.ceil(accountPerMon
						+ interest, 2);

				collection.setColOrder(++i);
				collection.setCapital(accountPerMon);
				collection.setInterest(interest);
				collection.setRepayAccount(repaymentAccount);

				collection.setTenderId(tender.getId());

				if (repaymentStyle == Constant.BORROW_REPAYMENT_STYLE_DQHBHX) {

					collection.setRepayTime(DateUtils.rollMonth(
							DateUtils.rollDay(borrow.getVerifyTime(),
									borrow.getValidTime()),
							borrow.getTimeLimit()));
				} else {

					collection.setRepayTime(DateUtils.rollMonth(
							DateUtils.rollDay(borrow.getVerifyTime(),
									borrow.getValidTime()), i));
				}
				collection.setStatus(Constant.BORROW_COLLECTION_STATUS_XJ);

				collection.setRepayYesaccount(0d);
				collection.setLateDays(0);
				collection.setLateInterest(0d);
				collection.setAddip(tender.getAddip());
				collection.setAddtime(System.currentTimeMillis() / 1000);

				borrowCollections.add(collection);

			}

		}

		return borrowCollections;
	}

	public BorrowCollection getCollectionForAccount(Long userId){
		return borrowCollectionDao.getCollectionForAccount(userId);
	}
	
	public List<BorrowCollection> queryBorrowCollectionByTenderId(long tenderId){
		return borrowCollectionDao.queryBorrowCollectionByTenderId(tenderId);
	}
}
