/**
 * com.hpe.cmca.service.SjkgService.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.SjkgMapper;
import com.hpe.cmca.pojo.SjkgData;

/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2017-11-10 上午10:27:16
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-11-10 	   hufei 	         1. Created this class.
 * </pre>
 */
@Service("/SjkgService")
public class SjkgService extends BaseObject {

    @Autowired
    protected MybatisDao     mybatisDao;
    protected List<SjkgData> subjectList;

    /**
     * 
     * <pre>
     * Desc  审计开关-获取默认信息
     * @return
     * @author hufei
     * 2017-11-15 下午3:20:08
     * </pre>
     */
    public Map<String, Object> queryDefaultInfo(SjkgData parameterData) {
	Map<String, Object> result = new HashMap<String, Object>();
	parameterData.setQueryOrAdd("query");
	subjectList = getSubjectList(parameterData);
	result.put("subjectList", subjectList);
	result.put("roleId", parameterData.getRoldId());
	return result;
    }

    /**
     * 
     * <pre>
     * Desc  获取审计开关-专题列表
     * @return
     * @author hufei
     * 2017-11-10 下午3:22:20
     * </pre>
     */
    public List<SjkgData> getSubjectList(SjkgData parameterData) {
	SjkgMapper sjkgMapper = mybatisDao.getSqlSession().getMapper(SjkgMapper.class);
	List<SjkgData> list = sjkgMapper.getSubjectList(parameterData);
	return list;
    }

    /**
     * 
     * <pre>
     * Desc 审计开关-根据专题返回审计月列表
     * @param parameterData
     * @return
     * @author hufei
     * 2017-11-15 下午3:20:25
     * </pre>
     */
    public List<SjkgData> getAudTrmBySubject(SjkgData parameterData) {
	SjkgMapper sjkgMapper = mybatisDao.getSqlSession().getMapper(SjkgMapper.class);
	return sjkgMapper.getAudTrmBySubject(parameterData);
    }

    /**
     * <pre>
     * Desc  审计开关-根据专题、审计月获取可以新增的开关类型
     * @param parameterData
     * @return
     * @author hufei
     * 2017-11-17 上午11:21:18
     * </pre>
     */
    public List<Map<String, Object>> getSwitchType(SjkgData parameterData) {
	SjkgMapper sjkgMapper = mybatisDao.getSqlSession().getMapper(SjkgMapper.class);
	List<SjkgData> sjkgList = sjkgMapper.getSwitchType(parameterData);
	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	Map<String, Object> webMap = new HashMap<String, Object>();
	Map<String, Object> reportMap = new HashMap<String, Object>();
	webMap.put("switchType", 1);
	webMap.put("switchTypeName", "系统页面");
	reportMap.put("switchType", 2);
	reportMap.put("switchTypeName", "报告及清单下载");
	if (sjkgList == null || sjkgList.size() == 0) {
	    result.add(webMap);
	    result.add(reportMap);
	}
	if (sjkgList != null && sjkgList.size() > 0 && sjkgList.get(0).getSwitchType() == 1) {
	    result.add(reportMap);
	}
	if (sjkgList != null && sjkgList.size() > 0 && sjkgList.get(0).getSwitchType() == 2) {
	    result.add(webMap);
	}
	return result;
    }

    /**
     * 
     * <pre>
     * Desc  审计开关-保存审计开关信息
     * @param parameterData
     * @return
     * @author hufei
     * 2017-11-15 下午3:20:44
     * </pre>
     */
    public int saveSwitchInfo(SjkgData parameterData) {
	if ("1".equals(parameterData.getRoldId()) && parameterData.getSwitchState() <= 1) {
	    SjkgMapper sjkgMapper = mybatisDao.getSqlSession().getMapper(SjkgMapper.class);
	    return sjkgMapper.saveSwitchInfo(parameterData);
	} else {
	    return 0;
	}

    }

    /**
     * 
     * <pre>
     * Desc  审计开关-查询结果列表
     * @param parameterData
     * @return
     * @author hufei
     * 2017-11-15 下午3:21:02
     * </pre>
     */
    public List<SjkgData> getSwitchInfoList(SjkgData parameterData) {
	SjkgMapper sjkgMapper = mybatisDao.getSqlSession().getMapper(SjkgMapper.class);
	List<SjkgData> result = sjkgMapper.getSwitchInfoList(parameterData);
	if (result != null && !result.isEmpty()) {
	    for (SjkgData sjkgData : result) {
		sjkgData.setAudTrm(getAudtrmToString(sjkgData.getAudTrm()));
		sjkgData.setSwitchStateByRole(getSwitchStateByRole(parameterData.getRoldId(),sjkgData.getSwitchState()));
	    }
	}

	return result;
    }

