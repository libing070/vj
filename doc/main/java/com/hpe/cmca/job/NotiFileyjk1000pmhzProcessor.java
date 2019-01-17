package com.hpe.cmca.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import com.hpe.cmca.util.CalendarUtils;
/**
 * 
 * <pre>
 * Desc： 
 * @author   xuwenhu
 * @refactor xuwenhu
 * @date     2017-5-11 上午11:44:08
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-5-11 	   xuwenhu 	         1. Created this class. 
 * </pre>
 */
@Service("NotiFileyjk1000pmhzProcessor")
public class NotiFileyjk1000pmhzProcessor extends AbstractNotiFileProcessor{
	@Override
	public boolean generate() throws Exception{
	    	String startMonth = "201604";//起始审计月
	    	String audTrm = month;
	    	//System.out.println("最新审计月"+month);
	    	if(Integer.parseInt(month)<Integer.parseInt("201604"))return false;
	    	List<String> audList = getAudTrmListToSomeDate(audTrm, startMonth);
		this.setFileName(CalendarUtils.buildAuditCycle(startMonth)+"-"+CalendarUtils.buildAuditCycle(audTrm)+"有价卡违规排名汇总");
		
		writeSheetCommonSummary(audList, notiFileGenService.getNotiFile1000pmhzSum(audList));
		
		writeSheetCommonDetail(audList, notiFileGenService.getNotiFile1000pmhz(audList));
		return true;
	}
	@Override
	protected String buildFileName() {
		fileName = fileName + ".xlsx";
		return fileName;
	}
	public void writeSheetCommonSummary(List<String> audList,List<Map<String, Object>>data) {
	    	int audNum = audList.size();
	    	sh = wb.createSheet("全公司审计结果摘要");
	    	for(int i=0; i<11; i++) sh.createRow(i);
		sh.getRow(0).createCell(0).setCellValue("“有价卡违规”持续审计结果摘要");
		sh.getRow(0).getCell(0).setCellStyle(getStyle0()); 
		sh.getRow(2).createCell(0).setCellValue("总体情况");
		sh.getRow(2).getCell(0).setCellStyle(getStyle00()); 
		
		
		sh.getRow(3).createCell(1).setCellValue("月份");
		
		
		sh.getRow(5).createCell(1).setCellValue("有价卡总金额(元)");
		sh.getRow(6).createCell(1).setCellValue("有价卡违规金额(元)");
		sh.getRow(7).createCell(1).setCellValue("有价卡违规金额占比");
		sh.getRow(8).createCell(1).setCellValue("有价卡总数量(张)");
		sh.getRow(9).createCell(1).setCellValue("有价卡违规数量(张)");
		sh.getRow(10).createCell(1).setCellValue("有价卡违规数量占比");
		sh.getRow(4).createCell(1).setCellStyle(getStyle_boldNone());
		for(int i=5; i<11; i++)
		    sh.getRow(i).getCell(1).setCellStyle(getStyle_boldNone());
		
		creatCell4SumAutrms(audList);
		
        	sh.addMergedRegion(new CellRangeAddress(3, 4, 1, 1));
        	sh.getRow(3).getCell(1).setCellStyle(getStyle_boldNone());
        	
        	sh.setColumnWidth(1, 256 * 24);//?-196px
        	for(int i = 2; i<=2*audNum+1; i++)	
        	    sh.setColumnWidth(i, 256 * 15);//256 * 14-98px
        	//sh.setColumnWidth(2*audNum+2, 256 * 20);
        	for(int i = 1; i<11; i++)	
    			sh.getRow(i).setHeightInPoints(30);//24-32px;?-30px;30-40px
        	sh.getRow(0).setHeightInPoints(34);
        	
        	sh.createRow(12).createCell(0).setCellValue("备注：以上指标中涉及“数量”和“金额”的“增幅”指的是环比增幅，等于（本月数值-上月数值）/上月数值；涉及“占比”的“增幅”，等于本月占比-上月占比。");
        	sh.getRow(12).getCell(0).setCellStyle(getStyle1());
        	for(int i = 0; i < audNum; i++) {
			
			sh.getRow(5).createCell((i+1)*2).setCellStyle(getStyle5());
			sh.getRow(6).createCell((i+1)*2).setCellStyle(getStyle5());
			sh.getRow(8).createCell((i+1)*2).setCellStyle(getStyle5());
			sh.getRow(9).createCell((i+1)*2).setCellStyle(getStyle5());
			
			sh.getRow(7).createCell((i+1)*2).setCellStyle(getStyle2());
			sh.getRow(10).createCell((i+1)*2).setCellStyle(getStyle2());
			for(int j=5; j<11; j++){
			    sh.getRow(j).createCell((i+1)*2+1).setCellStyle(getStyle2());
			    sh.setColumnWidth((i+1)*2, 256 * 24);
			}
		}
        	//先填“-”
        	for(int n=5; n<11; n++){
        	    for(int m=2; m<=audNum*2+1; m++)
        		sh.getRow(n).getCell(m).setCellValue("");
        	}
        	Map<String,Object> row=new HashMap<String,Object>();
        	for(int i = 0,dataSize=data.size(); i < dataSize; i++) {
			row = data.get(i);
			for(int j =0; j<audNum; j++){
			    String audTrm = row.get("aud_trm")==null?"":(row.get("aud_trm")+"").trim();
			    if(audList.get(j).equals(audTrm)){
				for(int k = 5; k<8; k++){
				    if(row.get("stat_type")!=null&&Integer.parseInt(row.get("stat_type")+"")+1 == k){
					if(row.get("stat_num")!=null){//stat_num不能用sql格式化，在excel里格式化
					    if((Integer.parseInt(row.get("stat_type")+"")==3||Integer.parseInt(row.get("stat_type")+"")==6)&&row.get("stat_num").toString().substring(0, 5).equals("-9999")){
						 sh.getRow(k).getCell((j+1)*2).setCellValue("未发生");
					    }
					    else{
						sh.getRow(k).getCell((j+1)*2).setCellValue(row.get("stat_num") instanceof BigDecimal ? 
							((BigDecimal) row.get("stat_num")).doubleValue() : (Long) row.get("stat_num"));
					    }
					}
					if(row.get("stat_zf")!=null){
					    if(row.get("stat_zf").toString().substring(0, 5).equals("-9999")){
						 sh.getRow(k).getCell((j+1)*2+1).setCellValue("未发生");
					    }
					    else{
						sh.getRow(k).getCell((j+1)*2+1).setCellValue(row.get("stat_zf") instanceof BigDecimal ? 
							((BigDecimal) row.get("stat_zf")).doubleValue() : (Long) row.get("stat_zf"));
					    }
					}
				    }
				}
				for(int m = 8; m<11; m++){
					if(row.get("stat_type")!=null&&Integer.parseInt(row.get("stat_type")+"")+7 == m){
						if(row.get("stat_num")!=null){//stat_num不能用sql格式化，在excel里格式化
							if((Integer.parseInt(row.get("stat_type")+"")==3||Integer.parseInt(row.get("stat_type")+"")==6)&&row.get("stat_num").toString().substring(0, 5).equals("-9999")){
								sh.getRow(m).getCell((j+1)*2).setCellValue("未发生");
							}
							else{
								sh.getRow(m).getCell((j+1)*2).setCellValue(row.get("stat_num") instanceof BigDecimal ? 
										((BigDecimal) row.get("stat_num")).doubleValue() : (Long) row.get("stat_num"));
							}
						}
						if(row.get("stat_zf")!=null){
							if(row.get("stat_zf").toString().substring(0, 5).equals("-9999")){
								sh.getRow(m).getCell((j+1)*2+1).setCellValue("未发生");
							}
							else{
								sh.getRow(m).getCell((j+1)*2+1).setCellValue(row.get("stat_zf") instanceof BigDecimal ? 
										((BigDecimal) row.get("stat_zf")).doubleValue() : (Long) row.get("stat_zf"));
							}
						}
					}
				}
				break;
			    }
			}
		}
	}
	
