package com.hpe.cmwa.auditTask.service.jz;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
 * 3.2.15.3超低价流量
 * <pre>
 * Desc： 
 * @author   wangpeng
 * @refactor wangpeng
 * @date     2017-1-22 上午10:56:55
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-1-22 	   wangpeng 	         1. Created this class. 
 * </pre>
 */
@Service
public class GPRS1502Service {
    @Autowired
    private MybatisDao	mybatisDao;
    
    /**
     * 获取单价汇总数据
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-22 下午2:17:56
     * </pre>
     */
    public List<Map<String, Object>> getSumPrice(Map<String, Object> parameterMap) {
	return mybatisDao.getList("gprs1502.getSumPrice", parameterMap);
    }

    /**
     * 获取省汇总信息
     * 
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-22 下午3:17:20
     * </pre>
     */
    public List<Map<String, Object>> getSumPrvd(Map<String, Object> parameterMap) {
	return mybatisDao.getList("gprs1502.getSumPrvd", parameterMap);
    }
    /**
     * 获取地市汇总信息
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-22 下午4:23:19
     * </pre>
     */
    public List<Map<String, Object>> getSumCty(Map<String, Object> parameterMap) {
	return mybatisDao.getList("gprs1502.getSumCty", parameterMap);
    }
    
    public List<Map<String, Object>> getSumGPRS(Map<String, Object> parameterMap) {
    	return mybatisDao.getList("gprs1502.getSumGPRS", parameterMap);
        }
    
    public List<Map<String, Object>> maxCity3(Map<String, Object> parameterMap) {
    	return mybatisDao.getList("gprs1502.maxCity3", parameterMap);
        }
    
    public List<Map<String, Object>> minCity3(Map<String, Object> parameterMap) {
    	return mybatisDao.getList("gprs1502.minCity3", parameterMap);
        }
    
    
    
    
    /**
     * 获取地市汇总信息分页
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-22 下午4:23:19
     * </pre>
     */
    public List<Map<String, Object>> getSumCtyPager(Pager pager) {
	return mybatisDao.getList("gprs1502.getSumCtyPager", pager);
    }
    /**
     * 分页查询明细
     * <pre>
     * Desc  
     * @param pager
     * @return
     * @author wangpeng
     * 2017-1-22 下午5:36:39
     * </pre>
     */
    @DataSourceName("dataSourceGBase")
    public List<Map<String, Object>> getDetailPager(Pager pager) {
	return mybatisDao.getList("gprs1502.getDetailPager", pager);
    }

