package com.hpe.cmwa.auditTask.service.sjjk;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.common.datasource.DataSourceName;
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;


@Service
public class KHQF06Service {
	private DecimalFormat df = new DecimalFormat("######0.00");

	@Autowired
	private MybatisDao	mybatisDao;
	
	/**
	 * 根据省份代码判断无数据的省份
	 * @param parameterMap
	 * @return
	 */
	public List<String> getResultByProvinceCode(Map<String, Object> parameterMap) {
		String provinceCode =(String) parameterMap.get("provinceCode");
		List<String> resultList = new ArrayList<String>();
		if(!"10000".equals(provinceCode)){
			List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
			List<String>  arrayList = new ArrayList<String> (provinceCodes);
			parameterMap.remove("provinceCode");
			parameterMap.put("provinceCode", arrayList);
			List<Map<String, Object>> list = mybatisDao.getList("khqf06Mapper.getResultByProvinceCode", parameterMap);
			for (Map<String, Object> map : list) {
				if(arrayList.contains(map.get("cmccProvPrvdId").toString())){
					arrayList.remove(map.get("cmccProvPrvdId").toString());
				}
			}
			resultList = arrayList;
		}else{
			Map<String, Object> map = mybatisDao.get("khqf06Mapper.getIfQgHaveData", parameterMap);
			if("0".equals(map.get("dataNum").toString())){
				resultList.add("10000");
			}
		}
		return resultList;
	}
	
