package com.hpe.cmca.pojo;

import java.math.BigDecimal;

/**
 * 客户欠费实体
 * <pre>
 * Desc： 
 * @author   issuser
 * @refactor issuser
 * @date     2017-7-5 下午2:15:09
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-7-5 	   issuser 	         1. Created this class. 
 * </pre>
 */
public class KhqfData {

	private String genDate ;
	private String audTrm ;
	private int prvdId ;
	private int ctyId ;
	private String focusCd ;
	private String prvdName;
	private String ctyName;
	private String concern ;			//客户欠费类型 （个人 or集团）
	private BigDecimal infractionAmt;	//欠费金额
	private Long infractionNum;			//欠费数量
	private BigDecimal newAmt; 			//新增账户欠费金额
	private Long newNum; 				//新增欠费账户
	private BigDecimal oriAmt; 			//原有账户欠费金额（欠费-新增）
	private Long oriNum; 				//原有欠费账户（欠费-新增）
	private int rn;					//排名
	private BigDecimal incremental;	//增量金额
	private BigDecimal num1;
	private BigDecimal amt1;
	private BigDecimal num2;
	private BigDecimal amt2;
	private BigDecimal num3;
	private BigDecimal amt3;
	private int numOrder;//数量排名
	private int amtOrder;//金额排名
	private String target;//指标，以金额排名为指标还是以数量排名为指标
	private BigDecimal infractionAmtMom;	//欠费金额较上月变化
	private Long infractionNumMom;			//欠费数量较上月变化
	private BigDecimal oriAmtMom; 			//新增账户欠费金额较上月变化
	private Long oriNumMom; 				//新增欠费账户较上月变化
	private BigDecimal vipAmt; 			//红名单账户欠费金额
	private Long vipNum; 				//红名单欠费账户数
	
	private String custNm;    //客户名称  用于明细级别
	private String custTyp;  //客户类别  用于明细级别
	private int acctAge;    //欠费账龄  用于明细级别
	private BigDecimal rectifyNum; //整改问责次数
	
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

	public Long getInfractionNum() {
		return this.infractionNum;
	}
	public void setInfractionNum(Long infractionNum) {
		this.infractionNum = infractionNum;
	}
	public String getConcern() {
	    return this.concern;
	}
	public void setConcern(String concern) {
	    this.concern = concern;
	}
	public BigDecimal getInfractionAmt() {
	    return this.infractionAmt;
	}
	public void setInfractionAmt(BigDecimal infractionAmt) {
	    this.infractionAmt = infractionAmt;
	}
	
	
	public BigDecimal getNewAmt() {
		return this.newAmt;
	}
	
	public void setNewAmt(BigDecimal newAmt) {
		this.newAmt = newAmt;
	}
	
	public Long getNewNum() {
		return this.newNum;
	}
	
	public void setNewNum(Long newNum) {
		this.newNum = newNum;
	}
	
	public BigDecimal getOriAmt() {
		return this.oriAmt;
	}
	
	public void setOriAmt(BigDecimal oriAmt) {
		this.oriAmt = oriAmt;
	}
	
	public Long getOriNum() {
		return this.oriNum;
	}
	
	public void setOriNum(Long oriNum) {
		this.oriNum = oriNum;
	}
	public int getRn() {
		return this.rn;
	}
	public void setRn(int rn) {
		this.rn = rn;
	}
	public BigDecimal getIncremental() {
		return this.incremental;
	}
	public void setIncremental(BigDecimal incremental) {
		this.incremental = incremental;
	}
	public BigDecimal getNum1() {
		return this.num1;
	}
	public void setNum1(BigDecimal num1) {
		this.num1 = num1;
	}
	public BigDecimal getAmt1() {
		return this.amt1;
	}
	public void setAmt1(BigDecimal amt1) {
		this.amt1 = amt1;
	}
	public BigDecimal getNum2() {
		return this.num2;
	}
	public void setNum2(BigDecimal num2) {
		this.num2 = num2;
	}
	public BigDecimal getAmt2() {
		return this.amt2;
	}
	public void setAmt2(BigDecimal amt2) {
		this.amt2 = amt2;
	}
	public BigDecimal getNum3() {
		return this.num3;
	}
	public void setNum3(BigDecimal num3) {
		this.num3 = num3;
	}
	public BigDecimal getAmt3() {
		return this.amt3;
	}
	public void setAmt3(BigDecimal amt3) {
		this.amt3 = amt3;
	}
	
	public int getNumOrder() {
		return this.numOrder;
	}
	
	public void setNumOrder(int numOrder) {
		this.numOrder = numOrder;
	}
	
	public int getAmtOrder() {
		return this.amtOrder;
	}
	
	public void setAmtOrder(int amtOrder) {
		this.amtOrder = amtOrder;
	}
	
	public String getTarget() {
		return this.target;
	}
	
	public void setTarget(String target) {
		this.target = target;
	}
	
	public BigDecimal getInfractionAmtMom() {
		return this.infractionAmtMom;
	}
	
	public void setInfractionAmtMom(BigDecimal infractionAmtMom) {
		this.infractionAmtMom = infractionAmtMom;
	}
	
	public Long getInfractionNumMom() {
		return this.infractionNumMom;
	}
	
	public void setInfractionNumMom(Long infractionNumMom) {
		this.infractionNumMom = infractionNumMom;
	}
	
	public BigDecimal getOriAmtMom() {
		return this.oriAmtMom;
	}
	
	public void setOriAmtMom(BigDecimal oriAmtMom) {
		this.oriAmtMom = oriAmtMom;
	}
	
	public Long getOriNumMom() {
		return this.oriNumMom;
	}
	
	public void setOriNumMom(Long oriNumMom) {
		this.oriNumMom = oriNumMom;
	}
	
	public BigDecimal getVipAmt() {
		return this.vipAmt;
	}
	
	public void setVipAmt(BigDecimal vipAmt) {
		this.vipAmt = vipAmt;
	}
	
	public Long getVipNum() {
		return this.vipNum;
	}
	
	public void setVipNum(Long vipNum) {
		this.vipNum = vipNum;
	}
	
	public String getCustNm() {
		return this.custNm;
	}
	
	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}
	
	public String getCustTyp() {
		return this.custTyp;
	}
	
	public void setCustTyp(String custTyp) {
		this.custTyp = custTyp;
	}
	
	public int getAcctAge() {
		return this.acctAge;
	}
	
	public void setAcctAge(int acctAge) {
		this.acctAge = acctAge;
	}
	
	public BigDecimal getRectifyNum() {
	    return this.rectifyNum;
	}
	
	
	public void setRectifyNum(BigDecimal rectifyNum) {
	    this.rectifyNum = rectifyNum;
	}
	
}
