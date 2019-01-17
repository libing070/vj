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

import com.hpe.cmwa.auditTask.service.jz.ZHBTL7001Service;
import com.hpe.cmwa.common.CommonResult;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.CSVUtils;
import com.hpe.cmwa.util.Json;
import com.hpe.cmwa.util.Prvd_info;

/**
 * 综合补贴率
 * 
 * <pre>
 * Desc： 
 * @author   wangpeng
 * @refactor wangpeng
 * @date     2016-11-30 上午10:15:01
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2016-11-30 	   jh 	         1. Created this class.
 * </pre>
 */
@Controller
@RequestMapping("/zhbtl7001/")
public class ZHBTL7001Controller extends BaseController {

    @Autowired
    private ZHBTL7001Service ZHBTL7001Service;

    private DecimalFormat df = new DecimalFormat("######0.00");

    /**
     * 跳转页面
     * 
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2016-11-25 下午1:43:40
     * </pre>
     */
    @RequestMapping(value = "address")
    public String index(HttpServletRequest request) {
    String url = "";
	String begin =request.getParameter("beforeAcctMonth").toString();
	String s1 = begin.substring(0,4);
	String s2 = begin.substring(4,6);
	String end =request.getParameter("endAcctMonth").toString();
	String s3 = end.substring(0,4);
	String s4 = end.substring(4,6);
	request.getSession().setAttribute("beforeAcctMonth", s1+"-"+s2);
	request.getSession().setAttribute("provinceCode", request.getParameter("provinceCode"));
	request.getSession().setAttribute("endAcctMonth", s3+"-"+s4);
	request.getSession().setAttribute("auditId", request.getParameter("auditId"));
	request.getSession().setAttribute("taskCode", request.getParameter("taskCode"));
	request.getSession().setAttribute("tabType", "1");
	if(request.getParameter("provinceCode").equals("10000")){
		url = "auditTask/sjk/zdbt/zdbt7001";
	}else{
		url = "auditTask/jz/zhbtl/zhbtl7001";
	}
	return url;
    }
    @RequestMapping(value = "index")
    public String indexAddress(HttpServletRequest request) {
	Map<String, Object> params =formatParameter(this.getParameterMap(request), request);
//	request.getSession().setAttribute("tabType", params.get("tabType"));
	return "auditTask/jz/zhbtl/zhbtl7001";
    }
    /**
     * 初始化页面参数
     * 
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2016-11-25 下午1:44:30
     * </pre>
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "initDefaultParams")
    public Map initDefaultParams(HttpServletRequest request) {

	HttpSession sessoin = request.getSession();
	Map<String, Object> defaultParams = new HashMap<String, Object>();
	defaultParams.put("beforeAcctMonth", sessoin.getAttribute("beforeAcctMonth"));
	defaultParams.put("provinceCode", sessoin.getAttribute("provinceCode"));
	defaultParams.put("endAcctMonth", sessoin.getAttribute("endAcctMonth"));
	defaultParams.put("auditId", sessoin.getAttribute("auditId"));
	defaultParams.put("taskCode", sessoin.getAttribute("taskCode"));
//	defaultParams.put("tabType", sessoin.getAttribute("tabType"));
	
	// 汇总参数默认值
	defaultParams.put("currSumBeginDate", sessoin.getAttribute("beforeAcctMonth"));
	defaultParams.put("currSumEndDate", sessoin.getAttribute("endAcctMonth"));

	// 清单参数默认值
	defaultParams.put("currDetBeginDate", sessoin.getAttribute("beforeAcctMonth"));
	defaultParams.put("currDetEndDate", sessoin.getAttribute("endAcctMonth"));

	defaultParams.put("cityList", this.getCityObjectList(sessoin.getAttribute("provinceCode") + ""));
	defaultParams.put("marketingTypeList", this.dict.getList("marketingType"));
	defaultParams.put("marketingTypeMap", this.dict.getMap("marketingType"));
	defaultParams.put("xnumber", "100");
	defaultParams.put("ynumber", "1000");
	return defaultParams;
    }
    
    @ResponseBody
    @RequestMapping(value = "getSumPrvdinceCon")
    public Object getSumPrvdinceCon(HttpServletRequest request){
    	Map<String, Object> listCon = ZHBTL7001Service.getSumPrvdinceCon(formatParameter(this.getParameterMap(request), request));
    	Map<String, Object> returnMap = new HashMap<String, Object>();
    	returnMap.put("listCon", listCon);
    	return returnMap;
    }

    /**
     * 获取省汇总信息,实现折线图
     * 
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2016-11-25 下午1:45:35
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getSumPrvdince")
    public Object getSumPrvdince(HttpServletRequest request) {
	// Y1数据
	List<Object> Y1List = new ArrayList<Object>();
	// Y2数据
	List<Object> Y2List = new ArrayList<Object>();
	// 审计月名
	List<Object> audTrmList = new ArrayList<Object>();

	List<Map<String, Object>> list = ZHBTL7001Service.getSumPrvdince(formatParameter(this.getParameterMap(request), request));

	if (list != null && list.size() > 0) {
	    for (int i = 0; i < list.size(); i++) {
		Map<String, Object> eachMap = list.get(i);
		if (eachMap.get("zhbtlwgPer") != null && !"".equals(eachMap.get("zhbtlwgPer").toString())) {
		    Double eachDouble = Double.parseDouble(eachMap.get("zhbtlwgPer").toString());
		    Y1List.add(Double.parseDouble(df.format(eachDouble*100)));
		} else {
		    Y1List.add(0);
		}
		if (eachMap.get("weiguiImeiNum") != null && !"".equals(eachMap.get("weiguiImeiNum").toString())) {
		    Double eachDouble = Double.parseDouble(eachMap.get("weiguiImeiNum").toString());
		    Y2List.add(Double.parseDouble(df.format(eachDouble)));
		} else {
		    Y2List.add(0);
		}
		audTrmList.add(eachMap.get("audTrm"));
	    }
	}
	Map<String, Object> returnMap = new HashMap<String, Object>();

	returnMap.put("Y1List", Y1List);
	returnMap.put("Y2List", Y2List);

	returnMap.put("audTrmList", audTrmList);

	return CommonResult.success(returnMap);
    }

    /**
     * 获取地图左侧明细介绍
     * 
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author jh
     * 2016-11-27 下午4:46:43
     * </pre>
     */
    @RequestMapping(value = "getMapDetail")
    @ResponseBody
    public Object getMapDetail(HttpServletRequest request) {
	Map<String, Object> sumMap = new HashMap<String, Object>();
	Map<String, Object> params = formatParameter(this.getParameterMap(request), request);
	List<Map<String, Object>> list = ZHBTL7001Service.getSumSort(params);
	sumMap.put("auditMonth", request.getParameter("currSumEndDate"));
	if (list == null || list.size()==0) {
	    sumMap.put("weiGuiImeiNum", "暂无数据");
	    sumMap.put("sort", "暂无数据");
	    sumMap.put("top3City", "暂无数据");
	    return sumMap;
	}
	for(int i = 0 ;i<list.size();i++){
	    Map<String, Object> eachMap = list.get(i);
	    String provinceCode = eachMap.get("CMCC_prov_prvd_id")==null?"":eachMap.get("CMCC_prov_prvd_id").toString();
	    if(provinceCode.equals(params.get("provinceCode").toString())){
		sumMap.putAll(eachMap);
		break;
	    }
	}
	// top3 city
	List<Map<String, Object>> top3CityList = this.ZHBTL7001Service.getTop3City(params);
	StringBuffer top3City = new StringBuffer();
	for (Map<String, Object> cityInfo : top3CityList) {
	    top3City.append(cityInfo.get("CMCC_prvd_nm_short")).append(",");
	}
	if (top3City.length() > 0) {
	    sumMap.put("top3City", top3City.substring(0, top3City.length() - 1));
	}
	sumMap.put("provinceCode", params.get("provinceCode").toString());
	return sumMap;
    }

