package com.hpe.cmwa.auditTask.service.report;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.util.CalendarUtils;
import com.hpe.cmwa.util.ExceptionTool;
import com.hpe.cmwa.util.FileUtil;
import com.hpe.cmwa.util.StringUtils;
import com.hpe.cmwa.util.WordUtil;

/**
 * <pre>
 * @author renyx
 * @refactor renyx
 * @date   20180119
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  20180119 	   renyx 	         1. Created this class.
 * </pre>
 */
@Service
public class KhqfFileGenProcessor extends CommonSubjectFileGenProcessor {
	
	// 当前专题生成汇总审计报告所需要"跑完模型的关注点数量"
	@Autowired
	KhqfGenService khqfGenService;
	
	@Override
	public File genProvDocFile(String audTrm, String subjectId,  int prvdId, Map<String, Object> configInfo,	Map<String, Object> request,Boolean useChineseName) {

		Date currentDate = new Date();
		String docTemplatePath = (String) configInfo.get("docTemplatePath");
		String docTemplate = (String) configInfo.get("docTemplate");


		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);
		params.put("audTrm", audTrm);
		params.put("provinceCode", prvdId);
		Map<String, List<Object>> dataMap = khqfGenService.selectPageData(params);

		StringBuffer key = new StringBuffer(200);
		String subjectName =  request.get("subject_name")==null?(String)request.get("subjectName"):(String)request.get("subject_name");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.putAll(dataMap);
		String localFilePath ="";
		String localFileName ="";
		resultMap.put("provinceName", getCompanyNameOfProvince(prvdId + ""));
		resultMap.put("auditTime", CalendarUtils.buildAuditTimeOfMonth(audTrm));
		resultMap.put("fileGenTime", sdf.format(currentDate));
		if(!"custom".equals((String)request.get("reportType"))){
			resultMap.put("reporter", propertyUtil.getPropValue("reporter"));
			localFilePath = getLocalFilePath(audTrm,subjectName);
			localFileName = buildFileName( Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, subjectName,prvdId, useChineseName);
		}else{
			resultMap.put("reporter", request.get("report_create_persons").toString());
			localFilePath = propertyUtil.getPropValue("tempDir");
			localFileName = buildFileName( Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, subjectName,prvdId, useChineseName);
		}
		
		try {
			logger.error("The subjectId is :"+subjectId+"-----the data transmitted to the audit report is :"+resultMap);
			File localFile = new WordUtil().write(docTemplatePath, docTemplate, resultMap, localFilePath, localFileName);
			request.put("filePath", localFilePath);
			request.put("fileName", localFileName);
			request.put("docFileGenTime", sdf.format(currentDate));
			return localFile;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			FileUtil.removeFile(FileUtil.buildFullFilePath(localFilePath, localFileName));
			logger.error("#### 终端套利生成审计报告(  " + localFileName + "  )异常，文件已删除。错误信息为:" + ExceptionTool.getExceptionDescription(e));
			throw new RuntimeException("#### 终端套利生成审计报告异常。", e);
		}

	}


}
