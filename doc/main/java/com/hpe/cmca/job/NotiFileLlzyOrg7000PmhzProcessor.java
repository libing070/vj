package com.hpe.cmca.job;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service("NotiFileLlzyOrg7000PmhzProcessor")
public class NotiFileLlzyOrg7000PmhzProcessor extends AbstractNotiFileProcessor {


    protected String focusCd = "7000";
    protected Logger logger  = Logger.getLogger(this.getClass());

    @Override
    protected String buildFileName() {
	fileName = fileName + ".xlsx";
	return fileName;
    }

    /***
     * 
     * <pre>
     * Desc  返回起始月与最新月之间的月份
     * @param d1
     * @param d2
     * @return
     * @author feihu
     * May 11, 2017 11:33:06 AM
     * </pre>
     */
    protected List<String> getAudTrmListToSomeDate(String d1, String d2) {
	List<String> dateList = new ArrayList<String>();
	String temp = d1;
	dateList.add(temp);
	while (!d2.equals(temp)) {
	    temp = getLastMon(temp);
	    dateList.add(temp);

	}
	return dateList;
    }

    protected String getLastMon(String d1) {
	Integer thisYear = Integer.parseInt(d1.substring(0, 4));
	Integer thisMonth = Integer.parseInt("0".equals(d1.substring(4, 5)) ? d1.substring(5, 6) : d1.substring(4, 6));
	Integer retYear = thisYear;
	Integer retMonth = 0;
	if (thisMonth >= 2) {
	    retMonth = thisMonth - 1;
	} else if (thisMonth == 1) {
	    retMonth = 12;
	    retYear = thisYear - 1;
	}
	String mon = retMonth.toString();
	StringBuffer sb = new StringBuffer(2);
	sb.append("0");
	if (retMonth <= 9) {
	    sb.append(retMonth);
	    mon = sb.toString();
	}
	return retYear.toString() + mon;
    }
    
    public void start() throws Exception{
		wb = new XSSFWorkbook();
		this.generate();
		FileOutputStream out = null;
		try {  
			out = new FileOutputStream(this.getLocalPath());  
			wb.write(out);
			out.close();  
		} catch (IOException e) { 
			logger.error("paiminghuizong File ERROR:",e);
		    throw e;
		} catch (Exception e) {  
			logger.error("paiminghuizong File ERROR:",e);
		    throw e;
		}finally {  
		    try {  
		    	if(out != null)
		    		out.close();  
		    } catch (IOException e) {  
		    	logger.error("paiminghuizong File ERROR:",e);
		    	 throw e;  
		    }  
		} 
	}

    public boolean generate() throws Exception {
	    Integer thisYear = Integer.parseInt(month.substring(0, 4));
	    Integer thisMonth = Integer.parseInt("0".equals(month.substring(4, 5)) ? month.substring(5, 6) : month.substring(4, 6));
    	 this.setFileName("疑似违规流量转售_排名汇总"+ month);
 	    // 更新最新排名
 	    notiFileGenService.getNotiFileLlzy7000Pmhz(month, "updateReportSort");
 	    // 创建第一个sheet
 	    sh = wb.createSheet("排名汇总");
 	    writeFirstPart(notiFileGenService.getNotiFileLlzy7000Pmhz(month, "allorgReport"), sh, month);
 	    writeSecondPart(notiFileGenService.getNotiFileLlzy7000Pmhz(month, "orgReport"), sh, month);
 	    
 	   // 创建第二个sheet
 	    sh = wb.createSheet("疑似违规流量转售集团客户清单");
 	    writeThirdPart(notiFileGenService.getNotiFileLlzy7000Pmhz(month, "orgOrder"), sh, month);
	    return true;
    }
    
