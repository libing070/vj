/**
 * com.hp.cmcc.job.AbstractDocFileProcessor.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job.v2;

import java.io.File;
import java.util.Map;

import com.hpe.cmca.common.BaseObject;

/**
 * <pre>
 * Desc： 
 * @author GuoXY
 * @refactor GuoXY
 * @date   20161019
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  20161019 	   GuoXY 	         1. Created this class. 
 * </pre>
 */
public abstract class AbstractFileProcessor extends BaseObject {

	protected String				audTrm		= "";
	protected String				subjectId	= "";
	protected String				focusCd		= "";
	protected int					prvdId		= 0;
	protected Map<String, Object>	configInfo	= null;
	protected Map<String, Object>	request		= null;

	protected String				tmpFileName	= "";
	protected String				tmpFilePath	= "";

	/**
	 * <pre>
	 * Desc  具体执行方法（模板方法）
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * </pre>
	 */
	public abstract File execute();

	public String getAudTrm() {
		return this.audTrm;
	}

	public void setAudTrm(String audTrm) {
		this.audTrm = audTrm;
	}

	public String getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getFocusCd() {
		return this.focusCd;
	}

	public void setFocusCd(String focusCd) {
		this.focusCd = focusCd;
	}

	public int getPrvdId() {
		return this.prvdId;
	}

	public void setPrvdId(int prvdId) {
		this.prvdId = prvdId;
	}

	public Map<String, Object> getConfigInfo() {
		return this.configInfo;
	}

	public void setConfigInfo(Map<String, Object> configInfo) {
		this.configInfo = configInfo;
	}

	public Map<String, Object> getRequest() {
		return this.request;
	}

	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

	
	public String getTmpFileName() {
		return this.tmpFileName;
	}

	
	public void setTmpFileName(String tmpFileName) {
		this.tmpFileName = tmpFileName;
	}

	
	public String getTmpFilePath() {
		return this.tmpFilePath;
	}

	
	public void setTmpFilePath(String tmpFilePath) {
		this.tmpFilePath = tmpFilePath;
	}
}
