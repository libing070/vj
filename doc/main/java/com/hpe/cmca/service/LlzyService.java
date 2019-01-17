/**
 * com.hp.cmcc.service.YgycService.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.service;



import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.util.CalendarUtils;
import com.hpe.cmca.common.CustomXWPFDocument;
import com.hpe.cmca.util.WordUtils;

/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2017-8-18 上午11:04:10
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-8-18 	   hufei 	         1. Created this class.
 * </pre>
 */
@Service("LlzyService")
public class LlzyService extends BaseObject {

    @Autowired
    MybatisDao       mybatisDao;

    /**
     * New by Zhangqiang
     * 通过复制模板  替换 变量方式生成审计报告
     * @param subjectId
     * @return
     */
	 // 获取审计报告 文字描述 部分模板
 	public Map<String, Object> getDocData(String subjectId, String audTrm, int prvdId) {
 		// 存放所有数据
 		Map<String, Object> allDocData = new HashMap<String, Object>();
 		
 		// 初始化  将没有数据的表格置为“-”
 		for (int i = 1; i < 5; i++) {
			for (int j = 1; j < 6; j++) {
				allDocData.put("one"+i+j, "-");
				allDocData.put("tow"+i+j, "-");
				allDocData.put("three"+i+j, "-");
				//allDocData.put("four"+i+j, "-");
			}
		}
 		
 		// SQL参数值
 		Map<String, Object> params = new HashMap<String, Object>();
 		params.put("audTrm", audTrm);
 		params.put("prvdId", prvdId);
 		
 		// 审计报告正文描述
 		Map<String, Object> llzyBaseInfo = mybatisDao.get("llzyReport.getModelLlzyDate", params);
 		
 		if (llzyBaseInfo == null || llzyBaseInfo.size() == 0) {
 			allDocData =  null;
 			return allDocData ;
		}
 		
 		// 构建表格title格式（全国 - 分公司、地市 - 地市公司）
 		String prvdOrCity = "cmcc_prov_name" ;
 		String prvdData = "llzyReport.getLlzyCityData";
 		allDocData.put("nametype", "分");
 		if(prvdId == 10000 ){
 			allDocData.put("nametype", "省");
 			// 构建Word省份名称格式（全国 - 全、各省 - 省名称）
 			prvdOrCity = "cmcc_prov_prvd_name" ;
 			prvdData = "llzyReport.getLlzyPrvdData";
 		}
 		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
 		int dateYear = c.get(Calendar.YEAR);
 		int dateMonth = c.get(Calendar.MONTH)+1;
 		int dateDay = c.get(Calendar.DATE);
 		// 2017年8月1日-2017年8月31日 格式拼接
 		String audTrmTxt = (String)llzyBaseInfo.get("aud_trm_y")+"年"+llzyBaseInfo.get("aud_trm_m")+"月" ;
 		String timeAll = audTrmTxt+"1日-"+getLastDayByAudtrm(audTrm, Integer.valueOf((String) llzyBaseInfo.get("aud_trm_m")));
 		
 		// 生成日子 例如：2017年10月11日
 		String date =  dateYear+"年"+dateMonth+"月"+dateDay+"日";
 		allDocData.put("date", date);
 		// 摘要数据接收
 		String prvdName = (String)llzyBaseInfo.get("cmcc_prov_prvd_name") ;
		allDocData.put("prvd_nm", prvdId==10000 ? "全":prvdName);
		allDocData.put("time_all", timeAll);
		allDocData.put("aud_trm_txt",audTrmTxt);
		allDocData.put("num1",  String.valueOf(llzyBaseInfo.get("hz_cnt_org")));
		allDocData.put("num2",  String.valueOf(llzyBaseInfo.get("hz_cnt_msisdn")));
		allDocData.put("num3",  String.valueOf(llzyBaseInfo.get("hz_cnt")));
		allDocData.put("num4",  String.valueOf(llzyBaseInfo.get("hz_sum_strm_cap")));
		allDocData.put("num5",  String.valueOf(llzyBaseInfo.get("cnt_org_id")));
		allDocData.put("per1",  String.valueOf(llzyBaseInfo.get("abn_cli_rat")));
		allDocData.put("num6",  String.valueOf(llzyBaseInfo.get("cnt_msisdn")));
		allDocData.put("num7",  String.valueOf(llzyBaseInfo.get("sum_cnt")));
		allDocData.put("num8",  String.valueOf(llzyBaseInfo.get("sum_strm_cap")));
		allDocData.put("per2",  String.valueOf(llzyBaseInfo.get("abn_tra_rat")));
 		
 		// 转售流量总量排名前5的公司：
 		params.put("ranking_type", "101");
 		List<Map<String,Object>> llzy7002llTop5=mybatisDao.getList(prvdData, params);
 		if(llzy7002llTop5!=null){
 		    for(int i=0,size=llzy7002llTop5.size()-1;i<=size;i++){
 		    	Map<String,Object> map=llzy7002llTop5.get(i);
 		    	int y =  i+1 ;
 		    	
 		    	allDocData.put("one1"+y, String.valueOf(map.get(prvdOrCity)));
 		    	allDocData.put("one2"+y, String.valueOf(map.get("cnt_org_id")));
 		    	allDocData.put("one3"+y, String.valueOf(map.get("sum_strm_cap")));
 		    	allDocData.put("one4"+y, String.valueOf(map.get("abn_tra_rat")));
 		    }
 		}
 		 
 		// 转售流量占比排名前5的公司：
 		params.put("ranking_type", "102");
 		List<Map<String,Object>> llzy7002zbTop5=mybatisDao.getList(prvdData, params);
 		if(llzy7002zbTop5!=null){
 		    for(int i=0,size=llzy7002zbTop5.size()-1;i<=size;i++){
 		    	Map<String,Object> map=llzy7002zbTop5.get(i);
 		    	int y =  i+1 ;
 		    	allDocData.put("tow1"+y, String.valueOf(map.get(prvdOrCity)));
 		    	allDocData.put("tow2"+y, String.valueOf(map.get("cnt_org_id")));
 		    	allDocData.put("tow3"+y, String.valueOf(map.get("sum_strm_cap")));
 		    	allDocData.put("tow4"+y, String.valueOf(map.get("abn_tra_rat")));
 		    }
 		}
 		
 		// 转售流量排名前5的集团客户：
 		List<Map<String,Object>> llzy7002JtTop5=mybatisDao.getList("llzyReport.getLlzyPrvdCityJtData", params);
 		if(llzy7002JtTop5!=null){
 		    for(int i=0,size=llzy7002JtTop5.size()-1;i<=size;i++){
 		    	Map<String,Object> map=llzy7002JtTop5.get(i);
 		    	int y =  i+1 ;
 		    	allDocData.put("three1"+y, String.valueOf(map.get(prvdOrCity)));
 		    	allDocData.put("three2"+y, String.valueOf(map.get("org_cust_id")));
 		    	allDocData.put("three3"+y, String.valueOf(map.get("sum_strm_cap")));
 		    	allDocData.put("three4"+y, String.valueOf(map.get("abn_tra_rat")));
 		    }
 		}
 	
 		// 转售流量占比排名前5的集团客户：
 		/*List<Map<String,Object>> llzy7002JtZbTop5=mybatisDao.getList("llzyReport.getLlzyPrvdCityJtData", params);
 		if(llzy7002JtZbTop5!=null){
 		    for(int i=0,size=llzy7002JtZbTop5.size()-1;i<=size;i++){
 		    	Map<String,Object> map=llzy7002JtZbTop5.get(i);
 		    	int y =  i+1 ;
 		    	allDocData.put("four1"+y, String.valueOf(map.get(prvdOrCity)));
 		    	allDocData.put("four2"+y, String.valueOf(map.get("org_cust_id")));
 		    	allDocData.put("four3"+y, String.valueOf(map.get("sum_strm_cap")));
 		    	allDocData.put("four4"+y, String.valueOf(map.get("abn_tra_rat")));
 		    }
 		}*/
 		return allDocData ;
 	}
    
