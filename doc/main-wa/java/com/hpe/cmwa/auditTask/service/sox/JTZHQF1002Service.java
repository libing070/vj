/**
 * com.hpe.cmwa.auditTask.service.jz.TFSRHGXService.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.service.sox;

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
 * @date     Jan 15, 2017 8:10:01 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Jan 15, 2017 	   peter.fu 	         1. Created this class. 
 * </pre>  
 */
@Service
public class JTZHQF1002Service  extends BaseObject{

    @Autowired
    private MybatisDao mybatisDao;

	/**
	 * Desc:
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> load_qst_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("jtzhqf1002Mapper.load_qst_chart", params);
		return list;
	}
    
	
	/**
	 * 汇总页-统计分析-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_qst_sjb_table(Pager pager) {
		List<Map<String, Object>> list =new ArrayList<Map<String,Object>>();
		list = mybatisDao.getList("jtzhqf1002Mapper.load_qst_sjb_table", pager);
		return list;
	}
	
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void hz_qst_sjb_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("jtzhqf1002Mapper.hz_qst_sjb_export", parameterMap);
		
		setFileDownloadHeader(request, response, "销账准确性_集团账户同时存在欠费和预存款_同时存在欠费和预存款的账户数量波动趋势_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,同时有欠费和预存款的帐户数量");
		
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			
			sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("acct_cnt"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}
		out.flush();
		out.close();
	}
	
	/**
	 * Desc:
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> load_tj_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("jtzhqf1002Mapper.load_tj_chart", params);
		return list;
	}
	
	/**
	 * 汇总页-统计分析-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_tj_sjb_table(Pager pager) {
		List<Map<String, Object>> list =new ArrayList<Map<String,Object>>();
		list = mybatisDao.getList("jtzhqf1002Mapper.load_tj_sjb_table", pager);
		return list;
	}
	
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void hz_tj_sjb_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("jtzhqf1002Mapper.hz_tj_sjb_export", parameterMap);
		
		setFileDownloadHeader(request, response, "销账准确性_集团账户同时存在欠费和预存款_欠费金额和账本余额统计_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,帐户标识,客户标识,集团客户名称,欠费金额(元),帐本余额(元),账本余额和欠费金额差值(元)");
		
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("acct_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cust_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("org_nm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("dbt_amt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("acct_bk_acum_amt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("diff_amt"))).append("\t");
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
	public List<Map<String, Object>> load_mx_table_qf(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("jtzhqf1002Mapper.load_mx_table_qf", pager);
		return list;
	}
	/**
	 * mx-数据表
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> load_mx_table_zb(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("jtzhqf1002Mapper.load_mx_table_zb", pager);
		return list;
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  明细table导出 SERVICE
	 * @param request
	 * @param response
	 * @param params
	 * @throws Exception
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-12 下午2:39:35
	 * </pre>
	 */
	@DataSourceName("dataSourceGBase")
	public void export_mx_table_qf(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = new ArrayList<Map<String,Object>>(); 
		setFileDownloadHeader(request, response, "销账准确性_集团账户同时存在欠费和预存款_欠费明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,帐户标识,客户标识,集团客户名称,集团客户状态,集团客户等级,欠费帐期,集团业务类型,欠费金额(元)");
		
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for(int i=0;i>=0;i++){
			params.put("pageStar", 10000*i);
			params.put("pageEnd", 10000);
			charList = mybatisDao.getList("jtzhqf1002Mapper.export_mx_table_qf", params);
			if(charList.size()==0){
				break;
			}
			for (Map<String, Object> resultMap : charList) {
				sb.append(HelperString.objectConvertString(resultMap.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("acct_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("cust_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("org_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("cust_stat_typ_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("org_cust_lvl"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("acct_prd_ytm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("org_busn_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("dbt_amt"))).append("\t");
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			charList.clear();
		}
		out.flush();
		out.close();
	}
	/**
	 * 
	 * <pre>
	 * Desc  明细table导出 SERVICE
	 * @param request
	 * @param response
	 * @param params
	 * @throws Exception
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-12 下午2:39:35
	 * </pre>
	 */
	@DataSourceName("dataSourceGBase")
	public void export_mx_table_zb(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = new ArrayList<Map<String,Object>>(); 
		setFileDownloadHeader(request, response, "销账准确性_集团账户同时存在欠费和预存款_账本明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,帐户标识,客户标识,集团客户名称,集团客户状态,集团客户等级,账本科目,账本余额(元)");
		
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for(int i=0;i>=0;i++){
			params.put("pageStar", 10000*i);
			params.put("pageEnd", 10000);
			charList = mybatisDao.getList("jtzhqf1002Mapper.export_mx_table_zb", params);
			if(charList.size()==0){
				break;
			}
			for (Map<String, Object> resultMap : charList) {
				sb.append(HelperString.objectConvertString(resultMap.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("acct_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("cust_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("org_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("cust_stat_typ_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("org_cust_lvl"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("acct_bk_subj_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("acct_bk_acum_amt"))).append("\t");
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			charList.clear();
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
