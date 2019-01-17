package com.hpe.cmwa.auditTask.service.jy;

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


@Service
public class Qdyk2401Service {
	
	private DecimalFormat df = new DecimalFormat("######0.00");
	  @Autowired
	  private MybatisDao mybatisDao;
	  
	  
	// 渠道养卡用户数量波动趋势图
	public List<Map<String, Object>> getQdykyhslChart(Map<String, Object> parameterMap){
		return mybatisDao.getList("QDYK2401.getQdykyhslChart", parameterMap);
	}
	// 渠道养卡用户数量波动趋势平均值
	public Map<String, Object>  getQdykyhsl2304AVGNumber(Map<String, Object> parameterMap){
		return mybatisDao.get("QDYK2401.getQdykyhsl2304AVGNumber", parameterMap);
	}
	//渠道养卡用户数量波动趋势最大值
	public List<Map<String, Object>> getQdykyhsl2304MAXNumber(Map<String, Object> parameterMap){
		return mybatisDao.getList("QDYK2401.getQdykyhsl2304MAXNumber", parameterMap);
	}
	//渠道养卡用户数量波动趋势数据表
	public List<Map<String, Object>> loadQdykyhslTabDetailTable(Pager pager){
		return mybatisDao.getList("QDYK2401.loadQdykyhslTabDetailTable", pager);
	}
	//渠道养卡用户数量波动趋势数据表导出
	public List<Map<String, Object>> exportQdykyhslDetail(Map<String, Object> parameterMap) {
		return mybatisDao.getList("QDYK2401.exportQdykyhslDetail", parameterMap);
	}
	//地市小额非整数充值统计 图形
	public List<Map<String,Object>> dsqdykyhtjCharts(Map<String, Object> params){
		return mybatisDao.getList("QDYK2401.dsqdykyhtjCharts", params);
	}
	//地市小额非整数充值统计 图形
	public List<Map<String,Object>> qdykyhslhz(Map<String, Object> params){
		return mybatisDao.getList("QDYK2401.qdykyhslhz", params);
	}
	// 地市小额非整数充值统计 结论
	public List<Map<String, Object>> dsqdykyhtjCon(Map<String, Object> parameterMap){
		return mybatisDao.getList("QDYK2401.dsqdykyhtjCon", parameterMap);
	}
	//地市小额非整数充值统计 数据表
	public List<Map<String, Object>> loadDsxefzcz_TabDetailTable(Pager pager){
		return mybatisDao.getList("QDYK2401.loadDsxefzcz_TabDetailTable", pager);
	}
	//地市小额非整数充值统计 数据表导出
	public List<Map<String, Object>> exportDsxefzczDetail(Map<String, Object> parameterMap) {
		return mybatisDao.getList("QDYK2401.exportDsxefzczDetail", parameterMap);
	}
	//明细表
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getCityDetailPagerList(Pager pager){
		return mybatisDao.getList("QDYK2401.getCityDetailPagerList", pager);
	}
	
