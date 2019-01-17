/**
 * com.hpe.cmca.interfaces.ZdtlMapper.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.interfaces;

import java.util.List;
import java.util.Map;

import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.pojo.ZdtlData;


/**
 * <pre>
 * Desc： 终端套利接口表
 * @author   hufei
 * @refactor hufei
 * @date     2017-7-13 上午10:06:58
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-7-13 	   hufei 	         1. Created this class. 
 * </pre>  
 */
public interface ZdtlMapper {
    //获取风险地图数据
    public List<ZdtlData> getMapData(ParameterData parameterData);
    //地图下方图片-异常销售
    public List<ZdtlData> getMapBottomData(ParameterData parameterData);
    //地图下方图片-套利
    public List<ZdtlData> getMapBottomCJData(ParameterData parameterData);
    //获取地市渠道信息
    public List<ZdtlData> getChnlTable(ParameterData parameterData);
    //根据渠道名称查询渠道信息
    public List<ZdtlData> getChnlByChnlName(ParameterData parameterData);
    //获取金额排名-柱状图
    public List<ZdtlData> getAmountColumnData(ParameterData parameterData);
    //获取金额占比排名-柱状图
    public List<ZdtlData> getAmountPercentColumnData(ParameterData parameterData);
    //获取占比排名-柱状图
    public List<ZdtlData> getPercentColumnData(ParameterData parameterData);
    //获取异常销售数量排名-柱状图
    public List<ZdtlData> getNumberColumnData(ParameterData parameterData);
    //获取金额排名-折线图
    public List<ZdtlData> getAmountLineData(ParameterData parameterData);
    //获取占比排名-折线图
    public List<ZdtlData> getPercentLineData(ParameterData parameterData);
    //获取异常销售渠道数量-柱状图
    public List<ZdtlData> getNumberChnlColumnData(ParameterData parameterData);
    //获取异常销售渠道占比-柱状图
    public List<ZdtlData> getPercentChnlColumnData(ParameterData parameterData);
    //获取异常销售渠道占比-折线图
    public List<ZdtlData> getPercentChnlLineData(ParameterData parameterData);
    //获取渠道-近6个月占比趋势信息
    public List<ZdtlData> getChnlTrend(ParameterData parameterData);
    //获取渠道基本信息
    public List<Map> getChnlBaseInfo(ParameterData parameterData);
    //获取统计分析-排名汇总
    public List<ZdtlData> getRankTable(ParameterData parameterData);
    //获取统计分析-增量分析数据
    public List<ZdtlData> getIncrementalData(ParameterData parameterData);
    //获取统计分析-违规类型分布-饼图
    public ZdtlData getTypeDistributePie(ParameterData parameterData);
    //获取统计分析-违规类型分布-堆积图
    public List<ZdtlData> getTypeDistributeStack(ParameterData parameterData);
    //获取统计分析-整改问责-整改问责统计-六个月内达到的次数
    public List<ZdtlData> getRectifyForSixColumn(ParameterData parameterData);
    //获取统计分析-整改问责-整改问责统计-累计达到整改的次数-柱状图
    public List<ZdtlData> getRectifyColumn(ParameterData parameterData);
    //获取统计分析-整改问责-整改问责统计-累计达到整改的次数-折线图
    public List<ZdtlData> getRectifyLine(ParameterData parameterData);
    //获取统计分析-审计报告
    public List<ZdtlData> getReportInfo(ParameterData parameterData);
    //获取统计分析-整改问责-整改问责统计-累计达到问责的次数-柱状图
    public List<ZdtlData> getAccountForSixColumn(ParameterData parameterData);
    //获取统计分析-整改问责-整改问责统计-累计达到问责的次数-柱状图
    public List<ZdtlData> getAccountabilityColumn(ParameterData parameterData);
    //获取统计分析-整改问责-整改问责统计-累计达到问责的次数-折线图
    public List<ZdtlData> getAccountabilityLine(ParameterData parameterData);   
    //获取统计分析-整改问责-整改问责统计-重点关注事项-table
    public List<Map> getFocusThingTable(ParameterData parameterData); 
}
