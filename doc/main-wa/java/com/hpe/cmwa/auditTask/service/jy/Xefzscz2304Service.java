package com.hpe.cmwa.auditTask.service.jy;

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
public class Xefzscz2304Service {
	@Autowired
	private MybatisDao mybatisDao;
	
	// 折线图
	public List<Map<String, Object>> getZheXian2304Trend(Map<String, Object> parameterMap){
		return mybatisDao.getList("XEFZSCZ2304.getZheXian2304Trend", parameterMap);
	}
	// 折线图平均值
	public Map<String, Object>  getZheXian2304AVGNumber(Map<String, Object> parameterMap){
		return mybatisDao.get("XEFZSCZ2304.getZheXian2304AVGNumber", parameterMap);
	}
	//折线图最大值
	public List<Map<String, Object>> getZheXian2304MAXNumber(Map<String, Object> parameterMap){
		return mybatisDao.getList("XEFZSCZ2304.getZheXian2304MAXNumber", parameterMap);
	}
	//折线数据表
	public List<Map<String, Object>> loadXefzscz_TabDetailTable(Pager pager){
		return mybatisDao.getList("XEFZSCZ2304.loadXefzscz_TabDetailTable", pager);
	}
	//折线导出
	public List<Map<String, Object>> exportXefzsczDetail(Map<String, Object> parameterMap) {
		return mybatisDao.getList("XEFZSCZ2304.exportXefzsczDetail", parameterMap);
	}
	//柱形图
	public List<Map<String, Object>> loadzhuxingChart(Map<String, Object> parameterMap){
		return mybatisDao.getList("XEFZSCZ2304.loadzhuxingChart", parameterMap);
	}
	//柱形图结论
	public List<Map<String, Object>> getzhuxing2304Top3Con(Map<String, Object> parameterMap){
		return mybatisDao.getList("XEFZSCZ2304.getzhuxing2304Top3Con", parameterMap);
	}
	//柱形图数据表
	public List<Map<String, Object>> loadDsxefzcz_TabDetailTable(Pager pager){
		return mybatisDao.getList("XEFZSCZ2304.loadDsxefzcz_TabDetailTable", pager);
	}
	//柱形图数据表导出
	public List<Map<String, Object>> exportDsxefzczDetail(Map<String, Object> parameterMap) {
		return mybatisDao.getList("XEFZSCZ2304.exportDsxefzczDetail", parameterMap);
	}
	//明细表
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getCityDetailPagerList(Pager pager){
		return mybatisDao.getList("XEFZSCZ2304.getCityDetailPagerList", pager);
	}
	//明细表导出
	@DataSourceName("dataSourceGBase")
	public void exportMxDetailList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap)throws Exception  {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "员工异常操作_小额非整数充值_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,被充值手机号码用户标识,缴费类型编码,缴费类型,充值日期,充值金额(元),渠道标识,渠道名称");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("XEFZSCZ2304.exportMxDetailList", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("CMCC_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("user_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("pay_type"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("pay_type_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("pay_date"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("pay_amt"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("chnl_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("chnl_nm"))).append("\t");
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
