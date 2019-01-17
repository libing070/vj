/**
 * com.hpe.cmwa.auditTask.controller.jz.JKYWHGXController.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.controller.jz;

import java.text.DecimalFormat;
import java.util.ArrayList;
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

import com.hpe.cmwa.auditTask.service.jz.JKYWHGXService;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
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
@RequestMapping("/jkywhgx/")
public class JKYWHGXController extends BaseController {
	
	 private DecimalFormat df = new DecimalFormat("######0.00");

	@Autowired
	private JKYWHGXService	jkywhgxService;

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
			return "auditTask/sjk/jkywhgx_qg/kdxjkt_qg";
		}else{
			return "auditTask/jz/jkywhgx/jkywhgx";
		}
		
	}
	
	/**
	 * desc:初始化积分回馈率页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "indexjy")
	public String indexjy(HttpServletRequest request) {
		
		logger.debug(this.getParameterStr(request));
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		if(request.getParameter("provinceCode").equals("10000")){
			return "auditTask/sjk/jkywxjkt/kdxjkt_qg";
		}else{
			return "auditTask/jy/jkywxjkt/jkywhgx";
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

		return defaultParams;
	}

	/**
	 * <pre>
	 * Desc  汇总页-波动趋势-结论
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:20:18 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_bdqs_conclusion")
	public Map<String, Object> load_hz_bdqs_conclusion(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Map<String, Object>> list = jkywhgxService.hz_bdqs_chart(params);
		Map<String, Object> avgMap = jkywhgxService.hz_bdqs_ChartAVGNumber(params);
		Double avg_weigui_num = (avgMap.get("avg_weigui_num")==null?0.00:Double.parseDouble(avgMap.get("avg_weigui_num").toString()));
		int sumWeiguiNum = 0;
		int sumNum = 0;
		for(Map<String, Object> map :list){
			sumWeiguiNum += Integer.valueOf(map.get("weiguiNum").toString());
			sumNum++;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("sumWeiguiNum", sumWeiguiNum);
		resultMap.put("sumNum", sumNum);
		resultMap.put("avg_weigui_num", df.format(avg_weigui_num));
		resultMap.put("maxAudTrm", jkywhgxService.hz_bdqs_conclusion_max(params));
		return resultMap;
	}

	/**
	 * <pre>
	 * Desc  汇总页-波动趋势-图形
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:20:25 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_bdqs_chart")
	public List<Map<String, Object>> load_hz_bdqs_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		return jkywhgxService.hz_bdqs_chart(params);
	}

	/**
	 * <pre>
	 * Desc  汇总页-统计分析-统计-结论
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:20:41 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_tjfx_tj_conclusion")
	public List<Object> load_hz_tjfx_tj_conclusion(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Object> resultList = new ArrayList<Object>();
		resultList.add(jkywhgxService.hz_tjfx_tj_chart(params));
		resultList.add(jkywhgxService.hz_tjfx_tj_conclusion(params));
		resultList.add(jkywhgxService.hz_tjfx_tj_conclusion_2(params));
		return resultList;
	}

	/**
	 * <pre>
	 * Desc  汇总页-统计分析-统计-图形
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:20:51 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_tjfx_tj_chart")
	public List<Map<String, Object>> load_hz_tjfx_tj_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		return jkywhgxService.hz_tjfx_tj_chart(params);
	}

	/**
	 * <pre>
	 * Desc  汇总页-统计分析-明细-结论
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:21:04 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_tjfx_mx_conclusion")
	public Map<String, String> load_hz_tjfx_mx_conclusion(HttpServletRequest request) {

		return new HashMap<String, String>();
	}

	/**
	 * <pre>
	 * Desc  汇总页-统计分析-明细-表格
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:21:04 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_tjfx_mx_table")
	public Pager load_hz_tjfx_mx_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = jkywhgxService.hz_tjfx_mx_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	/**
	 * <pre>
	 * Desc  汇总页-统计分析-明细-表格-导出
	 * @param request
	 * @author peter.fu
	 * Jan 15, 2017 10:21:04 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "export_hz_tjfx_mx_table")
	public void export_hz_tjfx_mx_table(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		jkywhgxService.export_hz_tjfx_mx_table(request, response, parameterMap);
	}
	/**
	 * <pre>
	 * Desc  明细页-表格
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:21:04 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_mx_table")
	public Pager load_mx_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = jkywhgxService.mx_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}

	/**
	 * <pre>
	 * Desc  明细页-表格-导出
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:21:04 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "export_mx_table")
	public void export_mx_table(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		jkywhgxService.mx_export_btn(request, response, parameterMap);
	}

}
