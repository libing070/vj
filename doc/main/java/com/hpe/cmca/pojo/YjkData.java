package com.hpe.cmca.pojo;

import java.math.BigDecimal;


public class YjkData {
	
	private int concernId;
	private String concernName ;
	private String audTrm;
	private int prvdId;
	private String prvdName;
	private int ctyId;
	private String ctyName;
	private Long infractionNum ;
	private Long totalNum ;
	
	private BigDecimal infractionNumTmp ;//处理后的，比如除1万或除1百万
	private BigDecimal totalNumTmp ;//处理后的，比如除1万或除1百万
	
	private BigDecimal numPercent;
	private BigDecimal infractionAmt;
	private BigDecimal tolAmt;
	private BigDecimal amtPercent;
	private int cntArea;
	private int rn;	
	private Long infractionNumMom;//违规数量较上月变化
	private BigDecimal infractionNumMomTmp;//违规数量较上月变化,处理后的，比如除1万或除1百万
	private BigDecimal numPercentMom;//数量占比较上月变化		
	private BigDecimal infractionAmtMom;//违规金额较上月变化	
	private BigDecimal amtPercentMom;//金额占比较上月变化
	private int numPercentOrder;//数量占比排名
	private int amtPercentOrder;//金额占比排名	
	private BigDecimal errQtyLrr;
	private BigDecimal errAmtLrr;
	private int crmNum;//crm存在vc不存在
	private int vcNum; //vc存在crm不存在
	private int allNum;//vc和crm均存在
	private String yjkStat;//有价卡状态

	

	
	
	

	public int getConcernId() {
		return this.concernId;
	}
	
	public void setConcernId(int concernId) {
		this.concernId = concernId;
	}
	
	public String getConcernName() {
		return this.concernName;
	}
	
	public void setConcernName(String concernName) {
		this.concernName = concernName;
	}
	
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
	
	public String getPrvdName() {
		return this.prvdName;
	}
	
	public void setPrvdName(String prvdName) {
		this.prvdName = prvdName;
	}
	
	public int getCtyId() {
		return this.ctyId;
	}
	
	public void setCtyId(int ctyId) {
		this.ctyId = ctyId;
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
	
	public Long getTotalNum() {
		return this.totalNum;
	}
	
	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}
	
	
	public BigDecimal getInfractionNumTmp() {
		return this.infractionNumTmp;
	}

	
	public void setInfractionNumTmp(BigDecimal infractionNumTmp) {
		this.infractionNumTmp = infractionNumTmp;
	}

	
	public BigDecimal getTotalNumTmp() {
		return this.totalNumTmp;
	}

	
	public void setTotalNumTmp(BigDecimal totalNumTmp) {
		this.totalNumTmp = totalNumTmp;
	}

	public BigDecimal getNumPercent() {
		return this.numPercent;
	}
	
	public void setNumPercent(BigDecimal numPercent) {
		this.numPercent = numPercent;
	}
	
	public BigDecimal getInfractionAmt() {
		return this.infractionAmt;
	}
	
	public void setInfractionAmt(BigDecimal infractionAmt) {
		this.infractionAmt = infractionAmt;
	}
	
	public BigDecimal getTolAmt() {
		return this.tolAmt;
	}
	
	public void setTolAmt(BigDecimal tolAmt) {
		this.tolAmt = tolAmt;
	}
	
	public BigDecimal getAmtPercent() {
		return this.amtPercent;
	}
	
	public void setAmtPercent(BigDecimal amtPercent) {
		this.amtPercent = amtPercent;
	}
	
	public int getCntArea() {
		return this.cntArea;
	}
	
	public void setCntArea(int cntArea) {
		this.cntArea = cntArea;
	}

	
	public int getRn() {
		return this.rn;
	}

	
	public void setRn(int rn) {
		this.rn = rn;
	}

	
	public Long getInfractionNumMom() {
		return this.infractionNumMom;
	}

	
	public void setInfractionNumMom(Long infractionNumMom) {
		this.infractionNumMom = infractionNumMom;
	}

	
	
	public BigDecimal getInfractionNumMomTmp() {
		return this.infractionNumMomTmp;
	}

	
	public void setInfractionNumMomTmp(BigDecimal infractionNumMomTmp) {
		this.infractionNumMomTmp = infractionNumMomTmp;
	}

	public BigDecimal getNumPercentMom() {
		return this.numPercentMom;
	}

	
	public void setNumPercentMom(BigDecimal numPercentMom) {
		this.numPercentMom = numPercentMom;
	}

	
	public BigDecimal getInfractionAmtMom() {
		return this.infractionAmtMom;
	}

	
	public void setInfractionAmtMom(BigDecimal infractionAmtMom) {
		this.infractionAmtMom = infractionAmtMom;
	}

	
	public BigDecimal getAmtPercentMom() {
		return this.amtPercentMom;
	}

	
	public void setAmtPercentMom(BigDecimal amtPercentMom) {
		this.amtPercentMom = amtPercentMom;
	}

	
	public int getNumPercentOrder() {
		return this.numPercentOrder;
	}

	
	public void setNumPercentOrder(int numPercentOrder) {
		this.numPercentOrder = numPercentOrder;
	}

	
	public int getAmtPercentOrder() {
		return this.amtPercentOrder;
	}

	
	public void setAmtPercentOrder(int amtPercentOrder) {
		this.amtPercentOrder = amtPercentOrder;
	}

	
	
	public BigDecimal getErrQtyLrr() {
		return this.errQtyLrr;
	}

	
	public void setErrQtyLrr(BigDecimal errQtyLrr) {
		this.errQtyLrr = errQtyLrr;
	}

	
	public BigDecimal getErrAmtLrr() {
		return this.errAmtLrr;
	}

	
	public void setErrAmtLrr(BigDecimal errAmtLrr) {
		this.errAmtLrr = errAmtLrr;
	}

	public int getCrmNum() {
		return this.crmNum;
	}

	
	public void setCrmNum(int crmNum) {
		this.crmNum = crmNum;
	}

	
	public int getVcNum() {
		return this.vcNum;
	}

	
	public void setVcNum(int vcNum) {
		this.vcNum = vcNum;
	}

	
	public int getAllNum() {
		return this.allNum;
	}

	
	public void setAllNum(int allNum) {
		this.allNum = allNum;
	}

	
	public String getYjkStat() {
		return this.yjkStat;
	}

	
	public void setYjkStat(String yjkStat) {
		this.yjkStat = yjkStat;
	}
	
}
