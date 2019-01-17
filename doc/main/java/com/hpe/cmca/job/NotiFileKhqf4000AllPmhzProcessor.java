package com.hpe.cmca.job;

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
import org.springframework.stereotype.Service;

@Service("NotiFileKhqf4000AllPmhzProcessor")
public class NotiFileKhqf4000AllPmhzProcessor extends AbstractNotiFileProcessor {

    protected String focusCd = "4000";
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


    public boolean generate() throws Exception {
	if (Integer.parseInt(month) >= 201610) {
	    Integer thisYear = Integer.parseInt(month.substring(0, 4));
	    Integer thisMonth = Integer.parseInt("0".equals(month.substring(4, 5)) ? month.substring(5, 6) : month.substring(4, 6));
	    this.setFileName("2016年10月-" + thisYear + "年" + thisMonth + "月-" + "客户欠费排名汇总");
	    // 更新最新排名
	    notiFileGenService.getNotiFileKhqf4000Pmhz(month, "updateReportSort");
	    // 创建第一个sheet
	    sh = wb.createSheet("全公司审计结果摘要");
	    writeFirstPart(notiFileGenService.getNotiFileKhqf4000Pmhz(month, "allPersonReport"), sh, month);
	    writeSecondPart(notiFileGenService.getNotiFileKhqf4000Pmhz(month, "allReport"), sh, month);

	    // 创建第二个sheet
	    sh = wb.createSheet("各公司个人客户欠费排名汇总");
	    writeThirdPart(notiFileGenService.getNotiFileKhqf4000Pmhz(month, "prvdPersonReport"), sh, month);

	    // 创建第二个sheet
	    sh = wb.createSheet("各公司集团客户欠费排名汇总");
	    writeFourthPart(notiFileGenService.getNotiFileKhqf4000Pmhz(month, "prvdReport"), sh, month);
	} else {
	    return false;
	}
	return true;
    }

