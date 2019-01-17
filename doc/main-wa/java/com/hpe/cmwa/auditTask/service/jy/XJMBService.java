/**
 * com.hpe.cmwa.auditTask.service.jz.JKYWHGXService.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.service.jy;

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
 * @author   peter.fu
 * @refactor peter.fu
 * @date     Jan 15, 2017 8:01:03 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Jan 15, 2017 	   peter.fu 	         1. Created this class. 
 * </pre>  
 */
@Service
public class XJMBService extends BaseObject{
	
	
    @Autowired
    private MybatisDao mybatisDao;

	/**
	 * <pre>
	 * Desc  
	 * @param formatParameter
	 * @return
	 * @author renyuxing
	 * @refactor renyuxing
	 * @date   2017-1-17 下午8:00:56
	 * </pre>
	*/
	public List<Map<String, Object>> load_hz_qst_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("xjmbMapper.load_hz_qst_chart", params);
		return list;
	}

	/**
	 * <pre>
	 * Desc  
	 * @param formatParameter
	 * @return
	 * @author renyuxing
	 * @refactor renyuxing
	 * @date   2017-1-17 下午8:00:56
	 * </pre>
	*/
	public List<Map<String, Object>> load_hz_czy_conclusion(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("xjmbMapper.load_hz_conclusion", params);
		
		return list;
	}
	/**
	 * 汇总页-统计分析-操作员-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_czy_table(Pager pager) {
		List<Map<String, Object>> list =new ArrayList<Map<String,Object>>();
		list = mybatisDao.getList("xjmbMapper.load_hz_czy_table", pager);
		return list;
	}
	
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void export_hz_czy_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("xjmbMapper.export_hz_czy_table", parameterMap);
		
		setFileDownloadHeader(request, response, "虚假开通家庭业务_虚假魔百和互联网电视_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计开始月,审计结束月,省份代码,省名称,地市代码,地市名称,疑似虚假魔百和互联网电视用户数,开通魔百和互联网电视用户总数,占比(%)");
		
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("aud_trm_begin"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("aud_trm_end"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("prvd_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("weigui_subs_cnt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("tol_subs_cnt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("per"))).append("\t");
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
