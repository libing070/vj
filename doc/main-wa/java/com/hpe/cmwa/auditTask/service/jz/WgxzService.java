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

@Service
public class WgxzService {

	@Autowired
	private MybatisDao mybatisDao;
	//折线数据
	public List<Map<String, Object>> getWgzsChart(Map<String, Object> parameterMap) {
		return mybatisDao.getList("wgxz.getWgzsChart", parameterMap);
	}
	//平均值
	public Map<String, Object> getWgzsChartAVGNumber(Map<String, Object> parameterMap) {
		return mybatisDao.get("wgxz.getWgzsChartAVGNumber", parameterMap);
	}
	//最大值
	public List<Map<String, Object>> getWgzsChartMAXNumber(Map<String, Object> parameterMap) {
		return mybatisDao.getList("wgxz.getWgzsChartMAXNumber", parameterMap);
	}
	//折线数据表
	public List<Map<String, Object>> loadWgzs_TabDetailTable(Pager pager) {
		return mybatisDao.getList("wgxz.loadWgzs_TabDetailTable", pager);
	}
	public void exportWgzsDetail(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "3.12_违规新增非长市漫合一的套餐_汇总.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,新增非长市漫合一的套餐数量");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("wgxz.exportWgzsDetail", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("infrac_pack_num"))).append("\t,");
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
		}
		out.flush();
		out.close();

	}
	
	//明细数据
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getCityDetailPagerList(Pager pager) {
		return mybatisDao.getList("wgxz.getCityDetailPagerList", pager);
	}
	
	//明细导出
	@DataSourceName("dataSourceGBase")
	public void exportMxDetailList(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap) throws Exception{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "3.12_违规新增非长市漫合一的套餐_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,资费套餐统一编码,资费套餐名称,资费套餐描述,资费套餐销售状态,资费套餐停售时间,上线日期,目标区域市场,目标客户群,语音总量,国内通话," 
					+" 省内通话,本地通话,集团通话,亲情通话,网内通话,小区通话,闲时通话,长市漫一体化套餐标识");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("wgxz.exportMxDetailList", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("fee_pack_unit_cd"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("fee_pack_nm"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("fee_pack_desc"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("fee_pack_stat"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("pack_end_dt"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("onln_dt"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("tgt_rgn_mkt"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("tgt_grp"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("voic_tot"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("dom_call"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("prov_call"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("loacl_call"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("org_call"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("folk_call"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("nets_call"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("cell_call"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("idle_tm_call"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("long_pack_id"))).append("\t,");				

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
