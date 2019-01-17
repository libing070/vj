package com.hpe.cmca.common;

import com.aspose.cells.*;
import com.aspose.cells.BorderType;
import com.aspose.words.*;
import com.aspose.words.Border;
import com.aspose.words.Cell;
import com.aspose.words.HtmlSaveOptions;
import com.aspose.words.License;
import com.aspose.words.Row;
import com.aspose.words.RowCollection;
import com.aspose.words.SaveFormat;
import com.aspose.words.Style;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.finals.Constants;
import com.hpe.cmca.interfaces.FileGenMapper;
import com.hpe.cmca.job.v2.Word0000FileGenProcessor;
import com.hpe.cmca.pojo.ExcelModelConfig;
import com.hpe.cmca.pojo.FileGenData;
import com.hpe.cmca.pojo.WordModelConfig;
import com.hpe.cmca.service.FileAutoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.*;
import java.awt.Color;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("asposeUtil")
public class AsposeUtil {

    private static InputStream inputStream = null;

    private static Logger logger = Logger.getLogger(AsposeUtil.class);

    @Autowired
    protected MybatisDao mybatisDao;

    @Autowired
    Word0000FileGenProcessor wfg;

    @Autowired
    protected FilePropertyPlaceholderConfigurer propertyUtil = null;

    @Autowired
    private FileAutoService fileAutoService;

