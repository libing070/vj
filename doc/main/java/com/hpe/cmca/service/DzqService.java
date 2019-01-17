package com.hpe.cmca.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.DzqMapper;
import com.hpe.cmca.pojo.DzqChartPojo;
import com.hpe.cmca.pojo.DzqMapCardPojo;
import com.hpe.cmca.pojo.DzqOfferTabPojo;
import com.hpe.cmca.pojo.DzqPiePojo;
import com.hpe.cmca.pojo.DzqReportInfoPojo;
import com.hpe.cmca.pojo.DzqSubsTabPojo;
import com.hpe.cmca.pojo.ParameterData;

@Service("dzqService")
public class DzqService extends BaseObject {

	@Autowired
	protected MybatisDao mybatisDao;

	/**
	 * 风险地图总方法
	 * 
	 * @param paraData
	 */
	public Object getMapData(ParameterData paraData) {

		// * 电子券 违规金额 map(全国，地市)
		if ("6002".equals(paraData.getConcern()) && "errAmount".equals(paraData.getParameterType())) {
			return getMapAmtData(paraData);
		}
		// * 电子券 违规金额 map(全国，地市)
		if ("6002".equals(paraData.getConcern()) && "errAmountPercent".equals(paraData.getParameterType())) {
			return getMapAmtPerData(paraData);
		}
		// * 电子券 （数量）(全国，地市)
		if ("6002".equals(paraData.getConcern()) && "errIssueNum".equals(paraData.getParameterType())) {
			return getMapNumData(paraData);
		}

		// * 电子券 数量占比 map(全国，地市)
		if ("6002".equals(paraData.getConcern()) && "errIssueNumPercent".equals(paraData.getParameterType())) {
			return getMapNumPerData(paraData);
		}
		// 省无基地有 金额
		if ("6001".equals(paraData.getConcern()) && "prvdNoAmount".equals(paraData.getParameterType())) {
			return getPlatBasePrvdMapAmtData(paraData);
		}

		// 省无基地有 金额占比
		if ("6001".equals(paraData.getConcern()) && "prvdNoAmountPercent".equals(paraData.getParameterType())) {
			return getPlatBasePrvdMapAmtPerData(paraData);
		}

		// 省有基地无 金额
		if ("6001".equals(paraData.getConcern()) && "prvdHaveAmount".equals(paraData.getParameterType())) {
			return getPlatPrvdMapAmtData(paraData);
		}

		// 省有基地无 金额占比
		if ("6001".equals(paraData.getConcern()) && "prvdHaveAmountPercent".equals(paraData.getParameterType())) {
			return getPlatPrvdMapAmtPerData(paraData);
		}
		return null;
	}

	public Object getChartData(ParameterData paraData) {
		// * 电子券 违规金额 Column(全国，地市)
		if ("6002".equals(paraData.getConcern()) && "errAmount".equals(paraData.getParameterType())) {
			return getColumnAmtData(paraData);
		}
		// * 电子券 违规金额占比 Column(全国，地市)
		if ("6002".equals(paraData.getConcern()) && "errAmountPercent".equals(paraData.getParameterType())) {
			return getColumnAmtPerData(paraData);
		}
		// * 电子券 （数量）(全国，地市)
		if ("6002".equals(paraData.getConcern()) && "errIssueNum".equals(paraData.getParameterType())) {
			return getColumnNumData(paraData);
		}
		// * 电子券 数量占比 map(全国，地市)
		if ("6002".equals(paraData.getConcern()) && "errIssueNumPercent".equals(paraData.getParameterType())) {
			return getColumnNumPerData(paraData);
		}
		// 省无基地有 金额
		if ("6001".equals(paraData.getConcern()) && "prvdNoAmount".equals(paraData.getParameterType())) {
			return getPlatBaseColumnAmtData(paraData);
		}
		// 省无基地有 金额占比
		if ("6001".equals(paraData.getConcern()) && "prvdNoAmountPercent".equals(paraData.getParameterType())) {
			return getPlatBaseColumnAmtPerData(paraData);
		}
		// 省有基地无 金额
		if ("6001".equals(paraData.getConcern()) && "prvdHaveAmount".equals(paraData.getParameterType())) {
			return getPlatPrvdColumnAmtData(paraData);
		}
		// 省有基地无 金额占比
		if ("6001".equals(paraData.getConcern()) && "prvdHaveAmountPercent".equals(paraData.getParameterType())) {
			return getPlatPrvdColumnAmtPerData(paraData);
		}
		return null;
	}

