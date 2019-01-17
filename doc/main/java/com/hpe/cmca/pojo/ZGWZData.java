/**
 * com.hpe.cmca.pojo.khqfParameterData.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.pojo;


/**
 * <pre>
 * Desc： 该类为接收前端JS变量实体类
 * @author   hufei
 * @refactor hufei
 * @date     2017-7-6 下午4:47:04
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-7-6 	   hufei 	         1. Created this class. 
 * </pre>  
 */
public class ZGWZData {
	
	private String audTrm;//审计月
	private int issueRn;//有价卡整改问责要求列表--序号
	private String issuePrvdName;//有价卡整改问责要求列表--省份名称
	private String issueInfo;//有价卡整改问责要求列表--概要
	private String issueDetail;//有价卡整改问责要求列表--详细描述
	private String issueAdvice;//有价卡整改问责要求列表--整改建议
	private String issueDemand;//有价卡整改问责要求列表--问责要求
	private String issueTime;//有价卡整改问责要求列表--整改时间
	private String issueDeadline;//有价卡整改问责要求列表--整改期限
	private int issueNum;//有价卡整改问责统计-整改次数

	
	
	public String getAudTrm() {
		return this.audTrm;
	}


	
	public void setAudTrm(String audTrm) {
		this.audTrm = audTrm;
	}


	public int getIssueRn() {
		return this.issueRn;
	}

	
	public void setIssueRn(int issueRn) {
		this.issueRn = issueRn;
	}

	
	public String getIssuePrvdName() {
		return this.issuePrvdName;
	}

	
	public void setIssuePrvdName(String issuePrvdName) {
		this.issuePrvdName = issuePrvdName;
	}

	
	public String getIssueInfo() {
		return this.issueInfo;
	}

	
	public void setIssueInfo(String issueInfo) {
		this.issueInfo = issueInfo;
	}

	
	public String getIssueDetail() {
		return this.issueDetail;
	}

	
	public void setIssueDetail(String issueDetail) {
		this.issueDetail = issueDetail;
	}

	
	public String getIssueAdvice() {
		return this.issueAdvice;
	}

	
	public void setIssueAdvice(String issueAdvice) {
		this.issueAdvice = issueAdvice;
	}

	
	public String getIssueDemand() {
		return this.issueDemand;
	}

	
	public void setIssueDemand(String issueDemand) {
		this.issueDemand = issueDemand;
	}

	
	public String getIssueTime() {
		return this.issueTime;
	}

	
	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	
	public String getIssueDeadline() {
		return this.issueDeadline;
	}

	
	public void setIssueDeadline(String issueDeadline) {
		this.issueDeadline = issueDeadline;
	}

	
	public int getIssueNum() {
		return this.issueNum;
	}

	
	public void setIssueNum(int issueNum) {
		this.issueNum = issueNum;
	}
    
    
	
}
