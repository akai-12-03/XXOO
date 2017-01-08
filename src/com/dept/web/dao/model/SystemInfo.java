package com.dept.web.dao.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SystemInfo {
	private Map<String,Setting> map;
	
	public SystemInfo(){
		map=Collections.synchronizedMap(new HashMap<String,Setting>());
	}
	
	public void addConfig(Setting sys){
		map.put(sys.getName().replace("con_", ""), sys);
	}
	
	private Setting getConfig(String key){
	    Setting sys=(Setting)map.get(key);
		return sys;
	}
	
	public String getValue(String key){
	    Setting c=getConfig(key);
		if(c==null) return null;
		return c.getValue();
	}
	
}
