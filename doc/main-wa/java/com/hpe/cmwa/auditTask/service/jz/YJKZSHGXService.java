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
public class YJKZSHGXService extends BaseObject{
	
	
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
	public Map<String, Object> load_hz_je_chart(Map<String, Object> params) {
		Map<String, Object> list = mybatisDao.get("yjkzshgxMapper.load_hz_je_chart", params);
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
	public Map<String, Object> load_hz_je_chart_1(Map<String, Object> params) {
		Map<String, Object> list = mybatisDao.get("yjkzshgxMapper.load_hz_je_chart_1", params);
		return list;
	}
	
	/**
	 * 
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_je_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("yjkzshgxMapper.load_hz_je_table", pager);
		return list;
	}
	/**
	 * 导出有价卡赠送集中度右侧数据表
	 * 
	 * @param pager
	 * @return
	 */
	public void export_hz_je(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> parameterMap)
			throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("yjkzshgxMapper.export_hz_je", parameterMap);

		setFileDownloadHeader(request, response, "3.11_有价卡赠送合规性_赠送金额汇总分析_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK")); // 导出乱码

		StringBuffer sb = new StringBuffer();
		sb.append("统计月份,科目,本月累计(元),赠送有价卡面值(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("KJQJ"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("kemu"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("yjkamt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("yjkmz"))).append("\t");
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
	public Map<String, Object> load_hz_zsx_chart(Map<String, Object> params) {
		Map<String, Object> list = mybatisDao.get("yjkzshgxMapper.load_hz_zsx_chart", params);
		return list;
	}
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void export_hz_zsx_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		Map<String, Object> map = mybatisDao.get("yjkzshgxMapper.export_hz_zsx_table", parameterMap);
		
		setFileDownloadHeader(request, response, "3.11_有价卡赠送合规性_赠送真实性分析_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计区间, 公文号, 金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		sb.append(HelperString.objectConvertString(map.get("audtrm"))).append("\t,");
		sb.append("有公文号").append("\t,");
		sb.append(HelperString.objectConvertString(map.get("ygwh_sum"))).append("\t");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		sb.append(HelperString.objectConvertString(map.get("audtrm"))).append("\t,");
		sb.append("无公文号").append("\t,");
		sb.append(HelperString.objectConvertString(map.get("wgwh_sum"))).append("\t");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		sb.append("合计").append("\t,");
		sb.append("-").append("\t,");
		sb.append(HelperString.objectConvertString(map.get("total"))).append("\t");
		out.println(sb.toString());
		sb.delete(0, sb.length());
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
	public Map<String, Object> load_hz_jzd_chart_1(Map<String, Object> params) {
		Map<String, Object> list = mybatisDao.get("yjkzshgxMapper.load_hz_jzd_chart_1", params);
		return list;
	}
	/**
	 * 
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_jzd_table_1(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("yjkzshgxMapper.load_hz_jzd_table_1", pager);
		return list;
	}
	
	/**
	 * 
	 * @param pager
	 * @return
	 */
	public void export_hz_jzd_1(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("yjkzshgxMapper.export_hz_jzd_1", parameterMap);
		
		setFileDownloadHeader(request, response, "3.11_有价卡赠送合规性_赠送对象集中度分析_赠送区间统计_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计区间,赠送号码,类型,集团客户名称,赠送总金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audtrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("msisdn"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("user_type"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("org_nm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("yjkamt"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}
		
		out.flush();
		out.close();
	}
	/**
	 * <pre>
	 * Desc  有价卡赠送集中度右侧柱状图
	 * @param formatParameter
	 * @return
	 * @author renyuxing
	 * @refactor renyuxing
	 * @date   2017-1-17 下午8:00:56
	 * </pre>
	*/
	public List<Map<String, Object>> load_hz_jzd_chart_2(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("yjkzshgxMapper.load_hz_jzd_chart_2", params);
		return list;
	}
	
	/**
	 * 有价卡赠送集中度右侧数据表
	 * 
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_jzd_table_2(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList(
				"yjkzshgxMapper.load_hz_jzd_table_2", pager);
		return list;
	}

	/**
	 * 导出有价卡赠送集中度右侧数据表
	 * 
	 * @param pager
	 * @return
	 */
	public void export_hz_jzd_table_2(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> parameterMap)
			throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("yjkzshgxMapper.export_hz_jzd_table_2", parameterMap);

		setFileDownloadHeader(request, response, "3.11_有价卡赠送合规性_赠送对象集中度分析_赠送手机号码_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK")); // 导出乱码

		StringBuffer sb = new StringBuffer();
		sb.append("审计区间,赠送号码 ,类型,集团客户名称,赠送总金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audtrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("msisdn"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("user_type"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("org_nm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("yjkamt"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}
		out.flush();
		out.close();
	}
	
	/**
	 * 加载有价卡赠送用途柱状图数据
	 */
	public Map<String, Object> load_hz_zsyt_chart(Map<String, Object> params) {
		return mybatisDao.get("yjkzshgxMapper.load_hz_zsyt_chart", params);
	}
	
	/**
	 * 加载有价卡赠送用途数据表数据
	 */
	public List<Map<String, Object>> load_hz_zsyt_table(Pager pager) {
		return mybatisDao.getList("yjkzshgxMapper.load_hz_zsyt_table", pager);
	}
	
	/**
	 * 导出有价卡赠送用途数据表数据
	 * 
	 * @param pager
	 * @return
	 */
	public void export_hz_zsyt_table(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> parameterMap)
			throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("yjkzshgxMapper.export_hz_zsyt_table", parameterMap);

		setFileDownloadHeader(request, response, "3.11_有价卡赠送合规性_赠送用途分析 _汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK")); // 导出乱码

		StringBuffer sb = new StringBuffer();
		sb.append("审计区间,有价卡用途,营销案编码,营销案名称,有价卡金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audtrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("yjk_offer_cd"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("yjk_offer_nm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("yjk_purpose"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("total_amt"))).append("\t");
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
	public Map<String, Object> load_hz_yg_chart(Map<String, Object> params) {
		Map<String, Object> list = mybatisDao.get("yjkzshgxMapper.load_hz_yg_chart", params);
		return list;
	}
	/**
	 * 
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_yg_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("yjkzshgxMapper.load_hz_yg_table", pager);
		return list;
	}
	
	/**
	 * 
	 * @param pager
	 * @return
	 */
	public void export_yg_jzd(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("yjkzshgxMapper.export_yg_jzd", parameterMap);
		
		setFileDownloadHeader(request, response, "3.11_有价卡赠送合规性__向员工赠送分析_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计区间,赠送号码,员工姓名,部门,在职/离职,赠送总金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audtrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("msisdn"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("emp_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("emp_dept"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("emp_stat"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("yjkamt"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}
		
		out.flush();
		out.close();
	}
	/**
	 * 
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> load_mx_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("yjkzshgxMapper.load_mx_table", pager);
		return list;
	}
	
	
	
	/**
	 * 
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public void export_mx_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "3.11_有价卡赠送合规性_赠送有价卡汇总分析_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省份代码,省份名称,地市代码,地市名称,有价卡序列号,有价卡赠送时间,获赠有价卡的用户标识,获赠有价卡的客户标识,有价卡类型,获赠有价卡的手机号,有价卡赠送依据,有价卡赠送涉及的营销案编号,有价卡赠送涉及的营销案名称,有价卡赠送文号说明,获赠有价卡到期时间,赠送有价卡金额(元),赠送渠道标识,赠送渠道名称,用途");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("yjkzshgxMapper.export_mx_table", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				 sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("yjk_ser_no"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("yjk_pres_dt"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("user_id"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("cust_id"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("yjk_typ"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("msisdn"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("yjk_dependency"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("yjk_offer_cd"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("yjk_offer_nm"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("offer_word"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("yjk_end_dt"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("yjk_amt"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("chnl_id"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("chnl_nm"))).append("\t,");
				 sb.append(HelperString.objectConvertString(map.get("yjk_purpose"))).append("\t");
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