    /**
     * 
     * <pre>
     * Desc  根据开关状态及角色ID返回相应的状态码。
     * 
     * @param switchState
     * @return
     * @author hufei
     * 2017-11-17 上午11:30:59
     * </pre>
     */
    private int getSwitchStateByRole(String roleId,int switchState) {
	if ("2".equals(roleId) && switchState == 0) {
	    return 0;
	}
	if ("2".equals(roleId) && switchState == 1) {
	    return 1;
	}
	if(("1".equals(roleId)||"4".equals(roleId)) && switchState == 0){
	    return 1;
	}
	if ("2".equals(roleId) && switchState == 2) {
	    return 2;
	}
	if(("1".equals(roleId)||"4".equals(roleId)) && switchState != 0){
	    return 2;
	}
	return 0;
    }

    /**
     * 
     * <pre>
     * Desc  审计开关-获取甘特图、
     * 步骤1：获取所有审计开关数据、截止到目前的月份数据、专题数
     * 步骤2：遍历专题数，每个专题对应着不同的一个map
     * 步骤3：遍历月份数，每个月默认填充值3，代表不存在
     * 步骤4：遍历开关数据，对应月份的值改为数据库中开关的实际值
     * @param parameterData
     * @return
     * @author hufei
     * 2017-11-15 下午2:38:43
     * </pre>
     */
    public List<Object> getGanttChart(SjkgData parameterData) {
	SjkgMapper sjkgMapper = mybatisDao.getSqlSession().getMapper(SjkgMapper.class);
	List<SjkgData> resultList = sjkgMapper.getGanttChart(parameterData);
	List<String> audTrmList = getAudtrmList();
	List<String> webList;
	List<String> reportList;
	List<String> audTrmResult;
	Map<String, Object> map;
	List<Object> result = new ArrayList<Object>();
	for (SjkgData subjectData : subjectList) {
	    map = new HashMap<String, Object>(); // 专题结果表，通过for循环中的new,保证每个专题有一个结果表
	    webList = new ArrayList<String>(); // 专题-系统页面结果表
	    reportList = new ArrayList<String>();// 专题-报告及清单下载结果表
	    audTrmResult = new ArrayList<String>();// 专题-审计月结果表
	    for (int m = audTrmList.size() - 1; m >= 0; m--) {
		webList.add("3");
		reportList.add("3");
		audTrmResult.add(audTrmList.get(m));
		for (SjkgData sjkgData : resultList) {
		    if (subjectData.getSubjectCode().equals(sjkgData.getSubjectCode()) && audTrmList.get(m).equals(sjkgData.getAudTrm()) && "1".equals(String.valueOf(sjkgData.getSwitchType()))) {
			webList.set(audTrmList.size() - 1 - m, String.valueOf(sjkgData.getSwitchState()));
		    }
		    if (subjectData.getSubjectCode().equals(sjkgData.getSubjectCode()) && audTrmList.get(m).equals(sjkgData.getAudTrm()) && "2".equals(String.valueOf(sjkgData.getSwitchType()))) {
			reportList.set(audTrmList.size() - 1 - m, String.valueOf(sjkgData.getSwitchState()));
		    }

		}

	    }
	    map.put("subjectCode", subjectData.getSubjectCode());
	    map.put("subjectName", subjectData.getSubjectName());
	    map.put("webList", webList);
	    map.put("reportList", reportList);
	    map.put("audTrmResult", audTrmResult);
	    result.add(map);
	}

	return result;
    }
    
