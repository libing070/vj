package com.hpe.cmca.util;

public class Key_Value_Parent extends Key_Value {

	private String p;

	public Key_Value_Parent(String k, String v, String p) {

		super(k, v);
		this.p = p;
	}

	public Key_Value_Parent() {
		// TODO Auto-generated constructor stub
	}

	public String getp() {
		return p;
	}

	public void setp(String p) {
		this.p = p;
	}
}
