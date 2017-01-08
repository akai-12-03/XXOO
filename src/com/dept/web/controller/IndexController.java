package com.dept.web.controller;

import java.util.ArrayList;
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
import com.dept.web.dao.model.Borrow;
import com.dept.web.dao.model.BorrowTender;
import com.dept.web.dao.model.CalculateJson;
import com.dept.web.dao.model.PowerJson;
import com.dept.web.dao.model.Site;
import com.dept.web.dao.model.User;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserAccountSummary;
import com.dept.web.general.util.NumberUtil;
import com.dept.web.general.util.TimeUtil;
import com.dept.web.service.ArticleService;
import com.dept.web.service.BorrowService;
import com.dept.web.service.BorrowTenderService;
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
public class IndexController extends WebController{

    @Autowired
    private UserService userService;
    
    @Autowired
	private ArticleService articleService;
    
    @Autowired
    private BorrowService borrowService;
    
    @Autowired
    private UserAccountService userAccountService;
    
    @Autowired
    private BorrowTenderService borrowTenderService;
    
    
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
    @RequestMapping("index")
    public String index(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
    	
    	//首页标列表
    	List<Borrow> borList= borrowService.getIndexBorrowList(6);
    	for(Borrow bw :borList){
			Double count=Double.valueOf(bw.getAccount())-Double.valueOf(bw.getAccountYes());
			bw.setLastAccount(count);
			
			int tzcount=borrowService.getTenderList(bw.getId());
			bw.setTzcount(tzcount);
					if(bw.getVerifyTime()!=null && bw.getVerifyTime()>0){
						bw.setLastTime(TimeUtil.getEndTimeHMS(String.valueOf(bw.getVerifyTime()), 0, bw.getValidTime(), 0, 0, 0));
					}
		}
    	map.addAttribute("borList", borList);
//    	//统计数据
		UserAccountSummary uas = userAccountService.getForIndexCount();
		int zrs=userService.getAllUserCount();
		uas.setZrs(zrs);
		map.addAttribute("uas", uas);
    	
    	//查询首页推荐最新的一条数据。－－－黄金
    	Borrow b= new Borrow();
    	b.setBorrowType(1);
    	b.setIndex_status(1);
    	b.setLimits(1);
    	List<Borrow> boList = borrowService.getBorrowListByborrowTypeForIndexStatus(b);
    	map.addAttribute("boList", boList);
    	
    	//投资记录与收益
    	List<BorrowTender> tenderList= borrowTenderService.getTenderList();
    	map.addAttribute("tenderList", tenderList);
    	
    	//新手标
    	Borrow bs= new Borrow();
    	bs.setBorrowType(5);
    	bs.setIndex_status(1);
    	bs.setLimits(1);
    	List<Borrow> bosList = borrowService.getBorrowListByborrowTypeForIndexStatus(bs);
    	if(!bosList.isEmpty()){
    		map.addAttribute("bos", bosList.get(0));
    	}
    	
    	// 官方公告
		List<Article> ggList = articleService.queryArticleList("wzgg", 0, 4);
		map.addAttribute("ggList", ggList);
		
		// 媒体报道
		List<Article> zxList = articleService.queryArticleList("xwbd", 0, 5);
		map.addAttribute("zxList", zxList);
		
		
		// 合作伙伴
		List<Article> hzhbList= articleService.queryArticleList("hzhb", 0, 20);
		map.addAttribute("hzhbList", hzhbList);
		
		// banner图
		List<Article> bannerList= articleService.queryArticleList("banner", 0, 10);
		map.addAttribute("bannerList", bannerList);
		
    	User currUser = this.getCurrUser(request, response);
		map.addAttribute("currUser", currUser);
        return "index";
    }

    @RequestMapping("topbar")
    public String getTopBar(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
    	User currUser = this.getCurrUser(request, response);
		
		map.addAttribute("currUser", currUser);
		
		map.addAttribute("headnav", request.getParameter("headnav"));
		
        return "topbar";
    }
    
    @RequestMapping("foot")
    public String getFooter(ModelMap map, HttpServletRequest request) throws Exception{
        
        return "footer";
    }
    
