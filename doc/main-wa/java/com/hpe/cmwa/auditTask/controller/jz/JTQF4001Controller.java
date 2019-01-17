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

import com.hpe.cmwa.auditTask.service.jz.JTQF4001Service;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.HelperDate;
import com.hpe.cmwa.util.Prvd_info;

/**
 * <pre>
 * Desc：个人客户欠费controller
 * 访问路径：http://localhost:8080/cmwa/groupArrears/index?cmccProvPrvdId='10600'&audTrmStar='201602'&audTrmEnd='201610'
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
@RequestMapping("/jtqf4001/")
public class JTQF4001Controller extends BaseController {

	@Autowired
	private JTQF4001Service jtqf4001Service;

	/**
	 * desc:初始化集团客户欠费页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "index")
	public String integral(HttpServletRequest request,HttpServletResponse response) {
		String url = "";
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		if(request.getParameter("provinceCode").equals("10000")){
			url = "auditTask/sjk/jtqf/jtqf4001";
		}else{
			url = "auditTask/jz/jtqf/jtqf4001";
		}
		return url;
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
			return "auditTask/sjk/cqgeqfjt/jtqf4001";
		}else{
			return "auditTask/jy/cqgeqfjt/jtqf4001";
		}
		
	}

	
	/**
	 * Desc:欠费波动趋势  欠费账户数
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getBdqsInfo")
	@ResponseBody
	public List<Map<String, Object>> getBdqsFirInfo(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Map<String, Object>> list = jtqf4001Service.selectBdqsInfo(formatParameter(params));
		
		return list;
	}
	
	

	
	/**
	 * Desc:长期欠费  
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getCqqfInfo")
	@ResponseBody
	public List<Map<String, Object>>  getCqqfInfo(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		return jtqf4001Service.selectCqqfInfo(formatParameter(params));
	}
	
	/**
	 * Desc:高额欠费  折线图数据  
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getGeqfInfo")
	@ResponseBody
	public List<Map<String, Object>>  getGeqfInfo(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		return jtqf4001Service.selectGeqfInfo(formatParameter(params));
	}
	
	/**
	 * 获取欠费趋势波动数据表数据
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "getBdqsTableData")
	@ResponseBody
	public Object getBdqsTableData(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		Map<String, Object> params = pager.getParams();
		//params.put("nowDate", ThreeMouthUtils.getThreeMouth());
		pager.setParams(params);
		List<Map<String, Object>> cityMapList = jtqf4001Service.selectBdqsTableData(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	/**
	 * 获取欠费趋势波动数据表数据
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "khDataTable")
	@ResponseBody
	public Object khDataTable(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		Map<String, Object> params = pager.getParams();
		//params.put("nowDate", ThreeMouthUtils.getThreeMouth());
		pager.setParams(params);
		List<Map<String, Object>> cityMapList = jtqf4001Service.khDataTable(pager);
		pager.setDataRows(cityMapList);
		return pager;
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
		return jtqf4001Service.selectBodongzhsMax(parameterMap);
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
		return jtqf4001Service.selectBodongjeMax(parameterMap);
	}
	/**
	 * 欠费波动趋势图  欠费金额审计结论
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "khconclusion")
	@ResponseBody
	public List<Map<String, Object>> khconclusion(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		return jtqf4001Service.khconclusion(parameterMap);
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
		resultList.add(jtqf4001Service.getzhanglingZhs(parameterMap));
		resultList.add(jtqf4001Service.getzhanglingQg(parameterMap));
		return resultList;
	}
	/**
	 * 获取欠费账龄分布数据表数据
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "getZlfbTableData")
	@ResponseBody
	public Object getZlfbTableData(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		Map<String, Object> params = pager.getParams();
		//params.put("nowDate", ThreeMouthUtils.getThreeMouth());
		pager.setParams(params);
		List<Map<String, Object>> cityMapList = jtqf4001Service.selectZlfbTableData(pager);
		pager.setDataRows(cityMapList);
		return pager;
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
		Map<String, Object> map = jtqf4001Service.getzhanglingjielun(parameterMap);
		if(map!=null){
			Map<String, Object> map2 = jtqf4001Service.getQGzhanglingjielun(parameterMap);
			if(map2!=null){
				map.put("acctNumMom", map2.get("acctNumMom"));
				map.put("dbtAmtPer", map2.get("dbtAmtPer"));
			}
		}
		return map;
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
		List<Map<String, Object>> dataRecords = jtqf4001Service.getguimoPagerList(pager);
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
		resultList.add(jtqf4001Service.getguimoZhs(parameterMap));
		resultList.add(jtqf4001Service.getguimoQg(parameterMap));
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
		Map<String, Object> map = jtqf4001Service.getguimojielun(parameterMap);
		if(map!=null){
			Map<String, Object> map2 = jtqf4001Service.getQGguimojielun(parameterMap);
			if(map2!=null){
				map.put("acctNumMom", map2.get("acctNumMom"));
				map.put("dbtAmtPer", map2.get("dbtAmtPer"));
			}
		}
		return map;
	}

	/**
	 * 获取长期欠费数据表数据
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "getCqqfTableData")
	@ResponseBody
	public Object getCqqfTableData(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		Map<String, Object> params = pager.getParams();
		//params.put("nowDate", ThreeMouthUtils.getThreeMouth());
		pager.setParams(params);
		List<Map<String, Object>> cityMapList = jtqf4001Service.selectCqqfTableData(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	/**
	 * 获取长期欠费审计结论
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "getCqqfResult")
	@ResponseBody
	public Map<String, Object> getCqqfResult(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		return jtqf4001Service.selectCqqfResult(formatParameter(params));
	}
	/**
	 * 获取高额欠费审计结论
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "getGeqfResult")
	@ResponseBody
	public Map<String, Object> getGeqfResult(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		return jtqf4001Service.selectGeqfResult(formatParameter(params));
	}
	
	/**
	 * 获取高额欠费数据表数据
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "getGeqfTableData")
	@ResponseBody
	public Object getGeqfTableData(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		Map<String, Object> params = pager.getParams();
		//params.put("nowDate", ThreeMouthUtils.getThreeMouth());
		pager.setParams(params);
		List<Map<String, Object>> cityMapList = jtqf4001Service.selectGeqfTableData(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}

	/**
	 * 获取明细数据表数据
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "getDetialTableData")
	@ResponseBody
	public Object getDetialTableData(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		
		pager.setParams(formatParameter(this.getParameterMap(request)));
		Map<String, Object> params = pager.getParams();
		pager.setParams(params);
		List<Map<String, Object>> cityMapList = jtqf4001Service.selectDetialTableData(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	
	/**
	 * 波动趋势图 数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
    @RequestMapping(value = "exportkhList")
    public void exportkhList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        jtqf4001Service.exportkhList(request, response, parameterMap);
    }
	/**
	 * 波动趋势图 数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
    @RequestMapping(value = "exportBodongList")
    public void exportBodongList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        jtqf4001Service.exportBodongList(request, response, parameterMap);
    }
	/**
	 * 账龄 数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "exportZhangLingList")
	public void exportZhangLingList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		jtqf4001Service.exportZhangLingList(request, response, parameterMap);
	}
	/**
	 * 欠费规模分布  数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "exportGuiMoList")
	public void exportGuiMoList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		jtqf4001Service.exportguimoList(request, response, parameterMap);
	}
	/**
	 * 长期欠费  数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "exportChangQiList")
	public void exportChangQiList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		jtqf4001Service.exportChangQiList(request, response, parameterMap);
	}
	/**
	 * 高额欠费  数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "exportGaoEList")
	public void exportGaoEList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		jtqf4001Service.exportGaoEList(request, response, parameterMap);
	}
	/**
	 * 明细  数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "exportDetailTable")
	public void exportDetailTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		jtqf4001Service.exportDetailTable(request, response, parameterMap);
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
	 * Desc  根据需要对页面参数进行格式化
	 * @param requestParamsMap
	 * @author peter.fu
	 * Nov 25, 2016 5:27:07 PM
	 * </pre>
	 */
	private Map<String, Object> formatParameter(Map<String, Object> requestParamsMap) {

		if (requestParamsMap == null) {
			return null;
		}else{
			//格式化时间等
			for(String key : requestParamsMap.keySet()){
				if(key.equals("currSumBeginDate") || key.equals("currSumEndDate") || key.equals("currDetBeginDate") || key.equals("currDetEndDate")){
					HelperDate.formatDateStrToStr(requestParamsMap.get(key).toString(), "yyyyMM");
				}
			}
		}
		
		return requestParamsMap;
	}
}
