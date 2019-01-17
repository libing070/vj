package com.hpe.cmwa.util;

import java.io.Serializable;

public class SimpleMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimpleMap() {

	};

	public SimpleMap(String value, String text) {
		this.value = value;
		this.text = text;
	};

	private String value;
	private String text;

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return (this.text == null ? "" : this.text);
	}

	public void setText(String text) {
		this.text = text;
	}

}
