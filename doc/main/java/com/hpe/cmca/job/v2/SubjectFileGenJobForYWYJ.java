/**
 * com.hpe.cmca.job.v2.SubjectFileGenJobForYWYJ.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.service.YwyjService;

/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2018-4-15 下午12:41:16
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2018-4-15 	   hufei 	         1. Created this class.
 * </pre>
 */
public class SubjectFileGenJobForYWYJ extends BaseObject {

//    @Autowired
//    protected ConcernFileGenService concernFileGenService = null;
    @Autowired
    protected YwyjService ywyjService;
    // 这两个参数来自于applicationContext-jog.xml中的配置
    protected String	  subjectIds;
    protected List<String>	  focusCds	      = new ArrayList<String>();
    protected List<String>	  focusCds_tot	  = new ArrayList<String>();

    // 汇总情况配置的伪关注点编码（各专题在applicationContext-jog.xml中配置自己专题的汇总关注点）
    protected String		totalFocusCds	 = "1000";

    // 用于判断该省份此周期是否生成过，如果生成过（即map里包含该省份该周期）则不继续执行
    protected Map<String, String>   finishedFileMap       = new HashMap<String, String>();

    protected Map<String, String>   finishedFileMapMore   = new HashMap<String, String>();

    private IFileGenProcessor       fileGenProcessor      = null;

    // 是否使用中文文件名
    protected Boolean	       useChineseName	= false;

    protected String		audTrm;
    protected String		loginAccount;
    protected String		userName;
    protected String		prvdId;

    protected List<Integer>	 ids		   = new ArrayList<Integer>();

    // protected Integer[] ids = null;//用来更新notify记录时，确保更新的记录在此次扫描的记录中。

    public Boolean getUseChineseName() {
	return useChineseName;
    }

    public void setUseChineseName(Boolean useChineseName) {
	this.useChineseName = useChineseName;
    }

