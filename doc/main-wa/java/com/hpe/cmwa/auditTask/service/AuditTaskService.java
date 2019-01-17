/**
 * com.hpe.cmwa.service.AuditTaskService.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.dao.MybatisDao;

/**
 * <pre>
 * Desc： 审计任务相关的服务
 * @author   peter.fu
 * @refactor peter.fu
 * @date     Nov 21, 2016 3:47:14 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Nov 21, 2016 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
@Service
public class AuditTaskService extends BaseObject {

	@Autowired
	private MybatisDao	mybatisDao;

	/**
	 * <pre>
	 * Desc  根据审计点id，获取审计点相关的详情页面
	 * @param auditId
	 * @return
	 * @author peter.fu
	 * Nov 21, 2016 4:08:06 PM
	 * </pre>
	 */
	public String queryAuditTaskUrl(Map<String, Object> parameterMap) {

		String auditId = parameterMap.get("auditId") == null ? "" : parameterMap.get("auditId").toString();
		// String taskCode = parameterMap.get("taskCode") == null ? "" : parameterMap.get("taskCode").toString();
		Map<String, String> map = this.dict.getMap("auditTaskUrl");

		return map.get(auditId);
	}

	/**
	 * <pre>
	 * Desc  记录文件加载完成的通知消息model_file_notification
	 * @param parameterMap
	 * @return
	 * @author peter.fu
	 * Nov 21, 2016 3:49:15 PM
	 * </pre>
	 */
	public void addFileNotification(Map<String, Object> parameterMap) {
		parameterMap.put("status", 0);
		mybatisDao.add("workMapper.insetModelFileNotification", parameterMap);
	}

}
