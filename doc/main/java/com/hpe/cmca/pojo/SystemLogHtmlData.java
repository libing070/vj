/**
 * com.hpe.cmca.pojo.SystemLogHtmlData.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.pojo;

import java.util.List;



/**
 * <pre>
 * Desc： 系统登录日志、系统操作日志以及功能操作日志等前端JS变量实体类
 * @author   zhangqiang
 * @refactor zhangqiang
 * @date     2018-8-3 
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2018-8-3  	   zhangqiang 	         1. Created this class. 
 * </pre>  
 */
public class SystemLogHtmlData {
	
	private String behavTimeSd ;  //  '登录开始时间'
	private String behavTimeEd ;  //  '登录截止时间'
	private String behavLv1 ; //  '操作一级菜单'
	private String behavLv2 ; // '二级菜单'
	private String behavLv3 ;  // '三级菜单'
	
	private String toolName ;  // '一级/二级/三级菜单拼接的字符串'
	
	private List<String> userIds;  //  '操作人姓名list'
	private List<String> userPrvdIds;  // '操作人省份名称list'
	private List<String> depIds  ; // '操作人部门名称list'	
	private List<String> subjectIds  ; // '审计专题list'
	private List<String> audTrms  ; // '审计月list'
	private List<String> fileTyps  ; // '文件类型list'
	private List<String> filePrvdIds  ; // '文件所属公司list'
	private List<String> focusIds  ; // '审计关注点list'
	private List<String> reqmIds  ; // '审计关注点list'
	
	public String getBehavTimeSd() {
		return behavTimeSd;
	}
	public void setBehavTimeSd(String behavTimeSd) {
		this.behavTimeSd = behavTimeSd;
	}
	public String getBehavTimeEd() {
		return behavTimeEd;
	}
	public void setBehavTimeEd(String behavTimeEd) {
		this.behavTimeEd = behavTimeEd;
	}
	public String getBehavLv1() {
		return behavLv1;
	}
	public void setBehavLv1(String behavLv1) {
		this.behavLv1 = behavLv1;
	}
	public String getBehavLv2() {
		return behavLv2;
	}
	public void setBehavLv2(String behavLv2) {
		this.behavLv2 = behavLv2;
	}
	public String getBehavLv3() {
		return behavLv3;
	}
	public void setBehavLv3(String behavLv3) {
		this.behavLv3 = behavLv3;
	}
	public String getToolName() {
		return toolName;
	}
	public void setToolName(String toolName) {
		this.toolName = toolName;
	}
	public List<String> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}
	public List<String> getUserPrvdIds() {
		return userPrvdIds;
	}
	public void setUserPrvdIds(List<String> userPrvdIds) {
		this.userPrvdIds = userPrvdIds;
	}
	public List<String> getDepIds() {
		return depIds;
	}
	public void setDepIds(List<String> depIds) {
		this.depIds = depIds;
	}
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
	public List<String> getFocusIds() {
		return focusIds;
	}
	public void setFocusIds(List<String> focusIds) {
		this.focusIds = focusIds;
	}
	public List<String> getReqmIds() {
		return reqmIds;
	}
	public void setReqmIds(List<String> reqmIds) {
		this.reqmIds = reqmIds;
	}
	
}
