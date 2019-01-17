/**
 * com.hpe.cmca.job.v2.NotiFileAutoProcessorForWord.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job.v2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.finals.Constants;
import com.hpe.cmca.finals.Dict;
import com.hpe.cmca.pojo.BgxzData;
import com.hpe.cmca.service.BgxzService;
import com.hpe.cmca.service.YwyjService;
import com.hpe.cmca.util.ExceptionTool;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;

/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2018-3-31 下午7:04:06
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2018-3-31 	   hufei 	         1. Created this class.
 * </pre>
 */
public class NotiFileAutoProcessorForYWYJ extends BaseObject implements IFileGenProcessor {

    @Autowired
    private YwyjService	ywyjService;
    @Autowired
    private JdbcTemplate       jdbcTemplate;

    // @Autowired
    // protected ConcernFileGenService concernFileGenService = null;
    // @Autowired
    // protected ConcernService concernService = null;
    @Autowired
    protected BgxzService      bgxzService       = null;
    @Autowired
    protected Dict	     dict	      = null;

    protected SimpleDateFormat sdf	       = new SimpleDateFormat("yyyy年MM月dd日");
    protected SimpleDateFormat yyyyMMddHHmmssSdf = new SimpleDateFormat("yyyyMMddHHmmss");

    protected boolean validateRequest(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo) {

	int focusCdsCount = 1;//Integer.parseInt(ywyjService.getPointNumber(subjectId).get("sjqd_point_Number").toString());
	List<Map<String,Object>> concernList = ywyjService.selectFinishedConcerns(null, audTrm, subjectId, prvdId);
	if (concernList.size() < focusCdsCount) {// 代表该省的5个有价卡关注点并未都执行完毕，所以不需要生成审计报告
	    return false;
	}
	return true;
    }

