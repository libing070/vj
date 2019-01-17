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
import com.hpe.cmca.interfaces.YjkMapper;
import com.hpe.cmca.pojo.YjkData;
import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.pojo.ZGWZData;

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
@Service("YjkService")
public class YjkService extends BaseObject {
	@Autowired
	protected MybatisDao mybatisDao;

	public Map<Integer, YjkData> getMapData(ParameterData yjk) {
		YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
		List<YjkData> tpList = yjkMapper.getMapData(yjk);
		Map<Integer, YjkData> tpMap = new HashMap<Integer, YjkData>();
		for (YjkData yd : tpList) {
			if (yjk.getPrvdId() == 10000) {
				tpMap.put(yd.getPrvdId(), yd);
			}
			if (yjk.getPrvdId() != 10000) {
				tpMap.put(yd.getCtyId(), yd);
			}

		}
		return tpMap;
	}

	public Map<Integer, YjkData> getMapBottomData(ParameterData yjk) {
		YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
		List<YjkData> tpList = yjkMapper.getMapBottomData(yjk);
		Map<Integer, YjkData> tpMap = new HashMap<Integer, YjkData>();
		if (tpList.size() > 0)
			tpMap.put(tpList.get(0).getPrvdId(), tpList.get(0));
		return tpMap;
	}

