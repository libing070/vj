package com.hpe.cmca.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.KhqfMapper;
import com.hpe.cmca.pojo.KhqfData;
import com.hpe.cmca.pojo.ParameterData;

/**
 * <pre>
 * Desc： 
 * &#64;author sinly
 * &#64;refactor sinly
 * &#64;date   2017年7月3日 下午3:09:52
 * &#64;version 1.0
 * &#64;see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017年7月3日 	   sinly 	         1. Created this class.
 * </pre>
 */
@Service("KhqfService")
public class KhqfService extends BaseObject {

	@Autowired
	protected MybatisDao mybatisDao;

	// 获取个人欠费金额排名柱状图-全国
	public Map<String, Object> getAmountColumnData(ParameterData parameter) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		List<KhqfData> khqfDataList = khqfMapper.getAmountColumnData(parameter);
		List<BigDecimal> amountList = new ArrayList<BigDecimal>();
		List<String> prvdList = new ArrayList<String>();

		for (KhqfData khqfData : khqfDataList) {
			prvdList.add(khqfData.getPrvdName());
			amountList.add(khqfData.getInfractionAmt());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("prvdList", prvdList);
		result.put("amountList", amountList);
		return result;
	}

	// 获取欠费金额排名折线图-省份/全国
	public Map<String, Object> getAmountLineData(ParameterData parameter) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		List<KhqfData> khqfDataList = khqfMapper.getAmountLineData(parameter);
		List<BigDecimal> amountList = new ArrayList<BigDecimal>();
		List<String> monthList = new ArrayList<String>();
		if (khqfDataList.size() > 0) {
			for (KhqfData khqf : khqfDataList) {
				monthList.add(khqf.getAudTrm());
				amountList.add(khqf.getInfractionAmt());
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("monthList", monthList);
		result.put("amountList", amountList);
		return result;
	}

	// 获取增量分析-全国（瀑布图）
	public List<Map<String, Object>> getIncrementalData(ParameterData parameter) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		List<KhqfData> khqfDataList = khqfMapper.getIncrementalData(parameter);
		KhqfData jtKhqfData = new KhqfData();

		List<KhqfData> lessZeroList = new ArrayList<KhqfData>();
		List<KhqfData> nullList = new ArrayList<KhqfData>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> singResult = null;
		if (khqfDataList.size() > 0) {
			for (KhqfData khqfData : khqfDataList) {
				if (khqfData.getPrvdId() == 10000) {
					jtKhqfData = khqfData;
				} else if (khqfData.getIncremental() == null) {
					nullList.add(khqfData);
				} else if (khqfData.getIncremental().compareTo(BigDecimal.ZERO) == -1) {
					lessZeroList.add(khqfData);
				} else {
					singResult = new HashMap<String, Object>();
					singResult.put("name", khqfData.getPrvdName());
					singResult.put("y", khqfData.getIncremental());
					result.add(singResult);
				}
			}
		}

		if (lessZeroList.size() > 0) {
			for (int i = lessZeroList.size() - 1; i >= 0; i--) {
				KhqfData khqfData = lessZeroList.get(i);
				singResult = new HashMap<String, Object>();
				singResult.put("name", khqfData.getPrvdName());
				singResult.put("y", khqfData.getIncremental());
				result.add(singResult);
			}
		}
		if (nullList.size() > 0) {
			for (KhqfData khqfData : nullList) {
				singResult = new HashMap<String, Object>();
				singResult.put("name", khqfData.getPrvdName());
				singResult.put("y", khqfData.getIncremental());
				result.add(singResult);
			}
		}
		singResult = new HashMap<String, Object>();
		singResult.put("name", "全网环比增量");
		singResult.put("y", jtKhqfData.getIncremental());
		result.add(singResult);
		return result;
	}

