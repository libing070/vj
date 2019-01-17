package com.hpe.cmwa.auditTask.controller.jz;

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

import com.hpe.cmwa.auditTask.service.jz.ZsyjkczjzdService;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;



@Controller
@RequestMapping("/zsyjkczjzd")
public class ZsyjkczjzdController  extends BaseController{
	
	private DecimalFormat df = new DecimalFormat("######0.00");
	
	@Autowired
	private ZsyjkczjzdService zsyjkczjzdservice;
	
	/**
	 * 初始化界面
	 * @return
	 */
	@RequestMapping(value = "/index")
    public String index(HttpServletRequest request) {
		if(request.getParameter("provinceCode").equals("10000")){
			return "auditTask/sjk/yjkzshgx_qg/zsyjkczjzd_qg";
		}else{
			return "auditTask/jz/yjkzshgx/zsyjkczjzd";
		}
		
    }
	
	
	/**
     * 赠送有价卡集中充值地市分布
     * @param request
     * @author yuzn
     */
	@ResponseBody
	@RequestMapping(value = "/getZsyjkjzdCzdsChart")
	 public  Map<String, Object> getZsyjkjzdCzdsChart(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		//图形数据
		List<Map<String, Object>> listChar = zsyjkczjzdservice.getZsyjkjzdCzdsChart(parameterMap);
		//结论数据
		List<Map<String, Object>> listCon = zsyjkczjzdservice.getZsyjkjzdCzdsCon(parameterMap);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("dataChar", listChar);
	    returnMap.put("dataCon", listCon);
		return returnMap;
	}
	
	/**
	 * 赠送有价卡集中充值地市分布 数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/getZsyjkjzdCzdsDetailTable")
	public Pager getZsyjkjzdCzdsDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
			pager.setParams(parameterMap);
			List<Map<String, Object>> list = zsyjkczjzdservice.getZsyjkjzdCzdsDetailTable(pager);
			pager.setDataRows(list);
			return pager;
		
	}
	
	/**
	 * 赠送有价卡集中充值地市分布 数据表 导出
	 * @param request
	 * @param response
	 */
	@ResponseBody
    @RequestMapping(value = "/exportZsyjkjzczdsDetail")
	public void exportZsyjkjzczdsDetail(HttpServletRequest request, HttpServletResponse response){
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    try {
		    	zsyjkczjzdservice.exportZsyjkjzczdsDetail(request, response,parameterMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	/**
     * 赠送有价卡集中充值金额排名前十手机号码
     * @param request
     * @author yuzn
     */
	@ResponseBody
	@RequestMapping(value = "/getZsyjkjzczjeCharts")
	 public  Map<String, Object> getZsyjkjzczjeCharts(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		//图形数据
		List<Map<String, Object>> listChar = zsyjkczjzdservice.getZsyjkjzczjeCharts(parameterMap);
		//结论数据
		List<Map<String, Object>> listCon = zsyjkczjzdservice.getZsyjkjzczjeCon(parameterMap);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("dataChar", listChar);
	    returnMap.put("dataCon", listCon);
		return returnMap;
	}
	
	/**
	 * 赠送有价卡集中充值地市分布 数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/getZsyjkjzczjeDetail")
	public Pager getZsyjkjzczjeDetail(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
			pager.setParams(parameterMap);
			List<Map<String, Object>> list = zsyjkczjzdservice.getZsyjkjzczjeDetail(pager);
			pager.setDataRows(list);
			return pager;
		
	}
	
	/**
	 * 赠送有价卡集中充值地市分布 数据表 导出
	 * @param request
	 * @param response
	 */
	@ResponseBody
    @RequestMapping(value = "/exportZsyjkjzczjeDetail")
	public void exportZsyjkjzczjeDetail(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> parameterMap = this.getParameterMap(request);
	    try {
	    	zsyjkczjzdservice.exportZsyjkjzczjeDetail(request, response,parameterMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 赠送有价卡充值集中度 明细数据表
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
			List<Map<String, Object>> list = zsyjkczjzdservice.getCityDetailPagerList(pager);
			pager.setDataRows(list);
			return pager;
		
	}
	
	
	/**
	 * 明细数据表 导出
	 * @param request
	 * @param response
	 */
	@ResponseBody
    @RequestMapping(value = "/exportMxDetailList")
	public void exportMxDetailList(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> parameterMap = this.getParameterMap(request);
	    try {
	    	zsyjkczjzdservice.exportMxDetailList(request, response,parameterMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	  /**
	    * <pre>
	    * Desc 每个controller都要实现的一个方法，用来设置页面初始化查询参数的form使用 
	    * @return
	    * @author peter.fu
	    * Nov 17, 2016 2:16:17 PM
	    * </pre>
	    */
	   @SuppressWarnings("rawtypes")
	   @ResponseBody
	   @RequestMapping(value = "/initDefaultParams")
	   public Map initDefaultParams(HttpServletRequest request) {

	       Map<String, Object> defaultParams = new HashMap<String, Object>();
	       
	       // 默认按地市排名
	       defaultParams.put("hz_rankType", 0);
	       // 地市汇总地图倍数
	       defaultParams.put("hz_map_double", 5);
	       
	       return defaultParams;
	   }
	

}
