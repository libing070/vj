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


@Service("NotiFileQdyk2000pmhzProcessor")
public class NotiFileQdyk2000pmhzProcessor extends AbstractNotiFileProcessor {

    @Override
    public boolean generate() throws Exception {
	String startMonth = "201507";
	String audTrm = month;
	if (Integer.parseInt(month) < Integer.parseInt("201507"))
	    return false;
	List<String> audList = getAudTrmListToSomeDate(audTrm, startMonth);
	this.setFileName(CalendarUtils.buildAuditCycle(startMonth) + "-" + CalendarUtils.buildAuditCycle(audTrm) + "渠道养卡排名汇总");

	writeSheetCommonSummary(audList, notiFileGenService.getNotiFile2000pmhzSum(audList));

	writeSheetCommonDetail(audList, notiFileGenService.getNotiFile2000pmhzNum(audList), notiFileGenService.getNotiFile2000pmhzChnl(audList));
	return true;
    }

    @Override
    protected String buildFileName() {
	fileName = fileName + ".xlsx";
	return fileName;
    }

    public void writeSheetCommonSummary(List<String> audList, List<Map<String, Object>> data) {
	int audNum = audList.size();
	sh = wb.createSheet("全公司审计结果摘要");
	for (int i = 0; i < 11; i++)
	    sh.createRow(i);
	sh.getRow(1).createCell(0).setCellValue("“全渠道养卡、套利”持续审计结果摘要");
	sh.getRow(1).getCell(0).setCellStyle(getStyle0());
	sh.getRow(3).createCell(0).setCellValue("总体情况");
	sh.getRow(3).getCell(0).setCellStyle(getStyle00());

	sh.getRow(4).createCell(1).setCellValue("用户入网月份");

	sh.getRow(6).createCell(1).setCellValue("全渠道入网号码数量（万）");
	sh.getRow(7).createCell(1).setCellValue("疑似养卡号码数量");
	sh.getRow(8).createCell(1).setCellValue("疑似养卡号码占比%");
	sh.getRow(9).createCell(1).setCellValue("涉及全渠道数量");
	sh.getRow(10).createCell(1).setCellValue("涉及全渠道占比%");
	for (int i = 6; i < 11; i++)
	    sh.getRow(i).getCell(1).setCellStyle(getStyle_boldNone_aLeft());

	creatCell4SumAutrms(audList);

	sh.addMergedRegion(new CellRangeAddress(1, 1, 0, 2 * audNum + 1));
	sh.addMergedRegion(new CellRangeAddress(3, 3, 0, 2 * audNum + 1));
	sh.addMergedRegion(new CellRangeAddress(4, 5, 1, 1));
	sh.getRow(4).getCell(1).setCellStyle(getStyle_boldNone());
	sh.getRow(5).createCell(1).setCellStyle(getStyle_boldNone());

	sh.setColumnWidth(1, 256 * 30);// ?-196px
	for (int i = 2; i <= 2 * audNum + 1; i++)
	    sh.setColumnWidth(i, 256 * 14);// 256 * 14-98px
	for (int i = 0; i < 11; i++)
	    sh.getRow(i).setHeightInPoints(20);// 24-32px;?-26px
	sh.getRow(1).setHeightInPoints(30);
	sh.getRow(3).setHeightInPoints(30);// 30-40px

	sh.createRow(13).createCell(0).setCellValue("备注：以上指标中涉及“数量”和“金额”的“增幅”指的是环比增幅，等于（本月数值-上月数值）/上月数值；涉及“占比”的“增幅”，等于本月占比-上月占比。");
	sh.getRow(13).getCell(0).setCellStyle(getStyle1());
	for (int i = 0; i < audNum; i++) {

	    sh.getRow(6).createCell((i + 1) * 2).setCellStyle(getStyle80());
	    sh.getRow(7).createCell((i + 1) * 2).setCellStyle(getStyle80());

	    sh.getRow(9).createCell((i + 1) * 2).setCellStyle(getStyle5());

	    sh.getRow(8).createCell((i + 1) * 2).setCellStyle(getStyle2());
	    sh.getRow(10).createCell((i + 1) * 2).setCellStyle(getStyle2());
	    for (int j = 6; j < 11; j++)
		sh.getRow(j).createCell((i + 1) * 2 + 1).setCellStyle(getStyle2());
	}
	// 先set“-”
	for (int n = 6; n < 11; n++) {
	    for (int m = 2; m <= audNum * 2 + 1; m++)
		sh.getRow(n).getCell(m).setCellValue("-");
	}
	Map<String,Object> row=new HashMap<String,Object>();
	for (int i = 0; i < audNum; i++) {
	    for (int j = 0,dataSize=data.size(); j < dataSize; j++) {
		row = data.get(j);
		// 0-2
		// 1-4
		// 2-6
		String audTrm = row.get("Aud_trm") == null ? "" : (row.get("Aud_trm") + "").trim();
		if (audList.get(i).equals(audTrm)) {
		    if (row.get("qqd_num") != null) {
			sh.getRow(6).getCell((i + 1) * 2).setCellValue(row.get("qqd_num") instanceof BigDecimal ? ((BigDecimal) row.get("qqd_num")).doubleValue() : (Long) row.get("qqd_num"));
		    }
		    if (row.get("ys_num") != null) {
			sh.getRow(7).getCell((i + 1) * 2).setCellValue(row.get("ys_num") instanceof BigDecimal ? ((BigDecimal) row.get("ys_num")).doubleValue() : (Long) row.get("ys_num"));
		    }
		    if (row.get("zb") != null) {
			if (row.get("zb").toString().substring(0, 5).equals("-9999")) {
			    sh.getRow(8).getCell((i + 1) * 2).setCellValue("NA");
			} else {
			    sh.getRow(8).getCell((i + 1) * 2).setCellValue(row.get("zb") instanceof BigDecimal ? ((BigDecimal) row.get("zb")).doubleValue() : (Long) row.get("zb"));
			}
		    }
		    if (row.get("sjqqd_num") != null) {
			sh.getRow(9).getCell((i + 1) * 2).setCellValue(row.get("sjqqd_num") instanceof BigDecimal ? ((BigDecimal) row.get("sjqqd_num")).doubleValue() : (Long) row.get("sjqqd_num"));
		    }
		    if (row.get("qqd_zb") != null) {
			if (row.get("qqd_zb").toString().substring(0, 5).equals("-9999")) {
			    sh.getRow(10).getCell((i + 1) * 2).setCellValue("NA");
			} else {
			    sh.getRow(10).getCell((i + 1) * 2).setCellValue(row.get("qqd_zb") instanceof BigDecimal ? ((BigDecimal) row.get("qqd_zb")).doubleValue() : (Long) row.get("qqd_zb"));
			}
		    }
		    if (row.get("zf_qqd_num") != null) {
			if (row.get("zf_qqd_num").toString().substring(0, 5).equals("-9999")) {
			    sh.getRow(6).getCell((i + 1) * 2 + 1).setCellValue("NA");
			} else {
			    sh.getRow(6).getCell((i + 1) * 2 + 1)
				    .setCellValue(row.get("zf_qqd_num") instanceof BigDecimal ? ((BigDecimal) row.get("zf_qqd_num")).doubleValue() : (Long) row.get("zf_qqd_num"));
			}
		    }
		    if (row.get("zf_ys_num") != null) {
			if (row.get("zf_ys_num").toString().substring(0, 5).equals("-9999")) {
			    sh.getRow(7).getCell((i + 1) * 2 + 1).setCellValue("NA");
			} else {
			    sh.getRow(7).getCell((i + 1) * 2 + 1)
				    .setCellValue(row.get("zf_ys_num") instanceof BigDecimal ? ((BigDecimal) row.get("zf_ys_num")).doubleValue() : (Long) row.get("zf_ys_num"));
			}
		    }
		    if (row.get("zf_zb") != null) {
			if (row.get("zf_zb").toString().substring(0, 5).equals("-9999")) {
			    sh.getRow(8).getCell((i + 1) * 2 + 1).setCellValue("NA");
			} else {
			    sh.getRow(8).getCell((i + 1) * 2 + 1).setCellValue(row.get("zf_zb") instanceof BigDecimal ? ((BigDecimal) row.get("zf_zb")).doubleValue() : (Long) row.get("zf_zb"));
			}
		    }
		    if (row.get("zf_sjqqd_num") != null) {
			if (row.get("zf_sjqqd_num").toString().substring(0, 5).equals("-9999")) {
			    sh.getRow(9).getCell((i + 1) * 2 + 1).setCellValue("NA");
			} else {
			    sh.getRow(9).getCell((i + 1) * 2 + 1)
				    .setCellValue(row.get("zf_sjqqd_num") instanceof BigDecimal ? ((BigDecimal) row.get("zf_sjqqd_num")).doubleValue() : (Long) row.get("zf_sjqqd_num"));
			}
		    }
		    if (row.get("zf_qqd_zb") != null) {
			if (row.get("zf_qqd_zb").toString().substring(0, 5).equals("-9999")) {
			    sh.getRow(10).getCell((i + 1) * 2 + 1).setCellValue("NA");
			} else {
			    sh.getRow(10).getCell((i + 1) * 2 + 1)
				    .setCellValue(row.get("zf_qqd_zb") instanceof BigDecimal ? ((BigDecimal) row.get("zf_qqd_zb")).doubleValue() : (Long) row.get("zf_qqd_zb"));
			}
		    }
		    break;
		}
	    }
	}
    }

