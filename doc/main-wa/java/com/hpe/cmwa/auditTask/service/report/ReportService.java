package com.hpe.cmwa.auditTask.service.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.StringUtils;
@Service
public class ReportService  extends BaseObject{
	
	@Autowired
    private MybatisDao	mybatisDao;
    /**
     *
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author 
     * </pre>
     */

	public Map<String, Object> getReportInfor(Map<String, Object> parameterMap) {
		Map<String, Object> map= mybatisDao.get("reportMapper.getReportInfor", parameterMap);
		return map;
	}
	public Map<String, Object> getDataNum(Map<String, Object> parameterMap) {
		Map<String, Object> map= mybatisDao.get("reportMapper.getDataNum", parameterMap);
		return map;
	}
	
}
