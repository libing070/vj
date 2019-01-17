/**
 * com.hp.cmcc.service.QDYKService.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.util.JacksonJsonUtil;
import com.hpe.cmca.web.taglib.Pager;


/**
 * <pre>
 * Desc： 审计专题——个人客户长期高额欠费，用于处理该模块的业务逻辑
 * author GuoXY
 * refactor 
 * date   2016-08-02 11:00
 * version 1.0
 * see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2016-08-02 	    GuoXY 	         1. Created this class.
 * </pre>
 */
@Service
public class QF4003Service {

	@Autowired
	private MybatisDao	mybatisDao	= null;

	

	/**
	 * <pre>
	 * @Desc  总体情况_查询地图数据
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   2016-08-02 11:00
	 * </pre>
	 */
	public List<Object> selectPageDataMap(Map<String, Object> params) {
		// ibatis mapper 的 xml配置文件为程序中用到的每一个SQL进行“分门别类、注册标识”，每条SQL以namespace.id的方式进行标识，
		// 如果id确定唯一可以省略namespace，比如下面的语句第一参数可以改成“totalReport_selectChinaMap”       20160731 add commit by GuoXY
		return mybatisDao.getList("customerDebtGEKH.totalReport_selectChinaMap", params);
	}
	
	/**
	 * <pre>
	 * @Desc  总体情况_关注点属性配置信息
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   2016-08-02 11:00
	 * </pre>
	 */
//	public List<Object> selPagDatExpQDYK2002ConcernAttr(Map<String, Object> params) {
//		return mybatisDao.getList("ConcernMapper.selecConcernAttr", params);
//	}

	/**
	 * <pre>
	 * @Desc  总体情况_按数量top5_查询数据
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   2016-08-02 13:00
	 * </pre>
	 */
	public List<Object> selectPageDataNumTop5(Map<String, Object> params) {
		return mybatisDao.getList("customerDebtGEKH.totalReport_selectNumberTop5", params);
	}

	/**
	 * <pre>
	 * @Desc  总体情况_按占比top5_查询数据
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   2016-08-02 13:00
	 * </pre>
	 */
	public List<Object> selectPageDataPerTop5(Map<String, Object> params) {
		return mybatisDao.getList("customerDebtGEKH.totalReport_selectPercentTop5", params);
	}

	/**
	 * <pre>
	 * @Desc 模型说明
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   2016-08-02 13:00
	 * </pre>
	 */
	public List<Object> selectPageDataIntro(Map<String, Object> params) {
		return mybatisDao.getList("ConcernMapper.selectPointReport", params);
	}

