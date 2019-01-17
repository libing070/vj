/**
 * com.hpe.cmwa.auditTask.controller.jz.JKYWHGXController.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.controller.jy;

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

import com.hpe.cmwa.auditTask.service.jy.SHQDCJYCService;
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
@RequestMapping("/shqdcjyc/")
public class SHQDCJYCController extends BaseController {

	@Autowired
	private SHQDCJYCService	shqdcjycService;

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
			return "auditTask/sjk/shqdfwfyc/shqdfwfcj";
		}else{
			return "auditTask/jy/shqdfwfyc/shqdfwfcj";
		}
	}

	/**
	 * 
	 * <pre>
	 * Desc  Charts1
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-13 下午4:14:14
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="hz_cj_chart")
	public List<Map<String, Object>> hz_cj_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		return shqdcjycService.hz_cj_chart(params);
	}
	
	/**
     *  table1
     * <pre>
     * Desc  
     * @param request
     * @param pager
     * @return
     * @author jh
     * @refactor jh
     * @date   2017-4-13 下午5:10:52
     * </pre>
     */
    @ResponseBody
	@RequestMapping(value="hz_cj_table")
	public Pager hz_cj_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = shqdcjycService.hz_cj_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
    /**
     * 
     * <pre>
     * Desc  批量缴费波动趋势 table 导出
     * @param request
     * @param response
     * @throws Exception
     * @author jh
     * @refactor jh
     * @date   2017-4-13 下午5:11:13
     * </pre>
     */
	@ResponseBody
	@RequestMapping(value="hz_cj_table_export")
	public void hz_cj_table_export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = this.getParameterMap(request);
		shqdcjycService.hz_cj_table_export(request,response,params);
	}
	
	
	@ResponseBody
	@RequestMapping(value="hz_hb_chart")
	public Map<String, Object> ten_qd_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Map<String, Object>> busiChnlIdList = shqdcjycService.hz_hb_chart1(params);
		List<String> busiChnlIds = new ArrayList<String>();
		Map<String, Object> map = new HashMap<String, Object>();
		if(busiChnlIdList.size()>0){
			for(Map<String, Object> busiChnlId : busiChnlIdList){
				busiChnlIds.add((String) busiChnlId.get("sell_fee_subj"));
			}
			params.put("sell_fee_subjs", busiChnlIds);
			List<Map<String, Object>> list = shqdcjycService.hz_hb_chart2(params);
			map.put("sell_fee_subjs", busiChnlIdList);
			map.put("list", list);
		}
		return map;
	}
	/**
	 * 
	 * <pre>
	 * Desc 
	 * @param request
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-14 下午4:36:53
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="hz_hb_table")
	public Pager hz_hb_fc_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = shqdcjycService.hz_hb_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
    /**
     * 渠道批量缴费业务统计 table导出
     * <pre>
     * Desc  
     * @param request
     * @param response
     * @throws Exception
     * @author jh
     * @refactor jh
     * @date   2017-4-14 下午4:36:40
     * </pre>
     */
	@ResponseBody
	@RequestMapping(value="hz_hb_table_export")
	public void hz_hb_table_export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = this.getParameterMap(request);
		shqdcjycService.hz_hb_table_export(request,response,params);
	}
	
	
	
	
	
	
	
	
	
	@ResponseBody
	@RequestMapping(value="hz_fc_chart")
	public Map<String, Object> hz_fc_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Map<String, Object>> busiChnlIdList = shqdcjycService.hz_fc_chart1(params);
		List<String> sell_fee_subjs = new ArrayList<String>();
		Map<String, Object> map = new HashMap<String, Object>();
		if(busiChnlIdList.size()>0){
			for(Map<String, Object> busiChnlId : busiChnlIdList){
				sell_fee_subjs.add((String) busiChnlId.get("sell_fee_subj"));
			}
			params.put("sell_fee_subjs", sell_fee_subjs);
			List<Map<String, Object>> list = shqdcjycService.hz_fc_chart2(params);
			map.put("sell_fee_subjs", busiChnlIdList);
			map.put("list", list);
		}
		return map;
	}
	/**
	 * 
	 * <pre>
	 * Desc 
	 * @param request
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-14 下午4:36:53
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="hz_fc_table")
	public Pager hz_fc_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = shqdcjycService.hz_fc_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
    /**
     * 渠道批量缴费业务统计 table导出
     * <pre>
     * Desc  
     * @param request
     * @param response
     * @throws Exception
     * @author jh
     * @refactor jh
     * @date   2017-4-14 下午4:36:40
     * </pre>
     */
	@ResponseBody
	@RequestMapping(value="hz_fc_table_export")
	public void hz_fc_table_export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = this.getParameterMap(request);
		shqdcjycService.hz_fc_table_export(request,response,params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  明细
	 * @param request
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-14 下午5:49:45
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="mx_table")
	public Pager mx_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords =shqdcjycService.mx_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  导出明细数据表Controller
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
		shqdcjycService.mx_table_export(request,response,params);
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
		defaultParams.put("busiTypeList", dict.getList("busiType"));
		return defaultParams;
	}
	

}
