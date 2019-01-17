package com.hpe.cmca.job.v2;

import java.io.*;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.hpe.cmca.common.AsposeUtil;
import com.hpe.cmca.util.DateComputeUtils;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.springframework.beans.factory.annotation.Autowired;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.util.ExceptionTool;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.entity.WordConfigEntity;
import com.hpe.cmca.common.Constants;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * <pre>
 * @author GuoXY
 * @refactor GuoXY
 * @date 20161019
 * @version 1.0
 * @see
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  20161019 	   GuoXY 	         1. Created this class.
 * </pre>
 */
@Component("wfg")
//@RequestMapping("/genWord")
public class Word0000FileGenProcessor extends CommonSubjectFileGenProcessor {

    // 当前专题生成汇总审计报告所需要"跑完模型的关注点数量"
    private int focusCdsCount = 5;
    @Autowired
    private MybatisDao mybatisDao = null;

    @Override
    protected boolean validateRequest(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId,
                                      Map<String, Object> configInfo) {

        if (prvdId != Constants.ChinaCode) {
            List<Object> concernList = concernFileGenService.selectFinishedConcernsNew(null, audTrm, subjectId, prvdId);
            if (concernList.size() < focusCdsCount) {// 代表该省的5个有价卡关注点并未都执行完毕，所以不需要生成审计报告
                return false;
            }
        }
        return true;
    }

    //@RequestMapping(value = "index", produces = "plain/text; charset=UTF-8")
    public void genWordTest(String audTrm, String subjectId, int prvdId) {
        genProvDocFile(audTrm, subjectId, null, prvdId, null, null, null);
    }

