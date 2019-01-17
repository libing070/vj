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

import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.common.datasource.DataSourceName;
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;

/**
 * <pre>
 * Desc： 
 * @author   gongtingting
 * @refactor gongtingting
 * @date     2017-01-04  下午2:08:33
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-01-04 	   gongtingting 	         1. Created this class. 
 * </pre>
 */
@Service
public class RealNameInternetService {

	@Autowired
	private MybatisDao	mybatisDao;
	/**
	 * 趋势图
	 * @param formatParameter
	 * @return
	 */
	public List<Map<String, Object>> getSumRealNamePrvd(Map<String, Object> formatParameter) {
		
		List<Map<String, Object>> list = mybatisDao.getList("realMapper.selectSumRealNamePrvd", formatParameter);

		return list;
	}
	
	public List<Map<String, Object>> getSumRealNamePrvdCon(Map<String, Object> formatParameter) {
		
		List<Map<String, Object>> list = mybatisDao.getList("realMapper.selectSumRealNamePrvdCon", formatParameter);

		return list;
	}
	
	
	//全国排名
	public List<Map<String, Object>> getSumSort(Map<String, Object> params) {
		
		List<Map<String, Object>> list = mybatisDao.getList("realMapper.selectSumSort", params);

		return list;
	}
	//得到前三地市
	public List<Map<String, Object>> getTop3City(Map<String, Object> formatParameter) {
		List<Map<String, Object>> list = mybatisDao.getList("realMapper.selectTop3City", formatParameter);
		
		return list;
	}
	//地图
	public List<Map<String, Object>> getSumCityAll(Map<String, Object> formatParameter) {
		
		List<Map<String, Object>> list = mybatisDao.getList("realMapper.selectSumCityAll", formatParameter);

		return list;
	}
	//加载数据表
	public List<Map<String, Object>> getSumCity(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("realMapper.selectSumCity", pager);

		return list;
	}
	
	
	//导出头设置
  	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
  		// 这里设置一下让浏览器弹出下载提示框,而不是直接在浏览器中打开
  		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
  		response.setContentType("application/octet-stream;charset=GBK");
  	}
  	
	//加载明细数据
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getRealNameDetailData(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("realMapper.selectRealNameDetail", pager);
	
		return list;
	}
	//导出明细
	@DataSourceName("dataSourceGBase")
	public void getRealNameDetailDataDaoChu(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	setFileDownloadHeader(request, response, "4.1.3_实名制登记率_存量物联网M2M用户实名制率_明细.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,用户标识,手机号,入网时间,用户状态,用户状态代码,入网渠道,付费类型,付费类型代码,用户类型,用户类型代码,客户标识");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("realMapper.selectRealNameDetailAll", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				
				sb.append(HelperString.objectConvertString(map.get("Aud_trm"))).append("\t,");		 								
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");  								
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("subs_id"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("msisdn"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("ent_dt"))).append("\t,");  								
				sb.append(HelperString.objectConvertString(map.get("subs_stat_typ_nm"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("subs_stat_typ_cd"))).append("\t,"); 
				sb.append(HelperString.objectConvertString(map.get("ent_chnl_id"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("subs_pay_typ_nm"))).append("\t,");  								
				sb.append(HelperString.objectConvertString(map.get("subs_pay_typ_cd"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("subs_typ_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("subs_typ_cd"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("cust_id"))).append("\t,");
				
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
		}
		out.flush();
		out.close();		
		
	}

	
}
