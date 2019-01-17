/**
 * com.hp.cmcc.job.ConcernSummaryFileGenJob.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.auditTask.service.report.CommonSubjectFileGenProcessor;
import com.hpe.cmwa.auditTask.service.report.ConcernFileGenService;
import com.hpe.cmwa.auditTask.service.report.IFileGenProcessor;
import com.hpe.cmwa.common.BaseObject;

/**
 * <pre>
 * Desc： 专题级别的汇总报告（即该汇总报告包含多个关注点的信息）
 * 比如：有价卡汇总情况的审计报告，以省为单位出报告，每个报告都包含同一周期的所有关注点（目前为4个）的数据
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
@Service
public class SubjectFileGenJob extends BaseObject {

	@Autowired
	protected ConcernFileGenService	concernFileGenService	= null;
	//这两个参数来自于applicationContext-jog.xml中的配置
	protected List<String>			subjectIds				= new ArrayList<String>();
	protected String			    subjectId				= null;
	protected List<String>			focusCds				= new ArrayList<String>();
	protected List<String>			focusCds_tot				= new ArrayList<String>();

	// 用于判断该省份此周期是否生成过，如果生成过（即map里包含该省份该周期）则不继续执行
	protected Map<String, String>	finishedFileMap			= new HashMap<String, String>();
	
	protected Map<String, String>	finishedFileMapMore			= new HashMap<String, String>();

	private CommonSubjectFileGenProcessor		commonSubjectFileGenProcessor		= null;

	// 是否使用中文文件名
	protected Boolean				useChineseName			= false;

	protected List<Integer> ids = new ArrayList<Integer>();
	//protected Integer[] ids = null;//用来更新notify记录时，确保更新的记录在此次扫描的记录中。
	
	public Boolean getUseChineseName() {
		return useChineseName;
	}

	public void setUseChineseName(Boolean useChineseName) {
		this.useChineseName = useChineseName;
	}

	/**
	 * 将(审计月、省)所有关注点请求都查出来，但在下面执行时，由于是按专题级别生成，
	 * 所以，无论selectFileGenRequest查询出几个关注点请求，都只算一次（finishedFileMap控制）。
	 * 重跑模型时，跑完了若干关注点数据（无论跑完几个）本次job也只执行一次（本质就是更新数据）
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   20161019
	 * </pre>
	 */
	public void work() {
		this.logger.debug("#### 启动专题级别审计报告生成任务！");
		// 查询 HPMGR.busi_model_notify 所有状态为 0 （模型运行完毕）的记录
		// 一次job中subjectIds只有一个值
		List<Map<String, Object>> requestList = concernFileGenService.selectFileGenRequest(subjectId);
		if (requestList.isEmpty()) {
			this.logger.error("#### 本次job未发现文件生成请求，已结束！");
			return;
		}
		for (Map<String, Object> request : requestList) {
			ids.add((Integer) request.get("id"));
		}
//		String focus_cds =totalFocusCds;
		//ids=(Integer[]) tpIds.toArray();
		List<String> fileflag =new ArrayList<String>();
		this.logger.debug("#### 找到文件生成请求notfiy：subjectIds=" + subjectIds + ",focusCds=" + focusCds + ",requestList.size=" + requestList.size());
		for (Map<String, Object> request : requestList) {
			String audTrm = (String) request.get("audTrm");
			int prvdId = Integer.parseInt( request.get("prvdId").toString());
			
			request.put("ids", ids);
			request.put("subjectIds", subjectIds);
			try {
				// 若本条请求生成了审计报告文件和csv明细文件，那么，文件结果表HPMGR.busi_report_file中相应这两个文件记录ID与当前请求ID一致
				int modelNotifyId = request.get("id") == null ? 0 : (Integer) request.get("id");			
				// 仅根据 月份 + 省份 来决定本次job中，该省份的专题审计报告是否生成。因为，只要有一个关注点的模型执行完毕 or 更新完毕，就可以生成 or 更新专题的审计报告了（虽然数据不完整）--除了员工异常操作的csv文件
				boolean flag = false;
				
				// 从 HPMGR.busi_report_file_config 中查询"本专题、汇总关注点"的配置信息 
				Map<String, Object> configInfo = concernFileGenService.selectFileConfig(subjectId);
				this.logger.debug("#### 根据专题配置信息启动专题处理器：subjectId=" + subjectId + ",prvdId=" + prvdId);
				commonSubjectFileGenProcessor.genFile(audTrm, subjectId, prvdId, request, modelNotifyId, configInfo, useChineseName, flag);
				//this.finishedFileMap.put(buildFinishedFileKey(audTrm, subjectId, prvdId, request), "true");
					
				if(!fileflag.contains(audTrm+prvdId)){
					fileflag.add(audTrm+prvdId);
				}
				
			} catch (Exception e) {
				this.logger.error("#### 文件生成异常：subjectId=" + subjectId  + ",prvdId=" + prvdId, e);
			} finally {
				this.logger.debug("#### 当前文件生成完毕：subjectId=" + subjectId  + ",prvdId=" + prvdId);
			}
		}
		this.finishedFileMap.clear();
		this.logger.debug("#### 本次job所有文件生成完毕 ！");
	}
	public String customReport(Map<String, Object> params){
		// 从 HPMGR.busi_report_file_config 中查询"本专题、汇总关注点"的配置信息 
		String  reportType = params.get("reportType").toString();
		
		String report_name = params.get("report_name").toString()+".doc";
		String  audTrm = params.get("report_end_date").toString();
		String  subjectId =  params.get("report_items").toString().split(",")[0];
		int  prvdId = Integer.parseInt(params.get("report_province").toString().split(",")[0]);
		String  report_create_persons = params.get("report_create_persons").toString();
		Map<String, Object>  request = new HashMap<String, Object>();
		Map<String, Object> configInfo = concernFileGenService.selectFileConfig(subjectId);
		boolean  useChineseName = true;
		boolean  flag = false;
		
		request.put("reportType", reportType);
		request.put("audTrm", audTrm);
		request.put("subjectId", subjectId);
		request.put("subjectName", this.dict.getMap("subjectType").get(subjectId));
		request.put("prvdId", prvdId);
		request.put("report_name", report_name);
		request.put("report_create_persons", report_create_persons);
		String reportPath = commonSubjectFileGenProcessor.customGenFile(audTrm, subjectId, prvdId, request, configInfo, useChineseName, flag);
		return reportPath;
	}

	/**
	 * <pre>
	 * Desc  判断文件在此次job中是否生成过
	 * @param audTrm
	 * @param subjectId
	 * @param focusCd
	 * @param prvdId
	 * @param request
	 * @param modelNotifyId
	 * @param configInfo
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   20161019
	 * </pre>
	 */
	protected boolean isContainedInFinishedMap(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request) {
		
		if (null!=this.finishedFileMap.get(buildFinishedFileKey(audTrm, subjectId,  prvdId, request))) {
			return true;
		}
		return false;
	}
	
	protected boolean isContainedInFinishedMapMore(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request) {
		
		if (null!=this.finishedFileMap.get(buildFinishedFileKeyMore(audTrm, subjectId, focusCd, prvdId, request))) {
			return true;
		}
		return false;
	}

	/**
	 * <pre>
	 * Desc  构建已经此次job已经完成的文件生成的条件，用避免一次job过程中文件的重复生成（主要指针对需要生成跨关注点的汇总报告）
	 * @param audTrm
	 * @param subjectId
	 * @param focusCd
	 * @param prvdId
	 * @param request
	 * @param modelNotifyId
	 * @param configInfo
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   20161019
	 * </pre>
	 */
	protected String buildFinishedFileKey(String audTrm, String subjectId, int prvdId, Map<String, Object> request) {

		StringBuffer key = new StringBuffer(50);
		key.append(prvdId).append("_").append(audTrm);
		return key.toString();

	}
	
	protected String buildFinishedFileKeyMore(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request) {

		StringBuffer key = new StringBuffer(50);
		key.append(prvdId).append("_").append(audTrm).append("_").append(focusCd);
		return key.toString();

	}

	public CommonSubjectFileGenProcessor getCommonSubjectFileGenProcessor() {
		return this.commonSubjectFileGenProcessor;
	}

	public void setCommonSubjectFileGenProcessor(CommonSubjectFileGenProcessor commonSubjectFileGenProcessor) {
		this.commonSubjectFileGenProcessor = commonSubjectFileGenProcessor;
	}

	public List<String> getSubjectIds() {
		return this.subjectIds;
	}

	public void setSubjectIds(List<String> subjectIds) {
		this.subjectIds = subjectIds;
	}
	
	public String getSubjectId() {
		return this.subjectId;
	}
	
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public List<String> getFocusCds() {
		return this.focusCds;
	}

	public void setFocusCds(List<String> focusCds) {
		this.focusCds = focusCds;
	}

}
