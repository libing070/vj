/**
 * com.hpe.cmca.interfaces.SystemLogMgMapper.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.interfaces;

import java.util.List;
import java.util.Map;

import com.hpe.cmca.pojo.SystemLogControlData;
import com.hpe.cmca.pojo.SystemLogHtmlData;
import com.hpe.cmca.pojo.SystemLogLoginData;
import com.hpe.cmca.pojo.SystemLogModelData;
import com.hpe.cmca.pojo.SystemLogOtapaData;
import com.hpe.cmca.pojo.SystemLogReqmData;
import com.hpe.cmca.pojo.SystemLogUseData;


/**
 * <pre>
 * Desc： 对系统的操作、尤其是像审计结果、审计开关等关键功能的操作进行记录，增加系统日志查询功能，包括系统登录日志、系统操作日志、功能操作日志
 * @author   zhangqiang
 * @refactor zhangqiang
 * @date     2018-8-2
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2018-8-2 	   zhangqiang 	         1. Created this class. 
 * </pre>  
 */
public interface SystemLogMgMapper {
	
	// 插入对审计开关功能模块进行的操作的日志信息 
	public Integer addControlLogData(SystemLogControlData systemLogControlData);
	
	// 插入需求管理日志 -- 直接插入
	public Integer addReqmLogData(SystemLogReqmData systemLogReqmData);
	
	// 插入需求管理日志 -- 通过需求编号查询后插入
	public Integer addReqmLogOfIdData(SystemLogReqmData systemLogReqmData);
	
	// 插入 参数管理功能模块日志
	public Integer addOtapaLogData(SystemLogOtapaData systemLogOtapaData);
	
	// 插入 参数管理功能模块日志 -- 针对新增数据
	public Integer addNewOtapaLogData(SystemLogOtapaData systemLogOtapaData);
	
	// 插入审计报告、清单、排名汇总操作日志
	public Integer addModelLogData(SystemLogModelData systemLogModelData);
    
    // 登陆日志-管理界面，查询默认展示数据
    //public Map<String,Map<String, Object>>  queryDefaultData(SystemLogHtmlData systemLogHtmlData);
    
    // 登陆日志-管理界面，查询展示数据 queryDefLogData
    public List<SystemLogLoginData>  queryDefLogData(SystemLogHtmlData systemLogHtmlData);
    
    // 登陆日志-管理界面，查询默认展示筛选项数据 queryTermData
    public List<Map<String, Object>>  queryTermData(SystemLogHtmlData systemLogHtmlData);
    
    // 登陆日志-管理界面，查询展示数据 queryLoginTableData
    public List<SystemLogLoginData>  queryLoginTableData(SystemLogHtmlData systemLogHtmlData);
    
    // 系统操作日志-管理界面，查询默认展示筛选项数据 queryUseTermData
    public List<Map<String, Object>>  queryUseTermData(SystemLogHtmlData systemLogHtmlData);
    
    // 系统操作日志-管理界面，查询默认展示筛选项数据--功能菜单list queryUseToolData
    public List<Map<String, Object>>  queryUseToolData(SystemLogHtmlData systemLogHtmlData);
    
    // 系统操作日志-管理界面，查询默认展示筛选项数据--功能菜单list queryUseToolData
    //public List<Map<String, Object>>  mohuQueryUseToolData(SystemLogHtmlData systemLogHtmlData);
    
    // 系统操作日志-管理界面，查询展示数据 queryUseTableData
    public List<SystemLogUseData>  queryUseTableData(SystemLogHtmlData systemLogHtmlData);
    
    // 新增用户登录信息
    public Integer addLoginInfoData(SystemLogLoginData systemLogLoginData);
      
    // 功能操作日志操作审计报告、审计清单、排名汇总的日志-管理界面，查询展示数据 queryLogModelTableData
    public List<SystemLogModelData>  queryLogModelTableData(SystemLogHtmlData systemLogHtmlData);
    