    public void genFile(String audTrm, String subjectId, String totalFocusCds, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo,
	    Boolean useChineseName, Boolean flag) {

	this.logger.debug("#### 生成文件：subjectId=" + subjectId + ",汇总关注点totalFocusCds=" + totalFocusCds + ",focusCd=" + focusCd + ",prvdId=" + prvdId + ",audTrm=" + audTrm);

	if (!validateRequest(audTrm, subjectId, totalFocusCds, prvdId, request, modelNotifyId, configInfo)) {
	    this.logger.error("#### 不满足生成文件的条件，专题处理器无法启动：subjectId=" + subjectId + ",汇总关注点totalFocusCds=" + totalFocusCds + ",focusCd=" + focusCd + ",prvdId=" + prvdId + ",audTrm=" + audTrm);
	    return;
	}
	try {
	    if (prvdId != Constants.ChinaCode) {// 生成省csv,doc
		genPrvdFile(audTrm, subjectId, totalFocusCds, focusCd, prvdId, request, modelNotifyId, configInfo, useChineseName, flag);
		return;
	    }
	    genAllFile(audTrm, subjectId, focusCd, prvdId, request, modelNotifyId, configInfo, useChineseName);// 生成全国视角的文件doc

	} finally {
	    this.logger.info("#### 模型数据文件生成完毕");
	    // concernFileGenService.updateFileRequestExecCount(request);
	    ywyjService.updateFileRequestExecCountBysubjectNew(request);// 按专题更新执行次数
	   
	}
	
    }
    /**
     * <pre>
     * Desc  生成全国的清单文件zip,doc汇总报告，ftp file，update status，delete file
     * @author GuoXY
     * @refactor GuoXY
     * @date   20161019
     * </pre>
     */
    public void genAllFile(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo, Boolean useChineseName) {

	File docFile = null,excelFile =null;
	try {
	    this.logger.debug("#### 生成全国文件：audTrm=" + audTrm + ",focusCd=" + focusCd);
	    String localFilePath = getLocalFilePath();
	      
	    // 第三步：从数据库中查询已生成的31各省级审计明细文件名，进行打包
	    List<String> fileCsvList = ywyjService.selectAuditResultFile(audTrm, subjectId, focusCd, Constants.Model.FileType.AUD_DETAIL);
	    String csvZipFileName = buildFileName(Constants.Model.FileType.AUD_DETAIL, audTrm, subjectId, focusCd, prvdId, useChineseName);
	    csvZipFileName = csvZipFileName.replaceAll("csv", "zip");
	    FileUtil.zipFile(localFilePath, localFilePath, csvZipFileName, fileCsvList);
	    File csvZipFile = new File(localFilePath + File.separator + csvZipFileName);

	    // 第三步：上传31省的Csv.zip，更新notify请求记录状态为3
	    Map<String, Object> csvFtpPutResult = transferFileToFtpServer(Constants.Model.FileType.AUD_DETAIL, audTrm, subjectId, focusCd, Constants.ChinaCode, csvZipFile, useChineseName);
	    csvFtpPutResult.put("createTime", request.get("csvFileGenTime"));
	    request.put("csvFileFtpTime", new Date());
	    // updateFileRequestStatus(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.CSV_FTP_FINISHED);
	    updateFileRequestStatusBySubjectId(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.CSV_FTP_FINISHED);
	    csvFtpPutResult.put("loginAccount", configInfo.get("loginAccount"));
	    csvFtpPutResult.put("userName", configInfo.get("userName"));
	    csvFtpPutResult.put("num", request.get("num"));
	    // 第五步：删除HPMGR.busi_report_file表中原文件，插入新生成的csv文件记录；
	    insertReportFile(modelNotifyId, audTrm, subjectId, focusCd, prvdId, Constants.Model.FileType.AUD_DETAIL, csvFtpPutResult,configInfo);
	    // 第七步：更新notify请求记录状态为5
	    // updateFileRequestStatus(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.File_FINISHED);
	    updateFileRequestStatusBySubjectId(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.File_FINISHED);
	} catch (Exception e) {
	    logger.error("#### " + e.getMessage(), e);
	    throw new RuntimeException("生成文件异常", e);

	} finally {
	    String isDelLocalFile = StringUtils.trimToEmpty(propertyUtil.getPropValue("isDelLocalFile"));
	    if ("true".equalsIgnoreCase(isDelLocalFile)) {
		FileUtil.removeFile(docFile);
	    }
	}
    }
    public void genPrvdFile(String audTrm, String subjectId, String totalFocusCds, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo,
	    Boolean useChineseName, Boolean flag) {
	try {
	    File csvFile = genCsvFile(audTrm, subjectId, focusCd, prvdId, configInfo, request, useChineseName);
	    if (csvFile != null) {
		// File csvZipFile = FileUtil.zipOneFile(csvFile); //省公司doc.csv文件不应该打包上传，直接传文件即可，用户直接下载文件，直接看 modified by GuoXY 20161121
		request.put("csvFileGenTime", new Date());
		// modify by pxl 改为按专题更新notify,不再按id更新
		// updateFileRequestStatus(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.CSV_FILE_FINISHED);
		updateFileRequestStatusBySubjectId(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.CSV_FILE_FINISHED);

		// 上传Csv文件，更新数据库请求记录状态
		Map<String, Object> csvFtpPutResult = transferFileToFtpServer(Constants.Model.FileType.AUD_DETAIL, audTrm, subjectId, focusCd, prvdId, csvFile, useChineseName);
		request.put("csvFileFtpTime", new Date());
		// updateFileRequestStatus(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.CSV_FTP_FINISHED);
		updateFileRequestStatusBySubjectId(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.File_FINISHED);
		// csvFtpPutResult.put("loginAccount", configInfo.get("loginAccount"));
		// csvFtpPutResult.put("userName", configInfo.get("userName"));
		// 向文件生成结果表HPMGR.busi_report_file中插入新生成的文件信息
		insertReportFile(modelNotifyId, audTrm, subjectId, focusCd, prvdId, Constants.Model.FileType.AUD_DETAIL, csvFtpPutResult, configInfo);

	    }

	} catch (Exception e) {
	    logger.error("#### 文件上传FTP异常。错误信息为：" + ExceptionTool.getExceptionDescription(e));
	} finally {
	    this.logger.info("#### 模型数据文件生成完毕");
	}
    }

    public File genCsvFile(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, Map<String, Object> request, Boolean useChineseName) {
	// 获取新文件生成目录
	String localFilePath = getLocalFilePath();
	File localPath = new File(localFilePath);
	if (localPath.exists() == false) {
	    localPath.mkdirs();
	}
	// 获取新文件名
	String localFileName = buildFileName(Constants.Model.FileType.AUD_DETAIL, audTrm, subjectId, focusCd, prvdId, useChineseName);
	String sql = (String) configInfo.get("csvSql");
	File file = new File(FileUtil.buildFullFilePath(localFilePath, localFileName));
	Writer streamWriter = null;
	try {
	    streamWriter = new OutputStreamWriter(new FileOutputStream(file), "GBK");
	    final PrintWriter printWriter = new PrintWriter(streamWriter);
	    printWriter.println((String) configInfo.get("csvHeader"));
	    jdbcTemplate.query(sql, new Object[] {audTrm, prvdId}, new RowCallbackHandler() {

		public void processRow(ResultSet rs) throws SQLException {
		    int columCount = rs.getMetaData().getColumnCount();
		    StringBuilder line = new StringBuilder(100);
		    for (int i = 1; i <= columCount; i++) {
			line.append(rs.getObject(i)).append("	,");
		    }
		    printWriter.println(line.substring(0, line.length() - 1));
		}
	    });

	    printWriter.flush();
	} catch (Exception e) {
	    throw new RuntimeException("生成csv文件异常", e);
	} finally {
	    FileUtil.closeWriter(streamWriter);
	}
	return file;
    }

