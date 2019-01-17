package com.hpe.cmca.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.QdykMapper;
import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.pojo.QdykData;
import com.hpe.cmca.util.Json;

/**
 * 
 * <pre>
 * Desc： 
 * &#64;author   issuser
 * &#64;refactor issuser
 * &#64;date     2017-7-12 下午2:50:53
 * &#64;version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-7-12 	   issuser 	         1. Created this class.
 * </pre>
 */
@Service("qdykService")
public class QdykService extends BaseObject {
	@Autowired
	protected MybatisDao mybatisDao;

	public Map<String, Object> getCardPercentPm(ParameterData qdykdata) {
		List<BigDecimal> perList = new ArrayList<BigDecimal>();
		List<String> prvdList = new ArrayList<String>();
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> dataList = qdykMapper.getCardPercentPm(qdykdata);
		for (QdykData qdyk : dataList) {
			perList.add(qdyk.getQtyPercent());
			prvdList.add(qdyk.getPrvdName());
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("perList", perList);
		data.put("prvdList", prvdList);
		return data;

	}

	public Map<String, Object> getCardNumbersPm(ParameterData qdykdata) {
		List<BigDecimal> numberList = new ArrayList<BigDecimal>();
		List<String> prvdList = new ArrayList<String>();
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> dataList = qdykMapper.getCardNumbersPm(qdykdata);
		for (QdykData qdyk : dataList) {
			numberList.add(qdyk.getErrQtyQdyk());
			prvdList.add(qdyk.getPrvdName());
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("numberList", numberList);
		data.put("prvdList", prvdList);
		return data;

	}

	public Map<String, Object> getChnlNumbersPm(ParameterData qdykdata) {
		List<Long> numberList = new ArrayList<Long>();
		List<String> prvdList = new ArrayList<String>();
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> dataList = qdykMapper.getChnlNumbersPm(qdykdata);
		for (QdykData qdyk : dataList) {
			numberList.add(qdyk.getChnlQty());
			prvdList.add(qdyk.getPrvdName());
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("numberList", numberList);
		data.put("prvdList", prvdList);
		return data;

	}

	public Map<String, Object> getChnlPercentPm(ParameterData qdykdata) {
		List<BigDecimal> percentList = new ArrayList<BigDecimal>();
		List<String> prvdList = new ArrayList<String>();
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> dataList = qdykMapper.getChnlPercentPm(qdykdata);
		for (QdykData qdyk : dataList) {
			percentList.add(qdyk.getChnlPercent());
			prvdList.add(qdyk.getPrvdName());
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("percentList", percentList);
		data.put("prvdList", prvdList);
		return data;

	}

	public Map<String, Object> getCardPercentPmLine(ParameterData qdykdata) {
		if (qdykdata.getAudTrm() == "" || "".equals(qdykdata.getAudTrm())) {
			List<BigDecimal> percentList = new ArrayList<BigDecimal>();
			List<String> audTrmList = new ArrayList<String>();

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("percentList", percentList);
			data.put("audTrmList", audTrmList);
			return data;
		} else {

			List<BigDecimal> percentList = new ArrayList<BigDecimal>();
			List<String> audTrmList = new ArrayList<String>();
			QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
			List<QdykData> dataList = qdykMapper.getCardPercentPmLine(qdykdata);
			for (QdykData qdyk : dataList) {
				percentList.add(qdyk.getQtyPercent());
				audTrmList.add(qdyk.getAudTrm());

			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("percentList", percentList);
			data.put("audTrmList", audTrmList);

			return data;
		}

	}

	public Map<String, Object> getCardNumPmLine(ParameterData qdykdata) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		if(qdykdata.getAudTrm()==null||"".equals(qdykdata.getAudTrm())){
			List<BigDecimal> numList = new ArrayList<BigDecimal>();
			List<String> audTrmList = new ArrayList<String>();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("numList", numList);
			data.put("audTrmList", audTrmList);
			return data;
		}else{
			
			List<BigDecimal> numList = new ArrayList<BigDecimal>();
			List<String> audTrmList = new ArrayList<String>();
			
			List<QdykData> dataList = qdykMapper.getCardNumPmLine(qdykdata);
			for (QdykData qdyk : dataList) {
				numList.add(qdyk.getErrQtyQdyk());
				audTrmList.add(qdyk.getAudTrm());
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("numList", numList);
			data.put("audTrmList", audTrmList);
			return data;
		}

	}

	public Map<String, Object> getChanlPercentPmLine(ParameterData qdykdata) {
		if (qdykdata.getAudTrm() == "" || "".equals(qdykdata.getAudTrm())) {
			List<BigDecimal> percentList = new ArrayList<BigDecimal>();
			List<String> audTrmList = new ArrayList<String>();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("percentList", percentList);
			data.put("audTrmList", audTrmList);
			return data;
		} else {

			List<BigDecimal> percentList = new ArrayList<BigDecimal>();
			List<String> audTrmList = new ArrayList<String>();
			QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
			List<QdykData> dataList = qdykMapper.getChanlPercentPmLine(qdykdata);
			for (QdykData qdyk : dataList) {
				percentList.add(qdyk.getChnlPercent());
				audTrmList.add(qdyk.getAudTrm());
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("percentList", percentList);
			data.put("audTrmList", audTrmList);
			return data;
		}
	}

	public Map<String, Object> getChnlNumsPmLine(ParameterData qdykdata) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		
		if(qdykdata.getAudTrm()==null||"".equals(qdykdata.getAudTrm())){
			List<Long> numList = new ArrayList<Long>();
			List<String> audTrmList = new ArrayList<String>();
			Map<String, Object> a = new HashMap<String, Object>();
			a.put("numList", numList);
			a.put("audTrmList", audTrmList);
			return a;
		}else{
			
			List<Long> numList = new ArrayList<Long>();
			List<String> audTrmList = new ArrayList<String>();
			
			List<QdykData> dataList = qdykMapper.getChnlNumsPmLine(qdykdata);
			for (QdykData qdyk : dataList) {
				numList.add(qdyk.getChnlQty());
				audTrmList.add(qdyk.getAudTrm());
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("numList", numList);
			data.put("audTrmList", audTrmList);
			return data;
		}

	}

	public Map<Integer, QdykData> getQdykMap(ParameterData qdyk) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> tpList = qdykMapper.getQdykMap(qdyk);
		Map<Integer, QdykData> tpMap = new HashMap<Integer, QdykData>();
		for (QdykData kpd : tpList) {
			if (qdyk.getPrvdId() == 10000) {
				tpMap.put(kpd.getPrvdId(), kpd);
			}
			if (qdyk.getPrvdId() != 10000) {
				tpMap.put(kpd.getCtyId(), kpd);
			}

		}
		return tpMap;
	}

	public Map<Integer, QdykData> getQdykMapBottom(ParameterData qdyk) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> tpList = qdykMapper.getQdykMapBottom(qdyk);
		Map<Integer, QdykData> tpMap = new HashMap<Integer, QdykData>();
		if (tpList.size() > 0)
			tpMap.put(tpList.get(0).getPrvdId(), tpList.get(0));
		return tpMap;
	}

	public Map<String, Object> getQdykChnlTable(ParameterData qdyk) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> tpList = qdykMapper.getQdykChnlTable(qdyk);
		Map<String, Object> tpMap = new HashMap<String, Object>();
		tpMap.put("qdykChnl", tpList);
		return tpMap;
	}

	public Map<String, Object> getQdykChnlBaseInfo(ParameterData qdyk) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<Map> tpList = qdykMapper.getQdykChnlBaseInfo(qdyk);
		Map<String, Object> tpMap = new HashMap<String, Object>();
		if (tpList.size() > 0)
			tpMap.put("chnlInfo", tpList.get(0));
		return tpMap;
	}

	public Map<String, Object> getQdykChnlTrend(ParameterData qdyk) {
		List<String> audtrmList = new ArrayList<String>();
		List<Long> numList = new ArrayList<Long>();
		List<BigDecimal> perList = new ArrayList<BigDecimal>();
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> tpList = qdykMapper.getQdykChnlTrend(qdyk);
		Map<String, Object> tpMap = new HashMap<String, Object>();
		for (QdykData qd : tpList) {
			audtrmList.add(qd.getAudTrm());
			numList.add(qd.getErrQty());
			perList.add(qd.getQtyPercent());
		}
		tpMap.put("audtrmList", audtrmList);
		tpMap.put("numList", numList);
		tpMap.put("perList", perList);
		return tpMap;
	}

	public Map<String, Object> getQdykDataPm(ParameterData qdyk) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> tpList = qdykMapper.getQdykDataPm(qdyk);
		Map<String, Object> tpMap = new HashMap<String, Object>();
		tpMap.put("pmdata", tpList);
		return tpMap;
	}

	public List<Object> getIncrementalData(ParameterData qdyk) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> tpList = qdykMapper.getIncrementalData(qdyk);
		// List<Map<String,Object>> tpMapList = new
		// ArrayList<Map<String,Object>>();
		List<Map<String, Object>> numList1 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> numList2 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> numList3 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> numList4 = new ArrayList<Map<String, Object>>();
		List<Object> allList = new ArrayList<Object>();
		Map<String, Object> mapdata = null;
		if (tpList.size() > 0) {
			for (QdykData qdykdata : tpList) {
				if (qdykdata.getPrvdName().equals("全网环比增量")) {
					mapdata = new HashMap<String, Object>();
					mapdata.put("name", qdykdata.getPrvdName());
					mapdata.put("y", qdykdata.getIncrement());
					numList4.add(mapdata);
				} else if (qdykdata.getIncrement() == null || qdykdata.getIncrement().equals("")) {
					mapdata = new HashMap<String, Object>();
					mapdata.put("name", qdykdata.getPrvdName());
					mapdata.put("y", qdykdata.getIncrement());
					numList2.add(mapdata);
				} else if (qdykdata.getIncrement() < 0) {
					mapdata = new HashMap<String, Object>();
					mapdata.put("name", qdykdata.getPrvdName());
					mapdata.put("y", qdykdata.getIncrement());
					numList3.add(mapdata);
				} else if (qdykdata.getIncrement() > 0) {
					mapdata = new HashMap<String, Object>();
					mapdata.put("name", qdykdata.getPrvdName());
					mapdata.put("y", qdykdata.getIncrement());
					numList1.add(mapdata);
				}
			}
			Collections.reverse(numList3);
			allList.addAll(numList1);
			allList.addAll(numList2);
			allList.addAll(numList3);
			allList.addAll(numList4);
		}
		return allList;
	}

