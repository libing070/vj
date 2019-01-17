/**
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.controller.yjk;

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

import com.hpe.cmwa.auditTask.service.jz.YJK2001Service;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.CSVUtils;

/**
 * <pre>
 * 有价卡合规性 - 批量激活分析
 * 访问路径：http://localhost:7001/cmwa/yjk/2001/index
 * @author   Huang Tao
 * @date     Nov 22, 2016 13:48:35 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Nov 22, 2016 	   Huang Tao 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/yjk/2001/")
public class YJK2001Controller extends BaseController {

    @Autowired
    private YJK2001Service yjk2001Service;
    
    private DecimalFormat df = new DecimalFormat("######0.00");

    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request,HttpServletResponse response) {
    	String url = "";
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		if(request.getParameter("provinceCode").equals("10000")){
			url = "auditTask/sjk/yjkhgx/yjk200101";
		}else{
			url = "yjk/2001/index";
		}
		return url;
	}

//    @ResponseBody
//    @RequestMapping(value = "/getRankTrendList")
//    public List<Map<String, Object>> getRankTrendList(HttpServletRequest request) {
//        Map<String, Object> parameterMap = this.getParameterMap(request);
//        return yjk2001Service.getRankTrendList(parameterMap);
//    }
    /**
     * 获取有价卡数量波动趋势图
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-5 下午2:51:42
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getYJKNumberTrend")
    public  Map<String, Object> getYJKNumberTrend(HttpServletRequest request) {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        List<Map<String, Object>> list = yjk2001Service.getYJKNumberTrend(parameterMap);
        Map<String, Object> avgMap = yjk2001Service.getYJKAVGNumber(parameterMap);
        Map<String, Object> maxMap = yjk2001Service.getYJKMAXNumber(parameterMap);
        Double highAvg = 0.00;
        if(maxMap!=null){
        	
        	Double maxNumber = maxMap.get("max_yjk_cnt")==null?0.00:Double.parseDouble(maxMap.get("max_yjk_cnt").toString());
        	Double avgNumber = avgMap.get("avg_yjk_cnt")==null?0.00:Double.parseDouble(avgMap.get("avg_yjk_cnt").toString());
        	highAvg = (maxNumber-avgNumber)/avgNumber;
        	highAvg = highAvg*100;
        	maxMap.put("highAvg", df.format(highAvg)+"%");
        }
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("data", list);
        returnMap.put("avgMap", avgMap);
        returnMap.put("maxMap", maxMap);
        
        return returnMap;
    }
    /**
     * 获取有价卡激活数量前十地市
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-6 下午1:55:20
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getYJKTop10CityNumber")
    public Map<String, Object> getYJKTop10CityNumber(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<Map<String, Object>> list = yjk2001Service.getYJKTop10CityNumber(parameterMap);
	Map<String, Object> returnMap = new HashMap<String, Object>();
	returnMap.put("data", list);
	return returnMap;
    }
    
    /**
     * 获取有价卡激活数量占比前十地市
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-6 下午1:55:20
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getYJKTop10CityPer")
    public Map<String, Object> getYJKTop10CityPer(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<Map<String, Object>> list = yjk2001Service.getYJKTop10CityPer(parameterMap);
	Map<String, Object> avgMap = yjk2001Service.getYJKTop10CityAvgPer(parameterMap);
	Map<String, Object> returnMap = new HashMap<String, Object>();
	returnMap.put("data", list);
	returnMap.put("avgMap", avgMap);
	return returnMap;
    }
    
    /**
     * 获取有价卡操作员前十明细
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-6 下午1:55:20
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getYJKCZYTop10Detail")
    public Map<String, Object> getYJKCZYTop10Detail(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<Map<String, Object>> list = yjk2001Service.getYJKCZYTop10Detail(parameterMap);
	Map<String, Object> returnMap = new HashMap<String, Object>();
	returnMap.put("data", list);
	return returnMap;
    }
    /**
     * 获取有价卡操作员前十明细
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-6 下午1:55:20
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getYJKCZYDetail")
    public Pager getYJKCZYDetail(HttpServletResponse response, HttpServletRequest request, Pager pager) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	pager.setParams(parameterMap);
	List<Map<String, Object>> list = yjk2001Service.getYJKCZYDetail(pager);
	pager.setDataRows(list);
	return pager;
    }
    
    @ResponseBody
    @RequestMapping(value = "/getYJKCZY_tableDetail")
    public Pager getYJKCZY_tableDetail(HttpServletResponse response, HttpServletRequest request, Pager pager) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	pager.setParams(parameterMap);
	List<Map<String, Object>> list = yjk2001Service.getYJKCZY_tableDetail(pager);
	pager.setDataRows(list);
	return pager;
    }
    
    /**
     * 导出有价卡操作员汇总信息
     * <pre>
     * Desc  
     * @param request
     * @param response
     * @throws Exception
     * @author wangpeng
     * 2017-1-9 下午4:32:41
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/exportYJKCZYDetail")
    public void exportYJKCZYDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
	try {
	    Map<String, Object> parameterMap = this.getParameterMap(request);
	    List<Map<String, Object>> list = yjk2001Service.exportYJKCZYDetail(parameterMap);
	    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	    if (list != null && list.size() > 0) {
		LinkedHashMap<String, Object> lhm = null;
		for (int i = 0; i < list.size(); i++) {
		    Map<String, Object> eachMap = list.get(i);
		    lhm = new LinkedHashMap<String, Object>();
		    lhm.put("1", parameterMap.get("hz_startMonth")+"-"+parameterMap.get("hz_endMonth"));
		    lhm.put("2", eachMap.get("cmcc_prvd_nm_short"));
		    lhm.put("3", eachMap.get("opr_id"));
		    lhm.put("4", eachMap.get("nm"));
		    lhm.put("5", eachMap.get("opr_dt"));
		    lhm.put("6", eachMap.get("opr_tm"));
		    lhm.put("7", eachMap.get("yjk_cnt"));
		    lhm.put("8", eachMap.get("amt_sum"));
		    lhm.put("9", eachMap.get("zs_yjk_cnt"));
		    lhm.put("10", eachMap.get("dzkh_zs_num"));
		    exportData.add(lhm);
		}
	    }
	    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    map.put("1", "审计月");
	    map.put("2", "地市");
	    map.put("3", "操作员工号");
	    map.put("4", "操作员姓名");
	    map.put("5", "操作日期");
	    map.put("6", "操作时间");
	    map.put("7", "批量激活有价卡数量");
	    map.put("8", "批量激活有价卡金额(元)");
	    map.put("9", "赠送有价卡数量");
	    map.put("10", "营销活动向非中高端、非集团客户赠送有价卡");
	    String fileName="";
	    if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("single")) {
		fileName = "4.1.4_有价卡合规性_批量激活分析_操作员汇总_单时.csv";
	    } else {
		fileName = "4.1.4_有价卡合规性_批量激活分析_操作员汇总_单秒.csv";
	    }
	    CSVUtils.exportCSVList(fileName, exportData, map, request, response);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    /**
     * 导出有价卡操集中赠送汇总
     * <pre>
     * Desc  
     * @param request
     * @param response
     * @throws Exception
     * @author wangpeng
     * 2017-1-9 下午4:32:41
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/exportJzzs")
    public void exportJzzs(HttpServletRequest request, HttpServletResponse response) throws Exception {
	try {
	    Map<String, Object> parameterMap = this.getParameterMap(request);
	    List<Map<String, Object>> list = yjk2001Service.exportYJKCZYDetail(parameterMap);
	    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	    if (list != null && list.size() > 0) {
		LinkedHashMap<String, Object> lhm = null;
		for (int i = 0; i < list.size(); i++) {
		    Map<String, Object> eachMap = list.get(i);
		    lhm = new LinkedHashMap<String, Object>();
		    lhm.put("1", parameterMap.get("hz_startMonth")+"-"+parameterMap.get("hz_endMonth"));
		    lhm.put("2", eachMap.get("cmcc_prvd_nm_short"));
		    lhm.put("3", eachMap.get("opr_id"));
		    lhm.put("4", eachMap.get("nm"));
		    lhm.put("5", eachMap.get("opr_dt"));
		    lhm.put("6", eachMap.get("opr_tm"));
		    lhm.put("7", eachMap.get("yjk_cnt"));
		    lhm.put("8", eachMap.get("amt_sum"));
		    lhm.put("9", eachMap.get("pljh_zs_num"));
		    lhm.put("10", eachMap.get("jzzs_cz_num"));
		    exportData.add(lhm);
		}
	    }
	    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    map.put("1", "审计月");
	    map.put("2", "地市");
	    map.put("3", "操作员工号");
	    map.put("4", "操作员姓名");
	    map.put("5", "操作日期");
	    map.put("6", "操作时间");
	    map.put("7", "批量激活有价卡数量");
	    map.put("8", "批量激活有价卡金额(元)");
	    map.put("9", "批量激活的有价卡涉及集中赠送的有价卡数量");
	    map.put("10", "集中赠送的有价卡涉及集中充值的有价卡数量");
	    String fileName="";
	    if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("single")) {
		fileName = "4.1.4_有价卡合规性_批量激活分析_操作员批量激活有价卡及集中赠送激活统计_单时.csv";
	    } else {
		fileName = "4.1.4_有价卡合规性_批量激活分析_操作员批量激活有价卡及集中赠送激活统计_单秒.csv";
	    }
	    CSVUtils.exportCSVList(fileName, exportData, map, request, response);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    @ResponseBody
    @RequestMapping(value = "/getProvSumList")
    public List<Map<String, Object>> getProvSumList(HttpServletRequest request) {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        return yjk2001Service.getProvSumList(parameterMap);
    }

    @ResponseBody
    @RequestMapping(value = "/getCitySumPagerList")
    public Pager getCitySumPagerList(HttpServletRequest request, Pager pager) {
        pager.setParams(this.getParameterMap(request));

        List<Map<String, Object>> dataRecords = yjk2001Service.getCitySumPagerList(pager);
        pager.setDataRows(dataRecords);
        return pager;
    }

    @ResponseBody
    @RequestMapping(value = "/getCityDetailPagerList")
    public Pager getCityDetailPagerList(HttpServletRequest request, Pager pager) {
        pager.setParams(this.getParameterMap(request));
        
        List<Map<String, Object>> dataRecords = yjk2001Service.getCityDetailPagerList(pager);
        pager.setDataRows(dataRecords);
        return pager;
    }
    
    @ResponseBody
    @RequestMapping(value = "/exportSumList")
    public void exportSumList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        yjk2001Service.exportSumList(request, response, parameterMap);
    }

    @ResponseBody
    @RequestMapping(value = "/exportDetailList")
    public void exportDetailList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        yjk2001Service.exportDetailList(request, response, parameterMap);
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
