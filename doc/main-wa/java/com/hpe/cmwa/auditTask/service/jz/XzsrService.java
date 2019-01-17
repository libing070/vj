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
 * 
 * @author gongtingting
 *
 */
@Service
public class XzsrService {

	private DecimalFormat	   df = new DecimalFormat("######0.00");
	@Autowired
	private MybatisDao	mybatisDao;

	/**
	 * Desc 查询省汇总表统计周期内每月总虚增收入
	 * @param formatParameter
	 * @return
	 */
	public List<Map<String, Object>> getMonthTotalIncome(Map<String, Object> params) {
		
		   List<Map<String, Object>> list = mybatisDao.getList("xzsr.selectPerMonthXzsr", params);
		
		   return list;
	}
	/**
	 * Desc 查询省汇总表统计周期内每月总虚增收入平均值
	 * @param formatParameter
	 * @return
	 */
	public List<Map<String, Object>> getMonthTotalIncomeAvg(Map<String, Object> params) {
		
		   List<Map<String, Object>> list1 = mybatisDao.getList("xzsr.selectAvgMonthXzsr", params);
		   
		   return list1;
	}
	
	/**
	 * 查询折线结论
	 * @param pager
	 * @return
	 */
	public Map<String, Object> getQianZheJieLun(Map<String, Object> formatParameter) {

		Map<String, Object> map = mybatisDao.get("xzsr.selectQianZhejielun",formatParameter);
		
		return map;
		
	}
	public Map<String, Object> getZheJieLun(Map<String, Object> formatParameter) {
		Map<String, Object> map = mybatisDao.get("xzsr.selectZhejielun",formatParameter);
		Double zds= Double.parseDouble(map.get("maxzhi").toString());
		Double pjz= Double.parseDouble(map.get("pjz").toString());
		Double avg_tol_vt_amt = pjz;
		if(pjz==0){
			map.put("zb", 0.00);
			map.put("avg_tol_vt_amt", 0.00);
		}else{
			Double  d=(zds-pjz)/pjz*100;
			map.put("zb", Double.parseDouble(df.format(d)));
			map.put("avg_tol_vt_amt",avg_tol_vt_amt);
		}
		return map;
	}
	/**
	 * 加载柱状图
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getOnloadZhu(Map<String, Object> formatParameter) {
		
		
		List<Map<String, Object>> list1 = mybatisDao.getList("xzsr.selectOnloadZhu", formatParameter);
	
	
		return list1;
	}
	/**
	 * 地市汇总
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getSumCity(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("xzsr.selectSumCity", pager);
	
		return list;
	}
	/**
	 * daochudishi
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getSumCityAll(Map<String, Object> params) {

		List<Map<String, Object>> list = mybatisDao.getList("xzsr.selectSumCityAll", params);
		return list;
	}
	/**
	 * 柱状图的结论
	 * @param formatParameter
	 * @return
	 */
	public List<Map<String, Object>> getZhuJieLun(Map<String, Object> formatParameter) {

		List<Map<String, Object>> list = mybatisDao.getList("xzsr.selectZhuJieLun", formatParameter);
		return list;
	}
	
	
	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
		// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
		response.setContentType("application/octet-stream;charset=GBK");
	}
	
	/**
	 * Desc 查询明细数据
	 * @param formatParameter
	 * @return
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getDetDetailData(Pager pager) {
		
		 List<Map<String, Object>> list = mybatisDao.getList("xzsr.selectMingXi", pager);
		 return list;
	}
	@DataSourceName("dataSourceGBase")
	public void getXzsrDetailData(HttpServletRequest request,HttpServletResponse response, Map<String, Object> params)throws Exception  {
			List<Map<String, Object>> charList = new ArrayList<Map<String,Object>>(); 
			setFileDownloadHeader(request, response, "4.1.1_指标作假_虚增收入_明细.csv");
			PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
	    	StringBuffer sb = new StringBuffer();
	    	sb.append("审计月,省代码,省名称,地市代码,地市名称,虚增收入类型编码,虚增收入类型名称,用户标识,客户名称,虚增收入(元)");
	    	out.println(sb.toString());
	    	sb.delete(0, sb.length());
	    	for(int i=0;i>=0;i++){
				params.put("pageStar", 10000*i);
				params.put("pageEnd", 10000);
				charList = mybatisDao.getList("xzsr.selectXzsrDetailAll", params);
				if(charList.size()==0){
					break;
				}
		    	for (Map<String, Object> resultMap : charList) {
		    		sb.append(HelperString.objectConvertString(resultMap.get("Aud_trm"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("cmcc_prov_prvd_id"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("short_name"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("cmcc_prov_id"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("cmcc_prvd_nm_short"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("VT_FEE_TYP_ID"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("VT_FEE_TYP"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("USER_ID"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("cust_nm"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(df.format(resultMap.get("vt_amt")))).append("\t,");
		    		out.println(sb.toString());
		    		sb.delete(0, sb.length());
		    	}
		    	charList.clear();
			}
	    	out.flush();
	    	out.close();
	    	
	    	
	    	
		}
	
	
}

