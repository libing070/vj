/**
 * com.hpe.cmca.interfaces.XqglMapper.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.interfaces;

import java.util.List;
import java.util.Map;

import com.hpe.cmca.pojo.XqglData;
import com.hpe.cmca.pojo.XqglListData;


/**
 * <pre>
 * Desc：
 * @author   hufei
 * @refactor hufei
 * @date     2018-4-16 下午4:29:01
 * @version  1.0
 *
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  2018-4-16 	   hufei 	         1. Created this class.
 * </pre>
 */
public interface XqglMapper {

    //需求管理-突发性数据统计-列表获取
    public List<XqglListData> getList(XqglListData xqglListData);
    //需求管理-突发性数据统计-获取配置项
    public List<Map<String,Object>> getSysConfig(Map<String,Object> map);
    //需求管理-突发性数据统计-获取详情
    public List<XqglData> getDetailById(Map<String,Object> map);
    //需求管理-突发性数据统计-新增需求
    public int  addRequirement(XqglData xqglData);
    //需求管理-突发性数据统计-获取当天最大需求编号
    public Map<String,Object> getCurrentMaxId(Map<String,Object> map);
    //需求管理-突发性数据统计-编辑需求
    public int editRequirementByNew(XqglData xqglData);
    public int editRequirementByApprove(XqglData xqglData);
    public int editRequirementByHandle(XqglData xqglData);
    //需求管理-突发性数据统计-文件生成-判断文件是否存在
    public List<Map<String, Object>> getTable(Map<String,Object> map);
    //需求管理-突发性数据统计-获取正在生成的文件数量
    public int getRunList(Map<String,Object> map);
    //需求管理-突发性数据统计-更新需求状态、生成时间
    public int updateRequirement(Map<String,Object> map);
    //需求管理-获取导出列表
    public List<XqglData> getOutPutList(XqglListData xqglListData);
    //需求管理-突发性数据统计-记录下载日志
    public int insertDownLoadLog(Map<String,Object> map);

    public void updateAttachAddr(Map<String,Object> map);

    public void updateHandleAttachAddr(Map<String,Object> map);

    public void updateAttachAddrByAddr(Map<String,Object> map);

    public void updateGenAddrByAddr(Map<String,Object> map);

    public int getMyReqNum(Map<String,Object> map);

    public int getDoneReqNumByApprove(Map<String,Object> map);

    public int getDoneReqNumByHandle();

}
