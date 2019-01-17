/**
 * com.hp.cmcc.job.service.CommonSubjectFileGenProcessor.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.service.report;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.common.Constants;
import com.hpe.cmwa.service.Dict;
import com.hpe.cmwa.util.ExceptionTool;
import com.hpe.cmwa.util.FileUtil;
import com.hpe.cmwa.util.HelperHttp;
import com.hpe.cmwa.util.MD5;

/**
 * @author 
 * @refactor
 * @date   
 * @version 1.0
 * @see  
 * REVISIONS: 

 * </pre>
 */
@Service
public class CommonSubjectFileGenProcessor extends BaseObject  {
	
	/**
	 * 国信给的加密key
	 */
	private String				key					= "sj2bonc";
    @Autowired
    protected ConcernFileGenService concernFileGenService = null;
    @Autowired
    protected ConcernService	concernService	= null;
    @Autowired
    protected Dict		  dict		  = null;

    protected SimpleDateFormat      sdf		   = new SimpleDateFormat("yyyyMMdd");
    protected SimpleDateFormat      yyyyMMddHHmmssSdf     = new SimpleDateFormat("yyyyMMddHHmmss");

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.cmcc.job.service.IFileGenProcessor#genFile(java.lang.String,java.lang.String, java.lang.String, int, java.util.Map, int, java.util.Map)
     */
    public void genFile(String audTrm, String subjectId,  int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo,
	    Boolean useChineseName, Boolean flag) {

	this.logger.debug("#### 生成文件：subjectId=" + subjectId + ",prvdId=" + prvdId + ",audTrm=" + audTrm);


	try {
		genPrvdFile(audTrm, subjectId,  prvdId, request, modelNotifyId, configInfo, useChineseName, flag);
//	    genAllFile(audTrm, subjectId, totalFocusCds, prvdId, request, modelNotifyId, configInfo, useChineseName);// 生成全国视角的文件doc

	} finally {
	    this.logger.info("#### 模型数据文件生成完毕");
	}
    }

    /**
     * <pre>
     * Desc  额外的验证条件，判断此请求是否需要生成文件 （由继承本类的processor覆盖本方法进行处理）
     * 返回true为验证通过，需要生成文件
     * 返回false为验证失败，不需要生成文件
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @param request
     * @param modelNotifyId
     * @param configInfo
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   20161019
     * </pre>
     */
    protected boolean validateRequest(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo) {
	return true;
    }

 

    /**
     * <pre>
     * Desc 自定义报表的生成
     * </pre>
     */
    public String customGenFile(String audTrm, String subjectId,   int prvdId, Map<String, Object> request, Map<String, Object> configInfo,
    		Boolean useChineseName, Boolean flag) {
    	
    	File docFile = null;
    	//定义变量flags是否需要向国信推送报告信息
    	try {
    		this.logger.debug("#### 生成省文件:subjectId=" + subjectId + ",prvdId=" + prvdId + ",audTrm=" + audTrm);
    		// 生成省Doc文件，更新数据库请求记录状态
    		docFile = this.genProvDocFile(audTrm, subjectId, prvdId, configInfo, request, useChineseName);
    		propertyUtil.getPropValue("tempDir");
    		
    	} catch (Exception e) {
    		throw new RuntimeException("生成文件异常:", e);
    	}
		return propertyUtil.getPropValue("tempDir")+"/"+request.get("fileName").toString();
    }
    
