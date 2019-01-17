package com.hpe.cmca.job;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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

@Service("NotiFileKhqf4000PmhzProcessor")
public class NotiFileKhqf4000PmhzProcessor extends AbstractNotiFileProcessor {
	
	DecimalFormat dfs_2 = new DecimalFormat("0.00");
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	public boolean generate() throws Exception{
		//this.setFocusCd("3003");
		this.setFileName("客户长期高额欠费汇总排名");
		writeSheet1(notiFileGenService.selGRPmhz(month),month);
		writeSheet2(notiFileGenService.selJTPmhz(month),month);
		return true;
	}
	
	public void writeSheet1(List<Map<String, Object>> data,String month){
		String[] columnNames = {"统计截止月份",month+"月","","","","","","","","","","","","",""};
		String[] columnNames1 = {"欠费金额排名","省份名称","欠费金额（元）","欠费用户数量（户）","新增欠费用户欠费金额（元）","新增欠费用户数量（户）","新增欠费用户欠费金额占上期长期高额欠费金额的比例（%）",
				"缴清全部欠上期用户欠费回收金额（元）","欠费用户回收数量","当期回收欠费金额占上期长期高额欠费的比例（%）","原有欠费用户欠费金额（元）","原有欠费用户数量（户）","其他原有用户欠费变动金额（元）","上期长期高额欠费金额（元）","上期欠费用户数量（户）"};
		String[] columnNames2 = {"统计截止月份","欠费金额（万元）","欠费用户数量（户）","新增欠费用户欠费金额（万元）","新增欠费用户数量（户）",
				"新增欠费用户欠费金额占上期长期高额欠费金额的比例（%）","缴清全部欠费上期用户欠费回收金额（万元）","欠费用户回收数量（户）",
				"当期回收欠费金额占上期长期高额欠费的比例（%）","上期长期高额欠费金额（万元）","上期欠费用户数量（户）"};
		writeSheetCommon(data, "个人客户长期高额欠费", "个人客户长期高额欠费","个人用户长期高额欠费整体情况","31省公司个人用户长期高额欠费情况", columnNames,columnNames1,columnNames2);
	}
	public void writeSheet2(List<Map<String, Object>> data,String month){
		String[] columnNames = {"统计截止月份",month+"月","","","","","","","","","","","","",""};
//		String[] columnNames1 = {"欠费金额排名","省份名称","欠费金额（元）","欠费用户数量（户）","新增欠费用户欠费金额（元）","新增欠费用户数量（户）","原有欠费用户欠费金额（元）","原有欠费用户数量（户）","缴清全部欠费原有用户欠费回收金额（元）","欠费用户回收数量","其他原有用户欠费变动金额（元）"};
		String[] columnNames1 = {"欠费金额排名","省份名称","欠费金额（元）","欠费用户数量（户）","新增欠费用户欠费金额（元）","新增欠费用户数量（户）","新增欠费用户欠费金额占上期长期高额欠费金额的比例（%）",
				"缴清全部欠上期用户欠费回收金额（元）","欠费用户回收数量","当期回收欠费金额占上期长期高额欠费的比例（%）","原有欠费用户欠费金额（元）","原有欠费用户数量（户）","其他原有用户欠费变动金额（元）","上期长期高额欠费金额（元）","上期欠费用户数量（户）"};
		String[] columnNames2 = {"统计截止月份","欠费金额（万元）","欠费用户数量（户）","新增欠费用户欠费金额（万元）","新增欠费用户数量（户）",
				"新增欠费用户欠费金额占上期长期高额欠费金额的比例（%）","缴清全部欠费上期用户欠费回收金额（万元）","欠费用户回收数量（户）",
				"当期回收欠费金额占上期长期高额欠费的比例（%）","上期长期高额欠费金额（万元）","上期欠费用户数量（户）"};
		writeSheetCommon(data, "集团客户长期高额欠费", "集团客户长期高额欠费","集团用户长期高额欠费整体情况","31省公司集团用户长期高额欠费情况", columnNames,columnNames1,columnNames2);
	}
	

