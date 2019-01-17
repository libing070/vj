package com.hpe.cmca.job;

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
import org.springframework.stereotype.Service;


@Service("NotiFileKhqf4000SegmProcessor")
public class NotiFileKhqf4000SegmProcessor extends AbstractNotiFileProcessor {
	protected Logger logger = Logger.getLogger(this.getClass());
	public boolean generate() throws Exception{
		this.setFileName("客户欠费账龄金额阶段统计");
		writeSheet1(notiFileGenService.selGRAge(month));
		writeSheet2(notiFileGenService.selGRAmt(month));
		writeSheet3(notiFileGenService.selJTAge(month));
		writeSheet4(notiFileGenService.selJTAmt(month));
		return true;
	}
	
	public void writeSheet1(List<Map<String, Object>> data){
		String[] columnNames = {"省份名称","异常欠费用户数量","异常欠费金额（元）","数量排名","金额排名","7至12个月","","13至24个月","","24个月以上",""};
		String[] columnNames1 = {"","","","","","数量","金额（元）","数量","金额（元）","数量","金额（元）"};
		writeSheetCommon(data, "个人欠费账龄分布", columnNames,columnNames1);
	}
	public void writeSheet2(List<Map<String, Object>> data){

		String[] columnNames = {"省份名称","异常欠费用户数量","异常欠费金额（元）","数量排名","金额排名","200至1000","","1000至5000","","5000以上",""};
		String[] columnNames1 = {"","","","","","数量","金额（元）","数量","金额（元）","数量","金额（元）"};
		writeSheetCommon(data, "个人欠费金额分布",columnNames,columnNames1);
	}
	
	public void writeSheet3(List<Map<String, Object>> data){
		String[] columnNames = {"省份名称","异常欠费用户数量","异常欠费金额（元）","数量排名","金额排名","13至18个月","","19至24个月","","24个月以上",""};
		String[] columnNames1 = {"","","","","","数量","金额（元）","数量","金额（元）","数量","金额（元）"};
		writeSheetCommon(data, "集团欠费账龄分布", columnNames,columnNames1);
	}
	
	public void writeSheet4(List<Map<String, Object>> data){
		String[] columnNames = {"省份名称","异常欠费用户数量","异常欠费金额（元）","数量排名","金额排名","10000至50000","","50000至100000","","100000以上",""};
		String[] columnNames1 = {"","","","","","数量","金额（元）","数量","金额（元）","数量","金额（元）"};
		writeSheetCommon(data, "集团欠费金额分布", columnNames,columnNames1);
	}
	


