/**
 * com.hpe.cmca.controller.YWYJController.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.pojo.AssemblyInfoData;
import com.hpe.cmca.pojo.MonitorPointsConfigData;
import com.hpe.cmca.pojo.YWYJParameterData;
import com.hpe.cmca.service.YwyjService;
import com.hpe.cmca.util.Json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Desc：
 * &#64;author   hufei
 * &#64;refactor hufei
 * &#64;date     2017-11-7 下午4:51:33
 * &#64;version  1.0
 *
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  2017-11-7 	   hufei 	         1. Created this class.
 * </pre>
 */
@Controller
@RequestMapping("/ywyj")
public class YWYJController extends BaseController {

	@Autowired
	protected YwyjService ywyjService;

	// 业务预警-获取审计月
	@ResponseBody
	@RequestMapping(value = "/queryMonth", produces = "text/json;charset=UTF-8")
	public String queryMonth(HttpServletRequest request, String lv2Code, String pointCode) {
		String authorityAttr = getAuthorityAttr(request);
		return Json.Encode(ywyjService.queryMonth(authorityAttr, lv2Code, pointCode));
	}

	// 业务预警-查询时-获取默认版本及组件ID信息
	@ResponseBody
	@RequestMapping(value = "/queryMonitorPointInfo", produces = "text/json;charset=UTF-8")
	public String queryMonitorPointInfo(String pointCode) {
		return Json.Encode(ywyjService.queryMonitorPointInfo(pointCode));
	}

	// 业务预警-组件渲染及获取包含的控件Ids
	@ResponseBody
	@RequestMapping(value = "/queryAssemblyInfo", produces = "text/json;charset=UTF-8")
	public String queryAssemblyInfo(AssemblyInfoData parameterData) {
		return Json.Encode(ywyjService.queryAssemblyInfo(parameterData));
	}

	// 业务预警-控件渲染
	@ResponseBody
	@RequestMapping(value = "/getControlInfo", produces = "text/json;charset=UTF-8")
	public String getControlInfo(HttpServletRequest request, YWYJParameterData parameterData) {
		if (parameterData.getAudTrm() == null || "".equals(parameterData.getAudTrm())) {
			Map<Object, Object> map = new HashMap<>();
			return Json.Encode(map);
		}
		String authorityAttr = getAuthorityAttr(request);
		return Json.Encode(ywyjService.getControlInfo(authorityAttr, parameterData));
	}

	// 业务预警-查询-获取版本列表
	@ResponseBody
	@RequestMapping(value = "/queryVersion", produces = "text/json;charset=UTF-8")
	public String queryVersion(HttpServletRequest request, String pointCode) {
		if (request.getSession().getAttribute("userName") == null) {
			request.getSession().setAttribute("userName", "hp_daiyp");
		}
		String createPerson = request.getSession().getAttribute("userName").toString();
		return Json.Encode(ywyjService.queryVersion(createPerson, pointCode));

	}

	// 业务预警-查询-根据版本获取组件信息
	@ResponseBody
	@RequestMapping(value = "/queryContainsAssembly", produces = "text/json;charset=UTF-8")
	public String queryContainsAssembly(String pointVersionId) {
		return Json.Encode(ywyjService.queryContainsAssembly(pointVersionId));

	}

	// 业务预警-新增版本
	@ResponseBody
	@RequestMapping(value = "/addVersion", produces = "text/json;charset=UTF-8")
	public String addVersion(HttpServletRequest request, String pointCode, String pointVersionId) {
		if (request.getSession().getAttribute("userName") == null)
			request.getSession().setAttribute("userName", "hp_daiyp");
		String createPerson = request.getSession().getAttribute("userName").toString();
		return Json.Encode(ywyjService.addVersion(createPerson, pointCode, pointVersionId));
	}

	// 业务预警-设置为默认版本
	@ResponseBody
	@RequestMapping(value = "/setDefaultVersion", produces = "text/json;charset=UTF-8")
	public String setDefaultVersion(MonitorPointsConfigData parameterData) {
		return Json.Encode(ywyjService.setDefaultVersion(parameterData));
	}

	// 业务预警-组件-新增组件
	@ResponseBody
	@RequestMapping(value = "/addAssemblyData", produces = "text/json;charset=UTF-8")
	public String addAssemblyData(MonitorPointsConfigData parameterData) {
		return Json.Encode(ywyjService.addAssemblyData(parameterData));
	}

	// 业务预警_组件_布局_保存
	@ResponseBody
	@RequestMapping(value = "/saveAssemblyInfo", produces = "text/json;charset=UTF-8")
	public String saveAssemblyInfo(AssemblyInfoData parameterData) {
		return Json.Encode(ywyjService.saveAssemblyInfo(parameterData));
	}

	// 业务预警-删除组件信息
	@ResponseBody
	@RequestMapping(value = "/deleteAssemblyInfo", produces = "text/json;charset=UTF-8")
	public void deleteAssemblyInfo(String pointVersionId, String assemblyId) {
		ywyjService.deleteAssemblyInfo(pointVersionId, assemblyId);
	}

