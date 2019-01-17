/**
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.controller.jz;

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

import com.hpe.cmwa.auditTask.service.jz.GPRS1502Service;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.CSVUtils;

/**
 * 3.2.15包月流量产品不设限
 * <pre>
 * Desc： 
 * @author   wangpeng
 * @refactor wangpeng
 * @date     2017-1-20 上午9:55:11
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-1-20 	   wangpeng 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/gprs/1502")
public class GPRS1502Controller extends BaseController {

    
    private DecimalFormat df = new DecimalFormat("######0.00");
    
    @Autowired
    private GPRS1502Service gprs1502Service;
    
    @RequestMapping(value = "/index")
    public String index() {
	
	return "auditTask/jz/gprs/gprs1502";
    }
    /**
     * 获取单价汇总信息
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-22 下午2:18:54
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getSumPrice")
    public List<Map<String, Object>> getSumPrice(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<Map<String, Object>> list = gprs1502Service.getSumPrice(parameterMap);
	return list;
    }
    /**
     * 获取省汇总信息
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-22 下午3:17:47
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getSumPrvd")
    public List<Map<String, Object>> getSumPrvd(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<Map<String, Object>> list = gprs1502Service.getSumPrvd(parameterMap);
	return list;
    }
    /**
     * 获取地市汇总信息
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-22 下午4:24:06
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getSumCty")
    public Map<String, Object> getSumCty(HttpServletRequest request) {
    	Map<String, Object> parameterMap = this.getParameterMap(request);
		List<Map<String, Object>> list = gprs1502Service.getSumCty(parameterMap);
		List<Map<String, Object>> sumGPRSList = gprs1502Service.getSumGPRS(parameterMap);
		List<Map<String, Object>> maxCity3 = gprs1502Service.maxCity3(parameterMap);
		List<Map<String, Object>> minCity3 = gprs1502Service.minCity3(parameterMap);
		int sumGPRS = 0;
		if(sumGPRSList.size()>0){
			sumGPRS = Integer.parseInt(sumGPRSList.get(0).get("sum_gprsG").toString());
		}else{
			sumGPRS = 0;
		}
		
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("data", list);
		returnMap.put("sumGPRS", sumGPRS);
		returnMap.put("maxCity3", maxCity3);
		returnMap.put("minCity3", minCity3);
		return returnMap;
    }
    /**
     * 获取地市汇总信息
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-22 下午4:24:06
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getSumCtyPager")
    public Pager getSumCtyPager(HttpServletRequest request,Pager pager) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	pager.setParams(parameterMap);
	List<Map<String, Object>> list = gprs1502Service.getSumCtyPager(pager);
	pager.setDataRows(list);
	return pager;
    }
    /**
     * 分页查询明细
     * <pre>
     * Desc  
     * @param request
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-22 下午5:39:36
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getDetailPager")
    public Pager getDetailPager(HttpServletRequest request,Pager pager) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	pager.setParams(parameterMap);
	List<Map<String, Object>> list = gprs1502Service.getDetailPager(pager);
	pager.setDataRows(list);
	return pager;
    }
    
    
    /**
     * 导出地市汇总数据
     * <pre>
     * Desc  
     * @param request
     * @param response
     * @throws Exception
     * @author wangpeng
     * 2017-1-20 下午5:05:02
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/exportCtyTable")
    public void exportCtyTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
	try {
	    Map<String, Object> parameterMap = this.getParameterMap(request);
	    List<Map<String, Object>> list = this.gprs1502Service.getSumCty(parameterMap);
	    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	    if (list != null && list.size() > 0) {
		LinkedHashMap<String, Object> lhm = null;
		for (int i = 0; i < list.size(); i++) {
		    Map<String, Object> eachMap = list.get(i);
		    lhm = new LinkedHashMap<String, Object>();
		    lhm.put("1", parameterMap.get("hz_startMonth")+"-"+parameterMap.get("hz_endMonth"));
		    lhm.put("2", eachMap.get("CMCC_PRVD_NM_SHORT"));
		    
		    lhm.put("3", eachMap.get("SUM_LOW_GPRS_TOL_G"));
		  
		    Double SUM_LOW_GPRS_FEE = eachMap.get("SUM_LOW_GPRS_FEE")==null?0.00:Double.parseDouble(eachMap.get("SUM_LOW_GPRS_FEE").toString());
		    lhm.put("4", df.format(SUM_LOW_GPRS_FEE));
		    
		    Double AVG_PER_GPRS_SUBS_M = eachMap.get("AVG_PER_GPRS_SUBS_M")==null?0.00:Double.parseDouble(eachMap.get("AVG_PER_GPRS_SUBS_M").toString());
		    lhm.put("5", df.format(AVG_PER_GPRS_SUBS_M));
		    
		    Double AVG_PER_GPRS_SUBS_G = eachMap.get("AVG_PER_GPRS_SUBS_G")==null?0.00:Double.parseDouble(eachMap.get("AVG_PER_GPRS_SUBS_G").toString());
		    lhm.put("6", df.format(AVG_PER_GPRS_SUBS_G));
		    
		    exportData.add(lhm);
		}
	    }
	   
	    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    map.put("1", "审计区间");
	    map.put("2", "地市名称");
	    map.put("3", "低价流量使用量（G）");
	    map.put("4", "低价流量收入（元）");
	    map.put("5", "低价流量平均单价（元/M）");
	    map.put("6", "低价流量平均单价（元/G）");
	    String fileName="4.3.2_流量产品管理合规性_超低价流量_低价流量使用情况地域分布_数据表.csv";
	    CSVUtils.exportCSVList(fileName, exportData, map, request, response);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    
    /**
     * 导出明细数据
     * <pre>
     * Desc  
     * @param request
     * @param response
     * @throws Exception
     * @author wangpeng
     * 2017-1-20 下午5:05:02
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/exportDetailTable")
    public void exportDetailTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
	try {
	    Map<String, Object> parameterMap = this.getParameterMap(request);
	    gprs1502Service.getDetailPagerAll(request, response,parameterMap);
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
