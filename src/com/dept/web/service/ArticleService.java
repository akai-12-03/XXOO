package com.dept.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dept.web.dao.ArticleDao;
import com.dept.web.dao.SiteDao;
import com.dept.web.dao.model.Article;
import com.dept.web.dao.model.Site;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;

@Service
@Transactional(rollbackFor=Exception.class)
public class ArticleService {
	@Autowired
	private SiteDao siteDao;
	@Autowired
	private ArticleDao articleDao;
	/**
	 * 
	 * @Description:  关于我们的列表
	 * @param:        @param flag
	 * @param:        @param code
	 * @param:        @return   
	 * @return:       List<Article>   
	 * @throws
	 */
	public List<Site> queryAboutUsList(String code){
		return siteDao.queryAboutUsList(code);
	}
	
	/**
	 * 
	 * @Description:  关于我们详情
	 * @param:        @param flag
	 * @param:        @return   
	 * @return:       Article   
	 * @throws
	 */
	public Page<Article> queryByflag(PageRequest<Map<String, String>> pageRequest){
		return articleDao.queryAboutUsDetail(pageRequest);
		
	}

	/**
	 * 
	 * @Description:  TODO
	 * @param:        @param id
	 * @param:        @return   
	 * @return:       Article   
	 * @throws
	 */
	public Article queryArtById(long id){
		
		return articleDao.queryById(id);
		
	}
	
	/**
	 * 
	 * @Description:  TODO
	 * @param:        @param site
	 * @param:        @param start
	 * @param:        @param end
	 * @param:        @return   
	 * @return:       List<Article>   
	 * @throws
	 */
	public List<Article> queryArticleList(String nid, int start, int end){
		
		Map<String, Object> filters = new HashMap<String, Object>();
		
		filters.put("nid", nid);
		filters.put("start", start);
		filters.put("end", end);
		return articleDao.queryArticleList(filters);
	}
	
	public List<Article> queryArticleBbList(String nid, int start, int end){
		
		Map<String, Object> filters = new HashMap<String, Object>();
		
		filters.put("nid", nid);
		filters.put("start", start);
		filters.put("end", end);
		return articleDao.queryArticleBbList(filters);
	}
	
}
