package com.hpe.cmwa.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class HelperIO {
	public static void outPutExcel(String fileStr,HSSFWorkbook wb,HttpServletResponse response) throws Exception{
		File file = new File(fileStr);
		// 第六步，将文件写入

		OutputStream os = new FileOutputStream(file);
		wb.write(os);
		os.close();
		response.setContentType("application/msexcel;charset=UTF-8");
		String fileName = file.getName();
		String nowPath = file.getPath();
		File file1 = new File(nowPath);
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename="
				.concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
		response.addHeader("Content-Length", "" + file1.length());
		response.setHeader("Connection", "close");
		response.setHeader("Content-Type", "application/vnd.ms-excel");
		// 以流的形式下载文件
		try {
			InputStream fis = new BufferedInputStream(new FileInputStream(
					nowPath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			OutputStream outPutStream = new BufferedOutputStream(
					response.getOutputStream());
			outPutStream.write(buffer);
			outPutStream.flush();
			outPutStream.close();
		} catch (Exception e) {
			throw e;
		}
	}
}
