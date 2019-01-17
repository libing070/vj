package com.hpe.cmwa.auditTask.service.sox;

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
public class GrkhcqqfwtjService {
	
	 private DecimalFormat df = new DecimalFormat("######0.00");
	 @Autowired
	 private MybatisDao mybatisDao;

	public List<Map<String, Object>> getGrkhcqqfwtjChart(Map<String, Object> parameterMap) {
		return mybatisDao.getList("grkhcqqfwtj.getGrkhcqqfwtjChart", parameterMap);
	}

	public Map<String, Object> getGrkhcqqfwtjAVGNum(Map<String, Object> parameterMap) {
		return mybatisDao.get("grkhcqqfwtj.getGrkhcqqfwtjAVGNum", parameterMap);
	}

	public List<Map<String, Object>> getGrkhcqqfwtjMAXNum(Map<String, Object> parameterMap) {
		return mybatisDao.getList("grkhcqqfwtj.getGrkhcqqfwtjMAXNum", parameterMap);
	}
	
	//数据表1 
	public List<Map<String, Object>> loadCqqfwtjzhs_sf_TabDetailTable(Pager pager) {
		return mybatisDao.getList("grkhcqqfwtj.loadCqqfwtjzhs_sf_TabDetailTable", pager);
	}
	//数据表2 账户汇总
	public List<Map<String, Object>> loadCqqfwtjzhs_zh_TabDetailTable(Pager pager) {
		return mybatisDao.getList("grkhcqqfwtj.loadCqqfwtjzhs_zh_TabDetailTable", pager);
	}

	public void exportCqqfwtjzhs_sf_Detail(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception{
		List<Map<String, Object>> list = mybatisDao.getList("grkhcqqfwtj.exportCqqfwtjzhs_sf_Detail", parameterMap);
		setFileDownloadHeader(request, response, "客户长期欠费未停机_个人客户长期欠费未停机_长期欠费未停机的帐户数量波动趋势省份.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		int list_num = Integer.parseInt(list.get(0).get("count_acct_id").toString());
		if(list_num==0){
			sb.append("审计区间,省代码,省名称,长期欠费帐户数量,欠费金额(元)");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}else{
				sb.append("审计区间,省代码,省名称,长期欠费帐户数量,欠费金额(元)");
				out.println(sb.toString());
				sb.delete(0, sb.length());
				list = mybatisDao.getList("grkhcqqfwtj.exportCqqfwtjzhs_sf_Detail", parameterMap);
				for (Map<String, Object> map : list) {
					sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");				
					sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
					sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
					sb.append(HelperString.objectConvertString(map.get("count_acct_id"))).append("\t,");				
					sb.append(HelperString.objectConvertString(df.format(map.get("sum_dbt_amt")))).append("\t,");			
					out.println(sb.toString());
					sb.delete(0, sb.length());
				}
				list.clear();
		}
		out.flush();
		out.close();
	}

	public void exportCqqfwtjzhs_zh_Detail(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "客户长期欠费未停机_个人客户长期欠费未停机_长期欠费未停机的帐户数量波动趋势账户.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,帐户标识,用户标识,客户标识,欠费帐期,欠费金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("grkhcqqfwtj.exportCqqfwtjzhs_zh_Detail", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");				
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("acct_id"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("subs_id"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("cust_id"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("acct_prd_cnt"))).append("\t,");	
				sb.append(HelperString.objectConvertString(df.format(map.get("dbt_amt")))).append("\t,");	
						
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
		}
		out.flush();
		out.close();
		
	}
	
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getCityDetailPagerList(Pager pager) {
		return mybatisDao.getList("grkhcqqfwtj.getCityDetailPagerList", pager);
	}
	
	@DataSourceName("dataSourceGBase")
	public void exportMxDetailList(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap) throws Exception{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "客户长期欠费未停机_个人客户长期欠费未停机_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,帐户标识,用户标识,客户标识,欠费科目,欠费账期,欠费金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("grkhcqqfwtj.exportMxDetailList", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,"); 					
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,"); 			
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("acct_id"))).append("\t,"); 					
				sb.append(HelperString.objectConvertString(map.get("subs_id"))).append("\t,"); 					
				sb.append(HelperString.objectConvertString(map.get("cust_id"))).append("\t,");					
				sb.append(HelperString.objectConvertString(map.get("compt_accts_subj_nm"))).append("\t,"); 		
				sb.append(HelperString.objectConvertString(map.get("acct_prd_ytm"))).append("\t,"); 				
				sb.append(HelperString.objectConvertString(df.format(map.get("dbt_amt")))).append("\t,");  	
						
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
