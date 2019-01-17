package com.hpe.cmca.pojo;


/**
 * <pre>
 * Desc：持续审计系统用户，对参数管理功能模块进行的操作的日志信息
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
public class SystemLogOtapaData {
	
	private Integer rn ; //序号 -- 前端
	private String userId ; //(40)  '用户ID',
	private String userNm ; //(40)  '用户名称',
	private String userPrvdId ; //(40)  '公司ID',
	private String userPrvdNm ; //(40)  '公司名称',
	private String depId ; //(40)  '部门ID',
	private String depNm ; //(40)  '部门名称',
	private String subjectId; //(50)    '审计专题编码',
	private String subjectNm; //(50)    '审计专题名称',
	private String focusId ; //(50)   '审计关注点',
	private String focusNm ; //(50)   '审计关注点名称',
	private String otapaCd ; //(100)   '参数编码',
	private String operateFields ; //(1000)   '操作列名',
	private String behavTyp ; //(50)   '操作类型',
	private String behavEffOt ; //(50)   '操作前阈值',
	private String behavEndOt ; //(50)   '操作后阈值',
	private String saveTime ; //(19)   '操作时间',
	private String operateDoc ; //(10)   '操作说明',
	
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
	public String getOtapaCd() {
		return otapaCd;
	}
	public void setOtapaCd(String otapaCd) {
		this.otapaCd = otapaCd;
	}
	public String getOperateFields() {
		return operateFields;
	}
	public void setOperateFields(String operateFields) {
		this.operateFields = operateFields;
	}
	public String getBehavTyp() {
		return behavTyp;
	}
	public void setBehavTyp(String behavTyp) {
		this.behavTyp = behavTyp;
	}
	public String getSaveTime() {
		return saveTime;
	}
	public void setSaveTime(String saveTime) {
		this.saveTime = saveTime;
	}
	
	public String getBehavEffOt() {
		return behavEffOt;
	}
	public void setBehavEffOt(String behavEffOt) {
		this.behavEffOt = behavEffOt;
	}
	public String getBehavEndOt() {
		return behavEndOt;
	}
	public void setBehavEndOt(String behavEndOt) {
		this.behavEndOt = behavEndOt;
	}
	public String getOperateDoc() {
		return operateDoc;
	}
	public void setOperateDoc(String operateDoc) {
		this.operateDoc = operateDoc;
	}

	
	
}
