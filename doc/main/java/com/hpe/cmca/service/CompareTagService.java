package com.hpe.cmca.service;

import com.hpe.cmca.common.FilePropertyPlaceholderConfigurer;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.CompareTagMapper;
import com.hpe.cmca.pojo.CompareTag;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;
import com.hpe.cmca.util.ZipFileUtil;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CompareTagService {

    @Autowired
    protected FilePropertyPlaceholderConfigurer propertyUtil = null;
    @Autowired
    private MybatisDao mybatisDao;
    private static final Logger logger = Logger.getLogger(CompareTagService.class);

    //private Map<String, Object> compareResultMap = new HashMap<>();//map形式记录对比结果，
    private Map<String, Object> tagValueMap = new HashMap<>();//map形式记录标签值，用于内存张对比

    private List<Map<String, Object>> tagValueMapList = new ArrayList<>();//map形式记录标签值，用于更新数据库
    private List<Map<String, Object>> compareResultMapList = new ArrayList<>();//map形式记录对比结果，用于更新数据库

    private Map<String, String> prvdMap = new HashMap<String, String>() {
        {
            put("10000", "全公司");
            put("10100", "北京");
            put("10200", "上海");
            put("10300", "天津");
            put("10400", "重庆");
            put("10500", "贵州");
            put("10600", "湖北");
            put("10700", "陕西");
            put("10800", "河北");
            put("10900", "河南");
            put("11000", "安徽");
            put("11100", "福建");
            put("11200", "青海");
            put("11300", "甘肃");
            put("11400", "浙江");
            put("11500", "海南");
            put("11600", "黑龙江");
            put("11700", "江苏");
            put("11800", "吉林");
            put("11900", "宁夏");
            put("12000", "山东");
            put("12100", "山西");
            put("12200", "新疆");
            put("12300", "广东");
            put("12400", "辽宁");
            put("12500", "广西");
            put("12600", "湖南");
            put("12700", "江西");
            put("12800", "内蒙古");
            put("12900", "云南");
            put("13000", "四川");
            put("13100", "西藏");
        }
    };

    private String buildTagMapKey(String tagId, String prvdId, String subjectId) {

        return tagId + "-" + subjectId + "-" + prvdId;
    }

    private String buildCompareMapKey(String tagId, String tagIdComp, String prvdId, String subjectId) {

        return tagId + "-" + tagIdComp + "-" + subjectId + "-" + prvdId;
    }

    private String buildCompareMapValue(String tagValue, String tagValueComp, String compResult) {

        return tagValue + "-" + tagValueComp + "-" + compResult;
    }

    private List<CompareTag> getCompareTag(String subjectId) {

        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("subjectId", subjectId);
        CompareTagMapper ctMapper = mybatisDao.getSqlSession().getMapper(CompareTagMapper.class);
        List<CompareTag> ctL = ctMapper.getCompareTag(paraMap);

        return ctL;
    }

    public Map<String, Object> computeTag(String subjectId, String audTrm) throws Exception {


        String currentPrvdId = "";
        Map<String, Object> resultMap = new HashMap<>();
        List<String> tpL1 = new ArrayList<String>();
        List<String> tpL3 = new ArrayList<String>();
        List<CompareTag> ctL = getCompareTag(subjectId);
        Iterator<CompareTag> itr = ctL.iterator();
        List<String> alreadyDownZip = new ArrayList<String>();
        String fileNmLast = "";
        String sheetNmLast = "";
        InputStream inp = null;
        XSSFWorkbook wb = null;
        Sheet sh = null;
        List<String> docText = new ArrayList<String>();
        //for (CompareTag ct : ctL) {
        try {
            while (itr.hasNext()) {
                CompareTag ct = itr.next();

                if (!currentPrvdId.equals(ct.getTagBelongPrvdId())) {
                    updateTagValueItr();
                }
                currentPrvdId = ct.tagBelongPrvdId;

                if (ct.getTagType() == 0 || ct.getTagType() == 1) {//如果取值类型是文件取值
                    String ftpFullPath = replaceText(ct.getTagZipPath(), audTrm);
                    String ftpPath = ftpFullPath.substring(0, ftpFullPath.lastIndexOf("/"));
                    String ftpName = ftpFullPath.substring(ftpFullPath.lastIndexOf("/") + 1, ftpFullPath.length());

                    String unzipPath = getLocalDir(subjectId) + "/" + ftpName.substring(0, ftpName.lastIndexOf("."));

                    File file = new File(getLocalDir(subjectId) + "/" + ftpName);
                    if (!file.exists()) {
                        String tpS = downloadFile(getLocalDir(subjectId) + "/" + ftpName, ftpPath, ftpName);
                        if ("FAIL".equals(tpS) && ct.getTagFileType() == 0) {
                            if (!tpL1.contains("缺少排名汇总压缩包；"))
                                tpL1.add("缺少排名汇总压缩包；");
                            resultMap.put("1", tpL1);

                            return resultMap;
                        }
                        if ("FAIL".equals(tpS) && ct.getTagFileType() == 1) {
                            if (!tpL1.contains("缺少审计报告压缩包；"))
                                tpL1.add("缺少审计报告压缩包；");
                            resultMap.put("1", tpL1);

                            return resultMap;
                        }

                        ZipFileUtil.unZip(getLocalDir(subjectId) + "/" + ftpName, unzipPath);
                    }
                    File file1 = new File(unzipPath);
                    if (!file1.exists()) {
                        ZipFileUtil.unZip(getLocalDir(subjectId) + "/" + ftpName, unzipPath);
                    }
                    String fileNm = replaceText(ct.getTagFileNm(), audTrm);
                    File tpFile1 = new File(unzipPath + "/" + fileNm);
                    if (!tpFile1.exists() && ct.getTagFileType() == 1) {

                        if (!tpL3.contains("" + fileNm + " 报告文件缺失(其他省报告继续比对)；"))
                            tpL3.add("" + fileNm + " 报告文件缺失(其他省报告继续比对)；");
                        resultMap.put("3", tpL3);

                        tagValueMap.put(buildTagMapKey(ct.getTagId(), ct.getTagBelongPrvdId(), subjectId), "0");

                        Map<String, Object> tpMap = new HashMap<>();
                        tpMap.put("id", ct.getId());
                        tpMap.put("tagValue", "0");
                        tagValueMapList.add(tpMap);
                        continue;
                    }
                    if (!tpFile1.exists() && ct.getTagFileType() == 0) {

                        if (!tpL3.contains("" + fileNm + " 排名汇总文件缺失(其他排名汇总文件继续比对)；"))
                            tpL3.add("" + fileNm + " 排名汇总文件缺失(其他排名汇总文件继续比对)；");
                        resultMap.put("3", tpL3);

                        tagValueMap.put(buildTagMapKey(ct.getTagId(), ct.getTagBelongPrvdId(), subjectId), "0");
                        Map<String, Object> tpMap = new HashMap<>();
                        tpMap.put("id", ct.getId());
                        tpMap.put("tagValue", "0");
                        tagValueMapList.add(tpMap);
                        continue;
                    }
                    if (ct.getTagFileType() == 0) {//如果文件类型是排名汇总
                        String sheetNm = ct.getTagSheetNm();
                        if (!fileNmLast.equals(fileNm)) {
                            inp = new FileInputStream(unzipPath + "/" + fileNm);
                            wb = new XSSFWorkbook(inp);
                            sh = wb.getSheet(sheetNm);
                            fileNmLast = fileNm;
                            sheetNmLast = sheetNm;
                        }

                        if (!sheetNmLast.equals(sheetNm)) {
                            sh = wb.getSheet(sheetNm);
                            sheetNmLast = sheetNm;
                        }
                        if (ct.getTagLocationType() == 0) {//如果是坐标直取类型，从汇总数据处取数据
                            int c = Integer.parseInt(ct.getTagLocation().split("\\|")[0]);
                            int r = Integer.parseInt(ct.getTagLocation().split("\\|")[1]);
                            Row row = sh.getRow(r);
                            Cell cell = row.getCell(c);

                            String result = "0";
                            if (cell.getCellType() == 0)
                                result = String.valueOf(cell.getNumericCellValue());
                            if (cell.getCellType() == 1) {
                                result = String.valueOf(cell.getStringCellValue());
                                if ("-".equals(result)) {
                                    result = "0";
                                }
                            }
                            logger.error(buildTagMapKey(ct.getTagId(), ct.getTagBelongPrvdId(), subjectId) + "/" + result.replace(",", "").replace(" ", ""));
                            tagValueMap.put(buildTagMapKey(ct.getTagId(), ct.getTagBelongPrvdId(), subjectId), result.replace(",", "").replace(" ", ""));
                            addToList(ct, result);

                        } else {//如果是 根据坐标 动态判断（分省）

                            int vC = Integer.parseInt(ct.getTagLocation().split("\\|")[0]);
                            int vR = Integer.parseInt(ct.getTagLocation().split("\\|")[1]);

                            int pC = Integer.parseInt(ct.getTagPrvdLocation().split("\\|")[0]);
                            int pR = Integer.parseInt(ct.getTagPrvdLocation().split("\\|")[1]);
                            Row row = null;
                            Cell cell = null;
                            for (int i = 0; i < 31; i++) {
                                row = sh.getRow(pR + i);
                                cell = row.getCell(pC);
                                String prvdName = "";
                                if (cell.getCellType() == 0)
                                    prvdName = String.valueOf(cell.getNumericCellValue());
                                if (cell.getCellType() == 1)
                                    prvdName = String.valueOf(cell.getStringCellValue());
                                if (prvdName.equals(prvdMap.get(ct.getTagBelongPrvdId())) || (prvdName.equals("全国") && "10000".equals(ct.getTagBelongPrvdId()))) {
                                    cell = row.getCell(vC);
                                    String result = "0";
                                    if (cell.getCellType() == 0)
                                        result = String.valueOf(cell.getNumericCellValue());
                                    if (cell.getCellType() == 1) {
                                        result = String.valueOf(cell.getStringCellValue());
                                        if ("-".equals(result)) {
                                            result = "0";
                                        }
                                    }
                                    logger.error(buildTagMapKey(ct.getTagId(), ct.getTagBelongPrvdId(), subjectId) + "/" + result.replace(",", "").replace(" ", ""));
                                    tagValueMap.put(buildTagMapKey(ct.getTagId(), ct.getTagBelongPrvdId(), subjectId), result.replace(",", "").replace(" ", ""));

                                    addToList(ct, result);
                                    break;
                                }
                            }
                        }
                    }
                    if (ct.getTagFileType() == 1) {//如果文件类型是审计报告
                        if (!fileNmLast.equals(fileNm)) {
                            docText.clear();
                            inp = new FileInputStream(unzipPath + "/" + fileNm);
                            XWPFDocument doc = new XWPFDocument(inp);
                            for (IBodyElement element : doc.getBodyElements()) {
                                if (element.getElementType() == BodyElementType.PARAGRAPH) {
                                    XWPFParagraph paragraph = (XWPFParagraph) element;
                                    XWPFRun r1 = paragraph.getRuns().get(0);
                                    if ("仿宋_GB2312".equals(r1.getFontFamily()) && !r1.isBold()) {
                                        String tpStr = paragraph.getParagraphText();
                                        if (!tpStr.equals("")) {
                                            docText.add(tpStr);
                                        }
                                    }
                                }
                            }
                            fileNmLast = fileNm;
                        }
                        String affix = ct.getTagAffix();
                        String prefix = affix.split("\\#")[0];
                        String suffix = affix.split("\\#")[1];

                        String regex = ".*" + prefix + "([^。^|^；^-]+?)" + suffix + ".*";
                        Matcher m = Pattern.compile(regex).matcher(docText.get(ct.getTagParagr()));
                        String result = "0";
                        while (m.find()) {
                            result = m.group(1);
                        }
                        if (result.contains("增加")) result = result.substring(result.indexOf("增加") + 2, result.length());
                        if (result.contains("减少")) result = result.substring(result.indexOf("减少") + 2, result.length());
                        logger.error(buildTagMapKey(ct.getTagId(), ct.getTagBelongPrvdId(), subjectId) + "/" + result.replace(",", "").replace(" ", ""));
                        tagValueMap.put(buildTagMapKey(ct.getTagId(), ct.getTagBelongPrvdId(), subjectId), result.replace(",", "").replace(" ", ""));
                        addToList(ct, result);
                    }

                }
                if (ct.getTagType() == 1 || ct.getTagType() == 2) {//如果取值类型是现有标签计算
                    String result = "0";
                    result = computeTagValue(ct);
                    logger.error(buildTagMapKey(ct.getTagId(), ct.getTagBelongPrvdId(), subjectId) + "/" + result);
                    tagValueMap.put(buildTagMapKey(ct.getTagId(), ct.getTagBelongPrvdId(), subjectId), result);
                    Map<String, Object> tpMap = new HashMap<>();
                    tpMap.put("id", ct.getId());
                    tpMap.put("tagValue", result.replace(",", "").replace(" ", ""));
                    tagValueMapList.add(tpMap);
                }


            }
            updateTagValueItr();//只剩全国的未更新

            compareTagByRule(subjectId);

            if (!((List<Map<String, Object>>) getCompareResult(subjectId, audTrm).get("dataList")).isEmpty()) {

                if (!tpL3.contains("数据异常"))
                    tpL3.add("数据异常");
                resultMap.put("3", tpL3);

            }

            if (((List<Map<String, Object>>) getCompareResult(subjectId, audTrm).get("dataList")).isEmpty()) {

                if (!tpL1.contains("数据正常"))
                    tpL1.add("数据正常");
                resultMap.put("1", tpL1);

            }
        } catch (Exception e) {
            if (!tpL1.contains("比对过程出错，请检查文件或标签配置"))
                tpL1.add("比对过程出错，请检查文件或标签配置");
            resultMap.put("1", tpL1);

        } finally {
            //清除临时文件
            deleteDirectory(getLocalDir(subjectId));
            return resultMap;
        }

    }

    private void addToList(CompareTag ct, String result) {

        if (ct.getTagType() == 0) {

            Map<String, Object> tpMap = new HashMap<>();
            tpMap.put("id", ct.getId());
            tpMap.put("tagValue", result.replace(",", "").replace(" ", ""));
            tagValueMapList.add(tpMap);
        }
    }

    private String computeTagValue(CompareTag ct) {
        String expression = ct.getTagComputRule();
        Map<String, Object> map = new HashMap<>();
        JexlContext jc = new MapContext();
        //String expression = "|A1-A2|";
        String regex = "([!A-Z]{1,2}[0-9]{1,2})";
        Matcher m = Pattern.compile(regex).matcher(expression);
        String groupResult = "";
        while (m.find()) {
            groupResult = m.group(1);
            if (groupResult.contains("!")) {
                groupResult = groupResult.replace("!", "");
                expression = expression.replace("!", "");
                jc.set(groupResult, getChinaValueByTagIdFromMap(tagValueMap, groupResult, ct.getTagBelongSubId()));
            } else {
                jc.set(groupResult, getValueByTagIdFromMap(tagValueMap, groupResult, ct.getTagBelongPrvdId(), ct.getTagBelongSubId()));
            }
        }


        jc.set("Math", Math.class);
        Expression e = new JexlEngine().createExpression(expression);
        Object obj = e.evaluate(jc);

        return obj.toString();

    }

    private Double getChinaValueByTagIdFromMap(Map<String, Object> tagValueMap, String tagId, String subjectId) {

        Double result = 0.0;

        Iterator<String> itr = prvdMap.keySet().iterator();
        while (itr.hasNext()) {
            String prvdId = itr.next();
            try {
                if (!"10000".equals(prvdId))
                    result += Double.parseDouble(tagValueMap.get(buildTagMapKey(tagId, prvdId, subjectId)).toString());
            } catch (Exception e) {
                logger.error("FILE LOST ERROR:" + buildTagMapKey(tagId, prvdId, subjectId) + e.getMessage());
            }
        }
        return result;
    }

    private Double getValueByTagIdFromMap(Map<String, Object> tagValueMap, String tagId, String prvdId, String subjectId) {

        Double dd = 0.0;
        try {
            dd = Double.parseDouble(tagValueMap.get(buildTagMapKey(tagId, prvdId, subjectId)).toString());
        } catch (Exception e) {
            logger.error("FILE LOST ERROR:" + buildTagMapKey(tagId, prvdId, subjectId) + e.getMessage());
        }
        return dd;
    }

    public void compareTagByRule(String subjectId) {

        String currentPrvdId = "";

        CompareTagMapper ctMapper = mybatisDao.getSqlSession().getMapper(CompareTagMapper.class);
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("subjectId", subjectId);
        List<Map<String, Object>> crL = ctMapper.getCompareRule(paraMap);
        for (Map<String, Object> cr : crL) {

            if (!currentPrvdId.equals(cr.get("tag_belong_prvd_id").toString())) {
                updateCompareResultItr();
            }
            currentPrvdId = cr.get("tag_belong_prvd_id").toString();

            Double v1 = getValueByTagIdFromMap(tagValueMap, cr.get("tag_id").toString(), cr.get("tag_belong_prvd_id").toString(), subjectId);
            Double v2 = getValueByTagIdFromMap(tagValueMap, cr.get("tag_id_comp").toString(), cr.get("tag_belong_prvd_id").toString(), subjectId);
            int compResult = 0;
            if ("=".equals(cr.get("comp_rule").toString())) {
                if (Math.abs(v1 - v2) < 0.01) compResult = 1;
                else compResult = 2;

            }
            if (">".equals(cr.get("comp_rule").toString())) {
                if ((v1 - v2) > 0 && Math.abs(v1 - v2) >= 0.01) compResult = 1;
                else compResult = 2;
            }
            if ("<".equals(cr.get("comp_rule").toString())) {
                if ((v1 - v2) < 0 && Math.abs(v1 - v2) >= 0.01) compResult = 1;
                else compResult = 2;
            }

            logger.error(buildCompareMapKey(cr.get("tag_id").toString(), cr.get("tag_id_comp").toString(), cr.get("tag_belong_prvd_id").toString(), subjectId) + "/" + buildCompareMapValue(v1.toString().replace(",", "").replace(" ", ""), v2.toString().replace(",", "").replace(" ", ""), String.valueOf(compResult)));
            //compareResultMap.put(buildCompareMapKey(cr.get("tag_id").toString(), cr.get("tag_id_comp").toString(), cr.get("tag_belong_prvd_id").toString(), subjectId), buildCompareMapValue(v1.toString().replace(",", "").replace(" ", ""), v2.toString().replace(",", "").replace(" ", ""), String.valueOf(compResult)));

            Map<String, Object> tpMap = new HashMap<>();
            tpMap.put("id", cr.get("id"));
            tpMap.put("tagValue", v1.toString().replace(",", "").replace(" ", ""));
            tpMap.put("tagValueComp", v2.toString().replace(",", "").replace(" ", ""));
            tpMap.put("compStatus", compResult);
            compareResultMapList.add(tpMap);

        }
        updateCompareResultItr();//只剩全国的未更新
    }

    private void updateTagValueItr() {
        CompareTagMapper ctMapper = mybatisDao.getSqlSession().getMapper(CompareTagMapper.class);
        if (!tagValueMapList.isEmpty()) {
            ctMapper.updateTagValueItr(tagValueMapList);
            tagValueMapList.clear();
        }

    }

    private void updateCompareResultItr() {
        CompareTagMapper ctMapper = mybatisDao.getSqlSession().getMapper(CompareTagMapper.class);
        if (!compareResultMapList.isEmpty()) {
            ctMapper.updateCompareResultItr(compareResultMapList);
            compareResultMapList.clear();
        }

    }


    public Map<String, Object> getCompareResult(String subjectId, String audTrm) {

        CompareTagMapper ctMapper = mybatisDao.getSqlSession().getMapper(CompareTagMapper.class);

        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("subjectId", subjectId);
        List<Map<String, Object>> dataList = ctMapper.getCompareResult(paraMap);
        for (Map<String, Object> tpMap : dataList) {
            tpMap.put("word_name", replaceText(tpMap.get("word_name").toString(), audTrm));
            tpMap.put("excel_name", replaceText(tpMap.get("excel_name").toString(), audTrm));
        }

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("dataList", dataList);
        return map;
    }

    private boolean isNumber(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private String replaceText(String text, String audTrm) {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("audTrm", audTrm);
        String textEnd = replaceParam(text, paramMap);
        return textEnd;
    }

    private String replaceParam(String text, Map<String, Object> params) {
        Matcher m = Pattern.compile("[{]([^{^}]*?)[}]").matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            if (params.get(m.group(1)) == null)
                params.put(m.group(1), "0");//如果为空则填默认值
            m.appendReplacement(sb, params.get(m.group(1)).toString());
        }
        m.appendTail(sb);
        return sb.toString();
    }
    //新增欠费、流量专题文件你对 by pxl

    private String getLocalDir(String subjectId) {
        String tempDir = propertyUtil.getPropValue("tagDir");
        FileUtil.mkdirs(tempDir + "/" + subjectId);
        return propertyUtil.getPropValue("tagDir") + "/" + subjectId;
    }


    private String buildRelativePath(String audTrm, String focusCd, String subjectId, String prvdId) {

        StringBuilder path = new StringBuilder();
        path.append(audTrm).append("/").append(subjectId).append("/").append(focusCd).append("/").append(prvdId);
        return path.toString();
    }

    private FtpUtil initFtp() {
        String isTransferFile = propertyUtil.getPropValue("isTransferFile");
        if (!"true".equalsIgnoreCase(isTransferFile)) {
            return null;
        }
        FtpUtil ftpTool = new FtpUtil();

        String ftpServer = org.apache.commons.lang.StringUtils.trimToEmpty(propertyUtil
                .getPropValue("ftpServer"));
        String ftpPort = org.apache.commons.lang.StringUtils.trimToEmpty(propertyUtil
                .getPropValue("ftpPort"));
        String ftpUser = org.apache.commons.lang.StringUtils.trimToEmpty(propertyUtil
                .getPropValue("ftpUser"));
        String ftpPass = org.apache.commons.lang.StringUtils.trimToEmpty(propertyUtil
                .getPropValue("ftpPass"));
        ftpTool.initClient(ftpServer, ftpPort, ftpUser, ftpPass);
        return ftpTool;
    }

    public String downloadFile(String localFilePath, String remotePath, String fileName) {

        String downStatus = "";
        FtpUtil ftpTool = null;
        try {
            ftpTool = initFtp();
            if (ftpTool == null) {
                return null;
            }
            ftpTool.downloadFile(localFilePath, remotePath, fileName);
            downStatus = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("NotiFile downloadFile>>>" + e.getMessage(), e);
            downStatus = "FAIL";
        } finally {
            if (ftpTool != null) {
                ftpTool.disConnect();
            }
            return downStatus;
        }
    }


    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                logger.error("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                logger.error("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            logger.error("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    private boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            logger.error("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            logger.error("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            logger.error("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }


}
