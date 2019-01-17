/**
 * com.hpe.cmca.pojo.ControlInfoData.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.pojo;



public class WzxxData {
	private Integer     ID        ;//    ID
	private Integer     ORDER_NUM ;//    序号
	private String  A         ;//    被问责公司
	private String  B         ;//    问责事项
	private String  C         ;//    问责原因  1000字
	private String  D         ;//    问责要求  1000字
	private String  E         ;//    发文号
	private String  F         ;//    发文日期
	private String  G         ;//    要求整改反馈时限
	private Integer  H         ;//    是否已反馈
	private String  I         ;//    收文号 40字
	private String  J         ;//    收文日期
	private Integer  K         ;//    已做出问责决定
	private Integer  L         ;//    尚未做出问责决定 
	private Integer     M         ;//    总计
	private Integer     N         ;//    总部二级经理
	private Integer     O         ;//    总部三级经理
	private Integer     P         ;//    总部员工
	private Integer     Q         ;//    省公司领导
	private Integer     R         ;//    省公司二级经理级
	private Integer     S         ;//    省公司三级经理级
	private Integer     T         ;//    省公司员工
	private Integer     U         ;//    免职或开除                  
	private Integer     V         ;//    责令辞职              
	private Integer     W         ;//    引咎辞职              
	private Integer     X         ;//    停职检查或留用察看    
	private Integer     Y         ;//    降职（降级）          
	private Integer     Z         ;//    责令公开道歉         
	private Integer     AA        ;//    记过                 
	private Integer     AB        ;//    警告                 
	private Integer     AC        ;//    通报批评             
	private Integer     AD        ;//    扣减工资或奖金       
	private Integer     AE        ;//    诫勉谈话             
	private String  AF        ;//    备注 100-500字   

	private Integer IS_EFFEC; //是否有效
	private String CREATE_TIME_; // 创建时间
	private String CREATE_PERSON; // 创建人
	private String LAST_UPDATE_TIME_; // 最后更改时间
	private String LAST_UPDATE_PERSON; // 最后修改人
	private String CREATE_TIME_BEG ; // 创建开始时间
	private String CREATE_TIME_END ; // 创建开始时间
	
	public String getCREATE_PERSON() {
		return CREATE_PERSON;
	}

	public void setCREATE_PERSON(String cREATE_PERSON) {
		CREATE_PERSON = cREATE_PERSON;
	}

	public String getLAST_UPDATE_TIME_() {
		return LAST_UPDATE_TIME_;
	}

	public void setLAST_UPDATE_TIME_(String lAST_UPDATE_TIME_) {
		LAST_UPDATE_TIME_ = lAST_UPDATE_TIME_;
	}

	public String getLAST_UPDATE_PERSON() {
		return LAST_UPDATE_PERSON;
	}

	public void setLAST_UPDATE_PERSON(String lAST_UPDATE_PERSON) {
		LAST_UPDATE_PERSON = lAST_UPDATE_PERSON;
	}

	
	
	public Integer getID() {
		return this.ID;
	}
	
	public void setID(Integer iD) {
		ID = iD;
	}
	
	public Integer getORDER_NUM() {
		return this.ORDER_NUM;
	}
	
	public void setORDER_NUM(Integer oRDER_NUM) {
		ORDER_NUM = oRDER_NUM;
	}
	
	public String getA() {
		return this.A;
	}
	
	public void setA(String a) {
		A = a;
	}
	
	public String getB() {
		return this.B;
	}
	
	public void setB(String b) {
		B = b;
	}
	
	public String getC() {
		return this.C;
	}
	
	public void setC(String c) {
		C = c;
	}
	
	public String getD() {
		return this.D;
	}
	
	public void setD(String d) {
		D = d;
	}
	
	public String getE() {
		return this.E;
	}
	
	public void setE(String e) {
		E = e;
	}
	
	public String getF() {
		return this.F;
	}
	
	public void setF(String f) {
		F = f;
	}
	
	public String getG() {
		return this.G;
	}
	
	public void setG(String g) {
		G = g;
	}
	
	
	
	
	public Integer getH() {
		return this.H;
	}

	
	public void setH(Integer h) {
		H = h;
	}

	public String getI() {
		return this.I;
	}
	
	public void setI(String i) {
		I = i;
	}
	
	public String getJ() {
		return this.J;
	}
	
	public void setJ(String j) {
		J = j;
	}
	
	
	
	
	public Integer getK() {
		return this.K;
	}

	
	public void setK(Integer k) {
		K = k;
	}

	
	public Integer getL() {
		return this.L;
	}

	
	public void setL(Integer l) {
		L = l;
	}

	public Integer getM() {
		return this.M;
	}
	
	public void setM(Integer m) {
		M = m;
	}
	
	public Integer getN() {
		return this.N;
	}
	
	public void setN(Integer n) {
		N = n;
	}
	
	public Integer getO() {
		return this.O;
	}
	
	public void setO(Integer o) {
		O = o;
	}
	
	public Integer getP() {
		return this.P;
	}
	
	public void setP(Integer p) {
		P = p;
	}
	
	public Integer getQ() {
		return this.Q;
	}
	
	public void setQ(Integer q) {
		Q = q;
	}
	
	public Integer getR() {
		return this.R;
	}
	
	public void setR(Integer r) {
		R = r;
	}
	
	public Integer getS() {
		return this.S;
	}
	
	public void setS(Integer s) {
		S = s;
	}
	
	public Integer getT() {
		return this.T;
	}
	
	public void setT(Integer t) {
		T = t;
	}
	
	public Integer getU() {
		return this.U;
	}
	
	public void setU(Integer u) {
		U = u;
	}
	
	public Integer getV() {
		return this.V;
	}
	
	public void setV(Integer v) {
		V = v;
	}
	
	public Integer getW() {
		return this.W;
	}
	
	public void setW(Integer w) {
		W = w;
	}
	
	public Integer getX() {
		return this.X;
	}
	
	public void setX(Integer x) {
		X = x;
	}
	
	public Integer getY() {
		return this.Y;
	}
	
	public void setY(Integer y) {
		Y = y;
	}
	
	public Integer getZ() {
		return this.Z;
	}
	
	public void setZ(Integer z) {
		Z = z;
	}
	
	public Integer getAA() {
		return this.AA;
	}
	
	public void setAA(Integer aA) {
		AA = aA;
	}
	
	public Integer getAB() {
		return this.AB;
	}
	
	public void setAB(Integer aB) {
		AB = aB;
	}
	
	public Integer getAC() {
		return this.AC;
	}
	
	public void setAC(Integer aC) {
		AC = aC;
	}
	
	public Integer getAD() {
		return this.AD;
	}
	
	public void setAD(Integer aD) {
		AD = aD;
	}
	
	public Integer getAE() {
		return this.AE;
	}
	
	public void setAE(Integer aE) {
		AE = aE;
	}
	
	public String getAF() {
		return this.AF;
	}
	
	public void setAF(String aF) {
		AF = aF;
	}
	
	public Integer getIS_EFFEC() {
		return this.IS_EFFEC;
	}
	
	public void setIS_EFFEC(Integer iS_EFFEC) {
		IS_EFFEC = iS_EFFEC;
	}
	
	
	public String getCREATE_TIME_() {
		return this.CREATE_TIME_;
	}

	
	public void setCREATE_TIME_(String cREATE_TIME_) {
		CREATE_TIME_ = cREATE_TIME_;
	}

	public String getCREATE_TIME_BEG() {
		return this.CREATE_TIME_BEG;
	}
	
	public void setCREATE_TIME_BEG(String cREATE_TIME_BEG) {
		CREATE_TIME_BEG = cREATE_TIME_BEG;
	}
	
	public String getCREATE_TIME_END() {
		return this.CREATE_TIME_END;
	}
	
	public void setCREATE_TIME_END(String cREATE_TIME_END) {
		CREATE_TIME_END = cREATE_TIME_END;
	}
	 
	
}