	public void writeSheetCommonDetail(List<String> audList, List<Map<String, Object>>data) {
	    	sh = wb.createSheet("各公司排名汇总");
	    	int audNum = audList.size();
	    	for(int i = 0; i<=6*audNum; i++){
	    	    sh.setColumnWidth(i, 256 * 24);
		}
		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("“有价卡违规”各公司违规金额占比概况");
		for(int i=1;i<=35;i++)sh.createRow(i);
		sh.getRow(2).createCell(0).setCellValue("月份");
		
		sh.getRow(3).createCell(0).setCellValue("省份");
		
		sh.getRow(0).getCell(0).setCellStyle(getStyle00());
		sh.getRow(2).getCell(0).setCellStyle(getStyle_boldRigTop());
		
		addMergedRegions(2, audNum);
		creatCell4Autrms(2, audList);
		
		sh.getRow(3).setHeightInPoints(60);//26.25-35px
		for(int i = 4; i<35; i++){
		    sh.getRow(i).createCell(0).setCellStyle(getStyle3());
		    sh.getRow(i).setHeightInPoints(20);//?-26px
		}
		sh.getRow(0).setHeightInPoints(30);//30-40px
		sh.getRow(1).setHeightInPoints(30);//30-40px
		sh.getRow(2).setHeightInPoints(20);//30-40px;?-26px
		sh.getRow(3).setHeightInPoints(45);//45-60px
		
		sh.getRow(34).createCell(0).setCellStyle(getStyle31());
		
		//违规金额
		sh.createRow(37);
		sh.getRow(37).createCell(0).setCellValue("“有价卡违规”各公司违规数量占比概况");
		for(int i=38;i<=70;i++)sh.createRow(i);
		sh.getRow(38).createCell(0).setCellValue("月份");
		
		sh.getRow(39).createCell(0).setCellValue("省份");
		creatCell4Autrms(38, audList);
		addMergedRegions(38, audNum);
		
		sh.getRow(37).getCell(0).setCellStyle(getStyle00());
		sh.getRow(38).getCell(0).setCellStyle(getStyle_boldRigTop());
		sh.getRow(38).getCell(1).setCellStyle(getStyle_boldLeRigTop());
		
		for(int i = 40; i<71; i++){
			sh.getRow(i).createCell(0).setCellStyle(getStyle3());
			sh.getRow(i).setHeightInPoints(20);//?-26px
		}
		sh.getRow(70).createCell(0).setCellStyle(getStyle31());
		sh.getRow(37).setHeightInPoints(30);//30-40px
		sh.getRow(38).setHeightInPoints(15);//30-40px
		sh.getRow(39).setHeightInPoints(60);//?-26px
		//先set“-”
		for(int n=4; n<35; n++){
        	    for(int m=0; m<=audNum*6; m++)
        		sh.getRow(n).getCell(m).setCellValue("-");
        	}
		for(int n=40; n<71; n++){
        	    for(int m=0; m<=audNum*6; m++)
        		sh.getRow(n).getCell(m).setCellValue("-");
        	}
		Map<String,Object> row=new HashMap<String,Object>();
	for (int a = 0; a < audNum; a++) {
	    for (int i = 0,dataSize=data.size(); i < dataSize && data.get(i).containsKey("total_num"); i++) {// “有价卡违规”各公司违规数量占比概况
		row = data.get(i);
		String audTrm = row.get("aud_trm") == null ? "" : (row.get("aud_trm") + "").trim();
		if (audList.get(a).equals(audTrm)) {
		    String rank = row.get("Ranking") == null ? "" : row.get("Ranking").toString();
		    int ind = rank.equals("") ? -1 : Integer.parseInt(rank.trim());
		    // ind:1-index:4
		    // 第一列：a/31==0 第二列：a/31==1
		    for (int m = 40; m < 72; m++) {
			if (m == ind + 39) {// 当前行是否对应该排名的记录
			    int j = 1 + a * 6;
			    // 一行的六列
			    sh.getRow(m).getCell(0).setCellValue(formatOutput(row.get("prvd_name") + ""));
			    if (row.get("ratio_num") != null&&!(row.get("ratio_num").toString().substring(0, 5).equals("-9999"))) {
				sh.getRow(m).getCell(j).setCellValue(formatOutput(row.get("Ranking_old") + ""));
			    }else{
				sh.getRow(m).getCell(j).setCellValue("-");
			    }
			    if (row.get("total_num") != null) {
				sh.getRow(m).getCell(j + 1).setCellValue(row.get("total_num") instanceof BigDecimal ? ((BigDecimal) row.get("total_num")).doubleValue() : (Long) row.get("total_num"));
			    }
			    if (row.get("total_num_foul") != null) {
				sh.getRow(m).getCell(j + 2)
					.setCellValue(row.get("total_num_foul") instanceof BigDecimal ? ((BigDecimal) row.get("total_num_foul")).doubleValue() : (Long) row.get("total_num_foul"));
			    }
			    if (row.get("ratio_num") != null) {
				if (row.get("ratio_num").toString().substring(0, 5).equals("-9999")) {
				    sh.getRow(m).getCell(j + 3).setCellValue("未发生");
				} else {
				    sh.getRow(m).getCell(j + 3)
					    .setCellValue(row.get("ratio_num") instanceof BigDecimal ? ((BigDecimal) row.get("ratio_num")).doubleValue() : (Long) row.get("ratio_num"));
				}
			    }
			    if (row.get("ratio_zb_zf") != null) {
				if (row.get("ratio_zb_zf").toString().substring(0, 5).equals("-9999")) {
				    sh.getRow(m).getCell(j + 4).setCellValue("未发生");
				} else {
				    sh.getRow(m).getCell(j + 4)
					    .setCellValue(row.get("ratio_zb_zf") instanceof BigDecimal ? ((BigDecimal) row.get("ratio_zb_zf")).doubleValue() : (Long) row.get("ratio_zb_zf"));
				}
			    }
			    if (row.get("ratio_hb_zf") != null) {
				if (row.get("ratio_hb_zf").toString().substring(0, 5).equals("-9999")) {
				    sh.getRow(m).getCell(j + 5).setCellValue("未发生");
				} else {
				    sh.getRow(m).getCell(j + 5)
					    .setCellValue(row.get("ratio_hb_zf") instanceof BigDecimal ? ((BigDecimal) row.get("ratio_hb_zf")).doubleValue() : (Long) row.get("ratio_hb_zf"));
				}
			    }
			    break;
			}
		    }
		}
	    }
	}
	Map<String,Object> rowAmo=new HashMap<String,Object>();
	for (int a1 = 0; a1 < audNum; a1++) {
	    for (int i = 0,dataSize=data.size(); i < dataSize; i++) {
		if (data.get(i).containsKey("total_amt")) {// “有价卡违规”各公司违规金额占比概况
		    rowAmo = data.get(i);
		    String audTrm = rowAmo.get("aud_trm") == null ? "" : (rowAmo.get("aud_trm") + "").trim();
		    if (audList.get(a1).equals(audTrm)) {
			String rank = rowAmo.get("Ranking") == null ? "" : rowAmo.get("Ranking").toString();
			int ind = rank.equals("") ? -1 : Integer.parseInt(rank.trim());
			for (int m = 4; m < 35; m++) {
			    if (ind + 3 == m) {
				// 第一列：a/31==0 第二列：a/31==1
				int j = 1 + a1 * 6;
				sh.getRow(m).getCell(0).setCellValue(formatOutput(rowAmo.get("prvd_name") + ""));
				if (rowAmo.get("ratio_num") != null&&!(rowAmo.get("ratio_num").toString().substring(0, 5).equals("-9999"))) {
				    sh.getRow(m).getCell(j).setCellValue(formatOutput(rowAmo.get("Ranking_old") + ""));
				}else{
				    sh.getRow(m).getCell(j).setCellValue(formatOutput("-"));
				}
				if (rowAmo.get("total_amt") != null) {
				    sh.getRow(m).getCell(j + 1)
					    .setCellValue(rowAmo.get("total_amt") instanceof BigDecimal ? ((BigDecimal) rowAmo.get("total_amt")).doubleValue() : (Long) rowAmo.get("total_amt"));
				}
				if (rowAmo.get("total_amt_foul") != null) {
				    sh.getRow(m).getCell(j + 2)
					    .setCellValue(rowAmo.get("total_amt_foul") instanceof BigDecimal ? ((BigDecimal) rowAmo.get("total_amt_foul")).doubleValue() : (Long) rowAmo.get("total_amt_foul"));
				}
				if (rowAmo.get("ratio_num") != null) {
				    if (rowAmo.get("ratio_num").toString().substring(0, 5).equals("-9999")) {
					sh.getRow(m).getCell(j + 3).setCellValue("未发生");
				    } else {
					sh.getRow(m).getCell(j + 3)
						.setCellValue(rowAmo.get("ratio_num") instanceof BigDecimal ? ((BigDecimal) rowAmo.get("ratio_num")).doubleValue() : (Long) rowAmo.get("ratio_num"));
				    }
				}
				if (rowAmo.get("ratio_zb_zf") != null) {
				    if (rowAmo.get("ratio_zb_zf").toString().substring(0, 5).equals("-9999")) {
					sh.getRow(m).getCell(j + 4).setCellValue("未发生");
				    } else {
					sh.getRow(m).getCell(j + 4)
						.setCellValue(rowAmo.get("ratio_zb_zf") instanceof BigDecimal ? ((BigDecimal) rowAmo.get("ratio_zb_zf")).doubleValue() : (Long) rowAmo.get("ratio_zb_zf"));
				    }
				}
				if (rowAmo.get("ratio_hb_zf") != null) {
				    if (rowAmo.get("ratio_hb_zf").toString().substring(0, 5).equals("-9999")) {
					sh.getRow(m).getCell(j + 5).setCellValue("未发生");
				    } else {
					sh.getRow(m).getCell(j + 5)
						.setCellValue(rowAmo.get("ratio_hb_zf") instanceof BigDecimal ? ((BigDecimal) rowAmo.get("ratio_hb_zf")).doubleValue() : (Long) rowAmo.get("ratio_hb_zf"));
				    }
				}
				break;
			    }
			}
		    }
		}
	    }
	}
	}	
	private String formatOutput(Object str) {
    	    if(str==null)
    		return "-";
    	    String s = str.toString().trim();
    	    if("".equals(s))
    	    	s = "-";
    	    return (s);
    	}
	private void creatCell4SumAutrms(List<String> audList){
	    int audNum = audList.size();
	    for(int i = 0; i<audNum; i++){
		int index = i*2+2;
	
    	for(int j=index; j<index+2; j++)
    	    sh.getRow(3).createCell(j).setCellStyle(getStyle_boldNone());
    	
	//月份
    	sh.getRow(3).getCell(index).setCellValue(CalendarUtils.buildAuditCycle(audList.get(i)));
	sh.addMergedRegion(new CellRangeAddress(3, 3, index, index+1));
	
    	sh.getRow(4).createCell(index).setCellValue("数值");
    	sh.getRow(4).createCell(index+1).setCellValue("增幅");
    	sh.getRow(4).getCell(index).setCellStyle(getStyle_boldNone());
    	sh.getRow(4).getCell(index+1).setCellStyle(getStyle_boldNone());
	    }
	}
	private void creatCell4Autrms(int rowNum, List<String> audList){
	    int audNum = audList.size();
	for (int i = 0; i < audNum; i++) {
	    int index = i * 6 + 1;
	    // 月份的单元格 格式
	    for (int j = index; j < index + 6; j++)
		sh.getRow(rowNum).createCell(j).setCellStyle(getStyle_boldLeRigTop());
	    // 月份
	    sh.getRow(rowNum).getCell(index).setCellValue(CalendarUtils.buildAuditCycle(audList.get(i)));
	    // 每个月份对应的6列表头
	    // String name = "";
	    sh.getRow(rowNum + 1).createCell(index).setCellValue("排名");
	    if (rowNum == 38) {// 违规数量
		sh.getRow(39).createCell(index + 1).setCellValue("有价卡总数量(张)");
		sh.getRow(39).createCell(index + 2).setCellValue("有价卡违规数量(张)");
		sh.getRow(39).createCell(index + 3).setCellValue("有价卡违规数量占比");
		sh.getRow(39).createCell(index + 4).setCellValue("有价卡违规数量环比增幅%（占比增幅）");
		sh.getRow(39).createCell(index + 5).setCellValue("有价卡违规数量环比增幅%");
	    }
	    if (rowNum == 2) {// 违规金额
		// name ="数量";
		sh.getRow(3).createCell(index + 1).setCellValue("有价卡总金额(元)");
		sh.getRow(3).createCell(index + 2).setCellValue("有价卡违规金额(元)");
		sh.getRow(3).createCell(index + 3).setCellValue("有价卡违规金额占比");
		sh.getRow(3).createCell(index + 4).setCellValue("有价卡违规金额环比增幅%（占比增幅）");
		sh.getRow(3).createCell(index + 5).setCellValue("有价卡违规金额环比%");
	    }
	    for (int k = rowNum + 2; k < rowNum + 33; k++) {// 数据
		if (k == rowNum + 32) {// excel的表的最后一行
		    sh.getRow(k).createCell(index).setCellStyle(getStyle41());
		    sh.getRow(k).createCell(index + 1).setCellStyle(getStyle81());
		    sh.getRow(k).createCell(index + 2).setCellStyle(getStyle81());
		    if (rowNum == 38) {// 违规金额
			sh.getRow(k).createCell(index + 1).setCellStyle(getStyle61());
			sh.getRow(k).createCell(index + 2).setCellStyle(getStyle61());
		    }
		    sh.getRow(k).createCell(index + 3).setCellStyle(getStyle71());
		    sh.getRow(k).createCell(index + 4).setCellStyle(getStyle71());
		    sh.getRow(k).createCell(index + 5).setCellStyle(getStyle91());
		    break;
		}
		sh.getRow(k).createCell(index).setCellStyle(getStyle42());
		sh.getRow(k).createCell(index + 1).setCellStyle(getStyle8());
		sh.getRow(k).createCell(index + 2).setCellStyle(getStyle8());
		if (rowNum == 38) {// 违规金额
		    sh.getRow(k).createCell(index + 1).setCellStyle(getStyle6());
		    sh.getRow(k).createCell(index + 2).setCellStyle(getStyle6());
		}
		sh.getRow(k).createCell(index + 3).setCellStyle(getStyle7());
		sh.getRow(k).createCell(index + 4).setCellStyle(getStyle7());
		sh.getRow(k).createCell(index + 5).setCellStyle(getStyle9());
	    }
	}
	    //为每个月份对应的6列表头设置格式
	    sh.getRow(rowNum+1).getCell(0).setCellStyle(getStyle_boldRight());
	    for(int i = 1; i<=audNum*6; i++){//
		if(i%6==1){
		    sh.getRow(rowNum+1).getCell(i).setCellStyle(getStyle_boldLeft());
		}else if(i%6==0){
		    sh.getRow(rowNum+1).getCell(i).setCellStyle(getStyle_boldRight());
		}else{
		    sh.getRow(rowNum+1).getCell(i).setCellStyle(getStyle_boldNone());
		}
	    }
	   
	}
	/**
	 * 
	 * <pre>
	 * Desc  获取d2-d1之间的月份,返回的list是从最新月往前推的
	 * @param d1
	 * @param d2
	 * @return
	 * @author xuwenhu
	 * 2017-5-10 上午10:03:18
	 * </pre>
	 */
	
