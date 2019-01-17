/**
 * com.hpe.cmwa.auditTask.service.jz.TFSRHGXService.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
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

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.common.datasource.DataSourceName;
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;


/**
 * <pre>
 * Desc： 
 * @author   peter.fu
 * @refactor peter.fu
 * @date     Jan 15, 2017 8:10:01 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Jan 15, 2017 	   peter.fu 	         1. Created this class. 
 * </pre>  
 */
@Service
public class ZDBTWGFF1602Service  extends BaseObject{

    @Autowired
    private MybatisDao mybatisDao;
    
    /**
     * 全国各省向非4G定制终端发放补贴金额排名chart
     * @param params
     * @return
     */
    public List<Map<String, Object>> hz_qgpm_tongji_chart(Map<String, Object> params) {
		return mybatisDao.getList("zdbtwgff1602Mapper.hz_qgpm_tongji_chart", params);
	}
    /**
     * 全国各省向非4G定制终端发放补贴金额排名  tab
     * @param pager
     * @return
     */
    public List<Map<String, Object>> hz_qgpm_tongji_table(Pager pager) {
		return mybatisDao.getList("zdbtwgff1602Mapper.hz_qgpm_tongji_table", pager);
	}
    
    /**
     * 按照 金额 从高到低排序，展示该省前三名的渠道数据
     * @param params
     * @return
     */
    public List<Map<String, Object>> getThreeChnlNm(Map<String, Object> params) {
    	return mybatisDao.getList("zdbtwgff1602Mapper.getThreeChnlNm", params);
    }
    
    /**
     * 按照 金额 从高到低排序，展示地市前三名的渠道数据
     * @param params
     * @return
     */
    public List<Map<String, Object>> getCityThreeChnlNm(Map<String, Object> params) {
    	return mybatisDao.getList("zdbtwgff1602Mapper.getCityThreeChnlNm", params);
    }
    
    /**
     * 非4G定制终端发放补贴金额地市分布  tab
     * @param pager
     * @return
     */
    public List<Map<String, Object>> load_hz_dqfb_sjb_table(Pager pager) {
    	List<Map<String, Object>> charList = mybatisDao.getList("zdbtwgff1602Mapper.hz_dqfb_sjb_table", pager);
    	Map<String, Object> params = pager.getParams();
    	List<Map<String,Object>> chnlList = null;
    	List<Map<String, Object>> resultList =  new ArrayList<Map<String,Object>>();
    	for(Map<String, Object> map : charList){
    		params.put("cmccProvId", map.get("cmccProvId"));
    		chnlList = this.getCityThreeChnlNm(params);
    		if(chnlList.size()==1){
    			map.put("tolAllowCost1", chnlList.get(0).get("tolAllowCost"));
    			map.put("trmnlNum1", chnlList.get(0).get("trmnlNum"));
    			map.put("topOneName", chnlList.get(0).get("chnlNm"));
    			map.put("trmnlNum1", chnlList.get(0).get("trmnlNum"));
    			map.put("tolAllowCost2", 0);
    			map.put("trmnlNum2",0);
    			map.put("tolAllowCost3", 0);
    			map.put("trmnlNum3",0);
    		}
    		if(chnlList.size()==2){
    			map.put("tolAllowCost1", chnlList.get(0).get("tolAllowCost"));
    			map.put("trmnlNum1", chnlList.get(0).get("trmnlNum"));
    			map.put("topOneName", chnlList.get(0).get("chnlNm"));
	    		map.put("tolAllowCost2", chnlList.get(1).get("tolAllowCost"));
	    		map.put("trmnlNum2", chnlList.get(1).get("trmnlNum"));
	    		map.put("topTwoName", chnlList.get(1).get("chnlNm"));
	    		map.put("tolAllowCost3", 0);
	    		map.put("trmnlNum3", 0);
    		}
    		if(chnlList.size()==3){
    			map.put("tolAllowCost1", chnlList.get(0).get("tolAllowCost"));
    			map.put("trmnlNum1", chnlList.get(0).get("trmnlNum"));
    			map.put("topOneName", chnlList.get(0).get("chnlNm"));
    			map.put("tolAllowCost2", chnlList.get(1).get("tolAllowCost"));
    			map.put("trmnlNum2", chnlList.get(1).get("trmnlNum"));
    			map.put("topTwoName", chnlList.get(1).get("chnlNm"));
	    		map.put("tolAllowCost3", chnlList.get(2).get("tolAllowCost"));
	    		map.put("trmnlNum3", chnlList.get(2).get("trmnlNum"));
	    		map.put("topThreeName", chnlList.get(2).get("chnlNm"));
    		}
    		resultList.add(map);
    	}
    	return resultList;
    }
    
