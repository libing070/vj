package com.hpe.cmca.pojo;

import java.util.List;
import java.util.Map;


public class LoginData {


	private String userId ;
	private String prvdId ;
	private Integer loginTimes;
	private String userNm;
	private String prvdNm;
	private String depId;
	private String depNm;
	private String phoneNum;
	private String email;
	private String lastLoginTime;
	private Map<String, Boolean> rightMap;
	private String rightList;
	
	private List<String> userIds;
	private List<String> prvdIds;
	private List<String> depIds;
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getPrvdId() {
		return this.prvdId;
	}
	
	public void setPrvdId(String prvdId) {
		this.prvdId = prvdId;
	}
	
	public Integer getLoginTimes() {
		return this.loginTimes;
	}
	
	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	
	public String getUserNm() {
		return this.userNm;
	}

	
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	
	public String getPrvdNm() {
		return this.prvdNm;
	}

	
	public void setPrvdNm(String prvdNm) {
		this.prvdNm = prvdNm;
	}

	
	public String getDepId() {
		return this.depId;
	}

	
	public void setDepId(String depId) {
		this.depId = depId;
	}

	
	public String getDepNm() {
		return this.depNm;
	}

	
	public void setDepNm(String depNm) {
		this.depNm = depNm;
	}

	
	public String getPhoneNum() {
		return this.phoneNum;
	}

	
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	
	public String getEmail() {
		return this.email;
	}

	
	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getLastLoginTime() {
		return this.lastLoginTime;
	}

	
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	
	public Map<String, Boolean> getRightMap() {
		return this.rightMap;
	}

	
	public void setRightMap(Map<String, Boolean> rightMap) {
		this.rightMap = rightMap;
	}

	
	public String getRightList() {
		return this.rightList;
	}

	
	public void setRightList(String rightList) {
		this.rightList = rightList;
	}

	
	public List<String> getUserIds() {
		return this.userIds;
	}

	
	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

	
	public List<String> getPrvdIds() {
		return this.prvdIds;
	}

	
	public void setPrvdIds(List<String> prvdIds) {
		this.prvdIds = prvdIds;
	}

	
	public List<String> getDepIds() {
		return this.depIds;
	}

	
	public void setDepIds(List<String> depIds) {
		this.depIds = depIds;
	}
	
}
