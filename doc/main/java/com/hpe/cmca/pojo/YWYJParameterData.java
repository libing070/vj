/**
 * com.hpe.cmca.pojo.YWYJParameterData.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.pojo;

/**
 * <pre>
 * Desc： 业务预警-参数类
 * @author   hufei
 * @refactor hufei
 * @date     2017-11-7 下午4:58:05
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-11-7 	   hufei 	         1. Created this class.
 * </pre>
 */
public class YWYJParameterData {

    private String audTrm; //审计月
    private int    prvdId; //省份ID
    private String pointCode; //监控点ID
    private String pointName; //监控点名称
    private String assemblyId; //组件ID
    private String controlId; //控件ID
    private String tableName; //数据库表名
    
   
    
    //控件信息表数据；后续如有必要，可以考虑单独为一个数据类。
    private String controlType; //控件类型
    private String controlName;//控件业务名称
    private String showField;//展示字段信息（X轴;Y1轴;Y2轴） 
    private String showName; //列字段的业务信息（X轴;Y1轴;Y2轴）
    private String screenField;//筛选条件字段信息
    private String[] screenFieldArray;//筛选条件字段信息
    private String querySql;//查询SQL信息
    private String showTitle;//图表Title
    private String showTooltip; //图表悬浮框
    
    public String getAudTrm() {
        return this.audTrm;
    }
    
    public int getPrvdId() {
        return this.prvdId;
    }
    
    public String getPointCode() {
        return this.pointCode;
    }
    
    public String getPointName() {
        return this.pointName;
    }
    
    public String getAssemblyId() {
        return this.assemblyId;
    }
    
    public String getControlId() {
        return this.controlId;
    }
    
    public String getTableName() {
        return this.tableName;
    }
    
    public void setAudTrm(String audTrm) {
        this.audTrm = audTrm;
    }
    
    public void setPrvdId(int prvdId) {
        this.prvdId = prvdId;
    }
    
    public void setPointCode(String pointCode) {
        this.pointCode = pointCode;
    }
    
    public void setPointName(String pointName) {
        this.pointName = pointName;
    }
    
    public void setAssemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
    }
    
    public void setControlId(String controlId) {
        this.controlId = controlId;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    
    public String getControlType() {
        return this.controlType;
    }

    
    public String getControlName() {
        return this.controlName;
    }


    
    public String getShowField() {
        return this.showField;
    }

    
    public String getShowName() {
        return this.showName;
    }

    
    public String getScreenField() {
        return this.screenField;
    }

    
    public String getQuerySql() {
        return this.querySql;
    }

    
    public String getShowTitle() {
        return this.showTitle;
    }

    
    public String getShowTooltip() {
        return this.showTooltip;
    }

    
    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    
    public void setControlName(String controlName) {
        this.controlName = controlName;
    }

    
    public void setShowField(String showField) {
        this.showField = showField;
    }

    
    public void setShowName(String showName) {
        this.showName = showName;
    }

    
    public void setScreenField(String screenField) {
        this.screenField = screenField;
    }

    
    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    
    public void setShowTitle(String showTitle) {
        this.showTitle = showTitle;
    }

    
    public void setShowTooltip(String showTooltip) {
        this.showTooltip = showTooltip;
    }

    
    public String[] getScreenFieldArray() {
        return this.screenFieldArray;
    }

    
    public void setScreenFieldArray(String[] screenFieldArray) {
        this.screenFieldArray = screenFieldArray;
    }

}
