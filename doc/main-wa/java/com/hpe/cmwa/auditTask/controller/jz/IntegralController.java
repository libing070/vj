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

import com.hpe.cmwa.auditTask.service.jz.IntegralService;
import com.hpe.cmwa.common.CommonResult;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.HelperDate;
import com.hpe.cmwa.util.HelperDouble;
import com.hpe.cmwa.util.Json;
import com.hpe.cmwa.util.Prvd_info;

/**
 * <pre>
 * Desc： 参考例子
 * 访问路径：http://localhost:8080/cmwa/integral/getIntegralPrvdInfo?cmccProvPrvdId='10600'&audTrmStar='201602'&audTrmEnd='201610'
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
@RequestMapping("/integral/")
public class IntegralController extends BaseController {

	@Autowired
	private IntegralService	integralService;

	/**
	 * desc:初始化积分回馈率页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "index")
	public String integral(HttpServletRequest request) {
		
		logger.debug(this.getParameterStr(request));
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		if(request.getParameter("provinceCode").equals("10000")){
			return "auditTask/sjk/jfhgx_qg/jfhklhgx_qg";
		}else{
			return "auditTask/jz/integral/integral";
		}
		
	}
	@RequestMapping(value = "jyindex")
	public String integralJY(HttpServletRequest request) {
		
		logger.debug(this.getParameterStr(request));
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		if(request.getParameter("provinceCode").equals("10000")){
			return "auditTask/sjk/jy_jfhklhgx_qg/jy_jfhklhgx_qg";
		}else{
			return "auditTask/jy/integral/integral";
		}
		
	}
	/**
	 * Desc:积分回馈率趋势图数据
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getIntegralPrvdInfo")
	@ResponseBody
	public String getIntegralPrvdInfo(HttpServletRequest request,HttpServletResponse response) {
		String audTrmStar = formatParameter(this.getParameterMap(request)).get("currSumBeginDate").toString();
		String audTrmEnd = formatParameter(this.getParameterMap(request)).get("currSumEndDate").toString();
		Map<String, Object> requestMap = this.getParameterMap(request);
		//判断开始时间是否是一月份
		if(!audTrmStar.substring(4, 6).equals("01")){
			audTrmStar =Integer.parseInt(audTrmStar.substring(0, 4))+1+"01" ;
			requestMap.remove("currSumBeginDate");
			requestMap.put("currSumBeginDate", audTrmStar);
		}
		
		int starYear = Integer.parseInt(audTrmStar.substring(0, 4));
		int endYear = Integer.parseInt(audTrmEnd.substring(0, 4));
		List<Map<String, Object>> timeMapList = new ArrayList<Map<String,Object>>();
		//曲线图查询参数map
		Map<String, Object> parameterMap = null;
		String provinceCode = requestMap.get("provinceCode").toString();
		for(int i =starYear; i<=endYear; i++){
			parameterMap = new HashMap<String, Object>();
			parameterMap.put("provinceCode", provinceCode);
			parameterMap.put("currSumBeginDate", i+"01");
			if(i==endYear){
				parameterMap.put("currSumEndDate", audTrmEnd);
			}else{
				parameterMap.put("currSumEndDate", i+"12");
			}
			timeMapList.add(parameterMap);
		}
		
		/*查询积分回馈率，返回时间、新增积分、出账收入等值*/
		List<Map<String, Object>> jfhkllist = new ArrayList<Map<String,Object>>();;
		Map<String, Object> jfDate= null;
		for(Map<String, Object> timeMap : timeMapList){
			jfDate = integralService.selectIntegralFeedbackRate(timeMap);
			if(jfDate == null || jfDate.isEmpty()){
				jfDate= new HashMap<String, Object>();
				if(timeMap.get("currSumEndDate").toString().substring(4, 6).equals("12")){
					jfDate.put("audTrm",timeMap.get("currSumEndDate").toString().substring(0, 4));
				}else{
					jfDate.put("audTrm",timeMap.get("currSumEndDate").toString());
				}
				jfDate.put("curmonScoreTol", 0d);
				jfDate.put("merTotFeeTol", 0d);
				jfDate.put("perJfhk", 0d);
			}else{
				if(timeMap.get("currSumEndDate").toString().substring(4, 6).equals("12")){
					jfDate.put("audTrm",timeMap.get("currSumEndDate").toString().substring(0, 4));
				}else{
					jfDate.put("audTrm",timeMap.get("currSumEndDate").toString());
				}
			}
			jfhkllist.add(jfDate);
		}
		
		//时间
		List<String> jftime = new ArrayList<String>();
		//积分回馈率
		List<Double> jfhkl = new ArrayList<Double>();
		//新增积分
		List<Double> xzjf = new ArrayList<Double>();
		//出账收入
		List<Double> czsr = new ArrayList<Double>();
		
		//封装数据
		for(Map<String, Object> map : jfhkllist){
			jftime.add(map.get("audTrm").toString());
			HelperDouble.formatTwoPoint((Double)Double.parseDouble(map.get("perJfhk").toString()));
			jfhkl.add(HelperDouble.formatTwoPoint((Double)Double.parseDouble(map.get("perJfhk").toString())));
			xzjf.add(HelperDouble.formatTwoPoint((Double)Double.parseDouble(map.get("curmonScoreTol").toString())/1000000));
			czsr.add(HelperDouble.formatTwoPoint((Double)Double.parseDouble(map.get("merTotFeeTol").toString())/1000000));
		}
		Map<String, Object> jifen = new HashMap<String, Object>();
		jifen.put("jftime", jftime);
		jifen.put("jfhkl", jfhkl);
		jifen.put("xzjf", xzjf);
		jifen.put("czsr", czsr);
		return CommonResult.success(jifen);
	}

	
	@RequestMapping(value = "getDataAnalysis")
	@ResponseBody
	public Map<String, Object> getDataAnalysis(HttpServletRequest request, HttpServletResponse response) {
	
		//根据省份编号 和 审计时间  获取积分率排名前三的地市
		List<String> threeCityList = integralService.selectThreeCity(formatParameter(this.getParameterMap(request)));
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		//审计时间
		dataMap.put("audTrmEnd", formatParameter(this.getParameterMap(request)).get("currSumEndDate"));
		
		//根据省份编号 和 审计时间 获取分析数据
		List<Map<String, Object>> dataAnalysisList = integralService.selectDataAnalysis(formatParameter(this.getParameterMap(request)));
		Map<String, Object> dataAnalysisMap = new HashMap<String, Object>();
		for(Map<String, Object> infoMap : dataAnalysisList){
			if(infoMap.get("cmccProvPrvdId").toString().equals(this.getParameterMap(request).get("provinceCode").toString())){
				dataAnalysisMap=infoMap;
			}
		}
		
		if(dataAnalysisMap !=null && !dataAnalysisMap.isEmpty()){
			//省份名称
			dataMap.put("provinceCode", this.getParameterMap(request).get("provinceCode"));
			dataMap.put("shortName", dataAnalysisMap.get("shortName"));
			//用户新增积分
			dataMap.put("curmonQtyTol", HelperDouble.formatTwoPoint((Double)Double.parseDouble(dataAnalysisMap.get("curmonQtyTol").toString())/1000000));
			//新增积分总价值
			dataMap.put("curmonScoreTol", HelperDouble.formatTwoPoint((Double)Double.parseDouble(dataAnalysisMap.get("curmonScoreTol").toString())/1000000));
			//全国排名
			dataMap.put("ranking", dataAnalysisMap.get("sort"));
		}else{
			//省份名称
			dataMap.put("provinceCode", this.getParameterMap(request).get("provinceCode"));
			dataMap.put("shortName", "");
			//用户新增积分
			dataMap.put("curmonQtyTol", "暂无数据");
			//新增积分总价值
			dataMap.put("curmonScoreTol", "暂无数据");
			//全国排名
			dataMap.put("ranking", dataMap.get("sort"));
		}
		//排名前三的地市名称
		StringBuffer cityNames = new StringBuffer() ;
		if(threeCityList.size()>0){
			for(String cityName : threeCityList){
				cityNames.append(cityName+",");
			}
			if(cityNames.substring(cityNames.length()-1).equals(",")){
				cityNames = cityNames.deleteCharAt(cityNames.length()-1);
			}
		}
		dataMap.put("threeCity", cityNames.toString());
		
		return dataMap;
	}
	
	/**
	 * 获取本省审计时间的所有城市数据
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "getCityData")
	@ResponseBody
	public Object getCityData(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		Map<String, Object> requestMap =this.getParameterMap(request);
		List<Map<String, Object>> timeMapList = getParamsList(request,
				"currSumBeginDate","currSumEndDate", requestMap.get("provinceCode").toString());
		pager.setParamsList(timeMapList);
		List<Map<String, Object>> cityMapList = integralService.selectAllPrvdCity(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	
	/**
	 * 获取全国从审计开始点都审计结束点所有地市的数据
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "getAllCityData")
	@ResponseBody
	public Object getAllCityData(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		
		Map<String, Object> requestMap =this.getParameterMap(request);
		List<Map<String, Object>> timeMapList = getParamsList(request,
				"currDetBeginDate","currDetEndDate", requestMap.get("provinceCode").toString());
		pager.setParamsList(timeMapList);
		List<Map<String, Object>> cityMapList = integralService.selectAllCity(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}

	private List<Map<String, Object>> getParamsList(HttpServletRequest request,
			String beginDate, String endData, String provinceCode) {
		String audTrmStar = formatParameter(this.getParameterMap(request)).get(beginDate).toString();
		String audTrmEnd = formatParameter(this.getParameterMap(request)).get(endData).toString();
		int starYear = 0;
		if(beginDate.equals("currSumBeginDate")){
			if(audTrmStar.substring(4,6).equals("01")){
				starYear = Integer.parseInt(audTrmStar.substring(0, 4));
			}else{
				starYear = Integer.parseInt(audTrmStar.substring(0, 4))+1;
			}
		}
		starYear = Integer.parseInt(audTrmStar.substring(0, 4));
		int endYear = Integer.parseInt(audTrmEnd.substring(0, 4));
		List<Map<String, Object>> timeMapList = new ArrayList<Map<String,Object>>();
		//曲线图查询参数map
		Map<String, Object> parameterMap = null;
		for(int i =starYear; i<=endYear; i++){
			parameterMap = new HashMap<String, Object>();
			parameterMap.remove(beginDate);
			if(i == starYear){
				parameterMap.put(beginDate, audTrmStar);
			}else{
				parameterMap.put(beginDate, i+"01");
			}
			parameterMap.remove(endData);
			if(i==endYear){
				parameterMap.put(endData, audTrmEnd);
			}else{
				parameterMap.put(endData, i+"12");
			}
			parameterMap.put("provinceCode", provinceCode);
			timeMapList.add(parameterMap);
		}
		return timeMapList;
	}
	/**
	 * 根据省代码和审计时间查询该省地市信息并将信息展示在地图上
	 * @param cmccProvPrvdId
	 * @param audTrmEnd
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getMapData",produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public Object getMapData(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> allCityMapList = integralService.selectAllMapCity(formatParameter(this.getParameterMap(request)));
		List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
		Map<String, Object> value = null;
		for(Map<String, Object> map :allCityMapList){
			value = new HashMap<String, Object>();
			value.put("cityName", map.get("cmccPrvdNmShort"));
			value.put("perJfhk", map.get("perJfhk"));
			value.put("cmccProvId", map.get("cmccProvId"));
			values.add(value);
		}
		result.put("values", values);
		CommonResult commonResult = new CommonResult();
		commonResult.setBody(result);
		return Json.Encode(commonResult);
	}
	
	/**
	 * 导出 该省所有地市数据
	 * @param cmccProvPrvdId
	 * @param audTrmEnd
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping("cityExport")
	@ResponseBody
	public void export(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String, Object> parameterMap = this.getParameterMap(request);
		
		List<Map<String, Object>> timeMapList = getParamsList(request,
				"currSumBeginDate","currSumEndDate", parameterMap.get("provinceCode").toString());
		parameterMap.put("timeMapList", timeMapList);
		integralService.exportPrvdCity(request, response, parameterMap);
	}
	
	@RequestMapping("allCityExport")
	@ResponseBody
	public void exportAllCity(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String, Object> parameterMap = this.getParameterMap(request);
		
		List<Map<String, Object>> timeMapList = getParamsList(request,
				"currDetBeginDate","currDetEndDate", parameterMap.get("provinceCode").toString());
		parameterMap.put("timeMapList", timeMapList);
		integralService.exportAllCity(request, response, parameterMap);
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
