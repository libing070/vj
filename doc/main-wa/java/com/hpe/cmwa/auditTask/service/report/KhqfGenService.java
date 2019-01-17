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
 * @date     20180119
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  20180119 	   renyx 	         1. Created this class. 
 * </pre>  
 */
@Service
public class KhqfGenService  extends BaseObject{

    @Autowired
    private MybatisDao mybatisDao;
    
    
    public Map<String, List<Object>> selectPageData(Map<String, Object> params) {
    	Map<String, List<Object>> returnMap = new HashMap<String, List<Object>>();
    	String []ref_name = {
				"totalInfo01"						
				,"khqf11_top5"	
				,"khqf12_top5"	
				
				,"totalInfo02"		
				,"khqf21_top5"
				,"khqf22_top5"  
				
				,"totalInfo03"	
				,"khqf31_top5" 
				
				,"totalInfo04" 
				,"khqf41_top5" 
				,"khqf42_top5" 
				
				,"totalInfo05" 
				,"khqf51_top5" 
				
				,"totalInfo06" 
				,"khqf61_top5" 
				,"khqf62_top5" 
				};

		String []ref_value = {
						"khqfGenMapper.select_totalInfo01"			
						,"khqfGenMapper.select_khqf11_top5"	
						,"khqfGenMapper.select_khqf12_top5"	
		
						,"khqfGenMapper.select_totalInfo02"			
						,"khqfGenMapper.select_khqf21_top5"
						,"khqfGenMapper.select_khqf22_top5"	
						
						,"khqfGenMapper.select_totalInfo03"		
						,"khqfGenMapper.select_khqf31_top5"	
						
						,"khqfGenMapper.select_totalInfo04"			
						,"khqfGenMapper.select_khqf41_top5"
						,"khqfGenMapper.select_khqf42_top5"	
						
						,"khqfGenMapper.select_totalInfo05"		
						,"khqfGenMapper.select_khqf51_top5"	
						
						,"khqfGenMapper.select_totalInfo06"		
						,"khqfGenMapper.select_khqf61_top5"	
						,"khqfGenMapper.select_khqf62_top5"	
						
						
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