	protected List<String> getAudTrmListToSomeDate(String d1,String d2){
		List<String> dateList =new ArrayList<String>();
		String temp = d1;
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
	
	//为月份合并单元格
	public void addMergedRegions(int rowNum, int audNum){
	    for(int i = 0; i<audNum; i++){
		int firstCol = i*6+1;
		int lastCol = i*6+6;
		 sh.addMergedRegion(new CellRangeAddress(rowNum, rowNum, firstCol, lastCol));
	    }
	   
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
	private CellStyle getStyle0(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_LEFT);  
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
		style.setFont(font);
		return style;
	}
	private CellStyle getStyle_boldRigTop(){
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
		style.setBorderRight(CellStyle.BORDER_THICK);
		style.setBorderTop(CellStyle.BORDER_THICK);
		style.setWrapText(true); //换行
		return style;
	}
	private CellStyle getStyle_boldLeRigTop(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_CENTER);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THICK);
		style.setBorderRight(CellStyle.BORDER_THICK);
		style.setBorderTop(CellStyle.BORDER_THICK);
		style.setWrapText(true); 
		return style;
	}
	private CellStyle getStyle_boldAll(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_CENTER);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THICK);
		style.setBorderLeft(CellStyle.BORDER_THICK);
		style.setBorderRight(CellStyle.BORDER_THICK);
		style.setBorderTop(CellStyle.BORDER_THICK);
		style.setWrapText(true); 
		return style;
	}
	private CellStyle getStyle_boldRight(){
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
		style.setBorderRight(CellStyle.BORDER_THICK);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setWrapText(true); 
		return style;
	}
	private CellStyle getStyle_boldLeft(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_CENTER);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THICK);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setWrapText(true); 
		return style;
	}
	private CellStyle getStyle_boldNone(){
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
		style.setWrapText(true); //换行
		return style;
	}
