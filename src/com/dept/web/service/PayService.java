package com.dept.web.service;

import java.util.Map;

import com.dept.web.dao.model.UserRecharge;

public interface PayService {
	
	
	
	/**
	 * 充值
	 * @param recharge
	 * @return
	 */
	public String payCharge(UserRecharge recharge);
	
	
	/**
	 * 提现
	 * @return
	 */
	public String withdraw(double d);


	/**
	 * 充值回调
	 * @param params
	 * @return
	 */
	public String payChargeNofity(Map<String, String> params);
	
	
	/**
	 * 支付回调数据有效性验证
	 * @param params
	 * @return
	 */
	public boolean payNofityValidate(Map<String, String> params);
	
	
	
}
