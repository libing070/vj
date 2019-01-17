package com.hpe.cmca.interfaces;

import com.hpe.cmca.pojo.AudTrmData;
import com.hpe.cmca.pojo.ExcelModelConfig;
import com.hpe.cmca.pojo.FileGenData;
import com.hpe.cmca.pojo.WordModelConfig;

import java.util.List;
import java.util.Map;


public interface FileGenMapper {


    public List<FileGenData> getFileGenData(Map<String, Object> map);

    public List<WordModelConfig> selectWordModel(Map<String, Object> map);

    public List<ExcelModelConfig> selectExcelModel(Map<String, Object> map);

    public void insertGenRecord(Map<String, Object> map);

    public void deleteGenRecord(Map<String, Object> map);

    public List<Map<String, Object>> selectZipRule(Map<String, Object> map);

    public List<Map<String, Object>> getSubject();

    public List<Map<String, Object>> getModeltype(Map<String, Object> map);

    public List<Map<String, Object>> getTableData(Map<String, Object> map);

    public List<Map<String, Object>> getVersion(Map<String, Object> map);

    public List<Map<String, Object>> getMaxVersion(Map<String, Object> map);

    public List<Map<String, Object>> getVersionData(Map<String, Object> map);

    public void deleteVersionWord(Map<String, Object> map);

    public void deleteVersionExcel(Map<String, Object> map);

    public List<WordModelConfig> selectWordModelByIds(Map<String, Object> map);

    public List<ExcelModelConfig> selectExcelModelByIds(Map<String, Object> map);

    public List<Map<String, Object>> getModelFileName(Map<String, Object> map);

    public List<Map<String, Object>>getExcelSheetName(Map<String, Object> map);

    public void insertExcelModel(Map<String, Object> map);

    public void insertWordModel(Map<String, Object> map);

    public List<Map<String, Object>>getBlockList(Map<String, Object> map);

    public void updateWordModel(Map<String, Object> map);

    public void updateExcelModel(Map<String, Object> map);

    public void cancelWordDefaultVersion(Map<String, Object> map);
    public void setWordDefaultVersion(Map<String, Object> map);
    public void cancelExcelDefaultVersion(Map<String, Object> map);
    public void setExcelDefaultVersion(Map<String, Object> map);

    public void setExcelFullVersion(Map<String, Object> map);
    public void setWordFullVersion(Map<String, Object> map);


    public List<Map<String, Object>>getOutputExcelConfTitle(Map<String, Object> map);
    public List<Map<String, Object>>getOutputExcelConfContent(Map<String, Object> map);
    public List<Map<String, Object>>getOutputWordConfTitle(Map<String, Object> map);
    public List<Map<String, Object>>getOutputWordConfContent(Map<String, Object> map);


    public List<Map<String, Object>> executeSql(Map<String, Object> map);

    public List<Map<String, Object>> getExcelName(Map<String, Object> map);
    public List<Map<String, Object>> getWordName(Map<String, Object> map);

    public List<Map<String, Object>> getPreGenAudTrm(Map<String, Object> map);
    public List<Map<String, Object>> getPreGenPrvd(Map<String, Object> map);

}