	public void writeSheetCommon(List<Map<String, Object>> data, String sheetName, String[] columnNames, String[] columnNames1){
		logger.debug("---------data:\n" + data);
		sh = wb.createSheet(sheetName);
		//sh.createRow(0).createCell(0).setCellValue(title);
		//sh.getRow(0).getCell(0).setCellStyle(getStyle0());
		//sh.addMergedRegion(new CellRangeAddress(0, 0, 0, columnNames.length-1)); 
		sh.createRow(0);
		sh.createRow(1);
		for (int i = 0; i < columnNames.length; i++) {
			sh.getRow(0).createCell(i).setCellValue(columnNames[i]);
			sh.setColumnWidth(i, 256 * 12);			
			sh.getRow(0).getCell(i).setCellStyle(getStyle1());	
		}
		
		for (int i = 0; i < columnNames1.length; i++) {
			sh.getRow(1).createCell(i).setCellValue(columnNames1[i]);
			sh.setColumnWidth(i, 256 * 12);			
			sh.getRow(1).getCell(i).setCellStyle(getStyle1());	
		}
		sh.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sh.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
		sh.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
		sh.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
		sh.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));		
		sh.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 9, 10));
		
		//sh.getRow(0).setHeightInPoints(32);
		//sh.getRow(1).setHeightInPoints(32);
		sh.createFreezePane(0, 2, 0, 2);
		//int tolNum = 0;
		Map<String, Object> row = new HashMap<String, Object>();
		for(int i = 0,dataSize=data.size(); i <= dataSize; i++) {
			sh.createRow(2+i);
			if (i < dataSize) {
				row = data.get(i);
				if(row.get("CMCC_prov_prvd_nm")!=null){
					sh.getRow(2+i).createCell(0).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
					sh.getRow(2+i).getCell(0).setCellStyle(getStyle3());
				}else{
					sh.getRow(2+i).createCell(0).setCellValue("-");
					sh.getRow(2+i).getCell(0).setCellStyle(getStyle3());
				}

				
				if(row.get("cust_num")!=null){
					sh.getRow(2+i).createCell(1).setCellValue((Integer) row.get("cust_num"));
					sh.getRow(2+i).getCell(1).setCellStyle(getStyle6());
				}else{
					sh.getRow(2+i).createCell(1).setCellValue("-");
					sh.getRow(2+i).getCell(1).setCellStyle(getStyle3());
				}

				if(row.get("dbt_amt_num")!=null){
					sh.getRow(2+i).createCell(2).setCellValue(row.get("dbt_amt_num") instanceof BigDecimal ? ((BigDecimal) row.get("dbt_amt_num")).doubleValue():(Double) row.get("dbt_amt_num"));			
					sh.getRow(2+i).getCell(2).setCellStyle(getStyle5());
				}else{
					sh.getRow(2+i).createCell(2).setCellValue("-");			
					sh.getRow(2+i).getCell(2).setCellStyle(getStyle3());
				}
				
				if(row.get("num_rank")!=null){
					sh.getRow(2+i).createCell(3).setCellValue((Integer) row.get("num_rank"));
					sh.getRow(2+i).getCell(3).setCellStyle(getStyle6());
				}else{
					sh.getRow(2+i).createCell(3).setCellValue("-");
					sh.getRow(2+i).getCell(3).setCellStyle(getStyle3());
				}

				if(row.get("amt_rank")!=null){
					sh.getRow(2+i).createCell(4).setCellValue((Integer) row.get("amt_rank"));
					sh.getRow(2+i).getCell(4).setCellStyle(getStyle6());
				}else{
					sh.getRow(2+i).createCell(4).setCellValue("-");
					sh.getRow(2+i).getCell(4).setCellStyle(getStyle3());
				}

				if(row.get("num1")!=null){
					sh.getRow(2+i).createCell(5).setCellValue((Integer) row.get("num1"));
					sh.getRow(2+i).getCell(5).setCellStyle(getStyle6());
				}else{
					sh.getRow(2+i).createCell(5).setCellValue("-");
					sh.getRow(2+i).getCell(5).setCellStyle(getStyle3());
				}
			
				if(row.get("amt1")!=null){
					sh.getRow(2+i).createCell(6).setCellValue(row.get("amt1") instanceof BigDecimal ? ((BigDecimal) row.get("amt1")).doubleValue():(Double) row.get("amt1"));			
					sh.getRow(2+i).getCell(6).setCellStyle(getStyle5());
				}else{
					sh.getRow(2+i).createCell(6).setCellValue("-");
					sh.getRow(2+i).getCell(6).setCellStyle(getStyle3());
				}
				
				if(row.get("num2")!=null){
					sh.getRow(2+i).createCell(7).setCellValue((Integer) row.get("num2"));
					sh.getRow(2+i).getCell(7).setCellStyle(getStyle6());
				}else{
					sh.getRow(2+i).createCell(7).setCellValue("-");
					sh.getRow(2+i).getCell(7).setCellStyle(getStyle3());
				}
				
				if(row.get("amt2")!=null){
					sh.getRow(2+i).createCell(8).setCellValue(row.get("amt2") instanceof BigDecimal ? ((BigDecimal) row.get("amt2")).doubleValue():(Double) row.get("amt3"));			
					sh.getRow(2+i).getCell(8).setCellStyle(getStyle5());
				}else{
					sh.getRow(2+i).createCell(8).setCellValue("-");
					sh.getRow(2+i).getCell(8).setCellStyle(getStyle3());
				}
				
				if(row.get("num3")!=null){
					sh.getRow(2+i).createCell(9).setCellValue((Integer) row.get("num3"));
					sh.getRow(2+i).getCell(9).setCellStyle(getStyle6());
				}else{
					sh.getRow(2+i).createCell(9).setCellValue("-");
					sh.getRow(2+i).getCell(9).setCellStyle(getStyle3());
				}
			
				if(row.get("amt3")!=null){
					sh.getRow(2+i).createCell(10).setCellValue(row.get("amt3") instanceof BigDecimal ? ((BigDecimal) row.get("amt3")).doubleValue():(Double) row.get("amt3"));			
					sh.getRow(2+i).getCell(10).setCellStyle(getStyle5());
				}else{
					sh.getRow(2+i).createCell(10).setCellValue("-");
					sh.getRow(2+i).getCell(10).setCellStyle(getStyle3());
				}

			}
		}
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
}
