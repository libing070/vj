/**
 * com.hpe.cmca.pojo.SjkgData.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.pojo;


/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2017-11-10 上午10:47:53
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-11-10 	   hufei 	         1. Created this class. 
 * </pre>  
 */
public class SjkgData {
    private String audTrm; //审计月
    private String subjectCode; //专题ID
    private String subjectName; //专题名
    private int switchState; //开关状态
    private int switchType; //开关类型
    private String switchTypeName; //开关类型中文名
    private String managerOprPerson; //管理员操作人
    private String oprPerson; //内审操作人
    private String oprTime;//内审操作时间
    private String managerOprTime;//管理员操作时间
    private String createPerson; //创建人
    private String createTime; //创建时间
    private String queryOrAdd; //是查詢還是新增
    private int switchStateByRole;//开关状态-和用户角色有关，专门用于查询table
    private String roldId;
    
    public String getAudTrm() {
        return this.audTrm;
    }
    
    public String getSubjectCode() {
        return this.subjectCode;
    }
    
    public String getSubjectName() {
        return this.subjectName;
    }
    
    public int getSwitchState() {
        return this.switchState;
    }
    
    public int getSwitchType() {
        return this.switchType;
    }
    
    public String getSwitchTypeName() {
        return this.switchTypeName;
    }
    
    public String getManagerOprPerson() {
        return this.managerOprPerson;
    }
    
    public String getOprPerson() {
        return this.oprPerson;
    }
    
    public void setAudTrm(String audTrm) {
        this.audTrm = audTrm;
    }
    
    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
    
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    public void setSwitchState(int switchState) {
        this.switchState = switchState;
    }
    
    public void setSwitchType(int switchType) {
        this.switchType = switchType;
    }
    
    public void setSwitchTypeName(String switchTypeName) {
        this.switchTypeName = switchTypeName;
    }
    
    public void setManagerOprPerson(String managerOprPerson) {
        this.managerOprPerson = managerOprPerson;
    }
    
    public void setOprPerson(String oprPerson) {
        this.oprPerson = oprPerson;
    }

    
    public String getOprTime() {
        return this.oprTime;
    }

    
    public String getManagerOprTime() {
        return this.managerOprTime;
    }

    
    public String getCreatePerson() {
        return this.createPerson;
    }

    
    public String getCreateTime() {
        return this.createTime;
    }

    
    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    
    public void setOprTime(String oprTime) {
        this.oprTime = oprTime;
    }

    
    public void setManagerOprTime(String managerOprTime) {
        this.managerOprTime = managerOprTime;
    }

    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    
    public String getQueryOrAdd() {
        return this.queryOrAdd;
    }

    
    public void setQueryOrAdd(String queryOrAdd) {
        this.queryOrAdd = queryOrAdd;
    }

    
    public int getSwitchStateByRole() {
        return this.switchStateByRole;
    }

    
    public void setSwitchStateByRole(int switchStateByRole) {
        this.switchStateByRole = switchStateByRole;
    }

    
    public String getRoldId() {
        return this.roldId;
    }

    
    public void setRoldId(String roldId) {
        this.roldId = roldId;
    }

}
