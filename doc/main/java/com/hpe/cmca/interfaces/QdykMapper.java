package com.hpe.cmca.interfaces;

import java.util.List;
import java.util.Map;

import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.pojo.QdykData;


public interface QdykMapper {
	//养卡号码占比排名 （柱形图）
   public List<QdykData> getCardPercentPm(ParameterData qdykdata);
   //养卡号码数量排名 （柱形图）
   public List<QdykData> getCardNumbersPm(ParameterData qdykdata);
   //养卡渠道数量排名 （柱形图）
   public List<QdykData> getChnlNumbersPm(ParameterData qdykdata);
   //养卡渠道占比排名 （柱形图）
   public List<QdykData> getChnlPercentPm(ParameterData qdykdata);
   //养卡号码占比趋势（折线图）
   public List<QdykData> getCardPercentPmLine(ParameterData qdykdata);
   //养卡号码个数趋势（折线图）
   public List<QdykData> getCardNumPmLine(ParameterData qdykdata);
   //养卡渠道占比趋势（折线图）
   public List<QdykData> getChanlPercentPmLine(ParameterData qdykdata);
   //养卡渠道数量趋势（折线图）
   public List<QdykData> getChnlNumsPmLine(ParameterData qdykdata);
   //风险地图
   public List<QdykData> getQdykMap(ParameterData qdykdata);
   //风险地图下方卡片
   public List<QdykData> getQdykMapBottom(ParameterData qdykdata);
   //养卡地市下钻渠道表格
   public List<QdykData> getQdykChnlTable(ParameterData qdykdata);
   //养卡渠道基本信息
   public List<Map> getQdykChnlBaseInfo(ParameterData qdykdata);
   //渠道养卡趋势
   public List<QdykData> getQdykChnlTrend(ParameterData qdykdata);
   //统计分析  排名汇总
   public List<QdykData> getQdykDataPm(ParameterData qdykdata);
   //增量分析
   public List<QdykData> getIncrementalData(ParameterData qdykdata);
   //养卡渠道类型（饼图）
   public List<QdykData> getQDYKqdlxData(ParameterData qdykdata);
   //养卡渠道类型（堆积图）
   public List<QdykData> getQDYKqdlxDuiData(ParameterData qdykdata);
   //养卡整改问责统计（六个月达到整改标准次数排名）
   public List<QdykData> getQDYKZgwzTjSixMonth(ParameterData qdykdata);
   //养卡整改问责统计（累计达整改次数排名）
   public List<QdykData> getQDYKZgwzTj(ParameterData qdykdata);
   //养卡整改问责统计（达到整改标准的省公司数量趋势--折线图）
   public List<QdykData> getQDYKSjzgTjLine(ParameterData qdykdata);
   //养卡   审计报告 （文本）
   public List<Map<String,Object>> getQDYKReportText(ParameterData qdykdata);
   //养卡   审计报告 （表格）
   public List<Map<String,Object>> getQDYKReportTable(ParameterData qdykdata);
   //养卡 重点关注渠道
   public List<Map<String,Object>> getQDYKConcernChnl(ParameterData qdykdata);
   //养卡 重点关注营销案
   public List<Map<String,Object>> getQDYKConcernOffer(ParameterData qdykdata);
   //养卡 重点关注渠道 下钻趋势图
   public List<QdykData> getQdykFocusChnlTrend(ParameterData qdykdata);
   //重点关注营销案 下钻趋势图
   public List<QdykData> getQdykFocusOfferTrend(ParameterData qdykdata);
}
