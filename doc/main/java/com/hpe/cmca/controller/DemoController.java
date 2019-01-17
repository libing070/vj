/**
 * com.hpe.cmca.controller.DemoController.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.common.CommonResult;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.util.Json;

/**
 * <pre>
 * Desc： 
 * @author   peter.fu
 * @refactor peter.fu
 * @date     Sep 13, 2016 9:42:41 AM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Sep 13, 2016 	   peter.fu 	         1. Created this class. 
 * </pre>
 */

@Controller
@RequestMapping("/demo")
public class DemoController extends BaseController {

	@Autowired
	private MybatisDao	mybatisDao;
	
	 @RequestMapping(value = "uploadpage")
	    public String uploadpage() {
		return "csgl/test";
	    }

	/**
	 * 获取累计的“异常销售”和“套利”柱状图数据
	 * @param audTrm
	 * @param prvdIds
	 * @param ctyIds
	 * @param concernId
	 * @param attrName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSumColumn", produces = "plain/text; charset=UTF-8")
	public String getSumColumn(String audTrm, Integer[] prvdIds, Integer[] ctyIds, String concernId, String attrName) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("audTrm", audTrm);
		params.put("prvdIdStr", prvdIds != null ? StringUtils.join(prvdIds, ",") : "");
		params.put("ctyIdStr", ctyIds != null ? StringUtils.join(ctyIds, ",") : "");
		params.put("concernId", concernId);
		params.put("attrName", attrName);

		List<String> categories = new ArrayList<String>();
		List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
		if ("ycxssl".equals(attrName)) {

			List<Map<String, Object>> dataList = mybatisDao.getList("zdtl.getTLZDList", params);
			buildChartData(dataList,categories,values);
			return buildResult(categories, values);
		}
		if ("ycxszb".equals(attrName)) {

			List<Map<String, Object>> dataList = mybatisDao.getList("zdtl.getTLZDList", params);
			buildChartData(dataList,categories,values);
			return buildResult(categories, values);
		}
		if ("tlzdsl".equals(attrName)) {

			List<Map<String, Object>> dataList = mybatisDao.getList("zdtl.getTLZDList", params);
			buildChartData2(dataList,categories,values);
			return buildResult(categories, values);
		}
		if ("tlzdzb".equals(attrName)) {

			List<Map<String, Object>> dataList = mybatisDao.getList("zdtl.getTLZDList", params);
			buildChartData2(dataList,categories,values);
			return buildResult(categories, values);
		}

		Map<String, Object> result = new HashMap<String, Object>();
		return Json.Encode(CommonResult.success(result));
	}

	protected String buildResult(List<String> categories, List<Map<String, Object>> values) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("categories", categories);
		result.put("values", values);
		return Json.Encode(CommonResult.success(result));
	}

	private void buildChartData(List<Map<String, Object>> dataList, List<String> categories, List<Map<String, Object>> values) {
		
		// TODO Auto-generated method stub
		
	}
	
	private void buildChartData2(List<Map<String, Object>> dataList, List<String> categories, List<Map<String, Object>> values) {
		
		// TODO Auto-generated method stub
		
	}

	/**
	 * <pre>
	 * Desc 返回值样式：
	 * {
	 * 	"status":"0",
	 * 	"msg":"成功",
	 * 	"body":[
	 * 				{"name":"fusz1","value":"11111"},
	 * 				{"name":"fusz2","value":"22222"}
	 * 	],
	 * 	"pageNo":0,
	 * 	"pageSize":100
	 * } 
	 * @return
	 * @author peter.fu
	 * Sep 13, 2016 9:55:19 AM
	 * </pre>
	 */
	@RequestMapping(value = "testList")
	@ResponseBody
	@SuppressWarnings({ "rawtypes" })
	public CommonResult testList() {

		List dataList = queryDataListFromDB();
		CommonResult result = new CommonResult();
		result.setBody(dataList);
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List queryDataListFromDB() {

		List dataList = new ArrayList();
		Map row1 = new HashMap();
		row1.put("name", "fusz1");
		row1.put("value", "11111");
		dataList.add(row1);
		Map row2 = new HashMap();
		row2.put("name", "fusz2");
		row2.put("value", "22222");
		dataList.add(row2);

		return dataList;
	}

	/**
	 * <pre>
	 * 返回值样式：
	 * {
	 * 		"status":"0",
	 * 		"msg":"成功",
	 * 		"body":{
	 * 			"yyyyy":"yyyyy",
	 * 			"xxxx":"xxxxx",
	 * 			"subList":[
	 * 						{"name":"fusz1","value":"11111"},
	 * 						{"name":"fusz2","value":"22222"}
	 * 					]
	 * },
	 * "pageNo":0,
	 * "pageSize":100
	 * }  
	 * @return
	 * @author peter.fu
	 * Sep 13, 2016 9:55:48 AM
	 * </pre>
	 */
	@RequestMapping(value = "testMap")
	@ResponseBody
	@SuppressWarnings({ "rawtypes" })
	public CommonResult testMap() {

		Map dataMap = queryDataMapFromDB();

		CommonResult result = new CommonResult();
		result.setBody(dataMap);
		return result;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map queryDataMapFromDB() {

		List dataList = new ArrayList();
		Map row1 = new HashMap();
		row1.put("name", "fusz1");
		row1.put("value", "11111");
		dataList.add(row1);
		Map row2 = new HashMap();
		row2.put("name", "fusz2");
		row2.put("value", "22222");
		dataList.add(row2);

		Map dataMap = new HashMap();
		dataMap.put("xxxx", "xxxxx");
		dataMap.put("yyyyy", "yyyyy");

		dataMap.put("subList", dataList);

		return dataMap;
	}

}
