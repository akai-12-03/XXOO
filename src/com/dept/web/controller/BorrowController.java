package com.dept.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dept.web.context.Constant;
import com.dept.web.context.Global;
import com.dept.web.controller.thrid.techSdk.ESignUtil;
import com.dept.web.controller.thrid.techSdk.ProjectConstants;
import com.dept.web.dao.HongbaoDao;
import com.dept.web.dao.model.Article;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowLoan;
import com.dept.web.dao.model.BorrowRepayment;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.Hongbao;
import com.dept.web.dao.model.Market;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountSummary;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.MD5;
import com.dept.web.general.util.MoneyToChinese;
import com.dept.web.general.util.TextUtil;
import com.dept.web.general.util.TimeUtil;
import com.dept.web.general.util.Utils;
import com.dept.web.service.ArticleService;
import com.dept.web.service.BorrowCollectionService;
import com.dept.web.service.BorrowRepaymentService;
import com.dept.web.service.BorrowService;
import com.dept.web.service.BorrowTenderService;
import com.dept.web.service.LoanService;
import com.dept.web.service.TransferService;
import com.dept.web.service.UserAccountService;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;
import com.sendinfo.xspring.ibatis.page.PageUtils;
import com.timevale.esign.sdk.tech.bean.OrganizeBean;
import com.timevale.esign.sdk.tech.bean.PersonBean;
import com.timevale.esign.sdk.tech.bean.result.AddAccountResult;
import com.timevale.esign.sdk.tech.bean.result.LoginResult;
import com.timevale.esign.sdk.tech.bean.result.Result;
import com.timevale.esign.sdk.tech.service.AccountService;
import com.timevale.esign.sdk.tech.service.EsignsdkService;
import com.timevale.esign.sdk.tech.service.factory.AccountServiceFactory;
import com.timevale.esign.sdk.tech.service.factory.EsignsdkServiceFactory;
import com.timevale.esign.sdk.tech.impl.constants.OrganRegType;

