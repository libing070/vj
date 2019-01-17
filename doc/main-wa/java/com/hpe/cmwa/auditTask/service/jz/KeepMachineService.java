/**
 * com.hpe.cmwa.service.DemoService.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.service.jz;

import java.io.IOException;
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
public class KeepMachineService extends BaseObject {

	@Autowired
	private MybatisDao	mybatisDao;

	/**
	 * 根据省编码、开始时间、结束时间获取 曲线图、柱状图的数据列表
	 * @param cmccProvPrvdId
	 * @param audTrmStar
	 * @param audTrmEnd
	 * @return
	 */
	public List<Map<String, Object>> selectIntegralFeedbackRate(Map<String, Object> params) {
		List<Map<String, Object>> list;
		if (params.get("pageType").equals("1")) {
			list = mybatisDao.getList("keepMachineMapper.selectPersonPrvdInfo", params);
		} else {
			list = mybatisDao.getList("keepMachineMapper.chselectPersonPrvdInfo", params);
		}
		return list;
	}
	
	public Map<String, Object> selectIntegralFeedbackRateCon(Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (params.get("pageType").equals("1")) {
			List<Map<String, Object>> list = mybatisDao.getList("keepMachineMapper.selectPersonPrvdInfoCon", params);
			map.put("pageType", 1);
			map.put("list", list);
		} else {
			List<Map<String, Object>> list = mybatisDao.getList("keepMachineMapper.chselectPersonPrvdInfoCon", params);
			map.put("pageType", 2);
			map.put("list", list);
		}
		return map;
	}

	/**
	 * 根据省份编号、审计时间获取该省的分析数据
	 * @param cmccProvPrvdId
	 * @param audTrmEnd
	 * @return
	 */
	public List<Map<String, Object>> selectDataAnalysis(Map<String, Object> params) {
		List<Map<String, Object>> list;
		if (params.get("pageType").equals("1")) {
			list = mybatisDao.getList("keepMachineMapper.selectDataAnalysis", params);
		} else {
			list = mybatisDao.getList("keepMachineMapper.chselectDataAnalysis", params);
		}
		return list;
	}

	/**
	 * 根据省份编号、审计时间获取地市排行前三的地市名称
	 * @param cmccProvPrvdId
	 * @param audTrmEnd
	 * @return
	 */
	public List<String> selectThreeCity(Map<String, Object> params) {
		List<String> list;
		if (params.get("pageType").equals("1")) {
			list = mybatisDao.getList("keepMachineMapper.selectThreeCity", params);
		} else {
			list = mybatisDao.getList("keepMachineMapper.chselectThreeCity", params);
		}
		return list;
	}

	/**
	 * 根据 审计时间获取全国各省数据(全国排名)
	 * @param cmccProvPrvdId
	 * @param audTrmEnd
	 * @return
	 */
	public List<Map<String, Object>> selectAllPrvd(Map<String, Object> params) {
		List<Map<String, Object>> list;
		if (params.get("pageType").equals("1")) {
			list = mybatisDao.getList("keepMachineMapper.selectAllPrvd", params);
		} else {
			list = mybatisDao.getList("keepMachineMapper.chselectAllPrvd", params);
		}
		return list;
	}

	/**
	 * 根据省份编号和审计时间获取全省所有地市数据展示成地图
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> selectAllMapCity(Map<String, Object> params) {
		List<Map<String, Object>> list;
		if (params.get("pageType").equals("1")) {
			list = mybatisDao.getList("keepMachineMapper.selectMapData", params);
		} else {
			list = mybatisDao.getList("keepMachineMapper.chselectMapData", params);
		}
		return list;
	}

	/**
	 * 根据省份编号和审计时间获取全省所有地市数据
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> selectAllCityPerson(Pager pager) {
		List<Map<String, Object>> list;
		if (pager.getParams().get("pageType").equals("1")) {
			list = mybatisDao.getList("keepMachineMapper.selectAllCityPerson", pager);
		} else {
			list = mybatisDao.getList("keepMachineMapper.chselectAllCityPerson", pager);
		}
		return list;
	}

	/**
	 * 导出 根据省份编号和审计时间获取全省各地市 个人客户欠费 数据
	 * @param pager
	 * @return
	 * @throws IOException
	 */
	public void exportAllCityPerson(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws IOException {
		
		List<Map<String, Object>> list;
		if (parameterMap.get("pageType").equals("1")) {
			list = mybatisDao.getList("keepMachineMapper.selectMapData", parameterMap);
		} else {
			list = mybatisDao.getList("keepMachineMapper.chselectMapData", parameterMap);
		}
		
		if (parameterMap.get("pageType").equals("1")) {
			setFileDownloadHeader(request, response, "4.4.3_终端违规销售_养机套利_汇总.csv");
		} else {
			setFileDownloadHeader(request, response, "4.4.3_终端违规销售_窜货套利_汇总.csv");
		}
		
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));

		StringBuffer sb = new StringBuffer();
		sb.append("审计区间,地市名称,违规数量 ,违规金额(元),违规数量占比排名");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		int i = 0;
		for (Map<String, Object> map : list) {
			i++;
			sb.append(HelperString.objectConvertString(/*map.get("audTrm")*/parameterMap.get("currSumBeginDate")+"-"+parameterMap.get("currSumEndDate"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("errNum"))).append(",");
			sb.append(HelperString.objectConvertString(map.get("errAmt"))).append(",");
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
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if (pager.getParams().get("pageType").equals("1")) {
			list = mybatisDao.getList("keepMachineMapper.selectAllChinaPerson", pager);
		} else {
			list = mybatisDao.getList("keepMachineMapper.chselectAllChinaPerson", pager);
		}
		return list;
	}

	/**
	 * 导出全国所有个人客户清单查询
	 * @param pager
	 * @return
	 * @throws IOException
	 */
	@DataSourceName("dataSourceGBase")
	public void exportAllChinaPerson(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws IOException {
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		if (parameterMap.get("pageType").equals("1")) {
			setFileDownloadHeader(request, response, "4.4.3_终端违规销售_养机套利_明细.csv");
		} else {
			setFileDownloadHeader(request, response, "4.4.3_终端违规销售_窜货套利_明细.csv");
		}
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		//渠道标识,渠道名称,渠道类型编号,渠道类型名称,销售时间,结酬金额    ,异常销售模式编号,异常销售模式名称,异常销售类型,相关省销售时间,异常销售相关省,异常结酬相关省,是否套利,套利模式名称
		StringBuffer sb = new StringBuffer();
		sb.append("审计月 ,地市名称,IMEI ,机型,渠道标识,渠道名称,渠道类型编号,渠道类型名称 ,销售时间 ,结酬金额（元）,异常销售模式编号,异常销售模式名称 , 异常销售类型,相关省销售时间,异常销售相关省 , 异常结酬相关省 ,是否套利,套利模式名称");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			if (parameterMap.get("pageType").equals("1")) {
				list = mybatisDao.getList("keepMachineMapper.exportAllChinaPerson", parameterMap);
			} else {
				list = mybatisDao.getList("keepMachineMapper.chexportAllChinaPerson", parameterMap);
			}
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("audTrm"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("cmccProvNmShort"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("imei"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("trmnlStyle"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("chnlId"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("chnlName"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("chnlType"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("chnlTypeNm"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("salsDt"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("infractionSettAmt"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("focusCd"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("focusNm"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("infractionTyp"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("salsTmOut"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("taoliShortName"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("weiguiShortName"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("infractionFlag"))).append(",");
				sb.append(HelperString.objectConvertString(map.get("infractionName")));
	
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
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
		Double d;
		if (params.get("pageType").equals("1")) {
			d = mybatisDao.get("keepMachineMapper.getAllChinaScal", params);
		} else {
			d = mybatisDao.get("keepMachineMapper.chgetAllChinaScal", params);
		}
		return d;
	}

	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {

		// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
		response.setContentType("application/octet-stream;charset=GBK");
	}
}
