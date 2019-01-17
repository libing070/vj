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

import com.hpe.cmwa.auditTask.service.sox.GrkhcqqfwtjService;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;


@Controller
@RequestMapping("/grkhcqqfwtj")
public class GrkhcqqfwtjController  extends BaseController{
	
	private DecimalFormat df = new DecimalFormat("######0.00");
	
	@Autowired
	private GrkhcqqfwtjService grkhcqqfwtjService;
	
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request,HttpServletResponse response) {
		String url = "";
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		if(request.getParameter("provinceCode").equals("10000")){
			url = "auditTask/sjk/cqqfwjsztjcl_qg/grkhcqqfwtj_qg";
		}else{
			url = "auditTask/sox/cqqfwjsztjcl/grkhcqqfwtj";
		}
		return url;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getGrkhcqqfwtjChart")
	public Map<String, Object> getGrkhcqqfwtjChart(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		List<Map<String, Object>> list = grkhcqqfwtjService.getGrkhcqqfwtjChart(parameterMap);
		// 计算平均值
		Map<String, Object> avgMap = grkhcqqfwtjService.getGrkhcqqfwtjAVGNum(parameterMap);
		// 计算周期内最大数量
		List<Map<String, Object>> listMax = grkhcqqfwtjService.getGrkhcqqfwtjMAXNum(parameterMap);
		int a = listMax.size();
		Map<String, Object> maxMap = new HashMap<String, Object>();
		Integer max_acc_cnt;
		String aud_trm = "";
		Double highAvg;
		if (listMax.size() == 0 ||listMax.get(0)==null) {
			max_acc_cnt = 0;
			aud_trm = null;
		} else {
			max_acc_cnt = Integer.parseInt(listMax.get(0).get("max_acc_cnt").toString());
			aud_trm = listMax.get(0).get("aud_trm").toString();
		}
		Double avgNumber = avgMap.get("avg_acc_cnt") == null ? 0.00 : Double.parseDouble(avgMap.get("avg_acc_cnt").toString());
		if (avgNumber == 0.00) {
			highAvg = 0.00;
		} else if (max_acc_cnt == 0) {
			highAvg = 0.00;
		} else {
			highAvg = Double.valueOf((max_acc_cnt - Double.parseDouble(df.format(avgNumber))) / avgNumber);
		}

		Map<String, Object> returnMap = new HashMap<String, Object>();
		maxMap.put("highAvg", df.format(highAvg * 100) + "%");
		maxMap.put("max_acc_cnt", max_acc_cnt);
		maxMap.put("aud_trm", aud_trm);
		returnMap.put("data", list);
		returnMap.put("avgMap", avgMap);
		returnMap.put("maxMap", maxMap);
		return returnMap;
	}
	
	/**
	 * 数据表1
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/loadCqqfwtjzhs_sf_TabDetailTable")
	public Pager loadCqqfwtjzhs_sf_TabDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		pager.setParams(parameterMap);
		List<Map<String, Object>> list = grkhcqqfwtjService.loadCqqfwtjzhs_sf_TabDetailTable(pager);
		int list_num = Integer.parseInt(list.get(0).get("count_acct_id").toString());
		if(list_num==0){
			List<Map<String, Object>> list1 = new ArrayList<Map<String,Object>>();
			pager.setTotalRecords(0);
			pager.setDataRows(list1);
			return pager;
		}else{
			pager.setDataRows(list);
			return pager;
		}
		
	}
	
	/**
	 * 数据表1 导出
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/exportCqqfwtjzhs_sf_Detail")
	public void exportCqqfwtjzhs_sf_Detail(HttpServletRequest request, HttpServletResponse response){
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    try {
		    	grkhcqqfwtjService.exportCqqfwtjzhs_sf_Detail(request, response,parameterMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 数据表2
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/loadCqqfwtjzhs_zh_TabDetailTable")
	public Pager loadCqqfwtjzhs_zh_TabDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		pager.setParams(parameterMap);
		List<Map<String, Object>> list = grkhcqqfwtjService.loadCqqfwtjzhs_zh_TabDetailTable(pager);
		pager.setDataRows(list);
		return pager;
	}
	/**
	 * 数据表2 导出
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/exportCqqfwtjzhs_zh_Detail")
	public void exportCqqfwtjzhs_zh_Detail(HttpServletRequest request, HttpServletResponse response){
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    try {
		    	grkhcqqfwtjService.exportCqqfwtjzhs_zh_Detail(request, response,parameterMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 明细数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/getCityDetailPagerList")
	public Pager getCityDetailPagerList(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		pager.setParams(parameterMap);
		List<Map<String, Object>> list = grkhcqqfwtjService.getCityDetailPagerList(pager);
		pager.setDataRows(list);
		return pager;
	}
	
	/**
	 *明细数据表导出 
	 * @param request
	 * @param response
	 */
	@ResponseBody
    @RequestMapping(value = "/exportMxDetailList")
	public void exportMxDetailList(HttpServletRequest request, HttpServletResponse response){
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    try {
		    	grkhcqqfwtjService.exportMxDetailList(request, response,parameterMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
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

}
