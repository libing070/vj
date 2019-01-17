/**
 * com.hpe.cmwa.controller.DemoController.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.controller.jz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.jz.KeepMachineService;
import com.hpe.cmwa.common.CommonResult;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.HelperDate;
import com.hpe.cmwa.util.Json;
import com.hpe.cmwa.util.Prvd_info;

/**
 * <pre>
 * Desc：养机套利controller
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
@RequestMapping("/keepMachine/")
public class KeepMachineController extends BaseController {

	@Autowired
	private KeepMachineService keepMachineService;

	/**
	 * desc:初始化违规套利页面
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
		//页面类型  1：养机套利页面  2：窜货套利页面
		//request.getSession().setAttribute("pageType", "1");
		if(request.getParameter("provinceCode").equals("10000")){
			return "auditTask/sjk/zdwgxs/yjtl_qg";
		}else{
			return "auditTask/jz/keepMachine/keepMachine";
		}
		
	}
	@RequestMapping(value = "getPersonPrvdInfoCon")
	@ResponseBody
	public Object getPersonPrvdInfoCon(HttpServletRequest request,HttpServletResponse response){
		String audTrmStar = formatParameter(this.getParameterMap(request)).get("currSumBeginDate").toString();
		String audTrmEnd = formatParameter(this.getParameterMap(request)).get("currSumEndDate").toString();
		
		List<String> dataList = new ArrayList<String>();
		int m = Integer.parseInt(audTrmStar.substring(0,4));
		int n = Integer.parseInt(audTrmEnd.substring(0,4));
		int timeStar = Integer.parseInt(audTrmStar);
		int timeEnd = Integer.parseInt(audTrmEnd);
		for (int i = m; i <= n; i++) {
			for(int j = 0; j<12;j++){
				dataList.add(timeStar+"");
				if(timeStar == Integer.parseInt(i + "12") || timeStar == timeEnd){
					timeStar = Integer.parseInt(i+1+"01");
					break;
				}
				timeStar++;
			}
		}
		Map<String, Object> listCon = keepMachineService.selectIntegralFeedbackRateCon(formatParameter(this.getParameterMap(request)));
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("listCon", listCon);
		return returnMap;
	}
	
	
	/**
	 * Desc:违规套利趋势图数据
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getPersonPrvdInfo")
	@ResponseBody
	public String getPersonPrvdInfo(HttpServletRequest request,HttpServletResponse response) {
		String audTrmStar = formatParameter(this.getParameterMap(request)).get("currSumBeginDate").toString();
		String audTrmEnd = formatParameter(this.getParameterMap(request)).get("currSumEndDate").toString();
		
		List<String> dataList = new ArrayList<String>();
		int m = Integer.parseInt(audTrmStar.substring(0,4));
		int n = Integer.parseInt(audTrmEnd.substring(0,4));
		int timeStar = Integer.parseInt(audTrmStar);
		int timeEnd = Integer.parseInt(audTrmEnd);
		for (int i = m; i <= n; i++) {
			for(int j = 0; j<12;j++){
				dataList.add(timeStar+"");
				if(timeStar == Integer.parseInt(i + "12") || timeStar == timeEnd){
					timeStar = Integer.parseInt(i+1+"01");
					break;
				}
				timeStar++;
			}
		}
		
		List<Map<String, Object>> list = keepMachineService.selectIntegralFeedbackRate(formatParameter(this.getParameterMap(request)));
		List<Map<String, Object>> lineList = new ArrayList<Map<String,Object>>();
		//时间
		List<String> jftime = new ArrayList<String>();
		//违规数量占比=违规数量/总数量
		List<Double> yhlzb = new ArrayList<Double>();
		//违规金额占比=违规金额/总金额
		List<Double> jezb = new ArrayList<Double>();
		//封装数据
		
		StringBuffer timeStr = new StringBuffer();
		if(list.size()>0 && list.get(0) != null){
			Iterator<Map<String, Object>> it = list.iterator();
			int flagSize = 0;
			while(it.hasNext()){
				Map<String, Object> next = it.next();
				lineList.add(next);
				timeStr.append(next.get("audTrm"));
				flagSize ++ ;
				if(flagSize == list.size()){
					for(String xtext : dataList){
						if(!timeStr.toString().contains(xtext)){
							Map<String, Object> nextMap = new HashMap<String, Object>();
							nextMap.put("audTrm", xtext);
							nextMap.put("errNum", 0.0000);
							nextMap.put("tolNum", 0);
							nextMap.put("errAmt", 0.0000);
							nextMap.put("tolAmt", 0.0000);
							nextMap.put("amtPer", 0.0000);
							nextMap.put("numPer", 0.0000);
							lineList.add(nextMap);
						}
					}
				}
				
				for (int i = 0; i < lineList.size() - 1; i++) {
					for (int j = 0; j < lineList.size() - i-1; j++) {
						Map<String, Object> a;
						if (Integer.parseInt(lineList.get(j).get("audTrm").toString()) > Integer.parseInt(lineList.get(j+1).get("audTrm").toString())) { // 比较两个整数的大小

							a = lineList.get(j);
							lineList.set((j), lineList.get(j+1));
							lineList.set(j+1, a);
						}
					}
				}
				
			}
		}else{
			for(String xtext : dataList){
				if(!timeStr.toString().contains(xtext)){
					Map<String, Object> nextMap = new HashMap<String, Object>();
					nextMap.put("audTrm", xtext);
					nextMap.put("errNum", 0.0000);
					nextMap.put("tolNum", 0);
					nextMap.put("errAmt", 0.0000);
					nextMap.put("numPer", 0.0000);
					nextMap.put("amtPer", 0.0000);
					nextMap.put("tolAmt", 0.0000);
					lineList.add(nextMap);
				}
			}
		}
		
		for(Map<String, Object> map : lineList){
			jftime.add(map.get("audTrm").toString());
			yhlzb.add(Double.parseDouble(map.get("numPer").toString())*100);
			jezb.add(Double.parseDouble(map.get("amtPer").toString())*100);
		}
		
		Map<String, Object> jifen = new HashMap<String, Object>();
		jifen.put("jftime", jftime);
		jifen.put("yhlzb", yhlzb);
		jifen.put("jezb", jezb);
		return CommonResult.success(jifen);
	}

	
	@RequestMapping(value = "getDataAnalysis")
	@ResponseBody
	public Map<String, Object> getDataAnalysis(HttpServletRequest request,HttpServletResponse response) {
	
		//根据省份编号 和 审计时间  获取积分率排名前三的省份
		List<String> threeCityList = keepMachineService.selectThreeCity(formatParameter(this.getParameterMap(request)));
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		//审计时间
		dataMap.put("sjtime", formatParameter(this.getParameterMap(request)).get("currSumEndDate"));
		//根据省份编号 和 审计时间 获取分析数据
		List<Map<String, Object>> dataAnalysisList = keepMachineService.selectDataAnalysis(formatParameter(this.getParameterMap(request)));
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
			//违规数量
			dataMap.put("sumCust1", dataAnalysisMap.get("errNum").toString());
			//违规金额
			dataMap.put("sumDbtAmt", dataAnalysisMap.get("errAmt").toString());
			//全国排名
			dataMap.put("ranking", dataAnalysisMap.get("sort"));
		}else{
			//省份名称
			dataMap.put("provinceCode", this.getParameterMap(request).get("provinceCode"));
			dataMap.put("shortName", "");
			//违规数量
			dataMap.put("sumCust1", "暂无数据");
			//违规金额
			dataMap.put("sumDbtAmt", "暂无数据");
			//全国排名
			dataMap.put("ranking", "");
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
		dataMap.put("threePrvd", cityNames.toString());
		
		//异常欠费金额比例min == 全国异常欠费金额比例
		Double chinScale = keepMachineService.getAllChinaScal(formatParameter(this.getParameterMap(request)));
		if(chinScale == null){
			chinScale=0d;
		}
		dataMap.put("scaleMin", chinScale*100);
		//异常欠费金额比例max == 全国异常欠费金额比例* 5
		Double scaleMax = chinScale*5;
		dataMap.put("scaleMax", scaleMax*100);
		
		return dataMap;
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
		List<Map<String, Object>> allCityMapList = keepMachineService.selectAllMapCity(formatParameter(this.getParameterMap(request)));
		List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
		Map<String, Object> value = null;
		for(Map<String, Object> map :allCityMapList){
			value = new HashMap<String, Object>();
			value.put("cityName", map.get("cmccPrvdNmShort"));
			value.put("wgslzb", Double.parseDouble(map.get("numPer").toString())*100);//违规数量占比
			value.put("cmccProvId", map.get("cmccProvId"));
			values.add(value);
		}
		
		//异常欠费金额比例min == 全国异常欠费金额比例
		Double chinScale = keepMachineService.getAllChinaScal(formatParameter(this.getParameterMap(request)));
		if(chinScale == null){
			chinScale=0d;
		}
		result.put("scaleMin", chinScale*100);
		//异常欠费金额比例max == 全国异常欠费金额比例* 5
		Double scaleMax = chinScale*5;
		result.put("scaleMax", scaleMax*100);
		
		result.put("values", values);
		CommonResult commonResult = new CommonResult();
		commonResult.setBody(result);
		return Json.Encode(commonResult);
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
		//defaultParams.put("pageType", sessoin.getAttribute("pageType"));
		// 清单参数默认值
		defaultParams.put("currDetBeginDate", sessoin.getAttribute("beforeAcctMonth"));
		defaultParams.put("currDetEndDate", sessoin.getAttribute("endAcctMonth"));
		defaultParams.put("currCityType", this.getCityObjectList(sessoin.getAttribute("provinceCode") + ""));
		defaultParams.put("currChnlType", dict.getList("chnlType"));
		defaultParams.put("currInfractionFlag", dict.getList("infractionFlag"));
		
		//异常欠费金额比例min == 全国异常欠费金额比例
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("currSumBeginDate", sessoin.getAttribute("beforeAcctMonth"));
		params.put("currSumEndDate", sessoin.getAttribute("endAcctMonth"));
		params.put("pageType", this.getParameterMap(request).get("pageType"));
		Double chinScale = keepMachineService.getAllChinaScal(params);
		if(chinScale == null){
			chinScale = 0d;
		}
		defaultParams.put("scaleMin", chinScale*100);
		//异常欠费金额比例max == 全国异常欠费金额比例* 5
		Double scaleMax = chinScale*5;
		defaultParams.put("scaleMax", scaleMax*100);
		
		return defaultParams;
	}
	
	/**
	 * 获取本省审计时间的所有城市数据
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "getCityPersonData")
	@ResponseBody
	public Object getCityPersonData(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		List<Map<String, Object>> cityMapList = keepMachineService.selectAllCityPerson(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	
	
	
	/**
	 * 导出 该省所有地市数据
	 * @param cmccProvPrvdId
	 * @param audTrmEnd
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("exportAllCityPerson")
	@ResponseBody
	public void exportAllCityPerson(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String, Object> parameterMap = this.getParameterMap(request);
		keepMachineService.exportAllCityPerson(request, response, parameterMap);
	}
	/**
	 * 获取全国从审计开始点都审计结束点所有养机套利数据
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "selectAllCinaPerson")
	@ResponseBody
	public Object selectAllCinaPerson(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		List<Map<String, Object>> cityMapList = keepMachineService.selectAllCinaPerson(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	/**
	 * 导出全国所有养机套利清单
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @param cityCode
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("exportAllChinaPerson")
	@ResponseBody
	public void exportAllChinaPerson(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String, Object> parameterMap = this.getParameterMap(request);
		keepMachineService.exportAllChinaPerson(request, response, parameterMap);
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
