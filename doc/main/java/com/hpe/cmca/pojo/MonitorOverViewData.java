package com.hpe.cmca.pojo;


public class MonitorOverViewData {


  public String pointCode; //监控点编码 ,
  public String pointName; //监控点名称,
  public String systemCode; //机制编码,
  public String systemName; //机制名称,
  public String level1ProcessCode; //1级流程编码,
  public String level1ProcessName; //1级流程名称,
  public String level2ProcessCode; //2级流程编码,
  public String level2ProcessName; //2级流程名称,
  public String oriInterfTname; //源接口表名称,
  public String oriInterfBusiname; //源接口表业务名称,
  public String resultTname; //结果表名称,
  public String resultBusiname; //表业务名称,
  public String authorityAttr; //权限属性,
  public String pointWeight; //权重,
  public String prvdNm;//省份名
  public String prvdId;//省份id
  /*
  public String riskNum1; //第1月风险值,
  public String riskNum2; //第2月风险值,
  public String riskNum3; //第3月风险值,
  public String riskNum4; //第4月风险值,
  public String riskNum5; //第5月风险值,
  public String riskNum6; //第6月风险值,
  public String riskNum7; //第7风险值,
  public String riskNum8; //第8月风险值,
  public String riskNum9; //第9月风险值,
  public String riskNum10; //第10月风险值,
  public String riskNum11; //第11月风险值,
  public String riskNum12; //第12月风险值,
  */
  public Double riskScore1; //第1月评分值,       
  public Double riskScore2; //第2月评分值,       
  public Double riskScore3; //第3月评分值,       
  public Double riskScore4; //第4月评分值,       
  public Double riskScore5; //第5月评分值,       
  public Double riskScore6; //第6月评分值,       
  public Double riskScore7; //第7月评分值,         
  public Double riskScore8; //第8月评分值,       
  public Double riskScore9; //第9月评分值,       
  public Double riskScore10; //第10月评分值,     
  public Double riskScore11; //第11月评分值,     
  public Double riskScore12; //第12月评分值,     
                                                 
