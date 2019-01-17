package com.hpe.cmwa.util;

public class Key_Value_Parent extends Key_Value {

	private String p;

	public Key_Value_Parent(String k, String v, String p) {

		super(k, v);
		this.p = p;
	}

	public Key_Value_Parent() {
	}

	public String getp() {
		return p;
	}

	public void setp(String p) {
		this.p = p;
	}
}
