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
 * Desc： 个人欠费Service类
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
public class JTQF4001Service extends BaseObject {

	@Autowired
	private MybatisDao	mybatisDao;

	/**
	 * 根据省编码、开始时间、结束时间获取 曲线图、柱状图的数据列表
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @return
	 */
	public List<Map<String, Object>> selectBdqsInfo(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4001Mapper.selectBdqsInfo", params);
		return list;
	}
	

	
	/**
	 * 获取欠费趋势波动数据表数据
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> selectBdqsTableData(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4001Mapper.selectBdqsTableData", pager);
		return list;
	}
	
	/**
	 * 获取欠费趋势波动数据表数据
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> khDataTable(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4001Mapper.khDataTable", pager);
		return list;
	}
	/**
	 * 欠费波动趋势图  欠费账户数审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> selectBodongzhsMax(Map<String, Object> params){
		return mybatisDao.get("jtqf4001Mapper.selectBodongzhsMax", params);
	}
	/**
	 * 欠费波动趋势图  欠费金额审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> selectBodongjeMax(Map<String, Object> params){
		return mybatisDao.get("jtqf4001Mapper.selectBodongjeMax", params);
	}
	/**
	 * 欠费波动趋势图  欠费金额审计结论
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> khconclusion(Map<String, Object> params){
		return mybatisDao.getList("jtqf4001Mapper.khconclusion", params);
	}
	/**
	 * 获取欠费账龄分布波动数据表数据
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> selectZlfbTableData(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4001Mapper.selectZlfbTableData", pager);
		return list;
	}
	
	/**
	 * 欠费账龄趋势图  欠费账户数
	 * @param params
	 * @return
	 */
	public Map<String, Object> getzhanglingZhs(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("jtqf4001Mapper.getzhanglingZhs", params);
		return map;
	}
	/**
	 * 欠费账龄趋势图  全国占比数
	 * @param params
	 * @return
	 */
	public Map<String, Object> getzhanglingQg(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("jtqf4001Mapper.getzhanglingQg", params);
		return map;
	}
	/**
	 * 账龄分布 审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> getzhanglingjielun(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("jtqf4001Mapper.getzhanglingjielun", params);
		return map;
	}
	/**
	 *  账龄分布 全国审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> getQGzhanglingjielun(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("jtqf4001Mapper.getQGzhanglingjielun", params);
		return map;
	}
	/**
	 * 欠费规模分布 数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getguimoPagerList(Pager pager) {
		return mybatisDao.getList("jtqf4001Mapper.getguimoPagerList", pager);
	}
	/**
	 * 欠费规模趋势图  欠费账户数
	 * @param params
	 * @return
	 */
	public Map<String, Object> getguimoZhs(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("jtqf4001Mapper.getguimoZhs", params);
		return map;
	}
	/**
	 * 欠费规模趋势图  全国占比数
	 * @param params
	 * @return
	 */
	public Map<String, Object> getguimoQg(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("jtqf4001Mapper.getguimoQg", params);
		return map;
	}
	/**
	 * 规模分布 审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> getguimojielun(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("jtqf4001Mapper.getguimojielun", params);
		return map;
	}
	/**
	 *  规模分布 全国审计结论
	 * @param params
	 * @return
	 */
	public Map<String, Object> getQGguimojielun(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("jtqf4001Mapper.getQGguimojielun", params);
		return map;
	}
	/**
	 * 获取长期欠费分布图数据
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> selectGeqfInfo(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4001Mapper.selectGeqf1Info", params);
		Double qgsp = mybatisDao.get("jtqf4001Mapper.selectGeqf2Info", params);
		for (Map<String, Object> map : list) {
			map.put("qgsp", qgsp);
		}
		return list;
	}
	/**
	 * 获取长期欠费分布图数据
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> selectCqqfInfo(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4001Mapper.selectCqqfInfo", params);
		Double qgsp = mybatisDao.get("jtqf4001Mapper.selectCqqf2Info", params);
		for (Map<String, Object> map : list) {
			map.put("qgsp", qgsp);
		}
		return list;
	}
	/**
	 * 获取长期欠费数据表数据
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> selectCqqfTableData(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4001Mapper.selectCqqfTableData", pager);
		return list;
	}

	/**
	 * 获取长期欠费审计结论数据
	 * @param pager
	 * @return
	 */
	public Map<String, Object> selectCqqfResult(Map<String, Object> params) {
		Map<String, Object> map= mybatisDao.get("jtqf4001Mapper.selectCqqfResult1", params);
		List<String> list=mybatisDao.getList("jtqf4001Mapper.selectCqqfResult2", params);
		
		StringBuffer cityNames = new StringBuffer() ;
		if(list.size()>0){
			for(String cityName : list){
				cityNames.append(cityName+",");
			}
			if(cityNames.substring(cityNames.length()-1).equals(",")){
				cityNames = cityNames.deleteCharAt(cityNames.length()-1);
			}
		}
		if(map!=null){
			map.put("threeCity", cityNames.toString());
		}
		return map;
	}
	/**
	 * 获取高额欠费审计结论数据
	 * @param pager
	 * @return
	 */
	public Map<String, Object> selectGeqfResult(Map<String, Object> params) {
		Map<String, Object> map= mybatisDao.get("jtqf4001Mapper.selectGeqfResult1", params);
		List<String> list=mybatisDao.getList("jtqf4001Mapper.selectGeqfResult2", params);
		StringBuffer cityNames = new StringBuffer() ;
		if(list.size()>0){
			for(String cityName : list){
				cityNames.append(cityName+",");
			}
			if(cityNames.substring(cityNames.length()-1).equals(",")){
				cityNames = cityNames.deleteCharAt(cityNames.length()-1);
			}
		}
		if(map!=null){
			map.put("threeCity", cityNames.toString());
		}
		return map;
	}
	
