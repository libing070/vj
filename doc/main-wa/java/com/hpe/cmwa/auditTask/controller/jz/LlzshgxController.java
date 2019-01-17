package com.hpe.cmwa.auditTask.controller.jz;

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

import com.hpe.cmwa.auditTask.service.jz.LlzshgxService;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.CSVUtils;


@Controller
@RequestMapping("/llzshgx")
public class LlzshgxController  extends BaseController{
	
	@Autowired
	private LlzshgxService llzshgxservice;
	
	/**
	 * 初始化界面
	 * @return
	 */
	@RequestMapping(value = "/index")
    public String index() {
		return "auditTask/jz/llcphgx/llzshgx";
    }
	
	
	
	@ResponseBody
    @RequestMapping(value = "/getUserCon")
    public Map<String, Object> getUserCon(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		//获取该省市数据库中最大最小审计月
				Map<String, Object> resultTimeMap = llzshgxservice.getMaxAndMinAud_trm(params);
				//返回结果集
				Map<String, Object> resultMap = new HashMap<String, Object>();
				if(resultTimeMap==null){
					resultMap.put("data", null);
				}else{
					
					if(resultTimeMap.get("min_aud_trm").equals(null)||resultTimeMap.get("min_aud_trm").equals("")){
						params.put("hz_startMonth", null);
					}else{
						params.put("hz_startMonth", resultTimeMap.get("min_aud_trm"));
					}
					if(resultTimeMap.get("max_aud_trm").equals(null)||resultTimeMap.get("max_aud_trm").equals("")){
						params.put("hz_endMonth",null);
					}else{
						params.put("hz_endMonth", resultTimeMap.get("max_aud_trm"));
					}
					
					this.logger.info("LlzshgxController:getFbqjChart时间参数为: hz_startMonth=["+params.get("hz_startMonth")+"], and hz_endMonth is [" +params.get("hz_endMonth") +"]");
					
					if(!params.get("hz_endMonth").equals(null)){
						Map<String, Object> map =llzshgxservice.getUserCon(params);
						resultMap.put("data", map);
					}else{
						resultMap.put("data", null);
					}
				}
				return resultMap;
	}
	
