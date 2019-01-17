/**
 * com.hpe.cmca.service.LlglwgService.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.LlglwgMapper;
import com.hpe.cmca.pojo.LlglwgBottomData;
import com.hpe.cmca.pojo.LlglwgCompanyInfoPojo;
import com.hpe.cmca.pojo.LlglwgData;
import com.hpe.cmca.pojo.LlglwgForZgwzInfoTablePojo;
import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.pojo.LlglwgForZgwzPojo;
import com.hpe.cmca.pojo.LltfForSumOrgPojo;
import com.hpe.cmca.util.DateUtilsForCurrProject;
import com.hpe.cmca.util.ObjectInterActiveMapUtil;

/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2018-2-2 上午10:42:51
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2018-2-2 	   hufei 	         1. Created this class.
 * </pre>
 */
@Service("LlglwgService")
public class LlglwgService extends BaseObject {

	
    @Autowired
    protected MybatisDao mybatisDao;
    //整改建议
  	private final static String ZG_OPINION = "请组织市场、集团客户、内审等部门对流量统付业务进行认真梳理、审核，深入核查业务协议管理、资费管理、营销管理等方面的规范性，综合运用外呼、拨测等方法逐一对集团公司判定的疑似流量转售客户进行核查，查证其是否存在违规行为。" ;  	
    private final static String WZTYPE_ONE = "自2018年10月通报起，对6个月中有3个月存在上述审计整改情形的公司予以审计问责" ;  
    private final static String WZ_REQUIR = "对负有直接责任、主管责任、领导责任的相关人员进行责任界定，根据规定追究相应责任。" ;  
    /**
     * 
     * <pre>
     * Desc  service:流量管理违规-风险地图
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-6 上午10:57:52
     * </pre>
     */
    public Map<Integer, LlglwgData> getMapData(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	List<LlglwgData> dataList = llglwgMapper.getMapData(parameterData);
	Map<Integer, LlglwgData> data = new HashMap<Integer, LlglwgData>();
	for (LlglwgData llglwg : dataList) {
	    if (parameterData.getPrvdId() == 10000) {
		data.put(llglwg.getPrvdId(), llglwg);
	    }
	    if (parameterData.getPrvdId() != 10000) {
		data.put(llglwg.getCtyId(), llglwg);
	    }
	}
	return data;
    }

    /**
     * 
     * <pre>
     * Desc  service:流量管理违规-审计结果-流量数量异常-柱状图
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-7 下午2:05:35
     * </pre>
     */
    public Map<Integer, Object> getMapBottomData(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	List<LlglwgBottomData> dataList = llglwgMapper.getMapBottomData(parameterData);
	Map<Integer, Object> result = new HashMap<Integer, Object>();
	if (dataList != null && dataList.size() > 0) {
	    result.put(parameterData.getPrvdId(), dataList.get(0));
	}
	return result;
    }

    /**
     * 
     * <pre>
     * Desc  service:流量管理违规-审计结果-所有柱状图
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-7 下午3:34:39
     * </pre>
     */
    public Map<String, Object> geColumnData(ParameterData parameterData) {
	// service：流量管理违规-审计结果-流量异常-柱状图
	if ("trafficNumColumn".equals(parameterData.getParameterType())) {
	    return getTrafficNumColumnData(parameterData);
	}
	// 流量管理违规-审计结果-流量占比异常-柱状图
	if ("trafficPercentColumn".equals(parameterData.getParameterType())) {
	    return getTrafficPercentColumnData(parameterData);
	}
	// service：流量管理违规-审计结果-疑似违规流量转售-疑似违规转售集团客户数-柱状图
	if ("illegalGroupNumColumn".equals(parameterData.getParameterType())) {
	    return getIllegalGroupNumColumnData(parameterData);
	}
	// 流量管理违规-审计结果-疑似违规流量转售-疑似违规转售集团客户占比-柱状图
	if ("illegalGroupPercentColumn".equals(parameterData.getParameterType())) {
	    return getIllegalGroupPercentColumnData(parameterData);
	}
	return new HashMap<String, Object>();
    }

    /**
     * 
     * <pre>
     * Desc  service:流量管理违规-审计结果-所有折线图
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-7 下午3:30:09
     * </pre>
     */
    public Map<String, Object> getLineData(ParameterData parameterData) {
	// service：流量管理违规-审计结果-流量数量异常-折线图
	if ("trafficLine".equals(parameterData.getParameterType()) || "trafficPercentLine".equals(parameterData.getParameterType())) {
	    return getTrafficLineData(parameterData);
	}
	// 流量管理违规-审计结果-疑似违规流量转售-疑似违规转售集团客户-折线图
	if ("illegalOrgLine".equals(parameterData.getParameterType()) || "illegalPercentOrgLine".equals(parameterData.getParameterType())) {
	    return getIllegalGroupLineData(parameterData);
	}
	return new HashMap<String, Object>();
    }

    /**
     *  流量管理违规-风险地图-地图下钻-异常流量赠送-7003-重点关注营销案
     * <pre>
     * Desc  
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-22 下午5:34:28
     * </pre>
     */
    public Map<String, Object> getFocusOfferOfCityTable(ParameterData parameterData) {
  	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
  	Map<String, Object> result = new LinkedHashMap<String, Object>();
  	List<Map<String, Object>> list = llglwgMapper.getFocusOfferOfCityTable(parameterData);
  	result.put("offer", list);
  	return result;
      } 
    
    
    /**
     * 
     * <pre>
     * Desc  流量管理违规-统计分析-地图下钻-疑似违规流量转售集团客户信息
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-23 上午10:30:15
     * </pre>
     */
    public Map<String, Object> getFocusOrgCustomerOfCityTable(ParameterData parameterData) {
   	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
   	Map<String, Object> result = new LinkedHashMap<String, Object>();
   	List<Map<String, Object>> list = llglwgMapper.getFocusOrgCustomerOfCityTable(parameterData);
   	result.put("orgCustomer", list);
   	return result;
       }
    
    /**
     * 
     * <pre>
     * Desc service：流量管理违规-审计结果-流量数量异常-柱状图
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-5 下午4:08:14
     * </pre>
     */
    public Map<String, Object> getTrafficNumColumnData(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	List<LlglwgData> list = llglwgMapper.getTrafficNumColumnData(parameterData);
	Map<String, Object> result = new HashMap<String, Object>();
	List<BigDecimal> errTrafficNumber = new ArrayList<BigDecimal>();
	List<String> prvdName = new ArrayList<String>();
	if (list != null && list.size() > 0) {
	    for (LlglwgData llglwgData : list) {
		prvdName.add(llglwgData.getPrvdName());
		errTrafficNumber.add(llglwgData.getErrTrafficNumber());
	    }
	}
	result.put("prvdName", prvdName);
	result.put("dataList", errTrafficNumber);
	return result;

    }

    /**
     * 
     * <pre>
     * Desc service:流量管理违规-审计结果-疑似违规流量转售-疑似违规转售集团客户数-柱状图
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-5 下午5:50:42
     * </pre>
     */
    public Map<String, Object> getIllegalGroupNumColumnData(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	List<LlglwgData> list = llglwgMapper.getIllegalGroupNumColumnData(parameterData);
	Map<String, Object> result = new HashMap<String, Object>();
	List<BigDecimal> illegalGroupNumber = new ArrayList<BigDecimal>();
	List<String> prvdName = new ArrayList<String>();
	if (list != null && list.size() > 0) {
	    for (LlglwgData llglwgData : list) {
		prvdName.add(llglwgData.getPrvdName());
		illegalGroupNumber.add(llglwgData.getIllegalGroupNumber());
	    }
	}
	result.put("prvdName", prvdName);
	result.put("dataList", illegalGroupNumber);
	return result;

    }

