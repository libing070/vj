/**
 * com.hpe.cmca.service.YwyjService.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.common.PageBean;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.YwyjMapper;
import com.hpe.cmca.pojo.*;
import com.hpe.cmca.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * Desc：
 * @author   hufei
 * @refactor hufei
 * @date     2017-11-7 下午5:14:30
 * @version  1.0
 *
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  2017-11-7 	   hufei 	         1. Created this class.
 * </pre>
 */
@Service("YwyjService")
public class YwyjService extends BaseObject {

    @Autowired
    private MybatisDao mybatisDao;
    @Autowired
    public BgxzService bgxzService;

    public Map<String,List<String>>  queryMonth(String authorityAttr, String lv2Code,String pointCode) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> paramterMap = new HashMap<String, Object>();
//	if (pointCode.startsWith("lp1_csyjlc")) {
//	    paramterMap.put("authorityAttr", authorityAttr);
//	    if (!"lp1_csyjlc0".equals(pointCode)) {
//		paramterMap.put("level1ProcessCode", pointCode);
//	    }
//	} else {
	    paramterMap.put("lv2Code", lv2Code);
		paramterMap.put("pointCode", pointCode);
		paramterMap.put("authorityAttr", authorityAttr);
//	}
	List<Map<String, Object>> result = ywyjMapper.queryMonth(paramterMap);
	List<String> audtrmList = new ArrayList<String>();
	Map<String,List<String>> audtrmListMap = new HashMap<String,List<String>>();
	for (Map<String, Object> map : result) {

		audtrmList.add(map.get("mon").toString());
	    //Integer thisMonth = Integer.parseInt("0".equals(mon.substring(4, 5)) ? mon.substring(5, 6) : mon.substring(4, 6));
	   // map.put("audtrmList", mon.substring(0, 4) + "年" + thisMonth + "月");
		audtrmListMap.put("audtrmList", audtrmList);
	}
	return audtrmListMap;
    }

    /**
     *
     * <pre>
     * Desc  新增版本信息
     * @param pointVersionId
     * @return
     * @author hufei
     * 2017-12-24 下午8:38:24
     * </pre>
     */
    public Map<String, Object> addVersion(String createPerson, String pointCode, String pointVersionId) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> paramterMap;
	// 1.查询版本信息表
	List<MonitorPointsConfigData> monitorPointsConfigList = getVersionByPointVersionId(pointVersionId);
	String newPointVersionId = "pv_" + getNumberForPK();
	StringBuffer newAssemblyIds = new StringBuffer();
	if (monitorPointsConfigList != null && monitorPointsConfigList.size() > 0 && monitorPointsConfigList.get(0).getAssemblyId() != null) {

	    // 2.处理后获得组件ID数组
	    String[] assemblyIds = monitorPointsConfigList.get(0).getAssemblyId().split(";");
	    String newAssemblyId = null;

	    for (int m = 0; m < assemblyIds.length; m++) {
		newAssemblyId = "a_" + getNumberForPK();
		// 3.根据组件ID获得控件ID
		AssemblyInfoData parameterData = new AssemblyInfoData();
		parameterData.setAssemblyId(assemblyIds[m]);
		List<AssemblyInfoData> assemblyInfoList = getAssemblyInfo(parameterData);
		StringBuffer newControIds = new StringBuffer();
		if (assemblyInfoList != null && assemblyInfoList.size() > 0 && assemblyInfoList.get(0).getControlIdArrays() != null) {
		    String[] controlIds = assemblyInfoList.get(0).getControlIdArrays().split(";");
		    String newControId;
		    for (int n = 0; n < controlIds.length; n++) {
			// 4.拼接后，插入控件信息
			newControId = "c_" + getNumberForPK();
			paramterMap = new HashMap<String, Object>(2);
			paramterMap.put("newControId", newControId);
			paramterMap.put("controlId", controlIds[n]);
			ywyjMapper.copyOfControl(paramterMap);
			newControIds.append(newControId + ";");
		    }
		}
		// 5.插入组件信息
		paramterMap = new HashMap<String, Object>(3);
		paramterMap.put("newAssemblyId", newAssemblyId);
		paramterMap.put("newControlIds", "".equals(newControIds.toString()) ? null : newControIds.toString());
		paramterMap.put("assemblyId", assemblyIds[m]);
		ywyjMapper.copyOfAssembly(paramterMap);
		newAssemblyIds.append(newAssemblyId + ";");
	    }

	}
	String versionCode = "版本" + generateVersion();
	// 6.插入版本信息
	if (pointVersionId == null || pointVersionId == "" || pointVersionId.isEmpty()) {
	    MonitorPointsConfigData parameterData = new MonitorPointsConfigData();
	    parameterData.setCreatePerson(createPerson);
	    parameterData.setPointCode(pointCode);
	    parameterData.setPointVersionId(newPointVersionId);
	    parameterData.setVersionCode(versionCode);
	    ywyjMapper.insertMonitorPointInfo(parameterData);
	} else {
	    paramterMap = new HashMap<String, Object>(3);
	    paramterMap.put("versionCode", versionCode);
	    paramterMap.put("newPointVersionId", newPointVersionId);
	    paramterMap.put("newAssemblyIds", "".equals(newAssemblyIds.toString()) ? null : newAssemblyIds.toString());
	    paramterMap.put("newCreatePerson", createPerson);
	    paramterMap.put("pointVersionId", pointVersionId);
	    ywyjMapper.copyOfVersion(paramterMap);
	}
	Map<String, Object> result = new HashMap<String, Object>(3);
	result.put("pointVersionId", newPointVersionId);
	result.put("isOpen", 0);
	result.put("versionCode", versionCode);
	return result;
    }

    /**
     *
     * <pre>
     * Desc设置默认版本
     * @param parameterData
     * @return
     * @author hufei
     * 2017-12-24 下午5:07:45
     * </pre>
     */
    public int setDefaultVersion(MonitorPointsConfigData parameterData) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	ywyjMapper.setNonDefaultVersion(parameterData);
	parameterData.setIsOpen(1);
	return ywyjMapper.setDefaultVersion(parameterData);
    }

    /**
     * <pre>
     * Desc 删除组件信息 -后续待优化，删除版本信息表中的组件信息
     * @author hufei
     * 2017-12-24 下午5:09:17
     * </pre>
     */
    public void deleteAssemblyInfo(String pointVersionId, String assemblyId) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	AssemblyInfoData assemblyParameter = new AssemblyInfoData();
	assemblyParameter.setAssemblyId(assemblyId);
	List<AssemblyInfoData> list = getAssemblyInfo(assemblyParameter);
	for (AssemblyInfoData data : list) {
	    if (data.getControlIdArrays() != null && !data.getControlIdArrays().isEmpty()) {
		String[] controlId = data.getControlIdArrays().split(";");
		for (int i = 0; i < controlId.length; i++) {
		    ywyjMapper.deleteControlInfo(controlId[i]);
		}
	    }
	}

	Map<String, Object> parameterMap = new HashMap<String, Object>();
	parameterMap.put("pointVersionId", pointVersionId);
	List<MonitorPointsConfigData> versionList = ywyjMapper.getMonitorPointInfo(parameterMap);
	if (versionList != null && versionList.size() > 0 && versionList.get(0).getAssemblyId() != null) {
	    MonitorPointsConfigData paramterVersion = versionList.get(0);
	    String newAssemblyId = paramterVersion.getAssemblyId().replace(assemblyId + ";", "");
	    if (newAssemblyId.trim().length() == 0)
		newAssemblyId = null;
	    paramterVersion.setAssemblyId(newAssemblyId);
	    ywyjMapper.updateMonitorPointInfo(paramterVersion);
	}
	ywyjMapper.deleteAssemblyInfo(assemblyId);
    }

    /**
     *
     * <pre>
     * Desc  获取控件对应的表
     * @param parameterData:业务预警入参变量类，必须包含监控点。
     * @return
     * @author hufei
     * 2017-11-9 下午2:48:05
     * </pre>
     */
    public List<Map<String, Object>> getTableInfo(YWYJParameterData parameterData) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> tableInfoMap = ywyjMapper.getTableInfo(parameterData);
	List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
	if (tableInfoMap != null && !tableInfoMap.isEmpty() && tableInfoMap.get("result_tname") != null) {
	    Map<String, Object> map;
	    String[] tableName = tableInfoMap.get("result_tname").toString().split(";");
	    String[] busiName = tableInfoMap.get("result_businame").toString().split(";");
	    for (int i = 0; i < tableName.length && i < busiName.length; i++) {
		map = new HashMap<String, Object>();
		map.put("tableName", tableName[i]);
		map.put("busiName", busiName[i]);
		resultList.add(map);
	    }

	}
	return resultList;
    }

    /**
     *
     * <pre>
     * Desc  获取表的列名、列title
     * @param parameterData：业务预警入参变量类，必须包含表名
     * @return map类型的表信息，包括列名、列title
     * @author hufei
     * 2017-11-8 下午4:56:30
     * </pre>
     */
    public Map<String, Object> getTableDetails(String parameterData) {
	List<Map<String, Object>> tableDetails = getTableLine(parameterData);
	Map<String, Object> result = new HashMap<String, Object>(2);
	List<Map<String, Object>> yList = new ArrayList<Map<String, Object>>();
	if (tableDetails != null && tableDetails.size() > 0) {
	    for (Map<String, Object> map : tableDetails) {
		if ("number".equals(map.get("columnType"))) {
		    yList.add(map);
		}
	    }
	    result.put("xList", tableDetails);
	    result.put("yList", yList);
	}
	return result;
    }

    private List<Map<String, Object>> getTableLine(String parameterData) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> parameterMap = new HashMap<String, Object>(2);
	parameterMap.put("dataBaseName", parameterData.substring(0, parameterData.indexOf(".")));
	parameterMap.put("tableName", parameterData.substring(parameterData.indexOf(".") + 1));
	List<Map<String, Object>> tableDetails = ywyjMapper.getTableDetails(parameterMap);
	if (tableDetails != null && tableDetails.size() > 0) {
	    for (Map<String, Object> map : tableDetails) {
		if ("I".equals(map.get("columnType")) || "F".equals(map.get("columnType")) || "I2".equals(map.get("columnType")) || "D".equals(map.get("columnType"))) {
		    map.put("columnType", "number");
		} else {
		    map.put("columnType", "string");
		}
	    }
	}
	return tableDetails;
    }

    /**
     *
     * <pre>
     * Desc获取筛选条件
     * @paramparameterDataT
     * @return
     * @author hufei
     * 2018-4-28 下午3:27:07
     * </pre>
     */
    private List<Map<String, Object>> getTableLineForRank(String parameterData) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> parameterMap = new HashMap<String, Object>(2);
	parameterMap.put("dataBaseName", parameterData.substring(0, parameterData.indexOf(".")));
	parameterMap.put("tableName", parameterData.substring(parameterData.indexOf(".") + 1));
	List<Map<String, Object>> tableDetails = ywyjMapper.getTableDetails(parameterMap);
	if (tableDetails != null && tableDetails.size() > 0) {
	    for (int i = 0; i < tableDetails.size(); i++) {
		Map<String, Object> map = tableDetails.get(i);
		if ("aud_trm".equals(map.get("columnName")) || "prvd_id".equals(map.get("columnName")) || "cmcc_prov_prvd_id".equals(map.get("columnName").toString().toLowerCase())
			|| "short_name".equals(map.get("columnName")) || "cmcc_prov_prvd_nm".equals(map.get("columnName").toString().toLowerCase()) || "prvd_name".equals(map.get("columnName"))||"prvd_nm".equals(map.get("columnName"))) {
		    tableDetails.remove(map);
		    i--;
		} else {
		    if ("I".equals(map.get("columnType")) || "F".equals(map.get("columnType")) || "I2".equals(map.get("columnType")) || "D".equals(map.get("columnType"))) {
			map.put("columnType", "number");
		    } else {
			map.put("columnType", "string");
		    }
		}

	    }
	}

	return tableDetails;
    }

    /**
     *
     * <pre>
     * Desc保存控件的数据的信息
     * @param parameterData
     * @return
     * @author hufei
     * 2017-11-9 下午4:42:37
     * </pre>
     */
    public int saveControlDataInfo(YWYJParameterData parameterData) {
	int i = updateControllerDataInfo(parameterData);
	genSql(parameterData);
	return i;
    }

    /**
     *
     * <pre>
     * Desc
     * @param map：表结构
     * @return map类型的表信息，包括列名、列title
     * @author hufei
     * 2017-11-8 下午4:57:53
     * </pre>
     */
    private List<Map<String, Object>> getColumnInfo(Map<String, Object> map) {
	Iterator iterator = map.entrySet().iterator();
	List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
	while (iterator.hasNext()) {
	    Map.Entry entry = (Map.Entry) iterator.next();
	    resultList = getColumnInfo(entry.getValue().toString());
	}
	return resultList;

    }

    /**
     *
     * <pre>
     * Desc
     * @param string：表结构
     * @return map类型的表信息，包括列名、列title
     * @author hufei
     * 2017-11-8 下午4:59:58
     * </pre>
     */
    private List<Map<String, Object>> getColumnInfo(String string) {
	List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
	string = string.substring(string.indexOf("("), string.indexOf("PRIMARY"));
	// String idRegex="[a-z]{1,}\\w+\\b";
	// String nameRegex="[\u4E00-\u9FA5]+\\b"; 获取中文字段信息
	List<String> columnIdList = getColumnInfo(string, "[a-z]{1,}\\w+\\b");
	List<String> columnNameList = getColumnName(string);
	Map<String, Object> resultMap;
	for (int i = 0; i < columnIdList.size() && i < columnNameList.size(); i++) {
	    resultMap = new HashMap<String, Object>();
	    resultMap.put("columnId", columnIdList.get(i));
	    resultMap.put("columnName", columnNameList.get(i));
	    resultList.add(resultMap);
	}
	return resultList;
    }

    /**
     *
     * <pre>
     * Desc
     * @param string 字符串
     * @param regex 正则表达式
     * @return List：正则表达式匹配成功的List集合
     * @author hufei
     * 2017-11-8 下午5:01:52
     * </pre>
     */
    private List<String> getColumnInfo(String string, String regex) {
	List<String> list = new ArrayList<String>();
	Matcher matcher = Pattern.compile(regex).matcher(string);
	while (matcher.find()) {
	    list.add(matcher.group());
	}
	return list;
    }

    /**
     *
     * <pre>
     * Desc
     * @param string 字符串
     * @return List：单引号括着的内容，匹配成功的List集合
     * @author hufei
     * 2017-11-8 下午5:01:52
     * </pre>
     */
    private List<String> getColumnName(String string) {
	List<String> list = new ArrayList<String>();
	Matcher matcher = Pattern.compile("[']([^{^}]*?)[']").matcher(string);
	// Matcher matcher=Pattern.compile("[\u4E00-\u9FA5]+\\b").matcher(string);
	while (matcher.find()) {
	    list.add(matcher.group().toString().substring(1, matcher.group().length() - 1));
	    // list.add(matcher.group());
	}
	return list;
    }

    /**
     *
     * <pre>
     * Desc  根据控件ID更新控件配置表信息；如果控件不存在，则先插入后更新
     * @param parameterData：控件信息表。控件ID必须存在
     * @return 正数代表成功，返回影响数量，其他代表失败
     * @author hufei
     * 2017-11-9 下午6:37:55
     * </pre>
     */
    private int updateControllerDataInfo(YWYJParameterData parameterData) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> controlInfoMap = ywyjMapper.getControlInfo(parameterData);
	// 筛选条件拼接
	if (parameterData.getScreenField() != null && parameterData.getScreenField().length() > 0) {
	    parameterData.setScreenField(screenFieldReplaceAll(parameterData.getScreenField()));
	}
	if (controlInfoMap == null || controlInfoMap.size() == 0) {
	    return ywyjMapper.insertControlDataInfo(parameterData);
	} else {
	    return ywyjMapper.updateControlDataInfo(parameterData);
	}
    }

    /**
     *
     * <pre>
     * Desc  筛选条件拼接/过滤
     * @param screenField
     * @return
     * @author hufei
     * 2017-12-22 上午10:39:20
     * </pre>
     */
    private String screenFieldReplaceAll(String screenField) {
	if (screenField.indexOf("**=*") > 0) {
	    String str = screenField.substring(screenField.indexOf("**=*"), screenField.indexOf("**=*") + 10);
	    screenField = screenField.toString().replace(str, "=#{p1}");
	}
	while (screenField.lastIndexOf("AND") == screenField.length() - 4) {
	    screenField = screenField.substring(0, screenField.length() - 4);
	}
	if (screenField.lastIndexOf("OR") == screenField.length() - 3) {
	    screenField = screenField.substring(0, screenField.length() - 3);
	}
	screenField = screenField.replaceAll("\\*", " ");
	return screenField;
    }

    /**
     *
     * <pre>
     * Desc  获取默认版本及组件ID信息
     * @paramparameterData
     * @return
     * @author hufei
     * 2017-12-20 上午9:56:47
     * </pre>
     */
    public Map<String, Object> queryMonitorPointInfo(String pointCode) {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("pointCode", pointCode);
	map.put("isOpen", 1);
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	List<MonitorPointsConfigData> monitorPointsConfigList = ywyjMapper.getMonitorPointInfo(map);
	return getContainsAssembly(monitorPointsConfigList);
    }

    /**
     *
     * <pre>
     * Desc  获取版本包含的组件ID
     * @param pointVersionId
     * @return
     * @author hufei
     * 2017-12-24 下午8:36:33
     * </pre>
     */
    public Map<String, Object> queryContainsAssembly(String pointVersionId) {
	List<MonitorPointsConfigData> monitorPointsConfigList = getVersionByPointVersionId(pointVersionId);
	return getContainsAssembly(monitorPointsConfigList);
    }

    /**
     *
     * <pre>
     * Desc  获取默认版本的组件及控件信息
     * @param parameterData
     * @return
     * @author hufei
     * 2017-12-21 下午2:48:27
     * </pre>
     */
    public Map<String, Object> queryAssemblyInfo(AssemblyInfoData parameterData) {
	List<AssemblyInfoData> AssemblyInfoDataList = getAssemblyInfo(parameterData);
	Map<String, Object> result = new HashMap<String, Object>();
	if (AssemblyInfoDataList != null && AssemblyInfoDataList.size() > 0) {
	    String assemblyName = AssemblyInfoDataList.get(0).getAssemblyName();
	    if (AssemblyInfoDataList.get(0).getControlIdArrays() != null) {
		String[] controlId = AssemblyInfoDataList.get(0).getControlIdArrays().split(";");
		result.put("controlIdSize", controlId.length);
		result.put("controlId", controlId);
	    } else {
		result.put("controlIdSize", null);
		result.put("controlId", null);
	    }

	    String assemblyLayout = AssemblyInfoDataList.get(0).getAssemblyLayout();
	    result.put("assemblyName", assemblyName);
	    result.put("assemblyLayout", assemblyLayout);
	}
	return result;
    }

    // 插入监控点版本信息
    public int insertMonitorPointInfo(MonitorPointsConfigData parameterData) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	return ywyjMapper.insertMonitorPointInfo(parameterData);
    }

    // 新增组件信息
    public String addAssemblyData(MonitorPointsConfigData parameterData) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	List<MonitorPointsConfigData> result = getVersionByPointVersionId(parameterData.getPointVersionId());
	String assemblyId = "a_" + getNumberForPK();
	AssemblyInfoData assemblyInfoData = new AssemblyInfoData();
	assemblyInfoData.setAssemblyId(assemblyId);
	if (result != null && result.size() > 0 && insertAssemblyInfo(assemblyInfoData) > 0) {
	    MonitorPointsConfigData mpc = result.get(0);
	    if (mpc.getAssemblyId() == null) {
		mpc.setAssemblyId(assemblyId + ";");
		mpc.setAssemblyType(parameterData.getAssemblyType() + ";");
	    } else {
		mpc.setAssemblyId(assemblyId + ";" + mpc.getAssemblyId());
		mpc.setAssemblyType(parameterData.getAssemblyType() + ";" + mpc.getAssemblyType());
	    }
	    ywyjMapper.updateMonitorPointInfo(mpc);
	}
	return assemblyId;
    }

    // 保存组件布局信息
    public Map<String, Object> saveAssemblyInfo(AssemblyInfoData parameterData) {
	List<AssemblyInfoData> list = getAssemblyInfo(parameterData);
	Map<String, Object> result = new HashMap<String, Object>();
	if (list == null || list.size() == 0 || list.get(0).getAssemblyLayout() == null) {
	    StringBuffer controlIdArrays = new StringBuffer();
	    if ("1".equals(parameterData.getAssemblyLayout())) {
		controlIdArrays = insertControl(controlIdArrays, 3);
	    }
	    if ("2".equals(parameterData.getAssemblyLayout()) || "3".equals(parameterData.getAssemblyLayout())) {
		controlIdArrays = insertControl(controlIdArrays, 2);
	    }
	    if ("4".equals(parameterData.getAssemblyLayout())) {
		controlIdArrays = insertControl(controlIdArrays, 1);
	    }
	    parameterData.setControlIdArrays(controlIdArrays.toString());
	    result.put("controlIds", controlIdArrays.toString().split(";"));
	} else {
	    result.put("controlIds", list.get(0).getControlIdArrays().split(";"));
	}
	updateAssemblyInfo(parameterData);
	return result;
    }

    public void insertControl(String controlId) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	YWYJParameterData controlParam = new YWYJParameterData();
	controlParam.setControlId(controlId);
	ywyjMapper.insertControlDataInfo(controlParam);
    }

    public StringBuffer insertControl(StringBuffer controlIds, int size) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	for (int i = 0; i < size; i++) {
	    String controlId = "c_" + getNumberForPK();
	    YWYJParameterData controlParam = new YWYJParameterData();
	    controlParam.setControlId(controlId);
	    ywyjMapper.insertControlDataInfo(controlParam);
	    controlIds = controlIds.append(controlId + ";");
	}
	return controlIds;
    }

    // 获取组件信息
    public List<AssemblyInfoData> getAssemblyInfo(AssemblyInfoData parameterData) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	return ywyjMapper.getAssemblyInfo(parameterData);
    }

    // 插入组件信息
    public int insertAssemblyInfo(AssemblyInfoData parameterData) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	return ywyjMapper.insertAssemblyInfo(parameterData);
    }

    /**
     *
     * <pre>
     * Desc  根据时间戳和随机数获取唯一值，为控件ID、组件ID的生成基础
     * @return
     * @author hufei
     * 2017-11-20 下午5:20:42
     * </pre>
     */
    public String getNumberForPK() {
	String id = "";
	SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
	String temp = sf.format(new Date());
	int random = (int) (Math.random() * 100000);
	id = temp + "_" + random;
	return id;
    }

    public String generateVersion() {
	DecimalFormat df = new DecimalFormat("######0.00");
	return df.format(Math.random() * 10.00);
    }

    public String replaceParams(String sql, int num, Map<String, Object> dmap) {
	for (int i = 0; i < num; i++) {
	    sql = sql.replace("#{p" + (i + 1) + "}", dmap.get("a" + (i + 1)) == null ? "" : dmap.get("a" + (i + 1)).toString());
	}
	return sql;
    }

    /**
     *
     * <pre>
     * Desc  渲染图形
     * @param parameterData
     * @return
     * @author hufei
     * 2017-12-24 下午5:04:49
     * </pre>
     */
    public Map<String, Object> getControlInfo(String authorityAttr, YWYJParameterData parameterData) {
	Map<String, Object> resultMap = new HashMap<String, Object>();
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> cmap = ywyjMapper.getControlInfo(parameterData);
	if (cmap != null && cmap.size() > 0 && cmap.get("query_sql") != null) {
	    // 字段列
	    String fields = cmap.get("show_field") == null ? "" : cmap.get("show_field").toString();
	    String control_typ = cmap.get("control_typ") == null ? "" : cmap.get("control_typ").toString();
	    // 字段数
	    int fields_num = fields.split("\\|").length;
	    String data_sql = cmap.get("query_sql") == null ? "" : cmap.get("query_sql").toString();
	    data_sql = data_sql.replace("#{p1}", parameterData.getAudTrm());
	    if (authorityAttr == null || authorityAttr.isEmpty()) {
		data_sql = data_sql.replace("|#{p2}|", "");
	    } else {
		data_sql = data_sql.replace("#{p2}", "'" + authorityAttr + "'");
	    }
	    if ("lp1_csyjlc0".equals(parameterData.getPointCode())) {
		String data_sql1 = data_sql.substring(data_sql.indexOf("like"));
		String data_sql2 = data_sql1.substring(data_sql1.indexOf("and"), data_sql1.indexOf("{p3}") + 4);
		data_sql = data_sql.replace(data_sql2, "");
	    }
	    data_sql = data_sql.replace("#{p3}", "'" + parameterData.getPointCode() + "'");
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("dataSql", data_sql);
	    resultMap = getChartsResultData(ywyjMapper.executeSql(params), resultMap, control_typ, fields_num);
	    String toolTipString = cmap.get("show_name") == null ? "" : cmap.get("show_name").toString();
	    String[] toolTips = toolTipString.substring(toolTipString.indexOf("|") + 1).split("\\|");
	    cmap.put("toolTips", toolTips);
	}

	resultMap.put("dataInfo", cmap);
	return resultMap;
    }

    // 转换结果集
    public Map<String, Object> getChartsResultData(List<Map<String, Object>> dataList, Map<String, Object> resultMap, String moduleFlag, int fields_num) {
	if (moduleFlag.equals("barCharts")) {// 柱形图
	    Map<String, Object> reobj = new HashMap<String, Object>();
	    List<Object> axList = new ArrayList<Object>();
	    List<Object> byList = new ArrayList<Object>();
	    if (dataList.size() > 0) {
		for (Map<String, Object> mdata : dataList) {
		    axList.add(mdata.get("a") == null ? "" : mdata.get("a").toString());
		    byList.add(mdata.get("b") == null ? 0 : mdata.get("b") instanceof BigDecimal ? ((BigDecimal) mdata.get("b")).doubleValue() : (Integer) mdata.get("b"));
		}
	    }
	    reobj.put("ax", axList);
	    reobj.put("by", byList);
	    resultMap.put("dataset", reobj);
	}
	if (moduleFlag.equals("lineCharts")) {// 折线图
	    Map<String, Object> reobj = new HashMap<String, Object>();
	    List<Object> axList = new ArrayList<Object>();
	    List<Object> byList = new ArrayList<Object>();
	    if (dataList.size() > 0) {
		for (Map<String, Object> mdata : dataList) {
		    axList.add(mdata.get("a") == null ? "" : mdata.get("a").toString());
		    byList.add(mdata.get("b") == null ? 0 : mdata.get("b") instanceof BigDecimal ? ((BigDecimal) mdata.get("b")).doubleValue() : (Integer) mdata.get("b"));
		}
	    }
	    reobj.put("ax", axList);
	    reobj.put("by", byList);
	    resultMap.put("dataset", reobj);
	}
	if (moduleFlag.equals("chartTable")) {// 排名汇总
	    List<Map<String, Object>> pm_list = new ArrayList<Map<String, Object>>();
	    Map<String, Object> data = new TreeMap<String, Object>();
	    for (Map<String, Object> mdata : dataList) {
		char m = 'a';
		data = new TreeMap<String, Object>();
		for (int j = 0; j < fields_num; j++) {
		    data.put(String.valueOf((char) (m + j)), mdata.get(String.valueOf((char) (m + j))));
		}
		pm_list.add(data);
	    }
	    resultMap.put("dataset", pm_list);
	}
	// 地图
	if (moduleFlag.equals("map")) {
	    for (Map<String, Object> dmap : dataList) {
		resultMap.put(dmap.get("prvd_id").toString(), dmap);
	    }
	}
	// 饼图
	if (moduleFlag.equals("pie")) {
	    List<Object> dataobj = new ArrayList<Object>();
	    Map<String, Object> dataMap;
	    if (dataList != null && dataList.size() > 0) {
		for (Map<String, Object> mdata : dataList) {
		    dataMap = new HashMap<String, Object>();
		    dataMap.put("name", mdata.get("a") == null ? "" : mdata.get("a").toString());
		    dataMap.put("y", mdata.get("b") == null ? 0 : mdata.get("b") instanceof BigDecimal ? ((BigDecimal) mdata.get("b")).doubleValue() : (Integer) mdata.get("b"));
		    dataobj.add(dataMap);
		}
	    }
	    resultMap.put("dataset", dataobj);
	}
	// 雷达图
	if (moduleFlag.equals("radarMap")) {
	    Map<String, Object> reobj = new HashMap<String, Object>();
	    List<Object> axList = new ArrayList<Object>();
	    List<Object> by1List = new ArrayList<Object>();
	    if (dataList.size() > 0) {
		for (Map<String, Object> mdata : dataList) {
		    axList.add(mdata.get("a") == null ? "" : mdata.get("a").toString());
		    by1List.add(mdata.get("b") == null ? 0 : mdata.get("b") instanceof String ? Double.valueOf(mdata.get("b").toString()) : mdata.get("b") instanceof BigDecimal ? ((BigDecimal) mdata
			    .get("b")).doubleValue() : (Integer) mdata.get("b"));
		}
	    }
	    reobj.put("ax", axList);
	    reobj.put("by1", by1List);
	    resultMap.put("dataset", reobj);
	}
	// 散点图
	if (moduleFlag.equals("scatter")) {
	    List<Object> reobj = new ArrayList<Object>();
	    List<Object> objList, list;
	    Map<String, Object> objMap;
	    if (dataList.size() > 0) {
		for (Map<String, Object> mdata : dataList) {
		    list = new ArrayList<Object>();
		    objList = new ArrayList<Object>();
		    objMap = new HashMap<String, Object>();
		    list.add(mdata.get("a") == null ? 0 : mdata.get("a") instanceof BigDecimal ? ((BigDecimal) mdata.get("a")).doubleValue() : (Integer) mdata.get("a"));
		    list.add(mdata.get("b") == null ? 0 : mdata.get("b") instanceof BigDecimal ? ((BigDecimal) mdata.get("b")).doubleValue() : (Integer) mdata.get("b"));
		    objList.add(list);
		    objMap.put("name", mdata.get("c") == null ? "" : mdata.get("c").toString());

		    objMap.put("data", objList);
		    reobj.add(objMap);
		}
	    }
	    resultMap.put("dataset", reobj);
	}
	// 气泡图
	if (moduleFlag.equals("bubble")) {
	    Map<String, Object> objMap;
	    List<Object> objList = new ArrayList<Object>();
	    List<Object> axList = new ArrayList<Object>();
	    List<Object> list;
	    String name = null;
	    List<Object> prvdList = new ArrayList<Object>();
	    if (dataList.size() > 0) {
		for (Map<String, Object> mdata : dataList) {
		    list = new ArrayList<Object>();

		    double a = Double.valueOf(mdata.get("a") == null ? "" : mdata.get("a").toString());
		    double b = Double.valueOf(mdata.get("b") == null ? "" : mdata.get("b").toString());
		    String prvdName = mdata.get("d") == null ? "" : mdata.get("d").toString();
		    list.add(a);
		    list.add(b);
		    list.add(mdata.get("c") == null ? 0 : mdata.get("c") instanceof BigDecimal ? ((BigDecimal) mdata.get("c")).doubleValue() : (Integer) mdata.get("c"));
		    if (prvdList != null && prvdList.contains(b)) {
			axList.add(list);
		    } else if (prvdList == null || prvdList.isEmpty()) {
			prvdList.add(b);
			axList.add(list);
			name = prvdName;
		    } else {
			objMap = new HashMap<String, Object>();
			objMap.put("name", name);
			objMap.put("data", axList);
			objList.add(objMap);
			prvdList.add(b);
			axList = new ArrayList<Object>();
			axList.add(list);
			name = prvdName;
		    }
		}
		objMap = new HashMap<String, Object>();
		objMap.put("name", name);
		objMap.put("data", axList);
		objList.add(objMap);
	    }
	    resultMap.put("dataset", objList);
	}

	// 折线图加柱形图
	if (moduleFlag.equals("barAndLineCharts")) {
	    Map<String, Object> reobj = new HashMap<String, Object>();
	    List<Object> axList = new ArrayList<Object>();
	    List<Object> by1List = new ArrayList<Object>();
	    List<Object> by2List = new ArrayList<Object>();
	    if (dataList.size() > 0) {
		for (Map<String, Object> mdata : dataList) {
		    axList.add(mdata.get("a") == null ? "" : mdata.get("a").toString());
		    by1List.add(mdata.get("b") == null ? 0 : mdata.get("b") instanceof BigDecimal ? ((BigDecimal) mdata.get("b")).doubleValue() : (Integer) mdata.get("b"));
		    by2List.add(mdata.get("c") == null ? 0 : mdata.get("c") instanceof BigDecimal ? ((BigDecimal) mdata.get("c")).doubleValue() : (Integer) mdata.get("c"));
		}
	    }
	    reobj.put("ax", axList);
	    reobj.put("by1", by1List);
	    reobj.put("by2", by2List);
	    resultMap.put("dataset", reobj);
	}
	// 瀑布图
	if (moduleFlag.equals("waterfall")) {
	    List<Map<String, Object>> numList1 = new ArrayList<Map<String, Object>>();
	    List<Map<String, Object>> numList2 = new ArrayList<Map<String, Object>>();
	    List<Map<String, Object>> numList3 = new ArrayList<Map<String, Object>>();
	    List<Map<String, Object>> numList4 = new ArrayList<Map<String, Object>>();
	    List<Object> allList = new ArrayList<Object>();
	    Map<String, Object> mapdata = null;
	    if (dataList.size() > 0) {
		for (Map<String, Object> bdata : dataList) {
		    if (bdata.get("a").equals("全网环比增量")) {
			mapdata = new HashMap<String, Object>();
			mapdata.put("name", bdata.get("a"));
			mapdata.put("y", bdata.get("b"));
			numList4.add(mapdata);
		    } else if (bdata.get("b") == null || bdata.get("b").equals("")) {
			mapdata = new HashMap<String, Object>();
			mapdata.put("name", bdata.get("a"));
			mapdata.put("y", bdata.get("b"));
			numList2.add(mapdata);
		    } else if (Long.valueOf(bdata.get("b").toString()) < 0) {
			mapdata = new HashMap<String, Object>();
			mapdata.put("name", bdata.get("a"));
			mapdata.put("y", bdata.get("b"));
			numList3.add(mapdata);
		    } else if (Long.valueOf(bdata.get("b").toString()) > 0) {
			mapdata = new HashMap<String, Object>();
			mapdata.put("name", bdata.get("a"));
			mapdata.put("y", bdata.get("b"));
			numList1.add(mapdata);
		    }
		}
		Collections.reverse(numList3);
		allList.addAll(numList1);
		allList.addAll(numList2);
		allList.addAll(numList3);
		allList.addAll(numList4);
		resultMap.put("pubuData", allList);
	    }
	}
	return resultMap;
    }

    /**
     *
     * <pre>
     * Desc 拼接生成SQL
     * @param parameterData
     * @author hufei
     * 2017-12-24 下午5:04:18
     * </pre>
     */
    public void genSql(YWYJParameterData parameterData) {
	String[] fNames = parameterData.getShowField().split("\\|");
	// String[] wNames = parameterData.getScreenField().split("\\|");
	String tName = parameterData.getTableName();
	// String[] options = option.split("\\|");
	String resultSql = null;
	StringBuilder sb = new StringBuilder();
	sb.append("select ");
	char ch = 97;
	for (String a : fNames) {
	    sb.append(a + " as " + ch + ",");
	    ch++;
	}
	sb = new StringBuilder(sb.substring(0, sb.length() - 1));

	// 根据需求变更后的拼接SQL方法
	sb.append(" from " + tName);
	if (!parameterData.getScreenField().trim().isEmpty()) {
	    sb.append(" where " + parameterData.getScreenField());
	}
	resultSql = sb.toString();
	parameterData.setQuerySql(resultSql);
	updateControllerDataInfo(parameterData);
    }

    public List<MonitorOverViewData> getMonitorOverViewData(String authorityAttr, String level1ProcessCode, String prvdId) {

	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("authorityAttr", authorityAttr);
	paramMap.put("prvdId", prvdId);
	if (!("lp1_csyjlc0".equals(level1ProcessCode))) {
	    paramMap.put("level1ProcessCode", level1ProcessCode);
	}
	List<MonitorOverViewData> MonitorOverViewList = ywyjMapper.getMonitorOverViewData(paramMap);
	return MonitorOverViewList;
    }

    public List<MonitorOverViewData> getLevel1OverViewData(String authorityAttr, String level1ProcessCode) {

	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("authorityAttr", authorityAttr);
	if (!("lp1_csyjlc0".equals(level1ProcessCode))) {
	    paramMap.put("level1ProcessCode", level1ProcessCode);
	}
	List<MonitorOverViewData> MonitorOverViewList = ywyjMapper.getLevel1OverViewData(paramMap);
	return MonitorOverViewList;
    }

    public List<Map<String, String>> getMonitorOverViewMon() {

	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	List<Map<String, String>> MonitorOverViewList = ywyjMapper.getMonitorOverViewMon();
	return MonitorOverViewList;
    }

    /**
     *
     * <pre>
     * Desc 获取版本列表
     * @param pointCode
     * @return
     * @author hufei
     * 2017-12-22 下午6:37:51
     * </pre>
     */
    public List<Map<String, Object>> queryVersion(String createPerson, String pointCode) {
	Map<String, Object> parameterMap = new HashMap<String, Object>(1);
	parameterMap.put("pointCode", pointCode);
	parameterMap.put("createPerson", createPerson);
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	List<MonitorPointsConfigData> monitorPointsConfigList = ywyjMapper.queryVersion(parameterMap);
	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	Map<String, Object> map;
	for (MonitorPointsConfigData data : monitorPointsConfigList) {
	    map = new HashMap<String, Object>(3);
	    map.put("pointVersionId", data.getPointVersionId());
	    map.put("versionCode", data.getVersionCode());
	    map.put("isOpen", data.getIsOpen());
	    result.add(map);
	}
	return result;

    }

    /**
     *
     * <pre>
     * Desc  获取监控点包含的组件ID
     * @param monitorPointsConfigList
     * @return
     * @author hufei
     * 2017-12-22 下午6:36:14
     * </pre>
     */
    private Map<String, Object> getContainsAssembly(List<MonitorPointsConfigData> monitorPointsConfigList) {
	Map<String, Object> result = new HashMap<String, Object>();
	if (monitorPointsConfigList != null && monitorPointsConfigList.size() > 0) {
	    String pointVersionId = monitorPointsConfigList.get(0).getPointVersionId();
	    String[] assemblyId;
	    result.put("pointVersionId", pointVersionId);
	    List<Object> assemblyInfoList = new ArrayList<Object>();
	    if (monitorPointsConfigList.get(0).getAssemblyId() != null) {
		assemblyId = monitorPointsConfigList.get(0).getAssemblyId().split(";");
		Map<String, Object> assemblyInfo;
		for (int i = 0; i < assemblyId.length; i++) {
		    assemblyInfo = new HashMap<String, Object>();
		    assemblyInfo.put("assemblyId", assemblyId[i]);
		    assemblyInfoList.add(assemblyInfo);
		}
	    }
	    result.put("assemblyInfo", assemblyInfoList);
	}
	return result;
    }

    /**
     *
     * <pre>
     * Desc获取版本列表
     * @param pointVersionId
     * @return
     * @author hufei
     * 2017-12-24 下午5:02:03
     * </pre>
     */
    private List<MonitorPointsConfigData> getVersionByPointVersionId(String pointVersionId) {
	Map<String, Object> parameterMap = new HashMap<String, Object>(2);
	parameterMap.put("pointVersionId", pointVersionId);
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	return ywyjMapper.queryVersion(parameterMap);
    }

    /**
     *
     * <pre>
     * Desc更新组件信息
     * @param parameterData
     * @return
     * @author hufei
     * 2017-12-24 下午5:01:34
     * </pre>
     */
    private int updateAssemblyInfo(AssemblyInfoData parameterData) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	List<AssemblyInfoData> assemblyInfoList = getAssemblyInfo(parameterData);
	if (assemblyInfoList == null || assemblyInfoList.size() == 0) {
	    String assemblyId = "a_" + getNumberForPK();
	    parameterData.setAssemblyId(assemblyId);
	    return insertAssemblyInfo(parameterData);
	} else {
	    return ywyjMapper.updateAssemblyInfo(parameterData);
	}
    }

    public List<MonitorOverViewData> getRadarMap(String authorityAttr, String level1ProcessCode) {

	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("authorityAttr", authorityAttr);
	if (!("lp1_csyjlc0".equals(level1ProcessCode))) {
	    paramMap.put("level1ProcessCode", level1ProcessCode);
	}
	List<MonitorOverViewData> MonitorOverViewList = ywyjMapper.getLevel1OverViewData(paramMap);
	return MonitorOverViewList;
    }

    // // 获取版本信息
    // public String getMonitorPointInfo(MonitorPointsConfigData parameterData) {
    // YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
    // parameterData.setIsOpen(1);
    // List<MonitorPointsConfigData> monitorPointsConfigList = ywyjMapper.getMonitorPointInfo(parameterData);
    // if (monitorPointsConfigList == null || monitorPointsConfigList.size() == 0) {
    // String pointVersionId = "pv_" + getNumberForPK();
    // parameterData.setPointVersionId(pointVersionId);
    // if (insertMonitorPointInfo(parameterData) > 0) {
    // return pointVersionId;
    // }
    // return null;
    // } else {
    // return monitorPointsConfigList.get(0).getPointVersionId();
    // }
    // }

    /**
     *
     * <pre>
     * Desc  获取排名汇总-源表列表
     * @param pointCode
     * @return
     * @author hufei
     * 2018-4-10 下午5:25:33
     * </pre>
     */
    public List<Map<String, Object>> getRankTableInfo(String pointCode) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	String tableName = null;
	String busiName = null;
	Map<String, Object> tmpMap = null;
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("pointCode", pointCode);
	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> list = ywyjMapper.getRankTableInfo(paramMap);
	for (Map<String, Object> map : list) {
	    tmpMap = new HashMap<String, Object>();
	    tableName = map.get("rank_result_tname").toString();
	    busiName = map.get("rank_result_businame").toString();
	    tmpMap.put("tableName", tableName);
	    tmpMap.put("busiName", busiName);
	    result.add(tmpMap);
	}
	return result;

    }

    /**
     *
     * <pre>
     * Desc 获取排名汇总、清单的筛选字段
     * @param pointCode
     * @param dataType
     * @return
     * @author hufei
     * 2018-3-21 下午4:07:29
     * </pre>
     */
    public Map<String, Object> getScreenField(String pointCode, String tableName, String dataType) {
	Map<String, Object> resultMap = new HashMap<String, Object>();
	if ("rank".equals(dataType)) {
	    resultMap.put("list", getTableLineForRank(tableName));
	} else {
	    YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	    Map<String, Object> map = ywyjMapper.getDetailTableInfo(pointCode);
	    if (map != null && map.size() > 0) {
		tableName = map.get("detail_table_name").toString();
		resultMap.put("list", getTableLineForRank(tableName));
	    }
	}
	return resultMap;
    }

    /**
     *
     * <pre>
     * Desc 获取排名汇总、清单的筛
     * @param pointCode
     * @param dataType
     * @return
     * @author hufei
     * 2018-3-20 下午4:41:29
     * </pre>
     *
     * public Map<String, Object> getRankOrDetailScreenField(String pointCode, String dataType) { YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class); String tableName =
     * null;
     *
     * Map<String, Object> resultMap = new HashMap<String, Object>(); if ("rank".equals(dataType)) { Map<String, Object> paramMap = new HashMap<String, Object>(); paramMap.put("pointCode", pointCode);
     * List<Map<String, Object>> map = ywyjMapper.getRankTableInfo(paramMap); if (map != null && map.size() > 0) { tableName = map.get(0).get("rank_table_name").toString(); resultMap.put("list",
     * getTableLine(tableName)); } } if ("detail".equals(dataType)) { Map<String, Object> map = ywyjMapper.getDetailTableInfo(pointCode); if (map != null && map.size() > 0) { tableName =
     * map.get("detail_table_name").toString(); resultMap.put("list", getTableLine(tableName)); } } return resultMap; }
     */

    /**
     *
     * <pre>
     * Desc  获取排名汇总、清单表数据
     * @param pointCode
     * @param tableName
     * @param screenField
     * @param audTrm
     * @param prvdId
     * @param pageNum
     * @param pageSize
     * @param dataType
     * @return
     * @author hufei
     * 2018-4-10 下午5:26:26
     * </pre>
     */
    public Map<String, Object> queryRankOrDetailTable(String pointCode, String tableName, String screenField, String audTrm, int prvdId, int pageNum, int pageSize, String dataType) {
	// 1.获取默认的查询SQL及列名
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("pointCode", pointCode);
	String sql = "";
	if ("rank".equals(dataType)) {
	    paramMap.put("tableName", tableName);
	    List<Map<String, Object>> map = ywyjMapper.getRankTableInfo(paramMap);
	    if (map != null && !map.isEmpty()) {
		sql = map.get(0).get("query_sql").toString();
	    } else {
		return null;
	    }
	}
	if ("detail".equals(dataType)) {
	    Map<String, Object> map = ywyjMapper.getDetailTableInfo(pointCode);
	    if (map != null && !map.isEmpty()) {
		sql = map.get("query_sql").toString();
	    } else {
		return null;
	    }
	}

	sql = setParamAndScreenField(sql, screenField, audTrm, prvdId);
	int countResult = getCountResult(sql);
	return getResultByPage(pageNum, pageSize, countResult, sql);
    }

    /*
     * public Map<String, Object> getResultByNull(int pageNum, int pageSize) { Map<String, Object> result = new HashMap<String, Object>(2); result.put("dataList", new ArrayList());
     * result.put("pageBean", new PageBean(1, pageSize, 0)); return result; }
     */
    /**
     *
     * <pre>
     * Desc  2.替换查询参数 3.在查询SQL后拼接筛选条件
     * @param sql:数据库中配置的基本SQL
     * @param screenField：前端拼接的筛选条件
     * @param audTrm：审计月
     * @param prvdId：省份ID
     * @return 替换查询参数，并拼接筛选条件后的SQL
     * @author hufei
     * 2018-3-28 下午3:48:28
     * </pre>
     */
    public String setParamAndScreenField(String sql, String screenField, String audTrm, int prvdId) {

	sql = sql.replace("#{p1}", audTrm);
	sql = sql.replace("#{p2}", String.valueOf(prvdId));
	if (prvdId == 10000) {
//	    if (sql.indexOf("prvd_id=10000") != -1) {
//		sql = sql.replace("prvd_id=10000", "1=1");
//	    }
//	    if (sql.indexOf("CMCC_prov_prvd_id=10000") != -1) {
//		sql = sql.replace("CMCC_prov_prvd_id=10000", "1=1");
//	    }
		Matcher m = Pattern.compile("([cmcc_prov_prvd_idCMCC_PROV_PRVD_ID]{7,17}\\s*=\\s*10000)").matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
		    m.appendReplacement(sb, "1=1");
		}
		m.appendTail(sb);
		sql = sb.toString();
	}
	if (screenField != null && !screenField.isEmpty()) {
	    if (screenField.length() > 4) {
		screenField = screenFieldReplaceAll(screenField);
	    }

	    Matcher m2 = Pattern.compile("contains\\s+'*([^\\s^']+)'*").matcher(screenField);
	    StringBuffer sb2 = new StringBuffer();
		while (m2.find()) {
		    m2.appendReplacement(sb2, " like "+"'%"+m2.group(1)+"%'");
		}
		m2.appendTail(sb2);
		screenField = sb2.toString();


	    Matcher m = Pattern.compile("(\\s+[whereWHERE]{5}\\s+)").matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
		    m.appendReplacement(sb, " where "+screenField+" and ");
		}
		m.appendTail(sb);
		sql = sb.toString();


