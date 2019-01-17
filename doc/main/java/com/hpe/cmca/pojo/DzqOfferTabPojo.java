package com.hpe.cmca.pojo;

/**
 * 重点关注营销案和风险地图数据表实体
 * @author yuzn1
 */
public class DzqOfferTabPojo {
	
	private Integer  	rankChina; 		  	  // '金额占比排名(全国)',
    private Integer  	rankPrvd; 		  	  // '金额占比排名(省内)',
	private Integer  	rankChinaNum; 		  // '异常金额排名(全国)',
    private Integer  	rankPrvdNum; 		  // '异常金额排名(省内)',
    private Integer  	rankChinaAmt; 		  // '异常金额排名(全国)',
    private Integer  	rankPrvdAmt; 		  // '异常金额排名(省内)',
	private String  	offerId; 			  // '营销案编码',
    private String  	offerNm; 			  // '营销案案名称',
	private Integer  	cmccProvPrvdId;       // '省公司标识',
    private String  	cmccProvPrvdNm; 	  // '省公司',
	private Integer  	offerCtyId; 		  // '地市编码     营销案归属地市编码',
    private String  	offerCtyNm; 		  // '地市名称     营销案归属地市名称',
	private Double  	offerSumAmt; 		  // '发放总金额(万元)  营销案赠送总金额',
	private Integer  	offerSumNum;		  // '发放总次数(次)'  营销案赠送总次数, 
	private Integer  	offerSumSubs; 	      // '发放涉及用户数(位)'
	private Double  	offerErrAmts; 	      // '异常发放总金额(万元)' 营销案异常赠送总金额,
	private Integer  	offerErrNum; 	  	  // '异常发放总次数(次)' 营销案异常赠送总次数,
	private Integer  	offerErrSubs; 	      // '异常发放涉及用户数(位)',
	private Double  	offerErrAmtPer; 	  // '异常发放金额占比(0.zzz9)' 营销案异常赠送总金额占比,
	private Double  	offererrHighAmts; 	  // '高频发放金额(万元)',
	private Double  	offererrBigAmts; 	  // '大额发放金额(万元)',
	private Double  	offererrSubsAmts;	  // '向异常状态用户发放金额(万元)',
	
	
	
	public Integer getRankChinaNum() {
		return rankChinaNum;
	}
	public void setRankChinaNum(Integer rankChinaNum) {
		this.rankChinaNum = rankChinaNum;
	}
	public Integer getRankPrvdNum() {
		return rankPrvdNum;
	}
	public void setRankPrvdNum(Integer rankPrvdNum) {
		this.rankPrvdNum = rankPrvdNum;
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
	public String getOfferId() {
		return offerId;
	}
	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}
	public String getOfferNm() {
		return offerNm;
	}
	public void setOfferNm(String offerNm) {
		this.offerNm = offerNm;
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
	public Integer getOfferCtyId() {
		return offerCtyId;
	}
	public void setOfferCtyId(Integer offerCtyId) {
		this.offerCtyId = offerCtyId;
	}
	public String getOfferCtyNm() {
		return offerCtyNm;
	}
	public void setOfferCtyNm(String offerCtyNm) {
		this.offerCtyNm = offerCtyNm;
	}
	public Double getOfferSumAmt() {
		return offerSumAmt;
	}
	public void setOfferSumAmt(Double offerSumAmt) {
		this.offerSumAmt = offerSumAmt;
	}
	public Integer getOfferSumNum() {
		return offerSumNum;
	}
	public void setOfferSumNum(Integer offerSumNum) {
		this.offerSumNum = offerSumNum;
	}
	public Integer getOfferSumSubs() {
		return offerSumSubs;
	}
	public void setOfferSumSubs(Integer offerSumSubs) {
		this.offerSumSubs = offerSumSubs;
	}
	public Double getOfferErrAmts() {
		return offerErrAmts;
	}
	public void setOfferErrAmts(Double offerErrAmts) {
		this.offerErrAmts = offerErrAmts;
	}
	public Integer getOfferErrNum() {
		return offerErrNum;
	}
	public void setOfferErrNum(Integer offerErrNum) {
		this.offerErrNum = offerErrNum;
	}
	public Integer getOfferErrSubs() {
		return offerErrSubs;
	}
	public void setOfferErrSubs(Integer offerErrSubs) {
		this.offerErrSubs = offerErrSubs;
	}
	public Double getOfferErrAmtPer() {
		return offerErrAmtPer;
	}
	public void setOfferErrAmtPer(Double offerErrAmtPer) {
		this.offerErrAmtPer = offerErrAmtPer;
	}
	public Double getOffererrHighAmts() {
		return offererrHighAmts;
	}
	public void setOffererrHighAmts(Double offererrHighAmts) {
		this.offererrHighAmts = offererrHighAmts;
	}
	public Double getOffererrBigAmts() {
		return offererrBigAmts;
	}
	public void setOffererrBigAmts(Double offererrBigAmts) {
		this.offererrBigAmts = offererrBigAmts;
	}
	public Double getOffererrSubsAmts() {
		return offererrSubsAmts;
	}
	public void setOffererrSubsAmts(Double offererrSubsAmts) {
		this.offererrSubsAmts = offererrSubsAmts;
	}
	public Integer getRankChinaAmt() {
		return rankChinaAmt;
	}
	public void setRankChinaAmt(Integer rankChinaAmt) {
		this.rankChinaAmt = rankChinaAmt;
	}
	public Integer getRankPrvdAmt() {
		return rankPrvdAmt;
	}
	public void setRankPrvdAmt(Integer rankPrvdAmt) {
		this.rankPrvdAmt = rankPrvdAmt;
	}
	
	

}