    /**
     * 
     * <pre>
     * Desc  service:流量管理违规-审计结果-疑似违规流量转售-疑似违规转售集团客户数占比-柱状图
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-5 下午6:29:14
     * </pre>
     */
    public Map<String, Object> getIllegalGroupPercentColumnData(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	List<LlglwgData> list = llglwgMapper.getIllegalGroupPercentColumnData(parameterData);
	Map<String, Object> result = new HashMap<String, Object>();
	List<BigDecimal> illegalGroupPercent = new ArrayList<BigDecimal>();
	List<String> prvdName = new ArrayList<String>();
	if (list != null && list.size() > 0) {
	    for (LlglwgData llglwgData : list) {
		prvdName.add(llglwgData.getPrvdName());
		illegalGroupPercent.add(llglwgData.getIllegalGroupPercent());
	    }
	}
	result.put("prvdName", prvdName);
	result.put("dataList", illegalGroupPercent);
	return result;

    }

    /**
     * 
     * <pre>
     * Desc  service：流量管理违规-审计结果-流量占比异常-柱状图
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-5 下午4:09:07
     * </pre>
     */
    public Map<String, Object> getTrafficPercentColumnData(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	List<LlglwgData> list = llglwgMapper.getTrafficPercentColumnData(parameterData);
	Map<String, Object> result = new HashMap<String, Object>();
	List<BigDecimal> errTrafficPercent = new ArrayList<BigDecimal>();
	List<String> prvdName = new ArrayList<String>();
	if (list != null && list.size() > 0) {
	    for (LlglwgData llglwgData : list) {
		prvdName.add(llglwgData.getPrvdName());
		errTrafficPercent.add(llglwgData.getErrTrafficPercent());
	    }
	}
	result.put("prvdName", prvdName);
	result.put("dataList", errTrafficPercent);
	return result;
    }

    /**
     * 
     * <pre>
     * Desc  service：流量管理违规-审计结果-流量数量异常-折线图
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-5 下午5:05:59
     * </pre>
     */
    public Map<String, Object> getTrafficLineData(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	List<LlglwgData> list = llglwgMapper.getTrafficLineData(parameterData);
	Map<String, Object> result = new HashMap<String, Object>();
	List<BigDecimal> errTrafficNumber = new ArrayList<BigDecimal>();
	List<BigDecimal> errTrafficPercent = new ArrayList<BigDecimal>();
	List<String> audTrm = new ArrayList<String>();
	if (list != null && list.size() > 0) {
	    for (LlglwgData llglwgData : list) {
		audTrm.add(llglwgData.getAudTrm());
		errTrafficNumber.add(llglwgData.getErrTrafficNumber());
		errTrafficPercent.add(llglwgData.getErrTrafficPercent());
	    }
	}
	result.put("audTrm", audTrm);
	if ("trafficPercentLine".equals(parameterData.getParameterType())) {
	    result.put("dataList", errTrafficPercent);
	}
	if ("trafficLine".equals(parameterData.getParameterType())) {
	    result.put("dataList", errTrafficNumber);
	}

	return result;
    }

    /**
     * 
     * <pre>
     * Desc  service：流量管理违规-审计结果-疑似违规流量转售-疑似违规转售集团客户-折线图
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-6 上午10:55:05
     * </pre>
     */
    public Map<String, Object> getIllegalGroupLineData(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	List<LlglwgData> list = llglwgMapper.getIllegalGroupLineData(parameterData);
	Map<String, Object> result = new HashMap<String, Object>();
	List<BigDecimal> illegalGroupNumber = new ArrayList<BigDecimal>();
	List<BigDecimal> illegalGroupPercent = new ArrayList<BigDecimal>();
	List<String> audTrm = new ArrayList<String>();
	if (list != null && list.size() > 0) {
	    for (LlglwgData llglwgData : list) {
		audTrm.add(llglwgData.getAudTrm());
		illegalGroupNumber.add(llglwgData.getIllegalGroupNumber());
		illegalGroupPercent.add(llglwgData.getIllegalGroupPercent());
	    }
	}
	result.put("audTrm", audTrm);
	if ("illegalPercentOrgLine".equals(parameterData.getParameterType())) {
	    result.put("dataList", illegalGroupPercent);
	}
	if ("illegalOrgLine".equals(parameterData.getParameterType())) {
	    result.put("dataList", illegalGroupNumber);
	}

	return result;
    }

    /**
     * 
     * <pre>
     * Desc  service:流量管理违规-统计分析-排名汇总
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-8 下午2:35:48
     * </pre>
     */
    public Map<String, Object> getRankTable(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	List<LlglwgData> list = llglwgMapper.getRankTable(parameterData);
	Map<String, Object> result = new HashMap<String, Object>();
	result.put("rankTable", list);
	return result;
    }

    /**
     * 
     * <pre>
     * Desc  service:流量管理违规-统计分析-增量分析
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-8 下午4:21:59
     * </pre>
     */
    public List<Map<String, Object>> getIncrementalData(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	// 第一步：获取数据
	List<LlglwgData> list = llglwgMapper.getIncrementalData(parameterData);

	Map<String, Object> singResult = null;
	LlglwgData orgLlglwgData = new LlglwgData();
	List<LlglwgData> lessZeroList = new ArrayList<LlglwgData>();
	List<LlglwgData> nullList = new ArrayList<LlglwgData>();

	// 第二步：遍历数据，将全国、增量<0、增量为空分别插入不同的list；将增量>0的直接加入结果表中
	if (list.size() > 0) {
	    for (LlglwgData llglwgData : list) {
		if (llglwgData.getPrvdId() == 10000) {
		    orgLlglwgData = llglwgData;
		} else if (llglwgData.getErrTrafficNumberMom() == null) {
		    nullList.add(llglwgData);
		} else if (llglwgData.getErrTrafficNumberMom().compareTo(BigDecimal.ZERO) == -1) {
		    lessZeroList.add(llglwgData);
		} else {
		    singResult = new HashMap<String, Object>();
		    singResult.put("name", llglwgData.getPrvdName());
		    singResult.put("y", llglwgData.getErrTrafficNumberMom());
		    result.add(singResult);
		}
	    }
	}
	// 第三步：将增量<0的，逆序加入结果表中
	if (lessZeroList.size() > 0) {
	    for (int i = lessZeroList.size() - 1; i >= 0; i--) {
		LlglwgData llglwgData = lessZeroList.get(i);
		singResult = new HashMap<String, Object>();
		singResult.put("name", llglwgData.getPrvdName());
		singResult.put("y", llglwgData.getErrTrafficNumberMom());
		result.add(singResult);
	    }
	}
	// 第四步：将增量为空的，加入结果表中
	if (nullList.size() > 0) {
	    for (LlglwgData llglwgData : nullList) {
		singResult = new HashMap<String, Object>();
		singResult.put("name", llglwgData.getPrvdName());
		singResult.put("y", llglwgData.getErrTrafficNumberMom());
		result.add(singResult);
	    }
	}
	// 第五步：将全国数据，加入结果表中
	singResult = new HashMap<String, Object>();
	singResult.put("name", "全网环比增量");
	singResult.put("y", orgLlglwgData.getErrTrafficNumberMom());
	result.add(singResult);
	return result;
    }

    /**
     * 
     * <pre>
     * Desc  service:流量管理违规-统计分析-重点关注地市
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-9 上午10:24:23
     * </pre>
     */
    public Map<String, Object> getFocusCityTable(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	Map<String, Object> result = new LinkedHashMap<String, Object>();
	List<Map<String, Object>> list = llglwgMapper.getFocusCityTable(parameterData);
	result.put("cityTop50", list);
	return result;
    }

    /**
     * 
     * <pre>
     * Desc service:流量管理违规-统计分析-重点关注渠道  
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-9 上午10:24:56
     * </pre>
     */
    public Map<String, Object> getFocusChnlTable(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	Map<String, Object> result = new LinkedHashMap<String, Object>();
	List<Map<String, Object>> list = llglwgMapper.getFocusChnlTable(parameterData);
	result.put("chnl", list);
	return result;
    }

