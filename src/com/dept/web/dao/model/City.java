package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 
 * 
 * @ClassName:     City
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月14日 下午9:55:37 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class City extends BaseEntity{

    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -5176910651142223772L;

    private Long cityId;
    
    private String city;
    
    private Long parentId;

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    
}
