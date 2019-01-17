/**
 * com.hp.cmcc.job.service.NotiFileKhqf4000UserByTypeProcessor.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job;

import java.math.BigDecimal;
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

/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2018-2-28 下午2:28:03
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2018-2-28 	   hufei 	         1. Created this class.
 * </pre>
 */
@Service("NotiFileKhqf4000UserByTypeProcessor")
public class NotiFileKhqf4000UserByTypeProcessor extends AbstractNotiFileProcessor {

    private Map<String, CellStyle> mapStyle = new HashMap<String, CellStyle>();
    /*
     * (non-Javadoc)
     * 
     * @see com.hp.cmcc.job.service.AbstractNotiFileProcessor#generate()
     */
    protected String buildFileName() {
	fileName = fileName + ".xlsx";
	return fileName;
    }

    @Override
    public boolean generate() throws Exception {
	mapStyle.put("style0", getStyle0());
	mapStyle.put("style1", getStyle1());
	mapStyle.put("style2", getStyle2());
	mapStyle.put("style3", getStyle3());
	mapStyle.put("style4", getStyle4());
	mapStyle.put("style5", getStyle5());
	mapStyle.put("style6", getStyle6());
	mapStyle.put("style7", getStyle7());
	
	this.setFileName("个人客户长期高额欠费-按用户类型汇总-" + month);
	sh = wb.createSheet("个人客户长期高额欠费-按用户类型汇总");
	writeSheet1(notiFileGenService.selUserByType(month), sh);
	// TODO Auto-generated method stub
	return true;
    }
    public void writeSheet1(List<Map<String, Object>> data,Sheet sh) {
	sh.createRow(0);
	sh.getRow(0).createCell(0).setCellValue("个人客户长期高额欠费-按用户类型汇总");
	sh.getRow(0).getCell(0).setCellStyle(mapStyle.get("style0"));
	sh.createRow(1).createCell(0).setCellValue("31省公司个人用户长期高额欠费-按用户类型汇总");
	sh.getRow(1).getCell(0).setCellStyle(mapStyle.get("style7"));
	
	sh.createRow(2).createCell(0).setCellValue("统计截止月份");
	
	sh.createRow(3).createCell(0).setCellValue("欠费金额排名");
	sh.getRow(3).createCell(1).setCellValue("省份名称");
	sh.getRow(3).createCell(2).setCellValue("欠费金额（元）");
	sh.getRow(3).createCell(3).setCellValue("欠费用户数量（户）");
	sh.getRow(3).createCell(4).setCellValue("新增欠费用户欠费金额（元）");
	sh.getRow(3).createCell(5).setCellValue("新增欠费用户数量（户）");
	sh.getRow(3).createCell(6).setCellValue("红名单及免催免停用户欠费金额（元）");
	sh.getRow(3).createCell(7).setCellValue("红名单及免催免停用户欠费金额占比（%）");
	sh.getRow(3).createCell(8).setCellValue("欠费红名单及免催免停用户数量（户）");
	sh.getRow(3).createCell(9).setCellValue("欠费红名单及免催免停用户数量占比（%）");
	sh.getRow(3).createCell(10).setCellValue("新增欠费红名单及免催免停用户欠费金额（元）");
	sh.getRow(3).createCell(11).setCellValue("新增欠费红名单及免催免停用户欠费金额占新增欠费用户欠费金额的比例（%)");
	sh.getRow(3).createCell(12).setCellValue("新增欠费红名单及免催免停用户数量（户）");
	sh.getRow(3).createCell(13).setCellValue("新增欠费红名单及免催免停用户数量占新增欠费用户数量的比例（%）");
	sh.getRow(3).createCell(14).setCellValue("红名单及免催免停失效用户欠费金额（元）");
	sh.getRow(3).createCell(15).setCellValue("红名单及免催免停失效用户欠费金额占比（%）");
	sh.getRow(3).createCell(16).setCellValue("欠费红名单及免催免停失效用户数量（户）");
	sh.getRow(3).createCell(17).setCellValue("欠费红名单及免催免停失效用户数量占比（%）");
	sh.getRow(3).createCell(18).setCellValue("新增欠费红名单及免催免停失效用户欠费金额（元）");
	sh.getRow(3).createCell(19).setCellValue("新增欠费红名单及免催免停失效用户欠费金额占新增欠费用户欠费金额的比例（%）");
	sh.getRow(3).createCell(20).setCellValue("新增欠费红名单及免催免停失效用户数量（户）");
	sh.getRow(3).createCell(21).setCellValue("新增欠费红名单及免催免停失效用户数量占新增欠费用户数量的比例（%）");
	sh.getRow(3).createCell(22).setCellValue("非数据SIM卡及M2M用户欠费金额（元）");
	sh.getRow(3).createCell(23).setCellValue("非数据SIM卡及M2M用户欠费金额占比（%）");
	sh.getRow(3).createCell(24).setCellValue("欠费非数据SIM卡及M2M用户数量（户）");
	sh.getRow(3).createCell(25).setCellValue("欠费非数据SIM卡及M2M用户数量占比（%）");
	sh.getRow(3).createCell(26).setCellValue("新增欠费非数据SIM卡及M2M用户欠费金额（元）");
	sh.getRow(3).createCell(27).setCellValue("新增欠费非数据SIM卡及M2M用户欠费金额占新增欠费用户欠费金额的比例（%）");
	sh.getRow(3).createCell(28).setCellValue("新增欠费非数据SIM卡及M2M用户数量（户）");
	sh.getRow(3).createCell(29).setCellValue("新增欠费非数据SIM卡及M2M用户数量占新增欠费用户数量的比例（%）");
	sh.getRow(3).createCell(30).setCellValue("数据SIM卡用户（含2G、3G和4G数据卡）欠费金额（元）");
	sh.getRow(3).createCell(31).setCellValue("数据SIM卡用户（含2G、3G和4G数据卡）欠费金额占比（%）");
	sh.getRow(3).createCell(32).setCellValue("欠费数据SIM卡用户（含2G、3G和4G数据卡）数量（户）");
	sh.getRow(3).createCell(33).setCellValue("欠费数据SIM卡用户（含2G、3G和4G数据卡）数量占比（%）");
	sh.getRow(3).createCell(34).setCellValue("新增欠费数据SIM卡用户（含2G、3G和4G数据卡）欠费金额（元）");
	sh.getRow(3).createCell(35).setCellValue("新增欠费数据SIM卡用户（含2G、3G和4G数据卡）欠费金额占新增欠费用户欠费金额的比例（%）");
	sh.getRow(3).createCell(36).setCellValue("新增欠费数据SIM卡用户（含2G、3G和4G数据卡）数量（户）");
	sh.getRow(3).createCell(37).setCellValue("新增欠费数据SIM卡用户（含2G、3G和4G数据卡）数量占新增欠费用户数量的比例（%）");
	sh.getRow(3).createCell(38).setCellValue("M2M业务用户欠费金额（元）");
	sh.getRow(3).createCell(39).setCellValue("M2M业务用户欠费金额占比（%）");
	sh.getRow(3).createCell(40).setCellValue("欠费M2M业务用户数量（户）");
	sh.getRow(3).createCell(41).setCellValue("欠费M2M业务用户数量占比（%）");
	sh.getRow(3).createCell(42).setCellValue("新增欠费M2M业务用户欠费金额（元）");
	sh.getRow(3).createCell(43).setCellValue("新增欠费M2M业务用户欠费金额占新增欠费用户欠费金额的比例（%）");
	sh.getRow(3).createCell(44).setCellValue("新增欠费M2M业务用户数量（户）");
	sh.getRow(3).createCell(45).setCellValue("新增欠费M2M业务用户数量占新增欠费用户数量的比例（%）");
	sh.getRow(3).createCell(46).setCellValue("物联网用户欠费金额（元）");
	sh.getRow(3).createCell(47).setCellValue("物联网用户欠费金额占比（%）");
	sh.getRow(3).createCell(48).setCellValue("欠费物联网用户数量（户）");
	sh.getRow(3).createCell(49).setCellValue("欠费物联网用户数量占比（%）");
	sh.getRow(3).createCell(50).setCellValue("新增欠费物联网用户欠费金额（元）");
	sh.getRow(3).createCell(51).setCellValue("新增欠费物联网用户欠费金额占新增欠费用户欠费金额的比例（%）");
	sh.getRow(3).createCell(52).setCellValue("新增欠费物联网用户数量（户）");
	sh.getRow(3).createCell(53).setCellValue("新增欠费物联网用户数量占新增欠费用户数量的比例（%）");
	sh.getRow(2).getCell(0).setCellStyle(mapStyle.get("style1"));
	sh.getRow(3).getCell(0).setCellStyle(mapStyle.get("style1"));
	for(int m=4;m<=34;m++){
	    sh.createRow(m).createCell(0).setCellStyle(mapStyle.get("style6"));
	}
	for (int i = 1; i <= 53; i++) {
	    sh.getRow(1).createCell(i).setCellStyle(mapStyle.get("style7"));
	    sh.getRow(2).createCell(i).setCellStyle(mapStyle.get("style1"));
	    sh.getRow(3).getCell(i).setCellStyle(mapStyle.get("style1"));
	    for(int n=4;n<=34;n++){
		    sh.getRow(n).createCell(i).setCellStyle(mapStyle.get("style6"));
		}
	}
	
	Integer thisMonth = Integer.parseInt("0".equals(month.substring(4, 5)) ?month.substring(5, 6) : month.substring(4, 6));
	sh.getRow(2).getCell(1).setCellValue(month.substring(0, 4) + "年" + thisMonth + "月");
	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 20));
	sh.addMergedRegion(new CellRangeAddress(1, 1, 0, 53));
	sh.addMergedRegion(new CellRangeAddress(2, 2, 1, 53));
	for (int i = 0; i < 53; i++) {
	    sh.setColumnWidth(i, 256 * 14);
	}

	// ！后续根据后台提供数据及表字段修改，总部与省公司的数据字段不完全相同
	Map<String, Object> row = new HashMap<String, Object>();
	for (int i = 0, dataSize = data.size(); i < dataSize; i++) {
	    row = data.get(i);
	    // 插入单元格数据
	    if (row.size() != 0) {
		if (row.get("rank_amt") != null) {
		    sh.getRow(i + 4).getCell(0).setCellValue((Integer) row.get("rank_amt"));
		} else {
		    sh.getRow(i + 4).getCell(0).setCellValue("-");
		}
		if (row.get("prvd_nm") != null) {
		    sh.getRow(i + 4).getCell(1).setCellValue(row.get("prvd_nm").toString());
		} else {
		    sh.getRow(i + 4).getCell(1).setCellValue("-");
		}
		if (row.get("sum_dbt_amt") != null) {
		    sh.getRow(i + 4).getCell(2).setCellValue(row.get("sum_dbt_amt") instanceof BigDecimal ? ((BigDecimal) row.get("sum_dbt_amt")).doubleValue() : (Long) row.get("sum_dbt_amt"));
		} else {
		    sh.getRow(i + 4).getCell(2).setCellValue("-");
		}
		if (row.get("sum_cust") != null) {
		    sh.getRow(i + 4).getCell(3).setCellValue((Integer) row.get("sum_cust"));
		}  else {
		    sh.getRow(i + 4).getCell(3).setCellValue("-");
		}
		if (row.get("amt_new") != null) {
		    sh.getRow(i + 4).getCell(4).setCellValue(row.get("amt_new") instanceof BigDecimal ? ((BigDecimal) row.get("amt_new")).doubleValue() : (Long) row.get("amt_new"));
		} else {
		    sh.getRow(i + 4).getCell(4).setCellValue("-");
		}
		if (row.get("subs_new") != null) {
		    sh.getRow(i + 4).getCell(5).setCellValue((Integer) row.get("subs_new"));
		} else {
		    sh.getRow(i + 4).getCell(5).setCellValue("-");
		}
		if (row.get("amt_red") != null) {
		    sh.getRow(i + 4).getCell(6)
			    .setCellValue(row.get("amt_red") instanceof BigDecimal ? ((BigDecimal) row.get("amt_red")).doubleValue() : (Long) row.get("amt_red"));
		} else {
		    sh.getRow(i + 4).getCell(6).setCellValue("-");
		}
		if (row.get("amt_red_per") != null) {
		    sh.getRow(i + 4).getCell(7).setCellValue(row.get("amt_red_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_red_per")).doubleValue() : (Long) row.get("amt_red_per"));
		} else {
		    sh.getRow(i + 4).getCell(7).setCellValue("-");
		}
		if (row.get("subs_red") != null) {
		    sh.getRow(i + 4).getCell(8).setCellValue((Integer) row.get("subs_red"));
		} else {
		    sh.getRow(i + 4).getCell(8).setCellValue("-");
		}
		if (row.get("subs_red_per") != null) {
		    sh.getRow(i + 4).getCell(9).setCellValue(row.get("subs_red_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_red_per")).doubleValue() : (Long) row.get("subs_red_per"));
		} else {
		    sh.getRow(i + 4).getCell(9).setCellValue("-");
		}
		if (row.get("amt_red_new") != null) {
		    sh.getRow(i + 4).getCell(10).setCellValue(row.get("amt_red_new") instanceof BigDecimal ? ((BigDecimal) row.get("amt_red_new")).doubleValue() : (Long) row.get("amt_red_new"));
		} else {
		    sh.getRow(i + 4).getCell(10).setCellValue("-");
		}
		if (row.get("amt_red_new_per") != null) {
		    sh.getRow(i + 4).getCell(11)
			    .setCellValue(row.get("amt_red_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_red_new_per")).doubleValue() : (Long) row.get("amt_red_new_per"));
		} else {
		    sh.getRow(i + 4).getCell(11).setCellValue("-");
		}
		if (row.get("subs_red_new") != null) {
		    sh.getRow(i + 4).getCell(12).setCellValue((Integer) row.get("subs_red_new"));
		} else {
		    sh.getRow(i + 4).getCell(12).setCellValue("-");
		}
		if (row.get("subs_red_new_per") != null) {
		    sh.getRow(i + 4).getCell(13).setCellValue(row.get("subs_red_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_red_new_per")).doubleValue() : (Long) row.get("subs_red_new_per"));
		} else {
		    sh.getRow(i + 4).getCell(13).setCellValue("-");
		}
		if (row.get("amt_red_expired") != null) {
		    sh.getRow(i + 4).getCell(14).setCellValue(row.get("amt_red_expired") instanceof BigDecimal ? ((BigDecimal) row.get("amt_red_expired")).doubleValue() : (Long) row.get("amt_red_expired"));
		} else {
		    sh.getRow(i + 4).getCell(14).setCellValue("-");
		}
		if (row.get("amt_red_expired_per") != null) {
		    sh.getRow(i + 4).getCell(15).setCellValue(row.get("amt_red_expired_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_red_expired_per")).doubleValue() : (Long) row.get("amt_red_expired_per"));
		} else {
		    sh.getRow(i + 4).getCell(15).setCellValue("-");
		}
		if (row.get("subs_red_expired") != null) {
		    sh.getRow(i + 4).getCell(16).setCellValue((Integer) row.get("subs_red_expired"));
		} else {
		    sh.getRow(i + 4).getCell(16).setCellValue("-");
		}
		if (row.get("subs_red_expired_per") != null) {
		    sh.getRow(i + 4).getCell(17).setCellValue(row.get("subs_red_expired_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_red_expired_per")).doubleValue() : (Long) row.get("subs_red_expired_per"));
		} else {
		    sh.getRow(i + 4).getCell(17).setCellValue("-");
		}
		if (row.get("amt_red_expired_new") != null) {
		    sh.getRow(i + 4).getCell(18).setCellValue(row.get("amt_red_expired_new") instanceof BigDecimal ? ((BigDecimal) row.get("amt_red_expired_new")).doubleValue() : (Long) row.get("amt_red_expired_new"));
		} else {
		    sh.getRow(i + 4).getCell(18).setCellValue("-");
		}
		if (row.get("amt_red_expired_new_per") != null) {
		    sh.getRow(i + 4).getCell(19).setCellValue(row.get("amt_red_expired_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_red_expired_new_per")).doubleValue() : (Long) row.get("amt_red_expired_new_per"));
		} else {
		    sh.getRow(i + 4).getCell(19).setCellValue("-");
		}
		if (row.get("subs_red_expired_new") != null) {
		    sh.getRow(i + 4).getCell(20).setCellValue((Integer) row.get("subs_red_expired_new"));
		} else {
		    sh.getRow(i + 4).getCell(20).setCellValue("-");
		}
		if (row.get("subs_red_expired_new_per") != null) {
		    sh.getRow(i + 4).getCell(21).setCellValue(row.get("subs_red_expired_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_red_expired_new_per")).doubleValue() : (Long) row.get("subs_red_expired_new_per"));
		} else {
		    sh.getRow(i + 4).getCell(21).setCellValue("-");
		}
		
		
		if (row.get("amt_subs_non_sim_m2m") != null) {
		    sh.getRow(i + 4).getCell(22).setCellValue(row.get("amt_subs_non_sim_m2m") instanceof BigDecimal ? ((BigDecimal) row.get("amt_subs_non_sim_m2m")).doubleValue() : (Long) row.get("amt_subs_non_sim_m2m"));
		} else {
		    sh.getRow(i + 4).getCell(22).setCellValue("-");
		}
		if (row.get("amt_subs_non_sim_m2m_per") != null) {
		    sh.getRow(i + 4).getCell(23).setCellValue(row.get("amt_subs_non_sim_m2m_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_subs_non_sim_m2m_per")).doubleValue() : (Long) row.get("amt_subs_non_sim_m2m_per"));
		}  else {
		    sh.getRow(i + 4).getCell(23).setCellValue("-");
		}
		if (row.get("subs_non_sim_m2m") != null) {
		    sh.getRow(i + 4).getCell(24).setCellValue((Integer)row.get("subs_non_sim_m2m"));
		} else {
		    sh.getRow(i + 4).getCell(24).setCellValue("-");
		}
		if (row.get("subs_non_sim_m2m_per") != null) {
		    sh.getRow(i + 4).getCell(25).setCellValue(row.get("subs_non_sim_m2m_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_non_sim_m2m_per")).doubleValue() : (Long) row.get("subs_non_sim_m2m_per"));
		} else {
		    sh.getRow(i + 4).getCell(25).setCellValue("-");
		}
		if (row.get("amt_subs_non_sim_m2m_new") != null) {
		    sh.getRow(i + 4).getCell(26)
			    .setCellValue(row.get("amt_subs_non_sim_m2m_new") instanceof BigDecimal ? ((BigDecimal) row.get("amt_subs_non_sim_m2m_new")).doubleValue() : (Long) row.get("amt_subs_non_sim_m2m_new"));
		} else {
		    sh.getRow(i + 4).getCell(26).setCellValue("-");
		}
		if (row.get("amt_subs_non_sim_m2m_new_per") != null) {
		    sh.getRow(i + 4).getCell(27).setCellValue(row.get("amt_red_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_red_per")).doubleValue() : (Long) row.get("amt_red_per"));
		} else {
		    sh.getRow(i + 4).getCell(27).setCellValue("-");
		}
		if (row.get("subs_non_sim_m2m_new") != null) {
		    sh.getRow(i + 4).getCell(28).setCellValue((Integer) row.get("subs_non_sim_m2m_new"));
		} else {
		    sh.getRow(i + 4).getCell(28).setCellValue("-");
		}
		if (row.get("subs_non_sim_m2m_new_per") != null) {
		    sh.getRow(i + 4).getCell(29).setCellValue(row.get("subs_non_sim_m2m_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_non_sim_m2m_new_per")).doubleValue() : (Long) row.get("subs_non_sim_m2m_new_per"));
		} else {
		    sh.getRow(i + 4).getCell(29).setCellValue("-");
		}
		if (row.get("amt_subs_sim") != null) {
		    sh.getRow(i + 4).getCell(30).setCellValue(row.get("amt_subs_sim") instanceof BigDecimal ? ((BigDecimal) row.get("amt_subs_sim")).doubleValue() : (Long) row.get("amt_subs_sim"));
		} else {
		    sh.getRow(i + 4).getCell(30).setCellValue("-");
		}
		if (row.get("amt_subs_sim_per") != null) {
		    sh.getRow(i + 4).getCell(31)
			    .setCellValue(row.get("amt_subs_sim_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_subs_sim_per")).doubleValue() : (Long) row.get("amt_subs_sim_per"));
		} else {
		    sh.getRow(i + 4).getCell(31).setCellValue("-");
		}
		if (row.get("subs_sim") != null) {
		    sh.getRow(i + 4).getCell(32).setCellValue((Integer) row.get("subs_sim"));
		} else {
		    sh.getRow(i + 4).getCell(32).setCellValue("-");
		}
		if (row.get("subs_sim_per") != null) {
		    sh.getRow(i + 4).getCell(33).setCellValue(row.get("subs_sim_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_sim_per")).doubleValue() : (Long) row.get("subs_sim_per"));
		} else {
		    sh.getRow(i + 4).getCell(33).setCellValue("-");
		}
		if (row.get("amt_subs_sim_new") != null) {
		    sh.getRow(i + 4).getCell(34).setCellValue(row.get("amt_subs_sim_new") instanceof BigDecimal ? ((BigDecimal) row.get("amt_subs_sim_new")).doubleValue() : (Long) row.get("amt_subs_sim_new"));
		} else {
		    sh.getRow(i + 4).getCell(34).setCellValue("-");
		}
		if (row.get("amt_subs_sim_new_per") != null) {
		    sh.getRow(i + 4).getCell(35).setCellValue(row.get("amt_subs_sim_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_subs_sim_new_per")).doubleValue() : (Long) row.get("amt_subs_sim_new_per"));
		} else {
		    sh.getRow(i + 4).getCell(35).setCellValue("-");
		}
		if (row.get("subs_sim_new") != null) {
		    sh.getRow(i + 4).getCell(36).setCellValue((Integer) row.get("subs_sim_new"));
		} else {
		    sh.getRow(i + 4).getCell(36).setCellValue("-");
		}
		if (row.get("subs_sim_new_per") != null) {
		    sh.getRow(i + 4).getCell(37).setCellValue(row.get("subs_sim_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_sim_new_per")).doubleValue() : (Long) row.get("subs_sim_new_per"));
		} else {
		    sh.getRow(i + 4).getCell(37).setCellValue("-");
		}
		if (row.get("amt_subs_m2m") != null) {
		    sh.getRow(i + 4).getCell(38).setCellValue(row.get("amt_subs_m2m") instanceof BigDecimal ? ((BigDecimal) row.get("amt_subs_m2m")).doubleValue() : (Long) row.get("amt_subs_m2m"));
		} else {
		    sh.getRow(i + 4).getCell(38).setCellValue("-");
		}
		if (row.get("amt_subs_m2m_per") != null) {
		    sh.getRow(i + 4).getCell(39).setCellValue(row.get("amt_subs_m2m_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_subs_m2m_per")).doubleValue() : (Long) row.get("amt_subs_m2m_per"));
		} else {
		    sh.getRow(i + 4).getCell(39).setCellValue("-");
		}
		if (row.get("subs_m2m") != null) {
		    sh.getRow(i + 4).getCell(40).setCellValue((Integer) row.get("subs_m2m"));
		} else {
		    sh.getRow(i + 4).getCell(40).setCellValue("-");
		}
		if (row.get("subs_m2m_per") != null) {
		    sh.getRow(i + 4).getCell(41).setCellValue(row.get("subs_m2m_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_m2m_per")).doubleValue() : (Long) row.get("subs_m2m_per"));
		} else {
		    sh.getRow(i + 4).getCell(41).setCellValue("-");
		}

		if (row.get("amt_subs_m2m_new") != null) {
		    sh.getRow(i + 4).getCell(42).setCellValue(row.get("amt_subs_m2m_new") instanceof BigDecimal ? ((BigDecimal) row.get("amt_subs_m2m_new")).doubleValue() : (Long) row.get("amt_subs_m2m_new"));
		} else {
		    sh.getRow(i + 4).getCell(42).setCellValue("-");
		}
		if (row.get("amt_subs_m2m_new_per") != null) {
		    sh.getRow(i + 4).getCell(43).setCellValue(row.get("amt_subs_m2m_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_subs_m2m_new_per")).doubleValue() : (Long) row.get("amt_subs_m2m_new_per"));
		}  else {
		    sh.getRow(i + 4).getCell(43).setCellValue("-");
		}
		if (row.get("subs_m2m_new") != null) {
		    sh.getRow(i + 4).getCell(44).setCellValue((Integer) row.get("subs_m2m_new"));
		} else {
		    sh.getRow(i + 4).getCell(44).setCellValue("-");
		}
		if (row.get("subs_m2m_new_per") != null) {
		    sh.getRow(i + 4).getCell(45).setCellValue(row.get("subs_m2m_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_m2m_new_per")).doubleValue() : (Long) row.get("subs_m2m_new_per"));
		} else {
		    sh.getRow(i + 4).getCell(45).setCellValue("-");
		}
		if (row.get("amt_subs_iot") != null) {
		    sh.getRow(i + 4).getCell(46)
			    .setCellValue(row.get("amt_subs_iot") instanceof BigDecimal ? ((BigDecimal) row.get("amt_subs_iot")).doubleValue() : (Long) row.get("amt_subs_iot"));
		} else {
		    sh.getRow(i + 4).getCell(46).setCellValue("-");
		}
		if (row.get("amt_subs_iot_per") != null) {
		    sh.getRow(i + 4).getCell(47).setCellValue(row.get("amt_subs_iot_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_subs_iot_per")).doubleValue() : (Long) row.get("amt_subs_iot_per"));
		} else {
		    sh.getRow(i + 4).getCell(47).setCellValue("-");
		}
		if (row.get("subs_iot") != null) {
		    sh.getRow(i + 4).getCell(48).setCellValue((Integer) row.get("subs_iot"));
		} else {
		    sh.getRow(i + 4).getCell(48).setCellValue("-");
		}
		if (row.get("subs_iot_per") != null) {
		    sh.getRow(i + 4).getCell(49).setCellValue(row.get("subs_iot_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_iot_per")).doubleValue() : (Long) row.get("subs_iot_per"));
		} else {
		    sh.getRow(i + 4).getCell(49).setCellValue("-");
		}
		if (row.get("amt_subs_iot_new") != null) {
		    sh.getRow(i + 4).getCell(50).setCellValue(row.get("amt_subs_iot_new") instanceof BigDecimal ? ((BigDecimal) row.get("amt_subs_iot_new")).doubleValue() : (Long) row.get("amt_subs_iot_new"));
		} else {
		    sh.getRow(i + 4).getCell(50).setCellValue("-");
		}
		if (row.get("amt_subs_iot_new_per") != null) {
		    sh.getRow(i + 4).getCell(51)
			    .setCellValue(row.get("amt_subs_iot_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_subs_iot_new_per")).doubleValue() : (Long) row.get("amt_subs_iot_new_per"));
		} else {
		    sh.getRow(i + 4).getCell(51).setCellValue("-");
		}
		if (row.get("subs_iot_new") != null) {
		    sh.getRow(i + 4).getCell(52).setCellValue((Integer) row.get("subs_iot_new"));
		} else {
		    sh.getRow(i + 4).getCell(52).setCellValue("-");
		}
		if (row.get("subs_iot_new_per") != null) {
		    sh.getRow(i + 4).getCell(53).setCellValue(row.get("subs_iot_new_per") instanceof BigDecimal ? ((BigDecimal) row.get("subs_iot_new_per")).doubleValue() : (Long) row.get("subs_iot_new_per"));
		} else {
		    sh.getRow(i + 4).getCell(53).setCellValue("-");
		}
		
		
		// 小数点
		sh.getRow(i + 4).getCell(2).setCellStyle(mapStyle.get("style5"));
		sh.getRow(i + 4).getCell(4).setCellStyle(mapStyle.get("style5"));
		for (int n = 6; n < 53; n = n + 4) {
		    sh.getRow(i + 4).getCell(n).setCellStyle(mapStyle.get("style5"));
		}
		// 占比
		for (int n = 7; n <=53; n = n + 2) {
		    sh.getRow(i + 4).getCell(n).setCellStyle(mapStyle.get("style4"));
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
	font.setFontHeightInPoints((short) 16);
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
    private CellStyle getStyle7() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_LEFT);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 14);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style.setFont(font);
	return style;
    }

}
