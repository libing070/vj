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

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.common.datasource.DataSourceName;
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;

/**
 * 
 * <pre>
 * Desc： 社会渠道酬金异常波动
 * @author jh
 * @refactor jh
 * @date   2017-4-10 上午11:15:33
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-4-10 	   jh 	         1. Created this class. 
 * </pre>
 */
@Service
public class SHQDService extends BaseObject{
	
	@Autowired
	private MybatisDao mybatisDao;
	
	/**
	 * 
	 * <pre>
	 * Desc  社会渠道酬金环比波动区间分布图chart SERVICE
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 上午11:04:41
	 * </pre>
	 */
	public List<Map<String,Object>> hb_chart(Map<String, Object> params){
		return mybatisDao.getList("shqdMapper.hb_chart", params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  社会渠道酬金环比波动区间数据表table SERVICE
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 下午2:31:29
	 * </pre>
	 */
	public List<Map<String,Object>> hb_table(Pager pager){
		return mybatisDao.getList("shqdMapper.hb_table", pager);
	}
	
	
	/**
	 * 
	 * <pre>
	 * Desc  社会渠道酬金方差波动区间分布chart SERVICE
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 上午11:04:41
	 * </pre>
	 */
	public List<Map<String,Object>> fc_chart(Map<String, Object> params){
		return mybatisDao.getList("shqdMapper.fc_chart", params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  社会渠道酬金方差波动区间分布table SERVICE
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 下午2:31:29
	 * </pre>
	 */
	public List<Map<String,Object>> fc_table(Pager pager){
		return mybatisDao.getList("shqdMapper.fc_table", pager);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  酬金环比排名前五的渠道
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 下午4:28:38
	 * </pre>
	 */
	public List<Map<String,Object>> five_qd(Map<String, Object> params){
		return mybatisDao.getList("shqdMapper.five_qd", params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  酬金环比排名前五的渠道统计图chart SERVICE
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 下午4:29:39
	 * </pre>
	 */
	public List<Map<String,Object>> five_qd_chart(Map<String, Object> params){
		return mybatisDao.getList("shqdMapper.five_qd_chart", params);
	}
	
	/**
	 * 酬金环比排名前5的社会渠道table SERVICE
	 * <pre>
	 * Desc  
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-12 下午2:38:51
	 * </pre>
	 */
	public List<Map<String,Object>> five_table(Pager pager){
		return mybatisDao.getList("shqdMapper.five_table", pager);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  酬金环比排名前5的社会渠道table导出 SERVICE
	 * @param request
	 * @param response
	 * @param params
	 * @throws Exception
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-12 下午2:39:35
	 * </pre>
	 */
	public void five_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = mybatisDao.getList("shqdMapper.five_export", params);
		setFileDownloadHeader(request, response, "社会渠道服务费异常_社会渠道酬金异常波动_汇总_环比排名.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("审计起始月,审计结束月,省名称,地市名称,社会渠道标识,社会渠道名称,月份,酬金金额（元）,环比(%)");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for (Map<String, Object> resultMap : charList) {
    		sb.append(HelperString.objectConvertString(resultMap.get("audTrmBegin"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("audTrmEnd"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("shortName"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("cmccPrvdNmShort"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("socChnlId"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("socChnlNm"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("mon"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("rwdAmt"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("relaRat"))).append("\t");
    		out.println(sb.toString());
    		sb.delete(0, sb.length());
    	}
    	
    	out.flush();
    	out.close();
    }
	
	/**
	 * 
	 * <pre>
	 * Desc 酬金方差排名前10的社会渠道
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 下午4:28:38
	 * </pre>
	 */
	public List<Map<String,Object>> ten_qd(Map<String, Object> params){
		return mybatisDao.getList("shqdMapper.ten_qd", params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  酬金方差排名前10的社会渠道chart SERVICE
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-11 下午4:29:39
	 * </pre>
	 */
	public List<Map<String,Object>> ten_qd_chart(Map<String, Object> params){
		return mybatisDao.getList("shqdMapper.ten_qd_chart", params);
	}
	
	
	/**
	 * 酬金方差排名前10的社会渠道table SERVICE
	 * <pre>
	 * Desc  
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-12 下午2:38:51
	 * </pre>
	 */
	public List<Map<String,Object>> ten_table(Pager pager){
		return mybatisDao.getList("shqdMapper.ten_table", pager);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  酬金方差排名前10的社会渠道table导出 SERVICE
	 * @param request
	 * @param response
	 * @param params
	 * @throws Exception
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-12 下午2:39:35
	 * </pre>
	 */
	public void ten_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = mybatisDao.getList("shqdMapper.ten_export", params);
		setFileDownloadHeader(request, response, "社会渠道服务费异常_社会渠道酬金异常波动_汇总_方差波动排名.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("审计起始月,审计结束月,省名称,地市名称,社会渠道标识,社会渠道名称,月份,酬金金额（元）,方差波动");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for (Map<String, Object> resultMap : charList) {
    		sb.append(HelperString.objectConvertString(resultMap.get("audTrmBegin"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("audTrmEnd"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("shortName"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("cmccPrvdNmShort"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("socChnlId"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("socChnlNm"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("mon"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("rwdAmt"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("varWave"))).append("\t");
    		out.println(sb.toString());
    		sb.delete(0, sb.length());
    	}
    	
    	out.flush();
    	out.close();
    }
	
	/**
	 * 
	 * <pre>
	 * Desc  明细数据table
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-13 下午12:28:43
	 * </pre>
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String,Object>> mx_table(Pager pager){
		return mybatisDao.getList("shqdMapper.mx_table", pager);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  明细table导出 SERVICE
	 * @param request
	 * @param response
	 * @param params
	 * @throws Exception
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-12 下午2:39:35
	 * </pre>
	 */
	@DataSourceName("dataSourceGBase")
	public void mx_table_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "社会渠道服务费异常_社会渠道酬金异常波动_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("审计起始月,审计结束月,省代码,省名称,地市代码,地市名称,月份,社会渠道标识,社会渠道名称,渠道基础类型,销售费用科目编码,销售费用科目,酬金金额(元),酬金结算月份");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for(int i=0;i>=0;i++){
			params.put("pageStar", 10000*i);
			params.put("pageEnd", 10000);
			charList = mybatisDao.getList("shqdMapper.mx_table_export", params);
			if(charList.size()==0){
				break;
			}
			for (Map<String, Object> resultMap : charList) {
				sb.append(HelperString.objectConvertString(resultMap.get("audTrmBegin"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("audTrmEnd"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("cmccProvPrvdId"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("shortName"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("cmccProvId"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("cmccPrvdNmShort"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("mon"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("socChnlId"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("socChnlNm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("chnlBasicTyp"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("sellFeeSubj"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("sellFeeSubjNm"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("rwdAmt"))).append("\t,");
				sb.append(HelperString.objectConvertString(resultMap.get("rwdStlmntMon"))).append("\t");
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			charList.clear();
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
