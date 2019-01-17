/**
 * com.hp.bmcc.job.SmsJob.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.job;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.auditTask.service.job.ModelConclusionJobService;
import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.util.HelperHttp;
import com.hpe.cmwa.util.MD5;
import com.hpe.cmwa.util.VariantTool;

/**
 * <pre>
 * Desc： 模型结论推送任务
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
@Service("modelConclusionJob")
public class ModelConclusionJob extends BaseObject {
	
	//推送成功状态 0-未推送 1-已推送 2-推送异常
	private static int PUSH_SUCCESS = 1;
	//推送失败
	private static int PUSH_FAIL = 2;
	//未推送
	private static int NOT_PUSH = 0;
	
	//0-未处理 1-处理中 2-处理完成 3-处理异常
	private static int NOT_DO = 0;
	
	private static int DO_ING = 1;
	
	private static int DO_SUCCESS = 2;
	
	private static int DO_ERROR = 3;
	
	/**
	 * 国信给的加密key
	 */
	private String				key					= "sj2bonc";
	
	@Autowired
	private ModelConclusionJobService modelConclusionJobService;
	
	
	public void taskWork() throws Exception{
		String intoType = "taskWork";
		List<Map<String, Object>> auditTaskInfoShareList = modelConclusionJobService.selectAuditTaskInfoShare();
		List<Map<String, Object>> modelFinishedAuditTaskList = modelConclusionJobService.selectModelFinishedAuditTask();
		for(Map<String, Object> shareMap : auditTaskInfoShareList){
			if(!modelFinishedAuditTaskList.contains(shareMap)){
				pushAndSqlResult(shareMap,intoType);
				shareMap.put("status", "s");
				modelConclusionJobService.insetModelFinishedAuditTask(shareMap);
			}
		}
		
	}
	
	/**
	 * <pre>
	 * Desc  国信的url
	 * http://10.255.219.165:8321/frame_sj/setModelConAuth.action?auditId=as90&taskCode=jzsj030&modelConclusion=结论&validateStr=F57DB0C93BB3DD5792EAF252C3D0BDF5&timestamp=20161025085432&extParam= 
	 * @author peter.fu
	 * Dec 1, 2016 1:10:53 PM
	 * </pre>
	 * @throws Exception 
	 */
	public void work() throws Exception{

		this.logger.info("开始处理模型结论推送");
		String intoType = "work";
		// 1.查询hpeadm.model_file_notification获取尚未处理的文件加载完成通知
		List<Map<String, Object>> IntfCodeList = modelConclusionJobService.selectIntfCodes();
		
		// 2.根据文件的接口单元编码去hpeadm.model_file_audit_mapping查询对应的审计点(一个审计点对应多个接口单元编码文件)
		if(IntfCodeList!=null && IntfCodeList.size()>0){
			List<Map<String, Object>> auditIdList = modelConclusionJobService.selectauditIds(IntfCodeList);
			// 3.根据审计点去audit_db.audit_task_info_share查询对应的任务信息列表
			for(Map<String, Object> auditMappingMap : auditIdList ){
				List<Map<String, Object>> auditTaskInfoShareList = modelConclusionJobService.selecAuditTaskInfoShare(auditMappingMap);
				// 4.轮训任务列表生成对应的审计点的结论
				for(Map<String, Object> map : auditTaskInfoShareList){
					pushAndSqlResult(map,intoType);
				}
			}
			this.logger.info("模型结论推送处理完毕");
		}
	}

	private void pushAndSqlResult(Map<String, Object> map,String intoType) throws Exception {
		// 4.1 根据任务信息和审计点信息，查询模型结论配置表hpeadm.model_conclusion_config，进而生成新的模型结论，可以考虑dict_common缓存的应用
		List<Map<String, Object>> list = modelConclusionJobService.selectModelConclusionConfig(map.get("auditId").toString());
		for(Map<String, Object> modleConconfigMap : list){
			//4.1.1 进而生成新的模型结论
			Map<String, Object> allResult = getTaskAllSqlResult(modleConconfigMap,map);			
			String modelConclusion= modleConconfigMap.get("conclusion").toString();
			String finalConclusion = VariantTool.eval(modelConclusion, allResult, null);
			//finalConclusion = new String(finalConclusion.getBytes("GBK"),"UTF-8");
			// 4.2 推送模型结论给国信(httpClient)(参数：auditId , taskCode , modelConclusion , validateStr , timestamp , extParam)
			pushConclusionToGX(map, modelConclusion, finalConclusion,intoType);
		}
	}


	
	private Map<String, Object> getTaskAllSqlResult(
			Map<String, Object> modleConconfigMap,Map<String, Object> map) throws IOException {
		Map<String,Object> allResult = new HashMap<String,Object>();
		
		String sqlConfig = modleConconfigMap.get("sql").toString();				
		Properties props = new Properties();
		props.load(new ByteArrayInputStream(sqlConfig.getBytes()));
		Iterator<Entry<Object,Object>> iter=props.entrySet().iterator();
		while(iter.hasNext()){
			Entry<Object,Object> entry= iter.next();
			String sqlId = entry.getValue().toString();
			Map<String, Object> sqlResultMap = modelConclusionJobService.getResult(sqlId,map);//mybatisDao.get(sqlId,params);
			if(sqlResultMap != null && !sqlResultMap.isEmpty()){
				allResult.putAll(sqlResultMap);
			}
		}
		return allResult;
	}

	private void pushConclusionToGX(Map<String, Object> map,
			String modelConclusion, String finalConclusion,String intoType) throws Exception {
		// http://10.255.219.165:8321/frame_sj/setModelConAuth.action?auditId=as90&taskCode=jzsj030&modelConclusion=结论&validateStr=F57DB0C93BB3DD5792EAF252C3D0BDF5&timestamp=20161025085432&extParam=
		//String httpsUrl = "http://10.255.219.165:8321/frame_sj/setModelConAuth.action";
		Map<String, Object> mapInfo = new HashMap<String, Object>();
		mapInfo.put("auditId", map.get("auditId"));
		mapInfo.put("taskCode", map.get("taskCode"));
		mapInfo.put("timestamp", map.get("timestamp"));
		mapInfo.put("validateStr", map.get("validateStr"));
		mapInfo.put("timestamp", map.get("timestamp"));
		mapInfo.put("extParam", map.get("extParam"));
		finalConclusion = finalConclusion.trim().replaceAll("%", "%BBB");
		mapInfo.put("modelConclusion", URLEncoder.encode(finalConclusion,"UTF-8"));
		mapInfo.put("intoType", intoType);
		
		String httpUrl = this.propertyUtil.getPropValue("gxUrl");
		Map<String, Object> params =  new HashMap<String, Object>();
		params.put("auditId", mapInfo.get("auditId"));
		params.put("taskCode", mapInfo.get("taskCode"));
		params.put("modelConclusion", URLEncoder.encode(finalConclusion,"UTF-8"));
		if(mapInfo.get("timestamp") == null || mapInfo.get("timestamp")== "" ){
			String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
			mapInfo.remove("timestamp");
			mapInfo.put("timestamp", timestamp);
		}
		String validateStr = MD5.MD5Crypt(mapInfo.get("auditId").toString()+ mapInfo.get("taskCode").toString()+ finalConclusion +mapInfo.get("timestamp")+key);
		params.put("validateStr", validateStr);
		params.put("timestamp", mapInfo.get("timestamp"));
		params.put("extParam", mapInfo.get("extParam"));
		
		String pushResult = "";
		String httpsReturnValue ="";
		
		try {
			httpsReturnValue = HelperHttp.getURLContent(httpUrl, params);
			JSONObject jsonObject = JSONObject.fromObject(httpsReturnValue);
			pushResult = insertLogAndUpdatePushStatus(map,mapInfo, finalConclusion,
					pushResult, jsonObject);
		} catch (Exception e) {
			this.logger.info(map.get("auditId")+"：推送结果:"+ httpsReturnValue+"====插入结果："+pushResult,e);
		}
	}
	
	
	private String insertLogAndUpdatePushStatus(Map<String, Object> map,Map<String, Object> mapInfo,
			String finalConclusion, String pushResult, JSONObject jsonObject)
			throws UnsupportedEncodingException {
		Map<String, Object> logMap = new HashMap<String, Object>();
		int pushStatus = 0;
		int doStatus = 0;
		if(jsonObject.get("resultCode").equals("00")){
			// 4.3 记录模型推送日志到hpeadm.model_conclusion_log
			logMap.put("status", PUSH_SUCCESS);
			pushStatus = PUSH_SUCCESS;
			doStatus = DO_SUCCESS;
			pushResult = "推送成功";
		}else if(jsonObject.get("resultCode").equals("01")){
			logMap.put("status", PUSH_FAIL);
			pushStatus = PUSH_FAIL;
			doStatus = DO_ERROR;
			pushResult = "推送失败";
		}else{
			logMap.put("status", NOT_PUSH);
			pushStatus = NOT_PUSH;
			pushResult="noResult";
		}
		if(pushStatus !=0){
			logMap.put("fileNotificationId", map.get("id"));
			logMap.put("auditId", mapInfo.get("auditId"));
			logMap.put("taskCode", mapInfo.get("taskCode"));
			finalConclusion =finalConclusion.replaceAll("%BBB", "%");
			logMap.put("modelConclusion",finalConclusion);
			logMap.put("timestamp", mapInfo.get("timestamp"));
			logMap.put("extParam", mapInfo.get("extParam"));
			logMap.put("create_time", new Date());
			modelConclusionJobService.insetConclusionLog(logMap);
			if(mapInfo.get("intoType")=="work"){
				Map<String, Object> updateStatus = new HashMap<String, Object>();
				updateStatus.put("status", doStatus);
				updateStatus.put("id", map.get("id"));
				modelConclusionJobService.updateModelFileNotification(updateStatus);
			}
		}
		return pushResult;
	}
}
