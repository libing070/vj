/**
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
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

import com.hpe.cmwa.auditTask.service.jz.HYZDBTHGX1702Service;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.CSVUtils;

/**
 * 3.2.17.3重复办理合约
 * <pre>
 * Desc： 
 * @author   wangpeng
 * @refactor wangpeng
 * @date     2017-2-4 上午10:48:13
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-2-4 	   wangpeng 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/hyzdbthgx_jy/1702")
public class HYZDBTHGX1702JYController extends BaseController {

    
    private DecimalFormat df = new DecimalFormat("######0.00");
    @Autowired
    private HYZDBTHGX1702Service huzdbthgx1702Service;
    
    @RequestMapping(value = "/index")
    public String index() {
	
	return "auditTask/jy/hyzdcfxs/hyzdbthgx1702";
    }
    /**
     * 重复办理统计
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-2-6 下午2:28:18
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getSumRepeat")
    public Map<String, Object> getSumRepeat(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	Map<String, Object> returnMap = new HashMap<String, Object>();
	List<Map<String, Object>> list = huzdbthgx1702Service.getSumRepeat(parameterMap);
	
	List<Map<String, Object>> top3list = huzdbthgx1702Service.getRepeatTop3City(parameterMap);
	
	Map<String, Object> sumMap = huzdbthgx1702Service.getSumImeiNum(parameterMap);
	
	returnMap.put("dataList", list);
	returnMap.put("top3list", top3list);
	returnMap.put("sumMap", sumMap);
	return returnMap;
    }
    /**
     * 获取饼图数据
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-2-7 上午9:56:14
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getSumPieCharts")
    public Map<String, Object> getSumPieCharts(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	Map<String, Object> map = huzdbthgx1702Service.getSumPieCharts(parameterMap);
	Map<String, Object> conMap = huzdbthgx1702Service.getSumPieCon(parameterMap);
	Map<String, Object> returnMap = new HashMap<String, Object>();
	returnMap.put("dataMap", map);
	returnMap.put("conMap", conMap);
	return returnMap;
    }
    /**
     * 获取数据表数据
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-2-6 下午3:57:23
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getSumRepeatTable")
    public Pager getSumRepeatTable(HttpServletRequest request,Pager pager) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	pager.setParams(parameterMap);
	List<Map<String, Object>> list = huzdbthgx1702Service.getSumRepeatTable(pager);
	pager.setDataRows(list);
	return pager;
    }
    /**
     * 明细表查询 分页
     * <pre>
     * Desc  
     * @param request
     * @param pager
     * @return
     * @author wangpeng
     * 2017-2-7 下午2:06:38
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getDetailListPager")
    public Pager getDetailListPager(HttpServletRequest request,Pager pager) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	pager.setParams(parameterMap);
	List<Map<String, Object>> list = huzdbthgx1702Service.getDetailListPager(pager);
	pager.setDataRows(list);
	return pager;
    }
    
    /**
     * 导出数据表
     * <pre>
     * Desc  
     * @param request
     * @param response
     * @throws Exception
     * @author wangpeng
     * 2017-2-7 上午9:54:36
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/exprotSumTable")
    public void exprotSumTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
	try {
	    Map<String, Object> parameterMap = this.getParameterMap(request);
	    List<Map<String, Object>> list = this.huzdbthgx1702Service.getSumRepeatTableAll(parameterMap);
	    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	    if (list != null && list.size() > 0) {
		LinkedHashMap<String, Object> lhm = null;
		for (int i = 0; i < list.size(); i++) {
		    Map<String, Object> eachMap = list.get(i);
		    lhm = new LinkedHashMap<String, Object>();
		    lhm.put("1", eachMap.get("CMCC_PRVD_NM_SHORT"));
		    lhm.put("2", eachMap.get("flag"));
		    lhm.put("3", eachMap.get("usr_num"));
		    exportData.add(lhm);
		}
	    }
	    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    map.put("1", "地市名称");
	    map.put("2", "同一用户重复办理次数");
	    map.put("3", "用户数");
	    
	    String fileName="4.4.2_合约终端补贴合规性_重复办理终端类合约计划_重复办理统计及明细_数据表.csv";
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
     * 2017-2-7 下午2:48:57
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/exportDetailTable")
    public void exportDetailTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Map<String, Object> parameterMap = this.getParameterMap(request);
    	huzdbthgx1702Service.getDetailList(request, response,parameterMap);
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
