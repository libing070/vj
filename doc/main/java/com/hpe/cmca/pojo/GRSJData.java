package com.hpe.cmca.pojo;

public class GRSJData {
	private String execAud;//执行月
	private Integer subjectId;//专题编码
	private String subjectNm;//专题名称
	private Integer queryDt;//日期
	private Integer status;//状态
	private String process;//审计流程
	private Integer prvdId;//省份
	private String prvdNm;//省份名称
	public String getExecAud() {
		return execAud;
	}
	public void setExecAud(String execAud) {
		this.execAud = execAud;
	}
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectNm() {
		return subjectNm;
	}
	public void setSubjectNm(String subjectNm) {
		this.subjectNm = subjectNm;
	}
	public Integer getQueryDt() {
		return queryDt;
	}
	public void setQueryDt(Integer queryDt) {
		this.queryDt = queryDt;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public Integer getPrvdId() {
		return prvdId;
	}
	public void setPrvdId(Integer prvdId) {
		this.prvdId = prvdId;
	}
	public String getPrvdNm() {
		return prvdNm;
	}
	public void setPrvdNm(String prvdNm) {
		this.prvdNm = prvdNm;
	}
	
}