    /**
     * 查询地市汇总信息(数据表)
     * 
     * <pre>
     * Desc  
     * @param response
     * @param request
     * @return
     * @author wangpeng
     * 2016-11-25 下午4:20:04
     * </pre>
     */
    @RequestMapping(value = "sumCityPager")
    @ResponseBody
    public Object getSumCityPager(HttpServletResponse response, HttpServletRequest request, Pager pager) {
	pager.setParams(formatParameter(this.getParameterMap(request), request));
	List<Map<String, Object>> list = this.ZHBTL7001Service.getSumCity(pager);
	pager.setDataRows(list);
	return pager;
    }

    /**
     * 获取明细清单信息
     * 
     * <pre>
     * Desc  
     * @param response
     * @param request
     * @param pager
     * @return
     * @author wangpeng
     * 2016-11-25 下午5:13:08
     * </pre>
     */
    @RequestMapping(value = "getDetailList")
    @ResponseBody
    public Object getSumDitail(HttpServletResponse response, HttpServletRequest request, Pager pager) {
	pager.setParams(formatParameter(this.getParameterMap(request), request));
	List<Map<String, Object>> list = ZHBTL7001Service.getDetailList(pager);
	pager.setDataRows(list);
	return pager;
    }

    /**
     * 获取地图汇总信息
     * 
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2016-11-25 下午5:12:27
     * </pre>
     */
    @RequestMapping(value = "getMapData", produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public Object getMapData(HttpServletRequest request) {
	HttpSession session = request.getSession();
	Map<String, Object> result = new HashMap<String, Object>();
	Map<String, Object> params =formatParameter(this.getParameterMap(request), request);
	List<Map<String, Object>> list = ZHBTL7001Service.getSumCityByMap(params);
	List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
	Map<String, Object> value = null;
	for (int i = 0; i < list.size(); i++) {
	    Map<String, Object> map = list.get(i);
	    value = new HashMap<String, Object>();
	    value.put("cityName", map.get("shortName"));
	    value.put("cmccProvId", map.get("cmccProvId"));
	    Double zhbtlwgPer =  map.get("zhbtlwgPer")==null?0.00:Double.parseDouble(map.get("zhbtlwgPer").toString());
	    zhbtlwgPer = Double.parseDouble(df.format(zhbtlwgPer*100));
	    value.put("zhbtlwgPer", zhbtlwgPer);
	    values.add(value);
	}
	//计算全国违规终端数量占比
	Map<String, Object> map = ZHBTL7001Service.getImeiNumPer(params);
	if (map != null) {
	    Double weiguiImeiNum = Double.parseDouble(map.get("weiguiImeiNum").toString());
	    Double tolImeiNum = Double.parseDouble(map.get("tolImeiNum").toString());
	    Double per = weiguiImeiNum / tolImeiNum;
	    per = Double.parseDouble(df.format(per));
	    result.put("xnumber", Double.parseDouble(df.format(per*100)));
	    result.put("ynumber", Double.parseDouble(df.format(per * 5*100)));
	} else {
	    result.put("xnumber", 100);
	    result.put("ynumber", 1000);
	}
	Prvd_info prvdInfo = Constants.getPrvdInfo(session.getAttribute("provinceCode").toString());
	result.put("values", values);
	result.put("prvdPinYinName", prvdInfo.getPrivdcd().toLowerCase());
	result.put("provName", prvdInfo.getPrivdnm());
	CommonResult commonResult = new CommonResult();
	commonResult.setBody(result);
	return Json.Encode(commonResult);
    }

    /**
     * 导出明细信息
     * 
     * <pre>
     * Desc  
     * @param response
     * @param request
     * @return
     * @author wangpeng
     * 2016-11-23 下午4:15:54
     * </pre>
     */
    @RequestMapping(value = "exportDetail")
    public void exportDetail(HttpServletResponse response, HttpServletRequest request)throws Exception {
    	
    	Map<String, Object> params = formatParameter(this.getParameterMap(request),request);
    	ZHBTL7001Service.getDetailAll(request,response,params);
    	
    }

    /**
     * <pre>
     * Desc  导出地市汇总信息
     * @param response
     * @param request
     * @return
     * @author wangpeng
     * 2016-11-22 下午7:09:16
     * </pre>
     */
    @RequestMapping(value = "exportSumCity")
    public Object exportSumCity(HttpServletResponse response, HttpServletRequest request, Map<String, Object> postData) {
	try {
	    Map<String,Object> params = formatParameter(this.getParameterMap(request), request);
	    List<Map<String, Object>> list = this.ZHBTL7001Service.getSumCityAll(params);
	    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	    if (list != null && list.size() > 0) {
		LinkedHashMap<String, Object> lhm = null;
		for (int i = 0; i < list.size(); i++) {
		    Map<String, Object> eachMap = list.get(i);
		    lhm = new LinkedHashMap<String, Object>();
		    lhm.put("1", params.get("currSumBeginDate")+"-"+params.get("currSumEndDate"));
		    lhm.put("2", eachMap.get("cmccPrvdNmShort"));
		    lhm.put("3", eachMap.get("offerNum"));
		    lhm.put("4", eachMap.get("weiguiImeiNum"));
		    lhm.put("5", eachMap.get("tolImeiNum"));
		    lhm.put("6", Double.parseDouble(df.format(eachMap.get("zhbtlwgPer"))));
		    exportData.add(lhm);
		}
	    }
	    
	    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    map.put("1", "审计月");
	    map.put("2", "地市名称");
	    map.put("3", "营销案数量");
	    map.put("4", "违规终端数");
	    map.put("5", "终端ＩＭＥＩ数");
	    map.put("6", "违规终端占比(%)");
	  
	    CSVUtils.exportCSVList("4.2.3_终端综合补贴合规性_综合补贴率_数据表.csv", exportData, map, request, response);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * <pre>
     * Desc  根据需要对页面参数进行格式化
     * @param requestParamsMap
     * @author peter.fu
     * Nov 25, 2016 5:27:07 PM
     * </pre>
     */
    private Map<String, Object> formatParameter(Map<String, Object> requestParamsMap, HttpServletRequest request) {
	if (request.getSession().getAttribute("provinceCode") != null) {
	    requestParamsMap.put("provinceCode", request.getSession().getAttribute("provinceCode"));
	}
	if (requestParamsMap.get("currSumEndDate") != null && !"".equals(requestParamsMap.get("currSumEndDate").toString())) {
	    Object currSumEndDate = requestParamsMap.get("currSumEndDate");
	    currSumEndDate = currSumEndDate.toString().replace("-", "");
	    requestParamsMap.put("currSumEndDate", currSumEndDate);
	}
	if (requestParamsMap.get("currSumBeginDate") != null && !"".equals(requestParamsMap.get("currSumBeginDate").toString())) {
	    Object currSumBeginDate = requestParamsMap.get("currSumBeginDate");
	    currSumBeginDate = currSumBeginDate.toString().replace("-", "");
	    requestParamsMap.put("currSumBeginDate", currSumBeginDate);
	}
	if (requestParamsMap.get("currDetEndDate") != null && !"".equals(requestParamsMap.get("currDetEndDate").toString())) {
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