/**
 * 标部分
 * 
 * @ClassName: BorrowController.java
 * 
 * @version V1.0
 * @Date 2014-8-15 下午2:08:12 <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class BorrowController extends WebController {

	private Logger Log = Logger.getLogger(BorrowController.class);

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
	private HongbaoDao hongbaoDao;

	@Autowired
	private LoanService loanService;

	@Autowired
	private TransferService transferService;

	/**
	 * 招标记录
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("borrow/toubiao")
	public String borrowList(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(8);
			Map<String, String> params = getParamMap(request);
			createSearchParams(params);
			if (StringUtil.isNotEmpty(params.get("page"))) {
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			pageRequest.setFilters(params);
			Page<Borrow> borrowpage = borrowService
					.searchBorrowList(pageRequest);
			for (Borrow bw : borrowpage.getResult()) {
				Double count = Double.valueOf(bw.getAccount())
						- Double.valueOf(bw.getAccountYes());
				bw.setLastAccount(count);

				int tzcount = borrowService.getTenderList(bw.getId());
				bw.setTzcount(tzcount);
				if (bw.getVerifyTime() != null && bw.getVerifyTime() > 0) {
					bw.setLastTime(TimeUtil.getEndTimeHMS(
							String.valueOf(bw.getVerifyTime()), 0,
							bw.getValidTime(), 0, 0, 0));
				}
			}

			map.addAttribute("borrowType", params.get("borrowType"));
			map.addAttribute("rzqx", params.get("rzqx"));
			map.addAttribute("xmsy", params.get("xmsy"));

			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(
					borrowpage.getTotalCount(), borrowpage.getPageSize()));
			map.addAttribute("page", pageRequest.getPageNumber());
			//
			// //发布时间
			// List<Borrow> baddtimelist=borrowService.getBorrowAddtime();
			// map.addAttribute("baddtimelist", baddtimelist);
			//
			// //预期年化收益
			// List<Borrow> baprlist=borrowService.getBorrowApr();
			// map.addAttribute("baprlist", baprlist);
			//
			// //总金额
			// List<Borrow> baccountlist=borrowService.getBorrowAccount();
			// map.addAttribute("baccountlist", baccountlist);

			return "borrow/list";

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 创建搜索参数
	 * 
	 * @Description: TODO
	 * @param: @param params
	 * @param: @return
	 * @return: Map<String,String>
	 * @throws
	 */
	public Map<String, String> createSearchParams(Map<String, String> params) {
		String borrowType = TextUtil.isNull(params.get("borrowType"));
		if (borrowType == null || "".equals(borrowType)) {
			borrowType = "1";
		}
		String rzqx = TextUtil.isNull(params.get("rzqx"));
		String xmsy = TextUtil.isNull(params.get("xmsy"));
		/**
		 * 理财收益
		 */
		if (xmsy.equals("1")) {
			params.put("lcsysql", "A.apr >=5 and  A.apr <7");
		} else if (xmsy.equals("2")) {
			params.put("lcsysql", "A.apr >= 7 and  A.apr <10");
		} else if (xmsy.equals("3")) {
			params.put("lcsysql", "A.apr >= 10 and  A.apr <=15");
		}

		/**
		 * 理财期限
		 */
		if (rzqx.equals("1")) {
			params.put("lcqxsql",
					"CASE WHEN A.isDay!=1 THEN A.TIME_LIMIT <=1  ELSE a.time_limit<=31 END");
		} else if (rzqx.equals("2")) {
			params.put(
					"lcqxsql",
					"CASE WHEN A.isDay!=1 THEN A.TIME_LIMIT >1 AND a.time_limit<=3 ELSE a.time_limit>31 AND a.time_limit<=90 END");
		} else if (rzqx.equals("3")) {
			params.put(
					"lcqxsql",
					"CASE WHEN A.isDay!=1 THEN A.TIME_LIMIT >3  AND a.time_limit<=6 ELSE a.time_limit>90 AND a.time_limit<=180 END");
		} else if (rzqx.equals("4")) {
			params.put(
					"lcqxsql",
					"CASE WHEN A.isDay!=1 THEN A.TIME_LIMIT >6  AND a.time_limit<=12 ELSE a.time_limit>180 AND a.time_limit<=360 END");
		} else if (rzqx.equals("5")) {
			params.put(
					"lcqxsql",
					"CASE WHEN A.isDay!=1 THEN A.TIME_LIMIT >12  ELSE a.time_limit>90 AND a.time_limit>360 END");
		}

		/**
		 * 标类型
		 */
		if (borrowType.equals("1")) {
			params.put("borrowTypeSql", "A.borrow_type=1");// xx借贷
		} else if (borrowType.equals("2")) {
			params.put("borrowTypeSql", "A.borrow_type=2");// 车贷宝
		} else if (borrowType.equals("3")) {
			params.put("borrowTypeSql", "A.borrow_type=3");// 车贷宝
		} else if (borrowType.equals("4")) {
			params.put("borrowTypeSql", "A.borrow_type=4");// 车贷宝
		} else if (borrowType.equals("5")) {
			params.put("borrowTypeSql", "A.borrow_type=5");// 车贷宝
		}

		// /**
		// * 投资状态
		// */
		// if(xmzt.equals("1")){
		// params.put("tzgmsql", "A.status=1");
		// }else if(xmzt.equals("2")){
		// params.put("tzgmsql", "A.status=5");
		// }else if(xmzt.equals("3")){
		// params.put("tzgmsql", "A.status=6 or A.status=11");
		// }else if(xmzt.equals("4")){
		// params.put("tzgmsql", "A.status =0");
		// }else{
		// params.put("tzgmsql", "A.status in(0,1,3,5,6,11)");
		// }
		return params;
	}

	/**
	 * 精选理财
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @param borrowId
	 * @return
	 */
	@RequestMapping("borrow/toubiaIndex")
	public String licaiIndex(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException,
			Exception {
		List<Borrow> borrowList = borrowService.queryIndexList(6);
		for (Borrow br : borrowList) {
			Double count = Double.valueOf(br.getAccount())
					- Double.valueOf(br.getAccountYes());
			br.setLastAccount(count);

			br.setLastTime(TimeUtil.getEndTime(
					String.valueOf(br.getVerifyTime()), 0, br.getValidTime(),
					0, 0, 0));
			int tzcount = borrowService.getTenderList(br.getId());
			br.setTzcount(tzcount);
		}
		map.addAttribute("borrowList", borrowList);

		List<BorrowTender> btlist = borrowService.getTender();
		// User u=userService.queryByUserId(btlist.get(0).getUserId());
		map.addAttribute("btlist", btlist);

		// 网站公告
		List<Article> ggList = articleService.queryArticleList("wzgg", 0, 5);
		map.addAttribute("ggList", ggList);

		int zrs = userService.getAllUserCount();
		map.addAttribute("zrs", zrs);
		UserAccountSummary uas = userAccountService.getForIndexCount();
		Borrow b = borrowService.getAprs();
		uas.setAprs(b.getAprs());
		map.addAttribute("uas", uas);

		return "borrow/toubiaoIndex";
	}

	/**
	 * 标详情
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @param borrowId
	 * @return
	 */
	@RequestMapping("borrow/detail")
	public String borrowDetail(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, @RequestParam long id) {
		User user = new User();

		user = this.getCurrUser(request, response);
		if (user != null) {
			UserAccount ua = userAccountService.getUserAccount(user.getId());
			map.addAttribute("ua", ua);
			// 查询可使用的红包
			Map<String, String> params = getParamMap(request);
			params.put("userId", String.valueOf(user.getId()));
			params.put("status", "0");
			List<Hongbao> hblist = hongbaoDao
					.queryListHongbaoByIDAndStatus(params);
			map.addAttribute("hblist", hblist);
		}
		map.addAttribute("user", user);

		// 查询标的
		Borrow borrow = borrowService.getBorrowById(id);
		if (borrow == null) {
			return "redirect:/borrow/toubiao.html";
		}
		if (borrow.getVerifyTime() != null && borrow.getVerifyTime() > 0) {
			borrow.setLastTime(TimeUtil.getEndTimeHMS(
					String.valueOf(borrow.getVerifyTime()), 0,
					borrow.getValidTime(), 0, 0, 0));
		} else {
			borrow.setLastTime("");
		}

		int tzcount = borrowService.getTenderList(borrow.getId());
		borrow.setTzcount(tzcount);
		Double count = Double.valueOf(borrow.getAccount())
				- Double.valueOf(borrow.getAccountYes());
		borrow.setLastAccount(count);
		map.addAttribute("borrow", borrow);

		// 查询标的投资记录
		List<BorrowTender> tenderList = borrowTenderService
				.getTenderListByBorrowId(borrow.getId());
		map.addAttribute("tenderList", tenderList);

		// 查询借款人信息
		User borrowUser = userService.queryByUserId(borrow.getUserId());
		map.addAttribute("borrowUser", borrowUser);
		map.addAttribute("errormsg", request.getParameter("errormsg"));

		return "borrow/detail";
	}

	/**
	 * 投标操作
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @param borrowId
	 * @return
	 */
	@RequestMapping(value = "borrow/tender")
	public String borrowTender(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, @RequestParam long id) {

		User user = getCurrUser(request, response);
		if (user == null) {
			return "redirect:/tologin.html";
		}

		Borrow borrow = borrowService.getBorrowById(id);
		if (borrow == null) {
			return "redirect:/borrow/toubiao.html";
		}
		
		if(user.getAutoType()==null||user.getAutoType()!=1)
		{
			MoneymoremoreId=user.getMoneymoremoreId();
			return "redirect:/qddApi/loanAuthorize.html?MoneymoremoreId="+MoneymoremoreId;
//			map.addAttribute("errormsg", "您未授权请先进行授权操作");
//			map.addAttribute("user", user);
//			return "myhome/openAuth";
		}

		String BORROW_INFO = "redirect:/borrow/detail.html?id=" + id + "";

		if (borrow.getBorrowType() == 5) {
			int count = borrowTenderService
					.getTenderCountByUserId(user.getId());
			if (count > 0) {
				map.addAttribute("errormsg", "您已经有过投资记录，不能再投新手标");
				return BORROW_INFO;
			}
		}

		if (borrow.getStatus() != 1) {
			map.addAttribute("errormsg", "该标不能购买");
			return BORROW_INFO;
		}

		if (user.getId().equals(borrow.getUserId())) {
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

		UserAccount userAccount = userAccountService.getUserAccount(user
				.getId());
		if (userAccount == null) {
			userAccount = new UserAccount();
		}

		String tendermoneyStr = request.getParameter("tendermoney");
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		if (!pattern.matcher(tendermoneyStr).matches()) {
			map.addAttribute("errormsg", "请输入正确的投标金额");
			return BORROW_INFO;
		}
		// 投资金额
		double tenderMoney = 0.0;
		if (com.dept.web.general.util.StringUtils.isNumber(tendermoneyStr)) {
			tenderMoney = Utils.getDouble(Double.valueOf(tendermoneyStr), 2);
		} else {
			map.addAttribute("errormsg", "请输入正确的投标金额");
			return BORROW_INFO;
		}

		double lowAccount = borrow.getLowestAccount();
		if (tenderMoney % lowAccount != 0) {
			map.addAttribute("errormsg", "请投该标的最低金额的整数倍金额");
			return BORROW_INFO;
		}

		double moneyUsable = Utils.getDouble(userAccount.getMoneyUsable(), 2);// 可以余额
		double moneyLowest = Utils.getDouble(borrow.getLowestAccount(), 2);// 最少投资
		double moneyMost = Utils.getDouble(borrow.getMostAccount(), 2);// 最多投资
		double moneyAccount = Utils.getDouble(borrow.getAccount(), 2);// 总金额
		double moneyYes = Utils.getDouble(borrow.getAccountYes(), 2);// 已投金额
		double moneyNo = moneyAccount - moneyYes;

		// if(user.getCardBindingStatus()==null||user.getCardBindingStatus()!=1){
		// map.addAttribute("errormsg", "未绑定银行卡不能进行投标操作");
		// return BORROW_INFO;
		// }

		if (moneyUsable < tenderMoney) {
			map.addAttribute("errormsg", "账户可用余额不足");
			return BORROW_INFO;
		}

		if (moneyNo <= 0) {
			map.addAttribute("errormsg", "此标已满! ");
			return BORROW_INFO;
		}
		if (tenderMoney > moneyNo) {
			map.addAttribute("errormsg", "投资金额不能大于剩余可投金额");
			return BORROW_INFO;
		}

		if (moneyNo > tenderMoney) {
			if (tenderMoney < moneyLowest) {
				map.addAttribute("errormsg", "投资金额不能小于最小限制金额");
				return BORROW_INFO;
			}
		}
		if (tenderMoney > moneyMost) {
			map.addAttribute("errormsg", "投资金额不能大于最大限制金额");
			return BORROW_INFO;
		}
		String hongbaoId = request.getParameter("hongbaoId");
		String url = "redirect:/qddApi/tenderOrderNo.html?id=" + id
				+ "&tendermoney=" + tenderMoney;
		if (hongbaoId != null && !"".equals(hongbaoId.trim())) {
			url += "&tenderhongbao=" + hongbaoId;
		}
		return url;
	}

	/**
	 * 我的借款
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("myhome/borrow-list")
	public String myBorrowList(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		User loginUser = getCurrUser(request, response);
		if (loginUser == null) {
			return "redirect:/tologin.html";
		}
		int page = 1;
		if (StringUtils.isNotBlank(request.getParameter("page"))) {
			page = Integer.valueOf(request.getParameter("page"));
		}

		int status = 1;
		if (StringUtils.isNotBlank(request.getParameter("status"))) {
			status = Integer.valueOf(request.getParameter("status"));
		}

		Map<String, Object> filterMap = new HashMap<String, Object>();
		filterMap.put("userId", loginUser.getId());
		filterMap.put("status", status);
		Page<Borrow> borrowPage = borrowService.getBorrowPage(page,
				DEFAULT_PAGE_SIZE, filterMap);

		map.addAttribute("page", borrowPage);
		map.addAttribute("status", status);
		return "myhome/borrow-list";
	}

	/**
	 * 还款明细
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("myhome/repayment-list")
	public String borrowRepaymentList(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		User loginUser = getCurrUser(request, response);
		if (loginUser == null) {
			return "redirect:/tologin.html";
		}
		int page = 1;
		if (StringUtils.isNotBlank(request.getParameter("page"))) {
			page = Integer.valueOf(request.getParameter("page"));
		}

		Map<String, Object> filterMap = new HashMap<String, Object>();
		filterMap.put("borrowerUserId", loginUser.getId());
		// 标id
		String borrowId = request.getParameter("borrowId");
		if (StringUtils.isNotBlank(borrowId) && StringUtils.isNumeric(borrowId)) {
			filterMap.put("borrowerId", Integer.valueOf(borrowId));
		}

		Page<BorrowRepayment> repaymentPage = borrowRepaymentService
				.getBorrowRepaymentPage(page, DEFAULT_PAGE_SIZE, filterMap);

		map.addAttribute("page", repaymentPage);
		map.addAttribute("borrowId", borrowId);

		return "myhome/repayment-list";
	}

	/**
	 * 投资记录
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("myhome/tender-list")
	public String borrowTenderList(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		User loginUser = getCurrUser(request, response);
		if (loginUser == null) {
			return "redirect:/tologin.html";
		}
		int page = 1;
		if (StringUtils.isNotBlank(request.getParameter("page"))) {
			page = Integer.valueOf(request.getParameter("page"));
		}

		Map<String, Object> filterMap = new HashMap<String, Object>();
		filterMap.put("userId", loginUser.getId());

		// 投标状态
		String tenderType = request.getParameter("tenderType");
		if (StringUtils.isBlank(tenderType)) {
			tenderType = "tbz";
		}
		if (tenderType.equals("tbz")) {// 投标中
			/* filterMap.put("borrowStatus_IN", new Integer[]{1,2,3}); */
			filterMap.put("borrowStatus_IN", new Integer[] {
					Constant.BORROW_STATUS_CSTG, Constant.BORROW_STATUS_MBDFS,
					Constant.BORROW_STATUS_FSTG });
		} else if (tenderType.equals("hkz")) {// 还款中
			filterMap.put("borrowStatus", Constant.BORROW_STATUS_HKZ);
		} else if (tenderType.equals("yhk")) {// 已还款
			// filterMap.put("borrowStatus", Constant.BORROW_STATUS_YHK);
			filterMap.put("borrowStatus", new Integer[] {
					Constant.BORROW_STATUS_YHK, Constant.BORROW_STATUS_CGWC });
		}

		// 标id
		String borrowId = request.getParameter("borrowId");
		if (StringUtils.isNotBlank(borrowId) && StringUtils.isNumeric(borrowId)) {
			filterMap.put("borrowerId", Integer.valueOf(borrowId));
		}

		Page<BorrowTender> tenderPage = borrowTenderService
				.getBorrowTenderPage(page, DEFAULT_PAGE_SIZE, filterMap);
		map.addAttribute("page", tenderPage);
		map.addAttribute("borrowId", borrowId);
		map.addAttribute("tenderType", tenderType);
		return "myhome/tender-list";
	}

	/**
	 * 收款明细
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("myhome/collection-list")
	public String borrowCollectionList(ModelMap map,
			HttpServletRequest request, HttpServletResponse response) {

		User loginUser = getCurrUser(request, response);
		if (loginUser == null) {
			return "redirect:/tologin.html";
		}
		int page = 1;
		if (StringUtils.isNotBlank(request.getParameter("page"))) {
			page = Integer.valueOf(request.getParameter("page"));
		}

		Map<String, Object> filterMap = new HashMap<String, Object>();
		filterMap.put("tenderUserId", loginUser.getId());
		// 标id
		String borrowId = request.getParameter("borrowId");
		if (StringUtils.isNotBlank(borrowId) && StringUtils.isNumeric(borrowId)) {
			filterMap.put("borrowNo", Integer.valueOf(borrowId));
		}
		// 投资id
		String tenderId = request.getParameter("tenderId");
		if (StringUtils.isNotBlank(tenderId) && StringUtils.isNumeric(tenderId)) {
			filterMap.put("tenderId", Integer.valueOf(tenderId));
		}

		Page<BorrowCollection> collectionPage = borrowCollectionService
				.getBorrowCollectionPage(page, DEFAULT_PAGE_SIZE, filterMap);
		map.addAttribute("page", collectionPage);
		map.addAttribute("borrowId", borrowId);
		map.addAttribute("tenderId", tenderId);
		return "myhome/collection-list";
	}

	/**
	 * 还款页面
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @param repaymentId
	 * @return
	 */
	@RequestMapping(value = "myhome/repayment", method = RequestMethod.GET)
	public String repayment(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, Long repaymentId) {

		User user = this.getCurrUser(request, response);

		BorrowRepayment repayment = borrowRepaymentService
				.getRepaymentById(repaymentId);

		UserAccount userAccount = userAccountService.getUserAccount(user
				.getId());

		map.addAttribute("repayment", repayment);
		map.addAttribute("userAccount", userAccount);
		map.addAttribute("borrowId", request.getParameter("borrowId"));
		map.addAttribute("errormsg", request.getParameter("errormsg"));
		return "/borrow/repayment";
	}

	/**
	 * 还款操作
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @param repaymentId
	 * @return
	 */
	@RequestMapping(value = "myhome/repayment", method = RequestMethod.POST)
	public String doRepayment(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, Long repaymentId) {

		User user = getCurrUser(request, response);

		String msg = null;
		try {
			msg = borrowRepaymentService.repayment(user.getId(), repaymentId,
					getAreaByIp(request));
		} catch (RuntimeException e) {
			e.printStackTrace();
			msg = e.getMessage();
			if (msg == null) {
				msg = "还款失败";
			}
		}
		map.addAttribute("errormsg", msg);
		map.addAttribute("repaymentId", repaymentId);
		map.addAttribute("borrowId", request.getParameter("borrowId"));

		return "redirect:/myhome/repayment.html";
	}

	@RequestMapping("borrow/loan")
	public String loan(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		User user = getCurrUser(request, response);
		if (user != null) {
			Borrow borrow = borrowService.queryForLoan();
			map.put("borrow", borrow);
			return "borrow/loan";
		} else {
			return "redirect:/tologin.html";
		}
	}

	/**
	 * @desc 用途描述: 我要借款
	 * @param map
	 * @param request
	 * @param response
	 * @param repaymentId
	 * @return 返回说明:
	 * @exception 内部异常说明
	 *                :
	 * @throws 抛出异常说明
	 *             :
	 * @author gwx
	 * @version 1.0
	 * @created 2016-2-24 下午4:36:55
	 * @mod 修改描述:
	 * @modAuthor 修改人:
	 */
	@RequestMapping("borrow/loanBorrow")
	public String loanBorrow(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		User user = getCurrUser(request, response);
		if (user != null) {
			String imgCode = request.getParameter("imgCode");
			if (StringUtils.isEmpty(imgCode)
					|| !imgCode.equals(session.getAttribute("captchaToken"))) {
				map.addAttribute("msg", "验证码不存在或错误！");
				return "borrow/loan";
			}
			BorrowLoan loan = new BorrowLoan();
			String realname = request.getParameter("realname");
			String phone = request.getParameter("phone");
			if (realname == null || "".equals(realname.trim())) {
				map.addAttribute("msg", "请输入真实姓名");
				return "borrow/loan";
			}
			if (realname == null || "".equals(phone.trim())) {
				map.addAttribute("msg", "请输入手机号码或联系方式");
				return "borrow/loan";
			}
			loan.setRealname(realname);
			loan.setStatus(0);
			loan.setPhone(phone);
			loan.setAddtime(TimeUtil.getNowTimeStr());
			loan.setAddUserid(user.getId());
			loan.setCreatedIp(getRequestIp(request));
			loan.setCarStatus(Integer.valueOf(request.getParameter("carStatus")));
			loanService.addLoan(loan);
			map.addAttribute("msg", "发起成功，等待管理员审核");
			return "borrow/loan";
		} else {
			return "redirect:/tologin.html";
		}
	}

	/**
	 * 生成项目合同
	 * 
	 * @Title: 生成项目合同
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("borrow/general/protocol")
	public String genderalProtocol(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, Long btId) throws Exception {
		User user = new User();
		user = this.getCurrUser(request, response);
		if (user == null) {
			return "redirect:/login.html";
		}

		map.addAttribute("msg", "没有找到投资记录");
		map.addAttribute("code", "0");

		/************ init e-sign ************/
		Integer userType = user.getUserType();
		if (StringUtil.isEmpty(String.valueOf(userType))) {
			map.put("msg", "请先进行实名认证");
			return "to_success";
		} else {
			// e签宝初始化信息
			EsignsdkService SDK = EsignsdkServiceFactory.instance();

			// e签宝初始化服务
			Log.info("e签宝开始执行初始化...");
			request.getSession().invalidate();

			Result result = SDK.init(ProjectConstants.PROJECTID,
					ProjectConstants.PROJECT_SECRET);
			if (0 != result.getErrCode()) {
				Log.debug("e签宝初始化失败，错误码=" + result.getErrCode() + ",错误信息="
						+ result.getMsg());
				// map.addAttribute(ERRORMSG, "e签宝初始化失败");
				putCurrUser(request, response, user);
				map.put("msg", "签章初始化失败");
				return "to_success";
			}

			Log.info("e签宝初始化成功，开始执行项目登录...");

			// 使用初始化设置的项目编号和项目验证码做项目账户登录
			LoginResult loginResult = SDK.login();
			if (0 != loginResult.getErrCode()) {
				Log.debug("e签宝验证处理失败！");
				// map.addAttribute(ERRORMSG, "e签宝验证处理失败！");
				putCurrUser(request, response, user);
				map.put("msg", "签章验证失败");
				return "to_success";
			}
			// 设置项目账户标识，后续接口调用时可以从session中获取
			request.getSession().setAttribute("devId",
					loginResult.getAccountId());
			Log.info("e签宝验证处理成功! accountId:" + loginResult.getAccountId());

			AccountService SERVICE = AccountServiceFactory.instance();
			String devId = (String) request.getSession().getAttribute("devId");
			AddAccountResult r = null;
			// 个人用户
			if (userType == Constant.PERSON) {
				PersonBean psn = new PersonBean();
				psn.setAddress("地址")
						.setEmail(user.getMobile() + "@timevla.com")
						.setMobile(user.getMobile())
						.setName(user.getRealname()).setIdNo(user.getIdCard())
						.setOrgan("我在这里工作").setTitle("这是我的职位");

				r = SERVICE.addAccount(devId, psn);
			} else if (userType == Constant.ENTERPRISE) {
				OrganizeBean org = new OrganizeBean();
				org.setMobile(user.getMobile())
				.setName(user.getRealname())
				.setAgentIdNo("330125197210051844")
		        .setAgentName("强红樱")
		        .setLegalIdNo("330125197210051844")
		        .setLegalName("强红樱")
				.setOrganCode(user.getOrganization())
				.setUserType(1);
				if (!StringUtils.isEmpty(user.getOrganization()) && user.getOrganization().length() == 18) {
					org.setRegType(OrganRegType.MERGE);
				} else {
					org.setRegType(OrganRegType.NORMAL);
				}
				r = SERVICE.addAccount(devId, org);
			}
			if (0 != r.getErrCode() || null == r.getAccountId()
					|| r.getAccountId().isEmpty()) {
				// Tools.forward(req, resp, "/error.jsp");
				putCurrUser(request, response, user);
				map.put("msg", "签章登录失败" + r.getMsg());
				return "to_success";
			}

			// 保存用户账户
			request.getSession().setAttribute("userId", r.getAccountId());
			user.setLoginAccountId(r.getAccountId());
			userService.userUpdate(user);
			putCurrUser(request, response, user);
		}

		BorrowTender bt = borrowTenderService.getUserBorrowTenderById(btId); // 投资成功的
		Borrow borrow = borrowService.getBorrowById(bt.getBorrowId());
		if (borrow == null || bt == null) {
			map.put("msg", "没有找到投资项目");
			return "to_success";
		}

		User jiekuanUser = userService.queryByUserId(borrow.getUserId());
		if (jiekuanUser == null) {
			map.put("msg", "没有找到借款人信息");
			return "to_success";
		}

		User tenderUser = user;

		// generate file name
		String pathdir = borrow.getId().toString();
		// 得到图片保存目录的真实路径
		String realpathdir = request.getSession().getServletContext()
				.getRealPath(pathdir);
		File savedir = new File(realpathdir);
		if (!savedir.exists())
			savedir.mkdirs();// 如果目录不存在就创建

		MD5 md5 = new MD5();
		String fileNameSuffix = md5.getMD5ofStr(String.valueOf(bt.getId()));

		String fileNameUnsigned = "unsign_" + fileNameSuffix + ".pdf";
		String fileNameSigned = "signed_" + fileNameSuffix + ".pdf";

		String filePathUnsigned = savedir + File.separator + fileNameUnsigned;
		String filePathSigned = savedir + File.separator + fileNameSigned;

		String filePathUnsignedRelative = pathdir + File.separator
				+ fileNameUnsigned;
		String filePathSignedRelative = pathdir + File.separator
				+ fileNameSigned;

		String filePathSignedPersonTmp = savedir + File.separator
				+ "signed_tmp_" + fileNameSuffix + ".pdf";// 个人
		String filePathSignedPersonTmpRelative = pathdir + File.separator
				+ "signed_tmp_" + fileNameSuffix + ".pdf";// 个人

		String filePathSignedBorrowTmp1 = savedir + File.separator
				+ "signed_tmp1_" + fileNameSuffix + ".pdf";// 借款人
		String filePathSignedBorrowTmpRelative1 = pathdir + File.separator
				+ "signed_tmp1_" + fileNameSuffix + ".pdf";// 借款人

		String filePathSignedGuaranteeTmp2 = savedir + File.separator
				+ "signed_tmp2_" + fileNameSuffix + ".pdf";// 担保
		String filePathSignedGuaranteeTmpRelative2 = pathdir + File.separator
				+ "signed_tmp2_" + fileNameSuffix + ".pdf";// 担保

		File fUnsigned = new File(filePathUnsigned);
		if (!fUnsigned.exists()) {
			String repaymentStyleName = "";
			// 还款方式
			int repaymentStyle = borrow.getRepaymentStyle();
			if (repaymentStyle == 1) {
				repaymentStyleName = "等额本息";
			} else if (repaymentStyle == 2) {
				repaymentStyleName = "到期还本还息";
			} else if (repaymentStyle == 3) {
				repaymentStyleName = "按月付息到期还本";
			} else if (repaymentStyle == 4) {
				repaymentStyleName = "按季付息到期还本";
			}
			// @Description: TODO
			Article article = articleService.queryArtById(229L);// 正式环境

			String artcontent = article.getContent();

			artcontent = artcontent.replace("xyNo",
					DateUtils.dateStr(bt.getAddtime(), "yyyyMMddHHmmss") + "-"
							+ borrow.getId() + "-" + bt.getId());// 协议编号

			artcontent = artcontent.replace("jiekuanUser",
					jiekuanUser.getRealname());// 甲方（借款人）
			artcontent = artcontent.replace("jiekuanRealName",
					jiekuanUser.getRealname());// 甲方（借款人真实姓名）
			artcontent = artcontent.replace("jiekuanRRCUserName",
					jiekuanUser.getUsername());// 日日昌平台用户名
			if(jiekuanUser.getUserType()==1)
			{
				artcontent = artcontent.replace("userType",
						"身份证号码"); 
				artcontent = artcontent.replace("jiekuanCardNo",
						jiekuanUser.getIdCard()); // 身份证号码 /组织机构代码
			}
			else
			{
				artcontent = artcontent.replace("userType",
						"组织机构代码"); // 身份证号码 /组织机构代码
				artcontent = artcontent.replace("jiekuanCardNo",
						jiekuanUser.getOrganization()); // 身份证号码 /组织机构代码
			}
			
			artcontent = artcontent.replace("jiekuanAddress", ""); // 通讯地址

			artcontent = artcontent.replace("jikuanMobile",
					jiekuanUser.getMobile()); // 联系方式

			artcontent = artcontent.replace("tenderUser",
					tenderUser.getRealname());// 乙方（投资人）
			artcontent = artcontent.replace("tenderRRCUserName",
					tenderUser.getUsername());// 日日昌平台用户名
			artcontent = artcontent.replace("tenderRealName",
					tenderUser.getRealname());// 已方（投资人真实姓名）
			artcontent = artcontent.replace("tenderCardNo",
					tenderUser.getIdCard()); // 身份证号码 /组织机构代码
			artcontent = artcontent.replace("tenderAddress", ""); // 通讯地址

			artcontent = artcontent.replace("tenderMobile",
					tenderUser.getMobile()); // 联系方式

			artcontent = artcontent.replace("jiekuanTotalMoney",
					String.valueOf(bt.getMoney())); // 投资金额

			artcontent = artcontent.replace("moneyChinese",
					MoneyToChinese.getMoneyString(bt.getMoney())); // 投资金额 大写

			artcontent = artcontent.replace("apr",
					String.valueOf(borrow.getApr())); // 借款利率

			artcontent = artcontent.replace("borrowUse",
					String.valueOf(borrow.getBorrowUse())); // 借款用途

			String signProtocolTime = getSpecifiedDayAfter(DateUtils.dateStr(
					borrow.getVerifyTime(), "yyyy年MM月dd日"));

			artcontent = artcontent.replace("qdrq", signProtocolTime); // 借款期限开始

			artcontent = artcontent.replace("account",
					String.valueOf(bt.getAccount()));

			artcontent = artcontent.replace("allMoney",
					String.valueOf(bt.getRepaymentAccount()));

			long endTime = 0;
			if (borrow.getIsDay() == 1) {
				artcontent = artcontent.replace("period", borrow.getTimeLimit()
						+ "天");
				endTime = DateUtils.rollDay(borrow.getVerifyTime(),
						borrow.getTimeLimit());
			} else {
				artcontent = artcontent.replace("period", borrow.getTimeLimit()
						+ "个月");
				endTime = DateUtils.rollMonth(borrow.getVerifyTime(),
						borrow.getTimeLimit());
			}

			artcontent = artcontent.replace("jiekuanEndTime",
					getSpecifiedDayAfter(DateUtils.dateStr(endTime,
							"yyyy年MM月dd日"))); // 借款期限结束

			Date s = DateUtils.getDate(endTime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			String time = sdf.format(s);

			// 借款期限共计
			artcontent = artcontent.replace("jiekuanTimeLimit", String
					.valueOf(DateUtils.daysBetween2(signProtocolTime, time))); // 借款期限

			artcontent = artcontent.replace("repaymentStyle",
					repaymentStyleName);// 还款方式

			// 到期还款日
			// 满标放款日 + 借款期限
			artcontent = artcontent.replace("repaymentTime",
					getSpecifiedDayAfter(DateUtils.dateStr(endTime,
							"yyyy年MM月dd日")));

			artcontent = artcontent.replace("signProtocolTime",
					signProtocolTime); // 签署合同时间 满标复审时间+1天开始计息

			generatePDF(artcontent, savedir, fileNameUnsigned);

			map.addAttribute("msg", "融资借款协议生成成功!");
		}

		File fSigned = new File(filePathSigned);
		if (!fSigned.exists()) {
			String accountType = (String) request.getSession().getAttribute(
					"accountType");
			if (userType == Constant.PERSON) {
				accountType = "person";// e签宝个人
			} else {
				accountType = "organize";// e签宝企业
			}
			request.getSession().setAttribute("accountType", accountType);

			request.getSession().setAttribute("userId",
					user.getLoginAccountId());

			ESignUtil esignUtil = new ESignUtil();
			String page = "1";
			boolean result = false;
			if (userType == Constant.PERSON) {
				result = esignUtil.SignPerson(request, response,
						filePathUnsignedRelative,
						filePathSignedPersonTmpRelative, page, 130, 450);
				if (!result) {
					map.put("msg", "签章个人失败");
					return "to_success";
				}
			} else {
				result = esignUtil.SignOrganize(request, response,
						filePathUnsignedRelative,
						filePathSignedPersonTmpRelative, page, 130, 450);
				if (!result) {
					map.put("msg", "签章企业失败");
					return "to_success";
				}
			}

			// String orgId = "34148054-0";
			// jiekuanUser.setRealname("杭州互联网金融协会");
			// jiekuanUser.setMobile("13713700102");
			// String idCard = "330381199005296413";
			// jiekuanUser.setIdCard(idCard);
			// jiekuanUser.setOrganization(orgId);
			// init organizer借款人企业
			AccountService SERVICE = AccountServiceFactory.instance();
			String devId = (String) request.getSession().getAttribute("devId");
			AddAccountResult r = null;

			if (jiekuanUser.getUserType() == Constant.PERSON) {
				PersonBean psn = new PersonBean();
				psn.setAddress("地址")
						.setEmail(jiekuanUser.getMobile() + "@timevla.com")
						.setMobile(jiekuanUser.getMobile())
						.setName(jiekuanUser.getRealname())
						.setIdNo(jiekuanUser.getIdCard()).setOrgan("我在这里工作")
						.setTitle("这是我的职位");

				r = SERVICE.addAccount(devId, psn);
				request.getSession().setAttribute("accountType", "person");
			} else {

				OrganizeBean org = new OrganizeBean();
				org.setMobile(jiekuanUser.getMobile())
						.setUserType(1)
						.setName(jiekuanUser.getRealname())
						.setAgentIdNo("330125197210051844")
				        .setAgentName("强红樱")
				        .setLegalIdNo("330125197210051844")
				        .setLegalName("强红樱")
						.setOrganCode(jiekuanUser.getOrganization());

				if (!StringUtils.isEmpty(jiekuanUser.getOrganization()) && jiekuanUser.getOrganization().length() == 18) {
					org.setRegType(OrganRegType.MERGE);
				} else {
					org.setRegType(OrganRegType.NORMAL);
				}
				r = SERVICE.addAccount(devId, org);
				request.getSession().setAttribute("accountType", "organize");
			}

			if (0 != r.getErrCode() || null == r.getAccountId()
					|| r.getAccountId().isEmpty()) {
				// Tools.forward(req, resp, "/error.jsp");
				map.put("msg", "签章借款人失败" + r.getMsg());
				return "to_success";
			}
			jiekuanUser.setLoginAccountId(r.getAccountId());
			jiekuanUser.setTechSignStatus(1);
			userService.userUpdate(jiekuanUser);

			request.getSession().setAttribute("userId", r.getAccountId());

			if (jiekuanUser.getUserType() == Constant.PERSON) {
				result = esignUtil.SignPerson(request, response,
						filePathSignedPersonTmpRelative,
						filePathSignedBorrowTmpRelative1, page, 240, 450);
				if (!result) {
					map.put("msg", "签章个人失败");
					return "to_success";
				}
			} else {
				result = esignUtil.SignOrganize(request, response,
						filePathSignedPersonTmpRelative,
						filePathSignedBorrowTmpRelative1, page, 240, 450);
				if (!result) {
					map.put("msg", "签章企业失败");
					return "to_success";
				}
			}

			// init SignGuaranteeCompany担保公司
			// AccountService GuaranteeSERVICE =
			// AccountServiceFactory.instance();
			// AddAccountResult gr = null;
			//
			// String guarantee_company =
			// Global.getValue("guarantee_company");//丙方担保公司（担保人）
			// String glegalRepresentative =
			// Global.getValue("glegalRepresentative");//丙方担保公司（法定代表人）
			// String guarantee_organization =
			// Global.getValue("guarantee_organization");//丙方担保公司（组织机构号）
			// String guarantee_moblie =
			// Global.getValue("guarantee_moblie");//丙方担保公司（手机号）
			// String guarantee_idCard =
			// Global.getValue("guarantee_idCard");//丙方担保公司（法人的身份证）
			// OrganizeBean orgg = new OrganizeBean();
			// orgg.setAddress("地址").setEmail(guarantee_moblie +
			// "@timevla.com").setMobile(guarantee_moblie)
			// .setName(guarantee_company).setAgentIdNo(guarantee_idCard).setAgentName(glegalRepresentative)
			// .setLegalArea(0).setLegalIdNo("110000197609260652")
			// .setLegalName(glegalRepresentative).setOrganCode(guarantee_organization)
			// .setOrganType(1).setRegCode(guarantee_organization).setScope("这是我的经营范围")
			// .setUserType(2);
			// gr = GuaranteeSERVICE.addAccount(devId, orgg);
			//
			// if (0 != gr.getErrCode() || null == gr.getAccountId() ||
			// gr.getAccountId().isEmpty()) {
			// // Tools.forward(req, resp, "/error.jsp");
			// map.put("msg", "签章担保公司失败！"+ gr.getMsg());
			// return "to_success";
			// }
			//
			// request.getSession().setAttribute("accountType", "organize");
			// request.getSession().setAttribute("userId", gr.getAccountId());
			//
			// result = esignUtil.SignGuaranteeCompany(request, response,
			// filePathSignedBorrowTmpRelative1,
			// filePathSignedGuaranteeTmpRelative2);
			// if(!result) {
			// map.put("msg", "签章担保公司失败");
			// return "to_success";
			// } else {
			// File tmpfile = new File(filePathSignedBorrowTmp1);
			// tmpfile.delete();
			// }

			// SignPlatform平台
			// result = esignUtil.SignPlatform(request, response,
			// filePathSignedGuaranteeTmpRelative2, filePathSignedRelative);
			// if(!result) {
			// map.put("msg", "签章平台失败");
			// return "to_success";
			// } else {
			// File tmpfile = new File(filePathSignedGuaranteeTmp2);
			// tmpfile.delete();
			// }
		}

		return "redirect:/borrow/download/protocol.html?borrowTenderId="
				+ bt.getId() + "&borrowId=" + bt.getBorrowId();
	}

	public static String getSpecifiedDayAfter(String specifiedDay) {

		Calendar c = Calendar.getInstance();

		Date date = null;

		try {

			date = new SimpleDateFormat("yy年MM月dd日").parse(specifiedDay);

		} catch (ParseException e) {

			e.printStackTrace();

		}

		c.setTime(date);

		int day = c.get(Calendar.DATE);

		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy年MM月dd日").format(c
				.getTime());

		return dayAfter;

	}

	public static void generatePDF(String htmlCode, File dir, String pdfPath)
			throws Exception {

		Document doc = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(new File(dir,
					pdfPath)));
			doc.open();

			// 解决中文问题
			com.lowagie.text.pdf.BaseFont bfChinese = BaseFont.createFont(
					"STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			Font FontChinese = new Font(bfChinese, 12, Font.NORMAL);

			Paragraph p1 = new Paragraph("\n互联网融资借款协议", new Font(bfChinese, 15,
					Font.BOLD));
			p1.setAlignment(1);
			doc.add(p1);

			Paragraph t = new Paragraph(htmlCode, FontChinese);
			doc.add(t);

			doc.close();
			System.out.println("文档创建成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 标详情
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @param borrowId
	 * @return
	 */
	@RequestMapping(value = "borrow/detail/{borrowid}", method = RequestMethod.GET)
	public String borrowDetail(ModelMap map, HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable(value = "borrowid") Long borrowId) {
		// 债券转让用
		String marketId = request.getParameter("marketId");
		if (marketId != null && !"".equals(marketId)) {
			Market market = transferService.queryMarketById(Long
					.parseLong(marketId));
			map.addAttribute("marketmoney", market.getTransferPrice());
			map.addAttribute("marketId", marketId);
			map.addAttribute("market", market);
			
			BorrowTender tender=borrowService.queryTenderById(market.getTenderId());
			
			double waitMoney=tender.getWaitAccount()+tender.getWaitInterest();
			map.addAttribute("waitMoney", waitMoney);
		}
		// 查询标的
		try {
			Borrow borrow = borrowService.getBorrowById(borrowId);
			map.addAttribute("borrow", borrow);

			// 根据标id查询标详情
			// BorrowInfo borrowinfo =
			// borrowService.queryBorrowInfoById(borrowId);
			// map.addAttribute("borrowinfo", borrowinfo);

			if (borrow == null) {
				return "redirect:/borrow/toubiao.html";
			}

			// 查询标的投资记录
			List<BorrowTender> tenderList = borrowTenderService
					.getTenderListByBorrowId(borrow.getId());
			Collections.reverse(tenderList);
			map.addAttribute("tenderList", tenderList);

			// 查询借款人信息
			User borrowUser = userService.queryByUserId(borrow.getUserId());
			map.addAttribute("borrowUser", borrowUser);

			// 查询借款成功次数
			// Map<String,Object> filterMap = new HashMap<String, Object>();
			// filterMap.put("userId", borrowUser.getId());
			// filterMap.put("status", Constant.BORROW_STATUS_CGWC);//借款成功
			// int borrowCount = borrowService.getBorrowListCount(filterMap);
			// map.addAttribute("borrowCount", borrowCount);

			// 登录用户 账户资金
			User user = getCurrUser(request, response);
			// map.addAttribute("user", user);
			if (user != null) {
				UserAccount userAccount = userAccountService
						.getUserAccount(user.getId());
				map.addAttribute("ua", userAccount);
			}
			map.addAttribute("user", user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		map.addAttribute("errormsg", request.getParameter("errormsg"));

		return "borrow/assignment-Detail";
	}

	/**
	 * @param map
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @param borrowTenderId
	 * @param borrowId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("borrow/download/protocol")
	public String downloadProtocol(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, @RequestParam Long borrowTenderId,
			@RequestParam Long borrowId) throws Exception {

		String pathdir = borrowId.toString();

		MD5 md5 = new MD5();
		String fileNameSuffix = md5.getMD5ofStr(String.valueOf(borrowTenderId));

		String fileNameSigned = "signed_tmp1_" + fileNameSuffix + ".pdf";
		// String filePathSignedRelative = pathdir + File.separator +
		// fileNameSigned;

		// 得到图片保存目录的真实路径
		String realpathdir = request.getSession().getServletContext()
				.getRealPath(pathdir);
		try {
			this.downFile(response, realpathdir, fileNameSigned);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("文件下载出错", e);
		}
		return null;
	}

	/**
	 * @param map
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @param borrowTenderId
	 * @param borrowId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("borrow/download/marketProtocol")
	public String marketProtocol(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, @RequestParam Long borrowTenderId,
			@RequestParam Long borrowId) throws Exception {

		String pathdir = borrowId.toString();

		MD5 md5 = new MD5();
		String fileNameSuffix = md5.getMD5ofStr(String.valueOf(borrowTenderId));

		String fileNameSigned = "market_tmp1_" + fileNameSuffix + ".pdf";
		// String filePathSignedRelative = pathdir + File.separator +
		// fileNameSigned;

		// 得到图片保存目录的真实路径
		String realpathdir = request.getSession().getServletContext()
				.getRealPath(pathdir);
		try {
			this.downFile(response, realpathdir, fileNameSigned);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("文件下载出错", e);
		}
		return null;
	}

	/**
	 * 文件下载
	 * 
	 * @param response
	 * @param str
	 */
	private void downFile(HttpServletResponse response, String dirpath,
			String str) {
		try {
			String path = dirpath + File.separator + str;
			File file = new File(path);
			if (file.exists()) {
				InputStream ins = new FileInputStream(path);
				BufferedInputStream bins = new BufferedInputStream(ins);// 放到缓冲流里面
				OutputStream outs = response.getOutputStream();// 获取文件输出IO流
				BufferedOutputStream bouts = new BufferedOutputStream(outs);
				response.setContentType("application/x-download");// 设置response内容的类型
				response.setHeader(
						"Content-disposition",
						"attachment;filename="
								+ URLEncoder.encode(str, "UTF-8"));// 设置头部信息
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				// 开始向网络传输文件流
				while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
					bouts.write(buffer, 0, bytesRead);
				}
				bouts.flush();// 这里一定要调用flush()方法
				ins.close();
				bins.close();
				outs.close();
				bouts.close();
			} else {
				System.out.println("出错！");
			}
		} catch (IOException e) {
			Log.error("文件下载出错", e);
		}
	}

	/**
	 * 生成债权转让协议
	 * 
	 * @Title: 生成项目合同
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("borrow/downloadDept")
	public String downloadDept(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, Long btId, Long marketId)
			throws Exception {

		// @Description: TODO
		User user = this.getCurrUser(request, response);
		if (user == null) {
			map.put("msg", "请先登录");
			return "to_success";
		}
		BorrowTender bt = borrowTenderService.getUserBorrowTenderById(btId); // 投资成功的
		Borrow borrow = borrowService.getBorrowById(bt.getBorrowId());
		if (borrow == null || bt == null) {
			map.put("msg", "没有找到投资项目");
			return "to_success";
		}

		Market market = transferService.queryMarketById(marketId);
		User jiekuanUser = userService.queryByUserId(market.getTenderUserId());
		Integer userType = user.getUserType();
		if (StringUtil.isEmpty(String.valueOf(userType))) {
			map.put("msg", "请先进行实名认证");
			return "to_success";
		} else {
			// e签宝初始化信息
			EsignsdkService SDK = EsignsdkServiceFactory.instance();

			// e签宝初始化服务
			Log.info("e签宝开始执行初始化...");
			request.getSession().invalidate();

			Result result = SDK.init(ProjectConstants.PROJECTID,
					ProjectConstants.PROJECT_SECRET);
			if (0 != result.getErrCode()) {
				Log.debug("e签宝初始化失败，错误码=" + result.getErrCode() + ",错误信息="
						+ result.getMsg());
				// map.addAttribute(ERRORMSG, "e签宝初始化失败");
				putCurrUser(request, response, user);
				map.put("msg", "签章初始化失败");
				return "to_success";
			}

			Log.info("e签宝初始化成功，开始执行项目登录...");

			// 使用初始化设置的项目编号和项目验证码做项目账户登录
			LoginResult loginResult = SDK.login();
			if (0 != loginResult.getErrCode()) {
				Log.debug("e签宝验证处理失败！");
				// map.addAttribute(ERRORMSG, "e签宝验证处理失败！");
				putCurrUser(request, response, user);
				map.put("msg", "签章验证失败");
				return "to_success";
			}
			// 设置项目账户标识，后续接口调用时可以从session中获取
			request.getSession().setAttribute("devId",
					loginResult.getAccountId());
			Log.info("e签宝验证处理成功! accountId:" + loginResult.getAccountId());

			AccountService SERVICE = AccountServiceFactory.instance();
			String devId = (String) request.getSession().getAttribute("devId");
			AddAccountResult r = null;
			// 个人用户
			if (userType == Constant.PERSON) {
				PersonBean psn = new PersonBean();
				psn.setAddress("地址")
						.setEmail(user.getMobile() + "@timevla.com")
						.setMobile(user.getMobile())
						.setName(user.getRealname()).setIdNo(user.getIdCard())
						.setOrgan("我在这里工作").setTitle("这是我的职位");
				r = SERVICE.addAccount(devId, psn);
			} else if (userType == Constant.ENTERPRISE) {
				OrganizeBean org = new OrganizeBean();
				org.setAddress("地址")
						.setEmail(user.getMobile() + "@timevla.com")
						.setMobile(user.getMobile())
						.setName(user.getRealname())
						.setAgentIdNo(user.getIdCard()).setAgentName("这是代理人姓名")
						.setLegalArea(0).setLegalIdNo("110000197609260652")
						.setLegalName(user.getRealname())
						.setOrganCode(user.getOrganization()).setOrganType(1)
						.setRegCode(user.getOrganization())
						.setScope("这是我的经营范围").setUserType(2);
				r = SERVICE.addAccount(devId, org);
			}
			if (0 != r.getErrCode() || null == r.getAccountId()
					|| r.getAccountId().isEmpty()) {
				// Tools.forward(req, resp, "/error.jsp");
				putCurrUser(request, response, user);
				map.put("msg", "签章登录失败" + r.getMsg());
				return "to_success";
			}

			// 保存用户账户
			request.getSession().setAttribute("userId", r.getAccountId());
			user.setLoginAccountId(r.getAccountId());
			userService.userUpdate(user);
			putCurrUser(request, response, user);
		}

		Article article = articleService.queryArtById(230L);// 正式环境
		String artcontent = article.getContent();

		artcontent = artcontent.replace("xyNo",
				DateUtils.dateStr(bt.getAddtime(), "yyyyMMddHHmmss") + "-"
						+ borrow.getId() + "-" + bt.getId());// 协议编号

		artcontent = artcontent.replace("jiekuanUser",
				jiekuanUser.getRealname());// 甲方（借款人）
		
		if(jiekuanUser.getUserType()==1)
		{
			artcontent = artcontent.replace("companyType","身份证号码");// 乙方（债权受让人）
			artcontent = artcontent.replace("org", user.getIdCard());// 乙方（债权受让人）
		}
		else
		{
			artcontent = artcontent.replace("companyType","企业注册号");// 乙方（债权受让人）
			artcontent = artcontent.replace("org", user.getOrganization());// 乙方（债权受让人）
		}

		artcontent = artcontent.replace("shourenname", user.getRealname());// 乙方（债权受让人）
		if(userType==1)
		{
			artcontent = artcontent.replace("userType","身份证号码");// 乙方（债权受让人）
			artcontent = artcontent.replace("idCard", user.getIdCard());// 乙方（债权受让人）
		}
		else
		{
			artcontent = artcontent.replace("userType","企业注册号");// 乙方（债权受让人）
			artcontent = artcontent.replace("idCard", user.getOrganization());// 乙方（债权受让人）
		}

		artcontent = artcontent.replace("oldMoney",
				String.valueOf(bt.getAccount()));// （借款金额）

		artcontent = artcontent.replace("marketMoney",
				String.valueOf(bt.getRepaymentAccount()));// （借款金额）

		artcontent = artcontent.replace("deptMoney",
				String.valueOf(market.getTransferPrice()));// （转让金额）

		artcontent = artcontent.replace("day",
				String.valueOf(market.getRepayOrder()));// （期限）

		artcontent = artcontent.replace("limitType", "期");// （期限）

		artcontent = artcontent.replace("apr", String.valueOf(borrow.getApr()));// （年利率）
		artcontent = artcontent.replace("borrowname", borrow.getName());// （标的名称）

		// generate file name
		String pathdir = borrow.getId().toString();
		// 得到图片保存目录的真实路径
		String realpathdir = request.getSession().getServletContext()
				.getRealPath(pathdir);
		File savedir = new File(realpathdir);
		if (!savedir.exists())
			savedir.mkdirs();// 如果目录不存在就创建
		MD5 md5 = new MD5();
		String fileNameSuffix = md5.getMD5ofStr(String.valueOf(bt.getId()));

		String fileNameUnsigned = "market_" + fileNameSuffix + ".pdf";
		String fileNameSigned = "market_signed_" + fileNameSuffix + ".pdf";
		generatePDF(artcontent, savedir, fileNameUnsigned);

		String filePathUnsigned = savedir + File.separator + fileNameUnsigned;
		String filePathSigned = savedir + File.separator + fileNameSigned;

		String filePathUnsignedRelative = pathdir + File.separator
				+ fileNameUnsigned;
		String filePathSignedRelative = pathdir + File.separator
				+ fileNameSigned;

		String filePathSignedPersonTmp = savedir + File.separator
				+ "market_tmp_" + fileNameSuffix + ".pdf";// 个人
		String filePathSignedPersonTmpRelative = pathdir + File.separator
				+ "market_tmp_" + fileNameSuffix + ".pdf";// 个人

		String filePathSignedBorrowTmp1 = savedir + File.separator
				+ "market_tmp1_" + fileNameSuffix + ".pdf";// 借款人
		String filePathSignedBorrowTmpRelative1 = pathdir + File.separator
				+ "market_tmp1_" + fileNameSuffix + ".pdf";// 借款人

		// String filePathSignedGuaranteeTmp2 = savedir + File.separator
		// + "market_tmp2_" + fileNameSuffix + ".pdf";// 担保
		// String filePathSignedGuaranteeTmpRelative2 = pathdir + File.separator
		// + "market_tmp2_" + fileNameSuffix + ".pdf";// 担保

		File fSigned = new File(filePathSigned);
		if (!fSigned.exists()) {
			String accountType = (String) request.getSession().getAttribute(
					"accountType");
			if (userType == Constant.PERSON) {
				accountType = "person";// e签宝个人
			} else {
				accountType = "organize";// e签宝企业
			}
			request.getSession().setAttribute("accountType", accountType);

			request.getSession().setAttribute("userId",
					user.getLoginAccountId());

			ESignUtil esignUtil = new ESignUtil();
			String page = "2";
			boolean result = false;
			if (userType == Constant.PERSON) {
				result = esignUtil.SignPerson(request, response,
						filePathUnsignedRelative,
						filePathSignedPersonTmpRelative, page, 500, 500);
				if (!result) {
					map.put("msg", "签章借款人失败");
					return "to_success";
				}
			} else {
				result = esignUtil.SignOrganize(request, response,
						filePathUnsignedRelative,
						filePathSignedPersonTmpRelative, page, 500, 500);
				if (!result) {
					map.put("msg", "签章借款人失败");
					return "to_success";
				}
			}

			// String orgId = "34148054-0";
			// jiekuanUser.setRealname("杭州互联网金融协会");
			// jiekuanUser.setMobile("13713700102");
			// String idCard = "330381199005296413";
			// jiekuanUser.setIdCard(idCard);
			// jiekuanUser.setOrganization(orgId);
			// init organizer借款人企业
			AccountService SERVICE = AccountServiceFactory.instance();
			String devId = (String) request.getSession().getAttribute("devId");
			AddAccountResult r = null;

			if (jiekuanUser.getUserType() == Constant.PERSON) {
				PersonBean psn = new PersonBean();
				psn.setAddress("地址")
						.setEmail(jiekuanUser.getMobile() + "@timevla.com")
						.setMobile(jiekuanUser.getMobile())
						.setName(jiekuanUser.getRealname())
						.setIdNo(jiekuanUser.getIdCard()).setOrgan("我在这里工作")
						.setTitle("这是我的职位");

				r = SERVICE.addAccount(devId, psn);
				request.getSession().setAttribute("accountType", "person");
			} else {

				OrganizeBean org = new OrganizeBean();
				org.setAddress("地址")
						.setEmail(jiekuanUser.getMobile() + "@timevla.com")
						.setMobile(jiekuanUser.getMobile())
						.setName(jiekuanUser.getRealname())
						.setAgentIdNo(jiekuanUser.getIdCard())
						.setAgentName("这是代理人姓名").setLegalArea(0)
						.setLegalIdNo("110000197609260652")
						.setLegalName(jiekuanUser.getRealname())
						.setOrganCode(jiekuanUser.getOrganization())
						.setOrganType(1)
						.setRegCode(jiekuanUser.getOrganization())
						.setScope("这是我的经营范围").setUserType(2);
				r = SERVICE.addAccount(devId, org);
				request.getSession().setAttribute("accountType", "organize");
			}

			if (0 != r.getErrCode() || null == r.getAccountId()
					|| r.getAccountId().isEmpty()) {
				// Tools.forward(req, resp, "/error.jsp");
				map.put("msg", "签章借款人失败" + r.getMsg());
				return "to_success";
			}
			jiekuanUser.setLoginAccountId(r.getAccountId());
			jiekuanUser.setTechSignStatus(1);
			userService.userUpdate(jiekuanUser);

			request.getSession().setAttribute("userId", r.getAccountId());

			if (jiekuanUser.getUserType() == Constant.PERSON) {
				result = esignUtil.SignPerson(request, response,
						filePathSignedPersonTmpRelative,
						filePathSignedBorrowTmpRelative1, page, 80, 500);
				if (!result) {
					map.put("msg", "签章投资人失败");
					return "to_success";
				}
			} else {
				result = esignUtil.SignOrganize(request, response,
						filePathSignedPersonTmpRelative,
						filePathSignedBorrowTmpRelative1, page, 80, 500);
				if (!result) {
					map.put("msg", "签章投资人失败");
					return "to_success";
				}
			}
			// init SignGuaranteeCompany担保公司
			// AccountService GuaranteeSERVICE =
			// AccountServiceFactory.instance();
			// AddAccountResult gr = null;
			//
			// String guarantee_company =
			// Global.getValue("guarantee_company");//丙方担保公司（担保人）
			// String glegalRepresentative =
			// Global.getValue("glegalRepresentative");//丙方担保公司（法定代表人）
			// String guarantee_organization =
			// Global.getValue("guarantee_organization");//丙方担保公司（组织机构号）
			// String guarantee_moblie =
			// Global.getValue("guarantee_moblie");//丙方担保公司（手机号）
			// String guarantee_idCard =
			// Global.getValue("guarantee_idCard");//丙方担保公司（法人的身份证）
			// OrganizeBean orgg = new OrganizeBean();
			// orgg.setAddress("地址").setEmail(guarantee_moblie +
			// "@timevla.com").setMobile(guarantee_moblie)
			// .setName(guarantee_company).setAgentIdNo(guarantee_idCard).setAgentName(glegalRepresentative)
			// .setLegalArea(0).setLegalIdNo("110000197609260652")
			// .setLegalName(glegalRepresentative).setOrganCode(guarantee_organization)
			// .setOrganType(1).setRegCode(guarantee_organization).setScope("这是我的经营范围")
			// .setUserType(2);
			// gr = GuaranteeSERVICE.addAccount(devId, orgg);
			//
			// if (0 != gr.getErrCode() || null == gr.getAccountId() ||
			// gr.getAccountId().isEmpty()) {
			// // Tools.forward(req, resp, "/error.jsp");
			// map.put("msg", "签章担保公司失败！"+ gr.getMsg());
			// return "to_success";
			// }
			//
			// request.getSession().setAttribute("accountType", "organize");
			// request.getSession().setAttribute("userId", gr.getAccountId());
			//
			// result = esignUtil.SignGuaranteeCompany(request, response,
			// filePathSignedBorrowTmpRelative1,
			// filePathSignedGuaranteeTmpRelative2);
			// if(!result) {
			// map.put("msg", "签章担保公司失败");
			// return "to_success";
			// } else {
			// File tmpfile = new File(filePathSignedBorrowTmp1);
			// tmpfile.delete();
			// }

			// SignPlatform平台
			// result = esignUtil.SignPlatform(request, response,
			// filePathSignedGuaranteeTmpRelative2, filePathSignedRelative);
			// if(!result) {
			// map.put("msg", "签章平台失败");
			// return "to_success";
			// } else {
			// File tmpfile = new File(filePathSignedGuaranteeTmp2);
			// tmpfile.delete();
			// }
		}

		return "redirect:/borrow/download/marketProtocol.html?borrowTenderId="
				+ bt.getId() + "&borrowId=" + bt.getBorrowId();
	}

}