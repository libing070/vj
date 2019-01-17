package com.hpe.cmca.job;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.springframework.beans.factory.annotation.Autowired;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.service.NotiFileGenService;

public abstract class AbstractSJTBWordProcessor {
	protected Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	public NotiFileGenService notiFileGenService	= null;
	
	@Autowired
	protected MybatisDao	mybatisDao	= null;
	
	protected String fileName;
	
	protected String path = null;
	
	protected String month;
	
	protected String focusCd;
	
	protected String subjectNm;
	
	protected Map<String, Object> data = null;
	
	protected String localDir = null;
	
	protected XWPFDocument doc;
	
	public void setWord(XWPFDocument doc,ParagraphAlignment pa,Boolean isSetBold,String content, String fontFamily, String fontSize,int indentationFirstLine){
		XWPFParagraph p = doc.createParagraph();//新建段落
		p.setAlignment(pa);
		XWPFRun r = p.createRun();// 一个XWPFRun代表具有相同属性的一个区域
		setParagraphRunFontInfo(p,r,content,fontFamily,fontSize);
		r.setBold(isSetBold);
		p.setIndentationFirstLine(indentationFirstLine);  //首行缩进
	}
	protected String buildFileName() {
		fileName = subjectNm+ "_" +fileName + "(" + month+ ")"  + ".docx";
		return fileName;
	}
	
	public abstract boolean generate() throws Exception;
	
	public void start() throws Exception{
		doc = new XWPFDocument();  //新建一个文档
		this.generate();
		FileOutputStream out = null;
		try {  
			out = new FileOutputStream(this.getLocalPath());  
			doc.write(out);
		} catch (IOException e) {  
			logger.error("shenjitongbao",e);
		    throw e;
		} catch (Exception e) {  
			logger.error("shenjitongbao",e);
		    throw e;
		}finally {  
		    try {  
		        out.close();  
		    } catch (IOException e) { 
		    	logger.error("shenjitongbao",e);
		    	 throw e;  
		    }  
		} 
	}
	
	
	public String getSubjectNm() {
		return this.subjectNm;
	}

	
	public void setSubjectNm(String subjectNm) {
		this.subjectNm = subjectNm;
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
	
	public ArrayList<String> getFileNameList() {
		return new ArrayList<String>();
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
	/**
	 * @Description: 得到XWPFRun的CTRPr
	 */
	public CTRPr getRunCTRPr(XWPFParagraph p, XWPFRun pRun) {
		CTRPr pRpr = null;
		if (pRun.getCTR() != null) {
			pRpr = pRun.getCTR().getRPr();
			if (pRpr == null) {
				pRpr = pRun.getCTR().addNewRPr();
			}
		} else {
			pRpr = p.getCTP().addNewR().addNewRPr();
		}
		return pRpr;
	}
	
	/**
	 * @Description 设置字体信息
	 */
	public void setParagraphRunFontInfo(XWPFParagraph p, XWPFRun pRun,
			String content, String fontFamily, String fontSize) {
		CTRPr pRpr = getRunCTRPr(p, pRun);
		if (StringUtils.isNotBlank(content)) {
			pRun.setText(content);
		}
		// 设置字体
		CTFonts fonts = pRpr.isSetRFonts() ? pRpr.getRFonts() : pRpr
				.addNewRFonts();
		fonts.setAscii(fontFamily);
		fonts.setEastAsia(fontFamily);
		fonts.setHAnsi(fontFamily);

		// 设置字体大小
		CTHpsMeasure sz = pRpr.isSetSz() ? pRpr.getSz() : pRpr.addNewSz();
		sz.setVal(new BigInteger(fontSize));

		CTHpsMeasure szCs = pRpr.isSetSzCs() ? pRpr.getSzCs() : pRpr
				.addNewSzCs();
		szCs.setVal(new BigInteger(fontSize));
	}
	public String replaceParam(String text,Map<String, Object> params){
		Matcher m = Pattern.compile("[{]([^{^}]*?)[}]").matcher(text);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {			
		    m.appendReplacement(sb, params.get(m.group(1)).toString());	    
		}
		m.appendTail(sb);
		return sb.toString();
		
	}
	
}