    /**
     * 
     * <pre>
     * Desc service:流量管理违规-统计分析-重点关注营销案
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-9 上午10:26:50
     * </pre>
     */
    public Map<String, Object> getFocusOfferTable(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	Map<String, Object> result = new LinkedHashMap<String, Object>();
	List<Map<String, Object>> list = llglwgMapper.getFocusOfferTable(parameterData);
	result.put("offer", list);
	return result;
    }

    /**
     * 
     * <pre>
     * Desc  流量管理违规-统计分析-重点关注用户
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-22 下午4:48:07
     * </pre>
     */
    public Map<String, Object> getFocusUserTable(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	Map<String, Object> result = new LinkedHashMap<String, Object>();
	List<Map<String, Object>> list = llglwgMapper.getFocusUserTable(parameterData);
	result.put("orgCustomer", list);
	return result;
    }
    /**
     * 
     * <pre>
     * Desc 流量管理违规-统计分析-重点关注集团客户 
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-9 下午2:47:32
     * </pre>
     */
    public Map<String, Object> getFocusOrgCustomerTable(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	Map<String, Object> result = new LinkedHashMap<String, Object>();
	List<Map<String, Object>> list = llglwgMapper.getFocusOrgCustomerTable(parameterData);
	result.put("orgCustomer", list);
	return result;
    }

    /**
     * 
     * <pre>
     * Desc  service:流量管理违规-统计分析-违规类型分析-饼图  
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-9 下午3:42:20
     * </pre>
     */
    public Map<String, Object> getTypeDistributePie(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	Map<String, Object> result = new HashMap<String, Object>();
	LlglwgData llglwgData = llglwgMapper.getTypeDistributePie(parameterData);
	if (llglwgData != null) {
	    result.put("errHighFrequency", llglwgData.getErrHighFrequency());
	    result.put("errHighLimit", llglwgData.getErrHighLimit());
	    result.put("abnormalSubs", llglwgData.getAbnormalSubs());
	}
	return result;
    }

    /**
     * 
     * <pre>
     * Desc  service:流量管理违规-统计分析-违规类型分析-趋势图 
     * @param parameterData
     * @return
     * @author hufei
     * 2018-2-9 下午3:43:18
     * </pre>
     */
    public Map<String, Object> getTypeDistributeStack(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	Map<String, Object> result = new HashMap<String, Object>();
	List<LlglwgData> list = llglwgMapper.getTypeDistributeStack(parameterData);
	List<String> audTrm = new ArrayList<String>();
	List<BigDecimal> errHighFrequency = new ArrayList<BigDecimal>();
	List<BigDecimal> errHighLimit = new ArrayList<BigDecimal>();
	List<BigDecimal> abnormalSubs = new ArrayList<BigDecimal>();
	if (list != null && list.size() > 0) {
	    for (LlglwgData llglwgData : list) {
		audTrm.add(llglwgData.getAudTrm());
		errHighFrequency.add(llglwgData.getErrHighFrequency());
		errHighLimit.add(llglwgData.getErrHighLimit());
		abnormalSubs.add(llglwgData.getAbnormalSubs());
	    }
	}
	result.put("audTrm", audTrm);
	result.put("errHighFrequency", errHighFrequency);
	result.put("errHighLimit", errHighLimit);
	result.put("abnormalSubs", abnormalSubs);
	return result;
    }

    /**
     * 
     * <pre>
     * Desc  流量管理违规-审计报告
     * @param parameterData
     * @return
     * @author hufei
     * 2018-3-12 下午4:47:16
     * </pre>
     */
    public String getReportInfo(ParameterData parameterData) {
	LlglwgMapper llglwgMapper = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	@SuppressWarnings("unused")
	Map<String, Object> result = new LinkedHashMap<String, Object>();
	Map<String, Object> llzsResult = llglwgMapper.getLlzsAuditReport(parameterData);
	Map<String, Object> llzyResult = llglwgMapper.getLlzyAuditReport(parameterData);
	String modelText1 = "一、流量异常赠送|审计关注异常大额、多次赠送流量以及向非正常状态用户、终端赠送流量。审计期间，共赠送流量值{giv_value_tol}G，异常赠送流量值{infrac_value}G，占比{infrac_value_pre}%，存在拉低整体流量价值的风险。|";
	String modelText2 = "疑似违规流量转售|审计关注办理通用流量统付业务的集团客户将流量转售给个人客户的违规行为。审计期间，开展通用流量统付业务的集团客户{tf_cnt_org}个，统付流量合计{tf_sum_strm_cap}G。审计识别出{yszs_cnt_org}个集团客户存在疑似违规流量转售行为，涉及统付流量{yszs_sum_strm_cap}G，占当期集团客户通用流量统付总量的{yszs_propor}%，存在集团客户违规转售套利的风险。";
	String tpText = "";
	if (llzsResult != null && llzsResult.get("infrac_cnt") != null && !"0".equals(llzsResult.get("infrac_cnt").toString())) {
	    tpText = tpText + replaceParam(modelText1, llzsResult);
	}
	if (llzyResult != null && llzyResult.get("yszs_sum_cnt") != null && !"0".equals(llzyResult.get("yszs_sum_cnt").toString())) {
	    if (llzsResult != null && llzsResult.get("infrac_cnt") != null && !"0".equals(llzsResult.get("infrac_cnt").toString())) {
		tpText = tpText + "二、" + replaceParam(modelText2, llzyResult);
	    } else {
		tpText = tpText + replaceParam(modelText2, llzyResult);
	    }
	}
	if ("".equals(tpText)) {
	    return null;
	} else {
	    String[] stringList = tpText.split("\\|");
	    StringBuffer resultBuffer = new StringBuffer(512);
	    for (int i = 0; i < stringList.length; i++) {
		resultBuffer.append(stringList[i]);
		resultBuffer.append("<br/>");
	    }
	    return resultBuffer.toString();
	}

    }

    /**
     * <pre>
     * Desc  数据填充入模板的变量
     * @param text：模板文本
     * @param params：变量
     * @return
     * @author hufei
     * 2017-9-4 上午10:45:20
     * </pre>
     */
    public String replaceParam(String text, Map<String, Object> params) {
	Matcher m = Pattern.compile("[{]([^{^}]*?)[}]").matcher(text);
	StringBuffer sb = new StringBuffer();
	while (m.find()) {
	    m.appendReplacement(sb, params.get(m.group(1)).toString());
	}
	m.appendTail(sb);
	return sb.toString();
    }
    

    
    /**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
 	
    /**
     * <pre>
     * 		@Description  六个月内达到整改标准次数排名——柱状图
     * 		@param currDate：当前审计月
     * 		@return HashMap；
     * 		@author 林春雨
     * </pre>
     */
 	 public Map<String, Object> getAmountColumnDataForChangeStandardBetweenSixMonth(String currDate) throws ParseException {

	 			Map<String, Object> result = new HashMap<String, Object>();
	 	 		 ArrayList<BigDecimal> amountList = new ArrayList<BigDecimal>();
		 		 ArrayList<String> prvdList = new ArrayList<String>();
 		 		//获取当前年月
		 		 Date date = null  ;
		 		 //当前日期是否为空
		 		 if(currDate == null || "".equals(currDate)){
		 			result.put("prvdName", prvdList);
			 		result.put("accountNum", amountList);
		 		 }else{
		 			 //当前日期是否可转换字符串
		 			 if(DateUtilsForCurrProject.currStrIsNotDate(currDate) == false){
		 				date = new Date() ;
			 			currDate = DateUtilsForCurrProject.getYearAndMonth(date) ;
		 			 }
		 			date = DateUtilsForCurrProject.stringToDate(currDate) ;
		 			 //获取六个月前的年月
			 		 String sixMonthDate = DateUtilsForCurrProject.GetDateBeforeSixMonth(date) ;
			 		LlglwgMapper llglwgMapper  = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
			 		 ArrayList<LlglwgForZgwzPojo> sjzgwzDataForSixMonthList = llglwgMapper.getAmountColumnDataForChangeStandardSixMonth(sixMonthDate,currDate) ;
			 		 if(sjzgwzDataForSixMonthList != null && sjzgwzDataForSixMonthList.size() != 0){
			 			 //建立装载数据集合
				 		 for (LlglwgForZgwzPojo lfzp : sjzgwzDataForSixMonthList) {
				 			prvdList.add(lfzp.getProvinceName());
				 		    amountList.add(lfzp.getCurrNumForColumn());
						 }
				 		result.put("prvdName", prvdList);
				 		result.put("rectifyNum", amountList);
			 		 }else{
			   			result.put("prvdName", prvdList);
				 		result.put("accountNum", amountList);
			  		}
		 		 }
		 		 return result ;
 	 }
 	 
