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


@Service("NotiFileKhqf4000Top100Processor")
public class NotiFileKhqf4000Top100Processor extends AbstractNotiFileProcessor {
	protected Logger logger = Logger.getLogger(this.getClass());
	public boolean generate() throws Exception{
		//this.setFocusCd("3003");
		this.setFileName("个人和集团欠费TOP100清单");
		writeSheet1(notiFileGenService.selHisGRTop100(month));
		writeSheet2(notiFileGenService.selCurGRTop100(month));
		writeSheet3(notiFileGenService.selHisJTTop100(month));
		writeSheet4(notiFileGenService.selCurJTTop100(month));
		writeSheet5(notiFileGenService.selGRorJTTop100());
		return true;
	}
	
	public void writeSheet1(List<Map<String, Object>> data){
		String[] columnNames = {"序号","审计月","省份名称","地市名称","账户标识","账户名称","账户状态","累计欠费金额（元）","欠费起始期间","欠费账龄","欠费月数（产生欠费的月数）","手机号码","用户标识","用户状态","用户付费类型","用户类型","客户标识","客户状态","重要客户标识"};
		writeSheetCommon(data, "历史个人欠费用户top100", "历史个人欠费用户top100", columnNames);
	}
	public void writeSheet2(List<Map<String, Object>> data){
		String[] columnNames = {"序号","审计月","省份名称","地市名称","账户标识","账户名称","账户状态","累计欠费金额（元）","欠费起始期间","欠费账龄","欠费月数（产生欠费的月数）","手机号码","用户标识","用户状态","用户付费类型","用户类型","客户标识","客户状态","重要客户标识"};
		writeSheetCommon(data, "本月新增个人欠费用户top100", "本月新增个人欠费用户top100", columnNames);
	}
	
	public void writeSheet3(List<Map<String, Object>> data){
		String[] columnNames = {"序号","审计月","省份名称","地市名称","账户标识","账户名称","账户状态","累计欠费金额（元）","欠费起始期间","欠费账龄","欠费月数（产生欠费的月数）","手机号码","用户标识","用户状态","用户付费类型","用户类型","客户标识","客户状态","集团客户等级"};
		writeSheetCommon(data, "历史集团欠费客户top100", "历史集团欠费客户top100", columnNames);
	}
	
	public void writeSheet4(List<Map<String, Object>> data){
		String[] columnNames = {"序号","审计月","省份名称","地市名称","账户标识","账户名称","账户状态","累计欠费金额（元）","欠费起始期间","欠费账龄","欠费月数（产生欠费的月数）","手机号码","用户标识","用户状态","用户付费类型","用户类型","客户标识","客户状态","集团客户等级"};
		writeSheetCommon(data, "本月新增集团欠费客户top100", "本月新增集团欠费客户top100", columnNames);
	}
	
	public void writeSheet5(List<Map<String, Object>> data){
		String[] columnNames = {"序号","审计月","省份名称","地市名称","账户标识","账户名称","账户状态","累计欠费金额（元）","欠费起始期间","欠费账龄","欠费月数","手机号码","用户标识","用户状态","用户付费类型","用户类型","客户标识","客户状态"};
		writeSheetCommon(data, "无法区分个人或集团欠费账户top100", "无法区分个人或集团欠费账户top100", columnNames);
	}
	

