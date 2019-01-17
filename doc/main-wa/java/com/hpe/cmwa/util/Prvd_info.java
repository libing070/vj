package com.hpe.cmwa.util;

public class Prvd_info {

	private String prvdid;
	private String privdcd;
	private String privdnm;

	public Prvd_info(String prvdid, String privdcd, String privdnm) {
		super();
		this.prvdid = prvdid;
		this.privdcd = privdcd;
		this.privdnm = privdnm;
	}

	public String getPrvdid() {
		return prvdid;
	}

	public void setPrvdid(String prvdid) {
		this.prvdid = prvdid;
	}

	public String getPrivdcd() {
		return privdcd;
	}

	public void setPrivdcd(String privdcd) {
		this.privdcd = privdcd;
	}

	public String getPrivdnm() {
		return privdnm;
	}

	public void setPrivdnm(String privdnm) {
		this.privdnm = privdnm;
	}

	/**
	 * 对象是否包含指定信息
	 * 
	 * @param str
	 * @return
	 */
	public boolean containsInfo(String str) {
		return this.privdcd.equals(str) || this.privdnm.equals(str)
				|| this.prvdid.equals(str);
	}

	/**
	 * 对象是否模糊包含指定信息
	 * 
	 * @param str
	 * @return
	 */
	public boolean likeInfo(String str) {
		return this.privdcd.indexOf(str) + this.privdnm.indexOf(str)
				+ this.prvdid.indexOf(str) > -3;
	}
	 

}
