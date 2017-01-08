package com.dept.web.controller.weixin;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dept.web.controller.WebController;
import com.dept.web.dao.model.Article;
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.Site;
import com.dept.web.dao.model.User;
import com.dept.web.general.util.TimeUtil;
import com.dept.web.service.ArticleService;
import com.dept.web.service.BorrowService;
import com.dept.web.service.FinancingService;
import com.dept.web.service.HelpService;
import com.dept.web.service.PlanRecordService;
import com.dept.web.service.SystemService;
import com.dept.web.service.UserAccountService;
import com.dept.web.service.UserService;
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
public class WebAppIndexController extends WebController{

    @Autowired
    private HelpService helpService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
	private ArticleService articleService;
    
    @Autowired
    private SystemService systemService;
    
    @Autowired
    private BorrowService borrowService;
    
    @Autowired
    private PlanRecordService planRecordService;
    
    @Autowired
    private UserAccountService userAccountService;
    
    @Autowired
    private FinancingService financingService;
    
    /**
     * 首页
     * @Description:  
     * @param:        @param map
     * @param:        @param request
     * @param:        @param response
     * @param:        @return
     * @param:        @throws NumberFormatException
     * @param:        @throws Exception   
     * @return:       String   
     * @throws
     */
    @RequestMapping("webapp/index")
    public String index(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
    	//查询首页推荐最新的一条数据。－－－黄金
    	Borrow b= new Borrow();
    	b.setStatus(1);
    	b.setIndex_status(1);
    	b.setLimits(1);
    	List<Borrow> boList = borrowService.getBorrowListBorrowIndex(b);
    	if(boList.size()>0){
    		map.addAttribute("boList", boList);
    	}else{
    		//查询首页推荐最新的两条数据，－－－借贷
        	Borrow bs= new Borrow();
        	bs.setStatus(1);
        	bs.setIndex_status(0);
        	bs.setLimits(1);
        	List<Borrow> bosList = borrowService.getBorrowListBorrowIndex(bs);
        	map.addAttribute("boList", bosList);
    	}
        return "qgfwebapp/index";
    }
    
    @RequestMapping("webapp/rightmenu")
	public String rightmenu(ModelMap map,HttpServletRequest request, HttpServletResponse response ) throws Exception{
    	User currUser = this.getCurrUser(request, response);
		
		map.addAttribute("currUser", currUser);
		return "qgfwebapp/rightmenu";

	}
    @RequestMapping("webapp/weixinnvg")
   	public String weixinnvg(ModelMap map,HttpServletRequest request, HttpServletResponse response ) throws Exception{
       	User currUser = this.getCurrUser(request, response);
   		
   		map.addAttribute("currUser", currUser);
   		return "webapp/nvg";

   	}
    /**
     * 更多
     * @param map
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("webapp/more")
   	public String more(ModelMap map,HttpServletRequest request, HttpServletResponse response ) throws Exception{
       	User currUser = this.getCurrUser(request, response);
   		
   		map.addAttribute("currUser", currUser);
   		return "webapp/more";

   	}
    
    
    /**
	 * 平台公告/新闻动态
	 */
	@RequestMapping("webapp/notice")
	public String notice(ModelMap map, HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		String flag = params.get("flag");
		if(StringUtil.isEmpty(flag)){
			flag="wzgg";
		}
		String code ="gywm";
		//获取提交过来的code
		map.put("flag", flag);
		PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
        populate(pageRequest, request);
        pageRequest.setPageSize(10);
        params.put("flag", flag);
        params.put("code", code);
        if(StringUtil.isNotEmpty(params.get("page"))){
            pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
        }
        pageRequest.setFilters(params);
    Page<Article> artpage = articleService.queryByflag(pageRequest);
        map.addAttribute("artpage", artpage);
        map.addAttribute("page",pageRequest.getPageNumber());
        map.addAttribute("totalPage", PageUtils.computeLastPageNumber(artpage.getTotalCount(), artpage.getPageSize()));
        map.addAttribute("flag", flag);
        return "webapp/media";
	}
	
	
	/**
	 * 关于我们所有动态的详情页
	 */
	  @RequestMapping("webapp/aboutus_detail")
	    public String aboutus_detail(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long did) throws Exception{
	        
	        Map<String, String> params = getParamMap(request);
	        
	        String flag = params.get("flag");
	        String code = "gywm";
	        Article art = articleService.queryArtById(did);
	        
	        map.addAttribute("art", art);
	        
	        map.addAttribute("did", did);
	        
	        map.addAttribute("flag", flag);
	        map.addAttribute("code", code);
	        //标记是否是查看公告详情
	        map.addAttribute("detail","detail");
	        
	        return "webapp/media-detail";

	    }  
	  
	  
	  /**
	     * 常见问题
	     * @param map
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    @RequestMapping("webapp/question")
	   	public String question(ModelMap map,HttpServletRequest request, HttpServletResponse response ) throws Exception{
	   		return "qgfwebapp/commonProblem";

	   	}
	    /**
	     * 关于我们
	     * @param map
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    @RequestMapping("webapp/aboutUs")
	   	public String aboutUs(ModelMap map,HttpServletRequest request, HttpServletResponse response ) throws Exception{
	   		return "qgfwebapp/aboutUs";

	   	}
	    
	    
	    /**  关于我们-行业资讯    
		 * @desc 
		 * @version 1.0      
		 * @created 2016-3-1 
		 * @mod 修改描述:
		 * @modAuthor 修改人:
		 */
		@RequestMapping("webapp/about/hyzx")
		public String hyzx(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
			Map<String, String> params = getParamMap(request);
			//显示大菜单
			List<Site> sitelist = articleService.queryAboutUsList("hyzx");
			map.addAttribute("sitelist", sitelist);
			String flag = params.get("flag");
			map.put("code", "hyzx");
			map.put("flag", flag);
			
			//显示菜单下的内容
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);
			params.put("code", "hyzx");
			params.put("flag", flag);
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			pageRequest.setFilters(params);