 	 /**
      * <pre>
      * 		@Description  累计整改次数排名——柱状图
      * 		@param currDate：当前审计月
      * 		@return HashMap；
      * 		@author 林春雨
      * </pre>
      */
 	 public Map<String,Object> getAmountColumnDataForChangeStandardAll(String currDate){
 		 
   		 Map<String, Object> result = new HashMap<String, Object>();
		 ArrayList<BigDecimal> amountList = new ArrayList<BigDecimal>();
		 ArrayList<String> prvdList = new ArrayList<String>();
 		 //判断当前date是否可以转换时间格式
 		 if(currDate == null || "".equals(currDate)){
 			result.put("prvdName", prvdList);
	 		result.put("accountNum", amountList);
 		 }else{
	 		 //根据时间段获取每个省份数据集合
	 		LlglwgMapper llglwgMapper  = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	 		 ArrayList<LlglwgForZgwzPojo> sjzgwzDataAllList = llglwgMapper.getAmountColumnDataForChangeStandardAll(currDate) ;
	
	 		if(sjzgwzDataAllList != null && sjzgwzDataAllList.size() != 0){
		 		 //迭代集合装入省份与数据集合
		 		 for (LlglwgForZgwzPojo lfzp : sjzgwzDataAllList) {
		 			prvdList.add(lfzp.getProvinceName());
		 		    amountList.add(lfzp.getCurrNumForColumn());
				 }
		 		result.put("prvdName", prvdList);
		 		result.put("rectifyNum", amountList);
	 		}else{
	  			result.put("prvdName", prvdList);
		 		result.put("accountNum", amountList);
	  		}
 		 }
 		 return result ;
 	 }
 	 
 	 /**
      * <pre>
      * 		@Description  达到整改标准省公司数量趋势—折线图
      * 		@param currDate：当前审计月
      * 		@initDate：初始化日期—201809
      * 		@return HashMap；
      * 		@author 林春雨
      * </pre>
      */
 	 public Map<String,Object> getRectifiyLineDataForChangeStandard(String dateEnd) throws ParseException{
 		 

 		Map<String, Object> result = new HashMap<String, Object>();
 		//新建返回数据集合
 		ArrayList<Integer> lineList = new ArrayList<Integer>();
 		
 		//判断当前两个日期是否可以转换
// 		 if(dateStart == null || dateStart.equals("")){
// 			 		//给出默认值
		String	dateStart = "201809" ;
//		 }else{
//			 if(DateUtilsForCurrProject.currStrIsNotDate(dateStart) == false){
//	 			 	//给出默认值
//	 			 	dateStart = "201510" ;
//			 	}
//		 }
 		if(dateEnd == null || "".equals(dateEnd)){
 			ArrayList<String> allDateList = new ArrayList<String>() ;
 			result.put("audTrm", allDateList);
 	 		result.put("rectifyNum", lineList);
	 			
		 }else{
			 if(DateUtilsForCurrProject.currStrIsNotDate(dateStart) != false){
				 		 LlglwgMapper llglwgMapper  = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
				  		//查询月份中有数据的集合
				  		ArrayList<LlglwgForZgwzPojo> dataList = llglwgMapper.getRectifiyLineDataForChangeStandardAll(dateStart, dateEnd) ;
				 		//获取开始到结束所有月份信息
				  		ArrayList<String> allDateList = DateUtilsForCurrProject.getAllYearAndMonth(dateStart, dateEnd) ;
				  		//增加默认值
				  		for (int i = 0; i < allDateList.size(); i++) {
				  			lineList.add(0) ;
				 		}
				  		if(dataList != null && dataList.size() !=0){
					  		//便利查询数据，根据含有数据的月份下标，修改对应数据集合的数据
					  		for(LlglwgForZgwzPojo lfzp : dataList){
					  			//获取含有数据的审计月
					  			String audTrm = lfzp.getAuditMonth() ;
					  			//获取当前审计月对应的数据
					  			BigDecimal num = lfzp.getCurrNumForLine() ;
					  			int numInt = num.intValue() ;
					  			//查询当前审计月再月份中的下标位置
					  			for (int i = 0; i < allDateList.size(); i++) {
					 				String dateStr = allDateList.get(i) ;
					 				//如果当前日期内有数据
					 				if(dateStr.equals(audTrm)){
					 					//根据下标修改值
					 					lineList.set(i , numInt) ;
					 				}
					 			}
					  		}
					  		result.put("audTrm", allDateList);
					  		result.put("rectifyNum", lineList);
				  		}else{
				  			result.put("audTrm", allDateList);
					  		result.put("rectifyNum", lineList);
				  		}
			 	}else{
			 		ArrayList<String> allDateList = new ArrayList<String>() ;
			 		result.put("audTrm", allDateList);
			  		result.put("rectifyNum", lineList);
			 	}
		 }

		return result;
 	 }
 	 //-------------------------------------------------------------------------------------------------------------------------------------
 	 
 	 /**
      * <pre>
      * 		@Description  六个月内达到问责标准次数排名——柱状图
      * 		@param currDate：当前审计月
      * 		@initDate：初始化日期—201809
      * 		@return HashMap；
      * 		@author 林春雨
      * </pre>
      */
 	 public Map<String, Object> getAccountForSixColumn(String currDate) throws ParseException {
 		 
		 		 //新建返回数值集合
		 		 ArrayList<BigDecimal> amountList = new ArrayList<BigDecimal>();
		 		 ArrayList<String> prvdList = new ArrayList<String>();
			 	 Map<String, Object> result = new HashMap<String, Object>();
		 		 //获取当前年月
		 		 Date date = null  ;
		 		 //当前日期是否为空
		 		 if(currDate == null || "".equals(currDate)){
		 			 result.put("prvdName", prvdList);
		 			 result.put("accountNum", amountList);
		 		 }else{
		 			 //当前日期是否可转换字符串
		 			 if(DateUtilsForCurrProject.currStrIsNotDate(currDate) == false){
		 				date = new Date() ;
			 			currDate = DateUtilsForCurrProject.getYearAndMonth(date) ;
		 			 }
		 			date = DateUtilsForCurrProject.stringToDate(currDate) ;
		 			 //获取六个月前的年月
			 		 String sixMonthDate = DateUtilsForCurrProject.GetDateBeforeSixMonth(date) ;
			 		 LlglwgMapper llglwgMapper  = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
			 		 
//			 		
				 	//六个月内达到问责标准的省份次数排名
				 	ArrayList<LlglwgForZgwzPojo> sjzgwzDataForSixMonthList = llglwgMapper.getAccountForSixColumn(sixMonthDate, currDate) ;
			 		if(sjzgwzDataForSixMonthList.size() != 0){
			 			//查询当前月被问责的省公司
			 			
			 			//近六个月至当前月被问责的省公司数量
				 		for (LlglwgForZgwzPojo lfzp : sjzgwzDataForSixMonthList) {
				 			prvdList.add(lfzp.getProvinceName());
				 		    amountList.add(lfzp.getCurrNumForColumn());
						 }
				 		result.put("prvdName", prvdList);
				 		result.put("accountNum", amountList);
			 		}else{
			  			result.put("prvdName", prvdList);
				 		result.put("accountNum", amountList);
			  		}
		 		 }
		 		
		 		return result ;
 	 }
 	 