	//明细 数据表导出
	@DataSourceName("dataSourceGBase")
	public void exportMxDetailList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap)throws Exception   {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "渠道养卡_渠道养卡_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,用户标识,手机号码,入网渠道,渠道名称,渠道类型,渠道类型名称,渠道类别,渠道类别名称,"+
				"渠道状态,同一高频IMEI,同一高频基站,同一高频对端号码,相同的月消费金额(元),同一天开通飞信特征,同一天开通手机邮箱特征,同一天开通MM,"+
				"同一天开通手机报纸,同一天开通手机钱包,同一天开通无线音乐,同一天办理同一叠加套餐,漫游次数零次,基站个数少,被叫天数少,付费通信次数少,"+
				"被叫交往圈人数小,零充值特征,数据业务套利,SP消费套利,垃圾短信套利,骚扰电话套利,组合业务套利,获赠话费套利,"+
				"第一月点对点短信条数,第一月通信天数,第一月高频终端IMEI,第一月高频对端号码,第一月主叫交往圈人数,第一月主叫总次数,第一月被叫交往圈人数,"+
				"第一月漫游通话次数,第一月最高频基站,第一月被叫天数,第一月付费通信次数,"+
				"第二月点对点短信条数,第二月通信天数,第二月高频终端IMEI,第二月高频对端号码,第二月主叫交往圈人数,第二月主叫总次数,第二月被叫交往圈人数,"+
				"第二月漫游通话次数,第二月最高频基站,第二月被叫天数,第二月付费通信次数,"+
				"第三月点对点短信条数,第三月通信天数,第三月高频终端IMEI,第三月高频对端号码,第三月主叫交往圈人数,第三月主叫总次数,"+
				"第三月被叫交往圈人数,第三月漫游通话次数,第三月最高频基站,第三月被叫天数,第三月付费通信次数");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("QDYK2401.exportMxDetailList", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");				
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");	 	
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");				
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("CMCC_prvd_nm_short"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("user_id"))).append("\t,");				
				sb.append(HelperString.objectConvertString(map.get("rase_crd_no"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("chnl_id"))).append("\t,");				
				sb.append(HelperString.objectConvertString(map.get("chnl_nm"))).append("\t,");				
				sb.append(HelperString.objectConvertString(map.get("chnl_class"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("chnl_class_nm"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("chnl_type"))).append("\t,");				
				sb.append(HelperString.objectConvertString(map.get("chnl_type_nm"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("chnl_stat"))).append("\t,");				
				sb.append(HelperString.objectConvertString(map.get("same_imei"))).append("\t,");				
				sb.append(HelperString.objectConvertString(map.get("same_bts"))).append("\t,");				
				sb.append(HelperString.objectConvertString(map.get("same_ops_nbr"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("same_cnsm_amt"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("open_ftn"))).append("\t,");				
				sb.append(HelperString.objectConvertString(map.get("open_139mail"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("open_MM"))).append("\t,");				
				sb.append(HelperString.objectConvertString(map.get("open_mnews"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("open_mbl_wlt"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("open_wmc"))).append("\t,");				
				sb.append(HelperString.objectConvertString(map.get("open_stack_pack"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("zero_roam"))).append("\t,");				
				sb.append(HelperString.objectConvertString(map.get("less_bts_qty")));			
				sb.append(HelperString.objectConvertString(map.get("less_becall_days"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("less_pay_comm_qty"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("less_becall_cnct_pqty"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("zero_pay_fee"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("data_busn_artage"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("spcnsm_artage"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("spam_sms_artage"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("crank_call_artage"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("group_busn_artage")));	 	
				sb.append(HelperString.objectConvertString(map.get("pres_fee_artage"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("frst_ptop_sms_qty"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("frst_comm_days"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("frst_mstfrqcy_imei"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("frst_mstfrqcy_ops_nbr"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("frst_call_cntct_pqty"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("frst_call_tot_qty"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("frst_becall_cnct_pqty"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("frst_roam_call_qty"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("frst_mstfrqcy_bts"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("frst_becall_days"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("frst_pay_comm_qty"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("secd_ptop_sms_qty"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("secd_comm_days"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("secd_mstfrqcy_imei"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("secd_mstfrqcy_ops_nbr"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("secd_call_cntct_pqty"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("secd_call_tot_qty"))).append("\t,");			
				sb.append(HelperString.objectConvertString(map.get("secd_becall_cnct_pqty"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("secd_roam_call_qty"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("secd_mstfrqcy_bts"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("secd_becall_days"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("secd_pay_comm_qty"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("thrd_ptop_sms_qty"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("thrd_comm_days"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("thrd_mstfrqcy_imei"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("thrd_mstfrqcy_ops_nbr"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("thrd_call_cntct_pqty"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("thrd_call_tot_qty"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("thrd_recall_cntct_pqty"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("thrd_roam_call_qty"))).append("\t,");	
				sb.append(HelperString.objectConvertString(map.get("thrd_mstfrqcy_bts"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("thrd_becall_days"))).append("\t,");		
				sb.append(HelperString.objectConvertString(map.get("thrd_pay_comm_qty"))).append("\t,");
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
