/**
 * com.hpe.cmca.service.SystemLogMgService.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.visitor.functions.Length;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserCompany;
import com.asiainfo.biframe.privilege.IUserPrivilegeService;
import com.asiainfo.biframe.privilege.webosimpl.UserPrivilegeServiceImpl;
import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.SystemLogMgMapper;
import com.hpe.cmca.model.ModelParam;
import com.hpe.cmca.pojo.BgxzData;
import com.hpe.cmca.pojo.SjkgData;
import com.hpe.cmca.pojo.SystemLogControlData;
import com.hpe.cmca.pojo.SystemLogHtmlData;
import com.hpe.cmca.pojo.SystemLogLoginData;
import com.hpe.cmca.pojo.SystemLogModelData;
import com.hpe.cmca.pojo.SystemLogOtapaData;
import com.hpe.cmca.pojo.SystemLogReqmData;
import com.hpe.cmca.pojo.SystemLogUseData;
import com.hpe.cmca.pojo.XqglData;
import com.hpe.cmca.pojo.XqglListData;
import com.hpe.cmca.util.Json;

/**
 * <pre>
 * Desc： 
 * @author   ZhangQiang
 * @refactor ZhangQiang
 * @date     2018-8-8 11:57:15
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2018-8-8 	   ZhangQiang 	         1. Created this class.
 * </pre>
 */
@Service("SystemLogMgService")
public class SystemLogMgService extends BaseObject {

	IUserPrivilegeService service = new UserPrivilegeServiceImpl();
	
    @Autowired
    protected MybatisDao mybatisDao;
            
    /**
     * 
     * @Description: 记录持续审计系统用户，对审计开关功能模块进行的操作的日志信息
     * @param @param request
     * @param @param parameterData 开关实体类
     * @param @param doType
     * @param @param fileType
     * @param @return   
     * @return Integer  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-16
     */
    public Integer addControlLogData(HttpServletRequest request, SjkgData parameterData, String doType, String fileType){
    	
    	Map<String, String> userMap = this.getSessionUserInfo(request);

    	SystemLogControlData systemLogControlData = new SystemLogControlData();
    	
    	systemLogControlData.setRn(null);
    	systemLogControlData.setUserId(userMap.get("userId"));
    	systemLogControlData.setUserNm(userMap.get("userName"));
    	systemLogControlData.setUserPrvdId(userMap.get("userPrvdId"));
    	systemLogControlData.setUserPrvdNm(getPrvdName.PRVDNAMES.get( userMap.get("userPrvdId")) );
    	systemLogControlData.setDepId(userMap.get("depId"));
    	systemLogControlData.setDepNm(userMap.get("depName"));
    	systemLogControlData.setSubjectId(parameterData.getSubjectCode());
    	systemLogControlData.setSubjectNm(parameterData.getSubjectName());
    	systemLogControlData.setAudTrm(parameterData.getAudTrm());
    	systemLogControlData.setAudTrmNm(null);
    	systemLogControlData.setControlTyp(String.valueOf(parameterData.getSwitchType()));
    	// 操作类型：包括新增、打开、关闭，若插入开关则为新增，从状态0-1则为打开，从1-2为打开，反之则为关闭
    	systemLogControlData.setBehavTyp(null);
    	systemLogControlData.setOperateBeforeStatus(null);
    	systemLogControlData.setOperateAfterStatus(String.valueOf(parameterData.getSwitchState()));
    	Date date = new Date();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    	systemLogControlData.setSaveTime(formatDate.format(date.getTime()));
    	
    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
    	
    	return  systemlogmgMapper.addControlLogData(systemLogControlData) ;
    }
    
    /**
     * 
     * @Description: 插入持续审计系统用户，对参数管理功能模块进行的操作的日志信息
     * @param @param mapList
     * @param @param doType
     * @param @param fileType
     * @param @return   
     * @return Integer  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-17
     */
    public Integer addOtapaLogData(HttpServletRequest request,  String subjectId, String doType, String oper){
    	ModelParam modelparam = new ModelParam();
    	modelparam.setThresholdSubjectid(subjectId);
    	return  this.addOtapaLogData(request, modelparam, doType, oper) ;
    }
    
