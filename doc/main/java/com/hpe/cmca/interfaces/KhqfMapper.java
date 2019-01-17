package com.hpe.cmca.interfaces;

import java.util.List;

import com.hpe.cmca.pojo.KhqfData;
import com.hpe.cmca.pojo.ParameterData;


public interface KhqfMapper {
    //获取欠费金额排名（柱状图）
    public List<KhqfData> getAmountColumnData(ParameterData parameter);
    //获取集团欠费数量排名-全国（柱形图）
    public List<KhqfData> getJTNumberPm(ParameterData khqf);
    //获取集团欠费数量排名-地市（柱形图）
    public List<KhqfData> getJTNumPrvdData(ParameterData khqf);
    //获取个人欠费数量排名-全国（柱形图）
    public List<KhqfData> getGRNumberData(ParameterData khqf);
    //获取欠费金额折线图-全国/省份
    public List<KhqfData> getAmountLineData(ParameterData khqf);
    //获取集团欠费增量分析-全国（增量分析图）
    public List<KhqfData> getIncrementalData(ParameterData khqf);
    //获取欠费金额分布(饼图)-省份/全国
    public KhqfData getAmountPie(ParameterData parameter);
    //获取集团欠费数量排名-全国（折线图）
    public List<KhqfData> getJTNumberPmZheXian(ParameterData khqf);
    //统计分析 ->统计报表-> 排名汇总
    public List<KhqfData> getJTNumberDataPaiming(ParameterData khqf);
    //统计分析 ->统计报表-> 个人客户欠费账龄
    public List<KhqfData> getGRNumberDataQfAge(ParameterData khqf);
    //统计分析 ->统计报表-> 欠费账龄(集团) 
    public List<KhqfData> getOrgOweAging(ParameterData khqf);
    //统计分析 ->统计报表-> 欠费金额(集团) 
    public List<KhqfData> getOrgAmount(ParameterData khqf);
    //统计分析 ->统计报表-> 个人客户欠费金额
    public List<KhqfData> getGRNumberDataQfAmt(ParameterData khqf);
    //获取欠费风险地图的数据
    public List<KhqfData> getMapData(ParameterData khqf);
    //获取欠费风险地图下方的数据
    public List<KhqfData> getMapBottomData(ParameterData khqf);
   //统计分析 ->统计报表-> 新增原有欠费分布（折线）
    public List<KhqfData> getJTNumberDataQffenbu(ParameterData khqf);
    //审计报告个人欠费数据
    public List<KhqfData> getSjbgGrData(ParameterData khqf);
    //审计报告集团欠费数据
    public List<KhqfData> getSjbgJtData(ParameterData khqf);
    //风险地图地市下钻清单数据
    public List<KhqfData> getSjbgCtyDetData(ParameterData khqf);
    
    //集团欠费回收数据
    public List<KhqfData> getJTQFHSData(ParameterData khqf);
    
    //个人欠费回收数据
    public List<KhqfData> getGRQFHSData(ParameterData khqf);
    //获取统计分析-整改问责-整改问责统计-六个月内达到的次数-柱状图
    public List<KhqfData> getRectifyForSixColumn(ParameterData parameterData);
    //获取统计分析-整改问责-整改问责统计-累计达到的次数-柱状图
    public List<KhqfData> getRectifyColumn(ParameterData parameterData);
    //获取统计分析-整改问责-整改问责统计-累计达到的次数-折线图
    public List<KhqfData> getRectifyLine(ParameterData parameterData);
    
}
