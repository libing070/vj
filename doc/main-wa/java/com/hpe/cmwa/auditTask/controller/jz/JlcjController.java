package com.hpe.cmwa.auditTask.controller.jz;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.jz.JlcjService;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.CSVUtils;


@Controller
@RequestMapping("/jlcj/")
public class JlcjController extends BaseController {

    @Autowired
    private  JlcjService  jlcjService;

    private DecimalFormat	   df = new DecimalFormat("######0.00");

    /**
	 * desc:初始化积分回馈率页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request) {

		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		String url = "";
		if(request.getParameter("provinceCode").equals("10000")){
			url = "auditTask/sjk/jlcj/jlcj";
		}else{
			url = "auditTask/jz/jlcj/jlcj";
		}
		return url;
	}
	
	@RequestMapping(value = "jzjlcj")
	public String jzjlcj(HttpServletRequest request) {
		
		request.getSession().setAttribute("beforeAcctMonth", request.getParameter("beforeAcctMonth"));
		request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
		request.getSession().setAttribute("endAcctMonth", request.getParameter("endAcctMonth"));
		request.getSession().setAttribute("auditId", request.getParameter("auditId"));
		request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
		return "auditTask/jz/jlcj/jzjlcj";
	}
	
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
		
		// 清单参数默认值
		defaultParams.put("currDetBeginDate", sessoin.getAttribute("beforeAcctMonth"));
		defaultParams.put("currDetEndDate", sessoin.getAttribute("endAcctMonth"));
		defaultParams.put("currCityType", this.getCityObjectList(sessoin.getAttribute("provinceCode") + ""));
		
		return defaultParams;
	    }
	   public List<Map<String, Object>> getYear(String beginDate,String endDate) {
		   int beginYear = Integer.parseInt(beginDate.substring(0, 4));
		   int endYear = Integer.parseInt(endDate.substring(0, 4).toString());
		  // int yearQJ = endYear - beginYear;
		   List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		   for (int i = beginYear; i <= endYear; i++) {
			   Map<String, Object> dateMap = new HashMap<String, Object>();
			   dateMap.put("beginMouth", i + "01");
			   dateMap.put("endMouth", i + "12");
			   resultList.add(dateMap);
		   }
		   
		   return resultList;
	}
	   
	   /**
	     * Desc  加载明细数据
	     * @param request
	     * @return
	     */

	    @ResponseBody
	    @RequestMapping(value = "getDetailData")
	    public Pager getDetailData(HttpServletRequest request,HttpServletResponse response,Pager pager) {
	    	
	    		pager.setParams(formatParameter(this.getParameterMap(request), request));	
	    		List<Map<String, Object>> list =  jlcjService.getDetailData(pager);
	    		
	    		pager.setDataRows(list);
	    	
	    		return pager;
	   }
	    /**
	     * 导出明细信息
	     * 
	     * <pre>
	     * Desc  
	     * @param response
	     * @param request
	     * @return
	     * @author 
	     * 2016-1-24 下午4:15:54
	     * </pre>
	     */
	    @RequestMapping(value = "exportDetailShuJu")
	    public void exportDetail(HttpServletResponse response, HttpServletRequest request)throws Exception {
			 Map<String, Object> parameterMap = formatParameter(this.getParameterMap(request),request);
			 jlcjService.getDaoChuDetailData(request, response,parameterMap);
	    }
	    
