package com.hpe.cmca.job;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import com.asiainfo.biframe.utils.json.JsonUtil;

@Service("NotiFileQdyk2000AllChnlsProcessor")
public class NotiFileQdyk2000AllChnlsProcessor extends AbstractNotiFileProcessor {
	protected Logger logger = Logger.getLogger(this.getClass());
	protected String preFileName = "31省全渠道汇总";
	
	protected Integer rNum = 0;
	
	protected String oldDataMaxMonth = "201507";
	protected String qitaFirstMonth = "201511";
	
	protected String chnlClass = "0";
	
	protected String focusCd = "2000";
	
	protected Map<String, String> titleMap = new HashMap<String,String>(){{
		put("0", "“全渠道养卡、套利”持续审计结果摘要");
		put("2", "“社会渠道养卡、套利”持续审计结果摘要");
		put("1", "“自有渠道养卡、套利”持续审计结果摘要");
		put("qt", "“其它渠道养卡、套利”持续审计结果摘要");
	}};
	
	protected Map<String, String> FileNameMap = new HashMap<String,String>(){{
		put("0", "31省全渠道汇总");
		put("2", "31省社会渠道汇总");
		put("1", "31省自有渠道汇总");
		put("qt", "31省其它渠道汇总");
	}};
	
	protected Map<String, String> nameMap = new HashMap<String,String>(){{
		put("0", "全渠道");
		put("2", "社会渠道");
		put("1", "自有渠道");
		put("qt", "其它渠道");
	}};
	
	public String getChnlCls() {
		return chnlClass;
	}

	public void setChnlClass(String chnlClass) {
		this.chnlClass = chnlClass;
	}

	@Override
	public boolean generate() throws Exception{
		logger.debug("------qdyk[" + "FileName:" + FileNameMap.get(chnlClass) + ",chnlClass=" + chnlClass + "] start !");
		if (!titleMap.containsKey(chnlClass)) return false;
		this.setFileName(FileNameMap.get(chnlClass));
		
		sh = wb.createSheet("sheet1"); 
		setColumnWidth();
		setRowHeight();

		sh.getRow(1).createCell(0).setCellValue(titleMap.get(chnlClass));
		sh.getRow(1).getCell(0).setCellStyle(getStyle3());
		sh.addMergedRegion(new CellRangeAddress(1, 1, 0, 15));  
		
		writeFirstPart(notiFileGenService.getNotiFile2000DataSum(month, chnlClass));
		
		writeSecondPart(notiFileGenService.getNotiFile2000DataQtyPerc(month, chnlClass));
		writeThirdPart(notiFileGenService.getNotiFile2000DataChnlPerc(month, chnlClass));
		return true;
	}

