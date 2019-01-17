package com.hpe.cmwa.auditTask.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.auditTask.service.job.ModelNotifyGenService;
import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.common.Constants;

/**
 * <pre>
 * Desc： 每月月初一号定时向busi_model_notify表中生成并插入当月需要生成报告的记录
 * @author ren yx
 * @refactor ren yx
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Mar 9, 2015 	   ren yx	         1. Created this class. 
 * </pre>
 */
@Service("modelNotifyGenJob")
public class ModelNotifyGenJob extends BaseObject{
	
	protected SimpleDateFormat      sdf		   = new SimpleDateFormat("yyyyMM");
	@Autowired
	private ModelNotifyGenService modelNotifyGenService;
	
	public void work(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
        c.add(Calendar.MONTH, -2);
        Date m3 = c.getTime();
		String currentDate = sdf.format(m3);
		List<String> existsAudTrms =modelNotifyGenService.getExistsAudTrm();
		if(!existsAudTrms.contains(currentDate)){
			genDataAndPutResult(currentDate);
		}
	}
	/**
	 * 组织数据插入到busi_model_notify
	 */
	public void genDataAndPutResult(String currentDate){
		List<Map> ResultData = new ArrayList<Map>();
		
		Set<String> subjectIds = Constants.MAP_SUBJECT_NAME.keySet();
		for (String subjectId : subjectIds) {
			String subjectNm = Constants.MAP_SUBJECT_NAME.get(subjectId);
			Set<Integer> prvdIds =  Constants.MAP_PROVD_NAME.keySet();
			for (Integer prvdId : prvdIds) {
				Map<String,Object> sujectMap = new HashMap<String, Object>();
				sujectMap.put("audTrm", currentDate);
				sujectMap.put("prvdId", prvdId);
				sujectMap.put("status", 0);
				sujectMap.put("subjectId", subjectId);
				sujectMap.put("subjectNm", subjectNm);
				ResultData.add(sujectMap);
//				sujectMap.clear();
			}
		}
		putDataToBusiModelNotify(ResultData);
	}
	
	public void putDataToBusiModelNotify(List<Map> ResultData){
		modelNotifyGenService.insertNewData(ResultData);
	}
	
}