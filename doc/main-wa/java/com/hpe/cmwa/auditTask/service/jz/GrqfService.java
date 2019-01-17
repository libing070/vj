/**
 * com.hpe.cmwa.service.DemoService.java
 * Copyright (c) 2016 xx Development Company, L.P.
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
public class GrqfService extends BaseObject {

	@Autowired
	private MybatisDao	mybatisDao;
	
	
	/**
	 * 欠费波动趋势图  欠费账户数
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> selectBodongZhs(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("grqfMapper.selectBodongZhs", params);
		return list;
	}

	/**
	 * 欠费波动趋势图  欠费账户数审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> selectBodongzhsMax(Map<String, Object> params){
		return mybatisDao.get("grqfMapper.selectBodongzhsMax", params);
	}
	/**
	 * 欠费波动趋势图  欠费金额审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> selectBodongjeMax(Map<String, Object> params){
		return mybatisDao.get("grqfMapper.selectBodongjeMax", params);
	}
	
	/**
	 * 欠费波动趋势图 数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getBodongPagerList(Pager pager) {
        return mybatisDao.getList("grqfMapper.getBodongPagerList", pager);
    }
	
	/**
	 * 欠费账龄分布 数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getzhanglingPagerList(Pager pager) {
		return mybatisDao.getList("grqfMapper.getzhanglingPagerList", pager);
	}
	
	/**
	 * 欠费账龄趋势图  欠费账户数
	 * @param params
	 * @return
	 */
	public Map<String, Object> getzhanglingZhs(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfMapper.getzhanglingZhs", params);
		return map;
	}
	/**
	 * 欠费账龄趋势图  全国占比数
	 * @param params
	 * @return
	 */
	public Map<String, Object> getzhanglingQg(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfMapper.getzhanglingQg", params);
		return map;
	}
	/**
	 * 账龄分布 审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> getzhanglingjielun(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfMapper.getzhanglingjielun", params);
		return map;
	}
	/**
	 *  账龄分布 全国审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> getQGzhanglingjielun(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfMapper.getQGzhanglingjielun", params);
		return map;
	}
	
	
	/**
	 * 欠费规模分布 数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getguimoPagerList(Pager pager) {
		return mybatisDao.getList("grqfMapper.getguimoPagerList", pager);
	}
	
	/**
	 * 欠费规模趋势图  欠费账户数
	 * @param params
	 * @return
	 */
	public Map<String, Object> getguimoZhs(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfMapper.getguimoZhs", params);
		return map;
	}
	/**
	 * 欠费规模趋势图  全国占比数
	 * @param params
	 * @return
	 */
	public Map<String, Object> getguimoQg(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfMapper.getguimoQg", params);
		return map;
	}
	/**
	 * 账龄分布 审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> getguimojielun(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfMapper.getguimojielun", params);
		return map;
	}
	/**
	 *  账龄分布 全国审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> getQGguimojielun(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfMapper.getQGguimojielun", params);
		return map;
	}
	
	/**
	 * 长期欠费管控风险地域分布  欠费账户数
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getguankongZhs(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("grqfMapper.getguankongZhs", params);
		return list;
	}
	/**
	 *  长期欠费管控风险地域分布  全国占比
	 * @param params
	 * @return
	 */
	public Map<String, Object> getQGguankongZhs(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfMapper.getQGguankongZhs", params);
		return map;
	}
	
	/**
	 * 长期欠费管控风险地域分布 数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getguankongPagerList(Pager pager) {
		return mybatisDao.getList("grqfMapper.getguankongPagerList", pager);
	}
	
	/**
	 * 长期欠费管控风险地域分布 审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> getguankongjielun(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfMapper.getguankongjielun", params);
		return map;
	}
	/**
	 *  长期欠费管控风险地域分布 全国审计结论
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getguankongThreeCity(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("grqfMapper.getguankongThreeCity", params);
		return list;
	}
	/**
	 * 高额欠费管控风险地域分布  欠费账户数
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getgeguankongZhs(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("grqfMapper.getgeguankongZhs", params);
		return list;
	}
	/**
	 * 高额欠费管控风险地域分布  全国占比
	 * @param params
	 * @return
	 */
	public Map<String, Object> getgeQGguankongZhs(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfMapper.getQGgeguankongZhs", params);
		return map;
	}
	
	/**
	 * 高额欠费管控风险地域分布 数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getgeguankongPagerList(Pager pager) {
		return mybatisDao.getList("grqfMapper.getgeguankongPagerList", pager);
	}
	
	
	/**
	 * 高额欠费管控风险地域分布 审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> getgeguankongjielun(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("grqfMapper.getgeguankongjielun", params);
		return map;
	}
	/**
	 *  高额欠费管控风险地域分布 全国审计结论
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getgeguankongThreeCity(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("grqfMapper.getgeguankongThreeCity", params);
		return list;
	}
	
	/**
	 * 明细 数据表
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getDetList(Pager pager) {
		return mybatisDao.getList("grqfMapper.getDetList", pager);
	}
	
	/**
	 * 欠费波动趋势图 数据表导出
	 * @param pager
	 * @return
	 */
	public void exportBodongList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("grqfMapper.selectBodongZhs", parameterMap);

		setFileDownloadHeader(request, response, "4.1.7_个人客户欠费_总部欠费数据分析__汇总_欠费波动趋势.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));

		StringBuffer sb = new StringBuffer();
		sb.append("审计月,欠费账户数 ,欠费账户数环比 (%),欠费金额(元) ,欠费金额环比(%)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("acctNum"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("acctNumMom"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("dbtAmt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("dbtAmtMom"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}

		out.flush();
		out.close();
	}
	/**
	 * 账龄 数据表导出
	 * @param pager
	 * @return
	 */
	public void exportZhangLingList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("grqfMapper.exportZhangLingList", parameterMap);
		
		setFileDownloadHeader(request, response, "4.1.7_个人客户欠费_总部欠费数据分析__汇总_欠费账龄分布.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,欠费账龄,欠费账户数 ,欠费账户数环比 (%),欠费金额(元) ,欠费金额环比(%)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("aging"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("acctNum"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("acctNumPer"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("dbtAmt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("dbtAmtPer"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}
		
		out.flush();
		out.close();
	}
	/**
	 * 长期欠费管控风险地域分布 数据表导出
	 * @param pager
	 * @return
	 */
	public void exportGuankongList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("grqfMapper.exportGuankongList", parameterMap);
		
		setFileDownloadHeader(request, response, "4.1.7_个人客户欠费_总部欠费数据分析__汇总_长期欠费管控风险地域分布.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,地市,长期欠费金额（元） ,长期欠费金额占比 (%),全部欠费金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("dbtAmt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("qfNumPer"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("dbtAmtAll"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}
		
		out.flush();
		out.close();
	}
	/**
	 * 高额欠费管控风险地域分布 数据表导出
	 * @param pager
	 * @return
	 */
	public void exportgeGuankongList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("grqfMapper.exportgeGuankongList", parameterMap);
		
		setFileDownloadHeader(request, response, "4.1.7_个人客户欠费_总部欠费数据分析__汇总_高额欠费管控风险地域分布.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,地市,高额欠费账户数 ,高额欠费账户占比 (%),欠费个人账户数");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("dbtAmt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("qfNumPer"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("dbtAmtAll"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}
		
		out.flush();
		out.close();
	}
	
	
	/**
	 * 欠费规模 数据表导出
	 * @param pager
	 * @return
	 */
	public void exportguimoList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("grqfMapper.exportguimoList", parameterMap);
		
		setFileDownloadHeader(request, response, "4.1.7_个人客户欠费_总部欠费数据分析__汇总_欠费规模分布.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,历史欠费规模,欠费账户数 ,欠费账户数环比 (%),欠费金额(元) ,欠费金额环比(%)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("abtAmtArr"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("acctNum"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("acctNumPer"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("dbtAmt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("dbtAmtPer"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}
		
		out.flush();
		out.close();
	}
	/**
	 * 欠费规模 数据表导出
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public void exprotDetList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		
		setFileDownloadHeader(request, response, "4.1.7_个人客户欠费_总部欠费数据分析__明细.csv");
		StringBuffer sb = new StringBuffer();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		sb.append("审计月, 省份编码,省份名称,地市编码,地市名称,欠费账期,最早欠费账期,账龄,欠费用户标识, 欠费客户标识 , 欠费帐户标识  ,欠费金额 (元),最后欠费月套餐 ");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("grqfMapper.exprotDetList", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmccProvPrvdId"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("shortName"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmccProvId"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("acctPrdYtm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("minACCTPrdYtm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("aging"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("subsId"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("bltoCustId"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("acctId"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("dbtAmt"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("basicPackId"))).append("\t");
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
