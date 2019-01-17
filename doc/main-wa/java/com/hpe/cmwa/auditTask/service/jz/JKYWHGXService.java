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
public class JKYWHGXService extends BaseObject{
	
	
    @Autowired
    private MybatisDao mybatisDao;
    
    /**
     * 虚假开通家庭宽带波动趋势图
     * @param params
     * @return
     */
    public List<Map<String, Object>> hz_bdqs_chart(Map<String, Object> params) {
		return mybatisDao.getList("jtywhgxMapper.hz_bdqs_chart", params);
	}
    
    /**
     * 疑似虚假开通家庭宽带X笔（省汇总表统计周期内单月疑似虚假办理宽带用户数的最大值）
     * @param params
     * @return
     */
    public Map<String, Object> hz_bdqs_conclusion_max(Map<String, Object> params){
    	return mybatisDao.get("jtywhgxMapper.hz_bdqs_conclusion_max", params);
    }
    
    /**
     * 柱状、折线图：虚假开通家庭宽带统计
     * @param params
     * @return
     */
    public List<Map<String, Object>> hz_tjfx_tj_chart(Map<String, Object> params) {
    	return mybatisDao.getList("jtywhgxMapper.hz_tjfx_tj_chart", params);
    }
    /**
     * 柱状、折线图：虚假开通家庭宽带统计 审计结论
     * @param params
     * @return
     */
    public List<Map<String, Object>> hz_tjfx_tj_conclusion(Map<String, Object> params) {
    	return mybatisDao.getList("jtywhgxMapper.hz_tjfx_tj_conclusion", params);
    }
    /**
     * 柱状、折线图：虚假开通家庭宽带统计 审计结论
     * @param params
     * @return
     */
    public List<Map<String, Object>> hz_tjfx_tj_conclusion_2(Map<String, Object> params) {
    	return mybatisDao.getList("jtywhgxMapper.hz_tjfx_tj_conclusion_2", params);
    }
    
    /**
     * 虚假开通家庭宽带明细数据 数据表格
     * @param pager
     * @return
     */
    public List<Map<String, Object>> hz_tjfx_mx_table(Pager pager) {
		return mybatisDao.getList("jtywhgxMapper.hz_tjfx_mx_table", pager);
	}
    /**
	 * <pre>
	 * Desc  汇总页-统计分析-明细-表格-导出
	 * @param request
	 * @return
	 * @author peter.fu
	 * Jan 15, 2017 10:21:04 PM
	 * </pre>
	 */
    public void export_hz_tjfx_mx_table(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("jtywhgxMapper.export_hz_tjfx_mx_table", parameterMap);

		if("jy".equals(parameterMap.get("expTyp"))){
			setFileDownloadHeader(request, response, "宽带虚假开通_汇总.csv");
		}else{
			setFileDownloadHeader(request, response, "3.2_家宽业务合规性_宽带虚假开通_汇总.csv");
		}
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));

		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,疑似重复办理宽带用户数,办理宽带用户数,违规办理用户数占比(%)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmccProvPrvdId"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("shortName"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmccProvId"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("weiguiNum"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("tolNum"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("perWeigui"))).append("\t");
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
    	return mybatisDao.getList("jtywhgxMapper.mx_table", pager);
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
    	if("jy".equals(parameterMap.get("expTyp"))){
			setFileDownloadHeader(request, response, "宽带虚假开通_明细.csv");
		}else{
			setFileDownloadHeader(request, response, "3.2_家宽业务合规性_宽带虚假开通_明细.csv");
		}
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("审计月,省代码,省名称,地市代码,地市名称,宽带业务用户标识,办理渠道,办理渠道名称,订单类型,账号状态,生效日期,失效日期,开通日期,小区名称,装机地址,联系电话");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("jtywhgxMapper.mx_export_btn", parameterMap);
			if(list.size()==0){
				break;
			}
	    	for (Map<String, Object> map : list) {
	    		sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("cmccProvPrvdId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("shortName"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("cmccProvId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("brdbdSubsId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("procChnl"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("chnlNm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("crderTyp"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("acctNbr"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("effDt"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("endDt"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("crtDt"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("cellNm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("instaLLAddr"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("cntctPh"))).append("\t");
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

	public Map<String, Object> hz_bdqs_ChartAVGNumber(Map<String, Object> params) {
		return  mybatisDao.get("jtywhgxMapper.hz_bdqs_ChartAVGNumber", params);
	}

}
