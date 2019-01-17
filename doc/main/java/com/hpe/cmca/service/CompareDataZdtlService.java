package com.hpe.cmca.service;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.pojo.DataCompare;

@Service
public class CompareDataZdtlService  extends BaseObject{
	
	@Autowired
	private MybatisDao mybatisDao;
	
	private  String FILE_PATH_EXCEL = "";
	
	private  String FILE_PATH_WORD = "";
		 
	private  String FILE_PATH_CSV = "D:/审计报告/终端套利/csv/";
	
	private  String FILE_PATH_RESULT = "D:/审计报告/终端套利/result/";
	
	DecimalFormat dfs = new DecimalFormat("0");
	
	DecimalFormat dfs2 = new DecimalFormat("0.0000");
	
	DecimalFormat dfs3 = new DecimalFormat("0.00");
	
	//获取excel数据   
	public  String match(File webExcelDir,File webWordDir,String audMonth) throws Exception{
		logger.error(">>>>>start download......."+webExcelDir);
		logger.error(">>>>>start download FILE_PATH_EXCEL......."+FILE_PATH_EXCEL);
		
		FILE_PATH_EXCEL =webExcelDir.getPath()+File.separator;
		FILE_PATH_WORD = webWordDir.getPath()+File.separator;
		logger.error(">>>>>end download......."+webExcelDir.getPath());
		logger.error(">>>>>end download FILE_PATH_EXCEL......."+FILE_PATH_EXCEL);
		String resultList="";
		InputStream inp =null;
		XSSFWorkbook wb =null;
		try{
		File file = new File(FILE_PATH_EXCEL);
		logger.error(">>>>>file length......."+file.length());
	    File[] filelist = file.listFiles();
	    logger.error(">>>>>filelist length......."+filelist.toString());
	    logger.error(">>>>>filelist length......."+filelist.length);
	    for (File f:filelist){
	    	System.out.println("文件：：："+f.getName());
	    	logger.error(">>>>>fileName......."+f.getName());
	        if (f.isFile() && f.getName().contains("社会渠道终端套利排名汇总"+"-"+audMonth)&&(f.getName().endsWith(".xls") || f.getName().endsWith(".xlsx"))){
	        	//创建要读入的文件的输入流 
	        	logger.error(">>>>>start redIO......."+FILE_PATH_EXCEL+f.getName());
	        	logger.error(">>>>>start redIO.......");
		         inp = new FileInputStream(FILE_PATH_EXCEL+f.getName());
		         logger.error(">>>>>end redIO......."+inp);
		        //根据上述创建的输入流 创建工作簿对象 
		         wb = new XSSFWorkbook(inp);
		         logger.error(">>>>>end wb......"+wb);
		         logger.error(">>>>>end fatchName......."+f.getName());
		         Map<String, Object> map =getExcelData(wb);
		         logger.error(">>>>>end fatchExcel......."+map);
		         resultList= compareDataToWord(map,wb,FILE_PATH_EXCEL+f.getName(),0,"终端套利排名汇总",audMonth);
	        	// compareDataToCsv(map);
	        }
	    }
		} catch (Exception e) {
			logger.error(">>>>>read excel exception", e);
			throw new RuntimeException(">>>>>read excel exception", e);
		}
	    logger.error(">>>>>end resultList......."+resultList);
		return resultList;
	   
	}
	
	
	public void compareDataToCsv(Map<String,Object> exceldata) throws IOException{
		 int errorNum =1;
		StringBuffer errorInfo= new StringBuffer();
		errorInfo.append("序号|字段名称|审计清单|审计清单值|排名汇总|排名汇总值|比对结果||");
		 //排名汇总
	    List<Map<String,Object>> paiming_list  = (List<Map<String,Object>>)exceldata.get("paiming_list");
	    //沉默套利
    	List<Map<String,Object>> silent_list =(List<Map<String,Object>>)exceldata.get("silent_list");
    	//养机套利 
    	List<Map<String,Object>> yangji_list =(List<Map<String,Object>>)exceldata.get("yangji_list");
    	//拆包套利
    	List<Map<String,Object>> chaibao_list =(List<Map<String,Object>>)exceldata.get("chaibao_list");
    	File file = new File(FILE_PATH_CSV);
	    File[] filelist = file.listFiles();
		for(Map<String,Object> pmdata:paiming_list){
			String prvdname =pmdata.get("prvdName_paiming").toString();
			if(prvdname.equals("合计")){
				prvdname ="全公司";
			}
			System.out.println("审计清单比对.........."+prvdname);
			for(Map<String,Object> cmdata:silent_list){
				if(cmdata.containsValue(prvdname)){
					for(Map<String,Object> yjdata:yangji_list){
						if(yjdata.containsValue(prvdname)){
							for(Map<String,Object> cbdata:chaibao_list){
								if(cbdata.containsValue(prvdname)){
									for (File f:filelist){
										if (f.isFile() && f.getName().contains(prvdname)&&f.getName().endsWith(".csv")){
									         Map<String, Object> csvdata =getCsvData(FILE_PATH_CSV+f.getName());
									         doCompareCsv(pmdata,cmdata,yjdata,cbdata,csvdata,prvdname,errorInfo,errorNum);
										}
									}
								}
							}
						}
					}
				}
			}
			
		}
	}
	//进行比对
	public void doCompareCsv(Map<String, Object> pmdata,Map<String, Object> cmdata,Map<String, Object> yjdata,Map<String, Object> cbdata,
			Map<String, Object> csvdata,String t_prvdname,StringBuffer errorInfo,int errorNum) throws FileNotFoundException, IOException{
		if(isNotEmpty(pmdata.get("ycsale_num")) && isNotEmpty(csvdata.get("ycsale_zdnum"))){
			long pm_num = Long.valueOf(pmdata.get("ycsale_num").toString());
			long csv_num = Long.valueOf(csvdata.get("ycsale_zdnum").toString());
			if(pm_num != csv_num){
				errorInfo.append(errorNum+++"|"+t_prvdname+"异常销售终端数量|D001|"+csv_num+"|A082|"+pm_num+"|不相同  差值"+Math.abs(pm_num-csv_num)).append("||");
			}
		}
		if(isNotEmpty(pmdata.get("ycsale_qudao")) && isNotEmpty(csvdata.get("ycsale_qdcount"))){
			long pm_num = Long.valueOf(pmdata.get("ycsale_qudao").toString());
			long csv_num = Long.valueOf(csvdata.get("ycsale_qdcount").toString());
			if(pm_num != csv_num){
				errorInfo.append(errorNum+++"|"+t_prvdname+"异常销售涉及渠道|D002|"+csv_num+"|A084|"+pm_num+"|不相同  差值"+Math.abs(pm_num-csv_num)).append("||");
			}
		}
		if(isNotEmpty(cmdata.get("silent_zdnum")) && isNotEmpty(csvdata.get("cmzd_count"))){
			long pm_num = Long.valueOf(cmdata.get("silent_zdnum").toString());
			long csv_num = Long.valueOf(csvdata.get("cmzd_count").toString());
			if(pm_num != csv_num){
				errorInfo.append(errorNum+++"|"+t_prvdname+"沉默串码终端数量|D003|"+csv_num+"|A089|"+pm_num+"|不相同  差值"+Math.abs(pm_num-csv_num)).append("||");
			}
		}
		if(isNotEmpty(yjdata.get("yj_zdnum")) && isNotEmpty(csvdata.get("yjzd_count"))){
			long pm_num = Long.valueOf(yjdata.get("yj_zdnum").toString());
			long csv_num = Long.valueOf(csvdata.get("yjzd_count").toString());
			if(pm_num != csv_num){
				errorInfo.append(errorNum+++"|"+t_prvdname+"养机终端数量|D004|"+csv_num+"|A091|"+pm_num+"|不相同  差值"+Math.abs(pm_num-csv_num)).append("||");
			}
		}
		if(isNotEmpty(cbdata.get("chaibao_num")) && isNotEmpty(csvdata.get("cbzd_count"))){
			long pm_num = Long.valueOf(cbdata.get("chaibao_num").toString());
			long csv_num = Long.valueOf(csvdata.get("cbzd_count").toString());
			if(pm_num != csv_num){
				errorInfo.append(errorNum+++"|"+t_prvdname+"拆包终端数量|D005|"+csv_num+"|A093|"+pm_num+"|不相同  差值"+Math.abs(pm_num-csv_num)).append("||");
			}
		}
		//输出比对结果
		creatExcel(errorInfo.toString(), FILE_PATH_RESULT+"审计清单比对结果.xlsx");
	}
	
	
	//获取审计清单数据
	public Map<String, Object> getCsvData(String filename) throws IOException{
		 //获取csv数据
		 List<Object> csvdata =readCSVFile(filename);
		 //渠道标识
		 List<String> qdflag_list =new ArrayList<String>();
		 //IMEI列
		 List<String> imei_list =new ArrayList<String>();
		 
		 //省异常销售终端数量  省异常销售涉及渠道  省沉默串码终端数量     省养机终端数量   省拆包终端数量
		 int ycsale_zdnum = 0,ycsale_qdcount =0,cmzd_count = 0,yjzd_count=0,cbzd_count =0;
		 for(int i = 1;i < csvdata.size();i++){
         	String[] l =(String[])csvdata.get(i);
         	if(l.length > 16){
         		//IMEI列
         		if(isNotEmpty(l[1].trim()) && !imei_list.contains(l[1].trim())){
         			imei_list.add(l[1].trim());
         			ycsale_zdnum++;
         		}
         		//渠道标识列
         		if(isNotEmpty(l[3].trim()) && !qdflag_list.contains(l[3].trim())){
         			qdflag_list.add(l[3].trim());
         			ycsale_qdcount++;
         		}
         		if(isNotEmpty(l[9].trim()) && l[9].trim().contains("3001")){
         			cmzd_count++;
         		}
         		if(isNotEmpty(l[9].trim()) && l[9].trim().contains("3002")){
         			yjzd_count++;
         		}
         		if(isNotEmpty(l[9].trim()) && l[9].trim().contains("3004")){
         			cbzd_count++;
         		}
//         		if(isNotEmpty(l[16].trim()) && !l[16].trim().contains("代理商沉默串码套利")){
//         			cmzd_count++;
//         		}
//         		if(isNotEmpty(l[16].trim()) && !l[16].trim().contains("代理商养机套利")){
//         			yjzd_count++;
//         		}
//         		if(isNotEmpty(l[16].trim()) && !l[16].trim().contains("代理商终端拆包套利")){
//         			cbzd_count++;
//         		}
         	}
         		
         }
        Map<String, Object> map =new HashMap<String, Object>();
        map.put("ycsale_zdnum", ycsale_zdnum);
        map.put("ycsale_qdcount", ycsale_qdcount);
        map.put("cmzd_count", cmzd_count);
        map.put("yjzd_count", yjzd_count);
        map.put("cbzd_count", cbzd_count);
        return map;
	}
	
	 /**
     * 解析csv文件 到一个list中
     * @return
     * @throws IOException
     */
    public List<Object> readCSVFile(String filename) throws IOException {
    	 InputStreamReader fr = null;
	      BufferedReader br = null;
	      fr = new InputStreamReader(new FileInputStream(filename));
            br = new BufferedReader(fr);
            String rec = null;//一行
            String str ="";//一个单元格
            List<Object> listFile = new ArrayList<Object>();
            try {                        
            	  //读取一行
                while ((rec = br.readLine()) != null) {
                	listFile.add(rec.split(","));
                }                        
            } catch (Exception e) {
                    e.printStackTrace();
            } finally {
                    if (fr != null) {
                            fr.close();
                    }
                    if (br != null) {
                            br.close();
                    }
            }
            return listFile;
    }
	
    //去重list
//    1.	HashSet h  =   new  HashSet(qdflag_list); 
//	        qdflag_list.clear(); 
//	        qdflag_list.addAll(h);
    
// 	2.  ArrayList<String> result = new ArrayList<String>();
//	        for(String s: sources){
//	            if(Collections.frequency(reslut, s) < 1) result.add(s);
//	        }
	
