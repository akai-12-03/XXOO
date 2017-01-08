
package com.dept.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dept.web.dao.model.HongbaoLog;
import com.dept.web.dao.model.Hongbao;
import com.dept.web.dao.model.PlanAppendInsure;
import com.dept.web.dao.model.PlanRate;
import com.dept.web.dao.model.PlanRecord;
import com.dept.web.dao.model.SystemInfo;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.general.util.NewDateUtils;
import com.dept.web.general.util.NumberUtil;
import com.dept.web.general.util.tools.iphelper.IPUtils;
import com.dept.web.service.PlanRateService;
import com.dept.web.service.PlanRecordService;
import com.dept.web.service.PlanService;
import com.dept.web.service.SystemService;
import com.dept.web.service.UserAccountLogService;
import com.dept.web.service.UserAccountService;
import com.dept.web.service.UserService;

/**
 * 配资记录
 * @author Administrator
 *
 */
@SuppressWarnings("all")
@Controller
public class PlanController extends WebController{

	@Autowired
	private PlanRecordService planRecordService;
	@Autowired
	private PlanRateService planRateService;
	
	@Autowired
	private PlanService planService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserAccountLogService userAccountLogService;
	
	/**
	 * 推广联盟
	 * @Title: index 
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("plan/tglm")
    public String tglm_action(ModelMap map,HttpServletRequest request, HttpServletResponse response){
           
	    return "action";
    }
	
	/**
	 * 配资计划首页
	 * @Title: index 
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("plan/index")
    public String index(ModelMap map,HttpServletRequest request, HttpServletResponse response){
           
	    return "plan/fund_Index";
    }
	/**
	 * 新手体验
	 * @Title: index 
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("plan/feeling")
    public String feeling(ModelMap map,HttpServletRequest request, HttpServletResponse response){
           
	    return "plan/freeEnjoy";
    }
	
	/**
	 * 配资利率
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("plan/month")
	public String planmonth(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		 
	    Map<String, String> params = getParamMap(request);
	    
	    User user = getCurrUser(request, response);
	    
	    String opstr = params.get("opt");
	    
	    if(StringUtils.isEmpty(opstr)){
	        
	        return "plan/fund_month";
	        
	    }else if(opstr.equals("add")){
	        
	        if(user==null){
	            
	            return "redirect:../tologin.html"; 
	        }
	        
            if(user.getRealVerifyStatus()!=null && user.getRealVerifyStatus()==1){
                
                if(Double.valueOf(params.get("lilv"))==0){
                    
                    map.addAttribute("msg", "该金额不在配资范围内");
                    
                    return "plan/fund_month";
                }
                
                PlanRate pr = new PlanRate();
                pr.setType(2);  //按月配资
                pr.setAmount(Double.valueOf(params.get("amount")));
                pr.setInterval(Integer.valueOf(params.get("interval")));
                pr.setRate(Double.valueOf(params.get("lilv")));
                pr.setStatus(0);  //新建
                pr.setUserCreate(user.getId());
                pr.setCreatedAt(NewDateUtils.getNowTimeStr());
                Long pid = planService.createPlanRate(pr);
                
                PlanRecord precord = new PlanRecord();
                
                precord.setUserId(user.getId());
                precord.setPlanId(pid);
                precord.setPlanType(2L); //按月配资2 按天配资1
                precord.setMoneyInsure(Double.valueOf(params.get("moneyInsure")));
                precord.setPower(Double.valueOf(params.get("power")));
                precord.setInterval(Integer.valueOf(params.get("interval")));
                precord.setRate(Double.valueOf(params.get("lilv")));
                precord.setMoneyFee(0.00);
                precord.setMoneyWarning(Double.valueOf(params.get("ksjjx")));
                precord.setMoneyForce(Double.valueOf(params.get("kspcx")));
                precord.setMoneyOp(Double.valueOf(params.get("amount")));
                precord.setMoneySettlement(0.00);  //结算金
                precord.setStatus(9);  //状态(-1 删除；0 新建；1 批准; 9 未支付)
                precord.setUserId(user.getId());
                precord.setCreatedAt(NewDateUtils.getNowTimeStr());
                
                Long prid = planService.createPlanRecord(precord);
                
                return "redirect:plan_pay.html?opt=view&prid="+prid;
                
            }else{
                
                map.addAttribute("msg", "请先进行实名认证");
                
                return "plan/fund_month"; 
            }
	        
	    }
	    
	    return "plan/fund_month";
	}

	
	/**
	 * 支付配资记录
	 * @Title: planday 
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @param opt
	 * @param @param prid
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
    @RequestMapping("plan/plan_pay")
    public String planday(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam String opt, @RequestParam long prid) throws Exception{
               
        try {
            
        User user = getCurrUser(request, response);
        
        if(user==null){
            
            return "redirect:../tologin.html"; 
            
        }else{
            
                map.addAttribute("user", user);
                
                PlanRecord pr = planService.queryRecordById(prid);
                
                if(user.getId().equals(pr.getUserId())){
                    
                    UserAccount userac = userAccountService.getUserAccount(user.getId());
                    
                    double sylx = pr.getMoneyInsure()*pr.getPower()*pr.getRate()/100*pr.getInterval(); //月利息
                    
                    double glf = pr.getMoneyFee();
                    
                    double totalpay = pr.getMoneyInsure()+sylx;
                    
                    if(pr.getPlanType().equals(1L)){
                        
                        totalpay = pr.getMoneyInsure()+glf;
                        
                    //免费体验   
                    }else if(pr.getPlanType().equals(3L)){
                        
                        totalpay = pr.getMoneyInsure();
                    }
                    
                    if(pr!=null && opt.equals("view")){
                        
                        map.addAttribute("fxbzj", NumberUtil.ceil(pr.getMoneyInsure(),0));
                        map.addAttribute("ptype", String.valueOf(pr.getPlanType()));
                        
                        if(pr.getPlanType().equals(1L)){
                            
                            map.addAttribute("glf", NumberUtil.ceil(pr.getMoneyFee(),0));
                            
                        //免费体验
                        }else if(pr.getPlanType().equals(3L)){
                            
                            map.addAttribute("glf", 0);
                        }
                        
                        if(totalpay>userac.getMoneyUsable()){
                            
                            map.addAttribute("needpay", NumberUtil.ceil(totalpay-userac.getMoneyUsable(),2));
                        }
                        
                        map.addAttribute("sylx", NumberUtil.ceil(sylx,2));
                        map.addAttribute("totalpay", NumberUtil.ceil(totalpay,2));
                        
                        map.addAttribute("prid", prid);
                        
                        map.addAttribute("userac", userac);
//                        加载用户的可使用红包信息
                        Map maps=new HashMap();
                        maps.put("userId",user.getId() );
                        maps.put("status", "1");
                        List<HongbaoLog> hongbaoList=userService.getListHongbaoLogByIDAndStatus(maps);
                        map.addAttribute("hongbaoList", hongbaoList);
                       
                        return "plan/plan_pay";
                        
                    }else if(pr!=null && opt.equals("pay")){
                        //通过实名认证
                        if(user.getRealVerifyStatus()!=null && user.getRealVerifyStatus()==1){
                        	
                        	//一元体验时 直接比较可用金额是否大于支付金额
                        	//有红包消息 说明书属于按天支付或者按月支付
                        	
                         String hongbaoId=request.getParameter("hongbao");
//                    	 如果用户使用了红包
                         if(hongbaoId!="-1" && !("-1").equals(hongbaoId )){
//                        	 根据红包ID获取红包信息
                        	 HongbaoLog hongbao=userService.getHongbaoLogById(Long.parseLong(hongbaoId));
                        	 double hongbaoMoney=Double.parseDouble(hongbao.getMoney().toString());
                        	 
                        	 if(totalpay>userac.getMoneyUsable()+hongbaoMoney){
                        		 out(response, 2); //余额不足
                        	 }else{
                        		 if(pr.getStatus()==9){
	                        		 //余额充足 先扣除红包里面的金额
	//                        		 修改红包的状态   
                        			 try{
	                        		 hongbao.setStatus(2);
	                        		 hongbao.setUpdateAt(NewDateUtils.getNowTimeStr());
	                        		 hongbao.setUpdateBy(Integer.parseInt(user.getId().toString()));
	                        		 userService.updateHongbaoLog(hongbao);
	                        		 
//	                        		 //添加红包的使用记录
//	                        		 Hongbao newhongbaoPlan=new Hongbao();
//	                        		 newhongbaoPlan.setUserId(user.getId());
//	                        		 newhongbaoPlan.setHongbaoId(Long.parseLong(hongbaoId));
//	                        		 newhongbaoPlan.setPlanRecordId(pr.getId());
//	                        		 newhongbaoPlan.setCreatedAt(NewDateUtils.getNowTimeStr());
//	                        		 newhongbaoPlan.setCreatedBy(Integer.parseInt(user.getId().toString()));
//	                        		 newhongbaoPlan.setHongbaoStatus(2);
//	                        		 newhongbaoPlan.setPlanRecordStatus(0);
//	                        		 userService.createNewPlan(newhongbaoPlan);
	                        		 
                        		//如果红包够支付保证金 就不用扣除可用余额
	                        		 if(totalpay<=hongbaoMoney){
//	                        			 try{
	                        			 //
	                        			 /** 更新配资记录表状态 */
                                         planRecordService.updateRecordStatus(0, pr.getId());
                                         
