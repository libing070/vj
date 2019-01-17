package com.hpe.cmwa.auditTask.controller;

import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.job.SubjectFileGenJob;
import com.hpe.cmwa.auditTask.service.report.KhqfFileGenProcessor;
import com.hpe.cmwa.auditTask.service.report.QdcjFileGenProcessor;
import com.hpe.cmwa.auditTask.service.report.YjkFileGenProcessor;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.MD5;

/**
 * <pre>
 * Desc：
 * url: http://ip:port/cmwa/dataMonitoring/customReport?report_name=aaaa&report_province=10100&report_start_date=201010&report_end_date=201010&report_end_date=201001&report_items=1001
 * 
 * 给国信自定义报告的生成接口
 * 实时生成报告，将报告路径和地址返回给国信
 * @author   yuxing.ren
 * @refactor yuxing.ren
 * @date     03 20, 2018
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  03 20, 2018 	   yuxing.ren 	         1. Created this class. 
 * </pre>
 */

@Controller
@RequestMapping("/dataMonitoring/")
public class CustomReportController extends BaseController {

	@Autowired
	private SubjectFileGenJob subjectFileGenJob;
	@Autowired
	KhqfFileGenProcessor khqfFileGenProcessor;
	@Autowired
	YjkFileGenProcessor yjkFileGenProcessor;
	@Autowired
	QdcjFileGenProcessor qdcjFileGenProcessor;
	
	
	/**
	 * 国信给的加密key
	 */
	private String				key					= "sj2bonc";

	/**
	 * <pre>
	 * Desc  根据审计点id映射到不同的controller访问路径
	 * 国信接口参数：
	 * 	report_name  报告名称（非空）
	 * report_province  报告对象（非空）10000
	 * report_start_date  报告开始时间（非空）201010
     * report_end_date  报告介绍时间（非空）201010
     * report_items  监控事项（非空）
     * validateStr  签名参数（非空）
     * timestamp时间戳 格式为yyyyMMddHHmmss（非空）
	 * 返回值：
	 *    生成报告的路径和名称/home/sj/XX.doc
	 * @param request
	 * @param response
	 * @return
	 * @author ren,yuxing 
	 * 03 20, 2018 3:10:16 PM
	 * </pre>
	 * @throws Exception 
	 */
	@RequestMapping(value = "customReport",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> params = this.getParameterMap(request);
		
		params.put("reportType", "custom");
		
		request.getSession().setAttribute("report_name", request.getParameter("report_name"));
	    request.getSession().setAttribute("report_province", request.getParameter("report_province"));
	    request.getSession().setAttribute("report_start_date", request.getParameter("report_start_date"));
	    request.getSession().setAttribute("report_end_date", request.getParameter("report_end_date"));
	    request.getSession().setAttribute("report_items", request.getParameter("report_items"));
	    request.getSession().setAttribute("report_create_persons", request.getParameter("report_create_persons"));
		    
//	    String report_name = new String(request.getParameter("report_name").getBytes("iso-8859-1"), "utf-8");//为了测试环境在不同环境下  编码不同的结果
	    @SuppressWarnings("deprecation")
		String report_name = URLDecoder.decode(request.getParameter("report_name"));
	    
		String report_province = request.getParameter("report_province");
		String report_start_date = request.getParameter("report_start_date");
		String report_end_date = request.getParameter("report_end_date");
		String report_items = request.getParameter("report_items");
//		String report_create_persons = new String(request.getParameter("report_create_persons").getBytes("iso-8859-1"), "utf-8");
		String report_create_persons = request.getParameter("report_create_persons");
		String validateStr = request.getParameter("validateStr");
		String timestamp = request.getParameter("timestamp");
		params.put("report_name", report_name);
		params.put("report_create_persons", report_create_persons);
		logger.debug("本次要生成的文件名："+report_name);
		 // 2.验证签名一致性
		String validate =report_name+report_province+report_start_date+report_end_date+report_items+report_create_persons+timestamp+key;
		String	newBuildValidateStr= MD5.MD5Crypt(validate);
		if (!StringUtils.equals(validateStr, newBuildValidateStr)) {		
			throw new RuntimeException("详情校验不通过!");
		}
		StringBuffer sb = new StringBuffer();
		String[] items = report_items.split(",");
			String item = items[0];
			if("SJKJ.24.17.5".equals(item)){
				params.put("report_items", "1001");
				subjectFileGenJob.setCommonSubjectFileGenProcessor(khqfFileGenProcessor);
			}else if("SJKJ.24.17.2".equals(item)){
				params.put("report_items", "2001");
				subjectFileGenJob.setCommonSubjectFileGenProcessor(qdcjFileGenProcessor);
			}else if("SJKJ.24.17.1".equals(item)){
				params.put("report_items", "3001");
				subjectFileGenJob.setCommonSubjectFileGenProcessor(yjkFileGenProcessor);
			}else{
				return "notHPpoint";
			}
		String reportPath = subjectFileGenJob.customReport(params);
		return reportPath;
	}
}
