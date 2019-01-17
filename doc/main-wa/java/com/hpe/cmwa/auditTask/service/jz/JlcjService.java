package com.hpe.cmwa.auditTask.service.jz;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.common.datasource.DataSourceName;
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;

/**
 * <pre>
 * Desc： 
 * @author   gongtingting
 * @refactor gongtingting
 * @date     2017-01-22  下午2:08:33
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-01-22 	   gongtingting 	         1. Created this class. 
 * </pre>[]
 */
@Service
public class JlcjService {
	
	private DecimalFormat df = new DecimalFormat("######0.00");

	@Autowired
	private MybatisDao	mybatisDao;
	

	public List<Map<String, Object>> getgstongji(Map<String, Object> parameterMap) {
		return mybatisDao.getList("jlcj.getgstongji", parameterMap);
	}
	//加载地市在审计期间的比列统计图
	public List<Map<String, Object>> getdstongji(Map<String, Object> parameterMap) {
		
		return mybatisDao.getList("jlcj.getdstongji", parameterMap);
	}
	//加载同年度在审计期间的比列统计图
		public List<Map<String, Object>> gettndtongji(Map<String, Object> parameterMap) {
			
			return mybatisDao.getList("jlcj.gettndtongji", parameterMap);
		}
	//加载各省在审计期间数据表
	public List<Map<String, Object>> getgssjb(Pager pager) {

		 List<Map<String, Object>> list = mybatisDao.getList("jlcj.getgssjbtongji", pager);
		 
		 if(list.size()!=0){
			 Map<String, Object> map=new HashMap<String, Object>();
			 for(int i=0;i<list.size();i++){
				 map=list.get(i);
				 Calendar cal = Calendar.getInstance();
			     int year = cal.get(Calendar.YEAR);//获取年份
			     
			     if(Integer.parseInt((String) list.get(i).get("AUD_YEAR")) != year)
			     {
				    	 map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+"12");
				    	
			     }else{
			    	   int month=cal.get(Calendar.MONTH)+1;//获取月份
			    	   
			    	   if(month<10){
							month=Integer.parseInt(Integer.toString(month));
							map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+"0"+month);
						}
						else{
							month=Integer.parseInt(Integer.toString(month));
							map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+""+month);
						}
			    	 	
			     }
			 }
		 }
		 return list;
	}
	
	//导出各省数据表信息
	public List<Map<String, Object>> getDaoChuGsData(Map<String, Object> formatParameter) {
		List<Map<String, Object>> list = mybatisDao.getList("jlcj.selectSumGsAll", formatParameter);
		 if(list.size()!=0){
			 Map<String, Object> map=new HashMap<String, Object>();
			 for(int i=0;i<list.size();i++){
				 map=list.get(i);
				 Calendar cal = Calendar.getInstance();
			     int year = cal.get(Calendar.YEAR);//获取年份
			     
			     if(Integer.parseInt((String) list.get(i).get("AUD_YEAR")) != year)
			     {
				    	 map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+"12");
				    	
			     }else{
			    	   int month=cal.get(Calendar.MONTH)+1;//获取月份
			    	   
			    	   if(month<10){
							month=Integer.parseInt(Integer.toString(month));
							map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+"0"+month);
						}
						else{
							month=Integer.parseInt(Integer.toString(month));
							map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+""+month);
						}
			    	 	
			     }
			 }
		 }
		return list;
	}
	
   //加载地市在审计期间的数据表
	public List<Map<String, Object>> getdssjb(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("jlcj.getdssjbtongji", pager);
		 if(list.size()!=0){
			 Map<String, Object> map=new HashMap<String, Object>();
			 for(int i=0;i<list.size();i++){
				 map=list.get(i);
				 Calendar cal = Calendar.getInstance();
			     int year = cal.get(Calendar.YEAR);//获取年份
			     
			     if(Integer.parseInt((String) list.get(i).get("AUD_YEAR")) != year)
			     {
				    	 map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+"12");
				    	
			     }else{
			    	   int month=cal.get(Calendar.MONTH)+1;//获取月份
			    	   
			    	   if(month<10){
							month=Integer.parseInt(Integer.toString(month));
							map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+"0"+month);
						}
						else{
							month=Integer.parseInt(Integer.toString(month));
							map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+""+month);
						}
			    	 	
			     }
			 }
		 }
		 return list;
	}
	//导出地市数据表信息
	public List<Map<String, Object>> getDaoChuDsData(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("jlcj.selectSumDsAll", params);
		 if(list.size()!=0){
			 Map<String, Object> map=new HashMap<String, Object>();
			 for(int i=0;i<list.size();i++){
				 map=list.get(i);
				 Calendar cal = Calendar.getInstance();
			     int year = cal.get(Calendar.YEAR);//获取年份
			     
			     if(Integer.parseInt((String) list.get(i).get("AUD_YEAR")) != year)
			     {
				    	 map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+"12");
				    	
			     }else{
			    	   int month=cal.get(Calendar.MONTH)+1;//获取月份
			    	   
			    	   if(month<10){
							month=Integer.parseInt(Integer.toString(month));
							map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+"0"+month);
						}
						else{
							month=Integer.parseInt(Integer.toString(month));
							map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+""+month);
						}
			    	 	
			     }
			 }
		 }
		return list;
	}
	//加载同年度在审计期间的数据表
	public List<Map<String, Object>> gettndsjb(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("jlcj.gettndsjbtongji", pager);
		 if(list.size()!=0){
			 Map<String, Object> map=new HashMap<String, Object>();
			 for(int i=0;i<list.size();i++){
				 map=list.get(i);
				 Calendar cal = Calendar.getInstance();
			     int year = cal.get(Calendar.YEAR);//获取年份
			     
			     if(Integer.parseInt((String) list.get(i).get("AUD_YEAR")) != year)
			     {
				    	 map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+"12");
				    	
			     }else{
			    	   int month=cal.get(Calendar.MONTH)+1;//获取月份
			    	   
			    	   if(month<10){
							month=Integer.parseInt(Integer.toString(month));
							map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+"0"+month);
						}
						else{
							month=Integer.parseInt(Integer.toString(month));
							map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+""+month);
						}
			    	 	
			     }
			 }
		 }
		 return list;
	}
	//导出同年度数据表信息
	
	public List<Map<String, Object>> getDaoChuTndData(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("jlcj.selectSumTndAll", params);
		 if(list.size()!=0){
			 Map<String, Object> map=new HashMap<String, Object>();
			 for(int i=0;i<list.size();i++){
				 map=list.get(i);
				 Calendar cal = Calendar.getInstance();
			     int year = cal.get(Calendar.YEAR);//获取年份
			     
			     if(Integer.parseInt((String) list.get(i).get("AUD_YEAR")) != year)
			     {
				    	 map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+"12");
				    	
			     }else{
			    	   int month=cal.get(Calendar.MONTH)+1;//获取月份
			    	   
			    	   if(month<10){
							month=Integer.parseInt(Integer.toString(month));
							map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+"0"+month);
						}
						else{
							month=Integer.parseInt(Integer.toString(month));
							map.put("AUD_YEAR", list.get(i).get("AUD_YEAR")+"01-"+list.get(i).get("AUD_YEAR")+""+month);
						}
			    	 	
			     }
			 }
		 }
		return list;
	}
	//加载同年度的审计结论
	public List<Map<String, Object>> getQianTndJieLun(Map<String, Object> parameterMap) {
		
		List<Map<String, Object>> list = mybatisDao.getList("jlcj.selectQianTndJieLun", parameterMap);
		return list;
	}
	public List<Map<String, Object>> getHouTndJieLun(Map<String, Object> parameterMap) {
		List<Map<String, Object>> list = mybatisDao.getList("jlcj.selectHouTndJieLun", parameterMap);
		return list;
	}
	
	
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getDetailData(Pager pager) {

		 List<Map<String, Object>> list = mybatisDao.getList("jlcj.selectMingXi", pager);
		 return list;
	}
	@DataSourceName("dataSourceGBase")
	public void getDaoChuDetailData(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	setFileDownloadHeader(request, response, "4.5.2_激励酬金发放合规性_明细.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,实体渠道标识,实体渠道名称,激励酬金(元),放号酬金(元),基础业务服务代理酬金(元),增值业务代理酬金(元)" +
				"终端酬金(元),房租补贴(元),发放酬金总额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("jlcj.selectDaoChuDetailAll", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				
				sb.append(HelperString.objectConvertString(map.get("auditMonth"))).append("\t,");		 								
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");  								
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("COR_CHNL_ID"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("CHNL_NM"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("INCEN_RWD_SUM"))).append("\t,");  								
				sb.append(HelperString.objectConvertString(map.get("OUT_NBR_RWD_SUM"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("BASIC_BUSN_SVC_AGC_RWD_SUM"))).append("\t,"); 
				sb.append(HelperString.objectConvertString(map.get("VALUE_ADDED_BUSN_AGC_RWD_SUM"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("TRMNL_RWD_SUM"))).append("\t,");  								
				sb.append(HelperString.objectConvertString(map.get("HOUSE_FEE_SUM"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("TOL_FEE"))).append("\t,"); 
				
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
		}
		out.flush();
		out.close();
	}
	
	//导出头设置
  	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
  		// 这里设置一下让浏览器弹出下载提示框,而不是直接在浏览器中打开
  		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
  		response.setContentType("application/octet-stream;charset=GBK");
  	}

	
}
