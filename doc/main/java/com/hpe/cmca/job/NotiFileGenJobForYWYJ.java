/**
 * com.hpe.cmca.job.NotiFileGenJobForYWYJ.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.pojo.BgxzData;
import com.hpe.cmca.service.BgxzService;
import com.hpe.cmca.service.NotiFileGenService;
import com.hpe.cmca.service.YwyjService;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;

/**
 * <pre>
 * Desc：
 * @author   hufei
 * @refactor hufei
 * @date     2018-3-31 下午2:14:39
 * @version  1.0
 *
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  2018-3-31 	   hufei 	         1. Created this class.
 * </pre>
 */
@Service("NotiFileGenJobForYWYJ")
public class NotiFileGenJobForYWYJ extends BaseObject {

    @Autowired
    public NotiFileGenService	      notiFileGenService       = null;

    @Autowired
    public BgxzService		     bgxzService	      = null;

    public List<AbstractNotiFileProcessor> notiFileGenProcessorList = new ArrayList<AbstractNotiFileProcessor>();

    protected Map<String, Object>	  notiFile		 = null;

    protected String		       month		    = null;

    protected String		       focusCd		  = null;

    protected String		       userName		 = null;

    protected String		       loginAccount	     = null;

    protected String		       zipFileName	      = null;

    protected boolean		      error		    = false;

    @Autowired
    public NotiFileGenService	      pmhzService;
    @Autowired
    private YwyjService		    ywyjService;
    protected String		       subjectId		= null;
    protected String		       focusNum		 = null;
    protected Boolean		      isAuto		   = true;

    protected String		       oprUser		  = "unknown";
    protected String		       oprPrvd		  = "unknown";

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int work() {
	int a = 0;
	List<String> alrGenAudTrm = new ArrayList<String>();
	try {
	    String focusNum = "1";//ywyjService.getPointNumber(subjectId).get("pmhz_point_Number").toString();

	    List<Map<String, Object>> result = ywyjService.getFileGenRequst(subjectId, focusNum, isAuto);
	    if (!result.isEmpty()) {
	    	List<String> haveDone =new ArrayList<String>();
		for (Map<String, Object> each : result) {
		    String audTrm = (String) each.get("audTrm");
		    Integer id = (Integer) each.get("id");
		    focusCd=(String)each.get("focusCd");
		    ywyjService.updatePmhzStatus(2, id);
		    if(!haveDone.contains(audTrm+focusCd)){
				a = genFile(audTrm, subjectId, isAuto);
				haveDone.add(audTrm+focusCd);
			}

		    ywyjService.updatePmhzStatus(5, id);

		}
		return a;
	    } else {
		return 2;// 模型执行完成的省份的排名汇总已经生成完毕
	    }
	} catch (Exception e) {
	    a = 1;// 发生异常
	    e.printStackTrace();
	    logger.error(e.getMessage(), e);
	    return a;
	}
    }

    public int genFile(String audTrm, String subjectId, Boolean isAuto) throws Exception {
	ArrayList<String> strs = new ArrayList<String>();
	month = audTrm;
	zipFileName = "排名汇总_" + month + "_" + subjectId + ".zip";
	BgxzData bgxz = new BgxzData();
	bgxz.setAudTrm(audTrm);

	bgxz.setFileType("auditPm");
	bgxz.setOperType("手动生成");
	bgxz.setCreateType("manual");
	bgxz.setSubjectId(subjectId);
	bgxz.setFocusCd(focusCd);
	bgxz.setPrvdId(10000);
	bgxz.setLoginAccount(loginAccount);
	bgxz.setOperPerson(userName);
	bgxz.setCreateDatetime(new Date());

	try {
	    for (AbstractNotiFileProcessor processor : notiFileGenProcessorList) {
		// processor.set
		processor.setMonth(month);
		processor.setFocusCd(focusCd);
		processor.setLocalDir(getLocalDir());
		processor.start();
		ArrayList<String> strs1 = processor.getFileNameList();
		if (strs1.size() > 0 && strs1.size() < 2) {
		    bgxz.setFilePath(buildDownloadUrl(strs1.get(0)));
		    bgxz.setFileName(strs1.get(0));

		    String zipFilePath = FileUtil.buildFullFilePath(getLocalDir(), strs1.get(0));
		    logger.error("start upload noti file to ftp>>" + zipFilePath + ">" + getFtpPath());
		    uploadFile(zipFilePath, getFtpPath());
		} else if (strs1.size() >=2) {
		    for (int i = 0; i < strs1.size(); i++) {
			strs.add(strs1.get(i));
		    }
		    FileUtil.zipFile(getLocalDir(), getLocalDir(), zipFileName, strs);
		    String zipFilePath = FileUtil.buildFullFilePath(getLocalDir(), zipFileName);
		    logger.error("start upload noti file to ftp>>" + zipFilePath + ">" + getFtpPath());
		    uploadFile(zipFilePath, getFtpPath());

		    // 更新busi_notification_file
		    // pmhzService.updateNotificationFile(month,subjectId,focusCd,isAuto,buildDownloadUrl());
		    bgxz.setFilePath(buildDownloadUrl(zipFileName));
		    bgxz.setFileName(zipFileName);
		}

		// 判断是否有初始化数据不完整的记录
		int count = bgxzService.updateInitReportLog(bgxz, "create");
		if (count == 0) {
		    bgxzService.addReportLog(bgxz, "create");
		}
	    }
	    return 0;// 执行完毕
	} catch (Exception e) {
	    ywyjService.updatePmhzStatusByTrmSub(4, audTrm, subjectId);
	    error = true;
	    throw e;
	    // e.printStackTrace();
	    // error = true;
	    // return 1;//执行异常
	}
    }

