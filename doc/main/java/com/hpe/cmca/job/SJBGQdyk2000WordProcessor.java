package com.hpe.cmca.job;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.service.SJTBWordService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.finals.Constants;

@Service("SJBGQdyk2000WordProcessor")
public class SJBGQdyk2000WordProcessor extends AbstractSJTBWordProcessor {
	@Autowired
	SJTBWordService sjtbService;
	protected Logger logger = Logger.getLogger(this.getClass());
	Map<String, Object> paramsMap = new HashMap<String, Object>();
	public boolean generate() throws Exception{

		this.setFileName("持续审计通报");
		this.setSubjectNm(Constants.MAP_SUBJECT_NAME.get(focusCd.substring(0, 1)));
		String[] str = preparData();
		genWord(doc,str,paramsMap);
		return true;
	}
	public String [] preparData() throws IOException, ParseException{
		paramsMap = sjtbService.getQdykData(this.getMonth());
		String modelText = sjtbService.getModel(2);		
		String tpText = replaceParam(modelText,paramsMap);		
		logger.error(paramsMap);	
		String re[] = tpText.split("\\|");		
		return re;		
	}

	public void genWord(XWPFDocument doc,String[] str,Map<String, Object> paramsMap) throws IOException{
		this.logger.info("开始生成word");
		setWord(doc,ParagraphAlignment.CENTER,true,str[0],"仿宋_GB2312","36",0);
		setWord(doc,ParagraphAlignment.BOTH,false,str[1],"仿宋_GB2312","32",600);
		setWord(doc,ParagraphAlignment.BOTH,true,str[2],"黑体","32",600);
		setWord(doc,ParagraphAlignment.BOTH,false,str[3],"仿宋_GB2312","32",600);
		setWord(doc,ParagraphAlignment.BOTH,false,str[4],"仿宋_GB2312","32",600);
		setWord(doc,ParagraphAlignment.BOTH,false,str[5],"仿宋_GB2312","32",600);
		if(paramsMap.get("prvdNameWZYK")==" "){
			setWord(doc,ParagraphAlignment.BOTH,true,"二、"+str[6].split("/")[1],"黑体","32",600);
			setWord(doc,ParagraphAlignment.BOTH,true,str[7],"楷体","32",600);
			setWord(doc,ParagraphAlignment.BOTH,false,str[8],"仿宋_GB2312","32",600);
		}else{
			setWord(doc,ParagraphAlignment.BOTH,true,str[6].split("/")[0],"黑体","32",600);
			setWord(doc,ParagraphAlignment.BOTH,true,"(一)"+str[7],"楷体","32",600);
			setWord(doc,ParagraphAlignment.BOTH,false,str[8],"仿宋_GB2312","32",600);
			setWord(doc,ParagraphAlignment.BOTH,true,"（二）"+str[9],"楷体","32",600);
			setWord(doc,ParagraphAlignment.BOTH,false,str[10],"仿宋_GB2312","32",600);
		}

		setWord(doc,ParagraphAlignment.BOTH,true,str[11],"黑体","32",600);
		setWord(doc,ParagraphAlignment.BOTH,false,str[12],"仿宋_GB2312","32",600);
		setWord(doc,ParagraphAlignment.BOTH,false,str[13],"仿宋_GB2312","32",600);
	    this.logger.info("生成word"+getLocalDir()+"/simple.docx完成");
	}
}