    public List<Object> getBubbleChart(SjkgData parameterData) {
	SjkgMapper sjkgMapper = mybatisDao.getSqlSession().getMapper(SjkgMapper.class);
	List<SjkgData> resultList = sjkgMapper.getBubbleChart(parameterData);
	List<String> strlist;
	List<Object> webList = new ArrayList<Object>();
	List<Object> reportList = new ArrayList<Object>();
	List<Object> oneDayList = new ArrayList<Object>();
	Map<String, Object> webMap = new HashMap<String, Object>();
	Map<String, Object> reportMap = new HashMap<String, Object>();
	Map<String, Object> oneDayResultMap = new HashMap<String, Object>();
	List<String> oprTimeList = new ArrayList<String>();
	if (resultList != null && !resultList.isEmpty()) {
	    strlist = new ArrayList<String>();
	    StringBuffer showTooltip = new StringBuffer();
	    for (int i = 0; i < resultList.size(); i++) {
		SjkgData sjkgData = resultList.get(i);
		if (oprTimeList.contains(sjkgData.getOprTime() + sjkgData.getSwitchState())) {
		} else {
		    strlist = new ArrayList<String>();
		    oprTimeList.add(sjkgData.getOprTime());
		    strlist.add(sjkgData.getOprTime());
		    strlist.add("1");
		}
		showTooltip.append("审计月：" + sjkgData.getAudTrm() + " 开关类型：" + sjkgData.getSwitchTypeName() + ";</br>");
		if (i == resultList.size() - 1 ||(!resultList.get(i + 1).getQueryOrAdd().equals(sjkgData.getQueryOrAdd()))||!(resultList.get(i + 1).getOprTime().equals(sjkgData.getOprTime()))) {
		    showTooltip.append("打开时间：" + sjkgData.getOprTime());
		    strlist.add(showTooltip.toString());
		    if ("1".equals(sjkgData.getQueryOrAdd())&&1 == sjkgData.getSwitchType()) {
			webList.add(strlist);
		    }
		    if ("1".equals(sjkgData.getQueryOrAdd())&&2 == sjkgData.getSwitchType()) {
			reportList.add(strlist);
		    }
		    if("3".equals(sjkgData.getQueryOrAdd())){
			oneDayList.add(strlist);
		    }
		    showTooltip = new StringBuffer();
		}
	    }
	}
	webMap.put("switchType", 1);
	webMap.put("switchTypeName", "系统页面");
	webMap.put("myValue", webList);
	reportMap.put("switchType", 2);
	reportMap.put("switchTypeName", "报告及清单下载");
	reportMap.put("myValue", reportList);

	// 同时打开
//	List<SjkgData> oneResultList = sjkgMapper.getOpenSwitchOneDay(parameterData);
//	oprTimeList = new ArrayList<String>();
//	if (oneResultList != null && !oneResultList.isEmpty()) {
//	    strlist = new ArrayList<String>();
//	    StringBuffer showTooltip = new StringBuffer();
//	    for (int i = 0; i < oneResultList.size(); i++) {
//		SjkgData sjkgData = oneResultList.get(i);
//		if (oprTimeList.contains(sjkgData.getOprTime() + sjkgData.getSwitchState())) {
//		} else {
//		    strlist = new ArrayList<String>();
//		    oprTimeList.add(sjkgData.getOprTime());
//		    strlist.add(sjkgData.getOprTime());
//		    strlist.add("1");
//		}
//		showTooltip.append("审计月：" + sjkgData.getAudTrm() + " 开关类型：" + sjkgData.getSwitchTypeName() + ";</br>");
//		if (i == oneResultList.size() - 1 || !(oneResultList.get(i + 1).getOprTime().equals(sjkgData.getOprTime()))) {
//		    showTooltip.append("打开时间：" + sjkgData.getOprTime());
//		    strlist.add(showTooltip.toString());
//		    oneDayList.add(strlist);
//		    showTooltip = new StringBuffer();
//		}
//	    }
//	}
	oneDayResultMap.put("switchType", 3);
	oneDayResultMap.put("switchTypeName", "系统页面、报告及清单下载");
	oneDayResultMap.put("myValue", oneDayList);

	List<Object> result = new ArrayList<Object>();
	result.add(webMap);
	result.add(reportMap);
	result.add(oneDayResultMap);
	return result;

    }

//    public List<Object> getBubbleChart(SjkgData parameterData) {
//	SjkgMapper sjkgMapper = mybatisDao.getSqlSession().getMapper(SjkgMapper.class);
//	List<SjkgData> resultList = sjkgMapper.getBubbleChart(parameterData);
//	List<String> strlist;
//	List<Object> webList = new ArrayList<Object>();
//	List<Object> reportList = new ArrayList<Object>();
//	List<Object> oneDayList = new ArrayList<Object>();
//	Map<String, Object> webMap = new HashMap<String, Object>();
//	Map<String, Object> reportMap = new HashMap<String, Object>();
//	Map<String, Object> oneDayResultMap = new HashMap<String, Object>();
//	List<String> oprTimeList = new ArrayList<String>();
//	if (resultList != null && !resultList.isEmpty()) {
//	    strlist = new ArrayList<String>();
//	    StringBuffer showTooltip = new StringBuffer();
//	    for (int i = 0; i < resultList.size(); i++) {
//		SjkgData sjkgData = resultList.get(i);
//		if (oprTimeList.contains(sjkgData.getOprTime() + sjkgData.getSwitchState())) {
//		} else {
//		    strlist = new ArrayList<String>();
//		    oprTimeList.add(sjkgData.getOprTime());
//		    strlist.add(sjkgData.getOprTime());
//		    strlist.add("1");
//		}
//		showTooltip.append("审计月：" + sjkgData.getAudTrm() + " 开关类型：" + sjkgData.getSwitchTypeName() + ";</br>");
//		if (i == resultList.size() - 1 || !(resultList.get(i + 1).getOprTime().equals(sjkgData.getOprTime()))) {
//		    showTooltip.append("打开时间：" + sjkgData.getOprTime());
//		    strlist.add(showTooltip.toString());
//		    if (1 == sjkgData.getSwitchType()) {
//			webList.add(strlist);
//		    }
//		    if (2 == sjkgData.getSwitchType()) {
//			reportList.add(strlist);
//		    }
//		    showTooltip = new StringBuffer();
//		}
//	    }
//	}
//	webMap.put("switchType", 1);
//	webMap.put("switchTypeName", "系统页面");
//	webMap.put("myValue", webList);
//	reportMap.put("switchType", 2);
//	reportMap.put("switchTypeName", "报告及清单下载");
//	reportMap.put("myValue", reportList);
//
//	// 同时打开
//	List<SjkgData> oneResultList = sjkgMapper.getOpenSwitchOneDay(parameterData);
//	oprTimeList = new ArrayList<String>();
//	if (oneResultList != null && !oneResultList.isEmpty()) {
//	    strlist = new ArrayList<String>();
//	    StringBuffer showTooltip = new StringBuffer();
//	    for (int i = 0; i < oneResultList.size(); i++) {
//		SjkgData sjkgData = oneResultList.get(i);
//		if (oprTimeList.contains(sjkgData.getOprTime() + sjkgData.getSwitchState())) {
//		} else {
//		    strlist = new ArrayList<String>();
//		    oprTimeList.add(sjkgData.getOprTime());
//		    strlist.add(sjkgData.getOprTime());
//		    strlist.add("1");
//		}
//		showTooltip.append("审计月：" + sjkgData.getAudTrm() + " 开关类型：" + sjkgData.getSwitchTypeName() + ";</br>");
//		if (i == oneResultList.size() - 1 || !(oneResultList.get(i + 1).getOprTime().equals(sjkgData.getOprTime()))) {
//		    showTooltip.append("打开时间：" + sjkgData.getOprTime());
//		    strlist.add(showTooltip.toString());
//		    oneDayList.add(strlist);
//		    showTooltip = new StringBuffer();
//		}
//	    }
//	}
//	oneDayResultMap.put("switchType", 3);
//	oneDayResultMap.put("switchTypeName", "系统页面、报告及清单下载");
//	oneDayResultMap.put("myValue", oneDayList);
//
//	List<Object> result = new ArrayList<Object>();
//	result.add(webMap);
//	result.add(reportMap);
//	result.add(oneDayResultMap);
//	return result;
//
//    }

