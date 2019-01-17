package com.hpe.cmca.job;

import java.math.BigDecimal;
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

@Service("NotiFileZdtl3000TolProcessor")
public class NotiFileZdtl3000TolProcessor extends AbstractNotiFileProcessor{
	
	protected String focusCd = "3000";
	
	private int maxRows=0;
	
	protected List<String> getAudTrmListToSomeDate(String d1,String d2){
		List<String> dateList =new ArrayList<String>();
		String temp=d1;
		dateList.add(temp);
		while(!d2.equals(temp)){			
			temp = getLastMon(temp);
			dateList.add(temp);
			
		}
		return dateList;		
	}
	
	protected String getLastMon(String d1){
		Integer thisYear=Integer.parseInt(d1.substring(0,4));
		Integer thisMonth=Integer.parseInt("0".equals(d1.substring(4,5))?d1.substring(5,6):d1.substring(4,6));
		Integer retYear=thisYear;
		Integer retMonth = 0;
		if(thisMonth >= 2 ){
			retMonth = thisMonth - 1;
		}else if(thisMonth == 1 ){
			retMonth = 12;
			retYear=thisYear - 1 ;
		}
		String mon = retMonth.toString();
		StringBuffer sb = new StringBuffer(2);
		sb.append("0");
		if(retMonth<=9){
			sb.append(retMonth);
			mon = sb.toString();
		}				
		return retYear.toString()+mon;
	}
	//重写构建文件名方法
	protected String buildFileName() {
		fileName = "2015年10月-"+month.substring(0, 4)+"年"+("0".equals(month.substring(4,5))?month.substring(5,6):month.substring(4,6))+"月"+fileName + ".xlsx";
		return fileName;
	}
	
