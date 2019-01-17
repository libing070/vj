/**
 * SSO_WebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hpe.cmwa.webservice.sms;

public interface SSO_WebService extends javax.xml.rpc.Service {
    public java.lang.String getSSO_WebServiceSoapAddress();

    public com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap_PortType getSSO_WebServiceSoap() throws javax.xml.rpc.ServiceException;

    public com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap_PortType getSSO_WebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getSSO_WebServiceSoap12Address();

    public com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap_PortType getSSO_WebServiceSoap12() throws javax.xml.rpc.ServiceException;

    public com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap_PortType getSSO_WebServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