 	 /**
      * <pre>
      * 		@Description  累计问责标准次数排名——柱状图
      * 		@param currDate：当前审计月
      * 		@initDate：初始化日期—201809
      * 		@return HashMap；
      * 		@author 林春雨
      * </pre>
      */
 	 public Map<String,Object> getAccountabilityColumn(String currDate){
 		 ArrayList<BigDecimal> amountList = new ArrayList<BigDecimal>();
 		 ArrayList<String> prvdList = new ArrayList<String>();
  		Map<String, Object> result = new HashMap<String, Object>();
 		 //判断日期是否可以转换
 		 if(currDate == null || "".equals(currDate)){
 			result.put("prvdName", prvdList);
	 		result.put("accountNum", amountList);
 		 }else{
	 		 //查询当钱累计每个省份数据集合
	 		LlglwgMapper llglwgMapper  = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	 		 ArrayList<LlglwgForZgwzPojo> sjzgwzDataAllList = llglwgMapper.getAccountabilityColumn(currDate) ;
	 		
	  		if(sjzgwzDataAllList != null && sjzgwzDataAllList.size() != 0){
		 		 for (LlglwgForZgwzPojo lfzp : sjzgwzDataAllList) {
		 			prvdList.add(lfzp.getProvinceName());
		 		    amountList.add(lfzp.getCurrNumForColumn());
				 }
		 		result.put("prvdName", prvdList);
		 		result.put("accountNum", amountList);
	  		}else{
	  			result.put("prvdName", prvdList);
		 		result.put("accountNum", amountList);
	  		}
 		 }
 		 return result ;
 	 }


