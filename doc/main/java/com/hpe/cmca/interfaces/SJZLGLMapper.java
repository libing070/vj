/**
 * com.hpe.cmca.interfaces.SystemLogMgMapper.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.interfaces;

import com.hpe.cmca.pojo.*;

import java.util.List;
import java.util.Map;


/*
 * @Author ZhangQiang
 * @Description //TODO 
 * @Date 16:40 2018/10/24
 * @Param
 * @return 
 **/
public interface SJZLGLMapper {

    // 顶部查询-审计月和专题
    public List<Map<String, Object>>  getAudTrm();
    public List<Map<String, Object>>  getSubject();
    //数据质量稽核点详情-顶部-区域list
    public List<Map<String, Object>>  getDetPrvd();
    //数据质量稽核点详情-顶部-接口list
    public List<Map<String, Object>>  getPortList(SJZLGLParamData sjzlglParamData);
    //数据质量稽核点详情-顶部-稽核点list
    public List<Map<String, Object>>  getDetjihePoint(SJZLGLParamData sjzlglParamData);
    //数据质量稽核点详情-顶部-稽核点list
    public List<Map<String, Object>>  getDetjihePointNew(SJZLGLParamData sjzlglParamData);
    //数据质量稽核点详情-顶部-稽核点list
    public List<Map<String, Object>>  getAuditCodeOfSheetCode(SJZLGLParamData sjzlglParamData);

    //数据质量情况概览
    public List<Map<String, Object>>  getOverviewList(SJZLGLParamData sjzlglParamData);

    //数据质量详情汇总
    public List<Map<String, Object>>  getSummarizedList(SJZLGLParamData sjzlglParamData);

    //稽核点异常情况统计
    public List<Map<String, Object>>  getSummarizedDetail(SJZLGLParamData sjzlglParamData);

    //稽核点异常情况统计-编辑前保存历史数据
    public Integer  getSummarizedSaveBakDet(SJZLGLParamData sjzlglParamData);

    //稽核点异常情况统计-编辑保存
    public Integer  getSummarizedSaveState(SJZLGLParamData sjzlglParamData);

    //数据质量稽核点详情-title
    public List<Map<String, Object>>  getJihePointAutoFileTab(SJZLGLParamData sjzlglParamData);
    //数据质量稽核点详情-高亮标志
    public List<Map<String, Object>>  getFalgGlInfo(SJZLGLParamData sjzlglParamData);

    //执行sql
    public List<Map<String, Object>>  executeSql(SJZLGLParamData sjzlglParamData);

    //数据质量报告下载--表格数据
    public List<Map<String, Object>>  getDownloadInfo(SJZLGLParamData sjzlglParamData);

    //数据质量报告下载--表格数据
    public Integer  setDownloadInfo(SJZLGLParamData sjzlglParamData);

    //数据质量报告下载--备份历史数据
    public Integer  insertDownloadHist(SJZLGLParamData sjzlglParamData);

    //数据质量报告下载--获取下载链接
    public List<Map<String, Object>> getDownLoadUrl(Map<String, Object> map);
    //数据质量报告下载--获取下载链接
    public List<Map<String, Object>> getDownLoadUrlWeb(Map<String, Object> map);
    //数据质量报告下载--修改下载信息
    public Integer  updateDownloadInfo(SJZLGLParamData sjzlglParamData);
    //数据质量报告下载--修改下载信息
    public Integer  updateDownloadInfo1(SJZLGLParamData sjzlglParamData);
    //数据质量报告下载--修改下载信息
    public Integer  updateDownloadInfo2(SJZLGLParamData sjzlglParamData);

    //数据质量报告下载--查询下载信息中的下载次数
    public List<Map<String, Object>>    queryDownNum(SJZLGLParamData sjzlglParamData);

    //数据质量报告下载--手动生成弹窗信息数据
    public List<Map<String, Object>>  getManualInfoDel(SJZLGLParamData sjzlglParamData);
    //数据质量报告下载--手动生成弹窗信息数据
    public List<Map<String, Object>>  getManualInfoSub(SJZLGLParamData sjzlglParamData);


    //数据质量报告下载--变更配置表信息
    public Integer  updateStatusByFocusCd(SJZLGLParamData sjzlglParamData);
    //数据质量报告下载--手动生成弹窗信息数据
    public Integer  updateStatusById(Map<String, Object> map);
    //数据质量报告下载--查询Excel名称
    public List<Map<String, Object>> getExcName(SJZLGLParamData sjzlglParamData);

    //数据质量影响评估
    public List<Map<String, Object>>  getAssessment(SJZLGLParamData sjzlglParamData);
    //数据质量影响评估 - 稽核点异常
    public List<Map<String, Object>>  getAssessment2(SJZLGLParamData sjzlglParamData);
    //数据质量影响评估- 影响模型异常稽核点
    public List<Map<String, Object>>  getAssessment3(SJZLGLParamData sjzlglParamData);
    //数据质量影响评估--编辑--保存
    public Integer  getSaveState(SJZLGLParamData sjzlglParamData);
    //数据质量影响评估--编辑--保存-数据备份
    public Integer  getInsertState(SJZLGLParamData sjzlglParamData);

    //数据质量影响评估--异常稽核点弹窗表格数据
    public List<Map<String, Object>>  getErrorAuditData(SJZLGLParamData sjzlglParamData);

    //数据质量影响评估--影响模型异常稽核点弹窗表格数据
    public List<Map<String, Object>>  getInfluenceAuditData(SJZLGLParamData sjzlglParamData);

    //数据质量影响评估--重传次数弹窗表格数据
    public List<Map<String, Object>>  getRetransmissionInfo(SJZLGLParamData sjzlglParamData);

    //数据质量报告下载--校验配置信息
    public List<Map<String, Object>>  getFileGenList(Map<String, Object> map);
}