    private int titleOne=0;//已删除一级标题
    private int titleTwo=0;//已删除二级标题
    /**
     * 获取License的输入流
     *
     * @return
     */
    private static InputStream getLicenseInput() {
        if (inputStream == null) {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                inputStream = new FileInputStream(contextClassLoader.getResource("license.xml").getPath());

            } catch (FileNotFoundException e) {
                logger.error("license not found!", e);
            }
        }
        return inputStream;
    }

    /**
     * 设置License
     *
     * @return true表示已成功设置License, false表示失败
     */
    public static boolean setWordsLicense() {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            com.aspose.words.License aposeLic = new com.aspose.words.License();
            aposeLic.setLicense(contextClassLoader.getResourceAsStream("license.xml"));
            return aposeLic.getIsLicensed();
        } catch (Exception e) {
            logger.error("set words license error!", e);
        }
        return false;
    }

    /**
     * 设置License
     *
     * @return true表示已成功设置License, false表示失败
     */
    public static boolean setCellsLicense() {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            com.aspose.cells.License aposeLic = new com.aspose.cells.License();
            aposeLic.setLicense(contextClassLoader.getResourceAsStream("license.xml"));
            return true;

        } catch (Exception e) {
            logger.error("set cells license error!", e);
        }
        return false;
    }


    @RequestMapping(value = "word2HtmlNoHandle", produces = "text/json;charset=UTF-8")
    public String word2HtmlNoHandle(String wordPath) {
        setWordsLicense();
        String path = this.getClass().getResource("/").getPath();
        path = path.replace("classes", "resource");
        File file = new File(wordPath);
        try {
            HtmlSaveOptions htmlSaveOptions = new HtmlSaveOptions();
            htmlSaveOptions.setSaveFormat(SaveFormat.HTML);
            htmlSaveOptions.setEncoding(java.nio.charset.Charset.forName("UTF-8"));
            htmlSaveOptions.setImageSavingCallback(new HandleImageSaving());

            InputStream inputStream = new FileInputStream(file);
            Document doc = new Document(inputStream);

            SectionCollection sc = doc.getSections();
            for (int sci = 0; sci < sc.getCount(); sci++) {
                NodeCollection ndc = sc.get(sci).getBody().getChildNodes();
                handWhite(ndc);
            }
            String time = String.valueOf(new Date().getTime());
            System.out.println(path);
            File f1 = new File(path);
            List<File> fileList = Arrays.asList(f1.listFiles());
            for (File ff : fileList) {
                if (ff.isFile()) {
                    if (ff.getName().contains("tpWord")) {
                        System.out.println("正在删除：" + ff.getName() + " 文件");
                        ff.delete();
                    }
                }
            }
            doc.save(path + "tpWord" + time + ".html", htmlSaveOptions);
            return "tpWord" + time + ".html";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    private void handWhite(NodeCollection ndc) {

        int count = ndc.getCount();
        Paragraph pa = null;
        Table ta = null;
        for (int i = 0; i < count; i++) {
            try {
                System.out.println(ndc.get(i).getNodeType());
                if (ndc.get(i).getNodeType() == 8) {
                    pa = (Paragraph) ndc.get(i);
                    if (pa.getParagraphFormat().isListItem()) {
                        pa.getParagraphFormat().clearFormatting();
                    }
                    for (int j = 0; j < pa.getRuns().getCount(); j++) {
                        pa.getRuns().get(j).getFont().setColor(Color.white);
                        System.out.println(pa.getRuns().get(j).getText());
                    }

                }
                if (ndc.get(i).getNodeType() == 5) {
                    ta = (Table) ndc.get(i);
                    RowCollection rows = ta.getRows();
                    for (int j = 0; j < rows.getCount(); j++) {
                        Row row = rows.get(j);
                        CellCollection cells = row.getCells();
                        for (int jj = 0; jj < cells.getCount(); jj++) {
                            Cell cell = cells.get(jj);
                            cell.getCellFormat().getBorders().setColor(Color.white);
                            NodeCollection ndcCell = cell.getChildNodes();
                            handWhite(ndcCell);//递归处理
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public List<Map<String, Object>> word2Html(File file,String wordName, String fileCode, String versionId,String srcPath,int isPreGen) throws Exception {
        setWordsLicense();
        List<Map<String, Object>> lm = new ArrayList<>();
        //File file = new File(propertyUtil.getPropValue("tmpModel") + "/" + wordName);
        Path p = Paths.get(srcPath, fileCode + "V" + versionId);
        try {
            HtmlSaveOptions htmlSaveOptions = new HtmlSaveOptions();
            htmlSaveOptions.setSaveFormat(SaveFormat.HTML);
            htmlSaveOptions.setEncoding(java.nio.charset.Charset.forName("UTF-8"));
            htmlSaveOptions.setImageSavingCallback(new HandleImageSaving());
            ByteArrayOutputStream outputStreamTp = new ByteArrayOutputStream();

            InputStream inputStream = new FileInputStream(file);
            Document doc = new Document(inputStream);

            doc.save(p.toString() + "/word.html", htmlSaveOptions);
            if(isPreGen==1)//如果是预生成的预览，无需以下处理
                return lm;
            doc.save(outputStreamTp, htmlSaveOptions);
            outputStreamTp.close();

            String str = outputStreamTp.toString("UTF-8");
            //System.out.println(text);
            Integer blockNum1 = parseWordOne(str, p.toString() + "/wordOne.html");
            Integer blockNum = parseWordTwo(str, p.toString() + "/wordTwo.html");
            Map<String, Object> mapTp = new HashMap<>();
            mapTp.put("dataPointNum", blockNum);
            lm.add(mapTp);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return lm;
    }

    public Integer parseWordOne(String text, String target) {
        Matcher m = Pattern.compile("(<table.*?</table>)|(<p.*?<span.*?[\u4e00-\u9fa5]+.*?</span></p>)").matcher(text);
        StringBuffer sb = new StringBuffer();
        String m1 = "";
        String m2 = "";
        int i = 0;
        while (m.find()) {
            m1 = m.group(1);
            m2 = m.group(2);
            if (m1 != null)
                m.appendReplacement(sb, m.group(1).substring(0, m.group(1).lastIndexOf("</table>")) + "<a href='javascript:;' @click='edit(" + i + ")'>编辑</a>  <a href='javascript:;' @click='reuse(" + i + ")'>复用</a></table>");
            if (m2 != null)
                m.appendReplacement(sb, m.group(2).substring(0, m.group(2).lastIndexOf("</p>")) + "<a href='javascript:;' @click='edit(" + i + ")'>编辑</a>  <a href='javascript:;' @click='reuse(" + i + ")'>复用</a></p>");
            i++;
        }
        m.appendTail(sb);
        write(Paths.get(target).toFile(), sb.toString());

        return i;
    }

    public Integer parseWordTwo(String text, String target) {
        //Matcher m = Pattern.compile("(<table.*?</table>)|(<p[^<]*<span[^<]*[\u4e00-\u9fa5]+[^>]*?</span>.*?</p>)").matcher(text);
        Matcher m = Pattern.compile("(<table.*?</table>)|(<p.*?<span.*?[\u4e00-\u9fa5]+.*?</span></p>)").matcher(text);
        StringBuffer sb = new StringBuffer();
        String m1 = "";
        String m2 = "";
        int i = 0;
        while (m.find()) {
            m1 = m.group(1);
            m2 = m.group(2);
            System.out.println(m.group(1));
            System.out.println(m.group(2));
            if (m1 != null)
                m.appendReplacement(sb, m.group(1).substring(0, m.group(1).lastIndexOf("</table>")) + "<span style='color:#409EFF'>" + i + "</span></table>");
            if (m2 != null)
                m.appendReplacement(sb, m.group(2).substring(0, m.group(2).lastIndexOf("</p>")) + "<span style='color:#409EFF'>" + i + "</span></p>");
            i++;
        }
        m.appendTail(sb);
        write(Paths.get(target).toFile(), sb.toString());

        return i;
    }

    public class HandleImageSaving implements IImageSavingCallback {
        public void imageSaving(ImageSavingArgs e) {
            try {
                e.setImageStream(new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {

                    }
                });
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.setKeepImageStreamOpen(false);
        }

    }

    @RequestMapping(value = "excel2Html", produces = "text/json;charset=UTF-8")
    //public void excel2Html(String excelName, HttpServletResponse response) {
    public List<Map<String, Object>> excel2Html(File file,String excelName, String fileCode, String versionId,String srcPath,int isPreGen) throws Exception {
        setCellsLicense();

        //File file = new File(propertyUtil.getPropValue("tmpModel") + "/" + excelName);
        List<Map<String, Object>> lm = new ArrayList<>();
        try {
            InputStream inputStream = new FileInputStream(file);
            Workbook wk = new Workbook(inputStream);
            WorksheetCollection wsc = wk.getWorksheets();
            Path p = Paths.get(srcPath, fileCode + "V" + versionId);
            for (int ii = 0; ii < wsc.getCount(); ii++) {
                Map<String, Object> map = new HashMap<>();
                Worksheet ws = wsc.get(ii);
                Workbook wkn = new Workbook();
                com.aspose.cells.HtmlSaveOptions htmlSaveOptions = new com.aspose.cells.HtmlSaveOptions();
                htmlSaveOptions.setExcludeUnusedStyles(true);
                htmlSaveOptions.setExportSimilarBorderStyle(false);
                htmlSaveOptions.setExportWorksheetCSSSeparately(true);

                wkn.getWorksheets().get(0).setName(ws.getName());
                wkn.getWorksheets().get(0).copy(ws);
                wkn.save(p.toString() + "/sheet" + ii + ".html", htmlSaveOptions);

                ByteArrayOutputStream outputStreamTp = new ByteArrayOutputStream();
                wkn.save(outputStreamTp, htmlSaveOptions);
                String str = outputStreamTp.toString("UTF-8").replace("\n", "").replace("\t", "").replace("\r", "");

                str = parseExcel(str, p.toString() + "/sheet" + ii + ".html");
                if(isPreGen==1)//如果是预生成的预览，无需以下处理
                    continue;
                parseExcelOne(str, p.toString() + "/sheetOne" + ii + ".html");
                Integer blockNum = parseExcelTwo(str, p.toString() + "/sheetTwo" + ii + ".html");
                Map<String, Object> mapTp = new HashMap<>();
                mapTp.put("dataPointNum", blockNum);
                lm.add(mapTp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return lm;
    }

    public String parseExcel(String text, String target) throws IOException {
        Matcher m = Pattern.compile("(<body.*?</body>)").matcher(text);

        while (m.find()) {
            text = m.group(1);
            //System.out.println(m.group(1));
        }
        text = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><div v-html=\"style\"></div></head>" + text + "</html>";
        Files.delete(Paths.get(target));
        write(Paths.get(target).toFile(), text);//原来的html文件第一种处理
        return text;

    }

    public Integer parseExcelOne(String text, String target) {
        Matcher m = Pattern.compile("(<tr.*?[{]{1}[a]{1}[}]{1}.*?</tr>)").matcher(text);
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (m.find()) {
            System.out.println(m.group(1));
            if (m != null)
                m.appendReplacement(sb, m.group(1).substring(0, m.group(1).lastIndexOf("</tr>")) + "<td><a href='javascript:;' @click='edit(" + i + ")'>编辑</a>  <a href='javascript:;' @click='reuse(" + i + ")'>复用</a></td></tr>");
            i++;
        }
        m.appendTail(sb);
        write(Paths.get(target).toFile(), sb.toString());//原来的html文件第一种处理

        return i;
    }

    public Integer parseExcelTwo(String text, String target) {
        Matcher m = Pattern.compile("(<tr.*?[{]{1}[a]{1}[}]{1}.*?</tr>)").matcher(text);
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (m.find()) {
            System.out.println(m.group(1));
            if (m != null)
                m.appendReplacement(sb, m.group(1).substring(0, m.group(1).lastIndexOf("</tr>")) + "<td><span style='color:#409EFF'>" + i + "</span></td></tr>");
            i++;
        }
        m.appendTail(sb);
        write(Paths.get(target).toFile(), sb.toString());//原来的html文件第二种处理
        return i;
    }

    public String read(File file) {
        String str = "";
        try {
            InputStream inputStream = new FileInputStream(file);
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            str = result.toString("UTF-8");
            inputStream.close();
            result.close();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public void write(File file, String str) {
        try {
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(str.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String work(FileGenData fileGenData,String srcPath,String targetPath) throws Exception {
        if(srcPath==null)
            srcPath = propertyUtil.getPropValue("tmpModel");
        if(targetPath==null)
            targetPath = propertyUtil.getPropValue("tmpFile");
        String fileFinalName = null;
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("fileCode", fileGenData.getFileCode());
        paraMap.put("versionId",fileGenData.getVersionId());
        try {
            FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
            if ("0".equals(fileGenData.getFileFormatType())) {//word
                List<WordModelConfig> wordModelConfigList = fileGenMapper.selectWordModel(paraMap);
                if (wordModelConfigList.size() > 0) {
                    fileFinalName = wordHandle(wordModelConfigList, fileGenData.getAudTrm(), fileGenData.getPrvdId(), wordModelConfigList.get(0).getWordName(),fileGenData.getFileCode(),fileGenData.getVersionId(),srcPath,targetPath);
                }
            }
            if ("1".equals(fileGenData.getFileFormatType())) {//excel
                List<ExcelModelConfig> excelModelConfigList = fileGenMapper.selectExcelModel(paraMap);
                if (excelModelConfigList.size() > 0) {
                    fileFinalName = excelHandle(excelModelConfigList, fileGenData.getAudTrm(), fileGenData.getPrvdId(), excelModelConfigList.get(0).getExcelName(),fileGenData.getFileCode(),fileGenData.getVersionId(),srcPath,targetPath);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return fileFinalName;
    }

    private String excelHandle(List<ExcelModelConfig> excelModelConfigList, String audTrm, String prvdId, String fileName,String fileCode,String versionId,String srcPath,String targetPath) throws Exception {
        setCellsLicense();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("audTrm", audTrm);
        paramMap.put("prvdName", Constants.MAP_PROVD_NAME.get(Integer.parseInt(prvdId)));
        String excelName = wfg.replaceParam(fileName, paramMap);
        File file = new File( srcPath+ "/"+fileCode+"V"+versionId+"/"+fileName);
        try {
            int sheetOrder = 0;
            Map<Integer, List<ExcelModelConfig>> listMap = new HashMap<>();
            List<ExcelModelConfig> tpList = new ArrayList<>();
            for (ExcelModelConfig excelModelConfig : excelModelConfigList) {
                if (sheetOrder == excelModelConfig.getSheetOrder()) {
                    tpList.add(excelModelConfig);
                }
                if (sheetOrder != excelModelConfig.getSheetOrder()) {
                    listMap.put(sheetOrder, tpList);
                    sheetOrder = excelModelConfig.getSheetOrder();
                    tpList = new ArrayList<>();
                    tpList.add(excelModelConfig);
                }
            }
            listMap.put(sheetOrder, tpList);
            InputStream inputStream = new FileInputStream(file);
            Workbook wb = new Workbook(inputStream);
            WorksheetCollection wsc = wb.getWorksheets();
            int a = wsc.getCount();
            for (int shIndex = 0; shIndex < wsc.getCount(); shIndex++) {
                int dataPointOrder = 0;
                Cells cells = wsc.get(shIndex).getCells();
                for (int i = 0; i < cells.getRows().getCount(); i++) {
                    for (int j = 0; j < cells.getColumns().getCount(); j++) {
                        try {
                            if ("{a}".equals(cells.get(i, j).getStringValue())) {
                                System.out.println(cells.get(i, j).getStringValue());
                                ExcelModelConfig excelModelConfig = listMap.get(shIndex).get(dataPointOrder);
                                setTb(cells, i, j, excelModelConfig, audTrm, prvdId);//先行后列
                                dataPointOrder++;
                            }
                        } catch (Exception e) {
                            logger.error("生成" + audTrm + "月" + prvdId + "省的EXCEL:" + excelName + "的第" + shIndex + "Sheet页第" + i + "行第" + j + "列出错");
                            continue;
                        }
                    }
                }
            }
            wb.save( targetPath+ "/" + excelName, com.aspose.cells.SaveFormat.XLSX);
        } catch (Exception e) {
            logger.error("生成" + audTrm + "月" + prvdId + "省的EXCEL" + excelName + "出错");
            e.printStackTrace();
            throw e;
        }

        return excelName;
    }
    private void setTb(Cells cells, int i, int j, ExcelModelConfig excelModelConfig, String
            audTrm, String prvdId) {
        Integer vH=excelModelConfig.getvH();

        if(excelModelConfig.getIsWideTb()!=null&&excelModelConfig.getIsWideTb()==0){//不是宽表
            List<Map<String, Object>> mapList1 = fileAutoService.executeSql(excelModelConfig.getQuerySql(),audTrm, null,prvdId);
            insertDataToSheet(cells,i,j,vH,mapList1,true,0);
            if(vH==0) cells.deleteRow(i);
            if(vH==1) cells.deleteColumn(j);
            return;
        }

        String audTrmStart = excelModelConfig.getWideTbMonth();
        Integer startCol = excelModelConfig.getWideTbCol();
        List<String> audTrmList = fileAutoService.getAudTrmListToSomeDate(audTrm, audTrmStart);
        Integer wideLength=0;//记录表宽度

        List<com.aspose.cells.Cell> titleList = new ArrayList<>();
        List<com.aspose.cells.Cell> pointList = new ArrayList<>();
        List<Double> widthList = new ArrayList<>();
        List<String> prvdOrderFirst = new ArrayList<>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("prvdId", prvdId);
        List<Map<String, Object>> prvdOrCtyList = mybatisDao.getList("commonMapper.getPrvdOrCtyName", paramMap);
        List<String> prvdOrCtyName = new ArrayList<>();//Arrays.asList("全国","全公司", "北京", "上海", "天津", "重庆", "贵州", "湖北", "陕西", "河北", "河南", "安徽", "福建", "青海", "甘肃", "浙江", "海南", "黑龙江", "江苏", "吉林", "宁夏", "山东", "山西", "新疆", "广东", "辽宁", "广西", "湖南", "江西", "内蒙古", "云南", "四川", "西藏");
        for(Map<String, Object> tMap:prvdOrCtyList){
            prvdOrCtyName.add(tMap.get("prvdOrCtyName").toString());
        }
        prvdOrCtyName.add("全国");
        for (int audTrmIndex = 0; audTrmIndex < audTrmList.size(); audTrmIndex++) {
            List<Map<String, Object>> mapList = fileAutoService.executeSql(excelModelConfig.getQuerySql(), audTrmList.get(audTrmIndex),prvdOrderFirst, prvdId);
            if (mapList.size() == 0) return;
            if(audTrmIndex==0){
                wideLength=mapList.get(0).size()-startCol;
                for(int titleIndex=startCol;titleIndex<=mapList.get(0).size();titleIndex++){
                    if(vH==0){
                        titleList.add(cells.get(i-1,titleIndex));
                        pointList.add(cells.get(i,titleIndex));
                        widthList.add(cells.getColumnWidth(titleIndex));
                    }
                    if(vH==1){
                        titleList.add(cells.get(titleIndex,j-1));
                        pointList.add(cells.get(titleIndex,j));
                        widthList.add(cells.getRowHeight(titleIndex));
                    }
                }
                if(vH==0){
                    cells.insertRow(i-1);
                    cells.merge(i-1,j,1,wideLength+startCol);
                    cells.get(i-1,j).getMergedRange().setValue(audTrmList.get(audTrmIndex));
                    cells.get(i-1,j).getMergedRange().setStyle(setStyle(cells));
                    i++;
                }
                if(vH==1){
                    cells.insertColumn(j-1);
                    cells.merge(i,j-1,wideLength+startCol,1);
                    cells.get(i,j-1).getMergedRange().setValue(audTrmList.get(audTrmIndex));
                    cells.get(i,j-1).getMergedRange().setStyle(setStyle(cells));
                    j++;
                }
                insertDataToSheet(cells,i,j,vH,mapList,true,0);


                if (prvdOrderFirst.size() == 0) {
                    for (Map<String, Object> tpMap : mapList) {
                        Iterator it = tpMap.values().iterator();
                        while (it.hasNext()) {
                            String value = it.next().toString();
                            if (prvdOrCtyName.contains(value)) {
                                prvdOrderFirst.add(value);
                                break;
                            }
                        }
                    }
                }

                continue;
            }
            int pointJ = j+startCol+wideLength*audTrmIndex;
            int pointI = i+startCol+wideLength*audTrmIndex;
            if(vH==0){
                cells.merge(i-2,pointJ,1,wideLength);
                cells.get(i-2,pointJ).getMergedRange().setValue(audTrmList.get(audTrmIndex));
                cells.get(i-2,pointJ).getMergedRange().setStyle(setStyle(cells));
            }
            if(vH==1){
                cells.merge(pointI,j-2,wideLength,1);
                cells.get(pointI,j-2).getMergedRange().setValue(audTrmList.get(audTrmIndex));
                cells.get(pointI,j-2).getMergedRange().setStyle(setStyle(cells));
            }
            for(int index=0;index<wideLength;index++){
                if(vH==0){
                    genCol(cells,pointJ+index);
                    com.aspose.cells.Cell cellT = titleList.get(index);
                    cells.get(i-1,pointJ+index).copy(cellT);

                    com.aspose.cells.Cell cellP = pointList.get(index);
                    cells.get(i,pointJ+index).copy(cellP);
                    cells.get(i,pointJ+index).setValue("");

                    cells.setColumnWidth(pointJ+index,widthList.get(index));

                }
                if(vH==1){

                    genRow(cells,pointI+index);
                    com.aspose.cells.Cell cellT = titleList.get(index);
                    cells.get(pointI+index,j-1).copy(cellT);

                    com.aspose.cells.Cell cellP = pointList.get(index);
                    cells.get(pointI+index,j).copy(cellP);
                    cells.get(pointI+index,j).setValue("");

                    cells.setRowHeight(pointI+index,widthList.get(index));

                }
            }
            if(vH==0) insertDataToSheet(cells,i,pointJ,vH,mapList,false,startCol);
            if(vH==1) insertDataToSheet(cells,pointI,j,vH,mapList,false,startCol);
        }
        if(vH==0) cells.deleteRow(i);
        if(vH==1) cells.deleteColumn(j);
    }

    private com.aspose.cells.Style setStyle(Cells cells){
        //Set top border style and color
        com.aspose.cells.Style style = cells.getStyle();
        com.aspose.cells.Border borderT = style.getBorders().getByBorderType(BorderType.TOP_BORDER);
        borderT.setLineStyle(CellBorderType.THIN);
        borderT.setColor(com.aspose.cells.Color.getBlack());
        com.aspose.cells.Border borderB = style.getBorders().getByBorderType(BorderType.BOTTOM_BORDER);
        borderB.setLineStyle(CellBorderType.THIN);
        borderB.setColor(com.aspose.cells.Color.getBlack());
        com.aspose.cells.Border borderL = style.getBorders().getByBorderType(BorderType.LEFT_BORDER);
        borderL.setLineStyle(CellBorderType.THIN);
        borderL.setColor(com.aspose.cells.Color.getBlack());
        com.aspose.cells.Border borderR = style.getBorders().getByBorderType(BorderType.RIGHT_BORDER);
        borderR.setLineStyle(CellBorderType.THIN);
        borderR.setColor(com.aspose.cells.Color.getBlack());
        style.setHorizontalAlignment(TextAlignmentType.CENTER);
        return style;
    }

    private void genRow(Cells cells,Integer index){
        if(cells.getRows().get(index)==null){
            cells.insertRow(index);
        }
    }
    private void genCol(Cells cells,Integer index){
        if(cells.getColumns().get(index)==null){
            cells.insertColumn(index);
        }
    }
    private void insertDataToSheet(Cells cells, int i, int j, Integer vH,List<Map<String, Object>> mapList,boolean isInsert,Integer startCol) {

        if (vH == 0) {
            int firstRow = i;
            int firstCol = j;
            for (Map<String, Object> map : mapList) {
                i++;
                j = firstCol;
                if(isInsert)
                    cells.insertRow(i);
                for (int index = 0; index < map.size(); index++) {
                    if(index<startCol)continue;
                    com.aspose.cells.Cell cell = cells.get(firstRow, j);
                    cells.get(i, j).copy(cell);
                    cells.get(i, j).setValue(map.get(getNumToChar(index)));
//                    com.aspose.cells.Style style = cells.get(firstRow, j).getStyle();
//                    cells.get(i, j).setValue(map.get(getNumToChar(index)));
//                    cells.get(i, j).setStyle(style);
                    j++;
                }
            }
            //cells.deleteRow(firstRow);
        }
        if (vH == 1) {
            int firstRow = i;
            int firstCol = j;
            for (Map<String, Object> map : mapList) {
                j++;
                i = firstRow;
                if(isInsert)
                    cells.insertColumn(j);
                for (int index = 0; index < map.size(); index++) {
                    if(index<startCol)continue;
                    com.aspose.cells.Cell cell = cells.get(i, firstCol);
                    cells.get(i, j).copy(cell);
                    cells.get(i, j).setValue(map.get(getNumToChar(index)));
                    i++;
                }
            }
            //cells.deleteColumn(firstCol);
        }

    }

    public String wordHandle(List<WordModelConfig> wordModelConfigList, String audTrm, String prvdId, String fileName, String fileCode,String versionId,String srcPath,String targetPath) throws Exception {
        setWordsLicense();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("audTrm", audTrm);
        paramMap.put("prvdName", Constants.MAP_PROVD_NAME.get(Integer.parseInt(prvdId)));
        String wordName = wfg.replaceParam(fileName, paramMap);
        File file = new File(srcPath + "/"+fileCode+"V"+versionId+"/"+fileName);
        try {
            InputStream inputStream = new FileInputStream(file);
            Document doc = new Document(inputStream);
            int count = 0;
            int i = 0;
            SectionCollection sc = doc.getSections();
            for (int sci = 0; sci < sc.getCount(); sci++) {
                NodeCollection ndc = sc.get(sci).getBody().getChildNodes();
                count = count + ndc.getCount();
                Paragraph pa = null;
                Table ta = null;
                titleOne=0;//已删除一级标题
                titleTwo=0;//已删除二级标题
                for (i = i + 0; i < count; i++) {
                    try {
                        WordModelConfig wordModelConfig = wordModelConfigList.get(i);
//                    System.out.println(ndc.get(i).getNodeType() + "---" + i);//8-段落  5-表格
                        if (wordModelConfig.getShowType() != null && wordModelConfig.getShowType() == 1) {//按显示控制sql的结果控制显示与否
                            List<Map<String, Object>> mapList = wfg.executeSql(wordModelConfig.getShowSql(), audTrm, prvdId);
                            if (mapList.isEmpty() || (mapList.size() > 0 && mapList.get(0).containsValue(0))) {
                                ndc.remove(ndc.get(i));
                                wordModelConfigList.remove(wordModelConfig);
                                i--;
                                count--;

                                if("1".equals(wordModelConfig.getTitleLevel()))titleOne--;
                                if("2".equals(wordModelConfig.getTitleLevel()))titleTwo--;
                                continue;
                            }
                        }
                        if (ndc.get(i).getNodeType() == 8) {
                            pa = (Paragraph) ndc.get(i);
//                            if (wordModelConfig.getQuerySql() == null || "".equals(wordModelConfig.getQuerySql()))
//                                continue;
                            handleParagraph(pa, wordModelConfig, audTrm, prvdId);
                        }
                        if (ndc.get(i).getNodeType() == 5) {
                            ta = (Table) ndc.get(i);
                            handleTable(doc, ta, wordModelConfig, audTrm, prvdId);
                        }

                        if (wordModelConfig.getMergeLast() != null && wordModelConfig.getMergeLast() == 1) {
                            String lastText = ((Paragraph) ndc.get(i - 1)).getRuns().get(0).getText();//上一段文本
                            String curText = ((Paragraph) ndc.get(i)).getRuns().get(0).getText();//当前段文本
                            ((Paragraph) ndc.get(i - 1)).getRuns().get(0).setText(lastText + curText);//当前段文本合并至上一段
                            ndc.remove(ndc.get(i));
                            wordModelConfigList.remove(wordModelConfig);
                            i--;
                            count--;
                        }
                    } catch (Exception e) {
                        logger.error("生成" + audTrm + "月" + prvdId + "省审计报告:" + wordName + " 的第" + sci + "章节第" + i + "段出错");
                        e.printStackTrace();
                        continue;
                    }
                }

            }

            doc.save( targetPath+ "/" + wordName, SaveFormat.DOCX);
        } catch (Exception e) {
            logger.error("生成" + audTrm + "月" + prvdId + "省" + "审计报告:" + wordName + "出错");
            e.printStackTrace();
            throw e;
        }
        return wordName;
    }

    private Table handleTable(Document doc, Table table, WordModelConfig wordModelConfig, String audTrm, String
            prvdId) {

        List<Map<String, Object>> mapList = wfg.executeSql(wordModelConfig.getQuerySql(), audTrm, prvdId);
        int i = 1;
        for (Map<String, Object> map : mapList) {
            Row row = new Row(doc);
            for (int j = 0; j < map.size(); j++) {
                Cell cell = (Cell) table.getRows().get(1).getCells().get(j).deepClone(true);//复用第一行的样式
                String text = map.get(getNumToChar(j)).toString();

                for (int ii = 0; ii < cell.getParagraphs().getCount(); ii++) {
                    for (int jj = 0; jj < cell.getParagraphs().get(ii).getRuns().getCount(); jj++) {
                        cell.getParagraphs().get(ii).getRuns().get(jj).setText("");
                    }
                }
                cell.getParagraphs().get(0).getRuns().get(0).setText(text);
                row.getCells().insert(j, cell);

            }
            table.getRows().insert(i, row);
            i++;
        }
        table.getRows().removeAt(i);
        return table;
    }

    private Paragraph handleParagraph(Paragraph paragraph, WordModelConfig wordModelConfig, String audTrm, String
            prvdId) {

        int runsCount = paragraph.getRuns().getCount();
        System.out.println(runsCount);
        String reslutText = null;
        String ss = paragraph.getRuns().get(0).getText();

        for (int i = 1; i < runsCount; i++) {
            ss = ss + paragraph.getRuns().get(1).getText();
            paragraph.getRuns().removeAt(1);
        }
        if("1".equals(wordModelConfig.getTitleLevel())){
            titleTwo=0;//经过1级标题后，二级标题减值重置
            for(int i=0;i>titleOne;i--){
                ss = subTitle(ss);
            }
        }
        if("2".equals(wordModelConfig.getTitleLevel())){
            for(int i=0;i>titleTwo;i--){
                ss = subTitle(ss);
            }
        }
        paragraph.getRuns().get(0).setText(ss);
        List<Map<String, Object>> mapList = wfg.executeSql(wordModelConfig.getQuerySql(), audTrm, prvdId);
        if (mapList.size() == 0) return paragraph;

        reslutText = wfg.replaceParamLoop(ss, mapList);

        paragraph.getRuns().get(0).setText(reslutText);

        return paragraph;
    }
    private String subTitle(String title){
        String orderTypeList1 []  ={"一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
        String orderTypeList2 []  ={"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String titleNum = "";
        String titleTxt = "";
        if(title.substring(0,3).contains("、")){
            titleTxt=title.substring(title.indexOf("、")+1);
            titleNum=title.substring(0,title.indexOf("、"));
        }

        if(title.substring(0,3).contains("）")){
            titleTxt=title.substring(title.indexOf("）")+1);
            titleNum = title.substring(title.indexOf("（")+1,title.indexOf("）"));
        }
        if("".equals(titleNum))return title;
        for(int i=0;i<orderTypeList1.length;i++){
            if(titleNum.equals(orderTypeList1[i])){
                titleNum=orderTypeList1[i-1];
            }
        }
        for(int i=0;i<orderTypeList2.length;i++){
            if(titleNum.equals(orderTypeList2[i])){
                titleNum=orderTypeList2[i-1];
            }
        }

        if(title.substring(0,3).contains("、")){
            return titleNum+"、"+titleTxt;
        }

        if(title.substring(0,3).contains("）")){
            return "（"+titleNum+"）"+titleTxt;
        }
        return title;
    }

    public String getNumToChar(int i) {

        char ch1 = 0, ch2 = 0;
        if (i < 26) {
            ch2 = (char) (i + 97);
            return String.valueOf(ch2);
        }
        ch2 = (char) (i % 26 + 97);
        if (i >= 26)
            ch1 = (char) (i / 26 - 1 + 97);
        return String.valueOf(ch1) + String.valueOf(ch2);
    }

    public void testWord() {
        setWordsLicense();
        try {
            File file = new File(propertyUtil.getPropValue("tmpModel") + "/安徽_201810_客户欠费审计报告1.docx");
            InputStream inputStream = new FileInputStream(file);
            Document doc = new Document(inputStream);
            doc.save(propertyUtil.getPropValue("tmpModel") + "/安徽_201810_客户欠费审计报告_处理后.docx",SaveFormat.DOCX);
        }catch(Exception e){

        }
    }
}
