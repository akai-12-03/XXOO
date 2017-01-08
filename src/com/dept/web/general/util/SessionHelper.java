package com.dept.web.general.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dept.web.general.context.HttpContext;
import com.sendinfo.common.lang.StringUtil;

public class SessionHelper {
	public enum SessionType {
		USER_ID, USER_TYPE, LOG_LOGIN_ID, CAPTCHA, MOBILE, MOBILECODE, OTHER, LASTURL
	};

	/***
	 * 根据给定的SessionType获得Session的值，getSession(aRequest, aType, null)
	 * 
	 * @param aRequest
	 *            HttpServletRequest
	 * @param aType
	 *            USER_ID, USER_TYPE, CAPTCHA, OTHER（无法扩展OTHER的附加信息）
	 * @return
	 */
	public static Object getSession(HttpServletRequest aRequest,
			SessionType aType) {
		return getSession(aRequest, aType, null);
	}

	/***
	 * 根据给定的SessionType获得Session的值
	 * 
	 * @param aRequest
	 *            HttpServletRequest
	 * @param aType
	 *            USER_ID, USER_TYPE, CAPTCHA, OTHER
	 * @param aOther
	 *            SessionType.OTHER的扩展信息，当OTHER.equals(aType)时才起效，key为"OTHER_{aOther}"
	 * @return
	 */
	public static Object getSession(HttpServletRequest aRequest,
			SessionType aType, String aOther) {
		Object result = null;

		if (aType != null) {
			String strKey = aType.toString();
			if (SessionType.OTHER.equals(aType) && !StringUtil.isEmpty(aOther))
				strKey += "_" + aOther;

			result = aRequest.getSession().getAttribute(strKey);
		}
		return result;
	}

	/***
	 * 根据给定的SessionType设置Session值，setSession(aRequest, aType, aValue, null)
	 * 
	 * @param aRequest
	 *            HttpServletRequest
	 * @param aType
	 *            USER_ID, USER_TYPE, CAPTCHA, OTHER（无法扩展OTHER的附加信息）
	 * @param aValue
	 *            Session的值
	 */
	public static void setSession(HttpServletRequest aRequest,
			SessionType aType, Object aValue) {
		if(aValue==null)
			clearSession(aRequest,aType);
		setSession(aRequest, aType, aValue, null);
	}

	/***
	 * 根据给定的SessionType设置Session值
	 * 
	 * @param aRequest
	 *            HttpServletRequest
	 * @param aType
	 *            USER_ID, USER_TYPE, CAPTCHA, OTHER
	 * @param aValue
	 *            Session的值
	 * @param aOther
	 *            SessionType.OTHER的扩展信息，当OTHER.equals(aType)时才起效，key为"OTHER_{aOther}"
	 */
	public static void setSession(HttpServletRequest aRequest,
			SessionType aType, Object aValue, String aOther) {
		if (aType != null) {
			String strKey = aType.toString();
			if (SessionType.OTHER.equals(aType) && !StringUtil.isEmpty(aOther))
				strKey += "_" + aOther;
			aRequest.getSession().setAttribute(strKey, aValue);
		}
	}

	/***
	 * 根据给定的SessionType获取Integer的Session值，getSessionForInt(aRequest, aType,
	 * null)
	 * 
	 * @param aRequest
	 *            HttpServletRequest
	 * @param aType
	 *            USER_ID, USER_TYPE, CAPTCHA, OTHER
	 * @return
	 */
	public static Integer getSessionForInt(HttpServletRequest aRequest,
			SessionType aType) {
		return getSessionForInt(aRequest, aType, null);
	}

	public static Long getSessionForLong(HttpServletRequest aRequest,
			SessionType aType) {
		return getSessionForLong(aRequest, aType, null);
	}

	/***
	 * 根据给定的SessionType获取Integer的Session值，getSession(aRequest, aType, aOther)
	 * 
	 * @param aRequest
	 *            HttpServletRequest
	 * @param aType
	 *            USER_ID, USER_TYPE, CAPTCHA, OTHER
	 * @param aOther
	 *            SessionType.OTHER的扩展信息，当OTHER.equals(aType)时才起效，key为"OTHER_{aOther}"
	 * @return
	 */
	public static Integer getSessionForInt(HttpServletRequest aRequest,
			SessionType aType, String aOther) {
		Integer result = null;
		Object temp = getSession(aRequest, aType, aOther);
		if (temp != null)
			result = (Integer) temp;
		return result;
	}

	public static Long getSessionForLong(HttpServletRequest aRequest,
			SessionType aType, String aOther) {
		Long result = null;
		Object temp = getSession(aRequest, aType, aOther);
		if (temp != null)
			result = (Long) temp;
		return result;
	}

