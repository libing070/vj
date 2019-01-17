package com.hpe.cmca.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;


@Service("NotiFileYCCZ5000SFXZProcessor")
public class NotiFileYCCZ5000SFXZProcessor extends AbstractNotiFileProcessor {

    private String		   month;

    private String		   prvdId = "10000";

    private ArrayList<String> strs   = new ArrayList<String>();

    // private HashMap<String, String> prvdMap = new HashMap<String, String>();

    public void setMonth(String month) {
	this.month = month;
    }

    public void setPrvdId(String prvdId) {
	this.prvdId = prvdId;
    }

    // 重写构建文件名方法
    protected String buildFileName() {
	fileName = fileName + ".xlsx";
	return fileName;
    }

    public ArrayList<String> getFileNameList() {
	strs.add(fileName);
	return strs;
    }

    @Override
    public boolean generate() throws Exception {

	// 根据省份及月份对文件夹名命名
	if (this.prvdId.equals("10000")) {
	    this.setFileName("员工异常操作排名汇总_省份选择_" + month);
	    // 创建第一个sheet
	    sh = wb.createSheet("省份选择");
	    // getNotiFile5000YCJFZS()根据月份month、省份prvdId、类型Top50获取后台数据。
	    writeFirstPart(notiFileGenService.getNotiFile5000SFXZ(month, "SFXZ"), sh);

	    // 创建第二个sheet
	    sh = wb.createSheet("员工异常操作通报情况");
	    writeSecondPart(notiFileGenService.getNotiFile5000SFXZ(month, "QKTB"), sh, prvdId, month);
	}

	return true;
    }

