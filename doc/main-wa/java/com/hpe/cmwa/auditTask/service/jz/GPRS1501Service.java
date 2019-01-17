package com.hpe.cmwa.auditTask.service.jz;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
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
 * 3.2.15包月流量产品不设限
 * <pre>
 * Desc： 
 * @author   wangpeng
 * @refactor wangpeng
 * @date     2017-1-20 上午9:59:00
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-1-20 	   wangpeng 	         1. Created this class. 
 * </pre>
 */
@Service
public class GPRS1501Service {
	
	private DecimalFormat df = new DecimalFormat("######0.00");
    @Autowired
    private MybatisDao	mybatisDao;
    /**
     * 获取省汇总信息
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-20 上午11:03:07
     * </pre>
     */
    public List<Map<String, Object>> getSumGprsPrvd(Map<String, Object> parameterMap) {
	return mybatisDao.getList("gprs1501.getSumGprsPrvd", parameterMap);
    }
    
    /**
     * 分页查询
     * <pre>
     * Desc  
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-20 下午3:36:13
     * </pre>
     */
    public List<Map<String, Object>> getSumGprsPrvdPager(Pager pager) {
 	return mybatisDao.getList("gprs1501.getSumGprsPrvdPager", pager);
     }
    /**
     * 获取地市汇总
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-20 下午4:17:53
     * </pre>
     */
    public List<Map<String, Object>> getSumGprsCty(Map<String, Object> parameterMap) {
	return mybatisDao.getList("gprs1501.getSumGprsCty", parameterMap);
    }
    
    /**
     * 分页查询地市汇总信息
     * <pre>
     * Desc  
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-20 下午4:41:00
     * </pre>
     */
    public List<Map<String, Object>> getSumGprsCtyPager(Pager pager) {
 	return mybatisDao.getList("gprs1501.getSumGprsCtyPager", pager);
     }
    
    /**
     * 获得大明细分页数据
     * <pre>
     * Desc  
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-20 下午5:18:44
     * </pre>
     */
    @DataSourceName("dataSourceGBase")
    public List<Map<String, Object>> getGprsDetailPager(Pager pager) {
	return mybatisDao.getList("gprs1501.getGprsDetailPager", pager);
    }

    /**
     * 获取全部明细数据
     * 
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-20 下午5:20:00
     * </pre>
     */
    @DataSourceName("dataSourceGBase")
	public void getGprsDetailAll(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	setFileDownloadHeader(request, response, "4.3.2_包月流量产品不设限_用户月使用流量违规超过50G_明细表.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省份代码,省份名称,地市代码,地市名称,用户标识,手机号,用户使用总流量(G),用户状态,用户类型,出账收入(元)," +
				"业务受理类型,业务办理时间,基础套餐标识,基础套餐名称");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("gprs1501.getGprsDetailAll", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("AUD_TRM"))).append("\t,");		 								
				sb.append(HelperString.objectConvertString(map.get("CMCC_PROV_PRVD_ID"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("SHORT_NAME"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("CMCC_PROV_ID"))).append("\t,");  								
				sb.append(HelperString.objectConvertString(map.get("CMCC_PRVD_NM_SHORT"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("SUBS_ID"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("MSISDN"))).append("\t,"); 
				
				
				sb.append(HelperString.objectConvertString(map.get("SUM_STRM_AMT"))).append("\t,");		 								
				sb.append(HelperString.objectConvertString(map.get("SUBS_STAT_TYP_CD"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("SUBS_BUSN_TYP_CD"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(df.format(map.get("MER_AMT")))).append("\t,");  								
				sb.append(HelperString.objectConvertString(map.get("BUSI_ACCE_TYP"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("BUSI_OPR_TM"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("BASIC_PACK_ID"))).append("\t,"); 
				sb.append(HelperString.objectConvertString(map.get("BASIC_PACK_NAME"))).append("\t,"); 							
				
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
