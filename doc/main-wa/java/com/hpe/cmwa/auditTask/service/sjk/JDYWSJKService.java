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
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Jan 15, 2017 	   renyuxing 	         1. Created this class. 
 * </pre>  
 */
@Service
public class JDYWSJKService extends BaseObject{
	
	
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
		List<Map<String, Object>> list = mybatisDao.getList("jdywsjkMapper.load_column_chart", params);
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
		List<Map<String, Object>> list = mybatisDao.getList("jdywsjkMapper.load_map_chart", params);
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
		List<Map<String, Object>> list = mybatisDao.getList("jdywsjkMapper.load_line_chart", params);
		return list;
	}
	/**
	 * 数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("jdywsjkMapper.load_table", pager);
		return list;
	}
	/**
	 * 数据表导出
	 * @param pager
	 * @return
	 */
	public void exportTable(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("jdywsjkMapper.exportTable", parameterMap);
		if("wgdg".equals(parameterMap.get("diffView"))){
			setFileDownloadHeader(request, response, "违规订购_违规对M2M行业卡用户开通梦网业务和5类基地业务_全国.csv");
		}else{
			setFileDownloadHeader(request, response, "指标作假_违规对欠费停机、预销号用户订购业务_全国.csv");
		}
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,违规办理用户数,违规办理业务笔数,办理用户总数,办理业务总笔数,违规办理用户占比%（违规办理用户数/办理用户总数）,违规办理笔数占比%（违规办理业务笔数/办理业务总笔数）");
		
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("Aud_trm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("USER_NUM"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("BUSI_NUM"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("TOL_USER_NUM"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("TOL_BUSI_NUM"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("PER_USER_NUM"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("PER_BUSI_NUM"))).append("\t");
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