	public boolean generate() throws Exception{
		this.setFileName("社会渠道终端套利排名汇总(分关注点)");
		
		
		List<String> d = getAudTrmListToSomeDate(month,"201510");//获得201510至审计月中间的审计月列表
		//String aa = getEngChar('A',53);
		//aa="";
		sh = wb.createSheet("全公司审计结果摘要");
		int index=0;
		for(String mon :d ){			
			writeSheetTol(notiFileGenService.getNotiFile3000Data(mon, "3000"),sh,index,mon,d.size()*1);
			index=index+1;
		}
		
		sh = wb.createSheet("汇总排名");
		index=0;
		maxRows=0;
		for(String mon :d ){			
			writeSheet3000(notiFileGenService.getNotiFile3000Data(mon, "3000"),sh,index,mon,d.size()*13);
			index=index+13;
		}
		
		sh = wb.createSheet("沉默终端套利");
		index=0;
		maxRows=0;
		for(String mon :d ){
			
			writeSheet3001(notiFileGenService.getNotiFile3000Data(mon, "3001"),sh,index,mon,d.size()*6);
			index=index+6;
		}
		
		sh = wb.createSheet("养机套利");
		index=0;
		maxRows=0;
		for(String mon :d ){
			writeSheet3002(notiFileGenService.getNotiFile3000Data(mon, "3002"),sh,index,mon,d.size()*6);
			index=index+6;
		}
		
		sh = wb.createSheet("拆包套利");
		index=0;
		maxRows=0;
		for(String mon :d ){
			
			writeSheet3004(notiFileGenService.getNotiFile3000Data(mon, "3004"),sh,index,mon,d.size()*10);
			index=index+10;
		}
		
		sh = wb.createSheet("跨省窜货套利");
		index=0;	
		maxRows=0;
		for(String mon :d ){
			
			writeSheet3005(notiFileGenService.getNotiFile3000Data(mon, "3005"),sh,index,mon,d.size()*10);
			index=index+10;
		}
		return true;
	}
	public void writeSheetTol(List<Map<String, Object>> data,Sheet sh,int index,String mon,int maxCol){		
		if(index==0){
			for(int i = 0; i <= 12; i++) {
				sh.createRow(i);
			}
			sh.getRow(0).createCell(0).setCellValue("“社会渠道终端套利”全公司审计结果摘要");
			sh.getRow(0).getCell(0).setCellStyle(getStyle0());
			sh.addMergedRegion(new CellRangeAddress(0, 0, 0, maxCol+1));
			sh.getRow(0).setHeightInPoints(32);
			sh.getRow(1).createCell(0).setCellValue("");
			sh.getRow(1).getCell(0).setCellStyle(getStyle0());
			sh.addMergedRegion(new CellRangeAddress(1, 1, 0, maxCol+1));
			sh.getRow(1).setHeightInPoints(32);
			sh.getRow(2).createCell(0).setCellValue("");
			sh.getRow(2).getCell(0).setCellStyle(getStyle00());
			sh.addMergedRegion(new CellRangeAddress(2, 2, 0, maxCol+1));
			sh.getRow(2).setHeightInPoints(32);
			sh.getRow(3).createCell(0).setCellValue("社会渠道终端销售月份");
			sh.getRow(4).createCell(0).setCellValue("社会渠道终端销售数量");
			sh.getRow(5).createCell(0).setCellValue("终端销售渠道数量");
			sh.getRow(6).createCell(0).setCellValue("异常销售终端数量");
			sh.getRow(7).createCell(0).setCellValue("异常销售占比");
			sh.getRow(8).createCell(0).setCellValue("异常销售涉及渠道数量");
			sh.getRow(9).createCell(0).setCellValue("套利终端数量");
			sh.getRow(10).createCell(0).setCellValue("套利终端销售占比");
			sh.getRow(11).createCell(0).setCellValue("套利金额");
			sh.getRow(12).createCell(0).setCellValue("套利终端占异常终端数量比");
			for(int j=0;j<10;j++) {
				sh.getRow(3+j).getCell(0).setCellStyle(getStyle2());
				sh.getRow(3+j).setHeightInPoints(32);
			}
			sh.setColumnWidth(0, 256 * 50);
		}	
		
		sh.setColumnWidth(index+1, 256 * 14);
		
		Integer tol_num=0;
		Integer totalChnlQty=0;
		Integer weigui_num=0;
		Integer weigui_chnlqty=0;
		
		Integer taoli_num=0;
		Double infraction_sett_amt = 0.00;
		Integer taoli_chnlqty =0;
		
		Double weigui_per_cnt = 0.00;
		Double taoli_per_cnt= 0.00;
		Double taoli_weigui_per_cnt= 0.00;
		Map<String,Object> row=new HashMap<String,Object>();
		for(int i = 0,dataSize=data.size(); i <= dataSize; i++) {
			if (i < dataSize) {
				row = data.get(i);
				tol_num+=(Integer) row.get("tol_num");
				totalChnlQty+=(Integer) row.get("totalChnlQty");
				weigui_num+=(Integer) row.get("weigui_num");
				weigui_chnlqty+=(Integer) row.get("weigui_chnlqty");
				taoli_num+=(Integer) row.get("taoli_num");
				infraction_sett_amt+=row.get("infraction_sett_amt") instanceof BigDecimal?((BigDecimal)row.get("infraction_sett_amt")).doubleValue():  (Double) row.get("infraction_sett_amt");				 
				taoli_chnlqty+=(Integer) row.get("taoli_chnlqty");
			} else {
				if(tol_num!=0)
					weigui_per_cnt = weigui_num*1.0000/tol_num;
				if(tol_num!=0)
					taoli_per_cnt = taoli_num*1.0000/tol_num;
				if(weigui_num!=0)
					taoli_weigui_per_cnt = taoli_num*1.0000/weigui_num;
				
				sh.getRow(3).createCell(index+1).setCellValue(mon.substring(0, 4)+"年"+("0".equals(mon.substring(4,5))?mon.substring(5,6):mon.substring(4,6))+"月");
				sh.getRow(4).createCell(index+1).setCellValue(tol_num);
				sh.getRow(5).createCell(index+1).setCellValue(totalChnlQty);
				sh.getRow(6).createCell(index+1).setCellValue(weigui_num);
				if(weigui_per_cnt==0.00){
					sh.getRow(7).createCell(index+1).setCellValue("-");
					sh.getRow(7).getCell(index+1).setCellStyle(getStyle3());
				}else {
					sh.getRow(7).createCell(index+1).setCellValue(weigui_per_cnt);
				}
				
				sh.getRow(8).createCell(index+1).setCellValue(weigui_chnlqty);
				sh.getRow(9).createCell(index+1).setCellValue(taoli_num);
				if(taoli_per_cnt==0.00){
					sh.getRow(10).createCell(index+1).setCellValue("-");
					sh.getRow(10).getCell(index+1).setCellStyle(getStyle3());
				}else{
					sh.getRow(10).createCell(index+1).setCellValue(taoli_per_cnt);					
				}
				
				sh.getRow(11).createCell(index+1).setCellValue(infraction_sett_amt);
				if(taoli_weigui_per_cnt==0.00){
					sh.getRow(12).createCell(index+1).setCellValue("-");
					sh.getRow(12).getCell(index+1).setCellStyle(getStyle3());
				}else{
					sh.getRow(12).createCell(index+1).setCellValue(taoli_weigui_per_cnt);			
				}
			
				for(int j=0;j<10;j++) {
					sh.getRow(3+j).getCell(index+1).setCellStyle(getStyle6());
				}
				sh.getRow(3).getCell(index+1).setCellStyle(getStyle3());
				if(weigui_per_cnt!=0.00)sh.getRow(7).getCell(index+1).setCellStyle(getStyle4());
				if(taoli_per_cnt!=0.00)sh.getRow(10).getCell(index+1).setCellStyle(getStyle4());
				sh.getRow(11).getCell(index+1).setCellStyle(getStyle5());
				if(taoli_weigui_per_cnt!=0.00)sh.getRow(12).getCell(index+1).setCellStyle(getStyle4());
			}
			
		}
	}
	
