package com.hpe.cmwa.auditTask.service.jy;

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
 * 有价卡赠送集中度异常(3001)
 * @author yuzhinan
 */

@Service
public class YJKZCYC3001Service {

	  @Autowired
	  private MybatisDao mybatisDao;
	  private DecimalFormat df = new DecimalFormat("######0.00");
	 
	 // 统计周期内每月累计赠送有价卡金额 
	 public List<Map<String, Object>> getYJKZCYC3001Trend(Map<String, Object> parameterMap){
		 return mybatisDao.getList("YJKZCYC3001.getYJKZCYC3001Trend", parameterMap);
	 }
	// -- 统计周期内全省的平均单月累计赠送有价卡金额 
	 public Map<String, Object>  getYJKZCYC3001AVGNumber(Map<String, Object> parameterMap){
		 return mybatisDao.get("YJKZCYC3001.getYJKZCYC3001AVGNumber", parameterMap);
	 }
	//计算周期内最大赠送金额
	 public List<Map<String, Object>> getYJKZCYC3001MAXNumber(Map<String, Object> parameterMap){
		 return mybatisDao.getList("YJKZCYC3001.getYJKZCYC3001MAXNumber", parameterMap);
	 }
	 //各区间赠送
	 public Map<String, Object> getGzsqjChart(Map<String, Object> parameterMap) {
			return mybatisDao.get("YJKZCYC3001.getGzsqjChart", parameterMap);
	}
	 
	//各区间统计明细 
	public List<Map<String, Object>> getGzsqjyhDetailTable(Pager pager){
		return mybatisDao.getList("YJKZCYC3001.getGzsqjyhDetailTable", pager);
	}
	//用户获赠统计top10
	 public List<Map<String, Object>> getYhhzTop10TelCharts(Map<String, Object> parameterMap) {
		 return mybatisDao.getList("YJKZCYC3001.getYhhzTop10TelCharts", parameterMap);
	 }
	 
	//用户获赠统计明细 
	 public List<Map<String, Object>> getYhhzyjkDetailTable(Pager pager){
		 return mybatisDao.getList("YJKZCYC3001.getYhhzyjkDetailTable", pager);
	 }
	//用户获赠统计明细 (导出)
	 public List<Map<String, Object>> exportYhhzDetail(Map<String, Object> parameterMap) {
			return mybatisDao.getList("YJKZCYC3001.exportYhhzDetail", parameterMap);
		}
	 //渠道赠送有价卡金额统计top10
	 public List<Map<String, Object>> getQdzsyjkTop10Charts(Map<String, Object> parameterMap) {
		 return mybatisDao.getList("YJKZCYC3001.getQdzsyjkTop10Charts", parameterMap);
	 }
	// 渠道赠送明细 
	 public List<Map<String, Object>> getQdzsyjkDetailTable(Pager pager){
		 return mybatisDao.getList("YJKZCYC3001.getQdzsyjkDetailTable", pager);
	 }
	 
	//渠道赠送有价卡金额统计(导出)
	public List<Map<String, Object>> exportQdzsDetail(Map<String, Object> parameterMap) {
		return mybatisDao.getList("YJKZCYC3001.exportQdzsDetail", parameterMap);
	}
	 
	 //营销案赠送top10
	 public List<Map<String, Object>> getYxazsyjkTop10Chart(Map<String, Object> parameterMap) {
		 return mybatisDao.getList("YJKZCYC3001.getYxazsyjkTop10Chart", parameterMap);
	 }
	// 营销案赠送明细 
	public List<Map<String, Object>> getYxazsyjkDetailTable(Pager pager){
		return mybatisDao.getList("YJKZCYC3001.getYxazsyjkDetailTable", pager);
	}
	//营销案赠送有价卡金额统计(导出)
	public List<Map<String, Object>> exportYxazsDetail(Map<String, Object> parameterMap) {
		return mybatisDao.getList("YJKZCYC3001.exportYxazsDetail", parameterMap);
	}
	//明细页面
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getCityDetailPagerList(Pager pager) {
		return mybatisDao.getList("YJKZCYC3001.getCityDetailPagerList", pager);
	}
	//明细页面导出
	@DataSourceName("dataSourceGBase")
	public void exportMxDetailList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap)throws Exception  {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "有价卡赠送相关集中度异常_有价卡赠送集中度异常_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计起始月,审计结束月,省代码,省名称,地市代码,地市名称,有价卡序列号,金额(元),有价卡类型," +
				  "有价卡赠送时间,获赠有价卡到期时间,获赠有价卡的用户标识,获赠有价卡的手机号,有价卡赠送涉及的营销案编号" +
				  ",营销案名称,赠送渠道标识,赠送渠道名称");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("YJKZCYC3001.exportMxDetailList", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm_begin"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("aud_trm_end"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("yjk_ser_no"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("yjk_amt"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("yjk_typ"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("yjk_pres_dt"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("yjk_end_dt"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("user_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("pres_msisdn"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("yjk_offer_cd"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("yjk_offer_nm"))).append("\t,");
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
	
	
	
	public List<Map<String, Object>> getYhhzTelDetailTable(Pager pager) {
		 return mybatisDao.getList("YJKZCYC3001.getYhhzTelDetailTable", pager);
	}
	public void exportYhhzTelDetailList(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "有价卡赠送相关集中度异常_有价卡赠送集中度异常_用户获赠有价卡金额统计2_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省份代码,省份名称,地市代码,地市名称,获赠手机号,用户类型,集团客户名称,赠送有价卡数量,赠送金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("YJKZCYC3001.exportYhhzTelDetailList", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("msisdn"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("user_type"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("org_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("yjk_num"))).append("\t,");
				sb.append(HelperString.objectConvertString(df.format(map.get("yjk_amt")))).append("\t,");
				
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