    // 功能操作日志操作审计报告、审计清单、排名汇总的日志-管理界面，查询默认展示筛选项数据 queryUseTermData
    public List<Map<String, Object>>  queryLogModelTermData();
    
    // 持续审计系统用户，对需求管理功能模块进行的操作的日志信息： 查询界面默认的筛选条件list 
    // 需求名称list
    public List<Map<String, Object>>  queryReqmTermData();
    
    // 持续审计系统用户，对需求管理功能模块进行的操作的日志信息： 查询界面操作日志表格内容
    public List<SystemLogReqmData>  queryReqmTableData(SystemLogHtmlData systemLogHtmlData);
    
    // 持续审计系统用户，对参数管理功能模块进行的操作的日志信息： 查询界面默认的筛选条件list 
    // 关注点list
    public List<Map<String, Object>>  queryOtapaTermFocData();
    
    // 审计月list
    public List<Map<String, Object>>  queryOtapaTermSubData();
    
    // 持续审计系统用户，对参数管理功能模块进行的操作的日志信息： 查询界面操作日志表格内容
    public List<SystemLogOtapaData>  queryOtapaTableData(SystemLogHtmlData systemLogHtmlData);
    
    // 持续审计系统用户，对审计开关功能模块进行的操作的日志信息： 查询界面默认的筛选条件list 
    // 审计专题list
    public List<Map<String, Object>>  queryControlTermSubData();
    
    // 审计月list
    public List<Map<String, Object>>  queryControlTermTmData();
    
    // 持续审计系统用户，对审计开关功能模块进行的操作的日志信息： 查询界面操作日志表格内容
    public List<SystemLogControlData>  queryControlTableData(SystemLogHtmlData systemLogHtmlData);
    
    // 操作审计报告、审计清单、排名汇总的日志： 查询界面默认的筛选条件list
    // 审计专题list
    public List<Map<String, Object>>  queryModelTermSubData();
    
    // 审计月list
    public List<Map<String, Object>>  queryModelTermTmData();
    
    // 文件类型list
    public List<Map<String, Object>>  queryModelTermTypeData();
    
    // 文件所属省list
    public List<Map<String, Object>>  queryModelTermPrvdData();
    
    // 操作审计报告、审计清单、排名汇总的日志： 查询界面操作日志表格内容
    public List<SystemLogModelData>  queryLogModelTbData(SystemLogHtmlData systemLogHtmlData);
    
    // 通过专题id获取审计月
    public List<Map<String, Object>> getAudtrmOfId(SystemLogHtmlData systemLogHtmlData);
    
    // 通过专题id获取审计月
    public List<Map<String, Object>> getFocusBySubId(SystemLogHtmlData systemLogHtmlData);
    
    /**
     * 记录所有hpmgr.busi_report_log的相关操作记录
     */
    
    // 来源 bgxzMapper.xml
	public void addReportLog(Map<String, Object> params);
	
	// 来源 bgxzMapper.xml
	public void updateReportLog(Map<String, Object> params);
	
	// 来源 bgxzMapper.xml
	public void updateReportLog2(Map<String, Object> params);
	
	// 来源 bgxzMapper.xml
	public void updateReportLog7(Map<String, Object> params);
	
	// 来源 bgxzMapper.xml
	public void deleteReportLog(Map<String, Object> params);
	    
	// 来源 SynchronizeTwoSysMapper.xml
	//从老系统向新系统添加审计报告清单路径
	public int addReportAndCsvPathToNew(Map<String,Object> map);
	
	//从新系统删除审计报告清单路径
	public int delReportAndCsvPathFromNew(Map<String,Object> map);
	
	//从老系统向新系统添加排名汇总和审计通报路径
	public int addPmhzAndSjtbPathToNew(Map<String,Object> map);
	
	//从新系统删除排名汇总和审计通报路径
	public int delPmhzAndSjtbPathFromNew(Map<String,Object> map); 
	
}
