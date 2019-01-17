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

@Service("NotiFileYjk1000SumProcessor")
public class NotiFileYjk1000SumProcessor extends AbstractNotiFileProcessor{
	
	private Integer rNum = 0;
	
	protected String focusCd = "1000";
	
	public boolean generate() throws Exception{
		sh = wb.createSheet("有价卡违规汇总-"+month); 
		writeSheetSum();
		
		List<Map<String, Object>> amtData = notiFileGenService.getNotiFile1000DataSumAmt(month);
		writeSheetAmt(amtData);		
		
		List<Map<String, Object>> qtyData = notiFileGenService.getNotiFile1000DataSumQty(month);
		writeSheetQty(qtyData);
		this.setFileName("有价卡违规排名");
		for(int i = 0 ; i < 7; i++) {
			sh.setColumnWidth(i, 256 * 17);
		}
		sh.setColumnWidth(3, 256 * 20);
		
		//将有价卡排名合并到有价卡汇总中，作为一个excel的不同sheet页 addBy pxl  20170721
		writeSheet1001(notiFileGenService.getNotiFile1000DataRank(month, "1001"));
		writeSheet1002(notiFileGenService.getNotiFile1000DataRank(month, "1002"));
		writeSheet1003(notiFileGenService.getNotiFile1000DataRank(month, "1003"));
		writeSheet1004(notiFileGenService.getNotiFile1000DataRank(month, "1004"));
		writeSheet1005(notiFileGenService.getNotiFile1000DataRank(month, "1005"));
		
		return true;
	}
	
