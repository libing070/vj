package com.hpe.cmwa.auditTask.controller.jy;

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

import com.hpe.cmwa.auditTask.service.jy.YJKWCZService;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.Prvd_info;

/**
 * 
 * <pre>
 * Desc： 
 * @author jh
 * @refactor jh
 * @date   2017-3-29 下午2:05:57
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-3-29 	   jh 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping(value="yjkwcz")
public class YJKWCZController extends BaseController {
	
	@Autowired
	private YJKWCZService yjkwczService;

	/**
	 * 
	 * <pre>
	 * Desc  初始化有价卡赠送未充值异常页面
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-3-29 下午2:08:36
	 * </pre>
	 */
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request) {

		logger.debug(this.getParameterStr(request));
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		request.getSession().getAttribute("provinceCode");
		if(request.getParameter("provinceCode").equals("10000")){
			return "auditTask/sjk/yjkzsxgczblyc_qg/zsyjkwczblyc_qg";
		}else{
			return "auditTask/jy/yjkzscz/yjkwcz";
		}
		
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  赠送有价卡未充值比例异常 省环形图 Controller
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-3-30 下午8:08:07
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="hz_yc_chart")
	public List<Map<String, Object>> hz_yc_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		return yjkwczService.hz_yc_chart(params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  赠送有价卡未充值比例异常 地市环形图 Controller
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-3-30 下午8:09:13
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="hz_yc_city_chart")
	public List<Map<String, Object>> hz_yc_city_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		return yjkwczService.hz_yc_city_chart(params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 省Controller
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 上午11:05:49
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="hz_tj_chart")
	public List<Map<String, Object>> hz_tj_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		return yjkwczService.hz_tj_chart(params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 地市Controller
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 上午11:06:18
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="hz_tj_city_chart")
	public List<Map<String, Object>> hz_tj_city_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		return yjkwczService.hz_tj_city_chart(params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 省结论Controller
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 下午1:14:21
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="hz_tj_table_conclusion")
	public List<Map<String, Object>> hz_tj_table_conclusion(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		return yjkwczService.hz_tj_table_conclusion(params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 地市结论Controller
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 下午1:14:39
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="hz_tj_city_table_conclusion")
	public List<Map<String, Object>> hz_tj_city_table_conclusion(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		return yjkwczService.hz_tj_city_table_conclusion(params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 省数据表Controller
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 下午1:14:21
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="hz_tj_table")
	public Pager hz_tj_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = yjkwczService.hz_tj_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 地市数据表Controller
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 下午1:14:39
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="hz_tj_city_table")
	public Pager hz_tj_city_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = yjkwczService.hz_tj_city_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 导出地市数据表Controller
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 下午1:14:39
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="hz_tj_table_export")
	public void hz_tj_table_export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = this.getParameterMap(request);
		yjkwczService.hz_tj_table_export(request,response,params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 导出省数据表Controller
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 下午1:14:21
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="hz_tj_city_table_export")
	public void hz_tj_city_table_export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = this.getParameterMap(request);
		yjkwczService.hz_tj_city_table_export(request,response,params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  明细数据
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-5 下午3:09:16
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="mx_table")
	public Pager mx_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords =yjkwczService.mx_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 导出省数据表Controller
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 下午1:14:21
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="mx_table_export")
	public void mx_table_export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = this.getParameterMap(request);
		yjkwczService.mx_table_export(request,response,params);
	}
	
	/***
	 * 
	 * <pre>
	 * Desc  有价卡赠送未充值异常
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-3-30 上午11:23:15
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
	
}
