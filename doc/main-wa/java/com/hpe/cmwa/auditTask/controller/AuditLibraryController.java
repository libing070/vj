package com.hpe.cmwa.auditTask.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.job.AuditLibraryConclusion;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.MD5;

/**
 * <pre>
 * Desc：
 * url:   
 * 模型结论：http://ip:port/cmwa/auditLibrary/modelConclusion?provinceCode=12400&beforeAcctMonth=201512&endAcctMonth=201603&auditId=1001&url=xx&validateStr=xx&timestamp=yyyyMMddHHmmss&extParam=xx
 * 
 * 审计库模型结论与国信集成接口
 * 生成实时模型结果
 * @author   yuxing.ren
 * @refactor yuxing.ren
 * @date     07 20, 2017
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  07 20, 2017 	   yuxing.ren 	         1. Created this class. 
 * </pre>
 */

@Controller
@RequestMapping("/auditLibrary/")
public class AuditLibraryController extends BaseController {

	@Autowired
	private AuditLibraryConclusion	auditLibraryConclusion	= null;

	/**
	 * 国信给的加密key
	 */
	private String				key					= "sj2bonc";

	/**
	 * <pre>
	 * Desc  根据审计点id映射到不同的controller访问路径
	 * 国信接口参数：
	 * 		provinceCode 省分编码（非空）
	 * 		beforeAcctMonth 数据起始账期 格式为yyyyMM,如:201601（非空）
	 * 		endAcctMonth 数据结束账期 格式为yyyyMM,如:201601（非空）
	 * 		auditId 审计点ID（非空）
	 * 		url要访问的页面地址（非空）--忽略，国信内部框架的要求
	 * 		validateStr  签名参数（非空）--MD5.MD5Crypt(provinceCode+ beforeAcctMonth+ endAcctMonth+ auditId+ taskCode+url+timestamp+key);
	 * 		timestamp时间戳 格式为yyyyMMddHHmmss（非空）
	 * 		extParam 拓展参数（保留使用，可空）
	 * 返回值：
	 *    html页面
	 * @param request
	 * @param response
	 * @return
	 * @author peter.fu
	 * Nov 21, 2016 3:10:16 PM
	 * </pre>
	 * @throws Exception 
	 */
	@RequestMapping(value = "modelConclusion",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, Object> params = this.getParameterMap(request);
		
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
	    request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
	    request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
	    request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		    
	    String provinceCode = request.getParameter("provinceCode");
		String beforeAcctMonth = request.getParameter("beforeAcctMonth");
		String endAcctMonth = request.getParameter("endAcctMonth");
		String auditId = request.getParameter("auditId");
		String gxUrl = request.getParameter("url");
		String validateStr = request.getParameter("validateStr");
		String timestamp = request.getParameter("timestamp");
		
		 // 2.验证签名一致性
		String	newBuildValidateStr= MD5.MD5Crypt(provinceCode+ beforeAcctMonth+ endAcctMonth+ auditId+gxUrl+timestamp+key);
		if (!StringUtils.equals(validateStr, newBuildValidateStr)) {		
			throw new RuntimeException("详情校验不通过!");
		}
		
		String conclusion = auditLibraryConclusion.getAuditLibraryResult(params);
		return conclusion ;
	}
}
