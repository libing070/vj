/**
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.controller.yjk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.jz.YJK2004Service;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;

/**
 * <pre>
 * 有价卡合规性 - 跨省使用分析
 * 访问路径：http://localhost:7001/cmwa/yjk/2004/index
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
@RequestMapping("/yjk/2004/")
public class YJK2004Controller extends BaseController {

    @Autowired
    private YJK2004Service yjk2004Service;

    @RequestMapping(value = "/index")
    public void index() {
    }
    @RequestMapping(value = "/kssy")
    public String kssy(HttpServletRequest request) {
    	if(request.getParameter("provinceCode").equals("10000")){
			return "auditTask/sjk/jy_yjkkssy_qg/yjk200401";
		}else{
			return "auditTask/jy/yjkkssy/index";
		}
    	
    }
    @ResponseBody
    @RequestMapping(value = "/getRankTrendList")
    public List<Object> getRankTrendList(HttpServletRequest request) {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        List <Object> resultList = new ArrayList<Object>();
        List<Map<String, Object>>list1 =  yjk2004Service.getRankTrendList(parameterMap);
        List<Map<String, Object>>list2 =  yjk2004Service.getConclusionList(parameterMap);
        resultList.add(list1);
        resultList.add(list2);
        return resultList;
    }

    @ResponseBody
    @RequestMapping(value = "/getRankProvSum")
    public List<Map<String, Object>> getRankProvSum(HttpServletRequest request) {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        return yjk2004Service.getRankProvSum(parameterMap);
    }

    @ResponseBody
    @RequestMapping(value = "/selectProvinceList")
    public List<Map<String, Object>> selectProvinceList(HttpServletRequest request) {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        return yjk2004Service.selectProvinceList(parameterMap);
    }

    @ResponseBody
    @RequestMapping(value = "/getProvSumList")
    public List<Map<String, Object>> getProvSumList(HttpServletRequest request) {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        return yjk2004Service.getProvSumList(parameterMap);
    }
    
    @ResponseBody
    @RequestMapping(value = "/getProvSumPagerList")
    public Pager getCitySumPagerList(HttpServletRequest request, Pager pager) {
        pager.setParams(this.getParameterMap(request));

        List<Map<String, Object>> dataRecords = yjk2004Service.getProvSumPagerList(pager);
        pager.setDataRows(dataRecords);
        return pager;
    }

    @ResponseBody
    @RequestMapping(value = "/getCityDetailPagerList")
    public Pager getCityDetailPagerList(HttpServletRequest request, Pager pager) {
        pager.setParams(this.getParameterMap(request));
        
        List<Map<String, Object>> dataRecords = yjk2004Service.getCityDetailPagerList(pager);
        pager.setDataRows(dataRecords);
        return pager;
    }
    
    @ResponseBody
    @RequestMapping(value = "/exportSumList")
    public void exportSumList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        yjk2004Service.exportSumList(request, response, parameterMap);
    }

    @ResponseBody
    @RequestMapping(value = "/exportDetailList")
    public void exportDetailList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        yjk2004Service.exportDetailList(request, response, parameterMap);
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
        
        // 地市汇总地图倍数
        defaultParams.put("hz_map_double", 5);
        
        return defaultParams;
    }

}
