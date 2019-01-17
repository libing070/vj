package com.hpe.cmca.util;

import java.io.Serializable;


public class Key_Value implements Comparable ,Serializable{

	private String k;
	private String v;

	public Key_Value(String k, String v) {
		this.k = k;
		this.v = v;

	}

	public boolean isContains(String x) {
		return this.k.equals(x) || this.v.equals(x);
	}

	public Key_Value() {

	}

	public String getk() {
		return k;
	}

	public void setk(String k) {
		this.k = k;
	}

	public String getv() {
		return v;
	}

	public void setv(String v) {
		this.v = v;
	}

	/**
	 * 降序
	 */
	@Override
	public int compareTo(Object o) {

		Key_Value kv = (Key_Value) o;
		double o1v = Math.abs(Double.parseDouble(this.getv()));
		double o2v = Math.abs(Double.parseDouble(kv.getv()));

		return o1v > o2v ? -1 : o1v == o2v ? 0 : 1;

	}

	@Override
	public String toString() { 
		return "{"+this.k + ":" + this.v+"}";
	}
	public static void main(String[] args) {
//		List<Key_Value> l = new ArrayList<Key_Value>();
//		l.add(new Key_Value("aaa","111"));
//		l.add(new Key_Value("bbb","222"));
//		l.add(new Key_Value("ccc","333"));
//		l.add(new Key_Value("ddd","444"));
//		l.add(new Key_Value("eee","555"));
//		
//		System.out.println(l);
		
		System.out.println(Json.Encode(true));
	}

}
