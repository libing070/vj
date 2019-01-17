package com.hpe.cmca.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hpe.cmca.service.CompareTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.service.DataComparesService;
import com.hpe.cmca.util.Json;
import org.springframework.transaction.annotation.Transactional;

/**
 * <pre>
 * Desc：
 * @author   cyz
 * @date     May 3, 2018 5:01:00 PM
 * @version  1.0
 *
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  May 3, 2018 	    cyz 	         1. Created this class.
 * </pre>
 */
@Controller
@RequestMapping("/sjbgys")
public class DataComparesController extends BaseController {
	@Autowired
	private DataComparesService dataComparesService = null;
	@Autowired
	public CompareTagService compareTagService;
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjjh })
	// 判断是否具有参数管理权限
	@RequestMapping(value = "index")
	public String index() {
		return "sjbgys/index";
	}

	/**
	 * 获取权限
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAuthStatus", produces = "text/json; charset=UTF-8")
	@ResponseBody
	public String getAuthStatus(HttpServletRequest request) {
		int authorityAttr = 0;
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			session.setAttribute("depId", 10000);
		}

		Object depId = session.getAttribute("depId");
		if (Integer.parseInt(depId.toString()) == 10009) {
			authorityAttr = 1;
		}
		return Json.Encode(authorityAttr);
	}

	/**
	 * 只获取渠道样卡和终端套利下拉专题
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/selectSubjectList", produces = "text/json; charset=UTF-8")
	@ResponseBody
	public String selectSubjectList(HttpServletRequest request) {
		List<Map<String, Object>> paramList = null;
		try {
			paramList = dataComparesService.selectSubject();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return Json.Encode(paramList);
	}

	/**
	 * 获取审计月下拉列表
	 */
	@ResponseBody
	@RequestMapping(value = "/getAuditMonthList", produces = "text/json;charset=UTF-8")
	public String getAuditMonthList(String subjectId, String roleId) {
		Map<String, List> map = new HashMap<String, List>();
		List<Map<String, Object>> dataList = null;
		try {
			if (roleId != null) {
				dataList = dataComparesService.getAuditMonth(subjectId, roleId);
				map.put("auditMonthInfo", dataList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return Json.Encode(map);

	}

	/**
	 * 获取比对数据列表接口
	 *
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "selDataCompareList", produces = "text/json;charset=UTF-8")
	public String selDataCompareList(HttpServletRequest request,String subjectId,String audTrm)
			throws ParseException {
		if("4".equals(subjectId)||"7".equals(subjectId)){
			return Json.Encode(compareTagService.getCompareResult(subjectId,audTrm));
		}
		return Json.Encode(dataComparesService.selDataCompareList());
	}

	/**
	 * 在线数据比对接口
	 *
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	//@Transactional(rollbackFor = Exception.class)
	@ResponseBody
	@RequestMapping(value = "onlineDataCompare", produces = "text/json;charset=UTF-8")
	public String onlineDataCompare(HttpServletRequest request,
			String subjectId, String audMonth) throws ParseException {
		JSONObject json = new JSONObject();
		try {
			json = dataComparesService.onlineDataCompare(subjectId, audMonth);
			if("4".equals(subjectId)||"7".equals(subjectId)){
				return Json.Encode(compareTagService.computeTag(subjectId, audMonth));
				//json.put("3", new String[]{"文件正常,数据异常"});

			}

		} catch (Exception e) {
			json.put("1", new String[]{"ERROR"});

			e.printStackTrace();
		}
		return Json.Encode(json);

	}

}
