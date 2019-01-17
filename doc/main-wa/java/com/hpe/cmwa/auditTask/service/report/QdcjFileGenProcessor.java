package com.hpe.cmwa.auditTask.service.report;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
 * 1.0 		  20161019 	   GuoXY 	         1. Created this class.
 * </pre>
 */
@Service
public class QdcjFileGenProcessor extends CommonSubjectFileGenProcessor {
	// 当前专题生成汇总审计报告所需要"跑完模型的关注点数量"
	@Autowired
	QdcjGenService qdcjGenService;

	@Override
	public File genProvDocFile(String audTrm, String subjectId,  int prvdId, Map<String, Object> configInfo,	Map<String, Object> request,Boolean useChineseName) {

		Date currentDate = new Date();
		String docTemplatePath = (String) configInfo.get("docTemplatePath");
		String docTemplate = (String) configInfo.get("docTemplate");


		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);
		params.put("beginAudTrm", getBeginDate(audTrm));
		params.put("endAudTrm", audTrm);
		params.put("firAudTrm", getFirAudtrm(audTrm));
		params.put("provinceCode", prvdId);
		Map<String, List<Object>> dataMap = qdcjGenService.selectPageData(params);

		String subjectName = request.get("subject_name")==null?(String)request.get("subjectName"):(String)request.get("subject_name");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.putAll(dataMap);

		resultMap.put("provinceName", getCompanyNameOfProvince(prvdId + ""));
		resultMap.put("fileGenTime", sdf.format(currentDate));
		resultMap.put("beginAuditTime", CalendarUtils.buildAuditTimeOfMonth(getBeginDate(audTrm)));
		resultMap.put("endAudTime", CalendarUtils.buildAuditTimeOfMonth(audTrm));
		resultMap.put("endAudMouthTime", CalendarUtils.buildAuditTimeOfOnlyMonth(audTrm));
		resultMap.put("firAudTime", CalendarUtils.buildAuditTimeOfMonth(getFirAudtrm(audTrm)));
		String localFilePath ="";
		String localFileName ="";
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
			throw new RuntimeException("#### 生成审计报告异常。", e);
		}

	}
	//获取当前年的一月份
	public String getFirAudtrm(String  endAudtrm){
		return endAudtrm.substring(0,4)+"01";
	}
	//获取当前时间往前一年的审计月
	public String getBeginDate(String  endAudtrm){
		int endYear =Integer.parseInt( endAudtrm.substring(0, 4));
		int endMonth =Integer.parseInt(endAudtrm.substring(4,6));
		if(endMonth != 12){
			int beginYear = endYear-1;
			int beginMouth = endMonth+1;
			if(beginMouth<10){
			return  beginYear +"0"+beginMouth;
			}
			return beginYear +""+beginMouth;
		}
		return endYear+"01";
	}

}
