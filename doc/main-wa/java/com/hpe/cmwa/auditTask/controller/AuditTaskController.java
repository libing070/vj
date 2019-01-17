/**
 * com.hpe.cmwa.controller.AuditTaskController.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.AuditTaskService;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.MD5;

/**
 * <pre>
 * Desc：
 * url:   
 * 详情页面：http://ip:port/cmwa/auditTask/detail?provinceCode=12400&beforeAcctMonth=201512&endAcctMonth=201603&auditId=1001&taskCode=1&url=xx&validateStr=xx&timestamp=yyyyMMddHHmmss&extParam=xx
 * 接受通知：http://ip:port/cmwa/auditTask/notify?IntfCode=12400&DateMonth=201512&validateStr=xxx&timestamp=yyyyMMddHHmmss&extParam=xx
 * 
 * 审计任务与国信集成接口
 * 1.专项审计任务的获取详情页面的统一入口
 * 2.接收国信的文件加载完成通知，生成模型结果，调用国信的模型结论推送接口
 * @author   peter.fu
 * @refactor peter.fu
 * @date     Nov 21, 2016 3:01:46 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Nov 21, 2016 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/auditTask/")
public class AuditTaskController extends BaseController {

	@Autowired
	private AuditTaskService	auditTaskService	= null;

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
	 * 		taskCode 任务编码（可空）--专项任务不为空，通用任务为空
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
	 */
	@RequestMapping(value = "detail")
	public String getDetail(HttpServletRequest request, HttpServletResponse response) {

		this.logger.debug("获取审计任务的详情页面的url: " + this.getParameterStr(request));
		
		String url = auditTaskService.queryAuditTaskUrl(this.getParameterMap(request));
		
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
	    request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
	    request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
	    request.getSession().setAttribute("auditId", request.getParameter("auditId"));
	    request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		    
	    String provinceCode = request.getParameter("provinceCode");
		String beforeAcctMonth = request.getParameter("beforeAcctMonth");
		String endAcctMonth = request.getParameter("endAcctMonth");
		String auditId = request.getParameter("auditId");
		String taskCode = request.getParameter("taskCode");
		String gxUrl = request.getParameter("url");
		String validateStr = request.getParameter("validateStr");
		String timestamp = request.getParameter("timestamp");
		
		 // 2.验证签名一致性
		String newBuildValidateStr ="";
		if(taskCode == null){
			newBuildValidateStr= MD5.MD5Crypt(provinceCode+ beforeAcctMonth+ endAcctMonth+ auditId+gxUrl+timestamp+key);
		}else{
			newBuildValidateStr= MD5.MD5Crypt(provinceCode+ beforeAcctMonth+ endAcctMonth+ auditId+ taskCode+gxUrl+timestamp+key);
		}
		if (StringUtils.equals(validateStr, newBuildValidateStr)) {		
			throw new RuntimeException("详情页面校验不通过!");
		}
		return "forward:" + url;
	}

	/**
	 * <pre>
	 * Desc  应用库接口数据装载完成通知接口
	 * 参数列表：
	 * 		IntfCode 接口单元编码（非空）
	 * 		DateMonth 数据账期 格式为yyyyMM,如:201601（非空）
	 * 		validateStr  签名参数（非空）
	 * 		timestamp时间戳 格式为yyyyMMddHHmmss（非空）
	 * 		extParam 拓展参数（保留使用，可空）
	 * 返回值：
	 * { 
	 * 		IntfCode:'' ,  //审计点ID（非空）
	 * 		DateMonth:'',  //数据账期（非空）
	 * 		resultCode:'', //状态码（00表示成功，01-表示失败）（非空）
	 * 		validateStr:'' //签名参数（同参数值一致）（非空） MD5.MD5Crypt(IntfCode+DateMonth +timestamp+key);
	 * }
	 * @param request
	 * @param response
	 * @return
	 * @author peter.fu
	 * Nov 21, 2016 3:10:16 PM
	 * </pre>
	 */
	@RequestMapping(value = "notify")
	@ResponseBody
	public Map<String,String> notify(HttpServletRequest request, HttpServletResponse response) {

		this.logger.debug("应用库接口数据装载完成通知接口url: " + this.getParameterStr(request));
		// 1.获取参数
		String IntfCode = request.getParameter("IntfCode");// 接口单元编码（非空）
		String DateMonth = request.getParameter("DateMonth");// 接口单元编码（非空）
		String validateStr = request.getParameter("validateStr");// 接口单元编码（非空）
		String timestamp = request.getParameter("timestamp");// 接口单元编码（非空）
		// String extParam = request.getParameter("extParam");// 接口单元编码（非空）
		// 2.验证签名一致性
		String newBuildValidateStr = MD5.MD5Crypt(IntfCode + DateMonth + timestamp + key);
		if (!StringUtils.equals(validateStr, newBuildValidateStr)) {
			this.logger.debug("应用库接口数据装载完成通知接口校验FAIL");
			return buildNotifyReslut(IntfCode, DateMonth, Constants.AuditTask.Result.FAIL, validateStr);
		}
		// 3.记录文件加载完成的通知消息
		auditTaskService.addFileNotification(this.getParameterMap(request));
		return buildNotifyReslut(IntfCode, DateMonth, Constants.AuditTask.Result.SUCCESS, validateStr);
	}

	/**
	 * <pre>
	 * Desc  根据模型结论推送情况生成通知接口的返回结果
	 * 返回值：
	 * { 
	 * 		IntfCode:'' ,  //审计点ID（非空）
	 * 		DateMonth:'',  //数据账期（非空）
	 * 		resultCode:'', //状态码（00表示成功，01-表示失败）（非空）
	 * 		validateStr:'' //签名参数（同参数值一致）（非空） MD5.MD5Crypt(IntfCode+DateMonth +timestamp+key);
	 * }
	 * @param pushResultMap
	 * @return
	 * @author peter.fu
	 * Nov 21, 2016 3:45:44 PM
	 * </pre>
	 */
	private Map<String, String> buildNotifyReslut(String IntfCode, String DateMonth, String resultCode, String validateStr) {

		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("IntfCode", IntfCode);
		resultMap.put("DateMonth", DateMonth);
		resultMap.put("resultCode", resultCode);
		resultMap.put("validateStr", validateStr);
		return resultMap;
	}
}
