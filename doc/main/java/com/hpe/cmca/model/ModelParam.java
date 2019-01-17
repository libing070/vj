package com.hpe.cmca.model;

public class ModelParam {

	private String	thresholdId;
	private String	thresholdCode			= "";
	private String	thresholdName;
	private String	thresholdOperators		= "";
	private String	thresholdValue;
	private String	thresholdSubjectid;
	private String	thresholdFocusid;

	private String	thresholdSid;
	private String	thresholdFid;

	private String	thresholdEffdate;
	private String	thresholdEnddate;

	private String	old_ThresholdId;
	private String	old_ThresholdCode		= "";
	private String	old_ThresholdName;
	private String	old_ThresholdOperators	= "";
	private String	old_ThresholdValue;
	private String	old_ThresholdEffdate;
	private String	old_ThresholdEnddate;

	private Boolean	isChanged				= false;//本次编辑操作是否对参数记录进行变更 
	private Boolean	isCodeChanged			= false;//本次编辑操作是否对阈值编码字段进行变更 
	private Boolean	isNameChanged			= false;//本次编辑操作是否对阈值名称字段进行变更 
	private Boolean	isValueChanged			= false;//本次编辑操作是否对阈值字段进行变更 
	private Boolean	isOperatorsChanged		= false;//本次编辑操作是否对阈值逻辑进行变更 
	private Boolean	isEffChanged			= false;//本次编辑操作是否对生效时间进行变更 
	private Boolean	isEndChanged			= false;//本次编辑操作是否对失效时间进行变更 

	private String	reason;
	private String	person;

	public void setChangeSta() {

		if (!(old_ThresholdCode.equals(thresholdCode))) {
			isCodeChanged = true;
			isChanged = true;
		}

		if (!(old_ThresholdName.equals(thresholdName))) {
			isNameChanged = true;
			isChanged = true;
		}

		if (!(old_ThresholdOperators.equals(thresholdOperators))) {
			isOperatorsChanged = true;
			isChanged = true;
		}

		if (!(old_ThresholdValue.equals(thresholdValue))) {
			isValueChanged = true;
			isChanged = true;
		}

		if (!(old_ThresholdEffdate.equals(thresholdEffdate))) {

			isEffChanged = true;
			isChanged = true;
		}

		if (!(old_ThresholdEnddate.equals(thresholdEnddate))) {

			isEndChanged = true;
			isChanged = true;
		}
	}

	public String getThresholdId() {
		return thresholdId;
	}

	public void setThresholdId(String thresholdId) {
		this.thresholdId = thresholdId;
	}

	public String getThresholdCode() {
		return thresholdCode;
	}

	public void setThresholdCode(String thresholdCode) {
		this.thresholdCode = thresholdCode;
	}

	public String getThresholdName() {
		return thresholdName;
	}

	public void setThresholdName(String thresholdName) {
		this.thresholdName = thresholdName;
	}

	public String getThresholdOperators() {
		return thresholdOperators;
	}

	public void setThresholdOperators(String thresholdOperators) {
		this.thresholdOperators = thresholdOperators;
	}

	public String getThresholdValue() {
		return thresholdValue;
	}

	public void setThresholdValue(String thresholdValue) {
		this.thresholdValue = thresholdValue;
	}

	public String getThresholdSubjectid() {
		return thresholdSubjectid;
	}

	public void setThresholdSubjectid(String thresholdSubjectid) {
		this.thresholdSubjectid = thresholdSubjectid;
	}

	public String getThresholdFocusid() {
		return thresholdFocusid;
	}

	public void setThresholdFocusid(String thresholdFocusid) {
		this.thresholdFocusid = thresholdFocusid;
	}

	public String getThresholdEffdate() {
		return thresholdEffdate;
	}

	public void setThresholdEffdate(String thresholdEffdate) {
		this.thresholdEffdate = thresholdEffdate;
	}

	public String getThresholdEnddate() {
		return thresholdEnddate;
	}

	public void setThresholdEnddate(String thresholdEnddate) {
		this.thresholdEnddate = thresholdEnddate;
	}

	public Boolean getIsChanged() {
		return isChanged;
	}

	public void setIsChanged(Boolean isChanged) {
		this.isChanged = isChanged;
	}

	public Boolean getIsCodeChanged() {
		return isCodeChanged;
	}

	public void setIsCodeChanged(Boolean isCodeChanged) {
		this.isCodeChanged = isCodeChanged;
	}

	public Boolean getIsNameChanged() {
		return isNameChanged;
	}

	public void setIsNameChanged(Boolean isNameChanged) {
		this.isNameChanged = isNameChanged;
	}

	public Boolean getIsValueChanged() {
		return isValueChanged;
	}

	public void setIsValueChanged(Boolean isValueChanged) {
		this.isValueChanged = isValueChanged;
	}

	public Boolean getIsOperatorsChanged() {
		return isOperatorsChanged;
	}

	public void setIsOperatorsChanged(Boolean isOperatorsChanged) {
		this.isOperatorsChanged = isOperatorsChanged;
	}

	public Boolean getIsEffChanged() {
		return isEffChanged;
	}

	public void setIsEffChanged(Boolean isEffChanged) {
		this.isEffChanged = isEffChanged;
	}

	public Boolean getIsEndChanged() {
		return isEndChanged;
	}

	public void setIsEndChanged(Boolean isEndChanged) {
		this.isEndChanged = isEndChanged;
	}

	public String getOld_ThresholdCode() {
		return old_ThresholdCode;
	}

	public void setOld_ThresholdCode(String old_ThresholdCode) {
		this.old_ThresholdCode = old_ThresholdCode;
	}

	public String getOld_ThresholdName() {
		return old_ThresholdName;
	}

	public void setOld_ThresholdName(String old_ThresholdName) {
		this.old_ThresholdName = old_ThresholdName;
	}

	public String getOld_ThresholdOperators() {
		return old_ThresholdOperators;
	}

	public void setOld_ThresholdOperators(String old_ThresholdOperators) {
		this.old_ThresholdOperators = old_ThresholdOperators;
	}

	public String getOld_ThresholdValue() {
		return old_ThresholdValue;
	}

	public void setOld_ThresholdValue(String old_ThresholdValue) {
		this.old_ThresholdValue = old_ThresholdValue;
	}

	public String getOld_ThresholdEffdate() {
		return old_ThresholdEffdate;
	}

	public void setOld_ThresholdEffdate(String old_ThresholdEffdate) {
		this.old_ThresholdEffdate = old_ThresholdEffdate;
	}

	public String getOld_ThresholdEnddate() {
		return old_ThresholdEnddate;
	}

	public void setOld_ThresholdEnddate(String old_ThresholdEnddate) {
		this.old_ThresholdEnddate = old_ThresholdEnddate;
	}

	public String getThresholdSid() {
		return thresholdSid;
	}

	public void setThresholdSid(String thresholdSid) {
		this.thresholdSid = thresholdSid;
	}

	public String getThresholdFid() {
		return thresholdFid;
	}

	public void setThresholdFid(String thresholdFid) {
		this.thresholdFid = thresholdFid;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getOld_ThresholdId() {
		return old_ThresholdId;
	}

	public void setOld_ThresholdId(String old_ThresholdId) {
		this.old_ThresholdId = old_ThresholdId;
	}

}