 	/**
      * <pre>
      * 		@Description  达到问责标准数量趋势
      * 		@param dateEnd：当前审计月
      * 		@initDate：初始化日期—201809
      * 		@return HashMap；
      * 		@author 林春雨
      * </pre>
      */
 	 public Map<String,Object> getAccountabilityLine(String dateEnd) throws ParseException{

 		Map<String, Object> result = new HashMap<String, Object>();
 		//新建返回数据集合
  		ArrayList<Integer> lineList = new ArrayList<Integer>();
 		//判断当前两个日期是否可以转换
// 		 if(dateStart == null || dateStart.equals("")){
 			 		//给出默认值
		String	dateStart = "201809" ;
//		 }else{
//			 if(DateUtilsForCurrProject.currStrIsNotDate(dateStart) == false){
//	 			 	//给出默认值
//	 			 	dateStart = "201510" ;
//			 	}
//		 }
 		if(dateEnd == null || "".equals(dateEnd)){
 			ArrayList<String> allDateList  = new ArrayList<String>() ;
 			result.put("audTrm", allDateList);
 	 		result.put("accountNum", lineList);
	 			
		 }else{
			 if(DateUtilsForCurrProject.currStrIsNotDate(dateStart) == false){
					 	//给出默认值
				 		Date date = new Date() ;
				 		dateEnd = DateUtilsForCurrProject.getYearAndMonth(date) ;
			 	}
			 LlglwgMapper llglwgMapper  = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
		 		//查询月份中有数据的集合
		 		ArrayList<LlglwgForZgwzPojo> dataList = llglwgMapper.getAccountabilityLine(dateStart, dateEnd) ;
				//获取开始到结束所有月份信息
		 		ArrayList<String> allDateList = DateUtilsForCurrProject.getAllYearAndMonth(dateStart, dateEnd) ;
		 		
		 		//增加默认值
		 		for (int i = 0; i < allDateList.size(); i++) {
		 			lineList.add(0) ;
				}
		 		//便利查询数据，根据含有数据的月份下标，修改对应数据集合的数据
		 		for(LlglwgForZgwzPojo lfzp : dataList){
		 			//获取含有数据的审计月
		 			String audTrm = lfzp.getAuditMonth() ;
		 			//获取当前审计月对应的数据
		 			BigDecimal num = lfzp.getCurrNumForLine() ;
		 			int numInt = num.intValue() ;
		 			//查询当前审计月再月份中的下标位置
		 			for (int i = 0; i < allDateList.size(); i++) {
						String dateStr = allDateList.get(i) ;
						//如果当前日期内有数据
						if(dateStr.equals(audTrm)){
							//根据下标修改值
							lineList.set(i , numInt) ;
						}
					}
		 		}
		 		result.put("audTrm", allDateList);
		 		result.put("accountNum", lineList);
		 }
		return result;
 	 }

 	 
 	/**
      * <pre>
      * 		@Description  整改及问责事项明细表
      * 		@param audTrm：当前审计月，prvdId：当前集团省份
      * 		@initDate：初始化日期—201809
      * 		@return HashMap；
      * 		@author 林春雨
      * </pre>
      */
	public ArrayList<HashMap<String,Object>> getChangeAccountInfoTable(String audTrm, String prvdId) throws ParseException {
// 		String wzDetailInfo = "" ;
 		//根据审计月查询当前距离当前审计月六个月前的月份集合
 		 //获取六个月前的年月
		 Date date = null ; 
		 if(audTrm == null || "".equals(audTrm)){
			 audTrm = "200001" ;
		 }
		date = DateUtilsForCurrProject.stringToDate(audTrm) ;
		 String sixMonthDate = DateUtilsForCurrProject.GetDateBeforeSixMonth(date) ;
		 LlglwgMapper llglwgMapper  = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
		//违规情况
		List<LlglwgForZgwzInfoTablePojo> wgCompanyListForZg = llglwgMapper.getChangeAccountInfoTableForZg(audTrm, prvdId) ;
		ArrayList<HashMap<String,Object>> wgCompanyMapList = ObjectInterActiveMapUtil.getZgObjectListMap(wgCompanyListForZg, audTrm) ;	
		
		
		//获取六个月前至审计月的所有日期集合
		ArrayList<String> dateForSixMonthList = DateUtilsForCurrProject.getAllYearAndMonth(sixMonthDate , audTrm) ;
	
		ArrayList<String> allwzDetail  ;
		
		//装载问责违规条目
		if(prvdId.equals("10000")){
				//查询当前问责省份问责具体信息--------------------（全国）
				//查询当前审计月有哪些问责省份
				List<LlglwgForZgwzInfoTablePojo> wgCompanyListForWz = llglwgMapper.getWzCompanyByAudTrmAndPrvdId(audTrm, prvdId) ;
				if(wgCompanyListForWz!= null && wgCompanyListForWz.size()!=0){
					for (int k = 0; k < wgCompanyListForWz.size(); k++) {
						LlglwgForZgwzInfoTablePojo wgCompanyPrvd = wgCompanyListForWz.get(k) ;
						//迭代省份，	//装载所有月份问责信息
						allwzDetail = new ArrayList<String>() ;
						//装载当前违规省份数据
						HashMap<String , Object> wgCompanyMap = new HashMap<>() ;
						wgCompanyMap.put("rn", wgCompanyMapList.size()+1) ; //序号
						wgCompanyMap.put("wtSummary", WZTYPE_ONE) ; //问责类型一
						
						wgCompanyMap.put("zgOpinion", ZG_OPINION) ;
						//通过审计月查询当前整改时间
						String zgwzDate = DateUtilsForCurrProject.getAfterAnyMonthLastDay(audTrm , 4) ;
						wgCompanyMap.put("prvdName", wgCompanyPrvd.getPrvdName()) ;
						//将整改时间更换为（YYYY年MM月）
						String zgTime = DateUtilsForCurrProject.yyyyMMToyyyyYearMMMonth(audTrm) ;
						wgCompanyMap.put("zgTime", zgTime) ;//整改时间
						wgCompanyMap.put("zgqx", zgwzDate) ;
						wgCompanyMap.put("wzRequire", WZ_REQUIR) ;	//问责要求
						//获取当前月违规省份信息
						String provinceId = wgCompanyPrvd.getPrvdId() ;
//						//根据省份ID及当前审计月查询当前审计月省份问责具体信息
//						List<LlglwgForZgwzInfoTablePojo> currAudTrmCompanyListForWz = llglwgMapper.getWzCompanyForCurrMonth(audTrm, provinceId) ;
						//迭代当前月份被问责省份信息
//						ArrayList<String> wzDetail = new ArrayList<String>() ;
						ArrayList<String> sixDetail = null ;
//						for(LlglwgForZgwzInfoTablePojo caclfw : currAudTrmCompanyListForWz){
//							if(caclfw.getWgType().equals("zg1")){
//								//问题详细描述拼接
//								double newPrvdYszsSum = caclfw.getPrvdYszsSum().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
//								BigDecimal Zg1per = caclfw.getZg1pe() ;
//								double newZg1per = Zg1per.doubleValue() ;
//								double lastZg1perDouble = newZg1per * 100 ;
//								double lastZg1per = m2Double(lastZg1perDouble) ;
//								String ZG_DES_ONE = caclfw.getPrvdName()+"公司疑似违规转售流量总值"+newPrvdYszsSum+"TB且违规转售流量占本公司统付流量总量比例"+lastZg1per+"%" ;
//								wzDetail.add(ZG_DES_ONE) ;
//							}else if(caclfw.getWgType().equals("zg2")){
//								//保留两位小数
//								double newOrgYszsllSum = caclfw.getOrgYszsllSum().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ; 
//								BigDecimal Zg2pe = caclfw.getZg2pe() ;
//								double newZg2pe = Zg2pe.doubleValue() ;
//								double lastZg2peDouble = newZg2pe * 100 ;
//								double lastZg2pe = m2Double(lastZg2peDouble) ;
//								String ZG_DES_TWO = caclfw.getPrvdName()+"公司集团客户“"+caclfw.getOrgNm()+"”疑似违规转售流量超过"+newOrgYszsllSum+"TB且占当月全集团违规转售流量总量比例"+lastZg2pe+"%" ;
//								wzDetail.add(ZG_DES_TWO) ;
//							}else if(caclfw.getWgType().equals("zg3")){
//								//将审计月去除空格
//								String newWgAud = caclfw.getWgAud().trim() ;
//								String lastWgAud = newWgAud.replace(",", "、") ;
//								String ZG_DES_THREE = caclfw.getPrvdName()+"公司集团客户“"+caclfw.getOrgNm()+"”于审计月"+lastWgAud+"被判定为疑似违规转售流量" ;
//								wzDetail.add(ZG_DES_THREE) ;
//							}
//						}
						//迭代六个月信息集合
						for(int i=0 ; i<dateForSixMonthList.size() ; i++){
							sixDetail = new ArrayList<String>() ;
							String currYearAndMonth = dateForSixMonthList.get(i) ;
							List<LlglwgForZgwzInfoTablePojo> sixMonthWzCompanyInfoList = llglwgMapper.getSixMonthInfo(currYearAndMonth, provinceId, audTrm) ;
							for(LlglwgForZgwzInfoTablePojo sixMonthWzCompany : sixMonthWzCompanyInfoList){
								if(sixMonthWzCompany.getWgType().equals("zg1")){
									//问题详细描述拼接
									double newPrvdYszsSum = sixMonthWzCompany.getPrvdYszsSum().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
									BigDecimal zg1per = sixMonthWzCompany.getZg1pe() ;
									double newZg1per = zg1per.doubleValue() ;
									double lastZg1perDouble = newZg1per * 100 ;
									double lastZg1per = m2Double(lastZg1perDouble) ;
									String ZG_DES_ONE = sixMonthWzCompany.getPrvdName()+"公司疑似违规转售流量总值"+newPrvdYszsSum+"TB且违规转售流量占本公司统付流量总量比例"+lastZg1per+"%" ;
									sixDetail.add(ZG_DES_ONE) ;
								}else if(sixMonthWzCompany.getWgType().equals("zg2")){
									//保留两位小数
									double newOrgYszsllSum = sixMonthWzCompany.getOrgYszsllSum().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ; 
									BigDecimal zg2pe = sixMonthWzCompany.getZg2pe() ;
									double newZg2pe = zg2pe.doubleValue() ;
									double lastZg2peDouble = newZg2pe * 100 ;
									double lastZg2pe = m2Double(lastZg2peDouble) ;
									String ZG_DES_TWO = sixMonthWzCompany.getPrvdName()+"公司集团客户“"+sixMonthWzCompany.getOrgNm()+"”疑似违规转售流量"+newOrgYszsllSum+"TB且占当月全集团违规转售流量总量比例"+lastZg2pe+"%" ;
									sixDetail.add(ZG_DES_TWO) ;
								}else if(sixMonthWzCompany.getWgType().equals("zg3")){
									//将审计月去除空格
									String newWgAud = sixMonthWzCompany.getWgAud().trim() ;
									String lastWgAud = newWgAud.replace(",", "、") ;
									String ZG_DES_THREE =sixMonthWzCompany.getPrvdName()+"公司集团客户“"+sixMonthWzCompany.getOrgNm()+"”于审计月"+lastWgAud+"被判定为疑似违规转售流量" ;
									sixDetail.add(ZG_DES_THREE) ;
								}
							}
							if(sixMonthWzCompanyInfoList != null && sixMonthWzCompanyInfoList.size() !=0){
								String sixMonthWzDetail = StringUtils.join(sixDetail , "；") ;
								allwzDetail.add( currYearAndMonth+"，" +sixMonthWzDetail+"。") ;
							}
						}
//						String currMonthWzDetail = StringUtils.join(wzDetail , "；") ;
//						allwzDetail.add(audTrm+"，"+currMonthWzDetail+"。") ;
						String lastDetailInfo = StringUtils.join(allwzDetail , "<br />") ;
						wgCompanyMap.put("wtDetails", lastDetailInfo) ; //问题详细描述
						wgCompanyMapList.add(wgCompanyMap) ;
					}
				}
				
		}else{
			//查询当前问责省份问责具体信息--------------------（省份 )
			//查询省份名称
			String prvdName = llglwgMapper.getPrvdNameByID(prvdId) ;
			List<LlglwgForZgwzInfoTablePojo> currAudTrmCompanyListForWz = llglwgMapper.getWzCompanyForCurrMonth(audTrm, prvdId) ;
			if(currAudTrmCompanyListForWz != null && currAudTrmCompanyListForWz.size() !=0){
				allwzDetail = new ArrayList<String>() ;
				//装载当前违规省份数据
				HashMap<String , Object> wgCompanyMap = new HashMap<>() ;
				wgCompanyMap.put("rn", wgCompanyMapList.size()+1) ; //序号
				wgCompanyMap.put("wtSummary", WZTYPE_ONE) ; //问责类型一
				
				wgCompanyMap.put("zgOpinion", ZG_OPINION) ;
				//通过审计月查询当前整改时间
				String zgwzDate = DateUtilsForCurrProject.getAfterAnyMonthLastDay(audTrm , 4) ;
				wgCompanyMap.put("prvdName", prvdName) ;
				//将整改时间更换为（YYYY年MM月）
				String zgTime = DateUtilsForCurrProject.yyyyMMToyyyyYearMMMonth(audTrm) ;
				wgCompanyMap.put("zgTime", zgTime) ;//整改时间
				wgCompanyMap.put("zgqx", zgwzDate) ;
				wgCompanyMap.put("wzRequire", WZ_REQUIR) ;	//问责要求
				//获取当前月违规省份信息
				//根据省份ID及当前审计月查询当前审计月省份问责具体信息
				//迭代当前月份被问责省份信息
//				ArrayList<String> wzDetail = new ArrayList<String>() ;
				ArrayList<String> sixDetail = null ;
//				for(LlglwgForZgwzInfoTablePojo caclfw : currAudTrmCompanyListForWz){
//					if(caclfw.getWgType().equals("zg1")){
//						//问题详细描述拼接
//						double newPrvdYszsSum = caclfw.getPrvdYszsSum().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
//						BigDecimal Zg1per = caclfw.getZg1pe() ;
//						double newZg1per = Zg1per.doubleValue() ;
//						double lastZg1perDouble = newZg1per * 100 ;
//						double lastZg1per = m2Double(lastZg1perDouble) ;
//						String ZG_DES_ONE = caclfw.getPrvdName()+"公司疑似违规转售流量总值"+newPrvdYszsSum+"TB且违规转售流量占本公司统付流量总量比例"+lastZg1per+"%" ;
//						wzDetail.add(ZG_DES_ONE) ;
//					}else if(caclfw.getWgType().equals("zg2")){
//						//保留两位小数
//						double newOrgYszsllSum = caclfw.getOrgYszsllSum().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ; 
//						BigDecimal Zg2pe = caclfw.getZg2pe() ;
//						double newZg2pe = Zg2pe.doubleValue() ;
//						double lastZg2peDouble = newZg2pe * 100 ;
//						double lastZg2pe = m2Double(lastZg2peDouble) ;
//						String ZG_DES_TWO = caclfw.getPrvdName()+"公司集团客户“"+caclfw.getOrgNm()+"”疑似违规转售流量超过"+newOrgYszsllSum+"TB且占当月全集团违规转售流量总量比例"+lastZg2pe+"%" ;
//						wzDetail.add(ZG_DES_TWO) ;
//					}else if(caclfw.getWgType().equals("zg3")){
//						//将审计月去除空格
//						String newWgAud = caclfw.getWgAud().trim() ;
//						String lastWgAud = newWgAud.replace(",", "、") ;
//						String ZG_DES_THREE = caclfw.getPrvdName()+"公司集团客户“"+caclfw.getOrgNm()+"”于审计月"+lastWgAud+"被判定为疑似违规转售流量" ;
//						wzDetail.add(ZG_DES_THREE) ;
//					}
//				}
				//迭代六个月信息集合
				for(int i=0 ; i<dateForSixMonthList.size() ; i++){
					sixDetail = new ArrayList<String>() ;
					String currYearAndMonth = dateForSixMonthList.get(i) ;
					//TODO
					List<LlglwgForZgwzInfoTablePojo> sixMonthWzCompanyInfoList = llglwgMapper.getSixMonthInfo(currYearAndMonth, prvdId , audTrm) ;
					for(LlglwgForZgwzInfoTablePojo sixMonthWzCompany : sixMonthWzCompanyInfoList){
						if(sixMonthWzCompany.getWgType().equals("zg1")){
							//问题详细描述拼接
							double newPrvdYszsSum = sixMonthWzCompany.getPrvdYszsSum().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
							BigDecimal Zg1per = sixMonthWzCompany.getZg1pe() ;
							double newZg1per = Zg1per.doubleValue() ;
							double lastZg1perDouble = newZg1per * 100 ;
							double lastZg1per = m2Double(lastZg1perDouble) ;
							String ZG_DES_ONE = sixMonthWzCompany.getPrvdName()+"公司疑似违规转售流量总值"+newPrvdYszsSum+"TB且违规转售流量占本公司统付流量总量比例"+lastZg1per+"%" ;
							sixDetail.add(ZG_DES_ONE) ;
						}else if(sixMonthWzCompany.getWgType().equals("zg2")){
							//保留两位小数
							double newOrgYszsllSum = sixMonthWzCompany.getOrgYszsllSum().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ; 
							BigDecimal Zg2pe = sixMonthWzCompany.getZg2pe() ;
							double newZg2pe = Zg2pe.doubleValue() ;
							double lastZg2peDouble = newZg2pe * 100 ;
							double lastZg2pe = m2Double(lastZg2peDouble) ;
							String ZG_DES_TWO = sixMonthWzCompany.getPrvdName()+"公司集团客户“"+sixMonthWzCompany.getOrgNm()+"”疑似违规转售流量"+newOrgYszsllSum+"TB且占当月全集团违规转售流量总量比例"+lastZg2pe+"%" ;
							sixDetail.add(ZG_DES_TWO) ;
						}else if(sixMonthWzCompany.getWgType().equals("zg3")){
							//将审计月去除空格
							String newWgAud = sixMonthWzCompany.getWgAud().trim() ;
							String lastWgAud = newWgAud.replace(",", "、") ;
							String ZG_DES_THREE =sixMonthWzCompany.getPrvdName()+"公司集团客户“"+sixMonthWzCompany.getOrgNm()+"”于审计月"+lastWgAud+"被判定为疑似违规转售流量" ;
							sixDetail.add(ZG_DES_THREE) ;
						}
					}
					if(sixMonthWzCompanyInfoList != null && sixMonthWzCompanyInfoList.size() !=0){
						String sixMonthWzDetail = StringUtils.join(sixDetail , "；") ;
						allwzDetail.add( currYearAndMonth+"，" +sixMonthWzDetail+"。") ;
					}
				}
//				String currMonthWzDetail = StringUtils.join(wzDetail , "；") ;
//				allwzDetail.add(audTrm+"，"+currMonthWzDetail+"。") ;
				String lastDetailInfo = StringUtils.join(allwzDetail , "<br />") ;
				wgCompanyMap.put("wtDetails", lastDetailInfo) ; //问题详细描述
				wgCompanyMapList.add(wgCompanyMap) ;
			}
		}
		return wgCompanyMapList ;
	}
	