	private void writeFirstPart(List<Map<String, Object>> dataAll){
		//if (dataAll.size() > 12) dataAll = dataAll.subList(0, 12);
		logger.debug("------writeFirstPart:\n" + dataAll);
		CellStyle style0 = getStyle0();
		CellStyle style1 = getStyle1();
		  
		if ("0".equals(chnlClass)) {
			sh.getRow(3).createCell(0).setCellValue("总体情况");
			sh.getRow(3).getCell(0).setCellStyle(getStyle2());
			sh.addMergedRegion(new CellRangeAddress(3, 3, 0, 2+dataAll.size()));
			sh.getRow(4).createCell(1).setCellValue("用户入网月份");
			sh.getRow(5);
			sh.getRow(4).getCell(1).setCellStyle(style0);
			sh.getRow(4).createCell(2).setCellStyle(style0);
			sh.getRow(5).createCell(1).setCellStyle(style0);
			sh.getRow(5).createCell(2).setCellStyle(style0);
		} else {
			sh.getRow(4).createCell(0).setCellValue("总体情况");
			sh.getRow(4).getCell(0).setCellStyle(getStyle2());
			sh.addMergedRegion(new CellRangeAddress(4, 4, 0, 2+dataAll.size()));
			sh.getRow(5).createCell(1).setCellValue("用户入网月份");
			sh.getRow(5).getCell(1).setCellStyle(style0);
			sh.getRow(5).createCell(2).setCellStyle(style0);
		}
		
		sh.getRow(6).createCell(1).setCellValue(nameMap.get(chnlClass) + "入网号码数量");
		sh.getRow(7).createCell(1).setCellValue("疑似养卡号码数量");
		sh.getRow(8).createCell(1).setCellValue("疑似养卡号码占比%");
		sh.getRow(9).createCell(1).setCellValue("涉及" + nameMap.get(chnlClass) +  "数量");
		sh.getRow(10).createCell(1).setCellValue("涉及" + nameMap.get(chnlClass) + "占比%");
		
		for(int i = 6; i<=10;i++) {
			sh.getRow(i).getCell(1).setCellStyle(style1);
			sh.getRow(i).createCell(2).setCellStyle(style1);
		}
		if ("0".equals(chnlClass)) {
			sh.addMergedRegion(new CellRangeAddress(4, 5, 1, 2));
		} else {
			sh.addMergedRegion(new CellRangeAddress(5, 5, 1, 2));
		}

		if (dataAll.size() > 1) {
			//sh.addMergedRegion(new CellRangeAddress(4, 4, 3, 2+dataAll.size()));  
		}
		String temp = "";

		Map<String,Object> map=new HashMap<String,Object>();
		for (int i = 0,dataSize=dataAll.size(); i <dataSize; i++) {
			map = (Map<String, Object>) dataAll.get(i);
			if ("0".equals(chnlClass)) {
				sh.getRow(4).createCell(3+i).setCellStyle(style0);
			} else {
				
			}
			String month = (String) map.get("aud_trm");
			if (null == month) continue;
			if ("0".equals(chnlClass) && month.equals(oldDataMaxMonth)) {
				sh.addMergedRegion(new CellRangeAddress(4, 4, 3, i+2));
				sh.addMergedRegion(new CellRangeAddress(4, 4, i+3, 2+dataAll.size()));
				sh.getRow(4).getCell(3).setCellValue("全渠道");
				sh.getRow(4).getCell(i+3).setCellValue("社会渠道");
				
			}
			
			for(int j = 5; j<=10; j ++) {
				sh.getRow(j).createCell(i+3).setAsActiveCell();
				sh.getRow(j).getCell(i+3).setCellStyle(style0);
			}
			String t1 = (String) map.get("aud_trm");
			temp = t1.substring(0,4) + "年" + Integer.parseInt(t1.substring(4,6)) + "月";
			sh.getRow(5).getCell(i+3).setCellValue(temp);
			Integer t2 = null;
			if (map.get("nation_ent_num") instanceof BigDecimal) {
				t2 = ((BigDecimal) map.get("nation_ent_num")).intValue();
			} else {
				t2 = (Integer) map.get("nation_ent_num");
			}
			DecimalFormat df = new java.text.DecimalFormat("#,###0.00");
			temp = df.format((new BigDecimal(new Double(t2) / 10000).setScale(2, BigDecimal.ROUND_HALF_UP))).toString() + "万";
			sh.getRow(6).getCell(i+3).setCellValue(temp);
			
			Integer t3 = null;
			if (map.get("nation_qdyk_subs_num") instanceof BigDecimal) {
				t3 = ((BigDecimal) map.get("nation_qdyk_subs_num")).intValue();
			} else {
				t3 = (Integer) map.get("nation_qdyk_subs_num");
			}
			
			temp = df.format((new BigDecimal(new Double(t3) / 10000).setScale(2, BigDecimal.ROUND_HALF_UP))).toString() + "万";
			sh.getRow(7).getCell(i+3).setCellValue(temp);
			
			BigDecimal t4 = (BigDecimal) map.get("nation_qdyk_num_perc");
			sh.getRow(8).createCell(i+3).setCellStyle(getStyle4());
			sh.getRow(8).getCell(i+3).setCellValue((t4.setScale(4, BigDecimal.ROUND_HALF_UP)).doubleValue());
			
			Integer t5 = null;
			if (map.get("total_qdyk_chnl_num") instanceof BigDecimal) {
				t5 = ((BigDecimal) map.get("total_qdyk_chnl_num")).intValue();
			} else {
				t5 = (Integer) map.get("total_qdyk_chnl_num");
			}
			sh.getRow(9).createCell(i+3).setCellStyle(getStyle5());
			sh.getRow(9).getCell(i+3).setCellValue(t5);
			
			
			BigDecimal t6 = (BigDecimal) map.get("total_qdyk_chnl_perc");
			sh.getRow(10).createCell(i+3).setCellStyle(getStyle4());
			sh.getRow(10).getCell(i+3).setCellValue((t6.setScale(4, BigDecimal.ROUND_HALF_UP)).doubleValue());
		}
	}
	
