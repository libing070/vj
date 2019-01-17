package com.hpe.cmca.pojo;

import java.io.Serializable;

public class SJCXSubject implements Serializable{
	
	
	private String tables;//涉及表名称
	private String columnNm;//字段名称
	private String columntitle;//字段注释
	private Integer columnRank;//字段顺序
	private String columntypeId;//字段类型简称
	private String columntype;//字段类型
	private Integer columnLength;//字段长度
	private String flag;//默认参数接受字段
	private String proColumn;//是否百分比展示
	private String nullColumn;//空值处理
	private String colunmHandle;//空值特殊处理
	public String getTables() {
		return tables;
	}
	public void setTables(String tables) {
		this.tables = tables;
	}
	public String getColumnNm() {
		return columnNm;
	}
	public void setColumnNm(String columnNm) {
		this.columnNm = columnNm;
	}
	public String getColumntitle() {
		return columntitle;
	}
	public void setColumntitle(String columntitle) {
		this.columntitle = columntitle;
	}
	public Integer getColumnRank() {
		return columnRank;
	}
	public void setColumnRank(Integer columnRank) {
		this.columnRank = columnRank;
	}
	public String getColumntypeId() {
		return columntypeId;
	}
	public void setColumntypeId(String columntypeId) {
		this.columntypeId = columntypeId;
	}
	public String getColumntype() {
		return columntype;
	}
	public void setColumntype(String columntype) {
		this.columntype = columntype;
	}
	public Integer getColumnLength() {
		return columnLength;
	}
	public void setColumnLength(Integer columnLength) {
		this.columnLength = columnLength;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getProColumn() {
		return proColumn;
	}
	public void setProColumn(String proColumn) {
		this.proColumn = proColumn;
	}
	public String getNullColumn() {
		return nullColumn;
	}
	public void setNullColumn(String nullColumn) {
		this.nullColumn = nullColumn;
	}
	public String getColunmHandle() {
		return colunmHandle;
	}
	public void setColunmHandle(String colunmHandle) {
		this.colunmHandle = colunmHandle;
	}
	
	
}
