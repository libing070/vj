package com.hpe.cmwa.auditTask.service.job;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.dao.MybatisDao;
@Service
public class ModelNotifyGenService extends BaseObject {

	@Autowired
	private MybatisDao	mybatisDao;
	
	/**
	 * 获取busi_model_notify表中已经存在的审计月记录
	 * @return
	 */
	public List<String> getExistsAudTrm() {
		List<String> list = mybatisDao.getList("modelNotifyGenMapper.getExistsAudTrm", null);
		return list;
	}
	/**
	 * 获取需要同步的数据
	 * @return
	 */
	public void insertNewData(List<Map> paramsList) {
		mybatisDao.addBatch("modelNotifyGenMapper.insertNewData", paramsList);
	}
	
}
