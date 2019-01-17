/**
 * com.hpe.cmwa.auditTask.controller.jz.TFSRHGXController.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.controller.report;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.io.ByteStreams;
import com.hpe.cmwa.auditTask.service.report.KhqfGenService;
import com.hpe.cmwa.auditTask.service.report.QdcjGenService;
import com.hpe.cmwa.auditTask.service.report.ReportService;
import com.hpe.cmwa.auditTask.service.report.YjkGenService;
import com.hpe.cmwa.controller.BaseController;

/**
 * <pre>
 * Desc： 
 * @author   peter.fu
 * @refactor peter.fu
 * @date     Jan 15, 2017 8:09:36 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Jan 15, 2017 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/report/")
public class ReportController extends BaseController {

	@Autowired
	private ReportService	reportService;
	
	@Autowired
	private KhqfGenService khqfGenService;
	
	@Autowired
	private QdcjGenService qdcjGenService;
	@Autowired
	private YjkGenService yjkGenService;


	@RequestMapping(value = "downloadNoti", produces = "plain/text; charset=UTF-8")
	public void downloadNoti(HttpServletResponse response,
			HttpServletRequest request, Model uiModel) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		Map<String, Object> reportInfor = reportService.getReportInfor(parameterMap);
		if(reportInfor != null){
			InputStream fis =null;
			try {
				String fileName = reportInfor.get("report_name").toString();
				String path =  reportInfor.get("report_path").toString()+"/"+fileName;
				fis = new FileInputStream(path);
				response.setContentType("applicatoin/octet-stream;charset=GBK");
				setFileDownloadHeader(request,response,fileName);
//				response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"",URLEncoder.encode(fileName, "UTF-8")));
				response.flushBuffer();
				ByteStreams.copy(fis,response.getOutputStream());
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally {  
                if (fis != null) {  
                    try {  
                        fis.close();  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
			
		}
	}
	 @RequestMapping(value="existsData")
	 @ResponseBody
	public boolean existsData(HttpServletResponse response,
			HttpServletRequest request){
		 Map<String, Object> parameterMap = this.getParameterMap(request);
		 Map<String, Object> dataNumMap =  reportService.getDataNum(parameterMap);
		 if("0".equals(dataNumMap.get("dataNum").toString())){
			 return false;
		 }
		 return true;
	}
	@RequestMapping(value="getReportData")
	@ResponseBody
	public Map<String, List<Object>> getReportData(HttpServletResponse response,
			HttpServletRequest request){
		Map<String, List<Object>> dataMap = new HashMap<String, List<Object>>();
		String subjectId = request.getParameter("subjectId");
		String audTrm = request.getParameter("currEndDate2");
		String prvdId = request.getParameter("provinceCode2");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);
		params.put("provinceCode", prvdId);
		if("1001".equals(subjectId)){
			params.put("audTrm", audTrm);
			dataMap = khqfGenService.selectPageData(params);
		}
		if("2001".equals(subjectId)){
			params.put("beginAudTrm", request.getParameter("currStartDate2"));
			params.put("endAudTrm", audTrm);
			params.put("firAudTrm", request.getParameter("currDateFirstMonth"));
			dataMap = qdcjGenService.selectPageData(params);
		}
		if("3001".equals(subjectId)){
			params.put("audTrm", audTrm);
			dataMap = yjkGenService.selectPageData(params);
		}
		return dataMap;
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @param fileName
	 * Ren yx
	 * 设置字符集   判断请求来自哪个浏览器
	 * 
	 */
	 public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
	        final String userAgent = request.getHeader("USER-AGENT");
	        try {
	            String finalFileName = null;
	                finalFileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
	            response.setHeader("Content-Disposition", "attachment; filename=\"" + finalFileName + "\"");//这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
	        } catch (UnsupportedEncodingException e) {
	        }
	    }
	
}
