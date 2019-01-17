package com.hpe.cmca.pojo;

import java.math.BigDecimal;

/**
 * @Title 装载违规公司的所有属性值
 * @author admin
 *
 */
public class LlglwgCompanyInfoPojo {

	
	private String orgCustId ; 	//集团客户标识
	private String companyName ;	//集团客户名称
	private String formName; //业务形态
	private String modeName ;	//统付方式
	private String prvdName ; //公司（省份）
	private String city ; //集团所在地市名称
	private String accountAudTrm  ; //违规审计月
	
	private String audTrm ; //审计月
	private String prvdId ;	//省份标识
	private String cityId ;	//集团所在地市标识
	private String isNotWgCurrMonth ; 	//当前月份是否违规
	private String tfSum ; 	//统付流量总值
	private String tfSumStrmPrvdPer ; //统付流量占该省统付总流量比
	private String tfSumStrmAllerrPer ;	//统付流量占当月全集团违规转售总流量比
	private int tfSumCnt ;	//统付总次数
	private int tfCntMsisdn ;	//转入号码数
	private String avgStrm ; //'每用户平均统付流量值（G）
	private String memberStabRate ; 	//成员稳定率
	private String ctyLevelRate ; //本地市集中度
	private String  a3 ;	//业务种类是否单一
	private int SumDt ; 	//操作天数
	private int duociSum ; //多次订购用户数
	private int duochuSum ;	//多处订购用户数
	
	/*-----------------------------------------【重点关注集团属性】--------------------------------------------------------*/
	private String focusOrgId ; 	//集团ID
	private String focusOrgName ;		//名称
	private String focusPrvdId ;			//省份Id
	private String focusPrvdName ;	//省份名称
	private String currIsNotWg ;		//当前月份是否违规
	private String currAudTrm ;		//当前违规审计月
	//数组下标默认值
	private int currIndex  = 3 ;
	private BigDecimal currTfSum ;	//当前月统付流量总值
	private BigDecimal currTfSumStrmPrvdPer ; //当前月统付流量占该省统付总流量比
	private BigDecimal currTfSumStrmAllerrPer ;	//当前月统付流量占当月全集团违规转售总流量比
	private int oneMonthAgoIndex  = 2 ;
	private String oneMonthAgoIsNotWg ;		//上月月份是否违规
	private String oneMonthAgoAudTrm ;		//上月违规审计月
	private BigDecimal oneMonthAgoTfSum ;	//上月统付流量总值
	private BigDecimal oneMonthAgoTfSumStrmPrvdPer ; //上月统付流量占该省统付总流量比
	private BigDecimal oneMonthAgoTfSumStrmAllerrPer ;	//上月统付流量占当月全集团违规转售总流量比
	private int twoMonthAgoIndex  = 1 ;
	private String twoMonthAgoIsNotWg ;		//两个月前是否违规
	private String twoMonthAgoAudTrm ;		//两个月前违规审计月
	private BigDecimal twoMonthAgoTfSum ;	//两个月前统付流量总值
	private BigDecimal twoMonthAgoTfSumStrmPrvdPer ; //两个月前统付流量占该省统付总流量比
	private BigDecimal twoMonthAgoTfSumStrmAllerrPer ;	//两个月前统付流量占当月全集团违规转售总流量比
	
	public LlglwgCompanyInfoPojo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getOrgCustId() {
		return orgCustId;
	}

