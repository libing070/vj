package com.hpe.cmca.filter;


public class StaffSessBean {
	String casStaffId;
	String ipaddr;
	String BrowserVersion;
	String SessionId;
	
	public String getCasStaffId() {
		return this.casStaffId;
	}
	
	public void setCasStaffId(String casStaffId) {
		this.casStaffId = casStaffId;
	}
	
	public String getIpaddr() {
		return this.ipaddr;
	}
	
	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}
	
	public String getBrowserVersion() {
		return this.BrowserVersion;
	}
	
	public void setBrowserVersion(String browserVersion) {
		BrowserVersion = browserVersion;
	}
	
	public String getSessionId() {
		return this.SessionId;
	}
	
	public void setSessionId(String sessionId) {
		SessionId = sessionId;
	}

}
