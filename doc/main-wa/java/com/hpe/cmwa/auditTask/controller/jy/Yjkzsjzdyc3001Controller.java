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

import com.hpe.cmwa.auditTask.service.jy.YJKZCYC3001Service;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.CSVUtils;

/**
 * 有价卡赠送集中度异常(3001)
 * @author yuzhinan
 */
@Controller
@RequestMapping("/yjkzsjzdyc/3001")
public class Yjkzsjzdyc3001Controller extends BaseController {
	
	
	 @Autowired
	 private YJKZCYC3001Service yjkzcyc3001Service;

	private DecimalFormat df = new DecimalFormat("######0.00");

	/**
	 * 初始化界面
	 * @return
	 */
	@RequestMapping(value = "/index")
    public String index(HttpServletRequest request) {
		if(request.getParameter("provinceCode").equals("10000")){
			return "auditTask/sjk/yjkzsxgjzdyc_qg/yjkzsjzdyc_qg";
		}else{
			return "auditTask/jy/yjk/yjkzsjzdyc3001";
		}
		
    }
	
	/**
     * 有价卡赠送波动趋势图
     * @param request
     * @author yuzhinan
     */
	@ResponseBody
	@RequestMapping(value = "/getYJKYC3001Trend")
	 public  Map<String, Object> getYJKYC3001Trend(HttpServletRequest request) {
		
		Map<String, Object> parameterMap = this.getParameterMap(request);
		 List<Map<String, Object>> list = yjkzcyc3001Service.getYJKZCYC3001Trend(parameterMap);
		 //计算平均值
		 Map<String, Object> avgMap = yjkzcyc3001Service.getYJKZCYC3001AVGNumber(parameterMap);
		 //计算周期内最大赠送金额
		 List<Map<String, Object>> listMax = yjkzcyc3001Service.getYJKZCYC3001MAXNumber(parameterMap);
		 
		 Map<String, Object> maxMap = new HashMap<String, Object>();
		 Double maxCount_zsyjkje;
		 String maxShort_name="";
		 String maxYjk_pres_dt="";
		 Double highAvg;
		 
		 if(listMax.size()==0){
			 maxCount_zsyjkje=0.00;
			 maxShort_name=null;
			 maxYjk_pres_dt=null;
		 }else{
			 maxCount_zsyjkje = Double.parseDouble(listMax.get(0).get("max_count_zsyjkje").toString());
			 maxShort_name = listMax.get(0).get("short_name").toString();
			 maxYjk_pres_dt = listMax.get(0).get("yjk_pres_dt").toString();
		 }
		
	     Double avgNumber = avgMap.get("avg_count_zsyjkje")==null?0.00:Double.parseDouble(avgMap.get("avg_count_zsyjkje").toString());
	     
	     if(avgNumber==0.00){
	    	 highAvg=0.00;
	     }else if(maxCount_zsyjkje==0.00){
	    	 highAvg=0.00;
	     }else{
	    	 highAvg = (maxCount_zsyjkje-avgNumber)/avgNumber;
	     }
		 
		 Map<String, Object> returnMap = new HashMap<String, Object>();
		 maxMap.put("highAvg", df.format(highAvg*100)+"%");
		 maxMap.put("maxCount_zsyjkje", maxCount_zsyjkje);
		 maxMap.put("maxShort_name", maxShort_name);
		 maxMap.put("maxYjk_pres_dt", maxYjk_pres_dt);
	     returnMap.put("data", list);
	     returnMap.put("avgMap", avgMap);
	     returnMap.put("maxMap", maxMap);
		 return returnMap;
	 }
	/**
	 * 各赠送区间有价卡统计
	 * @param request
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/getGzsqjChart")
    public Map<String, Object> getGzsqjChart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		Map<String, Object> resultMap = yjkzcyc3001Service.getGzsqjChart(params);
		if(resultMap != null){
			if(!resultMap.containsKey("sum_bt1_2000_zsyjkje")){
				resultMap.put("sum_bt1_2000_zsyjkje", 0);
				resultMap.put("sum_je1_2000_zsyhsl", 0);
			}
			if(!resultMap.containsKey("sum_bt2000_5000_zsyjkje")){
				resultMap.put("sum_bt2000_5000_zsyjkje", 0);
				resultMap.put("sum_je2000_5000_zsyhsl", 0);
			}
			if(!resultMap.containsKey("sum_bt5000_10000_zsyjkje")){
				resultMap.put("sum_bt5000_10000_zsyjkje", 0);
				resultMap.put("sum_je5000_10000_zsyhsl", 0);
			}
			if(!resultMap.containsKey("sum_bt10000_20000_zsyjkje")){
				resultMap.put("sum_bt10000_20000_zsyjkje", 0);
				resultMap.put("sum_je10000_20000_zsyhsl", 0);
			}
			if(!resultMap.containsKey("sum_bt20000_50000_zsyjkje")){
				resultMap.put("sum_bt20000_50000_zsyjkje", 0);
				resultMap.put("sum_je20000_50000_zsyhsl", 0);
			}
			if(!resultMap.containsKey("sum_bt50000_100000_zsyjkje")){
				resultMap.put("sum_bt50000_100000_zsyjkje", 0);
				resultMap.put("sum_je50000_100000_zsyhsl", 0);
			}
			if(!resultMap.containsKey("sum_gt100000_zsyjkje")){
				resultMap.put("sum_gt100000_zsyjkje", 0);
				resultMap.put("sum_gt100000_zsyhsl", 0);
			}
		}else{
			resultMap = new HashMap<String, Object>();
			resultMap.put("sum_bt1_2000_zsyjkje", 0);
			resultMap.put("sum_je1_2000_zsyhsl", 0);
			resultMap.put("sum_bt2000_5000_zsyjkje", 0);
			resultMap.put("sum_je2000_5000_zsyhsl", 0);
			resultMap.put("sum_bt5000_10000_zsyjkje", 0);
			resultMap.put("sum_je5000_10000_zsyhsl", 0);
			resultMap.put("sum_bt10000_20000_zsyjkje", 0);
			resultMap.put("sum_je10000_20000_zsyhsl", 0);
			resultMap.put("sum_bt20000_50000_zsyjkje", 0);
			resultMap.put("sum_je20000_50000_zsyhsl", 0);
			resultMap.put("sum_bt50000_100000_zsyjkje", 0);
			resultMap.put("sum_je50000_100000_zsyhsl", 0);
			resultMap.put("sum_gt100000_zsyjkje", 0);
			resultMap.put("sum_gt100000_zsyhsl", 0);
		}
		return resultMap;
    }
	
	@ResponseBody
    @RequestMapping(value = "/getGzsqjyhDetailTable")
	public Pager getGzsqjyhDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		pager.setParams(parameterMap);
		List<Map<String, Object>> list = yjkzcyc3001Service.getGzsqjyhDetailTable(pager);
		pager.setDataRows(list);
		return pager;
	}
	/**
	 * 用户获赠有价卡金额统计top10
	 * @param request
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/getYhhzTop10TelCharts")
    public Map<String, Object> getYhhzTop10TelCharts(HttpServletRequest request){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		List<Map<String, Object>> list = yjkzcyc3001Service.getYhhzTop10TelCharts(parameterMap);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("data", list);
		return returnMap;
	}
	
	/**
	 * 用户获赠有价卡金额统计(数据表2详情表)
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/getYhhzTelDetailTable")
	public Pager getYhhzTelDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
			pager.setParams(parameterMap);
			List<Map<String, Object>> list = yjkzcyc3001Service.getYhhzTelDetailTable(pager);
			pager.setDataRows(list);
			return pager;
	}
	
	 @ResponseBody
	 @RequestMapping(value = "/exportYhhzTelDetailList")
	 public void exportYhhzTelDetailList(HttpServletRequest request, HttpServletResponse response) throws Exception {
				Map<String, Object> parameterMap = this.getParameterMap(request);
			    yjkzcyc3001Service.exportYhhzTelDetailList(request, response,parameterMap);
	 }
	
	
	/**
	 * 用户获赠有价卡金额统计(详情表 导出)
	 * @param request
	 * @param response
	 */
	@ResponseBody
    @RequestMapping(value = "/exportYhhzDetail")
	public void exportYhhzDetail(HttpServletRequest request, HttpServletResponse response){
		try{
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    List<Map<String, Object>> list = yjkzcyc3001Service.exportYhhzDetail(parameterMap);
		    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
		    if (list != null && list.size() > 0) {
		    	LinkedHashMap<String, Object> lhm = null;
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> eachMap = list.get(i);
					lhm = new LinkedHashMap<String, Object>();
					lhm.put("1", eachMap.get("aud_trm_begin"));
					lhm.put("2", eachMap.get("aud_trm_end"));
					lhm.put("3", eachMap.get("cmcc_prov_prvd_id"));
					lhm.put("4", eachMap.get("short_name"));
					lhm.put("5", eachMap.get("cmcc_prov_id"));
					lhm.put("6", eachMap.get("cmcc_prvd_nm_short"));
					lhm.put("7", eachMap.get("trade_msisdn"));
					lhm.put("8", eachMap.get("sum_yjk_trade_num"));
					lhm.put("9", eachMap.get("sum_yjk_trade_amt")+"\t");
					exportData.add(lhm);
				}
			}
		    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			    map.put("1", "审计起始月");
				map.put("2", "审计结束月");
				map.put("3", "省代码");
				map.put("4", "省名称");
				map.put("5", "地市代码");
				map.put("6", "地市名称");
				map.put("7", "手机号码");
				map.put("8", "累计赠送有价卡数量");
				map.put("9", "累计赠送有价卡金额(元)");
			String fileName = "有价卡赠送相关集中度异常_有价卡赠送集中度异常_用户获赠有价卡金额统计_汇总.csv";
			CSVUtils.exportCSVList(fileName, exportData, map, request, response);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * 用户获赠有价卡金额统计(详情表)
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/getYhhzyjkDetailTable")
	public Pager getYhhzyjkDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
			pager.setParams(parameterMap);
			List<Map<String, Object>> list = yjkzcyc3001Service.getYhhzyjkDetailTable(pager);
			pager.setDataRows(list);
			return pager;
	}
	
