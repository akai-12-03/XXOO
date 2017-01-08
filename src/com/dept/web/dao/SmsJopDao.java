package com.dept.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.SmsJop;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;

@Repository
public class SmsJopDao  extends IbatisBaseDaoImpl<SmsJop, Long>{
	public static final String NAME_SPACE_SMSJOP = "SmsJop";
	
	
	public Long  insertSmsJop(SmsJop smsJop){
		return (Long) save(smsJop);
	}
	
	public List selectSmsJopByAddtime(SmsJop smsjop){
		return getObjList(NAME_SPACE_SMSJOP, smsjop, "SMS_JOP_BY_ADDTIME");
	}
	
	public SmsJop selectSmsJopById(Long id){
		return getObj(NAME_SPACE_SMSJOP, id, "SMS_JOP_BY_ID");
	}
	public SmsJop selectSmsJopByPhone(String phone){
		return getObj(NAME_SPACE_SMSJOP, phone, "SMS_JOP_BY_PHONE");
	}
	
	
	public void  deleteSmsJop(Long id){
		delete(NAME_SPACE_SMSJOP, id, "SMS_JOP_DEL");
	}
}