    public void writeThirdPart(List<Map<String, Object>> data, Sheet sh, String month) {
    //排名	省名称	地市名称	统付方式	集团客户名称	集团客户标识	统付总流量（G)	集团疑似转售流量占该省统付流量占比	统付总次数	转入号码数	成员稳定率%	本地市集中度%	业务种类是否单一	操作天数	多次订购用户数	多处订购用户数
    		sh.createRow(0);
    		sh.getRow(0).createCell(0).setCellValue("排名");
    		sh.getRow(0).createCell(1).setCellValue("公司");
    		sh.getRow(0).createCell(2).setCellValue("地市名称");
    		sh.getRow(0).createCell(3).setCellValue("统付方式");
    		sh.getRow(0).createCell(4).setCellValue("集团客户名称");
    		sh.getRow(0).createCell(5).setCellValue("集团客户标识");
    		sh.getRow(0).createCell(6).setCellValue("疑似转售流量总值(G)");
    		sh.getRow(0).createCell(7).setCellValue("疑似转售流量占该省统付总流量占比(%)");
    		sh.getRow(0).createCell(8).setCellValue("统付总次数");
    		sh.getRow(0).createCell(9).setCellValue("转入号码数");
    		sh.getRow(0).createCell(10).setCellValue("每用户平均统付流量值(G)");
    		sh.getRow(0).createCell(11).setCellValue("成员稳定率%");
    		sh.getRow(0).createCell(12).setCellValue("本地市集中度%");
    		sh.getRow(0).createCell(13).setCellValue("业务种类是否单一");
    		sh.getRow(0).createCell(14).setCellValue("操作天数");
    		sh.getRow(0).createCell(15).setCellValue("多次订购用户数");
    		sh.getRow(0).createCell(16).setCellValue("多处订购用户数");
    		for(int i = 0;i <= 16;i++){
    			sh.getRow(0).getCell(i).setCellStyle(getStyle3());
    		}
    		for(int j = 0;j < 17;j++){
    			if(j ==7){
    				sh.setColumnWidth(j, 256 * 20);
    			}else{
    				sh.setColumnWidth(j, 256 * 16);
    			}
    			
    		}
    		
    	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
    	for (int i = 0; i < data.size(); i++) {
    	    Map<String, Object> row = data.get(i);
    	    // 插入单元格数据
//    		int rowNumber = 1;
    		sh.createRow(1+i);
    		for(int j=0;j <= 16;j++){
    			sh.getRow(1+i).createCell(j).setCellValue("-");
    			sh.getRow(1+i).getCell(j).setCellStyle(getStyle2());
    		}
    		    if (row.get("rank_org") != null) {
    			sh.getRow(1+i).getCell(0).setCellValue((Integer)row.get("rank_org"));
    			sh.getRow(1+i).getCell(0).setCellStyle(getStyle6());
    		    }
    		    if (row.get("CMCC_prov_prvd_nm") != null) {
    			sh.getRow(1+i).getCell(1).setCellValue(row.get("CMCC_prov_prvd_nm").toString());
    			sh.getRow(1+i).getCell(1).setCellStyle(getStyle6());
    		    }
    		    if (row.get("jt_cmcc_prvd_nm_short") != null) {
        			sh.getRow(1+i).getCell(2).setCellValue(row.get("jt_cmcc_prvd_nm_short").toString());
        			sh.getRow(1+i).getCell(2).setCellStyle(getStyle6());
        		    }
    		    //统付方式
    		    if (row.get("busn_mode_nm") != null) {
        			sh.getRow(1+i).getCell(3).setCellValue(row.get("busn_mode_nm").toString());
        			sh.getRow(1+i).getCell(3).setCellStyle(getStyle6());
        		    }
    		    //集团客户名称
    		    if (row.get("org_nm") != null) {
        			sh.getRow(1+i).getCell(4).setCellValue(row.get("org_nm").toString());
        			sh.getRow(1+i).getCell(4).setCellStyle(getStyle6());
        		    }
    		    //集团客户标识
    		    if (row.get("org_cust_id") != null) {
        			sh.getRow(1+i).getCell(5).setCellValue(row.get("org_cust_id").toString());
        			sh.getRow(1+i).getCell(5).setCellStyle(getStyle66());
        		    }
    		    //统付总流量（G
    		    if (row.get("sum_strm_cap") != null) {
        			sh.getRow(1+i).getCell(6).setCellValue(
        					row.get("sum_strm_cap") instanceof BigDecimal ? ((BigDecimal)row.get("sum_strm_cap")).doubleValue() : (Long) row.get("sum_strm_cap"));
        			sh.getRow(1+i).getCell(6).setCellStyle(getStyle5());
        		    }
    		    //流量占比===================
    		    if (row.get("strm_cap_per") != null) {
        			sh.getRow(1+i).getCell(7).setCellValue(
        					row.get("strm_cap_per") instanceof BigDecimal ? ((BigDecimal)row.get("strm_cap_per")).doubleValue() : (Long) row.get("strm_cap_per"));
        			sh.getRow(1+i).getCell(7).setCellStyle(getStyle8());
        		    }
    		    if (row.get("sum_cnt") != null) {
        			sh.getRow(1+i).getCell(8).setCellValue((Integer)row.get("sum_cnt"));
        			sh.getRow(1+i).getCell(8).setCellStyle(getStyle6());
        		    }
    		    if (row.get("sum_msisdn") != null) {
        			sh.getRow(1+i).getCell(9).setCellValue((Integer)row.get("sum_msisdn"));
        			sh.getRow(1+i).getCell(9).setCellStyle(getStyle6());
        		    }
    		    if (row.get("avg_strm_cap") != null) {
        			sh.getRow(1+i).getCell(10).setCellValue(
        					row.get("avg_strm_cap")  instanceof BigDecimal ? ((BigDecimal)row.get("avg_strm_cap")).doubleValue() : (Long) row.get("avg_strm_cap"));
        			sh.getRow(1+i).getCell(10).setCellStyle(getStyle5());
        		    }
    		    if (row.get("member_stab_rate") != null) {
        			sh.getRow(1+i).getCell(11).setCellValue(
        					row.get("member_stab_rate") instanceof BigDecimal ? ((BigDecimal)row.get("member_stab_rate")).doubleValue() : (Long) row.get("member_stab_rate"));
        			sh.getRow(1+i).getCell(11).setCellStyle(getStyle8());
        		    }
    		    if (row.get("cty_level_rate") != null) {
        			sh.getRow(1+i).getCell(12).setCellValue(
        					row.get("cty_level_rate") instanceof BigDecimal ? ((BigDecimal)row.get("cty_level_rate")).doubleValue() : (Long) row.get("cty_level_rate"));
        			sh.getRow(1+i).getCell(12).setCellStyle(getStyle8());
        		    }
    		    if (row.get("a3") != null) {
        			sh.getRow(1+i).getCell(13).setCellValue(row.get("a3").toString());
        			sh.getRow(1+i).getCell(13).setCellStyle(getStyle6());
        		    }
    		    if (row.get("sum_dt") != null) {
        			sh.getRow(1+i).getCell(14).setCellValue((Integer)row.get("sum_dt"));
        			sh.getRow(1+i).getCell(14).setCellStyle(getStyle6());
        		    }
    		    if (row.get("duoci_sum") != null) {
        			sh.getRow(1+i).getCell(15).setCellValue((Integer)row.get("duoci_sum"));
        			sh.getRow(1+i).getCell(15).setCellStyle(getStyle6());
        		    }
    		    if (row.get("duochu_sum") != null) {
        			sh.getRow(1+i).getCell(16).setCellValue((Integer)row.get("duochu_sum"));
        			sh.getRow(1+i).getCell(16).setCellStyle(getStyle6());
        		    }
    	}
     }
    
