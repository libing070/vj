package com.hpe.cmca.job;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service("NotiFileZdtl3000Processor")
public class NotiFileZdtl3000Processor extends AbstractNotiFileProcessor{
	protected Logger logger = Logger.getLogger(this.getClass());
	protected String focusCd = "3000";
	
	public boolean generate() throws Exception{
		this.setFileName("社会渠道终端套利排名汇总");
		logger.debug("---------writeSheet3000 [month:" + month + ",focusCd=3000]");
		writeSheet3000(notiFileGenService.getNotiFile3000Data(month, "3000"));
		logger.debug("---------writeSheet3001 [month:" + month + ",focusCd=3001]");
		writeSheet3001(notiFileGenService.getNotiFile3000Data(month, "3001"));
		logger.debug("---------writeSheet3002 [month:" + month + ",focusCd=3001]");
		writeSheet3002(notiFileGenService.getNotiFile3000Data(month, "3002"));
		logger.debug("---------writeSheet3004 [month:" + month + ",focusCd=3001]");
		writeSheet3004(notiFileGenService.getNotiFile3000Data(month, "3004"));
		logger.debug("---------writeSheet3005 [month:" + month + ",focusCd=3001]");
		writeSheet3005(notiFileGenService.getNotiFile3000Data(month, "3005"));
		return true;
	}
	