    /**
     * 
     * <pre>
     * Desc 审计开关-获取气泡图  
     * 步骤1：获取非同一天打开的审计开关
     * 步骤2：根据开关类型的不同，将时间及value值（固定为1）填充到不同的list中：系统页面-webList,报告及清单下载-reportList
     * 步骤3：将webList封装填充到webMap中，将reportList填充到reportMap中。
     * 步骤4：获取同一天打开的审计开关，并填充到结果表中
     * 步骤5：将时间及value值（固定为1）填充到list中：系统页面、报告及清单下载-oneDayList
     * 步骤6：将oneDayList封装填充到oneDayMap中。
     * 步骤7：将webMap、reportMap、oneDayMap填充到结果list中：result。
     * @param parameterData
     * @return
     * @author hufei
     * 2017-11-15 上午10:23:29
     * </pre>
     * 
     * public List<Object> getBubbleChart(SjkgData parameterData){ SjkgMapper sjkgMapper=mybatisDao.getSqlSession().getMapper(SjkgMapper.class); List<SjkgData>
     * resultList=sjkgMapper.getBubbleChart(parameterData); List<String> strlist; List<Object> webList=new ArrayList<Object>(); List<Object> reportList=new ArrayList<Object>(); List<Object>
     * oneDayList=new ArrayList<Object>(); Map<String,Object> webMap=new HashMap<String,Object>(); Map<String,Object> reportMap=new HashMap<String,Object>(); Map<String,Object> oneDayResultMap=new
     * HashMap<String,Object>(); if(resultList!=null&&!resultList.isEmpty()){ for(SjkgData sjkgData:resultList){ strlist=new ArrayList<String>(); strlist.add(sjkgData.getOprTime()); strlist.add("1");
     * if(1==sjkgData.getSwitchType()){ webList.add(strlist); } if(2==sjkgData.getSwitchType()){ reportList.add(strlist); } if(3==sjkgData.getSwitchType()){ oneDayList.add(strlist); } } }
     * webMap.put("switchType",1); webMap.put("switchTypeName","系统页面"); webMap.put("myValue",webList); reportMap.put("switchType", 2); reportMap.put("switchTypeName", "报告及清单下载");
     * reportMap.put("myValue", reportList); oneDayResultMap.put("switchType", 3); oneDayResultMap.put("switchTypeName", "系统页面、报告及清单下载"); oneDayResultMap.put("myValue", oneDayList);
     * 
     * List<Object> result=new ArrayList<Object>(); result.add(webMap); result.add(reportMap); result.add(oneDayResultMap); return result;
     * 
     * }
     */