    @RequestMapping("myhome/user-sidebar")
    public String getUserSidebar(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
    	User currUser = this.getCurrUser(request, response);
		if(currUser!=null){
			UserAccount account = userAccountService.getUserAccount(currUser.getId());
			map.addAttribute("account", account);
		}
		map.addAttribute("currUser", currUser);
		String s = request.getParameter("sidenav");
		map.addAttribute("sidenav", s);
		if(StringUtils.isNotBlank(s)){
			map.addAttribute("sidenav2", s.substring(0,1));
		}
		
        return "myhome/user-sidebar";
    }
    
    
	
	
	  /**
     * 帮助页列表
     * @Title: helplist 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @return
     * @param @throws Exception 设定文件 
     * @return String 返回类型 
     * @throws
     */
    @RequestMapping("help/detail")
    public String detail(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long did) throws Exception{
        
        Map<String, String> params = getParamMap(request);
        
        String flag = params.get("flag");
        String code = params.get("code");
        List<Site> sitelist = new ArrayList<Site>();
        
        sitelist = articleService.queryAboutUsList(code);

        map.addAttribute("sitelist", sitelist);
        
        Article art = articleService.queryArtById(did);
        
        map.addAttribute("art", art);
        
        map.addAttribute("did", did);
        
        map.addAttribute("flag", flag);
        map.addAttribute("code", code);
        //标记是否是查看公告详情
        map.addAttribute("detail","detail");
        
        return "/news/aboutus";

    }   

    /**
     * 按月配资计算
     * @Title: calculate 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @param money
     * @param @param power
     * @param @param month
     * @param @param jkmoney
     * @param @throws Exception 设定文件 
     * @return void 返回类型 
     * @throws
     */
    @RequestMapping("calculate")
    public void calculate(ModelMap map, HttpServletRequest request, HttpServletResponse response, @RequestParam double money, @RequestParam double power, @RequestParam int month, @RequestParam double jkmoney) throws Exception{
        
        double ksjjx = NumberUtil.format2(jkmoney*1.15);
        
        double kspcx = NumberUtil.format2(jkmoney*1.12);
        
        double rate = 0;
        
        if(money<10000 || money > 500000){
            
            out(response, null);
        }else{
            
            int tp;
            
            tp = (int) (power);
            
            switch (tp) {
            
            case 1:
                
                if(money>=10000 && money<=50000){
                    
                    rate = 1.6;
                    
                }else if(money>50000&&money<=100000){
                    
                    rate = 1.5;
                    
                }else if(money>100000&&money<=160000){
                    
                    rate = 1.5;
                }else if(money>160000&&money<=250000){
                    
                    rate = 1.4;
                }else if(money>250000&&money<=500000){
                    
                    rate = 1.4;
                }
                
                
                break;
                
            case 2:
                
                if(money>=10000 && money<=50000){
                    
                    rate = 1.7;
                    
                }else if(money>50000&&money<=100000){
                    
                    rate = 1.6;
                    
                }else if(money>100000&&money<=160000){
                    
                    rate = 1.6;
                }else if(money>160000&&money<=250000){
                    
                    rate = 1.5;
                }else if(money>250000&&money<=500000){
                    
                    out(response, null);
                }
                
                
                break;
                
            case 3:
                
                if(money>=10000 && money<=50000){
                    
                    rate = 1.9;
                    
                }else if(money>50000&&money<=100000){
                    
                    rate = 1.8;
                    
                }else if(money>100000&&money<=160000){
                    
                    rate = 1.7;
                }else if(money>160000&&money<=250000){
                    
                    out(response, null);
                    
                }else if(money>250000&&money<=500000){
                    
                    out(response, null);
                }
                
                
                break;
                
            case 4:
                
                if(money>=10000 && money<=50000){
                    
                    rate = 2.0;
                    
                }else if(money>50000&&money<=100000){
                    
                    rate = 1.9;
                    
                }else if(money>100000&&money<=160000){
                    
                    out(response, null);
                }else if(money>160000&&money<=250000){
                    
                    out(response, null);
                    
                }else if(money>250000&&money<=500000){
                    
                    out(response, null);
                }
                
                
                break;                
            default:
                break;
            }
            
            if(rate==0){
                
                rate = 0;
            }
            
            CalculateJson cj = new CalculateJson();
            
            cj.setCpzj(NumberUtil.ceil(jkmoney+money,0));
            cj.setKsjjx(NumberUtil.ceil(ksjjx,0));
            cj.setKspcx(NumberUtil.ceil(kspcx,0));
            cj.setJklv(rate);
            cj.setGlf(NumberUtil.ceil(jkmoney*rate*0.01*month,2));
            cj.setPower(power);
            
            out(response, cj);
        }
        

        
    }    
    
    
    
