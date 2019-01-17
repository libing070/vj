package com.hpe.cmwa.controller;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.common.CommonResult;
import com.hpe.cmwa.common.datasource.MultipleDataSourceContextHolder;
import com.hpe.cmwa.dao.MybatisDao;

@Controller
@RequestMapping("/base/")
public class BaseController extends BaseObject {

	@Autowired
	protected MybatisDao mybatisDao;

	@ResponseBody
	@RequestMapping(value = "getCurrentDate", produces = "plain/text; charset=UTF-8")
	protected String getCurrentDate() {
		return DateFormatUtils.format(new Date(), "yyyyMMdd");
	}

	/**
	 * <pre>
	 * Desc  获取request中的参数,并结成string
	 * &#64;param request
	 * &#64;return
	 * &#64;author peter.fu
	 * Nov 17, 2016 10:09:25 AM
	 * </pre>
	 */
	protected String getParameterStr(HttpServletRequest request) {

		StringBuffer resultStr = request.getRequestURL();
		resultStr.append("?");
		Enumeration<?> enum_term = request.getParameterNames();
		while (enum_term.hasMoreElements()) {
			String paramName = (String) enum_term.nextElement();
			String paramValue = request.getParameter(paramName);
			resultStr.append(paramName).append("=").append(paramValue).append("&");
		}
		return resultStr.toString();
	}

	/**
	 * <pre>
	 * Desc  获取request中的参数
	 * &#64;param request
	 * &#64;return
	 * &#64;author 
	 * &#64;refactor 
	 * &#64;date   2016-9-19 下午4:09:15
	 * </pre>
	 */
	protected Map<String, Object> getParameterMap(HttpServletRequest request) {
		Map<String, Object> pathMap = new HashMap<String, Object>(20);
		Enumeration<?> enum_term = request.getParameterNames();
		while (enum_term.hasMoreElements()) {
			String paramName = (String) enum_term.nextElement();
			if (request.getParameterValues(paramName).length > 1) {
				pathMap.put(paramName.replace("[]", ""), request.getParameterValues(paramName));
			} else {
				pathMap.put(paramName, request.getParameter(paramName));
			}
		}
		return pathMap;
	}

	/**
	 * <pre>
	 * Desc  根据省份ID获取地市列表
	 * &#64;param prvdId
	 * &#64;return
	 * &#64;author GuoXY
	 * &#64;refactor GuoXY
	 * &#64;date   Sep 21, 2016 10:29:38 AM
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "getCityList", produces = "plain/text; charset=UTF-8")
	public String getCityList(String prvdId) {
		MultipleDataSourceContextHolder.setDataSource("dataSourceMysql");
		Map<String, String> params = new HashMap<String, String>();
		params.put("provinceCode", prvdId);
		List<Map<String, Object>> list = mybatisDao.getList("commonMapper.selectCityList", params);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("ctyList", list);

		return CommonResult.success(result);
	}

	/**
	 * 根据省份获取地市信息列表
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param prvdId
	 * &#64;return
	 * &#64;author wangpeng
	 * 2016-11-24 下午5:52:02
	 * </pre>
	 */
	public List<Map<String, Object>> getCityObjectList(String prvdId) {
		MultipleDataSourceContextHolder.setDataSource("dataSourceMysql");
		Map<String, String> params = new HashMap<String, String>();
		params.put("provinceCode", prvdId);
		List<Map<String, Object>> list = mybatisDao.getList("commonMapper.selectCityList", params);
		return list;
	}

	/**
	 * 插入事件码到
	 */
	@ResponseBody
	@RequestMapping(value = "insertLocalCode")
	public void insertLocalCode(HttpServletRequest request, String code, String staffId, String staffName,
			String operationType, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", -1);
		Map<String, String> params = new HashMap<String, String>();

		if ("MAS_hp_cmwa_sjjk_top_tab_01".equals(code) || "MAS_hp_cmwa_hzmx_tab_01".equals(code)
				|| "MAS_hp_cmwa_hzmx_top_tab_01".equals(code)) {
			params.put("operationType", "跳转");
		}
		if ("MAS_hp_cmwa_sjjk_search_02".equals(code) || "MAS_hp_cmwa_hzmx_search_02".equals(code)) {
			params.put("operationType", "查询");
		}
		if ("MAS_hp_cmwa_hzmx_table_download_03".equals(code) || "MAS_hp_cmwa_sjjk_pro_download_03".equals(code)
				|| "MAS_hp_cmwa_sjjk_table_export_03".equals(code)) {
			params.put("operationType", "下载/导出");
		}

		String eventName = this.dict.getMap("eventCodeType").get(code);

		// 返回客户端的ip地址
		String clientAddress = request.getRemoteAddr();
		// 模块路径url
		String resourceUrl = request.getHeader("Referer");

		// 获取服务端的ip地址
		String serverAddress = request.getLocalAddr();
		// 浏览器基本信息
		String other = request.getHeader("User-Agent");

		System.out.println(staffId + "**********************************************" + staffName
				+ "************************************" + other);
		params.put("eventName", eventName);
		params.put("userId", staffId);
		params.put("userName", staffName);
		params.put("clientAddress", clientAddress);
		params.put("resourceUrl", resourceUrl);

		params.put("serverAddress", serverAddress);
		params.put("other", other);
		mybatisDao.add("commonMapper.addLocalCode", params);

	}

}
