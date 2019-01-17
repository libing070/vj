package com.hpe.cmca.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class HelperReq {

	static Logger logger = Logger.getLogger(HelperReq.class);

	public static String getParameterOrDefaultValue(HttpServletRequest req, String key, String defaultValue) {

		try {

			String param = req.getParameter(key);
			if (param == null || param.trim().isEmpty()) {
				return defaultValue;
			}
			return param;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("HelperReq.getParameterOrDefaultValue 报错:" + key + "," + defaultValue);

		}
		return defaultValue;

	}
}
