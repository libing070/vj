package com.hpe.cmca.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.service.ComparisonService;

/**
 * 
 * <pre>
 * Desc： 养卡套利 比对
 * @author jh
 * @refactor jh
 * @date   2017-6-7 下午2:31:57
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-6-7 	   jh 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/yktlComp")
public class YKTLComparisonController {
	
	private final static String FILE_PATH = "C:/Users/jh/Desktop/审计报告/养卡套利/201705/";
	
	@Autowired
	private ComparisonService comparisonService;

	//根据CSV 或者 Excel 文档 获取 审计月和省份id 
	//对上述数据表进行查询获取数据
	public static List<String> getExcleList(){
		File file = new File(FILE_PATH);
	    File[] filelist = file.listFiles();
	    List<String> list = new ArrayList<String>();
	    for (File f:filelist){
	        if (f.isFile() && (f.getName().endsWith(".xls") || f.getName().endsWith(".xlsx"))){
	        	list.add(f.getName().toString());
	        }
	    }
		return list;
	}
	
	@ResponseBody
	@RequestMapping("match")
	public String readWorkBook() throws Exception { 
		//生成不同数据文档
		StringBuffer errorInfo = new StringBuffer();
		int errorNum = 1;
		//获取文件夹下的所有Excel文件
		List<String> excleList = getExcleList();
		//将查询的所有Excel文件的数据都封装到同一个list里面
		List<Map<String, Object>> allExcleList =  this.getAllExcleList(excleList);
		//获取审计报告的json数据
		List<Map<String, Object>> reportList = getReportList(excleList);
	    
	    for (int i = 0; i < reportList.size(); i++) {
	    	JSONObject  reportData = JSONObject.fromObject(reportList.get(i).get("reportJsonData").toString());
	    	
	    	for(int j=0;j<allExcleList.size();j++){
	    		
		        //根据上述创建的输入流 创建工作簿对象 
	    		FileInputStream excelFileInputStream = new FileInputStream(FILE_PATH+excleList.get(j));
	    		// XSSFWorkbook 就代表一个 Excel 文件
	    		// 创建其对象，就打开这个 Excel 文件
	    		XSSFWorkbook workbook = new XSSFWorkbook(excelFileInputStream);
		        //得到第一页 sheet 
		        //页Sheet是从0开始索引的 
		        Sheet sheet = workbook.getSheetAt(0);
	    		
	    		Map<String, Object> chnlListOne = allExcleList.get(j);
	    		
	    		List<Map<String, Object>> bottomList = (List<Map<String, Object>>) chnlListOne.get("bottom");
    			List<Map<String, Object>> topList = (List<Map<String, Object>>) chnlListOne.get("top");
	    		//全公司比对
	    		if("全公司".equals(reportData.get("provinceName"))){
	    				List<String> total = (List<String>) chnlListOne.get("tatol");
    					if("全渠道汇总".equals(chnlListOne.get("sheetName"))){
    						List<Map<String, Object>> totalqdMap = (List<Map<String, Object>>) reportData.get("totalInfo");
    						String chnlName = "、全渠道汇总-全公司-";
    						errorNum = getAllShqdMatch(errorInfo, errorNum,
    								totalqdMap, workbook, sheet, total,chnlName);
    						
    						//疑似养卡数量排名前5的省公司
    						List<Map<String, Object>> totalNumTop5 = (List<Map<String, Object>>) reportData.get("total_num_top5");
    						List<Map<String, Object>> newTopList = (List<Map<String, Object>>) chnlListOne.get("top");
    						//对topList，根据num数据进行冒泡排序
    						errorNum = matchNumTop5(errorInfo, errorNum,
									chnlName, totalNumTop5, newTopList);
    						
    						//疑似养卡比例排名前5的省公司   total_percent_top5 topList
    						List<Map<String, Object>> totalPercentTop5 = (List<Map<String, Object>>) reportData.get("total_percent_top5");
    						errorNum = matchPerTop5(errorInfo, errorNum,
									topList, chnlName, totalPercentTop5);
    					}
    					if("其它渠道汇总".equals(chnlListOne.get("sheetName"))){
    						
    					}
    					if("社会渠道汇总".equals(chnlListOne.get("sheetName"))){
    						List<Map<String, Object>> shqdMap = (List<Map<String, Object>>) reportData.get("shqdInfo");
    						String chnlName = "、社会渠道汇总-全公司-";
    						errorNum = getAllShqdMatch(errorInfo, errorNum,
    								shqdMap, workbook, sheet, total,chnlName);
    					} 
    					if("自有渠道汇总".equals(chnlListOne.get("sheetName"))){
    						List<Map<String, Object>> zyqdMap = (List<Map<String, Object>>) reportData.get("zyqdInfo");
    						String chnlName = "、自有渠道汇总-全公司-";
    						errorNum = getAllShqdMatch(errorInfo, errorNum,
    								zyqdMap, workbook, sheet, total,chnlName);
    					}
	    		}
	    		
	    		//各省市公司比对
	    		if(!"全公司".equals(reportData.get("provinceName"))){
	    			for(int k=0;k<bottomList.size();k++){
	    				if(reportData.get("provinceName").equals(bottomList.get(k).get("prvdName")+"公司")){
	    					if("全渠道汇总".equals(chnlListOne.get("sheetName"))){
	    						List<Map<String, Object>> totalqdMap = (List<Map<String, Object>>) reportData.get("totalInfo");
	    						String chnlName = "、全渠道汇总-"+bottomList.get(k).get("prvdName");
	    						//各公司疑似养卡号码数量及占比top:排序 row 省份 prvdName     养卡号码数量num-》errQty_tmp  占比per-》qtyPercent  养卡号码数量环比增幅ratio-》abs_fbQtyPercent
	    						//各公司疑似养卡渠道数量及占比bottom:涉及渠道数量 errChnlQty_tmp    占比qtyChnlPercent     渠道数量环比增幅abs_fbChnlPercent
	    						//{ num=10197.0,  per=3.6,  ratio=0.09,  prvdName=内蒙  row=17 },
	    						Map<String, Object> bottomMap = bottomList.get(k);
	    						Map<String, Object> topMap = topList.get(k);
	    						errorNum = getOnePrivenceData(errorInfo,
										errorNum, workbook, sheet, totalqdMap,
										chnlName, bottomMap, topMap);
	    					}
	    					if("其它渠道汇总".equals(chnlListOne.get("sheetName"))){
	    						
	    					}
	    					if("社会渠道汇总".equals(chnlListOne.get("sheetName"))){
	    						List<Map<String, Object>> shqdMap = (List<Map<String, Object>>) reportData.get("shqdInfo");
	    						String chnlName = "、社会渠道汇总"+bottomList.get(k).get("prvdName");
	    						//各公司疑似养卡号码数量及占比top:排序 row 省份 prvdName     养卡号码数量num-》errQty_tmp  占比per-》qtyPercent  养卡号码数量环比增幅ratio-》abs_fbQtyPercent
	    						//各公司疑似养卡渠道数量及占比bottom:涉及渠道数量 errChnlQty_tmp    占比qtyChnlPercent     渠道数量环比增幅abs_fbChnlPercent
	    						//{ num=10197.0,  per=3.6,  ratio=0.09,  prvdName=内蒙  row=17 },
	    						Map<String, Object> bottomMap = bottomList.get(k);
	    						Map<String, Object> topMap = topList.get(k);
	    						errorNum = getOnePrivenceData(errorInfo,
										errorNum, workbook, sheet, shqdMap,
										chnlName, bottomMap, topMap);
	    					} 
	    					if("自有渠道汇总".equals(chnlListOne.get("sheetName"))){
	    						List<Map<String, Object>> zyqdMap = (List<Map<String, Object>>) reportData.get("zyqdInfo");
	    						String chnlName = "、自有渠道汇总"+bottomList.get(k).get("prvdName");
	    						//各公司疑似养卡号码数量及占比top:排序 row 省份 prvdName     养卡号码数量num-》errQty_tmp  占比per-》qtyPercent  养卡号码数量环比增幅ratio-》abs_fbQtyPercent
	    						//各公司疑似养卡渠道数量及占比bottom:涉及渠道数量 errChnlQty_tmp    占比qtyChnlPercent     渠道数量环比增幅abs_fbChnlPercent
	    						//{ num=10197.0,  per=3.6,  ratio=0.09,  prvdName=内蒙  row=17 },
	    						Map<String, Object> bottomMap = bottomList.get(k);
	    						Map<String, Object> topMap = topList.get(k);
	    						errorNum = getOnePrivenceData(errorInfo,
										errorNum, workbook, sheet, zyqdMap,
										chnlName, bottomMap, topMap);
	    					}
	    				}
	    				
	    			}
	    			
	    		}
	    		
	    		FileOutputStream outPath = new FileOutputStream(FILE_PATH+excleList.get(j));
    			workbook.write(outPath);
    			outPath.close();
    			createTxt(errorInfo.toString(), FILE_PATH+"error.txt");
	    	}
		}
	    
        return "success";
    }

	//疑似养卡比例排名前5的省公司  
	private int matchPerTop5(StringBuffer errorInfo, int errorNum,
			List<Map<String, Object>> topList, String chnlName,
			List<Map<String, Object>> totalPercentTop5) {
		for (int k = 0; k < totalPercentTop5.size(); k++) {
			if(!totalPercentTop5.get(k).get("regionName").equals(topList.get(k).get("prvdName"))){
				errorInfo.append(errorNum+++chnlName+"公司_疑似养卡比例排名前5的省公司_排名第"+(k+1)+"不同").append("\r\n");
			}
		}
		return errorNum;
	}

	//疑似养卡数量排名前5的省公司
	private int matchNumTop5(StringBuffer errorInfo, int errorNum,
			String chnlName, List<Map<String, Object>> totalNumTop5,
			List<Map<String, Object>> newTopList) {
		int temRow =0;
		float temNum =0.0f;
		String temPrvdName ="";
		float temPer =0.0f;
		//float btmPer = Float.parseFloat(bottomMap.get("per").toString());
		double temRatio =0.0d;
		//double topRatioDou = Double.parseDouble(topMap.get("ratio").toString());
		for(int m=0;m<newTopList.size();m++){
			for(int n=0;n<newTopList.size()-m-1;n++){//row prvdName num per ratio
				Float f = new Float(Float.parseFloat(newTopList.get(n).get("num").toString()));
				Float f1 = new Float(Float.parseFloat(newTopList.get(n+1).get("num").toString()));
				if(f.intValue()<f1.intValue()){
					temRow = Integer.parseInt(newTopList.get(n).get("row").toString());
					temNum =  Float.parseFloat(newTopList.get(n).get("num").toString());
					temPrvdName = newTopList.get(n).get("prvdName").toString();
					temPer = Float.parseFloat(newTopList.get(n).get("per").toString());
					temRatio = Double.parseDouble(newTopList.get(n).get("ratio").toString());
					
					newTopList.get(n).put("row", Integer.parseInt(newTopList.get(n+1).get("row").toString()));
					newTopList.get(n).put("num",  Float.parseFloat(newTopList.get(n+1).get("num").toString()));
					newTopList.get(n).put("prvdName", newTopList.get(n+1).get("prvdName").toString());
					newTopList.get(n).put("per", Float.parseFloat(newTopList.get(n+1).get("per").toString()));
					newTopList.get(n).put("ratio", Double.parseDouble(newTopList.get(n+1).get("ratio").toString()));
					
					newTopList.get(n+1).put("row", temRow);
					newTopList.get(n+1).put("num", temNum);
					newTopList.get(n+1).put("prvdName", temPrvdName);
					newTopList.get(n+1).put("per", temPer);
					newTopList.get(n+1).put("ratio", temRatio);
				}
			}
		}
		
		for(int y = 0; y<totalNumTop5.size();y++){
			if(!totalNumTop5.get(y).get("regionName").equals(newTopList.get(y).get("prvdName"))){
				errorInfo.append(errorNum+++chnlName+"公司_疑似养卡数量排名前5的省公司_排名第"+(y+1)+"不同").append("\r\n");
			}
		}
		return errorNum;
	}

	//比对各个省市的数据
	private int getOnePrivenceData(StringBuffer errorInfo, int errorNum,
			XSSFWorkbook workbook, Sheet sheet,
			List<Map<String, Object>> shqdMap, String chnlName,
			Map<String, Object> bottomMap, Map<String, Object> topMap) {
		//养卡号码数量num-》errQty_tmp
		float btmNum = Float.parseFloat(bottomMap.get("num").toString());
		Float f = new Float(btmNum);
		int btmRow = Integer.parseInt(bottomMap.get("row").toString());
		int btmErrQtyTmp = Integer.parseInt(shqdMap.get(0).get("errQty_tmp").toString());
		if(f.intValue() != btmErrQtyTmp){
			errorInfo.append(errorNum+++chnlName+"公司_疑似养卡渠道数量及占比_养卡号码数量不同").append("\r\n");
			sheet.getRow(btmRow).getCell(3).setCellStyle(createStyle1(workbook));
		}
		//占比per-》qtyPercent
		float btmPer = Float.parseFloat(bottomMap.get("per").toString());
		float btmQtyPercent = Float.parseFloat(shqdMap.get(0).get("qtyPercent").toString().replace("%", ""));
		if(btmPer != btmQtyPercent){
			errorInfo.append(errorNum+++chnlName+"公司_疑似养卡渠道数量及占比_占比不同").append("\r\n");
			sheet.getRow(btmRow).getCell(4).setCellStyle(createStyle1(workbook));
		}
		//养卡号码数量环比增幅ratio-》abs_fbQtyPercent
		DecimalFormat df = new DecimalFormat("0.00");
		String btmRatio = Math.abs(Double.parseDouble(bottomMap.get("ratio").toString()))+"";
		double btmRatioDou = Double.parseDouble(btmRatio);
		String btmAbsFbQtyPercent = shqdMap.get(0).get("abs_fbQtyPercent").toString();
		btmAbsFbQtyPercent = btmAbsFbQtyPercent.substring(2);
		double btmAbsFbQtyPercentdel = Double.parseDouble(btmAbsFbQtyPercent);
		if(df.format(btmRatioDou) != df.format(btmAbsFbQtyPercentdel)){
			errorInfo.append(errorNum+++chnlName+"公司_疑似养卡渠道数量及占比_养卡号码数量环比增幅不同").append("\r\n");
			sheet.getRow(btmRow).getCell(5).setCellStyle(createStyle1(workbook));
		}
		
		float topNum = Float.parseFloat(topMap.get("num").toString());
		Float f1 = new Float(topNum);
		int topRow = Integer.parseInt(topMap.get("row").toString());
		int topErrQtyTmp = Integer.parseInt(shqdMap.get(0).get("errQty_tmp").toString());
		if(f1.intValue() != topErrQtyTmp){
			errorInfo.append(errorNum+++chnlName+"公司_疑似养卡号码数量及占比_养卡号码数量不同").append("\r\n");
			sheet.getRow(topRow).getCell(3).setCellStyle(createStyle1(workbook));
		}
		//占比per-》qtyPercent
		float topPer = Float.parseFloat(topMap.get("per").toString());
		float topQtyPercent = Float.parseFloat(shqdMap.get(0).get("qtyPercent").toString().replace("%", ""));
		if(topPer != topQtyPercent){
			errorInfo.append(errorNum+++chnlName+"公司_疑似养卡号码数量及占比_占比不同").append("\r\n");
			sheet.getRow(topRow).getCell(4).setCellStyle(createStyle1(workbook));
		}
		//养卡号码数量环比增幅ratio-》abs_fbQtyPercent
		String topRatio = Math.abs(Double.parseDouble(bottomMap.get("ratio").toString()))+"";
		double topRatioDou = Double.parseDouble(topRatio);
		String topAbsFbQtyPercent = shqdMap.get(0).get("abs_fbQtyPercent").toString();
		topAbsFbQtyPercent = topAbsFbQtyPercent.substring(2);
		double topAbsFbQtyPercentdel = Double.parseDouble(topAbsFbQtyPercent);
		if(df.format(topRatioDou) != df.format(topAbsFbQtyPercentdel)){
			errorInfo.append(errorNum+++chnlName+"公司_疑似养卡号码数量及占比_养卡号码数量环比增幅不同").append("\r\n");
			sheet.getRow(topRow).getCell(5).setCellStyle(createStyle1(workbook));
		}
		return errorNum;
	}

	//比对全公司的数据
	private int getAllShqdMatch(StringBuffer errorInfo, int errorNum,
			List<Map<String, Object>> qdMap, XSSFWorkbook workbook, Sheet sheet,
			List<String> total,String chnlName) {
		//渠道入网号码数量       疑似养卡号码数量       疑似养卡号码占比%    涉及社会渠道数量      涉及社会渠道占比%
		int totalQtyInt = Integer.parseInt(qdMap.get(0).get("totalQty_tmp").toString());
		int errQtyInt = Integer.parseInt(qdMap.get(0).get("errQty_tmp").toString());
		float qtyPercent = Float.parseFloat(qdMap.get(0).get("qtyPercent").toString().replaceAll("%", ""));
		int errChnlQty = Integer.parseInt(qdMap.get(0).get("errChnlQty_tmp").toString());
		float qtyChnlPercent = Float.parseFloat(qdMap.get(0).get("qtyChnlPercent").toString().replaceAll("%", ""));
		DecimalFormat df = new DecimalFormat("0.00");
		
		String totalQtyStr = df.format((float)totalQtyInt/10000).toString();
		String totalZeroStr = total.get(0).replaceAll("万", "").toString();
		
		if(!totalQtyStr.equals(totalZeroStr)){
			errorInfo.append(errorNum+++chnlName+"_渠道入网号码数量不相同").append("\r\n");
			sheet.getRow(6).getCell(3).setCellStyle(createStyle(workbook));
		}
		String errQtyStr = df.format((float)errQtyInt/10000).toString();
		String totalOneStr = total.get(1).replaceAll("万", "");
		if(errQtyStr.equals(totalOneStr)){
			errorInfo.append(errorNum+++chnlName+"_疑似养卡号码数量不相同").append("\r\n");
			sheet.getRow(7).getCell(3).setCellStyle(createStyle(workbook));
		}
		if(qtyPercent != Float.parseFloat(total.get(2))*100){
			errorInfo.append(errorNum+++chnlName+"_疑似养卡号码占比不同").append("\r\n");
			errorInfo.append(Float.parseFloat(total.get(2))*100);
			sheet.getRow(8).getCell(3).setCellStyle(createStyle0(workbook));
		}
		if(errChnlQty*100 != Float.parseFloat(total.get(3))*100){ 
			errorInfo.append(errorNum+++chnlName+"_涉及社会渠道数量不同").append("\r\n");
			sheet.getRow(9).getCell(3).setCellStyle(createStyle1(workbook));
		}
		if(qtyChnlPercent != Float.parseFloat(total.get(4))*100){
			errorInfo.append(errorNum+++chnlName+"_涉及社会渠道占比不同").append("\r\n");
			sheet.getRow(10).getCell(3).setCellStyle(createStyle0(workbook));
		}
		return errorNum;
	}

	private List<Map<String, Object>> getReportList(List<String> excleList) {
		Map<String, Object> params = new HashMap<String, Object>();
		//通过Excel名称获取审计月, 通过审计月和文档类型获取审计报告的数据集合
		Pattern p=Pattern.compile("-(\\w+).");
	    Matcher m=p.matcher(excleList.get(0));
	    while(m.find()){
	        params.put("audTrm",m.group(1));
	    }
	    params.put("reportType", "渠道养卡");
	    List<Map<String, Object>> reportList = comparisonService.getQDYKList(params);
		return reportList;
	}

	private List<Map<String, Object>> getAllExcleList(List<String> excleList)
			throws FileNotFoundException, IOException, InvalidFormatException {
		List<Map<String, Object>> allExcleList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < excleList.size(); i++) {
			//将查询excle文档的三个部分的数据封装到map里面
			Map<String, Object> excleMap = new HashMap<String, Object>();
			//创建要读入的文件的输入流 
	        InputStream inp = new FileInputStream(FILE_PATH+excleList.get(i)); 
	        //根据上述创建的输入流 创建工作簿对象 
	        Workbook wb = WorkbookFactory.create(inp); 
	        //得到第一页 sheet 
	        //页Sheet是从0开始索引的 
	        Sheet sheet = wb.getSheetAt(0);
	        List<String> tatolList = new ArrayList<String>();
	        String sheetName = excleList.get(i);
	        sheetName = sheetName.substring(3,sheetName.length()-12);
	        //总体情况
	        for(int j = 6;j<11;j++){
	        	tatolList.add(sheet.getRow(j).getCell(3).toString());
	        }
	        //各公司疑似养卡号码数量及占比 row prvdName num per ratio
	        List<Map<String, Object>> topNumAndPerList = new ArrayList<Map<String,Object>>();
	        for(int m = 17;m<48;m++){
	        	Map<String, Object> map = new HashMap<String, Object>();
	        	map.put("row", m);
	        	map.put("prvdName", sheet.getRow(m).getCell(2).toString());
	        	map.put("num", sheet.getRow(m).getCell(3).toString());
	        	map.put("per", sheet.getRow(m).getCell(4).toString());
	        	map.put("ratio", sheet.getRow(m).getCell(5).toString());
	        	topNumAndPerList.add(map);
	        }
	        //各公司疑似养卡渠道数量及占比
	        List<Map<String, Object>> bottomNumAndPerList = new ArrayList<Map<String,Object>>();
	        for(int m = 56;m<85;m++){
	        	Map<String, Object> map = new HashMap<String, Object>();
	        	map.put("row", m);
	        	map.put("prvdName", sheet.getRow(m).getCell(2).toString());
	        	map.put("num", sheet.getRow(m).getCell(3).toString());
	        	map.put("per", sheet.getRow(m).getCell(4).toString());
	        	map.put("ratio", sheet.getRow(m).getCell(5).toString());
	        	bottomNumAndPerList.add(map);
	        }
	        
	        excleMap.put("sheetName", sheetName);
	        excleMap.put("tatol", tatolList);
	        excleMap.put("top", topNumAndPerList);
	        excleMap.put("bottom", bottomNumAndPerList);
	        
	        allExcleList.add(excleMap);
		}
		return allExcleList;
	} 
	
	
	private CellStyle createStyle(Workbook wb){
		CellStyle cs = wb.createCellStyle();
		cs.setAlignment(CellStyle.ALIGN_CENTER);  
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER); 
		Font textFont = wb.createFont(); //字体颜色
		textFont.setFontName("宋体"); 
		textFont.setFontHeightInPoints((short)12);
		textFont.setColor(HSSFColor.RED.index);
		cs.setFont(textFont);
		return cs;
	}
	private CellStyle createStyle0(Workbook wb){
		CellStyle cs = wb.createCellStyle();
		cs.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
		cs.setAlignment(CellStyle.ALIGN_CENTER);  
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER); 
		Font textFont = wb.createFont(); //字体颜色
		textFont.setFontName("宋体"); 
		textFont.setFontHeightInPoints((short)12);
		textFont.setColor(HSSFColor.RED.index);
		cs.setFont(textFont);
		return cs;
	}
	private CellStyle createStyle1(Workbook wb){
		CellStyle cs = wb.createCellStyle();
		cs.setDataFormat(HSSFDataFormat.getBuiltinFormat("###,###"));
		cs.setAlignment(CellStyle.ALIGN_CENTER);  
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER); 
		Font textFont = wb.createFont(); //字体颜色
		textFont.setFontName("宋体"); 
		textFont.setFontHeightInPoints((short)12);
		textFont.setColor(HSSFColor.RED.index);
		cs.setFont(textFont);
		return cs;
	}
	
	
	private void createTxt(String s,String filePath){
		PrintWriter out;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
			out.print(s);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
