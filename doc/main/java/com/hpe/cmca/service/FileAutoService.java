/**
 * com.hp.cmcc.service.sjzlService.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hpe.cmca.util.DateComputeUtils;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.pojo.confInfo;


@Repository
@Service
public class FileAutoService {
	@Autowired
	private MybatisDao mybatisDao;

	public List<Map<String, Object>> getSJZLGenList(String focusCd) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("focusCd", focusCd);
		List<Map<String, Object>> genList = mybatisDao.getList("commonMapper.getFileGenList",paraMap);
		return genList;
	}

	public Map<String, Object> getRowsCountById(confInfo ci) {
		Map<String, Object> cntMap = mybatisDao.get("commonMapper.getRowsCountById",ci);
		return cntMap;
	}

	public Map<String, Object> getBlockCodeLikeCode(confInfo ci) {
		Map<String, Object> blockCodeMap = mybatisDao.get("commonMapper.getBlockCodeLikeCode",ci);
		return blockCodeMap;
	}

	public void insertNewRow(confInfo ci) {//用于当前数据块生成完毕，插入下一数据块的信息
		mybatisDao.add("commonMapper.insertNewRow",ci);

	}

	public void insertNewBlock(Map<String,String> ci) {//用于插入界面新配置的数据块
		mybatisDao.add("commonMapper.insertNewBlock",ci);

	}

	public void updateRealPoint(confInfo ci) {

			mybatisDao.update("commonMapper.updateRealPoint",ci);
	}

	public void updateNewBlockInfo(confInfo ci) {

		mybatisDao.update("commonMapper.updateNewBlockInfo",ci);
	}

	public void updatePoint(confInfo ci) {

		Map<String, Object> cntMap = getRowsCountById(ci);
		if(((Integer)cntMap.get("cnt"))==0)
			insertNewRow(ci);
		else
			mybatisDao.update("commonMapper.updatePoint",ci);
	}

	public void updateStatusByFocusCd(String focusCd,int status) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("focusCd", focusCd);
		paramMap.put("status", status);
		mybatisDao.update("commonMapper.updateStatusByFocusCd",paramMap);

	}

	public void updateStatusById(Integer id,int status) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", id);
		paramMap.put("status", status);
		mybatisDao.update("commonMapper.updateStatusById",paramMap);

	}

	public List<Map<String,Object>> executeSql(String querySql,String audTrm,List<String> prvdOrderFirst,String prvdId) {

		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		if("".equals(querySql.trim()))
			return mapList;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("audTrm", audTrm);
		paramMap.put("prvdId", prvdId);
		String querySqlEnd = replaceParam(querySql,paramMap);
		paramMap.put("querySql", querySqlEnd.replace("\r", " ").replace("\t", " ").replace("\n", " ") );
		mapList = mybatisDao.getList("commonMapper.executeSql",paramMap);
		int find=0;
		if(prvdOrderFirst==null||prvdOrderFirst.size()==0){
			resultList = mapList;
		}
		else{
			Map<String,Object> tpMap=new HashMap<>();
			for(String ss:prvdOrderFirst){
				find=0;
				for(Map<String,Object> map:mapList){
					if(map.containsValue(ss)) {
						resultList.add(map);
						mapList.remove(map);
						find=1;
						break;
					}
				}
				if(find==0)resultList.add(tpMap);
			}

		}
		return resultList;
	}


	/**
	 * <pre>
	 * Desc
	 * @param 待取代的字符串，关键字用{}分隔
	 * @param 参数值字典，键值为关键字
	 * @return
	 * @author sinly
	 * @refactor sinly
	 * @date   2017年12月6日 上午11:07:58
	 * </pre>
	*/
	public String replaceParam(String text,Map<String, Object> params){
		JexlContext jc = new MapContext(params);
		jc.set("DateUtils", DateComputeUtils.class);
//		jc.set("audTrm",params.get("audTrm"));
//		jc.set("audTrm1",params.get("audTrm1"));


		Matcher m = Pattern.compile("[{]([^{^}]*?)[}]").matcher(text);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			Expression e = new JexlEngine().createExpression(m.group(1));
			Object obj = e.evaluate(jc);
			m.appendReplacement(sb,obj.toString());
		    //m.appendReplacement(sb, params.get(m.group(1)).toString());
		}
		m.appendTail(sb);
		return sb.toString();

	}

	public String replaceAudTrm(String text,String audTrm){

		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("audTrm", audTrm);
		paramMap.put("audTrm1", subDateZero(audTrm.substring(0, 4)+"年"+audTrm.substring(4, 6)+"月"));
		String textEnd = replaceParam(text,paramMap);
		return textEnd;
	}

	public String subDateZero(String text) {//处理02月 01日这样的问题
		Matcher m = Pattern.compile("年([0][1-9])月").matcher(text);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "年"+m.group(1).toString().substring(1,2)+"月");
		}
		m.appendTail(sb);

		Matcher m1 = Pattern.compile("月([0][1-9])日").matcher(sb.toString());
		StringBuffer sb1 = new StringBuffer();
		while (m1.find()) {
			m1.appendReplacement(sb1, "月"+m1.group(1).toString().substring(1,2)+"日");
		}
		m1.appendTail(sb1);

		return sb1.toString();
	}

	public List<String> getAudTrmListToSomeDate(String d1,String d2){
		List<String> dateList =new ArrayList<String>();
		String temp = d1;
		dateList.add(temp);
		while(!d2.equals(temp)){
			temp = getLastMon(temp);
			dateList.add(temp);

		}
		return dateList;
	}
	public String getLastMon(String d1){
		Integer thisYear=Integer.parseInt(d1.substring(0,4));
		Integer thisMonth=Integer.parseInt("0".equals(d1.substring(4,5))?d1.substring(5,6):d1.substring(4,6));
		Integer retYear=thisYear;
		Integer retMonth = 0;
		if(thisMonth >= 2 ){
			retMonth = thisMonth - 1;
		}else if(thisMonth == 1 ){
			retMonth = 12;
			retYear=thisYear - 1 ;
		}
		String mon = retMonth.toString();
		StringBuffer sb = new StringBuffer(2);
		sb.append("0");
		if(retMonth<=9){
			sb.append(retMonth);
			mon = sb.toString();
		}
		return retYear.toString()+mon;
	}

	public String getNumToChar(int i){

		char ch1=0,ch2=0;
		if(i<26){
			ch2 = (char) (i+97);
			return String.valueOf(ch2);
		}
		ch2=(char)(i%26+97);
		if(i>=26)
			ch1 = (char) (i/26-1+97);
		return String.valueOf(ch1)+String.valueOf(ch2);
	}

	public String getAudTrmChinese(String audTrm) {

		Integer thisMonth = Integer.parseInt("0".equals(audTrm.substring(4, 5)) ? audTrm.substring(5, 6) : audTrm.substring(4, 6));
		return audTrm.substring(0, 4) + "年" + thisMonth + "月";
	}

	 /**
	  * 日期减几月
	  */
	 public String dateMinusMonth(String str,Integer i) throws Exception {

	  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	  Date dt = sdf.parse(str);//将字符串生成Date
	  Calendar rightNow = Calendar.getInstance();
	  rightNow.setTime(dt);//使用给定的 Date 设置此 Calendar 的时间。
	  rightNow.add(Calendar.MONTH, i);// 日期减1个月
	  Date dt1 = rightNow.getTime();//返回一个表示此 Calendar 时间值的 Date 对象。
	  String reStr = sdf.format(dt1);//将给定的 Date 格式化为日期/时间字符串，并将结果添加到给定的 StringBuffer。
	  return reStr;
	 }
}
