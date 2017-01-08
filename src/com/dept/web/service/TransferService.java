package com.dept.web.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.context.Constant;
import com.dept.web.dao.BorrowCollectionDao;
import com.dept.web.dao.DebtTransferDao;
import com.dept.web.dao.MarketDao;
import com.dept.web.dao.UserAccountDao;
import com.dept.web.dao.VerifyBorrowLogDao;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.DebtTransfer;
import com.dept.web.dao.model.Market;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.VerifyBorrowLog;
import com.dept.web.general.util.DateUtils;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
@Transactional(rollbackFor=Exception.class)
public class TransferService {
	
	@Autowired
	private MarketDao marketDao;
	
	@Autowired
	private DebtTransferDao debtTransferDao;
	
	@Autowired
	private VerifyBorrowLogDao verifyBorrowLogDao;
	
	@Autowired
	private BorrowCollectionDao borrowCollectionDao;
	
	@Autowired
	private UserAccountDao userAccountDao;
	
	/**
	 * 转让市场列表
	 * @Title: searchMarketList 
	 * @Description: TODO
	 * @param @param pageRequest
	 * @param @return 设定文件 
	 * @return Page<Market> 返回类型 
	 * @throws
	 */
    public Page<Market> searchMarketList(PageRequest<Map<String, String>> pageRequest){
		
		return marketDao.searchMarket(pageRequest);
	}
    
    
    /**
     * 查找审核状态的标
     * @Title: queryVerifyBorrowLogByStatus 
     * @Description: TODO
     * @param @param optype
     * @param @param borrrowId
     * @param @return 设定文件 
     * @return VerifyBorrowLog 返回类型 
     * @throws
     */
    public VerifyBorrowLog queryVerifyBorrowLogByStatus(int optype, long borrrowId){
        
        VerifyBorrowLog vb = new VerifyBorrowLog();
        
        switch (optype) {
        case 1:
            
            vb = verifyBorrowLogDao.queryVerifyByF(borrrowId);
            
            break;
        case 3:
            
            vb = verifyBorrowLogDao.queryVerifyByD(borrrowId);
            
            break;
        default:
            break;
        }
        
        return vb;
    }
	
    
    /**
     * @throws ParseException 
     * 计算待收收益和剩余天数
     * @Title: comCollectionMoney 
     * @Description: TODO
     * @param @param borrow
     * @param @param market
     * @param @return 设定文件 
     * @return Map<String,String> 返回类型 
     * @throws
     */
    public Map<String, String> comCollectionMoney(Borrow borrow, BorrowTender tender) throws ParseException{

        VerifyBorrowLog vb = queryVerifyBorrowLogByStatus(1, borrow.getId());
        
        Map<String,String> reparams = new HashMap<String,String>();
        double summonye=0d;
        // 天标处理
        if(borrow.getIsDay()==1){
            
            long totalday = DateUtils.dateadd(vb.getCreatedAt(),borrow.getTimeLimit());
            
            int remaindays = DateUtils.daysBetween(DateUtils.getNowTimeStr(), totalday);
            
            reparams.put("remaindays", String.valueOf(remaindays));
            
            double collectionMoney = (Double.valueOf(remaindays)/Double.valueOf(borrow.getTimeLimit()))*tender.getRepaymentAccount();

            reparams.put("collectionMoney", String.valueOf(collectionMoney));
            
            reparams.put("repayOrder", "1");
            reparams.put("repayTotalOrder", "1");

            
        //一次性还款的标处理 
        }else if(borrow.getRepaymentStyle()== Constant.BORROW_REPAYMENT_STYLE_DQHBHX){ 
            
            long totalday = DateUtils.dateadd(vb.getCreatedAt(),borrow.getTimeLimit()*30);
            
            int remaindays = DateUtils.daysBetween(DateUtils.getNowTimeStr(), totalday);
            
            reparams.put("remaindays", String.valueOf(remaindays));
            
            double collectionMoney = (Double.valueOf(remaindays)/Double.valueOf(borrow.getTimeLimit()))*30*tender.getRepaymentAccount();
            
            reparams.put("collectionMoney", String.valueOf(collectionMoney));

            reparams.put("repayOrder", "1");
            reparams.put("repayTotalOrder", "1");

        //等额本息的标处理
        }else if(borrow.getRepaymentStyle()== Constant.BORROW_REPAYMENT_STYLE_DEBX){ 
            
            long totalday = DateUtils.dateadd(vb.getCreatedAt(),borrow.getTimeLimit()*30);
            
            int remaindays = DateUtils.daysBetween(DateUtils.getNowTimeStr(), totalday);
            
            reparams.put("remaindays", String.valueOf(remaindays));
            
//            double collectionMoney = (Double.valueOf(remaindays)/Double.valueOf(borrow.getTimeLimit()))*tender.getRepaymentAccount();
            
            
            
            List<BorrowCollection> collection = borrowCollectionDao.queryPeriodByStatus(tender.getId(), Constant.BORROW_COLLECTION_STATUS_XJ);
            for (BorrowCollection borrowCollection : collection) {
            	summonye=summonye+borrowCollection.getRepayAccount();
			}
            reparams.put("collectionMoney", String.valueOf(summonye));
            reparams.put("repayOrder", String.valueOf(collection.size()));
            reparams.put("repayTotalOrder", String.valueOf(borrow.getTimeLimit()));
          //按月付息到期还本
        }else if(borrow.getRepaymentStyle()== Constant.BORROW_REPAYMENT_STYLE_MYHX){
            long totalday = DateUtils.dateadd(vb.getCreatedAt(),borrow.getTimeLimit()*30);
            
            int remaindays = DateUtils.daysBetween(DateUtils.getNowTimeStr(), totalday);
            
            reparams.put("remaindays", String.valueOf(remaindays));
            
//            double collectionMoney = (Double.valueOf(remaindays)/Double.valueOf(borrow.getTimeLimit()))*tender.getRepaymentAccount();
           
            List<BorrowCollection> collection = borrowCollectionDao.queryPeriodByStatus(tender.getId(), Constant.BORROW_COLLECTION_STATUS_XJ);
            for (BorrowCollection borrowCollection : collection) {
            	summonye=summonye+borrowCollection.getRepayAccount();
			}
            reparams.put("collectionMoney", String.valueOf(summonye));
//            reparams.put("repayOrder", String.valueOf(collection.get(0).getColOrder()+1));
            reparams.put("repayOrder", String.valueOf(collection.size()));
            reparams.put("repayTotalOrder", String.valueOf(borrow.getTimeLimit()));
        }
        
        return reparams;
        
    }
    
    
    /**
     * 创建新转让市场记录
     * @Title: createNewMarket 
     * @Description: TODO
     * @param @param market
     * @param @return 设定文件 
     * @return Long 返回类型 
     * @throws
     */
    public Long createNewMarket(Market market){
        
        return (Long) marketDao.save(market);
    }
    
