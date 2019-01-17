package com.hpe.cmca.entity;

import java.util.List;
import java.util.Map;


public class WordConfigEntity {

	public	String 	blockCode;//段落编码
	public	Integer subjectId;//所属专题编码
	public	Integer prvdId;//省份编码
	public	String  wordCode;//文档编码
	public	String  wordName;//文档名称
	public	Integer textOrTable;//文本还是 表格
	public	String  blockContent;//段落内容
	public	String  querySql;//查询SQL
	public	String[]  perIndex;//占比的占位符 逗号分隔
	public  Integer showByFlag;//是否按指标值展示
	public  String  showFlag;//指标
	public  Integer orderType;//段落前序号类型0无 1-（三 ）2-三、3-(3) 4-3、
	public  String[]  orderRange;//序号排序范围
	//public	String  orderNumSql;//查询序号数量sql  展示出来的段落数量 字段名为num_order
	//public	Integer numOrder;//展示出来的段落数量
	public	String  fontFamily;//字体
	public	Integer fontSize;//字号
	public	String  color;//颜色
	public	Integer bold;//1-加粗0-不加粗
	public	Integer italic;//1-倾斜0-不倾斜
	public	Integer strike;//1-设置删除线0-不设置删除线
	public	Integer underline;//1-设置下划线0-不设置下划线
	public	Integer textPosition;//行高一般填20
	public	Integer alignment;//对齐方式0-两边对齐1-左对齐2-右对齐
	public	Integer indentationFirstLine;//首行缩进一般填600
	public	Integer spacingBefore;//段落前的间距
	public	Integer pageBreak;//1换页0-不换页
	public  Integer tbWidth ;//表格宽度 文本不填
	public  Integer cellWidth;//单元格宽度 文本不填
	public  Integer tbHeight;//表格行高 文本不填
	public  String  cellColor;//单元格填充色
	public  Integer tbRows;//表格列数
	public  Integer tbCols;//表格行数

	public  Integer mergeLast;//是否与上一段落合并0否1是
	public  Integer loopText;//是否循环多行数据填充文本0否1是

	public  Integer externalSql;// 是否使用外置sql 即sql的执行结果仍是sql 0否1是

	public	Integer status;//是否已生成--0完成配置等待生成-1生成完毕-2生成异常

	public	List<String> tbHead ;//表格表头
	public	List<Map<String,Object>> tbDataMap ;//表格数据列表

	public	String[] mergeCell ;//合并单元格

	public  Integer additionalShow;//是否额外控制是否显示 0否 1是
	public	String  additionalShowSql;//是否额外控制是否显示的sql 字段名为show_flag

