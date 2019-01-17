package com.hpe.cmwa.auditTask.service.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.dao.MybatisDao;
@Service
public class ModelConclusionJobService extends BaseObject {

	@Autowired
	private MybatisDao	mybatisDao;
	
	/**
	 * 根据状态获取 尚未处理的文件加载完成通知
	 * 状态0-未处理 1-处理中 2-处理完成 3-处理异常
	 * @return
	 */
	public  List<Map<String, Object>> selectIntfCodes(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", 0);
		List<Map<String, Object>> list = mybatisDao.getList("workMapper.selectIntfCodes", params);
		return list;
	}

	/**
	 * 根据 接口单元编码去查询对应的审计点
	 * @param intCode
	 * @return
	 */
	public  List<Map<String, Object>> selectauditIds(List<Map<String, Object>> intfCodeMap){
		List<Object> intfCodeList = new ArrayList<Object>();
		for(Map<String, Object>  map : intfCodeMap){
			intfCodeList.add(map.get("IntfCode"));
		}
		List<Map<String, Object>> list = mybatisDao.getList("workMapper.selectauditIds", intfCodeList);
		return list;
	}
	/**
	 * 根据审计点去audit_db.audit_task_info_share查询对应的任务信息列表
	 * @param intCode
	 * @return
	 */
	public  List<Map<String, Object>> selecAuditTaskInfoShare(Map<String, Object> auditMappingMap){
		List<Map<String, Object>> list = mybatisDao.getList("workMapper.selecAuditTaskInfoShare", auditMappingMap);
		return list;
	}
	
	/**
	 * 根据任务信息和审计点信息，查询模型结论配置表，进而生成新的模型结论
	 * @param auditId
	 * @return
	 */
	public  List<Map<String, Object>> selectModelConclusionConfig(String auditId){
		List<Map<String, Object>> list = mybatisDao.getList("workMapper.selectModelConclusionConfig", auditId);
		return list;
	}
	/**
	 * 根据任务信息和审计点信息，查询模型结论配置表，进而生成新的模型结论
	 * @param auditId
	 * @return
	 */
	public  List<Map<String, Object>> selectQGModelConclusionConfig(String auditId){
		List<Map<String, Object>> list = mybatisDao.getList("workMapper.selectQGModelConclusionConfig", auditId);
		return list;
	}
	
	/**
	 * 插入推送状态log日志
	 * @param logMap
	 * @return
	 */
	public  void insetConclusionLog(Map<String, Object> logMap){
		mybatisDao.add("workMapper.insetConclusionLog", logMap);
	}
	
	public Map<String, Object> getResult(String sqlId,Map<String, Object> map){
		Map<String, Object> shortNameMap = mybatisDao.get("workMapper.selectShortNameById", map.get("provinceCode"));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("provinceCode", map.get("provinceCode"));
		params.put("currSumBeginDate", map.get("beforeAcctMonth"));
		params.put("currSumEndDate", map.get("endAcctMonth"));
		params.put("shortName", shortNameMap.get("short_name"));
		Map<String, Object> resultMap =  mybatisDao.get(sqlId, params);
		return resultMap;
	}
	/**
	 * 获取model_finished_audit_task表  所有信息
	 * @return
	 */
	public List<Map<String, Object>> selectAuditTaskInfoShare(){
		List<Map<String, Object>> list = mybatisDao.getList("workMapper.selectAuditTaskInfoShare", null);
		return list;
	}
	/**
	 * 获取model_finished_audit_task表  所有信息
	 * @return
	 */
	public List<Map<String, Object>> selectModelFinishedAuditTask(){
		List<Map<String, Object>> list = mybatisDao.getList("workMapper.selectModelFinishedAuditTask", null);
		return list;
	}
	public  void insetModelFinishedAuditTask(Map<String, Object> map){
		mybatisDao.add("workMapper.insetModelFinishedAuditTask", map);
	}
	
	/**
	 * 修改推送状态
	 * @param status
	 */
	public void updateModelFileNotification(Map<String, Object> params){
		mybatisDao.update("workMapper.updateModelFileNotification", params);
	}
}
