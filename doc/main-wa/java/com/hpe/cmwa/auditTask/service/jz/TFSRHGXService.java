/**
 * com.hpe.cmwa.auditTask.service.jz.TFSRHGXService.java
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
public class TFSRHGXService  extends BaseObject{

    @Autowired
    private MybatisDao mybatisDao;
    
    /**
     * 统付收入合规性趋势 chart
     * @param params
     * @return
     */
    public List<Map<String, Object>> hz_hgxqs_je_chart(Map<String, Object> params) {
		return mybatisDao.getList("tfsrhgxMapper.hz_hgxqs_je_chart", params);
	}
    /**
     * 统付收入合规性趋势 chart
     * @param params
     * @return
     */
    public List<Map<String, Object>> hz_hgxqs_yhs_chart(Map<String, Object> params) {
    	return mybatisDao.getList("tfsrhgxMapper.hz_hgxqs_yhs_chart", params);
    }
    
    /**
     * 各地市统付收入合规性分析 chart
     * @param params
     * @return
     */
    public List<Map<String, Object>> hz_hgxfx_qst_chart(Map<String, Object> params) {
    	return mybatisDao.getList("tfsrhgxMapper.hz_hgxfx_qst_chart", params);
    }
    /**
     * 各地市统付收入合规性分析 tab
     * @param pager
     * @return
     */
    public List<Map<String, Object>> hz_hgxfx_mx_table(Pager pager) {
		return mybatisDao.getList("tfsrhgxMapper.hz_hgxfx_mx_table", pager);
	}
    
    /**
     * 统付收入统计 tab 导出
     * @param request
     * @param response
     * @param parameterMap
     * @throws Exception
     */
    public void hz_hgxfx_mx_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("tfsrhgxMapper.hz_hgxfx_mx_export", parameterMap);

		setFileDownloadHeader(request, response, "4.2.2_统付收入合规性_违规将成员数小于3的集团客户纳入统付收入_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));

		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省名称,地市名称,成员数小于3的集团客户数,违规统付收入金额(元),统付集团客户数,统付总收入金额(元),违规统付集团客户数占比(%),违规统付收入占比(%)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("shortName"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("wgCustNum"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("wgMerAmt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("tolCustNum"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("tolMerAmt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("wgCustPer"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("wgMerAmtPer"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}

		out.flush();
		out.close();
	}
    
    
    /**
     * 明细 数据表格
     * @param pager
     * @return
     */
    @DataSourceName("dataSourceGBase")
    public List<Map<String, Object>> mx_table(Pager pager) {
    	return mybatisDao.getList("tfsrhgxMapper.mx_table", pager);
    }
    /**
     * <pre>
     * Desc  明细-表格-导出
     * @param request
     * @return
     * @author peter.fu
     * Jan 15, 2017 10:21:04 PM
     * </pre>
     */
    @DataSourceName("dataSourceGBase")
    public void mx_export_btn(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
    	List<Map<String, Object>> list =  new ArrayList<Map<String,Object>>();
    	
    	setFileDownloadHeader(request, response, "4.2.2_统付收入合规性_违规将成员数小于3的集团客户纳入统付收入_明细.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("审计月,省代码,省名称,地市代码,地市名称,集团客户标识,集团客户名称,个人用户标识,集团帐户标识,综合帐目科目编码,综合帐目科目名称,增值税税率,统付收入(元)");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("tfsrhgxMapper.mx_export_btn", parameterMap);
			if(list.size()==0){
				break;
			}
	    	for (Map<String, Object> map : list) {
	    		sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("cmccProvPrvdId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("shortName"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("cmccProvId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("orgCustId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("orgNm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("indvlSubsId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("orgAcctId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("comptAcctSubjId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("comptAcctsSubjNm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("vatRatNm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("merAmt"))).append("\t");
	    		out.println(sb.toString());
	    		sb.delete(0, sb.length());
	    	}
	    	list.clear();
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
