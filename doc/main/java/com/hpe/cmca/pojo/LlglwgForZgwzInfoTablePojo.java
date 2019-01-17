package com.hpe.cmca.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Title 审计整改及问责事项明细表属性、
 * @author admin
 *
 */
public class LlglwgForZgwzInfoTablePojo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6434133126845782931L;
	
	private int rn ;									//序号
	private String audTrm ; 					//审计月
	private String zgaudTrm ;					//整改审计月
	private String prvdId ;						//省份
	private String prvdName ; 				//公司（省份）
	private String orgNm ;						//集团客户名称
	private String orgCustId ;					//集团客户ID
	private BigDecimal prvdTfllSum ;		//ZG1省公司统付流量总值(TB)
	private BigDecimal prvdYszsSum ; 	//ZG1省公司疑似违规转售流量总值（TB）
	private BigDecimal zg1pe ;			 	// '占比',
	private BigDecimal orgYszsllSum ;	//ZG2单一集团客户疑似违规转售流量总值（TB）
	private BigDecimal allOrgWgzsSum ;//'ZG2全集团违规转售流量（TB）
	private BigDecimal zg2pe	;				//占比
	private String wgAud	;					//违规审计月
	private String wgType ;					//违规类型
	private String wzType ;						//问责类型
	private String wgTm ;						//违规条目
	public int getRn() {
		return rn;
	}
	public void setRn(int rn) {
		this.rn = rn;
	}
	public String getAudTrm() {
		return audTrm;
	}
	public void setAudTrm(String audTrm) {
		this.audTrm = audTrm;
	}
	public String getPrvdId() {
		return prvdId;
	}
	public void setPrvdId(String prvdId) {
		this.prvdId = prvdId;
	}
	public String getPrvdName() {
		return prvdName;
	}
	public void setPrvdName(String prvdName) {
		this.prvdName = prvdName;
	}
	public String getOrgNm() {
		return orgNm;
	}
	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	public String getOrgCustId() {
		return orgCustId;
	}
	public void setOrgCustId(String orgCustId) {
		this.orgCustId = orgCustId;
	}
	public BigDecimal getPrvdTfllSum() {
		return prvdTfllSum;
	}
	public void setPrvdTfllSum(BigDecimal prvdTfllSum) {
		this.prvdTfllSum = prvdTfllSum;
	}
	public BigDecimal getPrvdYszsSum() {
		return prvdYszsSum;
	}
	public void setPrvdYszsSum(BigDecimal prvdYszsSum) {
		this.prvdYszsSum = prvdYszsSum;
	}
	public BigDecimal getZg1pe() {
		return zg1pe;
	}
	public void setZg1pe(BigDecimal zg1pe) {
		this.zg1pe = zg1pe;
	}
	public BigDecimal getOrgYszsllSum() {
		return orgYszsllSum;
	}
	public void setOrgYszsllSum(BigDecimal orgYszsllSum) {
		this.orgYszsllSum = orgYszsllSum;
	}
	public BigDecimal getAllOrgWgzsSum() {
		return allOrgWgzsSum;
	}
	public void setAllOrgWgzsSum(BigDecimal allOrgWgzsSum) {
		this.allOrgWgzsSum = allOrgWgzsSum;
	}
	public BigDecimal getZg2pe() {
		return zg2pe;
	}
	public void setZg2pe(BigDecimal zg2pe) {
		this.zg2pe = zg2pe;
	}
	public String getWgAud() {
		return wgAud;
	}
	public void setWgAud(String wgAud) {
		this.wgAud = wgAud;
	}
	public String getWgType() {
		return wgType;
	}
	public void setWgType(String wgType) {
		this.wgType = wgType;
	}
	public String getWzType() {
		return wzType;
	}
	public void setWzType(String wzType) {
		this.wzType = wzType;
	}
	public String getWgTm() {
		return wgTm;
	}
	public void setWgTm(String wgTm) {
		this.wgTm = wgTm;
	}
	public String getZgaudTrm() {
		return zgaudTrm;
	}
	public void setZgaudTrm(String zgaudTrm) {
		this.zgaudTrm = zgaudTrm;
	}
}