	public Object getChartLineData(ParameterData paraData) {
		// * 电子券 违规金额 (全国，地市)
		if ("6002".equals(paraData.getConcern()) && "errAmountTrend".equals(paraData.getParameterType())) {
			return getLineAmtData(paraData);
		}
		// * 电子券 违规金额占比 (全国，地市)
		if ("6002".equals(paraData.getConcern()) && "errAmountPercentTrend".equals(paraData.getParameterType())) {
			return getLineAmtPerData(paraData);
		}
		// * 电子券 （数量）(全国，地市)
		if ("6002".equals(paraData.getConcern()) && "errIssueNumTrend".equals(paraData.getParameterType())) {
			return getLineNumData(paraData);
		}
		// * 电子券 数量占比 (全国，地市)
		if ("6002".equals(paraData.getConcern()) && "errIssueNumPercentTrend".equals(paraData.getParameterType())) {
			return getLineNumData(paraData);
		}
		// 省无基地有 金额
		if ("6001".equals(paraData.getConcern()) && "prvdNoAmountTrend".equals(paraData.getParameterType())) {
			return getPlatBaseLineAmtData(paraData);
		}
		// 省无基地有 金额占比
		if ("6001".equals(paraData.getConcern()) && "prvdNoAmountPercentTrend".equals(paraData.getParameterType())) {
			return getPlatBaseLineAmtPerData(paraData);
		}
		// 省有基地无 金额
		if ("6001".equals(paraData.getConcern()) && "prvdHaveAmountTrend".equals(paraData.getParameterType())) {
			return getPlatPrvdLineAmtData(paraData);
		}
		// 省有基地无 金额占比
		if ("6001".equals(paraData.getConcern()) && "prvdHaveAmountPercentTrend".equals(paraData.getParameterType())) {
			return getPlatPrvdLineAmtPerData(paraData);
		}
		return null;
	}