    /**
     * <pre>
     * Desc  将获取到的数据插入到excel‘客户欠费-排名汇总’下的sheet1中
     * @param data：获取的数据
     * @param sh：应插入的sheet
     * @param prvdId：省份
     * @param month：月份
     * @author feihu
     * Mar 29, 2017 3:31:13 PM
     * </pre>
     */
    public void writeFirstPart(List<Map<String, Object>> data, Sheet sh, String month) {
	sh.createRow(0);
	sh.getRow(0).createCell(0).setCellValue("“个人客户长期高额欠费”和“集团客户长期高额欠费”持续审计结果摘要");
	// 创建行
	sh.createRow(1).createCell(0).setCellStyle(getStyle0());
	sh.createRow(2).createCell(0).setCellStyle(getStyle9());
	for (int i = 3; i <= 12; i++) {
	    sh.createRow(i).createCell(0).setCellStyle(getStyle10());

	}
	sh.getRow(1).getCell(0).setCellValue("个人客户欠费总体情况");
	sh.getRow(2).getCell(0).setCellValue("欠费统计截止月份");
	sh.getRow(4).getCell(0).setCellValue("欠费金额（元）");
	sh.getRow(5).getCell(0).setCellValue("欠费用户数量（户）");
	sh.getRow(6).getCell(0).setCellValue("新增欠费用户欠费金额（元）");
	sh.getRow(7).getCell(0).setCellValue("新增欠费用户数量（户）");
	sh.getRow(8).getCell(0).setCellValue("原有欠费用户欠费金额（元）");
	sh.getRow(9).getCell(0).setCellValue("原有欠费用户数量（户）");
	
	sh.getRow(10).getCell(0).setCellValue("缴清全部欠费原有用户欠费回收金额（元）");
	sh.getRow(11).getCell(0).setCellValue("欠费用户回收数量");
	sh.getRow(12).getCell(0).setCellValue("其他原有用户欠费变动金额（元）");

	// sh.createRow(14).createCell(0).setCellValue("备注：以上指标中涉及“数量”的“增幅”指的是环比增幅，等于（本月数值-上月数值）/上月数值；涉及“占比”的“增幅”，等于本月占比-上月占比。");

	int j = 1;

	HashMap<String, String> mothMap = new HashMap<String, String>();
	List<String> mothList = getAudTrmListToSomeDate(month, "201610");
	for (int i = 0, dataSize=mothList.size(); i < dataSize; i++) {
	    // Map<String, Object> row = data.get(i);
	    String moth = mothList.get(i);
	    if (!mothMap.containsValue(moth)) {
		mothMap.put(Integer.toString(i), moth);
		Integer thisMonth = Integer.parseInt("0".equals(moth.substring(4, 5)) ? moth.substring(5, 6) : moth.substring(4, 6));
		sh.getRow(2).createCell(j).setCellValue(moth.substring(0, 4) + "年" + thisMonth + "月");
		// sh.getRow(2).createCell(j).setCellValue(moth.substring(4, 6) + "/1/" + moth.substring(0, 4));
		sh.getRow(3).createCell(j).setCellValue("数值");
		sh.getRow(3).createCell(j + 1).setCellValue("增幅");
		sh.getRow(2).getCell(j).setCellStyle(getStyle9());
		sh.getRow(2).createCell(j + 1).setCellStyle(getStyle9());
		for (int n = 4; n <= 12; n++) {
		    sh.getRow(n).createCell(j).setCellStyle(getStyle6());
		    sh.getRow(n).createCell(j + 1).setCellStyle(getStyle4());
		    sh.getRow(n).getCell(j).setCellValue("-");
		    sh.getRow(n).getCell(j + 1).setCellValue("-");
		}
		sh.addMergedRegion(new CellRangeAddress(2, 2, j, j + 1));
		sh.getRow(3).getCell(j).setCellStyle(getStyle9());
		sh.getRow(3).getCell(j + 1).setCellStyle(getStyle9());
		j = j + 2;
	    }

	}

	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.getRow(0).getCell(0).setCellStyle(getStyle00());
	// sh.getRow(14).getCell(0).setCellStyle(getStyle000());
	// sh.addMergedRegion(new CellRangeAddress(0, 0, 0, j - 1));
	sh.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
	// sh.addMergedRegion(new CellRangeAddress(14, 14, 0, j-1));
	sh.setColumnWidth(0, 256 * 35);
	for (int i = 1; i < j; i++) {
	    sh.setColumnWidth(i, 256 * 18);
	    // for (int m = 1; m <= 31; m++) {
	    // sh.getRow(m + 2).createCell(i).setCellStyle(getStyle6());
	    // }
	}

	sh.getRow(1).setHeightInPoints(35);

	Map<String,Object> row=new HashMap<String,Object>();
	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
	for (int i = 0; i < data.size(); i++) {
	    row = data.get(i);
	    // 插入单元格数据
	    if (row.size() == 0) {
		sh.getRow(2 + i).createCell(2).setCellValue("-");
	    } else {
		//this.logger.info("客戶欠費-全公司：本行数据为：" + row);
		int colNumber = 1 + 2 * (Integer.parseInt(getKey(mothMap, row.get("Aud_trm").toString())));
		// for(int n=5;n<=12;n++){
		// sh.getRow(n).createCell(colNumber).setCellStyle(getStyle6());
		// sh.getRow(n).createCell(colNumber+1).setCellStyle(getStyle4());
		// }
		// 数值
		if (row.get("tol_amt") != null) {
		    sh.getRow(4).getCell(colNumber).setCellValue(row.get("tol_amt") instanceof BigDecimal ? ((BigDecimal) row.get("tol_amt")).doubleValue() : (Long) row.get("tol_amt"));
		    sh.getRow(4).getCell(colNumber).setCellStyle(getStyle5());
		}
		if (row.get("tol_num") != null) {
		    sh.getRow(5).getCell(colNumber).setCellValue((Integer) row.get("tol_num"));
		}
		if (row.get("amt_new") != null) {
		    sh.getRow(6).getCell(colNumber).setCellValue(row.get("amt_new") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new")).doubleValue() : (Long) row.get("amt_new"));
		    sh.getRow(6).getCell(colNumber).setCellStyle(getStyle5());
		}
		if (row.get("subs_new") != null) {
		    // sh.getRow(7).getCell(colNumber).setCellValue(row.get("subs_new") instanceof BigDecimal ? ((BigDecimal) row.get("subs_new")).doubleValue() : (Long) row.get("subs_new"));
		    // sh.getRow(7).getCell(colNumber).setCellStyle(getStyle5());
		    sh.getRow(7).getCell(colNumber).setCellValue((Integer) row.get("subs_new"));
		}
		if (row.get("amt_before") != null) {
		    sh.getRow(8).getCell(colNumber).setCellValue(row.get("amt_before") instanceof BigDecimal ? ((BigDecimal) row.get("amt_before")).doubleValue() : (Long) row.get("amt_before"));
		    sh.getRow(8).getCell(colNumber).setCellStyle(getStyle5());
		}
		if (row.get("subs_before") != null) {
		    sh.getRow(9).getCell(colNumber).setCellValue((Integer) row.get("subs_before"));
		}
		
		if (row.get("amt_all_recover") != null) {
		    sh.getRow(10).getCell(colNumber).setCellValue(row.get("amt_all_recover") instanceof BigDecimal ? ((BigDecimal) row.get("amt_all_recover")).doubleValue() : (Long) row.get("amt_all_recover"));
		    sh.getRow(10).getCell(colNumber).setCellStyle(getStyle5());
		}
		if (row.get("sum_subs_recover") != null) {
		    sh.getRow(11).getCell(colNumber).setCellValue((Integer) row.get("sum_subs_recover"));
		}
		if (row.get("amt_other_var") != null) {
		    sh.getRow(12).getCell(colNumber).setCellValue(row.get("amt_other_var") instanceof BigDecimal ? ((BigDecimal) row.get("amt_other_var")).doubleValue() : (Long) row.get("amt_other_var"));
		    sh.getRow(12).getCell(colNumber).setCellStyle(getStyle5());
		}

		// 增幅
		if (row.get("tol_amt_per") != null) {
		    if (row.get("tol_amt_per").toString().equals("-9999.0000")) {
			sh.getRow(4).getCell(colNumber + 1).setCellValue("NA");
		    } else {
			sh.getRow(4).getCell(colNumber + 1)
				.setCellValue(row.get("tol_amt_per") instanceof BigDecimal ? ((BigDecimal) row.get("tol_amt_per")).doubleValue() : (Long) row.get("tol_amt_per"));
		    }
		}
		if (row.get("tol_num_per") != null) {
		    if (row.get("tol_num_per").toString().equals("-9999")) {
			sh.getRow(5).getCell(colNumber + 1).setCellValue("NA");
		    } else {
			sh.getRow(5).getCell(colNumber + 1)
				.setCellValue(row.get("tol_num_per") instanceof BigDecimal ? ((BigDecimal) row.get("tol_num_per")).doubleValue() : (Long) row.get("tol_num_per"));
		    }
		}
		if (row.get("amt_new_per") != null) {
		    if (row.get("amt_new_per").toString().substring(0, 5).equals("-9999")) {
			sh.getRow(6).getCell(colNumber + 1).setCellValue("NA");
		    } else {
			sh.getRow(6).getCell(colNumber + 1)
				.setCellValue(row.get("amt_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new_per")).doubleValue() : (Long) row.get("amt_new_per"));
		    }
		    // sh.getRow(6).getCell(colNumber + 1)
		    // .setCellValue(row.get("amt_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new_per")).doubleValue() : (Long) row.get("amt_new_per"));
		}
		if (row.get("subs_new_per") != null) {
		    if (row.get("subs_new_per").toString().substring(0,5).equals("-9999")) {
			sh.getRow(7).getCell(colNumber + 1).setCellValue("NA");
		    } else {
			sh.getRow(7).getCell(colNumber + 1)
				.setCellValue(row.get("subs_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_new_per")).doubleValue() : (Long) row.get("subs_new_per"));
		    }
		    // sh.getRow(7).getCell(colNumber + 1)
		    // .setCellValue(row.get("subs_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_new_per")).doubleValue() : (Long) row.get("subs_new_per"));
		}
		 if ( row.get("amt_before_per") != null) {
		     if (row.get("amt_before_per").toString().substring(0,5).equals("-9999")) {
			 sh.getRow(8).getCell(colNumber + 1).setCellValue("NA");
		     } else {
			sh.getRow(8).getCell(colNumber + 1).setCellValue(row.get("amt_before_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_before_per")).doubleValue() : (Long) row.get("amt_before_per"));
		     }
//		 sh.getRow(9).getCell(colNumber + 1).setCellValue(row.get("taoli_huanbi") instanceof BigDecimal ? ((BigDecimal) row.get("taoli_huanbi")).doubleValue() : (Long) row
//		 .get("taoli_huanbi"));
		 //sh.getRow(10).getCell(colNumber+1).setCellStyle(getStyle4());
		 }
		 if (row.get("subs_before_per") != null) {
		     if (row.get("subs_before_per").toString().substring(0,5).equals("-9999")) {
			 sh.getRow(9).getCell(colNumber + 1).setCellValue("NA");
		     } else {
			sh.getRow(9).getCell(colNumber + 1).setCellValue(row.get("subs_before_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_before_per")).doubleValue() : (Long) row.get("subs_before_per"));
		     }
//		 sh.getRow(8).getCell(colNumber + 1).setCellValue(row.get("diff_weigui_pnt") instanceof BigDecimal ? ((BigDecimal) row.get("diff_weigui_pnt")).doubleValue() : (Long) row
//		 .get("diff_weigui_pnt"));
		 }
		 
		 
		 if ( row.get("amt_all_recover_inc") != null) {
		     if (row.get("amt_all_recover_inc").toString().substring(0,5).equals("-9999")) {
		    	 sh.getRow(10).getCell(colNumber + 1).setCellValue("NA");
		     } else {
		    	 sh.getRow(10).getCell(colNumber + 1).setCellValue(row.get("amt_all_recover_inc") instanceof BigDecimal ? ((BigDecimal) row.get("amt_all_recover_inc")).doubleValue() : (Long) row.get("amt_all_recover_inc"));
		     }
		 }
		 
		 
		 if (row.get("sum_subs_recover_inc") != null) {
		     if (row.get("sum_subs_recover_inc").toString().substring(0,5).equals("-9999")) {
		    	 sh.getRow(11).getCell(colNumber + 1).setCellValue("NA");
		     } else {
		    	 sh.getRow(11).getCell(colNumber + 1).setCellValue(row.get("sum_subs_recover_inc") instanceof BigDecimal ? ((BigDecimal) row.get("sum_subs_recover_inc")).doubleValue() : (Long) row.get("sum_subs_recover_inc"));
		     }
		 }
		 
