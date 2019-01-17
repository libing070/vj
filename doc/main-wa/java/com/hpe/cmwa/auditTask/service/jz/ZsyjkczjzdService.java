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
public class ZsyjkczjzdService {
	
	private DecimalFormat df = new DecimalFormat("######0.00");

	 @Autowired
	 private MybatisDao mybatisDao;
	 
	//赠送有价卡集中充值地市分布      图形数据
	public List<Map<String, Object>> getZsyjkjzdCzdsChart(Map<String, Object> parameterMap) {
		 return mybatisDao.getList("zsyjkczjzd.getZsyjkjzdCzdsChart", parameterMap);
	}
	//赠送有价卡集中充值地市分布      结论数据
	public List<Map<String, Object>> getZsyjkjzdCzdsCon(Map<String, Object> parameterMap) {
		return mybatisDao.getList("zsyjkczjzd.getZsyjkjzdCzdsCon", parameterMap);
	}
	//赠送有价卡集中充值地市分布	    数据表
	public List<Map<String, Object>> getZsyjkjzdCzdsDetailTable(Pager pager) {
		return mybatisDao.getList("zsyjkczjzd.getZsyjkjzdCzdsDetailTable", pager);
	}
	public void exportZsyjkjzczdsDetail(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap) throws Exception  {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "3.11_有价卡赠送合规性_赠送有价卡充值集中度_地市排名_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("统计区间,省名称,地市名称,集中充值有价卡金额（元）,集中充值有价卡数量,被充值手机号码数量");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("zsyjkczjzd.exportZsyjkjzczdsDetail", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("sum_yjk_amt"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("sum_yjk_num"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("sum_charge_msisdn_num"))).append("\t,");
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
		}
		out.flush();
		out.close();
	}
	 
	 
	
	//赠送有价卡集中充值金额排名前十手机号码 图形
	public List<Map<String, Object>> getZsyjkjzczjeCharts(Map<String, Object> parameterMap) {
		return mybatisDao.getList("zsyjkczjzd.getZsyjkjzczjeCharts", parameterMap);
	}
	//赠送有价卡集中充值金额排名前十手机号码 结论
	public List<Map<String, Object>> getZsyjkjzczjeCon(Map<String, Object> parameterMap) {
		return mybatisDao.getList("zsyjkczjzd.getZsyjkjzczjeCon", parameterMap);
	}
	//赠送有价卡集中充值金额排名前十手机号码 数据表
	public List<Map<String, Object>> getZsyjkjzczjeDetail(Pager pager) {
		return mybatisDao.getList("zsyjkczjzd.getZsyjkjzczjeDetail", pager);
	}
	//赠送有价卡集中充值金额排名前十手机号码 数据表导出
	public void exportZsyjkjzczjeDetail(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap) throws Exception{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "3.11_有价卡赠送合规性_赠送有价卡充值集中度_用户排名_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("统计区间,省名称,地市名称,被充值手机号码用户标识,充值有价卡金额（元）,充值有价卡数量");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("zsyjkczjzd.exportZsyjkjzczjeDetail", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("charge_user"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("sum_yjk_amt"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("sum_yjk_num"))).append("\t,");
				
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
		return mybatisDao.getList("zsyjkczjzd.getCityDetailPagerList", pager);
	}
	//明细数据表 导出
	@DataSourceName("dataSourceGBase")
	public void exportMxDetailList(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "3.11_有价卡赠送合规性_赠送有价卡充值集中度_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,操作省份代码,操作省份名称,操作地市代码,操作地市名称,充值月份,交易充值日期,充值交易时间,被充值手机号码用户标识,"
				 +"充值方式,有价卡序列号,有价卡类型,有价卡状态,金额（元）,有价卡赠送时间,获赠有价卡的用户标识," 
				 +"有价卡赠送涉及的营销案编号,营销案名称,营销案种类");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("zsyjkczjzd.exportMxDetailList", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");	 		
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("trade_mon"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("tradedate"))).append("\t,");	 		
				sb.append(HelperString.objectConvertString(map.get("tradetime"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("charge_user"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("tradetype"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("yjk_ser_no"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("supplycardkind"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("cardflag"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("yjk_amt"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("yjk_pres_dt"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("user_id"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("yjk_offer_cd"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("offer_nm"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("offer_cls"))).append("\t,");			


				
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