			Page<Article> artpage = articleService.queryByflag(pageRequest);
			
			 	if(artpage.getResult().size()>0){
		            map.addAttribute("siteName", artpage.getResult().get(0).getSitename());
		        }
				map.addAttribute("artpage", artpage);
				
				map.addAttribute("flag", params.get("flag"));
				
				map.addAttribute("page",pageRequest.getPageNumber());
				
				map.addAttribute("totalPage", PageUtils.computeLastPageNumber(artpage.getTotalCount(), artpage.getPageSize()));
			return "qgfwebapp/information";
		}
		
		/**      
		 * @desc 关于我们-行业资讯 详情
		 * @version 1.0      
		 * @created 2016-3-1 
		 * @mod 修改描述:
		 * @modAuthor 修改人:
		 */
		@RequestMapping("webapp/about/hyzxdetail")
		public String hyzxdetail(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long id) throws Exception{
			Map<String, String> params = getParamMap(request);
			List<Site> sitelist = articleService.queryAboutUsList("hyzx");
			map.addAttribute("sitelist", sitelist);
			Article art = articleService.queryArtById(id);
			map.addAttribute("art", art);
			map.addAttribute("flag", params.get("flag"));
			return "qgfwebapp/media";
		}
		
		
		/**  关于我们-平台公告    
		 * @desc 
		 * @version 1.0      
		 * @created 2016-3-1 
		 * @mod 修改描述:
		 * @modAuthor 修改人:
		 */
		@RequestMapping("webapp/about/ptgg")
		public String news(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
			Map<String, String> params = getParamMap(request);
			//显示大菜单
			List<Site> sitelist = articleService.queryAboutUsList("ptgg");
			map.addAttribute("sitelist", sitelist);
			String flag = params.get("flag");
			map.put("code", "ptgg");
			map.put("flag", flag);
			
			//显示菜单下的内容
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);
			params.put("code", "ptgg");
			params.put("flag", flag);
			if(StringUtil.isNotEmpty(params.get("page"))){
				pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
			}
			pageRequest.setFilters(params);

			Page<Article> artpage = articleService.queryByflag(pageRequest);
			
			 	if(artpage.getResult().size()>0){
		            map.addAttribute("siteName", artpage.getResult().get(0).getSitename());
		        }
				map.addAttribute("artpage", artpage);
				
				map.addAttribute("flag", params.get("flag"));
				
				map.addAttribute("page",pageRequest.getPageNumber());
				
				map.addAttribute("totalPage", PageUtils.computeLastPageNumber(artpage.getTotalCount(), artpage.getPageSize()));
			return "qgfwebapp/ptgg";
		}
	   
	/**      
	 * @desc 关于我们-平台公告详情
	 * @version 1.0      
	 * @created 2016-3-1 
	 * @mod 修改描述:
	 * @modAuthor 修改人:
	 */
	@RequestMapping("webapp/about/ptggdetail")
	public String arDetail(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long id) throws Exception{
		Map<String, String> params = getParamMap(request);
		List<Site> sitelist = articleService.queryAboutUsList("ptgg");
		map.addAttribute("sitelist", sitelist);
		Article art = articleService.queryArtById(id);
		map.addAttribute("art", art);
		map.addAttribute("flag", params.get("flag"));
		return "qgfwebapp/media";
	}
	    
}