/**
 * com.hpe.cmwa.auditTask.service.jz.JKYWHGXService.java
 * Copyright (c) 2017 xx Development Company, L.P.
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
public class CJYDJSService extends BaseObject{
	
	
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
	public List<Map<String, Object>> load_qst_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("cjydjsMapper.load_qst_chart", params);
		return list;
	}
	
	

	/**
	 * 汇总页-统计分析-统计-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_qst_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("cjydjsMapper.load_hz_qst_table", pager);
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
	public List<Map<String, Object>> load_ds_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("cjydjsMapper.load_ds_chart", params);
		return list;
	}
	
	/**
	 * 汇总页-统计分析-统计-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_ds_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("cjydjsMapper.load_hz_ds_table", pager);
		return list;
	}
	/**
	 * 明细-数据表
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> load_mx_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("cjydjsMapper.load_mx_table", pager);
		return list;
	}
	
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void export_hz_qst_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("cjydjsMapper.export_hz_qst_table", parameterMap);
		
		setFileDownloadHeader(request, response, "2.7_终端补贴合规性_酬金异地二次结算_波动趋势_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,用户数,异常终端IMEI数,异常占比(%),违规终端涉及酬金(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("Aud_trm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("CMCC_prov_prvd_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("subs_num"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("yc_imei_num"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("per_imei"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("yc_amt"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}
		out.flush();
		out.close();
	}
	
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void export_hz_ds_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("cjydjsMapper.export_hz_ds_table", parameterMap);
		
		setFileDownloadHeader(request, response, "2.7_终端补贴合规性_酬金异地二次结算_地市排名_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,终端IMEI,本省手机号,本省结算酬金金额(元),异省代码,异省名称,异省地市代码,异省地市名称,异省手机号,异省结算酬金金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("Aud_trm"))).append("\t,");
		    sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
		    sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
		    sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
		    sb.append(HelperString.objectConvertString(map.get("local_imei"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("local_sett_msisdn"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("local_pay_sett_amt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("other_cmcc_prov_prvd_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("other_short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("other_cmcc_prov_id"))).append("\t,");
		    sb.append(HelperString.objectConvertString(map.get("other_cmcc_prvd_nm_short"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("other_sett_msisdn"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("other_pay_sett_amt"))).append("\t");
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
	public void export_mx_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list =  new ArrayList<Map<String,Object>>();
		
		setFileDownloadHeader(request, response, "2.7_终端补贴合规性_酬金异地二次结算_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,终端IMEI,手机号,结算酬金的月份,酬金类型,酬金状态,本次结算酬金金额(元),销售渠道标识,异省代码,异省名称");
		
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list =mybatisDao.getList("cjydjsMapper.export_mx_table", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("Aud_trm"))).append("\t,");
			    sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
			    sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			    sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
			    sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("imei"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("local_sett_msisdn"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("local_sett_month"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("local_sett_amt_typ"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("local_sett_stat"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("local_pay_sett_amt"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("local_chnl_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("other_cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("other_short_name"))).append("\t");
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
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
	public List<Map<String, Object>> load_czy_sl_tj_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("cjydjsMapper.load_czy_sl_tj_chart", params);
		return list;
	}
	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {

		// 这里设置一下让浏览器弹出下载提示框,而不是直接在浏览器中打开
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
		response.setContentType("application/octet-stream;charset=GBK");
	}
	

}
