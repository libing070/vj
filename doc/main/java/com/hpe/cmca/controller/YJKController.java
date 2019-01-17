package com.hpe.cmca.controller;

/**
 * 有价卡
 */

import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.job.ZgwzGenFileProcessor;
import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.pojo.ZGWZData;
import com.hpe.cmca.service.YjkService;
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

@Controller
@RequestMapping("/yjk")
public class YJKController extends BaseController {

    @Autowired
    protected YjkService yjkService;

    @Autowired
	 protected ZgwzGenFileProcessor  zgwzGenFileProcessor;

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_sjjg,AuthorityType.yjk_pmhz,AuthorityType.yjk_qtcd,AuthorityType.yjk_sjzl,AuthorityType.yjk_wjxz,AuthorityType.yjk_zgwz})
    @RequestMapping(value = "index")
    public String index() {
        return "yjk/index";
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_sjjg })
    @ResponseBody
    @RequestMapping(value = "/getMapData", produces = "text/json;charset=UTF-8")
    public String getMapData(ParameterData yjk) {

    	return Json.Encode(yjkService.getMapData(yjk));
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_sjjg })
    @ResponseBody
    @RequestMapping(value = "/getMapBottomData", produces = "text/json;charset=UTF-8")
    public String getMapBottomData(ParameterData yjk) {

    	return Json.Encode(yjkService.getMapBottomData(yjk));
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_sjjg })
    @ResponseBody
    @RequestMapping(value = "/getColumnAmtData", produces = "text/json;charset=UTF-8")
    public String getColumnAmtData(ParameterData yjk) {

    	return Json.Encode(yjkService.getColumnAmtData(yjk));
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_sjjg })
    @ResponseBody
    @RequestMapping(value = "/getColumnNumData", produces = "text/json;charset=UTF-8")
    public String getColumnNumData(ParameterData yjk) {

    	return Json.Encode(yjkService.getColumnNumData(yjk));
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_sjjg })
    @ResponseBody
    @RequestMapping(value = "/getLineAmtData", produces = "text/json;charset=UTF-8")
    public String getLineAmtData(HttpServletRequest request,ParameterData yjk) {
	yjk.setSwitchState(getSwitchStateByDepId(request));
    	return Json.Encode(yjkService.getLineAmtData(yjk));
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_sjjg })
    @ResponseBody
    @RequestMapping(value = "/getLineNumData", produces = "text/json;charset=UTF-8")
    public String getLineNumData(HttpServletRequest request,ParameterData yjk) {
	yjk.setSwitchState(getSwitchStateByDepId(request));
    	return Json.Encode(yjkService.getLineNumData(yjk));
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_sjjg })
	@ResponseBody
	@RequestMapping(value = "getCrmVsVcData", produces = "text/json; charset=UTF-8")
	public String getCrmVcDiftData(ParameterData yjk) {

		return Json.Encode(yjkService.getCrmVsVcData(yjk));

	}

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_qtcd })
	@ResponseBody
	@RequestMapping(value = "/getIncrementalData", produces = "text/json;charset=UTF-8")
	public String getIncrementalData(ParameterData yjk) {
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		result=yjkService.getIncrementalData(yjk);
		return Json.Encode(result);
	}

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_qtcd })
	@ResponseBody
	@RequestMapping(value = "/getAmountPie", produces = "text/json;charset=UTF-8")
	public String getAmountPie(ParameterData yjk) {
		Map<Integer,Object> result=new HashMap<Integer,Object>();
		result=yjkService.getAmountPie(yjk);
		return Json.Encode(result);
	}

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_qtcd })
	@ResponseBody
	@RequestMapping(value = "/getPerTrend", produces = "text/json;charset=UTF-8")
	public String getPerTrend(HttpServletRequest request,ParameterData yjk) {
		Map<String,List<Object>> result=new HashMap<String,List<Object>>();
		yjk.setSwitchState(getSwitchStateByDepId(request));
		result=yjkService.getPerTrend(yjk);
		return Json.Encode(result);
	}

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_zgwz })
	@ResponseBody
	@RequestMapping(value = "/getZGWZReq", produces = "text/json;charset=UTF-8")
	public String getZGWZReq(ParameterData yjk) {
		List<ZGWZData> result = new ArrayList<ZGWZData>();
		result=yjkService.getZGWZReq(yjk);
		return Json.Encode(result);
	}

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_zgwz })
    @ResponseBody
    @RequestMapping(value = "/getZGWZColumn1", produces = "text/json;charset=UTF-8")
    public String getZGWZColumn1(ParameterData yjk) {

    	return Json.Encode(yjkService.getZGWZColumn1(yjk));
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_zgwz })
    @ResponseBody
    @RequestMapping(value = "/getZGWZColumn2", produces = "text/json;charset=UTF-8")
    public String getZGWZColumn2(ParameterData yjk) {

    	return Json.Encode(yjkService.getZGWZColumn2(yjk));
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_zgwz })
    @ResponseBody
    @RequestMapping(value = "/getZGWZLine", produces = "text/json;charset=UTF-8")
    public String getZGWZLine(HttpServletRequest request,ParameterData yjk) {
	yjk.setSwitchState(getSwitchStateByDepId(request));
    	return Json.Encode(yjkService.getZGWZLine(yjk));
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_wjxz })
    @ResponseBody
    @RequestMapping(value = "/getAuditReport", produces = "text/json;charset=UTF-8")
    public String getAuditReport(HttpServletRequest request,ParameterData yjk) {
	if(getAttributeByAudTrmAndUser(request,yjk.getAudTrm(),"1")){
	    return Json.Encode(yjkService.getAuditReport(yjk));
	}else{
		Map<String,Object> map=new HashMap<String,Object>(1);
		map.put("switchState", "audTrmColseForReport");
	    return Json.Encode(map);
	}
    	//return Json.Encode(yjkService.getAuditReport(yjk));
    }

    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_pmhz })
    @ResponseBody
    @RequestMapping(value = "/getYjkPmhz", produces = "text/json;charset=UTF-8")
    public String getYjkPmhz(ParameterData yjk) {

    	return Json.Encode(yjkService.getYjkPmhz(yjk));
    }

    /**
     * 整改问责要求（有价卡）
     * <pre>
     * Desc
     * @param qdyk
     * @return
     * @throws Exception
     * @author issuser
     * 2017-8-30 上午10:49:36
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.yjk_zgwz })
    @ResponseBody
   	@RequestMapping(value = "getYJKZgwzData", produces = "text/json; charset=UTF-8")
   	public String getYJKZgwzData(ParameterData qdyk) throws Exception {
    	if(qdyk.getAudTrm()==null||"".equals(qdyk.getAudTrm())){
    		List<Object> list = new ArrayList<>();
    		 return Json.Encode(list);
    	}
    	List<Map<String,Object>> zgwzlist  =zgwzGenFileProcessor.generateQdyk(qdyk.getAudTrm(),qdyk.getPrvdId(),qdyk.getConcern());
   		return Json.Encode(zgwzlist);

   	}
}
