/**
 * com.hpe.cmca.filter.AuthorityType.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.finals;


/**
 * <pre>
 * Desc： 权限枚举
 * @author sinly
 * @refactor sinly
 * @date   2016-11-10 下午8:36:02
 * @version 1.0
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  2016-11-10 	   sinly 	         1. Created this class.
 * </pre>
 */
public enum AuthorityType {

	qdyksjjg("qdyksjjg",1),
	khqfsjjg("khqfsjjg",2),
	yjk_sjjg("yjk_sjjg",3),
	zdtlsjjg("zdtlsjjg",4),
	ygycsjjg("ygycsjjg",5),
	qdykpmhz("qdykpmhz",6),
	khqfpmhz("khqfpmhz",7),
	yjk_pmhz("yjk_pmhz",8),
	zdtlpmhz("zdtlpmhz",9),
	ygycpmhz("ygycpmhz",10),
	qdykqtcd("qdykqtcd",11),
	khqfqtcd("khqfqtcd",12),
	yjk_qtcd("yjk_qtcd",13),
	zdtlqtcd("zdtlqtcd",14),
	ygycqtcd("ygycqtcd",15),
	qdykzgwz("qdykzgwz",16),
	khqfzgwz("khqfzgwz",17),
	yjk_zgwz("yjk_zgwz",18),
	zdtlzgwz("zdtlzgwz",19),
	ygyczgwz("ygyczgwz",20),
	qdykwjxz("qdykwjxz",21),
	khqfwjxz("khqfwjxz",22),
	yjk_wjxz("yjk_wjxz",23),
	zdtlwjxz("zdtlwjxz",24),
	ygycwjxz("ygycwjxz",25),
	cxsjpmhz("cxsjpmhz",26),
	cxsjsjbg("cxsjsjbg",27),
	cxsjsjzl("cxsjsjzl",28),
	cxsjywjk("cxsjywjk",29),
	qdyksjzl("qdyksjzl",30),
	khqfsjzl("khqfsjzl",31),
	yjk_sjzl("yjk_sjzl",32),
	zdtlsjzl("zdtlsjzl",33),
	ygycsjzl("ygycsjzl",34),
	cxsjsjkg("cxsjsjkg",35),
	cxsjcsgl("cxsjcsgl",36),
	cxsjmxjk("cxsjmxjk",37),
	cxsjrzcx("cxsjrzcx",38),
	cxsjdxgj("cxsjdxgj",39),
	cxsjsjjh("cxsjsjjh",40),
	cxsjsjtb("cxsjsjtb",41),
	cxsjzgwz("cxsjzgwz",42),
	cxsjcxjf("cxsjcxjf",43),
	cxsjxqgl("cxsjxqgl",44),
	cxsjsjts("cxsjsjts",45),
	llglsjjg("llglsjjg",46),
	llglpmhz("llglpmhz",47),
	llglqtcd("llglqtcd",48),
	llglzgwz("llglzgwz",49),
	llglsjbg("llglsjbg",50),
	dzqsjjg("dzqsjjg",51),
	dzqpmhz("dzqpmhz",52),
	dzqqtcd("dzqqtcd",53),
	dzqzgwz("dzqzgwz",54),
	dzqsjbg("dzqsjbg",55),
	cxsjsjgj("cxsjsjgj",56),
	cxsjqxcx("cxsjqxcx",57),
	cxsjsjrw("cxsjsjrw",58),
	cxsjwtfk("cxsjwtfk",59),


	sjzt("sjzt",1),
	sjgl("sjgl",2),
    qdyk("qdyk",3),
    yjk ("yjk",4),
    zdtl("zdtl",5),
    khqf("khqf",6),
    yccz("yccz",7),
    sjbg("sjbg",8),
    sjts("sjts",9),
    pmhz("pmhz",10),
    sjzl("sjzl",11),
    csgl("csgl",12),
    mxjk("mxjk",13),
    sjkg("sjkg",14),
    rzcx("rzcx",15);




	private String name;
	private int index;

	private AuthorityType(String name, int index) {
	    this.name = name;
	    this.index = index;
	}
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}
	public int getIndex() {
	    return index;
	}
	public void setIndex(int index) {
	    this.index = index;
	}

}
