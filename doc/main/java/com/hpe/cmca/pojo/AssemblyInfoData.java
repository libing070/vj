/**
 * com.hpe.cmca.pojo.ControlInfoData.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.pojo;


/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2017-11-24 下午2:40:23
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-11-24 	   hufei 	         1. Created this class. 
 * </pre>  
 */
public class AssemblyInfoData {
    //组件信息表数据；后续如有必要，可以考虑单独为一个数据类。
    private String assemblyId; //组件ID
    private String assemblyName; //组件名称
    private String assemblyLayout;//组件内布局,
    private String controlIdArrays;//控件编号（A11;B12;C33） 
    private String pointVersionId;//监控点版本ID
    private String assemblyType;//组件类型
    
    public String getAssemblyId() {
        return this.assemblyId;
    }
    
    public String getAssemblyName() {
        return this.assemblyName;
    }
    
    public String getAssemblyLayout() {
        return this.assemblyLayout;
    }
    
    public String getControlIdArrays() {
        return this.controlIdArrays;
    }
    
    public String getPointVersionId() {
        return this.pointVersionId;
    }
    
    public String getAssemblyType() {
        return this.assemblyType;
    }
    
    public void setAssemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
    }
    
    public void setAssemblyName(String assemblyName) {
        this.assemblyName = assemblyName;
    }
    
    public void setAssemblyLayout(String assemblyLayout) {
        this.assemblyLayout = assemblyLayout;
    }
    
    public void setControlIdArrays(String controlIdArrays) {
        this.controlIdArrays = controlIdArrays;
    }
    
    public void setPointVersionId(String pointVersionId) {
        this.pointVersionId = pointVersionId;
    }
    
    public void setAssemblyType(String assemblyType) {
        this.assemblyType = assemblyType;
    }

}
