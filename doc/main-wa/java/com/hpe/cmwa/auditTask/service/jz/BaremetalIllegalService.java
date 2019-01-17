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

@Service
public class BaremetalIllegalService extends BaseObject {
	
	private DecimalFormat	   df = new DecimalFormat("######0.00");

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
     * 2016-11-25 下午3:30:39
     * </pre>
     */
    public List<Map<String, Object>> getSumPrvdince(Map<String, Object> params) {
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	if (moduleType(params)) {
	    list = mybatisDao.getList("baremetalIllegalMapper.selectSumPrvdince", params);
	} else {
	    list = mybatisDao.getList("contractIllegalMapper.selectSumPrvdince", params);
	}
	return list;
    }
    
    public Map<String, Object> getSumPrvdinceCon(Map<String, Object> params) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	if (moduleType(params)) {
    		List<Map<String, Object>> list = mybatisDao.getList("baremetalIllegalMapper.selectSumPrvdinceCon", params);
    		map.put("pageType", 1);
			map.put("list", list);
    	} else {
    		List<Map<String, Object>> list = mybatisDao.getList("contractIllegalMapper.selectSumPrvdinceCon", params);
    		map.put("pageType", 2);
			map.put("list", list);
    	}
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
     * 2016-11-25 下午3:31:10
     * </pre>
     */
    public List<Map<String, Object>> getSumCityByMap(Map<String, Object> params) {
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	if (moduleType(params)) {
	    list = mybatisDao.getList("baremetalIllegalMapper.selectSumCity", params);
	} else {
	    list = mybatisDao.getList("contractIllegalMapper.selectSumCity", params);
	}
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
     * 2016-11-27 下午4:44:54
     * </pre>
     */
    public List<Map<String, Object>> getSumSort(Map<String, Object> params) {
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	if (moduleType(params)) {
	    list = mybatisDao.getList("baremetalIllegalMapper.selectSumSort", params);
	} else {
	    list = mybatisDao.getList("contractIllegalMapper.selectSumSort", params);
	}
	return list;
    }

    /**
     * 获取前三城市
     * 
     * <pre>
     * Desc  
     * @param params
     * @return
     * @author jh
     * 2016-11-27 下午5:00:18
     * </pre>
     */
    public List<Map<String, Object>> getTop3City(Map<String, Object> params) {
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	if (moduleType(params)) {
	    list = mybatisDao.getList("baremetalIllegalMapper.selectTop3City", params);
	} else {
	    list = mybatisDao.getList("contractIllegalMapper.selectTop3City", params);
	}
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
     * 2016-11-25 下午3:31:10
     * </pre>
     */
    public List<Map<String, Object>> getSumCity(Pager pager) {
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	if (moduleType(pager.getParams())) {
	    list = mybatisDao.getList("baremetalIllegalMapper.selectSumCityPager", pager);
	} else {
	    list = mybatisDao.getList("contractIllegalMapper.selectSumCityPager", pager);
	}
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
    public Map<String,Object> getErrNumPer(Map<String, Object> params){
	Map<String, Object> map = new HashMap<String, Object>();
	if (moduleType(params)) {
	    map = mybatisDao.get("baremetalIllegalMapper.selectErrNumPer",params);
	}else{
	    map = mybatisDao.get("contractIllegalMapper.selectErrNumPer",params);
	}
	return map;
    }
    /**
     * 获取明细分页数据
     * 2016-11-27 下午6:02:14
     */
    @DataSourceName("dataSourceGBase")
    public List<Map<String, Object>> getDetailList(Pager pager) {
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	if (moduleType(pager.getParams())) {
	    list = mybatisDao.getList("baremetalIllegalMapper.selectDetailList", pager);
	} else {
	    list = mybatisDao.getList("contractIllegalMapper.selectDetailList", pager);
	}
	return list;
    }

    
   
    public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
		// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
		response.setContentType("application/octet-stream;charset=GBK");
	}
    /**
     * 查询所有明细信息做导出
     * 2016-11-27 下午6:04:57
     */
    @DataSourceName("dataSourceGBase")
    public void getDetailAll(HttpServletRequest request,HttpServletResponse response, Map<String, Object> params)throws Exception {
    	if (moduleType(params)) {
    		List<Map<String, Object>> charList = new ArrayList<Map<String,Object>>(); 
    		setFileDownloadHeader(request, response, "4.1.3_终端服务费合规性_终端裸机服务费违规_明细.csv");
    		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
	    	StringBuffer sb = new StringBuffer();
	    	sb.append("审计月,省代码,省名称,地市代码,地市名称,终端IMEI,销售日期,零售价格(元),销售月结酬金额(元)," +
	    			"销售第二月结酬金额(元),销售第三月结酬金额(元),结酬总金额(元),比例(酬金/裸机零售价)");
	    	out.println(sb.toString());
	    	sb.delete(0, sb.length());
	    	for(int i=0;i>=0;i++){
				params.put("pageStar", 10000*i);
				params.put("pageEnd", 10000);
				charList = mybatisDao.getList("baremetalIllegalMapper.selectDetailAll", params);
				if(charList.size()==0){
					break;
				}
		    	for (Map<String, Object> resultMap : charList) {
		    		sb.append(HelperString.objectConvertString(resultMap.get("audTrm"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("cmccProvPrvdId"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("shortName"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("cmccProvId"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("cmccPrvdNmShort"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("IMEI"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("salsTm"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("salsAmt"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("fstPayAmt"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("secPayAmt"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("thrPayAmt"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("tolPayAmt"))).append("\t,");
		    		
		    		Double perPaySals = resultMap.get("perPaySals") == null ? 0.00 : Double.parseDouble(resultMap.get("perPaySals").toString());
		    		sb.append(HelperString.objectConvertString(df.format(perPaySals * 100) + "%")).append("\t,");
		    		
		    		out.println(sb.toString());
		    		sb.delete(0, sb.length());
		    	}
		    	charList.clear();
			}
	    	out.flush();
	    	out.close();
	    	
	    	
		} else {
			List<Map<String, Object>> charList = new ArrayList<Map<String,Object>>(); 
    		setFileDownloadHeader(request, response, "4.1.3_终端服务费合规性_终端合约计划服务费违规_明细.csv");
    		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
	    	StringBuffer sb = new StringBuffer();
	    	sb.append("审计月,省标识,省名称,地市标识,地市简称,终端IMEI,销售时间,客户承诺月最低消费(元),销售月结酬金额(元),销售第二月结酬金额(元),销售第三月结酬金额(元)," +
	    			"结酬总金额(元),比例%（结酬总金额/月承诺最低消费）,第一月/最低消费(元),第二月/最低消费(元),第三月/最低消费(元)");
	    	out.println(sb.toString());
	    	sb.delete(0, sb.length());
	    	
	    	for(int i=0;i>=0;i++){
				params.put("pageStar", 10000*i);
				params.put("pageEnd", 10000);
				charList = mybatisDao.getList("contractIllegalMapper.selectDetailAll", params);
				if(charList.size()==0){
					break;
				}
		    	for (Map<String, Object> resultMap : charList) {
		    		sb.append(HelperString.objectConvertString(resultMap.get("audTrm"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("cmccProvPrvdId"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("shortName"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("cmccProvId"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("cmccPrvdNmShort"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("IMEI"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("salsTm"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("custPromMonCnsm"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("fstPayAmt"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("secPayAmt"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("thrPayAmt"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("tolPayAmt"))).append("\t,");
		    		
		    		Double perPaySals = resultMap.get("perPaySals") == null ? 0.00 : Double.parseDouble(resultMap.get("perPaySals").toString());
		    		sb.append(HelperString.objectConvertString(df.format(perPaySals * 100)) + "%").append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("perFstCnsm"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("perSecCnsm"))).append("\t,");
		    		sb.append(HelperString.objectConvertString(resultMap.get("perThrCnsm"))).append("\t,");
		    		
		    		out.println(sb.toString());
		    		sb.delete(0, sb.length());
		    	}
		    	charList.clear();
			}
	    	out.flush();
	    	out.close();
		}
	}

    /**
     * 查询所有地市汇总信息做导出
     * 
     * <pre>
     * Desc  
     * @param params
     * @return
     * @author jh
     * 2016-11-27 下午6:28:55
     * </pre>
     */
    public List<Map<String, Object>> getSumCityAll(Map<String, Object> params) {
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	if (moduleType(params)) {
	    list = mybatisDao.getList("baremetalIllegalMapper.selectSumCityAll", params);
	} else {
	    list = mybatisDao.getList("contractIllegalMapper.selectSumCityAll", params);
	}
	return list;
    }

    /**
     * 判断模块类型
     * 
     * <pre>
     * Desc  
     * @param parameterMap
     * @return
     * @author jh
     * 2016-11-29 上午9:44:45
     * </pre>
     */
    private boolean moduleType(Map<String, Object> parameterMap) {
	if (parameterMap.get("currModuleType") != null && !"".equals(parameterMap.get("currModuleType").toString()) && "1".equals(parameterMap.get("currModuleType").toString())) {
	    return true;
	}
	return false;
    }

	

}
