package com.hpe.cmca.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.util.JacksonJsonUtil;
import com.hpe.cmca.web.taglib.Pager;


/**
 * 
 * <pre>
 * Desc： copy from cmccca
 * @author GuoXY
 * @refactor GuoXY
 * @date   Oct 27, 2016 7:33:34 PM
 * @version 1.0
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Oct 27, 2016 	   GuoXY 	         1. Created this class. 
 * </pre>
 */
@Service
public class ConcernService {

	@Autowired
	private MybatisDao mybatisDao = null;

	public static Map<String, Map<String, Object>> concernMap = new HashMap<String, Map<String, Object>>();
	public static Map<String, Map<String, Object>> concernAttrMap = new HashMap<String, Map<String, Object>>();

	/**
	 * <pre>
	 * Desc  查询关注点的子页面信息
	 * @param concernId
	 * @return
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Dec 17, 2014 3:06:49 PM
	 * </pre>
	 */
	public Map<String, Object> selectConcernInfos(int concernId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("concernId", concernId);
		Map<String, Object> result = mybatisDao.get("ConcernMapper.selectConcernInfo", params);
		return result;
	}

	/**
	 * 根据专题Id查询所有关注点
	 * 
	 * <pre>
	 * Desc  
	 * @param subjectId
	 * @return
	 * @author robin.du
	 * @refactor robin.du
	 * @date   Jan 20, 2015 2:38:43 PM
	 * </pre>
	 */
	public List<Map<String, Object>> selectConcernList(int subjectId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);
		List<Map<String, Object>> list = mybatisDao.getList("ConcernMapper.selectConcernList", params);
		return list;
	}

	public List<Map<String, Object>> selectConcernAttrInfos(int concernId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("concernId", concernId);
		List<Map<String, Object>> result = mybatisDao.getList("ConcernMapper.selecConcernAttr", params);
		return result;
	}

	public String getConcernAttr(int concernId, String attrKey) {

		List<Map<String, Object>> result = selectConcernAttrInfos(concernId);
		if (result == null || result.isEmpty()) {
			return "";
		}
		for (Map<String, Object> map : result) {
			String attrName = (String) map.get("attrName");
			if (attrKey.equalsIgnoreCase(attrName)) {
				return (String) map.get("attrValue");
			}
		}
		return "";
	}

	/**
	 * <pre>
	 * Desc  查询关注点的子页面信息
	 * @param concernId
	 * @return
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Dec 17, 2014 3:06:49 PM
	 * </pre>
	 */
	public List<Map<String, Object>> selectPageInfos(Map<String, Object> params) {
		List<Map<String, Object>> resultList = mybatisDao.getList("ConcernMapper.selectPageInfos", params);
		return resultList;
	}

	/**
	 * <pre>
	 * Desc  查询具体子页面信息
	 * @param params
	 * @return
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Dec 29, 2014 4:14:28 PM
	 * </pre>
	 */
	public Map<String, Object> selectSubPageInfo(Map<String, Object> params) {

		Map<String, Object> result = mybatisDao.get("ConcernMapper.selectSubPageInfo", params);
		return result;
	}

	/**
	 * <pre>
	 * Desc  查询子页面数据
	 * @param params
	 * @return
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Dec 29, 2014 4:14:03 PM
	 * </pre>
	 */
	public Map<String, List<Object>> selectPageData(Map<String, Object> params) {

		Map<String, List<Object>> returnMap = new HashMap<String, List<Object>>();

		List<Map<String, Object>> dataList = mybatisDao.getList("ConcernMapper.selectSubPageData", params);
		for (Map<String, Object> dataInfo : dataList) {

			if ("mybatis".equalsIgnoreCase((String) dataInfo.get("dataType"))) {
				// TODO 根据表明查询数据权限
				// String tableName = (String) dataInfo.get("table_name");
				String refName = (String) dataInfo.get("refName");
				String refValue = (String) dataInfo.get("refValue");

				List<Object> resultList = mybatisDao.getList(refValue, params);
				returnMap.put(refName, resultList);
			}
		}

		return returnMap;
	}

	/**
	 * 处理有价卡的映射 1001 到 1009都用1001的接口
	 * 
	 * @param params
	 * @param dataInfo
	 * @param refValue
	 * @return
	 */
	public static String handYJKMapping(Map<String, Object> params, String refValue) {

		if (refValue.startsWith("YJK100") && refValue.indexOf(".") == 7 && refValue.startsWith("YJK1000") == false
		// 有价卡新表里缺少yjk_stat字段，只有1004在用
				&& refValue.startsWith("YJK1004") == false) {
			String focus_cd = refValue.substring(3, 7);
			params.put("focuscd_", focus_cd);

			if (params.containsKey("wgTypeKey")) {
				String wgTypeKey = params.get("wgTypeKey").toString();
				if (wgTypeKey.indexOf(",") > 0) {

					params.remove("wgTypeKey");
					params.remove("wgTypeSTR");
				}
			}

			if (refValue.startsWith("YJK1001") == false) {

				refValue = refValue.substring(0, 6) + "1" + refValue.substring(7);

			}

		}
		// 如果直辖市，就直接用省份ID过滤，去掉地市参数否则VC单边无地市的数据会遗漏。
		if (refValue.equals("YJK1000.tableReport_selectRegion_Detail") && params.get("selProvCodeSTR") != null && params.get("selCityCodeSTR") != null) {
			int prvdid = Integer.parseInt(params.get("selProvCodeSTR").toString().trim());
			if (prvdid <= 10400 && prvdid >= 10100) {
				params.remove("selCityCodeSTR");
			}

		}
		return refValue;
	}

	/**
	 * 处理有价卡的映射 1001 到 1009都用1001的接口
	 * 
	 * @param pager
	 * @param refValue
	 * @return
	 */
	public static String handYJKMapping(Pager pager, String refValue) {
		if (refValue != null && refValue.startsWith("YJK1004") == true) {

			if (pager != null && pager.getParams() != null && pager.getParams().get("vcStatSTR") != null) {
				String statstr = pager.getParams().get("vcStatSTR").toString().trim();

				if ("('-1','-2','0','1','2','3','4')".equals(statstr) || statstr.indexOf("100401") >= 0) {

					pager.getParams().put("vcStatSTR", null);

				} else if (statstr.indexOf("-2") >= 0) {
					pager.getParams().put("vcStatSTR_2", -2);
				}

			}
			return refValue;

		}

		if (refValue.startsWith("YJK100") && refValue.indexOf(".") == 7 && refValue.startsWith("YJK1000") == false
		// 有价卡新表里缺少yjk_stat字段，只有1004在用
				&& refValue.startsWith("YJK1004") == false) {
			String focus_cd = refValue.substring(3, 7);

			pager.setFocuscd_(focus_cd);

			if (pager.getParams().containsKey("wgTypeKey")) {
				String wgTypeKey = pager.getParams().get("wgTypeKey").toString();
				if (wgTypeKey.indexOf(",") > 0) {

					pager.getParams().remove("wgTypeKey");
					// 这个方法需要wgTypeSTR进行区分关注点。
					if (refValue.indexOf("tableReport_selectRegion_Detail") < 0) {

						pager.getParams().remove("wgTypeSTR");
					}
				}
			}

			if (refValue.startsWith("YJK1001") == false) {

				refValue = refValue.substring(0, 6) + "1" + refValue.substring(7);

				pager.setSelectId(refValue);

			}

		} else if (refValue.startsWith("YJK1000") && pager.getParams() != null) {
			if (pager.getParams().size() > 0 && pager.getParams().containsKey("focusCdKey") && pager.getParams().get("focusCdKey") != null) {
				String fk = pager.getParams().get("focusCdKey").toString().trim();

				if (fk.indexOf("1006") >= 0 || fk.indexOf("1007") >= 0 || fk.indexOf("1009") > 0) {

					String[] fks = fk.split(",");
					String fkt = "";
					for (String fk1 : fks) {
						if (fk1.equals("1006") || fk1.equals("1007") || fk1.equals("1009")) {
							continue;
						}

						fkt += fk1 + ",";

					}
					if (fkt.endsWith(",")) {
						fkt = fkt.substring(0, fkt.length() - 1);
					}
					pager.getParams().put("focusCdKey", fkt);

				}

			}

		}

		// 如果直辖市，就直接用省份ID过滤，去掉地市参数否则VC单边无地市的数据会遗漏。
		if (refValue.startsWith("YJK100") && refValue.indexOf(".tableReport_selectRegion_Detail") > 0 && pager.getParams().get("selProvCodeSTR") != null && pager.getParams().get("selCityCodeSTR") != null) {
			String prvdid = pager.getParams().get("selProvCodeSTR").toString().trim();
			if (prvdid.indexOf("10100") > 0 || prvdid.indexOf("10200") > 0 || prvdid.indexOf("10300") > 0 || prvdid.indexOf("10400") > 0) {
				pager.getParams().remove("selCityCodeSTR");
			}

		}

		return refValue;
	}

	// 20160512 新增终端套利namespace处理
	/**
	 * 处理终端逃离的映射
	 * 
	 * @param pager
	 * @param refValue
	 * @return
	 */
	public static String handZDTLMapping(Pager pager, String refValue) {
		if (refValue.startsWith("ZDTL300") && refValue.indexOf(".") == 8 && refValue.startsWith("ZDTL3000") == false && refValue.startsWith("ZDTL3003") == false) {
			String focus_cd = refValue.substring(4, 8);

			pager.setFocuscd_(focus_cd);
			refValue = "ZDTL" + refValue.substring(8);
			pager.setSelectId(refValue);

		}
		return refValue;
	}

	// 20160512 新增终端套利namespace处理
	/**
	 * 处理终端逃离的映射
	 * 
	 * @param params
	 * @param refValue
	 * @return
	 */
	public static String handZDTLMapping(Map<String, Object> params, String refValue) {
		if (refValue.startsWith("ZDTL300") && refValue.indexOf(".") == 8 && refValue.startsWith("ZDTL3000") == false && refValue.startsWith("ZDTL3003") == false) {
			String focus_cd = refValue.substring(4, 8);
			params.put("focuscd_", focus_cd);
			refValue = "ZDTL" + refValue.substring(8);
		}
		return refValue;
	}

	/**
	 * <pre>
	 * Desc 处理分页
	 * 建议所有与页面表格相关查询都采用类似原型的方法----即方法原型为 void xxxxxPager(Pager pager)，通过pager.returnAll参数控制是否需要分页。这样一来可以统计风格，二来方便将来做导出功能。  
	 * @param pager
	 * @author robin.du
	 * @refactor robin.du
	 * @date   Jan 5, 2015 9:07:41 AM
	 * </pre>
	 */
	public void processPager(Pager pager) {
		mybatisDao.processPager(pager);
	}
	
	/**
	 * <pre>
	 * Desc  “统计分析”页面图表所需数据
	 * 返回数据类型需讨论
	 * @param resultList
	 * @return
	 * @author robin.du
	 * @refactor robin.du
	 * @date   Jan 5, 2015 9:24:29 AM
	 * </pre>
	 */
	public Map<String, Object> statReportHC(List<Map<String, Object>> resultList) {

		Map<String, Object> rMap = new HashMap<String, Object>();

		List<Object> categories = new ArrayList<Object>();
		rMap.put("categories", categories);

		List<Map<String, Object>> series = new ArrayList<Map<String, Object>>();
		rMap.put("series", series);

		Map<String, Object> errQtyMap = new HashMap<String, Object>() {

			{
				put("name", "违规有价卡数量");
				put("color", "#00FFCC");
				put("type", "column");
				put("xAxis", 0);
				put("tooltip", new HashMap<String, Object>() {

					{
						put("valueSuffix", "");
					}
				});
				put("data", new ArrayList<Object>());
			}
		};
		series.add(errQtyMap);

		Map<String, Object> errAmountMap = new HashMap<String, Object>() {

			{
				put("name", "违规有价卡金额(元)");
				put("color", "#33CCCC");
				put("type", "column");
				put("xAxis", 0);
				put("tooltip", new HashMap<String, Object>() {

					{
						put("valueSuffix", "");
					}
				});
				put("data", new ArrayList<Object>());
			}
		};
		series.add(errAmountMap);

		Map<String, Object> qtyPercentMap = new HashMap<String, Object>() {

			{
				put("name", "违规有价卡数量占比");
				put("color", "#00FFCC");
				put("type", "spline");
				put("yAxis", 1);
				put("tooltip", new HashMap<String, Object>() {

					{
						put("valueSuffix", "%");
					}
				});
				put("data", new ArrayList<Object>());
			}
		};
		series.add(qtyPercentMap);

		Map<String, Object> amountPercentMap = new HashMap<String, Object>() {

			{
				put("name", "违规有价卡金额占比");
				put("color", "#33CCCC");
				put("type", "spline");
				put("yAxis", 1);
				put("tooltip", new HashMap<String, Object>() {

					{
						put("valueSuffix", "%");
					}
				});
				put("data", new ArrayList<Object>());
			}
		};
		series.add(amountPercentMap);
		int errQty = 0;
		int totalQty = 0;
		long errAmount = 0;
		long totalAmount = 0;

		int errQtyAvg = 0;
		long errAmountAvg = 0;
		double qtyPercentAvg = 0;
		double amtPercentAvg = 0;

		if (resultList != null && resultList.size() > 0) {
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> map = resultList.get(i);
				// 表中存在errQty字段名，统计综合的时候就不能用errQty，使用先的totalErrQty
				Object temp = map.get("errQty");
				if (null == temp) {
					temp = map.get("totalErrQty");
				}
				if (null == temp) {
					temp = map.get("errQtyNew");
				}
				errQty = errQty + Integer.valueOf(temp.toString());

				totalQty = totalQty + Integer.valueOf(map.get("totalQty").toString());
				if (map.get("errAmount") instanceof BigDecimal) {
					errAmount = errAmount + ((BigDecimal) map.get("errAmount")).longValue();
				} else if (map.get("errAmount") instanceof Long) {
					errAmount = errAmount + ((Long) map.get("errAmount")).longValue();
				}

				if (map.get("errAmount") instanceof BigDecimal) {
					totalAmount = totalAmount + ((BigDecimal) map.get("totalAmount")).longValue();
				} else if (map.get("errAmount") instanceof Long) {
					totalAmount = totalAmount + ((Integer) map.get("totalAmount")).longValue();
				}

				categories.add(map.get("statisticalObj"));
				((ArrayList<Object>) errQtyMap.get("data")).add(temp);
				((ArrayList<Object>) errAmountMap.get("data")).add(map.get("errAmount"));
				((ArrayList<Object>) qtyPercentMap.get("data")).add(map.get("qtyPercent"));
				((ArrayList<Object>) amountPercentMap.get("data")).add(map.get("amountPercent"));
			}
			/*
			 * errQtyAvg = errQty / resultList.size(); errAmountAvg = errAmount
			 * / resultList.size();
			 * 
			 * qtyPercentAvg = totalQty == 0 ? 0 : (errQty * 100 / totalQty);
			 * amtPercentAvg = totalAmount == 0 ? 0 : (errAmount * 100 /
			 * totalAmount);
			 */

			errQtyAvg = (new BigDecimal(new Double(errQty) / resultList.size()).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();
			errAmountAvg = (new BigDecimal(new Double(errAmount) / resultList.size()).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();

			qtyPercentAvg = totalQty == 0 ? 0 : (new BigDecimal(new Double(errQty * 100) / totalQty).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();
			amtPercentAvg = totalAmount == 0 ? 0 : (new BigDecimal(new Double(errAmount * 100) / totalAmount).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();

		}
		rMap.put("errQtyAvg", errQtyAvg);
		rMap.put("errAmountAvg", errAmountAvg);
		rMap.put("qtyPercentAvg", qtyPercentAvg);
		rMap.put("amtPercentAvg", amtPercentAvg);

		rMap.put("categories", JacksonJsonUtil.beanToJson(categories));
		rMap.put("series", JacksonJsonUtil.beanToJson(series));

		return rMap;
	}

	/**
	 * <pre>
	 * Desc  
	 * @param params
	 * @return
	 * @author robin.du
	 * @refactor robin.du
	 * @date   Jan 14, 2015 5:39:17 PM
	 * </pre>
	 */
	public String getSelectId(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("ConcernMapper.selectSubPageData", params);
		String selectId = "";
		if (list != null && list.size() > 0) {
			selectId = (String) list.get(0).get("refValue");
		}

		return selectId;
	}

	/**
	 * <pre>
	 * Desc  
	 * @param params
	 * @return
	 * @author robin.du
	 * @refactor robin.du
	 * @date   Jan 15, 2015 9:30:36 AM
	 * </pre>
	 */
	public String getUrl(Map<String, Object> params) {
		List<Map<String, Object>> list = mybatisDao.getList("ConcernMapper.selectSubPageInfo", params);
		String url = "";
		if (list != null && list.size() > 0) {
			url = (String) list.get(0).get("requestUrl");
		}

		return url;
	}

	public List<Map<String, Object>> selectCity(Map<String, Object> params) {
		List<Map<String, Object>> resultList = mybatisDao.getList("commonMapper.selectCityList", params);
		return resultList;
	}

	public List<Map<String, Object>> selectProvince() {
		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = mybatisDao.getList("commonMapper.selectProvinceList", params);
		return resultList;
	}

	public Map<String, Object> selectAudReportFileId(String auditMonthly, String auditSubject, String aduitor, String auditConcern) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("auditMonthly", auditMonthly);
		params.put("auditSubject", auditSubject);
		params.put("aduitor", aduitor);
		params.put("auditConcern", auditConcern);
		Map<String, Object> result = mybatisDao.get("commonMapper.selectAudReportFileId", params);
		return result;
	}

	public Map<String, Object> selectAudDetailFileId(String auditMonthly, String auditSubject, String aduitor, String auditConcern) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("auditMonthly", auditMonthly);
		params.put("auditSubject", auditSubject);
		params.put("aduitor", aduitor);
		params.put("auditConcern", auditConcern);
		Map<String, Object> result = mybatisDao.get("commonMapper.selectAudDetailFileId", params);
		return result;
	}
	
	/**
	 * 将审计报告的数据存入数据库中hppdata.busi_job_report_json
	 * <pre>
	 * Desc  
	 * @param map
	 * @author jh
	 * @refactor jh
	 * @date   2017-5-31 下午3:46:30
	 * </pre>
	 */
	public void insertInto(Map<String, Object> map){
		mybatisDao.add("jobReportMapper.insertInto", map);
	}
	
	public String convert(String param){
		
		StringBuilder sb = new StringBuilder();		
		Integer index=0;
		for(int i=param.indexOf(".")+1;i<param.length();i++){
			String ch = param.substring(i, i+1);
			if("0".equals(ch))
				index++;	
			else 
				break;
		}	
		for(int j=0;j<index+1;j++){//有数字为止后一位
			sb.append("0");
		}

		DecimalFormat df = new DecimalFormat("##0."+sb.toString());
		
		DecimalFormat df2 = new DecimalFormat("##0.00");
		
		Double d = Double.valueOf(param.replace("%",""));
		
		String result = "";
		if(d>=0.01)
			result = df2.format(d);
		else{
			result = df.format(d);
			while(true){
				if("0".equals(result.substring(result.length()-1, result.length())))
					result = result.substring(0,result.length()-1);
				else
					break;
			}

		}
		if(".".equals(result.substring(result.length()-1, result.length()))){
			return result.substring(0, result.length()-1)+"%";
		}
		return result+"%";//df.format(d)+"%";
	}

}
