package com.hpe.cmca.service;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.pojo.DataCompare;
import com.hpe.cmca.util.FtpUtil;
import com.hpe.cmca.util.StringUtils;
import com.hpe.cmca.util.ZipFileUtil;

@Service
public class DataComparesService extends BaseObject {
	@Autowired
	private MybatisDao mybatisDao;

	@Autowired
	private CompareDataZdtlService compareDataZdtlService = null;


	@Autowired
	private CompareDataDzqService compareDataDzqService = null;

	@Autowired
	private CompareDataYjkService compareDataYjkService = null;


	private FTPClient			client;

	private static final Logger	logger	= Logger.getLogger(FtpUtil.class);

	/**
	 * 获取主题下拉
	 *
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectSubject() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		return mybatisDao.getList("DataCompareMapper.selectSubject", params);
	}

	/**
	 * 获取审计月份下拉
	 *
	 * @param subjectId
	 * @param roleId
	 * @return
	 */
    public List<Map<String,Object>> getAuditMonth(String subjectId,String roleId){
			Map<String,Object> params =new HashMap<String,Object>();
			params.put("subjectId", subjectId);
			List<Map<String,Object>> dataList =new ArrayList<Map<String,Object>>();
			if(roleId != null && (roleId.equals("1"))){
				dataList= mybatisDao.getList("DataCompareMapper.getAuditMonth", params);
			}
			return dataList;
	}

