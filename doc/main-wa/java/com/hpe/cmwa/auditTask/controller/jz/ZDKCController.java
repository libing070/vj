/**
 * com.hpe.cmwa.auditTask.controller.jz.JKYWHGXController.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.controller.jz;

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

import com.hpe.cmwa.auditTask.service.jz.ZDKCService;
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
 * @date     Jan 15, 2017 8:00:12 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Jan 15, 2017 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/zdkc/")
public class ZDKCController extends BaseController {

	@Autowired
	private ZDKCService	zdkcService;

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
		String url = "";
		if(request.getParameter("provinceCode").equals("10000")){
			url = "auditTask/sjk/zdkchgx/zdkc";
		}else{
			url = "auditTask/jz/zdkc/zdkc";
		}
		return url;
	}

	/**
	 * <pre>
	 * Desc 每个controller都要实现的一个方法，用来设置页面初始化查询参数的form使用 
	 * @return
	 * @author xinguangchuan
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
	 * Desc  汇总页-终端厂商-趋势图
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:20:18 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_zdcs_chart")
	public Map<String, Object> load_hz_zdcs_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		Map<String, Object> list = zdkcService.load_hz_zdcs_chart(formatParameter(params));
		return list;
	}

	/**
	 * <pre>
	 * Desc  汇总页-终端厂商-审计结论
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:20:18 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_zdcs_conclusion")
	public List<Map<String, Object>> load_hz_zdcs_conclusion(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Map<String, Object>> list= zdkcService.load_hz_zdcs_conclusion(formatParameter(params));
		return list;
	}
	
	/**
	 * <pre>
	 * Desc  汇总页-终端类型-趋势图
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:20:18 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_zdlx_chart")
	public Map<String, Object> load_hz_zdlx_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		Map<String, Object> list = zdkcService.load_hz_zdlx_chart(formatParameter(params));
		return list;
	}
	
	/**
	 * <pre>
	 * Desc  汇总页-终端类型-审计结论
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:20:18 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_zdlx_conclusion")
	public List<Map<String, Object>> load_hz_zdlx_conclusion(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Map<String, Object>> list= zdkcService.load_hz_zdlx_conclusion(formatParameter(params));
		return list;
	}
	
	/**
	 * 获取欠费趋势波动数据表数据
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "load_mx_table")
	@ResponseBody
	public Object getBdqsTableData(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		Map<String, Object> params = pager.getParams();
		pager.setParams(params);
		List<Map<String, Object>> cityMapList = zdkcService.load_mx_table(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	
	/**
	 * 明细  数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "export_mx_table")
	public void exportDetailTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		zdkcService.export_mx_table(request, response, parameterMap);
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