	public void writeSheetCommon(List<Map<String, Object>> data, String sheetName, String title,String title1,String title2, String[] columnNames, String[] columnNames1, String[] columnNames2){
		logger.debug("---------data:\n" + data);
		sh = wb.createSheet(sheetName);
		sh.createRow(0).createCell(0).setCellValue(title);
		sh.getRow(0).getCell(0).setCellStyle(getStyle0());
		sh.addMergedRegion(new CellRangeAddress(0, 0, 0, columnNames.length)); 
		
		sh.createRow(1);
		
		sh.createRow(2).createCell(0).setCellValue(title1);
		sh.getRow(2).getCell(0).setCellStyle(getStyle00());
		sh.addMergedRegion(new CellRangeAddress(2, 2, 0, 4)); 
		
		sh.createRow(3+columnNames2.length).createCell(0).setCellValue("注：从2013年1月1日开始统计，下同");
		sh.getRow(3+columnNames2.length).getCell(0).setCellStyle(getStyle00());
		sh.addMergedRegion(new CellRangeAddress(3+columnNames2.length, 3+columnNames2.length, 0, 4)); 
		
		sh.createRow(5+columnNames2.length).createCell(0).setCellValue(title2);
		sh.getRow(5+columnNames2.length).getCell(0).setCellStyle(getStyle00());
		sh.addMergedRegion(new CellRangeAddress(5+columnNames2.length, 5+columnNames2.length, 0, 4)); 
		
		
		sh.createRow(6+columnNames2.length);
		for (int i = 1; i < columnNames.length+1; i++) {
			sh.getRow(6+columnNames2.length).createCell(i).setCellValue(columnNames[i-1]);
			sh.setColumnWidth(i, 256 * 18);			
			sh.getRow(6+columnNames2.length).getCell(i).setCellStyle(getStyle1());	
		}
		sh.addMergedRegion(new CellRangeAddress(6+columnNames2.length, 6+columnNames2.length, 2, columnNames.length)); 
		
		sh.createRow(7+columnNames2.length);
		for (int i = 1; i < columnNames1.length+1; i++) {
			sh.getRow(7+columnNames2.length).createCell(i).setCellValue(columnNames1[i-1]);
			sh.setColumnWidth(i, 256 * 18);			
			sh.getRow(7+columnNames2.length).getCell(i).setCellStyle(getStyle1());	
		}
		
		//sh.getRow(1).setHeightInPoints(32);
		sh.createFreezePane(0, 2, 0, 7+columnNames2.length);
		Integer num1= 0;
		Integer num2= 0;
		Integer num3 = 0;
		Integer num4 = 0;
		Integer num7 = 0;
		Double amt1=0.00;
		Double amt2=0.00;
		Double amt3=0.00;
		Double amt4=0.00;
		Double amt5=0.00;
		Double amt6=0.00;
		Double amt7=0.00;
		Double amt8=0.00;
		Double amt9=0.00;
		Double amt10=0.00;
		Map<String, Object> row = new HashMap<String, Object>();
		for(int i = 0,dataSize=data.size(); i <= dataSize; i++) {
			sh.createRow(8+columnNames2.length+i);
			if (i < dataSize) {
				row = data.get(i);
				if(row.get("rn")!=null){
					sh.getRow(8+columnNames2.length+i).createCell(1).setCellValue((Integer) row.get("rn"));
					sh.getRow(8+columnNames2.length+i).getCell(1).setCellStyle(getStyle6());
				}else{
					sh.getRow(8+columnNames2.length+i).createCell(1).setCellValue("-");
					sh.getRow(8+columnNames2.length+i).getCell(1).setCellStyle(getStyle3());
				}

				if(row.get("CMCC_prov_prvd_nm")!=null){
					sh.getRow(8+columnNames2.length+i).createCell(2).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
					sh.getRow(8+columnNames2.length+i).getCell(2).setCellStyle(getStyle3());
				}else{
					sh.getRow(8+columnNames2.length+i).createCell(2).setCellValue("-");
					sh.getRow(8+columnNames2.length+i).getCell(2).setCellStyle(getStyle3());
				}

				
				if(row.get("infraction_amt")!=null){
					sh.getRow(8+columnNames2.length+i).createCell(3).setCellValue(row.get("infraction_amt") instanceof BigDecimal ? ((BigDecimal) row.get("infraction_amt")).doubleValue():(Double) row.get("infraction_amt"));			
					sh.getRow(8+columnNames2.length+i).getCell(3).setCellStyle(getStyle5());
					amt1=amt1+(row.get("infraction_amt") instanceof BigDecimal ? ((BigDecimal) row.get("infraction_amt")).doubleValue():(Double) row.get("infraction_amt"));
				}else{
					sh.getRow(8+columnNames2.length+i).createCell(3).setCellValue("-");			
					sh.getRow(8+columnNames2.length+i).getCell(3).setCellStyle(getStyle3());
				}


				if(row.get("infraction_num")!=null){
					sh.getRow(8+columnNames2.length+i).createCell(4).setCellValue((Integer) row.get("infraction_num"));
					sh.getRow(8+columnNames2.length+i).getCell(4).setCellStyle(getStyle6());
					num1=num1+((Integer) row.get("infraction_num"));
				}else{
					sh.getRow(8+columnNames2.length+i).createCell(4).setCellValue("-");
					sh.getRow(8+columnNames2.length+i).getCell(4).setCellStyle(getStyle3());
				}

				
				if( row.get("amt_new")!=null){
					sh.getRow(8+columnNames2.length+i).createCell(5).setCellValue(row.get("amt_new") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new")).doubleValue():(Double) row.get("amt_new"));			
					sh.getRow(8+columnNames2.length+i).getCell(5).setCellStyle(getStyle5());
					amt2=amt2+(row.get("amt_new") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new")).doubleValue():(Double) row.get("amt_new"));
				}else{
					sh.getRow(8+columnNames2.length+i).createCell(5).setCellValue("-");			
					sh.getRow(8+columnNames2.length+i).getCell(5).setCellStyle(getStyle3());
				}

				
				if( row.get("subs_new")!=null){
					sh.getRow(8+columnNames2.length+i).createCell(6).setCellValue((Integer) row.get("subs_new"));
					sh.getRow(8+columnNames2.length+i).getCell(6).setCellStyle(getStyle6());
					num2=num2+((Integer) row.get("subs_new"));
				}else{
					sh.getRow(8+columnNames2.length+i).createCell(6).setCellValue("-");
					sh.getRow(8+columnNames2.length+i).getCell(6).setCellStyle(getStyle3());
				}
				
				if(row.get("last_month_amt")==null || ((BigDecimal)row.get("last_month_amt")).doubleValue()==0){
					sh.getRow(8+columnNames2.length+i).createCell(7).setCellValue("*");			
					sh.getRow(8+columnNames2.length+i).getCell(7).setCellStyle(getStyle3());
				}
				else if( row.get("amt_new_per")!=null && !row.get("amt_new_per").equals("-9999.00")){
					sh.getRow(8+columnNames2.length+i).createCell(7).setCellValue(
							row.get("amt_new_per")instanceof BigDecimal?((BigDecimal)row.get("amt_new_per")).doubleValue():Double.valueOf(row.get("amt_new_per").toString().trim()));			
					sh.getRow(8+columnNames2.length+i).getCell(7).setCellStyle(getStyle5());
//					amt3=amt3+(row.get("amt_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new_per")).doubleValue():(Double) row.get("amt_new_per"));
				}else{
					sh.getRow(8+columnNames2.length+i).createCell(7).setCellValue("-");			
					sh.getRow(8+columnNames2.length+i).getCell(7).setCellStyle(getStyle3());
				}

				if( row.get("amt_all_recover")!=null){
					sh.getRow(8+columnNames2.length+i).createCell(8).setCellValue(row.get("amt_all_recover") instanceof BigDecimal ? ((BigDecimal) row.get("amt_all_recover")).doubleValue():(Double) row.get("amt_all_recover"));			
					sh.getRow(8+columnNames2.length+i).getCell(8).setCellStyle(getStyle5());
					amt8=amt8+(row.get("amt_all_recover") instanceof BigDecimal ? ((BigDecimal) row.get("amt_all_recover")).doubleValue():(Double) row.get("amt_all_recover"));
				}else{
					sh.getRow(8+columnNames2.length+i).createCell(8).setCellValue("-");			
					sh.getRow(8+columnNames2.length+i).getCell(8).setCellStyle(getStyle3());
				}
				
				if( row.get("sum_subs_recover")!=null){
					sh.getRow(8+columnNames2.length+i).createCell(9).setCellValue((Integer) row.get("sum_subs_recover"));
					sh.getRow(8+columnNames2.length+i).getCell(9).setCellStyle(getStyle6());
					num4=num4+((Integer) row.get("sum_subs_recover"));
				}else{
					sh.getRow(8+columnNames2.length+i).createCell(9).setCellValue("-");
					sh.getRow(8+columnNames2.length+i).getCell(9).setCellStyle(getStyle3());
				}
				
				if(row.get("last_month_amt")==null || ((BigDecimal)row.get("last_month_amt")).doubleValue()==0){
					sh.getRow(8+columnNames2.length+i).createCell(10).setCellValue("*");
					sh.getRow(8+columnNames2.length+i).getCell(10).setCellStyle(getStyle3());
				}
				else if( row.get("amt_recover_per")!=null && !row.get("amt_recover_per").equals("-9999.00")){
					sh.getRow(8+columnNames2.length+i).createCell(10).setCellValue(
							row.get("amt_recover_per")instanceof BigDecimal?((BigDecimal)row.get("amt_recover_per")).doubleValue():Double.valueOf(row.get("amt_recover_per").toString().trim()));
					sh.getRow(8+columnNames2.length+i).getCell(10).setCellStyle(getStyle5());
//					num4=num4+((Integer) row.get("amt_recover_per"));
				}else{
					sh.getRow(8+columnNames2.length+i).createCell(10).setCellValue("-");
					sh.getRow(8+columnNames2.length+i).getCell(10).setCellStyle(getStyle3());
				}
				
				if( row.get("amt_old")!=null){
					sh.getRow(8+columnNames2.length+i).createCell(11).setCellValue(row.get("amt_old") instanceof BigDecimal ? ((BigDecimal) row.get("amt_old")).doubleValue():(Double) row.get("amt_old"));			
					sh.getRow(8+columnNames2.length+i).getCell(11).setCellStyle(getStyle5());
					amt3=amt3+(row.get("amt_old") instanceof BigDecimal ? ((BigDecimal) row.get("amt_old")).doubleValue():(Double) row.get("amt_old"));
				}else{
					sh.getRow(8+columnNames2.length+i).createCell(11).setCellValue("-");			
					sh.getRow(8+columnNames2.length+i).getCell(11).setCellStyle(getStyle3());
				}

				
				if( row.get("num_old")!=null){
					sh.getRow(8+columnNames2.length+i).createCell(12).setCellValue((Integer) row.get("num_old"));
					sh.getRow(8+columnNames2.length+i).getCell(12).setCellStyle(getStyle6());
					num3=num3+((Integer) row.get("num_old"));
				}else{
					sh.getRow(8+columnNames2.length+i).createCell(12).setCellValue("-");
					sh.getRow(8+columnNames2.length+i).getCell(12).setCellStyle(getStyle3());
				}
				
				if( row.get("amt_other_var")!=null){
					sh.getRow(8+columnNames2.length+i).createCell(13).setCellValue(row.get("amt_other_var") instanceof BigDecimal ? ((BigDecimal) row.get("amt_other_var")).doubleValue():(Double) row.get("amt_other_var"));			
					sh.getRow(8+columnNames2.length+i).getCell(13).setCellStyle(getStyle5());
					amt5=amt5+(row.get("amt_other_var") instanceof BigDecimal ? ((BigDecimal) row.get("amt_other_var")).doubleValue():(Double) row.get("amt_other_var"));
				}else{
					sh.getRow(8+columnNames2.length+i).createCell(13).setCellValue("-");			
					sh.getRow(8+columnNames2.length+i).getCell(13).setCellStyle(getStyle3());
				}
				
				if( row.get("last_month_amt")!=null){
					sh.getRow(8+columnNames2.length+i).createCell(14).setCellValue(row.get("last_month_amt") instanceof BigDecimal ? ((BigDecimal) row.get("last_month_amt")).doubleValue():(Double) row.get("last_month_amt"));			
					sh.getRow(8+columnNames2.length+i).getCell(14).setCellStyle(getStyle5());
					amt7=amt7+(row.get("last_month_amt") instanceof BigDecimal ? ((BigDecimal) row.get("last_month_amt")).doubleValue():(Double) row.get("last_month_amt"));
				}else{
					sh.getRow(8+columnNames2.length+i).createCell(14).setCellValue("-");			
					sh.getRow(8+columnNames2.length+i).getCell(14).setCellStyle(getStyle3());
				}
				
				if( row.get("last_month_num")!=null){
					sh.getRow(8+columnNames2.length+i).createCell(15).setCellValue((Integer)row.get("last_month_num") );			
					sh.getRow(8+columnNames2.length+i).getCell(15).setCellStyle(getStyle6());
					num7=num7+(Integer)row.get("last_month_num");
				}else{
					sh.getRow(8+columnNames2.length+i).createCell(15).setCellValue("-");			
					sh.getRow(8+columnNames2.length+i).getCell(15).setCellStyle(getStyle3());
				}

			}
		}
		
		for (int i = 0; i < columnNames2.length; i++) {
			sh.createRow(3+i).createCell(1).setCellValue(columnNames2[i]);
			sh.setColumnWidth(1, 256 * 18);		
			sh.getRow(3+i).getCell(1).setCellStyle(getStyle1());		
		}
		sh.setColumnWidth(2, 256 * 18);
		sh.getRow(3).createCell(2).setCellValue(month+"月");
//		dfs_2.setRoundingMode(RoundingMode.HALF_UP); 
		BigDecimal bamt1= new BigDecimal(amt1);
		BigDecimal bamt2= new BigDecimal(amt2);
		BigDecimal bamt6= new BigDecimal(amt6);
		BigDecimal bamt3= new BigDecimal(amt3);
		BigDecimal bamt5= new BigDecimal(amt5);
		BigDecimal bamt7= new BigDecimal(amt7);
		BigDecimal bamt8= new BigDecimal(amt8);
		BigDecimal b1w = new BigDecimal(10000);
		BigDecimal b1h = new BigDecimal(100);
		amt1 = Double.valueOf(bamt1.divide(b1w).toString());
		amt2 = Double.valueOf(bamt2.divide(b1w).toString());
		amt6 = Double.valueOf(bamt6.divide(b1w).toString());
		amt3 = Double.valueOf(bamt3.divide(b1w).toString());
		amt5 = Double.valueOf(bamt5.divide(b1w).toString());
		amt7 = Double.valueOf(bamt7.divide(b1w).toString());
		amt8 = Double.valueOf(bamt8.divide(b1w).toString());
		sh.getRow(4).createCell(2).setCellValue(amt1);
		sh.getRow(5).createCell(2).setCellValue(num1);
		sh.getRow(6).createCell(2).setCellValue(amt2);
		sh.getRow(7).createCell(2).setCellValue(num2);
		
		 if(bamt7 ==null||bamt7.doubleValue()==0 ||amt7==0.0){
			sh.getRow(8).createCell(2).setCellValue("*");//新增比例
			sh.getRow(11).createCell(2).setCellValue("*");//当期比例
		}
		 else if(amt7.compareTo(new Double(0))==0){
		    sh.getRow(8).createCell(2).setCellValue("-");//新增比例
		    sh.getRow(11).createCell(2).setCellValue("-");//当期比例
		}else{
		    BigDecimal bamt9= new BigDecimal(amt2/amt7);
		    amt9 = Double.valueOf(bamt9.multiply(b1h).toString());
		    sh.getRow(8).createCell(2).setCellValue(amt9);//新增比例
		    BigDecimal bamt10= new BigDecimal(amt8/amt7);
		    amt10 = Double.valueOf(bamt10.multiply(b1h).toString());
		    sh.getRow(11).createCell(2).setCellValue(amt10);//当期比例
		}
		sh.getRow(9).createCell(2).setCellValue(amt8);
		sh.getRow(10).createCell(2).setCellValue(num4);
		sh.getRow(12).createCell(2).setCellValue(amt7);
		sh.getRow(13).createCell(2).setCellValue(num7);
						
		sh.getRow(3).getCell(2).setCellStyle(getStyle3());
		sh.getRow(4).getCell(2).setCellStyle(getStyle5());
		sh.getRow(5).getCell(2).setCellStyle(getStyle6());
		sh.getRow(6).getCell(2).setCellStyle(getStyle5());
		sh.getRow(7).getCell(2).setCellStyle(getStyle6());
		sh.getRow(8).getCell(2).setCellStyle(getStyle5());
		sh.getRow(9).getCell(2).setCellStyle(getStyle5());
		sh.getRow(10).getCell(2).setCellStyle(getStyle6());
		sh.getRow(11).getCell(2).setCellStyle(getStyle5());
		sh.getRow(12).getCell(2).setCellStyle(getStyle5());
		sh.getRow(13).getCell(2).setCellStyle(getStyle6());
		
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
	
	private CellStyle getStyle00(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_LEFT);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)14);
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
