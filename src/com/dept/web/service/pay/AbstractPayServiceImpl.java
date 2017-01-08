package com.dept.web.service.pay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.context.Constant;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountLog;
import com.dept.web.dao.model.UserRecharge;
import com.dept.web.service.PayService;
import com.dept.web.service.UserAccountLogService;
import com.dept.web.service.UserAccountService;
import com.dept.web.service.UserRechargeService;

@Transactional
public abstract class AbstractPayServiceImpl implements PayService {

	
	@Autowired
	protected UserAccountService userAccountService;
	
	
	@Autowired
	protected UserAccountLogService accountLogService;
	
	
	@Autowired
	protected UserRechargeService rechargeService;
	
	
	/**
	 * 子类重设recharge值使用
	 * @param recharge
	 * @return
	 */
	public abstract UserRecharge creatreCharge(UserRecharge recharge);
	
	@Override
	public String payCharge(UserRecharge recharge) {
		//生成订单号
		Random random = new Random(System.currentTimeMillis());
		int iRandom = random.nextInt(10000) + 10000000;
		String out_trade_no = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "-" + String.valueOf(iRandom);
		
		recharge.setOrderId(out_trade_no);//订单号
		recharge.setPaySource("Web");//支付来源（IOS，Android，Web，Wap）
		recharge.setAccount("网站支付");
		recharge.setCardNo("");//卡号
		recharge.setCreatedAt(System.currentTimeMillis()/1000);
		recharge.setPayResult("成功");//默认支付成功
		recharge.setRemark("用户充值"+recharge.getAccount()+"元");
		recharge.setStatus(1);
		recharge.setThirdPlatform(2L);
		recharge.setThirdPlatformOrderId(0L);
		
		/**/
		creatreCharge(recharge);
		
		rechargeService.addCharge(recharge);//保存提现记录
		
		return "pay/recharge-success";
	}

	@Override
	public String withdraw(double d) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
