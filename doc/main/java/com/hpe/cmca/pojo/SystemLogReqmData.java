package com.hpe.cmca.pojo;

/**
 * <pre>
 * Desc：持续审计系统用户，对需求管理功能模块进行的操作的日志信息
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
public class SystemLogReqmData {

	private Integer rn ; //序号 -- 前端
	private String userId ; //(40)  '用户ID',
	private String userNm ; //(40)  '用户名称',
	private String userPrvdId ; //(40)  '公司ID',
	private String userPrvdNm ; //(40)  '公司名称',
	private String depId ; //(40)  '部门ID',
	private String depNm ; //(40)  '部门名称',
	private String reqmCd ; //(100)   '需求编号',
	private String reqmNm ; //(100)   '需求名称',
	private String fileNm ; //(200)   '文件名称',
	private String behavTyp ; //(50)   '操作类型',
	private String saveTime ; //(19)   '操作时间',
	
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
	public String getReqmCd() {
		return reqmCd;
	}
	public void setReqmCd(String reqmCd) {
		this.reqmCd = reqmCd;
	}
	public String getReqmNm() {
		return reqmNm;
	}
	public void setReqmNm(String reqmNm) {
		this.reqmNm = reqmNm;
	}
	public String getFileNm() {
		return fileNm;
	}
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
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
	
	
}
