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
public class KHQF05Service {
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
			List<Map<String, Object>> list = mybatisDao.getList("khqf05Mapper.getResultByProvinceCode", parameterMap);
			for (Map<String, Object> map : list) {
				if(arrayList.contains(map.get("cmccProvPrvdId").toString())){
					arrayList.remove(map.get("cmccProvPrvdId").toString());
				}
			}
			resultList = arrayList;
		}else{
			Map<String, Object> map = mybatisDao.get("khqf05Mapper.getIfQgHaveData", parameterMap);
			if("0".equals(map.get("dataNum").toString())){
				resultList.add("10000");
			}
		}
		return resultList;
	}
	
	/**
	 * 获取第一个柱状图  测试号码数量
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getFirColumnNumData(Map<String, Object> parameterMap) {
		String provinceCode = parameterMap.get("provinceCode").toString();
		Map<String, Object> avgMap=null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		List<Object> areaCode = new ArrayList<Object>();
		Map<String,	Object> map  =  new HashMap<String,	Object> ();
		//判断是否是多省的数据
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			avgMap = mybatisDao.get("khqf05Mapper.getFirColumnNum1", parameterMap);
			if(provinceCode.contains(",")){
				List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
			list = mybatisDao.getList("khqf05Mapper.getFirColumnNumDataProv", parameterMap);
		}
		//判断是否是单个省的数据
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			avgMap = mybatisDao.get("khqf05Mapper.getFirColumnNum2", parameterMap);
			list = mybatisDao.getList("khqf05Mapper.getFirColumnNumDataCty", parameterMap);
		}
		//封装数据
		if(avgMap.get("entSubsNum") != null){
			xdata.add(avgMap.get("areaName"));
			ydata.add(avgMap.get("entSubsNum"));
			areaCode.add(avgMap.get("areaCode"));
		}
		if(list.size()!=0){
			for (Map<String, Object> obj : list) {
				xdata.add(obj.get("areaName"));
				ydata.add(obj.get("entSubsNum"));
				areaCode.add(obj.get("areaCode"));
			}
		}
		map.put("xdata", xdata);
		map.put("ydata", ydata);
		map.put("areaCode", areaCode);
		return map;
	}
	
	/**
	 * 第一个折现图数据   测试号码数量
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
			list= mybatisDao.getList("khqf05Mapper.getFirLineNumDataQG", parameterMap);
		}
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			if(cityId != null && !"".equals(cityId)){
				list= mybatisDao.getList("khqf05Mapper.getFirLineNumDataCty", parameterMap);
			}else{
				list= mybatisDao.getList("khqf05Mapper.getFirLineNumDataProv", parameterMap);
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
					ydata.add(list.get(index).get("entSubsNum"));
					index += 1;
				}
			}
		}
		map.put("xdata", xdata);
		map.put("ydata", ydata);
		return map;
	}
	/**
	 * 右侧柱图  type1
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
			avgMap = mybatisDao.get("khqf05Mapper.getSecColumnNumQg", parameterMap);
			if(provinceCode.contains(",")){
				List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
			list = mybatisDao.getList("khqf05Mapper.getSecColumnNumDataProv1", parameterMap);
		}
		//判断是否是单个省的数据
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			avgMap = mybatisDao.get("khqf05Mapper.getSecColumnNumProvAvg", parameterMap);
			list = mybatisDao.getList("khqf05Mapper.getSecColumnNumDataCty", parameterMap);
		}
		//封装数据
		if(avgMap.get("testDbtAmt") != null){
			xdata.add(avgMap.get("areaName"));
			ydata.add(avgMap.get("testDbtAmt"));
			areaCode.add(avgMap.get("areaCode"));
		}
		if(list.size()!=0){
			for (Map<String, Object> obj : list) {
				xdata.add(obj.get("areaName"));
				ydata.add(obj.get("testDbtAmt"));
				areaCode.add(obj.get("areaCode"));
			}
		}
		map.put("xdata", xdata);
		map.put("ydata", ydata);
		map.put("areaCode", areaCode);
		return map;
		
	}
	/**
	 * 右侧柱图  type2 占比
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
			avgMap = mybatisDao.get("khqf05Mapper.getSecColumnPerQg", parameterMap);
			if(provinceCode.contains(",")){
				List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
			list = mybatisDao.getList("khqf05Mapper.getSecColumnPerDataProv", parameterMap);
		}
		//判断是否是单个省的数据
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			avgMap = mybatisDao.get("khqf05Mapper.getSecColumnPerProvAvg", parameterMap);
			list = mybatisDao.getList("khqf05Mapper.getSecColumnPerDataCty", parameterMap);
		}
		//封装数据
		if(avgMap.get("amtPer") != null){
			xdata.add(avgMap.get("areaName"));
			ydata.add(avgMap.get("amtPer"));
			areaCode.add(avgMap.get("areaCode"));
		}
		if(list.size()!=0){
			for (Map<String, Object> obj : list) {
				xdata.add(obj.get("areaName"));
				ydata.add(obj.get("amtPer"));
				areaCode.add(obj.get("areaCode"));
			}
		}
		map.put("xdata", xdata);
		map.put("ydata", ydata);
		map.put("areaCode", areaCode);
		return map;
	}
	
	/**
	 * 右侧折线  type2  金额
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getSecLineNumData(Map<String, Object> parameterMap) {
		String provinceCode =(String) parameterMap.get("provinceCode");
		String cityId = (String) parameterMap.get("cityId");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		Map<String,	Object> map  =  new HashMap<String,	Object> ();
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			list= mybatisDao.getList("khqf05Mapper.getSecLineNumDataQG", parameterMap);
		}
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			if(cityId != null && !"".equals(cityId)){
				list= mybatisDao.getList("khqf05Mapper.getSecLineNumDataCty", parameterMap);
			}else{
				list= mybatisDao.getList("khqf05Mapper.getSecLineNumDataProv", parameterMap);
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
					ydata.add(list.get(index).get("testDbtAmt"));
					index += 1;
				}
			}
		}
		map.put("xdata", xdata);
		map.put("ydata", ydata);
		return map;
	}
	/**
	 * 右侧折线  type2 占比
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
			list= mybatisDao.getList("khqf05Mapper.getSecLinePerDataQG", parameterMap);
		}
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			if(cityId != null && !"".equals(cityId)){
				list= mybatisDao.getList("khqf05Mapper.getSecLinePerDataCty", parameterMap);
			}else{
				list= mybatisDao.getList("khqf05Mapper.getSecLinePerDataProv", parameterMap);
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
					ydata.add(list.get(index).get("amtPer"));
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
			list = mybatisDao.getList("khqf05Mapper.getTableDataProv", parameterMap);
		}
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			list = mybatisDao.getList("khqf05Mapper.getTableDataCty", parameterMap);
		}
		return list;
	}
	
	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {

		// 这里设置一下让浏览器弹出下载提示框,而不是直接在浏览器中打开
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
		response.setContentType("application/octet-stream;charset=GBK");
	}
	
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void exportHzTableData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		setFileDownloadHeader(request, response, "客户欠费__测试号费用列入欠费_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		
		String provinceCode =(String) parameterMap.get("provinceCode");
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			
			if(provinceCode.contains(",")){
				List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
			list = mybatisDao.getList("khqf05Mapper.getTableDataProv", parameterMap);
			sb.append("审计月,省份代码,省份名称,列入欠费的测试号码数量,测试号码欠费金额（元）,总欠费（元）,测试号码欠费占比（%）");
			out.println(sb.toString());
			sb.delete(0, sb.length());
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,"); 
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("ent_subs_num"))).append("\t,");  	
				sb.append(HelperString.objectConvertString(map.get("test_dbt_amt"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("tol_dbt_amt"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(df.format(map.get("per_test_amt")))).append("\t,"); 		
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
		}
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			list = mybatisDao.getList("khqf05Mapper.getTableDataCty", parameterMap);
			sb.append("审计月,省份代码,省份名称,地市代码,地市名称,列入欠费的测试号码数量,测试号码欠费金额（元）,总欠费（元）,测试号码欠费占比（%）");
			out.println(sb.toString());
			sb.delete(0, sb.length());
			for (Map<String, Object> map : list) {

				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,"); 			
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,"); 
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");  	
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("ent_subs_num"))).append("\t,");  	
				sb.append(HelperString.objectConvertString(map.get("test_dbt_amt"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("tol_dbt_amt"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(df.format(map.get("per_test_amt")))).append("\t,");	
				
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
	 * @return  明细取自汇总表
	 * @throws IOException 
	 */
	public void exportMxTableData(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> parameterMap) throws IOException {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		setFileDownloadHeader(request, response, "客户欠费_测试号费用列入欠费_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		
		String provinceCode =(String) parameterMap.get("provinceCode");
		if(!"10000".equals(provinceCode)){
			List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
			parameterMap.remove("provinceCode");
			parameterMap.put("provinceCode", provinceCodes);
		}
		
		list = mybatisDao.getList("khqf05Mapper.exportMxTable", parameterMap);
		sb.append("审计月,省份代码,省份名称,地市代码,地市名称,手机号,用户类型," +
				 "入网日期,账户标识,账户名称,入网渠道标识,入网渠道名称,欠费金额（元）");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			
			sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");	 		
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,"); 
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,"); 		
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");  	
			sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("msisdn"))).append("\t,"); 			
			sb.append(HelperString.objectConvertString(map.get("subs_busn_typ"))).append("\t,");  	
			sb.append(HelperString.objectConvertString(map.get("ent_dt"))).append("\t,"); 			
			sb.append(HelperString.objectConvertString(map.get("acct_id"))).append("\t,"); 			
			sb.append(HelperString.objectConvertString(map.get("acct_nm"))).append("\t,"); 			
			sb.append(HelperString.objectConvertString(map.get("ent_chnl_id"))).append("\t,"); 		
			sb.append(HelperString.objectConvertString(map.get("ent_chnl_nm"))).append("\t,"); 		
			sb.append(HelperString.objectConvertString(map.get("dbt_amt"))).append("\t,"); 	
			
			
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}
		out.flush();
		out.close();
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
