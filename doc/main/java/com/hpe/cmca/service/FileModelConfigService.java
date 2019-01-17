package com.hpe.cmca.service;

import com.alibaba.druid.sql.visitor.functions.If;
import com.hpe.cmca.common.AsposeUtil;
import com.hpe.cmca.common.FileGenThread;
import com.hpe.cmca.common.FilePropertyPlaceholderConfigurer;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.finals.Constants;
import com.hpe.cmca.interfaces.FileGenMapper;
import com.hpe.cmca.job.v2.Word0000FileGenProcessor;
import com.hpe.cmca.pojo.ExcelModelConfig;
import com.hpe.cmca.pojo.FileGenData;
import com.hpe.cmca.pojo.WordModelConfig;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileModelConfigService {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FileModelConfigService.class);

    @Autowired
    MybatisDao mybatisDao;
    @Autowired
    protected FilePropertyPlaceholderConfigurer propertyUtil = null;
    @Autowired
    AsposeUtil asposeUtil;

    @Autowired
    Word0000FileGenProcessor wfg;

    public List<Map<String, Object>> getSubject() {
        List<Map<String, Object>> lMap = null;
        try {
            FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
            lMap = fileGenMapper.getSubject();
        } catch (Exception e) {
            logger.error("接口getSubject异常检查参数是否正确传递");
            e.printStackTrace();
        }
        return lMap;
    }


    public List<Map<String, Object>> getModeltype(Integer subjectId, String fileType) {
        List<Map<String, Object>> lMap = null;
        try {
            FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
            Map<String, Object> param = new HashMap<>();
            param.put("subjectId", subjectId);
            if ("Word".equals(fileType))
                param.put("fileFormatType", 0);
            if ("Excel".equals(fileType))
                param.put("fileFormatType", 1);
            lMap = fileGenMapper.getModeltype(param);
        } catch (Exception e) {
            logger.error("接口getModeltype异常检查参数是否正确传递");
            e.printStackTrace();
        }
        return lMap;
    }

    public List<Map<String, Object>> getTableData(Integer subjectId, String category, String modelTypeId) {
        List<Map<String, Object>> lMap = null;
        try {
            FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
            Map<String, Object> param = new HashMap<>();
            param.put("subjectId", subjectId);
            param.put("modelTypeId", modelTypeId);
            Pattern p = null;
            if ("Word".equals(category)) {
                param.put("fileFormatType", 0);
                p = Pattern.compile("[_]{1}([\u4e00-\u9fa5a-zA-Z0-9]+)[.]{1}");
            }

            if ("Excel".equals(category)) {
                param.put("fileFormatType", 1);
                p = Pattern.compile("([\u4e00-\u9fa5a-zA-Z0-9]+)[_]{1}");
            }

            lMap = fileGenMapper.getTableData(param);
            for (Map<String, Object> map : lMap) {
                String fileName = map.get("fileName").toString();
                String defaultVersionId = "1.0";
                if (map.get("defaultVersionId") != null)
                    defaultVersionId = map.get("defaultVersionId").toString();

                Matcher m = p.matcher(fileName);
                while (m.find()) {
                    fileName = m.group(1);
                }
                fileName = fileName + "V" + defaultVersionId;
                map.put("fileName", fileName);

                String fileCode = map.get("rowCode").toString();
                Map<String, Object> param1 = new HashMap<>();
                param1.put("fileCode", fileCode);
                List<Map<String, Object>> lMap1 = fileGenMapper.getVersion(param1);
                for (Map<String, Object> tmp : lMap1) {
                    String name = tmp.get("name").toString();
                    String id = tmp.get("id").toString();
                    Matcher m1 = p.matcher(name);
                    while (m1.find()) {
                        name = m1.group(1);
                    }
                    name = name + "V" + id;
                    tmp.put("name", name);
                }
                map.put("versionList", lMap1);
            }
        } catch (Exception e) {
            logger.error("接口getTableData异常检查参数是否正确传递");
            e.printStackTrace();
        }
        return lMap;
    }

    public Map<String, Object> getVersionData(String rowCode, String versionId) {
        List<Map<String, Object>> lMap = null;
        try {
            FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
            Map<String, Object> param = new HashMap<>();
            param.put("rowCode", rowCode);
            param.put("versionId", versionId);
            lMap = fileGenMapper.getVersionData(param);
        } catch (Exception e) {
            logger.error("接口getTableDataByVersion异常检查参数是否正确传递");
            e.printStackTrace();
        }
        if (lMap.size() > 0) {
            return lMap.get(0);
        } else {
            return null;
        }
    }

    public int deleteByVersion(String rowCode, String versionId) {
        try {
            FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
            Map<String, Object> param = new HashMap<>();
            param.put("rowCode", rowCode);
            param.put("versionId", versionId);
            fileGenMapper.deleteVersionWord(param);
            fileGenMapper.deleteVersionExcel(param);
        } catch (Exception e) {
            logger.error("接口deleteByVersion异常检查参数是否正确传递");
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public List<Map<String, Object>> getExcelSheetName(String rowCode, String versionId) {
        List<Map<String, Object>> lMap = null;
        try {
            FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
            Map<String, Object> param = new HashMap<>();
            param.put("rowCode", rowCode);
            param.put("versionId", versionId);
            lMap = fileGenMapper.getExcelSheetName(param);
        } catch (Exception e) {
            logger.error("接口getExcelSheetName异常检查参数是否正确传递");
            e.printStackTrace();
        }
        return lMap;
    }

    public WordModelConfig selectWordModelByIds(String rowCode, String versionId, String blockOrder) {
        List<WordModelConfig> lWMC = null;
        try {
            FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
            Map<String, Object> param = new HashMap<>();
            param.put("rowCode", rowCode);
            param.put("versionId", versionId);
            param.put("blockOrder", blockOrder);
            lWMC = fileGenMapper.selectWordModelByIds(param);
        } catch (Exception e) {
            logger.error("接口selectWordModelByIds异常检查参数是否正确传递");
            e.printStackTrace();
        }
        if (lWMC.size() > 0) {
            return lWMC.get(0);
        } else {
            return null;
        }
    }

    public ExcelModelConfig selectExcelModelByIds(String rowCode, String versionId, String sheetOrder, String blockOrder) {
        List<ExcelModelConfig> lEMC = null;
        try {
            FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
            Map<String, Object> param = new HashMap<>();
            param.put("rowCode", rowCode);
            param.put("versionId", versionId);
            param.put("sheetOrder", sheetOrder);
            param.put("blockOrder", blockOrder);
            lEMC = fileGenMapper.selectExcelModelByIds(param);
        } catch (Exception e) {
            logger.error("接口selectExcelModelByIds异常检查参数是否正确传递");
            e.printStackTrace();
        }
        if (lEMC.size() > 0) {
            return lEMC.get(0);
        } else {
            return null;
        }
    }

    public Map<String, Object> getModelTxtHandle(String category, String rowCode, String versionId, String sheetOrder, String type,int isPreGen) {
        String srcPath=propertyUtil.getPropValue("tmpModel");
        if(isPreGen==1)
            srcPath=propertyUtil.getPropValue("tmpPreFile");
        String txt = null;
        String style = null;
        FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
        Path p = Paths.get(srcPath, rowCode + "V" + versionId);
        Map<String, Object> map = new HashMap<>();
        if ("Word".equals(category)) {
            txt = asposeUtil.read(Paths.get(p.toString(), "word" + type + ".html").toFile());
        }

        if ("Excel".equals(category)) {
            txt = asposeUtil.read(Paths.get(p.toString(), "sheet" + type + (Integer.parseInt(sheetOrder)) + ".html").toFile());
            style = "<style>" + asposeUtil.read(Paths.get(p.toString(), "sheet" + Integer.parseInt(sheetOrder) + "_files", "stylesheet.css").toFile()) + "</style>";
            map.put("style", style.replace("\n", "").replace("\t", "").replace("\r", ""));
        }
        map.put("txt", txt);

        if ("Two".equals(type)) {
            Map<String, Object> param = new HashMap<>();
            param.put("rowCode", rowCode);
            param.put("versionId", versionId);
            param.put("sheetOrder", sheetOrder);
            List<Map<String, Object>> lMap = fileGenMapper.getBlockList(param);
            map.put("blockOrder", lMap);
        }
        return map;
    }

    public List<Map<String, Object>> getImportName(Integer subjectId, String category, String modelTypeId) {
        List<Map<String, Object>> lMap = null;
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        try {
            FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
            Map<String, Object> param = new HashMap<>();
            param.put("subjectId", subjectId);
            param.put("modelTypeId", modelTypeId);
            Pattern p = null;
            if ("Word".equals(category)) {
                param.put("fileFormatType", 0);
                p = Pattern.compile("[_]{1}([\u4e00-\u9fa5a-zA-Z0-9]+)[.]{1}");
            }
            if ("Excel".equals(category)) {
                param.put("fileFormatType", 1);
                p = Pattern.compile("([\u4e00-\u9fa5a-zA-Z0-9]+)[_]{1}");
            }
            lMap = fileGenMapper.getTableData(param);

            for (Map<String, Object> map : lMap) {
                String fileCode = map.get("rowCode").toString();
                String fileName = map.get("fileName").toString();
                Matcher m2 = p.matcher(fileName);
                while (m2.find()) {
                    fileName = m2.group(1);
                }
                Map<String, Object> param1 = new HashMap<>();
                param1.put("fileCode", fileCode);
                List<Map<String, Object>> lMap1 = fileGenMapper.getMaxVersion(param1);
                Map<String, Object> resultMap = new HashMap<>();
                if (lMap1.size() > 0) {
                    String name = lMap1.get(0).get("name").toString();
                    Integer id = Integer.parseInt(lMap1.get(0).get("id").toString().replace(".0", "")) + 1;
                    Matcher m1 = p.matcher(name);
                    while (m1.find()) {
                        name = m1.group(1);
                    }
                    name = name + "V" + id + ".0";
                    resultMap.put("name", name);
                    resultMap.put("id", fileCode + "V" + id + ".0");
                    // resultMap.put("rowCode", fileCode);
                } else {
                    resultMap.put("name", fileName + "V1.0");
                    resultMap.put("id", fileCode + "V1.0");
                    //resultMap.put("rowCode", fileCode);
                }
                resultMapList.add(resultMap);
            }
        } catch (Exception e) {
            logger.error("接口getImportName异常检查参数是否正确传递");
            e.printStackTrace();
        }
        return resultMapList;
    }

    public int webUploader(MultipartFile file,HttpServletRequest resquest, String category, String rowCode, String versionId) throws Exception {
        String reviser = resquest.getSession().getAttribute("userName").toString();
        String oriName=file.getOriginalFilename();
        String fileType = oriName.substring(oriName.indexOf("."),oriName.length());
        System.out.println(fileType);
        if ("Word".equals(category)){
            if(!(".docx".equals(fileType)||".doc".equals(fileType)))
                return 0;
        }
        if ("Excel".equals(category)){
            if(!(".xlsx".equals(fileType)||".xls".equals(fileType)))
                return 0;
        }
        List<Map<String, Object>> lMap = null;
        List<Map<String, Object>> lm = null;
        String fileName = null;
        FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("fileCode", rowCode);
            lMap = fileGenMapper.getModelFileName(param);
        } catch (Exception e) {
            logger.error("接口webUploader异常检查参数是否正确传递");
            e.printStackTrace();
            return 0;
        }
        try {
            if (lMap.size() > 0) {
                fileName = lMap.get(0).get("name").toString();//导入的文档必须先在zip_rule表中配置
                Path p = Paths.get(propertyUtil.getPropValue("tmpModel"), rowCode + "V" + versionId);
                try {
                    Files.createDirectory(p);
                } catch (Exception e) {
                    logger.error("DIR:" + p.toString() + "has already existed");
                }
                Path p1 = Paths.get(p.toString(), fileName);
                File f = p1.toFile();
                file.transferTo(f);
                f.createNewFile();

                if ("Word".equals(category)) {
                    lm = asposeUtil.word2Html(f, fileName, rowCode, versionId,propertyUtil.getPropValue("tmpModel"),0);
                    for (Map<String, Object> tMap : lm) {
                        Integer num = Integer.parseInt(tMap.get("dataPointNum").toString());
                        for (int blockOrder = 0; blockOrder < num; blockOrder++) {
                            Map<String, Object> param = new HashMap<>();
                            param.put("rowCode", rowCode);
                            param.put("fileName", fileName);
                            param.put("versionId", versionId);
                            param.put("blockOrder", blockOrder);
                            param.put("reviser", reviser);
                            fileGenMapper.insertWordModel(param);
                        }
                    }
                }
                if ("Excel".equals(category)) {
                    lm = asposeUtil.excel2Html(f, fileName, rowCode, versionId,propertyUtil.getPropValue("tmpModel"),0);
                    int sheetOrder = 0;
                    for (Map<String, Object> tMap : lm) {
                        Integer num = Integer.parseInt(tMap.get("dataPointNum").toString());
                        for (int blockOrder = 0; blockOrder < num; blockOrder++) {
                            Map<String, Object> param = new HashMap<>();
                            param.put("rowCode", rowCode);
                            param.put("fileName", fileName);
                            param.put("versionId", versionId);
                            param.put("sheetOrder", sheetOrder);
                            param.put("blockOrder", blockOrder);
                            param.put("reviser", reviser);
                            fileGenMapper.insertExcelModel(param);
                        }
                        sheetOrder++;
                    }
                }
            }

        } catch (Exception e) {
            logger.error("请检查导入模板格式是否符合规范");
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public void updateExcelModel(Map<String, Object> param) {

        FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
        fileGenMapper.updateExcelModel(param);

    }

    public void updateWordModel(Map<String, Object> param) {

        FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
        fileGenMapper.updateWordModel(param);
    }

    public int setDefaultVersion(String category, String rowCode, String versionId) {
        FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyyMM");
            String effectiveTime = sd.format(new Date());

            Map<String, Object> param = new HashMap<>();
            param.put("rowCode", rowCode);
            param.put("versionId", versionId);
            param.put("effectiveTime", effectiveTime);

            if ("Word".equals(category)) {
                fileGenMapper.cancelWordDefaultVersion(param);
                fileGenMapper.setWordDefaultVersion(param);
            }

            if ("Excel".equals(category)) {
                fileGenMapper.cancelExcelDefaultVersion(param);
                fileGenMapper.setExcelDefaultVersion(param);
            }
        } catch (Exception e) {
            logger.error("接口setDefaultVersion异常检查参数是否正确传递");
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public int setFullVersion(String category, String rowCode, String versionId) {
        FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("rowCode", rowCode);
            param.put("versionId", versionId);

            if ("Word".equals(category)) {
                fileGenMapper.setWordFullVersion(param);
            }
            if ("Excel".equals(category)) {
                fileGenMapper.setExcelFullVersion(param);
            }
        } catch (Exception e) {
            logger.error("接口setFullVersion异常检查参数是否正确传递");
            e.printStackTrace();
            return 0;
        }
        return 1;
    }


    public String outPutConf(String category, String rowCode, String versionId) throws UnsupportedEncodingException {
        FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
        Map<String, Object> param = new HashMap<>();
        param.put("rowCode", rowCode);
        param.put("versionId", versionId);
        List<Map<String, Object>> lMapT = new ArrayList<>();
        List<Map<String, Object>> lMapC = new ArrayList<>();
        StringBuilder sbT = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sbDel = new StringBuilder();
        String targetTb = "";
        if ("Word".equals(category)) {
            lMapT = fileGenMapper.getOutputWordConfTitle(param);
            lMapC = fileGenMapper.getOutputWordConfContent(param);
            targetTb = "hpmgr.busi_word_model_config";
            sbDel.append("DELETE FROM "+targetTb+" where word_code='"+rowCode+"' and version_code='"+versionId+"' and delete_flag=1;\n");
        }

        if ("Excel".equals(category)) {
            lMapT = fileGenMapper.getOutputExcelConfTitle(param);
            lMapC = fileGenMapper.getOutputExcelConfContent(param);
            targetTb = "hpmgr.busi_excel_model_config";
            sbDel.append("DELETE FROM "+targetTb+" where excel_code='"+rowCode+"' and version_code='"+versionId+"' and delete_flag=1;\n");

        }

        sb1.append("INSERT	INTO ");
        sb1.append(targetTb + " ( ");
        List<String> titleList = new ArrayList<>();
        for (Map<String, Object> map : lMapT) {
            titleList.add(map.get("Column Name").toString().replace(" ", "").replace("  ", ""));

            sb1.append(map.get("Column Name").toString().replace(" ", "").replace("  ", ""));
            sb1.append(",");
        }
        sb1.deleteCharAt(sb1.lastIndexOf(","));

        sb1.append(") VALUES (");


        for (Map<String, Object> map : lMapC) {
            StringBuilder sb2 = new StringBuilder();
            for (String title : titleList) {
                if (map.get(title) == null)
                    sb2.append("null");
                else {
                    sb2.append("'");
                    sb2.append(map.get(title).toString().replace("'", "''"));
                    //sb2.append(map.get(title).toString());
                    sb2.append("'");
                }
                sb2.append(",");
            }
            sb2.deleteCharAt(sb2.lastIndexOf(","));
            sb2.append(");\n");
            sbT.append(sb1).append(sb2);
        }
        return sbDel.toString()+sbT.toString();
    }

    public int inputConf(List<String> insertList) {

        try {
            FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
            for (String querySql : insertList) {
                if (querySql.contains("INSERT")) {
                    Map<String, Object> param = new HashMap<>();
                    param.put("querySql", querySql+")");
                    fileGenMapper.executeSql(param);
                }
            }
        } catch (Exception e) {
            logger.error("接口setFullVersion异常检查参数是否正确传递");
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public Path downLoadModelFile(String category, String rowCode, String versionId) {

        List<Map<String, Object>> nameList = null;
        FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
        Map<String, Object> param = new HashMap<>();
        param.put("rowCode", rowCode);
        param.put("versionId", versionId);
        if ("Word".equals(category)) {
            nameList = fileGenMapper.getWordName(param);
        }
        if ("Excel".equals(category)) {
            nameList = fileGenMapper.getExcelName(param);
        }
        String fileName = nameList.get(0).get("fileName").toString();

        Path p = Paths.get(propertyUtil.getPropValue("tmpModel"), rowCode + "V" + versionId, fileName);

        return p;
    }

    public int preGenFile(String category, String rowCode, String versionId, String audTrm, String prvdId) {
        try {
            FileGenData fileGenData = new FileGenData();
            fileGenData.setAudTrm(audTrm);
            fileGenData.setPrvdId(prvdId);
            fileGenData.setFileCode(rowCode);
            fileGenData.setVersionId(versionId);
            if ("Word".equals(category)) {
                fileGenData.setFileFormatType("0");
            }
            if ("Excel".equals(category)) {
                fileGenData.setFileFormatType("1");
            }
            Path p = Paths.get(propertyUtil.getPropValue("tmpPreFile"), rowCode + "V" + versionId);
            try {
                Files.createDirectory(p);
            } catch (Exception e) {
                logger.error("DIR:" + p.toString() + "has already existed");
            }
            String fileName = asposeUtil.work(fileGenData,propertyUtil.getPropValue("tmpModel"),p.toString());
            File file = Paths.get(p.toString(),fileName).toFile();

            if ("Word".equals(category)) {
                asposeUtil.word2Html(file,fileName,rowCode,versionId,propertyUtil.getPropValue("tmpPreFile"),1);
            }
            if ("Excel".equals(category)) {
                asposeUtil.excel2Html(file,fileName,rowCode,versionId,propertyUtil.getPropValue("tmpPreFile"),1);
            }
        } catch (Exception e) {
            logger.error("接口preGenFile异常检查参数是否正确传递");
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public Path downPreGenFile(String category, String rowCode, String versionId, String audTrm, String prvdId) {

        List<Map<String, Object>> nameList = null;
        FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
        Map<String, Object> param = new HashMap<>();
        param.put("rowCode", rowCode);
        param.put("versionId", versionId);
        if ("Word".equals(category)) {
            nameList = fileGenMapper.getWordName(param);
        }
        if ("Excel".equals(category)) {
            nameList = fileGenMapper.getExcelName(param);
        }
        String fileTpName = nameList.get(0).get("fileName").toString();


        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("audTrm", audTrm);
        paramMap.put("prvdName", Constants.MAP_PROVD_NAME.get(Integer.parseInt(prvdId)));
        String fileName = wfg.replaceParam(fileTpName, paramMap);


        Path p = Paths.get(propertyUtil.getPropValue("tmpPreFile"), rowCode + "V" + versionId, fileName);

        return p;
    }

    public List<Map<String, Object>> getPreGenAudTrm(String subjectId){
        FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
        Map<String, Object> param = new HashMap<>();
        param.put("subjectId", subjectId);
        return fileGenMapper.getPreGenAudTrm(param);

    }
    public List<Map<String, Object>> getPreGenPrvd(String subjectId){
        FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
        Map<String, Object> param = new HashMap<>();
        param.put("subjectId", subjectId);
        return fileGenMapper.getPreGenPrvd(param);
    }
}