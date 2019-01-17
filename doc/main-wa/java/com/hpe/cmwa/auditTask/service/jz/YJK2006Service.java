package com.hpe.cmwa.auditTask.service.jz;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.dao.MybatisDao;
/***
 * 
 * <pre>
 * Desc： 有价卡赠送分析
 * @author   wangpeng
 * @refactor wangpeng
 * @date     2017-1-17 上午10:50:30
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-1-17 	   wangpeng 	         1. Created this class. 
 * </pre>
 */
@Service
public class YJK2006Service {
    
    @Autowired
    private MybatisDao mybatisDao;
    /**
     * 获取有价卡真实性信息
     * <pre>
     * Desc  
     * @param params
     * @return
     * @author wangpeng
     * 2017-1-17 下午2:48:56
     * </pre>
     */
    public List<Map<String,Object>> getYJKRealInfo(Map<String,Object> parameterMap){
	if(parameterMap.get("trendType")!=null&&parameterMap.get("trendType").equals("boss")){
	    return mybatisDao.getList("YJK2006.getYJKRealInfo", parameterMap);
	}else{
	    return mybatisDao.getList("YJK2006MANU.getYJKRealInfo", parameterMap);
	}
    }
    /**
     * 获取赠送集中度分析
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-17 下午7:16:50
     * </pre>
     */
    public List<Map<String, Object>> getYJKFocus(Map<String, Object> parameterMap) {
	if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("boss")) {
	    return mybatisDao.getList("YJK2006.getYJKFocus", parameterMap);
	} else {
	    return mybatisDao.getList("YJK2006MANU.getYJKFocus", parameterMap);
	}
    }
    /**
     * 获取赠送集中度分析明細信息
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-17 下午7:59:28
     * </pre>
     */
    public List<Map<String, Object>> getYJKFocusInfo(Pager pager) {
	if (pager.getParams().get("trendType") != null && pager.getParams().get("trendType").equals("boss")) {
	    return mybatisDao.getList("YJK2006.getYJKFocusInfo", pager);
	} else {
	    return mybatisDao.getList("YJK2006MANU.getYJKFocusInfo", pager);
	}
    }
    /**
     * 导出赠送集中度分析明細信息
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-18 上午11:11:41
     * </pre>
     */
    public List<Map<String, Object>> getYJKFocusInfoAll(Map parameterMap) {
	if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("boss")) {
	    return mybatisDao.getList("YJK2006.getYJKFocusInfoAll", parameterMap);
	} else {
	    return mybatisDao.getList("YJK2006MANU.getYJKFocusInfoAll", parameterMap);
	}
    }
    
    /**
     * 获取赠送非中高非集团客户 的有价卡金额
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-18 上午11:11:41
     * </pre>
     */
    public Map<String, Object> getZsfzgfjtCustomerCon(Map parameterMap) {
	if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("boss")) {
	    return mybatisDao.get("YJK2006.getZsfzgfjtCustomerCon", parameterMap);
	} else {
	    return mybatisDao.get("YJK2006MANU.getZsfzgfjtCustomerCon", parameterMap);
	}
    }
    
    /**
     * 获取赠送非中高非集团客户 的有价卡金额前5
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-18 上午11:11:41
     * </pre>
     */
    public List<Map<String, Object>> getZsfzgfjtCustomer(Map parameterMap) {
	if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("boss")) {
	    return mybatisDao.getList("YJK2006.getZsfzgfjtCustomer", parameterMap);
	} else {
	    return mybatisDao.getList("YJK2006MANU.getZsfzgfjtCustomer", parameterMap);
	}
    }
    /**
     * 获取赠送非中高非集团客户 的有价卡明细
     * <pre>
     * Desc  
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-18 下午3:20:00
     * </pre>
     */
    public List<Map<String, Object>> getZsfzgfjtCustomerInfo(Pager pager){
	if (pager.getParams().get("trendType") != null && pager.getParams().get("trendType").equals("boss")) {
	    return mybatisDao.getList("YJK2006.getZsfzgfjtCustomerInfo", pager);
	} else {
	    return mybatisDao.getList("YJK2006MANU.getZsfzgfjtCustomerInfo", pager);
	}
    }
    /**
     * 导出数据
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-18 下午3:43:48
     * </pre>
     */
    public List<Map<String, Object>> getZsfzgfjtCustomerExport(Map parameterMap) {
	if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("boss")) {
	    return mybatisDao.getList("YJK2006.getZsfzgfjtCustomerExport", parameterMap);
	} else {
	    return mybatisDao.getList("YJK2006MANU.getZsfzgfjtCustomerExport", parameterMap);
	}
    }

    /**
     * 获取赠送用途分析汇总信息
     * 
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-18 下午4:12:50
     * </pre>
     */
    public List<Map<String, Object>> getSumPurpose(Map parameterMap) {
	if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("boss")) {
	    return mybatisDao.getList("YJK2006.getSumPurpose", parameterMap);
	} else {
	    return mybatisDao.getList("YJK2006MANU.getSumPurpose", parameterMap);
	}
    }
    /**
     * 获取赠送员工汇总分析
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-18 下午6:44:42
     * </pre>
     */
    public Map<String, Object> getSumStaff(Map parameterMap) {
	if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("boss")) {
	    return mybatisDao.get("YJK2006.getSumStaff", parameterMap);
	} else {
	    return mybatisDao.get("YJK2006MANU.getSumStaff", parameterMap);
	}
    }
    /**
     * 获取赠送员工明细信息
     * <pre>
     * Desc  
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-18 下午7:57:35
     * </pre>
     */
    public List<Map<String, Object>> getStaffInfo(Pager pager) {
	if (pager.getParams().get("trendType") != null && pager.getParams().get("trendType").equals("boss")) {
	    return mybatisDao.getList("YJK2006.getStaffInfo", pager);
	} else {
	    return mybatisDao.getList("YJK2006MANU.getStaffInfo", pager);
	}
    }

    /**
     * 查询所有赠送员工信息
     * 
     * <pre>
     * Desc  
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-18 下午8:11:34
     * </pre>
     */
    public List<Map<String, Object>> getStaffInfoAll(Map parameterMap) {
	if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("boss")) {
	    return mybatisDao.getList("YJK2006.getStaffInfoAll", parameterMap);
	} else {
	    return mybatisDao.getList("YJK2006MANU.getStaffInfoAll", parameterMap);
	}
    }
    /**
     * 获取赠送金额汇总
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-19 上午10:34:46
     * </pre>
     */
    public Map<String, Object> getSumMoney(Map parameterMap) {
	if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("boss")) {
	    return mybatisDao.get("YJK2006.getSumMoney", parameterMap);
	} else {
	    return mybatisDao.get("YJK2006MANU.getSumMoney", parameterMap);
	}
    }

    /**
     * 获取ods金额汇总小于201406月
     * 
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-19 上午10:35:25
     * </pre>
     */
    public Map<String, Object> getSumOdsMoneyMin(Map<String, Object>  parameterMap) {
	return mybatisDao.get("YJK2006.getSumOdsMoneyMin", parameterMap);
    }
    /**
     * 获取ods金额汇总大于201406月
     * 
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-19 上午10:35:25
     * </pre>
     */
    public Map<String, Object> getSumOdsMoneyMax(Map parameterMap) {
	return mybatisDao.get("YJK2006.getSumOdsMoneyMax", parameterMap);
    }
    /**
     * 获取非中高非集团客户大明细
     * <pre>
     * Desc  
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-19 下午1:56:46
     * </pre>
     */
    public List<Map<String, Object>> getFzgFjtDetail(Pager pager) {
	if (pager.getParams().get("trendType") != null && pager.getParams().get("trendType").equals("boss")) {
	    return mybatisDao.getList("YJK2006.getFzgFjtDetail", pager);
	} else {
	    return mybatisDao.getList("YJK2006MANU.getFzgFjtDetail", pager);
	}
    }
    /**
     * 导出明细数据
     * <pre>
     * Desc  
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-19 下午2:57:43
     * </pre>
     */
    public List<Map<String, Object>> getFzgFjtDetailAll(Map<String, Object>  parameterMap) {
	if (parameterMap.get("trendType") != null && parameterMap.get("trendType").equals("boss")) {
	    return mybatisDao.getList("YJK2006.getFzgFjtDetailAll", parameterMap);
	} else {
	    return mybatisDao.getList("YJK2006MANU.getFzgFjtDetailAll", parameterMap);
	}
    }
    
    
    
}