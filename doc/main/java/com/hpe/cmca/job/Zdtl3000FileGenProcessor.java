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
public class Zdtl3000FileGenProcessor extends CommonSubjectFileGenProcessor {
	private static String REPORT_TYPE = "终端套利";
	private static String REPORT_TYPE_CODE = "2";
	// 当前专题生成汇总审计报告所需要"跑完模型的关注点数量"
	private int focusCdsCount = 4;

	@Override
	protected boolean validateRequest(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo) {

		if (prvdId != Constants.ChinaCode) {
			List<Object> concernList = concernFileGenService.selectFinishedConcerns(null, audTrm, subjectId, prvdId);
			List<Map> resultSet = new ArrayList<Map>();
			for (Object item : concernList) {
				Map result = (Map) item;
				if (!"3003".equals(result.get("focusCd"))) {
					resultSet.add(result);
				}
			}
			if (resultSet.size() < focusCdsCount) {// 代表该省的4个终端套利关注点并未都执行完毕，所以不需要生成审计报告
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

		StringBuffer key = new StringBuffer(200);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.putAll(dataMap);

		resultMap.put("provinceName", getCompanyNameOfProvince(prvdId + ""));
		resultMap.put("concernInfos", returnConcernMap);
		resultMap.put("reporter", propertyUtil.getPropValue("reporter"));
		resultMap.put("fileGenTime", sdf.format(currentDate));
		resultMap.put("concernName", concernInfoMap.get("name").toString());
		resultMap.put("auditTime", CalendarUtils.buildAuditTimeOfMonth(audTrm));

		String localFilePath = getLocalFilePath();
		String localFileName = buildFileName( Constants.Model.FileType.AUD_REPORT, audTrm, subjectId,focusCd, prvdId, useChineseName);
		try {
			File localFile = new WordUtil().write(docTemplatePath, docTemplate, resultMap, localFilePath, localFileName);
			request.put("docFileGenTime", currentDate);
			
			insertIntoMatch(audTrm, prvdId, resultMap);
			return localFile;
		} catch (Exception e) {
			FileUtil.removeFile(FileUtil.buildFullFilePath(localFilePath, localFileName));
			logger.error("#### 终端套利生成审计报告(  " + localFileName + "  )异常，文件已删除。错误信息为:" + ExceptionTool.getExceptionDescription(e));
			throw new RuntimeException("#### 终端套利生成审计报告异常。", e);
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
		//shqdInfo   shqdInfocj  focusDataListcj focusDataList  provinceName concernName
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if(prvdId == 10000){
			jsonMap.put("total_num_top5", resultMap.get("total_num_top5"));
			jsonMap.put("total_percent_top5", resultMap.get("total_percent_top5"));
		}else{
			jsonMap.put("total_num_top5", "");
			jsonMap.put("total_percent_top5", "");
		}
		jsonMap.put("shqdInfo", resultMap.get("shqdInfo"));
		jsonMap.put("shqdInfocj", resultMap.get("shqdInfocj"));
		jsonMap.put("focusDataList", resultMap.get("focusDataList"));
		jsonMap.put("focusDataListcj", resultMap.get("focusDataListcj"));
		jsonMap.put("provinceName", resultMap.get("provinceName"));
		jsonMap.put("concernName", resultMap.get("concernName"));
		
		reportMap.put("reportJsonData",  JSONObject.fromObject(jsonMap).toString());
		reportMap.put("reportStatus", 0);
		reportMap.put("reportTime", new Date());
		concernService.insertInto(reportMap);
	}

	
	
	public int getFocusCdsCount() {
		return focusCdsCount;
	}

	public void setFocusCdsCount(int focusCdsCount) {
		this.focusCdsCount = focusCdsCount;
	}

	protected int getAuditInterval(int concernId) {

		String attrValue = concernService.getConcernAttr(concernId, "auditInterval");
		if (StringUtils.isBlank(attrValue)) {
			return Constants.Concern.defaultAuditInterval;
		}
		return Integer.parseInt(attrValue);

	}

}