	/**
	 * 风险地图（金额）map
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getMapAmtData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		Map<Integer, DzqChartPojo> dzMap = new HashMap<Integer, DzqChartPojo>();
		List<DzqChartPojo> dzList = new ArrayList<DzqChartPojo>();
		// 电子券 违规金额 map(全国，地市)
		dzList = dzqglwgMapper.getMapAmtData(paraData);
		for (DzqChartPojo dz : dzList) {
			if (dz.getPrvdId() == 10000) {
				dzMap.put(dz.getPrvdId(), dz);
			}
			if (dz.getPrvdId() != 10000) {
				dzMap.put(dz.getPrvdId(), dz);
			}
		}
		return dzMap;
	}

	/**
	 * 风险地图（金额占比）map 电子券 违规金额占比 map(全国，地市)
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getMapAmtPerData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		Map<Integer, DzqChartPojo> dzMap = new HashMap<Integer, DzqChartPojo>();
		List<DzqChartPojo> dzList = new ArrayList<DzqChartPojo>();
		dzList = dzqglwgMapper.getMapAmtPerData(paraData);
		for (DzqChartPojo dz : dzList) {
			if (dz.getPrvdId() == 10000) {
				dzMap.put(dz.getPrvdId(), dz);
			}
			if (dz.getPrvdId() != 10000) {
				dzMap.put(dz.getPrvdId(), dz);
			}
		}

		return dzMap;
	}

	/**
	 * 风险地图（数量）map 电子券 违规数量 map(全国，地市)
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getMapNumData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = new ArrayList<DzqChartPojo>();
		Map<Integer, DzqChartPojo> dzMap = new HashMap<Integer, DzqChartPojo>();
		dzList = dzqglwgMapper.getMapNumData(paraData);
		for (DzqChartPojo dz : dzList) {
			if (dz.getPrvdId() == 10000) {
				dzMap.put(dz.getPrvdId(), dz);
			}
			if (dz.getPrvdId() != 10000) {
				dzMap.put(dz.getPrvdId(), dz);
			}
		}

		return dzMap;
	}

	/**
	 * 风险地图（数量占比）map 平台数据不一致（ 金额占比 省有基地无，省饼图（金额）） 电子券 违规数量占比 map(全国，地市)
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getMapNumPerData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = new ArrayList<DzqChartPojo>();
		Map<Integer, DzqChartPojo> dzMap = new HashMap<Integer, DzqChartPojo>();
		dzList = dzqglwgMapper.getMapNumPerData(paraData);
		for (DzqChartPojo dz : dzList) {
			if (dz.getPrvdId() == 10000) {
				dzMap.put(dz.getPrvdId(), dz);
			}
			if (dz.getPrvdId() != 10000) {
				dzMap.put(dz.getPrvdId(), dz);
			}
		}

		return dzMap;
	}

	/**
	 * 地图下方卡片 电子券 数据平台不一致
	 * 
	 * @param parameterData
	 * @return
	 */
	public Object getCardData(ParameterData parameterData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqMapCardPojo> cartList = dzqglwgMapper.getCardData(parameterData);
		Map<String, Object> resultmap = new HashMap<String, Object>();
		for (DzqMapCardPojo dz : cartList) {
			if (dz.getPrvdId() == 10000) {
				resultmap.put(dz.getPrvdId().toString(), dz);
			}
			if (dz.getPrvdId() != 10000) {
				resultmap.put(dz.getPrvdId().toString(), dz);
			}
		}
		return resultmap;
	}

	/**
	 * 违规金额排名 柱形图 (电子券)
	 * 
	 * @param paraData
	 * @return
	 */
	public Map<String, Object> getColumnAmtData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getColumnAmtData(paraData);
		List<String> nameList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		for (DzqChartPojo dz : dzList) {
			nameList.add(dz.getName());
			dataList.add(dz.getAmts());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("nameList", nameList);
		result.put("dataList", dataList);
		return result;
	}

	/**
	 * 违规金额排名 折线 省无基地有 金额排名
	 * 
	 * @param paraData
	 * @return
	 */
	public Map<String, Object> getLineAmtData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = new ArrayList<DzqChartPojo>();
		List<String> audTrmList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		// 违规金额趋势 (全国,地市)
		dzList = dzqglwgMapper.getLineAmtData(paraData);
		for (DzqChartPojo dz : dzList) {
			audTrmList.add(dz.getAudTrm());

			dataList.add(dz.getAmts());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("audTrmList", audTrmList);
		result.put("dataList", dataList);
		return result;
	}

	/**
	 * 违规金额占比排名 柱形图 (电子券)
	 * 
	 * @param paraData
	 * @return
	 */
	public Map<String, Object> getColumnAmtPerData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getColumnAmtPerData(paraData);
		List<String> nameList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		for (DzqChartPojo dz : dzList) {
			nameList.add(dz.getName());
			dataList.add(dz.getPers());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("nameList", nameList);
		result.put("dataList", dataList);
		return result;
	}

	/**
	 * 违规金额占比排名 折线 (电子券)
	 * 
	 * @param paraData
	 * @return
	 */
	public Map<String, Object> getLineAmtPerData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = new ArrayList<DzqChartPojo>();
		List<String> audTrmList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		// 违规金额占比趋势 (全国,地市)
		dzList = dzqglwgMapper.getLineAmtPerData(paraData);
		for (DzqChartPojo dz : dzList) {
			audTrmList.add(dz.getAudTrm());
			dataList.add(dz.getPers());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("audTrmList", audTrmList);
		result.put("dataList", dataList);
		return result;
	}

