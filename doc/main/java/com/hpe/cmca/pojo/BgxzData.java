package com.hpe.cmca.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BgxzData {

	private String audTrm ;
	private Integer prvdId;
	private String subjectId;
	private String fileType;
	private String fileName;
	private String roleId;
	private String focusCd;
	private String operType;
	private Date downDatetime;
	private Date createDatetime;
	private String operPerson;
	private Integer downCount;
	private String createType;
	private String loginAccount;
	private Date fileUploadDate;
	private String fileComment;
	private String reviewOpinion;
	private String filePath;
	private String status;
	private Integer id;
	private String flag;
	private List<Integer> ids =new ArrayList<Integer>();
	
	public String getAudTrm() {
		return this.audTrm;
	}
	
	public void setAudTrm(String audTrm) {
		this.audTrm = audTrm;
	}

	public Integer getPrvdId() {
		return this.prvdId;
	}

	public void setPrvdId(Integer prvdId) {
		this.prvdId = prvdId;
	}

	public String getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getFocusCd() {
		return this.focusCd;
	}

	public void setFocusCd(String focusCd) {
		this.focusCd = focusCd;
	}

	public String getOperType() {
		return this.operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public Date getDownDatetime() {
		return this.downDatetime;
	}

	public void setDownDatetime(Date downDatetime) {
		this.downDatetime = downDatetime;
	}

	public String getOperPerson() {
		return this.operPerson;
	}

	public void setOperPerson(String operPerson) {
		this.operPerson = operPerson;
	}

	public Integer getDownCount() {
		return this.downCount;
	}

	public void setDownCount(Integer downCount) {
		this.downCount = downCount;
	}

	public String getCreateType() {
		return this.createType;
	}

	public void setCreateType(String createType) {
		this.createType = createType;
	}

	public String getLoginAccount() {
		return this.loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public Date getFileUploadDate() {
		return this.fileUploadDate;
	}

	public void setFileUploadDate(Date fileUploadDate) {
		this.fileUploadDate = fileUploadDate;
	}

	public String getFileComment() {
		return this.fileComment;
	}

	public void setFileComment(String fileComment) {
		this.fileComment = fileComment;
	}

	public String getReviewOpinion() {
		return this.reviewOpinion;
	}

	public void setReviewOpinion(String reviewOpinion) {
		this.reviewOpinion = reviewOpinion;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getCreateDatetime() {
		return this.createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Integer> getIds() {
		return this.ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	

}
