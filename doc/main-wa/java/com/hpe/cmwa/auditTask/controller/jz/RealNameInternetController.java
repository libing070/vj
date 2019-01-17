package com.hpe.cmwa.auditTask.controller.jz;

import java.io.UnsupportedEncodingException;
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

import com.hpe.cmwa.auditTask.service.jz.RealNameInternetService;
import com.hpe.cmwa.common.CommonResult;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.CSVUtils;
import com.hpe.cmwa.util.Prvd_info;

/**
 * <pre>
 * 存量物联网M2M实名制
 * Desc： 
 * @author   gongtingting
 * @refactor gongtingting
 * @date     2016-11-18 上午10:05:54
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-01-05 	   gongtingting	         1. Created this class.
 * </pre>
 */
@Controller
@RequestMapping("/real/")
public class RealNameInternetController extends BaseController {

    @Autowired
    private RealNameInternetService  realNameInternetService;

    private DecimalFormat	   df = new DecimalFormat("######0.00");

    @RequestMapping(value = "indexAddress")
    public String indexAddress(HttpServletRequest request) {
    	Map<String, Object> params =formatParameter(this.getParameterMap(request), request);
    	request.getSession().setAttribute("tabType", params.get("tabType"));
    	if(request.getParameter("provinceCode").equals("10000")){
    		return "auditTask/sjk/realname/clwlM2MRealName";
    	}else{
    		return "auditTask/jz/realname/internet";
    	}
    	
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
	defaultParams.put("tabType", sessoin.getAttribute("tabType"));
	
	
	// 汇总参数默认值
	defaultParams.put("currSumBeginDate", sessoin.getAttribute("beforeAcctMonth"));
	defaultParams.put("currSumEndDate", sessoin.getAttribute("endAcctMonth"));
	defaultParams.put("tabType", sessoin.getAttribute("tabType"));
	
	
	// 清单参数默认值
	defaultParams.put("currDetBeginDate", sessoin.getAttribute("beforeAcctMonth"));
	defaultParams.put("currDetEndDate", sessoin.getAttribute("endAcctMonth"));
	defaultParams.put("currEntDtBegin", "");
	defaultParams.put("currEntDtEnd", "");
	defaultParams.put("currUserType", "");
	defaultParams.put("currCityType", "");
	defaultParams.put("currUserStatus", "");
	defaultParams.put("currPayType", "");

	// 明细页面的下拉列表
	defaultParams.put("userTypeList", dict.getList("userType"));
	defaultParams.put("userStatusList", dict.getList("userStatus"));
	defaultParams.put("payTypeList", dict.getList("payType"));
	defaultParams.put("cityList", this.getCityObjectList(sessoin.getAttribute("provinceCode") + ""));

	return defaultParams;
    }
    /**
     * 加载趋势图
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "sumRealNamePrvd")
    public Object getSumRealNamePrvd(HttpServletRequest request) {

	List<Map<String, Object>> list = realNameInternetService.getSumRealNamePrvd(formatParameter(this.getParameterMap(request), request));
	List<Map<String, Object>> listCon = realNameInternetService.getSumRealNamePrvdCon(formatParameter(this.getParameterMap(request), request));
	List<Object> realPerList = new ArrayList<Object>();
	List<Object> auditList = new ArrayList<Object>();

	if (list != null && list.size() > 0) {
	    for (int i = 0; i < list.size(); i++) {
		Map<String, Object> eachMap = list.get(i);
		if (eachMap.get("real_name_per") != null) {
		    Double eachPer = Double.parseDouble(eachMap.get("real_name_per").toString());
		    realPerList.add(Double.parseDouble(df.format(eachPer * 100)));
		} else {
		    realPerList.add(0);
		}
		String eachMonth = eachMap.get("Aud_trm") + "";
		auditList.add(eachMonth);
	    }
	}

	Map<String, Object> returnMap = new HashMap<String, Object>();
	returnMap.put("realPerList", realPerList);
	returnMap.put("auditList", auditList);
	returnMap.put("listCon", listCon);
	return returnMap;
    }
    
    /**
     * <pre>
     * Desc  获取汇总信息：地图左边的数据
     * @return
     * @author
     * 2016-11-21 下午4:11:33
     * </pre>
     */
    @RequestMapping(value = "summaryDetails")
    @ResponseBody
    public Object getSummaryDetails(HttpServletRequest request, HttpServletResponse response) {
	Map<String, Object> params = formatParameter(this.getParameterMap(request), request);
	Map<String, Object> sumMap = new HashMap<String, Object>();
	 sumMap.put("auditMonth", request.getParameter("currSumEndDate"));
	 sumMap.put("prvdName", request.getParameter("currSumEndDate"));
	List<Map<String, Object>> list = this.realNameInternetService.getSumSort(params);
	
	if (list == null || list.size()==0) {
	    sumMap.put("noRealNameNum", "暂无数据");
	    sumMap.put("tolSubsNum", "暂无数据");
	    sumMap.put("realNamePer", "暂无数据");
	    sumMap.put("sort", "暂无数据");
	    sumMap.put("lastProvince", "暂无数据");
	    return sumMap;
	}
	for(int i = 0;i<list.size();i++){
	    Map<String, Object> eachMap = list.get(i);
	    String provinceCode = eachMap.get("provinceCode")==null?"":eachMap.get("provinceCode").toString();
	    if(provinceCode.equals(params.get("provinceCode").toString())){
		sumMap.putAll(eachMap);
		break;
	    }
	}
	// 排名最后3省
	List<Map<String, Object>> top3CityList = this.realNameInternetService.getTop3City(formatParameter(this.getParameterMap(request), request));
	StringBuffer lastProvince = new StringBuffer();
	for (Map<String, Object> cityInfo : top3CityList) {
	    lastProvince.append(cityInfo.get("CMCC_prvd_nm_short")).append(",");
	}
	if (lastProvince.length() > 0) {
	    sumMap.put("lastProvince", lastProvince.substring(0, lastProvince.length() - 1));
	}
	sumMap.put("provinceCode", params.get("provinceCode").toString());
	return sumMap;
    }
    
