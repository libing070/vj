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
 * @author   wangpeng
 * @refactor wangpeng
 * @date     2016-11-18 下午2:08:33
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2016-11-18 	   wangpeng 	         1. Created this class. 
 * </pre>
 */
@Service
public class RealNameRegisterService {

	@Autowired
	private MybatisDao	mybatisDao;

	
	/**
	 * 获取地市汇总信息
	 * 
	 * <pre>
	 * Desc  
	 * @param params
	 * @return
	 * @author wangpeng
	 * 2016-11-22 下午2:55:07
	 * </pre>
	 */
	public List<Map<String, Object>> getSumCity(Pager pager) {

		List<Map<String, Object>> list = mybatisDao.getList("realNameMapper.selectSumCity", pager);

		return list;
	}

	/**
	 * 获取所有地市汇总信息
	 * 
	 * <pre>
	 * Desc  
	 * @param params
	 * @return
	 * @author wangpeng
	 * 2016-11-22 下午7:07:35
	 * </pre>
	 */
	public List<Map<String, Object>> getSumCityAll(Map<String, Object> params) {

		List<Map<String, Object>> list = mybatisDao.getList("realNameMapper.selectSumCityAll", params);

		return list;
	}

	/**
	 * 获取全国汇总信息
	 * 
	 * <pre>
	 * Desc  
	 * @param params
	 * @return
	 * @author wangpeng
	 * 2016-11-21 下午4:59:39
	 * </pre>
	 */
	public List<Map<String, Object>> getSumSort(Map<String, Object> params) {

		List<Map<String, Object>> list = mybatisDao.getList("realNameMapper.selectSumSort", params);

		return list;
	}

	/**
	 * 查询实名制登记率省份汇总信息
	 * 
	 * <pre>
	 * Desc  
	 * @return
	 * @author wangpeng
	 * 2016-11-18 下午3:03:02
	 * </pre>
	 */
	public List<Map<String, Object>> getSumRealNamePrvd(Map<String, Object> params) {

		List<Map<String, Object>> list = mybatisDao.getList("realNameMapper.selectSumRealNamePrvd", params);

		return list;
	}
	
	public List<Map<String, Object>> getSumRealNamePrvdCon(Map<String, Object> params) {

		List<Map<String, Object>> list = mybatisDao.getList("realNameMapper.selectSumRealNamePrvdCon", params);

		return list;
	}

	/**
	 * <pre>
	 * Desc  获取省份的top3地市
	 * @param parameterMap
	 * @return
	 * @author peter.fu
	 * Nov 25, 2016 5:03:46 PM
	 * </pre>
	 */
	public List<Map<String, Object>> getTop3City(Map<String, Object> parameterMap) {
		
		List<Map<String, Object>> list = mybatisDao.getList("realNameMapper.selectTop3City", parameterMap);
		
		return list;
	}
	
	/**
	 * 查询实名制登记率明细
	 * @author wangpeng
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getRealNameDetailData(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("realNameMapper.selectRealNameDetail", pager);
		return list;
	}

	/**
	 * 查询所有实名制登记率明细
	 * @author wangpeng
	 * 2016-11-23 下午4:22:10
	 */
	@DataSourceName("dataSourceGBase")
	public void getRealNameDetailData(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	setFileDownloadHeader(request, response, "4.1.3_实名制登记率_存量用户实名制率_明细.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,用户标识,手机号,入网时间,用户状态,入网渠道,付费类型,用户类型,客户标识");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("realNameMapper.selectRealNameDetailAll", parameterMap);
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
				sb.append(HelperString.objectConvertString(map.get("subs_stat_typ_cd"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("ent_chnl_id"))).append("\t,"); 
				sb.append(HelperString.objectConvertString(map.get("subs_pay_typ_cd"))).append("\t,"); 									
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
	
	//导出头设置
  	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
  		// 这里设置一下让浏览器弹出下载提示框,而不是直接在浏览器中打开
  		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
  		response.setContentType("application/octet-stream;charset=GBK");
  	}


}
