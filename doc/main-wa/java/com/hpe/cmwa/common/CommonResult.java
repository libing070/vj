/**
 * com.hpe.cmwa.common.CommonResult.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.common;

import com.hpe.cmwa.util.Json;

/**
 * <pre>
 * Desc： 
 * @author   peter.fu
 * @refactor peter.fu
 * @date     Sep 12, 2016 11:01:58 AM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Sep 12, 2016 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
public class CommonResult {

	private String	status		= "0";			// 0-success 1-exception 2-overtime 3----
	private String	msg			= "成功";
	private int		pageNo		= 0;
	private int		pageSize	= 100;
	private Object	body		= new Object(); // map List<Map>

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getPageNo() {
		return this.pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Object getBody() {
		return this.body;
	}
	
	public void setBody(Object body) {
		this.body = body;
	}
	
	
	public static String success(Object result){
		CommonResult commonResult = new CommonResult();
		commonResult.setStatus("0");
		commonResult.setBody(result);
		return Json.Encode(commonResult);
	}

}