    /**
     * <pre>
     * Desc  根据请求id更新生成文件请求的状态为
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @author GuoXY
     * @refactor GuoXY
     * @date   20161019
     * </pre>
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateFileRequestStatus(int modelNotifyId, String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, int status) {
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateFileRequestStatusBySubjectId(int modelNotifyId, String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, int status) {
	configInfo.put("audTrm", audTrm);
	configInfo.put("subjectId", subjectId);
	configInfo.put("prvdId", prvdId);
	configInfo.put("status", status);
	ywyjService.updateFileGenReqStatusAndTimeBySubjectNew(configInfo);
    }

    /**
     * <pre>
     * Desc  文件生成完毕插入到结果表
     * 		   删除 HPMGR.busi_report_file表中原文件信息，并插入新文件信息（ID与HPMGR.busi_model_notify的ID对应）
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param chinacode
     * @param configInfo
     * @param csvFile
     * @author GuoXY
     * @refactor GuoXY
     * @date   20161019
     * </pre>
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void insertReportFile(int modelNotifyId, String audTrm, String subjectId, String focusCd, int chinacode, String fileType, Map<String, Object> ftpPutResult, Map<String, Object> conf) {

	String filePath = (String) ftpPutResult.get("filePath");
	String fileName = (String) ftpPutResult.get("fileName");
	String downloadUrl = (String) ftpPutResult.get("downloadUrl");
	Date createTime = ftpPutResult.get("createTime") == null ? new Date() : (Date) ftpPutResult.get("createTime");
	BgxzData bgxz = new BgxzData();
	bgxz.setAudTrm(audTrm);
	bgxz.setFilePath(downloadUrl);
	bgxz.setFileType(fileType);
	bgxz.setOperType("审计清单自动生成");
	bgxz.setCreateType("manual");
	bgxz.setSubjectId(subjectId);
	bgxz.setFocusCd(focusCd);
	bgxz.setPrvdId(chinacode);
	bgxz.setLoginAccount("admin");
	bgxz.setOperPerson("sys");
	bgxz.setCreateDatetime(new Date());
	bgxz.setFileName(fileName);
	bgxz.setDownCount(0);
	// 判断是否有初始化数据不完整的记录
	bgxzService.addReportLog(bgxz, "create");

    }

    /**
     * <pre>
     * Desc  ftp文件到ftp服务器
     *       包括创建目录
     * @param csvFile
     * @param docFile
     * @author GuoXY
     * @throws Exception 
     * @refactor GuoXY
     * @date   20161019
     * </pre>
     */

    protected Map<String, Object> transferFileToFtpServer(String fileType, String audTrm, String subjectId, String focusCd, int prvdId, File upFile, Boolean useChineseName) throws Exception {

	logger.debug("#### 开始上传文件至FTP：" + fileType + "," + audTrm + "," + subjectId + "," + focusCd + "," + prvdId + ",");
	if (upFile == null) {
	    this.logger.error("#### 文件为空，不需要ftp操作");
	    return new HashMap<String, Object>();
	}

	Map<String, Object> resuluMap = new HashMap<String, Object>();
	String filePath = buildFtpFilePath(fileType, audTrm, subjectId, focusCd, prvdId);
	logger.debug("#### 构造文件上传路径为：" + filePath);

	String fileName = upFile.getName();// buildFileName(fileType, audTrm, subjectId, focusCd, prvdId, modelFinTime, useChineseName);
	String downloadUrl = buildDownloadUrl(audTrm, subjectId, focusCd, prvdId, filePath, fileName);
	resuluMap.put("filePath", filePath);
	resuluMap.put("fileName", fileName);
	resuluMap.put("downloadUrl", downloadUrl);

	String isTransferFile = propertyUtil.getPropValue("isTransferFile");
	if (!"true".equalsIgnoreCase(isTransferFile)) {
	    this.logger.error("由于ftp server 传输配置开关没有打开，暂时文件不传输到ftp server。");
	    return resuluMap;
	}
	logger.debug("#### 开始上传文件(进度:1/2):" + filePath + "," + downloadUrl);

	// 20161110 add try by GuoXY for 让文件上传不影响web服务word文件的生成
	try {
	    uploadFile(upFile, filePath);
	} catch (Exception e) {
	    logger.error("#### 文件上传FTP(  " + fileName + "  )异常。错误信息为：" + ExceptionTool.getExceptionDescription(e));
	}

	logger.debug("#### 完成上传文件(进度:2/2):" + filePath + "," + downloadUrl);
	return resuluMap;
    }