    /**
	 * 获取省份名称和代码
	 *
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getProvinceCode() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		return mybatisDao.getList("DataCompareMapper.getProvinceCode", params);
	}

	/**
	 * 清除上次比对结果数据
	 *
	 * @return
	 * @throws Exception
	 */
	public void  updateDataIsdel() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		 mybatisDao.update("DataCompareMapper.updateDataIsdel", params);
	}

    /**
     * 比对数据列表接口
     * @return
     */
    public Map<String, Object> selDataCompareList() {
    	Map<String, Object> params = new HashMap<String, Object>();
    	List<Map<String,Object>> dataList=null;
    	Map<String,Object> map=new HashMap<String,Object>();
		  dataList=mybatisDao.getList("DataCompareMapper.selDataCompare", params);
		  Integer countResult=(Integer)mybatisDao.get("DataCompareMapper.countDataCompare", params);
	      map.put("countResult", countResult);
	      map.put("dataList", dataList);
		return map;
    }

    /**
     * 比对结果都正常默认插入一条数据库做为记录不展示，
     * isdel 0:代表当次异常结果插入
     *       1:代表已清除的历史数据
     *       2:代表当次正常结果插入
     */
    public void addDatabase(){
            DataCompare data=new DataCompare();
			data.setOrderNum(1);
			data.setFieldName("无异常字段");
			data.setWordName("正常");
			data.setWordValue("正常");
			data.setExcelName("正常");
			data.setExcelValue("正常");
			data.setCompareResult("本次文件和数据比对结果都正常");
			data.setCreateDatetime(new Date());
			data.setIsdel(2);
			mybatisDao.add("DataCompareMapper.insertList", data);
	}

    //---数据比对核心逻辑--------------------------------------------------------------
	private  String FILE_PATH_EXCEL = "";

	private  String FILE_PATH_WORD = "";

	DecimalFormat dfs = new DecimalFormat("0");

	DecimalFormat dfs2 = new DecimalFormat("0.0000");

	DecimalFormat dfs3 = new DecimalFormat("0.00");

	/**
	 * 数据比对核心逻辑
	 * 获取excel数据
	 * @param webExcelDir
	 * @param webWordDir
	 * @return
	 * @throws Exception
	 */
	public  String match(File webExcelDir,File webWordDir,String audMonth) throws Exception{
		StringBuffer sb=new StringBuffer();
		//String localDir=DataComparesService.class.getClassLoader().getResource("").getPath();//获取当前类class的路径
		logger.error(">>>>>Startcompare current directory is.....[" + webExcelDir + "]");
		logger.error(">>>>>Startcompare current directory is.....[" + webWordDir + "]");
		FILE_PATH_EXCEL =webExcelDir+File.separator;
		FILE_PATH_WORD = webWordDir+File.separator;
		logger.error(">>>>>endcompare current directory is.....[" + FILE_PATH_EXCEL + "]");
		logger.error(">>>>>endcompare current directory is.....[" + FILE_PATH_WORD + "]");
		 InputStream inp =null;
		 XSSFWorkbook wb =null;
		 Sheet sheet =null;
		 int num=1;
		File file = new File(FILE_PATH_EXCEL);
		logger.error(">>>>>filePath current directory is.....[" + file.getPath() + "]");
	    File[] filelist = file.listFiles();   //listFiles()方法是返回某个目录下所有文件和目录的绝对路径，返回的是File数组
	    logger.error(">>>>>fileLength current directory is.....[" + file.length() + "]");
	    for (File f:filelist){
	    	logger.error(">>>>>excelFileName current directory is.....[" + file.getName() + "]");
	    	//System.err.println("文件：：："+f.getName());
	       if (f.isFile() && (f.getName().endsWith(".xls") || f.getName().endsWith(".xlsx"))){
	    	   if(f.getName().indexOf("渠道养卡排名汇总")==-1){//排除掉总的汇总单
	        	//创建要读入的文件的输入流
		         inp = new FileInputStream(FILE_PATH_EXCEL+f.getName());
		        //根据上述创建的输入流 创建工作簿对象
		         wb = new XSSFWorkbook(inp);
		        //得到第一页 sheet
		         sheet = wb.getSheetAt(0);
		         Map map =getExcelData(wb,sheet);
	        	if(f.getName().contains("社会渠道汇总")){
	        		logger.error(">>>>>包含社会渠道汇总文件");
		 		if(compareDataToWord(map,wb,sheet,FILE_PATH_EXCEL+f.getName(),5,"社会渠道汇总",num,audMonth)!=null){
		 			sb.append("社会渠道汇总excel和word比对结果正常,");
		 			logger.error(">>>>>社会渠道汇总文件比对完成");
		 		}
	        	}
	        	if(f.getName().contains("自有渠道汇总")){
	        		if(compareDataToWord(map,wb,sheet,FILE_PATH_EXCEL+f.getName(),4,"自有渠道汇总",num,audMonth)!=null){
	        			sb.append("自有渠道汇总excel和word比对结果正常,");
	        		}
	        	}
	        	if(f.getName().contains("全渠道汇总")){
	        		if(compareDataToWord(map,wb,sheet,FILE_PATH_EXCEL+f.getName(),6,"全渠道汇总",num,audMonth)!=null){
	        			sb.append("全渠道汇总excel和word比对结果正常,");
	        		}
	        	}
	        	if(f.getName().contains("其它渠道汇总")){
                   // compareDataToWord(map,wb,sheet,FILE_PATH_EXCEL+f.getName(),0,"其它渠道汇总");
	        	}
	    	   }
	        }
	    }
		return sb.toString();

	}


	public Map getExcelData(XSSFWorkbook wb,Sheet sheet){
		 //养卡号码数量
		 List<Map<String, Object>> dataList_card =new ArrayList<Map<String, Object>>();
		 //渠道数量
		 List<Map<String, Object>> dataList_chnl =new ArrayList<Map<String, Object>>();
		 //全公司
		 List<Map<String, Object>> dataList_total =new ArrayList<Map<String, Object>>();
		 Map<String, Object> map =new HashMap<String, Object>();
		 Map<String, Object> exceldata = null;
//		 sheet.getRow(10).getCell(3).setCellStyle(createStyletxt(wb));
		 //总体情况
    	 exceldata = new HashMap<String, Object>();

    	 exceldata.put("total_usernum", sheet.getRow(6).getCell(3).toString()); 		//入网号码数量
    	 exceldata.put("total_cardnum", sheet.getRow(7).getCell(3).toString()); 		//疑似养卡号码数量
    	 exceldata.put("total_cardpercent", getCellValue(sheet.getRow(8).getCell(3))); 	//疑似养卡号码占比%
    	 exceldata.put("total_chnlnum", getCellValue(sheet.getRow(9).getCell(3))); 		//涉及社会渠道数量
    	 exceldata.put("total_chnlpercent", getCellValue(sheet.getRow(10).getCell(3))); //涉及社会渠道占比%
    	 dataList_total.add(exceldata);
         //各公司疑似养卡号码数量及占比
	        for(int m = 17;m < 48;m++){
	        	exceldata = new HashMap<String, Object>();
	        	exceldata.put("row", m);
	        	exceldata.put("prvdName_card", sheet.getRow(m).getCell(2).toString());  //省份
	        	exceldata.put("ykcardNum", getCellValue(sheet.getRow(m).getCell(3))); 		//养卡号码
	        	exceldata.put("ykcardPercent", getCellValue_2(sheet.getRow(m).getCell(4)));  //占比
	        	exceldata.put("ykhuanbi", getCellValue_2(sheet.getRow(m).getCell(5)));   	//环比
	        	dataList_card.add(exceldata);
	        }
	        //各公司疑似养卡渠道数量及占比
	       for(int mi = 54;mi < 85;mi++){
	    	  exceldata = new HashMap<String, Object>();
	    	  exceldata.put("row", mi);
	    	  exceldata.put("prvdName_chnl", sheet.getRow(mi).getCell(2).toString());//省份
	    	  exceldata.put("chnlNum", getCellValue(sheet.getRow(mi).getCell(3)));     //涉及渠道数量
	    	  exceldata.put("chnlPercent", getCellValue_2(sheet.getRow(mi).getCell(4))); //占比 %
	    	  exceldata.put("chnlHuanbi", getCellValue_2(sheet.getRow(mi).getCell(5)));  //渠道数量环比增幅%
	    	  dataList_chnl.add(exceldata);
	       }
	      map.put("allprvdcard", dataList_card);
	      map.put("allprvdchnl", dataList_chnl);
		  map.put("totalnum", dataList_total);
		  return map;
	}


	public String getCellValue(Cell cell){
		String returnvalue = null;
		switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:  // 字符串
                	returnvalue =cell.getRichStringCellValue()
                            .getString();
                    break;
                case Cell.CELL_TYPE_NUMERIC:   // 数值型
                    if (DateUtil.isCellDateFormatted(cell)) {  //日期类型
                    	returnvalue=cell.getDateCellValue().toLocaleString();
                    } else {
                    	if(isNumber(cell.getNumericCellValue())){//如果是正数
                    		returnvalue = dfs.format(cell.getNumericCellValue());
//                    		returnvalue =Long.toString(Math.round(cell.getNumericCellValue()));
                    	}else{//为小数则保留4位小数
                    		returnvalue = dfs2.format(cell.getNumericCellValue());
//                    		BigDecimal a1 = new BigDecimal(cell.getNumericCellValue());
//                    		returnvalue =a1.toString();
                    	}
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:  // Boolean
                	returnvalue=Boolean.toString(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:   // 公式
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

	//return 正数为true，小数为false
	public boolean isNumber(double num){
		boolean n =true;
		char a2 ='0';
		String str_num =Double.toString(num);
		if(str_num.indexOf(".") >= 0){//判断是否存在
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


	/**
	 * 核心解析xml格式的word文档,本项目不支持poi的HWPF
	 * @param filename
	 * @return
	 */
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
	 * 比对数据
	 * <pre>
	 * Desc  flag 标识渠道类型（社会渠道=5，自有渠道=4，全渠道，其他渠道）
	 * @author issuser
	 * 2017-6-24 下午12:11:15
	 * </pre>
	 * @throws Exception
	 */
	public String compareDataToWord(Map<String,Object> exceldata,XSSFWorkbook workbook, Sheet sheet
			,String filepath,int flag,String chnlname,int num,String audMonth) throws Exception{
		    String resultList=null;
			String para = null;
			int errorNum =1;
			StringBuffer errorInfo= new StringBuffer();
	        try {
	        	logger.error(">>>>>开始进行比对读取word数据");
	        	File file = new File(FILE_PATH_WORD);
	    	    File[] filelist = file.listFiles();
	    	    //全公司
	    	    List<Map<String,Object>> allList  = (List<Map<String,Object>>)exceldata.get("totalnum");
	    	    //养卡数量
    	    	List<Map<String,Object>> prvdcardList =(List<Map<String,Object>>)exceldata.get("allprvdcard");
    	    	//渠道数量
    	    	List<Map<String,Object>> prvdchnlList =(List<Map<String,Object>>)exceldata.get("allprvdchnl");
    	    	//比对31个省份
    	    	for(Map<String,Object> carddata:prvdcardList){
    	    		//System.out.println("比对省份.............."+carddata.get("prvdName_card"));
    	    		for(Map<String,Object> chnldata:prvdchnlList){
    	    			if(chnldata.containsValue(carddata.get("prvdName_card"))){//先保证养卡数量和渠道数量是同一省份
    	    				for (File f:filelist){//保证excel和word省份是相同的
    	    	    	    	if (f.isFile() && f.getName().endsWith(".doc")&&f.getName().contains(carddata.get("prvdName_card").toString())){
    	    	    	    		InputStream	is = new FileInputStream(FILE_PATH_WORD+f.getName());
    	    	    	    		//社会渠道入网号码数量                        疑似养卡号码数量                               疑似养卡号码占比%        涉及社会渠道数量                             涉及社会渠道占比%
    	    	    	    		String totalnum =null,total_cardnum=null,total_cardper=null,totalqudaonum=null,total_percent=null;
    	    	    	    		//解析word
    	    	    	    		List<String> dataList =getWorddata(FILE_PATH_WORD+f.getName());
    	    	    	    		if(dataList != null && dataList.size() > 0){
    	    	    	    			if(flag ==4){
    	    	    	    				//自有渠道
        	    	    		        	String data = dataList.get(10);//共计
        	    	    		        	String ykcrad_num = dataList.get(12);   //养卡号码数量
        	    	    		            String card_percent = dataList.get(14);	//养卡号码数量占比
        	    	    		            String card_huanbi = dataList.get(16);	//环比
        	    	    		            String chnl_num = dataList.get(18);		//渠道数量
        	    	    		            String chnl_percent = dataList.get(20); //渠道占比
        	    	    		            String chnl_huanbi=dataList.get(22);	//渠道环比
        	    	    		            String s1 =card_huanbi.substring(0,2);
        	    	    		             	String s =chnl_huanbi.substring(0,2);
        	    	    		             	card_percent =card_percent.replace("%", "");
        	    	    		             	chnl_percent =chnl_percent.replace("%", "");
        	    	    		             	if(s.equals("下降")){
        	    	    		             		chnl_huanbi ="-"+chnl_huanbi.substring(2);
        	    	    		             	}else{
        	    	    		             		chnl_huanbi =chnl_huanbi.substring(2);
        	    	    		             	}
        	    	    		             	if(s1.equals("下降")){
        	    	    		             		card_huanbi ="-"+card_huanbi.substring(2);
        	    	    		             	}else{
        	    	    		             		card_huanbi =card_huanbi.substring(2);
        	    	    		             	}
        	    	    		             	ykcrad_num =ykcrad_num.replace("，", "");
        	    	    		             	ykcrad_num =ykcrad_num.replace(",", "");
        	    	    		             	chnl_num =chnl_num.replace("，", "");
        	    	    		             	chnl_num =chnl_num.replace(",", "");
        	    	    		             	doCompare(ykcrad_num,card_percent,card_huanbi,chnl_num,chnl_percent, chnl_huanbi,
        	    	    		             			sheet,workbook,chnlname,chnldata,carddata, errorInfo,errorNum,f.getName());
    	    	    	    			}
    	    	    	    			if(flag==5){
    	    	    	    				//社会渠道
    	    	    	    				String data = dataList.get(26);
    	    	    	 		            String ykcrad_num = dataList.get(28);
    	    	    	 		            String card_percent = dataList.get(30);
    	    	    	 		            String card_huanbi = dataList.get(32);
    	    	    	 		            String chnl_num = dataList.get(34);
    	    	    	 		            String chnl_percent = dataList.get(36);
    	    	    	 		            String chnl_huanbi =dataList.get(38);
    	    	    	 		            String s1 =card_huanbi.substring(0,2);
	    	    	    	 		        String s =chnl_huanbi.substring(0,2);
	    	    	    	 		        card_percent =card_percent.replace("%", "");
 	    	    	 		             	chnl_percent =chnl_percent.replace("%", "");
	    	    	    	 		        if(s.equals("下降")){
	    	    	    	 		        	chnl_huanbi ="-"+chnl_huanbi.substring(2);
	    	    	    	 		        }else{
	    	    	    	 		        	chnl_huanbi =chnl_huanbi.substring(2);
	    	    	    	 		        }
	    	    	    	 		        if(chnl_huanbi.equals("N/A")){
	    	    	    	 		        	chnl_huanbi ="0";
	    	    	    	 		        }
	    	    	    	 		         if(s1.equals("下降")){
	    	    	    	 		        	card_huanbi ="-"+card_huanbi.substring(2);
		    	    	    	 		     }else{
		    	    	    	 		    	card_huanbi =card_huanbi.substring(2);
		    	    	    	 		     }
	    	    	    	 		        if(card_huanbi.equals("N/A")){
	    	    	    	 		        	card_huanbi ="0";
	    	    	    	 		        }
		    	    	    	 		     ykcrad_num =ykcrad_num.replace("，", "");
		    	    	    	 		     ykcrad_num =ykcrad_num.replace(",", "");
		    	    	    	 		     chnl_num =chnl_num.replace("，", "");
		    	    	    	 		     chnl_num =chnl_num.replace(",", "");
		    	    	    	 		     doCompare(ykcrad_num,card_percent,card_huanbi,chnl_num,chnl_percent, chnl_huanbi,
	    	    	    	 		        		sheet,workbook,chnlname,chnldata,carddata, errorInfo,errorNum,f.getName());
    	    	    	    			}
    	    	    	    			if(flag ==6){
    	    	    	    				//全渠道
    	    	    	    				 para =dataList.get(48);
    	    	    	    				totalnum = para.substring(para.indexOf("的")+1, para.indexOf("个"));
   	    	    	 		             	total_cardnum = para.substring(para.indexOf("养卡用户")+4, para.indexOf("个",para.indexOf("养卡用户")));
   	    	    	 		             	total_cardper = para.substring(para.indexOf("总量的")+3, para.indexOf("%"));
	   	    	    	 		            totalnum =totalnum.replace("，", "");
	   	    	    	 		            totalnum =totalnum.replace(",", "");
	   	    	    	 		            total_cardnum =total_cardnum.replace("，", "");
	   	    	    	 		            total_cardnum =total_cardnum.replace(",", "");
	   	    	    	 		            String para1 =dataList.get(51);
		   	    	    	 		        totalqudaonum = para1.substring(para1.indexOf("渠道")+2, para1.indexOf("个"));
			 	    	 		             total_percent = para1.substring(para1.indexOf("总量的")+3, para1.indexOf("%"));
			 	    	 		            totalqudaonum =totalqudaonum.replace("，", "");
			 	    	 		            totalqudaonum =totalqudaonum.replace(",", "");
			 	    	 		            doCompare(total_cardnum,total_cardper,null,totalqudaonum,total_percent, null,
		    	    	 		        		sheet,workbook,chnlname,chnldata,carddata, errorInfo,errorNum,f.getName());
    	    	    	    			}

    	    	    		        }
    	    	    	            is.close();
    	    	    	    	}
    	    	    	    }
    	    			}
    	    		}

    	    	}
    	    	//比对全公司
    	    	for(Map<String,Object> all:allList){
    	    		for (File f:filelist){
    	    	    	if (f.isFile() && f.getName().endsWith(".doc")&&f.getName().contains("全公司")){
    	    	    		System.out.println("比对审计报告..............全公司");
    	    	    		InputStream is = new FileInputStream(FILE_PATH_WORD+f.getName());
    	    	    		String totalnum =null,total_cardnum=null,total_cardper=null,totalqudaonum=null,total_percent=null;
    	    	    		List<String> dataList =getWorddata(FILE_PATH_WORD+f.getName());
    	    	    		if(dataList != null && dataList.size() > 0){
    	    	    			if(flag ==4){
    	    	    				//自有渠道
	    	    		        	String usernum = dataList.get(10);//共计
	    	    		        	String ykcrad_num = dataList.get(12);   //养卡号码数量
	    	    		            String card_percent = dataList.get(14);	//养卡号码数量占比
	    	    		            String card_huanbi = dataList.get(16);	//环比
	    	    		            String chnl_num = dataList.get(18);		//渠道数量
	    	    		            String chnl_percent = dataList.get(20); //渠道占比
	    	    		            String chnl_huanbi=dataList.get(22);	//渠道环比
	    	    		            String s1 =card_huanbi.substring(0,2);
	    	    		             	String s =chnl_huanbi.substring(0,2);
	    	    		             	card_percent =card_percent.replace("%", "");
	    	    		             	chnl_percent =chnl_percent.replace("%", "");
	    	    		             	if(s.equals("下降")){
	    	    		             		chnl_huanbi ="-"+chnl_huanbi.substring(2);
	    	    		             	}
	    	    		             	if(s1.equals("下降")){
	    	    		             		card_huanbi ="-"+card_huanbi.substring(2);
	    	    		             	}
	    	    		             	usernum =usernum.replace(",", "");
	    	    		             	ykcrad_num =ykcrad_num.replace(",", "");
	    	    		             	chnl_num =chnl_num.replace(",", "");
	    	    		             	doCompareAll(usernum,ykcrad_num,card_percent,chnl_num,chnl_percent,
	   	    	 		             			sheet,workbook,chnlname,all, errorInfo,errorNum,f.getName());
    	    	    			}
    	    	    			if(flag==5){
    	    	    				//社会渠道
    	    	    				String usernum = dataList.get(26);
    	    	 		            String ykcrad_num = dataList.get(28);
    	    	 		            String card_percent = dataList.get(30);
    	    	 		            String card_huanbi = dataList.get(32);
    	    	 		            String chnl_num = dataList.get(34);
    	    	 		            String chnl_percent = dataList.get(36);
    	    	 		            String chnl_huanbi =dataList.get(38);
    	    	 		            String s1 =card_huanbi.substring(0,2);
	    	    	 		        String s =chnl_huanbi.substring(0,2);
	    	    	 		        card_percent =card_percent.replace("%", "");
 	    	 		             	chnl_percent =chnl_percent.replace("%", "");
	    	    	 		        if(s.equals("下降")){
	    	    	 		        	chnl_huanbi ="-"+chnl_huanbi.substring(2);
	    	    	 		        }
	    	    	 		         if(s1.equals("下降")){
	    	    	 		        	card_huanbi ="-"+card_huanbi.substring(2);
    	    	    	 		     }
	    	    	 		        usernum =usernum.replace(",", "");
    	    	    	 		     ykcrad_num =ykcrad_num.replace(",", "");
    	    	    	 		     chnl_num =chnl_num.replace(",", "");
    	    	    	 		    doCompareAll(usernum,ykcrad_num,card_percent,chnl_num,chnl_percent,
   	    	 		             			sheet,workbook,chnlname,all, errorInfo,errorNum,f.getName());
    	    	    			}
    	    	    			if(flag ==6){
    	    	    				//全渠道
	    	    				 	para =dataList.get(48);
	    	    				 	totalnum = para.substring(para.indexOf("的")+1, para.indexOf("个"));
    	 		             		total_cardnum = para.substring(para.indexOf("养卡用户")+4, para.indexOf("个",para.indexOf("养卡用户")));
    	 		             		total_cardper = para.substring(para.indexOf("总量的")+3, para.indexOf("%"));
    	    	 		            totalnum =totalnum.replace(",", "");
    	    	 		            total_cardnum =total_cardnum.replace("，", "");
    	    	 		            total_cardnum =total_cardnum.replace(",", "");
    	    	 		            String para1 =dataList.get(51);
   	    	    	 		        totalqudaonum = para1.substring(para1.indexOf("渠道")+2, para1.indexOf("个"));
	 	    	 		             total_percent = para1.substring(para1.indexOf("总量的")+3, para1.indexOf("%"));
	 	    	 		            totalqudaonum =totalqudaonum.replace("，", "");
	 	    	 		            totalqudaonum =totalqudaonum.replace(",", "");
	 	    	 		          doCompareAll(totalnum,total_cardnum,total_cardper,totalqudaonum,total_percent,
 	    	 		             			sheet,workbook,chnlname,all, errorInfo,errorNum,f.getName());
    	    	    			}

    	    		        }
    	    	            is.close();
    	    	    	}
    	    	    }
    	    }
    	    	FileOutputStream outPath = new FileOutputStream(filepath);
    			workbook.write(outPath);
    			outPath.close();
         //输出比对结果入库-------------------------------------------------
    	    logger.error(">>>>>数据比对完成开始入库");
    	  	String result[]=errorInfo.toString().split(";");
    	  	logger.error(">>>>>数据比对完成开始入库errorInfo信息"+errorInfo);
    	  	//如果excel和word比对结果无异常
    	  	if(!result[0].equals("")){
    	  	List<DataCompare>  dataList=new ArrayList<DataCompare>(result.length);
    	  	DataCompare data=null;//此处未使用sql批量添加是应为 td数据库语法不支持批量添加
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
        			data.setExcelName("31省"+content[4]+"-"+audMonth);
        			data.setExcelValue(content[5]);
        			data.setCompareResult(content[6]);
        			data.setCreateDatetime(new Date());
        			data.setIsdel(0);
        			mybatisDao.add("DataCompareMapper.insertList", data);
    			dataList.add(data);
    		}
    	  	}else{
    	  		resultList="excel和word比对结果正常!";
    	  		return resultList;
    	  	}
    	   // createTxt(errorInfo.toString(), FILE_PATH_EXCEL+chnlname+".txt");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return resultList;
	}

	/**
	 * 全公司比对
	 * <pre>
	 * Desc
	 * @author issuser
	 * 2017-6-24 下午4:49:22
	 * </pre>
	 */
	public void doCompareAll(String usernum,String ykcrad_num,String card_percent,String chnl_num,
			String chnl_percent,Sheet sheet,XSSFWorkbook workbook,String chnlname,
			Map<String,Object> data,StringBuffer errorInfo,int errorNum,String wordName){
			String total_usr=data.get("total_usernum").toString();
			Long lnum =null;
			if(total_usr.indexOf("万") >=0){
				Double l =Double.valueOf(total_usr.substring(0,total_usr.indexOf("万")));
				BigDecimal a1 = new BigDecimal(Double.toString(l));
				BigDecimal b1 = new BigDecimal(Double.toString(10000));
				BigDecimal result1 = a1.multiply(b1);
			     lnum =Math.round(result1.doubleValue());
//				total_usr =lnum.toString();
			}
			Long i_user=Long.valueOf(usernum.trim());
			if(i_user -lnum >= 100  || i_user -lnum <-99 ){//添加互相判断
		 		/*errorInfo.append(errorNum+++"、"+chnlname+"_全公司_入网号码数量不相同").append("\r\n");
		 		sheet.getRow(6).getCell(3).setCellStyle(createStyle(workbook));*/
				errorInfo.append(errorNum+++"、"+"入网号码数量、"+wordName.substring(0,wordName.length()-4)+"、"+usernum+"、"+chnlname+"、"+total_usr+"、"+"全公司_入网号码数量不相同").append(";");
		 	}
		 	String total_card =data.get("total_cardnum").toString();
		 	if(total_card.indexOf("万") >=0){
		 		Double l =Double.valueOf(total_card.substring(0,total_card.indexOf("万")));
		 		BigDecimal a1 = new BigDecimal(Double.toString(l));
				BigDecimal b1 = new BigDecimal(Double.toString(10000));
				BigDecimal result1 = a1.multiply(b1);
			    lnum =Math.round(result1.doubleValue());
//			    total_card =lnum.toString();
			}
		 	Long i_card=Long.valueOf(ykcrad_num.trim());
		 	if(i_card -lnum >= 100  ||  i_card -lnum <-99){
		 	//if(i_card -lnum >= 100){
		 		/*errorInfo.append(errorNum+++"、"+chnlname+"_全公司_疑似养卡号码数量不相同").append("\r\n");
		 		sheet.getRow(7).getCell(3).setCellStyle(createStyle(workbook));*/
		 		errorInfo.append(errorNum+++"、"+"疑似养卡号码数量、"+wordName.substring(0,wordName.length()-4)+"、"+ykcrad_num+"、"+chnlname+"、"+total_card+"、"+"全公司_疑似养卡号码数量不相同").append(";");

		 	}
		 	String totalper =data.get("total_cardpercent").toString();
		 	if(totalper.indexOf("%") >=0){
		 		totalper =totalper.replace("%", "");
			}
		 	BigDecimal b1 =new BigDecimal(totalper);
			BigDecimal b2 =new BigDecimal(Double.toString(100));
			double totalper_d =b1.multiply(b2).doubleValue();
		 	double totalper_d_d2=Double.valueOf(card_percent.trim());
		 	if(totalper_d!= totalper_d_d2){
		 	//if(totalper_d != totalper_d_d2){
            	/*errorInfo.append(errorNum+++"、"+chnlname+"_全公司_疑似养卡号码数量占比不相同").append("\r\n");
            	sheet.getRow(8).getCell(3).setCellStyle(createStyle(workbook));*/
          //序号，字段名称，审计报告，审计报告值，排名汇总，排名汇总值，比对结果
            	errorInfo.append(errorNum+++"、"+"疑似养卡号码数量占比%、"+wordName.substring(0,wordName.length()-4)+"、"+card_percent+"%"+"、"+chnlname+"、"+totalper_d+"%"+"、"+"全公司_疑似养卡号码数量占比不相同").append(";");
            }
	        if(!data.get("total_chnlnum").equals(chnl_num.trim())){
	        	/*errorInfo.append(errorNum+++"、"+chnlname+"_全公司_涉及渠道数量不相同").append("\r\n");
	        	sheet.getRow(9).getCell(3).setCellStyle(createStyle(workbook));*/
	        	errorInfo.append(errorNum+++"、"+"涉及渠道数量、"+wordName.substring(0,wordName.length()-4)+"、"+chnl_num.trim()+"、"+chnlname+"、"+data.get("total_chnlnum")+"、"+"全公司_涉及渠道数量不相同").append(";");
	        }
	        String total_chnlper =data.get("total_chnlpercent").toString();
		 	if(total_chnlper.indexOf("%") >=0){
		 		total_chnlper =total_chnlper.replace("%", "");
			}
		 	BigDecimal b11 =new BigDecimal(total_chnlper);
			BigDecimal b21 =new BigDecimal(Double.toString(100));
			double total_chnlper_d =b11.multiply(b21).doubleValue();
		 	double total_chnlper_d2=Double.valueOf(chnl_percent.trim());
	        /*if(total_chnlper_d != total_chnlper_d2){
	        	errorInfo.append(errorNum+++"、"+chnlname+"_全公司_涉及渠道数量占比不相同").append("\r\n");
	        	sheet.getRow(10).getCell(3).setCellStyle(createStyle(workbook));
	        }*/
		    if(total_chnlper_d != total_chnlper_d2){
		        	errorInfo.append(errorNum+++"、"+"涉及渠道数量占比%、"+wordName.substring(0,wordName.length()-4)+"、"+total_chnlper_d2+"%"+"、"+chnlname+"、"+total_chnlper_d+"%"+"、"+"全公司_涉及渠道数量占比不相同").append(";");
		        }
	}
	/**
	 * 所有省分比对
	 * 进行excel和word数据比对
	 * <pre>
	 * Desc
	 * @author issuser
	 * 2017-6-24 下午4:49:39
	 * </pre>
	 * carddata {ykhuanbi=1.01, ykcardPercent=3.59, prvdName_card=新疆, ykcardNum=14592, row=17}
	 * chnldata {prvdName_chnl=新疆, chnlPercent=0.34, chnlHuanbi=0.05, chnlNum=197, row=58}
	 *                           14592          3.59                 1.01             197
	 */
	public void doCompare(String ykcrad_num,String card_percent,String card_huanbi,String chnl_num,
			String chnl_percent,String chnl_huanbi,Sheet sheet,XSSFWorkbook workbook,String chnlname,
			Map chnldata,Map carddata,StringBuffer errorInfo,int errorNum,String wordName){
	          int row_card =Integer.parseInt(carddata.get("row").toString());
	          logger.error(">>>>>开始进行数据比对");
	          int row_chnl =Integer.parseInt(chnldata.get("row").toString());
	          if(ykcrad_num != null &&!carddata.get("ykcardNum").equals("")&& !ykcrad_num.equals("")){
	        	  if(!carddata.get("ykcardNum").equals(ykcrad_num.trim())){
	                	/*errorInfo.append(errorNum+++"、"+chnlname+"_"+carddata.get("prvdName_card")+"_疑似养卡号码数量不相同").append("\r\n");
	                	sheet.getRow(row_card).getCell(3).setCellStyle(createStyle(workbook));*/
	        		  errorInfo.append(errorNum+++"、"+"疑似养卡号码数量、"+wordName.substring(0,wordName.length()-4)+"、"+ykcrad_num.trim()+"、"+chnlname+"、"+carddata.get("ykcardNum")+"、"+carddata.get("prvdName_card")+"_疑似养卡号码数量不相同").append(";");
	                }
	          }
	          if(card_percent!= null &&!carddata.get("ykcardPercent").equals("")&& !card_percent.equals("")){
	        	  double card_per=Double.valueOf(card_percent.trim());
	        	  if(Double.valueOf(carddata.get("ykcardPercent").toString()) != card_per){
		            	/*errorInfo.append(errorNum+++"、"+chnlname+"_"+carddata.get("prvdName_card")+"_疑似养卡号码数量占比不相同").append("\r\n");
		            	sheet.getRow(row_card).getCell(4).setCellStyle(createStyle(workbook));*/
	        		  errorInfo.append(errorNum+++"、"+"疑似养卡号码数量占比%、"+wordName.substring(0,wordName.length()-4)+"、"+card_per+"、"+chnlname+"、"+carddata.get("ykcardPercent").toString()+"、"+carddata.get("prvdName_card")+"_疑似养卡号码数量占比不相同").append(";");
		        }
	          }
	         if(card_huanbi != null &&!carddata.get("ykhuanbi").equals("")&&!card_huanbi.equals("")){
	        	 double card_hb=Double.valueOf(card_huanbi.trim());
	        	 if(Double.valueOf(carddata.get("ykhuanbi").toString()) != card_hb){
	             	/*errorInfo.append(errorNum+++"、"+chnlname+"_"+carddata.get("prvdName_card")+"_疑似养卡号码数量环比不相同").append("\r\n");
	             	sheet.getRow(row_card).getCell(5).setCellStyle(createStyle(workbook));*/
	        		 errorInfo.append(errorNum+++"、"+"疑似养卡号码数量环比%、"+wordName.substring(0,wordName.length()-4)+"、"+card_hb+"、"+chnlname+"、"+carddata.get("ykhuanbi").toString()+"、"+carddata.get("prvdName_card")+"_疑似养卡号码数量环比不相同").append(";");
	             }
	         }
	        if(chnl_num != null && !chnldata.get("chnlNum").equals("")&& !chnl_num.equals("")){
	        	 if(!chnldata.get("chnlNum").equals(chnl_num.trim())){
	             	/*errorInfo.append(errorNum+++"、"+chnlname+"_"+carddata.get("prvdName_card")+"_疑似养卡渠道数量不相同").append("\r\n");
	             	sheet.getRow(row_chnl).getCell(3).setCellStyle(createStyle(workbook));*/
	        		 errorInfo.append(errorNum+++"、"+"疑似养卡渠道数量、"+wordName.substring(0,wordName.length()-4)+"、"+chnl_num.trim()+"、"+chnlname+"、"+chnldata.get("chnlNum")+"、"+carddata.get("prvdName_card")+"_疑似养卡渠道数量不相同").append(";");
	             }
	        }
	        if(chnl_percent != null && !chnldata.get("chnlPercent").equals("")&& !chnl_percent.equals("")){
	        	double chnl_per =Double.valueOf(chnl_percent.trim());
	        	if(Double.valueOf(chnldata.get("chnlPercent").toString()) != chnl_per){
	            	/*errorInfo.append(errorNum+++"、"+chnlname+"_"+carddata.get("prvdName_card")+"_疑似养卡渠道数量占比不相同").append("\r\n");
	            	sheet.getRow(row_chnl).getCell(4).setCellStyle(createStyle(workbook));*/
	        		errorInfo.append(errorNum+++"、"+"疑似养卡渠道数量占比%、"+wordName.substring(0,wordName.length()-4)+"、"+chnl_per+"、"+chnlname+"、"+chnldata.get("chnlPercent").toString()+"、"+carddata.get("prvdName_card")+"_疑似养卡渠道数量占比不相同").append(";");
	            }
	        }
	        if(chnl_huanbi != null && !chnldata.get("chnlHuanbi").equals("")&& !chnl_huanbi.equals("")){
	        	double chnlhb=Double.valueOf(chnl_huanbi.trim());
	        	 if(Double.valueOf(chnldata.get("chnlHuanbi").toString())!=chnlhb){
	             	/*errorInfo.append(errorNum+++"、"+chnlname+"_"+carddata.get("prvdName_card")+"_疑似养卡渠道数量环比不相同").append("\r\n");
	             	sheet.getRow(row_chnl).getCell(5).setCellStyle(createStyle(workbook));*/
	        		 errorInfo.append(errorNum+++"、"+"疑似养卡渠道数量环比%、"+wordName.substring(0,wordName.length()-4)+"、"+chnlhb+"、"+chnlname+"、"+chnldata.get("chnlHuanbi").toString()+"、"+carddata.get("prvdName_card")+"_疑似养卡渠道数量环比不相同").append(";");
	             }
	        }
	        logger.error(">>>>>数据比对结束");

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


	/**
	 * 在线数据比对
	 * @return
	 * @throws Exception
	 */
	//@Transactional(rollbackFor = Exception.class)
	public JSONObject onlineDataCompare(String subjectId, String audMonth) throws Exception{
		String lostfiles []=null;

		String ftpServer = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpServer"));
		String ftpPort = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpPort"));
		String name = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpUser"));
		String password = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpPass"));

		//新建web下载路径
		String webExcelDir = StringUtils.trimToEmpty(propertyUtil.getPropValue("dataCompareDirExcel"));
		String webWordDir = StringUtils.trimToEmpty(propertyUtil.getPropValue("dataCompareDirWord"));
		File downPathExcel=new File(webExcelDir);
		File downPathWord=new File(webWordDir);
		if(!downPathExcel.exists()){
			downPathExcel.mkdirs();
		}
		if(!downPathWord.exists()){
			downPathWord.mkdirs();
		}
		//如果目录存在，则删除历史文件
	   for(File fe:downPathExcel.listFiles()){
	            fe.delete();
	        }
	     for(File fe:downPathWord.listFiles()){
	            fe.delete();
	     }
	    JSONObject json=new JSONObject();
		//返回获取连接ftp后文件是否缺失
	    String lostFile=testInitClient(ftpServer, ftpPort, name, password,subjectId,audMonth,downPathExcel,downPathWord);
        System.err.println(lostFile);
        logger.error(">>>>>compare success......."+lostFile);
        //返回结果定义在json,以状态吗的形式返回
        if(lostFile.contains("服务器上未找到")){
        	json.put("1", new String[]{lostFile});
         //	resultList=lostFile;
        }else{
        	if(!lostFile.equals("")){
        		lostfiles=lostFile.split(";");
        	}

        	 //清空上次比对结果
            Map<String, Object> params = new HashMap<String, Object>();
            Integer countResult=(Integer)mybatisDao.get("DataCompareMapper.countDataCompares", params);
            logger.error(">>>>>countResult......."+countResult);
            if(countResult!=0){
            	updateDataIsdel();
            }
            if(Integer.parseInt(subjectId)==2){//渠道养卡返回值
            //开始比对核心文件数据
            String compareResult=match(downPathExcel,downPathWord,audMonth);
            logger.error(">>>>>compareResult success......."+compareResult);
            //比对结束清楚web文件
            for(File fe:downPathExcel.listFiles()){
                fe.delete();
            }
            for(File fe:downPathWord.listFiles()){
                fe.delete();
            }

            if(compareResult.equals("")  && lostFile.equals("")){
            	//resultList="文件未缺少,数据有异常;";
            	json.put("3", new String[]{"文件正常,数据异常"});

            }else if(!compareResult.equals("") &&  lostFile.equals("")){
            	if(compareResult.contains("自有") && compareResult.contains("社会") && compareResult.contains("全渠道") ){
            		//resultList="文件未缺少,数据正常;";
            		json.put("0", new String[]{"文件和数据比对都正常"});
            		addDatabase();
            	}else{
            		json.put("3", new String[]{"文件正常,数据异常"});
            	}
            }else if(!compareResult.equals("") && !lostFile.equals("")){
            	if(compareResult.contains("自有") && compareResult.contains("社会") && compareResult.contains("全渠道") ){
            		//resultList="文件缺少,数据正常;";
            		json.put("2", lostfiles);
            	}else{
            		json.put("4", lostfiles);
            	}
            }else if(compareResult.equals("")  && !lostFile.equals("")){
            	    json.put("4", lostfiles);
            }
            }
          //终端套利
            if(Integer.parseInt(subjectId)==3){
                //开始比对核心文件数据
            	logger.error(">>>>>success3.......");
                String compareResult=compareDataZdtlService.match(downPathExcel,downPathWord,audMonth);
                logger.error(">>>>>compareResult success......."+compareResult);
                //比对结束清楚web文件
               for(File fe:downPathExcel.listFiles()){
                    fe.delete();
                }
                for(File fe:downPathWord.listFiles()){
                    fe.delete();
                }
                if(compareResult.equals("")  && lostFile.equals("")){
                	//resultList="文件未缺少,数据有异常;";
                	json.put("3", new String[]{"文件正常,数据异常"});

                }else if(!compareResult.equals("") &&  lostFile.equals("")){
                		json.put("0", new String[]{"文件和数据比对都正常"});
                		addDatabase();
                }else if(!compareResult.equals("") && !lostFile.equals("")){
                		//resultList="文件缺少,数据正常;";
                		json.put("2", lostfiles);
                }else if(compareResult.equals("")  && !lostFile.equals("")){
                	    json.put("4", lostfiles);
                }
                }

            //电子券专题
            if(Integer.parseInt(subjectId)==6){
                //开始比对核心文件数据
            	logger.error(">>>>>success3.......");
              //  String compareResult=compareDataZdtlService.match(downPathExcel.getPath(),downPathWord.getPath(),audMonth);
                String compareResult=compareDataDzqService.match(downPathExcel,downPathWord,audMonth);
               // String compareResult="";
                logger.error(">>>>>compareResult success......."+compareResult);
                //比对结束清楚web文件
                for(File fe:downPathExcel.listFiles()){
                    fe.delete();
                }
                for(File fe:downPathWord.listFiles()){
                    fe.delete();
                }
                if(compareResult.equals("")  && lostFile.equals("")){
                	//resultList="文件未缺少,数据有异常;";
                	json.put("3", new String[]{"文件正常,数据异常"});

                }else if(!compareResult.equals("") &&  lostFile.equals("")){
                		json.put("0", new String[]{"文件和数据比对都正常"});
                		addDatabase();
                }else if(!compareResult.equals("") && !lostFile.equals("")){
                		//resultList="文件缺少,数据正常;";
                		json.put("2", lostfiles);
                }else if(compareResult.equals("")  && !lostFile.equals("")){
                	    //文件数据都异常
                	    json.put("4", lostfiles);
                }
                }

            //有价卡专题
            if(Integer.parseInt(subjectId)==1){
                //开始比对核心文件数据
            	logger.error(">>>>>success3.......");
                String compareResult=compareDataYjkService.match(downPathExcel,downPathWord,audMonth);
                logger.error(">>>>>compareResult success......."+compareResult);
                //比对结束清楚web文件
                for(File fe:downPathExcel.listFiles()){
                    fe.delete();
                }
                for(File fe:downPathWord.listFiles()){
                    fe.delete();
                }
                if(compareResult.equals("")  && lostFile.equals("")){
                	//resultList="文件未缺少,数据有异常;";
                	json.put("3", new String[]{"文件正常,数据异常"});

                }else if(!compareResult.equals("") &&  lostFile.equals("")){
                	    //本行等有价卡违规排名文件格式修改之后打开注释即可，暂不做比较
                	    //if(compareResult.contains("有价卡赠送") && compareResult.contains("有价卡业财") && compareResult.contains("有价卡排名汇总") &&  lostFile.equals("")){
                	    if(compareResult.contains("有价卡赠送") && compareResult.contains("有价卡业财") &&  lostFile.equals("")){
                	    	json.put("0", new String[]{"文件和数据比对都正常"});
                	    	addDatabase();
                	    }else{
                	    	json.put("3", new String[]{"文件正常,数据异常"});
                	    }
                }else if(!compareResult.equals("") && !lostFile.equals("")){
                		//resultList="文件缺少,数据正常;";  同上暂不做不比较
                		//if(compareResult.contains("有价卡赠送") && compareResult.contains("有价卡业财") && compareResult.contains("有价卡排名汇总") &&  !lostFile.equals("")){
                		if(compareResult.contains("有价卡赠送") && compareResult.contains("有价卡业财")  &&  !lostFile.equals("")){
                	    	json.put("2", lostfiles);
                	    }else{
                	    	json.put("4", lostfiles);
                	    }
                }else if(compareResult.equals("")  && !lostFile.equals("")){
                	    //文件数据都异常
                	    json.put("4", lostfiles);
                }
                }
        }

		System.out.println("比对结束........");
		logger.error(">>>>>比对结束........"+json);
		return json;
	}


	/**
	 * 获取ftp服务器数据，并进行解压，比对数据丢失
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param subjectId
	 * @param audMonth
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws FTPIllegalReplyException
	 * @throws FTPException
	 */
	public String testInitClient(String host, String port, String username, String password,String subjectId, String audMonth,
			File webExcelDir,File webWordDir) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		StringBuffer sign=new StringBuffer();
		int status=0;//判断文件是否存在状态
		try {
			client = new FTPClient();
			logger.error(">>>>>start connect.......");
			System.out.println(">>>>>start connect.......");
			client.connect(host, Integer.valueOf(port));
			logger.error(">>>>>connect success.......");
			logger.error(">>>>>start login .......");
			client.login(username, password);
			logger.error(">>>>>login success.......");
			System.out.println(">>>>>login success.......");
			logger.error(">>>>>current directory is.....[" + client.currentDirectory() + "]");
			//------------------
			System.err.println(client.currentDirectory());
			client.setType(FTPClient.TYPE_BINARY);
			List<String> ykExecllistloacl=null;
			List<String> ykExecllistOnline=null;
			List<String> ykWordlistloacl=null;
			List<String> ykWordlistOnline=null;
			List<Map<String, Object>> listmap=getProvinceCode();//获取所有的省份和代码

			if(subjectId.equals("2")){//渠道养卡
				//比对排名汇总excel
				ykExecllistloacl=new ArrayList<String>();
				ykExecllistloacl.add("31省社会渠道汇总-"+audMonth+".xlsx");
				ykExecllistloacl.add("31省自有渠道汇总-"+audMonth+".xlsx");
				ykExecllistloacl.add("31省全渠道汇总-"+audMonth+".xlsx");

				ykExecllistOnline=new ArrayList<String>();
				logger.error(">>>>>start current directory is.....[" + client.currentDirectory() + "]");
				client.changeDirectory("/");
				logger.error(">>>>>start current directory is.....[" + client.currentDirectory() + "]");

				try
				  {
					client.changeDirectory("hp/data3/"+audMonth+"/"+subjectId+"/"+Integer.parseInt(subjectId)*1000);//进入ftp文件excel路径
				  } catch (Exception e)
				  {
					  return "服务器上未找到渠道养卡审计月"+audMonth+"排名汇总文件路径,可能不存在:hp/data3/"+audMonth+"/"+subjectId+"/"+Integer.parseInt(subjectId)*1000;
				  }

				logger.error(">>>>>end current directory is.....[" + client.currentDirectory() + "]");
				System.err.println(client.currentDirectory());
				logger.error(">>>>>new File current directory is.....[" + client.currentDirectory() + "]");
				//File chaExcel = new File("D:\\"+client.currentDirectory());
				//File chaExcel = new File("D:\\apache-tomcat-6.0.37-windows-x64\\apache-tomcat-6.0.37\\webapps\\"+client.currentDirectory());
				String[] files = client.listNames();
				//if (chaExcel.isDirectory()) {
					//File[] files = chaExcel.listFiles();
					if(files.length==0){
						return "服务器上未找到渠道养卡审计月"+audMonth+"排名汇总压缩文件";
					}else{//服务器相同路径存在2个压缩包
						for (int j = 0; j < files.length; j++) {
							if(files[j].contains("排名汇总_"+audMonth+"_"+"渠道养卡"+".zip")){
								status=1;
							}
						}
						if(status!=1){
							return "服务器上未找到渠道养卡审计月:"+audMonth+"排名汇总文件";
						}
						String localFilePath=webExcelDir.getPath();
						for (int i = 0; i < files.length; i++) {
								//判断是否存在对应的排名汇总压缩包
								if(files[i].contains("排名汇总_"+audMonth+"_"+"渠道养卡"+".zip")){
								System.err.println(files[i]);
								client.download(files[i], new File(localFilePath+File.separator+files[i]));
								ZipFileUtil.unZip(localFilePath+"/"+files[i], localFilePath);
								}
						}
						for(File f:new File(localFilePath).listFiles()){
							ykExecllistOnline.add(f.getName());
						}
						//比对excel文件缺失
						ykExecllistloacl.removeAll(ykExecllistOnline);
						if(!ykExecllistloacl.isEmpty()){
						for(String lostExcel:ykExecllistloacl){
							sign.append(lostExcel+"文件缺失;");
						}
						}
					}

				//比对和下载word文件
				client.changeDirectory("/");
				try
				  {
					client.changeDirectory("hp/data3/v2/"+audMonth+"/"+subjectId+"/"+2002+"/"+10000);//进入ftp文件word路径
				  } catch (Exception e)
				  {
					  return "服务器上未找到渠道养卡审计月"+audMonth+"审计报告文件路径,可能不存在:hp/data3/v2/"+audMonth+"/"+subjectId+"/"+2002+"/"+10000;
				  }
				System.err.println(client.currentDirectory());

				//提交时记得修改本地模拟路径
				//File chaWord = new File(client.currentDirectory());
				String[] filew = client.listNames();//此处应使用ftp的遍历方法，本地遍历的不行
				//File chaWord = new File("D:\\apache-tomcat-6.0.37-windows-x64\\apache-tomcat-6.0.37\\webapps\\"+client.currentDirectory());
				//File chaWord = new File("D:\\"+client.currentDirectory());
				//if (chaWord.isDirectory()) {
					//File[] filew = chaWord.listFiles();
					if(filew.length==0){
						return "服务器上未找到渠道养卡审计月"+audMonth+"审计报告压缩文件";
					}else{

						for (int  j= 0; j < filew.length; j++) {
							if(filew[j].contains(audMonth+"_"+"渠道养卡审计报告"+".zip")){
								status=2;
							}
						}
						if(status!=2){
							return "服务器上未找到渠道养卡审计月:"+audMonth+"审计报告文件";
						}
						String localFilePath=webWordDir.getPath();
						for (int i = 0; i < filew.length; i++) {
								//判断是否存在对应的渠道养卡审计报告压缩包
								if(filew[i].contains(audMonth+"_"+"渠道养卡审计报告"+".zip")){
								System.err.println(filew[i]);
								client.download(filew[i], new File(localFilePath+File.separator+filew[i]));
								ZipFileUtil.unZip(localFilePath+"/"+filew[i], localFilePath);
								}
						}
						//渠道养卡审计报告
						ykWordlistloacl=new ArrayList<String>();
						ykWordlistOnline=new ArrayList<String>();
						for(Map<String, Object> map:listmap){
							ykWordlistloacl.add(map.get("short_name").toString()+"_"+audMonth+"_"+"渠道养卡审计报告.doc");
						}
						for(File f:new File(localFilePath).listFiles()){
							ykWordlistOnline.add(f.getName());
						}
						//比对word文件缺失
						ykWordlistloacl.removeAll(ykWordlistOnline);
						if(!ykWordlistloacl.isEmpty()){
						for(String lostWord:ykWordlistloacl){
							sign.append(lostWord+"文件缺失;");
						}
						}
					}
				//}
			System.err.println(client.currentDirectory());
		 }

//-------------------------------------------------------------------------------------------------------------
			//终端套利专题  比对文件缺失和下载到web服务器
			if(subjectId.equals("3")){
				//社会渠道终端套利排名汇总excel-201708
				ykExecllistloacl=new ArrayList<String>();
				ykExecllistloacl.add("社会渠道终端套利排名汇总-"+audMonth+".xlsx");

				ykExecllistOnline=new ArrayList<String>();
				client.changeDirectory("/");
				  try
				  {
					  client.changeDirectory("hp/data3/"+audMonth+"/"+subjectId+"/"+Integer.parseInt(subjectId)*1000);//进入ftp文件excel路径
				  } catch (Exception e)
				  {
					  return "服务器上未找到终端套利审计月"+audMonth+"排名汇总文件路径,可能不存在:hp/data3/"+audMonth+"/"+subjectId+"/"+Integer.parseInt(subjectId)*1000;
				  }
				//client.changeDirectory("hp/data3/"+audMonth+"/"+subjectId+"/"+Integer.parseInt(subjectId)*1000);//进入ftp文件excel路径
				System.err.println(client.currentDirectory());
//提交时记得修改本地模拟路径
				//File terminalExcel = new File(client.currentDirectory());
				String[] files = client.listNames();
			//	File terminalExcel = new File("D:\\"+client.currentDirectory());
				//File terminalExcel = new File("D:\\apache-tomcat-6.0.37-windows-x64\\apache-tomcat-6.0.37\\webapps\\"+client.currentDirectory());
				//if (terminalExcel.isDirectory()) {
					//File[] files = terminalExcel.listFiles();
					if(files.length==0){
						return "服务器上未找到终端套利审计月"+audMonth+"排名汇总压缩文件";
					}else{
						for (int j = 0; j < files.length; j++) {
							if(files[j].contains("排名汇总_"+audMonth+"_"+"社会渠道终端异常销售、套利"+".zip")){
								status=3;
							}
						}
						if(status!=3){
							return "服务器上未找到终端套利审计月:"+audMonth+"排名汇总文件";
						}

						String localFilePath=webExcelDir.getPath();
						for (int i = 0; i < files.length; i++) {
							//if (files[i].isFile()) {
								//判断是否存在对应的排名汇总压缩包
								if(files[i].contains("排名汇总_"+audMonth+"_"+"社会渠道终端异常销售、套利"+".zip")){
								System.err.println(files[i]);
								client.download(files[i], new File(localFilePath+File.separator+files[i]));
								ZipFileUtil.unZip(localFilePath+"/"+files[i], localFilePath);
								}
							//}
						for(File f:new File(localFilePath).listFiles()){
							ykExecllistOnline.add(f.getName());
						}
						//比对excel文件缺失
						ykExecllistloacl.removeAll(ykExecllistOnline);
						if(!ykExecllistloacl.isEmpty()){
						for(String lostExcel:ykExecllistloacl){
							sign.append(lostExcel+"文件缺失;");
						}
						}
					}
				}
				//}
				//终端套利专题  比对和下载word文件
				client.changeDirectory("/");
				 try
				  {
					  client.changeDirectory("hp/data3/v2/"+audMonth+"/"+subjectId+"/"+Integer.parseInt(subjectId)*1000+"/"+10000);//进入ftp文件word路径
				  } catch (Exception e)
				  {
					  return "服务器上未找到终端套利审计月"+audMonth+"审计报告文件路径,可能不存在:hp/data3/v2/"+audMonth+"/"+subjectId+"/"+Integer.parseInt(subjectId)*1000+"/"+10000;
				  }
				  System.err.println(client.currentDirectory());
//提交时记得修改本地模拟路径
				//File terminalWord = new File(client.currentDirectory());
				String[] filezd = client.listNames();
				//File terminalWord = new File("D:\\"+client.currentDirectory());
				//File terminalWord = new File("D:\\apache-tomcat-6.0.37-windows-x64\\apache-tomcat-6.0.37\\webapps\\"+client.currentDirectory());
				//if (terminalWord.isDirectory()) {
				//	File[] filezd = terminalWord.listFiles();
					if(filezd.length==0){
						return "服务器上未找到终端套利审计月"+audMonth+"审计报告压缩文件";
					}else{
						for (int j = 0; j < filezd.length; j++) {
							if(filezd[j].contains("全公司"+"_"+audMonth+"_"+"社会渠道终端异常销售、套利审计报告"+".zip")){
								status=4;
							}
						}
						if(status!=4){
							return "服务器上未找到终端套利审计月:"+audMonth+"审计报告文件";
						}
						String localFilePath=webWordDir.getPath();
						for (int j = 0; j < filezd.length; j++) {
							//if (filezd[j].isFile()) {
								//判断是否存在对应的渠道养卡审计报告压缩包  格式 :全公司_201708_社会渠道终端异常销售、套利审计报告
								if(filezd[j].contains("全公司"+"_"+audMonth+"_"+"社会渠道终端异常销售、套利审计报告"+".zip")){
								System.err.println(filezd[j]);
								client.download(filezd[j], new File(localFilePath+File.separator+filezd[j]));
								ZipFileUtil.unZip(localFilePath+"/"+filezd[j], localFilePath);
								}
							//}
						}
						//渠道养卡审计报告
						ykWordlistloacl=new ArrayList<String>();
						ykWordlistOnline=new ArrayList<String>();
						for(Map<String, Object> map:listmap){
							//终端格式:终端异常销售、套利审计报告  全公司_201708_社会渠道终端异常销售、套利审计报告
							ykWordlistloacl.add(map.get("short_name").toString()+"_"+audMonth+"_"+"社会渠道终端异常销售、套利审计报告.doc");
						}
						for(File f:new File(localFilePath).listFiles()){
							ykWordlistOnline.add(f.getName());
						}
						//比对word文件缺失
						ykWordlistloacl.removeAll(ykWordlistOnline);
						if(!ykWordlistloacl.isEmpty()){
						for(String lostWord:ykWordlistloacl){
							sign.append(lostWord+"文件缺失;");
						}
						}
					}
			//	}
			System.err.println(client.currentDirectory());
			}

//-------------------------------------------------------------------------------------------------------------
			//电子券专题6  比对文件缺失和下载到web服务器
			if(subjectId.equals("6")){
				//电子券违规管理排名汇总全国_201804.xlsx
				ykExecllistloacl=new ArrayList<String>();
				ykExecllistloacl.add("电子券违规管理排名汇总全国_"+audMonth+".xlsx");
				ykExecllistOnline=new ArrayList<String>();
				client.changeDirectory("/");
				  try
				  {
					  client.changeDirectory("hp/data3/"+audMonth+"/"+subjectId+"/"+Integer.parseInt(subjectId)*1000);//进入ftp文件excel路径
				  } catch (Exception e)
				  {
					  return "服务器上未找到电子券违规审计月"+audMonth+"排名汇总文件路径,可能不存在:hp/data3/"+audMonth+"/"+subjectId+"/"+Integer.parseInt(subjectId)*1000;
				  }
				  System.err.println(client.currentDirectory());
				  String[] files = client.listNames();//排名汇总_201804_电子券管理违规.zip
				  if(files.length==0){
						return "服务器上未找到电子券违规审计月"+audMonth+"排名汇总压缩文件";
				  }else{
						for (int j = 0; j < files.length; j++) {
							if(files[j].contains("排名汇总_"+audMonth+"_"+"电子券管理违规"+".zip")){
								status=5;
							}
						}
						if(status!=5){
							return "服务器上未找到电子券管理审计月:"+audMonth+"排名汇总文件";
						}

						String localFilePath=webExcelDir.getPath();
						for (int i = 0; i < files.length; i++) {
							//if (files[i].isFile()) {
								//判断是否存在对应的排名汇总压缩包
								if(files[i].contains("排名汇总_"+audMonth+"_"+"电子券管理违规"+".zip")){
								System.err.println(files[i]);
								client.download(files[i], new File(localFilePath+File.separator+files[i]));
								ZipFileUtil.unZip(localFilePath+"/"+files[i], localFilePath);
								}
							//}
						System.err.println(localFilePath);
						for(File f:new File(localFilePath).listFiles()){
							ykExecllistOnline.add(f.getName());
						}
						//比对excel文件缺失
						ykExecllistloacl.removeAll(ykExecllistOnline);
						if(!ykExecllistloacl.isEmpty()){
						for(String lostExcel:ykExecllistloacl){
							sign.append(lostExcel+"文件缺失;");
						}
						}
					}
				}
				//}
				//电子券专题  比对和下载word文件
				client.changeDirectory("/");
				 try
				  {
					  client.changeDirectory("hp/data3/v2/"+audMonth+"/"+subjectId+"/"+Integer.parseInt(subjectId)*1000+"/"+10000);//进入ftp文件word路径
				  } catch (Exception e)
				  {
					  return "服务器上未找到电子券管理审计月"+audMonth+"审计报告文件路径,可能不存在:hp/data3/v2/"+audMonth+"/"+subjectId+"/"+Integer.parseInt(subjectId)*1000+"/"+10000;
				  }
				  System.err.println(client.currentDirectory());
				//File terminalWord = new File(client.currentDirectory());
				String[] filezd = client.listNames();
				//File terminalWord = new File("D:\\"+client.currentDirectory());
				//File terminalWord = new File("D:\\apache-tomcat-6.0.37-windows-x64\\apache-tomcat-6.0.37\\webapps\\"+client.currentDirectory());
				//if (terminalWord.isDirectory()) {
				//	File[] filezd = terminalWord.listFiles();
					if(filezd.length==0){
						return "服务器上未找到电子券管理审计月"+audMonth+"审计报告文件压缩文件";
					}else{
						for (int j = 0; j < filezd.length; j++) {//全公司_201804_电子券管理违规审计报告.doc
							if(filezd[j].contains("全公司"+"_"+audMonth+"_"+"电子券管理违规审计报告"+".zip")){
								status=6;
							}
						}
						if(status!=6){
							return "服务器上未找到电子券管理审计月:"+audMonth+"审计报告文件";
						}
						String localFilePath=webWordDir.getPath();
						for (int j = 0; j < filezd.length; j++) {
							//if (filezd[j].isFile()) {
								//判断是否存在对应的渠道养卡审计报告压缩包  格式 :全公司_201804_电子券管理违规审计报告.doc
								if(filezd[j].contains("全公司"+"_"+audMonth+"_"+"电子券管理违规审计报告"+".zip")){
								System.err.println(filezd[j]);
								client.download(filezd[j], new File(localFilePath+File.separator+filezd[j]));
								ZipFileUtil.unZip(localFilePath+"/"+filezd[j], localFilePath);
								}
							//}
						}
						//电子券管理审计报告
						ykWordlistloacl=new ArrayList<String>();
						ykWordlistOnline=new ArrayList<String>();
						for(Map<String, Object> map:listmap){
							//终端格式:终端异常销售、套利审计报告  全公司_201708_社会渠道终端异常销售、套利审计报告
							ykWordlistloacl.add(map.get("short_name").toString()+"_"+audMonth+"_"+"电子券管理违规审计报告.doc");
						}
						for(File f:new File(localFilePath).listFiles()){
							ykWordlistOnline.add(f.getName());
						}
						//比对word文件缺失
						ykWordlistloacl.removeAll(ykWordlistOnline);
						if(!ykWordlistloacl.isEmpty()){
						for(String lostWord:ykWordlistloacl){
							sign.append(lostWord+"文件缺失;");
						}
						}
					}
			//	}
			System.err.println(client.currentDirectory());
			}

//-------------------------------------------------------------------------------------------------------------
			//有价卡管理违规 专题1  比对文件缺失和下载到web服务器
			if(subjectId.equals("1")){
				//电子券违规管理排名汇总全国_201804.xlsx
				ykExecllistloacl=new ArrayList<String>();

				//有价卡违规排名-201804.xlsx  有价卡业财数据对比汇总情况-201802.xlsx   赠送有价卡异省充值汇总情况-201804.xlsx
				ykExecllistloacl.add("有价卡违规排名-"+audMonth+".xlsx");
				//ykExecllistloacl.add("有价卡业财数据对比汇总情况-"+audMonth+".xlsx");
				ykExecllistloacl.add("赠送有价卡异省充值汇总情况-"+audMonth+".xlsx");
				ykExecllistOnline=new ArrayList<String>();
				client.changeDirectory("/");
				  try
				  {
					  client.changeDirectory("hp/data3/"+audMonth+"/"+subjectId+"/"+Integer.parseInt(subjectId)*1000);//进入ftp文件excel路径
				  } catch (Exception e)
				  {
					  return "服务器上未找到有价卡违规审计月"+audMonth+"排名汇总文件路径,可能不存在:hp/data3/"+audMonth+"/"+subjectId+"/"+Integer.parseInt(subjectId)*1000;
				  }
				  System.err.println(client.currentDirectory());
				  String[] files = client.listNames();////排名汇总_201804_有价卡违规管理 .zip
				  if(files.length==0){
						return "服务器上未找到有价卡违规审计月"+audMonth+"排名汇总压缩文件";
				  }else{
						for (int j = 0; j < files.length; j++) {
							if(files[j].contains("排名汇总_"+audMonth+"_"+"有价卡违规管理"+".zip") || files[j].contains("排名汇总_"+audMonth+"_"+"有价卡违规管理 "+".zip")){
								status=7;
							}
						}
						if(status!=7){
							return "服务器上未找到有价卡管理审计月:"+audMonth+"排名汇总文件";
						}

						String localFilePath=webExcelDir.getPath();
						for (int i = 0; i < files.length; i++) {
							//if (files[i].isFile()) {
								//判断是否存在对应的排名汇总压缩包
								if(files[i].contains("排名汇总_"+audMonth+"_"+"有价卡违规管理"+".zip") || files[i].contains("排名汇总_"+audMonth+"_"+"有价卡违规管理 "+".zip")){
								System.err.println(files[i]);
								client.download(files[i], new File(localFilePath+File.separator+files[i]));
								ZipFileUtil.unZip(localFilePath+"/"+files[i], localFilePath);
								}
							//}
						for(File f:new File(localFilePath).listFiles()){
							ykExecllistOnline.add(f.getName());
						}
						//比对excel文件缺失
						ykExecllistloacl.removeAll(ykExecllistOnline);
						if(!ykExecllistloacl.isEmpty()){
						for(String lostExcel:ykExecllistloacl){
							sign.append(lostExcel+"文件缺失;");
						}
						}
					}
				}
				//}
				//有价卡专题  比对和下载word文件
				client.changeDirectory("/");
				System.err.println(client.currentDirectory());
				 try
				  {
					  client.changeDirectory("hp/data3/v2/"+audMonth+"/"+subjectId+"/"+Integer.parseInt(subjectId)*1000+"/"+10000);//进入ftp文件word路径
				  } catch (Exception e)
				  {
					  return "服务器上未找到有价卡管理审计月"+audMonth+"审计报告文件路径,可能不存在:hp/data3/v2/"+audMonth+"/"+subjectId+"/"+Integer.parseInt(subjectId)*1000+"/"+10000;
				  }
				  System.err.println(client.currentDirectory());
				String[] filezd = client.listNames();
					if(filezd.length==0){
						return "服务器上未找到有价卡管理审计月"+audMonth+"审计报告文件压缩文件";
					}else{
						for (int j = 0; j < filezd.length; j++) {//全公司_201804_有价卡违规管理审计报告
							if(filezd[j].contains("全公司"+"_"+audMonth+"_"+"有价卡违规管理审计报告"+".zip")){
								status=8;
							}
						}
						if(status!=8){
							return "服务器上未找到有价卡管理审计月:"+audMonth+"审计报告文件";
						}
						String localFilePath=webWordDir.getPath();
						for (int j = 0; j < filezd.length; j++) {
							//if (filezd[j].isFile()) {
								//判断是否存在对应的渠道养卡审计报告压缩包  格式 :全公司_201804_电子券管理违规审计报告.doc
								if(filezd[j].contains("全公司"+"_"+audMonth+"_"+"有价卡违规管理审计报告"+".zip")){
								System.err.println(filezd[j]);
								client.download(filezd[j], new File(localFilePath+File.separator+filezd[j]));
								ZipFileUtil.unZip(localFilePath+"/"+filezd[j], localFilePath);
								}
							//}
						}
						//电子券管理审计报告
						ykWordlistloacl=new ArrayList<String>();
						ykWordlistOnline=new ArrayList<String>();
						for(Map<String, Object> map:listmap){
							//有价卡格式:江苏_201804_有价卡违规管理审计报告
							ykWordlistloacl.add(map.get("short_name").toString()+"_"+audMonth+"_"+"有价卡违规管理审计报告.docx");
						}
						for(File f:new File(localFilePath).listFiles()){
							ykWordlistOnline.add(f.getName());
						}
						//比对word文件缺失
						ykWordlistloacl.removeAll(ykWordlistOnline);
						if(!ykWordlistloacl.isEmpty()){
						for(String lostWord:ykWordlistloacl){
							sign.append(lostWord+"文件缺失;");
						}
						}
					}
			//	}
			System.err.println(client.currentDirectory());
			}

		} catch (Exception e) {
			logger.error("build ftp contection exception", e);
			throw new RuntimeException("build ftp contection exception", e);
		}
		return sign.toString();
	}


}