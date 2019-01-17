/**
 * com.hpe.cmca.pojo.XqglData.java
 * Copyright (c) 2018 xx Development Company L.P.
 * All rights reserved.
 */
package com.hpe.cmca.pojo;

/**
 * <pre>
 * Desc：
 * @author   hufei
 * @refactor hufei
 * @date     2018-4-16 下午4:14:06
 * @version  1.0
 *
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  2018-4-16 	   hufei 	         1. Created this class.
 * </pre>
 */
public class XqglData {
    private String  reqSrcDep;//需求提出部门
    private String reqSrcType;//需求提出类型
    private String reqExpectTime;//期望完成时间
    private String 	reqApprovePerson;//需求审批人
    private String 	reqApprovePersonId;//需求审批人ID
    private String 	reqApproveTime;//需求审批时间
    private String 	reqApproveSuggestion;//需求审批意见
    private String 	reqFinishTime;//需求处理完成时间
    private String 	reqHandleAttachAddr;//需求处理附件

    private String reqStatus;	   // 需求状态
    private String reqDestPerson;       // 需求负责人
    private String reqDestPersonName;       // 需求负责人名称
    private String reqNm;	       // 需求名称
    private String reqId;	       // 需求编号
    private String reqSrcPerson;      // 需求提出人
    private String reqSrcPersonId; // 需求提出人ID
    private String reqType;	    // 工作分类
    private String reqTypeName;	    // 工作分类名称
    private String reqTime;	    // 需求提出时间
    private String reqDescription;     // 需求描述
    private String reqSubmitPerson;   // 需求提交人
    private String reqWorkload;	// 实际工作量
    private String reqTbNm;	   // 结果表名|分隔
    private String reqColNm;	  // 字段名
    private String reqFinishComments; // 任务完成说明
    private String reqResultAddr; //需求结果文件FTP地址
    private String reqAttachAddr; //需求附件FTP地址
    private String srcTb1Nm;	  // 源接口表1名称
    private String srcTb1Audtrm;      // 源接口表1数据周期
    private String srcTb1Sensitive;   // 源接口表1是否金库审批
    private String srcTb1Output;      // 源接口表1是否批量导出
    private String srcTb2Nm;	  // 源接口表2名称
    private String srcTb2Audtrm;      // 源接口表2数据周期
    private String srcTb2Sensitive;   // 源接口表2是否金库审批
    private String srcTb2Output;      // 源接口表2是否批量导出
    private String srcTb3Nm;	  // 源接口表3名称
    private String srcTb3Audtrm;      // 源接口表3数据周期
    private String srcTb3Sensitive;   // 源接口表3是否金库审批
    private String srcTb3Output;      // 源接口表3是否批量导出
    private String srcTb4Nm;	  // 源接口表4名称
    private String srcTb4Audtrm;      // 源接口表4数据周期
    private String srcTb4Sensitive;   // 源接口表4是否金库审批
    private String srcTb4Output;      // 源接口表4是否批量导出
    private String srcTb5Nm;	  // 源接口表5名称
    private String srcTb5Audtrm;      // 源接口表5数据周期
    private String srcTb5Sensitive;   // 源接口表5是否金库审批
    private String srcTb5Output;      // 源接口表5是否批量导出
    private String reqResultStartTime; //文件生成开始时间
    private String reqResultEndTime; //文件生成结束时间
    private String reqResultGenerter; //文件生成人
    private String reqResultState; //文件生成状态

//    private String reqDestPersonName; //需求负责人
//    private String reqSrcPersonName; //需求提出人
//    private String reqStatusName; //需求状态名
//    private String reqTypeName; //工作分类名

    public String getReqApprovePersonId() {
        return reqApprovePersonId;
    }

    public void setReqApprovePersonId(String reqApprovePersonId) {
        this.reqApprovePersonId = reqApprovePersonId;
    }

    public String getReqSrcDep() {
        return reqSrcDep;
    }

    public void setReqSrcDep(String reqSrcDep) {
        this.reqSrcDep = reqSrcDep;
    }

    public String getReqSrcType() {
        return reqSrcType;
    }

    public void setReqSrcType(String reqSrcType) {
        this.reqSrcType = reqSrcType;
    }

    public String getReqExpectTime() {
        return reqExpectTime;
    }

    public void setReqExpectTime(String reqExpectTime) {
        this.reqExpectTime = reqExpectTime;
    }

    public String getReqApprovePerson() {
        return reqApprovePerson;
    }

    public void setReqApprovePerson(String reqApprovePerson) {
        this.reqApprovePerson = reqApprovePerson;
    }

    public String getReqApproveTime() {
        return reqApproveTime;
    }

