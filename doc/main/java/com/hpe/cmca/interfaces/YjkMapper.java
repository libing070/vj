package com.hpe.cmca.interfaces;

import java.util.List;

import com.hpe.cmca.pojo.KhqfData;
import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.pojo.YjkData;
import com.hpe.cmca.pojo.ZGWZData;


public interface YjkMapper {

	//风险地图
	public List<YjkData> getMapData(ParameterData yjk);
	//地图下方卡片
	public List<YjkData> getMapBottomData(ParameterData yjk);
	//违规金额占比柱状图
	public List<YjkData> getColumnAmtData(ParameterData yjk);
	//违规数量占比柱状图
	public List<YjkData> getColumnNumData(ParameterData yjk);
	
	//违规金额占比折线图
	public List<YjkData> getLineAmtData(ParameterData yjk);
	//违规数量占比折线图
	public List<YjkData> getLineNumData(ParameterData yjk);
	
	//vc和crm间数据对比
	public List<YjkData> getCrmVsVcData(ParameterData yjk);
	
	 //获取有价卡增量分析-全国（增量分析图）
    public List<YjkData> getIncrementalData(ParameterData yjk);
    
	 //获取有价卡违规数量对比饼图
    public List<YjkData> getAmountPie(ParameterData yjk);
    
	 //获取有价卡占比趋势图
    public List<YjkData> getPerTrend(ParameterData yjk);
    
	 //获取有价卡整改问责要求
    public List<ZGWZData> getZGWZReq(ParameterData yjk);
    
  //获取有价卡整改问责6个月统计柱状图
    public List<ZGWZData> getZGWZColumn1(ParameterData yjk);
    
  //获取有价卡整改问责累计统计柱状图
    public List<ZGWZData> getZGWZColumn2(ParameterData yjk);
    
  //获取有价卡整改问责统计折线图
    public List<ZGWZData> getZGWZLine(ParameterData yjk);
    
    //获取有价卡审计报告
    public List<YjkData> getAuditReport(ParameterData yjk);
    
    //获取有价卡排名汇总数据
    public List<YjkData> getYjkPmhz(ParameterData yjk);
}