	public void writeSheetSum() {
		sh.createRow(0).createCell(0).setCellValue("“有价卡违规”持续审计结果摘要");
		sh.getRow(0).getCell(0).setCellStyle(getStyle0());
		sh.createRow(2).createCell(0).setCellValue("总体情况");
		sh.getRow(2).getCell(0).setCellStyle(getStyle1());
		sh.createRow(3).createCell(1).setCellValue("月份");
		sh.createRow(4).createCell(1).setCellValue("有价卡总金额(元)");
		sh.createRow(5).createCell(1).setCellValue("有价卡违规金额(元)");
		sh.createRow(6).createCell(1).setCellValue("有价卡违规金额占比");
		sh.createRow(7).createCell(1).setCellValue("有价卡总数量(张)");
		sh.createRow(8).createCell(1).setCellValue("有价卡违规量(张)");
		sh.createRow(9).createCell(1).setCellValue("有价卡违规量占比");
		
		for(int i=3;i<10;i++) {
			sh.getRow(i).getCell(1).setCellStyle(getStyle2());
			sh.getRow(i).createCell(2).setCellStyle(getStyle2());
			sh.addMergedRegion(new CellRangeAddress(i, i, 1, 2)); 
			if (i == 6 || i == 9) {
				sh.getRow(i).createCell(3).setCellStyle(getStyle4());
			} else {
				sh.getRow(i).createCell(3).setCellStyle(getStyle3());
			}
		}

		sh.getRow(3).createCell(3).setCellValue(month.substring(4)+"月");
		sh.getRow(3).getCell(3).setCellStyle(getStyle2());
		sh.createRow(10).createCell(0).setCellValue("注：当月发生状态变化的有价卡（下同）");
		sh.getRow(10).getCell(0).setCellStyle(getStyle1());
		sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 6)); 
		sh.addMergedRegion(new CellRangeAddress(2, 2, 0, 6)); 
		sh.addMergedRegion(new CellRangeAddress(10, 10, 0, 6)); 
		sh.getRow(0).setHeightInPoints(32);
		sh.getRow(2).setHeightInPoints(32);
	}
	
	public void writeSheetQty(List<Map<String, Object>> data){
		rNum = rNum+2;
		sh.createRow(rNum).createCell(0).setCellValue("31省违规数量占比概况(先按违规量占比逆序，再按照违规量逆序)");
		sh.getRow(rNum).getCell(0).setCellStyle(getStyle1());
		sh.addMergedRegion(new CellRangeAddress(rNum, rNum, 0, 6)); 
		sh.createRow(rNum+1);
		sh.createRow(rNum+2);			

		sh.getRow(rNum+1).createCell(1).setCellValue("月份");
		sh.getRow(rNum+1).createCell(3).setCellValue(month.substring(4)+"月");
		sh.getRow(rNum+2).createCell(1).setCellValue("排名");
		sh.getRow(rNum+2).createCell(2).setCellValue("省份");
		sh.getRow(rNum+2).createCell(3).setCellValue("有价卡总数量(张)");
		sh.getRow(rNum+2).createCell(4).setCellValue("有价卡违规量(张)");
		sh.getRow(rNum+2).createCell(5).setCellValue("有价卡违规数量占比");
		sh.getRow(rNum+2).createCell(6).setCellValue("有价卡违规数量环比增幅%");

		sh.addMergedRegion(new CellRangeAddress(rNum+1, rNum+1, 1, 2)); 
		sh.addMergedRegion(new CellRangeAddress(rNum+1, rNum+1, 3, 6)); 
		sh.getRow(rNum+2).setHeightInPoints(32);
		
		for(int i=1;i<7; i++) {
			sh.getRow(rNum+2).getCell(i).setCellStyle(getStyle2());
			if (null == sh.getRow(rNum+1).getCell(i)) {
				sh.getRow(rNum+1).createCell(i).setCellStyle(getStyle2());
			} else {
				sh.getRow(rNum+1).getCell(i).setCellStyle(getStyle2());
			}
		}
		
		Map<String,Object> row=new HashMap<String,Object>();
		for(int i = 0,dataSize=data.size(); i < dataSize; i++) {
			sh.createRow(rNum+3+i);
			row = data.get(i);
			sh.getRow(rNum+3+i).createCell(1).setCellValue(i+1);
			sh.getRow(rNum+3+i).createCell(2).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
			if (row.size() == 1) {
				for(int j=3;j<7;j++) {
					sh.getRow(rNum+3+i).createCell(j).setCellValue("NA");
					sh.getRow(rNum+3+i).getCell(j).setCellStyle(getStyle7());
				}
				sh.getRow(rNum+3+i).getCell(1).setCellStyle(getStyle5());
				sh.getRow(rNum+3+i).getCell(2).setCellStyle(getStyle5());
			} else {
				for(int j=1;j<7;j++) {
					if (null == sh.getRow(rNum+3+i).getCell(j)) {
						sh.getRow(rNum+3+i).createCell(j);
					}
					if(j>4) {
						sh.getRow(rNum+3+i).getCell(j).setCellStyle(getStyle6());
					}else {
						sh.getRow(rNum+3+i).getCell(j).setCellStyle(getStyle5());
					}
				}
				
				sh.getRow(rNum+3+i).getCell(3).setCellValue((Integer) row.get("t_cnt1"));
				sh.getRow(rNum+3+i).getCell(4).setCellValue((Integer)row.get("cnt1"));
			
				sh.getRow(rNum+3+i).getCell(5).setCellValue(row.get("per_cnt1") instanceof BigDecimal ? ((BigDecimal) row.get("per_cnt1")).doubleValue():(Double) row.get("per_cnt1"));
				if (null != row.get("id_pec")){
					sh.getRow(rNum+3+i).getCell(6).setCellValue((Double)row.get("id_pec"));
				} else {
					sh.getRow(rNum+3+i).getCell(6).setCellValue("NA");
					sh.getRow(rNum+3+i).getCell(6).setCellStyle(getStyle7());
				}

			}
		}
		sh.getRow(7).getCell(3).setCellFormula("SUM(D"+(rNum+4)+":D"+(rNum+3+data.size())+")");
		sh.getRow(8).getCell(3).setCellFormula("SUM(E"+(rNum+4)+":E"+(rNum+3+data.size())+")");
		sh.getRow(9).getCell(3).setCellFormula("D9/D8");
	}
	
	public void writeSheetAmt(List<Map<String, Object>> data){
		rNum = 13;
		sh.createRow(rNum).createCell(0).setCellValue("31省违规金额占比概况(先按违规金额占比逆序，再按照违规金额逆序)");
		sh.getRow(rNum).getCell(0).setCellStyle(getStyle1());
		
		sh.addMergedRegion(new CellRangeAddress(rNum, rNum, 0, 6)); 
		sh.createRow(rNum+1);
		sh.createRow(rNum+2);			

		sh.getRow(rNum+1).createCell(1).setCellValue("月份");
		sh.getRow(rNum+1).createCell(3).setCellValue(month.substring(4)+"月");
		sh.getRow(rNum+2).createCell(1).setCellValue("排名");
		sh.getRow(rNum+2).createCell(2).setCellValue("省份");
		sh.getRow(rNum+2).createCell(3).setCellValue("有价卡总金额(元)");
		sh.getRow(rNum+2).createCell(4).setCellValue("有价卡违规金额(元)");
		sh.getRow(rNum+2).createCell(5).setCellValue("有价卡违规金额占比");
		sh.getRow(rNum+2).createCell(6).setCellValue("有价卡违规金额环比增幅%");

		sh.addMergedRegion(new CellRangeAddress(rNum+1, rNum+1, 1, 2)); 
		sh.addMergedRegion(new CellRangeAddress(rNum+1, rNum+1, 3, 6)); 
		sh.getRow(rNum+2).setHeightInPoints(32);
		
		for(int i=1;i<7; i++) {
			sh.getRow(rNum+2).getCell(i).setCellStyle(getStyle2());
			if (null == sh.getRow(rNum+1).getCell(i)) {
				sh.getRow(rNum+1).createCell(i).setCellStyle(getStyle2());
			} else {
				sh.getRow(rNum+1).getCell(i).setCellStyle(getStyle2());
			}
		}
		Map<String,Object> row=new HashMap<String,Object>();
		for(int i = 0,dataSize=data.size(); i < dataSize; i++) {
			sh.createRow(rNum+3+i);
			row = data.get(i);
			sh.getRow(rNum+3+i).createCell(1).setCellValue(i+1);
			sh.getRow(rNum+3+i).createCell(2).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
			if (row.size() == 1) {
				for(int j=3;j<7;j++) {
					sh.getRow(rNum+3+i).createCell(j).setCellValue("NA");
					sh.getRow(rNum+3+i).getCell(j).setCellStyle(getStyle7());
				}
				sh.getRow(rNum+3+i).getCell(1).setCellStyle(getStyle5());
				sh.getRow(rNum+3+i).getCell(2).setCellStyle(getStyle5());
			} else {
				for(int j=1;j<7;j++) {
					if (null == sh.getRow(rNum+3+i).getCell(j)) {
						sh.getRow(rNum+3+i).createCell(j);
					}
					if(j>4) {
						sh.getRow(rNum+3+i).getCell(j).setCellStyle(getStyle6());
					}else {
						sh.getRow(rNum+3+i).getCell(j).setCellStyle(getStyle5());
					}
				}
				
				sh.getRow(rNum+3+i).getCell(3).setCellValue(row.get("t_amt1") instanceof BigDecimal ? ((BigDecimal) row.get("t_amt1")).longValue():(Long) row.get("t_amt1"));
				sh.getRow(rNum+3+i).getCell(4).setCellValue(row.get("amt1") instanceof BigDecimal ? ((BigDecimal) row.get("amt1")).longValue():(Long) row.get("amt1"));
				sh.getRow(rNum+3+i).getCell(5).setCellValue(row.get("per_amt1") instanceof BigDecimal ? ((BigDecimal) row.get("per_amt1")).doubleValue():(Double) row.get("per_amt1"));
				
				if (null != row.get("id_pec")){
					sh.getRow(rNum+3+i).getCell(6).setCellValue((Double)row.get("id_pec"));
				} else {
					sh.getRow(rNum+3+i).getCell(6).setCellValue("NA");
					sh.getRow(rNum+3+i).getCell(6).setCellStyle(getStyle7());
				}
			}
		}
		sh.getRow(4).getCell(3).setCellFormula("SUM(D"+(rNum+4)+":D"+(rNum+3+data.size())+")");
		sh.getRow(5).getCell(3).setCellFormula("SUM(E"+(rNum+4)+":E"+(rNum+3+data.size())+")");
		sh.getRow(6).getCell(3).setCellFormula("D6/D5");
		rNum = 17 + data.size();
	}
	
	public void writeSheet1001(List<Map<String, Object>>data) {
		writeSheetCommon("未按规定在系统间同步加载","有价卡生成数据未在BOSS系统和VC系统同步加载", "有价卡生成状态","CRM和VC系统有价卡生成状态",data);
	}
	
	public void writeSheet1002(List<Map<String, Object>>data) {
		writeSheetCommon("违规激活","有价卡销售激活在BOSS系统和VC系统不同步", "有价卡激活状态","CRM和VC系统有价卡激活状态",data);
	}
	
	public void writeSheet1003(List<Map<String, Object>>data) {
		writeSheetCommon("违规销售","有价卡销售前已被充值", "有价卡已使用状态","VC系统有价卡已使用状态",data);
	}
	
	public void writeSheet1004(List<Map<String, Object>>data) {
		writeSheetCommon("退换后的坏卡或报废卡未封锁","有价卡报废未在VC系统锁定", "CRM有价卡锁定、清退状态","CRM有价卡锁定、清退状态",data);
	}
	
	public void writeSheet1005(List<Map<String, Object>>data) {
		writeSheetCommon("违规重复使用","有价卡已充值但未在VC系统标记“已充值”状态","有价卡已使用状态","CRM有价卡已使用状态", data);
	}
	
	public void writeSheetCommon(String sheetName, String typeName, String fieldName,String fieldTwoName, List<Map<String, Object>>data) {
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
		sh.getRow(2).createCell(4).setCellValue(fieldName+"总金额(元)");
		sh.getRow(2).createCell(5).setCellValue("有价卡违规金额(元)");
		sh.getRow(2).createCell(6).setCellValue("有价卡违规金额占比");
		
		sh.getRow(2).createCell(7).setCellValue(fieldTwoName+"总数量(张)");
		sh.getRow(2).createCell(8).setCellValue("有价卡违规数量(张)");
		sh.getRow(2).createCell(9).setCellValue("有价卡违规数量占比");
		sh.getRow(2).createCell(10).setCellValue("备注");
																
		sh.addMergedRegion(new CellRangeAddress(0, 0, 1, 14)); 
		sh.addMergedRegion(new CellRangeAddress(1, 1, 2, 3)); 
		sh.addMergedRegion(new CellRangeAddress(1, 1, 4, 14)); 
		sh.addMergedRegion(new CellRangeAddress(2, 2, 10, 14));
		sh.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
		sh.addMergedRegion(new CellRangeAddress(3, 2+data.size(), 1, 1));
		
		sh.getRow(0).getCell(1).setCellStyle(getRankStyle0());
		for(int i=1;i<=2;i++){
			for(int j=1;j<=14;j++){
				if (null == sh.getRow(i).getCell(j)) {
					sh.getRow(i).createCell(j);
				}
				sh.getRow(i).getCell(j).setCellStyle(getRankStyle1());
			}
		}
		
		for(int i = 1 ; i < 14; i++) sh.setColumnWidth(i, 256 * 16);
		sh.setColumnWidth(14, 256 * 24);
		
		sh.setColumnWidth(1, 256 * 18);
		sh.getRow(2).setHeightInPoints(45);
		
		Map<String, Object> row=new HashMap<String,Object>();
		for(int i = 0,dataSize=data.size(); i < dataSize; i++) {
			if (i>0) sh.createRow(3+i);
			row = data.get(i);
			sh.getRow(3+i).createCell(2).setCellValue(i+1);
			sh.getRow(3+i).createCell(3).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
			if (row.size() < 7) {
				for(int j=4;j<=9;j++) {
					sh.getRow(3+i).createCell(j).setCellValue("NA");
					sh.getRow(3+i).getCell(j).setCellStyle(getRankStyle7());
				}
				sh.getRow(3+i).getCell(2).setCellStyle(getRankStyle5());
				sh.getRow(3+i).getCell(3).setCellStyle(getRankStyle5());
			}else{
				//System.out.println(row);
				sh.getRow(3+i).createCell(4).setCellValue(row.get("t_amt1") instanceof BigDecimal ? ((BigDecimal) row.get("t_amt1")).longValue():(Long) row.get("amt1"));
				sh.getRow(3+i).createCell(5).setCellValue(row.get("amt1") instanceof BigDecimal ? ((BigDecimal) row.get("amt1")).longValue():(Long) row.get("amt1"));
				sh.getRow(3+i).createCell(6).setCellValue(row.get("per_amt1") instanceof BigDecimal ? ((BigDecimal) row.get("per_amt1")).doubleValue():(Double) row.get("per_amt1"));
				sh.getRow(3+i).createCell(7).setCellValue((Integer) row.get("t_cnt1"));
				sh.getRow(3+i).createCell(8).setCellValue((Integer) row.get("cnt1"));
				sh.getRow(3+i).createCell(9).setCellValue(row.get("per_cnt1") instanceof BigDecimal ? ((BigDecimal) row.get("per_cnt1")).doubleValue():(Double) row.get("per_cnt1"));
				
				for(int j=2;j<=9;j++) {
					if(j==6 || j == 9) {
						sh.getRow(3+i).getCell(j).setCellStyle(getRankStyle6());
					}else {
						sh.getRow(3+i).getCell(j).setCellStyle(getRankStyle5());
					}
				}
				
			}
			sh.getRow(3+i).createCell(10).setCellStyle(getRankStyle7());
			if (null != row.get("infraction_typ_name")) {
				sh.getRow(3+i).getCell(10).setCellValue((String) row.get("infraction_typ_name"));
				
			}
			sh.addMergedRegion(new CellRangeAddress(3+i, 3+i, 10, 14));
			for(int j=11;j<=14;j++) {
				sh.getRow(3+i).createCell(j).setCellStyle(getRankStyle2());
			}
			

		}
		
		for(int i=3,dataSize=data.size();i<=2+dataSize;i++) {
			if (null == sh.getRow(i).getCell(1)) {
				sh.getRow(i).createCell(1);
			}
			sh.getRow(i).getCell(1).setCellStyle(getRankStyle1());
		}
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
		style.setAlignment(CellStyle.ALIGN_LEFT);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		style.setFont(font);
		//style.setWrapText(true); 
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
	
	private CellStyle getRankStyle0(){
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
	
	private CellStyle getRankStyle1(){
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
	
	private CellStyle getRankStyle2(){
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
	
	private CellStyle getRankStyle3(){
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
	
	private CellStyle getRankStyle4(){
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
	
	private CellStyle getRankStyle5(){
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
	
	private CellStyle getRankStyle6(){
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
	
	private CellStyle getRankStyle7(){
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
