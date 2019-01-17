package com.hpe.cmca.job;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.hpe.cmca.pojo.confInfo;
import com.hpe.cmca.service.FileAutoService;

import javax.swing.text.html.HTMLDocument;

@Service("NotiFile0000PmhzProcessor")
//@Controller
//@RequestMapping("/FileTest")
public class NotiFileAutoProcessor extends AbstractNotiFileProcessor {
    @Autowired
    private FileAutoService fileAutoService;
//	protected XSSFWorkbook	wb;
//	@Autowired
//	private JdbcTemplate jdbcTemplate = null;

    protected ArrayList<String> fileNameList = new ArrayList<String>();

    protected Logger logger = Logger.getLogger(this.getClass());

    private Map<String, CellStyle> mapStyle = new HashMap<String, CellStyle>();
//	@Autowired
//	protected FilePropertyPlaceholderConfigurer propertyUtil = null;


    public boolean generate() throws Exception {
        //work(month);

        return true;
    }

    @RequestMapping(value = "work")
    public void start() throws Exception {
        fileNameList = new ArrayList<String>();
//		this.setMonth("201711");
//		this.setFocusCd("6000");
//		this.setLocalDir("/data1/hp_web/caTmp");
        work(month, focusCd);

    }

	/*
	private confInfo getPoint(confInfo ci){

		if(ci.direction == 1){
			ci.vPointReal = ci.vPointLD + ci.vPointRelate;
			ci.hPointReal = ci.hPointLD + ci.hPointRelate;
		}

		if(ci.direction == 0){
			ci.vPointReal = ci.vPointRU + ci.vPointRelate;
			ci.hPointReal = ci.hPointRU + ci.hPointRelate;
		}
		fileAutoService.updateRealPoint(ci);
		return ci;

	}

	private confInfo updatePoint(confInfo ci){

		if(ci.vH == 1){//横行
			ci.vPointLD = ci.vPointReal + ci.dataList.size()+2; //上一数据块的左下角纵坐标
			ci.hPointLD = ci.hPointReal; //上一数据块的左下角横坐标
			ci.vPointRU = ci.vPointReal; //上一数据块的右上角纵坐标
			ci.hPointRU = ci.hPointReal + ci.cellContent.length; //上一数据块的右上角横坐标
		}
		if(ci.vH == 0){//纵行
			ci.vPointLD = ci.vPointReal + ci.cellContent.length+1; //上一数据块的左下角纵坐标
			ci.hPointLD = ci.hPointReal; //上一数据块的左下角横坐标
			ci.vPointRU = ci.vPointReal; //上一数据块的右上角纵坐标
			ci.hPointRU = ci.hPointReal + ci.dataList.size()+1; //上一数据块的右上角横坐标
		}
		int tmp = Integer.parseInt(ci.blockCode.substring(ci.blockCode.lastIndexOf('_')+1))+1;
		ci.blockCode = ci.blockCode.substring(0,ci.blockCode.lastIndexOf('_'))+"_"+tmp;

		fileAutoService.updatePoint(ci);

		return ci;
	}*/

