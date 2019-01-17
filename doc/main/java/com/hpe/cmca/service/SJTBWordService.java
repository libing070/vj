package com.hpe.cmca.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.service.ConcernService;

@Service("SJTBWordService")
public class SJTBWordService extends BaseObject {

    @Autowired
    MybatisDao       mybatisDao;
    
	@Autowired
	ConcernService concernService;

    SimpleDateFormat sdf  = new SimpleDateFormat("yyyy年MM月dd日");

    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy年MM月");

    @SuppressWarnings("unchecked")
    public Map<String, Object> getQdykData(String audTrm) throws ParseException {

	Map<String, Object> returnMap = new HashMap<String, Object>();

	returnMap.put("totalNumAll", " ");
	returnMap.put("errNumAll", " ");
	returnMap.put("percentNumAll", " ");
	returnMap.put("momAll", " ");
	returnMap.put("chnlNumAll", " ");
	returnMap.put("percentChnlNumAll", " ");
	returnMap.put("totalNumZiyou", " ");
	returnMap.put("errNumZiyou", " ");
	returnMap.put("percentNumZiyou", " ");
	returnMap.put("momZiyou", " ");
	returnMap.put("chnlNumZiyou", " ");
	returnMap.put("percentChnlNumZiyou", " ");
	returnMap.put("totalNumShehui", " ");
	returnMap.put("errNumShehui", " ");
	returnMap.put("percentNumShehui", " ");
	returnMap.put("momShehui", " ");
	returnMap.put("chnlNumShehui", " ");
	returnMap.put("percentChnlNumShehui", " ");

	returnMap.put("prvdInfoZG1", " ");
	returnMap.put("prvdInfoZG2", " ");
	returnMap.put("prvdInfoZG3", " ");
	returnMap.put("prvdInfoZG4", " ");
	returnMap.put("prvdNameZG1", " ");
	returnMap.put("prvdNameZG2", " ");
	returnMap.put("prvdNameZG3", " ");
	returnMap.put("prvdNameZG4", " ");
	returnMap.put("prvdNameWZYK", " ");
	returnMap.put("audTrmZGYK", " ");
	returnMap.put("audTrmWZYK", " ");
	returnMap.put("audTrm2", " ");
	returnMap.put("audTrm3", " ");
	returnMap.put("audTrm", getMonthByAudtrm(audTrm, -3, sdf3));

	returnMap.put("audTrmZGYK", getLastDayByAudtrm(audTrm, 3));
	returnMap.put("audTrmWZYK", getLastDayByAudtrm(audTrm, 6));
	returnMap.put("audTrm2", getMonthByAudtrm(audTrm, -1, sdf3));
	returnMap.put("audTrm3", getMonthByAudtrm(audTrm, 0, sdf3));

	DecimalFormat df = new DecimalFormat("###,##0.00");
	// 添加整改1参数
	StringBuffer sb = new StringBuffer("");
	StringBuffer sb2 = new StringBuffer("");
	Map<String, Object> qdykMap = new HashMap<String, Object>();
	qdykMap.put("aud_trm", getMonthByAudtrm(audTrm, -3, sdf2));
	List<Map<String, Object>> qdykList1 = mybatisDao.getList("reportModel.selectAuditYktlParams1", qdykMap);
	if (qdykList1 != null && qdykList1.size() > 0) {
	    Map<String, Object> m = qdykList1.get(0);
	    sb.append(m.get("province") + "公司自有渠道疑似养卡号码" + m.get("zyqd_auditUser") + "个，占比"
		    + (m.get("zyqd_auditPercent") == null ? " " : df.format(Double.parseDouble(m.get("zyqd_auditPercent").toString()))) + "%");
	    sb2.append(m.get("province"));
	    for (int i = 1; i < qdykList1.size(); i++) {
		m = qdykList1.get(i);
		// {省名称}公司自有渠道疑似养卡号码{INT}个，占比{DECIMAL}%；
		sb.append("；" + m.get("province") + "公司自有渠道疑似养卡号码" + m.get("zyqd_auditUser") + "个，占比"
			+ (m.get("zyqd_auditPercent") == null ? " " : df.format(Double.parseDouble(m.get("zyqd_auditPercent").toString()))) + "%");
		sb2.append("、" + m.get("province"));
	    }
	    sb.append("。");
	    returnMap.put("prvdInfoZG1", sb.toString());
	    returnMap.put("prvdNameZG1", sb2.toString());
	}
	// 添加整改3参数
	sb.setLength(0);
	sb2.setLength(0);
	List<Map<String, Object>> qdykList3 = mybatisDao.getList("reportModel.selectAuditYktlParams3", qdykMap);
	if (qdykList3 != null && qdykList3.size() > 0) {
	    Map<String, Object> m = qdykList3.get(0);
	    sb.append(m.get("province") + "公司自有渠道“" + m.get("yk1w_qudao") + "”疑似养卡号码" + m.get("yk1w_auditCardno") + "个");
	    sb2.append(m.get("province"));
	    for (int i = 1; i < qdykList3.size(); i++) {
		m = qdykList3.get(i);
		// {省名称}公司自有渠道“{渠道名称}”疑似养卡号码{INT}个
		sb.append("；" + m.get("province") + "公司自有渠道“" + m.get("yk1w_qudao") + "”疑似养卡号码" + m.get("yk1w_auditCardno") + "个");
		sb2.append("、" + m.get("province"));
	    }
	    sb.append("。");
	    returnMap.put("prvdInfoZG3", sb.toString());
	    returnMap.put("prvdNameZG3", sb2.toString());
	}
	// 添加整改2参数
	sb.setLength(0);
	sb2.setLength(0);
	List<Map<String, Object>> qdykList2 = mybatisDao.getList("reportModel.selectAuditYktlParams2", qdykMap);
	if (qdykList2 != null && qdykList2.size() > 0) {
	    Map<String, Object> m = qdykList2.get(0);
	    sb.append(m.get("province") + "公司社会渠道疑似养卡号码" + m.get("shqd_auditUser") + "个，占比"
		    + (m.get("shqd_auditPercent") == null ? " " : df.format(Double.parseDouble(m.get("shqd_auditPercent").toString()))) + "%");
	    sb2.append(m.get("province"));
	    for (int i = 1; i < qdykList2.size(); i++) {
		m = qdykList2.get(i);
		// {省名称}公司自有渠道疑似养卡号码{INT}个，占比{DECIMAL}%；
		sb.append("；" + m.get("province") + "公司社会渠道疑似养卡号码" + m.get("shqd_auditUser") + "个，占比"
			+ (m.get("shqd_auditPercent") == null ? " " : df.format(Double.parseDouble(m.get("shqd_auditPercent").toString()))) + "%");
		sb2.append("、" + m.get("province"));
	    }
	    sb.append("。");
	    returnMap.put("prvdInfoZG2", sb.toString());
	    returnMap.put("prvdNameZG2", sb2.toString());
	}
	// 添加整改4参数
	sb.setLength(0);
	sb2.setLength(0);
	List<Map<String, Object>> qdykList4 = mybatisDao.getList("reportModel.selectAuditYktlParams4", qdykMap);
	if (qdykList4 != null && qdykList4.size() > 0) {
	    Map<String, Object> m = qdykList4.get(0);
	    sb.append(m.get("province") + "公司社会渠道“" + m.get("yk5k_qudao") + "”疑似养卡号码" + m.get("yk5k_auditCardno") + "个");
	    sb2.append(m.get("province"));
	    for (int i = 1; i < qdykList4.size(); i++) {
		m = qdykList4.get(i);
		// {省名称}公司自有渠道“{渠道名称}”疑似养卡号码{INT}个
		sb.append("；" + m.get("province") + "公司社会渠道“" + m.get("yk5k_qudao") + "”疑似养卡号码" + m.get("yk5k_auditCardno") + "个");
		sb2.append("、" + m.get("province"));
	    }
	    sb.append("。");
	    returnMap.put("prvdInfoZG4", sb.toString());
	    returnMap.put("prvdNameZG4", sb2.toString());
	}
	// 添加问责参数
	List<String> prvdList = new ArrayList<String>();
	sb.setLength(0);
	List<Map<String, Object>> qdykList5 = mybatisDao.getList("reportModel.selectAuditYktlParams5", qdykMap);
	if (qdykList5 != null && qdykList5.size() > 0) {
	    Map<String, Object> m = qdykList5.get(0);
	    sb.append(m.get("province"));
	    prvdList.add(m.get("province").toString());
	    for (int i = 1; i < qdykList5.size(); i++) {
		m = qdykList5.get(i);
		if (!prvdList.contains(m.get("province"))) {
		    sb.append("、" + m.get("province"));
		    prvdList.add(m.get("province").toString());
		}
	    }
	    returnMap.put("prvdNameWZYK", sb.toString());
	}

	Map<String, Object> paramsMap = new HashMap<String, Object>();
	paramsMap.put("statCycle", getMonthByAudtrm(audTrm, -3, sdf2));
	paramsMap.put("userCityId", 10000);
	paramsMap.put("provinceCode", 10000);
	Map<String, Object> returnMapTp = mybatisDao.get("QDYK2002.auditReport_selectChnl_all", paramsMap);
	String tpTx = null;

	if (returnMapTp != null && returnMapTp.size() > 0) {
	    Double a = Double.parseDouble(returnMapTp.get("totalQty").toString().replaceAll(",", "")) / 10000;
	    returnMap.put("totalNumAll", df.format(a));

	    Double b = Double.parseDouble(returnMapTp.get("errQty").toString().replaceAll(",", "")) / 10000;
	    returnMap.put("errNumAll", df.format(b));

	    returnMap.put("percentNumAll", returnMapTp.get("qtyPercent"));

	    tpTx = returnMapTp.get("abs_fbQtyPercent").toString();
	    returnMap.put("momAll", "比上期" + tpTx + "个百分点");
	    if (tpTx.contains("0.00")) {
		returnMap.put("momAll", "与上期持平");
	    }

	    returnMap.put("chnlNumAll", returnMapTp.get("errChnlQty"));

	    returnMap.put("percentChnlNumAll", returnMapTp.get("qtyChnlPercent"));

	}

	Map<String, Object> returnMapTp1 = mybatisDao.get("QDYK2002.auditReport_selectChnl_1", paramsMap);
	if (returnMapTp1 != null && returnMapTp1.size() > 0) {
	    Double c = Double.parseDouble(returnMapTp1.get("totalQty_w").toString().replaceAll(",", "")) / 10000;
	    returnMap.put("totalNumZiyou", df.format(c));

	    Double d = Double.parseDouble(returnMapTp1.get("errQty_w").toString().replaceAll(",", "")) / 10000;
	    returnMap.put("errNumZiyou", df.format(d));

	    returnMap.put("percentNumZiyou", returnMapTp1.get("qtyPercent"));

	    tpTx = returnMapTp1.get("abs_fbQtyPercent").toString();
	    returnMap.put("momZiyou", "比上期" + tpTx + "个百分点");
	    if (tpTx.contains("0.00")) {
		returnMap.put("momZiyou", "与上期持平");
	    }

	    returnMap.put("chnlNumZiyou", returnMapTp1.get("errChnlQty_w"));
	    returnMap.put("percentChnlNumZiyou", returnMapTp1.get("qtyChnlPercent"));

	}

	Map<String, Object> returnMapTp2 = mybatisDao.get("QDYK2002.auditReport_selectChnl_2", paramsMap);
	if (returnMapTp2 != null && returnMapTp2.size() > 0) {
	    Double e = Double.parseDouble(returnMapTp2.get("totalQty_w").toString().replaceAll(",", "")) / 10000;
	    returnMap.put("totalNumShehui", df.format(e));

	    Double f = Double.parseDouble(returnMapTp2.get("errQty_w").toString().replaceAll(",", "")) / 10000;
	    returnMap.put("errNumShehui", df.format(f));

	    returnMap.put("percentNumShehui", returnMapTp2.get("qtyPercent"));

	    tpTx = returnMapTp2.get("abs_fbQtyPercent").toString();
	    returnMap.put("momShehui", "比上期" + tpTx + "个百分点");
	    if (tpTx.contains("0.00")) {
		returnMap.put("momShehui", "与上期持平");
	    }

	    returnMap.put("chnlNumShehui", returnMapTp2.get("errChnlQty_w"));
	    returnMap.put("percentChnlNumShehui", returnMapTp2.get("qtyChnlPercent"));

	}

	return returnMap;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getYjkData(String audTrm) {

	Map<String, Object> returnMap = new HashMap<String, Object>();
	returnMap.put("totalAmt", " ");// 将参数初始化，避免出现查询的数据为空时，没有该参数的情形
	returnMap.put("totalNum", " ");
	returnMap.put("errAmt", " ");
	returnMap.put("percentAmt", " ");
	returnMap.put("errNum", " ");
	returnMap.put("percentNum", " ");
	returnMap.put("momNum", " ");
	returnMap.put("momAmt", " ");
	returnMap.put("errAmt1", " ");
	returnMap.put("errAmt2", " ");
	returnMap.put("errAmt3", " ");
	returnMap.put("errAmt4", " ");
	returnMap.put("errAmt5", " ");
	returnMap.put("errNum1", " ");
	returnMap.put("errNum2", " ");
	returnMap.put("errNum3", " ");
	returnMap.put("errNum4", " ");
	returnMap.put("errNum5", " ");
	returnMap.put("prvdInfoZG1", " ");
	returnMap.put("prvdInfoZG2", " ");
	returnMap.put("prvdInfoZG3", " ");
	returnMap.put("prvdNameZG1", " ");
	returnMap.put("prvdNameZG2", " ");
	returnMap.put("prvdNameZG3", " ");
	returnMap.put("prvdNameWZ", " ");
	returnMap.put("audTrmZGYJK", getLastDayByAudtrm(audTrm, 2));
	returnMap.put("audTrmWZYJK", getLastDayByAudtrm(audTrm, 6));
	returnMap.put("audTrm", getMonthByAudtrm(audTrm, 0, sdf3));
	returnMap.put("audTrm1", getMonthByAudtrm(audTrm, -1, sdf3));

	DecimalFormat df = new DecimalFormat("###,##0.00");
	Map<String, Object> paramsMap = new HashMap<String, Object>();
	paramsMap.put("statCycle", getMonthByAudtrm(audTrm, -1, sdf2));
	paramsMap.put("userCityId", 10000);
	paramsMap.put("provinceCode", 10000);
	Map<String, Object> returnMapTp = mybatisDao.get("YJK1000.auditReport_selectTotal", paramsMap);

	String tolAmt = returnMapTp.get("tolAmt") == null ? " " : df.format(Double.parseDouble(returnMapTp.get("tolAmt").toString().replaceAll(",", "")) / 10000);
	returnMap.put("totalAmt", tolAmt);

	String tolQty = returnMapTp.get("tolQty") == null ? " " : df.format(Double.parseDouble(returnMapTp.get("tolQty").toString().replaceAll(",", "")) / 10000);
	returnMap.put("totalNum", tolQty);

	String amountSum = returnMapTp.get("amountSum") == null ? " " : df.format(Double.parseDouble(returnMapTp.get("amountSum").toString().replaceAll(",", "")) / 10000);
	returnMap.put("errAmt", amountSum);

	
	String s1 = concernService.convert(returnMapTp.get("amountPercent").toString());
	returnMap.put("percentAmt", s1);
	
	returnMap.put("errNum", returnMapTp.get("numberSum"));
	
	String s2 = concernService.convert(returnMapTp.get("numberPrecent").toString());
	returnMap.put("percentNum", s2);

	Map<String, Object> paramsMap1 = new HashMap<String, Object>();
	paramsMap1.put("statCycle", getMonthByAudtrm(audTrm, -2, sdf2));
	paramsMap1.put("userCityId", 10000);
	paramsMap1.put("provinceCode", 10000);
	Map<String, Object> returnMapTp1 = mybatisDao.get("YJK1000.auditReport_selectTotal", paramsMap1);

	if ((returnMapTp.get("tolNum") != null && returnMapTp1.get("tolNum") != null)
		&& (!((Double.parseDouble(returnMapTp.get("tolNum").toString()) - 0 < 0.0000001) || (Double.parseDouble(returnMapTp1.get("tolNum").toString()) - 0 < 0.0000001)))) {

	    double a = Double.parseDouble(returnMapTp.get("errNum").toString()) * 1.000 / Double.parseDouble(returnMapTp.get("tolNum").toString());
	    double b = Double.parseDouble(returnMapTp1.get("errNum").toString()) * 1.000 / Double.parseDouble(returnMapTp1.get("tolNum").toString());
	    double c = a - b;
	    String tx = "";
	    if (c > 0)
		tx = "比上月上升" + df.format(c*100) + "个百分点";
	    if (c < 0)
		tx = "比上月下降" + df.format(Math.abs(c*100)) + "个百分点";
	    if (c == 0)
		tx = "与上月持平";
	    returnMap.put("momNum", tx);
	}

	if ((returnMapTp.get("tolMoney") != null && returnMapTp1.get("tolMoney") != null)
		&& (!((Double.parseDouble(returnMapTp.get("tolMoney").toString()) - 0 < 0.0000001) || (Double.parseDouble(returnMapTp1.get("tolMoney").toString()) - 0 < 0.0000001)))) {
	    double a1 = Double.parseDouble(returnMapTp.get("errMoney").toString()) * 1.000 / Double.parseDouble(returnMapTp.get("tolMoney").toString());
	    double b1 = Double.parseDouble(returnMapTp1.get("errMoney").toString()) * 1.000 / Double.parseDouble(returnMapTp1.get("tolMoney").toString());
	    double c1 = a1 - b1;
	    String tx = "";
	    if (c1 > 0)
		tx = "比上月上升" + df.format(c1*100) + "个百分点";
	    if (c1 < 0)
		tx = "比上月下降" + df.format(Math.abs(c1*100)) + "个百分点";
	    if (c1 == 0)
		tx = "与上月持平";
	    returnMap.put("momAmt", tx);
	}

	Map<String, Object> paramsMapConcern = new HashMap<String, Object>();
	paramsMapConcern.put("statCycle", getMonthByAudtrm(audTrm, -1, sdf2));
	paramsMapConcern.put("provinceCode", 10000);
	List<Map<String, Object>> returnList = mybatisDao.getList("YJK1000.auditReport_selectConcerns", paramsMapConcern);
	for (Map<String, Object> tpMap : returnList) {
	    switch (Integer.parseInt(tpMap.get("bizCode").toString())) {
		case 1001:
		    returnMap.put("errAmt1", tpMap.get("errAmt"));
		    returnMap.put("errNum1", tpMap.get("errQty"));
		    break;
		case 1002:
		    Double errAmt2 = Double.parseDouble(tpMap.get("errAmt").toString().replaceAll(",", "")) / 10000;
		    returnMap.put("errAmt2", df.format(errAmt2));
		    returnMap.put("errNum2", tpMap.get("errQty"));
		    break;
		case 1003:
		    Double errAmt3 = Double.parseDouble(tpMap.get("errAmt").toString().replaceAll(",", "")) / 10000;

		    returnMap.put("errAmt3", df.format(errAmt3));
		    returnMap.put("errNum3", tpMap.get("errQty"));
		    break;
		case 1004:
		    Double errAmt4 = Double.parseDouble(tpMap.get("errAmt").toString().replaceAll(",", "")) / 10000;

		    returnMap.put("errAmt4", df.format(errAmt4));
		    returnMap.put("errNum4", tpMap.get("errQty"));
		    break;
		case 1005:
		    Double errAmt5 = Double.parseDouble(tpMap.get("errAmt").toString().replaceAll(",", "")) / 10000;

		    returnMap.put("errAmt5", df.format(errAmt5));
		    returnMap.put("errNum5", tpMap.get("errQty"));
		    break;
	    }
	}

	Map<String, Object> paramsMapZg1 = new HashMap<String, Object>();
	paramsMapZg1.put("audTrm", getMonthByAudtrm(audTrm, -1, sdf2));
	List<Map<String, Object>> returnListZg1 = mybatisDao.getList("reportModel.getParamyjkzg1", paramsMapZg1);
	String txgz1 = "";
	String namez1 = "";
	if (returnListZg1 != null) {
	    for (Map<String, Object> tpMap : returnListZg1) {
		txgz1 = txgz1 + tpMap.get("prvd_name").toString() + "公司有价卡整体违规金额" + tpMap.get("amt").toString() + "万元，占比" + concernService.convert(tpMap.get("per_amt").toString()) + ";";
		namez1 = namez1 + tpMap.get("prvd_name").toString() + "、";
	    }
	    if (txgz1.length() > 0)
		returnMap.put("prvdInfoZG1", txgz1.substring(0, txgz1.length() - 1) + "。");
	    if (namez1.length() > 0)
		returnMap.put("prvdNameZG1", namez1.substring(0, namez1.length() - 1));
	}

	Map<String, Object> paramsMapZg2 = new HashMap<String, Object>();
	paramsMapZg2.put("audTrm", getMonthByAudtrm(audTrm, -1, sdf2));
	List<Map<String, Object>> returnListZg2 = mybatisDao.getList("reportModel.getParamyjkzg2", paramsMapZg2);
	String txgz2 = "";
	String namez2 = "";
	if (returnListZg2 != null) {
	    for (Map<String, Object> tpMap : returnListZg2) {
		txgz2 = txgz2 + tpMap.get("prvd_name").toString() + "公司此类有价卡违规金额" + tpMap.get("amt").toString() + "万元，占本公司审计期间充值有价卡金额的比例" + concernService.convert(tpMap.get("per_amt").toString()) + ";";
		namez2 = namez2 + tpMap.get("prvd_name").toString() + "、";
	    }
	    if (txgz2.length() > 0)
		returnMap.put("prvdInfoZG2", txgz2.substring(0, txgz2.length() - 1) + "。");
	    if (namez2.length() > 0)
		returnMap.put("prvdNameZG2", namez2.substring(0, namez2.length() - 1));
	}

	Map<String, Object> paramsMapZg3 = new HashMap<String, Object>();
	paramsMapZg3.put("audTrm", getMonthByAudtrm(audTrm, -1, sdf2));
	List<Map<String, Object>> returnListZg3 = mybatisDao.getList("reportModel.getParamyjkzg3", paramsMapZg3);
	String txgz3 = "";
	String namez3 = "";
	if (returnListZg3 != null) {
	    for (Map<String, Object> tpMap : returnListZg3) {
		txgz3 = txgz3 + tpMap.get("prvd_name").toString() + "公司此类有价卡违规金额" + tpMap.get("amt").toString() + "万元，占本公司审计期间充值有价卡金额的比例" + concernService.convert(tpMap.get("per_amt").toString()) + ";";
		namez3 = namez3 + tpMap.get("prvd_name").toString() + "、";
	    }
	    if (txgz3.length() > 0)
		returnMap.put("prvdInfoZG3", txgz3.substring(0, txgz3.length() - 1) + "。");
	    if (namez3.length() > 0)
		returnMap.put("prvdNameZG3", namez3.substring(0, namez3.length() - 1));
	}

	Map<String, Object> paramsMapWz = new HashMap<String, Object>();
	paramsMapWz.put("audTrm", getMonthByAudtrm(audTrm, -1, sdf2));
	List<Map<String, Object>> returnListWz = mybatisDao.getList("reportModel.getParamyjkwz", paramsMapWz);
	String nameWz = "";
	if (returnListWz != null) {
	    for (Map<String, Object> tpMap : returnListWz) {
		nameWz = nameWz + tpMap.get("prvd_name").toString() + "、";
	    }
	    if (nameWz.length() > 0)
		returnMap.put("prvdNameWZ", nameWz.substring(0, nameWz.length() - 1));
	}

	// returnMap.put("prvdNameWZ", "");
	return returnMap;
    }

    // 获取终端套利数据 -by hufei 20170615
    @SuppressWarnings("unchecked")
    public Map<String, Object> getZdtlData(String audTrm) {
	String statCycle = getMonthByAudtrm(audTrm, -4, sdf2);
	Map<String, Object> params = new HashMap<String, Object>();
	Map<String, Object> returnMap = new HashMap<String, Object>();
	DecimalFormat df = new DecimalFormat("###,###,##0.00");
	DecimalFormat df1 = new DecimalFormat("###,###,###,##0");
	returnMap.put("errNumAll", " ");
	returnMap.put("totalNumAll", " ");
	returnMap.put("errPercentAll", " ");
	returnMap.put("momErrNum", " ");
	returnMap.put("errChnlNum", " ");
	returnMap.put("errChnlPercent", " ");
	returnMap.put("momErrChnl", " ");
	returnMap.put("totalNumTaoli", " ");
	returnMap.put("totleNumchenmo", " ");
	returnMap.put("totleNumYangji", " ");
	returnMap.put("totalNumChaibao", " ");
	returnMap.put("totalNumKuasheng", " ");
	returnMap.put("aud_trm", " ");
	returnMap.put("aud_trm1", " ");
	returnMap.put("aud_trm2", " ");
	returnMap.put("aud_trm3", " ");
	returnMap.put("prvdInfoZG1", " ");
	returnMap.put("prvdInfoZG2", " ");
	returnMap.put("prvdNameList1", " ");
	returnMap.put("prvdNameList2", " ");
	returnMap.put("prvdNameList3", " ");

	// 查询数据，并转换相关名字
	params.put("userCityId", "10000");
	params.put("provinceCode", "10000");
	params.put("statCycle", statCycle);
	// 总量及异常销售数量占比 --复用的审计报告查询SQL，并将查结果进行拼接入返回map;
	Map<String, Object> totalResult = mybatisDao.get("ZDTL3000.auditReport_selectChnl_2", params);
	if (totalResult != null) {
	    returnMap.put("errNumAll", df.format(Double.parseDouble(totalResult.get("errQty").toString().replaceAll(",", "")) / 10000.00));
	    returnMap.put("totalNumAll", df.format(Double.parseDouble(totalResult.get("totalQty").toString().replaceAll(",", "")) / 10000.00));
	    returnMap.put("errPercentAll", totalResult.get("qtyPercent"));
	    returnMap.put("momErrNum", totalResult.get("abs_fbQtyPercent"));
	    returnMap.put("errChnlNum", df.format(Double.parseDouble(totalResult.get("errChnlQty").toString().replaceAll(",", "")) / 10000.00));
	    returnMap.put("errChnlPercent", totalResult.get("qtyChnlPercent"));
	    returnMap.put("momErrChnl", totalResult.get("abs_fbChnlPercent"));
	}

	// 套利终端相关及渠道
	Map<String, String> taotliTotalResult = mybatisDao.get("ZDTL3000.auditReport_selectChnl_2cj", params);
	if (taotliTotalResult != null) {
	    returnMap.put("totalNumTaoli", df.format(Double.parseDouble(taotliTotalResult.get("errQty").toString().replaceAll(",", "")) / 10000.00));
	}

	// 分关注点
	Map<String, Object> focusResult = mybatisDao.get("ZDTL3000.auditReport_selectFocusCount", params);
	if (focusResult != null) {
	    returnMap.put("totleNumchenmo", df.format((Integer) focusResult.get("errQty_3001") / 10000.00));
	    returnMap.put("totleNumYangji", df.format((Integer) focusResult.get("errQty_3002") / 10000.00));
	    returnMap.put("totalNumChaibao", df.format((Integer) focusResult.get("errQty_3004") / 10000.00));
	    returnMap.put("totalNumKuasheng", df.format((Integer) focusResult.get("errQty_3005") / 10000.00));
	}
	// 待确认点：养机使用的数据为‘ZDTL3000.auditReport_selectChnl_2’中的查询结果
	// Map<String,String> taotliFocusResult=mybatisDao.get("ZDTL3000.auditReport_selectChnl_2", params);

	// 日期时间处理

	// aud_trm1=gc.toString();
	try {
	    returnMap.put("aud_trm", sdf3.format(sdf2.parse(statCycle)));
	    returnMap.put("aud_trm1", sdf3.format(sdf2.parse(audTrm)));
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    logger.error(e.getMessage(), e);
	}
	// gc.add(2, 3);
	returnMap.put("aud_trm2", getLastDayByAudtrm(statCycle, 7));
	// gc.add(2, 3);
	returnMap.put("aud_trm3", getLastDayByAudtrm(statCycle, 10));

	// 整改公司情况1
	List<Map<String, Object>> ZhenggaiComp1Results = mybatisDao.getList("reportModel.getZDTLZhenggaiComp1", params);
	StringBuffer strComp1 = new StringBuffer();
	if (ZhenggaiComp1Results != null) {
	    for (int i = 0, resultSize = ZhenggaiComp1Results.size() - 1; i <= resultSize; i++) {
		// {省名称}公司社会渠道异常销售终端{INT}台，占比{DECIMAL}%；
		Map<String, Object> mapPrvd = ZhenggaiComp1Results.get(i);
		if (i == resultSize) {
		    strComp1.append(mapPrvd.get("CMCC_prov_prvd_nm").toString() + "公司社会渠道异常销售终端" + mapPrvd.get("weigui_num") + "台，占比" + mapPrvd.get("weigui_percent").toString() + "%。");
		} else {
		    strComp1.append(mapPrvd.get("CMCC_prov_prvd_nm").toString() + "公司社会渠道异常销售终端" + mapPrvd.get("weigui_num") + "台，占比" + mapPrvd.get("weigui_percent").toString() + "%;");
		}
	    }
	}

	returnMap.put("prvdInfoZG1", strComp1.toString());

	// 整改公司情况2
	List<Map<String, Object>> ZhenggaiComp2Results = new ArrayList<Map<String, Object>>();
	ZhenggaiComp2Results = mybatisDao.getList("reportModel.getZDTLZhenggaiComp2", params);
	StringBuffer strComp2 = new StringBuffer();
	for (int i = 0; i < ZhenggaiComp2Results.size(); i++) {
	    // {省名称}公司“{渠道名称}”异常销售终端{INT}台，占比{DECIMAL}%；
	    Map<String, Object> mapPrvd = ZhenggaiComp2Results.get(i);
	    if (!mapPrvd.isEmpty()) {
		if (i == 0) {
		    // ((Integer) mapPrvd.get("infraction_num")).toString()
		    strComp2.append("其中" + mapPrvd.get("CMCC_prov_prvd_nm").toString() + "公司“" + mapPrvd.get("chnl_name").toString() + "”异常销售终端" + df1.format(mapPrvd.get("infraction_num")) + "台，占比"
			    + mapPrvd.get("weigui_percent").toString() + "%。");
		} else if (i == ZhenggaiComp2Results.size() - 1) {
		    strComp2.append(mapPrvd.get("CMCC_prov_prvd_nm").toString() + "公司“" + mapPrvd.get("chnl_name").toString() + "”异常销售终端" + df1.format(mapPrvd.get("infraction_num")) + "台，占比"
			    + mapPrvd.get("weigui_percent").toString() + "%。");
		} else {
		    strComp2.append(mapPrvd.get("CMCC_prov_prvd_nm").toString() + "公司“" + mapPrvd.get("chnl_name").toString() + "”异常销售终端" + df1.format(mapPrvd.get("infraction_num")) + "台，占比"
			    + mapPrvd.get("weigui_percent").toString() + "%；");
		}

	    }

	}
	returnMap.put("prvdInfoZG2", strComp2.toString());

	// 整改省名称1
	List<Map<String, Object>> ZhenggaiPrvd1Results = mybatisDao.getList("reportModel.getZDTLZhenggaiPrvd1", params);
	StringBuffer strPrvd1 = new StringBuffer();
	for (int i = 0, resultSize = ZhenggaiPrvd1Results.size() - 1; i <= resultSize; i++) {
	    Map<String, Object> mapPrvd = ZhenggaiPrvd1Results.get(i);
	    if (!mapPrvd.isEmpty()) {
		if (i == resultSize) {
		    strPrvd1.append(mapPrvd.get("CMCC_prov_prvd_nm").toString());
		} else {
		    strPrvd1.append(mapPrvd.get("CMCC_prov_prvd_nm").toString() + "、");
		}
	    }
	}
	if (ZhenggaiPrvd1Results.size() > 0) {
	    returnMap.put("prvdNameList1", strPrvd1.toString());
	}

	// 整改省名称2
	List<Map<String, Object>> ZhenggaiPrvd2Results = mybatisDao.getList("reportModel.getZDTLZhenggaiPrvd2", params);
	StringBuffer strPrvd2 = new StringBuffer();
	for (int i = 0, resultSize = ZhenggaiPrvd2Results.size() - 1; i <= resultSize; i++) {
	    Map<String, Object> mapPrvd = ZhenggaiPrvd2Results.get(i);
	    if (!mapPrvd.isEmpty()) {
		if (i == resultSize) {
		    strPrvd2.append(mapPrvd.get("CMCC_prov_prvd_nm").toString());
		} else {
		    strPrvd2.append(mapPrvd.get("CMCC_prov_prvd_nm").toString() + "、");
		}

	    }
	}
	if (ZhenggaiPrvd2Results.size() > 0) {
	    returnMap.put("prvdNameList2", strPrvd2.toString());
	}

	// 问责省份处理
	List<Map<String, Object>> wenzePrvdResults = mybatisDao.getList("reportModel.getZDTLWenzePrvd", params);
	StringBuffer strPrvd3 = new StringBuffer();
	for (int i = 0, resultSize = wenzePrvdResults.size() - 1; i <= resultSize; i++) {
	    Map<String, Object> mapPrvd = wenzePrvdResults.get(i);
	    if (!mapPrvd.isEmpty()) {
		if (i == resultSize) {
		    strPrvd3.append(mapPrvd.get("CMCC_prov_prvd_nm").toString());
		} else {
		    strPrvd3.append(mapPrvd.get("CMCC_prov_prvd_nm").toString() + "、");
		}

	    }
	}
	returnMap.put("prvdNameList3", strPrvd3);
	return returnMap;
    }

    // 获取客户欠费数据 -by hufei 20170619
    @SuppressWarnings("unchecked")
    public Map<String, Object> getKhqfData(String audTrm) {
	String statCycle = getMonthByAudtrm(audTrm, -1, sdf2);
	Map<String, Object> returnMap = new HashMap<String, Object>();
	Map<String, Object> params = new HashMap<String, Object>();
	DecimalFormat df = new DecimalFormat("###,###,##0.00");
	DecimalFormat df1 = new DecimalFormat("###,###,###,##0");
	params.put("provinceCode", "10000");
	params.put("statCycle", statCycle);
	// reportModel.getKhqfZGPrvdName prvd_name
	List<Map<String, Object>> prvdNameList = mybatisDao.getList("reportModel.getKhqfZGPrvdName", params);
	StringBuffer prvdNameStrBuf = new StringBuffer();
	if (prvdNameList != null && prvdNameList.size() > 0) {
	    for (Map map : prvdNameList) {
		prvdNameStrBuf.append(map.get("prvd_name"));
		prvdNameStrBuf.append("、");
	    }
	    prvdNameStrBuf.deleteCharAt(prvdNameStrBuf.lastIndexOf("、"));
	}
	returnMap.put("prvdNameStrBuf", prvdNameStrBuf);
	
	statCycle = getMonthByAudtrm(statCycle, -7, sdf2);
	params.put("statCycle", statCycle);
	// 日期时间处理
	try {
	    returnMap.put("aud_trm", sdf3.format(sdf2.parse(getMonthByAudtrm(audTrm, -1, sdf2))));
	    returnMap.put("aud_trm1", sdf3.format(sdf2.parse(audTrm)));
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    logger.error(e.getMessage(), e);
	}
	returnMap.put("aud_trm2", getLastDayByAudtrm(audTrm, 1));

	// Start-复用审计报告查询SQL，并将查询结果拼接返回map
	// 个人客户长期高额欠费情况-第一段数据源
	Map<String, Object> resultMap1 = mybatisDao.get("customerDebtGEKH.auditReport_select_totalInfo", params);
	if (resultMap1 == null) {
	    returnMap.put("totalNumErr", " ");
	    returnMap.put("totalAmount", " ");
	    returnMap.put("reduceNumErr", " 与上期基本持平");
	    returnMap.put("momAmountNum", "与上期基本持平");
	    returnMap.put("newUser", " ");
	    returnMap.put("newAmount", " ");
	    returnMap.put("oldUser", " ");
	    returnMap.put("oldAmount", " ");
	} else {
	    int totalNumErr = (resultMap1.get("errQty") == null) ? 0 : Integer.parseInt(resultMap1.get("errQty").toString().replaceAll(",", ""));
	    Double totalAmount = (resultMap1.get("errAmtW") == null) ? 0 : Double.parseDouble(resultMap1.get("errAmtW").toString().replaceAll(",", ""));
	    int reduceNumErr = (resultMap1.get("subs_num") == null) ? 0 : Integer.parseInt(resultMap1.get("subs_num").toString().replaceAll(",", ""));
	    Double momAmountNum = (resultMap1.get("amt_numW") == null) ? 0 : Double.parseDouble(resultMap1.get("amt_numW").toString().replaceAll(",", ""));
	    int newUser = (resultMap1.get("subs_new") == null) ? 0 : Integer.parseInt(resultMap1.get("subs_new").toString().replaceAll(",", ""));
	    Double newAmount = (resultMap1.get("amt_newW") == null) ? 0 : Double.parseDouble(resultMap1.get("amt_newW").toString().replaceAll(",", ""));
	    int oldUser = totalNumErr - newUser;
	    Double oldAmount = totalAmount - newAmount;
	    String isReduceAmount = resultMap1.get("amt_num_flag").toString();
	    String isReduceNum = resultMap1.get("subs_num_flag").toString();

	    returnMap.put("totalNumErr", df1.format(totalNumErr));
	    returnMap.put("totalAmount", df.format(totalAmount));
	    returnMap.put("reduceNumErr", "比上期" + isReduceNum + df1.format(reduceNumErr) + "户");
	    returnMap.put("momAmountNum", "比上期" + isReduceAmount + df.format(momAmountNum) + "万元");
	    returnMap.put("newUser", df1.format(newUser));
	    returnMap.put("newAmount", df.format(newAmount));
	    returnMap.put("oldUser", df1.format(oldUser));
	    returnMap.put("oldAmount", df.format(oldAmount));
	}

	// 个人客户长期高额欠费情况-第二段数据源
	returnMap.put("redUser", " ");
	returnMap.put("redAmount", " ");
	returnMap.put("redPercent", " ");

	Map<String, Object> resultMap2 = mybatisDao.get("customerDebtGEKH.auditReport_select_totalImportantLevel", params);
	if (resultMap2 != null) {
	    if (resultMap2.get("errQty2") != null) {
		returnMap.put("redUser", resultMap2.get("errQty2"));
	    }
	    if (resultMap2.get("errAmt2W") != null) {
		returnMap.put("redAmount", resultMap2.get("errAmt2W"));
	    }
	    if (resultMap2.get("amountPercent2") != null) {
		returnMap.put("redPercent", resultMap2.get("amountPercent2"));
	    }
	}

	// 获取个人-新增欠费金额排名前五的公司
	String[] keyArray = { "regionName", "errAmt10000" };
	String[] returnKeyArray = { "prvdNameTop5", "prvdAmtTop5" };
	returnMap = getKhqfPrvdTop5(returnMap, "customerDebtGEKH.auditReport_select_newAmtComp_top5", params, keyArray, returnKeyArray);

	// 获取个人-新增欠费增幅排名前五的公司
	String[] keyPerArray = { "regionName", "errPer" };
	String[] returnKeyPerArray = { "prvdNamePerTop5", "prvdAmtPerTop5" };
	returnMap = getKhqfPrvdTop5(returnMap, "customerDebtGEKH.auditReport_select_newAmtPerComp_top5", params, keyPerArray, returnKeyPerArray);

	// 获取个人-本期缴清数据查询
	// OrgCustCHG.auditReport_newamtPer_top5 regionName errPer
	String[] keyPaidArray = { "amtAllRecover", "sumSubsRecover", "amtRecoverPer" };
	String[] returnKeyPaidArray = { "amtRecover", "sumSubsRecover", "amtRecoverPer" };
	returnMap = getKhqfPaid(returnMap, "reportModel.getKhqfPaid", params, keyPaidArray, returnKeyPaidArray);

	// // Start-整改查询SQL，并将查询结果拼接返回map
	// // 个人欠费省公司top5
	// List<Map<String, Object>> prvdResultList = mybatisDao.getList("reportModel.getKHQFPrvd", params);
	// StringBuffer prvdInfoStr = new StringBuffer();
	// StringBuffer prvdNameStr = new StringBuffer();
	// for (int i = 0, resultSize = prvdResultList.size() - 1; i <= resultSize; i++) {
	// Map<String, Object> resultMap = prvdResultList.get(i);
	// if (!resultMap.isEmpty()) {
	// // {省名称}公司新增用户欠费{DECIMAL}万元
	// if (resultMap.get("amt_new") != null) {
	// prvdInfoStr.append("," + resultMap.get("CMCC_prov_prvd_nm").toString() + "公司新增用户欠费" + df.format(Double.parseDouble(resultMap.get("amt_new").toString()) / 10000.00) + "万元");
	// }
	// if (i == resultSize) {
	// prvdNameStr.append(resultMap.get("CMCC_prov_prvd_nm").toString() + "公司");
	// } else {
	// prvdNameStr.append(resultMap.get("CMCC_prov_prvd_nm").toString() + "公司、");
	// }
	// }
	// }
	// returnMap.put("prvdInfoZG1", prvdInfoStr);
	// returnMap.put("prvdNameList1", prvdNameStr);
	statCycle = getMonthByAudtrm(statCycle, -12, sdf2);
	params.put("statCycle", statCycle);
	// 集团客户长期高额欠费情况
	// OrgCustCHG.auditReport_all
	Map<String, Object> orgResultMap = mybatisDao.get("OrgCustCHG.auditReport_all", params);

	if (orgResultMap == null) {
	    returnMap.put("totalOrgNumErr", " ");
	    returnMap.put("totalOrgAmount", " ");
	    returnMap.put("newUserOrg", " ");
	    returnMap.put("newAmountOrg", " ");
	    returnMap.put("oldUserOrg", " ");
	    returnMap.put("oldAmountOrg", " ");
	    returnMap.put("momNumOrg", "与上期基本持平");
	    returnMap.put("momAmountOrg", "与上期基本持平");
	} else {
	    int totalOrgNumErr = (orgResultMap.get("infraction_num") == null) ? 0 : Integer.parseInt(orgResultMap.get("infraction_num").toString().replaceAll(",", ""));
	    Double totalOrgAmount = (orgResultMap.get("infraction_amtW") == null) ? 0 : Double.parseDouble(orgResultMap.get("infraction_amtW").toString().replaceAll(",", ""));
	    int newUserOrg = (orgResultMap.get("subs_new") == null) ? 0 : Integer.parseInt(orgResultMap.get("subs_new").toString().replaceAll(",", ""));
	    Double newAmountOrg = (orgResultMap.get("amt_newW") == null) ? 0 : Double.parseDouble(orgResultMap.get("amt_newW").toString().replaceAll(",", ""));
	    int oldUserOrg = totalOrgNumErr - newUserOrg;
	    Double oldAmountOrg = totalOrgAmount - newAmountOrg;

	    int reduceNumErr = (orgResultMap.get("num_dif") == null) ? 0 : Integer.parseInt(orgResultMap.get("num_dif").toString().replaceAll(",", ""));
	    Double momAmountNum = (orgResultMap.get("amt_difW") == null) ? 0 : Double.parseDouble(orgResultMap.get("amt_difW").toString().replaceAll(",", ""));
	    String isReduceAmount = orgResultMap.get("amt_dif_flag").toString();
	    String isReduceNum = orgResultMap.get("num_dif_flag").toString();

	    returnMap.put("momNumOrg", "比上期" + isReduceNum + df1.format(reduceNumErr) + "户");
	    returnMap.put("momAmountOrg", "比上期" + isReduceAmount + df.format(momAmountNum) + "万元");

	    returnMap.put("totalOrgNumErr", df1.format(totalOrgNumErr));
	    returnMap.put("totalOrgAmount", df.format(totalOrgAmount));
	    returnMap.put("newUserOrg", df1.format(newUserOrg));
	    returnMap.put("newAmountOrg", df.format(newAmountOrg));
	    returnMap.put("oldUserOrg", df1.format(oldUserOrg));
	    returnMap.put("oldAmountOrg", df.format(oldAmountOrg));
	}

	// OrgCustCHG.auditReport_newamt_top5 regionName errAmt10000
	// 获取集团-新增用户长期高额欠费金额排名前5的省公司top5
	String[] keyOrgArray = { "regionName", "errAmt10000" };
	String[] returnKeyOrgArray = { "prvdNameOrgTop5", "prvdAmtOrgTop5" };
	returnMap = getKhqfPrvdTop5(returnMap, "OrgCustCHG.auditReport_newamt_top5", params, keyOrgArray, returnKeyOrgArray);

	// 获取集团-新增用户长期高额欠费金额占上期长期高额欠费金额的比例名前5的省公司top5
	// OrgCustCHG.auditReport_newamtPer_top5 regionName errPer
	String[] keyPerOrgArray = { "regionName", "errPer" };
	String[] returnKeyPerOrgArray = { "prvdNamePerOrgTop5", "prvdAmtPerOrgTop5" };
	returnMap = getKhqfPrvdTop5(returnMap, "OrgCustCHG.auditReport_newamtPer_top5", params, keyPerOrgArray, returnKeyPerOrgArray);
	
	// reportModel.getKhqfPaidOrg amtAllRecover sumSubsRecover amtRecoverPer
	// 获取集团-本期缴清数据查询
	String[] keyPaidOrgArray = { "amtAllRecover", "sumSubsRecover", "amtRecoverPer" };
	String[] returnKeyPaidOrgArray = { "amtRecoverOrg", "sumSubsRecoverOrg", "amtRecoverPerOrg" };
	returnMap = getKhqfPaid(returnMap, "reportModel.getKhqfPaidOrg", params, keyPaidOrgArray, returnKeyPaidOrgArray);

	// // End-复用审计报告查询SQL，并将查询结果拼接返回map
	//
	// returnMap.put("prvdInfoZG1", " ");
	// returnMap.put("prvdNameList1", " ");
	// returnMap.put("prvdInfoZG2", " ");
	// returnMap.put("prvdNameList2", " ");
	//
	// // 集团欠费省公司top5
	// List<Map<String, Object>> OrgPrvdResultList = mybatisDao.getList("reportModel.getKHQFOrgPrvd", params);
	// StringBuffer orgPrvdInfoStr = new StringBuffer();
	// StringBuffer orgPrvdNameStr = new StringBuffer();
	// for (int i = 0, resultSize = OrgPrvdResultList.size() - 1; i <= resultSize; i++) {
	// Map<String, Object> resultMap = OrgPrvdResultList.get(i);
	// if (!resultMap.isEmpty()) {
	// // {省名称}公司新增用户欠费{DECIMAL}万元
	// if (resultMap.get("amt_new") != null) {
	// orgPrvdInfoStr.append("," + resultMap.get("CMCC_prov_prvd_nm").toString() + "公司新增用户欠费" + df.format(Double.parseDouble(resultMap.get("amt_new").toString()) / 10000.00) + "万元");
	// }
	// if (i == resultSize) {
	// orgPrvdNameStr.append(resultMap.get("CMCC_prov_prvd_nm").toString() + "公司");
	// } else {
	// orgPrvdNameStr.append(resultMap.get("CMCC_prov_prvd_nm").toString() + "公司、");
	// }
	// }
	// }
	// returnMap.put("prvdInfoZG2", orgPrvdInfoStr);
	// returnMap.put("prvdNameList2", orgPrvdNameStr);

	// End-整改查询SQL，并将查询结果拼接返回map

	return returnMap;
    }

    /**
     * <pre>
     * Desc  获取客户欠费省公司Top5信息
     * @param returnMap 返回的结果表，包含Top5拼接后的数据；
     * @param params 查询参数
     * @param selectId 查询ID
     * @param keyArray map参数值列表
     * @param returnKeyArray 返回的Map中的key值列表
     * @return
     * @author hufei
     * 2017-11-1 下午4:40:40
     * </pre>
     */
    public Map<String, Object> getKhqfPrvdTop5(Map<String, Object> returnMap, String selectId, Map<String, Object> params, String[] keyArray, String[] returnKeyArray) {
	List<Map<String, Object>> top5List = mybatisDao.getList(selectId, params);
	StringBuffer result1 = new StringBuffer(" ");
	StringBuffer result2 = new StringBuffer(" ");
	returnMap.put(returnKeyArray[0], " ");
	returnMap.put(returnKeyArray[1], " ");

	if (top5List != null && top5List.size() > 0) {
	    for (Map map : top5List) {
		result1.append(map.get(keyArray[0]));
		result1.append("、");
		result2.append(map.get(keyArray[1]));
		result2.append("、");
	    }
	    result1.deleteCharAt(result1.lastIndexOf("、"));
	    result2.deleteCharAt(result2.lastIndexOf("、"));
	    returnMap.put(returnKeyArray[0], result1);
	    returnMap.put(returnKeyArray[1], result2);
	}

	return returnMap;
    }

    /**
     * 
     * <pre>
     * Desc  
     * @param returnMap
     * @param selectId
     * @param params
     * @param keyArray
     * @param returnKeyArray
     * @return
     * @author hufei
     * 2017-11-2 下午6:01:47
     * </pre>
     */
    public Map<String, Object> getKhqfPaid(Map<String, Object> returnMap, String selectId, Map<String, Object> params, String[] keyArray, String[] returnKeyArray) {
	List<Map<String, Object>> khqfPaid = mybatisDao.getList(selectId, params);
	StringBuffer result1 = new StringBuffer();
	StringBuffer result2 = new StringBuffer();
	StringBuffer result3 = new StringBuffer();
	returnMap.put(returnKeyArray[0], " ");
	returnMap.put(returnKeyArray[1], " ");
	returnMap.put(returnKeyArray[2], " ");

	if (khqfPaid != null && khqfPaid.size() > 0) {
	    if(khqfPaid.get(0).get(keyArray[0])!=null){
		result1.append(khqfPaid.get(0).get(keyArray[0]));
		returnMap.put(returnKeyArray[0], result1);
	    }
	    if(khqfPaid.get(0).get(keyArray[1])!=null){
		result2.append(khqfPaid.get(0).get(keyArray[1]));
		returnMap.put(returnKeyArray[1], result2);
	    }
	    if(khqfPaid.get(0).get(keyArray[2])!=null){
		result3.append(khqfPaid.get(0).get(keyArray[2]));
		returnMap.put(returnKeyArray[2], result3);
	    }
	}
	return returnMap;

    }

    public String getModel(int subjectId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("subjectId", subjectId);

	Map<String, Object> contentMap = mybatisDao.get("reportModel.getModelContent", params);
	return contentMap.get("report_content").toString();

    }

    /**
     * 
     * <pre>
     * Desc  
     * @param audtrm  审计月
     * @param months  增加/减少的月份
     * @return
     * @throws ParseException
     * @author issuser
     * 2017-6-28 下午5:45:07
     * </pre>
     */
    public String getLastDayByAudtrm(String audtrm, int months) {
	Calendar c = Calendar.getInstance();
	try {
	    c.setTime(sdf2.parse(audtrm));
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    logger.error(e.getMessage(), e);
	}
	c.add(Calendar.MONTH, months + 1);
	c.set(Calendar.DAY_OF_MONTH, 0);
	String s = sdf.format(c.getTime());
	return s;
    }

    public String getMonthByAudtrm(String audtrm, int months, SimpleDateFormat sdf) {
	Calendar c = Calendar.getInstance();

	try {
	    c.setTime(sdf2.parse(audtrm));
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    logger.error(e.getMessage(), e);
	}
	c.add(Calendar.MONTH, months);
	String s = sdf.format(c.getTime());
	return s;
    }

}
