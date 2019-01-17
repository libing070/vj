package com.hpe.cmca.finals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.biframe.privilege.IUserPrivilegeService;
import com.asiainfo.biframe.privilege.webosimpl.UserPrivilegeServiceImpl;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.util.SimpleMap;

/**
 * <pre>
 * Desc： 
 * @author robin.du
 * @refactor robin.du
 * @date   Jan 12, 2015 10:37:23 AM
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Jan 12, 2015 	   robin.du 	         1. Created this class. 
 * </pre>
 */
@Service
public class Dict {

	@Autowired
	private MybatisDao			mybatisDao;

	public static Map<String, Object>	DictMap		= new HashMap<String, Object>();

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


	public String getAudTrmSTR(String concernId, String selProvCode) {
		if(!DictMap.containsKey("AUD_TRM")){
			initAudTrm();
		}
		String str = ((Map<String, String>)DictMap.get("AUD_TRM")).get(concernId);
		return str == null ? "('-1')" : str;
	}
	
	public List<SimpleMap> getAuditList(String concernId) {
		if(!DictMap.containsKey("AUD_TRM")){
			initAudTrm();
		}
		Map<String, List<SimpleMap>> map =	(Map<String, List<SimpleMap>>)DictMap.get("AUD_TRM_LIST");
		return map.get(concernId);
	}
	public List<SimpleMap> getCityList(String provCode) {
		if(!DictMap.containsKey(getMapKey("CITY"))){
			initCity();
		}	
		return (List<SimpleMap>)DictMap.get(getListKey("CITY_"+provCode));
	}