	/**
	 * 违规数量排名 柱形图 (电子券)
	 * 
	 * @param paraData
	 * @return
	 */
	public Map<String, Object> getColumnNumData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getColumnNumData(paraData);
		List<String> nameList = new ArrayList<String>();
		List<Integer> dataList = new ArrayList<Integer>();
		for (DzqChartPojo dz : dzList) {
			nameList.add(dz.getName());
			dataList.add(dz.getNums());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("nameList", nameList);
		result.put("dataList", dataList);
		return result;
	}

	/**
	 * 违规数量排名 折线 (电子券)
	 * 
	 * @param paraData
	 * @return
	 */
	public Map<String, Object> getLineNumData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = new ArrayList<DzqChartPojo>();
		List<String> audTrmList = new ArrayList<String>();
		List<Integer> dataList = new ArrayList<Integer>();
		// 违规数量趋势 (全国,地市)
		dzList = dzqglwgMapper.getLineNumData(paraData);
		for (DzqChartPojo dz : dzList) {
			audTrmList.add(dz.getAudTrm());
			dataList.add(dz.getNums());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("audTrmList", audTrmList);
		result.put("dataList", dataList);
		return result;
	}

	/**
	 * 违规数量占比排名 柱形图 (电子券)
	 * 
	 * @param paraData
	 * @return
	 */
	public Map<String, Object> getColumnNumPerData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getColumnNumPerData(paraData);
		List<String> nameList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		for (DzqChartPojo dz : dzList) {
			nameList.add(dz.getName());
			dataList.add(dz.getPers());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("nameList", nameList);
		result.put("dataList", dataList);
		return result;
	}

	/**
	 * 违规数量占比排名 折线 (电子券)
	 * 
	 * @param paraData
	 * @return
	 */
	public Map<String, Object> getLineNumPerData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = new ArrayList<DzqChartPojo>();
		List<String> audTrmList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		// 违规数量趋势 (全国,地市)
		dzList = dzqglwgMapper.getLineNumPerData(paraData);
		for (DzqChartPojo dz : dzList) {
			audTrmList.add(dz.getAudTrm());
			dataList.add(dz.getPers());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("audTrmList", audTrmList);
		result.put("dataList", dataList);
		return result;
	}

	/*
	 * ==========================================统计分析
	 * （电子券）================================================
	 */
	/**
	 * 排名汇总 （电子券）table
	 * 
	 * @param parameterData
	 * @return
	 */
	public Map<String, Object> getRankTable(ParameterData parameterData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<Map<String, Object>> dzqRankDataList = dzqglwgMapper.getRankTable(parameterData);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("dzqRankDataList", dzqRankDataList);
		return result;
	}

	/**
	 * 统计分析 瀑布图
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getIncrementalData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = new ArrayList<DzqChartPojo>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		dzList = dzqglwgMapper.getIncrementalData(paraData);

		Map<String, Object> singResult = null;
		DzqChartPojo dzqChart = new DzqChartPojo();
		List<DzqChartPojo> lessZeroList = new ArrayList<DzqChartPojo>();
		List<DzqChartPojo> nullList = new ArrayList<DzqChartPojo>();
		// 第二步：遍历数据，将全国、增量<0、增量为空分别插入不同的list；将增量>0的直接加入结果表中
		if (dzList.size() > 0) {
			for (DzqChartPojo dzqChartPojo : dzList) {
				if (dzqChartPojo.getPrvdId() == 10000) {
					// 全国数据
					dzqChart = dzqChartPojo;
				} else if (dzqChartPojo.getAmts() == null) {
					// null数据集
					nullList.add(dzqChartPojo);
				} else if (dzqChartPojo.getAmts().compareTo(BigDecimal.ZERO) == -1) {
					// 小于0的数据集
					lessZeroList.add(dzqChartPojo);
				} else {
					singResult = new HashMap<String, Object>();
					singResult.put("name", dzqChartPojo.getName());
					singResult.put("y", dzqChartPojo.getAmts());
					result.add(singResult);
				}
			}
		}
		// 第三步：将增量<0的，逆序加入结果表中
		if (lessZeroList.size() > 0) {
			for (int i = lessZeroList.size() - 1; i >= 0; i--) {
				DzqChartPojo dzqChartPojo = lessZeroList.get(i);
				singResult = new HashMap<String, Object>();
				singResult.put("name", dzqChartPojo.getName());
				singResult.put("y", dzqChartPojo.getAmts());
				result.add(singResult);
			}
		}

		// 第四步：将增量为空的，加入结果表中
		if (nullList.size() > 0) {
			for (DzqChartPojo dzqChartPojo : nullList) {
				singResult = new HashMap<String, Object>();
				singResult.put("name", dzqChartPojo.getName());
				singResult.put("y", dzqChartPojo.getAmts());
				result.add(singResult);
			}
		}
		// 第五步：将全国数据，加入结果表中
		singResult = new HashMap<String, Object>();
		singResult.put("name", "全网环比增量");
		singResult.put("y", dzqChart.getAmts());
		result.add(singResult);

		return result;
	}

	/**
	 * 重点关注地市
	 * 
	 * @param parameterData
	 * @return
	 */
	public Object getCityTable(ParameterData parameterData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<Map<String, Object>> dzqCityDataList = dzqglwgMapper.getCityTable(parameterData);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("dzqCityDataList", dzqCityDataList);
		return result;
	}

