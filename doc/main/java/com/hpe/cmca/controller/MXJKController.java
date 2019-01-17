package com.hpe.cmca.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.common.CommonResult;
import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.util.CalendarUtils;
import com.hpe.cmca.util.Json;
import com.hpe.cmca.web.taglib.Pager;

/**
 * 
 * <pre>
 * Desc： 模型监控
 * @author GuoXY
 * @refactor GuoXY
 * @date   Nov 30, 2016 6:05:26 PM
 * @version 1.0
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Nov 30, 2016 	   GuoXY 	         1. Created this class.
 * </pre>
 */
@Controller
@RequestMapping("/mxjk")
public class MXJKController extends BaseController {

    /**
     * 
     * <pre>
     * Desc  模型监控首页
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   Oct 26, 2016 3:19:33 PM
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.mxjk })
    // 判断是否具有审计报告权限
    @RequestMapping(value = "index")
    public String index() {
	return "mxjk/index";
    }

    /**
     * 
     * <pre>
     * Desc  根据权限设置prvdIds、isQuanguo，查询公司选择列表框范围，查询审计年列表框范围
     * @param request
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   Oct 29, 2016 10:09:20 AM
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.mxjk })
    @ResponseBody
    @RequestMapping(value = "queryDefaultParams", produces = "plain/text; charset=UTF-8")
    public String queryDefaultParams(HttpServletRequest request) {
	Map<String, Object> result = new HashMap<String, Object>();
	Map<String, Object> params = new HashMap<String, Object>();
	Date nowDate = new Date();
	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH:mm:ss");
	String now = df.format(nowDate);
	String date = now.substring(0, 6);// 年月
	// TODO 根据权限进行判断集团还是省公司
	String prvdIds = request.getParameter("prvdIds");
	// 20161130 add by GuoXY for prvdIds中包含逗号说明是集团用户选中了多个省时，点击了审计报告下载
	if ("10000".equals(prvdIds) || prvdIds == null || "".equals(prvdIds) || prvdIds.contains(",")) {
	    result.put("isQuanguo", true);
	    result.put("prvdIds", "10000");
	    params.put("prvdIdStr", "10000");
	} else {
	    // TODO 获取人的所属省id
	    result.put("isQuanguo", false);
	    result.put("prvdIds", prvdIds);
	    params.put("prvdIdStr", prvdIds);
	}
	result.put("selYear", date.substring(0, 4));
	result.put("selMonth", date.substring(4, 6));
	// 返回1：公司列表
	List<Map<String, Object>> prvdList = mybatisDao.getList("commonMapper.getProvIdName", params);

	// 返回2：所有专题公共的年份列表
	// List<String> auditYearList = mybatisDao.getList("commonMapper.getAuditYear");

	result.put("prvdList", prvdList);
	// result.put("auditYearList", auditYearList);

	logger.debug(result);

	CommonResult commonResult = new CommonResult();
	commonResult.setStatus("0");
	commonResult.setBody(result);
	return Json.Encode(commonResult);
    }

    /**
     * 获取"文件加载"柱形图数据
     * 
     * @param audTrm
     *            : 审计月
     * @param subjectId
     *            : 专题ID
     * @param fileLoadTab
     *            : 当前图形对应的自己的tab页（查询数据的条件）
     * @return
     * @author GuoXY
     * @date 20161217
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.mxjk })
    @ResponseBody
    @RequestMapping(value = "getWJJZColumnChartData", produces = "plain/text; charset=UTF-8")
    public String getWJJZColumnChartData(String audTrm, String subjectId, int fileLoadTab) {
	// 第一步：根据参数，获取所有数据
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("audTrm", audTrm);// "201512"
	params.put("subjectId", subjectId);// "1"
	params.put("fileLoadTab", fileLoadTab);
	List<Map<String, Object>> list = mybatisDao.getList("mxjk.fileLoadColumnData", params);

	// 第二步：按接口规范定义所有图形所需参数对象及其结构
	Map<String, Object> result = new HashMap<String, Object>();
	List<String> categories = new ArrayList<String>();
	List<Integer> series = new ArrayList<Integer>();

	// 第三步：填充图形对象数据
	// 遍历柱图中已经排序的数据，对于数据库中查不到的数据填充为0
	for (Map<String, Object> rec : list) {
	    categories.add((String) rec.get("shortName"));
	    series.add((Integer) rec.get("num"));
	}

	// 第四步：将图形所需参数对象按接口定义结构组装
	result.put("xAxisCategories", categories);
	result.put("seriesData", series);
	// System.out.println("#########" + CommonResult.success(result));
	return CommonResult.success(result);
    }

    /**
     * 获取"模型执行"圆形图数据
     * 
     * @param audTrm
     *            : 审计月
     * @param subjectId
     *            : 专题ID
     * @param modelExecTab
     *            : 当前图形对应的自己的tab页（查询数据的条件）
     * @return
     * @author GuoXY
     * @date 20161214
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.mxjk })
    @ResponseBody
    @RequestMapping(value = "getMXZXRoundChartData", produces = "plain/text; charset=UTF-8")
    public String getMXZXRoundChartData(String audTrm, String subjectId, int modelExecTab) {
	// 第一步：根据参数，获取所有数据
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("audTrm", audTrm);// "201601"
	params.put("subjectId", subjectId);// "1"
	params.put("modelExecTab", modelExecTab);

	List<Map<String, Object>> list = mybatisDao.getList("mxjk.modelExecRoundData", params);
	Map<String, Object> rec = list.get(0);

	// 第二步：按接口规范(图形所需要的数据格式)定义所有图形所需参数对象及其结构
	Map<String, Object> result = new HashMap<String, Object>();
	List<Object> series = null;

	// 第三步：校验 如果数据都是0，就不展示饼图了
	if (0 == modelExecTab) { // 总体执行情况
	    if (new Integer(0).equals((Integer) rec.get("sucCount")) && new Integer(0).equals((Integer) rec.get("runCount")) && new Integer(0).equals((Integer) rec.get("errorCount"))
		    && new Integer(0).equals((Integer) rec.get("unfinishCount"))) {
		return CommonResult.success(result);
	    }

	} else if (1 == modelExecTab) { // 时长数环比变化>30%
	    if (new Integer(0).equals((Integer) rec.get("execSuccess")) && new Integer(0).equals((Integer) rec.get("execFailed"))) {
		return CommonResult.success(result);
	    }
	}
	// 第四步：填充图形对象数据（注意：官网上案例提供的数据格式可能并不是唯一的输入格式，用百度多搜搜其他应用案例也许就恍然大悟了）
	series = fillDataForMXZX(rec, modelExecTab);

	// 第五步：将图形所需参数对象按接口定义结构组装
	result.put("seriesData", series);
	// System.out.println("#########" + CommonResult.success(result));
	return CommonResult.success(result);
    }

    /**
     * 获取"结果展示"表格数据
     * 
     * @param audTrm
     *            : 审计月
     * @param prvdIds
     *            : 省公司列表值
     * @param ctyIds
     *            : 市公司列表值
     * @param chnlTyp
     *            : 渠道类型
     * @return
     * @author GuoXY
     * @date 20160914
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.mxjk })
    @ResponseBody
    @RequestMapping(value = "getJGZSTableData", produces = "plain/text; charset=UTF-8")
    public String getJGZSTableData(String audTrm, String subjectId) {
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	Map<String, Object> param = new HashMap<String, Object>();
	param.put("year", audTrm);
	param.put("subjectCode", subjectId);
	list = mybatisDao.getList("sjkg.querySwitchs_year", param);
	for (Map<String, Object> rec : list) {
	    String[] times = new String[] {};
	    times = rec.get("Opr_time").toString().split("-");
	    String oTime = times[0] + "年" + times[1] + "月" + times[2].substring(0, 2) + "日";
	    rec.put("Opr_time", oTime);
	    if (0 == (Integer) rec.get("Switch")) {
		rec.put("Switch", "<button class='off'></button>");
	    } else if (1 == (Integer) rec.get("Switch")) {
		rec.put("Switch", "<button class='on'></button>");
	    }
	}
	return Json.Encode(list);
    }

    /**
     * 获取"详单抽取"柱型图数据
     * 
     * @param audTrm
     *            : 审计月
     * @param subjectId
     *            : 专题ID
     * @param modelExecTab
     *            : 当前图形对应的自己的tab页（查询数据的条件）
     * @return
     * @author GuoXY
     * @date 20160914
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.mxjk })
    @ResponseBody
    @RequestMapping(value = "getXDCQColumnChartData", produces = "plain/text; charset=UTF-8")
    public String getXDCQColumnChartData(String audTrm, String subjectId, int detailExtractTab) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("audTrm", audTrm);// "201605"
	params.put("subjectId", subjectId);// 2
	// params.put("detailExtractTab", detailExtractTab);
	logger.debug(params);

	Map<String, Object> result = new HashMap<String, Object>();

	if (0 == detailExtractTab) {
	    List<Map<String, Object>> finishList = mybatisDao.getList("mxjk.detailExtractTotalFinish", params);
	    List<Map<String, Object>> ingList = mybatisDao.getList("mxjk.detailExtractTotalIng", params);
	    List<Map<String, Object>> readyList = mybatisDao.getList("mxjk.detailExtractTotalReady", params);

	    result = fillDataForXDCQ(finishList, ingList, readyList);
	} else if (1 == detailExtractTab) {
	    List<Map<String, Object>> normalList = mybatisDao.getList("mxjk.detailExtractExecNormal", params);
	    List<Map<String, Object>> exceptionList = mybatisDao.getList("mxjk.detailExtractExecException", params);

	    result = fillDataForXDCQ(normalList, exceptionList, null);
	}

	// logger.debug(result);
	System.out.println("#########" + CommonResult.success(result));

	return CommonResult.success(result);
    }

    /**
     * 报表展示弹出框 - jqgrid分页查询数据
     * 
     * <pre>
     * Desc  
     * @param request
     * @param pager
     * @param subjectId
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   Dec 14, 2016 3:05:52 PM
     * </pre>
     */
    @RequestMapping(value = "getBBZSDetailData")
    @ResponseBody
    public Pager getBBZSDetailData(HttpServletRequest request, Pager pager, String subjectId) {

	pager.setOrderBy("threshold_id");
	// 切换查询专题，将page还原成默认值
	// if (!subjectId.equals(curSubjectId)) {
	// pager.clear();
	// curSubjectId = subjectId;
	// }
	List<Map<String, Object>> paramList = null;
	try {
	    // paramList = modelparamService.getModelParamList(pager, subjectId, null);
	} catch (Exception e) {

	    this.logger.error("getModelParamList这个service出现异常");
	    e.printStackTrace();
	    logger.error(e.getMessage(),e);
	}
	pager.setResultList(paramList);
	return pager;
    }

