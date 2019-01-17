package com.hpe.cmwa.auditTask.controller.jy;

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

import com.hpe.cmwa.auditTask.service.jy.SHQDService;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.Prvd_info;

/**
 * 
 * <pre>
 * Desc： 社会渠道酬金异常波动Controller
 * @author jh
 * @refactor jh
 * @date   2017-4-10 上午11:16:18
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-4-10 	   jh 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping(value="shqd")
public class SHQDController  extends BaseController {
	
	@Autowired
	private SHQDService shqdService;
	
	/**
	 * 
	 * <pre>
	 * Desc  社会渠道酬金异常波动初始化页面
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-10 上午11:20:57
	 * </pre>
	 */
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request) {

		logger.debug(this.getParameterStr(request));
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		if(request.getParameter("provinceCode").equals("10000")){
    		return "auditTask/sjk/shqd/shqd";
    	}else{
    		return "auditTask/jy/shqd/shqd";
    	}
	}
	
	
	/**
	 * 
	 * <pre>
	 * Desc  社会渠道酬金环比波动区间统计图chart CONTROLLER
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 下午2:33:28
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="hb_chart")
	public List<Map<String, Object>> hb_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		return shqdService.hb_chart(params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  社会渠道酬金环比波动区间数据表table CONTROLLER
	 * @param request
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 下午2:32:25
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="hb_table")
	public Pager hb_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = shqdService.hb_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  社会渠道酬金环比波动区间统计图chart CONTROLLER
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 下午2:33:28
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="fc_chart")
	public List<Map<String, Object>> fc_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		return shqdService.fc_chart(params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  社会渠道酬金环比波动区间数据表table CONTROLLER
	 * @param request
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 下午2:32:25
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="fc_table")
	public Pager fc_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = shqdService.fc_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}

	/**
	 * 
	 * <pre>
	 * Desc  酬金环比排名前五的渠道统计图chart CONTROLLER
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 下午4:34:52
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="five_qd_chart")
	public Map<String, Object> five_qd_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Map<String, Object>> socChnlIdList = shqdService.five_qd(params);
		List<String> socChnlIds = new ArrayList<String>();
		Map<String, Object> map = new HashMap<String, Object>();
		if(socChnlIdList.size()>0){
			for(Map<String, Object> socChnlId : socChnlIdList){
				socChnlIds.add((String) socChnlId.get("socChnlId"));
			}
			params.put("socChnlIds", socChnlIds);
			List<Map<String, Object>> list = shqdService.five_qd_chart(params);
			map.put("socChnlIds", socChnlIdList);
			map.put("list", list);
		}
		return map;
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  酬金环比排名前五的渠道数据表table CONTROLLER
	 * @param request
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-12 下午3:03:19
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="five_table")
	public Pager five_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = shqdService.five_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  酬金环比排名前五的渠道数据表table导出 CONTROLLER
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-12 下午3:04:48
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="five_export")
	public void five_export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = this.getParameterMap(request);
		shqdService.five_export(request,response,params);
	}

	/**
	 * 
	 * <pre>
	 * Desc  酬金方差排名前10的社会渠道chart CONTROLLER
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 下午4:34:52
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="ten_qd_chart")
	public Map<String, Object> ten_qd_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Map<String, Object>> socChnlIdList = shqdService.ten_qd(params);
		List<String> socChnlIds = new ArrayList<String>();
		Map<String, Object> map = new HashMap<String, Object>();
		if(socChnlIdList.size()>0){
			for(Map<String, Object> socChnlId : socChnlIdList){
				socChnlIds.add((String) socChnlId.get("socChnlId"));
			}
			params.put("socChnlIds", socChnlIds);
			List<Map<String, Object>> list = shqdService.ten_qd_chart(params);
			map.put("socChnlIds", socChnlIdList);
			map.put("list", list);
		}
		return map;
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  酬金方差排名前10的社会渠道table CONTROLLER
	 * @param request
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-12 下午3:03:19
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="ten_table")
	public Pager ten_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords = shqdService.ten_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  酬金方差排名前10的社会渠道table导出 CONTROLLER
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-12 下午3:04:48
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="ten_export")
	public void ten_export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = this.getParameterMap(request);
		shqdService.ten_export(request,response,params);
	}
	
	
	/**
	 * 
	 * <pre>
	 * Desc  明细数据
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-5 下午3:09:16
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="mx_table")
	public Pager mx_table(HttpServletRequest request, Pager pager) {
		pager.setParams(this.getParameterMap(request));
		List<Map<String, Object>> dataRecords =shqdService.mx_table(pager);
		pager.setDataRows(dataRecords);
		return pager;
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  导出明细数据表Controller
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 下午1:14:21
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value="mx_table_export")
	public void mx_table_export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = this.getParameterMap(request);
		shqdService.mx_table_export(request,response,params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  社会渠道酬金 参数初始化
	 * @param request
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 上午11:06:19
	 * </pre>
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "initDefaultParams")
	public Map initDefaultParams(HttpServletRequest request) {

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
