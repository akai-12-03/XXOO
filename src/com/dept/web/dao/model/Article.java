package com.dept.web.dao.model;

import com.sendinfo.xspring.ibatis.base.BaseEntity;

/**
 * 
 * 
 * @ClassName:     Article
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年4月16日 下午2:58:43 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class Article extends BaseEntity{

	/** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */ 
    private static final long serialVersionUID = -5796538462751358225L;

    /**
     * article.site_id
     * 
     */
    private Long siteId;

    /**
     * article.name
     * 
     */
    private String name;

    /**
     * article.littitle
     * 
     */
    private String littitle;

    /**
     * article.status
     * 
     */
    private Integer status;

    /**
     * article.litpic
     * 
     */
    private String litpic;

    /**
     * article.flag
     * 
     */
    private String flag;

    /**
     * article.source
     * 
     */
    private String source;

    /**
     * article.publish
     * 
     */
    private String publish;

    /**
     * article.is_jump
     * 
     */
    private String isJump;

    /**
     * article.author
     * 
     */
    private String author;

    /**
     * article.jumpurl
     * 
     */
    private String jumpurl;

    /**
     * article.summary
     * 
     */
    private String summary;

    /**
     * article.order
     * 
     */
    private Integer aOrder;

    /**
     * article.hits
     * 
     */
    private Integer hits;

    /**
     * article.comment
     * 
     */
    private Integer comment;

    /**
     * article.is_comment
     * 
     */
    private String isComment;

    /**
     * article.user_id
     * 
     */
    private Long userId;

    /**
     * article.addtime
     * 
     */
    private Long createdAt;

    /**
     * article.addip
     * 
     */
    private Long updatedAt;
    
    
    private String content;

    private String sitename;

    public Long getSiteId() {
        return siteId;
    }


    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getLittitle() {
        return littitle;
    }


    public void setLittitle(String littitle) {
        this.littitle = littitle;
    }


    public Integer getStatus() {
        return status;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getLitpic() {
        return litpic;
    }


    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }


    public String getFlag() {
        return flag;
    }


    public void setFlag(String flag) {
        this.flag = flag;
    }


    public String getSource() {
        return source;
    }


    public void setSource(String source) {
        this.source = source;
    }


    public String getPublish() {
        return publish;
    }


    public void setPublish(String publish) {
        this.publish = publish;
    }


    public String getIsJump() {
        return isJump;
    }


    public void setIsJump(String isJump) {
        this.isJump = isJump;
    }


    public String getAuthor() {
        return author;
    }


    public void setAuthor(String author) {
        this.author = author;
    }


    public String getJumpurl() {
        return jumpurl;
    }


    public void setJumpurl(String jumpurl) {
        this.jumpurl = jumpurl;
    }


    public String getSummary() {
        return summary;
    }


    public void setSummary(String summary) {
        this.summary = summary;
    }


    public Integer getaOrder() {
        return aOrder;
    }


    public void setaOrder(Integer aOrder) {
        this.aOrder = aOrder;
    }


    public Integer getHits() {
        return hits;
    }


    public void setHits(Integer hits) {
        this.hits = hits;
    }


    public Integer getComment() {
        return comment;
    }


    public void setComment(Integer comment) {
        this.comment = comment;
    }


    public String getIsComment() {
        return isComment;
    }


    public void setIsComment(String isComment) {
        this.isComment = isComment;
    }


    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }


    public Long getUpdatedAt() {
        return updatedAt;
    }


    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


	public String getSitename() {
		return sitename;
	}


	public void setSitename(String sitename) {
		this.sitename = sitename;
	}
    
    
}