    /**
     * <pre>
     * Desc 
     * </pre>
     */
    public void genPrvdFile(String audTrm, String subjectId,   int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo,
	    Boolean useChineseName, Boolean flag) {

	File docFile = null;
	//定义变量flags是否需要向国信推送报告信息
	boolean flag1 = false;
	try {
	    this.logger.debug("#### 生成省文件:subjectId=" + subjectId + ",prvdId=" + prvdId + ",audTrm=" + audTrm);

		// 生成省Doc文件，更新数据库请求记录状态
		docFile = this.genProvDocFile(audTrm, subjectId, prvdId, configInfo, request, useChineseName);
		if(docFile != null){
			updateFileRequestStatusBySubjectId(modelNotifyId, audTrm, subjectId,  prvdId, request, Constants.Model.FileRequestStatus.DOC_FILE_FINISHED);
			// 向文件生成结果表hpeapm.busi_report_file中插入新生成的文件信息,并返回是否之前已经生成过此文件，从而判断是否需要向国信推送
 		    flag1 = insertReportFile(modelNotifyId, audTrm, subjectId, prvdId,request);
			
		}
	    
	    // 第六步：更新数据库请求记录状态
	    // updateFileRequestStatus(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.File_FINISHED);
	    updateFileRequestStatusBySubjectId(modelNotifyId, audTrm, subjectId,  prvdId, request, Constants.Model.FileRequestStatus.File_FINISHED);
	    //调用国信接口，推送给国信
	    if(flag1){
	    	request.put("report_items", configInfo.get("id"));
	    	informGXTakeData(audTrm,prvdId,request);
	    }
	} catch (Exception e) {
	    throw new RuntimeException("生成文件异常", e);
	} 
    }
    