	/**
	 * 获取第一个柱状图 长期欠费产品涉及集团客户数
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getFirColumnNumData(Map<String, Object> parameterMap) {
		String provinceCode =(String) parameterMap.get("provinceCode");
		Map<String, Object> avgMap=null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		List<Object> areaCode = new ArrayList<Object>();
		Map<String,	Object> map  =  new HashMap<String,	Object> ();
		//判断是否是多省的数据
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			avgMap = mybatisDao.get("khqf06Mapper.getFirColumnQgAvgNum", parameterMap);
			if(provinceCode.contains(",")){
				List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
			list = mybatisDao.getList("khqf06Mapper.getFirColumnNumDataProv", parameterMap);
		}
		//判断是否是单个省的数据
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			avgMap = mybatisDao.get("khqf06Mapper.getFirColumnProvAvgNum", parameterMap);
			list = mybatisDao.getList("khqf06Mapper.getFirColumnNumDataCty", parameterMap);
		}
		//封装数据
		if(avgMap.get("custNum") != null){
			xdata.add(avgMap.get("areaName"));
			ydata.add(avgMap.get("custNum"));
			areaCode.add(avgMap.get("areaCode"));
		}
		if(list.size()!=0){
			for (Map<String, Object> obj : list) {
				xdata.add(obj.get("areaName"));
				ydata.add(obj.get("custNum"));
				areaCode.add(obj.get("areaCode"));
			}
		}
		map.put("xdata", xdata);
		map.put("ydata", ydata);
		map.put("areaCode", areaCode);
		return map;
	}
	
	/**
	 * 第一个折现图数据 用户数
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getFirLineNumData(Map<String, Object> parameterMap) {
		String provinceCode =(String) parameterMap.get("provinceCode");
		String cityId = (String) parameterMap.get("cityId");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		Map<String,	Object> map  =  new HashMap<String,	Object> ();
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			list= mybatisDao.getList("khqf06Mapper.getFirLineNumDataQG", parameterMap);
		}
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			if(cityId != null && !"".equals(cityId)){
				list= mybatisDao.getList("khqf06Mapper.getFirLineNumDataCty", parameterMap);
			}else{
				list= mybatisDao.getList("khqf06Mapper.getFirLineNumDataProv", parameterMap);
			}
		}
		if(list.size()!=0){
			int index = 0;
			xdata = getTimeList(parameterMap);
			List <Object> monArray = new ArrayList<Object> ();
			for (Map<String, Object> obj : list) {
				monArray.add(obj.get("audTrm"));
			}
			for(int i=0;i<xdata.size();i++){
				if(!monArray.contains(xdata.get(i))){
					ydata.add(0);
				}else{
					ydata.add(list.get(index).get("custNum"));
					index += 1;
				}
			}
		}
		map.put("xdata", xdata);
		map.put("ydata", ydata);
		return map;
	}
	
	/**
	 * 获取第一个柱状图 用户数数据
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getSecColumnAmtData(Map<String, Object> parameterMap) {
		String provinceCode =(String) parameterMap.get("provinceCode");
		Map<String, Object> avgMap=null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		List<Object> areaCode = new ArrayList<Object>();
		Map<String,	Object> map  =  new HashMap<String,	Object> ();
		//判断是否是多省的数据
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			avgMap = mybatisDao.get("khqf06Mapper.getSecColumnQgAvgAmt", parameterMap);
			if(provinceCode.contains(",")){
				List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
			list = mybatisDao.getList("khqf06Mapper.getSecColumnAmtDataProv", parameterMap);
		}
		//判断是否是单个省的数据
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			avgMap = mybatisDao.get("khqf06Mapper.getSecColumnProvAvgAmt", parameterMap);
			list = mybatisDao.getList("khqf06Mapper.getSecColumnAmtDataCty", parameterMap);
		}
		//封装数据
		if(avgMap.get("busnDbtAmt") != null){
			xdata.add(avgMap.get("areaName"));
			ydata.add(avgMap.get("busnDbtAmt"));
			areaCode.add(avgMap.get("areaCode"));
		}
		if(list.size()!=0){
			for (Map<String, Object> obj : list) {
				xdata.add(obj.get("areaName"));
				ydata.add(obj.get("busnDbtAmt"));
				areaCode.add(obj.get("areaCode"));
			}
		}
		map.put("xdata", xdata);
		map.put("ydata", ydata);
		map.put("areaCode", areaCode);
		return map;
	}
	
	
	/**
	 * 获取第2个柱状图     占比 数据
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getSecColumnPerData(Map<String, Object> parameterMap) {
		String provinceCode =(String) parameterMap.get("provinceCode");
		Map<String, Object> avgMap=null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		List<Object> areaCode = new ArrayList<Object>();
		Map<String,	Object> map  =  new HashMap<String,	Object> ();
		//判断是否是多省的数据
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			avgMap = mybatisDao.get("khqf06Mapper.getSecColumnQgPer", parameterMap);
			if(provinceCode.contains(",")){
				List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
			list = mybatisDao.getList("khqf06Mapper.getSecColumnPerDataProv", parameterMap);
		}
		//判断是否是单个省的数据
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			avgMap = mybatisDao.get("khqf06Mapper.getSecColumnProvAvgPer", parameterMap);
			list = mybatisDao.getList("khqf06Mapper.getSecColumnPerDataCty", parameterMap);
		}
		//封装数据
		if(avgMap.get("busnAmtPer") != null){
			xdata.add(avgMap.get("areaName"));
			ydata.add(avgMap.get("busnAmtPer"));
			areaCode.add(avgMap.get("areaCode"));
		}
		if(list.size()!=0){
			for (Map<String, Object> obj : list) {
				xdata.add(obj.get("areaName"));
				ydata.add(obj.get("busnAmtPer"));
				areaCode.add(obj.get("areaCode"));
			}
		}
		map.put("xdata", xdata);
		map.put("ydata", ydata);
		map.put("areaCode", areaCode);
		return map;
	}
	
	
	/**
	 * 第2个折现图数据   amt
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getSecLineAmtData(Map<String, Object> parameterMap) {
		String provinceCode =(String) parameterMap.get("provinceCode");
		String cityId = (String) parameterMap.get("cityId");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		Map<String,	Object> map  =  new HashMap<String,	Object> ();
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			list= mybatisDao.getList("khqf06Mapper.getSecLineAmtDataQG", parameterMap);
		}
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			if(cityId != null && !"".equals(cityId)){
				list= mybatisDao.getList("khqf06Mapper.getSecLineAmtDataCty", parameterMap);
			}else{
				list= mybatisDao.getList("khqf06Mapper.getSecLineAmtDataProv", parameterMap);
			}
		}
		if(list.size()!=0){
			int index = 0;
			xdata = getTimeList(parameterMap);
			List <Object> monArray = new ArrayList<Object> ();
			for (Map<String, Object> obj : list) {
				monArray.add(obj.get("audTrm"));
			}
			for(int i=0;i<xdata.size();i++){
				if(!monArray.contains(xdata.get(i))){
					ydata.add(0);
				}else{
					ydata.add(list.get(index).get("busnDbtAmt"));
					index += 1;
				}
			}
		}
		map.put("xdata", xdata);
		map.put("ydata", ydata);
		return map;
	}
	
	
	/**
	 * 第2个折现图数据  占比
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getSecLinePerData(Map<String, Object> parameterMap) {
		String provinceCode =(String) parameterMap.get("provinceCode");
		String cityId = (String) parameterMap.get("cityId");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		Map<String,	Object> map  =  new HashMap<String,	Object> ();
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			list= mybatisDao.getList("khqf06Mapper.getSecLinePerDataQG", parameterMap);
		}
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			if(cityId != null && !"".equals(cityId)){
				list= mybatisDao.getList("khqf06Mapper.getSecLinePerDataCty", parameterMap);
			}else{
				list= mybatisDao.getList("khqf06Mapper.getSecLinePerDataProv", parameterMap);
			}
		}
		if(list.size()!=0){
			int index = 0;
			xdata = getTimeList(parameterMap);
			List <Object> monArray = new ArrayList<Object> ();
			for (Map<String, Object> obj : list) {
				monArray.add(obj.get("audTrm"));
			}
			for(int i=0;i<xdata.size();i++){
				if(!monArray.contains(xdata.get(i))){
					ydata.add(0);
				}else{
					ydata.add(list.get(index).get("busnAmtPer"));
					index += 1;
				}
			}
		}
		map.put("xdata", xdata);
		map.put("ydata", ydata);
		return map;
	}
	
	
	/**
	 * 获取统计分析数据表数据
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getTableData(Map<String, Object> parameterMap) {
		String provinceCode =(String) parameterMap.get("provinceCode");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			if(provinceCode.contains(",")){
				List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
			list = mybatisDao.getList("khqf06Mapper.getTableDataProv", parameterMap);
		}
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			list = mybatisDao.getList("khqf06Mapper.getTableDataCty", parameterMap);
		}
		return list;
	}
	
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void exportHzTableData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		setFileDownloadHeader(request, response, "客户欠费_未对已长期欠费的集团产品进行暂停或注销_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		
		String provinceCode =(String) parameterMap.get("provinceCode");
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			
			if(provinceCode.contains(",")){
				List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
			list = mybatisDao.getList("khqf06Mapper.getTableDataProv", parameterMap);
			sb.append("审计月,省份代码,省份名称,集团产品编码,集团产品,集团客户数,欠费金额（元）");
			out.println(sb.toString());
			sb.delete(0, sb.length());
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,"); 			
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,"); 
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("org_busn_typ_cd"))).append("\t,"); 	
				sb.append(HelperString.objectConvertString(map.get("org_busn_typ_nm"))).append("\t,"); 	
				sb.append(HelperString.objectConvertString(map.get("cust_num"))).append("\t,"); 			
				sb.append(HelperString.objectConvertString(map.get("dbt_amt"))).append("\t,"); 	
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
		}
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			list = mybatisDao.getList("khqf06Mapper.getTableDataCty", parameterMap);
			sb.append("审计月,省份代码,省份名称,地市代码,地市名称,集团产品编码,集团产品,集团客户数,欠费金额（元）");
			out.println(sb.toString());
			sb.delete(0, sb.length());
			for (Map<String, Object> map : list) {
				
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,"); 
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("org_busn_typ_cd"))).append("\t,"); 	
				sb.append(HelperString.objectConvertString(map.get("org_busn_typ_nm"))).append("\t,"); 	
				sb.append(HelperString.objectConvertString(map.get("cust_num"))).append("\t,"); 			
				sb.append(HelperString.objectConvertString(map.get("dbt_amt"))).append("\t,");		
				
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
		}
		out.flush();
		out.close();
	}
	
	/**
	 * 明细数据表导出
	 * @param pager
	 * @return
	 * @throws IOException 
	 */
	@DataSourceName("dataSourceGBase")
	public void exportMxTableData(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> parameterMap) throws IOException {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		setFileDownloadHeader(request, response, "客户欠费_未对已长期欠费的集团产品进行暂停或注销_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		
		String provinceCode =(String) parameterMap.get("provinceCode");
		if(!"10000".equals(provinceCode)){
			List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
			parameterMap.remove("provinceCode");
			parameterMap.put("provinceCode", provinceCodes);
		}
		
		sb.append("审计月,省份代码,省份名称,地市代码,地市名称,账期,账户标识,账户名称," +
				"客户标识,客户名称,产品最早欠费月,产品欠费月数,集团产品编码,集团产品,欠费金额（元）");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("khqf06Mapper.exportMxTable", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				
				
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,"); 			
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,"); 
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("acct_prd_ytm"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("acct_id"))).append("\t,"); 			
				sb.append(HelperString.objectConvertString(map.get("acct_nm"))).append("\t,"); 			
				sb.append(HelperString.objectConvertString(map.get("cust_id"))).append("\t,"); 			
				sb.append(HelperString.objectConvertString(map.get("cust_nm"))).append("\t,"); 			
				sb.append(HelperString.objectConvertString(map.get("ear_trm"))).append("\t,"); 			
				sb.append(HelperString.objectConvertString(map.get("acct_age"))).append("\t,"); 			
				sb.append(HelperString.objectConvertString(map.get("org_busn_typ_cd"))).append("\t,"); 	
				sb.append(HelperString.objectConvertString(map.get("org_busn_typ_nm"))).append("\t,"); 	
				sb.append(HelperString.objectConvertString(map.get("dbt_amt"))).append("\t,"); 		
				
				
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
	
	
	
	
	/**
	 * 根据请求获取时间区间的所有时间集合
	 * 
	 * @param parameterMap
	 * @return
	 */
	public List<Object> getTimeList(Map<String, Object> parameterMap){
		List<Object> xdata = new ArrayList<Object>();
		String BeginDate = (String) parameterMap.get("currBeginDate");
		String EndDate = (String) parameterMap.get("currEndDate");
		int beginYear = Integer.parseInt(BeginDate.substring(0, 4));
		int endYear = Integer.parseInt(EndDate.substring(0, 4));
		int beginMouth = Integer.parseInt(BeginDate.substring(4, 6));
		int endMouth = Integer.parseInt(EndDate.substring(4, 6));
		int dateLength = (endYear-beginYear)*12+endMouth - beginMouth;
		xdata.add(BeginDate);
		for (int i = 0; i < dateLength; i++) {
			beginMouth +=1;
			if(beginMouth>12){
				beginYear = beginYear + 1;
				beginMouth = beginMouth -12;
				if(beginMouth >=10){
					xdata.add(beginYear+""+beginMouth);
				}else{
					xdata.add(beginYear+"0"+beginMouth);
				}
			}else{
				if(beginMouth >=10){
					xdata.add(beginYear+""+beginMouth);
				}else{
					xdata.add(beginYear+"0"+beginMouth);
				}
			}
		}
		return xdata;
	}
}