    public void writeSecondPart(List<Map<String, Object>> data, Sheet sh, String month) {
    	sh.createRow(11).createCell(0).setCellValue("各公司“疑似违规流量转售”排名情况");
    	sh.getRow(11).getCell(0).setCellStyle(getStyle14());
    	sh.addMergedRegion(new CellRangeAddress(11, 11, 0, 4));
    	// 创建行
    	for (int i = 15; i <= 46; i++) {
    	    sh.createRow(i).createCell(1).setCellStyle(getStyle6());
    	    sh.getRow(i).createCell(2).setCellStyle(getStyle6());
    	    sh.getRow(i).getCell(1).setCellValue("-");
    	    sh.getRow(i).getCell(2).setCellValue("-");
    	}
    	sh.createRow(13).createCell(1).setCellValue("排名");
    	sh.getRow(13).createCell(2).setCellValue("公司");
    	sh.getRow(13).getCell(1).setCellStyle(getStyle13());
    	sh.getRow(13).getCell(2).setCellStyle(getStyle13());
    	sh.createRow(14);
    	sh.getRow(14).createCell(1).setCellStyle(getStyle13());
    	sh.getRow(14).createCell(2).setCellStyle(getStyle13());
    	int j = 3;
    	HashMap<String, String> mothMap = new HashMap<String, String>();
    	List<String> mothList = getAudTrmListToSomeDate(month, "201710");
    	for (int i = 0; i < mothList.size(); i++) {
    	    String moth = mothList.get(i);
    	    if (!mothMap.containsValue(moth)) {
    		mothMap.put(Integer.toString(i), moth);
    		Integer thisMonth = Integer.parseInt("0".equals(moth.substring(4, 5)) ? moth.substring(5, 6) : moth.substring(4, 6));
    		sh.getRow(13).createCell(j).setCellValue(moth.substring(0, 4) + "年" + thisMonth + "月");
    		sh.getRow(14).createCell(j).setCellValue("统付涉及集团客户数");
    		sh.getRow(14).createCell(j + 1).setCellValue("统付涉及统付流量总值(G)");
    		sh.getRow(14).createCell(j + 2).setCellValue("统付流量订购次数");
    		sh.getRow(14).createCell(j + 3).setCellValue("统付涉及号码总数");
    		sh.getRow(14).createCell(j + 4).setCellValue("疑似违规流量转售集团客户数");
    		sh.getRow(14).createCell(j + 5).setCellValue("疑似违规流量转售流量总值(G)");
    		sh.getRow(14).createCell(j + 6).setCellValue("疑似违规流量转售总次数");
    		sh.getRow(14).createCell(j + 7).setCellValue("疑似违规流量转售涉及号码总数");
    		sh.getRow(14).createCell(j + 8).setCellValue("疑似违规转售流量占总统付流量比(%)");
    		sh.getRow(14).getCell(j).setCellStyle(getStyle3());
    		sh.getRow(14).getCell(j + 1).setCellStyle(getStyle3());
    		sh.getRow(14).getCell(j + 2).setCellStyle(getStyle3());
    		sh.getRow(14).getCell(j + 3).setCellStyle(getStyle3());
    		sh.getRow(14).getCell(j + 4).setCellStyle(getStyle3());
    		sh.getRow(14).getCell(j + 5).setCellStyle(getStyle3());
    		sh.getRow(14).getCell(j + 6).setCellStyle(getStyle3());
    		sh.getRow(14).getCell(j + 7).setCellStyle(getStyle3());
    		sh.getRow(14).getCell(j + 8).setCellStyle(getStyle3());
    		for (int m = 0; m <= 8; m++) {
    		    if (m == 0) {
    			sh.getRow(13).getCell(j).setCellStyle(getStyle5());
    		    }else{
    			sh.getRow(13).createCell(j+m).setCellStyle(getStyle5());
    		    }
    		    for (int n = 15; n <= 46; n++) {
    			sh.getRow(n).createCell(j + m).setCellStyle(getStyle5());
    			sh.getRow(n).getCell(j + m).setCellValue("-");
    		    }
    		}
    		sh.addMergedRegion(new CellRangeAddress(13, 13, j, j + 8));
    		j = j + 9;
    	    }

    	}
    	sh.getRow(14).createCell(0).setCellStyle(getStyle0());
    	sh.addMergedRegion(new CellRangeAddress(13, 14, 1, 1));
    	sh.addMergedRegion(new CellRangeAddress(13, 14, 2, 2));
//    	sh.setColumnWidth(0, 256 * 16);
//    	sh.setColumnWidth(1, 256 * 16);
//    	sh.setColumnWidth(2, 256 * 16);
    	for (int i = 0; i < j; i++) {
    	    sh.setColumnWidth(i, 256 * 16);
    	}

    	sh.getRow(13).setHeightInPoints(35);
    	sh.getRow(14).setHeightInPoints(40);
    	for(int k=0;k < mothList.size();k++){
			int colNum = 3 + 9 * (Integer.parseInt(getKey(mothMap, mothList.get(k))));
			for(int i=15 ;i<=46;i++ ){
				sh.getRow(i).createCell(colNum).setCellValue("未上传当期数据");
		    	sh.getRow(i).getCell(colNum).setCellStyle(getStyle6());
			}
		}
    	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
    	for (int i = 0; i < data.size(); i++) {
    	    Map<String, Object> row = data.get(i);
    	    // 插入单元格数据
    	    if (row.size() == 0) {
    		sh.createRow(2 + i).createCell(2).setCellValue("-");
    	    } else {
    		int rowNumber = 0;
    		int colNumber = 3 + 9 * (Integer.parseInt(getKey(mothMap, row.get("aud_trm").toString())));
    		    rowNumber = row.get("ranking")==null?15:(Integer)row.get("ranking") + 15;
//    		    if (row.get("ranking") != null) {
//    			sh.createRow(rowNumber).createCell(1).setCellValue(row.get("ranking").toString());
//    			sh.getRow(rowNumber).getCell(1).setCellStyle(getStyle6());
//    		    }
    		    if(mothList.get(0).equals(row.get("aud_trm").toString()) &&row.get("ranking") != null){
    		    	sh.getRow(rowNumber).getCell(1).setCellValue((Integer)row.get("ranking"));
    		    }
    		    if(mothList.get(0).equals(row.get("aud_trm").toString()) 
    		    		&&(row.get("yszs_cnt_org") ==null || row.get("yszs_cnt_org").toString().equals("0"))){
    		    	sh.getRow(rowNumber).getCell(1).setCellValue("-");
    		    }
    		    if (row.get("CMCC_prov_prvd_nm") != null) {
    			sh.getRow(rowNumber).createCell(2).setCellValue(row.get("CMCC_prov_prvd_nm").toString());
    			sh.getRow(rowNumber).getCell(2).setCellStyle(getStyle6());
//    			for(int k=0;k < mothList.size();k++){
//    				int colNum = 3 + 9 * (Integer.parseInt(getKey(mothMap, mothList.get(k))));
//    				sh.getRow(rowNumber).createCell(colNum).setCellValue("未上传当期数据");
//    		    	sh.getRow(rowNumber).getCell(colNum).setCellStyle(getStyle6());
//    			}
    		    }
    		    //统付涉及集团客户数
    		    if (row.get("tf_cnt_org") != null) {
    			sh.getRow(rowNumber).createCell(colNumber).setCellValue((Integer) row.get("tf_cnt_org"));
    			sh.getRow(rowNumber).getCell(colNumber).setCellStyle(getStyle6());
    		    }else{
    		    	sh.getRow(rowNumber).createCell(colNumber).setCellValue("未上传当期数据");
    		    	sh.getRow(rowNumber).getCell(colNumber).setCellStyle(getStyle6());
    		    }
    		    //'统付涉及统付流量总值（G
    		    if (row.get("tf_sum_strm_cap") != null) {
    			sh.getRow(rowNumber).createCell(colNumber + 1).setCellValue(
    					row.get("tf_sum_strm_cap") instanceof BigDecimal ? ((BigDecimal) row.get("tf_sum_strm_cap")).doubleValue() : (Long) row.get("tf_sum_strm_cap"));
    			sh.getRow(rowNumber).getCell(colNumber+1).setCellStyle(getStyle5());
    		    }
    		    //统付流量订购次数
    		    if (row.get("tf_sum_cnt") != null) {
        			sh.getRow(rowNumber).createCell(colNumber + 2).setCellValue((Integer) row.get("tf_sum_cnt"));
        			sh.getRow(rowNumber).getCell(colNumber+2).setCellStyle(getStyle6());
        		    }
    		    //统付涉及号码总数  
    		    if (row.get("tf_sum_msisdn") != null) {
        			sh.getRow(rowNumber).createCell(colNumber + 3).setCellValue((Integer)row.get("tf_sum_msisdn"));
        			sh.getRow(rowNumber).getCell(colNumber+3).setCellStyle(getStyle6());
        		    }
    		    //疑似流量转售集团客户数
    		    if (row.get("yszs_cnt_org") != null) {
        			sh.getRow(rowNumber).createCell(colNumber + 4).setCellValue((Integer)row.get("yszs_cnt_org"));
        			sh.getRow(rowNumber).getCell(colNumber+4).setCellStyle(getStyle6());
        		    }
    		    //疑似转售流量总值（G
    		    if (row.get("yszs_sum_strm_cap") != null) {
        			sh.getRow(rowNumber).createCell(colNumber + 5).setCellValue(
        					row.get("yszs_sum_strm_cap") instanceof BigDecimal ? ((BigDecimal) row.get("yszs_sum_strm_cap")).doubleValue() : (Long) row.get("yszs_sum_strm_cap"));
        			sh.getRow(rowNumber).getCell(colNumber+5).setCellStyle(getStyle5());
        		    }
    		    //疑似转售总次数
    		    if (row.get("yszs_sum_cnt") != null) {
        			sh.getRow(rowNumber).createCell(colNumber + 6).setCellValue((Integer)row.get("yszs_sum_cnt"));
        			sh.getRow(rowNumber).getCell(colNumber+6).setCellStyle(getStyle6());
        		    }
    		    //违规转入号码总数
    		    if (row.get("yszs_sum_msisdn") != null) {
        			sh.getRow(rowNumber).createCell(colNumber + 7).setCellValue((Integer)row.get("yszs_sum_msisdn"));
        			sh.getRow(rowNumber).getCell(colNumber+7).setCellStyle(getStyle6());
        		    }
    		    //疑似转售流量占比
    		    if (row.get("yszs_propor") != null) {
        			sh.getRow(rowNumber).createCell(colNumber + 8).setCellValue(
        					row.get("yszs_propor") instanceof BigDecimal ? ((BigDecimal) row.get("yszs_propor")).doubleValue() : (Long) row.get("yszs_propor"));
        			sh.getRow(rowNumber).getCell(colNumber+8).setCellStyle(getStyle8());
        		    }
    	    }
    	}
     }
    