                                         /** 更新资金 */
                                         userac.setMoneyUsable(userac.getMoneyUsable());//可用余额不改变
                                         userac.setMoneyInsure(userac.getMoneyInsure()+hongbaoMoney);//冻结红包的钱
                                         userac.setUpdatedAt(NewDateUtils.getNowTimeStr());
                                         
                                         userAccountService.updateUserAccount(userac);
                                         
                                         //新增资金记录
                                         UserAccount useracc = userAccountService.getUserAccount(user.getId());
                                         
                                         //新建资金记录 两个
                                         UserAccountLog ual = new UserAccountLog();
                                         ual.setUserId(useracc.getUserId());
                                         ual.setType(1);
                                         ual.setMoneyOperate(pr.getMoneyInsure());
                                         ual.setMoneyTotal(useracc.getMoneyTotal());
                                         ual.setMoneyUsable(useracc.getMoneyUsable());
                                         ual.setMoneyWithdraw(useracc.getMoneyWithdraw());
                                         ual.setMoneyInsure(useracc.getMoneyInsure());
                                         ual.setRemark("为配资记录编号"+pr.getId()+"的支付保证金" + pr.getMoneyInsure()+"元,此次全额使用红包券支付"+hongbaoMoney);
                                         ual.setCreatedAt(NewDateUtils.getNowTimeStr());
                                         ual.setCreatedIp(IPUtils.getRemortIP(request));
                                         ual.setMoneyCollection(useracc.getMoneyCollection());
                                         ual.setMoneyTenderFreeze(useracc.getMoneyTenderFreeze());
                                         
