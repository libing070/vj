package com.hpe.cmca.job;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service("NotiFileYjk1000SumIllegalProcessor")
public class NotiFileYjk1000SumIllegalProcessor extends AbstractNotiFileProcessor{

	protected String focusCd = "1000";
	
	public boolean generate() throws Exception{
		this.setFileName("有价卡管理违规统计");
		writeSheet(notiFileGenService.getNotiFile1000DataSumIllegal(month, "1001"),
				notiFileGenService.getNotiFile1000DataSumIllegal(month, "1002"),
				notiFileGenService.getNotiFile1000DataSumIllegal(month, "1003"),
				notiFileGenService.getNotiFile1000DataSumIllegal(month, "1004"),
				notiFileGenService.getNotiFile1000DataSumIllegal(month, "1005"));
		return true;
	}
	
	public void writeSheet(List<Map<String, Object>> data1, List<Map<String, Object>> data2, List<Map<String, Object>> data3, List<Map<String, Object>> data4, List<Map<String, Object>> data5) {
		sh = wb.createSheet("sheet1"); 
		sh.createRow(0).createCell(0).setCellValue("有价卡管理违规统计");
		sh.getRow(0).getCell(0).setCellStyle(getStyle0());
		
		sh.createRow(1);
		sh.getRow(1).createCell(0).setCellValue("违规情形");
		sh.getRow(1).createCell(1).setCellValue("1）有价卡生成数据未在BOSS系统和VC系统同步加载");
		sh.getRow(1).createCell(3).setCellValue("2）有价卡销售激活在BOSS系统和VC系统不同步");
		sh.getRow(1).createCell(5).setCellValue("3）有价卡销售前已被充值");
		sh.getRow(1).createCell(7).setCellValue("4）有价卡报废未在VC系统锁定");
		sh.getRow(1).createCell(9).setCellValue("5）有价卡已充值但未在VC系统标记“已充值”状态");
		
		sh.createRow(2);
		sh.getRow(2).createCell(0).setCellValue("省份");
		sh.getRow(2).createCell(1).setCellValue("金额(元)");
		sh.getRow(2).createCell(2).setCellValue("数量(张)");
		sh.getRow(2).createCell(3).setCellValue("金额(元)");		
		sh.getRow(2).createCell(4).setCellValue("数量(张)");
		sh.getRow(2).createCell(5).setCellValue("金额(元)");		
		sh.getRow(2).createCell(6).setCellValue("数量(张)");
		sh.getRow(2).createCell(7).setCellValue("金额(元)");		
		sh.getRow(2).createCell(8).setCellValue("数量(张)");
		sh.getRow(2).createCell(9).setCellValue("金额(元)");
		sh.getRow(2).createCell(10).setCellValue("数量(张)");
		
																
		sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 10)); 
		sh.addMergedRegion(new CellRangeAddress(1, 1, 1, 2)); 
		sh.addMergedRegion(new CellRangeAddress(1, 1, 3, 4)); 
		sh.addMergedRegion(new CellRangeAddress(1, 1, 5, 6)); 
		sh.addMergedRegion(new CellRangeAddress(1, 1, 7, 8)); 
		sh.addMergedRegion(new CellRangeAddress(1, 1, 9, 10)); 

		for(int i=1;i<=2;i++){
			for(int j=0;j<=10;j++){
				if (null == sh.getRow(i).getCell(j)) {
					sh.getRow(i).createCell(j);
				}
				sh.getRow(i).getCell(j).setCellStyle(getStyle1());
			}
		}
		
		for(int i = 0 ; i <= 10; i++) sh.setColumnWidth(i, 256 * 16);
		
		sh.getRow(1).setHeightInPoints(35);
		
		Map<String,Object> row=new HashMap<String,Object>();
		for(int i = 0,dataSize=data1.size(); i < dataSize; i++) {
			row = data1.get(i);
			sh.createRow(3+i);
			sh.getRow(3+i).createCell(0).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
			sh.getRow(3+i).getCell(0).setCellStyle(getStyle2());

			if (row.size() == 1) {
				sh.getRow(3+i).createCell(1).setCellValue("未发生");
				sh.getRow(3+i).createCell(2).setCellValue("未发生");
			}else{
				sh.getRow(3+i).createCell(1).setCellValue(row.get("amt1") instanceof BigDecimal ? ((BigDecimal) row.get("amt1")).longValue():(Long) row.get("amt1"));
				sh.getRow(3+i).createCell(2).setCellValue(row.get("cnt1") instanceof BigDecimal ? ((BigDecimal) row.get("cnt1")).intValue():(Integer) row.get("cnt1"));
			}
			sh.getRow(3+i).getCell(1).setCellStyle(getStyle5());
			sh.getRow(3+i).getCell(2).setCellStyle(getStyle5());
			writeCustomData((String) row.get("CMCC_prov_prvd_nm"), data2, 3+i, 3);
			writeCustomData((String) row.get("CMCC_prov_prvd_nm"), data3, 3+i, 5);
			writeCustomData((String) row.get("CMCC_prov_prvd_nm"), data4, 3+i, 7);
			writeCustomData((String) row.get("CMCC_prov_prvd_nm"), data5, 3+i, 9);
		}

	}
	
	private void writeCustomData(String prvdName, List<Map<String, Object>> data, int rowNum, int colNum) {
		for(Map<String, Object> map : data) {
			if (!prvdName.equals((String)map.get("CMCC_prov_prvd_nm"))) continue;
			if (map.size() == 1) {
				sh.getRow(rowNum).createCell(colNum).setCellValue("未发生");
				sh.getRow(rowNum).createCell(colNum+1).setCellValue("未发生");
			}else{
				sh.getRow(rowNum).createCell(colNum).setCellValue(map.get("amt1") instanceof BigDecimal ? ((BigDecimal) map.get("amt1")).longValue():(Long) map.get("amt1"));
				sh.getRow(rowNum).createCell(colNum+1).setCellValue(map.get("cnt1") instanceof BigDecimal ? ((BigDecimal) map.get("cnt1")).intValue():(Integer) map.get("cnt1"));
			}
			sh.getRow(rowNum).getCell(colNum).setCellStyle(getStyle5());
			sh.getRow(rowNum).getCell(colNum+1).setCellStyle(getStyle5());
			break;
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
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		style.setFont(font);
		style.setWrapText(true); 
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
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
		return style;
	}
	
	private CellStyle getStyle3(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_CENTER);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
		return style;
	}
	
	private CellStyle getStyle4(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_CENTER);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
		return style;
	}
	
	private CellStyle getStyle5(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_RIGHT);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12); 
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
		return style;
	}
	
	private CellStyle getStyle6(){
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
		style.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
		return style;
	}
	
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
		return style;
	}
}
