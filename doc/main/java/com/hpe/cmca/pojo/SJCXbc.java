package com.hpe.cmca.pojo;

public class SJCXbc {
	private String newSql;//sql语句
	private String createTime;//创建时间
	private String name;//标题名称
	private String createPerson;//创建人名称
	private String subSql;//用户输入的部分
	private String tablenm;//表名称，用于筛选符合条件的
	
	public String getSubSql() {
		return subSql;
	}
	public void setSubSql(String subSql) {
		this.subSql = subSql;
	}
	public String getTablenm() {
		return tablenm;
	}
	public void setTablenm(String tablenm) {
		this.tablenm = tablenm;
	}
	public String getNewSql() {
		return newSql;
	}
	public void setNewSql(String newSql) {
		this.newSql = newSql;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	
	
	
}