    /**
     * 
     * @Description: 插入持续审计系统用户，对参数管理功能模块进行的操作的日志信息
     * @param @param request
     * @param @param modelparam
     * @param @param doType
     * @param @param oper
     * @param @return   
     * @return Integer  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-20
     */
    public Integer addOtapaLogData( HttpServletRequest request, ModelParam modelparam, String doType,String oper){
    	    	 	
    	Map<String, String> userMap = this.getSessionUserInfo(request);

    	SystemLogOtapaData systemLogOtapaData = new SystemLogOtapaData();
    	
    	// 此时查询配置表，将未出现在上次查询中的阈值配置，更新为新增操作，并插入参数管理日志表。
    	if (oper.equals("insert")) {
    		systemLogOtapaData.setUserId("SYS");
        	systemLogOtapaData.setUserNm("系统管理员");
        	systemLogOtapaData.setBehavTyp("新增");
        	systemLogOtapaData.setUserPrvdId("10000");
        	systemLogOtapaData.setUserPrvdNm("全公司");
        	systemLogOtapaData.setDepId("HPYW");
        	systemLogOtapaData.setDepNm("系统运维");
        	Date date = new Date();
    		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    		systemLogOtapaData.setSaveTime(formatDate.format(date.getTime()));
    		
    		SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
        	return systemlogmgMapper.addNewOtapaLogData(systemLogOtapaData) ;
    	}
    	
    	systemLogOtapaData.setRn(null);
    	systemLogOtapaData.setUserId(userMap.get("userId"));
    	systemLogOtapaData.setUserNm(userMap.get("userName"));
    	systemLogOtapaData.setUserPrvdId(userMap.get("userPrvdId"));
    	systemLogOtapaData.setUserPrvdNm(getPrvdName.PRVDNAMES.get( userMap.get("userPrvdId")) );
    	systemLogOtapaData.setDepId(userMap.get("depId"));
    	systemLogOtapaData.setDepNm(userMap.get("depName"));
    	systemLogOtapaData.setSubjectId(modelparam.getThresholdSubjectid()) ;
    	systemLogOtapaData.setFocusId(modelparam.getThresholdFocusid()) ;
    	systemLogOtapaData.setOtapaCd(modelparam.getOld_ThresholdCode()) ;
    	systemLogOtapaData.setBehavEffOt(modelparam.getOld_ThresholdValue());
    	systemLogOtapaData.setBehavEndOt(modelparam.getThresholdValue()) ;
    	Date date = new Date();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		systemLogOtapaData.setSaveTime(formatDate.format(date.getTime()));
    	systemLogOtapaData.setOperateDoc(modelparam.getReason()) ;
    	String behavTyp = "未知操作" ;
    	if (oper.equals("select")) {
    		systemLogOtapaData.setOperateDoc("访问") ;
    		behavTyp = "查询" ; 
    		systemLogOtapaData.setOtapaCd(null) ;
    	}
    	if (oper.equals("update")) behavTyp = "修改" ; 
    	
    	systemLogOtapaData.setBehavTyp(behavTyp);
    	
    	String operateFields = "" ;
    	try {
    		if (modelparam.getIsNameChanged()) operateFields = operateFields + "/阈值名称" ;
    		if (modelparam.getIsValueChanged()) operateFields = operateFields + "/阈值" ;
    		if (modelparam.getIsEffChanged()) operateFields = operateFields + "/生效时间" ;
    		if (modelparam.getIsEndChanged()) operateFields = operateFields + "/失效时间" ;
    		if ("".equals(operateFields) && !"查询".equals(behavTyp) ) operateFields = "内容未变更" ;
    		if ("".equals(operateFields)) {
    			systemLogOtapaData.setOperateFields(null) ;
    		} else {
    			systemLogOtapaData.setOperateFields(operateFields.substring(1, operateFields.length())) ;
    		}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("addReqmLogData >>>>> " + systemLogOtapaData, e) ;
			logger.error("addReqmLogData >>>>> 插入持续审计系统用户，对参数管理功能模块进行的操作的日志信息") ;
		}
    	logger.error("addOtapaLogData >>>>> " + systemLogOtapaData);
    	
		SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
    	
    	return systemlogmgMapper.addOtapaLogData(systemLogOtapaData) ;
    }
    