//	private CellStyle getStyle_boldNone_aCenter(){
//		CellStyle style = wb.createCellStyle();  
//		style.setAlignment(CellStyle.ALIGN_CENTER);  
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
//		Font font = wb.createFont();
//		font.setFontName("宋体"); 
//		font.setFontHeightInPoints((short)12);
//		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
//		style.setFont(font);
//		style.setBorderBottom(CellStyle.BORDER_THIN);
//		style.setBorderLeft(CellStyle.BORDER_THIN);
//		style.setBorderRight(CellStyle.BORDER_THIN);
//		style.setBorderTop(CellStyle.BORDER_THIN);
//		style.setWrapText(true); //换行
//		return style;
//	}
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
		style.setBorderRight(CellStyle.BORDER_THICK);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setWrapText(true);
		return style;
	}
	private CellStyle getStyle31(){
		CellStyle style = wb.createCellStyle();  
		style.setAlignment(CellStyle.ALIGN_CENTER);  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THICK);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THICK);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setWrapText(true);
		return style;
	}
	private CellStyle getStyle4(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_RIGHT);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THICK);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setWrapText(true);
//		style0.setDataFormat(wb.createDataFormat().getFormat("0"));
		return style0;
	}
	private CellStyle getStyle41(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_CENTER);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THICK);
		style0.setBorderLeft(CellStyle.BORDER_THICK);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
