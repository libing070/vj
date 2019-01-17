package com.hpe.cmca.pojo;


public class WordModelConfig{
	private String wordCode;
	private String wordName;
	private Integer versionCode;
	private Integer blockOrder;
	private String querySql;
	private Integer showType;
	private String showSql;
	private Integer mergeLast;
	private String titleLevel;
	private Integer isFullVersion;
	private String reviser;//修订人
	private String effectiveTime;//生效时间

	public String getWordCode() {
		return wordCode;
	}

	public void setWordCode(String wordCode) {
		this.wordCode = wordCode;
	}

	public String getWordName() {
		return wordName;
	}

	public void setWordName(String wordName) {
		this.wordName = wordName;
	}

	public Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}

	public Integer getBlockOrder() {
		return blockOrder;
	}

	public void setBlockOrder(Integer blockOrder) {
		this.blockOrder = blockOrder;
	}

	public String getQuerySql() {
		return querySql;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}

	public String getShowSql() {
		return showSql;
	}

	public void setShowSql(String showSql) {
		this.showSql = showSql;
	}

	public Integer getMergeLast() {
		return mergeLast;
	}

	public void setMergeLast(Integer mergeLast) {
		this.mergeLast = mergeLast;
	}

	public String getTitleLevel() {
		return titleLevel;
	}

	public void setTitleLevel(String titleLevel) {
		this.titleLevel = titleLevel;
	}

	public Integer getIsFullVersion() {
		return isFullVersion;
	}

	public void setIsFullVersion(Integer isFullVersion) {
		this.isFullVersion = isFullVersion;
	}

	public String getReviser() {
		return reviser;
	}

	public void setReviser(String reviser) {
		this.reviser = reviser;
	}

	public String getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
}
