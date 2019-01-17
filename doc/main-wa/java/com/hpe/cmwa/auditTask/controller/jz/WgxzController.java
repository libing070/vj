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

import com.hpe.cmwa.auditTask.service.jz.WgxzService;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;


@Controller
@RequestMapping("/wgxz")
public class WgxzController extends BaseController{
	
	private DecimalFormat df = new DecimalFormat("######0.00");
	
	@Autowired
	private WgxzService wgxzService;
	
	/**
	 * 初始化界面
	 * @return
	 */
	@RequestMapping(value = "/index")
    public String index(HttpServletRequest request,HttpServletResponse response) {
		String url = "";
		if(request.getParameter("provinceCode").equals("10000")){
			url = "auditTask/sjk/wgxz/wgxz";
		}else{
			url = "auditTask/jz/wgxz/wgxz";
		}
		return url;
    }
	
	/**
	 * 折线图
	 * @param request
	 * @return
	 */
	@ResponseBody
 	@RequestMapping(value = "/getWgzsChart")
	public  Map<String, Object> getWgzsChart(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		List<Map<String, Object>> list = wgxzService.getWgzsChart(parameterMap);
		Map<String, Object> avgMap = wgxzService.getWgzsChartAVGNumber(parameterMap);
		//计算周期内最大赠送金额
		 List<Map<String, Object>> listMax = wgxzService.getWgzsChartMAXNumber(parameterMap);
		 Map<String, Object> maxMap = new HashMap<String, Object>();
		 Integer max_infrac_pack_num;
		 String max_aud_trm="";
		 Double highAvg;
		 if(listMax.size()==0){
			 max_infrac_pack_num=0;
			 max_aud_trm=null;
		 }else{
			 max_infrac_pack_num = Integer.parseInt(listMax.get(0).get("max_infrac_pack_num").toString());
			 max_aud_trm = listMax.get(0).get("aud_trm").toString();
		 }
		 Double avgNumber = avgMap.get("avg_infrac_pack_num")==null?0.00:Double.parseDouble(avgMap.get("avg_infrac_pack_num").toString());
	     if(avgNumber==0.00){
	    	 highAvg=0.00;
	     }else if(max_infrac_pack_num==0){
	    	 highAvg=0.00;
	     }else{
	    	 highAvg = Double.valueOf((max_infrac_pack_num-avgNumber)/avgNumber);
	     }
	     
		 Map<String, Object> returnMap = new HashMap<String, Object>();
		 maxMap.put("highAvg", df.format(highAvg*100)+"%");
		 maxMap.put("max_infrac_pack_num", max_infrac_pack_num);
		 maxMap.put("max_aud_trm", max_aud_trm);
	     returnMap.put("data", list);
	     returnMap.put("avgMap", avgMap);
	     returnMap.put("maxMap", maxMap);
		 return returnMap;
	}
	
	/**
	 * 折线数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/loadWgzs_TabDetailTable")
	public Pager loadWgzs_TabDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		pager.setParams(parameterMap);
		List<Map<String, Object>> list = wgxzService.loadWgzs_TabDetailTable(pager);
		pager.setDataRows(list);
		return pager;
	}
	
	@ResponseBody
    @RequestMapping(value = "/exportWgzsDetail")
	public void exportWgzsDetail(HttpServletRequest request, HttpServletResponse response){
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    try {
		    	wgxzService.exportWgzsDetail(request, response,parameterMap);
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
		List<Map<String, Object>> list = wgxzService.getCityDetailPagerList(pager);
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
	    	wgxzService.exportMxDetailList(request, response,parameterMap);
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
