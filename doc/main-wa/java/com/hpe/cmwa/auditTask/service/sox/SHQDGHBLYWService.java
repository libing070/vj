/**
 * com.hpe.cmwa.auditTask.service.jz.JKYWHGXService.java
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
public class SHQDGHBLYWService extends BaseObject{
	
	
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
		List<Map<String, Object>> list = mybatisDao.getList("shqdgjblywMapper.load_hz_qst_chart", params);
		return list;
	}
	
	/**
	 * 汇总页-统计分析-统计-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_qst_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("shqdgjblywMapper.load_hz_qst_table", pager);
		return list;
	}
	
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void export_hz_qst_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("shqdgjblywMapper.export_hz_qst_table", parameterMap);
		
		setFileDownloadHeader(request, response, "业务受理合规性_社会渠道工号在自营厅办理业务_波动趋势_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,违规办理业务笔数,违规办理操作员数,违规办理渠道数");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("busi_num"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("staff_num"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("chnl_num"))).append("\t");
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
		List<Map<String, Object>> list = mybatisDao.getList("shqdgjblywMapper.load_hz_city_chart", params);
		return list;
	}
	
	/**
	 * 汇总页-统计分析-地市-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_city_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("shqdgjblywMapper.load_hz_city_table", pager);
		return list;
	}
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void export_hz_city_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("shqdgjblywMapper.export_hz_city_table", parameterMap);
		
		setFileDownloadHeader(request, response, "业务受理合规性_社会渠道工号在自营厅办理业务_操作员统计_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,操作员工标识,违规办理业务笔数");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("staff_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("busi_num"))).append("\t");
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
		Map<String, Object> list = mybatisDao.get("shqdgjblywMapper.load_hz_city_conclusion", params);
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
		List<Map<String, Object>> list = mybatisDao.getList("shqdgjblywMapper.load_hz_city_conclusion_2", params);
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
	public List<Map<String, Object>> load_hz_qd_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("shqdgjblywMapper.load_hz_qd_chart", params);
		return list;
	}
	
	/**
	 * 汇总页-统计分析-地市-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_qd_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("shqdgjblywMapper.load_hz_qd_table", pager);
		return list;
	}
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void export_hz_qd_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("shqdgjblywMapper.export_hz_qd_table", parameterMap);
		
		setFileDownloadHeader(request, response, "业务受理合规性_社会渠道工号在自营厅办理业务_渠道统计_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,办理业务渠道标识,办理业务渠道名称,违规办理业务笔数");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("busi_chnl_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("busi_chnl_nm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("busi_num"))).append("\t");
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
	public Map<String, Object> load_hz_qd_conclusion(Map<String, Object> params) {
		Map<String, Object> list = mybatisDao.get("shqdgjblywMapper.load_hz_qd_conclusion", params);
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
	public List<Map<String, Object>> load_hz_qd_conclusion_2(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("shqdgjblywMapper.load_hz_qd_conclusion_2", params);
		return list;
	}
	
	/**
	 * 明细-数据表
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> load_mx_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("shqdgjblywMapper.load_mx_table", pager);
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
		
		setFileDownloadHeader(request, response, "业务受理合规性_社会渠道工号在自营厅办理业务_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,操作流水号,操作员工标识,办理业务渠道标识,办理业务渠道名称,业务受理类型编码,业务受理类型名称,用户标识,业务类型编码,业务类型名称,业务办理时间,订单状态,订购业务生效时间,订购业务失效时间");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("shqdgjblywMapper.export_mx_table", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("opr_ser_no"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("staff_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("busi_chnl_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("busi_chnl_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("busi_acce_typ"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("busi_acce_typ_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("subs_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("busi_typ_no"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("busi_typ_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("busi_opr_tm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("order_stat"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("busi_eff_dt"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("busi_end_dt"))).append("\t");
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
