/**
 * SSO_WebServiceSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hpe.cmca.webservice.sms;

public interface SSO_WebServiceSoap_PortType extends java.rmi.Remote {
    public boolean upDateUser(java.lang.String userName, java.lang.String displayName, java.lang.String email, java.lang.String mobile, int dept, java.lang.String systemID) throws java.rmi.RemoteException;
    public boolean addDataUser(java.lang.String userName, java.lang.String banchId, java.lang.String displayName, java.lang.String email, java.lang.String mobile, int dept, java.lang.String systemID) throws java.rmi.RemoteException;
    public boolean delDataUser(java.lang.String userName, java.lang.String systemID) throws java.rmi.RemoteException;
    public void yiJingState(java.lang.String userName, java.lang.String session_id, java.lang.String systemID) throws java.rmi.RemoteException;
    public void yiJingStateDispose(java.lang.String userName) throws java.rmi.RemoteException;
    public int isYJLogin(java.lang.String userName) throws java.rmi.RemoteException;
    public java.lang.String GXPTGetLogon(java.lang.String validateID, int systemID) throws java.rmi.RemoteException;
    public java.lang.String getQuestionInfo(java.lang.String userid) throws java.rmi.RemoteException;
    public boolean sendSMS(java.lang.String mobile, java.lang.String content) throws java.rmi.RemoteException;
    public java.lang.String getLogonMs(java.lang.String userName, java.lang.String mobile, java.lang.String validateNO, java.lang.String openRequest) throws java.rmi.RemoteException;
    public java.lang.String implStakeoutData(java.lang.String userName, java.lang.String data) throws java.rmi.RemoteException;
    public int addSudden(java.lang.String title, java.lang.String description, java.lang.String department, java.lang.String proposer, java.lang.String proposedate, java.lang.String expectdate, java.lang.String developers, java.lang.String linkman) throws java.rmi.RemoteException;
    public java.lang.String getLatestModelCount(java.lang.String userName) throws java.rmi.RemoteException;
}