    public confInfo getConfInfo(Map<String, Object> req, String audTrm) throws Exception {
        confInfo ci = new confInfo();
        if (req.get("block_code") == null) ci.blockCode = null;
        else ci.blockCode = (String) req.get("block_code");
        if (req.get("excel_code") == null) ci.excelCode = null;
        else ci.excelCode = (String) req.get("excel_code");
        if (req.get("excel_name") == null) ci.excelName = null;
        else ci.excelName = (String) req.get("excel_name");
        if (req.get("id") == null) ci.id = null;
        else ci.id = (Integer) req.get("id");
        if (req.get("sheet_code") == null) ci.sheetCode = null;
        else ci.sheetCode = (String) req.get("sheet_code");
        if (req.get("sheet_name") == null) ci.sheetName = null;
        else ci.sheetName = fileAutoService.replaceAudTrm((String) req.get("sheet_name"), audTrm);
        if (req.get("v_h") == null) ci.vH = null;
        else ci.vH = (Integer) req.get("v_h");
        if (req.get("direction") == null) ci.direction = null;
        else ci.direction = (Integer) req.get("direction");
        if (req.get("block_title") == null) ci.blockTitle = null;
        else ci.blockTitle = (String) req.get("block_title");
        if (req.get("is_widthtb") == null) ci.isWidthTb = null;
        else ci.isWidthTb = (Integer) req.get("is_widthtb");
        if (req.get("aud_trm_start") == null) ci.audTrmStart = null;
        else ci.audTrmStart = (String) req.get("aud_trm_start");
        if (req.get("has_audtrm") == null) ci.hasAudTrm = null;
        else ci.hasAudTrm = (Integer) req.get("has_audtrm");
        if (req.get("col_location") == null) ci.colLocation = null;
        else ci.colLocation = (Integer) req.get("col_location");
        if (req.get("col_type") == null) ci.colType = null;
        else ci.colType = ((String) req.get("col_type")).split(",");
        if (req.get("cell_content") == null) ci.cellContent = null;
        else ci.cellContent = ((String) req.get("cell_content")).split(",");
        if (req.get("query_sql") == null) ci.querySql = null;
        else ci.querySql = (String) req.get("query_sql");
        if (req.get("is_null_set") == null) ci.isNullSet = null;
        else ci.isNullSet = (Integer) req.get("is_null_set");
        if (req.get("default_val") == null) ci.defaultVal = null;
        else ci.defaultVal = (String) req.get("default_val");
        if (req.get("spe_null_col") == null) ci.speNullCol = null;
        else ci.speNullCol = ((String) req.get("spe_null_col")).split(",");
        if (req.get("spe_default_val") == null) ci.speDefaultVal = null;
        else ci.speDefaultVal = (String) req.get("spe_default_val");
        if (req.get("word_font") == null) ci.wordFont = null;
        else ci.wordFont = (String) req.get("word_font");
        if (req.get("word_size") == null) ci.wordSize = null;
        else ci.wordSize = (String) req.get("word_size");
        if (req.get("word_color") == null) ci.wordColor = null;
        else ci.wordColor = (String) req.get("word_color");
        if (req.get("v_point_relate") == null) ci.vPointRelate = null;
        else ci.vPointRelate = (Integer) req.get("v_point_relate");
        if (req.get("h_point_relate") == null) ci.hPointRelate = null;
        else ci.hPointRelate = (Integer) req.get("h_point_relate");
        if (req.get("v_point_l_d") == null) ci.vPointLD = null;
        else ci.vPointLD = (Integer) req.get("v_point_l_d");
        if (req.get("h_point_l_d") == null) ci.hPointLD = null;
        else ci.hPointLD = (Integer) req.get("h_point_l_d");
        if (req.get("v_point_r_u") == null) ci.vPointRU = null;
        else ci.vPointRU = (Integer) req.get("v_point_r_u");
        if (req.get("h_point_r_u") == null) ci.hPointRU = null;
        else ci.hPointRU = (Integer) req.get("h_point_r_u");
        if (req.get("v_point_real") == null) ci.vPointReal = null;
        else ci.vPointReal = (Integer) req.get("v_point_real");
        if (req.get("h_point_real") == null) ci.hPointReal = null;
        else ci.hPointReal = (Integer) req.get("h_point_real");
        if (req.get("query_sql") == null) ci.dataList = null;
        else ci.dataList = fileAutoService.executeSql(ci.querySql, audTrm, null,"10000");

        if (req.get("aud_trm_op") == null) ci.audTrmOp = null;
        else ci.audTrmOp = (Integer) req.get("aud_trm_op");
        if (req.get("complex_pattern") == null) ci.complexPattern = null;
        else ci.complexPattern = (Integer) req.get("complex_pattern");
        if (req.get("complex_title") == null) ci.complexTitle = null;
        else ci.complexTitle = ((String) req.get("complex_title")).split("#");
        //ci.complexTitle = fileAutoService.replaceAudTrm(((String) req.get("complex_title")), fileAutoService.dateMinusMonth(audTrm, ci.audTrmOp)).split("#");

        if (req.get("text_type") == null) ci.textType = null;
        else ci.textType = (Integer) req.get("text_type");
        if (req.get("col_width") == null) ci.colWidth = null;
        else ci.colWidth = ((String) req.get("col_width")).split(",");
        if (req.get("merge_cell") == null) ci.mergeCell = null;
        else ci.mergeCell = ((String) req.get("merge_cell")).split("\\|");
        return ci;
    }


    public void work(String audTrm, String focusCd) {
        String srcAudTrm = audTrm;
        try {
            List<Map<String, Object>> reqList = fileAutoService.getSJZLGenList(focusCd);
            if (reqList.isEmpty()) {
                logger.error("数据库无配置信息");
                return;
            }
            Map<String, SXSSFWorkbook> wbMap = new HashMap<String, SXSSFWorkbook>();
            Map<String, String> nameMap = new HashMap<String, String>();
            confInfo ci = new confInfo();
            for (Map<String, Object> req : reqList) {
                try {
                    audTrm = srcAudTrm;
                    ci = getConfInfo(req, audTrm);
                    audTrm = fileAutoService.dateMinusMonth(audTrm, ci.audTrmOp);
                    if (!wbMap.containsKey(ci.excelCode)) {
                        SXSSFWorkbook wb = new SXSSFWorkbook(500);
                        wbMap.put(ci.excelCode, wb);

//					Map<String,Object> paramMap = new HashMap<String,Object>();
//					paramMap.put("audTrm", audTrm);
                        nameMap.put(ci.excelCode, fileAutoService.replaceAudTrm(ci.excelName, audTrm));
                    }
                    //

                    writeFile(wbMap.get(ci.excelCode), ci, audTrm);
                } catch (Exception e) {
                    logger.error("数据块编码为" + ci.blockCode + "的排名汇总配置生成异常：" + e.getMessage(), e);
                    fileAutoService.updateStatusById(ci.id, 2);
                    continue;
                }
                //updatePoint(ci);
            }
            generateSingle(wbMap, nameMap);
            //fileAutoService.updateStatusByFocusCd(focusCd,1);//生成成功将该专题下所有专题状态置为1
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("文件生成异常：" + e.getMessage(), e);
            fileAutoService.updateStatusByFocusCd(focusCd, 2);//生成异常将该专题下所有专题状态置为2
            return;
        }

    }

