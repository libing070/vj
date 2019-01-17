/**
 * com.hpe.cmca.pojo.SystemLogModelData.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.pojo;

/**
 * <pre>
 * Desc： 记录持续审计系统用户，操作审计报告、审计清单、排名汇总的日志
 * @author   zhangqiang
 * @refactor zhangqiang
 * @date     2018-8-10 
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2018-8-10  	   zhangqiang 	         1. Created this class. 
 * </pre>  
 */
public class SystemLogModelData {
	
	private Integer rn ; //序号 -- 前端
	private String userId ; //(40)  '用户ID',
	private String userNm ; //(40)  '用户名称',
	private String userPrvdId ; //(40)  '公司ID',
	private String userPrvdNm ; //(40)  '公司名称',
	private String depId ; //(40)  '部门ID',
	private String depNm ; //(40)  '部门名称',
	private String subjectId ; //(50)   '审计专题编码',
	private String subjectNm ; //(50)   '审计专题名称',
	private String focusId ; //(50)  '审计关注点',
	private String focusNm ; //(50)  '审计关注点名称',
	private String audTrm ; //(6)   '审计月',
	private String audTrmNm ; //(6)   '审计月',
	private String saveTime ; //(19)  '操作时间',
	private String behavTyp ; //(50)  '操作类型',
	private String fileTyp  ; //(50)  '文件类型',
	private String fileTypNm  ; //(50)  '文件类型',
	private String fileNm ; //(100)  '文件名称',
	private String fileUrl ; //(500)  '文件路径',
	private String filePrvdId ; //(5)  '文件所属公司ID',
	private String filePrvdNm ; //(50)  '文件所属公司',
	private Integer loadCnt  ; // '下载次数',
	private String releaseNote ; //(40)  '操作结果',
	
	
	
	public String getAudTrmNm() {
		return audTrmNm;
	}
	public void setAudTrmNm(String audTrmNm) {
		this.audTrmNm = audTrmNm;
	}
	public String getFileTypNm() {
		return fileTypNm;
	}
	public void setFileTypNm(String fileTypNm) {
		this.fileTypNm = fileTypNm;
	}
	public Integer getRn() {
		return rn;
	}
	public void setRn(Integer rn) {
		this.rn = rn;
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
	public String getUserPrvdId() {
		return userPrvdId;
	}
	public void setUserPrvdId(String userPrvdId) {
		this.userPrvdId = userPrvdId;
	}
	public String getUserPrvdNm() {
		return userPrvdNm;
	}
	public void setUserPrvdNm(String userPrvdNm) {
		this.userPrvdNm = userPrvdNm;
	}
	public String getDepId() {
		return depId;
	}
	public void setDepId(String depId) {
		this.depId = depId;
	}
	public String getDepNm() {
		return depNm;
	}
	public void setDepNm(String depNm) {
		this.depNm = depNm;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectNm() {
		return subjectNm;
	}
	public void setSubjectNm(String subjectNm) {
		this.subjectNm = subjectNm;
	}
	public String getFocusId() {
		return focusId;
	}
	public void setFocusId(String focusId) {
		this.focusId = focusId;
	}
	public String getFocusNm() {
		return focusNm;
	}
	public void setFocusNm(String focusNm) {
		this.focusNm = focusNm;
	}
	public String getAudTrm() {
		return audTrm;
	}
	public void setAudTrm(String audTrm) {
		this.audTrm = audTrm;
	}
	public String getSaveTime() {
		return saveTime;
	}
	public void setSaveTime(String saveTime) {
		this.saveTime = saveTime;
	}
	public String getBehavTyp() {
		return behavTyp;
	}
	public void setBehavTyp(String behavTyp) {
		this.behavTyp = behavTyp;
	}
	public String getFileTyp() {
		return fileTyp;
	}
	public void setFileTyp(String fileTyp) {
		this.fileTyp = fileTyp;
	}
	public String getFileNm() {
		return fileNm;
	}
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getFilePrvdId() {
		return filePrvdId;
	}
	public void setFilePrvdId(String filePrvdId) {
		this.filePrvdId = filePrvdId;
	}
	public String getFilePrvdNm() {
		return filePrvdNm;
	}
	public void setFilePrvdNm(String filePrvdNm) {
		this.filePrvdNm = filePrvdNm;
	}
	public Integer getLoadCnt() {
		return loadCnt;
	}
	public void setLoadCnt(Integer loadCnt) {
		this.loadCnt = loadCnt;
	}
	public String getReleaseNote() {
		return releaseNote;
	}
	public void setReleaseNote(String releaseNote) {
		this.releaseNote = releaseNote;
	}
}