    public void setReqApproveTime(String reqApproveTime) {
        this.reqApproveTime = reqApproveTime;
    }

    public String getReqApproveSuggestion() {
        return reqApproveSuggestion;
    }

    public void setReqApproveSuggestion(String reqApproveSuggestion) {
        this.reqApproveSuggestion = reqApproveSuggestion;
    }

    public String getReqFinishTime() {
        return reqFinishTime;
    }

    public void setReqFinishTime(String reqFinishTime) {
        this.reqFinishTime = reqFinishTime;
    }

    public String getReqHandleAttachAddr() {
        return reqHandleAttachAddr;
    }

    public void setReqHandleAttachAddr(String reqHandleAttachAddr) {
        this.reqHandleAttachAddr = reqHandleAttachAddr;
    }

    public String getReqStatus() {
	return this.reqStatus;
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


    public String getReqSrcPerson() {
        return this.reqSrcPerson;
    }

    public String getReqSrcPersonId() {
        return reqSrcPersonId;
    }

    public void setReqSrcPersonId(String reqSrcPersonId) {
        this.reqSrcPersonId = reqSrcPersonId;
    }

    public String getReqType() {
        return this.reqType;
    }


    public String getReqTime() {
        return this.reqTime;
    }


    public String getReqDescription() {
        return this.reqDescription;
    }


    public String getReqSubmitPerson() {
        return this.reqSubmitPerson;
    }


    public String getReqWorkload() {
        return this.reqWorkload;
    }


    public String getReqTbNm() {
        return this.reqTbNm;
    }


    public String getReqColNm() {
        return this.reqColNm;
    }

    public String getReqDestPersonName() {
        return reqDestPersonName;
    }

    public void setReqDestPersonName(String reqDestPersonName) {
        this.reqDestPersonName = reqDestPersonName;
    }

    public String getReqTypeName() {
        return reqTypeName;
    }

    public void setReqTypeName(String reqTypeName) {
        this.reqTypeName = reqTypeName;
    }

    public String getReqFinishComments() {
        return this.reqFinishComments;
    }



	public String getReqResultAddr() {
		return this.reqResultAddr;
	}


	public void setReqResultAddr(String reqResultAddr) {
		this.reqResultAddr = reqResultAddr;
	}


	public String getReqAttachAddr() {
		return this.reqAttachAddr;
	}


	public void setReqAttachAddr(String reqAttachAddr) {
		this.reqAttachAddr = reqAttachAddr;
	}

	public String getSrcTb1Nm() {
        return this.srcTb1Nm;
    }


    public String getSrcTb1Audtrm() {
        return this.srcTb1Audtrm;
    }


    public String getSrcTb1Sensitive() {
        return this.srcTb1Sensitive;
    }


    public String getSrcTb1Output() {
        return this.srcTb1Output;
    }


    public String getSrcTb2Nm() {
        return this.srcTb2Nm;
    }


    public String getSrcTb2Audtrm() {
        return this.srcTb2Audtrm;
    }


    public String getSrcTb2Sensitive() {
        return this.srcTb2Sensitive;
    }


    public String getSrcTb2Output() {
        return this.srcTb2Output;
    }


    public String getSrcTb3Nm() {
        return this.srcTb3Nm;
    }


    public String getSrcTb3Audtrm() {
        return this.srcTb3Audtrm;
    }


    public String getSrcTb3Sensitive() {
        return this.srcTb3Sensitive;
    }


    public String getSrcTb3Output() {
        return this.srcTb3Output;
    }


    public String getSrcTb4Nm() {
        return this.srcTb4Nm;
    }


    public String getSrcTb4Audtrm() {
        return this.srcTb4Audtrm;
    }


    public String getSrcTb4Sensitive() {
        return this.srcTb4Sensitive;
    }


    public String getSrcTb4Output() {
        return this.srcTb4Output;
    }


    public String getSrcTb5Nm() {
        return this.srcTb5Nm;
    }


    public String getSrcTb5Audtrm() {
        return this.srcTb5Audtrm;
    }


    public String getSrcTb5Sensitive() {
        return this.srcTb5Sensitive;
    }


    public String getSrcTb5Output() {
        return this.srcTb5Output;
    }


    public void setReqSrcPerson(String reqSrcPerson) {
        this.reqSrcPerson = reqSrcPerson;
    }


    public void setReqType(String reqType) {
        this.reqType = reqType;
    }


    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }


    public void setReqDescription(String reqDescription) {
        this.reqDescription = reqDescription;
    }


    public void setReqSubmitPerson(String reqSubmitPerson) {
        this.reqSubmitPerson = reqSubmitPerson;
    }


    public void setReqWorkload(String reqWorkload) {
        this.reqWorkload = reqWorkload;
    }


