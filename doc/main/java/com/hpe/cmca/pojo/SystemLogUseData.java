/**
 * com.hpe.cmca.pojo.SystemLogUseData.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.pojo;

import java.math.BigDecimal;

/**
 * 
 * ClassName: SystemLogUseData 
 * @Description: '持续审计系统用户，操作系统各功能菜单的日志信息' ;
 * @author ZhangQiang
 * @date 2018-8-8
 */
public class SystemLogUseData {

	private Integer rn ; //序号 -- 前端
	private String userId ; // '操作人ID'
	private String userNm;  //  '操作人姓名'
	private String userPrvdId;  // '操作人省份ID'
	private String userPrvdNm;  // '操作人省份名称'
	private String depId  ; // '操作人部门ID'
	private String depNm  ; // '操作人部门名称'
	private String behavLv1 ; //  '操作一级菜单'
	private String behavLv2 ; // '二级菜单'
	private String behavLv3 ;  // '三级菜单'
	private String behavTyp ; //'操作类型',
	private BigDecimal responseTime ; //'响应时长'
	private String releaseNote ; //'操作结果'
	private String saveTime ; //'操作时间'
	
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
	public String getBehavTyp() {
		return behavTyp;
	}
	public void setBehavTyp(String behavTyp) {
		this.behavTyp = behavTyp;
	}
	public BigDecimal getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(BigDecimal responseTime) {
		this.responseTime = responseTime;
	}
	public String getReleaseNote() {
		return releaseNote;
	}
	public void setReleaseNote(String releaseNote) {
		this.releaseNote = releaseNote;
	}
	public String getSaveTime() {
		return saveTime;
	}
	public void setSaveTime(String saveTime) {
		this.saveTime = saveTime;
	}
	
	
	
	
}