    public Row genRow(Sheet sh, int arg0) {
        Row r = null;
        r = sh.getRow(arg0);
        if (r == null)
            r = sh.createRow(arg0);
        return r;
    }

    public Cell genCell(Row row, int arg0) {
        Cell c = null;
        c = row.getCell(arg0);
        if (c == null)
            c = row.createCell(arg0);
        return c;
    }

    public void writeFile(SXSSFWorkbook wb, confInfo ci, String audTrmEnd) {
        mapStyle.clear();
        mapStyle.put("style0", getStyle(wb, 0));
        mapStyle.put("style1", getStyle(wb, 1));
        mapStyle.put("style2", getStyle(wb, 2));
        mapStyle.put("style3", getStyle(wb, 3));
        mapStyle.put("style31", getStyle(wb, 31));
        mapStyle.put("style4", getStyle(wb, 4));
        mapStyle.put("style5", getStyle(wb, 5));
        mapStyle.put("style6", getStyle(wb, 6));

        mapStyle.put("style7", getStyle(wb, 7));
        mapStyle.put("style8", getStyle(wb, 8));
        mapStyle.put("style9", getStyle(wb, 9));
        mapStyle.put("style10", getStyle(wb, 10));
        mapStyle.put("style11", getStyle(wb, 11));

        int hDistince = 0;//标题与表格的距离 -横行
        int vDistince = 0;//标题与表格的距离 -纵行

        Sheet sh = null;
        sh = wb.getSheet(ci.sheetName);
        if (sh == null)
            sh = wb.createSheet(ci.sheetName);

        if (ci.blockTitle != null) {
            genRow(sh, ci.vPointReal).createCell(ci.hPointReal).setCellValue(ci.blockTitle);
            genRow(sh, ci.vPointReal).getCell(ci.hPointReal).setCellStyle(mapStyle.get("style0"));
            sh.addMergedRegion(new CellRangeAddress(ci.vPointReal, ci.vPointReal, ci.hPointReal, ci.hPointReal + ci.blockTitle.length() / 3));
        } else {
            hDistince = 0;//无标题
        }
        List<String> prvdOrderFirst = new ArrayList<>();
        List<String> prvdName = Arrays.asList("全国","全公司", "北京", "上海", "天津", "重庆", "贵州", "湖北", "陕西", "河北", "河南", "安徽", "福建", "青海", "甘肃", "浙江", "海南", "黑龙江", "江苏", "吉林", "宁夏", "山东", "山西", "新疆", "广东", "辽宁", "广西", "湖南", "江西", "内蒙古", "云南", "四川", "西藏");

        List<String> audTrmList = new ArrayList<String>();
        if (ci.isWidthTb != null && ci.isWidthTb == 1) {
            if (Integer.parseInt(audTrmEnd) < Integer.parseInt(ci.audTrmStart)) return;
            audTrmList = fileAutoService.getAudTrmListToSomeDate(audTrmEnd, ci.audTrmStart);
            if (prvdOrderFirst.size() == 0) {
                for (Map<String, Object> tpMap : ci.dataList) {
                    Iterator it = tpMap.values().iterator();
                    while (it.hasNext()) {
                        String value = it.next().toString();
                        if (prvdName.contains(value)) {
                            prvdOrderFirst.add(value);
                            break;
                        }
                    }
                }
            }
        } else
            audTrmList.add(audTrmEnd);
        int audTrmDistince = 0;//计算宽表的当前月的某列与下一月份对应列的距离
        int startIndex = 0;//开始填充的各列的初始位置，即从第几列开始填，默认从第一列，但是宽表第二月可能会少开头的字段，例如从第3列开始填


        for (int audTrmIndex = 0; audTrmIndex < audTrmList.size(); audTrmIndex++) {
            String audTrm = audTrmList.get(audTrmIndex);
            if (audTrmIndex > 0) {
                if (ci.colLocation == null) ci.setColLocation(0);
                audTrmDistince = audTrmDistince + (ci.colWidth != null ? ci.colWidth.length : ci.cellContent.length) - ci.colLocation;//宽表的各个月公共数据宽度（除去公共列）
                ci.dataList = fileAutoService.executeSql(ci.querySql, audTrm, prvdOrderFirst,"10000");
                startIndex = ci.colLocation;
            }
            if (ci.blockTitle != null)
                hDistince = 1;//标题与表格的距离 -横行
            else
                hDistince = 0;
            vDistince = 0;//标题与表格的距离 -纵行
            if (ci.hasAudTrm != null && ci.hasAudTrm == 1) {
                if (ci.vH != null && ci.vH == 1) {//横行
                    genRow(sh, ci.vPointReal + hDistince).createCell(ci.hPointReal + ci.colLocation + audTrmDistince).setCellValue(fileAutoService.getAudTrmChinese(audTrm));
                    genRow(sh, ci.vPointReal + hDistince).getCell(ci.hPointReal + ci.colLocation + audTrmDistince).setCellStyle(mapStyle.get("style3"));
                    for (int cellIndex = 1; cellIndex < (ci.colWidth != null ? ci.colWidth.length : ci.cellContent.length) - ci.colLocation; cellIndex++) {
                        genRow(sh, ci.vPointReal + hDistince).createCell(ci.hPointReal + ci.colLocation + cellIndex + audTrmDistince).setCellStyle(mapStyle.get("style3"));
                    }//将合并的单元格全部设置格式
                    sh.addMergedRegion(new CellRangeAddress(ci.vPointReal + hDistince, ci.vPointReal + hDistince, ci.hPointReal + ci.colLocation + audTrmDistince, ci.hPointReal + (ci.colWidth != null ? ci.colWidth.length : ci.cellContent.length) - 1 + audTrmDistince));
                }
                if (ci.vH != null && ci.vH == 0) {//纵行
                    genRow(sh, ci.vPointReal + 1 + ci.colLocation + audTrmDistince).createCell(ci.hPointReal).setCellValue(fileAutoService.getAudTrmChinese(audTrm));
                    genRow(sh, ci.vPointReal + 1 + ci.colLocation + audTrmDistince).getCell(ci.hPointReal).setCellStyle(mapStyle.get("style3"));
                    for (int cellIndex = 1; cellIndex < (ci.colWidth != null ? ci.colWidth.length : ci.cellContent.length) - ci.colLocation; cellIndex++) {
                        genRow(sh, ci.vPointReal + 1 + ci.colLocation + audTrmDistince + cellIndex).createCell(ci.hPointReal).setCellStyle(mapStyle.get("style3"));
                    }//将合并的单元格全部设置格式
                    sh.addMergedRegion(new CellRangeAddress(ci.vPointReal + ci.colLocation + 1 + audTrmDistince, ci.vPointReal + (ci.colWidth != null ? ci.colWidth.length : ci.cellContent.length) - 1 + 1 + audTrmDistince, ci.hPointReal, ci.hPointReal));
                }
                hDistince++;
                vDistince++;
            }
            if (ci.complexPattern != null && ci.complexPattern == 0 && ci.cellContent != null && ci.cellContent.length > 1) {
                for (int i = startIndex; i < (ci.colWidth != null ? ci.colWidth.length : ci.cellContent.length); i++) {//例如，第二个月从第二列开始填充
                    if (ci.vH == 1) {//横行
                        if (ci.hasAudTrm == 1 && i < ci.colLocation) {
                            genRow(sh, ci.vPointReal + hDistince - 1).createCell(i + ci.hPointReal + audTrmDistince).setCellValue(ci.cellContent[i]);
                            genRow(sh, ci.vPointReal + hDistince - 1).getCell(i + ci.hPointReal + audTrmDistince).setCellStyle(mapStyle.get("style1"));
                            genRow(sh, ci.vPointReal + hDistince).createCell(i + ci.hPointReal + audTrmDistince).setCellStyle(mapStyle.get("style1"));
                            sh.addMergedRegion(new CellRangeAddress(ci.vPointReal + hDistince - 1, ci.vPointReal + hDistince, i + ci.hPointReal + audTrmDistince, i + ci.hPointReal + audTrmDistince));
                        } else {
                            genRow(sh, ci.vPointReal + hDistince).createCell(i + ci.hPointReal + audTrmDistince).setCellValue(ci.cellContent[i]);
                            genRow(sh, ci.vPointReal + hDistince).getCell(i + ci.hPointReal + audTrmDistince).setCellStyle(mapStyle.get("style1"));
                        }

                    }
                    if (ci.vH == 0) {//纵行
                        if (ci.hasAudTrm == 1 && i < ci.colLocation) {
                            genRow(sh, i + ci.vPointReal + audTrmDistince + 1).createCell(ci.hPointReal).setCellValue(ci.cellContent[i]);
                            genRow(sh, i + ci.vPointReal + audTrmDistince + 1).getCell(ci.hPointReal).setCellStyle(mapStyle.get("style1"));
                            genRow(sh, i + ci.vPointReal + audTrmDistince + 1).createCell(ci.hPointReal + vDistince).setCellStyle(mapStyle.get("style1"));
                            sh.addMergedRegion(new CellRangeAddress(i + ci.vPointReal + audTrmDistince + 1, i + ci.vPointReal + audTrmDistince + 1, ci.hPointReal, ci.hPointReal + vDistince));
                        } else {
                            genRow(sh, i + ci.vPointReal + audTrmDistince + 1).createCell(ci.hPointReal + vDistince).setCellValue(ci.cellContent[i]);
                            genRow(sh, i + ci.vPointReal + audTrmDistince + 1).getCell(ci.hPointReal + vDistince).setCellStyle(mapStyle.get("style1"));
                        }
                    }
                }
                hDistince++;
                vDistince++;
            }
            if (ci.complexPattern != null && ci.complexPattern == 1 && ci.complexTitle != null) {
                for (String c : ci.complexTitle) {
                    c = fileAutoService.replaceAudTrm(c, audTrm);
                    List<String> l = new ArrayList<String>(Arrays.asList(c.split("\\$")));
                    for (int i = startIndex; i < l.size(); i++) {
                        CellStyle style = mapStyle.get("style1");
                        if (ci.textType != null && ci.textType == 1)
                            style = mapStyle.get("style7");
                        if (ci.textType != null && ci.textType == 2)
                            style = mapStyle.get("style8");
                        if (ci.textType != null && ci.textType == 3)
                            style = mapStyle.get("style9");
                        if (ci.textType != null && ci.textType == 4)
                            style = mapStyle.get("style10");
                        if (ci.textType != null && ci.textType == 5)
                            style = mapStyle.get("style11");

                        if (ci.vH == 1) {//横行
                            if (ci.hasAudTrm != null && ci.colLocation != null && ci.hasAudTrm == 1 && i < ci.colLocation) {
                                genRow(sh, ci.vPointReal + hDistince - 1).createCell(i + ci.hPointReal + audTrmDistince).setCellValue(l.get(i));
                                genRow(sh, ci.vPointReal + hDistince - 1).getCell(i + ci.hPointReal + audTrmDistince).setCellStyle(style);
                                genRow(sh, ci.vPointReal + hDistince).createCell(i + ci.hPointReal + audTrmDistince).setCellStyle(style);
                                sh.addMergedRegion(new CellRangeAddress(ci.vPointReal + hDistince - 1, ci.vPointReal + hDistince, i + ci.hPointReal + audTrmDistince, i + ci.hPointReal + audTrmDistince));
                            } else {
                                genRow(sh, ci.vPointReal + hDistince).createCell(i + ci.hPointReal + audTrmDistince).setCellValue(l.get(i));
                                genRow(sh, ci.vPointReal + hDistince).getCell(i + ci.hPointReal + audTrmDistince).setCellStyle(style);
                                sh.setColumnWidth(i + ci.hPointReal + audTrmDistince, Integer.parseInt(ci.colWidth[i]) * 1000);
                            }
                            if (ci.textType != null && ci.textType == 1)//普通文本
                                sh.getRow(ci.vPointReal + hDistince).setHeightInPoints(30);
                            if (ci.textType != null && ci.textType == 3)//大标题
                                sh.getRow(ci.vPointReal + hDistince).setHeightInPoints(30);
                        }
                        if (ci.vH == 0) {//纵行
                            if (ci.hasAudTrm != null && ci.colLocation != null && ci.hasAudTrm == 1 && i < ci.colLocation) {
                                genRow(sh, i + ci.vPointReal + audTrmDistince + 1).createCell(ci.hPointReal).setCellValue(l.get(i));
                                genRow(sh, i + ci.vPointReal + audTrmDistince + 1).getCell(ci.hPointReal).setCellStyle(style);
                                genRow(sh, i + ci.vPointReal + audTrmDistince + 1).createCell(ci.hPointReal + vDistince).setCellStyle(style);
                                sh.addMergedRegion(new CellRangeAddress(i + ci.vPointReal + audTrmDistince + 1, i + ci.vPointReal + audTrmDistince + 1, ci.hPointReal, ci.hPointReal + vDistince));
                            } else {
                                genRow(sh, i + ci.vPointReal + audTrmDistince + 1).createCell(ci.hPointReal + vDistince).setCellValue(l.get(i));
                                genRow(sh, i + ci.vPointReal + audTrmDistince + 1).getCell(ci.hPointReal + vDistince).setCellStyle(style);

                                sh.getRow(i + ci.vPointReal + audTrmDistince + 1).setHeightInPoints(Integer.parseInt(ci.colWidth[i]) * 10);
                            }
                            if (ci.textType != null && ci.textType == 1)
                                sh.setColumnWidth(ci.hPointReal + vDistince, 3 * 1000);

                            if (ci.textType != null && ci.textType == 3)
                                sh.setColumnWidth(ci.hPointReal + vDistince, 3 * 1000);
                        }

                    }
                    hDistince++;
                    vDistince++;
                }
                if (ci.mergeCell != null) {
                    for (String m : ci.mergeCell) {
                        List<String> l = new ArrayList<String>(Arrays.asList(m.split(",")));
                        if (ci.vH == 1) {
                            if (Integer.parseInt(l.get(0)) == 0) {
                                genCell(genRow(sh, Integer.parseInt(l.get(1))), Integer.parseInt(l.get(2)) + audTrmDistince);
                                genCell(genRow(sh, Integer.parseInt(l.get(1))), Integer.parseInt(l.get(3)) + audTrmDistince);
                                sh.addMergedRegion(new CellRangeAddress(Integer.parseInt(l.get(1)), Integer.parseInt(l.get(1)), Integer.parseInt(l.get(2)) + audTrmDistince, Integer.parseInt(l.get(3)) + audTrmDistince));
                            }
                            if (Integer.parseInt(l.get(0)) == 1) {
                                genCell(genRow(sh, Integer.parseInt(l.get(2))), Integer.parseInt(l.get(1)) + audTrmDistince);
                                genCell(genRow(sh, Integer.parseInt(l.get(3))), Integer.parseInt(l.get(1)) + audTrmDistince);
                                sh.addMergedRegion(new CellRangeAddress(Integer.parseInt(l.get(2)), Integer.parseInt(l.get(3)), Integer.parseInt(l.get(1)) + audTrmDistince, Integer.parseInt(l.get(1)) + audTrmDistince));
                            }
                        }
                        if (ci.vH == 0) {
                            if (Integer.parseInt(l.get(0)) == 0) {
                                genCell(genRow(sh, Integer.parseInt(l.get(1)) + audTrmDistince), Integer.parseInt(l.get(2)));
                                genCell(genRow(sh, Integer.parseInt(l.get(1)) + audTrmDistince), Integer.parseInt(l.get(3)));
                                sh.addMergedRegion(new CellRangeAddress(Integer.parseInt(l.get(1)) + audTrmDistince, Integer.parseInt(l.get(1)) + audTrmDistince, Integer.parseInt(l.get(2)), Integer.parseInt(l.get(3))));
                            }
                            if (Integer.parseInt(l.get(0)) == 1) {
                                genCell(genRow(sh, Integer.parseInt(l.get(2)) + audTrmDistince), Integer.parseInt(l.get(1)));
                                genCell(genRow(sh, Integer.parseInt(l.get(3)) + audTrmDistince), Integer.parseInt(l.get(1)));
                                sh.addMergedRegion(new CellRangeAddress(Integer.parseInt(l.get(2)) + audTrmDistince, Integer.parseInt(l.get(3)) + audTrmDistince, Integer.parseInt(l.get(1)), Integer.parseInt(l.get(1))));
                            }
                        }

                    }
                }
            }
            if (ci.dataList != null) {
                for (int i = 0; i < ci.dataList.size(); i++) {
                    for (int j = startIndex; j < (ci.colWidth != null ? ci.colWidth.length : ci.cellContent.length); j++) {
                        String str = fileAutoService.getNumToChar(j);
                        if (ci.vH == 1) {//横行
                            Cell cell = genRow(sh, i + ci.vPointReal + hDistince).createCell(j + ci.hPointReal + audTrmDistince);
                            Object cellContent = null;
                            try {
                                cellContent = ci.dataList.get(i) == null ? null : ci.dataList.get(i).get(str);
                            } catch (Exception e) {
                                //							cellContent="-";
                            }
                            if (ci.isNullSet != null && ci.isNullSet == 1 && cellContent == null) {
                                cell.setCellStyle(mapStyle.get("style3"));
                                cell.setCellValue(ci.defaultVal);
                                if (ci.speNullCol != null && Arrays.asList(ci.speNullCol).contains(str)) {
                                    cell.setCellValue(ci.speDefaultVal);
                                }
                                continue;
                            }

                            if (cellContent instanceof Integer) {
                                cell.setCellStyle(mapStyle.get("style6"));
                                cell.setCellValue((Integer) cellContent);
                                continue;
                            }
                            if (cellContent instanceof Double || cellContent instanceof BigDecimal) {
                                Double tpCellV = cellContent instanceof BigDecimal ? ((BigDecimal) cellContent).doubleValue() : (Double) cellContent;

                                if (ci.colType != null && Arrays.asList(ci.colType).contains(str)) {
                                    cell.setCellValue(convert(tpCellV * 100, "%"));
                                    cell.setCellStyle(mapStyle.get("style31"));
                                } else {
                                    cell.setCellValue(tpCellV);
                                    cell.setCellStyle(mapStyle.get("style5"));
                                }
                                continue;
                            }
                            cell.setCellStyle(mapStyle.get("style3"));
                            cell.setCellValue((String) cellContent);
                        }
                        if (ci.vH == 0) {//纵行
                            //genRow(sh,j+ci.vPointReal).createCell(i+ci.hPointReal+2).setCellValue(ci.dataList.get(i).get(str).toString());
                            Cell cell = genRow(sh, j + ci.vPointReal + audTrmDistince + 1).createCell(i + ci.hPointReal + vDistince);
                            Object cellContent = ci.dataList.get(i) == null ? null : ci.dataList.get(i).get(str);
                            if (ci.isNullSet != null && ci.isNullSet == 1 && cellContent == null) {
                                cell.setCellStyle(mapStyle.get("style3"));
                                cell.setCellValue(ci.defaultVal);
                                if (ci.speNullCol != null && Arrays.asList(ci.speNullCol).contains(str)) {
                                    cell.setCellValue(ci.speDefaultVal);
                                }
                                continue;
                            }
                            if (cellContent instanceof Integer) {
                                cell.setCellStyle(mapStyle.get("style6"));
                                cell.setCellValue((Integer) cellContent);
                                continue;
                            }
                            if (cellContent instanceof Double || cellContent instanceof BigDecimal) {
                                Double tpCellV = cellContent instanceof BigDecimal ? ((BigDecimal) cellContent).doubleValue() : (Double) cellContent;

                                if (ci.colType != null && Arrays.asList(ci.colType).contains(str)) {//把需要按百分比展示的列按百分比展示
                                    cell.setCellValue(convert(tpCellV * 100, "%"));
                                    cell.setCellStyle(mapStyle.get("style31"));
                                } else {
                                    cell.setCellValue(tpCellV);
                                    cell.setCellStyle(mapStyle.get("style5"));
                                }

                                continue;
                            }
                            cell.setCellStyle(mapStyle.get("style3"));
                            cell.setCellValue((String) cellContent);
                        }
                    }
                }
            }
            //if(ci.hasAudTrm==0)break;
        }
    }

