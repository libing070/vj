/**
 * com.hp.cmcc.job.service.NotiFileLlzs8000PmhzProcessor.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2017-8-10 下午3:54:52
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-8-10 	   hufei 	         1. Created this class.
 * </pre>
 */
@Service("NotiFileLlzs8000PmhzProcessor")
public class NotiFileLlzs8000PmhzProcessor extends AbstractNotiFileProcessor {

    protected String	       focusCd  = "8000";
    protected Logger	       logger   = Logger.getLogger(this.getClass());
    private Map<String, CellStyle> mapStyle = new HashMap<String, CellStyle>();
    private String		 prvdId   = "10000";
    private String		 prvdName = "全国";
    private ArrayList<String>      strs     = new ArrayList<String>();
    private SXSSFWorkbook	  wb;

    public ArrayList<String> getFileNameList() {
	return strs;
    }

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

    @Override
    public void start() throws Exception {
	List<Map<String, Object>> prvdList = notiFileGenService.getPrvdAndCode();
	Map<String, Object> row = new HashMap<String, Object>();
	FileOutputStream out = null;
	for (int i = 0; i < prvdList.size(); i++) {
	    row = prvdList.get(i);
	    if (row.size() != 0) {
		prvdId = row.get("CMCC_prov_prvd_cd").toString();
		prvdName = row.get("CMCC_prov_prvd_nm").toString();
		wb = new SXSSFWorkbook(500);
		this.generate();
		out = null;
		try {
		    out = new FileOutputStream(this.getLocalPath());
		    wb.write(out);
		    strs.add(fileName);
		} catch (IOException e) {
		    throw e;
		} catch (Exception e) {
		    throw e;
		} finally {
		    try {
			out.close();
		    } catch (IOException e) {
			throw e;
		    }
		}
	    }
	}

    }

    public boolean generate() throws Exception {
	mapStyle.put("style0", getStyle0());
	mapStyle.put("style1", getStyle1());
	mapStyle.put("style2", getStyle2());
	mapStyle.put("style3", getStyle3());
	mapStyle.put("style4", getStyle4());
	mapStyle.put("style5", getStyle5());
	mapStyle.put("style6", getStyle6());
	if (Integer.parseInt(month) >= 201710) {

	    if ("10000".equals(prvdId)) {
		this.setFileName("流量异常赠送_排名汇总_全国_" + month);
		// 更新最新排名
		notiFileGenService.getNotiFileLlzs8000Pmhz(month, prvdId, "updateReportSort");
		// 创建第一个sheet
		sh = wb.createSheet("排名汇总");
		writeSheet1FirstPart(notiFileGenService.getNotiFileLlzs8000Pmhz(month, prvdId, "allReport"), sh, month);
		writeSheet1SecondPart(notiFileGenService.getNotiFileLlzs8000Pmhz(month, prvdId, "prvdReport"), sh, month);
		sh = wb.createSheet("异常赠送排名前50地市清单");
		writeSheet2(notiFileGenService.getNotiFileLlzs8000Pmhz(month, prvdId, "cityTop50"), sh, month);
		writeOfferTop50Sheet(notiFileGenService.getNotiFileLlzs8000Pmhz(month, prvdId, "offerTop50"));
		writeChnlTop50Sheet(notiFileGenService.getNotiFileLlzs8000Pmhz(month, prvdId, "chnlTop50"));
	    } else {
		this.setFileName("流量异常赠送_排名汇总_" + prvdName + "_" + month);
		// 创建第一个sheet
		sh = wb.createSheet("排名汇总");
		writeFirstPart(notiFileGenService.getNotiFileLlzs8000Pmhz(month, prvdId, "prvdInfo"), sh, month);
		writeSecondPart(notiFileGenService.getNotiFileLlzs8000Pmhz(month, prvdId, "city"), sh);
		writePrvdOfferSheet(notiFileGenService.getNotiFileLlzs8000Pmhz(month, prvdId, "offer"));
		writePrvdChnlSheet(notiFileGenService.getNotiFileLlzs8000Pmhz(month, prvdId, "chnl"));
	    }

	}

	return true;
    }

    public void writeChnlTop50Sheet(List<Map<String, Object>> data) {
	sh = wb.createSheet("异常赠送排名前50渠道清单");
	String title = "全国流量异常赠送排名前50渠道清单";
	String[] chnlRowTitle = { "排名", "渠道名称", "渠道标识", "渠道所在省份", "渠道所在地市", "总赠送流量（G）", "总赠送流量次数", "总赠送用户数", "异常赠送流量（G）", "异常赠送次数", "异常赠送涉及用户数", "异常赠送流量占比", "高频赠送流量（G）", "高频赠送次数", "高频赠送流量占比",
		"高额赠送流量（G）", "高额赠送次数", "高额赠送流量占比", "向非正常用户赠送流量（G）", "向非正常用户赠送次数", "向非正常用户赠送流量占比" };
	writeSheet3(data, title, chnlRowTitle, sh);
    }

    public void writeOfferTop50Sheet(List<Map<String, Object>> data) {
	sh = wb.createSheet("异常赠送排名前50营销案清单");
	String title = "全国异常赠送排名前50营销案清单";
	String[] offerRowTitle = { "排名", "营销案名称", "营销案编号", "营销案所在省份", "营销案所在地市", "总赠送流量（G）", "总赠送流量次数", "总赠送用户数", "异常赠送流量（G）", "异常赠送次数", "异常赠送涉及用户数", "异常赠送流量占比", "高频赠送流量（G）", "高频赠送次数", "高频赠送流量占比",
		"高额赠送流量（G）", "高额赠送次数", "高额赠送流量占比", "向非正常用户赠送流量（G）", "向非正常用户赠送次数", "向非正常用户赠送流量占比" };
	writeSheet3(data, title, offerRowTitle, sh);
    }

    public void writePrvdOfferSheet(List<Map<String, Object>> data) {
	sh = wb.createSheet("异常赠送营销案清单");
	String title = "异常赠送营销案清单";
	String[] offerRowTitle = { "排名", "营销案名称", "营销案编号", "营销案所在省份", "营销案所在地市", "总赠送流量（G）", "总赠送流量次数", "总赠送用户数", "异常赠送流量（G）", "异常赠送次数", "异常赠送涉及用户数", "异常赠送流量占比", "高频赠送流量（G）", "高频赠送次数", "高频赠送流量占比",
		"高额赠送流量（G）", "高额赠送次数", "高额赠送流量占比", "向非正常用户赠送流量（G）", "向非正常用户赠送次数", "向非正常用户赠送流量占比" };
	writeSheet4(data, title, offerRowTitle, sh);
    }

    public void writePrvdChnlSheet(List<Map<String, Object>> data) {
	sh = wb.createSheet("异常赠送渠道清单");
	String title = "流量异常赠送渠道清单";
	String[] chnlRowTitle = { "排名", "渠道名称", "渠道标识", "渠道所在省份", "渠道所在地市", "总赠送流量（G）", "总赠送流量次数", "总赠送用户数", "异常赠送流量（G）", "异常赠送次数", "异常赠送涉及用户数", "异常赠送流量占比", "高频赠送流量（G）", "高频赠送次数", "高频赠送流量占比",
		"高额赠送流量（G）", "高额赠送次数", "高额赠送流量占比", "向非正常用户赠送流量（G）", "向非正常用户赠送次数", "向非正常用户赠送流量占比" };
	writeSheet4(data, title, chnlRowTitle, sh);
    }

