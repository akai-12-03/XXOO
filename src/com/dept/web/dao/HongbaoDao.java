package com.dept.web.dao;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Hongbao;
import com.dept.web.dao.model.HongbaoLog;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class HongbaoDao  extends IbatisBaseDaoImpl<Hongbao, Long> {
	
	private static final String NAME_SPACE_HONGBAO = "Hongbao";
	
	public Hongbao queryHongbaoById(Long hongbaoId){
		return getObj(NAME_SPACE_HONGBAO, hongbaoId, "BYHONGBAOID");
	}
	
	public Hongbao queryHongbaoForAccount(Long userId){
		return getObj(NAME_SPACE_HONGBAO, userId, "HONGBAOFORACCOUNT");
	}
	
	public Page<Hongbao> queryHongbaoByUserId(PageRequest<Map<String, String>> map){
		return pageQuery(NAME_SPACE_HONGBAO, map, "FIND_HONGBAOLOG_BY_USERID");
	}
	/**
	 * 更新红包状态
	 * @param 
	 * @return
	 */
	public int updateHongbao(Hongbao hongbao){
		return update(NAME_SPACE_HONGBAO, hongbao, "UPSTATUS");
	}
	/**
	 * 添加红包记录
	 * @param 
	 * @return
	 */
	public Long insertHonbao(Hongbao hongbao){
		
		return (Long) save(NAME_SPACE_HONGBAO, hongbao, "HONGBAO");
	}
	/**
	 * 根据ID查找未使用的红包总金额
	 * @param 
	 * @return
	 */
	public double getSumHongbaoById(Long userId){
		Object obj=getObjs(NAME_SPACE_HONGBAO, userId, "USERID");
		if(obj==null){
			return 0;
		}else{
		  return Double.parseDouble(obj.toString());
		}
	}
	
	   
	/**      
	 * @desc 用途描述: 查询红包奖励的金额
	 * @return 返回说明:
	 * @exception 内部异常说明:
	 * @throws 抛出异常说明:
	 * @author gwx
	 * @version 1.0      
	 * @created 2016-3-8 下午3:15:13 
	 * @mod 修改描述:
	 * @modAuthor 修改人:
	    
	 */
	public double queryMoneyByTuiguang(long userId){
		Object obj=getObjs(NAME_SPACE_HONGBAO, userId, "TUIGUANGMONEY");
		if(obj==null){
			return 0;
		}else{
		  return Double.parseDouble(obj.toString());
		}
	}
	
	public List<Hongbao>  queryListHongbaoByIDAndStatus(Map map){
  		
  		return getObjList(NAME_SPACE_HONGBAO, map, "STATUS");
  	}

	public Integer queryCountHongbaoLogByIDAndStatus(Map map){
  		
  		return getSelectCount(NAME_SPACE_HONGBAO, map, "IDSTATUS");
  	}

}
