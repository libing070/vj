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

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
/*import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.model.TextPiece;
import org.apache.poi.hwpf.model.TextPieceTable;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;*/
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
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.pojo.DataCompare;

//电子券比对
@Service
public class CompareDataDzqService extends BaseObject{
	
	@Autowired
	private MybatisDao mybatisDao;
	
	private static  String FILE_PATH_EXCEL = "";
	
	private static  String FILE_PATH_WORD = "";
	
	DecimalFormat dfs = new DecimalFormat("0");
	
	DecimalFormat dfs2 = new DecimalFormat("0.0000");
	
	static DecimalFormat dfs3 = new DecimalFormat("0.00");
	
	//获取excel数据
	public  String match(File webExcelDir,File webWordDir,String audMonth) throws Exception{
		//15:25
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
	        	if(f.getName().contains("电子券违规管理排名汇总全国_"+audMonth)){
	        	//创建要读入的文件的输入流 
		         inp = new FileInputStream(FILE_PATH_EXCEL+f.getName()); 
		        //根据上述创建的输入流 创建工作簿对象 
		         wb = new XSSFWorkbook(inp); 
		        //得到第一页 sheet 
		         sheet = wb.getSheetAt(0);
		         Map map =getExcelData(wb,sheet);
	        	
	        	  System.err.println("进入成功");
	        	  resultList= compareDataToWord(map,wb,sheet,FILE_PATH_EXCEL+f.getName(),1,"电子券违规管理排名汇总全国",audMonth);
	        	}
	        }
	    }
	       return  resultList;
	   
	}
	
	/**
	 * 获取排名汇总excel内容
	 * @param wb
	 * @param sheet
	 * @return
	 */
	public Map getExcelData(XSSFWorkbook wb,Sheet sheet){
		 //各公司"电子券管理违规"排名情况						
		 List<Map<String, Object>> dataList_prov =new ArrayList<Map<String, Object>>();
		 //全公司
		 List<Map<String, Object>> dataList_total =new ArrayList<Map<String, Object>>();
		 Map<String, Object> map =new HashMap<String, Object>();
		 Map<String, Object> exceldata = null;
    	 exceldata = new HashMap<String, Object>();
    	 exceldata.put("prvdName_dzq", sheet.getRow(16).getCell(1).toString()); 		    //省公司名称
    	 exceldata.put("total_payment", getCellValue_2(sheet.getRow(16).getCell(3)));       //发放总金额（万元）
    	 exceldata.put("total_count", getCellValue_2(sheet.getRow(16).getCell(4))); 		//发放总次数
    	 exceldata.put("total_ex_payment", getCellValue(sheet.getRow(16).getCell(6))); 	//异常发放总金额（万元）
    	 exceldata.put("total_ex_count", getCellValue_2(sheet.getRow(16).getCell(7))); 		//异常发放总次数
    	 exceldata.put("total_ex_percent", doubleExchangePercent(Double.valueOf(getCellValue(sheet.getRow(16).getCell(9))))); //异常发放金额占比%
    	 dataList_total.add(exceldata);
         //各公司"电子券管理违规"排名情况
	        for(int m = 17;m < 47;m++){
	        	exceldata = new HashMap<String, Object>();
	        	exceldata.put("row", m);
	        	exceldata.put("prvdName_dzq", sheet.getRow(m).getCell(1).toString()); 		//省公司名称
	        	if(sheet.getRow(m).getCell(3).toString().contains("未上传当期数据")){
	        		 exceldata.put("total_payment", sheet.getRow(m).getCell(3).toString()); //发放总金额（万元）-未上传当期数据
	        	}else{
	       	    exceldata.put("total_payment", getCellValue_2(sheet.getRow(m).getCell(3))); //发放总金额（万元）
	            exceldata.put("total_count", getCellValue_2(sheet.getRow(m).getCell(4))); 		//发放总次数
	       	    exceldata.put("total_ex_payment", getCellValue(sheet.getRow(m).getCell(6))); 	//异常发放总金额（万元）
	       	    exceldata.put("total_ex_count", getCellValue_2(sheet.getRow(m).getCell(7))); 		//异常发放总次数
	       	    exceldata.put("total_ex_percent", doubleExchangePercent(Double.valueOf(getCellValue(sheet.getRow(m).getCell(9))))); //异常发放金额占比%
	        	}
	       	    dataList_prov.add(exceldata);
	        }
	      map.put("dataList_prov", dataList_prov);
		  map.put("dataList_total", dataList_total);
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
	 * 比对电子券的数据
	 * <pre>
	 * Desc  flag 
	 * @author cyz
	 * 2018-5-28 下午10:41:15
	 * </pre>
	 * @throws Exception 
	 */
	public String compareDataToWord(Map<String,Object> exceldata,XSSFWorkbook workbook, Sheet sheet
			,String filepath,int flag,String chnlname,String audMonth) throws Exception{
			String resultList="";
			int errorNum =1;
			StringBuffer errorInfo= new StringBuffer();
	        try {
	        	File file = new File(FILE_PATH_WORD);
	    	    File[] filelist = file.listFiles();
	    	    //全公司
	    	    List<Map<String,Object>> allList  = (List<Map<String,Object>>)exceldata.get("dataList_total");
	    	    //省电子券
    	    	List<Map<String,Object>> prvdList =(List<Map<String,Object>>)exceldata.get("dataList_prov");
    	    	//比对31个省份
    	    	for(Map<String,Object> dzqdata:prvdList){
    	    		System.out.println("比对审计报告..........."+dzqdata.get("prvdName_dzq"));
    	    				for (File f:filelist){
    	    	    	    	if (f.isFile() && f.getName().endsWith(".doc")&&f.getName().contains(dzqdata.get("prvdName_dzq").toString())){
    	    	    	    		List<String> dataList =getWorddata(FILE_PATH_WORD+f.getName());//读取xml格式的word文档内容
    	    	    	    		for(String s:dataList){
    	    	    	    			System.err.println(s);
    	    	    	    		}
    	    	    	    		//审计关注电子券异常大额、多次发放以及向非正常状态用户、终端发放电子券。审计期间，共发放电子券154,189张，金额
    	    	    	    		//3,153,954.61元。本期异常发放电子券3,121张,金额
    	    	    	    		//1,879,218.95元，占电子券发放总金额的59.58%，存在电子券营销资源被违规使用或使用低效的风险。
    	    	    	    		if(dataList != null && dataList.size() > 0){
    	    	    	    			if(flag ==1){
    	    	    	    				//目前只针对2.0，3.0生产缺失文档后期才上
    	    	    	    				if(dzqdata.get("total_payment").toString().contains("未上传当期数据")){
    	    	    	    					if(!dataList.contains("未上传数据")){
    	    	    	    					 errorInfo.append(errorNum+++"、"+"审计期间，未上传数据、"+f.getName()+"、"+"-"+"、"+chnlname+"、"+
    	    	    	    					 dzqdata.get("total_payment").toString()+"、"+dzqdata.get("prvdName_dzq")+"_未发现审计期间，未上传数据关键字信息").append(";");		
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
    	    	    	    			    doComparePrvo(count,money,excount,exmoney,exPercent, 
    	    	    		             			sheet,workbook,chnlname,dzqdata, errorInfo,errorNum,f.getName());
    	    	    	    				}
    	    	    	    			}
    	    	    		        }
    	    	    	    	}
    	    	    	    }
    	    		
    	    	}
    	    	//比对全公司
    	    	for(Map<String,Object> dzqdataAll:allList){
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
    	    	}
    	    	FileOutputStream outPath = new FileOutputStream(filepath);
    			workbook.write(outPath);
    			outPath.close();
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
            			data.setExcelName("电子券违规管理排名汇总全国_"+audMonth);
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
	        return resultList;
	}
	
	
	/**
	 * 所有省份比对
	 * @param count
	 * @param money
	 * @param excount
	 * @param exmoney
	 * @param exPercent
	 * @param sheet
	 * @param workbook
	 * @param chnlname
	 * @param dzqdata
	 * @param errorInfo
	 * @param errorNum
	 * @param wordName
	 */
	public void doComparePrvo(String count,String money,String excount,String exmoney,
			String exPercent,Sheet sheet,XSSFWorkbook workbook,String chnlname,
			Map dzqdata,StringBuffer errorInfo,int errorNum,String wordName){
		if(money != null &&!dzqdata.get("total_payment").equals("")&& !money.equals("")){
       	  if(!dzqdata.get("total_payment").equals(moneyExchangeDecimal(money.trim()))){
       		  errorInfo.append(errorNum+++"、"+"发放总金额(万元)、"+wordName.substring(0,wordName.length()-4)+"、"+money.trim()+"、"+chnlname+"、"+dzqdata.get("total_payment")+"、"+dzqdata.get("prvdName_dzq")+"_发放总金额不相同").append(";");	
               }
        }
		if(count != null &&!dzqdata.get("total_count").equals("")&& !count.equals("")){
	      if(!dzqdata.get("total_count").equals(count.trim())){
	          errorInfo.append(errorNum+++"、"+"发放总次数、"+wordName.substring(0,wordName.length()-4)+"、"+count.trim()+"、"+chnlname+"、"+dzqdata.get("total_count")+"、"+dzqdata.get("prvdName_dzq")+"_发放总次数不相同").append(";");	
	       }
	    }
		if(exmoney != null &&!dzqdata.get("total_ex_payment").equals("")&& !exmoney.equals("")){
	       	  if(!dzqdata.get("total_ex_payment").equals(moneyExchangeDecimal(exmoney.trim()))){
	       		Double tex=Double.valueOf(dzqdata.get("total_ex_payment").toString());
	       		  Double ex=Double.valueOf(exmoney);
	       		  System.err.println(dzqdata.get("prvdName_dzq").toString()+tex*1000000);
	       		  System.err.println(ex*10000);
	       		if(tex*1000000-ex*100>100  || tex*10000-ex<-100){
	       			errorInfo.append(errorNum+++"、"+"异常发放总金额(万元)、"+wordName.substring(0,wordName.length()-4)+"、"+exmoney.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_payment")+"、"+dzqdata.get("prvdName_dzq")+"_异常发放总金额不相同").append(";");	 
	       		  }
	          }
	    }
		if(excount != null &&!dzqdata.get("total_ex_count").equals("")&& !excount.equals("")){
		      if(!dzqdata.get("total_ex_count").equals(excount.trim())){
		          errorInfo.append(errorNum+++"、"+"异常发放总次数、"+wordName.substring(0,wordName.length()-4)+"、"+excount.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_count")+"、"+dzqdata.get("prvdName_dzq")+"_异常发放总次数不相同").append(";");	
		       }
		}
		if(exPercent != null &&!dzqdata.get("total_ex_percent").equals("")&& !exPercent.equals("")){
		      if(!dzqdata.get("total_ex_percent").equals(exPercent.trim())){
		    	  String ex=dzqdata.get("total_ex_percent").toString();
		    	  String e=ex.substring(0,ex.length()-1);
		    	  String te=exPercent.substring(0,exPercent.length()-1);
		    	  if(Double.valueOf(e)*10000!=Double.valueOf(te)*10000){
		    		  errorInfo.append(errorNum+++"、"+"异常发放金额占比、"+wordName.substring(0,wordName.length()-4)+"、"+exPercent.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_percent")+"、"+dzqdata.get("prvdName_dzq")+"_异常发放金额占比不相同").append(";");	
		    	  }
		      }
		      
		}
	}
	
	/**
	 * 全公司比对
	 * @param count
	 * @param money
	 * @param excount
	 * @param exmoney
	 * @param exPercent
	 * @param sheet
	 * @param workbook
	 * @param chnlname
	 * @param dzqdata
	 * @param errorInfo
	 * @param errorNum
	 * @param wordName
	 */
	public void doCompareAll(String count,String money,String excount,String exmoney,
			String exPercent,Sheet sheet,XSSFWorkbook workbook,String chnlname,
			Map dzqdata,StringBuffer errorInfo,int errorNum,String wordName){
		if(money != null &&!dzqdata.get("total_payment").equals("")&& !money.equals("")){
       	  if(!dzqdata.get("total_payment").equals(moneyExchangeDecimal(money.trim()))){
       		  errorInfo.append(errorNum+++"、"+"发放总金额(万元)、"+wordName.substring(0,wordName.length()-4)+"、"+money.trim()+"、"+chnlname+"、"+dzqdata.get("total_payment")+"、"+dzqdata.get("prvdName_dzq")+"_发放总金额不相同").append(";");	
               }
        }
		if(count != null &&!dzqdata.get("total_count").equals("")&& !count.equals("")){
	      if(!dzqdata.get("total_count").equals(count.trim())){
	          errorInfo.append(errorNum+++"、"+"发放总次数、"+wordName.substring(0,wordName.length()-4)+"、"+count.trim()+"、"+chnlname+"、"+dzqdata.get("total_count")+"、"+dzqdata.get("prvdName_dzq")+"_发放总次数不相同").append(";");	
	       }
	    }
		if(exmoney != null &&!dzqdata.get("total_ex_payment").equals("")&& !exmoney.equals("")){
	       	  if(!dzqdata.get("total_ex_payment").equals(moneyExchangeDecimal(exmoney.trim()))){
	       		  Double tex=Double.valueOf(dzqdata.get("total_ex_payment").toString());
	       		  Double ex=Double.valueOf(exmoney);
	       		  if(tex*1000000-ex*100>100  || tex*10000-ex<-100){
	       			 errorInfo.append(errorNum+++"、"+"异常发放总金额(万元)、"+wordName.substring(0,wordName.length()-4)+"、"+exmoney.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_payment")+"、"+dzqdata.get("prvdName_dzq")+"_异常发放总金额不相同").append(";");	  
	       		  }
	          }
	    }
		if(excount != null &&!dzqdata.get("total_ex_count").equals("")&& !excount.equals("")){
		      if(!dzqdata.get("total_ex_count").equals(excount.trim())){
		          errorInfo.append(errorNum+++"、"+"异常发放总次数、"+wordName.substring(0,wordName.length()-4)+"、"+excount.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_count")+"、"+dzqdata.get("prvdName_dzq")+"_异常发放总次数不相同").append(";");	
		       }
		}
		if(exPercent != null &&!dzqdata.get("total_ex_percent").equals("")&& !exPercent.equals("")){
		      if(!dzqdata.get("total_ex_percent").equals(exPercent.trim())){
		    	  String ex=dzqdata.get("total_ex_percent").toString();
		    	  String e=ex.substring(0,ex.length()-1);
		    	  String te=exPercent.substring(0,exPercent.length()-1);
		    	  if(Double.valueOf(e)*10000!=Double.valueOf(te)*10000){
		          errorInfo.append(errorNum+++"、"+"异常发放金额占比、"+wordName.substring(0,wordName.length()-4)+"、"+exPercent.trim()+"、"+chnlname+"、"+dzqdata.get("total_ex_percent")+"、"+dzqdata.get("prvdName_dzq")+"_异常发放金额占比不相同").append(";");	
		    	  }
		      }
		}
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
	
	/*public static void main(String[] args) throws Exception{
		dfs3.format("0.00");
		dfs3.format("0");
		if(dfs3.format("0.00").equals(dfs3.format("0"))){
			System.err.println("aaa");
		}
		CompareDataDzqService pw =new CompareDataDzqService();
		FILE_PATH_EXCEL ="D:\\data1\\excel";
		FILE_PATH_WORD = "D:\\data1\\word";
		pw.match(FILE_PATH_EXCEL,FILE_PATH_WORD,"201804");
		System.out.println("比对结束........");
		
	}*/

	//Double小数转换成百分比
	public String doubleExchangePercent(Double rate) {
		//百分数格式化  
        NumberFormat num = NumberFormat.getPercentInstance();   
        num.setMaximumFractionDigits(2);//最多两位百分小数，如25.23% 
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
}
