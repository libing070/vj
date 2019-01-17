/**
 * com.hp.cmcc.job.service.QdykFileGenService.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.hpe.cmca.finals.Constants;
import com.hpe.cmca.util.CalendarUtils;
import com.hpe.cmca.util.ExceptionTool;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.StringUtils;
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
public class Qdyk2002FileGenProcessor extends CommonSubjectFileGenProcessor {
	
	private static String REPORT_TYPE = "渠道养卡";
	private static String REPORT_TYPE_CODE = "1";
	// 当前专题生成汇总审计报告所需要"跑完模型的关注点数量" 
	private int focusCdsCount = 1;

	@Override
	protected boolean validateRequest(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo) {

		if (prvdId != Constants.ChinaCode) {
			List<Object> concernList = concernFileGenService.selectFinishedConcerns(null, audTrm, subjectId, prvdId);
			if (concernList.size() < focusCdsCount) {// 渠道养卡只有一个关注点且没有汇总点，只要为1==concernList.size()时，就可以生成审计报告
				return false;
			}
		}
		return true;
	}

	@Override
	public File genProvDocFile(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo,	Map<String, Object> request,Boolean useChineseName) {

		Date currentDate = new Date();
		String docTemplatePath = (String) configInfo.get("docTemplatePath");
		String docTemplate = (String) configInfo.get("docTemplate");
		String category = (String) configInfo.get("focusCategory");

		Map<String, Object> concernInfoMap = concernFileGenService.selectConcernInfosByCode(focusCd);
		int concernId = (Integer) concernInfoMap.get("id");
		Map<String, Object> returnConcernMap = new HashMap<String, Object>();
		returnConcernMap.put("concernName", concernInfoMap.get("name").toString());
		returnConcernMap.put("auditTime", CalendarUtils.buildAuditTimeOfMonth(audTrm, this.getAuditInterval(concernId)));
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
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (dataMap.containsKey("rptInfoList") && dataMap.get("rptInfoList").size() > 0) {
			// .addAttribute("rptInfo", dataMap.get("rptInfoList").get(0));
			resultMap.put("rptInfo", dataMap.get("rptInfoList").get(0));
		}
		if (dataMap.containsKey("rptTop5") && dataMap.get("rptTop5").size() > 0) {
			List<Object> list = (List<Object>) dataMap.get("rptTop5");
			Map<String, List<Object>> top5Map = new HashMap<String, List<Object>>();
			Map<String, Object> obj = null;
			String target = "flag_ame";
			for (Object o : list) {
				obj = (Map<String, Object>) o;
				target = (String) obj.get("flag_name");
				if (!top5Map.containsKey(target)) {
					top5Map.put(target, new ArrayList<Object>());
				}
				top5Map.get(target).add(o);
			}
			// uiModel.addAllAttributes(top5Map);
			resultMap.putAll(top5Map);
		}

		resultMap.putAll(dataMap);
		resultMap.put("provinceName", getCompanyNameOfProvince(prvdId + ""));
		resultMap.put("concernInfos", returnConcernMap);
		resultMap.put("reporter", propertyUtil.getPropValue("reporter"));
		resultMap.put("concernName", concernInfoMap.get("name").toString());
		resultMap.put("auditRegion", prvdId == 10000 ? "中国通信集团有限公司" : dict.getAlias("PROVINCE", prvdId + "") + "分公司");
		resultMap.put("auditTime", CalendarUtils.buildAuditTimeOfMonth(audTrm));
		resultMap.put("fileGenTime", sdf.format(currentDate));

		String localFilePath = getLocalFilePath();
		String localFileName = buildFileName(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, focusCd, prvdId, useChineseName);
		try {
			File localFile = new WordUtil().write(docTemplatePath, docTemplate, resultMap, localFilePath, localFileName);
			request.put("docFileGenTime", currentDate);
			
			//将审计报告的数据存入数据库id,aud_trm,prvd_id,prvd_name,report_type,report_json_data,report_status,report_time
			//#{id},#{audTrm},#{prvdId},#{prvdName},#{reportType},#{reportJsonData},#{reportStatus},#{reportTime})
			insertIntoMatch(audTrm, prvdId, resultMap);
			
			return localFile;
		} catch (Exception e) {
			FileUtil.removeFile(FileUtil.buildFullFilePath(localFilePath, localFileName));
			logger.error("#### 渠道养卡生成审计报告(" + localFileName + ")异常，文件已删除。错误信息为：" + ExceptionTool.getExceptionDescription(e));
			throw new RuntimeException("#### 渠道养卡生成审计报告异常。", e);
		}

	}

	//将审计报告的数据存入数据库中
	private void insertIntoMatch(String audTrm, int prvdId,
			Map<String, Object> resultMap) {
		Map<String, Object> reportMap = new HashMap<String, Object>();
		reportMap.put("id", REPORT_TYPE_CODE+audTrm+prvdId);
		reportMap.put("audTrm", audTrm);
		reportMap.put("prvdId", prvdId);
		reportMap.put("prvdName",  Constants.MAP_PROVD_NAME.get(prvdId));
		reportMap.put("reportType", REPORT_TYPE);
		//total_num_top5  total_percent_top5  totalInfo   shqdInfo   provinceName concernName
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if(prvdId == 10000){
			jsonMap.put("total_num_top5", resultMap.get("total_num_top5"));
			jsonMap.put("total_percent_top5", resultMap.get("total_percent_top5"));
		}else{
			jsonMap.put("total_num_top5", "");
			jsonMap.put("total_percent_top5", "");
		}
		jsonMap.put("totalInfo", resultMap.get("totalInfo"));
		jsonMap.put("shqdInfo", resultMap.get("shqdInfo"));
		jsonMap.put("zyqdInfo", resultMap.get("zyqdInfo"));
		jsonMap.put("provinceName", resultMap.get("provinceName"));
		jsonMap.put("concernName", resultMap.get("concernName"));
		
		reportMap.put("reportJsonData",  JSONObject.fromObject(jsonMap).toString());
		reportMap.put("reportStatus", 0);
		reportMap.put("reportTime", new Date());
		concernService.insertInto(reportMap);
	}

	protected int getAuditInterval(int concernId) {

		String attrValue = concernService.getConcernAttr(concernId, "auditInterval");
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
