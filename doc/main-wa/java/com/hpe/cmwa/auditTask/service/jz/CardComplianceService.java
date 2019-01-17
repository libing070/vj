package com.hpe.cmwa.auditTask.service.jz;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
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
 * 卡号销售费合规
 * <pre>
 * Desc： 
 * @author   wangpeng
 * @refactor wangpeng
 * @date     2016-11-25 上午11:06:05
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2016-11-25 	   wangpeng 	         1. Created this class. 
 * </pre>
 */
@Service
public class CardComplianceService extends BaseObject {
	
	private DecimalFormat df = new DecimalFormat("######0.00");
	@Autowired
	private MybatisDao	mybatisDao;
	
	public Map<String,Object> getSumPrvdinceCon(Map<String,Object> params){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = mybatisDao.getList("cardCompliancesMapper.selectSumPrvdinceCon", params);
		map.put("list", list);
		return map;
	}
	
	/**
	 * 获取省汇总信息
	 * <pre>
	 * Desc  
	 * @param params
	 * @return
	 * @author wangpeng
	 * 2016-11-25 下午3:30:39
	 * </pre>
	 */
	public List<Map<String,Object>> getSumPrvdince(Map<String,Object> params){
		
		List<Map<String, Object>> list = mybatisDao.getList("cardCompliancesMapper.selectSumPrvdince", params);
		
		return list;
	}
	/**
	 * 获取地市汇总信息
	 * <pre>
	 * Desc  
	 * @param params
	 * @return
	 * @author wangpeng
	 * 2016-11-25 下午3:31:10
	 * </pre>
	 */
	public List<Map<String,Object>> getSumCityByMap(Map<String,Object> params){
		
		List<Map<String, Object>> list = mybatisDao.getList("cardCompliancesMapper.selectSumCity", params);
		
		return list;
	}
	/**
	 * 获取全国排名
	 * <pre>
	 * Desc  
	 * @param params
	 * @return
	 * @author jh
	 * 2016-11-27 下午4:44:54
	 * </pre>
	 */
	public List<Map<String,Object>> getSumSort(Map<String,Object> params){
		
	    List<Map<String,Object>> list = mybatisDao.getList("cardCompliancesMapper.selectSumSort", params);
		
	    return list;
	}
	/**
	 * 获取放号酬金前三城市
	 * <pre>
	 * Desc  
	 * @param params
	 * @return
	 * @author jh
	 * 2016-11-27 下午5:00:18
	 * </pre>
	 */
	public List<Map<String,Object>> getTop3City(Map<String,Object> params){
		
		List<Map<String, Object>> list = mybatisDao.getList("cardCompliancesMapper.selectTop3City", params);
		
		return list;
	}
	/**
	 * 获取地市汇总信息分页
	 * <pre>
	 * Desc  
	 * @param params
	 * @return
	 * @author wangpeng
	 * 2016-11-25 下午3:31:10
	 * </pre>
	 */
	public List<Map<String,Object>> getSumCity(Pager pager){
		
		List<Map<String, Object>> list = mybatisDao.getList("cardCompliancesMapper.selectSumCityPager", pager);
		
		return list;
	}
	
	/**
	 * 查询所有地市汇总信息做导出
	 * <pre>
	 * Desc  
	 * @param params
	 * @return
	 * @author jh
	 * 2016-11-27 下午6:28:55
	 * </pre>
	 */
	public List<Map<String,Object>> getSumCityAll(Map<String,Object> params){
		
		List<Map<String, Object>> list = mybatisDao.getList("cardCompliancesMapper.selectSumCityAll", params);
		
		return list;
	}
	
	/**
	 * 获取明细分页数据
	 * <pre>
	 * Desc  
	 * @param pager
	 * @return
	 * @author jh
	 * 2016-11-27 下午6:02:14
	 * </pre>
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String,Object>> getDetailList(Pager pager){
		
		List<Map<String, Object>> list = mybatisDao.getList("cardCompliancesMapper.selectDetailList", pager);
		
		return list;
	}
	
	//导出头设置
  	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
  		// 这里设置一下让浏览器弹出下载提示框,而不是直接在浏览器中打开
  		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
  		response.setContentType("application/octet-stream;charset=GBK");
  	}
  	@DataSourceName("dataSourceGBase")
	public void getDetailAll(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	setFileDownloadHeader(request, response, "4.1.2_卡号销售费合规_卡号销售费合规_明细表.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,实体渠道标识,实体渠道名称,放号酬金(元),放号酬金占比(%)," +
				"基础业务服务代理酬金(元),基础业务服务代理酬金占比(%),增值业务代理酬金(元),增值业务代理酬金占比(%)," +
				"激励酬金(元),激励酬金占比(%),终端酬金(元),终端酬金占比(%),总酬金(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("cardCompliancesMapper.selectDetailAll", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");		 								
				sb.append(HelperString.objectConvertString(map.get("cmccProvPrvdId"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("shortName"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("cmccProvId"))).append("\t,");  								
				sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("corChnlId"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("chnlNm"))).append("\t,"); 
				sb.append(HelperString.objectConvertString(df.format(map.get("outNbrRwd")))).append("\t,"); 
				
				Double perOutNbrRwd = map.get("perOutNbrRwd") == null ? 0.00 : Double.parseDouble(map.get("perOutNbrRwd").toString());
			    sb.append(HelperString.objectConvertString(df.format(perOutNbrRwd)+"%")).append("\t,");

			    sb.append(HelperString.objectConvertString(df.format(map.get("basicBusnSvcAgcRwd")))).append("\t,"); 	

			    Double perBasicBusnSvcAgcRwd = map.get("perBasicBusnSvcAgcRwd") == null ? 0.00 : Double.parseDouble(map.get("perBasicBusnSvcAgcRwd").toString());
			    sb.append(HelperString.objectConvertString(df.format(perBasicBusnSvcAgcRwd)+"%")).append("\t,");

			    sb.append(HelperString.objectConvertString(df.format(map.get("valueAddedBusnAgcRwd")))).append("\t,"); 

			    Double perValueAddedBusnAgcRwd = map.get("perValueAddedBusnAgcRwd") == null ? 0.00 : Double.parseDouble(map.get("perValueAddedBusnAgcRwd").toString());
			    sb.append(HelperString.objectConvertString(df.format(perValueAddedBusnAgcRwd)+"%")).append("\t,");

			    sb.append(HelperString.objectConvertString(df.format(map.get("incenRwd")))).append("\t,"); 

			    Double perIncenRwd = map.get("perIncenRwd") == null ? 0.00 : Double.parseDouble(map.get("perIncenRwd").toString());
			    sb.append(HelperString.objectConvertString(df.format(perIncenRwd)+"%")).append("\t,");

			    sb.append(HelperString.objectConvertString(df.format(map.get("trmnlRwd")))).append("\t,"); 

			    Double perTrmnlRwd = map.get("perTrmnlRwd") == null ? 0.00 : Double.parseDouble(map.get("perTrmnlRwd").toString());
			    sb.append(HelperString.objectConvertString(df.format(perTrmnlRwd)+"%")).append("\t,");

			    sb.append(HelperString.objectConvertString(df.format(map.get("chouJinTol")))).append("\t,"); 
				
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
		}
		out.flush();
		out.close();
	}
	
	
}
