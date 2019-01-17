package com.hpe.cmca.pojo;

import java.math.BigDecimal;
/**
 * 员工异常
 * <pre>
 * Desc： 
 * @author   issuser
 * @refactor issuser
 * @date     2017-7-12 上午10:59:20
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-7-12 	   issuser 	         1. Created this class. 
 * </pre>
 */
public class YgycData {

	private String genDate ;
	private String audTrm ;
	private int prvdId ;
	private int ctyId ;
	private int rn ;
	private String focusCd ;
	private String prvdName;
	private String ctyName;
	private String concern ;	
	private Long employeeQty;	//员工数量
	private Long userNum;		//用户数量
	private Long prvdNum;		//涉及省份数量
	private BigDecimal presentAmt;	//赠送金额
	private String operatorId;	//操作员工号
	private BigDecimal totalAmt;	//赠送金额
	private Long totalSubs;		//获赠用户数
	private Long totalTimes;	//获赠次数
	private String cellPhone;	//手机号码
	private String staffId;		//操作工号
	private String chnlType;	//渠道类型
	private String chnlName;	//渠道名称
	private String accountId;	//账户标识
	private String operateTime;	//操作时间
	private Long prvdNumMom;	//省份数增量
	private BigDecimal totalAmountMom;	//总金额增量
	private Long totalOprMom;	//涉及员工数增量
	private Long totalSubsMom;	//涉及用户数增量
	private String subsId;	//用户标识
	
	public String getAudTrm() {
		return this.audTrm;
	}
	public void setAudTrm(String audTrm) {
		this.audTrm = audTrm;
	}
	public int getPrvdId() {
		return this.prvdId;
	}
	public void setPrvdId(int prvdId) {
		this.prvdId = prvdId;
	}
	
	public int getCtyId() {
		return this.ctyId;
	}
	
	public void setCtyId(int ctyId) {
		this.ctyId = ctyId;
	}
	public String getGenDate() {
		return this.genDate;
	}
	public void setGenDate(String genDate) {
		this.genDate = genDate;
	}
	public String getFocusCd() {
		return this.focusCd;
	}
	public void setFocusCd(String focusCd) {
		this.focusCd = focusCd;
	}
	public String getPrvdName() {
		return this.prvdName;
	}
	public void setPrvdName(String prvdName) {
		this.prvdName = prvdName;
	}
	
	public String getCtyName() {
		return this.ctyName;
	}
	
	public void setCtyName(String ctyName) {
		this.ctyName = ctyName;
	}
	public String getConcern() {
		return this.concern;
	}
	public void setConcern(String concern) {
		this.concern = concern;
	}
	public int getRn() {
		return this.rn;
	}
	public void setRn(int rn) {
		this.rn = rn;
	}
	public Long getEmployeeQty() {
		return this.employeeQty;
	}
	public void setEmployeeQty(Long employeeQty) {
		this.employeeQty = employeeQty;
	}
	public BigDecimal getPresentAmt() {
		return this.presentAmt;
	}
	public void setPresentAmt(BigDecimal presentAmt) {
		this.presentAmt = presentAmt;
	}
	public Long getUserNum() {
		return this.userNum;
	}
	public void setUserNum(Long userNum) {
		this.userNum = userNum;
	}
	public Long getPrvdNum() {
		return this.prvdNum;
	}
	public void setPrvdNum(Long prvdNum) {
		this.prvdNum = prvdNum;
	}
	public String getOperatorId() {
		return this.operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public BigDecimal getTotalAmt() {
		return this.totalAmt;
	}
	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}
	public Long getTotalSubs() {
		return this.totalSubs;
	}
	public void setTotalSubs(Long totalSubs) {
		this.totalSubs = totalSubs;
	}
	public Long getTotalTimes() {
		return this.totalTimes;
	}
	public void setTotalTimes(Long totalTimes) {
		this.totalTimes = totalTimes;
	}
	
	public String getCellPhone() {
		return this.cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	public String getStaffId() {
	    return this.staffId;
	}
	
	public void setStaffId(String staffId) {
	    this.staffId = staffId;
	}
	public String getChnlType() {
		return this.chnlType;
	}
	public void setChnlType(String chnlType) {
		this.chnlType = chnlType;
	}
	public String getChnlName() {
		return this.chnlName;
	}
	public void setChnlName(String chnlName) {
		this.chnlName = chnlName;
	}
	public String getAccountId() {
		return this.accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getOperateTime() {
		return this.operateTime;
	}
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	public Long getPrvdNumMom() {
		return this.prvdNumMom;
	}
	public void setPrvdNumMom(Long prvdNumMom) {
		this.prvdNumMom = prvdNumMom;
	}
	public BigDecimal getTotalAmountMom() {
		return this.totalAmountMom;
	}
	public void setTotalAmountMom(BigDecimal totalAmountMom) {
		this.totalAmountMom = totalAmountMom;
	}
	public Long getTotalOprMom() {
		return this.totalOprMom;
	}
	public void setTotalOprMom(Long totalOprMom) {
		this.totalOprMom = totalOprMom;
	}
	public Long getTotalSubsMom() {
		return this.totalSubsMom;
	}
	public void setTotalSubsMom(Long totalSubsMom) {
		this.totalSubsMom = totalSubsMom;
	}
	
	public String getSubsId() {
	    return this.subsId;
	}
	
	public void setSubsId(String subsId) {
	    this.subsId = subsId;
	}
	
}