	// 获取欠费金额分布(饼图)-省份/全国
	public Map<String, Object> getAmountPie(ParameterData parameter) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		KhqfData khqfData = khqfMapper.getAmountPie(parameter);
		Map<String, Object> result = new HashMap<String, Object>();
		if (khqfData != null) {
			result.put("newAmt", khqfData.getNewAmt());
			if (khqfData.getOriAmt() != null && khqfData.getOriAmt().compareTo(BigDecimal.ZERO) == -1) {
				result.put("oldAmt", 0);
			} else {
				result.put("oldAmt", khqfData.getOriAmt());
			}
		}
		return result;
	}

	// 获取集团欠费账龄分布
	public Map<String, Object> getOrgOweAging(ParameterData parameter) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		List<KhqfData> khqfDataList = khqfMapper.getOrgOweAging(parameter);
		return amtNumListToJson(khqfDataList);
	}

	// 获取集团欠费金额分布
	public Map<String, Object> getOrgAmount(ParameterData parameter) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		List<KhqfData> khqfDataList = khqfMapper.getOrgAmount(parameter);
		return amtNumListToJson(khqfDataList);

	}

	public Map<String, Object> getJTNumberPm(ParameterData khqf) {
		List<Long> numberList = new ArrayList<Long>();
		List<String> prvdList = new ArrayList<String>();
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		List<KhqfData> result_list = khqfMapper.getJTNumberPm(khqf);
		for (KhqfData khqfdata : result_list) {
			numberList.add(khqfdata.getInfractionNum());
			prvdList.add(khqfdata.getPrvdName());
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("numberList", numberList);
		data.put("prvdList", prvdList);
		return data;
	}

	public Map<String, Object> getJTNumPrvdData(ParameterData khqf) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		List<Long> numberList = new ArrayList<Long>();
		List<String> prvdList = new ArrayList<String>();
		List<KhqfData> result_list = khqfMapper.getJTNumPrvdData(khqf);
		for (KhqfData khqfdata : result_list) {
			numberList.add(khqfdata.getInfractionNum());
			prvdList.add(khqfdata.getPrvdName());
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("numberList", numberList);
		data.put("prvdList", prvdList);
		return data;
	}

	public Map<String, Object> getGRNumberData(ParameterData khqf) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		List<Long> numberList = new ArrayList<Long>();
		List<String> prvdList = new ArrayList<String>();
		List<KhqfData> result_list = khqfMapper.getGRNumberData(khqf);
		for (KhqfData khqfdata : result_list) {
			numberList.add(khqfdata.getInfractionNum());
			prvdList.add(khqfdata.getPrvdName());
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("numberList", numberList);
		data.put("prvdList", prvdList);
		return data;
	}

	public Map<String, Object> getJTNumberPmZheXian(ParameterData khqf) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		if (khqf.getAudTrm() == null || "".equals(khqf.getAudTrm())) {
			List<Long> numberList = new ArrayList<Long>();
			List<String> audtrmList = new ArrayList<String>();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("numberList", numberList);
			data.put("audtrmList", audtrmList);
			return data;
		}
		List<Long> numberList = new ArrayList<Long>();
		List<String> audtrmList = new ArrayList<String>();
		List<KhqfData> result_list = khqfMapper.getJTNumberPmZheXian(khqf);
		for (KhqfData khqfdata : result_list) {
			numberList.add(khqfdata.getInfractionNum());
			audtrmList.add(khqfdata.getAudTrm());
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("numberList", numberList);
		data.put("audtrmList", audtrmList);
		return data;
	}

	public Map<String, Object> getJTNumberDataPaiming(ParameterData khqf) {
		List<Map<String, Object>> pm_list = new ArrayList<Map<String, Object>>();
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		Map<String, Object> data = null;
		List<KhqfData> result_list = khqfMapper.getJTNumberDataPaiming(khqf);
		for (KhqfData khqfdata : result_list) {
			data = new HashMap<String, Object>();
			data.put("rn", khqfdata.getRn());
			data.put("prvdName", khqfdata.getPrvdName());
			data.put("infractionAmt", khqfdata.getInfractionAmt());
			data.put("infractionNum", khqfdata.getInfractionNum());
			data.put("newAmt", khqfdata.getNewAmt());
			data.put("newNum", khqfdata.getNewNum());
			pm_list.add(data);
		}
		data = new HashMap<String, Object>();
		data.put("paimingList", pm_list);
		return data;
	}

	public Map<String, Object> getGRNumberDataQfAge(ParameterData khqf) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		List<KhqfData> re_list = khqfMapper.getGRNumberDataQfAge(khqf);
		return amtNumListToJson(re_list);
	}

	public Map<String, Object> getGRNumberDataQfAmt(ParameterData khqf) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		List<KhqfData> re_list = khqfMapper.getGRNumberDataQfAmt(khqf);
		return amtNumListToJson(re_list);
	}

	/**
	 * 欠费账龄/金额分析 数据组装
	 * 
	 * <pre>
	 * Desc  
	 * &#64;param result_list
	 * &#64;return
	 * &#64;author issuser
	 * 2017-7-5 下午4:36:56
	 * </pre>
	 */
	public Map<String, Object> amtNumListToJson(List<KhqfData> result_list) {
		List<Map<String, Object>> num_list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> amt_list = new ArrayList<Map<String, Object>>();
		Map<String, Object> datanum = null, dataamt = null;
		for (KhqfData khqfdata : result_list) {
			datanum = new TreeMap<String, Object>();
			dataamt = new TreeMap<String, Object>();
			datanum.put("num1", khqfdata.getNum1());
			datanum.put("num2", khqfdata.getNum2());
			datanum.put("num3", khqfdata.getNum3());
			dataamt.put("amt1", khqfdata.getAmt1());
			dataamt.put("amt2", khqfdata.getAmt2());
			dataamt.put("amt3", khqfdata.getAmt3());
			num_list.add(datanum);
			amt_list.add(dataamt);
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("numList", num_list);
		data.put("amtList", amt_list);
		return data;
	}

	public Map<String, Object> getJTNumberDataQffenbu(ParameterData khqf) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		if (khqf.getAudTrm() == null || "".equals(khqf.getAudTrm())) {
			List<BigDecimal> newAmtList = new ArrayList<BigDecimal>();
			List<BigDecimal> oriAmtList = new ArrayList<BigDecimal>();
			List<String> audtrmList = new ArrayList<String>();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("newAmtList", newAmtList);
			data.put("oriAmtList", oriAmtList);
			data.put("audtrmList", audtrmList);
			return data;
		}
		List<BigDecimal> newAmtList = new ArrayList<BigDecimal>();
		List<BigDecimal> oriAmtList = new ArrayList<BigDecimal>();
		List<String> audtrmList = new ArrayList<String>();
		List<KhqfData> result_list = khqfMapper.getJTNumberDataQffenbu(khqf);
		for (KhqfData kh : result_list) {
			newAmtList.add(kh.getNewAmt());
			oriAmtList.add(kh.getOriAmt());
			audtrmList.add(kh.getAudTrm());
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("newAmtList", newAmtList);
		data.put("oriAmtList", oriAmtList);
		data.put("audtrmList", audtrmList);
		return data;
	}

	public Map<Integer, KhqfData> getMapData(ParameterData khqf) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		List<KhqfData> tpList = khqfMapper.getMapData(khqf);
		Map<Integer, KhqfData> tpMap = new HashMap<Integer, KhqfData>();
		for (KhqfData kpd : tpList) {
			if (khqf.getPrvdId() == 10000) {
				tpMap.put(kpd.getPrvdId(), kpd);
			}
			if (khqf.getPrvdId() != 10000) {
				tpMap.put(kpd.getCtyId(), kpd);
			}

		}
		return tpMap;
	}

	public Map<Integer, KhqfData> getMapBottomData(ParameterData khqf) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		List<KhqfData> tpList = khqfMapper.getMapBottomData(khqf);
		Map<Integer, KhqfData> tpMap = new HashMap<Integer, KhqfData>();
		if (tpList.size() > 0)
			tpMap.put(tpList.get(0).getPrvdId(), tpList.get(0));
		return tpMap;
	}

	public Map<Integer, KhqfData> getSjbgGrData(ParameterData khqf) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		List<KhqfData> tpList = khqfMapper.getSjbgGrData(khqf);
		Map<Integer, KhqfData> tpMap = new HashMap<Integer, KhqfData>();
		if (tpList.size() > 0)
			tpMap.put(4003, tpList.get(0));
		return tpMap;
	}

	public Map<Integer, KhqfData> getSjbgJtData(ParameterData khqf) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		List<KhqfData> tpList = khqfMapper.getSjbgJtData(khqf);
		Map<Integer, KhqfData> tpMap = new HashMap<Integer, KhqfData>();
		if (tpList.size() > 0)
			tpMap.put(4001, tpList.get(0));
		return tpMap;
	}

	public List<Map<Integer, KhqfData>> getSjbgData(ParameterData khqf) {
		Map<Integer, KhqfData> tpGrMap = getSjbgGrData(khqf);
		Map<Integer, KhqfData> tpJtMap = getSjbgJtData(khqf);
		List<Map<Integer, KhqfData>> QfList = new ArrayList<Map<Integer, KhqfData>>();
		QfList.add(tpGrMap);
		QfList.add(tpJtMap);
		return QfList;
	}

	public List<KhqfData> getSjbgCtyDetData(ParameterData khqf) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		List<KhqfData> tpList = khqfMapper.getSjbgCtyDetData(khqf);
		return tpList;
	}

	// 获取集团欠费回收分布
	public Map<String, Object> getJTQFHSData(ParameterData parameter) {

		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		if (parameter.getAudTrm() == null || "".equals(parameter.getAudTrm())) {
			List<Map<String, Object>> num_list = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> amt_list = new ArrayList<Map<String, Object>>();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("numList", num_list);
			data.put("amtList", amt_list);
			return data;

		} else {

			List<KhqfData> khqfDataList = khqfMapper.getJTQFHSData(parameter);
			return amtNumListToJson(khqfDataList);
		}
	}

	// 获取个人欠费回收分布
	public Map<String, Object> getGRQFHSData(ParameterData parameter) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		if (parameter.getAudTrm() == null || "".equals(parameter.getAudTrm())) {
			List<Map<String, Object>> num_list = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> amt_list = new ArrayList<Map<String, Object>>();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("numList", num_list);
			data.put("amtList", amt_list);
			return data;
		} else {

			List<KhqfData> khqfDataList = khqfMapper.getGRQFHSData(parameter);
			return amtNumListToJson(khqfDataList);
		}
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
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		if (parameterData.getAudTrm() == null || "".equals(parameterData.getAudTrm())) {
			Map<String, Object> result = new HashMap<String, Object>();
			List<String> prvdName = new ArrayList<String>();
			List<BigDecimal> rectifyNum = new ArrayList<BigDecimal>();
			result.put("prvdName", prvdName);
			result.put("rectifyNum", rectifyNum);
			return result;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		List<KhqfData> khqfDataList = khqfMapper.getRectifyForSixColumn(parameterData);
		List<String> prvdName = new ArrayList<String>();
		List<BigDecimal> rectifyNum = new ArrayList<BigDecimal>();
		if (khqfDataList.size() > 0) {
			for (KhqfData khqfData : khqfDataList) {
				prvdName.add(khqfData.getPrvdName());
				rectifyNum.add(khqfData.getRectifyNum());
			}
		}
		result.put("prvdName", prvdName);
		result.put("rectifyNum", rectifyNum);
		return result;
	}

	/**
	 * <pre>
	 * Desc  统计分析-整改问责统计-累计整改-柱状图
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-8-28 上午10:13:12
	 * </pre>
	 */
	public Map<String, Object> getRectifyColumn(ParameterData parameterData) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<KhqfData> khqfDataList = khqfMapper.getRectifyColumn(parameterData);
		List<String> prvdName = new ArrayList<String>();
		List<BigDecimal> rectifyNum = new ArrayList<BigDecimal>();
		if (khqfDataList.size() > 0) {
			for (KhqfData khqfData : khqfDataList) {
				prvdName.add(khqfData.getPrvdName());
				rectifyNum.add(khqfData.getRectifyNum());
			}
		}
		result.put("prvdName", prvdName);
		result.put("rectifyNum", rectifyNum);
		return result;
	}

	/**
	 * <pre>
	 * Desc  统计分析-整改问责统计-累计整改-折线图
	 * &#64;param parameterData
	 * &#64;return
	 * &#64;author hufei
	 * 2017-8-28 上午10:13:12
	 * </pre>
	 */
	public Map<String, Object> getRectifyLine(ParameterData parameterData) {
		KhqfMapper khqfMapper = mybatisDao.getSqlSession().getMapper(KhqfMapper.class);
		Map<String, Object> result = new HashMap<String, Object>();
		List<KhqfData> khqfDataList = khqfMapper.getRectifyLine(parameterData);
		List<String> audTrm = new ArrayList<String>();
		List<BigDecimal> rectifyNum = new ArrayList<BigDecimal>();
		if (khqfDataList.size() > 0) {
			for (KhqfData khqfData : khqfDataList) {
				audTrm.add(khqfData.getAudTrm());
				rectifyNum.add(khqfData.getRectifyNum());
			}
		}
		result.put("audTrm", audTrm);
		result.put("rectifyNum", rectifyNum);
		return result;
	}

}