    /**
     * 按天配资计算
     * @Title: calculate 
     * @Description: TODO
     * @param @param map
     * @param @param request
     * @param @param response
     * @param @param power
     * @param @param day
     * @param @param jkmoney
     * @param @throws Exception 设定文件 
     * @return void 返回类型 
     * @throws
     */
    @RequestMapping("calculateDay")
    public void calculate(ModelMap map, HttpServletRequest request, HttpServletResponse response, @RequestParam double power, @RequestParam int day, @RequestParam double jkmoney) throws Exception{
        
        double money = NumberUtil.format2(NumberUtil.ceil(jkmoney/power,0));
        
        double ksjjx = NumberUtil.format2(jkmoney*1.15);
        
        double kspcx = NumberUtil.format2(jkmoney*1.12);
                
        CalculateJson cj = new CalculateJson();
        
        cj.setCpzj(NumberUtil.ceil(jkmoney+money,0));
        cj.setBzj(money);
        cj.setKsjjx(NumberUtil.ceil(ksjjx,0));
        cj.setKspcx(NumberUtil.ceil(kspcx,0));
        cj.setGlf(NumberUtil.ceil(jkmoney*0.002));
        cj.setPower(power);
        
        out(response, cj);
    }
    

      /**
      * 
      * @Title: anyue 
      * @Description: TODO
      * @param @param map
      * @param @param request
      * @param @param response
      * @param @param money
      * @param @throws Exception 设定文件 
      * @return void 返回类型 
      * @throws
      */
     @RequestMapping("anyue")
     public void anyue(ModelMap map, HttpServletRequest request, HttpServletResponse response, @RequestParam double money) throws Exception{
         
         PowerJson pj = new PowerJson();
         
         List<PowerJson> pjList = new ArrayList<PowerJson>();
         
         if(money<10000 || money > 500000){
             
             pj.setCpmoney(money*0);
             pj.setPower(0);
             pjList.add(pj);
             
         }else if(money>=10000 && money<=100000){
             pj.setCpmoney(money*1);
             pj.setPower(1);
             pjList.add(pj);             
             pj = new PowerJson();
             pj.setCpmoney(money*2);
             pj.setPower(2);
             pjList.add(pj);             
             pj = new PowerJson();
             pj.setCpmoney(money*3);
             pj.setPower(3);
             pjList.add(pj);
             pj = new PowerJson();
             pj.setCpmoney(money*4);
             pj.setPower(4);           
             pjList.add(pj);
         }else if(money>100000 && money<=160000){    
             pj.setCpmoney(money*1);
             pj.setPower(1);
             pjList.add(pj);
             pj = new PowerJson();
             pj.setCpmoney(money*2);
             pj.setPower(2);
             pjList.add(pj);             
             pj = new PowerJson();
             pj.setCpmoney(money*3);
             pj.setPower(3);
             pjList.add(pj);
         }else if(money>160000 && money<=250000){ 
             pj.setCpmoney(money*1);
             pj.setPower(1);
             pjList.add(pj);
             pj = new PowerJson();
             pj.setCpmoney(money*2);
             pj.setPower(2);
             pjList.add(pj);            
         }else if(money>250000 && money<=500000){ 
             pj.setCpmoney(money*1);
             pj.setPower(1);
             pjList.add(pj);           
         }
         
         out(response, pjList);
         
     }    
    
