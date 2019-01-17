package com.hpe.cmca.pojo;

import java.util.Date;

public class DataCompare {
	private Integer id;
	private Integer orderNum;
	private String fieldName;
	private String wordValue;
	private String wordName;
	private String excelName;
	private String excelValue;
	private String compareResult;
	private String operPerson;
	private Date createDatetime;
	private Integer isdel;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getWordValue() {
		return wordValue;
	}
	public void setWordValue(String wordValue) {
		this.wordValue = wordValue;
	}
	public String getExcelName() {
		return excelName;
	}
	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}
	public String getCompareResult() {
		return compareResult;
	}
	public void setCompareResult(String compareResult) {
		this.compareResult = compareResult;
	}
	public String getOperPerson() {
		return operPerson;
	}
	public void setOperPerson(String operPerson) {
		this.operPerson = operPerson;
	}
	public Date getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}
	public String getExcelValue() {
		return excelValue;
	}
	public void setExcelValue(String excelValue) {
		this.excelValue = excelValue;
	}
	public String getWordName() {
		return wordName;
	}
	public void setWordName(String wordName) {
		this.wordName = wordName;
	}
	public Integer getIsdel() {
		return isdel;
	}
	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}
	

}
