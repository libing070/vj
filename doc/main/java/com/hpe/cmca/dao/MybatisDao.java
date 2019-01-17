/**
 * com.hp.base.dao.MybatisDao.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hpe.cmca.service.ConcernService;
import com.hpe.cmca.web.taglib.Pager;


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

	@Transactional
	public void add(String key, Object entity) {
		getSqlSession().insert(key, entity);
	}

	@Transactional
	public void delete(String key, Serializable id) {
		getSqlSession().delete(key, id);
	}

	@Transactional
	public void delete(String key, Object object) {
		getSqlSession().delete(key, object);
	}

	public <T> T get(String key, Object params) {
		T obj = getSqlSession().selectOne(key, params);
		return obj;
	}

	@Transactional
	public void update(String key, Object object) {
		getSqlSession().update(key, object);
	}

	public <T> List<T> getList(String key) {

		List<T> returnList = getSqlSession().selectList(key);
		if (returnList == null) {
			return new ArrayList<T>();
		}
		return returnList;
	}

	public <T> List<T> getList(String key, Object params) {

		List<T> returnList = getSqlSession().selectList(key, params);

		if (returnList == null) {
			return new ArrayList<T>();
		}
		return returnList;
	}

	@SuppressWarnings("rawtypes")
	@Transactional
	public void addBatch(String key, List<Map> list) {

		getSqlSession().insert(key, list);

	}
	//copy by cmccca 20170509
	public void processPager(Pager pager) {
		// TODO 实现分页功能
		long t1 = System.currentTimeMillis();
		System.err.println("setPager timer[" + pager.getSelectId()
				+ "]:===================start============");

		// 有价卡映射改造，1001至1009都用1001的xml
		ConcernService.handYJKMapping(pager, pager.getSelectId());
		// 20160512 把原来 3001 3002 3004 3005 的namespace forward 到zdtl中
		ConcernService.handZDTLMapping(pager, pager.getSelectId());
		List<Map<String, Object>> list = getSqlSession().selectList(
				pager.getSelectId(), pager);

		System.err.println("setPager timer[" + pager.getSelectId()
				+ "]:=================== end ============"
				+ (System.currentTimeMillis() - t1));
		if (list != null) {
			pager.setResultList(list);
		}
	}
	
}
