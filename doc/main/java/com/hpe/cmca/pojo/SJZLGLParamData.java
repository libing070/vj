/**
 * com.hpe.cmca.pojo.SystemLogHtmlData.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.pojo;

import java.util.List;



/**
 * @Author ZhangQiang
 * @Description //数据质量管理需求，接收前端JS变量实体类
 * @Date 16:32 2018/10/23
 * @Param
 * @return
 **/
public class SJZLGLParamData {

	private List<String> subjectIds  ; // '审计专题list'
	private List<String> audTrms  ; // '审计月list'
	private List<String> fileTyps  ; // '文件类型list'
	private List<String> filePrvdIds  ; // '文件所属公司list'

	private String prvdId ; // 省份标识 // 区域
	private String audTrm ; // 审计月
	private String subjectId ; // '审计专题
	private String jihePointId ; // 稽核点编码
	private List<String> jihePointIds ; // 稽核点编码
	private String port ; // 涉及接口
	private String prvdFeedback ; // 省公司反馈
	private String dataSql ; // 执行的sql
	private String audTrmStr ; // 审计期间开始
	private String audTrmEnd ; // 审计期间结束

	private String userId ; // '操作人ID'
	private String userNm;  //  '操作人姓名'
	private String doType ; //'操作类型',
	private String doDate ; //'操作时间'
	private String fileType;  //  '文件类型'
	private String fileCreateDate ; //'文件生成时间',
	private String fileReportDate ; //'最新下载时间'
	private String fileWebUrl ; //'文件生成web路径',
	private String fileFtpUrl ; //'文件上传ftp路径'

	private String handleState ; // 是否特殊处理
	private String handleCause ; // 处理原因
	private String retransmissionCause ; // 重传原因
	private Integer downNum ; // 下载次数
	private Integer status ; // 状态

	public List<String> getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(List<String> subjectIds) {
		this.subjectIds = subjectIds;
	}

	public List<String> getAudTrms() {
		return audTrms;
	}

	public void setAudTrms(List<String> audTrms) {
		this.audTrms = audTrms;
	}

	public List<String> getFileTyps() {
		return fileTyps;
	}

	public void setFileTyps(List<String> fileTyps) {
		this.fileTyps = fileTyps;
	}

	public List<String> getFilePrvdIds() {
		return filePrvdIds;
	}

	public void setFilePrvdIds(List<String> filePrvdIds) {
		this.filePrvdIds = filePrvdIds;
	}

	public String getPrvdId() {
		return prvdId;
	}

	public void setPrvdId(String prvdId) {
		this.prvdId = prvdId;
	}

	public String getAudTrm() {
		return audTrm;
	}

	public void setAudTrm(String audTrm) {
		this.audTrm = audTrm;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getJihePointId() {
		return jihePointId;
	}

	public void setJihePointId(String jihePointId) {
		this.jihePointId = jihePointId;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPrvdFeedback() {
		return prvdFeedback;
	}

	public void setPrvdFeedback(String prvdFeedback) {
		this.prvdFeedback = prvdFeedback;
	}

	public String getDataSql() {
		return dataSql;
	}

	public void setDataSql(String dataSql) {
		this.dataSql = dataSql;
	}

	public List<String> getJihePointIds() {
		return jihePointIds;
	}

	public void setJihePointIds(List<String> jihePointIds) {
		this.jihePointIds = jihePointIds;
	}

	public String getAudTrmStr() {
		return audTrmStr;
	}

	public void setAudTrmStr(String audTrmStr) {
		this.audTrmStr = audTrmStr;
	}

	public String getAudTrmEnd() {
		return audTrmEnd;
	}

	public void setAudTrmEnd(String audTrmEnd) {
		this.audTrmEnd = audTrmEnd;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public String getDoType() {
		return doType;
	}

	public void setDoType(String doType) {
		this.doType = doType;
	}

	public String getDoDate() {
		return doDate;
	}

	public void setDoDate(String doDate) {
		this.doDate = doDate;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileCreateDate() {
		return fileCreateDate;
	}

	public void setFileCreateDate(String fileCreateDate) {
		this.fileCreateDate = fileCreateDate;
	}

	public String getFileReportDate() {
		return fileReportDate;
	}

	public void setFileReportDate(String fileReportDate) {
		this.fileReportDate = fileReportDate;
	}

	public String getHandleState() {
		return handleState;
	}

	public void setHandleState(String handleState) {
		this.handleState = handleState;
	}

	public String getHandleCause() {
		return handleCause;
	}

	public void setHandleCause(String handleCause) {
		this.handleCause = handleCause;
	}

	public String getRetransmissionCause() {
		return retransmissionCause;
	}

	public void setRetransmissionCause(String retransmissionCause) {
		this.retransmissionCause = retransmissionCause;
	}

	public Integer getDownNum() {
		return downNum;
	}

	public void setDownNum(Integer downNum) {
		this.downNum = downNum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFileWebUrl() {
		return fileWebUrl;
	}

	public void setFileWebUrl(String fileWebUrl) {
		this.fileWebUrl = fileWebUrl;
	}

	public String getFileFtpUrl() {
		return fileFtpUrl;
	}

	public void setFileFtpUrl(String fileFtpUrl) {
		this.fileFtpUrl = fileFtpUrl;
	}
}