	// 业务预警_控件_数据源表_列表
	@ResponseBody
	@RequestMapping(value = "/getTableInfo", produces = "text/json;charset=UTF-8")
	public String getTableInfo(YWYJParameterData parameterData) {
		return Json.Encode(ywyjService.getTableInfo(parameterData));
	}

	// 业务预警_控件_数据源表_表详情
	@ResponseBody
	@RequestMapping(value = "/getTableDetails", produces = "text/json;charset=UTF-8")
	public String getTableDetails(String tableName) {
		return Json.Encode(ywyjService.getTableDetails(tableName));
	}

	// 业务预警_控件_数据源表_保存数据
	@ResponseBody
	@RequestMapping(value = "/saveControlDataInfo", produces = "text/json;charset=UTF-8")
	public String saveControlDataInfo(YWYJParameterData parameterData) {
		return Json.Encode(ywyjService.saveControlDataInfo(parameterData));
	}

	// // 业务预警-监控点-默信息输出
	// @ResponseBody
	// @RequestMapping(value = "/getMonitorPointInfo", produces =
	// "text/json;charset=UTF-8")
	// public String getMonitorPointInfo(MonitorPointsConfigData parameterData)
	// {
	// return Json.Encode(ywyjService.getMonitorPointInfo(parameterData));
	// }

	/**
	 *
	 * <pre>
	 * Desc  获取当前一级流程下所有监控点的数据
	 * &#64;param request
	 * &#64;param level1ProcessCode  当前一级流程，不传该值代表登录人权限下的所有监控点
	 * &#64;param prvdId 当前省份 可写死10000
	 * &#64;return
	 * &#64;author sinly
	 * &#64;refactor sinly
	 * &#64;date   2017年12月22日 下午6:06:11
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "/getMonitorOverViewData", produces = "text/json;charset=UTF-8")
	public String getMonitorOverViewData(HttpServletRequest request, String level1ProcessCode, String prvdId) {
		HttpSession session = request.getSession();
		String authorityAttr = null;
		if (session.getAttribute("userId") == null)// 如果当前未登录则手动添加部门编码到session中
			session.setAttribute("depId", 187);

		Object depId = session.getAttribute("depId");
		if (Integer.parseInt(depId.toString()) == 18
				|| (Integer.parseInt(depId.toString()) <= 193 && Integer.parseInt(depId.toString()) >= 163)) {
			authorityAttr = "neishen";
		}
		if (Integer.parseInt(depId.toString()) == 12
				|| (Integer.parseInt(depId.toString()) <= 113 && Integer.parseInt(depId.toString()) >= 83)) {
			authorityAttr = "yezhi";
		}
		return Json.Encode(ywyjService.getMonitorOverViewData(authorityAttr, level1ProcessCode, prvdId));
	}

	/**
	 *
	 * <pre>
	 * Desc  获取各省当前一级流程下的所有监控点的带权重汇总信息
	 * &#64;param request
	 * &#64;param level1ProcessCode不传该值代表登录人权限下的所有监控点
	 * &#64;return
	 * &#64;author sinly
	 * &#64;refactor sinly
	 * &#64;date   2017年12月22日 下午6:10:45
	 * </pre>
	 */

	@ResponseBody
	@RequestMapping(value = "/getLevel1OverViewData", produces = "text/json;charset=UTF-8")
	public String getLevel1OverViewData(HttpServletRequest request, String level1ProcessCode) {
		HttpSession session = request.getSession();
		String authorityAttr = null;
		if (session.getAttribute("userId") == null)// 如果当前未登录则手动添加部门编码到session中
			session.setAttribute("depId", 10009);

		Object depId = session.getAttribute("depId");
		if (Integer.parseInt(depId.toString()) == 18
				|| (Integer.parseInt(depId.toString()) <= 193 && Integer.parseInt(depId.toString()) >= 163)) {
			authorityAttr = "neishen";
		}
		if (Integer.parseInt(depId.toString()) == 12
				|| (Integer.parseInt(depId.toString()) <= 113 && Integer.parseInt(depId.toString()) >= 83)) {
			authorityAttr = "yezhi";
		}

		return Json.Encode(ywyjService.getLevel1OverViewData(authorityAttr, level1ProcessCode));
	}

	// 获取监控点数据的月份列表
	@ResponseBody
	@RequestMapping(value = "/getMonitorOverViewMon", produces = "text/json;charset=UTF-8")
	public String getMonitorOverViewMon() {
		return Json.Encode(ywyjService.getMonitorOverViewMon());
	}

	@ResponseBody
	@RequestMapping(value = "/getRadarMap", produces = "text/json;charset=UTF-8")
	public String getRadarMap(HttpServletRequest request, String level1ProcessCode) {
		String authorityAttr = getAuthorityAttr(request);
		return Json.Encode(ywyjService.getRadarMap(authorityAttr, level1ProcessCode));
	}

