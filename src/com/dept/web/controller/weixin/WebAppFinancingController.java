package com.dept.web.controller.weixin;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dept.web.controller.WebController;
import com.dept.web.dao.model.Article;
import com.dept.web.dao.model.Financing;
import com.dept.web.dao.model.FinancingApply;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.general.util.MD5;
import com.dept.web.general.util.NewDateUtils;
import com.dept.web.general.util.TextUtil;
import com.dept.web.general.util.TimeUtil;
import com.dept.web.general.util.tools.iphelper.IPUtils;
import com.dept.web.service.ArticleService;
import com.dept.web.service.FinancingApplyService;
import com.dept.web.service.FinancingService;
import com.dept.web.service.UserAccountLogService;
import com.dept.web.service.UserAccountService;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;
import com.sendinfo.xspring.ibatis.page.PageUtils;


/**
 *融资部分
 * 
 * @ClassName: BorrowController.java
 * 
 * @version V1.0
 * @Date 2014-8-15 下午2:08:12 <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class WebAppFinancingController extends WebController {

	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private FinancingService financingService;
	
	@Autowired
	private FinancingApplyService financingApplyService;
	
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private UserAccountLogService userAccountLogService;
	
	
	/**
	 * 首页。查询融资首页
	 */
    @RequestMapping("webapp/financ_Index")
    public String financIndex(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
    
    	//查询企业项目。3条
    	Financing financing = new Financing();
    	financing.setTypes1(2);
    	financing.setStatus(1);
    	List<Financing> qylist= financingService.queryFinancingListByIndex(financing);
    	map.addAttribute("qylist", qylist);
    	// 网站公告
		List<Article> ggList = articleService.queryArticleList("wzgg", 0, 5);
		map.addAttribute("ggList", ggList);
		// 帮助中心
		List<Article> bbList = articleService.queryArticleBbList("bzzx", 0, 5);
		map.addAttribute("bbList", bbList);
    	//查询个人项目 。3条
    	financing.setTypes1(1);
    	List<Financing> grlist= financingService.queryFinancingListByIndex(financing);
    	map.addAttribute("grlist", grlist);
    	//统计参与人数，融资总额，平均利率
    	Financing indexfinanc = financingService.queryFinancingSumMessage(financing);
    	map.addAttribute("indexfinanc", indexfinanc);
    	//网站公告
    	//最新参与投资者
    	return "borrow/borrowIndex";
    } 
	
	/**
	 * 查询企业融资列表
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
    @RequestMapping("webapp/financ_searchCompany")
	public String financsearchCompany(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException{
    	PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
		populate(pageRequest, request);
		pageRequest.setPageSize(12);
		Map<String, String> params = getParamMap(request);
		params.put("typessql","2");
		createSearchParams(params);
		if(StringUtil.isNotEmpty(params.get("page"))){
			pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
		}
		pageRequest.setFilters(params);
        Page<Financing> financingPage = financingService.queryFinancingPage(pageRequest);
        map.addAttribute("financingPage", financingPage);
        map.addAttribute("totalPage", PageUtils.computeLastPageNumber(financingPage.getTotalCount(), financingPage.getPageSize()));
        map.addAttribute("page",pageRequest.getPageNumber());
       
        map.addAttribute("dq",params.get("dq"));
		map.addAttribute("qyfl",params.get("qyfl"));
		map.addAttribute("hylx",params.get("hylx"));
		map.addAttribute("rzje",params.get("rzje"));
		map.addAttribute("lcqx",params.get("lcqx"));
		map.addAttribute("fxpj",params.get("fxpj"));
		map.addAttribute("dbfs",params.get("dbfs"));
		map.addAttribute("fbsj",params.get("fbsj"));
		map.addAttribute("tbxm",params.get("tbxm"));
		
		return "webapp/rongzi-list";
	}
    
    
    /**
	 * 查询个人融资列表
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
    @RequestMapping("webapp/financ_searchPersonal")
	public String financsearchPersonal(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException{
    	PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
		populate(pageRequest, request);
		pageRequest.setPageSize(12);
		Map<String, String> params = getParamMap(request);
		params.put("typessql","1");
		createSearchParams(params);
		if(StringUtil.isNotEmpty(params.get("page"))){
			pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
		}
		pageRequest.setFilters(params);
        Page<Financing> financingPage = financingService.queryFinancingPage(pageRequest);
        map.addAttribute("financingPage", financingPage);
        map.addAttribute("totalPage", PageUtils.computeLastPageNumber(financingPage.getTotalCount(), financingPage.getPageSize()));
        map.addAttribute("page",pageRequest.getPageNumber());
       
        map.addAttribute("dq",params.get("dq"));
		map.addAttribute("qyfl",params.get("qyfl"));
		map.addAttribute("hylx",params.get("hylx"));
		map.addAttribute("rzje",params.get("rzje"));
		map.addAttribute("lcqx",params.get("lcqx"));
		map.addAttribute("fxpj",params.get("fxpj"));
		map.addAttribute("dbfs",params.get("dbfs"));
		map.addAttribute("fbsj",params.get("fbsj"));
		map.addAttribute("tbxm",params.get("tbxm"));
		
		return "webapp/rongzi-list";
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
		String dq = TextUtil.isNull(params.get("dq"));
		String qyfl = TextUtil.isNull(params.get("qyfl"));
		String hylx = TextUtil.isNull(params.get("hylx"));
		String rzje = TextUtil.isNull(params.get("rzje"));
		String lcqx = TextUtil.isNull(params.get("lcqx"));
		String fxpj = TextUtil.isNull(params.get("fxpj"));
		String dbfs = TextUtil.isNull(params.get("dbfs"));
		String fbsj = TextUtil.isNull(params.get("fbsj"));
		String tbxm = TextUtil.isNull(params.get("tbxm"));
		
		/**
		 * 地区
		 */
		if(dq.equals("1")){
			params.put("dqsql", " city = 1 ");	
		}else if(dq.equals("2")){
			params.put("dqsql", " city = 2 ");
		}else if(dq.equals("3")){
			params.put("dqsql", " city = 3 ");
		}else if(dq.equals("4")){
			params.put("dqsql", " city = 4 ");
		}else if(dq.equals("5")){
			params.put("dqsql", " city = 5 ");
		}
		/**
		 * 种类
		 */
		if(qyfl.equals("1")){
			params.put("qyflsql", " types = 1 ");	
		}else if(qyfl.equals("2")){
			params.put("qyflsql", " types = 2 ");	
		}else if(qyfl.equals("3")){
			params.put("qyflsql", " types = 3 ");	
		}else if(qyfl.equals("4")){
			params.put("qyflsql", " types = 4 ");	
		}else if(qyfl.equals("5")){
			params.put("qyflsql", " types = 5 ");	
		}else if(qyfl.equals("6")){
			params.put("qyflsql", " types = 6 ");	
		}
		/**
		 * 行业类型
		 */
		if(hylx.equals("1")){
			params.put("hylxsql", "trade = 1");	
		}else if(hylx.equals("2")){
			params.put("hylxsql", "trade = 2");	
		}else if(hylx.equals("3")){
			params.put("hylxsql", "trade = 3");	
		}else if(hylx.equals("4")){
			params.put("hylxsql", "trade = 4");	
		}else if(hylx.equals("5")){
			params.put("hylxsql", "trade = 5");	
		}
		
		/**
		 * 融资金额
		 */
		if(rzje.equals("1")){
			params.put("rzjesql", "money < 3000000");	
		}else if(rzje.equals("2")){
			params.put("rzjesql", "3000000 <= money and  money< 5000000");
		}else if(rzje.equals("3")){
			params.put("rzjesql", "5000000 <=money and  money< 1000000");	
		}else if(rzje.equals("4")){
			params.put("rzjesql", "money >= 10000000");	
		}
		else if(rzje.equals("5")){
			params.put("rzjesql", "money < 200000");	
		}
		else if(rzje.equals("6")){
			params.put("rzjesql", "200000 <= money and  money< 500000");	
		}
		else if(rzje.equals("7")){
			params.put("rzjesql", "500000 <= money and  money< 1000000");	
		}
		else if(rzje.equals("8")){
			params.put("rzjesql", "money >= 1000000");	
		}
		/**
		 * 理财期限
		 */
		if(lcqx.equals("1")){
			params.put("lcqxsql", "time_limit < 1");	
		}else if(lcqx.equals("2")){
			params.put("lcqxsql", "1 <= time_limit and time_limit< 3");
		}else if(lcqx.equals("3")){
			params.put("lcqxsql", "3 <= time_limit  and time_limit< 6");
		}else if(lcqx.equals("4")){
			params.put("lcqxsql", "time_limit >= 6");
		}
		
		/**
		 * 风险评级
		 */
		if(fxpj.equals("1")){
			params.put("fxpjsql", " risk = 1");	
		}else if(fxpj.equals("2")){
			params.put("fxpjsql", " risk = 2");
		}else if(fxpj.equals("3")){
			params.put("fxpjsql", " risk = 3");
		}else if(fxpj.equals("4")){
			params.put("fxpjsql", " risk = 4");
		}
		
		/**
		 * 担保方式
		 */
		if(dbfs.equals("1")){
			params.put("dbfssql", " guarantee = 1");	
		}else if(dbfs.equals("2")){
			params.put("dbfssql", " guarantee = 2");
		}else if(dbfs.equals("3")){
			params.put("dbfssql", " guarantee = 3");
		}else if(dbfs.equals("4")){
			params.put("dbfssql", " guarantee = 4");
		}
		
		/**
		 * 发布时间，融资金额，排序
		 */
		if(fbsj.equals("1")){
			//发布时间
			params.put("fbsjsql", " order by addtime desc");
			
		}else if(fbsj.equals("2")){
			//融资金额
			params.put("fbsjsql", " order by money desc");
		}else{
			//默认排序
			params.put("fbsjsql", " order by status,addtime desc");
		}
		
		/**
		 * 投标项目
		 */
		if(tbxm.equals("1")){
			//只显示可融资项目
			params.put("tbxmsql", "  status =1");
			
		}else{
			//默认显示
			params.put("tbxmsql", " status in(1,3)");
		}
		return params;
	}
	
	 /**
		 * 查询融资详情
		 * @throws InvocationTargetException 
		 * @throws IllegalAccessException 
		 */
	    @RequestMapping("webapp/financ_detail")
		public String finanDetail(ModelMap map,HttpServletRequest request, HttpServletResponse response) {
	    	String bid = request.getParameter("bid");
	    	Financing financing = financingService.queryById(Long.valueOf(bid));
	    	 if(financing!=null){
	    		 if(financing.getStatus()==1){
	    			Financing f = new Financing();
	 	 	    	f.setCounts(financing.getCounts()+1);
	 	 	    	f.setId(financing.getId());
	 	 	    	financingService.updateFinancingCounts(f);
	 	 	    	financing.setCounts(f.getCounts());
	    		 }
	 	    	if(financing.getUser_id()>0){
	 	     		User user = userService.queryByUserId(financing.getUser_id());
	 	     		if(user!=null){
	 	     			financing.setUsername(user.getUsername());	
	 	     		}
	 	     	} 
	    	 }
	         map.addAttribute("financing", financing);
	         return "webapp/rongzi-detail";
	    }
	    /**
	     * 个人申请融资,起始页
	     */
	    @RequestMapping("webapp/apply_gr")
		public String apply_gr(ModelMap map,HttpServletRequest request, HttpServletResponse response) {

			User user = getCurrUser(request, response);
			if(user == null){
				return "redirect:tologin.html";
			}
	         return "webapp/rongzi-apply";
	    }
	    /**
	     * 企业申请融资,起始页
	     */
	    @RequestMapping("webapp/apply_qy")
		public String apply_qy(ModelMap map,HttpServletRequest request, HttpServletResponse response) {

			User user = getCurrUser(request, response);
			if(user == null){
				return "redirect:webapp/tologin.html";
			}
	         return "borrow/applyC1";
	    }
	    
	    /**
	     * 企业申请融资,起始页
	     */
	    @RequestMapping("webapp/apply_add")
		public String apply_add(ModelMap map,HttpServletRequest request, HttpServletResponse response) {
	    	User user = getCurrUser(request, response);
			if(user == null){
				return "redirect:webapp/tologin.html";
			}
	    	
	    	Map<String, String> params = getParamMap(request);
	    	String types = params.get("types");
	    	Long returnId =0L;
			if(types.equals("1")){
				//个人。
				FinancingApply fa = new FinancingApply();
				fa.setName(params.get("name"));
				fa.setName_py(params.get("name_py"));
				fa.setPhone(params.get("phone"));
				fa.setAddress(params.get("address"));
				fa.setCompany(params.get("company"));
				fa.setCompany_phone(params.get("company_phone"));
				fa.setApply_money(params.get("apply_money"));
				if(StringUtil.isNotEmpty(params.get("year_money"))){
					fa.setYear_money(Integer.valueOf(params.get("year_money")));
				}
				if(StringUtil.isNotEmpty(params.get("house"))){
					fa.setHouse(Integer.valueOf(params.get("house")));
				}
				if(StringUtil.isNotEmpty(params.get("apply_type"))){
					fa.setApply_type(Integer.valueOf(params.get("apply_type")));
				}
				if(StringUtil.isNotEmpty(params.get("apply_guarantee"))){
					fa.setApply_guarantee(Integer.valueOf(params.get("apply_guarantee")));
				}
				if(StringUtil.isNotEmpty(params.get("money_use"))){
					fa.setMoney_use(Integer.valueOf(params.get("money_use")));
				}
				fa.setTypes(1);
				fa.setStatus(0);
				fa.setUser_id(user.getId());
				fa.setAddtime(TimeUtil.getNowTimeStr());
				returnId =	financingApplyService.insertFinancingApply(fa);
			}else{
				//企业
				FinancingApply fa = new FinancingApply();
				fa.setName(params.get("name"));
				fa.setYyzz(params.get("yyzz"));
				fa.setPhone(params.get("phone"));
				fa.setAddress(params.get("address"));
				fa.setSshy(params.get("sshy"));
				fa.setCompany_title(params.get("company_title"));
				fa.setApply_money(params.get("apply_money"));
				if(StringUtil.isNotEmpty(params.get("apply_type"))){
					fa.setApply_type(Integer.valueOf(params.get("apply_type")));
				}
				if(StringUtil.isNotEmpty(params.get("apply_guarantee"))){
					fa.setApply_guarantee(Integer.valueOf(params.get("apply_guarantee")));
				}
				fa.setTypes(2);
				fa.setStatus(0);
				fa.setUser_id(user.getId());
				fa.setAddtime(TimeUtil.getNowTimeStr());
				returnId =financingApplyService.insertFinancingApply(fa);
			}
			map.addAttribute("returnId",returnId);
			/*return "redirect:applyC2.html";*/
			
			FinancingApply fa = financingApplyService.queryBySeting();
			if(fa!=null){
				map.addAttribute("dmoney",fa.getSetName());
			}
			UserAccount account = userAccountService.getUserAccount(user.getId());
			map.addAttribute("account",account);
			map.addAttribute("user",user);
	         return "webapp/rongzi-apply";
	    }
	    /**
	     * 企业申请融资,起始页
	     */
	    @RequestMapping("webapp/applyC2")
		public String applyC2(ModelMap map,HttpServletRequest request, HttpServletResponse response) {
	    	
			User user = getCurrUser(request, response);
			if(user == null){
				return "redirect:/tologin.html";
			}
			FinancingApply fa = financingApplyService.queryBySeting();
			if(fa!=null){
				map.addAttribute("dmoney",fa.getSetName());
				map.addAttribute("dmoney",fa.getSetName());
			}
			UserAccount account = userAccountService.getUserAccount(user.getId());
			map.addAttribute("account",account);
			map.addAttribute("user",user);
			map.addAttribute("returnId",request.getParameter("returnId"));
			map.addAttribute("msg",request.getParameter("msg"));
	         return "borrow/applyC2";
	    }
	    
	    /**
	     * 企业申请融资,起始页
	     */
	    @RequestMapping("webapp/applyC2_add")
		public String applyC2_add(ModelMap map,HttpServletRequest request, HttpServletResponse response) {
	    	String financId = request.getParameter("financId");
	    	map.addAttribute("returnId",financId);
	    	
			User user = getCurrUser(request, response);
			if(user == null){
				return "redirect:/tologin.html";
			}
			String password = request.getParameter("password");
			if(StringUtil.isEmpty(password)){
				map.addAttribute("msg","请输入支付密码");
				return "redirect:applyC2.html";
			}
			MD5 md5 = new MD5();
			User users = userService.queryByUserId(user.getId());
			if (users != null) {
				String pass = users.getPasswordPayHash();
				if(!pass.equals(md5.getMD5ofStr(password))){
					map.addAttribute("msg","支付密码错误");
					return "redirect:applyC2.html";
				}
			}
			FinancingApply fs = financingApplyService.queryBySeting();
			Double money = 0.0;
			if(fs!=null){
				money = Double.valueOf(fs.getSetName());
			}
			UserAccount ua = userAccountService.getUserAccount(user.getId());
			if(ua!=null){
				if(ua.getMoneyUsable()<money){
					map.addAttribute("msg","余额不足，请充值");
					return "redirect:applyC2.html";
				}
			}
			
			FinancingApply fa = financingApplyService.queryById(Long.valueOf(financId));
			if(fa!=null){
				FinancingApply apply= new FinancingApply();
				apply.setMoney(fs.getSetName());
				apply.setStatus(1);
				apply.setId(fa.getId());
				financingApplyService.updateFinancingApplyById(apply);
				
				//修改账户金额
				UserAccount userAccounts = userAccountService.getUserAccount(user.getId());
				UserAccount userAccount = new UserAccount();
				userAccount.setId(userAccounts.getId());
				userAccount.setUserId(user.getId());
				double moneyUsable = userAccounts.getMoneyUsable() - Double.valueOf(apply.getMoney());
				double moneyTotal = userAccounts.getMoneyTotal() - Double.valueOf(apply.getMoney());
				userAccount.setMoneyUsable(moneyUsable);
				userAccount.setMoneyTotal(moneyTotal);
				userAccount.setUpdatedAt(System.currentTimeMillis() / 1000);
				userAccountService.updateUserAccount(userAccount);
				
				//增加日志
				 UserAccountLog ual = new UserAccountLog();
                 ual.setUserId(user.getId());
                 ual.setType(99);
                 ual.setMoneyOperate(Double.valueOf(apply.getMoney()));
                 ual.setMoneyTotal(moneyTotal);
                 ual.setMoneyUsable(moneyUsable);
                 ual.setMoneyWithdraw(userAccounts.getMoneyWithdraw());
                 ual.setMoneyInsure(userAccounts.getMoneyInsure());
                 ual.setRemark("申请融资，付定金，扣除用户可用余额"+apply.getMoney()+"元");
                 ual.setCreatedAt(NewDateUtils.getNowTimeStr());
                 ual.setCreatedIp(IPUtils.getRemortIP(request));
                 ual.setMoneyCollection(userAccounts.getMoneyCollection());
                 ual.setMoneyTenderFreeze(userAccounts.getMoneyTenderFreeze());
                 userAccountLogService.insertUserAccountLog(ual); 
			}
	         return "borrow/apply3";
	    }
	    /**
	     * 查看申请融资标列表
	     * @Title: borrowlist 
	     * @Description: TODO
	     * @param @param map
	     * @param @param request
	     * @param @param response
	     * @param @return
	     * @param @throws Exception 设定文件 
	     * @return String 返回类型 
	     * @throws
	     */
	    @RequestMapping("webapp/myhome/financApply")
	    public String financApplyList(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
	    	User user = getCurrUser(request, response);
			if(user == null){
				return "redirect:/tologin.html";
			}
	        Map<String, String> params = getParamMap(request);
	        try {
	            
	            PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
	            populate(pageRequest, request);
	            pageRequest.setPageSize(10);
	            
	            if(StringUtil.isNotEmpty(params.get("page"))){
	                
	                pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
	            }
	            params.put("user_id", String.valueOf(user.getId()));
	            pageRequest.setFilters(params);

	            Page<FinancingApply> financingApplyPage = financingApplyService.queryFinancingApplyPage(pageRequest);
	            
	            List<FinancingApply> financApplyList=financingApplyPage.getResult();
	            financingApplyPage.setResult(financApplyList);
	             
	            map.addAttribute("financingApplyPage", financingApplyPage);
	          
	            
	            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(financingApplyPage.getTotalCount(), financingApplyPage.getPageSize()));
	            
	            map.addAttribute("page",pageRequest.getPageNumber());
	            
	            map.addAttribute("name", params.get("name"));
	            
	            map.addAttribute("starttime", params.get("starttime"));
	            
	            map.addAttribute("endtime", params.get("endtime"));
	            
	            map.addAttribute("status", params.get("status"));
	            
	            
	        } catch (Exception e) {
	            
	            e.printStackTrace();
	        }
	        return "myhome/tender-list";
	    }  
	    
}