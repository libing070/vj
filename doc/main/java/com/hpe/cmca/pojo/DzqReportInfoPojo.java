package com.hpe.cmca.pojo;

public class DzqReportInfoPojo {
	
	private Integer prvdId;
	private Integer cmccSumNum; // 发放总数量
	private Double cmccAmt;     //发放总金额
	private Double cmccErrAmt;  //异常发放金额
	private Integer cmccErrNum; //异常发放数量
	private Double cmccErrAmtPer;//异常发放占比
	public Integer getPrvdId() {
		return prvdId;
	}
	public void setPrvdId(Integer prvdId) {
		this.prvdId = prvdId;
	}
	public Integer getCmccSumNum() {
		return cmccSumNum;
	}
	public void setCmccSumNum(Integer cmccSumNum) {
		this.cmccSumNum = cmccSumNum;
	}
	public Double getCmccAmt() {
		return cmccAmt;
	}
	public void setCmccAmt(Double cmccAmt) {
		this.cmccAmt = cmccAmt;
	}
	public Double getCmccErrAmt() {
		return cmccErrAmt;
	}
	public void setCmccErrAmt(Double cmccErrAmt) {
		this.cmccErrAmt = cmccErrAmt;
	}
	public Integer getCmccErrNum() {
		return cmccErrNum;
	}
	public void setCmccErrNum(Integer cmccErrNum) {
		this.cmccErrNum = cmccErrNum;
	}
	public Double getCmccErrAmtPer() {
		return cmccErrAmtPer;
	}
	public void setCmccErrAmtPer(Double cmccErrAmtPer) {
		this.cmccErrAmtPer = cmccErrAmtPer;
	}
	
	
	
	
}
