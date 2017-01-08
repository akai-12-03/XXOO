package com.dept.web.controller.thrid.techSdk;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dept.web.controller.WebController;
import com.dept.web.dao.model.User;
import com.timevale.esign.sdk.tech.bean.result.AddSealResult;
import com.timevale.esign.sdk.tech.bean.result.Result;
import com.timevale.esign.sdk.tech.bean.result.SignDataResult;
import com.timevale.esign.sdk.tech.bean.seal.OrganizeTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.PersonTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.SealColor;
import com.timevale.esign.sdk.tech.service.SealService;
import com.timevale.esign.sdk.tech.service.SignService;
import com.timevale.esign.sdk.tech.service.factory.SealServiceFactory;
import com.timevale.esign.sdk.tech.service.factory.SignServiceFactory;

/**
 * e签宝签署
 * @author supergirl
 *
 */
@Controller
public class TechSdkController extends WebController {
	
	Logger LOG = LoggerFactory.getLogger(TechSdkController.class);

	SignService SERVICE = SignServiceFactory.instance();
	
	SealService SEAL = SealServiceFactory.instance();
	
	@RequestMapping("tech/techSdk")
	public String techSdk(ModelMap map, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		User user = getCurrUser(req, resp);
		if (user != null) {
			boolean result = true;
	        if (result) {
	        	map.addAttribute(ERRORMSG, "签章成功");
	        	user.setTechSignStatus(1);
	        	userService.userUpdate(user);
	        } else {
	        	map.addAttribute(ERRORMSG, "签章出错");
	        }
		}
		return "redirect:/myhome/safety.html";
	}
	
	public boolean preSignDoc(HttpServletRequest req, HttpServletResponse resp) {

        // 签名/验证
        boolean sign = Boolean.valueOf(req.getParameter("mode"));
        LOG.debug("sign is: " + sign);

        if (sign) {
            // 签署文档使用的印章类型
            int sealType = Integer.valueOf(req.getParameter("sealType"));
            if (-1 == sealType) {
                return signDraw(req, resp);
            } else {
            	return signModel(req, resp, sealType);
            }
        } else {
        	Signtech signTech = new Signtech();
        	return signTech.verifyPdf(req, resp);
        }

    }

    private boolean signDraw(HttpServletRequest req, HttpServletResponse resp) {

        // 页面: 签署文档状态
        String id = req.getParameter("dmId");
        LOG.debug("sign id: " + id);
        // 签署文档使用的印章数据,若存在
        String sealData = req.getParameter("sealData");
        sealData = sealData.substring(sealData.indexOf(',') + 1);
        LOG.debug("sign seal data: " + sealData);

        // 开发者账户ID
        String devId = (String) req.getSession().getAttribute("devId");
        LOG.debug("sign dev id: " + devId);
        // 用户ID
        String userId = (String) req.getSession().getAttribute("userId");
        LOG.debug("sign user id: " + userId);

        // 完整印章数据
        String seal = createSeal(devId, userId, sealData);
        if (null == seal) {
            LOG.error("create seal failed.");
            // 签署失败
            req.getSession().setAttribute(id, SignStatus.SignInit);
//	            Tools.forward(req, resp, "/error.jsp");
            return false;
        }

        Signtech signTech = new Signtech();
        return signTech.sign(req, resp, id, seal);
    }

    private boolean signModel(HttpServletRequest req, HttpServletResponse resp, int sealType) {

        // 页面: 签署文档状态
        String id = req.getParameter("dmId");
        LOG.debug("sign id: " + id);

        // 开发者账户ID
        String devId = (String) req.getSession().getAttribute("devId");
        LOG.debug("sign dev id: " + devId);
        // 用户ID
        String userId = (String) req.getSession().getAttribute("userId");
        LOG.debug("sign user id: " + userId);
        // 用户类型
        String userType = (String) req.getSession().getAttribute("accountType");
        LOG.debug("sign user type: " + userType);

        // 完整印章数据
        String seal = createSeal(userType, devId, userId, sealType);
        if (null == seal) {
            LOG.error("create seal failed.");
            // 签署失败
            req.getSession().setAttribute(id, SignStatus.SignInit);
//	            Tools.forward(req, resp, "/error.jsp");
            return false;
        }
        Signtech signTech = new Signtech();
        return signTech.sign(req, resp, id, seal);
    }

