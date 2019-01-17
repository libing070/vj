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
public class TYGHJFYCService extends BaseObject{
	
	
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
		List<Map<String, Object>> list = mybatisDao.getList("tyghjfycMapper.load_hz_qst_chart", params);
		return list;
	}
	/**
	 * 汇总页-统计分析-趋势图-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_qst_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("tyghjfycMapper.load_hz_qst_table", pager);
		return list;
	}
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void export_hz_qst_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("tyghjfycMapper.export_hz_qst_table", parameterMap);
		
		setFileDownloadHeader(request, response, "员工异常操作_同一工号批量缴费异常_批量缴费波动趋势_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,缴费笔数,天数,交易金额(元)");
		
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("TRADE_CNT"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("tianshu"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("TRADE_AMT"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}
		out.flush();
		out.close();
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
	public List<Map<String, Object>> load_hz_czy_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("tyghjfycMapper.load_hz_czy_chart", params);
		
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
		List<Map<String, Object>> list = mybatisDao.getList("tyghjfycMapper.load_hz_czy_conclusion", params);
		
		return list;
	}
	/**
	 * 汇总页-统计分析-操作员-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_czy_table(Pager pager) {
		List<Map<String, Object>> list =new ArrayList<Map<String,Object>>();
		list = mybatisDao.getList("tyghjfycMapper.load_hz_czy_table", pager);
		return list;
	}
	
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void export_hz_czy_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("tyghjfycMapper.export_hz_czy_table", parameterMap);
		
		setFileDownloadHeader(request, response, "员工异常操作_同一工号批量缴费异常_员工批量缴费业务统计_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计区间,省代码,省名称,地市代码,地市名称,员工标识,姓名,业务类型编码,业务类型名称,缴费笔数,天数,交易金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			
			sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("CMCC_prvd_nm_short"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("EMP_ID"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("EMP_NM"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("busi_typ"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("busi_typ_nm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("TRADE_CNT"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("TRADE_TM"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("TRADE_AMT"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}
		out.flush();
		out.close();
	}
	
	/**
	 * mx-数据表
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> load_mx_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("tyghjfycMapper.load_mx_table", pager);
		return list;
	}
	
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public void export_mx_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		setFileDownloadHeader(request, response, "员工异常操作_同一工号批量缴费异常_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,员工标识,姓名,员工岗位,工号归属渠道标识,渠道名称,渠道类型,交易流水号,用户标识,业务类型编码,业务类型名称,交易金额(元),业务办理时间,订单状态,办理业务渠道标识,办理业务渠道名称");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("tyghjfycMapper.export_mx_table", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("emp_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("emp_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("emp_post"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("emp_chnl_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("emp_chnl_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("emp_chnl_typ"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("trade_ser_no"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("subs_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("busi_typ"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("busi_typ_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("trade_amt"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("opr_tm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("order_typ"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("busi_chnl_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("busi_chnl_nm"))).append("\t");
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
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
