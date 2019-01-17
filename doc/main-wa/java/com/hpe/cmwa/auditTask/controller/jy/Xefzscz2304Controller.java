package com.hpe.cmwa.auditTask.controller.jy;

import java.io.UnsupportedEncodingException;
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

import com.hpe.cmwa.auditTask.service.jy.Xefzscz2304Service;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.CSVUtils;



@Controller
@RequestMapping("/xefzscz/2304")
public class Xefzscz2304Controller extends BaseController{
	
	private DecimalFormat df = new DecimalFormat("######0.00");
	
	@Autowired
	private Xefzscz2304Service xefzscz2304Service;

	/**
	 * 初始化界面
	 * @return
	 */
	@RequestMapping(value = "/index")
    public String index() {
		return "auditTask/jy/ygyccz/xefzscz2304";
    }
	
	@ResponseBody
	@RequestMapping(value = "/getXefzscz2304Trend")
	 public  Map<String, Object> getXefzscz2304Trend(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		 List<Map<String, Object>> list = xefzscz2304Service.getZheXian2304Trend(parameterMap);
		 //计算平均值
		 Map<String, Object> avgMap = xefzscz2304Service.getZheXian2304AVGNumber(parameterMap);
		 //计算周期内最大赠送金额
		 List<Map<String, Object>> listMax = xefzscz2304Service.getZheXian2304MAXNumber(parameterMap);
		 Map<String, Object> maxMap = new HashMap<String, Object>();
		 Integer max_pay_num;
		 String maxShort_name="";
		 String maxAud_trm="";
		 Double highAvg;
		 if(listMax.size()==0){
			 max_pay_num=0;
			 maxShort_name=null;
			 maxAud_trm=null;
		 }else{
			 max_pay_num = Integer.parseInt(listMax.get(0).get("max_pay_num").toString());
			 maxShort_name = listMax.get(0).get("short_name").toString();
			 maxAud_trm = listMax.get(0).get("aud_trm").toString();
		 }
		 Double avgNumber = avgMap.get("avg_pay_num")==null?0.00:Double.parseDouble(avgMap.get("avg_pay_num").toString());
	     if(avgNumber==0.00){
	    	 highAvg=0.00;
	     }else if(max_pay_num==0){
	    	 highAvg=0.00;
	     }else{
	    	 highAvg = Double.valueOf((max_pay_num-Double.parseDouble(df.format(avgNumber)))/avgNumber);
	     }
	     
		 Map<String, Object> returnMap = new HashMap<String, Object>();
		 maxMap.put("highAvg", df.format(highAvg*100)+"%");
		 maxMap.put("max_pay_num", max_pay_num);
		 maxMap.put("maxShort_name", maxShort_name);
		 maxMap.put("maxAud_trm", maxAud_trm);
	     returnMap.put("data", list);
	     returnMap.put("avgMap", avgMap);
	     returnMap.put("maxMap", maxMap);
		 return returnMap;
	 }
	
	
	@ResponseBody
    @RequestMapping(value = "/loadXefzscz_TabDetailTable")
	public Pager loadXefzscz_TabDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		pager.setParams(parameterMap);
		List<Map<String, Object>> list = xefzscz2304Service.loadXefzscz_TabDetailTable(pager);
		pager.setDataRows(list);
		return pager;
	}
	
	
	@ResponseBody
    @RequestMapping(value = "/exportXefzsczDetail")
	public void exportXefzsczDetail(HttpServletRequest request, HttpServletResponse response){
		try{
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    List<Map<String, Object>> list = xefzscz2304Service.exportXefzsczDetail(parameterMap);
		    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
		    if (list != null && list.size() > 0) {
		    	LinkedHashMap<String, Object> lhm = null;
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> eachMap = list.get(i);
					lhm = new LinkedHashMap<String, Object>();
					lhm.put("1", eachMap.get("aud_trm"));
					lhm.put("2", eachMap.get("cmcc_prov_prvd_id"));
					lhm.put("3", eachMap.get("short_name"));
					lhm.put("4", eachMap.get("pay_msisdn_num"));
					lhm.put("5", eachMap.get("pay_num")+"\t");
					lhm.put("6", eachMap.get("pay_amt")+"\t");
					exportData.add(lhm);
				}
			}
		    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			    map.put("1", "审计月");
				map.put("2", "省代码");
				map.put("3", "省名称");
				map.put("4", "缴费用户数");
				map.put("5", "缴费笔数");
				map.put("6", "缴费金额(元)");
			String fileName = "员工异常操作_小额非整数充值_小额非整数充值波动趋势_汇总.csv";
			CSVUtils.exportCSVList(fileName, exportData, map, request, response);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/loadzhuxingChart")
	 public  Map<String, Object> loadzhuxingChart(HttpServletRequest request) {
		 Map<String, Object> parameterMap = this.getParameterMap(request);
		 List<Map<String, Object>> list = xefzscz2304Service.loadzhuxingChart(parameterMap);
		 List<Map<String, Object>> top3list = xefzscz2304Service.getzhuxing2304Top3Con(parameterMap);
		 Map<String, Object> returnMap = new HashMap<String, Object>();
	     returnMap.put("data", list);
	     returnMap.put("top3data", top3list);
		 return returnMap;
	}
	
	
	@ResponseBody
    @RequestMapping(value = "/exportDsxefzczDetail")
	public void exportDsxefzczDetail(HttpServletRequest request, HttpServletResponse response){
		try{
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    List<Map<String, Object>> list = xefzscz2304Service.exportDsxefzczDetail(parameterMap);
		    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
		    if (list != null && list.size() > 0) {
		    	LinkedHashMap<String, Object> lhm = null;
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> eachMap = list.get(i);
					lhm = new LinkedHashMap<String, Object>();
					lhm.put("1", parameterMap.get("hz_startMonth")+"-"+parameterMap.get("hz_endMonth"));
					lhm.put("2", eachMap.get("cmcc_prov_prvd_id"));
					lhm.put("3", eachMap.get("short_name"));
					lhm.put("4", eachMap.get("cmcc_prov_id"));
					lhm.put("5", eachMap.get("cmcc_prvd_nm_short"));
					lhm.put("6", eachMap.get("pay_msisdn_num"));
					lhm.put("7", eachMap.get("pay_num"));
					lhm.put("8", eachMap.get("pay_amt")+"\t");
					exportData.add(lhm);
				}
			}
		    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			    map.put("1", "审计区间");
				map.put("2", "省代码");
				map.put("3", "省名称");
				map.put("4", "地市代码");
				map.put("5", "地市名称");
				map.put("6", "缴费用户数");
				map.put("7", "缴费笔数");
				map.put("8", "缴费金额（元）");
			String fileName = "员工异常操作_小额非整数充值_地市小额非整数充值统计_汇总.csv";
			CSVUtils.exportCSVList(fileName, exportData, map, request, response);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	
	@ResponseBody
    @RequestMapping(value = "/loadDsxefzcz_TabDetailTable")
	public Pager loadDsxefzcz_TabDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		pager.setParams(parameterMap);
		List<Map<String, Object>> list = xefzscz2304Service.loadDsxefzcz_TabDetailTable(pager);
		pager.setDataRows(list);
		return pager;
	}
	
	
	@ResponseBody
    @RequestMapping(value = "/getCityDetailPagerList")
	public Pager getCityDetailPagerList(HttpServletResponse response, HttpServletRequest request, Pager pager) throws UnsupportedEncodingException{
		Map<String, Object> parameterMap = this.getParameterMap(request);

		pager.setParams(parameterMap);
		List<Map<String, Object>> list = xefzscz2304Service.getCityDetailPagerList(pager);
		pager.setDataRows(list);
		return pager;
	}
	
	
	@ResponseBody
    @RequestMapping(value = "/exportMxDetailList")
	public void exportMxDetailList(HttpServletRequest request, HttpServletResponse response){
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    try {
				xefzscz2304Service.exportMxDetailList(request, response,parameterMap);
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
