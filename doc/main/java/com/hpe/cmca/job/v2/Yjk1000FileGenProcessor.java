/**
 * com.hp.cmcc.job.service.Yjk1000FileGenProcessor.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job.v2;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hpe.cmca.finals.Constants;
import com.hpe.cmca.util.CalendarUtils;
import com.hpe.cmca.util.ExceptionTool;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.WordUtil;

/**
 * <pre>
 * @author GuoXY
 * @refactor GuoXY
 * @date   20161019
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  20161019 	   GuoXY 	         1. Created this class.
 * </pre>
 */
public class Yjk1000FileGenProcessor extends CommonSubjectFileGenProcessor {
	// 当前专题生成汇总审计报告所需要"跑完模型的关注点数量"
	private int focusCdsCount = 6;

	@Override
	protected boolean validateRequest(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo) {

		if (prvdId != Constants.ChinaCode) {
			List<Object> concernList = concernFileGenService.selectFinishedConcerns(null, audTrm, subjectId, prvdId);
			if (concernList.size() < focusCdsCount) {// 代表该省的5个有价卡关注点并未都执行完毕，所以不需要生成审计报告
				return false;
			}
		}
		return true;
	}

	@Override
	public File genProvDocFile(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, Map<String, Object> request, Boolean useChineseName) {

		Date currentDate = new Date();
		String docTemplatePath = (String) configInfo.get("docTemplatePath");
		String docTemplate = (String) configInfo.get("docTemplate");
		String category = (String) configInfo.get("focusCategory");

		Map<String, Object> concernInfoMap = concernFileGenService.selectConcernInfosByCode(focusCd);
		int concernId = (Integer) concernInfoMap.get("id");

		Map<String, Object> returnConcernMap = new HashMap<String, Object>();
		returnConcernMap.put("concernName", concernInfoMap.get("name").toString());
		returnConcernMap.put("auditTime", CalendarUtils.buildAuditTimeOfMonth(audTrm));
		returnConcernMap.put("auditCycle", CalendarUtils.buildAuditCycle(audTrm, "yyyy年MM月"));

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);
		params.put("focusCd", focusCd);
		params.put("concernId", concernId);
		params.put("category", category);
		params.put("statCycle", audTrm);
		params.put("provinceCode", prvdId);
		params.put("userCityId", prvdId);
		Map<String, List<Object>> dataMap = concernService.selectPageData(params);


		List<Object> dataInfoListNew = dataMap.remove("reportDataList");
		List<Object> dataInfoList = new ArrayList<Object>();
		//将百分比的数字处理为直到有数字为止
		Map<String,String> listOneMapC = null;
		String numberPrecentOldC = null;
		String numberPrecentNewC = null;
		String amountPercentOldC = null;
		String amountPercentNewC = null;
		for(Object dataObj : dataInfoListNew){					
			listOneMapC = (Map<String,String>)dataObj;
			numberPrecentOldC = listOneMapC.get("qtyPercent");
			numberPrecentNewC = concernService.convert(numberPrecentOldC);
			listOneMapC.put("qtyPercent", numberPrecentNewC);
			
			amountPercentOldC = listOneMapC.get("errPercent");
			amountPercentNewC = concernService.convert(amountPercentOldC);
			listOneMapC.put("errPercent", amountPercentNewC);
			
			dataInfoList.add(listOneMapC);
			//dataMap.put("reportDataList", dataInfoListTp);
		}
		StringBuffer key = new StringBuffer(200);
		
		Map<String, Map<String,String>> reportDataMap = new HashMap<String, Map<String,String>>();
		for (Object dataObj : dataInfoList) {
			@SuppressWarnings("unchecked")
			Map<String,String> concernCode = (Map<String,String>) dataObj;
			key.append("_").append((String) concernCode.get("bizCode"));//像_1001
			reportDataMap.put(key.toString(), concernCode);
			key.setLength(0);//清空key
		}
		dataInfoList.clear();
		
