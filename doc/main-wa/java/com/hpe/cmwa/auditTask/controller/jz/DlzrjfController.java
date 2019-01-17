/**
 * com.hpe.cmwa.controller.DemoController.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.controller.jz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.jz.DlzrjfService;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.Prvd_info;

/**
 * <pre>
 * Desc：个人客户欠费controller
 * 访问路径：http://localhost:8080/cmwa/personTuition/index?cmccProvPrvdId='10600'&audTrmStar='201602'&audTrmEnd='201610'
 * @author   xinguangchuan
 * @refactor xinguangchuan
 * @date     Nov 16, 2016 10:17:41 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Nov 16, 2016 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/dlzrjf/")
public class DlzrjfController extends BaseController {

	@Autowired
	private DlzrjfService dlzrjfService;

	/**
	 * desc:初始化同一工号操作大量转入积分
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "index")
	public String integral(HttpServletRequest request,HttpServletResponse response) {
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		return "auditTask/jz/integral/dlzrjf";
	}
	/**
	 * desc:初始化同一工号操作大量转入积分
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "jfindex")
	public String index(HttpServletRequest request,HttpServletResponse response) {
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		if(request.getParameter("provinceCode").equals("10000")){
			return  "auditTask/sjk/ygyccz/tyghchdlzrjf_qg";
		}else{
			return "auditTask/jy/ygyccz/jfyccz";
		}
	}
	/**
	 * 信控等级汇总统计 审计结论
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getbodongjielun")
	@ResponseBody
	public Map<String, Object> getXinkongjielun(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		return dlzrjfService.getbodongjielun(parameterMap);
	}
	
	/**
	 * 省汇总表统计周期内每月异常转入积分值 波动趋势图数据
	 * 省汇总表统计周期内全省的平均单月异常转入积分值 波动趋势图数据
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getbodongZRValue")
	@ResponseBody
	public List<Object> getbodongZRValue(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		List<Object> list = new ArrayList<Object>();
		List<Map<String, Object>> resultList = dlzrjfService.getbodongZRValue(parameterMap);
		list.add(resultList);
		list.add(dlzrjfService.getbodongPrvdAVG(parameterMap));
		return list;
	}
	/**
	 * 大量转入积分值波动趋势
	 * 同一工号办理同一号码大量转入积分的情况，共涉及工号XX个、X积分、用户数XX个
	 * 其中单笔最高转入积分X分
	 * 异常操作积分值前三的地市：XX地市、XX地市、XX地市，涉及工号XX个、XXX积分、用户数XX个
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "gettongjijielun")
	@ResponseBody
	public List<Object> gettongjijielun(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		List<Object> list = new ArrayList<Object>();
		list.add(dlzrjfService.getjfzrqktongji(parameterMap));
		list.add(dlzrjfService.getMaxPerCity(parameterMap));
		return list;
	}
	/**
	 * 同一工号办理同一号码大量转入积分统计波动趋势图数据
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getjftongji")
	@ResponseBody
	public List<Map<String, Object>> getjftongji(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		return dlzrjfService.getjftongji(parameterMap);
	}
	
	/**
	 *  明细  数据表展示
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/gettongjiSJB")
	public Pager gettongjiSJB(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = dlzrjfService.gettongjiSJB(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 同一工号办理同一号码大量转入积分统计 数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/exprottongji")
	public void exprottongji(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		dlzrjfService.exprottongji(request, response, parameterMap);
	}
	
	
	
	
	/**
	 *  明细  数据表展示
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDetList")
	public Pager getDetList(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = dlzrjfService.getDetList(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 明细 数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/exprotDetList")
	public void exprotDetList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		dlzrjfService.exprotDetList(request, response, parameterMap);
	}
	
	
	
	
	/**
	 * <pre>
	 * Desc 每个controller都要实现的一个方法，用来设置页面初始化查询参数的form使用 
	 * @return
	 * @author xinguangchuan
	 * Nov 17, 2016 2:16:17 PM
	 * </pre>
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "initDefaultParams")
	public Map initDefaultParams(HttpServletRequest request,HttpServletResponse response) {
		HttpSession sessoin = request.getSession();
		Map<String, Object> defaultParams = new HashMap<String, Object>();
		// 国信参数
		defaultParams.put("provinceCode", sessoin.getAttribute("provinceCode"));
		defaultParams.put("beforeAcctMonth", sessoin.getAttribute("beforeAcctMonth"));
		defaultParams.put("endAcctMonth", sessoin.getAttribute("endAcctMonth"));
		defaultParams.put("auditId", sessoin.getAttribute("auditId"));
		defaultParams.put("taskCode", sessoin.getAttribute("taskCode"));
		// 汇总参数默认值
		defaultParams.put("currSumBeginDate", sessoin.getAttribute("beforeAcctMonth"));
		defaultParams.put("currSumEndDate", sessoin.getAttribute("endAcctMonth"));
		Prvd_info prvdInfo = Constants.getPrvdInfo(sessoin.getAttribute("provinceCode").toString());
		defaultParams.put("provinceName", prvdInfo.getPrivdcd());
		// 清单参数默认值
		defaultParams.put("currDetBeginDate", sessoin.getAttribute("beforeAcctMonth"));
		defaultParams.put("currDetEndDate", sessoin.getAttribute("endAcctMonth"));
		defaultParams.put("currCityType", this.getCityObjectList(sessoin.getAttribute("provinceCode") + ""));
		defaultParams.put("pointsChnlName", dict.getList("pointsChnlName"));
		
		return defaultParams;
	}
}
