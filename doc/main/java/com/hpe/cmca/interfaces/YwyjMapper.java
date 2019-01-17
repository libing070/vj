/**
 * com.hpe.cmca.interfaces.YwyjMapper.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.interfaces;

import java.util.List;
import java.util.Map;

import com.hpe.cmca.pojo.AssemblyInfoData;
import com.hpe.cmca.pojo.MonitorOverViewData;
import com.hpe.cmca.pojo.MonitorPointsConfigData;
import com.hpe.cmca.pojo.YWYJParameterData;

/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2017-11-7 下午5:25:52
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-11-7 	   hufei 	         1. Created this class. 
 * </pre>  
 */
public interface YwyjMapper {
    //获取控件-数据源-表详情信息
    public List<Map<String,Object>> getTableDetails(Map map);
    //获取控件-数据源-列表信息
    public Map<String,Object> getTableInfo(YWYJParameterData parameterData);
    //获取控件信息
    public Map<String,Object> getControlInfo(YWYJParameterData parameterData);
    //插入控件信息表
    public int insertControlDataInfo(YWYJParameterData parameterData);
    //更新控件信息表
    public int updateControlDataInfo(YWYJParameterData parameterData);
    //填充控件的数据
    public List<Map<String,Object>> getControldataInfos(YWYJParameterData parameterData );
    
    public List<Map<String,Object>> executeSql(Map<String,Object> m);
    
    //获取组件信息
    public List<AssemblyInfoData> getAssemblyInfo(AssemblyInfoData parameterData);
    //更新组件信息
    public int updateAssemblyInfo(AssemblyInfoData parameterData);
    //插入组件信息
    public int insertAssemblyInfo(AssemblyInfoData parameterData);
    //获取监控点版本表信息
    public List<MonitorPointsConfigData> getMonitorPointInfo(Map<String,Object> parameterMap);
    //插入监控点版本表信息
    public int insertMonitorPointInfo(MonitorPointsConfigData parameterData);
    //更改监控点版本表信息
    public int updateMonitorPointInfo(MonitorPointsConfigData parameterData);
    //更监控点其他版本为非默认版本信息
    public int setNonDefaultVersion(MonitorPointsConfigData parameterData);
    //更监控点版本为默认版本信息
    public int setDefaultVersion(MonitorPointsConfigData parameterData);
    
    public List<MonitorOverViewData> getMonitorOverViewData(Map<String,Object> param);
    
    public List<MonitorOverViewData> getLevel1OverViewData(Map<String,Object> param);
    
    public List<Map<String,String>> getMonitorOverViewMon();
    //查询版本列表
    public List<MonitorPointsConfigData> queryVersion(Map<String,Object> parameterMap);
    //删除控件信息
    public void deleteControlInfo(String controlId);
    //删除组建信息
    public void deleteAssemblyInfo(String assemblyId);
    //业务预警-新增版本-复制控件
    public int copyOfControl(Map<String,Object> map);
    //业务预警-新增版本-复制组件
    public int copyOfAssembly(Map<String,Object> map);
    //业务预警-新增版本-复制版本
    public int copyOfVersion(Map<String,Object> map);
    
    public List<Map<String,Object>> queryMonth(Map<String,Object> map);
    
    //业务预警-排名汇总-源表
    public List<Map<String,Object>> getRankTableInfo(Map<String,Object> map);
    //业务预警-清单-源表
    public Map<String,Object> getDetailTableInfo(String pointCode);
    
    //业务预警-获取一级流程下对应的监控点数量
    public Map<String,Object> getPointNumber(String  fristlevelCode);
    //业务预警-排名汇总-生成请求
    public List<Map<String,Object>> getFileGenRequst(Map<String,Object> map);
    //业务预警-排名汇总-更改状态
    public int updatePmhzStatus(Map<String,Object> map);
    //业务预警-排名汇总-更改状态按审计月和专题
    public int updatePmhzStatusByTrmSub(Map<String,Object> map);
    //业务预警-审计清单-查询此次待生成清单文件和审计报告的请求
    public List<Map<String,Object>> selectFileGenReqList(Map<String,Object> map);
    //业务预警-审计清单-查询对应专题和关注点的文件输出配置信息
    public Map<String,Object> selectFileConfig(Map<String,Object> map);
    //业务预警-审计清单-查询该省该月该一级流程下对应的监控点插入数量
    public List<Map<String,Object>> selectFinishedConcerns(Map<String,Object> map);
    //业务预警-审计清单-查询该省该月该一级流程下对应的监控点插入数量
    public int updateFileGenReqStatusAndTimeBySubject(Map<String,Object> map);    
    //业务预警-审计清单-查询该省该月该一级流程下对应的监控点插入数量
    public int updateFileGenReqExecCountBySubject(Map<String,Object> map);
    //业务预警-审计清单-查询该监控点该月生成的文件名
    public List<String> selectAuditResultFile(Map<String,Object> map);
    
    //业务预警-清单-根据监控点ID获取监控点名称
    public Map<String,Object> getPointNameByCode(String pointCode);
    
    public List<Map<String,String>> getLv1Info(Map<String,String> para);

    public List<Map<String,String>> getLv2Info(Map<String,String> para);

    public List<Map<String,String>> getPointInfo(Map<String,String> para);

    public List<Map<String,String>> getDataView(Map<String,String> para);

    public List<Map<String,String>> getPmhzView(Map<String,String> para);

    public List<Map<String,Object>> getZZT(Map<String,String> para);

    public List<Map<String,Object>> getZZTOther(Map<String,String> para);

    public List<Map<String,Object>> getZXT(Map<String,String> para);
    
}
