/**
 * com.hp.cmcc.job.service.Yjk1000FileGenProcessor.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hpe.cmca.finals.Constants;
import com.hpe.cmca.util.CalendarUtils;
import com.hpe.cmca.util.ExceptionTool;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.WordUtil;
/**
 * <pre>
 * @author GuoXY
 * @refactor GuoXY
 * @date   20161019
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  20161019 	   GuoXY 	         1. Created this class.
 * </pre>
 */
public class Yjk1000FileGenProcessor extends CommonSubjectFileGenProcessor {
	private static String REPORT_TYPE = "有价卡违规管理";
	// 当前专题生成汇总审计报告所需要"跑完模型的关注点数量"
	private int focusCdsCount = 5;

	@Override
	protected boolean validateRequest(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo) {

		if (prvdId != Constants.ChinaCode) {
			List<Object> concernList = concernFileGenService.selectFinishedConcerns(null, audTrm, subjectId, prvdId);
			if (concernList.size() < focusCdsCount) {// 代表该省的5个有价卡关注点并未都执行完毕，所以不需要生成审计报告
				return false;
			}
		}
		return true;
	}

	@Override
	public File genProvDocFile(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, Map<String, Object> request, Boolean useChineseName) {

		Date currentDate = new Date();
		String docTemplatePath = (String) configInfo.get("docTemplatePath");
		String docTemplate = (String) configInfo.get("docTemplate");
		String category = (String) configInfo.get("focusCategory");

		Map<String, Object> concernInfoMap = concernFileGenService.selectConcernInfosByCode(focusCd);
		int concernId = (Integer) concernInfoMap.get("id");

		Map<String, Object> returnConcernMap = new HashMap<String, Object>();
		returnConcernMap.put("concernName", concernInfoMap.get("name").toString());
		returnConcernMap.put("auditTime", CalendarUtils.buildAuditTimeOfMonth(audTrm));
		returnConcernMap.put("auditCycle", CalendarUtils.buildAuditCycle(audTrm));

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);
		params.put("focusCd", focusCd);
		params.put("concernId", concernId);
		params.put("category", category);
		params.put("statCycle", audTrm);
		params.put("provinceCode", prvdId);
		params.put("userCityId", prvdId);
		Map<String, List<Object>> dataMap = concernService.selectPageData(params);

		StringBuffer key = new StringBuffer(200);
		List<Object> dataInfoList = dataMap.remove("reportDataList");
		Map<String, Map<String,String>> reportDataMap = new HashMap<String, Map<String,String>>();
		for (Object dataObj : dataInfoList) {
			@SuppressWarnings("unchecked")
			Map<String,String> concernCode = (Map<String,String>) dataObj;
			key.append("_").append((String) concernCode.get("bizCode"));//像_1001
			reportDataMap.put(key.toString(), concernCode);
			key.setLength(0);//清空key
		}
		dataInfoList.clear();

		List<Object> pointDataList = dataMap.remove("pointDataList");
		for (Object dataObj : pointDataList) {
			@SuppressWarnings("unchecked")
			Map<String,String> concernCode = (Map<String,String>) dataObj;
			key.append("_").append((String) concernCode.get("concernCode")).append("_").append((String) concernCode.get("bizCode"));//_1001_100101
			reportDataMap.put(key.toString(), concernCode);
			key.setLength(0);
		}
		dataInfoList.clear();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.putAll(dataMap);
		resultMap.put("infoList", reportDataMap);

		resultMap.put("provinceName", getCompanyNameOfProvince(prvdId + ""));
		resultMap.put("concernInfos", returnConcernMap);
		resultMap.put("reporter", propertyUtil.getPropValue("reporter"));
		resultMap.put("fileGenTime", sdf.format(currentDate));
		
		String localFilePath = getLocalFilePath();
		String localFileName = buildFileName(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, focusCd, prvdId, useChineseName);
		try {
			File localFile = new WordUtil().write(docTemplatePath, docTemplate, resultMap, localFilePath, localFileName);
			
			return localFile;
		} catch (Exception e) {
			FileUtil.removeFile(FileUtil.buildFullFilePath(localFilePath, localFileName));
			logger.error("#### 有价卡生成审计报告(  " + localFileName + "  )异常，文件已删除。错误信息为：" + ExceptionTool.getExceptionDescription(e));
			throw new RuntimeException("#### 有价卡生成审计报告异常。", e);
		}

	}

	public int getFocusCdsCount() {
		return focusCdsCount;
	}

	public void setFocusCdsCount(int focusCdsCount) {
		this.focusCdsCount = focusCdsCount;
	} 

	
}