	/**
	 * 统一错误提示
	 * @return
	 */
	@RequestMapping("fail")
	public String failed(){
		return "error";
	}
	@RequestMapping("indexnav")
	public String indexnav(){
		return "nav";
	}
	/**
	 * 找不到url
	 * @return
	 */
	@RequestMapping("404")
	public String erorr_404(){
		return "404";
	}
	/**
	 * 服务器内部错误
	 * @return
	 */
	@RequestMapping("505")
	public String erorr_500(){
		return "505";
	}
	/**
	 * 拒绝访问
	 * @return
	 */
	@RequestMapping("403")
	public String erorr_403(){
		return "403";
	}
	/**
	 * 安全保障
	 */
	@RequestMapping("safe")
	public String safe(){
		return "safe";
	}
	/**
	 *   联系我们
	 */
	@RequestMapping("contactUs")
	public String contactUs(){
		return "news/contactUs";
	}
	/**
	 *   加入我们
	 */
	@RequestMapping("joinUs")
	public String joinUs(){
		return "news/joinUs";
	}
	
	/**
	 *   管理团队
	 */
	@RequestMapping("team")
	public String team(ModelMap map, HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Map<String, String> params = getParamMap(request);
		String flag ="hxtd";
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
		return "news/team";
	}
	
	/**
	 *   常见问题
	 */
	@RequestMapping("question")
	public String question(){
		return "news/question";
	}
	
