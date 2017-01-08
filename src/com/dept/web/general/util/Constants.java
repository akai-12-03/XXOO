package com.dept.web.general.util;

public class Constants {
	
	public static final String SESSION_USER_ID = "userId";
	
	/**
	 * 幻灯图片类型
	 */
	public static final String IMAGE_SLIDE_TYPE = "02";
	
	/**
	 * 最顶部图片类型（logo）
	 */
	public static final String IMAGE_LOGO_TYPE = "01";
	
	/**
	 * 幻灯图片路径key
	 * image_url
	 */
	public static final String IMAGE_URL = "image_url";
	
	/**
	 * 科室带头人key
	 * image_member_url
	 */
	public static final String IMAGE_MEMBER_URL = "image_member_url";
	
	
	public static String CODE_FAILURE = "0"; // 失败，服务器未知错误（服务器错误，请报告服务端）
	public static String CODE_SUCCESS = "1"; // 成功
	public static String CODE_PARAMS_ERROR = "-1"; // 参数错误（要传入的参数是否正确）
	public static String CODE_VALIDATION_ERROR = "-2"; // 权限验证失败
	public static String CODE_NO_METHOD = "-3"; // 接口不存在，此情况请确认req传输是否正确
	public static String CODE_LOGIN_OVERTIME = "-4"; // 登录超时
	public static String CODE_SERVICE_PAUSE = "-5"; // 系统正在维护中，暂停服务
	public static String CODE_ILLEGAL_MEMBER = "-1"; // 非法用户名
	public static String CODE_MEMBER_REPEATED = "-2"; // 用户名重复
	
	public static String MSG_FAILURE = "失败，服务器未知错误（服务器错误，请报告服务端）";
	public static String MSG_SUCCESS = "成功";
	public static String MSG_PARAMS_ERROR = "参数错误（要传入的参数是否正确）";
	public static String MSG_PARAMS_ERROR_TWO = "图片url不能全部为空";
	public static String MSG_VALIDATION_ERROR = "权限验证失败";
	public static String MSG_NO_METHOD = "接口不存在，此情况请确认req传输是否正确";
	public static String MSG_LOGIN_OVERTIME = "登录超时";
	public static String MSG_SERVICE_PAUSE = "系统正在维护中，暂停服务";
	public static String MSG_ILLEGAL_MEMBER = "非法用户名";
	public static String MSG_MEMBER_REPEATED = "用户名重复";
	public static String MSG_MOBILENUM_ERROR = "输入的手机号码有误!";
	public static String MSG_MOBILE_VCODE_ERROR = "验证码有误!";
	public static String MSG_MOBILE_BETS_NOENOUGH = "活动金额不足!";
	
	
	public static String RETURN_TYPE_FILE = "FILE";
	
	public static String CLIENT_TYPE_ANDROID = "android";
	public static String CLIENT_TYPE_IPHONE = "iphone";
	public static String CLIENT_TYPE_IPHONE_TWO = "ios";
	public static String CLIENT_TYPE_WP7 = "wp7";
	public static String CLIENT_TYPE_WEB = "web";
	
	
	/**
	 * 注册途径
	 * source
	 */
	public static final int REG_SOURCE_1001 = 1001;  //四合网站
	public static final int REG_SOURCE_1002 = 1002;  //四合短信
	public static final int REG_SOURCE_1003 = 1003;  //手机webApp
	public static final int REG_SOURCE_1004 = 1004;  //温州贷推广链接
	public static final int REG_SOURCE_2001 = 2001;  //莱斯达WEB


}
