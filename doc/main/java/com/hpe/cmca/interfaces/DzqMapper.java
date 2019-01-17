package com.hpe.cmca.interfaces;

import java.util.List;
import java.util.Map;

import com.hpe.cmca.pojo.DzqChartPojo;
import com.hpe.cmca.pojo.DzqMapCardPojo;
import com.hpe.cmca.pojo.DzqOfferTabPojo;
import com.hpe.cmca.pojo.DzqPiePojo;
import com.hpe.cmca.pojo.DzqReportInfoPojo;
import com.hpe.cmca.pojo.DzqSubsTabPojo;
import com.hpe.cmca.pojo.ParameterData;

public interface DzqMapper {
	
	//风险地图    金额 (电子券)
	List<DzqChartPojo> getMapAmtData(ParameterData paraData);
	
	//风险地图     金额 占比(电子券)
	List<DzqChartPojo> getMapAmtPerData(ParameterData paraData);
	
	//风险地图    数量 (电子券)
	List<DzqChartPojo> getMapNumData(ParameterData paraData);
	
	//风险地图    数量占比(电子券)
	List<DzqChartPojo> getMapNumPerData(ParameterData paraData);
	
	// 违规金额排名  Column (电子券)
	List<DzqChartPojo> getColumnAmtData(ParameterData paraData);
	
	// 违规金额排名  line  (电子券)
	List<DzqChartPojo> getLineAmtData(ParameterData paraData);
	
	// 违规金额占比排名  Column (电子券)
	List<DzqChartPojo> getColumnAmtPerData(ParameterData paraData);
	
	// 违规金额占比排名  line (电子券)
	List<DzqChartPojo> getLineAmtPerData(ParameterData paraData);
	
	// 违规数量排名  Column (电子券)
	List<DzqChartPojo> getColumnNumData(ParameterData paraData);
	
	// 违规数量排名  line  (电子券)
	List<DzqChartPojo> getLineNumData(ParameterData paraData);
	
	// 违规数量占比排名  Column (电子券)
	List<DzqChartPojo> getColumnNumPerData(ParameterData paraData);
	
	// 违规数量占比排名  line (电子券)
	List<DzqChartPojo> getLineNumPerData(ParameterData paraData);
/*=====================================================================================================*/
	//排名汇总
	List<Map<String, Object>> getRankTable(ParameterData parameterData);
	
	//统计分析  (电子券)
	List<DzqChartPojo> getIncrementalData(ParameterData paraData);
	
	//重点关注地市  电子券
	List<Map<String, Object>> getCityTable(ParameterData parameterData);
	
	//	重点关注营销案
	List<DzqOfferTabPojo> getOfferTable(ParameterData parameterData);

	//	重点关注用户
	List<DzqSubsTabPojo> getSubsTable(ParameterData parameterData);

	//重点关注渠道
	List<Map<String, Object>> getChannelTable(ParameterData parameterData);
	
	//重点关注类型分布(饼图)
	DzqPiePojo getTypeDistributionPie(ParameterData parameterData);
	
	//重点关注类型分布(line)
	List<Map<String, Object>> getTypeDistributionLine(ParameterData parameterData);

	//地图下方卡片
	List<DzqMapCardPojo> getCardData(ParameterData parameterData);
	
/*-----------------电子券，平台数据不一致---------------------------------------------------*/
	//风险地图数据   金额    省有基地无(电子券平台数据不一致)
	List<DzqChartPojo> getPlatPrvdMapAmtData(ParameterData paraData);
	
	//风险地图数据   金额    省有基地无(电子券平台数据不一致)
	List<DzqChartPojo> getPlatPrvdMapAmtPerData(ParameterData paraData);
	
	//风险地图数据   金额    省无基地有(电子券平台数据不一致)
	List<DzqChartPojo> getPlatBasePrvdMapAmtData(ParameterData paraData);
	
	//风险分析   省无基地有 金额占比
	List<DzqChartPojo> getPlatBasePrvdMapAmtPerData(ParameterData paraData);
	
	//风险地图数据   金额    省 pie(电子券平台数据不一致)
	DzqPiePojo getPlatCityPieAmtData(ParameterData parameterData);
	
	//省无基地有     金额排名 Column(电子券平台数据不一致)
	List<DzqChartPojo> getPlatBaseColumnAmtData(ParameterData paraData);

	//省无基地有     金额排名  line(电子券平台数据不一致)
	List<DzqChartPojo> getPlatBaseLineAmtData(ParameterData paraData);

	//省无基地有     金额占比排名 Column(电子券平台数据不一致)
	List<DzqChartPojo> getPlatBaseColumnAmtPerData(ParameterData paraData);

	//省无基地有     金额占比排名  line(电子券平台数据不一致)
	List<DzqChartPojo> getPlatBaseLineAmtPerData(ParameterData paraData);

	//省有基地无     金额排名 Column(电子券平台数据不一致)
	List<DzqChartPojo> getPlatPrvdColumnAmtData(ParameterData paraData);

	//省有基地无     金额排名 Line(电子券平台数据不一致)
	List<DzqChartPojo> getPlatPrvdLineAmtData(ParameterData paraData);

	//省有基地无     金额占比排名 Column (电子券平台数据不一致)
	List<DzqChartPojo> getPlatPrvdColumnAmtPerData(ParameterData paraData);

	//省有基地无    金额占比排名  line  (电子券平台数据不一致)
	List<DzqChartPojo> getPlatPrvdLineAmtPerData(ParameterData paraData);
	
	//排名汇总  （平台不一致）table
	List<Map<String, Object>> getPlatRankTable(ParameterData parameterData);

	//(电子券平台数据不一致)  增量分析
	List<DzqChartPojo> getPlatIncrementalData(ParameterData paraData);

	//审计报告
	List<DzqReportInfoPojo> getReportInfoData(ParameterData parameterData);

	


	

	

	



	


}
