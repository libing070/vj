package com.hpe.cmca.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.service.LlglwgService;
import com.hpe.cmca.util.Json;

/**
 */
@Controller
@RequestMapping("llglwg")
public class LlglwgController extends BaseController {

    @Autowired
    protected LlglwgService llglwgService;
    
    @CmcaAuthority(authorityTypes = { AuthorityType.llglsjbg,AuthorityType.llglpmhz,AuthorityType.llglqtcd,AuthorityType.llglsjjg,AuthorityType.llglzgwz})
    @RequestMapping(value = "index")
    public String index() {
	return "llglwg/index";
    }
    
    // controller:流量管理违规-风险地图
    @CmcaAuthority(authorityTypes = { AuthorityType.llglsjjg })
    @ResponseBody
    @RequestMapping(value = "/getMapData", produces = "text/json;charset=UTF-8")
    public String getMapData(ParameterData parameterData){
	return Json.Encode(llglwgService.getMapData(parameterData));
    }
    
    // controller:流量管理违规-风险地图-下方卡片数据
    @CmcaAuthority(authorityTypes = { AuthorityType.llglsjjg })
    @ResponseBody
    @RequestMapping(value = "/getMapBottomData", produces = "text/json;charset=UTF-8")
    public String getMapBottomData(ParameterData parameterData){
	return Json.Encode(llglwgService.getMapBottomData(parameterData));
    }
    // controller:流量管理违规-风险地图-地图下钻-异常流量赠送-7003-重点关注营销案
    @CmcaAuthority(authorityTypes = { AuthorityType.llglsjjg })
    @ResponseBody
    @RequestMapping(value = "/getFocusOfferOfCityTable", produces = "text/json;charset=UTF-8")
    public String getFocusOfferOfCityTable(ParameterData parameterData){
	return Json.Encode(llglwgService.getFocusOfferOfCityTable(parameterData));
    }
    // controller:流量管理违规-统计分析-地图下钻-疑似违规流量转售集团客户信息
    @CmcaAuthority(authorityTypes = { AuthorityType.llglsjjg })
    @ResponseBody
    @RequestMapping(value = "/getFocusOrgCustomerOfCityTable", produces = "text/json;charset=UTF-8")
    public String getFocusOrgCustomerOfCityTable(ParameterData parameterData){
	return Json.Encode(llglwgService.getFocusOrgCustomerOfCityTable(parameterData));
    }
    //controller：流量管理违规-审计结果-所有柱状图
    @CmcaAuthority(authorityTypes = { AuthorityType.llglsjjg })
    @ResponseBody
    @RequestMapping(value = "/getColumnData", produces = "text/json;charset=UTF-8")
    public String getChartData(ParameterData paraData) {
    	return Json.Encode(llglwgService.geColumnData(paraData));
    }
    // controller:流量管理违规-审计结果-所有折线图
    @CmcaAuthority(authorityTypes = { AuthorityType.llglsjjg })
    @ResponseBody
    @RequestMapping(value = "/getLineData", produces = "text/json;charset=UTF-8")
    public String getChartLineData(ParameterData paraData) {
    	return Json.Encode(llglwgService.getLineData(paraData));
    }
    // controller:流量管理违规-审计结果-流量数量异常-柱状图
    @CmcaAuthority(authorityTypes = { AuthorityType.llglsjjg })
    @ResponseBody
    @RequestMapping(value = "/getTrafficNumColumnData", produces = "text/json;charset=UTF-8")
    public String getTrafficNumColumnData(ParameterData parameterData) {
	return Json.Encode(llglwgService.getTrafficNumColumnData(parameterData));
    }

    // controller:流量管理违规-审计结果-流量占比异常-柱状图
    @CmcaAuthority(authorityTypes = { AuthorityType.llglsjjg })
    @ResponseBody
    @RequestMapping(value = "/getTrafficPercentColumnData", produces = "text/json;charset=UTF-8")
    public String getTrafficPercentColumnData(ParameterData parameterData) {
	return Json.Encode(llglwgService.getTrafficPercentColumnData(parameterData));
    }

    // controller:流量管理违规-审计结果-疑似违规流量转售-疑似违规转售集团客户数-柱状图
    @CmcaAuthority(authorityTypes = { AuthorityType.llglsjjg })
    @ResponseBody
    @RequestMapping(value = "/getIllegalGroupNumColumnData", produces = "text/json;charset=UTF-8")
    public String getIllegalGroupNumColumnData(ParameterData parameterData) {
	return Json.Encode(llglwgService.getIllegalGroupNumColumnData(parameterData));
    }

