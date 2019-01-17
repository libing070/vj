package com.hpe.cmca.job;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.hpe.cmca.finals.Constants;
import com.hpe.cmca.service.QF4000Service;
import com.hpe.cmca.util.CalendarUtils;
import com.hpe.cmca.util.ExceptionTool;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.StringUtils;
import com.hpe.cmca.util.WordUtil;

public class QF4000FileGenProcessor extends CommonSubjectFileGenProcessor {
	private int focusCdsCount = 2;

	/*
	 * 首先判断该省份此周期是否生成过，如果生成过（即map里包含该省份该周期）则返回false
	 * 如果是非集团的省份（即prvdId!=10000）,需要所有关注点都运行完毕才能生成csv和doc文件
	 * 
	 * @see
	 * com.hp.cmcc.job.service.CommonSubjectFileGenProcessor#validateRequest
	 * (java.lang.String, java.lang.String, java.lang.String, int,
	 * java.util.Map, int, java.util.Map)
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

	@Autowired
	QF4000Service qf4000Service;

	/*
	 * 流程如下： 1.获取模型执行完毕的消息，即生成文件的请求消息 2.获取省编码和审计周期
	 * 3.根据内存数据，判断此省份编码和审计周期是否在此次运行任务中生成过文件 3.1 如果生成过文件，则跳过此请求迭代处理下一条请求，改状态 3.2
	 * 如果没生成过文件，则继续下一步 4.判断省编码是否是集团公司，即省编码是不是10000 4.1
	 * 如果是10000，则只生成doc文件并更改请求状态，请示在内存中记录已经生成文件的省公司编码和周期。生成完毕之后跳出处理下一条请求 4.2
	 * 如果不是10000，则下一步 5.判断该省该周期的关注点个数是否是=8（注意此时不看状态) 5.1 如果不等于8,则跳过此请求处理下一请求 5.2
	 * 如果等于8 ，则生成文件doc,csv且更改 有价卡prvdId和audTrm相同的所有请求
	 * 的状态，同时在内存中记录已经生成文件的省编码信息和周期 (non-Javadoc)
	 * 
	 * @see com.hp.cmcc.job.ConcernFileGenJob#genProvDocFile(java.lang.String,
	 * java.lang.String, java.lang.String, int, java.util.Map, java.util.Map)
	 */
	@Transactional
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
		returnConcernMap.put("auditCycle", CalendarUtils.buildAuditCycle(audTrm));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);
		params.put("focusCd", focusCd);
		params.put("concernId", concernId);
		params.put("category", category);
		params.put("statCycle", audTrm);
		params.put("provinceCode", prvdId);
		params.put("userCityId", prvdId);
		// Map<String, List<Object>> dataMap =
		// concernService.selectPageData(params);

		StringBuffer key = new StringBuffer(200);

		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.putAll(qf4000Service.selectAuditReportPageData(params));

		resultMap.put("provinceName", getCompanyNameOfProvince(prvdId + ""));
		resultMap.put("concernInfos", returnConcernMap);
		resultMap.put("reporter", propertyUtil.getPropValue("reporter"));
		resultMap.put("fileGenTime", sdf.format(currentDate));

		resultMap.put("concernName", concernInfoMap.get("name").toString());
		resultMap.put("auditTime", CalendarUtils.buildAuditTimeOfMonth(audTrm));
		
		String localFilePath = getLocalFilePath();
		String localFileName = buildFileName(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, focusCd, prvdId, useChineseName);
		try {
			File localFile = new WordUtil().write(docTemplatePath, docTemplate, resultMap, localFilePath, localFileName);
			return localFile;
		} catch (Exception e) {
			FileUtil.removeFile(FileUtil.buildFullFilePath(localFilePath, localFileName));
			logger.error("#### 欠费生成审计报告(  " + localFileName + "  )异常，文件已删除。错误信息为：" + ExceptionTool.getExceptionDescription(e));
			throw new RuntimeException("#### 欠费生成审计报告异常。", e);
		}

	}

	public int getFocusCdsCount() {
		return focusCdsCount;
	}

	public void setFocusCdsCount(int focusCdsCount) {
		this.focusCdsCount = focusCdsCount;
	} 

	
}
