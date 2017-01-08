package com.dept.web.controller.weixin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.dept.web.controller.WebController;
import com.dept.web.dao.HongbaoDao;
import com.dept.web.dao.model.Bank;
import com.dept.web.dao.model.BankCard;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowCollection;
import com.dept.web.dao.model.BorrowRepayment;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.City;
import com.dept.web.dao.model.Hongbao;
import com.dept.web.dao.model.PlanAppendInsure;
import com.dept.web.dao.model.PlanRecord;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.dao.model.UserBorrowModel;
import com.dept.web.dao.model.UserWithdraw;
import com.dept.web.general.javamail.Mail;
import com.dept.web.general.util.BASE64Decoder;
import com.dept.web.general.util.BASE64Encoder;
import com.dept.web.general.util.DateUtils;
import com.dept.web.general.util.MD5;
import com.dept.web.general.util.MD5Util;
import com.dept.web.general.util.NumberUtil;
import com.dept.web.general.util.TextUtil;
import com.dept.web.general.util.TimeUtil;
import com.dept.web.general.util.UploadUtil;
import com.dept.web.general.util.beifu_Pay.DesUtil;
import com.dept.web.general.util.beifu_Pay.RSACoder;
import com.dept.web.general.util.beifu_Pay.SendMess;
import com.dept.web.general.util.tools.iphelper.IPUtils;
import com.dept.web.general.util.yeepay.RealMameYeePay;
import com.dept.web.service.BankCardService;
import com.dept.web.service.BankService;
import com.dept.web.service.BorrowService;
import com.dept.web.service.CityService;
import com.dept.web.service.PlanRecordService;
import com.dept.web.service.PlanService;
import com.dept.web.service.UserAccountLogService;
import com.dept.web.service.UserAccountService;
import com.dept.web.service.UserService;
import com.dept.web.service.UserWithdrawService;
import com.mypay.merchantutil.Md5Encrypt;
import com.mypay.merchantutil.UrlHelper;
import com.mypay.merchantutil.timestamp.TimestampUtils;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;
import com.sendinfo.xspring.ibatis.page.PageUtils;

