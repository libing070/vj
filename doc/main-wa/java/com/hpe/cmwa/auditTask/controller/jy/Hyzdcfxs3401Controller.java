package com.hpe.cmwa.auditTask.controller.jy;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.jy.Hyzdcfxs3401Service;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.CSVUtils;

@Controller
@RequestMapping(value = "hyzdcfxs/3401")
public class Hyzdcfxs3401Controller extends BaseController {

	@Autowired
	private Hyzdcfxs3401Service hyzdcfxs3401Service;

	private DecimalFormat df = new DecimalFormat("######0.00");

	@RequestMapping(value = "index")
	public String index(HttpServletRequest request,HttpServletResponse response) {
		String url = "";
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		if(request.getParameter("provinceCode").equals("10000")){
			url = "auditTask/sjk/hyzdxsyc/hyzdxsyc";
		}else{
			url = "auditTask/jy/hyzdcfxs/hyzdcfxs3401";
		}
		return url;
	}
	/**
	 * 双柱图
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/hyzdcfxsNum")
	public Map<String, Object> hyzdcfxsNum(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		parameterMap.put("hz_startMonth", parameterMap.get("hz_startMonth")+"01");
		parameterMap.put("hz_endMonth",parameterMap.get("hz_endMonth")+"31");
		List<Map<String, Object>> list = hyzdcfxs3401Service.hyzdcfxsNum(parameterMap);
		//双柱图结论
		Map<String, Object> sum_map = hyzdcfxs3401Service.getSumNumCon(parameterMap);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("data", list);
		returnMap.put("sum_map", sum_map);

		return returnMap;
	}
	
	/**
	 * 双柱图数据表
	 */
	@ResponseBody
	@RequestMapping(value = "/getHyzdcfxsNum3401Detail_Table")
	public Pager getHyzdcfxsNum3401Detail_Table(HttpServletResponse response, HttpServletRequest request, Pager pager) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		parameterMap.put("hz_startMonth", parameterMap.get("hz_startMonth")+"01");
		parameterMap.put("hz_endMonth",parameterMap.get("hz_endMonth")+"31");
		pager.setParams(parameterMap);
		List<Map<String, Object>> list = hyzdcfxs3401Service.getHyzdcfxsNum3401Detail_Table(pager);
		pager.setDataRows(list);
		return pager;
	}
	
	/**
	 * 双柱图数据表导出
	 */
	@ResponseBody
    @RequestMapping(value = "/exportHyzdcfxsDetail")
	public void exportHyzdcfxsDetail(HttpServletRequest request, HttpServletResponse response){
		try{
			Map<String, Object> parameterMap = this.getParameterMap(request);
			parameterMap.put("hz_startMonth", parameterMap.get("hz_startMonth")+"01");
			parameterMap.put("hz_endMonth",parameterMap.get("hz_endMonth")+"31");

			List<Map<String, Object>> list = hyzdcfxs3401Service.exportHyzdcfxsDetail(parameterMap);
			List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
			if (list != null && list.size() > 0) {
		    	LinkedHashMap<String, Object> lhm = null;
		    	for (int i = 0; i < list.size(); i++) {
					Map<String, Object> eachMap = list.get(i);
					lhm = new LinkedHashMap<String, Object>();
					lhm.put("1", eachMap.get("aud_trm"));
					lhm.put("2", eachMap.get("cmcc_prvd_nm_short"));
					lhm.put("3", eachMap.get("trmnl_imei"));
					lhm.put("4", eachMap.get("sum_sell_num"));
					lhm.put("5", eachMap.get("sum_bt_je")+"\t");
					lhm.put("6", eachMap.get("sum_fee_allow_cost")+"\t");
					lhm.put("7", eachMap.get("sum_trmnl_allow_cost")+"\t");
					lhm.put("8", df.format(eachMap.get("avg_bt_je"))+"\t");
					exportData.add(lhm);
				}
			}
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		    map.put("1", "审计月");
			map.put("2", "地市名称");
			map.put("3", "终端IMEI");
			map.put("4", "累计销售次数");
			map.put("5", "累计补贴金额（元）");
			map.put("6", "话费补贴成本（元）");
			map.put("7", "终端补贴成本（元）");
			map.put("8", "平均单次补贴金额（元）");
		String fileName = "合约终端销售异常_合约终端重复销售_汇总_重复销售终端统计.csv";
		CSVUtils.exportCSVList(fileName, exportData, map, request, response);
		}catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	
	/**
	 * 补贴金额 合约终端重复销售地市分布
	 */
	@ResponseBody
	@RequestMapping(value = "/loadbtjeChart")
	public Map<String, Object> loadbtjeChart(HttpServletRequest request){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		parameterMap.put("hz_startMonth", parameterMap.get("hz_startMonth")+"01");
		parameterMap.put("hz_endMonth",parameterMap.get("hz_endMonth")+"31");

		List<Map<String, Object>> list = hyzdcfxs3401Service.loadbtjeChart(parameterMap);
		//补贴金额结论数据
		List<Map<String, Object>> listCon = hyzdcfxs3401Service.getTop3Con(parameterMap);
		//数据表结论
		List<Map<String, Object>> listTableCon = hyzdcfxs3401Service.getTableCon(parameterMap);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("data", list);
		returnMap.put("dataCon", listCon);
		returnMap.put("tableConData", listTableCon);

		return returnMap;
	}
	
	
	/**
	 * 补贴金额 合约终端重复销售地市分布
	 */
	@ResponseBody
	@RequestMapping(value = "/loadxsslChart")
	public Map<String, Object> loadxsslChart(HttpServletRequest request){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		parameterMap.put("hz_startMonth", parameterMap.get("hz_startMonth")+"01");
		parameterMap.put("hz_endMonth",parameterMap.get("hz_endMonth")+"31");

		List<Map<String, Object>> list = hyzdcfxs3401Service.loadxsslChart(parameterMap);
		//补贴金额结论数据
		List<Map<String, Object>> listCon = hyzdcfxs3401Service.getXsslTop3Con(parameterMap);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("data", list);
		returnMap.put("dataCon", listCon);

		return returnMap;
	}
	
	
	/**
	 * 销售数量数据表
	 */
	@ResponseBody
	@RequestMapping(value = "/getxssl3401Detail_Table")
	public Pager getxssl3401Detail_Table(HttpServletResponse response, HttpServletRequest request, Pager pager) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		parameterMap.put("hz_startMonth", parameterMap.get("hz_startMonth")+"01");
		parameterMap.put("hz_endMonth",parameterMap.get("hz_endMonth")+"31");

		pager.setParams(parameterMap);
		List<Map<String, Object>> list = hyzdcfxs3401Service.getxssl3401Detail_Table(pager);
		pager.setDataRows(list);
		return pager;
	}
	
	
	/**
	 * 销售数量数据表导出
	 */
	@ResponseBody
    @RequestMapping(value = "/exportXssl3401Detail")
	public void exportXssl3401Detail(HttpServletRequest request, HttpServletResponse response){
		try{
			Map<String, Object> parameterMap = this.getParameterMap(request);
			parameterMap.put("hz_startMonth", parameterMap.get("hz_startMonth")+"01");
			parameterMap.put("hz_endMonth",parameterMap.get("hz_endMonth")+"31");

			List<Map<String, Object>> list = hyzdcfxs3401Service.exportXssl3401Detail(parameterMap);
			List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
			if (list != null && list.size() > 0) {
		    	LinkedHashMap<String, Object> lhm = null;
		    	for (int i = 0; i < list.size(); i++) {
					Map<String, Object> eachMap = list.get(i);
					lhm = new LinkedHashMap<String, Object>();
					lhm.put("1", eachMap.get("aud_trm"));
					lhm.put("2", eachMap.get("cmcc_prvd_nm_short"));
					lhm.put("3", eachMap.get("trmnl_imei"));
					lhm.put("4", eachMap.get("sum_sell_num"));
					lhm.put("5", eachMap.get("sum_fee_allow_cost")+"\t");
					lhm.put("6", eachMap.get("sum_trmnl_allow_cost")+"\t");
					lhm.put("7", eachMap.get("sum_bt_je")+"\t");
					exportData.add(lhm);
				}
			}
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		    map.put("1", "审计月");
			map.put("2", "地市名称");
			map.put("3", "终端IMEI");
			map.put("4", "累计销售次数");
			map.put("5", "话费补贴成本(元)");
			map.put("6", "终端补贴成本（元）");
			map.put("7", "累计补贴金额（元）");
		String fileName = "合约终端销售异常_合约终端重复销售_汇总_地市.csv";
		CSVUtils.exportCSVList(fileName, exportData, map, request, response);
		}catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	/**
	 * 明细数据表
	 */
	@ResponseBody
	@RequestMapping(value = "/getCityDetailPagerList")
	public Pager getCityDetailPagerList(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		parameterMap.put("mx_startMonth", parameterMap.get("mx_startMonth")+"01");
		parameterMap.put("mx_endMonth",parameterMap.get("mx_endMonth")+"31");

		pager.setParams(parameterMap);
		List<Map<String, Object>> list = hyzdcfxs3401Service.getCityDetailPagerList(pager);
		pager.setDataRows(list);
		return pager;
	}
	
	
	/**
	 * 销售数量数据表导出
	 */
	@ResponseBody
    @RequestMapping(value = "/exportMxDetailList")
	public void exportMxDetailList(HttpServletRequest request, HttpServletResponse response){
			Map<String, Object> parameterMap = this.getParameterMap(request);
			parameterMap.put("mx_startMonth", parameterMap.get("mx_startMonth")+"01");
			parameterMap.put("mx_endMonth",parameterMap.get("mx_endMonth")+"31");
			try {
				hyzdcfxs3401Service.exportMxDetailList(request, response,parameterMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	/***
	 * <pre>
	 * Desc  合约终端重复销售
	 * @param request
	 * @refactor 于志男
	 * </pre>
	 */
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