	/**
	 * 渠道赠送有价卡金额统计top10
	 * @param request
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/getQdzsyjkTop10Charts")
    public Map<String, Object> getQdzsyjkTop10Charts(HttpServletRequest request){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		List<Map<String, Object>> list = yjkzcyc3001Service.getQdzsyjkTop10Charts(parameterMap);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("data", list);
		return returnMap;
	}
	/**
	 * 渠道赠送有价卡金额统计(详情表)
	 */
	@ResponseBody
    @RequestMapping(value = "/getQdzsyjkDetailTable")
    public Pager getQdzsyjkDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
			pager.setParams(parameterMap);
			List<Map<String, Object>> list = yjkzcyc3001Service.getQdzsyjkDetailTable(pager);
			pager.setDataRows(list);
			return pager;
    }
	/**
	 * 渠道赠送有价卡金额统计(导出)
	 * @param request
	 * @param response
	 */
	@ResponseBody
    @RequestMapping(value = "/exportQdzsDetail")
	public void exportQdzsDetail(HttpServletRequest request, HttpServletResponse response){
		try{
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    List<Map<String, Object>> list = yjkzcyc3001Service.exportQdzsDetail(parameterMap);
		    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
		    if (list != null && list.size() > 0) {
		    	LinkedHashMap<String, Object> lhm = null;
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> eachMap = list.get(i);
					lhm = new LinkedHashMap<String, Object>();
					lhm.put("1", eachMap.get("aud_trm_begin"));
					lhm.put("2", eachMap.get("aud_trm_end"));
					lhm.put("3", eachMap.get("cmcc_prov_prvd_id"));
					lhm.put("4", eachMap.get("short_name"));
					lhm.put("5", eachMap.get("cmcc_prov_id"));
					lhm.put("6", eachMap.get("cmcc_prvd_nm_short"));
					lhm.put("7", eachMap.get("cor_chnl_typ"));
					lhm.put("8", eachMap.get("cor_chnl_nm"));
					lhm.put("9", eachMap.get("count_zsyhsl"));
					lhm.put("10", eachMap.get("count_zsyjksl"));
					lhm.put("11", eachMap.get("count_zsyjkje")+"\t");
					exportData.add(lhm);
				}
			}
		    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			    map.put("1", "审计起始月");
				map.put("2", "审计结束月");
				map.put("3", "省代码");
				map.put("4", "省名称");
				map.put("5", "地市代码");
				map.put("6", "地市名称");
				map.put("7", "赠送渠道标识");
				map.put("8", "赠送渠道名称");
				map.put("9", "累计赠送用户数量");
				map.put("10", "累计赠送有价卡数量");
				map.put("11", "累计赠送有价卡金额(元)");
			String fileName = "有价卡赠送相关集中度异常_有价卡赠送集中度异常_渠道赠送有价卡金额统计_汇总.csv";
			CSVUtils.exportCSVList(fileName, exportData, map, request, response);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * 营销案赠送有价卡金额统计
	 * @param request
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/getYxazsyjkTop10Chart")
    public Map<String, Object> getYxazsyjkTop10Chart(HttpServletRequest request){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		List<Map<String, Object>> list = yjkzcyc3001Service.getYxazsyjkTop10Chart(parameterMap);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("data", list);
		return returnMap;
	}
	/**
	 * 营销案赠送有价卡金额统计(详情表)
	 */
	@ResponseBody
    @RequestMapping(value = "/getYxazsyjkDetailTable")
    public Pager getYxazsyjkDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
			pager.setParams(parameterMap);
			List<Map<String, Object>> list = yjkzcyc3001Service.getYxazsyjkDetailTable(pager);
			pager.setDataRows(list);
			return pager;
		
    }
	/**
	 * 营销案赠送有价卡金额统计(导出)
	 * @param request
	 * @param response
	 */
	@ResponseBody
    @RequestMapping(value = "/exportYxazsDetail_csv")
	public void exportYxazsDetail(HttpServletRequest request, HttpServletResponse response){
		try{
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    List<Map<String, Object>> list = yjkzcyc3001Service.exportYxazsDetail(parameterMap);
		    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
		    if (list != null && list.size() > 0) {
		    	LinkedHashMap<String, Object> lhm = null;
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> eachMap = list.get(i);
					lhm = new LinkedHashMap<String, Object>();
					lhm.put("1", eachMap.get("aud_trm_begin"));
					lhm.put("2", eachMap.get("aud_trm_end"));
					lhm.put("3", eachMap.get("cmcc_prov_prvd_id"));
					lhm.put("4", eachMap.get("short_name"));
					lhm.put("5", eachMap.get("cmcc_prov_id"));
					lhm.put("6", eachMap.get("cmcc_prvd_nm_short"));
					lhm.put("7", eachMap.get("yjk_offer_cd"));
					lhm.put("8", eachMap.get("yjk_offer_nm"));
					lhm.put("9", eachMap.get("sum_count_zsyhsl"));
					lhm.put("10", eachMap.get("sum_count_zsyjksl"));
					lhm.put("11", eachMap.get("sum_count_zsyjkje")+"\t");
					exportData.add(lhm);
				}
			}
		    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			    map.put("1", "审计起始月");
				map.put("2", "审计结束月");
				map.put("3", "省代码");
				map.put("4", "省名称");
				map.put("5", "地市代码");
				map.put("6", "地市名称");
				map.put("7", "有价卡赠送涉及的营销案编号");
				map.put("8", "营销案名称");
				map.put("9", "累计赠送用户数量");
				map.put("10", "累计赠送有价卡数量");
				map.put("11", "累计赠送有价卡金额(元)");
			String fileName = "有价卡赠送相关集中度异常_有价卡赠送集中度异常_营销案赠送有价卡金额统计_汇总.csv";
			CSVUtils.exportCSVList(fileName, exportData, map, request, response);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	
	 @ResponseBody
	 @RequestMapping(value = "/getCityDetailPagerList")
	 public Pager getCityDetailPagerList(HttpServletRequest request, Pager pager) {
	        pager.setParams(this.getParameterMap(request));
	        List<Map<String, Object>> dataRecords = yjkzcyc3001Service.getCityDetailPagerList(pager);
	        pager.setDataRows(dataRecords);
	        return pager;
	}
	
	 @ResponseBody
	 @RequestMapping(value = "/exportMxDetailList")
	 public void exportMxDetailList(HttpServletRequest request, HttpServletResponse response) throws Exception {
				Map<String, Object> parameterMap = this.getParameterMap(request);
			    yjkzcyc3001Service.exportMxDetailList(request, response,parameterMap);
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
