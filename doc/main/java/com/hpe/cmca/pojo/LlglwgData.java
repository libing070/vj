/**
 * com.hpe.cmca.pojo.LlglwgData.java
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
 * @date     2018-2-5 上午10:34:11
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2018-2-5 	   hufei 	         1. Created this class. 
 * </pre>  
 */
public class LlglwgData {
    private int rn; //序号
    private int prvdId; //省份ID
    private int ctyId; //地市ID
    private String prvdName;//省份名称
    private String audTrm;//审计月
    private BigDecimal errTrafficNumber;//流量违规数量
    private BigDecimal errTrafficPercent;//流量违规占比
    private BigDecimal illegalGroupNumber;//违规集团用户数
    private BigDecimal illegalGroupPercent;//违规集团用户数占比
    
    private BigDecimal errTrafficTotle;//异常次数
    
    private BigDecimal trafficValueTotle;//总赠送流量
    private BigDecimal trafficTotle;//总赠送次数
    private BigDecimal subsTotle;//总赠送用户数
    private BigDecimal errSubsTotle;//异常赠送涉及用户数
    
    private BigDecimal paySystemSubsOrg;//统付集团客户数
    private BigDecimal paySystemValueOrg;//统付涉及统付流量总值（G）
    private BigDecimal paySystemNumber;//统付流量订购次数
    private BigDecimal paySystemPhoneTotle;//统付涉及号码总数
    private BigDecimal errPaySystemPhoneTotle;//疑似违规转售涉及号码总数
    private BigDecimal errPaySystemPhonePer;//疑似违规转售涉及号码总数占比
    
    private BigDecimal errTrafficNumberMom;//流量违规数量增幅
    
    private BigDecimal errHighFrequency;//高频赠送流量值
    private BigDecimal errHighLimit;//高额赠送流量值
    private BigDecimal abnormalSubs;//向非正常用户赠送流量值
    
    
    public int getPrvdId() {
        return this.prvdId;
    }
    
    public String getPrvdName() {
        return this.prvdName;
    }
    
    public BigDecimal getErrTrafficNumber() {
        return this.errTrafficNumber;
    }
    
    public BigDecimal getErrTrafficPercent() {
        return this.errTrafficPercent;
    }
    
    public void setPrvdId(int prvdId) {
        this.prvdId = prvdId;
    }
    
    public void setPrvdName(String prvdName) {
        this.prvdName = prvdName;
    }
    
    public void setErrTrafficNumber(BigDecimal errTrafficNumber) {
        this.errTrafficNumber = errTrafficNumber;
    }
    
    public void setErrTrafficPercent(BigDecimal errTrafficPercent) {
        this.errTrafficPercent = errTrafficPercent;
    }

    
    public String getAudTrm() {
        return this.audTrm;
    }

    
    public void setAudTrm(String audTrm) {
        this.audTrm = audTrm;
    }

    
    public BigDecimal getIllegalGroupNumber() {
        return this.illegalGroupNumber;
    }

    
    public void setIllegalGroupNumber(BigDecimal illegalGroupNumber) {
        this.illegalGroupNumber = illegalGroupNumber;
    }

    
    public BigDecimal getIllegalGroupPercent() {
        return this.illegalGroupPercent;
    }

    
    public void setIllegalGroupPercent(BigDecimal illegalGroupPercent) {
        this.illegalGroupPercent = illegalGroupPercent;
    }

    
    public int getCtyId() {
        return this.ctyId;
    }

    
    public void setCtyId(int ctyId) {
        this.ctyId = ctyId;
    }

    
    public int getRn() {
        return this.rn;
    }

    
    public void setRn(int rn) {
        this.rn = rn;
    }

    
    public BigDecimal getTrafficValueTotle() {
        return this.trafficValueTotle;
    }

    
    public BigDecimal getTrafficTotle() {
        return this.trafficTotle;
    }

    
    public BigDecimal getSubsTotle() {
        return this.subsTotle;
    }

    
    public BigDecimal getErrTrafficTotle() {
        return this.errTrafficTotle;
    }

    
    public BigDecimal getErrSubsTotle() {
        return this.errSubsTotle;
    }

    
    public void setTrafficValueTotle(BigDecimal trafficValueTotle) {
        this.trafficValueTotle = trafficValueTotle;
    }

    
    public void setTrafficTotle(BigDecimal trafficTotle) {
        this.trafficTotle = trafficTotle;
    }

    
    public void setSubsTotle(BigDecimal subsTotle) {
        this.subsTotle = subsTotle;
    }

    
    public void setErrTrafficTotle(BigDecimal errTrafficTotle) {
        this.errTrafficTotle = errTrafficTotle;
    }

    
    public void setErrSubsTotle(BigDecimal errSubsTotle) {
        this.errSubsTotle = errSubsTotle;
    }

    
    public BigDecimal getPaySystemSubsOrg() {
        return this.paySystemSubsOrg;
    }

    
    public BigDecimal getPaySystemValueOrg() {
        return this.paySystemValueOrg;
    }

    
    public BigDecimal getPaySystemNumber() {
        return this.paySystemNumber;
    }

    
    public BigDecimal getPaySystemPhoneTotle() {
        return this.paySystemPhoneTotle;
    }

    
    public BigDecimal getErrPaySystemPhoneTotle() {
        return this.errPaySystemPhoneTotle;
    }

    
    public void setPaySystemSubsOrg(BigDecimal paySystemSubsOrg) {
        this.paySystemSubsOrg = paySystemSubsOrg;
    }

    
    public void setPaySystemValueOrg(BigDecimal paySystemValueOrg) {
        this.paySystemValueOrg = paySystemValueOrg;
    }

    
    public void setPaySystemNumber(BigDecimal paySystemNumber) {
        this.paySystemNumber = paySystemNumber;
    }

    
    public void setPaySystemPhoneTotle(BigDecimal paySystemPhoneTotle) {
        this.paySystemPhoneTotle = paySystemPhoneTotle;
    }

    
    public void setErrPaySystemPhoneTotle(BigDecimal errPaySystemPhoneTotle) {
        this.errPaySystemPhoneTotle = errPaySystemPhoneTotle;
    }

    
    public BigDecimal getErrTrafficNumberMom() {
        return this.errTrafficNumberMom;
    }

    
    public void setErrTrafficNumberMom(BigDecimal errTrafficNumberMom) {
        this.errTrafficNumberMom = errTrafficNumberMom;
    }

    
    public BigDecimal getErrHighFrequency() {
        return this.errHighFrequency;
    }

    
    public BigDecimal getErrHighLimit() {
        return this.errHighLimit;
    }

    
    public BigDecimal getAbnormalSubs() {
        return this.abnormalSubs;
    }

    
    public void setErrHighFrequency(BigDecimal errHighFrequency) {
        this.errHighFrequency = errHighFrequency;
    }

    
    public void setErrHighLimit(BigDecimal errHighLimit) {
        this.errHighLimit = errHighLimit;
    }

    
    public void setAbnormalSubs(BigDecimal abnormalSubs) {
        this.abnormalSubs = abnormalSubs;
    }

    
    public BigDecimal getErrPaySystemPhonePer() {
        return this.errPaySystemPhonePer;
    }

    
    public void setErrPaySystemPhonePer(BigDecimal errPaySystemPhonePer) {
        this.errPaySystemPhonePer = errPaySystemPhonePer;
    }

}