    /**
     * 
     * <pre>
     * Desc  根据请求数据的类型构造"模型执行"数据
     * @param rec
     * @param modelExecTab
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   Dec 17, 2016 1:50:57 PM
     * </pre>
     */
    List<Object> fillDataForMXZX(Map<String, Object> rec, int modelExecTab) {
	Map<String, Object> elementDataMap = null;
	List<Object> series = new ArrayList<Object>();

	if (0 == modelExecTab) { // 总体执行情况
	    // 完成
	    elementDataMap = new HashMap<String, Object>();
	    elementDataMap.put("name", "完成");
	    elementDataMap.put("color", "#17C554");
	    elementDataMap.put("y", (Integer) rec.get("sucCount"));
	    series.add(elementDataMap);
	    // 执行中
	    elementDataMap = new HashMap<String, Object>();
	    elementDataMap.put("name", "执行中");
	    elementDataMap.put("color", "#02C5F6");
	    elementDataMap.put("y", (Integer) rec.get("runCount"));
	    series.add(elementDataMap);
	    // 执行异常
	    elementDataMap = new HashMap<String, Object>();
	    elementDataMap.put("name", "执行异常");
	    elementDataMap.put("color", "#EB217A");
	    elementDataMap.put("y", (Integer) rec.get("errorCount"));
	    series.add(elementDataMap);
	    // 未开始
	    elementDataMap = new HashMap<String, Object>();
	    elementDataMap.put("name", "未开始");
	    elementDataMap.put("color", "#F8F006");
	    elementDataMap.put("y", (Integer) rec.get("unfinishCount"));
	    series.add(elementDataMap);

	} else if (1 == modelExecTab) { // 时长数环比变化>30%
	    elementDataMap = new HashMap<String, Object>();
	    elementDataMap.put("name", "执行正常");
	    elementDataMap.put("color", "#4F1FCB");
	    elementDataMap.put("y", (Integer) rec.get("execSuccess"));
	    series.add(elementDataMap);

	    elementDataMap = new HashMap<String, Object>();
	    elementDataMap.put("name", "执行异常");
	    elementDataMap.put("color", "#D4317A");
	    elementDataMap.put("y", (Integer) rec.get("execFailed"));
	    series.add(elementDataMap);
	}
	return series;
    }

