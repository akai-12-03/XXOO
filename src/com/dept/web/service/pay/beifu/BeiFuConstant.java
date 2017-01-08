package com.dept.web.service.pay.beifu;

import com.dept.web.context.Global;

/**
 * 贝付支付参数
 * 
 * @author liuhh
 * 
 */
public class BeiFuConstant {

	/*
	 * 开发环境测试商户号 201402271330347295 测试keyPDF9WKZCJ4E65UDO408207JNM27GCKqjmnjb
	 * http://180.168.127.5
	 * 
	 * 生产环境测试商户号 201204201739476361 9UCKYZ6Q804CO5O43TGHLMDO4YTU10hggixe
	 * https://www.ebatong.com
	 */

	/*
	 * 测试交易地址 http://180.168.127.5/direct/gateway.htm
	 * 测试获取时间戳地址：http://180.168.127.5/gateway.htm
	 * 测试订单查询戳地址：http://180.168.127.5/gateway.htm 
	 * 商户号： 201501131139398055 
	 * 加密KEY：
	 * 2XYEF5RDNQ0U7H25WWSHM3IF8YK0YVvgyftw (即时交易) 
	 * 加密KEY：
	 * J6MTP2HSFXC5BUZ8PTMAWJFE0LMLXSstrryp (接口代发)
	 */
	/*
	 * 正式 网关: 
	 * gateway: https://www.ebatong.com/direct/gateway.htm
	 * ask_for_time_stamp_gateway: http://www.ebatong.com/gateway.htm
	 */
	public static String partner = "201501131139398055";
	
	public static String seller_id = "201501131139398055"; // 卖家易八通用户ID

	// 商户加密KEY(即时支付)
	public static String key = "2XYEF5RDNQ0U7H25WWSHM3IF8YK0YVvgyftw";

	// 商户加密KEY(接口代发)
	public static String key_proxy = "J6MTP2HSFXC5BUZ8PTMAWJFE0LMLXSstrryp";

	// ebatong商户网关
	public static String gateway = "http://180.168.127.5/direct/gateway.htm";

	//
	public static String ask_for_time_stamp_gateway = "http://180.168.127.5/gateway.htm";
	// String ask_for_time_stamp_gateway =
	// "https://www.ebatong.com/gateway.htm";

	// 服务名称：请求时间戳
	public static String service_query_timestamp = "query_timestamp";

	// 合作者商户ID

	// 字符集
	public static String input_charset = "UTF-8";

	// 摘要签名算法
	public static String sign_type = "MD5";

	public static String notify_url = Global.getValue("weburl")+ "/pay/chargenotify.html"; // 商户上传的服务器异步通知页面路径
	public static String return_url = Global.getValue("weburl")+ "/pay/chargereturn.html"; // 页面跳转同步通知页面路径
	public static String error_notify_url = ""; // 请求出错时的通知页面路径，可空

	public static String payment_type = "1"; // 支付类型，默认值为：1（商品购买）
	public static String seller_email = ""; // 卖家易八通用户名
	public static String pay_method = "bankPay"; // 支付方式，directPay(余额支付)、bankPay(网银支付)，可空
	public static String EMPTY = ""; //
}