    @Override
    public File genProvDocFile(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo,
                               Map<String, Object> request, Boolean useChineseName) {

        String localFilePath = getLocalFilePath();


        XWPFDocument doc = new XWPFDocument(); // 新建一个文档
        XWPFDocument docEnd = new XWPFDocument(); // 新建一个最终文档
//		Map<String,XWPFParagraph> paraMap = new HashMap<String,XWPFParagraph>();
//		Map<String,String> textMap = new HashMap<String,String>();
        List<Map<String, String>> speBlockList = new ArrayList<Map<String, String>>();//用于记录需要判断显示“|”分隔符前方文字还是后方文字的段落，用于整个专题没有任何违规情况时，显示分隔符后的文字。
        Map<String, List<String>> rangListMap = new HashMap<String, List<String>>();
        int aFlag = 0;
        List<String> rangList = new ArrayList<String>();
        File resultFile = null;
        String wordName = "";
        XWPFParagraph p = null;
        try {
            List<Map<String, Object>> dataList = selectWordConfig(Integer.parseInt(subjectId), prvdId);
            for (Map<String, Object> dataMap : dataList) {
                try {
                    p = doc.createParagraph();// 新建段落
                    WordConfigEntity wce = genConfig(dataMap, audTrm);
                    if (wce == null) continue;
                    if (wce.getOrderRange() != null && !rangListMap.containsKey(listToString(wce.getOrderRange()))) {
                        rangListMap.put(listToString(wce.getOrderRange()), new ArrayList<String>());
                    }
                    if (wce.getOrderRange() != null && new ArrayList<String>(Arrays.asList(wce.getOrderRange())).contains(wce.getBlockCode())) {
                        rangListMap.get(listToString(wce.getOrderRange())).add(wce.getBlockCode());
                        rangList = new ArrayList<String>(rangListMap.get(listToString(wce.getOrderRange())));
                    }
//				if(wce.getOrderRange()==null||wce.getOrderRange().equals(""))
//					rangList.clear();

                    wordName = replaceText(wce.getWordName(), audTrm) + ".docx";

                    if (wce.getBlockContent().contains("|") && wce.getTextOrTable().equals(1)) {
//					Map<String,String> mpTp = new HashMap<String,String>();
//					mpTp.put(wce.getBlockCode(), wce.getBlockContent());
//					speBlockList.add(mpTp);
//					wce.setBlockContent(wce.getBlockContent().split("\\|")[1]);

                        List<Map<String, Object>> resultMapList = wce.getTbDataMap();

                        if (wce.getExternalSql() == 0) {
                            if (resultMapList == null || resultMapList.size() == 0 || resultMapList.isEmpty() || resultMapList.get(0) == null || resultMapList.get(0).get(wce.getShowFlag()) == null)
                                wce.setBlockContent(wce.getBlockContent().split("\\|")[0]);
                            if (resultMapList != null && resultMapList.size() > 0 && resultMapList.get(0) != null && !resultMapList.get(0).isEmpty() && resultMapList.get(0).get(wce.getShowFlag()) != null && Double.parseDouble(resultMapList.get(0).get(wce.getShowFlag()).toString().replace(",", "")) - 0.00 == 0)
                                wce.setBlockContent(wce.getBlockContent().split("\\|")[0]);
                            if (resultMapList != null && resultMapList.size() > 0 && resultMapList.get(0) != null && !resultMapList.get(0).isEmpty() && resultMapList.get(0).get(wce.getShowFlag()) != null && Double.parseDouble(resultMapList.get(0).get(wce.getShowFlag()).toString().replace(",", "")) - 0.00 == 1)
                                wce.setBlockContent(wce.getBlockContent().split("\\|")[1]);

                            if (resultMapList != null && resultMapList.size() > 0 && resultMapList.get(0) != null && !resultMapList.get(0).isEmpty() && resultMapList.get(0).get(wce.getShowFlag()) != null && Double.parseDouble(resultMapList.get(0).get(wce.getShowFlag()).toString().replace(",", "")) - 0.00 > 1)
                                wce.setBlockContent("");
                        }
                        if (wce.getExternalSql() == 1) {
                            if (resultMapList == null || resultMapList.size() == 0 || resultMapList.isEmpty() || resultMapList.get(0) == null || resultMapList.get(0).get(wce.getShowFlag()) == null)
                                wce.setBlockContent(wce.getBlockContent().split("\\|")[0]);
                            if (resultMapList != null && resultMapList.size() > 0 && resultMapList.get(0) != null && !resultMapList.get(0).isEmpty() && resultMapList.get(0).get(wce.getShowFlag()) != null && Double.parseDouble(resultMapList.get(0).get(wce.getShowFlag()).toString().replace(",", "")) - 0.00 < 0)
                                wce.setBlockContent(wce.getBlockContent().split("\\|")[0]);
                            if (resultMapList != null && resultMapList.size() > 0 && resultMapList.get(0) != null && !resultMapList.get(0).isEmpty() && resultMapList.get(0).get(wce.getShowFlag()) != null && Double.parseDouble(resultMapList.get(0).get(wce.getShowFlag()).toString().replace(",", "")) - 0.00 == 0)
                                wce.setBlockContent(wce.getBlockContent().split("\\|")[1]);

                            if (resultMapList != null && resultMapList.size() > 0 && resultMapList.get(0) != null && !resultMapList.get(0).isEmpty() && resultMapList.get(0).get(wce.getShowFlag()) != null && Double.parseDouble(resultMapList.get(0).get(wce.getShowFlag()).toString().replace(",", "")) - 0.00 > 0)
                                wce.setBlockContent("");
                        }
                    }

                    if (wce.getTextOrTable().equals(1)) createNewParagraph(p, wce, rangList, doc);// 新建段落
                    if (wce.getTextOrTable().equals(0)) createStyledTable(doc, wce);// 新建表格

                    //setParaOrder(paraMap,rangList,textMap);

                    if (rangList.size() == 2) {//初始时序号的第一个段不设置序号，如果有第二段，则第一段加上序号1
                        int a = getNumByBlockCode(rangList.get(0));
                        List<XWPFParagraph> p1 = doc.getParagraphs();
                        String ss = p1.get(a).getParagraphText();
                        XWPFRun r1 = p1.get(a).getRuns().get(0);
                        r1.setText(setOrderType(1, wce.getOrderType()) + ss, 0);
                        logger.error("wenbenchazhao：" + ss);
                    }


//				if(rangList.size()>0&&aFlag==0){//序号列表不为空，即认为有违规数据
//					int a = 0;
//					for(Map<String,String> sss:speBlockList){
//						for(Map.Entry<String, String> stp:sss.entrySet()){
//							a = getNumByBlockCode(stp.getKey());
//							List<XWPFParagraph> p1 = doc.getParagraphs();
//							//String ss = p1.get(a).getParagraphText();
//							XWPFRun r1 = p1.get(a).getRuns().get(0);
//							r1.setText(stp.getValue().split("\\|")[0],0);
//						}
//
//					}
//					aFlag=1;
//				}

                    rangList.clear();
                } catch (Exception e) {
                    logger.error("数据块编码为" + (String) dataMap.get("block_code") + "的审计报告配置生成异常：" + e.getMessage(), e);
                    continue;
                }

            }
            int pi = 0;
            int ti = 0;
            XWPFParagraph paragraph = null;
            XWPFTable table = null;
            for (IBodyElement element : doc.getBodyElements()) {
                if (element.getElementType() == BodyElementType.PARAGRAPH) {
                    paragraph = (XWPFParagraph) element;
                    if (!paragraph.getParagraphText().equals("")) {
                        docEnd.createParagraph();
                        docEnd.setParagraph(paragraph, pi);
                        pi++;
                    }
                }

                if (element.getElementType() == BodyElementType.TABLE) {
                    table = (XWPFTable) element;
                    if (table != null) {
                        docEnd.createTable();
                        docEnd.setTable(ti, table);
                        ti++;
                    }
                }
            }


        } catch (Exception e) {
            logger.error("数据异常：" + e.getMessage(), e);
            //return null;
        }
        try {
            if (wordName.equals("")) return null;
            resultFile = generate(docEnd, localFilePath, wordName);
        } catch (Exception e) {
            logger.error("word文件-" + wordName + "-生成异常：" + e.getMessage(), e);
            FileUtil.removeFile(FileUtil.buildFullFilePath(localFilePath, wordName));
            logger.error("#### 生成审计报告(  " + wordName + "  )异常，文件已删除。错误信息为:" + ExceptionTool.getExceptionDescription(e));
            throw new RuntimeException("#### 生成审计报告异常。", e);
        }
        return resultFile;
    }

