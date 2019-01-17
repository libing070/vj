package com.hpe.cmca.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.YgycMapper;
import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.pojo.YgycData;

/**
 * 
 * <pre>
 * Desc： 
 * @author   issuser
 * @refactor issuser
 * @date     2017-7-12 下午2:50:53
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-7-12 	   issuser 	         1. Created this class.
 * </pre>
 */
@Service("ygycService")
public class YgycService extends BaseObject {

    @Autowired
    protected MybatisDao mybatisDao;

    public Map<String, Object> getYgycEmployeeQty(ParameterData qdykdata) {
	List<Long> numList = new ArrayList<Long>();
	List<String> prvdList = new ArrayList<String>();
	YgycMapper qdykMapper = mybatisDao.getSqlSession().getMapper(YgycMapper.class);
	List<YgycData> dataList = qdykMapper.getYgycEmployeeQty(qdykdata);
	for (YgycData qdyk : dataList) {
	    numList.add(qdyk.getEmployeeQty());
	    prvdList.add(qdyk.getPrvdName());
	}
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("numList", numList);
	data.put("prvdList", prvdList);
	return data;

    }

    public Map<String, Object> getYgycPresentAmount(ParameterData qdykdata) {
	List<BigDecimal> amtList = new ArrayList<BigDecimal>();
	List<String> prvdList = new ArrayList<String>();
	YgycMapper qdykMapper = mybatisDao.getSqlSession().getMapper(YgycMapper.class);
	List<YgycData> dataList = qdykMapper.getYgycPresentAmount(qdykdata);
	for (YgycData qdyk : dataList) {
	    amtList.add(qdyk.getTotalAmt());
	    prvdList.add(qdyk.getPrvdName());
	}
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("amtList", amtList);
	data.put("prvdList", prvdList);
	return data;

    }

    public Map<String, Object> getYgycEmployeeQtyLine(ParameterData qdykdata) {
	List<Long> numList = new ArrayList<Long>();
	List<String> audtrmList = new ArrayList<String>();
	YgycMapper qdykMapper = mybatisDao.getSqlSession().getMapper(YgycMapper.class);
	List<YgycData> dataList = qdykMapper.getYgycEmployeeQtyLine(qdykdata);
	for (YgycData qdyk : dataList) {
	    numList.add(qdyk.getEmployeeQty());
	    audtrmList.add(qdyk.getAudTrm());
	}
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("numList", numList);
	data.put("audtrmList", audtrmList);
	return data;

    }

    public Map<String, Object> getYgycPreAmountLine(ParameterData qdykdata) {
	List<BigDecimal> amtList = new ArrayList<BigDecimal>();
	List<String> audtrmList = new ArrayList<String>();
	YgycMapper qdykMapper = mybatisDao.getSqlSession().getMapper(YgycMapper.class);
	List<YgycData> dataList = qdykMapper.getYgycPreAmountLine(qdykdata);
	for (YgycData qdyk : dataList) {
	    amtList.add(qdyk.getTotalAmt());
	    audtrmList.add(qdyk.getAudTrm());
	}
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("amtList", amtList);
	data.put("audtrmList", audtrmList);
	return data;

    }

    public Map<Integer, YgycData> getYgycMapData(ParameterData qdykdata) {
	List<BigDecimal> amtList = new ArrayList<BigDecimal>();
	List<String> audtrmList = new ArrayList<String>();
	YgycMapper qdykMapper = mybatisDao.getSqlSession().getMapper(YgycMapper.class);
	List<YgycData> dataList = qdykMapper.getYgycMapData(qdykdata);
	Map<Integer, YgycData> data = new HashMap<Integer, YgycData>();
	for (YgycData ygyc : dataList) {
	    if (qdykdata.getPrvdId() == 10000) {
		data.put(ygyc.getPrvdId(), ygyc);
	    }
	    if (qdykdata.getPrvdId() != 10000) {
		data.put(ygyc.getCtyId(), ygyc);
	    }
	}
	return data;

    }

    public Map<Integer, YgycData> getYgycMapBottomData(ParameterData qdykdata) {
	YgycMapper qdykMapper = mybatisDao.getSqlSession().getMapper(YgycMapper.class);
	List<YgycData> dataList = qdykMapper.getYgycMapBottomData(qdykdata);
	Map<Integer, YgycData> data = new HashMap<Integer, YgycData>();
	if (dataList.size() > 0)
	    data.put(dataList.get(0).getPrvdId(), dataList.get(0));
	return data;

    }

