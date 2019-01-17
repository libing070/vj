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
public class CQQFYWService extends BaseObject{
	
	
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
		List<Map<String, Object>> list = mybatisDao.getList("cqqfywMapper.load_hz_qst_chart", params);
		return list;
	}
	
	/**
	 * 汇总页-统计分析-统计-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_qst_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("cqqfywMapper.load_hz_qst_table", pager);
		return list;
	}
	
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void export_hz_qst_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("cqqfywMapper.export_hz_qst_table", parameterMap);
		
		setFileDownloadHeader(request, response, "集团客户欠费异常_长期高额欠费集团客户订购业务_波动趋势_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,长期高额欠费集团订购新业务笔数,涉及集团客户数");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("wg_busi_num"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("wg_cust_num"))).append("\t");
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
	public List<Map<String, Object>> load_hz_city_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("cqqfywMapper.load_hz_city_chart", params);
		return list;
	}
	
	/**
	 * 汇总页-统计分析-地市-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_city_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("cqqfywMapper.load_hz_city_table", pager);
		return list;
	}
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void export_hz_city_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("cqqfywMapper.export_hz_city_table", parameterMap);
		
		setFileDownloadHeader(request, response, "集团客户欠费异常_长期高额欠费集团客户订购业务_违规订购统计_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,长期高额欠费集团订购新业务笔数,涉及集团客户数");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("wg_busi_num"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("wg_cust_num"))).append("\t");
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
	public Map<String, Object> load_hz_city_conclusion(Map<String, Object> params) {
		Map<String, Object> list = mybatisDao.get("cqqfywMapper.load_hz_city_conclusion", params);
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
	public List<Map<String, Object>> load_hz_city_conclusion_2(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("cqqfywMapper.load_hz_city_conclusion_2", params);
		return list;
	}
	
	/**
	 * 明细-数据表
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> load_mx_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("cqqfywMapper.load_mx_table", pager);
		return list;
	}
	/**
	 * 明细 数据表导出
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public void export_mx_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		setFileDownloadHeader(request, response, "集团客户欠费异常_长期高额欠费集团客户订购业务_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,订购日期,账户标识,账户名称,集团客户标识,集团客户名称,集团客户等级,最早欠费月份,欠费账龄,客户欠费总金额（元）,集团业务类型,集团业务类型名称,生效日期,失效日期,订购状态");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("cqqfywMapper.export_mx_table", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("Aud_trm"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("subscrb_dt"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("acct_id"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("acct_nm"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("cust_id"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("cust_nm"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("org_cust_lvl"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("ear_trm"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("acct_age"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("dbt_amt"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("org_svc_typ"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("org_svc_typ_nm"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("eff_dt"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("end_dt"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("subscrb_stat"))).append("\t");
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
