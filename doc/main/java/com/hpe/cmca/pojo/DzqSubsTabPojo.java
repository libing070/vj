package com.hpe.cmca.pojo;

/**
 * 风险地图下钻  用户数table
 * @author yuzn1
 */
public class DzqSubsTabPojo {
	
	private  Integer   rankChina; 			// '总金额排名(全国)',
    private  Integer   rankPrvd; 			// '总金额排名(省内)',
	private  Integer   cmccProvPrvdId; 		// 'CMCC省公司标识',
    private  String    cmccProvPrvdNm; 		// 'CMCC省公司名称',
	private  String    cmccPrvdId; 			// '用户所在地市公司' ,
	private  String    cmccPrvdNm; 			// '用户所在地市公司' ,
	private  String    subsId; 				// '用户标识' ,
	private  Integer   cmccErrNum;			// '异常总次数' ,
	private  Double    cmccSumAmt; 			// '电子券赠送总金额(万元)',
	private  Integer   cmccSumNum; 		// '电子券发放总次数(次)',
	private  Double    cmccErrAmts; 		// '异常赠送总金额(万元)',
	private  Double    cmccErrAmtPer; 		// '异常赠送金额占比(0.zzz9)',
	
	 private  String    isBig; 			// '是否是大额用户',
	 private  String    isHigh; 		// '是否是高频用户',
	 private  String    isException; 	// '是否是异常用户',
	
	
	
	
	public Integer getCmccErrNum() {
		return cmccErrNum;
	}
	public void setCmccErrNum(Integer cmccErrNum) {
		this.cmccErrNum = cmccErrNum;
	}
	public Integer getRankChina() {
		return rankChina;
	}
	public void setRankChina(Integer rankChina) {
		this.rankChina = rankChina;
	}
	public Integer getRankPrvd() {
		return rankPrvd;
	}
	public void setRankPrvd(Integer rankPrvd) {
		this.rankPrvd = rankPrvd;
	}
	public Integer getCmccProvPrvdId() {
		return cmccProvPrvdId;
	}
	public void setCmccProvPrvdId(Integer cmccProvPrvdId) {
		this.cmccProvPrvdId = cmccProvPrvdId;
	}
	public String getCmccProvPrvdNm() {
		return cmccProvPrvdNm;
	}
	public void setCmccProvPrvdNm(String cmccProvPrvdNm) {
		this.cmccProvPrvdNm = cmccProvPrvdNm;
	}
	public String getCmccPrvdId() {
		return cmccPrvdId;
	}
	public void setCmccPrvdId(String cmccPrvdId) {
		this.cmccPrvdId = cmccPrvdId;
	}
	public String getCmccPrvdNm() {
		return cmccPrvdNm;
	}
	public void setCmccPrvdNm(String cmccPrvdNm) {
		this.cmccPrvdNm = cmccPrvdNm;
	}
	public String getSubsId() {
		return subsId;
	}
	public void setSubsId(String subsId) {
		this.subsId = subsId;
	}
	public Double getCmccSumAmt() {
		return cmccSumAmt;
	}
	public void setCmccSumAmt(Double cmccSumAmt) {
		this.cmccSumAmt = cmccSumAmt;
	}
	public Double getCmccErrAmts() {
		return cmccErrAmts;
	}
	public void setCmccErrAmts(Double cmccErrAmts) {
		this.cmccErrAmts = cmccErrAmts;
	}
	public Double getCmccErrAmtPer() {
		return cmccErrAmtPer;
	}
	public void setCmccErrAmtPer(Double cmccErrAmtPer) {
		this.cmccErrAmtPer = cmccErrAmtPer;
	}
	public Integer getCmccSumNum() {
		return cmccSumNum;
	}
	public void setCmccSumNum(Integer cmccSumNum) {
		this.cmccSumNum = cmccSumNum;
	}
	public String getIsBig() {
		return isBig;
	}
	public void setIsBig(String isBig) {
		this.isBig = isBig;
	}
	public String getIsHigh() {
		return isHigh;
	}
	public void setIsHigh(String isHigh) {
		this.isHigh = isHigh;
	}
	public String getIsException() {
		return isException;
	}
	public void setIsException(String isException) {
		this.isException = isException;
	}
	
	

}
