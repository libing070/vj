/**
 * com.hp.cmcc.service.sjzlService.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.common.CommonResult;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.util.Json;


/**
 * <pre>
 * Desc： 
 * @author zhangyic
 * @refactor zhangyic
 * @date   2016年4月26日 下午4:44:09
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2016年4月26日 	   zhangyic 	         1. Created this class. 
 * </pre>  
 */

@Repository
@Service
public class SJZLService {
	@Autowired
	private MybatisDao mybatisDao;
	
	public List<Map<String, Object>> getSJZLGenList() {
		List<Map<String, Object>> genList = mybatisDao.getList("sjzl.getSJZLGenList");
		return genList;
	}
	
	
	public List<Map<String, Object>> getAudTrmByNm(String audit_nm) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("audit_nm", audit_nm);
		List<Map<String, Object>> audTrmList = mybatisDao.getList("sjzl.getAudTrmByNm",paramMap);
		return audTrmList;
	}
	
	
	/**
	 * <pre>
	 * Desc  获取审计周期列表
	 * @return 
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月26日 下午4:47:36
	 * </pre>
	*/
	public List<Map<String, Object>> getAuditCycle() {
		List<Map<String, Object>> auditCycleList = mybatisDao.getList("sjzl.getAuditCycle");
		return auditCycleList;
	}
	
	/**
	 * <pre>
	 * Desc  根据专题和周期获取审计稽核点列表
	 * @param paramMap	包含审计周期、审计专题名称
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月26日 下午4:52:39
	 * </pre>
	*/
	public List<Map<String, Object>> getList(Map<String, Object> paramMap) {
		List<Map<String, Object>> auditList = mybatisDao.getList("sjzl.getAuditList", paramMap);
		/*判断是否为最新的版本*/
		if (auditList != null && auditList.size() > 0) {//这段代码迁移过来后保留但实际并没有用上
			for (int i = 0; i < auditList.size(); i++) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  
				Date date = new Date(); 
				String today = sdf.format(date);
				/*如果最近更新为今天,则是最新版本*/
				/*先判断是否有报告*/
				if ("1".equals(auditList.get(i).get("state"))) {
					if (today.equals(((String)auditList.get(i).get("latest_date")).substring(0, 10))) {
						auditList.get(i).put("is_new", "1");
					} else {
						auditList.get(i).put("is_new", "0");
					}
				} else {
					auditList.get(i).put("is_new", "null");
				}
			}
		}
		return auditList;
	}
	
	/**
	 * <pre>
	 * Desc  如果第一次下载,新增一条下载信息
	 * @param paramMap	包含审计周期、审计关注点名称
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月26日 下午4:56:00
	 * </pre>
	*/
	public void addDownloadInfo(Map<String, Object> paramMap) {
		mybatisDao.add("sjzl.addDownloadInfo", paramMap);
	}
	
	/**
	 * <pre>
	 * Desc  下载的时候,更新下载信息,下载数+1,下载时间为当前时间
	 * @param paramMap 包含审计周期、审计关注点名称
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月26日 下午4:59:51
	 * </pre>
	*/
	public void updateDownloadInfo(Map<String, Object> paramMap) {
		mybatisDao.update("sjzl.updateDownloadInfo", paramMap);
	}
	public void updateFileStatus(String audTrm,String focusCd,String audit_nm,String buildDownloadUrl,Boolean isTol){
		Map<String,Object> params=new HashMap<String,Object>();

		params.put("cycleDate", audTrm);
		params.put("nm_bm", audit_nm);
		params.put("focusCd", focusCd);
		params.put("buildDownloadUrl", buildDownloadUrl);
		if (!isTol) {
			mybatisDao.update("sjzl.updateFileStatus", params);
		} else {
			if (focusCd == "1000") params.put("nm_bm", "YJK_00");
			if (focusCd == "2000") params.put("nm_bm", "QDYK_00");
			if (focusCd == "3000") params.put("nm_bm", "ZDTL_00");
			Map<String,Integer> tolNum = mybatisDao.get("sjzl.getAuditTolNum", params);
			if (tolNum.get("tol") > 0) {
				mybatisDao.update("sjzl.updateFileStatus", params);
			}else{
				mybatisDao.add("sjzl.addAudTolInfo", params);				
			}
		}
		
		
		
	}
	
	public void updateStatus(String audTrm,Integer subjectId,Integer checkId,String checkCode,String checkName,String downloadUrl){
		
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		params.put("checkId", checkId);
		params.put("checkCode", checkCode);
		params.put("checkName", checkName);
		params.put("downloadUrl", downloadUrl);
		params.put("status", 1);
		
		mybatisDao.update("sjzl.updateAutoConf", params);
		Map<String,Integer> tolNum = mybatisDao.get("sjzl.getAutoInfoNum", params);
		if (tolNum.get("tol") > 0) {
			mybatisDao.update("sjzl.updateAutoInfo", params);
		}else{
			mybatisDao.add("sjzl.insertAutoInfo", params);				
		}
		
	
	}
	
	
	/**
	 * <pre>
	 * Desc  2.1有价卡违规（06049入库情况）
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 上午11:58:04
	 * </pre>
	*/
	public List<Map<String, Object>> getYJK01(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getYJK21", paramMap);
		return resultList;
	}

	/**
	 * <pre>
	 * Desc  2.2有价卡违规（06045入库情况）
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 上午11:58:31
	 * </pre>
	*/
	public List<Map<String, Object>> getYJK02(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getYJK22", paramMap);
		return resultList;
	}

	/**
	 * <pre>
	 * Desc  2.3有价卡违规（06049和06045关联比对）
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 上午11:58:52
	 * </pre>
	*/
	public List<Map<String, Object>> getYJK03(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getYJK23", paramMap);
		return resultList;
	}
	
	/**
	 * <pre>
	 * Desc  2.4有价卡金额异常
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 上午11:59:11
	 * </pre>
	*/
	public List<Map<String, Object>> getYJK04(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getYJK24", paramMap);
		return resultList;
	}
	
	/**
	 * <pre>
	 * Desc  2.5VC和CRM有价卡序列号相同但金额不同 
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 上午11:59:31
	 * </pre>
	*/
	public List<Map<String, Object>> getYJK05(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getYJK25", paramMap);
		return resultList;
	}
	
	/**
	 * <pre>
	 * Desc  2.6 CRM同种状态的有价卡重复数量
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 下午12:01:57
	 * </pre>
	*/
	public List<Map<String, Object>> getYJK06(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getYJK26", paramMap);
		return resultList;
	}
	
	/**
	 * <pre>
	 * Desc  2.7 VC同种状态的有价卡重复数量
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 下午12:02:12
	 * </pre>
	*/
	public List<Map<String, Object>> getYJK07(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getYJK27", paramMap);
		return resultList;
	}
	
	/**
	 * <pre>
	 * Desc  3.1终端套利（05004入库情况）
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 下午2:19:43
	 * </pre>
	*/
	public List<Map<String, Object>> getZDTL31(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getZDTL31", paramMap);
		return resultList;
	}
	
	/**
	 * <pre>
	 * Desc  3.2终端套利（05004和22064关联比对）
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 下午2:20:08
	 * </pre>
	*/
	public List<Map<String, Object>> getZDTL32(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getZDTL32", paramMap);
		return resultList;
	}
	
	/**
	 * <pre>
	 * Desc  3.3终端套利违规（22064和22108关联比对）
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 下午2:20:38
	 * </pre>
	*/
	public List<Map<String, Object>> getZDTL33(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getZDTL33", paramMap);
		return resultList;
	}
	
	/**
	 * <pre>
	 * Desc  3.4终端套利违规（22064与22062关联比对）
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 下午2:23:00
	 * </pre>
	*/
	public List<Map<String, Object>> getZDTL34(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getZDTL34", paramMap);
		return resultList;
	}
	
	/**
	 * <pre>
	 * Desc  3.5终端套利违规（22097与一级营销销售总数关联比对）
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 下午2:24:57
	 * </pre>
	*/
	public Map<String, List<Map<String, Object>>> getZDTL35(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList22097 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resultListYX = new ArrayList<Map<String, Object>>();
		resultList22097 = mybatisDao.getList("sjzl.getZDTL3522097", paramMap);
		resultListYX = mybatisDao.getList("sjzl.getZDTL35yingxiao", paramMap);
		Map<String, List<Map<String, Object>>> resultMap = new HashMap<String, List<Map<String, Object>>>();
		resultMap.put("resultList22097", resultList22097);
		resultMap.put("resultListYX", resultListYX);
		return resultMap;
	}
	
	/**
	 * <pre>
	 * Desc  3.6终端套利违规（22097与一级营销终端类型关联比对）
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 下午2:26:25
	 * </pre>
	*/
	public List<Map<String, Object>> getZDTL36(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getZDTL36", paramMap);
		return resultList;
	}
	
	
	public List<Map<String, Object>> getYKAll(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getYKAll", paramMap);
		return resultList;
	}
	/**
	 * <pre>
	 * Desc  1.1 用户历史信息表02004整体的数据情况
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 下午2:41:38
	 * </pre>
	*/
	public List<Map<String, Object>> getYKWhole(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getYKWhole", paramMap);
		return resultList;
	}
	
	/**
	 * <pre>
	 * Desc  1.2 用户类型的统计情况
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 下午2:42:11
	 * </pre>
	*/
	public List<Map<String, Object>> getYK12(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getYK12", paramMap);
		return resultList;
	}
	
	/**
	 * <pre>
	 * Desc  1.3 入网渠道异常变更
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 下午2:44:01
	 * </pre>
	*/
	public List<Map<String, Object>> getYK13(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getYK13", paramMap);
		return resultList;
	}
	
	/**
	 * <pre>
	 * Desc  1.4 入网时间的异常变更
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 下午2:44:25
	 * </pre>
	*/
	public List<Map<String, Object>> getYK14(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getYK14", paramMap);
		return resultList;
	}
	
	/**
	 * <pre>
	 * Desc  1.5 数据sim卡用户标识统计情况
	 * @param paramMap
	 * @return
	 * @author zhangyic
	 * @refactor zhangyic
	 * @date   2016年4月27日 下午2:44:55
	 * </pre>
	*/
	public List<Map<String, Object>> getYK15(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = mybatisDao.getList("sjzl.getYK15", paramMap);
		return resultList;
	}
	
	public List<String> getMonthByYear(String selYear) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("selYear", selYear);
		List<String> audMonList = mybatisDao.getList("sjzl.getMonthByYear", params);
		return audMonList;
	    }

	public List<String>  getYear() {
		List<String> auditYearList = mybatisDao.getList("sjzl.getYear");
		return auditYearList;
	    }

	public List<String>  getSJZLYear() {
		List<String> auditYearList = mybatisDao.getList("sjzl.getSJZLYear");
		return auditYearList;
	    }
	
	public List<String> getSJZLMonthByYear(String selYear) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("selYear", selYear);
		List<String> audMonList = mybatisDao.getList("sjzl.getSJZLMonthByYear", params);
		return audMonList;
	    }
}
