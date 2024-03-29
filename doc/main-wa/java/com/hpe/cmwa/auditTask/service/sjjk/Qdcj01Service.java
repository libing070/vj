package com.hpe.cmwa.auditTask.service.sjjk;

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

import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;

@Service
public class Qdcj01Service {
	
	private DecimalFormat df = new DecimalFormat("######0.00");

	@Autowired
	private MybatisDao	mybatisDao;
	
	/**
	 * 根据监控点名称以及关注点获取页面问号弹框信息
	 * @param parameterMap
	 * @return
	 */
	public List<Map<String, Object>> loadQdcjDialog(Map<String, Object> parameterMap) {
		List<Map<String, Object>> list = mybatisDao.getList("qdcj01Mapper.getQdcjDialog", parameterMap);
		return list;
	}
	
	
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
			List<Map<String, Object>> list = mybatisDao.getList("qdcj01Mapper.getResultByProvinceCode", parameterMap);
			for (Map<String, Object> map : list) {
				if(arrayList.contains(map.get("cmccProvPrvdId").toString())){
					arrayList.remove(map.get("cmccProvPrvdId").toString());
				}
			}
			resultList = arrayList;
		}else{
			Map<String, Object> map = mybatisDao.get("qdcj01Mapper.getIfQgHaveData", parameterMap);
			if("0".equals(map.get("dataNum").toString())){
				resultList.add("10000");
			}
		}
		return resultList;
	}
	
	
	/**
	 * 获取第一个柱状图 用户数数据
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getFirColumnPerData(Map<String, Object> parameterMap) {
		String provinceCode =(String) parameterMap.get("provinceCode");
		Map<String, Object> avgMap=null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		List<Object> areaCode = new ArrayList<Object>();
		Map<String,	Object> map  =  new HashMap<String,	Object> ();
		//判断省份参数是否包含“,” 如果包含未多省,不包含未单省或者全国
		if("10000".equals(provinceCode)){
			parameterMap.remove("provinceCode");
			parameterMap.put("provinceCode", provinceCode);
		}else{
			if(provinceCode.contains(",")){
				List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
			if(!provinceCode.contains(",") && !"10000".equals(provinceCode)){
				List<String> provinceCodes = Arrays.asList(provinceCode);
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
		}
		avgMap = mybatisDao.get("qdcj01Mapper.getFirColumnQgAvgPer", parameterMap);
		list = mybatisDao.getList("qdcj01Mapper.getFirColumnPerDataProv", parameterMap);
		//封装数据
		if(avgMap.get("amtPer") != null){
			xdata.add(avgMap.get("areaName"));
			ydata.add(avgMap.get("amtPer"));
			areaCode.add(avgMap.get("areaCode"));
		}
		if(!list.isEmpty()){
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
	 * 第一个折现图数据
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getFirLineNumData(Map<String, Object> parameterMap) {
		String provinceCode =(String) parameterMap.get("provinceCode");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		Map<String,	Object> map  =  new HashMap<String,	Object> ();
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			list= mybatisDao.getList("qdcj01Mapper.getFirLinePerDataQG", parameterMap);
		}
		if(!"10000".equals(provinceCode) && !provinceCode.contains(",")){
			list= mybatisDao.getList("qdcj01Mapper.getFirLinePerDataProv", parameterMap);
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
	 * 获取第2个柱状图
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getSecColumnData(Map<String, Object> parameterMap) {
		String provinceCode =(String) parameterMap.get("provinceCode");
		Map<String, Object> avgMap=null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		List<Object> areaCode = new ArrayList<Object>();
		Map<String,	Object> map  =  new HashMap<String,	Object> ();
		//判断省份参数是否包含“,” 如果包含未多省,不包含未单省或者全国
		if("10000".equals(provinceCode)){
			parameterMap.remove("provinceCode");
			parameterMap.put("provinceCode", provinceCode);
		}else{
			if(provinceCode.contains(",")){
				List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
			if(!provinceCode.contains(",") && !"10000".equals(provinceCode)){
				List<String> provinceCodes = Arrays.asList(provinceCode);
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
		}
		avgMap = mybatisDao.get("qdcj01Mapper.getSecColumnQgAvgAmt", parameterMap);
		list = mybatisDao.getList("qdcj01Mapper.getSecColumnAmtDataProv", parameterMap);
		//封装数据
		if(avgMap.get("shqdFeeAmt") != null){
			xdata.add(avgMap.get("areaName"));
			ydata.add(avgMap.get("shqdFeeAmt"));
			areaCode.add(avgMap.get("areaCode"));
		}
		if(!list.isEmpty()){
			for (Map<String, Object> obj : list) {
				xdata.add(obj.get("areaName"));
				ydata.add(obj.get("shqdFeeAmt"));
				areaCode.add(obj.get("areaCode"));
			}
		}
		map.put("xdata", xdata);
		map.put("ydata", ydata);
		map.put("areaCode", areaCode);
		return map;
	}
	
	
	/**
	 * 第一个折现图数据
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getSecLineData(Map<String, Object> parameterMap) {
		String provinceCode =(String) parameterMap.get("provinceCode");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		Map<String,	Object> map  =  new HashMap<String,	Object> ();
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			list= mybatisDao.getList("qdcj01Mapper.getSecLinePerDataQG", parameterMap);
		}
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			list= mybatisDao.getList("qdcj01Mapper.getSecLinePerDataProv", parameterMap);
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
					ydata.add(list.get(index).get("shqdFeeAmt"));
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
		if("10000".equals(provinceCode)){
			parameterMap.remove("provinceCode");
			parameterMap.put("provinceCode", provinceCode);
		}else{
			if(provinceCode.contains(",")){
				List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
			if(!provinceCode.contains(",") && !"10000".equals(provinceCode)){
				List<String> provinceCodes = Arrays.asList(provinceCode);
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
		}
		list = mybatisDao.getList("qdcj01Mapper.getTableDataProv", parameterMap);
		return list;
	}
	
	/**
	 * 汇总数据表导出
	 * @param pager
	 * @return
	 */
	public void exportHzTableData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		setFileDownloadHeader(request, response, "社会渠道酬金_社会渠道服务费占总收入比重_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		String provinceCode =(String) parameterMap.get("provinceCode");
		if("10000".equals(provinceCode)){
			parameterMap.remove("provinceCode");
			parameterMap.put("provinceCode", provinceCode);
		}else{
			if(provinceCode.contains(",")){
				List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
			if(!provinceCode.contains(",") && !"10000".equals(provinceCode)){
				List<String> provinceCodes = Arrays.asList(provinceCode);
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
		}
		list = mybatisDao.getList("qdcj01Mapper.exportTableDataProv", parameterMap);
		sb.append("审计月,省份代码,省份名称,社会渠道服务费（元）,个人业务收入（元）,集团业务收入（元）,家庭业务收入（元）,总收入（元）,占比（%）");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			
			sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,"); 			
			sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,"); 
			sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,"); 		
			sb.append(HelperString.objectConvertString(map.get("shqd_fee"))).append("\t,"); 			
			sb.append(HelperString.objectConvertString(df.format(map.get("gryw_amt")))).append("\t,");			
			sb.append(HelperString.objectConvertString(df.format(map.get("jtyw_amt")))).append("\t,"); 			
			sb.append(HelperString.objectConvertString(df.format(map.get("home_amt")))).append("\t,"); 			
			sb.append(HelperString.objectConvertString(df.format(map.get("tol_amt")))).append("\t,"); 			
			sb.append(HelperString.objectConvertString(df.format(map.get("amt_per")))).append("\t,");
			
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
	
	
	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {

		// 这里设置一下让浏览器弹出下载提示框,而不是直接在浏览器中打开
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
		response.setContentType("application/octet-stream;charset=GBK");
	}
	
}
