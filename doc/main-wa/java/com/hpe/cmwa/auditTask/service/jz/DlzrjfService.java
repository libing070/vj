/**
 * com.hpe.cmwa.service.DemoService.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.service.jz;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.common.datasource.DataSourceName;
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;

/**
 * <pre>
 * Desc：同一工号操作大量转入积分Service类
 * @author   peter.fu
 * @refactor peter.fu
 * @date     Nov 17, 2016 9:53:01 AM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Nov 17, 2016 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
@Service
public class DlzrjfService extends BaseObject {

	@Autowired
	private MybatisDao	mybatisDao;
	
	
	/**
	 * 大量转入积分值波动趋势图  审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> getbodongjielun(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("dlzrjfMapper.getbodongjielun", params);
		return map;
	}
	
	/**
	 * 省汇总表统计周期内每月异常转入积分值 波动趋势图数据
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getbodongZRValue(Map<String, Object> params) {
		return mybatisDao.getList("dlzrjfMapper.getbodongZRValue", params);
	}
	/**
	 * 省汇总表统计周期内全省的平均单月异常转入积分值 波动趋势图数据
	 * @param params
	 * @return
	 */
	public Map<String, Object> getbodongPrvdAVG(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("dlzrjfMapper.getbodongPrvdAVG", params);
		return map;
	}
	/**
	 * 同一工号办理同一号码大量转入积分统计波动趋势图数据
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getjftongji(Map<String, Object> params) {
		return mybatisDao.getList("dlzrjfMapper.getjftongji", params);
	}
	
	
	
	/**
	 * 同一工号办理同一号码大量转入积分的情况，共涉及工号XX个、X积分、用户数XX个
	 * @param params
	 * @return
	 */
	public Map<String, Object> getjfzrqktongji(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("dlzrjfMapper.getjfzrqktongji", params);
		return map;
	}
	/**
	 * 
	 * @param params
	 * @return
	 */
	public Map<String, Object> getMaxPerCity(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("dlzrjfMapper.getMaxPerCity", params);
		return map;
	}
	
	/**
	 * 大量转入积分值波动趋势 明细数据
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> gettongjiSJB(Pager pager) {
		return mybatisDao.getList("dlzrjfMapper.gettongjiSJB", pager);
	}
	
	/**
	 * 明细 数据表
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getDetList(Pager pager) {
		return mybatisDao.getList("dlzrjfMapper.getDetList", pager);
	}
	
	/**
	 * 同一工号办理同一号码大量转入积分统计 数据表导出
	 * @param pager
	 * @return
	 */
	public void exprottongji(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("dlzrjfMapper.exprottongji", parameterMap);
		
		if("jy".equals(parameterMap.get("expTyp"))){
			setFileDownloadHeader(request, response, "员工异常操作_积分异常操作_汇总.csv");
		}else{
			setFileDownloadHeader(request, response, "4.6.1_积分合规性_同一工号操作大量转入积分_汇总.csv");
		}
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,地市名称,异常操作员工数,积分异常用户数量,异常转入积分值,总转入积分值,总操作员工数,异常转入积分值占比(%)");
		
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("weiguiStaffNum"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("weiguiSubsNum"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("weiguiShiftValue"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("tolShiftValue"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("tolStaffNum"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("perValue"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}
		
		out.flush();
		out.close();
	}
	/**
	 * 明细 数据表导出
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public void exprotDetList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		if("jy".equals(parameterMap.get("expTyp"))){
			setFileDownloadHeader(request, response, "员工异常操作_积分异常操作_明细.csv");
		}else{
			setFileDownloadHeader(request, response, "4.6.1_积分合规性_同一工号操作大量转入积分_明细.csv");
		}
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省份代码,省份名称,地市代码,地市名称,交易流水号,操作员工标识,操作员工姓名,交易时间,手机号,业务交易类型,用户标识,积分服务渠道,积分类型,积分变动对端号码,转入积分值,有效期");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("dlzrjfMapper.exprotDetList", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("shortName"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmccProvId"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("tradeSerNo"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("staffId"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("tradeTm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("MSISDN"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("tradeTypName"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("subsId"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("pointsChnlName"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("pointsTypName"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("oppositeMsisdn"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("tradeValue"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("validityDt"))).append("\t");
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
		}
		
		out.flush();
		out.close();
	}
	

	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {

		// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
		response.setContentType("application/octet-stream;charset=GBK");
	}
}
