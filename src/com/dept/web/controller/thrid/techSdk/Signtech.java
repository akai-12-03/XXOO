package com.dept.web.controller.thrid.techSdk;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sendinfo.common.lang.StringUtil;
import com.timevale.esign.sdk.tech.bean.PosBean;
import com.timevale.esign.sdk.tech.bean.SignBean;
import com.timevale.esign.sdk.tech.bean.result.Result;
import com.timevale.esign.sdk.tech.bean.result.VerifyPdfResult;
import com.timevale.esign.sdk.tech.impl.constants.SignType;
import com.timevale.esign.sdk.tech.service.SignService;
import com.timevale.esign.sdk.tech.service.factory.SignServiceFactory;

/**
 * 类名：InitServlet.java <br/>
 * 功能说明：初始化服务 <br/>
 * 修改历史： <br/>
 * 1.[2015年9月14日下午7:49:23]创建类 by hewu
 */
public class Signtech extends HttpServlet {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2405561695238168334L;

	private Logger LOG = LoggerFactory.getLogger(Signtech.class);
	
    private SignService SERVICE = SignServiceFactory.instance();
    
    public boolean sign(HttpServletRequest req, HttpServletResponse resp, String id, String seal) {

        // 待签署文档路径
        String file = req.getParameter("file");
        LOG.debug("sign doc: " + file);

        String devId = (String) req.getSession().getAttribute("devId");
        String userId = (String) req.getSession().getAttribute("userId");
        LOG.debug("sign dev id: " + devId);
        LOG.debug("sign user id: " + userId);

        PosBean pos = new PosBean();
        pos.setPosX(400);
        pos.setPosY(100);
        pos.setPosPage("1");
        // 清理
        HttpServletRequest request = (HttpServletRequest)req;
		String realPath = request.getSession().getServletContext().getRealPath("/");;
		clear(realPath + "/doc");

        // 调整目录
        file = realPath + "/" + file;
        StringBuilder full = new StringBuilder();
        full.append(realPath);

        StringBuilder relative = new StringBuilder();
        relative.append("doc/signed_");
        relative.append(UUID.randomUUID().toString());
        relative.append(".pdf");

        // 使用用户印章签名
        Result r = SERVICE.localSignPDF(devId, userId, seal, file,
                full.append("/").append(relative).toString(), pos, SignType.Single,
                "用户测试文档.pdf");
        if (0 != r.getErrCode()) {

            // 签署失败
            req.getSession().setAttribute(id, SignStatus.SignInit);
//            Tools.forward(req, resp, "/error.jsp");
            return false;
        }

        // 文件签署成功
        req.getSession().setAttribute(id, SignStatus.SignDone);
        req.getSession().setAttribute("signedPdf", relative.toString());
        // 保存用户账户
//        Tools.forward(req, resp, "/list.jsp");
        return true;
    }
   
    private void clear(String path) {
        File root = new File(path);
        String[] children = root.list();

        for (String child : children) {
            File tmp = new File(root, child);
            if (tmp.isDirectory()) {
                continue;
            }

            if (!child.startsWith("signed_")) {
                continue;
            }

            tmp.delete();
        }
    }
    
    public boolean verifyPdf(HttpServletRequest req, HttpServletResponse resp) {

        String id = req.getParameter("dmId");
        LOG.debug("sign id: " + id);

        String devId = (String) req.getSession().getAttribute("devId");
        String userId = (String) req.getSession().getAttribute("userId");
        String signedFile = (String) req.getSession().getAttribute("signedPdf");
        
        StringBuilder full = new StringBuilder();
        full.append(this.getServletConfig().getServletContext().getRealPath("/"));
        full.append("/").append(signedFile);
        signedFile = full.toString();
        
        LOG.debug("sign dev id: " + devId);
        LOG.debug("sign user id: " + userId);
        LOG.debug("sign file: " + signedFile);

        VerifyPdfResult r = SERVICE.localVerifyPdf(signedFile);
        if (0 != r.getErrCode()) {
            // 验证失败
            req.getSession().setAttribute(id, SignStatus.VerifyFailed);

//            Tools.forward(req, resp, "/list.jsp");
            return true;
        }

        // 对所有签名进行验证
        for (SignBean sign : r.getSignatures()) {
            if (!sign.getSignature().isValidate()) {
                // 验证失败
                req.getSession().setAttribute(id, SignStatus.VerifyFailed);
//                Tools.forward(req, resp, "/list.jsp");
                return true;
            }
        }

        // 文件验证成功
        req.getSession().setAttribute(id, SignStatus.VerifyOk);

        // 保存用户账户
//        Tools.forward(req, resp, "/list.jsp");
        return true;
    }
    