	/**
	 * 重点关注营销案
	 * 
	 * @param parameterData
	 * @return
	 */
	public Object getOfferTable(ParameterData parameterData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqOfferTabPojo> dzqOfferDataList = dzqglwgMapper.getOfferTable(parameterData);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("dzqOfferDataList", dzqOfferDataList);
		return result;
	}

	/**
	 * 重点关注渠道
	 * 
	 * @param parameterData
	 * @return
	 */
	public Object getChannelTable(ParameterData parameterData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<Map<String, Object>> dzqChannelDataList = dzqglwgMapper.getChannelTable(parameterData);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("dzqChannelDataList", dzqChannelDataList);
		return result;
	}

	/**
	 * 重点关注用户
	 * 
	 * @param parameterData
	 * @return
	 */
	public Object getSubsTable(ParameterData parameterData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqSubsTabPojo> dzqSubsDataList = dzqglwgMapper.getSubsTable(parameterData);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("dzqSubsDataList", dzqSubsDataList);
		return result;
	}

	/**
	 * 重点类型分布 pie
	 * 
	 * @param parameterData
	 * @return
	 */
	public Object getTypeDistributionPie(ParameterData parameterData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		DzqPiePojo dzqPieData = dzqglwgMapper.getTypeDistributionPie(parameterData);
		Map<String, Object> resultmap = new HashMap<String, Object>();
		if (dzqPieData == null) {
			return resultmap;
		} else {
			resultmap.put("bigAmts", dzqPieData.getBigAmts());// 大额
			resultmap.put("highAmts", dzqPieData.getHighAmts());// 高频
			resultmap.put("exAmts", dzqPieData.getExAmts());// 向异常用户
			return resultmap;
		}

	}

