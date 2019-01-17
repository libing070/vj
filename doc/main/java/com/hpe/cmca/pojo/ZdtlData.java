/**
 * com.hpe.cmca.pojo.ZdtlData.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.pojo;

import java.math.BigDecimal;


/**
 * <pre>
 * Desc： 终端套利结果实体类
 * @author   hufei
 * @refactor hufei
 * @date     2017-7-12 下午5:08:26
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-7-12 	   hufei 	         1. Created this class. 
 * </pre>  
 */
public class ZdtlData {
    //销售总数量
    private BigDecimal tolSellNum;
    //销售渠道总数量
    private BigDecimal tolSellChnlNum;
    //异常销售占比
    private BigDecimal qtyPercent;
    //异常销售占比较上月增加
    private BigDecimal qtyPercentMom;
    //异常销售数量
    private BigDecimal infractionNum;
    //异常销售数量较上月增加
    private BigDecimal infractionNumMom;
    //异常销售数量环比
    private BigDecimal infractionNumMOM;
    //套利数量占比
    private BigDecimal qtyPercentCJ;
    //套利数量占比较上月增加
    private BigDecimal qtyPercentMomCJ;
    //套利数量
    private BigDecimal infractionNumCJ;
    //套利数量较上月增加
    private BigDecimal infractionNumMomCJ;
    //套利金额
    private BigDecimal infractionAmtCJ;
    //套利金额占比
    private BigDecimal amtPercentCJ;
    //省份名称
    private String prvdName;
    //审计月
    private String audTrm;  
    //省份ID
    private int prvdId ;
    //地市ID
    private int ctyId ;
    //关注点
    private String concern ;
    //关注点名称
    private String concernName;
    //地市名称
    private String ctyName;
    //渠道名称
    private String chnlName;
    //渠道ID
    private String chnlId;
    //渠道类型
    private String chnlType;
    //异常销售渠道数量
    private BigDecimal infractionChnlNum;
    //异常销售渠道数量占比
    private BigDecimal infractionChnlPercent;
    //异常销售渠道数量增幅
    private BigDecimal infractionChnlNumMom;
    //异常销售渠道数量占比
    private BigDecimal infractionChnlPercentMom;
    //异常销售渠道数量环比
    private BigDecimal infractionChnlPercentMOM;
    //违规地市数量
    private BigDecimal errCityNum;
    //整改问责次数
    private BigDecimal rectifyNum;
    
    //序号
    private int rn;
    private BigDecimal silentNum;	       // 沉默违规数量
    private BigDecimal keepMachineNum;	  // 养机违规数量
    private BigDecimal unpackingNum;	    // 拆包违规数量
    private BigDecimal transProvinceNum;	// 跨省串货
    
    
    public BigDecimal getQtyPercent() {
        return this.qtyPercent;
    }
    
    public String getAudTrm() {
        return this.audTrm;
    }
    
    public void setQtyPercent(BigDecimal qtyPercent) {
        this.qtyPercent = qtyPercent;
    }
    
    public void setPrvdName(String prvdName) {
        this.prvdName = prvdName;
    }
    
    public void setAudTrm(String audTrm) {
        this.audTrm = audTrm;
    }

    public int getPrvdId() {
        return this.prvdId;
    }
    
    public int getCtyId() {
        return this.ctyId;
    }
    
    public String getConcern() {
        return this.concern;
    }
    
    public String getCtyName() {
        return this.ctyName;
    }
    
    public void setPrvdId(int prvdId) {
        this.prvdId = prvdId;
    }
    
    public void setCtyId(int ctyId) {
        this.ctyId = ctyId;
    }
    
    public void setConcern(String concern) {
        this.concern = concern;
    }
    
    public void setCtyName(String ctyName) {
        this.ctyName = ctyName;
    }
    
    public int getRn() {
	return this.rn;
    }
    public void setRn(int rn) {
	this.rn = rn;
    }

    public String getPrvdName() {
        return this.prvdName;
    }