	public void writeSheetCommon(List<Map<String, Object>> data, String sheetName, String title, String[] columnNames){
		logger.debug("---------data:\n" + data);
		sh = wb.createSheet(sheetName);
		sh.createRow(0).createCell(0).setCellValue(title);
		sh.getRow(0).getCell(0).setCellStyle(getStyle0());
		sh.addMergedRegion(new CellRangeAddress(0, 0, 0, columnNames.length-1)); 
		sh.createRow(1);
		for (int i = 0; i < columnNames.length; i++) {
			sh.getRow(1).createCell(i).setCellValue(columnNames[i]);
			sh.setColumnWidth(i, 256 * 12);			
			sh.getRow(1).getCell(i).setCellStyle(getStyle1());
			
		}
		sh.getRow(1).setHeightInPoints(32);
		sh.createFreezePane(0, 2, 0, 2);
		//int tolNum = 0;
		Map<String, Object> row =new HashMap<String, Object>();
		for(int i = 0,dataSize=data.size(); i <= dataSize; i++) {
			sh.createRow(2+i);
			if (i < dataSize) {
				row = data.get(i);
				if(row.get("numbers")!=null){
					sh.getRow(2+i).createCell(0).setCellValue((Integer) row.get("numbers"));
					sh.getRow(2+i).getCell(0).setCellStyle(getStyle6());
				}else{
					sh.getRow(2+i).createCell(0).setCellValue("-");
					sh.getRow(2+i).getCell(0).setCellStyle(getStyle3());
				}

				if(row.get("aud_trm")!=null){
					sh.getRow(2+i).createCell(1).setCellValue((String) row.get("aud_trm"));
					sh.getRow(2+i).getCell(1).setCellStyle(getStyle3());
				}else{
					sh.getRow(2+i).createCell(1).setCellValue("-");
					sh.getRow(2+i).getCell(1).setCellStyle(getStyle3());
				}

				if(row.get("prvd_nm")!=null){
					sh.getRow(2+i).createCell(2).setCellValue((String) row.get("prvd_nm"));
					sh.getRow(2+i).getCell(2).setCellStyle(getStyle3());
				}else{
					sh.getRow(2+i).createCell(2).setCellValue("-");
					sh.getRow(2+i).getCell(2).setCellStyle(getStyle3());
				}
			
				if(row.get("prvd_nm_short")!=null){
					sh.getRow(2+i).createCell(3).setCellValue((String) row.get("prvd_nm_short"));
					sh.getRow(2+i).getCell(3).setCellStyle(getStyle3());
				}else{
					sh.getRow(2+i).createCell(3).setCellValue("-");
					sh.getRow(2+i).getCell(3).setCellStyle(getStyle3());
				}
				
				
				if(row.get("acct_id")!=null){
					sh.getRow(2+i).createCell(4).setCellValue((String) row.get("acct_id"));
					sh.getRow(2+i).getCell(4).setCellStyle(getStyle3());
				}else{
					sh.getRow(2+i).createCell(4).setCellValue("-");
					sh.getRow(2+i).getCell(4).setCellStyle(getStyle3());
				}

				
				if(row.get("acct_nm")!=null){
					sh.getRow(2+i).createCell(5).setCellValue((String) row.get("acct_nm"));
					sh.getRow(2+i).getCell(5).setCellStyle(getStyle3());
				}else{
					sh.getRow(2+i).createCell(5).setCellValue("-");
					sh.getRow(2+i).getCell(5).setCellStyle(getStyle3());
				}

				
				if(row.get("acct_stat_typ_nm")!=null){
					sh.getRow(2+i).createCell(6).setCellValue((String) row.get("acct_stat_typ_nm"));
					sh.getRow(2+i).getCell(6).setCellStyle(getStyle3());
				}else{
					sh.getRow(2+i).createCell(6).setCellValue("-");
					sh.getRow(2+i).getCell(6).setCellStyle(getStyle3());
				}

				
				if(row.get("dbt_amt")!=null){
					sh.getRow(2+i).createCell(7).setCellValue(row.get("dbt_amt") instanceof BigDecimal ? ((BigDecimal) row.get("dbt_amt")).doubleValue():(Double) row.get("dbt_amt"));			
					sh.getRow(2+i).getCell(7).setCellStyle(getStyle5());
				}else{
					sh.getRow(2+i).createCell(7).setCellValue("-");
					sh.getRow(2+i).getCell(7).setCellStyle(getStyle3());
				}

				
				if(row.get("min_acct_prd_ytm")!=null){
					sh.getRow(2+i).createCell(8).setCellValue((Integer) row.get("min_acct_prd_ytm"));
					sh.getRow(2+i).getCell(8).setCellStyle(getStyle6());
				}else{
					sh.getRow(2+i).createCell(8).setCellValue("-");
					sh.getRow(2+i).getCell(8).setCellStyle(getStyle3());
				}

				
				if(row.get("aging")!=null){
					sh.getRow(2+i).createCell(9).setCellValue((Integer) row.get("aging"));
					sh.getRow(2+i).getCell(9).setCellStyle(getStyle6());
				}else{
					sh.getRow(2+i).createCell(9).setCellValue("-");
					sh.getRow(2+i).getCell(9).setCellStyle(getStyle3());
				}

				if(row.get("cnt_month")!=null){
					sh.getRow(2+i).createCell(10).setCellValue((Integer) row.get("cnt_month"));
					sh.getRow(2+i).getCell(10).setCellStyle(getStyle6());
				}else{
					sh.getRow(2+i).createCell(10).setCellValue("-");
					sh.getRow(2+i).getCell(10).setCellStyle(getStyle3());
				}

				
//				if(row.get("subs_typ_cd")!=null){
//					sh.getRow(2+i).createCell(11).setCellValue((String) row.get("subs_typ_cd"));
//					sh.getRow(2+i).getCell(11).setCellStyle(getStyle3());
//				}else{
//					sh.getRow(2+i).createCell(11).setCellValue("-");
//					sh.getRow(2+i).getCell(11).setCellStyle(getStyle3());
//				}

				
				if(row.get("MSISDN")!=null){
					sh.getRow(2+i).createCell(11).setCellValue((String) row.get("MSISDN"));
					sh.getRow(2+i).getCell(11).setCellStyle(getStyle3());
				}else{
					sh.getRow(2+i).createCell(11).setCellValue("-");
					sh.getRow(2+i).getCell(11).setCellStyle(getStyle3());
				}

				
				if(row.get("subs_id")!=null){
					sh.getRow(2+i).createCell(12).setCellValue((String) row.get("subs_id"));
					sh.getRow(2+i).getCell(12).setCellStyle(getStyle3());
				}else{
					sh.getRow(2+i).createCell(12).setCellValue("-");
					sh.getRow(2+i).getCell(12).setCellStyle(getStyle3());
				}

				
				if(row.get("subs_stat_typ_cd")!=null){
					sh.getRow(2+i).createCell(13).setCellValue((String) row.get("subs_stat_typ_cd"));
					sh.getRow(2+i).getCell(13).setCellStyle(getStyle3());
				}else{
					sh.getRow(2+i).createCell(13).setCellValue("-");
					sh.getRow(2+i).getCell(13).setCellStyle(getStyle3());
				}

				
				if(row.get("subs_pay_typ_nm")!=null){
					sh.getRow(2+i).createCell(14).setCellValue((String) row.get("subs_pay_typ_nm"));
					sh.getRow(2+i).getCell(14).setCellStyle(getStyle3());
				}else{
					sh.getRow(2+i).createCell(14).setCellValue("-");
					sh.getRow(2+i).getCell(14).setCellStyle(getStyle3());
				}

				
				if(row.get("subs_typ_cd")!=null){
					sh.getRow(2+i).createCell(15).setCellValue((String) row.get("subs_typ_cd"));
					sh.getRow(2+i).getCell(15).setCellStyle(getStyle3());
				}else{
					sh.getRow(2+i).createCell(15).setCellValue("-");
					sh.getRow(2+i).getCell(15).setCellStyle(getStyle3());
				}

				
				if(row.get("blto_cust_id")!=null){
					sh.getRow(2+i).createCell(16).setCellValue((String) row.get("blto_cust_id"));
					sh.getRow(2+i).getCell(16).setCellStyle(getStyle3());
				}else{
					sh.getRow(2+i).createCell(16).setCellValue("-");
					sh.getRow(2+i).getCell(16).setCellStyle(getStyle3());
				}

				
				if(row.get("cust_stat_typ_nm")!=null){
					sh.getRow(2+i).createCell(17).setCellValue((String) row.get("cust_stat_typ_nm"));
					sh.getRow(2+i).getCell(17).setCellStyle(getStyle3());
				}else{
					sh.getRow(2+i).createCell(17).setCellValue("-");
					sh.getRow(2+i).getCell(17).setCellStyle(getStyle3());
				}

				
				if(columnNames.length != 18){
					if(row.get("subs_attr")!=null){
						sh.getRow(2+i).createCell(18).setCellValue((String) row.get("subs_attr"));
						sh.getRow(2+i).getCell(18).setCellStyle(getStyle3());
					}else{
						sh.getRow(2+i).createCell(18).setCellValue("-");
						sh.getRow(2+i).getCell(18).setCellStyle(getStyle3());
					}
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
