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

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.common.datasource.DataSourceName;
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;

/**
 * 
 * <pre>
 * Desc： 有价卡赠送未充值异常service
 * @author jh
 * @refactor jh
 * @date   2017-3-29 下午2:10:58
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-3-29 	   jh 	         1. Created this class. 
 * </pre>
 */
@Service
public class YJKWCZService extends BaseObject{
	
	private DecimalFormat df = new DecimalFormat("######0.00");

	@Autowired
    private MybatisDao mybatisDao;
	
	/**
	 * 
	 * <pre>
	 * Desc  赠送有价卡未充值比例异常 省环形图 service
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-3-30 下午8:04:45
	 * </pre>
	 */
	public List<Map<String,Object>> hz_yc_chart(Map<String, Object> params){
		return mybatisDao.getList("yjkwczMapper.hz_yc_chart", params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  赠送有价卡未充值比例异常 地市环形图service
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-3-30 下午8:06:16
	 * </pre>
	 */
	public List<Map<String,Object>> hz_yc_city_chart(Map<String, Object> params){
		return mybatisDao.getList("yjkwczMapper.hz_yc_city_chart", params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 省service
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 上午11:03:57
	 * </pre>
	 */
	public List<Map<String,Object>> hz_tj_chart(Map<String, Object> params){
		return mybatisDao.getList("yjkwczMapper.hz_tj_chart", params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 地市service
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 上午11:04:45
	 * </pre>
	 */
	public List<Map<String,Object>> hz_tj_city_chart(Map<String, Object> params){
		return mybatisDao.getList("yjkwczMapper.hz_tj_city_chart", params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 省结论service
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 下午1:13:04
	 * </pre>
	 */
	public List<Map<String,Object>> hz_tj_table_conclusion(Map<String, Object> params){
		return mybatisDao.getList("yjkwczMapper.hz_tj_table_conclusion", params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 地市结论service
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 下午1:13:47
	 * </pre>
	 */
	public List<Map<String,Object>> hz_tj_city_table_conclusion(Map<String, Object> params){
		return mybatisDao.getList("yjkwczMapper.hz_tj_city_table_conclusion", params);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 省数据表service
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 下午1:56:53
	 * </pre>
	 */
	public List<Map<String,Object>> hz_tj_table(Pager pager){
		return mybatisDao.getList("yjkwczMapper.hz_tj_table", pager);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 地市数据表service
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 下午1:13:47
	 * </pre>
	 */
	public List<Map<String,Object>> hz_tj_city_table(Pager pager){
		return mybatisDao.getList("yjkwczMapper.hz_tj_city_table", pager);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 导出省数据表service
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 下午1:56:53
	 * </pre>
	 */
	public void hz_tj_table_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = mybatisDao.getList("yjkwczMapper.hz_tj_table_export", params);
		setFileDownloadHeader(request, response, "有价卡赠送相关充值比例异常_赠送有价卡未充值比例异常_汇总.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("赠送起始月,赠送结束月,省代码,省名称,地市代码,地市名称,营销案编码,营销案名称,累计营销案赠送有价卡的数量,累计营销案赠送有价卡的金额(元),累计营销案赠送有价卡未充值的数量,累计营销案赠送有价卡未充值的金额(元),未充值金额比例(%)");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for (Map<String, Object> resultMap : charList) {
    		sb.append(HelperString.objectConvertString(resultMap.get("presBg"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("presEd"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("cmccProvPrvdId"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("shortName"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("cmccProvId"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("cmccPrvdNmShort"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("offerCd"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("offerNm"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("offerZsyjkNum"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("offerZsyjkAmt"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("offerZsyjkNopayNum"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("offerZsyjkNopayAmt"))).append("\t,");
    		sb.append(HelperString.objectConvertString(df.format(resultMap.get("offerAmtPer")))).append("\t");
    		out.println(sb.toString());
    		sb.delete(0, sb.length());
    	}
    	
    	out.flush();
    	out.close();
    }
	
	/**
	 * 
	 * <pre>
	 * Desc  营销案赠送有价卡未充值金额统计 导出地市数据表service
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 下午1:13:47
	 * </pre>
	 */
	public void hz_tj_city_table_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = mybatisDao.getList("yjkwczMapper.hz_tj_city_table_export", params);
		setFileDownloadHeader(request, response, "有价卡赠送相关充值比例异常_赠送有价卡未充值比例异常_汇总.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("赠送起始月,赠送结束月,省代码,省名称,地市代码,地市名称,营销案编码,营销案名称,累计营销案赠送有价卡的数量,累计营销案赠送有价卡的金额(元),累计营销案赠送有价卡未充值的数量,累计营销案赠送有价卡未充值的金额(元),未充值金额比例(%)");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for (Map<String, Object> resultMap : charList) {
    		sb.append(HelperString.objectConvertString(resultMap.get("presBg"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("presEd"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("cmccProvPrvdId"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("shortName"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("cmccProvId"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("cmccPrvdNmShort"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("offerCd"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("offerNm"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("offerZsyjkNum"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("offerZsyjkAmt"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("offerZsyjkNopayNum"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("offerZsyjkNopayAmt"))).append("\t,");
    		sb.append(HelperString.objectConvertString(df.format(resultMap.get("offerAmtPer")))).append("\t");
    		out.println(sb.toString());
    		sb.delete(0, sb.length());
    	}
    	
    	out.flush();
    	out.close();
    }
	
	/**
	 * 
	 * <pre>
	 * Desc  明细数据service
	 * @param pager
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-5 下午3:11:17
	 * </pre>
	 */
	@DataSourceName("dataSourceGBase")
	public List<Map<String,Object>> mx_table(Pager pager){
		return mybatisDao.getList("yjkwczMapper.mx_table", pager);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  明细 导出数据表service
	 * @param params
	 * @return
	 * @author jh
	 * @refactor jh
	 * @date   2017-4-1 下午1:56:53
	 * </pre>
	 */
	@DataSourceName("dataSourceGBase")
	public void mx_table_export(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> charList = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "有价卡赠送相关充值比例异常_赠送有价卡未充值比例异常_明细.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("审计月,赠送起始月,赠送结束月,省代码,省名称,地市代码,地市名称,有价卡序号,有价卡面额(元),有价卡当前状态,有价卡种类,有价卡有效期开始日期,有价卡有效期结束日期,有价卡赠送时间,获赠有价卡的用户标识,有价卡类型,获赠有价卡的手机号,有价卡赠送涉及的营销案编号,营销案名称,赠送渠道标识,赠送渠道名称,获赠有价卡到期时间");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for(int i=0;i>=0;i++){
			params.put("pageStar", 10000*i);
			params.put("pageEnd", 10000);
			charList = mybatisDao.getList("yjkwczMapper.mx_table_export", params);
			if(charList.size()==0){
				break;
			}
	    	for (Map<String, Object> resultMap : charList) {
	    		sb.append(HelperString.objectConvertString(resultMap.get("audTrm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("presBg"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("presEd"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("cmccProvPrvdId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("shortName"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("cmccProvId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("cmccPrvdNmShort"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("yjkSerNo"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("counTatal"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("cardFlag"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("supplyCardKind"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("FeeAccountStart"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("FeeAccountStop"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("yjkPresTm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("userId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("yjkTyp"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("msisdn"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("yjkOfferCd"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("offerNm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("corChnlId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("corChnlNm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("yjkEndDt"))).append("\t");
	    		out.println(sb.toString());
	    		sb.delete(0, sb.length());
	    	}
	    	charList.clear();
		}
    	out.flush();
    	out.close();
    }
	
	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {

		// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
		response.setContentType("application/octet-stream;charset=GBK");
	}
}