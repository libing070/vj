/**
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.controller.yjk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.jz.YJK2002Service;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;

/**
 * <pre>
 * 有价卡合规性 - 充值异常分析
 * 访问路径：http://localhost:7001/cmwa/yjk/2002/index
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
@RequestMapping("/yjk/2002/")
public class YJK2002Controller extends BaseController {

    @Autowired
    private YJK2002Service yjk2002Service;

    @RequestMapping(value = "/index")
    public void index() {
    }

    @ResponseBody
    @RequestMapping(value = "/getRankTrendList")
    public List<Map<String, Object>> getRankTrendList(HttpServletRequest request) {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        return yjk2002Service.getRankTrendList(parameterMap);
    }

    @ResponseBody
    @RequestMapping(value = "/getCitySumList")
    public List<Map<String, Object>> getCitySumList(HttpServletRequest request) {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        return yjk2002Service.getCitySumList(parameterMap);
    }

    @ResponseBody
    @RequestMapping(value = "/getCitySumOrderByCntList")
    public List<Map<String, Object>> getCitySumOrderByCntList(HttpServletRequest request) {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        return yjk2002Service.getCitySumOrderByCntList(parameterMap);
    }

    @ResponseBody
    @RequestMapping(value = "/getProvSumList")
    public List<Map<String, Object>> getProvSumList(HttpServletRequest request) {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        return yjk2002Service.getProvSumList(parameterMap);
    }

    @ResponseBody
    @RequestMapping(value = "/getCitySumPagerList")
    public Pager getCitySumPagerList(HttpServletRequest request, Pager pager) {
        pager.setParams(this.getParameterMap(request));

        List<Map<String, Object>> dataRecords = yjk2002Service.getCitySumPagerList(pager);
        pager.setDataRows(dataRecords);
        return pager;
    }

    @ResponseBody
    @RequestMapping(value = "/getCityDetailPagerList")
    public Pager getCityDetailPagerList(HttpServletRequest request, Pager pager) {
        pager.setParams(this.getParameterMap(request));
        
        
        List<Map<String, Object>> dataRecords = yjk2002Service.getCityDetailPagerList(pager);
        pager.setDataRows(dataRecords);
        return pager;
    }
    
    @ResponseBody
    @RequestMapping(value = "/exportSumList")
    public void exportSumList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        yjk2002Service.exportSumList(request, response, parameterMap);
    }

    @ResponseBody
    @RequestMapping(value = "/exportDetailList")
    public void exportDetailList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> parameterMap = this.getParameterMap(request);
        yjk2002Service.exportDetailList(request, response, parameterMap);
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