		List<Integer> pointOrder = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5));	
		List<String>  pointOrderCN = new ArrayList<String>(Arrays.asList("一","二","三","四","五","六","七"));	
		Map<String,String> concernTp;
		for(int i=0;i<5;i++){
			concernTp = reportDataMap.get("_100"+String.valueOf(i+1));
			if(concernTp == null||concernTp.get("errQty") == null||"0".equals(concernTp.get("errQty") )){
				
				pointOrder.set(i,0);//违规数量为0的关注点，顺序设为0，之后的每个关注点顺序递减
				for(int j = i+1 ; j < pointOrder.size() ; j++) pointOrder.set(j,pointOrder.get(j)-1);
			}				
		}
		int sum=0;//不为0的关注点数量，则不显示序号，放在第7个位置
		for(int i=0;i<5;i++){
			if(pointOrder.get(i)!=0)
				sum++;
		}
		pointOrder.add(9);//默认有数据
		List<Object> dataListTol = dataMap.get("totalInfo");
		if(dataListTol == null ||!(dataListTol.size()>0) ||"0".equals(((Map<String,String>)dataListTol.get(0)).get("numberSum")))
			pointOrder.set(5,0);
		
		pointOrder.add(sum);//放在第7个位置
		
		for(int i=0;i<pointOrder.size();i++){
			if(pointOrder.get(i)==0)pointOrderCN.set(i, "零");
			if(pointOrder.get(i)==1)pointOrderCN.set(i, "一");
			if(pointOrder.get(i)==2)pointOrderCN.set(i, "二");
			if(pointOrder.get(i)==3)pointOrderCN.set(i, "三");
			if(pointOrder.get(i)==4)pointOrderCN.set(i, "四");
			if(pointOrder.get(i)==5)pointOrderCN.set(i, "五");
			if(pointOrder.get(i)==9)pointOrderCN.set(i, "九");
		}
		
		//将百分比的数字处理为直到有数字为止
		List<Object> dataListT = new ArrayList<Object>();
		Map<String,String> listOneMapT = null;
		String numberPrecentOldT = null;
		String numberPrecentNewT = null;
		String amountPercentOldT = null;
		String amountPercentNewT = null;
		for(Object dataObj : dataListTol){					
			listOneMapT = (Map<String,String>)dataObj;
			numberPrecentOldT = listOneMapT.get("numberPrecent");
			numberPrecentNewT = concernService.convert(numberPrecentOldT);
			listOneMapT.put("numberPrecent", numberPrecentNewT);
			
			amountPercentOldT = listOneMapT.get("amountPercent");
			amountPercentNewT = concernService.convert(amountPercentOldT);
			listOneMapT.put("amountPercent", amountPercentNewT);
			
			dataListT.add(listOneMapT);
			//dataMap.put("reportDataList", dataInfoListTp);
		}
		
		//将百分比的数字处理为直到有数字为止
