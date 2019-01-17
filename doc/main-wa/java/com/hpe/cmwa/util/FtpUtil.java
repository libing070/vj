/**
 * com.hp.base.util.FtpTool.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.util;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/**
 * <pre>
 * Desc： 
 * @author peter.fu
 * @refactor peter.fu
 * @date   Mar 17, 2015 2:49:51 PM
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Mar 17, 2015 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
public class FtpUtil {

	private static final Logger	logger	= Logger.getLogger(FtpUtil.class);
	private FTPClient			client;

	/**
	 * 初使化客户端
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws FTPIllegalReplyException
	 * @throws FTPException
	 */
	public void initClient(String host, String port, String username, String password) {

		try {
			client = new FTPClient();
			logger.error(">>>>>start connect.......");
			System.out.println(">>>>>start connect.......");
			client.connect(host, Integer.valueOf(port));
			logger.error(">>>>>connect success.......");
			logger.error(">>>>>start login .......");
			client.login(username, password);
			logger.error(">>>>>login success.......");
			System.out.println(">>>>>login success.......");
			logger.error(">>>>>current directory is.....[" + client.currentDirectory() + "]");
		} catch (Exception e) {
			logger.error("build ftp contection exception", e);
			throw new RuntimeException("build ftp contection exception", e);
		}
	}

	public void uploadDir(String localPath, String remotePath) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {

		File localFile = new File(localPath);
		if (!localFile.exists()) {
			return;
		}
		if (localFile.isDirectory()) {
			File[] files = localFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					uploadFile(files[i], remotePath);
					continue;
				}
				String newToPath = FileUtil.buildFullFilePath(remotePath, files[i].getName());
				uploadDir(files[i].getAbsolutePath(), newToPath);
			}
			return;
		}
		uploadFile(localFile, remotePath);
	}

	/**
	 * <pre>
	 * Desc  文件是覆盖关系
	 * @param localFile
	 * @param remotePath
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws FTPIllegalReplyException
	 * @throws FTPException
	 * @throws FTPDataTransferException
	 * @throws FTPAbortedException
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Mar 17, 2015 3:40:06 PM
	 * </pre>
	 */
	public void uploadFile(File localFile, String remotePath) {

		try {
			// 如果当前目录 不是上传的目的目录,则切换目录
			if (!client.currentDirectory().equals(remotePath)) {
				if (!this.isDirExist(remotePath)) {
					this.makeDir(remotePath);
				}
				client.changeDirectory(remotePath);
			}
			// 上传到client所在的目录
			client.setType(FTPClient.TYPE_BINARY);
			client.upload(localFile);
			logger.error(">>>>>upload file [" + localFile + "] to FTP directory: [" + client.currentDirectory() + "]");
		} catch (Exception e) {
			logger.error("upload file to ftp server error.", e);
			throw new RuntimeException("upload file to ftp server error.", e);
		}
	}

	private void makeDir(String newToPath) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {

		if (null == newToPath) {
			return;
		}
		String cpath = client.currentDirectory();
		logger.error(">>>>>client dang qian menu:[" + cpath + "]");
		client.changeDirectory("/");// 切换到根目录
		StringTokenizer dirs = new StringTokenizer(newToPath, "/");
		String temp = null;
		boolean flag = false;
		while (dirs.hasMoreElements()) {
			temp = dirs.nextElement().toString();
			if (!isDirExist(temp)) {
				client.createDirectory(temp);// 创建目录
				client.changeDirectory(temp);// 进入创建的目录
				flag = true;
			}
		}
		if (flag) {
			logger.error(">>>>>create directory:[" + newToPath + "]");
		}
		client.changeDirectory(cpath);
	}

	/**
	 * 检查目录是否存在
	 * @param dir
	 * @param ftpClient
	 * @return
	 */
	public boolean isDirExist(String dir) {
		try {
			client.changeDirectory(dir);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 断开连接
	 * @throws FTPException
	 * @throws FTPIllegalReplyException
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public void disConnect() {

		try {
			client.disconnect(true);
			logger.error(">>>>>login out......");
		} catch (Exception e) {
			logger.error("ftp disconnect error.", e);
		}
	}

	/**
	 * 上传文件/目录到FTP服务器
	 * @param file
	 *        文件/目录
	 * @return
	 */
	public static void main(String[] args) throws Exception {
		FtpUtil fu = new FtpUtil();
		fu.testClient();
		fu.uploadDir("C:/aprograms/Tomcat/webapps/cmcccaData", "/home/ftp_test");
		fu.disConnect();
	}

	/**
	 * 客户端初使化
	 * @throws FTPException
	 * @throws FTPIllegalReplyException
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public void testClient() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {

		client = new FTPClient();
		logger.debug(">>>>>start connect.......");
		client.connect("10.4.29.45", 21);
		logger.debug(">>>>>connect success.......");
		logger.debug(">>>>>start login .......");
		client.login("ftp_test", "ftp_test");
		logger.debug(">>>>>login success.......");
		logger.debug(">>>>>current directory is.....[" + client.currentDirectory() + "]");
	}

	public void test() throws Exception {
		File dir = new File("C:/aprograms/Tomcat/webapps/cmcccaData");
		System.out.println(dir.getName());// cmcccaData
		System.out.println(dir.getParent());// C:\aprograms\Tomcat\webapps
		System.out.println(dir.getAbsolutePath());// C:\aprograms\Tomcat\webapps\cmcccaData

		File dir1 = new File("C:/aprograms/Tomcat/webapps/cmcccaData/a.txt");
		System.out.println(dir1.getName());// a.txt
		System.out.println(dir1.getParent());// C:\aprograms\Tomcat\webapps\cmcccaData
		System.out.println(dir1.getAbsolutePath());// C:\aprograms\Tomcat\webapps\cmcccaData\a.txt
	}

}
