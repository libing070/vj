/**
 * com.hpe.cmca.interfaces.SjkgMapper.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.interfaces;

import java.util.List;

import com.hpe.cmca.pojo.SjkgData;


/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2017-11-10 上午10:30:11
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-11-10 	   hufei 	         1. Created this class. 
 * </pre>  
 */
public interface SjkgMapper {
    //审计开关-获取专题列表
    public List<SjkgData> getSubjectList(SjkgData sjkgData);
    //审计开关-根据专题获取审计月
    public List<SjkgData> getAudTrmBySubject(SjkgData sjkgData);
    //审计开关-根据专题&&审计月 获取可以新增的开关类型
    public List<SjkgData> getSwitchType(SjkgData sjkgData);
    //审计开关-保存新增信息
    public int saveSwitchInfo(SjkgData sjkgData);
    //审计开关-查询开关信息
    public List<SjkgData> getSwitchInfoList(SjkgData sjkgData);
    //审计开关-更新开关状态
    public int updateSwitchInfo(SjkgData sjkgData);
    //审计开关-模拟甘特图
    public List<SjkgData> getGanttChart(SjkgData sjkgData);
    //审计开关-获取气泡图数据
    public List<SjkgData> getBubbleChart(SjkgData sjkgData);
    //审计开关-获取同一天打开两个开关
    public List<SjkgData> getOpenSwitchOneDay(SjkgData sjkgData);
    
    
}