	//排名汇总文件数据
	public Map<String, Object> getExcelData(XSSFWorkbook wb){
		logger.error(">>>>> start  map.......");
	//此处没有循环合计这一行，是由于poi读取行数表格只支持3.1版本，系统目前使用3.9
		/*XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow xssfRow1 = sheet.getRow(33);
		XSSFCell cell3 = xssfRow1.getCell(1);
		XSSFFormulaEvaluator evaluator = new XSSFFormulaEvaluator(wb); 
		CellValue tempCellValue = evaluator.evaluate(cell3);  
        double iCellValue = tempCellValue.getNumberValue();  
        System.err.println(iCellValue);
        return null;*/
		Sheet sheet =null,sheet2=null,sheet3=null,sheet4=null,sheet5=null;
		 //排名汇总
		 List<Map<String, Object>> paiming_list =new ArrayList<Map<String, Object>>();
		 //沉默终端套利
		 List<Map<String, Object>> silent_list =new ArrayList<Map<String, Object>>();
		 //养机套利
		 List<Map<String, Object>> yangji_list =new ArrayList<Map<String, Object>>();
		 //拆包套利
		 List<Map<String, Object>> chaibao_list =new ArrayList<Map<String, Object>>();
		 //跨省串货套利
		 List<Map<String, Object>> chuanhuo_list =new ArrayList<Map<String, Object>>();
		 Map<String, Object> map =new HashMap<String, Object>();
		 Map<String, Object> exceldata = null;
		 logger.error(">>>>>read1  map.......");
		//得到第一页 sheet  排名汇总
	        sheet = wb.getSheetAt(0);
	        for(int m = 2;m < 33;m++){
	        	exceldata = new HashMap<String, Object>();
	        	exceldata.put("row", m);
	        	exceldata.put("sheet", 0);
	        	exceldata.put("prvdName_paiming", sheet.getRow(m).getCell(0).toString());  			//省份
	        	exceldata.put("zdsale_num", getCellValue_2(sheet.getRow(m).getCell(1))); 			//终端销售数量
	        	exceldata.put("zdsale_qudao", getCellValue_2(sheet.getRow(m).getCell(2)));  		//终端销售渠道
	        	exceldata.put("ycsale_num", getCellValue_2(sheet.getRow(m).getCell(3)));   			//异常销售数量
	        	exceldata.put("ycsale_qudao", getCellValue_2(sheet.getRow(m).getCell(4)));   		//异常销售涉及渠道
	        	exceldata.put("ycsale_percent", getCellValueForPer(sheet.getRow(m).getCell(5)));   	//异常销售占比
	        	exceldata.put("ycsale_perpm", getCellValue_2(sheet.getRow(m).getCell(6)));   		//异常销售占比排名
	        	exceldata.put("tlzd_num", getCellValue_2(sheet.getRow(m).getCell(7)));   			//套利终端数量
	        	exceldata.put("tl_amount", getCellValue_2(sheet.getRow(m).getCell(8)));   			//套利金额
	        	exceldata.put("tlzd_qudao", getCellValue_2(sheet.getRow(m).getCell(9)));   			//终端套利涉及渠道
	        	exceldata.put("tlzd_saleper", getCellValueForPer(sheet.getRow(m).getCell(10)));   	//套利终端占销售比
	        	exceldata.put("tlzd_ycper", getCellValueForPer(sheet.getRow(m).getCell(11)));   	//套利终端占异常终端比
	        	exceldata.put("tlzd_ycperpm", getCellValue_2(sheet.getRow(m).getCell(12)));   		//套利终端占异常终端比排名
	        	paiming_list.add(exceldata);
	        }
	        logger.error(">>>>>read2  map.......");
	      //得到第二页 sheet 沉默终端套利
	        sheet2 = wb.getSheetAt(1);
	        for(int m = 2;m < 33;m++){
	        	exceldata = new HashMap<String, Object>();
	        	exceldata.put("row", m);
	        	exceldata.put("sheet", 1);
	        	exceldata.put("prvdName_silent", getCellValue_2(sheet2.getRow(m).getCell(0)));  	//省份
	        	exceldata.put("silent_zdnum", getCellValue_2(sheet2.getRow(m).getCell(1))); 		//沉默套利终端数量
	        	exceldata.put("taoli_amount", getCellValue_2(sheet2.getRow(m).getCell(2)));  		//套利金额
	        	exceldata.put("taoli_qudao", getCellValue_2(sheet2.getRow(m).getCell(3)));   		//沉默套利涉及渠道数量
	        	exceldata.put("silent_zdpercent", getCellValueForPer(sheet2.getRow(m).getCell(4)));  //沉默套利终端占总销量比
	        	exceldata.put("sl_zdperpm", getCellValue_2(sheet2.getRow(m).getCell(5)));   		//沉默套利终端占比排名
	        	silent_list.add(exceldata);
	        }
	        logger.error(">>>>>read3  map.......");
	        //得到第三页 sheet 养机套利
	        sheet3 = wb.getSheetAt(2);
	        for(int m = 2;m < 33;m++){
	        	exceldata = new HashMap<String, Object>();
	        	exceldata.put("row", m);
	        	exceldata.put("sheet", 2);
	        	exceldata.put("prvdName_yangji", getCellValue_2(sheet3.getRow(m).getCell(0)));  	//省份
	        	exceldata.put("yj_zdnum",getCellValue_2(sheet3.getRow(m).getCell(1))); 				//养机套利终端数量
	        	exceldata.put("taoli_amount", getCellValue_2(sheet3.getRow(m).getCell(2)));  		//套利金额
	        	exceldata.put("taoli_qudao", getCellValue_2(sheet3.getRow(m).getCell(3)));   		//养机套利涉及渠道数量
	        	exceldata.put("yj_zdpercent", getCellValueForPer(sheet3.getRow(m).getCell(4)));   	//养机套利终端占总销量比
	        	exceldata.put("yj_zdperpm", getCellValue_2(sheet3.getRow(m).getCell(5)));   		//养机套利终端占比排名
	        	yangji_list.add(exceldata);
	        }
	        logger.error(">>>>>read4  map.......");
	        //得到第四页 sheet 拆包套利
	        sheet4 = wb.getSheetAt(3);
	        for(int m = 2;m < 33;m++){
	        	exceldata = new HashMap<String, Object>();
	        	exceldata.put("row", m);
	        	exceldata.put("sheet", 3);
	        	exceldata.put("prvdName_chaibao", getCellValue_2(sheet4.getRow(m).getCell(0)));  	//省份
	        	exceldata.put("chaibao_num", getCellValue_2(sheet4.getRow(m).getCell(1))); 			//拆包数量
	        	exceldata.put("chaibao_qudao", getCellValue_2(sheet4.getRow(m).getCell(2)));  		//拆包涉及渠道数量
	        	exceldata.put("taoli_zdnum", getCellValue_2(sheet4.getRow(m).getCell(3)));   		//拆包套利终端数量
	        	exceldata.put("taoli_amount", getCellValue_2(sheet4.getRow(m).getCell(4)));   		//套利金额
	        	exceldata.put("chaibao_tlqudao", getCellValue_2(sheet4.getRow(m).getCell(5)));   	//拆包套利涉及渠道数量
	        	exceldata.put("chaibao_per", getCellValueForPer(sheet4.getRow(m).getCell(6)));   	//拆包占总销量比
	        	exceldata.put("chaibao_zdper", getCellValueForPer(sheet4.getRow(m).getCell(7)));   	//拆包套利终端占拆包终端比
	        	exceldata.put("cb_zdperpm", getCellValue_2(sheet4.getRow(m).getCell(8)));   		//终端拆包占比排名
	        	exceldata.put("cbtl_zdperpm", getCellValue_2(sheet4.getRow(m).getCell(9)));   		//拆包套利终端占比排名
	        	chaibao_list.add(exceldata);
	        }
	        logger.error(">>>>>read5  map.......");
	        //得到第五页 sheet 跨省串货套利
	        sheet5 = wb.getSheetAt(4);
	        for(int m = 2;m < 33;m++){
	        	exceldata = new HashMap<String, Object>();
	        	exceldata.put("row", m);
	        	exceldata.put("sheet", 4);
	        	exceldata.put("prvdName_chuanhuo", getCellValue_2(sheet5.getRow(m).getCell(0)));  	//省份
	        	exceldata.put("chuanhuo_num", getCellValue_2(sheet5.getRow(m).getCell(1))); 		//跨省窜货数量
	        	exceldata.put("chuanhuo_qudao", getCellValue_2(sheet5.getRow(m).getCell(2)));  		//跨省窜货渠道数量
	        	exceldata.put("taoli_zdnum", getCellValue_2(sheet5.getRow(m).getCell(3)));   		//跨省窜货套利终端数量
	        	exceldata.put("taoli_amount", getCellValue_2(sheet5.getRow(m).getCell(4)));   		//套利金额
	        	exceldata.put("chuanhuo_tlqudao", getCellValue_2(sheet5.getRow(m).getCell(5)));   	//跨省窜货套利渠道数量
	        	exceldata.put("chuanhuo_per", getCellValue_2(sheet5.getRow(m).getCell(6)));   		//跨省窜货占总销量比
	        	exceldata.put("chuanhuo_zdper", getCellValueForPer(sheet5.getRow(m).getCell(7)));   //跨省窜货套利终端占跨省窜货终端比
	        	exceldata.put("ch_zdperpm", getCellValue_2(sheet5.getRow(m).getCell(8)));   		//终端跨省窜货占比排名
	        	exceldata.put("kschtl_zdperpm", getCellValue_2(sheet5.getRow(m).getCell(9)));   	//跨省窜货套利终端占比排名
	        	chuanhuo_list.add(exceldata);
	        }
	      logger.error(">>>>>end5  map.......");
	      map.put("paiming_list", paiming_list);
	      map.put("silent_list", silent_list);
	      map.put("yangji_list", yangji_list);
	      map.put("chaibao_list", chaibao_list);
	      map.put("chuanhuo_list", chuanhuo_list);
	      logger.error(">>>>> end readExcel  map.......");
		  return map;
	}
	
	public String getCellValueForPer(Cell cell){
		String returnvalue = null;
		double d =cell.getNumericCellValue();
		BigDecimal a1 =new BigDecimal(d);
		BigDecimal a2 =new BigDecimal(100);
		returnvalue =dfs3.format(a1.multiply(a2).doubleValue());
		return returnvalue;
	}
	

	public String getCellValue(Cell cell) {
		String returnvalue = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			returnvalue = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				returnvalue = cell.getDateCellValue().toLocaleString();
			} else {
				if (isNumber(cell.getNumericCellValue())) {
					returnvalue = dfs.format(cell.getNumericCellValue());
				} else {
					returnvalue = dfs2.format(cell.getNumericCellValue());
				}
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			returnvalue = Boolean.toString(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			returnvalue = cell.getStringCellValue();
			break;
		default:
			returnvalue = "";
			break;
		}

		return returnvalue;
	}

	public String getCellValue_2(Cell cell) {
		String returnvalue = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			returnvalue = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				returnvalue = cell.getDateCellValue().toLocaleString();
			} else {
				if (isNumber(cell.getNumericCellValue())) {
					returnvalue = dfs.format(cell.getNumericCellValue());
				} else {
					returnvalue = dfs3.format(cell.getNumericCellValue());
				}
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			returnvalue = Boolean.toString(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			returnvalue = cell.getStringCellValue();
			break;
		default:
			returnvalue = "";
			break;
		}
		return returnvalue;
	}
	public String getCellValue_test(Cell cell) {
		String returnvalue = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			returnvalue = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				returnvalue = cell.getDateCellValue().toLocaleString();
			} else {
				if (isNumber(cell.getNumericCellValue())) {
					returnvalue = dfs.format(cell.getNumericCellValue());
				} else {
					returnvalue = dfs3.format(cell.getNumericCellValue());
				}
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			returnvalue = Boolean.toString(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			
			 System.out.println("Cell Formula="+cell.getCellFormula());  
             System.out.println("Cell Formula Result Type="+cell.getCachedFormulaResultType());  
             if(cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC){  
                 System.out.println("Formula Value="+cell.getNumericCellValue());  
             } 
			
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			returnvalue = String.valueOf(cell.getNumericCellValue());
			break;
			
		default:
			returnvalue = "";
			break;
		}
		return returnvalue;
	}

	public String getCellValue_3(Cell cell) {
		String returnvalue = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			returnvalue = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				returnvalue = cell.getDateCellValue().toLocaleString();
			} else {
				if (isNumber(cell.getNumericCellValue())) {
					returnvalue = dfs.format(cell.getNumericCellValue());
				} else {
					returnvalue = dfs3.format(cell.getNumericCellValue());
				}
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			returnvalue = Boolean.toString(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			returnvalue = cell.getStringCellValue();
			break;
		default:
			returnvalue = "";
			break;
		}
		return returnvalue;
	}
	
