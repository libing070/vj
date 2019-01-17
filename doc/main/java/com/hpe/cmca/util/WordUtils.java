package com.hpe.cmca.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.hpe.cmca.common.CustomXWPFDocument;






/**
 * 适用于word 2007
 * poi 版本 3.7
 */
public class WordUtils {

    protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 根据指定的参数值、模板，生成 word 文档
	 * @param param 需要替换的变量
	 * @param template 模板
	 */
	public  CustomXWPFDocument generateWord(Map<String, Object> param, String template) {
		CustomXWPFDocument doc = null;
		try {
//			String tmppath =WordUtils.class.getClassLoader().getResource("").getPath()+"wordTemplate";
//			logger.error(tmppath);
//			OPCPackage pack = POIXMLDocument.openPackage(tmppath+"/"+template);
////			OPCPackage pack = POIXMLDocument.openPackage(template);
//			doc = new CustomXWPFDocument(pack);
//		    	logger.error(template);
//		    	InputStream    in = WordUtils.class.getResourceAsStream("wordTemplate/"+template);
//		    	logger.error(in);
		    	
		    	ClassLoader classloader =Thread.currentThread().getContextClassLoader();
		        InputStream is =classloader.getResourceAsStream("wordTemplate/"+template);
		        logger.error(is);
		        
		        doc=new CustomXWPFDocument(is);
		    	logger.error(doc);
		    	
//
//		        CustomXWPFDocument doc1=new CustomXWPFDocument(POIXMLDocument.openPackage(path2.toString().substring(4, path2.toString().length())));
//		        logger.error(doc1);
//		    	
//		    	URL path1 =classloader.getResource(template);
//		    	logger.error(path1.toString());
			if (param != null && param.size() > 0) {
				
				//处理段落
				List<XWPFParagraph> paragraphList = doc.getParagraphs();
				processParagraphs(paragraphList, param, doc);
				
				//处理表格
				Iterator<XWPFTable> it = doc.getTablesIterator();
				while (it.hasNext()) {
					XWPFTable table = it.next();
					List<XWPFTableRow> rows = table.getRows();
					for (XWPFTableRow row : rows) {
						List<XWPFTableCell> cells = row.getTableCells();
						for (XWPFTableCell cell : cells) {
							List<XWPFParagraph> paragraphListTable =  cell.getParagraphs();
							processParagraphs(paragraphListTable, param, doc);
						}
					}
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
		    logger.error(e);
		}
		return doc;
	}
	/**
	 * 处理段落
	 * @param paragraphList
	 */
	public  void processParagraphs(List<XWPFParagraph> paragraphList,Map<String, Object> param,CustomXWPFDocument doc){
		if(paragraphList != null && paragraphList.size() > 0){
			for(XWPFParagraph paragraph:paragraphList){
				List<XWPFRun> runs = paragraph.getRuns();
				for (XWPFRun run : runs) {
					String text = run.getText(0);
					//logger.error(" text = " + text );
					if(text != null){
						boolean isSetText = false;
						for (Entry<String, Object> entry : param.entrySet()) {
							String key = entry.getKey();
							if(text.indexOf(key) != -1){
								isSetText = true;
								Object value = entry.getValue();
								if (value instanceof String) {//文本替换
									text = text.replace(key, value.toString());
									// 加粗
									if(text == "省"||text == "分" ||text.equals("省") || text.equals("分") ){
										run.setBold(true);
									}
									continue;
								} 
							}
						}
						if(isSetText){
							run.setText(text,0);
						}
					}
				}
			}
		}
	}
	/**
	 * 根据图片类型，取得对应的图片类型代码
	 * @param picType
	 * @return int
	 */
	/*private static int getPictureType(String picType){
		int res = CustomXWPFDocument.PICTURE_TYPE_PICT;
		if(picType != null){
			if(picType.equalsIgnoreCase("png")){
				res = CustomXWPFDocument.PICTURE_TYPE_PNG;
			}else if(picType.equalsIgnoreCase("dib")){
				res = CustomXWPFDocument.PICTURE_TYPE_DIB;
			}else if(picType.equalsIgnoreCase("emf")){
				res = CustomXWPFDocument.PICTURE_TYPE_EMF;
			}else if(picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")){
				res = CustomXWPFDocument.PICTURE_TYPE_JPEG;
			}else if(picType.equalsIgnoreCase("wmf")){
				res = CustomXWPFDocument.PICTURE_TYPE_WMF;
			}
		}
		return res;
	}*/
	/**
	 * 将输入流中的数据写入字节数组
	 * @param in
	 * @return
	 */
	public  byte[] inputStream2ByteArray(InputStream in,boolean isClose){
		byte[] byteArray = null;
		try {
			int total = in.available();
			byteArray = new byte[total];
			in.read(byteArray);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(isClose){
				try {
					in.close();
				} catch (Exception e2) {
					System.out.println("关闭流失败");
				}
			}
		}
		return byteArray;
	}
	
	
	public static void main(String[] args){
		String tmppath =WordUtils.class.getClassLoader().getResource("").getPath();
		System.out.println(tmppath+"wordTemplate");
	}
}
