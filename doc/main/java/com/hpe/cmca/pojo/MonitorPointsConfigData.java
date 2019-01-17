/**
 * com.hpe.cmca.pojo.MonitorPointsConfigData.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.pojo;


/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2017-11-24 下午2:47:12
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-11-24 	   hufei 	         1. Created this class. 
 * </pre>  
 */
public class MonitorPointsConfigData {
    private String pointVersionId;//监控点版本ID
    private String pointCode;//监控点ID
    private String versionCode;//版本ID
    private String assemblyId;//组件ID组
    private String assemblyType;//组件ID组
    private String assemblyOrder;//组件排序方式
    private String createPerson;//创建人
    private String createTime;//创建时间
    private String updateTime;//更新时间
    private int isOpen;//是否为默认版本
    
    public String getPointVersionId() {
        return this.pointVersionId;
    }
    
    
    public String getVersionCode() {
        return this.versionCode;
    }
    
    public String getAssemblyId() {
        return this.assemblyId;
    }
    
    public String getAssemblyOrder() {
        return this.assemblyOrder;
    }
    
    public String getCreatePerson() {
        return this.createPerson;
    }
    
    public String getCreateTime() {
        return this.createTime;
    }
    
    public String getUpdateTime() {
        return this.updateTime;
    }
    
    public int getIsOpen() {
        return this.isOpen;
    }
    
    public void setPointVersionId(String pointVersionId) {
        this.pointVersionId = pointVersionId;
    }
    
    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }
    
    public void setAssemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
    }
    
    public void setAssemblyOrder(String assemblyOrder) {
        this.assemblyOrder = assemblyOrder;
    }
    
    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }
    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    
    public String getAssemblyType() {
        return this.assemblyType;
    }

    
    public void setAssemblyType(String assemblyType) {
        this.assemblyType = assemblyType;
    }


    
    public String getPointCode() {
        return this.pointCode;
    }


    
    public void setPointCode(String pointCode) {
        this.pointCode = pointCode;
    }
    

}
