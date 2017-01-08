package com.dept.web.general.service.vo;

import java.io.Serializable;


public class VoResponse implements Serializable{

	private static final long serialVersionUID = -1878309972068096244L;
	
	private String info;
	
	private String url;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
