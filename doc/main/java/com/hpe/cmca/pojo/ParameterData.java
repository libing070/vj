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
public class ParameterData {
    private String audTrm ;
    private String concern ;
    private int prvdId ;
    private int ctyId ;
    private String chnlId ;
    private String chnlName;
    private String parameterType;
    private String staffId;
    private String acctId;
    private String focusCd ;
    private String subjectId;
    private String moduleId;
    private String sqlChart;
    private int switchState;
    private String flagId ;
    
    public String getAudTrm() {
        return this.audTrm;
    }
    
    public String getConcern() {
        return this.concern;
    }
    
    public int getPrvdId() {
        return this.prvdId;
    }
    
    public void setAudTrm(String audTrm) {
        this.audTrm = audTrm;
    }
    
    public void setConcern(String concern) {
        this.concern = concern;
    }
    
    public void setPrvdId(int prvdId) {
        this.prvdId = prvdId;
    }

	
	public int getCtyId() {
		return this.ctyId;
	}

	
	public void setCtyId(int ctyId) {
		this.ctyId = ctyId;
	}

	public String getChnlId() {
		return this.chnlId;
	}

	public void setChnlId(String chnlId) {
		this.chnlId = chnlId;
	}

	public String getChnlName() {
		return this.chnlName;
	}

	public void setChnlName(String chnlName) {
		this.chnlName = chnlName;
	}

	
	public String getParameterType() {
	    return this.parameterType;
	}

	
	public void setParameterType(String parameterType) {
	    this.parameterType = parameterType;
	}

	
	public String getStaffId() {
	    return this.staffId;
	}

	
	public void setStaffId(String staffId) {
	    this.staffId = staffId;
	}

	public String getAcctId() {
		return this.acctId;
	}

	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}

	public String getFocusCd() {
		return this.focusCd;
	}

	public void setFocusCd(String focusCd) {
		this.focusCd = focusCd;
	}

	public String getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getSqlChart() {
		return this.sqlChart;
	}

	public void setSqlChart(String sqlChart) {
		this.sqlChart = sqlChart;
	}

	
	public int getSwitchState() {
	    return this.switchState;
	}

	
	public void setSwitchState(int switchState) {
	    this.switchState = switchState;
	}

	public String getFlagId() {
		return flagId;
	}

	public void setFlagId(String flagId) {
		this.flagId = flagId;
	}

	
}