    public BigDecimal getQtyPercentMom() {
        return this.qtyPercentMom;
    }

    
    public BigDecimal getInfractionNum() {
        return this.infractionNum;
    }

    
    public BigDecimal getInfractionNumMom() {
        return this.infractionNumMom;
    }

    
    public BigDecimal getQtyPercentCJ() {
        return this.qtyPercentCJ;
    }

    
    public BigDecimal getQtyPercentMomCJ() {
        return this.qtyPercentMomCJ;
    }

    
    public BigDecimal getInfractionNumCJ() {
        return this.infractionNumCJ;
    }

    
    public BigDecimal getInfractionNumMomCJ() {
        return this.infractionNumMomCJ;
    }

    
    public BigDecimal getInfractionAmtCJ() {
        return this.infractionAmtCJ;
    }

    
    public void setQtyPercentMom(BigDecimal qtyPercentMom) {
        this.qtyPercentMom = qtyPercentMom;
    }

    
    public void setInfractionNum(BigDecimal infractionNum) {
        this.infractionNum = infractionNum;
    }

    
    public void setInfractionNumMom(BigDecimal infractionNumMom) {
        this.infractionNumMom = infractionNumMom;
    }

    
    public void setQtyPercentCJ(BigDecimal qtyPercentCJ) {
        this.qtyPercentCJ = qtyPercentCJ;
    }

    
    public void setQtyPercentMomCJ(BigDecimal qtyPercentMomCJ) {
        this.qtyPercentMomCJ = qtyPercentMomCJ;
    }

    
    public void setInfractionNumCJ(BigDecimal infractionNumCJ) {
        this.infractionNumCJ = infractionNumCJ;
    }

    
    public void setInfractionNumMomCJ(BigDecimal infractionNumMomCJ) {
        this.infractionNumMomCJ = infractionNumMomCJ;
    }

    
    public void setInfractionAmtCJ(BigDecimal infractionAmtCJ) {
        this.infractionAmtCJ = infractionAmtCJ;
    }

    
    public String getChnlName() {
        return this.chnlName;
    }

    
    public String getChnlType() {
        return this.chnlType;
    }

    
    public void setChnlName(String chnlName) {
        this.chnlName = chnlName;
    }

    
    public void setChnlType(String chnlType) {
        this.chnlType = chnlType;
    }

    
    public BigDecimal getAmtPercentCJ() {
        return this.amtPercentCJ;
    }

    
    public void setAmtPercentCJ(BigDecimal amtPercentCJ) {
        this.amtPercentCJ = amtPercentCJ;
    }

    
    public BigDecimal getInfractionChnlNum() {
        return this.infractionChnlNum;
    }

    
    public BigDecimal getInfractionChnlPercent() {
        return this.infractionChnlPercent;
    }

    
    public void setInfractionChnlNum(BigDecimal infractionChnlNum) {
        this.infractionChnlNum = infractionChnlNum;
    }

    
    public void setInfractionChnlPercent(BigDecimal infractionChnlPercent) {
        this.infractionChnlPercent = infractionChnlPercent;
    }

    
    public String getChnlId() {
        return this.chnlId;
    }

    
    public void setChnlId(String chnlId) {
        this.chnlId = chnlId;
    }

    
    public BigDecimal getInfractionChnlNumMom() {
        return this.infractionChnlNumMom;
    }

    
    public BigDecimal getInfractionChnlPercentMom() {
        return this.infractionChnlPercentMom;
    }

    
    public void setInfractionChnlNumMom(BigDecimal infractionChnlNumMom) {
        this.infractionChnlNumMom = infractionChnlNumMom;
    }

    
    public void setInfractionChnlPercentMom(BigDecimal infractionChnlPercentMom) {
        this.infractionChnlPercentMom = infractionChnlPercentMom;
    }

    
    public BigDecimal getSilentNum() {
        return this.silentNum;
    }

    
    public BigDecimal getKeepMachineNum() {
        return this.keepMachineNum;
    }

    
    public BigDecimal getUnpackingNum() {
        return this.unpackingNum;
    }

    
    public BigDecimal getTransProvinceNum() {
        return this.transProvinceNum;
    }

    
    public void setSilentNum(BigDecimal silentNum) {
        this.silentNum = silentNum;
    }

    
    public void setKeepMachineNum(BigDecimal keepMachineNum) {
        this.keepMachineNum = keepMachineNum;
    }

    
    public void setUnpackingNum(BigDecimal unpackingNum) {
        this.unpackingNum = unpackingNum;
    }

    
    public void setTransProvinceNum(BigDecimal transProvinceNum) {
        this.transProvinceNum = transProvinceNum;
    }

    
    public BigDecimal getTolSellNum() {
        return this.tolSellNum;
    }

    
    public void setTolSellNum(BigDecimal tolSellNum) {
        this.tolSellNum = tolSellNum;
    }

    
    public BigDecimal getInfractionNumMOM() {
        return this.infractionNumMOM;
    }

    
    public void setInfractionNumMOM(BigDecimal infractionNumMOM) {
        this.infractionNumMOM = infractionNumMOM;
    }

    
    public String getConcernName() {
        return this.concernName;
    }

    
    public void setConcernName(String concernName) {
        this.concernName = concernName;
    }

    
    public BigDecimal getInfractionChnlPercentMOM() {
        return this.infractionChnlPercentMOM;
    }

    
    public void setInfractionChnlPercentMOM(BigDecimal infractionChnlPercentMOM) {
        this.infractionChnlPercentMOM = infractionChnlPercentMOM;
    }

    
    public BigDecimal getErrCityNum() {
        return this.errCityNum;
    }

    
    public void setErrCityNum(BigDecimal errCityNum) {
        this.errCityNum = errCityNum;
    }

    
    public BigDecimal getTolSellChnlNum() {
        return this.tolSellChnlNum;
    }

    
    public void setTolSellChnlNum(BigDecimal tolSellChnlNum) {
        this.tolSellChnlNum = tolSellChnlNum;
    }

    
    public BigDecimal getRectifyNum() {
        return this.rectifyNum;
    }

    
    public void setRectifyNum(BigDecimal rectifyNum) {
        this.rectifyNum = rectifyNum;
    }
   
}
