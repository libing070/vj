package com.hpe.cmca.pojo;

import java.util.List;
import java.util.Map;


public class confInfo {
	
	public Integer id = null;           // '数据块编号'
	//public Integer  isFirst = null;     // '是否是第一个数据块1是0不是'
	public Integer  direction = null;   //'相对上一数据块的方向1纵向0横向',
	public String  blockCode = null;	// '数据块编码'
	public String  excelName = null;   // '表格名称'
	public String  excelCode = null;   // '表格编码'
	public String  sheetName = null;   // 'sheet名称'
	public String  sheetCode = null;   // 'sheet编码'
	public String  focusCd = null;   // '专题编码'
	//public Integer  sheetId = null;     // 'sheet编号'
	public Integer  vPointRelate = null;      // '纵坐标'若相对上一数据块是纵向，上一数据块的左下纵坐标加上该值即为真实纵坐标，横坐标为真实坐标
	public Integer  hPointRelate = null;      // '横坐标'若相对上一数据块是横向，上一数据块的右上横坐标加上该值即为真实横坐标，纵坐标为真实坐标
	public Integer  vH = null;          // '1横行OR 0纵行'
	public String  blockTitle = null;    // '数据块标题'
	public Integer isWidthTb = null;//是否是宽表1是0否
	public String  audTrmStart = null;    // '开始月份'
	public Integer hasAudTrm = null;//是否有列上方的审计月标题1有0无
	public Integer  colLocation = null;//宽表公共列开始位置
	public String[]  colType = null; // '需要百分比展示的列逗号分隔'
	public String[]  cellContent = null; // '单元格内容'
	public String  querySql = null;    // '查询SQL'
	public Integer isNullSet = null;    // '是否将空值设置为默认值1是0否'
	public String  defaultVal = null;    // '空值时的默认值'
	public String[]  speNullCol = null; // '特殊处理的空值列逗号分隔'
	public String  speDefaultVal = null;    // '空值时的特殊处理默认值'
	public String  wordFont = null;    // '字体'
	public String  wordSize = null;    // '字号'
	public String  wordColor = null;   // '颜色'
	
	//String  width = null;        // '上一数据块的宽度'
	//String  height = null;       // '上一数据块的高度'
	public Integer vPointLD = null; //上一数据块的左下角纵坐标
	public Integer hPointLD = null; //上一数据块的左下角横坐标
	public Integer vPointRU = null; //上一数据块的右上角纵坐标
	public Integer hPointRU = null; //上一数据块的右上角横坐标
	
	public Integer vPointReal = null; //真实左上角纵坐标
	public Integer hPointReal = null; //真实左上角横坐标
	
	public List<Map<String,Object>> dataList = null; //数据块的数据
	
	public Integer audTrmOp = null;//在审计月基础上加上指定月份 1加1月 -1减1月 不对sql起作用
	
	public Integer complexPattern = null;//是否启用复杂模式
	public String[]  complexTitle = null;//复杂标题
	public Integer textType = null;//文字类型1-普通文本 2-列标题 3-大标题
	public String[] colWidth = null;//列宽
	public String[] mergeCell = null;//合并单元格
	
