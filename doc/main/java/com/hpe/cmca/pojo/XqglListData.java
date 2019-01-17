/**
 * com.hpe.cmca.pojo.XqglListData.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.pojo;


import java.util.List;

/**
 * <pre>
 * Desc：
 * @author   hufei
 * @refactor hufei
 * @date     2018-4-17 上午10:37:27
 * @version  1.0
 *
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  2018-4-17 	   hufei 	         1. Created this class.
 * </pre>
 */
public class XqglListData {
    private String startReqTime;	// 需求提出-开始时间
    private String endReqTime;	  // 需求提出-结束时间

    private String reqDestPerson;       // 需求负责人

    private String reqTbNm;	   // 结果表名|分隔

    private String reqTime;//需求提出时间
    private String reqNm;//需求名称
    private String reqSrcType;//需求提出类型
    private String reqSrcPerson;//需求提出人
    private String reqSrcPersonId;//需求提出人


    private String reqSrcDep;//需求提出部门
    private String reqApprovePerson;//需求审批人
    private String 	reqApprovePersonId;//需求审批人ID
    private String reqExpectTime;//期望完成时间
    private String reqStatus;//需求状态

    private List<String> reqStatusList;//需求状态列表

    private String reqId;//需求编号
    private String reqResultAddr;//生成文件结果

    public String getReqApprovePersonId() {
        return reqApprovePersonId;
    }

    public void setReqApprovePersonId(String reqApprovePersonId) {
        this.reqApprovePersonId = reqApprovePersonId;
    }

    public String getReqSrcPersonId() {
        return reqSrcPersonId;
    }

    public void setReqSrcPersonId(String reqSrcPersonId) {
        this.reqSrcPersonId = reqSrcPersonId;
    }
    public String getReqSrcType() {
        return reqSrcType;
    }

    public void setReqSrcType(String reqSrcType) {
        this.reqSrcType = reqSrcType;
    }

    public String getReqSrcDep() {
        return reqSrcDep;
    }

    public void setReqSrcDep(String reqSrcDep) {
        this.reqSrcDep = reqSrcDep;
    }

    public String getReqApprovePerson() {
        return reqApprovePerson;
    }

    public void setReqApprovePerson(String reqApprovePerson) {
        this.reqApprovePerson = reqApprovePerson;
    }

    public String getReqExpectTime() {
        return reqExpectTime;
    }

    public void setReqExpectTime(String reqExpectTime) {
        this.reqExpectTime = reqExpectTime;
    }

    public String getStartReqTime() {
        return this.startReqTime;
    }

    public String getEndReqTime() {
        return this.endReqTime;
    }

    public String getReqStatus() {
        return this.reqStatus;
    }

    public List<String> getReqStatusList() {
        return reqStatusList;
    }

    public void setReqStatusList(List<String> reqStatusList) {
        this.reqStatusList = reqStatusList;
    }

    public String getReqDestPerson() {
        return this.reqDestPerson;
    }

    public String getReqNm() {
        return this.reqNm;
    }

    public String getReqId() {
        return this.reqId;
    }

    public String getReqSrcPerson() {
        return this.reqSrcPerson;
    }

    public String getReqTime() {
        return this.reqTime;
    }

    public String getReqTbNm() {
        return this.reqTbNm;
    }

    public void setStartReqTime(String startReqTime) {
        this.startReqTime = startReqTime;
    }

    public void setEndReqTime(String endReqTime) {
        this.endReqTime = endReqTime;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }

    public void setReqDestPerson(String reqDestPerson) {
        this.reqDestPerson = reqDestPerson;
    }

    public void setReqNm(String reqNm) {
        this.reqNm = reqNm;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public void setReqSrcPerson(String reqSrcPerson) {
        this.reqSrcPerson = reqSrcPerson;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public void setReqTbNm(String reqTbNm) {
        this.reqTbNm = reqTbNm;
    }


    public String getReqResultAddr() {
        return this.reqResultAddr;
    }


    public void setReqResultAddr(String reqResultAddr) {
        this.reqResultAddr = reqResultAddr;
    }
}