	public String getBlockCode() {
		return this.blockCode;
	}

	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}

	public Integer getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getPrvdId() {
		return this.prvdId;
	}

	public void setPrvdId(Integer prvdId) {
		this.prvdId = prvdId;
	}

	public String getWordCode() {
		return this.wordCode;
	}

	public void setWordCode(String wordCode) {
		this.wordCode = wordCode;
	}

	public String getWordName() {
		return this.wordName;
	}

	public void setWordName(String wordName) {
		this.wordName = wordName;
	}

	public String getBlockContent() {
		return this.blockContent;
	}

	public void setBlockContent(String blockContent) {
		this.blockContent = blockContent;
	}

	public String getQuerySql() {
		return this.querySql;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	public String getFontFamily() {
		return this.fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public Integer getFontSize() {
		return this.fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getBold() {
		return this.bold;
	}

	public void setBold(Integer bold) {
		this.bold = bold;
	}

	public Integer getItalic() {
		return this.italic;
	}

	public void setItalic(Integer italic) {
		this.italic = italic;
	}

	public Integer getStrike() {
		return this.strike;
	}

	public void setStrike(Integer strike) {
		this.strike = strike;
	}

	public Integer getUnderline() {
		return this.underline;
	}

	public void setUnderline(Integer underline) {
		this.underline = underline;
	}

	public Integer getTextPosition() {
		return this.textPosition;
	}

	public void setTextPosition(Integer textPosition) {
		this.textPosition = textPosition;
	}

	public Integer getAlignment() {
		return this.alignment;
	}

	public void setAlignment(Integer alignment) {
		this.alignment = alignment;
	}

	public Integer getIndentationFirstLine() {
		return this.indentationFirstLine;
	}

	public void setIndentationFirstLine(Integer indentationFirstLine) {
		this.indentationFirstLine = indentationFirstLine;
	}

	public Integer getSpacingBefore() {
		return this.spacingBefore;
	}

	public void setSpacingBefore(Integer spacingBefore) {
		this.spacingBefore = spacingBefore;
	}

	public Integer getPageBreak() {
		return this.pageBreak;
	}

	public void setPageBreak(Integer pageBreak) {
		this.pageBreak = pageBreak;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	public Integer getTextOrTable() {
		return this.textOrTable;
	}


	public void setTextOrTable(Integer textOrTable) {
		this.textOrTable = textOrTable;
	}


	public Integer getTbWidth() {
		return this.tbWidth;
	}


	public void setTbWidth(Integer tbWidth) {
		this.tbWidth = tbWidth;
	}


	public Integer getCellWidth() {
		return this.cellWidth;
	}


	public void setCellWidth(Integer cellWidth) {
		this.cellWidth = cellWidth;
	}


	public Integer getTbHeight() {
		return this.tbHeight;
	}


	public void setTbHeight(Integer tbHeight) {
		this.tbHeight = tbHeight;
	}


	public String getCellColor() {
		return this.cellColor;
	}


	public void setCellColor(String cellColor) {
		this.cellColor = cellColor;
	}


	public Integer getTbRows() {
		return this.tbRows;
	}


	public void setTbRows(Integer tbRows) {
		this.tbRows = tbRows;
	}


	public Integer getTbCols() {
		return this.tbCols;
	}


	public void setTbCols(Integer tbCols) {
		this.tbCols = tbCols;
	}


	public List<String> getTbHead() {
		return this.tbHead;
	}


	public void setTbHead(List<String> tbHead) {
		this.tbHead = tbHead;
	}


	public List<Map<String, Object>> getTbDataMap() {
		return this.tbDataMap;
	}


	public void setTbDataMap(List<Map<String, Object>> tbDataMap) {
		this.tbDataMap = tbDataMap;
	}


	public Integer getShowByFlag() {
		return this.showByFlag;
	}


	public void setShowByFlag(Integer showByFlag) {
		this.showByFlag = showByFlag;
	}


	public String getShowFlag() {
		return this.showFlag;
	}


	public void setShowFlag(String showFlag) {
		this.showFlag = showFlag;
	}


	public Integer getOrderType() {
		return this.orderType;
	}


	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}


	public String[] getOrderRange() {
		return this.orderRange;
	}


	public void setOrderRange(String[] orderRange) {
		this.orderRange = orderRange;
	}


	public String[] getPerIndex() {
		return this.perIndex;
	}


	public void setPerIndex(String[] perIndex) {
		this.perIndex = perIndex;
	}

	public String[] getMergeCell() {
		return this.mergeCell;
	}


	public void setMergeCell(String[] mergeCell) {
		this.mergeCell = mergeCell;
	}

	public Integer getAdditionalShow() {
		return this.additionalShow;
	}


	public void setAdditionalShow(Integer additionalShow) {
		this.additionalShow = additionalShow;
	}


	public String getAdditionalShowSql() {
		return this.additionalShowSql;
	}


	public void setAdditionalShowSql(String additionalShowSql) {
		this.additionalShowSql = additionalShowSql;
	}


	public Integer getMergeLast() {
		return this.mergeLast;
	}


	public void setMergeLast(Integer mergeLast) {
		this.mergeLast = mergeLast;
	}


	public Integer getLoopText() {
		return this.loopText;
	}


	public void setLoopText(Integer loopText) {
		this.loopText = loopText;
	}

	public Integer getExternalSql() {
		return externalSql;
	}

	public void setExternalSql(Integer externalSql) {
		this.externalSql = externalSql;
	}
}
