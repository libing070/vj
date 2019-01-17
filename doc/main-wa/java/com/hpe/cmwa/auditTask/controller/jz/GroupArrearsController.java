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

import com.hpe.cmwa.auditTask.service.jz.GroupArrearsService;
import com.hpe.cmwa.common.CommonResult;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.HelperDate;
import com.hpe.cmwa.util.HelperDouble;
import com.hpe.cmwa.util.Json;
import com.hpe.cmwa.util.Prvd_info;
import com.hpe.cmwa.util.ThreeMouthUtils;

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
@RequestMapping("/groupArrears/")
public class GroupArrearsController extends BaseController {

	@Autowired
	private GroupArrearsService groupArrearsService;

	/**
	 * desc:初始化集团客户欠费页面
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
		return "auditTask/jz/groupArrears/groupArrears";
	}
	
	/**
	 * Desc:集团客户欠费趋势图数据
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getPersonPrvdInfo")
	@ResponseBody
	public String getPersonPrvdInfo(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		params.put("nowDate", ThreeMouthUtils.getThreeMouth());
		List<Map<String, Object>> list = groupArrearsService.selectIntegralFeedbackRate(formatParameter(params));
		//时间
		List<String> jftime = new ArrayList<String>();
		//欠费集团客户数
		List<Double> yhlzb = new ArrayList<Double>();
		//欠费总金额
		List<Double> jezb = new ArrayList<Double>();
		//封装数据
		for(Map<String, Object> map : list){
			jftime.add(map.get("acctPrdYtm").toString());
			yhlzb.add(HelperDouble.formatTwoPoint((Double)Double.parseDouble(map.get("sumCustId").toString())));
			jezb.add(HelperDouble.formatTwoPoint((Double)Double.parseDouble(map.get("sumDbtAmt").toString())));
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
		Map<String, Object> params = this.getParameterMap(request);
		params.put("nowDate", ThreeMouthUtils.getThreeMouth());
		List<String> threeCityList = groupArrearsService.selectThreeCity(formatParameter(params));
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		//审计时间
		dataMap.put("sjtime", this.getParameterMap(request).get("currSumEndDate"));
		
		//根据省份编号 和 审计时间 获取分析数据
		List<Map<String, Object>> dataAnalysisList = groupArrearsService.selectDataAnalysis(formatParameter(params));
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
			//异常欠费用户数
			dataMap.put("tolNum", dataAnalysisMap.get("sumCustId").toString());
			//异常欠费历史总金额
			dataMap.put("tolAmt", dataAnalysisMap.get("sumDbtAmt").toString());
			//全国排名
			dataMap.put("ranking", dataAnalysisMap.get("sort"));
		}else{
			//省份名称
			dataMap.put("provinceCode", this.getParameterMap(request).get("provinceCode"));
			dataMap.put("shortName", "");
			//异常欠费用户数
			dataMap.put("tolNum", "暂无数据");
			//异常欠费历史总金额
			dataMap.put("tolAmt","暂无数据");
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
		dataMap.put("threeCity", cityNames.toString());
		
		//欠费总金额min == 欠费总金额平均数
		params.put("acctPrdYtm", ThreeMouthUtils.getFirthMouth());
		Double chinScale = groupArrearsService.getAllChinaScal(formatParameter(params));
		if(chinScale == null){
			chinScale =0d;
		}
		dataMap.put("scaleMin", chinScale*100);
		//欠费总金额max == 欠费总金额平均数* 2
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
		Map<String, Object> params = this.getParameterMap(request);
		params.put("nowDate", ThreeMouthUtils.getThreeMouth());
		List<Map<String, Object>> allCityMapList = groupArrearsService.selectAllMapCity(formatParameter(params));
		List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
		Map<String, Object> value = null;
		for(Map<String, Object> map :allCityMapList){
			value = new HashMap<String, Object>();
			value.put("cityName", map.get("cmccPrvdNmShort"));
			value.put("amtPer", map.get("sumDbtAmtPer"));//异常欠费金额占比
			value.put("cmccProvId", map.get("cmccProvId"));
			values.add(value);
		}
		//异常欠费金额比例min == 全国异常欠费金额比例
		Map<String, Object> dataMap = new HashMap<String, Object>();
		params.put("acctPrdYtm", ThreeMouthUtils.getFirthMouth());
		Double chinScale = groupArrearsService.getAllChinaScal(formatParameter(params));
		if(chinScale == null){
			chinScale =0d;
		}
		result.put("scaleMin", chinScale*100);
		//异常欠费金额比例max == 全国异常欠费金额比例* 5
		Double scaleMax = chinScale*5;
		result.put("scaleMax", scaleMax*100);
		values.add(dataMap);
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
		// 清单参数默认值
		defaultParams.put("currDetBeginDate", sessoin.getAttribute("beforeAcctMonth"));
		defaultParams.put("currDetEndDate", sessoin.getAttribute("endAcctMonth"));
		defaultParams.put("currCityType", this.getCityObjectList(sessoin.getAttribute("provinceCode") + ""));
		defaultParams.put("currKHStatus", dict.getList("custStatTypCd"));
		defaultParams.put("currKHLvl", dict.getList("orgCustLvl"));
		//异常欠费金额比例min == 全国异常欠费金额比例
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("currSumEndDate", sessoin.getAttribute("endAcctMonth"));
		params.put("currSumBeginDate", sessoin.getAttribute("beforeAcctMonth"));
		params.put("currSumEndDate", sessoin.getAttribute("endAcctMonth"));
		params.put("nowDate", ThreeMouthUtils.getThreeMouth());
		Double chinScale = groupArrearsService.getAllChinaScal(params);
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
		Map<String, Object> params = pager.getParams();
		params.put("nowDate", ThreeMouthUtils.getThreeMouth());
		pager.setParams(params);
		List<Map<String, Object>> cityMapList = groupArrearsService.selectAllCityPerson(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	
	
	
	/**
	 * 导出 该省所有地市数据
	 * @param cmccProvPrvdId
	 * @param audTrmEnd
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping("exportAllCityPerson")
	@ResponseBody
	public void exportAllCityPerson(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String, Object> parameterMap = this.getParameterMap(request);
		parameterMap.put("nowDate", ThreeMouthUtils.getThreeMouth());
		groupArrearsService.exportAllCityPerson(request, response, parameterMap);
	}
	/**
	 * 获取全国从审计开始点都审计结束点所有个人客户欠费数据
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "selectAllCinaPerson")
	@ResponseBody
	public Object selectAllCinaPerson(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		Map<String, Object> params = pager.getParams();
		params.put("nowDate", ThreeMouthUtils.getThreeMouth());
		pager.setParams(params);
		List<Map<String, Object>> cityMapList = groupArrearsService.selectAllCinaPerson(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	/**
	 * 导出全国所有个人客户欠费清单
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @param cityCode
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping("exportAllChinaPerson")
	@ResponseBody
	public void exportAllChinaPerson(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String, Object> parameterMap = this.getParameterMap(request);
		parameterMap.put("nowDate", ThreeMouthUtils.getThreeMouth());
		groupArrearsService.exportAllChinaPerson(request, response, parameterMap);
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