    /**
     * 将(审计月、省)所有关注点请求都查出来，但在下面执行时，由于是按专题级别生成， 所以，无论selectFileGenRequest查询出几个关注点请求，都只算一次（finishedFileMap控制）。 重跑模型时，跑完了若干关注点数据（无论跑完几个）本次job也只执行一次（本质就是更新数据）
     * 
     * @author GuoXY
     * @refactor GuoXY
     * @date 20161019 </pre>
     */
    public void work() {
	this.logger.debug("#### 启动专题级别审计报告生成任务！");
	ids = new ArrayList<Integer>();
	// 查询 HPMGR.busi_model_notify 所有状态为 0 （模型运行完毕）的记录
	// 一次job中subjectIds只有一个值
	List<Map<String, Object>> requestList = ywyjService.selectFileGenRequest(subjectIds);
	if (requestList.isEmpty()) {
	    this.logger.error("#### 本次job未发现文件生成请求，已结束！");
	    return;
	}
	for (Map<String, Object> request : requestList) {
	    ids.add((Integer) request.get("id"));
	}
	List<String> fileflag = new ArrayList<String>();
	this.logger.debug("#### 找到文件生成请求notfiy：subjectIds=" + subjectIds + ",focusCds=" + focusCds + ",requestList.size=" + requestList.size());
	for (Map<String, Object> request : requestList) {
	    String subjectId = (String) request.get("subjectId");
	    String focusCd = (String) request.get("focusCd");
	    String audTrm = (String) request.get("audTrm");
	    int prvdId = (Integer) request.get("prvdId");

	    request.put("ids", ids);
	    request.put("subjectIds", subjectIds);

	    try {
		// 若本条请求生成了审计报告文件和csv明细文件，那么，文件结果表HPMGR.busi_report_file中相应这两个文件记录ID与当前请求ID一致
		int modelNotifyId = request.get("id") == null ? 0 : (Integer) request.get("id");
		// 仅根据 月份 + 省份 来决定本次job中，该省份的专题审计报告是否生成。因为，只要有一个关注点的模型执行完毕 or 更新完毕，就可以生成 or 更新专题的审计报告了（虽然数据不完整）--除了员工异常操作的csv文件
		boolean flag = false;
//		if (isContainedInFinishedMap(audTrm, subjectId, focusCd, prvdId, request)) {
//		    this.logger.debug("#### 此次job已经生成过该文件：subjectId=" + subjectId + ",focusCd=" + focusCd + ",prvdId=" + prvdId + ",audTrm=" + audTrm + ",totalFocusCds=" + totalFocusCds);
//		    // modify by pxl 按ID更新notify的状态导致状态更新不全，比如欠费有两个关注点4001,4003 ，
//		    // 当某省4001的notify记录生成报告后，4003的该省notify记录将只更新状态，无法更新各阶段时间信息。
//		    // 于是改为，此处不再更新已经生成报告的notify记录，改为在生成时按专题更新notify
//		    // fileGenProcessor.updateFileRequestStatus(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.File_FINISHED);
//		    continue;
//		}

		// 从 HPMGR.busi_report_file_config 中查询"本专题、汇总关注点"的配置信息
		Map<String, Object> configInfo = ywyjService.selectFileConfig(subjectId, focusCd);
		this.logger.debug("#### 根据专题配置信息启动专题处理器：subjectId=" + subjectId + ",focusCd=" + focusCd + ",totalFocusCds=" + totalFocusCds + ",prvdId=" + prvdId);
		if (configInfo != null) {
		    configInfo.put("isauto", "true");
		    configInfo.put("focusCdTemp", focusCd);
		}
		fileGenProcessor.genFile(audTrm, subjectId, totalFocusCds, focusCd, prvdId, request, modelNotifyId, configInfo, useChineseName, flag);
		this.finishedFileMap.put(buildFinishedFileKey(audTrm, subjectId, focusCd, prvdId, request), "true");
	    } catch (Exception e) {
		this.logger.error("#### 文件生成异常：subjectId=" + subjectId + ",focusCd=" + focusCd + ",totalFocusCds=" + totalFocusCds + ",prvdId=" + prvdId, e);
	    } finally {
		this.logger.debug("#### 当前文件生成完毕：subjectId=" + subjectId + ",focusCd=" + focusCd + ",totalFocusCds=" + totalFocusCds + ",prvdId=" + prvdId);
	    }
	}

	this.finishedFileMap.clear();
	this.logger.debug("#### 本次job所有文件生成完毕 ！");
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

	if (null != this.finishedFileMap.get(buildFinishedFileKey(audTrm, subjectId, focusCd, prvdId, request))) {
	    return true;
	}
	return false;
    }

    protected boolean isContainedInFinishedMapMore(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request) {

	if (null != this.finishedFileMap.get(buildFinishedFileKeyMore(audTrm, subjectId, focusCd, prvdId, request))) {
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
    protected String buildFinishedFileKey(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request) {

	StringBuffer key = new StringBuffer(50);
	key.append(prvdId).append("_").append(audTrm);
	return key.toString();

    }

    protected String buildFinishedFileKeyMore(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request) {

	StringBuffer key = new StringBuffer(50);
	key.append(prvdId).append("_").append(audTrm).append("_").append(focusCd);
	return key.toString();

    }

    public IFileGenProcessor getFileGenProcessor() {
	return this.fileGenProcessor;
    }

    public void setFileGenProcessor(IFileGenProcessor fileGenProcessor) {
	this.fileGenProcessor = fileGenProcessor;
    }

    public String getSubjectIds() {
	return this.subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
	this.subjectIds = subjectIds;
    }

    public List<String> getFocusCds() {
	return this.focusCds;
    }

    public void setFocusCds(List<String> focusCds) {
	this.focusCds = focusCds;
    }

    public String getTotalFocusCds() {
	return this.totalFocusCds;
    }

    public void setTotalFocusCds(String totalFocusCds) {
	this.totalFocusCds = totalFocusCds;
    }

    public String getAudTrm() {
	return this.audTrm;
    }

    public void setAudTrm(String audTrm) {
	this.audTrm = audTrm;
    }

    public String getLoginAccount() {
	return this.loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
	this.loginAccount = loginAccount;
    }

    public String getUserName() {
	return this.userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getPrvdId() {
	return this.prvdId;
    }

    public void setPrvdId(String prvdId) {
	this.prvdId = prvdId;
    }

}