    /**
     * 非4G定制终端发放补贴金额地市分布  chart
     * @param params
     * @return
     */
    public List<Map<String, Object>> hz_dqfb_tongji_chart(Map<String, Object> params) {
    	List<Map<String, Object>> charList = mybatisDao.getList("zdbtwgff1602Mapper.hz_dqfb_tongji_chart", params);
    	List<Map<String,Object>> chnlList = null;
    	List<Map<String, Object>> resultList =  new ArrayList<Map<String,Object>>();
    	for(Map<String, Object> map : charList){
    		params.put("cmccProvId", map.get("cmccProvId"));
    		chnlList = this.getCityThreeChnlNm(params);
    		if(chnlList.size()==1){
    			map.put("tolAllowCost1", chnlList.get(0).get("tolAllowCost"));
    			map.put("trmnlNum1", chnlList.get(0).get("trmnlNum"));
    			map.put("topOneName", chnlList.get(0).get("chnlNm"));
    			map.put("trmnlNum1", chnlList.get(0).get("trmnlNum"));
    			map.put("tolAllowCost2", 0);
    			map.put("trmnlNum2",0);
    			map.put("tolAllowCost3", 0);
    			map.put("trmnlNum3",0);
    		}
    		if(chnlList.size()==2){
    			map.put("tolAllowCost1", chnlList.get(0).get("tolAllowCost"));
    			map.put("trmnlNum1", chnlList.get(0).get("trmnlNum"));
    			map.put("topOneName", chnlList.get(0).get("chnlNm"));
	    		map.put("tolAllowCost2", chnlList.get(1).get("tolAllowCost"));
	    		map.put("trmnlNum2", chnlList.get(1).get("trmnlNum"));
	    		map.put("topTwoName", chnlList.get(1).get("chnlNm"));
	    		map.put("tolAllowCost3", 0);
	    		map.put("trmnlNum3", 0);
    		}
    		if(chnlList.size()==3){
    			map.put("tolAllowCost1", chnlList.get(0).get("tolAllowCost"));
    			map.put("trmnlNum1", chnlList.get(0).get("trmnlNum"));
    			map.put("topOneName", chnlList.get(0).get("chnlNm"));
    			map.put("tolAllowCost2", chnlList.get(1).get("tolAllowCost"));
    			map.put("trmnlNum2", chnlList.get(1).get("trmnlNum"));
    			map.put("topTwoName", chnlList.get(1).get("chnlNm"));
	    		map.put("tolAllowCost3", chnlList.get(2).get("tolAllowCost"));
	    		map.put("trmnlNum3", chnlList.get(2).get("trmnlNum"));
	    		map.put("topThreeName", chnlList.get(2).get("chnlNm"));
    		}
    		resultList.add(map);
    	}
    	return resultList;
    }
    
