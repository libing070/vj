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

import com.hpe.cmwa.auditTask.service.jz.ZDBT7002Service;
import com.hpe.cmwa.common.CommonResult;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.CSVUtils;
import com.hpe.cmwa.util.Json;
import com.hpe.cmwa.util.Prvd_info;

@Controller
@RequestMapping(value = "/zdbt7002/")
public class ZDBT7002Controller extends BaseController {

    @Autowired
    private ZDBT7002Service ZDBT7002Service;

    private DecimalFormat   df = new DecimalFormat("######0.00");

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
    @RequestMapping(value = "index")
    public String index(HttpServletRequest request) {
	Map<String, Object> params =formatParameter(this.getParameterMap(request), request);
//	request.getSession().setAttribute("tabType", params.get("tabType"));
	return "auditTask/jz/zdbt/zdbt7002";
    }
    @RequestMapping(value = "zdbt7002")
    public String zdbt7002(HttpServletRequest request) {
	Map<String, Object> params =formatParameter(this.getParameterMap(request), request);
//	request.getSession().setAttribute("tabType", params.get("tabType"));
	return "auditTask/jy/wgzdbt/zdbt7002";
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
	defaultParams.put("tabType", sessoin.getAttribute("tabType"));
	// 清单参数默认值
	defaultParams.put("currDetBeginDate", sessoin.getAttribute("beforeAcctMonth"));
	defaultParams.put("currDetEndDate", sessoin.getAttribute("endAcctMonth"));
	
	if (request.getSession().getAttribute("provinceCode") != null) {
		if(request.getParameter("provinceCode")!=null){
			defaultParams.put("cityList", this.getCityObjectList(request.getParameter("provinceCode") + ""));
		}else{
			defaultParams.put("cityList", this.getCityObjectList(sessoin.getAttribute("provinceCode") + ""));
		}
	}else{
		defaultParams.put("cityList", this.getCityObjectList(sessoin.getAttribute("provinceCode") + ""));
	}
	
	defaultParams.put("marketingTypeList", this.dict.getList("marketingType"));
	defaultParams.put("marketingTypeMap", this.dict.getMap("marketingType"));
	defaultParams.put("xnumber", "1");
	defaultParams.put("ynumber", "5");
	return defaultParams;
    }
    