	/**
	 *
	 * <pre>
	 * Desc 获取用户登录权限
	 * &#64;param request
	 * &#64;return
	 * &#64;author hufei
	 * 2017-12-27 下午8:27:21
	 * </pre>
	 */
	private String getAuthorityAttr(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String authorityAttr = null;
		if (session.getAttribute("userId") == null)// 如果当前未登录则手动添加部门编码到session中
			session.setAttribute("depId", 10009);

		Object depId = session.getAttribute("depId");
		if (Integer.parseInt(depId.toString()) == 18
				|| (Integer.parseInt(depId.toString()) <= 193 && Integer.parseInt(depId.toString()) >= 163)) {
			authorityAttr = "neishen";
		}
		if (Integer.parseInt(depId.toString()) == 12
				|| (Integer.parseInt(depId.toString()) <= 113 && Integer.parseInt(depId.toString()) >= 83)) {
			authorityAttr = "yezhi";
		}
		return authorityAttr;
	}

	// 获取排名汇总-源表列表
	@ResponseBody
	@RequestMapping(value = "/getRankTableInfo", produces = "text/json;charset=UTF-8")
	public String getRankTableInfo(String pointCode) {
		return Json.Encode(ywyjService.getRankTableInfo(pointCode));
	}

	// 获取排名汇总、清单的表头
	@ResponseBody
	@RequestMapping(value = "/getTableTitle", produces = "text/json;charset=UTF-8")
	public String getTableTitle(String pointCode, String tableName, String dataType) {
		return Json.Encode(ywyjService.getTableTitle(pointCode, tableName, dataType));
	}

	// 获取排名汇总、清单表数据
	@ResponseBody
	@RequestMapping(value = "/queryRankOrDetailTable", produces = "text/json;charset=UTF-8")
	public String queryRankTable(String pointCode, String tableName, String screenField, String audTrm, int prvdId,
			int pageNum, int pageSize, String dataType) {
		if(audTrm==null||"".equals(audTrm)){
			Map<Object,Object> map = new HashMap<>();
			return Json.Encode(map);
		}
		return Json.Encode(ywyjService.queryRankOrDetailTable(pointCode, tableName, screenField, audTrm, prvdId,
				pageNum, pageSize, dataType));
	}

	// 获取清单、排名汇总的筛选字段
	@ResponseBody
	@RequestMapping(value = "/getScreenField", produces = "text/json;charset=UTF-8")
	public String getDetailScreenField(String pointCode, String tableName, String dataType) {
		return Json.Encode(ywyjService.getScreenField(pointCode, tableName, dataType));
	}

	// // 获取清单的表数据
	// @ResponseBody
	// @RequestMapping(value = "/queryDetailTable", produces =
	// "text/json;charset=UTF-8")
	// public String queryDetailTable(String pointCode, String screenField,
	// String audTrm, int prvdId, int pageNum, int pageSize) {
	// return Json.Encode(ywyjService.queryDetailTable(pointCode, screenField,
	// audTrm, prvdId, pageNum, pageSize));
	// }
	// 下载的表数据
	@ResponseBody
	@RequestMapping(value = "/downFilePage", produces = "text/json;charset=UTF-8")
	public void downFilePage(HttpServletResponse response, HttpServletRequest request, String subjectId, String focusCd,
			String audTrm, String prvdId, String fileType) {
		ywyjService.downFilePage(response, request, subjectId, focusCd, audTrm, prvdId, fileType);
	}

	@ResponseBody
	@RequestMapping(value = "/getLv1Lv2PointInfo", produces = "text/json;charset=UTF-8")
	public String getLv1Lv2PointInfo(HttpServletRequest request) {
		String authorityAttr = getAuthorityAttr(request);
		return Json.Encode(ywyjService.getLv1Lv2PointInfo(authorityAttr));
	}

	@ResponseBody
	@RequestMapping(value = "/getDataView", produces = "text/json;charset=UTF-8")
	public String getDataView(HttpServletRequest request, String audTrm, String lv2Code) {
		String authorityAttr = getAuthorityAttr(request);
		return Json.Encode(ywyjService.getDataView(authorityAttr, audTrm, lv2Code));
	}

	@ResponseBody
	@RequestMapping(value = "/getPmhzView", produces = "text/json;charset=UTF-8")
	public String getPmhzView(HttpServletRequest request, String audTrm, String lv2Code) {
		String authorityAttr = getAuthorityAttr(request);
		return Json.Encode(ywyjService.getPmhzView(authorityAttr, audTrm, lv2Code));
	}

	@ResponseBody
	@RequestMapping(value = "/getZZT", produces = "text/json;charset=UTF-8")
	public String getZZT(String audTrm, String pointCode) {
		try {
			return Json.Encode(ywyjService.getZZT(audTrm, pointCode));
		} catch (Exception e) {
			return Json.Encode("may exists null value " + e.getMessage());
		}

	}

	@ResponseBody
	@RequestMapping(value = "/getZXT", produces = "text/json;charset=UTF-8")
	public String getZXT(HttpServletRequest request, String audTrm, String lv2Code) {
		String authorityAttr = getAuthorityAttr(request);
		try {
			return Json.Encode(ywyjService.getZXT(authorityAttr, audTrm, lv2Code));
		} catch (Exception e) {
			return Json.Encode("may exists null value " + e.getMessage());
		}

	}
}