	public Integer getId() {
		return this.id;
	}

	
	public void setId(Integer id) {
		this.id = id;
	}

	
//	public Integer getIsFirst() {
//		return this.isFirst;
//	}
//
//	
//	public void setIsFirst(Integer isFirst) {
//		this.isFirst = isFirst;
//	}

	
	public Integer getDirection() {
		return this.direction;
	}

	
	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	
	public String getExcelName() {
		return this.excelName;
	}

	
	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}

	
	public String getSheetName() {
		return this.sheetName;
	}

	
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	
//	public Integer getSheetId() {
//		return this.sheetId;
//	}
//
//	
//	public void setSheetId(Integer sheetId) {
//		this.sheetId = sheetId;
//	}

	
	public Integer getvPointRelate() {
		return this.vPointRelate;
	}

	
	public void setvPointRelate(Integer vPointRelate) {
		this.vPointRelate = vPointRelate;
	}

	
	public Integer gethPointRelate() {
		return this.hPointRelate;
	}

	
	public void sethPointRelate(Integer hPointRelate) {
		this.hPointRelate = hPointRelate;
	}

	
	public Integer getvH() {
		return this.vH;
	}

	
	public void setvH(Integer vH) {
		this.vH = vH;
	}

	
	public String[] getCellContent() {
		return this.cellContent;
	}

	
	public void setCellContent(String[] cellContent) {
		this.cellContent = cellContent;
	}

	
	public String getQuerySql() {
		return this.querySql;
	}

	
	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	
	public String getWordFont() {
		return this.wordFont;
	}

	
	public void setWordFont(String wordFont) {
		this.wordFont = wordFont;
	}

	
	public String getWordSize() {
		return this.wordSize;
	}

	
	public void setWordSize(String wordSize) {
		this.wordSize = wordSize;
	}

	
	public String getWordColor() {
		return this.wordColor;
	}

	
	public void setWordColor(String wordColor) {
		this.wordColor = wordColor;
	}

	
	public Integer getvPointLD() {
		return this.vPointLD;
	}

	
	public void setvPointLD(Integer vPointLD) {
		this.vPointLD = vPointLD;
	}

	
	public Integer gethPointLD() {
		return this.hPointLD;
	}

	
	public void sethPointLD(Integer hPointLD) {
		this.hPointLD = hPointLD;
	}

	
	public Integer getvPointRU() {
		return this.vPointRU;
	}

	
	public void setvPointRU(Integer vPointRU) {
		this.vPointRU = vPointRU;
	}

	
	public Integer gethPointRU() {
		return this.hPointRU;
	}

	
	public void sethPointRU(Integer hPointRU) {
		this.hPointRU = hPointRU;
	}

	
	public Integer getvPointReal() {
		return this.vPointReal;
	}

	
	public void setvPointReal(Integer vPointReal) {
		this.vPointReal = vPointReal;
	}

	
	public Integer gethPointReal() {
		return this.hPointReal;
	}

	
	public void sethPointReal(Integer hPointReal) {
		this.hPointReal = hPointReal;
	}


	
	public List<Map<String, Object>> getDataList() {
		return this.dataList;
	}


	
	public void setDataList(List<Map<String, Object>> dataList) {
		this.dataList = dataList;
	}


	
	public String getBlockCode() {
		return this.blockCode;
	}


	
	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}


	
	public String getExcelCode() {
		return this.excelCode;
	}


	
	public void setExcelCode(String excelCode) {
		this.excelCode = excelCode;
	}


	
	public String getSheetCode() {
		return this.sheetCode;
	}


	
	public void setSheetCode(String sheetCode) {
		this.sheetCode = sheetCode;
	}


	
	public String getBlockTitle() {
		return this.blockTitle;
	}


	
	public void setBlockTitle(String blockTitle) {
		this.blockTitle = blockTitle;
	}


	
	public String[] getColType() {
		return this.colType;
	}


	
	public void setColType(String[] colType) {
		this.colType = colType;
	}


	
	public Integer getHasAudTrm() {
		return this.hasAudTrm;
	}


	
	public void setHasAudTrm(Integer hasAudTrm) {
		this.hasAudTrm = hasAudTrm;
	}


	
	public Integer getColLocation() {
		return this.colLocation;
	}


	
	public void setColLocation(Integer colLocation) {
		this.colLocation = colLocation;
	}


	
	public Integer getIsWidthTb() {
		return this.isWidthTb;
	}


	
	public void setIsWidthTb(Integer isWidthTb) {
		this.isWidthTb = isWidthTb;
	}


	
	public Integer getIsNullSet() {
		return this.isNullSet;
	}


	
	public void setIsNullSet(Integer isNullSet) {
		this.isNullSet = isNullSet;
	}


	
	public String getDefaultVal() {
		return this.defaultVal;
	}


	
	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}


	
	public String[] getSpeNullCol() {
		return this.speNullCol;
	}


	
	public void setSpeNullCol(String[] speNullCol) {
		this.speNullCol = speNullCol;
	}


	
	public String getSpeDefaultVal() {
		return this.speDefaultVal;
	}


	
	public void setSpeDefaultVal(String speDefaultVal) {
		this.speDefaultVal = speDefaultVal;
	}


	
	public String getFocusCd() {
		return this.focusCd;
	}


	
	public void setFocusCd(String focusCd) {
		this.focusCd = focusCd;
	}


	
	public String getAudTrmStart() {
		return this.audTrmStart;
	}


	
	public void setAudTrmStart(String audTrmStart) {
		this.audTrmStart = audTrmStart;
	}


	
	
	public Integer getAudTrmOp() {
		return this.audTrmOp;
	}


	
	public void setAudTrmOp(Integer audTrmOp) {
		this.audTrmOp = audTrmOp;
	}


	public Integer getComplexPattern() {
		return this.complexPattern;
	}


	
	public void setComplexPattern(Integer complexPattern) {
		this.complexPattern = complexPattern;
	}


	
	public String[] getComplexTitle() {
		return this.complexTitle;
	}


	
	public void setComplexTitle(String[] complexTitle) {
		this.complexTitle = complexTitle;
	}
	
	
	public Integer getTextType() {
		return this.textType;
	}


	
	public void setTextType(Integer textType) {
		this.textType = textType;
	}


	public String[] getColWidth() {
		return this.colWidth;
	}


	
	public void setColWidth(String[] colWidth) {
		this.colWidth = colWidth;
	}


	public String[] getMergeCell() {
		return this.mergeCell;
	}


	
	public void setMergeCell(String[] mergeCell) {
		this.mergeCell = mergeCell;
	}

}