    /**
     * 获取地图信息
     * 
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author 
     * 2016-11-23 上午10:36:26
     * </pre>
     */
    @RequestMapping(value = "getMapData")
    @ResponseBody
    public Object getMapData(HttpServletRequest request) {

	List<Map<String, Object>> list = realNameInternetService.getSumCityAll(formatParameter(this.getParameterMap(request), request));
	List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
	for (Map<String, Object> map : list) {
	    Map<String, Object> value = new HashMap<String, Object>();
	    value.put("cityName", map.get("CMCC_prvd_nm_short"));
	    String perStr = map.get("real_name_per").toString();
	    Double per = Double.parseDouble(perStr) * 100;
	    value.put("perJfhk", Double.parseDouble(df.format(per)));
	    value.put("cmccProvId", map.get("CMCC_prov_id"));
	    values.add(value);
	}

	Prvd_info prvdInfo = Constants.getPrvdInfo(request.getParameter("provinceCode"));
	Map<String, Object> result = new HashMap<String, Object>();
	result.put("values", values);
	result.put("prvdPinYinName", prvdInfo.getPrivdcd().toLowerCase());
	result.put("provName", prvdInfo.getPrivdnm());
	return CommonResult.success(result);
    }
    
    
    /**
     * <pre>
     * Desc  
     * 地图后面的表格
     * @param response
     * @param request
     * @return
     * @author
     * 2016-11-22 下午2:59:35
     * </pre>
     */
    @RequestMapping(value = "sumCity")
    @ResponseBody
    public Object getSumCity(HttpServletResponse response, HttpServletRequest request, Pager pager) {

	pager.setParams(formatParameter(this.getParameterMap(request), request));
	List<Map<String, Object>> list = realNameInternetService.getSumCity(pager);
	pager.setDataRows(list);
	return pager;
    }
    
    /**
     * <pre>
     * Desc  导出数据表信息
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

	    List<Map<String, Object>> list = realNameInternetService.getSumCityAll(formatParameter(this.getParameterMap(request), request));
	    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	    if (list != null && list.size() > 0) {
		LinkedHashMap<String, Object> lhm = null;
		for (int i = 0; i < list.size(); i++) {
		    Map<String, Object> eachMap = list.get(i);
		    lhm = new LinkedHashMap<String, Object>();
		    String perStr = eachMap.get("real_name_per") == null ? "" : eachMap.get("real_name_per").toString();
		    if (!"".equals(perStr)) {
			Double per = Double.parseDouble(perStr) * 100;
			lhm.put("Aud_trm", eachMap.get("Aud_trm"));
			lhm.put("CMCC_prvd_nm_short", eachMap.get("CMCC_prvd_nm_short"));
			lhm.put("no_real_name_num", eachMap.get("no_real_name_num"));
			lhm.put("tol_subs_num", eachMap.get("tol_subs_num"));
			lhm.put("real_name_per", (df.format(per)) + "%");
			exportData.add(lhm);
		    }
		}
	    }
	    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    map.put("Aud_trm", "审计月");
	    map.put("CMCC_prvd_nm_short", "地市名称");
	    map.put("no_real_name_num", "未实名用户数");
	    map.put("tol_subs_num", "用户总数");
	    map.put("real_name_per", "实名比例");
	    
	    CSVUtils.exportCSVList("4.1.3_实名制登记率_存量物联网M2M用户实名制率_汇总.csv", exportData, map, request, response);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return null;
    }
    
    /**
     * <pre>
     * Desc  明细数据
     * 清单列表
     * @param response
     * @param request
     * @return
     * @author 
     * 2016-11-22 下午2:58:03
     * </pre>
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "detail")
    @ResponseBody
    public Pager detail(HttpServletResponse response, HttpServletRequest request, Pager pager) throws UnsupportedEncodingException {
    	request.setCharacterEncoding("utf-8") ;
		pager.setParams(formatParameter(this.getParameterMap(request), request));
		//判断pager是否有值
		List<Map<String, Object>> list = realNameInternetService.getRealNameDetailData(pager);
			
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
     * @author wangpeng
     * 2016-11-23 下午4:15:54
     * </pre>
     */
    @RequestMapping(value = "exportDetail")
    public void exportDetail(HttpServletResponse response, HttpServletRequest request)throws Exception {
		Map<String, Object> parameterMap = formatParameter(this.getParameterMap(request),request);
		realNameInternetService.getRealNameDetailDataDaoChu(request, response,parameterMap);
		
    }
    /**
     * <pre>
     * Desc  根据需要对页面参数进行格式化
     * @param requestParamsMap
     * @author peter.fu
     * 2017-01-05 5:27:07 PM
     * </pre>
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
