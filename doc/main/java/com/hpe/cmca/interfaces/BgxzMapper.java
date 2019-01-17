package com.hpe.cmca.interfaces;

import java.util.List;
import java.util.Map;

import com.hpe.cmca.pojo.AudTrmData;
import com.hpe.cmca.pojo.BgxzData;



public interface BgxzMapper {
	
    public List<Map<String,Object>> downNumsBySubject(BgxzData bgxz);
    
    public List<Map<String,Object>> downNumsByPrvd(BgxzData bgxz);
    
    public List<Map<String,Object>> createDayBySubjectMonth(BgxzData bgxz);
    
    public List<Map<String,Object>> downRecordsTable(BgxzData bgxz);
    
    public List<Map<String,Object>> selReportLog(Map<String, Object> params);

    // 2018-12-24 18:03:27  zhangqiang add
    public List<Map<String,Object>> selReportLog_new(Map<String, Object> params);

    public List<Map<String,Object>> selReportLogCsv(Map<String, Object> params);
    
    public List<Map<String,Object>> selReportPerson(Map<String, Object> params);
    
    public List<Map<String,Object>> selReportUpload(BgxzData bgxz);
    
    public Integer countReportUpload(BgxzData bgxz);
    
    public void updateReportLog(Map<String, Object> params);

    // 2018-12-24 18:03:27  zhangqiang add
    public void updateReportLog6(Map<String, Object> params);
    
    public void updateReportLog2(Map<String, Object> params);
    
    
    public void updateReportLog7(Map<String, Object> params);
    
    public void deleteReportLog(Map<String, Object> params);
    
    public void updateReportUpload(BgxzData bgxz);
    
    public void reviewReportUpload(Map<String, Object> params);
    
    public void addReportUpload(BgxzData bgxz);
    
    public void delUploadFile(Map<String, Object> params);
    
    public List<Map<String,Object>> downUploadFile(Map<String,Object> params);
    
    public List<Map<String,Object>> selSubjectName(Map<String,Object> params);
    
    public void addReportLog(Map<String, Object> params);
    
    public List<Map<String,Object>> getAudTrmDataTrmConf1(Map<String, Object> params);
    
    public List<Map<String,Object>> getAudTrmDataTrmConf2(Map<String, Object> params);
    
    public List<Map<String,Object>> getAudTrmDataTrmConf3(Map<String, Object> params);
    
    public List<Map<String,Object>> getAuditMonth(Map<String, Object> params);
}
