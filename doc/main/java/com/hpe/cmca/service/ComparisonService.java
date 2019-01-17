package com.hpe.cmca.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.dao.MybatisDao;

@Service
public class ComparisonService {

	@Autowired
	private MybatisDao	mybatisDao	= null;
	
	public List<Map<String, Object>> getQDYKList(Map<String, Object> params){
		return mybatisDao.getList("jobReportMapper.getQDYKList", params);
	}
}
