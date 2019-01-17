package com.hpe.cmca.job;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.service.SJTBWordService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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

@Service("SJBGyjk1000WordProcessor")
public class SJBGyjk1000WordProcessor extends AbstractSJTBWordProcessor {
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
	
	public String [] preparData() throws IOException{
		paramsMap = sjtbService.getYjkData(this.getMonth());
		String modelText = sjtbService.getModel(1);		
		String tpText = replaceParam(modelText,paramsMap);		
		logger.error(paramsMap);	
		String re[] = tpText.split("\\|");		
		return re;
		//return sjtbService.getModel(1).split("\\|");
	}
	
	public void genWord(XWPFDocument doc,String[] str,Map<String, Object> paramsMap) throws IOException{
		this.logger.info("开始生成word");
		setWord(doc,ParagraphAlignment.CENTER,true,str[0],"仿宋_GB2312","36",0);
		setWord(doc,ParagraphAlignment.BOTH,false,str[1],"仿宋_GB2312","32",600);
		setWord(doc,ParagraphAlignment.BOTH,true,str[2],"黑体","32",600);
		setWord(doc,ParagraphAlignment.BOTH,false,str[3],"仿宋_GB2312","32",600);
		setWord(doc,ParagraphAlignment.BOTH,false,str[4],"仿宋_GB2312","32",600);
		setWord(doc,ParagraphAlignment.BOTH,false,str[5],"仿宋_GB2312","32",600);
		setWord(doc,ParagraphAlignment.BOTH,false,str[6],"仿宋_GB2312","32",600);
		setWord(doc,ParagraphAlignment.BOTH,false,str[7],"仿宋_GB2312","32",600);
		setWord(doc,ParagraphAlignment.BOTH,false,str[8],"仿宋_GB2312","32",600);
		
		if(paramsMap.get("prvdNameWZ")==" "){	
			setWord(doc,ParagraphAlignment.BOTH,true,"二、"+str[9].split("/")[1],"黑体","32",600);
			setWord(doc,ParagraphAlignment.BOTH,true,str[10],"仿宋_GB2312","32",600);
			setWord(doc,ParagraphAlignment.BOTH,false,str[11],"仿宋_GB2312","32",600);
		}else{
			setWord(doc,ParagraphAlignment.BOTH,true,str[9].split("/")[0],"黑体","32",600);
			setWord(doc,ParagraphAlignment.BOTH,true,"(一)"+str[10],"仿宋_GB2312","32",600);
			setWord(doc,ParagraphAlignment.BOTH,false,str[11],"仿宋_GB2312","32",600);
			setWord(doc,ParagraphAlignment.BOTH,true,"（二）"+str[12],"仿宋_GB2312","32",600);
			setWord(doc,ParagraphAlignment.BOTH,false,str[13],"仿宋_GB2312","32",600);
		}

		
		
		setWord(doc,ParagraphAlignment.BOTH,true,str[14],"黑体","32",600);
		setWord(doc,ParagraphAlignment.BOTH,false,str[15],"仿宋_GB2312","32",600);
	    this.logger.info("生成word"+getLocalDir()+"/simple.docx完成");
	}
}
