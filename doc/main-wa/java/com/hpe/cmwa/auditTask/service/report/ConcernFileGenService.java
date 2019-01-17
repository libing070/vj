/**
 * com.hp.cmcc.service.ConcernFileGenService.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.service.report;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.dao.MybatisDao;

/**
 * <pre>
 * Desc： 
 * @author peter.fu
 * @refactor peter.fu
 * @date   Mar 9, 2015 3:26:05 PM
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Mar 9, 2015 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
@Service
public class ConcernFileGenService extends BaseObject {

	@Autowired
	private MybatisDao		mybatisDao		= null;

	/**
	 * <pre>
	 * Desc  查询关注点的子页面信息
	 * @param concernId
	 * @return
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Dec 17, 2014 3:06:49 PM
	 * </pre>
	 */
	public Map<String, Object> selectConcernInfosByCode(String bizCode) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bizCode", bizCode);
		Map<String, Object> result = mybatisDao.get("ConcernMapper.selectConcernInfoByCode", params);
		return result;
	}
	
	/**
	 * <pre>
	 * Desc  查询此次待生成清单文件和审计报告的请求,subjectIds,focusCds从配置文件读入，状态不是5即认为可以生成
	 * @param concernId
	 * @return
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Mar 9, 2015 3:27:07 PM
	 * </pre>
	 */
	public List<Map<String, Object>> selectFileGenRequest(String subjectId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);
		List<Map<String, Object>> list = mybatisDao.getList("FileJobMapper.selectFileGenReqList", params);
		return list;
	}

	/**
	 * <pre>
	 * Desc  针对全国模型查看已经生成文件的省份数，=31代表全国数据都到了
	 * @param status
	 * @param audTrm
	 * @param subjectId
	 * @param focusCd
	 * @return
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Mar 9, 2015 3:37:31 PM
	 * </pre>
	 */
	public List<Object> selectFinishedConcerns(Integer status, String audTrm, String subjectId, int prvdId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		params.put("prvdId", prvdId);

		return mybatisDao.getList("FileJobMapper.selectFinishedConcerns", params);
	}
	
	public List<Object> selectFinishedConcernsByIds(Integer status, String audTrm, List<String>	subjectIds, int prvdId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		params.put("audTrm", audTrm);
		params.put("ids", subjectIds);
		params.put("prvdId", prvdId);

		return mybatisDao.getList("FileJobMapper.selectFinishedConcernsByIds", params);
	}
	
	public int selectFileCompletedCount(int status, String audTrm, String subjectId, String focusCd) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		params.put("focusCd", focusCd);
		
		return mybatisDao.get("FileJobMapper.selectFileCompletedCount", params);
	}

	/**
	 * <pre>
	 * Desc  根据请求id更新文件生成完毕状态
	 * @param configInfo
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Mar 10, 2015 4:29:26 PM
	 * </pre>
	 */
	@Transactional
	public void updateFileGenReqStatusAndTimeById(Map<String, Object> configInfo) {

		mybatisDao.update("FileJobMapper.updateFileGenReqStatusAndTimeById", configInfo);
	}
	
	/**
	 * <pre>
	 * Desc  根据请求周期、省份、专题等信息更新文件生成完毕状态
	 * @param configInfo
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Mar 10, 2015 4:29:26 PM
	 * </pre>
	 */
	@Transactional
	public void updateFileGenReqStatusAndTimeBySubject(Map<String, Object> configInfo) {
		
		mybatisDao.update("FileJobMapper.updateFileGenReqStatusAndTimeBySubject", configInfo);
	}
	
	@Transactional
	public void updateFileRequestExecCount(Map<String, Object> configInfo) {

		mybatisDao.update("FileJobMapper.updateFileGenReqExecCount", configInfo);
	}
	
	@Transactional
	public void updateFileRequestExecCountBysubject(Map<String, Object> configInfo) {

		mybatisDao.update("FileJobMapper.updateFileGenReqExecCountBySubject", configInfo);
	}
	/**
	 * <pre>
	 * Desc  查询对应专题和关注点的文件输出配置信息
	 * @param subjectId
	 * @param concernId
	 * @return
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Mar 10, 2015 4:42:17 PM
	 * </pre>
	 */
	public Map<String, Object> selectFileConfig(String subjectId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);
		return mybatisDao.get("FileJobMapper.selectFileConfig", params);
	}

	/**
	 * <pre>
	 * Desc  查询对应审计月、专题、关注点、文件类型的文件信息
	 * @param subjectId
	 * @param concernId
	 * @param fileType : audReport\audDetail (用于对全国审计报告|明细文件进行压缩时，从数据库中获取文件名)
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   20161019
	 * </pre>
	 */
	public List<String> selectAuditResultFile(String audTrm, String subjectId, String focusCd, String fileType) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		params.put("focusCd", focusCd);
		params.put("fileType", fileType);
		
		return mybatisDao.getList("FileJobMapper.selectAuditResultFile", params);
	}
	
	/**
	 * <pre>
	 * Desc  插入文件生成结果表
	 * @param audTrm
	 * @param subjectId
	 * @param concernId
	 * @param chinacode
	 * @param downloadUrl
	 * @param filePath
	 * @param fileName
	 * @author peter.fu
	 * @return 
	 * @refactor peter.fu
	 * @date   Mar 10, 2015 4:54:05 PM
	 * </pre>
	 */
	public boolean insertReportFile(int modelNotifyId,String audTrm, String subjectId,String subjectName, int chinacode, String filePath, String fileName) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("id", modelNotifyId);
		params.put("aud_trm", audTrm);
		params.put("report_name", fileName);
		params.put("report_path", filePath);
		params.put("subject_id", subjectId);
		params.put("subject_name", subjectName);
		params.put("prvd_id", chinacode);
		params.put("report_create_date", new Date());
		params.put("report_create_persons", propertyUtil.getPropValue("reporter"));
		 
		System.out.println("report_file:"+params);
		 
		logger.error("### 文件已生成，删除 HPMGR.busi_report_file表中原文件信息，并插入新文件信息："+params);
		
		int num = mybatisDao.get("FileJobMapper.selectReportFileNum", params);
		if(num != 0){mybatisDao.delete("FileJobMapper.deleteReportFile", params);}
		
		mybatisDao.add("FileJobMapper.insertReportFile", params);
		return num==0?true:false;
	}
	 
}