    // controller:流量管理违规-审计结果-疑似违规流量转售-疑似违规转售集团客户数占比-柱状图
    @CmcaAuthority(authorityTypes = { AuthorityType.llglsjjg })
    @ResponseBody
    @RequestMapping(value = "/getIllegalGroupPercentColumnData", produces = "text/json;charset=UTF-8")
    public String getIllegalGroupPercentColumnData(ParameterData parameterData) {
	return Json.Encode(llglwgService.getIllegalGroupPercentColumnData(parameterData));
    }

    // controller:流量管理违规-审计结果-流量异常-折线图
    @CmcaAuthority(authorityTypes = { AuthorityType.llglsjjg })
    @ResponseBody
    @RequestMapping(value = "/getTrafficLineData", produces = "text/json;charset=UTF-8")
    public String getTrafficLineData(HttpServletRequest request,ParameterData parameterData) {
	parameterData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(llglwgService.getTrafficLineData(parameterData));
    }
    // controller:流量管理违规-审计结果-疑似违规流量转售-疑似违规转售集团客户-折线图
    @CmcaAuthority(authorityTypes = { AuthorityType.llglsjjg })
    @ResponseBody
    @RequestMapping(value = "/getIllegalGroupLineData", produces = "text/json;charset=UTF-8")
    public String getIllegalGroupLineData(HttpServletRequest request,ParameterData parameterData) {
	parameterData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(llglwgService.getIllegalGroupLineData(parameterData));
    }
    // controller:流量管理违规-统计分析-排名汇总
    @CmcaAuthority(authorityTypes = { AuthorityType.llglpmhz })
    @ResponseBody
    @RequestMapping(value = "/getRankTable", produces = "text/json;charset=UTF-8")
    public String  getRankTable(ParameterData parameterData) {
	return Json.Encode(llglwgService.getRankTable(parameterData));
    }
    

    //controller:流量管理违规-统计分析-增量分析
    @CmcaAuthority(authorityTypes = { AuthorityType.llglqtcd})
    @ResponseBody
    @RequestMapping(value = "/getIncrementalData", produces = "text/json;charset=UTF-8")
    public String getIncrementalData(ParameterData parameterData){
	return Json.Encode(llglwgService.getIncrementalData(parameterData));
    }
    //controller:流量管理违规-统计分析-重点关注地市
    @CmcaAuthority(authorityTypes = { AuthorityType.llglqtcd})
    @ResponseBody
    @RequestMapping(value = "/getFocusCityTable", produces = "text/json;charset=UTF-8")
    public String getFocusCityTable(ParameterData parameterData){
	return Json.Encode(llglwgService.getFocusCityTable(parameterData));
    }
    //controller:流量管理违规-统计分析-重点关注渠道
    @CmcaAuthority(authorityTypes = { AuthorityType.llglpmhz})
    @ResponseBody
    @RequestMapping(value = "/getFocusChnlTable", produces = "text/json;charset=UTF-8")
    public String getFocusChnlTable(ParameterData parameterData){
	return Json.Encode(llglwgService.getFocusChnlTable(parameterData));
    }
    //controller:流量管理违规-统计分析-重点关注营销案
    @CmcaAuthority(authorityTypes = { AuthorityType.llglpmhz})
    @ResponseBody
    @RequestMapping(value = "/getFocusOfferTable", produces = "text/json;charset=UTF-8")
    public String getFocusOfferTable(ParameterData parameterData){
	return Json.Encode(llglwgService.getFocusOfferTable(parameterData));
    }
    
    //controller:流量管理违规-统计分析-重点关注用户
    @CmcaAuthority(authorityTypes = { AuthorityType.llglpmhz})
    @ResponseBody
    @RequestMapping(value = "/getFocusUserTable", produces = "text/json;charset=UTF-8")
    public String getFocusUserTable(ParameterData parameterData){
	return Json.Encode(llglwgService.getFocusUserTable(parameterData));
    }
    
    //controller:流量管理违规-统计分析-重点关注集团客户
    @CmcaAuthority(authorityTypes = { AuthorityType.llglpmhz})
    @ResponseBody
    @RequestMapping(value = "/getFocusOrgCustomerTable", produces = "text/json;charset=UTF-8")
    public String getFocusOrgCustomerTable(ParameterData parameterData){
	return Json.Encode(llglwgService.getFocusOrgCustomerTable(parameterData));
    }
    