    private String formatOutput(Object str) {
	if (str == null)
	    return "-";
	String s = str.toString().trim();
	if ("".equals(s))
	    s = "-";
	return (s);
    }

    private String formatOutputZb(Object str) {
	if (str == null)
	    return "-";
	String s = str.toString().trim();
	if ("".equals(s))
	    s = "-";
	if (s.length() > 5 && ("-9999".equals(s.substring(0, 5)) || "-9,999".equals(s.substring(0, 6))))// sql里的数值是用千分位符格式化的
	    s = "NA";
	return (s);
    }

    public void writeSheetCommonDetail(List<String> audList, List<Map<String, Object>> data1, List<Map<String, Object>> data2) {
	sh = wb.createSheet("各公司排名汇总");
	int audNum = audList.size();
	for (int i = 0; i <= 5 * audNum; i++) {
	    sh.setColumnWidth(i, 256 * 16);
	}
	sh.createRow(0);
	sh.getRow(0).createCell(0).setCellValue("各公司全渠道疑似养卡号码数量及占比");
	// sh.createRow(2);
	for (int i = 2; i <= 35; i++)
	    sh.createRow(i);
	sh.getRow(2).createCell(0).setCellValue("用户入网月份");
	sh.getRow(2).createCell(1).setCellValue("全渠道");

	sh.getRow(4).createCell(0).setCellValue("省份");

	sh.getRow(0).getCell(0).setCellStyle(getStyle00());
	sh.getRow(2).getCell(0).setCellStyle(getStyle_boldRigTop());
	sh.getRow(2).getCell(1).setCellStyle(getStyle_boldAll());
	for (int i = 2; i <= audNum * 5; i++)
	    sh.getRow(2).createCell(i).setCellStyle(getStyle_boldAll());

	sh.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
	sh.addMergedRegion(new CellRangeAddress(2, 2, 1, audNum * 5));
	addMergedRegions(3, audNum);
	creatCell4Autrms(3, audList);

	sh.getRow(4).setHeightInPoints(26);// 26.25-35px
	for (int i = 5; i < 36; i++) {
	    sh.getRow(i).createCell(0).setCellStyle(getStyle3());
	    sh.getRow(i).setHeightInPoints(20);// ?-26px
	}
	sh.getRow(0).setHeightInPoints(30);// 30-40px
	sh.getRow(2).setHeightInPoints(20);// ?-26px
	sh.getRow(3).setHeightInPoints(20);// ?-26px
	sh.getRow(4).setHeightInPoints(45);// 45-60px

	sh.getRow(35).getCell(0).setCellStyle(getStyle31());
	sh.createRow(38);
	sh.getRow(38).createCell(0).setCellValue("各公司全渠道疑似养卡渠道数量及占比");
	for (int i = 39; i <= 72; i++)
	    sh.createRow(i);
	sh.getRow(39).createCell(0).setCellValue("用户入网月份");
	sh.getRow(39).createCell(1).setCellValue("全渠道");

	for (int i = 2; i <= audNum * 5; i++)
	    sh.getRow(39).createCell(i).setCellStyle(getStyle_boldNone());

	// sh.createRow(41);
	sh.getRow(41).createCell(0).setCellValue("省份");
	creatCell4Autrms(40, audList);
	sh.addMergedRegion(new CellRangeAddress(39, 40, 0, 0));
	sh.addMergedRegion(new CellRangeAddress(39, 39, 1, audNum * 5));
	addMergedRegions(40, audNum);

	sh.getRow(38).getCell(0).setCellStyle(getStyle00());
	sh.getRow(39).getCell(0).setCellStyle(getStyle_boldNone());
	sh.getRow(39).getCell(1).setCellStyle(getStyle_boldNone());

	for (int i = 42; i < 73; i++) {
	    sh.getRow(i).createCell(0).setCellStyle(getStyle3());
	    sh.getRow(i).setHeightInPoints(20);// ?-26px
	}
	sh.getRow(72).getCell(0).setCellStyle(getStyle31());
	sh.getRow(38).setHeightInPoints(30);// 30-40px
	sh.getRow(39).setHeightInPoints(20);// ?-26px
	sh.getRow(40).setHeightInPoints(20);// ?-26px
	sh.getRow(41).setHeightInPoints(45);// 45-60px
	// 先set“-”
	for (int n = 5; n < 36; n++) {
	    for (int m = 0; m <= audNum * 5; m++)
		sh.getRow(n).getCell(m).setCellValue("-");
	}
	for (int n = 42; n < 73; n++) {
	    for (int m = 0; m <= audNum * 5; m++)
		sh.getRow(n).getCell(m).setCellValue("-");
	}
	if (data1.size() > 0) {
	    Map<String,Object> row=new HashMap<String,Object>();
	    for (int a = 0; a < audNum; a++) {
		for (int i = 0,data1Size=data1.size(); i < data1Size; i++) {// 号码
		    row = data1.get(i);
		    String audTrm = row.get("Aud_trm") == null ? "" : (row.get("Aud_trm") + "").trim();
		    if (audList.get(a).equals(audTrm)) {
			int ind = row.get("rankNo") == null || ((row.get("rankNo") + "").trim()).equals("") ? -1 : Integer.parseInt((row.get("rankNo") + "").trim());
			for (int m = 5; m < 36; m++) {
			    if (ind + 4 == m) {// 当前行是否对应该排名的记录
				int j = 1 + a * 5;
				// 第一列：i/31==0 第二列：i/31==1
				sh.getRow(m).getCell(0).setCellValue(formatOutput(row.get("prvd_name") + ""));
				sh.getRow(m).getCell(j).setCellValue(formatOutput(row.get("qdyk_num_perc_rank") + ""));
				if (row.get("qdyk_subs_num") != null) {
				    // if (row.get("qdyk_subs_num").toString().substring(0, 5).equals("-9999")) {
				    // sh.getRow(m).getCell(j + 1).setCellValue("NA");
				    // } else {
				    sh.getRow(m).getCell(j + 1)
					    .setCellValue(row.get("qdyk_subs_num") instanceof BigDecimal ? ((BigDecimal) row.get("qdyk_subs_num")).doubleValue() : (Long) row.get("qdyk_subs_num"));
				    // }
				}
				if (row.get("qdyk_num_perc") != null) {
				    if (row.get("qdyk_num_perc").toString().substring(0, 5).equals("-9999")) {
					sh.getRow(m).getCell(j + 2).setCellValue("NA");
				    } else {
					sh.getRow(m).getCell(j + 2)
						.setCellValue(row.get("qdyk_num_perc") instanceof BigDecimal ? ((BigDecimal) row.get("qdyk_num_perc")).doubleValue() : (Long) row.get("qdyk_num_perc"));
				    }
				}
				if (row.get("qdyk_num_zb") != null) {
				    if (row.get("qdyk_num_zb").toString().substring(0, 5).equals("-9999")) {
					sh.getRow(m).getCell(j + 3).setCellValue("NA");
				    } else {
					sh.getRow(m).getCell(j + 3)
						.setCellValue(row.get("qdyk_num_zb") instanceof BigDecimal ? ((BigDecimal) row.get("qdyk_num_zb")).doubleValue() : (Long) row.get("qdyk_num_zb"));
				    }
				}
				if (row.get("qdyk_num_hb") != null) {
				    if (row.get("qdyk_num_hb").toString().substring(0, 5).equals("-9999")) {
					sh.getRow(m).getCell(j + 4).setCellValue("NA");
				    } else {
					sh.getRow(m).getCell(j + 4)
						.setCellValue(row.get("qdyk_num_hb") instanceof BigDecimal ? ((BigDecimal) row.get("qdyk_num_hb")).doubleValue() : (Long) row.get("qdyk_num_hb"));
				    }
				}
				break;
			    }
			}
		    }
		}
	    }
	}
	if (data2.size() > 0) {
	    Map<String, Object> row=new HashMap<String,Object>();
	    for (int a1 = 0; a1 < audNum; a1++) {
		for (int i = 0,data2Size=data2.size(); i < data2Size; i++) { // 渠道
		    row = data2.get(i);
		    String audTrm = row.get("Aud_trm") == null ? "" : (row.get("Aud_trm") + "").trim();
		    if (audList.get(a1).equals(audTrm)) {
			int ind = row.get("rankNo") == null || (row.get("rankNo") + "").equals("") ? -1 : Integer.parseInt((row.get("rankNo") + "").trim());
			for (int m = 42; m < 73; m++) {
			    if (m == ind + 41) {
				// 第一列：a/31==0 第二列：a/31==1
				int j = 1 + a1 * 5;
				sh.getRow(m).getCell(0).setCellValue(formatOutput(row.get("prvd_name") + ""));
				sh.getRow(m).getCell(j).setCellValue(formatOutput(row.get("qdyk_chnl_perc_rank") + ""));
				if (row.get("qdyk_chnl_num") != null) {
				    // if (row.get("qdyk_chnl_num").toString().substring(0, 5).equals("-9999")) {
				    // sh.getRow(m).getCell(j + 1).setCellValue("NA");
				    // } else {
				    sh.getRow(m).getCell(j + 1)
					    .setCellValue(row.get("qdyk_chnl_num") instanceof BigDecimal ? ((BigDecimal) row.get("qdyk_chnl_num")).doubleValue() : (Long) row.get("qdyk_chnl_num"));
				    // }
				}
				if (row.get("qdyk_chnl_perc") != null) {
				    if (row.get("qdyk_chnl_perc").toString().substring(0, 5).equals("-9999")) {
					sh.getRow(m).getCell(j + 2).setCellValue("NA");
				    } else {
					sh.getRow(m)
						.getCell(j + 2)
						.setCellValue(
							row.get("qdyk_chnl_perc") instanceof BigDecimal ? ((BigDecimal) row.get("qdyk_chnl_perc")).doubleValue() : (Long) row.get("qdyk_chnl_perc"));
				    }
				}
				if (row.get("qdyk_chnl_zb") != null) {
				    if (row.get("qdyk_chnl_zb").toString().substring(0, 5).equals("-9999")) {
					sh.getRow(m).getCell(j + 3).setCellValue("NA");
				    } else {
					sh.getRow(m).getCell(j + 3)
						.setCellValue(row.get("qdyk_chnl_zb") instanceof BigDecimal ? ((BigDecimal) row.get("qdyk_chnl_zb")).doubleValue() : (Long) row.get("qdyk_chnl_zb"));
				    }
				}
				if (row.get("qdyk_chnl_hb") != null) {
				    if (row.get("qdyk_chnl_hb").toString().substring(0, 5).equals("-9999")) {
					sh.getRow(m).getCell(j + 4).setCellValue("NA");
				    } else {
					sh.getRow(m).getCell(j + 4)
						.setCellValue(row.get("qdyk_chnl_hb") instanceof BigDecimal ? ((BigDecimal) row.get("qdyk_chnl_hb")).doubleValue() : (Long) row.get("qdyk_chnl_hb"));
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

    private void creatCell4SumAutrms(List<String> audList) {
	int audNum = audList.size();
	for (int i = 0; i < audNum; i++) {
	    int index = i * 2 + 2;
	    // 月份

	    sh.addMergedRegion(new CellRangeAddress(4, 4, index, index + 1));
	    for (int j = index; j < index + 2; j++)
		sh.getRow(4).createCell(j).setCellStyle(getStyle_boldNone());
	    sh.getRow(4).getCell(index).setCellValue(CalendarUtils.buildAuditCycle(audList.get(i)));

	    sh.getRow(5).createCell(index).setCellValue("数值");
	    sh.getRow(5).createCell(index + 1).setCellValue("增幅");
	    sh.getRow(5).getCell(index).setCellStyle(getStyle_boldNone());
	    sh.getRow(5).getCell(index + 1).setCellStyle(getStyle_boldNone());
	}
    }

    private void creatCell4Autrms(int rowNum, List<String> audList) {
	int audNum = audList.size();
	for (int i = 0; i < audNum; i++) {
	    int index = i * 5 + 1;
	    // 月份
	    for (int j = index; j < index + 5; j++)
		sh.getRow(rowNum).createCell(j).setCellStyle(getStyle_boldLeRigTop());
	    sh.getRow(rowNum).getCell(index).setCellValue(CalendarUtils.buildAuditCycle(audList.get(i)));
	    // 每个月份对应的5列表头
	    sh.getRow(rowNum + 1).createCell(index).setCellValue("排名");
	    sh.getRow(rowNum + 1).createCell(index + 2).setCellValue("占比 %");
	    if (rowNum == 3) {// 号码
		sh.getRow(4).createCell(index + 1).setCellValue("养卡号码数量");
		sh.getRow(4).createCell(index + 3).setCellValue("养卡号码数量环比增幅%(占比增幅）");
		sh.getRow(4).createCell(index + 4).setCellValue("养卡号码数量环比增幅%");
	    }
	    if (rowNum == 40) {// 渠道
		sh.getRow(41).createCell(index + 1).setCellValue("涉及渠道数量");
		sh.getRow(41).createCell(index + 3).setCellValue("渠道数量环比增幅%(占比增幅）");
		sh.getRow(41).createCell(index + 4).setCellValue("渠道数量环比增幅%");
	    }
	    for (int k = rowNum + 2; k < rowNum + 33; k++) {// 数据
		if (k == rowNum + 32) {// excel的表的最后一行
		    sh.getRow(k).createCell(index).setCellStyle(getStyle41());
		    sh.getRow(k).createCell(index + 1).setCellStyle(getStyle61());
		    sh.getRow(k).createCell(index + 2).setCellStyle(getStyle81());
		    sh.getRow(k).createCell(index + 3).setCellStyle(getStyle81());
		    sh.getRow(k).createCell(index + 4).setCellStyle(getStyle91());
		    break;
		}
		sh.getRow(k).createCell(index).setCellStyle(getStyle4());
		sh.getRow(k).createCell(index + 1).setCellStyle(getStyle6());
		sh.getRow(k).createCell(index + 2).setCellStyle(getStyle8());
		sh.getRow(k).createCell(index + 3).setCellStyle(getStyle8());
		sh.getRow(k).createCell(index + 4).setCellStyle(getStyle9());
	    }
	}
	// 为每个月份对应的5列表头设置格式
	sh.getRow(rowNum + 1).getCell(0).setCellStyle(getStyle_boldRight());
	for (int i = 1; i <= audNum * 5; i++) {//
	    if (i % 5 == 1) {
		sh.getRow(rowNum + 1).getCell(i).setCellStyle(getStyle_boldLeft());
	    } else if (i % 5 == 0) {
		sh.getRow(rowNum + 1).getCell(i).setCellStyle(getStyle_boldRight());
	    } else {
		sh.getRow(rowNum + 1).getCell(i).setCellStyle(getStyle_boldNone());
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

    // 为月份合并单元格
    public void addMergedRegions(int rowNum, int audNum) {
	for (int i = 0; i < audNum; i++) {
	    int firstCol = i * 5 + 1;
	    int lastCol = i * 5 + 5;
	    sh.addMergedRegion(new CellRangeAddress(rowNum, rowNum, firstCol, lastCol));
	}

    }

    private CellStyle getStyle00() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_LEFT);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style.setFont(font);
	return style;
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
	style.setAlignment(CellStyle.ALIGN_LEFT);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	style.setFont(font);
	return style;
    }

    private CellStyle getStyle_boldRigTop() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THICK);
	style.setBorderTop(CellStyle.BORDER_THICK);
	style.setWrapText(true); // 换行
	return style;
    }

    private CellStyle getStyle_boldLeRigTop() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THICK);
	style.setBorderRight(CellStyle.BORDER_THICK);
	style.setBorderTop(CellStyle.BORDER_THICK);
	style.setWrapText(true);
	return style;
    }

    private CellStyle getStyle_boldAll() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THICK);
	style.setBorderLeft(CellStyle.BORDER_THICK);
	style.setBorderRight(CellStyle.BORDER_THICK);
	style.setBorderTop(CellStyle.BORDER_THICK);
	style.setWrapText(true);
	return style;
    }

    private CellStyle getStyle_boldRight() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THICK);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setWrapText(true);
	return style;
    }

    private CellStyle getStyle_boldLeft() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THICK);
	style.setBorderRight(CellStyle.BORDER_THIN);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setWrapText(true);
	return style;
    }