	private void writeSecondPart(List<List<Map<String, Object>>> data){
		logger.debug("------writeSecondPart:\n" + data);
		CellStyle style0 = getStyle0();
		if ("0".equals(chnlClass)) {
			sh.getRow(13).createCell(0).setCellValue("各公司疑似养卡号码数量及占比");
			sh.getRow(13).getCell(0).setCellStyle(getStyle2());
			sh.getRow(14).createCell(1).setCellValue("用户入网月份");
			sh.addMergedRegion(new CellRangeAddress(14, 15, 1, 2));
			sh.getRow(14).getCell(1).setCellStyle(style0);
			sh.getRow(14).createCell(2).setCellStyle(style0);
			sh.getRow(15).createCell(1).setCellStyle(style0);
			sh.getRow(15).createCell(2).setCellStyle(style0);
		} else {
			sh.getRow(14).createCell(0).setCellValue("各公司疑似养卡号码数量及占比");
			sh.getRow(14).getCell(0).setCellStyle(getStyle2());
			sh.getRow(15).createCell(1).setCellValue("用户入网月份");
			sh.addMergedRegion(new CellRangeAddress(15, 15, 1, 2));
			sh.getRow(15).getCell(1).setCellStyle(style0);
			sh.getRow(15).createCell(2).setCellStyle(style0);
		}
		
		sh.getRow(16).createCell(1).setCellValue("排名");
		sh.getRow(16).createCell(2).setCellValue("省份");
		sh.getRow(16).getCell(1).setCellStyle(style0);
		sh.getRow(16).getCell(2).setCellStyle(style0);

		for(int k=3,dataSize=data.size();k<=2+dataSize*3;k++){
			if ("0".equals(chnlClass)) sh.getRow(14).createCell(k).setCellStyle(style0);
			sh.getRow(15).createCell(k).setCellStyle(style0);
			sh.getRow(16).createCell(k).setCellStyle(style0);
		}
		if ("0".equals(chnlClass)) {
			sh.addMergedRegion(new CellRangeAddress(13, 13, 0, 2+(data.size()>12?36:data.size()*3))); 
		} else {
			sh.addMergedRegion(new CellRangeAddress(14, 14, 0, 2+(data.size()>12?36:data.size()*3)));
		}
		boolean hasOldData = false;
		int displayNum = data.size()-1;
		List<Map<String, Object>> monthData=new ArrayList<Map<String,Object>>();
		for (int i=0,dataSize=data.size();i<dataSize;i++) {
			monthData = data.get(i);
			if (null == monthData || monthData.size() == 0) continue;
			if (monthData.get(0).size() == 0) continue;
			String curMonth = (String) monthData.get(0).get("aud_trm");
			sh.getRow(16).getCell(3+3*i).setCellValue("养卡号码数量");
			sh.getRow(16).getCell(4+3*i).setCellValue("占比 %");
			
			sh.getRow(16).getCell(5+3*i).setCellValue("养卡号码数量环比增幅%");
			
			//System.out.println(monthData);
			sh.getRow(15).getCell(3+3*i).setCellValue(curMonth.substring(0,4) + "年" + Integer.parseInt(curMonth.substring(4,6)) + "月");
			
			sh.addMergedRegion(new CellRangeAddress(15, 15, 3+3*i, 5+3*i));
	
			if ("0".equals(chnlClass) && curMonth.equals(oldDataMaxMonth)) {
				sh.addMergedRegion(new CellRangeAddress(14, 14, 3, 2+3*i));
				sh.addMergedRegion(new CellRangeAddress(14, 14, 3+3*i, 2+data.size()*3));
				sh.getRow(14).getCell(3).setCellValue("全渠道");
				sh.getRow(14).getCell(3+3*i).setCellValue("社会渠道");
				hasOldData = true;
			} 
			
			double preTemp = 0;
			int counter = 1;
			for (Map<String, Object> row : monthData){
				if (row.size() == 0) continue;
				if(i == 0 && (i < displayNum || displayNum == 0)) {
					sh.getRow(16+counter).createCell(1).setCellValue(counter);
					sh.getRow(16+counter).getCell(1).setCellStyle(getStyle6());
					sh.getRow(16+counter).createCell(2).setCellValue(row.get("prvd_name").toString());
					sh.getRow(16+counter).getCell(2).setCellStyle(getStyle6());
				}
				//System.out.println(row);
				String qdykSubsNum = row.get("qdyk_subs_num").toString();
				String qdykNumPerc = row.get("qdyk_num_perc").toString();
				
				if (i < data.size()) {
					sh.getRow(16+counter).createCell(3+3*i).setCellValue(Integer.parseInt(qdykSubsNum));
					sh.getRow(16+counter).getCell(3+3*i).setCellStyle(getStyle7());
					sh.getRow(16+counter).createCell(4+3*i).setCellValue(Double.parseDouble(qdykNumPerc));
					sh.getRow(16+counter).getCell(4+3*i).setCellStyle(getStyle8());
					sh.getRow(16+counter).createCell(5+3*i).setCellStyle(getStyle8());
					if(row.containsKey("qdyk_num_perc_id")) {
						Double tmp =  row.get("qdyk_num_perc_id") instanceof BigDecimal ? 
								((BigDecimal) row.get("qdyk_num_perc_id")).doubleValue(): (Double) row.get("qdyk_num_perc_id"); 
						sh.getRow(16+counter).getCell(5+3*i).setCellValue(tmp);
					}
				}
				if (i > 0) {
					if (null !=sh.getRow(16+counter).getCell(1+3*i)) {
						preTemp = sh.getRow(16+counter).getCell(1+3*i).getNumericCellValue();
						Double increase = preTemp - Double.parseDouble(qdykNumPerc);
						sh.getRow(16+counter).getCell(2+3*i).setCellValue(increase);
					}
				}
				counter++;
			}
		}
		if ("0".equals(chnlClass) && !hasOldData) {
			sh.addMergedRegion(new CellRangeAddress(14, 14, 3, 3+data.size()*3));
			sh.getRow(14).getCell(3).setCellValue("全渠道");
		}
		rNum = 50;
	}
	
