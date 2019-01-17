/**
 * com.hpe.cmca.finals.RoleEnum.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.finals;

/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2017-6-6 上午11:04:58
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-6-6 	   hufei 	         1. Created this class.
 * </pre>
 */
public enum RoleEnum {
    internalAudit("18","2"),
    hpe("10009","1"),
    hpeyz("12","4"),
    hpetest("10000","1");
    private String realRoleID;
    private String sjkgRoleID;

    private RoleEnum(String realRoleID, String sjkgRoleID) {
	this.realRoleID = realRoleID;
	this.sjkgRoleID = sjkgRoleID;
    }

    public String getRealRoleID() {
	return realRoleID;
    }

    public void setRealRoleID(String realRoleID) {
	this.realRoleID = realRoleID;
    }

    public String getSjkgRoleID() {
	return sjkgRoleID;
    }

    public void setSjkgRoleID(String sjkgRoleID) {
	this.sjkgRoleID = sjkgRoleID;
    }
    public static String getSjkgIDByRealID(String realRoleID) {  
        for (RoleEnum c : RoleEnum.values()) {  
            if (c.getRealRoleID().equals(realRoleID)) {  
                return c.sjkgRoleID;  
            }else if(Integer.parseInt(realRoleID) >= 163 && Integer.parseInt(realRoleID) <= 193){
            	return "3";
            }  
        }  
        return null;  
    }  

}
