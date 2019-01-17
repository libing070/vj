package com.hpe.cmwa.auditTask.service.jz;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
 * 3.2.17.3重复办理合约
 * <pre>
 * Desc： 
 * @author   wangpeng
 * @refactor wangpeng
 * @date     2017-2-4 上午10:49:59
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-2-4 	   wangpeng 	         1. Created this class. 
 * </pre>
 */
@Service
public class HYZDBTHGX1702Service {
	private DecimalFormat df = new DecimalFormat("######0.00");
    @Autowired
    private MybatisDao	mybatisDao;
    /**
     * 重复办理统计
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-2-6 下午2:27:14
     * </pre>
     */
    @DataSourceName("dataSourceGBase")
    public List<Map<String, Object>> getSumRepeat(Map<String, Object> parameterMap) {
	return mybatisDao.getList("hyzdbthgx1702.getSumRepeat", parameterMap);
    }
    /**
     * 获取前三地市
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-2-6 下午3:18:33
     * </pre>
     */
    public List<Map<String, Object>> getRepeatTop3City(Map<String, Object> parameterMap) {
	return mybatisDao.getList("hyzdbthgx1702.getRepeatTop3City", parameterMap);
    }
    /**
     * 获取重复办理合约计划终端数总数
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-2-6 下午3:20:13
     * </pre>
     */
    public Map<String, Object> getSumImeiNum(Map<String, Object> parameterMap) {
	return mybatisDao.get("hyzdbthgx1702.getSumImeiNum", parameterMap);
    }
    /**
     * 获取数据表数据
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-2-6 下午3:56:33
     * </pre>
     */
    @DataSourceName("dataSourceGBase")
    public List<Map<String, Object>> getSumRepeatTable(Pager pager) {
	return mybatisDao.getList("hyzdbthgx1702.getSumRepeatTable", pager);
    }
    /**
     * 获取数据表数据 不分页
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-2-6 下午4:26:22
     * </pre>
     */
    @DataSourceName("dataSourceGBase")
    public List<Map<String, Object>> getSumRepeatTableAll(Map<String, Object> parameterMap) {
	return mybatisDao.getList("hyzdbthgx1702.getSumRepeatTableAll", parameterMap);
    }
    /**
     * 获取饼图数据
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-2-7 上午9:53:58
     * </pre>
     */
    @DataSourceName("dataSourceGBase")
    public Map<String, Object> getSumPieCharts(Map<String, Object> parameterMap) {
	return mybatisDao.get("hyzdbthgx1702.getSumPieCharts", parameterMap);
    }
    /**
     * 获取饼图审计结论数据
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-2-7 上午10:29:03
     * </pre>
     */
    public Map<String, Object> getSumPieCon(Map<String, Object> parameterMap) {
 	return mybatisDao.get("hyzdbthgx1702.getSumPieCon", parameterMap);
     }
    /**
     * 获取详情列表 分页
     * <pre>
     * Desc  
     * @param pager
     * @return
     * @author wangpeng
     * 2017-2-7 上午11:22:01
     * </pre>
     */
    @DataSourceName("dataSourceGBase")
    public List<Map<String, Object>> getDetailListPager(Pager pager) {
	return mybatisDao.getList("hyzdbthgx1702.getDetailListPager", pager);
    }
  //导出头设置
  	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
  		// 这里设置一下让浏览器弹出下载提示框,而不是直接在浏览器中打开
  		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
  		response.setContentType("application/octet-stream;charset=GBK");
  	}
  	@DataSourceName("dataSourceGBase")
	public void getDetailList(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	setFileDownloadHeader(request, response, "4.4.2_合约终端补贴合规性_重复办理终端类合约计划_明细表.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,营销案编码,营销案名称,销售时间,渠道标识,渠道名称,员工标识,用户标识,终端IMEI,捆绑周期," +
				"合约失效日期,重复办理营销案编码,重复办理营销案名称,重复办理时间,重复办理终端IMEI,重复办理渠道标识,重复办理渠道名称,重复办理员工标识," +
				"实际购机金额(元),积分兑换值,使用积分兑换金额(元),采购成本(元),裸机零售价(元),终端补贴成本(元)," +
				"话费补贴成本(元),客户预存话费(元),客户实缴费用总额(元),客户承诺月最低消费(元)," +
				"重复营销案捆绑周期,客户捆绑合约类型,产品类型名称");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("hyzdbthgx1702.getDetailList", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {

			      sb.append(HelperString.objectConvertString(map.get("AUD_TRM"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("CMCC_PROV_PRVD_ID"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("SHORT_NAME"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("CMCC_PROV_ID"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("CMCC_PRVD_NM_SHORT"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("OFFER_ID2"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("OFFER_NM2"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("SELL_DAT2"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("SELL_CHNL_ID2"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("CHNL_NM2"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("EMP_ID2"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("SUBS_ID2"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("TRMNL_IMEI2"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("BND_PRD2"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("END_DT"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("OFFER_ID"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("OFFER_NM"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("SELL_DAT"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("TRMNL_IMEI"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("SELL_CHNL_ID"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("CHNL_NM"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("EMP_ID"))).append("\t,");
			      
			      
			      sb.append(HelperString.objectConvertString(df.format(map.get("ACTL_SHOP_AMT")))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("ACUM_EXCH_VAL"))).append("\t,");
			      sb.append(HelperString.objectConvertString(df.format(map.get("USD_ACUM_EXCH_AMT")))).append("\t,");
			      sb.append(HelperString.objectConvertString(df.format(map.get("SHOP_COST")))).append("\t,");
			      sb.append(HelperString.objectConvertString(df.format(map.get("UNLOCKED_RETL_PRC")))).append("\t,");
			      sb.append(HelperString.objectConvertString(df.format(map.get("TRMNL_ALLOW_COST")))).append("\t,");
			      sb.append(HelperString.objectConvertString(df.format(map.get("FEE_ALLOW_COST")))).append("\t,");
			      sb.append(HelperString.objectConvertString(df.format(map.get("CUST_PPAY_FEE")))).append("\t,");
			      sb.append(HelperString.objectConvertString(df.format(map.get("CUST_ACTL_FEE_SUM")))).append("\t,");
			      sb.append(HelperString.objectConvertString(df.format(map.get("CUST_PRMS_MON_LEAST_CONSM")))).append("\t,");
			      
			      
			      sb.append(HelperString.objectConvertString(map.get("BND_PRD"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("CUST_BND_CONTRT_NM"))).append("\t,");
			      sb.append(HelperString.objectConvertString(map.get("PROD_TYP_NM"))).append("\t,");
			   					
				
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
		}
		out.flush();
		out.close();
	}
}
