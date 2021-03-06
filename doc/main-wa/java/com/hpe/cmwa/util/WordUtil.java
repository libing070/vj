/**
 * com.hp.base.util.WordUtil.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.log4j.Logger;


import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * <pre>
 * Desc： 
 * @author peter.fu
 * @refactor peter.fu
 * @date   Jan 20, 2015 3:47:25 PM
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Jan 20, 2015 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
public class WordUtil {

	protected Logger logger = Logger.getLogger(this.getClass());
	
	public  File write(String templatePath, String templateName, Map<String, Object> dataMap, String toPath,String toFileName) throws IOException {
		
		
	 
		String fullPath = FileUtil.buildFullFilePath(toPath, toFileName);
		return write(templatePath,templateName,dataMap,fullPath);
	}
	/**
	 * <pre>
	 * Desc  输出文件
	 * @param templatePath
	 * @param templateName
	 * @param dataMap
	 * @param toFullPathFile
	 * @throws IOException
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Jan 20, 2015 8:16:05 PM
	 * </pre>
	 */
	public File write(String templatePath, String templateName, Map<String, Object> dataMap, String toFullPathFile) throws IOException {

		Writer out = null;
		File targetFile = null;
		try {
			targetFile = new File(toFullPathFile);
			//判断文档路径是否存在
			if (!targetFile.getParentFile().exists()) {  
		        boolean result = targetFile.getParentFile().mkdirs();  
		        if (!result) {  
		        	logger.error("----------   文档路径生成失败  --------"); 
		        }  
		    }
			out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8"));// 生成文件的保存路径 
			Template t = getTemplate(templatePath, templateName);
			t.process(dataMap, out);
			System.err.println(JacksonJsonUtil.beanToJson(dataMap));
			
			return targetFile;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		} finally {
			out.close();
		}
		return targetFile;
	}

	/**
	 * <pre>
	 * Desc  生成模板
	 * @param templatePath
	 * @param templateName
	 * @return
	 * @throws IOException
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Jan 20, 2015 8:15:50 PM
	 * </pre>
	 */
	private Template getTemplate(String templatePath, String templateName) throws IOException {

		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("UTF-8");
		configuration.setClassForTemplateLoading(this.getClass(), templatePath);
		Template template = configuration.getTemplate(templateName);
		template.setEncoding("UTF-8");
		return template;
	}

}