	public List<SimpleMap> getWglxList(Object concernId) {
		if(!DictMap.containsKey(getMapKey("WGLX"))){
			initWglx();
		}	
		return (List<SimpleMap>)DictMap.get(getListKey("WGLX_"+ concernId));
	}


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
		if (key == null || "".equals(key))
			return "";
		Object name = getMap(type).get(key);
		if (name == null) {
			name = key;
		}
		return name;
	}

	public boolean haveRight(String sourcesKey, String userId) {
		if(StringUtils.isEmpty(userId)){
			return false;
		}
		
		IUserPrivilegeService service = new UserPrivilegeServiceImpl();
		return service.haveRightByUserId(userId, 9000, (String)getAlias("SOURCES_ID", sourcesKey));
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
			System.err.println("initDict==============================");
			initDb(bizType);
			System.err.println("initDict====initDb");
			initAudTrm();
			System.err.println("initDict====initAudTrm");
			initCity();
			System.err.println("initDict====initCity");
			initWglx();
			System.err.println("initDict====initWglx");
		} else {
			DictMap.remove(getMapKey(bizType));
			DictMap.remove(getListKey(bizType));
			initDb(bizType);
		}
	}

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
			//判断Map中是否有这类数据
			mapKey = getMapKey((String) map.get("bizType"));
			if (!DictMap.containsKey(mapKey)) {
				DictMap.put(mapKey, new HashMap<String, String>());
			}

			//判断List中是否有这类数据
			listKey = getListKey((String) map.get("bizType"));
			if (!DictMap.containsKey(listKey)) {
				DictMap.put(listKey, new ArrayList<SimpleMap>());
			}
			
			//普通键值对
			if(!StringUtils.isEmpty((String) map.get("bizValue")) && !StringUtils.isEmpty((String) map.get("bizText"))){
				((Map<String, String>) DictMap.get(mapKey)).put((String) map.get("bizValue"), (String) map.get("bizText"));
				((List<SimpleMap>) DictMap.get(listKey)).add(new SimpleMap((String) map.get("bizValue"), (String) map.get("bizText")));
			}
			//selectId
			if(!StringUtils.isEmpty((String) map.get("script"))){
				initDbBySql((String) map.get("bizType"), (String) map.get("script"));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void initDbBySql(final String bizType, final String selectId){
		Map<String, String> params = new HashMap<String, String>();
		List<Map<String, Object>> list = mybatisDao.getList(selectId, params);
		String mapKey = "";
		String listKey = "";
		for (Map<String, Object> map : list) {
			//判断Map中是否有这类数据
			mapKey = getMapKey(bizType);
			if (!DictMap.containsKey(mapKey)) {
				DictMap.put(mapKey, new HashMap<String, String>());
			}

			//判断List中是否有这类数据
			listKey = getListKey(bizType);
			if (!DictMap.containsKey(listKey)) {
				DictMap.put(listKey, new ArrayList<SimpleMap>());
			}
			
			
			if(!StringUtils.isEmpty((String) map.get("bizValue")) && !StringUtils.isEmpty((String) map.get("bizText"))){
				((Map<String, String>) DictMap.get(mapKey)).put((String) map.get("bizValue"), (String) map.get("bizText"));
				((List<SimpleMap>) DictMap.get(listKey)).add(new SimpleMap((String) map.get("bizValue"), (String) map.get("bizText")));
			}
		}
	}

	public void initAudTrm(){
		Map<String, String> params = new HashMap<String, String>();
		List<Map<String, Object>> list = mybatisDao.getList("commonMapper.dict_auditTrm_conf", params);
		

		System.err.println("AUD_TRM=================================================================================================");
		Map<String, String> auditMap =  new HashMap<String, String>();
		Map<String, List<SimpleMap>> monthListMap = new HashMap<String, List<SimpleMap>>();
		String audit_trm = "'-1'";
		List<SimpleMap> auditList = new ArrayList<SimpleMap>();
		
		String concernId = "";
		String auditTrmFmt="";
		for (Map<String, Object> map : list) {
			auditTrmFmt = map.get("audTrm")+"";
			auditTrmFmt = auditTrmFmt.substring(0,4)+ "年"+auditTrmFmt.substring(4, 6) +"月";
			SimpleMap simpleMap = new SimpleMap(map.get("audTrm")+"",auditTrmFmt);
			if(String.valueOf(map.get("concernId")).equals(concernId)){
				audit_trm = audit_trm + ",'" + map.get("audTrm") + "'";
				auditList.add(simpleMap);
			}else{
				auditMap.put(concernId, "(" + audit_trm + ")");
				monthListMap.put(concernId, auditList);
				
				auditList = new ArrayList<SimpleMap>();
				auditList.add(simpleMap);
				
				System.err.println(concernId + "=======================" + auditMap.get(concernId));
				concernId = String.valueOf(map.get("concernId"));
				audit_trm = "'-1','" + map.get("audTrm") + "'";
			}
		}
		auditMap.put(concernId, "(" + audit_trm + ")");
		monthListMap.put(concernId, auditList);
		
		System.err.println(concernId + "=======================" + auditMap.get(concernId));
		System.err.println("AUD_TRM=================================================================================================");
		DictMap.put("AUD_TRM", auditMap);
		DictMap.put("AUD_TRM_LIST", monthListMap);
	}
	private void initCity(){
		Map<String, String> params = new HashMap<String, String>();
		List<Map<String, Object>> list = mybatisDao.getList("commonMapper.selectCityList", params);
		

		String mapKey = getMapKey("CITY");
		String listKey = "";
		String provinceCode = "";
		for (Map<String, Object> map : list) {		
			if (!DictMap.containsKey(mapKey)) {
				DictMap.put(mapKey, new HashMap<String, String>());
			}
			((Map<String, String>) DictMap.get(mapKey)).put(map.get("CityCode").toString(), (String) map.get("name"));
			
			
			
			provinceCode = "CITY_" + map.get("provinceCode");
			//判断List中是否有这类数据
			listKey = getListKey(provinceCode);
			if (!DictMap.containsKey(listKey)) {
				DictMap.put(listKey, new ArrayList<SimpleMap>());
			}
			((List<SimpleMap>) DictMap.get(listKey)).add(new SimpleMap(map.get("CityCode").toString(), (String) map.get("name")));
			
		}
	}
	private void initWglx(){
		Map<String, String> params = new HashMap<String, String>();
		List<Map<String, Object>> list = mybatisDao.getList("commonMapper.dict_infractionType", params);
		

		String mapKey = getMapKey("WGLX");
		String listKey = "";
		String concernId = "";
		for (Map<String, Object> map : list) {		
			if (!DictMap.containsKey(mapKey)) {
				DictMap.put(mapKey, new HashMap<String, String>());
			}
			((Map<String, String>) DictMap.get(mapKey)).put(map.get("bizValue").toString(), (String) map.get("bizText"));
			
			
			
			concernId = "WGLX_" + map.get("bizType");
			//判断List中是否有这类数据
			listKey = getListKey(concernId);
			if (!DictMap.containsKey(listKey)) {
				DictMap.put(listKey, new ArrayList<SimpleMap>());
			}
			((List<SimpleMap>) DictMap.get(listKey)).add(new SimpleMap(map.get("bizValue").toString(), (String) map.get("bizText")));
			
		}
		
	}



	
	
	
	
	private static String getMapKey(String bizType) {
		return "MAP_" + bizType;
	}

	private static String getListKey(String bizType) {
		return "LIST_" + bizType;
	}
}
