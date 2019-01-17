/**
 * com.hpe.cmwa.auditTask.controller.jz.JKYWHGXController.java
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

import com.hpe.cmwa.auditTask.service.sox.TYGHDMSLYWService;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.HelperDate;
import com.hpe.cmwa.util.Prvd_info;

/**
 * <pre>
 * Desc： 
 * @author   ren.yx
 * @refactor ren.yx
 * @date     Jan 15, 2017 8:00:12 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Jan 15, 2017 	   ren.yx 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/tyghdmslyw/")
public class TYGHDMSLYWController extends BaseController {

	@Autowired
	private TYGHDMSLYWService tyghdmslywService;

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
			return "auditTask/sjk/htblyw/tyghdmsl";
		}else{
			return "auditTask/sox/htblyw/tyghdmsl";
		}
	}

	/**
	 * <pre>
	 * Desc 每个controller都要实现的一个方法，用来设置页面初始化查询参数的form使用 
	 * @return
	 * @author ren yx
	 * Nov 17, 2016 2:16:17 PM
	 * </pre>
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "initDefaultParams")
	public Map initDefaultParams(HttpServletRequest request,HttpServletResponse response) {
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
		
		return defaultParams;
	}
	
	/**
	 * <pre>
	 * Desc  汇总页-趋势图
	 * @param request
	 * @return
	 * @author ren.yx
	 * Jan 15, 2017 10:20:18 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_qst_chart")
	public List<Map<String, Object>> load_hz_qst_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Map<String, Object>> list = tyghdmslywService.load_hz_qst_chart(formatParameter(params));
		return list;
	}

	/**
	 *  汇总页-统计分析-趋势图-数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "load_hz_qst_table")
	@ResponseBody
	public Object load_hz_qst_table(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		List<Map<String, Object>> cityMapList = tyghdmslywService.load_hz_qst_table(pager);
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
	@RequestMapping(value = "export_hz_qst_table")
	public void export_hz_qst_table(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		tyghdmslywService.export_hz_qst_table(request, response, parameterMap);
	}
	
	
	
	
	
	
	/**
	 * <pre>
	 * Desc  汇总页-统计分析-地市-图形
	 * @param request
	 * @return
	 * @author ren.yx
	 * Jan 15, 2017 10:20:51 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_city_chart")
	public List<Map<String, Object>> load_hz_city_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Map<String, Object>> list = tyghdmslywService.load_hz_city_chart(formatParameter(params));
		return list;
	}
	/**
	 *  汇总页-统计分析-趋势图-数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "load_hz_city_table")
	@ResponseBody
	public Object load_hz_city_table(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		List<Map<String, Object>> cityMapList = tyghdmslywService.load_hz_city_table(pager);
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
	@RequestMapping(value = "export_hz_city_table")
	public void export_hz_city_table(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		tyghdmslywService.export_hz_city_table(request, response, parameterMap);
	}
	/**
	 * <pre>
	 * Desc  汇总页-统计分析-地市-结论
	 * @param request
	 * @return
	 * @author ren.yx
	 * Jan 15, 2017 10:20:41 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_city_conclusion")
	public Map<String, Object> load_hz_city_conclusion(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		Map<String, Object> map = tyghdmslywService.load_hz_city_conclusion(formatParameter(params));
		return map;
	}
	

	/**
	 *  明细-数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "load_mx_table")
	@ResponseBody
	public Object load_mx_table(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		List<Map<String, Object>> cityMapList = tyghdmslywService.load_mx_table(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	
	/**
	 *  导出_明细-数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "export_mx_table")
	public void export_mx_table(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		tyghdmslywService.export_mx_table(request, response, parameterMap);
	}

	/**
	 * <pre>
	 * Desc  根据需要对页面参数进行格式化
	 * @param requestParamsMap
	 * @author ren.yx
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
