/**
 * SSO_WebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hpe.cmwa.webservice.sms;

public class SSO_WebServiceLocator extends org.apache.axis.client.Service implements com.hpe.cmwa.webservice.sms.SSO_WebService {

    public SSO_WebServiceLocator() {
    }


    public SSO_WebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SSO_WebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SSO_WebServiceSoap
    private java.lang.String SSO_WebServiceSoap_address = "http://localhost/WebServrce/SSO_WebService.asmx";

    public java.lang.String getSSO_WebServiceSoapAddress() {
        return SSO_WebServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SSO_WebServiceSoapWSDDServiceName = "SSO_WebServiceSoap";

    public java.lang.String getSSO_WebServiceSoapWSDDServiceName() {
        return SSO_WebServiceSoapWSDDServiceName;
    }

    public void setSSO_WebServiceSoapWSDDServiceName(java.lang.String name) {
        SSO_WebServiceSoapWSDDServiceName = name;
    }

    public com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap_PortType getSSO_WebServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SSO_WebServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSSO_WebServiceSoap(endpoint);
    }

    public com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap_PortType getSSO_WebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap_BindingStub _stub = new com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap_BindingStub(portAddress, this);
            _stub.setPortName(getSSO_WebServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSSO_WebServiceSoapEndpointAddress(java.lang.String address) {
        SSO_WebServiceSoap_address = address;
    }


    // Use to get a proxy class for SSO_WebServiceSoap12
    private java.lang.String SSO_WebServiceSoap12_address = "http://localhost/WebServrce/SSO_WebService.asmx";

    public java.lang.String getSSO_WebServiceSoap12Address() {
        return SSO_WebServiceSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SSO_WebServiceSoap12WSDDServiceName = "SSO_WebServiceSoap12";

    public java.lang.String getSSO_WebServiceSoap12WSDDServiceName() {
        return SSO_WebServiceSoap12WSDDServiceName;
    }

    public void setSSO_WebServiceSoap12WSDDServiceName(java.lang.String name) {
        SSO_WebServiceSoap12WSDDServiceName = name;
    }

    public com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap_PortType getSSO_WebServiceSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SSO_WebServiceSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSSO_WebServiceSoap12(endpoint);
    }

    public com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap_PortType getSSO_WebServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap12Stub _stub = new com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap12Stub(portAddress, this);
            _stub.setPortName(getSSO_WebServiceSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSSO_WebServiceSoap12EndpointAddress(java.lang.String address) {
        SSO_WebServiceSoap12_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap_BindingStub _stub = new com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap_BindingStub(new java.net.URL(SSO_WebServiceSoap_address), this);
                _stub.setPortName(getSSO_WebServiceSoapWSDDServiceName());
                return _stub;
            }
            if (com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap12Stub _stub = new com.hpe.cmwa.webservice.sms.SSO_WebServiceSoap12Stub(new java.net.URL(SSO_WebServiceSoap12_address), this);
                _stub.setPortName(getSSO_WebServiceSoap12WSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SSO_WebServiceSoap".equals(inputPortName)) {
            return getSSO_WebServiceSoap();
        }
        else if ("SSO_WebServiceSoap12".equals(inputPortName)) {
            return getSSO_WebServiceSoap12();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("DIQuestion", "SSO_WebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("DIQuestion", "SSO_WebServiceSoap"));
            ports.add(new javax.xml.namespace.QName("DIQuestion", "SSO_WebServiceSoap12"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SSO_WebServiceSoap".equals(portName)) {
            setSSO_WebServiceSoapEndpointAddress(address);
        }
        else 
if ("SSO_WebServiceSoap12".equals(portName)) {
            setSSO_WebServiceSoap12EndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
