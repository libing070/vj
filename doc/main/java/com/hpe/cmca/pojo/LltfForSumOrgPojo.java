package com.hpe.cmca.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Title 装载自201809审计月被判定为疑似转售流量的审计月对应数据实体类
 * @author admin
 *
 */

public class LltfForSumOrgPojo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2147689197252235873L;
	
	
	private BigDecimal rankOrg ; 								//排名
	private BigDecimal rankPrvd;							 	//省排名
	private String audTrm ; 										//审计月
	private BigDecimal prvdId ;									//省份编号
	private String prvdName ;									//省份名称
	private BigDecimal cityId ;									//城市编码
	private String cityName ;										//城市名称
	private String busnMode ;									//统付方式
	private String busnForm ;										//业务形态
	private String 	orgNm ;										//集团客户名称
	private String orgCustId ;										//集团客户标识
	private BigDecimal sumStrmCap ;						//疑似转售流量总值
	private BigDecimal strmCapPer ;							//疑似转售流量占该省统付总流量占比
	private BigDecimal sumCnt ;								//统付总次数
	private BigDecimal sumMission ;							//转入号码数
	private  BigDecimal avgStrmCap ; 						//用户平均统付流量值（G）
	private BigDecimal memberStabRate ;					//成员稳定率
	private BigDecimal ctyLevelRate ; 						//本市集中度
	private String a3 ;													//业务种类是否单一
	private BigDecimal sumDt ;									//操作天数
	private BigDecimal duociSum ;								//多次订购用户数
	private BigDecimal duochuSum ;							//多处订购用户数
	public LltfForSumOrgPojo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LltfForSumOrgPojo(BigDecimal rankOrg, BigDecimal rankPrvd, String audTrm, BigDecimal prvdId, String prvdName,
			BigDecimal cityId, String cityName, String busnMode, String busnForm, String orgNm, String orgCustId,
			BigDecimal sumStrmCap, BigDecimal strmCapPer, BigDecimal sumCnt, BigDecimal sumMission,
			BigDecimal avgStrmCap, BigDecimal memberStabRate, BigDecimal ctyLevelRate, String a3, BigDecimal sumDt,
			BigDecimal duociSum, BigDecimal duochuSum) {
		super();
		this.rankOrg = rankOrg;
		this.rankPrvd = rankPrvd;
		this.audTrm = audTrm;
		this.prvdId = prvdId;
		this.prvdName = prvdName;
		this.cityId = cityId;
		this.cityName = cityName;
		this.busnMode = busnMode;
		this.busnForm = busnForm;
		this.orgNm = orgNm;
		this.orgCustId = orgCustId;
		this.sumStrmCap = sumStrmCap;
		this.strmCapPer = strmCapPer;
		this.sumCnt = sumCnt;
		this.sumMission = sumMission;
		this.avgStrmCap = avgStrmCap;
		this.memberStabRate = memberStabRate;
		this.ctyLevelRate = ctyLevelRate;
		this.a3 = a3;
		this.sumDt = sumDt;
		this.duociSum = duociSum;
		this.duochuSum = duochuSum;
	}
	public BigDecimal getRankOrg() {
		return rankOrg;
	}
	public void setRankOrg(BigDecimal rankOrg) {
		this.rankOrg = rankOrg;
	}
	public BigDecimal getRankPrvd() {
		return rankPrvd;
	}
	public void setRankPrvd(BigDecimal rankPrvd) {
		this.rankPrvd = rankPrvd;
	}
	public String getAudTrm() {
		return audTrm;
	}
	public void setAudTrm(String audTrm) {
		this.audTrm = audTrm;
	}
	public BigDecimal getPrvdId() {
		return prvdId;
	}
	public void setPrvdId(BigDecimal prvdId) {
		this.prvdId = prvdId;
	}
	public String getPrvdName() {
		return prvdName;
	}
	public void setPrvdName(String prvdName) {
		this.prvdName = prvdName;
	}
	public BigDecimal getCityId() {
		return cityId;
	}
	public void setCityId(BigDecimal cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getBusnMode() {
		return busnMode;
	}
	public void setBusnMode(String busnMode) {
		this.busnMode = busnMode;
	}
	public String getBusnForm() {
		return busnForm;
	}
	public void setBusnForm(String busnForm) {
		this.busnForm = busnForm;
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
	public BigDecimal getSumStrmCap() {
		return sumStrmCap;
	}
	public void setSumStrmCap(BigDecimal sumStrmCap) {
		this.sumStrmCap = sumStrmCap;
	}
	public BigDecimal getStrmCapPer() {
		return strmCapPer;
	}
	public void setStrmCapPer(BigDecimal strmCapPer) {
		this.strmCapPer = strmCapPer;
	}
	public BigDecimal getSumCnt() {
		return sumCnt;
	}
	public void setSumCnt(BigDecimal sumCnt) {
		this.sumCnt = sumCnt;
	}
	public BigDecimal getSumMission() {
		return sumMission;
	}
	public void setSumMission(BigDecimal sumMission) {
		this.sumMission = sumMission;
	}
	public BigDecimal getAvgStrmCap() {
		return avgStrmCap;
	}
	public void setAvgStrmCap(BigDecimal avgStrmCap) {
		this.avgStrmCap = avgStrmCap;
	}
	public BigDecimal getMemberStabRate() {
		return memberStabRate;
	}
	public void setMemberStabRate(BigDecimal memberStabRate) {
		this.memberStabRate = memberStabRate;
	}
	public BigDecimal getCtyLevelRate() {
		return ctyLevelRate;
	}
	public void setCtyLevelRate(BigDecimal ctyLevelRate) {
		this.ctyLevelRate = ctyLevelRate;
	}
	public String getA3() {
		return a3;
	}
	public void setA3(String a3) {
		this.a3 = a3;
	}
	public BigDecimal getSumDt() {
		return sumDt;
	}
	public void setSumDt(BigDecimal sumDt) {
		this.sumDt = sumDt;
	}
	public BigDecimal getDuociSum() {
		return duociSum;
	}
	public void setDuociSum(BigDecimal duociSum) {
		this.duociSum = duociSum;
	}
	public BigDecimal getDuochuSum() {
		return duochuSum;
	}
	public void setDuochuSum(BigDecimal duochuSum) {
		this.duochuSum = duochuSum;
	}

}
