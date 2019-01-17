package com.hpe.cmwa.auditTask.controller.sox;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.sox.QfycyzxService;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;


@Controller
@RequestMapping("/qfycyzx")
public class QfycyzxController extends BaseController{
	
	private DecimalFormat df = new DecimalFormat("######0.00");
	
	@Autowired
	private QfycyzxService qfycyzxService;
	
	
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request,HttpServletResponse response) {
		String url = "";
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		if(request.getParameter("provinceCode").equals("10000")){
			url = "auditTask/sjk/yskycyzx_qg/qfycyzx_qg";
		}else{
			url = "auditTask/sox/yyskycyzx/qfycyzx";
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
	@RequestMapping(value = "/getCwxtqftjChart")
	public Map<String, Object> getCwxtqftjChart(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		//图形
		List<Map<String, Object>> list = qfycyzxService.getCwxtqftjChart(parameterMap);
		//结论
		List<Map<String, Object>> listCon = qfycyzxService.getCwxtqftjCon(parameterMap);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("dataChar", list);
		returnMap.put("dataCon", listCon);
		return returnMap;
	}
	
	@ResponseBody
    @RequestMapping(value = "/load_qfycyzx_TabDetailTable")
	public Pager load_qfycyzx_TabDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		pager.setParams(parameterMap);
		List<Map<String, Object>> list = qfycyzxService.load_qfycyzx_TabDetailTable(pager);
		pager.setDataRows(list);
		return pager;
		
	}
	
	@ResponseBody
    @RequestMapping(value = "/exportQfycyzx_Detail")
	public void exportQfycyzx_Detail(HttpServletRequest request, HttpServletResponse response){
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    try {
		    	qfycyzxService.exportQfycyzx_Detail(request, response,parameterMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