	public Map<String, Object> getQDYKqdlxData(ParameterData qdyk) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> tpList = qdykMapper.getQDYKqdlxData(qdyk);
		Map<String, Object> tpMap = new HashMap<String, Object>();
		if (tpList.size() > 0) {
			tpMap.put("zyqudao", tpList.get(0) == null ? 0 : tpList.get(0).getChnlQtyQdyk());
			tpMap.put("shqudao", tpList.get(1) == null ? 0 : tpList.get(1).getChnlQtyQdyk());
		}
		return tpMap;
	}

	public Map<String, Object> getQDYKqdlxDuiData(ParameterData qdyk) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		if(qdyk.getAudTrm()==null||"".equals(qdyk.getAudTrm())){
			List<String> monthList = new ArrayList<String>();
			List<BigDecimal> zyqdList = new ArrayList<BigDecimal>();
			List<BigDecimal> shqdList = new ArrayList<BigDecimal>();
			Map<String, Object> tpMap = new HashMap<String, Object>();
			tpMap.put("zyqudao", zyqdList);
			tpMap.put("shqudao", shqdList);
			tpMap.put("audtrm", monthList);
			return tpMap;
		}else{
			
			List<QdykData> tpList = qdykMapper.getQDYKqdlxDuiData(qdyk);
			List<String> monthList = new ArrayList<String>();
			List<BigDecimal> zyqdList = new ArrayList<BigDecimal>();
			List<BigDecimal> shqdList = new ArrayList<BigDecimal>();
			Map<String, Object> tpMap = new HashMap<String, Object>();
			if (tpList.size() > 0) {
				for (QdykData qk : tpList) {
					if (qk != null) {
						if (qk.getChnlClass() == null) {
							zyqdList.add(new BigDecimal(0));
							shqdList.add(new BigDecimal(0));
						} else if (qk.getChnlClass().equals("1")) {
							zyqdList.add(qk.getChnlQtyQdyk());
						} else if (qk.getChnlClass().equals("2")) {
							shqdList.add(qk.getChnlQtyQdyk());
						}
						if (!monthList.contains(qk.getAudTrm())) {
							monthList.add(qk.getAudTrm());
						}
					}
				}
			}
			tpMap.put("zyqudao", zyqdList);
			tpMap.put("shqudao", shqdList);
			tpMap.put("audtrm", monthList);
			return tpMap;
		}
			
	}

	public Map<String, Object> getQDYKZgwzTjSixMonth(ParameterData qdyk) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> tpList = qdykMapper.getQDYKZgwzTjSixMonth(qdyk);
		List<Long> numList = new ArrayList<Long>();
		List<String> prvdList = new ArrayList<String>();
		Map<String, Object> tpMap = new HashMap<String, Object>();
		for (QdykData qk : tpList) {
			numList.add(qk.getIncrement());
			prvdList.add(qk.getPrvdName());
		}
		tpMap.put("zgnum", numList);
		tpMap.put("prvdname", prvdList);
		return tpMap;
	}

	public Map<String, Object> getQDYKZgwzTj(ParameterData qdyk) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> tpList = qdykMapper.getQDYKZgwzTj(qdyk);
		List<Long> numList = new ArrayList<Long>();
		List<String> prvdList = new ArrayList<String>();
		Map<String, Object> tpMap = new HashMap<String, Object>();
		for (QdykData qk : tpList) {
			numList.add(qk.getIncrement());
			prvdList.add(qk.getPrvdName());
		}
		tpMap.put("zgnum", numList);
		tpMap.put("prvdname", prvdList);
		return tpMap;
	}

	public Map<String, Object> getQDYKSjzgTjLine(ParameterData qdyk) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> tpList = qdykMapper.getQDYKSjzgTjLine(qdyk);
		List<Long> numList = new ArrayList<Long>();
		List<String> audtrmList = new ArrayList<String>();
		Map<String, Object> tpMap = new HashMap<String, Object>();
		for (QdykData qk : tpList) {
			numList.add(qk.getIncrement());
			audtrmList.add(qk.getAudTrm());
		}
		tpMap.put("zgnum", numList);
		tpMap.put("audtrm", audtrmList);
		return tpMap;
	}

	public Map<String, Object> getQDYKReportText(ParameterData qdyk) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<Map<String, Object>> tpList = qdykMapper.getQDYKReportText(qdyk);
		Map<String, Object> tpMap = new HashMap<String, Object>();
		if (tpList.size() > 0) {
			tpMap.put("reportText", tpList.get(0));
		}
		return tpMap;
	}

	public Map<String, Object> getQDYKReportTable(ParameterData qdyk) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<Map<String, Object>> tpList = qdykMapper.getQDYKReportTable(qdyk);
		Map<String, Object> tpMap = new HashMap<String, Object>();
		if (tpList.size() > 0) {
			tpMap.put("reportTable", tpList);
		}
		return tpMap;
	}

	public Map<String, Object> getQDYKConcernChnl(ParameterData qdyk) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<Map<String, Object>> tpList = qdykMapper.getQDYKConcernChnl(qdyk);
		Map<String, Object> tpMap = new HashMap<String, Object>();
		if (tpList.size() > 0) {
			tpMap.put("chnlTable", tpList);
		}
		return tpMap;
	}

	public Map<String, Object> getQDYKConcernOffer(ParameterData qdyk) {
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<Map<String, Object>> tpList = qdykMapper.getQDYKConcernOffer(qdyk);
		Map<String, Object> tpMap = new HashMap<String, Object>();
		if (tpList.size() > 0) {
			tpMap.put("offerTable", tpList);
		}
		return tpMap;
	}

	public Map<String, Object> getQdykFocusChnlTrend(ParameterData qdyk) {
		List<String> audtrmList = new ArrayList<String>();
		List<Long> numList = new ArrayList<Long>();
		List<BigDecimal> perList = new ArrayList<BigDecimal>();
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> tpList = qdykMapper.getQdykFocusChnlTrend(qdyk);
		Map<String, Object> tpMap = new HashMap<String, Object>();
		for (QdykData qd : tpList) {
			audtrmList.add(qd.getAudTrm());
			numList.add(qd.getErrQty());
			perList.add(qd.getQtyPercent());
		}
		tpMap.put("audtrmList", audtrmList);
		tpMap.put("numList", numList);
		tpMap.put("perList", perList);
		return tpMap;
	}

	public Map<String, Object> getQdykFocusOfferTrend(ParameterData qdyk) {
		List<String> audtrmList = new ArrayList<String>();
		List<Long> numList = new ArrayList<Long>();
		List<BigDecimal> perList = new ArrayList<BigDecimal>();
		QdykMapper qdykMapper = mybatisDao.getSqlSession().getMapper(QdykMapper.class);
		List<QdykData> tpList = qdykMapper.getQdykFocusOfferTrend(qdyk);
		Map<String, Object> tpMap = new HashMap<String, Object>();
		for (QdykData qd : tpList) {
			audtrmList.add(qd.getAudTrm());
			numList.add(qd.getErrQty());
			perList.add(qd.getQtyPercent());
		}
		tpMap.put("audtrmList", audtrmList);
		tpMap.put("numList", numList);
		tpMap.put("perList", perList);
		return tpMap;
	}

}