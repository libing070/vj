/**
 * com.hp.base.dao.MybatisDao.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * Desc： 
 * @author peter.fu
 * @refactor peter.fu
 * @date   Oct 22, 2014 5:56:55 PM
 * @version 1.0
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Oct 22, 2014 	   peter.fu 	         1. Created this class.
 * </pre>
 */
@Service(value = "mybatisDao")
public class MybatisDao extends SqlSessionDaoSupport {

	public void add(String key, Object entity) {
		getSqlSession().insert(key, entity);
	}

	public void delete(String key, Serializable id) {
		getSqlSession().delete(key, id);
	}

	public void delete(String key, Object object) {
		getSqlSession().delete(key, object);
	}

	public <T> T get(String key, Object params) {
		T obj = getSqlSession().selectOne(key, params);
		return obj;
	}

	public void update(String key, Object object) {
		getSqlSession().update(key, object);
	}

	public <T> List<T> getList(String key) {

		long startTime = System.currentTimeMillis();
		List<T> returnList = getSqlSession().selectList(key);
		if (returnList == null) {
			return new ArrayList<T>();
		}
		long endTime = System.currentTimeMillis();		
		this.logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ sql=["+key+"], and time is [" +(endTime-startTime) +"] ms");
		return returnList;
	}

	public <T> List<T> getList(String key, Object params) {

		long startTime = System.currentTimeMillis();
		List<T> returnList = getSqlSession().selectList(key, params);
		if (returnList == null) {
			return new ArrayList<T>();
		}
		long endTime = System.currentTimeMillis();		
		this.logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ sql=["+key+"], and time is [" +(endTime-startTime) +"] ms");
		return returnList;
	}

	@SuppressWarnings("rawtypes")
	public void addBatch(String key, List<Map> list) {

		getSqlSession().insert(key, list);

	}
}