    public void mergeParagraphWithLast(XWPFDocument doc, int paraNum) {
        List<IBodyElement> elementL=doc.getBodyElements();
        IBodyElement ele1 = null;
        for(int i=paraNum-1;i>=0;i--){
            ele1=elementL.get(i);
            if(ele1.getElementType()==BodyElementType.PARAGRAPH&&!"".equals(((XWPFParagraph)ele1).getParagraphText())){
                break;
            }
        }
        IBodyElement ele = elementL.get(paraNum);
        if(ele1.getElementType()==BodyElementType.PARAGRAPH&&ele.getElementType()==BodyElementType.PARAGRAPH){
            String s1 = ((XWPFParagraph)ele1).getParagraphText().replace(" ","").replace("  ","");
            String s2 = ((XWPFParagraph)ele).getParagraphText().replace(" ","").replace("  ","");

            List<XWPFRun> runL1=((XWPFParagraph)ele1).getRuns();
            for(XWPFRun run:runL1){
                run.setText("");
            }
            List<XWPFRun> runL=((XWPFParagraph)ele).getRuns();
            for(XWPFRun run:runL){
                run.setText("");
            }

            XWPFRun r1 = runL1.get(0);
            XWPFRun r2 = runL.get(0);

            r1.setText(s1 + s2, 0);
            r2.setText("", 0);

        }
//        List<XWPFParagraph> p1 = doc.getParagraphs();
//        String s1 = p1.get(paraNum - 1).getParagraphText();
//        String s2 = p1.get(paraNum).getParagraphText();
//        XWPFRun r1 = p1.get(paraNum - 1).getRuns().get(0);
//        XWPFRun r2 = p1.get(paraNum).getRuns().get(0);
//        r1.setText(s1 + s2, 0);
//        r2.setText("", 0);

        //r1.setText(setOrderType(1,wce.getOrderType())+ss,0);
    }

    public int getNumByBlockCode(String blockCode) {
        int a = blockCode.indexOf("_");
        String result = blockCode.substring(a + 1, blockCode.length());
        return Integer.parseInt(result) - 1;
    }

