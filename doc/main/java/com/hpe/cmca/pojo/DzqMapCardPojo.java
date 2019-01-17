package com.hpe.cmca.pojo;

public class DzqMapCardPojo {
	
	private Integer prvdId;
	private Double cmccErrAmts; //基地有省无电子券金额//异常发放总金额
	private Double cmccErrAmtsZf;//基地有省无电子券金额增幅//异常发放总金额增幅
	private Double cmccErrAmtPer;//基地有省无金额占比//异常发放总金额占比
	private Double cmccErrAmtPerZf;//基地有省无金额占比增幅//异常发放总金额占比增幅
	private Double baseSumAmt;//基地电子券发放总金额
	private Double baseSumAmtZf;//'基地电子券发放总金额增幅
	private Double cmccSumAmt;//省电子券发放总金额
	private Double cmccSumAmtZf;//省电子券发放总金额增幅
	private Double baseErrAmts;//省有基地无电子券金额
	private Double baseErrAmtsZf;//省有基地无电子券金额
	private Double baseErrAmtPer;//省有基地无电子券金额占比
	private Double baseErrAmtPerZf;//省有基地无电子券金额占比增幅

	private Integer cmccErrNum;//异常发放总次数
	private Integer cmccErrNumZf;//异常发放总次数增幅
	private Double cmccErrNumPer;//异常发放总次数占比
	private Double cmccErrNumPerZf;//异常发放总次数占比增幅
	
	
	public Double getCmccErrAmts() {
		return cmccErrAmts;
	}
	public void setCmccErrAmts(Double cmccErrAmts) {
		this.cmccErrAmts = cmccErrAmts;
	}
	public Double getCmccErrAmtsZf() {
		return cmccErrAmtsZf;
	}
	public void setCmccErrAmtsZf(Double cmccErrAmtsZf) {
		this.cmccErrAmtsZf = cmccErrAmtsZf;
	}
	public Double getCmccErrAmtPer() {
		return cmccErrAmtPer;
	}
	public void setCmccErrAmtPer(Double cmccErrAmtPer) {
		this.cmccErrAmtPer = cmccErrAmtPer;
	}
	public Double getCmccErrAmtPerZf() {
		return cmccErrAmtPerZf;
	}
	public void setCmccErrAmtPerZf(Double cmccErrAmtPerZf) {
		this.cmccErrAmtPerZf = cmccErrAmtPerZf;
	}
	public Double getBaseSumAmt() {
		return baseSumAmt;
	}
	public void setBaseSumAmt(Double baseSumAmt) {
		this.baseSumAmt = baseSumAmt;
	}
	public Double getBaseSumAmtZf() {
		return baseSumAmtZf;
	}
	public void setBaseSumAmtZf(Double baseSumAmtZf) {
		this.baseSumAmtZf = baseSumAmtZf;
	}
	public Double getCmccSumAmt() {
		return cmccSumAmt;
	}
	public void setCmccSumAmt(Double cmccSumAmt) {
		this.cmccSumAmt = cmccSumAmt;
	}
	public Double getCmccSumAmtZf() {
		return cmccSumAmtZf;
	}
	public void setCmccSumAmtZf(Double cmccSumAmtZf) {
		this.cmccSumAmtZf = cmccSumAmtZf;
	}
	public Double getBaseErrAmts() {
		return baseErrAmts;
	}
	public void setBaseErrAmts(Double baseErrAmts) {
		this.baseErrAmts = baseErrAmts;
	}
	public Double getBaseErrAmtsZf() {
		return baseErrAmtsZf;
	}
	public void setBaseErrAmtsZf(Double baseErrAmtsZf) {
		this.baseErrAmtsZf = baseErrAmtsZf;
	}
	public Double getBaseErrAmtPer() {
		return baseErrAmtPer;
	}
	public void setBaseErrAmtPer(Double baseErrAmtPer) {
		this.baseErrAmtPer = baseErrAmtPer;
	}
	public Double getBaseErrAmtPerZf() {
		return baseErrAmtPerZf;
	}
	public void setBaseErrAmtPerZf(Double baseErrAmtPerZf) {
		this.baseErrAmtPerZf = baseErrAmtPerZf;
	}
	public Integer getCmccErrNum() {
		return cmccErrNum;
	}
	public void setCmccErrNum(Integer cmccErrNum) {
		this.cmccErrNum = cmccErrNum;
	}
	public Integer getCmccErrNumZf() {
		return cmccErrNumZf;
	}
	public void setCmccErrNumZf(Integer cmccErrNumZf) {
		this.cmccErrNumZf = cmccErrNumZf;
	}
	public Double getCmccErrNumPer() {
		return cmccErrNumPer;
	}
	public void setCmccErrNumPer(Double cmccErrNumPer) {
		this.cmccErrNumPer = cmccErrNumPer;
	}
	public Double getCmccErrNumPerZf() {
		return cmccErrNumPerZf;
	}
	public void setCmccErrNumPerZf(Double cmccErrNumPerZf) {
		this.cmccErrNumPerZf = cmccErrNumPerZf;
	}
	public Integer getPrvdId() {
		return prvdId;
	}
	public void setPrvdId(Integer prvdId) {
		this.prvdId = prvdId;
	}

}
