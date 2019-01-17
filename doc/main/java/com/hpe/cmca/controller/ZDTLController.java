package com.hpe.cmca.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.job.ZgwzGenFileProcessor;
import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.service.ZdtlService;
import com.hpe.cmca.util.Json;

/**
 * 终端套利
 */
@Controller
@RequestMapping("/zdtl")
public class ZDTLController extends BaseController {
    @Autowired
    protected ZdtlService zdtlService;
    
    @Autowired
	 protected ZgwzGenFileProcessor  zgwzGenFileProcessor;
    
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg,AuthorityType.zdtlpmhz,AuthorityType.zdtlqtcd,AuthorityType.zdtlsjzl,AuthorityType.zdtlwjxz,AuthorityType.zdtlzgwz })
    @RequestMapping(value = "index")
    public String index() {
	return "zdtl/index";
    }

    //获取终端套利风险地图数据 -add by hufei -2017-7-25 下午5:41:35
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg })
    @ResponseBody
    @RequestMapping(value = "/getMapData", produces = "text/json;charset=UTF-8")
    public String getMapData(ParameterData parameterData){
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
	return Json.Encode(zdtlService.getMapData(parameterData));
    }
    
    //获取终端套利风险地图-下方卡片数据 -add by hufei -2017-7-26 下午5:41:35
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg })
    @ResponseBody
    @RequestMapping(value = "/getMapBottomData", produces = "text/json;charset=UTF-8")
    public String getMapBottomData(ParameterData parameterData){
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
	return Json.Encode(zdtlService.getMapBottomData(parameterData));
    }
    
    //获取终端套利风险地图-地市渠道级别信息数据-top50 -add by hufei -2017-7-27 下午3:34:35
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg })
    @ResponseBody
    @RequestMapping(value = "/getChnlTable", produces = "text/json;charset=UTF-8")
    public String getChnlTable(ParameterData parameterData){
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
	return Json.Encode(zdtlService.getChnlTable(parameterData));
    }
    
    //获取终端套利风险地图-根据渠道名称查询渠道信息-add by hufei -2017-8-7 下午3:34:35
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg })
    @ResponseBody
    @RequestMapping(value = "/getChnlByChnlName", produces = "text/json;charset=UTF-8")
    public String getChnlByChnlName(ParameterData parameterData){
	return Json.Encode(zdtlService.getChnlByChnlName(parameterData));
    }
    
    //终端套利-审计结果-套利金额排名（柱状图） -add by hufei -2017-7-20 下午5:46:01
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg })
    @ResponseBody
    @RequestMapping(value = "/getAmountColumnData", produces = "text/json;charset=UTF-8")
    public String getAmountColumnData(ParameterData parameterData) {
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
	return Json.Encode(zdtlService.getAmountColumnData(parameterData));
    }
    
    //终端套利-审计结果-套利金额占比排名（柱状图） -add by hufei -2017-7-20 下午5:46:01
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg })
    @ResponseBody
    @RequestMapping(value = "/getAmountPercentColumnData", produces = "text/json;charset=UTF-8")
    public String getAmountPercentColumnData(ParameterData parameterData) {
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
	return Json.Encode(zdtlService.getAmountPercentColumnData(parameterData));
    }

    //终端套利-审计结果-异常销售数量排名（柱状图） -add by hufei -2017-7-20 下午5:47:04
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg })
    @ResponseBody
    @RequestMapping(value = "/getNumberColumnData", produces = "text/json;charset=UTF-8")
    public String getNumberColumnData(ParameterData parameterData) {
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
	return Json.Encode(zdtlService.getNumberColumnData(parameterData));
    }

    //终端套利-审计结果-异常销售占比排名（柱状图） -add by hufei -2017-7-20 下午5:47:04
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg })
    @ResponseBody
    @RequestMapping(value = "/getPercentColumnData", produces = "text/json;charset=UTF-8")
    public String getPercentColumnData(ParameterData parameterData) {
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
	return Json.Encode(zdtlService.getPercentColumnData(parameterData));
    }

    //终端套利-审计结果-套利金额排名（折线图） -add by hufei -2017-7-20 下午5:56:36
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg })
    @ResponseBody
    @RequestMapping(value = "/getAmountLineData", produces = "text/json;charset=UTF-8")
    public String getAmountLineData(HttpServletRequest request,ParameterData parameterData){
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
   	parameterData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(zdtlService.getAmountLineData(parameterData));
    }

    //终端套利-审计结果-套利占比排名（折线图） -add by hufei -2017-7-20 下午5:56:36
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg })
    @ResponseBody
    @RequestMapping(value = "/getPercentLineData", produces = "text/json;charset=UTF-8")
    public String getPercentLineData(HttpServletRequest request,ParameterData parameterData){
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
	parameterData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(zdtlService.getPercentLineData(parameterData));
    }
    
    //终端套利-审计结果-异常销售渠道占比排名（折线图） -add by hufei -2017-7-31 
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg })
    @ResponseBody
    @RequestMapping(value = "/getPercentChnlLineData", produces = "text/json;charset=UTF-8")
    public String getPercentChnlLineData(HttpServletRequest request,ParameterData parameterData){
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
	parameterData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(zdtlService.getPercentChnlLineData(parameterData));
    }
    
    //终端套利-审计结果-异常销售渠道占比排名（柱状图） -add by hufei -2017-7-31 
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg })
    @ResponseBody
    @RequestMapping(value = "/getPercentChnlColumnData", produces = "text/json;charset=UTF-8")
    public String getPercentChnlColumnData(ParameterData parameterData){
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
	return Json.Encode(zdtlService.getPercentChnlColumnData(parameterData));
    }
	
    //终端套利-审计结果-异常销售渠道数量排名（柱状图） -add by hufei -2017-7-31 
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg })
    @ResponseBody
    @RequestMapping(value = "/getNumberChnlColumnData", produces = "text/json;charset=UTF-8")
    public String getNumberChnlColumnData(ParameterData parameterData){
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
	return Json.Encode(zdtlService.getNumberChnlColumnData(parameterData));
    }
    
    //终端套利-风险地图-渠道基本信息 -add by hufei -2017-7-31 
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg })
    @ResponseBody
    @RequestMapping(value = "/getChnlBaseInfo", produces = "text/json;charset=UTF-8")
    public String getChnlBaseInfo(ParameterData parameterData){
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
	return Json.Encode(zdtlService.getChnlBaseInfo(parameterData));
    }
    
    //终端套利-风险地图-渠道趋势信息 -add by hufei -2017-7-31 
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlsjjg })
    @ResponseBody
    @RequestMapping(value = "/getChnlTrend", produces = "text/json;charset=UTF-8")
    public String getChnlTrend(HttpServletRequest request,ParameterData parameterData){
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
	parameterData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(zdtlService.getChnlTrend(parameterData));
    }
    //终端套利-风险地图-渠道信息 -add by hufei -2017-7-31 
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlpmhz })
    @ResponseBody
    @RequestMapping(value = "/getRankTable", produces = "text/json;charset=UTF-8")
    public String getRankTable(ParameterData parameterData){
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
	return Json.Encode(zdtlService.getRankTable(parameterData));
    }
    
    //终端套利-统计分析-增量分析 -add by hufei -2017-8-8 
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlqtcd })
    @ResponseBody
    @RequestMapping(value = "/getIncrementalData", produces = "text/json;charset=UTF-8")
    public String getIncrementalData(ParameterData parameterData){
	if(parameterData.getConcern().equals("3000")){
	    parameterData.setConcern("3001,3002,3004,3005");
	}
	return Json.Encode(zdtlService.getIncrementalData(parameterData));
    }
    //终端套利-统计分析-违规类型分析-饼图 -add by hufei -2017-8-9 
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlqtcd })
    @ResponseBody
    @RequestMapping(value = "/getTypeDistributePie", produces = "text/json;charset=UTF-8")
    public String getTypeDistributePie(ParameterData parameterData){
	return Json.Encode(zdtlService.getTypeDistributePie(parameterData));
    }
    //终端套利-统计分析-违规类型分析-趋势图 -add by hufei -2017-8-9 
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlqtcd })
    @ResponseBody
    @RequestMapping(value = "/getTypeDistributeStack", produces = "text/json;charset=UTF-8")
    public String getTypeDistributeStack(HttpServletRequest request,ParameterData parameterData){
	parameterData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(zdtlService.getTypeDistributeStack(parameterData));
    }
    //终端套利-统计分析-整改问责统计-近六个月整改-柱状图-add by hufei 2017.8.28
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlzgwz })
    @ResponseBody
    @RequestMapping(value = "/getRectifiyForSixColumn", produces = "text/json;charset=UTF-8")
    public String getRectifyForSixColumn(ParameterData parameterData){
	return Json.Encode(zdtlService.getRectifyForSixColumn(parameterData));
    }
    //终端套利-统计分析-整改问责统计-近六个月问责-柱状图-add by hufei 2017.8.28
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlzgwz })
    @ResponseBody
    @RequestMapping(value = "/getAccountForSixColumn", produces = "text/json;charset=UTF-8")
    public String getAccountForSixColumn(ParameterData parameterData){
	return Json.Encode(zdtlService.getAccountForSixColumn(parameterData));
    }
    //终端套利-统计分析-整改问责统计-累计达到整改次数-柱状图-add by hufei 2017.8.28
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlzgwz })
    @ResponseBody
    @RequestMapping(value = "/getRectifiyColumn", produces = "text/json;charset=UTF-8")
    public String getRectifyColumn(ParameterData parameterData){
	return Json.Encode(zdtlService.getRectifyColumn(parameterData));
    }
    //终端套利-统计分析-整改问责统计-累计达到问责次数-柱状图-add by hufei 2017.8.28
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlzgwz })
    @ResponseBody
    @RequestMapping(value = "/getAccountabilityColumn", produces = "text/json;charset=UTF-8")
    public String getAccountabilityColumn(ParameterData parameterData){
	return Json.Encode(zdtlService.getAccountabilityColumn(parameterData));
    }
    
    //终端套利-统计分析-整改问责统计-累计达到整改次数-折线图-add by hufei 2017.8.28
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlzgwz })
    @ResponseBody
    @RequestMapping(value = "/getRectifiyLine", produces = "text/json;charset=UTF-8")
    public String getRectifyLine(HttpServletRequest request,ParameterData parameterData){
	parameterData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(zdtlService.getRectifyLine(parameterData));
    }
    //终端套利-统计分析-整改问责统计-累计达到问责次数-折线图-add by hufei 2017.8.28
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlzgwz })
    @ResponseBody
    @RequestMapping(value = "/getAccountabilityLine", produces = "text/json;charset=UTF-8")
    public String getAccountabilityLine(HttpServletRequest request,ParameterData parameterData){
	parameterData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(zdtlService.getAccountabilityLine(parameterData));
    }
    //终端套利-统计分析-审计报告 -add by hufei -2017-8-23
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlwjxz })
    @ResponseBody
    @RequestMapping(value = "/getReportInfo", produces = "text/json;charset=UTF-8")
    public String getReportInfo(HttpServletRequest request,ParameterData parameterData){
	if(getAttributeByAudTrmAndUser(request,parameterData.getAudTrm(),"3")){
	    return Json.Encode(zdtlService.getReportInfo(parameterData));    
	}else{
		Map<String,Object> map=new HashMap<String,Object>(1);
		map.put("switchState", "audTrmColseForReport");
	    return Json.Encode(map);
	}
    }
    //终端套利-统计分析-重点关注渠道
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlqtcd })
    @ResponseBody
    @RequestMapping(value = "/getFocusThingTable", produces = "text/json;charset=UTF-8")
    public String getFocusThingTable(ParameterData parameterData){
	return Json.Encode(zdtlService.getFocusThingTable(parameterData));
    }
    
    /**
     * 整改问责要求（终端套利）
     * <pre>
     * Desc  
     * @param qdyk
     * @return
     * @throws Exception
     * @author issuser
     * 2017-8-30 上午10:49:36
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.zdtlzgwz })
    @ResponseBody
   	@RequestMapping(value = "getZDTLZgwzData", produces = "text/json; charset=UTF-8")
   	public String getZDTLZgwzData(ParameterData qdyk) throws Exception {
    	if(qdyk.getAudTrm()==null||"".equals(qdyk.getAudTrm())){
    		List<Object> list = new ArrayList<>();
    		return Json.Encode(list);
    	}
    	List<Map<String,Object>> zgwzlist  =zgwzGenFileProcessor.generateQdyk(qdyk.getAudTrm(),qdyk.getPrvdId(),qdyk.getConcern());
   		return Json.Encode(zgwzlist);

   	}
    
}
