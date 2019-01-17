/**
 * com.hp.cmcc.job.AbstractDocFileProcessor.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LogBKProcessor extends BaseObject {

	public String serverId = "";

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public void work() {
		uploadFile(getLogLocalDir()+"/"+"cmca.log", getLogFtpDir(serverId));
		logger.error(serverId+":cmca.log-uploadDone");

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());

		uploadFile(getLogLocalDir()+"/"+"cmca-log-"+sd.format(c.getTime())+".txt", getLogFtpDir(serverId));
		logger.error(serverId+":cmca-log-"+sd.format(c.getTime())+".txt-uploadDone");

	}
	public String getLogLocalDir() {

		String	tempDir = propertyUtil.getPropValue("logDir");
		FileUtil.mkdirs(tempDir);
		return propertyUtil.getPropValue("logDir");
	}

	protected String getLogFtpDir(String serverId) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());

		String logFtpDir = "";
		if("165".equals(serverId)){
			logFtpDir = propertyUtil.getPropValue("logBKDir165");
		}
		if("164".equals(serverId)){
			logFtpDir = propertyUtil.getPropValue("logBKDir164");
		}

		return logFtpDir+"/"+sd.format(c.getTime());
	}

	private FtpUtil initFtp() {
		String isTransferFile = propertyUtil.getPropValue("isTransferFile");
		if (!"true".equalsIgnoreCase(isTransferFile)) {
			return null;
		}
		FtpUtil ftpTool = new FtpUtil();
		String ftpServer = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpServer"));
		String ftpPort = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpPort"));
		String ftpUser = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpUser"));
		String ftpPass = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpPass"));
		ftpTool.initClient(ftpServer, ftpPort, ftpUser, ftpPass);
		return ftpTool;
	}
	public void uploadFile(String filePath, String ftpPath){
		FtpUtil ftpTool = null;
		try {
			ftpTool = initFtp();
			if (ftpTool == null) {
				return;
			}
			ftpTool.makeDir(ftpPath);
			ftpTool.uploadFile(new File(filePath), ftpPath);
		} catch (Exception e){
			e.printStackTrace();
			logger.error("NotiFile uploadFile>>>"+e.getMessage(),e);
		} finally {
			if (ftpTool != null) {
				ftpTool.disConnect();
			}
		}
	}
}