	public void writeSheet3000(List<Map<String, Object>> data,Sheet sh,int index,String mon,int maxCol){		
		if(index==0){
			sh.createRow(0);
			sh.createRow(1);
			sh.createRow(2);
			for(int i = 0,dataSize=data.size(); i <= dataSize; i++) {
				sh.createRow(3+i);
			}
			sh.getRow(0).createCell(0).setCellValue("社会渠道终端异常销售及套利情况");
			sh.getRow(0).getCell(index+0).setCellStyle(getStyle00());
			sh.addMergedRegion(new CellRangeAddress(0, 0, 0, maxCol));
		}	
		
		sh.getRow(1).createCell(index+0).setCellValue(mon.substring(0, 4)+"年"+("0".equals(mon.substring(4,5))?mon.substring(5,6):mon.substring(4,6))+"月");
		sh.getRow(1).getCell(index+0).setCellStyle(getStyle0());		
		sh.addMergedRegion(new CellRangeAddress(1, 1, index+0, index+12)); 
		//sh.createRow(1);
		sh.getRow(2).createCell(index+0).setCellValue("省份");
		sh.getRow(2).createCell(index+1).setCellValue("终端销售数量");
		sh.getRow(2).createCell(index+2).setCellValue("终端销售渠道");
		sh.getRow(2).createCell(index+3).setCellValue("异常销售数量");
		sh.getRow(2).createCell(index+4).setCellValue("异常销售涉及渠道");
		sh.getRow(2).createCell(index+5).setCellValue("异常销售占比");
		sh.getRow(2).createCell(index+6).setCellValue("异常销售占比排名");
		sh.getRow(2).createCell(index+7).setCellValue("套利终端数量");
		sh.getRow(2).createCell(index+8).setCellValue("套利金额");
		sh.getRow(2).createCell(index+9).setCellValue("终端套利涉及渠道");
		sh.getRow(2).createCell(index+10).setCellValue("套利终端占销量比");
		sh.getRow(2).createCell(index+11).setCellValue("套利终端占异常终端比");
		sh.getRow(2).createCell(index+12).setCellValue("套利终端占异常终端比排名");
		//第一个参数表示要冻结的列数，这里只冻结行所以为0；
		//第二个参数表示要冻结的行数；
		//第三个参数表示右边区域可见的首列序号，从1开始计算，这里是冻结行，所以为0；
		//第四个参数表示下边区域可见的首行序号，也是从1开始计算；
		sh.createFreezePane(0, 3, 0, 3);
		for(int i=0;i<13; i++) {
			sh.setColumnWidth(index+i, 256 * 12);
			if((index/13)%2==0)
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle2());
			else
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle2_green());
		}
		sh.setColumnWidth(index+11, 256 * 14);
		sh.setColumnWidth(index+12, 256 * 16);
		sh.getRow(2).setHeightInPoints(32);

		Map<String,Object> row=new HashMap<String,Object>();
		for(int i = 0,dataSize=data.size(); i <= dataSize; i++) {
			//sh.createRow(2+i);
			if (i < dataSize) {
				row = data.get(i);
				sh.getRow(3+i).createCell(index+0).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
				sh.getRow(3+i).createCell(index+1).setCellValue((Integer) row.get("tol_num"));
				sh.getRow(3+i).createCell(index+2).setCellValue((Integer) row.get("totalChnlQty"));
				sh.getRow(3+i).createCell(index+3).setCellValue((Integer) row.get("weigui_num"));
				sh.getRow(3+i).createCell(index+4).setCellValue((Integer) row.get("weigui_chnlqty"));
				
				sh.getRow(3+i).createCell(index+5).setCellValue((row.get("weigui_per_cnt") instanceof BigDecimal? ((BigDecimal) row.get("weigui_per_cnt")).doubleValue() :(Double) row.get("weigui_per_cnt")));
				sh.getRow(3+i).createCell(index+6).setCellValue((Integer) row.get("weigui_order"));
				sh.getRow(3+i).createCell(index+7).setCellValue((Integer) row.get("taoli_num"));
				sh.getRow(3+i).createCell(index+8).setCellValue(row.get("infraction_sett_amt") instanceof BigDecimal?((BigDecimal)row.get("infraction_sett_amt")).doubleValue():  (Double) row.get("infraction_sett_amt"));
				 
				sh.getRow(3+i).createCell(index+9).setCellValue((Integer) row.get("taoli_chnlqty"));
				sh.getRow(3+i).createCell(index+10).setCellValue(row.get("taoli_per_cnt") instanceof BigDecimal?((BigDecimal)row.get("taoli_per_cnt")).doubleValue():  (Double) row.get("taoli_per_cnt"));
				sh.getRow(3+i).createCell(index+11).setCellValue(row.get("taoli_weigui_per_cnt") instanceof BigDecimal?((BigDecimal)row.get("taoli_weigui_per_cnt")).doubleValue():  (Double) row.get("taoli_weigui_per_cnt"));
				// modified by GuoXY 20161024 for CMCC_集中化持续审计系统_软件需求规格说明书_社会渠道终端套利专题的逻辑和变更说明v1.7 郭倩
				//sh.getRow(2+i).createCell(12).setCellValue(((Integer) row.get("taoli_num") != null && (Integer) row.get("taoli_num") == 0) ? '-' : (Integer) row.get("taoli_weigui_order") );
				if ((Integer) row.get("taoli_num") != null && (Integer) row.get("taoli_num") == 0){
					sh.getRow(3+i).createCell(index+12).setCellValue("-");
					sh.getRow(3+i).getCell(index+12).setCellStyle(getStyle7());
				}else{
					sh.getRow(3+i).createCell(index+12).setCellValue((Integer) row.get("taoli_weigui_order"));
					sh.getRow(3+i).getCell(index+12).setCellStyle(getStyle6());
				}
			} else {
				sh.getRow(3+i).createCell(index+0).setCellValue("合计");
				sh.getRow(3+i).createCell(index+1).setCellFormula("SUM("+getEngChar('B',index)+"3:"+getEngChar('B',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+2).setCellFormula("SUM("+getEngChar('C',index)+"3:"+getEngChar('C',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+3).setCellFormula("SUM("+getEngChar('D',index)+"3:"+getEngChar('D',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+4).setCellFormula("SUM("+getEngChar('E',index)+"3:"+getEngChar('E',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+5).setCellFormula(getEngChar('D',index) + (i+4) + "/"+getEngChar('B',index)+ (i+4));
				sh.getRow(3+i).createCell(index+6);
				sh.getRow(3+i).createCell(index+7).setCellFormula("SUM("+getEngChar('H',index)+"3:"+getEngChar('H',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+8).setCellFormula("SUM("+getEngChar('I',index)+"3:"+getEngChar('I',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+9).setCellFormula("SUM("+getEngChar('J',index)+"3:"+getEngChar('J',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+10).setCellFormula(getEngChar('H',index) + (i+4) + "/"+getEngChar('B',index)+ (i+4));
				sh.getRow(3+i).createCell(index+11).setCellFormula(getEngChar('H',index) + (i+4) + "/"+getEngChar('D',index)+ (i+4));
				sh.getRow(3+i).createCell(index+12);
			}
			for(int j=0;j<13;j++) {
				sh.getRow(3+i).getCell(index+j).setCellStyle(getStyle6());
			}
			sh.getRow(3+i).getCell(index+0).setCellStyle(getStyle3());
			sh.getRow(3+i).getCell(index+8).setCellStyle(getStyle5());
			sh.getRow(3+i).getCell(index+5).setCellStyle(getStyle4());
			sh.getRow(3+i).getCell(index+10).setCellStyle(getStyle4());
			sh.getRow(3+i).getCell(index+11).setCellStyle(getStyle4());
		}
	}
	
	public void writeSheetCommon(List<Map<String, Object>> data, String sheetName, String title, String[] columnNames,Sheet sh,int index,String mon,int maxCol){
		if(index==0){
			sh.createRow(0);
			sh.createRow(1);
			sh.createRow(2);
			for(int i = 0; i <= data.size(); i++) {
				sh.createRow(3+i);
			}
			sh.getRow(0).createCell(0).setCellValue(title);
			sh.getRow(0).getCell(index+0).setCellStyle(getStyle00());
			sh.addMergedRegion(new CellRangeAddress(0, 0, 0, maxCol));
			maxRows=data.size();
		}
		if(maxRows<data.size()){
			for(int i = maxRows+1; i <= data.size(); i++) {
				sh.createRow(3+i);
			}
			maxRows=data.size();
		}				
		// 第一步：创建sheet页标题行内容、样式、合并单元格
		sh.getRow(1).createCell(index+0).setCellValue(mon.substring(0, 4)+"年"+("0".equals(mon.substring(4,5))?mon.substring(5,6):mon.substring(4,6))+"月");
		sh.getRow(1).getCell(index+0).setCellStyle(getStyle0());		
		sh.addMergedRegion(new CellRangeAddress(1, 1, index+0, index+9)); 
		
		for (int i = 0,columnLen= columnNames.length; i < columnLen; i++) {
			sh.getRow(2).createCell(index+i).setCellValue(columnNames[i]);
			sh.setColumnWidth(index+i, 256 * 12);
			if (i == 0 || i == 1 || i == 2 || i == 6 || i == 8) {
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle1());
			} else {
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle2());
			}
		}
		sh.setColumnWidth(index+3, 256 * 14);
		sh.setColumnWidth(index+5, 256 * 16);
		sh.setColumnWidth(index+6, 256 * 14);
		sh.setColumnWidth(index+7, 256 * 20);
		sh.setColumnWidth(index+8, 256 * 14);
		sh.setColumnWidth(index+9, 256 * 16);
		sh.getRow(2).setHeightInPoints(32);
		sh.createFreezePane(0, 3, 0, 3);
		for(int i=0;i<10; i++) {
			sh.setColumnWidth(index+i, 256 * 12);
			if((index/10)%2==0)
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle2());
			else
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle2_green());
		}
		int tolNum = 0;
		Map<String, Object> row=new HashMap<String,Object>();
		for(int i = 0,dataSize=data.size(); i <= dataSize; i++) {
			//sh.createRow(2+i);
			if (i < dataSize) {
				row = data.get(i);
				sh.getRow(3+i).createCell(index+0).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
				sh.getRow(3+i).createCell(index+1).setCellValue((Integer) row.get("weigui_num"));
				sh.getRow(3+i).createCell(index+2).setCellValue((Integer) row.get("weigui_chnlqty"));
				sh.getRow(3+i).createCell(index+3).setCellValue((Integer) row.get("taoli_num"));
				sh.getRow(3+i).createCell(index+4).setCellValue(row.get("infraction_sett_amt") instanceof BigDecimal ? ((BigDecimal) row.get("infraction_sett_amt")).doubleValue():(Double) row.get("infraction_sett_amt"));
				sh.getRow(3+i).createCell(index+5).setCellValue((Integer) row.get("taoli_chnlqty"));
				sh.getRow(3+i).createCell(index+6).setCellValue(row.get("weigui_per_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("weigui_per_cnt")).doubleValue():(Double) row.get("weigui_per_cnt"));
				//(Double) row.get("weigui_per_cnt"));
				sh.getRow(3+i).createCell(index+7).setCellValue(row.get("taoli_weigui_per_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_weigui_per_cnt")).doubleValue():(Double) row.get("taoli_weigui_per_cnt"));
				//(Double) row.get("taoli_weigui_per_cnt"));
				sh.getRow(3+i).createCell(index+8).setCellValue((Integer) row.get("weigui_order"));
				// modified by GuoXY 20161024 for CMCC_集中化持续审计系统_软件需求规格说明书_社会渠道终端套利专题的逻辑和变更说明v1.7 郭倩
				if ((Integer) row.get("taoli_num") != null && (Integer) row.get("taoli_num") == 0){
					sh.getRow(3+i).createCell(index+9).setCellValue("-");
					sh.getRow(3+i).getCell(index+9).setCellStyle(getStyle7());
				}else{
					sh.getRow(3+i).createCell(index+9).setCellValue((Integer) row.get("taoli_order"));
					sh.getRow(3+i).getCell(index+9).setCellStyle(getStyle6());
				}
				tolNum += (Integer) row.get("tol_num");
			} else {
				sh.getRow(3+i).createCell(index+0).setCellValue("合计");
				sh.getRow(3+i).createCell(index+1).setCellFormula("SUM("+getEngChar('B',index)+"3:"+getEngChar('B',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+2).setCellFormula("SUM("+getEngChar('C',index)+"3:"+getEngChar('C',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+3).setCellFormula("SUM("+getEngChar('D',index)+"3:"+getEngChar('D',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+4).setCellFormula("SUM("+getEngChar('E',index)+"3:"+getEngChar('E',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+5).setCellFormula("SUM("+getEngChar('F',index)+"3:"+getEngChar('F',index)+(i+3) + ")");
	
				if("3000".equals(this.focusCd)) {
					sh.getRow(3+i).createCell(index+6).setCellFormula(getEngChar('B',index) + (i+4) +"/"+"汇总排名!"+getEngChar('B',index+3)+35);
				} else {
					sh.getRow(3+i).createCell(index+6).setCellFormula(getEngChar('B',index) + (i+4) +"/" + tolNum);
				}
				sh.getRow(3+i).createCell(index+7).setCellFormula(getEngChar('D',index) + (i+4) + "/"+getEngChar('B',index)+ (i+4));
				sh.getRow(3+i).createCell(index+8);
				sh.getRow(3+i).createCell(index+9);
				sh.getRow(3+i).getCell(index+9).setCellStyle(getStyle6());
			}
			// 排名列因为有两种格式数据，所以单独设置 modified by GuoXY 20161024
			for(int j=0;j<9;j++) {
				sh.getRow(3+i).getCell(index+j).setCellStyle(getStyle6());
			}
			sh.getRow(3+i).getCell(index+0).setCellStyle(getStyle3());
			sh.getRow(3+i).getCell(index+6).setCellStyle(getStyle4());
			sh.getRow(3+i).getCell(index+7).setCellStyle(getStyle4());
			sh.getRow(3+i).getCell(index+4).setCellStyle(getStyle5());
		}
	}
	
	// add by GuoXY 20161024 for CMCC_集中化持续审计系统_软件需求规格说明书_社会渠道终端套利专题的逻辑和变更说明v1.7 郭倩
	public void writeSheetCommon3001(List<Map<String, Object>> data, String sheetName, String title, String[] columnNames,Sheet sh,int index,String mon,int maxCol){

		if(index==0){
			sh.createRow(0);
			sh.createRow(1);
			sh.createRow(2);
			for(int i = 0; i <= data.size(); i++) {
				sh.createRow(3+i);
			}
			sh.getRow(0).createCell(0).setCellValue(title);
			sh.getRow(0).getCell(index+0).setCellStyle(getStyle00());
			sh.addMergedRegion(new CellRangeAddress(0, 0, 0, maxCol));
			maxRows=data.size();
		}
		if(maxRows<data.size()){
			for(int i = maxRows+1; i <= data.size(); i++) {
				sh.createRow(3+i);
			}
			maxRows=data.size();
		}	
		//sh = wb.createSheet(sheetName);		
		// 第一步：创建sheet页标题行内容、样式、合并单元格
		sh.getRow(1).createCell(index+0).setCellValue(mon.substring(0, 4)+"年"+("0".equals(mon.substring(4,5))?mon.substring(5,6):mon.substring(4,6))+"月");
		sh.getRow(1).getCell(index+0).setCellStyle(getStyle0());		
		sh.addMergedRegion(new CellRangeAddress(1, 1, index+0, index+5)); 
		
		// 第二步：创建标题行每个单元格的内容、宽度、样式、列宽(根据标题内容长度)、行高、锁定标题行
		for (int i = 0,columnLen=columnNames.length; i < columnLen; i++) {
			sh.getRow(2).createCell(index+i).setCellValue(columnNames[i]);
			sh.setColumnWidth(index+i, 256 * 12);
			//sh.getRow(2).getCell(index+i).setCellStyle(getStyle1());
		}
		for(int i=0;i<6; i++) {
			sh.setColumnWidth(index+i, 256 * 12);
			if((index/6)%2==0)
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle2());
			else
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle2_green());
		}
		sh.setColumnWidth(index+1, 256 * 14);
		sh.setColumnWidth(index+3, 256 * 16);
		sh.setColumnWidth(index+4, 256 * 14);
		sh.getRow(2).setHeightInPoints(32);
		sh.createFreezePane(0, 3, 0, 3);
		
		// 第三步：填充表格内容部分
		int sumWG = 0;
		Map<String, Object> row=new HashMap<String,Object>();
		for(int i = 0,dataSize=data.size(); i <= dataSize; i++) {
			//sh.createRow(2+i);
			if (i < dataSize) { // 明细行
				row = data.get(i);
				sh.getRow(3+i).createCell(index+0).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
				sumWG += (Integer) row.get("tol_num");
				sh.getRow(3+i).createCell(index+1).setCellValue((Integer) row.get("taoli_num"));
				sh.getRow(3+i).createCell(index+2).setCellValue(row.get("infraction_sett_amt") instanceof BigDecimal ? ((BigDecimal) row.get("infraction_sett_amt")).doubleValue():(Double) row.get("infraction_sett_amt"));
				sh.getRow(3+i).createCell(index+3).setCellValue((Integer) row.get("taoli_chnlqty"));
				// 郭倩说将沉默套利终端占比 = 沉默套利终端数量 / 终端销售数量 20161125 modified by GuoXY 
				//sh.getRow(2+i).createCell(4).setCellValue(row.get("taoli_weigui_per_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_weigui_per_cnt")).doubleValue():(Double) row.get("taoli_weigui_per_cnt"));
				double res = (Double) ((0==(Integer) row.get("tol_num")) ? 0 : ((Integer)row.get("taoli_num")).doubleValue() / ((Integer)row.get("tol_num")).doubleValue());
				sh.getRow(3+i).createCell(index+4).setCellValue( res );
				
				if ((Integer) row.get("taoli_num") != null && (Integer) row.get("taoli_num") == 0){
					sh.getRow(3+i).createCell(index+5).setCellValue("-");
					sh.getRow(3+i).getCell(index+5).setCellStyle(getStyle7());
				}else{
					sh.getRow(3+i).createCell(index+5).setCellValue((Integer) row.get("taoli_order"));
					sh.getRow(3+i).getCell(index+5).setCellStyle(getStyle6());
				}
			} else { // 合计行
				sh.getRow(3+i).createCell(index+0).setCellValue("合计");
				sh.getRow(3+i).createCell(index+1).setCellFormula("SUM("+getEngChar('B',index)+"3:"+getEngChar('B',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+2).setCellFormula("SUM("+getEngChar('C',index)+"3:"+getEngChar('C',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+3).setCellFormula("SUM("+getEngChar('D',index)+"3:"+getEngChar('D',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+4).setCellFormula(getEngChar('B',index) + (i+4) + "/" + sumWG);
				sh.getRow(3+i).createCell(index+5);
				sh.getRow(3+i).getCell(index+5).setCellStyle(getStyle3());
			}
			// 设置每个单元格的文字格式
			sh.getRow(3+i).getCell(index+0).setCellStyle(getStyle3());
			sh.getRow(3+i).getCell(index+1).setCellStyle(getStyle6());
			sh.getRow(3+i).getCell(index+2).setCellStyle(getStyle5());
			sh.getRow(3+i).getCell(index+3).setCellStyle(getStyle6());
			sh.getRow(3+i).getCell(index+4).setCellStyle(getStyle4());		
		}
	}
	
	// add by PXL 20161228 for CMCC_集中化持续审计系统_软件需求规格说明书_社会渠道终端套利专题的逻辑和变更说明v2.0 郭倩
	public void writeSheetCommon3002(List<Map<String, Object>> data, String sheetName, String title, String[] columnNames,Sheet sh,int index,String mon,int maxCol){
		int dataSize=data.size();
	    	if(index==0){
			sh.createRow(0);
			sh.createRow(1);
			sh.createRow(2);
			for(int i = 0; i <= dataSize; i++) {
				sh.createRow(3+i);
			}
			sh.getRow(0).createCell(0).setCellValue(title);
			sh.getRow(0).getCell(index+0).setCellStyle(getStyle00());
			sh.addMergedRegion(new CellRangeAddress(0, 0, 0, maxCol));			
			maxRows=data.size();
		}
		if(maxRows<dataSize){
			for(int i = maxRows+1; i <= dataSize; i++) {
				sh.createRow(3+i);
			}
			maxRows=data.size();
		}
		//sh = wb.createSheet(sheetName);		
		// 第一步：创建sheet页标题行内容、样式、合并单元格
		sh.getRow(1).createCell(index+0).setCellValue(mon.substring(0, 4)+"年"+("0".equals(mon.substring(4,5))?mon.substring(5,6):mon.substring(4,6))+"月");
		sh.getRow(1).getCell(index+0).setCellStyle(getStyle0());		
		sh.addMergedRegion(new CellRangeAddress(1, 1, index+0, index+5)); 
		
		// 第二步：创建标题行每个单元格的内容、宽度、样式、列宽(根据标题内容长度)、行高、锁定标题行
		for (int i = 0; i < columnNames.length; i++) {
			sh.getRow(2).createCell(index+i).setCellValue(columnNames[i]);
			sh.setColumnWidth(index+i, 256 * 12);
			//sh.getRow(2).getCell(index+i).setCellStyle(getStyle1());
		}
		for(int i=0;i<6; i++) {
			sh.setColumnWidth(index+i, 256 * 12);
			if((index/6)%2==0)
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle2());
			else
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle2_green());
		}
		sh.setColumnWidth(index+1, 256 * 14);
		sh.setColumnWidth(index+3, 256 * 16);
		sh.setColumnWidth(index+4, 256 * 14);
		sh.getRow(2).setHeightInPoints(32);
		sh.createFreezePane(0, 3, 0, 3);
		
		// 第三步：填充表格内容部分
		int sumWG = 0;
		Map<String,Object> row=new HashMap<String,Object>();
		for(int i = 0; i <= dataSize; i++) {
			//sh.createRow(2+i);
			if (i < dataSize) { // 明细行
				row = data.get(i);
				sh.getRow(3+i).createCell(index+0).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
				sumWG += (Integer) row.get("tol_num");
				sh.getRow(3+i).createCell(index+1).setCellValue((Integer) row.get("taoli_num"));
				sh.getRow(3+i).createCell(index+2).setCellValue(row.get("infraction_sett_amt") instanceof BigDecimal ? ((BigDecimal) row.get("infraction_sett_amt")).doubleValue():(Double) row.get("infraction_sett_amt"));
				sh.getRow(3+i).createCell(index+3).setCellValue((Integer) row.get("taoli_chnlqty"));
				// 郭倩说养机套利终端占总销量比=养机套利终端数量/终端总销售数量 20161228 modified by PXL
				//sh.getRow(2+i).createCell(4).setCellValue(row.get("taoli_weigui_per_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_weigui_per_cnt")).doubleValue():(Double) row.get("taoli_weigui_per_cnt"));
				double res = (Double) ((0==(Integer) row.get("tol_num")) ? 0 : ((Integer)row.get("taoli_num")).doubleValue() / ((Integer)row.get("tol_num")).doubleValue());
				sh.getRow(3+i).createCell(index+4).setCellValue( res );
				
				if ((Integer) row.get("taoli_num") != null && (Integer) row.get("taoli_num") == 0){
					sh.getRow(3+i).createCell(index+5).setCellValue("-");
					sh.getRow(3+i).getCell(index+5).setCellStyle(getStyle7());
				}else{
					sh.getRow(3+i).createCell(index+5).setCellValue((Integer) row.get("taoli_order"));
					sh.getRow(3+i).getCell(index+5).setCellStyle(getStyle6());
				}
			} else { // 合计行
				sh.getRow(3+i).createCell(index+0).setCellValue("合计");
				sh.getRow(3+i).createCell(index+1).setCellFormula("SUM("+getEngChar('B',index)+"3:"+getEngChar('B',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+2).setCellFormula("SUM("+getEngChar('C',index)+"3:"+getEngChar('C',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+3).setCellFormula("SUM("+getEngChar('D',index)+"3:"+getEngChar('D',index)+(i+3) + ")");
				sh.getRow(3+i).createCell(index+4).setCellFormula(getEngChar('B',index) + (i+4) + "/" + sumWG);
				sh.getRow(3+i).createCell(index+5);
				sh.getRow(3+i).getCell(index+5).setCellStyle(getStyle3());
			}
			// 设置每个单元格的文字格式
			sh.getRow(3+i).getCell(index+0).setCellStyle(getStyle3());
			sh.getRow(3+i).getCell(index+1).setCellStyle(getStyle6());
			sh.getRow(3+i).getCell(index+2).setCellStyle(getStyle5());
			sh.getRow(3+i).getCell(index+3).setCellStyle(getStyle6());
			sh.getRow(3+i).getCell(index+4).setCellStyle(getStyle4());		
		}
	}
	
	public void writeSheet3001(List<Map<String, Object>> data,Sheet sh,int index,String mon,int maxCol){
		// modified by GuoXY 20161024 for CMCC_集中化持续审计系统_软件需求规格说明书_社会渠道终端套利专题的逻辑和变更说明v1.7 郭倩
//		String[] columnNames = {"省份名称","沉默终端数量","沉默终端涉及渠道","沉默套利终端数量","套利金额","沉默套利涉及渠道数量","沉默终端占总销量比","沉默套利终端占沉默终端比","沉默终端占比排名","沉默套利终端占比排名"};
//		writeSheetCommon(data, "沉默终端套利", "社会渠道沉默终端及套利情况", columnNames);
		String[] columnNames = {"省份名称",                         "沉默套利终端数量","套利金额","沉默套利涉及渠道数量",               "沉默套利终端占比",             "沉默套利终端占比排名"};
		writeSheetCommon3001(data, "沉默终端套利", "社会渠道沉默终端及套利情况", columnNames,sh,index,mon,maxCol);
	}
	
	public void writeSheet3002(List<Map<String, Object>> data,Sheet sh,int index,String mon,int maxCol){
		//String[] columnNames = {"省份名称","养机数量","养机涉及渠道数量","养机套利终端数量","套利金额","养机套利涉及渠道数量","养机占总销量比","养机套利终端占养机终端比","养机占比排名","养机套利终端占比排名"};
		//writeSheetCommon(data, "养机套利", "社会渠道养机及套利情况", columnNames);
		String[] columnNames = {"省份名称",                          "养机套利终端数量","套利金额","养机套利涉及渠道数量",               "养机套利终端占总销量比",        "养机套利终端占比排名"};
		writeSheetCommon3002(data, "养机套利", "社会渠道养机套利情况", columnNames,sh,index,mon,maxCol);
	}
	
	public void writeSheet3004(List<Map<String, Object>> data,Sheet sh,int index,String mon,int maxCol){
		String[] columnNames = {"省份名称","拆包数量","拆包涉及渠道数量","拆包套利终端数量","	套利金额","拆包套利涉及渠道数量","拆包占总销量比","拆包套利终端占拆包终端比","终端拆包占比排名","拆包套利终端占比排名"};
		writeSheetCommon(data, "拆包套利", "社会渠道终端拆包及套利情况", columnNames,sh,index,mon,maxCol);
	}
	
	public void writeSheet3005(List<Map<String, Object>> data,Sheet sh,int index,String mon,int maxCol){
		String[] columnNames = {"省份名称","跨省窜货数量","跨省窜货渠道数量","跨省窜货套利终端数量","套利金额","跨省窜货套利渠道数量","跨省窜货占总销量比","跨省窜货套利终端占跨省窜货终端比","终端跨省窜货占比排名","跨省窜货套利终端占比排名"};
		writeSheetCommon(data, "跨省窜货套利", "社会渠道终端跨省窜货及套利情况", columnNames,sh,index,mon,maxCol);
	}
	
	private CellStyle getStyle00(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_LEFT);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)16);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		style.setFont(font);
		return style;
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
	private CellStyle getStyle2_green(){
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
		style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
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
	private String getEngChar(char begin,int distince){
		char prefix = 0;
		char value = 0;
		int ascii_begin = (int)begin;
		int ascii_end = ascii_begin+distince;
		int first_int = (ascii_end-65)/26+64;
		int second_int = (ascii_end-65)%26+65;
		
		if(first_int>=65&&first_int<=90)
			prefix = (char)first_int;
		
		if(second_int>=65&&second_int<=90)
			value = (char)second_int;
		
		
		return (String.valueOf(prefix)+String.valueOf(value)).trim();
		
	}
	

	public String getFocusCd() {
		return focusCd;
	}

	public void setFocusCd(String focusCd) {
		this.focusCd = focusCd;
	}
	
	
}
