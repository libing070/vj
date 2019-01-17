package com.hpe.cmca.job;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.service.NotiFileGenService;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;

@Service
public abstract class AbstractNotiFileProcessor{
	@Autowired
	public NotiFileGenService notiFileGenService	= null;
	
	protected String fileName;
	
	protected String path = null;
	
	protected String month;
	
	protected String focusCd;
	
	protected Map<String, Object> data = null;
	
	protected String localDir = null;
	
	protected XSSFWorkbook wb;
	protected Sheet sh;
	
	protected String buildFileName() {
		fileName = fileName + "-" + month + ".xlsx";
		return fileName;
	}
	
	public abstract boolean generate() throws Exception;
	
	public void start() throws Exception{
		wb = new XSSFWorkbook();
		boolean b = this.generate();
		if(!b)
		    return;
		FileOutputStream out = null;
		try {  
			out = new FileOutputStream(this.getLocalPath());  
		    wb.write(out);
		} catch (IOException e) {  
		    throw e;
		} catch (Exception e) {  
		    throw e;
		}finally {  
		    try {  
		        out.close();  
		    } catch (IOException e) {  
		    	 throw e;  
		    }  
		} 
	}
	
	public void setLocalDir(String localDir) {
		this.localDir = localDir;
	}
	
	public String getLocalDir() {
		return this.localDir;
	}

	public String getLocalPath() {
		return getLocalDir() + '/' + this.buildFileName();
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFocusCd() {
		return focusCd;
	}

	public void setFocusCd(String focusCd) {
		this.focusCd = focusCd;
	}
	public ArrayList<String> getFileNameList() {
		return new ArrayList<String>();
	}
	
	
}
