package com.hpe.cmca.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.service.DzqService;
import com.hpe.cmca.util.Json;

/**
 * @author yuzn1
 */
@Controller
@RequestMapping("/dzqglwg")
public class DzqController extends BaseController {

    @Autowired
    private DzqService dzqService;
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqpmhz,AuthorityType.dzqqtcd,AuthorityType.dzqsjbg,AuthorityType.dzqsjjg,AuthorityType.dzqzgwz})
    @RequestMapping(value = "index")
    public String index() {
	return "dzqglwg/index";
    }

    /**
     * 所有地图方法
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getMapData", produces = "text/json;charset=UTF-8")
    public String getMapData(ParameterData paraData) {
	return Json.Encode(dzqService.getMapData(paraData));
    }

    /**
     * 所有柱图方法
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getChartData", produces = "text/json;charset=UTF-8")
    public String getChartData(ParameterData paraData) {
	return Json.Encode(dzqService.getChartData(paraData));
    }

    /**
     * 所有折线方法
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getChartLineData", produces = "text/json;charset=UTF-8")
    public String getChartLineData(HttpServletRequest request, ParameterData paraData) {
	paraData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(dzqService.getChartLineData(paraData));
    }

    /**
     * 风险地图（金额）map 平台数据不一致（ 金额 省无基地有，省饼图（金额）） 电子券 违规金额 map(全国，地市)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getMapAmtData", produces = "text/json;charset=UTF-8")
    public String getMapAmtData(ParameterData paraData) {
	return Json.Encode(dzqService.getMapAmtData(paraData));
    }

    /**
     * 风险地图（金额占比）map 平台数据不一致（ 金额占比 省无基地有，省饼图（金额）） 电子券 违规金额占比 map(全国，地市)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getMapAmtPerData", produces = "text/json;charset=UTF-8")
    public String getMapAmtPerData(ParameterData paraData) {
	return Json.Encode(dzqService.getMapAmtPerData(paraData));
    }

    /**
     * 风险地图（数量）map 平台数据不一致（ 金额 省有基地无，省饼图（金额）） 电子券 违规数量 map(全国，地市)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getMapNumData", produces = "text/json;charset=UTF-8")
    public String getMapNumData(ParameterData paraData) {
	return Json.Encode(dzqService.getMapNumData(paraData));
    }

    /**
     * 风险地图（数量占比）map 平台数据不一致（ 金额占比 省有基地无，省饼图（金额）） 电子券 违规数量占比 map(全国，地市)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getMapNumPerData", produces = "text/json;charset=UTF-8")
    public String getMapNumPerData(ParameterData paraData) {
	return Json.Encode(dzqService.getMapNumPerData(paraData));
    }

    /**
     * 地图下方卡片 电子券 数据平台不一致
     * 
     * @param parameterData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getCardData", produces = "text/json;charset=UTF-8")
    public String getCardData(ParameterData parameterData) {
	return Json.Encode(dzqService.getCardData(parameterData));
    }

    /**
     * 违规金额排名， Column 省无基地有电子券金额排名
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getColumnAmtData", produces = "text/json;charset=UTF-8")
    public String getColumnAmtData(ParameterData paraData) {
	return Json.Encode(dzqService.getColumnAmtData(paraData));
    }

    /**
     * 违规金额排名 line (电子券) 省无基地有 line
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getLineAmtData", produces = "text/json;charset=UTF-8")
    public String getLineAmtData(HttpServletRequest request, ParameterData paraData) {
	paraData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(dzqService.getLineAmtData(paraData));
    }

    /**
     * 违规金额占比排名 Column (电子券)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getColumnAmtPerData", produces = "text/json;charset=UTF-8")
    public String getColumnAmtPerData(ParameterData paraData) {
	return Json.Encode(dzqService.getColumnAmtPerData(paraData));
    }

    /**
     * 违规金额占比排名 line 省无基地有 line
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getLineAmtPerData", produces = "text/json;charset=UTF-8")
    public String getLineAmtPerData(HttpServletRequest request, ParameterData paraData) {
	paraData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(dzqService.getLineAmtPerData(paraData));
    }

    /**
     * 违规数量排名 Column (电子券)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getColumnNumData", produces = "text/json;charset=UTF-8")
    public String getColumnNumData(ParameterData paraData) {
	return Json.Encode(dzqService.getColumnNumData(paraData));
    }

    /**
     * 违规数量排名 line (电子券)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getLineNumData", produces = "text/json;charset=UTF-8")
    public String getLineNumData(HttpServletRequest request, ParameterData paraData) {
	paraData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(dzqService.getLineNumData(paraData));
    }

    /**
     * 违规数量占比排名 Column (电子券)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getColumnNumPerData", produces = "text/json;charset=UTF-8")
    public String getColumnNumPerData(ParameterData paraData) {
	return Json.Encode(dzqService.getColumnNumPerData(paraData));
    }

    /**
     * 违规数量占比排名 line (电子券)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getLineNumPerData", produces = "text/json;charset=UTF-8")
    public String getLineNumPerData(HttpServletRequest request, ParameterData paraData) {
	paraData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(dzqService.getLineNumData(paraData));
    }

    /* ============================================================统计分析======================================================== */

    /**
     * 排名汇总 （电子券）table
     * 
     * @param parameterData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqpmhz })
    @ResponseBody
    @RequestMapping(value = "/getRankTable", produces = "text/json;charset=UTF-8")
    public String getRankTable(ParameterData parameterData) {
	return Json.Encode(dzqService.getRankTable(parameterData));
    }

    /**
     * 统计分析 瀑布图 (电子券)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqqtcd })
    @ResponseBody
    @RequestMapping(value = "/getIncrementalData", produces = "text/json;charset=UTF-8")
    public String getIncrementalData(ParameterData paraData) {
	return Json.Encode(dzqService.getIncrementalData(paraData));
    }

    /**
     * 重点关注地市 table （电子券）
     * 
     * @param parameterData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqqtcd })
    @ResponseBody
    @RequestMapping(value = "/getCityTable", produces = "text/json;charset=UTF-8")
    public String getCityTable(ParameterData parameterData) {
	return Json.Encode(dzqService.getCityTable(parameterData));
    }

    /**
     * 重点关注营销案 table （电子券）
     * 
     * @param parameterData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqpmhz })
    @ResponseBody
    @RequestMapping(value = "/getOfferTable", produces = "text/json;charset=UTF-8")
    public String getOfferTable(ParameterData parameterData) {
	return Json.Encode(dzqService.getOfferTable(parameterData));
    }

    /**
     * 重点关注渠道 table （电子券）
     * 
     * @param parameterData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqpmhz })
    @ResponseBody
    @RequestMapping(value = "/getChannelTable", produces = "text/json;charset=UTF-8")
    public String getChannelTable(ParameterData parameterData) {
	return Json.Encode(dzqService.getChannelTable(parameterData));
    }

    /**
     * 重点关注用户 table （电子券）
     * 
     * @param parameterData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqpmhz })
    @ResponseBody
    @RequestMapping(value = "/getSubsTable", produces = "text/json;charset=UTF-8")
    public String getSubsTable(ParameterData parameterData) {
	return Json.Encode(dzqService.getSubsTable(parameterData));
    }

    /**
     * 重点关注类型分布 （饼图） （电子券）
     * 
     * @param parameterData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqpmhz })
    @ResponseBody
    @RequestMapping(value = "/getTypeDistributionPie", produces = "text/json;charset=UTF-8")
    public String getTypeDistributionPie(ParameterData parameterData) {
	return Json.Encode(dzqService.getTypeDistributionPie(parameterData));
    }

    /**
     * 重点关注类型分布 （折线） （电子券）
     * 
     * @param parameterData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqpmhz })
    @ResponseBody
    @RequestMapping(value = "/getTypeDistributionLine", produces = "text/json;charset=UTF-8")
    public String getTypeDistributionLine(ParameterData parameterData) {
	return Json.Encode(dzqService.getTypeDistributionLine(parameterData));
    }

    /**
     * 审计报告
     * 
     * @param parameterData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjbg })
    @ResponseBody
    @RequestMapping(value = "/getReportInfoData", produces = "text/json;charset=UTF-8")
    public String getReportInfoData(ParameterData parameterData) {
	return Json.Encode(dzqService.getReportInfoData(parameterData));
    }

    /* ================================================电子券 平台数据不一致================================================================== */
    /**
     * 风险地图数据 金额 省有基地无 (电子券 平台数据不一致)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getPlatPrvdMapAmtData", produces = "text/json;charset=UTF-8")
    public String getPlatPrvdMapAmtData(ParameterData paraData) {
	return Json.Encode(dzqService.getPlatPrvdMapAmtData(paraData));
    }

    /**
     * 风险地图数据 金额 省无基地有 (电子券 平台数据不一致)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getPlatBasePrvdMapAmtData", produces = "text/json;charset=UTF-8")
    public String getPlatBasePrvdMapAmtData(ParameterData paraData) {
	return Json.Encode(dzqService.getPlatBasePrvdMapAmtData(paraData));
    }

    /**
     * 风险地图数据 金额 省（饼图 电子券 平台数据不一致）
     * 
     * @param parameterData
     * @return
     * */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getPlatCityPieAmtData", produces = "text/json;charset=UTF-8")
    public String getPlatCityPieAmtData(ParameterData parameterData) {
	return Json.Encode(dzqService.getPlatCityPieAmtData(parameterData));
    }

    /**
     * 省无基地有 金额排名 Column (电子券平台数据不一致)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getPlatBaseColumnAmtData", produces = "text/json;charset=UTF-8")
    public String getPlatBaseColumnAmtData(ParameterData paraData) {
	return Json.Encode(dzqService.getPlatBaseColumnAmtData(paraData));
    }

    /**
     * 省无基地有 金额排名 line (电子券平台数据不一致)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getPlatBaseLineAmtData", produces = "text/json;charset=UTF-8")
    public String getPlatBaseLineAmtData(HttpServletRequest request, ParameterData paraData) {
	paraData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(dzqService.getPlatBaseLineAmtData(paraData));
    }

    /**
     * 省无基地有 金额占比排名 Column (电子券平台数据不一致)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getPlatBaseColumnAmtPerData", produces = "text/json;charset=UTF-8")
    public String getPlatBaseColumnAmtPerData(ParameterData paraData) {
	return Json.Encode(dzqService.getPlatBaseColumnAmtPerData(paraData));
    }

    /**
     * 省无基地有 金额占比排名 line (电子券平台数据不一致)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getPlatBaseLineAmtPerData", produces = "text/json;charset=UTF-8")
    public String getPlatBaseLineAmtPerData(HttpServletRequest request, ParameterData paraData) {
	paraData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(dzqService.getPlatBaseLineAmtPerData(paraData));
    }

    /**
     * 省有基地无 金额排名 Column (电子券平台数据不一致)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getPlatPrvdColumnAmtData", produces = "text/json;charset=UTF-8")
    public String getPlatPrvdColumnAmtData(ParameterData paraData) {
	return Json.Encode(dzqService.getPlatPrvdColumnAmtData(paraData));
    }

    /**
     * 省有基地无 金额排名 line (电子券平台数据不一致)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getPlatPrvdLineAmtData", produces = "text/json;charset=UTF-8")
    public String getPlatPrvdLineAmtData(HttpServletRequest request, ParameterData paraData) {
	paraData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(dzqService.getPlatPrvdLineAmtData(paraData));
    }

    /**
     * 省有基地无 金额占比排名 Column (电子券平台数据不一致)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getPlatPrvdColumnAmtPerData", produces = "text/json;charset=UTF-8")
    public String getPlatPrvdColumnAmtPerData(ParameterData paraData) {
	return Json.Encode(dzqService.getPlatPrvdColumnAmtPerData(paraData));
    }

    /**
     * 省有基地无 金额占比排名 line (电子券平台数据不一致)
     * 
     * @param paraData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqsjjg })
    @ResponseBody
    @RequestMapping(value = "/getPlatPrvdLineAmtPerData", produces = "text/json;charset=UTF-8")
    public String getPlatPrvdLineAmtPerData(HttpServletRequest request, ParameterData paraData) {
	paraData.setSwitchState(getSwitchStateByDepId(request));
	return Json.Encode(dzqService.getPlatPrvdLineAmtPerData(paraData));
    }

    /**
     * 排名汇总 （平台不一致）table
     * 
     * @param parameterData
     * @return
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.dzqpmhz })
    @ResponseBody
    @RequestMapping(value = "/getPlatRankTable", produces = "text/json;charset=UTF-8")
    public String getPlatRankTable(ParameterData parameterData) {
	return Json.Encode(dzqService.getPlatRankTable(parameterData));
    }

}