    public void writeSheet1FirstPart(List<Map<String, Object>> data, Sheet sh, String month) {
	sh.createRow(0);
	sh.getRow(0).createCell(0).setCellValue("“流量异常赠送”持续审计结果摘要");
	// 创建行
	sh.createRow(1).createCell(0);
	sh.createRow(2).createCell(0).setCellStyle(mapStyle.get("style0"));
	HashMap<String, String> mothMap = new HashMap<String, String>();
	List<String> mothList = getAudTrmListToSomeDate(month, "201710");
	for (int i = 3; i <= 10; i++) {
	    sh.createRow(i).createCell(1).setCellStyle(mapStyle.get("style3"));
	}
	for (int i = 0, mothSize = mothList.size(); i < mothSize; i++) {
	    String moth = mothList.get(i);
	    if (!mothMap.containsValue(moth)) {
		mothMap.put(Integer.toString(i), moth);
		Integer thisMonth = Integer.parseInt("0".equals(moth.substring(4, 5)) ? moth.substring(5, 6) : moth.substring(4, 6));
		for (int n = 3; n < 11; n++) {
		    sh.getRow(n).createCell(i + 2).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(n).getCell(i + 2).setCellValue("-");
		}
		sh.getRow(3).getCell(i + 2).setCellValue(moth.substring(0, 4) + "年" + thisMonth + "月");
	    }
	}
	sh.getRow(3).getCell(1).setCellStyle(mapStyle.get("style1"));
	sh.getRow(2).getCell(0).setCellValue("总体情况");
	sh.getRow(3).getCell(1).setCellValue("审计月");
	sh.getRow(4).getCell(1).setCellValue("总赠送流量（G）");
	sh.getRow(5).getCell(1).setCellValue("总赠送流量次数");
	sh.getRow(6).getCell(1).setCellValue("总赠送用户数");
	sh.getRow(7).getCell(1).setCellValue("异常赠送流量（G）");
	sh.getRow(8).getCell(1).setCellValue("异常赠送次数");
	sh.getRow(9).getCell(1).setCellValue("异常赠送涉及用户数");
	sh.getRow(10).getCell(1).setCellValue("异常赠送流量占比");

	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.getRow(0).getCell(0).setCellStyle(mapStyle.get("style2"));

	Map<String, Object> row = new HashMap<String, Object>();
	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
	for (int i = 0; i < data.size(); i++) {
	    row = data.get(i);
	    if (row.size() != 0) {
		// 插入单元格数据
		int colNumber = 2 + Integer.parseInt(getKey(mothMap, row.get("aud_trm").toString().trim()));
		if (row.get("giv_value_tol") != null) {
		    sh.getRow(4).getCell(colNumber)
			    .setCellValue(row.get("giv_value_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_value_tol")).doubleValue() : (Long) row.get("giv_value_tol"));
		}
		if (row.get("giv_cnt_tol") != null) {
		    sh.getRow(5).getCell(colNumber).setCellValue(row.get("giv_cnt_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_cnt_tol")).doubleValue() : (Long) row.get("giv_cnt_tol"));
		}
		if (row.get("giv_subs_tol") != null) {
		    sh.getRow(6).getCell(colNumber).setCellValue(row.get("giv_subs_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_subs_tol")).doubleValue() : (Long) row.get("giv_subs_tol"));
		}
		if (row.get("infrac_value") != null) {
		    sh.getRow(7).getCell(colNumber).setCellValue(row.get("infrac_value") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_value")).doubleValue() : (Long) row.get("infrac_value"));
		}
		if (row.get("infrac_cnt") != null) {
		    sh.getRow(8).getCell(colNumber).setCellValue(row.get("infrac_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_cnt")).doubleValue() : (Long) row.get("infrac_cnt"));
		}
		if (row.get("infrac_subs_cnt") != null) {
		    sh.getRow(9).getCell(colNumber)
			    .setCellValue(row.get("infrac_subs_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_subs_cnt")).doubleValue() : (Long) row.get("infrac_subs_cnt"));
		}
		if (row.get("infrac_value_pre") != null) {
		    sh.getRow(10).getCell(colNumber)
			    .setCellValue(row.get("infrac_value_pre") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_value_pre")).doubleValue() : (Long) row.get("infrac_value_pre"));
		}
		sh.getRow(4).getCell(colNumber).setCellStyle(mapStyle.get("style5"));
		sh.getRow(7).getCell(colNumber).setCellStyle(mapStyle.get("style5"));
		sh.getRow(10).getCell(colNumber).setCellStyle(mapStyle.get("style4"));
	    }
	}

    }

    public void writeSheet1SecondPart(List<Map<String, Object>> data, Sheet sh, String month) {
	sh.createRow(13);
	sh.getRow(13).createCell(0).setCellValue("各公司“流量异常赠送”排名情况");
	// 创建行
	for (int i = 15; i <= 47; i++) {
	    sh.createRow(i).createCell(1).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(i).createCell(2).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(i).getCell(1).setCellValue("-");
	    sh.getRow(i).getCell(2).setCellValue("-");
	}
	sh.createRow(14).createCell(1).setCellValue("排名");
	sh.getRow(14).createCell(2).setCellValue("公司");
	sh.getRow(14).getCell(1).setCellStyle(mapStyle.get("style1"));
	sh.getRow(14).getCell(2).setCellStyle(mapStyle.get("style1"));
	int j = 3;

	HashMap<String, String> mothMap = new HashMap<String, String>();
	List<String> mothList = getAudTrmListToSomeDate(month, "201710");
	for (int i = 0, mothSize = mothList.size(); i < mothSize; i++) {
	    String moth = mothList.get(i);
	    if (!mothMap.containsValue(moth)) {
		mothMap.put(Integer.toString(i), moth);
		Integer thisMonth = Integer.parseInt("0".equals(moth.substring(4, 5)) ? moth.substring(5, 6) : moth.substring(4, 6));
		sh.getRow(14).createCell(j).setCellValue(moth.substring(0, 4) + "年" + thisMonth + "月");
		sh.getRow(15).createCell(j).setCellValue("总赠送流量（G）");
		sh.getRow(15).createCell(j + 1).setCellValue("总赠送流量次数");
		sh.getRow(15).createCell(j + 2).setCellValue("总赠送用户数");
		sh.getRow(15).createCell(j + 3).setCellValue("异常赠送流量（G）");
		sh.getRow(15).createCell(j + 4).setCellValue("异常赠送次数");
		sh.getRow(15).createCell(j + 5).setCellValue("异常赠送涉及用户数");
		sh.getRow(15).createCell(j + 6).setCellValue("异常赠送流量占比");
		sh.getRow(15).createCell(j + 7).setCellValue("高频赠送流量（G）");
		sh.getRow(15).createCell(j + 8).setCellValue("高频赠送次数");
		sh.getRow(15).createCell(j + 9).setCellValue("高频赠送流量占比");
		sh.getRow(15).createCell(j + 10).setCellValue("高额赠送流量（G）");
		sh.getRow(15).createCell(j + 11).setCellValue("高额赠送次数");
		sh.getRow(15).createCell(j + 12).setCellValue("高额赠送流量占比");
		sh.getRow(15).createCell(j + 13).setCellValue("向非正常用户赠送流量（G）");
		sh.getRow(15).createCell(j + 14).setCellValue("向非正常用户赠送次数");
		sh.getRow(15).createCell(j + 15).setCellValue("向非正常用户赠送流量占比");

		for (int m = 0; m <= 15; m++) {
		    if (m == 0) {
			sh.getRow(14).getCell(j).setCellStyle(mapStyle.get("style5"));
		    } else {
			sh.getRow(14).createCell(j + m).setCellStyle(mapStyle.get("style5"));
		    }
		    sh.getRow(15).getCell(j + m).setCellStyle(mapStyle.get("style3"));
		    for (int n = 16; n <= 47; n++) {
			sh.getRow(n).createCell(j + m).setCellStyle(mapStyle.get("style5"));
			if (m == 0) {
			    sh.getRow(n).getCell(j).setCellValue("未上传当期数据");
			} else {
			    sh.getRow(n).getCell(j + m).setCellValue("-");
			}

		    }
		}
		sh.addMergedRegion(new CellRangeAddress(14, 14, j, j + 15));
		j = j + 16;
	    }

	}

	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.getRow(13).getCell(0).setCellStyle(mapStyle.get("style0"));
	sh.addMergedRegion(new CellRangeAddress(13, 13, 0, 4));
	sh.addMergedRegion(new CellRangeAddress(14, 15, 1, 1));
	sh.addMergedRegion(new CellRangeAddress(14, 15, 2, 2));
	sh.setColumnWidth(1, 256 * 32);
	sh.setColumnWidth(2, 256 * 20);
	for (int i = 3; i < j; i++) {
	    sh.setColumnWidth(i, 256 * 16);
	}

	sh.getRow(14).setHeightInPoints(35);
	sh.getRow(15).setHeightInPoints(40);

	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
	Map<String, Object> row = new HashMap<String, Object>();
	for (int i = 0, dataSize = data.size(); i < dataSize; i++) {
	    row = data.get(i);
	    // 插入单元格数据
	    if (row.size() != 0) {
		int rowNumber = 16;
		int colNumber = 3 + 16 * (Integer.parseInt(getKey(mothMap, row.get("aud_trm").toString().trim())));
		if ((row.get("rank_china_new") == null || (Integer) row.get("rank_china_new") == 0) && (Integer)row.get("prvd_id")!=10000) {

		} else {
		    if (row.get("rank_china_new") != null && (Integer) row.get("rank_china_new") != 0) {
			rowNumber = (Integer) row.get("rank_china_new") + rowNumber;
			if (row.get("prvd_nm") != null) {
			    sh.getRow(rowNumber).getCell(2).setCellValue(row.get("prvd_nm").toString());
			}
			if (row.get("giv_value_tol") != null) {
			    sh.getRow(rowNumber).getCell(1).setCellValue((Integer) row.get("rank_china_new"));
			}
		    } else {
			sh.getRow(rowNumber).getCell(2).setCellValue("全国");
		    }
		    if (row.get("giv_value_tol") != null) {
			sh.getRow(rowNumber).createCell(colNumber)
				.setCellValue(row.get("giv_value_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_value_tol")).doubleValue() : (Long) row.get("giv_value_tol"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber).setCellValue("未上传当期数据");
		    }
		    if (row.get("giv_cnt_tol") != null) {
			sh.getRow(rowNumber).createCell(colNumber + 1)
				.setCellValue(row.get("giv_cnt_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_cnt_tol")).doubleValue() : (Long) row.get("giv_cnt_tol"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber + 1).setCellValue("-");
		    }
		    if (row.get("giv_subs_tol") != null) {
			sh.getRow(rowNumber).createCell(colNumber + 2)
				.setCellValue(row.get("giv_subs_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_subs_tol")).doubleValue() : (Long) row.get("giv_subs_tol"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber + 2).setCellValue("-");
		    }
		    if (row.get("infrac_value") != null) {
			sh.getRow(rowNumber).createCell(colNumber + 3)
				.setCellValue(row.get("infrac_value") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_value")).doubleValue() : (Long) row.get("infrac_value"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber + 3).setCellValue("-");
		    }
		    if (row.get("infrac_cnt") != null) {
			sh.getRow(rowNumber).createCell(colNumber + 4)
				.setCellValue(row.get("infrac_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_cnt")).doubleValue() : (Long) row.get("infrac_cnt"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber + 4).setCellValue("-");
		    }
		    if (row.get("infrac_subs_cnt") != null) {
			sh.getRow(rowNumber).createCell(colNumber + 5)
				.setCellValue(row.get("infrac_subs_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_subs_cnt")).doubleValue() : (Long) row.get("infrac_subs_cnt"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber + 5).setCellValue("-");
		    }
		    if (row.get("infrac_value_pre") != null) {
			sh.getRow(rowNumber).createCell(colNumber + 6)
				.setCellValue(row.get("infrac_value_pre") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_value_pre")).doubleValue() : (Long) row.get("infrac_value_pre"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber + 6).setCellValue("-");
		    }
		    if (row.get("hf_value") != null) {
			sh.getRow(rowNumber).createCell(colNumber + 7)
				.setCellValue(row.get("hf_value") instanceof BigDecimal ? ((BigDecimal) row.get("hf_value")).doubleValue() : (Long) row.get("hf_value"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber + 7).setCellValue("-");
		    }
		    if (row.get("hf_cnt") != null) {
			sh.getRow(rowNumber).createCell(colNumber + 8)
				.setCellValue(row.get("hf_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("hf_cnt")).doubleValue() : (Long) row.get("hf_cnt"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber + 8).setCellValue("-");
		    }
		    if (row.get("hf_value_per") != null) {
			sh.getRow(rowNumber).createCell(colNumber + 9)
				.setCellValue(row.get("hf_value_per") instanceof BigDecimal ? ((BigDecimal) row.get("hf_value_per")).doubleValue() : (Long) row.get("hf_value_per"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber + 9).setCellValue("-");
		    }
		    if (row.get("hq_value") != null) {
			sh.getRow(rowNumber).createCell(colNumber + 10)
				.setCellValue(row.get("hq_value") instanceof BigDecimal ? ((BigDecimal) row.get("hq_value")).doubleValue() : (Long) row.get("hq_value"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber + 10).setCellValue("-");
		    }
		    if (row.get("hq_cnt") != null) {
			sh.getRow(rowNumber).createCell(colNumber + 11)
				.setCellValue(row.get("hq_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("hq_cnt")).doubleValue() : (Long) row.get("hq_cnt"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber + 11).setCellValue("-");
		    }
		    if (row.get("hq_value_per") != null) {
			sh.getRow(rowNumber).createCell(colNumber + 12)
				.setCellValue(row.get("hq_value_per") instanceof BigDecimal ? ((BigDecimal) row.get("hq_value_per")).doubleValue() : (Long) row.get("hq_value_per"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber + 12).setCellValue("-");
		    }
		    if (row.get("yk_value") != null) {
			sh.getRow(rowNumber).createCell(colNumber + 13)
				.setCellValue(row.get("yk_value") instanceof BigDecimal ? ((BigDecimal) row.get("yk_value")).doubleValue() : (Long) row.get("yk_value"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber + 13).setCellValue("-");
		    }
		    if (row.get("yk_cnt") != null) {
			sh.getRow(rowNumber).createCell(colNumber + 14)
				.setCellValue(row.get("yk_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("yk_cnt")).doubleValue() : (Long) row.get("yk_cnt"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber + 14).setCellValue("-");
		    }
		    if (row.get("yk_value_per") != null) {
			sh.getRow(rowNumber).createCell(colNumber + 15)
				.setCellValue(row.get("yk_value_per") instanceof BigDecimal ? ((BigDecimal) row.get("yk_value_per")).doubleValue() : (Long) row.get("yk_value_per"));
		    } else {
			sh.getRow(rowNumber).createCell(colNumber + 15).setCellValue("-");
		    }
		    // 小数点
		    sh.getRow(rowNumber).getCell(colNumber).setCellStyle(mapStyle.get("style5"));
		    sh.getRow(rowNumber).getCell(colNumber + 3).setCellStyle(mapStyle.get("style5"));
		    for (int n = 7; n < 15; n = n + 3) {
			sh.getRow(rowNumber).getCell(colNumber + n).setCellStyle(mapStyle.get("style5"));
		    }
		    // 分位符
		    sh.getRow(rowNumber).getCell(2).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rowNumber).getCell(colNumber + 1).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rowNumber).getCell(colNumber + 2).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rowNumber).getCell(colNumber + 4).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rowNumber).getCell(colNumber + 5).setCellStyle(mapStyle.get("style6"));
		    for (int n = 8; n < 15; n = n + 3) {
			sh.getRow(rowNumber).getCell(colNumber + n).setCellStyle(mapStyle.get("style6"));
		    }
		    // 占比
		    for (int n = 6; n < 16; n = n + 3) {
			sh.getRow(rowNumber).getCell(colNumber + n).setCellStyle(mapStyle.get("style4"));
		    }
		}
	    }

	}

    }

    public void writeSheet2(List<Map<String, Object>> data, Sheet sh, String month) {
	sh.createRow(0);
	sh.getRow(0).createCell(0).setCellValue("全国异常赠送排名前50地市清单");
	sh.getRow(0).getCell(0).setCellStyle(mapStyle.get("style0"));
	// 创建行
	for (int i = 2; i <= 52; i++) {
	    sh.createRow(i).createCell(1).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(i).createCell(2).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(i).getCell(1).setCellValue("-");
	}
	sh.createRow(1).createCell(1).setCellValue("排名");
	sh.getRow(1).getCell(1).setCellStyle(mapStyle.get("style1"));
	sh.getRow(2).createCell(2).setCellValue("省份");
	sh.getRow(2).createCell(3).setCellValue("地市");
	sh.getRow(2).createCell(4).setCellValue("总赠送流量（G）");
	sh.getRow(2).createCell(5).setCellValue("总赠送流量次数");
	sh.getRow(2).createCell(6).setCellValue("总赠送用户数");
	sh.getRow(2).createCell(7).setCellValue("异常赠送流量（G）");
	sh.getRow(2).createCell(8).setCellValue("异常赠送次数");
	sh.getRow(2).createCell(9).setCellValue("异常赠送涉及用户数");
	sh.getRow(2).createCell(10).setCellValue("异常赠送流量占比");
	sh.getRow(2).createCell(11).setCellValue("高频赠送流量（G）");
	sh.getRow(2).createCell(12).setCellValue("高频赠送次数");
	sh.getRow(2).createCell(13).setCellValue("高频赠送流量占比");
	sh.getRow(2).createCell(14).setCellValue("高额赠送流量（G）");
	sh.getRow(2).createCell(15).setCellValue("高额赠送次数");
	sh.getRow(2).createCell(16).setCellValue("高额赠送流量占比");
	sh.getRow(2).createCell(17).setCellValue("向非正常用户赠送流量（G）");
	sh.getRow(2).createCell(18).setCellValue("向非正常用户赠送次数");
	sh.getRow(2).createCell(19).setCellValue("向非正常用户赠送流量占比");
	for (int i = 2; i < 20; i++) {
	    sh.getRow(1).createCell(i).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2).getCell(i).setCellStyle(mapStyle.get("style3"));
	    for (int n = 3; n <= 52; n++) {
		sh.getRow(n).createCell(i).setCellStyle(mapStyle.get("style5"));
		sh.getRow(n).getCell(i).setCellValue("-");
	    }
	}

	Integer thisMonth = Integer.parseInt("0".equals(month.substring(4, 5)) ? month.substring(5, 6) : month.substring(4, 6));
	sh.getRow(1).getCell(2).setCellValue(month.substring(0, 4) + "年" + thisMonth + "月");
	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
	sh.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
	sh.addMergedRegion(new CellRangeAddress(1, 1, 2, 19));
	for (int i = 0; i < 20; i++) {
	    sh.setColumnWidth(i, 256 * 16);
	}

	sh.getRow(1).setHeightInPoints(35);
	sh.getRow(2).setHeightInPoints(40);

	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
	Map<String, Object> row = new HashMap<String, Object>();
	for (int i = 0, dataSize = data.size(); i < dataSize; i++) {
	    row = data.get(i);
	    // 插入单元格数据
	    if (row.size() != 0) {
		int rowNumber = 0;

		if (row.get("rank_china") != null && (Integer) row.get("rank_china") != 0) {
		    rowNumber = (Integer) row.get("rank_china") + 2;
		    sh.getRow(rowNumber).getCell(1).setCellValue((Integer) row.get("rank_china"));
		    if (row.get("prvd_nm") != null) {
			sh.getRow(rowNumber).getCell(2).setCellValue(row.get("prvd_nm").toString());
		    }
		    if (row.get("cty_nm") != null) {
			sh.getRow(rowNumber).createCell(3).setCellValue(row.get("cty_nm").toString());
		    }
		    if (row.get("giv_value_tol") != null) {
			sh.getRow(rowNumber).createCell(4)
				.setCellValue(row.get("giv_value_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_value_tol")).doubleValue() : (Long) row.get("giv_value_tol"));
		    }
		    if (row.get("giv_cnt_tol") != null) {
			sh.getRow(rowNumber).createCell(5)
				.setCellValue(row.get("giv_cnt_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_cnt_tol")).doubleValue() : (Long) row.get("giv_cnt_tol"));
		    }
		    if (row.get("giv_subs_tol") != null) {
			sh.getRow(rowNumber).createCell(6)
				.setCellValue(row.get("giv_subs_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_subs_tol")).doubleValue() : (Long) row.get("giv_subs_tol"));
		    }
		    if (row.get("infrac_value") != null) {
			sh.getRow(rowNumber).createCell(7)
				.setCellValue(row.get("infrac_value") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_value")).doubleValue() : (Long) row.get("infrac_value"));
		    }
		    if (row.get("infrac_cnt") != null) {
			sh.getRow(rowNumber).createCell(8)
				.setCellValue(row.get("infrac_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_cnt")).doubleValue() : (Long) row.get("infrac_cnt"));
		    }
		    if (row.get("infrac_subs_cnt") != null) {
			sh.getRow(rowNumber).createCell(9)
				.setCellValue(row.get("infrac_subs_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_subs_cnt")).doubleValue() : (Long) row.get("infrac_subs_cnt"));
		    }
		    if (row.get("infrac_value_pre") != null) {
			sh.getRow(rowNumber).createCell(10)
				.setCellValue(row.get("infrac_value_pre") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_value_pre")).doubleValue() : (Long) row.get("infrac_value_pre"));
		    }
		    if (row.get("hf_value") != null) {
			sh.getRow(rowNumber).createCell(11).setCellValue(row.get("hf_value") instanceof BigDecimal ? ((BigDecimal) row.get("hf_value")).doubleValue() : (Long) row.get("hf_value"));
		    }
		    if (row.get("hf_cnt") != null) {
			sh.getRow(rowNumber).createCell(12).setCellValue(row.get("hf_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("hf_cnt")).doubleValue() : (Long) row.get("hf_cnt"));
		    }
		    if (row.get("hf_value_per") != null) {
			sh.getRow(rowNumber).createCell(13)
				.setCellValue(row.get("hf_value_per") instanceof BigDecimal ? ((BigDecimal) row.get("hf_value_per")).doubleValue() : (Long) row.get("hf_value_per"));
		    }
		    if (row.get("hq_value") != null) {
			sh.getRow(rowNumber).createCell(14).setCellValue(row.get("hq_value") instanceof BigDecimal ? ((BigDecimal) row.get("hq_value")).doubleValue() : (Long) row.get("hq_value"));
		    }
		    if (row.get("hq_cnt") != null) {
			sh.getRow(rowNumber).createCell(15).setCellValue(row.get("hq_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("hq_cnt")).doubleValue() : (Long) row.get("hq_cnt"));
		    }
		    if (row.get("hq_value_per") != null) {
			sh.getRow(rowNumber).createCell(16)
				.setCellValue(row.get("hq_value_per") instanceof BigDecimal ? ((BigDecimal) row.get("hq_value_per")).doubleValue() : (Long) row.get("hq_value_per"));
		    }
		    if (row.get("yk_value") != null) {
			sh.getRow(rowNumber).createCell(17).setCellValue(row.get("yk_value") instanceof BigDecimal ? ((BigDecimal) row.get("yk_value")).doubleValue() : (Long) row.get("yk_value"));
		    }
		    if (row.get("yk_cnt") != null) {
			sh.getRow(rowNumber).createCell(18).setCellValue(row.get("yk_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("yk_cnt")).doubleValue() : (Long) row.get("yk_cnt"));
		    }
		    if (row.get("yk_value_per") != null) {
			sh.getRow(rowNumber).createCell(19)
				.setCellValue(row.get("yk_value_per") instanceof BigDecimal ? ((BigDecimal) row.get("yk_value_per")).doubleValue() : (Long) row.get("yk_value_per"));
		    }
		    // 小数点
		    sh.getRow(rowNumber).getCell(4).setCellStyle(mapStyle.get("style5"));
		    sh.getRow(rowNumber).getCell(7).setCellStyle(mapStyle.get("style5"));
		    for (int n = 11; n < 20; n = n + 3) {
			sh.getRow(rowNumber).getCell(n).setCellStyle(mapStyle.get("style5"));
		    }
		    // 分位符
		    sh.getRow(rowNumber).getCell(2).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rowNumber).getCell(3).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rowNumber).getCell(5).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rowNumber).getCell(6).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rowNumber).getCell(8).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rowNumber).getCell(9).setCellStyle(mapStyle.get("style6"));
		    for (int n = 12; n < 20; n = n + 3) {
			sh.getRow(rowNumber).getCell(n).setCellStyle(mapStyle.get("style6"));
		    }
		    // 占比
		    for (int n = 10; n < 20; n = n + 3) {
			sh.getRow(rowNumber).getCell(n).setCellStyle(mapStyle.get("style4"));
		    }
		}
	    }

	}

    }

    public void writeSheet3(List<Map<String, Object>> data, String title, String[] rowTitle, Sheet sh) {
	sh.createRow(0);
	sh.getRow(0).createCell(0).setCellValue(title);
	sh.getRow(0).getCell(0).setCellStyle(mapStyle.get("style0"));
	// 创建行
	for (int i = 2; i <= 52; i++) {
	    sh.createRow(i).createCell(1).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(i).createCell(2).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(i).getCell(1).setCellValue("-");
	}
	sh.createRow(1).createCell(1).setCellValue(rowTitle[0]);
	sh.getRow(1).getCell(1).setCellStyle(mapStyle.get("style1"));
	sh.getRow(2).createCell(2).setCellValue(rowTitle[1]);
	sh.getRow(2).createCell(3).setCellValue(rowTitle[2]);
	sh.getRow(2).createCell(4).setCellValue(rowTitle[3]);
	sh.getRow(2).createCell(5).setCellValue(rowTitle[4]);
	sh.getRow(2).createCell(6).setCellValue(rowTitle[5]);
	sh.getRow(2).createCell(7).setCellValue(rowTitle[6]);
	sh.getRow(2).createCell(8).setCellValue(rowTitle[7]);
	sh.getRow(2).createCell(9).setCellValue(rowTitle[8]);
	sh.getRow(2).createCell(10).setCellValue(rowTitle[9]);
	sh.getRow(2).createCell(11).setCellValue(rowTitle[10]);
	sh.getRow(2).createCell(12).setCellValue(rowTitle[11]);
	sh.getRow(2).createCell(13).setCellValue(rowTitle[12]);
	sh.getRow(2).createCell(14).setCellValue(rowTitle[13]);
	sh.getRow(2).createCell(15).setCellValue(rowTitle[14]);
	sh.getRow(2).createCell(16).setCellValue(rowTitle[15]);
	sh.getRow(2).createCell(17).setCellValue(rowTitle[16]);
	sh.getRow(2).createCell(18).setCellValue(rowTitle[17]);
	sh.getRow(2).createCell(19).setCellValue(rowTitle[18]);
	sh.getRow(2).createCell(20).setCellValue(rowTitle[19]);
	sh.getRow(2).createCell(21).setCellValue(rowTitle[20]);
	for (int i = 2; i < 22; i++) {
	    sh.getRow(1).createCell(i).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2).getCell(i).setCellStyle(mapStyle.get("style3"));
	    for (int n = 3; n <= 52; n++) {
		sh.getRow(n).createCell(i).setCellStyle(mapStyle.get("style5"));
		sh.getRow(n).getCell(i).setCellValue("-");
	    }
	}
	Integer thisMonth = Integer.parseInt("0".equals(month.substring(4, 5)) ? month.substring(5, 6) : month.substring(4, 6));
	sh.getRow(1).getCell(2).setCellValue(month.substring(0, 4) + "年" + thisMonth + "月");
	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
	sh.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
	sh.addMergedRegion(new CellRangeAddress(1, 1, 2, 21));
	for (int i = 0; i < 22; i++) {
	    sh.setColumnWidth(i, 256 * 16);
	}

	sh.getRow(1).setHeightInPoints(35);
	sh.getRow(2).setHeightInPoints(40);

	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
	Map<String, Object> row = new HashMap<String, Object>();
	for (int i = 0, dataSize = data.size(); i < dataSize; i++) {
	    row = data.get(i);
	    // 插入单元格数据
	    if (row.size() != 0) {
		if (row.get("rank_china") != null && (Integer) row.get("rank_china") != 0) {
		    int rankChina=(Integer) row.get("rank_china");
		    sh.getRow(rankChina+2).getCell(1).setCellValue(rankChina);
		    // 营销案的时候使用以下offer_nm,offer_cd
		    if (row.get("offer_nm") != null) {
			sh.getRow(rankChina+2).getCell(2).setCellValue(row.get("offer_nm").toString());
		    }
		    if (row.get("offer_cd") != null) {
			sh.getRow(rankChina+2).getCell(3).setCellValue(row.get("offer_cd").toString());
		    }
		    // 渠道的时候使用以下chnl_nm,chnl_id
		    if (row.get("chnl_nm") != null) {
			sh.getRow(rankChina+2).getCell(2).setCellValue(row.get("chnl_nm").toString());
		    }
		    if (row.get("chnl_id") != null) {
			sh.getRow(rankChina+2).getCell(3).setCellValue(row.get("chnl_id").toString());
		    }

		    if (row.get("prvd_nm") != null) {
			sh.getRow(rankChina+2).getCell(4).setCellValue(row.get("prvd_nm").toString());
		    }
		    if (row.get("cty_nm") != null) {
			sh.getRow(rankChina+2).createCell(5).setCellValue(row.get("cty_nm").toString());
		    }
		    if (row.get("giv_value_tol") != null) {
			sh.getRow(rankChina+2).createCell(6)
				.setCellValue(row.get("giv_value_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_value_tol")).doubleValue() : (Long) row.get("giv_value_tol"));
		    }
		    if (row.get("giv_cnt_tol") != null) {
			sh.getRow(rankChina+2).createCell(7).setCellValue(row.get("giv_cnt_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_cnt_tol")).doubleValue() : (Long) row.get("giv_cnt_tol"));
		    }
		    if (row.get("giv_subs_tol") != null) {
			sh.getRow(rankChina+2).createCell(8)
				.setCellValue(row.get("giv_subs_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_subs_tol")).doubleValue() : (Long) row.get("giv_subs_tol"));
		    }
		    if (row.get("infrac_value") != null) {
			sh.getRow(rankChina+2).createCell(9)
				.setCellValue(row.get("infrac_value") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_value")).doubleValue() : (Long) row.get("infrac_value"));
		    }
		    if (row.get("infrac_cnt") != null) {
			sh.getRow(rankChina+2).createCell(10).setCellValue(row.get("infrac_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_cnt")).doubleValue() : (Long) row.get("infrac_cnt"));
		    }
		    if (row.get("infrac_subs_cnt") != null) {
			sh.getRow(rankChina+2).createCell(11)
				.setCellValue(row.get("infrac_subs_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_subs_cnt")).doubleValue() : (Long) row.get("infrac_subs_cnt"));
		    }
		    if (row.get("infrac_value_pre") != null) {
			sh.getRow(rankChina+2).createCell(12)
				.setCellValue(row.get("infrac_value_pre") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_value_pre")).doubleValue() : (Long) row.get("infrac_value_pre"));
		    }
		    if (row.get("hf_value") != null) {
			sh.getRow(rankChina+2).createCell(13).setCellValue(row.get("hf_value") instanceof BigDecimal ? ((BigDecimal) row.get("hf_value")).doubleValue() : (Long) row.get("hf_value"));
		    }
		    if (row.get("hf_cnt") != null) {
			sh.getRow(rankChina+2).createCell(14).setCellValue(row.get("hf_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("hf_cnt")).doubleValue() : (Long) row.get("hf_cnt"));
		    }
		    if (row.get("hf_value_per") != null) {
			sh.getRow(rankChina+2).createCell(15)
				.setCellValue(row.get("hf_value_per") instanceof BigDecimal ? ((BigDecimal) row.get("hf_value_per")).doubleValue() : (Long) row.get("hf_value_per"));
		    }
		    if (row.get("hq_value") != null) {
			sh.getRow(rankChina+2).createCell(16).setCellValue(row.get("hq_value") instanceof BigDecimal ? ((BigDecimal) row.get("hq_value")).doubleValue() : (Long) row.get("hq_value"));
		    }
		    if (row.get("hq_cnt") != null) {
			sh.getRow(rankChina+2).createCell(17).setCellValue(row.get("hq_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("hq_cnt")).doubleValue() : (Long) row.get("hq_cnt"));
		    }
		    if (row.get("hq_value_per") != null) {
			sh.getRow(rankChina+2).createCell(18)
				.setCellValue(row.get("hq_value_per") instanceof BigDecimal ? ((BigDecimal) row.get("hq_value_per")).doubleValue() : (Long) row.get("hq_value_per"));
		    }
		    if (row.get("yk_value") != null) {
			sh.getRow(rankChina+2).createCell(19).setCellValue(row.get("yk_value") instanceof BigDecimal ? ((BigDecimal) row.get("yk_value")).doubleValue() : (Long) row.get("yk_value"));
		    }
		    if (row.get("yk_cnt") != null) {
			sh.getRow(rankChina+2).createCell(20).setCellValue(row.get("yk_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("yk_cnt")).doubleValue() : (Long) row.get("yk_cnt"));
		    }
		    if (row.get("yk_value_per") != null) {
			sh.getRow(rankChina+2).createCell(21)
				.setCellValue(row.get("yk_value_per") instanceof BigDecimal ? ((BigDecimal) row.get("yk_value_per")).doubleValue() : (Long) row.get("yk_value_per"));
		    }
		    // 小数点
		    sh.getRow(rankChina+2).getCell(6).setCellStyle(mapStyle.get("style5"));
		    sh.getRow(rankChina+2).getCell(9).setCellStyle(mapStyle.get("style5"));
		    for (int n = 13; n < 22; n = n + 3) {
			sh.getRow(rankChina+2).getCell(n).setCellStyle(mapStyle.get("style5"));
		    }
		    // 分位符
		    sh.getRow(rankChina+2).getCell(2).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rankChina+2).getCell(3).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rankChina+2).getCell(4).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rankChina+2).getCell(5).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rankChina+2).getCell(7).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rankChina+2).getCell(8).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rankChina+2).getCell(10).setCellStyle(mapStyle.get("style6"));
		    sh.getRow(rankChina+2).getCell(11).setCellStyle(mapStyle.get("style6"));
		    for (int n = 14; n < 22; n = n + 3) {
			sh.getRow(rankChina+2).getCell(n).setCellStyle(mapStyle.get("style6"));
		    }
		    // 占比
		    for (int n = 12; n < 22; n = n + 3) {
			sh.getRow(rankChina+2).getCell(n).setCellStyle(mapStyle.get("style4"));
		    }
		}
	    }

	}

    }

    public void writeFirstPart(List<Map<String, Object>> data, Sheet sh, String month) {
	sh.createRow(0);
	sh.getRow(0).createCell(0).setCellValue("“流量异常赠送”持续审计结果摘要");
	// 创建行
	sh.createRow(1).createCell(0);
	sh.createRow(2).createCell(0).setCellStyle(mapStyle.get("style0"));
	for (int i = 3; i <= 10; i++) {
	    sh.createRow(i).createCell(1).setCellStyle(mapStyle.get("style3"));
	    if (i == 3 || i == 5 || i == 6 || i == 8 || i == 9) {
		sh.getRow(i).createCell(2).setCellStyle(mapStyle.get("style6"));
	    } else if (i == 4 || i == 7) {
		sh.getRow(i).createCell(2).setCellStyle(mapStyle.get("style5"));

	    } else {
		sh.getRow(i).createCell(2).setCellStyle(mapStyle.get("style4"));
	    }
	}
	sh.getRow(3).getCell(1).setCellStyle(mapStyle.get("style1"));
	sh.getRow(2).getCell(0).setCellValue("总体情况");
	sh.getRow(3).getCell(1).setCellValue("审计月");
	sh.getRow(4).getCell(1).setCellValue("总赠送流量（G）");
	sh.getRow(5).getCell(1).setCellValue("总赠送流量次数");
	sh.getRow(6).getCell(1).setCellValue("总赠送用户数");
	sh.getRow(7).getCell(1).setCellValue("异常赠送流量（G）");
	sh.getRow(8).getCell(1).setCellValue("异常赠送次数");
	sh.getRow(9).getCell(1).setCellValue("异常赠送涉及用户数");
	sh.getRow(10).getCell(1).setCellValue("异常赠送流量占比");

	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.getRow(0).getCell(0).setCellStyle(mapStyle.get("style2"));

	Map<String, Object> row = new HashMap<String, Object>();
	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
	for (int i = 0; i < data.size(); i++) {
	    row = data.get(i);
	    // 插入单元格数据
	    if (row.size() != 0) {
		if (row.get("aud_trm") != null) {
		    sh.getRow(3).getCell(2).setCellValue(row.get("aud_trm").toString());
		} else {
		    sh.getRow(3).getCell(2).setCellValue("-");
		}
		if (row.get("giv_value_tol") != null) {
		    sh.getRow(4).getCell(2).setCellValue(row.get("giv_value_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_value_tol")).doubleValue() : (Long) row.get("giv_value_tol"));
		} else {
		    sh.getRow(4).getCell(2).setCellValue("-");
		}
		if (row.get("giv_cnt_tol") != null) {
		    sh.getRow(5).getCell(2).setCellValue(row.get("giv_cnt_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_cnt_tol")).doubleValue() : (Long) row.get("giv_cnt_tol"));
		} else {
		    sh.getRow(5).getCell(2).setCellValue("-");
		}
		if (row.get("giv_subs_tol") != null) {
		    sh.getRow(6).getCell(2).setCellValue(row.get("giv_subs_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_subs_tol")).doubleValue() : (Long) row.get("giv_subs_tol"));
		} else {
		    sh.getRow(6).getCell(2).setCellValue("-");
		}
		if (row.get("infrac_value") != null) {
		    sh.getRow(7).getCell(2).setCellValue(row.get("infrac_value") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_value")).doubleValue() : (Long) row.get("infrac_value"));
		} else {
		    sh.getRow(7).getCell(2).setCellValue("-");
		}
		if (row.get("infrac_cnt") != null) {
		    sh.getRow(8).getCell(2).setCellValue(row.get("infrac_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_cnt")).doubleValue() : (Long) row.get("infrac_cnt"));
		} else {
		    sh.getRow(8).getCell(2).setCellValue("-");
		}
		if (row.get("infrac_subs_cnt") != null) {
		    sh.getRow(9).getCell(2)
			    .setCellValue(row.get("infrac_subs_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_subs_cnt")).doubleValue() : (Long) row.get("infrac_subs_cnt"));
		} else {
		    sh.getRow(9).getCell(2).setCellValue("-");
		}
		if (row.get("infrac_value_pre") != null) {
		    sh.getRow(10).getCell(2)
			    .setCellValue(row.get("infrac_value_pre") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_value_pre")).doubleValue() : (Long) row.get("infrac_value_pre"));
		} else {
		    sh.getRow(10).getCell(2).setCellValue("-");
		}
	    }
	}

    }

    public void writeSecondPart(List<Map<String, Object>> data, Sheet sh) {
	sh.createRow(13);
	sh.getRow(13).createCell(0).setCellValue(prvdName + "省公司“流量异常赠送”排名情况");
	sh.getRow(13).getCell(0).setCellStyle(mapStyle.get("style0"));
	// 创建行
	sh.createRow(14).createCell(1).setCellValue("排名");
	sh.getRow(14).getCell(1).setCellStyle(mapStyle.get("style1"));
	sh.createRow(15).createCell(2).setCellValue("地市");
	sh.getRow(15).createCell(3).setCellValue("总赠送流量（G）");
	sh.getRow(15).createCell(4).setCellValue("总赠送流量次数");
	sh.getRow(15).createCell(5).setCellValue("总赠送用户数");
	sh.getRow(15).createCell(6).setCellValue("异常赠送流量（G）");
	sh.getRow(15).createCell(7).setCellValue("异常赠送次数");
	sh.getRow(15).createCell(8).setCellValue("异常赠送涉及用户数");
	sh.getRow(15).createCell(9).setCellValue("异常赠送流量占比");
	sh.getRow(15).createCell(10).setCellValue("高频赠送流量（G）");
	sh.getRow(15).createCell(11).setCellValue("高频赠送次数");
	sh.getRow(15).createCell(12).setCellValue("高频赠送流量占比");
	sh.getRow(15).createCell(13).setCellValue("高额赠送流量（G）");
	sh.getRow(15).createCell(14).setCellValue("高额赠送次数");
	sh.getRow(15).createCell(15).setCellValue("高额赠送流量占比");
	sh.getRow(15).createCell(16).setCellValue("向非正常用户赠送流量（G）");
	sh.getRow(15).createCell(17).setCellValue("向非正常用户赠送次数");
	sh.getRow(15).createCell(18).setCellValue("向非正常用户赠送流量占比");
	for (int i = 2; i < 19; i++) {
	    sh.getRow(14).createCell(i).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(15).getCell(i).setCellStyle(mapStyle.get("style3"));
	}
	Integer thisMonth = Integer.parseInt("0".equals(month.substring(4, 5)) ? month.substring(5, 6) : month.substring(4, 6));
	sh.getRow(14).getCell(2).setCellValue(month.substring(0, 4) + "年" + thisMonth + "月");
	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.addMergedRegion(new CellRangeAddress(13, 13, 0, 4));
	sh.addMergedRegion(new CellRangeAddress(14, 15, 1, 1));
	sh.addMergedRegion(new CellRangeAddress(14, 14, 2, 18));
	for (int i = 0; i < 18; i++) {
	    sh.setColumnWidth(i, 256 * 16);
	}

	sh.getRow(14).setHeightInPoints(35);
	sh.getRow(15).setHeightInPoints(40);

	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
	Map<String, Object> row = new HashMap<String, Object>();
	for (int i = 0, dataSize = data.size(); i < dataSize; i++) {
	    row = data.get(i);
	    // 插入单元格数据
	    if (row.size() != 0) {
		if (row.get("rank_prvd") != null) {
		    sh.createRow(i + 16).createCell(1).setCellValue((Integer) row.get("rank_prvd"));
		} else {
		    sh.createRow(i + 16).createCell(1).setCellValue("-");
		}
		if (row.get("cty_nm") != null) {
		    sh.getRow(i + 16).createCell(2).setCellValue(row.get("cty_nm").toString());
		} else {
		    sh.getRow(i + 16).createCell(2).setCellValue("-");
		}
		if (row.get("giv_value_tol") != null) {
		    sh.getRow(i + 16).createCell(3)
			    .setCellValue(row.get("giv_value_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_value_tol")).doubleValue() : (Long) row.get("giv_value_tol"));
		} else {
		    sh.getRow(i + 16).createCell(4).setCellValue("-");
		}
		if (row.get("giv_cnt_tol") != null) {
		    sh.getRow(i + 16).createCell(4).setCellValue(row.get("giv_cnt_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_cnt_tol")).doubleValue() : (Long) row.get("giv_cnt_tol"));
		} else {
		    sh.getRow(i + 16).createCell(4).setCellValue("-");
		}
		if (row.get("giv_subs_tol") != null) {
		    sh.getRow(i + 16).createCell(5).setCellValue(row.get("giv_subs_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_subs_tol")).doubleValue() : (Long) row.get("giv_subs_tol"));
		} else {
		    sh.getRow(i + 16).createCell(5).setCellValue("-");
		}
		if (row.get("infrac_value") != null) {
		    sh.getRow(i + 16).createCell(6).setCellValue(row.get("infrac_value") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_value")).doubleValue() : (Long) row.get("infrac_value"));
		} else {
		    sh.getRow(i + 16).createCell(6).setCellValue("-");
		}
		if (row.get("infrac_cnt") != null) {
		    sh.getRow(i + 16).createCell(7).setCellValue(row.get("infrac_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_cnt")).doubleValue() : (Long) row.get("infrac_cnt"));
		} else {
		    sh.getRow(i + 16).createCell(7).setCellValue("-");
		}
		if (row.get("infrac_subs_cnt") != null) {
		    sh.getRow(i + 16).createCell(8)
			    .setCellValue(row.get("infrac_subs_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_subs_cnt")).doubleValue() : (Long) row.get("infrac_subs_cnt"));
		} else {
		    sh.getRow(i + 16).createCell(8).setCellValue("-");
		}
		if (row.get("infrac_value_pre") != null) {
		    sh.getRow(i + 16).createCell(9)
			    .setCellValue(row.get("infrac_value_pre") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_value_pre")).doubleValue() : (Long) row.get("infrac_value_pre"));
		} else {
		    sh.getRow(i + 16).createCell(9).setCellValue("-");
		}
		if (row.get("hf_value") != null) {
		    sh.getRow(i + 16).createCell(10).setCellValue(row.get("hf_value") instanceof BigDecimal ? ((BigDecimal) row.get("hf_value")).doubleValue() : (Long) row.get("hf_value"));
		} else {
		    sh.getRow(i + 16).createCell(10).setCellValue("-");
		}
		if (row.get("hf_cnt") != null) {
		    sh.getRow(i + 16).createCell(11).setCellValue(row.get("hf_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("hf_cnt")).doubleValue() : (Long) row.get("hf_cnt"));
		} else {
		    sh.getRow(i + 16).createCell(11).setCellValue("-");
		}
		if (row.get("hf_value_per") != null) {
		    sh.getRow(i + 16).createCell(12)
			    .setCellValue(row.get("hf_value_per") instanceof BigDecimal ? ((BigDecimal) row.get("hf_value_per")).doubleValue() : (Long) row.get("hf_value_per"));
		} else {
		    sh.getRow(i + 16).createCell(12).setCellValue("-");
		}
		if (row.get("hq_value") != null) {
		    sh.getRow(i + 16).createCell(13).setCellValue(row.get("hq_value") instanceof BigDecimal ? ((BigDecimal) row.get("hq_value")).doubleValue() : (Long) row.get("hq_value"));
		} else {
		    sh.getRow(i + 16).createCell(13).setCellValue("-");
		}
		if (row.get("hq_cnt") != null) {
		    sh.getRow(i + 16).createCell(14).setCellValue(row.get("hq_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("hq_cnt")).doubleValue() : (Long) row.get("hq_cnt"));
		} else {
		    sh.getRow(i + 16).createCell(14).setCellValue("-");
		}
		if (row.get("hq_value_per") != null) {
		    sh.getRow(i + 16).createCell(15)
			    .setCellValue(row.get("hq_value_per") instanceof BigDecimal ? ((BigDecimal) row.get("hq_value_per")).doubleValue() : (Long) row.get("hq_value_per"));
		} else {
		    sh.getRow(i + 16).createCell(15).setCellValue("-");
		}
		if (row.get("yk_value") != null) {
		    sh.getRow(i + 16).createCell(16).setCellValue(row.get("yk_value") instanceof BigDecimal ? ((BigDecimal) row.get("yk_value")).doubleValue() : (Long) row.get("yk_value"));
		} else {
		    sh.getRow(i + 16).createCell(16).setCellValue("-");
		}
		if (row.get("yk_cnt") != null) {
		    sh.getRow(i + 16).createCell(17).setCellValue(row.get("yk_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("yk_cnt")).doubleValue() : (Long) row.get("yk_cnt"));
		} else {
		    sh.getRow(i + 16).createCell(17).setCellValue("-");
		}
		if (row.get("yk_value_per") != null) {
		    sh.getRow(i + 16).createCell(18)
			    .setCellValue(row.get("yk_value_per") instanceof BigDecimal ? ((BigDecimal) row.get("yk_value_per")).doubleValue() : (Long) row.get("yk_value_per"));
		} else {
		    sh.getRow(i + 16).createCell(18).setCellValue("-");
		}
		// 小数点
		sh.getRow(i + 16).getCell(3).setCellStyle(mapStyle.get("style5"));
		sh.getRow(i + 16).getCell(6).setCellStyle(mapStyle.get("style5"));
		for (int n = 10; n < 19; n = n + 3) {
		    sh.getRow(i + 16).getCell(n).setCellStyle(mapStyle.get("style5"));
		}
		// 分位符
		sh.getRow(i + 16).getCell(1).setCellStyle(mapStyle.get("style6"));
		sh.getRow(i + 16).getCell(2).setCellStyle(mapStyle.get("style6"));
		sh.getRow(i + 16).getCell(4).setCellStyle(mapStyle.get("style6"));
		sh.getRow(i + 16).getCell(5).setCellStyle(mapStyle.get("style6"));
		sh.getRow(i + 16).getCell(7).setCellStyle(mapStyle.get("style6"));
		sh.getRow(i + 16).getCell(8).setCellStyle(mapStyle.get("style6"));
		for (int n = 11; n < 19; n = n + 3) {
		    sh.getRow(i + 16).getCell(n).setCellStyle(mapStyle.get("style6"));
		}
		// 占比
		for (int n = 9; n < 19; n = n + 3) {
		    sh.getRow(i + 16).getCell(n).setCellStyle(mapStyle.get("style4"));
		}
	    }

	}

    }

    public void writeSheet4(List<Map<String, Object>> data, String title, String[] rowTitle, Sheet sh) {
	sh.createRow(0);
	sh.getRow(0).createCell(0).setCellValue(title);
	sh.getRow(0).getCell(0).setCellStyle(mapStyle.get("style0"));
	sh.createRow(1).createCell(1).setCellValue(rowTitle[0]);
	sh.getRow(1).getCell(1).setCellStyle(mapStyle.get("style1"));
	sh.createRow(2).createCell(2).setCellValue(rowTitle[1]);
	sh.getRow(2).createCell(3).setCellValue(rowTitle[2]);
	sh.getRow(2).createCell(4).setCellValue(rowTitle[3]);
	sh.getRow(2).createCell(5).setCellValue(rowTitle[4]);
	sh.getRow(2).createCell(6).setCellValue(rowTitle[5]);
	sh.getRow(2).createCell(7).setCellValue(rowTitle[6]);
	sh.getRow(2).createCell(8).setCellValue(rowTitle[7]);
	sh.getRow(2).createCell(9).setCellValue(rowTitle[8]);
	sh.getRow(2).createCell(10).setCellValue(rowTitle[9]);
	sh.getRow(2).createCell(11).setCellValue(rowTitle[10]);
	sh.getRow(2).createCell(12).setCellValue(rowTitle[11]);
	sh.getRow(2).createCell(13).setCellValue(rowTitle[12]);
	sh.getRow(2).createCell(14).setCellValue(rowTitle[13]);
	sh.getRow(2).createCell(15).setCellValue(rowTitle[14]);
	sh.getRow(2).createCell(16).setCellValue(rowTitle[15]);
	sh.getRow(2).createCell(17).setCellValue(rowTitle[16]);
	sh.getRow(2).createCell(18).setCellValue(rowTitle[17]);
	sh.getRow(2).createCell(19).setCellValue(rowTitle[18]);
	sh.getRow(2).createCell(20).setCellValue(rowTitle[19]);
	sh.getRow(2).createCell(21).setCellValue(rowTitle[20]);
	for (int i = 2; i < 22; i++) {
	    sh.getRow(1).createCell(i).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2).getCell(i).setCellStyle(mapStyle.get("style3"));
	}
	Integer thisMonth = Integer.parseInt("0".equals(month.substring(4, 5)) ?month.substring(5, 6) : month.substring(4, 6));
	sh.getRow(1).getCell(2).setCellValue(month.substring(0, 4) + "年" + thisMonth + "月");
	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
	sh.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
	sh.addMergedRegion(new CellRangeAddress(1, 1, 2, 21));
	for (int i = 0; i < 22; i++) {
	    sh.setColumnWidth(i, 256 * 16);
	}

	sh.getRow(1).setHeightInPoints(35);
	sh.getRow(2).setHeightInPoints(40);

	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
	Map<String, Object> row = new HashMap<String, Object>();
	for (int i = 0, dataSize = data.size(); i < dataSize; i++) {
	    row = data.get(i);
	    // 插入单元格数据
	    if (row.size() != 0) {
		if (row.get("rank_prvd") != null) {
		    sh.createRow(i + 3).createCell(1).setCellValue((Integer) row.get("rank_prvd"));
		} else {
		    sh.createRow(i + 3).createCell(1).setCellValue("-");
		}
		if (row.get("chnl_nm") != null) {
		    sh.getRow(i + 3).createCell(2).setCellValue(row.get("chnl_nm").toString());
		} else if (row.get("offer_nm") != null) {
		    sh.getRow(i + 3).createCell(2).setCellValue(row.get("offer_nm").toString());
		} else {
		    sh.getRow(i + 3).createCell(2).setCellValue("-");
		}

		if (row.get("chnl_id") != null) {
		    sh.getRow(i + 3).createCell(3).setCellValue(row.get("chnl_id").toString());
		} else if (row.get("offer_cd") != null) {
		    sh.getRow(i + 3).createCell(3).setCellValue(row.get("offer_cd").toString());
		} else {
		    sh.getRow(i + 3).createCell(3).setCellValue("-");
		}
		if (row.get("prvd_nm") != null) {
		    sh.getRow(i + 3).createCell(4).setCellValue(row.get("prvd_nm").toString());
		} else {
		    sh.getRow(i + 3).createCell(4).setCellValue("-");
		}
		if (row.get("cty_nm") != null) {
		    sh.getRow(i + 3).createCell(5).setCellValue(row.get("cty_nm").toString());
		} else {
		    sh.getRow(i + 3).createCell(5).setCellValue("-");
		}
		if (row.get("giv_value_tol") != null) {
		    sh.getRow(i + 3).createCell(6)
			    .setCellValue(row.get("giv_value_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_value_tol")).doubleValue() : (Long) row.get("giv_value_tol"));
		} else {
		    sh.getRow(i + 3).createCell(6).setCellValue("-");
		}
		if (row.get("giv_cnt_tol") != null) {
		    sh.getRow(i + 3).createCell(7).setCellValue(row.get("giv_cnt_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_cnt_tol")).doubleValue() : (Long) row.get("giv_cnt_tol"));
		} else {
		    sh.getRow(i + 3).createCell(7).setCellValue("-");
		}
		if (row.get("giv_subs_tol") != null) {
		    sh.getRow(i + 3).createCell(8).setCellValue(row.get("giv_subs_tol") instanceof BigDecimal ? ((BigDecimal) row.get("giv_subs_tol")).doubleValue() : (Long) row.get("giv_subs_tol"));
		} else {
		    sh.getRow(i + 3).createCell(8).setCellValue("-");
		}
		if (row.get("infrac_value") != null) {
		    sh.getRow(i + 3).createCell(9).setCellValue(row.get("infrac_value") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_value")).doubleValue() : (Long) row.get("infrac_value"));
		} else {
		    sh.getRow(i + 3).createCell(9).setCellValue("-");
		}
		if (row.get("infrac_cnt") != null) {
		    sh.getRow(i + 3).createCell(10).setCellValue(row.get("infrac_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_cnt")).doubleValue() : (Long) row.get("infrac_cnt"));
		} else {
		    sh.getRow(i + 3).createCell(10).setCellValue("-");
		}
		if (row.get("infrac_subs_cnt") != null) {
		    sh.getRow(i + 3).createCell(11)
			    .setCellValue(row.get("infrac_subs_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_subs_cnt")).doubleValue() : (Long) row.get("infrac_subs_cnt"));
		} else {
		    sh.getRow(i + 3).createCell(11).setCellValue("-");
		}
		if (row.get("infrac_value_pre") != null) {
		    sh.getRow(i + 3).createCell(12)
			    .setCellValue(row.get("infrac_value_pre") instanceof BigDecimal ? ((BigDecimal) row.get("infrac_value_pre")).doubleValue() : (Long) row.get("infrac_value_pre"));
		} else {
		    sh.getRow(i + 3).createCell(12).setCellValue("-");
		}
		if (row.get("hf_value") != null) {
		    sh.getRow(i + 3).createCell(13).setCellValue(row.get("hf_value") instanceof BigDecimal ? ((BigDecimal) row.get("hf_value")).doubleValue() : (Long) row.get("hf_value"));
		} else {
		    sh.getRow(i + 3).createCell(13).setCellValue("-");
		}
		if (row.get("hf_cnt") != null) {
		    sh.getRow(i + 3).createCell(14).setCellValue(row.get("hf_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("hf_cnt")).doubleValue() : (Long) row.get("hf_cnt"));
		} else {
		    sh.getRow(i + 3).createCell(14).setCellValue("-");
		}
		if (row.get("hf_value_per") != null) {
		    sh.getRow(i + 3).createCell(15).setCellValue(row.get("hf_value_per") instanceof BigDecimal ? ((BigDecimal) row.get("hf_value_per")).doubleValue() : (Long) row.get("hf_value_per"));
		} else {
		    sh.getRow(i + 3).createCell(15).setCellValue("-");
		}
		if (row.get("hq_value") != null) {
		    sh.getRow(i + 3).createCell(16).setCellValue(row.get("hq_value") instanceof BigDecimal ? ((BigDecimal) row.get("hq_value")).doubleValue() : (Long) row.get("hq_value"));
		} else {
		    sh.getRow(i + 3).createCell(16).setCellValue("-");
		}
		if (row.get("hq_cnt") != null) {
		    sh.getRow(i + 3).createCell(17).setCellValue(row.get("hq_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("hq_cnt")).doubleValue() : (Long) row.get("hq_cnt"));
		} else {
		    sh.getRow(i + 3).createCell(17).setCellValue("-");
		}
		if (row.get("hq_value_per") != null) {
		    sh.getRow(i + 3).createCell(18).setCellValue(row.get("hq_value_per") instanceof BigDecimal ? ((BigDecimal) row.get("hq_value_per")).doubleValue() : (Long) row.get("hq_value_per"));
		} else {
		    sh.getRow(i + 3).createCell(18).setCellValue("-");
		}
		if (row.get("yk_value") != null) {
		    sh.getRow(i + 3).createCell(19).setCellValue(row.get("yk_value") instanceof BigDecimal ? ((BigDecimal) row.get("yk_value")).doubleValue() : (Long) row.get("yk_value"));
		} else {
		    sh.getRow(i + 3).createCell(19).setCellValue("-");
		}
		if (row.get("yk_cnt") != null) {
		    sh.getRow(i + 3).createCell(20).setCellValue(row.get("yk_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("yk_cnt")).doubleValue() : (Long) row.get("yk_cnt"));
		} else {
		    sh.getRow(i + 3).createCell(20).setCellValue("-");
		}
		if (row.get("yk_value_per") != null) {
		    sh.getRow(i + 3).createCell(21).setCellValue(row.get("yk_value_per") instanceof BigDecimal ? ((BigDecimal) row.get("yk_value_per")).doubleValue() : (Long) row.get("yk_value_per"));
		} else {
		    sh.getRow(i + 3).createCell(21).setCellValue("-");
		}
		// 小数点
		sh.getRow(i + 3).getCell(6).setCellStyle(mapStyle.get("style5"));
		sh.getRow(i + 3).getCell(9).setCellStyle(mapStyle.get("style5"));
		for (int n = 13; n < 22; n = n + 3) {
		    sh.getRow(i + 3).getCell(n).setCellStyle(mapStyle.get("style5"));
		}
		// 分位符
		sh.getRow(i + 3).getCell(1).setCellStyle(mapStyle.get("style6"));
		sh.getRow(i + 3).getCell(2).setCellStyle(mapStyle.get("style6"));
		sh.getRow(i + 3).getCell(3).setCellStyle(mapStyle.get("style6"));
		sh.getRow(i + 3).getCell(4).setCellStyle(mapStyle.get("style6"));
		sh.getRow(i + 3).getCell(5).setCellStyle(mapStyle.get("style6"));
		sh.getRow(i + 3).getCell(7).setCellStyle(mapStyle.get("style6"));
		sh.getRow(i + 3).getCell(8).setCellStyle(mapStyle.get("style6"));
		sh.getRow(i + 3).getCell(10).setCellStyle(mapStyle.get("style6"));
		sh.getRow(i + 3).getCell(11).setCellStyle(mapStyle.get("style6"));
		for (int n = 14; n < 22; n = n + 3) {
		    sh.getRow(i + 3).getCell(n).setCellStyle(mapStyle.get("style6"));
		}
		// 占比
		for (int n = 12; n < 22; n = n + 3) {
		    sh.getRow(i + 3).getCell(n).setCellStyle(mapStyle.get("style4"));
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
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 14);
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

    private CellStyle getStyle2() {
	CellStyle style = wb.createCellStyle();
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 16);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style.setFont(font);
	return style;
    }

    private CellStyle getStyle3() {
	CellStyle style0 = wb.createCellStyle();
	style0.setAlignment(CellStyle.ALIGN_LEFT);
	style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	style0.setFont(font);
	style0.setBorderBottom(CellStyle.BORDER_THIN);
	style0.setBorderLeft(CellStyle.BORDER_THIN);
	style0.setBorderRight(CellStyle.BORDER_THIN);
	style0.setBorderTop(CellStyle.BORDER_THIN);
	style0.setWrapText(true);
	style0.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	style0.setFillPattern(CellStyle.SOLID_FOREGROUND);
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
	font.setFontHeightInPoints((short) 12);
	style0.setFont(font);
	style0.setBorderBottom(CellStyle.BORDER_THIN);
	style0.setBorderLeft(CellStyle.BORDER_THIN);
	style0.setBorderRight(CellStyle.BORDER_THIN);
	style0.setBorderTop(CellStyle.BORDER_THIN);
	style0.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
	return style0;
    }

}