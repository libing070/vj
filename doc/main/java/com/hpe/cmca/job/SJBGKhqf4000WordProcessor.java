/**
 * com.hp.cmcc.job.service.SJBGKhqf4000WordProcessor.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.finals.Constants;
import com.hpe.cmca.service.SJTBWordService;


/**
 * <pre>
 * Desc： 此Java类主要负责客户欠费-审计通报生成。包含以下内容：1.将数据源填充进模板中；2.设计生成文档格式。 
 * 		不包括：数据源整理；模板获取等内容。
 * @author   hufei
 * @refactor hufei
 * @date     2017-6-19 上午10:09:31
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-6-19 	   hufei 	         1. Created this class. 
 * </pre>  
 */
@Service("SJBGKhqf4000WordProcessor")
public class SJBGKhqf4000WordProcessor extends AbstractSJTBWordProcessor {

    @Autowired
    SJTBWordService  sjtbService;
    protected Logger logger = Logger.getLogger(this.getClass());

    //实现父类的抽象方法;该Java类的入口  -by hufei 
    public boolean generate() throws Exception {
	//第一步：设置文档名称
	this.setFileName("持续审计通报");
	this.setSubjectNm(Constants.MAP_SUBJECT_NAME.get(focusCd.substring(0, 1)));
	//第二步：将数据源填充进模板，并对设置文档格式
	genWord(doc, preparData());
	return true;
    }
    //将数据源填充进模板 -by hufei
    private String[] preparData() throws IOException {
	//第一步：获取数据
	Map<String, Object> paramsMap = sjtbService.getKhqfData(this.getMonth());
	//第二步：获取模板
	String modelText = sjtbService.getModel(4);
	//第三步：替换变量
	String tpText = replaceParam(modelText, paramsMap);
	logger.error(paramsMap);
	//第四步：将String字符串划分字符串数组
	String re[] = tpText.split("\\|");
	return re;
    }
    /**
     * <pre>
     * Desc设置文档格式  
     * @param doc 
     * @param str
     * @throws IOException
     * @author hufei
     * 2017-6-14 下午5:45:53
     * </pre>
     */
    private void genWord(XWPFDocument doc, String[] str) throws IOException {
	this.logger.info("开始生成word");
	setWord(doc, ParagraphAlignment.CENTER, true, str[0], "仿宋_GB2312", "36", 0);
	setWord(doc, ParagraphAlignment.BOTH, false, str[1], "仿宋_GB2312", "32", 600);
	setWord(doc, ParagraphAlignment.BOTH, false, str[2], "黑体", "32", 600);
	setWord(doc, ParagraphAlignment.BOTH, false, str[3], "仿宋_GB2312", "32", 600);
	setWord(doc, ParagraphAlignment.BOTH, false, str[4], "仿宋_GB2312", "32", 600);
	setWord(doc, ParagraphAlignment.BOTH, false, str[5], "仿宋_GB2312", "32", 600);
	setWord(doc, ParagraphAlignment.BOTH, false, str[6], "黑体", "32", 600);
	setWord(doc, ParagraphAlignment.BOTH, false, str[7], "仿宋_GB2312", "32", 600);
	setWord(doc, ParagraphAlignment.BOTH, false, str[8], "仿宋_GB2312", "32", 600);
	setWord(doc, ParagraphAlignment.BOTH, false, str[9], "仿宋_GB2312", "32", 600);
	setWord(doc, ParagraphAlignment.BOTH, false, str[10], "黑体", "32", 600);
	setWord(doc, ParagraphAlignment.BOTH, false, str[11], "仿宋_GB2312", "32", 600);	
	//setWord(doc, ParagraphAlignment.BOTH, false, str[12], "仿宋_GB2312", "32", 600);
	this.logger.info("生成word" + getLocalDir() + "/simple.docx完成");
    }
}