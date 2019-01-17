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

import com.hpe.cmwa.auditTask.service.jz.PersonTuitionService;
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
@RequestMapping("/personTuition/")
public class PersonTuitionController extends BaseController {

	@Autowired
	private PersonTuitionService personTuitionService;

	/**
	 * desc:初始化积分回馈率页面
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
		return "auditTask/jz/personTuition/personTuition";
	}
	
	/**
	 * Desc:积分回馈率趋势图数据
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
		List<Map<String, Object>> list = personTuitionService.selectIntegralFeedbackRate(formatParameter(params));
		//时间
		List<String> jftime = new ArrayList<String>();
		//异常欠费用户量占比
		List<Double> yhlzb = new ArrayList<Double>();
		//异常欠费金额占比
		List<Double> jezb = new ArrayList<Double>();
		//封装数据
		for(Map<String, Object> map : list){
			jftime.add(map.get("acctPrdYtm").toString());
			yhlzb.add(HelperDouble.formatTwoPoint((Double)Double.parseDouble(map.get("numPer").toString())*100));
			jezb.add(HelperDouble.formatTwoPoint((Double)Double.parseDouble(map.get("amtPer").toString())*100));
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
	
		//根据省份编号 和 审计时间 获取分析数据  全国排名
		Map<String, Object> params = this.getParameterMap(request);
		params.put("nowDate", ThreeMouthUtils.getThreeMouth());
		List<Map<String, Object>> dataAnalysisList = personTuitionService.selectDataAnalysis(formatParameter(params));
		//根据省份编号 和 审计时间 个人异常欠费金额排名前三的地市
		List<String> threeCityList = personTuitionService.selectThreeCity(formatParameter(params));

		Map<String, Object> dataMap = new HashMap<String, Object>();
		//审计时间
		dataMap.put("sjtime", formatParameter(this.getParameterMap(request)).get("currSumEndDate"));
		Map<String, Object> dataAnalysis = new HashMap<String, Object>();
		for(Map<String, Object> dataAnalysisMap : dataAnalysisList){
			if(dataAnalysisMap.get("cmccProvPrvdId").toString().equals(this.getParameterMap(request).get("provinceCode").toString())){
				dataAnalysis = dataAnalysisMap;
			}
		}
		if(dataAnalysis !=null && !dataAnalysis.isEmpty()){
			//省份名称
			dataMap.put("provinceCode", this.getParameterMap(request).get("provinceCode"));
			dataMap.put("shortName", dataAnalysis.get("shortName"));
			//异常欠费用户数
			dataMap.put("sumCust1", dataAnalysis.get("sumSubsId").toString());
			//异常欠费历史总金额
			dataMap.put("sumDbtAmt", dataAnalysis.get("sumDbtAmt").toString());
			//全国排名
			dataMap.put("ranking", dataAnalysis.get("sort"));
		}else{
			//省份名称
			dataMap.put("provinceCode", this.getParameterMap(request).get("provinceCode"));
			dataMap.put("shortName", "");
			//用户新增积分
			dataMap.put("sumCust1", "暂无数据");
			//新增积分总价值
			dataMap.put("sumDbtAmt", "暂无数据");
			//全国排名
			dataMap.put("ranking","");
		}
		//全国排名
		//dataMap.put("ranking", ranking);
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
		Double chinScale = personTuitionService.getAllChinaScal(formatParameter(params));
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
		Map<String, Object> params = this.getParameterMap(request);
		params.put("nowDate", ThreeMouthUtils.getThreeMouth());
		List<Map<String, Object>> allCityMapList = personTuitionService.selectAllMapCity(formatParameter(params));
		List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
		Map<String, Object> value = null;
		for(Map<String, Object> map :allCityMapList){
			value = new HashMap<String, Object>();
			value.put("cityName", map.get("cmccPrvdNmShort"));
			value.put("dbtAmtPer", Double.parseDouble(map.get("dbtAmtPer").toString())*100);//异常欠费金额比例
			value.put("cmccProvId", map.get("cmccProvId"));
			values.add(value);
		}
		result.put("values", values);
		//异常欠费金额比例min == 全国异常欠费金额比例
		Double chinScale = personTuitionService.getAllChinaScal(formatParameter(params));
		if(chinScale == null){
			chinScale=0d;
		}
		result.put("scaleMin", chinScale*100);
		//异常欠费金额比例max == 全国异常欠费金额比例* 5
		Double scaleMax = chinScale*5;
		result.put("scaleMax", scaleMax*100);
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
		
		//异常欠费金额比例min == 全国异常欠费金额比例
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("currSumBeginDate", sessoin.getAttribute("beforeAcctMonth"));
		params.put("currSumEndDate", sessoin.getAttribute("endAcctMonth"));
		params.put("nowDate", ThreeMouthUtils.getThreeMouth());
		Double chinScale = personTuitionService.getAllChinaScal(params);
		if(chinScale == null){
			chinScale = 0d;
		}
		defaultParams.put("scaleMin", chinScale*100);
		//异常欠费金额比例max == 全国异常欠费金额比例* 5
		Double scaleMax = chinScale*5*100;
		defaultParams.put("scaleMax", scaleMax);
		
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
		List<Map<String, Object>> cityMapList = personTuitionService.selectAllCityPerson(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	
	
	
	/**
	 * 导出 该省所有地市数据
	 * @param cmccProvPrvdId
	 * @param audTrmEnd
	 * @param request
	 * @param response
	 */
	@RequestMapping("exportAllCityPerson")
	@ResponseBody
	public void exportAllCityPerson(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String, Object> parameterMap = this.getParameterMap(request);
		parameterMap.put("nowDate", ThreeMouthUtils.getThreeMouth());
		personTuitionService.exportAllCityPerson(request, response, parameterMap);
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
		List<Map<String, Object>> cityMapList = personTuitionService.selectAllCinaPerson(pager);
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
	 */
	@RequestMapping("exportAllChinaPerson")
	@ResponseBody
	public void exportAllChinaPerson(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String, Object> parameterMap = this.getParameterMap(request);
		parameterMap.put("nowDate", ThreeMouthUtils.getThreeMouth());
		personTuitionService.exportAllChinaPerson(request, response, parameterMap);
		
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
