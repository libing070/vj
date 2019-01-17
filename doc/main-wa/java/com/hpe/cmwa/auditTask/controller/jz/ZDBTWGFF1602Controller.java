/**
 * com.hpe.cmwa.auditTask.controller.jz.TFSRHGXController.java
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

import com.hpe.cmwa.auditTask.service.jz.ZDBTWGFF1602Service;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
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
@RequestMapping("/zdbtwgff1602/")
public class ZDBTWGFF1602Controller extends BaseController {

	@Autowired
	private ZDBTWGFF1602Service	zdbtwgffService;

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
			return "auditTask/sjk/zdbtwgff_qg/wgdfdzzdff_qg";
		}else{
			return "auditTask/jz/zdbtwgff/zdbtwgff1602";
		}
		
	}
	@RequestMapping(value = "index1602")
	public String index1602(HttpServletRequest request) {

		logger.debug(this.getParameterStr(request));
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		return "auditTask/jy/wgzdbt/zdbtwgff1602";
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
	 * Desc  向无线座机、数据卡发放补贴金额地市分布  chart
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:20:25 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_qgpm_tongji_chart")
	public List<Map<String, Object>> hz_qgpm_tongji_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		return zdbtwgffService.hz_qgpm_tongji_chart(params);
	}

	/**
	 * <pre>
	 * Desc  X省向无线座机、数据卡发放补贴金额全国排名 tab
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:21:04 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_qgpm_tongji_table")
	public Pager load_hz_qgpm_tongji_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = zdbtwgffService.hz_qgpm_tongji_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * <pre>
	 * Desc  省向无线座机、数据卡发放补贴金额地市分布 chart
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:20:25 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_dqfb_tongji_chart")
	public List<Map<String, Object>> hz_dqfb_tongji_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		return zdbtwgffService.hz_dqfb_tongji_chart(params);
	}
	
	/**
	 * <pre>
	 * Desc  X省向无线座机、数据卡发放补贴金额全国排名 tab
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:21:04 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "load_hz_dqfb_sjb_table")
	public Pager load_hz_dqfb_sjb_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = zdbtwgffService.load_hz_dqfb_sjb_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * <pre>
	 * Desc  汇总页-合规性分析-数据表-图表-导出
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:21:04 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "export_hz_qgpm_sjb")
	public  void export_hz_qgpm_sjb(HttpServletRequest request, HttpServletResponse response) throws Exception {
			Map<String, Object> parameterMap = this.getParameterMap(request);
			zdbtwgffService.export_hz_qgpm_sjb(request, response, parameterMap);
	}
	/**
	 * <pre>
	 * Desc  汇总页-合规性分析-数据表-图表-导出
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:21:04 PM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "export_hz_dqfb_sjb")
	public  void export_hz_dqfb_sjb(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		zdbtwgffService.export_hz_dqfb_sjb(request, response, parameterMap);
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
		List<Map<String, Object>> dataRecords = zdbtwgffService.mx_table(pager);
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
		zdbtwgffService.mx_export_btn(request, response, parameterMap);
	}


}