    /**
     * <pre>
     * Desc  
     * @param data：获取的数据
     * @param sh：应插入的sheet
     * @param prvdId：省份
     * @param month：月份
     * </pre>
     */
    public void writeFirstPart(List<Map<String, Object>> data, Sheet sh, String month) {
		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("“疑似违规流量转售”持续审计结果摘要");
		// 创建行
		sh.createRow(2).createCell(0).setCellStyle(getStyle9());
		sh.createRow(3).createCell(1).setCellStyle(getStyle13());
		for (int i = 4; i <= 8; i++) {
		    sh.createRow(i).createCell(1).setCellStyle(getStyle3());
//		    sh.getRow(i).setHeightInPoints(30);
		}
		sh.getRow(2).getCell(0).setCellValue("总体情况");
		sh.getRow(3).getCell(1).setCellValue("审计月");
		sh.getRow(4).getCell(1).setCellValue("统付涉及集团客户数");
		sh.getRow(5).getCell(1).setCellValue("统付涉及统付流量总值（G）");
		sh.getRow(6).getCell(1).setCellValue("疑似违规流量转售集团客户数（个）");
		sh.getRow(7).getCell(1).setCellValue("疑似违规流量转售涉及流量总值(G)");
		sh.getRow(8).getCell(1).setCellValue("疑似违规转售流量占总统付流量比（%）");
		sh.getRow(2).getCell(0).setCellStyle(getStyle10());
		for (int i = 4; i <= 8; i++) {
		    sh.getRow(i).createCell(2).setCellValue("-");
		}
		int j = 1;
		// 设置第一行单元格格式 及 合并单元格 
		sh.getRow(0).getCell(0).setCellStyle(getStyle00());
		sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
		sh.addMergedRegion(new CellRangeAddress(2, 2, 0, 4));
		sh.setColumnWidth(0, 256 * 35);
		sh.setColumnWidth(2, 256 * 18);
		//cnt_org_id, cnt_msisdn,sum_strm_cap,sum_cnt,abn_tra_rat
		sh.getRow(3).createCell(2).setCellValue(month);
		
		HashMap<String, String> mothMap = new HashMap<String, String>();
    	List<String> mothList = getAudTrmListToSomeDate(month, "201710");
		for (int i = 0; i < mothList.size(); i++) {
    	    String moth = mothList.get(i);
    	    if (!mothMap.containsValue(moth)) {
    	    	mothMap.put(Integer.toString(i), moth);
    	    }
    	    int colNumber = 2 + (Integer.parseInt(getKey(mothMap, mothList.get(i))));
    	    sh.getRow(3).createCell(colNumber).setCellValue(mothList.get(i));
    	    sh.getRow(4).createCell(colNumber).setCellValue("-");
    	    sh.getRow(5).createCell(colNumber).setCellValue("-");
    	    sh.getRow(6).createCell(colNumber).setCellValue("-");
    	    sh.getRow(7).createCell(colNumber).setCellValue("-");
    	    sh.getRow(8).createCell(colNumber).setCellValue("-");
    	    sh.getRow(3).getCell(colNumber).setCellStyle(getStyle6());
			sh.getRow(4).getCell(colNumber).setCellStyle(getStyle6());
			sh.getRow(5).getCell(colNumber).setCellStyle(getStyle5());
			sh.getRow(6).getCell(colNumber).setCellStyle(getStyle6());
			sh.getRow(7).getCell(colNumber).setCellStyle(getStyle5());
			sh.getRow(8).getCell(colNumber).setCellStyle(getStyle8());
		}
		for (int i = 0; i < data.size(); i++) {
    	    Map<String, Object> map = data.get(i);
    	    // 插入单元格数据
    	    if (map.size() == 0) {
    		sh.createRow(2 + i).createCell(2).setCellValue("-");
    	    } else {
    	    	int colNumber = 2 + (Integer.parseInt(getKey(mothMap, map.get("aud_trm").toString())));
				if(map != null && map.size() > 0){
					if(map.get("tf_cnt_org")!=null){
						sh.getRow(4).createCell(colNumber).setCellValue((Integer) map.get("tf_cnt_org"));
					}
					if(map.get("tf_sum_strm_cap") != null){
						sh.getRow(5).createCell(colNumber).setCellValue(
								map.get("tf_sum_strm_cap") instanceof BigDecimal ? ((BigDecimal) map.get("tf_sum_strm_cap")).doubleValue() : (Long) map.get("tf_sum_strm_cap"));
					}
					if(map.get("yszs_cnt_org") != null){
						sh.getRow(6).createCell(colNumber).setCellValue((Integer) map.get("yszs_cnt_org"));
					}
					if(map.get("yszs_sum_strm_cap") != null){
						sh.getRow(7).createCell(colNumber).setCellValue(
								map.get("yszs_sum_strm_cap") instanceof BigDecimal ? ((BigDecimal) map.get("yszs_sum_strm_cap")).doubleValue() : (Long) map.get("yszs_sum_strm_cap"));
					}
					if(map.get("yszs_propor") != null){
						sh.getRow(8).createCell(colNumber).setCellValue(
								map.get("yszs_propor") instanceof BigDecimal ? ((BigDecimal) map.get("yszs_propor")).doubleValue() : (Long) map.get("yszs_propor"));
					}
					sh.getRow(3).getCell(colNumber).setCellStyle(getStyle6());
					sh.getRow(4).getCell(colNumber).setCellStyle(getStyle6());
					sh.getRow(5).getCell(colNumber).setCellStyle(getStyle5());
					sh.getRow(6).getCell(colNumber).setCellStyle(getStyle6());
					sh.getRow(7).getCell(colNumber).setCellStyle(getStyle5());
					sh.getRow(8).getCell(colNumber).setCellStyle(getStyle8());
				}
    	    }
		}

    }


