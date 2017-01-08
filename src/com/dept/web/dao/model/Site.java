package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 
 * 
 * @ClassName:     Site
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月16日 下午2:56:21 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class Site extends BaseEntity{


    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -6961545173581272725L;

    /**
     * site.code
     * 
     */
    private String code;
    
    private String nid;

    /**
     * site.name
     * 
     */
    private String name;

    /**
     * site.pid
     * 
     */
    private Long pid;

    /**
     * site.order
     * 
     */
    private Integer sOrder;

    /**
     * site.status
     * 
     */
    private Integer status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getsOrder() {
        return sOrder;
    }

    public void setsOrder(Integer sOrder) {
        this.sOrder = sOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }
    
    

}