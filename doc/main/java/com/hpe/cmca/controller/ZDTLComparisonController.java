package com.hpe.cmca.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hpe.cmca.service.ComparisonService;

/**
 * 
 * <pre>
 * Desc： 终端套利 比对
 * @author jh
 * @refactor jh
 * @date   2017-6-7 下午2:34:45
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-6-7 	   jh 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/zdtlCopm")
public class ZDTLComparisonController {
	
	//文档存放目录路径
	private final static String FILE_PATH = "C:/Users/jh/Desktop/审计报告/终端套利/社会渠道终端套利排名汇总-201701.xlsx";
	
	@Autowired
	private ComparisonService comparisonService;
	
	@RequestMapping("/match")
	public String match(){
		
		//获取排名汇总文件下的所有sheet的数据
		List<Map<String, Object>> allSheetList = getAllSheetData();
		//获取审计报告的所有数据
		List<Map<String, Object>> reportList = getReportList();
		
		//TODO 继续allSheetList、reportList数据比对，不同数据在Excel标记并输出txt文件
		
		
		
		
		
		
		
		
		
		
		
		
		return "";
	}

	//获取审计报告的所有数据
	private List<Map<String, Object>> getReportList() {
		Map<String, Object> params = new HashMap<String, Object>();
		//通过Excel名称获取审计月, 通过审计月和文档类型获取审计报告的数据集合
		Pattern p=Pattern.compile("-(\\w+).");
	    Matcher m=p.matcher(FILE_PATH);
	    while(m.find()){
	        params.put("audTrm",m.group(1));
	    }
	    params.put("reportType", "终端套利");
	    List<Map<String, Object>> reportList = comparisonService.getQDYKList(params);
		return reportList;
	}
	
	private List<Map<String, Object>> getAllSheetData() {
		//获取Excel文件下的所有sheet
		List<Map<String, Object>> allSheetList = new ArrayList<Map<String,Object>>();
		Map<String, Object> sheetMap = new HashMap<String, Object>();
		//根据上述创建的输入流 创建工作簿对象 
		FileInputStream excelFileInputStream;
		try {
			excelFileInputStream = new FileInputStream(FILE_PATH);
			// XSSFWorkbook 就代表一个 Excel 文件
			// 创建其对象，就打开这个 Excel 文件
			XSSFWorkbook workbook = new XSSFWorkbook(excelFileInputStream);
			//得到第一页 sheet 
			//页Sheet是从0开始索引的 
			for(int i=0;i<5;i++){
				Sheet sheet = workbook.getSheetAt(i);
				List<Map<String, Object>> sheetOneRow = new ArrayList<Map<String,Object>>();
				if(i == 0){
					for(int j = 2;j<34;j++){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("row", j);
						map.put("prvdName", sheet.getRow(j).getCell(1));
						map.put("zdxsNum", sheet.getRow(j).getCell(2));
						map.put("errXsNum", sheet.getRow(j).getCell(4));
						map.put("errXsPer", sheet.getRow(j).getCell(6));
						map.put("errChnl", sheet.getRow(j).getCell(5));
						map.put("tlNum", sheet.getRow(j).getCell(8));
						map.put("tlPer", sheet.getRow(j).getCell(11));
						map.put("tlChnl", sheet.getRow(j).getCell(10));
						map.put("zdChnl", sheet.getRow(j).getCell(3));
						sheetOneRow.add(map);
					}
				}
				if(i==1){
					for(int j = 2;j<34;j++){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("row", j);
						map.put("prvdName", sheet.getRow(j).getCell(1));
						map.put("zdxsNum", sheet.getRow(j).getCell(2));
						map.put("tlChnl", sheet.getRow(j).getCell(4));
						sheetOneRow.add(map);
					}
				}
				if(i==2){
					for(int j = 2;j<34;j++){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("row", j);
						map.put("prvdName", sheet.getRow(j).getCell(1));
						map.put("zdxsNum", sheet.getRow(j).getCell(2));
						map.put("tlChnl", sheet.getRow(j).getCell(4));
						sheetOneRow.add(map);
					}
				}
				if(i==3){
					for(int j = 2;j<34;j++){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("row", j);
						map.put("prvdName", sheet.getRow(j).getCell(1));
						map.put("cbNum", sheet.getRow(j).getCell(2));
						map.put("cbChnl", sheet.getRow(j).getCell(3));
						map.put("tlNum", sheet.getRow(j).getCell(4));
						map.put("tlChnl", sheet.getRow(j).getCell(6));
						sheetOneRow.add(map);
					}
				}
				if(i==4){
					for(int j = 2;j<34;j++){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("row", j);
						map.put("prvdName", sheet.getRow(j).getCell(1));
						map.put("chNum", sheet.getRow(j).getCell(2));
						map.put("chChnl", sheet.getRow(j).getCell(3));
						map.put("tlNum", sheet.getRow(j).getCell(4));
						map.put("tlChnl", sheet.getRow(j).getCell(6));
						sheetOneRow.add(map);
					}
				}
				sheetMap.put("sheetName", sheet.getSheetName());
				sheetMap.put("sheetData", sheetOneRow);
				allSheetList.add(sheetMap);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return allSheetList;
	}
}
