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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.common.datasource.DataSourceName;
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;

/**
 * <pre>
 * 有价卡合规性 - 集中激活分析 （接口服务）
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
public class YJK2001Service {

    @Autowired
    private MybatisDao mybatisDao;
    /**
     * 获取数量波动趋势图
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-5 下午2:47:03
     * </pre>
     */
    public List<Map<String, Object>> getYJKNumberTrend(Map<String, Object> parameterMap) {
	if(parameterMap.get("trendType")!=null&&parameterMap.get("trendType").equals("single")){
	    return mybatisDao.getList("YJK2001.getYJKNumberTrend", parameterMap);
	}else{
	    return mybatisDao.getList("YJK2001SS.getYJKNumberTrend", parameterMap);
	}
    }
    /**
     * 获取有价卡平均激活数量
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-5 下午4:29:11
     * </pre>
     */
    public Map<String, Object> getYJKMAXNumber(Map<String, Object> parameterMap) {
	if(parameterMap.get("trendType")!=null&&parameterMap.get("trendType").equals("single")){
	    return mybatisDao.get("YJK2001.getYJKMAXNumber", parameterMap);
	}else{
	    return mybatisDao.get("YJK2001SS.getYJKMAXNumber", parameterMap);
	}
    }
    /**
     * 获取有价卡最大激活数量
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-5 下午4:29:11
     * </pre>
     */
    public Map<String, Object> getYJKAVGNumber(Map<String, Object> parameterMap) {
	if(parameterMap.get("trendType")!=null&&parameterMap.get("trendType").equals("single")){
	    return mybatisDao.get("YJK2001.getYJKAVGNumber", parameterMap);
	}else{
	    return mybatisDao.get("YJK2001SS.getYJKAVGNumber", parameterMap);
	}
    }
    
    /**
     * 获取有价卡激活数量前十地市
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-6 上午11:01:05
     * </pre>
     */
    public List<Map<String, Object>> getYJKTop10CityNumber(Map<String, Object> parameterMap) {
	if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("single")) {
	    return mybatisDao.getList("YJK2001.getYJKTop10CityNumber", parameterMap);
	} else {
	    return mybatisDao.getList("YJK2001SS.getYJKTop10CityNumber", parameterMap);
	}
    }
     
    /**
     * 获取有价卡激活数量占比前十地市
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-6 上午11:01:05
     * </pre>
     */
    public List<Map<String, Object>> getYJKTop10CityPer(Map<String, Object> parameterMap) {
	if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("single")) {
	    return mybatisDao.getList("YJK2001.getYJKTop10CityPer", parameterMap);
	} else {
	    return mybatisDao.getList("YJK2001SS.getYJKTop10CityPer", parameterMap);
	}
    }
    /**
     * 获取有价卡数量占比前十地市平均值
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-5 下午4:29:11
     * </pre>
     */
    public Map<String, Object> getYJKTop10CityAvgPer(Map<String, Object> parameterMap) {
	if(parameterMap.get("trendType")!=null&&parameterMap.get("trendType").equals("single")){
	    return mybatisDao.get("YJK2001.getYJKTop10CityAvgPer", parameterMap);
	}else{
	    return mybatisDao.get("YJK2001SS.getYJKTop10CityAvgPer", parameterMap);
	}
    }
    /**
     * 获取有价卡操作员前十明细信息
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-9 上午10:41:56
     * </pre>
     */
    public List<Map<String, Object>> getYJKCZYTop10Detail(Map<String, Object> parameterMap) {
	if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("single")) {
	    return mybatisDao.getList("YJK2001.getYJKCZYTop10Detail", parameterMap);
	} else {
	    return mybatisDao.getList("YJK2001SS.getYJKCZYTop10Detail", parameterMap);
	}
    }
    /**
     * 获取有价卡操作员明细信息
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-9 上午10:41:56
     * </pre>
     */
    public List<Map<String, Object>> getYJKCZYDetail(Pager pager) {
	if (pager.getParams().get("trendType") != null && pager.getParams().get("trendType").equals("single")) {
	    return mybatisDao.getList("YJK2001.getYJKCZYDetail", pager);
	} else {
	    return mybatisDao.getList("YJK2001SS.getYJKCZYDetail", pager);
	}
    }
    
    //<!-- 按操作员标识展示数据表 -->
    public List<Map<String, Object>> getYJKCZY_tableDetail(Pager pager) {
    	if (pager.getParams().get("trendType") != null && pager.getParams().get("trendType").equals("single")) {
    	    return mybatisDao.getList("YJK2001.getYJKCZY_tableDetail", pager);
    	} else {
    	    return mybatisDao.getList("YJK2001SS.getYJKCZY_tableDetail", pager);
    	}
        }
    /**
     * 导出有价卡操作员会汇总信息
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-9 下午4:35:35
     * </pre>
     */
    public List<Map<String, Object>> exportYJKCZYDetail(Map<String, Object> parameterMap) {
	if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("single")) {
	    return mybatisDao.getList("YJK2001.exportYJKCZYDetail", parameterMap);
	} else {
	    return mybatisDao.getList("YJK2001SS.exportYJKCZYDetail", parameterMap);
	}
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
        return mybatisDao.getList("YJK2001.getRankTrendList", parameterMap);
    }

    /**
     * 获取地市汇总列表
     * 
     * @param parameterMap
     * @return
     * @author Huang Tao
     * @refactor Huang Tao
     * @date 2016年11月23日 上午11:12:38
     */
    public List<Map<String, Object>> getCitySumList(Map<String, Object> parameterMap) {
        return mybatisDao.getList("YJK2001.getCitySumList", parameterMap);
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
    public List<Map<String, Object>> getCityDetailList(Map<String, Object> parameterMap) {
	if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("single")) {
	    return mybatisDao.getList("YJK2001.getCityDetailList", parameterMap);
	}else{
	    return mybatisDao.getList("YJK2001SS.getCityDetailList", parameterMap);
	}
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
        return mybatisDao.getList("YJK2001.getProvSumList", parameterMap);
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
        return mybatisDao.getList("YJK2001.getCitySumPagerList", pager);
    }

    /**
     * 获取地市明细列表（分页）
     * 
     * @param pager
     * @return
     * @author Huang Tao
     * @refactor Huang Tao
     * @date 2016年11月24日 下午3:19:29
     */
    @DataSourceName("dataSourceGBase")
    public List<Map<String, Object>> getCityDetailPagerList(Pager pager) {
	if (pager.getParams().get("trendType") != null && pager.getParams().get("trendType").equals("single")) {
	    return mybatisDao.getList("YJK2001.getCityDetailPagerList", pager);
	}else{
	    return mybatisDao.getList("YJK2001SS.getCityDetailPagerList", pager);
	}
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

        setFileDownloadHeader(request, response, HelperString.objectConvertString(parameterMap.get("exportFileName")));

        PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));

        StringBuffer sb = new StringBuffer();
        sb.append("审计月,地市名称,操作员标识,操作员姓名,批量激活有价卡数,批量激活有价卡金额（元）,赠送有价卡数量,赠送有价卡金额（元）");
        out.println(sb.toString());
		sb.delete(0, sb.length());
        for (Map<String, Object> map : dataList) {
            sb.append(hz_startMonth).append(" ~ ").append(hz_endMonth).append(",");
            sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append(",");
            sb.append(HelperString.objectConvertString(map.get("opr_id"))).append(",");
            sb.append(HelperString.objectConvertString(map.get("nm"))).append(",");
            sb.append(HelperString.objectConvertString(map.get("yjk_cnt"))).append(",");
            sb.append(HelperString.objectConvertString(map.get("cnt_tol"))).append(",");
            sb.append(HelperString.objectConvertString(map.get("zs_yjk_cnt"))).append(",");
            sb.append(HelperString.objectConvertString(map.get("zs_amt_sum"))).append("\n");
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
       
        
        setFileDownloadHeader(request, response, "4.1.4_有价卡合规性_批量激活分析_明细.csv");

        PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
        
        StringBuffer sb = new StringBuffer();
        sb.append("审计月,省代码,省份名称,地市代码,地市名称,激活日期,激活时段,操作员标识,操作员姓名,有价卡序列号,有价卡类型,有价卡状态,金额（元）,操作类型,操作流水号,有价卡赠送时间,获赠有价卡的用户标识,获赠有价卡的手机号,赠送月ARPU（元）,赠送前一月ARPU（元）,赠送前两月ARPU（元）,平均 月ARPU（元）,有价卡赠送涉及的营销案编号,营销案名称,营销案类型,是否对应营销案,被充值手机号码");
        out.println(sb.toString());
		sb.delete(0, sb.length());
        for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			 if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("single")) {
		        	dataList = mybatisDao.getList("YJK2001.getCityDetailList", parameterMap);
		    	}else{
		    		dataList = mybatisDao.getList("YJK2001SS.getCityDetailList", parameterMap);
		    	}
			if(dataList.size()==0){
				break;
			}
	        for (Map<String, Object> map : dataList) {
	            sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("opr_dt"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("opr_tm"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("opr_id"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("nm"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("yjk_ser_no"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("yjk_typ"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("yjk_stat"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("yjk_amt"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("opr_typ"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("opr_nbr"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("yjk_pres_dt"))).append(",");
	            sb.append(HelperString.objectConvertString(map.get("user_id"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("msisdn"))).append("\t,");
	            
	            
	            sb.append(HelperString.objectConvertString(map.get("send_mon_arpu"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("send_lastm_arpu"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("send_last2m_arpu"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("send_avg_arpu"))).append("\t,");
	            
	            sb.append(HelperString.objectConvertString(map.get("yjk_offer_cd"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("offer_nm"))).append("\t,");
	            sb.append(HelperString.objectConvertString(map.get("offer_cls"))).append("\t,");
	            sb.append(StringUtils.equals(HelperString.objectConvertString(map.get("is_yjk_offer_cd")), "Y") ? "是" : "否").append(",");
	            sb.append(HelperString.objectConvertString(map.get("cz_msisdn"))).append("\n");
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