    /**
     * 
     * @Title: queryByTenderId 
     * @Description: TODO
     * @param @param tid
     * @param @return 设定文件 
     * @return List<Market> 返回类型 
     * @throws
     */
    public List<Market> queryByTenderId(Long tid){
        
        return marketDao.queryByTenderId(tid);
    }
    
    
    /**
     * ID获取转让记录
     * @Title: queryById 
     * @Description: TODO
     * @param @param id
     * @param @return 设定文件 
     * @return Market 返回类型 
     * @throws
     */
    public Market queryMarketById(long id){
        
        return marketDao.queryMarketById(id);
    }
    
    public void updateMarket(long m){
    	marketDao.updateMarket(m);
    }
    public void updateMarket2(long m){
    	marketDao.updateMarket2(m);
    }
    
    /**
     * 
     * @Title: createNewDebt 
     * @Description: TODO
     * @param @param dtf
     * @param @return 设定文件 
     * @return Long 返回类型 
     * @throws
     */
    public Long createNewDebt(DebtTransfer dtf){
        
        return (Long) debtTransferDao.save(dtf);
    }
    
    
    public DebtTransfer getDebtTransferById(long id){
    	return debtTransferDao.getDebtTransferByid(id);
    }
    public Page<DebtTransfer> searchDebtTransfer(PageRequest<Map<String, String>> pageRequest){
    	return debtTransferDao.searchDebtTransfer(pageRequest);
    }
    /**
     * 接受债权
     * @Title: acceptDebt 
     * @Description: TODO
     * @param @param market
     * @param @param user
     * @param @return 设定文件 
     * @return boolean 返回类型 
     * @throws
     */
    public boolean acceptDebt(Market market, User user){
        
        UserAccount ua = userAccountDao.queryByUserId(user.getId());
        
        if(ua!=null && ua.getMoneyUsable() > market.getTransferPrice()){
            
            /** 扣除可用金额 */
            DebtTransfer dt = new DebtTransfer();
            
            dt.setTenderId(market.getTenderId());
            dt.setTenderUserId(market.getTenderUserId());
            dt.setReceiveUserId(user.getId());
            dt.setBorrowId(market.getBorrowId());
            dt.setTransferPrice(market.getTransferPrice());
            dt.setAdvanceInterest(market.getCollectionMoney());
            dt.setCreatedAt(DateUtils.getNowTimeStr());
            dt.setCreatedBy(user.getId());
        }
        
        return true;
        
    }
    
    public List<Market> getMardketListByStatus(int status){
    	return marketDao.getMardketListByStatus(status);
    }
    
    public void delMarketByTenderId(long tenderId)
    {
    	marketDao.delMarketByTenderId(tenderId);
    }
    
    /**
	 * 转让市场列表
	 * @Title: searchMarketList 
	 * @Description: TODO
	 * @param @param pageRequest
	 * @param @return 设定文件 
	 * @return Page<Market> 返回类型 
	 * @throws
	 */
    public Page<Market> searchMarketList2(PageRequest<Map<String, String>> pageRequest){
		
		return marketDao.searchMarket2(pageRequest);
	}


	public List<Market> findMarketByBorrowIdAndStatus(Long borrowId, int status) {
		// TODO Auto-generated method stub
		return marketDao.findMarketByBorrowIdAndStatus(borrowId,status);
	}


	
}
