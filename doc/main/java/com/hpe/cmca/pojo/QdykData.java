package com.hpe.cmca.pojo;

import java.math.BigDecimal;
/**
 * 渠道养卡
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
public class QdykData {

	private String genDate ;
	private String audTrm ;
	private int prvdId ;
	private int ctyId ;
	private String focusCd ;
	private String prvdName;
	private String ctyName;
	private String concern ;	//关注点类型 0全渠道  1自有渠道 2 社会渠道
	private BigDecimal qtyPercent;   //养卡号码占比
	private int rn;
	private Long errQty;				//养卡号码数量
	private BigDecimal errQtyQdyk;		//养卡号码数量
	private Long chnlQty;				//养卡渠道数量
	private BigDecimal chnlQtyQdyk;				//养卡渠道数量
	private BigDecimal chnlPercent;		//养卡渠道占比
	private Integer rnErrQty;		//养卡号码数量排名
	private Integer rnQtyPercent;		//养卡占比排名
	private Integer rnChnlQty;		//养卡渠道数量排名
	private Integer rnChnlPercent;		//养卡渠道占比排名
	private BigDecimal fbQtyMonth;		//养卡数量增量
	private BigDecimal fbChnlMonth;		//养卡渠道增量
	private BigDecimal fbQtyPerMonth;		//养卡数量占比增量
	private BigDecimal fbChnlPerMonth;		//养卡渠道占比增量
	private String chnlClassNm;	//渠道类别名称
	private String chnlName;	//渠道名称
	private String chnlId;		//渠道标识
	private Integer cntArea;
	private Long increment;	//数量增量
	private String chnlClass; //渠道类型
	
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
	public BigDecimal getQtyPercent() {
		return this.qtyPercent;
	}
	public void setQtyPercent(BigDecimal qtyPercent) {
		this.qtyPercent = qtyPercent;
	}
	public int getRn() {
		return this.rn;
	}
	public void setRn(int rn) {
		this.rn = rn;
	}
	public Long getErrQty() {
		return this.errQty;
	}
	public void setErrQty(Long errQty) {
		this.errQty = errQty;
	}
	public Long getChnlQty() {
		return this.chnlQty;
	}
	public void setChnlQty(Long chnlQty) {
		this.chnlQty = chnlQty;
	}
	public BigDecimal getChnlPercent() {
		return this.chnlPercent;
	}
	public void setChnlPercent(BigDecimal chnlPercent) {
		this.chnlPercent = chnlPercent;
	}
	public Integer getRnErrQty() {
		return this.rnErrQty;
	}
	public void setRnErrQty(Integer rnErrQty) {
		this.rnErrQty = rnErrQty;
	}
	public Integer getRnQtyPercent() {
		return this.rnQtyPercent;
	}
	public void setRnQtyPercent(Integer rnQtyPercent) {
		this.rnQtyPercent = rnQtyPercent;
	}
	public Integer getRnChnlQty() {
		return this.rnChnlQty;
	}
	public void setRnChnlQty(Integer rnChnlQty) {
		this.rnChnlQty = rnChnlQty;
	}
	public Integer getRnChnlPercent() {
		return this.rnChnlPercent;
	}
	public void setRnChnlPercent(Integer rnChnlPercent) {
		this.rnChnlPercent = rnChnlPercent;
	}
	public BigDecimal getErrQtyQdyk() {
		return this.errQtyQdyk;
	}
	public void setErrQtyQdyk(BigDecimal errQtyQdyk) {
		this.errQtyQdyk = errQtyQdyk;
	}
	public BigDecimal getFbQtyMonth() {
		return this.fbQtyMonth;
	}
	public void setFbQtyMonth(BigDecimal fbQtyMonth) {
		this.fbQtyMonth = fbQtyMonth;
	}
	public BigDecimal getFbChnlMonth() {
		return this.fbChnlMonth;
	}
	public void setFbChnlMonth(BigDecimal fbChnlMonth) {
		this.fbChnlMonth = fbChnlMonth;
	}
	public BigDecimal getFbQtyPerMonth() {
		return this.fbQtyPerMonth;
	}
	public void setFbQtyPerMonth(BigDecimal fbQtyPerMonth) {
		this.fbQtyPerMonth = fbQtyPerMonth;
	}
	public BigDecimal getFbChnlPerMonth() {
		return this.fbChnlPerMonth;
	}
	public void setFbChnlPerMonth(BigDecimal fbChnlPerMonth) {
		this.fbChnlPerMonth = fbChnlPerMonth;
	}
	public String getChnlClassNm() {
		return this.chnlClassNm;
	}
	public void setChnlClassNm(String chnlClassNm) {
		this.chnlClassNm = chnlClassNm;
	}
	public String getChnlName() {
		return this.chnlName;
	}
	public void setChnlName(String chnlName) {
		this.chnlName = chnlName;
	}
	public String getChnlId() {
		return this.chnlId;
	}
	public void setChnlId(String chnlId) {
		this.chnlId = chnlId;
	}
	public Integer getCntArea() {
		return this.cntArea;
	}
	public void setCntArea(Integer cntArea) {
		this.cntArea = cntArea;
	}
	public Long getIncrement() {
		return this.increment;
	}
	public void setIncrement(Long increment) {
		this.increment = increment;
	}
	public String getChnlClass() {
		return this.chnlClass;
	}
	public void setChnlClass(String chnlClass) {
		this.chnlClass = chnlClass;
	}
	public BigDecimal getChnlQtyQdyk() {
		return this.chnlQtyQdyk;
	}
	public void setChnlQtyQdyk(BigDecimal chnlQtyQdyk) {
		this.chnlQtyQdyk = chnlQtyQdyk;
	}
	
	
}