 	// @test
	public  void genNewDoc() throws FileNotFoundException, IOException {
		
		int prvdId = 11100 ;
		Map<String, Object> params =  getDocData("7", "201701",prvdId);
		
		String path1 = "模板_流量异常转移审计报告.docx" ;
		CustomXWPFDocument doc = new WordUtils().generateWord(params, path1);
		FileOutputStream fopts;
		try {
			fopts = new FileOutputStream("c:/"+prvdId+"_流量异常转移审计报告Test.docx");
			doc.write(fopts);
			fopts.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 时间转换
	 * 时间获取
	 * 
	 * **/
	public String getLastDayByAudtrm(String audtrm, int months) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
		try {
			c.setTime(sdf2.parse(audtrm));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		c.add(Calendar.MONTH, months );
		c.set(Calendar.DAY_OF_MONTH, 0);
		String s = sdf.format(c.getTime());
		return s;
	}
	
	public Map<String, List<Object>> selectAuditReportPageData(Map<String, Object> params)  {
		
		Map<String, List<Object>> returnMap = new HashMap<String, List<Object>>();
		if(params.get("subjectId") != null && "7".equals(params.get("subjectId"))){
			String aud_cyl= CalendarUtils.getMonBeginEnd((String) params.get("statCycle"));
			List<Object> TX_DATE= new ArrayList<Object>();
			TX_DATE.add(aud_cyl);
			returnMap.put("aud_info",TX_DATE );
		}else{
			String aud_cyl= CalendarUtils.getMonBeginEnd((String) params.get("statCycle"));
			String aud_trm=aud_cyl.substring(aud_cyl.indexOf("-")+1,aud_cyl.length());
			List<Object> TX_DATE= new ArrayList<Object>();
			TX_DATE.add(aud_trm);
			returnMap.put("aud_info",TX_DATE );
		}
		
		
		//开通新业务审计周期
		params.put("statCycle", (String) params.get("statCycle")) ; 
		
//		params.put("statCycle", (CalendarUtils.AddMontns((String) (params.get("statCycle")+"01"), -7)).substring(0,6)) ;
		returnMap.putAll(selectLlzyAuditReportData(params));

		return returnMap;
	}
	
	public Map<String, List<Object>> selectLlzyAuditReportData(Map<String, Object> params) {
		Map<String, List<Object>> returnMap = new HashMap<String, List<Object>>();

		String []ref_name = {
							"llzytotalInfo",
							"llzstotalInfo"
							};
		
		String []ref_value = {
							"llzyMapper.getLlzyAuditReport",
							"llzyMapper.getLlzsAuditReport"
							};
		
		for (int i = 0; i < ref_name.length; i++){
			List<Object> resultList = mybatisDao.getList(ref_value[i], params);
			returnMap.put(ref_name[i], resultList);
			System.out.println("-------" + ref_name[i] + "::" + resultList);
		}
		
		return returnMap;
	}
	
    /*
*//**-----------------------以下内容暂时废弃------------------------------------------**//*	
	// 获取审计报告 文字描述 部分模板
	public String getModel(int subjectId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", subjectId);

		Map<String, Object> contentMap = mybatisDao.get(
				"llzyReport.getModelContent", params);
		return contentMap.get("report_content").toString();

	}

    // 获取审计报告 文字描述 部分模板 place_id 展示顺序
    public String getTable(int subject_id, int place_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subject_id", subject_id);
		params.put("place_id", place_id);
		Map<String, Object> contentMap = mybatisDao.get("llzyReport.getLlzyTableContent", params);
		
		return contentMap.get("report_content").toString();
    }
    
    // 获取审计报告第一段文字描述中的变量值
	@SuppressWarnings("unchecked")
	public Map<String, Object> getLlzyDate(String aud_trm, int prvd_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("aud_trm", aud_trm);
		params.put("prvd_id", prvd_id);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> llzyBaseInfo = mybatisDao.get(
				"llzyReport.getModelLlzyDate", params);
		result.put("prvd_nm", llzyBaseInfo.get("cmcc_prov_prvd_name"));
		result.put("aud_trm_y", llzyBaseInfo.get("aud_trm_y"));
		result.put("aud_trm_m", llzyBaseInfo.get("aud_trm_m"));
		result.put("hz_cnt_org", llzyBaseInfo.get("hz_cnt_org"));
		result.put("hz_cnt_msisdn", llzyBaseInfo.get("hz_cnt_msisdn"));
		result.put("hz_cnt", llzyBaseInfo.get("hz_cnt"));
		result.put("hz_sum_strm_cap", llzyBaseInfo.get("hz_sum_strm_cap"));
		result.put("cnt_org_id", llzyBaseInfo.get("cnt_org_id"));
		result.put("abn_cli_rat", llzyBaseInfo.get("abn_cli_rat"));
		result.put("cnt_msisdn", llzyBaseInfo.get("cnt_msisdn"));
		result.put("sum_cnt", llzyBaseInfo.get("sum_cnt"));
		result.put("sum_strm_cap", llzyBaseInfo.get("sum_strm_cap"));
		result.put("abn_tra_rat", llzyBaseInfo.get("abn_tra_rat"));

		return result;
	}

	// 正则表达式匹配 查找变量值
	public String replaceParam(String text, Map<String, Object> params) {
		Matcher m = Pattern.compile("[{]([^{^}]*?)[}]").matcher(text);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, params.get(m.group(1)).toString());
		}
		m.appendTail(sb);
		return sb.toString();
	}

	// @Description: 得到XWPFRun的CTRPr
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

	
	// @Description 设置字体信息
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
    
    
	// 构建审计报告开头表格内容（无边框表格）
 	public  void setWordTable(XWPFDocument xdoc,String[] str) {
 		try {
 			// 设置表格套入内容
 			XWPFParagraph headLine1 = xdoc.createParagraph();
 			
 			headLine1.setAlignment(ParagraphAlignment.LEFT);
 			XWPFTable dTable = xdoc.createTable(4, 2);
 			//取消邊框
 			setBorders(dTable);
 	 
 			createTable(dTable, xdoc,str);
 		} catch (Exception e) {
 			
 		} 
 		
 	}
 // 构建审计报告开头表格内容（控制有无边框）
  	public  void setWordTable(XWPFDocument xdoc,int x ,int y , Boolean bool,
  			String[] str,String bgColor) {
  		XWPFParagraph titleMes = xdoc.createParagraph();
		titleMes.setAlignment(ParagraphAlignment.CENTER);
  		XWPFRun r1 = titleMes.createRun();
  		
  		try {
  			// 设置表格套入内容
  			XWPFParagraph headLine1 = xdoc.createParagraph();
  			
  			headLine1.setAlignment(ParagraphAlignment.LEFT);
  			XWPFTable dTable = xdoc.createTable(x, y);
  			//取消邊框
  			if (bool) {
  				setBorders(dTable);
			}
  			createTable(dTable, xdoc,str,bgColor);
  			setEmptyRow(xdoc, r1);
  		} catch (Exception e) {
  			
  		} 
  		
  	}
  	// 加空行
  	public void addNewPage(XWPFDocument document,BreakType breakType){  
        XWPFParagraph xp = document.createParagraph();  
        xp.createRun().addBreak(breakType);  
    }  
	 // 设置表格间的空行
	 	public static void setEmptyRow(XWPFDocument xdoc, XWPFRun r1) {
	 		XWPFParagraph p1 = xdoc.createParagraph();
	 		p1.setAlignment(ParagraphAlignment.CENTER);
	 		p1.setVerticalAlignment(TextAlignment.CENTER);
	 		r1 = p1.createRun();
	 	}
 	// 创建内容
 	public  void createTable(XWPFTable xTable, XWPFDocument xdoc,String[] str) {
 		String bgColor = "111111";
 		setCellText(xdoc, getCellHight(xTable, 0, 0), str[0], bgColor, 3800);
 		setCellText(xdoc, getCellHight(xTable, 0, 1), str[1], bgColor,3800);
 		setCellText(xdoc, getCellHight(xTable, 1, 0), str[2], bgColor, 3800);
 		setCellText(xdoc, getCellHight(xTable, 1, 1), str[3], bgColor, 3800);
 		setCellText(xdoc, getCellHight(xTable, 2, 0), str[4], bgColor, 3800);
 		setCellText(xdoc, getCellHight(xTable, 2, 1), str[5],bgColor, 3800);
 		setCellText(xdoc, getCellHight(xTable, 3, 0), str[6], bgColor, 3800);
 		setCellText(xdoc, getCellHight(xTable, 3, 1), str[7], bgColor, 3800);

 	}

 	// 创建内容
  	public  void createTable(XWPFTable xTable, XWPFDocument xdoc, String[] str,String bgColor) {

		for (int i = 0; i < 5; i++) {
			setCellText(xdoc, getCellHight(xTable, 0, i), str[i], bgColor, 3800);
			}
		
		// 表格左侧序号排名
		setCellText(xdoc, getCellHight(xTable, 1, 0), "1", bgColor, 3800);
		setCellText(xdoc, getCellHight(xTable, 2, 0), "2", bgColor, 3800);
		setCellText(xdoc, getCellHight(xTable, 3, 0), "3", bgColor, 3800);
		setCellText(xdoc, getCellHight(xTable, 4, 0), "4", bgColor, 3800);
		setCellText(xdoc, getCellHight(xTable, 5, 0), "5", bgColor, 3800);
  	}
 	   
 	// 设置表格高度
 	private  XWPFTableCell getCellHight(XWPFTable xTable, int rowNomber,
 			int cellNumber) {
 		XWPFTableRow row = null;
 		row = xTable.getRow(rowNomber);
 		//表格高度  
 		row.setHeight(600);
 		//表格属性  
 	    CTTblPr tablePr = xTable.getCTTbl().addNewTblPr();  
 		//表格宽度  
 	    CTTblWidth width = tablePr.addNewTblW();  
 	    width.setW(BigInteger.valueOf(10000));// 宽度 10000
 		XWPFTableCell cell = null;
 		cell = row.getCell(cellNumber);
 		return cell;
 	}

 	// 设置内容格式（貌似没有生效） --  setParagraphRunFontInfo 改用这个方法
 	private  void setCellText(XWPFDocument xDocument, XWPFTableCell cell,
 			String text, String bgcolor, int width) {
 		XWPFParagraph pIO = cell.addParagraph();
 		cell.removeParagraph(0);
 		XWPFRun rIO = pIO.createRun();
 		rIO.setFontFamily("仿宋_GB2312");
 		rIO.setColor("000000");
 		rIO.setFontSize(16);
 		rIO.setText(text);
 	}
 	
 	// 取消边框（边框不显示，网上扒拉的）
 	public  void setBorders(XWPFTable dTable) {
 		  
         CTTblBorders borders=dTable.getCTTbl().getTblPr().addNewTblBorders();  
         CTBorder hBorder=borders.addNewInsideH();  
         hBorder.setVal(STBorder.Enum.forString("none"));  
         hBorder.setSz(new BigInteger("1"));  
         hBorder.setColor("0000FF");  
           
         CTBorder vBorder=borders.addNewInsideV();  
         vBorder.setVal(STBorder.Enum.forString("none"));  
         vBorder.setSz(new BigInteger("1"));  
         vBorder.setColor("00FF00");  
           
         CTBorder lBorder=borders.addNewLeft();  
         lBorder.setVal(STBorder.Enum.forString("none"));  
         lBorder.setSz(new BigInteger("1"));  
         lBorder.setColor("3399FF");  
           
         CTBorder rBorder=borders.addNewRight();  
         rBorder.setVal(STBorder.Enum.forString("none"));  
         rBorder.setSz(new BigInteger("1"));  
         rBorder.setColor("F2B11F");  
           
         CTBorder tBorder=borders.addNewTop();  
         tBorder.setVal(STBorder.Enum.forString("none"));  
         tBorder.setSz(new BigInteger("1"));  
         tBorder.setColor("C3599D");  
           
         CTBorder bBorder=borders.addNewBottom();  
         bBorder.setVal(STBorder.Enum.forString("none"));  
         bBorder.setSz(new BigInteger("1"));  
         bBorder.setColor("F7E415");  
 	   }
 	

    *//**
     * splitAry方法<br>
     * @param tableText 要分割的内容
     * @param titleCnt 分割的块大小
     * @return
     *
     *//*
 	public  String[][] splitAry(String tableText , int[] titleCnt) {
 		
 		String[] ary = tableText.split("\\|");
 		// 改为可变长度数组
    	//int[] titleCnt = {8,5,5,5,5};
    	int titleNum = 0 ;
    	for (int i = 0; i < titleCnt.length; i++) {
    		titleNum = titleCnt[i] + titleNum;
		}
    	
         int count = titleCnt.length ;//% titleNum == 0 ? titleCnt.length: titleCnt.length + 1;
 
         List<List<String>> subAryList = new ArrayList<List<String>>();
         int index = 0 ;
         for (int i = 0; i < count; i++) {
          index = index + titleCnt[i];
          List<String> list = new ArrayList<String>();
          int j = 0 ;
          while (j <  titleCnt[i] && index <= ary.length) {
              list.add(ary[index - titleCnt[i] + j]);
              j++;
          }
          subAryList.add(list);
         }
          
         Object[] subAry = new Object[subAryList.size()];
          
         for(int i = 0; i < subAryList.size(); i++){
              List<String> subList = subAryList.get(i);
              String[] subAryItem = new String[subList.size()];
              for(int j = 0; j < subList.size(); j++){
                  subAryItem[j] = subList.get(j);
              }
              subAry[i] = subAryItem;
         }

      	String[][] str = new String[titleCnt.length][getMax(titleCnt)];
      	int le = 0;
      	for(Object obj: subAry){
        	 String[] aryItem = (String[]) obj;
	           	 if ( le < titleCnt.length) {
	           		for(int i = 0; i < aryItem.length; i++){
	           			str[le][i] = aryItem[i] ;
	                }
	         le++ ;
				}
         }
      	
      	return str;
         }
 	*//**
     * 取出数组中的最大值
      * @param arr
      * @return
      *//*
     public static int getMax(int[] arr){
         int max=arr[0];
         for(int i=1;i<arr.length;i++){
             if(arr[i]>max){
                 max=arr[i];
             }
         }
         return max;
     }*/
}
