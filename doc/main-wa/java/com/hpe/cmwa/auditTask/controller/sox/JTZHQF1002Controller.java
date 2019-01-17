/**
 * com.hpe.cmwa.auditTask.controller.jz.TFSRHGXController.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.controller.sox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.sox.JTZHQF1002Service;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.HelperDate;
import com.hpe.cmwa.util.Prvd_info;

/**
 * <pre>
 * Desc： 
 * @author   peter.fu
 * @refactor peter.fu
 * @date     Jan 15, 2017 8:09:36 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Jan 15, 2017 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/jtzhqf1002/")
public class JTZHQF1002Controller extends BaseController {

	@Autowired
	private JTZHQF1002Service	jtzhqffService;

	/**
	 * desc:初始化积分回馈率页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request) {

		logger.debug(this.getParameterStr(request));
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		if(request.getParameter("provinceCode").equals("10000")){
			return "auditTask/sjk/khzhqf/jtzhqf1002";
		}else{
			return "auditTask/sox/khzhqf/jtzhqf1002";
		}
		
	}
	/**
	 * <pre>
	 * Desc  每个controller都要实现的一个方法，用来设置页面初始化查询参数的form使用 
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 8:05:17 PM
	 * </pre>
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "initDefaultParams")
	public Map initDefaultParams(HttpServletRequest request) {

		HttpSession sessoin = request.getSession();
		Map<String, Object> defaultParams = new HashMap<String, Object>();
		// 国信参数
		defaultParams.put("provinceCode", sessoin.getAttribute("provinceCode"));
		defaultParams.put("beforeAcctMonth", sessoin.getAttribute("beforeAcctMonth"));
		defaultParams.put("endAcctMonth", sessoin.getAttribute("endAcctMonth"));
		defaultParams.put("auditId", sessoin.getAttribute("auditId"));
		defaultParams.put("taskCode", sessoin.getAttribute("taskCode"));
		// 汇总参数默认值
		defaultParams.put("currSumBeginDate", sessoin.getAttribute("beforeAcctMonth"));
		defaultParams.put("currSumEndDate", sessoin.getAttribute("endAcctMonth"));
		Prvd_info prvdInfo = Constants.getPrvdInfo(sessoin.getAttribute("provinceCode").toString());
		defaultParams.put("provinceName", prvdInfo.getPrivdcd());
		// 清单参数默认值
		defaultParams.put("currDetBeginDate", sessoin.getAttribute("beforeAcctMonth"));
		defaultParams.put("currDetEndDate", sessoin.getAttribute("endAcctMonth"));
		defaultParams.put("currCityType", this.getCityObjectList(sessoin.getAttribute("provinceCode") + ""));
		defaultParams.put("trmnlForm", dict.getList("trmnlForm"));
		return defaultParams;
	}
	
	/**
	 * Desc
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "load_qst_chart")
	@ResponseBody
	public List<Map<String, Object>> load_qst_chart(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		return jtzhqffService.load_qst_chart(params);
	}

	/**
	 *  汇总页-统计分析-趋势图-数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "load_qst_sjb_table")
	@ResponseBody
	public Object load_qst_sjb_table(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		List<Map<String, Object>> cityMapList = jtzhqffService.load_qst_sjb_table(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	
	/**
	 * 导出 汇总页-统计分析-趋势图-数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value = "hz_qst_sjb_export")
	public void hz_qst_sjb_export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		jtzhqffService.hz_qst_sjb_export(request, response, parameterMap);
	}
	
	/**
	 * Desc
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "load_tj_chart")
	@ResponseBody
	public List<Map<String, Object>> load_tj_chart(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		return jtzhqffService.load_tj_chart(params);
	}
	
	/**
	 *  汇总页-统计分析-趋势图-数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "load_tj_sjb_table")
	@ResponseBody
	public Object load_tj_sjb_table(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		List<Map<String, Object>> cityMapList = jtzhqffService.load_tj_sjb_table(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	
	/**
	 * 导出 汇总页-统计分析-趋势图-数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value = "hz_tj_sjb_export")
	public void hz_tj_sjb_export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		jtzhqffService.hz_tj_sjb_export(request, response, parameterMap);
	}
	
	/**
	 *  汇总页-统计分析-趋势图-数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "load_mx_table_qf")
	@ResponseBody
	public Object load_mx_table_qf(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		List<Map<String, Object>> cityMapList = jtzhqffService.load_mx_table_qf(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	
	/**
	 *  汇总页-统计分析-趋势图-数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "load_mx_table_zb")
	@ResponseBody
	public Object load_mx_table_zb(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		List<Map<String, Object>> cityMapList = jtzhqffService.load_mx_table_zb(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	/**
	 * 
	 * <pre>
	 * Desc s
	 * @param request
	 * @return
	 * @author 
	 * @refactor 
	 * @date   2017-4-1 下午1:14:21
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="export_mx_table_qf")
	public void export_mx_table_qf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = this.getParameterMap(request);
		jtzhqffService.export_mx_table_qf(request,response,params);
	}
	/**
	 * 
	 * <pre>
	 * Desc s
	 * @param request
	 * @return
	 * @author 
	 * @refactor 
	 * @date   2017-4-1 下午1:14:21
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="export_mx_table_zb")
	public void export_mx_table_zb(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = this.getParameterMap(request);
		jtzhqffService.export_mx_table_zb(request,response,params);
	}
	
	/**
	 * <pre>
	 * Desc  根据需要对页面参数进行格式化
	 * @param requestParamsMap
	 * @author peter.fu
	 * Nov 25, 2016 5:27:07 PM
	 * </pre>
	 */
	private Map<String, Object> formatParameter(Map<String, Object> requestParamsMap) {

		if (requestParamsMap == null) {
			return null;
		} else {
			// 格式化时间等
			for (String key : requestParamsMap.keySet()) {
				if (key.equals("currSumBeginDate") || key.equals("currSumEndDate") || key.equals("currDetBeginDate") || key.equals("currDetEndDate")) {
					HelperDate.formatDateStrToStr(requestParamsMap.get(key).toString(), "yyyyMM");
				}
			}
		}

		return requestParamsMap;
	}

}
