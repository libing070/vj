/**
 * com.hp.cmcc.service.ConcernFileGenService.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.pojo.BgxzData;

/**
 *
 * <pre>
 * Desc： copy from cmccca
 * @author GuoXY
 * @refactor GuoXY
 * @date   Oct 27, 2016 7:33:34 PM
 * @version 1.0
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  Oct 27, 2016 	   GuoXY 	         1. Created this class.
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
	 * Desc  查询此次待生成清单文件和审计报告的请求
	 * @param concernId
	 * @return
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Mar 9, 2015 3:27:07 PM
	 * </pre>
	 */
	public List<Map<String, Object>> selectFileGenRequest(List<String> subjectIds, List<String> focusCds) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectIds", subjectIds);
		params.put("focusCds", focusCds);
		List<Map<String, Object>> list = mybatisDao.getList("FileJobMapper.selectFileGenReqList", params);
		return list;
	}

	public List<Map<String, Object>> selectFileGenRequestNew(List<String> subjectIds, List<String> focusCds) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectIds", subjectIds);
		params.put("focusCds", focusCds);
		List<Map<String, Object>> list = mybatisDao.getList("FileJobMapper.selectFileGenReqListNew", params);
		return list;
	}

	public List<Map<String, Object>> selectFileGenRequest2(List<String> subjectIds, List<String> focusCds,String audTrm) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectIds", subjectIds);
		params.put("focusCds", focusCds);
		params.put("audTrm", audTrm);
		List<Map<String, Object>> list = mybatisDao.getList("FileJobMapper.selectFileGenReqListNew", params);
		return list;
	}

	public List<Map<String, Object>> selectFileGenRequest3(List<String> subjectIds, List<String> focusCds,String audTrm,String prvdId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectIds", subjectIds);
		params.put("focusCds", focusCds);
		params.put("audTrm", audTrm);
		params.put("prvdId", prvdId);
		List<Map<String, Object>> list = mybatisDao.getList("FileJobMapper.selectFileGenReqListNew", params);
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

	public List<Object> selectFinishedConcernsNew(Integer status, String audTrm, String subjectId, int prvdId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		params.put("prvdId", prvdId);

		return mybatisDao.getList("FileJobMapper.selectFinishedConcernsNew", params);
	}

	public int selectFileCompletedCount(int status, String audTrm, String subjectId, String focusCd) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		params.put("focusCd", focusCd);

		return mybatisDao.get("FileJobMapper.selectFileCompletedCount", params);
	}

	public List<Object> selectFinishedConcernsByIds(Integer status, String audTrm, List<String>	subjectIds, int prvdId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		params.put("audTrm", audTrm);
		params.put("ids", subjectIds);
		params.put("prvdId", prvdId);

		return mybatisDao.getList("FileJobMapper.selectFinishedConcernsByIds", params);
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
	@Transactional
	public void updateFileRequestExecCountBysubject(Map<String, Object> configInfo) {

		mybatisDao.update("FileJobMapper.updateFileGenReqExecCountBySubject", configInfo);
	}

	@Transactional
	public void updateFileRequestExecCountBysubjectNew(Map<String, Object> configInfo) {

		mybatisDao.update("FileJobMapper.updateFileGenReqExecCountBySubjectNew", configInfo);
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
	public void updateFileGenReqStatusAndTimeBySubjectNew(Map<String, Object> configInfo) {

		mybatisDao.update("FileJobMapper.updateFileGenReqStatusAndTimeBySubjectNew", configInfo);
	}

	@Transactional
	public void updateFileRequestExecCount(Map<String, Object> configInfo) {

		mybatisDao.update("FileJobMapper.updateFileGenReqExecCount", configInfo);
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
	public Map<String, Object> selectFileConfig(String subjectId, String focusCd) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);
		params.put("focusCd", focusCd);

		return mybatisDao.get("FileJobMapper.selectFileConfig", params);
	}
	
	/**
	 * <pre>
	 * Desc  查询对应专题和关注点的全国文件输出配置信息
	 * @param subjectId
	 * @param concernId
	 * @return
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Mar 10, 2015 4:42:17 PM
	 * </pre>
	 */
	public Map<String, Object> selectAllFileConfig(String subjectId, String focusCd) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);
		params.put("focusCd", focusCd);

		return mybatisDao.get("FileJobMapper.selectAllFileConfig", params);
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

	public List<String> selectAuditResultFileNew(String audTrm, String subjectId, String focusCd, String fileType) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		params.put("fileType", fileType);

		if((!"1".equals(subjectId))&&(!"6".equals(subjectId))&&(!"7".equals(subjectId))&&(!"4".equals(subjectId))&&(!"13".equals(subjectId))) {
			params.put("focusCd", focusCd);
		}

		if("12".equals(subjectId)) {
			params.put("focusCd", "");
		}

		return mybatisDao.getList("FileJobMapper.selectAuditResultFileNew", params);
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
	 * @refactor peter.fu
	 * @date   Mar 10, 2015 4:54:05 PM
	 * </pre>
	 */
	public void insertReportFile(int modelNotifyId,String audTrm, String subjectId, String concernId, int chinacode, String fileType, String filePath, String fileName, String downloadUrl, Date createTime) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("modelNotifyId", modelNotifyId);
		params.put("fileName", fileName);
		params.put("filePath", filePath);
		params.put("downloadUrl", downloadUrl);
		params.put("fileType", fileType);
		params.put("auditMonthly", audTrm);
		params.put("auditSubject", subjectId);
		params.put("aduitor", chinacode);
		params.put("auditConcern", concernId);
		params.put("createTime", new Date());
		params.put("createPerson", propertyUtil.getPropValue("reporter"));

		System.out.println("report_file:"+params);

		logger.error("### 文件已生成，删除 HPMGR.busi_report_file表中原文件信息，并插入新文件信息："+params);

		mybatisDao.delete("FileJobMapper.deleteReportFile", params);
		mybatisDao.add("FileJobMapper.insertReportFile", params);
	}

	public void insertReportFileNew(int modelNotifyId,String audTrm, String subjectId, String concernId, int chinacode, String fileType, String filePath, String fileName, String downloadUrl, Date createTime) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("modelNotifyId", modelNotifyId);
		params.put("fileName", fileName);
		params.put("filePath", filePath);
		params.put("downloadUrl", downloadUrl);
		params.put("fileType", fileType);
		params.put("auditMonthly", audTrm);
		params.put("auditSubject", subjectId);
		params.put("aduitor", chinacode);
		params.put("auditConcern", concernId);
		params.put("createTime", new Date());
		params.put("createPerson", propertyUtil.getPropValue("reporter"));

		System.out.println("report_file:"+params);