    public String listToString(String[] ss) {
        StringBuilder result = new StringBuilder();
        List<String> l = new ArrayList<String>(Arrays.asList(ss));
        for (String s : l) {
            result.append(s);
        }
        return result.toString();
    }

//    public File generate(XWPFDocument doc, String localFilePath, String localFileName) throws Exception {
//        String fullPathName = localFilePath + "/" + localFileName;
//        FileOutputStream out = null;
//        File targetFile = null;
//        try {
//            targetFile = new File(fullPathName);
//            out = new FileOutputStream(fullPathName);
//            doc.write(out);
//        } catch (IOException e) {
//            logger.error("shenjitongbao", e);
//            throw e;
//        } catch (Exception e) {
//            logger.error("shenjitongbao", e);
//            throw e;
//        } finally {
//            try {
//                out.close();
//            } catch (IOException e) {
//                logger.error("shenjitongbao", e);
//                throw e;
//            }
//        }
//        return targetFile;
//    }
    public File generate(XWPFDocument doc, String localFilePath, String localFileName) throws Exception {
        AsposeUtil.setWordsLicense();
        String fullPathName = localFilePath + "/" + localFileName;
        FileOutputStream out = null;
        File targetFile = null;
        ByteArrayOutputStream outputStreamTp = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream =null;
        Document docNew =null;
        try {
            targetFile = new File(fullPathName);
            out = new FileOutputStream(fullPathName);
            doc.write(outputStreamTp);
            inputStream = new ByteArrayInputStream(outputStreamTp.toByteArray());
            docNew = new Document(inputStream);
            docNew.save(out, SaveFormat.DOCX);
        } catch (IOException e) {
            logger.error("shenjitongbao", e);
            throw e;
        } catch (Exception e) {
            logger.error("shenjitongbao", e);
            throw e;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                logger.error("shenjitongbao", e);
                throw e;
            }
        }
        return targetFile;
    }

    public WordConfigEntity genConfig(Map<String, Object> dataMap, String audTrm) {
        WordConfigEntity wce = new WordConfigEntity();
        if (dataMap.get("block_code") == null) wce.setBlockCode(null);
        else wce.setBlockCode((String) dataMap.get("block_code"));
        if (dataMap.get("subject_id") == null) wce.setSubjectId(null);
        else wce.setSubjectId((Integer) dataMap.get("subject_id"));
        if (dataMap.get("prvd_id") == null) wce.setPrvdId(null);
        else wce.setPrvdId((Integer) dataMap.get("prvd_id"));
        if (dataMap.get("word_code") == null) wce.setWordCode(null);
        else wce.setWordCode((String) dataMap.get("word_code"));
        if (dataMap.get("word_name") == null) wce.setWordName(null);
        else wce.setWordName((String) dataMap.get("word_name"));
        if (dataMap.get("text_or_table") == null) wce.setTextOrTable(null);
        else wce.setTextOrTable((Integer) dataMap.get("text_or_table"));
        if (dataMap.get("block_content") == null) wce.setBlockContent(null);
        else wce.setBlockContent(((String) dataMap.get("block_content")).replace("\n","").replace("\r", "").replace("\t", ""));
        if (dataMap.get("query_sql") == null) wce.setQuerySql(null);
        else wce.setQuerySql((String) dataMap.get("query_sql"));

        if (dataMap.get("per_index") == null) wce.setPerIndex(null);
        else wce.setPerIndex(((String) dataMap.get("per_index")).split(","));

        if (dataMap.get("show_by_flag") == null) wce.setShowByFlag(null);
        else wce.setShowByFlag((Integer) dataMap.get("show_by_flag"));
        if (dataMap.get("show_flag") == null) wce.setShowFlag(null);
        else wce.setShowFlag((String) dataMap.get("show_flag"));
        if (dataMap.get("order_type") == null) wce.setOrderType(null);
        else wce.setOrderType((Integer) dataMap.get("order_type"));
        if (dataMap.get("order_range") == null) wce.setOrderRange(null);
        else wce.setOrderRange(((String) dataMap.get("order_range")).split(","));

        //if(dataMap.get("order_num_sql")==null)wce.setOrderNumSql(null);else wce.setOrderNumSql((String) dataMap.get("order_num_sql"));

        if (dataMap.get("font_family") == null) wce.setFontFamily(null);
        else wce.setFontFamily((String) dataMap.get("font_family"));
        if (dataMap.get("font_size") == null) wce.setFontSize(null);
        else wce.setFontSize((Integer) dataMap.get("font_size"));
        if (dataMap.get("color") == null) wce.setColor(null);
        else wce.setColor((String) dataMap.get("color"));
        if (dataMap.get("bold") == null) wce.setBold(null);
        else wce.setBold((Integer) dataMap.get("bold"));
        if (dataMap.get("italic") == null) wce.setItalic(null);
        else wce.setItalic((Integer) dataMap.get("italic"));
        if (dataMap.get("strike") == null) wce.setStrike(null);
        else wce.setStrike((Integer) dataMap.get("strike"));
        if (dataMap.get("underline") == null) wce.setUnderline(null);
        else wce.setUnderline((Integer) dataMap.get("underline"));
        if (dataMap.get("text_position") == null) wce.setTextPosition(null);
        else wce.setTextPosition((Integer) dataMap.get("text_position"));
        if (dataMap.get("alignment") == null) wce.setAlignment(null);
        else wce.setAlignment((Integer) dataMap.get("alignment"));
        if (dataMap.get("indentation_first_line") == null) wce.setIndentationFirstLine(null);
        else wce.setIndentationFirstLine((Integer) dataMap.get("indentation_first_line"));
        if (dataMap.get("spacing_before") == null) wce.setSpacingBefore(null);
        else wce.setSpacingBefore((Integer) dataMap.get("spacing_before"));
        if (dataMap.get("page_break") == null) wce.setPageBreak(null);
        else wce.setPageBreak((Integer) dataMap.get("page_break"));

        if (dataMap.get("tb_width") == null) wce.setTbWidth(null);
        else wce.setTbWidth((Integer) dataMap.get("tb_width"));
        if (dataMap.get("tb_height") == null) wce.setTbHeight(null);
        else wce.setTbHeight((Integer) dataMap.get("tb_height"));
        if (dataMap.get("cell_width") == null) wce.setCellWidth(null);
        else wce.setCellWidth((Integer) dataMap.get("cell_width"));
        if (dataMap.get("cell_color") == null) wce.setCellColor(null);
        else wce.setCellColor((String) dataMap.get("cell_color"));
        if (dataMap.get("tb_rows") == null) wce.setTbRows(null);
        else wce.setTbRows((Integer) dataMap.get("tb_rows"));
        if (dataMap.get("tb_cols") == null) wce.setTbCols(null);
        else wce.setTbCols((Integer) dataMap.get("tb_cols"));

        if (dataMap.get("merge_last") == null) wce.setMergeLast(null);
        else wce.setMergeLast((Integer) dataMap.get("merge_last"));
        if (dataMap.get("loop_text") == null) wce.setLoopText(null);
        else wce.setLoopText((Integer) dataMap.get("loop_text"));
        if (dataMap.get("external_sql") == null) wce.setExternalSql(null);
        else wce.setExternalSql((Integer) dataMap.get("external_sql"));


        if (dataMap.get("status") == null) wce.setStatus(null);
        else wce.setStatus((Integer) dataMap.get("status"));

        if (dataMap.get("merge_cell") == null) wce.setMergeCell(null);
        else wce.setMergeCell(((String) dataMap.get("merge_cell")).split("\\|"));

        if (dataMap.get("additional_show") == null) wce.setAdditionalShow(null);
        else wce.setAdditionalShow((Integer) dataMap.get("additional_show"));

        if (dataMap.get("additional_show_sql") == null) wce.setAdditionalShowSql(null);
        else wce.setAdditionalShowSql((String) dataMap.get("additional_show_sql"));

        if (wce.getAdditionalShow() != null && wce.getAdditionalShow() == 1) {
            List<Map<String, Object>> additionalShowList = executeSql(wce.getAdditionalShowSql(), audTrm,"0");
            if (additionalShowList == null || additionalShowList.size() == 0 || additionalShowList.get(0) == null || additionalShowList.get(0).isEmpty() || additionalShowList.get(0).get("show_flag") == null || Double.parseDouble(additionalShowList.get(0).get("show_flag").toString().replace(",", "")) - 0.00 <= 0)
                return null;
        }


        if (dataMap.get("status") == null) wce.setStatus(null);
        else wce.setStatus((Integer) dataMap.get("status"));

//		wce.setNumOrder(null);
//		List<Map<String, Object>> numOrderList =  executeSql(wce.getOrderNumSql(), audTrm);
//		if(numOrderList==null||numOrderList.size()==0||numOrderList.get(0)==null||numOrderList.get(0).isEmpty()||numOrderList.get(0).get("num_order") ==null){
//			//return null;
//		}else{
//			wce.setNumOrder((Integer)numOrderList.get(0).get("num_order"));
//		}

        List<Map<String, Object>> paramsMap = new ArrayList<>();
        List<Map<String, Object>> paramsMap1 = executeSql(wce.getQuerySql(), audTrm,"0");


        for (Map<String, Object> map : paramsMap1) {
            if (wce.getPerIndex() != null) {
                List<String> l = new ArrayList<String>(Arrays.asList(wce.getPerIndex()));
                for (String index : l) {
                    if (map.get(index) != null)
                        map.put(index, convert(map.get(index).toString()));
                    else
                        map.put(index, "0%");
                }
            }
            paramsMap.add(formatData(map));
        }

        wce.setTbDataMap(paramsMap);
        String tpText = "";
        String tpTextLoop = "";
        if (wce.getShowByFlag().equals(1)) {
            if (wce.getTextOrTable().equals(1)) {
                if (paramsMap == null || paramsMap.size() == 0 || paramsMap.get(0) == null || paramsMap.get(0).isEmpty() || paramsMap.get(0).get(wce.getShowFlag()) == null || Double.parseDouble(paramsMap.get(0).get(wce.getShowFlag()).toString().replace(",", "")) - 0.00 <= 0) {
                    return null;
                } else {
                    if (wce.getLoopText() != null && wce.getLoopText() == 1) {
                        tpTextLoop = replaceParamLoop(wce.getBlockContent(), paramsMap);
                        wce.setBlockContent(tpTextLoop);
                    } else {
                        tpText = replaceParam(wce.getBlockContent(), paramsMap.get(0));
                        wce.setBlockContent(tpText);
                    }

                }
            }
            if (wce.getTextOrTable().equals(0)) {
                if (paramsMap == null || paramsMap.size() == 0) {
                    return null;
                } else {
                    if (wce.getBlockContent() != null && !wce.getBlockContent().equals("")) {
                        String re[] = wce.getBlockContent().split("\\|");
                        wce.setTbHead(Arrays.asList(re));
                    }
                }

            }
        }

        if (wce.getShowByFlag().equals(0)) {
            if (wce.getTextOrTable().equals(1)) {
                if (paramsMap.size() > 0) {
                    if (wce.getLoopText() != null && wce.getLoopText() == 1) {
                        tpTextLoop = replaceParamLoop(wce.getBlockContent(), paramsMap);
                        wce.setBlockContent(tpTextLoop);
                    } else {
                        tpText = replaceParam(wce.getBlockContent(), paramsMap.get(0));
                        wce.setBlockContent(tpText);
                    }
                } else {
                    wce.setBlockContent(wce.getBlockContent());
                }

            }
            if (wce.getTextOrTable().equals(0)) {
                if (wce.getBlockContent() != null && !wce.getBlockContent().equals("")) {
                    String re[] = wce.getBlockContent().split("\\|");
                    wce.setTbHead(Arrays.asList(re));
                }
            }
        }

        return wce;
    }

    private Map<String, Object> formatData(Map<String, Object> params) {
        Map<String, Object> tpMap = new HashMap<>();
        //Object value = null;
        if (params != null && params.size() > 0) {
            Iterator it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next().toString();
                Object obj = params.get(key);
                tpMap.put(key, obj);
                try {
                    if (obj.toString().contains(".")) {
                        Double.parseDouble(obj.toString());
                        DecimalFormat df1 = new DecimalFormat("###,###,###,##0.00");
                        obj = df1.format(obj);
                        tpMap.put(key, obj);
                        continue;
                    }
                } catch (Exception e) {

                }

                try {
                    Integer.parseInt(obj.toString());
                    DecimalFormat df = new DecimalFormat("###,###,###,##0");
                    obj = df.format(obj);
                    tpMap.put(key, obj);
                    continue;
                } catch (Exception e) {

                }

            }
        }
        return tpMap;
    }


    public String setOrderType(int order, int orderType) {
        String paraOrder = "";
        List<String> orderTypeList1 = new ArrayList<String>(Arrays.asList("一", "二", "三", "四", "五", "六", "七", "八", "九", "十"));
        if (orderType == 1) {
            paraOrder = "（" + orderTypeList1.get(order - 1) + "）";
        }
        if (orderType == 2) {
            paraOrder = orderTypeList1.get(order - 1) + "、";
        }
        if (orderType == 3) {
            paraOrder = "（" + order + "）";
        }
        if (orderType == 4) {
            paraOrder = order + "、";
        }
        if (orderType == 0) {
            paraOrder = "";
        }
        return paraOrder;
    }

    public CTRPr getRunCTRPr(XWPFParagraph p, XWPFRun pRun) {

        CTRPr pRpr = null;//当前段落中的当前文本区域的属性
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
    public void setParagraphRunFontInfo(XWPFParagraph p, XWPFRun pRun, String fontFamily) {
        CTRPr pRpr = getRunCTRPr(p, pRun);
        // 设置字体
        CTFonts fonts = pRpr.isSetRFonts() ? pRpr.getRFonts() : pRpr
                .addNewRFonts();
        fonts.setAscii(fontFamily);
        fonts.setEastAsia(fontFamily);
        fonts.setHAnsi(fontFamily);
    }

    public void createNewParagraph(XWPFParagraph p, WordConfigEntity wce, List<String> rangList, XWPFDocument doc) {

        //XWPFParagraph p = doc.createParagraph();// 新建段落
        XWPFRun r = p.createRun();// 一个XWPFRun代表具有相同属性的一个区域
        //if(wce.getOrderType().equals(0))// 设置文本 没有序号
        //	r.setText(wce.getBlockContent());
        //if(wce.getOrderType().equals(1))// 设置文本 有序号

        if (rangList.size() > 0) {
            //if(wce.getNumOrder()!=null&&wce.getNumOrder()==1)
            if (rangList.size() == 1)
                r.setText(wce.getBlockContent(), 0);
            else
                r.setText(setOrderType(rangList.size(), wce.getOrderType()) + wce.getBlockContent(), 0);
        } else
            r.setText(wce.getBlockContent(), 0);

        if (wce.getBold().equals(1)) r.setBold(true);// 是否加粗

        if (wce.getUnderline().equals(1)) r.setUnderline(UnderlinePatterns.SINGLE);// 是否下划线

        if (wce.getItalic().equals(1)) r.setItalic(true);// 是否删倾斜

        if (wce.getStrike().equals(1)) r.setStrike(true);// 是否删除线

        //r.setFontFamily(wce.getFontFamily());// 设置字体
        //r.setFontFamily("黑体");
        setParagraphRunFontInfo(p, r, wce.getFontFamily());


        r.setFontSize(wce.getFontSize());// 字号

        r.setColor(wce.getColor());// 颜色

        r.setTextPosition(wce.getTextPosition());// 行高

        if (wce.getAlignment().equals(0)) // 段落对齐方式
            p.setAlignment(ParagraphAlignment.BOTH);
        if (wce.getAlignment().equals(1)) p.setAlignment(ParagraphAlignment.LEFT);
        if (wce.getAlignment().equals(2)) p.setAlignment(ParagraphAlignment.RIGHT);
        if (wce.getAlignment().equals(3)) p.setAlignment(ParagraphAlignment.CENTER);

        if (wce.getPageBreak().equals(1)) // 换页
            r.addBreak(BreakType.PAGE);

        p.setIndentationFirstLine(wce.getIndentationFirstLine());// 首行缩进
        p.setSpacingBefore(wce.getSpacingBefore());// 段落前的间距

        // System.out.println(tpText);
        if (wce.getMergeLast() != null && wce.getMergeLast() == 1)
            mergeParagraphWithLast(doc, getNumByBlockCode(wce.getBlockCode()));
    }

    public void createStyledTable(XWPFDocument doc, WordConfigEntity wce) {
        int rowIndex = 0;
        int colIndex = 0;
        int dataSize = 0;
        if (wce.getTbDataMap() == null || wce.getTbDataMap().size() == 0)
            dataSize = 1;
        else
            dataSize = wce.getTbDataMap().size();
        int nRows = 0;
        if (wce.getTbHead() != null) {
            nRows = wce.getTbRows() < dataSize + 1 ? wce.getTbRows() : dataSize + 1;
        } else {
            nRows = wce.getTbRows() < dataSize ? wce.getTbRows() : dataSize;
        }
        int nCols = wce.getTbCols();
        XWPFTable table = doc.createTable(nRows, nCols);
        //table.setWidth(wce.getTbWidth());
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        //CTString styleStr = tblPr.addNewTblStyle();
        //styleStr.setVal("StyledTable");

        CTTblWidth width = tblPr.addNewTblW();
        if (wce.getTbWidth() != null)
            width.setW(BigInteger.valueOf(wce.getTbWidth()));// 表格宽度
        else
            width.setW(BigInteger.valueOf(8000));// 表格宽度

        List<XWPFTableRow> rows = table.getRows();
        for (XWPFTableRow row : rows) {
            //row.setHeight(wce.getTbHeight());
            CTTrPr trPr = row.getCtRow().addNewTrPr();// 得到行属性
            CTHeight ht = trPr.addNewTrHeight();
            if (wce.getTbHeight() != null)
                ht.setVal(BigInteger.valueOf(wce.getTbHeight()));// 设置行高
            else
                ht.setVal(BigInteger.valueOf(360));// 设置行高

            List<XWPFTableCell> cells = row.getTableCells();// 得到行的单元格
            colIndex = 0;
            for (XWPFTableCell cell : cells) {
                //cell.
                CTTcPr tcpr = cell.getCTTc().addNewTcPr();// 得到单元格属性
                if (wce.getCellWidth() != null)
                    tcpr.addNewTcW().setW(BigInteger.valueOf(wce.getCellWidth()));// 设置单元格宽度

                CTVerticalJc va = tcpr.addNewVAlign();
                va.setVal(STVerticalJc.CENTER);// 单元格内容的对齐方式
                // 设置单元格颜色
                if (rowIndex == 0) {
                    CTShd ctshd = tcpr.addNewShd();
                    ctshd.setColor("auto");
                    ctshd.setVal(STShd.CLEAR);
                    // ctshd.setFill("A7BFDE");
                    ctshd.setFill(wce.getCellColor());
                }
                // 得到单元格的第一段
                XWPFParagraph p = cell.getParagraphs().get(0);

                XWPFRun r = p.createRun();

                if (wce.getTbHead() != null) {
                    if (rowIndex == 0) r.setText(wce.getTbHead().get(colIndex));// 设置文本
                    if (rowIndex > 0)
                        if (wce.getTbDataMap() != null && wce.getTbDataMap().size() > rowIndex - 1 && wce.getTbDataMap().get(rowIndex - 1) != null && wce.getTbDataMap().get(rowIndex - 1).get(getNumToChar(colIndex)) != null)
                            r.setText(wce.getTbDataMap().get(rowIndex - 1).get(getNumToChar(colIndex)).toString());// 设置文本
                } else {
                    if (wce.getTbDataMap() != null && wce.getTbDataMap().size() > rowIndex && wce.getTbDataMap().get(rowIndex) != null && wce.getTbDataMap().get(rowIndex).get(getNumToChar(colIndex)) != null)
                        r.setText(wce.getTbDataMap().get(rowIndex).get(getNumToChar(colIndex)).toString());// 设置文本
                }

                if (wce.getBold().equals(1)) r.setBold(true);// 是否加粗

                if (wce.getUnderline().equals(1)) r.setUnderline(UnderlinePatterns.SINGLE);// 是否下划线

                if (wce.getItalic().equals(1)) r.setItalic(true);// 是否删倾斜

                if (wce.getStrike().equals(1)) r.setStrike(true);// 是否删除线

                r.setFontFamily(wce.getFontFamily());// 设置字体

                r.setFontSize(wce.getFontSize());// 字号

                r.setColor(wce.getColor());// 颜色

                if (wce.getAlignment().equals(0)) p.setAlignment(ParagraphAlignment.BOTH);// 段落对齐方式
                if (wce.getAlignment().equals(1)) p.setAlignment(ParagraphAlignment.LEFT);
                if (wce.getAlignment().equals(2)) p.setAlignment(ParagraphAlignment.RIGHT);
                if (wce.getAlignment().equals(3)) p.setAlignment(ParagraphAlignment.CENTER);
                colIndex++;
            }
            rowIndex++;
        }
        if (wce.getMergeCell() != null) {
            for (String s : wce.getMergeCell()) {
                List<String> l = new ArrayList<String>(Arrays.asList(s.split(",")));
                if (Integer.parseInt(l.get(0)) == 0) {
                    mergeCellsHorizontal(table, Integer.parseInt(l.get(1)), Integer.parseInt(l.get(2)), Integer.parseInt(l.get(3)));
                }
                if (Integer.parseInt(l.get(0)) == 1) {
                    mergeCellsVertically(table, Integer.parseInt(l.get(1)), Integer.parseInt(l.get(2)), Integer.parseInt(l.get(3)));
                }
            }
        }

    }

    public String replaceParam(String text, Map<String, Object> params) {
//		Matcher m = Pattern.compile("[{]([^{^}]*?)[}]").matcher(text);
//		StringBuffer sb = new StringBuffer();
//		while (m.find()) {
//			if(params.get(m.group(1))==null)
//				params.put(m.group(1), "0");//如果为空则填横杠
//			m.appendReplacement(sb, subStrEnding(params.get(m.group(1)).toString(),""));
//		}
//		m.appendTail(sb);
//		return subDateZero(sb.toString());

        JexlContext jc = new MapContext(params);
        jc.set("DateUtils", DateComputeUtils.class);
        Matcher m = Pattern.compile("[{]([^{^}]*?)[}]").matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            if (jc.get(m.group(1)) == null)
                jc.set(m.group(1), "0");//如果为空则填横杠
            Expression e = new JexlEngine().createExpression(m.group(1));
            Object obj = e.evaluate(jc);
            m.appendReplacement(sb, subStrEnding(obj.toString(), ""));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public String subStrEnding(String text, String replaceText) {//字符串最后的符号，比如、，。；

        Matcher m = Pattern.compile("([，；、,;])$").matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, replaceText);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public String subDateZero(String text) {//处理02月 01日这样的问题
        Matcher m = Pattern.compile("年([0][1-9])月").matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "年" + m.group(1).toString().substring(1, 2) + "月");
        }
        m.appendTail(sb);

        Matcher m1 = Pattern.compile("月([0][1-9])日").matcher(sb.toString());
        StringBuffer sb1 = new StringBuffer();
        while (m1.find()) {
            m1.appendReplacement(sb1, "月" + m1.group(1).toString().substring(1, 2) + "日");
        }
        m1.appendTail(sb1);

        return sb1.toString();
    }

    public String subloopKeyWord(String text) {//删掉循环文本时的提示词，比如   “如：”
        Matcher m = Pattern.compile("([\u4e00-\u9fa5]+：|[\u4e00-\u9fa5]+:)").matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public String replaceParamLoop(String text, List<Map<String, Object>> paramsList) {
        StringBuffer sbTp = new StringBuffer();
        int i = 0;
        for (Map<String, Object> params : paramsList) {

//			Matcher m = Pattern.compile("[{]([^{^}]*?)[}]").matcher(text);
//			StringBuffer sb = new StringBuffer();
//			while (m.find()) {
//				if(params.get(m.group(1))==null)
//					params.put(m.group(1), "0");//如果为空则填横杠
//				m.appendReplacement(sb, params.get(m.group(1)).toString());
//			}
//			m.appendTail(sb);
            String sbString = replaceParam(text, params);

            if (i > 0) sbString = subloopKeyWord(sbString);

            sbTp.append(sbString);
            i++;
        }
        //String sbResult = sbTp.toString().substring(0, sbTp.toString().length()-1)+"。";
        String sbResult = subStrEnding(sbTp.toString(), "。");
        return subDateZero(sbResult);
    }

    public List<Map<String, Object>> executeSql(String querySql, String audTrm,String prvdId) {

        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        if (querySql == null || "".equals(querySql)) return mapList;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("audTrm", audTrm);
        paramMap.put("prvdId", prvdId);
        String querySqlEnd = replaceParam(querySql, paramMap);
        paramMap.put("querySql", querySqlEnd.replace("\r", " ").replace("\t", " ").replace("\n", " ") );
        mapList = mybatisDao.getList("commonMapper.executeSql", paramMap);
        if (mapList != null && mapList.size() > 0) {
            Object tpFlag = mapList.get(0).get("tp_flag");
            Object showFlag = mapList.get(0).get("show_flag");
            if (tpFlag != null || showFlag != null) {
                String tpStr = tpFlag != null ? tpFlag.toString().replace("\r", " ").replace("\t", " ") : null;
                if (tpStr == null) {
                    tpStr = showFlag != null ? showFlag.toString().replace("tp_flag", "show_flag").replace("\r", " ").replace("\t", " ") : null;
                }
                if ((tpStr.contains("from")||tpStr.contains("FROM")) && (tpStr.contains("SELECT") ||tpStr.contains("SEL") ||tpStr.contains("select") || tpStr.contains("sel"))) {
                    tpStr = replaceParam(tpStr, paramMap);
                    paramMap.put("querySql", tpStr);
                    mapList.clear();
                    mapList = mybatisDao.getList("commonMapper.executeSql", paramMap);
                }
            }
        }
        return mapList;
    }

    public String replaceText(String text, String audTrm) {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("audTrm", audTrm);
        String textEnd = replaceParam(text, paramMap);
        return textEnd;
    }

    public List<Map<String, Object>> selectWordConfig(int subjectId, int prvdId) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("subjectId", subjectId);
        paraMap.put("prvdId", prvdId);
        paraMap.put("status", 0);
        List<Map<String, Object>> resultMap = mybatisDao.getList("commonMapper.selectWordConfig", paraMap);
        return resultMap;
    }

    public String getNumToChar(int i) {

        char ch1 = 0, ch2 = 0;
        if (i < 26) {
            ch2 = (char) (i + 97);
            return String.valueOf(ch2);
        }
        ch2 = (char) (i % 26 + 97);
        if (i >= 26) ch1 = (char) (i / 26 - 1 + 97);
        return String.valueOf(ch1) + String.valueOf(ch2);
    }

    //如小于0.01%，则小数点保留到小数后出现数字为止
    public String convert(String param) {

        StringBuilder sb = new StringBuilder();
        Integer index = 0;
        for (int i = param.indexOf(".") + 1; i < param.length(); i++) {
            String ch = param.substring(i, i + 1);
            if ("0".equals(ch))
                index++;
            else
                break;
        }
        for (int j = 0; j < index + 1; j++) {//有数字为止后一位
            sb.append("0");
        }

        DecimalFormat df = new DecimalFormat("###,###,###,##0." + sb.toString());

        DecimalFormat df2 = new DecimalFormat("###,###,###,##0.00");

        Double d = Double.valueOf(param.replace("%", "").replace(",", ""));

        String result = "";
        if (d >= 0.01)
            result = df2.format(d);
        else {
            result = df.format(d);
            while (true) {
                if ("0".equals(result.substring(result.length() - 1, result.length())))
                    result = result.substring(0, result.length() - 1);
                else
                    break;
            }

        }
        if (".".equals(result.substring(result.length() - 1, result.length()))) {
            return result.substring(0, result.length() - 1) + "%";
        }
        return result + "%";//df.format(d)+"%";
    }

    // word跨列合并单元格
    public void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
            if (cellIndex == fromCell) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    // word跨行并单元格
    public void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            if (rowIndex == fromRow) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    public int getFocusCdsCount() {
        return focusCdsCount;
    }

    public void setFocusCdsCount(int focusCdsCount) {
        this.focusCdsCount = focusCdsCount;
    }
}