	/**
	 * 重点类型分布 line
	 * 
	 * @param parameterData
	 * @return
	 */
	public Object getTypeDistributionLine(ParameterData parameterData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		if (parameterData.getAudTrm() == null || "".equals(parameterData.getAudTrm())) {
			List<String> audTrmList = new ArrayList<String>();
			List<Double> aAmtList = new ArrayList<Double>();
			List<Double> bAmtList = new ArrayList<Double>();
			List<Double> cAmtList = new ArrayList<Double>();
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("audTrmList", audTrmList);
			result.put("bigAmtsList", aAmtList);
			result.put("highAmtsList", bAmtList);
			result.put("exAmtsList", cAmtList);
			return result;
		}
		List<Map<String, Object>> dzList = dzqglwgMapper.getTypeDistributionLine(parameterData);
		List<String> audTrmList = new ArrayList<String>();
		List<Double> aAmtList = new ArrayList<Double>();
		List<Double> bAmtList = new ArrayList<Double>();
		List<Double> cAmtList = new ArrayList<Double>();
		for (int i = 0; i < dzList.size(); i++) {
			audTrmList.add(dzList.get(i).get("audTrm").toString());
			aAmtList.add(Double.parseDouble(dzList.get(i).get("bigAmts").toString()));// 大额
			bAmtList.add(Double.parseDouble(dzList.get(i).get("highAmts").toString()));// 高频
			cAmtList.add(Double.parseDouble(dzList.get(i).get("exAmts").toString()));// 向异常用户
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("audTrmList", audTrmList);
		result.put("bigAmtsList", aAmtList);
		result.put("highAmtsList", bAmtList);
		result.put("exAmtsList", cAmtList);
		return result;
	}

	/*
	 * =============================电子券 平台数据不一致================================
	 */
	/**
	 * 风险地图数据 金额 省有基地无 (电子券 平台数据不一致)
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getPlatPrvdMapAmtData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getPlatPrvdMapAmtData(paraData);
		Map<Integer, DzqChartPojo> dzMap = new HashMap<Integer, DzqChartPojo>();
		for (DzqChartPojo dz : dzList) {
			dzMap.put(dz.getPrvdId(), dz);
		}
		return dzMap;
	}

	/**
	 * 风险地图数据 金额 省有基地无 (电子券 平台数据不一致)
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getPlatPrvdMapAmtPerData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getPlatPrvdMapAmtPerData(paraData);
		Map<Integer, DzqChartPojo> dzMap = new HashMap<Integer, DzqChartPojo>();
		for (DzqChartPojo dz : dzList) {
			dzMap.put(dz.getPrvdId(), dz);
		}
		return dzMap;
	}

	/**
	 * 风险地图数据 金额 省无基地有(电子券 平台数据不一致)
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getPlatBasePrvdMapAmtData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getPlatBasePrvdMapAmtData(paraData);
		Map<Integer, DzqChartPojo> dzMap = new HashMap<Integer, DzqChartPojo>();
		for (DzqChartPojo dz : dzList) {
			dzMap.put(dz.getPrvdId(), dz);
		}
		return dzMap;
	}

	/**
	 * 风险地图数据 金额占比 省无基地有(电子券 平台数据不一致)
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getPlatBasePrvdMapAmtPerData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getPlatBasePrvdMapAmtPerData(paraData);
		Map<Integer, DzqChartPojo> dzMap = new HashMap<Integer, DzqChartPojo>();
		for (DzqChartPojo dz : dzList) {
			dzMap.put(dz.getPrvdId(), dz);
		}
		return dzMap;
	}

	/**
	 * 风险地图数据 金额 省（饼图 电子券 平台数据不一致）
	 * 
	 * @param parameterData
	 * @return
	 */
	public Object getPlatCityPieAmtData(ParameterData parameterData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		DzqPiePojo pieList = dzqglwgMapper.getPlatCityPieAmtData(parameterData);
		Map<String, Object> resultmap = new HashMap<String, Object>();
		if (pieList == null) {
			return resultmap;
		} else {
			resultmap.put("baseErrAmts", pieList.getBaseErrAmts());// 省有基地无电子券金额
			resultmap.put("cmccErrAmts", pieList.getCmccErrAmts());// 基地有省无电子券金额
			resultmap.put("cmccbaseSumAmt", pieList.getCmccbaseSumAmt());// 省有基地有电子券发放总金额
		}

		return resultmap;
	}

	/*
	 * =================================平台一致性风险地图end============================
	 * ===========================================
	 */
	/**
	 * 省无基地有 金额排名 Column (电子券平台数据不一致)
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getPlatBaseColumnAmtData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getPlatBaseColumnAmtData(paraData);
		List<String> nameList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		for (DzqChartPojo dz : dzList) {
			nameList.add(dz.getName());
			dataList.add(dz.getAmts());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("nameList", nameList);
		result.put("dataList", dataList);
		return result;
	}

	/**
	 * 省无基地有 金额排名 line (电子券平台数据不一致)
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getPlatBaseLineAmtData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getPlatBaseLineAmtData(paraData);
		List<String> audTrmList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		for (DzqChartPojo dz : dzList) {
			audTrmList.add(dz.getAudTrm());
			dataList.add(dz.getAmts());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("audTrmList", audTrmList);
		result.put("dataList", dataList);
		return result;
	}

	/**
	 * 省无基地有 金额占比排名 Column (电子券平台数据不一致)
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getPlatBaseColumnAmtPerData(ParameterData paraData) {

		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getPlatBaseColumnAmtPerData(paraData);
		List<String> nameList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		for (DzqChartPojo dz : dzList) {
			nameList.add(dz.getName());
			dataList.add(dz.getPers());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("nameList", nameList);
		result.put("dataList", dataList);
		return result;

	}

	/**
	 * 省无基地有 金额占比排名 line (电子券平台数据不一致)
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getPlatBaseLineAmtPerData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getPlatBaseLineAmtPerData(paraData);
		List<String> audTrmList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		for (DzqChartPojo dz : dzList) {
			audTrmList.add(dz.getAudTrm());
			dataList.add(dz.getPers());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("audTrmList", audTrmList);
		result.put("dataList", dataList);
		return result;
	}

	/**
	 * 省有基地无 金额排名 Column (电子券平台数据不一致)
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getPlatPrvdColumnAmtData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getPlatPrvdColumnAmtData(paraData);
		List<String> nameList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		for (DzqChartPojo dz : dzList) {
			nameList.add(dz.getName());
			dataList.add(dz.getAmts());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("nameList", nameList);
		result.put("dataList", dataList);
		return result;
	}

	/**
	 * 省有基地无 金额排名 line (电子券平台数据不一致)
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getPlatPrvdLineAmtData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getPlatPrvdLineAmtData(paraData);
		List<String> audTrmList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		for (DzqChartPojo dz : dzList) {
			audTrmList.add(dz.getAudTrm());
			dataList.add(dz.getAmts());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("audTrmList", audTrmList);
		result.put("dataList", dataList);
		return result;
	}

	/**
	 * 省有基地无 金额占比排名 Column (电子券平台数据不一致)
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getPlatPrvdColumnAmtPerData(ParameterData paraData) {

		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getPlatPrvdColumnAmtPerData(paraData);
		List<String> nameList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		for (DzqChartPojo dz : dzList) {
			nameList.add(dz.getName());
			dataList.add(dz.getPers());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("nameList", nameList);
		result.put("dataList", dataList);
		return result;
	}

	/**
	 * 省有基地无 金额占比排名 line (电子券平台数据不一致)
	 * 
	 * @param paraData
	 * @return
	 */
	public Object getPlatPrvdLineAmtPerData(ParameterData paraData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqChartPojo> dzList = dzqglwgMapper.getPlatPrvdLineAmtPerData(paraData);
		List<String> audTrmList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		for (DzqChartPojo dz : dzList) {
			audTrmList.add(dz.getAudTrm());
			dataList.add(dz.getPers());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("audTrmList", audTrmList);
		result.put("dataList", dataList);
		return result;
	}

	/**
	 * 排名汇总 （平台不一致）table
	 * 
	 * @param parameterData
	 * @return
	 */
	public Object getPlatRankTable(ParameterData parameterData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<Map<String, Object>> dzqRankPlatDataList = dzqglwgMapper.getPlatRankTable(parameterData);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("dzqRankDataList", dzqRankPlatDataList);
		return result;
	}

	/**
	 * 审计报告
	 * 
	 * @param parameterData
	 * @return
	 */
	public Object getReportInfoData(ParameterData parameterData) {
		DzqMapper dzqglwgMapper = mybatisDao.getSqlSession().getMapper(DzqMapper.class);
		List<DzqReportInfoPojo> dzList = dzqglwgMapper.getReportInfoData(parameterData);
		Map<String, DzqReportInfoPojo> dzMap = new HashMap<String, DzqReportInfoPojo>();
		for (DzqReportInfoPojo dz : dzList) {
			dzMap.put("reportData", dz);
		}
		return dzMap;
	}

}
