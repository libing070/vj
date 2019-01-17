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

import com.hpe.cmwa.auditTask.service.jy.Qdyk2401Service;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.CSVUtils;


@Controller
@RequestMapping("/qdyk/2401")
public class Qdyk2401Controller extends BaseController{
	 @Autowired
	 private Qdyk2401Service qdyk2401Service;

	private DecimalFormat df = new DecimalFormat("######0.00");

	/**
	 * 初始化界面
	 * @return
	 */
	@RequestMapping(value = "/index")
    public String index(HttpServletRequest request) {
		if(request.getParameter("provinceCode").equals("10000")){
			return "auditTask/sjk/qdyk_qg/qdyk_qg";
		}else{
			return "auditTask/jy/qdyk/qdyk2401";
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
    
    @ResponseBody
	@RequestMapping(value = "/getQdykyhslChart")
	 public  Map<String, Object> getQdykyhslChart(HttpServletRequest request) {
    	Map<String, Object> parameterMap = this.getParameterMap(request);
    	List<Map<String, Object>> list = qdyk2401Service.getQdykyhslChart(parameterMap);
		 //计算平均值
		 Map<String, Object> avgMap = qdyk2401Service.getQdykyhsl2304AVGNumber(parameterMap);
		 //计算周期内最大赠送金额
		 List<Map<String, Object>> listMax = qdyk2401Service.getQdykyhsl2304MAXNumber(parameterMap);
		 Map<String, Object> maxMap = new HashMap<String, Object>();
		 Integer max_qdyk_subs_num;
		 String maxAud_trm="";
		 Double highAvg;
		 if(listMax.size()==0||listMax.isEmpty()){
			 max_qdyk_subs_num=0;
			 maxAud_trm="";
		 }else{
			 max_qdyk_subs_num = Integer.parseInt(listMax.get(0).get("max_qdyk_subs_num").toString());
			 maxAud_trm = listMax.get(0).get("max_aud_trm").toString();
		 }
		 Double avgNumber = avgMap.get("avg_qdyk_num")==null?0.00:Double.parseDouble(avgMap.get("avg_qdyk_num").toString());
	     if(avgNumber==0.00){
	    	 highAvg=0.00;
	     }else if(max_qdyk_subs_num==0){
	    	 highAvg=0.00;
	     }else{
	    	 highAvg = Double.valueOf((max_qdyk_subs_num-Double.parseDouble(df.format(avgNumber)))/avgNumber);
	     }
	     
		 Map<String, Object> returnMap = new HashMap<String, Object>();
		 maxMap.put("highAvg", df.format(highAvg*100)+"%");
		 maxMap.put("max_qdyk_subs_num", max_qdyk_subs_num);
		 maxMap.put("maxAud_trm", maxAud_trm);
	     returnMap.put("data", list);
	     returnMap.put("avgMap", avgMap);
	     returnMap.put("maxMap", maxMap);
		 return returnMap;
    }
    
    
    @ResponseBody
    @RequestMapping(value = "/loadQdykyhslTabDetailTable")
	public Pager loadQdykyhslTabDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		pager.setParams(parameterMap);
		List<Map<String, Object>> list = qdyk2401Service.loadQdykyhslTabDetailTable(pager);
		pager.setDataRows(list);
		return pager;
	}
    
    @ResponseBody
    @RequestMapping(value = "/exportQdykyhslDetail")
	public void exportQdykyhslDetail(HttpServletRequest request, HttpServletResponse response){
		try{
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    List<Map<String, Object>> list = qdyk2401Service.exportQdykyhslDetail(parameterMap);
		    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
		    if (list != null && list.size() > 0) {
		    	LinkedHashMap<String, Object> lhm = null;
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> eachMap = list.get(i);
					lhm = new LinkedHashMap<String, Object>();
					lhm.put("1", eachMap.get("aud_trm"));
					lhm.put("2", eachMap.get("cmcc_prov_prvd_id"));
					lhm.put("3", eachMap.get("short_name"));
					lhm.put("4", eachMap.get("chnl_class"));
					lhm.put("5", eachMap.get("chnl_class_nm")+"\t");
					lhm.put("6", eachMap.get("city_num")+"\t");
					lhm.put("7", eachMap.get("chnl_num")+"\t");
					lhm.put("8", eachMap.get("qdyk_chnl_num")+"\t");
					lhm.put("9", eachMap.get("qdyk_chnl_perc")+"\t");
					lhm.put("10", eachMap.get("qdyk_subs_num")+"\t");
					lhm.put("11", eachMap.get("qdyk_subs_num_rank")+"\t");
					lhm.put("12", eachMap.get("ent_num")+"\t");
					lhm.put("13", eachMap.get("qdyk_num_perc")+"\t");
					lhm.put("14", eachMap.get("qdyk_num_perc_rank")+"\t");
					lhm.put("15", eachMap.get("nation_qdyk_subs_num")+"\t");
					lhm.put("16", eachMap.get("nation_ent_num")+"\t");
					lhm.put("17", eachMap.get("nation_qdyk_num_perc")+"\t");
					lhm.put("18", eachMap.get("prov_qdyk_num_nation_perc")+"\t");
					exportData.add(lhm);
				}
			}
		    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			    map.put("1", "审计月");
				map.put("2", "省代码");
				map.put("3", "省名称");
				map.put("4", "渠道类型编码");
				map.put("5", "渠道类型名称");
				map.put("6", "地市公司数量");
				map.put("7", "本省份的渠道数量");
				map.put("8", "有疑似养卡用户的渠道数量");
				map.put("9", "有违规行为的渠道数量占所有渠道的比例");
				map.put("10", "疑似养卡用户数");
				map.put("11", "疑似养卡用户数排名");
				map.put("12", "新入网用户数");
				map.put("13", "疑似养卡用户数占新入网用户数的比例(%)");
				map.put("14", "疑似养卡用户数占新入网用户数的比例在全国范围的排名");
				map.put("15", "本审计月全国的疑似养卡用户数");
				map.put("16", "本审计月全国新入网用户数");
				map.put("17", "全国疑似养卡用户数占全国新入网用户数的比例");
				map.put("18", "本省疑似养卡用户数占全国疑似养卡用户数的比例");
			String fileName = "渠道养卡_渠道养卡_渠道养卡用户数量波动趋势_汇总.csv";
			CSVUtils.exportCSVList(fileName, exportData, map, request, response);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
    

	@ResponseBody
	@RequestMapping(value="dsqdykyhtjCharts")
	public Map<String, Object> dsqdykyhtjCharts(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Map<String, Object>> cmccProvIdList = qdyk2401Service.dsqdykyhtjCharts(params);
		List<String> cmccProvIds = new ArrayList<String>();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if(cmccProvIdList.size()>0){
			for(Map<String, Object> cmccProvId : cmccProvIdList){
				cmccProvIds.add(cmccProvId.get("cmcc_prov_id").toString());
			}
			params.put("cmccProvIds", cmccProvIds);
			List<Map<String, Object>> qdlist = qdyk2401Service.qdykyhslhz(params);
			//结论
			List<Map<String, Object>> conList = qdyk2401Service.dsqdykyhtjCon(params);
			returnMap.put("cmccProvIdList", cmccProvIdList);
			returnMap.put("qdlist", qdlist);
			returnMap.put("conList", conList);
		}else{
			returnMap.put("cmccProvIdList", cmccProvIdList);
			returnMap.put("qdlist", null);
			returnMap.put("conList", null);
		}
		return returnMap;
	}
    
	@ResponseBody
    @RequestMapping(value = "/loadDsxefzcz_TabDetailTable")
	public Pager loadDsxefzcz_TabDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		pager.setParams(parameterMap);
		List<Map<String, Object>> list = qdyk2401Service.loadDsxefzcz_TabDetailTable(pager);
		pager.setDataRows(list);
		return pager;
	}
	
	@ResponseBody
    @RequestMapping(value = "/exportDsxefzczDetail")
	public void exportDsxefzczDetail(HttpServletRequest request, HttpServletResponse response){
		try{
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    List<Map<String, Object>> list = qdyk2401Service.exportDsxefzczDetail(parameterMap);
		    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
		    if (list != null && list.size() > 0) {
		    	LinkedHashMap<String, Object> lhm = null;
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> eachMap = list.get(i);
					lhm = new LinkedHashMap<String, Object>();
					lhm.put("1", eachMap.get("aud_trm"));
					lhm.put("2", eachMap.get("cmcc_prov_prvd_id"));
					lhm.put("3", eachMap.get("short_name"));
					lhm.put("4", eachMap.get("cmcc_prov_id"));
					lhm.put("5", eachMap.get("cmcc_prvd_nm_short")+"\t");
					lhm.put("6", eachMap.get("chnl_id")+"\t");
					lhm.put("7", eachMap.get("chnl_nm")+"\t");
					lhm.put("8", eachMap.get("chnl_class")+"\t");
					lhm.put("9", eachMap.get("chnl_class_nm")+"\t");
					lhm.put("10", eachMap.get("chal_stat")+"\t");
					lhm.put("11", eachMap.get("rase_crd_qty")+"\t");
					lhm.put("12", eachMap.get("tol_users")+"\t");
					exportData.add(lhm);
				}
			}
		    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			    map.put("1", "审计月");
				map.put("2", "省代码");
				map.put("3", "省名称");
				map.put("4", "地市代码");
				map.put("5", "地市名称");
				map.put("6", "渠道编码");
				map.put("7", "渠道名称");
				map.put("8", "渠道类型编码");
				map.put("9", "渠道类型名称");
				map.put("10", "渠道状态");
				map.put("11", "疑似养卡用户数");
				map.put("12", "新入网用户数");
			String fileName = "渠道养卡_渠道养卡_地市渠道养卡用户数量统计_汇总.csv";
			CSVUtils.exportCSVList(fileName, exportData, map, request, response);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	
	@ResponseBody
    @RequestMapping(value = "/getCityDetailPagerList")
	public Pager getCityDetailPagerList(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		pager.setParams(parameterMap);
		List<Map<String, Object>> list = qdyk2401Service.getCityDetailPagerList(pager);
		pager.setDataRows(list);
		return pager;
	}
	
	@ResponseBody
    @RequestMapping(value = "/exportMxDetailList")
	public void exportMxDetailList(HttpServletRequest request, HttpServletResponse response){
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    try {
				qdyk2401Service.exportMxDetailList(request, response,parameterMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
}