    private void uploadFile(File csvFile, String filePath) {
	FtpUtil ftpTool = null;
	try {
	    ftpTool = initFtp();
	    if (ftpTool == null) {
		return;
	    }
	    ftpTool.uploadFile(csvFile, filePath);
	} finally {
	    if (ftpTool != null) {
		ftpTool.disConnect();
	    }
	}
    }

    /**
     * <pre>
     * Desc  初始化ftp服务
     * @author GuoXY
     * @refactor GuoXY
     * @date   20161019
     * </pre>
     */
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

    /**
     * <pre>
     * Desc  从配置文件中获取本地文件存放目录
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   20161019
     * </pre>
     */
    protected String getLocalFilePath() {
	String tempDir = propertyUtil.getPropValue("tempDirV2");
	return tempDir;
    }

    /**
     * <pre>
     * Desc  /yyyymm/subjectId/focusCd/provId
     *       /yyyymm/subjectId/focusCd/10000
     *       /yyyymm/subjectId/focusCd/10100
     * @param fileType
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   20161019
     * </pre>
     */
    protected String buildFtpFilePath(String fileType, String audTrm, String subjectId, String focusCd, int prvdId) {
	String ftpPath = propertyUtil.getPropValue("ftpPathV2");
	String path = buildRelativePath(audTrm, subjectId, prvdId, focusCd);
	String finalPath = FileUtil.buildFullFilePath(ftpPath, path);
	logger.debug("#### ftp中文件存储路径为：" + finalPath);
	FileUtil.mkdirs(finalPath);

	return finalPath;
    }

    /**
     * <pre>
     * Desc  构造相对路径 /yyyymm/subjectId/focusCd/provId
     * @param audTrm
     * @param subjectId
     * @param prvdId
     * @param focusCd
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   20161019
     * </pre>
     */
    protected String buildRelativePath(String audTrm, String subjectId, int prvdId, String focusCd) {

	StringBuilder path = new StringBuilder();
	path.append(audTrm).append("/").append(subjectId).append("/").append(focusCd).append("/").append(prvdId);

	logger.error("#### buildRelativePath>>>" + path.toString());
	return path.toString();
    }

    /**
     * <pre>
     * Desc  构造http形式的下载地址
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param chinacode
     * @param filePath
     * @param fileName
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   20161019
     * </pre>
     */
    protected String buildDownloadUrl(String audTrm, String subjectId, String focusCd, int prvdId, String filePath, String fileName) {

	String ftpHttpUrlPrefix = propertyUtil.getPropValue("ftpHttpUrlPrefixV2");
	StringBuilder url = new StringBuilder(30);
	url.append(buildRelativePath(audTrm, subjectId, prvdId, focusCd)).append("/").append(fileName);
	return FileUtil.buildFullFilePath(ftpHttpUrlPrefix, url.toString());
    }

    /**
     * 
     * <pre>
     * Desc  生成文件名
     * 中文名：上海_201605_虚假家庭宽带审计清单.csv
     * 非中文：subjectId_focusCd_YYYYMM_prvdId.csv
     * @param fileType
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @param useChineseName
     * @return
     * @author hufei
     * 2018-4-15 下午1:24:15
     * </pre>
     */
    protected String buildFileName(String fileType, String audTrm, String subjectId, String focusCd, int prvdId, Boolean useChineseName) {

	StringBuilder path = new StringBuilder();
	String prvdName = prvdId + "";

	if (useChineseName) {
	    // 生成中文名审计报告/csv
	    prvdName = Constants.MAP_PROVD_NAME.get(prvdId);
	    String pointName=ywyjService.getPointName(focusCd);
	    path.append(prvdName).append("_").append(audTrm).append("_").append(pointName);

	} else {
	    path.append(subjectId).append("_").append(focusCd).append("_").append(audTrm).append("_").append(prvdName);
	}

	if (Constants.Model.FileType.AUD_REPORT.equalsIgnoreCase(fileType)) {
	    if (useChineseName) {
		path.append("审计报告.doc");
	    } else {
		path.append(".doc");
	    }
	    return path.toString();
	}

	if (Constants.Model.FileType.AUD_DETAIL.equalsIgnoreCase(fileType)) {
	    if (useChineseName) {
		path.append("审计清单.csv");
	    } else {
		path.append(".csv");
	    }
	}

	return path.toString();
    }

}