    public Map<String, Object> getYgycJobnumber(ParameterData qdykdata) {
	YgycMapper qdykMapper = mybatisDao.getSqlSession().getMapper(YgycMapper.class);
	List<YgycData> dataList = qdykMapper.getYgycJobnumber(qdykdata);
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("jobinfo", dataList);
	return data;
    }

    public Map<String, Object> getYgycPhoneTable(ParameterData qdykdata) {
	YgycMapper qdykMapper = mybatisDao.getSqlSession().getMapper(YgycMapper.class);
	List<YgycData> dataList = qdykMapper.getYgycPhoneTable(qdykdata);
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("phoneSum", dataList);
	return data;
    }

    public Map<String, Object> getYgycOperatorByphone(ParameterData qdykdata) {
	YgycMapper qdykMapper = mybatisDao.getSqlSession().getMapper(YgycMapper.class);
	List<YgycData> dataList = qdykMapper.getYgycOperatorByphone(qdykdata);
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("operatorInfo", dataList);
	return data;
    }

    // 员工异常操作-统计分析-排名汇总
    public Map<String, Object> getRankTable(ParameterData ygycdata) {
	YgycMapper ygycMapper = mybatisDao.getSqlSession().getMapper(YgycMapper.class);
	List<YgycData> prvdList = ygycMapper.getRankTable(ygycdata);
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("rankTable", prvdList);
	return data;
    }

    // 员工异常操作-统计分析-历史统计
    public Map<String, Object> getHistoryTable(ParameterData ygycdata) {
	YgycMapper ygycMapper = mybatisDao.getSqlSession().getMapper(YgycMapper.class);
	List<Map> prvdList = ygycMapper.getHistoryTable(ygycdata);
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("historyTable", prvdList);
	return data;
    }
    // 员工异常操作-统计分析-重点关注用户
    public Map<String, Object> getFocusUserTable(ParameterData ygycdata) {
	YgycMapper ygycMapper = mybatisDao.getSqlSession().getMapper(YgycMapper.class);
	List<Map> prvdList = ygycMapper.getFocusUserTable(ygycdata);
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("FocusUserData", prvdList);
	return data;
    }
    // 员工异常操作-统计分析-重点关注用户-下钻
    public Map<String, Object> getFocusUserDetaile(ParameterData ygycdata) {
	YgycMapper ygycMapper = mybatisDao.getSqlSession().getMapper(YgycMapper.class);
	List<YgycData> prvdList = ygycMapper.getFocusUserDetaile(ygycdata);
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("DetaileData", prvdList);
	return data;
    }
    
    // 员工异常操作-统计分析-重点关注员工号
    public Map<String, Object> getYgycFocusEmployee(ParameterData ygycdata) {
		YgycMapper ygycMapper = mybatisDao.getSqlSession().getMapper(YgycMapper.class);
		List<Map<String, Object>> prvdList = ygycMapper.getYgycFocusEmployee(ygycdata);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("employeeData", prvdList);
		return data;
    }
    
    // 员工异常操作-统计分析-重点关注员工号--下钻
    public Map<String, Object> getYgycFocusEmployeeTable(ParameterData ygycdata) {
		YgycMapper ygycMapper = mybatisDao.getSqlSession().getMapper(YgycMapper.class);
		List<Map<String, Object>> prvdList = ygycMapper.getYgycFocusEmployeeTable(ygycdata);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("detailsData", prvdList);
		return data;
    }

