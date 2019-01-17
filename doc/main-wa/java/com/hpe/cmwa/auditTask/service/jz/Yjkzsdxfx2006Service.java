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


@Service
public class Yjkzsdxfx2006Service {
	
	private DecimalFormat df = new DecimalFormat("######0.00");

	 @Autowired
	 private MybatisDao mybatisDao;
	
	//折线每月数据
	public List<Map<String, Object>> getZheXian200604Trend(Map<String, Object> parameterMap) {
		return mybatisDao.getList("yjkzsdxfx200604.getZheXian200604Trend", parameterMap);
	}
	//折线平均值
	public Map<String, Object> getZheXian200604AvgNumber(Map<String, Object> parameterMap) {
		return mybatisDao.get("yjkzsdxfx200604.getZheXian200604AvgNumber", parameterMap);
	}
	//折线最大值
	public List<Map<String, Object>> getZheXian200604MAXNumber(Map<String, Object> parameterMap) {
		return mybatisDao.getList("yjkzsdxfx200604.getZheXian200604MAXNumber", parameterMap);
	}
	//	柱形图
	public List<Map<String, Object>> loadzhuxingChart(Map<String, Object> parameterMap) {
		return mybatisDao.getList("yjkzsdxfx200604.loadzhuxingChart", parameterMap);	
	}
	//	柱形图结论
	public List<Map<String, Object>> getzhuxingTolAmtCon(Map<String, Object> parameterMap) {
		return mybatisDao.getList("yjkzsdxfx200604.getzhuxingTolAmtCon", parameterMap);	
	}
	//赠送非中高端客户、非集团客户数据表
	public List<Map<String, Object>> loadZsfzdfjt_TabDetailTable(Pager pager) {
		return mybatisDao.getList("yjkzsdxfx200604.loadZsfzdfjt_TabDetailTable", pager);
	}
	//导出 赠送非中高端客户、非集团客户数据表
	public void exportloadZsfzdfjt_TabDetail(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception   {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "3.10_有价卡赠送对象分析_赠送非中高端非集团客户_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计区间,省名称,营销案编码,营销案名称,赠送费高端客户、非集团客户金额（元）");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("yjkzsdxfx200604.exportloadZsfzdfjt_TabDetail", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("yjk_offer_cd"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("yjk_offer_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(df.format(map.get("sum_yjk_amt")))).append("\t,");

				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
		}
		out.flush();
		out.close();
	}
	
	//明细数据表
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getCityDetailPagerList(Pager pager) {
		return mybatisDao.getList("yjkzsdxfx200604.getCityDetailPagerList", pager);
	}
	
	//明细数据表导出
	@DataSourceName("dataSourceGBase")
	public void exportMxDetailList(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "3.10_有价卡赠送对象分析_赠送非中高端非集团客户_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省份代码,省份名称,地市代码,地市名称,有价卡序列号,有价卡赠送时间,有价卡类型,赠送有价卡金额（元）,获赠有价卡到期时间," +
				 "获赠有价卡的用户标识,获赠有价卡的客户标识,获赠有价卡的手机号,增送月ARPU,赠送前一月ARPU,赠送前两月ARPU,平均月ARPU,有价卡赠送依据," +
				 "营销案编码,营销案名称,有价卡赠送文号说明,赠送渠道标识,赠送渠道名称 ");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("yjkzsdxfx200604.exportMxDetailList", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");						 
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");				 
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");						 
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");					 
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");				 
				sb.append(HelperString.objectConvertString(map.get("yjk_ser_no"))).append("\t,");						 
				sb.append(HelperString.objectConvertString(map.get("yjk_pres_dt"))).append("\t,");					 
				sb.append(HelperString.objectConvertString(map.get("yjk_typ"))).append("\t,");						 
				sb.append(HelperString.objectConvertString(df.format(map.get("yjk_amt")))).append("\t,");						 
				sb.append(HelperString.objectConvertString(map.get("yjk_end_dt"))).append("\t,");						 
				sb.append(HelperString.objectConvertString(map.get("user_id"))).append("\t,");						 
				sb.append(HelperString.objectConvertString(map.get("cust_id"))).append("\t,");						 
				sb.append(HelperString.objectConvertString(map.get("msisdn"))).append("\t,");							 
				sb.append(HelperString.objectConvertString(df.format(map.get("send_mon_arpu")))).append("\t,");					 
				sb.append(HelperString.objectConvertString(df.format(map.get("send_lastm_arpu")))).append("\t,");				 
				sb.append(HelperString.objectConvertString(df.format(map.get("send_last2m_arpu")))).append("\t,");				 
				sb.append(HelperString.objectConvertString(map.get("send_avg_arpu"))).append("\t,");					 
				sb.append(HelperString.objectConvertString(map.get("yjk_dependency"))).append("\t,");					 
				sb.append(HelperString.objectConvertString(map.get("yjk_offer_cd"))).append("\t,");					 
				sb.append(HelperString.objectConvertString(map.get("yjk_offer_nm"))).append("\t,");					 
				sb.append(HelperString.objectConvertString(map.get("offer_word"))).append("\t,");						 
				sb.append(HelperString.objectConvertString(map.get("cor_chnl_typ"))).append("\t,");					 
				sb.append(HelperString.objectConvertString(map.get("cor_chnl_nm"))).append("\t,");	

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
