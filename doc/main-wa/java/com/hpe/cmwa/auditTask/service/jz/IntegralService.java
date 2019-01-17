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
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;

/**
 * <pre>
 * Desc： 积分回馈率Service类
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
public class IntegralService extends BaseObject {

	@Autowired
	private MybatisDao	mybatisDao;

	/**
	 * 根据省编码、开始时间、结束时间获取 曲线图、柱状图的数据列表
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @return
	 */
	public Map<String, Object> selectIntegralFeedbackRate(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("integralMapper.selectIntegralPrvdInfo", params);
		return map;
	}

	/**
	 * 根据省份编号、审计时间获取该省的分析数据
	 * @param cmccProvPrvdId
	 * @param audTrmEnd
	 * @return
	 */
	public List<Map<String, Object>> selectDataAnalysis(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("integralMapper.selectDataAnalysis", params);
		return list;
	}

	/**
	 * 根据省份编号、审计时间获取地市排行前三的地市名称
	 * @param cmccProvPrvdId
	 * @param audTrmEnd
	 * @return
	 */
	public List<String> selectThreeCity(Map<String, Object> params) {
		List<String> list = mybatisDao.getList("integralMapper.selectThreeCity", params);
		return list;
	}

	/**
	 * 根据省份编号和审计时间获取全国各省数据
	 * @param cmccProvPrvdId
	 * @param audTrmEnd
	 * @return
	 */
	public Map<String, Object> selectAllPrvd(Map<String, Object> params) {
		Map<String, Object> map = mybatisDao.get("integralMapper.selectAllPrvd", params);
		return map;
	}

	/**
	 * 根据省份编号和审计时间获取全省所有地市数据
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> selectAllPrvdCity(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("integralMapper.selectAllPrvdCity", pager);
		return list;
	}

	/**
	 * 根据省份编号和审计时间获取全省所有地市数据展示成地图
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> selectAllMapCity(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("integralMapper.PrvdCityMap", params);
		return list;
	}

	/**
	 * 全国所有地市数据查询
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> selectAllCity(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("integralMapper.selectAllCity", pager);
		return list;
	}

	/**
	 * 导出全省所有省市信息
	 * @param request
	 * @param response
	 * @param parameterMap
	 * @throws Exception
	 */
	public void exportPrvdCity(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {

		List<Map<String, Object>> list = mybatisDao.getList("integralMapper.exportAllPrvdCity", parameterMap);

		setFileDownloadHeader(request, response, "4.6.1_积分合规性_积分回馈合规性_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));

		StringBuffer sb = new StringBuffer();
		sb.append("审计月,用户新增积分 ,新增积分总价值(元) ,用户出账收入(元) ,积分回馈率(%)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(map.get("audTrm"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("curmonQtyTol"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("curmonScoreTol"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("merTotFeeTol"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("perJfhk")));
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}

		out.flush();
		out.close();
	}

	/**
	 * 导出全国所有地市数据查询
	 * @param pager
	 * @return
	 */
	public void exportAllCity(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

		setFileDownloadHeader(request, response, "4.6.1_积分合规性_积分回馈合规性_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));

		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省份名称,地市编码,地市名称,用户新增积分 ,新增积分总价值(元) ,用户出账收入(元) ,积分回馈率(%)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("integralMapper.exportAllCity", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("audTrm"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("cmccProvPrvdId"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("shortName"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("cmccProvId"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("curmonQtyTol"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("curmonScoreTol"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("merTotFeeTol"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("perJfhk")));
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