	/**
	 * <pre>
	 * @Desc  查询审计报中所有子页面数据
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   2016-08-02 13:00
	 * </pre>
	 */
	public Map<String, List<Object>> selectAuditReportPageData(Map<String, Object> params) {
		Map<String, List<Object>> returnMap = new HashMap<String, List<Object>>();

		String []ref_name = {
							"totalInfo"						/*1：审计报告与结果-总体：SUM_CUST_CHG_PRVD*/
							,"customer_num_company_top5"	/*2：个人客户数量排名前5的省公司/地市*/
							,"money_num_company_top5"		/*3：欠费金额排名前5的省公司/地市*/
							
							,"customerImportantLevel"		/*4：按客户重要等级分-总体：SUM_CUST_TYP*/
							//,"cusDebt_top5_important_level"	/*5：按客户重要等级分-显示按欠费金额top5的省/地市分别显示：SUM_CUST_TYP*/
							
							//,"customerMonthRange"			/*6：按欠费账龄分布（欠费月份区间）-总体：SUM_CUST_CHG_AGE*/
							//,"cusDebt_top5_month_range"		/*7：按欠费账龄分布（欠费月份区间）-显示按欠费金额top5的省/地市分别显示：SUM_CUST_CHG_AGE*/
							
							//,"customerMoneyScope"			/*8：按欠费金额分布（欠费金额区间）-总体：SUM_CUST_CHG_DBT_AMT*/
							//,"cusDebt_top5_money_scope"		/*9：按欠费金额分布（欠费金额区间）-显示按欠费金额top5的省/地市分别显示：SUM_CUST_CHG_DBT_AMT*/
							,"auditReport_indvl_top10"
							//,"customer_percent_company_top5"/*2:个人客户占比排名前5的省公司*/
							//,"money_percent_company_top5"/*4:欠费金额占比排名前5的省公司*/
							//,"customer_num_chnl_top5"/*5:个人客户数量排名前5的渠道*/
							//,"customer_percent_chnl_top5"/*6:个人客户比例排名前5的渠道*/
							//,"shqdChannelInfo"/*审计报告与结果-渠道：SUM_CUST_CHG_CHNL*/
							//,"zyqdChannelInfo"/*审计报告与结果-渠道：SUM_CUST_CHG_CHNL*/
							};
		
		String []ref_value = {
							"customerDebtGEKH.auditReport_select_totalInfo"			/*1：审计报告与结果-总体：SUM_CUST_CHG_PRVD*/
							,"customerDebtGEKH.auditReport_select_cusNumComp_top5"	/*2：个人客户数量排名前5的省公司/地市*/
							,"customerDebtGEKH.auditReport_select_monNumComp_top5"	/*3：欠费金额排名前5的省公司/地市*/

							,"customerDebtGEKH.auditReport_select_totalImportantLevel"	/*4：按客户重要等级分-总体：SUM_CUST_TYP*/
							//,"customerDebtGEKH.auditReport_select_top5ImportantLevel"	/*5：按客户重要等级分-显示按欠费金额top5的省/地市分别显示：SUM_CUST_TYP*/
							
							//,"customerDebtGEKH.auditReport_select_totalMonthRange"		/*6：按欠费账龄分布（欠费月份区间）-总体：SUM_CUST_CHG_AGE*/
							//,"customerDebtGEKH.auditReport_select_top5MonthRange"		/*7：按欠费账龄分布（欠费月份区间）-显示按欠费金额top5的省/地市分别显示：SUM_CUST_CHG_AGE*/
							
							//,"customerDebtGEKH.auditReport_select_totalMoneyScope"		/*8：按欠费金额分布（欠费金额区间）-总体：SUM_CUST_CHG_DBT_AMT*/
							//,"customerDebtGEKH.auditReport_select_top5MoneyScope"		/*9：按欠费金额分布（欠费金额区间）-显示按欠费金额top5的省/地市分别显示：SUM_CUST_CHG_DBT_AMT*/
							,"customerDebtGEKH.auditReport_indvl_top10"
							//,"customerDebtGEKH.auditReport_select_cusPerComp_top5"/*2:个人客户占比排名前5的省公司*/
							//,"customerDebtGEKH.auditReport_select_monPerComp_top5"/*4:个人客户占比排名前5的省公司*/
							//,"customerDebtGEKH.auditReport_select_cusNumChnl_top5"/*5:个人客户数量排名前5的渠道*/
							//,"customerDebtGEKH.auditReport_select_cusPerChnl_top5"/*6:个人客户比例排名前5的渠道*/
							//,"customerDebtGEKH.auditReport_select_shqdChannelInfo"/*审计报告与结果-社会渠道：SUM_CUST_CHG_CHNL*/
							//,"customerDebtGEKH.auditReport_select_zyqdChannelInfo"/*审计报告与结果-自有渠道：SUM_CUST_CHG_CHNL*/
							};
		
		for (int i = 0; i < ref_name.length; i++){
			List<Object> resultList = mybatisDao.getList(ref_value[i], params);
//System.out.println("-------" + ref_name[i] + "::" + resultList);
			returnMap.put(ref_name[i], resultList);
			System.out.println("-------" + ref_name[i] + "::" + resultList);
		}
		
		return returnMap;
	}
	
	
	/**
	 * <pre>
	 * @Desc  访问数据库公共方法
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   2016-08-02 13:00
	 * </pre>
	 */
	public Pager getSubPageDataBySelectId(String selectId, Model uiModel, Pager pager) {
		pager.setSelectId(selectId);
		mybatisDao.processPager(pager);
		return pager;
	}
	
	/**
	 * <pre>
	 * Desc  “统计分析”页面图表所需数据
	 * 返回数据类型需讨论
	 * @param resultList
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   2016-08-02 13:00
	 * </pre>
	 */
	public Map<String, Object> selectPageDataStat(List<Map<String, Object>> resultList) {

		Map<String, Object> rMap = new HashMap<String, Object>();
		List<Object> categories = new ArrayList<Object>();
		rMap.put("categories", categories);

		List<Map<String, Object>> series = new ArrayList<Map<String, Object>>();
		rMap.put("series", series);

		Map<String, Object> errQtyMap = new HashMap<String, Object>() {
			{
				put("name", "长期高额欠费客户数");
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

		Map<String, Object> qtyPercentMap = new HashMap<String, Object>() {
			{
				put("name", "长期高额欠费客户数占比");
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

		int errQty = 0;
		int totalQty = 0;

		int errQtyAvg = 0;
		double qtyPercentAvg = 0;

		if (resultList != null && resultList.size() > 0) {
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> map = resultList.get(i);
//System.out.println("selectPageDataStat:" + map);
				categories.add(map.get("statisticalObj"));
				((ArrayList<Object>) errQtyMap.get("data")).add(map.get("errQty"));
				((ArrayList<Object>) qtyPercentMap.get("data")).add(map.get("qtyPercent"));
				
				errQty = errQty + Integer.valueOf(map.get("errQty").toString());
				totalQty = totalQty + Integer.valueOf(map.get("totalQty").toString());
			}

			errQtyAvg = (new BigDecimal(new Double(errQty)/ resultList.size()).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();
			qtyPercentAvg = totalQty == 0 ? 0 : (new BigDecimal(new Double(errQty * 100) / totalQty).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();
		}
		rMap.put("errQtyAvg", errQtyAvg);
		// 添加一个变量用户显示千分位格式化后的数量字符串，原来的变量不变用于图形的value 20160812 add by GuoXY
		DecimalFormat df=new DecimalFormat(",###,###");
		rMap.put("errQtyAvgFormat", df.format(errQtyAvg));
		rMap.put("qtyPercentAvg", qtyPercentAvg);

		rMap.put("categories", JacksonJsonUtil.beanToJson(categories));
		rMap.put("series", JacksonJsonUtil.beanToJson(series));

		return rMap;
	}

	
	public static void main(String[] args){
		int errQtyAvg = (new BigDecimal(new Double(3)/ 1).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();
		
		System.err.println("errQtyAvg=========" + errQtyAvg) ;
	}

}