    //controller:流量管理违规-统计分析-违规类型分析-饼图  
    @CmcaAuthority(authorityTypes = { AuthorityType.llglpmhz})
    @ResponseBody
    @RequestMapping(value = "/getTypeDistributePie", produces = "text/json;charset=UTF-8")
    public String getTypeDistributePie(ParameterData parameterData){
	return Json.Encode(llglwgService.getTypeDistributePie(parameterData));
    }
    //controller:流量管理违规-统计分析-违规类型分析-趋势图 
    @CmcaAuthority(authorityTypes = { AuthorityType.llglpmhz})
    @ResponseBody
    @RequestMapping(value = "/getTypeDistributeStack", produces = "text/json;charset=UTF-8")
    public String getTypeDistributeStack(HttpServletRequest request,ParameterData parameterData){
	parameterData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(llglwgService.getTypeDistributeStack(parameterData));
    }
    //controller:流量管理违规-统计分析-审计报告
    @CmcaAuthority(authorityTypes = { AuthorityType.llglsjbg})
    @ResponseBody
    @RequestMapping(value = "/getReportInfo", produces = "text/json;charset=UTF-8")
    public String getReportInfo(HttpServletRequest request,ParameterData parameterData){
	if(getAttributeByAudTrmAndUser(request,parameterData.getAudTrm(),"7")){
	    return Json.Encode(llglwgService.getReportInfo(parameterData));    
	}else{
		Map<String,Object> map=new HashMap<String,Object>(1);
		map.put("switchState", "audTrmColseForReport");
	    return Json.Encode(map);
	}
    }
    
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    
    /**
	 * @throws ParseException 
	 * @Title 各省六个月内达到<整改>标准次数排名——柱状图
	 */
 	@ResponseBody
    @RequestMapping(value = "/getRectifiyForSixColumn", produces = "text/json;charset=UTF-8")
    public String getAmountColumnDataForChangeStandardSixMonth(@RequestParam("audTrm") String currDate) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		result = llglwgService.getAmountColumnDataForChangeStandardBetweenSixMonth(currDate) ;
		return Json.Encode(result);
    }
 	
 	/**
	 * @Title 各省累计达到<整改>次数排名——柱状图
	 */
 	@ResponseBody
    @RequestMapping(value = "/getRectifiyColumn", produces = "text/json;charset=UTF-8")
    public String getAmountColumnDataForChangeStandardAll(@RequestParam("audTrm") String currDate) {
		Map<String, Object> result = new HashMap<String, Object>();
		result = llglwgService.getAmountColumnDataForChangeStandardAll(currDate) ;
		return Json.Encode(result);
    }
	 	
 	/**
	 * @throws ParseException 
 	 * @Title 达到<整改>标准省公司数量趋势——折线图
	 */
 	@ResponseBody
    @RequestMapping(value = "/getRectifiyLine", produces = "text/json;charset=UTF-8")
    public String getRectifiyLineDataForChangeStandardAll(@RequestParam("audTrm") String audTrm) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		result = llglwgService.getRectifiyLineDataForChangeStandard(audTrm) ;
		return Json.Encode(result);
    }
 	
 	
 	/**
	 * @throws ParseException 
	 * @Title 各省六个月内达到<问责>标准次数排名——柱状图
	 */
 	@ResponseBody
    @RequestMapping(value = "/getAccountForSixColumn", produces = "text/json;charset=UTF-8")
    public String getAccountForSixColumn(@RequestParam("audTrm") String currDate) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		result = llglwgService.getAccountForSixColumn(currDate) ;
		return Json.Encode(result);
    }
 	
 	/**
	 * @Title 各省累计达到<问责>次数排名——柱状图
	 */
 	@ResponseBody
    @RequestMapping(value = "/getAccountabilityColumn", produces = "text/json;charset=UTF-8")
    public String getAccountabilityColumn(@RequestParam("audTrm") String currDate) {
		Map<String, Object> result = new HashMap<String, Object>();
		result = llglwgService.getAccountabilityColumn(currDate) ;
		return Json.Encode(result);
    }
	 	
 	/**
	 * @throws ParseException 
 	 * @Title 达到<问责>标准省公司数量趋势——折线图
	 */
 	@ResponseBody
    @RequestMapping(value = "/getAccountabilityLine", produces = "text/json;charset=UTF-8")
    public String getAccountabilityLine(@RequestParam("audTrm") String audTrm ) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		result = llglwgService.getAccountabilityLine(audTrm) ;
		return Json.Encode(result);
    }
 	
 	/**
 	 *  @throws ParseException 
 	 * @Title 审计整改问责事项明细表
 	 */
 	@ResponseBody
    @RequestMapping(value = "/getChangeAccountInfoTable", produces = "text/json;charset=UTF-8")
 	public String getChangeAccountInfoTable(@RequestParam("audTrm") String audTrm , @RequestParam("prvdId") String prvdId ) throws ParseException{
 		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		result = llglwgService.getChangeAccountInfoTable(audTrm, prvdId) ;
		return Json.Encode(result);
 	}
 	
 	/**
 	 * @throws Exception 
 	 * @Title 重点关注集团客户（表格）
 	 */
 	@ResponseBody
    @RequestMapping(value = "/getFocusOrgCustomerTableForZgWz", produces = "text/json;charset=UTF-8")
 	public String getFocusOrgCustomerTableForZgWz(@RequestParam("audTrm") String audTrm , @RequestParam("prvdId") String prvdId) throws Exception{
 		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		result = llglwgService.getFocusOrgCustomerTableForZgWz(audTrm , prvdId) ;
		return Json.Encode(result);
 	}
 	
 	
 	/**
 	 * @throws Exception 
 	 * @Title 根据集团标识查询当前集团具体信息
 	 */
 	@ResponseBody
    @RequestMapping(value = "/getGroupInfo", produces = "text/json;charset=UTF-8")
 	public String getCompanyInfoByCompanyIDAndProvince(@RequestParam("orgCustId")  String orgCustId  , @RequestParam("prvdId")  String prvdId , @RequestParam("audTrm") String audTrm ) throws Exception{
 		Map<String, Object> result = new HashMap<String, Object>();
		result = llglwgService.getCompanyInfoByCompanyID(orgCustId , prvdId , audTrm) ;
		return Json.Encode(result);
 	}
 	
 	
 	/**
 	 * @throws Exception 
 	 * @Title 自201809审计月被判定为疑似转售流量的审计月对应数据
 	 */
 	@ResponseBody
    @RequestMapping(value = "/getYszsllAudTrmData", produces = "text/json;charset=UTF-8")
 	public String getYszsllAudTrmData(@RequestParam("orgCustId")  String orgCustId  , @RequestParam("audTrm")  String audTrm ) throws Exception{
 		Map<String, Object> result = new HashMap<String, Object>();
		result = llglwgService.getYszsllAudTrmData(orgCustId , audTrm) ;
		return Json.Encode(result);
 	}
 	
 	
 	/**
 	 * @throws Exception 
 	 * @Title 地图联动（全国，城市当前审计月违规公司数据）
 	 */
 	@ResponseBody
    @RequestMapping(value = "/getDataForMapLinkage", produces = "text/json;charset=UTF-8")
 	public String getDataForMapLinkage(@RequestParam("audTrm")  String audTrm  , @RequestParam("prvdId")  String prvdId ) throws Exception{
 		Map<String, Object> result = new HashMap<String, Object>();
		result = llglwgService.getDataForMapLinkage(audTrm,prvdId) ;
		return Json.Encode(result);
 	}
