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

import com.hpe.cmwa.auditTask.service.jz.HYZDBTHGX1701Service;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.CSVUtils;

/**
 * 3.2.17.2合约内离网
 * <pre>
 * Desc： 
 * @author   wangpeng
 * @refactor wangpeng
 * @date     2017-1-22 下午7:10:59
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-1-22 	   wangpeng 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/hyzdbthgx/1701")
public class HYZDBTHGX1701Controller extends BaseController {

    
    private DecimalFormat df = new DecimalFormat("######0.00");
    @Autowired
    private HYZDBTHGX1701Service huzdbthgx1701Service;
    
    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request) {
    	if(request.getParameter("provinceCode").equals("10000")){
    		return "auditTask/sjk/hyzdbthgx_qg/hynlw_qg";
    	}else{
    		return "auditTask/jz/hyzdbthgx/hyzdbthgx1701";
    	}
	
    }
    /**
     * 营销案离网率情况
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-23 上午10:21:07
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getQdhyPrvd")
    public List<Map<String, Object>> getQdhyPrvd(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<Map<String, Object>> list = huzdbthgx1701Service.getQdhyPrvd(parameterMap);
	return list;
    }
    /**
     * 合约内离网用户的在网时长分
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-2-3 上午9:59:53
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getOnlineMon")
    public List<Map<String, Object>> getOnlineMon(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<Map<String, Object>> list = huzdbthgx1701Service.getOnlineMon(parameterMap);
	return list;
    }
    
    /**
     * 获取营销案汇总信息
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-23 下午2:42:34
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getSumOffer")
    public Pager getSumOffer(HttpServletRequest request,Pager pager) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	pager.setParams(parameterMap);
	List<Map<String, Object>> list = huzdbthgx1701Service.getSumOffer(pager);
	pager.setDataRows(list);
	return pager;
    }
    /**
     * 获取详情信息（分页）
     * <pre>
     * Desc  
     * @param request
     * @param pager
     * @return
     * @author wangpeng
     * 2017-2-3 下午2:20:23
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getDetailListPager")
    public Pager getDetailListPager(HttpServletRequest request,Pager pager) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	pager.setParams(parameterMap);
	List<Map<String, Object>> list = huzdbthgx1701Service.getDetailListPager(pager);
	pager.setDataRows(list);
	return pager;
    }
    
    /**
     * 导出数据
     * <pre>
     * Desc  
     * @param request
     * @param response
     * @throws Exception
     * @author wangpeng
     * 2017-1-23 下午3:51:47
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/exprotSumOfferTable")
    public void exprotSumOfferTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
	try {
	    Map<String, Object> parameterMap = this.getParameterMap(request);
	    List<Map<String, Object>> list = this.huzdbthgx1701Service.getSumOfferAll(parameterMap);
	    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	    if (list != null && list.size() > 0) {
		LinkedHashMap<String, Object> lhm = null;
		for (int i = 0; i < list.size(); i++) {
		    Map<String, Object> eachMap = list.get(i);
		    lhm = new LinkedHashMap<String, Object>();
		    lhm.put("1", eachMap.get("CMCC_PROV_PRVD_ID"));
		    lhm.put("2", eachMap.get("SHORT_NAME"));
		    lhm.put("3", eachMap.get("OFFER_ID"));
		    lhm.put("4", eachMap.get("OFFER_NM"));
		    lhm.put("5", parameterMap.get("hz_startMonth")+"-"+parameterMap.get("hz_endMonth"));
		    lhm.put("6", eachMap.get("SUM_SUBS_NUM"));
		    exportData.add(lhm);
		}
	    }
	    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    map.put("1", "省代码");
	    map.put("2", "省名称");
	    map.put("3", "营销案编码");
	    map.put("4", "营销案名称");
	    map.put("5", "销售月");
	    map.put("6", "合约内离网用户数");
	    String fileName="4.4.2_合约终端补贴合规性_合约内离网_营销案离网率情况_数据表.csv";
	    CSVUtils.exportCSVList(fileName, exportData, map, request, response);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    @ResponseBody
    @RequestMapping(value = "/exportDetailTable")
    public void exportDetailTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    Map<String, Object> parameterMap = this.getParameterMap(request);
	    huzdbthgx1701Service.getDetailListAll(request, response,parameterMap);
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
