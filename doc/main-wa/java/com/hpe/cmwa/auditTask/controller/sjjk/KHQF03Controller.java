package com.hpe.cmwa.auditTask.controller.sjjk;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.sjjk.KHQF03Service;
import com.hpe.cmwa.controller.BaseController;
@Controller
@RequestMapping("/khqf03/")
public class KHQF03Controller extends BaseController{

	
	@Autowired 
	private KHQF03Service khqf03Service;
	
	/**
	 *  通过省编码查询该省份是否有数据
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getResultByProvinceCode")
    public List<String> getResultByProvinceCode(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<String> list = khqf03Service.getResultByProvinceCode(parameterMap);
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
	Map<String, Object> list = khqf03Service.getFirColumnData(parameterMap);
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
		Map<String, Object> list = khqf03Service.getFirLineData(parameterMap);
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
		List<Map<String, Object>> list = khqf03Service.getTableData(parameterMap);
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
		khqf03Service.exportHzTableData(request, response, parameterMap);
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
		khqf03Service.exportMxTableData(request, response, parameterMap);
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
