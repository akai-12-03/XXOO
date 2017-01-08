package com.dept.web.dao.model;

/**
 * 
 * @ClassName:     IdentifierReturn.java
 * @Description:   身份证认证返回信息
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-10-14 下午4:59:07
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class IdentifierReturn {

	private IdentifierData Identifier;
	
	private String RawXml;
	
	private String ResponseCode;
	
	private String ResponseText;

	public IdentifierData getIdentifier() {
		return Identifier;
	}

	public void setIdentifier(IdentifierData identifier) {
		Identifier = identifier;
	}

	public String getRawXml() {
		return RawXml;
	}

	public void setRawXml(String rawXml) {
		RawXml = rawXml;
	}

	public String getResponseCode() {
		return ResponseCode;
	}

	public void setResponseCode(String responseCode) {
		ResponseCode = responseCode;
	}

	public String getResponseText() {
		return ResponseText;
	}

	public void setResponseText(String responseText) {
		ResponseText = responseText;
	}
	
	
}