/**
 * 用户中心
 * 
 * @ClassName: CenterController.java
 * @Description:
 * 
 * @author cannavaro
 * @version V1.0
 * @Date 2014-9-2 上午11:24:26 <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class WebAppCenterController extends WebController {
	@Autowired
	private UserService userService;
	@Autowired
	private UserAccountService accountService;
	@Autowired
	private BankCardService bankCardService;
	@Autowired
	private UserAccountLogService userAccountLogService;
	@Autowired
	private UserWithdrawService userWithdrawService;
	@Autowired
	private PlanRecordService planRecordService;
	@Autowired
	private BorrowService borrowService;
	@Autowired
	private BankService bankService;
	
	@Autowired
	private PlanService planService;
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private HongbaoDao hongbaoDao;
	
	/**
	 * 设置 一个成员变量用来保存要绑定的邮箱
	 */
	private String emailBind;
	
	
	/**
	 * 我的账户
	 * 
	 * @Description:
	 * @param: @param map
	 * @param: @param request
	 * @param: @param response
	 * @param: @param status
	 * @param: @return
	 * @param: @throws NumberFormatException
	 * @param: @throws Exception
	 * @return: String
	 * @throws
	 */
	@RequestMapping("webapp/myhome/user-index")
	public String userIndex(ModelMap map, HttpServletRequest request,HttpServletResponse response, HttpSession session) throws NumberFormatException, Exception {
					// 获取登录的用户信息
					User user = new User();
					user = this.getCurrUser(request, response);
					if(user==null){
						return "redirect:/webapp/tologin.html";
					}
					// 获取用户账户信息
					UserAccount ua = accountService.getUserAccount(user.getId());
					ua.setTtdj(ua.getMoneyTenderFreeze()+ua.getMoneyWithdraw());
					System.out.println(ua.getTtdj());
					
					//获取我的收益，代收收益，代收金额。
					double myinterest = 	borrowService.getInterestByBorrowTenderForUserId(user.getId());
					double collotionInterest =borrowService.getCount(user.getId());
					double collotionMoney = borrowService.getDSmoney(user.getId());
					map.addAttribute("myinterest", myinterest);
					map.addAttribute("collotionInterest", collotionInterest);
					map.addAttribute("collotionMoney", collotionMoney);
					map.addAttribute("ua", ua);
					
		            //获取推荐理财
					Map<String,Object> filterMap = new HashMap<String, Object>();
			    	filterMap.put("status", 1);
		            List<Borrow> borrowList = borrowService.getBorrowList(1, 3,filterMap);
		            if(borrowList != null && borrowList.size() > 0) {
						map.addAttribute("borrowList", borrowList);
					}
					// 把登录或注册成功的user对象放到session中
					session.setAttribute("userInSession", user);
					map.addAttribute("msg",request.getParameter("msg"));
					
					//赚取的钱(已收的利息)
					double zqmoney=borrowService.getCount(user.getId());
					map.addAttribute("zqmoney", zqmoney);
					
					//获取红包详情
		            Hongbao hb=hongbaoDao.queryHongbaoForAccount(user.getId());
		            map.addAttribute("hb", hb);
					
		return "qgfwebapp/myAcount";
	}
	
	
	
	
	/**
	 * 用户中心实名认证接口
	 * 
	 * @Description: TODO
	 * @param: @param map
	 * @param: @param request
	 * @param: @param response
	 * @param: @param nid
	 * @param: @return
	 * @param: @throws Exception
	 * @return: String
	 * @throws
	 */
	@RequestMapping("webapp/myhome/toide")
	public String toide(ModelMap map, HttpServletRequest request,HttpServletResponse response, HttpSession session) throws MalformedURLException {
		try {
			User user = this.getCurrUser(request, response);
			if (user != null) {
				 Map<String,String> params = getParamMap(request); 
			     String idCardNumber = params.get("idcard"); //身份证号码
		         String name = params.get("realname"); //姓名和身份证号码对应
		         String bankId = params.get("bank"); //银行卡名称
			     String bankCardNumber = params.get("bankCardNumber");//银行卡卡号
			     String bankCardNumberTO = params.get("bankCardNumberTO");//银行卡卡号
			     String pattern ="1";//认证模式为“1”表示只认证信息真实性
			     String busId =String.valueOf(user.getId());//由商户上传的认证订单号
			     String province="";
			     String city="";
			     String res_desc="";
			     map.addAttribute("ctype", "name");
		          
		           if(StringUtil.isEmpty(idCardNumber)){
		           	map.addAttribute("msg", "请输入身份证号码!");
		           	return "redirect:/webapp/myhome/aqzx.html";
		           }
		           // 身份证正则表达式
					String regEx = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";// （要么是15位，要么是18位，最后一位可以为字母）
					Pattern p = Pattern.compile(regEx);
					if (!p.matcher(idCardNumber).matches()) {// 不匹配则返回到认证页
						map.addAttribute("msg", "身份证号有误");
						return "redirect:/webapp/myhome/aqzx.html";
					}
					
		           if(StringUtil.isEmpty(name)){
		           	map.addAttribute("msg", "请输入姓名!");
		           	return "redirect:/webapp/myhome/aqzx.html";
		           }
		           if(StringUtil.isEmpty(bankCardNumber)){
		           	map.addAttribute("msg", "请输入银行卡号码!");
		           	return "redirect:/webapp/myhome/aqzx.html";
		           }
		           if(StringUtil.isEmpty(bankId)){
		           	map.addAttribute("msg", "请选择银行!");
		           	return "redirect:/webapp/myhome/aqzx.html";
		           }
		           if(!bankCardNumber.equals(bankCardNumberTO)){
		           	map.addAttribute("msg", "两次输入的银行卡号不一致!");
		           	return "redirect:/webapp/myhome/aqzx.html";
		           }
		           user = userService.queryByUserId(user.getId());
		           if(user!=null && user.getRealVerifyStatus()!=null && user.getRealVerifyStatus()==1){
		        		map.addAttribute("msg", "您已经实名认证过，请勿重复认证!");
		        		return "redirect:/webapp/myhome/aqzx.html";
		           }
		           
		           String bankNames="";
		           String bankCode = "";//参考银行编码列表
		           if(!StringUtil.isEmpty(bankId)){
		           	Bank bank = bankService.queryAllBank(Long.valueOf(bankId));
		           	if(bank!=null){
		           		bankCode =bank.getAbbreviation();
			           	bankNames = bank.getBankName();
			           	params.put("bankCode", bankCode);
			        	params.put("bankName", bankNames);
		           	}
		           
		           }
		           
		            params.put("pattern", pattern);
		            Random random = new Random(System.currentTimeMillis());
					int iRandom = random.nextInt(10000) + 10000000;
					String out_trade_no = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + String.valueOf(iRandom)+busId;
					params.put("busId", out_trade_no);
					params.put("province", province);
					params.put("city", city);
					params.put("res_desc", res_desc);
		           /**
		            * 产生hamc需要两个参数，并调用相关的API参数
		            * 1:列表中的参数值按照签名顺序拼接所产生的字符串，注意null要转换为 ””，并确保无乱码.
		            * 参数2:密钥（易宝支付为每一个客户分配一个密钥）
		            */
		         Map result =  RealMameYeePay.realNameVerify(params);
		         if(result!=null){
		        	String responseCode = (String) result.get("responseCode");
		        	String authStatus = (String) result.get("authStatus");
		        	String authMsg = (String) result.get("authMsg");
		        	String flowId = (String) result.get("flowId");
		        	 if(!StringUtil.isEmpty(responseCode) && responseCode.equals("00000")){
		   	        	 if(authStatus.equals("SUCCESS")){
		   	        	//更新用户信息
		   	        	     user.setRealname(URLDecoder.decode(name,"UTF-8"));
		   	        	     user.setIdCard(idCardNumber);
		                     user.setCardBindingStatus(1);
		                     user.setRealVerifyStatus(1);
		                     //  更新用户信息
		                     userService.userUpdate(user);
		                     
		                     
		                     //更新银行卡信息
		                     List<BankCard> bankList = bankCardService.getUserBankCard(user.getId());
		                     if(bankList==null ||  bankList.size()==0){
		                    	 BankCard bank = new BankCard();
		                    	 bank.setCardNo(bankCardNumber); 
		                    	 bank.setCardId(flowId); 
		                    	 bank.setBankName(bankNames);
		                    	 bank.setBankDetail(bankCode);
		                    	 bank.setBankId(Long.valueOf(bankId));
		                    	 bank.setUserId(user.getId());
		                    	 bank.setStatus(1);
		                    	 bank.setCreatedAt(Long.valueOf(TimeUtil.getNowTimeStr()));
		                    	 bankCardService.addBankCard(bank);
		                     }
		                     //增加红包。注册成功，实名成功送红包。18元
		                     accountService.insertHongbao(user.getId());                     
		                     
		                     
		                     map.addAttribute("msg", "实名认证成功!");
		   	        	 } else if(authStatus.equals("NOT_AUTH")){
		   	        		map.addAttribute("msg", "实名认证未认证!");
		   	        	 }else{
		   	        		map.addAttribute("msg", "实名认证失败!");
		   	        	 }
		   	        	 
		   	          }else{
		   	        	  map.addAttribute("msg", authMsg);
		   	          }
		         }
		         return "redirect:/webapp/myhome/aqzx.html";
			}else{
				return "redirect:/webapp/tologin.html";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.addAttribute("msg", "系统异常，请重试");
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return "redirect:/myhome/aqzx.html";
		}
	}

	
	
	private String md5(String s){
		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	
	private String url2string(String url){
		StringBuffer sb=new StringBuffer();
		try {
			InputStream is=new URL(url).openStream();
			byte[] buf=new byte[1024*10];
			int len=0;
			while((len=is.read(buf, 0, 1024*10))>0){
				sb.append(new String(buf,0,len));
			}
			is.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 实名认证
	 */
	@RequestMapping(value = "webapp/myhome/realname")
	public String realName(HttpServletRequest request,HttpServletResponse response, HttpSession session, ModelMap map) {
		User user = getCurrUser(request, response);
		//查询当前用户绑定的银行卡。
		List<BankCard> bankList=bankCardService.getUserBankCard(user.getId());
		 map.addAttribute("bankList", bankList);
		map.addAttribute("user", user);
		map.addAttribute("msg", request.getParameter("msg"));
		//查询所有易宝可用的银行
		List<Bank> banks= bankService.queryAllBank();
		map.addAttribute("banks", banks);
		return "qgfwebapp/reallyName";
	}
	
	
	/**
	 * 银行卡查询
	 */
	@RequestMapping(value = "webapp/myhome/bannk")
	public String bank(HttpServletRequest request,HttpServletResponse response, HttpSession session, ModelMap map) {
		User user = getCurrUser(request, response);
		//查询当前用户绑定的银行卡。
		List<BankCard> bankList=bankCardService.getUserBankCard(user.getId());
		 map.addAttribute("bankList", bankList);
		map.addAttribute("user", user);
		map.addAttribute("msg", request.getParameter("msg"));
		//查询所有易宝可用的银行
		List<Bank> banks= bankService.queryAllBank();
		map.addAttribute("banks", banks);
		return "qgfwebapp/myBank";
	}
	
	
	
	/**
	 * 绑定银行卡
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "webapp/myhome/bankBind")
	public String bankBind(HttpServletRequest request,HttpServletResponse response, HttpSession session, ModelMap map) throws SQLException {
		String bankId = request.getParameter("bankName");
		String bankDetail = request.getParameter("bankDetail");
		String cardNo = request.getParameter("cardNo");
		String recardNo = request.getParameter("recardNo");
		String province = request.getParameter("province");
		String city = request.getParameter("city");
				
		User user = getCurrUser(request, response);
		if(user!=null){
		map.put("user", user);
		//查询所有的银行卡
		List<Bank> bankList = bankService.queryAllBank();
		map.put("bankList", bankList);

		//查询省份
		List<City> CityList = cityService.queryCityListByParent(0L); 
		map.put("provinceList", CityList);
		
		String flag = request.getParameter("flag");
		String msg = "";
		if (flag == null) {// 查询已经绑定的银行卡(跳转到绑定银行卡页面)
			List<BankCard> list = bankCardService.selectAllBankCardByUser(user.getId());
			if(!list.isEmpty()){
//				list.get(0).setCardNo(TextUtil.hideUsernameChar(list.get(0).getCardNo()));
				map.put("bankCard", list.get(0));
			}
			session.setAttribute("bankCardList", list);
		}else if(flag.equals("add")) {// 添加绑定 银行卡
				if ((bankId == null || "".equals(bankId.trim()))
						|| (bankDetail == null || "".equals(bankDetail.trim()))
						|| (cardNo == null || "".equals(cardNo.trim()))
						|| (recardNo == null || "".equals(recardNo.trim()))
						|| (province == null || "".equals(province.trim()))
						|| (city == null || "".equals(city.trim()))) {
					msg = "信息填写不完整";
				} else {
					// 输入的两次卡号不一致
					if (!cardNo.equals(recardNo)) {
						msg = "输入的两次卡号不一致";
						map.put("msg", msg);
						return "qgfwebapp/myBank";
					}

					// 银行卡号正则验证：16或19位数字
					Pattern pattern = Pattern.compile("^(\\d{16}|\\d{17}|\\d{18}|\\d{19})$");
					Matcher matcher = pattern.matcher(cardNo);
					if (!matcher.matches()) {
						msg = "请输入正确的银行卡号";
						map.put("msg", msg);
						return "qgfwebapp/myBank";
					}

					// 查询该银行卡是否已经被别人绑定
					if (bankCardService.getBankCardByCardNo(cardNo) != null) {
						msg = "该银行卡已经被绑定,请核对";
						map.put("msg", msg);
						return "qgfwebapp/myBank";
					}
					
					// 查询该用户是否已经绑定银行卡
					if (bankCardService.getUserBankCard(user.getId()) != null) {
						msg = "您已绑定过一张银行卡";
						map.put("msg", msg);
						return "qgfwebapp/myBank";
					}
					
					if(bankList.get(0).getBankName()!=null){
						Bank b =bankService.queryAllBank(Long.valueOf(bankId));
						map.put("b", b);
						
						BankCard bankCard = new BankCard();
						bankCard.setBankId(b.getId());
						bankCard.setBankName(b.getBankName());
						bankCard.setBankDetail(bankDetail);
						bankCard.setCardNo(cardNo);
						bankCard.setStatus(0);
						bankCard.setProvince(province);
						bankCard.setCity(city);
						bankCard.setUserId(user.getId());
						bankCard.setCreatedAt(System.currentTimeMillis() / 1000);
						
						bankCardService.addBankCardInTransaction(user,bankCard);
					}
					user.setCardBindingStatus(1);
					// 更新session里的user
					session.setAttribute("userInSession", user);
					// 返回该用户绑定的所有银行卡列表
					List<BankCard> list = bankCardService.selectAllBankCardByUser(user.getId());
					if(!list.isEmpty()){
						list.get(0).setCardNo(TextUtil.hideUsernameChar(list.get(0).getCardNo()));
						map.put("bankCard", list.get(0));
					}
					
					msg = "添加成功";
					map.put("msg", msg);
				}
			}else if(flag.equals("upd")){
				if ((bankId == null || "".equals(bankId.trim()))
						|| (bankDetail == null || "".equals(bankDetail.trim()))
						|| (cardNo == null || "".equals(cardNo.trim()))
						|| (recardNo == null || "".equals(recardNo.trim()))
						|| (province == null || "".equals(province.trim()))
						|| (city == null || "".equals(city.trim()))
						) {
					msg = "信息填写不完整";
				} else {
					// 输入的两次卡号不一致
					if (!cardNo.equals(recardNo)) {
						msg = "输入的两次卡号不一致";
						map.put("msg", msg);
						return "qgfwebapp/myBank";
					}

					// 银行卡号正则验证：16或19位数字
					Pattern pattern = Pattern.compile("^(\\d{16}|\\d{17}|\\d{18}|\\d{19})$");
					Matcher matcher = pattern.matcher(cardNo);
					if (!matcher.matches()) {
						msg = "请输入正确的银行卡号";
						map.put("msg", msg);
						return "qgfwebapp/myBank";
					}

					// 查询该银行卡是否已经被别人绑定
					if (bankCardService.getBankCardByCardNo(cardNo) != null) {
						msg = "该银行卡已经被绑定,请核对";
						map.put("msg", msg);
						return "qgfwebapp/myBank";
					}
					
					if(bankList.get(0).getBankName()!=null){
						Bank b =bankService.queryAllBank(Long.valueOf(bankId));
						map.put("b", b);
						BankCard bankCard = new BankCard();
						bankCard.setBankId(b.getId());
						bankCard.setBankName(b.getBankName());
						bankCard.setBankDetail(bankDetail);
						bankCard.setCardNo(cardNo);
						bankCard.setStatus(0);
						bankCard.setProvince(province);
						bankCard.setCity(city);
						bankCard.setUserId(user.getId());
						bankCard.setUpdatedAt(System.currentTimeMillis() / 1000);
						bankCardService.updateBankCardByUserId(bankCard);
					}

					// 返回该用户绑定的所有银行卡列表
					List<BankCard> list = bankCardService.selectAllBankCardByUser(user.getId());
					if(!list.isEmpty()){
						list.get(0).setCardNo(TextUtil.hideUsernameChar(list.get(0).getCardNo()));
						map.put("bankCard", list.get(0));
					}
					
					msg = "修改成功";
					map.put("msg", msg);
				}
			}else if(flag.equals("del")){
				// 查询该用户是否已经绑定银行卡
				if (bankCardService.getUserBankCard(user.getId()) != null) {
					bankCardService.delCardByUserIdInTransaction(user.getId(), session, user);
				}
			}else if(flag.equals("toUpd")){
				// 返回该用户绑定的所有银行卡列表
				List<BankCard> list = bankCardService.selectAllBankCardByUser(user.getId());
				if(!list.isEmpty()){
					list.get(0).setCardNo(TextUtil.hideUsernameChar(list.get(0).getCardNo()));
					map.put("bankCard", list.get(0));
					map.put("flag", flag);
				}
			}
		
			List<BankCard> list = bankCardService.selectAllBankCardByUser(user.getId());
			if(!list.isEmpty()){
	//			list.get(0).setCardNo(TextUtil.hideUsernameChar(list.get(0).getCardNo()));
				map.put("bankCard", list.get(0));
			}
			session.setAttribute("bankCardList", list);
			return "qgfwebapp/myBank";
		}else{
			return "redirect:/webapp/tologin.html";
		}
	}
		
				


	
	
	
	/**
	 * 正在投标的记录，但是没有满标审核
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping("webapp/myhome/tblist")
	public String tblist(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			long user_id=user.getId();
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			String numberstr =(String) request.getParameter("topNext");
			pageRequest.setPageSize(10);
			Map<String, String> params = getParamMap(request);
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			params.put("userId",String.valueOf(user_id));
			pageRequest.setFilters(params);
			Page<BorrowTender> borrowpage=borrowService.getInvestTenderingListByUserid1(pageRequest);
			map.addAttribute("page",pageRequest.getPageNumber());
			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
			 //正在投标的项目
			map.addAttribute("mt", 31);
		}
		return "webapp/tzmx";
	}

	/**
	 * 查看用户的借款情况
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping("webapp/myhome/jieBorrow")
	public String jieBorrow(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			long user_id=user.getId();
			String type = request.getParameter("type");
			if(StringUtils.isBlank(type)){
				type="repayment";
			}
			// 查询条件
			String dotime1 =request.getParameter("dotime1");
			String dotime2 = request.getParameter("dotime2");
			String keywords = request.getParameter("keywords");
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);
			Map<String, String> params = getParamMap(request);
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			params.put("keywords",keywords);
			params.put("dotime1",dotime1);
			params.put("dotime2",dotime2);
			params.put("type", type);
			params.put("userId",String.valueOf(user_id));
			pageRequest.setFilters(params);
			Page<UserBorrowModel> borrowpage = borrowService.getUserCenterBorrowList(pageRequest);
			map.addAttribute("page",pageRequest.getPageNumber());
			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
			if(type.equals("repayment")){
				map.addAttribute("mt",43);   //正在还款的项目
			}else if(type.equals("repaymentyes")){
				map.addAttribute("mt",44);   //已还款的项目
			}
			map.addAttribute("type",type);
		}
		map.addAttribute("errmsg", request.getParameter("errmsg"));
		return "webapp/jkmx";
	}
	
	/**
	 * 查看用户还款明细帐
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping("webapp/myhome/repinfo")
	public String repaymentdetail(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			long user_id=user.getId();
			long borrowId=NumberUtil.getLong(request.getParameter("borrowId"));
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			String numberstr =(String) request.getParameter("topNext");
			pageRequest.setPageSize(10);
			Map<String, String> params = getParamMap(request);
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			Page<BorrowRepayment> borrowpage;
			params.put("userId",String.valueOf(user_id));
			if(borrowId>0){
				params.put("borrowId",String.valueOf(borrowId));
				pageRequest.setFilters(params);
				borrowpage = borrowService.getUserCenterRepaymentdetailByborrowId(pageRequest);
			}else{
				pageRequest.setFilters(params);
				borrowpage = borrowService.getUserCenterRepaymentdetail(pageRequest);
			}
			map.addAttribute("page",pageRequest.getPageNumber());
			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
			Date date = Calendar.getInstance().getTime();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        map.addAttribute("currentTime",sdf.format(date)); 
	        map.addAttribute("today",date.getTime());
	        map.addAttribute("mt", 45); //还款明细帐
		}
		
		map.addAttribute("errmsg", request.getParameter("errmsg"));
		
		return "myhome/repinfo";
	}
	/**
	 * 跳转到安全中心
	 * 
	 * @return
	 */
	@RequestMapping(value = "webapp/myhome/aqzx", method = RequestMethod.GET)
	public String aqzx(HttpServletRequest request,HttpServletResponse response, HttpSession session, ModelMap map) {
		String ctype = request.getParameter("ctype");
		User user = getCurrUser(request, response);
		List<BankCard> bankList=bankCardService.getUserBankCard(user.getId());
		 map.addAttribute("bankList", bankList);
		map.addAttribute("user", user);
		map.addAttribute("ctype", ctype);
		map.addAttribute("msg", request.getParameter("msg"));
		return "qgfwebapp/safeCenter";
	}
	/**
	 * 邮箱认证
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("webapp/myhome/email")
	public String email(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws MalformedURLException{
		User user = getCurrUser(request, response);
		map.addAttribute("user", user);
		map.addAttribute("msg",request.getParameter("msg"));
		return "myhome/user-youxiang";
	}

	/**
	 * 跳转到修改交易密码页
	 * 
	 * @return
	 */
	@RequestMapping(value = "webapp/myhome/toUpdatePaySecurityPage")
	public String toUpdatePaySecurityPage() {
		return "myhome/update_paysecurity";
	}
	/**
	 * 修改密码页面
	 * @param map
	 * @param request
	 * @param response
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("webapp/myhome/xiugai")
	public String xiugai(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		User user = getCurrUser(request, response);
		if(user==null){
			return "redirect:/webapp/tologin.html";
		}
		map.addAttribute("msg", request.getParameter("msg"));
		return "qgfwebapp/changrePassword";
	}
	
	/**
	 * 修改交易密码页面
	 * @param map
	 * @param request
	 * @param response
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("webapp/myhome/xiugaijy")
	public String xiugaijy(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		User user = getCurrUser(request, response);
		if(user==null){
			return "redirect:/webapp/tologin.html";
		}
		map.addAttribute("msg", request.getParameter("msg"));
		return "webapp/change-jypwd";
	}
	
	
	/**
	 * 用户中心检查密码
	 * @Description:  TODO
	 * @param:        @param oldpassword
	 * @param:        @param newpassword
	 * @param:        @param user
	 * @param:        @return   
	 * @return:       String   
	 * @throws
	 */
	public String checkUserpwd(String oldpassword, String newpassword, String renewpassword, User user, int type,HttpServletRequest request) {	
		User vailduser = new User();
		
		vailduser = userService.queryByUserId(user.getId());
		MD5 md5 = new MD5();
		
		HttpSession session = request.getSession();
		String seesionPhone = (String) session.getAttribute("vcrand");
		if(type==1){
			
			if(!vailduser.getPasswordHash().toLowerCase().equals(md5.getMD5ofStr(oldpassword).toLowerCase())){
				
				return "原密码不正确，请输入您的旧密码 ";
				
			}else if (newpassword.length() < 6 || newpassword.length() > 16) {
				
				return "新密码长度要在6到16之间";
				
			}else if (!newpassword.equals(renewpassword)){
				
				return "两次密码不一致";
				
			}else if (renewpassword.length() < 6 || renewpassword.length() > 16){
				
				return "确认密码长度要在6到16之间";
			}
			
		}else{
			
			if(!vailduser.getPasswordPayHash().equals(md5.getMD5ofStr(oldpassword).toLowerCase())){
				
				return "密码不正确，请输入您的旧密码 ";
				
			}else if (newpassword.length() < 6 || newpassword.length() > 15) {
				
				return "新密码长度在6到15之间";
				
			}
		}
		return "";
	}
	/**
	 * 用户中心修改密码
	 * @Description:  TODO
	 * @param:        @param map
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param nid
	 * @param:        @return
	 * @param:        @throws Exception   
	 * @return:       String   
	 * @throws
	 */
	@RequestMapping("webapp/myhome/security")
	@Transactional(rollbackFor={Exception.class})
	public String security(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam int type) throws Exception{
		User user = getCurrUser(request, response);
		if(user==null){
			return "redirect:/webapp/tologin.html";
		}
		String oldpassword = request.getParameter("oldpassword");
		String newpassword = request.getParameter("password");//新密码
		String renewpassword = request.getParameter("repassword");//确认新密码
		
		String msg = "";
		
		if(!StringUtils.isEmpty(oldpassword)){
			
			MD5 md5 = new MD5();
			
			msg = checkUserpwd(oldpassword, newpassword, renewpassword, user, type,request);
			if(!StringUtil.isEmpty(msg)){
				map.addAttribute("msg", msg);
				return "redirect:/webapp/myhome/xiugai.html";
			}
			if(oldpassword.equals(newpassword)){
				map.addAttribute("msg", "新旧密码不能相同");
				return "redirect:/webapp/myhome/xiugai.html";
			}
			if(!newpassword.equals(renewpassword)){
				map.addAttribute("msg", "两次输入密码不一致");
				return "redirect:/webapp/myhome/xiugai.html";
			}
			int count = userService.updatePassword(user.getId(), md5.getMD5ofStr(newpassword).toLowerCase(), type);
			if(count>0){
				map.addAttribute("msg", "修改成功");
				return "redirect:/webapp/myhome/xiugai.html";
			}
		
		}
		return "redirect:/webapp/myhome/xiugai.html";
	}
	
	/**
	 * 用户中心修改交易密码
	 * @Description:  TODO
	 * @param:        @param map
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param nid
	 * @param:        @return
	 * @param:        @throws Exception   
	 * @return:       String   
	 * @throws
	 */
	@RequestMapping("webapp/myhome/securityjy")
	@Transactional(rollbackFor={Exception.class})
	public String securityjy(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam int type) throws Exception{
		User user = getCurrUser(request, response);
		if(user==null){
			return "redirect:/webapp/tologin.html";
		}
		String newpassword = request.getParameter("password");//新密码
		String renewpassword = request.getParameter("repassword");//确认新密码
		
		String msg = "";
			
		MD5 md5 = new MD5();
		if(!newpassword.equals(renewpassword)){
			map.addAttribute("msg", "两次输入密码不一致");
			return "webapp/paypassword";
		}
			
		int count = userService.updatePassword(user.getId(), md5.getMD5ofStr(newpassword).toLowerCase(), type);
		if(count>0){
			map.addAttribute("msg", "设置成功");
		}		
		return "webapp/paypassword";
	}
	
	



	/**
	 * 修改交易密码
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "webapp/myhome/updatePayPassword", method = RequestMethod.POST)
	@Transactional(rollbackFor = { Exception.class })
	public String updatePayPassword(
			HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session,
			ModelMap map,
			@RequestParam(value = "oldpaypassword", required = false) String oldpaypassword,
			@RequestParam(value = "paypassword", required = false) String paypassword,
			@RequestParam(value = "repaypassword", required = false) String repaypassword) {

		User user = getCurrUser(request, response);
		if(user==null){
			return "redirect:/webapp/tologin.html";
		}
		MD5 md5 = new MD5();

		//String sessionVcrand = (String) session.getAttribute("vcrand");

		String msg = "";
		if (!"".equals(oldpaypassword) 
				&& !"".equals(paypassword) && !"".equals(repaypassword)) {
			if (!user.getPasswordPayHash().toLowerCase()
					.equals(md5.getMD5ofStr(oldpaypassword).toLowerCase())) {
				msg = "旧交易密码不正确，请重新输入您的旧交易密码 !";
			} else if (user.getPasswordPayHash().toLowerCase()
					.equals(md5.getMD5ofStr(paypassword).toLowerCase())) {
				msg = "新旧交易密码相同，请重新输入新交易密码!";
			} else if (!paypassword.equals(repaypassword)) {
				msg = "输入的两次新交易密码不同，请重新输入!";
			} else if (paypassword.length() < 6 || paypassword.length() > 15) {
				msg = "新交易密码长度在6到15之间";
			} else if (user.getPasswordHash().toLowerCase()
					.equals(md5.getMD5ofStr(paypassword).toLowerCase())) {
				msg = "交易密码不能和登录密码一致，请重新输入!";
			} else {// 验证都通过，可以修改
				try {
					user.setPasswordPayHash(md5.getMD5ofStr(paypassword));
					int count = userService.userUpdate(user);
					if (count > 0) {
						msg = "修改成功!";
					} else {
						msg = "修改失败!";
					}
				} catch (Exception e) {
					map.put("msg", msg);
					e.printStackTrace();
					TransactionAspectSupport.currentTransactionStatus()
							.setRollbackOnly();
				}
			}
		} else {
			msg = "信息填写不完整，请检查!";
		}
		map.put("msg", msg);
		return "redirect:/webapp/myhome/xiugaijy.html";
	}

	/**
	 * 发送邮箱验证信息
	 */
	@RequestMapping("webapp/myhome/sendEmail")
	public String sendEmail(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@RequestParam(value = "email", required = false) String email)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		User user = getCurrUser(request, response);
		String msg = "";
		if (StringUtil.isEmpty(email)) {
			msg = "email 不存在";// email 不存在
		}
		// 添加邮箱正则表达式验证
		Pattern pattern = Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

		Matcher matcher = pattern.matcher(email);

		if (matcher.matches()) {
			if (user != null) {
				// 查询邮箱是否已经被绑定过
				int count = userService.getEmailForUser(email);
				if (count == 0) {
					// 把email赋值给成员变量
					emailBind = email;
					// 发送邮箱
					user.setEmail(email);
					sendMSG(user);
					String flag = "邮件已发送成功,请登录邮箱确认!";
					msg = "邮件已发送成功,请登录邮箱确认!";
					map.put("flag", flag);
				} else {
					msg = "邮箱已存在";
				}
			} else {
				msg = "用户不存在";
			}
		} else {
			msg = "邮箱格式不正确";
		}

		map.put("msg", msg);
		map.addAttribute("msg", msg);
		return "redirect:/myhome/email.html";
	}

	public void sendMSG(User user) throws Exception {
		String to = user.getEmail();
		Mail m = Mail.getInstance();
		m.setTo(to);
		m.readActiveMailMsg();
		String signStr = new MD5().getMD5ofStr(user.getId()
				+ user.getUsername() + "2");
		m.replace(user.getUsername(), to,"webapp/call_backEmail.html?id=" + user.getId() + "&type=" + 2 + "&sign=" + signStr);
		m.sendMail();
	}

	/**
	 * 
	 * @Description: 验证邮箱返回
	 * @param: @param map
	 * @param: @param request
	 * @param: @param response
	 * @param: @return
	 * @param: @throws NumberFormatException
	 * @param: @throws Exception
	 * 
	 * @return: String
	 * @throws
	 */
	@RequestMapping("webapp/call_backEmail")
	public String call_backEmail(ModelMap map, HttpServletRequest request,
			HttpServletResponse response, SessionStatus status,
			HttpSession session) throws NumberFormatException, Exception {
		Map<String, String> params = getParamMap(request);
		String id = params.get("id");
		String type = params.get("type");
		String sign = params.get("sign");
		User user = userService.queryByUserId(Long.parseLong(id));
		String signStr = new MD5().getMD5ofStr(user.getId() + user.getUsername() + type);
		if (!sign.equals(signStr)) {
			map.addAttribute("msg", "邮箱发送连接错误，请重新发送");
			return "redirect:user-youxiang.html?ctype=email";
		}
		// 更新user对象
		user.setEmail(emailBind);
		user.setEmailBindingStatus(1);
		userService.userUpdate(user);
		//头像
		if (user.getLitpic() != null && !user.getLitpic().equals("")) {
			user.setBigPic(user.getLitpic().substring(0,user.getLitpic().lastIndexOf(".")) + "_120x120.jpg");
			user.setOnPic(user.getLitpic().substring(0,user.getLitpic().lastIndexOf(".")) + "_100x100.jpg");
			user.setSmallPic(user.getLitpic().substring(0,user.getLitpic().lastIndexOf(".")) + "_80x80.jpg");
		} else {
			user.setOnPic(user.getLitpic());
		}
		// 更新session中的user
		session.setAttribute("userInSession", user);
		return "redirect:myhome/user-youxiang.html";
	}

	/**
	 * 验证忘记密码页面提交过来的参数  第二步
	 * 
	 * @return
	 */
	@RequestMapping("webapp/checkforgetpasswordparam")
	public String forgetPasword(HttpServletRequest request,HttpServletResponse response, ModelMap map) {
		String msg = "";
		
		// 获取提交的参数
		String mobile = request.getParameter("mobile");
		String password = request.getParameter("password");  
		String repassword = request.getParameter("repassword");
		// 校验
		if (password == null || repassword == null){
			msg = "信息填写不完整，请确认！";
		} else if (userService.queryUserByMobile(mobile) == null) {// 绑定该手机号的账户的不存在
			msg = "绑定该手机号的帐号为空，请核对!";
		} else {
			if (!password.equals(repassword)) {
				msg = "两次密码不一致!";
			}else {// 验证都通过，可以修改
				try {
					User user = userService.queryUserByMobile(mobile);
					MD5 md5 = new MD5();
						user.setPasswordHash(md5.getMD5ofStr(password));
						int count = userService.userUpdate(user);
						if (count > 0) {
							msg = "找回密码成功!";
						} else {
							msg = "找回密码失败!";
						}
						
						map.put("msg", msg);
						return "qgfwebapp/modifyNewPassWord";
						
//					}else{
//						user.setPasswordPayHash(md5.getMD5ofStr(password));
//						int count = userService.userUpdate(user);
//						if (count > 0) {
//							msg = "找回密码成功!";
//						} else {
//							msg = "找回密码失败!";
//						}
//					}
					
					
				} catch (Exception e) {
					map.put("msg", "系统出错，找回密码失败");
					e.printStackTrace();
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return "qgfwebapp/modifyNewPassWord";
				}

			}
		}
		map.put("msg", msg);
//		if(type.equals("2")){
//			return "webapp/forgetPaypassword";
//		}else{
			return "qgfwebapp/modifyNewPassWord";
	}
	
	@RequestMapping("webapp/forgetpassword")
	public String forget(ModelMap map, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		
		return "qgfwebapp/modifyPassWord";
	}
	

	@RequestMapping("webapp/forgetpasswordOne")
	public String forgetpasswordOne(ModelMap map, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
			String msg = "";
			// 获取提交的参数
			String mobile = request.getParameter("telphone");
			//String password = request.getParameter("password");  
			//String repassword = request.getParameter("repassword");
			String verify = request.getParameter("imgCode");// 提交过来的验证码
			String code = request.getParameter("code");// 提交过来的手机短信验证码
			// 从session里获取验证码// 从sesion里获取手机验证码
			String sessionVcrand = (String) session.getAttribute("vcrand");
			String seesionPhone = (String) session.getAttribute("moblienum");
			if(StringUtil.isEmpty(mobile)){
				msg = "请输入手机号码";
			}
			// 校验
			 if (userService.queryUserByMobile(mobile) == null) {// 绑定该手机号的账户的不存在
				msg = "绑定该手机号的帐号为空，请核对!";
				return "qgfwebapp/modifyPassWord";
			} else {
				if(StringUtils.isEmpty(verify) || !verify.equals(session.getAttribute("captchaToken"))){
					msg = "验证码不存在或错误！";
					map.addAttribute("msg", msg);
					return "qgfwebapp/modifyPassWord";
				}
				
				if (StringUtils.isEmpty(sessionVcrand) || StringUtils.isEmpty(seesionPhone)) {

					map.addAttribute("msg", "手机验证码错误");

					return "qgfwebapp/modifyPassWord";
				} else {
					if (!(sessionVcrand.equals(code) && seesionPhone.equals(mobile))) {
						map.addAttribute("msg", "手机验证码错误");
						return "qgfwebapp/modifyPassWord";
					}
				}
				request.setAttribute("telphone",mobile);
				return "qgfwebapp/modifyNewPassWord";
			}
	}
	
	
	
	
	@RequestMapping("webapp/forgetPaypassword")
	public String forgetPay(ModelMap map, HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		
		return "webapp/forgetPaypassword";
	}

	
	
	
	/**
	 * 上传头像
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "webapp/uploadPic")
	public String uploadPic(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap map)
			throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获得第一张图片（根据前台的file name属性得到上传的文件）
		MultipartFile imgFile = multipartRequest.getFile("imgFile");

		// 定义一个数组用于保存可上传的文件类型
		List<String> fileTypes = new ArrayList<String>();
		fileTypes.add("jpg");
		fileTypes.add("jpeg");
		fileTypes.add("png");

		// 保存第一张图片
		if (!(imgFile.getOriginalFilename() == null || "".equals(imgFile.getOriginalFilename()))) {
			UploadUtil uploadUtil = new UploadUtil();

			String path = request.getRealPath("/");

			File file = uploadUtil.getFile(imgFile, fileTypes, path, "", "");
			if(file != null ) {
				String strurl = file.getPath();
				String str = strurl.substring(0, strurl.lastIndexOf('.'));
				UploadUtil.saveImageAsJpg(file.getPath(), str + "_120x120.jpg",
						120, 120, true);
				UploadUtil.saveImageAsJpg(file.getPath(), str + "_100x100.jpg",
						100, 100, true);
				UploadUtil.saveImageAsJpg(file.getPath(), str + "_80x80.jpg", 80,
						80, true);

				User user = getCurrUser(request, response);
				if (user != null) {
					String filePath = file.getPath();
					filePath = filePath.replaceAll("\\\\", "/");
					int index = filePath.indexOf("/data");
					String litpic = filePath.substring(index);
					user.setLitpic(litpic);
					userService.userUpdate(user);
					user.setBigPic(user.getLitpic().substring(0,
							user.getLitpic().lastIndexOf("."))
							+ "_120x120.jpg");
					user.setOnPic(user.getLitpic().substring(0,
							user.getLitpic().lastIndexOf("."))
							+ "_100x100.jpg");
					user.setSmallPic(user.getLitpic().substring(0,
							user.getLitpic().lastIndexOf(".")) 
							
							+ "_80x80.jpg");
					session.setAttribute("userInSession", user);
					map.addAttribute("msg", "图像上传成功");
				}
				
			} else {
				map.addAttribute("msg", "图片格式不支持");
			}
		}
		return "myhome/avatar_upload";
	}

	/**
	 * 在绑定银行卡的时候未实名认证，则跳转到实名认证页
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "webapp/toRealNamePage")
	public String toRealNamePage(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		return "myhome/user-auth";
	}

	/**
	 * 跳转到帮助中心
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "webapp/help/list")
	public String toHelpList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		return "new/helps";
	}

	/**
	 * 跳转到提现页
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value = "webapp/myhome/withdraw")
	public String toWithDrawPage(HttpServletRequest request,HttpServletResponse response, HttpSession session, ModelMap map) throws SQLException {

		User user = getCurrUser(request, response);
		if(user==null){
			return "redirect:/webapp/tologin.html";
		}
		// 拿到登录用户的账户，在提现页面显示 用
		UserAccount userAccount = accountService.getUserAccount(user.getId());
		map.put("userAccount", userAccount);
		List<BankCard> bclist=bankCardService.selectAllBankCardByUser(user.getId());
		map.put("bclist", bclist);
		map.put("msg", request.getParameter("msg"));
		return "qgfwebapp/Withdrawals";
	}
	/**
	 * 提现
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "webapp/withdrawadd")
	public String withdrawadd(HttpServletRequest request,HttpServletResponse response, HttpSession session, ModelMap map) {
		String msg = "";
		double tmoney;
		UserAccount userAccount = null;
		MD5 md5 = new MD5();
		try {
			// 获取登录用户的账户
			User user = getCurrUser(request, response);
			if(user==null){
				return "redirect:/webapp/tologin.html";
			}
			userAccount = accountService.getUserAccount(user.getId());
			List<BankCard> bclist=bankCardService.selectAllBankCardByUser(user.getId());
			if(bclist.size()<=0){
				msg = "请绑定银行卡";
				map.put("msg", msg);
				return "redirect:/webapp/myhome/withdraw.html";
			}
			map.put("bclist", bclist);
			// 获取页面提交过来的提现金额
			String tmoneyString = request.getParameter("tmoney");
			tmoney = Double.valueOf(tmoneyString);
			String paypassword = request.getParameter("paypassword");
			//String cardNo=request.getParameter("cardNo");

			if (!StringUtils.isNotEmpty(request.getParameter("tmoney"))) {// 判断输入是否为空
				msg = "提现金额不能为空!";
			} else if (tmoney <= 0) { // 判断提现金额是否大于零
				msg = "输入的金额必须大于0";
			} else if (Double.valueOf(tmoney) > userAccount.getMoneyUsable()) {// 判断提现金额是否超过可用金额
				msg = "提现金额不能大于可用金额!";
			} else {
				//如果是小数，则检查小数点后面有几位(提现金额最多精确到分,即小数点后两位)
				if(tmoneyString.contains(".")) {
					String[] s = tmoneyString.split("\\.");
					if(s[1].length()>2) {
						msg = "提现金额的最小单位是分!";
						map.put("msg", msg);
						map.put("userAccount", userAccount);
						return "redirect:/webapp/myhome/withdraw.html";
					}
				}
				System.out.println(user.getPasswordPayHash().toLowerCase());
				System.out.println(md5.getMD5ofStr(paypassword).toLowerCase());
				if(!StringUtils.isNotEmpty(request.getParameter("paypassword"))){
					msg = "交易密码不能为空!";
					map.put("msg", msg);
					return "redirect:/webapp/myhome/withdraw.html";
				}else{
					if(user.getPasswordPayHash().equals(md5.getMD5ofStr(paypassword).toLowerCase())){
						// 输入正确
						double moneyUsable = userAccount.getMoneyUsable() - tmoney;
						double moneyWithdraw = userAccount.getMoneyWithdraw() + tmoney;
						userAccount.setMoneyUsable(moneyUsable);
						userAccount.setMoneyWithdraw(moneyWithdraw);
						userAccount.setUpdatedAt(System.currentTimeMillis() / 1000);
						//提现相关操作
						accountService.tiXianMethods(request, userAccount, tmoney);
						// 更新request中的userAccount
						userAccount = accountService.getUserAccount(user.getId());
						
						UserWithdraw uw=new UserWithdraw();
						uw.setMoneyWithdraw(tmoney);
						uw.setRemark("提现冻结金额:"+tmoney);
						uw.setStatus(0);
						uw.setCreatedBy(user.getId());
						uw.setCreatedAt(DateUtils.getTime(new Date()));
						uw.setUpdatedBy(user.getId());
						uw.setUpdatedAt(DateUtils.getTime(new Date()));  //审核时间  目前取当前时间 等审核 之后再修改
						uw.setCreatedIp(IPUtils.getRemortIP(request));
						uw.setUseCard(String.valueOf(bclist.get(0).getId()));
						userWithdrawService.addUserWithdraw(uw);
						
						
						msg = "提现申请成功，请等待管理员审核!";
						map.put("msg", msg);
						map.put("userAccount", userAccount);
						return "redirect:/webapp/myhome/withdraw.html";
					}else{
						msg = "交易密码不正确!";
						map.put("msg", msg);
						map.put("userAccount", userAccount);
						return "redirect:/webapp/myhome/withdraw.html";
					}
				}
				
				
			}
			// 在提现页面显示用
			map.put("msg", msg);
			map.put("userAccount", userAccount);
		} catch (Exception e) {
			msg = "请输入正确的金额!";
			map.put("msg", msg);
			map.put("userAccount", userAccount);
			return "redirect:/webapp/myhome/withdraw.html";
		}

		return "redirect:/webapp/myhome/withdraw.html";
	}
	
	/**
	 * 用户中心充值
	 * @Description:  TODO
	 * @param:        @param map
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param nid
	 * @param:        @return
	 * @param:        @throws Exception   
	 * @return:       String   
	 * @throws
	 */
	@RequestMapping("webapp/myhome/charge")
	@Transactional(rollbackFor={Exception.class})
	public String charge(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		User user = getCurrUser(request, response);
		if(user==null){
			return "redirect:/webapp/tologin.html";
		}
		map.addAttribute("user2", user);
		UserAccount ua = accountService.getUserAccount(user.getId());
		map.addAttribute("ua", ua);
		
		List<BankCard> bclist=bankCardService.selectAllBankCardByUser(user.getId());
		for(BankCard bc:bclist){
			if(!StringUtil.isEmpty(bc.getCardNo())){
				String num = bc.getCardNo();
				if(num.length()>4){
					num =num.substring(num.length()-4, num.length());
				}
				bc.setCardNumSub(num);
			}
		}
		map.put("bclist", bclist);
		return "qgfwebapp/Recharge";
		
	}
	
	/**
	 * 交易明细
	 * @Title: moneyLog 
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("webapp/myhome/moneyLog")
	public String moneyLog(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception {
		 User user = new User();
	        user = this.getCurrUser(request, response);
	        if(user==null){
				return "redirect:/webapp/tologin.html";
			}
            long user_id=user.getId();
            PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
            populate(pageRequest, request);
            pageRequest.setPageSize(10);
            Map<String, String> params = getParamMap(request);
            String type=params.get("type");
            if(StringUtil.isNotEmpty(params.get("page"))){
                pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
            }
            if(StringUtil.isNotEmpty(params.get("type"))){
                params.put("type", params.get("type"));
            }
            params.put("userId",String.valueOf(user_id));
            pageRequest.setFilters(params);
            Page<UserAccountLog> accountpage=borrowService.getAccountLogByUserId(pageRequest);
            map.addAttribute("type",type);
            map.addAttribute("page",pageRequest.getPageNumber());
            map.addAttribute("borrowPage", accountpage);
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(accountpage.getTotalCount(), accountpage.getPageSize()));
            if(StringUtil.isNotEmpty(params.get("type"))){
                    if(params.get("type").equals("1")){
                        map.addAttribute("mt", 21);//充值记录
                    }else{
                        map.addAttribute("mt", 22);//提现记录
                    }
            }else{
                map.addAttribute("mt", 23);//交易明细
            }
		return "qgfwebapp/transactionRecord";
	}

	/**
	 * 收益明细toubiao
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("webapp/myhome/tzjl")
	public String tzjl(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user==null){
			return "redirect:/webapp/tologin.html";
		}
		long user_id=user.getId();

		PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
		populate(pageRequest, request);
		String numberstr =(String) request.getParameter("topNext");
		pageRequest.setPageSize(8);
		Map<String, String> params = getParamMap(request);
		if(StringUtil.isNotEmpty(params.get("page"))){
			pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
		}
		params.put("userId",String.valueOf(user_id));
		pageRequest.setFilters(params);
		Page<BorrowTender> borrowpage=borrowService.getInvestTenderListByUserid(pageRequest);
		map.addAttribute("page",pageRequest.getPageNumber());
		map.addAttribute("borrowPage", borrowpage);
		map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
		return "webapp/tzjl";
	}
	
	/**
	 * 已完成的标记录
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("webapp/myhome/tzjl2")
	public String tzjl2(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user==null){
			return "redirect:/webapp/tologin.html";
		}
		long user_id=user.getId();
		PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
		populate(pageRequest, request);
		pageRequest.setPageSize(8);
		Map<String, String> params = getParamMap(request);
		if(StringUtil.isNotEmpty(params.get("page"))){
			pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
		}
		params.put("userId",String.valueOf(user_id));
		params.put("type", "2");
		pageRequest.setFilters(params);
		Page<BorrowTender> borrowpage=borrowService.getSuccessListByUserid(pageRequest);
		map.addAttribute("page",pageRequest.getPageNumber());
		map.addAttribute("borrowPage", borrowpage);
		map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
		return "webapp/tzjl2";
	}
	/**
	 * 我的红包
	 */
	
	@RequestMapping("webapp/myhome/hongbao")
	public String hongbao(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user==null){
			return "redirect:/webapp/tologin.html";
		}
		long user_id=user.getId();

		PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
		populate(pageRequest, request);
		pageRequest.setPageSize(5);
		Map<String, String> params = getParamMap(request);
		params.put("userId",String.valueOf(user.getId()));
		if(StringUtil.isNotEmpty(params.get("page"))){
			pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
		}
		pageRequest.setFilters(params);
		Page<Hongbao> borrowpage=userService.getHongbaoBypage(pageRequest);
		map.addAttribute("borrowPage", borrowpage);
		map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
		return "qgfwebapp/redWallet";
	}
	
	/**
	 * 找回交易密码
	 */
	
	@RequestMapping("webapp/myhome/findjypwdUp")
	public String findjypwdUp(
			HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session,
			ModelMap map,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "paypassword", required = false) String paypassword) {

		User user = getCurrUser(request, response);
		MD5 md5 = new MD5();
		
		String imgcode = request.getParameter("imgCode");
		if(StringUtils.isEmpty(imgcode) || !imgcode.equals(session.getAttribute("captchaToken"))){
			map.addAttribute("msg", "你输入的图形验证码不正确！");
			return "redirect:/webapp/myhome/findjypwd.html";
		}
		
		String sessionVcrand = (String) session.getAttribute("vcrand");
		String msg = "";
		if (!"".equals(paypassword) && !"".equals(code)) {
			if (user.getPasswordPayHash().toLowerCase()
					.equals(md5.getMD5ofStr(paypassword).toLowerCase())) {
				msg = "新旧交易密码相同，请重新输入新交易密码!";
			}  else if (StringUtils.isEmpty(sessionVcrand)
					|| !(sessionVcrand.equals(code))) {
				msg = "验证码有误，请重新输入!";
			}else if (paypassword.length() < 6 || paypassword.length() > 15) {
				msg = "新交易密码长度在6到15之间";
			} else if (user.getPasswordHash().toLowerCase()
					.equals(md5.getMD5ofStr(paypassword).toLowerCase())) {
				msg = "交易密码不能和登录密码一致，请重新输入!";
			} else {// 验证都通过，可以修改
				try {
					user.setPasswordPayHash(md5.getMD5ofStr(paypassword));
					int count = userService.userUpdate(user);
					if (count > 0) {
						msg = "修改成功!";
					} else {
						msg = "修改失败!";
					}
				} catch (Exception e) {
					map.put("msg", msg);
					e.printStackTrace();
					TransactionAspectSupport.currentTransactionStatus()
							.setRollbackOnly();
				}
			}
		} else {
			msg = "信息填写不完整，请检查!";
		}
		map.put("msg", msg);
		map.put("type", request.getParameter("type"));
		return "redirect:/webapp/myhome/findjypwd.html";
	}
	/**
	 * 找回交易密码前置页面
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("webapp/myhome/findjypwd")
	public String findjypwd(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		User user = getCurrUser(request, response);
		if(user==null){
			return "redirect:/webapp/tologin.html";
		}
		map.addAttribute("user", user);
		map.addAttribute("msg", request.getParameter("msg"));
		return "webapp/find-jypwd";
	}
	/**
	 * 推广联盟
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping("webapp/myhome/tuiguang")
	public String tuiguang(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException{
		User user = getCurrUser(request, response);
		if(user!=null){
			map.addAttribute("user", user);
		}
		return "qgfwebapp/InviteFriends";
	}
	
	
	@RequestMapping("webapp/myhome/toubiao")
	public String toubiao(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			long user_id=user.getId();

			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			String numberstr =(String) request.getParameter("topNext");
			pageRequest.setPageSize(10);
			Map<String, String> params = getParamMap(request);
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			params.put("userId",String.valueOf(user_id));
			pageRequest.setFilters(params);
			Page<BorrowTender> borrowpage=borrowService.getInvestTenderListByUserid(pageRequest);
			map.addAttribute("page",pageRequest.getPageNumber());
			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
			map.addAttribute("mt", 35);//借出明细帐
		}
		return "qgfwebapp/InvestmentRecords";
	}
	
	
	
	/**
	 * 正在收款的项目，已还清项目
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("webapp/myhome/sklist")
	public String sklist(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			long user_id=user.getId();
			String type=request.getParameter("type");
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			String numberstr =(String) request.getParameter("topNext");
			pageRequest.setPageSize(10);
			Map<String, String> params = getParamMap(request);
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			params.put("userId",String.valueOf(user_id));
			params.put("type", type);
			pageRequest.setFilters(params);
			Page<BorrowTender> borrowpage=borrowService.getSuccessListByUserid(pageRequest);
			map.addAttribute("page",pageRequest.getPageNumber());
			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
			if(type.equals("1")){
				 //正在收款的项目
				map.addAttribute("mt", 32);
			}else if(type.equals("2")){
				 //已还清项目
				map.addAttribute("mt", 36);
			}
			map.addAttribute("type", type);
		}
		
		return "qgfwebapp/InvestmentRecords-sklist";
	}
	
	/**
	 * 收款明细
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("webapp/myhome/repaymentlist")
	public String repaymentlist(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		User user = new User();
		user = this.getCurrUser(request, response);
		if(user!=null){
			long user_id=user.getId();
			int status=NumberUtil.getInt(request.getParameter("status"));
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);
			Map<String, String> params = getParamMap(request);
			params.put("userId",String.valueOf(user_id));
			params.put("status", String.valueOf(status));
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			
			pageRequest.setFilters(params);
			Page<BorrowCollection> borrowpage=borrowService.getCollectionList(pageRequest);
			map.addAttribute("borrowPage", borrowpage);
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(borrowpage.getTotalCount(), borrowpage.getPageSize()));
			if(status==0){
				//未收款明细帐
				map.addAttribute("mt", 37);
			}else if(status==2){
			//已收款明细帐
				map.addAttribute("mt", 38);
			}
			map.addAttribute("status", status);
		}
		return "qgfwebapp/InvestmentRecords-repaymentlist";
	}
}