/*
    @ResponseBody
    @RequestMapping(value = "/getBasicType", produces = "text/json;charset=UTF-8")
    public String getBasicType(int prvdId,String audTrm,String concern,String parameterType){
	System.out.println("prvdId："+prvdId);
	System.out.println("audTrm:"+audTrm);
	System.out.println("concern:"+concern);
	System.out.println("parameterType:"+parameterType);
	return Json.Encode("prvdId："+prvdId+";audTrm:"+audTrm+";concern:"+concern+";parameterType:"+parameterType);
    }

    @ResponseBody
    @RequestMapping(value = "/getObject", produces = "text/json;charset=UTF-8")
    public String getObject(ObjectData objectData){
	System.out.println("prvdId："+objectData.getPrvdId());
	System.out.println("audTrm:"+objectData.getAudTrm());
	System.out.println("concern:"+objectData.getConcern());
	System.out.println("parameterType:"+objectData.getParameterType());
	return Json.Encode(objectData);
    }
    
    @ResponseBody
    @RequestMapping(value = "/getReleaseObject", produces = "text/json;charset=UTF-8")
    public String getReleaseObject(ReleaseObjectData data){
	System.out.println("prvdName:"+data.getPrvdName());
	System.out.println("prvdId："+data.getObjectData().getPrvdId());
	System.out.println("audTrm:"+data.getObjectData().getAudTrm());
	System.out.println("concern:"+data.getObjectData().getConcern());
	System.out.println("parameterType:"+data.getObjectData().getParameterType());
	return Json.Encode(data);
    }
    */
    
    
}
