/**
 * com.hpe.cmca.pojo.LlglwgBottomData.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.pojo;

import java.math.BigDecimal;


/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2018-2-7 下午2:40:44
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2018-2-7 	   hufei 	         1. Created this class. 
 * </pre>  
 */
public class LlglwgBottomData {
    private BigDecimal errTrafficNumber;//流量违规数量
    private BigDecimal errTrafficPercent;//流量违规占比
    private BigDecimal errTrafficNumberMom;//流量违规数量增幅
    private BigDecimal errTrafficPercentMom;//流量违规占比增幅
    
    private BigDecimal errTrafficTotle;//异常赠送次数
    private BigDecimal errTrafficTotleMom;//异常赠送次数增幅
    private BigDecimal errSubsTotle;//异常赠送次数
    private BigDecimal errSubsTotleMom;//异常赠送次数增幅
    
    
    private BigDecimal paySystemGroupNumber;//统付集团用户数
    private BigDecimal paySystemGroupNumberMom;//统付集团用户数增幅
    private BigDecimal illegalGroupNumber;//违规集团用户数
    private BigDecimal illegalGroupNumberMom;//违规集团用户数增幅
    
    public BigDecimal getErrTrafficNumber() {
        return this.errTrafficNumber;
    }
    
    public BigDecimal getErrTrafficPercent() {
        return this.errTrafficPercent;
    }
    
    public BigDecimal getErrTrafficNumberMom() {
        return this.errTrafficNumberMom;
    }
    
    public BigDecimal getErrTrafficPercentMom() {
        return this.errTrafficPercentMom;
    }
    
    public BigDecimal getErrTrafficTotle() {
        return this.errTrafficTotle;
    }
    
    public BigDecimal getErrTrafficTotleMom() {
        return this.errTrafficTotleMom;
    }
    
    public BigDecimal getErrSubsTotle() {
        return this.errSubsTotle;
    }
    
    public BigDecimal getErrSubsTotleMom() {
        return this.errSubsTotleMom;
    }
    
    public BigDecimal getPaySystemGroupNumber() {
        return this.paySystemGroupNumber;
    }
    
    public BigDecimal getPaySystemGroupNumberMom() {
        return this.paySystemGroupNumberMom;
    }
    
    public BigDecimal getIllegalGroupNumber() {
        return this.illegalGroupNumber;
    }
    
    public BigDecimal getIllegalGroupNumberMom() {
        return this.illegalGroupNumberMom;
    }
    
    public void setErrTrafficNumber(BigDecimal errTrafficNumber) {
        this.errTrafficNumber = errTrafficNumber;
    }
    
    public void setErrTrafficPercent(BigDecimal errTrafficPercent) {
        this.errTrafficPercent = errTrafficPercent;
    }
    
    public void setErrTrafficNumberMom(BigDecimal errTrafficNumberMom) {
        this.errTrafficNumberMom = errTrafficNumberMom;
    }
    
    public void setErrTrafficPercentMom(BigDecimal errTrafficPercentMom) {
        this.errTrafficPercentMom = errTrafficPercentMom;
    }
    
    public void setErrTrafficTotle(BigDecimal errTrafficTotle) {
        this.errTrafficTotle = errTrafficTotle;
    }
    
    public void setErrTrafficTotleMom(BigDecimal errTrafficTotleMom) {
        this.errTrafficTotleMom = errTrafficTotleMom;
    }
    
    public void setErrSubsTotle(BigDecimal errSubsTotle) {
        this.errSubsTotle = errSubsTotle;
    }
    
    public void setErrSubsTotleMom(BigDecimal errSubsTotleMom) {
        this.errSubsTotleMom = errSubsTotleMom;
    }
    
    public void setPaySystemGroupNumber(BigDecimal paySystemGroupNumber) {
        this.paySystemGroupNumber = paySystemGroupNumber;
    }
    
    public void setPaySystemGroupNumberMom(BigDecimal paySystemGroupNumberMom) {
        this.paySystemGroupNumberMom = paySystemGroupNumberMom;
    }
    
    public void setIllegalGroupNumber(BigDecimal illegalGroupNumber) {
        this.illegalGroupNumber = illegalGroupNumber;
    }
    
    public void setIllegalGroupNumberMom(BigDecimal illegalGroupNumberMom) {
        this.illegalGroupNumberMom = illegalGroupNumberMom;
    }
    

}
