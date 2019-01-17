/**
 * com.hpe.cmca.controller.QXCXController.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.pojo.LoginData;
import com.hpe.cmca.service.SSOService;
import com.hpe.cmca.util.Json;


/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2018-5-15 上午9:54:05
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2018-5-15 	   hufei 	         1. Created this class. 
 * </pre>  
 */
@Controller
@RequestMapping("/qxcx")
public class QXCXController {
    
	@Autowired
	SSOService ssoService;
	
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjqxcx})
    @RequestMapping(value = "index")
    public String index() {
	return "qxcx/index";
    }
    
    @RequestMapping(value = "indexMine")
    public String indexMine() {
	return "qxcx/index";
    }
	/**
	 * 
	 * <pre>
	 * 登录后，我的权限，查询 
	 * @param request
	 * @return
	 * @author sinly
	 * @refactor sinly
	 * @date   2018年5月23日 下午1:53:10
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "getUser", produces = "text/json;charset=UTF-8")
	public String getUserInfoDetail(HttpServletRequest request) {
		LoginData loginData = new LoginData();
		HttpSession session = request.getSession();
		loginData.setUserIds(Arrays.asList(session.getAttribute("userId").toString()));
		List<Map<String,Object>> resultList = ssoService.getLoginInfoDetail(loginData);
		Map<String,Object> endMap = new HashMap<String,Object>();
		endMap.put("controlData", resultList);
		return Json.Encode(endMap);
    	
    }
	
	/**
	 * 
	 * <pre>
	 * 登录后，我的权限，导出  
	 * @param request
	 * @param response
	 * @author sinly
	 * @refactor sinly
	 * @date   2018年5月23日 下午1:54:26
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "outPutUser", produces = "text/json;charset=UTF-8")
	public void outPutUserInfoDetail(HttpServletRequest request,HttpServletResponse response) {
		LoginData loginData = new LoginData();
		HttpSession session = request.getSession();
		loginData.setUserIds(Arrays.asList(session.getAttribute("userId").toString()));
		List<Map<String,Object>> resultList = ssoService.getLoginInfoDetail(loginData);
		ssoService.genReqExcel(resultList, request, response,session.getAttribute("userName").toString());
    	
    }
	
	/**
	 * 
	 * <pre>
	 * 权限查询  
	 * @param user
	 * @param prvd
	 * @param department
	 * @return
	 * @author sinly
	 * @refactor sinly
	 * @date   2018年5月23日 下午1:55:11
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "getUserByOption", produces = "text/json;charset=UTF-8")
	public String getUserByOption(String user,String prvd,String department) {
		
		LoginData loginData = new LoginData();
		if("".equals(user))loginData.setUserIds(null);else loginData.setUserIds(Arrays.asList(user.split(",")));
		if("".equals(prvd))loginData.setPrvdIds(null);else loginData.setPrvdIds(Arrays.asList(prvd.split(",")));
		if("".equals(department))loginData.setDepIds(null);else loginData.setDepIds(Arrays.asList(department.split(",")));
		List<Map<String,Object>> resultList = ssoService.getLoginInfoDetail(loginData);
		Map<String,Object> endMap = new HashMap<String,Object>();
		endMap.put("controlData", resultList);
		return Json.Encode(endMap);
    	
    }
	
	/**
	 * 
	 * <pre>
	 * 权限导出  
	 * @param user
	 * @param prvd
	 * @param department
	 * @param request
	 * @param response
	 * @author sinly
	 * @refactor sinly
	 * @date   2018年5月23日 下午1:55:35
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "outPutUserByOption", produces = "text/json;charset=UTF-8")
	public void outPutUserByOption(String user,String prvd,String department,HttpServletRequest request,HttpServletResponse response) {
		
		LoginData loginData = new LoginData();
		if("".equals(user))loginData.setUserIds(null);else loginData.setUserIds(Arrays.asList(user.split(",")));
		if("".equals(prvd))loginData.setPrvdIds(null);else loginData.setPrvdIds(Arrays.asList(prvd.split(",")));
		if("".equals(department))loginData.setDepIds(null);else loginData.setDepIds(Arrays.asList(department.split(",")));
		List<Map<String,Object>> resultList = ssoService.getLoginInfoDetail(loginData);
		ssoService.genReqExcel(resultList, request, response,"");
    	
    }
	
	/**
	 * 
	 * <pre>
	 * 我的权限下拉框信息  
	 * @return
	 * @author sinly
	 * @refactor sinly
	 * @date   2018年5月23日 下午2:00:31
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "getUserInfo", produces = "text/json;charset=UTF-8")
	public String getUserInfo() {
		
		Map<String,List<Map<String,Object>>> resultListMap = ssoService.getUserInfo();
		return Json.Encode(resultListMap);
    	
    }
	
	/**
	 * 
	 * <pre>
	 * 权限查询下拉框  
	 * @param request
	 * @return
	 * @author sinly
	 * @refactor sinly
	 * @date   2018年5月23日 下午2:00:53
	 * </pre>
	 */
	@ResponseBody
	@RequestMapping(value = "getUserInfoById", produces = "text/json;charset=UTF-8")
	public String getUserInfoById(HttpServletRequest request) {
		
		Map<String,List<Map<String,Object>>> resultListMap = ssoService.getUserInfoById(request);
		return Json.Encode(resultListMap);
    	
    }
}
