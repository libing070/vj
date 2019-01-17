/**
 * com.hp.cmcc.job.service.FileGenService.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.service.report;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

public interface IFileGenProcessor {

	/**
	 * <pre>
	 * Desc  生成文件
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   20161019
	 * </pre>
	 */
	public void genFile(String audTrm, String subjectId,  int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo, Boolean useChineseName, Boolean flag);
 
	/**
	 * <pre>
	 * Desc  更新请求记录状态
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   20161019
	 * </pre>
	 */
	public void updateFileRequestStatus(int modelNotifyId, String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, int status);
}
