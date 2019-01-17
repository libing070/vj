/**
 * com.hpe.cmwa.service.SMSService.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.service.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.dao.MybatisDao;


/**
 * <pre>
 * Desc： 
 * @author peter.fu
 * @refactor peter.fu
 * @date   Mar 9, 2015 6:01:00 PM
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Mar 9, 2015 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
@Service("smsService")
public class SMSService extends BaseObject {

	@Autowired
	private MybatisDao	mybatisDao	= null;

	public List<Map<String, Object>> selectSmsRequest() {
		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> list = mybatisDao.getList("SmsJobMapper.selectSmsRequest", params);
		return list;
	}
	
	@Transactional
	public void updateSmsCountAndTime(int id,boolean result) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("sendFlag", result?"Y":"N");
		mybatisDao.update("SmsJobMapper.updateSmsCountAndTime", params);
	}

}
