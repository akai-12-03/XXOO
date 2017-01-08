package com.dept.web.controller.thrid.techSdk;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class ESignUtil extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger LOG = LoggerFactory.getLogger(ESignUtil.class);
	SignService SERVICE = SignServiceFactory.instance();
	
	SealService SEAL = SealServiceFactory.instance();
	
	/**
	 * 投资人个人签章
	 * @param request
	 * @param response
	 * @param unsignedFilePath
	 * @param signedFilePath
	 * @return
	 */
	public boolean SignPerson(HttpServletRequest request, HttpServletResponse response, String unsignedFilePath, String signedFilePath,String page,int x,int y) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "pdf");
		params.put("dmId", "dm_pdf_status");
		params.put("sealType", 4);
		params.put("mode", true);
		params.put("file", unsignedFilePath);
		params.put("fileSign", signedFilePath);
		params.put("posX", x);
		params.put("posY", y);
		params.put("page", page);
		return preSignDoc(request, response, params);
	}
	
	/**
	 * 借款人企业签章
	 * @param request
	 * @param response
	 * @param unsignedFilePath
	 * @param signedFilePath
	 * @return
	 */
	public boolean SignOrganize(HttpServletRequest request, HttpServletResponse response, String unsignedFilePath, String signedFilePath,String page,int x,int y) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "");
		params.put("dmId", "dm_pdf_status");
		params.put("sealType", 2);
		params.put("mode", true);
		params.put("file", unsignedFilePath);
		params.put("fileSign", signedFilePath);
		params.put("page", page);
		params.put("posX", x);
		params.put("posY", y);
		return preSignDoc(request, response, params);
	}
	
	/**
	 * 担保公司签章
	 * @param request
	 * @param response
	 * @param unsignedFilePath
	 * @param signedFilePath
	 * @return
	 */
	public boolean SignGuaranteeCompany(HttpServletRequest request, HttpServletResponse response, String unsignedFilePath, String signedFilePath) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "");
		params.put("dmId", "dm_pdf_status");
		params.put("sealType", 2);
		params.put("mode", true);
		params.put("file", unsignedFilePath);
		params.put("fileSign", signedFilePath);
		
		params.put("posX", 140);
		params.put("posY", 350);
		return preSignDoc(request, response, params);
	}
	
	/**
	 * 平台签章
	 * @param request
	 * @param response
	 * @param unsignedFilePath
	 * @param signedFilePath
	 * @return
	 */
	public boolean SignPlatform(HttpServletRequest request, HttpServletResponse response, String unsignedFilePath, String signedFilePath,String page) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "");
		params.put("dmId", "dm_pdf_status");
		params.put("sealType", 2);
		params.put("mode", true);
		params.put("file", unsignedFilePath);
		params.put("fileSign", signedFilePath);
		params.put("page", page);
		params.put("posX", 220);
		params.put("posY", 250);
		
		params.put("platform", "platform");// 平台签章
         
		return preSignDoc(request, response, params);
	}
	
	public boolean preSignDoc(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) {
		
        // 签名/验证
        boolean sign = (Boolean)params.get("mode");
        LOG.debug("sign is: " + sign);

        if (sign) {
            // 签署文档使用的印章类型
        	int sealType = Integer.valueOf((Integer)params.get("sealType"));
            if (-1 == sealType) {
                return signDraw(request, response, params);
            } else {
            	return signModel(request, response, params);
            }
        } else {
        	Signtech signTech = new Signtech();
        	return signTech.verifyPdf2(request, response, params);
        }

    }
	
	private boolean signDraw(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) {
		
        // 页面: 签署文档状态
        String id = (String)params.get("dmId");
        LOG.debug("sign id: " + id);
        // 签署文档使用的印章数据,若存在
        String sealData = (String)params.get("sealData");
        sealData = sealData.substring(sealData.indexOf(',') + 1);
        LOG.debug("sign seal data: " + sealData);

        // 开发者账户ID
        String devId = (String) request.getSession().getAttribute("devId");
        LOG.debug("sign dev id: " + devId);
        // 用户ID
        String userId = (String) request.getSession().getAttribute("userId");
        LOG.debug("sign user id: " + userId);

        // 完整印章数据
        String seal = createSeal(devId, userId, sealData);
        if (null == seal) {
            LOG.error("create seal failed.");
            // 签署失败
            request.getSession().setAttribute(id, SignStatus.SignInit);
//	            Tools.forward(req, resp, "/error.jsp");
            return false;
        }

        Signtech signTech = new Signtech();
        return signTech.sign2(request, response, params, id, seal);
    }
    
	private boolean signModel(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) {
		
        // 页面: 签署文档状态
        String id = (String)params.get("dmId");
        LOG.debug("sign id: " + id);

        // 开发者账户ID
        String devId = (String) request.getSession().getAttribute("devId");
        LOG.debug("sign dev id: " + devId);
        // 用户ID
        String userId = (String) request.getSession().getAttribute("userId");
        LOG.debug("sign user id: " + userId);
        // 用户类型
        String userType = (String) request.getSession().getAttribute("accountType");
        LOG.debug("sign user type: " + userType);

        // 完整印章数据
        int sealType = Integer.valueOf((Integer)params.get("sealType"));
        String seal = createSeal(userType, devId, userId, sealType);
        if (null == seal) {
            LOG.error("create seal failed.");
            // 签署失败
            request.getSession().setAttribute(id, SignStatus.SignInit);
//	            Tools.forward(req, resp, "/error.jsp");
            return false;
        }
        Signtech signTech = new Signtech();
        return signTech.sign2(request, response, params, id, seal);
    }
	
	public boolean preSignText(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) {

        boolean sign = Boolean.valueOf((Boolean)params.get("mode"));
        LOG.debug("sign is: " + sign);

        if (sign) {
            return signText(request, response, params);
        } else {
        	return verifyText(request, response, params);
        }
    }
	
	private boolean signText(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) {

        String text = (String)params.get("text");
        String id = (String)params.get("dmId");

        LOG.debug("sign text: " + text);
        LOG.debug("sign id: " + id);

        String devId = (String) request.getSession().getAttribute("devId");
        String userId = (String) request.getSession().getAttribute("userId");
        LOG.debug("sign dev id: " + devId);
        LOG.debug("sign user id: " + userId);

        SignDataResult r = SERVICE.localSignData(devId, text);
        if (0 != r.getErrCode()) {
            // 签署失败
            request.getSession().setAttribute(id, SignStatus.SignInit);

//	            Tools.forward(req, resp, "/error.jsp");
            return false;
        }

        // 文件签署成功
        request.getSession().setAttribute(id, SignStatus.SignDone);
        request.getSession().setAttribute("signature", r.getSignResult());

        // 保存用户账户
//	        Tools.forward(req, resp, "/list.jsp");
        return true;
    }
	
	private boolean verifyText(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) {
        String text = (String)params.get("text");
        String id = (String)params.get("dmId");

        LOG.debug("sign text: " + text);
        LOG.debug("sign id: " + id);

        String devId = (String) request.getSession().getAttribute("devId");
        String userId = (String) request.getSession().getAttribute("userId");
        String signature = (String) request.getSession().getAttribute("signature");
        LOG.debug("sign dev id: " + devId);
        LOG.debug("sign user id: " + userId);

        Result r = SERVICE.verifySignResult(text, signature);
        if (0 != r.getErrCode()) {
            // 验证失败
        	request.getSession().setAttribute(id, SignStatus.VerifyFailed);

//	            Tools.forward(req, resp, "/list.jsp");
            return false;
        }

        // 文件验证成功
        request.getSession().setAttribute(id, SignStatus.VerifyOk);

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

        AddSealResult r = SEAL.addFileSeal(devId, userId, sealData, SealColor.RED);
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

        AddSealResult r = SEAL.addTemplateSeal(devId, userId, seal, SealColor.RED);
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
