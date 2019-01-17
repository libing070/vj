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
 * 3.2.17.2合约内离网
 * <pre>
 * Desc： 
 * @author   wangpeng
 * @refactor wangpeng
 * @date     2017-1-22 下午7:13:14
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-1-22 	   wangpeng 	         1. Created this class. 
 * </pre>
 */
@Service
public class HYZDBTHGX1701Service {
	
	private DecimalFormat df = new DecimalFormat("######0.00");
    @Autowired
    private MybatisDao	mybatisDao;
    /**
     * 营销案离网率
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-23 上午10:14:40
     * </pre>
     */
    public List<Map<String, Object>> getQdhyPrvd(Map<String, Object> parameterMap) {
 	return mybatisDao.getList("hyzdbthgx1701.getQdhyPrvd", parameterMap);
     }
    /**
     * 获取营销案汇总信息
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-23 下午2:41:32
     * </pre>
     */
    public List<Map<String, Object>> getSumOffer(Pager pager) {
 	return mybatisDao.getList("hyzdbthgx1701.getSumOffer", pager);
     }
    /**
     *  获取营销案汇总所有信息
     * <pre>
     * Desc  
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-23 下午3:38:06
     * </pre>
     */
    public List<Map<String, Object>> getSumOfferAll(Map<String, Object> map) {
	return mybatisDao.getList("hyzdbthgx1701.getSumOfferAll", map);
    }

    /**
     * 获取合约内离网用户在线时长
     * 
     * <pre>
     * Desc  
     * @param map
     * @return
     * @author wangpeng
     * 2017-2-3 上午9:56:53
     * </pre>
     */
    public List<Map<String, Object>> getOnlineMon(Map<String, Object> map) {
	return mybatisDao.getList("hyzdbthgx1701.getOnlineMon", map);
    }

    /**
     * 获取详情信息分页
     * 
     * <pre>
     * Desc  
     * @param pager
     * @return
     * @author wangpeng
     * 2017-2-3 下午2:18:46
     * </pre>
     */
    @DataSourceName("dataSourceGBase")
    public List<Map<String, Object>> getDetailListPager(Pager pager) {
	return mybatisDao.getList("hyzdbthgx1701.getDetailListPager", pager);
    }

  //导出头设置
  	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
  		// 这里设置一下让浏览器弹出下载提示框,而不是直接在浏览器中打开
  		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
  		response.setContentType("application/octet-stream;charset=GBK");
  	}
  	 @DataSourceName("dataSourceGBase")
	public void getDetailListAll(HttpServletRequest request,HttpServletResponse response, Map<String, Object> parameterMap)throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	setFileDownloadHeader(request, response, "4.4.2_合约终端补贴合规性_合约内离网_营销案离网率情况_明细表.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,渠道标识,渠道名称,员工标识,用户标识,销售时间,终端IMEI,营销案编码,营销案名称," +
				"捆绑周期,实际购机金额(元),积分兑换值,使用积分兑换金额(元),采购成本(元),裸机零售价(元),终端补贴成本(元),话费补贴成本(元),客户预存话费(元),客户实缴费用总额(元)," +
				"客户承诺月最低消费(元),客户捆绑合约类型,产品类型名称,合约失效日期,用户状态,用户离网日期,用户入网日期,用户在网时长");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("hyzdbthgx1701.getDetailListAll", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				 sb.append(HelperString.objectConvertString(map.get("AUD_TRM"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("CMCC_PROV_PRVD_ID"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("SHORT_NAME"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("CMCC_PROV_ID"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("CMCC_PRVD_NM_SHORT"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("SELL_CHNL_ID"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("CHNL_NM"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("EMP_ID"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("SUBS_ID"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("SELL_DAT"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("TRMNL_IMEI"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("OFFER_ID"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("OFFER_NM"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("BND_PRD"))).append("\t,"); 
			     
			     
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
			     
			     sb.append(HelperString.objectConvertString(map.get("CUST_BND_CONTRT_NM"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("PROD_TYP_NM"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("END_DT"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("SUBS_STAT_TYP_NM"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("EFF_DT"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("ENT_DT"))).append("\t,"); 
			     sb.append(HelperString.objectConvertString(map.get("ONLINE_MON"))).append("\t,"); 						
				
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
		}
		out.flush();
		out.close();
	}
}