//		style0.setDataFormat(wb.createDataFormat().getFormat("0"));
		return style0;
	}
	private CellStyle getStyle42(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_CENTER);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)12);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THICK);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setWrapText(true);
//		style0.setDataFormat(wb.createDataFormat().getFormat("0"));
		return style0;
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
		style.setWrapText(false);
		style.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
		return style;
	}
	private CellStyle getStyle2(){
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
		style.setWrapText(false);
		style.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
		return style;
	}
	private CellStyle getStyle6(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_RIGHT);  
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
	private CellStyle getStyle61(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_RIGHT);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)11);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THICK);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
		return style0;
	}

	private CellStyle getStyle80(){
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
		style0.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
		return style0;
	}
	private CellStyle getStyle8(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_RIGHT);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)11);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
		return style0;
	}
	private CellStyle getStyle81(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_RIGHT);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)11);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THICK);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
		return style0;
	}
	private CellStyle getStyle71(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_RIGHT);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)11);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THICK);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
		return style0;
	}
	private CellStyle getStyle7(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_RIGHT);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)11);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
		return style0;
	}
	private CellStyle getStyle9(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_RIGHT);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)11);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THICK);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
		return style0;
	}
	private CellStyle getStyle91(){
		CellStyle style0 = wb.createCellStyle();  
		style0.setAlignment(CellStyle.ALIGN_RIGHT);  
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
		Font font = wb.createFont();
		font.setFontName("宋体"); 
		font.setFontHeightInPoints((short)11);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THICK);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THICK);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
		return style0;
	}
}
