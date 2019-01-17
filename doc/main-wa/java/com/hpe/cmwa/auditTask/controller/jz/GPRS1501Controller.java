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

import com.hpe.cmwa.auditTask.service.jz.GPRS1501Service;
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
@RequestMapping("/gprs/1501")
public class GPRS1501Controller extends BaseController {

    
    private DecimalFormat df = new DecimalFormat("######0.00");
    @Autowired
    private GPRS1501Service gprs1501Service;
    
    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request) {
    	if(request.getParameter("provinceCode").equals("10000")){
    		return "auditTask/sjk/byllcpbsx_qg/byllcpbsx_qg";
    	}else{
    		return "auditTask/jz/gprs/gprs1501";
    	}
    }
    /**
     * 获取省汇总信息
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-20 上午11:05:44
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getSumGprsPrvd")
    public List<Map<String, Object>> getSumGprsPrvd(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<Map<String, Object>> list = gprs1501Service.getSumGprsPrvd(parameterMap);
	return list;
    }
    /**
     * 
     * <pre>
     * Desc  
     * @param request
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-20 下午3:36:52
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getSumGprsPrvdPager")
    public Pager getSumGprsPrvdPager(HttpServletRequest request,Pager pager) {
 	Map<String, Object> parameterMap = this.getParameterMap(request);
 	pager.setParams(parameterMap);
 	List<Map<String, Object>> list = gprs1501Service.getSumGprsPrvdPager(pager);
 	pager.setDataRows(list);
 	return pager;
     }
    
    /**
     * 获取地市汇总
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-20 下午4:18:54
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getSumGprsCty")
    public List<Map<String, Object>> getSumGprsCty(HttpServletRequest request) {
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<Map<String, Object>> list = gprs1501Service.getSumGprsCty(parameterMap);
	return list;
    }
    /**
     * 分页查询地市汇总信息
     * <pre>
     * Desc  
     * @param request
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-20 下午4:41:37
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getSumGprsCtyPager")
    public Pager getSumGprsCtyPager(HttpServletRequest request,Pager pager) {
 	Map<String, Object> parameterMap = this.getParameterMap(request);
 	pager.setParams(parameterMap);
 	List<Map<String, Object>> list = gprs1501Service.getSumGprsCtyPager(pager);
 	pager.setDataRows(list);
 	return pager;
     }
    /**
     * 获得明细分页数据
     * <pre>
     * Desc  
     * @param request
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-20 下午5:19:11
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getGprsDetailPager")
    public Pager getGprsDetailPager(HttpServletRequest request,Pager pager) {
 	Map<String, Object> parameterMap = this.getParameterMap(request);
 	pager.setParams(parameterMap);
 	List<Map<String, Object>> list = gprs1501Service.getGprsDetailPager(pager);
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
	    List<Map<String, Object>> list = this.gprs1501Service.getSumGprsCty(parameterMap);
	    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	    if (list != null && list.size() > 0) {
		LinkedHashMap<String, Object> lhm = null;
		for (int i = 0; i < list.size(); i++) {
		    Map<String, Object> eachMap = list.get(i);
		    lhm = new LinkedHashMap<String, Object>();
		    lhm.put("1", parameterMap.get("hz_startMonth")+"-"+parameterMap.get("hz_endMonth"));
		    lhm.put("2", eachMap.get("CMCC_PRVD_NM_SHORT"));
		    lhm.put("3", eachMap.get("SUM_OVER_GPRS"));
		    Double per = eachMap.get("PER_OVER_GPRS")==null?0.00:Double.parseDouble(eachMap.get("PER_OVER_GPRS").toString());
		    lhm.put("4", df.format(per*100)+"%");
		    lhm.put("5", eachMap.get("SUM_TOL_GPRS"));
		    exportData.add(lhm);
		}
	    }
	   
	    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    map.put("1", "审计月");
	    map.put("2", "地市名称");
	    map.put("3", "超限流量（G）");
	    map.put("4", "超限流量占比");
	    map.put("5", "地市实际使用总流量（G）");
	    String fileName="4.3.2_包月流量产品不设限_用户月使用流量违规超过50G_超限流量地域分布_数据表.csv";
	    CSVUtils.exportCSVList(fileName, exportData, map, request, response);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    /**
     * 导出明细表
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
	    Map<String, Object> parameterMap = this.getParameterMap(request);
	    gprs1501Service.getGprsDetailAll(request, response,parameterMap);
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
