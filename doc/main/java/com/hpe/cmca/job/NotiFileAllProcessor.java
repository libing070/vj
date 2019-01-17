package com.hpe.cmca.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.finals.StringUtils;
import com.hpe.cmca.service.SJZLService;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;

@Service("NotiFileAllProcessor")
public class NotiFileAllProcessor extends  BaseObject{
	@Autowired
	private SJZLService		sjzlService;
	protected XSSFWorkbook	wb;
//	protected XSSFWorkbook	wbYjk;
//	protected XSSFWorkbook	wbQdyk;
//	protected XSSFWorkbook	wbZdtl;
//	protected XSSFWorkbook	wbKhqf;
//	protected XSSFWorkbook	wbYccz;
	@Autowired
	private JdbcTemplate jdbcTemplate = null;
	
	class confInfo{
		Integer id = null;
		Integer checkId = null;
		String checkCode = null;
		String checkName = null;
		Integer subjectId = null;
		String fileName = null;
		String sheetName = null;
		String titleName = null;
		String[] colName = null;
		String[] colNameLength = null;
		Integer colNum = null;
		String querySql = null;
		String[] fieldName = null;
		String[] fieldType = null;
		Integer checkType = null;
		String audTrm = null;
		Integer status = null;		
		String update_time = null;
	}
	
	
	public void work()throws Exception{
		//this.setFileName("社会渠道终端套利排名汇总");
		
		List<Map<String, Object>> reqList = sjzlService.getSJZLGenList();
		if(reqList.isEmpty()){
			logger.error("SJZL没有文件生成请求");
			return;
		}
		
		for (Map<String, Object> req : reqList){
			final confInfo ci = new confInfo();			
			wb  = new XSSFWorkbook();
			final List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			ci.audTrm=(String)req.get("aud_trm");
			ci.checkId=(Integer)req.get("check_id");
			ci.checkCode=(String)req.get("check_code");
			ci.checkName=(String)req.get("check_name");
			ci.fileName=(String)req.get("file_name");
			ci.sheetName=(String)req.get("sheet_name");
			ci.titleName=(String)req.get("title_name");
			ci.subjectId=(Integer)req.get("subject_id");
			ci.colName = ((String)req.get("col_name")).split(",");
			ci.colNameLength  = ((String)req.get("col_name_length")).split(",");			
			ci.colNum=(Integer)req.get("col_num");			
			ci.querySql=(String)req.get("query_sql");
			ci.fieldName = ((String)req.get("field_name")).split(",");
			ci.fieldType = ((String)req.get("field_type")).split(",");
			try{
			jdbcTemplate.query(ci.querySql, new Object[] { ci.audTrm }, new RowCallbackHandler() {

				public void processRow(ResultSet rs) throws SQLException {
					int columCount = rs.getMetaData().getColumnCount();
					Map<String, Object> tpMap = new HashMap<String, Object>();
					for (int i = 0; i < columCount; i++) {
						tpMap.put(ci.fieldName[i], rs.getObject(i+1));						
					}
					dataList.add(tpMap);
				}
			});	
			}
			catch(Exception e){
				logger.error("SJZL查询数据库异常");
				logger.error(e.getMessage(),e);
			}
			try{
				writeFile(wb,dataList,ci);
				generateSingle(wb,ci);
				
				if(ci.subjectId == 1){
					XSSFWorkbook	wb = openExcel("有价卡专题数据质量稽核_"+ci.audTrm+".xlsx",ci.sheetName);
					int sheetIndex = wb.getSheetIndex(ci.sheetName);
					if(sheetIndex>=0)wb.removeSheetAt(sheetIndex);
					writeFile(wb,dataList,ci);
					generateSubject(wb,ci,"有价卡专题数据质量稽核_"+ci.audTrm+".xlsx");
				}
				if(ci.subjectId == 2){
					XSSFWorkbook	wb = openExcel("渠道养卡专题数据质量稽核_"+ci.audTrm+".xlsx",ci.sheetName);
					int sheetIndex = wb.getSheetIndex(ci.sheetName);
					if(sheetIndex>=0)wb.removeSheetAt(sheetIndex);
					writeFile(wb,dataList,ci);
					generateSubject(wb,ci,"渠道养卡专题数据质量稽核_"+ci.audTrm+".xlsx");
				}
				if(ci.subjectId == 3){
					XSSFWorkbook	wb = openExcel("终端套利专题数据质量稽核_"+ci.audTrm+".xlsx",ci.sheetName);
					int sheetIndex = wb.getSheetIndex(ci.sheetName);
					if(sheetIndex>=0)wb.removeSheetAt(sheetIndex);
					writeFile(wb,dataList,ci);
					generateSubject(wb,ci,"终端套利专题数据质量稽核_"+ci.audTrm+".xlsx");
				}
				if(ci.subjectId == 4){
					XSSFWorkbook	wb = openExcel("客户欠费专题数据质量稽核_"+ci.audTrm+".xlsx",ci.sheetName);
					int sheetIndex = wb.getSheetIndex(ci.sheetName);
					if(sheetIndex>=0)wb.removeSheetAt(sheetIndex);
					writeFile(wb,dataList,ci);
					generateSubject(wb,ci,"客户欠费专题数据质量稽核_"+ci.audTrm+".xlsx");
				}
				if(ci.subjectId == 5){
					XSSFWorkbook	wb = openExcel("员工异常操作专题数据质量稽核_"+ci.audTrm+".xlsx",ci.sheetName);
					int sheetIndex = wb.getSheetIndex(ci.sheetName);
					if(sheetIndex>=0)wb.removeSheetAt(sheetIndex);
					writeFile(wb,dataList,ci);
					generateSubject(wb,ci,"员工异常操作专题数据质量稽核_"+ci.audTrm+".xlsx");
				}
				
				
			}
			catch(Exception e){
				logger.error("SJZL文件生成异常");
				e.printStackTrace();
				logger.error(e.getMessage(),e);
			}
		}

		
	}
public void writeFile(XSSFWorkbook	wb,List<Map<String, Object>> data, confInfo ci){
		Sheet sh = wb.createSheet(ci.sheetName);
		sh.createRow(0).createCell(0).setCellValue(ci.titleName);
		sh.getRow(0).getCell(0).setCellStyle(getStyle0(wb));
		sh.addMergedRegion(new CellRangeAddress(0, 0, 0, ci.colNum-1)); 
		sh.createRow(1);
		for (int i = 0; i < ci.colName.length; i++) {
			sh.getRow(1).createCell(i).setCellValue(ci.colName[i]);
			sh.setColumnWidth(i, 256*4*Integer.parseInt(ci.colNameLength[i]));
			if (i%2==0) {
				sh.getRow(1).getCell(i).setCellStyle(getStyle1(wb));
			} else {
				sh.getRow(1).getCell(i).setCellStyle(getStyle2(wb));
			}
		}
		sh.getRow(1).setHeightInPoints(32);
		sh.createFreezePane(0, 2, 0, 2);
		for(int i = 0; i <= data.size(); i++) {
			sh.createRow(2+i);
			if (i < data.size()) {
				Map<String, Object> row = data.get(i);
				for(int ii=0;ii<ci.colNum;ii++){
					if("0".equals(ci.fieldType[ii])){
						sh.getRow(2+i).createCell(ii).setCellValue((Integer) row.get(ci.fieldName[ii]));
						sh.getRow(2+i).getCell(ii).setCellStyle(getStyle6(wb));
					}
					if("1".equals(ci.fieldType[ii])){
						sh.getRow(2+i).createCell(ii).setCellValue(row.get(ci.fieldName[ii]) instanceof BigDecimal ? ((BigDecimal) row.get(ci.fieldName[ii])).doubleValue():(Double) row.get(ci.fieldName[ii]));
						sh.getRow(2+i).getCell(ii).setCellStyle(getStyle5(wb));
					}
					if("2".equals(ci.fieldType[ii])){
						sh.getRow(2+i).createCell(ii).setCellValue((String) row.get(ci.fieldName[ii]));
						sh.getRow(2+i).getCell(ii).setCellStyle(getStyle3(wb));
					}
					if("3".equals(ci.fieldType[ii])){
						sh.getRow(2+i).createCell(ii).setCellValue(row.get(ci.fieldName[ii]) instanceof BigDecimal ? ((BigDecimal) row.get(ci.fieldName[ii])).doubleValue():(Double) row.get(ci.fieldName[ii]));
						sh.getRow(2+i).getCell(ii).setCellStyle(getStyle4(wb));
					}

				}
		}
	}
}

	
	private CellStyle getStyle0(XSSFWorkbook	wb){
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
	
	private CellStyle getStyle1(XSSFWorkbook	wb){
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
	
	private CellStyle getStyle2(XSSFWorkbook	wb){
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
	
	private CellStyle getStyle3(XSSFWorkbook	wb){
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
	private CellStyle getStyle4(XSSFWorkbook	wb){
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
	
	private CellStyle getStyle5(XSSFWorkbook	wb){
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
	
	private CellStyle getStyle6(XSSFWorkbook	wb){
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

	public void generateSubject(XSSFWorkbook wb, confInfo ci,String fileName) throws Exception {
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
			uploadFile(this.getLocalPath(fileName), getFtpPath( ci.audTrm, String.valueOf(ci.subjectId*1000)));
			if(ci.subjectId == 1)
				sjzlService.updateStatus( ci.audTrm,ci.subjectId,1000,"YJK","有价卡专题全部稽核点",buildDownloadUrl(ci.audTrm,String.valueOf(ci.subjectId*1000),fileName));	
			if(ci.subjectId == 2)
				sjzlService.updateStatus( ci.audTrm,ci.subjectId,2000,"QDYK","渠道养卡专题全部稽核点",buildDownloadUrl(ci.audTrm,String.valueOf(ci.subjectId*1000),fileName));		
			if(ci.subjectId == 3)
				sjzlService.updateStatus( ci.audTrm,ci.subjectId,3000,"ZDTL","终端套利专题全部稽核点",buildDownloadUrl(ci.audTrm,String.valueOf(ci.subjectId*1000),fileName));		
			if(ci.subjectId == 4)
				sjzlService.updateStatus( ci.audTrm,ci.subjectId,4000,"KHQF","客户欠费专题全部稽核点",buildDownloadUrl(ci.audTrm,String.valueOf(ci.subjectId*1000),fileName));		
			if(ci.subjectId == 5)
				sjzlService.updateStatus( ci.audTrm,ci.subjectId,5000,"YCCZ","员工异常操作专题全部稽核点",buildDownloadUrl(ci.audTrm,String.valueOf(ci.subjectId*1000),fileName));		
	}
	
	public void generateSingle(XSSFWorkbook wb, confInfo ci) throws Exception {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(this.getLocalPath(ci.fileName+"_"+ci.audTrm+".xlsx"));
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
			uploadFile(this.getLocalPath(ci.fileName+"_"+ci.audTrm+".xlsx"), getFtpPath( ci.audTrm, String.valueOf(ci.subjectId*1000)));
			sjzlService.updateStatus( ci.audTrm,ci.subjectId,ci.checkId,ci.checkCode,ci.checkName,buildDownloadUrl(ci.audTrm,String.valueOf(ci.subjectId*1000),ci.fileName+"_"+ci.audTrm+".xlsx"));		
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
				generate(wb,fileName);
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
	
	public void generate(XSSFWorkbook wb, String fileName) throws Exception {
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

}
