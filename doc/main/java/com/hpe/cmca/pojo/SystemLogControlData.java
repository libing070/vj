package com.hpe.cmca.pojo;


/**
 * <pre>
 * Desc：持续审计系统用户，对审计开关功能模块进行的操作的日志信息'
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
public class SystemLogControlData {

	private Integer rn ; //序号 -- 前端
	private String userId ; //(40)  '用户ID',
	private String userNm ; //(40)  '用户名称',
	private String userPrvdId ; //(40)  '公司ID',
	private String userPrvdNm ; //(40)  '公司名称',
	private String depId ; //(40)  '部门ID',
	private String depNm ; //(40)  '部门名称',
	private String subjectId; //(50)    '审计专题编码',
	private String subjectNm; //(50)    '审计专题名称',
	private String audTrm; //(6)    '审计月',
	private String audTrmNm; //(6)    '审计月',
	private String controlTyp; //(50)   '开关类型名称',
	private String behavTyp; //(50)   '操作类型',
	private String operateBeforeStatus; //(10)   '操作前状态',
	private String operateAfterStatus; //(10)   '操作后状态',
	private String saveTime; //(19)   '操作时间',
	

	public String getAudTrmNm() {
		return audTrmNm;
	}
	public void setAudTrmNm(String audTrmNm) {
		this.audTrmNm = audTrmNm;
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
	public String getAudTrm() {
		return audTrm;
	}
	public void setAudTrm(String audTrm) {
		this.audTrm = audTrm;
	}
	public String getControlTyp() {
		return controlTyp;
	}
	public void setControlTyp(String controlTyp) {
		this.controlTyp = controlTyp;
	}
	public String getBehavTyp() {
		return behavTyp;
	}
	public void setBehavTyp(String behavTyp) {
		this.behavTyp = behavTyp;
	}
	public String getOperateBeforeStatus() {
		return operateBeforeStatus;
	}
	public void setOperateBeforeStatus(String operateBeforeStatus) {
		this.operateBeforeStatus = operateBeforeStatus;
	}
	public String getOperateAfterStatus() {
		return operateAfterStatus;
	}
	public void setOperateAfterStatus(String operateAfterStatus) {
		this.operateAfterStatus = operateAfterStatus;
	}
	public String getSaveTime() {
		return saveTime;
	}
	public void setSaveTime(String saveTime) {
		this.saveTime = saveTime;
	}
	
	
}