                                         userAccountLogService.insertUserAccountLog(ual); 
                                         
                                         
                                         /**
                                          * 管理费
                                          */
                                         /** 更新资金 */
                                         UserAccount feeuseracc = userAccountService.getUserAccount(user.getId());
                                         
                                         if(pr.getPlanType().equals(1L)){
                                             feeuseracc.setMoneyUsable(userac.getMoneyUsable()-glf); 
                                             feeuseracc.setMoneyTotal(userac.getMoneyTotal()-glf);
                                         }else{
                                             feeuseracc.setMoneyUsable(userac.getMoneyUsable()-sylx); 
                                             feeuseracc.setMoneyTotal(userac.getMoneyTotal()-sylx);
                                         }
                                         feeuseracc.setUpdatedAt(NewDateUtils.getNowTimeStr());
                                         
                                         userAccountService.updateUserAccount(feeuseracc);
                                         
                                         //新增资金记录
                                         UserAccount newfeeuseracc = userAccountService.getUserAccount(user.getId());
                                         
                                         //新建资金记录 两个
                                         UserAccountLog feeual = new UserAccountLog();
                                         feeual.setUserId(newfeeuseracc.getUserId());
                                         feeual.setType(1);
                                         if(pr.getPlanType().equals(1L)){
                                             feeual.setMoneyOperate(glf);
                                             feeual.setRemark("为配资记录编号"+pr.getId()+"的支付管理费" + glf+"元");
                                         }else{
                                             feeual.setMoneyOperate(sylx);
                                             feeual.setRemark("为配资记录编号"+pr.getId()+"的支付月利息" + sylx+"元");                                           
                                         }
                                         feeual.setMoneyTotal(newfeeuseracc.getMoneyTotal());
                                         feeual.setMoneyUsable(newfeeuseracc.getMoneyUsable());
                                         feeual.setMoneyWithdraw(newfeeuseracc.getMoneyWithdraw());
                                         feeual.setMoneyInsure(newfeeuseracc.getMoneyInsure());
                                         feeual.setCreatedAt(NewDateUtils.getNowTimeStr());
                                         feeual.setCreatedIp(IPUtils.getRemortIP(request));
                                         feeual.setMoneyCollection(newfeeuseracc.getMoneyCollection());
                                         feeual.setMoneyTenderFreeze(newfeeuseracc.getMoneyTenderFreeze());
                                         
                                         userAccountLogService.insertUserAccountLog(feeual);
                                         
