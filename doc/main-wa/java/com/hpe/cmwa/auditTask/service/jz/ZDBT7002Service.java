package com.hpe.cmwa.auditTask.service.jz;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

@Service
public class ZDBT7002Service extends BaseObject{
    @Autowired
    private MybatisDao mybatisDao;

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

	List<Map<String, Object>> list = mybatisDao.getList("ZDBT7002Mapper.selectSumPrvdince", params);

	return list;
    }
    
    public Map<String, Object> getSumPrvdinceCon(Map<String, Object> params) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	List<Map<String, Object>> list = mybatisDao.getList("ZDBT7002Mapper.selectSumPrvdinceCon", params);
    	map.put("list", list);
    	return map;
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
	
	map = mybatisDao.get("ZDBT7002Mapper.selectImeiNumPer", params);
	
	return map;
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

	List<Map<String, Object>> list = mybatisDao.getList("ZDBT7002Mapper.selectSumCity", params);

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

	List<Map<String, Object>> list = mybatisDao.getList("ZDBT7002Mapper.selectSumSort", params);

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

	List<Map<String, Object>> list = mybatisDao.getList("ZDBT7002Mapper.selectTop3City", params);

	return list;
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

	List<Map<String, Object>> list = mybatisDao.getList("ZDBT7002Mapper.selectSumCityPager", pager);

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

	List<Map<String, Object>> list = mybatisDao.getList("ZDBT7002Mapper.selectSumCityAll", params);

	return list;
    }
    
    /**
     * 获取明细分页数据
     */
    @DataSourceName("dataSourceGBase")
    public List<Map<String, Object>> getDetailList(Pager pager) {

	List<Map<String, Object>> list = mybatisDao.getList("ZDBT7002Mapper.selectDetailList", pager);

	return list;
    }
    @DataSourceName("dataSourceGBase")
	public void getDetailAll(HttpServletRequest request,HttpServletResponse response, Map<String, Object> params)throws Exception {
		List<Map<String, Object>> charList = new ArrayList<Map<String,Object>>(); 
		setFileDownloadHeader(request, response, "4.2.4_终端综合补贴合规性_是否严格执行终端酬金与4G流量挂钩_明细.csv");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
    	StringBuffer sb = new StringBuffer();
    	sb.append("审计月,营销案编码,营销案名称,营销案种类,用户标识,终端IMEI,销售时间,销售渠道标识,渠道名称,销售省代码," +
    			"销售省名称,销售地市代码,销售地市名称,结算酬金月份,酬金(元),用户流量,通话次数");
    	out.println(sb.toString());
    	sb.delete(0, sb.length());
    	
    	for(int i=0;i>=0;i++){
			params.put("pageStar", 10000*i);
			params.put("pageEnd", 10000);
			charList = mybatisDao.getList("ZDBT7002Mapper.selectDetailAll", params);
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
	    		sb.append(HelperString.objectConvertString(resultMap.get("cmccPrvdNmShort"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("shortName"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("cmccProvId"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("cmccPrvdNmShort"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("settMonth"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("paySettAmt"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("datStmAmtM"))).append("\t,");
	    		sb.append(HelperString.objectConvertString(resultMap.get("sum_call_qty"))).append("\t,");
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
