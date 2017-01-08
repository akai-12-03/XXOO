package com.dept.web.controller.weixin;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dept.web.context.Constant;
import com.dept.web.controller.WebController;
import com.dept.web.dao.HongbaoDao;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowRepayment;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.Hongbao;
import com.dept.web.dao.model.Market;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.general.context.HttpContext;
import com.dept.web.general.util.MD5;
import com.dept.web.general.util.SessionHelper;
import com.dept.web.general.util.TextUtil;
import com.dept.web.general.util.TimeUtil;
import com.dept.web.general.util.Utils;
import com.dept.web.service.ArticleService;
import com.dept.web.service.BorrowCollectionService;
import com.dept.web.service.BorrowRepaymentService;
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
 * 标部分
 * 
 * @ClassName: BorrowController.java
 * 
 * @version V1.0
 * @Date 2014-8-15 下午2:08:12 <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class WebAppBorrowController extends WebController {

	@Autowired
	private BorrowService borrowService;
	
	@Autowired
	private BorrowTenderService borrowTenderService;
	
	@Autowired
	private BorrowCollectionService borrowCollectionService;
	
	@Autowired
	private BorrowRepaymentService borrowRepaymentService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private ArticleService articleService;
	 
	@Autowired
	private UserAccountLogService accountLogService;
	@Autowired
	private HongbaoDao hongbaoDao;
	@Autowired
	private TransferService transferService;

	/**
	 * 投标列表页面
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("webapp/borrow/toubiao")
	public String borrowList(ModelMap map, HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(8);
			Map<String, String> params = getParamMap(request);
			createSearchParams(params);
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			pageRequest.setFilters(params);
			Page<Borrow> borrowpage = borrowService.searchBorrowList(pageRequest);
			for(Borrow bw :borrowpage.getResult() ){
				Double count=Double.valueOf(bw.getAccount())-Double.valueOf(bw.getAccountYes());
				bw.setLastAccount(count);
				
				int tzcount=borrowService.getTenderList(bw.getId());
				bw.setTzcount(tzcount);
						if(bw.getVerifyTime()!=null && bw.getVerifyTime()>0){
							bw.setLastTime(TimeUtil.getEndTimeHMS(String.valueOf(bw.getVerifyTime()), 0, bw.getValidTime(), 0, 0, 0));
						}
			}
			map.addAttribute("xmzt",params.get("xmzt"));
			map.addAttribute("rzqx",params.get("rzqx"));
			map.addAttribute("xmsy",params.get("xmsy"));
			map.addAttribute("borrowType",params.get("borrowType"));
			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
			map.addAttribute("page",pageRequest.getPageNumber());
			
			//发布时间
			List<Borrow> baddtimelist=borrowService.getBorrowAddtime();
			map.addAttribute("baddtimelist", baddtimelist);
			
			//预期年化收益
			List<Borrow> baprlist=borrowService.getBorrowApr();
			map.addAttribute("baprlist", baprlist);
			
			//总金额
			List<Borrow> baccountlist=borrowService.getBorrowAccount();
			map.addAttribute("baccountlist", baccountlist);
			
			
		    
			return "qgfwebapp/items-list";
			
		} catch (Exception e) {
			throw e;
		}
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
		String xmzt = TextUtil.isNull(params.get("xmzt"));
		String rzqx = TextUtil.isNull(params.get("rzqx"));
		String xmsy = TextUtil.isNull(params.get("xmsy"));
		
		String borrowType = TextUtil.isNull(params.get("borrowType"));
		if(borrowType==null||"".equals(borrowType)){
			borrowType="1";
		}
		/**
		 * 理财收益
		 */
		if(xmsy.equals("1")){
			params.put("lcsysql", "A.apr >=5 and  A.apr <7");	
		}else if(xmsy.equals("2")){
			params.put("lcsysql", "A.apr >= 7 and  A.apr <10");	
		}else if(xmsy.equals("3")){
			params.put("lcsysql", "A.apr >= 10 and  A.apr <=15");	
		}
		
		
		/**
		 * 理财期限
		 */
		if(rzqx.equals("1")){
			params.put("lcqxsql", "CASE WHEN A.isDay!=1 THEN A.TIME_LIMIT <=1  ELSE a.time_limit<=31 END");	
		}else if(rzqx.equals("2")){
			params.put("lcqxsql", "CASE WHEN A.isDay!=1 THEN A.TIME_LIMIT >1 AND a.time_limit<=3 ELSE a.time_limit>31 AND a.time_limit<=90 END");
		}else if(rzqx.equals("3")){
			params.put("lcqxsql", "CASE WHEN A.isDay!=1 THEN A.TIME_LIMIT >3  AND a.time_limit<=6 ELSE a.time_limit>90 AND a.time_limit<=180 END");
		}else if(rzqx.equals("4")){
			params.put("lcqxsql", "CASE WHEN A.isDay!=1 THEN A.TIME_LIMIT >6  AND a.time_limit<=12 ELSE a.time_limit>180 AND a.time_limit<=360 END");
		}else if(rzqx.equals("5")){
			params.put("lcqxsql", "CASE WHEN A.isDay!=1 THEN A.TIME_LIMIT >12  ELSE a.time_limit>90 AND a.time_limit>360 END");
		}
		
		/**
		 * 投资状态
		 */
		if(xmzt.equals("1")){
			params.put("tzgmsql", "A.status=1");	
		}else if(xmzt.equals("2")){
			params.put("tzgmsql", "A.status=5");
		}else if(xmzt.equals("3")){
			params.put("tzgmsql", "A.status=6 or A.status=11");
		}else if(xmzt.equals("4")){
			params.put("tzgmsql", "A.status =0");
		}else{
			params.put("tzgmsql", "A.status in(0,1,3,5,6,11)");
		}
		
		
		/**
		 * 标类型
		 */
		if(borrowType.equals("1")){
			params.put("borrowTypeSql", "A.borrow_type=1");//xx借贷
		}else if(borrowType.equals("2")){
			params.put("borrowTypeSql", "A.borrow_type=2");//车贷宝
		}else if(borrowType.equals("3")){
			params.put("borrowTypeSql", "A.borrow_type=3");//车贷宝
		}else if(borrowType.equals("4")){
			params.put("borrowTypeSql", "A.borrow_type=4");//车贷宝
		}else if(borrowType.equals("5")){
			params.put("borrowTypeSql", "A.borrow_type=5");//车贷宝
		}
		
		
		
		return params;
	}
	
	
	/**
	 * 精选理财
	 * @param map
	 * @param request
	 * @param response
	 * @param borrowId
	 * @return
	 */
	@RequestMapping("webapp/borrow/toubiaIndex")
	public String licaiIndex(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception{
		List<Borrow> borrowList = borrowService.queryIndexList(6);
		for(Borrow br :borrowList ){
			Double count=Double.valueOf(br.getAccount())-Double.valueOf(br.getAccountYes());
			br.setLastAccount(count);
			
			br.setLastTime(TimeUtil.getEndTime(String.valueOf(br.getVerifyTime()), 0, br.getValidTime(), 0, 0, 0));
			int tzcount=borrowService.getTenderList(br.getId());
			br.setTzcount(tzcount);
			}
		map.addAttribute("borrowList", borrowList);
		return "webapp/toubiao";
	}
	/**
	 * 标详情
	 * @param map
	 * @param request
	 * @param response
	 * @param borrowId
	 * @return
	 */
	@RequestMapping("webapp/borrow/detail")
	public String borrowDetail(ModelMap map, HttpServletRequest request,HttpServletResponse response,@RequestParam long id) {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			UserAccount ua = userAccountService.getUserAccount(user.getId());
			map.addAttribute("ua", ua);
			//查询可使用的红包
			Map<String, String> params = getParamMap(request);
			params.put("userId", String.valueOf(user.getId()));
			params.put("status", "0");
			List<Hongbao> hblist= hongbaoDao.queryListHongbaoByIDAndStatus(params);
			map.addAttribute("hblist", hblist);
		}
		map.addAttribute("user", user);
		
		//查询标的
		Borrow borrow = borrowService.getBorrowById(id);
		if(borrow.getVerifyTime()!=null && borrow.getVerifyTime()>0){
			borrow.setLastTime(TimeUtil.getEndTimeHMS(String.valueOf(borrow.getVerifyTime()), 0, borrow.getValidTime(), 0, 0, 0));
		}else{
			borrow.setLastTime("");
		}
		
		int tzcount=borrowService.getTenderList(borrow.getId());
		borrow.setTzcount(tzcount);
		Double count=Double.valueOf(borrow.getAccount())-Double.valueOf(borrow.getAccountYes());
		borrow.setLastAccount(count);
		map.addAttribute("borrow", borrow);
		
		if(borrow == null){
			return "redirect:/webapp/borrow/toubiao.html";
		}
		//查询借款人信息
		User borrowUser = userService.queryByUserId(borrow.getUserId());
		map.addAttribute("borrowUser", borrowUser);
		map.addAttribute("errormsg", request.getParameter("errormsg"));
		
		String type=request.getParameter("type");
		if(type==null||type.equals("")){
			return "qgfwebapp/ProjectDetails";
		}else if(type.equals("xmjs")){
			return "qgfwebapp/projectIntroduction";
		}else if(type.equals("fxkz")){
			return "qgfwebapp/fxkz";
		}else{
			return "qgfwebapp/ProjectDetails";
		}
		
	}
	/**
	 * 立即投资
	 * @param map
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping("webapp/borrow/detailtender")
	public String detailtender(ModelMap map,HttpServletRequest request, HttpServletResponse response,@RequestParam long id) throws NumberFormatException, Exception{
			User user = new User();
			user = this.getCurrUser(request, response);
			if(user!=null){
				UserAccount ua = userAccountService.getUserAccount(user.getId());
				map.addAttribute("ua", ua);
				//查询可使用的红包
				Map<String, String> params = getParamMap(request);
				params.put("userId", String.valueOf(user.getId()));
				params.put("status", "0");
				List<Hongbao> hblist= hongbaoDao.queryListHongbaoByIDAndStatus(params);
				map.addAttribute("hblist", hblist);
			}
			
			map.addAttribute("user", user);
			map.addAttribute("errormsg", request.getParameter("errormsg"));
			//查询标的
			Borrow borrow = borrowService.getBorrowById(id);
			if(borrow.getVerifyTime()!=null && borrow.getVerifyTime()>0){
				borrow.setLastTime(TimeUtil.getEndTimeHMS(String.valueOf(borrow.getVerifyTime()), 0, borrow.getValidTime(), 0, 0, 0));
			}else{
				borrow.setLastTime("");
			}
			
			int tzcount=borrowService.getTenderList(borrow.getId());
			borrow.setTzcount(tzcount);
			Double count=Double.valueOf(borrow.getAccount())-Double.valueOf(borrow.getAccountYes());
			borrow.setLastAccount(count);
			map.addAttribute("borrow", borrow);
		return "qgfwebapp/ImmediateInvestment";
	}
	
	
	
	/**
	 * 查询投资详情页的交易记录
	 * @param map
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping("webapp/borrow/detailJyjl")
	public String detailJyjl(ModelMap map,HttpServletRequest request, HttpServletResponse response,@RequestParam long id) throws NumberFormatException, Exception{
			//查询标的投资记录
			Long userId = this.getUserId(request, response);
			if (userId == null || userId.intValue() <= 0) {
				map.addAttribute("tenderList", null);
			} else {
				List<BorrowTender> tenderList = borrowTenderService.getTenderListByBorrowId(id);
				map.addAttribute("tenderList", tenderList);
			}
			return "qgfwebapp/transactionRecord2";
	}
	
	/**
	 * 查询投资详情页的项目介绍和安全保障
	 * @param map
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping("webapp/borrow/detailxmOraq")
	public String detailxmOraq(ModelMap map,HttpServletRequest request, HttpServletResponse response,@RequestParam long id,@RequestParam long type) throws NumberFormatException, Exception{
			//查询标的投资记录
			Borrow borrow = borrowService.getBorrowById(id);
			map.addAttribute("borrow", borrow);
			if(type==1){
				return "webapp/detail-xmjs";
			}else{
				return "webapp/detail-aqbz";
			}
	}
	
	@RequestMapping("webapp/borrow/goumai")
	public String goumai(ModelMap map,HttpServletRequest request, HttpServletResponse response,@RequestParam long id) throws NumberFormatException, Exception{
		Borrow borrow = borrowService.getBorrowById(id);
		borrow.setLastTime(TimeUtil.getEndTime(String.valueOf(borrow.getVerifyTime()), 0, borrow.getValidTime(), 0, 0, 0));
		int tzcount=borrowService.getTenderList(borrow.getId());
		borrow.setTzcount(tzcount);
		Double count=Double.valueOf(borrow.getAccount())-Double.valueOf(borrow.getAccountYes());
		borrow.setLastAccount(count);
		map.addAttribute("borrow", borrow);
		
		return "webapp/toubiao";
	}
	
	
	/**
	 * 投标操作
	 * @param map
	 * @param request
	 * @param response
	 * @param borrowId
	 * @return
	 */
	@RequestMapping(value = "webapp/borrow/tender")
	public String borrowTender(ModelMap map, HttpServletRequest request,HttpServletResponse response,@RequestParam long id) {
		
		User user = getCurrUser(request, response);
		if(user == null){
			return "redirect:/webapp/tologin.html";
		}
		
		Borrow borrow = borrowService.getBorrowById(id);
		if(borrow == null){
			return "redirect:/webapp/borrow/toubiao.html";
		}
		
		if(user.getAutoType()==null||user.getAutoType()!=1)
		{
			MoneymoremoreId=user.getMoneymoremoreId();
			return "redirect:/webapp/qddApi/loanAuthorize.html?MoneymoremoreId="+MoneymoremoreId;
//			map.addAttribute("errormsg", "请先开启投标授权");
//			map.addAttribute("user", user);
//			return "myhome/openAuth";
		}
		
		//String BORROW_INFO = "redirect:/webapp/borrow/detail.html?id="+id+"";
		String BORROW_INFO = "redirect:/webapp/borrow/detailtender.html?id="+id+"";
		
		if(borrow.getBorrowType()==5){
			int count = borrowTenderService.getTenderCountByUserId(user.getId());
			if(count>0){
				map.addAttribute("errormsg", "您已经有过投资记录，不能再投新手标");
				return BORROW_INFO;
			}
		}
		
		if(borrow.getStatus() != 1){
			map.addAttribute("errormsg", "该标不能购买");
			return BORROW_INFO;
		}
		
		if(user.getId().equals(borrow.getUserId())){
			map.addAttribute("errormsg", "不能购买自己的理财产品");
			return BORROW_INFO;
		}
		
		//垫资标
		if (borrow.getBorrowType() == 1) {
			if (user.getId().equals(borrow.getReceivePerson())) {
				map.addAttribute("errormsg", "收款人不能购买自己收款的理财产品");
				return BORROW_INFO;
			}
		}
		UserAccount userAccount = userAccountService.getUserAccount(user.getId());
		if(userAccount == null){
			userAccount = new UserAccount();
		}
		
		String tendermoneyStr = request.getParameter("tendermoney");
		//投资金额
		double tenderMoney = 0.0;
		if(com.dept.web.general.util.StringUtils.isNumber(tendermoneyStr)){
			tenderMoney = Utils.getDouble(Double.valueOf(tendermoneyStr),2);
		}else{
			map.addAttribute("errormsg", "请输入正确的投标金额");
			return BORROW_INFO;
		}
		
		double lowAccount=borrow.getLowestAccount();
		if (tenderMoney%lowAccount!=0) {
			map.addAttribute("errormsg", "请投该标的最低金额的整数倍金额");
			return BORROW_INFO;
		}
		
		double moneyUsable =  Utils.getDouble(userAccount.getMoneyUsable(),2);//可以余额
		double moneyLowest = Utils.getDouble(borrow.getLowestAccount(),2);//最少投资
		double moneyMost = Utils.getDouble(borrow.getMostAccount(),2);//最多投资
		double moneyAccount = Utils.getDouble(borrow.getAccount(),2);//总金额
		double moneyYes = Utils.getDouble(borrow.getAccountYes(),2);//已投金额
		double moneyNo = moneyAccount-moneyYes;
		
//		if(user.getCardBindingStatus()==null||user.getCardBindingStatus()!=1){
//			map.addAttribute("errormsg", "未绑定银行卡不能进行投标操作");
//			return BORROW_INFO;
//		}
		
		if(moneyUsable<tenderMoney){
			map.addAttribute("errormsg", "账户可用余额不足");
			return BORROW_INFO;
		}
		
		if(moneyNo <= 0){
			map.addAttribute("errormsg","此标已满! ");
			return BORROW_INFO;
		}
		if(tenderMoney > moneyNo){
				map.addAttribute("errormsg","投资金额不能大于剩余可投金额");
				return BORROW_INFO;
		}
		
		if(moneyNo > tenderMoney){
			if(tenderMoney < moneyLowest){
				map.addAttribute("errormsg","投资金额不能小于最小限制金额");
				return BORROW_INFO;
			}
		}
		if(tenderMoney > moneyMost){
			map.addAttribute("errormsg","投资金额不能大于最大限制金额");
			return BORROW_INFO;
		}
		String hongbaoId = request.getParameter("hongbaoId");
		String url="redirect:/webapp/qddApi/tenderOrderNo.html?id="+id+"&tendermoney="+tenderMoney;
		if(hongbaoId!=null&&!"".equals(hongbaoId.trim())){
			url+="&tenderhongbao="+hongbaoId;
		}
		return url;
	}
	
	/**
	 * 我的借款
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("webapp/myhome/borrow-list")
	public String myBorrowList(ModelMap map, HttpServletRequest request,HttpServletResponse response){
		User loginUser = getCurrUser(request, response);
		if(loginUser == null){
			return "redirect:/tologin.html";
		}
		int page = 1;
		if (StringUtils.isNotBlank(request.getParameter("page"))) {
			page = Integer.valueOf(request.getParameter("page"));
		}
		
		int status = 1;
		if(StringUtils.isNotBlank(request.getParameter("status"))){
			status = Integer.valueOf(request.getParameter("status"));
		}
		
		Map<String, Object> filterMap = new HashMap<String, Object>();
		filterMap.put("userId", loginUser.getId());
		filterMap.put("status", status);
		Page<Borrow> borrowPage = borrowService.getBorrowPage(page, DEFAULT_PAGE_SIZE, filterMap);

		map.addAttribute("page", borrowPage);
		map.addAttribute("status", status);
		return "myhome/borrow-list";
	}
	
	
	/**
	 * 还款明细
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("webapp/myhome/repayment-list")
	public String borrowRepaymentList(ModelMap map, HttpServletRequest request,HttpServletResponse response){
		User loginUser = getCurrUser(request, response);
		if(loginUser == null){
			return "redirect:/tologin.html";
		}
		int page = 1;
		if (StringUtils.isNotBlank(request.getParameter("page"))) {
			page = Integer.valueOf(request.getParameter("page"));
		}
		
		
		Map<String, Object> filterMap = new HashMap<String, Object>();
		filterMap.put("borrowerUserId", loginUser.getId());
		//标id
		String borrowId = request.getParameter("borrowId");
		if (StringUtils.isNotBlank(borrowId) && StringUtils.isNumeric(borrowId)) {
			filterMap.put("borrowerId", Integer.valueOf(borrowId));
		}
		
		Page<BorrowRepayment> repaymentPage = borrowRepaymentService.getBorrowRepaymentPage(page, DEFAULT_PAGE_SIZE, filterMap);
		
		map.addAttribute("page", repaymentPage);
		map.addAttribute("borrowId", borrowId);
		
		return "myhome/repayment-list";
	}
	
	
	/**
	 * 投资记录
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("webapp/myhome/tender-list")
	public String borrowTenderList(ModelMap map, HttpServletRequest request,HttpServletResponse response){
		User loginUser = getCurrUser(request, response);
		if(loginUser == null){
			return "redirect:/tologin.html";
		}
		int page = 1;
		if (StringUtils.isNotBlank(request.getParameter("page"))) {
			page = Integer.valueOf(request.getParameter("page"));
		}
		
		Map<String, Object> filterMap = new HashMap<String, Object>();
		filterMap.put("userId", loginUser.getId());
		
		//投标状态
		String tenderType = request.getParameter("tenderType");
		if(StringUtils.isBlank(tenderType)){
			tenderType = "tbz";
		}
		if(tenderType.equals("tbz")){//投标中
			/*filterMap.put("borrowStatus_IN", new Integer[]{1,2,3});*/
			filterMap.put("borrowStatus_IN", new Integer[]{Constant.BORROW_STATUS_CSTG,Constant.BORROW_STATUS_MBDFS,Constant.BORROW_STATUS_FSTG});
		}else if(tenderType.equals("hkz")){//还款中
			filterMap.put("borrowStatus", Constant.BORROW_STATUS_HKZ);
		}else if(tenderType.equals("yhk")){//已还款
			//filterMap.put("borrowStatus", Constant.BORROW_STATUS_YHK);
			filterMap.put("borrowStatus", new Integer[]{Constant.BORROW_STATUS_YHK,Constant.BORROW_STATUS_CGWC});
		}
		
		//标id
		String borrowId = request.getParameter("borrowId");
		if (StringUtils.isNotBlank(borrowId) && StringUtils.isNumeric(borrowId)) {
			filterMap.put("borrowerId", Integer.valueOf(borrowId));
		}
		
		Page<BorrowTender> tenderPage = borrowTenderService.getBorrowTenderPage(page, DEFAULT_PAGE_SIZE, filterMap);
		map.addAttribute("page", tenderPage);
		map.addAttribute("borrowId", borrowId);
		map.addAttribute("tenderType", tenderType);
		return "myhome/tender-list";
	}
	
	/**
	 * 收款明细
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("webapp/myhome/collection-list")
	public String borrowCollectionList(ModelMap map, HttpServletRequest request,HttpServletResponse response){
		
		User loginUser = getCurrUser(request, response);
		if(loginUser == null){
			return "redirect:/tologin.html";
		}
		int page = 1;
		if (StringUtils.isNotBlank(request.getParameter("page"))) {
			page = Integer.valueOf(request.getParameter("page"));
		}
		
		Map<String, Object> filterMap = new HashMap<String, Object>();
		filterMap.put("tenderUserId", loginUser.getId());
		//标id
		String borrowId = request.getParameter("borrowId");
		if (StringUtils.isNotBlank(borrowId) && StringUtils.isNumeric(borrowId)) {
			filterMap.put("borrowNo", Integer.valueOf(borrowId));
		}
		//投资id
		String tenderId = request.getParameter("tenderId");
		if (StringUtils.isNotBlank(tenderId) && StringUtils.isNumeric(tenderId)) {
			filterMap.put("tenderId", Integer.valueOf(tenderId));
		}
		
		Page<BorrowCollection> collectionPage = borrowCollectionService.getBorrowCollectionPage(page, DEFAULT_PAGE_SIZE, filterMap);
		map.addAttribute("page", collectionPage);
		map.addAttribute("borrowId", borrowId);
		map.addAttribute("tenderId", tenderId);
		return "myhome/collection-list";
	}
	
	
	/**
	 * 还款页面
	 * @param map
	 * @param request
	 * @param response
	 * @param repaymentId
	 * @return
	 */
	@RequestMapping(value = "webapp/myhome/repayment",method = RequestMethod.GET)
	public String repayment(ModelMap map, HttpServletRequest request,HttpServletResponse response,Long repaymentId){
		
		User user = this.getCurrUser(request, response);
		
		BorrowRepayment repayment = borrowRepaymentService.getRepaymentById(repaymentId);
		
		UserAccount userAccount = userAccountService.getUserAccount(user.getId());
		
		map.addAttribute("repayment",repayment);
		map.addAttribute("userAccount",userAccount);
		map.addAttribute("borrowId", request.getParameter("borrowId"));
		map.addAttribute("errormsg",request.getParameter("errormsg"));
		return "/borrow/repayment";
	}
	
	/**
	 * 还款操作
	 * @param map
	 * @param request
	 * @param response
	 * @param repaymentId
	 * @return
	 */
	@RequestMapping(value = "webapp/myhome/repayment",method = RequestMethod.POST)
	public String doRepayment(ModelMap map, HttpServletRequest request,HttpServletResponse response,Long repaymentId){
		
		User user = getCurrUser(request, response);
		
		String msg = null;
		try {
			msg = borrowRepaymentService.repayment(user.getId(), repaymentId, getAreaByIp(request));
		} catch (RuntimeException e) {
			e.printStackTrace();
			msg = e.getMessage();
			if(msg == null){
				msg = "还款失败";
			}
		}
		map.addAttribute("errormsg", msg);
		map.addAttribute("repaymentId", repaymentId);
		map.addAttribute("borrowId", request.getParameter("borrowId"));
		
		return "redirect:/myhome/repayment.html";
	}
	
	private Long getUserId(HttpServletRequest aRequest, HttpServletResponse aResponse) {
		Long result = 0l;
		boolean flag = false;
		User al = null;
		String strLoginedUser = "";
		String strLoginedPassword = "";
		
		try {
			result = SessionHelper.getUserId(aRequest, aResponse);
			
			if (result == null || result > 0l) {
				Cookie ary[] = aRequest.getCookies();
				if (ary != null && ary.length > 0) {
					for (Cookie item : ary) {
						if (HttpContext.SessionKey.LOGINED_USER.toString().equalsIgnoreCase(item.getName()))
							strLoginedUser = URLDecoder.decode(item.getValue(),"utf-8").replace("#", "@");
							
						if (HttpContext.SessionKey.LOGINED_PASSWORD.toString().equalsIgnoreCase(item.getName()))
							strLoginedPassword = item.getValue();
					}
					
					al = userService.geyByLogInfo(strLoginedUser, strLoginedPassword);
					
					if (al != null)
						flag = true;
				}
			}
			
			if (flag) {
				return al.getId();
			} else {
				return result;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 标详情
	 * @param map
	 * @param request
	 * @param response
	 * @param borrowId
	 * @return
	 */
	@RequestMapping(value = "webapp/borrow/detail/{marketid}",method=RequestMethod.GET)
	public String borrowDetail(ModelMap map, HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable(value = "marketid") Long marketId) {
        //债券转让用
		Market market =null;
		if(marketId!=null&&!"".equals(marketId)){
			market = transferService.queryMarketById(marketId);
			map.addAttribute("marketmoney", market.getTransferPrice());
			map.addAttribute("marketId", marketId);
			map.addAttribute("market", market);
		}
		//查询标的
		try {
			Borrow borrow = borrowService.getBorrowById(market.getBorrowId());
			map.addAttribute("borrow", borrow);
			
			//根据标id查询标详情
//			BorrowInfo borrowinfo = borrowService.queryBorrowInfoById(borrowId);
//			map.addAttribute("borrowinfo", borrowinfo);
					
			if(borrow == null){
				return "redirect:/webapp/market/search.html";
			}
			
			//查询标的投资记录
			List<BorrowTender> tenderList = borrowTenderService.getTenderListByBorrowId(borrow.getId());
			Collections.reverse(tenderList);
			map.addAttribute("tenderList", tenderList);
			
			//查询借款人信息
			User borrowUser = userService.queryByUserId(borrow.getUserId());
			map.addAttribute("borrowUser", borrowUser);
			
			//查询借款成功次数
//			Map<String,Object> filterMap = new HashMap<String, Object>();
//			filterMap.put("userId", borrowUser.getId());
//			filterMap.put("status", Constant.BORROW_STATUS_CGWC);//借款成功
//			int borrowCount = borrowService.getBorrowListCount(filterMap);
//			map.addAttribute("borrowCount", borrowCount);
			
			
			//登录用户 账户资金
			User user = getCurrUser(request, response);
//			map.addAttribute("user", user);
			if(user!=null){
				UserAccount userAccount = userAccountService.getUserAccount(user.getId());
				map.addAttribute("ua", userAccount);
			}
			map.addAttribute("user", user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		map.addAttribute("errormsg", request.getParameter("errormsg"));
		
		return "qgfwebapp/marketDetails";
	}
	
//	/**
//	 * 还款操作
//	 * @param map
//	 * @param request
//	 * @param response
//	 * @param repaymentId
//	 * @return
//	 */
//	@RequestMapping(value = "webapp/touzi")
//	public String touzi(ModelMap map, HttpServletRequest request,HttpServletResponse response,Long repaymentId){
//		
//		User user = getCurrUser(request, response);
//		map.addAttribute("user", user);
//		
//		return "qgfwebapp/ImmediateInvestment";
//	}
	
}