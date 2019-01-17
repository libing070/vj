/**
 * com.hp.cmcc.job.service.SJBGZdtl3000WordProcessor.java
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

import com.hpe.cmca.service.SJTBWordService;

/**
 * <pre>
 * Desc： 此Java类主要负责终端套利-审计通报生成。包含以下内容：1.将数据源填充进模板中；2.设计生成文档格式。 
 * 		不包括：数据源整理；模板获取等内容。
 * @author   hufei
 * @refactor hufei
 * @date     2017-6-12 下午3:01:09
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-6-12 	   hufei 	         1. Created this class.
 * </pre>
 */
@Service("SJBGZdtl3000WordProcessor")
public class SJBGZdtl3000WordProcessor extends AbstractSJTBWordProcessor {

    @Autowired
    SJTBWordService  sjtbService;
    protected Logger logger = Logger.getLogger(this.getClass());

    // 实现父类的抽象方法;该Java类的入口 -by hufei
    public boolean generate() throws Exception {
	// 第一步：设置文档名称
	this.setFileName("持续审计通报");
	this.setSubjectNm("终端套利");
	//数据查询使用前端传入月份，文件名使用当前月-与文件头的月份一致
	String audTrm=this.getMonth();
	//第二步：获取变量数据源
	Map<String, Object> paramsMap = sjtbService.getZdtlData(audTrm);
	//第三步：将数据源填充进模板，并对设置文档格式
	genWord(doc, preparData(paramsMap),paramsMap);
	return true;
    }

    // 将数据源填充进模板 -by hufei
    private String[] preparData(Map<String,Object> paramsMap) throws IOException {
	// 第一步：获取模板
	String modelText = sjtbService.getModel(3);
	// 第二步：替换变量
	String tpText = replaceParam(modelText, paramsMap);
	logger.error(paramsMap);
	// 第三步：将String字符串划分字符串数组
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
    private void genWord(XWPFDocument doc, String[] str,Map<String, Object> paramsMap) throws IOException {
	this.logger.info("开始生成word");
	setWord(doc, ParagraphAlignment.CENTER, true, str[0], "仿宋_GB2312", "36", 0);
	setWord(doc, ParagraphAlignment.BOTH, false, str[1], "仿宋_GB2312", "32", 600);
	setWord(doc, ParagraphAlignment.BOTH, true, str[2], "黑体", "32", 600);
	setWord(doc, ParagraphAlignment.BOTH, false, str[3], "仿宋_GB2312", "32", 600);
	setWord(doc, ParagraphAlignment.BOTH, false, str[4], "仿宋_GB2312", "32", 600);
	boolean isNull=(((StringBuffer)paramsMap.get("prvdNameList3")).length()==0);
	if (isNull) {
	    setWord(doc, ParagraphAlignment.BOTH, true, "二、"+str[5].split("/")[1], "黑体", "32", 600);
	    setWord(doc, ParagraphAlignment.BOTH, true, str[6], "仿宋_GB2312", "32", 600);
	    setWord(doc, ParagraphAlignment.BOTH, false, str[7], "仿宋_GB2312", "32", 600);
	} else {
	    setWord(doc, ParagraphAlignment.BOTH, true, "二、"+str[5].split("/")[0], "黑体", "32", 600);
	    setWord(doc, ParagraphAlignment.BOTH, true, "(一)" + str[6], "仿宋_GB2312", "32", 600);
	    setWord(doc, ParagraphAlignment.BOTH, false, str[7], "仿宋_GB2312", "32", 600);
	    setWord(doc, ParagraphAlignment.BOTH, true, "(二)" + str[8], "仿宋_GB2312", "32", 600);
	    setWord(doc, ParagraphAlignment.BOTH, false, str[9], "仿宋_GB2312", "32", 600);
	}

	setWord(doc, ParagraphAlignment.BOTH, true, str[10], "黑体", "32", 600);
	setWord(doc, ParagraphAlignment.BOTH, false, str[11], "仿宋_GB2312", "32", 600);
	this.logger.info("生成word" + getLocalDir() + "/simple.docx完成");
    }
}
