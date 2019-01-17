package com.hpe.cmwa.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.SimpleMap;

/**
 * <pre>
 * Desc： 数据字典--缓存
 * @author robin.du
 * @refactor robin.du
 * @date   Jan 12, 2015 10:37:23 AM
 * @version 1.0  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Jan 12, 2015 	   robin.du 	         1. Created this class.
 * </pre>
 */
@Service
public class Dict {

	@Autowired
	private MybatisDao					mybatisDao;

	public static Map<String, Object>	DictMap	= new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public synchronized List<SimpleMap> getList(String type) {

		String listKey = getListKey(type);
		List<SimpleMap> list = (List<SimpleMap>) DictMap.get(listKey);
		if (list == null) {
			init(type);
			list = (List<SimpleMap>) DictMap.get(listKey);
		}
		if (list == null) {
			list = new ArrayList<SimpleMap>();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getMap(String type) {

		Map<String, String> map = (Map<String, String>) DictMap.get(getMapKey(type));
		if (map == null) {
			init(type);
			map = (Map<String, String>) DictMap.get(getMapKey(type));
		}
		if (map == null) {
			map = new HashMap<String, String>();
		}
		return map;
	}

	public Object getAlias(String type, String key) {

		if (key == null || "".equals(key)) return "";
		Object name = getMap(type).get(key);
		if (name == null) {
			name = key;
		}
		return name;
	}

	/**
	 * 用于初始化静态字典
	 * 
	 * <pre>
	 * Desc  
	 * @param bizType 为null时，初始化所有值， 
	 * @author duq
	 * @refactor duq
	 * @date   Jan 12, 2015 2:25:03 PM
	 * </pre>
	 */
	public void init(String bizType) {

		if (bizType == null) {
			DictMap.clear();
			initDb(bizType);
		} else {
			DictMap.remove(getMapKey(bizType));
			DictMap.remove(getListKey(bizType));
			initDb(bizType);
		}
	}

	@SuppressWarnings({ "serial", "unchecked" })
	private void initDb(final String bizType) {

		Map<String, String> params = new HashMap<String, String>() {

			{
				put("bizType", bizType);
			}
		};
		List<Map<String, Object>> list = mybatisDao.getList("commonMapper.dict_common", params);

		String mapKey = "";
		String listKey = "";
		for (Map<String, Object> map : list) {
			// 判断Map中是否有这类数据
			mapKey = getMapKey((String) map.get("bizType"));
			if (!DictMap.containsKey(mapKey)) {
				DictMap.put(mapKey, new HashMap<String, String>());
			}

			// 判断List中是否有这类数据
			listKey = getListKey((String) map.get("bizType"));
			if (!DictMap.containsKey(listKey)) {
				DictMap.put(listKey, new ArrayList<SimpleMap>());
			}

			// 普通键值对
			if (!StringUtils.isEmpty((String) map.get("bizValue")) && !StringUtils.isEmpty((String) map.get("bizText"))) {
				((Map<String, String>) DictMap.get(mapKey)).put((String) map.get("bizValue"), (String) map.get("bizText"));
				((List<SimpleMap>) DictMap.get(listKey)).add(new SimpleMap((String) map.get("bizValue"), (String) map.get("bizText")));
			}
			// selectId
			if (!StringUtils.isEmpty((String) map.get("script"))) {
				initDbBySql((String) map.get("bizType"), (String) map.get("script"));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void initDbBySql(final String bizType, final String selectId) {

		Map<String, String> params = new HashMap<String, String>();
		List<Map<String, Object>> list = mybatisDao.getList(selectId, params);
		String mapKey = "";
		String listKey = "";
		for (Map<String, Object> map : list) {
			// 判断Map中是否有这类数据
			mapKey = getMapKey(bizType);
			if (!DictMap.containsKey(mapKey)) {
				DictMap.put(mapKey, new HashMap<String, String>());
			}

			// 判断List中是否有这类数据
			listKey = getListKey(bizType);
			if (!DictMap.containsKey(listKey)) {
				DictMap.put(listKey, new ArrayList<SimpleMap>());
			}

			if (!StringUtils.isEmpty((String) map.get("bizValue")) && !StringUtils.isEmpty((String) map.get("bizText"))) {
				((Map<String, String>) DictMap.get(mapKey)).put((String) map.get("bizValue"), (String) map.get("bizText"));
				((List<SimpleMap>) DictMap.get(listKey)).add(new SimpleMap((String) map.get("bizValue"), (String) map.get("bizText")));
			}
		}
	}

	private static String getMapKey(String bizType) {

		return "MAP_" + bizType;
	}

	private static String getListKey(String bizType) {

		return "LIST_" + bizType;
	}
}