    public void setReqTbNm(String reqTbNm) {
        this.reqTbNm = reqTbNm;
    }


    public void setReqColNm(String reqColNm) {
        this.reqColNm = reqColNm;
    }


    public void setReqFinishComments(String reqFinishComments) {
        this.reqFinishComments = reqFinishComments;
    }


    public void setSrcTb1Nm(String srcTb1Nm) {
        this.srcTb1Nm = srcTb1Nm;
    }


    public void setSrcTb1Audtrm(String srcTb1Audtrm) {
        this.srcTb1Audtrm = srcTb1Audtrm;
    }


    public void setSrcTb1Sensitive(String srcTb1Sensitive) {
        this.srcTb1Sensitive = srcTb1Sensitive;
    }


    public void setSrcTb1Output(String srcTb1Output) {
        this.srcTb1Output = srcTb1Output;
    }


    public void setSrcTb2Nm(String srcTb2Nm) {
        this.srcTb2Nm = srcTb2Nm;
    }


    public void setSrcTb2Audtrm(String srcTb2Audtrm) {
        this.srcTb2Audtrm = srcTb2Audtrm;
    }


    public void setSrcTb2Sensitive(String srcTb2Sensitive) {
        this.srcTb2Sensitive = srcTb2Sensitive;
    }


    public void setSrcTb2Output(String srcTb2Output) {
        this.srcTb2Output = srcTb2Output;
    }


    public void setSrcTb3Nm(String srcTb3Nm) {
        this.srcTb3Nm = srcTb3Nm;
    }


    public void setSrcTb3Audtrm(String srcTb3Audtrm) {
        this.srcTb3Audtrm = srcTb3Audtrm;
    }


    public void setSrcTb3Sensitive(String srcTb3Sensitive) {
        this.srcTb3Sensitive = srcTb3Sensitive;
    }


    public void setSrcTb3Output(String srcTb3Output) {
        this.srcTb3Output = srcTb3Output;
    }


    public void setSrcTb4Nm(String srcTb4Nm) {
        this.srcTb4Nm = srcTb4Nm;
    }


    public void setSrcTb4Audtrm(String srcTb4Audtrm) {
        this.srcTb4Audtrm = srcTb4Audtrm;
    }


    public void setSrcTb4Sensitive(String srcTb4Sensitive) {
        this.srcTb4Sensitive = srcTb4Sensitive;
    }


    public void setSrcTb4Output(String srcTb4Output) {
        this.srcTb4Output = srcTb4Output;
    }


    public void setSrcTb5Nm(String srcTb5Nm) {
        this.srcTb5Nm = srcTb5Nm;
    }


    public void setSrcTb5Audtrm(String srcTb5Audtrm) {
        this.srcTb5Audtrm = srcTb5Audtrm;
    }


    public void setSrcTb5Sensitive(String srcTb5Sensitive) {
        this.srcTb5Sensitive = srcTb5Sensitive;
    }


    public void setSrcTb5Output(String srcTb5Output) {
        this.srcTb5Output = srcTb5Output;
    }


    public String getReqResultStartTime() {
        return this.reqResultStartTime;
    }


    public String getReqResultEndTime() {
        return this.reqResultEndTime;
    }


    public String getReqResultGenerter() {
        return this.reqResultGenerter;
    }


    public String getReqResultState() {
        return this.reqResultState;
    }


    public void setReqResultStartTime(String reqResultStartTime) {
        this.reqResultStartTime = reqResultStartTime;
    }


    public void setReqResultEndTime(String reqResultEndTime) {
        this.reqResultEndTime = reqResultEndTime;
    }


    public void setReqResultGenerter(String reqResultGenerter) {
        this.reqResultGenerter = reqResultGenerter;
    }


    public void setReqResultState(String reqResultState) {
        this.reqResultState = reqResultState;
    }


//    public String getReqDestPersonName() {
//        return this.reqDestPersonName;
//    }
//
//
//    public String getReqSrcPersonName() {
//        return this.reqSrcPersonName;
//    }
//
//
//    public String getReqStatusName() {
//        return this.reqStatusName;
//    }
//
//
//    public String getReqTypeName() {
//        return this.reqTypeName;
//    }
//
//
//    public void setReqDestPersonName(String reqDestPersonName) {
//        this.reqDestPersonName = reqDestPersonName;
//    }
//
//
//    public void setReqSrcPersonName(String reqSrcPersonName) {
//        this.reqSrcPersonName = reqSrcPersonName;
//    }
//
//
//    public void setReqStatusName(String reqStatusName) {
//        this.reqStatusName = reqStatusName;
//    }
//
//
//    public void setReqTypeName(String reqTypeName) {
//        this.reqTypeName = reqTypeName;
//    }

}
