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

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.common.datasource.DataSourceName;
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;


@Service
public class LlzshgxService extends BaseObject{

	 @Autowired
	 private MybatisDao mybatisDao;
	 	
	 private DecimalFormat df = new DecimalFormat("######0.00");
	 
	 
	//导出头设置
	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
		// 这里设置一下让浏览器弹出下载提示框,而不是直接在浏览器中打开
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
		response.setContentType("application/octet-stream;charset=GBK");
	}

	public Map<String, Object> getUserCon(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mybatisDao.get("llzshgx.getUserCon", params);
	}

	public Map<String, Object> getMaxAndMinAud_trm(Map<String, Object> params) {
		return mybatisDao.get("llzshgx.getMaxAndMinAud_trm", params);
	}

	public List<Map<String, Object>> getFbqjChart(Map<String, Object> params) {
		return mybatisDao.getList("llzshgx.getFbqjChart", params);
	}

	public Map<String, Object> getZsllyhMaxAndMinAud_trm(Map<String, Object> params) {
		return mybatisDao.get("llzshgx.getZsllyhMaxAndMinAud_trm", params);
	}

	public List<Map<String, Object>> getZsllyhChart(Map<String, Object> params) {
		return mybatisDao.getList("llzshgx.getZsllyhChart", params);
	}

	public List<Map<String, Object>> getZsllyhCon(Map<String, Object> params) {
		return mybatisDao.getList("llzshgx.getZsllyhCon", params);
	}

	public List<Map<String, Object>> loadZsllyDetailTable(Pager pager) {
		return mybatisDao.getList("llzshgx.loadZsllyDetailTable", pager);
	}

	public void exportZsllyhDetail(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap) throws Exception{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "3.4_流量产品管理合规性_流量赠送合规性_用户统计_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计区间,省代码,省名称,地市代码,地市名称,用户标识,年度累计赠送流量（G）");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("llzshgx.exportZsllyhDetail", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("user_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(df.format(map.get("pres_strm_tol")))).append("\t,");
				
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
		}
		out.flush();
		out.close();
	}

	
	public Map<String, Object> getLlyxzMaxAndMinAud_trm(Map<String, Object> params) {
		return mybatisDao.get("llzshgx.getLlyxzMaxAndMinAud_trm", params);
	}


	public List<Map<String, Object>> getLlyxzCharts(Map<String, Object> params) {
		return mybatisDao.getList("llzshgx.getLlyxzCharts", params);
	}


	public List<Map<String, Object>> getLlyxzCon(Map<String, Object> params) {
		return mybatisDao.getList("llzshgx.getLlyxzCon", params);
	}


	public List<Map<String, Object>> loadLlyxzDetailTable(Pager pager) {
		return mybatisDao.getList("llzshgx.loadLlyxzDetailTable", pager);
	}


	public void exportLlyxzDetail(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "3.4_流量产品管理合规性_流量赠送合规性_营销案统计_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计区间,省代码,省名称,地市代码,地市名称,用户标识,营销案编码,营销案名称,办理渠道标识,办理渠道名称");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("llzshgx.exportLlyxzDetail", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("user_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("offer_cd"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("offer_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("chnl_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("chnl_nm"))).append("\t,");
				
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
		}
		out.flush();
		out.close();
	}

	@DataSourceName("dataSourceGBase")
	public Map<String, Object> getCityDetailMaxAndMinAud_trm(Map<String, Object> params) {
		return mybatisDao.get("llzshgx.getCityDetailMaxAndMinAud_trm", params);
	}

	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getCityDetailPagerList(Pager pager) {
		return mybatisDao.getList("llzshgx.getCityDetailPagerList", pager);
	}

	@DataSourceName("dataSourceGBase")
	public void exportMxDetailList(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "3.4_流量产品管理合规性_流量赠送合规性_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,用户标识,统计周期,本月赠送流量数合计（G）,本月赠送流量实际使用量（G）");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("llzshgx.exportMxDetailList", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("user_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("statis_mon"))).append("\t,");
				sb.append(HelperString.objectConvertString(df.format(map.get("pres_strm_tol")))).append("\t,");
				sb.append(HelperString.objectConvertString(df.format(map.get("pres_strm_use")))).append("\t,");
				
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
		}
		out.flush();
		out.close();
	}


	
		
}