  //通知国信同步数据
  	public void informGXTakeData(String audTrm, int prvdId, Map<String, Object> request) throws IOException{
  		String report_province= String.valueOf(prvdId);
  		String httpUrl = this.propertyUtil.getPropValue("gxReportUrl");
  		Map<String, Object> params =  new HashMap<String, Object>();
  		String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
  		String id = "hp"+(int)(10000+Math.random()*(99999-10000+1))+timestamp;
  		String report_name = (String)request.get("fileName");
  		String report_items = (String)request.get("report_items");
  		String report_path = (String)request.get("filePath")+report_name;
  		String report_create_persons = propertyUtil.getPropValue("reporter");
  		String report_create_date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());;
  		String validate= id+report_name+report_province+audTrm+audTrm+report_items+report_create_date+report_create_persons+report_path+timestamp+key;
  		String validateStr = MD5.MD5Crypt(validate);
  		params.put("id", id);
  		params.put("report_name",URLEncoder.encode(report_name,"utf-8"));
  		params.put("report_province", prvdId);
  		params.put("report_start_date", audTrm);
  		params.put("report_end_date", audTrm);
  		params.put("report_items", report_items);
  		params.put("report_create_date", report_create_date);
  		params.put("report_create_persons",URLEncoder.encode(report_create_persons,"utf-8") );
  		params.put("report_path",  URLEncoder.encode(report_path,"utf-8"));
  		params.put("timestamp", timestamp);
  		params.put("validateStr", validateStr);
  		String httpsReturnValue ="";
  		try {
  			httpsReturnValue = HelperHttp.getURLContent(httpUrl, params);
  			JSONObject.fromObject(httpsReturnValue);
  		} catch (Exception e) {
  			this.logger.info("推送报告"+report_name+"：",e);
  		}
  	}

    /**
     * <pre>
     * Desc 生成省相关的doc文件（目前项目使用继承方式覆盖本类）
     * 		 这是一个利用"数据库notify表请求记录的字段中配置processor处理类 和 反射机制"实现"生成省相关的doc文件"的方法；
     * 		 如果，看不懂，可以让新定义的processor类继承本类后，覆盖次方法，在通过xml文件配置到job中效果相同；
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @param configInfo
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   20161019
     * </pre>
     */
    public File genProvDocFile(String audTrm, String subjectId,int prvdId, Map<String, Object> configInfo, Map<String, Object> request, Boolean useChineseName) {

	String processor = (String) configInfo.get("processor");
	if (StringUtils.isNotBlank(processor)) {
	    File exportFile = genDocFileByProcessor(audTrm, subjectId,  prvdId, configInfo, useChineseName);
	    return exportFile;
	}
	return null;
    }
    
    public File genProvExcelFile(String audTrm, int prvdId) {

	return null;
    }

    /**
     * <pre>
     * Desc   使用特殊处理器生成word文件  
     * @param processor
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   20161019
     * </pre>
     */
    @SuppressWarnings("rawtypes")
    private File genDocFileByProcessor(String audTrm, String subjectId,  int prvdId, Map<String, Object> configInfo, Boolean useChineseName) {

	String processor = (String) configInfo.get("processor");
	AbstractFileProcessor fileProcessor = null;
	try {
	    Constructor ct = Class.forName(processor).getConstructor();
	    fileProcessor = (AbstractFileProcessor) ct.newInstance();
	    fileProcessor.setAudTrm(audTrm);
	    fileProcessor.setSubjectId(subjectId);
	    fileProcessor.setPrvdId(prvdId);
	    fileProcessor.setConfigInfo(configInfo);

//	    fileProcessor.setTmpFileName(this.buildFileName(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId,  prvdId, useChineseName));
	    fileProcessor.setTmpFilePath(this.buildFtpFilePath(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, prvdId));

	} catch (Throwable e) {
	    logger.error("#### error when make instance of file processor.processor=" + processor + " errorMsg=" + ExceptionTool.getExceptionDescription(e));
	}
	return fileProcessor == null ? null : fileProcessor.execute();
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
	configInfo.put("id", modelNotifyId);
	configInfo.put("status", status);
	concernFileGenService.updateFileGenReqStatusAndTimeById(configInfo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateFileRequestStatusBySubjectId(int modelNotifyId, String audTrm, String subjectId, int prvdId, Map<String, Object> configInfo, int status) {
	configInfo.put("audTrm", audTrm);
	configInfo.put("subjectId", subjectId);
	configInfo.put("prvdId", prvdId);
	configInfo.put("status", status);
	concernFileGenService.updateFileGenReqStatusAndTimeBySubject(configInfo);
    }

   

    /**
     * <pre>
     * Desc  生成全国的审计报告doc
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @param configInfo
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   20161019
     * </pre>
     */
    @Transactional
    protected File genAllDocFile(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, Map<String, Object> request, Boolean useChineseName) {
	return this.genProvDocFile(audTrm, subjectId, prvdId, configInfo, request, useChineseName);
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
     * @return 
     * @refactor GuoXY
     * @date   20161019
     * </pre>
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean insertReportFile(int modelNotifyId, String audTrm, String subjectId, int chinacode,Map<String, Object> request) {

	String subjectName = (String) request.get("subject_name");
	String filePath = (String) request.get("filePath");
	String fileName = (String) request.get("fileName");
	return concernFileGenService.insertReportFile(modelNotifyId, audTrm, subjectId,subjectName, chinacode, filePath, fileName);
	
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
	String tempDir = propertyUtil.getPropValue("tempDir");
	return tempDir;
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
    protected String getLocalFilePath(String audTrm, String subjectName) {
    	StringBuffer tempDir = new StringBuffer(propertyUtil.getPropValue("tempDir"));
    	tempDir.append("/").append(audTrm).append("/").append(subjectName);
    	return tempDir.toString();
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
    protected String buildFtpFilePath(String fileType, String audTrm, String subjectId,  int prvdId) {
	String ftpPath = propertyUtil.getPropValue("ftpPathV2");
	String path = buildRelativePath(audTrm, subjectId, prvdId);
	String finalPath = FileUtil.buildFullFilePath(ftpPath, path);
	logger.debug("#### ftp中文件存储路径为：" + finalPath);
	FileUtil.mkdirs(finalPath);

	return finalPath;
    }

    /**
     * <pre>
     * Desc 生成文件名
     *  中文名：上海_201605_渠道养卡审计清单.csv
     *  	    上海_201605_渠道养卡审计报告.doc
     *  非中文：subjectId_focusCd_YYYYMM_prvdId.csv
     * @param fileType
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @param useChineseName
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   20161019
     * </pre>
     */
    protected String buildFileName(String fileType, String audTrm, String subjectId, String subjectName, int prvdId, Boolean useChineseName) {

	StringBuilder path = new StringBuilder("数据监控_");
	String prvdName = prvdId + "";

	 // 生成中文名审计报告/csv
	prvdName = Constants.MAP_PROVD_NAME.get(prvdId);
	path.append(subjectName).append("_").append(prvdName).append("_").append(audTrm);
	path.append(".doc");
	return path.toString();
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
    protected String buildRelativePath(String audTrm, String subjectId, int prvdId) {

	StringBuilder path = new StringBuilder();
	path.append(audTrm).append("/").append(subjectId).append("/").append(prvdId);

	logger.error("#### buildRelativePath>>>" + path.toString());
	return path.toString();
    }

}
