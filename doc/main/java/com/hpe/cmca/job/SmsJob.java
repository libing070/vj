/**
 * com.hp.bmcc.job.SmsJob.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.service.SMSService;
import com.hpe.cmca.webservice.sms.SSO_WebServiceLocator;
import com.hpe.cmca.webservice.sms.SSO_WebServiceSoap_PortType;

/**
 * <pre>
 * Desc： 发送短信
 * @author peter.fu
 * @refactor peter.fu
 * @date   Mar 9, 2015 6:00:01 PM
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Mar 9, 2015 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
@Service("smsJob")
public class SmsJob extends BaseObject {

	@Autowired
	private SMSService	smsService	= null;

	public void work() {

		String isSendSms = propertyUtil.getPropValue("isSendSms");
		if (!"true".equalsIgnoreCase(isSendSms)) {
			this.logger.info("由于配置开关没有打开，暂时不发送的短信.");
			return ;
		}
		
		List<Map<String, Object>> requestList = smsService.selectSmsRequest();
		if (requestList.isEmpty()) {
			this.logger.info("没有要发送的短信信息");
			return;
		}

		this.logger.info("开始发送短信信息");
		for (Map<String, Object> request : requestList) {

			String phone = (String) request.get("phone");
			String content = (String) request.get("content");
			int id = (Integer) request.get("id");
			boolean result = sendSms(phone, content);
			smsService.updateSmsCountAndTime(id, result);
		}

		this.logger.info("短信信息发送完毕");
	}

	/**
	 * <pre>
	 * Desc  调用接口发送短信
	 * @param phone
	 * @param content
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Mar 9, 2015 6:27:08 PM
	 * </pre>
	 */
	private boolean sendSms(String phone, String content) {

		this.logger.debug("发送的短信:phone=" + phone + ",content=" + content);

		SSO_WebServiceLocator locator = new SSO_WebServiceLocator();
		String smsWebService = propertyUtil.getPropValue("smsWebService");
		locator.setSSO_WebServiceSoapEndpointAddress(smsWebService);

		try {
			SSO_WebServiceSoap_PortType smsServicePortType = locator.getSSO_WebServiceSoap();			
			return smsServicePortType.sendSMS(phone, content);
		} catch (Exception e) {
			logger.error("发送短信异常.phone=" + phone + ",content=" + content, e);
		}
		return false;
	}

}