    /**
     * 
     * @Description: 插入持续审计系统用户，对需求管理功能模块进行的操作的日志信息
     * @param @param mapList
     * @param @param doType
     * @param @param fileType
     * @param @return   
     * @return Integer  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-20
     */
    public Integer addReqmLogData(HttpServletRequest request,XqglListData xqglListData, String doType, String oper){
    	Map<String, String> userMap = this.getSessionUserInfo(request);
    	SystemLogReqmData systemLogReqmData = new SystemLogReqmData();
    	systemLogReqmData.setRn(null);
    	systemLogReqmData.setUserId(userMap.get("userId"));
    	systemLogReqmData.setUserNm(userMap.get("userName"));
    	systemLogReqmData.setUserPrvdId(userMap.get("userPrvdId"));
    	systemLogReqmData.setUserPrvdNm(getPrvdName.PRVDNAMES.get( userMap.get("userPrvdId")));
    	systemLogReqmData.setDepId(userMap.get("depId"));
    	systemLogReqmData.setDepNm(userMap.get("depName"));
    	systemLogReqmData.setReqmCd(xqglListData.getReqId());
    	systemLogReqmData.setReqmNm(xqglListData.getReqNm());
    	try {
    		
        	if(xqglListData.getReqResultAddr() != null && !"".equals(xqglListData.getReqResultAddr())){
        		systemLogReqmData.setFileNm(xqglListData.getReqResultAddr());
        	} else if(xqglListData.getReqId()!= null && !"".equals(xqglListData.getReqId())){
        		systemLogReqmData.setFileNm(xqglListData.getReqId() + ".zip");
        	}else if( !"查询".equals(oper) ) {
        		systemLogReqmData.setFileNm("未发现文件");
        	}
        	
        	systemLogReqmData.setBehavTyp(oper);
        	Date date = new Date();
    		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        	systemLogReqmData.setSaveTime(formatDate.format(date.getTime()));        	
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("addReqmLogData >>>>> " + systemLogReqmData.toString(), e) ;
			logger.error("addReqmLogData >>>>> 插入持续审计系统用户，对需求管理功能模块进行的操作的日志信息") ;
		}
    	logger.error("addReqmLogData >>>>> " + systemLogReqmData.toString()) ;
    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class) ;
    	return  systemlogmgMapper.addReqmLogData(systemLogReqmData) ;
    }
    /**
     * 
     * @Description: 插入持续审计系统用户，对需求管理功能模块进行的操作的日志信息
     * @param @param request
     * @param @param xqglData
     * @param @param doType
     * @param @param oper
     * @param @return   
     * @return Integer  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-21
     */
    public Integer addReqmLogData(HttpServletRequest request,XqglData xqglData, String doType, String oper){
    	
    	Map<String, String> userMap = this.getSessionUserInfo(request);
    	SystemLogReqmData systemLogReqmData = new SystemLogReqmData();
    	systemLogReqmData.setRn(null);
    	systemLogReqmData.setUserId(userMap.get("userId"));
    	systemLogReqmData.setUserNm(userMap.get("userName"));
    	systemLogReqmData.setUserPrvdId(userMap.get("userPrvdId"));
    	systemLogReqmData.setUserPrvdNm(getPrvdName.PRVDNAMES.get( userMap.get("userPrvdId")));
    	systemLogReqmData.setDepId(userMap.get("depId"));
    	systemLogReqmData.setDepNm(userMap.get("depName"));
    	systemLogReqmData.setReqmCd(xqglData.getReqId());
    	systemLogReqmData.setReqmNm(xqglData.getReqNm());
    	try {
    		if(xqglData.getReqResultAddr() != null && !"".equals(xqglData.getReqResultAddr())){
        		systemLogReqmData.setFileNm(xqglData.getReqResultAddr());
        	} else if(xqglData.getReqAttachAddr() != null && !"".equals(xqglData.getReqAttachAddr())){
        		systemLogReqmData.setFileNm(xqglData.getReqAttachAddr());
        	} else if( !"编辑".equals(oper) && !"新增".equals(oper)){
        		systemLogReqmData.setFileNm(xqglData.getReqId() + ".zip");
        	}
    		
    		if( "新增".equals(oper) ) {
        		systemLogReqmData.setFileNm(null);
        	}  
    		
        	systemLogReqmData.setBehavTyp(oper);
        	Date date = new Date();
    		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        	systemLogReqmData.setSaveTime(formatDate.format(date.getTime()));
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("addReqmLogData >>>>> " + systemLogReqmData, e) ;
			logger.error("addReqmLogData >>>>> 插入持续审计系统用户，对需求管理功能模块进行的操作的日志信息") ;
		}
    	logger.error("addReqmLogData >>>>> " + systemLogReqmData) ;
    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
    	
    	return  systemlogmgMapper.addReqmLogData(systemLogReqmData) ;
    }
    /**
     * 
     * @Description: 插入持续审计系统用户，对需求管理功能模块进行的操作的日志信息
     * @param @param request
     * @param @param reqmId
     * @param @param doType
     * @param @param oper
     * @param @return   
     * @return Integer  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-21
     */
    public Integer addReqmLogData(HttpServletRequest request,String reqmId, String doType, String oper){
    	
    	Map<String, String> userMap = this.getSessionUserInfo(request);
    	SystemLogReqmData systemLogReqmData = new SystemLogReqmData();
    	systemLogReqmData.setRn(null);
    	systemLogReqmData.setUserId(userMap.get("userId"));
    	systemLogReqmData.setUserNm(userMap.get("userName"));
    	systemLogReqmData.setUserPrvdId(userMap.get("userPrvdId"));
    	systemLogReqmData.setUserPrvdNm(getPrvdName.PRVDNAMES.get( userMap.get("userPrvdId")));
    	systemLogReqmData.setDepId(userMap.get("depId"));
    	systemLogReqmData.setDepNm(userMap.get("depName"));
    	systemLogReqmData.setReqmCd(reqmId);
    	systemLogReqmData.setReqmNm(null);
    	systemLogReqmData.setFileNm(null);
    	systemLogReqmData.setBehavTyp(oper);
    	Date date = new Date();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    	systemLogReqmData.setSaveTime(formatDate.format(date.getTime()));
    	
    	logger.error("addReqmLogData >>>>> " + systemLogReqmData) ;
		
    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);

    	return  systemlogmgMapper.addReqmLogOfIdData(systemLogReqmData) ;
    }
    
   /**
    * 
    * @Description: 操作审计报告、审计清单、排名汇总的日志： 查询界面默认的筛选条件list
    * @param @return   
    * @return Map<String,Object>  
    * @throws
    * @author ZhangQiang
    * @date 2018-8-11
    */
    public Map<String, Object>  queryModelTermData(){

    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
    	
    	// 查询数据  系统操作日志-管理界面，查询默认展示筛选项数据
    	List<Map<String, Object>> modelTermSubData = systemlogmgMapper.queryModelTermSubData() ;
    	List<Map<String, Object>> modelTermPrvdData = systemlogmgMapper.queryModelTermPrvdData() ;
	
    	Map<String, Object> modelTermData = new HashMap<String, Object>();
    	
    	modelTermData.put("modelTermSubData", modelTermSubData) ;
    	modelTermData.put("modelTermTmData", systemlogmgMapper.queryModelTermTmData()) ;
    	modelTermData.put("modelTermTypeData", systemlogmgMapper.queryModelTermTypeData()) ;
    	modelTermData.put("modelTermPrvdData", modelTermPrvdData) ;
    	    	
    	return modelTermData ;
    }

     /**
     * 
     * @Description: 操作审计报告、审计清单、排名汇总的日志： 查询界面操作日志表格内容
     * @param @param systemLogHtmlData
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-11
     */
    public List<SystemLogModelData>  queryLogModelTbData(SystemLogHtmlData systemLogHtmlData){
    	
    	try {
    		SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
    		return systemlogmgMapper.queryLogModelTbData(systemLogHtmlData) ;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("queryLogModelTbData >>>>>>>>>" , e);
		}
    	return null ;
    }
    
    /**
     * 
     * @Description: 持续审计系统用户，对审计开关功能模块进行的操作的日志信息： 查询界面默认的筛选条件list 
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-11
     */
    public Map<String, Object>  queryControlTermData(){

    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
    	
    	// 查询数据  系统操作日志-管理界面，查询默认展示筛选项数据
    	List<Map<String, Object>> controlTermSubData = systemlogmgMapper.queryControlTermSubData() ;
	
    	Map<String, Object> controlTermData = new HashMap<String, Object>();
    	
    	controlTermData.put("controlTermSubData", controlTermSubData) ;
    	controlTermData.put("controlTermTmData", systemlogmgMapper.queryControlTermTmData()) ;
    	    	
    	return controlTermData ;
    }
     /**
     * 
     * @Description: 持续审计系统用户，对审计开关功能模块进行的操作的日志信息： 查询界面操作日志表格内容
     * @param @param systemLogHtmlData
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-11
     */
    public Map<String, Object> queryControlTableData(SystemLogHtmlData systemLogHtmlData){

    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
		Map<String, Object> tableDetData = new HashMap<String, Object>();
		//tableDetData.put("jihePointList",jihePointList) ;
		List<SystemLogControlData> controlTableData = systemlogmgMapper.queryControlTableData(systemLogHtmlData) ;
		if (controlTableData.size() > 0){
			tableDetData.put("tableDetData",controlTableData) ;
		}
    	return  tableDetData ;
    }
    
    /**
     * 
     * @Description: 持续审计系统用户，对参数管理功能模块进行的操作的日志信息： 查询界面默认的筛选条件list  
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-11
     */
    public Map<String, Object>  queryOtapaTermData(){

    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
    	
    	// 查询数据  系统操作日志-管理界面，查询默认展示筛选项数据 focus_nm
    	List<Map<String, Object>> otapaTermFocData = systemlogmgMapper.queryOtapaTermFocData() ;
    	List<Map<String, Object>> otapaTermSubData = systemlogmgMapper.queryOtapaTermSubData() ;
    	
    	Map<String, Object> otapaTermData = new HashMap<String, Object>();

    	otapaTermData.put("otapaTermSubData", otapaTermSubData) ;
    	otapaTermData.put("otapaTermFocData", otapaTermFocData) ;

    	    	
    	return otapaTermData ;
    }
    
     /**
     * 
     * @Description: 持续审计系统用户，对参数管理功能模块进行的操作的日志信息： 查询界面操作日志表格内容
     * @param @param systemLogHtmlData
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-11
     */
    public List<SystemLogOtapaData>  queryOtapaTableData(SystemLogHtmlData systemLogHtmlData){

    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
    	
    	return systemlogmgMapper.queryOtapaTableData(systemLogHtmlData) ;
    }
    
    /**
     * 
     * @Description: 持续审计系统用户，对需求管理功能模块进行的操作的日志信息： 查询界面操作日志表格内容
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-11
     */
    public List<SystemLogReqmData>  queryReqmTableData(SystemLogHtmlData systemLogHtmlData){

    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
    	
    	return systemlogmgMapper.queryReqmTableData(systemLogHtmlData) ;
    }
    /**
     * 
     * @Description: 持续审计系统用户，对需求管理功能模块进行的操作的日志信息： 查询界面默认的筛选条件list 
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-11
     */
    public Map<String, Object>  queryReqmTermData(){


    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
    	List<Map<String, Object>> reqmTermlist = systemlogmgMapper.queryReqmTermData() ;
    	// 查询数据  系统操作日志-管理界面，查询默认展示筛选项数据
    	Map<String, Object> reqmTermData = new HashMap<String, Object>();

    	reqmTermData.put("reqmTermData", reqmTermlist) ;
    	    	
    	return reqmTermData ;
    }
    
    /**
     * 
     * @Description: 系统操作日志-管理界面，查询默认展示筛选项数据 queryUseTermData
     * @param @param systemLogHtmlData
     * @param @return   
     * @return List<Map<String,Object>>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-8
     */
    public Map<String, Object>  queryUseTermData(SystemLogHtmlData systemLogHtmlData){
    	   	
    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
    	
    	// 查询数据  系统操作日志-管理界面，查询默认展示筛选项数据
    	List<Map<String, Object>> logUseTermList = systemlogmgMapper.queryUseTermData(systemLogHtmlData) ;
    	// 查询数据  系统操作日志-管理界面，查询默认展示筛选项数据--功能菜单list 
    	List<Map<String, Object>> logUseToolList = systemlogmgMapper.queryUseToolData(systemLogHtmlData) ;	
    	List<String> logUseToolNewList = new ArrayList<String>();
    	try {
    		if (logUseToolList != null) {
        		for (Map<String, Object> str : logUseToolList) {
        			if (str.get("behav_lvlist") != null) {
                		logUseToolNewList.add((String)str.get("behav_lvlist")) ;
    				}
        		}
    		}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("queryUseTermData >>>>> " + systemLogHtmlData , e);
			logger.error("queryUseTermData >>>>> " + systemLogHtmlData);
		}
    	
    	logger.error("queryUseTermData >>>>> " + systemLogHtmlData);
    	
    	Map<String, Object> logUseTermData =  this.getListName(logUseTermList) ;
    	logUseTermData.put("logUseToolData", logUseToolNewList);
    	
    	return logUseTermData;
    }
            
    /**
     * 
     * @Description:  // 系统操作日志-管理界面，查询展示table数据 queryUseTableData
     * @param @param systemLogHtmlData
     * @param @return   
     * @return List<SystemLogUseData>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-8
     */
    public List<SystemLogUseData>  queryUseTableData(SystemLogHtmlData systemLogHtmlData){
 
    	// 查询数据 系统操作日志-管理界面，查询展示table数据
    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
		List<SystemLogUseData> logUseList = systemlogmgMapper.queryUseTableData(getQueryUseToolData(systemLogHtmlData)) ;

		return logUseList;
    }
    

    /**
     * 
     * @Description: 统操作日志-管理界面,通过字符解析菜单
     * @param @param systemLogHtmlData
     * @param @return   
     * @return List<Map<String,Object>>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-8
     */
    public SystemLogHtmlData  getQueryUseToolData(SystemLogHtmlData systemLogHtmlsData ){
	   
    	String toolName = systemLogHtmlsData.getToolName();
    	try {
    		if (toolName != null && ! "".equals(toolName) ) {
        		List<String> toolnm = Arrays.asList(toolName.split("/")) ;
        		if (toolnm.size() == 1) {
        			systemLogHtmlsData.setBehavLv1(toolnm.get(0));
    			}
        		if (toolnm.size() == 2) {
        			systemLogHtmlsData.setBehavLv1(toolnm.get(0));
        			systemLogHtmlsData.setBehavLv2(toolnm.get(1));
    			}
        		if (toolnm.size() == 3) {
        			systemLogHtmlsData.setBehavLv1(toolnm.get(0));
        			systemLogHtmlsData.setBehavLv2(toolnm.get(1));
        			systemLogHtmlsData.setBehavLv3(toolnm.get(2));
    			}
    		}else {
    			systemLogHtmlsData.setToolName(null);
    		}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getQueryUseToolData >>>>> " + systemLogHtmlsData,e);
			logger.error("getQueryUseToolData >>>>> " + systemLogHtmlsData);
		}   	
    	logger.error("getQueryUseToolData >>>>> " + systemLogHtmlsData);
    	
    	return systemLogHtmlsData;
    }
    
    
    /**
     * 
     * @Description: 登陆日志管理界面，查询默认展示数据
     * @param @param systemLogHtmlData
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-5
     */
    public Map<String, Object> queryDefaultData(SystemLogHtmlData systemLogHtmlData) {
				
    	// 查询数据
    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
		List<Map<String, Object>> termList = systemlogmgMapper.queryTermData(systemLogHtmlData) ;

		// 创建及调用公共方法处理
		return this.getListName(termList);

    }
        
    /**
     * 
     * @Description: 系统日志查询页面-条件查询返回Table结果
     * @param @param systemLogHtmlData
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-7
     */
    public Map<String, Object> queryLoginTableData(SystemLogHtmlData systemLogHtmlData) {   	
    	
    	// 查询数据
    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
    	List<SystemLogLoginData> defLogList = systemlogmgMapper.queryDefLogData(systemLogHtmlData) ;
 
		Map<String, Object> defLogData = new HashMap<String, Object>();
		defLogData.put("defLoglist", defLogList); 
		
		return defLogData;
		
    }
    
    /**
     * 
     * @Description: 通过专题id获取月份list
     * @param @param systemLogHtmlData
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-23
     */
    public Map<String, Object> getAudtrmOfId(SystemLogHtmlData systemLogHtmlData){
    	
    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
    	List<Map<String, Object>> audTrmList = systemlogmgMapper.getAudtrmOfId(systemLogHtmlData) ;
    	
    	Map<String, Object> modelTermTmData = new HashMap<String, Object>();
    	
    	try {
			for (Map<String, Object> map : audTrmList) {
				String aud_trm_min = (String)map.get("aud_trm_min") ;
		    	Integer aud_trm_us = (Integer)map.get("aud_trm_us") ;
				if (aud_trm_min != null && aud_trm_us != null) {
					modelTermTmData.put("modelTermTmData", this.getMonthBetween(aud_trm_min, 
							Integer.valueOf(aud_trm_us))) ;					
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getAudtrmOfId  >>>>>>>>>>>>> audTrmList=" + audTrmList,e);
		}
    	
    	return modelTermTmData ;
    }
    
    /**
     * 
     * @Description: 通过专题id获取关注点list
     * @param @param systemLogHtmlData
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-23
     */
    public Map<String, Object> getFocusBySubId(SystemLogHtmlData systemLogHtmlData){
    	
    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
    	List<Map<String, Object>> otapaTermFocData = systemlogmgMapper.getFocusBySubId(systemLogHtmlData) ;
    	Map<String, Object> otapaTermData = new HashMap<String, Object>();

    	otapaTermData.put("otapaTermFocData", otapaTermFocData) ;
    	
    	return otapaTermData ;
    }
    
    /**
     * 
     * @Description: insert 用户动作
     * @param @param userBehavData
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-7
     */
    public Integer addLoginInfoData(HttpServletRequest request) {   
    	
    	try {
			if ("0".equals(this.checkLogin(request))) {
				return 0;	
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("addLoginInfoData >>>>> " + e);
		}
    	
		HttpSession session = request.getSession();
		String userId= (String) session.getAttribute("userId");
		String userNm = (String) session.getAttribute("userName");
		String userPrvdId = (String) session.getAttribute("userPrvdId");
		Integer depId = (Integer) session.getAttribute("depId");
		String depNm = (String) session.getAttribute("depName");
		
		SystemLogLoginData systemLogLoginData = new SystemLogLoginData() ;
		
		Date date = new Date();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		systemLogLoginData.setBehavTime(formatDate.format(date.getTime()));;
		if(depId == null)systemLogLoginData.setDepId(null);else systemLogLoginData.setDepId(depId.toString());
		systemLogLoginData.setDepNm(depNm);
		String ip = this.getIPAddress(request);// 获取客户端IP
		systemLogLoginData.setPcId(ip);
		systemLogLoginData.setUserId(userId);
		systemLogLoginData.setUserNm(userNm);
		systemLogLoginData.setUserPrvdId(userPrvdId);
		systemLogLoginData.setLoginCnt(1);
		
		logger.error("addLoginInfoData >>>>> " + systemLogLoginData);
		
		// 插入数据
    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
    	Integer status = systemlogmgMapper.addLoginInfoData(systemLogLoginData) ;
		
		
		return status;	
    }
    
    /**
     * 
     * @Description: 登录生失效判断
     * @param @param request
     * @param @return
     * @param @throws ParseException   
     * @return String  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-15
     */
    public String checkLogin(HttpServletRequest request) throws ParseException {
		Map<String, Object> data = new HashMap<String, Object>();
		IUser user = (IUser) request.getSession().getAttribute("ssoUSER");
		if (user == null) {
		    data.put("islogin", "0");// 未登录/登录已失效
		} else {
		    data.put("islogin", "1");// 已登录
		}
		return Json.Encode(data);
    }
    
    /**
     * 
     * @Description: 判断登录的用户是否有查看页面的权限
     * @param @param request
     * @param @return   
     * @return Boolean  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-15
     */
     public Boolean getUserRzcxRight(HttpServletRequest request){
 		
    	HttpSession session = request.getSession();
    	String userPrvdId = (String) session.getAttribute("userPrvdId");
 		Integer depId = (Integer) session.getAttribute("depId");
 		
 		Boolean blean = true ;
 		
 		if (("10000".equals(userPrvdId) || userPrvdId == "10000" ) && (depId==12 || depId==18 )) {
 			blean = false ;
		}
 		
 		return blean ; 	
     }
    
     /**
      * 
      * @Description: 获取当前Session中的Users
      * @param @param request
      * @param @return   
      * @return Map<String,String>  
      * @throws
      * @author ZhangQiang
      * @date 2018-8-16
      */
     public Map<String, String> getSessionUserInfo(HttpServletRequest request){
    	
    	IUser user = (IUser) request.getSession().getAttribute("ssoUSER");
 		   	 
    	Map<String, String> userMap = new HashMap<String, String>();
    	HttpSession session = request.getSession();
    	userMap.put("userId", (String) session.getAttribute("userId"));
    	userMap.put("userName", (String) session.getAttribute("userName"));
    	
    	if (user == null) {
    		userMap.put("userId", "unknown");
    		userMap.put("userName", "未登录");
 		} 
    	userMap.put("userPrvdId", (String) session.getAttribute("userPrvdId"));
    	Integer depId =  (Integer) session.getAttribute("depId") ;
    	userMap.put("depId", String.valueOf(depId));
    	userMap.put("depName", (String) session.getAttribute("depName"));
    	
		return userMap;
    	 
     }
     /**
      * 
      * @Description: 通过用户Id获取用户属性
      * @param @param userId
      * @param @return
      * @param @throws Exception   
      * @return Map<String,String>  
      * @throws
      * @author ZhangQiang
      * @date 2018-8-22
      */
     public Map<String, String> getUserInfo(String userId) throws Exception{
    	 
    	 if (userId == null || "".equals(userId)) {
    		 return null ;
		}
    	 
    	 Map<String, String> userMap = new HashMap<String, String>();
    	 IUserCompany dep = service.getUserDept(userId);
    	 Integer depId = dep.getDeptid();
    	 String depName = dep.getTitle();
    	 IUser user = service.getUser(userId);
    	 String userName = user.getUsername();
    	 String prvdId=service.getUserCurrentCity(userId);	
    	 userMap.put("userId", userId);
    	 userMap.put("userName", userName);
    	 userMap.put("userPrvdId", prvdId);
    	 userMap.put("userPrvdNm", getPrvdName.PRVDNAMES.get(prvdId));
    	 userMap.put("depId", String.valueOf(depId));
    	 userMap.put("depName", depName);
    	 return userMap ;
     }
   /**
    * 
    * @Description: 处理前端传递的参数 -- 共用方法（暂时未用）
    * @param @param behavTimeSd
    * @param @param behavTimeEd
    * @param @param userIds
    * @param @param userPrvdIds
    * @param @param depIds
    * @param @return   
    * @return SystemLogHtmlData  
    * @throws
    * @author ZhangQiang
    * @date 2018-8-8
    */
    public SystemLogHtmlData getHtmlTerm(String behavTimeSd,String behavTimeEd ,String userIds,String userPrvdIds,String depIds){
    	
    	SystemLogHtmlData systemLogHtmlsData = new SystemLogHtmlData();
    	try {
        	if("".equals(userIds) ||  userIds == null )systemLogHtmlsData.setUserIds(null);else systemLogHtmlsData.setUserIds(Arrays.asList(userIds.split(",")));
    		if("".equals(userPrvdIds) ||  userPrvdIds == null )systemLogHtmlsData.setUserPrvdIds(null);else systemLogHtmlsData.setUserPrvdIds(Arrays.asList(userPrvdIds.split(",")));
    		if("".equals(depIds) ||  depIds == null )systemLogHtmlsData.setDepIds(null);else systemLogHtmlsData.setDepIds(Arrays.asList(depIds.split(",")));
    		if("".equals(behavTimeSd) ||  behavTimeSd == null )systemLogHtmlsData.setBehavTimeSd(null);else systemLogHtmlsData.setBehavTimeSd(behavTimeSd);
    		if("".equals(behavTimeEd) ||  behavTimeEd == null )systemLogHtmlsData.setBehavTimeEd(null);else systemLogHtmlsData.setBehavTimeEd(behavTimeEd);
    		
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getHtmlTerm >>>>> " + systemLogHtmlsData,e);
		}

		logger.error("getHtmlTerm >>>>> " + systemLogHtmlsData);
		
    	return systemLogHtmlsData ;    	
    }
    
    /**
     * 
     * @Description: 为用户list、部门list、公司list分类 - 共用方法
     * @param @param termList
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-8
     */
    public Map<String, Object> getListName(List<Map<String, Object>> termList){
    	
    	if (termList == null) {
			return null ;
		}
		
		// 分组接收处理数据
		List<Map<String, Object>> lgvalueslist1 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lgvalueslist2 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lgvalueslist3 = new ArrayList<Map<String, Object>>();
		Map<String, Object> termData = new HashMap<String, Object>();
		
		// 分类
		for (Map<String, Object> mapvalues : termList) {
			try {
				if( "1".equals(mapvalues.get("typeId").toString())  ){
					
					Map<String, Object> mapvaluesMap = new HashMap<String, Object>();
					mapvaluesMap.put("userId", mapvalues.get("user_id").toString());
					mapvaluesMap.put("userNm", mapvalues.get("user_nm").toString());
					lgvalueslist1.add(mapvaluesMap);
				}
				if( "2".equals(mapvalues.get("typeId").toString()) ){

					Map<String, Object> mapvaluesMap = new HashMap<String, Object>();

					mapvaluesMap.put("userPrvdId", mapvalues.get("user_id").toString());
					mapvaluesMap.put("userPrvdNm", mapvalues.get("user_nm").toString());
					
					lgvalueslist2.add(mapvaluesMap);
				}
				if( "3".equals(mapvalues.get("typeId").toString()) ){

					Map<String, Object> mapvaluesMap = new HashMap<String, Object>();
					mapvaluesMap.put("depId", mapvalues.get("user_id").toString());
					mapvaluesMap.put("depNm", mapvalues.get("user_nm").toString());
					
					lgvalueslist3.add(mapvaluesMap);
				}
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("getListName >>>>> 为用户list、部门list、公司list分类 - 共用方法" ,e);
			}				
		}
		// 加载
		termData.put("userNmList", lgvalueslist1);
		termData.put("prvdNmList", lgvalueslist2);
		termData.put("depNmList", lgvalueslist3);
		 		
		return termData;
    }
    
    /**
     * 
     * @Description: 获取List<Map<String, Object>> values的list
     * @param @param mapListData
     * @param @param keys
     * @param @return   
     * @return List<String>  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-11
     */
    public List<String> getListOfMap(List<Map<String, Object>> mapListData , String keys){
    	
    	List<String> modelTermSublist = new ArrayList<String>() ;
    	try {
    		if (mapListData != null) {
        		for (Map<String, Object> str : mapListData) {
        			modelTermSublist.add((String)str.get(keys)) ;
        		}
    		}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getListOfMap >>>>> 获取List<Map<String, Object>> values的list" ,e);
		}
    	
    	return modelTermSublist ;
    }
    
    /**
     * 
     * @Description: 获取用户IP地址
     * @param @param request
     * @param @return   
     * @return String  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-20
     */
    public String getIPAddress(HttpServletRequest request) {
    	
        String ipAddress = request.getHeader("x-forwarded-for");  
        try {
        	if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getRemoteAddr();  
                if(ipAddress.equals("127.0.0.1") || ipAddress.equals("10.248.12.24") || ipAddress.equals("10.255.219.198") 
                		|| ipAddress.equals("0:0:0:0:0:0:0:1")){  
                    //根据网卡取本机配置的IP  
                    InetAddress inet=null;  
                    try {  
                        inet = InetAddress.getLocalHost();  
                    } catch (UnknownHostException e) {  
                        e.printStackTrace();  
                    }  
                    ipAddress= inet.getHostAddress();  
                }  
            }  
            //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
            if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15  
                if(ipAddress.indexOf(",")>0){  
                    ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
                }  
            }  
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getIPAddress >>>>> 获取用户IP地址" ,e);
		}
        
        logger.error("getIPAddress >>>>> 获取用户IP地址：" + ipAddress);
        
        return ipAddress;   
    }

    /**
     * 
     * ClassName: getPrvdName 
     * @Description: 省份标识-省份名称Map
     * @author ZhangQiang
     * @date 2018-8-16
     */
    static class getPrvdName{
    	
    	public static final Map<String,String> PRVDNAMES = new HashMap<String , String>(){/**
			 * @Fields serialVersionUID : TODO
			 */
			private static final long serialVersionUID = 1L;

		{
			put("10000","全公司");
			put("10100","北京");
			put("10200","上海");
			put("10300","天津");
			put("10400","重庆");
			put("10500","贵州");
			put("10600","湖北");
			put("10700","陕西");
			put("10800","河北");
			put("10900","河南");
			put("11000","安徽");
			put("11100","福建");
			put("11200","青海");
			put("11300","甘肃");
			put("11400","浙江");
			put("11500","海南");
			put("11600","黑龙江");
			put("11700","江苏");
			put("11800","吉林");
			put("11900","宁夏");
			put("12000","山东");
			put("12100","山西");
			put("12200","新疆");
			put("12300","广东");
			put("12400","辽宁");
			put("12500","广西");
			put("12600","湖南");
			put("12700","江西");
			put("12800","内蒙古");
			put("12900","云南");
			put("13000","四川");
			put("13100","西藏");
    	}};
    	
    }
    
	//获取指定日期之间的所有年月
    private List<Map<String, Object>> getMonthBetween(String minDate, int tmus) throws ParseException {   	

    	List<Map<String, Object>> audTrmList = new ArrayList<Map<String,Object>>();
    	
    	minDate = minDate.substring(0,4)+"-"+minDate.substring(4) + "-01" ;  	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月
    	String maxDate =sdf.format(new Date()) + "-01";
    	
    	Calendar min = Calendar.getInstance();
    	Calendar max = Calendar.getInstance();

    	min.setTime(sdf.parse(minDate));
    	min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

    	max.setTime(sdf.parse(maxDate));
    	max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

    	Calendar curr = min;

    	List<String> tmList = new ArrayList<String>() ;
    	while (curr.before(max)) {
    		tmList.add(sdf.format(curr.getTime())) ;	
    		curr.add(Calendar.MONTH, 1);
    	}
    	
    	int i = tmList.size() ;
    	for (String tm : tmList) {
    		if (tmus < i) {
    			Map<String, Object>  audTrmListMap = new HashMap<String, Object>();
    			audTrmListMap.put("aud_trm", tm.substring(0,4)+tm.substring(5));
    			audTrmListMap.put("aud_trm_nm", tm.substring(0,4)+"年"+tm.substring(5)+"月");
    			audTrmList.add(audTrmListMap);
    			logger.error("getMonthBetween >>>>>>>>>>>>>>>>>>>>>>> " + tm) ;
			}
    		i-- ;
		}
	
    	List<Map<String, Object>> audTrmListNew = new ArrayList<Map<String,Object>>();
    	int size = audTrmList.size() ;
    	// 排序
    	for (int j = 1; j < size+1; j++) {
    		audTrmListNew.add(audTrmList.get(size-j)) ;
		}
   	
    	return audTrmListNew;
    	}
    }
