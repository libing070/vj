/**
 * com.hpe.cmca.job.DataQualityAuditProcessor.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.finals.StringUtils;
import com.hpe.cmca.service.SJZLService;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;

/**
 * <pre>
 * Desc： 
 * @author Administrator
 * @refactor Administrator
 * @date   2016-12-9 上午11:00:09
 * @version 1.0 
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2016-12-9 	   Administrator 	         1. Created this class. 
 * </pre>
 */
@Service
public class SJZLProcessor extends BaseObject {

	public List<String>		auditNmList	= new ArrayList<String>();
	protected XSSFWorkbook	wb;
	protected Sheet			sh;
	@Autowired
	private SJZLService		sjzlService;

	public void work() {

		 for (String audit_nm : auditNmList) {
		 for (Map<String, Object> audTrmMap : sjzlService.getAudTrmByNm(audit_nm)) {
		 String aud_trm = (String) audTrmMap.get("Aud_trm");
		//String aud_trm = "201604";
		//String audit_nm = "QDYK_07";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> monMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(aud_trm)) {
			paramMap.put("cycleDate", aud_trm);
			/* 取得审计周期最近月 */
			int month = Integer.parseInt(aud_trm.substring(4, 6));
			monMap.put("month", month);
			if (month - 1 > 0) {
				monMap.put("pmonth", month - 1);
			} else if (month - 1 == 0) {
				monMap.put("pmonth", 12);
			}
			if (month - 2 > 0) {
				monMap.put("ppmonth", month - 2);
			} else if (month - 2 == 0) {
				monMap.put("ppmonth", 12);
			} else if (month - 1 == 0) {
				monMap.put("ppmonth", 11);
			}
			if (month - 3 > 0) {
				monMap.put("pppmonth", month - 3);
			} else if (month - 3 == 0) {
				monMap.put("pppmonth", 12);
			} else if (month - 2 == 0) {
				monMap.put("pppmonth", 11);
			} else if (month - 1 == 0) {
				monMap.put("pppmonth", 10);
			}
			if (month - 4 > 0) {
				monMap.put("ppppmonth", month - 4);
			} else if (month - 4 == 0) {
				monMap.put("ppppmonth", 12);
			} else if (month - 3 == 0) {
				monMap.put("ppppmonth", 11);
			} else if (month - 2 == 0) {
				monMap.put("ppppmonth", 10);
			} else if (month - 1 == 0) {
				monMap.put("ppppmonth", 9);
			}
			monMap.put("aud_trm", aud_trm);
		}
		try {
			/**
			 * 根据审计稽核点选择查询的数据,调用相应的方法生成excel
			 */
			// request.setCharacterEncoding("utf-8");
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> ykWholeList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> ykWholeListTol = new ArrayList<Map<String, Object>>();
			Map<String, List<Map<String, Object>>> resultMap = new HashMap<String, List<Map<String, Object>>>();

			if ("QDYK_02".equals(audit_nm)) {
				paramMap.put("auditPoint", "用户类型异常变更");
				paramMap.put("err_type_cd", "subs_002");
				ykWholeList = sjzlService.getYKWhole(paramMap);
				resultList = sjzlService.getYK12(paramMap);
				ykWholeListTol = sjzlService.getYKAll(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeQdyk02(resultList, ykWholeList, monMap,wb,false);
				generate(wb, "用户类型异常变更" + monMap.get("aud_trm") + ".xlsx",aud_trm,"2000",audit_nm,false);
				//生成专题表格的一个sheet
				XSSFWorkbook wbQdyk = openExcel("渠道养卡违规" + monMap.get("aud_trm")+ ".xlsx","用户类型异常变更");	
				
				int sheetIndex_0 = wbQdyk.getSheetIndex("用户历史信息表02004整体的数据情况");
				if(sheetIndex_0<0)
					writeYk(ykWholeListTol,monMap,wbQdyk,"用户历史信息表02004整体的数据情况");
				
				int sheetIndex = wbQdyk.getSheetIndex("用户类型异常变更");
				if(sheetIndex>=0)wbQdyk.removeSheetAt(sheetIndex);				 
				writeQdyk02(resultList, ykWholeList, monMap,wbQdyk,true);
				generate(wbQdyk, "渠道养卡违规" + monMap.get("aud_trm")+ ".xlsx",aud_trm,"2000",audit_nm,true);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "用户类型异常变更" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "渠道养卡违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("QDYK_03".equals(audit_nm)) {
				paramMap.put("auditPoint", "入网渠道异常变更");
				paramMap.put("err_type_cd", "subs_001");
				ykWholeList = sjzlService.getYKWhole(paramMap);
				resultList = sjzlService.getYK13(paramMap);
				ykWholeListTol = sjzlService.getYKAll(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeQdyk03(resultList, ykWholeList, monMap,wb,false);
				generate(wb, "入网渠道异常变更" + monMap.get("aud_trm") + ".xlsx",aud_trm,"2000",audit_nm,false);
				//生成专题表格的一个sheet
				XSSFWorkbook wbQdyk = openExcel("渠道养卡违规" + monMap.get("aud_trm")+ ".xlsx","入网渠道异常变更");	
				
				int sheetIndex_0 = wbQdyk.getSheetIndex("用户历史信息表02004整体的数据情况");
				if(sheetIndex_0<0)
					writeYk(ykWholeListTol,monMap,wbQdyk,"用户历史信息表02004整体的数据情况");
				
				int sheetIndex = wbQdyk.getSheetIndex("入网渠道异常变更");
				if(sheetIndex>=0)wbQdyk.removeSheetAt(sheetIndex);				 
				writeQdyk03(resultList, ykWholeList, monMap,wbQdyk,true);
				generate(wbQdyk, "渠道养卡违规" + monMap.get("aud_trm")+ ".xlsx",aud_trm,"2000",audit_nm,true);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "入网渠道异常变更" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "渠道养卡违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("QDYK_04".equals(audit_nm)) {
				paramMap.put("auditPoint", "入网时间异常变更");
				paramMap.put("err_type_cd", "subs_004");
				ykWholeList = sjzlService.getYKWhole(paramMap);
				resultList = sjzlService.getYK14(paramMap);
				ykWholeListTol = sjzlService.getYKAll(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeQdyk04(resultList, ykWholeList, monMap,wb,false);
				generate(wb, "入网时间异常变更" + monMap.get("aud_trm") + ".xlsx",aud_trm,"2000",audit_nm,false);
				//生成专题表格的一个sheet
				XSSFWorkbook wbQdyk = openExcel("渠道养卡违规" + monMap.get("aud_trm")+ ".xlsx","入网时间异常变更");	
				
				int sheetIndex_0 = wbQdyk.getSheetIndex("用户历史信息表02004整体的数据情况");
				if(sheetIndex_0<0)
					writeYk(ykWholeListTol,monMap,wbQdyk,"用户历史信息表02004整体的数据情况");
				
				int sheetIndex = wbQdyk.getSheetIndex("入网时间异常变更");
				if(sheetIndex>=0)wbQdyk.removeSheetAt(sheetIndex);				 
				writeQdyk04(resultList, ykWholeList, monMap,wbQdyk,true);
				generate(wbQdyk, "渠道养卡违规" + monMap.get("aud_trm")+ ".xlsx",aud_trm,"2000",audit_nm,true);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "入网时间异常变更" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "渠道养卡违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("QDYK_05".equals(audit_nm)) {
				paramMap.put("auditPoint", "数据SIM卡用户标志的统计情况");
				paramMap.put("err_type_cd", "subs_003");
				ykWholeList = sjzlService.getYKWhole(paramMap);
				resultList = sjzlService.getYK15(paramMap);
				ykWholeListTol = sjzlService.getYKAll(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeQdyk05(resultList, ykWholeList, monMap,wb,false);
				generate(wb, "数据SIM卡用户标志的统计情况" + monMap.get("aud_trm") + ".xlsx",aud_trm,"2000",audit_nm,false);
				//生成专题表格的一个sheet
				XSSFWorkbook wbQdyk = openExcel("渠道养卡违规" + monMap.get("aud_trm")+ ".xlsx","数据SIM卡用户标志的统计情况");	
				
				int sheetIndex_0 = wbQdyk.getSheetIndex("用户历史信息表02004整体的数据情况");
				if(sheetIndex_0<0)
					writeYk(ykWholeListTol,monMap,wbQdyk,"用户历史信息表02004整体的数据情况");
				
				int sheetIndex = wbQdyk.getSheetIndex("数据SIM卡用户标志的统计情况");
				if(sheetIndex>=0)wbQdyk.removeSheetAt(sheetIndex);				 
				writeQdyk05(resultList, ykWholeList, monMap,wbQdyk,true);
				generate(wbQdyk, "渠道养卡违规" + monMap.get("aud_trm")+ ".xlsx",aud_trm,"2000",audit_nm,true);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "数据SIM卡用户标志的统计情况" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "渠道养卡违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("QDYK_06".equals(audit_nm)) {
				paramMap.put("auditPoint", "入网渠道相同但是归属地市不同");
				paramMap.put("err_type_cd", "subs_005");
				ykWholeList = sjzlService.getYKWhole(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeQdyk06(ykWholeList, monMap,wb,false);
				generate(wb, "入网渠道相同但是归属地市不同" + monMap.get("aud_trm") + ".xlsx",aud_trm,"2000",audit_nm,false);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "入网渠道相同但是归属地市不同" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "渠道养卡违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("QDYK_07".equals(audit_nm)) {
				paramMap.put("auditPoint", "入网用户接口数据延迟入库");
				paramMap.put("err_type_cd", "subs_006");
				ykWholeList = sjzlService.getYKWhole(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeQdyk07(ykWholeList, monMap,wb,false);
				generate(wb, "入网用户接口数据延迟入库" + monMap.get("aud_trm") + ".xlsx",aud_trm,"2000",audit_nm,false);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "入网用户接口数据延迟入库" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "渠道养卡违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("YJK_01".equals(audit_nm)) {
				// paramMap.put("auditPoint", "有价卡违规（06049入库情况）");
				resultList = sjzlService.getYJK01(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeYjk01(resultList, monMap,wb);
				generate(wb, "有价卡违规（06049有价卡状态变更记录(VC)入库情况）" + monMap.get("aud_trm") + ".xlsx",aud_trm,"1000",audit_nm,false);
				//生成专题表格的一个sheet
				XSSFWorkbook wbYjk = openExcel("有价卡违规" + monMap.get("aud_trm")+ ".xlsx","06049有价卡状态变更记录(VC)入库情况");	
				int sheetIndex = wbYjk.getSheetIndex("06049有价卡状态变更记录(VC)入库情况");
				if(sheetIndex>=0)wbYjk.removeSheetAt(sheetIndex);				 
				writeYjk01(resultList, monMap,wbYjk);
				generate(wbYjk, "有价卡违规" + monMap.get("aud_trm")+ ".xlsx",aud_trm,"1000",audit_nm,true);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "有价卡违规（06049有价卡状态变更记录(VC)入库情况）" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "有价卡违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("YJK_02".equals(audit_nm)) {
				// paramMap.put("auditPoint", "有价卡违规（06045入库情况）");
				resultList = sjzlService.getYJK02(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeYjk02(resultList, monMap,wb);
				generate(wb, "有价卡违规（06045有价卡状态变更记录(CRM)入库情况）" + monMap.get("aud_trm") + ".xlsx",aud_trm,"1000",audit_nm,false);
				//生成专题表格的一个sheet
				XSSFWorkbook wbYjk = openExcel("有价卡违规" + monMap.get("aud_trm")+ ".xlsx","06045有价卡状态变更记录(CRM)入库情况");	
				int sheetIndex = wbYjk.getSheetIndex("06045有价卡状态变更记录(CRM)入库情况");
				if(sheetIndex>=0)wbYjk.removeSheetAt(sheetIndex);				 
				writeYjk02(resultList, monMap,wbYjk);
				generate(wbYjk, "有价卡违规" + monMap.get("aud_trm")+ ".xlsx",aud_trm,"1000",audit_nm,true);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "有价卡违规（06045有价卡状态变更记录(CRM)入库情况）" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "有价卡违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("YJK_03".equals(audit_nm)) {
				// paramMap.put("auditPoint", "有价卡违规（06049和06045关联比对）");
				resultList = sjzlService.getYJK03(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeYjk03(resultList, monMap,wb);
				generate(wb, "有价卡违规（06049有价卡状态变更记录(VC)和06045有价卡状态变更记录(CRM)关联比对）" + monMap.get("aud_trm") + ".xlsx",aud_trm,"1000",audit_nm,false);
				//生成专题表格的一个sheet
				XSSFWorkbook wbYjk = openExcel("有价卡违规" + monMap.get("aud_trm")+ ".xlsx","06049(VC)与06045(CRM)关联比对");	
				int sheetIndex = wbYjk.getSheetIndex("06049(VC)与06045(CRM)关联比对");
				if(sheetIndex>=0)wbYjk.removeSheetAt(sheetIndex);				 
				writeYjk03(resultList, monMap,wbYjk);
				generate(wbYjk, "有价卡违规" + monMap.get("aud_trm")+ ".xlsx",aud_trm,"1000",audit_nm,true);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "有价卡违规（06049有价卡状态变更记录(VC)和06045有价卡状态变更记录(CRM)关联比对）" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "有价卡违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("YJK_04".equals(audit_nm)) {
				// paramMap.put("auditPoint", "有价卡金额异常");
				resultList = sjzlService.getYJK04(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeYjk04(resultList, monMap,wb);
				generate(wb, "有价卡金额异常" + monMap.get("aud_trm") + ".xlsx",aud_trm,"1000",audit_nm,false);
				//生成专题表格的一个sheet
				XSSFWorkbook wbYjk = openExcel("有价卡违规" + monMap.get("aud_trm")+ ".xlsx","有价卡金额异常");	
				int sheetIndex = wbYjk.getSheetIndex("有价卡金额异常");
				if(sheetIndex>=0)wbYjk.removeSheetAt(sheetIndex);				 
				writeYjk04(resultList, monMap,wbYjk);
				generate(wbYjk, "有价卡违规" + monMap.get("aud_trm")+ ".xlsx",aud_trm,"1000",audit_nm,true);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "有价卡金额异常" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "有价卡违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("YJK_05".equals(audit_nm)) {
				// paramMap.put("auditPoint", "VC和CRM有价卡序列号相同但金额不同");
				resultList = sjzlService.getYJK05(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeYjk05(resultList, monMap,wb);
				generate(wb, "VC和CRM有价卡序列号相同但金额不同" + monMap.get("aud_trm") + ".xlsx",aud_trm,"1000",audit_nm,false);
				//生成专题表格的一个sheet
				XSSFWorkbook wbYjk = openExcel("有价卡违规" + monMap.get("aud_trm")+ ".xlsx","VC和CRM有价卡序列号相同但金额不同");	
				int sheetIndex = wbYjk.getSheetIndex("VC和CRM有价卡序列号相同但金额不同");
				if(sheetIndex>=0)wbYjk.removeSheetAt(sheetIndex);				 
				writeYjk05(resultList, monMap,wbYjk);
				generate(wbYjk, "有价卡违规" + monMap.get("aud_trm")+ ".xlsx",aud_trm,"1000",audit_nm,true);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "VC和CRM有价卡序列号相同但金额不同" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "有价卡违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("YJK_06".equals(audit_nm)) {
				// paramMap.put("auditPoint", "CRM同种状态的有价卡重复数量");
				resultList = sjzlService.getYJK06(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeYjk06(resultList, monMap,wb);
				generate(wb, "CRM同种状态的有价卡重复数量" + monMap.get("aud_trm") + ".xlsx",aud_trm,"1000",audit_nm,false);
				//生成专题表格的一个sheet
				XSSFWorkbook wbYjk = openExcel("有价卡违规" + monMap.get("aud_trm")+ ".xlsx","CRM同种状态的有价卡重复数量");	
				int sheetIndex = wbYjk.getSheetIndex("CRM同种状态的有价卡重复数量");
				if(sheetIndex>=0)wbYjk.removeSheetAt(sheetIndex);				 
				writeYjk06(resultList, monMap,wbYjk);
				generate(wbYjk, "有价卡违规" + monMap.get("aud_trm")+ ".xlsx",aud_trm,"1000",audit_nm,true);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "CRM同种状态的有价卡重复数量" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "有价卡违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("YJK_07".equals(audit_nm)) {
				// paramMap.put("auditPoint", "VC同种状态的有价卡重复数量");
				resultList = sjzlService.getYJK07(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeYjk07(resultList, monMap,wb);
				generate(wb, "VC同种状态的有价卡重复数量" + monMap.get("aud_trm") + ".xlsx",aud_trm,"1000",audit_nm,false);
				//生成专题表格的一个sheet
				XSSFWorkbook wbYjk = openExcel("有价卡违规" + monMap.get("aud_trm")+ ".xlsx","VC同种状态的有价卡重复数量");	
				int sheetIndex = wbYjk.getSheetIndex("VC同种状态的有价卡重复数量");
				if(sheetIndex>=0)wbYjk.removeSheetAt(sheetIndex);				 
				writeYjk07(resultList, monMap,wbYjk);
				generate(wbYjk, "有价卡违规" + monMap.get("aud_trm")+ ".xlsx",aud_trm,"1000",audit_nm,true);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "VC同种状态的有价卡重复数量" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "有价卡违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("ZDTL_01".equals(audit_nm)) {
				paramMap.put("auditPoint", "终端套利（05004入库情况）");
				resultList = sjzlService.getZDTL31(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeZdtl01(resultList, monMap,wb);
				generate(wb, "终端套利（05004终端销售服务费用信息入库情况）" + monMap.get("aud_trm") + ".xlsx",aud_trm,"3000",audit_nm,false);
				//生成专题表格的一个sheet
				XSSFWorkbook wbZdtl = openExcel("终端套利违规" + monMap.get("aud_trm")+ ".xlsx","05004终端销售服务费用信息入库情况");	
				int sheetIndex = wbZdtl.getSheetIndex("05004终端销售服务费用信息入库情况");
				if(sheetIndex>=0)wbZdtl.removeSheetAt(sheetIndex);				 
				writeZdtl01(resultList, monMap,wbZdtl);
				generate(wbZdtl, "终端套利违规" + monMap.get("aud_trm")+ ".xlsx",aud_trm,"3000",audit_nm,true);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "终端套利（05004终端销售服务费用信息入库情况）" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "终端套利违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("ZDTL_02".equals(audit_nm)) {
				paramMap.put("auditPoint", "终端套利（05004和22064关联比对）");
				resultList = sjzlService.getZDTL32(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeZdtl02(resultList, monMap,wb);
				generate(wb, "终端套利（05004终端销售服务费用信息和22064社会渠道销售服务费关联比对）" + monMap.get("aud_trm") + ".xlsx",aud_trm,"3000",audit_nm,false);
				//生成专题表格的一个sheet
				XSSFWorkbook wbZdtl = openExcel("终端套利违规" + monMap.get("aud_trm")+ ".xlsx","05004与22064关联比对");	
				int sheetIndex = wbZdtl.getSheetIndex("05004与22064关联比对");
				if(sheetIndex>=0)wbZdtl.removeSheetAt(sheetIndex);				 
				writeZdtl02(resultList, monMap,wbZdtl);
				generate(wbZdtl, "终端套利违规" + monMap.get("aud_trm")+ ".xlsx",aud_trm,"3000",audit_nm,true);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "终端套利（05004终端销售服务费用信息和22064社会渠道销售服务费关联比对）" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "终端套利违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("ZDTL_03".equals(audit_nm)) {
				paramMap.put("auditPoint", "终端套利违规（22064和22108关联比对）");
				resultList = sjzlService.getZDTL33(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeZdtl03(resultList, monMap,wb);
				generate(wb, "终端套利违规（22064社会渠道销售服务费和22108分用户销售费用信息关联比对）" + monMap.get("aud_trm") + ".xlsx",aud_trm,"3000",audit_nm,false);
				//生成专题表格的一个sheet
				XSSFWorkbook wbZdtl = openExcel("终端套利违规" + monMap.get("aud_trm")+ ".xlsx","22064与22108关联比对");	
				int sheetIndex = wbZdtl.getSheetIndex("22064与22108关联比对");
				if(sheetIndex>=0)wbZdtl.removeSheetAt(sheetIndex);				 
				writeZdtl03(resultList, monMap,wbZdtl);
				generate(wbZdtl, "终端套利违规" + monMap.get("aud_trm")+ ".xlsx",aud_trm,"3000",audit_nm,true);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "终端套利违规（22064社会渠道销售服务费和22108分用户销售费用信息关联比对）" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "终端套利违规" + monMap.get("aud_trm")+ ".xlsx");
			} else if ("ZDTL_04".equals(audit_nm)) {
				paramMap.put("auditPoint", "终端套利违规（22064与22062关联比对）");
				resultList = sjzlService.getZDTL34(paramMap);
				//生成单独表格
				XSSFWorkbook wb = new XSSFWorkbook();
				writeZdtl04(resultList, monMap,wb);
				generate(wb, "终端套利违规（22064社会渠道销售服务费与22062社会渠道酬金及补贴信息关联比对）" + monMap.get("aud_trm") + ".xlsx",aud_trm,"3000",audit_nm,false);
				//生成专题表格的一个sheet
				XSSFWorkbook wbZdtl = openExcel("终端套利违规" + monMap.get("aud_trm")+ ".xlsx","22064与22062关联比对");	
				int sheetIndex = wbZdtl.getSheetIndex("22064与22062关联比对");
				if(sheetIndex>=0)wbZdtl.removeSheetAt(sheetIndex);				 
				writeZdtl04(resultList, monMap,wbZdtl);
				generate(wbZdtl, "终端套利违规" + monMap.get("aud_trm")+ ".xlsx",aud_trm,"3000",audit_nm,true);
				paramMap.put("nmBm", audit_nm);
				paramMap.put("excelNm", "终端套利违规（22064社会渠道销售服务费与22062社会渠道酬金及补贴信息关联比对）" + monMap.get("aud_trm") + ".xlsx");
				paramMap.put("excelNmTol", "终端套利违规" + monMap.get("aud_trm")+ ".xlsx");
			}
			/**
			 * 更新下载信息
			 */
			//pmhzService.updateNotificationFile(month,subjectId,focusCd,isAuto,buildDownloadUrl());
			//sjzlService.updateDownloadInfo(paramMap);

		} catch (Exception e) {
			logger.error(e);
		}
		 }
		 }
	}

	public void writeZdtl04(List<Map<String, Object>> data, Map<String, Object> mon,XSSFWorkbook wb) throws Exception {
		//wb = new XSSFWorkbook();
		Sheet sh = wb.createSheet("22064与22062关联比对");
		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("省份编码");
		sh.getRow(0).createCell(1).setCellValue("省份名称");
		sh.getRow(0).createCell(2).setCellValue("渠道类型（22064）");
		sh.getRow(0).createCell(3).setCellValue("酬金总数（22064）");
		sh.getRow(0).createCell(4).setCellValue("渠道类型（22062）");
		sh.getRow(0).createCell(5).setCellValue("酬金总数（22062）");

		sh.createFreezePane(0, 1, 0, 5);

		for (int i = 0; i <= 5; i++) {
			sh.setColumnWidth(i, 256 * 20);
			sh.getRow(0).getCell(i).setCellStyle(getStyle1(wb));
		}

		sh.getRow(0).setHeightInPoints(23);
		for (int i = 0; i < data.size(); i++) {
			sh.createRow(1 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(1 + i).createCell(0).setCellValue((Integer) row.get("CMCC_prov_prvd_id"));
				sh.getRow(1 + i).createCell(1).setCellValue((String) row.get("CMCC_prov_prvd_NM"));
				sh.getRow(1 + i).createCell(2).setCellValue((String) row.get("typ_22064"));
				sh.getRow(1 + i)
						.createCell(3)
						.setCellValue(
								(row.get("cnt_22064") instanceof BigDecimal ? ((BigDecimal) row.get("cnt_22064")).doubleValue() : (Double) row
										.get("cnt_22064")));
				sh.getRow(1 + i).createCell(4).setCellValue((String) row.get("typ_22062"));
				sh.getRow(1 + i)
						.createCell(5)
						.setCellValue(
								(row.get("cnt_22062") instanceof BigDecimal ? ((BigDecimal) row.get("cnt_22062")).doubleValue() : (Double) row
										.get("cnt_22062")));

				sh.getRow(1 + i).getCell(0).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(1).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(2).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(3).setCellStyle(getStyle7(wb));
				sh.getRow(1 + i).getCell(4).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(5).setCellStyle(getStyle7(wb));

				sh.getRow(1 + i).setHeightInPoints(16);
			}
		}
		//generate(wb, "终端套利违规（22064社会渠道销售服务费与22062社会渠道酬金及补贴信息关联比对）" + mon.get("aud_trm") + ".xlsx");
		//openExcel("终端套利违规" + mon.get("aud_trm")+ ".xlsx");
	}

	public void writeZdtl03(List<Map<String, Object>> data, Map<String, Object> mon,XSSFWorkbook wb) throws Exception {
		//wb = new XSSFWorkbook();
		Sheet sh = wb.createSheet("22064与22108关联比对");
		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("省份编码");
		sh.getRow(0).getCell(0).setCellStyle(getStyle1(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sh.getRow(0).createCell(1).setCellValue("省份名称");
		sh.getRow(0).getCell(1).setCellStyle(getStyle1(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));

		sh.getRow(0).createCell(2).setCellValue("22064-社会渠道销售服务费");
		sh.getRow(0).getCell(2).setCellStyle(getStyle1(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 2, 4));

		sh.getRow(0).createCell(5).setCellValue("22108-分用户酬金");
		sh.getRow(0).getCell(5).setCellStyle(getStyle1(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 5, 7));

		sh.getRow(0).createCell(8).setCellValue("酬金科目是否一一对应");
		sh.getRow(0).getCell(8).setCellStyle(getStyle1(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 1, 8, 8));

		sh.getRow(0).createCell(9).setCellValue("若有该科目酬金差值");
		sh.getRow(0).getCell(9).setCellStyle(getStyle1(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 1, 9, 9));

		sh.getRow(0).createCell(10).setCellValue("若有该科目酬金差值占比");
		sh.getRow(0).getCell(10).setCellStyle(getStyle1(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));

		sh.createRow(1);
		sh.getRow(1).createCell(2).setCellValue("酬金科目编码");
		sh.getRow(1).getCell(2).setCellStyle(getStyle1(wb));
		sh.getRow(1).createCell(3).setCellValue("酬金科目名称");
		sh.getRow(1).getCell(3).setCellStyle(getStyle1(wb));
		sh.getRow(1).createCell(4).setCellValue("酬金总数");
		sh.getRow(1).getCell(4).setCellStyle(getStyle1(wb));

		sh.getRow(1).createCell(5).setCellValue("酬金科目编码");
		sh.getRow(1).getCell(5).setCellStyle(getStyle1(wb));
		sh.getRow(1).createCell(6).setCellValue("酬金科目名称");
		sh.getRow(1).getCell(6).setCellStyle(getStyle1(wb));
		sh.getRow(1).createCell(7).setCellValue("酬金总数");
		sh.getRow(1).getCell(7).setCellStyle(getStyle1(wb));

		sh.createFreezePane(0, 2, 0, 7);
		for (int i = 0; i <= 10; i++) {
			sh.setColumnWidth(i, 256 * 23);
		}
		sh.getRow(0).setHeightInPoints(16);
		sh.getRow(1).setHeightInPoints(16);
		for (int i = 0; i < data.size(); i++) {
			sh.createRow(2 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(2 + i).createCell(0).setCellValue((Integer) row.get("prvd_id_22064"));
				sh.getRow(2 + i).createCell(1).setCellValue((String) row.get("prvd_nm_22064"));
				sh.getRow(2 + i).getCell(0).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(1).setCellStyle(getStyle3(wb));

				sh.getRow(2 + i).createCell(2).setCellValue((String) row.get("sell_fee_subj"));
				sh.getRow(2 + i).createCell(3).setCellValue((String) row.get("subj_nm_22064"));
				sh.getRow(2 + i)
						.createCell(4)
						.setCellValue(
								(row.get("cnt_22064") instanceof BigDecimal ? ((BigDecimal) row.get("cnt_22064")).doubleValue() : (Double) row
										.get("cnt_22064")));

				sh.getRow(2 + i).createCell(5).setCellValue((String) row.get("rwd_subj"));
				sh.getRow(2 + i).createCell(6).setCellValue((String) row.get("subj_nm_22108"));
				sh.getRow(2 + i)
						.createCell(7)
						.setCellValue(
								(row.get("cnt_22108") instanceof BigDecimal ? ((BigDecimal) row.get("cnt_22108")).doubleValue() : (Double) row
										.get("cnt_22108")));

				sh.getRow(2 + i).createCell(8).setCellValue((String) row.get("flag"));
				sh.getRow(2 + i).createCell(9).setCellValue((row.get("amt_diff") instanceof BigDecimal ? ((BigDecimal) row.get("amt_diff")).doubleValue(): (Double) row.get("amt_diff")));
				sh.getRow(2 + i).createCell(10).setCellValue((row.get("amt_diff_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_diff_per")).doubleValue(): (Double) row.get("amt_diff_per")));

				sh.getRow(2 + i).getCell(2).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(3).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(4).setCellStyle(getStyle7(wb));

				sh.getRow(2 + i).getCell(5).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(6).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(7).setCellStyle(getStyle7(wb));

				sh.getRow(2 + i).getCell(8).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(9).setCellStyle(getStyle7(wb));
				sh.getRow(2 + i).getCell(10).setCellStyle(getStyle4(wb));

				sh.getRow(2 + i).setHeightInPoints(16);
			}
		}
		//generate(wb, "终端套利违规（22064社会渠道销售服务费和22108分用户销售费用信息关联比对）" + mon.get("aud_trm") + ".xlsx");
	}

	public void writeZdtl02(List<Map<String, Object>> data, Map<String, Object> mon,XSSFWorkbook wb) throws Exception {
		//wb = new XSSFWorkbook();
		Sheet sh = wb.createSheet("05004与22064关联比对");
		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("省份");
		sh.getRow(0).createCell(1).setCellValue("省编码");
		sh.getRow(0).createCell(2).setCellValue("酬金科目");
		sh.getRow(0).createCell(3).setCellValue("科目名称");
		sh.getRow(0).createCell(4).setCellValue("酬金金额-05004");

		sh.getRow(0).createCell(5).setCellValue("省份");
		sh.getRow(0).createCell(6).setCellValue("省编码");
		sh.getRow(0).createCell(7).setCellValue("酬金科目");
		sh.getRow(0).createCell(8).setCellValue("科目名称");
		sh.getRow(0).createCell(9).setCellValue("酬金金额-22064");

		sh.getRow(0).createCell(10).setCellValue("酬金科目是否匹配");
		sh.getRow(0).createCell(11).setCellValue("如果匹配金额差异");
		sh.getRow(0).createCell(12).setCellValue("酬金科目金额差异占比");

		sh.createFreezePane(0, 1, 0, 12);

		for (int i = 0; i <= 12; i++) {
			sh.setColumnWidth(i, 256 * 18);
			sh.getRow(0).getCell(i).setCellStyle(getStyle1(wb));
		}
		sh.setColumnWidth(12, 256 * 23);
		sh.getRow(0).setHeightInPoints(23);
		for (int i = 0; i < data.size(); i++) {
			sh.createRow(1 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(1 + i).createCell(0).setCellValue((String) row.get("prvd_nm_05004"));
				sh.getRow(1 + i).createCell(1).setCellValue((Integer) row.get("prvd_id_05004"));
				sh.getRow(1 + i).createCell(2).setCellValue((String) row.get("sett_amt_typ"));
				sh.getRow(1 + i).createCell(3).setCellValue((String) row.get("sett_amt_typ_05004"));
				sh.getRow(1 + i)
						.createCell(4)
						.setCellValue(
								(row.get("amt_sum_05004") instanceof BigDecimal ? ((BigDecimal) row.get("amt_sum_05004")).doubleValue()
										: (Double) row.get("amt_sum_05004")));
				sh.getRow(1 + i).getCell(0).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(1).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(2).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(3).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(4).setCellStyle(getStyle7(wb));

				sh.getRow(1 + i).createCell(5).setCellValue((String) row.get("prvd_nm_22064"));
				sh.getRow(1 + i).createCell(6).setCellValue((Integer) row.get("prvd_id_22064"));
				sh.getRow(1 + i).createCell(7).setCellValue((String) row.get("sell_fee_subj"));
				sh.getRow(1 + i).createCell(8).setCellValue((String) row.get("sell_fee_subj_22064"));
				sh.getRow(1 + i)
						.createCell(9)
						.setCellValue((row.get("amt_sum_22064") instanceof BigDecimal ? ((BigDecimal) row.get("amt_sum_22064")).doubleValue(): (Double) row.get("amt_sum_22064")));
				sh.getRow(1 + i).getCell(5).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(6).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(7).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(8).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(9).setCellStyle(getStyle7(wb));

				sh.getRow(1 + i).createCell(10).setCellValue((String) row.get("flag"));
				sh.getRow(1 + i).createCell(11).setCellValue((row.get("amt_diff") instanceof BigDecimal ? ((BigDecimal) row.get("amt_diff")).doubleValue(): (Double) row.get("amt_diff")));
				sh.getRow(1 + i).createCell(12).setCellValue((row.get("amt_diff_per") instanceof BigDecimal ? ((BigDecimal) row.get("amt_diff_per")).doubleValue(): (Double) row.get("amt_diff_per")));
				sh.getRow(1 + i).getCell(10).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(11).setCellStyle(getStyle7(wb));
				sh.getRow(1 + i).getCell(12).setCellStyle(getStyle4(wb));

				sh.getRow(1 + i).setHeightInPoints(16);
			}
		}
		//generate(wb, "终端套利（05004终端销售服务费用信息和22064社会渠道销售服务费关联比对）" + mon.get("aud_trm") + ".xlsx");
	}

	public void writeZdtl01(List<Map<String, Object>> data, Map<String, Object> mon,XSSFWorkbook wb) throws Exception {
		//wb = new XSSFWorkbook();
		Sheet sh = wb.createSheet("05004终端销售服务费用信息入库情况");
		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("省份");
		sh.getRow(0).getCell(0).setCellStyle(getStyle2(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
		sh.getRow(0).createCell(2).setCellValue(mon.get("month") + "月");
		sh.getRow(0).getCell(2).setCellStyle(getStyle0(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 2, 5));
		sh.getRow(0).createCell(6).setCellValue(mon.get("pmonth") + "月");
		sh.getRow(0).getCell(6).setCellStyle(getStyle0(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 6, 9));
		sh.getRow(0).createCell(10).setCellValue(mon.get("ppmonth") + "月");
		sh.getRow(0).getCell(10).setCellStyle(getStyle0(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 10, 13));
		sh.getRow(0).createCell(14).setCellValue(mon.get("pppmonth") + "月");
		sh.getRow(0).getCell(14).setCellStyle(getStyle0(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 14, 17));
		sh.createRow(1);
		sh.getRow(1).createCell(0).setCellValue("省份编码");
		sh.getRow(1).createCell(1).setCellValue("省份名称");

		sh.getRow(1).createCell(2).setCellValue("结酬月份");
		sh.getRow(1).createCell(3).setCellValue("数据入库日期");
		sh.getRow(1).createCell(4).setCellValue("记录数");
		sh.getRow(1).createCell(5).setCellValue("结酬金额");

		sh.getRow(1).createCell(6).setCellValue("结酬月份");
		sh.getRow(1).createCell(7).setCellValue("数据入库日期");
		sh.getRow(1).createCell(8).setCellValue("记录数");
		sh.getRow(1).createCell(9).setCellValue("结酬金额");

		sh.getRow(1).createCell(10).setCellValue("结酬月份");
		sh.getRow(1).createCell(11).setCellValue("数据入库日期");
		sh.getRow(1).createCell(12).setCellValue("记录数");
		sh.getRow(1).createCell(13).setCellValue("结酬金额");

		sh.getRow(1).createCell(14).setCellValue("结酬月份");
		sh.getRow(1).createCell(15).setCellValue("数据入库日期");
		sh.getRow(1).createCell(16).setCellValue("记录数");
		sh.getRow(1).createCell(17).setCellValue("结酬金额");

		sh.createFreezePane(0, 2, 0, 17);
		for (int i = 0; i <= 17; i++) {
			sh.setColumnWidth(i, 256 * 13);
			sh.getRow(1).getCell(i).setCellStyle(getStyle1(wb));
		}
		sh.getRow(0).setHeightInPoints(16);
		sh.getRow(1).setHeightInPoints(16);
		for (int i = 0; i < data.size(); i++) {
			sh.createRow(2 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(2 + i).createCell(0).setCellValue((Integer) row.get("prvd_id"));
				sh.getRow(2 + i).createCell(1).setCellValue((String) row.get("prvd_nm"));
				sh.getRow(2 + i).getCell(0).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(1).setCellStyle(getStyle3(wb));

				sh.getRow(2 + i).createCell(2).setCellValue((String) row.get("sett_month4"));
				sh.getRow(2 + i).createCell(3).setCellValue((String) row.get("d4"));
				sh.getRow(2 + i).createCell(4).setCellValue((Integer) row.get("num4"));
				sh.getRow(2 + i)
						.createCell(5)
						.setCellValue(
								(row.get("amt4") instanceof BigDecimal ? ((BigDecimal) row.get("amt4")).doubleValue() : (Double) row.get("amt4")));

				sh.getRow(2 + i).getCell(2).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(3).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(4).setCellStyle(getStyle6(wb));
				sh.getRow(2 + i).getCell(5).setCellStyle(getStyle7(wb));

				sh.getRow(2 + i).createCell(6).setCellValue((String) row.get("sett_month3"));
				sh.getRow(2 + i).createCell(7).setCellValue((String) row.get("d3"));
				sh.getRow(2 + i).createCell(8).setCellValue((Integer) row.get("num3"));
				sh.getRow(2 + i)
						.createCell(9)
						.setCellValue(
								(row.get("amt3") instanceof BigDecimal ? ((BigDecimal) row.get("amt3")).doubleValue() : (Double) row.get("amt3")));

				sh.getRow(2 + i).getCell(6).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(7).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(8).setCellStyle(getStyle6(wb));
				sh.getRow(2 + i).getCell(9).setCellStyle(getStyle7(wb));

				sh.getRow(2 + i).createCell(10).setCellValue((String) row.get("sett_month2"));
				sh.getRow(2 + i).createCell(11).setCellValue((String) row.get("d2"));
				sh.getRow(2 + i).createCell(12).setCellValue((Integer) row.get("num2"));
				sh.getRow(2 + i)
						.createCell(13)
						.setCellValue(
								(row.get("amt2") instanceof BigDecimal ? ((BigDecimal) row.get("amt2")).doubleValue() : (Double) row.get("amt2")));

				sh.getRow(2 + i).getCell(10).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(11).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(12).setCellStyle(getStyle6(wb));
				sh.getRow(2 + i).getCell(13).setCellStyle(getStyle7(wb));

				sh.getRow(2 + i).createCell(14).setCellValue((String) row.get("sett_month1"));
				sh.getRow(2 + i).createCell(15).setCellValue((String) row.get("d1"));
				sh.getRow(2 + i).createCell(16).setCellValue((Integer) row.get("num1"));
				sh.getRow(2 + i)
						.createCell(17)
						.setCellValue(
								(row.get("amt1") instanceof BigDecimal ? ((BigDecimal) row.get("amt1")).doubleValue() : (Double) row.get("amt1")));

				sh.getRow(2 + i).getCell(14).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(15).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(16).setCellStyle(getStyle6(wb));
				sh.getRow(2 + i).getCell(17).setCellStyle(getStyle7(wb));

				sh.getRow(2 + i).setHeightInPoints(16);
			}
		}
		//generate(wb, "终端套利（05004终端销售服务费用信息入库情况）" + mon.get("aud_trm") + ".xlsx");
	}

	public void writeQdyk07(List<Map<String, Object>> data1, Map<String, Object> mon,XSSFWorkbook wb,Boolean isTol) throws Exception {
		//wb = new XSSFWorkbook();
		writeYk(data1, mon, wb, "入网用户接口数据延迟入库");
		//generate(wb, "入网用户接口数据延迟入库" + mon.get("aud_trm") + ".xlsx");
	}
	
	public void writeQdyk06(List<Map<String, Object>> data1, Map<String, Object> mon,XSSFWorkbook wb,Boolean isTol) throws Exception {
		//wb = new XSSFWorkbook();
		writeYk(data1, mon, wb, "入网渠道相同但是归属地市不同");
		//generate(wb, "入网渠道相同但是归属地市不同" + mon.get("aud_trm") + ".xlsx");
	}
	
	public void writeQdyk05(List<Map<String, Object>> data, List<Map<String, Object>> data1, Map<String, Object> mon,XSSFWorkbook wb,Boolean isTol) throws Exception {
		//wb = new XSSFWorkbook();
		if(!isTol){
			writeYk(data1, mon, wb, "统计情况");
			sh = wb.createSheet("详细信息");
		}else
			sh = wb.createSheet("数据SIM卡用户标志的统计情况");

		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("省份编码");
		sh.getRow(0).createCell(1).setCellValue("省份名称");
		sh.getRow(0).createCell(2).setCellValue("上一条数据SIM卡用户标志编码");
		sh.getRow(0).createCell(3).setCellValue("上一条数据SIM卡用户标志名称");
		sh.getRow(0).createCell(4).setCellValue("下一条数据SIM卡用户标志编码");
		sh.getRow(0).createCell(5).setCellValue("下一条数据SIM卡用户标志名称");
		sh.getRow(0).createCell(6).setCellValue("变更数");
		sh.createFreezePane(0, 1, 0, 6);
		for (int i = 0; i <= 6; i++) {
			sh.setColumnWidth(i, 256 * 23);
			sh.getRow(0).getCell(i).setCellStyle(getStyle1(wb));
		}
		sh.getRow(0).setHeightInPoints(28);
		for (int i = 0; i < data.size(); i++) {
			sh.createRow(1 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(1 + i).createCell(0).setCellValue((Integer) row.get("CMCC_prov_prvd_id"));
				sh.getRow(1 + i).createCell(1).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
				sh.getRow(1 + i).createCell(2).setCellValue((Integer) row.get("dat_sim_crd_subs_flg"));
				sh.getRow(1 + i).createCell(3).setCellValue((String) row.get("card_typ_name"));
				sh.getRow(1 + i).createCell(4).setCellValue((Integer) row.get("dat_sim_crd_subs_flg2"));
				sh.getRow(1 + i).createCell(5).setCellValue((String) row.get("card_typ_name2"));
				sh.getRow(1 + i).createCell(6).setCellValue((Integer) row.get("sl"));

				sh.getRow(1 + i).getCell(0).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(1).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(2).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(3).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(4).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(5).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(6).setCellStyle(getStyle6(wb));

				sh.getRow(1 + i).setHeightInPoints(16);
			}
		}

		//generate(wb, "数据SIM卡用户标志的统计情况" + mon.get("aud_trm") + ".xlsx");
	}

	public void writeQdyk04(List<Map<String, Object>> data, List<Map<String, Object>> data1, Map<String, Object> mon,XSSFWorkbook wb,Boolean isTol) throws Exception {
		//wb = new XSSFWorkbook();
		if(!isTol){
			writeYk(data1, mon, wb, "统计情况");
			sh = wb.createSheet("详细信息");
		}else
			sh = wb.createSheet("入网时间异常变更");

		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("省份编码");
		sh.getRow(0).createCell(1).setCellValue("省份名称");
		sh.getRow(0).createCell(2).setCellValue("时间长度");
		sh.getRow(0).createCell(3).setCellValue("变更数");
		sh.createFreezePane(0, 1, 0, 3);
		for (int i = 0; i <= 3; i++) {
			sh.setColumnWidth(i, 256 * 16);
			sh.getRow(0).getCell(i).setCellStyle(getStyle1(wb));
		}
		sh.getRow(0).setHeightInPoints(16);
		for (int i = 0; i < data.size(); i++) {
			sh.createRow(1 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(1 + i).createCell(0).setCellValue((Integer) row.get("CMCC_prov_prvd_id"));
				sh.getRow(1 + i).createCell(1).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
				sh.getRow(1 + i).createCell(2).setCellValue((String) row.get("day_between_info"));
				sh.getRow(1 + i).createCell(3).setCellValue((Integer) row.get("sl"));

				sh.getRow(1 + i).getCell(0).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(1).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(2).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(3).setCellStyle(getStyle6(wb));

				sh.getRow(1 + i).setHeightInPoints(16);
			}
		}

		//generate(wb, "入网时间异常变更" + mon.get("aud_trm") + ".xlsx");
	}

	public void writeQdyk03(List<Map<String, Object>> data, List<Map<String, Object>> data1, Map<String, Object> mon,XSSFWorkbook wb,Boolean isTol) throws Exception {
		//wb = new XSSFWorkbook();
		if(!isTol){
			writeYk(data1, mon, wb, "统计情况");
			sh = wb.createSheet("详细信息");
		}else
			sh = wb.createSheet("入网渠道异常变更");

		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("省份编码");
		sh.getRow(0).createCell(1).setCellValue("省份名称");
		sh.getRow(0).createCell(2).setCellValue("上一条渠道类型编码");
		sh.getRow(0).createCell(3).setCellValue("上一条渠道类型名称");
		sh.getRow(0).createCell(4).setCellValue("下一条渠道类型编码");
		sh.getRow(0).createCell(5).setCellValue("下一条渠道类型名称");
		sh.getRow(0).createCell(6).setCellValue("变更数");
		sh.createFreezePane(0, 1, 0, 6);
		for (int i = 0; i <= 6; i++) {
			sh.setColumnWidth(i, 256 * 20);
			sh.getRow(0).getCell(i).setCellStyle(getStyle1(wb));
		}
		sh.getRow(0).setHeightInPoints(16);
		for (int i = 0; i < data.size(); i++) {
			sh.createRow(1 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(1 + i).createCell(0).setCellValue((Integer) row.get("CMCC_prov_prvd_id"));
				sh.getRow(1 + i).createCell(1).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
				sh.getRow(1 + i).createCell(2).setCellValue((Integer) row.get("chnl_type"));
				sh.getRow(1 + i).createCell(3).setCellValue((String) row.get("chnl_type_name"));
				sh.getRow(1 + i).createCell(4).setCellValue((Integer) row.get("chnl_type2"));
				sh.getRow(1 + i).createCell(5).setCellValue((String) row.get("chnl_type_name2"));
				sh.getRow(1 + i).createCell(6).setCellValue((Integer) row.get("sl"));

				sh.getRow(1 + i).getCell(0).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(1).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(2).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(3).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(4).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(5).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(6).setCellStyle(getStyle6(wb));

				sh.getRow(1 + i).setHeightInPoints(16);
			}
		}

		//generate(wb, "入网渠道异常变更" + mon.get("aud_trm") + ".xlsx");
	}

	public void writeQdyk02(List<Map<String, Object>> data, List<Map<String, Object>> data1, Map<String, Object> mon,XSSFWorkbook wb,Boolean isTol) throws Exception {
		//wb = new XSSFWorkbook();
		if(!isTol){
			writeYk(data1, mon, wb, "统计情况");
			sh = wb.createSheet("详细信息");
		}else
			sh = wb.createSheet("用户类型异常变更");

		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("省份编码");
		sh.getRow(0).createCell(1).setCellValue("省份名称");
		sh.getRow(0).createCell(2).setCellValue("上一条用户类型编码");
		sh.getRow(0).createCell(3).setCellValue("上一条用户类型名称");
		sh.getRow(0).createCell(4).setCellValue("下一条用户类型编码");
		sh.getRow(0).createCell(5).setCellValue("下一条用户类型名称");
		sh.getRow(0).createCell(6).setCellValue("变更数");
		sh.createFreezePane(0, 1, 0, 6);
		for (int i = 0; i <= 6; i++) {
			sh.setColumnWidth(i, 256 * 20);
			sh.getRow(0).getCell(i).setCellStyle(getStyle1(wb));
		}
		sh.getRow(0).setHeightInPoints(16);
		for (int i = 0; i < data.size(); i++) {
			sh.createRow(1 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(1 + i).createCell(0).setCellValue((Integer) row.get("CMCC_prov_prvd_id"));
				sh.getRow(1 + i).createCell(1).setCellValue((String) row.get("CMCC_prov_prvd_nm"));
				sh.getRow(1 + i).createCell(2).setCellValue((Integer) row.get("subs_typ_cd"));
				sh.getRow(1 + i).createCell(3).setCellValue((String) row.get("subs_typ_name"));
				sh.getRow(1 + i).createCell(4).setCellValue((Integer) row.get("subs_typ_cd2"));
				sh.getRow(1 + i).createCell(5).setCellValue((String) row.get("subs_typ_name2"));
				sh.getRow(1 + i).createCell(6).setCellValue((Integer) row.get("sl"));

				sh.getRow(1 + i).getCell(0).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(1).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(2).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(3).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(4).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(5).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(6).setCellStyle(getStyle6(wb));

				sh.getRow(1 + i).setHeightInPoints(16);
			}
		}

		//generate(wb, "用户类型异常变更" + mon.get("aud_trm") + ".xlsx");
	}

	public void writeYk(List<Map<String, Object>> data, Map<String, Object> mon, XSSFWorkbook wb, String SheetName) throws Exception {
		sh = wb.createSheet(SheetName);
		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("数据表");
		sh.getRow(0).createCell(1).setCellValue("审计月份");
		sh.getRow(0).createCell(2).setCellValue("省份编码");
		sh.getRow(0).createCell(3).setCellValue("省份名称");
		sh.getRow(0).createCell(4).setCellValue("异常编码");
		sh.getRow(0).createCell(5).setCellValue("异常名称");
		sh.getRow(0).createCell(6).setCellValue("本月异常个数");
		sh.getRow(0).createCell(7).setCellValue("本月异常涉及的用户数");
		sh.getRow(0).createCell(8).setCellValue("本月的入网用户数（审计月上传）");
		sh.getRow(0).createCell(9).setCellValue("本月涉及异常用户数占比");
		sh.getRow(0).createCell(10).setCellValue("上月异常个数");
		sh.getRow(0).createCell(11).setCellValue("上月异常涉及的用户数");
		sh.getRow(0).createCell(12).setCellValue("上月的入网用户数（审计月上传）");
		sh.getRow(0).createCell(13).setCellValue("上月涉及异常用户数占比");
		sh.getRow(0).createCell(14).setCellValue("涉及异常用户数占比差值");
		sh.createFreezePane(0, 1, 0, 14);

		for (int i = 0; i <= 14; i++) {
			sh.setColumnWidth(i, 256 * 20);
			sh.getRow(0).getCell(i).setCellStyle(getStyle1(wb));
		}
		sh.setColumnWidth(0, 256 * 20);
		sh.setColumnWidth(5, 256 * 40);
		sh.getRow(0).setHeightInPoints(30);
		for (int i = 0; i < data.size(); i++) {
			sh.createRow(1 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(1 + i).createCell(0).setCellValue((String) row.get("src_table"));
				sh.getRow(1 + i).createCell(1).setCellValue((String) row.get("aud_trm"));
				sh.getRow(1 + i).createCell(2).setCellValue((Integer) row.get("prvd_id"));
				sh.getRow(1 + i).createCell(3).setCellValue((String) row.get("prvd_name"));
				sh.getRow(1 + i).createCell(4).setCellValue((String) row.get("err_type_cd"));
				sh.getRow(1 + i).createCell(5).setCellValue((String) row.get("err_type_name"));
				sh.getRow(1 + i)
						.createCell(6)
						.setCellValue(
								(row.get("this_err_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("this_err_cnt")).doubleValue() : (Double) row
										.get("this_err_cnt")));
				sh.getRow(1 + i)
						.createCell(7)
						.setCellValue(
								(row.get("this_err_subs_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("this_err_subs_cnt")).doubleValue()
										: (Double) row.get("this_err_subs_cnt")));
				sh.getRow(1 + i)
						.createCell(8)
						.setCellValue(
								(row.get("this_ent_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("this_ent_cnt")).doubleValue() : (Double) row
										.get("this_ent_cnt")));
				sh.getRow(1 + i)
						.createCell(9)
						.setCellValue(
								(row.get("this_err_subs_per") instanceof BigDecimal ? ((BigDecimal) row.get("this_err_subs_per")).doubleValue()
										: (Double) row.get("this_err_subs_per")));
				sh.getRow(1 + i)
						.createCell(10)
						.setCellValue(
								(row.get("last_err_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("last_err_cnt")).doubleValue() : (Double) row
										.get("last_err_cnt")));
				sh.getRow(1 + i)
						.createCell(11)
						.setCellValue(
								(row.get("last_err_subs_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("last_err_subs_cnt")).doubleValue()
										: (Double) row.get("last_err_subs_cnt")));
				sh.getRow(1 + i)
						.createCell(12)
						.setCellValue(
								(row.get("last_ent_cnt") instanceof BigDecimal ? ((BigDecimal) row.get("last_ent_cnt")).doubleValue() : (Double) row
										.get("last_ent_cnt")));
				sh.getRow(1 + i)
						.createCell(13)
						.setCellValue(
								(row.get("last_err_subs_per") instanceof BigDecimal ? ((BigDecimal) row.get("last_err_subs_per")).doubleValue()
										: (Double) row.get("last_err_subs_per")));
				sh.getRow(1 + i)
						.createCell(14)
						.setCellValue(
								(row.get("inter_err_subs_per") instanceof BigDecimal ? ((BigDecimal) row.get("inter_err_subs_per")).doubleValue()
										: (Double) row.get("inter_err_subs_per")));

				sh.getRow(1 + i).getCell(0).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(1).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(2).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(3).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(4).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(5).setCellStyle(getStyle3(wb));

				sh.getRow(1 + i).getCell(6).setCellStyle(getStyle6(wb));
				sh.getRow(1 + i).getCell(7).setCellStyle(getStyle6(wb));
				sh.getRow(1 + i).getCell(8).setCellStyle(getStyle6(wb));
				sh.getRow(1 + i).getCell(9).setCellStyle(getStyle4(wb));

				sh.getRow(1 + i).getCell(10).setCellStyle(getStyle6(wb));
				sh.getRow(1 + i).getCell(11).setCellStyle(getStyle6(wb));
				sh.getRow(1 + i).getCell(12).setCellStyle(getStyle6(wb));
				sh.getRow(1 + i).getCell(13).setCellStyle(getStyle4(wb));
				sh.getRow(1 + i).getCell(14).setCellStyle(getStyle4(wb));

				sh.getRow(1 + i).setHeightInPoints(16);
			}
		}

		// generate(wb, "用户历史信息表02004整体的数据情况" + mon.get("aud_trm")+ ".xlsx");
	}

	public void writeYjk07(List<Map<String, Object>> data, Map<String, Object> mon,XSSFWorkbook wb) throws Exception {
		//wb = new XSSFWorkbook();
		sh = wb.createSheet("VC同种状态的有价卡重复数量");
		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("公司编码");
		sh.getRow(0).createCell(1).setCellValue("公司名称");
		sh.getRow(0).createCell(2).setCellValue("有价卡状态");
		sh.getRow(0).createCell(3).setCellValue("有价卡状态名称");
		sh.getRow(0).createCell(4).setCellValue("涉及有价卡数量");
		sh.getRow(0).createCell(5).setCellValue("有价卡重复次数");
		sh.getRow(0).createCell(6).setCellValue("涉及有价卡金额");
		sh.createFreezePane(0, 1, 0, 6);
		for (int i = 0; i <= 6; i++) {
			sh.setColumnWidth(i, 256 * 15);
			sh.getRow(0).getCell(i).setCellStyle(getStyle1(wb));
		}
		sh.getRow(0).setHeightInPoints(16);
		for (int i = 0; i < data.size(); i++) {
			sh.createRow(1 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(1 + i).createCell(0).setCellValue((Integer) row.get("prvd_id"));
				sh.getRow(1 + i).createCell(1).setCellValue((String) row.get("prvd_nm"));
				sh.getRow(1 + i).createCell(2).setCellValue((String) row.get("yjk_stat"));
				sh.getRow(1 + i).createCell(3).setCellValue((String) row.get("yjk_stat_nm"));
				sh.getRow(1 + i).createCell(4).setCellValue((Integer) row.get("yjk_num"));
				sh.getRow(1 + i).createCell(5).setCellValue((Integer) row.get("re_num"));
				sh.getRow(1 + i)
						.createCell(6)
						.setCellValue(
								(row.get("re_amt") instanceof BigDecimal ? ((BigDecimal) row.get("re_amt")).doubleValue() : (Double) row
										.get("re_amt")));
				sh.getRow(1 + i).getCell(0).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(1).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(2).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(3).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(4).setCellStyle(getStyle6(wb));
				sh.getRow(1 + i).getCell(5).setCellStyle(getStyle6(wb));
				sh.getRow(1 + i).getCell(6).setCellStyle(getStyle7(wb));

				sh.getRow(1 + i).setHeightInPoints(16);
			}
		}

		//generate(wb, "VC同种状态的有价卡重复数量" + mon.get("aud_trm") + ".xlsx");
	}

	public void writeYjk06(List<Map<String, Object>> data, Map<String, Object> mon,XSSFWorkbook wb) throws Exception {
		//wb = new XSSFWorkbook();
		sh = wb.createSheet("CRM同种状态的有价卡重复数量");
		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("公司编码");
		sh.getRow(0).createCell(1).setCellValue("公司名称");
		sh.getRow(0).createCell(2).setCellValue("有价卡状态");
		sh.getRow(0).createCell(3).setCellValue("有价卡状态名称");
		sh.getRow(0).createCell(4).setCellValue("涉及有价卡数量");
		sh.getRow(0).createCell(5).setCellValue("有价卡重复次数");
		sh.getRow(0).createCell(6).setCellValue("涉及有价卡金额");
		sh.createFreezePane(0, 1, 0, 6);
		for (int i = 0; i <= 6; i++) {
			sh.setColumnWidth(i, 256 * 15);
			sh.getRow(0).getCell(i).setCellStyle(getStyle1(wb));
		}
		sh.getRow(0).setHeightInPoints(16);
		for (int i = 0; i < data.size(); i++) {
			sh.createRow(1 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(1 + i).createCell(0).setCellValue((Integer) row.get("prvd_id"));
				sh.getRow(1 + i).createCell(1).setCellValue((String) row.get("prvd_nm"));
				sh.getRow(1 + i).createCell(2).setCellValue((String) row.get("yjk_stat"));
				sh.getRow(1 + i).createCell(3).setCellValue((String) row.get("yjk_stat_nm"));
				sh.getRow(1 + i).createCell(4).setCellValue((Integer) row.get("yjk_num"));
				sh.getRow(1 + i).createCell(5).setCellValue((Integer) row.get("re_num"));
				sh.getRow(1 + i)
						.createCell(6)
						.setCellValue(
								(row.get("re_amt") instanceof BigDecimal ? ((BigDecimal) row.get("re_amt")).doubleValue() : (Double) row
										.get("re_amt")));
				sh.getRow(1 + i).getCell(0).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(1).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(2).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(3).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(4).setCellStyle(getStyle6(wb));
				sh.getRow(1 + i).getCell(5).setCellStyle(getStyle6(wb));
				sh.getRow(1 + i).getCell(6).setCellStyle(getStyle7(wb));

				sh.getRow(1 + i).setHeightInPoints(16);
			}
		}

		//generate(wb, "CRM同种状态的有价卡重复数量" + mon.get("aud_trm") + ".xlsx");
	}

	public void writeYjk05(List<Map<String, Object>> data, Map<String, Object> mon,XSSFWorkbook wb) throws Exception {
		//wb = new XSSFWorkbook();
		sh = wb.createSheet("VC和CRM有价卡序列号相同但金额不同");
		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("省份编码");
		sh.getRow(0).createCell(1).setCellValue("省份名称");
		sh.getRow(0).createCell(2).setCellValue("有价卡状态");
		sh.getRow(0).createCell(3).setCellValue("有价卡类型");
		sh.getRow(0).createCell(4).setCellValue("有价卡数量");
		sh.getRow(0).createCell(5).setCellValue("CRM侧金额");
		sh.getRow(0).createCell(6).setCellValue("VC侧金额");
		sh.createFreezePane(0, 1, 0, 6);
		for (int i = 0; i <= 6; i++) {
			sh.setColumnWidth(i, 256 * 15);
			sh.getRow(0).getCell(i).setCellStyle(getStyle1(wb));
		}
		sh.getRow(0).setHeightInPoints(16);
		for (int i = 0; i < data.size(); i++) {
			sh.createRow(1 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(1 + i).createCell(0).setCellValue((Integer) row.get("prvd_id"));
				sh.getRow(1 + i).createCell(1).setCellValue((String) row.get("prvd_nm"));
				sh.getRow(1 + i).createCell(2).setCellValue((String) row.get("yjk_stat"));
				sh.getRow(1 + i).createCell(3).setCellValue((String) row.get("yjk_typ"));
				sh.getRow(1 + i).createCell(4).setCellValue((Integer) row.get("yjk_num"));
				sh.getRow(1 + i)
						.createCell(5)
						.setCellValue(
								(row.get("crm_amt") instanceof BigDecimal ? ((BigDecimal) row.get("crm_amt")).doubleValue() : (Double) row
										.get("crm_amt")));
				sh.getRow(1 + i)
						.createCell(6)
						.setCellValue(
								(row.get("vc_amt") instanceof BigDecimal ? ((BigDecimal) row.get("vc_amt")).doubleValue() : (Double) row
										.get("vc_amt")));
				sh.getRow(1 + i).getCell(0).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(1).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(2).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(3).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(4).setCellStyle(getStyle6(wb));
				sh.getRow(1 + i).getCell(5).setCellStyle(getStyle7(wb));
				sh.getRow(1 + i).getCell(6).setCellStyle(getStyle7(wb));

				sh.getRow(1 + i).setHeightInPoints(16);
			}
		}

		//generate(wb, "VC和CRM有价卡序列号相同但金额不同" + mon.get("aud_trm") + ".xlsx");
	}

	public void writeYjk04(List<Map<String, Object>> data, Map<String, Object> mon,XSSFWorkbook wb) throws Exception {
		//wb = new XSSFWorkbook();
		sh = wb.createSheet("有价卡金额异常");
		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("省份编码");
		sh.getRow(0).createCell(1).setCellValue("省份名称");
		sh.getRow(0).createCell(2).setCellValue("接口编号");
		sh.getRow(0).createCell(3).setCellValue("金额异常类型");
		sh.getRow(0).createCell(4).setCellValue("有价卡类型");
		sh.getRow(0).createCell(5).setCellValue("有价卡类型名称");
		sh.getRow(0).createCell(6).setCellValue("有价卡状态");
		sh.getRow(0).createCell(7).setCellValue("有价卡状态名称");
		sh.getRow(0).createCell(8).setCellValue("有价卡数量");
		sh.getRow(0).createCell(9).setCellValue("涉及有价卡金额");
		sh.createFreezePane(0, 1, 0, 9);
		for (int i = 0; i <= 9; i++) {
			sh.setColumnWidth(i, 256 * 15);
			sh.getRow(0).getCell(i).setCellStyle(getStyle1(wb));
		}
		sh.setColumnWidth(3, 256 * 26);
		sh.setColumnWidth(9, 256 * 20);
		sh.getRow(0).setHeightInPoints(16);

		for (int i = 0; i < data.size(); i++) {
			sh.createRow(1 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(1 + i).createCell(0).setCellValue((Integer) row.get("prvd_id"));
				sh.getRow(1 + i).createCell(1).setCellValue((String) row.get("prvd_nm"));
				sh.getRow(1 + i).createCell(2).setCellValue((String) row.get("interface"));
				sh.getRow(1 + i).createCell(3).setCellValue((String) row.get("error_typ"));
				sh.getRow(1 + i).createCell(4).setCellValue((String) row.get("yjk_typ"));
				sh.getRow(1 + i).createCell(5).setCellValue((String) row.get("yjk_typ_nm"));
				sh.getRow(1 + i).createCell(6).setCellValue((String) row.get("yjk_stat"));
				sh.getRow(1 + i).createCell(7).setCellValue((String) row.get("yjk_stat_nm"));
				sh.getRow(1 + i).createCell(8).setCellValue((Integer) row.get("yjk_num"));
				sh.getRow(1 + i)
						.createCell(9)
						.setCellValue(
								(row.get("yjk_amt") instanceof BigDecimal ? ((BigDecimal) row.get("yjk_amt")).doubleValue() : (Double) row
										.get("yjk_amt")));

				sh.getRow(1 + i).getCell(0).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(1).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(2).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(3).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(4).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(5).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(6).setCellStyle(getStyle3(wb));
				sh.getRow(1 + i).getCell(7).setCellStyle(getStyle3(wb));

				sh.getRow(1 + i).getCell(8).setCellStyle(getStyle6(wb));
				sh.getRow(1 + i).getCell(9).setCellStyle(getStyle7(wb));

				sh.getRow(1 + i).setHeightInPoints(16);
			}
		}

		//generate(wb, "有价卡金额异常" + mon.get("aud_trm") + ".xlsx");
	}

	public void writeYjk03(List<Map<String, Object>> data, Map<String, Object> mon,XSSFWorkbook wb) throws Exception {

		//wb = new XSSFWorkbook();
		sh = wb.createSheet("06049(VC)与06045(CRM)关联比对");
		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("省份编码");
		sh.getRow(0).getCell(0).setCellStyle(getStyle2(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

		sh.getRow(0).createCell(1).setCellValue("有价卡状态变更记录表（crm）");
		sh.getRow(0).getCell(1).setCellStyle(getStyle1(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 1, 4));

		sh.getRow(0).createCell(5).setCellValue("有价卡状态变更记录表（vc）");
		sh.getRow(0).getCell(5).setCellStyle(getStyle1(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 5, 8));

		sh.getRow(0).createCell(9).setCellValue("差异量");
		sh.addMergedRegion(new CellRangeAddress(0, 1, 9, 9));
		sh.getRow(0).getCell(9).setCellStyle(getStyle2(wb));

		sh.getRow(0).createCell(10).setCellValue("问题描述");
		sh.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));
		sh.getRow(0).getCell(10).setCellStyle(getStyle2(wb));

		sh.createRow(1);
		sh.getRow(1).createCell(1).setCellValue("省公司名称");
		sh.getRow(1).createCell(2).setCellValue("接口编号");
		sh.getRow(1).createCell(3).setCellValue("有价卡状态名称");
		sh.getRow(1).createCell(4).setCellValue("数据量");
		sh.getRow(1).createCell(5).setCellValue("省公司名称");
		sh.getRow(1).createCell(6).setCellValue("接口编号");
		sh.getRow(1).createCell(7).setCellValue("有价卡状态名称");
		sh.getRow(1).createCell(8).setCellValue("数据量");

		sh.createFreezePane(0, 2, 0, 2);
		for (int i = 1; i <= 8; i++) {
			sh.setColumnWidth(i, 256 * 15);
			sh.getRow(1).getCell(i).setCellStyle(getStyle1(wb));
		}
		sh.getRow(0).setHeightInPoints(16);
		sh.getRow(1).setHeightInPoints(16);
		for (int i = 0; i < data.size(); i++) {
			sh.createRow(2 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(2 + i).createCell(0).setCellValue((Integer) row.get("crm_prvd_id"));
				sh.getRow(2 + i).getCell(0).setCellStyle(getStyle3(wb));

				sh.getRow(2 + i).createCell(1).setCellValue((String) row.get("crm_prvd_nm"));
				sh.getRow(2 + i).createCell(2).setCellValue("06045");
				sh.getRow(2 + i).createCell(3).setCellValue((String) row.get("crm_stat"));
				sh.getRow(2 + i).createCell(4).setCellValue((Integer) row.get("crm_num"));
				sh.getRow(2 + i).getCell(1).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(2).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(3).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(4).setCellStyle(getStyle6(wb));

				sh.getRow(2 + i).createCell(5).setCellValue((String) row.get("vc_prvd_nm"));
				sh.getRow(2 + i).createCell(6).setCellValue("06049");
				sh.getRow(2 + i).createCell(7).setCellValue((String) row.get("vc_stat"));
				sh.getRow(2 + i).createCell(8).setCellValue((Integer) row.get("vc_num"));
				sh.getRow(2 + i).getCell(5).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(6).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(7).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(8).setCellStyle(getStyle6(wb));

				sh.getRow(2 + i)
						.createCell(9)
						.setCellValue(
								(row.get("diff") instanceof BigDecimal ? ((BigDecimal) row.get("diff")).doubleValue() : (Double) row.get("diff")));
				sh.getRow(2 + i).getCell(9).setCellStyle(getStyle4(wb));

				sh.getRow(2 + i).createCell(10).setCellValue((String) row.get("descp"));
				sh.getRow(2 + i).getCell(10).setCellStyle(getStyle3(wb));
			}
		}

		//generate(wb, "有价卡违规（06049有价卡状态变更记录(VC)和06045有价卡状态变更记录(CRM)关联比对）" + mon.get("aud_trm") + ".xlsx");
	}

	public void writeYjk02(List<Map<String, Object>> data, Map<String, Object> mon,XSSFWorkbook wb) throws Exception {
		//wb = new XSSFWorkbook();
		sh = wb.createSheet("06045有价卡状态变更记录(CRM)入库情况");
		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("省份编码");
		sh.getRow(0).getCell(0).setCellStyle(getStyle2(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sh.getRow(0).createCell(1).setCellValue(mon.get("month") + "月");
		sh.getRow(0).getCell(1).setCellStyle(getStyle0(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
		sh.getRow(0).createCell(4).setCellValue(mon.get("pmonth") + "月");
		sh.getRow(0).getCell(4).setCellStyle(getStyle0(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 4, 6));
		sh.getRow(0).createCell(7).setCellValue(mon.get("ppmonth") + "月");
		sh.getRow(0).getCell(7).setCellStyle(getStyle0(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 7, 9));
		sh.getRow(0).createCell(10).setCellValue(mon.get("pppmonth") + "月");
		sh.getRow(0).getCell(10).setCellStyle(getStyle0(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 10, 12));
		sh.getRow(0).createCell(13).setCellValue(mon.get("ppppmonth") + "月");
		sh.getRow(0).getCell(13).setCellStyle(getStyle0(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 13, 15));
		sh.createRow(1);
		sh.getRow(1).createCell(1).setCellValue("省份名称");
		sh.getRow(1).createCell(2).setCellValue("记录数");
		sh.getRow(1).createCell(3).setCellValue("环比上月增幅");
		sh.getRow(1).createCell(4).setCellValue("省份名称");
		sh.getRow(1).createCell(5).setCellValue("记录数");
		sh.getRow(1).createCell(6).setCellValue("环比上月增幅");
		sh.getRow(1).createCell(7).setCellValue("省份名称");
		sh.getRow(1).createCell(8).setCellValue("记录数");
		sh.getRow(1).createCell(9).setCellValue("环比上月增幅");
		sh.getRow(1).createCell(10).setCellValue("省份名称");
		sh.getRow(1).createCell(11).setCellValue("记录数");
		sh.getRow(1).createCell(12).setCellValue("环比上月增幅");
		sh.getRow(1).createCell(13).setCellValue("省份名称");
		sh.getRow(1).createCell(14).setCellValue("记录数");
		sh.getRow(1).createCell(15).setCellValue("环比上月增幅");
		sh.createFreezePane(0, 2, 0, 2);
		for (int i = 1; i <= 15; i++) {
			sh.setColumnWidth(i, 256 * 13);
			sh.getRow(1).getCell(i).setCellStyle(getStyle1(wb));
		}
		sh.getRow(0).setHeightInPoints(16);
		sh.getRow(1).setHeightInPoints(16);
		for (int i = 0; i < data.size(); i++) {
			sh.createRow(2 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(2 + i).createCell(0).setCellValue((Integer) row.get("prvd_id"));
				sh.getRow(2 + i).getCell(0).setCellStyle(getStyle3(wb));

				sh.getRow(2 + i).createCell(1).setCellValue((String) row.get("prvd_nm"));
				sh.getRow(2 + i).createCell(2).setCellValue((Integer) row.get("aud_trm_cnt"));
				sh.getRow(2 + i).createCell(3)
						.setCellValue((row.get("per") instanceof BigDecimal ? ((BigDecimal) row.get("per")).doubleValue() : (Double) row.get("per")));
				sh.getRow(2 + i).getCell(1).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(2).setCellStyle(getStyle6(wb));
				sh.getRow(2 + i).getCell(3).setCellStyle(getStyle4(wb));

				sh.getRow(2 + i).createCell(4).setCellValue((String) row.get("prvd_nm"));
				sh.getRow(2 + i).createCell(5).setCellValue((Integer) row.get("pre_aud_trm_cnt"));
				sh.getRow(2 + i)
						.createCell(6)
						.setCellValue(
								(row.get("pre_per") instanceof BigDecimal ? ((BigDecimal) row.get("pre_per")).doubleValue() : (Double) row
										.get("pre_per")));
				sh.getRow(2 + i).getCell(4).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(5).setCellStyle(getStyle6(wb));
				sh.getRow(2 + i).getCell(6).setCellStyle(getStyle4(wb));

				sh.getRow(2 + i).createCell(7).setCellValue((String) row.get("prvd_nm"));
				sh.getRow(2 + i).createCell(8).setCellValue((Integer) row.get("ppre_aud_trm_cnt"));
				sh.getRow(2 + i)
						.createCell(9)
						.setCellValue(
								(row.get("ppre_per") instanceof BigDecimal ? ((BigDecimal) row.get("ppre_per")).doubleValue() : (Double) row
										.get("ppre_per")));
				sh.getRow(2 + i).getCell(7).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(8).setCellStyle(getStyle6(wb));
				sh.getRow(2 + i).getCell(9).setCellStyle(getStyle4(wb));

				sh.getRow(2 + i).createCell(10).setCellValue((String) row.get("prvd_nm"));
				sh.getRow(2 + i).createCell(11).setCellValue((Integer) row.get("pppre_aud_trm_cnt"));
				sh.getRow(2 + i)
						.createCell(12)
						.setCellValue(
								(row.get("pppre_per") instanceof BigDecimal ? ((BigDecimal) row.get("pppre_per")).doubleValue() : (Double) row
										.get("pppre_per")));
				sh.getRow(2 + i).getCell(10).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(11).setCellStyle(getStyle6(wb));
				sh.getRow(2 + i).getCell(12).setCellStyle(getStyle4(wb));

				sh.getRow(2 + i).createCell(13).setCellValue((String) row.get("prvd_nm"));
				sh.getRow(2 + i).createCell(14).setCellValue((Integer) row.get("ppppre_aud_trm_cnt"));
				sh.getRow(2 + i).createCell(15).setCellValue("");
				sh.getRow(2 + i).getCell(13).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(14).setCellStyle(getStyle6(wb));
				sh.getRow(2 + i).getCell(15).setCellStyle(getStyle3(wb));
			}
		}
		//generate(wb, "有价卡违规（06045有价卡状态变更记录(CRM)入库情况）" + mon.get("aud_trm") + ".xlsx");
	}

	public void writeYjk01(List<Map<String, Object>> data, Map<String, Object> mon,XSSFWorkbook wb) throws Exception {
		//wb = new XSSFWorkbook();
		sh = wb.createSheet("06049有价卡状态变更记录(VC)入库情况");
		sh.createRow(0);
		sh.getRow(0).createCell(0).setCellValue("省份编码");
		sh.getRow(0).getCell(0).setCellStyle(getStyle2(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sh.getRow(0).createCell(1).setCellValue(mon.get("month") + "月");
		sh.getRow(0).getCell(1).setCellStyle(getStyle0(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
		sh.getRow(0).createCell(4).setCellValue(mon.get("pmonth") + "月");
		sh.getRow(0).getCell(4).setCellStyle(getStyle0(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 4, 6));
		sh.getRow(0).createCell(7).setCellValue(mon.get("ppmonth") + "月");
		sh.getRow(0).getCell(7).setCellStyle(getStyle0(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 7, 9));
		sh.getRow(0).createCell(10).setCellValue(mon.get("pppmonth") + "月");
		sh.getRow(0).getCell(10).setCellStyle(getStyle0(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 10, 12));
		sh.getRow(0).createCell(13).setCellValue(mon.get("ppppmonth") + "月");
		sh.getRow(0).getCell(13).setCellStyle(getStyle0(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 13, 15));
		sh.createRow(1);
		sh.getRow(1).createCell(1).setCellValue("省份名称");
		sh.getRow(1).createCell(2).setCellValue("记录数");
		sh.getRow(1).createCell(3).setCellValue("环比上月增幅");
		sh.getRow(1).createCell(4).setCellValue("省份名称");
		sh.getRow(1).createCell(5).setCellValue("记录数");
		sh.getRow(1).createCell(6).setCellValue("环比上月增幅");
		sh.getRow(1).createCell(7).setCellValue("省份名称");
		sh.getRow(1).createCell(8).setCellValue("记录数");
		sh.getRow(1).createCell(9).setCellValue("环比上月增幅");
		sh.getRow(1).createCell(10).setCellValue("省份名称");
		sh.getRow(1).createCell(11).setCellValue("记录数");
		sh.getRow(1).createCell(12).setCellValue("环比上月增幅");
		sh.getRow(1).createCell(13).setCellValue("省份名称");
		sh.getRow(1).createCell(14).setCellValue("记录数");
		sh.getRow(1).createCell(15).setCellValue("环比上月增幅");
		sh.createFreezePane(0, 2, 0, 2);
		for (int i = 1; i <= 15; i++) {
			sh.setColumnWidth(i, 256 * 13);
			sh.getRow(1).getCell(i).setCellStyle(getStyle1(wb));
		}
		sh.getRow(0).setHeightInPoints(16);
		sh.getRow(1).setHeightInPoints(16);
		for (int i = 0; i < data.size(); i++) {
			sh.createRow(2 + i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				sh.getRow(2 + i).createCell(0).setCellValue((Integer) row.get("prvd_id"));
				sh.getRow(2 + i).getCell(0).setCellStyle(getStyle3(wb));

				sh.getRow(2 + i).createCell(1).setCellValue((String) row.get("prvd_nm"));
				sh.getRow(2 + i).createCell(2).setCellValue((Integer) row.get("aud_trm_cnt"));
				sh.getRow(2 + i).createCell(3)
						.setCellValue((row.get("per") instanceof BigDecimal ? ((BigDecimal) row.get("per")).doubleValue() : (Double) row.get("per")));
				sh.getRow(2 + i).getCell(1).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(2).setCellStyle(getStyle6(wb));
				sh.getRow(2 + i).getCell(3).setCellStyle(getStyle4(wb));

				sh.getRow(2 + i).createCell(4).setCellValue((String) row.get("prvd_nm"));
				sh.getRow(2 + i).createCell(5).setCellValue((Integer) row.get("pre_aud_trm_cnt"));
				sh.getRow(2 + i)
						.createCell(6)
						.setCellValue(
								(row.get("pre_per") instanceof BigDecimal ? ((BigDecimal) row.get("pre_per")).doubleValue() : (Double) row
										.get("pre_per")));
				sh.getRow(2 + i).getCell(4).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(5).setCellStyle(getStyle6(wb));
				sh.getRow(2 + i).getCell(6).setCellStyle(getStyle4(wb));

				sh.getRow(2 + i).createCell(7).setCellValue((String) row.get("prvd_nm"));
				sh.getRow(2 + i).createCell(8).setCellValue((Integer) row.get("ppre_aud_trm_cnt"));
				sh.getRow(2 + i)
						.createCell(9)
						.setCellValue(
								(row.get("ppre_per") instanceof BigDecimal ? ((BigDecimal) row.get("ppre_per")).doubleValue() : (Double) row
										.get("ppre_per")));
				sh.getRow(2 + i).getCell(7).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(8).setCellStyle(getStyle6(wb));
				sh.getRow(2 + i).getCell(9).setCellStyle(getStyle4(wb));

				sh.getRow(2 + i).createCell(10).setCellValue((String) row.get("prvd_nm"));
				sh.getRow(2 + i).createCell(11).setCellValue((Integer) row.get("pppre_aud_trm_cnt"));
				sh.getRow(2 + i)
						.createCell(12)
						.setCellValue(
								(row.get("pppre_per") instanceof BigDecimal ? ((BigDecimal) row.get("pppre_per")).doubleValue() : (Double) row
										.get("pppre_per")));
				sh.getRow(2 + i).getCell(10).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(11).setCellStyle(getStyle6(wb));
				sh.getRow(2 + i).getCell(12).setCellStyle(getStyle4(wb));

				sh.getRow(2 + i).createCell(13).setCellValue((String) row.get("prvd_nm"));
				sh.getRow(2 + i).createCell(14).setCellValue((Integer) row.get("ppppre_aud_trm_cnt"));
				sh.getRow(2 + i).createCell(15).setCellValue("");
				sh.getRow(2 + i).getCell(13).setCellStyle(getStyle3(wb));
				sh.getRow(2 + i).getCell(14).setCellStyle(getStyle6(wb));
				sh.getRow(2 + i).getCell(15).setCellStyle(getStyle3(wb));
			}
		}
		//generate(wb, "有价卡违规（06049有价卡状态变更记录(VC)入库情况）" + mon.get("aud_trm") + ".xlsx");
	}

	private CellStyle getStyle0(XSSFWorkbook wb) {//靠左显示
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_LEFT);
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
		// style.setFillBackgroundColor(HSSFColor.BLUE.index);
		style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return style;
	}
	
	private CellStyle getStyle1(XSSFWorkbook wb) {
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
		// style.setFillBackgroundColor(HSSFColor.BLUE.index);
		style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return style;
	}

	private CellStyle getStyle2(XSSFWorkbook wb) {
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
		// style.setFillBackgroundColor(HSSFColor.BLUE.index);
		style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return style;
	}

	private CellStyle getStyle3(XSSFWorkbook wb) {
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
		return style;
	}

	private CellStyle getStyle4(XSSFWorkbook wb) {
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

	private CellStyle getStyle5(XSSFWorkbook wb) {
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

	private CellStyle getStyle6(XSSFWorkbook wb) {
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
	private CellStyle getStyle7(XSSFWorkbook wb) {
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
		style.setDataFormat(wb.createDataFormat().getFormat("¥#,##0.00"));
		return style;
	}

	public void generate(XSSFWorkbook wb, String fileName,String audTrm,String focusCd,String auditNm,Boolean isTol) throws Exception {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(this.getLocalPath(fileName));
			wb.write(out);
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
		if(!audTrm.equals("")&&!focusCd.equals("")&&!auditNm.equals("")&&isTol!=null){
			uploadFile(this.getLocalPath(fileName), getFtpPath( audTrm, focusCd));
			sjzlService.updateFileStatus(audTrm,focusCd,auditNm,buildDownloadUrl(audTrm,focusCd,fileName),isTol);
		}
		
	}
	private FtpUtil initFtp() {
		String isTransferFile = propertyUtil.getPropValue("isTransferFile");
		if (!"true".equalsIgnoreCase(isTransferFile)) {
			return null;
		}
		FtpUtil ftpTool = new FtpUtil();
		String ftpServer = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpServer"));
		String ftpPort = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpPort"));
		String ftpUser = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpUser"));
		String ftpPass = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpPass"));
		ftpTool.initClient(ftpServer, ftpPort, ftpUser, ftpPass);
		return ftpTool;
	}
	
	public void uploadFile(String filePath, String ftpPath) throws Exception{
		FtpUtil ftpTool = null;
		try {
			ftpTool = initFtp();
			if (ftpTool == null) {
				return;
			}
			ftpTool.uploadFile(new File(filePath), ftpPath);
		} catch (Exception e){ 
			logger.error("SJZL uploadFile>>>"+e.getMessage(),e);
			throw e;
			//e.printStackTrace();
			
		} finally {
			if (ftpTool != null) {
				ftpTool.disConnect();
			}
		}
	}

	public XSSFWorkbook openExcel(String fileName,String sheetName) throws Exception {
		FileInputStream fIP = null;
		File file = null;
		File file1 = null;
		XSSFWorkbook workbook = null;
		try {
			file = new File(this.getLocalPath(fileName));
			if (!file.exists()){
				XSSFWorkbook wb = new XSSFWorkbook();
				wb.createSheet(sheetName);
				generate(wb,fileName,"","","",null);
				file1 = new File(this.getLocalPath(fileName));
				fIP = new FileInputStream(file1);
			}else {
			fIP = new FileInputStream(file);
			}
			// Get the workbook instance for XLSX file
			workbook = new XSSFWorkbook(fIP);
		} 
		catch (Exception e) {
			throw e;
		} finally {
			fIP.close();			
		}
		return workbook;
	}
	public String getFtpPath(String audTrm,String focusCd){
		String tempDir = propertyUtil.getPropValue("ftpPath");
		String finalPath = FileUtil.buildFullFilePath(tempDir, buildRelativePath(audTrm, focusCd));
		FileUtil.mkdirs(finalPath);
		return finalPath;// + "/" + zipFileName;
	}
	protected String buildRelativePath(String audTrm, String focusCd) {
		String subjectId = focusCd.substring(0,1);
		StringBuilder path = new StringBuilder();
		path.append(audTrm).append("/").append(subjectId).append("/").append(focusCd);
		return path.toString();
	}
	protected String buildDownloadUrl(String audTrm, String focusCd,String fileName) {
		String ftpHttpUrlPrefix = propertyUtil.getPropValue("ftpHttpUrlPrefix");
		StringBuilder url = new StringBuilder(30);
		url.append(buildRelativePath(audTrm, focusCd)).append("/").append(fileName);
		return FileUtil.buildFullFilePath(ftpHttpUrlPrefix, url.toString());
	}
	public String getLocalDir() {
		String tempDir = propertyUtil.getPropValue("tempDir");
		FileUtil.mkdirs(tempDir);
		return propertyUtil.getPropValue("tempDir");
	}

	public String getLocalPath(String fileName) {
		return getLocalDir() + '/' + fileName;
	}

	/**
	 * @return the auditNmList
	 */
	public List<String> getAuditNmList() {
		return this.auditNmList;
	}

	/**
	 * @param auditNmList the auditNmList to set
	 */
	public void setAuditNmList(List<String> auditNmList) {
		this.auditNmList = auditNmList;
	}

}
