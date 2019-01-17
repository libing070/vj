package com.hpe.cmwa.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.IOUtils;

/**
 * csv工具类
 * 
 * <pre>
 * Desc： 
 * @author   wangpeng
 * @refactor wangpeng
 * @date     2016-11-22 下午7:04:19
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2016-11-22 	   wangpeng 	         1. Created this class.
 * </pre>
 */
public class CSVUtils {

    /**
     * 创建CSV文件
     * 
     * <pre>
     * Desc  
     * @param exportData
     * @param rowMapper
     * @param outPutPath
     * @param filename
     * @return
     * @author wangpeng
     * 2016-11-23 下午3:43:51
     * </pre>
     */
    public static File createCSVFile(List exportData, Map rowMapper, String outPutPath, String filename) {

	File csvFile = null;
	BufferedWriter csvFileOutputStream = null;
	try {
	    csvFile = new File(outPutPath + filename + ".csv");
	    // csvFile.getParentFile().mkdir();
	    File parent = csvFile.getParentFile();
	    if (parent != null && !parent.exists()) {
		parent.mkdirs();
	    }
	    csvFile.createNewFile();

	    // GB2312使正确读取分隔符","
	    csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 1024);
	    // 写入文件头部
	    for (Iterator propertyIterator = rowMapper.entrySet().iterator(); propertyIterator.hasNext();) {
		java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
		csvFileOutputStream.write("\"" + propertyEntry.getValue().toString() + "\"");
		if (propertyIterator.hasNext()) {
		    csvFileOutputStream.write(",");
		}
	    }
	    csvFileOutputStream.newLine();

	    // 写入文件内容
	    for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {
		LinkedHashMap row = (LinkedHashMap) iterator.next();

		for (Iterator propertyIterator = row.entrySet().iterator(); propertyIterator.hasNext();) {
		    java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
		    csvFileOutputStream.write("\"" + propertyEntry.getValue() + "\"");
		    if (propertyIterator.hasNext()) {
		    	csvFileOutputStream.write(",");
		    }
		}
		if (iterator.hasNext()) {
		    csvFileOutputStream.newLine();
		}
	    }
	    csvFileOutputStream.flush();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		csvFileOutputStream.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return csvFile;
    }

    public static void exportCSV(Object columnName[], List<Map<String, Object>> dataList, HttpServletRequest request, HttpServletResponse response, String fileName) {
	String columns = getTableColumnName(columnName);
	String data = buildDataForCSV(columnName, dataList);

	if (fileName == null || "".equals(fileName))

	    response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-Disposition", (new StringBuilder()).append("attachment; filename=\"").append(fileName).append(".csv").append("\" ").toString());
	response.setHeader("Content-Transfer-Encoding", "binary");
	response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
	response.setHeader("Pragma", "public");

	try {
	    java.io.OutputStream fos = response.getOutputStream();
	    OutputStreamWriter writer = new OutputStreamWriter(fos, "gbk");
	    BufferedWriter bw = new BufferedWriter(writer);
	    bw.write((new StringBuilder()).append(columns).append(data).toString());
	    bw.newLine();
	    bw.flush();
	    bw.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * 下载CSV文件
     * 
     * <pre>
     * Desc  
     * @param response
     * @param csvFilePath
     * @param fileName
     * @throws IOException
     * @author wangpeng
     * 2016-11-23 下午3:44:19
     * </pre>
     */
    public static void exportFile(HttpServletResponse response, String csvFilePath, String fileName) throws IOException {
	String a = URLEncoder.encode(fileName, "UTF-8");
	System.out.println(a);
	response.setContentType("applicatoin/octet-stream;charset=UTF-8");
	response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(fileName, "UTF-8")));
	File file = new File(csvFilePath);
	InputStream inputStream = new FileInputStream(file);
	IOUtils.copy((InputStream) inputStream, response.getOutputStream());
    }

    public static String getTableColumnName(Object columnName[]) {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < columnName.length; i++)
	    if (i != columnName.length - 1)
		sb.append((new StringBuilder()).append("\"").append(columnName[i]).append("\",").toString());
	    else
		sb.append((new StringBuilder()).append("\"").append(columnName[i]).append("\"").toString());

	sb.append("\n");
	return sb.toString();
    }

    private static String buildDataForCSV(Object columnName[], List dataList) {
	StringBuilder strb = new StringBuilder();
	for (Iterator i$ = dataList.iterator(); i$.hasNext(); strb.append("\n")) {
	    Map map = (Map) i$.next();
	    for (int i = 0; i < columnName.length; i++)
		if (i != columnName.length - 1)
		    strb.append((new StringBuilder()).append("\"").append(map.get(columnName[i])).append("\",").toString());
		else
		    strb.append((new StringBuilder()).append("\"").append(map.get(columnName[i])).append("\"").toString());

	}

	return strb.toString();
    }

    /**
     * 最新版导出
     * <pre>
     * Desc  
     * @param fileName
     * @param dataList
     * @param rowMapper
     * @param request
     * @param response
     * @param parameterMap
     * @throws Exception
     * @author wangpeng
     * 2016-12-7 上午10:18:14
     * </pre>
     */
    public static void exportCSVList(String fileName, List dataList, Map rowMapper, HttpServletRequest request, HttpServletResponse response) throws Exception {
	setFileDownloadHeader(request, response, fileName);
	PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"));
	StringBuffer sb = new StringBuffer();

	for (Iterator propertyIterator = rowMapper.entrySet().iterator(); propertyIterator.hasNext();) {
	    java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
	    sb.append("\"" + propertyEntry.getValue().toString() + "\"");
	    if (propertyIterator.hasNext()) {
		sb.append(",");
	    }
	}
	sb.append("\n");
	if(dataList.size()==0){
		out.println(sb.toString());
		sb.delete(0, sb.length());
	}

		for (Iterator iterator = dataList.iterator(); iterator.hasNext();) {
			LinkedHashMap row = (LinkedHashMap) iterator.next();

			for (Iterator propertyIterator = row.entrySet().iterator(); propertyIterator.hasNext();) {
				java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
				sb.append("\""+ HelperString.objectConvertString(propertyEntry.getValue()) + "\"");
				if (propertyIterator.hasNext()) {
					sb.append("\t,");
				}
			}
			sb.append("\n");
			out.write(sb.toString());
			sb.delete(0, sb.length());
		}

	out.flush();
	out.close();
    }

    public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
	final String userAgent = request.getHeader("USER-AGENT");
	try {
	    String finalFileName = null;
	    if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent, "Trident")) {// IE浏览器
		finalFileName = URLEncoder.encode(fileName, "UTF8");
	    } else if (StringUtils.contains(userAgent, "Firefox")) {// 火狐浏览器 google
		finalFileName = new String(fileName.getBytes(), "ISO8859-1");
	    } else {
		finalFileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
	    }

	    // 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + finalFileName + "\"");
	    response.setContentType("application/octet-stream;charset=utf-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
    }
}