    private CellStyle getStyle(SXSSFWorkbook wb, int index) {
        CellStyle style = wb.createCellStyle();
        if (index == 0) {
            style.setAlignment(CellStyle.ALIGN_LEFT);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 14);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
        }
        if (index == 1) {
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 12);
            style.setFont(font);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setWrapText(true);
            //style.setFillBackgroundColor(HSSFColor.BLUE.index);
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        if (index == 3) {
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 12);
            style.setFont(font);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setWrapText(true);
        }
        if (index == 31) {
            style.setAlignment(CellStyle.ALIGN_RIGHT);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 12);
            style.setFont(font);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setWrapText(true);
        }
        if (index == 4) {
            style.setAlignment(CellStyle.ALIGN_RIGHT);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            //font.setFontName("Arial Unicode MS");
            font.setFontHeightInPoints((short) 12);
            style.setFont(font);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
        }
        if (index == 5) {
            style.setAlignment(CellStyle.ALIGN_RIGHT);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            //font.setFontName("Arial Unicode MS");
            font.setFontHeightInPoints((short) 12);
            style.setFont(font);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setWrapText(true);
            style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
        }
        if (index == 6) {
            style.setAlignment(CellStyle.ALIGN_RIGHT);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            //font.setFontName("Arial Unicode MS");
            font.setFontHeightInPoints((short) 12);
            style.setFont(font);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
        }

        if (index == 7) {//普通文本
            style.setAlignment(CellStyle.ALIGN_LEFT);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 12);
            style.setFont(font);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setWrapText(true);
        }
        if (index == 8) {//列标题
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 12);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setFont(font);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setWrapText(true);
            //style.setFillBackgroundColor(HSSFColor.BLUE.index);
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        if (index == 9) {//大标题
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 16);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setFont(font);
//            style.setBorderBottom(CellStyle.BORDER_THIN);
//            style.setBorderLeft(CellStyle.BORDER_THIN);
//            style.setBorderRight(CellStyle.BORDER_THIN);
//            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setWrapText(true);
        }
        if (index == 10) {//备注
            style.setAlignment(CellStyle.ALIGN_LEFT);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 12);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setFont(font);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setWrapText(true);
        }
        if (index == 11) {//列标题1
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 12);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setFont(font);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setWrapText(true);
        }


        return style;
    }

    //如小于0.01%，则小数点保留到小数后出现数字为止
    public String convert(Double db, String isPer) {
        DecimalFormat df3 = new DecimalFormat("###,###,###,##0.0000000000");
        String param = df3.format(db);
        StringBuilder sb = new StringBuilder();
        Integer index = 0;
        for (int i = param.indexOf(".") + 1; i < param.length(); i++) {
            String ch = param.substring(i, i + 1);
            if ("0".equals(ch))
                index++;
            else
                break;
        }
        for (int j = 0; j < index + 1; j++) {//有数字为止
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
            result = result.substring(0, result.length() - 1);
        }
        return "%".equals(isPer) ? result + "%" : result;//df.format(d)+"%";
    }

    /*
        private CellStyle getStyle0(SXSSFWorkbook	wb){
            CellStyle style = wb.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_LEFT);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short)14);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
            return style;
        }

        private CellStyle getStyle1(SXSSFWorkbook wb){
            CellStyle style = wb.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short)12);
            style.setFont(font);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setWrapText(true);
            //style.setFillBackgroundColor(HSSFColor.BLUE.index);
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            return style;
        }

        private CellStyle getStyle2(SXSSFWorkbook	wb){
            CellStyle style = wb.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short)12);
            style.setFont(font);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setWrapText(true);
            //style.setFillBackgroundColor(HSSFColor.BLUE.index);
            style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            return style;
        }

        private CellStyle getStyle3(SXSSFWorkbook	wb){
            CellStyle style = wb.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short)12);
            style.setFont(font);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setWrapText(true);
            return style;
        }
        private CellStyle getStyle4(SXSSFWorkbook	wb){
            CellStyle style0 = wb.createCellStyle();
            style0.setAlignment(CellStyle.ALIGN_CENTER);
            style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short)12);
            style0.setFont(font);
            style0.setBorderBottom(CellStyle.BORDER_THIN);
            style0.setBorderLeft(CellStyle.BORDER_THIN);
            style0.setBorderRight(CellStyle.BORDER_THIN);
            style0.setBorderTop(CellStyle.BORDER_THIN);
            style0.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
            return style0;
        }

        private CellStyle getStyle5(SXSSFWorkbook	wb){
            CellStyle style = wb.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short)12);
            style.setFont(font);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setWrapText(true);
            style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
            return style;
        }

        private CellStyle getStyle6(SXSSFWorkbook	wb){
            CellStyle style0 = wb.createCellStyle();
            style0.setAlignment(CellStyle.ALIGN_CENTER);
            style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short)12);
            style0.setFont(font);
            style0.setBorderBottom(CellStyle.BORDER_THIN);
            style0.setBorderLeft(CellStyle.BORDER_THIN);
            style0.setBorderRight(CellStyle.BORDER_THIN);
            style0.setBorderTop(CellStyle.BORDER_THIN);
            style0.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
            return style0;
        }

        public void generateSubject(XSSFWorkbook wb, confInfo ci,String fileName) throws Exception {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(this.getLocalPath(fileName));
                wb.write(out);
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw e;
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    throw e;
                }
            }
            //	uploadFile(this.getLocalPath(fileName), getFtpPath( ci.audTrm, String.valueOf(ci.subjectId*1000)));

        }*/
    public ArrayList<String> getFileNameList() {

        return fileNameList;
    }

    public void setFileNameList(String fileName) {

        fileNameList.add(fileName);
    }

    public void generateSingle(Map<String, SXSSFWorkbook> wbMap, Map<String, String> nameMap) throws Exception {
        fileNameList.clear();
        for (String wbCode : wbMap.keySet()) {

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(getLocalDir() + "/" + nameMap.get(wbCode) + ".xlsx");
                wbMap.get(wbCode).write(out);
                setFileNameList(nameMap.get(wbCode) + ".xlsx");
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw e;
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }
/*
	public XSSFWorkbook openExcel(String fileName,String sheetName) throws Exception {
		FileInputStream fIP = null;
		File file = null;
		File file1 = null;
		XSSFWorkbook workbook = null;
		try {
			file = new File(this.getLocalPath(fileName));
			if (!file.exists()){
				XSSFWorkbook wb = new XSSFWorkbook();
				wb.createSheet(sheetName);
				generate(wb,fileName);
				file1 = new File(this.getLocalPath(fileName));
				fIP = new FileInputStream(file1);
			}else {
			fIP = new FileInputStream(file);
			}
			// Get the workbook instance for XLSX file
			workbook = new XSSFWorkbook(fIP);
		}
		catch (Exception e) {
			throw e;
		} finally {
			fIP.close();
		}
		return workbook;
	}

	public void generate(XSSFWorkbook wb, String fileName) throws Exception {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(this.getLocalPath(fileName));
			wb.write(out);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				throw e;
			}
		}
	}

	private FtpUtil initFtp() {
		String isTransferFile = propertyUtil.getPropValue("isTransferFile");
		if (!"true".equalsIgnoreCase(isTransferFile)) {
			return null;
		}
		FtpUtil ftpTool = new FtpUtil();
		String ftpServer = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpServer"));
		String ftpPort = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpPort"));
		String ftpUser = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpUser"));
		String ftpPass = StringUtils.trimToEmpty(propertyUtil
				.getPropValue("ftpPass"));
		ftpTool.initClient(ftpServer, ftpPort, ftpUser, ftpPass);
		return ftpTool;
	}

	public void uploadFile(String filePath, String ftpPath) throws Exception{
		FtpUtil ftpTool = null;
		try {
			ftpTool = initFtp();
			if (ftpTool == null) {
				return;
			}
			ftpTool.uploadFile(new File(filePath), ftpPath);
		} catch (Exception e){
			logger.error("SJZL uploadFile>>>"+e.getMessage(),e);
			throw e;
			//e.printStackTrace();

		} finally {
			if (ftpTool != null) {
				ftpTool.disConnect();
			}
		}
	}

	public String getFtpPath(String audTrm,String focusCd){
		String tempDir = propertyUtil.getPropValue("ftpPath");
		String finalPath = FileUtil.buildFullFilePath(tempDir, buildRelativePath(audTrm, focusCd));
		FileUtil.mkdirs(finalPath);
		return finalPath;// + "/" + zipFileName;
	}
	protected String buildRelativePath(String audTrm, String focusCd) {
		String subjectId = focusCd.substring(0,1);
		StringBuilder path = new StringBuilder();
		path.append(audTrm).append("/").append(subjectId).append("/").append(focusCd);
		return path.toString();
	}
	protected String buildDownloadUrl(String audTrm, String focusCd,String fileName) {
		String ftpHttpUrlPrefix = propertyUtil.getPropValue("ftpHttpUrlPrefix");
		StringBuilder url = new StringBuilder(30);
		url.append(buildRelativePath(audTrm, focusCd)).append("/").append(fileName);
		return FileUtil.buildFullFilePath(ftpHttpUrlPrefix, url.toString());
	}
	public String getLocalDir() {
		String tempDir = propertyUtil.getPropValue("tempDir");
		FileUtil.mkdirs(tempDir);
		return propertyUtil.getPropValue("tempDir");
	}

	public String getLocalPath(String fileName) {
		return getLocalDir() + '/' + fileName;
	}
*/

}
