package com.hpe.cmwa.auditTask.service.jz;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.common.datasource.DataSourceName;
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;

/**
 * 
 * @author gongtingting
 *
 */
@Service
public class wgService {

	private DecimalFormat	   df = new DecimalFormat("######0.00");
	@Autowired
	private MybatisDao	mybatisDao;
	/**
	 * 地市汇总
	 * @param pager
	 * @return
	 */
	public List<Map<String, Object>> getSumCity(Pager pager) {
		List<Map<String, Object>> list = mybatisDao.getList("wg.selectSumCity", pager);
		
		return list;
	}
	/**
	 * daochudishi
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getSumCityAll(Map<String, Object> params) {

		List<Map<String, Object>> list = mybatisDao.getList("wg.selectSumCityAll", params);
		return list;
	}
	//加载省汇总表统计周期内每月违规办理业务笔数
	public List<Map<String, Object>> getMonthTotalIncome(Map<String, Object> formatParameter) {
		  List<Map<String, Object>> list = mybatisDao.getList("wg.selectPerMonthWg", formatParameter);
		 
		   return list;
	}
	//加载省汇总表统计周期内全省的平均单月违规办理业务笔数(累计违规办理业务笔数/月数)）
	public List<Map<String, Object>> getMonthTotalIncomeAvg(Map<String, Object> canshumap) {
		
		 List<Map<String, Object>> list1 = mybatisDao.getList("wg.selectAvgMonthWg", canshumap);
		   
		   return list1;
	}
	//加载审计结论
	public Map<String, Object> getQianZheJieLu(Map<String, Object> params) {
		
		Map<String, Object> map = mybatisDao.get("wg.selectQianZhejielun",params);
		return map;
	}

	public Map<String, Object> getZheJieLu(Map<String, Object> formatParameter) {
		Map<String, Object> map = mybatisDao.get("wg.selectZhejielun",formatParameter);
		Double zds= Double.parseDouble(map.get("maxzhi").toString());
		Double pjz= Double.parseDouble(map.get("pjz").toString());
		/*Double  d=(zds-pjz)/pjz*100;
		map.put("zb", Double.parseDouble(df.format(d)));*/
		if(pjz==0){
			map.put("zb", 0.00);
		}else{
			Double  d=(zds-pjz)/pjz*100;
			map.put("zb", Double.parseDouble(df.format(d)));
		}
		return map;
	}
	//进行对明细数据进行刷新
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getDetDetailData(Pager pager) {
		 List<Map<String, Object>> list = mybatisDao.getList("wg.selectMingXi", pager);
		 
		 return list;
	}
	//查找前三业务
	public List<Map<String, Object>>  getOnloadZhu(Map<String, Object> formatParameter) {
	
		List<Map<String, Object>>  list = mybatisDao.getList("wg.selectZhuZhuangTu", formatParameter);
		
		return list;
	}
	//加载柱状图
	public List<Map<String, Object>> getZhu(Map<String, Object> params) {
		List<Map<String, Object>>  list = mybatisDao.getList("wg.selectZhu", params);
		
		return list;
	}
	public List<Map<String, Object>> getQianZhu(Map<String, Object> params) {
		List<Map<String, Object>>  list = mybatisDao.getList("wg.selectZhuQianJieLun", params);
		
		return list;
	}
	public List<Map<String, Object>> getZhongZhu(Map<String, Object> params) {
		List<Map<String, Object>>  list = mybatisDao.getList("wg.selectZhuZhongJieLun", params);
		
		return list;
	}
	public List<Map<String, Object>> getHouZhu(Map<String, Object> params) {
		List<Map<String, Object>>  list = mybatisDao.getList("wg.selectZhuHouJieLun", params);
		
		return list;
	}
	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
		// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
		response.setContentType("application/octet-stream;charset=GBK");
	}
	//导出明细数据
	@DataSourceName("dataSourceGBase")
	public void getWgDetailData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> params)throws Exception {
		List<Map<String, Object>> charList = new ArrayList<Map<String,Object>>(); 
		if("wg".equals(params.get("expTyp"))){
			setFileDownloadHeader(request, response, "违规订购_违规对欠费停机、预销号用户订购业务_明细.csv");
		}else{
			setFileDownloadHeader(request, response, "4.1.1_指标作假_违规对欠费停机、预销号用户订购业务_明细.csv");
		}
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	StringBuffer sb = new StringBuffer();
    	sb.append("审计月,省代码,省名称,地市代码,地市名称,用户标识,操作流水号,业务受理类型,业务受理类型名称,手机号码,业务类型编码,业务类型名称,业务办理时间," +
    			"订单状态,订购业务生效时间,订购业务失效时间,办理业务渠道标识,渠道名称,操作员工标识,操作员工姓名");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for(int i=0;i>=0;i++){
			params.put("pageStar", 10000*i);
			params.put("pageEnd", 10000);
			charList = mybatisDao.getList("wg.selectWgDetailAll", params);
			if(charList.size()==0){
				break;
			}
	    	for (Map<String, Object> resultMap : charList) {
	    		sb.append(HelperString.objectConvertString(resultMap.get("Aud_trm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("cmcc_prov_prvd_id"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("short_name"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("cmcc_prov_id"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("cmcc_prvd_nm_short"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("USER_ID"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("OPR_SER_NO"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("BUSI_ACCE_TYP_ID"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("BUSI_ACCE_TYP_NM"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("MSISDN"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("BUSI_TYP_NO"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("BUSI_TYP_NM"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("BUSI_OPR_TM"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("ORDER_STAT"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("BUSI_EFF_DT"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("BUSI_END_DT"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("BUSI_CHNL_ID"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("BUSI_CHNL_NM"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("STAFF_ID"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("STAFF_NM"))).append("\t,");
	    		out.println(sb.toString());
	    		sb.delete(0, sb.length());
	    	}
	    	charList.clear();
		}
    	out.flush();
    	out.close();
	}
	
}

