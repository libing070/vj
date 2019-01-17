/**
 * com.hpe.cmca.controller.JKGJController.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.controller;


import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.common.CommonResult;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.util.Json;


/**
 * <pre>
 * Desc： 监控告警
 * @author   xuwenhu
 * @refactor xuwenhu
 * @date     2016-11-25 下午5:08:34
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2016-11-25 	   xuwenhu 	         1. Created this class. 
 * </pre>  
 */
@Controller
@RequestMapping("/jkgj")
public class JKGJController extends BaseController {
//    @Autowired
//    private MybatisDao mybatisDao;
    
//    @CmcaAuthority(authorityTypes = { AuthorityType.jkgj })//判断是否具有监控告警权限
    @RequestMapping(value = "index")
    public String index() {
	return "jkgj/index";
    }
    
    @RequestMapping(value = "prvdpie")
    public String prvdpie() {
	return "/chartsdraft/prvdpie";
    }
    @RequestMapping(value = "columnofPerHour")
    public String columnofPerHour() {
	return "/chartsdraft/columnofPerHour";
    }
    /**
     * 
     * <pre>
     * Desc  
     * @param request
     * @param uiModel
     * @return
     * @author xuwenhu
     * 2016-12-7 上午11:03:26
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "queryDefaultParams", produces = "plain/text; charset=UTF-8")
    public String queryDefaultParams(HttpServletRequest request, Model uiModel) {
	Map<String, Object> result = new HashMap<String, Object>();
	Date nowDate = new Date();
	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH:mm:ss"); 
	String now = df.format(nowDate);
	String date = now.substring(0, 8);//年月日
	result.put("date", date);
	result.put("prvdIds", "10000");//全国
	result.put("monitorPoint","100");//全部
	return  Json.Encode(result);
    }
    /**
     * 
     * <pre>
     * Desc  	柱图
     * @param warnDay
     * @param prvdIds
     * @param monitorPoint
     * @return
     * @author xuwenhu
     * 2016-11-28 上午10:52:30
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getColumnofPerHour", produces = "plain/text; charset=UTF-8")
    public String getColumnofPerHour(String warnDay, Integer[] prvdIds, Integer[] monitorPoint ) {
	int hours = 23;
	Date nowDate = new Date();
	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH:mm:ss"); 
	String now = df.format(nowDate);
	String date = now.substring(0, 8);//年月日
	if(date.equals(warnDay)){
	    hours = Integer.parseInt(now.substring(8, 10));
	}
	Map<String, Object> result = new HashMap<String, Object>();
	List<String> categories = new ArrayList<String>();
	ArrayList<Object> data = new ArrayList<Object>();
	//选中日期每个小时的时间点。如果日期是历史日期，则为24个小时。如果是当天日期，则是到查询操作时的小时数。例如早上8:05进行查询，查询当天短信情况，则只有0~8点8个柱图数据。
	for(int i=0;i<=hours;i++){
	    String hour =  String.valueOf(i);
	    hour += "点";
	    categories.add(hour);
	    data.add(0);
	}
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("warnDay", warnDay);
	paramMap.put("prvdIds", prvdIds==null?"":StringUtils.join(prvdIds, ","));
	paramMap.put("monitorPoint", StringUtils.join(monitorPoint, ","));
	result.put("categories", categories);
	List<Map<String, Object>> info = mybatisDao.getList("jkgj.getColumnofPerHour", paramMap);
	List<Map<String, Object>> series = new ArrayList<Map<String, Object>>();
	Map<String, Object> value = new HashMap<String, Object>();
	for (Map<String, Object> map : info) {
	    for(int i=0;i<=hours;i++){
		if(map.get("perHour").toString().equals(i<10?"0"+i:i+"")){//一位时，前面的0要做统一
		    data.set(i,map.get("smsCount"));
		    break;
		}
	    }
	}
	value.put("name", "短信数量");
	value.put("data", data);
	series.add(value);
	result.put("series", series);
	return CommonResult.success(result);
    }
   
     /**
      * 
      * <pre>
      * Desc  当月告警短信数量省份分布饼图
      * @param warnMonth
      * @param prvdIds
      * @param monitorPoint
      * @return
      * @author xuwenhu
      * 2016-11-28 上午11:18:27
      * </pre>
      */
    @ResponseBody
    @RequestMapping(value = "getPrvdPie", produces = "plain/text; charset=UTF-8")
    public String getPrvdPie(String warnMonth, Integer[] prvdIds, String[] monitorPoint) {
	Map<String, Object> result = new HashMap<String, Object>();
	Map<String, Object> paramMap = new HashMap<String, Object>();
//	String prvdName = "";
	// 如果选择了具体省分，则饼图生成规则：本省&其他省合计按照条件取得的短信数/ 按照条件取得的短信记录数总数。
	if(prvdIds.length==1||prvdIds[0]==10000){//查全国各省的
	    paramMap.put("prvdStr", "10000");
	}else{
	    paramMap.put("prvdStr", StringUtils.join(prvdIds, ","));
//	    for(int i=0; i<prvdIds.length; i++){
//		    if(i>0){
//			prvdName += ",";
//		    }
//		    prvdName += "'"+getCompanyNameOfProvince(prvdIds[i].toString()).substring(0, getCompanyNameOfProvince(prvdIds[i].toString()).length()-2)+"'";
//		}
	}
	paramMap.put("warnMonth", warnMonth);
//	paramMap.put("prvdName", prvdName);
	paramMap.put("monitorPoint", StringUtils.join(monitorPoint, ","));
	List<Map<String, Object>> info = mybatisDao.getList("jkgj.getPrvdpie", paramMap);
	List<Map<String, Object>> series = new ArrayList<Map<String, Object>>();
	ArrayList<Object> data = new ArrayList<Object>();
	Map<String, Object> value = new HashMap<String, Object>();
	ArrayList<Object> others = new ArrayList<Object>();
	Integer count = new Integer(0);
	boolean otherFlag = false;
	if(prvdIds.length == 1 && prvdIds[0] != 10000){//选择具体省份时
	    for(int i=0; i<info.size(); i++){
    	    	if(info.get(i).get("prvdId").toString().equals(prvdIds[0].toString())){
    	    	    ArrayList<Object> one = new ArrayList<Object>();
                    one.add(getCompanyNameOfProvince(prvdIds[0].toString()).substring(0, getCompanyNameOfProvince(prvdIds[0].toString()).length()-2));//省份名称
                    one.add(info.get(i).get("smsCount"));//短信数量
                    data.add(one);
    	    	}else{//除了已选的省外
    	    	    otherFlag= true;
    	    	    count += Integer.parseInt(info.get(i).get("smsCount").toString().trim());
    	    	}
	    }
	}else{
    		for(int i=0; i<info.size(); i++){
        	    if(i<4){
//        		if(i==2){//饼图上选中的那部分
//        		    Map<String, Object> item = new HashMap<String, Object>();
//        		    item.put("name", getCompanyNameOfProvince(info.get(i).get("prvdId").toString()).substring(0, getCompanyNameOfProvince(info.get(i).get("prvdId").toString()).length()-2));//省份名称
//        		    item.put("y", info.get(i).get("smsCount"));//短信数量
//        		    item.put("sliced", true);
//        		    item.put("selected", true);
//        		    data.add(item);
//        		}else{
        		ArrayList<Object> one = new ArrayList<Object>();
                	one.add(getCompanyNameOfProvince(info.get(i).get("prvdId").toString()).substring(0, getCompanyNameOfProvince(info.get(i).get("prvdId").toString()).length()-2));//省份名称
                	one.add(info.get(i).get("smsCount"));//短信数量
                	data.add(one);
//        		}
        	    }else{
        		otherFlag = true;
        		//除top1~4
        		count += Integer.parseInt(info.get(i).get("smsCount").toString().trim());
        	    }
    		} 
    	}
	if(otherFlag){
	    others.add("其他");//省份名称
	    others.add(count);//短信数量  
	    data.add(others);
	}
	value.put("type", "pie");
	value.put("name", "告警短信数量");
	value.put("data", data);
	series.add(value);
	result.put("series", series);
	return CommonResult.success(result);
    }
    /**
     * 
     * <pre>
     * Desc 当月告警短信数量监控点分布饼图
     * @param warnMonth
     * @param prvdIds
     * @param monitorPoint
     * @return
     * @author xuwenhu
     * 2016-11-28 上午11:18:14
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getMonitorPointPie", produces = "plain/text; charset=UTF-8")
    public String getMonitorPointPie(String warnMonth, Integer[] prvdIds, String[] monitorPoint ) {
	Map<String, Object> result = new HashMap<String, Object>();
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("prvdStr", StringUtils.join(prvdIds, ","));
//	String prvdName = "";
//	for(int i=0; i<prvdIds.length; i++){
//	    if(i>0){
//		prvdName += ",";
//	    }
//	    prvdName += "'"+getCompanyNameOfProvince(prvdIds[i].toString()).substring(0, getCompanyNameOfProvince(prvdIds[i].toString()).length()-2)+"'";
//	}
	paramMap.put("warnMonth", warnMonth);
//	paramMap.put("prvdName", prvdName);
	paramMap.put("monitorPoint",  StringUtils.join(monitorPoint, ","));
	List<Map<String, Object>> info = mybatisDao.getList("jkgj.getMonitorPointPie", paramMap);
	List<Map<String, Object>> series = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> othersItem = new ArrayList<Map<String, Object>>();
//	List<Map<String, Object>> othersData = new ArrayList<Map<String, Object>>();
	ArrayList<Object> othersData = new ArrayList<Object>();
	Map<String, Object> value1 = new HashMap<String, Object>();
	ArrayList<Object> data = new ArrayList<Object>();
	Map<String, Object> value = new HashMap<String, Object>();
//	 ArrayList<Object> others = new ArrayList<Object>();
	Map<String, Object> others = new HashMap<String, Object>();
	 Integer count = new Integer(0);
	 boolean otherFlag = false;
	for(int i=0; i<info.size(); i++){
    	    if(i<4){
//        	if(i==2){//饼图上选中的那部分
//		    Map<String, Object> item = new HashMap<String, Object>();
//		    item.put("name", info.get(i).get("Warn_name"));//监控点名称
//		    item.put("y", info.get(i).get("smsCount"));//短信数量
//		    item.put("sliced", true);
//		    item.put("selected", true);
//		    data.add(item);
//		 }else{
    		Map<String, Object> one = new HashMap<String, Object>();
    		// name: 'Animals',
    		// y: 5,
    		// drilldown: 'animals'
    		    one.put("name", info.get(i).get("Warn_name"));//监控点名称
		    one.put("y", info.get(i).get("smsCount"));//短信数量
		    data.add(one);
//			ArrayList<Object> one = new ArrayList<Object>();
//	        	one.add(info.get(i).get("Warn_name"));//监控点名称
//	        	one.add(info.get(i).get("smsCount"));//短信数量
//	        	data.add(one);

//		 }
    	    }else{
    		otherFlag = true;
		//除top1~4
		count += Integer.parseInt(info.get(i).get("smsCount").toString().trim());
//		Map<String, Object> otherItem = new HashMap<String, Object>();
		ArrayList<Object> otherItem = new ArrayList<Object>();
		otherItem.add(info.get(i).get("Warn_name"));//监控点名称
		otherItem.add(info.get(i).get("smsCount"));//短信数量
		othersData.add(otherItem);
	    }
	    
	}
	
	value.put("type", "pie");
	value.put("name", "告警短信数量");
	value.put("data", data);
	series.add(value);
	result.put("series", series);
	if(otherFlag){
	    value1.put("type", "pie");
	    value1.put("id", "others");//The drilldown series is linked to the parent series' point by its id.
	    value1.put("data", othersData);
	    othersItem.add(value1);
	    
	    others.put("name", "其他");//监控点名称
	    others.put("y", count);//短信数量
	    others.put("drilldown", "others");//加drilldown属性即可下钻，下钻到id是others的series
//	    others.add("其他");//监控点名称
//	    others.add(count);//短信数量
	    data.add(others);
	}
	result.put("others", othersItem);
	return CommonResult.success(result);
    }
    /**
     * 修改接收人和手机号
     * <pre>
     * Desc  
     * @param Receiver
     * @param phone
     * @param monitorPoint
     * @return
     * @author xuwenhu
     * 2016-11-30 上午10:30:53
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "modifyReceiverPhone", produces = "plain/text; charset=UTF-8")
    public String modifyReceiverPhone(String int_id,String alarm_type, String zhuanti_id, String[] user_nm, Long[] phone) {
	String user_nm_str = "";
	/*tomcat等服务器的配置文件里加转码就不需要在这做转码了*/
//	try {
//	    if(user_nm !=null && user_nm.length > 0){
//		for(int i=0; i<user_nm.length; i++){
//		    user_nm[i] = new String(user_nm[i].getBytes("ISO-8859-1"), "UTF-8");
//		}
		user_nm_str = StringUtils.join(user_nm,";");
//	    }
//	} catch (UnsupportedEncodingException e) {
//	    e.printStackTrace();
//	} 
//	phone=new Long[]{Long.parseLong("13459118245"),Long.parseLong("12345678909")};
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("alarm_type", alarm_type);
	if(alarm_type.equals("INTERFACE")){
	    paramMap.put("int_id", int_id);
	}else{//数据库和专题类别的用专题id字段区分
	    paramMap.put("zhuanti_id", zhuanti_id);
	}
	paramMap.put("user_nm", user_nm_str);
	paramMap.put("phone", StringUtils.join(phone,";"));
	mybatisDao.update("jkgj.modifyReceiverPhone", paramMap);
	List<Map<String, Object>> info = mybatisDao.getList("jkgj.getMonitorConfiguration", "");
	return CommonResult.success(info);
    }
    /**
     * 
     * <pre>
     * Desc  删除一条短信告警监控配置表信息
     * @param int_id
     * @return
     * @author xuwenhu
     * 2016-11-30 下午4:48:24
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "deleteMonitorConfiguration", produces = "plain/text; charset=UTF-8")
    public String deleteMonitorConfiguration(String int_id) {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("int_id", int_id);
	mybatisDao.delete("jkgj.deleteMonitorConfiguration", paramMap);
//	return CommonResult.success(info);//data.status==0?成功:失败
	List<Map<String, Object>> info = mybatisDao.getList("jkgj.getMonitorConfiguration", "");
	return CommonResult.success(info);
    }
    /**
     * 
     * <pre>
     * Desc 查询短信告警监控配置表信息
     * @param Receiver
     * @param phone
     * @param monitorPoint
     * @return
     * @author xuwenhu
     * 2016-11-30 上午10:18:25
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getMonitorConfiguration", produces = "plain/text; charset=UTF-8")
    public String getMonitorConfiguration() {
	List<Map<String, Object>> info = mybatisDao.getList("jkgj.getMonitorConfiguration", "");
	return CommonResult.success(info);
    }
    
   /**
    * 
    * <pre>
    * Desc  增加一条短信告警监控配置表信息
    * @param int_id
    * @param int_nm
    * @param zhuanti_id
    * @param zhuanti_nm
    * @param user_nm
    * @param phone
    * @return
    * @author xuwenhu
    * 2016-11-30 下午3:20:01
    * </pre>
    */
    @ResponseBody
    @RequestMapping(value = "addMonitorConfiguration", produces = "plain/text; charset=UTF-8")
    public String addMonitorConfiguration(String int_id, String int_nm, String zhuanti_id,String zhuanti_nm, String[] user_nm, Long[] phone) {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("int_id", int_id);
	paramMap.put("int_nm", int_nm);
	paramMap.put("zhuanti_id", zhuanti_id);
	String user_nm_str = "";
	/*tomcat等服务器的配置文件里加转码就不需要在这做转码了*/
//	try {
//	    zhuanti_nm = new String(zhuanti_nm.getBytes("ISO-8859-1"), "UTF-8");
//	    if(user_nm !=null && user_nm.length > 0){
//		for(int i=0; i<user_nm.length; i++){
//		    user_nm[i] = new String(user_nm[i].getBytes("ISO-8859-1"), "UTF-8");
//		}
		user_nm_str = StringUtils.join(user_nm,"；");
//	    }
//	} catch (UnsupportedEncodingException e) {
//	    e.printStackTrace();
//	} 
	paramMap.put("zhuanti_nm", zhuanti_nm);
	paramMap.put("user_nm", user_nm_str);
	paramMap.put("phone", phone==null||phone.length==0?"":StringUtils.join(phone,"；"));
	mybatisDao.add("jkgj.addMonitorConfiguration", paramMap);
	List<Map<String, Object>> info = mybatisDao.getList("jkgj.getMonitorConfiguration", "");
	return CommonResult.success(info);
    }
    
    /**
     * 
     * <pre>
     * Desc  查询接口文件（编号为1.1.*）的监控明细
     * @return
     * @author xuwenhu
     * 2016-12-2 下午3:23:59
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getMonitorDetailsOne", produces = "plain/text; charset=UTF-8")
    public String getMonitorDetailsOne(String date) {
	Map<String, Object> result = new HashMap<String, Object>();
	Map<String, Object> paramMap = new HashMap<String, Object>();
//	Date nowDate = new Date();
//	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH:mm:ss"); 
//	String now = df.format(nowDate);
//	String date = now.substring(0, 6);//年月
	paramMap.put("today", date);
//	paramMap.put("today", "201610");//测试用
	List<Map<String, Object>> infoList = mybatisDao.getList("jkgj.getMonitorDetailsOne", paramMap);
	result.put("infoList",infoList);
//	result.put("now",now);
	return CommonResult.success(result);
    }
    
    /**
     * 
     * <pre>
     * Desc 查询模型执行（编号为1.2.*）的监控明细 
     * @return
     * @author xuwenhu
     * 2016-12-2 下午3:24:13
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getMonitorDetailsTwo", produces = "plain/text; charset=UTF-8")
    public String getMonitorDetailsTwo(String date) {
	Map<String, Object> result = new HashMap<String, Object>();
	ArrayList<Object> subjectNmList = new ArrayList<Object>();
	ArrayList<Object> focusCount2List = new ArrayList<Object>();
	ArrayList<Object> focusCount3List = new ArrayList<Object>();
	Map<String, Object> paramMap = new HashMap<String, Object>();
//	Date nowDate = new Date();
//	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH:mm:ss"); 
//	String now = df.format(nowDate);
//	String date = now.substring(0, 8);//年月日
	paramMap.put("today", date);
//	paramMap.put("today", "20161202");//测试用
	List<Map<String, Object>> info = mybatisDao.getList("jkgj.getMonitorDetailsTwo", paramMap);
	for(Map<String, Object> map : info){
	    String subjectNm = map.get("subjectNm").toString();
	    if (subjectNm.equals("")) continue;//专题名是空的不显示到页面
	    if(!subjectNmList.contains(subjectNm)){
		subjectNmList.add(subjectNm);
		focusCount2List.add(0);//未完成
		focusCount3List.add(0);//执行失败
	    }
	    String status = map.get("status").toString();
	    String focusCount = map.get("focusCount").toString();
	    Integer index = subjectNmList.indexOf(subjectNm);
	    if(status.equals("未完成")){
		focusCount2List.set(index, focusCount);//未完成
		continue;
	    }
	    if(status.equals("执行失败")){
		focusCount3List.set(index,focusCount);//执行失败
	    }
	}
//	result.put("now", now);
	result.put("subjectNmList", subjectNmList);
	result.put("focusCount2List", focusCount2List);
	result.put("focusCount3List", focusCount3List);
	return CommonResult.success(result);
    }
    
    /**
     * 
     * <pre>
     * Desc  查询数据空间（编号为1.3.*）的监控明细
     * @return
     * @author xuwenhu
     * 2016-12-2 下午3:24:27
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getMonitorDetailsThree", produces = "plain/text; charset=UTF-8")
    public String getMonitorDetailsThree(String date) {
	Map<String, Object> result = new HashMap<String, Object>();
	Map<String, Object> paramMap = new HashMap<String, Object>();
//	Date nowDate = new Date();
//	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH:mm:ss"); 
//	String now = df.format(nowDate);
//	String date = now.substring(0, 8);//年月日
	paramMap.put("today", date);
//	paramMap.put("today", "20161031");//测试用
	ArrayList<Object> create_timeList = new ArrayList<Object>();
	ArrayList<Object> databaseNmList = new ArrayList<Object>();
	ArrayList<Object> send_countList = new ArrayList<Object>();
	List<Map<String, Object>> info = mybatisDao.getList("jkgj.getMonitorDetailsThree", paramMap);
	for(int i=0;i<info.size();i++){
	    String create_time = info.get(i).get("create_time").toString();//这种格式的：2016-11-23 11:08:46
//	    info.get(i).put("create_time", create_time.substring(5, create_time.indexOf(".")).replace("-", ""));//格式化成  MMDD hh:mm:ss
	    create_time = create_time.substring(10, create_time.indexOf(".")).replace("-", "");
	    create_timeList.add(i, create_time);
	    databaseNmList.add(i, info.get(i).get("databaseNm").toString());
//	    send_countList.add(i, info.get(i).get("send_count").toString());
	}
//	result.put("now", now);
	result.put("create_timeList", create_timeList);
	result.put("databaseNmList", databaseNmList);
	result.put("send_countList", send_countList);
	return CommonResult.success(result);
    }
    /**
     * 审计报告
     * <pre>
     * Desc  
     * @return
     * @author xuwenhu
     * 2016-12-12 下午7:31:13
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getMonitorDetailsFour", produces = "plain/text; charset=UTF-8")
    public String getMonitorDetailsFour(String date) {
	Map<String, Object> result = new HashMap<String, Object>();
	Map<String, Object> paramMap = new HashMap<String, Object>();
//	Date nowDate = new Date();
//	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH:mm:ss"); 
//	String now = df.format(nowDate);
//	String date = now.substring(0, 8);//年月日
	paramMap.put("today", date);
//	paramMap.put("today", "20161026");//测试用
	Map<String, Object> wwcReport = new HashMap<String, Object>();
	ArrayList<Object> subjectNmList = new ArrayList<Object>();
	ArrayList<Object> prvdNmList = new ArrayList<Object>();
	List<Map<String, Object>> info = mybatisDao.getList("jkgj.getMonitorDetailsFour", paramMap);//未完成审计报告的省份
	paramMap.put("audMonth", date.substring(0, 6));
	List<Map<String, Object>> ywcInfo = mybatisDao.getList("jkgj.getAudReportFileofSubj", paramMap);//已完成审计报告的专题
	int j = 0;
	for(int i=0;i<info.size();i++){
	    String subjectNm = info.get(i).get("subjectNm").toString();
	    String prvdId = info.get(i).get("prvdId").toString();
	    if(!subjectNmList.contains(subjectNm)){
		subjectNmList.add(j, subjectNm);
		 prvdNmList.add(j, getCompanyNameOfProvince(prvdId).substring(0, getCompanyNameOfProvince(prvdId).length()-2));
		 j++;
	    }else{
		int index = subjectNmList.indexOf(subjectNm);
		prvdNmList.set(index, prvdNmList.get(index)+" "+getCompanyNameOfProvince(prvdId).substring(0, getCompanyNameOfProvince(prvdId).length()-2));
	    }
	}
	Map<String, Object> ywcReport = new HashMap<String, Object>();
	ArrayList<Object> ysubjectNmList = new ArrayList<Object>();
	ArrayList<Object> timeList = new ArrayList<Object>();
	for(int i=0;i<ywcInfo.size();i++){
	    String subjectNm = ywcInfo.get(i).get("subjectNm").toString();
	    String create_time = ywcInfo.get(i).get("create_time").toString();
	    String[] times = new String[]{};
	    times = create_time.split("-");
	    String genTime = times[0]+"年"+times[1]+"月"+times[2].substring(0, 2)+"日";
	    ysubjectNmList.add(i, subjectNm);
	    timeList.add(i, genTime);
	}
//	result.put("now", now);
	wwcReport.put("subjectNmList", subjectNmList);
	wwcReport.put("prvdNmList", prvdNmList);
	result.put("wwcReport", wwcReport);
	ywcReport.put("ysubjectNmList", ysubjectNmList);
	ywcReport.put("timeList", timeList);
	result.put("ywcReport", ywcReport);
	return CommonResult.success(result);
    }
}
