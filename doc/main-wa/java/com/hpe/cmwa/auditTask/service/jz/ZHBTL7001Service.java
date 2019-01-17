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

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.common.datasource.DataSourceName;
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.HelperString;

/**
 * 
 * <pre>
 * Desc： 
 * @author   wangpeng
 * @refactor wangpeng
 * @date     2016-11-30 上午10:26:02
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2016-11-30 	   wangpeng 	         1. Created this class.
 * </pre>
 */
@Service
public class ZHBTL7001Service extends BaseObject {
	private DecimalFormat	   df = new DecimalFormat("######0.00");

    @Autowired
    private MybatisDao mybatisDao;
    
    
    public Map<String, Object> getSumPrvdinceCon(Map<String, Object> params) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	List<Map<String, Object>> list = mybatisDao.getList("ZHBTL7001Mapper.selectSumPrvdinceCon", params);
    	map.put("list", list);
    	return map;
    }
    /**
     * 获取省汇总信息
     * 
     * <pre>
     * Desc  
     * @param params
     * @return
     * @author wangpeng
     * </pre>
     */
    public List<Map<String, Object>> getSumPrvdince(Map<String, Object> params) {

	List<Map<String, Object>> list = mybatisDao.getList("ZHBTL7001Mapper.selectSumPrvdince", params);

	return list;
    }

    /**
     * 获取地市汇总信息
     * 
     * <pre>
     * Desc  
     * @param params
     * @return
     * @author wangpeng
     * </pre>
     */
    public List<Map<String, Object>> getSumCityByMap(Map<String, Object> params) {

	List<Map<String, Object>> list = mybatisDao.getList("ZHBTL7001Mapper.selectSumCity", params);

	return list;
    }

    /**
     * 获取全国排名
     * 
     * <pre>
     * Desc  
     * @param params
     * @return
     * @author jh
     * </pre>
     */
    public List<Map<String, Object>> getSumSort(Map<String, Object> params) {

	List<Map<String, Object>> list = mybatisDao.getList("ZHBTL7001Mapper.selectSumSort", params);

	return list;
    }

    /**
     * 获取TOP3城市
     * 
     * <pre>
     * Desc  
     * @param params
     * @return
     * @author jh
     * </pre>
     */
    public List<Map<String, Object>> getTop3City(Map<String, Object> params) {

	List<Map<String, Object>> list = mybatisDao.getList("ZHBTL7001Mapper.selectTop3City", params);

	return list;
    }
    
    /**
     * 获取全国违规终端数量占比
     * <pre>
     * Desc  
     * @param params
     * @return
     * @author wangpeng
     * 2016-12-12 下午2:13:18
     * </pre>
     */
    public Map<String,Object> getImeiNumPer(Map<String, Object> params){
	
	Map<String, Object> map = new HashMap<String, Object>();
	
	map = mybatisDao.get("ZHBTL7001Mapper.selectImeiNumPer", params);
	
	return map;
    }
    /**
     * 获取地市汇总信息分页
     * 
     * <pre>
     * Desc  
     * @param params
     * @return
     * @author wangpeng
     * </pre>
     */
    public List<Map<String, Object>> getSumCity(Pager pager) {

	List<Map<String, Object>> list = mybatisDao.getList("ZHBTL7001Mapper.selectSumCityPager", pager);

	return list;
    }



    /**
     * 查询所有地市汇总信息做导出
     * 
     * <pre>
     * Desc  
     * @param params
     * @return
     * @author jh
     * </pre>
     */
    public List<Map<String, Object>> getSumCityAll(Map<String, Object> params) {

	List<Map<String, Object>> list = mybatisDao.getList("ZHBTL7001Mapper.selectSumCityAll", params);

	return list;
    }
    
    /**
     * 获取明细分页数据
     * 
     * <pre>
     * Desc  
     * @param pager
     * @return
     * @author jh
     * </pre>
     */
    @DataSourceName("dataSourceGBase")
    public List<Map<String, Object>> getDetailList(Pager pager) {

	List<Map<String, Object>> list = mybatisDao.getList("ZHBTL7001Mapper.selectDetailList", pager);

	return list;
    }
    
    
    @DataSourceName("dataSourceGBase")
	public void getDetailAll(HttpServletRequest request,HttpServletResponse response, Map<String, Object> params)throws Exception {
		List<Map<String, Object>> charList = new ArrayList<Map<String,Object>>(); 
		setFileDownloadHeader(request, response, "4.2.3_终端综合补贴合规性_综合补贴率_明细.csv");
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	StringBuffer sb = new StringBuffer();
    	sb.append("审计月,营销案编码,营销案名称,营销案种类,用户标识,终端IMEI,销售时间,销售渠道标识,渠道名称,销售省份," +
    			 "地市名称,终端补贴成本(元),话费补贴(元),捆绑周期,客户承诺月最低消费(元),综合补贴率(%)");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	for(int i=0;i>=0;i++){
			params.put("pageStar", 10000*i);
			params.put("pageEnd", 10000);
			charList = mybatisDao.getList("ZHBTL7001Mapper.selectDetailAll", params);
			if(charList.size()==0){
				break;
			}
	    	for (Map<String, Object> resultMap : charList) {
	    		sb.append(HelperString.objectConvertString(resultMap.get("audTrm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("offerId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("offerNm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("offerCls"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("subsId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("imei"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("sellDat"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("chnlId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("chnlNm"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("shortName"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("cmccPrvdNmShort"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("trmnlAllowCost"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("feeAllowCost"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("bndPrd"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("custPrmsMonLeastConsm"))).append("\t,");
	    		
	    		Double zhbtl = resultMap.get("zhbtl") == null ? 0.00 : Double.parseDouble(resultMap.get("zhbtl").toString());
	    		sb.append(HelperString.objectConvertString(Double.parseDouble(df.format(zhbtl*100)) + "%")).append("\t,");
	    		
	    		out.println(sb.toString());
	    		sb.delete(0, sb.length());
	    	}
	    	charList.clear();
		}
    	out.flush();
    	out.close();
	}
	
	public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
		// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
		response.setContentType("application/octet-stream;charset=GBK");
	}

}
