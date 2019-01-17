/**
 * com.hpe.cmca.service.ZdtlService.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.ZdtlMapper;
import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.pojo.ZdtlData;
import com.hpe.cmca.util.Json;

/**
 * <pre>
 * Desc： 终端套利Service类
 * &#64;author   hufei
 * &#64;refactor hufei
 * &#64;date     2017-7-12 下午5:10:03
 * &#64;version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-7-12 	   hufei 	         1. Created this class.
 * </pre>
 */
@Service("ZdtlService")
public class ZdtlService extends BaseObject {

	@Autowired
	protected MybatisDao mybatisDao;

	/**
	 * 
	 * <pre>
	 * Desc获取终端套利地图数据  
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-7-25 下午5:40:01
	 * </pre>
	 */
	public Map<Integer, Object> getMapData(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		List<ZdtlData> zdtlDataList = zdtlMapper.getMapData(parameterData);
		Map<Integer, Object> result = new HashMap<Integer, Object>();
		if (parameterData.getPrvdId() == 10000) {
			for (ZdtlData zdtl : zdtlDataList) {
				result.put(zdtl.getPrvdId(), zdtl);
			}
		} else {
			for (ZdtlData zdtl : zdtlDataList) {
				result.put(zdtl.getCtyId(), zdtl);
			}
		}
		return result;
	}

	/**
	 * 
	 * <pre>
	 * Desc获取地图下方数据 
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-7-27 上午11:09:03
	 * </pre>
	 */
	public Map<Integer, Object> getMapBottomData(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		List<ZdtlData> zdtlDataList = zdtlMapper.getMapBottomData(parameterData);

		Map<Integer, Object> result = new HashMap<Integer, Object>();

		if (zdtlDataList.size() > 0) {
			result.put(parameterData.getPrvdId(), zdtlDataList.get(0));
		}
		return result;
	}

	/**
	 * 
	 * <pre>
	 * Desc  获取终端套利-风险地图-下钻-地市渠道级别信息-top50
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-7-27 下午3:31:12
	 * </pre>
	 */
	public Map<String, Object> getChnlTable(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		List<ZdtlData> zdtlDataList = zdtlMapper.getChnlTable(parameterData);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("zdtlChnl", zdtlDataList);
		return result;
	}

	/**
	 * <pre>
	 * Desc  根据渠道名称查询渠道信息
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-8-7 下午3:36:13
	 * </pre>
	 */
	public Map<String, Object> getChnlByChnlName(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		List<ZdtlData> zdtlDataList = zdtlMapper.getChnlByChnlName(parameterData);
		Map<String, Object> result = new HashMap<String, Object>();
		if (zdtlDataList.size() > 0) {
			result.put("zdtlChnl", zdtlDataList);
		}

		return result;
	}

