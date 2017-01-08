package com.dept.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dept.web.dao.model.Article;
import com.sendinfo.xspring.ibatis.IbatisBaseDaoImpl;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Repository
public class ArticleDao extends IbatisBaseDaoImpl<Article, Long> {

	private static final String NAME_SPACE_ARTICLE = "Article";

	/**
	 * 
	 * @Description: 关于我们详情列表
	 * @param: @param pageRequest
	 * @param: @return
	 * @return: Page<Article>
	 * @throws
	 */
	public Page<Article> queryAboutUsDetail(
			PageRequest<Map<String, String>> pageRequest) {

		return pageQuery(NAME_SPACE_ARTICLE, pageRequest, "ABOUTUS_DETAIL");
	}

	public Article queryById(long id) {
		return getObj(NAME_SPACE_ARTICLE, id, "ID");
	}
	
	/**
	 * 
	 * @Description:  查询网站公告
	 * @param:        @param fiters
	 * @param:        @return   
	 * @return:       List<Article>   
	 * @throws
	 */
	public List<Article> queryArticleList(Map<String,Object> fiters) {
		
		return getObjList(NAME_SPACE_ARTICLE, fiters, "INDEX");
	}
	public List<Article> queryArticleBbList(Map<String,Object> fiters) {
		
		return getObjList(NAME_SPACE_ARTICLE, fiters, "INDEX");
	}

}