//		logger.error("### 文件已生成，删除 HPMGR.busi_report_file表中原文件信息，并插入新文件信息："+params);

		mybatisDao.delete("FileJobMapper.deleteReportFileNew", params);
		mybatisDao.add("FileJobMapper.insertReportFileNew", params);
	}

	/**
	 *
	 * <pre>
	 * Desc  更新审计报告|审计清单下载计数
	 * @param parms
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   Nov 22, 2016 11:27:57 AM
	 * </pre>
	 */
	public void updateWorkspaceFile(Map<String, Object> parms) {
		mybatisDao.update("commonMapper.updateWorkspaceFile", parms);
	}

	public void updateWorkspaceFile2(Map<String, Object> parms) {
		mybatisDao.update("commonMapper.updateWorkspaceFile2", parms);
	}

	@Transactional
	public void updateFileGenConcern(String audTrm,String focusCd,String focusCdAfter) {
		Map<String, Object> configInfo =new HashMap<String,Object>();
		configInfo.put("audTrm", audTrm);
		configInfo.put("focusCd", focusCd);
		configInfo.put("focusCdAfter", focusCdAfter);
		mybatisDao.update("FileJobMapper.updateFileGenConcern", configInfo);
	}

	@Transactional
	public void updateFileGenConcernNew(String audTrm,String focusCd,String focusCdAfter) {
		Map<String, Object> configInfo =new HashMap<String,Object>();
		configInfo.put("audTrm", audTrm);
		configInfo.put("focusCd", focusCd);
		configInfo.put("focusCdAfter", focusCdAfter);
		mybatisDao.update("FileJobMapper.updateFileGenConcernNew", configInfo);
	}

}