	/**
	 * <pre>
	 * Desc  终端套利-审计结果-套利金额排名（柱状图）
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-7-20 下午5:47:50
	 * </pre>
	 */
	public Map<String, Object> getAmountColumnData(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> zdtlDataList = zdtlMapper.getAmountColumnData(parameterData);
		List<BigDecimal> infractionAmtCJ = new ArrayList<BigDecimal>();
		List<String> prvdName = new ArrayList<String>();
		if (zdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : zdtlDataList) {
				prvdName.add(zdtlData.getPrvdName());
				infractionAmtCJ.add(zdtlData.getInfractionAmtCJ());
			}
		}
		result.put("prvdName", prvdName);
		result.put("infractionAmtCJ", infractionAmtCJ);
		return result;
	}

	/**
	 * <pre>
	 * Desc  终端套利-审计结果-套利金额占比排名（柱状图）
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-7-27 下午4:37:50
	 * </pre>
	 */
	public Map<String, Object> getAmountPercentColumnData(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> ZdtlDataList = zdtlMapper.getAmountPercentColumnData(parameterData);
		List<BigDecimal> amtPercentCJ = new ArrayList<BigDecimal>();
		List<String> prvdName = new ArrayList<String>();
		if (ZdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : ZdtlDataList) {
				prvdName.add(zdtlData.getPrvdName());
				amtPercentCJ.add(zdtlData.getAmtPercentCJ());
			}
		}
		result.put("prvdName", prvdName);
		result.put("amtPercentCJ", amtPercentCJ);
		return result;
	}

	/**
	 * <pre>
	 * Desc  终端套利-审计结果-异常销售数量排名（柱状图）
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-7-27 下午4:43:37
	 * </pre>
	 */
	public Map<String, Object> getNumberColumnData(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> ZdtlDataList = zdtlMapper.getNumberColumnData(parameterData);
		List<BigDecimal> infractionNum = new ArrayList<BigDecimal>();
		List<String> prvdName = new ArrayList<String>();
		if (ZdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : ZdtlDataList) {
				prvdName.add(zdtlData.getPrvdName());
				infractionNum.add(zdtlData.getInfractionNum());
			}
		}
		result.put("prvdName", prvdName);
		result.put("infractionNum", infractionNum);
		return result;
	}

	/**
	 * <pre>
	 * Desc  终端套利-审计结果-异常销售占比排名（柱状图）
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-7-20 下午5:47:57
	 * </pre>
	 */
	public Map<String, Object> getPercentColumnData(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> ZdtlDataList = zdtlMapper.getPercentColumnData(parameterData);
		List<BigDecimal> qtyPercent = new ArrayList<BigDecimal>();
		List<String> prvdName = new ArrayList<String>();
		if (ZdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : ZdtlDataList) {
				prvdName.add(zdtlData.getPrvdName());
				qtyPercent.add(zdtlData.getQtyPercent());
			}
		}
		result.put("prvdName", prvdName);
		result.put("qtyPercent", qtyPercent);
		return result;
	}

	/**
	 * <pre>
	 * Desc  终端套利-审计结果-套利金额排名（折线图）
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-7-20 下午5:56:36
	 * </pre>
	 */
	public Map<String, Object> getAmountLineData(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> ZdtlDataList = zdtlMapper.getAmountLineData(parameterData);
		List<BigDecimal> infractionAmtCJ = new ArrayList<BigDecimal>();
		List<String> audTrm = new ArrayList<String>();
		if (ZdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : ZdtlDataList) {
				audTrm.add(zdtlData.getAudTrm());
				infractionAmtCJ.add(zdtlData.getInfractionAmtCJ());
			}
		}
		result.put("audTrm", audTrm);
		result.put("infractionAmtCJ", infractionAmtCJ);
		return result;
	}

	/**
	 * <pre>
	 * Desc  终端套利-审计结果-套利占比排名（折线图）
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-7-20 下午5:56:36
	 * </pre>
	 */
	public Map<String, Object> getPercentLineData(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> ZdtlDataList = zdtlMapper.getPercentLineData(parameterData);
		List<BigDecimal> qtyPercent = new ArrayList<BigDecimal>();
		List<String> audTrm = new ArrayList<String>();
		List<BigDecimal> infractionNum = new ArrayList<BigDecimal>();
		if (ZdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : ZdtlDataList) {
				audTrm.add(zdtlData.getAudTrm());
				qtyPercent.add(zdtlData.getQtyPercent());
				infractionNum.add(zdtlData.getInfractionNum());
			}
		}
		result.put("audTrm", audTrm);
		result.put("qtyPercent", qtyPercent);
		result.put("infractionNum", infractionNum);
		return result;
	}

	/**
	 * 
	 * <pre>
	 * Desc 获取异常销售渠道占比趋势-折线图
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-7-31 下午3:20:33
	 * </pre>
	 */
	public Map<String, Object> getPercentChnlLineData(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> ZdtlDataList = zdtlMapper.getPercentChnlLineData(parameterData);
		List<BigDecimal> infractionChnlPercent = new ArrayList<BigDecimal>();
		List<BigDecimal> infractionChnlNum = new ArrayList<BigDecimal>();
		List<String> audTrm = new ArrayList<String>();
		if (ZdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : ZdtlDataList) {
				audTrm.add(zdtlData.getAudTrm());
				infractionChnlPercent.add(zdtlData.getInfractionChnlPercent());
				infractionChnlNum.add(zdtlData.getInfractionChnlNum());
			}
		}
		result.put("audTrm", audTrm);
		result.put("infractionChnlPercent", infractionChnlPercent);
		result.put("infractionChnlNum", infractionChnlNum);
		return result;
	}

	/**
	 * 
	 * <pre>
	 * Desc 获取异常销售渠道占比-柱状图 
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-7-31 下午4:04:23
	 * </pre>
	 */
	public Map<String, Object> getPercentChnlColumnData(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> ZdtlDataList = zdtlMapper.getPercentChnlColumnData(parameterData);
		List<BigDecimal> infractionChnlPercent = new ArrayList<BigDecimal>();
		List<String> prvdName = new ArrayList<String>();
		if (ZdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : ZdtlDataList) {
				prvdName.add(zdtlData.getPrvdName());
				infractionChnlPercent.add(zdtlData.getInfractionChnlPercent());
			}
		}
		result.put("prvdName", prvdName);
		result.put("infractionChnlPercent", infractionChnlPercent);
		return result;
	}

	/**
	 * 
	 * <pre>
	 * Desc  获取违规渠道数量-柱状图
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-7-31 下午6:48:30
	 * </pre>
	 */
	public Map<String, Object> getNumberChnlColumnData(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> ZdtlDataList = zdtlMapper.getNumberChnlColumnData(parameterData);
		List<BigDecimal> infractionChnlNum = new ArrayList<BigDecimal>();
		List<String> prvdName = new ArrayList<String>();
		if (ZdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : ZdtlDataList) {
				prvdName.add(zdtlData.getPrvdName());
				infractionChnlNum.add(zdtlData.getInfractionChnlNum());
			}
		}
		result.put("prvdName", prvdName);
		result.put("infractionChnlNum", infractionChnlNum);
		return result;
	}

	/**
	 * 
	 * <pre>
	 * Desc风险地图-渠道基本信息  
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-7-31 下午6:55:45
	 * </pre>
	 */
	public Map<String, Object> getChnlBaseInfo(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map> zdtlDataList = zdtlMapper.getChnlBaseInfo(parameterData);
		if (zdtlDataList.size() > 0) {
			result.put("chnlBase", zdtlDataList.get(0));
		}
		return result;
	}

	/**
	 * <pre>
	 * Desc  风险地图-渠道趋势信息
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-8-8 上午11:26:52
	 * </pre>
	 */
	public Map<String, Object> getChnlTrend(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> zdtlDataList = zdtlMapper.getChnlTrend(parameterData);
		List<String> audTrm = new ArrayList<String>();
		List<BigDecimal> qtyPercent = new ArrayList<BigDecimal>();
		List<BigDecimal> infractionNum = new ArrayList<BigDecimal>();
		if (zdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : zdtlDataList) {
				audTrm.add(zdtlData.getAudTrm());
				qtyPercent.add(zdtlData.getQtyPercent());
				infractionNum.add(zdtlData.getInfractionNum());
			}
		}
		result.put("audTrm", audTrm);
		result.put("qtyPercent", qtyPercent);
		result.put("infractionNum", infractionNum);
		return result;
	}

	/**
	 * <pre>
	 * Desc  统计分析-排名汇总
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-8-24 上午11:26:52
	 * </pre>
	 */
	public Map<String, Object> getRankTable(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> zdtlDataList = zdtlMapper.getRankTable(parameterData);
		result.put("rankData", zdtlDataList);
		return result;
	}

	/**
	 * <pre>
	 * Desc  统计分析-增量分析
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-8-8 上午11:27:25
	 * </pre>
	 */
	public List<Map<String, Object>> getIncrementalData(ParameterData parameterData) {
		if(parameterData.getAudTrm()==null||"".equals(parameterData.getAudTrm())){
			List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
			return map;
		}else{
			
			ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			// 第一步：获取数据
			List<ZdtlData> zdtlDataList = zdtlMapper.getIncrementalData(parameterData);
			
			Map<String, Object> singResult = null;
			ZdtlData jtZdtlData = new ZdtlData();
			List<ZdtlData> lessZeroList = new ArrayList<ZdtlData>();
			List<ZdtlData> nullList = new ArrayList<ZdtlData>();
			
			// 第二步：遍历数据，将全国、增量<0、增量为空分别插入不同的list；将增量>0的直接加入结果表中
			if (zdtlDataList.size() > 0) {
				for (ZdtlData ZdtlData : zdtlDataList) {
					if (ZdtlData.getPrvdId() == 10000) {
						jtZdtlData = ZdtlData;
					} else if (ZdtlData.getQtyPercentMom() == null) {
						nullList.add(ZdtlData);
					} else if (ZdtlData.getQtyPercentMom().compareTo(BigDecimal.ZERO) == -1) {
						lessZeroList.add(ZdtlData);
					} else {
						singResult = new HashMap<String, Object>();
						singResult.put("name", ZdtlData.getPrvdName());
						singResult.put("y", ZdtlData.getQtyPercentMom());
						result.add(singResult);
					}
				}
			}
			// 第三步：将增量<0的，逆序加入结果表中
			if (lessZeroList.size() > 0) {
				for (int i = lessZeroList.size() - 1; i >= 0; i--) {
					ZdtlData ZdtlData = lessZeroList.get(i);
					singResult = new HashMap<String, Object>();
					singResult.put("name", ZdtlData.getPrvdName());
					singResult.put("y", ZdtlData.getQtyPercentMom());
					result.add(singResult);
				}
			}
			// 第四步：将增量为空的，加入结果表中
			if (nullList.size() > 0) {
				for (ZdtlData ZdtlData : nullList) {
					singResult = new HashMap<String, Object>();
					singResult.put("name", ZdtlData.getPrvdName());
					singResult.put("y", ZdtlData.getQtyPercentMom());
					result.add(singResult);
				}
			}
			// 第五步：将全国数据，加入结果表中
			singResult = new HashMap<String, Object>();
			singResult.put("name", "全网环比增量");
			singResult.put("y", jtZdtlData.getQtyPercentMom());
			result.add(singResult);
			return result;
		}
	}

	/**
	 * <pre>
	 * Desc  统计分析-增量分析
	 * &#64;param parameterData
	 * &#64;return 废弃原因：需求变更，展示数据由异常销售数量变为异常销售占比
	 * &#64;author hufei
	 * 2017-8-8 上午11:27:25
	 * </pre>
	 * 
	 * public List<Map<String, Object>> getIncrementalData(ParameterData
	 * parameterData) { ZdtlMapper zdtlMapper =
	 * mybatisDao.getSqlSession().getMapper(ZdtlMapper.class); List<Map<String,
	 * Object>> result = new ArrayList<Map<String, Object>>(); // 第一步：获取数据 List
	 * <ZdtlData> zdtlDataList = zdtlMapper.getIncrementalData(parameterData);
	 * 
	 * Map<String, Object> singResult = null; ZdtlData jtZdtlData = new
	 * ZdtlData(); List<ZdtlData> lessZeroList = new ArrayList<ZdtlData>(); List
	 * <ZdtlData> nullList = new ArrayList<ZdtlData>();
	 * 
	 * // 第二步：遍历数据，将全国、增量<0、增量为空分别插入不同的list；将增量>0的直接加入结果表中 if
	 * (zdtlDataList.size() > 0) { for (ZdtlData ZdtlData : zdtlDataList) { if
	 * (ZdtlData.getPrvdId() == 10000) { jtZdtlData = ZdtlData; } else if
	 * (ZdtlData.getInfractionNumMom() == null) { nullList.add(ZdtlData); } else
	 * if (ZdtlData.getInfractionNumMom().compareTo(BigDecimal.ZERO) == -1) {
	 * lessZeroList.add(ZdtlData); } else { singResult = new HashMap<String,
	 * Object>(); singResult.put("name", ZdtlData.getPrvdName());
	 * singResult.put("y", ZdtlData.getInfractionNumMom());
	 * result.add(singResult); } } } // 第三步：将增量<0的，逆序加入结果表中 if
	 * (lessZeroList.size() > 0) { for (int i = lessZeroList.size() - 1; i >= 0;
	 * i--) { ZdtlData ZdtlData = lessZeroList.get(i); singResult = new
	 * HashMap<String, Object>(); singResult.put("name",
	 * ZdtlData.getPrvdName()); singResult.put("y",
	 * ZdtlData.getInfractionNumMom()); result.add(singResult); } } //
	 * 第四步：将增量为空的，加入结果表中 if (nullList.size() > 0) { for (ZdtlData ZdtlData :
	 * nullList) { singResult = new HashMap<String, Object>();
	 * singResult.put("name", ZdtlData.getPrvdName()); singResult.put("y",
	 * ZdtlData.getInfractionNumMom()); result.add(singResult); } } //
	 * 第五步：将全国数据，加入结果表中 singResult = new HashMap<String, Object>();
	 * singResult.put("name", "全网环比增量"); singResult.put("y",
	 * jtZdtlData.getInfractionNumMom()); result.add(singResult); return result;
	 * }
	 */
	/**
	 * <pre>
	 * Desc  统计分析-违规类型分析
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-8-9 上午11:36:12
	 * </pre>
	 */
	public Map<String, Object> getTypeDistributePie(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		ZdtlData zdtlData = zdtlMapper.getTypeDistributePie(parameterData);
		if (zdtlData != null) {
			result.put("silentNum", zdtlData.getSilentNum());
			result.put("keepMachineNum", zdtlData.getKeepMachineNum());
			result.put("unpackingNum", zdtlData.getUnpackingNum());
			result.put("transProvinceNum", zdtlData.getTransProvinceNum());
		}
		return result;
	}

	/**
	 * <pre>
	 * Desc  统计分析-违规类型分析
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-8-9 上午11:36:12
	 * </pre>
	 */
	public Map<String, Object> getTypeDistributeStack(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> zdtlDataList = zdtlMapper.getTypeDistributeStack(parameterData);
		List<String> audTrm = new ArrayList<String>();
		List<BigDecimal> silentNum = new ArrayList<BigDecimal>();
		List<BigDecimal> keepMachineNum = new ArrayList<BigDecimal>();
		List<BigDecimal> unpackingNum = new ArrayList<BigDecimal>();
		List<BigDecimal> transProvinceNum = new ArrayList<BigDecimal>();
		if (zdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : zdtlDataList) {
				audTrm.add(zdtlData.getAudTrm());
				silentNum.add(zdtlData.getSilentNum());
				keepMachineNum.add(zdtlData.getKeepMachineNum());
				unpackingNum.add(zdtlData.getUnpackingNum());
				transProvinceNum.add(zdtlData.getTransProvinceNum());
			}
		}
		result.put("audTrm", audTrm);
		result.put("silentNum", silentNum);
		result.put("keepMachineNum", keepMachineNum);
		result.put("unpackingNum", unpackingNum);
		result.put("transProvinceNum", transProvinceNum);
		return result;
	}

	/**
	 * <pre>
	 * Desc  统计分析-整改问责统计-近六个月整改-柱状图
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-8-28 上午10:13:12
	 * </pre>
	 */
	public Map<String, Object> getRectifyForSixColumn(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		if (parameterData.getAudTrm() == null || "".equals(parameterData.getAudTrm())) {
			Map<String, Object> result = new HashMap<String, Object>();
			
			List<String> prvdName = new ArrayList<String>();
			List<BigDecimal> rectifyNum = new ArrayList<BigDecimal>();
			result.put("prvdName", prvdName);
			result.put("rectifyNum", rectifyNum);
			return result;
		} else {

			Map<String, Object> result = new HashMap<String, Object>();
			List<ZdtlData> zdtlDataList = zdtlMapper.getRectifyForSixColumn(parameterData);
			List<String> prvdName = new ArrayList<String>();
			List<BigDecimal> rectifyNum = new ArrayList<BigDecimal>();
			if (zdtlDataList.size() > 0) {
				for (ZdtlData zdtlData : zdtlDataList) {
					prvdName.add(zdtlData.getPrvdName());
					rectifyNum.add(zdtlData.getRectifyNum());
				}
			}
			result.put("prvdName", prvdName);
			result.put("rectifyNum", rectifyNum);
			return result;
		}
	}

	/**
	 * <pre>
	 * Desc  统计分析-整改问责统计-近六个月问责-柱状图
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-8-28 上午10:13:12
	 * </pre>
	 */
	public Map<String, Object> getAccountForSixColumn(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		if(parameterData.getAudTrm()==null||"".equals(parameterData.getAudTrm())){
			Map<String, Object> result = new HashMap<String, Object>();
			List<String> prvdName = new ArrayList<String>();
			List<BigDecimal> rectifyNum = new ArrayList<BigDecimal>();
			result.put("prvdName", prvdName);
			result.put("rectifyNum", rectifyNum);
			return result;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> zdtlDataList = zdtlMapper.getAccountForSixColumn(parameterData);
		List<String> prvdName = new ArrayList<String>();
		List<BigDecimal> rectifyNum = new ArrayList<BigDecimal>();
		if (zdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : zdtlDataList) {
				prvdName.add(zdtlData.getPrvdName());
				rectifyNum.add(zdtlData.getRectifyNum());
			}
		}
		result.put("prvdName", prvdName);
		result.put("rectifyNum", rectifyNum);
		return result;
	}

	/**
	 * <pre>
	 * Desc  统计分析-整改问责统计-累计达到整改次数-柱状图
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-8-28 上午10:13:12
	 * </pre>
	 */
	public Map<String, Object> getRectifyColumn(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> zdtlDataList = zdtlMapper.getRectifyColumn(parameterData);
		List<String> prvdName = new ArrayList<String>();
		List<BigDecimal> rectifyNum = new ArrayList<BigDecimal>();
		if (zdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : zdtlDataList) {
				prvdName.add(zdtlData.getPrvdName());
				rectifyNum.add(zdtlData.getRectifyNum());
			}
		}
		result.put("prvdName", prvdName);
		result.put("rectifyNum", rectifyNum);
		return result;
	}

	/**
	 * <pre>
	 * Desc  统计分析-整改问责统计-累计达到问责次数-柱状图
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-8-28 上午10:13:12
	 * </pre>
	 */
	public Map<String, Object> getAccountabilityColumn(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> zdtlDataList = zdtlMapper.getAccountabilityColumn(parameterData);
		List<String> prvdName = new ArrayList<String>();
		List<BigDecimal> rectifyNum = new ArrayList<BigDecimal>();
		if (zdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : zdtlDataList) {
				prvdName.add(zdtlData.getPrvdName());
				rectifyNum.add(zdtlData.getRectifyNum());
			}
		}
		result.put("prvdName", prvdName);
		result.put("rectifyNum", rectifyNum);
		return result;
	}

	/**
	 * <pre>
	 * Desc  统计分析-整改问责统计-达到整改次数趋势图-折线图
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-8-28 上午10:13:12
	 * </pre>
	 */
	public Map<String, Object> getRectifyLine(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> zdtlDataList = zdtlMapper.getRectifyLine(parameterData);
		List<String> audTrm = new ArrayList<String>();
		List<BigDecimal> rectifyNum = new ArrayList<BigDecimal>();
		if (zdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : zdtlDataList) {
				audTrm.add(zdtlData.getAudTrm());
				rectifyNum.add(zdtlData.getRectifyNum());
			}
		}
		result.put("audTrm", audTrm);
		result.put("rectifyNum", rectifyNum);
		return result;
	}

	/**
	 * <pre>
	 * Desc  统计分析-整改问责统计-达到问责次数趋势图-折线图
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-8-28 上午10:13:12
	 * </pre>
	 */
	public Map<String, Object> getAccountabilityLine(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<ZdtlData> zdtlDataList = zdtlMapper.getAccountabilityLine(parameterData);
		List<String> audTrm = new ArrayList<String>();
		List<BigDecimal> rectifyNum = new ArrayList<BigDecimal>();
		if (zdtlDataList.size() > 0) {
			for (ZdtlData zdtlData : zdtlDataList) {
				audTrm.add(zdtlData.getAudTrm());
				rectifyNum.add(zdtlData.getRectifyNum());
			}
		}
		result.put("audTrm", audTrm);
		result.put("rectifyNum", rectifyNum);
		return result;
	}

	/**
	 * <pre>
	 * Desc  统计分析-审计报告
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-8-9 上午11:36:12
	 * </pre>
	 */
	public Map<String, Object> getReportInfo(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		List<ZdtlData> zdtlDataList = zdtlMapper.getReportInfo(parameterData);
		ZdtlData totalData = null;
		if (zdtlDataList.size() > 0) {
			for (int i = 0; i < zdtlDataList.size(); i++) {
				ZdtlData zdtlData = zdtlDataList.get(i);
				if ("3001,3002,3004,3005".equals(zdtlData.getConcern())) {
					totalData = zdtlData;
					totalData.setConcern("3000");
				} else {
					result.put(zdtlData.getConcern(), zdtlData);
				}
			}
			result.put(totalData.getConcern(), totalData);
		}

		return result;
	}

	/**
	 * 
	 * <pre>
	 * Desc 终端套利-统计分析-重点关注事项 
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-9-6 上午9:38:18
	 * </pre>
	 */
	public Map<String, Object> getFocusThingTable(ParameterData parameterData) {
		ZdtlMapper zdtlMapper = mybatisDao.getSqlSession().getMapper(ZdtlMapper.class);
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		List<Map> zdtlDataList = zdtlMapper.getFocusThingTable(parameterData);
		result.put("focusData", zdtlDataList);
		return result;
	}

}