	    @RequestMapping(value = "getgstongji")
		@ResponseBody
		public List<Map<String, Object>> getjftongji(HttpServletRequest request) {
			Map<String, Object> parameterMap = this.getParameterMap(request);
			List<Map<String, Object>> timeList = getYear(parameterMap.get("currSumBeginDate").toString(),parameterMap.get("currSumEndDate").toString());
			parameterMap.put("timeList",timeList);
			parameterMap.put("beginYear",parameterMap.get("currSumBeginDate").toString().substring(0,4));
			parameterMap.put("endYear", parameterMap.get("currSumEndDate").toString().substring(0,4));
			
			return   jlcjService.getgstongji(parameterMap);	
		}
	    //加载各省数据表
	    @ResponseBody
	    @RequestMapping(value = "getgssjb")
	    public Pager getgssjb(HttpServletRequest request,HttpServletResponse response,Pager pager) {
	    	
	    		Map<String, Object> parameterMap = formatParameter(this.getParameterMap(request), request);
	    		List<Map<String, Object>> timeList = getYear(parameterMap.get("currSumBeginDate").toString(),parameterMap.get("currSumEndDate").toString());
				parameterMap.put("timeList",timeList);
				parameterMap.put("beginYear",parameterMap.get("currSumBeginDate").toString().substring(0,4));
				parameterMap.put("endYear", parameterMap.get("currSumEndDate").toString().substring(0,4));
			
				pager.setParams(parameterMap);
	    		List<Map<String, Object>> list =  jlcjService.getgssjb(pager);
	    		
	    		pager.setDataRows(list);
	    	
	    		return pager;
	   }
	    /**
	     * <pre>
	     * Desc  导出地市汇总信息
	     * @param response
	     * @param request
	     * @return
	     * @author 
	     * 2016-11-24 下午7:09:16
	     * </pre>
	     */
	    @RequestMapping(value = "exportGsCity")
	    public Object exportSumCity(HttpServletResponse response, HttpServletRequest request, Map<String, Object> postData) {
		try {
		    Map<String, Object> params = formatParameter(this.getParameterMap(request), request);
		  
    		List<Map<String, Object>> timeList = getYear(params.get("currSumBeginDate").toString(),params.get("currSumEndDate").toString());
    		params.put("timeList",timeList);
    		params.put("beginYear",params.get("currSumBeginDate").toString().substring(0,4));
    		params.put("endYear", params.get("currSumEndDate").toString().substring(0,4));
		
		    List<Map<String, Object>> list = this. jlcjService.getDaoChuGsData(params);
		    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
		    if (list != null && list.size() > 0) {
			LinkedHashMap<String, Object> lhm = null;
			for (int i = 0; i < list.size(); i++) {
			    Map<String, Object> eachMap = list.get(i);
			    lhm = new LinkedHashMap<String, Object>();
			    lhm.put("AUD_YEAR", eachMap.get("AUD_YEAR") + "");
			    lhm.put("short_name", eachMap.get("short_name") + "");
			    lhm.put("SUM_TOL_FEE", eachMap.get("SUM_TOL_FEE"));
			    lhm.put("SUM_INCEN_RWD_SUM", eachMap.get("SUM_INCEN_RWD_SUM"));
			    Double eachDouble = eachMap.get("zhexian1") == null ? 0.00 : Double.parseDouble(eachMap.get("zhexian1").toString());
			    lhm.put("zhexian1", df.format(eachDouble * 100) + "%");
			    lhm.put("SUM_BURGET", eachMap.get("SUM_BURGET"));
			    Double eachDouble1 = eachMap.get("zhexian2") == null ? 0.00 : Double.parseDouble(eachMap.get("zhexian2").toString());
			    lhm.put("zhexian2", df.format(eachDouble1 * 100) + "%");
			    
			    exportData.add(lhm);
			}
		    }
			    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			    map.put("AUD_YEAR", "审计月");
			    map.put("short_name", "省名称");
			    map.put("SUM_TOL_FEE", "发放酬金总额(元)");
			    map.put("SUM_INCEN_RWD_SUM", "激励酬金金额(元)");
			    map.put("zhexian1", "激励酬金占发放酬金总额(%)");
			    map.put("SUM_BURGET", "社会渠道费用预算(元)");
			    map.put("zhexian2", "激励酬金占社会渠道费用预算(%)");
		 
		    CSVUtils.exportCSVList("4.5.2_激励酬金发放合规性_省汇总.csv", exportData, map, request, response);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return null;
	    }
	    
	   @RequestMapping(value = "getdstongji")
		@ResponseBody
		public List<Map<String, Object>> getdstongji(HttpServletRequest request) {
			Map<String, Object> parameterMap = this.getParameterMap(request);
			
			List<Map<String, Object>> timeList = getYear(parameterMap.get("currSumBeginDate").toString(),parameterMap.get("currSumEndDate").toString());
			parameterMap.put("timeList",timeList);
			parameterMap.put("beginYear",parameterMap.get("currSumBeginDate").toString().substring(0,4));
			parameterMap.put("endYear", parameterMap.get("currSumEndDate").toString().substring(0,4));
			
			List<Map<String, Object>> list= jlcjService.getdstongji(parameterMap);
			
			return list;
		}
	   
	 //加载地市数据表
	    @ResponseBody
	    @RequestMapping(value = "getdssjb")
	    public Pager getdssjb(HttpServletRequest request,HttpServletResponse response,Pager pager) {
	    	
	    		Map<String, Object> parameterMap = formatParameter(this.getParameterMap(request), request);
	    		List<Map<String, Object>> timeList = getYear(parameterMap.get("currSumBeginDate").toString(),parameterMap.get("currSumEndDate").toString());
				parameterMap.put("timeList",timeList);
				parameterMap.put("beginYear",parameterMap.get("currSumBeginDate").toString().substring(0,4));
				parameterMap.put("endYear", parameterMap.get("currSumEndDate").toString().substring(0,4));
			
				pager.setParams(parameterMap);
	    		List<Map<String, Object>> list =  jlcjService.getdssjb(pager);
	    	
	    		pager.setDataRows(list);
	    	
	    		return pager;
	   }
	    
	    /**
	     * <pre>
	     * Desc  导出地市在审计期间的数据表
	     * @param response
	     * @param request
	     * @return
	     * @author 
	     * 2016-11-24 下午7:09:16
	     * </pre>
	     */
	    @RequestMapping(value = "exportDsCity")
	    public Object exportDsCity(HttpServletResponse response, HttpServletRequest request, Map<String, Object> postData) {
		try {
		    Map<String, Object> params = formatParameter(this.getParameterMap(request), request);
		  
    		List<Map<String, Object>> timeList = getYear(params.get("currSumBeginDate").toString(),params.get("currSumEndDate").toString());
    		params.put("timeList",timeList);
    		params.put("beginYear",params.get("currSumBeginDate").toString().substring(0,4));
    		params.put("endYear", params.get("currSumEndDate").toString().substring(0,4));
		
		    List<Map<String, Object>> list = this. jlcjService.getDaoChuDsData(params);
		    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
		    if (list != null && list.size() > 0) {
			LinkedHashMap<String, Object> lhm = null;
			for (int i = 0; i < list.size(); i++) {
			    Map<String, Object> eachMap = list.get(i);
			    lhm = new LinkedHashMap<String, Object>();
			    lhm.put("AUD_YEAR", eachMap.get("AUD_YEAR") + "");
			    lhm.put("CMCC_prvd_nm_short", eachMap.get("CMCC_prvd_nm_short") + "");
			    lhm.put("SUM_TOL_FEE", eachMap.get("SUM_TOL_FEE"));
			    lhm.put("SUM_INCEN_RWD_SUM", eachMap.get("SUM_INCEN_RWD_SUM"));
			    Double eachDouble = eachMap.get("zhexian1") == null ? 0.00 : Double.parseDouble(eachMap.get("zhexian1").toString());
			    lhm.put("zhexian1", df.format(eachDouble * 100) + "%");
			    lhm.put("SUM_BURGET", eachMap.get("SUM_BURGET"));
			    Double eachDouble1 = eachMap.get("zhexian2") == null ? 0.00 : Double.parseDouble(eachMap.get("zhexian2").toString());
			    lhm.put("zhexian2", df.format(eachDouble1 * 100) + "%");
			    
			    exportData.add(lhm);
			}
		    }
			    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			    map.put("AUD_YEAR", "审计月");
			    map.put("CMCC_prvd_nm_short", "地市名称");
			    map.put("SUM_TOL_FEE", "发放酬金总额(元)");
			    map.put("SUM_INCEN_RWD_SUM", "激励酬金金额(元)");
			    map.put("zhexian1", "激励酬金占发放酬金总额(%)");
			    map.put("SUM_BURGET", "社会渠道费用预算(元)");
			    map.put("zhexian2", "激励酬金占社会渠道费用预算(%)");
		 
		    CSVUtils.exportCSVList("4.5.2_激励酬金发放合规性_汇总_不同年度同一地市.csv", exportData, map, request, response);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return null;
	    }
	    //加载同年度结论
	   @RequestMapping(value = "getQianTndJieLun")
		@ResponseBody
		public List<Map<String, Object>> getQianTndJieLun(HttpServletRequest request) {
			Map<String, Object> parameterMap = this.getParameterMap(request);
			List<Map<String, Object>> timeList = getYear(parameterMap.get("currSumBeginDate").toString(),parameterMap.get("currSumEndDate").toString());
			parameterMap.put("timeList",timeList);
			parameterMap.put("beginYear",parameterMap.get("currSumBeginDate").toString().substring(0,4));
			parameterMap.put("endYear", parameterMap.get("currSumEndDate").toString().substring(0,4));
			
			List<Map<String, Object>> list= jlcjService.getQianTndJieLun(parameterMap);
			
			return list;
		}
	   @RequestMapping(value = "getHouTndJieLun")
		@ResponseBody
		public List<Map<String, Object>> getHouTndJieLun(HttpServletRequest request) {
			Map<String, Object> parameterMap = this.getParameterMap(request);
			List<Map<String, Object>> timeList = getYear(parameterMap.get("currSumBeginDate").toString(),parameterMap.get("currSumEndDate").toString());
			parameterMap.put("timeList",timeList);
			parameterMap.put("beginYear",parameterMap.get("currSumBeginDate").toString().substring(0,4));
			parameterMap.put("endYear", parameterMap.get("currSumEndDate").toString().substring(0,4));
			
			List<Map<String, Object>> list= jlcjService.getHouTndJieLun(parameterMap);
			
			return list;
		}
	   //加载同年度
	   @RequestMapping(value = "gettndtongji")
		@ResponseBody
		public List<Map<String, Object>> gettndongji(HttpServletRequest request) {
			Map<String, Object> parameterMap = this.getParameterMap(request);
			List<Map<String, Object>> timeList = getYear(parameterMap.get("currSumBeginDate").toString(),parameterMap.get("currSumEndDate").toString());
			parameterMap.put("timeList",timeList);
			parameterMap.put("beginYear",parameterMap.get("currSumBeginDate").toString().substring(0,4));
			parameterMap.put("endYear", parameterMap.get("currSumEndDate").toString().substring(0,4));
			
			List<Map<String, Object>> list= jlcjService.gettndtongji(parameterMap);
			
			return list;
		}
	   
	 //加载同年度数据表
	    @ResponseBody
	    @RequestMapping(value = "gettndsjb")
	    public Pager gettndsjb(HttpServletRequest request,HttpServletResponse response,Pager pager) {
	    	
	    		Map<String, Object> parameterMap = formatParameter(this.getParameterMap(request), request);
	    		List<Map<String, Object>> timeList = getYear(parameterMap.get("currSumBeginDate").toString(),parameterMap.get("currSumEndDate").toString());
				parameterMap.put("timeList",timeList);
				parameterMap.put("beginYear",parameterMap.get("currSumBeginDate").toString().substring(0,4));
				parameterMap.put("endYear", parameterMap.get("currSumEndDate").toString().substring(0,4));
			
				pager.setParams(parameterMap);
	    		List<Map<String, Object>> list =  jlcjService.gettndsjb(pager);
	    		
	    		pager.setDataRows(list);
	    	
	    		return pager;
	   }
	    
	    /**
	     * <pre>
	     * Desc  导出同年度在审计期间的数据表
	     * @param response
	     * @param request
	     * @return
	     * @author 
	     * 2016-11-24 下午7:09:16
	     * </pre>
	     */
	    @RequestMapping(value = "exportTndCity")
	    public Object exportTndCity(HttpServletResponse response, HttpServletRequest request, Map<String, Object> postData) {
		try {
		    Map<String, Object> params = formatParameter(this.getParameterMap(request), request);
    		List<Map<String, Object>> timeList = getYear(params.get("currSumBeginDate").toString(),params.get("currSumEndDate").toString());
    		params.put("timeList",timeList);
    		params.put("beginYear",params.get("currSumBeginDate").toString().substring(0,4));
    		params.put("endYear", params.get("currSumEndDate").toString().substring(0,4));
		
		    List<Map<String, Object>> list = this. jlcjService.getDaoChuTndData(params);
		    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
		    if (list != null && list.size() > 0) {
			LinkedHashMap<String, Object> lhm = null;
			for (int i = 0; i < list.size(); i++) {
			    Map<String, Object> eachMap = list.get(i);
			    lhm = new LinkedHashMap<String, Object>();
			    lhm.put("AUD_YEAR", eachMap.get("AUD_YEAR") + "");
			    lhm.put("CMCC_prvd_nm_short", eachMap.get("CMCC_prvd_nm_short") + "");
			    lhm.put("SUM_TOL_FEE", eachMap.get("SUM_TOL_FEE"));
			    lhm.put("SUM_INCEN_RWD_SUM", eachMap.get("SUM_INCEN_RWD_SUM"));
			    Double eachDouble = eachMap.get("zhexian1") == null ? 0.00 : Double.parseDouble(eachMap.get("zhexian1").toString());
			    lhm.put("zhexian1", df.format(eachDouble * 100) + "%");
			    lhm.put("SUM_BURGET", eachMap.get("SUM_BURGET"));
			    Double eachDouble1 = eachMap.get("zhexian2") == null ? 0.00 : Double.parseDouble(eachMap.get("zhexian2").toString());
			    lhm.put("zhexian2", df.format(eachDouble1 * 100) + "%");
			    
			    exportData.add(lhm);
			}
		    }
			    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			    map.put("AUD_YEAR", "审计月");
			    map.put("CMCC_prvd_nm_short", "地市名称");
			    map.put("SUM_TOL_FEE", "发放酬金总额(元)");
			    map.put("SUM_INCEN_RWD_SUM", "激励酬金金额(元)");
			    map.put("zhexian1", "激励酬金占发放酬金总额(%)");
			    map.put("SUM_BURGET", "社会渠道费用预算(元)");
			    map.put("zhexian2", "激励酬金占社会渠道费用预算(%)");
		 
		    CSVUtils.exportCSVList("4.5.2_激励酬金发放合规性_汇总_同年度各地市.csv", exportData, map, request, response);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return null;
	    }
	    /**
	     *  根据需要对页面参数进行格式化
	     * @param requestParamsMap
	     * @param request
	     * @return
	     */
	    private Map<String, Object> formatParameter(Map<String, Object> requestParamsMap, HttpServletRequest request) {
		if (request.getSession().getAttribute("provinceCode") != null) {
		    requestParamsMap.put("provinceCode", request.getSession().getAttribute("provinceCode"));
		}
		if (requestParamsMap.get("currSumEndDate") != null
			&& !"".equals(requestParamsMap.get("currSumEndDate").toString())) {
		Object currSumEndDate = requestParamsMap.get("currSumEndDate");
		currSumEndDate = currSumEndDate.toString().replace("-", "");
		requestParamsMap.put("currSumEndDate", currSumEndDate);
		}
		if (requestParamsMap.get("currSumBeginDate") != null && !"".equals(requestParamsMap.get("currSumBeginDate").toString())) {
		    Object currSumBeginDate = requestParamsMap.get("currSumBeginDate");
		    currSumBeginDate = currSumBeginDate.toString().replace("-", "");
		    requestParamsMap.put("currSumBeginDate", currSumBeginDate);
		}
		
		if (requestParamsMap.get("currDetEndDate") != null
			&& !"".equals(requestParamsMap.get("currDetEndDate").toString())) {
		Object currDetEndDate = requestParamsMap.get("currDetEndDate");
		currDetEndDate = currDetEndDate.toString().replace("-", "");
		requestParamsMap.put("currDetEndDate", currDetEndDate);
		}
		
		if (requestParamsMap.get("currDetBeginDate") != null && !"".equals(requestParamsMap.get("currDetBeginDate").toString())) {
		    Object currDetBeginDate = requestParamsMap.get("currDetBeginDate");
		    currDetBeginDate = currDetBeginDate.toString().replace("-", "");
		    requestParamsMap.put("currDetBeginDate", currDetBeginDate);
		}
		return requestParamsMap;
	    }

}
