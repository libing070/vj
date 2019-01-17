package com.hpe.cmca.pojo;


public class ExcelModelConfig{
	private String excelCode;
	private String excelName;
	private Integer sheetOrder;
	private Integer versionCode;
	private Integer blockOrder;
	private String querySql;
	private Integer vH;//横线或纵向0横向 1纵向
	private Integer isWideTb;//是否是宽表0否 1是
	private String wideTbMonth;//宽表起始月
	private Integer wideTbCol;//宽表起始列
    private Integer audTrmOption;//加减月份
	private Integer isFullVersion;
	private String reviser;//修订人
	private String effectiveTime;//生效时间

	public String getExcelCode() {
		return excelCode;
	}

	public void setExcelCode(String excelCode) {
		this.excelCode = excelCode;
	}

	public String getExcelName() {
		return excelName;
	}

	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}

	public Integer getSheetOrder() {
		return sheetOrder;
	}

	public void setSheetOrder(Integer sheetOrder) {
		this.sheetOrder = sheetOrder;
	}


	public Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}

	public Integer getBlockOrder() {
		return blockOrder;
	}

	public void setBlockOrder(Integer blockOrder) {
		this.blockOrder = blockOrder;
	}

	public String getQuerySql() {
		return querySql;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	public Integer getvH() {
		return vH;
	}

	public void setvH(Integer vH) {
		this.vH = vH;
	}

	public Integer getIsWideTb() {
		return isWideTb;
	}

	public void setIsWideTb(Integer isWideTb) {
		this.isWideTb = isWideTb;
	}

	public String getWideTbMonth() {
		return wideTbMonth;
	}

	public void setWideTbMonth(String wideTbMonth) {
		this.wideTbMonth = wideTbMonth;
	}

	public Integer getWideTbCol() {
		return wideTbCol;
	}

	public void setWideTbCol(Integer wideTbCol) {
		this.wideTbCol = wideTbCol;
	}

    public Integer getAudTrmOption() {
        return audTrmOption;
    }

    public void setAudTrmOption(Integer audTrmOption) {
        this.audTrmOption = audTrmOption;
    }

    public Integer getIsFullVersion() {
		return isFullVersion;
	}

	public void setIsFullVersion(Integer isFullVersion) {
		this.isFullVersion = isFullVersion;
	}

	public String getReviser() {
		return reviser;
	}

	public void setReviser(String reviser) {
		this.reviser = reviser;
	}

	public String getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
}
