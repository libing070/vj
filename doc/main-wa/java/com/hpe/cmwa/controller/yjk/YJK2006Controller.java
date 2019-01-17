/**
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.controller.yjk;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.jz.YJK2006Service;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.CSVUtils;

/**
 * <pre>
 * 有价卡合规性 - 集中激活分析
 * 访问路径：http://localhost:7001/cmwa/yjk/2001/index
 * @author   Huang Tao
 * @date     Nov 22, 2016 13:48:35 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Nov 22, 2016 	   Huang Tao 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/yjk/2006/")
public class YJK2006Controller extends BaseController {

    @Autowired
    private YJK2006Service yjk2006Service;
    
    private DecimalFormat df = new DecimalFormat("######0.00");

    @RequestMapping(value = "/index")
    public void index() {
	
    }
    /**
     * 获取有价卡真实性信息
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-17 下午2:52:56
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getYJKRealInfo")
    public List<Map<String, Object>> getYJKRealInfo(HttpServletRequest request){
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<Map<String, Object>> list = this.yjk2006Service.getYJKRealInfo(parameterMap);
	return list;
    }
    
    /**
     * 获取集中度分析
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-17 下午7:17:36
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getYJKFocus")
    public List<Map<String, Object>> getYJKFocus(HttpServletRequest request){
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<Map<String, Object>> list = this.yjk2006Service.getYJKFocus(parameterMap);
	return list;
    }
    /**
     * 获取集中度分析明細信息
     * <pre>
     * Desc  
     * @param request
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-17 下午8:06:38
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getYJKFocusInfo")
    public Pager getYJKFocusInfo(HttpServletRequest request,Pager pager){
	Map<String, Object> parameterMap = this.getParameterMap(request);
	pager.setParams(parameterMap);
	List<Map<String, Object>> list = this.yjk2006Service.getYJKFocusInfo(pager);
	pager.setDataRows(list);
	return pager;
    }
    /**
     * 
     * <pre>
     * Desc  
     * @param request
     * @param response
     * @throws Exception
     * @author wangpeng
     * 2017-1-18 上午10:38:39
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/exportFocusTab")
    public void exportFocusTab(HttpServletRequest request, HttpServletResponse response) throws Exception {
	try {
	    Map<String, Object> parameterMap = this.getParameterMap(request);
	    List<Map<String, Object>> list = yjk2006Service.getYJKFocusInfoAll(parameterMap);
	    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	    if (list != null && list.size() > 0) {
		LinkedHashMap<String, Object> lhm = null;
		for (int i = 0; i < list.size(); i++) {
		    Map<String, Object> eachMap = list.get(i);
		    lhm = new LinkedHashMap<String, Object>();
		    lhm.put("1", parameterMap.get("hz_startMonth")+"-"+parameterMap.get("hz_endMonth"));
		    lhm.put("2", eachMap.get("msisdn"));
		    lhm.put("3", eachMap.get("user_type"));
		    lhm.put("4", eachMap.get("org_nm"));
		    lhm.put("5", eachMap.get("yjk_amt"));
		    exportData.add(lhm);
		}
	    }
	    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    map.put("1", "审计月");
	    map.put("2", "赠送号码");
	    map.put("3", "类型（个人，集团）");
	    map.put("4", "集团用户名称");
	    map.put("5", "赠送总金额");
	    String fileName="";
	    if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("boss")) {
		fileName = "4.1.4_有价卡合规性_赠送对象分析_赠送集中度分析_总部BOSS数据.csv";
	    } else {
		fileName = "4.1.4_有价卡合规性_赠送对象分析_赠送集中度分析_手工电子台账数据.csv";
	    }
	    CSVUtils.exportCSVList(fileName, exportData, map, request, response);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    /**
     * 赠送非中高端客户、非集团客户
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-18 下午2:48:20
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getZsfzgfjtCustomer")
    public Map<String, Object> getZsfzgfjtCustomer(HttpServletRequest request){
	Map<String, Object> parameterMap = this.getParameterMap(request);
	Map<String, Object> conMap = this.yjk2006Service.getZsfzgfjtCustomerCon(parameterMap);
	List<Map<String, Object>> list = this.yjk2006Service.getZsfzgfjtCustomer(parameterMap);
	
	Map<String, Object> returnMap = new HashMap<String, Object>();
	returnMap.put("dataList", list);
	returnMap.put("yjkAmt", conMap);
	
	return returnMap;
    }
    /**
     * 赠送非中高端客户、非集团客户明细
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-18 下午3:17:18
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getZsfzgfjtCustomerInfo")
    public Pager getZsfzgfjtCustomerInfo(HttpServletRequest request,Pager pager){
	Map<String, Object> parameterMap = this.getParameterMap(request);
	pager.setParams(parameterMap);
	
	List<Map<String, Object>> list = this.yjk2006Service.getZsfzgfjtCustomerInfo(pager);
	pager.setDataRows(list);
	
	return pager;
    }
    /**
     * 导出赠送非中高端非集团客户有价卡信息
     * <pre>
     * Desc  
     * @param request
     * @param response
     * @throws Exception
     * @author wangpeng
     * 2017-1-18 下午3:44:48
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/exportFzgfjtTab")
    public void exportFzgfjtTab(HttpServletRequest request, HttpServletResponse response) throws Exception {
	try {
	    Map<String, Object> parameterMap = this.getParameterMap(request);
	    List<Map<String, Object>> list = yjk2006Service.getZsfzgfjtCustomerExport(parameterMap);
	    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	    if (list != null && list.size() > 0) {
		LinkedHashMap<String, Object> lhm = null;
		for (int i = 0; i < list.size(); i++) {
		    Map<String, Object> eachMap = list.get(i);
		    lhm = new LinkedHashMap<String, Object>();
		    lhm.put("1", parameterMap.get("hz_startMonth")+"-"+parameterMap.get("hz_endMonth"));
		    lhm.put("2", eachMap.get("yjk_offer_cd"));
		    lhm.put("3", eachMap.get("yjk_offer_nm"));
		    lhm.put("4", eachMap.get("sum_yjk_amt"));
		    exportData.add(lhm);
		}
	    }
	    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    map.put("1", "审计月");
	    map.put("2", "营销案编号");
	    map.put("3", "营销案名称");
	    map.put("4", "集团用户名称");
	    String fileName="";
	    if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("boss")) {
		fileName = "4.1.4_有价卡合规性_赠送对象分析_赠送非中高端客户、非集团客户分析_总部BOSS数据.csv";
	    } else {
		fileName = "4.1.4_有价卡合规性_赠送对象分析_赠送非中高端客户、非集团客户分析_手工电子台账数据.csv";
	    }
	    CSVUtils.exportCSVList(fileName, exportData, map, request, response);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    /**
     * 获取赠送用途分析汇总数据
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-18 下午4:13:46
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getSumPurpose")
    public List<Map<String, Object>> getSumPurpose(HttpServletRequest request){
	Map<String, Object> parameterMap = this.getParameterMap(request);
	List<Map<String, Object>> list = this.yjk2006Service.getSumPurpose(parameterMap);
	return list;
    }
    /**
     * 赠送员工分析
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-18 下午7:01:02
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getSumStaff")
    public Map<String, Object> getSumStaff(HttpServletRequest request){
	Map<String, Object> parameterMap = this.getParameterMap(request);
	Map<String, Object> map = this.yjk2006Service.getSumStaff(parameterMap);
	return map;
    }
    
    /**
     * 获取赠送员工明细信息
     * <pre>
     * Desc  
     * @param request
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-18 下午7:58:20
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getPurposeInfo")
    public Pager getPurposeInfo(HttpServletRequest request,Pager pager){
	Map<String, Object> parameterMap = this.getParameterMap(request);
	pager.setParams(parameterMap);
	
	List<Map<String, Object>> list = this.yjk2006Service.getStaffInfo(pager);
	pager.setDataRows(list);
	
	return pager;
    }
    
    @ResponseBody
    @RequestMapping(value = "/exportStaff")
    public void exportStaff(HttpServletRequest request, HttpServletResponse response) throws Exception {
	try {
	    Map<String, Object> parameterMap = this.getParameterMap(request);
	    List<Map<String, Object>> list = yjk2006Service.getStaffInfoAll(parameterMap);
	    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	    if (list != null && list.size() > 0) {
		LinkedHashMap<String, Object> lhm = null;
		for (int i = 0; i < list.size(); i++) {
		    Map<String, Object> eachMap = list.get(i);
		    lhm = new LinkedHashMap<String, Object>();
		    lhm.put("1", parameterMap.get("hz_startMonth")+"-"+parameterMap.get("hz_endMonth"));
		    lhm.put("2", eachMap.get("msisdn"));
		    lhm.put("3", eachMap.get("emp_name"));
		    lhm.put("4", eachMap.get("emp_dept"));
		    lhm.put("5", eachMap.get("emp_stat"));
		    lhm.put("6", eachMap.get("sum_yjk_amt"));
		    exportData.add(lhm);
		}
	    }
	    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    map.put("1", "审计月");
	    map.put("2", "赠送电话号码");
	    map.put("3", "员工名称");
	    map.put("4", "部门");
	    map.put("5", "在职/离职");
	    map.put("6", "赠送金额");
	    String fileName="";
	    if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("boss")) {
		fileName = "4.1.4_有价卡合规性_赠送对象分析_向员工赠送分析_总部BOSS数据.csv";
	    } else {
		fileName = "4.1.4_有价卡合规性_赠送对象分析_向员工赠送分析_手工电子台账数据.csv";
	    }
	    CSVUtils.exportCSVList(fileName, exportData, map, request, response);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    /**
     * 获取金额汇总信息
     * <pre>
     * Desc  
     * @param request
     * @return
     * @author wangpeng
     * 2017-1-19 上午10:48:40
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getSumMoney")
    public Map<String, Object> getSumMoney(HttpServletRequest request){
	Map<String, Object> parameterMap = this.getParameterMap(request);
	Map<String, Object> odsMapMin = this.yjk2006Service.getSumOdsMoneyMin(parameterMap);
	Map<String, Object> sumMap = this.yjk2006Service.getSumMoney(parameterMap);
	Double odsSum = 0.00;
	Double sum1 = 0.00;
	Double sum2 = 0.00;
	Double sum3 = 0.00;
	Double sum4 = 0.00;
	
	Double ods1 = 0.00;
	Double ods2 = 0.00;
	Double ods3 = 0.00;
	Double ods4 = 0.00;
	
	List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
	if(odsMapMin!=null&&!odsMapMin.isEmpty()){
	    Double yjk_51142723_sum = Double.parseDouble(odsMapMin.get("yjk_51142723_sum").toString());
	    sum1 = sum1+yjk_51142723_sum;
	    Double yjk_51142724_sum = Double.parseDouble(odsMapMin.get("yjk_51142724_sum").toString());
	    sum2 = sum2+ yjk_51142724_sum;
	    Double yjk_51142730_sum = Double.parseDouble(odsMapMin.get("yjk_51142730_sum").toString());
	    sum3 = sum3+yjk_51142730_sum;
	    Double yjk_51142731_sum = Double.parseDouble(odsMapMin.get("yjk_51142731_sum").toString());
	    sum4 = sum4+yjk_51142731_sum;
	    odsSum = odsSum+yjk_51142723_sum+yjk_51142724_sum+yjk_51142730_sum+yjk_51142731_sum;
	}
	
	Map<String, Object> odsMapMax = this.yjk2006Service.getSumOdsMoneyMax(parameterMap);
	if(odsMapMin!=null&&!odsMapMax.isEmpty()){
	    Double yjk_51142723_sum = Double.parseDouble(odsMapMax.get("yjk_51142723_sum").toString());
	    ods1 = sum1+(yjk_51142723_sum*1.11);
	    sum1 = sum1+yjk_51142723_sum;
	    Double yjk_51142724_sum = Double.parseDouble(odsMapMax.get("yjk_51142724_sum").toString());
	    ods2 = sum2+(yjk_51142724_sum*1.11);
	    sum2 = sum2+ yjk_51142724_sum;
	    Double yjk_51142730_sum = Double.parseDouble(odsMapMax.get("yjk_51142730_sum").toString());
	    ods3 = sum3+(yjk_51142730_sum*1.06);
	    sum3 = sum3+yjk_51142730_sum;
	    Double yjk_51142731_sum = Double.parseDouble(odsMapMax.get("yjk_51142731_sum").toString());
	    ods4 = sum4+(yjk_51142731_sum*1.06);
	    sum4 = sum4+yjk_51142731_sum;
	    odsSum = odsSum+(yjk_51142723_sum+yjk_51142724_sum)*1.11 + (yjk_51142730_sum+yjk_51142731_sum)*1.06;
	}
	odsSum = Double.parseDouble(df.format(odsSum));
	Map<String, Object>  map1 = new HashMap<String, Object>();
	map1.put("name", "基础业务有价卡折扣");
	map1.put("yjk_amt_sum", Double.parseDouble(df.format(sum1)));
	map1.put("odsSum",  Double.parseDouble(df.format(ods1)));
	Map<String, Object>  map2 = new HashMap<String, Object>();
	map2.put("name", "基础业务有价卡赠送");
	map2.put("yjk_amt_sum",  Double.parseDouble(df.format(sum2)));
	map2.put("odsSum", Double.parseDouble(df.format(ods2)));
	Map<String, Object>  map3 = new HashMap<String, Object>();
	map3.put("name", "增值业务有价卡折扣");
	map3.put("yjk_amt_sum", Double.parseDouble(df.format(sum3)));
	map3.put("odsSum", Double.parseDouble(df.format(ods3)));
	Map<String, Object>  map4 = new HashMap<String, Object>();
	map4.put("name", "增值业务有价卡赠送");
	map4.put("yjk_amt_sum", Double.parseDouble(df.format(sum4)));
	map4.put("odsSum", Double.parseDouble(df.format(ods4)));
	dataList.add(map1);
	dataList.add(map2);
	dataList.add(map3);
	dataList.add(map4);
	Map<String, Object> returnMap = new HashMap<String, Object>();
	returnMap.put("odsSum", odsSum);
	if(sumMap!=null&&sumMap.get("sum_yjk_amt")!=null){
	    returnMap.put("sum_yjk_amt", Double.parseDouble(sumMap.get("sum_yjk_amt").toString()));
	}else{
	    returnMap.put("sum_yjk_amt", 0.00);
	}
	returnMap.put("dataList", dataList);
	return returnMap;
    }
    /**
     * 获取非中高非集团客户大明细
     * <pre>
     * Desc  
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-19 下午1:56:46
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/getFzgFjtDetail")
    public Pager getFzgFjtDetail(HttpServletRequest request,Pager pager){
	Map<String, Object> parameterMap = this.getParameterMap(request);
	pager.setParams(parameterMap);
	List<Map<String, Object>> list = this.yjk2006Service.getFzgFjtDetail(pager);
	pager.setDataRows(list);
	return pager;
    }
    /**
     * 导出明细数据
     * <pre>
     * Desc  
     * @param request
     * @param response
     * @throws Exception
     * @author wangpeng
     * 2017-1-19 下午3:12:40
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "/exportFzgFjtDetail")
    public void exportFzgFjtDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
	try {
	    Map<String, Object> parameterMap = this.getParameterMap(request);
	    List<Map<String, Object>> list = yjk2006Service.getFzgFjtDetailAll(parameterMap);
	    List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	    if (list != null && list.size() > 0) {
		LinkedHashMap<String, Object> lhm = null;
		for (int i = 0; i < list.size(); i++) {
		    Map<String, Object> eachMap = list.get(i);
		    lhm = new LinkedHashMap<String, Object>();
		    lhm.put("1", parameterMap.get("hz_startMonth")+"-"+parameterMap.get("hz_endMonth"));
		    lhm.put("2", eachMap.get("cmcc_prov_prvd_id"));
		    lhm.put("3", eachMap.get("short_name"));
		    lhm.put("4", eachMap.get("cmcc_prov_id"));
		    lhm.put("5", eachMap.get("cmcc_prvd_nm_short"));
		    lhm.put("6", eachMap.get("yjk_ser_no"));
		    lhm.put("7", eachMap.get("yjk_pres_dt"));
		    lhm.put("8", eachMap.get("yjk_typ"));
		    lhm.put("9", eachMap.get("yjk_amt"));
		    lhm.put("10", eachMap.get("yjk_end_dt"));
		    lhm.put("11", eachMap.get("user_id"));
		    lhm.put("12", eachMap.get("cust_id"));
		    lhm.put("13", eachMap.get("msisdn"));
		    lhm.put("14", eachMap.get("send_mon_arpu"));
		    lhm.put("15", eachMap.get("send_lastm_arpu"));
		    lhm.put("16", eachMap.get("send_last2m_arpu"));
		    lhm.put("17", eachMap.get("send_avg_arpu"));
		    lhm.put("18", eachMap.get("yjk_dependency"));
		    lhm.put("19", eachMap.get("yjk_offer_cd"));
		    lhm.put("20", eachMap.get("yjk_offer_nm"));
		    lhm.put("21", eachMap.get("offer_word"));
		    lhm.put("22", eachMap.get("cor_chnl_typ"));
		    lhm.put("23", eachMap.get("cor_chnl_nm"));
		    exportData.add(lhm);
		}
	    }
	    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	    map.put("1", "审计月");
	    map.put("2", "省份代码");
	    map.put("3", "省份名称");
	    map.put("4", "地市代码");
	    map.put("5", "地市名称");
	    map.put("6", "有价卡序列号");
	    map.put("7", "有价卡赠送时间");
	    map.put("8", "有价卡类型");
	    map.put("9", "赠送有价卡金额");
	    map.put("10", "获赠有价卡到期时间");
	    map.put("11", "获赠有价卡的用户标识");
	    
	    map.put("12", "获赠有价卡的客户标识");
	    map.put("13", "获赠有价卡的手机号");
	    map.put("14", "赠送月ARPU");
	    map.put("15", "赠送前一月ARPU");
	    map.put("16", "赠送前两月ARPU");
	    map.put("17", "平均月ARPU");
	    
	    map.put("18", "有价卡赠送依据");
	    map.put("19", "营销案编号");
	    map.put("20", "营销案名称");
	    map.put("21", "有价卡赠送文号说明");
	    map.put("22", "赠送渠道标识");
	    map.put("23", "赠送渠道名称");
	    String fileName="";
	    if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("boss")) {
		fileName = "4.1.4_有价卡合规性_赠送对象分析_向员工赠送分析_总部BOSS数据.csv";
	    } else {
		fileName = "4.1.4_有价卡合规性_赠送对象分析_向员工赠送分析_手工电子台账数据.csv";
	    }
	    CSVUtils.exportCSVList(fileName, exportData, map, request, response);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    /**
     * <pre>
     * Desc 每个controller都要实现的一个方法，用来设置页面初始化查询参数的form使用 
     * @return
     * @author peter.fu
     * Nov 17, 2016 2:16:17 PM
     * </pre>
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/initDefaultParams")
    public Map initDefaultParams(HttpServletRequest request) {

        Map<String, Object> defaultParams = new HashMap<String, Object>();
        
        // 默认按地市排名
        defaultParams.put("hz_rankType", 0);
        // 地市汇总地图倍数
        defaultParams.put("hz_map_double", 5);
        
        return defaultParams;
    }

}
