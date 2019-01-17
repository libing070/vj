package com.hpe.cmwa.auditTask.service.job;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.dao.MybatisDao;
@Service
public class MonitorViewJobService extends BaseObject {

	@Autowired
	private MybatisDao	mybatisDao;
	
	/**
	 * 获取需要同步数据的时间标识
	 * @return
	 */
	public List<Map> getTimeStamp() {
		List<Map> list = mybatisDao.getList("monitorViewMapper.getTimeStamp", null);
		return list;
	}
	/**
	 * 获取需要同步的数据
	 * @return
	 */
	public List<Map> selectNeedSyncList(List<Map> paramsList) {
		List<Map> list = mybatisDao.getList("monitorViewMapper.selectNeedSyncList", paramsList);
		return list;
	}
	/**
	 * 将数据同步到auditDb库下的概览表
	 * @param list
	 */
	public void dataSynchToAudit(List<Map> list) {
		mybatisDao.addBatch("monitorViewMapper.addDataToAuditDb", list);
	}
	/**
	 * 
	 * 更新同步之后数据的状态
	 * @param timestamp
	 */
	public void updateDataStatus() {
		mybatisDao.update("monitorViewMapper.updateDataStatus", null);
		
	}
	
	/**
	 * 同步数据之前，删除audit_db库下含有同 未同步数据相同标识的数据
	 * @param paramsList
	 */
	public void deleteData(List<Map> paramsList) {
		mybatisDao.delete("monitorViewMapper.deleteData", paramsList);
	}
}
