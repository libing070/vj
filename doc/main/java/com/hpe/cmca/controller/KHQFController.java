/**
 * com.hpe.cmca.controller.KHQFController.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.controller;

import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.job.ZgwzGenFileProcessor;
import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.service.KhqfService;
import com.hpe.cmca.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Desc：
 * &#64;author   hufei
 * &#64;refactor hufei
 * &#64;date     2017-7-3 下午2:34:30
 * &#64;version  1.0
 *
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  2017-7-3 	   hufei 	         1. Created this class.
 * </pre>
 */
@Controller
@RequestMapping("/khqf")
public class KHQFController extends BaseController {

    @Autowired
    protected KhqfService khqfService;

    @Autowired
    protected ZgwzGenFileProcessor zgwzGenFileProcessor;

    /**
     * <pre>
     * Desc 获取金额柱状图所需的数据
     * &#64;param prvdId 省份Id
     * &#64;param audTrm 审计月
     * &#64;param concern 关注点：4001代表集团，4003代表个人
     * &#64;return JSon字符串
     * &#64;author hufei
     * 2017-7-4 下午2:01:34
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfsjjg })
    @ResponseBody
    @RequestMapping(value = "/getAmountColumnData", produces = "text/json;charset=UTF-8")
    public String getAmountColumnData(ParameterData khqfPrvdData) {
        Map<String, Object> result = new HashMap<String, Object>();
        result = khqfService.getAmountColumnData(khqfPrvdData);
        return Json.Encode(result);
    }

    /**
     * 集团/个人欠费数量排名（柱形图）
     *
     * <pre>
     * Desc
     * &#64;param khqf
     * &#64;return
     * &#64;author issuser
     * 2017-7-3 下午6:32:05
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfsjjg })
    @ResponseBody
    @RequestMapping(value = "getJTNumberPm", produces = "text/json; charset=UTF-8")
    public String getJTNumberPm(ParameterData khqf) {
        Map<String, Object> data = new HashMap<String, Object>();
        data = khqfService.getJTNumberPm(khqf);
        return Json.Encode(data);
    }

    /**
     * 集团/个人欠费数量排名（折线图）
     *
     * <pre>
     * Desc
     * &#64;param khqf
     * &#64;return
     * &#64;author issuser
     * 2017-7-4 下午2:35:37
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfsjjg })
    @ResponseBody
    @RequestMapping(value = "getJTNumberPmZheXian", produces = "text/json; charset=UTF-8")
    public String getJTNumberPmZheXian(HttpServletRequest request, ParameterData khqf) {
        khqf.setSwitchState(getSwitchStateByDepId(request));
        Map<String, Object> data = new HashMap<String, Object>();
        data = khqfService.getJTNumberPmZheXian(khqf);
        return Json.Encode(data);
    }

    /**
     * 统计分析 ->统计报表->排名汇总
     *
     * <pre>
     * Desc
     * &#64;param khqf
     * &#64;return
     * &#64;author issuser
     * 2017-7-4 下午2:35:37
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfpmhz })
    @ResponseBody
    @RequestMapping(value = "getJTNumberDataPaiming", produces = "text/json; charset=UTF-8")
    public String getJTNumberDataPaiming(ParameterData khqf) {
        if(khqf.getAudTrm()==null||"".equals(khqf.getAudTrm())){
            Map<Object,Object> map = new HashMap<>();
            return Json.Encode(map);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data = khqfService.getJTNumberDataPaiming(khqf);
        return Json.Encode(data);
    }

    /**
     * 集团客户欠费新增原有分布占比趋势
     *
     * <pre>
     * Desc
     * &#64;param khqf
     * &#64;return
     * &#64;author issuser
     * 2017-7-6 下午4:46:23
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfqtcd })
    @ResponseBody
    @RequestMapping(value = "getJTNumberDataQffenbu", produces = "text/json; charset=UTF-8")
    public String getJTNumberDataQffenbu(HttpServletRequest request, ParameterData khqf) {
        khqf.setSwitchState(getSwitchStateByDepId(request));
        Map<String, Object> data = new HashMap<String, Object>();
        data = khqfService.getJTNumberDataQffenbu(khqf);
        return Json.Encode(data);
    }

    /**
     * 统计分析 ->统计报表->个人客户欠费账龄
     *
     * <pre>
     * Desc
     * &#64;param khqf
     * &#64;return
     * &#64;author issuser
     * 2017-7-4 下午2:35:37
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfqtcd })
    @ResponseBody
    @RequestMapping(value = "getGRNumberDataQfAge", produces = "text/json; charset=UTF-8")
    public String getGRNumberDataQfAge(ParameterData khqf) {
        if(khqf.getAudTrm()==null||"".equals(khqf.getAudTrm())){
            Map<Object,Object> map = new HashMap<>();
            return Json.Encode(map);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data = khqfService.getGRNumberDataQfAge(khqf);
        return Json.Encode(data);
    }

    /**
     * 统计分析 ->统计报表->个人客户欠费金额
     *
     * <pre>
     * Desc
     * &#64;param khqf
     * &#64;return
     * &#64;author issuser
     * 2017-7-4 下午2:35:37
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfqtcd })
    @ResponseBody
    @RequestMapping(value = "getGRNumberDataQfAmt", produces = "text/json; charset=UTF-8")
    public String getGRNumberDataQfAmt(ParameterData khqf) {
        if(khqf.getAudTrm()==null||"".equals(khqf.getAudTrm())){
            Map<Object,Object> map = new HashMap<>();
            return Json.Encode(map);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data = khqfService.getGRNumberDataQfAmt(khqf);
        return Json.Encode(data);
    }

    /**
     *
     * <pre>
     * Desc  获取金额排名所用的数据（折线图）
     * &#64;param khqfParameter
     * &#64;return
     * &#64;author hufei
     * 2017-7-5 下午5:05:33
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfsjjg })
    @ResponseBody
    @RequestMapping(value = "/getAmountLineData", produces = "text/json;charset=UTF-8")
    public String getAmountLineData(HttpServletRequest request, ParameterData khqfParameter) {
        khqfParameter.setSwitchState(getSwitchStateByDepId(request));
        Map<String, Object> result = new HashMap<String, Object>();
        result = khqfService.getAmountLineData(khqfParameter);
        return Json.Encode(result);
    }

    /**
     *
     * <pre>
     * Desc  获取增量分析图表所用数据
     * &#64;param khqfParameter
     * &#64;return
     * &#64;author hufei
     * 2017-7-5 下午5:04:59
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfqtcd })
    @ResponseBody
    @RequestMapping(value = "/getIncrementalData", produces = "text/json;charset=UTF-8")
    public String getIncrementalData(ParameterData khqfParameter) {
        if(khqfParameter.getAudTrm()==null||"".equals(khqfParameter.getAudTrm())){
            Map<Object,Object> map = new HashMap<>();
            return Json.Encode(map);
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        result = khqfService.getIncrementalData(khqfParameter);
        return Json.Encode(result);
    }

    /**
     *
     * <pre>
     * Desc  获取统计分析->统计报表->新增原有欠费->欠费金额分布(饼图)-省份/全国
     * &#64;param khqfParameter
     * &#64;return
     * &#64;author hufei
     * 2017-7-6 下午5:02:29
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfqtcd })
    @ResponseBody
    @RequestMapping(value = "/getAmountPie", produces = "text/json;charset=UTF-8")
    public String getAmountPie(ParameterData khqfParameter) {
        if(khqfParameter.getAudTrm()==null||"".equals(khqfParameter.getAudTrm())){
            Map<Object,Object> map = new HashMap<>();
            return Json.Encode(map);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result = khqfService.getAmountPie(khqfParameter);
        return Json.Encode(result);
    }

    /**
     *
     * <pre>
     * Desc  数据获取：统计分析->集团客户欠费账龄分布
     * &#64;param khqfParameter
     * &#64;return
     * &#64;author hufei
     * 2017-7-5 下午5:25:43
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfqtcd })
    @ResponseBody
    @RequestMapping(value = "/getOrgOweAging", produces = "text/json;charset=UTF-8")
    public String getOrgOweAging(ParameterData khqfParameter) {
        if(khqfParameter.getAudTrm()==null||"".equals(khqfParameter.getAudTrm())){
            Map<Object,Object> map = new HashMap<>();
            return Json.Encode(map);
        }
        Map<String, Object> result_list = khqfService.getOrgOweAging(khqfParameter);
        return Json.Encode(result_list);
    }

    /**
     *
     * <pre>
     * Desc  数据获取：统计分析->集团客户欠费金额分布
     * &#64;param khqfParameter
     * &#64;return
     * &#64;author hufei
     * 2017-7-5 下午5:42:49
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfqtcd })
    @ResponseBody
    @RequestMapping(value = "/getOrgAmount", produces = "text/json;charset=UTF-8")
    public String getOrgAmount(ParameterData khqfParameter) {
        if(khqfParameter.getAudTrm()==null||"".equals(khqfParameter.getAudTrm())){
            Map<Object,Object> map = new HashMap<>();
            return Json.Encode(map);
        }
        Map<String, Object> result_list = khqfService.getOrgAmount(khqfParameter);
        return Json.Encode(result_list);
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.khqfsjjg })
    @ResponseBody
    @RequestMapping(value = "/getMapData", produces = "text/json;charset=UTF-8")
    public String getMapData(ParameterData khqfParameter) {
        if (khqfParameter.getAudTrm() == null || "".equals(khqfParameter.getAudTrm())) {
            Map<Object, Object> map = new HashMap<>();
            return Json.Encode(map);
        }
        return Json.Encode(khqfService.getMapData(khqfParameter));
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.khqfsjjg })
    @ResponseBody
    @RequestMapping(value = "/getMapBottomData", produces = "text/json;charset=UTF-8")
    public String getMapBottomData(ParameterData khqfParameter) {
        if (khqfParameter.getAudTrm() == null || "".equals(khqfParameter.getAudTrm())) {
            Map<Object, Object> map = new HashMap<>();
            return Json.Encode(map);
        }
        return Json.Encode(khqfService.getMapBottomData(khqfParameter));
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.khqfwjxz })
    @ResponseBody
    @RequestMapping(value = "/getSjbgData", produces = "text/json;charset=UTF-8")
    public String getSjbgData(HttpServletRequest request, ParameterData khqfParameter) {
        if (getAttributeByAudTrmAndUser(request, khqfParameter.getAudTrm(), "4")) {
            return Json.Encode(khqfService.getSjbgData(khqfParameter));
        } else {
            Map<String, Object> map = new HashMap<String, Object>(1);
            map.put("switchState", "audTrmColseForReport");
            return Json.Encode(map);
        }
        // return Json.Encode(khqfService.getSjbgData(khqfParameter));
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.khqfsjjg })
    @ResponseBody
    @RequestMapping(value = "/getSjbgCtyDetData", produces = "text/json;charset=UTF-8")
    public String getSjbgCtyDetData(ParameterData khqfParameter) {

        return Json.Encode(khqfService.getSjbgCtyDetData(khqfParameter));
    }

    // 数据获取：统计分析->集团客户欠费回收分布
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfqtcd })
    @ResponseBody
    @RequestMapping(value = "/getJTQFHSData", produces = "text/json;charset=UTF-8")
    public String getJTQFHSData(ParameterData khqfParameter) {
        Map<String, Object> result_list = khqfService.getJTQFHSData(khqfParameter);
        return Json.Encode(result_list);
    }

    // 数据获取：统计分析->个人客户欠费回收分布
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfqtcd })
    @ResponseBody
    @RequestMapping(value = "/getGRQFHSData", produces = "text/json;charset=UTF-8")
    public String getGRQFHSData(ParameterData khqfParameter) {
        Map<String, Object> result_list = khqfService.getGRQFHSData(khqfParameter);
        return Json.Encode(result_list);
    }

    // 客户欠费-统计分析-整改问责统计-近六个月整改-柱状图-add by hufei 2017.8.28
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfzgwz })
    @ResponseBody
    @RequestMapping(value = "/getRectifiyForSixColumn", produces = "text/json;charset=UTF-8")
    public String getRectifyForSixColumn(ParameterData parameterData) {
        return Json.Encode(khqfService.getRectifyForSixColumn(parameterData));
    }

    // 客户欠费-统计分析-整改问责统计-累计达到整改次数-柱状图-add by hufei 2017.8.28
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfzgwz })
    @ResponseBody
    @RequestMapping(value = "/getRectifiyColumn", produces = "text/json;charset=UTF-8")
    public String getRectifyColumn(ParameterData parameterData) {
        return Json.Encode(khqfService.getRectifyColumn(parameterData));
    }

    // 客户欠费-统计分析-整改问责统计-累计达到整改次数-折线图-add by hufei 2017.8.28
    @CmcaAuthority(authorityTypes = { AuthorityType.khqfzgwz })
    @ResponseBody
    @RequestMapping(value = "/getRectifiyLine", produces = "text/json;charset=UTF-8")
    public String getRectifyLine(HttpServletRequest request, ParameterData parameterData) {
        parameterData.setSwitchState(getSwitchStateByDepId(request));
        return Json.Encode(khqfService.getRectifyLine(parameterData));
    }

    @ResponseBody
    @RequestMapping(value = "/getKHQFgwzData", produces = "text/json;charset=UTF-8")
    public String getKHQFgwzData(ParameterData pdata) throws Exception {
        if(pdata.getAudTrm()==null||"".equals(pdata.getAudTrm())){
            List<Object> list = new ArrayList<>();
            return Json.Encode(list);
        }
        return Json.Encode(zgwzGenFileProcessor.generateQdyk(pdata.getAudTrm(), pdata.getPrvdId(), pdata.getConcern()));
    }

}
