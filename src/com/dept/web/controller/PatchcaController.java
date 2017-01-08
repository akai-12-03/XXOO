package com.dept.web.controller;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.patchca.color.ColorFactory;
import org.patchca.filter.predefined.DoubleRippleFilterFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * patchca生成多彩验证码
 * 
 * @ClassName:     PatchcaController
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014年11月10日 下午9:03:48 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
@Controller
public class PatchcaController {
    private static ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
    private static Random random = new Random();
    static {
//        cs.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
        cs.setColorFactory(new ColorFactory() {
            @Override
            public Color getColor(int x) {
                int[] c = new int[3];
                int i = random.nextInt(c.length);
                for (int fi = 0; fi < c.length; fi++) {
                    if (fi == i) {
                        c[fi] = random.nextInt(71);
                    } else {
                        c[fi] = random.nextInt(256);
                    }
                }
                return new Color(c[0], c[1], c[2]);
            }
        });
        RandomWordFactory wf = new RandomWordFactory();
        wf.setCharacters("123456789");
        wf.setMaxLength(4);
        wf.setMinLength(4);
        cs.setWordFactory(wf);
    }
    
    /**
     * 读取图片验证码
     * @Title: crimg 
     * @Description: TODO
     * @param @param request
     * @param @param response
     * @param @throws IOException 设定文件 
     * @return void 返回类型 
     * @throws
     */
    @RequestMapping("/pcrimg")
    public void crimg(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        switch (random.nextInt(5)) {
//            case 0:
//                cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
//                break;
//            case 1:
//                cs.setFilterFactory(new MarbleRippleFilterFactory());
//                break;
//            case 2:
//                cs.setFilterFactory(new DoubleRippleFilterFactory());
//                break;
//            case 3:
//                cs.setFilterFactory(new WobbleRippleFilterFactory());
//                break;
//            case 4:
//                cs.setFilterFactory(new DiffuseRippleFilterFactory());
//                break;
//        }
        
    	cs.setFilterFactory(new DoubleRippleFilterFactory());
        
        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession();
        }
        setResponseHeaders(response);
        String token = EncoderHelper.getChallangeAndWriteImage(cs, "png", response.getOutputStream());
        session.setAttribute("captchaToken", token);
    }
    protected void setResponseHeaders(HttpServletResponse response) {
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        long time = System.currentTimeMillis();
        response.setDateHeader("Last-Modified", time);
        response.setDateHeader("Date", time);
        response.setDateHeader("Expires", time);
    }
}
