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
 * 有价卡合规性 - 充值异常分析 （接口服务）
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
public class YJK2002Service {

    @Autowired
    private MybatisDao mybatisDao;

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
        return mybatisDao.getList("YJK2002.getRankTrendList", parameterMap);
    }

    /**
     * 获取地市汇总列表 （按照累计被充值手机号码排序）
     * 
     * @param parameterMap
     * @return
     * @author Huang Tao
     * @refactor Huang Tao
     * @date 2016年11月23日 上午11:12:38
     */
    public List<Map<String, Object>> getCitySumList(Map<String, Object> parameterMap) {
        return mybatisDao.getList("YJK2002.getCitySumList", parameterMap);
    }

    /**
     * 获取地市汇总列表 （按照异常卡数量排序）
     * 
     * @param parameterMap
     * @return
     * @author Huang Tao
     * @refactor Huang Tao
     * @date 2016年11月23日 上午11:12:38
     */
    public List<Map<String, Object>> getCitySumOrderByCntList(Map<String, Object> parameterMap) {
        return mybatisDao.getList("YJK2002.getCitySumOrderByCntList", parameterMap);
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
        return mybatisDao.getList("YJK2002.getCityDetailList", parameterMap);
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
        return mybatisDao.getList("YJK2002.getProvSumList", parameterMap);
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
        return mybatisDao.getList("YJK2002.getCitySumPagerList", pager);
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
        return mybatisDao.getList("YJK2002.getCityDetailPagerList", pager);
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
        
        List<Map<String, Object>> dataList = this.getCitySumList(parameterMap);
        
        setFileDownloadHeader(request, response, "4.1.4_有价卡合规性_充值集中度分析_汇总.csv");

        PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
        
        StringBuffer sb = new StringBuffer();
//        sb.append("审计月,地市名称,累计充值有价卡数量,累计充值有价卡金额（元）,异常充值数排名(说明:rank_num)");
        sb.append("审计区间,地市,累计充值有价卡数量,累计充值有价卡金额（元）,被充值手机号码数量");
        out.println(sb.toString());
		sb.delete(0, sb.length());
        for (Map<String, Object> map : dataList) {
            sb.append(hz_startMonth).append(" ~ ").append(hz_endMonth).append(",");
            sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append(",");
            sb.append(HelperString.objectConvertString(map.get("cnt"))).append(",");
            sb.append(HelperString.objectConvertString(map.get("amt_sum"))).append(",");
            sb.append(HelperString.objectConvertString(map.get("msisdn_count"))).append("\n");
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
        List<Map<String, Object>> dataList =  new ArrayList<Map<String,Object>>();

        setFileDownloadHeader(request, response, "4.1.4_有价卡合规性_充值集中度分析_明细.csv");

        PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));

        StringBuffer sb = new StringBuffer();
        sb.append("审计月,省代码,省代名称,地市代码,地市名称,充值月份,交易日期,交易时间,被充值手机号码,充值方式,有价卡序列号,有价卡类型,有价卡状态,金额（元）,有价卡赠送时间,获赠有价卡的用户标识,获赠有价卡的手机号,有价卡赠送涉及的营销案编号,营销案名称,营销案种类");
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
	            sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("short_name"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("mon"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("tradedate"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("tradetime"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("msisdn"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("tradetype"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("yjk_ser_no"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("cardflag"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("supplycardkind"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("countatal"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("yjk_pres_dt"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("user_id"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("hz_msisdn"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("yjk_offer_cd"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("offer_nm"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("offer_cls"))).append("\n");
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
