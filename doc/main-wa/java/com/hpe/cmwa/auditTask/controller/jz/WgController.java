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

import com.hpe.cmwa.auditTask.service.jz.wgService;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.CSVUtils;
import com.hpe.cmwa.util.HelperDate;


/**
 * 
 * @author gongtingting
 *
 */
@Controller
@RequestMapping("/wg/")
public class WgController extends BaseController {

	 @Autowired
	    private wgService   wgService;
	   
	    private DecimalFormat	   df = new DecimalFormat("######0.00");

    @RequestMapping(value = "indexAddress")
    public String indexAddress(HttpServletRequest request) {
	Map<String, Object> params =formatParameter(this.getParameterMap(request));
	request.getSession().setAttribute("tabType", params.get("tabType"));
	request.getSession().setAttribute("provinceCode", params.get("provinceCode"));
	return "auditTask/jz/zbzj/wg";
	
    }
    
    @RequestMapping(value = "index")
    public String index (HttpServletRequest request) {
	Map<String, Object> params =formatParameter(this.getParameterMap(request));
	request.getSession().setAttribute("tabType", params.get("tabType"));
	request.getSession().setAttribute("provinceCode", params.get("provinceCode"));
	if(request.getParameter("provinceCode").equals("10000")){
		return "auditTask/sjk/jy_wg/wgsjk";
	}else{
		return "auditTask/jy/zbzj/wg";
	}
    }
    @RequestMapping(value = "indexSox")
    public String indexSox (HttpServletRequest request) {
	Map<String, Object> params =formatParameter(this.getParameterMap(request));
	request.getSession().setAttribute("tabType", params.get("tabType"));
	request.getSession().setAttribute("provinceCode", params.get("provinceCode"));
	if(request.getParameter("provinceCode").equals("10000")){
		return "auditTask/sjk/htblyw/wgsjk";
	}else{
		return "auditTask/sox/htblyw/wg";
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
	//汇总下拉列表
	
	// 清单参数默认值
	defaultParams.put("currDetBeginDate", sessoin.getAttribute("beforeAcctMonth"));
	defaultParams.put("currDetEndDate", sessoin.getAttribute("endAcctMonth"));
	defaultParams.put("currCityName", "");
	defaultParams.put("currYwType", "");
	// 明细页面的下拉列表  地市名称  虚增收入类型
	defaultParams.put("ywTypeList", dict.getList("ywType"));
	defaultParams.put("cityNameList", this.getCityObjectList(sessoin.getAttribute("provinceCode") + ""));
	
	return defaultParams;
    }
    /**
     * Desc  加载虚增收入波动：趋势图  省汇总表统计周期内每月总虚增收入
     * @param request
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "monthTotalIncome")
    public Object getMonthTotalIncome(HttpServletRequest request) {
    Map<String, Object> params = formatParameter(this.getParameterMap(request));
	List<Map<String, Object>> list =  wgService.getMonthTotalIncome(params);
	Map<String, Object>   map=new HashMap<String, Object>();
	String smc="";
	if(list.size()>0){	
		map=list.get(0);
		smc=(String) map.get("provName");
	}
	//进行月份的计算
	//Map<String, Object>   canshumap=formatParameter(this.getParameterMap(request));
	int zongyue=0;
	String begin1= (String)params.get("currSumBeginDate");
	//空指针
	String s1 = begin1.substring(0,4);
	int begins1=Integer.parseInt(s1);
	String s2 = begin1.substring(4,6);
	int begins2=Integer.parseInt(s2);
	String end1= (String) params.get("currSumEndDate");
	String s3 = end1.substring(0,4);
	int ends3=Integer.parseInt(s3);
	String s4 = end1.substring(4,6);
	int ends4=Integer.parseInt(s4);
	if(ends3>begins1){
		int yue=(ends3-begins1)-1+12;
		 zongyue=yue+((ends4-begins2)+1);
	}else{
		 zongyue=(ends4-begins2)+1;
	}
	params.put("zongyue", zongyue);
	List<Map<String, Object>> list1 =  wgService.getMonthTotalIncomeAvg(params);
	List<Object> xzsrPerList = new ArrayList<Object>();
	List<Object> auditList = new ArrayList<Object>();
	List<Object> xzsrPerListAvg = new ArrayList<Object>();
	
	if (list != null && list.size() > 0||list != null && list.size() > 0) {
		    for (int i = 0; i < list.size(); i++) {
				Map<String, Object> eachMap = list.get(i);
				if (eachMap.get("busi_num") != null) {
				    Double eachPer = Double.parseDouble(eachMap.get("busi_num").toString());
				    xzsrPerList.add(Double.parseDouble(df.format(eachPer)));
				} else {
					xzsrPerList.add(0);
				}
				String eachMonth = eachMap.get("auditMonth") + "";
				auditList.add(eachMonth);
		    }
			for (int j = 0; j < list1.size(); j++) {
				Map<String, Object> eachMap1 = list1.get(j);
				if (eachMap1.get("pjsr") != null) {
				    Double eachPer = Double.parseDouble(eachMap1.get("pjsr").toString());
				    for (int i = 0; i < list.size(); i++) {
				    	 xzsrPerListAvg.add(Double.parseDouble(df.format(eachPer)));
				    }
				} else {
						xzsrPerListAvg.add(0);
				}
			}
	
	    }
	

	Map<String, Object> returnMap = new HashMap<String, Object>();
	returnMap.put("xzsrPerList", xzsrPerList);
	returnMap.put("auditList", auditList);
	returnMap.put("xzsrPerListAvg", xzsrPerListAvg);
	returnMap.put("provName", smc);
	
	return returnMap;
	
    }
    /**
     * Desc  加载审计结论
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getZheJieLun")
    public  Map<String, Object> getZheJieLun(HttpServletRequest request,HttpServletResponse response) {
    	Map<String, Object> params = formatParameter(this.getParameterMap(request));
  
    		Map<String, Object> map =  wgService.getZheJieLu(params);
    		return map;
    	    }
    @ResponseBody
    @RequestMapping(value = "getQianZheJieLun")
    public  Map<String, Object> getQianZheJieLun(HttpServletRequest request,HttpServletResponse response) {
    	
    		Map<String, Object> params = formatParameter(this.getParameterMap(request));
    	    //Map<String, Object> jiemap=formatParameter(this.getParameterMap(request));
    	   
    		Map<String, Object> map =  wgService.getQianZheJieLu(params);
    		return map;
    	    }

    /**
     * <pre>
     * Desc  获取地市汇总信息
     * 
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

	pager.setParams(formatParameter(this.getParameterMap(request)));
	
	List<Map<String, Object>> list =  wgService.getSumCity(pager);

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
     * 2016-11-22 下午7:09:16
     * </pre>
     */
    @RequestMapping(value = "exportSumCity")
    public Object exportSumCity(HttpServletResponse response, HttpServletRequest request, Map<String, Object> postData) {
	try {
		Map<String, Object> params = formatParameter(this.getParameterMap(request));
	    List<Map<String, Object>> list = this. wgService.getSumCityAll(params);
	    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	    if (list != null && list.size() > 0) {
		LinkedHashMap<String, Object> lhm = null;
		for (int i = 0; i < list.size(); i++) {
		    Map<String, Object> eachMap = list.get(i);
		    lhm = new LinkedHashMap<String, Object>();
		 
		    lhm.put("1", eachMap.get("Aud_trm") + "");
		    lhm.put("2", eachMap.get("cmcc_prov_prvd_id"));
		    lhm.put("3", eachMap.get("short_name"));
		    lhm.put("4", eachMap.get("cmcc_prov_id"));
		    lhm.put("5", eachMap.get("cmcc_prvd_nm_short"));
		    lhm.put("6", eachMap.get("BUSI_ACCE_TYP"));
		    lhm.put("7", eachMap.get("USER_NUM"));
		    lhm.put("8", eachMap.get("BUSI_NUM"));
		    lhm.put("9", eachMap.get("TOL_USER_NUM"));
		    lhm.put("10", eachMap.get("TOL_BUSI_NUM"));
		    Double eachDouble = eachMap.get("PER_USER_NUM") == null ? 0.00 : Double.parseDouble(eachMap.get("PER_USER_NUM").toString());
		    lhm.put("11", df.format(eachDouble * 100) + "%");
		    Double eachDouble1 = eachMap.get("PER_BUSI_NUM") == null ? 0.00 : Double.parseDouble(eachMap.get("PER_BUSI_NUM").toString());
		    lhm.put("12", df.format(eachDouble1 * 100) + "%");
		    exportData.add(lhm);
		}
	    }

	    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    map.put("1", "审计月");
	    map.put("2", "省代码");
	    map.put("3", "省名称");
	    map.put("4", "地市代码");
	    map.put("5", "地市名称");
	    map.put("6", "业务受理类型");
	    map.put("7", "违规办理用户数");
	    map.put("8", "违规办理业务笔数");
	    map.put("9", "办理用户总数");
	    map.put("10", "办理业务总笔数");
	    map.put("11", "违规办理用户占比(%)");
	    map.put("12", "违规办理笔数占比(%)");
	  
	    if("wg".equals(params.get("expTyp"))){
	    	
	    	CSVUtils.exportCSVList("违规订购_违规对欠费停机、预销号用户订购业务_汇总.csv", exportData, map, request, response);
	    }else{
	    	
	    	CSVUtils.exportCSVList("4.1.1_指标作假_违规对欠费停机、预销号用户订购业务_汇总.csv", exportData, map, request, response);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
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
     * 2016-11-23 下午4:15:54
     * </pre>
     */
    @RequestMapping(value = "exportDetail")
    public void exportDetail(HttpServletResponse response, HttpServletRequest request)throws Exception {
//		 Map<String, Object> params = formatParameter(this.getParameterMap(request));
//	    List<Map<String, Object>> list =  wgService.getWgDetailData(formatParameter(this.getParameterMap(request)));
		 Map<String, Object> params = this.getParameterMap(request);
		 String wg = request.getParameter("expTyp");
		 if(wg!=null){
			 params.put("expTyp", wg);
		 }
		 wgService.getWgDetailData(request,response,params);

    }
    /**
     * Desc  加载明细数据
     * @param request
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "getAllCityData")
    public Pager getAllCityData(HttpServletRequest request,HttpServletResponse response,Pager pager) {
    	
    		pager.setParams(formatParameter(this.getParameterMap(request)));	
    		
    		List<Map<String, Object>> list = wgService.getDetDetailData(pager);
    		
    		/*List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    		if (list != null && list.size() > 0) {
    		    for (int i = 0; i < list.size(); i++) {
    			Map<String, Object> eachMap = list.get(i);
    			
    			eachMap.put("BUSI_ACCE_TYP", dict.getMap("ywType").get(eachMap.get("BUSI_ACCE_TYP") + ""));
    			
    			resultList.add(eachMap);
    		    }
    		}*/
    		pager.setDataRows(list);
    		
    		return pager;
    	    }
    
    /**
     * Desc  加载地市汇总的柱状图
     * @param request
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "ZheZhu")
    public Map<String, Object> getZheZhu(HttpServletRequest request,HttpServletResponse response) {
    	Map<String, Object> params = formatParameter(this.getParameterMap(request));
    	List<Map<String, Object>>  list =  wgService.getOnloadZhu(params);
    	
    	List<Object> firstList = new ArrayList<Object>();
    	List<Object> secondList = new ArrayList<Object>();
    	List<Object> threeList = new ArrayList<Object>();
    	List<Object> otherList = new ArrayList<Object>();
    	List<Object> xList = new ArrayList<Object>();
    	
    	for(int i = 0; i < list.size(); i++){
    		params.put("type"+i, list.get(i).get("busi_acce_typ"));
    	}
    	
   	List<Map<String, Object>>  list1 =  wgService.getZhu(params);
    	
    	if (list1 != null && list1.size() > 0) {
    		    for (int i = 0; i < list1.size(); i++) {
    				Map<String, Object> eachMap = list1.get(i);
    				if (eachMap.get("firstyw") != null) {
    				   
    				    firstList.add(eachMap.get("firstyw"));
    				} else {
    					firstList.add(0);
    				}
    				if (eachMap.get("secondyw") != null) {
     				   
    					secondList.add(eachMap.get("secondyw"));
    				} else {
    					secondList.add(0);
    				}
    				if (eachMap.get("threeyw") != null) {
     				   
    					threeList.add(eachMap.get("threeyw"));
    				} else {
    					threeList.add(0);
    				}
    				if (eachMap.get("otheryw") != null) {
     				   
    					otherList.add(eachMap.get("otheryw"));
    				} else {
    					otherList.add(0);
    				}
    				String eachMonth = eachMap.get("cmcc_prvd_nm_short") + "";
    				xList.add(eachMonth);
    		    }
    	}
    	Map<String, Object> returnMap = new HashMap<String, Object>();
    	returnMap.put("firstList", firstList);
    	returnMap.put("secondList", secondList);
    	returnMap.put("threeList", threeList);
    	returnMap.put("otherList", otherList);
    	returnMap.put("xList", xList);
    	
    	Map<String, Object>  map=new HashMap<String, Object>();
		Map<String, Object>  map1=new HashMap<String, Object>();
		Map<String, Object>  map2=new HashMap<String, Object>();
		if (list != null && list.size() > 0){
	    	for(int i=0;i<list.size();i++){
				if(i==0){	
					map=list.get(0);
				}else if(i==1){
					map1=list.get(1);
				}else if(i==2){
					map2=list.get(2);
				}
			}
	    	if(map!= null)
	    	{	
	    		returnMap.put("firstMap",map.get("busi_acce_typ"));
	    	}
	    	if(map1!= null)
	    	{	
	    		returnMap.put("secondMap",map1.get("busi_acce_typ"));
	    	}
	    	if(map2!= null)
	    	{	
	    		returnMap.put("threeMap",map2.get("busi_acce_typ"));
	    	}
		}
    	
    	return returnMap;
    } 
    /**
     * Desc  加载柱状图审计结论
     * @param request
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "getQianZhuJieLun")
    public  Map<String, Object> getQianZhuJieLun(HttpServletRequest request,HttpServletResponse response) {
    	
    		List<Map<String, Object>>  list =  wgService.getQianZhu(formatParameter(this.getParameterMap(request)));
    		Map<String, Object>  map=new HashMap<String, Object>();
    		if(list != null){
   	
    			map=list.get(0);
    		}
    		
    		Map<String, Object> returnMap = new HashMap<String, Object>();
    		if(map !=null){
    			
    			returnMap.put("zyhs",map.get("sum(BUSI_NUM)"));
    			returnMap.put("wgzs",map.get("sum(USER_NUM)"));
    		}
      
        	return returnMap;
    	    }
    @ResponseBody
    @RequestMapping(value = "getZhongZhuJieLun")
    public  Map<String, Object> getZhongZhuJieLun(HttpServletRequest request,HttpServletResponse response) {
    	
    		List<Map<String, Object>>  list =  wgService.getZhongZhu(formatParameter(this.getParameterMap(request)));
    		Map<String, Object>  map=new HashMap<String, Object>();
    		Map<String, Object>  map1=new HashMap<String, Object>();
    		Map<String, Object>  map2=new HashMap<String, Object>();
    		Map<String, Object> returnMap = new HashMap<String, Object>();
    		if (list != null && list.size() > 0){
	    		for(int i=0;i<list.size();i++){
	    			if(i==0){	
	    				map=list.get(0);
	    			}else if(i==1){
	    				map1=list.get(1);
	    			}else if(i==2){
	    				map2=list.get(2);
	    			}
	    		}
    			if(map!= null )
        		{	
        			returnMap.put("onebs",map.get("ywbs"));
                	returnMap.put("oneyw",map.get("BUSI_ACCE_TYP"));
        		}
        		if(map1!= null)
        		{	
        			returnMap.put("twobs",map1.get("ywbs"));
                	returnMap.put("twoyw",map1.get("BUSI_ACCE_TYP"));
        		}
        		if(map2!= null)
        		{	
        			returnMap.put("thrbs",map2.get("ywbs"));
                	returnMap.put("thryw",map2.get("BUSI_ACCE_TYP"));
        		}
    		}
        	return returnMap;
    }
    @ResponseBody
    @RequestMapping(value = "getHouZhuJieLun")
    public List<Map<String, Object>> getHouZhuJieLun(HttpServletRequest request,HttpServletResponse response) {
    	
    	    List<Map<String, Object>> list =  wgService.getHouZhu(formatParameter(this.getParameterMap(request)));
	    	  
	    		
        	return list;
    	    }
    /**
	 * <pre>
	 * Desc  根据需要对页面参数进行格式化
	 * @param requestParamsMap
	 * @author peter.fu
	 * Nov 25, 2016 5:27:07 PM
	 * </pre>
	 */
	private Map<String, Object> formatParameter(Map<String, Object> requestParamsMap) {

		if (requestParamsMap == null) {
			return null;
		} else {
			// 格式化时间等
			for (String key : requestParamsMap.keySet()) {
				if (key.equals("currSumBeginDate") || key.equals("currSumEndDate") || key.equals("currDetBeginDate") || key.equals("currDetEndDate")) {
					HelperDate.formatDateStrToStr(requestParamsMap.get(key).toString(), "yyyyMM");
				}
			}
		}

		return requestParamsMap;
	}
    
    
}

