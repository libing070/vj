package com.hpe.cmca.job;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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

import com.asiainfo.biframe.utils.json.JsonUtil;

@Service("NotiFileQdyk2000TolProcessor")
public class NotiFileQdyk2000TolProcessor extends AbstractNotiFileProcessor {
	
	protected String focusCd = "2000";
	
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
			fileName = fileName + "-" + month + ".xlsx";
			return fileName;		
	}

	@Override
	public boolean generate() throws Exception{
		this.setFileName("渠道养卡排名(全渠道)");
		if(Integer.parseInt(month)<201507){
		    System.out.print(month+"的渠道养卡排名汇总不具备生成条件");
		    return false;
		}
		List<String> d = getAudTrmListToSomeDate(month,"201507");//获得201507至审计月中间的审计月列表

		sh = wb.createSheet("各省疑似养卡号码数量排名前三地市_"+month);				
		int index=0;
		for(String mon :d ){			
			writeFirstPart(notiFileGenService.getNotiFile2000Tol(mon, "NumTop3Cty"),sh,index,mon,d.size()*3);
			index=index+3;
		}

		
		sh = wb.createSheet("各省疑似养卡号码占比排名前三地市_"+month);				
		index=0;
		for(String mon :d ){			
			writeSecondPart(notiFileGenService.getNotiFile2000Tol(mon, "PerTop3Cty"),sh,index,mon,d.size()*3);
			index=index+3;
		}
		
		sh = wb.createSheet("各省疑似养卡号码数量排名前十渠道_"+month);				
		index=0;
		for(String mon :d ){			
			writeThirdPart(notiFileGenService.getNotiFile2000Tol(mon, "NumTop10Chnl"),sh,index,mon,d.size()*4);
			index=index+4;
		}
		return true;
	}
	public void writeFirstPart(List<Map<String, Object>> data,Sheet sh,int index,String mon,int maxCol){		
		if(index==0){
			for(int i = 0; i <= 95; i++) {
				sh.createRow(i);
			}
			sh.getRow(0).createCell(0).setCellValue("各省疑似养卡号码数量排名前三地市");
			sh.getRow(0).getCell(0).setCellStyle(getStyle00());
			sh.addMergedRegion(new CellRangeAddress(0, 0, 0, maxCol));		
			sh.getRow(1).createCell(1).setCellValue("");
			sh.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));		
			sh.getRow(2).createCell(1).setCellValue("序号");
			sh.getRow(2).getCell(1).setCellStyle(getStyle2_green());
			sh.getRow(2).createCell(2).setCellValue("省份");
			sh.getRow(2).getCell(2).setCellStyle(getStyle2_green());
		}
		sh.getRow(1).createCell(index+3).setCellValue(mon.substring(0, 4)+"年"+("0".equals(mon.substring(4,5))?mon.substring(5,6):mon.substring(4,6))+"月");
		sh.getRow(1).getCell(index+3).setCellStyle(getStyle0());		
		sh.addMergedRegion(new CellRangeAddress(1, 1, index+3, index+5)); 

		sh.getRow(2).createCell(index+3).setCellValue("地市");
		sh.getRow(2).createCell(index+4).setCellValue("养卡号码数量");
		sh.getRow(2).createCell(index+5).setCellValue("占比");
		//第一个参数表示要冻结的列数，这里只冻结行所以为0；
		//第二个参数表示要冻结的行数；
		//第三个参数表示右边区域可见的首列序号，从1开始计算，这里是冻结行，所以为0；
		//第四个参数表示下边区域可见的首行序号，也是从1开始计算；
		sh.createFreezePane(0, 3, 0, 3);
		for(int i=3;i<=5; i++) {
			sh.setColumnWidth(index+i, 256 * 12);
			if((index/3)%2==0)
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle2());
			else
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle2_green());
		}
		List<Map<String, Object>> resultTp = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> mapTp:notiFileGenService.getAllPrvd()){
			resultTp.add(mapTp);
			resultTp.add(mapTp);
			resultTp.add(mapTp);
		}

		int ii=0;
		for(Map<String, Object> mapTp1:resultTp){
			if(ii>=data.size()){
				Map<String, Object> tpMap = new HashMap<String, Object>();
				tpMap.put("prvd_id", (Integer)mapTp1.get("prvd_id"));
				tpMap.put("prvd_name", (String)mapTp1.get("prvd_name"));
				tpMap.put("cty_name_short", null);				
				tpMap.put("qdyk_num_perc", null);
				tpMap.put("qdyk_subs_num", null);
				data.add(tpMap);
			}else{
				int prvdId1 = (Integer)mapTp1.get("prvd_id");
				int prvdId2 = (Integer)data.get(ii).get("prvd_id");
				if(prvdId1 != prvdId2){
					Map<String, Object> tpMap = new HashMap<String, Object>();
					tpMap.put("prvd_id", prvdId1);
					tpMap.put("prvd_name", (String)mapTp1.get("prvd_name"));
					tpMap.put("cty_name_short", null);
					tpMap.put("qdyk_num_perc", null);
					tpMap.put("qdyk_subs_num", null);
					data.add(ii, tpMap);
				}
			}
				ii++;
			
		}
		
		for(int i = 0; i < data.size(); i++) {			
				Map<String, Object> row = data.get(i);
				
				if(index==0){
					sh.getRow(3+i).createCell(1).setCellValue(String.valueOf(i/3+1));
					sh.getRow(3+i).getCell(1).setCellStyle(getStyle3());
					sh.getRow(3+i).createCell(2).setCellValue((String) row.get("prvd_name"));	
					sh.getRow(3+i).getCell(2).setCellStyle(getStyle3());
					if(i%3==0){
						sh.addMergedRegion(new CellRangeAddress(3+i, 3+i+2, 1, 1));
						sh.addMergedRegion(new CellRangeAddress(3+i, 3+i+2, 2, 2));
					}

				}				
				if(row.get("cty_name_short")!= null ){
					sh.getRow(3+i).createCell(index+3).setCellValue(((String) row.get("cty_name_short")).replaceAll("\\s*", ""));
					sh.getRow(3+i).getCell(index+3).setCellStyle(getStyle3());				
				}else{
					sh.getRow(3+i).createCell(index+3).setCellValue("-");
					sh.getRow(3+i).getCell(index+3).setCellStyle(getStyle3());								
				}
				if(row.get("qdyk_subs_num")!= null ){
					sh.getRow(3+i).createCell(index+4).setCellValue((row.get("qdyk_subs_num") instanceof BigDecimal? ((BigDecimal) row.get("qdyk_subs_num")).doubleValue() :(Double) row.get("qdyk_subs_num")));				
					sh.getRow(3+i).getCell(index+4).setCellStyle(getStyle5());
				}else{
					sh.getRow(3+i).createCell(index+4).setCellValue("-");				
					sh.getRow(3+i).getCell(index+4).setCellStyle(getStyle3());
				}
				if(row.get("qdyk_num_perc")!= null ){
					sh.getRow(3+i).createCell(index+5).setCellValue((row.get("qdyk_num_perc") instanceof BigDecimal? ((BigDecimal) row.get("qdyk_num_perc")).doubleValue() :(Double) row.get("qdyk_num_perc")));
					sh.getRow(3+i).getCell(index+5).setCellStyle(getStyle4());	
				}else{
					sh.getRow(3+i).createCell(index+5).setCellValue("-");
					sh.getRow(3+i).getCell(index+5).setCellStyle(getStyle3());	
				}					
		}		
	}
	
	public void writeSecondPart(List<Map<String, Object>> data,Sheet sh,int index,String mon,int maxCol){		
		if(index==0){
			for(int i = 0; i <= 95; i++) {
				sh.createRow(i);
			}
			sh.getRow(0).createCell(0).setCellValue("各省疑似养卡号码占比排名前三地市");
			sh.getRow(0).getCell(0).setCellStyle(getStyle00());
			sh.addMergedRegion(new CellRangeAddress(0, 0, 0, maxCol));		
			sh.getRow(1).createCell(1).setCellValue("");
			sh.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));		
			sh.getRow(2).createCell(1).setCellValue("序号");
			sh.getRow(2).getCell(1).setCellStyle(getStyle2_green());
			sh.getRow(2).createCell(2).setCellValue("省份");
			sh.getRow(2).getCell(2).setCellStyle(getStyle2_green());
		}
		sh.getRow(1).createCell(index+3).setCellValue(mon.substring(0, 4)+"年"+("0".equals(mon.substring(4,5))?mon.substring(5,6):mon.substring(4,6))+"月");
		sh.getRow(1).getCell(index+3).setCellStyle(getStyle0());		
		sh.addMergedRegion(new CellRangeAddress(1, 1, index+3, index+5)); 

		sh.getRow(2).createCell(index+3).setCellValue("地市");
		sh.getRow(2).createCell(index+4).setCellValue("占比");
		sh.getRow(2).createCell(index+5).setCellValue("养卡号码数量");
		
		//第一个参数表示要冻结的列数，这里只冻结行所以为0；
		//第二个参数表示要冻结的行数；
		//第三个参数表示右边区域可见的首列序号，从1开始计算，这里是冻结行，所以为0；
		//第四个参数表示下边区域可见的首行序号，也是从1开始计算；
		sh.createFreezePane(0, 3, 0, 3);
		for(int i=3;i<=5; i++) {
			sh.setColumnWidth(index+i, 256 * 12);
			if((index/3)%2==0)
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle2());
			else
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle2_green());
		}
		List<Map<String, Object>> resultTp = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> mapTp:notiFileGenService.getAllPrvd()){
			resultTp.add(mapTp);
			resultTp.add(mapTp);
			resultTp.add(mapTp);
		}

		int ii=0;
		for(Map<String, Object> mapTp1:resultTp){
			if(ii>=data.size()){
				Map<String, Object> tpMap = new HashMap<String, Object>();
				tpMap.put("prvd_id", (Integer)mapTp1.get("prvd_id"));
				tpMap.put("prvd_name", (String)mapTp1.get("prvd_name"));
				tpMap.put("cty_name_short", null);				
				tpMap.put("qdyk_num_perc", null);
				tpMap.put("qdyk_subs_num", null);
				data.add(tpMap);
			}else{
				int prvdId1 = (Integer)mapTp1.get("prvd_id");
				int prvdId2 = (Integer)data.get(ii).get("prvd_id");
				if(prvdId1 != prvdId2){
					Map<String, Object> tpMap = new HashMap<String, Object>();
					tpMap.put("prvd_id", prvdId1);
					tpMap.put("prvd_name", (String)mapTp1.get("prvd_name"));
					tpMap.put("cty_name_short", null);
					tpMap.put("qdyk_num_perc", null);
					tpMap.put("qdyk_subs_num", null);
					data.add(ii, tpMap);
				}
			}
				ii++;
			
		}
		
		for(int i = 0; i < data.size(); i++) {			
				Map<String, Object> row = data.get(i);
				
				if(index==0){
					sh.getRow(3+i).createCell(1).setCellValue(String.valueOf(i/3+1));
					sh.getRow(3+i).getCell(1).setCellStyle(getStyle3());
					sh.getRow(3+i).createCell(2).setCellValue((String) row.get("prvd_name"));	
					sh.getRow(3+i).getCell(2).setCellStyle(getStyle3());
					if(i%3==0){
						sh.addMergedRegion(new CellRangeAddress(3+i, 3+i+2, 1, 1));
						sh.addMergedRegion(new CellRangeAddress(3+i, 3+i+2, 2, 2));
					}

				}				
				if(row.get("cty_name_short")!= null ){
					sh.getRow(3+i).createCell(index+3).setCellValue(((String) row.get("cty_name_short")).replaceAll("\\s*", ""));
					sh.getRow(3+i).getCell(index+3).setCellStyle(getStyle3());				
				}else{
					sh.getRow(3+i).createCell(index+3).setCellValue("-");
					sh.getRow(3+i).getCell(index+3).setCellStyle(getStyle3());								
				}
				if(row.get("qdyk_num_perc")!= null ){
					sh.getRow(3+i).createCell(index+4).setCellValue((row.get("qdyk_num_perc") instanceof BigDecimal? ((BigDecimal) row.get("qdyk_num_perc")).doubleValue() :(Double) row.get("qdyk_num_perc")));
					sh.getRow(3+i).getCell(index+4).setCellStyle(getStyle4());	
				}else{
					sh.getRow(3+i).createCell(index+4).setCellValue("-");
					sh.getRow(3+i).getCell(index+4).setCellStyle(getStyle3());	
				}	
				if(row.get("qdyk_subs_num")!= null ){
					sh.getRow(3+i).createCell(index+5).setCellValue((row.get("qdyk_subs_num") instanceof BigDecimal? ((BigDecimal) row.get("qdyk_subs_num")).doubleValue() :(Double) row.get("qdyk_subs_num")));				
					sh.getRow(3+i).getCell(index+5).setCellStyle(getStyle5());
				}else{
					sh.getRow(3+i).createCell(index+5).setCellValue("-");				
					sh.getRow(3+i).getCell(index+5).setCellStyle(getStyle3());
				}
				
		}	
	}
	
	
	public void writeThirdPart(List<Map<String, Object>> data,Sheet sh,int index,String mon,int maxCol){		
		if(index==0){
			for(int i = 0; i <= 312; i++) {
				sh.createRow(i);
			}
			sh.getRow(0).createCell(0).setCellValue("各省疑似养卡号码数量排名前十渠道");
			sh.getRow(0).getCell(0).setCellStyle(getStyle00());
			sh.addMergedRegion(new CellRangeAddress(0, 0, 0, maxCol));		
			sh.getRow(1).createCell(1).setCellValue("");
			sh.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));		
			sh.getRow(2).createCell(1).setCellValue("序号");
			sh.getRow(2).getCell(1).setCellStyle(getStyle2_green());
			sh.getRow(2).createCell(2).setCellValue("省份");
			sh.getRow(2).getCell(2).setCellStyle(getStyle2_green());
			
		}
		sh.getRow(1).createCell(index+3).setCellValue(mon.substring(0, 4)+"年"+("0".equals(mon.substring(4,5))?mon.substring(5,6):mon.substring(4,6))+"月");
		sh.getRow(1).getCell(index+3).setCellStyle(getStyle0());		
		sh.addMergedRegion(new CellRangeAddress(1, 1, index+3, index+6)); 

		sh.getRow(2).createCell(index+3).setCellValue("渠道名称");		
		sh.getRow(2).createCell(index+4).setCellValue("所属地市");		
		sh.getRow(2).createCell(index+5).setCellValue("养卡号码数量");		
		sh.getRow(2).createCell(index+6).setCellValue("占比");
		

		//第一个参数表示要冻结的列数，这里只冻结行所以为0；
		//第二个参数表示要冻结的行数；
		//第三个参数表示右边区域可见的首列序号，从1开始计算，这里是冻结行，所以为0；
		//第四个参数表示下边区域可见的首行序号，也是从1开始计算；
		sh.createFreezePane(0, 3, 0, 3);
		for(int i=3;i<=6; i++) {
			sh.setColumnWidth(index+i, 256 * 12);
			if((index/4)%2==0)
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle2());
			else
				sh.getRow(2).getCell(index+i).setCellStyle(getStyle2_green());
		}
		sh.setColumnWidth(index+3, 256 * 35);
		List<Map<String, Object>> resultTp = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> mapTp:notiFileGenService.getAllPrvd()){
			resultTp.add(mapTp);
			resultTp.add(mapTp);
			resultTp.add(mapTp);
			resultTp.add(mapTp);
			resultTp.add(mapTp);
			resultTp.add(mapTp);
			resultTp.add(mapTp);
			resultTp.add(mapTp);
			resultTp.add(mapTp);
			resultTp.add(mapTp);
		}

		int ii=0;
		for(Map<String, Object> mapTp1:resultTp){
			if(ii>=data.size()){
				Map<String, Object> tpMap = new HashMap<String, Object>();
				tpMap.put("prvd_id", (Integer)mapTp1.get("prvd_id"));
				tpMap.put("prvd_name", (String)mapTp1.get("prvd_name"));
				tpMap.put("cty_name_short", null);
				tpMap.put("chnl_name", null);
				tpMap.put("rase_crd_qty", null);
				tpMap.put("qdyk_per", null);
				data.add(tpMap);
			}else{
				int prvdId1 = (Integer)mapTp1.get("prvd_id");
				int prvdId2 = (Integer)data.get(ii).get("prvd_id");
				if(prvdId1 != prvdId2){
					Map<String, Object> tpMap = new HashMap<String, Object>();
					tpMap.put("prvd_id", prvdId1);
					tpMap.put("prvd_name", (String)mapTp1.get("prvd_name"));
					tpMap.put("cty_name_short", null);
					tpMap.put("chnl_name", null);
					tpMap.put("rase_crd_qty", null);
					tpMap.put("qdyk_per", null);
					data.add(ii, tpMap);
				}
			}
				ii++;
			

		}
		for(int i = 0; i < data.size(); i++) {			
				Map<String, Object> row = data.get(i);

				if(index==0){
					sh.getRow(3+i).createCell(1).setCellValue(String.valueOf(i/10+1));
					sh.getRow(3+i).getCell(1).setCellStyle(getStyle3());
					sh.getRow(3+i).createCell(2).setCellValue((String) row.get("prvd_name"));	
					sh.getRow(3+i).getCell(2).setCellStyle(getStyle3());
					if(i%10==0){
						sh.addMergedRegion(new CellRangeAddress(3+i, 3+i+9, 1, 1));
						sh.addMergedRegion(new CellRangeAddress(3+i, 3+i+9, 2, 2));
					}
				}
				if(row.get("chnl_name")!=null){
					sh.getRow(3+i).createCell(index+3).setCellValue((String) row.get("chnl_name"));
					sh.getRow(3+i).getCell(index+3).setCellStyle(getStyle3());
				}else{
					sh.getRow(3+i).createCell(index+3).setCellValue("-");
					sh.getRow(3+i).getCell(index+3).setCellStyle(getStyle3());
				}
				if(row.get("cty_name_short")!=null){
					sh.getRow(3+i).createCell(index+4).setCellValue(((String) row.get("cty_name_short")).replaceAll("\\s*", ""));
					sh.getRow(3+i).getCell(index+4).setCellStyle(getStyle3());
				}else{
					sh.getRow(3+i).createCell(index+4).setCellValue("-");
					sh.getRow(3+i).getCell(index+4).setCellStyle(getStyle3());
				}
				if(row.get("rase_crd_qty")!=null){
					sh.getRow(3+i).createCell(index+5).setCellValue((Integer)row.get("rase_crd_qty"));				
					sh.getRow(3+i).getCell(index+5).setCellStyle(getStyle5());
				}else{
					sh.getRow(3+i).createCell(index+5).setCellValue("-");				
					sh.getRow(3+i).getCell(index+5).setCellStyle(getStyle3());
				}
				if(row.get("qdyk_per")!=null){
					sh.getRow(3+i).createCell(index+6).setCellValue((row.get("qdyk_per") instanceof BigDecimal? ((BigDecimal) row.get("qdyk_per")).doubleValue() :(Double) row.get("qdyk_per")));
					sh.getRow(3+i).getCell(index+6).setCellStyle(getStyle4());	
				}else{
					sh.getRow(3+i).createCell(index+6).setCellValue("-");
					sh.getRow(3+i).getCell(index+6).setCellStyle(getStyle3());	
				}
		}
		
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
	
	
	private CellStyle getStyle8(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_CENTER);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)11);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setDataFormat(wb.createDataFormat().getFormat("0.00"));
		return style0;
	}
}
