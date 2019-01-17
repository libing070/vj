/**
 * com.hpe.cmwa.auditTask.controller.jz.JKYWHGXController.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.controller.sjk;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.sjk.SHQDGHBLYWSJKService;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.HelperDate;

/**
 * <pre>
 * Desc： 
 * @author   renyuxing
 * @refactor renyuxing
 * @date     Jan 15, 2017 8:00:12 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Jan 15, 2017 	   renyuxing 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/ycsdblywshqdsjk/")
public class SHQDGHBLYWSJKController extends BaseController {

	@Autowired
	private SHQDGHBLYWSJKService	shqdghblywsjkService;

	@RequestMapping(value = "/index")
    public String index(HttpServletRequest request,HttpServletResponse response) {
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		return "auditTask/sjk/ywslhgx/shqdgh";
	}
	/**
	 * <pre>
	 * Desc  汇总页-趋势图
	 * @param request
	 * @return
	 * @author ren yx
	 * Jan 15, 2017 10:20:18 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_column_chart")
	public List<Map<String, Object>> load_column_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Map<String, Object>> list = shqdghblywsjkService.load_column_chart(formatParameter(params));
		return list;
	}
	
	/**
	 * <pre>
	 * Desc  地图
	 * @param request
	 * @return
	 * @author ren yx
	 * Jan 15, 2017 10:20:18 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_map_chart")
	public List<Map<String, Object>> load_map_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Map<String, Object>> list = shqdghblywsjkService.load_map_chart(formatParameter(params));
		return list;
	}
	
	/**
	 * <pre>
	 * Desc  汇总页-趋势图
	 * @param request
	 * @return
	 * @author ren yx
	 * Jan 15, 2017 10:20:18 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_line_chart")
	public List<Map<String, Object>> load_line_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Map<String, Object>> list = shqdghblywsjkService.load_line_chart(formatParameter(params));
		return list;
	}
	/**
	 *  汇总页-统计分析-趋势图-数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "load_table")
	@ResponseBody
	public Object load_hz_qst_table(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		List<Map<String, Object>> cityMapList = shqdghblywsjkService.load_table(pager);
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
	@RequestMapping(value = "exportTable")
	public void exportTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		shqdghblywsjkService.exportTable(request, response, parameterMap);
	}
	
	/**
	 * <pre>
	 * Desc  根据需要对页面参数进行格式化
	 * @param requestParamsMap
	 * @author ren yx
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