	public boolean isNumber(double num){
		boolean n =true;
		char a2 ='0';
		String str_num =Double.toString(num);
		if(str_num.indexOf(".") >= 0){
			String subfix =str_num.substring(str_num.indexOf(".")+1);
			char[] a =subfix.toCharArray();
			for(char a1:a){
				if(a1!=a2){
					n =false;
				}
			}
		}
		return n;
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
	//31省公司表格
//		public String compareDataToWordtablePrvd(Map<String,Object> exceldata,XSSFWorkbook workbook
//				,String filepath,int flag,String chnlname,StringBuffer errorInfo,int errorNum) throws Exception{
//				Paragraph paragraph =null;
//		        try {
//		    	    List<Map<String,Object>> ycsaleper_list =null;
//		    	    List<Map<String,Object>> tlzdper_list =null;
//		    	    List<Map<String,Object>> tlzdnum_list =null;
//		    	    List<Map<String,Object>> ycsalenum_list =null;
//		    	    Map<String,Object> tabledata =null;
//		    	    File file = new File(FILE_PATH_WORD);
//		    	    File[] filelist = file.listFiles();
//		    	    //排名汇总
//		    	    List<Map<String,Object>> paiming_list  = (List<Map<String,Object>>)exceldata.get("paiming_list");
//		    	    	 for (File f:filelist){
//		 	    	    	if (f.isFile() && f.getName().endsWith(".doc")&&f.getName().contains("全公司")){
//		 	    	    		 ycsaleper_list =new ArrayList<Map<String,Object>>();
//		 	    	    		 ycsalenum_list =new ArrayList<Map<String,Object>>();
//			 			         tlzdper_list =new ArrayList<Map<String,Object>>();
//			 			         tlzdnum_list =new ArrayList<Map<String,Object>>();
//		 	    	    		List<String> worddata =getWorddataPrvd(FILE_PATH_WORD+f.getName(),"全公司",errorInfo);
//		 	    	    		//异常销售终端数量排名前5的省公司
//		 	    	    		int i =worddata.indexOf("异常销售终端数量排名前5的分公司");
//		 	    	    		int j =worddata.indexOf("异常销售终端占比排名前5的分公司");
//		 	    	    		int d =worddata.indexOf("异常销售终端数量排名前5的社会渠道");
//		 	    	    		int h =worddata.indexOf("套利终端数量排名前5的分公司");
//		 	    	    		int k =worddata.indexOf("套利终端占比排名前5的分公司");
//		 	    	    		int m =worddata.indexOf("套利终端数量排名前5的社会渠道");
//		 	    	    		for(i=i+5;i < j;i+=4){
//		 	    	    			tabledata =new HashMap<String,Object>();
//		 	    	    			tabledata.put("paiming", worddata.get(i));
//		 			            	tabledata.put("company", worddata.get(i+1));
//		 			            	tabledata.put("counts", worddata.get(i+2));
//		 			            	tabledata.put("percent", worddata.get(i+3));
//		 			            	ycsalenum_list.add(tabledata);
//		 	    	    		}
//		 	    	    		//异常销售终端占比排名前5的省公司
//		 	    	    		for(j =j+5;j < d;i+=4){
//		 	    	    			tabledata =new HashMap<String,Object>();
//		 	    	    			tabledata.put("paiming", worddata.get(j));
//		 			            	tabledata.put("company", worddata.get(j+1));
//		 			            	tabledata.put("counts", worddata.get(j+2));
//		 			            	tabledata.put("percent", worddata.get(j+3));
//		 			            	ycsaleper_list.add(tabledata);
//		 	    	    		}
//		 	    	    		//套利终端数量排名前5的省公司
//		    	    				for(h=h+5;h < k;h+=4){
//			 	    	    			tabledata =new HashMap<String,Object>();
//			 	    	    			tabledata.put("paiming", worddata.get(h));
//			 			            	tabledata.put("company", worddata.get(h+1));
//			 			            	tabledata.put("counts", worddata.get(h+2));
//			 			            	tabledata.put("percent", worddata.get(h+3));
//			 			            	tlzdnum_list.add(tabledata);
//			 	    	    		}
//		    	    				//套利终端占比排名前5的省公司
//		    	    				for(k = k+5;k < m;k+=4){
//			 	    	    			tabledata =new HashMap<String,Object>();
//			 	    	    			tabledata.put("paiming", worddata.get(k));
//			 			            	tabledata.put("company", worddata.get(k+1));
//			 			            	tabledata.put("counts", worddata.get(k+2));
//			 			            	tabledata.put("percent", worddata.get(k+3));
//			 			            	tlzdper_list.add(tabledata);
//			 	    	    		}
//		 			           doCompareAll(workbook,chnlname,paiming_list,ycsaleper_list,tlzdper_list,ycsalenum_list,tlzdnum_list,errorInfo,errorNum);
//		 	    	    	}
//		 	    	    }
//		        }catch(Exception e){
//		        	e.printStackTrace();
//		        }
//		        return errorInfo.toString();
//		}
	//全公司表格
	public String compareDataToWordtable(Map<String,Object> exceldata,XSSFWorkbook workbook
			,String filepath,int flag,String chnlname,StringBuffer errorInfo,int errorNum) throws Exception{
			//Paragraph paragraph =null;
	        try {
	    	    List<Map<String,Object>> ycsaleper_list =null;
	    	    List<Map<String,Object>> tlzdper_list =null;
	    	    List<Map<String,Object>> tlzdnum_list =null;
	    	    List<Map<String,Object>> ycsalenum_list =null;
	    	    Map<String,Object> tabledata =null;
	    	    File file = new File(FILE_PATH_WORD);
	    	    File[] filelist = file.listFiles();
	    	    //排名汇总
	    	    List<Map<String,Object>> paiming_list  = (List<Map<String,Object>>)exceldata.get("paiming_list");
	    	    	 for (File f:filelist){
	 	    	    	if (f.isFile() && f.getName().endsWith(".doc")&&f.getName().contains("全公司")){
	 	    	    		 ycsaleper_list =new ArrayList<Map<String,Object>>();
	 	    	    		 ycsalenum_list =new ArrayList<Map<String,Object>>();
		 			         tlzdper_list =new ArrayList<Map<String,Object>>();
		 			         tlzdnum_list =new ArrayList<Map<String,Object>>();
	 	    	    		List<String> worddata =getWorddata(FILE_PATH_WORD+f.getName(),"全公司",errorInfo);
	 	    	    		//异常销售终端数量排名前5的省公司
	 	    	    		int i =worddata.indexOf("异常销售终端数量排名前5的省公司");
	 	    	    		int j =worddata.indexOf("异常销售终端占比排名前5的省公司");
	 	    	    		int d =worddata.indexOf("异常销售终端数量排名前5的社会渠道");
	 	    	    		int h =worddata.indexOf("套利终端数量排名前5的省公司");
	 	    	    		int k =worddata.indexOf("套利终端占比排名前5的省公司");
	 	    	    		int m =worddata.indexOf("套利终端数量排名前5的社会渠道");
	 	    	    		for(i=i+5;i < j;i+=4){
	 	    	    			tabledata =new HashMap<String,Object>();
	 	    	    			tabledata.put("paiming", worddata.get(i));
	 			            	tabledata.put("company", worddata.get(i+1));
	 			            	tabledata.put("counts", worddata.get(i+2));
	 			            	tabledata.put("percent", worddata.get(i+3));
	 			            	ycsalenum_list.add(tabledata);
	 	    	    		}
	 	    	    		//异常销售终端占比排名前5的省公司
	 	    	    		for(j =j+5;j < d;j+=4){
	 	    	    			tabledata =new HashMap<String,Object>();
	 	    	    			tabledata.put("paiming", worddata.get(j));
	 			            	tabledata.put("company", worddata.get(j+1));
	 			            	tabledata.put("counts", worddata.get(j+2));
	 			            	tabledata.put("percent", worddata.get(j+3));
	 			            	ycsaleper_list.add(tabledata);
	 	    	    		}
	 	    	    		//套利终端数量排名前5的省公司
	    	    				for(h=h+5;h < k;h+=4){
		 	    	    			tabledata =new HashMap<String,Object>();
		 	    	    			tabledata.put("paiming", worddata.get(h));
		 			            	tabledata.put("company", worddata.get(h+1));
		 			            	tabledata.put("counts", worddata.get(h+2));
		 			            	tabledata.put("percent", worddata.get(h+3));
		 			            	tlzdnum_list.add(tabledata);
		 	    	    		}
	    	    				//套利终端占比排名前5的省公司
	    	    				for(k = k+5;k < m;k+=4){
		 	    	    			tabledata =new HashMap<String,Object>();
		 	    	    			tabledata.put("paiming", worddata.get(k));
		 			            	tabledata.put("company", worddata.get(k+1));
		 			            	tabledata.put("counts", worddata.get(k+2));
		 			            	tabledata.put("percent", worddata.get(k+3));
		 			            	tlzdper_list.add(tabledata);
		 	    	    		}
	 			           doCompareAll(workbook,chnlname,paiming_list,ycsaleper_list,tlzdper_list,ycsalenum_list,tlzdnum_list,errorInfo,errorNum);
	 	    	    	}
	 	    	    }
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	        return errorInfo.toString();
	}
	
//	public String compareDataToWordtable(Map<String,Object> exceldata,XSSFWorkbook workbook
//			,String filepath,int flag,String chnlname,StringBuffer errorInfo,int errorNum) throws Exception{
//			Paragraph paragraph =null;
//	        try {
//	    	    List<Map<String,Object>> ycsaleper_list =null;
//	    	    List<Map<String,Object>> tlzdper_list =null;
//	    	    File file = new File(FILE_PATH_WORD);
//	    	    File[] filelist = file.listFiles();
//	    	    //排名汇总
//	    	    List<Map<String,Object>> paiming_list  = (List<Map<String,Object>>)exceldata.get("paiming_list");
//	    	    	 for (File f:filelist){
//	 	    	    	if (f.isFile() && f.getName().endsWith(".doc")&&f.getName().contains("全公司")){
//	 			    	    InputStream is = new FileInputStream(FILE_PATH_WORD+f.getName()); 
//	 			    		//解析word
//	 			            HWPFDocument doc = new HWPFDocument(is);
//	 			            Range range =doc.getRange();
//	 			            ycsaleper_list =new ArrayList<Map<String,Object>>();
//	 			            tlzdper_list =new ArrayList<Map<String,Object>>();
//	 			            Map<String,Object> tabledata =null;
//	 			            Paragraph p =null,p2=null,p3=null,p4=null,p5=null;
//	 			            for(int d = 71;d < 95;d+=5){
//	 			            	 p =range.getParagraph(d);
//	 			            	if(p.text().equals("\r")){
//	 			            		continue;
//	 			            	}
//	 			            	p2 =range.getParagraph(d+1);
//	 			            	p3 =range.getParagraph(d+2);
//	 			            	p4 =range.getParagraph(d+3);
//	 			            	tabledata =new HashMap<String,Object>();
//	 			            	tabledata.put("paiming", p.text().replaceAll("\r", ""));
//	 			            	tabledata.put("company", p2.text().replaceAll("\r", ""));
//	 			            	tabledata.put("counts", p3.text().replaceAll("\r", ""));
//	 			            	tabledata.put("percent", p4.text().replaceAll("\r", ""));
//	 			            	ycsaleper_list.add(tabledata);
//	 			            }
//	 			            for(int d = 151;d < 174;d+=5){
//	 			            	 p =range.getParagraph(d);
//	 			            	if(p.text().equals("\r")){
//	 			            		continue;
//	 			            	}
//	 			            	p2 =range.getParagraph(d+1);
//	 			            	p3 =range.getParagraph(d+2);
//	 			            	p4 =range.getParagraph(d+3);
//	 			            	tabledata =new HashMap<String,Object>();
//	 			            	tabledata.put("paiming", p.text().replaceAll("\r", ""));
//	 			            	tabledata.put("company", p2.text().replaceAll("\r", ""));
//	 			            	tabledata.put("counts", p3.text().replaceAll("\r", ""));
//	 			            	tabledata.put("percent", p4.text().replaceAll("\r", ""));
//	 			            	tlzdper_list.add(tabledata);
//	 			            }
//	 			           doCompareAll(workbook,chnlname,paiming_list,ycsaleper_list,tlzdper_list,errorInfo,errorNum);
//	 	    	    	}
//	 	    	    }
//	        }catch(Exception e){
//	        	e.printStackTrace();
//	        }
//	        return errorInfo.toString();
//	}
	public List getWorddataPrvd(String filename,String prvdname,StringBuffer errorInfo){
		List<String> dataList =new ArrayList<String>();
		try {
	    	SAXReader reader = new SAXReader();  
	    	Document  document = reader.read(new File(filename));  
	    	Element rootElm = document.getRootElement();  
	    	int num =1;
	    	int j =0 ,k = 0;
	    	  // 枚举名称为foo的节点
	    	for ( Iterator i = rootElm.elementIterator("body"); i.hasNext();) {
		           Element foo = (Element) i.next();
		           for ( Iterator i1 = foo.elementIterator("p"); i1.hasNext();) {
	                   Element foo1 = (Element) i1.next();
	                   for ( Iterator i20 = foo1.elementIterator("r"); i20.hasNext();) {
	                       Element foo2 = (Element) i20.next();
//	                       System.out.println(num+".......1......"+foo2.elementText("t")); 
	                       dataList.add(foo2.elementText("t"));
	                       num++;
	                    }
	                }
	               for(Iterator i4 = foo.elementIterator("tbl"); i4.hasNext();) {
	                   Element foo4 = (Element) i4.next();
	                   for (Iterator i21 = foo4.elementIterator("tr"); i21.hasNext();) {
	                       Element foo21 = (Element) i21.next();
	                       for ( Iterator i31 = foo21.elementIterator("tc"); i31.hasNext();) {
	                           Element foo31 = (Element) i31.next();
	                           for ( Iterator i41 = foo31.elementIterator("p"); i41.hasNext();) {
	                               Element foo41 = (Element) i41.next();
	                               for ( Iterator i51 = foo41.elementIterator("r"); i51.hasNext();) {
	                                   Element foo5 = (Element) i51.next();
//	                                   System.out.println(num+".......2......"+foo5.elementText("t")); 
	                                   dataList.add(foo5.elementText("t"));
	                                   num++;
	                       /*            if(foo5.elementText("t").contains("异常销售终端数量排名前5的分公司")
	                                		   ||foo5.elementText("t").contains("异常销售终端占比排名前5的分公司")
	                                		   ||foo5.elementText("t").contains("异常销售终端数量排名前5的社会渠道")
	                                		   ||foo5.elementText("t").contains("异常销售终端数量大于10占比最严重的5个社会渠道")
	                                		   ||foo5.elementText("t").contains("套利终端数量排名前5的分公司")
	                                		   ||foo5.elementText("t").contains("套利终端占比排名前5的分公司")
	                                		   ||foo5.elementText("t").contains("套利终端数量排名前5的社会渠道")
	                                		   ||foo5.elementText("t").contains("套利终端数量大于10占比最严重的5个社会渠道")){
	                                	   List<Element> elist = foo31.elements("tbl");
	                                	   if(elist.size() > 0){
	                                		   Element foo43 = elist.get(j);
	                                		   for ( Iterator i24 = foo43.elementIterator("tr"); i24.hasNext();) {
	                                               Element foo24 = (Element) i24.next();
	                                               for ( Iterator i34 = foo24.elementIterator("tc"); i34.hasNext();) {
	                                                   Element foo34 = (Element) i34.next();
	                                                   for ( Iterator i43 = foo34.elementIterator("p"); i43.hasNext();) {
	                                                       Element foo53 = (Element) i43.next();
	                                                       for ( Iterator i53 = foo53.elementIterator("r"); i53.hasNext();) {
	                                                           Element foo55 = (Element) i53.next();
//	                                                           System.out.println(num+".......3......"+foo55.elementText("t")); 
	                                                           dataList.add(foo55.elementText("t"));
	                                                           num++;
	                                                           k++;
	                                                        }
	                                                    }
	                                                  
	                                                }
	                                              
	                                            }
	                                		   if(((k==30&&(foo5.elementText("t").contains("异常销售终端数量排名前5的社会渠道")
	                                				   ||foo5.elementText("t").contains("异常销售终端数量大于10占比最严重的5个社会渠道")
	                                				   ||foo5.elementText("t").contains("套利终端数量排名前5的社会渠道")
	                                				   ||foo5.elementText("t").contains("套利终端数量大于10占比最严重的5个社会渠道"))))
	                                				   ||(k==8&&((prvdname.equals("北京")||prvdname.equals("天津")||prvdname.equals("重庆"))
	                                				   ))
                            						   ||(k==24&&(!prvdname.equals("北京")&&!prvdname.equals("天津")&&!prvdname.equals("重庆")
        	                                				   &&(foo5.elementText("t").contains("异常销售终端数量排名前5的分公司")
        	                                						   ||foo5.elementText("t").contains("异常销售终端占比排名前5的分公司")
        	                                						   ||foo5.elementText("t").contains("套利终端数量排名前5的分公司")
        	                                						   ||foo5.elementText("t").contains("套利终端占比排名前5的分公司"))))){
	                                			   k = 0;
	                                		   }else{
	                                			   String e =foo5.elementText("t")+" 数据异常";
	                                			   errorInfo.append(" |"+prvdname+"终端套利审计报告| | | | |"+e).append("||");
	                                			   k = 0;
	                                		   }
	                                		   j++;
	                                	   }
	                                   }*/
	                                }
	                            }
	                           
	                        }
	                      
	                    }
	                }
		          
		        }
	    
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
		return dataList;
	}
	//全公司
	public List getWorddata(String filename,String prvdname,StringBuffer errorInfo){
		List<String> dataList =new ArrayList<String>();
		try {
	    	SAXReader reader = new SAXReader();  
	    	Document  document = reader.read(new File(filename));  
	    	Element rootElm = document.getRootElement();  
	    	int num =1;
	    	int j =0 ,k = 1;
	    	  // 枚举名称为foo的节点
	    	for ( Iterator i = rootElm.elementIterator("body"); i.hasNext();) {
		           Element foo = (Element) i.next();
		           for ( Iterator i1 = foo.elementIterator("p"); i1.hasNext();) {
	                   Element foo1 = (Element) i1.next();
	                   for ( Iterator i20 = foo1.elementIterator("r"); i20.hasNext();) {
	                       Element foo2 = (Element) i20.next();
//	                       System.out.println(num+".......1......"+foo2.elementText("t")); 
	                       dataList.add(foo2.elementText("t"));
	                       num++;
	                    }
	                }
	               for(Iterator i4 = foo.elementIterator("tbl"); i4.hasNext();) {
	                   Element foo4 = (Element) i4.next();
	                   for (Iterator i21 = foo4.elementIterator("tr"); i21.hasNext();) {
	                       Element foo21 = (Element) i21.next();
	                       for ( Iterator i31 = foo21.elementIterator("tc"); i31.hasNext();) {
	                           Element foo31 = (Element) i31.next();
	                           for ( Iterator i41 = foo31.elementIterator("p"); i41.hasNext();) {
	                               Element foo41 = (Element) i41.next();
	                               for ( Iterator i51 = foo41.elementIterator("r"); i51.hasNext();) {
	                                   Element foo5 = (Element) i51.next();
//	                                   System.out.println(num+".......2......"+foo5.elementText("t")); 
	                                   dataList.add(foo5.elementText("t"));
	                                   num++;
	                               /*    if(foo5.elementText("t").contains("异常销售终端数量排名前5的省公司")
	                                		   ||foo5.elementText("t").contains("异常销售终端占比排名前5的省公司")
	                                		   ||foo5.elementText("t").contains("异常销售终端数量排名前5的社会渠道")
	                                		   ||foo5.elementText("t").contains("异常销售终端数量大于100占比最严重的5个社会渠道")
	                                		   ||foo5.elementText("t").contains("套利终端数量排名前5的省公司")
	                                		   ||foo5.elementText("t").contains("套利终端占比排名前5的省公司")
	                                		   ||foo5.elementText("t").contains("套利终端数量排名前5的社会渠道")
	                                		   ||foo5.elementText("t").contains("套利终端数量大于100占比最严重的5个社会渠道")){
	                                	   List<Element> elist = foo31.elements("tbl");
	                                	   if(elist.size() > 0){
	                                		   Element foo43 = elist.get(j);
	                                		   for ( Iterator i24 = foo43.elementIterator("tr"); i24.hasNext();) {
	                                               Element foo24 = (Element) i24.next();
	                                               for ( Iterator i34 = foo24.elementIterator("tc"); i34.hasNext();) {
	                                                   Element foo34 = (Element) i34.next();
	                                                   for ( Iterator i43 = foo34.elementIterator("p"); i43.hasNext();) {
	                                                       Element foo53 = (Element) i43.next();
	                                                       for ( Iterator i53 = foo53.elementIterator("r"); i53.hasNext();) {
	                                                           Element foo55 = (Element) i53.next();
//	                                                           System.out.println(num+".......3......"+foo55.elementText("t")); 
	                                                           dataList.add(foo55.elementText("t"));
	                                                           num++;
	                                                           k++;
	                                                        }
	                                                    }
	                                                  
	                                                }
	                                              
	                                            }
	                                		   if((k==30&&(foo5.elementText("t").contains("异常销售终端数量排名前5的社会渠道")
	                                				   ||foo5.elementText("t").contains("异常销售终端数量大于100占比最严重的5个社会渠道")
	                                				   ||foo5.elementText("t").contains("套利终端数量排名前5的社会渠道")
	                                				   ||foo5.elementText("t").contains("套利终端数量大于100占比最严重的5个社会渠道")
	                                				   ))
	                                				   ||(k==8&&((prvdname.equals("北京")||prvdname.equals("天津")||prvdname.equals("重庆"))))
                            						   ||(k==24&&(!prvdname.equals("北京")&&!prvdname.equals("天津")&&!prvdname.equals("重庆")
        	                                				   &&(foo5.elementText("t").contains("异常销售终端数量排名前5的省公司")
        	                                						   ||foo5.elementText("t").contains("异常销售终端占比排名前5的省公司")
        	                                						   ||foo5.elementText("t").contains("套利终端数量排名前5的省公司")
        	                                						   ||foo5.elementText("t").contains("套利终端占比排名前5的省公司"))))
        	                                		||(k==25&&foo5.elementText("t").contains("异常销售终端数量排名前5的省公司"))){
	                                			   k = 0;
	                                		   }else{
	                                			   String e =foo5.elementText("t")+" 数据异常";
	                                			   errorInfo.append(" |"+prvdname+"终端套利审计报告| | | | |"+e).append("||");
	                                			   k = 0;
	                                		   }
	                                		   j++;
	                                	   }
	                                   }*/
	                                }
	                            }
	                           
	                        }
	                      
	                    }
	                }
		          
		        }
	    
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
		return dataList;
	}
	
	/**
	 * 比对数据
	 * <pre>
	 * Desc  flag 标识渠道类型(0:终端套利排名汇总)
	 * @author issuser
	 * 2017-6-24 下午12:11:15
	 * </pre>
	 * @throws Exception 
	 */
	public String compareDataToWord(Map<String,Object> exceldata,XSSFWorkbook workbook
			,String filepath,int flag,String chnlname,String audMonth) throws Exception{
		    logger.error(">>>>>start compare......."+filepath);
		    String resultList="";
			String para = null;
			int errorNum =1;
			StringBuffer errorInfo= new StringBuffer();
			//errorInfo.append("序号|字段名称|审计报告|审计报告值|排名汇总|排名汇总值|比对结果||");
	        try {
	        	File file = new File(FILE_PATH_WORD);
	    	    File[] filelist = file.listFiles();
	    	    //排名汇总
	    	    List<Map<String,Object>> paiming_list  = (List<Map<String,Object>>)exceldata.get("paiming_list");
	    	    //沉默套利
    	    	List<Map<String,Object>> silent_list =(List<Map<String,Object>>)exceldata.get("silent_list");
    	    	//养机套利
    	    	List<Map<String,Object>> yangji_list =(List<Map<String,Object>>)exceldata.get("yangji_list");
    	    	//拆包套利
    	    	List<Map<String,Object>> chaibao_list =(List<Map<String,Object>>)exceldata.get("chaibao_list");
    	    	//跨省串货套利
    	    	List<Map<String,Object>> chuanhuo_list =(List<Map<String,Object>>)exceldata.get("chuanhuo_list");
    	    	Map<String,Object> word_p =null;
    	    	String t_prvdname =null;
    	    	List<String> dataList =null;
    	    	//比对31个省份
    	    	for(int j = 0;j < paiming_list.size();j++){
    	    		Map<String,Object> pmdata =paiming_list.get(j);
    	    		/*if(pmdata.get("prvdName_paiming").equals("合计")){
    	    			t_prvdname = "全公司";
    	    		}else{*/
    	    			t_prvdname =pmdata.get("prvdName_paiming").toString();
    	    		//}
    	    		System.out.println("比对审计报告............."+t_prvdname);
    	    		for(Map<String,Object> chenmodata:silent_list){
    	    			if(chenmodata.containsValue(t_prvdname)){
    	    				for(Map<String,Object> yangjidata:yangji_list){
    	    	    			if(yangjidata.containsValue(t_prvdname)){
    	    	    				for(Map<String,Object> chaibaodata:chaibao_list){
    	    	    	    			if(chaibaodata.containsValue(t_prvdname)){
    	    	    	    				for(Map<String,Object> chuanhuodata:chuanhuo_list){
    	    	    	    	    			if(chuanhuodata.containsValue(t_prvdname)){
						    	    				for (File f:filelist){
						    	    	    	    	if (f.isFile() && f.getName().endsWith(".doc")&&f.getName().contains(t_prvdname)){
						    	    	    	    		 word_p = new HashMap<String,Object>();
						    	    	    	    		/* if(t_prvdname.equals("全公司")){
						    	    	    	    			 dataList =getWorddata(FILE_PATH_WORD+f.getName(),t_prvdname,errorInfo);
						    	    	    	    		 }else{*/
						    	    	    	    			 dataList =getWorddataPrvd(FILE_PATH_WORD+f.getName(),t_prvdname,errorInfo);
						    	    	    	    		 //}
						    	    	    	    		 String total_salenum = dataList.get(11);
						    	    	    	    		 total_salenum =total_salenum.replace(",", "");
					    	    	    	 	        	  word_p.put("total_salenum", total_salenum);
					    	    	    	 	        	  //异常销售段落
						    	    	    	    		 String ycsale_zdnum =dataList.get(13).substring(dataList.get(13).indexOf("终端数量")+4);
					    	    	    	 	        	  String yszd_numper = dataList.get(15);
					    	    	    	 	        	  String yszdnum_huanbi = dataList.get(17);  //环比
					    	    	    	 	        	  String ysshehui_qudao = dataList.get(19);  //渠道数量
					    	    	    	 	        	  String ysqudao_per = dataList.get(21);	//渠道占比
					    	    	    	 	        	  String yschnl_huanbi =dataList.get(23);	//环比
					    	    	    	 	        	 String s = yszdnum_huanbi.substring(2);
				    	    	    	 		             String s1 = yschnl_huanbi.substring(2);
				    	    	    	 		             if(s.equals("下降")){
				    	    	    	 		            	yszdnum_huanbi ="-"+yszdnum_huanbi.substring(0,2);
				    	    	    	 		             }else{
				    	    	    	 		            	yszdnum_huanbi =yszdnum_huanbi.substring(0,2);
				    	    	    	 		             }
					    	    	    	 		         if(s1.equals("下降")){
					    	    	    	 		        	yschnl_huanbi ="-"+yschnl_huanbi.substring(2);
						    	    	    	 		     }else{
						    	    	    	 		    	yschnl_huanbi =yschnl_huanbi.substring(2);
						    	    	    	 		     }
					    	    	    	 		        ycsale_zdnum =ycsale_zdnum.replace(",", "");
					    	    	    	 		        ysshehui_qudao =ysshehui_qudao.replace(",", "");
					    	    	    	 		       yszd_numper =yszd_numper.replace("%", "");
					    	    	    	 		       ysqudao_per =ysqudao_per.replace("%", "");
					    	    	    	 		       word_p.put("ycsale_zdnum", ycsale_zdnum);
					    	    	    	 		       word_p.put("yszd_numper", yszd_numper);
					    	    	    	 		       word_p.put("yszdnum_huanbi", yszdnum_huanbi);
					    	    	    	 		       word_p.put("ysshehui_qudao", ysshehui_qudao);
					    	    	    	 		       word_p.put("ysqudao_per", ysqudao_per);
					    	    	    	 		       word_p.put("yschnl_huanbi", yschnl_huanbi);
					    	    	    	 		       //套利段落
					    	    	    	 		       String tlzd_num = dataList.get(25).substring(dataList.get(25).indexOf("终端数量")+4);
					    	    	    	 	        	 String tlzd_percent = dataList.get(27);
					    	    	    	 	        	 String tlzdnum_huanbi = dataList.get(29);
					    	    	    	 	        	 String tlshehui_qudao = dataList.get(31);
					    	    	    	 	        	 String tlqudao_per = dataList.get(33);
					    	    	    	 	        	 String tlqudao_huanbi =dataList.get(35);
					    	    	    	 	        	 String ss = tlzdnum_huanbi.substring(0,2);
					    	    	    	 	        	 String ss1 = tlqudao_huanbi.substring(0,2);
					    	    	    	 	        	tlzd_num =tlzd_num.replace(",", "");
					    	    	    	 	        	tlshehui_qudao =tlshehui_qudao.replace(",", "");
					    	    	    	 	        	tlzd_percent =tlzd_percent.replace("%", "");
					    	    	    	 	        	tlqudao_per =tlqudao_per.replace("%", "");
					    	    	    	 	        	 if(ss.equals("下降")){
					    	    	    	 	        		tlzdnum_huanbi ="-"+tlzdnum_huanbi.substring(2);
					 	    	    	 		             }else{
					 	    	    	 		            	tlzdnum_huanbi =tlzdnum_huanbi.substring(2);
					 	    	    	 		             }
					    	    	    	 		         if(ss1.equals("下降")){
					    	    	    	 		        	tlqudao_huanbi ="-"+tlqudao_huanbi.substring(2);
						    	    	    	 		     }else{
						    	    	    	 		    	tlqudao_huanbi =tlqudao_huanbi.substring(2);
						    	    	    	 		     }
				    	    	    	 	        		word_p.put("tlzd_num", tlzd_num);
				    	    	    	 	        		word_p.put("tlzd_percent", tlzd_percent);
				    	    	    	 	        		word_p.put("tlzdnum_huanbi", tlzdnum_huanbi);
				    	    	    	 	        		word_p.put("tlshehui_qudao", tlshehui_qudao);
				    	    	    	 	        		word_p.put("tlqudao_per", tlqudao_per);
				    	    	    	 	        		word_p.put("tlqudao_huanbi", tlqudao_huanbi);
						    	    	    	    		 //异常销售类型   1 沉默串码
				    	    	    	 	        		String chenmo_tlzdnum = dataList.get(60);
				    	    	    	 	          		String chenmo_shqudaonum =dataList.get(62);
				    	    	    	 	          		chenmo_tlzdnum =chenmo_tlzdnum.replace(",", "");
				    	    	    	 	          		chenmo_shqudaonum =chenmo_shqudaonum.replace(",", "");
				    	    	    	 	          		word_p.put("chenmo_tlzdnum", chenmo_tlzdnum);
				    	    	    	 	          		word_p.put("chenmo_shqudaonum", chenmo_shqudaonum);
				    	    	    	 	          		//异常销售类型   2 养机
				    	    	    	 	          		para =dataList.get(64);
				    	    	    	 	          		String yj_tlzdnum = para.substring(para.indexOf("套利终端")+4, para.indexOf("台"));
				    	    	    	 	          		String yj_shqudaonum = para.substring(para.indexOf("社会渠道")+4, para.indexOf("个"));
				    	    	    	 	          		yj_tlzdnum =yj_tlzdnum.replace(",", "");
				    	    	    	 	          		yj_shqudaonum =yj_shqudaonum.replace(",", "");
				    	    	    	 	          		word_p.put("yj_tlzdnum", yj_tlzdnum);
				    	    	    	 	          		word_p.put("yj_shqudaonum", yj_shqudaonum);
				    	    	    	 	          		//异常销售类型   3 拆包
				    	    	    	 	          		String cb_zdnum = dataList.get(66);
				    	    	    	 	          		String cb_shqudaonum = dataList.get(68);
				    	    	    	 	          		para = dataList.get(69);
				    	    	    	 	          		String cb_tlzdnum = para.substring(para.lastIndexOf("终端")+2, para.lastIndexOf("台"));
				    	    	    	 	          		String cb_tlqudaonum = para.substring(para.lastIndexOf("社会渠道")+4, para.lastIndexOf("个"));
				    	    	    	 	          		cb_zdnum =cb_zdnum.replace(",", "");
				    	    	    	 	          		cb_shqudaonum =cb_shqudaonum.replace(",", "");
				    	    	    	 	          		cb_tlzdnum =cb_tlzdnum.replace(",", "");
				    	    	    	 	          		cb_tlqudaonum =cb_tlqudaonum.replace(",", "");
				    	    	    	 	          		word_p.put("cb_zdnum", cb_zdnum);
				    	    	    	 	          		word_p.put("cb_shqudaonum", cb_shqudaonum);
				    	    	    	 	          		word_p.put("cb_tlzdnum", cb_tlzdnum);
				    	    	    	 	          		word_p.put("cb_tlqudaonum", cb_tlqudaonum);
				    	    	    	 	          		//异常销售类型   4 跨省串货
				    	    	    	 	          		String ks_zdnum = dataList.get(71);
				    	    	    	 	          		String ks_shqudaonum = dataList.get(73);
				    	    	    	 	          		para =dataList.get(74);
				    	    	    	 	          		String ks_tlzdnum = para.substring(para.lastIndexOf("终端")+2, para.lastIndexOf("台"));
				    	    	    	 	          		String ks_tlqudaonum = para.substring(para.lastIndexOf("社会渠道")+4, para.lastIndexOf("个"));
				    	    	    	 	          		ks_zdnum =ks_zdnum.replace(",", "");
				    	    	    	 	          		ks_shqudaonum =ks_shqudaonum.replace(",", "");
				    	    	    	 	          		ks_tlzdnum =ks_tlzdnum.replace(",", "");
				    	    	    	 	          		ks_tlqudaonum =ks_tlqudaonum.replace(",", "");
				    	    	    	 	          		word_p.put("ks_zdnum", ks_zdnum);
				    	    	    	 	          		word_p.put("ks_shqudaonum", ks_shqudaonum);
				    	    	    	 	          		word_p.put("ks_tlzdnum", ks_tlzdnum);
				    	    	    	 	          		word_p.put("ks_tlqudaonum", ks_tlqudaonum);
				    	    	    	 	          		//比对
						    	    	    	 	        doCompare(workbook,chnlname,pmdata,chenmodata,yangjidata,chaibaodata,chuanhuodata,word_p,errorInfo,errorNum);
						    	    	    	    	}
						    	    	    	    }
    	    	    	    	    			}
				    	    	    	    }
    	    	    	    			}
		    	    	    	    }
    	    	    			}
    	    	    	    }
    	    			}
    	    		}
    	    		
    	    	}
    	    	//添加终端逃离全公司比对逻辑
    	    	//由于无法获取带函数的excel值,目前通过求和实现
                //获取排名汇总全公司
    	    	
    	    	Map<String,Object> word_ps =null;
    	    	List<String> dataLists =null;
               Map<String, Object> mappm =new HashMap<String, Object>();
 				int zdsale_num = 0,zdsale_qudao=0,ycsale_num=0,ycsale_qudao=0,tlzd_num=0,tlzd_qudao=0;
 				double tl_amount=0.00;	
 				for(Map<String,Object> pm:paiming_list){
 					zdsale_num+=Integer.parseInt(pm.get("zdsale_num").toString());
 					zdsale_qudao+=Integer.parseInt(pm.get("zdsale_qudao").toString());
 					ycsale_num+=Integer.parseInt(pm.get("ycsale_num").toString());
 					ycsale_qudao+=Integer.parseInt(pm.get("ycsale_qudao").toString());
 					tlzd_num+=Integer.parseInt(pm.get("tlzd_num").toString());
 					tlzd_qudao+=Integer.parseInt(pm.get("tlzd_qudao").toString());
 					tl_amount+=Double.valueOf(pm.get("tl_amount").toString());
 				}
 				mappm.put("zdsale_num", zdsale_num);//终端销售数量
 				mappm.put("zdsale_qudao", zdsale_qudao);//终端销售渠道
 				mappm.put("ycsale_num", ycsale_num);//异常销售数量
 				mappm.put("ycsale_qudao", ycsale_qudao);//异常销售涉及渠道
 				mappm.put("tlzd_num", tlzd_num);//套利终端数量
 				mappm.put("tlzd_qudao", tlzd_qudao);//终端套利涉及渠道
 				mappm.put("tl_amount", dfs3.format(tl_amount));////套利金额
 				mappm.put("ycsale_percent", dfs2.format(Double.valueOf(ycsale_num)/(zdsale_num)));//异常占比：
 				mappm.put("tlzd_saleper", dfs2.format(Double.valueOf(tlzd_num)/(zdsale_num)));//销量占比：
 				mappm.put("tlzd_ycper", dfs2.format(Double.valueOf(tlzd_num)/(ycsale_num)));//终端占比：
 				
 				 //获取沉默终端套利全公司
 		        Map<String, Object> mapcm =new HashMap<String, Object>();
 		        int silent_zdnum = 0,taoli_qudao=0;
 		        double taoli_amount=0.00;	
 		        for(Map<String,Object> pm:silent_list){
 		        	silent_zdnum+=Integer.parseInt(pm.get("silent_zdnum").toString());
 		    		taoli_amount+=Double.valueOf(pm.get("taoli_amount").toString());
 		    		taoli_qudao+=Integer.parseInt(pm.get("taoli_qudao").toString());
 		        }
 		        mapcm.put("silent_zdnum", silent_zdnum);//沉默套利终端数量
 		        mapcm.put("taoli_amount", dfs3.format(taoli_amount));////套利金额
 		        mapcm.put("taoli_qudao", taoli_qudao);//沉默套利涉及渠道数量
 		        mapcm.put("sl_zdperpm", dfs2.format(Double.valueOf(silent_zdnum)/(zdsale_num)));//沉默套利终端占总销量比
 		        
 		       //获取养机套利全公司
 		        Map<String, Object> mapyj =new HashMap<String, Object>();
 		        int yj_zdnum = 0,taoli_qudaos=0;
 		        double taoli_amounts=0.00;	
 		        for(Map<String,Object> pm:yangji_list){
 		        	yj_zdnum+=Integer.parseInt(pm.get("yj_zdnum").toString());
 		        	taoli_amounts+=Double.valueOf(pm.get("taoli_amount").toString());
 		        	taoli_qudaos+=Integer.parseInt(pm.get("taoli_qudao").toString());
 		        }
 		        mapyj.put("yj_zdnum", yj_zdnum);//养机套利终端数量
 		        mapyj.put("taoli_amount", dfs3.format(taoli_amounts));////套利金额
 		        mapyj.put("taoli_qudao", taoli_qudaos);//养机套利涉及渠道数量
 		        mapyj.put("yj_zdpercent", dfs2.format(Double.valueOf(yj_zdnum)/(zdsale_num)));//养机套利终端占总销量比
 		        
 		     //获取排名汇总拆包全公司
 		        Map<String, Object> mapcb =new HashMap<String, Object>();
 		        int chaibao_num = 0,chaibao_qudao=0,taoli_zdnums=0,chaibao_tlqudao=0;
 		        double taoli_amountcb=0.00;	
 		        for(Map<String,Object> pm:chaibao_list){
 		        	chaibao_num+=Integer.parseInt(pm.get("chaibao_num").toString());
 		        	chaibao_qudao+=Integer.parseInt(pm.get("chaibao_qudao").toString());
 		        	taoli_zdnums+=Integer.parseInt(pm.get("taoli_zdnum").toString());
 		        	taoli_amountcb+=Double.valueOf(pm.get("taoli_amount").toString());
 		        	chaibao_tlqudao+=Integer.parseInt(pm.get("chaibao_tlqudao").toString());
 		        }
 		       // System.err.println("异常占比："+dfs2.format(Double.valueOf(ycsale_num)/(zdsale_num)));
 		        mapcb.put("chaibao_num", chaibao_num);//拆包数量
 		        mapcb.put("chaibao_qudao", chaibao_qudao);//拆包涉及渠道数量
 		        mapcb.put("taoli_zdnum", taoli_zdnums);//拆包套利终端数量
 		        mapcb.put("taoli_amountcb", dfs3.format(taoli_amountcb));////套利金额
 		        mapcb.put("chaibao_tlqudao", chaibao_tlqudao);//拆包套利涉及渠道数量
 		        mapcb.put("chaibao_per", dfs2.format(Double.valueOf(chaibao_num)/(zdsale_num)));//拆包占总销量比：
 		        mapcb.put("tlzd_saleper", dfs2.format(Double.valueOf(taoli_zdnums)/(chaibao_num)));//拆包套利终端占拆包终端比：
 		        
 		     //获取排名汇总跨省全公司
 		        Map<String, Object> mapks =new HashMap<String, Object>();
 		        int chuanhuo_num = 0,chuanhuo_qudao=0,taoli_zdnumk=0,chuanhuo_tlqudao=0;
 		        double taoli_amountks=0.00;	
 		        for(Map<String,Object> pm:chuanhuo_list){
 		        	chuanhuo_num+=Integer.parseInt(pm.get("chuanhuo_num").toString());
 		        	chuanhuo_qudao+=Integer.parseInt(pm.get("chuanhuo_qudao").toString());
 		        	taoli_zdnumk+=Integer.parseInt(pm.get("taoli_zdnum").toString());
 		        	taoli_amountks+=Double.valueOf(pm.get("taoli_amount").toString());
 		        	chuanhuo_tlqudao+=Integer.parseInt(pm.get("chuanhuo_tlqudao").toString());
 		        }
 		       // System.err.println("异常占比："+dfs2.format(Double.valueOf(ycsale_num)/(zdsale_num)));
 		        mapks.put("chuanhuo_num", chuanhuo_num);//跨省窜货数量
 		        mapks.put("chuanhuo_qudao", chuanhuo_qudao);//跨省窜货渠道数量
 		        mapks.put("taoli_zdnumk", taoli_zdnumk);//拆包套利终端数量
 		        mapks.put("taoli_amountcb", dfs3.format(taoli_amountks));////套利金额
 		        mapks.put("chuanhuo_tlqudao", chuanhuo_tlqudao);//跨省窜货套利渠道数量
 		        mapks.put("chuanhuo_per", dfs2.format(Double.valueOf(chuanhuo_num)/(zdsale_num)));//拆包占总销量比：
 		        mapks.put("chuanhuo_zdper", dfs2.format(Double.valueOf(taoli_zdnumk)/(chuanhuo_num)));//跨省窜货套利终端占跨省窜货终端比：
    	    	
    			for (File f:filelist){
	    	    	if (f.isFile() && f.getName().endsWith(".doc")&&f.getName().contains("全公司")){
	    	    		 word_ps = new HashMap<String,Object>();
	    	    		 dataLists =getWorddata(FILE_PATH_WORD+f.getName(),t_prvdname,errorInfo);
	    	    		 String total_salenum = dataLists.get(11);
	    	    		 total_salenum =total_salenum.replace(",", "");
	    	    		 word_ps.put("total_salenum", total_salenum);
    	 	        	  //异常销售段落
	    	    		 String ycsale_zdnum =dataLists.get(13).substring(dataLists.get(13).indexOf("终端数量")+4);
    	 	        	  String yszd_numper = dataLists.get(15);
    	 	        	  String yszdnum_huanbi = dataLists.get(17);  //环比
    	 	        	  String ysshehui_qudao = dataLists.get(19);  //渠道数量
    	 	        	  String ysqudao_per = dataLists.get(21);	//渠道占比
    	 	        	  String yschnl_huanbi =dataLists.get(23);	//环比
    	 	        	 String s = yszdnum_huanbi.substring(2);
	 		             String s1 = yschnl_huanbi.substring(2);
	 		             if(s.equals("下降")){
	 		            	yszdnum_huanbi ="-"+yszdnum_huanbi.substring(0,2);
	 		             }else{
	 		            	yszdnum_huanbi =yszdnum_huanbi.substring(0,2);
	 		             }
    	 		         if(s1.equals("下降")){
    	 		        	yschnl_huanbi ="-"+yschnl_huanbi.substring(2);
	    	 		     }else{
	    	 		    	yschnl_huanbi =yschnl_huanbi.substring(2);
	    	 		     }
    	 		        ycsale_zdnum =ycsale_zdnum.replace(",", "");
    	 		        ysshehui_qudao =ysshehui_qudao.replace(",", "");
    	 		       yszd_numper =yszd_numper.replace("%", "");
    	 		       ysqudao_per =ysqudao_per.replace("%", "");
    	 		      word_ps.put("ycsale_zdnum", ycsale_zdnum);
    	 		     word_ps.put("yszd_numper", yszd_numper);
    	 		    word_ps.put("yszdnum_huanbi", yszdnum_huanbi);
    	 		   word_ps.put("ysshehui_qudao", ysshehui_qudao);
    	 		  word_ps.put("ysqudao_per", ysqudao_per);
    	 		 word_ps.put("yschnl_huanbi", yschnl_huanbi);
    	 		       //套利段落
    	 		       String tlzd_nums = dataLists.get(25).substring(dataLists.get(25).indexOf("终端数量")+4);
    	 	        	 String tlzd_percent = dataLists.get(27);
    	 	        	 String tlzdnum_huanbi = dataLists.get(29);
    	 	        	 String tlshehui_qudao = dataLists.get(31);
    	 	        	 String tlqudao_per = dataLists.get(33);
    	 	        	 String tlqudao_huanbi =dataLists.get(35);
    	 	        	 String ss = tlzdnum_huanbi.substring(0,2);
    	 	        	 String ss1 = tlqudao_huanbi.substring(0,2);
    	 	        	tlzd_nums =tlzd_nums.replace(",", "");
    	 	        	tlshehui_qudao =tlshehui_qudao.replace(",", "");
    	 	        	tlzd_percent =tlzd_percent.replace("%", "");
    	 	        	tlqudao_per =tlqudao_per.replace("%", "");
    	 	        	 if(ss.equals("下降")){
    	 	        		tlzdnum_huanbi ="-"+tlzdnum_huanbi.substring(2);
 	 		             }else{
 	 		            	tlzdnum_huanbi =tlzdnum_huanbi.substring(2);
 	 		             }
    	 		         if(ss1.equals("下降")){
    	 		        	tlqudao_huanbi ="-"+tlqudao_huanbi.substring(2);
	    	 		     }else{
	    	 		    	tlqudao_huanbi =tlqudao_huanbi.substring(2);
	    	 		     }
    	 		        word_ps.put("tlzd_num", tlzd_num);
    	 		       word_ps.put("tlzd_percent", tlzd_percent);
    	 		      word_ps.put("tlzdnum_huanbi", tlzdnum_huanbi);
    	 		     word_ps.put("tlshehui_qudao", tlshehui_qudao);
    	 		    word_ps.put("tlqudao_per", tlqudao_per);
    	 		   word_ps.put("tlqudao_huanbi", tlqudao_huanbi);
	    	    		 //异常销售类型   1 沉默串码
	 	        		String chenmo_tlzdnum = dataLists.get(60);
	 	          		String chenmo_shqudaonum =dataLists.get(62);
	 	          		chenmo_tlzdnum =chenmo_tlzdnum.replace(",", "");
	 	          		chenmo_shqudaonum =chenmo_shqudaonum.replace(",", "");
	 	          		word_ps.put("chenmo_tlzdnum", chenmo_tlzdnum);
	 	          		word_ps.put("chenmo_shqudaonum", chenmo_shqudaonum);
	 	          		//异常销售类型   2 养机
	 	          		para =dataLists.get(64);
	 	          		String yj_tlzdnum = para.substring(para.indexOf("套利终端")+4, para.indexOf("台"));
	 	          		String yj_shqudaonum = para.substring(para.indexOf("社会渠道")+4, para.indexOf("个"));
	 	          		yj_tlzdnum =yj_tlzdnum.replace(",", "");
	 	          		yj_shqudaonum =yj_shqudaonum.replace(",", "");
	 	          		word_ps.put("yj_tlzdnum", yj_tlzdnum);
	 	          		word_ps.put("yj_shqudaonum", yj_shqudaonum);
	 	          		//异常销售类型   3 拆包
	 	          		String cb_zdnum = dataLists.get(66);
	 	          		String cb_shqudaonum = dataLists.get(68);
	 	          		para = dataLists.get(69);
	 	          		String cb_tlzdnum = para.substring(para.lastIndexOf("终端")+2, para.lastIndexOf("台"));
	 	          		String cb_tlqudaonum = para.substring(para.lastIndexOf("社会渠道")+4, para.lastIndexOf("个"));
	 	          		cb_zdnum =cb_zdnum.replace(",", "");
	 	          		cb_shqudaonum =cb_shqudaonum.replace(",", "");
	 	          		cb_tlzdnum =cb_tlzdnum.replace(",", "");
	 	          		cb_tlqudaonum =cb_tlqudaonum.replace(",", "");
	 	          		word_ps.put("cb_zdnum", cb_zdnum);
	 	          		word_ps.put("cb_shqudaonum", cb_shqudaonum);
	 	          		word_ps.put("cb_tlzdnum", cb_tlzdnum);
	 	          		word_ps.put("cb_tlqudaonum", cb_tlqudaonum);
	 	          		//异常销售类型   4 跨省串货
	 	          		String ks_zdnum = dataLists.get(71);
	 	          		String ks_shqudaonum = dataLists.get(73);
	 	          		para =dataLists.get(74);
	 	          		String ks_tlzdnum = para.substring(para.lastIndexOf("终端")+2, para.lastIndexOf("台"));
	 	          		String ks_tlqudaonum = para.substring(para.lastIndexOf("社会渠道")+4, para.lastIndexOf("个"));
	 	          		ks_zdnum =ks_zdnum.replace(",", "");
	 	          		ks_shqudaonum =ks_shqudaonum.replace(",", "");
	 	          		ks_tlzdnum =ks_tlzdnum.replace(",", "");
	 	          		ks_tlqudaonum =ks_tlqudaonum.replace(",", "");
	 	          		word_ps.put("ks_zdnum", ks_zdnum);
	 	          		word_ps.put("ks_shqudaonum", ks_shqudaonum);
	 	          		word_ps.put("ks_tlzdnum", ks_tlzdnum);
	 	          		word_ps.put("ks_tlqudaonum", ks_tlqudaonum);
	 	          		//比对全公司excel和word数据
	    	 	        doCompareAllCompany(workbook,chnlname,mappm,mapcm,mapyj,mapcb,mapks,word_ps,errorInfo,errorNum);
	    	    	}
	    	    }
    	    	
    	    	//比对全公司的table（排名前五）
    	    	//compareDataToWordtable(exceldata,workbook,filepath,flag,chnlname,errorInfo,errorNum);
    	    	//FileOutputStream outPath = new FileOutputStream(filepath);
    			//workbook.write(outPath);
    			//outPath.close();
    		    //createTxt(errorInfo.toString(), FILE_PATH_EXCEL+chnlname+".txt");
    	    	//createTxt(errorInfo.toString(), FILE_PATH_RESULT+"审计报告比对结果.txt");
    	        //creatExcel(errorInfo.toString(), FILE_PATH_RESULT+"审计报告比对结果.xlsx");
        //输出比对结果入库-------------------------------------------------	
    			logger.error(">>>>>end compare......."+errorInfo.toString());
        	  	String result[]=errorInfo.toString().split(";");
        	  	logger.error(">>>>>end compare......."+result);
        	  	//如果excel和word比对结果无异常
        	  	if(!result[0].equals("")){
        	  	List<DataCompare>  zddataList=new ArrayList<DataCompare>(result.length);	
        	  	DataCompare data=null;
        		for(String re:result){
        			data=new DataCompare();
        			String content[]=re.split("、");
        			Map<String, Object> param = new HashMap<String, Object>();
        			Integer maxorder=(Integer)mybatisDao.get("DataCompareMapper.getMaxOrderNum", param);
        			if(maxorder==null){
        				data.setOrderNum(1);
        			}else{
        				data.setOrderNum(maxorder+1);
        			}
            			data.setFieldName(content[1]);
            			data.setWordName(content[2]+"_"+audMonth+"_"+"社会渠道终端异常销售、套利审计报告");
            			data.setWordValue(content[3]);
            			data.setExcelName("社会渠道终端套利排名汇总-"+audMonth);
            			data.setExcelValue(content[5]);
            			data.setCompareResult(content[6]);
            			data.setCreateDatetime(new Date());
            			data.setIsdel(0);
            			mybatisDao.add("DataCompareMapper.insertList", data);
            			zddataList.add(data);
        		}
        	  	}else{
        	  		resultList="excel和word比对结果正常!";
        	  		return resultList;
        	  	}
    			
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        logger.error(">>>>>end--- compare.......");
			return resultList;
	}
	/**
	 * 异常销售终端数量排名
	 * <pre>
	 * Desc  
	 * @param map_list
	 * @return
	 * @author issuser
	 * 2017-7-4 下午4:10:54
	 * </pre>
	 */
	public List<Map<String,Object>> sortListMap(List<Map<String,Object>> map_list,String flag){
		if(map_list != null && map_list.size() > 0){
			//去掉“合计”行
			if(map_list.size()==32)
				map_list.remove(map_list.size()-1);
			//异常销售数量排名
			if(flag != null && flag.equals("ycsale_num")){
				//根据Collections.sort重载方法来实现  
		        Collections.sort(map_list,new Comparator<Map<String,Object>>(){  
					@Override
					public int compare(Map<String, Object> o1, Map<String, Object> o2) {
						String s1 =o1.get("ycsale_num").toString().replace(",", "");
						if(s1.equals("")||s1.equals("-")){
							s1 ="0";
						}
						String s2 =o2.get("ycsale_num").toString().replace(",", "");
						if(s2.equals("")||s2.equals("-")){
							s2 ="0";
						}
						Long a1 =  Long.valueOf(s1);
						Long a2 =  Long.valueOf(s2); 
						return a2.compareTo(a1);
					}  
		              
		        });
			}
			//套利终端数量排名
			if(flag != null && flag.equals("tlzd_num")){
				//根据Collections.sort重载方法来实现  
		        Collections.sort(map_list,new Comparator<Map<String,Object>>(){  
					@Override
					public int compare(Map<String, Object> o1, Map<String, Object> o2) {
						String s1 =o1.get("tlzd_num").toString().replace(",", "");
						if(s1.equals("")||s1.equals("-")){
							s1 ="0";
						}
						String s2 =o2.get("tlzd_num").toString().replace(",", "");
						if(s2.equals("")||s2.equals("-")){
							s2 ="0";
						}
						Long a1 =  Long.valueOf(s1);
						Long a2 =  Long.valueOf(s2); 
						return a2.compareTo(a1);
					}  
		              
		        });
			}
			//套利终端占销量比排名
			if(flag != null && flag.equals("tlzd_saleper")){
				//根据Collections.sort重载方法来实现  
		        Collections.sort(map_list,new Comparator<Map<String,Object>>(){  
					@Override
					public int compare(Map<String, Object> o1, Map<String, Object> o2) {
						String s1 =o1.get("tlzd_saleper").toString().replace("%", "");
						if(s1.equals("")||s1.equals("-")){
							s1 ="0";
						}
						String s2 =o2.get("tlzd_saleper").toString().replace("%", "");
						if(s2.equals("")||s2.equals("-")){
							s2 ="0";
						}
						Double a1 =  Double.valueOf(s1);
						Double a2 =  Double.valueOf(s2); 
						if(a2.compareTo(a1)==0){
							String s1l =o1.get("tlzd_num").toString().replace(",", "");
							if(s1l.equals("")||s1l.equals("-")){
								s1l ="0";
							}
							String s2l =o2.get("tlzd_num").toString().replace(",", "");
							if(s2l.equals("")||s2l.equals("-")){
								s2l ="0";
							}
							Long a1l =  Long.valueOf(s1l);
							Long a2l =  Long.valueOf(s2l); 
							return a2l.compareTo(a1l);
						}else{
							return a2.compareTo(a1);
						}
					}  
		              
		        });
			}
		}
        return map_list;
	}
	
	
	/**
	 * 全公司比对
	 * <pre>
	 * Desc  
	 * @author issuser
	 * 2017-6-24 下午4:49:22
	 * </pre>
	 */
	public void doCompareAll(XSSFWorkbook workbook,String chnlname,List<Map<String,Object>> excel_list
			,List<Map<String,Object>> ycsaleper_list,List<Map<String,Object>> tlzdper_list,List<Map<String,Object>> ycsalenum_list,List<Map<String,Object>> tlzdnum_list,
			StringBuffer errorInfo,int errorNum){
			Map<String,Object> exceldata =null,	ycsaleper=null,tlzddata =null;
			Sheet sheet =workbook.getSheetAt(0);
			int index =52,index2 =53,index3=54;
			//异常销售终端占比排名
			for(int i = 0;i < 5;i++){
				 exceldata= excel_list.get(i);
				 ycsaleper =ycsaleper_list.get(i);
				if(!exceldata.get("prvdName_paiming").equals(ycsaleper.get("company"))){
					//errorInfo.append(errorNum+++"|全公司审计报告异常销售终端占比排名"+(i+1)+"省名称|B0"+index+"|"+ycsaleper.get("company")+"|A0"+(index-1)+"|"+exceldata.get("prvdName_paiming")+"|不相同 ").append("||");
//					resetCellStyle(workbook,sheet.getRow(i+2).getCell(0));
					errorInfo.append(errorNum+++"、"+"全公司审计报告异常销售终端占比排名、"+"、"+"全公司"+"、"+ycsaleper.get("company")+"、"+chnlname+"、"+exceldata.get("prvdName_paiming")+"、"+"全公司审计报告异常销售终端占比排名"+(i+1)+"省名称"+index).append(";");
				}
				if(isNotEmpty(exceldata.get("ycsale_num")) && isNotEmpty(ycsaleper.get("counts"))){
					long t_exceldata = Long.valueOf(exceldata.get("ycsale_num").toString().replace(",", ""));
					long t_worddata = Long.valueOf(ycsaleper.get("counts").toString().replace(",", ""));
					if(t_exceldata != t_worddata){
						//errorInfo.append(errorNum+++"|全公司审计报告异常销售终端占比排名"+(i+1)+"省数量"+"|B0"+index2+"|"+t_worddata+"|A0"+(index2-1)+"|"+t_exceldata+"|不相同  差值"+Math.abs(t_exceldata-t_worddata)).append("||");
						errorInfo.append(errorNum+++"、"+"全公司审计报告异常销售终端占比排名、"+"、"+"全公司"+"、"+t_worddata+"、"+chnlname+"、"+t_exceldata+"、"+"全公司审计报告异常销售终端占比排名"+(i+1)+"省数量"+index2).append(";");
//						resetCellStyle(workbook,sheet.getRow(i+2).getCell(3));
					}
				}
				if(isNotEmpty(exceldata.get("ycsale_percent")) && isNotEmpty(ycsaleper.get("percent"))){
					double t_exceldata =Double.valueOf(exceldata.get("ycsale_percent").toString().replace("%", ""));
					double t_worddata =Double.valueOf(ycsaleper.get("percent").toString().replace("%", ""));
					if(t_exceldata != t_worddata){
						BigDecimal e_num=new BigDecimal(Double.toString(t_exceldata));
		        		  BigDecimal w_num=new BigDecimal(Double.toString(t_worddata));
		        		  BigDecimal result =e_num.subtract(w_num);
						//errorInfo.append(errorNum+++"|全公司审计报告异常销售终端占比排名"+(i+1)+"省占比"+"|B0"+index3+"|"+t_worddata+"|A0"+(index3-1)+"|"+t_exceldata+"|不相同  差值"+Math.abs(result.doubleValue())).append("||");
//						resetCellStyle(workbook,sheet.getRow(i+2).getCell(5));
						errorInfo.append(errorNum+++"、"+"全公司审计报告异常销售终端占比排名、"+"、"+"全公司"+"、"+t_worddata+"、"+chnlname+"、"+t_exceldata+"、"+"全公司审计报告异常销售终端占比排名"+(i+1)+"省占比"+index3).append(";");
					}
				}
				index+=3;
				index2+=3;
				index3+=3;
			}
			 index =22;index2 =23;index3=24;
			//异常销售终端数量排名
			excel_list = sortListMap(excel_list, "ycsale_num");
			for(int i = 0;i < 5;i++){
				 exceldata= excel_list.get(i);
				 ycsaleper =ycsalenum_list.get(i);
				if(!exceldata.get("prvdName_paiming").equals(ycsaleper.get("company"))){
					//errorInfo.append(errorNum+++"|全公司审计报告异常销售终端数量排名"+(i+1)+"省名称"+"|B0"+index+"|"+ycsaleper.get("company")+"|A0"+(index-1)+"|"+exceldata.get("prvdName_paiming")+"|不相同 ").append("||");
//					resetCellStyle(workbook,sheet.getRow(i+2).getCell(0));
					errorInfo.append(errorNum+++"、"+"全公司审计报告异常销售终端数量排名、"+"、"+"全公司"+"、"+ycsaleper.get("company")+"、"+chnlname+"、"+exceldata.get("prvdName_paiming")+"、"+"全公司审计报告异常销售终端数量排名"+(i+1)+"省名称"+index+"不相同 ").append(";");
				}
				if(isNotEmpty(exceldata.get("ycsale_num")) && isNotEmpty(ycsaleper.get("counts"))){
					long t_exceldata = Long.valueOf(exceldata.get("ycsale_num").toString().replace(",", ""));
					long t_worddata = Long.valueOf(ycsaleper.get("counts").toString().replace(",", ""));
					if(t_exceldata != t_worddata){
						//errorInfo.append(errorNum+++"|全公司审计报告异常销售终端数量排名"+(i+1)+"省数量"+"|B0"+index2+"|"+t_worddata+"|A0"+(index2-1)+"|"+t_exceldata+"|不相同  差值"+Math.abs(t_exceldata-t_worddata)).append("||");
//						resetCellStyle(workbook,sheet.getRow(i+2).getCell(3));
						errorInfo.append(errorNum+++"、"+"全公司审计报告异常销售终端数量排名、"+"、"+"全公司"+"、"+t_worddata+"、"+chnlname+"、"+t_exceldata+"、"+"全公司审计报告异常销售终端数量排名"+(i+1)+"省数量"+index2+"不相同 ").append(";");
					}
				}
				if(isNotEmpty(exceldata.get("ycsale_percent")) && isNotEmpty(ycsaleper.get("percent"))){
					double t_exceldata =Double.valueOf(exceldata.get("ycsale_percent").toString().replace("%", ""));
					double t_worddata =Double.valueOf(ycsaleper.get("percent").toString().replace("%", ""));
					if(t_exceldata != t_worddata){
						BigDecimal e_num=new BigDecimal(Double.toString(t_exceldata));
		        		  BigDecimal w_num=new BigDecimal(Double.toString(t_worddata));
		        		  BigDecimal result =e_num.subtract(w_num);
					//	errorInfo.append(errorNum+++"|全公司审计报告异常销售终端数量排名"+(i+1)+"省占比"+"|B0"+index3+"|"+t_worddata+"|A0"+(index3-1)+"|"+t_exceldata+"|不相同  差值"+Math.abs(result.doubleValue())).append("||");
//						resetCellStyle(workbook,sheet.getRow(i+2).getCell(5));
						errorInfo.append(errorNum+++"、"+"全公司审计报告异常销售终端数量排名、"+"、"+"全公司"+"、"+t_worddata+"、"+chnlname+"、"+t_exceldata+"、"+"全公司审计报告异常销售终端数量排名"+(i+1)+"省占比"+index3+"不相同 ").append(";");
					}
				}
				index+=3;
				index2+=3;
				index3+=3;
			}
			 index =67;index2 =68;index3=69;
			//套利终端占比排名
			excel_list = sortListMap(excel_list, "tlzd_saleper");
			for(int i = 0;i < 5;i++){
				exceldata= excel_list.get(i);
				tlzddata = tlzdper_list.get(i);
					if(tlzddata.containsValue(i+1)){
						if(!exceldata.get("prvdName_paiming").equals(tlzddata.get("company"))){
							//errorInfo.append(errorNum+++"|全公司审计报告套利终端占比排名"+(i+1)+"省名称"+"|B0"+index+"|"+tlzddata.get("company")+"|A0"+(index-1)+"|"+exceldata.get("prvdName_paiming")+"|不相同 ").append("||");
//							resetCellStyle(workbook,sheet.getRow(i+2).getCell(0));
							errorInfo.append(errorNum+++"、"+"全公司审计报告套利终端占比排名、"+"、"+"全公司"+"、"+tlzddata.get("company")+"、"+chnlname+"、"+exceldata.get("prvdName_paiming")+"、"+"全公司审计报告套利终端占比排名"+(i+1)+"省名称"+index+"不相同 ").append(";");
						}
						if(isNotEmpty(exceldata.get("tlzd_num")) && isNotEmpty(tlzddata.get("counts"))){
							long t_exceldata = Long.valueOf(exceldata.get("tlzd_num").toString().replace(",", ""));
							long t_worddata = Long.valueOf(tlzddata.get("counts").toString().replace(",", ""));
							if(t_exceldata != t_worddata){
								//errorInfo.append(errorNum+++"|全公司审计报告套利终端占比排名"+(i+1)+"省数量"+"|B0"+index2+"|"+t_worddata+"|A0"+(index2-1)+"|"+t_exceldata+"|不相同  差值"+Math.abs(t_exceldata-t_worddata)).append("||");
//								resetCellStyle(workbook,sheet.getRow(i+2).getCell(7));
								errorInfo.append(errorNum+++"、"+"全公司审计报告套利终端占比排名、"+"、"+"全公司"+"、"+t_worddata+"、"+chnlname+"、"+t_exceldata+"、"+"全公司审计报告套利终端占比排名"+(i+1)+"省数量"+index2+"不相同 ").append(";");
							}
						}
						if(isNotEmpty(exceldata.get("tlzd_saleper")) && isNotEmpty(tlzddata.get("percent"))){
							double t_exceldata =Double.valueOf(exceldata.get("tlzd_saleper").toString().replace("%", ""));
							double t_worddata =Double.valueOf(tlzddata.get("percent").toString().replace("%", ""));
							if(t_exceldata != t_worddata){
								BigDecimal e_num=new BigDecimal(Double.toString(t_exceldata));
				        		  BigDecimal w_num=new BigDecimal(Double.toString(t_worddata));
				        		  BigDecimal result =e_num.subtract(w_num);
								errorInfo.append(errorNum+++"|全公司审计报告套利终端占比排名"+(i+1)+"省占比"+"|B0"+index3+"|"+t_worddata+"|A0"+(index3-1)+"|"+t_exceldata+"|不相同  差值"+Math.abs(result.doubleValue())).append("||");
//								resetCellStyle(workbook,sheet.getRow(i+2).getCell(10));
								errorInfo.append(errorNum+++"、"+"全公司审计报告套利终端占比排名、"+"、"+"全公司"+"、"+t_worddata+"、"+chnlname+"、"+t_exceldata+"、"+"全公司审计报告套利终端占比排名"+(i+1)+"省占比"+index3+"不相同 ").append(";");
							}
						}
					}
					index+=3;
					index2+=3;
					index3+=3;
			}
			 index =37;index2 =38;index3=39;
			//套利终端数量排名
			excel_list = sortListMap(excel_list, "tlzd_num");
			for(int i = 0;i < 5;i++){
				exceldata= excel_list.get(i);
				tlzddata = tlzdnum_list.get(i);
					if(tlzddata.containsValue(i+1)){
						if(!exceldata.get("prvdName_paiming").equals(tlzddata.get("company"))){
							//errorInfo.append(errorNum+++"|全公司审计报告异常销售终端占比排名"+(i+1)+"省数量"+"|B0"+index+"|"+tlzddata.get("company")+"|A0"+(index-1)+"|"+exceldata.get("prvdName_paiming")+"|不相同").append("||");
//							resetCellStyle(workbook,sheet.getRow(i+2).getCell(0));
							errorInfo.append(errorNum+++"、"+"全公司审计报告异常销售终端占比排名、"+"、"+"全公司"+"、"+tlzddata.get("company")+"、"+chnlname+"、"+exceldata.get("prvdName_paiming")+"、"+"全公司审计报告异常销售终端占比排名"+(i+1)+"省数量"+index+"不相同 ").append(";");
						}
						if(isNotEmpty(exceldata.get("tlzd_num")) && isNotEmpty(tlzddata.get("counts"))){
							long t_exceldata = Long.valueOf(exceldata.get("tlzd_num").toString().replace(",", ""));
							long t_worddata = Long.valueOf(tlzddata.get("counts").toString().replace(",", ""));
							if(t_exceldata != t_worddata){
							//	errorInfo.append(errorNum+++"|全公司审计报告套利终端数量排名"+(i+1)+"省数量"+"|B0"+index2+"|"+t_worddata+"|A0"+(index2-1)+"|"+t_exceldata+"|不相同  差值"+Math.abs(t_exceldata-t_worddata)).append("||");
//								resetCellStyle(workbook,sheet.getRow(i+2).getCell(7));
								errorInfo.append(errorNum+++"、"+"全公司审计报告套利终端占比排名、"+"、"+"全公司"+"、"+t_worddata+"、"+chnlname+"、"+t_exceldata+"、"+"全公司审计报告套利终端占比排名"+(i+1)+"省数量"+index2+"不相同 ").append(";");
							}
						}
						if(isNotEmpty(exceldata.get("tlzd_saleper")) && isNotEmpty(tlzddata.get("percent"))){
							double t_exceldata =Double.valueOf(exceldata.get("tlzd_saleper").toString().replace("%", ""));
							double t_worddata =Double.valueOf(tlzddata.get("percent").toString().replace("%", ""));
							if(t_exceldata != t_worddata){
								BigDecimal e_num=new BigDecimal(Double.toString(t_exceldata));
				        		  BigDecimal w_num=new BigDecimal(Double.toString(t_worddata));
				        		  BigDecimal result =e_num.subtract(w_num);
							//	errorInfo.append(errorNum+++"|全公司审计报告套利终端数量排名"+(i+1)+"省占比"+"|B0"+index3+"|"+t_worddata+"|A0"+(index3-1)+"|"+t_exceldata+"|不相同  差值"+Math.abs(result.doubleValue())).append("||");
//								resetCellStyle(workbook,sheet.getRow(i+2).getCell(10));
								errorInfo.append(errorNum+++"、"+"全公司审计报告套利终端占比排名、"+"、"+"全公司"+"、"+t_worddata+"、"+chnlname+"、"+t_exceldata+"、"+"全公司审计报告套利终端占比排名"+(i+1)+"省占比"+index3+"不相同 ").append(";");
							}
						}
					}
					index+=3;
					index2+=3;
					index3+=3;
			}
			
	}
	
	public boolean isNotEmpty(Object obj){
		boolean isflag =false;
		if(obj != null && !obj.equals("")){
			isflag =true;
		}
		return isflag;
	}
	/**
	 * 所有省分比对
	 * <pre>
	 * Desc  
	 * @author issuser
	 * 2017-6-24 下午4:49:39
	 * </pre>
	 */
	public void doCompare(XSSFWorkbook workbook,String chnlname,
			Map exceldata,Map chenmodata,Map yangjidata,Map chaibaodata,Map chuanhuodata,Map worddata,StringBuffer errorInfo,int errorNum){
				String t_prvdname =exceldata.get("prvdName_paiming").toString();
				if(t_prvdname.equals("合计")){
					t_prvdname = "全公司";
				}
	          int row =Integer.parseInt(exceldata.get("row").toString());
	          int sh =Integer.parseInt(exceldata.get("sheet").toString());
	          Sheet sheet = workbook.getSheetAt(sh);
	          String zhengshu="";
	          if(isNotEmpty(exceldata.get("zdsale_num")) &&isNotEmpty(worddata.get("total_salenum"))){
	        	  zhengshu=exceldata.get("zdsale_num").toString().replace(",", "");
	        	  if(zhengshu.contains(".")){
	        		  zhengshu=zhengshu.substring(0,zhengshu.length()-3);
	        	  }
	        	  long t_excel_num =Long.valueOf(zhengshu);//数量保留整数
	        	  long t_word_num =Long.valueOf(worddata.get("total_salenum").toString().trim());
	        	  if(t_excel_num != t_word_num){
	                	//errorInfo.append(errorNum+++"|"+t_prvdname+"终端销售数量|C001|"+t_word_num+"|A081|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//	                	resetCellStyle(workbook,sheet.getRow(row).getCell(1));
	                	errorInfo.append(errorNum+++"、"+"终端销售数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_终端销售数量不相同").append(";");	
	                }
	          }
	          if(isNotEmpty(exceldata.get("ycsale_num"))&& isNotEmpty(worddata.get("ycsale_zdnum"))){
	        	  long t_excel_num =Long.valueOf(exceldata.get("ycsale_num").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("ycsale_zdnum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"异常销售终端数量|C002|"+t_word_num+"|A082|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row).getCell(3));
		            	errorInfo.append(errorNum+++"、"+"异常销售数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_异常销售终端数量不相同").append(";");	
		        }
	          }
	          if(isNotEmpty(exceldata.get("ycsale_percent"))&& isNotEmpty(worddata.get("yszd_numper"))){
	        	  double t_excel_num =Double.valueOf(exceldata.get("ycsale_percent").toString().replace("%", ""));
	        	  double t_word_num =Double.valueOf(worddata.get("yszd_numper").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		  BigDecimal e_num=new BigDecimal(Double.toString(t_excel_num));
	        		  BigDecimal w_num=new BigDecimal(Double.toString(t_word_num));
	        		  BigDecimal result =e_num.subtract(w_num);
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"异常销售终端占比|C003|"+t_word_num+"|A083|"+t_excel_num+"|不相同  差值"+Math.abs(result.doubleValue())).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row).getCell(5));
		            	errorInfo.append(errorNum+++"、"+"异常销售占比、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_异常销售终端占比不相同").append(";");	
		        }
	          }
	          if(isNotEmpty(exceldata.get("ycsale_qudao"))&& isNotEmpty(worddata.get("ysshehui_qudao"))){
	        	  long t_excel_num =Long.valueOf(exceldata.get("ycsale_qudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("ysshehui_qudao").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"异常销售涉及渠道|C004|"+t_word_num+"|A084|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row).getCell(4));
		            	errorInfo.append(errorNum+++"、"+"异常销售涉及渠道、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_异常销售涉及渠道不相同").append(";");
		        }
	          }
	          //排名汇总excel中没有异常销售渠道占比 这个列（无法标红不相同单元格）
	          if(isNotEmpty(exceldata.get("ycsale_qudao"))&&isNotEmpty(exceldata.get("zdsale_qudao"))&& isNotEmpty(worddata.get("ysqudao_per"))){
	        	  long t_excel_ycnum =Long.valueOf(exceldata.get("ycsale_qudao").toString().replace(",", ""));
	        	  long t_excel_totnum =Long.valueOf(exceldata.get("zdsale_qudao").toString().replace(",", ""));
	        	  double t_excel_num = Double.valueOf(dfs3.format((double)t_excel_ycnum/t_excel_totnum*100));
	        	  double t_word_num =Double.valueOf(worddata.get("ysqudao_per").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		//  BigDecimal e_num=new BigDecimal(Double.toString(t_excel_num));
	        		 // BigDecimal w_num=new BigDecimal(Double.toString(t_word_num));
	        		//  BigDecimal result =e_num.subtract(w_num);
		            //	errorInfo.append(errorNum+++"|"+t_prvdname+"异常销售渠道占比|C005|"+t_word_num+"|A084/A085*100%|"+t_excel_num+"|不相同  差值"+Math.abs(result.doubleValue())).append("||");
		            	errorInfo.append(errorNum+++"、"+"异常销售渠道占比、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_异常销售渠道占比不相同").append(";");
		        }
	          }
	          if(isNotEmpty(exceldata.get("tlzd_num"))&& isNotEmpty(worddata.get("tlzd_num"))){
	        	  long t_excel_num =Long.valueOf(exceldata.get("tlzd_num").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("tlzd_num").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	errorInfo.append(errorNum+++"|"+t_prvdname+"套利终端数量|C006|"+t_word_num+"|A086|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row).getCell(7));
		            	errorInfo.append(errorNum+++"、"+"套利终端数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_套利终端数量占比不相同").append(";");
		        }
	          }
	          if(isNotEmpty(exceldata.get("tlzd_saleper"))&& isNotEmpty(worddata.get("tlzd_percent"))){
	        	  double t_excel_num =Double.valueOf(exceldata.get("tlzd_saleper").toString().replace("%", ""));
	        	  double t_word_num =Double.valueOf(worddata.get("tlzd_percent").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		  BigDecimal e_num=new BigDecimal(Double.toString(t_excel_num));
	        		  BigDecimal w_num=new BigDecimal(Double.toString(t_word_num));
	        		  BigDecimal result =e_num.subtract(w_num);
	        		  //	errorInfo.append(errorNum+++"|"+t_prvdname+"套利终端占销量比|C007|"+t_word_num+"|A087|"+t_excel_num+"|不相同  差值"+Math.abs(result.doubleValue())).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row).getCell(10));
	        		  	errorInfo.append(errorNum+++"、"+"套利终端占销量比、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_套利终端占销量比不相同").append(";");
		        }
	          }
	          if(isNotEmpty(exceldata.get("tlzd_qudao"))&& isNotEmpty(worddata.get("tlshehui_qudao"))){
	        	  long t_excel_num =Long.valueOf(exceldata.get("tlzd_qudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("tlshehui_qudao").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		  	//errorInfo.append(errorNum+++"|"+t_prvdname+"终端套利涉及渠道数量|C008|"+t_word_num+"|A088|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row).getCell(9));
	        			errorInfo.append(errorNum+++"、"+"终端套利涉及渠道、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_终端套利涉及渠道数量不相同").append(";");
		        }
	          }
	        //排名汇总excel中没有终端套利涉及渠道占比 这个列（无法标红不相同单元格）
	         //此段内容暂不做比对 ，不对excel字段名
	         /* if(isNotEmpty(exceldata.get("tlzd_qudao"))&&isNotEmpty(exceldata.get("zdsale_qudao"))&& isNotEmpty(worddata.get("tlqudao_per"))){
	        	  long t_excel_zdnum =Long.valueOf(exceldata.get("tlzd_qudao").toString().replace(",", ""));
	        	  long t_excel_totnum =Long.valueOf(exceldata.get("zdsale_qudao").toString().replace(",", ""));
	        	  double t_excel_num = Double.valueOf(dfs3.format((double)t_excel_zdnum/t_excel_totnum*100));
	        	  double t_word_num =Double.valueOf(worddata.get("tlqudao_per").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		  //BigDecimal e_num=new BigDecimal(Double.toString(t_excel_num));
	        		 // BigDecimal w_num=new BigDecimal(Double.toString(t_word_num));
	        		//  BigDecimal result =e_num.subtract(w_num);
	        		  	//errorInfo.append(errorNum+++"|"+t_prvdname+"终端套利涉及渠道占比|C009|"+t_word_num+"|A088/A085*100%|"+t_excel_num+"|不相同  差值"+Math.abs(result.doubleValue())).append("||");
	        		  	errorInfo.append(errorNum+++"、"+"终端套利涉及渠道占比、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_终端套利涉及渠道占比不相同").append(";");
		        }
	          }*/
	          //沉默套利
	          int row_ch =Integer.parseInt(chenmodata.get("row").toString());
	          int sheet_ch =Integer.parseInt(chenmodata.get("sheet").toString());
	           sheet = workbook.getSheetAt(sheet_ch);
	          if(isNotEmpty(chenmodata.get("silent_zdnum"))&& isNotEmpty(worddata.get("chenmo_tlzdnum"))){
	        	  long t_excel_num =Long.valueOf(chenmodata.get("silent_zdnum").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("chenmo_tlzdnum").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		  	//errorInfo.append(errorNum+++"|"+t_prvdname+"沉默串码终端数量|C010|"+t_word_num+"|A089|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_ch).getCell(1));
	        		  	errorInfo.append(errorNum+++"、"+"沉默串码终端数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_沉默串码终端数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(chenmodata.get("taoli_qudao"))&& isNotEmpty(worddata.get("chenmo_shqudaonum"))){
	        	  long t_excel_num =Long.valueOf(chenmodata.get("taoli_qudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("chenmo_shqudaonum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"沉默串码涉及渠道数量|C011|"+t_word_num+"|A090|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_ch).getCell(3));
		            	errorInfo.append(errorNum+++"、"+"沉默串码涉及渠道数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_沉默串码涉及渠道数量不相同").append(";");
		        }
	          }
	          //养机套利
	          int row_yj =Integer.parseInt(yangjidata.get("row").toString());
	          int sheet_yj =Integer.parseInt(yangjidata.get("sheet").toString());
	           sheet = workbook.getSheetAt(sheet_yj);
	          if(isNotEmpty(yangjidata.get("yj_zdnum"))&& isNotEmpty(worddata.get("yj_tlzdnum"))){
	        	  long t_excel_num =Long.valueOf(yangjidata.get("yj_zdnum").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("yj_tlzdnum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"养机套利终端数量|C012|"+t_word_num+"|A091|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_yj).getCell(1));
		            	errorInfo.append(errorNum+++"、"+"养机套利终端数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_养机套利终端数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(yangjidata.get("taoli_qudao"))&& isNotEmpty(worddata.get("yj_shqudaonum"))){
	        	  long t_excel_num =Long.valueOf(yangjidata.get("taoli_qudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("yj_shqudaonum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"养机套利涉及渠道数量|C013|"+t_word_num+"|A092|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_yj).getCell(3));
		            	errorInfo.append(errorNum+++"、"+"养机套利涉及渠道数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_养机套利涉及渠道数量不相同").append(";");
		        }
	          }
	        //拆包套利
	          int row_cb =Integer.parseInt(chaibaodata.get("row").toString());
	          int sheet_cb =Integer.parseInt(chaibaodata.get("sheet").toString());
	           sheet = workbook.getSheetAt(sheet_cb);
	          if(isNotEmpty(chaibaodata.get("chaibao_num"))&& isNotEmpty(worddata.get("cb_zdnum"))){
	        	  long t_excel_num =Long.valueOf(chaibaodata.get("chaibao_num").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("cb_zdnum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"拆包终端数量|C014|"+t_word_num+"|A093|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_cb).getCell(1));
		            	errorInfo.append(errorNum+++"、"+"拆包数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_拆包终端数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(chaibaodata.get("chaibao_qudao"))&& isNotEmpty(worddata.get("cb_shqudaonum"))){
	        	  long t_excel_num =Long.valueOf(chaibaodata.get("chaibao_qudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("cb_shqudaonum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"拆包套利终端涉及渠道数量|C015|"+t_word_num+"|A094|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_cb).getCell(2));
		            	errorInfo.append(errorNum+++"、"+"拆包套利终端涉及渠道数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_拆包套利终端涉及渠道数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(chaibaodata.get("taoli_zdnum"))&& isNotEmpty(worddata.get("cb_tlzdnum"))){
	        	  long t_excel_num =Long.valueOf(chaibaodata.get("taoli_zdnum").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("cb_tlzdnum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"拆包套利终端数量|C016|"+t_word_num+"|A095|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_cb).getCell(3));
		            	errorInfo.append(errorNum+++"、"+"拆包套利终端数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_拆包套利终端数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(chaibaodata.get("chaibao_tlqudao"))&& isNotEmpty(worddata.get("cb_tlqudaonum"))){
	        	  long t_excel_num =Long.valueOf(chaibaodata.get("chaibao_tlqudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("cb_tlqudaonum").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		 // errorInfo.append(errorNum+++"|"+t_prvdname+"拆包套利涉及渠道数量|C017|"+t_word_num+"|A096|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//	        		  resetCellStyle(workbook,sheet.getRow(row_cb).getCell(5));
	        		  errorInfo.append(errorNum+++"、"+"拆包套利涉及渠道数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_拆包套利涉及渠道数量不相同").append(";");
		        }
	          }
	        //跨省串货套利
	          int row_ks =Integer.parseInt(chuanhuodata.get("row").toString());
	          int sheet_ks =Integer.parseInt(chuanhuodata.get("sheet").toString());
	           sheet = workbook.getSheetAt(sheet_ks);
	          if(isNotEmpty(chuanhuodata.get("chuanhuo_num"))&& isNotEmpty(worddata.get("ks_zdnum"))){
	        	  long t_excel_num =Long.valueOf(chuanhuodata.get("chuanhuo_num").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("ks_zdnum").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		  	//errorInfo.append(errorNum+++"|"+t_prvdname+"跨省窜货数量|C018|"+t_word_num+"|A097|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_ks).getCell(1));
	        		  	errorInfo.append(errorNum+++"、"+"跨省窜货数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_跨省窜货数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(chuanhuodata.get("chuanhuo_qudao"))&& isNotEmpty(worddata.get("ks_shqudaonum"))){
	        	  long t_excel_num =Long.valueOf(chuanhuodata.get("chuanhuo_qudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("ks_shqudaonum").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		  //	errorInfo.append(errorNum+++"|"+t_prvdname+"跨省窜货渠道数量|C019|"+t_word_num+"|A098|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_ks).getCell(2));
	        		  	errorInfo.append(errorNum+++"、"+"跨省窜货渠道数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_跨省窜货渠道数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(chuanhuodata.get("taoli_zdnum"))&& isNotEmpty(worddata.get("ks_tlzdnum"))){
	        	  long t_excel_num =Long.valueOf(chuanhuodata.get("taoli_zdnum").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("ks_tlzdnum").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		//  errorInfo.append(errorNum+++"|"+t_prvdname+"跨省窜货套利终端数量|C020|"+t_word_num+"|A099|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//	        		  resetCellStyle(workbook,sheet.getRow(row_ks).getCell(3));
	        		  errorInfo.append(errorNum+++"、"+"跨省窜货套利终端数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_跨省窜货套利终端数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(chuanhuodata.get("chuanhuo_tlqudao"))&& isNotEmpty(worddata.get("ks_tlqudaonum"))){
	        	  long t_excel_num =Long.valueOf(chuanhuodata.get("chuanhuo_tlqudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("ks_tlqudaonum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"跨省窜货套利渠道数量|C021|"+t_word_num+"|A100|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_ks).getCell(5));
		            	errorInfo.append(errorNum+++"、"+"跨省窜货套利渠道数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_跨省窜货套利渠道数量不相同").append(";");
		        }
	          }
	       
	}
	
	/**
	 * 所有省分比对
	 * <pre>
	 * Desc  
	 * @author issuser
	 * 2017-6-24 下午4:49:39
	 * </pre>
	 */
	public void doCompareAllCompany(XSSFWorkbook workbook,String chnlname,
			Map exceldata,Map chenmodata,Map yangjidata,Map chaibaodata,Map chuanhuodata,Map worddata,StringBuffer errorInfo,int errorNum){
		    String t_prvdname= "全公司";
	          String zhengshu="";
	          if(isNotEmpty(exceldata.get("zdsale_num")) &&isNotEmpty(worddata.get("total_salenum"))){
	        	  zhengshu=exceldata.get("zdsale_num").toString().replace(",", "");
	        	  if(zhengshu.contains(".")){
	        		  zhengshu=zhengshu.substring(0,zhengshu.length()-3);
	        	  }
	        	  long t_excel_num =Long.valueOf(zhengshu);//数量保留整数
	        	  long t_word_num =Long.valueOf(worddata.get("total_salenum").toString().trim());
	        	  if(t_excel_num != t_word_num){
	                	errorInfo.append(errorNum+++"、"+"终端销售数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_终端销售数量不相同").append(";");	
	                }
	          }
	          if(isNotEmpty(exceldata.get("ycsale_num"))&& isNotEmpty(worddata.get("ycsale_zdnum"))){
	        	  long t_excel_num =Long.valueOf(exceldata.get("ycsale_num").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("ycsale_zdnum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	errorInfo.append(errorNum+++"、"+"异常销售数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_异常销售终端数量不相同").append(";");	
		        }
	          }
	          if(isNotEmpty(exceldata.get("ycsale_percent"))&& isNotEmpty(worddata.get("yszd_numper"))){
	        	  String t_excel_num =exceldata.get("ycsale_percent").toString().replace("%", "");
	        	  double t_word_num =Double.valueOf(worddata.get("yszd_numper").toString().trim());
	        	  //百分数格式化  
	              Double rate =Double.valueOf(exceldata.get("ycsale_percent").toString().replace("%", ""));  
	              NumberFormat num = NumberFormat.getPercentInstance();   
	              num.setMaximumFractionDigits(2);//最多两位百分小数，如25.23% 
	              String rates = num.format(rate); 
	        	  if(!t_excel_num.equals(dfs2.format(t_word_num/100))){
		            	errorInfo.append(errorNum+++"、"+"异常销售占比、"+t_prvdname+"、"+t_word_num+"%"+"、"+chnlname+"、"+rates+"、"+t_prvdname+"_异常销售终端占比不相同").append(";");	
		        }
	          }
	          if(isNotEmpty(exceldata.get("ycsale_qudao"))&& isNotEmpty(worddata.get("ysshehui_qudao"))){
	        	  long t_excel_num =Long.valueOf(exceldata.get("ycsale_qudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("ysshehui_qudao").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	errorInfo.append(errorNum+++"、"+"异常销售涉及渠道、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_异常销售涉及渠道不相同").append(";");
		        }
	          }
	          //排名汇总excel中没有异常销售渠道占比 这个列（无法标红不相同单元格）
	          if(isNotEmpty(exceldata.get("ycsale_qudao"))&&isNotEmpty(exceldata.get("zdsale_qudao"))&& isNotEmpty(worddata.get("ysqudao_per"))){
	        	  long t_excel_ycnum =Long.valueOf(exceldata.get("ycsale_qudao").toString().replace(",", ""));
	        	  long t_excel_totnum =Long.valueOf(exceldata.get("zdsale_qudao").toString().replace(",", ""));
	        	  double t_excel_num = Double.valueOf(dfs3.format((double)t_excel_ycnum/t_excel_totnum*100));
	        	  double t_word_num =Double.valueOf(worddata.get("ysqudao_per").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	errorInfo.append(errorNum+++"、"+"异常销售渠道占比、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_异常销售渠道占比不相同").append(";");
		        }
	          }
	          if(isNotEmpty(exceldata.get("tlzd_num"))&& isNotEmpty(worddata.get("tlzd_num"))){
	        	  long t_excel_num =Long.valueOf(exceldata.get("tlzd_num").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("tlzd_num").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	errorInfo.append(errorNum+++"、"+"套利终端数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_套利终端数量占比不相同").append(";");
		        }
	          }
	          if(isNotEmpty(exceldata.get("tlzd_saleper"))&& isNotEmpty(worddata.get("tlzd_percent"))){
	        	  String t_excel_num =exceldata.get("tlzd_saleper").toString().replace("%", "");
	        	  double t_word_num =Double.valueOf(worddata.get("tlzd_percent").toString().trim());
	        	  
	        	  Double rate =Double.valueOf(exceldata.get("tlzd_saleper").toString().replace("%", ""));  
	              NumberFormat num = NumberFormat.getPercentInstance();   
	              num.setMaximumFractionDigits(2);//最多两位百分小数，如25.23% 
	              String rates = num.format(rate); 
	        	  if(!t_excel_num.equals(dfs2.format(t_word_num/100))){
	        		  	errorInfo.append(errorNum+++"、"+"套利终端占销量比、"+t_prvdname+"、"+t_word_num+"%"+"、"+chnlname+"、"+rates+"、"+t_prvdname+"_套利终端占销量比不相同").append(";");
		        }
	          }
	          if(isNotEmpty(exceldata.get("tlzd_qudao"))&& isNotEmpty(worddata.get("tlshehui_qudao"))){
	        	  long t_excel_num =Long.valueOf(exceldata.get("tlzd_qudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("tlshehui_qudao").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		  	//errorInfo.append(errorNum+++"|"+t_prvdname+"终端套利涉及渠道数量|C008|"+t_word_num+"|A088|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row).getCell(9));
	        			errorInfo.append(errorNum+++"、"+"终端套利涉及渠道、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_终端套利涉及渠道数量不相同").append(";");
		        }
	          }
	        //排名汇总excel中没有终端套利涉及渠道占比 这个列（无法标红不相同单元格）
	         //此段内容暂不做比对 ，不对excel字段名
	         /* if(isNotEmpty(exceldata.get("tlzd_qudao"))&&isNotEmpty(exceldata.get("zdsale_qudao"))&& isNotEmpty(worddata.get("tlqudao_per"))){
	        	  long t_excel_zdnum =Long.valueOf(exceldata.get("tlzd_qudao").toString().replace(",", ""));
	        	  long t_excel_totnum =Long.valueOf(exceldata.get("zdsale_qudao").toString().replace(",", ""));
	        	  double t_excel_num = Double.valueOf(dfs3.format((double)t_excel_zdnum/t_excel_totnum*100));
	        	  double t_word_num =Double.valueOf(worddata.get("tlqudao_per").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		  //BigDecimal e_num=new BigDecimal(Double.toString(t_excel_num));
	        		 // BigDecimal w_num=new BigDecimal(Double.toString(t_word_num));
	        		//  BigDecimal result =e_num.subtract(w_num);
	        		  	//errorInfo.append(errorNum+++"|"+t_prvdname+"终端套利涉及渠道占比|C009|"+t_word_num+"|A088/A085*100%|"+t_excel_num+"|不相同  差值"+Math.abs(result.doubleValue())).append("||");
	        		  	errorInfo.append(errorNum+++"、"+"终端套利涉及渠道占比、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_终端套利涉及渠道占比不相同").append(";");
		        }
	          }*/
	          //沉默套利
	         /* int row_ch =Integer.parseInt(chenmodata.get("row").toString());
	          int sheet_ch =Integer.parseInt(chenmodata.get("sheet").toString());
	           sheet = workbook.getSheetAt(sheet_ch);*/
	          if(isNotEmpty(chenmodata.get("silent_zdnum"))&& isNotEmpty(worddata.get("chenmo_tlzdnum"))){
	        	  long t_excel_num =Long.valueOf(chenmodata.get("silent_zdnum").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("chenmo_tlzdnum").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		  	//errorInfo.append(errorNum+++"|"+t_prvdname+"沉默串码终端数量|C010|"+t_word_num+"|A089|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_ch).getCell(1));
	        		  	errorInfo.append(errorNum+++"、"+"沉默串码终端数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_沉默串码终端数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(chenmodata.get("taoli_qudao"))&& isNotEmpty(worddata.get("chenmo_shqudaonum"))){
	        	  long t_excel_num =Long.valueOf(chenmodata.get("taoli_qudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("chenmo_shqudaonum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"沉默串码涉及渠道数量|C011|"+t_word_num+"|A090|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_ch).getCell(3));
		            	errorInfo.append(errorNum+++"、"+"沉默串码涉及渠道数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_沉默串码涉及渠道数量不相同").append(";");
		        }
	          }
	          //养机套利
	         /* int row_yj =Integer.parseInt(yangjidata.get("row").toString());
	          int sheet_yj =Integer.parseInt(yangjidata.get("sheet").toString());
	           sheet = workbook.getSheetAt(sheet_yj);*/
	          if(isNotEmpty(yangjidata.get("yj_zdnum"))&& isNotEmpty(worddata.get("yj_tlzdnum"))){
	        	  long t_excel_num =Long.valueOf(yangjidata.get("yj_zdnum").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("yj_tlzdnum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"养机套利终端数量|C012|"+t_word_num+"|A091|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_yj).getCell(1));
		            	errorInfo.append(errorNum+++"、"+"养机套利终端数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_养机套利终端数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(yangjidata.get("taoli_qudao"))&& isNotEmpty(worddata.get("yj_shqudaonum"))){
	        	  long t_excel_num =Long.valueOf(yangjidata.get("taoli_qudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("yj_shqudaonum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"养机套利涉及渠道数量|C013|"+t_word_num+"|A092|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_yj).getCell(3));
		            	errorInfo.append(errorNum+++"、"+"养机套利涉及渠道数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_养机套利涉及渠道数量不相同").append(";");
		        }
	          }
	        //拆包套利
	      /*    int row_cb =Integer.parseInt(chaibaodata.get("row").toString());
	          int sheet_cb =Integer.parseInt(chaibaodata.get("sheet").toString());
	           sheet = workbook.getSheetAt(sheet_cb);*/
	          if(isNotEmpty(chaibaodata.get("chaibao_num"))&& isNotEmpty(worddata.get("cb_zdnum"))){
	        	  long t_excel_num =Long.valueOf(chaibaodata.get("chaibao_num").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("cb_zdnum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"拆包终端数量|C014|"+t_word_num+"|A093|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_cb).getCell(1));
		            	errorInfo.append(errorNum+++"、"+"拆包数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_拆包终端数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(chaibaodata.get("chaibao_qudao"))&& isNotEmpty(worddata.get("cb_shqudaonum"))){
	        	  long t_excel_num =Long.valueOf(chaibaodata.get("chaibao_qudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("cb_shqudaonum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"拆包套利终端涉及渠道数量|C015|"+t_word_num+"|A094|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_cb).getCell(2));
		            	errorInfo.append(errorNum+++"、"+"拆包套利终端涉及渠道数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_拆包套利终端涉及渠道数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(chaibaodata.get("taoli_zdnum"))&& isNotEmpty(worddata.get("cb_tlzdnum"))){
	        	  long t_excel_num =Long.valueOf(chaibaodata.get("taoli_zdnum").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("cb_tlzdnum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"拆包套利终端数量|C016|"+t_word_num+"|A095|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_cb).getCell(3));
		            	errorInfo.append(errorNum+++"、"+"拆包套利终端数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_拆包套利终端数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(chaibaodata.get("chaibao_tlqudao"))&& isNotEmpty(worddata.get("cb_tlqudaonum"))){
	        	  long t_excel_num =Long.valueOf(chaibaodata.get("chaibao_tlqudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("cb_tlqudaonum").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		 // errorInfo.append(errorNum+++"|"+t_prvdname+"拆包套利涉及渠道数量|C017|"+t_word_num+"|A096|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//	        		  resetCellStyle(workbook,sheet.getRow(row_cb).getCell(5));
	        		  errorInfo.append(errorNum+++"、"+"拆包套利涉及渠道数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_拆包套利涉及渠道数量不相同").append(";");
		        }
	          }
	        //跨省串货套利
	   /*       int row_ks =Integer.parseInt(chuanhuodata.get("row").toString());
	          int sheet_ks =Integer.parseInt(chuanhuodata.get("sheet").toString());
	           sheet = workbook.getSheetAt(sheet_ks);*/
	          if(isNotEmpty(chuanhuodata.get("chuanhuo_num"))&& isNotEmpty(worddata.get("ks_zdnum"))){
	        	  long t_excel_num =Long.valueOf(chuanhuodata.get("chuanhuo_num").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("ks_zdnum").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		  	//errorInfo.append(errorNum+++"|"+t_prvdname+"跨省窜货数量|C018|"+t_word_num+"|A097|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_ks).getCell(1));
	        		  	errorInfo.append(errorNum+++"、"+"跨省窜货数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_跨省窜货数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(chuanhuodata.get("chuanhuo_qudao"))&& isNotEmpty(worddata.get("ks_shqudaonum"))){
	        	  long t_excel_num =Long.valueOf(chuanhuodata.get("chuanhuo_qudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("ks_shqudaonum").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		  //	errorInfo.append(errorNum+++"|"+t_prvdname+"跨省窜货渠道数量|C019|"+t_word_num+"|A098|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_ks).getCell(2));
	        		  	errorInfo.append(errorNum+++"、"+"跨省窜货渠道数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_跨省窜货渠道数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(chuanhuodata.get("taoli_zdnum"))&& isNotEmpty(worddata.get("ks_tlzdnum"))){
	        	  long t_excel_num =Long.valueOf(chuanhuodata.get("taoli_zdnum").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("ks_tlzdnum").toString().trim());
	        	  if(t_excel_num != t_word_num){
	        		//  errorInfo.append(errorNum+++"|"+t_prvdname+"跨省窜货套利终端数量|C020|"+t_word_num+"|A099|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//	        		  resetCellStyle(workbook,sheet.getRow(row_ks).getCell(3));
	        		  errorInfo.append(errorNum+++"、"+"跨省窜货套利终端数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_跨省窜货套利终端数量不相同").append(";");
		        }
	          }
	          if(isNotEmpty(chuanhuodata.get("chuanhuo_tlqudao"))&& isNotEmpty(worddata.get("ks_tlqudaonum"))){
	        	  long t_excel_num =Long.valueOf(chuanhuodata.get("chuanhuo_tlqudao").toString().replace(",", ""));
	        	  long t_word_num =Long.valueOf(worddata.get("ks_tlqudaonum").toString().trim());
	        	  if(t_excel_num != t_word_num){
		            	//errorInfo.append(errorNum+++"|"+t_prvdname+"跨省窜货套利渠道数量|C021|"+t_word_num+"|A100|"+t_excel_num+"|不相同  差值"+Math.abs(t_excel_num-t_word_num)).append("||");
//		            	resetCellStyle(workbook,sheet.getRow(row_ks).getCell(5));
		            	errorInfo.append(errorNum+++"、"+"跨省窜货套利渠道数量、"+t_prvdname+"、"+t_word_num+"、"+chnlname+"、"+t_excel_num+"、"+t_prvdname+"_跨省窜货套利渠道数量不相同").append(";");
		        }
	          }
	       
	}

	
	private  void creatExcel(String errorInfo,String filepath) throws FileNotFoundException,IOException {  
		  // HSSFWorkbook workBook = new HSSFWorkbook();// 创建 一个excel文档对象  
		  XSSFWorkbook workBook = new XSSFWorkbook();  
		  XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象  
		  sheet.setColumnWidth(1, 256*40);// 设置第二列的宽度为  
		  sheet.setColumnWidth(2, 256*15);// 设置第三列的宽度为  
		  sheet.setColumnWidth(3, 256*15); 
		  sheet.setColumnWidth(4, 256*15);  
		  sheet.setColumnWidth(5, 256*15);  
		  sheet.setColumnWidth(6, 256*60);  
		  XSSFCellStyle style = workBook.createCellStyle();// 创建样式对象  
		  // 设置字体  
		  XSSFFont font = workBook.createFont();// 创建字体对象  
		  font.setFontHeightInPoints((short) 13);// 设置字体大小  
//		  font.setBoldweight(Font.BOLDWEIGHT_BOLD);// 设置粗体  
		  font.setFontName("黑体");// 设置为黑体字  
		  style.setFont(font);// 将字体加入到样式对象  
		  // 设置对齐方式  
		  style.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);// 水平居中  
		  style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中  
		  // 设置边框  
		  style.setBorderTop(CellStyle.BORDER_THIN);// 顶部边框线  
//		  style.setTopBorderColor(HSSFColor.RED.index);// 设置为红色  
		  style.setBorderBottom(CellStyle.BORDER_THIN);// 底部边框线  
		  style.setBorderLeft(CellStyle.BORDER_THIN);// 左边边框  
		  style.setBorderRight(CellStyle.BORDER_THIN);// 右边边框  
		  // 格式化日期  
//		  style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));  
		  String header[]={"序号","字段名称","审计报告","审计报告值","排名汇总","排名汇总值","比对结果"};
		  if(errorInfo != null&&!errorInfo.equals("")){
			  String rows[] =errorInfo.split("\\|\\|");
			  for(int i = 0 ;i<rows.length;i++){
				  XSSFRow row = sheet.createRow(i);// 创建一个行对象  
				  row.setHeightInPoints(23);// 设置行高23像素  
				  String cols[] =rows[i].split("\\|");
				  for(int j=0;j<cols.length;j++){
					  XSSFCell cell = row.createCell(j);// 创建单元格  
					  cell.setCellValue(cols[j]);  
					  cell.setCellStyle(style);// 应用样式对象  
				  }
			// 
			  }
//			  File f =new File(filepath);
//			  if(f != null &&f.exists()){
//				  InputStream inp = new FileInputStream(filepath); 
//				  XSSFWorkbook wb = new XSSFWorkbook(inp);
//				  for(int ii = 0;ii < wb.getNumberOfSheets();ii++){
//					  wb.removeSheetAt(ii);
//				  }
//				  inp.close();
//			  }
			  // 文件输出流  
			  FileOutputStream os = new FileOutputStream(filepath);
			  workBook.write(os);// 将文档对象写入文件输出流  
			  os.close();// 关闭文件输出流  
		  }
	  }  
	
//	 word解析表格
//     TableIterator it=new TableIterator(range);  
//     while(it.hasNext()){  
//         Table tb=(Table)it.next();  
//         for(int i=0;i<tb.numRows();i++){  
//             TableRow tr=tb.getRow(i);  
//             for(int j=0;j<tr.numCells();j++){  
//                 TableCell td=tr.getCell(j);  
//                 for(int k=0;k<td.numParagraphs();k++){  
//                     Paragraph para1=td.getParagraph(k);  
//                 }  
//             }  
//         }  
//     } 
	
	
	private void resetCellStyle(XSSFWorkbook wb,Cell c){
		CellStyle cs =c.getCellStyle();
		Font textFont = wb.createFont(); //字体颜色
		textFont.setFontHeightInPoints((short)12);
		textFont.setColor(HSSFColor.RED.index);
		textFont.setFontName("宋体"); 
		cs.setFont(textFont);
	}
	
	
	/*public static void main(String[] args) throws Exception{
		CompareDataZdtlService pw =new CompareDataZdtlService();
		pw.match();
		System.out.println("比对结束........");
	}*/

}
