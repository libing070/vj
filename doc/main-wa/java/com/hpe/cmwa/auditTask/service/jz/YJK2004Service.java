/**
 * Copyright (c) 2016 xx Development Company, L.P.
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

import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.common.datasource.DataSourceName;
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;

/**
 * <pre>
 * 有价卡合规性 - 跨省使用分析 （接口服务）
 * 
 * @author   Huang Tao
 * @date     Nov 22, 2016 13:48:35 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version     Date             Author            Description
 * ------------------------------------------------------------------- 
 * 1.0        Nov 22, 2016     Huang Tao             1. Created this class. 
 * </pre>
 */
@Service
public class YJK2004Service {

    @Autowired
    private MybatisDao mybatisDao;

    /**
     * 获取省份字典列表
     * 
     * @param parameterMap
     * @return
     * @author Huang Tao
     * @refactor Huang Tao
     * @date 2016年11月23日 上午11:12:49
     */
    public List<Map<String, Object>> selectProvinceList(Map<String, Object> parameterMap) {
        return mybatisDao.getList("commonMapper.selectProvinceList", parameterMap);
    }

    /**
     * 排名趋势图
     * 
     * @param parameterMap
     * @return
     * @author Huang Tao
     * @refactor Huang Tao
     * @date 2016年11月23日 上午11:12:49
     */
    public List<Map<String, Object>> getRankTrendList(Map<String, Object> parameterMap) {
        return mybatisDao.getList("YJK2004.getRankTrendList", parameterMap);
    }
    /**
     * jielun
     * @param parameterMap
     * @return
     */
    
    public List<Map<String, Object>> getConclusionList(Map<String, Object> parameterMap) {
    	return mybatisDao.getList("YJK2004.getConclusionList", parameterMap);
    }

    /**
     * 全国排名
     * 
     * @param parameterMap
     * @return
     * @author Huang Tao
     * @refactor Huang Tao
     * @date 2016年11月23日 上午11:12:49
     */
    public List<Map<String, Object>> getRankProvSum(Map<String, Object> parameterMap) {
        return mybatisDao.getList("YJK2004.getRankProvSum", parameterMap);
    }

    /**
     * 获取地市明细列表
     * 
     * @param parameterMap
     * @return
     * @author Huang Tao
     * @refactor Huang Tao
     * @date 2016年11月23日 上午11:12:38
     */
    @DataSourceName("dataSourceGBase")
    public List<Map<String, Object>> getCityDetailList(Map<String, Object> parameterMap) {
        return mybatisDao.getList("YJK2004.getCityDetailList", parameterMap);
    }

    /**
     * 获取省汇总列表
     * 
     * @param parameterMap
     * @return
     * @author Huang Tao
     * @refactor Huang Tao
     * @date 2016年11月24日 下午3:15:33
     */
    public List<Map<String, Object>> getProvSumList(Map<String, Object> parameterMap) {
        return mybatisDao.getList("YJK2004.getProvSumList", parameterMap);
    }

    /**
     * 获取省汇总列表（分页）
     * 
     * @param pager
     * @return
     * @author Huang Tao
     * @refactor Huang Tao
     * @date 2016年11月24日 下午3:15:33
     */
    public List<Map<String, Object>> getProvSumPagerList(Pager pager) {
        return mybatisDao.getList("YJK2004.getProvSumPagerList", pager);
    }

    /**
     * 获取地市汇总列表（分页）
     * 
     * @param pager
     * @return
     * @author Huang Tao
     * @refactor Huang Tao
     * @date 2016年11月24日 下午3:19:29
     */
    public List<Map<String, Object>> getCitySumPagerList(Pager pager) {
        return mybatisDao.getList("YJK2004.getCitySumPagerList", pager);
    }

    /**
     * 获取地市明细列表（分页）（按照累计被充值手机号码排序）
     * 
     * @param pager
     * @return
     * @author Huang Tao
     * @refactor Huang Tao
     * @date 2016年11月24日 下午3:19:29
     */
    @DataSourceName("dataSourceGBase")
    public List<Map<String, Object>> getCityDetailPagerList(Pager pager) {
        return mybatisDao.getList("YJK2004.getCityDetailPagerList", pager);
    }

    /**
     * 导出汇总列表
     * 
     * @param parameterMap
     * @return
     * @author Huang Tao
     * @throws Exception 
     * @refactor Huang Tao
     * @date 2016年11月25日 下午3:33:49
     */
    public void exportSumList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
        String hz_startMonth = String.valueOf(parameterMap.get("hz_startMonth"));
        String hz_endMonth = String.valueOf(parameterMap.get("hz_endMonth"));
        
        List<Map<String, Object>> dataList = this.getProvSumList(parameterMap);

        setFileDownloadHeader(request, response, "4.1.4_有价卡合规性_跨省使用分析_汇总.csv");

        PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
        
        StringBuffer sb = new StringBuffer();
        sb.append("审计区间,归属省名称,归属省全部异地充值卡数量,充值省份名称,异地充值有价卡数量,异地充值有价卡金额（元）,异地充值有价卡数量比例（%）");
        out.println(sb.toString());
		sb.delete(0, sb.length());
        for (Map<String, Object> map : dataList) {
            sb.append(hz_startMonth).append(" ~ ").append(hz_endMonth).append(",");
            sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_name"))).append(",");
            sb.append(HelperString.objectConvertString(map.get("sum_tol_cn_prvd"))).append(",");
            sb.append(HelperString.objectConvertString(map.get("msisdn_prvd_name"))).append(",");
            sb.append(HelperString.objectConvertString(map.get("cnt"))).append(",");
            sb.append(HelperString.objectConvertString(map.get("tol_amt"))).append(",");
            sb.append(HelperString.formatRoundUp(Double.valueOf(HelperString.objectConvertString(map.get("tol_cn_prvd"))))).append("\n");
            out.write(sb.toString());
            sb.delete(0, sb.length());
        }

        out.flush();
        out.close();
    }

    /**
     * 导出明细列表
     * 
     * @param parameterMap
     * @return
     * @author Huang Tao
     * @throws Exception 
     * @refactor Huang Tao
     * @date 2016年11月25日 下午3:33:49
     */
    @DataSourceName("dataSourceGBase")
    public void exportDetailList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception {
        List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();

        setFileDownloadHeader(request, response, "4.1.4_有价卡合规性_跨省使用分析_明细.csv");

        PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));

        StringBuffer sb = new StringBuffer();
        sb.append("审计月,充值月份,充值省份代码,充值省份名称,充值手机号码,归属省代码,归属省名称,操作时间,有价卡序列号,有价卡类型,有价卡状态,金额（元）");
        out.println(sb.toString());
		sb.delete(0, sb.length());
        for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
		    dataList = this.getCityDetailList(parameterMap);
			if(dataList.size()==0){
				break;
			}
	        for (Map<String, Object> map : dataList) {
	            sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("mon"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("msisdn_prvd_id"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("msisdn_prvd_name"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("msisdn"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_name"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("tradedate"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("yjk_ser_no"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("supplycardkind_name"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("cardflag"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("countatal"))).append("\t\n");
	            out.write(sb.toString());
	            sb.delete(0, sb.length());
	        }
	        dataList.clear();
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
