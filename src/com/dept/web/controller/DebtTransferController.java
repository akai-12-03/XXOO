package com.dept.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dept.web.context.Constant;
import com.dept.web.context.Global;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowRepayment;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.DebtTransfer;
import com.dept.web.dao.model.Market;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.TextUtil;
import com.dept.web.general.util.tools.iphelper.IPUtils;
import com.dept.web.service.BorrowCollectionService;
import com.dept.web.service.BorrowService;
import com.dept.web.service.BorrowTenderService;
import com.dept.web.service.TransferService;
import com.dept.web.service.UserAccountLogService;
import com.dept.web.service.UserAccountService;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;
import com.sendinfo.xspring.ibatis.page.PageUtils;

/**
 * 债权转让
 * 
 * @ClassName:     DebtTransferController
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年6月25日 上午10:02:10 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class DebtTransferController extends WebController{

    @Autowired
    private TransferService transferService;
    
    @Autowired
    private BorrowService borrowService;
    
    @Autowired
    private UserAccountService userAccountService;
    
    @Autowired
    private UserAccountLogService userAccountLogService;
    
    @Autowired
    private BorrowTenderService borrowTenderService;
    @Autowired
    private BorrowCollectionService borrowCollectionService;
    
    /**
     * 转让市场首页
     * @Title: search 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
	@RequestMapping("market/search")
	public String search(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
	    
	    Map<String, String> params = getParamMap(request);
	    PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
	    populate(pageRequest, request);
	    pageRequest.setPageSize(10);
	        
        if(StringUtil.isNotEmpty(params.get("page"))){
            
            pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
        }
        
        createSearchParams(params);
        
        pageRequest.setFilters(params);
        int zring =0;//转让中的项目个数
        int zred=0;//转让完成的项目个数
        List<Market> marketlist1=new ArrayList<Market>();
		try {
			marketlist1 = transferService.getMardketListByStatus(0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        if(marketlist1!=null&&marketlist1.size()>0){
        	zring=marketlist1.size();
        }
        List<Market> marketlist2= transferService.getMardketListByStatus(1);
        if(marketlist2!=null&&marketlist2.size()>0){
        	zred=marketlist2.size();
        }
        try {
            Page<Market> marketPage = transferService.searchMarketList(pageRequest);
            map.addAttribute("marketPage", marketPage);
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(marketPage.getTotalCount(), marketPage.getPageSize()));
            map.addAttribute("page",pageRequest.getPageNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.addAttribute("zring", zring);
        map.addAttribute("zred", zred);
        map.addAttribute("xmzt",TextUtil.isNull(params.get("xmzt")));
        map.addAttribute("xmqx",TextUtil.isNull(params.get("xmqx")));
        map.addAttribute("tzsy",TextUtil.isNull(params.get("tzsy")));
        map.addAttribute("bzfs",TextUtil.isNull(params.get("bzfs")));
        map.addAttribute("xmlx",TextUtil.isNull(params.get("xmlx")));
        map.addAttribute("msg", request.getParameter("msg"));
	    return "borrow/assignment";
	}
	
	
	 /**
     * 创建搜索参数
     * @Description:  TODO
     * @param:        @param params
     * @param:        @return   
     * @return:       Map<String,String>   
     * @throws
     */
    public Map<String, String> createSearchParams(Map<String, String> params){
        
        String xmzt = TextUtil.isNull(params.get("xmzt"));//项目状态
        
        String xmqx = TextUtil.isNull(params.get("xmqx"));//项目期限
        
        String tzsy = TextUtil.isNull(params.get("tzsy"));//投资收益
        
        String bzfs = TextUtil.isNull(params.get("bzfs"));//保障方式
        
        String xmlx = TextUtil.isNull(params.get("xmlx"));//项目类型
        
        /**
         * 项目状态
         * 
         */
        if(xmzt.equals("1")){
            params.put("xmztsql", " A.STATUS =0");  
        }else if(xmzt.equals("2")){
            params.put("xmztsql", " A.STATUS=1");
        }
        
        
        /**
         * 项目期限
         */
        if(xmqx.equals("1")){
            params.put("xmqxsql", " A.remaining_days <= 1*30"); 
        }else if(xmqx.equals("2")){
            params.put("xmqxsql", " A.remaining_days > 1*30 AND A.remaining_days <=3*30 ");
        }else if(xmqx.equals("3")){
            params.put("xmqxsql", " A.remaining_days > 3*30 AND A.remaining_days <=6*30");
        }else if(xmqx.equals("4")){
            params.put("xmqxsql", " A.remaining_days > 6*30 AND A.remaining_days<=12*30 ");
        }else if(xmqx.equals("5")){
            params.put("xmqxsql", " A.remaining_days > 12*30 ");
        }
        
//    	/**
//		 * 理财期限
//		 */
//		if(xmqx.equals("1")){
//			params.put("xmqxsql", "CASE WHEN A.isDay!=1 THEN A.TIME_LIMIT <=1  ELSE a.time_limit<=31 END");	
//		}else if(xmqx.equals("2")){
//			params.put("xmqxsql", "CASE WHEN A.isDay!=1 THEN A.TIME_LIMIT >1 AND a.time_limit<=3 ELSE a.time_limit>31 AND a.time_limit<=90 END");
//		}else if(xmqx.equals("3")){
//			params.put("xmqxsql", "CASE WHEN A.isDay!=1 THEN A.TIME_LIMIT >3  AND a.time_limit<=6 ELSE a.time_limit>90 AND a.time_limit<=180 END");
//		}else if(xmqx.equals("4")){
//			params.put("xmqxsql", "CASE WHEN A.isDay!=1 THEN A.TIME_LIMIT >6  AND a.time_limit<=12 ELSE a.time_limit>180 AND a.time_limit<=360 END");
//		}else if(xmqx.equals("5")){
//			params.put("xmqxsql", "CASE WHEN A.isDay!=1 THEN A.TIME_LIMIT >12  ELSE a.time_limit>90 AND a.time_limit>360 END");
//		}
        
        
        /**
         * 投资收益
         */
        if(tzsy.equals("1")){
            params.put("tzsysql", "A.BORROW_APR >=5 AND A.BORROW_APR < 7 ");   
        }else if(tzsy.equals("2")){
            params.put("tzsysql", "A.BORROW_APR >=7 AND A.BORROW_APR < 10");
        }else if(tzsy.equals("3")){
            params.put("tzsysql", "A.BORROW_APR >=10 AND A.BORROW_APR <= 15");
        }
        
        /**
         * 保障方式
         */
        if(bzfs.equals("1")){
            params.put("bzfssql", "b.borrow_use =1");   
        }else if(bzfs.equals("2")){
            params.put("bzfssql", "b.borrow_use =2");
        }else if(bzfs.equals("3")){
            params.put("bzfssql", "b.borrow_use =3");
        }else if(bzfs.equals("4")){
            params.put("bzfssql", "b.borrow_use =4");
        }
        
        /**
         * 项目类型
         */
        if(xmlx.equals("1")){
            params.put("xmlxsql", "b.borrow_type=1");   
        }else if(xmlx.equals("2")){
            params.put("xmlxsql", "b.borrow_type=2");
        }else if(xmlx.equals("3")){
            params.put("xmlxsql", "b.borrow_type=3");
        }else if(xmlx.equals("4")){
            params.put("xmlxsql", "b.borrow_type=4");
        }
        else if(xmlx.equals("5")){
            params.put("xmlxsql", "b.borrow_type=5");
        }
        return params;
    }
    
    
    /**
     * 放入转让市场
     * @Title: addmarket 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @param tid
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("myhome/addmarket")
    public String addmarket(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{

        User user = getCurrUser(request, response);
         String tid=request.getParameter("id");
         long id=0l;
         if(tid!=null&&!"".equals(tid)){
        	 id=Long.parseLong(tid);
         }
        String newprice2=request.getParameter("newprice");
        double newprice=0l;
        if(newprice2!=null&&!"".equals(newprice2)){
        	newprice=Double.parseDouble(newprice2);
        	if(newprice>1000000000)
        	{
        		map.addAttribute("msg", "转让价格过高，请重新设置");
                
                return "to_success"; 
        	}
        }
        
        BorrowTender tender = borrowService.queryTenderById(id);
        
        Borrow borrow = borrowService.getBorrowById(tender.getBorrowId());
        Long marketid=0l;
        if(borrow.getStatus()==Constant.BORROW_STATUS_HKZ){//借款标_还款中 =5
        	
        	if(borrow.getRepaymentStyle()==1)//等额本息
        	{
	        	List<BorrowRepayment> repaymentYesList=borrowService.getRepaymentByBorrowId(borrow.getId(),Constant.BORROW_REPAYMENT_STATUS_YH);
	        	if(repaymentYesList!=null&&!repaymentYesList.isEmpty())
	        	{
	        		
	        			map.addAttribute("msg", "该标已经还款过，等额本息标不支持分期转让");
	                    
	                    return "to_success"; 
	        	}
        	}
           
        	List<BorrowRepayment> repaymentList=borrowService.getRepaymentByBorrowId(borrow.getId(),Constant.BORROW_REPAYMENT_STATUS_XJ);
        	if(repaymentList!=null&&!repaymentList.isEmpty())
        	{
        		BorrowRepayment repay=repaymentList.get(0);
        		long nowTime=System.currentTimeMillis()/1000;
        		long repayTime=repay.getRepaymentTime();
        		if(repayTime<nowTime)
        		{
        			map.addAttribute("msg", "该标已经逾期,不能转让");
                    
                    return "to_success"; 
        		}
        	}
        	
            if(tender.getUserId().equals(user.getId())){
                
                if(tender.getStatus()==1){

                    List<Market> mlist = transferService.queryByTenderId(tender.getId());
                    
                    if(mlist.size()>0){
                        
                        map.addAttribute("msg", "该投资已在转让市场中");
                        
                        return "to_success"; 
                    }
                    
                   
					try {
						Market market = new Market();
						market.setBorrowId(borrow.getId());
						market.setTenderId(tender.getId());
						market.setTenderUserId(tender.getUserId());
						market.setTenderPrice(tender.getAccount());
						market.setTransferPrice(newprice);
						Map<String,String> markparams = transferService.comCollectionMoney(borrow, tender);
						market.setCollectionMoney(Double.valueOf(markparams.get("collectionMoney")));
						market.setRemainingDays(Integer.valueOf(markparams.get("remaindays")));
						market.setRepayOrder(Integer.valueOf(markparams.get("repayOrder")));
						market.setRepayTotalOrder(Integer.valueOf(markparams.get("repayTotalOrder")));
						market.setBorrowApr(borrow.getApr());
						market.setBorrowName(borrow.getName());
						market.setStatus(0);
						market.setCreatedAt(DateUtils.getNowTimeStr());
						market.setCreatedBy(user.getId());
						
						marketid = transferService.createNewMarket(market);
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    
                    if(marketid==null){
                        
                        map.addAttribute("msg", "放入转让市场失败");
                        
                        return "to_success";
                        
                    }else{
                    	tender.setTransfer(1);//设置成转让状态
                        borrowTenderService.updateBorrowTender(tender);
                        map.addAttribute("msg", "放入转让市场成功");
                        
                        return "to_success"; 
                        
                    }                    
                }
                
            }
        }else{
            
            map.addAttribute("msg", "该投资暂不能转让");
            return "to_success"; 
            
        }

        return "to_success";
    }
    
    
    /**
     * 用户中心我的可转让市场
     * @Title: marketsearch 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("myhome/tenderSearch")
    public String tenderSearch(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        
        User user = getCurrUser(request, response);
        
        PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
        populate(pageRequest, request);
        pageRequest.setPageSize(10);
            
        if(StringUtil.isNotEmpty(params.get("page"))){
            
            pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
        }
        
        params.put("userId", String.valueOf(user.getId()));
        params.put("type", "1");
        params.put("market", "1");
        pageRequest.setFilters(params);

        try {
        	Page<BorrowTender> borrowPage=borrowService.getSuccessListByUserid(pageRequest);
        	for(BorrowTender tender:borrowPage.getResult())
        	{
        		Borrow borrow=borrowService.getBorrowById2(tender.getBorrowId());
        		Map<String,String> markparams = transferService.comCollectionMoney(borrow, tender);
				tender.setRemainingDays(Integer.valueOf(markparams.get("remaindays")));
				tender.setRepayOrder(markparams.get("repayOrder"));
        	}
        	map.addAttribute("borrowPage", borrowPage);
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowPage.getTotalCount(), borrowPage.getPageSize()));
            map.addAttribute("page",pageRequest.getPageNumber());
            map.addAttribute("msg", params.get("msg"));
            map.addAttribute("mt", 31);
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "myhome/assignment";
    }
    
    


    /**
     * 用户中心我的转让市场
     * @Title: marketsearch 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("myhome/marketsearch")
    public String marketsearch(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        
        User user = getCurrUser(request, response);
        
        PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
        populate(pageRequest, request);
        pageRequest.setPageSize(10);
            
        if(StringUtil.isNotEmpty(params.get("page"))){
            
            pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
        }
        
        params.put("userId", String.valueOf(user.getId()));
        
        pageRequest.setFilters(params);

        try {
            Page<Market> marketPage = transferService.searchMarketList2(pageRequest);
            map.addAttribute("marketPage", marketPage);
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(marketPage.getTotalCount(), marketPage.getPageSize()));
            map.addAttribute("page",pageRequest.getPageNumber());
            map.addAttribute("msg", params.get("msg"));
            map.addAttribute("mt", 32);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "myhome/assignmentMarcket";
    }
    
    /**
     * 用户中心我的转让市场
     * @Title: marketsearch 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("myhome/debtTransferSearch")
    public String debtTransferSearch(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        
        User user = getCurrUser(request, response);
        
        PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
        populate(pageRequest, request);
        pageRequest.setPageSize(10);
            
        if(StringUtil.isNotEmpty(params.get("page"))){
            
            pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
        }
        
        params.put("userId", String.valueOf(user.getId()));
        
        pageRequest.setFilters(params);

        try {
            
            Page<DebtTransfer> deptTransferPage= transferService.searchDebtTransfer(pageRequest);
            map.addAttribute("deptTransferPage", deptTransferPage);
            
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(deptTransferPage.getTotalCount(), deptTransferPage.getPageSize()));
            map.addAttribute("page",pageRequest.getPageNumber());
            map.addAttribute("msg", params.get("msg"));
            map.addAttribute("mt", 33);
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "myhome/assignmentDebtTransfer";
    }
    
    
    
    
    /**
     * 接受
     * @Title: acpmarket 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @param marketId
     * @param @throws Exception 设定文件 
     * @return void 返回类型 
     * @throws
     */
    @RequestMapping("dept/acpmarket")
    public String acpmarket(ModelMap map,HttpServletRequest request, HttpServletResponse response, long marketId) throws Exception{
     
        try {
			User user = getCurrUser(request, response);
//			MD5 md5=new MD5();
//			String payPassword=request.getParameter("paypassword");
			if(user==null){
			    
//            out(response, 5);  //未登录
				return "redirect:/tologin.html";
			}else{
//				if(!user.getPasswordPayHash().equals(md5.getMD5ofStr(payPassword))){
//					 map.addAttribute("msg", "交易密码错误");
//		                return "to_success"; 
//				}
				
				if(user.getAutoType()==null||user.getAutoType()!=1)
				{
					MoneymoremoreId=user.getMoneymoremoreId();
					return "redirect:/qddApi/loanAuthorize.html?MoneymoremoreId="+MoneymoremoreId;
//					map.addAttribute("errormsg", "您未授权请先进行授权操作");
//					map.addAttribute("user", user);
//					return "myhome/openAuth";
				}
				
				UserAccount ua = userAccountService.getAccount(user.getId());
				
			    Market market = transferService.queryMarketById(marketId);
			    Borrow borrow = borrowService.getBorrowById(market.getBorrowId());
			    if(market!=null && market.getStatus()==0){
			        
			    	if(ua!=null && ua.getMoneyUsable()>=market.getTransferPrice()){
			    		
			    		//1为借款标
			    		if(borrow.getBorrowType()==1)
			    		{
			    			if(borrow.getReceivePerson()!=null)
			    			{
			    				if(borrow.getReceivePerson().equals(user.getId()))
			    				{
			    					 	map.addAttribute("msg", "收款人不能购买债权转让产品");
						                return "to_success"; 
			    				}
			    			}
			    			
			    		}
			            if(user.getId()==market.getTenderUserId()||user.getId().equals(market.getTenderUserId())){
			           	 map.addAttribute("msg", "不能购买自己的转让产品");
			                return "to_success"; 
			            }
			            if(user.getId()==borrow.getUserId()||user.getId().equals(borrow.getUserId())){
			            	 map.addAttribute("msg", "不能购买自己的借款产品");
				                return "to_success"; 	
			            }
							
							
							
			                return "redirect:/qddApi/marketOrderNo.html?tenderId="+market.getTenderId()+"&tendermoney="+market.getTransferPrice();

			        }else{
			        	map.addAttribute("msg", "可用余额不足");
			        }
			        
			    }else{
			        map.addAttribute("msg", "该投资不能被转让");
//                out(response, 2);  //该投资不能被转让
			    	return "to_success";
			    }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return "to_success";
    }
    
    /**
     * 取消转让市场
     * @Title: addmarket 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @param tid
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("myhome/delMarket")
    public String delMarket(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{

        String tenderId=request.getParameter("tenderId");
        if(tenderId!=null)
        {
        	transferService.delMarketByTenderId(Long.valueOf(tenderId));
        	BorrowTender borrowTender=borrowTenderService.getUserBorrowTenderById(Long.valueOf(tenderId));
        	borrowTender.setTransfer(0);//状态改为未转让状态
        	borrowTenderService.updateBorrowTender(borrowTender);
        }
        return "redirect:/market/search.html";
    }
    
    public String directMoneyToPerson(Double money,Long inUserId,Long outUserId)
	{
		String webUrl = Global.getValue("weburl");
		String urls = webUrl+"/qddApi/giveMoneyPersonToPerson.html?inid="+inUserId+"&outid="+outUserId+"&money="+money;
		URL getUrl;
		BufferedReader reader =null;
		HttpURLConnection connection=null;
		String str=null;
			try {
				getUrl = new URL(urls);
				connection = (HttpURLConnection) getUrl.openConnection();
		          // 建立与服务器的连接，并未发送数据
		         connection.connect();
		          // 发送数据到服务器并使用Reader读取返回的数据
		        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		        str=reader.readLine();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
			
	          try {
				reader.close();
				connection.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	          // 断开连接
			}
			return str;
	}
    
}