	/**
	 * 合作伙伴
	 */
	@RequestMapping("partner")
	public String partner(ModelMap map, HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParamMap(request);
		String flag ="hzhb";
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
		return "news/partner";
	}
	
	
	/**
	 * 平台公告/新闻动态
	 */
	@RequestMapping("notice")
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
        return "news/notice";
	}
	
	/**
	 * 关于我们
	 * 
	 * @Description:
	 * @param: @param map
	 * @param: @param request
	 * @param: @param response
	 * @param: @param flag
	 * @param: @return
	 * @param: @throws Exception
	 * @return: String
	 * @throws
	 */
	@RequestMapping("news/aboutus")
	public String aboutus(ModelMap map, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//获取提交过来的flag
		/*Map<String, String> params = getParamMap(request);
		String flag = params.get("flag");
		//获取提交过来的code
		String code = params.get("code");
		//如果flag为空，表示是从关于我们 点击进入的，设置flag的默认值为gsjs
		if(flag == null || flag.equals("")) {
			flag = "gsjs";
		}
		map.put("code", code);
		map.put("flag", flag);
		
		//显示左边的列表
		List<Site> sitelist = new ArrayList<Site>();
		sitelist = articleService.queryAboutUsList(code);
		map.put("sitelist", sitelist);
		//显示右边的内容
		PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
        populate(pageRequest, request);
        pageRequest.setPageSize(10);
        params.put("code", code);
        params.put("flag", flag);
        if(StringUtil.isNotEmpty(params.get("page"))){
            pageRequest.setPageNumber(Integer.valueOf(params.get("page")));
        }
        pageRequest.setFilters(params);
        Page<Article> artpage = articleService.queryByflag(pageRequest);
        if(!flag.equals("ggfb")){
            map.addAttribute("art", artpage.getResult().get(0));
        }else{
            map.addAttribute("artpage", artpage);
            map.addAttribute("page",pageRequest.getPageNumber());
            map.addAttribute("totalPage", PageUtils.computeLastPageNumber(artpage.getTotalCount(), artpage.getPageSize()));
        }    */  
		return "news/aboutus";
	}
	
	/**
	 * 关于我们所有动态的详情页
	 */
	  @RequestMapping("news/aboutus_detail")
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
	        
	        return "/news/aboutus_detail";

	    } 
	  
	  /**
		 * 关于我们-平台规则-网站规则
		 * 
		 * @Description:
		 * @param: @param map
		 * @param: @param request
		 * @param: @param response
		 * @param: @param flag
		 * @param: @return
		 * @param: @throws Exception
		 * @return: String
		 * @throws
		 */
		@RequestMapping("news/wzgz")
		public String wzgz(ModelMap map, HttpServletRequest request,HttpServletResponse response) throws Exception {
			return "news/wzgz";
		}
		
	  /**
		 * 关于我们-平台规则-协议范本
		 * 
		 * @Description:
		 * @param: @param map
		 * @param: @param request
		 * @param: @param response
		 * @param: @param flag
		 * @param: @return
		 * @version 1.0      
		 * @created 2016-3-1 
		 * @throws
		 */
		@RequestMapping("news/ptgz")
		public String aboutusptgz(ModelMap map, HttpServletRequest request,HttpServletResponse response) throws Exception {
			Map<String, String> params = getParamMap(request);
			//显示大菜单
			List<Site> sitelist = articleService.queryAboutUsList("ptgz");
			map.addAttribute("sitelist", sitelist);
			String flag = params.get("flag");
			map.put("code", "ptgz");
			map.put("flag", flag);
			
			//显示菜单下的内容
			PageRequest<Map<String, String>> pageRequest = new PageRequest<Map<String, String>>();
			populate(pageRequest, request);
			pageRequest.setPageSize(10);
			params.put("code", "ptgz");
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
			return "news/ptgz";
		}
		
		/**      
		 * @desc 关于我们-平台规则-协议范本详情
		 * @version 1.0      
		 * @created 2016-3-1 
		 * @mod 修改描述:
		 * @modAuthor 修改人:
		 */
		@RequestMapping("about/ptgzdetail")
		public String ptgzdetail(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long id) throws Exception{
			Map<String, String> params = getParamMap(request);
			List<Site> sitelist = articleService.queryAboutUsList("ptgg");
			map.addAttribute("sitelist", sitelist);
			Article art = articleService.queryArtById(id);
			map.addAttribute("art", art);
			map.addAttribute("flag", params.get("flag"));
			return "news/ptgz_detail";
		}
		
		/**  关于我们-平台公告    
		 * @desc 
		 * @version 1.0      
		 * @created 2016-3-1 
		 * @mod 修改描述:
		 * @modAuthor 修改人:
		 */
		@RequestMapping("about/ptgg")
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
			return "news/notice";
		}
	   
	/**      
	 * @desc 关于我们-平台公告详情
	 * @version 1.0      
	 * @created 2016-3-1 
	 * @mod 修改描述:
	 * @modAuthor 修改人:
	 */
	@RequestMapping("about/ptggdetail")
	public String arDetail(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long id) throws Exception{
		Map<String, String> params = getParamMap(request);
		List<Site> sitelist = articleService.queryAboutUsList("ptgg");
		map.addAttribute("sitelist", sitelist);
		Article art = articleService.queryArtById(id);
		map.addAttribute("art", art);
		map.addAttribute("flag", params.get("flag"));
		return "news/notice_detail";
	}
	
	/**  关于我们-行业资讯    
	 * @desc 
	 * @version 1.0      
	 * @created 2016-3-1 
	 * @mod 修改描述:
	 * @modAuthor 修改人:
	 */
	@RequestMapping("about/hyzx")
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
		return "news/zixun";
	}
	
	/**      
	 * @desc 关于我们-行业资讯 详情
	 * @version 1.0      
	 * @created 2016-3-1 
	 * @mod 修改描述:
	 * @modAuthor 修改人:
	 */
	@RequestMapping("about/hyzxdetail")
	public String hyzxdetail(ModelMap map,HttpServletRequest request, HttpServletResponse response, @RequestParam long id) throws Exception{
		Map<String, String> params = getParamMap(request);
		List<Site> sitelist = articleService.queryAboutUsList("hyzx");
		map.addAttribute("sitelist", sitelist);
		Article art = articleService.queryArtById(id);
		map.addAttribute("art", art);
		map.addAttribute("flag", params.get("flag"));
		return "news/zixun_detail";
	}
	/**
	 * 跳转到帮助中心
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping("about/help")
	public String help(ModelMap map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		return "news/helpCenter";
	}
	
	/**
	 * 新手指南
	 */
	  @RequestMapping("newGuide")
	  public String newGuide(){
		  return "newGuide";
	  }
	  
	  
	
}