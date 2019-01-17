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

import com.hpe.cmwa.auditTask.service.jz.XzsrService;
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
@RequestMapping("/xzsr/")
public class XzsrController extends BaseController {

    @Autowired
    private XzsrService   xzsrService;
   
    private DecimalFormat	   df = new DecimalFormat("######0.00");

    @RequestMapping(value = "zbzjXzsr")
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
	request.getSession().setAttribute("SumDown", "0");
	if(request.getParameter("provinceCode").equals("10000")){
		url = "auditTask/sjk/zbzj/xzsr";
	}else{
		url = "auditTask/jz/zbzj/xzsr";
	}
	return url;
    }
    
    @RequestMapping(value = "indexAddress")
    public String indexAddress(HttpServletRequest request) {
	Map<String, Object> params =formatParameter(this.getParameterMap(request));
	request.getSession().setAttribute("tabType", params.get("tabType"));
	return "auditTask/jz/zbzj/xzsr";
	
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
	defaultParams.put("currXzsrType", "");
	// 明细页面的下拉列表  地市名称  虚增收入类型
	defaultParams.put("xzsrTypeList", dict.getList("xzsrType"));
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
	List<Map<String, Object>> list =  xzsrService.getMonthTotalIncome(formatParameter(this.getParameterMap(request)));
	Map<String, Object>   map=new HashMap<String, Object>();
	//判断如果后台没有数据
	String smc="";
	if (list != null && list.size() > 0) {
			map=list.get(0);
			smc=(String) map.get("provName");
		}
	//进行月份的计算
	Map<String, Object>   canshumap=formatParameter(this.getParameterMap(request));
	int zongyue=0;
	String begin1= (String)canshumap.get("currSumBeginDate");
	//空指针
	String s1 = begin1.substring(0,4);
	int begins1=Integer.parseInt(s1);
	String s2 = begin1.substring(4,6);
	int begins2=Integer.parseInt(s2);
	String end1= (String) canshumap.get("currSumEndDate");
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
	
	canshumap.put("zongyue", zongyue);
	List<Map<String, Object>> list1 =  xzsrService.getMonthTotalIncomeAvg(canshumap);
	List<Object> xzsrPerList = new ArrayList<Object>();
	List<Object> auditList = new ArrayList<Object>();
	List<Object> xzsrPerListAvg = new ArrayList<Object>();
	
	if (list != null && list.size() > 0||list != null && list.size() > 0) {
		    for (int i = 0; i < list.size(); i++) {
				Map<String, Object> eachMap = list.get(i);
				if (eachMap.get("tol_vt_amt") != null) {	
				    Double eachPer = Double.parseDouble(eachMap.get("tol_vt_amt").toString());
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
				  
				    for (int i = 0; i < list.size(); i++) {
				    	 Double eachPer = Double.parseDouble(eachMap1.get("pjsr").toString());
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
    @RequestMapping(value = "getQianZheJieLun")
    public  Map<String, Object> getQianZheJieLun(HttpServletRequest request,HttpServletResponse response) {
    		Map<String, Object> map =  xzsrService.getQianZheJieLun(formatParameter(this.getParameterMap(request)));
    		return map;
    }

    @ResponseBody
    @RequestMapping(value = "getZheJieLun")
    public  Map<String, Object> getZheJieLun(HttpServletRequest request,HttpServletResponse response) {
    	
    	
    		Map<String, Object> map =  xzsrService.getZheJieLun(formatParameter(this.getParameterMap(request)));
    		
    		return map;
   }

    /**
     * Desc  加载地市汇总的柱状图
     * @param request
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "ZheZhu")
    public  Map<String, Object> getZheZhu(HttpServletRequest request,HttpServletResponse response) {
    	List<Map<String, Object>> list =  xzsrService.getOnloadZhu(formatParameter(this.getParameterMap(request)));
    	
    	//柱状图的集合
    	List<Object> ykList = new ArrayList<Object>();
    	List<Object> csList = new ArrayList<Object>();
    	List<Object> gmList = new ArrayList<Object>();
    	List<Object> bsList = new ArrayList<Object>();
    	List<Object> xList = new ArrayList<Object>();
    	
    	if (list != null && list.size() > 0||list != null && list.size() > 0) {
    		    for (int i = 0; i < list.size(); i++) {
    				Map<String, Object> eachMap = list.get(i);
    				if (eachMap.get("vt_yk_amt") != null) {
    				    ykList.add(eachMap.get("vt_yk_amt"));
    				} else {
    					ykList.add(0);
    				}
    				if (eachMap.get("vt_test_amt") != null) {
    				    csList.add(eachMap.get("vt_test_amt"));
    				} else {
    					csList.add(0);
    				}
    				if (eachMap.get("vt_free_amt") != null) {
    				    gmList.add(eachMap.get("vt_free_amt"));
    				} else {
    					gmList.add(0);
    				}
    				if (eachMap.get("vt_emp_amt") != null) {
    				    bsList.add(eachMap.get("vt_emp_amt"));
    				} else {
    					bsList.add(0);
    				}
    				String eachds = eachMap.get("dsmc") + "";
    				xList.add(eachds);
    		      }
    			
    			}
    	
			    	Map<String, Object> returnMap = new HashMap<String, Object>();
			    	returnMap.put("ykList", ykList);
			    	returnMap.put("csList", csList);
			    	returnMap.put("gmList", gmList);
			    	returnMap.put("bsList", bsList);
			    	returnMap.put("xList", xList);

			    	
			    	return returnMap; 
    	
    	    } 
    
    /**
     * Desc  加载柱状图审计结论
     * @param request
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "getZhuJieLun")
    public  Map<String, Object> getZhuJieLun(HttpServletRequest request,HttpServletResponse response) {
    	
    	    List<Map<String, Object>> list =  xzsrService.getZhuJieLun(formatParameter(this.getParameterMap(request)));
    	    List<Object> disList = new ArrayList<Object>();
        	List<Object> yangkList = new ArrayList<Object>();
        	List<Object> gongmList = new ArrayList<Object>();
        	List<Object> bensList = new ArrayList<Object>();
        	List<Object> cesList = new ArrayList<Object>();
        	List<Object> zxzsrList = new ArrayList<Object>();
        	
    		if (list != null && list.size() > 0||list != null && list.size() > 0) {
    		    for (int i = 0; i < list.size(); i++) {
	    				Map<String, Object> eachMap = list.get(i);
	    				if (eachMap.get("cmcc_prvd_nm_short") != null) {
	    					 disList.add(eachMap.get("cmcc_prvd_nm_short"));
	    				} else {
	    					 disList.add(0);
	    				}
	    				if (eachMap.get("VT_YK_AMT") != null) {
	    					yangkList.add(eachMap.get("VT_YK_AMT"));
	    				} else {
	    					yangkList.add(0);
	    				}
	    				if (eachMap.get("VT_TEST_AMT") != null) {
	    				    cesList.add(eachMap.get("VT_TEST_AMT"));
	    				} else {
	    					cesList.add(0);
	    				}
	    				if (eachMap.get("VT_FREE_AMT") != null) {
	    				    gongmList.add(eachMap.get("VT_FREE_AMT"));
	    				} else {
	    					gongmList.add(0);
	    				}
	    				if (eachMap.get("VT_EMP_AMT") != null) {
	    				    bensList.add(eachMap.get("VT_EMP_AMT"));
	    				} else {
	    					bensList.add(0);
	    				}
	    				if (eachMap.get("TOL_VT_AMT") != null) {
	    					zxzsrList.add(eachMap.get("TOL_VT_AMT"));
	    				} else {
	    					zxzsrList.add(0);
	    				}
    		    	}	
    		  }
    		    Map<String, Object> returnMap = new HashMap<String, Object>();
		    	returnMap.put("yangkList", yangkList);
		    	returnMap.put("cesList", cesList);
		    	returnMap.put("gongmList", gongmList);
		    	returnMap.put("bensList", bensList);
		    	returnMap.put("zxzsrList", zxzsrList);
		    	returnMap.put("disList", disList);
		    	
		    	
		    	return returnMap; 
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
	
	List<Map<String, Object>> list =  xzsrService.getSumCity(pager);

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
	    List<Map<String, Object>> list = this. xzsrService.getSumCityAll(params);
	    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	    if (list != null && list.size() > 0) {
		LinkedHashMap<String, Object> lhm = null;
		for (int i = 0; i < list.size(); i++) {
		    Map<String, Object> eachMap = list.get(i);
		    lhm = new LinkedHashMap<String, Object>();
		    lhm.put("1", eachMap.get("Aud_trm"));
		    lhm.put("2", eachMap.get("short_name"));
		    lhm.put("3", eachMap.get("cmcc_prov_prvd_id"));
		    lhm.put("4", eachMap.get("cmcc_prvd_nm_short"));
		    lhm.put("5", eachMap.get("cmcc_prov_id"));
		    lhm.put("6", eachMap.get("YK_USER_NUM"));
		    lhm.put("7", eachMap.get("TEST_USER_NUM"));
		    lhm.put("8", eachMap.get("FREE_USER_NUM"));
		    lhm.put("9", eachMap.get("EMP_USER_NUM"));
		    lhm.put("10", eachMap.get("VT_YK_AMT"));
		    lhm.put("11", eachMap.get("VT_TEST_AMT"));
		    lhm.put("12", eachMap.get("VT_FREE_AMT"));
		    lhm.put("13", eachMap.get("VT_EMP_AMT"));
		    lhm.put("14", eachMap.get("TOL_VT_AMT"));
		    lhm.put("15", eachMap.get("TOL_AMT"));
		    Double eachDouble = eachMap.get("PER_AMT") == null ? 0.00 : Double.parseDouble(eachMap.get("PER_AMT").toString());
		    lhm.put("16", df.format(eachDouble * 100) + "%");
		    
		    exportData.add(lhm);
		}
	    }

	    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    map.put("1", "审计月");
	    map.put("2", "省名称");
	    map.put("3", "省代码");
	    map.put("4", "地市名称");
	    map.put("5", "地市代码");
	    map.put("6", "养卡用户数");
	    map.put("7", "违规测试用户");
	    map.put("8", "违规公免用户");
	    map.put("9", "违规本省移动公司用户数");
	    map.put("10", "养卡用户虚增收入(元)");
	    map.put("11", "测试用户虚增收入(元)");
	    map.put("12", "公免用户虚增收入(元)");
	    map.put("13", "本省移动公司用户虚增收入(元)");
	    map.put("14", "总虚增收入(元)");
	    map.put("15", "总出账收入(元)");
	    map.put("16", "虚增收入占比(%)");
	   
	    CSVUtils.exportCSVList("4.1.1_指标作假_虚增收入_汇总.csv", exportData, map, request, response);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
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
    		
    		List<Map<String, Object>> list =  xzsrService.getDetDetailData(pager);
    		
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
     * 2016-11-23 下午4:15:54
     * </pre>
     */
    @RequestMapping(value = "exportDetail")
    public void exportDetail(HttpServletResponse response, HttpServletRequest request) throws Exception{
    	
    	Map<String, Object> params = this.getParameterMap(request);
    	xzsrService.getXzsrDetailData(request,response,params);
	

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