    public boolean verifyPdf2(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) {

        String id = (String)params.get("dmId");
        LOG.debug("sign id: " + id);

        String devId = (String) request.getSession().getAttribute("devId");
        String userId = (String) request.getSession().getAttribute("userId");
        String signedFile = (String) request.getSession().getAttribute("signedPdf");
        
        StringBuilder full = new StringBuilder();
        full.append(this.getServletConfig().getServletContext().getRealPath("/"));
        full.append("/").append(signedFile);
        signedFile = full.toString();
        
        LOG.debug("sign dev id: " + devId);
        LOG.debug("sign user id: " + userId);
        LOG.debug("sign file: " + signedFile);

        VerifyPdfResult r = SERVICE.localVerifyPdf(signedFile);
        if (0 != r.getErrCode()) {
            // 验证失败
        	request.getSession().setAttribute(id, SignStatus.VerifyFailed);

//            Tools.forward(req, resp, "/list.jsp");
            return true;
        }

        // 对所有签名进行验证
        for (SignBean sign : r.getSignatures()) {
            if (!sign.getSignature().isValidate()) {
                // 验证失败
            	request.getSession().setAttribute(id, SignStatus.VerifyFailed);
//                Tools.forward(req, resp, "/list.jsp");
                return true;
            }
        }

        // 文件验证成功
        request.getSession().setAttribute(id, SignStatus.VerifyOk);

        // 保存用户账户
//        Tools.forward(req, resp, "/list.jsp");
        return true;
    }
    
    public boolean sign2(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params, String id, String seal) {

        // 待签署文档路径
        String file = (String)params.get("file");
        LOG.debug("sign doc: " + file);
        
        String fileSigned = (String)params.get("fileSign");

        String devId = (String) request.getSession().getAttribute("devId");
        String userId = (String) request.getSession().getAttribute("userId");
        LOG.debug("sign dev id: " + devId);
        LOG.debug("sign user id: " + userId);

        PosBean pos = new PosBean();
        pos.setPosX((Integer)params.get("posX"));
        pos.setPosY((Integer)params.get("posY"));
        pos.setPosPage((String)params.get("page"));  // 页数
        // 清理
        HttpServletRequest request2 = (HttpServletRequest)request;
		String realPath = request2.getSession().getServletContext().getRealPath("/");;
		//clear(realPath + "/doc");

        // 调整目录
		file = realPath + File.separator + file;
		
        // 使用用户印章签名
        /*
        Result r = SERVICE.localSignPDF(devId, userId, seal, file,
                full.append("/").append(relative).toString(), pos, SignType.Single,
                "用户测试文档.pdf");
                */
		Result r;
		//平台章
		String platform = (String) params.get("platform");
		if (!StringUtil.isBlank(platform)) {
			r = SERVICE.localSignPDF(devId, file, realPath + File.separator + fileSigned, pos, 
					0, SignType.Single, "用户测试文档.pdf");
		} else {
			r = SERVICE.localSignPDF(devId, userId, seal, file, realPath + File.separator + fileSigned, 
	        		pos, SignType.Single, "用户测试文档.pdf");
		}
       
        if (0 != r.getErrCode()) {

            // 签署失败
        	request.getSession().setAttribute(id, SignStatus.SignInit);
//            Tools.forward(req, resp, "/error.jsp");
            return false;
        }

        // 文件签署成功
        request.getSession().setAttribute(id, SignStatus.SignDone);
        request.getSession().setAttribute("signedPdf", fileSigned);
        // 保存用户账户
//        Tools.forward(req, resp, "/list.jsp");
        return true;
    }
    
}
