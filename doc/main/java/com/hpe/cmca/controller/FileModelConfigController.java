package com.hpe.cmca.controller;


import com.aspose.words.Document;
import com.google.common.io.ByteStreams;
import com.hpe.cmca.common.AsposeUtil;
import com.hpe.cmca.interfaces.FileGenMapper;
import com.hpe.cmca.pojo.ExcelModelConfig;
import com.hpe.cmca.pojo.WordModelConfig;
import com.hpe.cmca.service.FileModelConfigService;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.util.*;

@Controller
@RequestMapping("/fileModelConfig")
public class FileModelConfigController {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FileModelConfigController.class);

    @Autowired
    AsposeUtil asposeUtil;
    @Autowired
    FileModelConfigService fileModelConfigService;

    @RequestMapping(value = "index")
    public String index() {
        return "fileModelConfig/index";
    }

    @ResponseBody
    @RequestMapping(value = "getSubject", produces = "text/json; charset=UTF-8")
    public String getSubject() {
        Map<String, Object> tpMap = new HashMap<>();
        tpMap.put("subjectList", fileModelConfigService.getSubject());
        return Json.Encode(tpMap);
    }

    @ResponseBody
    @RequestMapping(value = "getModeltype", produces = "text/json; charset=UTF-8")
    public String getModeltype(Integer subjectId, String category) {
        Map<String, Object> tpMap = new HashMap<>();
        tpMap.put("modelType", fileModelConfigService.getModeltype(subjectId, category));
        return Json.Encode(tpMap);
    }

    @ResponseBody
    @RequestMapping(value = "getTableData", produces = "text/json; charset=UTF-8")
    public String getTableData(Integer subjectId, String category, String modelTypeId) {
        Map<String, Object> tpMap = new HashMap<>();
        tpMap.put("tableData", fileModelConfigService.getTableData(subjectId, category, modelTypeId));
        return Json.Encode(tpMap);
    }

    @ResponseBody
    @RequestMapping(value = "getVersionData", produces = "text/json; charset=UTF-8")
    public String getVersionData(String rowCode, String versionId) {
        Map<String, Object> tpMap = new HashMap<>();
        tpMap.put("verisonData", fileModelConfigService.getVersionData(rowCode, versionId));
        return Json.Encode(tpMap);
    }

    @ResponseBody
    @RequestMapping(value = "deleteByVersion", produces = "text/json; charset=UTF-8")
    public String deleteByVersion(String rowCode, String versionId) {
        Map<String, Object> tpMap = new HashMap<>();
        tpMap.put("deleteInfo", fileModelConfigService.deleteByVersion(rowCode, versionId));
        return Json.Encode(tpMap);
    }

    @ResponseBody
    @RequestMapping(value = "getEditInfoData", produces = "text/json; charset=UTF-8")
    public String selectModelByIds(String category, String rowCode, String versionId, @RequestParam(value = "sheetOrder", required = false) String sheetOrder, String blockOrder) {
        Map<String, Object> tpMap = new HashMap<>();
        if ("Word".equals(category))
            tpMap.put("editInfoData", fileModelConfigService.selectWordModelByIds(rowCode, versionId, blockOrder));
        if ("Excel".equals(category))
            tpMap.put("editInfoData", fileModelConfigService.selectExcelModelByIds(rowCode, versionId, sheetOrder, blockOrder));
        return Json.Encode(tpMap);
    }

    @ResponseBody
    @RequestMapping(value = "getModelTxtNoHandle", produces = "text/json; charset=UTF-8")
    public String getModelTxtNoHandle(String category, String rowCode, String versionId, @RequestParam(value = "sheetOrder", required = false) String sheetOrder) {
        Map<String, Object> tpMap = new HashMap<>();
        tpMap.put("txtNoHandle", fileModelConfigService.getModelTxtHandle(category, rowCode, versionId, sheetOrder, "", 0));
        return Json.Encode(tpMap);
    }

    @ResponseBody
    @RequestMapping(value = "getModelTxtHandleOne", produces = "text/json; charset=UTF-8")
    public String getModelTxtHandleOne(String category, String rowCode, String versionId, @RequestParam(value = "sheetOrder", required = false) String sheetOrder) {

        Map<String, Object> tpMap = new HashMap<>();
        tpMap.put("txtHandleOne", fileModelConfigService.getModelTxtHandle(category, rowCode, versionId, sheetOrder, "One", 0));
        return Json.Encode(tpMap);
    }

    @ResponseBody
    @RequestMapping(value = "getModelTxtHandleTwo", produces = "text/json; charset=UTF-8")
    public String getModelTxtHandleTwo(String category, String rowCode, String versionId, @RequestParam(value = "sheetOrder", required = false) String sheetOrder) {

        Map<String, Object> tpMap = new HashMap<>();
        tpMap.put("txtHandleTwo", fileModelConfigService.getModelTxtHandle(category, rowCode, versionId, sheetOrder, "Two", 0));
        return Json.Encode(tpMap);
    }


    @ResponseBody
    @RequestMapping(value = "getExcelSheetName", produces = "text/json; charset=UTF-8")
    public String getExcelSheetName(String rowCode, String versionId) {
        Map<String, Object> tpMap = new HashMap<>();
        tpMap.put("sheetNameData", fileModelConfigService.getExcelSheetName(rowCode, versionId));
        return Json.Encode(tpMap);
    }

    @ResponseBody
    @RequestMapping(value = "getFileName", produces = "text/json; charset=UTF-8")
    public String getFileName(Integer subjectId, String category, String modelTypeId) {
        Map<String, Object> tpMap = new HashMap<>();
        tpMap.put("fileList", fileModelConfigService.getImportName(subjectId, category, modelTypeId));
        return Json.Encode(tpMap);
    }

    @ResponseBody
    @RequestMapping(value = "updateFileModel", produces = "text/json; charset=UTF-8", method = RequestMethod.POST)
    public String updateFileModel(HttpServletRequest resquest, String category,
                                  @RequestParam(value = "rowCode", required = false) String rowCode,
                                  @RequestParam(value = "sheetOrder", required = false) String sheetOrder,
                                  @RequestParam(value = "blockOrder", required = false) String blockOrder,
                                  @RequestParam(value = "isWideTb", required = false) String isWideTb,
                                  @RequestParam(value = "wideTbMonth", required = false) String wideTbMonth,
                                  @RequestParam(value = "wideTbCol", required = false) String wideTbCol,
                                  @RequestParam(value = "audTrmOption", required = false) String audTrmOption,
                                  @RequestParam(value = "querySql", required = false) String querySql,
                                  @RequestParam(value = "versionId", required = false) String versionId,
                                  @RequestParam(value = "showType", required = false) String showType,
                                  @RequestParam(value = "showSql", required = false) String showSql,
                                  @RequestParam(value = "mergeLast", required = false) String mergeLast) {

        Map<String, Object> tpMap = new HashMap<>();

        try {
            String reviser = resquest.getSession().getAttribute("userName").toString();

            Map<String, Object> param = new HashMap<>();
            param.put("excelCode", rowCode);
            param.put("versionCode", versionId);
            param.put("sheetOrder", sheetOrder);
            param.put("blockOrder", blockOrder);
            param.put("isWideTb", isWideTb);
            param.put("wideTbMonth", wideTbMonth);
            param.put("wideTbCol", wideTbCol);
            param.put("audTrmOption", audTrmOption);
            //param.put("querySql", querySql.replace("'", "''"));
            param.put("querySql", querySql);
            param.put("wordCode", rowCode);
            param.put("showType", showType);
            //param.put("showSql", showSql.replace("'", "''"));
            param.put("showSql", showSql);
            param.put("mergeLast", mergeLast);

            param.put("reviser", reviser);

            if ("Word".equals(category)) {
                fileModelConfigService.updateWordModel(param);
            }
            if ("Excel".equals(category)) {
                fileModelConfigService.updateExcelModel(param);
            }
        } catch (Exception e) {
            tpMap.put("updateInfo", 0);
            return Json.Encode(tpMap);
        }
        tpMap.put("updateInfo", 1);
        return Json.Encode(tpMap);

    }

    @ResponseBody
    @RequestMapping(value = "setDefaultVersion", produces = "text/json; charset=UTF-8")
    public String setDefaultVersion(String category, String rowCode, String versionId) {

        Map<String, Object> tpMap = new HashMap<>();
        tpMap.put("setDefaultInfo", fileModelConfigService.setDefaultVersion(category, rowCode, versionId));
        return Json.Encode(tpMap);
    }

    @ResponseBody
    @RequestMapping(value = "setFullVersion", produces = "text/json; charset=UTF-8")
    public String setFullVersion(String category, String rowCode, String versionId) {
        ;
        Map<String, Object> tpMap = new HashMap<>();
        tpMap.put("setFullInfo", fileModelConfigService.setFullVersion(category, rowCode, versionId));
        return Json.Encode(tpMap);
    }

    @ResponseBody
    @RequestMapping(value = "webUploader", produces = "text/json; charset=UTF-8")
    public String webUploader(@RequestParam("file") MultipartFile file, HttpServletRequest resquest, String category, String rowCodeVersionId) throws Exception {

        String rowCode = rowCodeVersionId.split("V")[0];
        String versionId = rowCodeVersionId.split("V")[1];
        int i = fileModelConfigService.webUploader(file, resquest, category, rowCode, versionId);
        return Json.Encode(i);

    }


    @RequestMapping(value = "outputConf", produces = "text/json; charset=UTF-8")
    public void outputConf(HttpServletRequest request, HttpServletResponse response, String category, String rowCode, String versionId) {
        response = FileUtil.handleResponseDown(request, response, category + "-" + rowCode + "V" + versionId + ".sql");
        try {
            String sqlTxt = fileModelConfigService.outPutConf(category, rowCode, versionId);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(sqlTxt.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "inputConf", produces = "text/json; charset=UTF-8")
    public String inputConf(@RequestParam("file") MultipartFile file, String category, String rowCode, String versionId) {
        //
//        rowCode="13-0-0-2-1";
//        versionId="9.0";
        int i = 0;
        try {
            String originalFilename = file.getOriginalFilename();

            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
            System.out.println(fileType);
            if (!".sql".equals(fileType))
                return Json.Encode(0);

            String inputRowCode = originalFilename.substring(originalFilename.indexOf("-") + 1, originalFilename.indexOf("V"));
            String inputVersionId = originalFilename.substring(originalFilename.indexOf("V") + 1, originalFilename.indexOf(".0") + 2);
            if (!inputRowCode.equals(rowCode)) {
                return Json.Encode(2);
            }

            InputStream inputStream = file.getInputStream();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String str = result.toString("UTF-8");
            inputStream.close();
            result.close();
            if ("".equals(str))
                return Json.Encode(i);
            if (!inputVersionId.equals(versionId)) {
                str = str.replace("'" + inputVersionId + "'", "'" + versionId + "'").replace("\n", " ").replace("\t", " ").replace("\r", " ");
            }
            List<String> insertList = Arrays.asList(str.split("\\);"));
            i = fileModelConfigService.inputConf(insertList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Json.Encode(i);
    }

    @RequestMapping(value = "downLoadModelFile", produces = "text/json; charset=UTF-8")
    public void downLoadModel(HttpServletRequest request, HttpServletResponse response, String category, String rowCode, String versionId) {
        try {

            Path p = fileModelConfigService.downLoadModelFile(category, rowCode, versionId);
            response = FileUtil.handleResponseDown(request, response, p.getFileName().toString());
            InputStream inputStream = new FileInputStream(p.toFile());
            ByteStreams.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            logger.error("接口downLoadModelFile异常检查参数是否正确传递");
            e.printStackTrace();
        }


    }

    @ResponseBody
    @RequestMapping(value = "preGenFile", produces = "text/json; charset=UTF-8")
    public String preGenFile(String category, String rowCode, String versionId, String audTrm, String prvdId) {
        int a = fileModelConfigService.preGenFile(category, rowCode, versionId, audTrm, prvdId);
        return Json.Encode(a);
    }

    @ResponseBody
    @RequestMapping(value = "getPreGenFile", produces = "text/json; charset=UTF-8")
    public String getPreGenFile(String category, String rowCode, String versionId, @RequestParam(value = "sheetOrder", required = false) String sheetOrder) {
        Map<String, Object> tpMap = new HashMap<>();
        tpMap.put("preGenFile", fileModelConfigService.getModelTxtHandle(category, rowCode, versionId, sheetOrder, "", 1));
        return Json.Encode(tpMap);
    }


    @RequestMapping(value = "downPreGenFile", produces = "text/json; charset=UTF-8")
    public void downPreGenFile(HttpServletRequest request, HttpServletResponse response, String category, String rowCode, String versionId, String audTrm, String prvdId) {
        try {
            Path p = fileModelConfigService.downPreGenFile(category, rowCode, versionId, audTrm, prvdId);
            response = FileUtil.handleResponseDown(request, response, p.getFileName().toString());
            InputStream inputStream = new FileInputStream(p.toFile());
            ByteStreams.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            logger.error("接口downPreGenFile异常检查参数是否正确传递");
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getPreGenAudTrmAndPrvd", produces = "text/json; charset=UTF-8")
    public String getPreGenAudTrmAndPrvd(String subjectId) {
        Map<String, Object> tpMap = new HashMap<>();
        tpMap.put("preGenAudTrm", fileModelConfigService.getPreGenAudTrm(subjectId));
        tpMap.put("preGenPrvd", fileModelConfigService.getPreGenPrvd(subjectId));
        return Json.Encode(tpMap);

    }

    @RequestMapping(value = "testWord", produces = "text/json; charset=UTF-8")
    public void testWord() throws Exception {
        asposeUtil.testWord();

    }


//    @ResponseBody
//    @RequestMapping(value = "getWordHtmlTxt", produces = "text/json; charset=UTF-8")
//    public String getWordHtmlTxt() throws IOException {
//        String resultTxt = asposeUtil.word2Html();
//        Map<String,String> result =new HashMap<String,String>();
//        result.put("txt",resultTxt);
//        return Json.Encode(result);
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "getExcelHtmlTxt", produces = "text/json; charset=UTF-8")
//    public void getExcelHtmlTxt() throws IOException {
//        asposeUtil.excel2Html();
////        String resultTxt = asposeUtil.excel2Html();
////        Map<String,String> result =new HashMap<String,String>();
////        result.put("txt",resultTxt);
////        return Json.Encode(result);
//    }
}