//	    sql = sql + " and " + screenField;
	}
	return sql;
    }

    /**
     *
     * <pre>
     * Desc  4.拼接count（*）SQL，返回查询出的结果数
     * @param sql 查询SQL
     * @return 结果条数
     * @author hufei
     * 2018-3-28 下午3:51:06
     * </pre>
     */
    public int getCountResult(String sql) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> paramMap = new HashMap<String, Object>(1);
	String queryValue = sql.substring((sql.indexOf("SELECT") != -1 ? sql.indexOf("SELECT") : sql.indexOf("select")) + 6,
		(sql.indexOf("FROM") != -1 ? sql.indexOf("FROM") : sql.indexOf("from")) - 1);
	sql = sql.replace(queryValue, " count(*) as totalRecord ");

    Matcher m = Pattern.compile("(\\s+[orderORDER]{5}\\s+[byBY]{2}\\s+.*)").matcher(sql);
	StringBuffer sb = new StringBuffer();
	while (m.find()) {
	    m.appendReplacement(sb, " ");
	}
	m.appendTail(sb);
	sql = sb.toString();

	paramMap.put("dataSql", sql);
	List<Map<String, Object>> countResult = ywyjMapper.executeSql(paramMap);
	int totalRecord = (Integer) countResult.get(0).get("totalRecord");
	return totalRecord;
    }

    /**
     *
     * <pre>
     * Desc  5.根据偏移量，查出结果
     * @param pageNum 当前页码
     * @param pageSize 每页结果条数
     * @param totalRecord 总结果数
     * @param sql 查询SQL
     * @return 根据偏移量，查询出的结果
     * @author hufei
     * 2018-3-28 下午3:52:00
     * </pre>
     */
    public Map<String, Object> getResultByPage(int pageNum, int pageSize, int totalRecord, String sql) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> paramMap = new HashMap<String, Object>(1);

	PageBean pb = new PageBean(pageNum, pageSize, totalRecord);
	int startIndex = pb.getStartIndex();

	sql = sql.replace(";", "") + " QUALIFY sum(1) over (rows unbounded preceding) between (" + startIndex + ") and (" + (startIndex + pageSize - 1) + ")";
	paramMap.put("dataSql", sql);
	List<Map<String, Object>> dataList = ywyjMapper.executeSql(paramMap);
	Map<String, Object> result = new HashMap<String, Object>(2);
	result.put("rows", dataList);
	result.put("total", totalRecord);
	return result;
    }

    /*
     * public Map<String, Object> queryDetailTable(String pointCode, String screenField, String audTrm, int prvdId, int pageNum, int pageSize) { // 1.获取默认的查询SQL及列名 YwyjMapper ywyjMapper =
     * mybatisDao.getSqlSession().getMapper(YwyjMapper.class); Map<String, Object> map = ywyjMapper.getDetailTableInfo(pointCode); String sql = ""; String column = ""; if (map != null &&
     * !map.isEmpty()) { sql = map.get("query_sql").toString(); column = map.get("query_filed").toString(); } else { return null; } sql = setParamAndScreenField(sql, screenField, audTrm, prvdId);
     * return getResultByPage(pageNum, pageSize, getCountResult(sql), sql); }
     */

    /**
     *
     * <pre>
     * Desc  获取排名汇总、清单的表头
     * @param pointCode
     * @param tableName
     * @param dataType
     * @return
     * @author hufei
     * 2018-4-10 下午5:26:59
     * </pre>
     */
    public List<Map<String, Object>> getTableTitle(String pointCode, String tableName, String dataType) {
	// 1.获取默认的查询SQL及列名
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> map = null;
	List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
	if ("rank".equals(dataType)) {
	    Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("pointCode", pointCode);
	    paramMap.put("tableName", tableName);
	    map = ywyjMapper.getRankTableInfo(paramMap).get(0);
	}
	if ("detail".equals(dataType)) {
	    map = ywyjMapper.getDetailTableInfo(pointCode);
	}
	if (map != null && !map.isEmpty()) {
	    String countSql = map.get("query_sql").toString();
	    String column = map.get("query_filed").toString();
	    String queryValue = countSql.substring((countSql.indexOf("SELECT") != -1 ? countSql.indexOf("SELECT"): countSql.indexOf("select")) + 6,
		    (countSql.indexOf("FROM") != -1 ? countSql.indexOf("FROM") : countSql.indexOf("from")) - 1);
	    // 2.整理表头
	    String[] columns = column.split(",");

	    //将不是列分隔符的，替换成| 以不影响split 用逗号分隔  by pxl
	    Matcher m = Pattern.compile("([^)^,]+,{1}[^(^)]+[)])").matcher(queryValue);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
		    m.appendReplacement(sb, m.group(1).replaceAll(",", "|"));
		}
		m.appendTail(sb);
		queryValue = sb.toString();


	    String[] queryValues = queryValue.split(",");
	    Map<String, Object> titleMap = null;
	    for (int i = 0; i < columns.length && i < queryValues.length; i++) {
		titleMap = new HashMap<String, Object>();
		titleMap.put("title", columns[i]);
		if(queryValues[i].contains("cast")||queryValues[i].contains("CAST")){//这里的约定是  包含cast转换的地方要给as别名  没有转换就不加别名  别名里面不能包含as
			System.err.println(queryValues[i]);
			String title=queryValues[i].trim();
				titleMap.put("field", title.substring((title.lastIndexOf(" as ")>0?title.lastIndexOf(" as "):title.lastIndexOf(" AS "))+4, title.length()).trim());
		}else{
			if(queryValues[i].contains(" as ")||queryValues[i].contains(" AS ")){
				String title=queryValues[i].trim();
				titleMap.put("field", title.substring((title.lastIndexOf(" as ")>0?title.lastIndexOf(" as "):title.lastIndexOf(" AS "))+4, title.length()).trim());
			}else{
				titleMap.put("field", queryValues[i].trim());
			}
		}
		titleList.add(titleMap);
	    }
	}
	return titleList;
    }

    /**
     *
     * <pre>
     * Desc 获取一级流程对应下的监控点数量
     * @param fristlevelCode 一级流程编码
     * @return 一级流程对应的监控点数量
     * @author hufei
     * 2018-4-10 下午5:27:35
     * </pre>
     */
    public Map<String, Object> getPointNumber(String fristlevelCode) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> result = ywyjMapper.getPointNumber(fristlevelCode);
	return result;

    }

    /**
     *
     * <pre>
     * Desc 排名汇总、清单文件下载
     * @param response
     * @param request
     * @param subjectId
     * @param focusCd
     * @param audTrm
     * @param prvdId
     * @param fileType
     * @author hufei
     * 2018-4-10 下午5:28:12
     * </pre>
     */
    public void downFilePage(HttpServletResponse response, HttpServletRequest request, String subjectId, String focusCd, String audTrm, String prvdId, String fileType) {

	String opertype = null;
	if (fileType.equals("audReport")) {
	    opertype = "审计报告下载";
	} else if (fileType.equals("audDetail")) {
	    opertype = "审计清单下载";
	} else if (fileType.equals("auditPm")) {
	    opertype = "排名汇总下载";
	} else if (fileType.equals("auditTB")) {
	    opertype = "审计通报下载";
	}
	if (focusCd == null || focusCd.equals(""))
	    focusCd = subjectId + "000";
	// 记录下载日志
	BgxzData bgxz = new BgxzData();
	bgxz.setAudTrm(audTrm);
	bgxz.setSubjectId(subjectId);
	bgxz.setFocusCd(focusCd);
	bgxz.setPrvdId(Integer.parseInt(prvdId));
	bgxz.setOperType(opertype);
	bgxz.setFileType(fileType);
	bgxz.setCreateType("down");
	List<Map<String, Object>> fileList = bgxzService.selReportLog(bgxz);
	String dlpath = null;
	if (fileList.size() > 0) {
	    Map<String, Object> f = fileList.get(0);
	    dlpath = f.get("file_path") == null ? "" : String.valueOf(f.get("file_path"));
	}
	if (dlpath == null || dlpath.equals("")) {
	    try {
		response.getWriter().print("empty");
	    } catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		logger.error(e1.getMessage(), e1);
	    }

	} else {
	    // 判断是否有初始化数据不完整的记录
	    String fileName = dlpath.substring(dlpath.lastIndexOf("/") + 1, dlpath.length());
	    logger.debug("######  当前下载排名汇总文件名:" + fileName);

		FileUtil.downFileByHttp(request,response, dlpath, fileName, logger);
	}

    }

    /**
     *
     * <pre>
     * Desc 查询排名汇总生成请求
     * @param subjectId 一级流程编码
     * @param focusNum 监控点数量
     * @param isAuto
     * @return
     * @author hufei
     * 2018-4-16 上午9:48:16
     * </pre>
     */
    public List<Map<String, Object>> getFileGenRequst(String subjectId, String focusNum, Boolean isAuto) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("subjectId", subjectId);
	params.put("focusNum", focusNum);
	params.put("isAuto", isAuto);
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	List<Map<String, Object>> result = ywyjMapper.getFileGenRequst(params);
	// List<Map<String, Object>> result = mybatisDao.getList("pmhz.getFileGenRequstNew", params);
	return result;
    }

    /**
     *
     * <pre>
     * Desc 按照ID更新排名汇总状态
     * @param pmhzStatus
     * @param Id
     * @author hufei
     * 2018-4-16 上午9:49:00
     * </pre>
     */
    public void updatePmhzStatus(Integer pmhzStatus, Integer Id) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("pmhzStatus", pmhzStatus);
	params.put("Id", Id);
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	ywyjMapper.updatePmhzStatus(params);
	// mybatisDao.update("pmhz.updatePmhzStatusNew", params);
    }

    /**
     *
     * <pre>
     * Desc  按照审计月/专题更新排名汇总状态
     * @param pmhzStatus
     * @param audTrm
     * @param subjectId
     * @author hufei
     * 2018-4-16 上午9:49:34
     * </pre>
     */
    public void updatePmhzStatusByTrmSub(Integer pmhzStatus, String audTrm, String subjectId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("pmhzStatus", pmhzStatus);
	params.put("audTrm", audTrm);
	params.put("subjectId", subjectId);

	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	ywyjMapper.updatePmhzStatusByTrmSub(params);

	// mybatisDao.update("pmhz.updatePmhzStatusByTrmSubNew", params);
    }

    /**
     * <pre>
     * Desc  查询此次待生成清单文件和审计报告的请求
     * @paramconcernId
     * @return
     * @author peter.fu
     * @refactor peter.fu
     * @date   Mar 9, 2015 3:27:07 PM
     * </pre>
     */
    public List<Map<String, Object>> selectFileGenRequest(String subjectIds) {

	Map<String, Object> params = new HashMap<String, Object>();
	params.put("subjectId", subjectIds);
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	List<Map<String, Object>> list = ywyjMapper.selectFileGenReqList(params);
	return list;
    }

    /**
     * <pre>
     * Desc  查询对应专题和关注点的文件输出配置信息
     * @param subjectId
     * @paramconcernId
     * @return
     * @author peter.fu
     * @refactor peter.fu
     * @date   Mar 10, 2015 4:42:17 PM
     * </pre>
     */
    public Map<String, Object> selectFileConfig(String subjectId, String focusCd) {

	Map<String, Object> params = new HashMap<String, Object>();
	params.put("subjectId", subjectId);
	params.put("focusCd", focusCd);
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> result = ywyjMapper.selectFileConfig(params);
	return result;
    }

    /**
     *
     * <pre>
     * Desc  查询该省该月该一级流程下对应的监控点插入数量
     * @param status
     * @param audTrm
     * @param subjectId
     * @param prvdId
     * @return
     * @author hufei
     * 2018-4-15 下午12:52:21
     * </pre>
     */
    public List<Map<String, Object>> selectFinishedConcerns(Integer status, String audTrm, String subjectId, int prvdId) {

	Map<String, Object> params = new HashMap<String, Object>();
	params.put("status", status);
	params.put("audTrm", audTrm);
	params.put("subjectId", subjectId);
	params.put("prvdId", prvdId);
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	// List<Map<String, Object>> result = ywyjMapper.selectFinishedConcerns(params);
	return ywyjMapper.selectFinishedConcerns(params);
    }

    @Transactional
    public void updateFileGenReqStatusAndTimeBySubjectNew(Map<String, Object> configInfo) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	ywyjMapper.updateFileGenReqStatusAndTimeBySubject(configInfo);
    }

    @Transactional
    public void updateFileRequestExecCountBysubjectNew(Map<String, Object> configInfo) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	ywyjMapper.updateFileGenReqExecCountBySubject(configInfo);
    }

    public List<String> selectAuditResultFile(String audTrm, String subjectId, String focusCd, String fileType) {

	Map<String, Object> params = new HashMap<String, Object>();
	params.put("audTrm", audTrm);
	params.put("subjectId", subjectId);
	params.put("focusCd", focusCd);
	params.put("fileType", fileType);
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	return ywyjMapper.selectAuditResultFile(params);
	// return mybatisDao.getList("FileJobMapper.selectAuditResultFile", params);
    }

    /**
     *
     * <pre>
     * Desc 根据监控点ID获取监控点名称
     * @paramfocusCd
     * @return
     * @author hufei
     * 2018-4-15 下午1:23:18
     * </pre>
     */
    public String getPointName(String pointCode) {
	YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
	Map<String, Object> map = ywyjMapper.getPointNameByCode(pointCode);
	return map.get("point_name").toString();
    }

	public YwyjOVData getLv1Lv2PointInfo(String authorityAttr) {
		YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
		YwyjOVData ywyjOVData = new YwyjOVData();
		Map<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("authorityAttr",authorityAttr);
		List<Map<String,String>> mapListLv1 = ywyjMapper.getLv1Info(paraMap);
		for(Map tMapLv1 :mapListLv1){
			YwyjOVData.Lv1Info lv1Info = ywyjOVData.new Lv1Info();
			lv1Info.setLv1Code((String)tMapLv1.get("lv1Code"));
			lv1Info.setLv1Name((String)tMapLv1.get("lv1Name"));
			Map<String,String> paraMapLv1 = new HashMap<String,String>();
			paraMapLv1.put("lv1Code",lv1Info.getLv1Code());
			paraMapLv1.put("authorityAttr",authorityAttr);
			List<Map<String,String>> mapListLv2 = ywyjMapper.getLv2Info(paraMapLv1);
			for(Map tMapLv2 :mapListLv2){
				YwyjOVData.Lv2Info lv2Info = ywyjOVData.new Lv2Info();
				lv2Info.setLv2Code((String)tMapLv2.get("lv2Code"));
				lv2Info.setLv2Name((String)tMapLv2.get("lv2Name"));
				Map<String,String> paraMapLv2 = new HashMap<String,String>();
				paraMapLv2.put("lv2Code",lv2Info.getLv2Code());
				paraMapLv2.put("lv1Code",lv1Info.getLv1Code());
				paraMapLv2.put("authorityAttr",authorityAttr);
				List<Map<String,String>> mapListPoint = ywyjMapper.getPointInfo(paraMapLv2);
				for(Map tMapPoint :mapListPoint){
					YwyjOVData.PointInfo pointInfo = ywyjOVData.new PointInfo();
					pointInfo.setPointCode((String)tMapPoint.get("pointCode"));
					pointInfo.setPointName((String)tMapPoint.get("pointName"));
					lv2Info.getPoint().add(pointInfo);
				}
				lv1Info.getLv2().add(lv2Info);
			}
			ywyjOVData.getLv1().add(lv1Info);
		}
		return ywyjOVData;
	}

	public List<Map<String,String>> getDataView(String authorityAttr,String audTrm,String lv2Code) {
		YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
		Map<String, String> paramterMap = new HashMap<String, String>();
		paramterMap.put("audTrm",audTrm);
		paramterMap.put("lv2Code",lv2Code);
		paramterMap.put("authorityAttr",authorityAttr);
		List<Map<String,String>> listMap = ywyjMapper.getDataView(paramterMap);
		for(Map<String,String> tMap:listMap){
			for (String key : tMap.keySet()) {
				tMap.put(key,convert(tMap.get(key)));
			}
		}
		return listMap;
	}

	public List<Map<String,String>> getPmhzView(String authorityAttr,String audTrm,String lv2Code) {
		YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
		Map<String, String> paramterMap = new HashMap<String, String>();
		paramterMap.put("audTrm",audTrm);
		paramterMap.put("lv2Code",lv2Code);
		paramterMap.put("authorityAttr",authorityAttr);
		List<Map<String,String>> listMap = ywyjMapper.getPmhzView(paramterMap);
		for(Map<String,String> tMap:listMap){
			for (String key : tMap.keySet()) {
				String ss = tMap.get(key);
				if(ss !=null &&!"".equals(ss)&&ss.indexOf("|")>0){
					String ss1 = ss.substring(0,ss.indexOf("|"));
					String ss2 = ss.substring(ss.indexOf("|"),ss.length());
					tMap.put(key,convert(ss1)+ss2);
				}

			}
		}

		for(Map<String,String> tpMap:listMap){
			String ss = tpMap.get("risk_target_value_10000");
			if(ss !=null &&!"".equals(ss)&&ss.indexOf("|")>0) {
				ss = ss.substring(0, ss.indexOf("|"));
				tpMap.put("risk_target_value_10000", ss);
			}
		}

		return listMap;
	}


	public Map<String,Object> getZZT(String audTrm,String pointCode){
    	String titlename ="";
		YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
		Map<String, String> paramterMap = new HashMap<String, String>();
		paramterMap.put("audTrm",audTrm);
		paramterMap.put("pointCode",pointCode);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> audTrmList = new ArrayList<String>();
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> listMap = ywyjMapper.getZZT(paramterMap);
		for(Map<String,Object> tMap:listMap){
			titlename = tMap.get("risk_target_nm").toString();
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("name",tMap.get("risk_target_prvd_nm").toString());

			Map<String, String> paramterMap1 = new HashMap<String, String>();
			paramterMap1.put("audTrmS",dateMinusMonth(audTrm,-2));
			paramterMap1.put("audTrmE",audTrm);
			paramterMap1.put("prvdId",tMap.get("risk_target_prvd_id").toString());
			paramterMap1.put("pointCode",pointCode);

			List<Map<String,Object>> listMap1 = ywyjMapper.getZZTOther(paramterMap1);
			List<Double> listValue = new ArrayList<Double>();
			for(Map<String,Object> tMap1:listMap1){
				listValue.add(Double.parseDouble(tMap1.get("risk_target_prvd_value").toString()));
			}
			map1.put("data",listValue);

			dataList.add(map1);

		}
		audTrmList.add(audTrm);
		audTrmList.add(dateMinusMonth(audTrm,-1));
		audTrmList.add(dateMinusMonth(audTrm,-2));
		resultMap.put("dataList",dataList);
		resultMap.put("audTrmList",audTrmList);
		resultMap.put("titlename",titlename);
		return resultMap;
	}

	public Map<String,Object> getZXT(String authorityAttr,String audTrm,String lv2Code){
		YwyjMapper ywyjMapper = mybatisDao.getSqlSession().getMapper(YwyjMapper.class);
		List<String> prvdName = new ArrayList<String>();
		Map<String, String> paramterMap = new HashMap<String, String>();
		paramterMap.put("audTrm",audTrm);
		paramterMap.put("lv2Code",lv2Code);
		paramterMap.put("authorityAttr",authorityAttr);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String,Object>> listMap = ywyjMapper.getZXT(paramterMap);
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> tMap:listMap){
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("name",tMap.get("point_name").toString());
			List<Double> listValue = new ArrayList<Double>();
			for(int i=10100;i<=13100;i=i+100){
				String ss= "risk_target_value_"+String.valueOf(i);
				listValue.add(Double.parseDouble(tMap.get(ss).toString()));
			}
			map1.put("data",listValue);
			dataList.add(map1);
		}
		resultMap.put("dataList",dataList);
		prvdName.add("北京");
		prvdName.add("上海");
		prvdName.add("天津");
		prvdName.add("重庆");
		prvdName.add("贵州");
		prvdName.add("湖北");
		prvdName.add("陕西");
		prvdName.add("河北");
		prvdName.add("河南");
		prvdName.add("安徽");
		prvdName.add("福建");
		prvdName.add("青海");
		prvdName.add("甘肃");
		prvdName.add("浙江");
		prvdName.add("海南");
		prvdName.add("黑龙江");
		prvdName.add("江苏");
		prvdName.add("吉林");
		prvdName.add("宁夏");
		prvdName.add("山东");
		prvdName.add("山西");
		prvdName.add("新疆");
		prvdName.add("广东");
		prvdName.add("辽宁");
		prvdName.add("广西");
		prvdName.add("湖南");
		prvdName.add("江西");
		prvdName.add("内蒙古");
		prvdName.add("云南");
		prvdName.add("四川");
		prvdName.add("西藏");

		resultMap.put("prvdName",prvdName);

		return resultMap;
	}

	/**
	 * 日期减几月
	 */
	public String dateMinusMonth(String str,Integer i) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Date dt = null;//将字符串生成Date
		try {
			dt = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);//使用给定的 Date 设置此 Calendar 的时间。
		rightNow.add(Calendar.MONTH, i);// 日期减1个月
		Date dt1 = rightNow.getTime();//返回一个表示此 Calendar 时间值的 Date 对象。
		String reStr = sdf.format(dt1);//将给定的 Date 格式化为日期/时间字符串，并将结果添加到给定的 StringBuffer。
		return reStr;
	}

	//如小于0.01%，则小数点保留到小数后出现数字为止,大于0.01%保留两位小数
	public String convert(String param) {

		StringBuilder sb = new StringBuilder();
		Integer index = 0;
		for (int i = param.indexOf(".") + 1; i < param.length(); i++) {
			String ch = param.substring(i, i + 1);
			if ("0".equals(ch))
				index++;
			else
				break;
		}
		for (int j = 0; j < index + 1; j++) {//有数字为止后一位
			sb.append("0");
		}

		DecimalFormat df = new DecimalFormat("###,###,###,##0." + sb.toString());

		DecimalFormat df2 = new DecimalFormat("###,###,###,##0.00");
		Double d =0.0;
		try{
			d = Double.valueOf(param.replace("%", "").replace(",", ""));
		}catch(Exception e){
			logger.error(e.getMessage());
			return param;
		}


		String result = "";
		if (d >= 0.01)
			result = df2.format(d);
		else {
			result = df.format(d);
			while (true) {
				if ("0".equals(result.substring(result.length() - 1, result.length())))
					result = result.substring(0, result.length() - 1);
				else
					break;
			}

		}
		if (".".equals(result.substring(result.length() - 1, result.length()))) {
			result = result.substring(0, result.length() - 1) ;
		}
		if(param.contains("%"))
			result = result + "%";
		//System.out.print(result); //result+"%";//df.format(d)+"%";

		return result;
	}
}
