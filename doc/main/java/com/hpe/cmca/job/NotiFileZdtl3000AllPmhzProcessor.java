package com.hpe.cmca.job;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service("NotiFileZdtl3000AllPmhzProcessor")
public class NotiFileZdtl3000AllPmhzProcessor extends AbstractNotiFileProcessor {

    protected String focusCd = "3000";
    protected Logger logger  = Logger.getLogger(this.getClass());

    @Override
    protected String buildFileName() {
	fileName = fileName + ".xlsx";
	return fileName;
    }

    protected List<String> getAudTrmListToSomeDate(String d1, String d2) {
	List<String> dateList = new ArrayList<String>();
	String temp = d1;
	dateList.add(temp);
	while (!d2.equals(temp)) {
	    temp = getLastMon(temp);
	    dateList.add(temp);

	}
	return dateList;
    }

    protected String getLastMon(String d1) {
	Integer thisYear = Integer.parseInt(d1.substring(0, 4));
	Integer thisMonth = Integer.parseInt("0".equals(d1.substring(4, 5)) ? d1.substring(5, 6) : d1.substring(4, 6));
	Integer retYear = thisYear;
	Integer retMonth = 0;
	if (thisMonth >= 2) {
	    retMonth = thisMonth - 1;
	} else if (thisMonth == 1) {
	    retMonth = 12;
	    retYear = thisYear - 1;
	}
	String mon = retMonth.toString();
	StringBuffer sb = new StringBuffer(2);
	sb.append("0");
	if (retMonth <= 9) {
	    sb.append(retMonth);
	    mon = sb.toString();
	}
	return retYear.toString() + mon;
    }


