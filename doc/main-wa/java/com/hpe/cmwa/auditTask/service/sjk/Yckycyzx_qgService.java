package com.hpe.cmwa.auditTask.service.sjk;

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
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;


@Service
public class Yckycyzx_qgService {

	
	@Autowired
	private MybatisDao mybatisDao;
	
	private DecimalFormat df = new DecimalFormat("######0.00");
	private DecimalFormat df4 = new DecimalFormat("######0.0000");
	
	/**
	 * 柱形图
	 * @param parameterMap
	 * @return
	 */
	public List<Map<String, Object>> load_column_chart(Map<String, Object> parameterMap) {
		return mybatisDao.getList("yckycyzx_qgMapper.load_column_chart", parameterMap);
	}
	
	/**
	 * 柱形图
	 * @param parameterMap
	 * @return
	 */
	public List<Map<String, Object>> load_map_chart(Map<String, Object> parameterMap) {
		return mybatisDao.getList("yckycyzx_qgMapper.load_map_chart", parameterMap);
	}
	
	/**
	 * 折线图
	 * @param parameterMap
	 * @return
	 */
	public List<Map<String, Object>> load_line_chart(Map<String, Object> parameterMap) {
		return mybatisDao.getList("yckycyzx_qgMapper.load_line_chart", parameterMap);
	}
	
	/**
	 * 数据表
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> load_table(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("yckycyzx_qgMapper.load_table", pager);
		return list;
	}
	/**
	 * 数据表导出
	 * @param request
	 * @param response
	 * @param parameterMap
	 * @throws Exception
	 */
	public void exportTable(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "营收款业财一致性_预存款业财数据一致性_全国.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,BOSS预存款总金额(元),ERP预收账款-用户预存款-期末余额(元),差异金额(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("yckycyzx_qgMapper.exportTable", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(df.format(map.get("sum_yjk_amt")))).append("\t,");
				sb.append(HelperString.objectConvertString(df.format(map.get("sum_qmye")))).append("\t,");
				sb.append(HelperString.objectConvertString(df.format(map.get("cyje")))).append("\t,");
				
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
		}
		out.flush();
		out.close();
	}
	
	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {

		// 这里设置一下让浏览器弹出下载提示框,而不是直接在浏览器中打开
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
		response.setContentType("application/octet-stream;charset=GBK");
	}

	
}
