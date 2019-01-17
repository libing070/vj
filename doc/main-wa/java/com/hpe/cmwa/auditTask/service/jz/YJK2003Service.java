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
 * 有价卡合规性 - 重复充值分析 （接口服务）
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
public class YJK2003Service {

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
        return mybatisDao.getList("YJK2003.getRankTrendList", parameterMap);
    }
    /**
     * jielun
     * @param parameterMap
     * @return
     */
    
    public List<Map<String, Object>> getConclusionList(Map<String, Object> parameterMap) {
    	return mybatisDao.getList("YJK2003.getConclusionList", parameterMap);
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
        return mybatisDao.getList("YJK2003.getCitySumList", parameterMap);
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
        return mybatisDao.getList("YJK2003.getCityDetailList", parameterMap);
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
        return mybatisDao.getList("YJK2003.getProvSumList", parameterMap);
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
        return mybatisDao.getList("YJK2003.getCitySumPagerList", pager);
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
        return mybatisDao.getList("YJK2003.getCityDetailPagerList", pager);
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
        
        setFileDownloadHeader(request, response, "4.1.4_有价卡合规性_重复使用分析_汇总.csv");

        PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));

        StringBuffer sb = new StringBuffer();
        sb.append("审计区间,地市名称,违规有价卡数量,总数量,有价卡数量违规占比（%）");
        out.println(sb.toString());
		sb.delete(0, sb.length());
        for (Map<String, Object> map : dataList) {
            sb.append(hz_startMonth).append(" ~ ").append(hz_endMonth).append(",");
            sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append(",");
            sb.append(HelperString.objectConvertString(map.get("infraction_num"))).append(",");
            sb.append(HelperString.objectConvertString(map.get("tol_num"))).append(",");
            sb.append(HelperString.formatRoundUp(Double.valueOf(HelperString.objectConvertString(map.get("per_weigui_cnt"))))).append("\n");
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

        setFileDownloadHeader(request, response,"4.1.4_有价卡合规性_重复使用分析_明细.csv");

        PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));

        StringBuffer sb = new StringBuffer();
        sb.append("审计月,省份标识,地市标识,有价卡类型,违规类型,有价卡序列号,有价卡状态,有价卡金额（元）,涉及手机号码,有价卡类型名称,违规类型名称,省份名称,运营地市公司,地市名称,充值时间");
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
	            sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("yjk_typ"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("infraction_typ"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("yjk_no"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("yjk_stat"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("yjk_amt"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("pay_msisdn"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("yjk_typ_name"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("infraction_typ_name"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("short_name"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("cty_name"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("pay_time"))).append("\t\n");
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
