/**
 * com.hpe.cmca.pojo.SystemLogLoginData.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.pojo;

import java.math.BigDecimal;


/**
 * <pre>
 * Desc： 登录日志，记录持续审计系统用户登入登出的操作信息
 * @author   zhangqiang
 * @refactor zhangqiang
 * @date     2018-8-2 
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2018-8-2  	   zhangqiang 	         1. Created this class. 
 * </pre>  
 */
public class SystemLogLoginData {
	
	private Integer rn ; //序号 -- 前端
	private String userId ; // '操作人ID'
	private String userNm;  //  '操作人姓名'
	private String userPrvdId;  // '操作人省份ID'
	private String userPrvdNm;  // '操作人省份名称'
	private String depId  ; // '操作人部门ID'
	private String depNm  ; // '操作人部门名称'
	private String pcId ; // 客户端id
	private String behavTime ;  //  '登录时间'
	private BigDecimal responseTime ;// 响应时长--废弃
	private Integer loginCnt ;// 登录总次数
	
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
	public String getPcId() {
		return pcId;
	}
	public void setPcId(String pcId) {
		this.pcId = pcId;
	}
	public String getBehavTime() {
		return behavTime;
	}
	public void setBehavTime(String behavTime) {
		this.behavTime = behavTime;
	}
	public BigDecimal getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(BigDecimal responseTime) {
		this.responseTime = responseTime;
	}
	public Integer getLoginCnt() {
		return loginCnt;
	}
	public void setLoginCnt(Integer loginCnt) {
		this.loginCnt = loginCnt;
	}
	
	

}