    public boolean generate() throws Exception {
	if (Integer.parseInt(month) >= 201510) {
	    this.logger.info("");
	    Integer thisYear = Integer.parseInt(month.substring(0, 4));
	    Integer thisMonth = Integer.parseInt("0".equals(month.substring(4, 5)) ? month.substring(5, 6) : month.substring(4, 6));
	    this.setFileName("2015年10月-" + thisYear + "年" + thisMonth + "月-" + "社会渠道终端套利排名汇总");
	    this.logger.info("开始生成：2015年10月-" + thisYear + "年" + thisMonth + "月-" + "社会渠道终端套利排名汇总");
	    // 更新最新排名
	    notiFileGenService.getNotiFileZdtl3000Pmhz(month, "updateReportSort");
	    // 创建第一个sheet
	    sh = wb.createSheet("全公司审计结果摘要");
	    writeFirstPart(notiFileGenService.getNotiFileZdtl3000Pmhz(month, "allReport"), sh, month);

	    // 创建第二个sheet
	    sh = wb.createSheet("各公司排名汇总");
	    writeSecondPart(notiFileGenService.getNotiFileZdtl3000Pmhz(month, "prvdReport"), sh, month);
	} else {
	    return false;
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
    public void writeFirstPart(List<Map<String, Object>> data, Sheet sh, String month) {
	sh.createRow(0);
	sh.getRow(0).createCell(0).setCellValue("“社会渠道终端套利”持续审计结果摘要");
	// 创建行
	sh.createRow(1);
	sh.createRow(2).createCell(0).setCellStyle(getStyle0());
	sh.createRow(3).createCell(0).setCellStyle(getStyle9());
	for (int i = 4; i <= 12; i++) {
	    sh.createRow(i).createCell(0).setCellStyle(getStyle10());
	    sh.getRow(i).getCell(0).setCellValue("-");
	}
	sh.getRow(2).getCell(0).setCellValue("总体情况");
	sh.getRow(3).getCell(0).setCellValue("审计月");
	sh.getRow(5).getCell(0).setCellValue("社会渠道终端销售数量");
	sh.getRow(6).getCell(0).setCellValue("终端销售渠道数量");
	sh.getRow(7).getCell(0).setCellValue("异常销售终端数量");
	sh.getRow(8).getCell(0).setCellValue("异常销售占比");
	sh.getRow(9).getCell(0).setCellValue("异常销售涉及渠道数量");
	sh.getRow(10).getCell(0).setCellValue("套利终端数量");
	sh.getRow(11).getCell(0).setCellValue("套利终端销售占比");
	sh.getRow(12).getCell(0).setCellValue("终端套利涉及渠道数量");

	sh.createRow(14).createCell(0).setCellValue("备注：以上指标中涉及“数量”的“增幅”指的是环比增幅，等于（本月数值-上月数值）/上月数值；涉及“占比”的“增幅”，等于本月占比-上月占比。");

	int j = 1;

	HashMap<String, String> mothMap = new HashMap<String, String>();
	List<String> mothList = getAudTrmListToSomeDate(month, "201510");
	for (int i = 0,mothSize=mothList.size(); i < mothSize; i++) {
	    // Map<String, Object> row = data.get(i);
	    String moth = mothList.get(i);
	    if (!mothMap.containsValue(moth)) {
		mothMap.put(Integer.toString(i), moth);
		Integer thisMonth = Integer.parseInt("0".equals(moth.substring(4, 5)) ? moth.substring(5, 6) : moth.substring(4, 6));
		sh.getRow(3).createCell(j).setCellValue(moth.substring(0, 4) + "年" + thisMonth + "月");
		sh.getRow(4).createCell(j).setCellValue("数值");
		sh.getRow(4).createCell(j + 1).setCellValue("增幅");
		sh.getRow(3).getCell(j).setCellStyle(getStyle9());
		sh.getRow(3).createCell(j + 1).setCellStyle(getStyle9());
		for (int n = 5; n <= 12; n++) {
		    sh.getRow(n).createCell(j).setCellStyle(getStyle6());
		    sh.getRow(n).createCell(j + 1).setCellStyle(getStyle4());
		    sh.getRow(n).getCell(j).setCellValue("-");
		    sh.getRow(n).getCell(j + 1).setCellValue("-");
		}
		sh.addMergedRegion(new CellRangeAddress(3, 3, j, j + 1));
		sh.getRow(4).getCell(j).setCellStyle(getStyle9());
		sh.getRow(4).getCell(j + 1).setCellStyle(getStyle9());
		j = j + 2;
	    }

	}

	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.getRow(0).getCell(0).setCellStyle(getStyle0());
	sh.getRow(14).getCell(0).setCellStyle(getStyle000());
	sh.addMergedRegion(new CellRangeAddress(0, 0, 0, j - 1));
	sh.addMergedRegion(new CellRangeAddress(3, 4, 0, 0));
	sh.addMergedRegion(new CellRangeAddress(14, 14, 0, j - 1));
	sh.setColumnWidth(0, 256 * 35);
	for (int i = 1; i < j; i++) {
	    sh.setColumnWidth(i, 256 * 18);
	    // for (int m = 1; m <= 31; m++) {
	    // sh.getRow(m + 2).createCell(i).setCellStyle(getStyle6());
	    // }
	}

	sh.getRow(1).setHeightInPoints(35);

	Map<String,Object> row=new HashMap<String,Object>();
	for (int i = 0,dataSize=data.size(); i < dataSize; i++) {
	    row = data.get(i);
	    // 插入单元格数据
	    if (row.size() == 0) {
		sh.getRow(2 + i).createCell(2).setCellValue("-");
	    } else {
		int colNumber = 1 + 2 * (Integer.parseInt(getKey(mothMap, row.get("aud_trm").toString())));
		// for(int n=5;n<=12;n++){
		// sh.getRow(n).createCell(colNumber).setCellStyle(getStyle6());
		// sh.getRow(n).createCell(colNumber+1).setCellStyle(getStyle4());
		// }
		// 数值
		if (row.get("tol_num") != null) {
		    sh.getRow(5).getCell(colNumber).setCellValue((Integer) row.get("tol_num"));
		}
		if (row.get("tol_chnlqty") != null) {
		    sh.getRow(6).getCell(colNumber).setCellValue((Integer) row.get("tol_chnlqty"));
		}
		if (row.get("weigui_num") != null) {
		    sh.getRow(7).getCell(colNumber).setCellValue((Integer) row.get("weigui_num"));
		}
		if (row.get("weigui_pnt") != null) {
		    if (row.get("weigui_pnt").toString().substring(0, 5).equals("-9999")) {
			sh.getRow(8).getCell(colNumber).setCellValue("NA");
		    } else {
			sh.getRow(8).getCell(colNumber).setCellValue(row.get("weigui_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("weigui_pnt")).doubleValue() : (Long) row.get("weigui_pnt"));
			sh.getRow(8).getCell(colNumber).setCellStyle(getStyle4());
		    }

		    // sh.getRow(8).getCell(colNumber).setCellValue(row.get("weigui_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("weigui_pnt")).doubleValue() : (Long) row.get("weigui_pnt"));
		    // sh.getRow(8).getCell(colNumber).setCellStyle(getStyle4());
		}
		if (row.get("weigui_chnlqty") != null) {
		    sh.getRow(9).getCell(colNumber).setCellValue((Integer) row.get("weigui_chnlqty"));
		}
		if (row.get("taoli_weigui_num") != null) {
		    sh.getRow(10).getCell(colNumber).setCellValue((Integer) row.get("taoli_weigui_num"));
		}
		if (row.get("taoli_tol_pnt") != null) {
		    if (row.get("taoli_tol_pnt").toString().substring(0, 5).equals("-9999")) {
			sh.getRow(11).getCell(colNumber).setCellValue("NA");
		    } else {
			sh.getRow(11).getCell(colNumber)
			    .setCellValue(row.get("taoli_tol_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_tol_pnt")).doubleValue() : (Long) row.get("taoli_tol_pnt"));
			sh.getRow(11).getCell(colNumber).setCellStyle(getStyle4());
		    }
		    
//		    sh.getRow(11).getCell(colNumber)
//			    .setCellValue(row.get("taoli_tol_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_tol_pnt")).doubleValue() : (Long) row.get("taoli_tol_pnt"));
//		    sh.getRow(11).getCell(colNumber).setCellStyle(getStyle4());
		}
		if (row.get("taoli_weigui_chnlqty") != null) {
		    sh.getRow(12).getCell(colNumber).setCellValue((Integer) row.get("taoli_weigui_chnlqty"));
		}
		// 增幅
		if (row.get("tol_huanbi") != null) {
		    if(row.get("tol_huanbi").toString().substring(0, 5).equals("-9999")){
			sh.getRow(5).getCell(colNumber + 1).setCellValue("NA");
		    }else{
			sh.getRow(5).getCell(colNumber + 1).setCellValue(row.get("tol_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("tol_huanbi")).doubleValue() : (Long) row.get("tol_huanbi"));
		    }
		    sh.getRow(5).getCell(colNumber + 1).setCellValue(row.get("tol_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("tol_huanbi")).doubleValue() : (Long) row.get("tol_huanbi"));
		}
		if (row.get("tol_chnlqty_huanbi") != null) {
		    if(row.get("tol_chnlqty_huanbi").toString().substring(0, 5).equals("-9999")){
			sh.getRow(6).getCell(colNumber + 1).setCellValue("NA");
		    }else{
			sh.getRow(6).getCell(colNumber + 1)
			    .setCellValue(row.get("tol_chnlqty_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("tol_chnlqty_huanbi")).doubleValue() : (Long) row.get("tol_chnlqty_huanbi"));
		    }
//		    sh.getRow(6).getCell(colNumber + 1)
//			    .setCellValue(row.get("tol_chnlqty_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("tol_chnlqty_huanbi")).doubleValue() : (Long) row.get("tol_chnlqty_huanbi"));
		}
		if (row.get("weigui_huanbi") != null) {
		    if(row.get("weigui_huanbi").toString().substring(0, 5).equals("-9999")){
			sh.getRow(7).getCell(colNumber + 1).setCellValue("NA");
		    }else{
			sh.getRow(7).getCell(colNumber + 1)
			    .setCellValue(row.get("weigui_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("weigui_huanbi")).doubleValue() : (Long) row.get("weigui_huanbi"));
		    }
//		    sh.getRow(7).getCell(colNumber + 1)
//			    .setCellValue(row.get("weigui_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("weigui_huanbi")).doubleValue() : (Long) row.get("weigui_huanbi"));
		    // sh.getRow(7).getCell(colNumber+1).setCellStyle(getStyle4());
		}
		if (row.get("diff_weigui_pnt") != null) {
		    if(row.get("diff_weigui_pnt").toString().substring(0, 5).equals("-9999")){
			sh.getRow(8).getCell(colNumber + 1).setCellValue("NA");
		    }else{
			sh.getRow(8).getCell(colNumber + 1)
			    .setCellValue(row.get("diff_weigui_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("diff_weigui_pnt")).doubleValue() : (Long) row.get("diff_weigui_pnt"));
		    }
//		    sh.getRow(8).getCell(colNumber + 1)
//			    .setCellValue(row.get("diff_weigui_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("diff_weigui_pnt")).doubleValue() : (Long) row.get("diff_weigui_pnt"));
		    // sh.getRow(8).getCell(colNumber+1).setCellStyle(getStyle4());
		}
		if (row.get("weigui_chnlqty_huanbi") != null) {
		    if(row.get("weigui_chnlqty_huanbi").toString().substring(0, 5).equals("-9999")){
			sh.getRow(9).getCell(colNumber + 1).setCellValue("NA");
		    }else{
			 sh.getRow(9)
			    .getCell(colNumber + 1)
			    .setCellValue(
				    row.get("weigui_chnlqty_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("weigui_chnlqty_huanbi")).doubleValue() : (Long) row.get("weigui_chnlqty_huanbi"));
		    }
//		    sh.getRow(9)
//			    .getCell(colNumber + 1)
//			    .setCellValue(
//				    row.get("weigui_chnlqty_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("weigui_chnlqty_huanbi")).doubleValue() : (Long) row.get("weigui_chnlqty_huanbi"));
		    // sh.getRow(9).getCell(colNumber+1).setCellStyle(getStyle4());
		}
		if (row.get("taoli_huanbi") != null) {
		    if(row.get("taoli_huanbi").toString().substring(0, 5).equals("-9999")){
			sh.getRow(10).getCell(colNumber + 1).setCellValue("NA");
		    }else{
			sh.getRow(10).getCell(colNumber + 1)
			    .setCellValue(row.get("taoli_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_huanbi")).doubleValue() : (Long) row.get("taoli_huanbi"));
		    }
//		    sh.getRow(10).getCell(colNumber + 1)
//			    .setCellValue(row.get("taoli_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_huanbi")).doubleValue() : (Long) row.get("taoli_huanbi"));
		    // sh.getRow(10).getCell(colNumber+1).setCellStyle(getStyle4());
		}
		if (row.get("diff_taoli_tol_pnt") != null) {
		    if(row.get("diff_taoli_tol_pnt").toString().substring(0, 5).equals("-9999")){
			sh.getRow(11).getCell(colNumber + 1).setCellValue("NA");
		    }else{
			sh.getRow(11).getCell(colNumber + 1)
			    .setCellValue(row.get("diff_taoli_tol_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("diff_taoli_tol_pnt")).doubleValue() : (Long) row.get("diff_taoli_tol_pnt"));
		    }
//		    sh.getRow(11).getCell(colNumber + 1)
//			    .setCellValue(row.get("diff_taoli_tol_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("diff_taoli_tol_pnt")).doubleValue() : (Long) row.get("diff_taoli_tol_pnt"));
		    // sh.getRow(11).getCell(colNumber+1).setCellStyle(getStyle4());
		}
		if (row.get("taoli_chnlqty_huanbi") != null) {
		    if(row.get("taoli_chnlqty_huanbi").toString().substring(0, 5).equals("-9999")){
			sh.getRow(12).getCell(colNumber + 1).setCellValue("NA");
		    }else{
			sh.getRow(12)
			    .getCell(colNumber + 1)
			    .setCellValue(row.get("taoli_chnlqty_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_chnlqty_huanbi")).doubleValue() : (Long) row.get("taoli_chnlqty_huanbi"));
		    }
//		    sh.getRow(12)
//			    .getCell(colNumber + 1)
//			    .setCellValue(row.get("taoli_chnlqty_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_chnlqty_huanbi")).doubleValue() : (Long) row.get("taoli_chnlqty_huanbi"));
		    // sh.getRow(12).getCell(colNumber+1).setCellStyle(getStyle4());
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
    public void writeSecondPart(List<Map<String, Object>> data, Sheet sh, String month) {
	sh.createRow(0);
	sh.getRow(0).createCell(0).setCellValue("各公司社会渠道终端异常销售及套利情况");
	// 创建行
	sh.createRow(1).createCell(0);
	for (int i = 2; i <= 34; i++) {
	    sh.createRow(i).createCell(0);
	    sh.getRow(i).getCell(0).setCellValue("-");
	    sh.getRow(i).getCell(0).setCellStyle(getStyle9());
	}
	sh.getRow(2).createCell(0).setCellValue("省份");
	int j = 1;

	HashMap<String, String> mothMap = new HashMap<String, String>();
	List<String> mothList = getAudTrmListToSomeDate(month, "201510");
	for (int i = 0,mothSize=mothList.size(); i < mothSize; i++) {
	    String moth = mothList.get(i);
	    if (!mothMap.containsValue(moth)) {
		mothMap.put(Integer.toString(i), moth);
		Integer thisMonth = Integer.parseInt("0".equals(moth.substring(4, 5)) ? moth.substring(5, 6) : moth.substring(4, 6));
		sh.getRow(1).createCell(j).setCellValue(moth.substring(0, 4) + "年" + thisMonth + "月");
		// sh.getRow(1).createCell(j).setCellValue(moth.substring(4, 6) + "/1/" + moth.substring(0, 4));
		sh.getRow(2).createCell(j).setCellValue("排名");
		sh.getRow(2).createCell(j + 1).setCellValue("终端销售数量");
		sh.getRow(2).createCell(j + 2).setCellValue("终端销售渠道");
		sh.getRow(2).createCell(j + 3).setCellValue("异常销售数量");
		sh.getRow(2).createCell(j + 4).setCellValue("异常销售涉及渠道");
		sh.getRow(2).createCell(j + 5).setCellValue("异常销售占比");
		sh.getRow(2).createCell(j + 6).setCellValue("异常销售占比排名");
		sh.getRow(2).createCell(j + 7).setCellValue("套利终端数量");
		sh.getRow(2).createCell(j + 8).setCellValue("套利金额");
		sh.getRow(2).createCell(j + 9).setCellValue("终端套利涉及渠道");
		sh.getRow(2).createCell(j + 10).setCellValue("套利终端占销量比");
		sh.getRow(2).createCell(j + 11).setCellValue("套利终端占异常终端比");
		sh.getRow(2).createCell(j + 12).setCellValue("套利终端占异常终端比排名");
		sh.getRow(2).createCell(j + 13).setCellValue("异常销售占比增幅");
		sh.getRow(2).createCell(j + 14).setCellValue("套利占比增幅");
		sh.getRow(2).createCell(j + 15).setCellValue("异常销售数量环比");
		sh.getRow(2).createCell(j + 16).setCellValue("套利终端数量环比");

		for (int m = 0; m <= 16; m++) {
		    if (m == 0) {
			sh.getRow(1).getCell(j).setCellStyle(getStyle9());
		    } else {
			sh.getRow(1).createCell(j + m).setCellStyle(getStyle9());
		    }

		    sh.getRow(2).getCell(j + m).setCellStyle(getStyle9());
		}
		sh.getRow(2).getCell(0).setCellStyle(getStyle9());
		for (int m = 0; m <= 16; m++) {
		    for (int n = 3; n <= 34; n++) {
			sh.getRow(n).createCell(j + m).setCellStyle(getStyle6());
			sh.getRow(n).getCell(j + m).setCellValue("-");
		    }

		}
		sh.addMergedRegion(new CellRangeAddress(1, 1, j, j + 16));
		j = j + 17;
	    }

	}
	sh.getRow(1).getCell(0).setCellStyle(getStyle9());

	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.getRow(0).getCell(0).setCellStyle(getStyle0());
	sh.addMergedRegion(new CellRangeAddress(0, 0, 0, j - 1));

	for (int i = 2; i < j; i++) {
	    sh.setColumnWidth(i, 256 * 16);
	    // for (int m = 1; m <= 31; m++) {
	    // sh.getRow(m + 2).createCell(i).setCellStyle(getStyle6());
	    // }
	}

	sh.getRow(1).setHeightInPoints(35);

	Map<String,Object> row=new HashMap<String,Object>();
	for (int i = 0,dataSize=data.size(); i < dataSize; i++) {
	    row = data.get(i);
	    // 插入单元格数据
	    if (row.size() == 0) {
		sh.getRow(2 + i).createCell(2).setCellValue("-");
	    } else {
		int rowNumber = 0;
		int colNumber = 1 + 17 * (Integer.parseInt(getKey(mothMap, row.get("aud_trm").toString())));
		if (row.get("zx_order") != null) {
		    if ((Integer) row.get("zx_order") == 0) {
			rowNumber = 34;
			sh.getRow(rowNumber).getCell(0).setCellValue("合计");
		    } else {
			rowNumber = (Integer) row.get("zx_order") + 2;
			if (row.get("CMCC_prov_prvd_nm") != null) {
			    sh.getRow(rowNumber).getCell(0).setCellValue(row.get("CMCC_prov_prvd_nm").toString());
			}
		    }
		    sh.getRow(rowNumber).getCell(0).setCellStyle(getStyle6());

		}

		// for(int m=0;m<=16;m++){
		// sh.getRow(rowNumber).createCell(colNumber+m).setCellStyle(getStyle6());
		// }
		// 排名==异常销售占比排名 by 后台-文德
		if (row.get("weigui_order") != null) {
		    sh.getRow(rowNumber).getCell(colNumber).setCellValue((Integer) row.get("weigui_order"));
		    sh.getRow(rowNumber).getCell(colNumber + 6).setCellValue((Integer) row.get("weigui_order"));
		}
		if (row.get("tol_num") != null) {
		    sh.getRow(rowNumber).getCell(colNumber + 1).setCellValue((Integer) row.get("tol_num"));
		}
		if (row.get("tol_chnlqty") != null) {
		    sh.getRow(rowNumber).getCell(colNumber + 2).setCellValue((Integer) row.get("tol_chnlqty"));
		}
		if (row.get("weigui_num") != null) {
		    sh.getRow(rowNumber).getCell(colNumber + 3).setCellValue((Integer) row.get("weigui_num"));
		}
		if (row.get("weigui_chnlqty") != null) {
		    sh.getRow(rowNumber).getCell(colNumber + 4).setCellValue((Integer) row.get("weigui_chnlqty"));
		}
		if (row.get("weigui_pnt") != null) {
		    if(row.get("weigui_pnt").toString().substring(0, 5).equals("-9999")){
			sh.getRow(rowNumber).getCell(colNumber + 5).setCellValue("NA");
		    }else{
			sh.getRow(rowNumber).getCell(colNumber + 5)
			    .setCellValue(row.get("weigui_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("weigui_pnt")).doubleValue() : (Long) row.get("weigui_pnt"));
			sh.getRow(rowNumber).getCell(colNumber + 5).setCellStyle(getStyle4());
		    }
//		    sh.getRow(rowNumber).getCell(colNumber + 5)
//			    .setCellValue(row.get("weigui_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("weigui_pnt")).doubleValue() : (Long) row.get("weigui_pnt"));
//		    sh.getRow(rowNumber).getCell(colNumber + 5).setCellStyle(getStyle4());
		}
		if (row.get("taoli_weigui_num") != null) {
		    sh.getRow(rowNumber).getCell(colNumber + 7).setCellValue((Integer) row.get("taoli_weigui_num"));
		}
		if (row.get("taoli_jine") != null) {
		    sh.getRow(rowNumber).getCell(colNumber + 8)
			    .setCellValue(row.get("taoli_jine") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_jine")).doubleValue() : (Long) row.get("taoli_jine"));
		}
		if (row.get("taoli_weigui_chnlqty") != null) {
		    sh.getRow(rowNumber).getCell(colNumber + 9).setCellValue((Integer) row.get("taoli_weigui_chnlqty"));
		}
		if (row.get("taoli_tol_pnt") != null) {
		    if(row.get("taoli_tol_pnt").toString().substring(0, 5).equals("-9999")){
			sh.getRow(rowNumber).getCell(colNumber + 10).setCellValue("NA");
		    }else{
			sh.getRow(rowNumber).getCell(colNumber + 10)
			    .setCellValue(row.get("taoli_tol_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_tol_pnt")).doubleValue() : (Long) row.get("taoli_tol_pnt"));
			sh.getRow(rowNumber).getCell(colNumber + 10).setCellStyle(getStyle4());
		    }
//		    sh.getRow(rowNumber).getCell(colNumber + 10)
//			    .setCellValue(row.get("taoli_tol_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_tol_pnt")).doubleValue() : (Long) row.get("taoli_tol_pnt"));
//		    sh.getRow(rowNumber).getCell(colNumber + 10).setCellStyle(getStyle4());
		}
		if (row.get("taoli_weigui_pnt") != null) {
		    if(row.get("taoli_weigui_pnt").toString().substring(0, 5).equals("-9999")){
			sh.getRow(rowNumber).getCell(colNumber + 11).setCellValue("NA");
		    }else{
			sh.getRow(rowNumber).getCell(colNumber + 11)
			    .setCellValue(row.get("taoli_weigui_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_weigui_pnt")).doubleValue() : (Long) row.get("taoli_weigui_pnt"));
			sh.getRow(rowNumber).getCell(colNumber + 11).setCellStyle(getStyle4());
		    }
//		    sh.getRow(rowNumber).getCell(colNumber + 11)
//			    .setCellValue(row.get("taoli_weigui_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_weigui_pnt")).doubleValue() : (Long) row.get("taoli_weigui_pnt"));
//		    sh.getRow(rowNumber).getCell(colNumber + 11).setCellStyle(getStyle4());
		}
		if (row.get("taoli_weigui_order") != null) {
		    sh.getRow(rowNumber).getCell(colNumber + 12).setCellValue((Integer) row.get("taoli_weigui_order"));
		}
		if (row.get("diff_weigui_pnt") != null) {
		    sh.getRow(rowNumber).getCell(colNumber + 13)
			    .setCellValue(row.get("diff_weigui_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("diff_weigui_pnt")).doubleValue() : (Long) row.get("diff_weigui_pnt"));
		    sh.getRow(rowNumber).getCell(colNumber + 13).setCellStyle(getStyle4());
		}
		if (row.get("diff_taoli_tol_pnt") != null) {
		    sh.getRow(rowNumber).getCell(colNumber + 14)
			    .setCellValue(row.get("diff_taoli_tol_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("diff_taoli_tol_pnt")).doubleValue() : (Long) row.get("diff_taoli_tol_pnt"));
		    sh.getRow(rowNumber).getCell(colNumber + 14).setCellStyle(getStyle4());
		}
		if (row.get("weigui_huanbi") != null) {
		    if(row.get("weigui_huanbi").toString().substring(0, 5).equals("-9999")){
			sh.getRow(rowNumber).getCell(colNumber + 15).setCellValue("NA");
		    }else{
			sh.getRow(rowNumber).getCell(colNumber + 15)
			    .setCellValue(row.get("weigui_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("weigui_huanbi")).doubleValue() : (Long) row.get("weigui_huanbi"));
			sh.getRow(rowNumber).getCell(colNumber + 15).setCellStyle(getStyle4());
		    }
//		    sh.getRow(rowNumber).getCell(colNumber + 15)
//			    .setCellValue(row.get("weigui_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("weigui_huanbi")).doubleValue() : (Long) row.get("weigui_huanbi"));
//		    sh.getRow(rowNumber).getCell(colNumber + 15).setCellStyle(getStyle4());
		}
		if (row.get("taoli_huanbi") != null) {
		    if(row.get("taoli_huanbi").toString().substring(0, 5).equals("-9999")){
			sh.getRow(rowNumber).getCell(colNumber + 16).setCellValue("NA");
		    }else{
			sh.getRow(rowNumber).getCell(colNumber + 16)
			    .setCellValue(row.get("taoli_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_huanbi")).doubleValue() : (Long) row.get("taoli_huanbi"));
			sh.getRow(rowNumber).getCell(colNumber + 16).setCellStyle(getStyle4());
		    }
//		    sh.getRow(rowNumber).getCell(colNumber + 16)
//			    .setCellValue(row.get("taoli_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_huanbi")).doubleValue() : (Long) row.get("taoli_huanbi"));
//		    sh.getRow(rowNumber).getCell(colNumber + 16).setCellStyle(getStyle4());
		}

	    }

	}

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

    private CellStyle getStyle000() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_LEFT);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 11);
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

    private CellStyle getStyle9() {
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
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style0.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
	return style0;
    }

    private CellStyle getStyle10() {
	CellStyle style0 = wb.createCellStyle();
	style0.setAlignment(CellStyle.ALIGN_LEFT);
	style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 11);
	style0.setFont(font);
	style0.setBorderBottom(CellStyle.BORDER_THIN);
	style0.setBorderLeft(CellStyle.BORDER_THIN);
	style0.setBorderRight(CellStyle.BORDER_THIN);
	style0.setBorderTop(CellStyle.BORDER_THIN);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style0.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
	return style0;
    }
}