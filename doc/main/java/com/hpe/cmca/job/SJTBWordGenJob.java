package com.hpe.cmca.job;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.biframe.privilege.IUser;
import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;
import com.hpe.cmca.job.AbstractNotiFileProcessor;
import com.hpe.cmca.job.AbstractSJTBWordProcessor;
import com.hpe.cmca.job.ZgwzGenFileProcessor;
import com.hpe.cmca.service.NotiFileGenService;

@Service("SJTBWordGenJob")
public class SJTBWordGenJob extends BaseObject{
	@Autowired
	public NotiFileGenService notiFileGenService	= null;
	
	@Autowired
	private ZgwzGenFileProcessor zgwzGenFileProcessor ;
	
	public List<AbstractSJTBWordProcessor> SJTBWordGenProcessorList = new  ArrayList<AbstractSJTBWordProcessor>();
	
	protected Map<String, Object> notiFile = null;
	
	protected String month = null;
	
	protected String audTrm = null;
	
	protected String focusCd = null;
	
	protected String userName = null;
	
	protected String zipFileName = null;
	
	protected boolean error = false;
	
	protected IUser user;
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void work(){
		error = false;
		logger.info("审计通报生成请求");
		
			ArrayList<String> strs = new ArrayList<String>();
			zipFileName = "审计通报_"+month+"_"+focusCd+".zip";
				try {
					for (AbstractSJTBWordProcessor processor : SJTBWordGenProcessorList) {
						processor.setMonth(month);
						processor.setFocusCd(focusCd);
						processor.setLocalDir(getLocalDir());
						processor.start();
						strs.add(processor.getFileName());
//						strs.add(getLocalDir()+"/"+processor.getFileName());
					}
					//调用整改问责明细
					zgwzGenFileProcessor.setUser(user);
					zgwzGenFileProcessor.setAudtrm(audTrm);
					zgwzGenFileProcessor.setFocusCd(focusCd);
					zgwzGenFileProcessor.setFileList(strs);
					zgwzGenFileProcessor.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e.getMessage(),e);
					error = true;
				}
	}
	
	public boolean hasError(){
		return error;
	}

	
	public List<AbstractSJTBWordProcessor> getSJTBWordGenProcessorList() {
		return this.SJTBWordGenProcessorList;
	}

	
	public void setSJTBWordGenProcessorList(List<AbstractSJTBWordProcessor> sJTBWordGenProcessorList) {
		SJTBWordGenProcessorList = sJTBWordGenProcessorList;
	}



	public String getFocusCd() {
		return focusCd;
	}

	public void setFocusCd(String focusCd) {
		this.focusCd = focusCd;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Map<String, Object> getNotiFile() {
		return notiFile;
	}

	public void setNotiFile(Map<String, Object> notiFile) {
		this.notiFile = notiFile;
	}
	
	
	public String getAudTrm() {
		return this.audTrm;
	}

	public void setAudTrm(String audTrm) {
		this.audTrm = audTrm;
	}
	

	public IUser getUser() {
		return this.user;
	}

	public void setUser(IUser user) {
		this.user = user;
	}

	public String getLocalDir() {
		String tempDir = propertyUtil.getPropValue("tempDir");
		FileUtil.mkdirs(tempDir);
		return propertyUtil.getPropValue("tempDir");
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
	
//	public String getFtpFileName() {
//		return "审计通报_" + month +".zip";
//	}
	
	public String getFtpPath(){
		String tempDir = propertyUtil.getPropValue("ftpPath");
		String finalPath = FileUtil.buildFullFilePath(tempDir, buildRelativePath(month, focusCd));
		FileUtil.mkdirs(finalPath);
		return finalPath;// + "/" + zipFileName;
	}
	
	protected String buildRelativePath(String audTrm, String focusCd) {
		String subjectId = focusCd.substring(0,1);
		StringBuilder path = new StringBuilder();
		path.append(month).append("/").append(subjectId).append("/").append(focusCd);
		return path.toString();
	}
	
	protected String buildDownloadUrl() {
		String ftpHttpUrlPrefix = propertyUtil.getPropValue("ftpHttpUrlPrefix");
		StringBuilder url = new StringBuilder(30);
		url.append(buildRelativePath(month, focusCd)).append("/").append(zipFileName);
		return FileUtil.buildFullFilePath(ftpHttpUrlPrefix, url.toString());
	}
}