    /**
     * 统付收入统计 tab 导出
     * @param request
     * @param response
     * @param parameterMap
     * @throws Exception
     */
    public void export_hz_qgpm_sjb(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
		List<Map<String, Object>> list = mybatisDao.getList("zdbtwgff1602Mapper.hz_qgpm_tongji_chart", parameterMap);
		String starEndTime = parameterMap.get("currSumBeginDate").toString()+"-"+ parameterMap.get("currSumEndDate");
		setFileDownloadHeader(request, response, "4.4.1_终端补贴违规发放_违规对非4G定制终端发放补贴_汇总_省.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));

		StringBuffer sb = new StringBuffer();
		sb.append("审计区间,省名称,向非4G定制终端发放补贴金额(元),违规补贴非4G定制终端终端数量");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		for (Map<String, Object> map : list) {
			sb.append(HelperString.objectConvertString(starEndTime)).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("shortName"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("tolAllowCost"))).append("\t,");
			sb.append(HelperString.objectConvertString(map.get("trmnlNum"))).append("\t");
			out.println(sb.toString());
			sb.delete(0, sb.length());
		}

		out.flush();
		out.close();
	}
    /**
     * 统付收入统计 tab 导出
     * @param request
     * @param response
     * @param parameterMap
     * @throws Exception
     */
    public void export_hz_dqfb_sjb(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
    	//List<Map<String, Object>> list = mybatisDao.getList("zdbtwgff1602Mapper.hz_dqfb_sjb_export", parameterMap);
    	List<Map<String, Object>> charList = mybatisDao.getList("zdbtwgff1602Mapper.hz_dqfb_sjb_export", parameterMap);
    	List<Map<String,Object>> chnlList = null;
    	List<Map<String, Object>> resultList =  new ArrayList<Map<String,Object>>();
    	for(Map<String, Object> map : charList){
    		parameterMap.put("cmccProvId", map.get("cmccProvId"));
    		chnlList = this.getCityThreeChnlNm(parameterMap);
    		if(chnlList.size()==1){
    			map.put("tolAllowCost1", chnlList.get(0).get("tolAllowCost"));
    			map.put("trmnlNum1", chnlList.get(0).get("trmnlNum"));
    			map.put("topOneName", chnlList.get(0).get("chnlNm"));
    			map.put("trmnlNum1", chnlList.get(0).get("trmnlNum"));
    			map.put("tolAllowCost2", 0);
    			map.put("trmnlNum2",0);
    			map.put("tolAllowCost3", 0);
    			map.put("trmnlNum3",0);
    		}
    		if(chnlList.size()==2){
    			map.put("tolAllowCost1", chnlList.get(0).get("tolAllowCost"));
    			map.put("trmnlNum1", chnlList.get(0).get("trmnlNum"));
    			map.put("topOneName", chnlList.get(0).get("chnlNm"));
	    		map.put("tolAllowCost2", chnlList.get(1).get("tolAllowCost"));
	    		map.put("trmnlNum2", chnlList.get(1).get("trmnlNum"));
	    		map.put("topTwoName", chnlList.get(1).get("chnlNm"));
	    		map.put("tolAllowCost3", 0);
	    		map.put("trmnlNum3", 0);
    		}
    		if(chnlList.size()==3){
    			map.put("tolAllowCost1", chnlList.get(0).get("tolAllowCost"));
    			map.put("trmnlNum1", chnlList.get(0).get("trmnlNum"));
    			map.put("topOneName", chnlList.get(0).get("chnlNm"));
    			map.put("tolAllowCost2", chnlList.get(1).get("tolAllowCost"));
    			map.put("trmnlNum2", chnlList.get(1).get("trmnlNum"));
    			map.put("topTwoName", chnlList.get(1).get("chnlNm"));
	    		map.put("tolAllowCost3", chnlList.get(2).get("tolAllowCost"));
	    		map.put("trmnlNum3", chnlList.get(2).get("trmnlNum"));
	    		map.put("topThreeName", chnlList.get(2).get("chnlNm"));
    		}
    		resultList.add(map);
    	}
    	String starEndTime = parameterMap.get("currSumBeginDate").toString()+"-"+ parameterMap.get("currSumEndDate");
    	setFileDownloadHeader(request, response, "4.4.1_终端补贴违规发放_违规对非4G定制终端发放补贴_汇总_地市.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("审计区间,地市名称,向非4G定制终端发放补贴金额(元),违规补贴非4G定制终端数量,Top1渠道名称,Top1渠道向非4G定制终端发放补贴金额(元),Top1渠道违规补贴非4G定制终端数量,Top2渠道名称,Top2渠道向非4G定制终端发放补贴金额(元),Top2渠道违规补贴非4G定制终端数量,Top3渠道名称,Top3渠道向非4G定制终端发放补贴金额(元),Top3渠道违规补贴非4G定制终端数量");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for (Map<String, Object> resultMap : resultList) {
    		sb.append(HelperString.objectConvertString(starEndTime)).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("cmccPrvdNmShort"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("tolAllowCost"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("trmnlNum"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("topOneName"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("tolAllowCost1"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("trmnlNum1"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("topTwoName"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("tolAllowCost2"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("trmnlNum2"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("topThreeName"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("tolAllowCost3"))).append("\t,");
    		sb.append(HelperString.objectConvertString(resultMap.get("trmnlNum3"))).append("\t");
    		out.println(sb.toString());
    		sb.delete(0, sb.length());
    	}
    	
    	out.flush();
    	out.close();
    }
    
    
    /**
     * 明细 数据表格
     * @param pager
     * @return
     */
    @DataSourceName("dataSourceGBase")
    public List<Map<String, Object>> mx_table(Pager pager) {
    	return mybatisDao.getList("zdbtwgff1602Mapper.mx_table", pager);
    }
    /**
     * <pre>
     * Desc  明细-表格-导出
     * @param request
     * @return
     * @author peter.fu
     * Jan 15, 2017 10:21:04 PM
     * </pre>
     */
    @DataSourceName("dataSourceGBase")
    public void mx_export_btn(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	
    	setFileDownloadHeader(request, response, "4.4.1_终端补贴违规发放_违规对非4G定制终端发放补贴_明细.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("审计月,省代码,省名称,地市代码,地市名称,渠道标识,渠道名称,用户标识,终端IMEI,销售时间,实际购机金额(元),积分兑换值,使用积分兑换金额(元),采购成本(元),裸机零售价(元),终端补贴成本(元),话费补贴成本(元),客户预存话费(元),客户实缴费用总额(元),客户承诺月最低消费(元),客户捆绑合约类型,产品类型名称");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("zdbtwgff1602Mapper.mx_export_btn", parameterMap);
			if(list.size()==0){
				break;
			}
	    	for (Map<String, Object> map : list) {
	    		sb.append(HelperString.objectConvertString(map.get("audTrm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("cmccProvPrvdId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("shortName"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("cmccPrvdId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("cmccPrvdNmShort"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("chnlId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("chnlNm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("userId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("IMEI"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("sellDat"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("actlShopAMT"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("acumExchVal"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("usdAcumExchAmt"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("shopCost"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("unlockedRetlPrc"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("trnmlAllowCost"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("feeAllowCost"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("custPpayFee"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("custActlFeeSum"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("custPrmsMonLeastConsm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("CustBndContrtTyp"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(map.get("prodTypNm"))).append("\t");
	    		out.println(sb.toString());
	    		sb.delete(0, sb.length());
	    	}
	    	list.clear();
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
