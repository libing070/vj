/**
 * com.hp.base.util.FtpTool.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.util;

import com.hpe.cmca.common.AsposeUtil;
import com.hpe.cmca.common.FilePropertyPlaceholderConfigurer;
import it.sauronsoftware.ftp4j.*;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
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
	private FilePropertyPlaceholderConfigurer propertyUtil = SpringContextHolder.getBean("propertyUtil");
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

	public void makeDir(String newToPath) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {

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
			logger.error(e.getMessage(),e);
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

	//从ftp下载到web
	public void downloadFile(String localFilePath, String remotePath,String fileName) {

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
			//client.upload(localFile);
			File localFile = new File(localFilePath);
			client.download(fileName, localFile);
			logger.error(">>>>>download file [" + fileName + "] from FTP directory: [" + client.currentDirectory() + "] To"+localFilePath);
		} catch (Exception e) {
			logger.error("download file from ftp server error.", e);
			throw new RuntimeException("download file to ftp server error.", e);
		}
	}

	//从ftp删除文件
	public void deleteFile(String remotePath,String fileName) {
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
			//client.upload(localFile);

			client.deleteFile(fileName);
			logger.error(">>>>>delete file [" + fileName + "] from FTP directory: [" + client.currentDirectory() + "]");
		} catch (Exception e) {
			logger.error("delete file from ftp server error.", e);
			throw new RuntimeException("delete file to ftp server error.", e);
		}
	}

	public List<String> getFileNameExpired(String remotePath,int days) {
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
			//client.upload(localFile);

			List<String> nameList = new ArrayList<String>();
			//获取当前时间
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar rightNow = Calendar.getInstance();
			Date rightNowDt = rightNow.getTime();
			// 浏览文件
			String[] list = client.listNames();
			// 使用通配浏览文件
			// FTPFile[] list = client.list("*.txt");
			// 显示文件或文件夹的修改时间 你不能获得 . 或 .. 的修改日期，否则Permission denied
			for(String f : list){
			    if(!f.equals(".") && !f.equals("..")){
			    	if(getDays(client.modifiedDate(f),rightNowDt)>days){
			    		nameList.add(f);
				    	logger.error(">>>>>list fileName expired: [" +f+">>>>>list fileTime expired: [" +client.modifiedDate(f));
			    	}
			    }
			}
			logger.error(">>>>>list file from FTP directory: [" + client.currentDirectory() + "]");
			return nameList;
		} catch (Exception e) {
			logger.error("list file from ftp server error.", e);
			throw new RuntimeException("list file to ftp server error.", e);
		}
	}
	public long getDays(Date start, Date end) throws ParseException {

	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	   return (df.parse(df.format(end)).getTime() -df.parse(df.format(start)).getTime()) / (24 * 60 * 60 * 1000);

	}


	//从ftp下载到输出流
	public void downloadFileStream(String remotePath, String fileName,OutputStream outputStream) {
		String ftpServer = org.apache.commons.lang.StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpServer"));
		String ftpPort = org.apache.commons.lang.StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpPort"));
		String ftpUser = org.apache.commons.lang.StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpUser"));
		String ftpPass = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpPass"));
		initClient(ftpServer, ftpPort, ftpUser, ftpPass);
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
			download(fileName, outputStream,0L, (FTPDataTransferListener)null);
			logger.error(">>>>>download file [" + fileName + "] from FTP directory: [" + client.currentDirectory() + "] ToFileOutputStream");
		} catch (Exception e) {
			logger.error("download FileOutputStream from ftp server error.", e);
			throw new RuntimeException("download FileOutputStream from ftp server error.", e);
		}
	}

	public void download(String remoteFileName, OutputStream outputStream, long restartAt, FTPDataTransferListener listener) throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {

		try {
			client.download(remoteFileName, (OutputStream)outputStream, restartAt, listener);
		} catch (IllegalStateException var22) {
			throw var22;
		} catch (IOException var23) {
			throw var23;
		} catch (FTPIllegalReplyException var24) {
			throw var24;
		} catch (FTPException var25) {
			throw var25;
		} catch (FTPDataTransferException var26) {
			throw var26;
		} catch (FTPAbortedException var27) {
			throw var27;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Throwable var21) {
					;
				}
			}

		}

	}
}
