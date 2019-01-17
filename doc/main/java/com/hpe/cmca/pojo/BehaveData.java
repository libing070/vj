package com.hpe.cmca.pojo;

import java.util.Date;

public class BehaveData {

	private String operationId;
	private String operator;
	private String msg;
	private String operationType;
	private String operateDate;
	private String operationName;
	private String plat;
	private String other;
	private String resourceUrl;
	private String clientAddress;
	private String serverAddress;
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getPlat() {
		return plat;
	}
	public void setPlat(String plat) {
		this.plat = plat;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getResourceUrl() {
		return resourceUrl;
	}
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	public String getClientAddress() {
		return clientAddress;
	}
	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}
	public String getServerAddress() {
		return serverAddress;
	}
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	@Override
	public String toString() {
		return "BehaveData [operationId=" + operationId + ", operator=" + operator + ", msg=" + msg + ", operationType="
				+ operationType + ", operateDate=" + operateDate + ", operationName=" + operationName + ", plat=" + plat
				+ ", other=" + other + ", resourceUrl=" + resourceUrl + ", clientAddress=" + clientAddress
				+ ", serverAddress=" + serverAddress + "]";
	}
	
	
}
