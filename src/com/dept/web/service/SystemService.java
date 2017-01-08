package com.dept.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dept.web.dao.SettingDao;
import com.dept.web.dao.model.Setting;
import com.dept.web.dao.model.SystemInfo;

@Service
public class SystemService {
	
	@Autowired
	private SettingDao settingDao;
	
	public SystemInfo getSystemInfo(){
		SystemInfo info = new SystemInfo();
		List<Setting> list = settingDao.queryAll();
		for (int i = 0; i < list.size(); i++) {
		    Setting sys = (Setting) list.get(i);
//          if(sys.getName().equals("con_weburl")){
//          sys.setValue("http://183.129.157.219:8020/sixiang");
//          }else if(sys.getName().equals("con_admurl")){
//              sys.setValue("http://183.129.157.219:8020/sixiangCms");
//          }		    
		    
		  info.addConfig(sys);
		}
		return info;
	}
}