	/**
     * <pre>
     * 		@Description  根据集团标识查询当前集团信息及违规审计月
     * 		@param companyId：集团标识，provinceId：集团所在省份，audTrm：审计月
     * 		@initDate：初始化日期—201809
     * 		@return HashMap；
     * 		@author 林春雨
     * </pre>
     */
	public Map<String , Object> getCompanyInfoByCompanyID(String companyId , String provinceId , String audTrm) throws Exception{
		

		 HashMap<String , Object> result = new HashMap<String , Object>() ;
		 if(audTrm == null || "".equals(audTrm)){
			 if(DateUtilsForCurrProject.currStrIsNotDate(audTrm) == false){
	 			 	Date date = new Date() ;
	 			 	SimpleDateFormat sf = new SimpleDateFormat("yyyyMM") ;
	 			 	audTrm = sf.format(date) ;
			 	}
		 }
		 LlglwgMapper llglwgMapper  = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
 		 LlglwgCompanyInfoPojo llglwgCompanyInfoPojo = llglwgMapper.getCompanyInfoByCompanyID(companyId , provinceId , audTrm) ;
 		 if(llglwgCompanyInfoPojo!=null){
	 		 //------------------------【集团客户标识解除空格】--------------------------------------//
	 		 String orgCustId = llglwgCompanyInfoPojo.getOrgCustId() ;
	 		 String newOrgCustId = orgCustId.trim() ;
	 		 llglwgCompanyInfoPojo.setOrgCustId(newOrgCustId);
	 		 //-------------------------【默认集团业务形态为"-"】--------------------------------------//
	 		 String formName = llglwgCompanyInfoPojo.getFormName() ;
	 		 if(formName == null || "".equals(formName)){
	 			 llglwgCompanyInfoPojo.setFormName("-");
	 		 }
	 		 //查询当前公司违规审计月信息
	 		 String prvdId = llglwgCompanyInfoPojo.getPrvdId() ;
	 		 List<String> wgAudTrmList = llglwgMapper.getConpamyWgList(orgCustId , prvdId) ;
	 		 //将当前String集合元素用“，”连接
	 		String wgAudTrm = StringUtils.join(wgAudTrmList,",") ;
	 		llglwgCompanyInfoPojo.setAccountAudTrm(wgAudTrm); 
	 		 Map<String , Object> llglwgCompanyInfoPojoMap = ObjectInterActiveMapUtil.loadingGroupInfo(llglwgCompanyInfoPojo) ;
	 		 result.put("groupInfo", llglwgCompanyInfoPojoMap) ;
 		 }
 		 return result ;
	}

