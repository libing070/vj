package com.hpe.cmca.service;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.pojo.DataCompare;

//有价卡比对
@Service
public class CompareDataYjkService extends BaseObject{
	@Autowired
	private MybatisDao mybatisDao;
	
	private static  String FILE_PATH_EXCEL = "";
	
	private static  String FILE_PATH_WORD = "";
	
	DecimalFormat dfs = new DecimalFormat("0");
	
	DecimalFormat dfs2 = new DecimalFormat("0.0000");
	
    DecimalFormat dfs3 = new DecimalFormat("0.00");
	
	//获取excel数据
	public  String match(File webExcelDir,File webWordDir,String audMonth) throws Exception{
		//15:25
		StringBuffer s=new StringBuffer();
		String resultList="";
		FILE_PATH_EXCEL =webExcelDir.getPath()+File.separator;
		FILE_PATH_WORD = webWordDir.getPath()+File.separator;
		 InputStream inp =null;
		 XSSFWorkbook wb =null;
		 Sheet sheet =null;
		File file = new File(FILE_PATH_EXCEL);
	    File[] filelist = file.listFiles();
	    for (File f:filelist){
	    	System.out.println("文件：：："+f.getName());
	        if (f.isFile() && (f.getName().endsWith(".xls") || f.getName().endsWith(".xlsx"))){
	         if(f.getName().contains("赠送有价卡异省充值汇总情况-"+audMonth)){//赠送有价卡异省充值汇总情况-201804.xlsx
	            //创建要读入的文件的输入流 
		         inp = new FileInputStream(FILE_PATH_EXCEL+f.getName()); 
		        //根据上述创建的输入流 创建工作簿对象 
		         wb = new XSSFWorkbook(inp); 
		        //得到第一页 sheet 
		         sheet = wb.getSheetAt(0);
		         Map map =getExcelDataPresent(wb,sheet);
		         logger.error(">>>>>Start resolve.......");
	        	 System.err.println("开始有价卡比对");
	        	 resultList= compareDataToWordPresent(map,wb,sheet,FILE_PATH_EXCEL+f.getName(),1,"赠送有价卡异省充值汇总情况",audMonth);
	        	 s.append(resultList);
	         }
	         if(f.getName().contains("有价卡业财数据对比汇总情况")){//有价卡业财数据对比汇总情况-201802.xlsx 默认t-3
			         inp = new FileInputStream(FILE_PATH_EXCEL+f.getName()); 
			         wb = new XSSFWorkbook(inp); 
			         sheet = wb.getSheetAt(0);
			         Map map =getExcelDataBusiness(wb,sheet);
		        	 resultList= compareDataToWordBusiness(map,wb,sheet,FILE_PATH_EXCEL+f.getName(),2,f.getName().substring(0, f.getName().lastIndexOf(".")),audMonth);
		        	 s.append(resultList);  
	         }
	         /*if(f.getName().contains("有价卡违规排名-"+audMonth)){//有价卡违规排名-201804.xlsx
			         inp = new FileInputStream(FILE_PATH_EXCEL+f.getName()); 
			         wb = new XSSFWorkbook(inp); 
			         Map map =getExcelDataIllegal(wb);
		             resultList= compareDataToWordIllegal(map,wb,sheet,FILE_PATH_EXCEL+f.getName(),3,"有价卡违规排名汇总情况",audMonth);
		             s.append(resultList);
	          }*/
	        	
	        }
	    }
	       return  s.toString();
	   
	}
	
