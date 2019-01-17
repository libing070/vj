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
import org.springframework.jdbc.core.JdbcTemplate;
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
public class GroupArrearsService extends BaseObject {

	@Autowired
	private MybatisDao	mybatisDao;
	@Autowired
	private JdbcTemplate jdbcTemplate ;

	/**
	 * 根据省编码、开始时间、结束时间获取 曲线图、柱状图的数据列表
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @return
	 */
	public List<Map<String, Object>> selectIntegralFeedbackRate(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("groupArrearsMapper.selectPersonPrvdInfo", params);
		return list;
	}

	/**
	 * 根据省份编号、审计时间获取该省的分析数据
	 * @param cmccProvPrvdId
	 * @param audTrmEnd
	 * @return
	 */
	public List<Map<String, Object>> selectDataAnalysis(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("groupArrearsMapper.selectDataAnalysis", params);
		return list;
	}

	/**
	 * 根据省份编号、审计时间获取地市排行前三的地市名称
	 * @param cmccProvPrvdId
	 * @param audTrmEnd
	 * @return
	 */
	public List<String> selectThreeCity(Map<String, Object> params) {
		List<String> list = mybatisDao.getList("groupArrearsMapper.selectThreeCity", params);
		return list;
	}

	/**
	 * 根据 审计时间获取全国各省数据(全国排名)
	 * @param cmccProvPrvdId
	 * @param audTrmEnd
	 * @return
	 */
	/*public List<Map<String, Object>> selectAllPrvd(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("groupArrearsMapper.selectAllPrvd", params);
		return list;
	}*/

	/**
	 * 根据省份编号和审计时间获取全省所有地市数据展示成地图
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> selectAllMapCity(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("groupArrearsMapper.selectMapData", params);
		return list;
	}

	/**
	 * 根据省份编号和审计时间获取全省所有地市数据
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> selectAllCityPerson(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("groupArrearsMapper.selectAllCityPerson", pager);
		return list;
	}

	/**
	 * 导出 根据省份编号和审计时间获取全省各地市 个人客户欠费 数据
	 * @param pager
	 * @return
	 */
	public void exportAllCityPerson(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		
		List<Map<String, Object>> list = mybatisDao.getList("groupArrearsMapper.exportCityPerson", parameterMap);
		setFileDownloadHeader(request, response, "4.2.3_集团客户欠费_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,地市名称,异常欠费集团客户数 ,欠费金额,集团用户欠费金额排名");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		int i = 0;
		for (Map<String, Object> map : list) {
			i++;
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("sumCustId"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("sumDbtAmt"))).append(",");
			sb.append(HelperString.objectConvertString(i));

			out.println(sb.toString());
			sb.delete(0, sb.length());
		}

		out.flush();
		out.close();
	}

	/**
	 * 全国所有欠费个人客户数据查询
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> selectAllCinaPerson(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("groupArrearsMapper.selectAllChinaPerson", pager);
		return list;
	}

	/**
	 * 导出全国所有个人客户清单查询
	 * @param pager
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public void exportAllChinaPerson(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		
		/*jdbcTemplate.query("select * from a where id=? and name=?", new Object[]{1,"fusz"}, new RowCallbackHandler(){
			
			@Override
			public void processRow(ResultSet arg0) throws SQLException {
				// TODO Auto-generated method stub
				
			}
		});*/
		
		List<Map<String, Object>> list = mybatisDao.getList("groupArrearsMapper.exportAllChinaPerson", parameterMap);
		setFileDownloadHeader(request, response, "4.2.3_集团客户欠费_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));

		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省份名称,地市名称,异常欠费账期 ,最早欠费账期,帐龄 ,异常欠费集团客户标识 , 异常欠费集团客户名称,集团客户状态 , 综合帐目科目编码,综合帐目科目名称 , 长期高额欠费金额 ,集团客户等级");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("cmccProvPrvdId"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("shortName"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("acctPrdYtm"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("minAcctPrdYtm"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("aging"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("bltoCustId"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("orgNm"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("custStatTypCd"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("comptAcctsSubjCd"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("comptAcctsSubjNm"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("dbtAmt"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("orgCustLvl")));

			out.println(sb.toString());
			sb.delete(0, sb.length());
		}

		out.flush();
		out.close();
	}

	/**
	 * 获取全国异常欠费金额比例
	 * @param audTrmEnd
	 * @return
	 */
	public Double getAllChinaScal(Map<String, Object> params) {
		return mybatisDao.get("groupArrearsMapper.getAllChinaScal", params);
	}

	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {

		// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
		response.setContentType("application/octet-stream;charset=GBK");
	}
}
