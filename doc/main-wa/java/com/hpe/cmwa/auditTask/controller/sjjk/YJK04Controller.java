package com.hpe.cmwa.auditTask.controller.sjjk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.sjjk.YJK04Service;
import com.hpe.cmwa.controller.BaseController;
@Controller
@RequestMapping("/yjk04/")
public class YJK04Controller extends BaseController{

	
	@Autowired 
	private YJK04Service yjk04Service;
	
	/**
	 *  通过省编码查询该省份是否有数据
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getResultByProvinceCode")
    public List<String> getResultByProvinceCode(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<String> list = yjk04Service.getResultByProvinceCode(parameterMap);
	return list;
    }
	
	/**
	 * 获取第一个柱图数据
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getFirColumnData")
    public Map<String, Object> getFirColumnData(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	Map<String, Object> list = new HashMap<String, Object>();
	String khqf01LeftType= (String) parameterMap.get("khqf01LeftType");
	if("1".equals(khqf01LeftType)){
		list = yjk04Service.getFirColumnNumData(parameterMap);
	}
	if("2".equals(khqf01LeftType)){
		list = yjk04Service.getFirColumnPerData(parameterMap);
	}
	return list;
    }
	/**
	 * 获取第一个折线图数据
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getFirLineData")
	public Map<String, Object> getFirLineData(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		String currBeginDate = getBeginDate(request);
		parameterMap.put("currBeginDate", currBeginDate);
		Map<String, Object> list = new HashMap<String, Object>();
		String khqf01LeftType= (String) parameterMap.get("khqf01LeftType");
		if("1".equals(khqf01LeftType)){
			list = yjk04Service.getFirLineNumData(parameterMap);
		}
		if("2".equals(khqf01LeftType)){
			list = yjk04Service.getFirLinePerData(parameterMap);
		}
		return list;
	}
	
	/**
	 * 获取第二个柱图数据
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSecColumnData")
    public Map<String, Object> getSecColumnData(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	Map<String, Object> list = new HashMap<String, Object>();
	String khqf01Type= (String) parameterMap.get("khqf01RightType");
	if("1".equals(khqf01Type)){
		list = yjk04Service.getSecColumnNumData(parameterMap);
	}
	if("2".equals(khqf01Type)){
		list = yjk04Service.getSecColumnPerData(parameterMap);
	}
	return list;
    }
	/**
	 * 获取第二个折线图数据
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSecLineData")
	public Map<String, Object> getSecLineData(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		String currBeginDate = getBeginDate(request);
		parameterMap.put("currBeginDate", currBeginDate);
		Map<String, Object> map = new HashMap<String, Object>();
		String khqf01Type= (String) parameterMap.get("khqf01RightType");
		if("1".equals(khqf01Type)){
			map = yjk04Service.getSecLineDataNum(parameterMap);
		}
		if("2".equals(khqf01Type)){
			map = yjk04Service.getSecLinePerData(parameterMap);
		}
		return map;
	}
	/**
	 * 获取统计分析数据表数据
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getTableData")
	public List<Map<String, Object>> getTableData(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		List<Map<String, Object>> list = yjk04Service.getTableData(parameterMap);
		return list;
	}
	
	/**
	 * 导出 汇总数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value = "exportHzTableData")
	public void exportHzTableData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		yjk04Service.exportHzTableData(request, response, parameterMap);
	}
	/**
	 * 导出 明细数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value = "exportMxTableData")
	public void exportMxTableData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		yjk04Service.exportMxTableData(request, response, parameterMap);
	}
	
	/**
	 * 判断起始时间到结束时间是否大于6个月，如果小于6个月，起始时间置为结束时间往前的6个月
	 * @param request
	 * @return
	 */
	public String getBeginDate(HttpServletRequest request){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		String beginDate = (String) parameterMap.get("currBeginDate");
		String endDate = (String) parameterMap.get("currEndDate");
		int beginYear =Integer.parseInt( beginDate.substring(0, 4));
		int beginMonth =Integer.parseInt(beginDate.substring(4,6));
		int endYear =Integer.parseInt( endDate.substring(0, 4));
		int endMonth =Integer.parseInt(endDate.substring(4,6));
		int intEndYear = endYear;
		int intEndMonth = endMonth;
		if(endMonth<beginMonth){
			intEndYear =endYear-1;
			intEndMonth = endMonth+12;
		}
		int monthLength = (intEndYear-beginYear)*12 + intEndMonth-beginMonth;
		if(monthLength<6){
			if(endMonth<6){
				beginYear = endYear - 1;
				beginMonth = endMonth+7;
			}else{
				beginMonth = endMonth-5;
			}
			if(beginMonth < 10){
				
				beginDate = beginYear +"0"+beginMonth;
			}else{
				beginDate = beginYear +""+beginMonth;
				
			}
		}
		return beginDate;
	}
}
