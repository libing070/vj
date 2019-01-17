package com.hpe.cmca.job.v2;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.util.ExceptionTool;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.WordUtil;
import com.hpe.cmca.util.CalendarUtils;
import com.hpe.cmca.finals.Constants;
import com.hpe.cmca.util.StringUtils;

public class DZJ6000FileGenProcessor extends CommonSubjectFileGenProcessor {
    @Autowired
    private MybatisDao mybatisDao = null;
    private int focusCdsCount = 2;

    /*
     * 首先判断该省份此周期是否生成过，如果生成过（即map里包含该省份该周期）则返回false 如果是非集团的省份（即prvdId!=10000）,需要所有关注点都运行完毕才能生成csv和doc文件
     * 
     * @see com.hp.cmcc.job.service.CommonSubjectFileGenProcessor#validateRequest (java.lang.String, java.lang.String, java.lang.String, int, java.util.Map, int, java.util.Map)
     */
    @Override
    protected boolean validateRequest(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo) {

	if (prvdId != Constants.ChinaCode) {
	    List<Object> concernList = concernFileGenService.selectFinishedConcerns(null, audTrm, subjectId, prvdId);
	    if (concernList.size() < focusCdsCount) {// 代表该省的2个关注点并未都执行完毕，所以不需要生成审计报告
		return false;
	    }
	}
	return true;
    }

    @Override
    public File genProvDocFile(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, Map<String, Object> request, Boolean useChineseName) {
	// Date currentDate = new Date();
	// String docTemplatePath = (String) configInfo.get("docTemplatePath");
	// String docTemplate = (String) configInfo.get("docTemplate");
	// String category = (String) configInfo.get("focusCategory");
	//
	// Map<String, Object> concernInfoMap = concernFileGenService.selectConcernInfosByCode(focusCd);
	// int concernId = (Integer) concernInfoMap.get("id");
	// Map<String, Object> returnConcernMap = new HashMap<String, Object>();
	// returnConcernMap.put("concernName", concernInfoMap.get("name").toString());
	// returnConcernMap.put("auditTime", CalendarUtils.buildAuditTimeOfMonth(audTrm, this.getAuditInterval(concernId)));
	// returnConcernMap.put("auditCycle", CalendarUtils.buildAuditCycle(audTrm, "yyyy年MM月"));
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put("subjectId", subjectId);
	// params.put("focusCd", focusCd);
	// params.put("concernId", concernId);
	// params.put("category", category);
	// params.put("statCycle", audTrm);
	// params.put("provinceCode", prvdId);
	// params.put("userCityId", prvdId);
	// Map<String, List<Object>> dataMap = concernService.selectPageData(params);
	// Map<String, Object> resultMap = new HashMap<String, Object>();
	//
	// String localFilePath = getLocalFilePath();
	// String localFileName = buildFileName(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, focusCd, prvdId, useChineseName);
	// try {
	// logger.error("The subjectId is :"+subjectId+"-----the data transmitted to the audit report is :"+resultMap);
	// File localFile = new WordUtil().write(docTemplatePath, docTemplate, resultMap, localFilePath, localFileName);
	// request.put("docFileGenTime", currentDate);
	//
	// return localFile;
	// } catch (Exception e) {
	// logger.error(e.getMessage(),e);
	// FileUtil.removeFile(FileUtil.buildFullFilePath(localFilePath, localFileName));
	// logger.error("#### 渠道养卡生成审计报告(" + localFileName + ")异常，文件已删除。错误信息为：" + ExceptionTool.getExceptionDescription(e));
	// throw new RuntimeException("#### 渠道养卡生成审计报告异常。", e);
	// }

	Date currentDate = new Date();
	String docTemplatePath = (String) configInfo.get("docTemplatePath");
	String docTemplate = (String) configInfo.get("docTemplate");
	String category = (String) configInfo.get("focusCategory");

	Map<String, Object> concernInfoMap = concernFileGenService.selectConcernInfosByCode(focusCd);
	int concernId = (Integer) concernInfoMap.get("id");

	Map<String, Object> returnConcernMap = new HashMap<String, Object>();
	returnConcernMap.put("concernName", concernInfoMap.get("name").toString());
	returnConcernMap.put("auditTime", CalendarUtils.buildAuditTimeOfMonth(audTrm, this.getAuditInterval(concernId)));
	returnConcernMap.put("auditCycle", CalendarUtils.buildAuditCycle(audTrm, "yyyy年MM月"));

	Map<String, Object> params = new HashMap<String, Object>();
	params.put("subjectId", subjectId);
	params.put("focusCd", focusCd);
	params.put("concernId", concernId);
	params.put("category", category);
	params.put("statCycle", audTrm);
	params.put("provinceCode", prvdId);
	params.put("userCityId", prvdId);
	Map<String, List<Object>> dataMap = concernService.selectPageData(params);
	
	List<Map<String,Object>> checkMap=mybatisDao.getList("DZQ6000.auditReport_select_sum_6002",params);
	if(checkMap==null||checkMap.size()==0||checkMap.get(0).get("sumNum")==null){
	    return null;
	}
	
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
	String localFileName = buildFileName(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, focusCd, prvdId, useChineseName);
	try {
	    logger.error("The subjectId is :" + subjectId + "-----the data transmitted to the audit report is :" + resultMap);
	    File localFile = new WordUtil().write(docTemplatePath, docTemplate, resultMap, localFilePath, localFileName);
	    request.put("docFileGenTime", currentDate);
	    return localFile;
	} catch (Exception e) {
	    logger.error(e.getMessage(), e);
	    FileUtil.removeFile(FileUtil.buildFullFilePath(localFilePath, localFileName));
	    logger.error("#### 电子券生成审计报告(  " + localFileName + "  )异常，文件已删除。错误信息为:" + ExceptionTool.getExceptionDescription(e));
	    throw new RuntimeException("#### 电子券生成审计报告异常。", e);
	}

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
