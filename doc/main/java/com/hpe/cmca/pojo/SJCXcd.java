package com.hpe.cmca.pojo;

public class SJCXcd {
	
	private String firstClassMenu;//一级菜单名称
	private String subjectNum;//二级菜单序号
	private String secondMenuId;//二级菜单id
	private String secondLevelMenu;//二级菜单名称
	private String metadataTableId;//元数据表id
	private String metadataTableNm;//元数据表名称
	private String tableName;//数据表名称
	private String tableFilter;//筛选条件
	private String stateFlag;//0展示，1不展示
	

	public String getFirstClassMenu() {
		return firstClassMenu;
	}
	public void setFirstClassMenu(String firstClassMenu) {
		this.firstClassMenu = firstClassMenu;
	}
	public String getSubjectNum() {
		return subjectNum;
	}
	public void setSubjectNum(String subjectNum) {
		this.subjectNum = subjectNum;
	}
	public String getSecondMenuId() {
		return secondMenuId;
	}
	public void setSecondMenuId(String secondMenuId) {
		this.secondMenuId = secondMenuId;
	}
	public String getSecondLevelMenu() {
		return secondLevelMenu;
	}
	public void setSecondLevelMenu(String secondLevelMenu) {
		this.secondLevelMenu = secondLevelMenu;
	}
	public String getMetadataTableId() {
		return metadataTableId;
	}
	public void setMetadataTableId(String metadataTableId) {
		this.metadataTableId = metadataTableId;
	}
	public String getMetadataTableNm() {
		return metadataTableNm;
	}
	public void setMetadataTableNm(String metadataTableNm) {
		this.metadataTableNm = metadataTableNm;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableFilter() {
		return tableFilter;
	}
	public void setTableFilter(String tableFilter) {
		this.tableFilter = tableFilter;
	}
	public String getStateFlag() {
		return stateFlag;
	}
	public void setStateFlag(String stateFlag) {
		this.stateFlag = stateFlag;
	}
	@Override
	public String toString() {
		return "SJCXcd [firstClassMenu=" + firstClassMenu + ", subjectNum=" + subjectNum + ", secondMenuId="
				+ secondMenuId + ", secondLevelMenu=" + secondLevelMenu + ", metadataTableId=" + metadataTableId
				+ ", metadataTableNm=" + metadataTableNm + ", tableName=" + tableName + ", tableFilter=" + tableFilter
				+ ", stateFlag=" + stateFlag + "]";
	}

	
	
}