    public boolean preSignText(HttpServletRequest req, HttpServletResponse resp) {

        boolean sign = Boolean.valueOf(req.getParameter("mode"));
        LOG.debug("sign is: " + sign);

        if (sign) {
            return signText(req, resp);
        } else {
        	return verifyText(req, resp);
        }
    }

    private boolean signText(HttpServletRequest req, HttpServletResponse resp) {

        String text = req.getParameter("text");
        String id = req.getParameter("dmId");

        LOG.debug("sign text: " + text);
        LOG.debug("sign id: " + id);

        String devId = (String) req.getSession().getAttribute("devId");
        String userId = (String) req.getSession().getAttribute("userId");
        LOG.debug("sign dev id: " + devId);
        LOG.debug("sign user id: " + userId);

        SignDataResult r = SERVICE.localSignData(devId, text);
        if (0 != r.getErrCode()) {
            // 签署失败
            req.getSession().setAttribute(id, SignStatus.SignInit);

//	            Tools.forward(req, resp, "/error.jsp");
            return false;
        }

        // 文件签署成功
        req.getSession().setAttribute(id, SignStatus.SignDone);
        req.getSession().setAttribute("signature", r.getSignResult());

        // 保存用户账户
//	        Tools.forward(req, resp, "/list.jsp");
        return true;
    }

    private boolean verifyText(HttpServletRequest req, HttpServletResponse resp) {
        String text = req.getParameter("text");
        String id = req.getParameter("dmId");

        LOG.debug("sign text: " + text);
        LOG.debug("sign id: " + id);

        String devId = (String) req.getSession().getAttribute("devId");
        String userId = (String) req.getSession().getAttribute("userId");
        String signature = (String) req.getSession().getAttribute("signature");
        LOG.debug("sign dev id: " + devId);
        LOG.debug("sign user id: " + userId);

        Result r = SERVICE.verifySignResult(text, signature);
        if (0 != r.getErrCode()) {
            // 验证失败
            req.getSession().setAttribute(id, SignStatus.VerifyFailed);

//	            Tools.forward(req, resp, "/list.jsp");
            return false;
        }

        // 文件验证成功
        req.getSession().setAttribute(id, SignStatus.VerifyOk);

        // 保存用户账户
//	        Tools.forward(req, resp, "/list.jsp");
        return true;
    }

    // 创建完整印章数据
    private String createSeal(String accountType, String devId, String userId,
            int type) {

        if (accountType.equalsIgnoreCase("person")) {
            return createSealPersonal(devId, userId, type);
        } else {
            return createSealOrganize(devId, userId, type);
        }
    }

    // 根据手绘数据创建完整印章数据
    private String createSeal(String devId, String userId, String sealData) {

        AddSealResult r = SEAL.addFileSeal(devId, userId, sealData,
                SealColor.RED);
        if (0 != r.getErrCode()) {
            return null;
        }

        return r.getSealData();
    }

    private String createSealPersonal(String devId, String userId, int type) {

        PersonTemplateType seal;

        switch (type) {

        case 1:
            seal = PersonTemplateType.FZKC;
            break;

        case 2:
            seal = PersonTemplateType.HYLSF;
            break;

        case 3:
            seal = PersonTemplateType.RECTANGLE;
            break;

        case 4:
            seal = PersonTemplateType.SQUARE;
            break;

        case 5:
        default:
            seal = PersonTemplateType.YYGXSF;
            break;
        }

        AddSealResult r = SEAL.addTemplateSeal(devId, userId, seal,
                SealColor.RED);
        if (0 != r.getErrCode()) {
            return null;
        }

        return r.getSealData();
    }

    private String createSealOrganize(String devId, String userId, int type) {

        OrganizeTemplateType seal;

        switch (type) {

        case 1:
            seal = OrganizeTemplateType.OVAL;
            break;

        case 2:
        default:
            seal = OrganizeTemplateType.STAR;
            break;
        }

        AddSealResult r = SEAL.addTemplateSeal(devId, userId, seal,
                SealColor.RED, "", "");
        if (0 != r.getErrCode()) {
            return null;
        }

        return r.getSealData();
    }
    
}
