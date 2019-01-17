package com.hpe.cmca.finals;

import java.util.HashMap;
import java.util.Map;
 

/**
 * 缓存管理
 * 
 * @author yuetian
 * 
 */ 
public class CachManager {

	private static final CachManager cm = new CachManager();

	private Map<String, Object> map;
	
 

	private CachManager() {
		map = new HashMap<String, Object>();
		 
//		List<Map> list = dao.getList("EAP_COMPONENTS_CFG.getComponentsConfig");
//		for (Map m : list) {
//			
//			map.put(m.get("COMP_CODE").toString(), m);
//			
//		}
	}

	public static CachManager getInstance() {
		return cm;
	}

	@SuppressWarnings("unchecked")
	public <T> T getCache(CacheKeys key) {

		return (T) map.get(key.toString());
	}

	public void putCache(CacheKeys key, Object val) {

		map.put(key.toString(), val);
	}

}