    /**
     * 
     * <pre>
     * Desc  审计开关-更改开关状态
     * @param parameterData
     * @return
     * @author hufei
     * 2017-11-15 下午3:21:31
     * </pre>
     */
    public int updateSwitchInfo(SjkgData parameterData) {

	List test = Arrays.asList(new String[] { "china", "japan" });

	SjkgMapper sjkgMapper = mybatisDao.getSqlSession().getMapper(SjkgMapper.class);
	
	return sjkgMapper.updateSwitchInfo(parameterData);
    }

    

    /**
     * 
     * <pre>
     * Desc  返回符合格式的年月
     * @param audTrm 月份 ******
     * @return 处理后的月份，****年**月
     * @author hufei
     * 2017-11-14 上午10:39:14
     * </pre>
     */
    private String getAudtrmToString(String audTrm) {
	int thisMonth = Integer.parseInt("0".equals(audTrm.substring(4, 5)) ? audTrm.substring(5, 6) : audTrm.substring(4, 6));
	String result = audTrm.substring(0, 4) + "年" + thisMonth + "月";
	return result;

    }

    /**
     * 
     * <pre>
     * Desc  获取到目前为止的月份列表
     * @return
     * @author hufei
     * 2017-11-15 下午3:21:43
     * </pre>
     */
    private List<String> getAudtrmList() {
	Calendar currentTime = Calendar.getInstance();
	int year = currentTime.get(Calendar.YEAR);
	int month = currentTime.get(Calendar.MONTH) + 1;
	String mon = String.valueOf(month);
	if(mon.length() == 1){
		mon ="0"+mon;
	}
	String yearMonth = String.valueOf(year) + mon;
	return getAudTrmListToSomeDate(yearMonth, "201507");
    }

    private List<String> getAudTrmListToSomeDate(String d1, String d2) {
	List<String> dateList = new ArrayList<String>();
	String temp = d1;
	dateList.add(temp);
	while (!d2.equals(temp)) {
	    temp = getLastMon(temp);
	    dateList.add(temp);
	}
	return dateList;
    }

    private String getLastMon(String d1) {
	Integer thisYear = Integer.parseInt(d1.substring(0, 4));
	Integer thisMonth = Integer.parseInt("0".equals(d1.substring(4, 5)) ? d1.substring(5, 6) : d1.substring(4, 6));
	Integer retYear = thisYear;
	Integer retMonth = 0;
	if (thisMonth >= 2) {
	    retMonth = thisMonth - 1;
	} else if (thisMonth == 1) {
	    retMonth = 12;
	    retYear = thisYear - 1;
	}
	String mon = retMonth.toString();
	StringBuffer sb = new StringBuffer(2);
	sb.append("0");
	if (retMonth <= 9) {
	    sb.append(retMonth);
	    mon = sb.toString();
	}
	return retYear.toString() + mon;
    }

}