    /**
     * 
     * <pre>
     * Desc  根据请求数据的类型构造"详单抽取"数据
     * @param list1: 完成_执行正常  （这是一个包含了31个省记录的list，提供了各省数据在图中出现的顺序，对于无数据的省已经在SQL中设置了结果0）
     * @param list2: 执行中_执行异常
     * @param list3: 未开始_null
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   Dec 17, 2016 4:01:00 PM
     * </pre>
     */
    Map<String, Object> fillDataForXDCQ(List<Map<String, Object>> list1, List<Map<String, Object>> list2, List<Map<String, Object>> list3) {
	Map<String, Object> result = new HashMap<String, Object>();
	List<String> categories = new ArrayList<String>();
	List<Object> series = new ArrayList<Object>();

	// chart图的series中的一个子元素结构
	Map<String, Object> elementDataMap = null;
	List<Integer> listData1 = new ArrayList<Integer>();
	List<Integer> listData2 = new ArrayList<Integer>();
	List<Integer> listData3 = new ArrayList<Integer>();

	if (null != list3) { // 详单总数
	    Map<String, Object> obj = null;
	    for (Map<String, Object> prov : list1) {
		categories.add((String) prov.get("shortName"));
		listData1.add((Integer) prov.get("num"));
		obj = findObjByKey(list2, "prvd_id", (Integer) prov.get("id"));
		listData2.add(null != obj ? (Integer) obj.get("num") : 0);
		obj = findObjByKey(list3, "prvd_id", (Integer) prov.get("id"));
		listData3.add(null != obj ? (Integer) obj.get("num") : 0);
	    }
	    // 完成
	    elementDataMap = new HashMap<String, Object>();
	    elementDataMap.put("name", "完成");
	    elementDataMap.put("color", "#17C554");
	    elementDataMap.put("data", listData1);
	    series.add(elementDataMap);
	    // 执行中
	    elementDataMap = new HashMap<String, Object>();
	    elementDataMap.put("name", "执行中");
	    elementDataMap.put("color", "#02C5F6");
	    elementDataMap.put("data", listData2);
	    series.add(elementDataMap);
	    // 未开始
	    elementDataMap = new HashMap<String, Object>();
	    elementDataMap.put("name", "未开始");
	    elementDataMap.put("color", "#F8F006");
	    elementDataMap.put("data", listData3);

	    series.add(elementDataMap);
	} else if (null == list3) { // 执行时间差>24h
	    Map<String, Object> obj = null;
	    for (Map<String, Object> prov : list1) {
		categories.add((String) prov.get("shortName"));
		listData1.add((Integer) prov.get("num"));
		obj = findObjByKey(list2, "prvd_id", (Integer) prov.get("id"));
		listData2.add(null != obj ? (Integer) obj.get("num") : 0);
	    }
	    elementDataMap = new HashMap<String, Object>();
	    elementDataMap.put("name", "执行正常");
	    elementDataMap.put("color", "#4F1FCB");
	    elementDataMap.put("data", listData1);
	    series.add(elementDataMap);

	    elementDataMap = new HashMap<String, Object>();
	    elementDataMap.put("name", "执行异常");
	    elementDataMap.put("color", "#D4317A");
	    elementDataMap.put("data", listData2);

	    series.add(elementDataMap);
	}
	result.put("categories", categories);
	result.put("seriesData", series);
	return result;
    }