    /**
     * <pre>
     * Desc  将获取到的数据插入到excel‘员工异常操作异常退费排名汇总’下的sheet1中
     * @param data：获取的数据
     * @param sh：应插入的sheet
     * @param prvdId：省份
     * @param month：月份
     * @author feihu
     * Mar 29, 2017 3:31:13 PM
     * </pre>
     */
    public void writeFirstPart(List<Map<String, Object>> data, Sheet sh) {
	sh.createRow(0);
	sh.getRow(0).createCell(0).setCellValue("省公司");
	sh.getRow(0).createCell(1).setCellValue("全国前50名情况：[用户数,最小排名,最大排名]");
	sh.getRow(0).createCell(5).setCellValue("合计");
	sh.getRow(0).createCell(6).setCellValue("全国前2名情况");
	sh.getRow(0).createCell(10).setCellValue("选择原因");
	sh.createRow(1);

	sh.getRow(1).createCell(1).setCellValue("积分赠送");
	sh.getRow(1).createCell(2).setCellValue("积分转移");
	sh.getRow(1).createCell(3).setCellValue("话费赠送");
	sh.getRow(1).createCell(4).setCellValue("退费");
	sh.getRow(1).createCell(6).setCellValue("积分赠送");
	sh.getRow(1).createCell(7).setCellValue("积分转移");
	sh.getRow(1).createCell(8).setCellValue("话费赠送");
	sh.getRow(1).createCell(9).setCellValue("退费");

	// 设置第一行单元格格式 及 合并单元格 -add by hufei

	// 合并单元格
	sh.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
	sh.addMergedRegion(new CellRangeAddress(0, 0, 1, 4));
	sh.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
	sh.addMergedRegion(new CellRangeAddress(0, 0, 6, 9));
	sh.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));
	sh.getRow(0).getCell(0).setCellStyle(getStyle1());
	sh.getRow(0).getCell(1).setCellStyle(getStyle1());
	sh.getRow(0).getCell(5).setCellStyle(getStyle1());
	sh.getRow(0).getCell(6).setCellStyle(getStyle1());
	sh.getRow(0).getCell(10).setCellStyle(getStyle1());
	for (int i = 1; i <= 9; i++) {
	    if (i != 5) {
		sh.getRow(1).getCell(i).setCellStyle(getStyle1());
	    }
	}
	// for(int j=0;j<=19;j++){
	// if (null == sh.getRow(1).getCell(j)) {
	// sh.getRow(1).createCell(j);
	// }
	// sh.getRow(1).getCell(j).setCellStyle(getStyle1());
	// }

	for (int i = 0; i <= 10; i++)
	    sh.setColumnWidth(i, 256 * 10);
	sh.setColumnWidth(10, 256 * 24);
	// sh.getRow(1).setHeightInPoints(35);

	// 第一个参数表示要冻结的列数，这里只冻结行所以为0；
	// 第二个参数表示要冻结的行数；
	// 第三个参数表示右边区域可见的首列序号，从1开始计算，这里是冻结行，所以为0；
	// 第四个参数表示下边区域可见的首行序号，也是从1开始计算；
	// sh.createFreezePane(14, 2, 0, 0);

	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
	Map<String, Object> row=new HashMap<String,Object>();
	for (int i = 0,dataSize=data.size(); i < dataSize; i++) {
	    row = data.get(i);
	    // 新增数据行
	    sh.createRow(2 + i);
	    // 插入单元格数据
	    if (row.size() == 0) {
		sh.getRow(2 + i).createCell(2).setCellValue("-");
	    } else {
		sh.getRow(2 + i).createCell(0).setCellValue(row.get("prvd_name").toString());
		if ((Integer) row.get("Num_5001") != 0) {
		    sh.getRow(2 + i).createCell(1).setCellValue("[" + row.get("Num_5001").toString() + "," + row.get("Min_5001").toString() + "," + row.get("Max_5001").toString() + "]");
		}
		if ((Integer) row.get("Num_5002") != 0) {
		    sh.getRow(2 + i).createCell(2).setCellValue("[" + row.get("Num_5002").toString() + "," + row.get("Min_5002").toString() + "," + row.get("Max_5002").toString() + "]");
		}
		if ((Integer) row.get("Num_5003") != 0) {
		    sh.getRow(2 + i).createCell(3).setCellValue("[" + row.get("Num_5003").toString() + "," + row.get("Min_5003").toString() + "," + row.get("Max_5003").toString() + "]");
		}
		if ((Integer) row.get("Num_5004") != 0) {
		    sh.getRow(2 + i).createCell(4).setCellValue("[" + row.get("Num_5004").toString() + "," + row.get("Min_5004").toString() + "," + row.get("Max_5004").toString() + "]");
		}
		if ((Integer) row.get("HZ_Num") != 0) {
		    sh.getRow(2 + i).createCell(5).setCellValue(row.get("HZ_Num").toString());
		}
		if ((Integer) row.get("Min_5005") != 0) {
		    if ((Integer) row.get("Max_5005") != 0 && (Integer) row.get("Max_5005") != (Integer) row.get("Min_5005")) {
			sh.getRow(2 + i).createCell(6).setCellValue("第" + row.get("Min_5005").toString() + "名、第" + row.get("Max_5005").toString() + "名");
		    } else {
			sh.getRow(2 + i).createCell(6).setCellValue("第" + row.get("Min_5005").toString() + "名");
		    }
		}
		if ((Integer) row.get("Min_5006") != 0) {
		    if ((Integer) row.get("Max_5006") != 0 && (Integer) row.get("Max_5006") != (Integer) row.get("Min_5006")) {
			sh.getRow(2 + i).createCell(7).setCellValue("第" + row.get("Min_5006").toString() + "名、第" + row.get("Max_5006").toString() + "名");
		    } else {
			sh.getRow(2 + i).createCell(7).setCellValue("第" + row.get("Min_5006").toString() + "名");
		    }
		}
		if ((Integer) row.get("Min_5007") != 0) {
		    if ((Integer) row.get("Max_5007") != 0 && (Integer) row.get("Max_5007") != (Integer) row.get("Min_5007")) {
			sh.getRow(2 + i).createCell(8).setCellValue("第" + row.get("Min_5007").toString() + "名、第" + row.get("Max_5007").toString() + "名");
		    } else {
			sh.getRow(2 + i).createCell(8).setCellValue("第" + row.get("Min_5007").toString() + "名");
		    }
		}
		if ((Integer) row.get("Min_5008") != 0) {
		    if ((Integer) row.get("Max_5008") != 0 && (Integer) row.get("Max_5008") != (Integer) row.get("Min_5008")) {
			sh.getRow(2 + i).createCell(9).setCellValue("第" + row.get("Min_5008").toString() + "名、第" + row.get("Max_5008").toString() + "名");
		    } else {
			sh.getRow(2 + i).createCell(9).setCellValue("第" + row.get("Min_5008").toString() + "名");
		    }
		}
		if (row.get("Sel_Reason") != "0" && !row.get("Sel_Reason").equals("")) {
		    sh.getRow(2 + i).createCell(10).setCellValue(row.get("Sel_Reason").toString());
		}
	    }

	    // 格式设置
	    int m = 0;
	    int n=0;
	    for (int j = 0; j <= 10; j++) {
		if (null == sh.getRow(i + 2).getCell(j)) {
		    sh.getRow(i + 2).createCell(j).setCellValue("");
		    if (j >= 6 && j <= 9) {
			m = m + 1;
		    }
		    n=n+1;
		}
		sh.getRow(i + 2).getCell(j).setCellStyle(getStyle6());
	    }
	    if (!(sh.getRow(i + 2).getCell(10).getStringCellValue().equals(""))) {
		if (m > 3) {
		    for (int j = 0; j <= 10; j++) {
			sh.getRow(i + 2).getCell(j).setCellStyle(getStyle2_yellow());
		    }
		} else {
		    for (int j = 0; j <= 10; j++) {
			sh.getRow(i + 2).getCell(j).setCellStyle(getStyle2_red());
		    }
		}
	    }
	    if(n>9){
		for(int p=1;p<10;p++){
		    sh.getRow(i + 2).getCell(p).setCellValue("-");
		}
	    }

	}

    }

    /**
     * <pre>
     * Desc  将获取到的数据插入到sheet中
     * @param data：获取的数据
     * @param sh：即将插入的sheet
     * @param prvdId：省份
     * @param month：月份
     * @author feihu
     * Mar 29, 2017 3:36:35 PM
     * </pre>
     */
    public void writeSecondPart(List<Map<String, Object>> data, Sheet sh, String prvdId, String month) {
	HashMap<String, String> prvdMap = prvdNameMap();
	sh.createRow(0);
	sh.getRow(0).createCell(0).setCellValue("员工异常操作通报情况");
	sh.createRow(1);
	sh.createRow(2);
	sh.getRow(2).createCell(0).setCellValue("序号");
	// if(prvdId.equals("10000")){
	sh.getRow(2).createCell(1).setCellValue("省份");
	for (int i = 1; i <= 31; i++) {
	    sh.createRow(i + 2);
	    sh.getRow(i + 2).createCell(0).setCellValue(i);
	    // if(prvdId.equals("10000")){
	    sh.getRow(i + 2).createCell(1).setCellValue(prvdMap.get(Integer.toString(i)).toString());
	    sh.getRow(i + 2).getCell(0).setCellStyle(getStyle6());
	    sh.getRow(i + 2).getCell(1).setCellStyle(getStyle6());
	}
	int j = 2;
	HashMap<String, String> mothMap = new HashMap<String, String>();
	for (int i = 0,dataSize=data.size(); i < dataSize; i++) {
	    Map<String, Object> row = data.get(i);

	    String moth = row.get("Aud_trm").toString();
	    if (!mothMap.containsValue(moth)) {
		mothMap.put(Integer.toString(j), moth);
		sh.getRow(1).createCell(j).setCellValue(moth.substring(0, 4) + "年" + moth.substring(4, 6) + "月" + "通报");
		sh.getRow(2).createCell(j).setCellValue("积分赠送");
		sh.getRow(2).createCell(j + 1).setCellValue("积分转移");
		sh.getRow(2).createCell(j + 2).setCellValue("话费赠送");
		sh.getRow(2).createCell(j + 3).setCellValue("退费");
		sh.addMergedRegion(new CellRangeAddress(1, 1, j, j + 3));
		sh.getRow(1).getCell(j).setCellStyle(getStyle1());
		sh.getRow(2).getCell(j).setCellStyle(getStyle1());
		sh.getRow(2).getCell(j + 1).setCellStyle(getStyle1());
		sh.getRow(2).getCell(j + 2).setCellStyle(getStyle1());
		sh.getRow(2).getCell(j + 3).setCellStyle(getStyle1());
		j = j + 4;
	    }

	}

	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.getRow(0).getCell(0).setCellStyle(getStyle0());
	sh.addMergedRegion(new CellRangeAddress(0, 0, 0, j));

	for (int i = 2; i < j; i++) {
	    sh.setColumnWidth(i, 256 * 10);
	    for (int m = 1; m <= 31; m++) {
		sh.getRow(m + 2).createCell(i).setCellStyle(getStyle6());
	    }
	}

	sh.getRow(1).setHeightInPoints(35);

	// 第一个参数表示要冻结的列数，这里只冻结行所以为0；
	// 第二个参数表示要冻结的行数；
	// 第三个参数表示右边区域可见的首列序号，从1开始计算，这里是冻结行，所以为0；
	// 第四个参数表示下边区域可见的首行序号，也是从1开始计算；
	// sh.createFreezePane(0, 1, 0, 13);

	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
	for (int i = 0; i < data.size(); i++) {
	    Map<String, Object> row = data.get(i);
	    // 新增数据行
	    // 插入单元格数据
	    if (row.size() == 0) {

	    } else {
		int rowNumber = Integer.parseInt(getKey(prvdMap, row.get("prvd_name").toString()));
		int colNumber = Integer.parseInt(getKey(mothMap, row.get("Aud_trm").toString()));
		if ((Integer) row.get("Num_5001") != 0) {
		    sh.getRow(2 + rowNumber).getCell(colNumber).setCellValue("[" + row.get("Num_5001").toString() + "," + row.get("Min_5001").toString() + "," + row.get("Max_5001").toString() + "]");
		}
		if ((Integer) row.get("Num_5002") != 0) {
		    sh.getRow(2 + rowNumber).getCell(colNumber + 1)
			    .setCellValue("[" + row.get("Num_5002").toString() + "," + row.get("Min_5002").toString() + "," + row.get("Max_5002").toString() + "]");
		}
		if ((Integer) row.get("Num_5003") != 0) {
		    sh.getRow(2 + rowNumber).getCell(colNumber + 2)
			    .setCellValue("[" + row.get("Num_5003").toString() + "," + row.get("Min_5003").toString() + "," + row.get("Max_5003").toString() + "]");
		}
		if ((Integer) row.get("Num_5004") != 0) {
		    sh.getRow(2 + rowNumber).getCell(colNumber + 3)
			    .setCellValue("[" + row.get("Num_5004").toString() + "," + row.get("Min_5004").toString() + "," + row.get("Max_5004").toString() + "]");
		}

	    }
	}
    }

    public HashMap prvdNameMap() {
	HashMap<String, String> prvdMap = new HashMap<String, String>();
	prvdMap.put("1", "北京");
	prvdMap.put("2", "天津");
	prvdMap.put("3", "河北");
	prvdMap.put("4", "山西");
	prvdMap.put("5", "内蒙古");
	prvdMap.put("6", "辽宁");
	prvdMap.put("7", "吉林");
	prvdMap.put("8", "黑龙江");
	prvdMap.put("9", "上海");
	prvdMap.put("10", "江苏");
	prvdMap.put("11", "浙江");
	prvdMap.put("12", "安徽");
	prvdMap.put("13", "福建");
	prvdMap.put("14", "江西");
	prvdMap.put("15", "山东");
	prvdMap.put("16", "河南");
	prvdMap.put("17", "湖北");
	prvdMap.put("18", "湖南");
	prvdMap.put("19", "广东");
	prvdMap.put("20", "广西");
	prvdMap.put("21", "海南");
	prvdMap.put("22", "重庆");
	prvdMap.put("23", "四川");
	prvdMap.put("24", "贵州");
	prvdMap.put("25", "云南");
	prvdMap.put("26", "西藏");
	prvdMap.put("27", "陕西");
	prvdMap.put("28", "甘肃");
	prvdMap.put("29", "青海");
	prvdMap.put("30", "宁夏");
	prvdMap.put("31", "新疆");
	return prvdMap;
    }

    public static String getKey(HashMap<String, String> map, String value) {
	String key = null;
	// Map,HashMap并没有实现Iteratable接口.不能用于增强for循环.
	for (String getKey : map.keySet()) {
	    if (map.get(getKey).equals(value)) {
		key = getKey;
	    }
	}
	return key;
	// 这个key肯定是最后一个满足该条件的key.
    }

    private CellStyle getStyle00() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_LEFT);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 16);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style.setFont(font);
	return style;
    }

    private CellStyle getStyle0() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 16);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style.setFont(font);
	return style;
    }

    private CellStyle getStyle1() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 11);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THIN);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setWrapText(true);
	// style.setFillBackgroundColor(HSSFColor.BLUE.index);
	style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	return style;
    }

    private CellStyle getStyle2() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 11);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THIN);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setWrapText(true);
	// style.setFillBackgroundColor(HSSFColor.BLUE.index);
	style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
	style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	return style;
    }

    private CellStyle getStyle2_yellow() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 11);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THIN);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setWrapText(true);
	// style.setFillBackgroundColor(HSSFColor.BLUE.index);
	style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
	style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	return style;
    }

    private CellStyle getStyle2_red() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 11);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THIN);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setWrapText(true);
	// style.setFillBackgroundColor(HSSFColor.BLUE.index);
	style.setFillForegroundColor(IndexedColors.RED.getIndex());
	style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	return style;
    }

    // modified by GuoXY 20161024 for CMCC_集中化持续审计系统_软件需求规格说明书_社会渠道终端套利专题的逻辑和变更说明v1.7 郭倩
    private CellStyle getStyle7() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THIN);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
	style.setWrapText(true);
	return style;
    }

    private CellStyle getStyle3() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THIN);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setWrapText(true);
	return style;
    }

    private CellStyle getStyle4() {
	CellStyle style0 = wb.createCellStyle();
	style0.setAlignment(CellStyle.ALIGN_CENTER);
	style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	style0.setFont(font);
	style0.setBorderBottom(CellStyle.BORDER_THIN);
	style0.setBorderLeft(CellStyle.BORDER_THIN);
	style0.setBorderRight(CellStyle.BORDER_THIN);
	style0.setBorderTop(CellStyle.BORDER_THIN);
	style0.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
	return style0;
    }

    private CellStyle getStyle5() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THIN);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setWrapText(true);
	style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
	return style;
    }

    private CellStyle getStyle6() {
	CellStyle style0 = wb.createCellStyle();
	style0.setAlignment(CellStyle.ALIGN_CENTER);
	style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 11);
	style0.setFont(font);
	style0.setBorderBottom(CellStyle.BORDER_THIN);
	style0.setBorderLeft(CellStyle.BORDER_THIN);
	style0.setBorderRight(CellStyle.BORDER_THIN);
	style0.setBorderTop(CellStyle.BORDER_THIN);
	style0.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
	return style0;
    }

    private CellStyle getStyle8() {
	CellStyle style0 = wb.createCellStyle();
	style0.setAlignment(CellStyle.ALIGN_CENTER);
	style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 11);
	style0.setFont(font);
	style0.setBorderBottom(CellStyle.BORDER_THIN);
	style0.setBorderLeft(CellStyle.BORDER_THIN);
	style0.setBorderRight(CellStyle.BORDER_THIN);
	style0.setBorderTop(CellStyle.BORDER_THIN);
	style0.setDataFormat(wb.createDataFormat().getFormat("0.00"));
	return style0;
    }
}