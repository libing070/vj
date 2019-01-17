package com.hpe.cmca.model;

/**
 * 
 * <pre>
 * Desc： copy from cmccca for 参数管理-操作日志
 * @author GuoXY
 * @refactor GuoXY
 * @date   Dec 2, 2016 3:19:54 PM
 * @version 1.0
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Dec 2, 2016 	   GuoXY 	         1. Created this class. 
 * </pre>
 */
public class ParamEditLog {

	private String	edit_person;
	private String	edit_time;
	private String	edit_code;
	private String	edit_col;
	private String	old_id;
	private String	old_value;
	private String	new_id;
	private String	new_value;
	private String	edit_reason;

	public String getEdit_person() {
		return edit_person;
	}

	public void setEdit_person(String edit_person) {
		this.edit_person = edit_person;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public String getEdit_code() {
		return edit_code;
	}

	public void setEdit_code(String edit_code) {
		this.edit_code = edit_code;
	}

	public String getEdit_col() {
		return edit_col;
	}

	public void setEdit_col(String edit_col) {
		this.edit_col = edit_col;
	}

	public String getOld_id() {
		return old_id;
	}

	public void setOld_id(String old_id) {
		this.old_id = old_id;
	}

	public String getOld_value() {
		return old_value;
	}

	public void setOld_value(String old_value) {
		this.old_value = old_value;
	}

	public String getNew_id() {
		return new_id;
	}

	public void setNew_id(String new_id) {
		this.new_id = new_id;
	}

	public String getNew_value() {
		return new_value;
	}

	public void setNew_value(String new_value) {
		this.new_value = new_value;
	}

	public String getEdit_reason() {
		return edit_reason;
	}

	public void setEdit_reason(String edit_reason) {
		this.edit_reason = edit_reason;
	}

}
