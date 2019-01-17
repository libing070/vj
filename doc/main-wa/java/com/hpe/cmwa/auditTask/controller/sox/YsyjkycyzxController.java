package com.hpe.cmwa.auditTask.controller.sox;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.sox.YsyjkycyzxService;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;


@Controller
@RequestMapping("/ysyjkycyzx")
public class YsyjkycyzxController extends BaseController{
	
	private DecimalFormat df = new DecimalFormat("######0.00");
	
	@Autowired
	private YsyjkycyzxService YsyjkycyzxService;
	
	
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request,HttpServletResponse response) {
		String url = "";
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		if(request.getParameter("provinceCode").equals("10000")){
			url = "auditTask/sjk/yskycyzx_qg/ysyjkycyzx_qg";
		}else{
			url = "auditTask/sox/yyskycyzx/ysyjkycyzx";
		}
		return url;
	}
	
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "initDefaultParams")
	public Map initDefaultParams(HttpServletRequest request) {

		Map<String, Object> defaultParams = new HashMap<String, Object>();
		// 默认按地市排名
		defaultParams.put("hz_rankType", 0);
		// 地市汇总地图倍数
		defaultParams.put("hz_map_double", 5);

		return defaultParams;
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/getYsyjkyzxChart")
	public Map<String, Object> getYsyjkyzxChart(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		//图形
		List<Map<String, Object>> list = YsyjkycyzxService.getYsyjkyzxChart(parameterMap);
		//结论
		List<Map<String, Object>> listCon = YsyjkycyzxService.getYsyjkyzxCon(parameterMap);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("dataChar", list);
		returnMap.put("dataCon", listCon);
		return returnMap;
	}
	
	@ResponseBody
    @RequestMapping(value = "/load_ysyjkyzx_TabDetailTable")
	public Pager load_ysyjkyzx_TabDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		pager.setParams(parameterMap);
		List<Map<String, Object>> list = YsyjkycyzxService.load_ysyjkyzx_TabDetailTable(pager);
		pager.setDataRows(list);
		return pager;
		
	}
	
	@ResponseBody
    @RequestMapping(value = "/exportYsyjkyzx_Detail")
	public void exportYsyjkyzx_Detail(HttpServletRequest request, HttpServletResponse response){
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    try {
		    	YsyjkycyzxService.exportYsyjkyzx_Detail(request, response,parameterMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
