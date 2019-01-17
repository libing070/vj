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
public class JFQLService extends BaseObject{
	
	
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
	public Map<String, Object> load_hz_chart(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("jfqlMapper.load_hz_chart", params);
		return map;
	}
	/**
	 * 汇总页-统计分析-趋势图-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_mx_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("jfqlMapper.load_hz_mx_table", pager);
		return list;
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
	public void export_hz_mx_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = mybatisDao.getList("jfqlMapper.export_hz_mx_table", params);
		setFileDownloadHeader(request, response, "积分变动异常_积分清零与赠送比例异常_汇总.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("审计区间,省代码,省名称,地市代码,地市名称,用户标识,赠送积分值,清零积分值,清零/赠送比例(%)");
    	
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for (Map<String, Object> resultMap : charList) {
    		sb.append(HelperString.objectConvertString(resultMap.get("aud_trm"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("CMCC_prov_prvd_id"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("short_name"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("cmcc_prov_id"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("CMCC_prvd_nm_short"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("subs_id"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("presvalue"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("zerovalue"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("zpper"))).append("\t");
    		out.println(sb.toString());
    		sb.delete(0, sb.length());
    	}
    	
    	out.flush();
    	out.close();
    }
	
	/**
	 * 明细-数据表
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> load_mx_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("jfqlMapper.load_mx_table", pager);
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
		
		setFileDownloadHeader(request, response, "积分变动异常_积分清零与赠送比例异常_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计起始月,审计结束月,省代码,省名称,地市代码,地市名称,用户标识,积分类型,交易积分值,交易状态,清零标志,积分变动对端号码,有效期,交易流水号,交易时间,积分服务渠道标识,业务交易类型,业务交易类型名称,员工标识,姓名,员工岗位,归属渠道标识,渠道名称,渠道类型");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("jfqlMapper.export_mx_table", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm_begin"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("aud_trm_end"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("CMCC_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("CMCC_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("subs_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("points_typ"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("trade_value"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("trade_stat"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("zero_flag"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("opposite_msisdn"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("validity_dt"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("trade_ser_no"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("trade_tm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("points_chnl_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("trade_typ"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("trade_typ_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("emp_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("emp_post"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("blto_chnl_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("chnl_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cor_chnl_id"))).append("\t");
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
