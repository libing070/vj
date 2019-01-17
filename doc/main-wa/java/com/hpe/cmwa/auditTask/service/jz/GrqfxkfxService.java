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
public class GrqfxkfxService extends BaseObject {

	@Autowired
	private MybatisDao	mybatisDao;
	
	/**
	 * 信控等级汇总统计  数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getXinkongPagerList(Pager pager) {
        return mybatisDao.getList("grqfxkfxMapper.getXinkongPagerList", pager);
    }
	
	/**
	 * 信控等级汇总统计  审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> getXinkongjielun(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfxkfxMapper.getXinkongjielun", params);
		return map;
	}
	
	/**
	 * 超透支额度后欠费账龄分布  数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getxkzhanglingPagerList(Pager pager) {
		return mybatisDao.getList("grqfxkfxMapper.getxkzhanglingPagerList", pager);
	}
	/**
	 * 超透支额度后欠费账龄分布  数据表  审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> getxkzhanglingjielun(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfxkfxMapper.getxkzhanglingjielun", params);
		return map;
	}
	
	/**
	 * 超透支额度后欠费账龄分布  欠费账户数
	 * @param params
	 * @return
	 */
	public Map<String, Object> getxkzhanglingzhs(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfxkfxMapper.getxkzhanglingzhs", params);
		return map;
	}
	/**
	 * 超透支额度后欠费账龄分布  欠费金额
	 * @param params
	 * @return
	 */
	public Map<String, Object> getxkzhanglingje(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfxkfxMapper.getxkzhanglingje", params);
		return map;
	}
	
	/**
	 * 明细 数据表
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getDetList(Pager pager) {
		return mybatisDao.getList("grqfxkfxMapper.getDetList", pager);
	}
	
	/**
	 * 超透支额度后欠费账龄分布 数据表导出
	 * @param pager
	 * @return
	 */
	public void exportxkZhangLingList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("grqfxkfxMapper.exportxkZhangLingList", parameterMap);
		
		setFileDownloadHeader(request, response, "4.1.7_个人客户欠费_引入省公司信控数据分析_超透支额度后欠费账龄分布.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("超约定缴费期限后仍欠费月数,欠费账户数,超约定缴费期限后在审计期间仍出账金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("outOfMon"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("acctNum"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("dbtAmt"))).append("\t");
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
		List<Map<String, Object>> list = mybatisDao.getList("grqfxkfxMapper.exprotDetList", parameterMap);
		
		setFileDownloadHeader(request, response, "4.1.7_个人客户欠费_引入省公司信控数据分析_明细数据.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月, 地市, 欠费账期,最早欠费账期,超透支额度欠费月,超透支额度欠费月数,欠费用户标识,手机号码,欠费客户标识,欠费帐户标识,信控等级编码,信控等级名称,透支额度(元),用户状态,欠费金额(元),最后欠费月套餐");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("acctPrdYtm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("minACCTPrdYtm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("outOfCrDat"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("outOfMon"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("subsId"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("MSISDN"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("bltoCustId"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("acctId"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("crLvlCd"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("crLvlNm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("outOfDbtAmt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("subsStatTypNm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("dbtAmt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("basicPackId"))).append("\t");
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