//		for(Object dataObj : dataListTol){	
//		Map<String,String> listOneMap = (Map<String,String>)dataListTol.remove(0);
//		String numberPrecentOld = listOneMap.get("numberPrecent");
//		String numberPrecentNew = concernService.convert(numberPrecentOld);
//		listOneMap.put("numberPrecent", numberPrecentNew);
//		
//		String amountPercentOld = listOneMap.get("amountPercent");
//		String amountPercentNew = concernService.convert(amountPercentOld);
//		listOneMap.put("amountPercent", amountPercentNew);
//		
//		dataListTol.add(listOneMap);
		dataMap.put("totalInfo", dataListT);
		
		
		List<Object> pointDataListNew = dataMap.remove("pointDataList");
		List<Object> pointDataList = new ArrayList<Object>();
		//将百分比的数字处理为直到有数字为止
		Map<String,String> listOneMapP = null;
		String numberPrecentOldP = null;
		String numberPrecentNewP = null;
		String amountPercentOldP = null;
		String amountPercentNewP = null;
		for(Object dataObj : pointDataListNew){					
			listOneMapP = (Map<String,String>)dataObj;
			numberPrecentOldP = listOneMapP.get("qtyPercent");
			numberPrecentNewP = concernService.convert(numberPrecentOldP);
			listOneMapP.put("qtyPercent", numberPrecentNewP);
			
			amountPercentOldP = listOneMapP.get("errPercent");
			amountPercentNewP = concernService.convert(amountPercentOldP);
			listOneMapP.put("errPercent", amountPercentNewP);
			
			pointDataList.add(listOneMapP);
			//dataMap.put("reportDataList", dataInfoListTp);
		}


		//List<Object> pointDataList = dataMap.remove("pointDataList");
		for (Object dataObj : pointDataList) {
			@SuppressWarnings("unchecked")
			Map<String,String> concernCode = (Map<String,String>) dataObj;
			key.append("_").append((String) concernCode.get("concernCode")).append("_").append((String) concernCode.get("bizCode"));//_1001_100101
			reportDataMap.put(key.toString(), concernCode);
			key.setLength(0);
		}
		dataInfoList.clear();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("pointOrder", pointOrder);
		resultMap.put("pointOrderCN", pointOrderCN);
		resultMap.putAll(dataMap);
		resultMap.put("infoList", reportDataMap);

		resultMap.put("provinceName", getCompanyNameOfProvince(prvdId + ""));
		resultMap.put("concernInfos", returnConcernMap);
		resultMap.put("reporter", propertyUtil.getPropValue("reporter"));
		resultMap.put("fileGenTime", sdf.format(currentDate));
		
		String localFilePath = getLocalFilePath();
		String localFileName = buildFileName(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, focusCd, prvdId, useChineseName);
		try {
			logger.error("The subjectId is :"+subjectId+"-----the data transmitted to the audit report is :"+resultMap);
			File localFile = new WordUtil().write(docTemplatePath, docTemplate, resultMap, localFilePath, localFileName);
			return localFile;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			FileUtil.removeFile(FileUtil.buildFullFilePath(localFilePath, localFileName));
			logger.error("#### 有价卡生成审计报告(  " + localFileName + "  )异常，文件已删除。错误信息为：" + ExceptionTool.getExceptionDescription(e));
			throw new RuntimeException("#### 有价卡生成审计报告异常。", e);
		}

	}

	public int getFocusCdsCount() {
		return focusCdsCount;
	}

	public void setFocusCdsCount(int focusCdsCount) {
		this.focusCdsCount = focusCdsCount;
	} 

	
	
	public void genProvDoc(String audTrm, String subjectId, String focusCd, int prvdId, Boolean useChineseName) {



//		Map<String, Object> concernInfoMap = concernFileGenService.selectConcernInfosByCode(focusCd);
//		int concernId = (Integer) concernInfoMap.get("id");
//
//		Map<String, Object> returnConcernMap = new HashMap<String, Object>();
//		returnConcernMap.put("concernName", concernInfoMap.get("name").toString());
//		returnConcernMap.put("auditTime", CalendarUtils.buildAuditTimeOfMonth(audTrm));
//		returnConcernMap.put("auditCycle", CalendarUtils.buildAuditCycle(audTrm, "yyyy年MM月"));

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);
		params.put("focusCd", focusCd);
//		params.put("concernId", concernId);
		
		params.put("statCycle", audTrm);
		params.put("provinceCode", prvdId);
		params.put("userCityId", prvdId);
		Map<String, List<Object>> dataMap = concernService.selectPageData(params);

		StringBuffer key = new StringBuffer(200);
		List<Object> dataInfoList = dataMap.remove("reportDataList");
		Map<String, Map<String,String>> reportDataMap = new HashMap<String, Map<String,String>>();
		for (Object dataObj : dataInfoList) {
			@SuppressWarnings("unchecked")
			Map<String,String> concernCode = (Map<String,String>) dataObj;
			key.append("_").append((String) concernCode.get("bizCode"));//像_1001
			reportDataMap.put(key.toString(), concernCode);
			key.setLength(0);//清空key
		}
		dataInfoList.clear();

		System.out.println(reportDataMap);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.putAll(dataMap);
		resultMap.put("infoList", reportDataMap);


		


	}
}
