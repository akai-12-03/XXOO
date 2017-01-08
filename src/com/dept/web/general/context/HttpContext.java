package com.dept.web.general.context;

public class HttpContext {
	public static enum LoginDomain {
		WEB(false), CALLCENTER(false), FORUM(false), BACKEND(false), AGENCY(false), PROVIDER(
				false);
		// MERCHANT(false), PARK(false),

		private boolean sso = false;

		private LoginDomain(boolean sso) {
			this.sso = sso;
		}

		public boolean ssoEnabled() {
			return this.sso;
		}
	}
	public enum SessionKey {
		LOGINED_USER("logined_user"), LOGINED_PASSWORD("logined_password"), EXPLORER_IPAREA("explorer_iparea"), LOGINED_DOMAIN("logined_domain"), LOGINING_REDIRECT("logining_redirect"), LOGINING_CHECK_CODE("logining_checkcode"), LOGINING_ACTIVE("active")
		,LOGINED_SESSION("logined_session"),JSESSIONID("jsessionId");
		private String key;

		public String getKey() {
			return this.key;
		}

		private SessionKey(String key) {
			this.key = key;
		}
	}
}
