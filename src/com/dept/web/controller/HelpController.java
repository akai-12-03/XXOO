package com.dept.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dept.web.dao.model.Article;
import com.dept.web.dao.model.Site;
import com.dept.web.service.ArticleService;
import com.sendinfo.common.lang.StringUtil;
import com.sendinfo.xspring.ibatis.page.Page;
import com.sendinfo.xspring.ibatis.page.PageRequest;
import com.sendinfo.xspring.ibatis.page.PageUtils;

/**
 * 首页
 * @ClassName:     IndexController.java
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-9-23 下午2:11:50
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class HelpController extends WebController{
	
	@Autowired
	private ArticleService articleService;
	/**
	 * 关于我们
	 * @Description:  
	 * @param:        @param map
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param flag
	 * @param:        @return
	 * @param:        @throws Exception   
	 * @return:       String   
	 * @throws
	 */
	@RequestMapping("help/helplist")
	public String helplist(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		List<Site> sitelist = articleService.queryAboutUsList("gywm");

		map.addAttribute("sitelist", sitelist);
		
		
		PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
		populate(pageRequest, request);
		pageRequest.setPageSize(10);
		Map<String, String> params = getParamMap(request);
		
		params.put("code", "gywm");
		
		if(StringUtil.isNotEmpty(params.get("page"))){
			
			pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
		}
		
		pageRequest.setFilters(params);

		Page<Article> artpage = articleService.queryByflag(pageRequest);
		
		if(artpage.getResult().size()==1 && pageRequest.getPageNumber()==0){
			
			map.addAttribute("art", artpage.getResult().get(0));
			map.addAttribute("flag", params.get("flag"));
			
			return "news/xwgg";
			
		}else{
			
			map.addAttribute("artpage", artpage);
			
			map.addAttribute("flag", params.get("flag"));
			
			map.addAttribute("page",pageRequest.getPageNumber());
			
			map.addAttribute("totalPage", PageUtils.computeLastPageNumber(artpage.getTotalCount(), artpage.getPageSize()));
			
			return "news/xwgg";
		}
	}
	/**
	 * 关于我们详情
	 * @Description:  
	 * @param:        @param map
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param flag
	 * @param:        @return
	 * @param:        @throws Exception   
	 * @return:       String   
	 * @throws
	 */
	@RequestMapping("help/aboutus/detail")
	public String aboutusDetail(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long id) throws Exception{
		Map<String, String> params = getParamMap(request);
		
		List<Site> sitelist = articleService.queryAboutUsList("gywm");

		map.addAttribute("sitelist", sitelist);
		
		Article art = articleService.queryArtById(id);
		
		map.addAttribute("art", art);
		map.addAttribute("flag", params.get("flag"));
		
		return "news/helps";

	}	
	
	
	
	
	/**
	 * 关于我们 修改版
	 * 新手指引
	 */
	@RequestMapping("help/rzwt")
	public String xszy_rzwt(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		return "news/xszy1";
	}
	@RequestMapping("help/lcwt")
	public String xszy_lcwt(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		return "news/xszy2";
	}
	@RequestMapping("help/zhwt")
	public String xszy_zhwt(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		return "news/xszy3";
	}
	
	/**
	 * 关于我们
	 */
	@RequestMapping("help/ynjs")
	public String gywm_ynjs(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		return "news/gywm1";
	}
	@RequestMapping("help/hxtd")
	public String gywm_hxtd(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		return "news/gywm2";
	}
	
	/**
	 * 用户保障
	 */
	@RequestMapping("help/fwtk")
	public String yhbz_fwtk(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		return "news/yhbz1";
	}
	@RequestMapping("help/ystk")
	public String yhbz_ystk(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		return "news/yhbz2";
	}
	@RequestMapping("help/aqbz")
	public String yhbz_aqbz(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		return "news/yhbz3";
	}
	@RequestMapping("help/flbz")
	public String yhbz_flbz(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		return "news/yhbz4";
	}
	@RequestMapping("help/jsbz")
	public String yhbz_jsbz(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		return "news/yhbz5";
	}
	
	/**
	 * 网站综合
	 */
	@RequestMapping("help/ynys")
	public String wzzh_ynys(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		return "news/wzzh1";
	}
	@RequestMapping("help/xmys")
	public String wzzh_xmys(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		return "news/wzzh2";
	}
	@RequestMapping("help/jrwm")
	public String wzzh_jrwm(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		return "news/wzzh3";
	}
	@RequestMapping("help/xwgg")
	public String xwgg(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		return "news/xwgg";
	}
	
	
	 @RequestMapping("help/helps-sidebar")
	    public String getHelpsSidebar(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
			String s = request.getParameter("zc");
			map.addAttribute("zc", s);
			if(StringUtils.isNotBlank(s)){
				map.addAttribute("zc2", s.substring(0,1));
			}
			
	        return "news/helps-siderbar";
	    }
}