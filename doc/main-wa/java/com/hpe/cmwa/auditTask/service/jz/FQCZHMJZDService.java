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
public class FQCZHMJZDService  extends BaseObject{

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
	public List<Map<String, Object>> load_city_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("fqczhmjzdMapper.load_city_chart", params);
		return list;
	}
	/**
	 * Desc:
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @param request
	 * @return
	 */
	public Map<String, Object> load_city_conclusion(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("fqczhmjzdMapper.load_city_conclusion", params);
		return map;
	}
    
	/**
	 * Desc:
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> load_city_conclusion_top3(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("fqczhmjzdMapper.load_city_conclusion_top3", params);
		return list;
	}
	/**
	 * 汇总页-统计分析-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_city_sjb_table(Pager pager) {
		List<Map<String, Object>> list =new ArrayList<Map<String,Object>>();
		list = mybatisDao.getList("fqczhmjzdMapper.load_city_sjb_table", pager);
		return list;
	}
	
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void hz_city_sjb_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("fqczhmjzdMapper.hz_city_sjb_export", parameterMap);
		
		setFileDownloadHeader(request, response, "3.11_有价卡赠送合规性_赠送有价卡发起充值号码集中度_地市排名_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("统计区间,省名称,地市名称,异常发起充值号码数量,充值有价卡金额（元）,充值有价卡数量,被充值号码数量");
		
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("callnumber_cnt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("charge_amt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("charge_yjk_cnt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("charge_msisdn_cnt"))).append("\t");
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
	public List<Map<String, Object>> load_msisdn_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("fqczhmjzdMapper.load_msisdn_chart", params);
		return list;
	}

	
	/**
	 * 汇总页-统计分析-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_msisdn_sjb_table(Pager pager) {
		List<Map<String, Object>> list =new ArrayList<Map<String,Object>>();
		list = mybatisDao.getList("fqczhmjzdMapper.load_msisdn_sjb_table", pager);
		return list;
	}
	
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void hz_msisdn_sjb_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("fqczhmjzdMapper.hz_msisdn_sjb_export", parameterMap);
		
		setFileDownloadHeader(request, response, "3.11_有价卡赠送合规性_赠送有价卡发起充值号码集中度_号码排名_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("统计区间,省名称,地市名称,异常发起充值号码,被充值号码数量,充值有价卡金额(元),充值有价卡数量,其中被充值外省号码数量,其中向外省号码充值有价卡金额(元),其中向外省号码充值有价卡数量");
		
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("callnumber"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("charge_msisdn_cnt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("charge_amt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("charge_yjk_cnt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("ys_charge_msisdn_cnt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("ys_charge_amt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("ys_charge_yjk_cnt"))).append("\t");
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
		List<Map<String, Object>> list = mybatisDao.getList("fqczhmjzdMapper.load_mx_table", pager);
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
	public void export_mx_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = new ArrayList<Map<String,Object>>(); 
		setFileDownloadHeader(request, response, "3.11_有价卡赠送合规性_赠送有价卡发起充值号码集中度_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,有价卡序列号,充值日期,充值时间,有价卡面额(元),充值方式,发起充值号码,被充值号码,被充值号码所属省份,有价卡类型,有价卡赠送时间,获赠有价卡的用户标识,获赠有价卡的手机号,有价卡赠送涉及的营销案编号,营销案名称,营销案种类");
		
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for(int i=0;i>=0;i++){
			params.put("pageStar", 10000*i);
			params.put("pageEnd", 10000);
			charList = mybatisDao.getList("fqczhmjzdMapper.export_mx_table", params);
			if(charList.size()==0){
				break;
			}
			for (Map<String, Object> resultMap : charList) {
				sb.append(HelperString.objectConvertString(resultMap.get("Aud_trm"))).append("\t,");
			    sb.append(HelperString.objectConvertString(resultMap.get("cmcc_prov_prvd_id"))).append("\t,");
			    sb.append(HelperString.objectConvertString(resultMap.get("short_name"))).append("\t,");
			    sb.append(HelperString.objectConvertString(resultMap.get("cmcc_prov_id"))).append("\t,");
			    sb.append(HelperString.objectConvertString(resultMap.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("yjk_ser_no"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("TradeDate"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("TradeTime"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("Yjk_amt"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("TradeType"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("callnumber"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("charge_msisdn"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("charge_prov"))).append("\t,");
			    sb.append(HelperString.objectConvertString(resultMap.get("yjk_typ"))).append("\t,");
			    sb.append(HelperString.objectConvertString(resultMap.get("yjk_pres_dt"))).append("\t,");
			    sb.append(HelperString.objectConvertString(resultMap.get("user_id"))).append("\t,");
			    sb.append(HelperString.objectConvertString(resultMap.get("pres_msisdn"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("yjk_offer_cd"))).append("\t,");
			    sb.append(HelperString.objectConvertString(resultMap.get("offer_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("offer_cls"))).append("\t");
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
