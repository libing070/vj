package com.hpe.cmca.pojo;

import java.math.BigDecimal;

/**
 * 电子券chart数据实体
 * @author yuzn1
 */
public class DzqChartPojo {
	
	/**
	 * 图形界面实体（柱图，折线，风向地图）
	 */
	private String audTrm;//审计月
	private Integer prvdId;//(省或地市编码)id
	private String  name;//名称
	private BigDecimal  amts;//金额
	private BigDecimal  pers;//占比
	private Integer nums;//数量
	private Integer rn;//数量
	
	
	public Integer getRn() {
		return rn;
	}
	public void setRn(Integer rn) {
		this.rn = rn;
	}
	public String getAudTrm() {
		return audTrm;
	}
	public void setAudTrm(String audTrm) {
		this.audTrm = audTrm;
	}
	public Integer getPrvdId() {
		return prvdId;
	}
	public void setPrvdId(Integer prvdId) {
		this.prvdId = prvdId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getNums() {
		return nums;
	}
	public void setNums(Integer nums) {
		this.nums = nums;
	}
	public BigDecimal getAmts() {
		return amts;
	}
	public void setAmts(BigDecimal amts) {
		this.amts = amts;
	}
	public BigDecimal getPers() {
		return pers;
	}
	public void setPers(BigDecimal pers) {
		this.pers = pers;
	}
	
	
	
	
	
	

}