    /**
     * 
     * <pre>
     * Desc  遍历（数据库）结果集，获取记录指定的字段跟参数key比较，如果，相等就返回记录
     * @param list
     * @param colName
     * @param key
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   Dec 18, 2016 9:10:54 AM
     * </pre>
     */
    Map<String, Object> findObjByKey(List<Map<String, Object>> list, String colName, Integer key) {
	for (Map<String, Object> prov : list) {
	    if (((Integer) prov.get(colName)).equals(key))
		return prov;
	}
	return null;
    }

    /**
     * 
     * <pre>
     * Desc  获取"文件加载 - 报表展示"数据
     * @param audTrm
     * @param subjectId
     * @param prvdIds
     * @param focuscds
     * @param loadStatus
     * @param monitorType
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   Dec 18, 2016 1:11:40 PM
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.mxjk })
    @ResponseBody
    @RequestMapping(value = "getTableShowDataWJJZ", produces = "plain/text; charset=UTF-8")
    public String getTableShowDataWJJZ(String audTrm, String subjectId, Integer[] prvdIds, String focuscds, String loadStatus, String monitorType) {
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	Map<String, Object> param = new HashMap<String, Object>();
	param.put("audTrm", audTrm);// "201512"
	param.put("subjectId", subjectId);
	param.put("prvdIds", (0 != prvdIds.length) ? StringUtils.join(prvdIds, ",") : null);
	param.put("focuscds", focuscds);
	param.put("loadStatus", (null != loadStatus) && loadStatus.equals("all") ? null : loadStatus);
	param.put("monitorType", (null != monitorType) && monitorType.equals("all") ? null : monitorType);
	// System.out.println("\n\n:" +param+"\n\n");
	list = mybatisDao.getList("mxjk.fileLoadTableShow", param);

	return Json.Encode(list);
    }

    /**
     * 
     * <pre>
     * Desc  获取"模型执行 - 报表展示"数据
     * @param audTrm
     * @param subjectId
     * @param prvdIds
     * @param focuscds
     * @param loadStatus
     * @param monitorType: half-模型执行时长比上周期减半,double-模型执行时长比上周期翻倍,all-全部
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   Dec 18, 2016 1:11:40 PM
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.mxjk })
    @ResponseBody
    @RequestMapping(value = "getTableShowDataMXZX", produces = "plain/text; charset=UTF-8")
    public String getTableShowDataMXZX(String audTrm, String subjectId, Integer[] prvdIds, String focuscds, String loadStatus, String monitorType) {
	List<Map<String, Object>> querylist = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
	Map<String, Object> param = new HashMap<String, Object>();
	param.put("audTrm", audTrm);// "201512"
	param.put("subjectId", subjectId);
	param.put("prvdIds", (0 != prvdIds.length) ? StringUtils.join(prvdIds, ",") : null);
	param.put("focuscds", focuscds);
	param.put("loadStatus", (null != loadStatus) && loadStatus.equals("all") ? null : loadStatus);
	param.put("monitorType", (null != monitorType) && monitorType.equals("all") ? null : monitorType);

	querylist = mybatisDao.getList("mxjk.modelExecTableShow", param);
	for (Map<String, Object> rec : querylist) {
	    // 监控类型-过滤数据：: half-模型执行时长比上周期减半,double-模型执行时长比上周期翻倍,all-全部
	    if (monitorType.equals("half")) {
		if (((String) rec.get("isDoubleHalf")).equals("1")) {
		    resultList.add(rec);
		}
	    } else if (monitorType.equals("double")) {
		if (((String) rec.get("isDoubleHalf")).equals("2")) {
		    resultList.add(rec);
		}
	    } else {
		resultList.add(rec);
	    }
	}
	return Json.Encode(resultList);
    }

    /**
     * 
     * <pre>
     * Desc  获取"详单抽取 - 报表展示"数据
     * @param audTrm
     * @param subjectId
     * @param prvdIds
     * @param focuscds
     * @param loadStatus
     * @param monitorType: half-详单抽取时长比上周期减半,double-详单抽取时长比上周期翻倍,all-全部
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   Dec 18, 2016 1:11:40 PM
     * </pre>
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.mxjk })
    @ResponseBody
    @RequestMapping(value = "getTableShowDataXDCQ", produces = "plain/text; charset=UTF-8")
    public String getTableShowDataXDCQ(String audTrm, String subjectId, Integer[] prvdIds, String focuscds, String loadStatus, String monitorType) {
	List<Map<String, Object>> querylist = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
	Map<String, Object> param = new HashMap<String, Object>();
	param.put("audTrm", audTrm);
	param.put("subjectId", subjectId);
	param.put("prvdIds", (0 != prvdIds.length) ? StringUtils.join(prvdIds, ",") : null);
	param.put("focuscds", focuscds);
	param.put("loadStatus", (null != loadStatus) && loadStatus.equals("all") ? null : loadStatus);
	param.put("monitorType", (null != monitorType) && monitorType.equals("all") ? null : monitorType);

	querylist = mybatisDao.getList("mxjk.detailExtractTableShow", param);
//	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	for (Map<String, Object> rec : querylist) {
	    // 本月
	    Date modelFinTime = (Date) rec.get("model_fin_time");
	    Date docFileFtpTime = (Date) rec.get("doc_file_ftp_time");
	    Date csvFileGenTime = (Date) rec.get("csv_file_gen_time");
	    Long resExecDuration = CalendarUtils.getTimeDiffBySec(modelFinTime, docFileFtpTime);
	    Long detailExttDuration = CalendarUtils.getTimeDiffBySec(modelFinTime, csvFileGenTime);
	    // 报告和审计结果执行时间差
	    rec.put("resExecDuration", null == resExecDuration ? "N/A" : resExecDuration);
	    // 详单抽取执行时长
	    rec.put("detailExttDuration", null == detailExttDuration ? "N/A" : detailExttDuration);

	    // 上月
	    Date lmodelFinTime = (Date) rec.get("last_model_fin_time");
	    Date lcsvFileGenTime = (Date) rec.get("last_csv_file_gen_time");
	    Long ldetailExttDuration = CalendarUtils.getTimeDiffBySec(lmodelFinTime, lcsvFileGenTime);

	    // 监控类型-过滤数据：: half-详单抽取时长比上周期减半,double-详单抽取时长比上周期翻倍,all-全部
	    if (monitorType.equals("half")) {
		if(ldetailExttDuration!=null)
		if ((detailExttDuration * 2 - ldetailExttDuration) < 0) {
		    resultList.add(rec);
		}
	    } else if (monitorType.equals("double")) {
		if(ldetailExttDuration!=null)
		if ((detailExttDuration / 2 - ldetailExttDuration) > 0) {
		    resultList.add(rec);
		}
	    } else {
		resultList.add(rec);
	    }
	}
	return Json.Encode(resultList);
    }

}