	public Map<String, Object> getColumnNumData(ParameterData yjk) {
		YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
		List<YjkData> tpList = yjkMapper.getColumnNumData(yjk);
		if(yjk.getAudTrm()==null||"".equals(yjk.getAudTrm())){
			List<String> nameList = new ArrayList<String>();
			List<BigDecimal> dataList = new ArrayList<BigDecimal>();
			for (YjkData yd : tpList) {
				nameList.add((yjk.getPrvdId() == 10000 ? yd.getPrvdName() : yd.getCtyName()));
				dataList.add(null);
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("nameList", nameList);
			result.put("dataList", dataList);
			return result;
		}
		List<String> nameList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		for (YjkData yd : tpList) {
			nameList.add((yjk.getPrvdId() == 10000 ? yd.getPrvdName() : yd.getCtyName()));
			dataList.add(yd.getNumPercent());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("nameList", nameList);
		result.put("dataList", dataList);
		return result;
	}

	public Map<String, Object> getColumnAmtData(ParameterData yjk) {
		YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
		List<YjkData> tpList = yjkMapper.getColumnAmtData(yjk);
		if(yjk.getAudTrm()==null||"".equals(yjk.getAudTrm())){
			List<String> nameList = new ArrayList<String>();
			List<BigDecimal> dataList = new ArrayList<BigDecimal>();
			for (YjkData yd : tpList) {
				nameList.add((yjk.getPrvdId() == 10000 ? yd.getPrvdName() : yd.getCtyName()));
				dataList.add(null);
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("nameList", nameList);
			result.put("dataList", dataList);
			return result;
		}
		List<String> nameList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		for (YjkData yd : tpList) {
			nameList.add((yjk.getPrvdId() == 10000 ? yd.getPrvdName() : yd.getCtyName()));
			dataList.add(yd.getAmtPercent());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("nameList", nameList);
		result.put("dataList", dataList);
		return result;
	}

	public Map<String, Object> getLineNumData(ParameterData yjk) {
		YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
		List<YjkData> tpList = yjkMapper.getLineNumData(yjk);
		List<String> trmList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		for (YjkData yd : tpList) {
			trmList.add(yd.getAudTrm());
			dataList.add(yd.getNumPercent());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("trmList", trmList);
		result.put("dataList", dataList);
		return result;
	}

	public Map<String, Object> getLineAmtData(ParameterData yjk) {
		YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
		List<YjkData> tpList = yjkMapper.getLineAmtData(yjk);
		List<String> trmList = new ArrayList<String>();
		List<BigDecimal> dataList = new ArrayList<BigDecimal>();
		for (YjkData yd : tpList) {
			trmList.add(yd.getAudTrm());
			dataList.add(yd.getAmtPercent());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("trmList", trmList);
		result.put("dataList", dataList);
		return result;
	}

	public Map<String, Object> getCrmVsVcData(ParameterData yjk) {
		YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
		if (yjk.getAudTrm() == null || "".equals(yjk.getAudTrm())) {

			List<String> yjk_stat = new ArrayList<String>();
			List<Integer> crm_num = new ArrayList<Integer>();
			List<Integer> vc_num = new ArrayList<Integer>();
			List<Integer> all_num = new ArrayList<Integer>();
			Map<String, Object> r = new HashMap<String, Object>();
			r.put("yjk_stat", yjk_stat);
			r.put("crm_num", crm_num);
			r.put("vc_num", vc_num);
			r.put("all_num", all_num);
			return r;
		} else {

			List<YjkData> tpList = yjkMapper.getCrmVsVcData(yjk);
			List<String> yjk_stat = new ArrayList<String>();
			List<Integer> crm_num = new ArrayList<Integer>();
			List<Integer> vc_num = new ArrayList<Integer>();
			List<Integer> all_num = new ArrayList<Integer>();
			for (YjkData yd : tpList) {
				yjk_stat.add(yd.getYjkStat());
				crm_num.add(yd.getCrmNum());
				vc_num.add(yd.getVcNum());
				all_num.add(yd.getAllNum());
			}

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("yjk_stat", yjk_stat);
			result.put("crm_num", crm_num);
			result.put("vc_num", vc_num);
			result.put("all_num", all_num);
			return result;
		}
	}

	// 获取增量分析-全国（瀑布图）
	public List<Map<String, Object>> getIncrementalData(ParameterData parameter) {
		YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
		List<YjkData> yjkDataList = yjkMapper.getIncrementalData(parameter);
		YjkData jtYjkData = new YjkData();

		List<YjkData> lessZeroList = new ArrayList<YjkData>();
		List<YjkData> nullList = new ArrayList<YjkData>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> singResult = null;
		if (yjkDataList.size() > 0) {
			for (YjkData yjkData : yjkDataList) {
				if (yjkData.getPrvdId() == 10000) {
					jtYjkData = yjkData;
				} else if (yjkData.getInfractionAmtMom() == null) {
					nullList.add(yjkData);
				} else if (yjkData.getInfractionAmtMom().compareTo(BigDecimal.ZERO) == -1) {
					lessZeroList.add(yjkData);
				} else {
					singResult = new HashMap<String, Object>();
					singResult.put("name", yjkData.getPrvdName());
					singResult.put("y", yjkData.getInfractionAmtMom());
					result.add(singResult);
				}
			}
		}

		if (lessZeroList.size() > 0) {
			for (int i = lessZeroList.size() - 1; i >= 0; i--) {
				YjkData yjkData = lessZeroList.get(i);
				singResult = new HashMap<String, Object>();
				singResult.put("name", yjkData.getPrvdName());
				singResult.put("y", yjkData.getInfractionAmtMom());
				result.add(singResult);
			}
		}
		if (nullList.size() > 0) {
			for (YjkData yjkData : nullList) {
				singResult = new HashMap<String, Object>();
				singResult.put("name", yjkData.getPrvdName());
				singResult.put("y", yjkData.getInfractionAmtMom());
				result.add(singResult);
			}
		}
		singResult = new HashMap<String, Object>();
		singResult.put("name", "全网环比增量");
		singResult.put("y", jtYjkData.getInfractionAmtMom());
		result.add(singResult);
		return result;
	}

	public Map<Integer, Object> getAmountPie(ParameterData yjk) {
		YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
		List<YjkData> tpList = yjkMapper.getAmountPie(yjk);
		Map<Integer, Object> result = new HashMap<Integer, Object>();
		for (YjkData yd : tpList) {
			result.put(yd.getConcernId(), yd.getInfractionNumTmp());
		}
		return result;

	}

	public Map<String, List<Object>> getPerTrend(ParameterData yjk) {
		YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
		List<YjkData> tpList = yjkMapper.getPerTrend(yjk);
		Map<String, List<Object>> result = new HashMap<String, List<Object>>();

		List<Object> listAudTrm = new ArrayList<Object>();
		List<Object> list1001 = new ArrayList<Object>();
		List<Object> list1002 = new ArrayList<Object>();
		List<Object> list1003 = new ArrayList<Object>();
		List<Object> list1004 = new ArrayList<Object>();
		List<Object> list1005 = new ArrayList<Object>();

		for (YjkData yd : tpList) {
			if (!listAudTrm.contains(yd.getAudTrm()))
				listAudTrm.add(yd.getAudTrm());
			if (yd.getConcernId() == 1001)
				list1001.add(yd.getInfractionNumTmp());
			if (yd.getConcernId() == 1002)
				list1002.add(yd.getInfractionNumTmp());
			if (yd.getConcernId() == 1003)
				list1003.add(yd.getInfractionNumTmp());
			if (yd.getConcernId() == 1004)
				list1004.add(yd.getInfractionNumTmp());
			if (yd.getConcernId() == 1005)
				list1005.add(yd.getInfractionNumTmp());
		}
		result.put("listAudTrm", listAudTrm);
		result.put("list1001", list1001);
		result.put("list1002", list1002);
		result.put("list1003", list1003);
		result.put("list1004", list1004);
		result.put("list1005", list1005);

		return result;

	}

	public List<ZGWZData> getZGWZReq(ParameterData yjk) {
		YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
		List<ZGWZData> tpList = yjkMapper.getZGWZReq(yjk);

		return tpList;
	}

	public Map<String, Object> getZGWZColumn1(ParameterData yjk) {
		if (yjk.getAudTrm() == null || "".equals(yjk.getAudTrm())) {
			List<String> nameList = new ArrayList<String>();
			List<Integer> dataList = new ArrayList<Integer>();
			Map<String, Object> r = new HashMap<String, Object>();
			r.put("nameList", nameList);
			r.put("dataList", dataList);
			return r;
		} else {

			YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
			List<ZGWZData> tpList = yjkMapper.getZGWZColumn1(yjk);
			List<String> nameList = new ArrayList<String>();
			List<Integer> dataList = new ArrayList<Integer>();
			for (ZGWZData zd : tpList) {
				nameList.add(zd.getIssuePrvdName());
				dataList.add(zd.getIssueNum());
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("nameList", nameList);
			result.put("dataList", dataList);
			return result;
		}
	}

	public Map<String, Object> getZGWZColumn2(ParameterData yjk) {
		YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
		List<ZGWZData> tpList = yjkMapper.getZGWZColumn2(yjk);
		List<String> nameList = new ArrayList<String>();
		List<Integer> dataList = new ArrayList<Integer>();
		for (ZGWZData zd : tpList) {
			nameList.add(zd.getIssuePrvdName());
			dataList.add(zd.getIssueNum());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("nameList", nameList);
		result.put("dataList", dataList);
		return result;
	}

	public Map<String, Object> getZGWZLine(ParameterData yjk) {
		YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
		List<ZGWZData> tpList = yjkMapper.getZGWZLine(yjk);
		List<String> trmList = new ArrayList<String>();
		List<Integer> dataList = new ArrayList<Integer>();
		for (ZGWZData yd : tpList) {
			trmList.add(yd.getAudTrm());
			dataList.add(yd.getIssueNum());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("trmList", trmList);
		result.put("dataList", dataList);
		return result;
	}

	public Map<Integer, YjkData> getAuditReport(ParameterData yjk) {
		YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
		List<YjkData> tpList = yjkMapper.getAuditReport(yjk);
		Map<Integer, YjkData> tpMap = new HashMap<Integer, YjkData>();
		for (YjkData yd : tpList) {
			tpMap.put(yd.getConcernId(), yd);
		}
		return tpMap;
	}

	public List<YjkData> getYjkPmhz(ParameterData yjk) {
		YjkMapper yjkMapper = mybatisDao.getSqlSession().getMapper(YjkMapper.class);
		List<YjkData> tpList = yjkMapper.getYjkPmhz(yjk);
		return tpList;
	}
}