    @ResponseBody
    @RequestMapping(value = "getSumPrvdinceCon")
    public Object getSumPrvdinceCon(HttpServletRequest request){
    	Map<String, Object> listCon = ZDBT7002Service.getSumPrvdinceCon(formatParameter(this.getParameterMap(request), request));
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

	List<Map<String, Object>> list = ZDBT7002Service.getSumPrvdince(formatParameter(this.getParameterMap(request), request));

	if (list != null && list.size() > 0) {
	    for (int i = 0; i < list.size(); i++) {
		Map<String, Object> eachMap = list.get(i);
		if (eachMap.get("perImei") != null && !"".equals(eachMap.get("perImei").toString())) {
		    Double eachDouble = Double.parseDouble(eachMap.get("perImei").toString());
		    Y1List.add(Double.parseDouble(df.format(eachDouble * 100)));
		} else {
		    Y1List.add(0);
		}
		if (eachMap.get("imeiCnt2") != null && !"".equals(eachMap.get("imeiCnt2").toString())) {
		    Double eachDouble = Double.parseDouble(eachMap.get("imeiCnt2").toString());
		    Y2List.add(eachDouble);
		} else {
		    Y2List.add(0);
		}
		audTrmList.add(eachMap.get("offerNm"));
	    }
	}
	Map<String, Object> returnMap = new HashMap<String, Object>();

	returnMap.put("Y1List", Y1List);
	returnMap.put("Y2List", Y2List);

	returnMap.put("audTrmList", audTrmList);

	return returnMap;
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
	List<Map<String, Object>> list = ZDBT7002Service.getSumSort(params);
	sumMap.put("auditMonth", request.getParameter("currSumEndDate"));
	if (list == null || list.size()==0) {
	    sumMap.put("imeiCnt1", "暂无数据");
	    sumMap.put("imeiCnt2", "暂无数据");
	    sumMap.put("sort", "暂无数据");
	    sumMap.put("top3City", "暂无数据");
	    return sumMap;
	}
	for(int i = 0 ;i<list.size();i++){
	    Map<String, Object> eachMap = list.get(i);
	    String provinceCode = eachMap.get("CMCC_PROV_PRVD_ID")==null?"":eachMap.get("CMCC_PROV_PRVD_ID").toString();
	    if(provinceCode.equals(params.get("provinceCode").toString())){
		sumMap.putAll(eachMap);
		break;
	    }
	}
	// top3 city
	List<Map<String, Object>> top3CityList = this.ZDBT7002Service.getTop3City(params);
	StringBuffer top3City = new StringBuffer();
	for (Map<String, Object> cityInfo : top3CityList) {
	    top3City.append(cityInfo.get("CMCC_PRVD_NM_SHORT")).append(",");
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
	List<Map<String, Object>> list = this.ZDBT7002Service.getSumCity(pager);
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
	List<Map<String, Object>> list = ZDBT7002Service.getDetailList(pager);
	/*List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
	if(list!=null&&list.size()>0){
	    for (int i = 0; i < list.size(); i++) {
		Map<String, Object> eachMap = list.get(i);
		String s1 = eachMap.get("offerCls").toString().trim();
		eachMap.put("offerCls",this.dict.getMap("marketingType").get(s1));
		resultList.add(eachMap);
	    }
	}*/
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
	Map<String, Object> params = formatParameter(this.getParameterMap(request), request);
	List<Map<String, Object>> list = ZDBT7002Service.getSumCityByMap(params);
	List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
	Map<String, Object> value = null;
	for (int i = 0; i < list.size(); i++) {
	    Map<String, Object> map = list.get(i);
	    value = new HashMap<String, Object>();
	    value.put("cityName", map.get("shortName"));
	    value.put("cmccProvId", map.get("cmccProvId"));
	    Double eachDouble = map.get("perImei") == null ? 0.00 : Double.parseDouble(map.get("perImei").toString());
	    value.put("perImei", Double.parseDouble(df.format(eachDouble * 100)));
	    values.add(value);
	}
	//计算全国违规终端数量占比
	Map<String, Object> map = ZDBT7002Service.getImeiNumPer(params);
	if (map != null) {
	    Double imeiCnt2 = Double.parseDouble(map.get("imeiCnt2").toString());
	    Double imeiCnt1 = Double.parseDouble(map.get("imeiCnt1").toString());
	    Double per = imeiCnt2 / imeiCnt1;
	    result.put("xnumber", Double.parseDouble(df.format(per * 100)));
	    result.put("ynumber", Double.parseDouble(df.format(per * 5 * 100)));
	} else {
	    result.put("xnumber", 100);
	    result.put("ynumber", 1000);
	}
//	Prvd_info prvdInfo = Constants.getPrvdInfo(session.getAttribute("provinceCode")+"");
	Prvd_info prvdInfo = Constants.getPrvdInfo(params.get("provinceCode")+"");
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
    	 ZDBT7002Service.getDetailAll(request,response,params);
    	 
    }

    /**
     * <pre>
     * Desc  导出地市实名制登记率汇总信息
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
	    Map<String, Object> params = formatParameter(this.getParameterMap(request), request);
	    List<Map<String, Object>> list = this.ZDBT7002Service.getSumCityAll(params);
	    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	    if (list != null && list.size() > 0) {
		LinkedHashMap<String, Object> lhm = null;
		for (int i = 0; i < list.size(); i++) {
		    Map<String, Object> eachMap = list.get(i);
		    lhm = new LinkedHashMap<String, Object>();
		    lhm.put("1",  params.get("currSumBeginDate")+"-"+params.get("currSumEndDate"));
		    lhm.put("2", eachMap.get("cmccPrvdNmShort"));
		    lhm.put("3", eachMap.get("offerId"));
		    lhm.put("4", eachMap.get("offerNm"));
		    lhm.put("5", eachMap.get("offerCls"));
		    lhm.put("6", eachMap.get("imeiCnt2"));
		    lhm.put("7", eachMap.get("imeiCnt1"));
		    Double eachDouble = eachMap.get("perImei") == null ? 0.00 : Double.parseDouble(eachMap.get("perImei").toString());
		    lhm.put("8", df.format(eachDouble * 100));
		    exportData.add(lhm);
		}
	    }

	    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    map.put("1", "审计区间");
	    map.put("2", "地市名称");
	    map.put("3", "营销案编码");
	    map.put("4", "营销案名称");
	    map.put("5", "营销案种类");
	    map.put("6", "异常终端数");
	    map.put("7", "终端总数");
	    map.put("8", "异常终端IMEI数占比(%)");
	   
	    CSVUtils.exportCSVList("4.2.4_终端综合补贴合规性_是否严格执行终端酬金与4G流量挂钩_汇总.csv", exportData, map, request, response);
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
		if(request.getParameter("provinceCode")!=null){
			requestParamsMap.put("provinceCode", request.getParameter("provinceCode"));
		}else{
			requestParamsMap.put("provinceCode", request.getSession().getAttribute("provinceCode"));
		}
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
	if (requestParamsMap.get("currSettMonth") != null && !"".equals(requestParamsMap.get("currSettMonth").toString())) {
	    Object currSettMonth = requestParamsMap.get("currSettMonth");
	    currSettMonth = currSettMonth.toString().replace("-", "");
	    requestParamsMap.put("currSettMonth", currSettMonth);
	}

	return requestParamsMap;
    }
}
