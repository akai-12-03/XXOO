package com.dept.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dept.web.context.Constant;
import com.dept.web.context.Global;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserRecharge;
import com.dept.web.dao.model.pay.Configuration;
import com.dept.web.dao.model.pay.PaymentForOnlineService;
import com.dept.web.dao.model.pay.YeepayService;
import com.dept.web.general.util.NewDateUtils;
import com.dept.web.general.util.StringUtils;
import com.dept.web.general.util.Utils;
import com.dept.web.service.PayService;
import com.dept.web.service.UserAccountService;
import com.dept.web.service.UserRechargeService;

/**
 * 支付类
 * 
 * @ClassName: PayController
 * @Description:
 * 
 * @author cannavaro
 * @version V1.0
 * @Date 2014年11月10日 下午9:04:17 <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class PayController extends WebController {

	@Autowired
	@Qualifier("beifuPayService")//贝付支付
//	@Qualifier("defaultPayService")//默认支付
	private PayService payService;
	
	@Autowired
	private UserRechargeService rechargeService;
	@Autowired
	private UserAccountService accountService;

	public static String getKeyValue() {
		return Configuration.getInstance().getValue("keyValue");
	}
	
	/**
	 * 充值支付方法PC
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("myhome/payCharge")
	public String payCharge(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		User user = getCurrUser(request, response);
		if (user == null) {
			return "redirect:/tologin.html";
		}

		/**
		 * unfinished
		 */
		String chargeMoneyStr = request.getParameter("chargeMoney");
		if (StringUtils.isNumber(chargeMoneyStr)) {
			
			if(StringUtils.isEmpty(user.getRealname())){
				/**
				 * 用作前台显示
				 * 查询用户账户
				 */
				UserAccount userAccount = accountService.getUserAccount(user.getId());
				request.setAttribute("userAccount", userAccount);
				request.setAttribute("user", user);
			    map.addAttribute("errormsg","请先进行实名认证");
				return "myhome/charge";
			}
			if(StringUtils.isEmpty(chargeMoneyStr) || Double.valueOf(chargeMoneyStr) >= 10000000){
				/**
				 * 用作前台显示
				 * 查询用户账户
				 */
				UserAccount userAccount = accountService.getUserAccount(user.getId());
				request.setAttribute("userAccount", userAccount);
				request.setAttribute("user", user);
				map.addAttribute("errormsg","单笔最高充值金额不超过10000000元");
				return "myhome/charge";
			}
			
			//要是充值金额为@等符号，提示numberFormatEeception
			try {
				double chargeMoney = Utils.getDouble(Double.valueOf(chargeMoneyStr), 2);
				UserRecharge recharge = new UserRecharge();
				recharge.setMoneyRecharge(chargeMoney);
				recharge.setCreatedBy(user.getId());

				recharge.setCreatedIp(getRequestIp(request));
				//生成订单号
				Random random = new Random(System.currentTimeMillis());
				int iRandom = random.nextInt(10000) + 10000000;
				String out_trade_no = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "-" + String.valueOf(iRandom);
				
				//去第三方充值
				String p0_Cmd           = "Buy";
				String p2_Order         = formatString(out_trade_no);
				String p3_Amt           = formatString(chargeMoneyStr);
				String p4_Cur           = "CNY";
				String p5_Pid           = "licaiProduct";
				String p6_Pcat          = "";
				String p7_Pdesc         = "";
				String web_Url = Global.getValue("weburl");
				String p8_Url           = web_Url+"/pay/yeepay_notify.html";
				String p9_SAF           = "0";
				String pa_MP            = "";
				String pd_FrpId         = "";//1、若不填写，则直接跳转到易宝支付的默认支付网关。2、若填写，则直接跳到对应的银行支付页面。
				String pm_Period        = "7";
				String pn_Unit          = "day";
				String pr_NeedResponse  = "1";

				Map<String, String> params = new HashMap<String, String>();
				params.put("p0_Cmd", 	p0_Cmd);
				params.put("p2_Order",	p2_Order);
				params.put("p3_Amt",	p3_Amt);
				params.put("p4_Cur",	p4_Cur);
				params.put("p5_Pid",	p5_Pid);
				params.put("p6_Pcat",	p6_Pcat);
				params.put("p7_Pdesc",	p7_Pdesc);
				params.put("p8_Url",	p8_Url);
				params.put("p9_SAF",	p9_SAF);
				params.put("pa_MP",		pa_MP);
				params.put("pd_FrpId",	pd_FrpId);
				params.put("pm_Period",	pm_Period);
				params.put("pn_Unit",	pn_Unit);
				params.put("pr_NeedResponse",pr_NeedResponse);

				System.out.println("params : " + params);

				String payURL		= YeepayService.getPayURL(params);

				if("".equals(payURL)) {
					System.out.println("生成链接失败！");
				} else {
					response.sendRedirect(payURL);
				}
				
				//判断是否充值成功
				recharge.setOrderId(out_trade_no);//订单号
				recharge.setPaySource("Web");//支付来源（IOS，Android，Web，Wap）
				recharge.setAccount("网站支付");
				recharge.setCardNo("");//卡号
				recharge.setCreatedAt(System.currentTimeMillis()/1000);
				recharge.setPayResult("成功");//默认支付成功
				recharge.setRemark("用户通过易宝充值"+chargeMoneyStr+"元");
				recharge.setStatus(0);
				recharge.setThirdPlatform(2L);
				recharge.setThirdPlatformOrderId(0L);
				rechargeService.addCharge(recharge);//保存提现记录
			} catch (NumberFormatException e) {
				request.setAttribute("errormsg", "请输入正确的金额");
				
				/**
				 * 用作前台显示
				 * 查询用户账户
				 */
				UserAccount userAccount = accountService.getUserAccount(user.getId());
				request.setAttribute("userAccount", userAccount);
				request.setAttribute("user", user);
			}
		} else {
			request.setAttribute("errormsg", "请输入正确的金额");
			
			/**
			 * 用作前台显示
			 * 查询用户账户
			 */
			UserAccount userAccount = accountService.getUserAccount(user.getId());
			request.setAttribute("userAccount", userAccount);
			request.setAttribute("user", user);
			
		}

		return "myhome/charge";
	}
	public static String formatString(String text) {
		return (text == null) ? "" : text.trim();
	}
	
	
	/**
	 * 易宝支付回调
	 * @Title: yeepaynofity 
	 * @Description: TODO
	 * @param @param map
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws ServletException
	 * @param @throws IOException
	 * @param @throws Exception 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
    @RequestMapping("pay/yeepay_notify")
    @Transactional(rollbackFor={Exception.class})
    public String yeepaynofity(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception{

        String keyValue = getKeyValue();
        String r0_Cmd = request.getParameter("r0_Cmd");
        String p1_MerId = request.getParameter("p1_MerId");
        String r1_Code = request.getParameter("r1_Code");
        String r2_TrxId = request.getParameter("r2_TrxId");
        String r3_Amt = request.getParameter("r3_Amt");
        String r4_Cur = request.getParameter("r4_Cur");
        String r5_Pid =new String(request.getParameter("r5_Pid").getBytes("utf-8"),"gbk");
        String r6_Order = request.getParameter("r6_Order");
        String r7_Uid = request.getParameter("r7_Uid");
        String r8_MP = request.getParameter("r8_MP");
        String r9_BType = request.getParameter("r9_BType");
        String hmac = request.getParameter("hmac");
        Boolean isOK = true;
        String signStr=
        "keyValue="+keyValue
        +"&r0_Cmd="+r0_Cmd
        +"&p1_MerId="+p1_MerId
        +"&r1_Code="+r1_Code
        +"&r2_TrxId="+r2_TrxId
        +"&r3_Amt="+r3_Amt
        +"&r4_Cur="+r4_Cur
        +"&r5_Pid="+r5_Pid
        +"&r6_Order="+r6_Order
        +"&r7_Uid="+r7_Uid
        +"&r8_MP="+r8_MP
        +"&r9_BType="+r9_BType
        +"&hmac="+hmac;
        
        try {
            isOK=PaymentForOnlineService.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, keyValue);
            if(isOK){
                if(r1_Code.equals("1")) {

                    //成功的流程
            			UserRecharge userRecharge = rechargeService
            					.getRechargeByOrderNo(r6_Order);
            			if (userRecharge!=null &&userRecharge.getStatus() == 0) {// 第一次回调
            				// 用户账户增加充值金额
            				accountService.recharge(userRecharge.getCreatedBy(),
            						userRecharge.getMoneyRecharge());
            				userRecharge.setStatus(1);
            				userRecharge.setPayResult("success");
            				userRecharge.setUpdatedAt(NewDateUtils.getNowTimeStr());
            				// 更新充值记录
            				rechargeService.updateRecharge(userRecharge);

            				// 生成资金变动记录
            				UserAccount account = accountService
            						.getUserAccount(userRecharge.getCreatedBy());

            				accountService.addUserAccountLog(account,
            						userRecharge.getCreatedBy(),
            						userRecharge.getMoneyRecharge(),
            						Constant.ACCOUNT_LOG_TYPE_CZCG,
            						userRecharge.getCreatedIp(), userRecharge.getRemark());

            			}
                        if(r9_BType.equals("2")){ //表示是notify
                            
                            PrintWriter p=response.getWriter();
                            p.print("success");
                            p.flush();
                            p.close();
                            
                        }else{
                            
                            return "redirect:/myhome/user-index.html";
                        }
                      
                    }else{
                        
                        if(r9_BType.equals("2")){ //表示是notify
                            
                            PrintWriter p=response.getWriter();
                            p.print("success");
                            p.flush();
                            p.close();
                        }
                        
                    }
                }
        }catch (Exception e) {
            e.printStackTrace();     
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        
        return null;
    }
   
    
}