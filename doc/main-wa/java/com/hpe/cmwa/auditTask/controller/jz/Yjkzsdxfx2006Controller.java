package com.hpe.cmwa.auditTask.controller.jz;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.jz.Yjkzsdxfx2006Service;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;


@Controller
@RequestMapping("/yjkzsdxfx2006/200604")
public class Yjkzsdxfx2006Controller extends BaseController{

	private DecimalFormat df = new DecimalFormat("######0.00");
	
	@Autowired
	private Yjkzsdxfx2006Service yjkzsdxfx2006service;

	
	/**
	 * 初始化界面
	 * @return
	 */
	@RequestMapping(value = "/index")
    public String index(HttpServletRequest request) {
		if(request.getParameter("provinceCode").equals("10000")){
			return  "auditTask/sjk/yjkzsdxfx/yjkzsdxfx";
		}else{
			return "auditTask/jz/yjkzsdxfx/yjkzsdxfx2006";
		}
		
    }
	
	@ResponseBody
	@RequestMapping(value = "/getYjkzsdxfx2006Trend")
	 public  Map<String, Object> getYjkzsdxfx2006Trend(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		 List<Map<String, Object>> list = yjkzsdxfx2006service.getZheXian200604Trend(parameterMap);
		 //计算平均值
		 Map<String, Object> avgMap = yjkzsdxfx2006service.getZheXian200604AvgNumber(parameterMap);
		 //计算周期内最大赠送金额
		 List<Map<String, Object>> listMax = yjkzsdxfx2006service.getZheXian200604MAXNumber(parameterMap);
		 Map<String, Object> maxMap = new HashMap<String, Object>();
		 Double max_yjk_amt;
		 String maxAud_trm="";
		 Double highAvg;
		 if(listMax.size()==0){
			 max_yjk_amt=0.00;
			 maxAud_trm=null;
		 }else{
			 max_yjk_amt = Double.parseDouble((listMax.get(0).get("max_yjk_amt").toString()));
			 maxAud_trm = listMax.get(0).get("aud_trm").toString();
		 }
		 Double avgNumber = avgMap.get("avg_yjk_amt")==null?0.00:Double.parseDouble(avgMap.get("avg_yjk_amt").toString());
	     if(avgNumber==0.00){
	    	 highAvg=0.00;
	     }else if(max_yjk_amt==0.00){
	    	 highAvg=0.00;
	     }else{
	    	 highAvg = Double.valueOf((max_yjk_amt-avgNumber)/avgNumber);
	     }
	     
		 Map<String, Object> returnMap = new HashMap<String, Object>();
		 maxMap.put("highAvg", df.format(highAvg*100)+"%");
		 maxMap.put("max_yjk_amt", max_yjk_amt);
		 maxMap.put("maxAud_trm", maxAud_trm);
	     returnMap.put("data", list);
	     returnMap.put("avgMap", avgMap);
	     returnMap.put("maxMap", maxMap);
		 return returnMap;
	 }
	
	
	@ResponseBody
	@RequestMapping(value = "/loadzhuxingChart")
	 public  Map<String, Object> loadzhuxingChart(HttpServletRequest request) {
		 Map<String, Object> parameterMap = this.getParameterMap(request);
		 List<Map<String, Object>> list = yjkzsdxfx2006service.loadzhuxingChart(parameterMap);
		 List<Map<String, Object>> tolAmtdataList = yjkzsdxfx2006service.getzhuxingTolAmtCon(parameterMap);
		 Map<String, Object> returnMap = new HashMap<String, Object>();
		 String tolAmtdata="";
		 if(tolAmtdataList.size()==0||list.size()==0){
			 tolAmtdata="0";
		 }else{
			 tolAmtdata=tolAmtdataList.get(0).get("sum_yjk_amt").toString();
		 }
	     returnMap.put("data", list);
	     returnMap.put("tolAmtdata", tolAmtdata);
		 return returnMap;
	}
	
	
	@ResponseBody
    @RequestMapping(value = "/loadZsfzdfjt_TabDetailTable")
	public Pager loadZsfzdfjt_TabDetailTable(HttpServletResponse response, HttpServletRequest request, Pager pager){
		Map<String, Object> parameterMap = this.getParameterMap(request);
		pager.setParams(parameterMap);
		List<Map<String, Object>> list = yjkzsdxfx2006service.loadZsfzdfjt_TabDetailTable(pager);
		pager.setDataRows(list);
		return pager;
	}
	
	
	@ResponseBody
    @RequestMapping(value = "/exportloadZsfzdfjt_TabDetail")
	public void exportloadZsfzdfjt_TabDetail(HttpServletRequest request, HttpServletResponse response){
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    try {
		    	yjkzsdxfx2006service.exportloadZsfzdfjt_TabDetail(request, response,parameterMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	
	@ResponseBody
    @RequestMapping(value = "/getCityDetailPagerList")
	public Pager getCityDetailPagerList(HttpServletResponse response, HttpServletRequest request, Pager pager) throws UnsupportedEncodingException{
		Map<String, Object> parameterMap = this.getParameterMap(request);
		pager.setParams(parameterMap);
		List<Map<String, Object>> list = yjkzsdxfx2006service.getCityDetailPagerList(pager);
		pager.setDataRows(list);
		return pager;
	}
	
	@ResponseBody
    @RequestMapping(value = "/exportMxDetailList")
	public void exportMxDetailList(HttpServletRequest request, HttpServletResponse response){
			Map<String, Object> parameterMap = this.getParameterMap(request);
		    try {
		    	yjkzsdxfx2006service.exportMxDetailList(request, response,parameterMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	/**
	    * <pre>
	    * Desc 每个controller都要实现的一个方法，用来设置页面初始化查询参数的form使用 
	    * @return
	    * @author peter.fu
	    * Nov 17, 2016 2:16:17 PM
	    * </pre>
	    */
	   @SuppressWarnings("rawtypes")
	   @ResponseBody
	   @RequestMapping(value = "/initDefaultParams")
	   public Map initDefaultParams(HttpServletRequest request) {

	       Map<String, Object> defaultParams = new HashMap<String, Object>();
	       
	       // 默认按地市排名
	       defaultParams.put("hz_rankType", 0);
	       // 地市汇总地图倍数
	       defaultParams.put("hz_map_double", 5);
	       
	       return defaultParams;
	   }
	
	
}
