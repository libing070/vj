/**
 * com.hpe.cmwa.service.DemoService.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.service.jz;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
 * Desc：个人欠费Service类
 * @author   peter.fu
 * @refactor peter.fu
 * @date     Nov 17, 2016 9:53:01 AM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Nov 17, 2016 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
@Service
public class JTQF4002Service extends BaseObject {

	@Autowired
	private MybatisDao	mybatisDao;
	
	/**
	 * 信控等级汇总统计  数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getXinkongPagerList(Pager pager) {
        return mybatisDao.getList("jtqf4002Mapper.getXinkongPagerList", pager);
    }
	
	/**
	 * 信控等级汇总统计  审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> getXinkongjielun(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("jtqf4002Mapper.getXinkongjielun", params);
		return map;
	}
	
	/**
	 * 超约定缴费期限后欠费账龄分布  数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getxkzhanglingPagerList(Pager pager) {
		return mybatisDao.getList("jtqf4002Mapper.getxkzhanglingPagerList", pager);
	}
	/**
	 * 超约定缴费期限后欠费账龄分布  数据表  审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> getxkzhanglingjielun(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("jtqf4002Mapper.getxkzhanglingjielun", params);
		return map;
	}
	
	/**
	 * 超约定缴费期限后欠费账龄分布  欠费账户数
	 * @param params
	 * @return
	 */
	public Map<String, Object> getxkzhanglingzhs(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("jtqf4002Mapper.getxkzhanglingzhs", params);
		return map;
	}
	/**
	 * 超约定缴费期限后欠费账龄分布  欠费金额
	 * @param params
	 * @return
	 */
	public Map<String, Object> getxkzhanglingje(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("jtqf4002Mapper.getxkzhanglingje", params);
		return map;
	}
	
	/**
	 * 明细 数据表
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getDetList(Pager pager) {
		return mybatisDao.getList("jtqf4002Mapper.getDetList", pager);
	}
	
	/**
	 * 超约定缴费期限后欠费账龄分布 数据表导出
	 * @param pager
	 * @return
	 */
	public void exportxkZhangLingList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4002Mapper.exportxkZhangLingList", parameterMap);
		
		setFileDownloadHeader(request, response, "4.2.3_集团客户欠费_引入省公司信控数据分析_超约定缴费期限后欠费账龄分布.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("超约定缴费期限后仍欠费月数,欠费账户数,超约定缴费期限后在审计期间仍出账金额（元）");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("outOfMon"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("acctNum"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("dbtAmt")));
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
	public void exprotDetList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4002Mapper.exprotDetList", parameterMap);
		
		setFileDownloadHeader(request, response, "4.2.3_集团客户欠费_引入省公司信控数据分析_明细数据.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("审计月,省份编码,省份名称,地市编码,地市名称,欠费账期,超约定缴费期限欠费月,超约定缴费期限后仍欠费月数,欠费集团帐户标识,欠费集团客户标识,欠费集团客户名称,集团客户等级,集团客户状态,信控等级编码,信控等级名称,延长缴费月份,欠费金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("cmccProvPrvdId"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("shortName"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("cmccProvId"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("acctPrdYtm"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("outOfCrDat"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("outOfMon"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("acctId"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("bltoCustId"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("orgNm"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("orgCustLvl"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("custStatTypNm"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("crLvlCd"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("crLvlNm"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("delMon"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("dbtAmt")));
			out.println(sb.toString());
			sb.delete(0, sb.length());
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
