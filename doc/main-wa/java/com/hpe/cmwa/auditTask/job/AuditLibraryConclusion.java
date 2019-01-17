package com.hpe.cmwa.auditTask.job;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.auditTask.service.job.ModelConclusionJobService;
import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.util.VariantTool;

@Service
public class AuditLibraryConclusion extends BaseObject{
	
	
	@Autowired
	private ModelConclusionJobService modelConclusionJobService;
	
	public String getAuditLibraryResult(Map<String, Object> map) throws Exception{
		StringBuffer allConclusion =new StringBuffer();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		// 4.1 根据任务信息和审计点信息，查询模型结论配置表hpeadm.model_conclusion_config，进而生成新的模型结论，可以考虑dict_common缓存的应用
		if("10000".equals(map.get("provinceCode"))){
			list = modelConclusionJobService.selectQGModelConclusionConfig(map.get("auditId").toString());
		}else{
			list = modelConclusionJobService.selectModelConclusionConfig(map.get("auditId").toString());
		}
		for(Map<String, Object> modleConconfigMap : list){
			//4.1.1 进而生成新的模型结论
			Map<String, Object> allResult = getTaskAllSqlResult(modleConconfigMap,map);			
			String modelConclusion= modleConconfigMap.get("conclusion").toString();
			String finalConclusion = VariantTool.eval(modelConclusion, allResult, null);
			//finalConclusion = new String(finalConclusion.getBytes("GBK"),"UTF-8");
			allConclusion.append(finalConclusion);
		}
		return allConclusion.toString();
		
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
}
