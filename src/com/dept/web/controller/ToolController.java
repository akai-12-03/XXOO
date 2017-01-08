package com.dept.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

/**
 * 工具相关
 * @ClassName:     ToolController.java
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014-9-2 下午3:22:01
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class ToolController extends WebController{
	
	/**
	 * 用户中心首页读取头像
	 * @Description:  
	 * @param:        @param map
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param status
	 * @param:        @return
	 * @param:        @throws NumberFormatException
	 * @param:        @throws Exception   
	 * @return:       String   
	 * @throws
	 */
	@RequestMapping("image")
	public String image(ModelMap map,HttpServletRequest request, HttpServletResponse response, SessionStatus status, @RequestParam long userid) throws NumberFormatException, Exception{
		
		 String size = request.getParameter("size");
		 String sep=File.separator;
	
		 String[] sizes={"big","middle","small"};
		 List<String> sizelist= Arrays.asList(sizes);
		 size=sizelist.contains(size)?size:"big";
		 
		 String url="data"+sep+"avatar"+sep+userid+"_avatar_"+size+".jpg";
		 
		 String contextPath="";
		 url=contextPath+url;
		 File avatarFile=new File(url); 
		 if(!avatarFile.exists()){
			 url=contextPath+"data"+sep+"images"+sep+"avatar"+sep+"default_"+size+".png";
		 }
		 
		 cteateImg(url, response);
		return null;
	}

	
	
	/**
	 * 以图片流形式输出
	 * @param url
	 * @throws IOException
	 */
	private void cteateImg(String url, HttpServletResponse response) throws IOException{
		response.setHeader("Pragma","No-cache");  
		response.setHeader("Cache-Control","no-cache");  
		response.setDateHeader("Expires", 0);  
		OutputStream output = response.getOutputStream();// 得到输出流   
		if(url.toLowerCase().endsWith(".jpg")){
			 //表明生成的响应是图片  
			response.setContentType("image/jpeg");  
		}else if(url.toLowerCase().endsWith(".gif")){
			response.setContentType("image/gif");  
		}
		InputStream imageIn = new FileInputStream(new File(url));  
        BufferedInputStream bis = new BufferedInputStream(imageIn);// 输入缓冲流  
        BufferedOutputStream bos = new BufferedOutputStream(output);// 输出缓冲流  
        byte data[] = new byte[4096];// 缓冲字节数  
        int size = 0;  
        size = bis.read(data);  
        while (size != -1) {  
            bos.write(data, 0, size);  
            size = bis.read(data);  
        }  
        bis.close();
        bos.flush();// 清空输出缓冲流  
        bos.close();  
        output.flush();
        output.close();
	}
}