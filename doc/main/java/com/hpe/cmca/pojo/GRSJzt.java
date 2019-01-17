package com.hpe.cmca.pojo;

public class GRSJzt {
	private Integer ztId;//专题id；
	private String ztNm;//专题名称
	public Integer getZtId() {
		return ztId;
	}
	public void setZtId(Integer ztId) {
		this.ztId = ztId;
	}
	public String getZtNm() {
		return ztNm;
	}
	public void setZtNm(String ztNm) {
		this.ztNm = ztNm;
	}
	@Override
	public String toString() {
		return "GRSJzt [ztId=" + ztId + ", ztNm=" + ztNm + "]";
	}
	
}
