/**
 * com.hpe.cmwa.auditTask.service.jz.TFSRHGXService.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.service.report;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.dao.MybatisDao;


/**
 * <pre>
 * Desc： 
 * @author   renyx
 * @refactor renyx
 * @date     2018
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		 2018 	  renyx	         1. Created this class. 
 * </pre>  
 */
@Service
public class YjkGenService  extends BaseObject{

    @Autowired
    private MybatisDao mybatisDao;
    
    
    public Map<String, List<Object>> selectPageData(Map<String, Object> params) {
    	Map<String, List<Object>> returnMap = new HashMap<String, List<Object>>();
    	String []ref_name = {
				"totalInfo01"						
				,"yjk11_top5"	
				
				,"totalInfo02"		
				,"yjk21_top5"
				,"yjk22_top10"  
				
				,"totalInfo03"	
				,"yjk31_top5" 
				,"yjk32_top10" 
				
				,"totalInfo04" 
				,"yjk41_top5" 
				
				,"totalInfo05" 
				,"yjk51_top5" 
				,"yjk52_top10" 
				
				,"totalInfo06" 
				,"yjk61_top5" 
				,"yjk62_top10" 
				
				,"totalInfo07" 
				,"yjk71_top5" 
				,"yjk72_top5" 
				
				,"totalInfo08" 
				,"yjk81_top5" 
				};

		String []ref_value = {
						"yjkGenMapper.select_totalInfo01"			
						,"yjkGenMapper.select_yjk11_top5"	
		
						,"yjkGenMapper.select_totalInfo02"			
						,"yjkGenMapper.select_yjk21_top5"
						,"yjkGenMapper.select_yjk22_top10"	
						
						,"yjkGenMapper.select_totalInfo03"		
						,"yjkGenMapper.select_yjk31_top5"	
						,"yjkGenMapper.select_yjk32_top10"	
						
						,"yjkGenMapper.select_totalInfo04"			
						,"yjkGenMapper.select_yjk41_top5"
						
						,"yjkGenMapper.select_totalInfo05"		
						,"yjkGenMapper.select_yjk51_top5"	
						,"yjkGenMapper.select_yjk52_top10"	
						
						,"yjkGenMapper.select_totalInfo06"		
						,"yjkGenMapper.select_yjk61_top5"	
						,"yjkGenMapper.select_yjk62_top10"	
						
						
						,"yjkGenMapper.select_totalInfo07"		
						,"yjkGenMapper.select_yjk71_top5"	
						,"yjkGenMapper.select_yjk72_top5"	
						
						
						,"yjkGenMapper.select_totalInfo08"		
						,"yjkGenMapper.select_yjk81_top5"	
						};

		for (int i = 0; i < ref_name.length; i++){
			List<Object> resultList = mybatisDao.getList(ref_value[i], params);
			returnMap.put(ref_name[i], resultList);
			System.out.println("-------" + ref_name[i] + "::" + resultList);
		}
		
		return returnMap;
	}

	
	
    public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {

		// 这里设置一下让浏览器弹出下载提示框,而不是直接在浏览器中打开
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GBK"), "iso-8859-1") + "\"");
		response.setContentType("application/octet-stream;charset=GBK");
	}
}
