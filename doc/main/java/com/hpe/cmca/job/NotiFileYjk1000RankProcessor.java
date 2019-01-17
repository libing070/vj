package com.hpe.cmca.job;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service("NotiFileYjk1000RankProcessor")
public class NotiFileYjk1000RankProcessor extends AbstractNotiFileProcessor{
	
	protected String focusCd = "1000";
	
	public boolean generate() throws Exception{
		this.setFileName("有价卡违规排名");
		writeSheet1001(notiFileGenService.getNotiFile1000DataRank(month, "1001"));
		writeSheet1002(notiFileGenService.getNotiFile1000DataRank(month, "1002"));
		writeSheet1003(notiFileGenService.getNotiFile1000DataRank(month, "1003"));
		writeSheet1004(notiFileGenService.getNotiFile1000DataRank(month, "1004"));
		writeSheet1005(notiFileGenService.getNotiFile1000DataRank(month, "1005"));
		return true;
	}
	
	public void writeSheetCommon(String sheetName, String typeName, String fieldName, List<Map<String, Object>>data) {
		sh = wb.createSheet(sheetName); 
		sh.createRow(3).createCell(1).setCellValue(typeName);
		sh.createRow(0).createCell(1).setCellValue("有价卡违规数量占比排名");
		sh.createRow(1);
		sh.getRow(1).createCell(1).setCellValue("有价卡违规类型");
		sh.getRow(1).createCell(2).setCellValue("月份");
		sh.getRow(1).createCell(4).setCellValue(month.substring(4)+"月");
		sh.createRow(2);
		sh.getRow(2).createCell(2).setCellValue("排名");
		sh.getRow(2).createCell(3).setCellValue("省份名称");
		sh.getRow(2).createCell(4).setCellValue(fieldName+"总数量");
		sh.getRow(2).createCell(5).setCellValue("有价卡违规数量");
		sh.getRow(2).createCell(6).setCellValue("有价卡违规数量占比");
		sh.getRow(2).createCell(7).setCellValue(fieldName+"总金额");
		sh.getRow(2).createCell(8).setCellValue("有价卡违规金额");
		sh.getRow(2).createCell(9).setCellValue("有价卡违规金额占比");
		sh.getRow(2).createCell(10).setCellValue("备注");
																
		sh.addMergedRegion(new CellRangeAddress(0, 0, 1, 14)); 
		sh.addMergedRegion(new CellRangeAddress(1, 1, 2, 3)); 
		sh.addMergedRegion(new CellRangeAddress(1, 1, 4, 14)); 
		sh.addMergedRegion(new CellRangeAddress(2, 2, 10, 14));
		sh.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
		sh.addMergedRegion(new CellRangeAddress(3, 2+data.size(), 1, 1));
		
		sh.getRow(0).getCell(1).setCellStyle(getStyle0());
		for(int i=1;i<=2;i++){
			for(int j=1;j<=14;j++){
				if (null == sh.getRow(i).getCell(j)) {
					sh.getRow(i).createCell(j);
				}
				sh.getRow(i).getCell(j).setCellStyle(getStyle1());
			}
		}
		
		for(int i = 1 ; i < 14; i++) sh.setColumnWidth(i, 256 * 16);
		sh.setColumnWidth(14, 256 * 24);
		
		sh.setColumnWidth(1, 256 * 18);
		sh.getRow(2).setHeightInPoints(45);
		
		for(int i = 0; i < data.size(); i++) {
			if (i>0) sh.createRow(3+i);
			Map<String, Object> row = data.get(i);
			sh.getRow(3+i).createCell(2).setCellValue(i+1);
			sh.getRow(3+i).createCell(3).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
			if (row.size() < 7) {
				for(int j=4;j<=9;j++) {
					sh.getRow(3+i).createCell(j).setCellValue("NA");
					sh.getRow(3+i).getCell(j).setCellStyle(getStyle7());
				}
				sh.getRow(3+i).getCell(2).setCellStyle(getStyle5());
				sh.getRow(3+i).getCell(3).setCellStyle(getStyle5());
			}else{
				//System.out.println(row);
				sh.getRow(3+i).createCell(4).setCellValue((Integer) row.get("t_cnt1"));
				sh.getRow(3+i).createCell(5).setCellValue((Integer) row.get("cnt1"));
				sh.getRow(3+i).createCell(6).setCellValue(row.get("per_cnt1") instanceof BigDecimal ? ((BigDecimal) row.get("per_cnt1")).doubleValue():(Double) row.get("per_cnt1"));
				sh.getRow(3+i).createCell(7).setCellValue(row.get("t_amt1") instanceof BigDecimal ? ((BigDecimal) row.get("t_amt1")).longValue():(Long) row.get("amt1"));
				sh.getRow(3+i).createCell(8).setCellValue(row.get("amt1") instanceof BigDecimal ? ((BigDecimal) row.get("amt1")).longValue():(Long) row.get("amt1"));
				sh.getRow(3+i).createCell(9).setCellValue(row.get("per_amt1") instanceof BigDecimal ? ((BigDecimal) row.get("per_amt1")).doubleValue():(Double) row.get("per_amt1"));
				
				for(int j=2;j<=9;j++) {
					if(j==6 || j == 9) {
						sh.getRow(3+i).getCell(j).setCellStyle(getStyle6());
					}else {
						sh.getRow(3+i).getCell(j).setCellStyle(getStyle5());
					}
				}
				
			}
			sh.getRow(3+i).createCell(10).setCellStyle(getStyle7());
			if (null != row.get("infraction_typ_name")) {
				sh.getRow(3+i).getCell(10).setCellValue((String) row.get("infraction_typ_name"));
				
			}
			sh.addMergedRegion(new CellRangeAddress(3+i, 3+i, 10, 14));
			for(int j=11;j<=14;j++) {
				sh.getRow(3+i).createCell(j).setCellStyle(getStyle2());
			}
			

		}
		
		for(int i=3;i<=2+data.size();i++) {
			if (null == sh.getRow(i).getCell(1)) {
				sh.getRow(i).createCell(1);
			}
			sh.getRow(i).getCell(1).setCellStyle(getStyle1());
		}
	}
	
	public void writeSheet1001(List<Map<String, Object>>data) {
		writeSheetCommon("未按规定在系统间同步加载","未按规定在系统间同步加载\n(有价卡数据未同步加载到BOSS系统或VC系统)", "CRM和VC系统有价卡生成状态",data);
	}
	
	public void writeSheet1002(List<Map<String, Object>>data) {
		writeSheetCommon("违规激活","违规激活\n（有价卡已销售但VC系统未激活，或者VC系统中的激活时间早于销售时间）", "CRM和VC系统有价卡激活状态",data);
	}
	
	public void writeSheet1003(List<Map<String, Object>>data) {
		writeSheetCommon("违规销售","违规销售\n（有价卡未销售，但已充值，或者有价卡充值时间早于销售时间）", "VC系统有价卡已使用状态",data);
	}
	
	public void writeSheet1004(List<Map<String, Object>>data) {
		writeSheetCommon("退换后的坏卡或报废卡未封锁","退换后的坏卡或报废卡未封锁\n（退换后的坏卡或报废卡未及时在系统中同步封锁状态）", "CRM有价卡锁定、清退状态",data);
	}
	
	public void writeSheet1005(List<Map<String, Object>>data) {
		writeSheetCommon("违规重复使用","违规重复使用\n（有价卡被多次充值，或者有价卡充值后仍可充值）","CRM有价卡已使用状态", data);
	}
	
	private CellStyle getStyle0(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_CENTER);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
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
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
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