	private void writeThirdPart(List<List<Map<String, Object>>> data){
		logger.debug("------writeThirdPart:\n" + data);
		CellStyle style0 = getStyle0();

		
		if ("0".equals(chnlClass)) {
			sh.getRow(rNum).createCell(0).setCellValue("各公司疑似养卡渠道数量及占比");
			sh.getRow(rNum).setHeightInPoints(30);
			sh.getRow(rNum+3).setHeightInPoints(45);
			sh.getRow(rNum).getCell(0).setCellStyle(getStyle2());
			sh.getRow(rNum+1).createCell(1).setCellValue("用户入网月份");
			sh.addMergedRegion(new CellRangeAddress(rNum+1, rNum+2, 1, 2));
			sh.getRow(rNum+1).getCell(1).setCellStyle(style0);
			sh.getRow(rNum+1).createCell(2).setCellStyle(style0);
			sh.getRow(rNum+2).createCell(1).setCellStyle(style0);
			sh.getRow(rNum+2).createCell(2).setCellStyle(style0);
		} else {
			sh.getRow(rNum+1).createCell(0).setCellValue("各公司疑似养卡渠道数量及占比");
			sh.getRow(rNum+1).setHeightInPoints(30);
			sh.getRow(rNum+3).setHeightInPoints(45);
			sh.getRow(rNum+1).getCell(0).setCellStyle(getStyle2());
			sh.getRow(rNum+2).createCell(1).setCellValue("用户入网月份");
			sh.addMergedRegion(new CellRangeAddress(rNum+2, rNum+2, 1, 2));
			sh.getRow(rNum+2).getCell(1).setCellStyle(style0);
			sh.getRow(rNum+2).createCell(2).setCellStyle(style0);
		}
		sh.getRow(rNum+3).createCell(1).setCellValue("排名");
		sh.getRow(rNum+3).createCell(2).setCellValue("省份");
		sh.getRow(rNum+3).getCell(1).setCellStyle(style0);
		sh.getRow(rNum+3).getCell(2).setCellStyle(style0);

		for(int k=3;k<=2+data.size()*3;k++){
			if ("0".equals(chnlClass)) sh.getRow(rNum+1).createCell(k).setCellStyle(style0);
			sh.getRow(rNum+2).createCell(k).setCellStyle(style0);
			sh.getRow(rNum+3).createCell(k).setCellStyle(style0);
		}
		if ("0".equals(chnlClass)) {
			sh.addMergedRegion(new CellRangeAddress(rNum, rNum, 0, 2+(data.size()>12?36:data.size()*3))); 
		} else {
			sh.addMergedRegion(new CellRangeAddress(rNum+1, rNum+1, 0, 2+(data.size()>12?36:data.size()*3)));
		}
		boolean hasOldData = false;
		int displayNum = data.size()-1;
		List<Map<String, Object>> monthData=new ArrayList<Map<String,Object>>();
		for (int i=0;i<data.size();i++) {
			monthData = data.get(i);
			if (null == monthData || monthData.size() ==0) continue;
			if (monthData.get(0).size() == 0) continue;
			String curMonth = (String) monthData.get(0).get("aud_trm");
			sh.getRow(rNum+3).getCell(3+3*i).setCellValue("涉及渠道数量");
			sh.getRow(rNum+3).getCell(4+3*i).setCellValue("占比 %");
			//if (i < displayNum){
				sh.getRow(rNum+3).getCell(5+3*i).setCellValue("渠道数量环比增幅%");
			//}
			
			sh.getRow(rNum+2).getCell(3+3*i).setCellValue(curMonth.substring(0,4) + "年" + Integer.parseInt(curMonth.substring(4,6)) + "月");
			
			
			sh.addMergedRegion(new CellRangeAddress(rNum+2, rNum+2, 3+3*i, 5+3*i));
		
			if ("0".equals(chnlClass) && curMonth.equals(oldDataMaxMonth)) {
				sh.addMergedRegion(new CellRangeAddress(rNum+1, rNum+1, 3, 2+3*i));
				sh.addMergedRegion(new CellRangeAddress(rNum+1, rNum+1, 3+3*i, 2+data.size()*3));
				sh.getRow(rNum+1).getCell(3).setCellValue("全渠道");
				sh.getRow(rNum+1).getCell(3+3*i).setCellValue("社会渠道");
				hasOldData = true;
			} 
			
			double preTemp = 0;
			int counter = 1;
			for (Map<String, Object> row : monthData){
				if (row.size() == 0) continue;
				if(i == 0 && i < 12) {
					sh.getRow(rNum+3+counter).createCell(1).setCellValue(counter);
					sh.getRow(rNum+3+counter).getCell(1).setCellStyle(getStyle6());
					sh.getRow(rNum+3+counter).createCell(2).setCellValue(row.get("prvd_name").toString());
					sh.getRow(rNum+3+counter).getCell(2).setCellStyle(getStyle6());
				}

				String qdykSubsNum = row.get("qdyk_chnl_num").toString();
				String qdykNumPerc = row.get("qdyk_chnl_perc").toString();
				
				if (i < data.size()) {
					sh.getRow(rNum+3+counter).createCell(3+3*i).setCellValue(Integer.parseInt(qdykSubsNum));
					sh.getRow(rNum+3+counter).getCell(3+3*i).setCellStyle(getStyle7());
					sh.getRow(rNum+3+counter).createCell(4+3*i).setCellValue(Double.parseDouble(qdykNumPerc));
					sh.getRow(rNum+3+counter).getCell(4+3*i).setCellStyle(getStyle8());
					sh.getRow(rNum+3+counter).createCell(5+3*i).setCellStyle(getStyle8());
					if(row.containsKey("qdyk_chnl_perc_id")) {
						Double tmp =  row.get("qdyk_chnl_perc_id") instanceof BigDecimal ? 
								((BigDecimal) row.get("qdyk_chnl_perc_id")).doubleValue(): (Double) row.get("qdyk_chnl_perc_id"); 
						sh.getRow(rNum+3+counter).getCell(5+3*i).setCellValue(tmp);
					}
					
				}
				counter++;
			}
		}
		
		if ("0".equals(chnlClass) && !hasOldData) {
			sh.addMergedRegion(new CellRangeAddress(rNum+1, rNum+1, 3, 3+data.size()*3));
			sh.getRow(rNum+1).getCell(3).setCellValue("全渠道");
		} 
	}
	