    public static String getKey(HashMap<String, String> map, String value) {
	String key = null;
	// Map,HashMap并没有实现Iteratable接口.不能用于增强for循环.
	for (String getKey : map.keySet()) {
	    if (map.get(getKey).equals(value)) {
		key = getKey;
	    }
	}
	return key;
	// 这个key肯定是最后一个满足该条件的key.
    }



	private CellStyle getStyle0() {
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		return style;
	}

	private CellStyle getStyle1() {
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 11);
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setWrapText(true);
		// style.setFillBackgroundColor(HSSFColor.BLUE.index);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return style;
	}

	private CellStyle getStyle2() {
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font font = wb.createFont();
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setWrapText(true);
		return style;
	}


	// modified by GuoXY 20161024 for
	// CMCC_集中化持续审计系统_软件需求规格说明书_社会渠道终端套利专题的逻辑和变更说明v1.7 郭倩
	private CellStyle getStyle7() {
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		style.setWrapText(true);
		return style;
	}

	private CellStyle getStyle3() {
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setWrapText(true);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return style;
	}
	
	private CellStyle getStyle13() {
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setWrapText(true);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return style;
	}
	
	private CellStyle getStyle00() {
		CellStyle style0 = wb.createCellStyle();
		style0.setAlignment(CellStyle.ALIGN_CENTER);
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 14);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style0.setFont(font);
		return style0;
	}

	
	private CellStyle getStyle14() {
		CellStyle style0 = wb.createCellStyle();
		style0.setAlignment(CellStyle.ALIGN_LEFT);
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 14);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style0.setFont(font);
		style0.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
		return style0;
	}
	
	  private CellStyle getStyle4() {
			CellStyle style0 = wb.createCellStyle();
			style0.setAlignment(CellStyle.ALIGN_CENTER);
			style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			Font font = wb.createFont();
			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 12);
			style0.setFont(font);
			style0.setBorderBottom(CellStyle.BORDER_THIN);
			style0.setBorderLeft(CellStyle.BORDER_THIN);
			style0.setBorderRight(CellStyle.BORDER_THIN);
			style0.setBorderTop(CellStyle.BORDER_THIN);
			style0.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			return style0;
		    }

		    private CellStyle getStyle5() {
			CellStyle style = wb.createCellStyle();
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			Font font = wb.createFont();
			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 12);
			style.setFont(font);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setWrapText(true);
			style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
			return style;
		    }
	

	private CellStyle getStyle6() {
		CellStyle style0 = wb.createCellStyle();
		style0.setAlignment(CellStyle.ALIGN_CENTER);
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 11);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
		return style0;
	}
	
	private CellStyle getStyle66() {
		CellStyle style0 = wb.createCellStyle();
		style0.setAlignment(CellStyle.ALIGN_CENTER);
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 11);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		return style0;
	}

	private CellStyle getStyle8() {
		CellStyle style0 = wb.createCellStyle();
		style0.setAlignment(CellStyle.ALIGN_CENTER);
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setDataFormat(wb.createDataFormat().getFormat("0.00"));
		return style0;
	}

	private CellStyle getStyle9() {
		CellStyle style0 = wb.createCellStyle();
		style0.setAlignment(CellStyle.ALIGN_CENTER);
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 11);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setWrapText(true);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style0.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
		return style0;
	}

	private CellStyle getStyle10() {
		CellStyle style0 = wb.createCellStyle();
		style0.setAlignment(CellStyle.ALIGN_LEFT);
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 14);
		style0.setFont(font);
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setBorderTop(CellStyle.BORDER_THIN);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style0.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
		return style0;
	}
}