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
 * 
 * <pre>
 * Desc： 同一渠道批量缴费异常 SERVICE
 * @author jh
 * @refactor jh
 * @date   2017-4-13 下午3:12:31
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-4-13 	   jh 	         1. Created this class. 
 * </pre>
 */
@Service
public class JTSRYCBDService extends BaseObject{
	
	
    @Autowired
    private MybatisDao mybatisDao;
    
    /**
     * 
     * <pre>
     * Desc  批量缴费波动趋势Chart
     * @param params
     * @return
     * @author jh
     * @refactor jh
     * @date   2017-4-13 下午4:12:13
     * </pre>
     */
    public List<Map<String,Object>> hz_cj_chart(Map<String, Object> params){
		return mybatisDao.getList("jtsrycbdMapper.hz_cj_chart", params);
	}
    /**
     * 
     * <pre>
     * Desc  
     * @param pager
     * @return
     * @author jh
     * @refactor jh
     * @date   2017-4-13 下午5:13:15
     * </pre>
     */
    public List<Map<String,Object>> hz_cj_table(Pager pager){
		return mybatisDao.getList("jtsrycbdMapper.hz_cj_table", pager);
	}
    /**
	 * 
	 * <pre>
	 * Desc  
	 * @param request
	 * @param response
	 * @param params
	 * @throws Exception
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-13 下午5:12:56
	 * </pre>
	 */
	public void hz_cj_table_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = mybatisDao.getList("jtsrycbdMapper.hz_cj_table_export", params);
		setFileDownloadHeader(request, response, "集团产品收入异常_集团产品收入异常波动_汇总_波动趋势.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	
    	StringBuffer sb = new StringBuffer();
    	String areaName = (params.get("areaCode1")==null||"".equals(params.get("areaCode1")))?"省份名称":"地市名称";
    	sb.append("审计月,省名称,地市名称,集团业务类型,集团业务类型名称,集团产品收入（元）,环比（%）");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for (Map<String, Object> resultMap : charList) {
    		sb.append(HelperString.objectConvertString(resultMap.get("aud_trm"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("area_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("org_svc_typ"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("org_svc_typ_nm"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("unitpay_inc"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("rela_rat"))).append("\t");
    		out.println(sb.toString());
    		sb.delete(0, sb.length());
    	}
    	out.flush();
    	out.close();
    }
    
	/**
	 * 
	 * <pre>
	 * Desc  
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-14 上午10:01:01
	 * </pre>
	 */
	public List<Map<String,Object>> hz_hb_chart1(Map<String, Object> params){
		return mybatisDao.getList("jtsrycbdMapper.hz_hb_chart1", params);
	}
    
	
	/**
	 * 
	 * <pre>
	 * Desc  
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 下午4:29:39
	 * </pre>
	 */
	public List<Map<String,Object>> hz_hb_chart2(Map<String, Object> params){
		return mybatisDao.getList("jtsrycbdMapper.hz_hb_chart2", params);
	}

	
	/**
	 * 
	 * <pre>
	 * Desc  
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-14 下午4:32:55
	 * </pre>
	 */
	public List<Map<String,Object>> hz_hb_table(Pager pager){
		return mybatisDao.getList("jtsrycbdMapper.hz_hb_table", pager);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  
	 * @param request
	 * @param response
	 * @param params
	 * @throws Exception
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-14 下午4:32:38
	 * </pre>
	 */
	public void hz_hb_table_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = mybatisDao.getList("jtsrycbdMapper.hz_hb_table_export", params);
		setFileDownloadHeader(request, response, "集团产品收入异常_集团产品收入异常波动_汇总_环比波动排名.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	
    	StringBuffer sb = new StringBuffer();
    	String areaName = (params.get("areaCode1")==null||"".equals(params.get("areaCode1")))?"省份名称":"地市名称";
    	sb.append("审计起始月,审计结束月,省名称,地市名称,集团业务类型,集团业务类型名称,环比波动(%)");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for (Map<String, Object> resultMap : charList) {
    		sb.append(HelperString.objectConvertString(resultMap.get("aud_trm_begin"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("aud_trm_end"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("area_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("org_svc_typ"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("org_svc_typ_nm"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("rat_wave"))).append("\t");
    		out.println(sb.toString());
    		sb.delete(0, sb.length());
    	}
    	
    	out.flush();
    	out.close();
    }
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * <pre>
	 * Desc  
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-14 上午10:01:01
	 * </pre>
	 */
	public List<Map<String,Object>> hz_fc_chart1(Map<String, Object> params){
		return mybatisDao.getList("jtsrycbdMapper.hz_fc_chart1", params);
	}
    
	
	/**
	 * 
	 * <pre>
	 * Desc  
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 下午4:29:39
	 * </pre>
	 */
	public List<Map<String,Object>> hz_fc_chart2(Map<String, Object> params){
		return mybatisDao.getList("jtsrycbdMapper.hz_fc_chart2", params);
	}

	
	/**
	 * 
	 * <pre>
	 * Desc  
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-14 下午4:32:55
	 * </pre>
	 */
	public List<Map<String,Object>> hz_fc_table(Pager pager){
		return mybatisDao.getList("jtsrycbdMapper.hz_fc_table", pager);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  
	 * @param request
	 * @param response
	 * @param params
	 * @throws Exception
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-14 下午4:32:38
	 * </pre>
	 */
	public void hz_fc_table_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = mybatisDao.getList("jtsrycbdMapper.hz_fc_table_export", params);
		setFileDownloadHeader(request, response, "集团产品收入异常_集团产品收入异常波动_汇总_方差波动排名.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	
    	StringBuffer sb = new StringBuffer();
    	String areaName = (params.get("areaCode1")==null||"".equals(params.get("areaCode1")))?"省份名称":"地市名称";
    	sb.append("审计起始月,审计结束月,省名称,地市名称,集团业务类型,集团业务类型名称,方差波动(%)");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for (Map<String, Object> resultMap : charList) {
    		sb.append(HelperString.objectConvertString(resultMap.get("aud_trm_begin"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("aud_trm_end"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("area_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("org_svc_typ"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("org_svc_typ_nm"))).append("\t,");
			sb.append(HelperString.objectConvertString(resultMap.get("var_wave"))).append("\t");
    		out.println(sb.toString());
    		sb.delete(0, sb.length());
    	}
    	
    	out.flush();
    	out.close();
    }
	
	
	/**
	 * 
	 * <pre>
	 * Desc  明细数据table
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-13 下午12:28:43
	 * </pre>
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String,Object>> mx_table(Pager pager){
		return mybatisDao.getList("jtsrycbdMapper.mx_table", pager);
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
	public void mx_table_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = new ArrayList<Map<String,Object>>(); 
		setFileDownloadHeader(request, response, "集团产品收入异常_集团产品收入异常波动_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省名称,地市名称,集团客户标识,集团客户名称,集团业务类型,集团业务类型名称,产品订购实例ID,费项类型编码,费项类型名称,集团产品收入（元）");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for(int i=0;i>=0;i++){
			params.put("pageStar", 10000*i);
			params.put("pageEnd", 10000);
			charList = mybatisDao.getList("jtsrycbdMapper.mx_table_export", params);
			if(charList.size()==0){
				break;
			}
			for (Map<String, Object> resultMap : charList) {
				
				sb.append(HelperString.objectConvertString(resultMap.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("CMCC_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("org_cust_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("org_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("org_svc_typ"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("org_svc_typ_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("prod_subscrb_exmbl_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("fee_itm_typ_cd"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("fee_itm_typ_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("unitpay_inc"))).append("\t");
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			charList.clear();
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