  public String riskTargetData1; //第1月数据值,  
  public String riskTargetData2; //第2月数据值,  
  public String riskTargetData3; //第3月数据值,  
  public String riskTargetData4; //第4月数据值,  
  public String riskTargetData5; //第5月数据值,  
  public String riskTargetData6; //第6月数据值,  
  public String riskTargetData7; //第7月数据值,    
  public String riskTargetData8; //第8月数据值,  
  public String riskTargetData9; //第9月数据值,  
  public String riskTargetData10; //第10月数据值,
  public String riskTargetData11; //第11月数据值,
  public String riskTargetData12; //第12月数据值,


public String getPointCode() {
	return this.pointCode;
}

public void setPointCode(String pointCode) {
	this.pointCode = pointCode;
}

public String getPointName() {
	return this.pointName;
}

public void setPointName(String pointName) {
	this.pointName = pointName;
}

public String getSystemCode() {
	return this.systemCode;
}

public void setSystemCode(String systemCode) {
	this.systemCode = systemCode;
}

public String getSystemName() {
	return this.systemName;
}

public void setSystemName(String systemName) {
	this.systemName = systemName;
}

public String getLevel1ProcessCode() {
	return this.level1ProcessCode;
}

public void setLevel1ProcessCode(String level1ProcessCode) {
	this.level1ProcessCode = level1ProcessCode;
}

public String getLevel1ProcessName() {
	return this.level1ProcessName;
}

public void setLevel1ProcessName(String level1ProcessName) {
	this.level1ProcessName = level1ProcessName;
}

public String getLevel2ProcessCode() {
	return this.level2ProcessCode;
}

public void setLevel2ProcessCode(String level2ProcessCode) {
	this.level2ProcessCode = level2ProcessCode;
}

public String getLevel2ProcessName() {
	return this.level2ProcessName;
}

public void setLevel2ProcessName(String level2ProcessName) {
	this.level2ProcessName = level2ProcessName;
}


public String getPrvdNm() {
	return this.prvdNm;
}


public void setPrvdNm(String prvdNm) {
	this.prvdNm = prvdNm;
}


public String getPrvdId() {
	return this.prvdId;
}


public void setPrvdId(String prvdId) {
	this.prvdId = prvdId;
}

public String getOriInterfTname() {
	return this.oriInterfTname;
}

public void setOriInterfTname(String oriInterfTname) {
	this.oriInterfTname = oriInterfTname;
}

public String getOriInterfBusiname() {
	return this.oriInterfBusiname;
}

public void setOriInterfBusiname(String oriInterfBusiname) {
	this.oriInterfBusiname = oriInterfBusiname;
}

public String getResultTname() {
	return this.resultTname;
}

public void setResultTname(String resultTname) {
	this.resultTname = resultTname;
}

public String getResultBusiname() {
	return this.resultBusiname;
}

public void setResultBusiname(String resultBusiname) {
	this.resultBusiname = resultBusiname;
}


public String getAuthorityAttr() {
	return this.authorityAttr;
}


public void setAuthorityAttr(String authorityAttr) {
	this.authorityAttr = authorityAttr;
}



public String getPointWeight() {
	return this.pointWeight;
}


public void setPointWeight(String pointWeight) {
	this.pointWeight = pointWeight;
}
/*
public String getRiskNum1() {
	return this.riskNum1;
}


public void setRiskNum1(String riskNum1) {
	this.riskNum1 = riskNum1;
}


public String getRiskNum2() {
	return this.riskNum2;
}


public void setRiskNum2(String riskNum2) {
	this.riskNum2 = riskNum2;
}


public String getRiskNum3() {
	return this.riskNum3;
}


public void setRiskNum3(String riskNum3) {
	this.riskNum3 = riskNum3;
}


public String getRiskNum4() {
	return this.riskNum4;
}


public void setRiskNum4(String riskNum4) {
	this.riskNum4 = riskNum4;
}


public String getRiskNum5() {
	return this.riskNum5;
}


public void setRiskNum5(String riskNum5) {
	this.riskNum5 = riskNum5;
}


public String getRiskNum6() {
	return this.riskNum6;
}


public void setRiskNum6(String riskNum6) {
	this.riskNum6 = riskNum6;
}


public String getRiskNum7() {
	return this.riskNum7;
}


public void setRiskNum7(String riskNum7) {
	this.riskNum7 = riskNum7;
}


public String getRiskNum8() {
	return this.riskNum8;
}


public void setRiskNum8(String riskNum8) {
	this.riskNum8 = riskNum8;
}


public String getRiskNum9() {
	return this.riskNum9;
}


public void setRiskNum9(String riskNum9) {
	this.riskNum9 = riskNum9;
}


public String getRiskNum10() {
	return this.riskNum10;
}


public void setRiskNum10(String riskNum10) {
	this.riskNum10 = riskNum10;
}


public String getRiskNum11() {
	return this.riskNum11;
}


public void setRiskNum11(String riskNum11) {
	this.riskNum11 = riskNum11;
}


public String getRiskNum12() {
	return this.riskNum12;
}


public void setRiskNum12(String riskNum12) {
	this.riskNum12 = riskNum12;
}
*/

public Double getRiskScore1() {
	return this.riskScore1;
}


public void setRiskScore1(Double riskScore1) {
	this.riskScore1 = riskScore1;
}


public Double getRiskScore2() {
	return this.riskScore2;
}


public void setRiskScore2(Double riskScore2) {
	this.riskScore2 = riskScore2;
}


public Double getRiskScore3() {
	return this.riskScore3;
}


public void setRiskScore3(Double riskScore3) {
	this.riskScore3 = riskScore3;
}


public Double getRiskScore4() {
	return this.riskScore4;
}


public void setRiskScore4(Double riskScore4) {
	this.riskScore4 = riskScore4;
}


public Double getRiskScore5() {
	return this.riskScore5;
}


public void setRiskScore5(Double riskScore5) {
	this.riskScore5 = riskScore5;
}


public Double getRiskScore6() {
	return this.riskScore6;
}


public void setRiskScore6(Double riskScore6) {
	this.riskScore6 = riskScore6;
}


public Double getRiskScore7() {
	return this.riskScore7;
}


public void setRiskScore7(Double riskScore7) {
	this.riskScore7 = riskScore7;
}


public Double getRiskScore8() {
	return this.riskScore8;
}


public void setRiskScore8(Double riskScore8) {
	this.riskScore8 = riskScore8;
}


public Double getRiskScore9() {
	return this.riskScore9;
}


public void setRiskScore9(Double riskScore9) {
	this.riskScore9 = riskScore9;
}


public Double getRiskScore10() {
	return this.riskScore10;
}


public void setRiskScore10(Double riskScore10) {
	this.riskScore10 = riskScore10;
}


public Double getRiskScore11() {
	return this.riskScore11;
}


public void setRiskScore11(Double riskScore11) {
	this.riskScore11 = riskScore11;
}


public Double getRiskScore12() {
	return this.riskScore12;
}


public void setRiskScore12(Double riskScore12) {
	this.riskScore12 = riskScore12;
}


public String getRiskTargetData1() {
	return this.riskTargetData1;
}


public void setRiskTargetData1(String riskTargetData1) {
	this.riskTargetData1 = riskTargetData1;
}


public String getRiskTargetData2() {
	return this.riskTargetData2;
}


public void setRiskTargetData2(String riskTargetData2) {
	this.riskTargetData2 = riskTargetData2;
}


public String getRiskTargetData3() {
	return this.riskTargetData3;
}


public void setRiskTargetData3(String riskTargetData3) {
	this.riskTargetData3 = riskTargetData3;
}


public String getRiskTargetData4() {
	return this.riskTargetData4;
}


public void setRiskTargetData4(String riskTargetData4) {
	this.riskTargetData4 = riskTargetData4;
}


public String getRiskTargetData5() {
	return this.riskTargetData5;
}


public void setRiskTargetData5(String riskTargetData5) {
	this.riskTargetData5 = riskTargetData5;
}


public String getRiskTargetData6() {
	return this.riskTargetData6;
}


public void setRiskTargetData6(String riskTargetData6) {
	this.riskTargetData6 = riskTargetData6;
}


public String getRiskTargetData7() {
	return this.riskTargetData7;
}


public void setRiskTargetData7(String riskTargetData7) {
	this.riskTargetData7 = riskTargetData7;
}


public String getRiskTargetData8() {
	return this.riskTargetData8;
}


public void setRiskTargetData8(String riskTargetData8) {
	this.riskTargetData8 = riskTargetData8;
}


public String getRiskTargetData9() {
	return this.riskTargetData9;
}


public void setRiskTargetData9(String riskTargetData9) {
	this.riskTargetData9 = riskTargetData9;
}


public String getRiskTargetData10() {
	return this.riskTargetData10;
}


public void setRiskTargetData10(String riskTargetData10) {
	this.riskTargetData10 = riskTargetData10;
}


public String getRiskTargetData11() {
	return this.riskTargetData11;
}


public void setRiskTargetData11(String riskTargetData11) {
	this.riskTargetData11 = riskTargetData11;
}


public String getRiskTargetData12() {
	return this.riskTargetData12;
}


public void setRiskTargetData12(String riskTargetData12) {
	this.riskTargetData12 = riskTargetData12;
}



}