		 if (row.get("amt_other_var_inc") != null) {
		     if (row.get("amt_other_var_inc").toString().substring(0,5).equals("-9999")) {
		    	 sh.getRow(12).getCell(colNumber + 1).setCellValue("NA");
		     } else {
		    	 sh.getRow(12).getCell(colNumber + 1).setCellValue(row.get("amt_other_var_inc") instanceof BigDecimal ? ((BigDecimal) row.get("amt_other_var_inc")).doubleValue() : (Long) row.get("amt_other_var_inc"));
		     }
		 }
		 


	    }

	}

    }

    /**
     * <pre>
     * Desc  将获取到的数据插入到sheet中
     * @param data：获取的数据
     * @param sh：即将插入的sheet
     * @param prvdId：省份
     * @param month：月份
     * @author feihu
     * Mar 29, 2017 3:36:35 PM
     * </pre>
     */
    public void writeSecondPart(List<Map<String, Object>> data, Sheet sh, String month) {
	// 创建行
	sh.createRow(11+3).createCell(0).setCellStyle(getStyle0());
	sh.createRow(12+3).createCell(0).setCellStyle(getStyle9());
	for (int i = 13+3; i <= 22+3; i++) {
	    sh.createRow(i).createCell(0).setCellStyle(getStyle10());

	}
	sh.getRow(11+3).getCell(0).setCellValue("集团客户欠费总体情况");
	sh.getRow(12+3).getCell(0).setCellValue("欠费统计截止月份");
	sh.getRow(14+3).getCell(0).setCellValue("欠费金额（元）");
	sh.getRow(15+3).getCell(0).setCellValue("欠费用户数量（户）");
	sh.getRow(16+3).getCell(0).setCellValue("新增欠费用户欠费金额（元）");
	sh.getRow(17+3).getCell(0).setCellValue("新增欠费用户数量（户）");
	sh.getRow(18+3).getCell(0).setCellValue("原有欠费用户欠费金额（元）");
	sh.getRow(19+3).getCell(0).setCellValue("原有欠费用户数量（户）");
	
	sh.getRow(20+3).getCell(0).setCellValue("缴清全部欠费原有用户欠费回收金额（元）");
	sh.getRow(21+3).getCell(0).setCellValue("欠费用户回收数量");
	sh.getRow(22+3).getCell(0).setCellValue("其他原有用户欠费变动金额（元）");

	sh.createRow(24+3).createCell(0).setCellValue("备注：以上指标中涉及“数量”和“金额”的“增幅”指的是环比增幅，等于（本月数值-上月数值）/上月数值；涉及“占比”的“增幅”，等于本月占比-上月占比。");

	int j = 1;

	HashMap<String, String> mothMap = new HashMap<String, String>();
	List<String> mothList = getAudTrmListToSomeDate(month, "201610");
	for (int i = 0,mothSize=mothList.size(); i < mothSize; i++) {
	    // Map<String, Object> row = data.get(i);
	    String moth = mothList.get(i);
	    if (!mothMap.containsValue(moth)) {
		mothMap.put(Integer.toString(i), moth);
		Integer thisMonth = Integer.parseInt("0".equals(moth.substring(4, 5)) ? moth.substring(5, 6) : moth.substring(4, 6));
		sh.getRow(12+3).createCell(j).setCellValue(moth.substring(0, 4) + "年" + thisMonth + "月");
		// sh.getRow(12).createCell(j).setCellValue(moth.substring(4, 6) + "/1/" + moth.substring(0, 4));
		sh.getRow(13+3).createCell(j).setCellValue("数值");
		sh.getRow(13+3).createCell(j + 1).setCellValue("增幅");
		sh.getRow(12+3).getCell(j).setCellStyle(getStyle9());
		sh.getRow(12+3).createCell(j + 1).setCellStyle(getStyle9());
		for (int n = 14+3; n <= 22+3; n++) {
		    sh.getRow(n).createCell(j).setCellStyle(getStyle6());
		    sh.getRow(n).createCell(j + 1).setCellStyle(getStyle4());
		    sh.getRow(n).getCell(j).setCellValue("-");
		    sh.getRow(n).getCell(j + 1).setCellValue("-");
		}
		sh.addMergedRegion(new CellRangeAddress(12+3, 12+3, j, j + 1));
		sh.getRow(13+3).getCell(j).setCellStyle(getStyle9());
		sh.getRow(13+3).getCell(j + 1).setCellStyle(getStyle9());
		j = j + 2;
	    }

	}

	// sh.getRow(21).getCell(0).setCellStyle(getStyle000());
	sh.addMergedRegion(new CellRangeAddress(12+3, 13+3, 0, 0));
	// sh.addMergedRegion(new CellRangeAddress(21, 21, 0, j - 1));
	sh.setColumnWidth(0, 256 * 35);
	for (int i = 1; i < j; i++) {
	    sh.setColumnWidth(i, 256 * 18);
	    // for (int m = 1; m <= 31; m++) {
	    // sh.getRow(m + 2).createCell(i).setCellStyle(getStyle6());
	    // }
	}

	sh.getRow(1).setHeightInPoints(35);

	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
	Map<String,Object> row=new HashMap<String,Object>();
	for (int i = 0,dataSize=data.size(); i <dataSize; i++) {
	    row = data.get(i);
	    // 插入单元格数据
	    if (row.size() == 0) {
		sh.getRow(2 + i).createCell(2).setCellValue("-");
	    } else {
		//this.logger.info("终端套利-全公司：本行数据为：" + row);
		int colNumber = 1 + 2 * (Integer.parseInt(getKey(mothMap, row.get("Aud_trm").toString())));
		// for(int n=5;n<=12;n++){
		// sh.getRow(n).createCell(colNumber).setCellStyle(getStyle6());
		// sh.getRow(n).createCell(colNumber+1).setCellStyle(getStyle4());
		// }
		// 数值
		if (row.get("tol_amt") != null) {
		    sh.getRow(14+3).getCell(colNumber).setCellValue(row.get("tol_amt") instanceof BigDecimal ? ((BigDecimal) row.get("tol_amt")).doubleValue() : (Long) row.get("tol_amt"));
		    sh.getRow(14+3).getCell(colNumber).setCellStyle(getStyle5());
		}
		if (row.get("tol_num") != null) {
		    sh.getRow(15+3).getCell(colNumber).setCellValue((Integer) row.get("tol_num"));
		}
		if (row.get("amt_new") != null) {
		    sh.getRow(16+3).getCell(colNumber).setCellValue(row.get("amt_new") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new")).doubleValue() : (Long) row.get("amt_new"));
		    sh.getRow(16+3).getCell(colNumber).setCellStyle(getStyle5());
		}
		if (row.get("subs_new") != null) {
		    // sh.getRow(17).getCell(colNumber).setCellValue(row.get("subs_new") instanceof BigDecimal ? ((BigDecimal) row.get("subs_new")).doubleValue() : (Long) row.get("subs_new"));
		    // sh.getRow(17).getCell(colNumber).setCellStyle(getStyle5());
		    sh.getRow(17+3).getCell(colNumber).setCellValue((Integer) row.get("subs_new"));
		}
		if (row.get("amt_before") != null) {
		    sh.getRow(18+3).getCell(colNumber).setCellValue(row.get("amt_before") instanceof BigDecimal ? ((BigDecimal) row.get("amt_before")).doubleValue() : (Long) row.get("amt_before"));
		    sh.getRow(18+3).getCell(colNumber).setCellStyle(getStyle5());
		}
		if (row.get("subs_before") != null) {
		    sh.getRow(19+3).getCell(colNumber).setCellValue((Integer) row.get("subs_before"));
		}
		
		if (row.get("amt_all_recover") != null) {
		    sh.getRow(20+3).getCell(colNumber).setCellValue(row.get("amt_all_recover") instanceof BigDecimal ? ((BigDecimal) row.get("amt_all_recover")).doubleValue() : (Long) row.get("amt_all_recover"));
		    sh.getRow(20+3).getCell(colNumber).setCellStyle(getStyle5());
		}
		if (row.get("sum_subs_recover") != null) {
		    sh.getRow(21+3).getCell(colNumber).setCellValue((Integer) row.get("sum_subs_recover"));
		}
		if (row.get("amt_other_var") != null) {
		    sh.getRow(22+3).getCell(colNumber).setCellValue(row.get("amt_other_var") instanceof BigDecimal ? ((BigDecimal) row.get("amt_other_var")).doubleValue() : (Long) row.get("amt_other_var"));
		    sh.getRow(22+3).getCell(colNumber).setCellStyle(getStyle5());
		}

		// 增幅
		if (row.get("tol_amt_per") != null) {
		    if (row.get("tol_amt_per").toString().substring(0, 5).equals("-9999")) {
			sh.getRow(14+3).getCell(colNumber + 1).setCellValue("NA");
		    } else {
			sh.getRow(14+3).getCell(colNumber + 1)
				.setCellValue(row.get("tol_amt_per") instanceof BigDecimal ? ((BigDecimal) row.get("tol_amt_per")).doubleValue() : (Long) row.get("tol_amt_per"));
		    }
		    // sh.getRow(14).getCell(colNumber + 1)
		    // .setCellValue(row.get("tol_amt_per") instanceof BigDecimal ? ((BigDecimal) row.get("tol_amt_per")).doubleValue() : (Long) row.get("tol_amt_per"));
		}
		if (row.get("tol_num_per") != null) {
		    if (row.get("tol_num_per").toString().substring(0, 5).equals("-9999")) {
			sh.getRow(15+3).getCell(colNumber + 1).setCellValue("NA");
		    } else {
			sh.getRow(15+3).getCell(colNumber + 1)
				.setCellValue(row.get("tol_num_per") instanceof BigDecimal ? ((BigDecimal) row.get("tol_num_per")).doubleValue() : (Long) row.get("tol_num_per"));
		    }
		    // sh.getRow(15).getCell(colNumber + 1)
		    // .setCellValue(row.get("tol_num_per") instanceof BigDecimal ? ((BigDecimal) row.get("tol_num_per")).doubleValue() : (Long) row.get("tol_num_per"));
		}
		if (row.get("amt_new_per") != null) {
		    if (row.get("amt_new_per").toString().substring(0, 5).equals("-9999")) {
			sh.getRow(16+3).getCell(colNumber + 1).setCellValue("NA");
		    } else {
			sh.getRow(16+3).getCell(colNumber + 1)
				.setCellValue(row.get("amt_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new_per")).doubleValue() : (Long) row.get("amt_new_per"));
		    }
		    // sh.getRow(16).getCell(colNumber + 1)
		    // .setCellValue(row.get("amt_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new_per")).doubleValue() : (Long) row.get("amt_new_per"));
		}
		if (row.get("subs_new_per") != null) {
		    if (row.get("subs_new_per").toString().substring(0, 5).equals("-9999")) {
			sh.getRow(17+3).getCell(colNumber + 1).setCellValue("NA");
		    } else {
			sh.getRow(17+3).getCell(colNumber + 1)
				.setCellValue(row.get("subs_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_new_per")).doubleValue() : (Long) row.get("subs_new_per"));
		    }
		    // sh.getRow(17).getCell(colNumber + 1)
		    // .setCellValue(row.get("subs_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_new_per")).doubleValue() : (Long) row.get("subs_new_per"));
		}
		if ( row.get("amt_before_per") != null) {
		     if (row.get("amt_before_per").toString().substring(0,5).equals("-9999")) {
			 sh.getRow(18+3).getCell(colNumber + 1).setCellValue("NA");
		     } else {
			sh.getRow(18+3).getCell(colNumber + 1).setCellValue(row.get("amt_before_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_before_per")).doubleValue() : (Long) row.get("amt_before_per"));
		     }
		 }
		 if (row.get("subs_before_per") != null) {
		     if (row.get("subs_before_per").toString().substring(0,5).equals("-9999")) {
			 sh.getRow(19+3).getCell(colNumber + 1).setCellValue("NA");
		     } else {
			sh.getRow(19+3).getCell(colNumber + 1).setCellValue(row.get("subs_before_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_before_per")).doubleValue() : (Long) row.get("subs_before_per"));
		     }
		 }
		 
		 if (row.get("amt_all_recover_inc") != null) {
		     if (row.get("amt_all_recover_inc").toString().substring(0,5).equals("-9999")) {
		    	 sh.getRow(20+3).getCell(colNumber + 1).setCellValue("NA");
		     } else {
		    	 sh.getRow(20+3).getCell(colNumber + 1).setCellValue(row.get("amt_all_recover_inc") instanceof BigDecimal ? ((BigDecimal) row.get("amt_all_recover_inc")).doubleValue() : (Long) row.get("amt_all_recover_inc"));
		     }
		 }
		 
		 if (row.get("sum_subs_recover_inc") != null) {
		     if (row.get("sum_subs_recover_inc").toString().substring(0,5).equals("-9999")) {
		    	 sh.getRow(21+3).getCell(colNumber + 1).setCellValue("NA");
		     } else {
		    	 sh.getRow(21+3).getCell(colNumber + 1).setCellValue(row.get("sum_subs_recover_inc") instanceof BigDecimal ? ((BigDecimal) row.get("sum_subs_recover_inc")).doubleValue() : (Long) row.get("sum_subs_recover_inc"));
		     }
		 }
		 
		 if (row.get("amt_other_var_inc") != null) {
		     if (row.get("amt_other_var_inc").toString().substring(0,5).equals("-9999")) {
		    	 sh.getRow(22+3).getCell(colNumber + 1).setCellValue("NA");
		     } else {
		    	 sh.getRow(22+3).getCell(colNumber + 1).setCellValue(row.get("amt_other_var_inc") instanceof BigDecimal ? ((BigDecimal) row.get("amt_other_var_inc")).doubleValue() : (Long) row.get("amt_other_var_inc"));
		     }
		 }
		 

	    }

	}

    }

    /**
     * <pre>
     * Desc  将获取到的数据插入到sheet中
     * @param data：获取的数据
     * @param sh：即将插入的sheet
     * @param prvdId：省份
     * @param month：月份
     * @author feihu
     * Mar 29, 2017 3:36:35 PM
     * </pre>
     */
    public void writeThirdPart(List<Map<String, Object>> data, Sheet sh, String month) {
	sh.createRow(0);
	sh.getRow(0).createCell(0).setCellValue("各公司个人客户长期高额欠费情况");
	// 创建行
	for (int i = 2; i < 35; i++) {
	    sh.createRow(i).createCell(0).setCellStyle(getStyle6());
	    sh.getRow(i).getCell(0).setCellValue("-");
	}
	sh.getRow(2).createCell(0).setCellValue("省份名称");
	int j = 1;

	HashMap<String, String> mothMap = new HashMap<String, String>();
	List<String> mothList = getAudTrmListToSomeDate(month, "201610");
	for (int i = 0,mothSize=mothList.size(); i < mothSize; i++) {
	    String moth = mothList.get(i);
	    if (!mothMap.containsValue(moth)) {
		mothMap.put(Integer.toString(i), moth);
		Integer thisMonth = Integer.parseInt("0".equals(moth.substring(4, 5)) ? moth.substring(5, 6) : moth.substring(4, 6));
		sh.getRow(2).createCell(j).setCellValue(moth.substring(0, 4) + "年" + thisMonth + "月");
		// sh.getRow(2).createCell(j).setCellValue(moth.substring(4, 6) + "/1/" + moth.substring(0, 4));
		sh.getRow(3).createCell(j).setCellValue("欠费金额排名");
		sh.getRow(3).createCell(j + 1).setCellValue("欠费金额（元）");
		sh.getRow(3).createCell(j + 2).setCellValue("欠费用户数量（户）");
		sh.getRow(3).createCell(j + 3).setCellValue("新增欠费用户欠费金额（元）");
		sh.getRow(3).createCell(j + 4).setCellValue("新增欠费用户数量（户）");
		sh.getRow(3).createCell(j + 5).setCellValue("新增欠费用户欠费金额占上期长期高额欠费金额的比例（%）");
		sh.getRow(3).createCell(j + 6).setCellValue("原有欠费用户欠费金额（元）");
		sh.getRow(3).createCell(j + 7).setCellValue("原有欠费用户数量（户）");
		sh.getRow(3).createCell(j + 8).setCellValue("欠费金额环比");
		sh.getRow(3).createCell(j + 9).setCellValue("欠费用户数环比");
		sh.getRow(3).createCell(j + 10).setCellValue("缴清全部欠费原有用户欠费回收金额（元）");
		sh.getRow(3).createCell(j + 11).setCellValue("欠费用户回收数量");
		sh.getRow(3).createCell(j + 12).setCellValue("当期回收欠费金额占上期长期高额欠费的比例（%）");
		sh.getRow(3).createCell(j + 13).setCellValue("其他原有用户欠费变动金额（元）");

		for (int m = 0; m <= 8+5; m++) {
		    if (m == 0) {
		    	sh.getRow(2).getCell(j).setCellStyle(getStyle9());
		    } else {
		    	sh.getRow(2).createCell(j + m).setCellStyle(getStyle9());
		    	sh.getRow(2).getCell(j + m).setCellValue("-");
		    }

		    sh.getRow(3).getCell(j + m).setCellStyle(getStyle9());
		}
		sh.getRow(3).getCell(0).setCellStyle(getStyle9());
		for (int m = 0; m <= 8+5; m++) {
		    for (int n = 4; n <= 34; n++) {
			sh.getRow(n).createCell(j + m).setCellStyle(getStyle6());
			sh.getRow(n).getCell(j + m).setCellValue("-");
		    }
		}
		sh.addMergedRegion(new CellRangeAddress(2, 2, j, j + 8+5));
		j = j + 9+5;
	    }

	}
	sh.getRow(2).getCell(0).setCellStyle(getStyle9());

	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.getRow(0).getCell(0).setCellStyle(getStyle00());
	sh.addMergedRegion(new CellRangeAddress(0, 0, 0, j - 1));
	sh.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));

	for (int i = 2; i < j; i++) {
	    sh.setColumnWidth(i, 256 * 16);
	    // for (int m = 1; m <= 31; m++) {
	    // sh.getRow(m + 2).createCell(i).setCellStyle(getStyle6());
	    // }
	}

	sh.getRow(2).setHeightInPoints(35);
	sh.getRow(3).setHeightInPoints(40);

	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
	Map<String,Object> row=new HashMap<String,Object>();
	for (int i = 0,dataSize=data.size(); i < dataSize; i++) {
	    row = data.get(i);
	    // 插入单元格数据
	    if (row.size() == 0) {
		sh.getRow(2 + i).createCell(2).setCellValue("-");
	    } else {
		//this.logger.info("终端套利-全公司：本行数据为：" + row);
		int rowNumber = 0;
		int colNumber = 1 + 14 * (Integer.parseInt(getKey(mothMap, row.get("Aud_trm").toString())));
		if (row.get("amt_order_new") != null && (Integer) row.get("amt_order_new") != 0) {
		    rowNumber = (Integer) row.get("amt_order_new") + 3;
		    if (row.get("prvd_nm") != null) {
			sh.getRow(rowNumber).getCell(0).setCellValue(row.get("prvd_nm").toString());
		    }
		    sh.getRow(rowNumber).getCell(0).setCellStyle(getStyle6());

		    // 排名==异常销售占比排名 by 后台-文德
		    if (row.get("amt_order") != null) {
			sh.getRow(rowNumber).getCell(colNumber).setCellValue((Integer) row.get("amt_order"));
		    }
		    if (row.get("tol_amt") != null) {
			sh.getRow(rowNumber).getCell(colNumber + 1)
				.setCellValue(row.get("tol_amt") instanceof BigDecimal ? ((BigDecimal) row.get("tol_amt")).doubleValue() : (Long) row.get("tol_amt"));
			sh.getRow(rowNumber).getCell(colNumber + 1).setCellStyle(getStyle5());
		    }
		    if (row.get("tol_num") != null) {
			sh.getRow(rowNumber).getCell(colNumber + 2).setCellValue((Integer) row.get("tol_num"));
		    }
		    if (row.get("amt_new") != null) {
			sh.getRow(rowNumber).getCell(colNumber + 3)
				.setCellValue(row.get("amt_new") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new")).doubleValue() : (Long) row.get("amt_new"));
			sh.getRow(rowNumber).getCell(colNumber + 3).setCellStyle(getStyle5());
		    }
		    if (row.get("subs_new") != null) {
			sh.getRow(rowNumber).getCell(colNumber + 4).setCellValue((Integer) row.get("subs_new"));
		    }
		    if (row.get("amt_new_per1") != null) {
		    	if (row.get("amt_new_per1").toString().equals("-9999.00")){
		    		sh.getRow(rowNumber).getCell(colNumber + 5).setCellValue("-");
		    	}else{
		    		sh.getRow(rowNumber).getCell(colNumber + 5)
					.setCellValue(row.get("amt_new_per1") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new_per1")).doubleValue() : (Long) row.get("amt_new_per1"));
		    		sh.getRow(rowNumber).getCell(colNumber + 5).setCellStyle(getStyle5());
		    	}
			}
		    if (row.get("amt_before") != null) {
			sh.getRow(rowNumber).getCell(colNumber + 6)
				.setCellValue(row.get("amt_before") instanceof BigDecimal ? ((BigDecimal) row.get("amt_before")).doubleValue() : (Long) row.get("amt_before"));
			sh.getRow(rowNumber).getCell(colNumber + 6).setCellStyle(getStyle5());
		    }
		    if (row.get("subs_before") != null) {
			sh.getRow(rowNumber).getCell(colNumber + 7).setCellValue((Integer) row.get("subs_before"));
		    }
		    if (row.get("amt_new_per") != null) {
			if (row.get("amt_new_per").toString().substring(0, 5).equals("-9999")) {
			    sh.getRow(rowNumber).getCell(colNumber + 8).setCellValue("NA");
			} else {
			    sh.getRow(rowNumber).getCell(colNumber + 8)
				    .setCellValue(row.get("amt_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new_per")).doubleValue() : (Long) row.get("amt_new_per"));
			    sh.getRow(rowNumber).getCell(colNumber + 8).setCellStyle(getStyle4());
			}
			// sh.getRow(rowNumber).getCell(colNumber + 7)
			// .setCellValue(row.get("amt_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new_per")).doubleValue() : (Long) row.get("amt_new_per"));
			// sh.getRow(rowNumber).getCell(colNumber + 7).setCellStyle(getStyle4());
		    }
		    if (row.get("subs_new_per") != null) {
			if (row.get("subs_new_per").toString().substring(0, 5).equals("-9999")) {
			    sh.getRow(rowNumber).getCell(colNumber + 9).setCellValue("NA");
			} else {
			    sh.getRow(rowNumber).getCell(colNumber + 9)
				.setCellValue(row.get("subs_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_new_per")).doubleValue() : (Long) row.get("subs_new_per"));
			    sh.getRow(rowNumber).getCell(colNumber + 9).setCellStyle(getStyle4());
			}
//			sh.getRow(rowNumber).getCell(colNumber + 8)
//				.setCellValue(row.get("subs_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_new_per")).doubleValue() : (Long) row.get("subs_new_per"));
//			sh.getRow(rowNumber).getCell(colNumber + 8).setCellStyle(getStyle4());
		    }
		    if (row.get("amt_all_recover") != null) {
		    	if(row.get("amt_all_recover").toString().equals("-9999.00")){
		    		 sh.getRow(rowNumber).getCell(colNumber + 10).setCellValue("-");
		    	}else{
		    		sh.getRow(rowNumber).getCell(colNumber + 10)
					.setCellValue(row.get("amt_all_recover") instanceof BigDecimal ? ((BigDecimal) row.get("amt_all_recover")).doubleValue() : (Long) row.get("amt_all_recover"));
				sh.getRow(rowNumber).getCell(colNumber + 10).setCellStyle(getStyle5());
		    	}
				
		    }
			if (row.get("sum_subs_recover") != null) {
				if(row.get("sum_subs_recover").toString().equals("-9999"))
					sh.getRow(rowNumber).getCell(colNumber + 11).setCellValue("-");
				else
					sh.getRow(rowNumber).getCell(colNumber + 11).setCellValue((Integer) row.get("sum_subs_recover"));
			}
			 if (row.get("amt_recover_per") != null) {
				 if (row.get("amt_recover_per").toString().equals("-9999.00")) {
					    sh.getRow(rowNumber).getCell(colNumber + 12).setCellValue("-");
					} else {
					sh.getRow(rowNumber).getCell(colNumber + 12)
						.setCellValue(row.get("amt_recover_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_recover_per")).doubleValue() : (Long) row.get("amt_recover_per"));
					sh.getRow(rowNumber).getCell(colNumber + 12).setCellStyle(getStyle5());
					}
			 }
		    if (row.get("amt_other_var") != null) {
		    	if(row.get("amt_other_var").toString().equals("-9999.00")){
		    		sh.getRow(rowNumber).getCell(colNumber + 13).setCellValue("-");
		    	}else{
		    		sh.getRow(rowNumber).getCell(colNumber + 13)
					.setCellValue(row.get("amt_other_var") instanceof BigDecimal ? ((BigDecimal) row.get("amt_other_var")).doubleValue() : (Long) row.get("amt_other_var"));
		    		sh.getRow(rowNumber).getCell(colNumber + 13).setCellStyle(getStyle5());
		    	}
				
		    }
		    
		}
	    }

	}

    }

    /**
     * <pre>
     * Desc  将获取到的数据插入到sheet中
     * @param data：获取的数据
     * @param sh：即将插入的sheet
     * @param prvdId：省份
     * @param month：月份
     * @author feihu
     * Mar 29, 2017 3:36:35 PM
     * </pre>
     */
    public void writeFourthPart(List<Map<String, Object>> data, Sheet sh, String month) {
	sh.createRow(0);
	sh.getRow(0).createCell(0).setCellValue("各公司集团客户长期高额欠费情况");
	// 创建行
	for (int i = 2; i < 35; i++) {
	    sh.createRow(i).createCell(0).setCellStyle(getStyle6());
	    sh.getRow(i).getCell(0).setCellValue("-");
	}
	sh.getRow(2).createCell(0).setCellValue("省份名称");
	int j = 1;

	HashMap<String, String> mothMap = new HashMap<String, String>();
	List<String> mothList = getAudTrmListToSomeDate(month, "201610");
	for (int i = 0,mothSize=mothList.size(); i < mothSize; i++) {
	    String moth = mothList.get(i);
	    if (!mothMap.containsValue(moth)) {
		mothMap.put(Integer.toString(i), moth);
		Integer thisMonth = Integer.parseInt("0".equals(moth.substring(4, 5)) ? moth.substring(5, 6) : moth.substring(4, 6));
		sh.getRow(2).createCell(j).setCellValue(moth.substring(0, 4) + "年" + thisMonth + "月");
		// sh.getRow(2).createCell(j).setCellValue(moth.substring(4, 6) + "/1/" + moth.substring(0, 4));
		sh.getRow(3).createCell(j).setCellValue("欠费金额排名");
		sh.getRow(3).createCell(j + 1).setCellValue("欠费金额（元）");
		sh.getRow(3).createCell(j + 2).setCellValue("欠费用户数量（户）");
		sh.getRow(3).createCell(j + 3).setCellValue("新增欠费用户欠费金额（元）");
		sh.getRow(3).createCell(j + 4).setCellValue("新增欠费用户数量（户）");
		sh.getRow(3).createCell(j + 5).setCellValue("新增欠费用户欠费金额占上期长期高额欠费金额的比例（%）");
		sh.getRow(3).createCell(j + 6).setCellValue("原有欠费用户欠费金额（元）");
		sh.getRow(3).createCell(j + 7).setCellValue("原有欠费用户数量（户）");
		sh.getRow(3).createCell(j + 8).setCellValue("欠费金额环比");
		sh.getRow(3).createCell(j + 9).setCellValue("欠费用户数环比");
		
		sh.getRow(3).createCell(j + 10).setCellValue("缴清全部欠费原有用户欠费回收金额（元）");
		sh.getRow(3).createCell(j + 11).setCellValue("欠费用户回收数量");
		sh.getRow(3).createCell(j + 12).setCellValue("当期回收欠费金额占上期长期高额欠费的比例（%）");
		sh.getRow(3).createCell(j + 13).setCellValue("其他原有用户欠费变动金额（元）");

		for (int m = 0; m <= 8+5; m++) {
		    if (m == 0) {
			sh.getRow(2).getCell(j).setCellStyle(getStyle9());
		    } else {
			sh.getRow(2).createCell(j + m).setCellStyle(getStyle9());
			sh.getRow(2).getCell(j + m).setCellValue("-");
		    }

		    sh.getRow(3).getCell(j + m).setCellStyle(getStyle9());
		}
		sh.getRow(3).getCell(0).setCellStyle(getStyle9());
		for (int m = 0; m <= 8+5; m++) {
		    for (int n = 4; n <= 34; n++) {
			sh.getRow(n).createCell(j + m).setCellStyle(getStyle6());
			sh.getRow(n).getCell(j + m).setCellValue("-");
		    }
		}
		sh.addMergedRegion(new CellRangeAddress(2, 2, j, j + 8+5));
		j = j + 9+5;
	    }

	}
	sh.getRow(2).getCell(0).setCellStyle(getStyle9());

	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.getRow(0).getCell(0).setCellStyle(getStyle00());
	sh.addMergedRegion(new CellRangeAddress(0, 0, 0, j - 1));
	sh.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
	for (int i = 2; i < j; i++) {
	    sh.setColumnWidth(i, 256 * 16);
	    // for (int m = 1; m <= 31; m++) {
	    // sh.getRow(m + 2).createCell(i).setCellStyle(getStyle6());
	    // }
	}

	sh.getRow(2).setHeightInPoints(35);
	sh.getRow(3).setHeightInPoints(40);
	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
	for (int i = 0,dataSize=data.size(); i < dataSize; i++) {
	    Map<String, Object> row = data.get(i);
	    // 插入单元格数据
	    if (row.size() == 0) {
		sh.getRow(2 + i).createCell(2).setCellValue("-");
	    } else {
		//this.logger.info("终端套利-全公司：本行数据为：" + row);
		int rowNumber = 0;
		int colNumber = 1 + 14 * (Integer.parseInt(getKey(mothMap, row.get("Aud_trm").toString())));
		if (row.get("amt_order_new") != null && (Integer) row.get("amt_order_new") != 0) {
		    rowNumber = (Integer) row.get("amt_order_new") + 3;
		    if (row.get("prvd_nm") != null) {
			sh.getRow(rowNumber).getCell(0).setCellValue(row.get("prvd_nm").toString());
		    }
		    sh.getRow(rowNumber).getCell(0).setCellStyle(getStyle6());

		    // 排名==异常销售占比排名 by 后台-文德
		    if (row.get("amt_order") != null) {
			sh.getRow(rowNumber).getCell(colNumber).setCellValue((Integer) row.get("amt_order"));
		    }
		    if (row.get("tol_amt") != null) {
			sh.getRow(rowNumber).getCell(colNumber + 1)
				.setCellValue(row.get("tol_amt") instanceof BigDecimal ? ((BigDecimal) row.get("tol_amt")).doubleValue() : (Long) row.get("tol_amt"));
			sh.getRow(rowNumber).getCell(colNumber + 1).setCellStyle(getStyle5());
		    }
		    if (row.get("tol_num") != null) {
			sh.getRow(rowNumber).getCell(colNumber + 2).setCellValue((Integer) row.get("tol_num"));
		    }
		    if (row.get("amt_new") != null) {
			sh.getRow(rowNumber).getCell(colNumber + 3)
				.setCellValue(row.get("amt_new") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new")).doubleValue() : (Long) row.get("amt_new"));
			sh.getRow(rowNumber).getCell(colNumber + 3).setCellStyle(getStyle5());
		    }
		    if (row.get("subs_new") != null) {
			sh.getRow(rowNumber).getCell(colNumber + 4).setCellValue((Integer) row.get("subs_new"));
		    }
		    if (row.get("amt_new_per1") != null) {
		    	if (row.get("amt_new_per1").toString().equals("-9999.00")){
		    		sh.getRow(rowNumber).getCell(colNumber + 5).setCellValue("-");
		    	}else{
		    		sh.getRow(rowNumber).getCell(colNumber + 5)
					.setCellValue(row.get("amt_new_per1") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new_per1")).doubleValue() : (Long) row.get("amt_new_per1"));
		    		sh.getRow(rowNumber).getCell(colNumber + 5).setCellStyle(getStyle5());
		    	}
			}
		    if (row.get("amt_before") != null) {
			sh.getRow(rowNumber).getCell(colNumber + 6)
				.setCellValue(row.get("amt_before") instanceof BigDecimal ? ((BigDecimal) row.get("amt_before")).doubleValue() : (Long) row.get("amt_before"));
			sh.getRow(rowNumber).getCell(colNumber + 6).setCellStyle(getStyle5());
		    }
		    if (row.get("subs_before") != null) {
			sh.getRow(rowNumber).getCell(colNumber + 7).setCellValue((Integer) row.get("subs_before"));
		    }
		    if (row.get("amt_new_per") != null) {
				if (row.get("amt_new_per").toString().substring(0, 5).equals("-9999")) {
				    sh.getRow(rowNumber).getCell(colNumber + 8).setCellValue("NA");
				} else {
				    sh.getRow(rowNumber).getCell(colNumber + 8)
					.setCellValue(row.get("amt_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new_per")).doubleValue() : (Long) row.get("amt_new_per"));
				sh.getRow(rowNumber).getCell(colNumber + 8).setCellStyle(getStyle4());
				}
		    }
		    if (row.get("subs_new_per") != null) {
				if (row.get("subs_new_per").toString().substring(0, 5).equals("-9999")) {
				    sh.getRow(rowNumber).getCell(colNumber + 9).setCellValue("NA");
				} else {
				    sh.getRow(rowNumber).getCell(colNumber + 9)
					.setCellValue(row.get("subs_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_new_per")).doubleValue() : (Long) row.get("subs_new_per"));
				    sh.getRow(rowNumber).getCell(colNumber + 9).setCellStyle(getStyle4());
				}
		    }
		    if (row.get("amt_all_recover") != null) {
		    	if(row.get("amt_all_recover").toString().equals("-9999.00")){
		    		 sh.getRow(rowNumber).getCell(colNumber + 10).setCellValue("-");
		    	}else{
		    		sh.getRow(rowNumber).getCell(colNumber + 10)
					.setCellValue(row.get("amt_all_recover") instanceof BigDecimal ? ((BigDecimal) row.get("amt_all_recover")).doubleValue() : (Long) row.get("amt_all_recover"));
		    		sh.getRow(rowNumber).getCell(colNumber + 10).setCellStyle(getStyle5());
		    	}
				
			}
			if (row.get("sum_subs_recover") != null) {
				if(row.get("sum_subs_recover").toString().equals("-9999"))
		    		 sh.getRow(rowNumber).getCell(colNumber + 11).setCellValue("-");
				else
					sh.getRow(rowNumber).getCell(colNumber + 11).setCellValue((Integer) row.get("sum_subs_recover"));
			}
			 if (row.get("amt_recover_per") != null) {
				 if (row.get("amt_recover_per").toString().equals("-9999.00")) {
					    sh.getRow(rowNumber).getCell(colNumber + 12).setCellValue("-");
					} else {
					sh.getRow(rowNumber).getCell(colNumber + 12)
						.setCellValue(row.get("amt_recover_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_recover_per")).doubleValue() : (Long) row.get("amt_recover_per"));
					sh.getRow(rowNumber).getCell(colNumber + 12).setCellStyle(getStyle5());
					}
			 }
		    if (row.get("amt_other_var") != null) {
		    	if(row.get("amt_other_var").toString().equals("-9999.00")){
		    		sh.getRow(rowNumber).getCell(colNumber + 13).setCellValue("-");
		    	}else{
		    		sh.getRow(rowNumber).getCell(colNumber + 13)
					.setCellValue(row.get("amt_other_var") instanceof BigDecimal ? ((BigDecimal) row.get("amt_other_var")).doubleValue() : (Long) row.get("amt_other_var"));
		    		sh.getRow(rowNumber).getCell(colNumber + 13).setCellStyle(getStyle5());
		    	}
				
			}
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

    private CellStyle getStyle00() {
	CellStyle style = wb.createCellStyle();
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 16);
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
	font.setFontHeightInPoints((short) 11);
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