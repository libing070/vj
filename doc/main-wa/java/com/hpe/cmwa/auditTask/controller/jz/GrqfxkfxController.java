/**
 * com.hpe.cmwa.controller.DemoController.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.controller.jz;

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

import com.hpe.cmwa.auditTask.service.jz.GrqfxkfxService;
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
@RequestMapping("/grqfxkfx/")
public class GrqfxkfxController extends BaseController {

	@Autowired
	private GrqfxkfxService grqfxkfxService;

	/**
	 * desc:初始化个人信控分析页面
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
		return "auditTask/jz/grqf/grqfxkfx";
	}
	
	/**
	 *  信控等级汇总统计 数据表展示
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/getXinkongPagerList")
    public Pager getXinkongPagerList(HttpServletRequest request, Pager pager) {
        pager.setParams(this.getParameterMap(request));
        List<Map<String, Object>> dataRecords = grqfxkfxService.getXinkongPagerList(pager);
        pager.setDataRows(dataRecords);
        return pager;
    }
	/**
	 * 信控等级汇总统计 审计结论
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getXinkongjielun")
	@ResponseBody
	public Map<String, Object> getXinkongjielun(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		return grqfxkfxService.getXinkongjielun(parameterMap);
	}
	
	/**
	 *  超透支额度后欠费账龄分布  数据表展示
	 * @param request
	 * @param pager
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getxkzhanglingPagerList")
	public Pager getzhanglingPagerList(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = grqfxkfxService.getxkzhanglingPagerList(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 超透支额度后欠费账龄分布  审计结论
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "getxkzhanglingjielun")
	@ResponseBody
	public Map<String, Object> getxkzhanglingjielun(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		return grqfxkfxService.getxkzhanglingjielun(parameterMap);
	}
	
	/**
	 * 超透支额度后欠费账龄分布 欠费账户数
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getxkzhanglingzhs")
	@ResponseBody
	public Map<String,Object> getxkzhanglingzhs(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		Map<String,Object> resultMap = grqfxkfxService.getxkzhanglingzhs(parameterMap);
		if(resultMap != null){
			if(!resultMap.containsKey("numPer1")){
				resultMap.put("numPer1", 0);
			}
			if(!resultMap.containsKey("numPer2")){
				resultMap.put("numPer2", 0);
			}
			if(!resultMap.containsKey("numPer3")){
				resultMap.put("numPer3", 0);
			}
			if(!resultMap.containsKey("numPer4")){
				resultMap.put("numPer4", 0);
			}
			if(!resultMap.containsKey("numPer5")){
				resultMap.put("numPer5", 0);
			}
			if(!resultMap.containsKey("numPer6")){
				resultMap.put("numPer6", 0);
			}
		}else{
			resultMap = new HashMap<String, Object>();
			resultMap.put("numPer1", 0);
			resultMap.put("numPer2", 0);
			resultMap.put("numPer3", 0);
			resultMap.put("numPer4", 0);
			resultMap.put("numPer5", 0);
			resultMap.put("numPer6", 0);
		}
		return resultMap;
	}
	
	/**
	 * 超透支额度后欠费账龄分布 欠费金额
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getxkzhanglingje")
	@ResponseBody
	public Map<String,Object> getxkzhanglingje(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		Map<String,Object>  resultMap= grqfxkfxService.getxkzhanglingje(parameterMap);
		if(resultMap != null){
			if(!resultMap.containsKey("amtPer1")){
				resultMap.put("amtPer1", 0);
			}
			if(!resultMap.containsKey("amtPer2")){
				resultMap.put("amtPer2", 0);
			}
			if(!resultMap.containsKey("amtPer3")){
				resultMap.put("amtPer3", 0);
			}
			if(!resultMap.containsKey("amtPer4")){
				resultMap.put("amtPer4", 0);
			}
			if(!resultMap.containsKey("amtPer5")){
				resultMap.put("amtPer5", 0);
			}
			if(!resultMap.containsKey("amtPer6")){
				resultMap.put("amtPer6", 0);
			}
		}else{
			resultMap = new HashMap<String, Object>();
			resultMap.put("amtPer1", 0);
			resultMap.put("amtPer2", 0);
			resultMap.put("amtPer3", 0);
			resultMap.put("amtPer4", 0);
			resultMap.put("amtPer5", 0);
			resultMap.put("amtPer6", 0);
		}
		return resultMap;
	}
	
	
	/**
	 * 超透支额度后欠费账龄分布 数据表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/exportxkZhangLingList")
	public void exportxkZhangLingList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		grqfxkfxService.exportxkZhangLingList(request, response, parameterMap);
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
		List<Map<String, Object>> dataRecords = grqfxkfxService.getDetList(pager);
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
		grqfxkfxService.exprotDetList(request, response, parameterMap);
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
		
		return defaultParams;
	}
}