	/**
	 * 获取高额欠费数据表数据
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> selectGeqfTableData(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4001Mapper.selectGeqfTableData", pager);
		return list;
	}

	
	/**
	 * 获取明细数据表数据
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> selectDetialTableData(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4001Mapper.selectDetialTableData", pager);
		return list;
	}
	/**
	 * 欠费波动趋势图 数据表导出
	 * @param pager
	 * @return
	 */
	public void exportkhList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4001Mapper.exportkhList", parameterMap);

		setFileDownloadHeader(request, response, "4.2.3_集团客户欠费_汇总_欠费客户.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));

		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,欠费集团帐户标识,欠费集团客户标识,欠费集团客户名称,集团客户等级,集团客户状态,欠费金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("CMCC_prov_prvd_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("CMCC_prov_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("CMCC_prvd_nm_short"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("acct_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("blto_cust_id"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("org_nm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("org_cust_lvl"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cust_stat_typ_nm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("sum_dbt_amt"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}

		out.flush();
		out.close();
	}
	/**
	 * 欠费波动趋势图 数据表导出
	 * @param pager
	 * @return
	 */
	public void exportBodongList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4001Mapper.selectBdqsInfo", parameterMap);

		setFileDownloadHeader(request, response, "4.2.3_集团客户欠费_汇总_欠费波动趋势.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));

		StringBuffer sb = new StringBuffer();
		sb.append("审计月,欠费账户数 ,欠费账户数环比 (%),欠费金额(元),欠费金额环比(%)");
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
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4001Mapper.exportZhangLingList", parameterMap);
		
		setFileDownloadHeader(request, response, "4.2.3_集团客户欠费_汇总_欠费账龄分布.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,欠费账龄,欠费账户数 ,欠费账户数占比 (%),欠费金额(元) ,欠费金额占比(%)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("aging"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("sumAcctNum"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("accNumPer"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("dbtAmt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("dbtAmtSum"))).append("\t");
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
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4001Mapper.exportguimoList", parameterMap);
		
		setFileDownloadHeader(request, response, "4.2.3_集团客户欠费_汇总_欠费规模分布.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,历史欠费规模,欠费账户数 ,欠费账户数占比 (%),欠费金额(元) ,欠费金额占比(%)");
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
	 * 长期欠费 地域分布 数据表导出
	 * @param pager
	 * @return
	 */
	public void exportChangQiList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4001Mapper.selectChangQiList", parameterMap);
		
		setFileDownloadHeader(request, response, "4.2.3_集团客户欠费_汇总_长期欠费管控风险地域分布.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,地市,长期欠费金额(元),长期欠费金额占比(%),全部欠费金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cityName"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("lontTimeDbtAmt"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("lontTimeDbtAmtPer"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("allDbtAmt"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}
		
		out.flush();
		out.close();
	}
	/**
	 * 高额欠费 地域分布 数据表导出
	 * @param pager
	 * @return
	 */
	public void exportGaoEList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("jtqf4001Mapper.selectGaoEList", parameterMap);
		
		setFileDownloadHeader(request, response, "4.2.3_集团客户欠费_汇总_高额欠费管控风险地域分布.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,地市,高额欠费账户数,高额欠费账户占比(%),欠费集团账户数");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("cityName"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("geAcctNum"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("geAcctNumPer"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("sumGeAcctNum"))).append("\t");
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
	public void exportDetailTable(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		setFileDownloadHeader(request, response, "4.2.3_集团客户欠费_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,欠费账期,最早欠费账期,账龄,欠费集团客户标识,欠费集团账户标识,欠费集团客户名称,集团客户状态,集团客户等级,集团业务类型,欠费金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("jtqf4001Mapper.selectDetailList", parameterMap);
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
				sb.append(HelperString.objectConvertString(map.get("minAcctPrdYtm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("aging"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("bltoCustId"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("acctId"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("orgNm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("custStatTypNm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("orgCustLvl"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("org_busn_typ_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("dbtAmt"))).append("\t");
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
