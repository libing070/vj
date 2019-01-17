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

import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;


@Service
public class KHQF04Service {
	private DecimalFormat df = new DecimalFormat("######0.00");
	@Autowired
    private MybatisDao mybatisDao;
	
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
			List<Map<String, Object>> list = mybatisDao.getList("khqf04Mapper.getResultByProvinceCode", parameterMap);
			for (Map<String, Object> map : list) {
				if(arrayList.contains(map.get("cmccProvPrvdId").toString())){
					arrayList.remove(map.get("cmccProvPrvdId").toString());
				}
			}
			resultList = arrayList;
		}else{
			Map<String, Object> map = mybatisDao.get("khqf04Mapper.getIfQgHaveData", parameterMap);
			if("0".equals(map.get("dataNum").toString())){
				resultList.add("10000");
			}
		}
		return resultList;
	}

	/**
	 * 左侧柱形图
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getFirColumnData(Map<String, Object> parameterMap) {
		String provinceCode = parameterMap.get("provinceCode").toString();
		List<String> provinceCodeslist = new ArrayList<String>();
		Map<String, Object> avgMap =null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		//数据整合		
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		List<Object> areaCode = new ArrayList<Object>();
		//判断是否是多省的数据
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			avgMap = mybatisDao.get("khqf04Mapper.getFirColumnDataQg", parameterMap);
			if(provinceCode.contains(",")){
				provinceCodeslist = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodeslist);
			}
			list = mybatisDao.getList("khqf04Mapper.getFirColumnDataProvince", parameterMap);
		}
		//判断是否是单个省的数据
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			avgMap = mybatisDao.get("khqf04Mapper.getFirColumnAvgCity", parameterMap);
			list = mybatisDao.getList("khqf04Mapper.getFirColumnDataCity", parameterMap);
		}
		
		if(avgMap.get("qfSubsPer") != null){
			xdata.add(avgMap.get("areaName"));
			ydata.add(avgMap.get("qfSubsPer"));
			areaCode.add(avgMap.get("areaCode"));
		}
		if(list.size()>0){
			for (Map<String, Object> obj : list) {
				xdata.add(obj.get("areaName"));
				ydata.add(obj.get("qfSubsPer"));
				areaCode.add(obj.get("areaCode"));
			}
		}
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("xdata", xdata);
		returnMap.put("ydata", ydata);
		returnMap.put("areaCode", areaCode);
		return returnMap;
	}
	
	/**
	 * 左侧折线图
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getFirLineData(Map<String, Object> parameterMap) {
		String provinceCode = parameterMap.get("provinceCode").toString();
		String cityId = parameterMap.get("cityId").toString();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		Map<String,	Object> map  =  new HashMap<String,	Object> ();
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			list= mybatisDao.getList("khqf04Mapper.getFirLineDataQG", parameterMap);
		}
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			if(cityId != null && !"".equals(cityId)){
				list= mybatisDao.getList("khqf04Mapper.getFirLineDataCity", parameterMap);
			}else{
				list= mybatisDao.getList("khqf04Mapper.getFirLineDataProvince", parameterMap);
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
					ydata.add(list.get(index).get("qfSubsPer"));
					index += 1;
				}
			}
		}
		map.put("xdata", xdata);
		map.put("ydata", ydata);
		return map;
	}
	
	
	/**
	 * 右侧柱形图
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getSecColumnData(Map<String, Object> parameterMap) {
		String provinceCode = parameterMap.get("provinceCode").toString();
		List<String> provinceCodeslist = new ArrayList<String>();
		Map<String, Object> avgMap =null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		//数据整合		
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		List<Object> areaCode = new ArrayList<Object>();
		//判断是否是多省的数据
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			avgMap = mybatisDao.get("khqf04Mapper.getSecColumnDataQg", parameterMap);
			if(provinceCode.contains(",")){
				provinceCodeslist = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodeslist);
			}
			list = mybatisDao.getList("khqf04Mapper.getSecColumnDataProvince", parameterMap);
		}
		//判断是否是单个省的数据
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			avgMap = mybatisDao.get("khqf04Mapper.getSecColumnAvgCity", parameterMap);
			list = mybatisDao.getList("khqf04Mapper.getSecColumnDataCity", parameterMap);
		}
		
		if(avgMap.get("lowChnlNumPer") != null){
			xdata.add(avgMap.get("areaName"));
			ydata.add(avgMap.get("lowChnlNumPer"));
			areaCode.add(avgMap.get("areaCode"));
		}
		
		if(list.size()>0){
			for (Map<String, Object> obj : list) {
				xdata.add(obj.get("areaName"));
				ydata.add(obj.get("lowChnlNumPer"));
				areaCode.add(obj.get("areaCode"));
			}
		}
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("xdata", xdata);
		returnMap.put("ydata", ydata);
		returnMap.put("areaCode", areaCode);
		return returnMap;
	}
	
	/**
	 * 右侧折线图
	 * @param parameterMap
	 * @return
	 */
	public Map<String, Object> getSecLineData(Map<String, Object> parameterMap) {
		String provinceCode = parameterMap.get("provinceCode").toString();
		String cityId = parameterMap.get("cityId").toString();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Object> xdata = new ArrayList<Object>();
		List<Object> ydata = new ArrayList<Object>();
		Map<String,	Object> map  =  new HashMap<String,	Object> ();
		if("10000".equals(provinceCode)||provinceCode.contains(",")){
			list= mybatisDao.getList("khqf04Mapper.getSeclineDataQg", parameterMap);
		}
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			if(cityId != null && !"".equals(cityId)){
				list= mybatisDao.getList("khqf04Mapper.getSecLineDataCity", parameterMap);
			}else{
				list= mybatisDao.getList("khqf04Mapper.getSecLineDataProvince", parameterMap);
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
					ydata.add(list.get(index).get("lowChnlNumPer"));
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
			list = mybatisDao.getList("khqf04Mapper.getTableDataProv", parameterMap);
		}
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			list = mybatisDao.getList("khqf04Mapper.getTableDataCty", parameterMap);
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
		
		setFileDownloadHeader(request, response, "客户欠费_渠道放号质量低_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		
		String provinceCode =(String) parameterMap.get("provinceCode");
	if("10000".equals(provinceCode)||provinceCode.contains(",")){
			
			if(provinceCode.contains(",")){
				List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
				parameterMap.remove("provinceCode");
				parameterMap.put("provinceCode", provinceCodes);
			}
			list = mybatisDao.getList("khqf04Mapper.getTableDataProv", parameterMap);
			sb.append("审计月,省份代码,省份名称,入网用户数,其中欠费用户数,欠费用户占比（%）,欠费金额（元）,入网涉及渠道数量,放号质量低的渠道数量,放号质量低渠道占比（%）");
			out.println(sb.toString());
			sb.delete(0, sb.length());
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");	 		
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("ent_subs_num"))).append("\t,");  	
				sb.append(HelperString.objectConvertString(map.get("qf_subs_num"))).append("\t,");  		
				sb.append(HelperString.objectConvertString(df.format(map.get("qf_subs_per")))).append("\t,");  		
				sb.append(HelperString.objectConvertString(map.get("qf_dbt_amt"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("ent_chnl_num"))).append("\t,");  	
				sb.append(HelperString.objectConvertString(map.get("low_chnl_num"))).append("\t,");  	
				sb.append(HelperString.objectConvertString(df.format(map.get("low_chnl_num_per")))).append("\t,");
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
		}
		if(!"10000".equals(provinceCode)&& !provinceCode.contains(",")){
			list = mybatisDao.getList("khqf04Mapper.getTableDataCty", parameterMap);
			sb.append("审计月,省份代码,省份名称,地市代码,地市名称," +
					"新入网用户数,其中欠费用户数,欠费用户占比（%）,欠费金额（元）,入网涉及渠道数量,放号质量低的渠道数量,放号质量低渠道占比（%）");
			out.println(sb.toString());
			sb.delete(0, sb.length());
			for (Map<String, Object> map : list) {

				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,"); 			
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,"); 
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,"); 	
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("ent_subs_num"))).append("\t,");  	
				sb.append(HelperString.objectConvertString(map.get("qf_subs_num"))).append("\t,");  		
				sb.append(HelperString.objectConvertString(df.format(map.get("qf_subs_per")))).append("\t,");  		
				sb.append(HelperString.objectConvertString(map.get("qf_dbt_amt"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("ent_chnl_num"))).append("\t,");  	
				sb.append(HelperString.objectConvertString(map.get("low_chnl_num"))).append("\t,");  	
				sb.append(HelperString.objectConvertString(df.format(map.get("low_chnl_num_per")))).append("\t,"); 
				
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
	 * @return 明细取自汇总表
	 * @throws IOException 
	 */
	public void exportMxTableData(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> parameterMap) throws IOException {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		setFileDownloadHeader(request, response, "客户欠费_渠道放号质量低_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		
		String provinceCode =(String) parameterMap.get("provinceCode");
		if(!"10000".equals(provinceCode)){
			List<String> provinceCodes = Arrays.asList(provinceCode.split(","));
			parameterMap.remove("provinceCode");
			parameterMap.put("provinceCode", provinceCodes);
		}
		
		list = mybatisDao.getList("khqf04Mapper.exportMxTable", parameterMap);
		sb.append("审计月,省份代码,省份名称,地市代码,地市名称,入网渠道标识,入网渠道名称,新入网用户数,其中欠费用户数,欠费用户占比（%）,欠费金额（元）");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			  sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
		      sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
		      sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
		      
		      sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
		      sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
		      sb.append(HelperString.objectConvertString(map.get("ent_chnl_id"))).append("\t,");
		      
		      sb.append(HelperString.objectConvertString(map.get("ent_chnl_nm"))).append("\t,");
		      sb.append(HelperString.objectConvertString(map.get("ent_subs_num"))).append("\t,");
		      sb.append(HelperString.objectConvertString(map.get("qf_subs_num"))).append("\t,");
		      
		      sb.append(HelperString.objectConvertString(df.format(map.get("qf_subs_per")))).append("\t,");
		      sb.append(HelperString.objectConvertString(map.get("qf_dbt_amt"))).append("\t,");
		     
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
