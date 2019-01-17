	package com.hpe.cmca.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 	@Title 审计管理问责（折线、柱状、全国、地市）图实体类 
 *  @author admin
 *
 */
public class LlglwgForZgwzPojo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1572656692656235392L;
	
	private String auditMonth ;			//审计月
	private String provinceId ;			//省份ID
	private String company ;				//公司
	private String provinceName ;		//省份
	private String yearAndMonth ;		//年月（折线图）
	private BigDecimal currNumForColumn ;		//及时数据（柱状图数据专用）
	private BigDecimal currNumForLine	;			//及时数据（折线图数据专用）
	private String groupFlag ; 			//集团标识
	private String groupName ; 			//集团名称
	private String wgtm ;					//违规条目
	private String wgType ;				//违规类型
	
	
	//地图省市联动属性
	private String initPrvdId ;
	private String initPrvdNm ;
	private String initCityId ;
	private String initCityNm ;
	private BigDecimal initPrvdData ;
	private BigDecimal initCityData ;
	public LlglwgForZgwzPojo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LlglwgForZgwzPojo(String auditMonth, String provinceId, String company, String provinceName,
			String yearAndMonth, BigDecimal currNumForColumn, BigDecimal currNumForLine, String groupFlag,
			String groupName, String wgtm, String wgType) {
		super();
		this.auditMonth = auditMonth;
		this.provinceId = provinceId;
		this.company = company;
		this.provinceName = provinceName;
		this.yearAndMonth = yearAndMonth;
		this.currNumForColumn = currNumForColumn;
		this.currNumForLine = currNumForLine;
		this.groupFlag = groupFlag;
		this.groupName = groupName;
		this.wgtm = wgtm;
		this.wgType = wgType;
	}
	public String getAuditMonth() {
		return auditMonth;
	}
	public void setAuditMonth(String auditMonth) {
		this.auditMonth = auditMonth;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getYearAndMonth() {
		return yearAndMonth;
	}
	public void setYearAndMonth(String yearAndMonth) {
		this.yearAndMonth = yearAndMonth;
	}
	public BigDecimal getCurrNumForColumn() {
		return currNumForColumn;
	}
	public void setCurrNumForColumn(BigDecimal currNumForColumn) {
		this.currNumForColumn = currNumForColumn;
	}
	public BigDecimal getCurrNumForLine() {
		return currNumForLine;
	}
	public void setCurrNumForLine(BigDecimal currNumForLine) {
		this.currNumForLine = currNumForLine;
	}
	public String getGroupFlag() {
		return groupFlag;
	}
	public void setGroupFlag(String groupFlag) {
		this.groupFlag = groupFlag;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getWgtm() {
		return wgtm;
	}
	public void setWgtm(String wgtm) {
		this.wgtm = wgtm;
	}
	public String getWgType() {
		return wgType;
	}
	public void setWgType(String wgType) {
		this.wgType = wgType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getInitPrvdId() {
		return initPrvdId;
	}
	public void setInitPrvdId(String initPrvdId) {
		this.initPrvdId = initPrvdId;
	}
	public String getInitPrvdNm() {
		return initPrvdNm;
	}
	public void setInitPrvdNm(String initPrvdNm) {
		this.initPrvdNm = initPrvdNm;
	}
	public String getInitCityId() {
		return initCityId;
	}
	public void setInitCityId(String initCityId) {
		this.initCityId = initCityId;
	}
	public String getInitCityNm() {
		return initCityNm;
	}
	public void setInitCityNm(String initCityNm) {
		this.initCityNm = initCityNm;
	}
	public BigDecimal getInitPrvdData() {
		return initPrvdData;
	}
	public void setInitPrvdData(BigDecimal initPrvdData) {
		this.initPrvdData = initPrvdData;
	}
	public BigDecimal getInitCityData() {
		return initCityData;
	}
	public void setInitCityData(BigDecimal initCityData) {
		this.initCityData = initCityData;
	}
	
}