    /**
     * 查询明细所有数据
     * 
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author wangpeng
     * 2017-1-22 下午5:37:26
     * </pre>
     */
    @DataSourceName("dataSourceGBase")
    public void getDetailPagerAll(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap)throws Exception   {
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	setFileDownloadHeader(request, response, "4.3.2_流量产品合规性_超低价流量_明细表.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省份代码,省份名称,地市代码,地市名称,用户标识,用户总流量(M),GPRS收入(元),"+
				"基本月租费,GPRS相关收入(元),实际使用流量单价(元/M),资费套餐统一编码,资费套餐名称,"+
				"资费套餐描述,资费套餐销售状态,资费套餐停售时间,流量是否递延,上线日期,目标区域市场,"+
				"目标客户群,流量总量,国内通用流量,省内通用流量,闲时流量,4G单模流量,小区流量,语音总量,"+
				"国内通话,省内通话,本地通话,集团通话,亲情通话,网内通话,小区通话,闲时通话,短信总量,"+
				"彩信总量,WLAN总量时长,WLAN总量流量,套餐收入拆分方式,流量单价,国内通话主叫单价,"+
				"国内通话被叫单价,省内通话主叫单价,省内通话被叫单价,本地主叫通话单价,本地忙时单价,"+
				"本地闲时单价,小区通话单价,短信单价,彩信单价,WLAN单价(时长),WLAN单价(流量),"+
				"长市漫一体化套餐标识,套餐总价格");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("gprs1502.getDetailPagerAll", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");		 								
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");  								
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("subs_id"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("sum_strm_amt"))).append("\t,"); 				 					
				sb.append(HelperString.objectConvertString(map.get("gprs_amt"))).append("\t,"); 					 					
				sb.append(HelperString.objectConvertString(map.get("bas_mon_fr"))).append("\t,"); 				 					
				sb.append(HelperString.objectConvertString(map.get("fee_sum"))).append("\t,"); 					 					
				sb.append(HelperString.objectConvertString(map.get("gprs_price"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("fee_pack_unit_cd"))).append("\t,"); 								
				sb.append(HelperString.objectConvertString(map.get("fee_pack_nm"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("fee_pack_desc"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("fee_pack_stat"))).append("\t,"); 								
				sb.append(HelperString.objectConvertString(map.get("pack_end_dt"))).append("\t,"); 				 			
				sb.append(HelperString.objectConvertString(map.get("strm_amt_is_def"))).append("\t,"); 								
				sb.append(HelperString.objectConvertString(map.get("onln_dt"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("tgt_rgn_mkt"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("tgt_grp"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("strm_tot"))).append("\t,"); 											
				sb.append(HelperString.objectConvertString(map.get("dom_athrth_strm"))).append("\t,");									
				sb.append(HelperString.objectConvertString(map.get("prov_athrty_strm"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("idle_tm_strm"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("sing_modl_4g_strm"))).append("\t,"); 								
				sb.append(HelperString.objectConvertString(map.get("cell_strm"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("voic_tot"))).append("\t,"); 											
				sb.append(HelperString.objectConvertString(map.get("dom_call"))).append("\t,"); 											
				sb.append(HelperString.objectConvertString(map.get("prov_call"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("loacl_call"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("org_call"))).append("\t,"); 											
				sb.append(HelperString.objectConvertString(map.get("folk_call"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("nets_call"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("cell_call"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("idle_tm_call"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("sms_tot"))).append("\t,"); 											
				sb.append(HelperString.objectConvertString(map.get("mms_tot"))).append("\t,"); 											
				sb.append(HelperString.objectConvertString(map.get("wlan_tot_dur"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("wlan_tot_strm"))).append("\t,");									
				sb.append(HelperString.objectConvertString(map.get("pack_inc_splt_typ"))).append("\t,"); 							
				sb.append(HelperString.objectConvertString(map.get("strm_prc"))).append("\t,"); 											
				sb.append(HelperString.objectConvertString(map.get("dom_call_prc"))).append("\t,"); 										
				sb.append(HelperString.objectConvertString(map.get("dom_be_call_prc"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("prov_call_prc"))).append("\t,");									
				sb.append(HelperString.objectConvertString(map.get("prov_be_call_prc"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("loacl_call_prc"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("loacl_bsy_tm_prc"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("loacl_idle_tm_prc"))).append("\t,"); 								
				sb.append(HelperString.objectConvertString(map.get("cell_call_prc"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("sms_prc"))).append("\t,");  											
				sb.append(HelperString.objectConvertString(map.get("mms_prc"))).append("\t,");  											
				sb.append(HelperString.objectConvertString(map.get("wlan_prc_dur"))).append("\t,");  									
				sb.append(HelperString.objectConvertString(map.get("wlan_prc_strm"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("long_pack_id"))).append("\t,"); 									
				sb.append(HelperString.objectConvertString(map.get("pack_tot_prc"))).append("\t,"); 													
				out.println(sb.toString());
				sb.delete(0, sb.length());
			}
			list.clear();
		}
		out.flush();
		out.close();
    }
    
  //导出头设置
  	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
  		// 这里设置一下让浏览器弹出下载提示框,而不是直接在浏览器中打开
  		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
  		response.setContentType("application/octet-stream;charset=GBK");
  	}
}