	/**
	 * 有价卡违规排名情况excel内容
	 * @param wb
	 * @param sheet
	 * @return
	 */
	public Map getExcelDataIllegal(XSSFWorkbook wb){
		Sheet sheet =null,sheet2=null,sheet3=null,sheet4=null,sheet5=null,sheet6=null;
		 //第一页排名汇总
		 //各公司  31省违规金额					
		 List<Map<String, Object>> illegal_money_yjk  =new ArrayList<Map<String, Object>>();
		 //各公司  31省违规数量
		 List<Map<String, Object>> illegal_count_yjk  =new ArrayList<Map<String, Object>>();
		 //全公司
		 List<Map<String, Object>> dataList_total =new ArrayList<Map<String, Object>>();
		 Map<String, Object> map =new HashMap<String, Object>();
		 Map<String, Object> exceldata = null;
		 sheet = wb.getSheetAt(0);//第一页 有价卡违规汇总
    	 
    	 double total_money=0.00,total_ex_money=0.00,total_count=0.00,total_ex_count=0.00;
         //各公司  31省违规金额占比概况(先按违规金额占比逆序，再按照违规金额逆序) 排名情况
	        for(int m = 16;m < 47;m++){
	        	if(!sheet.getRow(m).getCell(4).toString().equals("0.0")){//占比<50%,省报告会没有此部分
	        	exceldata = new HashMap<String, Object>();
		        exceldata.put("row", m);
		        exceldata.put("prvdName_yjk", sheet.getRow(m).getCell(2).toString()); 		//省公司名称	
	        	exceldata.put("money", getCellValue_2(sheet.getRow(m).getCell(3)));   //有价卡总金额(元)
	        	exceldata.put("ex_money", getCellValue_2(sheet.getRow(m).getCell(4)));   //有价卡违规金额(元)
	        	exceldata.put("ex_money_percent", doubleExchangePercent(Double.valueOf(getCellValue(sheet.getRow(m).getCell(5)))));   //有价卡违规金额占比
	        	illegal_money_yjk.add(exceldata);
	        	}
	        }
	       
	      //各公司   31省违规数量占比概况  排名情况 
	        for(int m = 53;m < 84;m++){
	        	if(!sheet.getRow(m).getCell(4).toString().equals("0.0")){//占比<50%,省报告会没有此部分
	        	exceldata = new HashMap<String, Object>();
		        exceldata.put("row", m);
		        exceldata.put("prvdName_yjk", sheet.getRow(m).getCell(2).toString()); 		//省公司名称		
	        	exceldata.put("count", getCellValue_2(sheet.getRow(m).getCell(3)));   //有价卡总数量(张)
	        	exceldata.put("ex_count", getCellValue_2(sheet.getRow(m).getCell(4)));   //有价卡违规量(张)
	        	exceldata.put("ex_count_percent", doubleExchangePercent(Double.valueOf(getCellValue(sheet.getRow(m).getCell(5)))));   //有价卡违规数量占比
	        	illegal_count_yjk.add(exceldata);
	        	}
	        
	        }
	       //全公司排名汇总情况
	        for(int m = 16;m < 47;m++){
	        	total_money+=Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(3)));//全公司总金额
	        	total_ex_money+=Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(4)));//全公司异常金额
	        }
	        for(int m = 53;m < 84;m++){
	        	total_count+=Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(3)));//全公司总金额
	        	total_ex_count+=Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(4)));//全公司异常金额
	        }
	         exceldata = new HashMap<String, Object>();
	         exceldata.put("prvdName_yjk", "全公司"); 		//全公司
	    	 exceldata.put("total_money", total_money); 		          //有价卡总金额(元) 
	    	 exceldata.put("total_ex_money", total_ex_money);             //有价卡违规金额(元)
	    	 exceldata.put("total_ex_money_percent", getCellValue_2(sheet.getRow(6).getCell(3)));     //有价卡违规金额占比
	    	 exceldata.put("total_count", total_count); 	              //有价卡总数量(张)
	    	 exceldata.put("total_ex_count", total_ex_count); 		      //有价卡违规量(张)
	    	 exceldata.put("total_ex_count_percent",getCellValue_2(sheet.getRow(9).getCell(3))); 	  //有价卡违规量占比
	    	 dataList_total.add(exceldata);
	     map.put("illegal_money_yjk", illegal_money_yjk);
         map.put("illegal_count_yjk", illegal_count_yjk);
	     map.put("dataList_total", dataList_total);	 
	     
	     sheet2 = wb.getSheetAt(1);//第二页 有价卡生成数据未在BOSS系统和VC系统同步加载	 
 //第二页 有价卡生成数据未在BOSS系统和VC系统同步加载
		 //省公司  
		 List<Map<String, Object>> noload_yjk  =new ArrayList<Map<String, Object>>();
		 //全公司
		 List<Map<String, Object>> noload_yjk_total =new ArrayList<Map<String, Object>>(); 
		 double total_noload_ex_money=0,total_noload_ex_count=0;
		 
		 for(int m = 3;m < 34;m++){
	        	if(!sheet2.getRow(m).getCell(5).toString().equals("0.0") && !sheet2.getRow(m).getCell(5).toString().equals("未发生")){//如何是-不需要比对
	        	exceldata = new HashMap<String, Object>();
		        exceldata.put("row", m);
		        exceldata.put("prvdName_yjk", sheet2.getRow(m).getCell(3).toString()); 		//省公司名称
		        exceldata.put("ex_money", getCellValue_2(sheet2.getRow(m).getCell(5)));   //有价卡违规金额(元)
	        	exceldata.put("ex_count", getCellValue_2(sheet2.getRow(m).getCell(8)));   //有价卡违规数量(张)
	        	noload_yjk.add(exceldata);
	        	}
	       }
	     //全公司未同步加载
	     for(int m = 3;m < 34;m++){
	    	 if(!sheet2.getRow(m).getCell(5).toString().equals("未发生")){
	    		 total_noload_ex_money+=Double.valueOf(getCellValue_2(sheet2.getRow(m).getCell(5)));//全公司总金额 
	    	 }
	    	 if(!sheet2.getRow(m).getCell(8).toString().equals("未发生")){
	    		 total_noload_ex_count+=Double.valueOf(getCellValue_2(sheet2.getRow(m).getCell(8)));//有价卡违规数量(张)
	    	 }
	     }
	     exceldata = new HashMap<String, Object>();
	     exceldata.put("prvdName_yjk", "全公司"); 		//全公司
    	 exceldata.put("total_ex_money", total_noload_ex_money);
    	 exceldata.put("total_ex_count", total_noload_ex_count);            
    	 noload_yjk_total.add(exceldata);  
    	 map.put("noload_yjk", noload_yjk);
	     map.put("noload_yjk_total", noload_yjk_total);	 

	    //第三页 违规激活
	    sheet3 = wb.getSheetAt(2);
		 //省公司  
		 List<Map<String, Object>> not_active_yjk  =new ArrayList<Map<String, Object>>();
		 //全公司
		 List<Map<String, Object>> not_active_yjk_total =new ArrayList<Map<String, Object>>(); 
		 double total_not_active_ex_money=0,total_not_active_ex_count=0;
		 for(int m = 3;m < 34;m++){
	        	if(!sheet3.getRow(m).getCell(5).toString().equals("0.0")){//如何是-不需要比对
	        	exceldata = new HashMap<String, Object>();
		        exceldata.put("row", m);
		        exceldata.put("prvdName_yjk", sheet3.getRow(m).getCell(3).toString()); 		//省公司名称
	        	exceldata.put("ex_money", getCellValue_2(sheet3.getRow(m).getCell(5)));   //有价卡违规激活金额(元)
	        	exceldata.put("ex_count", getCellValue_2(sheet3.getRow(m).getCell(8)));   //有价卡违规激活数量(张)
	        	not_active_yjk.add(exceldata);
	        	}
	     }
	     //全公司未同步加载
	     for(int m = 3;m < 34;m++){
	    		 total_not_active_ex_money+=Double.valueOf(getCellValue_2(sheet3.getRow(m).getCell(5)));//全公司总金额 
	    		 total_not_active_ex_count+=Double.valueOf(getCellValue_2(sheet3.getRow(m).getCell(8)));//有价卡违规数量(张)
	     }
	     exceldata = new HashMap<String, Object>();
	     exceldata.put("prvdName_yjk", "全公司"); 		//全公司名称
    	 exceldata.put("total_ex_money", total_not_active_ex_money);
    	 exceldata.put("total_ex_count", total_not_active_ex_count);            
    	 not_active_yjk_total.add(exceldata);  
    	 map.put("not_active_yjk", not_active_yjk);
	     map.put("not_active_yjk_total", not_active_yjk_total);	 
	     
	     
	    sheet4 = wb.getSheetAt(3);//第四页 违规销售
		 //省公司  
		 List<Map<String, Object>> not_sale_yjk  =new ArrayList<Map<String, Object>>();
		 //全公司
		 List<Map<String, Object>> not_sale_yjk_total =new ArrayList<Map<String, Object>>(); 
		 double total_not_sale_ex_money=0,total_not_sale_ex_count=0;
		 
		 for(int m = 3;m < 34;m++){
	        	if(!sheet4.getRow(m).getCell(5).toString().equals("0.0")){//如何是-不需要比对
	        	exceldata = new HashMap<String, Object>();
		        exceldata.put("row", m);
		        exceldata.put("prvdName_yjk", sheet4.getRow(m).getCell(3).toString()); 		//省公司名称
	        	exceldata.put("ex_money", getCellValue_2(sheet4.getRow(m).getCell(5)));   //有价卡违规销售金额(元)
	        	exceldata.put("ex_count", getCellValue_2(sheet4.getRow(m).getCell(8)));   //有价卡违规销售数量(张)
	        	not_sale_yjk.add(exceldata);
	        	}
	       }
	   
	     //全公司未同步加载
	     for(int m = 3;m < 34;m++){
	    		 total_not_sale_ex_money+=Double.valueOf(getCellValue_2(sheet4.getRow(m).getCell(5)));//全公司总金额 
	    		 total_not_sale_ex_count+=Double.valueOf(getCellValue_2(sheet4.getRow(m).getCell(8)));//有价卡违规数量(张)
	     }
	     exceldata = new HashMap<String, Object>();
	     exceldata.put("prvdName_yjk", "全公司"); 		//全公司名称
    	 exceldata.put("total_ex_money", total_not_sale_ex_money);
    	 exceldata.put("total_ex_count", total_not_sale_ex_count);            
    	 not_sale_yjk_total.add(exceldata);  
    	 map.put("not_sale_yjk", not_sale_yjk);
	     map.put("not_sale_yjk_total", not_sale_yjk_total);	 
	     
	    //第五页 托换货换卡未封锁 
	     sheet5 = wb.getSheetAt(4);
		 //省公司  
		 List<Map<String, Object>> unblocked_yjk  =new ArrayList<Map<String, Object>>();
		 //全公司
		 List<Map<String, Object>> unblocked_yjk_total =new ArrayList<Map<String, Object>>(); 
		 double total_unblocked_ex_money=0,total_unblocked_ex_count=0;
		 for(int m = 3;m < 34;m++){
	        	if(!sheet5.getRow(m).getCell(5).toString().equals("0.0") && !sheet5.getRow(m).getCell(5).toString().equals("未发生")){//如何是-不需要比对
	        	exceldata = new HashMap<String, Object>();
		        exceldata.put("row", m);
		        exceldata.put("prvdName_yjk", sheet5.getRow(m).getCell(3).toString()); 		//省公司名称
	        	exceldata.put("ex_money", getCellValue_2(sheet5.getRow(m).getCell(5)));   //有价卡为封锁金额(元)
	        	exceldata.put("ex_count", getCellValue_2(sheet5.getRow(m).getCell(8)));   //有价卡未封锁数量(张)
	        	unblocked_yjk.add(exceldata);
	        	}
	     }
	     //全公司未同步加载
	    for(int m = 3;m < 34;m++){
	    	 if(!sheet5.getRow(m).getCell(5).toString().equals("未发生")){
	    		 total_unblocked_ex_money+=Double.valueOf(sheet5.getRow(m).getCell(5).toString());//全公司总金额 
	    	 }
	    	 if(!sheet5.getRow(m).getCell(8).toString().equals("未发生")){
	    		 total_unblocked_ex_count+=Double.valueOf(sheet5.getRow(m).getCell(8).toString());//有价卡违规数量(张)
	    	 }
	     }
	     exceldata = new HashMap<String, Object>();
	     exceldata.put("prvdName_yjk", "全公司"); 		//全公司名称
    	 exceldata.put("total_ex_money", total_unblocked_ex_money);
    	 exceldata.put("total_ex_count", total_unblocked_ex_count);            
    	 unblocked_yjk_total.add(exceldata);
    	 map.put("unblocked_yjk", unblocked_yjk);
	     map.put("unblocked_yjk_total", unblocked_yjk_total);	
	     
    	 //第六页 违规重复使用
	     sheet6 = wb.getSheetAt(5);
		 //省公司  
		 List<Map<String, Object>> repeat_yjk  =new ArrayList<Map<String, Object>>();
		 //全公司
		 List<Map<String, Object>> repeat_yjk_total =new ArrayList<Map<String, Object>>(); 
		 double total_repeat_ex_money=0,total_repeat_ex_count=0;
		 for(int m = 3;m < 34;m++){
	        if(!sheet6.getRow(m).getCell(5).toString().equals("0.0")){//如何是-不需要比对
	        	exceldata = new HashMap<String, Object>();
		        exceldata.put("row", m);
		        exceldata.put("prvdName_yjk", sheet6.getRow(m).getCell(3).toString()); 	  //省公司名称
	        	exceldata.put("ex_money", getCellValue_2(sheet6.getRow(m).getCell(5)));   //有价卡违规销售金额(元)
	        	exceldata.put("ex_count", getCellValue_2(sheet6.getRow(m).getCell(8)));   //有价卡违规销售数量(张)
	          	repeat_yjk.add(exceldata);
	         }
		 }
	     //全公司未同步加载
	     for(int m1 = 3;m1 < 34;m1++){
	    		 total_repeat_ex_money+=Double.valueOf(getCellValue_2(sheet6.getRow(m1).getCell(5)));//全公司总金额 
	    		 total_repeat_ex_count+=Double.valueOf(getCellValue_2(sheet6.getRow(m1).getCell(8)));//有价卡违规数量(张)
	     }
	     exceldata = new HashMap<String, Object>();
	     exceldata.put("prvdName_yjk", "全公司"); 		//全公司名称
    	 exceldata.put("total_ex_money", total_repeat_ex_money);
    	 exceldata.put("total_ex_count", total_repeat_ex_count);            
    	 repeat_yjk_total.add(exceldata);  
    	 map.put("repeat_yjk", repeat_yjk);
	     map.put("repeat_yjk_total", repeat_yjk_total);	
		return map;
	}	

	
	/**
	 * 有价卡业财数据对比汇总情况excel内容
	 * @param wb
	 * @param sheet
	 * @return
	 */
	public Map getExcelDataBusiness(XSSFWorkbook wb,Sheet sheet){
		 //各公司"有价卡业财数据对比汇总情况 " 排名情况						
		 List<Map<String, Object>> yc_yjk  =new ArrayList<Map<String, Object>>();
		 //全公司
		 List<Map<String, Object>> yc_total =new ArrayList<Map<String, Object>>();
		 Map<String, Object> map =new HashMap<String, Object>();
		 Map<String, Object> exceldata = null;
         //省公司 有价卡业财数据对比汇总情况
	        for(int m = 5;m < 36;m++){
	        	if(sheet.getRow(m).getCell(8).toString().equals("-")){
	        		if(Double.valueOf(sheet.getRow(m).getCell(4).toString())>0.1){
	        			exceldata = new HashMap<String, Object>();
	    	        	exceldata.put("row", m);
	    	        	exceldata.put("prvdName_yjk", sheet.getRow(m).getCell(1).toString()); 		//省公司名称
	    	        	
	        			exceldata.put("c", doubleExchangePercent(Double.valueOf(getCellValue(sheet.getRow(m).getCell(4))))); //差异c
	        			exceldata.put("g", "-"); //差异g	
	        			if(sheet.getRow(m).getCell(2).toString().equals("-")){
		        			exceldata.put("a",0.00);   //财务数据a
		        		}else{
	        			exceldata.put("a", dfs3.format(Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(2)))));   //财务数据a
		        		}
		        		exceldata.put("b", dfs3.format(Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(3)))));   //业务数据b
		        		if(sheet.getRow(m).getCell(5).toString().equals("-")){
		        			exceldata.put("d",0.00);   //财务数据d
		        		}else{
		        			exceldata.put("d", dfs3.format(Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(5)))));   //财务数据d
		        		}
		        		exceldata.put("e", dfs3.format(Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(6)))));   //业务数据e
		        		exceldata.put("f1", dfs3.format(Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(7)))));   //业务数据f
		        		yc_yjk.add(exceldata);
	        		}
	        	}else{
	        		if(Double.valueOf(sheet.getRow(m).getCell(4).toString())>0.1 || Double.valueOf(sheet.getRow(m).getCell(8).toString())>0.1){
	        			exceldata = new HashMap<String, Object>();
	    	        	exceldata.put("row", m);
	    	        	exceldata.put("prvdName_yjk", sheet.getRow(m).getCell(1).toString()); 		//省公司名称
	    	        	
	        			exceldata.put("c", doubleExchangePercent(Double.valueOf(getCellValue(sheet.getRow(m).getCell(4))))); //差异c
	        			exceldata.put("g", doubleExchangePercent(Double.valueOf(getCellValue(sheet.getRow(m).getCell(8))))); //差异g	
	        			if(sheet.getRow(m).getCell(2).toString().equals("-")){
		        			exceldata.put("a",0.00);   //财务数据a
		        		}else{
	        			exceldata.put("a", dfs3.format(Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(2)))));   //财务数据a
		        		}
	        			exceldata.put("b", dfs3.format(Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(3)))));   //业务数据b
		        		if(sheet.getRow(m).getCell(5).toString().equals("-")){
		        			exceldata.put("d",0.00);   //财务数据d
		        		}else{
		        			exceldata.put("d", dfs3.format(Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(5)))));   //财务数据d
		        		}
		        		//exceldata.put("d", dfs3.format(Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(5)))));   //财务数据d
		        		exceldata.put("e", dfs3.format(Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(6)))));   //业务数据e
		        		exceldata.put("f1", dfs3.format(Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(7)))));   //业务数据f
		        		yc_yjk.add(exceldata);
	        		}
	        	}
	        }
	      //全公司比对
	       double a=0.00,b=0.00,d=0.00,e=0.00,f1=0.00,c=0.00,g=0.00; 
	       for(int m = 5;m < 36;m++){
	        	exceldata = new HashMap<String, Object>();
	        	if(!sheet.getRow(m).getCell(2).toString().equals("-")){
	        		a+=Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(2)));	
        		}
	            b+=Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(3)));
	            if(!sheet.getRow(m).getCell(5).toString().equals("-")){
	            d+=Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(5)));
        		}
	            e+=Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(6)));
	            f1+=Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(7)));
	        }  
	         exceldata = new HashMap<String, Object>();
	         exceldata.put("prvdName_yjk", "全公司"); 		
	    	 exceldata.put("a", a); 		 
	    	 exceldata.put("b", b); 		   
	    	 exceldata.put("d", d); 		    
	    	 exceldata.put("e", e); 		 
	    	 exceldata.put("f1", f1); 		  
	      // |a-b|/MAX(a,b) c
	    	c= Math.abs(a-b)/Math.max(a,b);
	    	g=Math.abs(Math.abs(d)-e-f1)/Math.max(Math.abs(d),e+f1);
	      // ||d|-e-f|/MAX(|d|,e+f) 	 
	    	 exceldata.put("c", doubleExchangePercent(c)); 		   
	    	 exceldata.put("g", doubleExchangePercent(g)); 		 
	    	 yc_total.add(exceldata);
	        
	      map.put("yc_yjk", yc_yjk);
	      map.put("yc_total", yc_total);
		  return map;
	}
	
	/**
	 * 赠送有价卡异省充值情况excel内容
	 * @param wb
	 * @param sheet
	 * @return
	 */
	public Map getExcelDataPresent(XSSFWorkbook wb,Sheet sheet){
		 //各公司"赠送有价卡异省充值情况"排名情况						
		 List<Map<String, Object>> present_yjk  =new ArrayList<Map<String, Object>>();
		 //全公司
		// List<Map<String, Object>> dataList_total =new ArrayList<Map<String, Object>>();
		 Map<String, Object> map =new HashMap<String, Object>();
		 Map<String, Object> exceldata = null;
	        for(int m = 3;m < 34;m++){
	        	if(Double.valueOf(sheet.getRow(m).getCell(9).toString())>0.5){//占比<50%,省报告会没有此部分
	        	exceldata = new HashMap<String, Object>();	
	        	exceldata.put("row", m);	
	        	exceldata.put("prvdName_yjk", sheet.getRow(m).getCell(1).toString()); 		//省公司名称	
	        	exceldata.put("other_prvd_money_percent", doubleExchangePercent(Double.valueOf(getCellValue(sheet.getRow(m).getCell(9))))); //给异省号码充值金额占比%	
	        	exceldata.put("be_money", dfs.format(Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(2)))));   //被充值赠卡金额（元）
	        	exceldata.put("be_count", getCellValue_2(sheet.getRow(m).getCell(3)));   //被充值赠卡赠卡数量（张）
	        	exceldata.put("prvd_money", dfs.format(Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(4)))));   //给本省号码充值金额（元）
	        	exceldata.put("prvd_money_percent", doubleExchangePercent(Double.valueOf(getCellValue(sheet.getRow(m).getCell(5))))); //给本省号码充值金额占比%
	        	exceldata.put("prvd_count", getCellValue_2(sheet.getRow(m).getCell(6)));   //给本省号码充值卡数量（张）
	        	exceldata.put("other_prvd_money", dfs.format(Double.valueOf(getCellValue_2(sheet.getRow(m).getCell(8)))));   //给异省号码充值金额（元）
	        	exceldata.put("other_prvd_count", getCellValue_2(sheet.getRow(m).getCell(10)));   //给异省号码充值卡数量（张）
	        	present_yjk.add(exceldata);
	        	}
	        	
	        }
	      map.put("present_yjk", present_yjk);
		//map.put("dataList_total", dataList_total);
		  return map;
	}	

	public String getCellValue(Cell cell){
		String returnvalue = null;
		switch (cell.getCellType()) {  
                case Cell.CELL_TYPE_STRING:  
                	returnvalue =cell.getRichStringCellValue()  
                            .getString();  
                    break;  
                case Cell.CELL_TYPE_NUMERIC:  
                    if (DateUtil.isCellDateFormatted(cell)) {  
                    	returnvalue=cell.getDateCellValue().toLocaleString(); 
                    } else {  
                    	if(isNumber(cell.getNumericCellValue())){
                    		returnvalue = dfs.format(cell.getNumericCellValue());
//                    		returnvalue =Long.toString(Math.round(cell.getNumericCellValue()));
                    	}else{
                    		returnvalue = dfs2.format(cell.getNumericCellValue());
//                    		BigDecimal a1 = new BigDecimal(cell.getNumericCellValue());
//                    		returnvalue =a1.toString(); 
                    	}
                    }  
                    break;  
                case Cell.CELL_TYPE_BOOLEAN:  
                	returnvalue=Boolean.toString(cell.getBooleanCellValue());  
                    break;  
                case Cell.CELL_TYPE_FORMULA:  
                    System.out.println(cell.getCellFormula());  
                    break;  
                default: 
                	returnvalue= "";
                    break;  
                }  
		
		return returnvalue;
	}
	
	public String getCellValue_2(Cell cell){
		String returnvalue = null;
		switch (cell.getCellType()) {  
                case Cell.CELL_TYPE_STRING:  
                	returnvalue =cell.getRichStringCellValue()  
                            .getString();  
                    break;  
                case Cell.CELL_TYPE_NUMERIC:  
                    if (DateUtil.isCellDateFormatted(cell)) {  
                    	returnvalue=cell.getDateCellValue().toLocaleString(); 
                    } else {  
                    	if(isNumber(cell.getNumericCellValue())){
                    		returnvalue = dfs.format(cell.getNumericCellValue());
                    	}else{
                    		returnvalue = dfs3.format(cell.getNumericCellValue());
                    	}
                    }  
                    break;  
                case Cell.CELL_TYPE_BOOLEAN:  
                	returnvalue=Boolean.toString(cell.getBooleanCellValue());  
                    break;  
                case Cell.CELL_TYPE_FORMULA:  
                    System.out.println(cell.getCellFormula());  
                    break;  
                default: 
                	returnvalue= "";
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
	
	public List getWorddata(String filename){
		List<String> dataList =new ArrayList<String>();
		try {
	    	SAXReader reader = new SAXReader();  
	    	Document  document = reader.read(new File(filename));  
	    	Element rootElm = document.getRootElement();  
	    	int num =1;
	    	  // 枚举名称为foo的节点
	        for ( Iterator i = rootElm.elementIterator("body"); i.hasNext();) {
	           Element foo = (Element) i.next();
	           for ( Iterator i1 = foo.elementIterator("p"); i1.hasNext();) {
	               Element foo1 = (Element) i1.next();
	               for ( Iterator i2 = foo1.elementIterator("r"); i2.hasNext();) {
	                   Element foo2 = (Element) i2.next();
//	                   System.out.println(num+"......"+foo2.elementText("t")); 
	                   dataList.add(foo2.elementText("t"));
//	                   num++;
	                }
	            }
	           for ( Iterator i1 = foo.elementIterator("tbl"); i1.hasNext();) {
	               Element foo1 = (Element) i1.next();
	               for ( Iterator i2 = foo1.elementIterator("tr"); i2.hasNext();) {
	                   Element foo2 = (Element) i2.next();
	                   for ( Iterator i3 = foo2.elementIterator("tc"); i3.hasNext();) {
	                       Element foo3 = (Element) i3.next();
	                       for ( Iterator i4 = foo3.elementIterator("p"); i4.hasNext();) {
	                           Element foo4 = (Element) i4.next();
	                           for ( Iterator i5 = foo4.elementIterator("r"); i5.hasNext();) {
	                               Element foo5 = (Element) i5.next();
//	                               System.out.println(num+"......"+foo5.elementText("t")); 
	                               dataList.add(foo5.elementText("t"));
//	                               num++;
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
	 * 比对赠送有价卡异省充值汇总情况
	 * <pre>
	 * Desc  flag 
	 * @author cyz
	 * 2018-6-11 下午10:41:15
	 * </pre>
	 * @throws Exception 
	 */
	public String compareDataToWordPresent(Map<String,Object> exceldata,XSSFWorkbook workbook, Sheet sheet
			,String filepath,int flag,String chnlname,String audMonth) throws Exception{
			String resultList="";
			int errorNum =1;
			StringBuffer errorInfo= new StringBuffer();
	        try {
	        	File file = new File(FILE_PATH_WORD);
	    	    File[] filelist = file.listFiles();
	    	    //全公司
	    	   // List<Map<String,Object>> allList  = (List<Map<String,Object>>)exceldata.get("dataList_total");
	    	    //省
    	    	List<Map<String,Object>> prvdList =(List<Map<String,Object>>)exceldata.get("present_yjk");
    	    	//比对31个省份
    	    	for(Map<String,Object> dzqdata:prvdList){
    	    				for (File f:filelist){
    	    	    	    	if (f.isFile() && f.getName().endsWith(".docx")&&f.getName().contains(dzqdata.get("prvdName_yjk").toString())){
    	    	    	    		String table=readTableByExtractors(FILE_PATH_WORD+f.getName());//获取对应word内容
    	    	    	    		if(StringUtils.isNotBlank(table)){
    	    	    	    			if(flag ==1){
   	    	    	    				String be_money,be_count,prvd_money,prvd_count,prvd_money_percent,other_prvd_money,other_prvd_count,other_prvd_money_percent;//被充值赠卡金额（元）
    	    	    	    				if(table.contains("被充值赠卡金额") && table.contains("差异c")){
    	    	    	    					String sbstring=table.substring(table.indexOf("给异省号码充值卡数量")-3, table.indexOf("给异省号码充值卡数量")-1);
    	    	    	    					int tables=Integer.parseInt(sbstring);
    	    	    	    			    	 be_money=table.substring(table.indexOf("、"+String.valueOf(tables+1))+4, table.indexOf("、"+String.valueOf(tables+2)));//被充值赠卡金额（元）
    	    	    	    			    	 be_money=be_money.replace(",", "");
    	    	    	    			    	 be_count=table.substring(table.indexOf("、"+String.valueOf(tables+2))+4, table.indexOf("、"+String.valueOf(tables+3)));//被充值赠卡数量
    	    	    	    			    	 be_count=be_count.replace(",", "");
    	    	    	    			    	 prvd_money=table.substring(table.indexOf("、"+String.valueOf(tables+3))+4, table.indexOf("、"+String.valueOf(tables+4)));//给本省号码充值金额（元）
    	    	    	    			    	 prvd_money=prvd_money.replace(",", "");
    	    	    	    			    	 
    	    	    	    			    	 prvd_money_percent=table.substring(table.indexOf("、"+String.valueOf(tables+4))+4, table.indexOf("、"+String.valueOf(tables+5)));//给本省号码充值金额占比
    	    	    	    			    	 
    	    	    	    			    	 prvd_count=table.substring(table.indexOf("、"+String.valueOf(tables+5))+4, table.indexOf("、"+String.valueOf(tables+6)));//给本省号码充值卡数量
    	    	    	    			    	 prvd_count=prvd_count.replace(",", "");
    	    	    	    			    	 
    	    	    	    			    	 other_prvd_money=table.substring(table.indexOf("、"+String.valueOf(tables+6))+4, table.indexOf("、"+String.valueOf(tables+7)));//给异省号码充值金额（元）
    	    	    	    			    	 other_prvd_money=other_prvd_money.replace(",", "");
    	    	    	    			    	 
    	    	    	    			    	 other_prvd_money_percent=table.substring(table.indexOf("、"+String.valueOf(tables+7))+4, table.indexOf("、"+String.valueOf(tables+8)));//给本省号码充值金额占比
    	    	    	    			    	 other_prvd_count=table.substring(table.indexOf("、"+String.valueOf(tables+8))+4, table.lastIndexOf("、"));
    	    	    	    			    	 other_prvd_count=other_prvd_count.replace(",", "");//给异省号码充值卡数量
    	    	    	    			    	 doComparePrvoPresent(be_money,be_count,prvd_money,prvd_count,prvd_money_percent,other_prvd_money,other_prvd_count,other_prvd_money_percent, 
    	     	    	    		             			sheet,workbook,chnlname,dzqdata, errorInfo,errorNum,f.getName());
    	    	    	    			    }else if(table.contains("被充值赠卡金额") && !table.contains("差异c")){
    	    	    	    			    	String sbstring=table.substring(table.indexOf("给异省号码充值卡数量")-2, table.indexOf("给异省号码充值卡数量")-1);
    	    	    	    					int tables=Integer.parseInt(sbstring);
    	    	    	    					be_money=table.substring(table.indexOf("、"+String.valueOf(tables+1))+3, table.indexOf("、"+String.valueOf(tables+2)));//被充值赠卡金额（元）
   	    	    	    			    	 be_money=be_money.replace(",", "");
   	    	    	    			    	 be_count=table.substring(table.indexOf("、"+String.valueOf(tables+2))+4, table.indexOf("、"+String.valueOf(tables+3)));//被充值赠卡数量
   	    	    	    			    	 be_count=be_count.replace(",", "");
   	    	    	    			    	 prvd_money=table.substring(table.indexOf("、"+String.valueOf(tables+3))+4, table.indexOf("、"+String.valueOf(tables+4)));//给本省号码充值金额（元）
   	    	    	    			    	 prvd_money=prvd_money.replace(",", "");
   	    	    	    			    	 
   	    	    	    			    	 prvd_money_percent=table.substring(table.indexOf("、"+String.valueOf(tables+4))+4, table.indexOf("、"+String.valueOf(tables+5)));//给本省号码充值金额占比
   	    	    	    			    	 
   	    	    	    			    	 prvd_count=table.substring(table.indexOf("、"+String.valueOf(tables+5))+4, table.indexOf("、"+String.valueOf(tables+6)));//给本省号码充值卡数量
   	    	    	    			    	 prvd_count=prvd_count.replace(",", "");
   	    	    	    			    	 
   	    	    	    			    	 other_prvd_money=table.substring(table.indexOf("、"+String.valueOf(tables+6))+4, table.indexOf("、"+String.valueOf(tables+7)));//给异省号码充值金额（元）
   	    	    	    			    	 other_prvd_money=other_prvd_money.replace(",", "");
   	    	    	    			    	 
   	    	    	    			    	 other_prvd_money_percent=table.substring(table.indexOf("、"+String.valueOf(tables+7))+4, table.indexOf("、"+String.valueOf(tables+8)));//给本省号码充值金额占比
   	    	    	    			    	 other_prvd_count=table.substring(table.indexOf("、"+String.valueOf(tables+8))+4, table.lastIndexOf("、"));
   	    	    	    			    	 other_prvd_count=other_prvd_count.replace(",", "");//给异省号码充值卡数量
   	    	    	    			    	 doComparePrvoPresent(be_money,be_count,prvd_money,prvd_count,prvd_money_percent,other_prvd_money,other_prvd_count,other_prvd_money_percent, 
   	     	    	    		             			sheet,workbook,chnlname,dzqdata, errorInfo,errorNum,f.getName());
    	    	    	    			    }
    	    	    	    			 
    	    	    	    			}
    	    	    	    		}
    	    	    	    		
    	    	    	    	}
    	    	    	    }
    	    		
    	    	}
    	    	//比对全公司
    	    /*	for(Map<String,Object> dzqdataAll:allList){
    	    		for (File f:filelist){
    	    	    	if (f.isFile() && f.getName().endsWith(".doc")&&f.getName().contains("全公司")){
    	    	    		System.out.println("比对审计报告..............全公司");
    	    	    		List<String> dataList =getWorddata(FILE_PATH_WORD+f.getName());
    	    	    		if(dataList != null && dataList.size() > 0){
    	    	    			if(flag ==1){
    	    	    				//目前只针对2.0，3.0生产缺失文档后期才上
    	    	    				if(dzqdataAll.get("total_payment").toString().contains("未上传当期数据")){
    	    	    					if(!dataList.contains("未上传数据")){
    	    	    					 errorInfo.append(errorNum+++"、"+"审计期间，未上传数据、"+f.getName()+"、"+"-"+"、"+chnlname+"、"+
    	    	    							 dzqdataAll.get("total_payment").toString()+"、"+dzqdataAll.get("prvdName_dzq")+"_未发现审计期间，未上传数据关键字信息").append(";");		
    	    	    					}
    	    	    				}else{
    	    	    				String dzqCount = dataList.get(9);
    	    	    				String count=dzqCount.substring(dzqCount.indexOf("共发放电子券")+6, dzqCount.indexOf("张"));//共计电子券数量
    	    	    				count =count.replace(",", "");
    	    	    				
    	    	    	    		String moneyAndExCount = dataList.get(10);//共计金额和异常电子券数量
    	    	    	    		String money=moneyAndExCount.substring(0,moneyAndExCount.indexOf("元"));//共计金额
    	    	    	    		money =money.replace(",", "");
    	    	    	    		String excount=moneyAndExCount.substring(moneyAndExCount.indexOf("异常发放电子券")+7,moneyAndExCount.indexOf("张"));//异常数量
    	    	    	    		excount =excount.replace(",", "");
    	    	    	    		
    	    	    	    		String exMoneyAndPercent = dataList.get(11);//异常金额和百分比
    	    	    	    		String exmoney=exMoneyAndPercent.substring(0,exMoneyAndPercent.indexOf("元"));//异常金额
    	    	    	    		exmoney =exmoney.replace(",", "");
    	    	    	    		String exPercent=exMoneyAndPercent.substring(exMoneyAndPercent.indexOf("占电子券发放总金额的")+10,exMoneyAndPercent.indexOf("%")+1);//异常数量
    	    	    	    		doCompareAll(count,money,excount,exmoney,exPercent, 
    	    		             			sheet,workbook,chnlname,dzqdataAll, errorInfo,errorNum,f.getName());
    	    	    				}
    	    	    			}
    	    		        }
    	    	    	}
    	    	    }
    	    	}*/
    	    	/*FileOutputStream outPath = new FileOutputStream(filepath);
    			workbook.write(outPath);
    			outPath.close();*/
    	    	//createTxt(errorInfo.toString(), FILE_PATH_EXCEL+chnlname+".txt");
    	    	//输出比对结果入库-------------------------------------------------	
        	  	String result[]=errorInfo.toString().split(";");
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
            			data.setWordName(content[2]);
            			data.setWordValue(content[3]);
            			data.setExcelName("赠送有价卡异省充值汇总情况_"+audMonth);
            			data.setExcelValue(content[5]);
            			data.setCompareResult(content[6]);
            			data.setCreateDatetime(new Date());
            			data.setIsdel(0);
            			mybatisDao.add("DataCompareMapper.insertList", data);
            			zddataList.add(data);
        		}
        	  	}else{
        	  		resultList="有价卡赠送excel和word比对结果正常!";
        	  		return resultList;
        	  	}
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return resultList;
	}
	
	/**
	 * 所有省公司有价卡赠送比对
	 */
	public void doComparePrvoPresent(
			String be_money,String be_count,String prvd_money,String prvd_count,String prvd_money_percent,
			String other_prvd_money,String other_prvd_count,String other_prvd_money_percent,
	        Sheet sheet,XSSFWorkbook workbook,String chnlname,
			Map dzqdata,StringBuffer errorInfo,int errorNum,String wordName){
	  if(StringUtils.isNotBlank(be_money) &&!dzqdata.get("be_money").equals("")){
       	  if(!dzqdata.get("be_money").toString().equals(be_money.trim())){
       		  errorInfo.append(errorNum+++"、"+"被充值赠卡金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+be_money.trim()+"、"+chnlname+"、"+dzqdata.get("be_money")+"、"+dzqdata.get("prvdName_yjk")+"_被充值赠卡金额不相同").append(";");	
               }
        }
	  if(StringUtils.isNotBlank(be_count) &&!dzqdata.get("be_count").equals("")){
 	      if(!dzqdata.get("be_count").toString().equals(be_count.trim())){
 		   errorInfo.append(errorNum+++"、"+"被充值赠卡赠卡数量(张)、"+wordName.substring(0,wordName.length()-5)+"、"+be_count.trim()+"、"+chnlname+"、"+dzqdata.get("be_count")+"、"+dzqdata.get("prvdName_yjk")+"_被充值赠卡数量不相同").append(";");	
          }
      }
	  if(StringUtils.isNotBlank(prvd_money) &&!dzqdata.get("prvd_money").equals("")){
 	      if(!dzqdata.get("prvd_money").toString().equals(prvd_money.trim())){
 		   errorInfo.append(errorNum+++"、"+"给本省号码充值金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+prvd_money.trim()+"、"+chnlname+"、"+dzqdata.get("prvd_money")+"、"+dzqdata.get("prvdName_yjk")+"_给本省号码充值金额不相同").append(";");	
          }
      }
	  if(StringUtils.isNotBlank(prvd_money_percent) &&!dzqdata.get("prvd_money_percent").equals("")){
 	      if(!dzqdata.get("prvd_money_percent").toString().equals(prvd_money_percent.trim())){
 		   errorInfo.append(errorNum+++"、"+"给本省号码充值金额占比%、"+wordName.substring(0,wordName.length()-5)+"、"+prvd_money_percent.trim()+"、"+chnlname+"、"+dzqdata.get("prvd_money_percent")+"、"+dzqdata.get("prvdName_yjk")+"_给本省号码充值金额占比%不相同").append(";");	
          }
      }
	  if(StringUtils.isNotBlank(prvd_count) &&!dzqdata.get("prvd_count").equals("")){
 	      if(!dzqdata.get("prvd_count").toString().equals(prvd_count.trim())){
 		   errorInfo.append(errorNum+++"、"+"给本省号码充值卡数量(张)、"+wordName.substring(0,wordName.length()-5)+"、"+prvd_count.trim()+"、"+chnlname+"、"+dzqdata.get("prvd_count")+"、"+dzqdata.get("prvdName_yjk")+"_给本省号码充值卡数量不相同").append(";");	
          }
      }
	  if(StringUtils.isNotBlank(other_prvd_money) &&!dzqdata.get("other_prvd_money").equals("")){
 	      if(!dzqdata.get("other_prvd_money").toString().equals(other_prvd_money.trim())){
 		   errorInfo.append(errorNum+++"、"+"给异省号码充值金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+other_prvd_money.trim()+"、"+chnlname+"、"+dzqdata.get("other_prvd_money")+"、"+dzqdata.get("prvdName_yjk")+"_给异省号码充值金额不相同").append(";");	
          }
      }
	  if(StringUtils.isNotBlank(other_prvd_count) &&!dzqdata.get("other_prvd_count").equals("")){
 	      if(!dzqdata.get("other_prvd_count").toString().equals(other_prvd_count.trim())){
 		   errorInfo.append(errorNum+++"、"+"给异省号码充值卡数量(张)、"+wordName.substring(0,wordName.length()-5)+"、"+other_prvd_count.trim()+"、"+chnlname+"、"+dzqdata.get("other_prvd_count")+"、"+dzqdata.get("prvdName_yjk")+"_给异省号码充值卡数量不相同").append(";");	
          }
      }
	  if(StringUtils.isNotBlank(other_prvd_money_percent) &&!dzqdata.get("other_prvd_money_percent").equals("")){
 	      if(!dzqdata.get("other_prvd_money_percent").toString().equals(other_prvd_money_percent.trim())){
 		   errorInfo.append(errorNum+++"、"+"给异省号码充值金额占比%、"+wordName.substring(0,wordName.length()-5)+"、"+other_prvd_money_percent.trim()+"、"+chnlname+"、"+dzqdata.get("other_prvd_money_percent")+"、"+dzqdata.get("prvdName_yjk")+"_给异省号码充值金额占比%不相同").append(";");	
          }
      }
	}
	
	/**
	 * 有价卡排名汇总情况
	 * <pre>
	 * Desc  flag 
	 * @author cyz
	 * 2018-6-114下午10:41:15
	 * </pre>
	 * @throws Exception 
	 */
	public String compareDataToWordIllegal(Map<String,Object> exceldata,XSSFWorkbook workbook, Sheet sheet
			,String filepath,int flag,String chnlname,String audMonth) throws Exception{
			String resultList="";
			int errorNum =1;
			StringBuffer errorInfo= new StringBuffer();
	        try {
	        	File file = new File(FILE_PATH_WORD);
	    	    File[] filelist = file.listFiles();
	    	    //第一页
	    	    //全公司
	    	    List<Map<String,Object>> allList  = (List<Map<String,Object>>)exceldata.get("dataList_total");
	    	    //省电子券
    	    	List<Map<String,Object>> prvdList1 =(List<Map<String,Object>>)exceldata.get("illegal_money_yjk");
    	    	List<Map<String,Object>> prvdListCount1 =(List<Map<String,Object>>)exceldata.get("illegal_count_yjk");
    	    	//比对31个省份
    	    	
    	    	for(Map<String,Object> p1:prvdList1){
    	    		for(Map<String,Object> p11:prvdListCount1){
    	    		   if(p1.get("prvdName_yjk").toString().contains(p11.get("prvdName_yjk").toString())){
    	    				for (File f:filelist){
    	    	    	    	if (f.isFile() && f.getName().endsWith(".docx")&&f.getName().contains(p1.get("prvdName_yjk").toString())){
    	    	    	    		String tablep=readContentByExtractors(FILE_PATH_WORD+f.getName());//获取对应word内容
    	    	    	    		if(StringUtils.isNotBlank(tablep)){
    	    	    	    			if(flag ==3){
    	    	    	    				 String money,ex_money,ex_money_percent,count,ex_count,ex_count_percent;
    	    	    	    				if(tablep.contains("审计关注有价卡")){
    	    	    	    					money=tablep.substring(tablep.indexOf("张，金额")+4, tablep.indexOf("元。其中"));
    	    	    	    					money=money.replace(",", "");
    	    	    	    					ex_money=tablep.substring(tablep.indexOf("违规金额")+4, tablep.indexOf("元，占比"));
    	    	    	    					ex_money=ex_money.replace(",", "");
    	    	    	    					count=tablep.substring(tablep.indexOf("发生变更的累计")+7, tablep.indexOf("张，金额"));
    	    	    	    					count=count.replace(",", "");
    	    	    	    					ex_count=tablep.substring(tablep.indexOf("违规数量")+4, tablep.indexOf("张，占比"));
    	    	    	    					ex_count=ex_count.replace(",", "");
    	    	    	    					
    	    	    	    					ex_money_percent=tablep.substring(tablep.indexOf("元，占比")+4, tablep.indexOf("%。")+1);
    	    	    	    					ex_count_percent=tablep.substring(tablep.indexOf("张，占比")+4, tablep.indexOf("%；")+1);
    	    	    	    					doComparePrvoIl1(money,ex_money,ex_money_percent,count,ex_count,ex_count_percent,
    	     	    	    		             			sheet,workbook,chnlname,p1,p11, errorInfo,errorNum,f.getName());
    	    	    	    			   }

    	    	    	    			}
    	    	    	    		}
    	    	    	    		
    	    	    	    	}
    	    	    	    }
    	    		  } 
    	    		}
    	    	}
    	    	//比对全公司第一页1
    	    	for(Map<String,Object> all:allList){
    	    		for (File f:filelist){
    	    	    	if (f.isFile() && f.getName().endsWith(".docx")&&f.getName().contains("全公司")){
    	    	    		String tablep=readContentByExtractors(FILE_PATH_WORD+f.getName());//获取对应word内容
    	    	    		if(StringUtils.isNotBlank(tablep)){
    	    	    			if(flag ==3){
    	    	    				 String money,ex_money,ex_money_percent,count,ex_count,ex_count_percent;
    	    	    				if(tablep.contains("审计关注有价卡")){
    	    	    					money=tablep.substring(tablep.indexOf("张，金额")+4, tablep.indexOf("元。其中"));
    	    	    					money=money.replace(",", "");
    	    	    					
    	    	    					ex_money=tablep.substring(tablep.indexOf("违规金额")+4, tablep.indexOf("元，占比"));
    	    	    					ex_money=ex_money.replace(",", "");
    	    	    					
    	    	    					count=tablep.substring(tablep.indexOf("发生变更的累计")+7, tablep.indexOf("张，金额"));
    	    	    					count=count.replace(",", "");
    	    	    					
    	    	    					ex_count=tablep.substring(tablep.indexOf("违规数量")+4, tablep.indexOf("张，占比"));
    	    	    					ex_count=ex_count.replace(",", "");
    	    	    					
    	    	    					ex_money_percent=tablep.substring(tablep.indexOf("元，占比")+4, tablep.indexOf("%。")+1);
    	    	    					ex_count_percent=tablep.substring(tablep.indexOf("张，占比")+4, tablep.indexOf("%；")+1);
    	    	    					doComparePrvoIlAll1(money,ex_money,ex_money_percent,count,ex_count,ex_count_percent,
     	    	    		             			sheet,workbook,chnlname,all, errorInfo,errorNum,f.getName());
    	    	    			   }

    	    	    			}
    	    	    		}
    	    		        }
    	    	    	}
    	    	}
    	    	
    	    	//排名汇总第二页省公司比对
    	    	List<Map<String,Object>> prvdList2 =(List<Map<String,Object>>)exceldata.get("noload_yjk");
    	    	//全公司
	    	    List<Map<String,Object>> allList2  = (List<Map<String,Object>>)exceldata.get("noload_yjk_total");
    	    	//比对31个省份
    	    	for(Map<String,Object> p2:prvdList2){
    	    				for (File f:filelist){
    	    	    	    	if (f.isFile() && f.getName().endsWith(".docx")&&f.getName().contains(p2.get("prvdName_yjk").toString())){
    	    	    	    		String tablep=readContentByExtractors(FILE_PATH_WORD+f.getName());//获取对应word内容
    	    	    	    		if(StringUtils.isNotBlank(tablep)){
    	    	    	    			if(flag ==3){
    	    	    	    				 String ex_money,ex_count;
    	    	    	    				if(tablep.contains("有价卡生成数据未在BOSS系统和VC系统同步加载")){
    	    	    	    					tablep=tablep.substring(tablep.indexOf("有价卡生成数据未在BOSS系统和VC系统同步加载"));
    	    	    	    					ex_money=tablep.substring(tablep.indexOf("涉及金额")+4, tablep.indexOf("元"));
    	    	    	    					ex_money=ex_money.replace(",", "");
    	    	    	    					
    	    	    	    					ex_count=tablep.substring(tablep.indexOf("有价卡数量")+5, tablep.indexOf("张，涉及"));
    	    	    	    					ex_count=ex_count.replace(",", "");
    	    	    	    					doComparePrvoIl2(ex_money,ex_count,
    	     	    	    		             			sheet,workbook,chnlname,p2,errorInfo,errorNum,f.getName(),1);
    	    	    	    			   }

    	    	    	    			}
    	    	    	    		}
    	    	    	    		
    	    	    	    	}
    	    	    	    }
    	    	}
    	    	
    	    	//比对全公司第2,3,4,5,6页
    	    	for(Map<String,Object> all:allList2){
    	    		for (File f:filelist){
    	    	    	if (f.isFile() && f.getName().endsWith(".docx")&&f.getName().contains("全公司")){
    	    	    		String tablep=readContentByExtractors(FILE_PATH_WORD+f.getName());//获取对应word内容
    	    	    		if(StringUtils.isNotBlank(tablep)){
    	    	    				 if(StringUtils.isNotBlank(tablep)){
     	    	    	    			if(flag ==3){
     	    	    	    				 String ex_money,ex_count;
     	    	    	    				if(tablep.contains("有价卡生成数据未在BOSS系统和VC系统同步加载")){
     	    	    	    					tablep=tablep.substring(tablep.indexOf("有价卡生成数据未在BOSS系统和VC系统同步加载"));
     	    	    	    					ex_money=tablep.substring(tablep.indexOf("涉及金额")+4, tablep.indexOf("元"));
     	    	    	    					ex_money=ex_money.replace(",", "");
     	    	    	    					
     	    	    	    					ex_count=tablep.substring(tablep.indexOf("有价卡数量")+5, tablep.indexOf("张，涉及"));
     	    	    	    					ex_count=ex_count.replace(",", "");
     	    	    	    					doComparePrvoIlAll2(ex_money,ex_count,
         	    	    		             			sheet,workbook,chnlname,all, errorInfo,errorNum,f.getName(),1);
     	    	    	    			   }
     	    	    	    			}
     	    	    	    		}
    	    	    			}
    	    	    		}
    	    		       }
    	    	 }
    	    	
    	    	//排名汇总第三页省公司比对
    	    	List<Map<String,Object>> prvdList3 =(List<Map<String,Object>>)exceldata.get("not_active_yjk");
    	    	List<Map<String,Object>> allList3 =(List<Map<String,Object>>)exceldata.get("not_active_yjk_total");
    	    	//比对31个省份
    	    	for(Map<String,Object> p3:prvdList3){
    	    				for (File f:filelist){
    	    	    	    	if (f.isFile() && f.getName().endsWith(".docx")&&f.getName().contains(p3.get("prvdName_yjk").toString())){
    	    	    	    		String tablep=readContentByExtractors(FILE_PATH_WORD+f.getName());//获取对应word内容
    	    	    	    		if(StringUtils.isNotBlank(tablep)){
    	    	    	    			if(flag ==3){
    	    	    	    				 String ex_money,ex_count;
    	    	    	    				if(tablep.contains("有价卡销售激活在BOSS系统和VC系统不同步")){
    	    	    	    					tablep=tablep.substring(tablep.indexOf("有价卡销售激活在BOSS系统和VC系统不同步"));
    	    	    	    					ex_money=tablep.substring(tablep.indexOf("涉及金额")+4, tablep.indexOf("元"));
    	    	    	    					ex_money=ex_money.replace(",", "");
    	    	    	    					
    	    	    	    					ex_count=tablep.substring(tablep.indexOf("有价卡数量")+5, tablep.indexOf("张，涉及"));
    	    	    	    					ex_count=ex_count.replace(",", "");
    	    	    	    					doComparePrvoIl2(ex_money,ex_count,
    	     	    	    		             			sheet,workbook,chnlname,p3,errorInfo,errorNum,f.getName(),2);
    	    	    	    			   }

    	    	    	    			}
    	    	    	    		}
    	    	    	    		
    	    	    	    	}
    	    	    	    }
    	    	}
    	    	
    	    	//比对全公司第2,3,4,5,6页
    	    	for(Map<String,Object> all:allList3){
    	    		for (File f:filelist){
    	    	    	if (f.isFile() && f.getName().endsWith(".docx")&&f.getName().contains("全公司")){
    	    	    		String tablep=readContentByExtractors(FILE_PATH_WORD+f.getName());//获取对应word内容
    	    	    		if(StringUtils.isNotBlank(tablep)){
    	    	    				 if(StringUtils.isNotBlank(tablep)){
     	    	    	    			if(flag ==3){
     	    	    	    				 String ex_money,ex_count;
     	    	    	    				if(tablep.contains("有价卡销售激活在BOSS系统和VC系统不同步")){
    	    	    	    					tablep=tablep.substring(tablep.indexOf("有价卡销售激活在BOSS系统和VC系统不同步"));
    	    	    	    					ex_money=tablep.substring(tablep.indexOf("涉及金额")+4, tablep.indexOf("元"));
    	    	    	    					ex_money=ex_money.replace(",", "");
    	    	    	    					
    	    	    	    					ex_count=tablep.substring(tablep.indexOf("有价卡数量")+5, tablep.indexOf("张，涉及"));
    	    	    	    					ex_count=ex_count.replace(",", "");
    	    	    	    					doComparePrvoIlAll2(ex_money,ex_count,
         	    	    		             			sheet,workbook,chnlname,all, errorInfo,errorNum,f.getName(),2);
    	    	    	    			   }
     	    	    	    			}
     	    	    	    		}
    	    	    			}
    	    	    		}
    	    		       }
    	    	 }
    	    	
    	    	//第四页   
    	     	List<Map<String,Object>> prvdList4 =(List<Map<String,Object>>)exceldata.get("not_sale_yjk");
    	     	List<Map<String,Object>> allList4 =(List<Map<String,Object>>)exceldata.get("not_sale_yjk_total");
    	    	//比对31个省份
    	    	for(Map<String,Object> p4:prvdList4){
    	    				for (File f:filelist){
    	    	    	    	if (f.isFile() && f.getName().endsWith(".docx")&&f.getName().contains(p4.get("prvdName_yjk").toString())){
    	    	    	    		String tablep=readContentByExtractors(FILE_PATH_WORD+f.getName());//获取对应word内容
    	    	    	    		if(StringUtils.isNotBlank(tablep)){
    	    	    	    			if(flag ==3){
    	    	    	    				 String ex_money,ex_count;
    	    	    	    				if(tablep.contains("有价卡销售前已被充值")){
    	    	    	    					tablep=tablep.substring(tablep.indexOf("有价卡销售前已被充值"));
    	    	    	    					ex_money=tablep.substring(tablep.indexOf("涉及金额")+4, tablep.indexOf("元"));
    	    	    	    					ex_money=ex_money.replace(",", "");
    	    	    	    					
    	    	    	    					ex_count=tablep.substring(tablep.indexOf("有价卡数量")+5, tablep.indexOf("张，涉及"));
    	    	    	    					ex_count=ex_count.replace(",", "");
    	    	    	    					doComparePrvoIl2(ex_money,ex_count,
    	     	    	    		             			sheet,workbook,chnlname,p4,errorInfo,errorNum,f.getName(),3);
    	    	    	    			   }

    	    	    	    			}
    	    	    	    		}
    	    	    	    		
    	    	    	    	}
    	    	    	    }
    	    	}
    	    	
    	    	//比对全公司第2,3,4,5,6页
    	    	for(Map<String,Object> all:allList4){
    	    		for (File f:filelist){
    	    	    	if (f.isFile() && f.getName().endsWith(".docx")&&f.getName().contains("全公司")){
    	    	    		String tablep=readContentByExtractors(FILE_PATH_WORD+f.getName());//获取对应word内容
    	    	    		if(StringUtils.isNotBlank(tablep)){
    	    	    				 if(StringUtils.isNotBlank(tablep)){
     	    	    	    			if(flag ==3){
     	    	    	    				 String ex_money,ex_count;
     	    	    	    				if(tablep.contains("有价卡销售前已被充值")){
    	    	    	    					tablep=tablep.substring(tablep.indexOf("有价卡销售前已被充值"));
    	    	    	    					ex_money=tablep.substring(tablep.indexOf("涉及金额")+4, tablep.indexOf("元"));
    	    	    	    					ex_money=ex_money.replace(",", "");
    	    	    	    					
    	    	    	    					ex_count=tablep.substring(tablep.indexOf("有价卡数量")+5, tablep.indexOf("张，涉及"));
    	    	    	    					ex_count=ex_count.replace(",", "");
    	    	    	    					doComparePrvoIlAll2(ex_money,ex_count,
         	    	    		             			sheet,workbook,chnlname,all, errorInfo,errorNum,f.getName(),3);
    	    	    	    			   }
     	    	    	    			}
     	    	    	    		}
    	    	    			}
    	    	    		}
    	    		       }
    	    	 }
    	    	
    	    	//第五页   
    	     	List<Map<String,Object>> prvdList5 =(List<Map<String,Object>>)exceldata.get("unblocked_yjk");
    	     	List<Map<String,Object>> allList5 =(List<Map<String,Object>>)exceldata.get("unblocked_yjk_total");
    	    	//比对31个省份
    	    	for(Map<String,Object> p5:prvdList5){
    	    				for (File f:filelist){
    	    	    	    	if (f.isFile() && f.getName().endsWith(".docx")&&f.getName().contains(p5.get("prvdName_yjk").toString())){
    	    	    	    		String tablep=readContentByExtractors(FILE_PATH_WORD+f.getName());//获取对应word内容
    	    	    	    		if(StringUtils.isNotBlank(tablep)){
    	    	    	    			if(flag ==3){
    	    	    	    				 String ex_money,ex_count;
    	    	    	    				if(tablep.contains("有价卡报废未在VC系统锁定")){
    	    	    	    					tablep=tablep.substring(tablep.indexOf("有价卡报废未在VC系统锁定"));
    	    	    	    					ex_money=tablep.substring(tablep.indexOf("涉及金额")+4, tablep.indexOf("元"));
    	    	    	    					ex_money=ex_money.replace(",", "");
    	    	    	    					
    	    	    	    					ex_count=tablep.substring(tablep.indexOf("有价卡数量")+5, tablep.indexOf("张，涉及"));
    	    	    	    					ex_count=ex_count.replace(",", "");
    	    	    	    					doComparePrvoIl2(ex_money,ex_count,
    	     	    	    		             			sheet,workbook,chnlname,p5,errorInfo,errorNum,f.getName(),4);
    	    	    	    			   }

    	    	    	    			}
    	    	    	    		}
    	    	    	    		
    	    	    	    	}
    	    	    	    }
    	    	}
    	    	//比对全公司第2,3,4,5,6页
    	    	for(Map<String,Object> all:allList5){
    	    		for (File f:filelist){
    	    	    	if (f.isFile() && f.getName().endsWith(".docx")&&f.getName().contains("全公司")){
    	    	    		String tablep=readContentByExtractors(FILE_PATH_WORD+f.getName());//获取对应word内容
    	    	    		if(StringUtils.isNotBlank(tablep)){
    	    	    				 if(StringUtils.isNotBlank(tablep)){
     	    	    	    			if(flag ==3){
     	    	    	    				 String ex_money,ex_count;
     	    	    	    				if(tablep.contains("有价卡报废未在VC系统锁定")){
    	    	    	    					tablep=tablep.substring(tablep.indexOf("有价卡报废未在VC系统锁定"));
    	    	    	    					ex_money=tablep.substring(tablep.indexOf("涉及金额")+4, tablep.indexOf("元"));
    	    	    	    					ex_money=ex_money.replace(",", "");
    	    	    	    					
    	    	    	    					ex_count=tablep.substring(tablep.indexOf("有价卡数量")+5, tablep.indexOf("张，涉及"));
    	    	    	    					ex_count=ex_count.replace(",", "");
    	    	    	    					doComparePrvoIlAll2(ex_money,ex_count,
         	    	    		             			sheet,workbook,chnlname,all, errorInfo,errorNum,f.getName(),4);
    	    	    	    			   }
     	    	    	    			}
     	    	    	    		}
    	    	    			}
    	    	    		}
    	    		       }
    	    	 }
    	    	
    	    	//第六页   
    	     	List<Map<String,Object>> prvdList6 =(List<Map<String,Object>>)exceldata.get("repeat_yjk");
    	     	List<Map<String,Object>> allList6 =(List<Map<String,Object>>)exceldata.get("repeat_yjk_total");
    	    	//比对31个省份
    	    	for(Map<String,Object> p6:prvdList6){
    	    				for (File f:filelist){
    	    	    	    	if (f.isFile() && f.getName().endsWith(".docx")&&f.getName().contains(p6.get("prvdName_yjk").toString())){
    	    	    	    		String tablep=readContentByExtractors(FILE_PATH_WORD+f.getName());//获取对应word内容
    	    	    	    		if(StringUtils.isNotBlank(tablep)){
    	    	    	    			if(flag ==3){
    	    	    	    				 String ex_money,ex_count;
    	    	    	    				if(tablep.contains("有价卡已充值但未在VC系统标记")){
    	    	    	    					tablep=tablep.substring(tablep.indexOf("有价卡已充值但未在VC系统标记"));
    	    	    	    					ex_money=tablep.substring(tablep.indexOf("涉及金额")+4, tablep.indexOf("元"));
    	    	    	    					ex_money=ex_money.replace(",", "");
    	    	    	    					
    	    	    	    					ex_count=tablep.substring(tablep.indexOf("有价卡数量")+5, tablep.indexOf("张，涉及"));
    	    	    	    					ex_count=ex_count.replace(",", "");
    	    	    	    					doComparePrvoIl2(ex_money,ex_count,
    	     	    	    		             			sheet,workbook,chnlname,p6,errorInfo,errorNum,f.getName(),5);
    	    	    	    			   }
    	    	    	    			}
    	    	    	    		}
    	    	    	    		
    	    	    	    	}
    	    	    	    }
    	    	}
    	    	
    	    	//比对全公司第2,3,4,5,6页
    	    	for(Map<String,Object> all:allList6){
    	    		for (File f:filelist){
    	    	    	if (f.isFile() && f.getName().endsWith(".docx")&&f.getName().contains("全公司")){
    	    	    		String tablep=readContentByExtractors(FILE_PATH_WORD+f.getName());//获取对应word内容
    	    	    		if(StringUtils.isNotBlank(tablep)){
    	    	    				 if(StringUtils.isNotBlank(tablep)){
     	    	    	    			if(flag ==3){
     	    	    	    				 String ex_money,ex_count;
     	    	    	    				if(tablep.contains("有价卡已充值但未在VC系统标记")){
    	    	    	    					tablep=tablep.substring(tablep.indexOf("有价卡已充值但未在VC系统标记"));
    	    	    	    					ex_money=tablep.substring(tablep.indexOf("涉及金额")+4, tablep.indexOf("元"));
    	    	    	    					ex_money=ex_money.replace(",", "");
    	    	    	    					
    	    	    	    					ex_count=tablep.substring(tablep.indexOf("有价卡数量")+5, tablep.indexOf("张，涉及"));
    	    	    	    					ex_count=ex_count.replace(",", "");
    	    	    	    					doComparePrvoIlAll2(ex_money,ex_count,
         	    	    		             			sheet,workbook,chnlname,all, errorInfo,errorNum,f.getName(),5);
    	    	    	    			   }
     	    	    	    			}
     	    	    	    		}
    	    	    			}
    	    	    		}
    	    		       }
    	    	 }
    	    	
    	    	/*FileOutputStream outPath = new FileOutputStream(filepath);
    			workbook.write(outPath);
    			outPath.close();*/
    	    	//createTxt(errorInfo.toString(), FILE_PATH_EXCEL+chnlname+".txt");
       //输出比对结果入库-------------------------------------------------	
        	  	String result[]=errorInfo.toString().split(";");
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
            			data.setWordName(content[2]);
            			data.setWordValue(content[3]);
            			data.setExcelName("有价卡违规排名-"+audMonth);
            			data.setExcelValue(content[5]);
            			data.setCompareResult(content[6]);
            			data.setCreateDatetime(new Date());
            			data.setIsdel(0);
            			mybatisDao.add("DataCompareMapper.insertList", data);
            			zddataList.add(data);
        		}
        	  	}else{
        	  		resultList="有价卡排名汇总excel和word比对结果正常!";
        	  		return resultList;
        	  	}
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return resultList;
	}
	
	/**
	 * 所有省公司有价卡赠送比对
	 */
	public void doComparePrvoBusiness(
			String a,String b,String c,String d,String e,String f1,String g,
	        Sheet sheet,XSSFWorkbook workbook,String chnlname,
			Map dzqdata,StringBuffer errorInfo,int errorNum,String wordName){
	  if(StringUtils.isNotBlank(a) &&!dzqdata.get("a").equals("")){
		  double worda=Double.valueOf(a);
		  double excela=Double.valueOf(dzqdata.get("a").toString());
       	  if(worda!=excela){
       		  errorInfo.append(errorNum+++"、"+"财务数据a、"+wordName.substring(0,wordName.length()-5)+"、"+a.trim()+"、"+chnlname+"、"+dzqdata.get("a")+"、"+dzqdata.get("prvdName_yjk")+"_财务数据a不相同").append(";");	
          }
        }
	 if(StringUtils.isNotBlank(b) &&!dzqdata.get("b").equals("")){
		  double wordb=Double.valueOf(b);
		  double excelb=Double.valueOf(dzqdata.get("b").toString());
      	  if(wordb!=excelb){
 		   errorInfo.append(errorNum+++"、"+"业务数据b、"+wordName.substring(0,wordName.length()-5)+"、"+b.trim()+"、"+chnlname+"、"+dzqdata.get("b")+"、"+dzqdata.get("prvdName_yjk")+"_业务数据b不相同").append(";");	
          }
      }
	  if(StringUtils.isNotBlank(c) &&!dzqdata.get("c").equals("")){
		  String cg=c.substring(0,c.indexOf("%"));
		   Double wordc=Double.valueOf(cg);
		   String eg=dzqdata.get("c").toString();
		   String eg1=eg.substring(0,eg.indexOf("%"));
		   Double excelc=Double.valueOf(eg1);   
	      if(wordc-excelc!=0){
 		   errorInfo.append(errorNum+++"、"+"差异c、"+wordName.substring(0,wordName.length()-5)+"、"+c.trim()+"、"+chnlname+"、"+dzqdata.get("c")+"、"+dzqdata.get("prvdName_yjk")+"_差异c不相同").append(";");	
          }
      }
	  if(StringUtils.isNotBlank(d) &&!dzqdata.get("d").equals("")){
		  double wordd=Double.valueOf(d);
		  double exceld=Double.valueOf(dzqdata.get("d").toString());
      	  if(wordd!=exceld){
 		   errorInfo.append(errorNum+++"、"+"财务数据d、"+wordName.substring(0,wordName.length()-5)+"、"+d.trim()+"、"+chnlname+"、"+dzqdata.get("d")+"、"+dzqdata.get("prvdName_yjk")+"_财务数据d不相同").append(";");	
          }
      }
	  if(StringUtils.isNotBlank(e) &&!dzqdata.get("e").equals("")){
		  double worde=Double.valueOf(e);
		  double excele=Double.valueOf(dzqdata.get("e").toString());
      	  if(worde!=excele){
 		   errorInfo.append(errorNum+++"、"+"业务数据e、"+wordName.substring(0,wordName.length()-5)+"、"+e.trim()+"、"+chnlname+"、"+dzqdata.get("e")+"、"+dzqdata.get("prvdName_yjk")+"_业务数据e不相同").append(";");	
          }
      }
	  if(StringUtils.isNotBlank(f1) &&!dzqdata.get("f1").equals("")){
		  double wordf1=Double.valueOf(f1);
		  double excelf1=Double.valueOf(dzqdata.get("f1").toString());
      	  if(wordf1!=excelf1){
 		   errorInfo.append(errorNum+++"、"+"业务数据f、"+wordName.substring(0,wordName.length()-5)+"、"+f1.trim()+"、"+chnlname+"、"+dzqdata.get("f1")+"、"+dzqdata.get("prvdName_yjk")+"_业务数据f不相同").append(";");	
          }
      }
	  if(StringUtils.isNotBlank(g) &&!dzqdata.get("g").equals("")){
		   String gc=g.substring(0,g.indexOf("%"));
		   Double wordg=Double.valueOf(gc);
		   String eg=dzqdata.get("g").toString();
		   String eg1=eg.substring(0,eg.indexOf("%"));
		   Double excelg=Double.valueOf(eg1);   
 	      if(wordg-excelg!=0){
 		   errorInfo.append(errorNum+++"、"+"差异g、"+wordName.substring(0,wordName.length()-5)+"、"+g.trim()+"、"+chnlname+"、"+dzqdata.get("g")+"、"+dzqdata.get("prvdName_yjk")+"_差异g不相同").append(";");	
          }
      }
	}
	
	/**
	 * 业财汇总情况
	 * <pre>
	 * Desc  flag 
	 * @author cyz
	 * 2018-6-15 下午10:41:15
	 * </pre>
	 * @throws Exception 
	 */
	public String compareDataToWordBusiness(Map<String,Object> exceldata,XSSFWorkbook workbook, Sheet sheet
			,String filepath,int flag,String chnlname,String audMonth) throws Exception{
			String resultList="";
			int errorNum =1;
			StringBuffer errorInfo= new StringBuffer();
	        try {
	        	File file = new File(FILE_PATH_WORD);
	    	    File[] filelist = file.listFiles();
	    	  
	    	    //全公司
	    	    List<Map<String,Object>> allList  = (List<Map<String,Object>>)exceldata.get("yc_total");
	    	    //省电子券
    	    	List<Map<String,Object>> prvdList =(List<Map<String,Object>>)exceldata.get("yc_yjk");
    	    	//比对31个省份
    	    	for(Map<String,Object> yjkdata:prvdList){
    	    				for (File f:filelist){
    	    	    	    	if (f.isFile() && f.getName().endsWith(".docx")&&f.getName().contains(yjkdata.get("prvdName_yjk").toString())){
    	    	    	    		String table=readTableByExtractors(FILE_PATH_WORD+f.getName());//获取对应word内容
    	    	    	    		System.err.println(table);
    	    	    	    		if(StringUtils.isNotBlank(table)){
    	    	    	    			if(flag ==2){
   	    	    	    				String a = null,b = null,c = null,d = null,e = null,g = null,f1 = null;
    	    	    	    				if(table.contains("财务数据a")){
    	    	    	    					String sbstring=table.substring(table.indexOf("||d|-e-f|/MAX(|d|,e+f)")-3, table.indexOf("||d|-e-f|/MAX(|d|,e+f)")-1);
    	    	    	    					int tables=Integer.parseInt(sbstring);
    	    	    	    			    	 /*be_money=table.substring(table.indexOf("、"+String.valueOf(tables+1))+4, table.indexOf("、"+String.valueOf(tables+2)));//被充值赠卡金额（元）
    	    	    	    			    	 be_money=be_money.replace(",", "");*/
    	    	    	    			    	 a=table.substring(table.indexOf("、"+String.valueOf(tables+1))+4, table.indexOf("、"+String.valueOf(tables+2)));
    	    	    	    			    	 a=a.replace(",", "");
    	    	    	    			    	 b=table.substring(table.indexOf("、"+String.valueOf(tables+2))+4, table.indexOf("、"+String.valueOf(tables+3)));
    	    	    	    			    	 b=b.replace(",", "");
    	    	    	    			    	 c=table.substring(table.indexOf("、"+String.valueOf(tables+3))+4, table.indexOf("、"+String.valueOf(tables+4)));
    	    	    	    			    	 d=table.substring(table.indexOf("、"+String.valueOf(tables+4))+4, table.indexOf("、"+String.valueOf(tables+5)));
    	    	    	    			    	 d=d.replace(",", "");
    	    	    	    			    	 
    	    	    	    			    	 e=table.substring(table.indexOf("、"+String.valueOf(tables+5))+4, table.indexOf("、"+String.valueOf(tables+6)));
    	    	    	    			    	 e=e.replace(",", "");
    	    	    	    			    	 
    	    	    	    			    	 f1=table.substring(table.indexOf("、"+String.valueOf(tables+6))+4, table.indexOf("、"+String.valueOf(tables+7)));
    	    	    	    			    	 f1=f1.replace(",", "");
    	    	    	    			    	 if(table.contains("、"+String.valueOf(tables+8))){
    	    	    	    			    		 g=table.substring(table.indexOf("、"+String.valueOf(tables+7))+4, table.indexOf("、"+String.valueOf(tables+8)));
    	    	    	    			    	 }else{
    	    	    	    			    	 g=table.substring(table.indexOf("、"+String.valueOf(tables+7))+4, table.lastIndexOf("、"));
    	    	    	    			    	 }
    	    	    	    	    			 doComparePrvoBusiness(a,b,c,d,e,f1,g,
    	     	    	    		             			sheet,workbook,chnlname,yjkdata, errorInfo,errorNum,f.getName());
    	    	    	    			    }

    	    	    	    			}
    	    	    	    		}
    	    	    	    		
    	    	    	    	}
    	    	    	    }
    	    		
    	    	}
    	    	//比对全公司
    	    	for(Map<String,Object> yjkdataAll:allList){
    	    		for (File f:filelist){
    	    	    	if (f.isFile() && f.getName().endsWith(".docx")&&f.getName().contains("全公司")){
    	    	    		String tableAll=readTableByExtractors(FILE_PATH_WORD+f.getName());//获取对应word内容
    	    	    		if(StringUtils.isNotBlank(tableAll)){
    	    	    			if(flag ==2){
    	    	    				String a = null,b = null,c = null,d = null,e = null,g = null,f1 = null;
    	    	    				if(tableAll.contains("财务数据a")){
    	    	    					String sbstring=tableAll.substring(tableAll.indexOf("||d|-e-f|/MAX(|d|,e+f)")-3, tableAll.indexOf("||d|-e-f|/MAX(|d|,e+f)")-1);
    	    	    					int tables=Integer.parseInt(sbstring);
    	    	    					
    	    	    			    	 a=tableAll.substring(tableAll.indexOf("、"+String.valueOf(tables+1))+4, tableAll.indexOf("、"+String.valueOf(tables+2)));
    	    	    			    	 a=a.replace(",", "");
    	    	    			    	 b=tableAll.substring(tableAll.indexOf("、"+String.valueOf(tables+2))+4, tableAll.indexOf("、"+String.valueOf(tables+3)));
    	    	    			    	 b=b.replace(",", "");
    	    	    			    	 c=tableAll.substring(tableAll.indexOf("、"+String.valueOf(tables+3))+4, tableAll.indexOf("、"+String.valueOf(tables+4)));
    	    	    			    	 d=tableAll.substring(tableAll.indexOf("、"+String.valueOf(tables+4))+4, tableAll.indexOf("、"+String.valueOf(tables+5)));
    	    	    			    	 d=d.replace(",", "");
    	    	    			    	 
    	    	    			    	 e=tableAll.substring(tableAll.indexOf("、"+String.valueOf(tables+5))+4, tableAll.indexOf("、"+String.valueOf(tables+6)));
    	    	    			    	 e=e.replace(",", "");
    	    	    			    	 
    	    	    			    	 f1=tableAll.substring(tableAll.indexOf("、"+String.valueOf(tables+6))+4, tableAll.indexOf("、"+String.valueOf(tables+7)));
    	    	    			    	 f1=f1.replace(",", "");
    	    	    			    	 if(tableAll.contains("、"+String.valueOf(tables+8))){
    	    	    			    		 g=tableAll.substring(tableAll.indexOf("、"+String.valueOf(tables+7))+4, tableAll.indexOf("、"+String.valueOf(tables+8)));
    	    	    			    	 }else{
    	    	    			    	 g=tableAll.substring(tableAll.indexOf("、"+String.valueOf(tables+7))+4, tableAll.lastIndexOf("、"));
    	    	    			    	 }
    	    	    	    			 doComparePrvoBusiness(a,b,c,d,e,f1,g,
     	    	    		             			sheet,workbook,chnlname,yjkdataAll, errorInfo,errorNum,f.getName());
    	    	    			    }
    	    	    			}
    	    	    			}
    	    		        }
    	    	    	}
    	    	}
    	    	/*FileOutputStream outPath = new FileOutputStream(filepath);
    			workbook.write(outPath);
    			outPath.close();*/
    	    	//createTxt(errorInfo.toString(), FILE_PATH_EXCEL+chnlname+".txt");
    	    	//输出比对结果入库-------------------------------------------------	
        	  	String result[]=errorInfo.toString().split(";");
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
            			data.setWordName(content[2]);
            			data.setWordValue(content[3]);
            			data.setExcelName(chnlname);
            			data.setExcelValue(content[5]);
            			data.setCompareResult(content[6]);
            			data.setCreateDatetime(new Date());
            			data.setIsdel(0);
            			mybatisDao.add("DataCompareMapper.insertList", data);
            			zddataList.add(data);
        		}
        	  	}else{
        	  		resultList="有价卡业财excel和word比对结果正常!";
        	  		return resultList;
        	  	}
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return resultList;
	}
	
	
//	 解析表格
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
	
	private CellStyle createStyletxt(Workbook wb){
		CellStyle cs = wb.createCellStyle();
//		cs.setDataFormat(HSSFDataFormat.getBuiltinFormat("###,###"));
		 DataFormat format = wb.createDataFormat();
         cs.setDataFormat(format.getFormat("@"));
//        cs.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
		return cs;
	}
	
	private CellStyle createStyle(Workbook wb){
		CellStyle cs = wb.createCellStyle();
		cs.setDataFormat(HSSFDataFormat.getBuiltinFormat("###,###"));
		cs.setAlignment(CellStyle.ALIGN_CENTER);  
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER); 
		Font textFont = wb.createFont(); //字体颜色
		textFont.setFontName("宋体"); 
		textFont.setFontHeightInPoints((short)12);
		textFont.setColor(HSSFColor.RED.index);
//		textFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cs.setFont(textFont);
		return cs;
	}
	
	public static void main(String[] args) throws Exception{
		CompareDataYjkService pw =new CompareDataYjkService();
		FILE_PATH_EXCEL ="D:\\data1\\excelyjk";
		FILE_PATH_WORD = "D:\\data1\\wordyjk";
		File e=new File(FILE_PATH_EXCEL);
		File w=new File(FILE_PATH_WORD);
		pw.match(e,w,"201804");
		System.out.println("比对结束........");
		
	}

	//Double小数转换成百分比
	public String doubleExchangePercent(Double rate) {
		//百分数格式化  
        NumberFormat num = NumberFormat.getPercentInstance();   
        num.setMaximumFractionDigits(2);//最多两位百分小数，如25.23% 
        return num.format(rate); 
	}
	public String doubleExchangePercent3(Double rate) {
		//百分数格式化  
        NumberFormat num = NumberFormat.getPercentInstance();   
        num.setMaximumFractionDigits(3);
        return num.format(rate); 
	}
	public String doubleExchangePercent4(Double rate) {
		//百分数格式化  
        NumberFormat num = NumberFormat.getPercentInstance();   
        num.setMaximumFractionDigits(4);
        return num.format(rate); 
	}
	
	//金额类保留以万为单位并且保留2位小数
	public String moneyExchangeDecimal(String price){
        BigDecimal bigDecimal = new BigDecimal(price);  
        // 转换为万元（除以10000）  
        BigDecimal decimal = bigDecimal.divide(new BigDecimal("10000"));  
        // 保留两位小数  
        DecimalFormat formater = new DecimalFormat("0.00");  
        // 四舍五入  
        formater.setRoundingMode(RoundingMode.HALF_UP);    // 5000008.89  
        //formater.setRoundingMode(RoundingMode.HALF_DOWN);// 5000008.89  
        //formater.setRoundingMode(RoundingMode.HALF_EVEN);  
        // 格式化完成之后得出结果  
        return formater.format(decimal);
	}
	
	/**
	 * 解析word中表格内容
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public  String readTableByExtractors(String filePath) throws Exception {  
	    InputStream is = new FileInputStream(filePath);  
	    XWPFDocument doc = new XWPFDocument(is);  
	    StringBuffer sb=new StringBuffer();
	    List<XWPFParagraph> paras = doc.getParagraphs();  
	    /*for (XWPFParagraph para : paras) {  
	       //当前段落的属性  
           //CTPPr pr = para.getCTP().getPPr();  
	       System.out.println(para.getText());  
	    }*/  
	    //获取文档中所有的表格  
	    List<XWPFTable> tables = doc.getTables();  
	    List<XWPFTableRow> rows;  
	    List<XWPFTableCell> cells;  
	    int count=0;
	    for (XWPFTable table : tables) {  
	       //表格属性  
           //CTTblPr pr = table.getCTTbl().getTblPr();  
	       //获取表格对应的行  
	       rows = table.getRows();  
	       for (XWPFTableRow row : rows) {  
	          //获取行对应的单元格  
	          cells = row.getTableCells();  
	          for (XWPFTableCell cell : cells) {  
	        	  count+=1;
	        	  sb.append(count+"."+cell.getText()+"、");
	          }  
	       }  
	    }
	    close(is);
		return sb.toString();  
	}  
	
	/**
	 * 解析word中段中内容
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public  String readContentByExtractors(String filePath) throws Exception {  
	    InputStream is = new FileInputStream(filePath);  
	    XWPFDocument doc = new XWPFDocument(is);  
	    StringBuffer sb=new StringBuffer();
	    List<XWPFParagraph> paras = doc.getParagraphs();  
	    for (XWPFParagraph para : paras) {  
	       //当前段落的属性  
           //CTPPr pr = para.getCTP().getPPr();  
	       sb.append(para.getText());  
	    }  
	    //获取文档中所有的表格  
	   /* List<XWPFTable> tables = doc.getTables();  
	    List<XWPFTableRow> rows;  
	    List<XWPFTableCell> cells;  
	    int count=0;
	    for (XWPFTable table : tables) {  
	       //表格属性  
           //CTTblPr pr = table.getCTTbl().getTblPr();  
	       //获取表格对应的行  
	       rows = table.getRows();  
	       for (XWPFTableRow row : rows) {  
	          //获取行对应的单元格  
	          cells = row.getTableCells();  
	          for (XWPFTableCell cell : cells) {  
	        	  count+=1;
	        	  sb.append(count+"."+cell.getText()+"、");
	          }  
	       }  
	    }*/
	    close(is);
		return sb.toString();  
	}  
	 /** 
	  * 关闭输入流 
	  * @param is 
	  */  
	 public  void close(InputStream is) {  
	    if (is != null) {  
	       try {  
	          is.close();  
	       } catch (IOException e) {  
	          e.printStackTrace();  
	       }  
	    }  
	 } 
	 
//-有价卡排名汇总比对------------------------------------------------------------------	 
		/**
		 * 排名汇总第一页省公司
		 */
		public void doComparePrvoIl1(
				String money,String ex_money,String ex_money_percent,String count,String ex_count,String ex_count_percent,
		        Sheet sheet,XSSFWorkbook workbook,String chnlname,
				Map dzqdata,Map dzqdatas,StringBuffer errorInfo,int errorNum,String wordName){
			
		  if(StringUtils.isNotBlank(money) &&!dzqdata.get("money").equals("")){
			  String dmoney=dzqdata.get("money").toString();
			  if(dmoney.contains(".")){
				  dmoney=dmoney.substring(0,dmoney.indexOf("."));
			  }
	       	  if(!dmoney.equals(money.trim())){
	       		  errorInfo.append(errorNum+++"、"+"有价卡总金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+money.trim()+"、"+chnlname+"、"+dzqdata.get("money")+"、"+dzqdata.get("prvdName_yjk")+"_有价卡总金额不相同").append(";");	
	          }
	       }
		  if(StringUtils.isNotBlank(ex_money) &&!dzqdata.get("ex_money").equals("")){
			  String dex_money=dzqdata.get("ex_money").toString();
			  if(dex_money.contains(".")){
				  dex_money=dex_money.substring(0,dex_money.indexOf("."));
			  }
	       	  if(!dex_money.equals(ex_money.trim())){
	       		  errorInfo.append(errorNum+++"、"+"有价卡违规金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+ex_money.trim()+"、"+chnlname+"、"+dzqdata.get("ex_money")+"、"+dzqdata.get("prvdName_yjk")+"_有价卡违规金额不相同").append(";");	
	          }
	       }
		  if(StringUtils.isNotBlank(ex_money_percent) &&!dzqdata.get("ex_money_percent").equals("")){
	       	  if(!dzqdata.get("ex_money_percent").toString().equals(ex_money_percent.trim())){
	       		String e=dzqdata.get("ex_money_percent").toString();
	       		  String e1=e.substring(0, e.indexOf("%"));
	       		  double a=Double.valueOf(e1);
	       		  double a1=Double.valueOf(dfs3.format(a));
	       		  String e2=ex_money_percent.substring(0, ex_money_percent.indexOf("%"));
	       		  double b=Double.valueOf(e2);
	       		  double b1=Double.valueOf(dfs3.format(b));
	       		  if(a1-b1!=0){
	       		  errorInfo.append(errorNum+++"、"+"有价卡违规金额占比、"+wordName.substring(0,wordName.length()-5)+"、"+ex_money_percent.trim()+"、"+chnlname+"、"+dzqdata.get("ex_money_percent")+"、"+dzqdata.get("prvdName_yjk")+"_有价卡违规金额占比不相同").append(";");	
	       		  }
	       		}
	       }
		  
		  if(StringUtils.isNotBlank(count) &&!dzqdatas.get("count").equals("")){
			  String dcount=dzqdatas.get("count").toString();
			  if(dcount.contains(".")){
				  dcount=dcount.substring(0,dcount.indexOf("."));
			  }
	       	  if(!dcount.equals(count.trim())){
	       		  errorInfo.append(errorNum+++"、"+"有价卡总数量(张)、"+wordName.substring(0,wordName.length()-5)+"、"+count.trim()+"、"+chnlname+"、"+dzqdatas.get("count")+"、"+dzqdatas.get("prvdName_yjk")+"_有价卡总数量不相同").append(";");	
	               }
	       }
		  if(StringUtils.isNotBlank(ex_count) &&!dzqdatas.get("ex_count").equals("")){
			  String dexcount=dzqdatas.get("ex_count").toString();
			  if(dexcount.contains(".")){
				  dexcount=dexcount.substring(0,dexcount.indexOf("."));
			  }
	       	  if(!dexcount.equals(ex_count.trim())){
	       		  errorInfo.append(errorNum+++"、"+"有价卡违规量(张)、"+wordName.substring(0,wordName.length()-5)+"、"+ex_count.trim()+"、"+chnlname+"、"+dzqdatas.get("ex_count")+"、"+dzqdatas.get("prvdName_yjk")+"_有价卡违规量不相同").append(";");	
	           }
	       }
		 if(StringUtils.isNotBlank(ex_count_percent) &&!dzqdatas.get("ex_count_percent").equals("")){
	       	  if(!dzqdatas.get("ex_count_percent").toString().equals(ex_count_percent.trim())){
	       		  String e=dzqdatas.get("ex_count_percent").toString();
	       		  String e1=e.substring(0, e.indexOf("%"));
	       		  double a=Double.valueOf(e1);
	       		  double a1=Double.valueOf(dfs3.format(a));
	       		  String e2=ex_count_percent.substring(0, ex_count_percent.indexOf("%"));
	       		  double b=Double.valueOf(e2);
	       		  double b1=Double.valueOf(dfs3.format(b));
	       		  if(a1-b1!=0){
	       		  errorInfo.append(errorNum+++"、"+"有价卡违规数量占比、"+wordName.substring(0,wordName.length()-5)+"、"+ex_count_percent.trim()+"、"+chnlname+"、"+dzqdatas.get("ex_count_percent")+"、"+dzqdatas.get("prvdName_yjk")+"_有价卡违规数量占比不相同").append(";");	
	       		  }
	       		  }
	       }
		}
		/**
		 * 排名汇总第一页全公司公司
		 */
		public void doComparePrvoIlAll1(
				String money,String ex_money,String ex_money_percent,String count,String ex_count,String ex_count_percent,
		        Sheet sheet,XSSFWorkbook workbook,String chnlname,
				Map dzqdata,StringBuffer errorInfo,int errorNum,String wordName){
		  if(StringUtils.isNotBlank(money) &&!dzqdata.get("total_money").equals("")){
			  String dmoney=dzqdata.get("total_money").toString();
			  double d=Double.valueOf(money);
			  double e=Double.valueOf(dmoney);
	       	  if(d!=e){
	       		  errorInfo.append(errorNum+++"、"+"有价卡总金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+money.trim()+"、"+chnlname+"、"+dmoney+"、"+dzqdata.get("prvdName_yjk")+"_有价卡总金额不相同").append(";");	
	          }
	       }
		  if(StringUtils.isNotBlank(ex_money) &&!dzqdata.get("total_ex_money").equals("")){
			  String dex_money=dzqdata.get("total_ex_money").toString();
			  double d=Double.valueOf(dex_money);
			  double e=Double.valueOf(ex_money);
	       	  if(d!=e){
	       		  errorInfo.append(errorNum+++"、"+"有价卡违规金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+ex_money.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_money")+"、"+dzqdata.get("prvdName_yjk")+"_有价卡违规金额不相同").append(";");	
	          }
	       }
		  if(StringUtils.isNotBlank(ex_money_percent) &&!dzqdata.get("total_ex_money_percent").equals("")){
	       	  if(!dzqdata.get("total_ex_money_percent").toString().equals(ex_money_percent.trim())){
	       		String e=dzqdata.get("total_ex_money_percent").toString();
	       		  String e1=e.substring(0, e.indexOf("%"));
	       		  double a=Double.valueOf(e1);
	       		  String e2=ex_money_percent.substring(0, ex_money_percent.indexOf("%"));
	       		  double b=Double.valueOf(e2);
	       		  if(a!=b){
	       		  errorInfo.append(errorNum+++"、"+"有价卡违规金额占比、"+wordName.substring(0,wordName.length()-5)+"、"+ex_money_percent.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_money_percent")+"、"+dzqdata.get("prvdName_yjk")+"_有价卡违规金额占比不相同").append(";");	
	       		  }
	       	  }
	       }
		  
		  if(StringUtils.isNotBlank(count) &&!dzqdata.get("total_count").equals("")){
			  String dcount=dzqdata.get("total_count").toString();
			  double d=Double.valueOf(dcount);
			  double e=Double.valueOf(count);
	       	  if(d!=e){
	       		  errorInfo.append(errorNum+++"、"+"有价卡总数量(张)、"+wordName.substring(0,wordName.length()-5)+"、"+count.trim()+"、"+chnlname+"、"+dzqdata.get("total_count")+"、"+dzqdata.get("prvdName_yjk")+"_有价卡总数量不相同").append(";");	
	               }
	       }
		  if(StringUtils.isNotBlank(ex_count) &&!dzqdata.get("total_ex_count").equals("")){
			  String dexcount=dzqdata.get("total_ex_count").toString();
			  double d=Double.valueOf(dexcount);
			  double e=Double.valueOf(ex_count);
	       	  if(d!=e){
	       		  errorInfo.append(errorNum+++"、"+"有价卡违规量(张)、"+wordName.substring(0,wordName.length()-5)+"、"+ex_count.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_count")+"、"+dzqdata.get("prvdName_yjk")+"_有价卡违规量不相同").append(";");	
	           }
	       }
		 if(StringUtils.isNotBlank(ex_count_percent) &&!dzqdata.get("total_ex_count_percent").equals("")){
	       	  if(!dzqdata.get("total_ex_count_percent").toString().equals(ex_count_percent.trim())){
	       		String e=dzqdata.get("total_ex_count_percent").toString();
	       		  String e1=e.substring(0, e.indexOf("%"));
	       		  double a=Double.valueOf(e1);
	       		  String e2=ex_count_percent.substring(0, ex_count_percent.indexOf("%"));
	       		  double b=Double.valueOf(e2);
	       		  if(a!=b){
	       		  errorInfo.append(errorNum+++"、"+"有价卡违规数量占比、"+wordName.substring(0,wordName.length()-5)+"、"+ex_count_percent.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_count_percent")+"、"+dzqdata.get("prvdName_yjk")+"_有价卡违规数量占比不相同").append(";");	
	       		  }
	       		}
	       }
		}

		/**
		 * 排名汇总二页省公司
		 */
		public void doComparePrvoIl2(String ex_money,String ex_count, Sheet sheet,XSSFWorkbook workbook,String chnlname,
				Map dzqdata,StringBuffer errorInfo,int errorNum,String wordName,int sign){
		  if(StringUtils.isNotBlank(ex_money) &&!dzqdata.get("ex_money").equals("")){
			  String dmoney=dzqdata.get("ex_money").toString();
			  if(dmoney.contains(".")){
				  dmoney=dmoney.substring(0,dmoney.indexOf("."));
			  }
	       	  if(!dmoney.equals(ex_money.trim())){
	       		  if(sign==1){
	       			errorInfo.append(errorNum+++"、"+"未按规定在系统间同步加载-有价卡违规金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+ex_money.trim()+"、"+chnlname+"、"+dzqdata.get("ex_money").toString()+"、"+dzqdata.get("prvdName_yjk")+"_未按规定在系统间同步加载-有价卡违规金额不相同").append(";");	  
	       		  }else if(sign==2){
	       			errorInfo.append(errorNum+++"、"+"违规激活-有价卡违规金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+ex_money.trim()+"、"+chnlname+"、"+dzqdata.get("ex_money").toString()+"、"+dzqdata.get("prvdName_yjk")+"_违规激活-有价卡违规金额不相同").append(";");	    
	       		  }else if(sign==3){
	       			errorInfo.append(errorNum+++"、"+"违规销售-有价卡违规金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+ex_money.trim()+"、"+chnlname+"、"+dzqdata.get("ex_money").toString()+"、"+dzqdata.get("prvdName_yjk")+"_违规销售-有价卡违规金额不相同").append(";");	    
	       		  }else if(sign==4){
	       			errorInfo.append(errorNum+++"、"+"退换后的坏卡或报废卡未封锁-有价卡违规金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+ex_money.trim()+"、"+chnlname+"、"+dzqdata.get("ex_money").toString()+"、"+dzqdata.get("prvdName_yjk")+"_退换后的坏卡或报废卡未封锁-有价卡违规金额不相同").append(";");	    
	       		  }else if(sign==5){
	       			errorInfo.append(errorNum+++"、"+"违规重复使用-有价卡违规金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+ex_money.trim()+"、"+chnlname+"、"+dzqdata.get("ex_money").toString()+"、"+dzqdata.get("prvdName_yjk")+"_违规重复使用-有价卡违规金额不相同").append(";");	    
	       		  }
	       		  
	          }
	       }
		  if(StringUtils.isNotBlank(ex_count) &&!dzqdata.get("ex_count").equals("")){
			  String dcount=dzqdata.get("ex_count").toString();
			  if(dcount.contains(".")){
				  dcount=dcount.substring(0,dcount.indexOf("."));
			  }
	       	  if(!dcount.equals(ex_count.trim())){
	       		if(sign==1){
	       		  errorInfo.append(errorNum+++"、"+"未按规定在系统间同步加载-有价卡违规数量、"+wordName.substring(0,wordName.length()-5)+"、"+ex_count.trim()+"、"+chnlname+"、"+dzqdata.get("ex_count").toString()+"、"+dzqdata.get("prvdName_yjk")+"_未按规定在系统间同步加载-有价卡违规数量不相同").append(";");	
	       		}else if(sign==2){
	       		 errorInfo.append(errorNum+++"、"+"违规激活-有价卡违规数量、"+wordName.substring(0,wordName.length()-5)+"、"+ex_count.trim()+"、"+chnlname+"、"+dzqdata.get("ex_count").toString()+"、"+dzqdata.get("prvdName_yjk")+"_违规激活-有价卡违规数量不相同").append(";");	
	       		}else if(sign==3){
	       		 errorInfo.append(errorNum+++"、"+"违规销售-有价卡违规数量、"+wordName.substring(0,wordName.length()-5)+"、"+ex_count.trim()+"、"+chnlname+"、"+dzqdata.get("ex_count").toString()+"、"+dzqdata.get("prvdName_yjk")+"_违规销售-有价卡违规数量不相同").append(";");	
	       		}else if(sign==4){
	       		 errorInfo.append(errorNum+++"、"+"退换后的坏卡或报废卡未封锁-有价卡违规数量、"+wordName.substring(0,wordName.length()-5)+"、"+ex_count.trim()+"、"+chnlname+"、"+dzqdata.get("ex_count").toString()+"、"+dzqdata.get("prvdName_yjk")+"_退换后的坏卡或报废卡未封锁-有价卡违规数量不相同").append(";");	
	       		}else if(sign==5){
	       		 errorInfo.append(errorNum+++"、"+"违规重复使用-有价卡违规数量、"+wordName.substring(0,wordName.length()-5)+"、"+ex_count.trim()+"、"+chnlname+"、"+dzqdata.get("ex_count").toString()+"、"+dzqdata.get("prvdName_yjk")+"_违规重复使用-有价卡违规数量不相同").append(";");	
	       		}
	       	}
	       }
	  }
		
		/**
		 * 排名汇总全公司2,3,4,5,6页
		 */
		public void doComparePrvoIlAll2(String ex_money,String ex_count,Sheet sheet,XSSFWorkbook workbook,String chnlname,
				Map dzqdata,StringBuffer errorInfo,int errorNum,String wordName,int sign){
		  if(StringUtils.isNotBlank(ex_money) &&!dzqdata.get("total_ex_money").equals("")){
			  String dex_money=dzqdata.get("total_ex_money").toString();
			  double d=Double.valueOf(dex_money);
			  double e=Double.valueOf(ex_money);
	       	  if(d!=e){
	       		      if(sign==1){
		       			errorInfo.append(errorNum+++"、"+"未按规定在系统间同步加载-有价卡违规金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+ex_money.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_money").toString()+"、"+dzqdata.get("prvdName_yjk")+"_未按规定在系统间同步加载-有价卡违规金额不相同").append(";");	  
		       		  }else if(sign==2){
		       			errorInfo.append(errorNum+++"、"+"违规激活-有价卡违规金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+ex_money.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_money").toString()+"、"+dzqdata.get("prvdName_yjk")+"_违规激活-有价卡违规金额不相同").append(";");	    
		       		  }else if(sign==3){
		       			errorInfo.append(errorNum+++"、"+"违规销售-有价卡违规金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+ex_money.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_money").toString()+"、"+dzqdata.get("prvdName_yjk")+"_违规销售-有价卡违规金额不相同").append(";");	    
		       		  }else if(sign==4){
		       			errorInfo.append(errorNum+++"、"+"退换后的坏卡或报废卡未封锁-有价卡违规金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+ex_money.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_money").toString()+"、"+dzqdata.get("prvdName_yjk")+"_退换后的坏卡或报废卡未封锁-有价卡违规金额不相同").append(";");	    
		       		  }else if(sign==5){
		       			errorInfo.append(errorNum+++"、"+"违规重复使用-有价卡违规金额(元)、"+wordName.substring(0,wordName.length()-5)+"、"+ex_money.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_money").toString()+"、"+dzqdata.get("prvdName_yjk")+"_违规重复使用-有价卡违规金额不相同").append(";");	    
		       	      }
	       	  }
	       }
		  if(StringUtils.isNotBlank(ex_count) &&!dzqdata.get("total_ex_count").equals("")){
			  String dexcount=dzqdata.get("total_ex_count").toString();
			  double d=Double.valueOf(dexcount);
			  double e=Double.valueOf(ex_count);
	       	  if(d!=e){
	       		    if(sign==1){
		       		  errorInfo.append(errorNum+++"、"+"未按规定在系统间同步加载-有价卡违规数量、"+wordName.substring(0,wordName.length()-5)+"、"+ex_count.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_count").toString()+"、"+dzqdata.get("prvdName_yjk")+"_未按规定在系统间同步加载-有价卡违规数量不相同").append(";");	
		       		}else if(sign==2){
		       		 errorInfo.append(errorNum+++"、"+"违规激活-有价卡违规数量、"+wordName.substring(0,wordName.length()-5)+"、"+ex_count.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_count").toString()+"、"+dzqdata.get("prvdName_yjk")+"_违规激活-有价卡违规数量不相同").append(";");	
		       		}else if(sign==3){
		       		 errorInfo.append(errorNum+++"、"+"违规销售-有价卡违规数量、"+wordName.substring(0,wordName.length()-5)+"、"+ex_count.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_count").toString()+"、"+dzqdata.get("prvdName_yjk")+"_违规销售-有价卡违规数量不相同").append(";");	
		       		}else if(sign==4){
		       		 errorInfo.append(errorNum+++"、"+"退换后的坏卡或报废卡未封锁-有价卡违规数量、"+wordName.substring(0,wordName.length()-5)+"、"+ex_count.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_count").toString()+"、"+dzqdata.get("prvdName_yjk")+"_退换后的坏卡或报废卡未封锁-有价卡违规数量不相同").append(";");	
		       		}else if(sign==5){
		       		 errorInfo.append(errorNum+++"、"+"违规重复使用-有价卡违规数量、"+wordName.substring(0,wordName.length()-5)+"、"+ex_count.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_count").toString()+"、"+dzqdata.get("prvdName_yjk")+"_违规重复使用-有价卡违规数量不相同").append(";");	
		       		}
	       	  }
	       }
		}		
}