	public void writeSheet3000(List<Map<String, Object>> data){
		logger.debug("---------data:\n" + data);
		sh = wb.createSheet("汇总排名");
		sh.createRow(0).createCell(0).setCellValue("社会渠道终端异常销售及套利情况");
		sh.getRow(0).getCell(0).setCellStyle(getStyle0());
		
		sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 12)); 
		sh.createRow(1);
		sh.getRow(1).createCell(0).setCellValue("省份");
		sh.getRow(1).createCell(1).setCellValue("终端销售数量");
		sh.getRow(1).createCell(2).setCellValue("终端销售渠道");
		sh.getRow(1).createCell(3).setCellValue("异常销售数量");
		sh.getRow(1).createCell(4).setCellValue("异常销售涉及渠道");
		sh.getRow(1).createCell(5).setCellValue("异常销售占比");
		sh.getRow(1).createCell(6).setCellValue("异常销售占比排名");
		sh.getRow(1).createCell(7).setCellValue("套利终端数量");
		sh.getRow(1).createCell(8).setCellValue("套利金额");
		sh.getRow(1).createCell(9).setCellValue("终端套利涉及渠道");
		sh.getRow(1).createCell(10).setCellValue("套利终端占销量比");
		sh.getRow(1).createCell(11).setCellValue("套利终端占异常终端比");
		sh.getRow(1).createCell(12).setCellValue("套利终端占异常终端比排名");
		sh.createFreezePane(0, 2, 0, 2);
		for(int i=0;i<13; i++) {
			sh.setColumnWidth(i, 256 * 12);
			sh.getRow(1).getCell(i).setCellStyle(getStyle2());
		}
		sh.setColumnWidth(11, 256 * 14);
		sh.setColumnWidth(12, 256 * 16);
		sh.getRow(1).setHeightInPoints(32);

		Map<String,Object> row=new HashMap<String,Object>();
		for(int i = 0,dataSize=data.size(); i <= dataSize; i++) {
			sh.createRow(2+i);
			if (i < data.size()) {
				row = data.get(i);
				sh.getRow(2+i).createCell(0).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
				sh.getRow(2+i).createCell(1).setCellValue((Integer) row.get("tol_num"));
				sh.getRow(2+i).createCell(2).setCellValue((Integer) row.get("totalChnlQty"));
				sh.getRow(2+i).createCell(3).setCellValue((Integer) row.get("weigui_num"));
				sh.getRow(2+i).createCell(4).setCellValue((Integer) row.get("weigui_chnlqty"));
				
				sh.getRow(2+i).createCell(5).setCellValue((row.get("weigui_per_cnt") instanceof BigDecimal? ((BigDecimal) row.get("weigui_per_cnt")).doubleValue() :(Double) row.get("weigui_per_cnt")));
				sh.getRow(2+i).createCell(6).setCellValue((Integer) row.get("weigui_order"));
				sh.getRow(2+i).createCell(7).setCellValue((Integer) row.get("taoli_num"));
				sh.getRow(2+i).createCell(8).setCellValue(row.get("infraction_sett_amt") instanceof BigDecimal?((BigDecimal)row.get("infraction_sett_amt")).doubleValue():  (Double) row.get("infraction_sett_amt"));
				 
				sh.getRow(2+i).createCell(9).setCellValue((Integer) row.get("taoli_chnlqty"));
				sh.getRow(2+i).createCell(10).setCellValue(row.get("taoli_per_cnt") instanceof BigDecimal?((BigDecimal)row.get("taoli_per_cnt")).doubleValue():  (Double) row.get("taoli_per_cnt"));
				sh.getRow(2+i).createCell(11).setCellValue(row.get("taoli_weigui_per_cnt") instanceof BigDecimal?((BigDecimal)row.get("taoli_weigui_per_cnt")).doubleValue():  (Double) row.get("taoli_weigui_per_cnt"));
				// modified by GuoXY 20161024 for CMCC_集中化持续审计系统_软件需求规格说明书_社会渠道终端套利专题的逻辑和变更说明v1.7 郭倩
				//sh.getRow(2+i).createCell(12).setCellValue(((Integer) row.get("taoli_num") != null && (Integer) row.get("taoli_num") == 0) ? '-' : (Integer) row.get("taoli_weigui_order") );
				if ((Integer) row.get("taoli_num") != null && (Integer) row.get("taoli_num") == 0){
					sh.getRow(2+i).createCell(12).setCellValue("-");
					sh.getRow(2+i).getCell(12).setCellStyle(getStyle7());
				}else{
					sh.getRow(2+i).createCell(12).setCellValue((Integer) row.get("taoli_weigui_order"));
					sh.getRow(2+i).getCell(12).setCellStyle(getStyle6());
				}
			} else {
				sh.getRow(2+i).createCell(0).setCellValue("合计");
				sh.getRow(2+i).createCell(1).setCellFormula("SUM(B3:B"+ (i+2) + ")");
				sh.getRow(2+i).createCell(2).setCellFormula("SUM(C3:C"+ (i+2) + ")");
				sh.getRow(2+i).createCell(3).setCellFormula("SUM(D3:D"+ (i+2) + ")");
				sh.getRow(2+i).createCell(4).setCellFormula("SUM(E3:E"+ (i+2) + ")");
				sh.getRow(2+i).createCell(5).setCellFormula("D" + (i+3) + "/B"+ (i+3));
				sh.getRow(2+i).createCell(6);
				sh.getRow(2+i).createCell(7).setCellFormula("SUM(H3:H"+ (i+2) + ")");
				sh.getRow(2+i).createCell(8).setCellFormula("SUM(I3:I"+ (i+2) + ")");
				sh.getRow(2+i).createCell(9).setCellFormula("SUM(J3:J"+ (i+2) + ")");
				sh.getRow(2+i).createCell(10).setCellFormula("H" + (i+3) + "/B"+ (i+3));
				sh.getRow(2+i).createCell(11).setCellFormula("H" + (i+3) + "/D"+ (i+3));
				sh.getRow(2+i).createCell(12);
			}
			for(int j=0;j<13;j++) {
				sh.getRow(2+i).getCell(j).setCellStyle(getStyle6());
			}
			sh.getRow(2+i).getCell(0).setCellStyle(getStyle3());
			sh.getRow(2+i).getCell(8).setCellStyle(getStyle5());
			sh.getRow(2+i).getCell(5).setCellStyle(getStyle4());
			sh.getRow(2+i).getCell(10).setCellStyle(getStyle4());
			sh.getRow(2+i).getCell(11).setCellStyle(getStyle4());
		}
	}
	
	public void writeSheetCommon(List<Map<String, Object>> data, String sheetName, String title, String[] columnNames){
		logger.debug("---------data:\n" + data);
		sh = wb.createSheet(sheetName);
		sh.createRow(0).createCell(0).setCellValue(title);
		sh.getRow(0).getCell(0).setCellStyle(getStyle0());
		sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 9)); 
		sh.createRow(1);
		for (int i = 0; i < columnNames.length; i++) {
			sh.getRow(1).createCell(i).setCellValue(columnNames[i]);
			sh.setColumnWidth(i, 256 * 12);
			if (i == 0 || i == 1 || i == 2 || i == 6 || i == 8) {
				sh.getRow(1).getCell(i).setCellStyle(getStyle1());
			} else {
				sh.getRow(1).getCell(i).setCellStyle(getStyle2());
			}
		}
		sh.setColumnWidth(3, 256 * 14);
		sh.setColumnWidth(5, 256 * 16);
		sh.setColumnWidth(6, 256 * 14);
		sh.setColumnWidth(7, 256 * 20);
		sh.setColumnWidth(8, 256 * 14);
		sh.setColumnWidth(9, 256 * 16);
		sh.getRow(1).setHeightInPoints(32);
		sh.createFreezePane(0, 2, 0, 2);
		int tolNum = 0;
		Map<String,Object> row=new HashMap<String,Object>();
		for(int i = 0,dataSize=data.size(); i <= dataSize; i++) {
			sh.createRow(2+i);
			if (i < dataSize) {
				row = data.get(i);
				sh.getRow(2+i).createCell(0).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
				sh.getRow(2+i).createCell(1).setCellValue((Integer) row.get("weigui_num"));
				sh.getRow(2+i).createCell(2).setCellValue((Integer) row.get("weigui_chnlqty"));
				sh.getRow(2+i).createCell(3).setCellValue((Integer) row.get("taoli_num"));
				sh.getRow(2+i).createCell(4).setCellValue(row.get("infraction_sett_amt") instanceof BigDecimal ? ((BigDecimal) row.get("infraction_sett_amt")).doubleValue():(Double) row.get("infraction_sett_amt"));
				sh.getRow(2+i).createCell(5).setCellValue((Integer) row.get("taoli_chnlqty"));
				sh.getRow(2+i).createCell(6).setCellValue(row.get("weigui_per_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("weigui_per_cnt")).doubleValue():(Double) row.get("weigui_per_cnt"));
				//(Double) row.get("weigui_per_cnt"));
				sh.getRow(2+i).createCell(7).setCellValue(row.get("taoli_weigui_per_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_weigui_per_cnt")).doubleValue():(Double) row.get("taoli_weigui_per_cnt"));
				//(Double) row.get("taoli_weigui_per_cnt"));
				sh.getRow(2+i).createCell(8).setCellValue((Integer) row.get("weigui_order"));
				// modified by GuoXY 20161024 for CMCC_集中化持续审计系统_软件需求规格说明书_社会渠道终端套利专题的逻辑和变更说明v1.7 郭倩
				if ((Integer) row.get("taoli_num") != null && (Integer) row.get("taoli_num") == 0){
					sh.getRow(2+i).createCell(9).setCellValue("-");
					sh.getRow(2+i).getCell(9).setCellStyle(getStyle7());
				}else{
					sh.getRow(2+i).createCell(9).setCellValue((Integer) row.get("taoli_order"));
					sh.getRow(2+i).getCell(9).setCellStyle(getStyle6());
				}
				tolNum += (Integer) row.get("tol_num");
			} else {
				sh.getRow(2+i).createCell(0).setCellValue("合计");
				sh.getRow(2+i).createCell(1).setCellFormula("SUM(B3:B"+ (i+2) + ")");
				sh.getRow(2+i).createCell(2).setCellFormula("SUM(C3:C"+ (i+2) + ")");
				sh.getRow(2+i).createCell(3).setCellFormula("SUM(D3:D"+ (i+2) + ")");
				sh.getRow(2+i).createCell(4).setCellFormula("SUM(E3:E"+ (i+2) + ")");
				sh.getRow(2+i).createCell(5).setCellFormula("SUM(F3:F"+ (i+2) + ")");
	
				if("3000".equals(this.focusCd)) {
					sh.getRow(2+i).createCell(6).setCellFormula("B" + (i+3) +"/汇总排名!B34");
				} else {
					sh.getRow(2+i).createCell(6).setCellFormula("B" + (i+3) +"/" + tolNum);
				}
				sh.getRow(2+i).createCell(7).setCellFormula("D" + (i+3) + "/B"+ (i+3));
				sh.getRow(2+i).createCell(8);
				sh.getRow(2+i).createCell(9);
				sh.getRow(2+i).getCell(9).setCellStyle(getStyle6());
			}
			// 排名列因为有两种格式数据，所以单独设置 modified by GuoXY 20161024
			for(int j=0;j<9;j++) {
				sh.getRow(2+i).getCell(j).setCellStyle(getStyle6());
			}
			sh.getRow(2+i).getCell(0).setCellStyle(getStyle3());
			sh.getRow(2+i).getCell(6).setCellStyle(getStyle4());
			sh.getRow(2+i).getCell(7).setCellStyle(getStyle4());
			sh.getRow(2+i).getCell(4).setCellStyle(getStyle5());
		}
	}
	
	// add by GuoXY 20161024 for CMCC_集中化持续审计系统_软件需求规格说明书_社会渠道终端套利专题的逻辑和变更说明v1.7 郭倩
	public void writeSheetCommon3001(List<Map<String, Object>> data, String sheetName, String title, String[] columnNames){
		logger.debug("---------data:\n" + data);
		sh = wb.createSheet(sheetName);
		// 第一步：创建sheet页标题行内容、样式、合并单元格
		sh.createRow(0).createCell(0).setCellValue(title);
		sh.getRow(0).getCell(0).setCellStyle(getStyle0());
		sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 4)); 
		sh.createRow(1);
		
		// 第二步：创建标题行每个单元格的内容、宽度、样式、列宽(根据标题内容长度)、行高、锁定标题行
		for (int i = 0; i < columnNames.length; i++) {
			sh.getRow(1).createCell(i).setCellValue(columnNames[i]);
			sh.setColumnWidth(i, 256 * 12);
			sh.getRow(1).getCell(i).setCellStyle(getStyle1());
		}
		sh.setColumnWidth(1, 256 * 14);
		sh.setColumnWidth(3, 256 * 16);
		sh.setColumnWidth(4, 256 * 14);
		sh.getRow(1).setHeightInPoints(32);
		sh.createFreezePane(0, 2, 0, 2);
		
		// 第三步：填充表格内容部分
		int sumWG = 0;
		Map<String,Object> row=new HashMap<String,Object>();
		for(int i = 0,dataSize=data.size(); i <= dataSize; i++) {
			sh.createRow(2+i);
			if (i < dataSize) { // 明细行
				row = data.get(i);
				sh.getRow(2+i).createCell(0).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
				sumWG += (Integer) row.get("tol_num");
				sh.getRow(2+i).createCell(1).setCellValue((Integer) row.get("taoli_num"));
				sh.getRow(2+i).createCell(2).setCellValue(row.get("infraction_sett_amt") instanceof BigDecimal ? ((BigDecimal) row.get("infraction_sett_amt")).doubleValue():(Double) row.get("infraction_sett_amt"));
				sh.getRow(2+i).createCell(3).setCellValue((Integer) row.get("taoli_chnlqty"));
				// 郭倩说将沉默套利终端占比 = 沉默套利终端数量 / 终端销售数量 20161125 modified by GuoXY 
				//sh.getRow(2+i).createCell(4).setCellValue(row.get("taoli_weigui_per_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_weigui_per_cnt")).doubleValue():(Double) row.get("taoli_weigui_per_cnt"));
				double res = (Double) ((0==(Integer) row.get("tol_num")) ? 0 : ((Integer)row.get("taoli_num")).doubleValue() / ((Integer)row.get("tol_num")).doubleValue());
				sh.getRow(2+i).createCell(4).setCellValue( res );
				
				if ((Integer) row.get("taoli_num") != null && (Integer) row.get("taoli_num") == 0){
					sh.getRow(2+i).createCell(5).setCellValue("-");
					sh.getRow(2+i).getCell(5).setCellStyle(getStyle7());
				}else{
					sh.getRow(2+i).createCell(5).setCellValue((Integer) row.get("taoli_order"));
					sh.getRow(2+i).getCell(5).setCellStyle(getStyle6());
				}
			} else { // 合计行
				sh.getRow(2+i).createCell(0).setCellValue("合计");
				sh.getRow(2+i).createCell(1).setCellFormula("SUM(B3:B"+ (i+2) + ")");
				sh.getRow(2+i).createCell(2).setCellFormula("SUM(C3:C"+ (i+2) + ")");
				sh.getRow(2+i).createCell(3).setCellFormula("SUM(D3:D"+ (i+2) + ")");
				sh.getRow(2+i).createCell(4).setCellFormula("B" + (i+3) + "/" + sumWG);
				sh.getRow(2+i).createCell(5);
				sh.getRow(2+i).getCell(5).setCellStyle(getStyle3());
			}
			// 设置每个单元格的文字格式
			sh.getRow(2+i).getCell(0).setCellStyle(getStyle3());
			sh.getRow(2+i).getCell(1).setCellStyle(getStyle6());
			sh.getRow(2+i).getCell(2).setCellStyle(getStyle5());
			sh.getRow(2+i).getCell(3).setCellStyle(getStyle6());
			sh.getRow(2+i).getCell(4).setCellStyle(getStyle4());		
		}
	}
	
	// add by PXL 20161228 for CMCC_集中化持续审计系统_软件需求规格说明书_社会渠道终端套利专题的逻辑和变更说明v2.0 郭倩
		public void writeSheetCommon3002(List<Map<String, Object>> data, String sheetName, String title, String[] columnNames){
			logger.debug("---------data:\n" + data);
			sh = wb.createSheet(sheetName);
			// 第一步：创建sheet页标题行内容、样式、合并单元格
			sh.createRow(0).createCell(0).setCellValue(title);
			sh.getRow(0).getCell(0).setCellStyle(getStyle0());
			sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 4)); 
			sh.createRow(1);
			
			// 第二步：创建标题行每个单元格的内容、宽度、样式、列宽(根据标题内容长度)、行高、锁定标题行
			for (int i = 0; i < columnNames.length; i++) {
				sh.getRow(1).createCell(i).setCellValue(columnNames[i]);
				sh.setColumnWidth(i, 256 * 12);
				sh.getRow(1).getCell(i).setCellStyle(getStyle1());
			}
			sh.setColumnWidth(1, 256 * 14);
			sh.setColumnWidth(3, 256 * 16);
			sh.setColumnWidth(4, 256 * 14);
			sh.getRow(1).setHeightInPoints(32);
			sh.createFreezePane(0, 2, 0, 2);
			
			// 第三步：填充表格内容部分
			int sumWG = 0;
			Map<String,Object> row=new HashMap<String,Object>();
			for(int i = 0,dataSize=data.size(); i <= dataSize; i++) {
				sh.createRow(2+i);
				if (i < dataSize) { // 明细行
					row = data.get(i);
					sh.getRow(2+i).createCell(0).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
					sumWG += (Integer) row.get("tol_num");
					sh.getRow(2+i).createCell(1).setCellValue((Integer) row.get("taoli_num"));
					sh.getRow(2+i).createCell(2).setCellValue(row.get("infraction_sett_amt") instanceof BigDecimal ? ((BigDecimal) row.get("infraction_sett_amt")).doubleValue():(Double) row.get("infraction_sett_amt"));
					sh.getRow(2+i).createCell(3).setCellValue((Integer) row.get("taoli_chnlqty"));
					// 郭倩说养机套利终端占总销量比=养机套利终端数量/终端总销售数量 20161228 modified by PXL
					//sh.getRow(2+i).createCell(4).setCellValue(row.get("taoli_weigui_per_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_weigui_per_cnt")).doubleValue():(Double) row.get("taoli_weigui_per_cnt"));
					double res = (Double) ((0==(Integer) row.get("tol_num")) ? 0 : ((Integer)row.get("taoli_num")).doubleValue() / ((Integer)row.get("tol_num")).doubleValue());
					sh.getRow(2+i).createCell(4).setCellValue( res );
					
					if ((Integer) row.get("taoli_num") != null && (Integer) row.get("taoli_num") == 0){
						sh.getRow(2+i).createCell(5).setCellValue("-");
						sh.getRow(2+i).getCell(5).setCellStyle(getStyle7());
					}else{
						sh.getRow(2+i).createCell(5).setCellValue((Integer) row.get("taoli_order"));
						sh.getRow(2+i).getCell(5).setCellStyle(getStyle6());
					}
				} else { // 合计行
					sh.getRow(2+i).createCell(0).setCellValue("合计");
					sh.getRow(2+i).createCell(1).setCellFormula("SUM(B3:B"+ (i+2) + ")");
					sh.getRow(2+i).createCell(2).setCellFormula("SUM(C3:C"+ (i+2) + ")");
					sh.getRow(2+i).createCell(3).setCellFormula("SUM(D3:D"+ (i+2) + ")");
					sh.getRow(2+i).createCell(4).setCellFormula("B" + (i+3) + "/" + sumWG);
					sh.getRow(2+i).createCell(5);
					sh.getRow(2+i).getCell(5).setCellStyle(getStyle3());
				}
				// 设置每个单元格的文字格式
				sh.getRow(2+i).getCell(0).setCellStyle(getStyle3());
				sh.getRow(2+i).getCell(1).setCellStyle(getStyle6());
				sh.getRow(2+i).getCell(2).setCellStyle(getStyle5());
				sh.getRow(2+i).getCell(3).setCellStyle(getStyle6());
				sh.getRow(2+i).getCell(4).setCellStyle(getStyle4());		
			}
		}
	
	public void writeSheet3001(List<Map<String, Object>> data){
		// modified by GuoXY 20161024 for CMCC_集中化持续审计系统_软件需求规格说明书_社会渠道终端套利专题的逻辑和变更说明v1.7 郭倩
//		String[] columnNames = {"省份名称","沉默终端数量","沉默终端涉及渠道","沉默套利终端数量","套利金额","沉默套利涉及渠道数量","沉默终端占总销量比","沉默套利终端占沉默终端比","沉默终端占比排名","沉默套利终端占比排名"};
//		writeSheetCommon(data, "沉默终端套利", "社会渠道沉默终端及套利情况", columnNames);
		String[] columnNames = {"省份名称",                         "沉默套利终端数量","套利金额","沉默套利涉及渠道数量",               "沉默套利终端占总销量比",             "沉默套利终端占比排名"};
		writeSheetCommon3001(data, "沉默终端套利", "社会渠道沉默终端套利情况", columnNames);
	}
	
	public void writeSheet3002(List<Map<String, Object>> data){
		//String[] columnNames = {"省份名称","养机数量","养机涉及渠道数量","养机套利终端数量","套利金额","养机套利涉及渠道数量","养机占总销量比","养机套利终端占养机终端比","养机占比排名","养机套利终端占比排名"};
		//writeSheetCommon(data, "养机套利", "社会渠道养机及套利情况", columnNames);
		String[] columnNames = {"省份名称",                          "养机套利终端数量","套利金额","养机套利涉及渠道数量",               "养机套利终端占总销量比",        "养机套利终端占比排名"};
		writeSheetCommon3002(data, "养机套利", "社会渠道养机套利情况", columnNames);
	}
	
	public void writeSheet3004(List<Map<String, Object>> data){
		String[] columnNames = {"省份名称","拆包数量","拆包涉及渠道数量","拆包套利终端数量","	套利金额","拆包套利涉及渠道数量","拆包占总销量比","拆包套利终端占拆包终端比","终端拆包占比排名","拆包套利终端占比排名"};
		writeSheetCommon(data, "拆包套利", "社会渠道终端拆包及套利情况", columnNames);
	}
	
	public void writeSheet3005(List<Map<String, Object>> data){
		String[] columnNames = {"省份名称","跨省窜货数量","跨省窜货渠道数量","跨省窜货套利终端数量","套利金额","跨省窜货套利渠道数量","跨省窜货占总销量比","跨省窜货套利终端占跨省窜货终端比","终端跨省窜货占比排名","跨省窜货套利终端占比排名"};
		writeSheetCommon(data, "跨省窜货套利", "社会渠道终端跨省窜货及套利情况", columnNames);
	}
	
	private CellStyle getStyle0(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_CENTER);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)16);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		style.setFont(font);
		return style;
	}
	
	private CellStyle getStyle1(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_CENTER);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setWrapText(true); 
		//style.setFillBackgroundColor(HSSFColor.BLUE.index);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return style;
	}
	
	private CellStyle getStyle2(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_CENTER);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setWrapText(true); 
		//style.setFillBackgroundColor(HSSFColor.BLUE.index);
		style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return style;
	}
	// modified by GuoXY 20161024 for  CMCC_集中化持续审计系统_软件需求规格说明书_社会渠道终端套利专题的逻辑和变更说明v1.7 郭倩
	private CellStyle getStyle7(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_CENTER);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		style.setWrapText(true);
		return style;
	}
	private CellStyle getStyle3(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_CENTER);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setWrapText(true);
		return style;
	}

	private CellStyle getStyle4(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_CENTER);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
		return style0;
	}
	
	private CellStyle getStyle5(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_CENTER);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setWrapText(true);
		style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
		return style;
	}
	
	private CellStyle getStyle6(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_CENTER);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
		return style0;
	}

	public String getFocusCd() {
		return focusCd;
	}

	public void setFocusCd(String focusCd) {
		this.focusCd = focusCd;
	}
	
	
}