                                         out(response, 1);
//	                        			 }catch(Exception e){
//	                        				 out(response, 3);
//	                        				 e.printStackTrace();
//	                        			 }
	                        		 }
	                        		 else{
	                        			 //红包不够支付 ，从用户可用余额中扣除
//	                        			 try {
	                                            /** 更新配资记录表状态 */
	                                            planRecordService.updateRecordStatus(0, pr.getId());
	                                            
	                                            double moneyInsureNOTHongbao=pr.getMoneyInsure()-hongbaoMoney;
	                                            /** 更新资金 */
	                                            userac.setMoneyUsable(userac.getMoneyUsable()-moneyInsureNOTHongbao);
	                                            userac.setMoneyInsure(userac.getMoneyInsure()+pr.getMoneyInsure());
	                                            userac.setUpdatedAt(NewDateUtils.getNowTimeStr());
	                                            
	                                            userAccountService.updateUserAccount(userac);
	                                            
	                                            //新增资金记录
	                                            UserAccount useracc = userAccountService.getUserAccount(user.getId());
	                                            
	                                            //新建资金记录 两个
	                                            UserAccountLog ual = new UserAccountLog();
	                                            ual.setUserId(useracc.getUserId());
	                                            ual.setType(1);
	                                            ual.setMoneyOperate(pr.getMoneyInsure());
	                                            ual.setMoneyTotal(useracc.getMoneyTotal());
	                                            ual.setMoneyUsable(useracc.getMoneyUsable()-moneyInsureNOTHongbao);
	                                            ual.setMoneyWithdraw(useracc.getMoneyWithdraw());
	                                            ual.setMoneyInsure(useracc.getMoneyInsure());
	                                            ual.setRemark("为配资记录编号"+pr.getId()+"的支付保证金" + pr.getMoneyInsure()+"元,其中使用红包支付"+hongbaoMoney);
	                                            ual.setCreatedAt(NewDateUtils.getNowTimeStr());
	                                            ual.setCreatedIp(IPUtils.getRemortIP(request));
	                                            ual.setMoneyCollection(useracc.getMoneyCollection());
	                                            ual.setMoneyTenderFreeze(useracc.getMoneyTenderFreeze());
	                                            
	                                            userAccountLogService.insertUserAccountLog(ual); 
	                                            
	                                            
	                                            /**
	                                             * 管理费
	                                             */
	                                            /** 更新资金 */
	                                            UserAccount feeuseracc = userAccountService.getUserAccount(user.getId());
	                                            
	                                            if(pr.getPlanType().equals(1L)){
	                                                feeuseracc.setMoneyUsable(userac.getMoneyUsable()-glf); 
	                                                feeuseracc.setMoneyTotal(userac.getMoneyTotal()-glf);
	                                            }else{
	                                                feeuseracc.setMoneyUsable(userac.getMoneyUsable()-sylx); 
	                                                feeuseracc.setMoneyTotal(userac.getMoneyTotal()-sylx);
	                                            }
	                                            feeuseracc.setUpdatedAt(NewDateUtils.getNowTimeStr());
	                                            
	                                            userAccountService.updateUserAccount(feeuseracc);
	                                            
	                                            //新增资金记录
	                                            UserAccount newfeeuseracc = userAccountService.getUserAccount(user.getId());
	                                            
	                                            //新建资金记录 两个
	                                            UserAccountLog feeual = new UserAccountLog();
	                                            feeual.setUserId(newfeeuseracc.getUserId());
	                                            feeual.setType(1);
	                                            if(pr.getPlanType().equals(1L)){
	                                                feeual.setMoneyOperate(glf);
	                                                feeual.setRemark("为配资记录编号"+pr.getId()+"的支付管理费" + glf+"元");
	                                            }else{
	                                                feeual.setMoneyOperate(sylx);
	                                                feeual.setRemark("为配资记录编号"+pr.getId()+"的支付月利息" + sylx+"元");                                           
	                                            }
	                                            feeual.setMoneyTotal(newfeeuseracc.getMoneyTotal());
	                                            feeual.setMoneyUsable(newfeeuseracc.getMoneyUsable());
	                                            feeual.setMoneyWithdraw(newfeeuseracc.getMoneyWithdraw());
	                                            feeual.setMoneyInsure(newfeeuseracc.getMoneyInsure());
	                                            feeual.setCreatedAt(NewDateUtils.getNowTimeStr());
	                                            feeual.setCreatedIp(IPUtils.getRemortIP(request));
	                                            feeual.setMoneyCollection(newfeeuseracc.getMoneyCollection());
	                                            feeual.setMoneyTenderFreeze(newfeeuseracc.getMoneyTenderFreeze());
	                                            
	                                            userAccountLogService.insertUserAccountLog(feeual);
	                                            
	                                            out(response, 1);
	                                            
	                                            
//	                                        } catch (Exception e) {
//	                                            out(response, 3);
//	                                            e.printStackTrace();
//	                                        }  
	                        		 }
                        			 } catch (Exception e) {
                                         out(response, 3);
                                         e.printStackTrace();
                                     }  
                        		 
                        		 }else{
                        			 out(response, 4);
                        		 }
                        	 }
                        	 
                         }else{
                        	
                         
//                        	 如果用户没有使用红包
                        	 if(totalpay>userac.getMoneyUsable()){
	                              
                           	  out(response, 2); //余额不足
                             
                             }else{
                            	 if(pr.getPlanType()==3){
                            		 
                            		 boolean isFree = true;
                                     
                                     List<PlanRecord> prd = planService.getFreePlanByUser(user.getId());
                                     
                                     if(prd.size()>0){
                                         isFree = false;
                                     }
                                         
                                     if(isFree){
                                     
                                         //查询当天的免费体验限额
                                         SystemInfo sysinfo = systemService.getSystemInfo();
     
                                         int freecount = 0;  
                                           
                                         if(StringUtils.isEmpty(sysinfo.getValue("free_count"))){
                                             
                                             freecount = 0;
                                             
                                         }else{
                                             
                                             freecount = Integer.valueOf(sysinfo.getValue("free_count"));
                                         }
                                         
                                         List<PlanRecord> allfreeprdlist = planService.queryFreeRecordByDate(NewDateUtils.getStartTime(), NewDateUtils.getEndTime());
                          
                                         if(freecount>0 && allfreeprdlist.size()<freecount){
                                             
                                             if(pr.getStatus()==9){
                                                 
                                                 try {
                                                     
                                                     /** 更新配资记录表状态 */
                                                     planRecordService.updateRecordStatus(0, pr.getId());
                                                     
                                                     /** 更新资金 */
                                                     userac.setMoneyUsable(userac.getMoneyUsable()-pr.getMoneyInsure());
                                                     userac.setMoneyInsure(userac.getMoneyInsure()+pr.getMoneyInsure());
                                                     userac.setUpdatedAt(NewDateUtils.getNowTimeStr());
                                                     
                                                     userAccountService.updateUserAccount(userac);
                                                     
                                                     //新增资金记录
                                                     UserAccount useracc = userAccountService.getUserAccount(user.getId());
                                                     
                                                     //新建资金记录 两个
                                                     UserAccountLog ual = new UserAccountLog();
                                                     ual.setUserId(useracc.getUserId());
                                                     ual.setType(1);
                                                     ual.setMoneyOperate(pr.getMoneyInsure());
                                                     ual.setMoneyTotal(useracc.getMoneyTotal());
                                                     ual.setMoneyUsable(useracc.getMoneyUsable());
                                                     ual.setMoneyWithdraw(useracc.getMoneyWithdraw());
                                                     ual.setMoneyInsure(useracc.getMoneyInsure());
                                                     ual.setRemark("为配资记录编号"+pr.getId()+"的支付保证金" + pr.getMoneyInsure()+"元");
                                                     ual.setCreatedAt(NewDateUtils.getNowTimeStr());
                                                     ual.setCreatedIp(IPUtils.getRemortIP(request));
                                                     ual.setMoneyCollection(useracc.getMoneyCollection());
                                                     ual.setMoneyTenderFreeze(useracc.getMoneyTenderFreeze());
                                                     
                                                     userAccountLogService.insertUserAccountLog(ual); 
                                                     
                                                     
                                                     /**
                                                      * 管理费
                                                      */
                                                     /** 更新资金 */
                                                     //免费体验
                                                     out(response, 1);
                                                     
                                                 } catch (Exception e) {
                                                     out(response, 3);
                                                     e.printStackTrace();
                                                 }                           
                                             }else{
                                                 
                                                 out(response, 4);
                                             }
                                             
                                         }else{
                                             
                                             out(response, 9);
                                         }
                                     }else{
                                      
                                         out(response, 8);
                                     }
                            		 
                            	 }else{
                            		 
                            		 if(pr.getStatus()==9){
                                         
                                         try {
                                             
                                             /** 更新配资记录表状态 */
                                             planRecordService.updateRecordStatus(0, pr.getId());
                                             
                                             /** 更新资金 */
                                             userac.setMoneyUsable(userac.getMoneyUsable()-pr.getMoneyInsure());
                                             userac.setMoneyInsure(userac.getMoneyInsure()+pr.getMoneyInsure());
                                             userac.setUpdatedAt(NewDateUtils.getNowTimeStr());
                                             
                                             userAccountService.updateUserAccount(userac);
                                             
                                             //新增资金记录
                                             UserAccount useracc = userAccountService.getUserAccount(user.getId());
                                             
                                             //新建资金记录 两个
                                             UserAccountLog ual = new UserAccountLog();
                                             ual.setUserId(useracc.getUserId());
                                             ual.setType(1);
                                             ual.setMoneyOperate(pr.getMoneyInsure());
                                             ual.setMoneyTotal(useracc.getMoneyTotal());
                                             ual.setMoneyUsable(useracc.getMoneyUsable());
                                             ual.setMoneyWithdraw(useracc.getMoneyWithdraw());
                                             ual.setMoneyInsure(useracc.getMoneyInsure());
                                             ual.setRemark("为配资记录编号"+pr.getId()+"的支付保证金" + pr.getMoneyInsure()+"元");
                                             ual.setCreatedAt(NewDateUtils.getNowTimeStr());
                                             ual.setCreatedIp(IPUtils.getRemortIP(request));
                                             ual.setMoneyCollection(useracc.getMoneyCollection());
                                             ual.setMoneyTenderFreeze(useracc.getMoneyTenderFreeze());
                                             
                                             userAccountLogService.insertUserAccountLog(ual); 
                                             
                                             
                                             /**
                                              * 管理费
                                              */
                                             /** 更新资金 */
                                             UserAccount feeuseracc = userAccountService.getUserAccount(user.getId());
                                             
                                             if(pr.getPlanType().equals(1L)){
                                                 feeuseracc.setMoneyUsable(userac.getMoneyUsable()-glf); 
                                                 feeuseracc.setMoneyTotal(userac.getMoneyTotal()-glf);
                                             }else{
                                                 feeuseracc.setMoneyUsable(userac.getMoneyUsable()-sylx); 
                                                 feeuseracc.setMoneyTotal(userac.getMoneyTotal()-sylx);
                                             }
                                             feeuseracc.setUpdatedAt(NewDateUtils.getNowTimeStr());
                                             
                                             userAccountService.updateUserAccount(feeuseracc);
                                             
                                             //新增资金记录
                                             UserAccount newfeeuseracc = userAccountService.getUserAccount(user.getId());
                                             
                                             //新建资金记录 两个
                                             UserAccountLog feeual = new UserAccountLog();
                                             feeual.setUserId(newfeeuseracc.getUserId());
                                             feeual.setType(1);
                                             if(pr.getPlanType().equals(1L)){
                                                 feeual.setMoneyOperate(glf);
                                                 feeual.setRemark("为配资记录编号"+pr.getId()+"的支付管理费" + glf+"元");
                                             }else{
                                                 feeual.setMoneyOperate(sylx);
                                                 feeual.setRemark("为配资记录编号"+pr.getId()+"的支付月利息" + sylx+"元");                                           
                                             }
                                             feeual.setMoneyTotal(newfeeuseracc.getMoneyTotal());
                                             feeual.setMoneyUsable(newfeeuseracc.getMoneyUsable());
                                             feeual.setMoneyWithdraw(newfeeuseracc.getMoneyWithdraw());
                                             feeual.setMoneyInsure(newfeeuseracc.getMoneyInsure());
                                             feeual.setCreatedAt(NewDateUtils.getNowTimeStr());
                                             feeual.setCreatedIp(IPUtils.getRemortIP(request));
                                             feeual.setMoneyCollection(newfeeuseracc.getMoneyCollection());
                                             feeual.setMoneyTenderFreeze(newfeeuseracc.getMoneyTenderFreeze());
                                             
                                             userAccountLogService.insertUserAccountLog(feeual);
                                             
                                             out(response, 1);
                                             
                                             
                                         } catch (Exception e) {
                                             out(response, 3);
                                             e.printStackTrace();
                                         }                           
                                     }else{
                                         
                                         out(response, 4);
                                     }
                            	 }
                            	 
                             }
                         }
                         
                        }else{
                            
                            out(response, 5); //未实名认证
                        }
                        
                        return null;
                    }
                }
            
        }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "redirect:../tologin.html"; 

    }
	
    /**
     * 按天配资
     * @Title: planday 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("plan/day")
    public String planday(ModelMap map,HttpServletRequest request, HttpServletResponse response){
         
        Map<String, String> params = getParamMap(request);
        
        User user = getCurrUser(request, response);
        
        String opstr = params.get("opt");
        
        if(StringUtils.isEmpty(opstr)){
            
            return "plan/fund_day";
            
        }else if(opstr.equals("add")){
            
            if(user==null){
                
                return "redirect:../tologin.html"; 
            }
            
            if(user.getRealVerifyStatus()!=null && user.getRealVerifyStatus()==1){
                
                PlanRate pr = new PlanRate();
                pr.setType(1);  //按天配资
                pr.setAmount(Double.valueOf(params.get("amount")));
                pr.setInterval(Integer.valueOf(params.get("interval")));
                pr.setRate(Double.valueOf(params.get("lilv")));
                pr.setStatus(0);  //新建
                pr.setUserCreate(user.getId());
                pr.setCreatedAt(NewDateUtils.getNowTimeStr());
                Long pid = planService.createPlanRate(pr);
                
                PlanRecord precord = new PlanRecord();
                
                precord.setUserId(user.getId());
                precord.setPlanId(pid);
                precord.setPlanType(1L); //按月配资2 按天配资1
                precord.setMoneyInsure(Double.valueOf(params.get("moneyInsure")));
                precord.setPower(Double.valueOf(params.get("power")));
                precord.setInterval(Integer.valueOf(params.get("interval")));
                precord.setRate(Double.valueOf(params.get("lilv")));
                precord.setMoneyFee(Double.valueOf(params.get("glf"))*Integer.valueOf(params.get("interval")));
                precord.setMoneyWarning(Double.valueOf(params.get("ksjjx")));
                precord.setMoneyForce(Double.valueOf(params.get("kspcx")));
                precord.setMoneyOp(Double.valueOf(params.get("amount"))-Double.valueOf(params.get("moneyInsure")));
                precord.setMoneySettlement(0.00);  //结算金
                precord.setStatus(9);  //状态(-1 删除；0 新建；1 批准; 9 未支付)
                precord.setUserId(user.getId());
                precord.setCreatedAt(NewDateUtils.getNowTimeStr());
                
                Long prid = planService.createPlanRecord(precord);
                
                return "redirect:plan_pay.html?opt=view&prid="+prid; 
                
            }else{
                
                map.addAttribute("msg", "请先进行实名认证");
                
                return "plan/fund_day";
                
            }
        }
        
        return "plan/fund_day";
    }
    
    /**
     * 按天配资委托协议
     * @Title: agreement_day_rule 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("plan/agreement-day-rule")
    public String agreement_day_rule(ModelMap map,HttpServletRequest request, HttpServletResponse response){
        
        return "plan/entrust-rule";
    }
    
    
    /**
     * 按天配资操盘须知
     * @Title: agreement_day 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("plan/agreement-day")
    public String agreement_day(ModelMap map,HttpServletRequest request, HttpServletResponse response){
        
        return "plan/agreement-day";
    }
    
    /**
     * 按月配资委托协议
     * @Title: agreement_month_rule 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("plan/agreement-month-rule")
    public String agreement_month_rule(ModelMap map,HttpServletRequest request, HttpServletResponse response){
        
        return "plan/entrust-rule";
    }
    
    
    /**
     * 按月配资操盘须知
     * @Title: agreement_month 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("plan/agreement-month")
    public String agreement_month(ModelMap map,HttpServletRequest request, HttpServletResponse response){
        
        return "plan/agreement-month";
    }
    
    /**
     * 按月配资操盘须知
     * @Title: agreement_month 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("plan/free")
    public String free(ModelMap map,HttpServletRequest request, HttpServletResponse response){
        
        try {
            

        
        Map<String,String> params = getParamMap(request);
        
        String ctype = params.get("opt");
        
        if(StringUtils.isEmpty(ctype)){
            
            return "plan/freeEnjoy";
            
        }else if(ctype.equals("join")){
            
            User user = getCurrUser(request, response);
            
            if(user==null){
                
                return "redirect:../tologin.html"; 
                
            }else{
                
                map.addAttribute("user", user);
            }
            
            long nowda = NewDateUtils.getNowTimeStr();
            
            long jiezhistart = NewDateUtils.getStartTime()+60*60*10;
            
            long jiezhiend = NewDateUtils.getStartTime()+60*60*16;
            
            if(nowda<jiezhistart || nowda > jiezhiend){
                
                map.addAttribute("msg", "免费配资申请时间为上午10:00-下午3:00");
                
                return "plan/freeEnjoy";
            }
            
            boolean isFree = true;
                
            if(user.getRealVerifyStatus()!=null && user.getRealVerifyStatus()==1){
                
                List<PlanRecord> prd = planService.getFreePlanByUser(user.getId());
                
                if(prd.size()>0){
                    isFree = false;
                }
                               
                if(!isFree){
                    
                  map.addAttribute("msg", "对不起，您已经免费体验过一次了");
                  
                  return "plan/freeEnjoy";
                  
                //已经满了  
                }else{
                    
                  isFree = true;
                   
                  //查询当天的免费体验限额
                  SystemInfo sysinfo = systemService.getSystemInfo();

                  int freecount = 0;  
                    
                  if(StringUtils.isEmpty(sysinfo.getValue("free_count"))){
                      
                      freecount = 0;
                      
                  }else{
                      
                      freecount = Integer.valueOf(sysinfo.getValue("free_count"));
                  }
                  
                  List<PlanRecord> allfreeprdlist = planService.queryFreeRecordByDate(NewDateUtils.getStartTime(), NewDateUtils.getEndTime());
   
                  if(freecount>0 && allfreeprdlist.size()<freecount){
                      
                      PlanRate pr = new PlanRate();
                      pr.setType(3);  //免费体验
                      pr.setAmount(2001.00);  //用户出1元，平台出2000
                      pr.setInterval(2);   //用2天
                      pr.setRate(0.00);
                      pr.setStatus(0);  //新建
                      pr.setUserCreate(user.getId());
                      pr.setCreatedAt(NewDateUtils.getNowTimeStr());
                      Long pid = planService.createPlanRate(pr);
                      
                      PlanRecord precord = new PlanRecord();
                      
                      precord.setUserId(user.getId());
                      precord.setPlanId(pid);
                      precord.setPlanType(3L); //按月配资2 按天配资1 免费体验3
                      precord.setMoneyInsure(1.00);
                      precord.setPower(1.00);
                      precord.setInterval(2);
                      precord.setRate(0.00);
                      precord.setMoneyFee(0.00);
                      precord.setMoneyWarning(0.00);
                      precord.setMoneyForce(0.00);
                      precord.setMoneyOp(2000.00);
                      precord.setMoneySettlement(0.00);  //结算金
                      precord.setStatus(9);  //状态(-1 删除；0 新建；1 批准; 9 未支付)
                      precord.setUserId(user.getId());
                      precord.setCreatedAt(NewDateUtils.getNowTimeStr());
                      
                      Long prid = planService.createPlanRecord(precord);
                      
                      return "redirect:plan_pay.html?opt=view&prid="+prid; 
                      
                  }else{
                      
                      map.addAttribute("msg", "对不起，免费额度已经被抢光了，请随时注意公告，您还可以进行平台的其他配资");
                      
                      return "plan/freeEnjoy";
                      
                  }
                }
                
            }else{
                
                map.addAttribute("msg", "请先进行实名认证");
                
                return "plan/freeEnjoy";
                
            }
            
            
         }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null; 
        
    }
    
    
    /**
     * 追加保证金
     * @Title: appendInsure 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @param pid
     * @param @return 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("plan/appendInsure")
    public String appendInsure(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long pid){
        
        Map<String,String> params = getParamMap(request);
        
        String ctype = params.get("opt");
        
        String money = params.get("money");
        
        PlanRecord pr = planService.queryRecordById(pid);
        
        User user = getCurrUser(request, response);
        
        map.addAttribute("user", user);
        
        if(pr==null || !user.getId().equals(pr.getUserId())){
            
            map.addAttribute("msg", "该配资记录不存在");
            
            return "redirect:../myhome/recordlog.html"; 

        }else if(StringUtils.isEmpty(ctype)){
            
            map.addAttribute("pr", pr);
            
            UserAccount userac = userAccountService.getUserAccount(user.getId());
            
            map.addAttribute("userac", userac);
                        
            return "plan/plan_append_insure_pay";
            
        }else if(ctype.equals("pay")&&StringUtils.isNotEmpty(money)){
            
            UserAccount userac = userAccountService.getUserAccount(user.getId());
            
            //扣除金额
            //生成追加记录
            //生成资金交易记录
            if(user.getRealVerifyStatus()!=null && user.getRealVerifyStatus()==1){
                
                if(pr.getStatus()==0 || pr.getStatus()==1 ){
                    
                    if(Double.valueOf(money)>userac.getMoneyUsable()){
                        
                        out(response, 2); //余额不足
                        
                    }else{
                        
                        //生成追加记录
                        PlanAppendInsure pai = new PlanAppendInsure();
                        pai.setUserId(user.getId());
                        pai.setPlanRecordId(pid);
                        pai.setMoneyInsure(Double.valueOf(money));
                        pai.setStatus(0);
                        pai.setOpLog("为配资记录编号"+pr.getId()+"的追加保证金" + money +"元");
                        pai.setCreatedAt(NewDateUtils.getNowTimeStr());
                        
                        planService.appendInsureByPlan(pai);
                      
                        /** 更新资金 */
                        userac.setMoneyUsable(userac.getMoneyUsable()-Double.valueOf(money));
                        userac.setMoneyInsure(userac.getMoneyInsure()+Double.valueOf(money));
                        userac.setUpdatedAt(NewDateUtils.getNowTimeStr());
                        
                        userAccountService.updateUserAccount(userac);
                        
                        //新增资金记录
                        UserAccount useracc = userAccountService.getUserAccount(user.getId());
                        
                        //新建资金记录 两个
                        UserAccountLog ual = new UserAccountLog();
                        ual.setUserId(useracc.getUserId());
                        ual.setType(11);
                        ual.setMoneyOperate(Double.valueOf(money));
                        ual.setMoneyTotal(useracc.getMoneyTotal());
                        ual.setMoneyUsable(useracc.getMoneyUsable());
                        ual.setMoneyWithdraw(useracc.getMoneyWithdraw());
                        ual.setMoneyInsure(useracc.getMoneyInsure());
                        ual.setRemark("为配资记录编号"+pr.getId()+"的追加保证金" + money +"元");
                        ual.setCreatedAt(NewDateUtils.getNowTimeStr());
                        ual.setCreatedIp(IPUtils.getRemortIP(request));
                        ual.setMoneyCollection(useracc.getMoneyCollection());
                        ual.setMoneyTenderFreeze(useracc.getMoneyTenderFreeze());
                        
                        userAccountLogService.insertUserAccountLog(ual); 

                        //免费体验
                        out(response, 1);                    
                        
                    }
                }else{
                    
                    out(response, 4);  
                }
                

            
            }else{
                
                out(response, 5); //未实名认证
            }
            
            return null;
        }
        
        out(response, 3); //支付错误
        
        return null;
    }
         
}