	public void setOrgCustId(String orgCustId) {
		this.orgCustId = orgCustId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getModeName() {
		return modeName;
	}

	public void setModeName(String modeName) {
		this.modeName = modeName;
	}

	public String getPrvdName() {
		return prvdName;
	}

	public void setPrvdName(String prvdName) {
		this.prvdName = prvdName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAccountAudTrm() {
		return accountAudTrm;
	}

	public void setAccountAudTrm(String accountAudTrm) {
		this.accountAudTrm = accountAudTrm;
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

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getIsNotWgCurrMonth() {
		return isNotWgCurrMonth;
	}

	public void setIsNotWgCurrMonth(String isNotWgCurrMonth) {
		this.isNotWgCurrMonth = isNotWgCurrMonth;
	}

	public String getTfSum() {
		return tfSum;
	}

	public void setTfSum(String tfSum) {
		this.tfSum = tfSum;
	}

	public String getTfSumStrmPrvdPer() {
		return tfSumStrmPrvdPer;
	}

	public void setTfSumStrmPrvdPer(String tfSumStrmPrvdPer) {
		this.tfSumStrmPrvdPer = tfSumStrmPrvdPer;
	}

	public String getTfSumStrmAllerrPer() {
		return tfSumStrmAllerrPer;
	}

	public void setTfSumStrmAllerrPer(String tfSumStrmAllerrPer) {
		this.tfSumStrmAllerrPer = tfSumStrmAllerrPer;
	}

	public int getTfSumCnt() {
		return tfSumCnt;
	}

	public void setTfSumCnt(int tfSumCnt) {
		this.tfSumCnt = tfSumCnt;
	}

	public int getTfCntMsisdn() {
		return tfCntMsisdn;
	}

	public void setTfCntMsisdn(int tfCntMsisdn) {
		this.tfCntMsisdn = tfCntMsisdn;
	}

	public String getAvgStrm() {
		return avgStrm;
	}

	public void setAvgStrm(String avgStrm) {
		this.avgStrm = avgStrm;
	}

	public String getMemberStabRate() {
		return memberStabRate;
	}

	public void setMemberStabRate(String memberStabRate) {
		this.memberStabRate = memberStabRate;
	}

	public String getCtyLevelRate() {
		return ctyLevelRate;
	}

	public void setCtyLevelRate(String ctyLevelRate) {
		this.ctyLevelRate = ctyLevelRate;
	}

	public String getA3() {
		return a3;
	}

	public void setA3(String a3) {
		this.a3 = a3;
	}

	public int getSumDt() {
		return SumDt;
	}

	public void setSumDt(int sumDt) {
		SumDt = sumDt;
	}

	public int getDuociSum() {
		return duociSum;
	}

	public void setDuociSum(int duociSum) {
		this.duociSum = duociSum;
	}

	public int getDuochuSum() {
		return duochuSum;
	}

	public void setDuochuSum(int duochuSum) {
		this.duochuSum = duochuSum;
	}

	public String getFocusOrgId() {
		return focusOrgId;
	}

	public void setFocusOrgId(String focusOrgId) {
		this.focusOrgId = focusOrgId;
	}

	public String getFocusOrgName() {
		return focusOrgName;
	}

	public void setFocusOrgName(String focusOrgName) {
		this.focusOrgName = focusOrgName;
	}

	public String getFocusPrvdId() {
		return focusPrvdId;
	}

	public void setFocusPrvdId(String focusPrvdId) {
		this.focusPrvdId = focusPrvdId;
	}

	public String getFocusPrvdName() {
		return focusPrvdName;
	}

	public void setFocusPrvdName(String focusPrvdName) {
		this.focusPrvdName = focusPrvdName;
	}

	public String getCurrIsNotWg() {
		return currIsNotWg;
	}

	public void setCurrIsNotWg(String currIsNotWg) {
		this.currIsNotWg = currIsNotWg;
	}

	public String getCurrAudTrm() {
		return currAudTrm;
	}

	public void setCurrAudTrm(String currAudTrm) {
		this.currAudTrm = currAudTrm;
	}

	public BigDecimal getCurrTfSum() {
		return currTfSum;
	}

	public void setCurrTfSum(BigDecimal currTfSum) {
		this.currTfSum = currTfSum;
	}

	public BigDecimal getCurrTfSumStrmPrvdPer() {
		return currTfSumStrmPrvdPer;
	}

	public void setCurrTfSumStrmPrvdPer(BigDecimal currTfSumStrmPrvdPer) {
		this.currTfSumStrmPrvdPer = currTfSumStrmPrvdPer;
	}

	public BigDecimal getCurrTfSumStrmAllerrPer() {
		return currTfSumStrmAllerrPer;
	}

	public void setCurrTfSumStrmAllerrPer(BigDecimal currTfSumStrmAllerrPer) {
		this.currTfSumStrmAllerrPer = currTfSumStrmAllerrPer;
	}

	public String getOneMonthAgoIsNotWg() {
		return oneMonthAgoIsNotWg;
	}

	public void setOneMonthAgoIsNotWg(String oneMonthAgoIsNotWg) {
		this.oneMonthAgoIsNotWg = oneMonthAgoIsNotWg;
	}

	public String getOneMonthAgoAudTrm() {
		return oneMonthAgoAudTrm;
	}

	public void setOneMonthAgoAudTrm(String oneMonthAgoAudTrm) {
		this.oneMonthAgoAudTrm = oneMonthAgoAudTrm;
	}

	public BigDecimal getOneMonthAgoTfSum() {
		return oneMonthAgoTfSum;
	}

	public void setOneMonthAgoTfSum(BigDecimal oneMonthAgoTfSum) {
		this.oneMonthAgoTfSum = oneMonthAgoTfSum;
	}

	public BigDecimal getOneMonthAgoTfSumStrmPrvdPer() {
		return oneMonthAgoTfSumStrmPrvdPer;
	}

	public void setOneMonthAgoTfSumStrmPrvdPer(BigDecimal oneMonthAgoTfSumStrmPrvdPer) {
		this.oneMonthAgoTfSumStrmPrvdPer = oneMonthAgoTfSumStrmPrvdPer;
	}

	public BigDecimal getOneMonthAgoTfSumStrmAllerrPer() {
		return oneMonthAgoTfSumStrmAllerrPer;
	}

	public void setOneMonthAgoTfSumStrmAllerrPer(BigDecimal oneMonthAgoTfSumStrmAllerrPer) {
		this.oneMonthAgoTfSumStrmAllerrPer = oneMonthAgoTfSumStrmAllerrPer;
	}

	public String getTwoMonthAgoIsNotWg() {
		return twoMonthAgoIsNotWg;
	}

	public void setTwoMonthAgoIsNotWg(String twoMonthAgoIsNotWg) {
		this.twoMonthAgoIsNotWg = twoMonthAgoIsNotWg;
	}

	public String getTwoMonthAgoAudTrm() {
		return twoMonthAgoAudTrm;
	}

	public void setTwoMonthAgoAudTrm(String twoMonthAgoAudTrm) {
		this.twoMonthAgoAudTrm = twoMonthAgoAudTrm;
	}

	public BigDecimal getTwoMonthAgoTfSum() {
		return twoMonthAgoTfSum;
	}

	public void setTwoMonthAgoTfSum(BigDecimal twoMonthAgoTfSum) {
		this.twoMonthAgoTfSum = twoMonthAgoTfSum;
	}

	public BigDecimal getTwoMonthAgoTfSumStrmPrvdPer() {
		return twoMonthAgoTfSumStrmPrvdPer;
	}

	public void setTwoMonthAgoTfSumStrmPrvdPer(BigDecimal twoMonthAgoTfSumStrmPrvdPer) {
		this.twoMonthAgoTfSumStrmPrvdPer = twoMonthAgoTfSumStrmPrvdPer;
	}

	public BigDecimal getTwoMonthAgoTfSumStrmAllerrPer() {
		return twoMonthAgoTfSumStrmAllerrPer;
	}

	public void setTwoMonthAgoTfSumStrmAllerrPer(BigDecimal twoMonthAgoTfSumStrmAllerrPer) {
		this.twoMonthAgoTfSumStrmAllerrPer = twoMonthAgoTfSumStrmAllerrPer;
	}

	public int getCurrIndex() {
		return currIndex;
	}

	public int getOneMonthAgoIndex() {
		return oneMonthAgoIndex;
	}

	public int getTwoMonthAgoIndex() {
		return twoMonthAgoIndex;
	}


}
