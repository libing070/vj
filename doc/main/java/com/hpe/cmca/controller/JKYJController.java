package com.hpe.cmca.controller;

/**
 * 监控预警
 */
import java.math.BigDecimal;
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
import org.springframework.web.servlet.ModelAndView;

import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.service.JkyjService;
import com.hpe.cmca.util.Json;

@Controller
@RequestMapping("/jkyj")
public class JKYJController extends BaseController {
	
	 @Autowired
	 protected JkyjService jkyjService;
	
	
	@RequestMapping(value = "jkyjPoint")
	public String jkyjPoint() {
		return "jkyj/jkyjPoint";
	}
	/**
	 * 获取所有专题信息
	 * <pre>
	 * Desc  
	 * @return
	 * @author issuser
	 * </pre>
	 */
	@ResponseBody
    @RequestMapping(value = "/getSubjectInfo", produces = "text/json;charset=UTF-8")
    public String getSubjectInfo(ParameterData p) {
	Map<String, Object> result = jkyjService.getSubjectInfo(p);
	return Json.Encode(result);
    }
	
	/**
	 * 获取专题的所有模块信息
	 * <pre>
	 * Desc  
	 * @return
	 * @author issuser
	 * </pre>
	 */
	@ResponseBody
    @RequestMapping(value = "/getModuleInfo", produces = "text/json;charset=UTF-8")
    public String getModuleInfo(ParameterData p) {
	List<Map<String, Object>> result = jkyjService.getModuleInfo(p);
	return Json.Encode(result);
    }
	
	/**
	 * 获取模块的所有关注点信息
	 * <pre>
	 * Desc  
	 * @return
	 * @author issuser
	 * </pre>
	 */
	@ResponseBody
    @RequestMapping(value = "/getConcernInfo", produces = "text/json;charset=UTF-8")
    public String getConcernInfo(ParameterData p) {
	List<Map<String, Object>> result = jkyjService.getConcernInfo(p);
	return Json.Encode(result);
    }
	
	/**
	 * 获取图标参数
	 * <pre>
	 * Desc  
	 * @return
	 * @author issuser
	 * </pre>
	 */
	@ResponseBody
    @RequestMapping(value = "/getChartInfo", produces = "text/json;charset=UTF-8")
    public String getChartInfo(ParameterData p) {
	Map<String, Object> result = jkyjService.getChartInfo(p);
	return Json.Encode(result);
    }
    
    /**
	 * 获取首页柱形图参数
	 * <pre>
	 * Desc  
	 * @return
	 * @author issuser
	 * </pre>
	 */
	@ResponseBody
    @RequestMapping(value = "/getColumnInfo", produces = "text/json;charset=UTF-8")
    public String getColumnInfo(ParameterData p) {
	Map<String, Object> result = jkyjService.getColumnInfo(p);
	return Json.Encode(result);
    }
	
    /**
	 * 获取首页折线图参数
	 * <pre>
	 * Desc  
	 * @return
	 * @author issuser
	 * </pre>
	 */
	@ResponseBody
    @RequestMapping(value = "/getLineInfo", produces = "text/json;charset=UTF-8")
    public String getLineInfo(ParameterData p) {
	Map<String, Object> result = jkyjService.getLineInfo(p);
	return Json.Encode(result);
    }
}
