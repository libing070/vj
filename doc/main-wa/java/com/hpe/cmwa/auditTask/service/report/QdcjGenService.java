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
public class QdcjGenService  extends BaseObject{

    @Autowired
    private MybatisDao mybatisDao;
    
    
    public Map<String, List<Object>> selectPageData(Map<String, Object> params) {
    	Map<String, List<Object>> returnMap = new HashMap<String, List<Object>>();
    	String []ref_name = {
				"totalInfo01"						
				,"qdcj11_tab"	
				
				,"totalInfo02"		
				,"qdcj21_tab"
				
				,"totalInfo03"	
				,"qdcj31_tab" 
				
				,"totalInfo04" 
				,"totalInfo04_2" 
				,"qdcj41_tab" 
				
				,"totalInfo05" 
				,"qdcj51_tab" 
				};

		String []ref_value = {
						"qdcjGenMapper.select_totalInfo01"			
						,"qdcjGenMapper.select_qdcj11_tab"	
		
						,"qdcjGenMapper.select_totalInfo02"			
						,"qdcjGenMapper.select_qdcj21_tab"
						
						,"qdcjGenMapper.select_totalInfo03"		
						,"qdcjGenMapper.select_qdcj31_tab"	
						
						,"qdcjGenMapper.select_totalInfo04"			
						,"qdcjGenMapper.select_totalInfo04_2"			
						,"qdcjGenMapper.select_qdcj41_tab"
						
						,"qdcjGenMapper.select_totalInfo05"		
						,"qdcjGenMapper.select_qdcj51_tab"	
						
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
