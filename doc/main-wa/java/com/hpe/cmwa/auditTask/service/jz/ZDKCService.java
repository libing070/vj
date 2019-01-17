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
import java.util.HashMap;
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
public class ZDKCService extends BaseObject{
	
	
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
	public Map<String, Object> load_hz_zdcs_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("zdkcMapper.load_hz_zdcs_chart1", params);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		for (int i = 0;i<list.size();i++) {
			paramsmap.put("value"+i, list.get(i).get("imeiFtyNm"));
			params.put("value"+i, list.get(i).get("imeiFtyNm"));
		}
		List<Map<String, Object>> list2 = mybatisDao.getList("zdkcMapper.load_hz_zdcs_chart2", params);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("threeCS", paramsmap);
		map.put("list2", list2);
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
	public List<Map<String, Object>> load_hz_zdcs_conclusion(Map<String, Object> params) {
		return mybatisDao.getList("zdkcMapper.load_hz_zdcs_conclusion", params);
		
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
	public Map<String, Object> load_hz_zdlx_chart(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("zdkcMapper.load_hz_zdlx_chart1", params);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		for (int i = 0;i<list.size();i++) {
			paramsmap.put("value"+i, list.get(i).get("imeiTypNm"));
			params.put("value"+i, list.get(i).get("imeiTypNm"));
		}
		List<Map<String, Object>> list2 = mybatisDao.getList("zdkcMapper.load_hz_zdlx_chart2", params);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("threeCS", paramsmap);
		map.put("list2", list2);
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
	public List<Map<String, Object>> load_hz_zdlx_conclusion(Map<String, Object> params) {
		return mybatisDao.getList("zdkcMapper.load_hz_zdlx_conclusion", params);
		
	}
	
	/**
	 * 获取欠费趋势波动数据表数据
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> load_mx_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("zdkcMapper.load_mx_table", pager);
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
		
		setFileDownloadHeader(request, response, "4.4.5_终端库存合规性_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,终端IMEI,入库时间,终端厂商名称,终端机型名称,终端制式,终端类型");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("zdkcMapper.export_mx_table", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("CMCC_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("imei"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("dat_rcd_dt"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("imei_fty_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("imei_model_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("imei_mode"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("imei_typ_nm")));
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