    /**
     * 
     * <pre>
     * Desc  获取审计报告信息
     * @param parameterData
     * @return
     * @author hufei
     * 2017-9-4 上午10:56:09
     * </pre>
     */
    public String getReportInfo(ParameterData parameterData) {
	Map<String,Object> result=new HashMap<String,Object>();
	// 第一步：获取文档模板
	String modelText = getModel(5);
	// 第二步：获取变量数据源
	Map<String, Object> paramsMap = getYgycReportData(parameterData.getAudTrm(), parameterData.getPrvdId());
	if (paramsMap.get("concern") == null || paramsMap.get("concern").toString().length() == 0) {
	    return null;
	}
	// 第三步：替换变量
	String tpText = replaceParam(modelText, paramsMap);
	String stringList[] = tpText.split("\\|");
	StringBuffer resultBuffer=new StringBuffer(512);
	for(int i=0;i<2;i++){
	    resultBuffer.append(stringList[i]);
	    resultBuffer.append("<br/>");
	}
	if(paramsMap.get("amt5001")!=null&&(!paramsMap.get("amt5001").toString().isEmpty())){
	    resultBuffer.append(paramsMap.get("No5001"));
	    resultBuffer.append(stringList[3]);
	    resultBuffer.append("<br/>");
	    resultBuffer.append(stringList[4]);
	    resultBuffer.append("<br/>");
	}
	if(paramsMap.get("amt5002")!=null&&(!paramsMap.get("amt5002").toString().isEmpty())){
	    resultBuffer.append(paramsMap.get("No5002"));
	    resultBuffer.append(stringList[5]);
	    resultBuffer.append("<br/>");
	    resultBuffer.append(stringList[6]);
	    resultBuffer.append("<br/>");
	}
	if(paramsMap.get("amt5003")!=null&&(!paramsMap.get("amt5003").toString().isEmpty())){
	    resultBuffer.append(paramsMap.get("No5003"));
	    resultBuffer.append(stringList[7]);
	    resultBuffer.append("<br/>");
	    resultBuffer.append(stringList[8]);
	    resultBuffer.append("<br/>");
	}
	if(paramsMap.get("amt5004")!=null&&(!paramsMap.get("amt5004").toString().isEmpty())){
	    resultBuffer.append(paramsMap.get("No5004"));
	    resultBuffer.append(stringList[9]);
	    resultBuffer.append("<br/>");
	    resultBuffer.append(stringList[10]);
	    resultBuffer.append("<br/>");
	}
	for(int i=11;i<=12;i++){
	    resultBuffer.append(stringList[i]);
	    resultBuffer.append("<br/>");
	}
	return resultBuffer.toString();
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

    /**
     * <pre>
     * Desc获取审计报告模板  
     * @param subjectId专题ID
     * 
     * @return
     * @author hufei
     * 2017-9-4 上午10:46:11
     * </pre>
     */
    public String getModel(int subjectId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("subjectId", subjectId);

	Map<String, Object> contentMap = mybatisDao.get("ygycReport.getModelContent", params);
	return contentMap.get("report_content").toString();

    }

    /**
     * <pre>
     * Desc  获取审计报告数据
     * @param audTrm 月份
     * @param prvdId 省份
     * @return
     * @author hufei
     * 2017-9-4 上午10:47:15
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getYgycReportData(String audTrm, int prvdId) {
	Map<String, Object> result = new HashMap<String, Object>();
	result.put("oprNum5001", "");
	result.put("userNum5001", "");
	result.put("giveNum5001", "");
	result.put("pointValue5001", "");
	result.put("amt5001", "");
	result.put("perPointValue5001", "");
	result.put("perAmt5001", "");
	result.put("oprNum5002", "");
	result.put("userNum5002", "");
	result.put("giveNum5002", "");
	result.put("pointValue5002", "");
	result.put("amt5002", "");
	result.put("perPointValue5002", "");
	result.put("perAmt5002", "");
	result.put("oprNum5003", "");
	result.put("userNum5003", "");
	result.put("giveNum5003", "");
	result.put("amt5003", "");
	result.put("perAmt5003", "");
	result.put("oprNum5004", "");
	result.put("userNum5004", "");
	result.put("giveNum5004", "");
	result.put("amt5004", "");
	result.put("perAmt5004", "");
	result.put("example5001", "");
	result.put("example5002", "");
	result.put("example5003", "");
	result.put("example5004", "");

	DecimalFormat df = new DecimalFormat("###,###,##0.00");
	DecimalFormat df1 = new DecimalFormat("###,###,###,##0");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");

	String No5001 = "";
	String No5002 = "";
	String No5003 = "";
	String No5004 = "";
	result.put("No5001", No5001);
	result.put("No5002", No5002);
	result.put("No5003", No5003);
	result.put("No5004", No5004);
	// 插入月份相关信息
	String thisMonth = "0".equals(audTrm.substring(4, 5)) ? audTrm.substring(5, 6) : audTrm.substring(4, 6);
	result.put("audTrm", audTrm);
	result.put("audTrm2", audTrm.substring(0, 4) + "年" + thisMonth+ "月");
	result.put("audTrm1", getLastDayByAudtrm(audTrm, 2));
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("audTrm", audTrm);
	params.put("prvdId", prvdId);
	StringBuffer concern = new StringBuffer();

	// 获取积分赠送-5001-基本信息-add by hufei- 20170819
	Map<String, Object> ygyc5001BaseInfo = mybatisDao.get("ygycReport.getYGYC5001BaseInfo", params);
	if (ygyc5001BaseInfo != null) {
	    result.put("oprNum5001", ygyc5001BaseInfo.get("totalStaff"));
	    result.put("userNum5001", ygyc5001BaseInfo.get("totalSubs"));
	    result.put("giveNum5001", ygyc5001BaseInfo.get("totalTime"));
	    result.put("pointValue5001", ygyc5001BaseInfo.get("totalWValue"));
	    result.put("amt5001", ygyc5001BaseInfo.get("totalAmt10000"));
	    result.put("perPointValue5001", ygyc5001BaseInfo.get("perWValue"));
	    result.put("perAmt5001", ygyc5001BaseInfo.get("perAmt10000"));
	    concern.append("积分赠送");
	    No5001 = "（一）";
	}
	// 获取积分赠送-5002-基本信息-add by hufei- 20170819
	Map<String, Object> ygyc5002BaseInfo = mybatisDao.get("ygycReport.getYGYC5002BaseInfo", params);
	if (ygyc5002BaseInfo != null) {
	    result.put("oprNum5002", ygyc5002BaseInfo.get("totalStaff"));
	    result.put("userNum5002", ygyc5002BaseInfo.get("totalSubs"));
	    result.put("giveNum5002", ygyc5002BaseInfo.get("totalTime"));
	    result.put("pointValue5002", ygyc5002BaseInfo.get("totalWValue"));
	    result.put("amt5002", ygyc5002BaseInfo.get("totalAmt10000"));
	    result.put("perPointValue5002", ygyc5002BaseInfo.get("perWValue"));
	    result.put("perAmt5002", ygyc5002BaseInfo.get("perAmt10000"));
	    if (concern.length() != 0) {
		concern.append("、");
		No5002 = "（二）";
	    } else {
		No5002 = "（一）";
	    }
	    concern.append("积分转移");
	}
	// 获取积分赠送-5003-基本信息-add by hufei- 20170819
	Map<String, Object> ygyc5003BaseInfo = mybatisDao.get("ygycReport.getYGYC5003BaseInfo", params);
	if (ygyc5003BaseInfo != null) {
	    result.put("oprNum5003", ygyc5003BaseInfo.get("totalStaff"));
	    result.put("userNum5003", ygyc5003BaseInfo.get("totalAcct"));
	    result.put("giveNum5003", ygyc5003BaseInfo.get("totalTime"));
	    result.put("amt5003", ygyc5003BaseInfo.get("totalAmt10000"));
	    result.put("perAmt5003", ygyc5003BaseInfo.get("perAmt10000"));
	    if (concern.length() != 0) {
		if (concern.length() > 4) {
		    No5003 = "（三）";
		} else {
		    No5003 = "（二）";
		}
		concern.append("、");
	    } else {
		No5003 = "（一）";
	    }

	    concern.append("话费赠送");
	}
	// 获取积分赠送-5004-基本信息-add by hufei- 20170819
	Map<String, Object> ygyc5004BaseInfo = mybatisDao.get("ygycReport.getYGYC5004BaseInfo", params);
	if (ygyc5004BaseInfo != null) {
	    result.put("oprNum5004", ygyc5004BaseInfo.get("totalStaff"));
	    result.put("userNum5004", ygyc5004BaseInfo.get("totalAcct"));
	    result.put("giveNum5004", ygyc5004BaseInfo.get("totalTime"));
	    result.put("amt5004", ygyc5004BaseInfo.get("totalAmt10000"));
	    result.put("perAmt5004", ygyc5004BaseInfo.get("perAmt10000"));
	    if (concern.length() != 0) {
		if (concern.length() < 8) {
		    No5004 = "（二）";
		} else if (concern.length() < 12) {
		    No5004 = "（三）";
		} else {
		    No5004 = "（四）";
		}
		concern.append("、");
	    } else {
		No5004 = "（一）";
	    }
	    concern.append("退费");
	}
	result.put("concern", concern);
	if (concern.length() > 4) {
	    result.put("No5001", No5001);
	    result.put("No5002", No5002);
	    result.put("No5003", No5003);
	    result.put("No5004", No5004);
	}

	params.put("report", "Top5");
	// 获取积分赠送-5001-样例信息-add by hufei- 20170819
	List<Map<String, Object>> ygyc5001Top50 = mybatisDao.getList("ygycReport.getYGYCJFZSdata", params);
	if (ygyc5001Top50 != null&&ygyc5001Top50.size()>0) {
	    StringBuffer example5001 = new StringBuffer();
	    String cityName;
	    for (int i = 0, size = ygyc5001Top50.size() - 1; i <= size; i++) {
		Map<String, Object> map = ygyc5001Top50.get(i);
		if (i == 0) {
		    example5001.append("如：");
		}
		cityName="";
		if(map.get("city_name")!=null){
		    cityName=map.get("city_name").toString();
		}
		if (map.get("total_Time") != null && ((BigDecimal) map.get("total_Time")).compareTo(new BigDecimal(1)) == 1) {
		    example5001.append(cityName + "分公司工号“" + map.get("staff_id") + "”向用户“" + map.get("subs_id") + "”赠送积分" +df1.format(map.get("total_Time")) + "次，共计" + map.get("total_value_10000")
			    + "万分，测算价值为" + map.get("total_Amt_10000") + "万元");
		} else {
		    example5001.append(cityName + "分公司工号“" + map.get("staff_id") + "”向用户“" + map.get("subs_id") + "”一次性赠送积分" + map.get("total_value_10000") + "万分，测算价值为"
			    + map.get("total_Amt_10000") + "万元");
		}

		if (i != size) {
		    example5001.append("；");
		} else {
		    example5001.append("。");
		}
	    }
	    result.put("example5001", example5001);
	}
	// 获取积分赠送-5002-样例信息-add by hufei- 20170819
	List<Map<String, Object>> ygyc5002Top50 = mybatisDao.getList("ygycReport.getYGYCJFZYdata", params);
	if (ygyc5002Top50 != null&&ygyc5002Top50.size()>0) {
	    StringBuffer example5002 = new StringBuffer();
	    String cityName;
	    for (int i = 0, size = ygyc5002Top50.size() - 1; i <= size; i++) {
		Map<String, Object> map = ygyc5002Top50.get(i);
		cityName="";
		if(map.get("city_name")!=null){
		    cityName=map.get("city_name").toString();
		}
		if (i == 0) {
		    example5002.append("如：");
		}
		if (map.get("total_Time") != null && ((BigDecimal) map.get("total_Time")).compareTo(new BigDecimal(1)) == 1) {
		    example5002.append(cityName + "分公司工号“" + map.get("staff_id") + "”向用户“" + map.get("subs_id") + "”转入积分" + df1.format(map.get("total_Time")) + "次，共计" + map.get("total_value_10000")
			    + "万分，测算价值为" + map.get("total_Amt_10000") + "万元");
		} else {
		    example5002.append(cityName + "分公司工号“" + map.get("staff_id") + "”向用户“" + map.get("subs_id") + "”一次性转入积分" + map.get("total_value_10000") + "万分，测算价值为"
			    + map.get("total_Amt_10000") + "万元");
		}
		if (i != size) {
		    example5002.append("；");
		} else {
		    example5002.append("。");
		}
	    }
	    result.put("example5002", example5002);
	}
	// 获取积分赠送-5003-样例信息-add by hufei- 20170819
	List<Map<String, Object>> ygyc5003Top50 = mybatisDao.getList("ygycReport.getYGYCHFZSdata", params);
	if (ygyc5003Top50 != null&&ygyc5003Top50.size()>0) {
	    StringBuffer example5003 = new StringBuffer();
	    String cityName;
	    for (int i = 0, size = ygyc5003Top50.size() - 1; i <= size; i++) {
		Map<String, Object> map = ygyc5003Top50.get(i);
		cityName="";
		if(map.get("city_name")!=null){
		    cityName=map.get("city_name").toString();
		}
		if (i == 0) {
		    example5003.append("如：");
		}
		if (map.get("total_Time") != null && ((BigDecimal) map.get("total_Time")).compareTo(new BigDecimal(1)) == 1) {
		    example5003.append(cityName + "分公司工号“" + map.get("staff_id") + "”向账号“" + map.get("acct_id") + "”赠送话费" + df1.format(map.get("total_Time")) + "次，共计" + map.get("total_value_10000")
			    + "万元");
		} else {
		    example5003.append(cityName+ "分公司工号“" + map.get("staff_id") + "”向账号“" + map.get("acct_id") + "”一次性赠送话费" + map.get("total_value_10000") + "万元");
		}
		if (i != size) {
		    example5003.append("；");
		} else {
		    example5003.append("。");
		}
	    }
	    result.put("example5003", example5003);
	}
	// 获取积分赠送-5004-样例信息-add by hufei- 20170819
	List<Map<String, Object>> ygyc5004Top50 = mybatisDao.getList("ygycReport.getYGYCGETFdata", params);
	if (ygyc5004Top50 != null&&ygyc5004Top50.size()>0) {
	    StringBuffer example5004 = new StringBuffer();
	    String cityName;
	    for (int i = 0, size = ygyc5004Top50.size() - 1; i <= size; i++) {
		Map<String, Object> map = ygyc5004Top50.get(i);
		cityName="";
		if(map.get("city_name")!=null){
		    cityName=map.get("city_name").toString();
		}
		if (i == 0) {
		    example5004.append("如：");
		}
		if (map.get("total_Time") != null && ((BigDecimal) map.get("total_Time")).compareTo(new BigDecimal(1)) == 1) {
		    example5004.append(cityName+ "分公司工号“" + map.get("staff_id") + "”向账号“" + map.get("acct_id") + "”退费" + df1.format(map.get("total_Time")) + "次，共计" + map.get("total_value_10000")
			    + "万元");
		} else {
		    example5004.append(cityName+ "分公司工号“" + map.get("staff_id") + "”向账号“" + map.get("acct_id") + "”一次性退费" + map.get("total_value_10000") + "万元");
		}
		if (i != size) {
		    example5004.append("；");
		} else {
		    example5004.append("。");
		}

	    }
	    result.put("example5004", example5004);
	}

	return result;
    }
    /**
     * <pre>
     * Desc获取n月后的的时间  
     * @param audtrm 审计月
     * @param months 增加的月数
     * @return
     * @author hufei
     * 2017-9-4 上午10:47:54
     * </pre>
     */
    public String getLastDayByAudtrm(String audtrm, int months) {
	Calendar c = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
	try {
	    c.setTime(sdf2.parse(audtrm));
	} catch (ParseException e) {
	    e.printStackTrace();
	    logger.error(e.getMessage(), e);
	}
	c.add(Calendar.MONTH, months + 1);
	c.set(Calendar.DAY_OF_MONTH, 0);
	String s = sdf.format(c.getTime());
	return s;
    }
    
    public List<Map<String, Object>> getYGYCJFZSdata(String audTrm, String prvdId, String flag) throws Exception {
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("audTrm", audTrm);
    	params.put("prvdId", prvdId);
    	List<Map<String, Object>> result = null;
    	if (flag.equals("1")) {
    	    result = mybatisDao.getList("ygycReport.getYGYCJFZSdata", params);
    	}
    	if (flag.equals("2")) {
    	    result = mybatisDao.getList("ygycReport.getYGYCJFZYdata", params);
    	}
    	if (flag.equals("3")) {
    	    result = mybatisDao.getList("ygycReport.getYGYCHFZSdata", params);
    	}
    	if (flag.equals("4")) {
    	    result = mybatisDao.getList("ygycReport.getYGYCGETFdata", params);
    	}
    	if (flag.equals("5")) {
    	    result = mybatisDao.getList("ygycReport.getYGYCreportQDdata", params);
    	}
    	return result;
        }
    
    // @Description: 得到XWPFRun的CTRPr
    public CTRPr getRunCTRPr(XWPFParagraph p, XWPFRun pRun) {
	CTRPr pRpr = null;
	if (pRun.getCTR() != null) {
	    pRpr = pRun.getCTR().getRPr();
	    if (pRpr == null) {
		pRpr = pRun.getCTR().addNewRPr();
	    }
	} else {
	    pRpr = p.getCTP().addNewR().addNewRPr();
	}
	return pRpr;
    }
    
    // @Description 设置字体信息
    public void setParagraphRunFontInfo(XWPFParagraph p, XWPFRun pRun, String content, String fontFamily, String fontSize) {
	CTRPr pRpr = getRunCTRPr(p, pRun);
	if (StringUtils.isNotBlank(content)) {
	    pRun.setText(content);
	}
	// 设置字体
	CTFonts fonts = pRpr.isSetRFonts() ? pRpr.getRFonts() : pRpr.addNewRFonts();
	fonts.setAscii(fontFamily);
	fonts.setEastAsia(fontFamily);
	fonts.setHAnsi(fontFamily);

	// 设置字体大小
	CTHpsMeasure sz = pRpr.isSetSz() ? pRpr.getSz() : pRpr.addNewSz();
	sz.setVal(new BigInteger(fontSize));

	CTHpsMeasure szCs = pRpr.isSetSzCs() ? pRpr.getSzCs() : pRpr.addNewSzCs();
	szCs.setVal(new BigInteger(fontSize));
    }

}