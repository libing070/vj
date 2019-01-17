package com.hpe.cmca.controller;

import java.util.ArrayList;
/**
 * 渠道养卡
 */
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
import com.hpe.cmca.pojo.QdykData;
import com.hpe.cmca.service.QdykService;
import com.hpe.cmca.util.Json;

@Controller
@RequestMapping("/yktl")
public class QDYKController extends BaseController {

	@Autowired
	protected QdykService qdykService;
	@Autowired
	protected ZgwzGenFileProcessor zgwzGenFileProcessor;

	@CmcaAuthority(authorityTypes = { AuthorityType.qdyksjjg, AuthorityType.qdykpmhz, AuthorityType.qdykqtcd,
			AuthorityType.qdyksjzl, AuthorityType.qdykwjxz, AuthorityType.qdykzgwz })
	@RequestMapping(value = "index")
	public String index() {
		return "yktl/index";
	}

	/**
	 * 养卡号码占比排名（柱形图）
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * 2017-7-12 下午5:02:15
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdyksjjg })
	@ResponseBody
	@RequestMapping(value = "getCardPercentPm", produces = "text/json; charset=UTF-8")
	public String getCardPercentPm(ParameterData qdyk) {
		Map<String, Object> data = qdykService.getCardPercentPm(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 养卡号码数量排名（柱形图）
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * 2017-7-14 下午5:04:09
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdyksjjg })
	@ResponseBody
	@RequestMapping(value = "getCardNumbersPm", produces = "text/json; charset=UTF-8")
	public String getCardNumbersPm(ParameterData qdyk) {
		Map<String, Object> data = qdykService.getCardNumbersPm(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 养卡渠道数量排名（柱形图）
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * 2017-7-14 下午5:04:09
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdyksjjg })
	@ResponseBody
	@RequestMapping(value = "getChnlNumbersPm", produces = "text/json; charset=UTF-8")
	public String getChnlNumbersPm(ParameterData qdyk) {
		Map<String, Object> data = qdykService.getChnlNumbersPm(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 养卡渠道占比排名（柱形图）
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * 2017-7-14 下午5:04:09
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdyksjjg })
	@ResponseBody
	@RequestMapping(value = "getChnlPercentPm", produces = "text/json; charset=UTF-8")
	public String getChnlPercentPm(ParameterData qdyk) {
		Map<String, Object> data = qdykService.getChnlPercentPm(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 养卡号码个数趋势（折线图）
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdyksjjg })
	@ResponseBody
	@RequestMapping(value = "getCardNumPmLine", produces = "text/json; charset=UTF-8")
	public String getCardNumPmLine(HttpServletRequest request, ParameterData qdyk) {
		qdyk.setSwitchState(getSwitchStateByDepId(request));
		Map<String, Object> data = qdykService.getCardNumPmLine(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 养卡渠道数量趋势（折线图）
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * 2017-7-14 下午5:04:09
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdyksjjg })
	@ResponseBody
	@RequestMapping(value = "getChnlNumsPmLine", produces = "text/json; charset=UTF-8")
	public String getChnlNumsPmLine(HttpServletRequest request, ParameterData qdyk) {
		qdyk.setSwitchState(getSwitchStateByDepId(request));
		Map<String, Object> data = qdykService.getChnlNumsPmLine(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 养卡号码占比趋势（折线图）
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * 2017-7-14 下午5:04:09
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdyksjjg })
	@ResponseBody
	@RequestMapping(value = "getCardPercentPmLine", produces = "text/json; charset=UTF-8")
	public String getCardPercentPmLine(HttpServletRequest request, ParameterData qdyk) {
		qdyk.setSwitchState(getSwitchStateByDepId(request));
		Map<String, Object> data = qdykService.getCardPercentPmLine(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 养卡渠道占比趋势（折线图）
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * 2017-7-14 下午5:04:09
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdyksjjg })
	@ResponseBody
	@RequestMapping(value = "getChanlPercentPmLine", produces = "text/json; charset=UTF-8")
	public String getChanlPercentPmLine(HttpServletRequest request, ParameterData qdyk) {
		qdyk.setSwitchState(getSwitchStateByDepId(request));
		Map<String, Object> data = qdykService.getChanlPercentPmLine(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 风险地图
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * 2017-7-14 下午5:04:09
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdyksjjg })
	@ResponseBody
	@RequestMapping(value = "getQdykMap", produces = "text/json; charset=UTF-8")
	public String getQdykMap(ParameterData qdyk) {
		Map<Integer, QdykData> data = qdykService.getQdykMap(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 风险地图下方图片
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * 2017-7-14 下午5:04:09
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdyksjjg })
	@ResponseBody
	@RequestMapping(value = "getQdykMapBottom", produces = "text/json; charset=UTF-8")
	public String getQdykMapBottom(ParameterData qdyk) {
		Map<Object,Object> map = new HashMap<>();
		if (qdyk.getAudTrm() == null || "".equals(qdyk.getAudTrm())) {
			return Json.Encode(map);
		}
		Map<Integer, QdykData> data = qdykService.getQdykMapBottom(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 养卡地市渠道信息表格
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * 2017-7-14 下午5:04:09
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdyksjjg })
	@ResponseBody
	@RequestMapping(value = "getQdykChnlTable", produces = "text/json; charset=UTF-8")
	public String getQdykChnlTable(ParameterData qdyk) {
		Map<String, Object> data = qdykService.getQdykChnlTable(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 养卡渠道基本信息
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * 2017-7-14 下午5:04:09
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdyksjjg })
	@ResponseBody
	@RequestMapping(value = "getQdykChnlBaseInfo", produces = "text/json; charset=UTF-8")
	public String getQdykChnlBaseInfo(ParameterData qdyk) {
		Map<String, Object> data = qdykService.getQdykChnlBaseInfo(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 渠道养卡趋势
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * 2017-7-14 下午5:04:09
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdyksjjg })
	@ResponseBody
	@RequestMapping(value = "getQdykChnlTrend", produces = "text/json; charset=UTF-8")
	public String getQdykChnlTrend(HttpServletRequest request, ParameterData qdyk) {
		qdyk.setSwitchState(getSwitchStateByDepId(request));
		Map<String, Object> data = qdykService.getQdykChnlTrend(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 渠道养卡排名汇总
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdykpmhz })
	@ResponseBody
	@RequestMapping(value = "getQdykDataPm", produces = "text/json; charset=UTF-8")
	public String getQdykDataPm(ParameterData qdyk) {
		Map<String, Object> data = qdykService.getQdykDataPm(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 增量分析
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdykqtcd })
	@ResponseBody
	@RequestMapping(value = "getIncrementalData", produces = "text/json; charset=UTF-8")
	public String getIncrementalData(ParameterData qdyk) {
		List<Object> data = qdykService.getIncrementalData(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 养卡渠道类型（饼图）
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdykqtcd })
	@ResponseBody
	@RequestMapping(value = "getQDYKqdlxData", produces = "text/json; charset=UTF-8")
	public String getQDYKqdlxData(ParameterData qdyk) {
		Map<String, Object> data = qdykService.getQDYKqdlxData(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 养卡渠道类型(堆积图)
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;author issuser
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdykqtcd })
	@ResponseBody
	@RequestMapping(value = "getQDYKqdlxDuiData", produces = "text/json; charset=UTF-8")
	public String getQDYKqdlxDuiData(HttpServletRequest request, ParameterData qdyk) {
		if(qdyk.getAudTrm()==null||"".equals(qdyk.getAudTrm())){
			Map<Object,Object> a = new HashMap<>();
			return Json.Encode(a);
		}
		qdyk.setSwitchState(getSwitchStateByDepId(request));
		Map<String, Object> data = qdykService.getQDYKqdlxDuiData(qdyk);
		return Json.Encode(data);

	}

	/**
	 * 整改问责要求（养卡，有价卡，终端）
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;throws Exception
	 * &#64;author issuser
	 * 2017-8-30 上午10:49:36
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdykzgwz })
	@ResponseBody
	@RequestMapping(value = "getQDYKZgwzData", produces = "text/json; charset=UTF-8")
	public String getQDYKZgwzData(ParameterData qdyk) throws Exception {
		List<Map<String, Object>> zgwzlist = zgwzGenFileProcessor.generateQdyk(qdyk.getAudTrm(), qdyk.getPrvdId(),
				qdyk.getConcern());
		return Json.Encode(zgwzlist);

	}

	/**
	 * 养卡整改问责统计（6个月整改标准次数排名）
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;throws Exception
	 * &#64;author issuser
	 * 2017-8-30 上午10:51:15
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdykzgwz })
	@ResponseBody
	@RequestMapping(value = "getQDYKZgwzTjSixMonth", produces = "text/json; charset=UTF-8")
	public String getQDYKZgwzTjSixMonth(ParameterData qdyk) throws Exception {
		Map<String, Object> map = qdykService.getQDYKZgwzTjSixMonth(qdyk);
		return Json.Encode(map);

	}

	/**
	 * 养卡整改问责统计（累计达到整改次数排名，包括问责--切换）
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;throws Exception
	 * &#64;author issuser
	 * 2017-8-30 上午10:51:15
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdykzgwz })
	@ResponseBody
	@RequestMapping(value = "getQDYKZgwzTj", produces = "text/json; charset=UTF-8")
	public String getQDYKZgwzTj(ParameterData qdyk) throws Exception {
		Map<String, Object> map = qdykService.getQDYKZgwzTj(qdyk);
		return Json.Encode(map);

	}

	/**
	 * 养卡整改问责统计（达到整改标准的省公司数量趋势--折线图）
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;throws Exception
	 * &#64;author issuser
	 * 2017-8-30 上午10:51:15
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdykzgwz })
	@ResponseBody
	@RequestMapping(value = "getQDYKSjzgTjLine", produces = "text/json; charset=UTF-8")
	public String getQDYKSjzgTjLine(HttpServletRequest request, ParameterData qdyk) throws Exception {
		qdyk.setSwitchState(getSwitchStateByDepId(request));
		Map<String, Object> map = qdykService.getQDYKSjzgTjLine(qdyk);
		return Json.Encode(map);

	}

	/**
	 * 养卡 审计报告（文本）
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;throws Exception
	 * &#64;author issuser
	 * 2017-8-30 上午10:51:15
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdykwjxz })
	@ResponseBody
	@RequestMapping(value = "getQDYKReportText", produces = "text/json; charset=UTF-8")
	public String getQDYKReportText(HttpServletRequest request, ParameterData qdyk) throws Exception {
		if (getAttributeByAudTrmAndUser(request, qdyk.getAudTrm(), "2")) {
			return Json.Encode(qdykService.getQDYKReportText(qdyk));
		} else {
			Map<String, Object> map = new HashMap<String, Object>(1);
			map.put("switchState", "audTrmColseForReport");
			return Json.Encode(map);
		}

	}

	/**
	 * 审计报告 表格
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;throws Exception
	 * &#64;author issuser
	 * 2017-9-1 下午5:27:30
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdykwjxz })
	@ResponseBody
	@RequestMapping(value = "getQDYKReportTable", produces = "text/json; charset=UTF-8")
	public String getQDYKReportTable(ParameterData qdyk) throws Exception {
		Map<String, Object> map = qdykService.getQDYKReportTable(qdyk);
		return Json.Encode(map);

	}

	/**
	 * 养卡 重点关注渠道
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;throws Exception
	 * &#64;author issuser
	 * 2017-9-1 下午5:28:17
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdykqtcd })
	@ResponseBody
	@RequestMapping(value = "getQDYKConcernChnl", produces = "text/json; charset=UTF-8")
	public String getQDYKConcernChnl(ParameterData qdyk) throws Exception {
		if(qdyk.getAudTrm()==null||"".equals(qdyk.getAudTrm())){
			List<Object> list = new ArrayList<>();
			Map<Object,Object> map = new HashMap<>();
			return Json.Encode(map);
		}
		Map<String, Object> map = qdykService.getQDYKConcernChnl(qdyk);
		return Json.Encode(map);

	}

	/**
	 * 养卡 重点关注营销案
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;throws Exception
	 * &#64;author issuser
	 * 2017-9-1 下午5:28:34
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdykqtcd })
	@ResponseBody
	@RequestMapping(value = "getQDYKConcernOffer", produces = "text/json; charset=UTF-8")
	public String getQDYKConcernOffer(ParameterData qdyk) throws Exception {
		if(qdyk.getAudTrm()==null||"".equals(qdyk.getAudTrm())){
			Map<Object,Object> a = new HashMap<>();
			return Json.Encode(a);
			
		}
		Map<String, Object> map = qdykService.getQDYKConcernOffer(qdyk);
		return Json.Encode(map);

	}

	/**
	 * 养卡 重点关注渠道 下钻趋势图
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;throws Exception
	 * &#64;author issuser
	 * 2017-9-1 下午5:28:34
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdykqtcd })
	@ResponseBody
	@RequestMapping(value = "getQdykFocusChnlTrend", produces = "text/json; charset=UTF-8")
	public String getQdykFocusChnlTrend(HttpServletRequest request, ParameterData qdyk) throws Exception {
		qdyk.setSwitchState(getSwitchStateByDepId(request));
		Map<String, Object> map = qdykService.getQdykFocusChnlTrend(qdyk);
		return Json.Encode(map);

	}

	/**
	 * 养卡 重点关注营销案 下钻趋势图
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param qdyk
	 * &#64;return
	 * &#64;throws Exception
	 * &#64;author issuser
	 * 2017-9-1 下午5:28:34
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.qdykqtcd })
	@ResponseBody
	@RequestMapping(value = "getQdykFocusOfferTrend", produces = "text/json; charset=UTF-8")
	public String getQdykFocusOfferTrend(HttpServletRequest request, ParameterData qdyk) throws Exception {
		qdyk.setSwitchState(getSwitchStateByDepId(request));
		Map<String, Object> map = qdykService.getQdykFocusOfferTrend(qdyk);
		return Json.Encode(map);

	}
}
