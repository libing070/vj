package com.hpe.cmwa.auditTask.service.jy;

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

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.common.datasource.DataSourceName;
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;


@Service
public class Hyzdcfxs3401Service extends BaseObject{
	
	@Autowired
    private MybatisDao mybatisDao;
	/**
	 * 双柱图
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> hyzdcfxsNum(Map<String, Object> params){
		return mybatisDao.getList("hyzdcfxs3401Mapper.hyzdcfxsNum", params);
	}
	//双柱图结论
	public Map<String,Object> getSumNumCon(Map<String, Object> params){
		return mybatisDao.get("hyzdcfxs3401Mapper.getSumNumCon", params);
	}
	//双柱图数据表
	public List<Map<String, Object>> getHyzdcfxsNum3401Detail_Table(Pager pager){
		return mybatisDao.getList("hyzdcfxs3401Mapper.getHyzdcfxsNum3401Detail_Table", pager);
	}
	//双柱图数据表导出
	public List<Map<String, Object>> exportHyzdcfxsDetail(Map<String, Object> parameterMap){
		return mybatisDao.getList("hyzdcfxs3401Mapper.exportHyzdcfxsDetail", parameterMap);
	}
	//补贴金额
	public List<Map<String, Object>> loadbtjeChart(Map<String, Object> params){
		return mybatisDao.getList("hyzdcfxs3401Mapper.loadbtjeChart", params);
	}
	//补贴金额结论
	public List<Map<String, Object>> getTop3Con(Map<String, Object> params) {
		 return mybatisDao.getList("hyzdcfxs3401Mapper.getTop3Con", params);
	}
	
	//补贴金额结论
	public List<Map<String, Object>> getTableCon(Map<String, Object> params) {
		 return mybatisDao.getList("hyzdcfxs3401Mapper.getTableCon", params);
	}
	
	//	销售数量
	public List<Map<String, Object>> loadxsslChart(Map<String, Object> params){
		return mybatisDao.getList("hyzdcfxs3401Mapper.loadxsslChart", params);
	}
	//	销售数量结论
	public List<Map<String, Object>> getXsslTop3Con(Map<String, Object> params) {
		 return mybatisDao.getList("hyzdcfxs3401Mapper.getXsslTop3Con", params);
	}
	//销售数量数据表
	public List<Map<String, Object>> getxssl3401Detail_Table(Pager pager){
		return mybatisDao.getList("hyzdcfxs3401Mapper.getxssl3401Detail_Table", pager);
	}
	//销售数量数据表导出
	public List<Map<String, Object>> exportXssl3401Detail(Map<String, Object> parameterMap){
		return mybatisDao.getList("hyzdcfxs3401Mapper.exportXssl3401Detail", parameterMap);
	}
	//明细页面
	@DataSourceName("dataSourceGBase")
	public List<Map<String, Object>> getCityDetailPagerList(Pager pager){
		return mybatisDao.getList("hyzdcfxs3401Mapper.getCityDetailPagerList", pager);
	}
	//明细导出
	@DataSourceName("dataSourceGBase")
	public void exportMxDetailList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> parameterMap) throws Exception  {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		setFileDownloadHeader(request, response, "合约终端销售异常_合约终端重复销售_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		sb.append("审计月,省代码,省名称,地市代码,地市名称,终端IMEI,销售渠道标识,销售渠道名称,员工标识," +
				  "员工名称,用户标识,销售时间,销售类型,营销案编码," +
				  "营销案名称,捆绑周期,实际购机金额(元),积分兑换值,使用积分兑换金额(元)," +
				  "采购成本(元),裸机零售价(元),终端补贴成本(元),话费补贴成本(元),客户预存话费(元),客户实缴费用总额(元),客户承诺月最低消费(元)");
		out.println(sb.toString());
		sb.delete(0, sb.length());
		//导出数据过大时分页导出
		for(int i=0;i>=0;i++){ 
			parameterMap.put("pageStar", 10000*i);
			parameterMap.put("pageEnd", 10000);
			list = mybatisDao.getList("hyzdcfxs3401Mapper.exportMxDetailList", parameterMap);
			if(list.size()==0){
				break;
			}
			for (Map<String, Object> map : list) {
				sb.append(HelperString.objectConvertString(map.get("aud_trm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_prvd_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("short_name"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prov_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cmcc_prvd_nm_short"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("trmnl_imei"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("chnl_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("chnl_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("emp_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("emp_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("subs_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("sell_dat"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("sell_typ"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("offer_id"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("offer_nm"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("bnd_prd"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("actl_shop_amt"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("acum_exch_val"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("usd_acum_exch_amt"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("shop_cost"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("unlocked_retl_prc"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("trmnl_allow_cost"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("fee_allow_cost"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cust_ppay_fee"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cust_actl_fee_sum"))).append("\t,");
				sb.append(HelperString.objectConvertString(map.get("cust_prms_mon_least_consm"))).append("\t,");
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