    private CellStyle getStyle_boldNone() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THIN);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setWrapText(true);
	return style;
    }

    private CellStyle getStyle_boldNone_aLeft() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_LEFT);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THIN);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setWrapText(false);
	return style;
    }

    private CellStyle getStyle3() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 11);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THICK);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setWrapText(true);
	return style;
    }

    private CellStyle getStyle31() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 11);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THICK);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THICK);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setWrapText(true);
	return style;
    }

    private CellStyle getStyle4() {
	CellStyle style0 = wb.createCellStyle();
	style0.setAlignment(CellStyle.ALIGN_CENTER);
	style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 11);
	style0.setFont(font);
	style0.setBorderBottom(CellStyle.BORDER_THIN);
	style0.setBorderLeft(CellStyle.BORDER_THICK);
	style0.setBorderRight(CellStyle.BORDER_THIN);
	style0.setBorderTop(CellStyle.BORDER_THIN);
	style0.setWrapText(true);
	// style0.setDataFormat(wb.createDataFormat().getFormat("0"));
	return style0;
    }

    private CellStyle getStyle41() {
	CellStyle style0 = wb.createCellStyle();
	style0.setAlignment(CellStyle.ALIGN_CENTER);
	style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 11);
	style0.setFont(font);
	style0.setBorderBottom(CellStyle.BORDER_THICK);
	style0.setBorderLeft(CellStyle.BORDER_THICK);
	style0.setBorderRight(CellStyle.BORDER_THIN);
	style0.setBorderTop(CellStyle.BORDER_THIN);
	// style0.setDataFormat(wb.createDataFormat().getFormat("0"));
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
	style.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
	return style;
    }

    private CellStyle getStyle2() {
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
	style.setDataFormat(wb.createDataFormat().getFormat("0.00%"));// 对setCellValue时的string类型不起作用
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

    private CellStyle getStyle61() {
	CellStyle style0 = wb.createCellStyle();
	style0.setAlignment(CellStyle.ALIGN_CENTER);
	style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 11);
	style0.setFont(font);
	style0.setBorderBottom(CellStyle.BORDER_THICK);
	style0.setBorderLeft(CellStyle.BORDER_THIN);
	style0.setBorderRight(CellStyle.BORDER_THIN);
	style0.setBorderTop(CellStyle.BORDER_THIN);
	style0.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
	return style0;
    }

    private CellStyle getStyle80() {
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
	style0.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
	return style0;
    }

    private CellStyle getStyle8() {
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
	style0.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
	return style0;
    }

    private CellStyle getStyle81() {
	CellStyle style0 = wb.createCellStyle();
	style0.setAlignment(CellStyle.ALIGN_CENTER);
	style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 11);
	style0.setFont(font);
	style0.setBorderBottom(CellStyle.BORDER_THICK);
	style0.setBorderLeft(CellStyle.BORDER_THIN);
	style0.setBorderRight(CellStyle.BORDER_THIN);
	style0.setBorderTop(CellStyle.BORDER_THIN);
	style0.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
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
	style0.setBorderRight(CellStyle.BORDER_THICK);
	style0.setBorderTop(CellStyle.BORDER_THIN);
	style0.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
	return style0;
    }

    private CellStyle getStyle91() {
	CellStyle style0 = wb.createCellStyle();
	style0.setAlignment(CellStyle.ALIGN_CENTER);
	style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 11);
	style0.setFont(font);
	style0.setBorderBottom(CellStyle.BORDER_THICK);
	style0.setBorderLeft(CellStyle.BORDER_THIN);
	style0.setBorderRight(CellStyle.BORDER_THICK);
	style0.setBorderTop(CellStyle.BORDER_THIN);
	style0.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
	return style0;
    }
}
