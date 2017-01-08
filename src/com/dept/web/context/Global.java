package com.dept.web.context;

import com.dept.web.dao.model.SystemInfo;

/**
 * 
 * @ClassName:     Global.java
 * @Description:   TODO
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-8-18 下午7:28:46
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class Global {
	
	public static SystemInfo SYSTEMINFO;
	
	public static String[] SYSTEMNAME=new String[]{"webname","meta_keywords","meta_description",
		"beian","copyright","fuwutel","address","weburl","theme_dir", "webid", "protocol_url"};
	

	
	public static String getValue(String key){
		Object o=null;
		if(SYSTEMINFO!=null){
			o=SYSTEMINFO.getValue(key);
		}
		if(o==null){
			return "";
		}
		return o.toString();
	}
	
}
