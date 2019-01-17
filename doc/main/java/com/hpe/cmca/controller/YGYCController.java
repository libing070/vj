/**
 * com.hpe.cmca.controller.KHQFController.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.pojo.YgycData;
import com.hpe.cmca.service.YgycService;
import com.hpe.cmca.util.Json;

/**
 * 员工异常
 */
@Controller
@RequestMapping("/ygyc")
public class YGYCController extends BaseController {

    @Autowired
    protected YgycService ygycService;

    /**
     * <pre>
     * Desc 异常赠送话费  员工数量排名（柱形图）
     * @param prvdId 省份Id
     * @param audTrm 审计月
     * @param concern 关注点：5001 异常赠送积分 5002 异常积分转移  5003异常赠送话费  5004 异常退费
     * @return JSon字符串
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycsjjg })
    @ResponseBody
    @RequestMapping(value = "/getYgycEmployeeQty", produces = "text/json;charset=UTF-8")
    public String getYgycEmployeeQty(ParameterData ygycdata) {
	Map<String, Object> result = new HashMap<String, Object>();
	result = ygycService.getYgycEmployeeQty(ygycdata);
	return Json.Encode(result);
    }

    /**
     * <pre>
     * Desc 异常赠送话费  赠送金额排名（柱形图）
     * @param prvdId 省份Id
     * @param audTrm 审计月
     * @param concern 关注点：5001 异常赠送积分 5002 异常积分转移  5003异常赠送话费  5004 异常退费
     * @return JSon字符串
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycsjjg })
    @ResponseBody
    @RequestMapping(value = "/getYgycPresentAmount", produces = "text/json;charset=UTF-8")
    public String getYgycPresentAmount(ParameterData ygycdata) {
	Map<String, Object> result = new HashMap<String, Object>();
	result = ygycService.getYgycPresentAmount(ygycdata);
	return Json.Encode(result);
    }

    /**
     * <pre>
     * Desc 异常赠送话费  员工数量趋势（折线图）
     * @param prvdId 省份Id
     * @param audTrm 审计月
     * @param concern 关注点：5001 异常赠送积分 5002 异常积分转移  5003异常赠送话费  5004 异常退费
     * @return JSon字符串
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycsjjg })
    @ResponseBody
    @RequestMapping(value = "/getYgycEmployeeQtyLine", produces = "text/json;charset=UTF-8")
    public String getYgycEmployeeQtyLine(HttpServletRequest request, ParameterData ygycdata) {
	ygycdata.setSwitchState(getSwitchStateByDepId(request));
	Map<String, Object> result = new HashMap<String, Object>();
	result = ygycService.getYgycEmployeeQtyLine(ygycdata);
	return Json.Encode(result);
    }

    /**
     * <pre>
     * Desc 异常赠送话费  赠送金额趋势（折线图）
     * @param prvdId 省份Id
     * @param audTrm 审计月
     * @param concern 关注点：5001 异常赠送积分 5002 异常积分转移  5003异常赠送话费  5004 异常退费
     * @return JSon字符串
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycsjjg })
    @ResponseBody
    @RequestMapping(value = "/getYgycPreAmountLine", produces = "text/json;charset=UTF-8")
    public String getYgycPreAmountLine(HttpServletRequest request, ParameterData ygycdata) {
	ygycdata.setSwitchState(getSwitchStateByDepId(request));
	Map<String, Object> result = new HashMap<String, Object>();
	result = ygycService.getYgycPreAmountLine(ygycdata);
	return Json.Encode(result);
    }

    /**
     * <pre>
     * Desc 风险地图
     * @param prvdId 省份Id
     * @param audTrm 审计月
     * @param concern 关注点：5001 异常赠送积分 5002 异常积分转移  5003异常赠送话费  5004 异常退费
     * @return JSon字符串
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycsjjg })
    @ResponseBody
    @RequestMapping(value = "/getYgycMapData", produces = "text/json;charset=UTF-8")
    public String getYgycMapData(ParameterData ygycdata) {
	Map<Integer, YgycData> result = new HashMap<Integer, YgycData>();
	result = ygycService.getYgycMapData(ygycdata);
	return Json.Encode(result);
    }

    /**
     * <pre>
     * Desc 风险地图下面卡片数据
     * @param prvdId 省份Id
     * @param audTrm 审计月
     * @param concern 关注点：5001 异常赠送积分 5002 异常积分转移  5003异常赠送话费  5004 异常退费
     * @return JSon字符串
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycsjjg })
    @ResponseBody
    @RequestMapping(value = "/getYgycMapBottomData", produces = "text/json;charset=UTF-8")
    public String getYgycMapBottomData(ParameterData ygycdata) {
	Map<Integer, YgycData> result = new HashMap<Integer, YgycData>();
	result = ygycService.getYgycMapBottomData(ygycdata);
	return Json.Encode(result);
    }

    /**
     * 地市下钻用户的员工信息表格
     * 
     * <pre>
     * Desc  
     * @param ygycdata
     * @return
     * @author issuser
     * 2017-8-3 下午4:29:29
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycsjjg })
    @ResponseBody
    @RequestMapping(value = "/getYgycJobnumber", produces = "text/json;charset=UTF-8")
    public String getYgycJobnumber(ParameterData ygycdata) {
	Map<String, Object> result = new HashMap<String, Object>();
	result = ygycService.getYgycJobnumber(ygycdata);
	return Json.Encode(result);
    }

    /**
     * <pre>
     * Desc  地市下钻用户的员工-手机号码-汇总信息表格
     * @param ygycdata
     * @return
     * @author hufei
     * 2017-8-3 下午5:52:12
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycsjjg })
    @ResponseBody
    @RequestMapping(value = "/getYgycPhoneTable", produces = "text/json;charset=UTF-8")
    public String getYgycPhoneTable(ParameterData ygycdata) {
	Map<String, Object> result = new HashMap<String, Object>();
	result = ygycService.getYgycPhoneTable(ygycdata);
	return Json.Encode(result);
    }

    /**
     * <pre>
     * Desc  手机号下钻操作员工赠送信息
     * @param ygycdata
     * @return
     * @author
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycsjjg })
    @ResponseBody
    @RequestMapping(value = "/getYgycOperatorByphone", produces = "text/json;charset=UTF-8")
    public String getYgycOperatorByphone(ParameterData ygycdata) {
	Map<String, Object> result = new HashMap<String, Object>();
	result = ygycService.getYgycOperatorByphone(ygycdata);
	return Json.Encode(result);
    }

    /**
     * <pre>
     * Desc  获取员工异常操作-统计分析-排名汇总
     * @param ygycdata
     * @return
     * @author
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycpmhz })
    @ResponseBody
    @RequestMapping(value = "/getRankTable", produces = "text/json;charset=UTF-8")
    public String getRankTable(ParameterData ygycdata) {
	Map<String, Object> result = new HashMap<String, Object>();
	result = ygycService.getRankTable(ygycdata);
	return Json.Encode(result);
    }

    /**
     * <pre>
     * Desc  获取员工异常操作-统计分析-排名汇总
     * @param ygycdata
     * @return
     * @author
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycqtcd })
    @ResponseBody
    @RequestMapping(value = "/getHistoryTable", produces = "text/json;charset=UTF-8")
    public String getHistoryTable(ParameterData ygycdata) {
	Map<String, Object> result = new HashMap<String, Object>();
	result = ygycService.getHistoryTable(ygycdata);
	return Json.Encode(result);
    }

    // 获取员工异常操作-统计分析-重点关注数据
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycqtcd })
    @ResponseBody
    @RequestMapping(value = "/getFocusUserTable", produces = "text/json;charset=UTF-8")
    public String getFocusUserTable(ParameterData ygycdata) {
	Map<String, Object> result = new HashMap<String, Object>();
	result = ygycService.getFocusUserTable(ygycdata);
	return Json.Encode(result);
    }

    // 获取员工异常操作-统计分析-重点关注数据-下钻
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycqtcd })
    @ResponseBody
    @RequestMapping(value = "/getFocusUserDetaile", produces = "text/json;charset=UTF-8")
    public String getFocusUserDetaile(ParameterData ygycdata) {
	Map<String, Object> result = new HashMap<String, Object>();
	result = ygycService.getFocusUserDetaile(ygycdata);
	return Json.Encode(result);
    }

    /**
     * <pre>
     * Desc  获取员工异常操作-统计分析-审计报告
     * @param ygycdata
     * @return
     * @author
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycwjxz })
    @ResponseBody
    @RequestMapping(value = "/getReportInfo", produces = "text/json;charset=UTF-8")
    public String getReportInfo(HttpServletRequest request,ParameterData ygycdata) {
	if(getAttributeByAudTrmAndUser(request,ygycdata.getAudTrm(),"5")){
	    return Json.Encode(ygycService.getReportInfo(ygycdata));    
	}else{
		Map<String,Object> map=new HashMap<String,Object>(1);
		map.put("switchState", "audTrmColseForReport");
	    return Json.Encode(map);
	}
    }

    /**
     * 养卡 重点关注员工号
     * 
     * <pre>
     * Desc  
     * @param ygycdata
     * @return
     * @author issuser
     * 2017-9-7 下午5:52:27
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycqtcd })
    @ResponseBody
    @RequestMapping(value = "/getYgycFocusEmployee", produces = "text/json;charset=UTF-8")
    public String getYgycFocusEmployee(ParameterData ygycdata) {
	Map<String, Object> result = new HashMap<String, Object>();
	result = ygycService.getYgycFocusEmployee(ygycdata);
	return Json.Encode(result);
    }

    /**
     * 养卡 重点关注员工号 --下钻
     * 
     * <pre>
     * Desc  
     * @param ygycdata
     * @return
     * @author issuser
     * 2017-9-7 下午5:52:27
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.ygycqtcd })
    @ResponseBody
    @RequestMapping(value = "/getYgycFocusEmployeeTable", produces = "text/json;charset=UTF-8")
    public String getYgycFocusEmployeeTable(ParameterData ygycdata) {
	Map<String, Object> result = new HashMap<String, Object>();
	result = ygycService.getYgycFocusEmployeeTable(ygycdata);
	return Json.Encode(result);
    }

}
