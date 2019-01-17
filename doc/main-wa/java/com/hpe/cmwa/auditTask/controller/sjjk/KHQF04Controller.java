package com.hpe.cmwa.auditTask.controller.sjjk;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.sjjk.KHQF04Service;
import com.hpe.cmwa.controller.BaseController;


/**
 * 渠道放号质量低
 * @author yuzn1
 */
@Controller
@RequestMapping("/khqf04/")
public class KHQF04Controller extends BaseController{
	
	@Autowired
	private KHQF04Service khqf04Service;
	
	/**
	 * 通过省编码查询该省份是否有数据
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getResultByProvinceCode")
    public List<String> getResultByProvinceCode(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<String> list = khqf04Service.getResultByProvinceCode(parameterMap);
	return list;
    }
	
	
	 @ResponseBody
	 @RequestMapping(value = "getFirColumnData")
	 public Object getFirColumnData(HttpServletRequest request){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		Map<String, Object> returnMap = khqf04Service.getFirColumnData(parameterMap);
		return returnMap;
	 }
	 
	 @ResponseBody
	 @RequestMapping(value = "getFirLineData")
	 public Object getFirLineData(HttpServletRequest request){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		String currBeginDate = getBeginDate(request);
		parameterMap.put("currBeginDate", currBeginDate);
		Map<String, Object> returnMap = khqf04Service.getFirLineData(parameterMap);
		return returnMap;
	 }
	 
	 
	 @ResponseBody
	 @RequestMapping(value = "getSecColumnData")
	 public Object getSecColumnData(HttpServletRequest request){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		Map<String, Object> returnMap = khqf04Service.getSecColumnData(parameterMap);
		System.out.println(returnMap.get("areaCode"));
		return returnMap;
	 }
	 
	 @ResponseBody
	 @RequestMapping(value = "getSecLineData")
	 public Object getSecLineData(HttpServletRequest request){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		String currBeginDate = getBeginDate(request);
		parameterMap.put("currBeginDate", currBeginDate);
		Map<String, Object> returnMap = khqf04Service.getSecLineData(parameterMap);
		return returnMap;
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
		List<Map<String, Object>> list = khqf04Service.getTableData(parameterMap);
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
		khqf04Service.exportHzTableData(request, response, parameterMap);
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
		khqf04Service.exportMxTableData(request, response, parameterMap);
	}

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
