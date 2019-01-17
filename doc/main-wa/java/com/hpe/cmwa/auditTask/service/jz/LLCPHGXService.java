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
public class LLCPHGXService extends BaseObject{
	
	
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
		List<Map<String, Object>> list = mybatisDao.getList("llcphgxMapper.load_hz_qst_chart", params);
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
	public Map<String, Object> load_hz_qst_conclusion(Map<String, Object> params) {
		Map<String, Object> map= mybatisDao.get("llcphgxMapper.load_hz_qst_conclusion", params);
		return map;
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
		List<Map<String, Object>> list = mybatisDao.getList("llcphgxMapper.load_hz_city_chart", params);
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
	public List<Map<String, Object>> load_hz_city_conclusion(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("llcphgxMapper.load_hz_city_conclusion", params);
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
		List<Map<String, Object>> list = mybatisDao.getList("llcphgxMapper.load_hz_city_conclusion_2", params);
		return list;
	}
	
	/**
	 * 汇总页-统计分析-统计-数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_hz_mx_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("llcphgxMapper.load_hz_mx_table", pager);
		return list;
	}
	/**
	 * 明细-数据表
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> load_mx_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("llcphgxMapper.load_mx_table", pager);
		return list;
	}
	
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void export_hz_mx_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("llcphgxMapper.export_hz_mx_table", parameterMap);
		
		setFileDownloadHeader(request, response, "3.4_流量产品管理合规性_低价套餐产品_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月, 省名称 , 地市名称,违规资费套餐数量,当月新上线资费套餐数量,当月违规订购用户数");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("shortName"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("infracPackNum"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("newPackNum"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("packUserNum")));
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
		
		setFileDownloadHeader(request, response, "3.4_流量产品管理合规性_低价套餐产品_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,资费套餐统一编码,资费套餐名称,资费套餐描述,资费套餐销售状态,资费套餐停售时间,流量是否递延,上线日期,目标区域市场,目标客户群,流量总量,国内通用流量,省内通用流量,闲时流量,4G单模流量,小区流量,语音总量,国内通话,省内通话,本地通话,集团通话,亲情通话,网内通话,小区通话,	闲时通话,短信总量,彩信总量,WLAN总量时长,WLAN总量流量,套餐收入拆分方式,流量单价,国内通话主叫单价,国内通话被叫单价,省内通话主叫单价,省内通话被叫单价,本地主叫通话单价,本地忙时单价,本地闲时单价,小区通话单价,短信单价,彩信单价,WLAN单价（时长）,WLAN单价（流量）,长市漫一体化套餐标识,最低流量单价,最低语音单价,最低短信单价,底限月租费,套餐总价格,当月订购用户数");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list =mybatisDao.getList("llcphgxMapper.export_mx_table", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("AUD_TRM"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("CMCC_PROV_PRVD_ID"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("SHORT_NAME"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("CMCC_PROV_ID"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("CMCC_PRVD_NM_SHORT"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("FEE_PACK_UNIT_CD"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("FEE_PACK_NM"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("FEE_PACK_DESC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("FEE_PACK_STAT"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("PACK_END_DT"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("STRM_AMT_IS_DEF"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("ONLN_DT"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("TGT_RGN_MKT"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("TGT_GRP"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("STRM_TOT"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("DOM_ATHRTY_STRM"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("PROV_ATHRTY_STRM"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("IDLE_TM_STRM"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("SING_MODL_4G_STRM"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("CELL_STRM"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("VOIC_TOT"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("DOM_CALL"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("PROV_CALL"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("LOACL_CALL"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("ORG_CALL"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("FOLK_CALL"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("NETS_CALL"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("CELL_CALL"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("IDLE_TM_CALL"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("SMS_TOT"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("MMS_TOT"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("WLAN_TOT_DUR"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("WLAN_TOT_STRM"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("PACK_INC_SPLT_TYP"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("STRM_PRC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("DOM_CALL_PRC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("DOM_BE_CALL_PRC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("PROV_CALL_PRC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("PROV_BE_CALL_PRC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("LOACL_CALL_PRC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("LOACL_BSY_TM_PRC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("LOACL_IDLE_TM_PRC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("CELL_CALL_PRC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("SMS_PRC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("MMS_PRC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("WLAN_PRC_DUR"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("WLAN_PRC_STRM"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("LONG_PACK_ID"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("MIN_STRM_PRC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("MIN_VOIC_PRC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("MIN_SMS_PRC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("MIN_MON_FEE"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("PACK_TOT_PRC"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("PACK_USER_NUM")));
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