	/***
	 * 根据给定的SessionType获取String的Session值，getSessionForStr(aRequest, aType, null)
	 * 
	 * @param aRequest
	 *            HttpServletRequest
	 * @param aType
	 *            USER_ID, USER_TYPE, CAPTCHA, OTHER
	 * @return
	 */
	public static String getSessionForStr(HttpServletRequest aRequest,
			SessionType aType) {
		return getSessionForStr(aRequest, aType, null);
	}

	/***
	 * 根据给定的SessionType获取String的Session值，getSession(aRequest, aType, aOther)
	 * 
	 * @param aRequest
	 *            HttpServletRequest
	 * @param aType
	 *            USER_ID, USER_TYPE, CAPTCHA, OTHER
	 * @param aOther
	 *            SessionType.OTHER的扩展信息，当OTHER.equals(aType)时才起效，key为"OTHER_{aOther}"
	 * @return
	 */
	public static String getSessionForStr(HttpServletRequest aRequest,
			SessionType aType, String aOther) {
		String result = "";
		Object temp = getSession(aRequest, aType, aOther);
		if (temp != null)
			result = (String) temp;
		return result;
	}

	/***
	 * 获取用户ID
	 * 
	 * @param aRequest
	 *            HttpServletRequest
	 * @return
	 */
	public static Long getUserId(HttpServletRequest request, HttpServletResponse response) {
		Long id = getSessionForLong(request, SessionType.USER_ID);
		if (id == null && HttpContext.LoginDomain.WEB.ssoEnabled()) {
			String s = HttpUtil.get(request, HttpContext.SessionKey.LOGINED_USER.toString());
			if (!StringUtil.isEmpty(s)) {
				id = Long.parseLong(s);
				setUserId(request, response, id);
			}
		}		
		return id;
	}

	/***
	 * 设置用户ID
	 * 
	 * @param aRequest
	 *            HttpServletRequest
	 * @return
	 */
	public static void setUserId(HttpServletRequest request, HttpServletResponse response, Long userId) {
		setSession(request, SessionType.USER_ID, userId);
		if (HttpContext.LoginDomain.WEB.ssoEnabled())
			HttpUtil.put(response, 
				new String[] { HttpContext.SessionKey.LOGINED_USER.toString(), HttpContext.SessionKey.LOGINED_DOMAIN.toString() , HttpContext.SessionKey.LOGINING_ACTIVE.toString()},
				new String[] { userId.toString(), HttpContext.LoginDomain.WEB.toString(), "0" }
			);
	}


	/***
	 * 获取用户类型
	 * 
	 * @param aRequest
	 *            HttpServletRequest
	 * @return 用户类型 (11=游客;21=企业用户;31=管理员;)
	 */
	public static Integer getUserType(HttpServletRequest aRequest) {
		return getSessionForInt(aRequest, SessionType.USER_TYPE);
	}

	/***
	 * 获取登录记录ID
	 * 
	 * @param aRequest
	 *            HttpServletRequest
	 * @return
	 */
	public static Integer getLogLoginId(HttpServletRequest aRequest) {
		return getSessionForInt(aRequest, SessionType.LOG_LOGIN_ID);
	}

	/***
	 * 根据给定的SessionType清除Session值
	 * 
	 * @param aRequest
	 *            HttpServletRequest
	 * @param aType
	 *            USER_ID, USER_TYPE, CAPTCHA, OTHER
	 * @param aOther
	 *            SessionType.OTHER的扩展信息，当OTHER.equals(aType)时才起效，key为"OTHER_{aOther}"
	 */
	public static void clearSession(HttpServletRequest aRequest,
			SessionType aType, String aOther) {
		if (aType != null) {
			String strKey = aType.toString();
			if (SessionType.OTHER.equals(aType) && !StringUtil.isEmpty(aOther))
				strKey += "_" + aOther;
			aRequest.getSession().removeAttribute(strKey);
		}
	}

	/***
	 * 根据给定的SessionType清除Session值，clearSession(aRequest, aType, null)
	 * 
	 * @param aRequest
	 *            HttpServletRequest
	 * @param aType
	 *            USER_ID, USER_TYPE, CAPTCHA, OTHER
	 */
	public static void clearSession(HttpServletRequest aRequest,
			SessionType aType) {
		clearSession(aRequest, aType, null);
	}

	/***
	 * 清空所有Session
	 * 
	 * @param aRequest
	 *            HttpServletRequest
	 */
	public static void clearSessionAll(HttpServletRequest aRequest) {
		aRequest.getSession().invalidate();
	}

	public static void putLastUrl(HttpServletRequest request,
			HttpServletResponse response, String url) {
		setSession(request, SessionType.LASTURL, url);
	}

	public static String pushLastUrl(HttpServletRequest request,
			HttpServletResponse response) {
		Object obj = getSession(request, SessionType.LASTURL);
		if (obj == null) {
			return "";
		} else {
			String url = (String) obj;
			clearSession(request, SessionType.LASTURL);
			return url;
		}
	}
}
