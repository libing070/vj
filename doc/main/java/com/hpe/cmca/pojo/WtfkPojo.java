package com.hpe.cmca.pojo;

import java.util.List;

public class WtfkPojo {

	private Integer proId;// 问题id
	private String proPutId;// 问题提出人id
	private String proName;// 问题名称
	private String proEncod;// 问题编号
	private Integer classId;// 问题类别id
	private Integer priorityId;// 优先级id
	private String proDescribe;// 问题描述
	private String proTime;// 问题创建时间
	private String proRcontent;// 联系人
	private Integer proStatus;// 问题状态状态定义，0新建，1已解决，2重新打开，3已反馈待确认
	private String proDel;// 删除问题id
	private String proTel;// 电话
	private String proPutName;// 问题提出人姓名
	private String proEmail;// 邮箱
	private String className;
	private String priorityName;
	private String dealProId;
	private String dealDescribe;
	private String dealProName;
	private String dealTel;
	private String dealEmail;
	private String dealPhotoName;
	private String proPhotoName;
	
	private String statusName;
	public Integer getProId() {
		return proId;
	}
	public void setProId(Integer proId) {
		this.proId = proId;
	}
	public String getProPutId() {
		return proPutId;
	}
	public void setProPutId(String proPutId) {
		this.proPutId = proPutId;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getProEncod() {
		return proEncod;
	}
	public void setProEncod(String proEncod) {
		this.proEncod = proEncod;
	}
	public Integer getClassId() {
		return classId;
	}
	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	public Integer getPriorityId() {
		return priorityId;
	}
	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}
	public String getProDescribe() {
		return proDescribe;
	}
	public void setProDescribe(String proDescribe) {
		this.proDescribe = proDescribe;
	}
	public String getProTime() {
		return proTime;
	}
	public void setProTime(String proTime) {
		this.proTime = proTime;
	}
	public String getProRcontent() {
		return proRcontent;
	}
	public void setProRcontent(String proRcontent) {
		this.proRcontent = proRcontent;
	}
	public Integer getProStatus() {
		return proStatus;
	}
	public void setProStatus(Integer proStatus) {
		this.proStatus = proStatus;
	}
	public String getProDel() {
		return proDel;
	}
	public void setProDel(String proDel) {
		this.proDel = proDel;
	}
	public String getProTel() {
		return proTel;
	}
	public void setProTel(String proTel) {
		this.proTel = proTel;
	}
	public String getProPutName() {
		return proPutName;
	}
	public void setProPutName(String proPutName) {
		this.proPutName = proPutName;
	}
	public String getProEmail() {
		return proEmail;
	}
	public void setProEmail(String proEmail) {
		this.proEmail = proEmail;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getPriorityName() {
		return priorityName;
	}
	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}
	public String getDealProId() {
		return dealProId;
	}
	public void setDealProId(String dealProId) {
		this.dealProId = dealProId;
	}
	public String getDealDescribe() {
		return dealDescribe;
	}
	public void setDealDescribe(String dealDescribe) {
		this.dealDescribe = dealDescribe;
	}
	public String getDealProName() {
		return dealProName;
	}
	public void setDealProName(String dealProName) {
		this.dealProName = dealProName;
	}
	public String getDealTel() {
		return dealTel;
	}
	public void setDealTel(String dealTel) {
		this.dealTel = dealTel;
	}
	public String getDealEmail() {
		return dealEmail;
	}
	public void setDealEmail(String dealEmail) {
		this.dealEmail = dealEmail;
	}
	public String getDealPhotoName() {
		return dealPhotoName;
	}
	public void setDealPhotoName(String dealPhotoName) {
		this.dealPhotoName = dealPhotoName;
	}
	public String getProPhotoName() {
		return proPhotoName;
	}
	public void setProPhotoName(String proPhotoName) {
		this.proPhotoName = proPhotoName;
	}
	
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	@Override
	public String toString() {
		return "WtfkPojo [proId=" + proId + ", proPutId=" + proPutId + ", proName=" + proName + ", proEncod=" + proEncod
				+ ", classId=" + classId + ", priorityId=" + priorityId + ", proDescribe=" + proDescribe + ", proTime="
				+ proTime + ", proRcontent=" + proRcontent + ", proStatus=" + proStatus + ", proDel=" + proDel
				+ ", proTel=" + proTel + ", proPutName=" + proPutName + ", proEmail=" + proEmail + ", className="
				+ className + ", priorityName=" + priorityName + ", dealProId=" + dealProId + ", dealDescribe="
				+ dealDescribe + ", dealProName=" + dealProName + ", dealTel=" + dealTel + ", dealEmail=" + dealEmail
				+ ", dealPhotoName=" + dealPhotoName + ", proPhotoName=" + proPhotoName + ", statusName=" + statusName
				+ "]";
	}
	
	

}
