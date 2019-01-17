/**
 * com.hpe.cmwa.auditTask.service.jz.JKYWHGXService.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.service.sjk;

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
 * Desc： 
 * @author   renyuxing
 * @refactor renyuxing
 * @date     Jan 15, 2017 8:01:03 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Jan 15, 2017 	   renyuxing 	         1. Created this class. 
 * </pre>  
 */
@Service
public class TYGHPLJFYCSJKService extends BaseObject{
	
	
    @Autowired
    private MybatisDao mybatisDao;

	/**
	 * <pre>
	 * 柱状图以及地图数据
	 * Desc  
	 * @param formatParameter
	 * @return
	 * @author renyuxing
	 * @refactor renyuxing
	 * @date   2017-1-17 下午8:00:56
	 * </pre>
	*/
	public List<Map<String, Object>> load_column_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("tyghpljfycsjkMapper.load_column_chart", params);
		return list;
	}
	/**
	 * <pre>
	 * 地图数据
	 * Desc  
	 * @param formatParameter
	 * @return
	 * @author renyuxing
	 * @refactor renyuxing
	 * @date   2017-1-17 下午8:00:56
	 * </pre>
	 */
	public List<Map<String, Object>> load_map_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("tyghpljfycsjkMapper.load_map_chart", params);
		return list;
	}
	/**
	 * <pre>
	 * 折线图
	 * Desc  
	 * @param formatParameter
	 * @return
	 * @author renyuxing
	 * @refactor renyuxing
	 * @date   2017-1-17 下午8:00:56
	 * </pre>
	 */
	public List<Map<String, Object>> load_line_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("tyghpljfycsjkMapper.load_line_chart", params);
		return list;
	}
	/**
	 * 数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("tyghpljfycsjkMapper.load_table", pager);
		return list;
	}
	/**
	 * 数据表导出
	 * @param pager
	 * @return
	 */
	public void exportTable(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("tyghpljfycsjkMapper.exportTable", parameterMap);
		
		setFileDownloadHeader(request, response, "员工异常操作_同一工号批量缴费异常_全国.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,员工标识,姓名,业务类型编码,业务类型名称,缴费笔数,日期,交易金额(元)");
		
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("AUD_TRM"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("CMCC_PROV_PRVD_ID"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("SHORT_NAME"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("CMCC_PROV_ID"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("CMCC_PRVD_NM_SHORT"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("EMP_ID"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("EMP_NM"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("BUSI_TYP"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("BUSI_TYP_NM"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("TRADE_CNT"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("TRADE_TM"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("TRADE_AMT"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}
		out.flush();
		out.close();
	}
	
	
	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {

		// 这里设置一下让浏览器弹出下载提示框,而不是直接在浏览器中打开
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
		response.setContentType("application/octet-stream;charset=GBK");
	}

}
