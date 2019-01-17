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

import com.hpe.cmwa.auditTask.service.sjjk.KHQF01Service;
import com.hpe.cmwa.controller.BaseController;
@Controller
@RequestMapping("/khqf01/")
public class KHQF01Controller extends BaseController{

	
	@Autowired 
	private KHQF01Service khqf01Service;
	
	@RequestMapping(value="index")
	public String index(HttpServletRequest request){
		return "auditTask/sjjk/khqf";
	}
	
	/**
	 * 根据监控点名称以及关注点获取页面问号弹框信息
	 */
	@ResponseBody
	@RequestMapping(value = "loadKhqfDialog")
	public List<Map<String, Object>> loadKhqfDialog(HttpServletRequest request){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		List<Map<String, Object>> list = khqf01Service.loadKhqfDialog(parameterMap);
		return list;
	}
	
	/**
	 *  通过省编码查询该省份是否有数据
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getResultByProvinceCode")
    public List<String> getResultByProvinceCode(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<String> list = khqf01Service.getResultByProvinceCode(parameterMap);
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
		list = khqf01Service.getFirColumnNumData(parameterMap);
	}
	if("2".equals(khqf01LeftType)){
		list = khqf01Service.getFirColumnPerData(parameterMap);
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
			list = khqf01Service.getFirLineNumData(parameterMap);
		}
		if("2".equals(khqf01LeftType)){
			list = khqf01Service.getFirLinePerData(parameterMap);
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
		Map<String, Object> list = khqf01Service.getSecColumnData(parameterMap);
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
		Map<String, Object> list = khqf01Service.getSecLineData(parameterMap);
		return list;
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
		List<Map<String, Object>> list = khqf01Service.getTableData(parameterMap);
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
		khqf01Service.exportHzTableData(request, response, parameterMap);
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
		khqf01Service.exportMxTableData(request, response, parameterMap);
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
