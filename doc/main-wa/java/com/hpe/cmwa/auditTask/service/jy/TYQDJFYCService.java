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
public class TYQDJFYCService extends BaseObject{
	
	
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
    public List<Map<String,Object>> hz_qst_chart(Map<String, Object> params){
		return mybatisDao.getList("tyqdjfycMapper.hz_qst_chart", params);
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
    public List<Map<String,Object>> hz_qst_table(Pager pager){
		return mybatisDao.getList("tyqdjfycMapper.hz_qst_table", pager);
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
	public void hz_qst_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = mybatisDao.getList("tyqdjfycMapper.hz_qst_export", params);
		setFileDownloadHeader(request, response, "员工异常操作_同一渠道批量缴费异常_批量缴费波动趋势_汇总.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("审计月,省代码,省名称,缴费笔数,天数,交易金额(元)");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for (Map<String, Object> resultMap : charList) {
    		sb.append(HelperString.objectConvertString(resultMap.get("audTrm"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("cmccProvPrvdId"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("shortName"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("countAmtNum"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("days"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("countAmt"))).append("\t");
    		out.println(sb.toString());
    		sb.delete(0, sb.length());
    	}
    	
    	out.flush();
    	out.close();
    }
	
	/**
	 * 
	 * <pre>
	 * Desc  批量缴费业务数量排名前十的渠道
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-14 上午10:01:01
	 * </pre>
	 */
	public List<Map<String,Object>> hz_yw_ten(Map<String, Object> params){
		return mybatisDao.getList("tyqdjfycMapper.hz_yw_ten", params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  排名前十的渠道对应的各自批量缴费业务的名称
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 下午4:29:39
	 * </pre>
	 */
	public List<Map<String,Object>> hz_yw_chart(Map<String, Object> params){
		return mybatisDao.getList("tyqdjfycMapper.hz_yw_chart", params);
	}
	/**
	 * 
	 * <pre>
	 * Desc  排名前十的渠道对应的各自批量缴费业务的名称结论
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-19 下午4:49:26
	 * </pre>
	 */
	public List<Map<String,Object>> hz_yw_table_conclusion(Map<String, Object> params){
		return mybatisDao.getList("tyqdjfycMapper.hz_yw_table_conclusion", params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  渠道批量缴费业务统计 table
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-14 下午4:32:55
	 * </pre>
	 */
	public List<Map<String,Object>> hz_yw_table(Pager pager){
		return mybatisDao.getList("tyqdjfycMapper.hz_yw_table", pager);
	}
	
	/**
	 * 渠道批量缴费业务统计 table导出
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
	public void hz_yw_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = mybatisDao.getList("tyqdjfycMapper.hz_yw_export", params);
		setFileDownloadHeader(request, response, "员工异常操作_同一渠道批量缴费异常_渠道批量缴费业务统计_汇总.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("审计区间,省代码,省名称,地市代码,地市名称,办理业务渠道标识,办理业务渠道名称,业务类型编码,业务类型名称,缴费笔数,天数,交易金额(元)");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for (Map<String, Object> resultMap : charList) {
    		sb.append(HelperString.objectConvertString(resultMap.get("audTrm"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("cmccProvPrvdId"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("shortName"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("cmccProvId"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("cmccPrvdNmShort"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("busiChnlId"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("busiChnlNm"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("busiTyp"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("busiTypNm"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("countAmtNum"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("days"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("countAmt"))).append("\t");
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
		return mybatisDao.getList("tyqdjfycMapper.mx_table", pager);
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
		setFileDownloadHeader(request, response, "员工异常操作_同一渠道批量缴费异常_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,员工标识,姓名,员工岗位,工号归属渠道标识,渠道名称,渠道类型,交易流水号,用户标识,业务类型编码,业务类型名称,交易金额(元),业务办理时间,订单状态,办理业务渠道标识,办理业务渠道名称");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for(int i=0;i>=0;i++){
			params.put("pageStar", 10000*i);
			params.put("pageEnd", 10000);
			charList = mybatisDao.getList("tyqdjfycMapper.mx_table_export", params);
			if(charList.size()==0){
				break;
			}
			for (Map<String, Object> resultMap : charList) {
				sb.append(HelperString.objectConvertString(resultMap.get("audTrm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("cmccProvPrvdId"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("shortName"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("cmccProvId"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("cmccPrvdNmShort"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("empId"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("empPost"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("bltoChnlId"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("chnlNm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("corChnlTyp"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("tradeSerNo"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("subsID"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("busiTyp"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("busiTypNm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("tradeAmt"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("oprTm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("orderTyp"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("busiChnlId"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("busiChnlNm"))).append("\t");
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