	/**
	 * 各赠送区间有价卡统计
	 * @param request
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/getFbqjChart")
    public Map<String, Object> getFbqjChart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		//获取该省市数据库中最大最小审计月
		Map<String, Object> resultTimeMap = llzshgxservice.getMaxAndMinAud_trm(params);
		//返回结果集
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(resultTimeMap==null){
			resultMap.put("data", null);
		}else{
			
			if(resultTimeMap.get("min_aud_trm").equals(null)||resultTimeMap.get("min_aud_trm").equals("")){
				params.put("hz_startMonth", null);
			}else{
				params.put("hz_startMonth", resultTimeMap.get("min_aud_trm"));
			}
			if(resultTimeMap.get("max_aud_trm").equals(null)||resultTimeMap.get("max_aud_trm").equals("")){
				params.put("hz_endMonth",null);
			}else{
				params.put("hz_endMonth", resultTimeMap.get("max_aud_trm"));
			}
			
			this.logger.info("LlzshgxController:getFbqjChart时间参数为: hz_startMonth=["+params.get("hz_startMonth")+"], and hz_endMonth is [" +params.get("hz_endMonth") +"]");
			
			if(!params.get("hz_endMonth").equals(null)){
				List<Map<String, Object>> list =llzshgxservice.getFbqjChart(params);
				resultMap.put("data", list);
			}else{
				resultMap.put("data", null);
			}
		}
		return resultMap;
	}
	
	
	@ResponseBody
    @RequestMapping(value = "/getZsllyhChart")
    public Map<String, Object> getZsllyhChart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		//获取该省市数据库中最大最小审计月
		Map<String, Object> resultTimeMap = llzshgxservice.getZsllyhMaxAndMinAud_trm(params);
		//返回结果集
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(resultTimeMap==null){
			resultMap.put("data", null);
			resultMap.put("count_user_num",null); 
			resultMap.put("end_aud_trm",null);
		}else{
			
			if(resultTimeMap.get("min_aud_trm").equals(null)||resultTimeMap.get("min_aud_trm").equals("")){
				params.put("hz_startMonth", null);
			}else{
				params.put("hz_startMonth", resultTimeMap.get("min_aud_trm"));
			}
			if(resultTimeMap.get("max_aud_trm").equals(null)||resultTimeMap.get("max_aud_trm").equals("")){
				params.put("hz_endMonth",null);
			}else{
				params.put("hz_endMonth", resultTimeMap.get("max_aud_trm"));
			}
			
			this.logger.info("LlzshgxController:getZsllyhChart时间参数为: hz_startMonth=["+params.get("hz_startMonth")+"], and hz_endMonth is [" +params.get("hz_endMonth") +"]");
			
			if(!params.get("hz_endMonth").equals(null)){
				List<Map<String, Object>> list =llzshgxservice.getZsllyhChart(params);
				List<Map<String, Object>> listCon =llzshgxservice.getZsllyhCon(params);
				String count_user_num = "";
				String end_aud_trm = "";
				if(listCon.size()>0){
					count_user_num = listCon.get(0).get("count_user_num").toString();
					end_aud_trm = listCon.get(0).get("aud_trm").toString();
				}else{
					count_user_num = null;
					end_aud_trm = null;
				}
				resultMap.put("data", list);
				resultMap.put("count_user_num",count_user_num);
				resultMap.put("end_aud_trm",end_aud_trm);
			}else{
				resultMap.put("data", null);
				resultMap.put("count_user_num",null);
				resultMap.put("end_aud_trm",null);
			}
		}
		return resultMap;
	}
	
	@ResponseBody
    @RequestMapping(value = "/loadZsllyDetailTable")
    public Pager loadZsllyDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager) {
		Map<String, Object> params = this.getParameterMap(request);
		//获取该省市数据库中最大最小审计月
		Map<String, Object> resultTimeMap = llzshgxservice.getZsllyhMaxAndMinAud_trm(params);
		//返回结果集
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(resultTimeMap==null){
			pager.setDataRows(list);
			return pager;
		}else{
			
			if(resultTimeMap.get("min_aud_trm").equals(null)||resultTimeMap.get("min_aud_trm").equals("")){
				params.put("hz_startMonth", null);
			}else{
				params.put("hz_startMonth", resultTimeMap.get("min_aud_trm"));
			}
			
			if(resultTimeMap.get("max_aud_trm").equals(null)||resultTimeMap.get("max_aud_trm").equals("")){
				params.put("hz_endMonth",null);
			}else{
				params.put("hz_endMonth", resultTimeMap.get("max_aud_trm"));
			}
			
			this.logger.info("LlzshgxController:loadZsllyDetailTable时间参数为: hz_startMonth=["+params.get("hz_startMonth")+"], and hz_endMonth is [" +params.get("hz_endMonth") +"]");
			
			if(!params.get("hz_endMonth").equals(null)){
				String hz_startMonth = params.get("hz_endMonth").toString();
				hz_startMonth = hz_startMonth.substring(0, 4)+"01";
				params.put("hz_startMonth", hz_startMonth);
				pager.setParams(params);
				list =llzshgxservice.loadZsllyDetailTable(pager);
				pager.setDataRows(list);
			}else{
				pager.setDataRows(list);
				return pager;
			}
		}
		return pager;
	}
	
	
	@ResponseBody
    @RequestMapping(value = "/exportZsllyhDetail")
    public void exportZsllyhDetail(HttpServletRequest request, HttpServletResponse response){
		try{
			Map<String, Object> params = this.getParameterMap(request);
			//获取该省市数据库中最大最小审计月
			Map<String, Object> resultTimeMap = llzshgxservice.getZsllyhMaxAndMinAud_trm(params);
			if(resultTimeMap==null){
				List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			    map.put("1", "审计区间");
				map.put("2", "省代码");
				map.put("3", "省名称");
				map.put("4", "地市代码");
				map.put("5", "地市名称");
				map.put("6", "用户标识");
				map.put("7", "年度累计赠送流量（G）");
			String fileName = "3.4_流量产品管理合规性_流量赠送合规性_用户统计_汇总.csv";
			CSVUtils.exportCSVList(fileName, exportData, map, request, response);
			}else{
				
				if(resultTimeMap.get("min_aud_trm").equals(null)||resultTimeMap.get("min_aud_trm").equals("")){
					params.put("hz_startMonth", null);
				}else{
					params.put("hz_startMonth", resultTimeMap.get("min_aud_trm"));
				}
				
				if(resultTimeMap.get("max_aud_trm").equals(null)||resultTimeMap.get("max_aud_trm").equals("")){
					params.put("hz_endMonth",null);
				}else{
					params.put("hz_endMonth", resultTimeMap.get("max_aud_trm"));
				}
				
				this.logger.info("LlzshgxController:exportZsllyhDetail时间参数为: hz_startMonth=["+params.get("hz_startMonth")+"], and hz_endMonth is [" +params.get("hz_endMonth") +"]");
				if(!params.get("hz_endMonth").equals(null)){
					String hz_startMonth = params.get("hz_endMonth").toString();
					hz_startMonth = hz_startMonth.substring(0, 4)+"01";
					params.put("hz_startMonth", hz_startMonth);
					llzshgxservice.exportZsllyhDetail(request, response,params);
				}else{
					List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
					LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				    map.put("1", "审计区间");
					map.put("2", "省代码");
					map.put("3", "省名称");
					map.put("4", "地市代码");
					map.put("5", "地市名称");
					map.put("6", "用户标识");
					map.put("7", "年度累计赠送流量（G）");
				String fileName = "3.4_流量产品管理合规性_流量赠送合规性_用户统计_汇总.csv";
				CSVUtils.exportCSVList(fileName, exportData, map, request, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@ResponseBody
    @RequestMapping(value = "/getLlyxzCharts")
    public Map<String, Object> getLlyxzCharts(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		//获取该省市数据库中最大最小审计月
		Map<String, Object> resultTimeMap = llzshgxservice.getLlyxzMaxAndMinAud_trm(params);
		//返回结果集
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(resultTimeMap==null){
			resultMap.put("data", null);
			resultMap.put("count_offer_cd_num",null); 
			resultMap.put("end_aud_trm",null);
		}else{
			
			if(resultTimeMap.get("min_aud_trm").equals(null)||resultTimeMap.get("min_aud_trm").equals("")){
				params.put("hz_startMonth", null);
			}else{
				params.put("hz_startMonth", resultTimeMap.get("min_aud_trm"));
			}
			if(resultTimeMap.get("max_aud_trm").equals(null)||resultTimeMap.get("max_aud_trm").equals("")){
				params.put("hz_endMonth",null);
			}else{
				params.put("hz_endMonth", resultTimeMap.get("max_aud_trm"));
			}
			
			this.logger.info("LlzshgxController:getLlyxzCharts时间参数为: hz_startMonth=["+params.get("hz_startMonth")+"], and hz_endMonth is [" +params.get("hz_endMonth") +"]");
			
			if(!params.get("hz_endMonth").equals(null)){
				List<Map<String, Object>> list =llzshgxservice.getLlyxzCharts(params);
				List<Map<String, Object>> listCon =llzshgxservice.getLlyxzCon(params);
				String count_offer_cd_num = "";
				String end_aud_trm = "";
				if(listCon.size()>0){
					count_offer_cd_num = listCon.get(0).get("count_offer_cd_num").toString();
					end_aud_trm = listCon.get(0).get("aud_trm").toString();
				}else{
					count_offer_cd_num = null;
					end_aud_trm = null;
				}
				resultMap.put("data", list);
				resultMap.put("count_offer_cd_num",count_offer_cd_num);
				resultMap.put("end_aud_trm",end_aud_trm);
			}else{
				resultMap.put("data", null);
				resultMap.put("count_offer_cd_num",null);
				resultMap.put("end_aud_trm",null);
			}
		}
		return resultMap;
	}
	
	
	@ResponseBody
    @RequestMapping(value = "/loadLlyxzDetailTable")
    public Pager loadLlyxzDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager) {
		Map<String, Object> params = this.getParameterMap(request);
		//获取该省市数据库中最大最小审计月
		Map<String, Object> resultTimeMap = llzshgxservice.getLlyxzMaxAndMinAud_trm(params);
		//返回结果集
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(resultTimeMap==null){
			pager.setDataRows(list);
			return pager;
		}else{
			
			if(resultTimeMap.get("min_aud_trm").equals(null)||resultTimeMap.get("min_aud_trm").equals("")){
				params.put("hz_startMonth", null);
			}else{
				params.put("hz_startMonth", resultTimeMap.get("min_aud_trm"));
			}
			
			if(resultTimeMap.get("max_aud_trm").equals(null)||resultTimeMap.get("max_aud_trm").equals("")){
				params.put("hz_endMonth",null);
			}else{
				params.put("hz_endMonth", resultTimeMap.get("max_aud_trm"));
			}
			
			this.logger.info("LlzshgxController:loadLlyxzDetailTable时间参数为: hz_startMonth=["+params.get("hz_startMonth")+"], and hz_endMonth is [" +params.get("hz_endMonth") +"]");
			
			if(!params.get("hz_endMonth").equals(null)){
				String hz_startMonth = params.get("hz_endMonth").toString();
				hz_startMonth = hz_startMonth.substring(0, 4)+"01";
				params.put("hz_startMonth", hz_startMonth);
				pager.setParams(params);
				list =llzshgxservice.loadLlyxzDetailTable(pager);
				pager.setDataRows(list);
			}else{
				pager.setDataRows(list);
				return pager;
			}
		}
		return pager;
	}
	
	
	@ResponseBody
    @RequestMapping(value = "/exportLlyxzDetail")
    public void exportLlyxzDetail(HttpServletRequest request, HttpServletResponse response){
		try{
			Map<String, Object> params = this.getParameterMap(request);
			//获取该省市数据库中最大最小审计月
			Map<String, Object> resultTimeMap = llzshgxservice.getLlyxzMaxAndMinAud_trm(params);
			if(resultTimeMap==null){
				List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			    map.put("1", "审计区间");
				map.put("2", "省代码");
				map.put("3", "省名称");
				map.put("4", "地市代码");
				map.put("5", "地市名称");
				map.put("6", "用户标识");
				map.put("7", "营销案编码");
				map.put("8", "营销案名称");
				map.put("9", "办理渠道标识");
				map.put("10", "办理渠道名称");
				String fileName = "3.4_流量产品管理合规性_流量赠送合规性_营销案统计_汇总.csv";
				CSVUtils.exportCSVList(fileName, exportData, map, request, response);
			}else{
				
				if(resultTimeMap.get("min_aud_trm").equals(null)||resultTimeMap.get("min_aud_trm").equals("")){
					params.put("hz_startMonth", null);
				}else{
					params.put("hz_startMonth", resultTimeMap.get("min_aud_trm"));
				}
				
				if(resultTimeMap.get("max_aud_trm").equals(null)||resultTimeMap.get("max_aud_trm").equals("")){
					params.put("hz_endMonth",null);
				}else{
					params.put("hz_endMonth", resultTimeMap.get("max_aud_trm"));
				}
				
				this.logger.info("LlzshgxController:exportLlyxzDetail时间参数为: hz_startMonth=["+params.get("hz_startMonth")+"], and hz_endMonth is [" +params.get("hz_endMonth") +"]");
				if(!params.get("hz_endMonth").equals(null)){
					String hz_startMonth = params.get("hz_endMonth").toString();
					hz_startMonth = hz_startMonth.substring(0, 4)+"01";
					params.put("hz_startMonth", hz_startMonth);
					llzshgxservice.exportLlyxzDetail(request, response,params);
				}else{
					List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
					LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("1", "审计区间");
					map.put("2", "省代码");
					map.put("3", "省名称");
					map.put("4", "地市代码");
					map.put("5", "地市名称");
					map.put("6", "用户标识");
					map.put("7", "营销案编码");
					map.put("8", "营销案名称");
					map.put("9", "办理渠道标识");
					map.put("10", "办理渠道名称");
					String fileName = "3.4_流量产品管理合规性_流量赠送合规性_营销案统计_汇总.csv";
					CSVUtils.exportCSVList(fileName, exportData, map, request, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
//	getCityDetailPagerList
	@ResponseBody
    @RequestMapping(value = "/getCityDetailPagerList")
	public Pager getCityDetailPagerList(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> params = this.getParameterMap(request);
		//获取该省市数据库中最大最小审计月
		Map<String, Object> resultTimeMap = llzshgxservice.getCityDetailMaxAndMinAud_trm(params);
		//返回结果集
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(resultTimeMap==null){
			pager.setDataRows(list);
			return pager;
		}else{
			
			if(resultTimeMap.get("min_aud_trm").equals(null)||resultTimeMap.get("min_aud_trm").equals("")){
				params.put("mx_startMonth", null);
			}else{
				params.put("mx_startMonth", resultTimeMap.get("min_aud_trm"));
			}
			
			if(resultTimeMap.get("max_aud_trm").equals(null)||resultTimeMap.get("max_aud_trm").equals("")){
				params.put("mx_endMonth",null);
			}else{
				params.put("mx_endMonth", resultTimeMap.get("max_aud_trm"));
			}
			
			this.logger.info("LlzshgxController:loadLlyxzDetailTable时间参数为: mx_startMonth=["+params.get("mx_startMonth")+"], and mx_endMonth is [" +params.get("mx_endMonth") +"]");
			
			if(!params.get("mx_endMonth").equals(null)){
				String mx_startMonth = params.get("mx_endMonth").toString();
				mx_startMonth = mx_startMonth.substring(0, 4)+"01";
				params.put("mx_startMonth", mx_startMonth);
				pager.setParams(params);
				list =llzshgxservice.getCityDetailPagerList(pager);
				pager.setDataRows(list);
			}else{
				pager.setDataRows(list);
				return pager;
			}
		}
		return pager;
		
	}
	
	
	@ResponseBody
    @RequestMapping(value = "/exportMxDetailList")
    public void exportMxDetailList(HttpServletRequest request, HttpServletResponse response){
		try{
			Map<String, Object> params = this.getParameterMap(request);
			//获取该省市数据库中最大最小审计月
			Map<String, Object> resultTimeMap = llzshgxservice.getCityDetailMaxAndMinAud_trm(params);
			if(resultTimeMap==null){
				List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			    map.put("1", "审计月");
				map.put("2", "省代码");
				map.put("3", "省名称");
				map.put("4", "地市代码");
				map.put("5", "地市名称");
				map.put("6", "用户标识");
				map.put("7", "统计周期");
				map.put("8", "本月赠送流量数合计（g）");
				map.put("9", "本月赠送流量实际使用量（g）");
				String fileName = "3.4_流量产品管理合规性_流量赠送合规性_明细.csv";
				CSVUtils.exportCSVList(fileName, exportData, map, request, response);
			}else{
				
				if(resultTimeMap.get("min_aud_trm").equals(null)||resultTimeMap.get("min_aud_trm").equals("")){
					params.put("mx_startMonth", null);
				}else{
					params.put("mx_startMonth", resultTimeMap.get("min_aud_trm"));
				}
				
				if(resultTimeMap.get("max_aud_trm").equals(null)||resultTimeMap.get("max_aud_trm").equals("")){
					params.put("mx_endMonth",null);
				}else{
					params.put("mx_endMonth", resultTimeMap.get("max_aud_trm"));
				}
				
				this.logger.info("LlzshgxController:exportLlyxzDetail时间参数为: mx_startMonth=["+params.get("mx_startMonth")+"], and mx_endMonth is [" +params.get("mx_endMonth") +"]");
				if(!params.get("mx_endMonth").equals(null)){
					String mx_startMonth = params.get("mx_endMonth").toString();
					mx_startMonth = mx_startMonth.substring(0, 4)+"01";
					params.put("mx_startMonth", mx_startMonth);
					llzshgxservice.exportMxDetailList(request, response,params);
				}else{
					List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
					LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("1", "审计月");
					map.put("2", "省代码");
					map.put("3", "省名称");
					map.put("4", "地市代码");
					map.put("5", "地市名称");
					map.put("6", "用户标识");
					map.put("7", "统计周期");
					map.put("8", "本月赠送流量数合计（g）");
					map.put("9", "本月赠送流量实际使用量（g）");
					String fileName = "3.4_流量产品管理合规性_流量赠送合规性_明细.csv";
					CSVUtils.exportCSVList(fileName, exportData, map, request, response);
				}
			}
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
