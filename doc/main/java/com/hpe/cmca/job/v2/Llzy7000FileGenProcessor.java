/**
 * com.hp.cmcc.job.service.QdykFileGenService.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job.v2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.util.ExceptionTool;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.WordUtil;
import com.hpe.cmca.service.LlzyService;
import com.hpe.cmca.util.CalendarUtils;
import com.hpe.cmca.finals.Constants;
import com.hpe.cmca.common.CustomXWPFDocument;
import com.hpe.cmca.util.StringUtils;
import com.hpe.cmca.util.WordUtils;

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
@Service("Llzy7000FileGenProcessor")
public class Llzy7000FileGenProcessor extends CommonSubjectFileGenProcessor {
	// 当前专题生成汇总审计报告所需要"跑完模型的关注点数量"
	private int focusCdsCount = 2;

	@Autowired
	private LlzyService llzyService;

	protected XWPFDocument doc;

	@Override
	protected boolean validateRequest(String audTrm, String subjectId,
			String focusCd, int prvdId, Map<String, Object> request,
			int modelNotifyId, Map<String, Object> configInfo) {

//		if (prvdId != Constants.ChinaCode) {
			List<Object> concernList = concernFileGenService
					.selectFinishedConcernsByIds(null, audTrm, (List<String>)request.get("subjectIds"), prvdId);
			if (concernList.size() < focusCdsCount) {// 渠道养卡只有一个关注点且没有汇总点，只要为1==concernList.size()时，就可以生成审计报告
				return false;
			}
//		}
		return true;
	}

	@Override
	public File genProvDocFile(String audTrm, String subjectId, String focusCd,
			int prvdId, Map<String, Object> configInfo,
			Map<String, Object> request, Boolean useChineseName) {
		Map<String,Object> params1 =new HashMap<String,Object>();
		params1.put("statCycle", audTrm);
		params1.put("provinceCode", prvdId);
		Map<String, List<Object>> datamap =llzyService.selectLlzyAuditReportData(params1);
		if(datamap != null && datamap.size() > 0){
			List<Object> d1 =datamap.get("llzytotalInfo");
			List<Object> d2 =datamap.get("llzstotalInfo");
			Map<String,Object> d1map=d1!=null&&d1.size()>0?(Map<String,Object>)d1.get(0):null;
			Map<String,Object> d2map=d2!=null&&d2.size()>0?(Map<String,Object>)d2.get(0):null;
			if((d1==null || d1.size() ==0)&&(d2==null || d2.size() ==0)){
				return null;
			}else if((d1map ==null || d1map.get("yszs_sum_cnt")==null || d1map.get("yszs_sum_cnt").equals(0))
					&&(d2map ==null || d2map.get("infrac_cnt")==null || d2map.get("infrac_cnt").toString().equals("0"))){
				return null;
			}
		}
		Date currentDate = new Date();
		String docTemplatePath = (String) configInfo.get("docTemplatePath");
		String docTemplate = (String) configInfo.get("docTemplate");
		String category = (String) configInfo.get("focusCategory");

//		Map<String, Object> concernInfoMap = concernFileGenService.selectConcernInfosByCode(focusCd);
//		int concernId = (Integer) concernInfoMap.get("id");

		Map<String, Object> returnConcernMap = new HashMap<String, Object>();
//		returnConcernMap.put("concernName", concernInfoMap.get("name").toString());
		returnConcernMap.put("auditCycle", CalendarUtils.buildAuditCycle(audTrm, "yyyy年MM月"));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);
		params.put("focusCd", focusCd);
//		params.put("concernId", concernId);
		params.put("category", category);
		params.put("statCycle", audTrm);
		params.put("provinceCode", prvdId);
		params.put("userCityId", prvdId);
		// Map<String, List<Object>> dataMap =
		// concernService.selectPageData(params);

		StringBuffer key = new StringBuffer(200);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.putAll(llzyService.selectAuditReportPageData(params));

		resultMap.put("provinceName", getCompanyNameOfProvince(prvdId + ""));
		resultMap.put("concernInfos", returnConcernMap);
		resultMap.put("reporter", propertyUtil.getPropValue("reporter"));
		resultMap.put("fileGenTime", sdf.format(currentDate));

//		resultMap.put("concernName", concernInfoMap.get("name").toString());
		resultMap.put("auditTime", CalendarUtils.buildAuditTimeOfMonth(audTrm));
		
		String localFilePath = getLocalFilePath();
		String localFileName = buildFileName(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, focusCd, prvdId, useChineseName);
		try {
			logger.error("The subjectId is :"+subjectId+"-----the data transmitted to the audit report is :"+resultMap);
			File localFile = new WordUtil().write(docTemplatePath, docTemplate, resultMap, localFilePath, localFileName);
			return localFile;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			FileUtil.removeFile(FileUtil.buildFullFilePath(localFilePath, localFileName));
			logger.error("#### 流量转售生成审计报告(  " + localFileName + "  )异常，文件已删除。错误信息为：" + ExceptionTool.getExceptionDescription(e));
			throw new RuntimeException("#### 流量转售生成审计报告异常。", e);
		}
	}
	
	protected int getAuditInterval(int concernId) {

		String attrValue = concernService.getConcernAttr(concernId,
				"auditInterval");
		if (StringUtils.isBlank(attrValue)) {
			return Constants.Concern.defaultAuditInterval;
		}
		return Integer.parseInt(attrValue);

	}

	public int getFocusCdsCount() {
		return focusCdsCount;
	}

	public void setFocusCdsCount(int focusCdsCount) {
		this.focusCdsCount = focusCdsCount;
	}

}