	private void setColumnWidth(){
		for(int i = 0 ; i <= 41; i++){
			sh.setColumnWidth(i, 256 * 14);
		}
		sh.setColumnWidth(2, 256 * 21);
	}
	
	private void setRowHeight(){
		for(int i = 0 ; i <= 100; i++){
			sh.createRow(i).setHeightInPoints(20);
		}
		sh.getRow(1).setHeightInPoints(30);
		sh.getRow(3).setHeightInPoints(30);
		sh.getRow(13).setHeightInPoints(30);
		sh.getRow(16).setHeightInPoints(45);
	}
	
	
	
	private CellStyle getStyle0(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_CENTER);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setWrapText(true); 
		return style0;
	}
	
	
	private CellStyle getStyle1(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_LEFT);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		return style0;
	}
	
	private CellStyle getStyle2(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_LEFT);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		style0.setFont(font);
		return style0;
	}
	
	private CellStyle getStyle3(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_CENTER);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		style0.setFont(font);
		return style0;
	}
	
	private CellStyle getStyle4(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_CENTER);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
		return style0;
	}
	
	private CellStyle getStyle5(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_CENTER);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
		return style0;
	}
	
	private CellStyle getStyle6(){
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
		style0.setWrapText(true); 
		return style0;
	}
	
	private CellStyle getStyle7(){
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
