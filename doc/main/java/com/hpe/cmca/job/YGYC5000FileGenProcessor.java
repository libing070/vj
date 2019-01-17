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
import com.hpe.cmca.util.StringUtils;
import com.hpe.cmca.util.WordUtil;

/**
 * 
 * <pre>
 * Desc： 员工异常操作审计报告生成
 * @author   xuwenhu
 * @refactor xuwenhu
 * @date     2017-3-23 下午9:49:56
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-3-23 	   xuwenhu 	         1. Created this class.
 * </pre>
 */
public class YGYC5000FileGenProcessor extends CommonSubjectFileGenProcessor {

    // 当前专题生成汇总审计报告所需要"跑完模型的关注点数量"
    private int focusCdsCount = 4;

    @Override
    protected boolean validateRequest(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo) {

	if (prvdId != Constants.ChinaCode) {
	    List<Object> concernList = concernFileGenService.selectFinishedConcerns(null, audTrm, subjectId, prvdId);
	    if (concernList.size() < focusCdsCount) {// 代表该省的4个员工异常操作关注点并未都执行完毕，所以不需要生成审计报告
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
	// 从hpmgr.busi_stat_concern查询关注点相关信息 
	Map<String, Object> concernInfoMap = concernFileGenService.selectConcernInfosByCode(focusCd);
	int concernId = (Integer) concernInfoMap.get("id");

	Map<String, Object> returnConcernMap = new HashMap<String, Object>();
	returnConcernMap.put("concernName", concernInfoMap.get("name").toString());
	returnConcernMap.put("auditTime", CalendarUtils.buildAuditTimeOfMonth(audTrm, this.getAuditInterval(concernId)));// 审计期间
	// returnConcernMap.put("auditCycle", CalendarUtils.buildAuditCycle(audTrm, "yyyy年MM月"));
	returnConcernMap.put("auditCycle", CalendarUtils.buildAuditCycle(audTrm));// 审计周期/审计时间 第二个参数没用，效果和上句一样

	Map<String, Object> params = new HashMap<String, Object>();
	params.put("subjectId", subjectId);
	params.put("focusCd", focusCd);
	params.put("concernId", concernId);//两位的关注点编码
	params.put("category", category);
	params.put("statCycle", audTrm);
	params.put("provinceCode", prvdId);
	params.put("userCityId", prvdId);
	Map<String, List<Object>> dataMap = concernService.selectPageData(params);// 查询出对应的querykey，再去执行相应的查询动作查出审计报告里要的数据信息，返回结果集

	Map<String, Object> resultMap = new HashMap<String, Object>();
	resultMap.putAll(dataMap);
	
	resultMap.put("provinceName", getCompanyNameOfProvince(prvdId + ""));
	resultMap.put("concernInfos", returnConcernMap);
	resultMap.put("reporter", propertyUtil.getPropValue("reporter"));
	resultMap.put("fileGenTime", sdf.format(currentDate));
	resultMap.put("concernName", concernInfoMap.get("name").toString());
	resultMap.put("auditTime", CalendarUtils.buildAuditTimeOfMonth(audTrm));
	// 找word模板，写入数据
	String localFilePath = getLocalFilePath();
	String localFileName = buildFileName(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, focusCd, prvdId, useChineseName);
	try {
	    File localFile = new WordUtil().write(docTemplatePath, docTemplate, resultMap, localFilePath, localFileName);
	    request.put("docFileGenTime", currentDate);
	    return localFile;
	} catch (Exception e) {
	    FileUtil.removeFile(FileUtil.buildFullFilePath(localFilePath, localFileName));
	    logger.error("#### 员工异常操作生成审计报告(  " + localFileName + "  )异常，文件已删除。错误信息为:" + ExceptionTool.getExceptionDescription(e));
	    throw new RuntimeException("#### 员工异常操作生成审计报告异常。", e);
	}

    }

    public int getFocusCdsCount() {
	return focusCdsCount;
    }

    public void setFocusCdsCount(int focusCdsCount) {
	this.focusCdsCount = focusCdsCount;
    }

    /**
     * 获取审计周期跨度
     * 
     * <pre>
     * Desc  
     * @param concernId
     * @return
     * @author xuwenhu
     * 2017-3-24 上午12:42:46
     * </pre>
     */
    protected int getAuditInterval(int concernId) {
	// 根据concernId查询关注点的属性列表 HPMGR.busi_stat_concern_attr 
	String attrValue = concernService.getConcernAttr(concernId, "auditInterval");
	if (StringUtils.isBlank(attrValue)) {
	    return Constants.Concern.defaultAuditInterval;
	}
	return Integer.parseInt(attrValue);

    }

}
