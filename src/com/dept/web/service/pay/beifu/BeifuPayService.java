package com.dept.web.service.pay.beifu;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.dept.web.context.Constant;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserRecharge;
import com.dept.web.general.util.NewDateUtils;
import com.dept.web.service.pay.AbstractPayServiceImpl;
import com.mypay.merchantutil.Md5Encrypt;
import com.mypay.merchantutil.UrlHelper;
import com.mypay.merchantutil.timestamp.TimestampUtils;

@Service("beifuPayService")
public class BeifuPayService extends AbstractPayServiceImpl {

	@Override
	public UserRecharge creatreCharge(UserRecharge recharge) {
		recharge.setStatus(0);// 临时状态, 等待回调
		recharge.setRemark("用户通过贝付网上充值" + recharge.getAccount() + "元");
		recharge.setThirdPlatform(3L);// 贝付充值参数(待确定)
		// recharge.setThirdPlatformOrderId(thirdPlatformOrderId);//贝付订单id

		return null;
	}

	@Override
	public String payCharge(UserRecharge recharge) {

		// 保存本地充值记录,资金记录
		super.payCharge(recharge);

		/*
		 * 贝付支付调用参数封装
		 */
		String timestamp = getAntiPhishingKey();
		Map<String, String[]> reqMap = new HashMap<String, String[]>();
		// reqMap.putAll(request.getParameterMap());
		reqMap.put("service", new String[] { "create_direct_pay_by_user" });// 服务名称：即时交易
		reqMap.put("partner", new String[] { BeiFuConstant.partner });// 合作者商户ID
		reqMap.put("input_charset",
				new String[] { BeiFuConstant.input_charset });// 字符集
		reqMap.put("sign_type", new String[] { BeiFuConstant.sign_type });// 签名算法
		reqMap.put("notify_url", new String[] { BeiFuConstant.notify_url });// 商户上传的服务器异步通知页面路径
		reqMap.put("return_url", new String[] { BeiFuConstant.return_url }); // 页面跳转同步通知页面路径
		reqMap.put("error_notify_url",
				new String[] { BeiFuConstant.error_notify_url });// 请求出错时的通知页面路径，可空

		/* 以下为业务参数 */

		reqMap.put("out_trade_no", new String[] { recharge.getOrderId() });// 易八通合作商户网站唯一订单号
		reqMap.put("subject", new String[] { "私享贷账户充值" });// 商品名称
		reqMap.put("payment_type", new String[] { BeiFuConstant.payment_type });// 支付类型，默认值为：1（商品购买）.目前只支持1

		/* ”卖家易八通用户ID“优先于”卖家易八通用户名“ 两者不可同时为空 */
		reqMap.put("seller_email", new String[] { BeiFuConstant.EMPTY });// 卖家易八通用户名
		reqMap.put("seller_id", new String[] { BeiFuConstant.partner });// 卖家易八通用户ID
		reqMap.put("buyer_email", new String[] { BeiFuConstant.EMPTY });// 买家易八通用户名，可空
		reqMap.put("buyer_id", new String[] { BeiFuConstant.EMPTY });// 买家易八通用户ID，可空
		reqMap.put("price", new String[] { BeiFuConstant.EMPTY });// 商品单价
		reqMap.put("total_fee",
				new String[] { String.valueOf(recharge.getMoneyRecharge()) });// 交易金额
		reqMap.put("quantity", new String[] { BeiFuConstant.EMPTY });// 购买数量
		reqMap.put("body", new String[] { BeiFuConstant.EMPTY });// 商品描述，可空
		reqMap.put("show_url", new String[] { BeiFuConstant.EMPTY });// 商品展示网址，可空
		reqMap.put("pay_method", new String[] { BeiFuConstant.pay_method });// 支付方式，directPay(余额支付)、bankPay(网银支付)，可空
		/**
		 * ABC_B2C=农行 BJRCB_B2C=北京农村商业银行 BOC_B2C=中国银行 CCB_B2C=建行
		 * CEBBANK_B2C=中国光大银行 CGB_B2C=广东发展银行 CITIC_B2C=中信银行 CMB_B2C=招商银行
		 * CMBC_B2C=中国民生银行 COMM_B2C=交通银行 FDB_B2C=富滇银行 HXB_B2C=华夏银行
		 * HZCB_B2C_B2C=杭州银行 ICBC_B2C=工商银行网 NBBANK_B2C=宁波银行 PINGAN_B2C=平安银行
		 * POSTGC_B2C=中国邮政储蓄银行 SDB_B2C=深圳发展银行 SHBANK_B2C=上海银行 SPDB_B2C=上海浦东发展银行
		 */
		// reqMap.put("default_bank", new String[]{});// 默认网银，可空
		// reqMap.put("royalty_parameters", new String[]{});// 分润用户名集(
		// 最多10组分润明细)。示例：100001=0.01|100002=0.02
		// 表示id为100001的用户要分润0.01元，id为100002的用户要分润0.02元。
		// reqMap.put("royalty_type", new String[]{});//
		// 提成类型，目前只支持一种类型：10，表示卖家给第三方提成；

		// 反钓鱼用参数
		reqMap.put("anti_phishing_key", new String[] { timestamp });
		reqMap.put("exter_invoke_ip", new String[] { recharge.getCreatedIp() });// 用户在外部系统创建交易时，由外部系统记录的用户IP地址

		String paramStr = UrlHelper.sortParamers(reqMap);
		String plaintext = TimestampUtils.mergePlainTextWithMerKey(paramStr,
				BeiFuConstant.key);

		String sign = Md5Encrypt.encrypt(plaintext);
		reqMap.put("sign", new String[] { sign });
		String url = null;
		try {
			String encodedParamString = UrlHelper.encode(reqMap,
					BeiFuConstant.input_charset);
			url = BeiFuConstant.gateway + "?" + encodedParamString;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return url;
	}

	@Override
	public String payChargeNofity(Map<String, String> params) {
		if (checkSign(params)) {
			String out_trade_no = params.get("out_trade_no");
			UserRecharge userRecharge = rechargeService
					.getRechargeByOrderNo(out_trade_no);
			if (userRecharge == null) {
				// 充值记录不存在
				return null;
			}
			if (userRecharge.getStatus() == 0) {// 第一次回调
				// 用户账户增加充值金额
				userAccountService.recharge(userRecharge.getCreatedBy(),
						userRecharge.getMoneyRecharge());

				// UserRecharge updateRecharge = new UserRecharge();
				// updateRecharge.setOrderId(userRecharge.getOrderId());
				// updateRecharge.setMoneyRecharge(moneyRecharge)(userRecharge.getOrderId());
				// updateRecharge.setOrderId(userRecharge.getOrderId());
				// updateRecharge.setOrderId(userRecharge.getOrderId());
				// updateRecharge.setOrderId(userRecharge.getOrderId());
				String trade_status = params.get("trade_status");
				if (trade_status.equals("TRADE_FINISHED")) {
					userRecharge.setStatus(1);
				}
				userRecharge.setPayResult(trade_status);
				userRecharge.setUpdatedAt(NewDateUtils.getNowTimeStr());
				// 更新充值记录
				rechargeService.updateRecharge(userRecharge);

				// 生成资金变动记录
				UserAccount account = userAccountService
						.getUserAccount(userRecharge.getCreatedBy());

				accountLogService.addUserAccountLog(account,
						userRecharge.getCreatedBy(),
						userRecharge.getMoneyRecharge(),
						Constant.ACCOUNT_LOG_TYPE_TZCG,
						userRecharge.getCreatedIp(), userRecharge.getRemark());
			}
			return params.get("notify_id");
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private String getAntiPhishingKey() {
		AskForTimestamp askTimestamp = new AskForTimestamp();
		String timestamp = null;

		try {
			timestamp = askTimestamp.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return timestamp;
	}

	private boolean checkSign(Map<String, String> params) {
		// 原加密字段
		String sign = params.get("sign");
		// 得到回传参数，并重新构建，iso-8859-1解决乱码问题
		Map<String, String> paramMap = new HashMap<String, String>(params);
		for (Iterator it = paramMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			Object value = entry.getValue();
			String paramValue;
			if (value == null) {
				entry.setValue(null);
			} else if (value instanceof String) {
				paramValue = (String) value;
				entry.setValue(paramValue);
			} else if (value instanceof String[]) {
				String[] values = (String[]) value;
				paramValue = (String) values[0];
				entry.setValue(paramValue);
			}
		}

		paramMap.remove("sign");

		List<String> keyList = new ArrayList<String>(paramMap.keySet());
		Collections.sort(keyList);

		StringBuffer strBuf = new StringBuffer();
		for (String paramName : keyList) {
			String paramValue = paramMap.get(paramName);
			Boolean a = false;
			if (paramValue != null || !paramValue.trim().equals("")) {
				a = true;
			}
			strBuf.append((strBuf.length() > 0 ? "&" : "") + paramName + "="
					+ (a ? paramValue : ""));
		}

		// 加密(MD5加签)，默认已取UTF-8字符集，如果需要更改，可调用Md5Encrypt.setCharset(inputCharset)
		String paramString = strBuf.toString();
		paramString = new StringBuilder(paramString).append(BeiFuConstant.key)
				.toString();
		String signl = Md5Encrypt.encrypt(paramString);

		// System.out.print("参数URL="+request.getQueryString());
		// System.out.print("回调的sign="+sign);
		// System.out.print("本地加密的sign="+sign);

		// MD5验签并回传
		if (sign.equals(signl)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean payNofityValidate(Map<String, String> params) {
		return checkSign(params);
	}
}