	//重点集团客户
	public ArrayList<HashMap<String, Object>> getFocusOrgCustomerTableForZgWz(String audTrm , String prvdId) throws Exception {
		 ArrayList<HashMap<String, Object>> resultMapList = new ArrayList<HashMap<String, Object>>() ;
		
		 //得出当前月与前三个月月份集合
		 //日期转换
		 Date date = null  ;
		 //当前日期是否为空
		 if(audTrm == null || "".equals(audTrm)){
			return  resultMapList ;
		 }else{
			 //当前日期是否可转换字符串
			 if(DateUtilsForCurrProject.currStrIsNotDate(audTrm) == false){
				date = new Date() ;
				audTrm = DateUtilsForCurrProject.getYearAndMonth(date) ;
			 }
			date = DateUtilsForCurrProject.stringToDate(audTrm) ;
			//获取两个月前的年月
			int twoMathAgo = 2 ;
			String lastDateTwo = DateUtilsForCurrProject.getBeforeAnyMonth(date, twoMathAgo) ;
			//获取一个月前的年月
			int oneMathAgo = 1 ;
			String lastDateOne = DateUtilsForCurrProject.getBeforeAnyMonth(date, oneMathAgo) ;
			//查询集团用户信息
			 LlglwgMapper llglwgMapper  = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
			 ArrayList<LlglwgCompanyInfoPojo> focusGroupList = llglwgMapper.getFocusGroupTable(audTrm, lastDateOne, lastDateTwo , prvdId) ;
			 for (LlglwgCompanyInfoPojo llglwgCompanyInfoPojo : focusGroupList ) {
				HashMap<String , Object> result = new HashMap<String ,Object>() ;
				 result = ObjectInterActiveMapUtil.loadingTableGroupInfo(llglwgCompanyInfoPojo , result , audTrm, lastDateOne, lastDateTwo ) ; 
				 resultMapList.add(result) ;
			 }
		 }
		return resultMapList ;
	}
	
	
	/**
     * <pre>
     * 		@Description  	展示该集团客户自审计月被判定为疑似转售流量的审计月对应数据。
     * 		@param orgCustId：集团标识，audTrm审计月
     * 		@initDate：初始化日期—201809
     * 		@return HashMap；
     * 		@author 林春雨
     * </pre>
     */
	public HashMap<String, Object> getYszsllAudTrmData(String orgCustId , String audTrm) throws ParseException{
		HashMap<String, Object> result = new HashMap<String, Object>() ;
		 //柱状图
		 ArrayList<Double> columnList = new ArrayList<Double>() ;
		 //折线图
		 ArrayList<Double> lineList  = new ArrayList<Double>() ;
		 if(audTrm == null || "".equals(audTrm)){
			 ArrayList<String> dateList = new ArrayList<>() ;
			 result.put("audTrm", dateList) ;
	 		 result.put("columnData", columnList) ;
	 		 result.put("lineData", lineList) ;
		 }else{
			 //TODO
			 LlglwgMapper llglwgMapper  = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
			 //设置起始月
			 String startDate = "201809" ;
			 //获取年月集合
			 ArrayList<String> dateList = DateUtilsForCurrProject.getAllYearAndMonth(startDate ,  audTrm) ;
		
			 for (int i = 0; i < dateList.size(); i++) {
				 columnList.add(0.00) ;
				 lineList.add(0.00) ;
			 }
			 ArrayList<LltfForSumOrgPojo> lfSoList= llglwgMapper.getYszsllAudTrmData(orgCustId , startDate ,  audTrm) ;
	 		 for(LltfForSumOrgPojo lfso : lfSoList){
	 			 //疑似违规转售流量值
	 			 BigDecimal sumStrmCap = lfso.getSumStrmCap() ;
	 			 double sumStrmCapDouble = sumStrmCap.doubleValue() ;
	 			 // 疑似转售流量占该省统付总流量占比（%）
	 			 BigDecimal strmCapPer = lfso.getStrmCapPer()  ;
	 			 double strmCapPerDouble = strmCapPer.doubleValue() ;
	 			 double newStrmCapPerDouble =strmCapPerDouble * 100 ;
	 			 //保留两位小数
	 			double lastStrmCapPerDouble =  m2Double(newStrmCapPerDouble) ;
	 			 //日期
	 			 String pojoDate = lfso.getAudTrm() ;
	 			 //查询当前审计月再月份中的下标位置
	  			 for (int i = 0; i < dateList.size(); i++) {
	 				String dateStr = dateList.get(i) ;
	 				//如果当前日期内有数据
	 				if(dateStr.equals(pojoDate)){
	 					//根据下标修改值
	 					columnList.set(i , sumStrmCapDouble) ;
	 					lineList.set(i, lastStrmCapPerDouble) ;
	 				}
	 			}
	 		 }
	 		 result.put("audTrm", dateList) ;
	 		 result.put("columnData", columnList) ;
	 		 result.put("lineData", lineList) ;
		 }
		 return result;
	}

	
	/**
     * <pre>
     * 		@Description  	展示全国及省份地图联动信息
     * 		@param prvdId：省份标识，audTrm审计月
     * 		@initDate：初始化日期—201809
     * 		@return HashMap；
     * 		@author 林春雨
     * </pre>
     */
	public Map<String, Object> getDataForMapLinkage(String audTrm, String prvdId) {
		 if(audTrm == null || "".equals(audTrm)){
			 if(DateUtilsForCurrProject.currStrIsNotDate(audTrm) == false){
	 			 	Date date = new Date() ;
	 			 	SimpleDateFormat sf = new SimpleDateFormat("yyyyMM") ;
	 			 	audTrm = sf.format(date) ;
			 	}
		 }
		 // TODO Auto-generated method stub
		 //根据时间段获取每个省份数据集合
		// 如果省份为10000，则查询全国的，否则为省市的。
 		Map<String, Object> result = new HashMap<String, Object>();
		if("10000".equals(prvdId)){
	 		 LlglwgMapper llglwgMapper  = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	 		 ArrayList<LlglwgForZgwzPojo> sjzgwzDataAllList = llglwgMapper.getDataForMapLinkageForPrvd(audTrm) ;
	 		 ArrayList<BigDecimal> amountList = new ArrayList<BigDecimal>();
	 		 ArrayList<String> prvdList = new ArrayList<String>();
	 		 //迭代集合装入省份与数据集合
	 		 for (LlglwgForZgwzPojo lfzp : sjzgwzDataAllList) {
	 			prvdList.add(lfzp.getInitPrvdNm());
//	 			BigDecimal bg = lfzp.getInitPrvdData() ;
	 		    amountList.add(lfzp.getInitPrvdData());
			 }
	 		result.put("prvdName", prvdList);
	 		result.put("num", amountList);
		}else{
			//查询地市数据
			 LlglwgMapper llglwgMapper  = mybatisDao.getSqlSession().getMapper(LlglwgMapper.class);
	 		 ArrayList<LlglwgForZgwzPojo> sjzgwzDataAllList = llglwgMapper.getDataForMapLinkageForCity(audTrm, prvdId) ;
	 		 ArrayList<BigDecimal> amountList = new ArrayList<BigDecimal>();
	 		 ArrayList<String> prvdList = new ArrayList<String>();
	 		 //迭代集合装入省份与数据集合
	 		 for (LlglwgForZgwzPojo lfzp : sjzgwzDataAllList) {
	 			prvdList.add(lfzp.getInitCityNm());
	 		    amountList.add(lfzp.getInitCityData());
			 }
	 		result.put("cityName", prvdList);
	 		result.put("num", amountList);
		}
 		return result  ;
	}
	

	/**
	 * 将数据保留两位小数
	 */
    public static double m2Double(double d) {  
   	 	//#.00 表示两位小数
        DecimalFormat df = new DecimalFormat("#0.00");  
        double newD = Double.parseDouble(df.format(d)) ;
        return newD ;
    } 
	
}