    public boolean hasError() {
	return error;
    }

    public List<AbstractNotiFileProcessor> getNotiFileGenProcessorList() {
	return notiFileGenProcessorList;
    }

    public void setNotiFileGenProcessorList(List<AbstractNotiFileProcessor> notiFileGenProcessorList) {
	this.notiFileGenProcessorList = notiFileGenProcessorList;
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
	String ftpServer = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpServer"));
	String ftpPort = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpPort"));
	String ftpUser = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpUser"));
	String ftpPass = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpPass"));
	ftpTool.initClient(ftpServer, ftpPort, ftpUser, ftpPass);
	return ftpTool;
    }

    public void uploadFile(String filePath, String ftpPath) {
	FtpUtil ftpTool = null;
	try {
	    ftpTool = initFtp();
	    if (ftpTool == null) {
		return;
	    }
	    ftpTool.uploadFile(new File(filePath), ftpPath);
	} catch (Exception e) {
	    e.printStackTrace();
	    logger.error("NotiFile uploadFile>>>" + e.getMessage(), e);
	} finally {
	    if (ftpTool != null) {
		ftpTool.disConnect();
	    }
	}
    }

    // public String getFtpFileName() {
    // return "审计通报_" + month +".zip";
    // }

    public String getFtpPath() {
	String tempDir = propertyUtil.getPropValue("ftpPath");
	String finalPath = FileUtil.buildFullFilePath(tempDir, buildRelativePath(month, focusCd));
	FileUtil.mkdirs(finalPath);
	return finalPath;// + "/" + zipFileName;
    }

    protected String buildRelativePath(String audTrm, String focusCd) {
	String subjectId = focusCd.substring(0, 1);
	StringBuilder path = new StringBuilder();
	path.append(month).append("/").append(subjectId).append("/").append(focusCd);
	return path.toString();
    }

    protected String buildDownloadUrl(String fileName) {
	String ftpHttpUrlPrefix = propertyUtil.getPropValue("ftpHttpUrlPrefix");
	StringBuilder url = new StringBuilder(30);
	url.append(buildRelativePath(month, focusCd)).append("/").append(fileName);
	return FileUtil.buildFullFilePath(ftpHttpUrlPrefix, url.toString());
    }

    /**
     * @return the subjectId
     */
    public String getSubjectId() {
	return this.subjectId;
    }

    /**
     * @param subjectId
     *            the subjectId to set
     */
    public void setSubjectId(String subjectId) {
	this.subjectId = subjectId;
    }

    /**
     * @return the focusNum
     */
    public String getFocusNum() {
	return this.focusNum;
    }

    /**
     * @param focusNum
     *            the focusNum to set
     */
    public void setFocusNum(String focusNum) {
	this.focusNum = focusNum;
    }

    /**
     * @return the isAuto
     */
    public Boolean getIsAuto() {
	return this.isAuto;
    }

    /**
     * @param isAuto
     *            the isAuto to set
     */
    public void setIsAuto(Boolean isAuto) {
	this.isAuto = isAuto;
    }

    /**
     * @return the pmhzService
     */
    public NotiFileGenService getPmhzService() {
	return this.pmhzService;
    }

    /**
     * @param pmhzService
     *            the pmhzService to set
     */
    public void setPmhzService(NotiFileGenService pmhzService) {
	this.pmhzService = pmhzService;
    }

    public String getOprUser() {
	return this.oprUser;
    }

    public void setOprUser(String oprUser) {
	this.oprUser = oprUser;
    }

    public String getOprPrvd() {
	return this.oprPrvd;
    }

    public void setOprPrvd(String oprPrvd) {
	this.oprPrvd = oprPrvd;
    }

    public String getLoginAccount() {
	return this.loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
	this.loginAccount = loginAccount;
    }

}
