/**
 * 
 * com.hpe.cmca.controller.RZCXController.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.common.CommonResult;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.util.CalendarUtils;
import com.hpe.cmca.util.HelperDate;
import com.hpe.cmca.util.Json;


/**
 * <pre>
 * Desc： 日志查询
 * @author   xuwenhu
 * @refactor xuwenhu
 * @date     2016-11-24 上午11:01:15
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2016-11-24 	   xuwenhu 	         1. Created this class. 
 * </pre>  
 */

@Controller
@RequestMapping("/rzcx")
public class RZCXController extends BaseController{
    @Autowired
    private MybatisDao mybatisDao;
    
//    @CmcaAuthority(authorityTypes = { AuthorityType.rzcx })//判断是否具有日志查询权限
    @RequestMapping(value = "index")
    public String index() {
	return "rzcx/index";
    }
    /**
     * 
     * <pre>
     * Desc  
     * @param request
     * @param uiModel
     * @return
     * @author xuwenhu
     * 2016-12-15 下午7:30:06
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "queryDefaultParams", produces = "plain/text; charset=UTF-8")
    public String queryDefaultParams(HttpServletRequest request, Model uiModel) {
	Map<String, Object> result = new HashMap<String, Object>();
	Date nowDate = new Date();
	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH:mm:ss"); 
	String now = df.format(nowDate);
	String date = now.substring(0, 6);//年月
//	result.put("date", date);
	result.put("prvdIds", "10000");//全国
//	result.put("logType", 1);//1-“审计通报”
	result.put("subjectId", 10);//全部
	result.put("operator", "");//全部
	result.put("audTrmEnd", date);//当前月
	//TODO
	int year = Integer.parseInt(date.substring(0,4))-1;
	result.put("audTrmStart", year+date.substring(4,6));//当前月-12
	return  Json.Encode(result);
    }
    
    /**
     * 
     * <pre>
     * Desc  
     * @param logType
     * @param audTrmStart
     * @param audTrmEnd
     * @param subjectid
     * @param operator
     * @return
     * @author xuwenhu
     * 2016-11-25 上午10:50:11
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getLogInfoList", produces = "plain/text; charset=UTF-8")  
    public String getLogInfoList(Integer logType, String audTrmStart, String audTrmEnd, String[] subjectId, String operator) {
	// Map<String, Object> result = new HashMap<String, Object>();
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("logType", logType);
	paramMap.put("audTrmEnd", audTrmEnd);
	paramMap.put("audTrmStart", audTrmStart);
	paramMap.put("subjectid", StringUtils.join(subjectId, ","));
//	if (logType == 4) {
//	    paramMap.put("subjectid", subjectId[0].equals("10")?subjectId[0]:StringUtils.join(subjectId, ",").replace("1", "'有价卡违规'").replace("2", "'渠道养卡'").replace("3", "'终端套利'"));
//	}
	
//	String operator_str = "";
//	try {
//	    if (operator != "") {
//		operator_str = new String(operator.getBytes("ISO-8859-1"), "UTF-8");//tomcat里配置了转码，这里就不需要再转码了。
//	    }
//	} catch (UnsupportedEncodingException e) {
//	    e.printStackTrace();
//	}
	paramMap.put("operator", operator);

	List<Map<String, Object>> info = mybatisDao.getList("rzcx.getLogInfoList", paramMap);

	return CommonResult.success(info);
    }
}
