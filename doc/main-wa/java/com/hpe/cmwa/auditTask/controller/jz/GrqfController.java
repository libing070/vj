/**
 * com.hpe.cmwa.controller.DemoController.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.controller.jz;

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

import com.hpe.cmwa.auditTask.service.jz.GrqfService;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.Prvd_info;

/**
 * <pre>
 * Desc：个人客户欠费controller
 * 访问路径：http://localhost:8080/cmwa/personTuition/index?cmccProvPrvdId='10600'&audTrmStar='201602'&audTrmEnd='201610'
 * @author   xinguangchuan
 * @refactor xinguangchuan
 * @date     Nov 16, 2016 10:17:41 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Nov 16, 2016 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/grqf/")
public class GrqfController extends BaseController {

	@Autowired
	private GrqfService grqfService;

	/**
	 * desc:初始化个人欠费页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "index")
	public String integral(HttpServletRequest request,HttpServletResponse response) {
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		if(request.getParameter("provinceCode").equals("10000")){
			return "auditTask/sjk/grqf_qg/grqf_qg";
		}else{
			return "auditTask/jz/grqf/grqf";
		}
		
	}
	
	
	/**
	 * 欠费波动趋势 欠费账户数
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getbodongZhs")
	@ResponseBody
	public List<Map<String, Object>> getbodongZhs(HttpServletRequest request) {
		 Map<String, Object> parameterMap = this.getParameterMap(request);
		 return grqfService.selectBodongZhs(parameterMap);
	}
	
	/**
	 * 欠费波动趋势图  欠费账户数审计结论
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getBodongzhsMax")
	@ResponseBody
	public Map<String, Object> getBodongzhsMax(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		return grqfService.selectBodongzhsMax(parameterMap);
	}
	
	/**
	 * 欠费波动趋势图  欠费金额审计结论
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getBodongjeMax")
	@ResponseBody
	public Map<String, Object> getBodongjeMax(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		return grqfService.selectBodongjeMax(parameterMap);
	}
	
	/**
	 *  波动趋势图 数据表展示
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/getBodongPagerList")
    public Pager getBodongPagerList(HttpServletRequest request, Pager pager) {
        pager.setParams(this.getParameterMap(request));
        List<Map<String, Object>> dataRecords = grqfService.getBodongPagerList(pager);
        pager.setDataRows(dataRecords);
        return pager;
    }
	
	/**
	 * 波动趋势图 数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
    @RequestMapping(value = "/exportBodongList")
    public void exportSumList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        grqfService.exportBodongList(request, response, parameterMap);
    }
	
	/**
	 *  欠费账龄分布  数据表展示
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getzhanglingPagerList")
	public Pager getzhanglingPagerList(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = grqfService.getzhanglingPagerList(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 账龄分布 欠费账户数
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getzhanglingZhs")
	@ResponseBody
	public List<Map<String,Object>> getzhanglingZhs(HttpServletRequest request) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String, Object> parameterMap = this.getParameterMap(request);
		resultList.add(grqfService.getzhanglingZhs(parameterMap));
		resultList.add(grqfService.getzhanglingQg(parameterMap));
		return resultList;
	}
	
	/**
	 * 账龄分布 审计结论
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getzhanglingjielun")
	@ResponseBody
	public Map<String, Object> getzhanglingjielun(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		return grqfService.getzhanglingjielun(parameterMap);
	}
	/**
	 * 账龄分布 全国审计结论 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getQGzhanglingjielun")
	@ResponseBody
	public Map<String, Object> getQGzhanglingjielun(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		return grqfService.getQGzhanglingjielun(parameterMap);
	}
	
	/**
	 * 账龄 数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/exportZhangLingList")
	public void exportZhangLingList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		grqfService.exportZhangLingList(request, response, parameterMap);
	}
	
	
	/**
	 *  欠费规模分布  数据表展示
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getGuiMoPagerList")
	public Pager getGuiMoPagerList(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = grqfService.getguimoPagerList(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 欠费规模分布 欠费账户数
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getGuiMoZhs")
	@ResponseBody
	public List<Map<String,Object>> getGuiMoZhs(HttpServletRequest request) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String, Object> parameterMap = this.getParameterMap(request);
		resultList.add(grqfService.getguimoZhs(parameterMap));
		resultList.add(grqfService.getguimoQg(parameterMap));
		return resultList;
	}
	
	/**
	 * 欠费规模分布  审计结论
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getGuiMojielun")
	@ResponseBody
	public Map<String, Object> getGuiMojielun(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		return grqfService.getguimojielun(parameterMap);
	}
	/**
	 * 欠费规模分布  全国审计结论 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getQGGuiMojielun")
	@ResponseBody
	public Map<String, Object> getQGGuiMojielun(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		return grqfService.getQGguimojielun(parameterMap);
	}
	
	/**
	 * 欠费规模分布  数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/exportGuiMoList")
	public void exportGuiMoList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		grqfService.exportguimoList(request, response, parameterMap);
	}
	
	/**
	 * 长期欠费管控风险地域分布 地域分布图
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getguankongZhs")
	@ResponseBody
	public List<Object> getguankongZhs(HttpServletRequest request) {
		List<Object> resultList = new ArrayList<Object>();
		Map<String, Object> parameterMap = this.getParameterMap(request);
		resultList.add(grqfService.getguankongZhs(parameterMap));
		resultList.add(grqfService.getQGguankongZhs(parameterMap));
		return resultList;
	}
	/**
	 *  长期欠费管控风险地域分布  数据表展示
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getGuankongPagerList")
	public Pager getguankongPagerList(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = grqfService.getguankongPagerList(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 长期欠费管控风险地域分布  审计结论
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getGuankongjielun")
	@ResponseBody
	public Map<String, Object> getguankongjielun(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		return grqfService.getguankongjielun(parameterMap);
	}
	/**
	 * 长期欠费管控风险地域分布  全国审计结论 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getGuankongThreeCity")
	@ResponseBody
	public List<Map<String, Object>> getguankongThreeCity(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		return grqfService.getguankongThreeCity(parameterMap);
	}
	
	/**
	 * 长期欠费管控风险地域分布 数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/exportGuankongList")
	public void exportGuankongList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		grqfService.exportGuankongList(request, response, parameterMap);
	}
	/**
	 * 高额欠费管控风险地域分布 地域分布图
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getgeguankongZhs")
	@ResponseBody
	public List<Object> getgeguankongZhs(HttpServletRequest request) {
		List<Object> resultList = new ArrayList<Object>();
		Map<String, Object> parameterMap = this.getParameterMap(request);
		resultList.add(grqfService.getgeguankongZhs(parameterMap));
		resultList.add(grqfService.getgeQGguankongZhs(parameterMap));
		return resultList;
	}
	/**
	 *  高额欠费管控风险地域分布  数据表展示
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getgeGuankongPagerList")
	public Pager getgeguankongPagerList(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = grqfService.getgeguankongPagerList(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 高额欠费管控风险地域分布  审计结论
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getgeGuankongjielun")
	@ResponseBody
	public Map<String, Object> getgeguankongjielun(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		return grqfService.getgeguankongjielun(parameterMap);
	}
	/**
	 * 高额欠费管控风险地域分布  全国审计结论 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getgeGuankongThreeCity")
	@ResponseBody
	public List<Map<String, Object>> getgeguankongThreeCity(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		return grqfService.getgeguankongThreeCity(parameterMap);
	}
	
	/**
	 * 高额欠费管控风险地域分布 数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/exportgeGuankongList")
	public void exportgeGuankongList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		grqfService.exportgeGuankongList(request, response, parameterMap);
	}
	
	
	/**
	 *  明细  数据表展示
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDetList")
	public Pager getDetList(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = grqfService.getDetList(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 明细 数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/exprotDetList")
	public void exprotDetList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		grqfService.exprotDetList(request, response, parameterMap);
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
}
