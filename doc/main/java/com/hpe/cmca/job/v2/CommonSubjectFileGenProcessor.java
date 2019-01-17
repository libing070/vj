/**
 * com.hp.cmcc.job.service.CommonSubjectFileGenProcessor.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job.v2;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.finals.Constants;
import com.hpe.cmca.finals.Dict;
import com.hpe.cmca.pojo.BgxzData;
import com.hpe.cmca.service.BgxzService;
import com.hpe.cmca.service.ConcernFileGenService;
import com.hpe.cmca.service.ConcernService;
import com.hpe.cmca.service.YgycService;
import com.hpe.cmca.util.DateUtilsForCurrProject;
import com.hpe.cmca.util.ExceptionTool;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;

/**
 * <pre>
 * 汇总审计报告的生成流程如下：
 * 1. 获取模型执行完毕的消息，即生成文件的请求消息，开始循环处理{接下来会将查到数据按"省编码"和"审计周期"一波一波执行，且"10000的省编码及各个审计月"会在最后一波一波(多个审计月)执行}；
 * 	sql：SELECT * FROM HPMGR.busi_model_notify  WHERE status<>5 and subject_id in ( '2' ) and focus_cd in ( 2002 ) order by prvd_id desc
 * 	sql：SELECT * FROM HPMGR.busi_model_notify  WHERE status<>5 and subject_id in ( '1' ) and focus_cd in ( 1000,1001,1002,1003,1004,1005  ) order by prvd_id desc
 * 	sql：SELECT * FROM HPMGR.busi_model_notify  WHERE status<>5 and subject_id in ( '3' ) and focus_cd in ( 3000,3001,3002,3004,3005 ) order by prvd_id desc
 * 2. 获取省编码和审计周期；
 * 		获取当前汇总关注点（生成审计报告和明细相关）的配置信息，以便作为生成csv和doc文件的参数；
 * 		获取当前汇总关注点的配置信息，作为生成csv和doc文件的参数（部分主要参数信息如下）；
 *  	csv_header : 生成审计清单的excel表头
 *  	csv_sql : 生成审计清单的excel表数据
 *  	doc_template_path : 生成当前专题关注点审计报告所需的word模板在项目中的绝对路径（值为：/wordTemplate ）
 *  	doc_template : 生成当前专题关注点审计报告所需的word XML模板文件名
 *  	category：生成审计报告数据的sql的查询条件
 * 	sql：SELECT * FROM HPMGR.busi_report_file_config where subject_id in ('1') and focus_cd in (1000)；// 有价卡job
 * 		SELECT * FROM HPMGR.busi_report_file_config where subject_id in ('2') and focus_cd in (2002)；// 渠道养卡job，因为渠道养卡没有汇总关注点，所以，用其唯一的子关注点作为汇总关注点
 * 		SELECT * FROM HPMGR.busi_report_file_config where subject_id in ('3') and focus_cd in (3000)；// 终端套利job
 * 3. 根据缓存记录（执行情况记录器），判断此"省份编码"和"审计周期"是否在本次任务中生成过文件(一次任务中:一个专题一个省一个周期内，所有汇总or子关注点的notify记录只生成一次文件就够了)
 * 	  3.1 如果生成过文件，则跳过此请求迭代处理下一条请求，改状态为已处理；
 * 			sql：update HPMGR.busi_model_notify status= 5 where id=#{modelNotifyId}
 * 	  3.2 如果没生成过文件，则继续下一步；
 * 4. 判断该省该周期的关注点模型完成情况(个数)是否是n（注意此时无需考虑状态，程序传入status参数为null；另外：jyk：n为5，qdyk：n为1，zdtl：n为4)；
 * 	   sql：SELECT distinct focus_cd as focusCd FROM HPMGR.busi_model_notify where  1=1 and aud_trm='201603' and prvd_id=12900 and subject_id='1'
 * 	  	<!-- 根据busi_model_notify表判断同一审计周期某个省已经执行完毕的关注点的数量-->
 * 		<select id="selectFinishedConcerns" resultType="java.util.Map" parameterType="java.util.Map">
 * 			SELECT distinct focus_cd as focusCd
 * 			FROM HPMGR.busi_model_notify
 * 			where aud_trm=#{audTrm}
 * 		 	<if test="prvdId != null and prvdId != '' ">
 * 	            and prvd_id=#{prvdId}
 * 	     	</if>
 * 		 	<if test="subjectId != null and subjectId != '' ">
 * 	           and subject_id=#{subjectId}
 * 	    	</if>
 * 		  	<if test="status != null and status>0">
 * 	           and status=#{status}
 * 	     	</if>
 * 		</select>
 *     4.1 如果小于n，则跳过此请求处理下一请求；
 * 		// 注：由于后台模型运行时，每个专题汇总关注点请求记录是最后生成；所以，判断是否可以生成省级审计报告，只需要看distinct focusCd是否大于等于n条记录；
 * 	   4.2  如果大于等于n，则生成文件doc,csv且更改所有请求的状态，同时在内存中记录已经生成文件的省编码信息和周期；
 * 5. 判断省编码是否是集团公司，即省编码是不是10000；
 * 		5.1 如果是10000，则只生成doc文件并更改请求状态，请示在内存中记录已经生成文件的"省公司编码"和"审计周期"。生成完毕之后跳出处理下一条请求；
 * 			5.1.1 生成"全公司"审计报告doc，更新数据库请求记录状态（2-doc文件生成完毕）
 * 				sql：select * from HPMGR.busi_stat_concern where subject_id =1 order by order_index // 各关注点基础配置信息
 * 				sql： // 通过汇总关注点ID查询其专题的审计报告中数据配置信息；所有审计报告中的数据，都是基于 data.ref_value 的sql查出来的
 * 					 // data.ref_value是定时任务使用的SQL ID，可以从配置文件 yjk_1000.xml、qdyk_2002.xml、zdtl_3000.xml 中查询相应SQL
 * 					select data.memo, data.ref_name, data.ref_value, data.* from HPMGR.busi_stat_concern_page page, busi_stat_concern_data data
 * 				where page.concern_id in ('1',  '22' , '30') and page.category='auditReport' and page.id=data.page_id
 * 			5.1.2 从数据库中查询已生成的31份级审计报告doc文件名 + 刚生成的全省doc，进行打包
 * 				sql：SELECT file_name FROM HPMGR.busi_report_file where aud_trm='201603' and subject_id='1' and focus_cd='1000'  and file_type='audReport'
 * 			5.1.3 从数据库中查询已生成的31各省级审计明细csv文件名，进行打包
 * 				sql：SELECT file_name FROM HPMGR.busi_report_file where aud_trm='201603' and subject_id='1' and focus_cd='1000'  and file_type='audReport'
 * 			5.1.4 上传31省的Csv.zip，更新notify请求记录状态为3
 * 			5.1.5 上传32省的Doc.zip，更新notify请求记录状态为2，上传31省的Csv.zip，更新notify请求记录状态为4
 * 			5.1.6 删除HPMGR.busi_report_file表中csv原文件记录，插入新生成的csv文件记录
 * 			5.1.7 删除HPMGR.busi_report_file表中doc原文件记录，插入新生成的doc文件记录
 * 				sql：delete from HPMGR.busi_report_file where audit_monthly=#{auditMonthly} and audit_subject=#{auditSubject} and file_name=#{fileName}
 * 				sql：insert into HPMGR.busi_report_file (model_notify_id,file_name,file_path,download_url,file_type,audit_monthly,audit_subject,aduitor,audit_concern,create_time,create_person)
 * 						values (#{modelNotifyId},#{fileName},#{filePath},#{downloadUrl},#{fileType},#{auditMonthly},#{auditSubject},#{aduitor},#{auditConcern},#{createTime},#{createPerson})
 * 			5.1.8 更新notify请求记录状态为5
 * 		5.2 如果不是10000，则下一步；
 * 			5.2.1 生成省Csv文件，更新数据库请求记录状态
 * 			5.2.2 生成省Doc文件，更新数据库请求记录状态
 * 			5.2.3 上传Csv文件，更新数据库请求记录状态
 * 			5.2.4 上传Doc文件，更新数据库请求记录状态
 * 			5.2.5 删除HPMGR.busi_report_file表中csv原文件记录，插入新生成的csv文件记录
 * 			5.2.6 删除HPMGR.busi_report_file表中doc原文件记录，插入新生成的doc文件记录
 * 			5.2.7 更新数据库请求记录状态
 *  6 更新缓存记录（执行情况记录器）即，map(省ID_审计月,true);
 *
 * 附1. 各专题下汇总关注点与子关注点
 * 		有价卡（1000）： 1001, 1002, 1003, 1004, 1005
 * 		渠道养卡（2002）： 2002
 * 		终端套利（3000）：3001, 3002, 3004, 3005
 * 附2. HPMGR.busi_model_notify表status字段取值说明
 * 		0-模型运行完毕，1-csv文件生成完毕,2-doc文件生成完毕,3-(省csv文件 | 31省csv.zip)ftp完毕,4-(省doc文件 | 全公司 + 31省的 doc.zip)ftp完毕,5-处理完毕
 * 		// 实际环境中的 -1 和 999，对于，前端系统来说是 未知状态，无意义，未处理；==> 经开会讨论 这些状态会在各个专题脚本上线时，进行去除 20161121
 * 附3. 审计报告文件再web服务器上的存放位置
 * 		/data1/hp_web/caTmp/v2
 * 附4. 后台模型处理逻辑 20161020 by 顺利、新力
 * 		1. 第一次跑模型时，每个省份每个关注点跑完后会插入一条状态为0的记录；
 * 			假如这个省份的这个关注点模型重跑后，会将这条记录从0状态改成1状态，然后再插入一条状态为0的记录。
 * 			状态为0 的记录至多只有一条，假如模型重跑多次，则可能会有多条状态为1的记录。
 * 			// 这里的状态1，跟前台定义的1有歧义，待大家开会重新定义，明确。
 * 		2. 因为模型每次执行顺序都是五个关注点跑完后，再跑汇总，所以，汇总与其他关注点插notify是一样的。
 * 		3. 每次重跑模型后，都要重调一次汇总，所以notify表中的记录也会将状态从0变为1。
 * 		4. 每个关注点31个省份跑完后，都会插一条本专题汇总关注点的全公司记录（这样才能重新生成全公司的审计报告和清单）。
 * 附5. 审计报告 wordXML模板
 * 		yjk_1000.xml、QDYK2002.xml、ZDTL_3000.xml
 * 		sql：SELECT t.doc_template, t.* FROM HPMGR.busi_report_file_config  t where subject_id in ('1','2','3') and focus_cd in (1000, 2002, 3000)
 *
 * @author GuoXY
 * @refactor GuoXY
 * @date 20161019
 * @version 1.0
 * @see
 */
public class CommonSubjectFileGenProcessor extends BaseObject implements IFileGenProcessor {

    @Autowired
    private SjbgQdFileGenProcessor sjbgQdFileGenProcessor;

    @Autowired
    private YgycService ygycService;

    @Autowired
    protected ConcernFileGenService concernFileGenService = null;
    @Autowired
    protected ConcernService concernService = null;
    @Autowired
    protected BgxzService bgxzService = null;
    @Autowired
    protected Dict dict = null;
    @Autowired
    private JdbcTemplate jdbcTemplate = null;

    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
    protected SimpleDateFormat yyyyMMddHHmmssSdf = new SimpleDateFormat("yyyyMMddHHmmss");

    /*
     * (non-Javadoc)
     *
     * @see com.hp.cmcc.job.service.IFileGenProcessor#genFile(java.lang.String,java.lang.String, java.lang.String, int, java.util.Map, int, java.util.Map)
     */
    public void genFile(String audTrm, String subjectId, String totalFocusCds, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo,
                        Boolean useChineseName, Boolean flag) {

        this.logger.debug("#### 生成文件：subjectId=" + subjectId + ",汇总关注点totalFocusCds=" + totalFocusCds + ",focusCd=" + focusCd + ",prvdId=" + prvdId + ",audTrm=" + audTrm);

        if (!validateRequest(audTrm, subjectId, totalFocusCds, prvdId, request, modelNotifyId, configInfo)) {
            this.logger.error("#### 不满足生成文件的条件，专题处理器无法启动：subjectId=" + subjectId + ",汇总关注点totalFocusCds=" + totalFocusCds + ",focusCd=" + focusCd + ",prvdId=" + prvdId + ",audTrm=" + audTrm);
            return;
        }

        try {
            if (prvdId != Constants.ChinaCode) {// 生成省csv,doc
                genPrvdFile(audTrm, subjectId, totalFocusCds, focusCd, prvdId, request, modelNotifyId, configInfo, useChineseName, flag);
                return;
            }
            genAllFile(audTrm, subjectId, totalFocusCds, prvdId, request, modelNotifyId, configInfo, useChineseName);// 生成全国视角的文件doc

        } finally {
            this.logger.info("#### 模型数据文件生成完毕");
            // concernFileGenService.updateFileRequestExecCount(request);
            concernFileGenService.updateFileRequestExecCountBysubjectNew(request);// 按专题更新执行次数

        }
    }

    /**
     * <pre>
     * Desc  额外的验证条件，判断此请求是否需要生成文件 （由继承本类的processor覆盖本方法进行处理）
     * 返回true为验证通过，需要生成文件
     * 返回false为验证失败，不需要生成文件
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @param request
     * @param modelNotifyId
     * @param configInfo
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     */
    protected boolean validateRequest(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo) {
        return true;
    }

    /**
     * <pre>
     * Desc  在本地生成省的csv文件
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @param configInfo
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     * <p>
     * public File genProvCsvFile(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, Map<String, Object> request, Boolean useChineseName) {
     * if ("5".equals(subjectId) && focusCd.equals("5000")) {
     * return null;
     * }
     * // 获取新文件生成目录
     * String localFilePath = getLocalFilePath();
     * File localPath = new File(localFilePath);
     * if (localPath.exists() == false) {
     * localPath.mkdirs();
     * }
     * // 获取新文件名
     * String localFileName = buildFileName(Constants.Model.FileType.AUD_DETAIL, audTrm, subjectId, focusCd, prvdId, useChineseName);
     * this.logger.info("该专题汇总关注点(员工异常操作是各个子关注点)：是" + focusCd);
     * if ("5".equals(subjectId)||"6".equals(subjectId)) {
     * configInfo = concernFileGenService.selectFileConfig(subjectId, focusCd);
     * }
     * String sql = (String) configInfo.get("csvSql");
     * File file = new File(FileUtil.buildFullFilePath(localFilePath, localFileName));
     * Writer streamWriter = null;
     * try {
     * streamWriter = new OutputStreamWriter(new FileOutputStream(file), "GBK");
     * final PrintWriter printWriter = new PrintWriter(streamWriter);
     * printWriter.println((String) configInfo.get("csvHeader"));
     * jdbcTemplate.query(sql, new Object[] { audTrm, prvdId }, new RowCallbackHandler() {
     * <p>
     * public void processRow(ResultSet rs) throws SQLException {
     * int columCount = rs.getMetaData().getColumnCount();
     * StringBuilder line = new StringBuilder(100);
     * for (int i = 1; i <= columCount; i++) {
     * line.append(rs.getObject(i)).append("	,");
     * }
     * printWriter.println(line.substring(0, line.length() - 1));
     * }
     * });
     * <p>
     * printWriter.flush();
     * } catch (Exception e) {
     * throw new RuntimeException("生成csv文件异常", e);
     * } finally {
     * FileUtil.closeWriter(streamWriter);
     * }
     * return file;
     * }
     */
    @SuppressWarnings("resource")
	public File genProvCsvFile(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, Map<String, Object> request, Boolean useChineseName) {
        if ("5".equals(subjectId) && focusCd.equals("5000")) {
            return null;
        }
        if ("1".equals(subjectId) && !("1001".equals(focusCd) || "1012".equals(focusCd))) {
            return null;
        }
        if ("4".equals(subjectId) && !("4001".equals(focusCd) || "4003".equals(focusCd))) {
            return null;
        }
        if ("6".equals(subjectId) && !("6001".equals(focusCd) || "6002".equals(focusCd) || "600301".equals(focusCd) || "600302".equals(focusCd) || "6004".equals(focusCd))) {
            return null;
        }
        if ("7".equals(subjectId) && !("7003".equals(focusCd) || "700101".equals(focusCd)|| "700102".equals(focusCd))) {
            return null;
        }
        if ("12".equals(subjectId) &&!("1201".equals(focusCd)||"1203".equals(focusCd))) {
        	return null;
    	}
        if ("13".equals(subjectId) &&!("1301".equals(focusCd)||"1302".equals(focusCd))) {
            return null;
        }
        // 获取新文件生成目录
        String localFilePath = getLocalFilePath();
        File localPath = new File(localFilePath);
        if (localPath.exists() == false) {
            localPath.mkdirs();
        }
        //TODO 变更审计月份
        //TODO 变更审计月份

        // 获取新文件名
        String localFileName = "";
		if("13".equals(subjectId)||"12".equals(subjectId)){
			String newAudTrm=DateUtilsForCurrProject.getAfterAnyMonth(audTrm,1);
			localFileName = buildFileName(Constants.Model.FileType.AUD_DETAIL, newAudTrm, subjectId, focusCd, prvdId, useChineseName);
		}else{
			
			localFileName = buildFileName(Constants.Model.FileType.AUD_DETAIL, audTrm, subjectId, focusCd, prvdId, useChineseName);
		}
        if ("1001".equals(focusCd)) {
            localFileName = localFileName.replaceAll("未按规定在系统间同步加载", "有价卡违规管理");
        }
        //this.logger.info("该专题汇总关注点(员工异常操作是各个子关注点)：是" + focusCd);
        this.logger.info("关注点是" + focusCd);
        if ("5".equals(subjectId) || "6".equals(subjectId) || "1".equals(subjectId)|| "4".equals(subjectId) || "12".equals(subjectId)|| "13".equals(subjectId)) {
            configInfo = concernFileGenService.selectFileConfig(subjectId, focusCd);
        }
        String sql = (String) configInfo.get("csvSql");
        File file = new File(FileUtil.buildFullFilePath(localFilePath, localFileName));
        Writer streamWriter = null;
        try {
            streamWriter = new OutputStreamWriter(new FileOutputStream(file), "GBK");
            final PrintWriter printWriter = new PrintWriter(streamWriter);
            printWriter.println((String) configInfo.get("csvHeader"));
            jdbcTemplate.query(sql, new Object[]{audTrm, prvdId}, new RowCallbackHandler() {

                public void processRow(ResultSet rs) throws SQLException {
                    int columCount = rs.getMetaData().getColumnCount();
                    StringBuilder line = new StringBuilder(100);
                    for (int i = 1; i <= columCount; i++) {
                        line.append(rs.getObject(i)).append("	,");
                    }
                    printWriter.println(line.substring(0, line.length() - 1));
                }
            });

            printWriter.flush();
        } catch (Exception e) {
            throw new RuntimeException("生成csv文件异常", e);
        } finally {
            FileUtil.closeWriter(streamWriter);
        }
        return file;
    }

    /**
     * <pre>
     * Desc
     * 		第一步：生成省Csv文件，并打包，更新数据库请求记录状态
     * 		第三步：上传Csv.zip，更新数据库请求记录状态
     * 		第五步：向文件生成结果表HPMGR.busi_report_file中插入新生成的Csv文件信息
     *
     * 		第二步：生成省Doc文件，并打包，更新数据库请求记录状态
     * 		第四步：上传Doc.zip，更新数据库请求记录状态
     * 		第五步：向文件生成结果表HPMGR.busi_report_file中插入新生成的Doc文件信息
     *
     * 		第六步：更新数据库请求记录状态
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @author GuoXY
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     */
    public void genPrvdFile(String audTrm, String subjectId, String totalFocusCds, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo,
                            Boolean useChineseName, Boolean flag) {

        File csvFile = null;
        File docFile = null;
        File excelFile = null;
        try {
            this.logger.debug("#### 生成省文件:subjectId=" + subjectId + ",汇总关注点totalFocusCds=" + totalFocusCds + ",focusCd=" + focusCd + ",prvdId=" + prvdId + ",audTrm=" + audTrm);
            // 生成省Csv文件，更新数据库请求记录状态
            if ("5".equals(subjectId) || "6".equals(subjectId) || "1".equals(subjectId)|| "4".equals(subjectId)|| "7".equals(subjectId)||"12".equals(subjectId)||"13".equals(subjectId)) {
                csvFile = genProvCsvFile(audTrm, subjectId, focusCd, prvdId, configInfo, request, useChineseName);
            } else {
                csvFile = genProvCsvFile(audTrm, subjectId, totalFocusCds, prvdId, configInfo, request, useChineseName);
            }

            if (csvFile != null) {

                // File csvZipFile = FileUtil.zipOneFile(csvFile); //省公司doc.csv文件不应该打包上传，直接传文件即可，用户直接下载文件，直接看 modified by GuoXY 20161121
                request.put("csvFileGenTime", new Date());
                // modify by pxl 改为按专题更新notify,不再按id更新
                // updateFileRequestStatus(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.CSV_FILE_FINISHED);
                updateFileRequestStatusBySubjectIdNew(modelNotifyId, audTrm, subjectId, totalFocusCds, prvdId, request, Constants.Model.FileRequestStatus.CSV_FILE_FINISHED);

                // 上传Csv文件，更新数据库请求记录状态
                Map<String, Object> csvFtpPutResult = transferFileToFtpServer(Constants.Model.FileType.AUD_DETAIL, audTrm, subjectId, totalFocusCds, prvdId, csvFile, useChineseName);
                request.put("csvFileFtpTime", new Date());
                // updateFileRequestStatus(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.CSV_FTP_FINISHED);
                updateFileRequestStatusBySubjectIdNew(modelNotifyId, audTrm, subjectId, totalFocusCds, prvdId, request, Constants.Model.FileRequestStatus.CSV_FTP_FINISHED);
//        	    csvFtpPutResult.put("loginAccount", configInfo.get("loginAccount"));
//        	    csvFtpPutResult.put("userName", configInfo.get("userName"));
                // 向文件生成结果表HPMGR.busi_report_file中插入新生成的文件信息
                if ("5".equals(subjectId) || "6".equals(subjectId) ||"4".equals(subjectId)||"1".equals(subjectId)||"7".equals(subjectId)||"12".equals(subjectId)||"13".equals(subjectId))
                    insertReportFile(modelNotifyId, audTrm, subjectId, focusCd, prvdId, Constants.Model.FileType.AUD_DETAIL, csvFtpPutResult, configInfo);
                else
                    insertReportFile(modelNotifyId, audTrm, subjectId, totalFocusCds, prvdId, Constants.Model.FileType.AUD_DETAIL, csvFtpPutResult, configInfo);

            }
            if (flag) {
                this.logger.debug("#### 此次job已经生成过审计报告文件：subjectId=" + subjectId + ",focusCd=" + focusCd + ",prvdId=" + prvdId + ",audTrm=" + audTrm + ",totalFocusCds=" + totalFocusCds);
            } else {
                // 生成省Doc文件，更新数据库请求记录状态
                docFile = genProvDocFile(audTrm, subjectId, totalFocusCds, prvdId, configInfo, request, useChineseName);
                if (docFile != null) {

                    // File docZipFile = FileUtil.zipOneFile(docFile); //省公司doc.csv文件不应该打包上传，直接传文件即可，用户直接下载文件，直接看 modified by GuoXY 20161121
                    request.put("docFileGenTime", new Date());

                    updateFileRequestStatusBySubjectIdNew(modelNotifyId, audTrm, subjectId, totalFocusCds, prvdId, request, Constants.Model.FileRequestStatus.DOC_FILE_FINISHED);

                    // 上传Doc.zip，更新数据库请求记录状态
                    Map<String, Object> docFtpPutResult = null;
//                    if("7".equals(subjectId)){
//                        docFtpPutResult = transferFileToFtpServer(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, "7000", prvdId, docFile, useChineseName);
//                    }else{
                        docFtpPutResult = transferFileToFtpServer(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, totalFocusCds, prvdId, docFile, useChineseName);
//                    }


                    request.put("docFileFtpTime", new Date());
                    // updateFileRequestStatus(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.DOC_FTP_FINISHED);
                    updateFileRequestStatusBySubjectIdNew(modelNotifyId, audTrm, subjectId, totalFocusCds, prvdId, request, Constants.Model.FileRequestStatus.DOC_FTP_FINISHED);
                    // 向文件生成结果表HPMGR.busi_report_file中插入新生成的文件信息
//                    if (totalFocusCds != null && (totalFocusCds.substring(0, 4).equals("7001") || totalFocusCds.substring(0, 4).equals("7003"))) {
//                        insertReportFile(modelNotifyId, audTrm, subjectId, "7000", prvdId, Constants.Model.FileType.AUD_REPORT, docFtpPutResult, configInfo);
//                    } else {
                        insertReportFile(modelNotifyId, audTrm, subjectId, totalFocusCds, prvdId, Constants.Model.FileType.AUD_REPORT, docFtpPutResult, configInfo);
//                    }

                }
//		if(docFile==null&&"6".equals(subjectId)){
//		    updateFileRequestStatusBySubjectId(modelNotifyId, audTrm, subjectId, totalFocusCds, prvdId, request, Constants.Model.FileRequestStatus.DOC_FTP_FINISHED);
//		}

                if ("5".equals(subjectId)) {
                    excelFile = genProvExcelFile(audTrm, prvdId, subjectId);
                    if (excelFile != null) {
                        Map<String, Object> excelFtpPutResult = transferFileToFtpServer(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, totalFocusCds, prvdId, excelFile, useChineseName);
                        insertReportFile(modelNotifyId, audTrm, subjectId, totalFocusCds, prvdId, Constants.Model.FileType.AUD_REPORT, excelFtpPutResult, configInfo);
                    }

                }
            }
            if (flag && csvFile == null) {
                this.logger.debug("#### 此次请求已经生成审计报告文件，不用生成明细清单：subjectId=" + subjectId + ",focusCd=" + focusCd + ",prvdId=" + prvdId + ",audTrm=" + audTrm + ",totalFocusCds=" + totalFocusCds);
                return;
            }
            // 第六步：更新数据库请求记录状态
            // updateFileRequestStatus(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.File_FINISHED);
            updateFileRequestStatusBySubjectIdNew(modelNotifyId, audTrm, subjectId, totalFocusCds, prvdId, request, Constants.Model.FileRequestStatus.File_FINISHED);

        } catch (Exception e) {
            throw new RuntimeException("生成文件异常", e);
        } finally {
            String isDelLocalFile = StringUtils.trimToEmpty(propertyUtil.getPropValue("isDelLocalFile"));
            if ("true".equalsIgnoreCase(isDelLocalFile)) {
                FileUtil.removeFile(docFile);
                FileUtil.removeFile(csvFile);
            }
        }
    }

    /**
     * <pre>
     * Desc 生成省相关的doc文件（目前项目使用继承方式覆盖本类）
     * 		 这是一个利用"数据库notify表请求记录的字段中配置processor处理类 和 反射机制"实现"生成省相关的doc文件"的方法；
     * 		 如果，看不懂，可以让新定义的processor类继承本类后，覆盖次方法，在通过xml文件配置到job中效果相同；
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @param configInfo
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     */
    public File genProvDocFile(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, Map<String, Object> request, Boolean useChineseName) {

        String processor = (String) configInfo.get("processor");
        if (StringUtils.isNotBlank(processor)) {
            File exportFile = genDocFileByProcessor(audTrm, subjectId, focusCd, prvdId, configInfo, useChineseName);
            return exportFile;
        }
        return null;
    }

    public File genProvExcelFile(String audTrm, int prvdId, String subjectId) {
        File file = null;
        if ("5".equals(subjectId)) {
            try {
                List<Map<String, Object>> resultList = ygycService.getYGYCJFZSdata(audTrm, Integer.toString(prvdId), "5");
                if (resultList.size() > 0) {
                    file = sjbgQdFileGenProcessor.generateFile(Integer.toString(prvdId), audTrm);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * <pre>
     * Desc   使用特殊处理器生成word文件
     * @paramprocessor
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     */
    @SuppressWarnings("rawtypes")
    private File genDocFileByProcessor(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, Boolean useChineseName) {

        String processor = (String) configInfo.get("processor");
        AbstractFileProcessor fileProcessor = null;
        try {
            Constructor ct = Class.forName(processor).getConstructor();
            fileProcessor = (AbstractFileProcessor) ct.newInstance();
            fileProcessor.setAudTrm(audTrm);
            fileProcessor.setSubjectId(subjectId);
            fileProcessor.setFocusCd(focusCd);
            fileProcessor.setPrvdId(prvdId);
            fileProcessor.setConfigInfo(configInfo);

            fileProcessor.setTmpFileName(this.buildFileName(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, focusCd, prvdId, useChineseName));
            fileProcessor.setTmpFilePath(this.buildFtpFilePath(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, focusCd, prvdId));

        } catch (Throwable e) {
            logger.error("#### error when make instance of file processor.processor=" + processor + " errorMsg=" + ExceptionTool.getExceptionDescription(e));
        }
        return fileProcessor == null ? null : fileProcessor.execute();
    }

    /**
     * <pre>
     * Desc  根据请求id更新生成文件请求的状态为
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @author GuoXY
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateFileRequestStatus(int modelNotifyId, String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, int status) {
        configInfo.put("id", modelNotifyId);
        configInfo.put("status", status);
        concernFileGenService.updateFileGenReqStatusAndTimeById(configInfo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateFileRequestStatusBySubjectId(int modelNotifyId, String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, int status) {
        configInfo.put("audTrm", audTrm);
        configInfo.put("subjectId", subjectId);
        configInfo.put("prvdId", prvdId);
        configInfo.put("status", status);
        concernFileGenService.updateFileGenReqStatusAndTimeBySubject(configInfo);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void updateFileRequestStatusBySubjectIdNew(int modelNotifyId, String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, int status) {
        configInfo.put("audTrm", audTrm);
        configInfo.put("subjectId", subjectId);
        configInfo.put("prvdId", prvdId);
        configInfo.put("status", status);
        concernFileGenService.updateFileGenReqStatusAndTimeBySubjectNew(configInfo);
    }

    /**
     * <pre>
     * Desc  生成全国的清单文件zip,doc汇总报告，ftp file，update status，delete file
     * @author GuoXY
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     */
    public void genAllFile(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> request, int modelNotifyId, Map<String, Object> configInfo, Boolean useChineseName) {

        //if("7".equals(subjectId))focusCd="7000";
        File docFile = null, excelFile = null;
        try {
            this.logger.debug("#### 生成全国文件：audTrm=" + audTrm + ",focusCd=" + focusCd);
            String localFilePath = getLocalFilePath();
            if (!"8".equals(subjectId)) {
                // 第一步：生成"全公司"审计报告doc，更新数据库请求记录状态
                docFile = genAllDocFile(audTrm, subjectId, focusCd, Constants.ChinaCode, configInfo, request, useChineseName);
                if ("7".equals(subjectId) && docFile == null) {
                } else {
                    if (docFile == null) {
                        this.logger.debug("#### 没有生成有效地doc文件：audTrm=" + audTrm + ",focusCd=" + focusCd);
                        return;
                    }
                    request.put("docFileGenTime", new Date());
                    if ("5".equals(subjectId)) {
                        excelFile = genProvExcelFile(audTrm, prvdId, subjectId);
                    }
                    // modify by pxl 改为按专题更新notify,不再按id更新
                    // updateFileRequestStatus(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.DOC_FILE_FINISHED);
                    updateFileRequestStatusBySubjectIdNew(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.DOC_FILE_FINISHED);
                    // 第二步：从数据库中查询已生成的32份省级审计报告文件名，进行打包
                    List<String> fileDocList = null;
//                 if (focusCd != null && (focusCd.substring(0, 4).equals("7001") || focusCd.substring(0, 4).equals("7003"))) {
//                        fileDocList = concernFileGenService.selectAuditResultFileNew(audTrm, subjectId, "7000", Constants.Model.FileType.AUD_REPORT);
//                    } else {
                        fileDocList = concernFileGenService.selectAuditResultFileNew(audTrm, subjectId, focusCd, Constants.Model.FileType.AUD_REPORT);
//                    }
                    fileDocList.add(docFile.getName());// 增加全公司Doc文件
                    if (excelFile != null) {
                        fileDocList.add(excelFile.getName());// 增加全公司审计报告匹配清单
                    }
                    //TODO
                    //如果当前专题确认是“虚假宽带”
                    String docZipFileName ="";
                    //修改信息安全审计报告zip压缩包月份为审计月加1
                    if("13".equals(subjectId)||"12".equals(subjectId)){
                    	String audTrm1=DateUtilsForCurrProject.getAfterAnyMonth(audTrm,1);
                    	docZipFileName = buildFileName(Constants.Model.FileType.AUD_REPORT, audTrm1, subjectId, focusCd, prvdId, useChineseName);
                    }else{
                    	docZipFileName = buildFileName(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, focusCd, prvdId, useChineseName);
                    }
                    docZipFileName = docZipFileName.replaceAll("doc", "zip");
                    FileUtil.zipFile(localFilePath, localFilePath, docZipFileName, fileDocList);
                    File docZipFile = new File(localFilePath + File.separator + docZipFileName);

                    // 第四步：上传32省的Doc.zip，更新notify请求记录状态为4
                    Map<String, Object> docFtpPutResult = null;
//                    if("7".equals(subjectId)){
//                        docFtpPutResult = transferFileToFtpServer(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, "7000", Constants.ChinaCode, docZipFile, useChineseName);
//                    }else{
                        docFtpPutResult = transferFileToFtpServer(Constants.Model.FileType.AUD_REPORT, audTrm, subjectId, focusCd, Constants.ChinaCode, docZipFile, useChineseName);
//                    }

                    docFtpPutResult.put("createTime", request.get("docFileGenTime"));
                    request.put("docFileFtpTime", new Date());
//			 		    docFtpPutResult.put("num", request.get("num"));
//			 		   docFtpPutResult.put("loginAccount", configInfo.get("loginAccount"));
//			 		   docFtpPutResult.put("userName", configInfo.get("userName"));
                    // updateFileRequestStatus(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.DOC_FTP_FINISHED);
                    updateFileRequestStatusBySubjectIdNew(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.DOC_FTP_FINISHED);

                    // 第六步：删除HPMGR.busi_report_file表中原文件，插入新生成的doc文件记录；
//                    if (focusCd != null && (focusCd.substring(0, 4).equals("7001") || focusCd.substring(0, 4).equals("7003"))) {
//                        insertReportFile(modelNotifyId, audTrm, subjectId, "7000", prvdId, Constants.Model.FileType.AUD_REPORT, docFtpPutResult, configInfo);
//                    } else {
                        insertReportFile(modelNotifyId, audTrm, subjectId, focusCd, prvdId, Constants.Model.FileType.AUD_REPORT, docFtpPutResult, configInfo);
//                    }
                }
            }

            // 第三步：从数据库中查询已生成的31各省级审计明细文件名，进行打包
            List<String> fileCsvList = concernFileGenService.selectAuditResultFileNew(audTrm, subjectId, focusCd, Constants.Model.FileType.AUD_DETAIL);
            //TODO 变更审计月份
            String csvZipFileName = "";
    		if("13".equals(subjectId)||"12".equals(subjectId)){
    			String audTrm1=DateUtilsForCurrProject.getAfterAnyMonth(audTrm,1);
    			csvZipFileName = buildFileName(Constants.Model.FileType.AUD_DETAIL, audTrm1, subjectId, focusCd, prvdId, useChineseName);
    		}else{
    			
    			csvZipFileName = buildFileName(Constants.Model.FileType.AUD_DETAIL, audTrm, subjectId, focusCd, prvdId, useChineseName);
    		}
            csvZipFileName = csvZipFileName.replaceAll("csv", "zip");
            FileUtil.zipFile(localFilePath, localFilePath, csvZipFileName, fileCsvList);
            File csvZipFile = new File(localFilePath + File.separator + csvZipFileName);

            // 第三步：上传31省的Csv.zip，更新notify请求记录状态为3
            Map<String, Object> csvFtpPutResult = transferFileToFtpServer(Constants.Model.FileType.AUD_DETAIL, audTrm, subjectId, focusCd, Constants.ChinaCode, csvZipFile, useChineseName);
            csvFtpPutResult.put("createTime", request.get("csvFileGenTime"));
            request.put("csvFileFtpTime", new Date());
            // updateFileRequestStatus(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.CSV_FTP_FINISHED);
            updateFileRequestStatusBySubjectIdNew(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.CSV_FTP_FINISHED);
            csvFtpPutResult.put("loginAccount", configInfo.get("loginAccount"));
            csvFtpPutResult.put("userName", configInfo.get("userName"));
            csvFtpPutResult.put("num", request.get("num"));
            // 第五步：删除HPMGR.busi_report_file表中原文件，插入新生成的csv文件记录；
            insertReportFile(modelNotifyId, audTrm, subjectId, focusCd, prvdId, Constants.Model.FileType.AUD_DETAIL, csvFtpPutResult, configInfo);
            // 第七步：更新notify请求记录状态为5
            // updateFileRequestStatus(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.File_FINISHED);
            updateFileRequestStatusBySubjectIdNew(modelNotifyId, audTrm, subjectId, focusCd, prvdId, request, Constants.Model.FileRequestStatus.File_FINISHED);
        } catch (Exception e) {
            logger.error("#### " + e.getMessage(), e);
            throw new RuntimeException("生成文件异常", e);

        } finally {
            String isDelLocalFile = StringUtils.trimToEmpty(propertyUtil.getPropValue("isDelLocalFile"));
            if ("true".equalsIgnoreCase(isDelLocalFile)) {
                FileUtil.removeFile(docFile);
            }
//            if ("7".equals(subjectId) && 10000 == prvdId) {
//                concernFileGenService.updateFileGenConcernNew(audTrm, focusCd, "7000");
//            }
        }
    }


    /**
     * <pre>
     * Desc  生成全国的审计报告doc
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @param configInfo
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     */
    @Transactional
    protected File genAllDocFile(String audTrm, String subjectId, String focusCd, int prvdId, Map<String, Object> configInfo, Map<String, Object> request, Boolean useChineseName) {
        return this.genProvDocFile(audTrm, subjectId, focusCd, prvdId, configInfo, request, useChineseName);
    }

    /**
     * <pre>
     * Desc  文件生成完毕插入到结果表
     * 		   删除 HPMGR.busi_report_file表中原文件信息，并插入新文件信息（ID与HPMGR.busi_model_notify的ID对应）
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param chinacode
     * @param modelNotifyId
     * @param ftpPutResult
     * @author GuoXY
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void insertReportFile(int modelNotifyId, String audTrm, String subjectId, String focusCd, int chinacode, String fileType, Map<String, Object> ftpPutResult, Map<String, Object> conf) {

        String filePath = (String) ftpPutResult.get("filePath");
        String fileName = (String) ftpPutResult.get("fileName");
        String downloadUrl = (String) ftpPutResult.get("downloadUrl");
        Date createTime = ftpPutResult.get("createTime") == null ? new Date() : (Date) ftpPutResult.get("createTime");
        concernFileGenService.insertReportFileNew(modelNotifyId, audTrm, subjectId, focusCd, chinacode, fileType, filePath, fileName, downloadUrl, createTime);
        String isauto = conf.get("isauto").toString();
        if (isauto.equals("false")) {
            String fft = null;
            String focusCd_ = focusCd;
            if (subjectId.equals("2"))
                focusCd_ = "2000";
            if (subjectId.equals("6") && chinacode == 10000)
                focusCd_ = "6000";
            if (fileType.equals("audReport")) {
                fft = "审计报告手动生成";
            }
            if (fileType.equals("audDetail")) {
                fft = "审计清单手动生成";
            }
            BgxzData bgxz = new BgxzData();
            bgxz.setAudTrm(audTrm);
            bgxz.setFilePath(downloadUrl);
            bgxz.setFileType(fileType);
            bgxz.setOperType(fft);
            bgxz.setCreateType("manual");
            bgxz.setSubjectId(subjectId);
            bgxz.setFocusCd(focusCd_);
            bgxz.setPrvdId(chinacode);
            bgxz.setLoginAccount(conf.get("loginAccount").toString());
            bgxz.setOperPerson(conf.get("userName").toString());
            bgxz.setCreateDatetime(new Date());
            bgxz.setFileName(fileName);
            bgxz.setDownCount(0);
            //判断是否有初始化数据不完整的记录
            int count = 0;
//            if (subjectId.equals("7"))
//                count = bgxzService.updateReportLog7(bgxz, "create");
//            else
                count = bgxzService.updateInitReportLog(bgxz, "create");
            if (count == 0) {
                bgxzService.addReportLog(bgxz, "create");
            }
        }

        if (isauto.equals("true")) {
            String fft = null;
            String focusCd_ = focusCd;
            if (subjectId.equals("2"))
                focusCd_ = "2000";
            if (subjectId.equals("6") && chinacode == 10000)
                focusCd_ = "6000";
            if (fileType.equals("audReport")) {
                fft = "审计报告自动生成";
            }
            if (fileType.equals("audDetail")) {
                fft = "审计清单自动生成";
            }
            BgxzData bgxz = new BgxzData();
            bgxz.setAudTrm(audTrm);
            bgxz.setFilePath(downloadUrl);
            bgxz.setFileType(fileType);
            bgxz.setOperType(fft);
            bgxz.setCreateType("auto");
            bgxz.setSubjectId(subjectId);
            bgxz.setFocusCd(focusCd_);
            bgxz.setPrvdId(chinacode);
            bgxz.setLoginAccount("system");
            bgxz.setOperPerson("system");
            bgxz.setCreateDatetime(new Date());
            bgxz.setFileName(fileName);
            bgxz.setDownCount(0);
            //判断是否有初始化数据不完整的记录
            int count = 0;
//            if (subjectId.equals("7"))
//                count = bgxzService.updateReportLog7(bgxz, "create");
//            else
            count = bgxzService.updateInitReportLog(bgxz, "create");
            if (count == 0) {
                bgxzService.addReportLog(bgxz, "create");
            }
        }


    }

    /**
     * <pre>
     * Desc  ftp文件到ftp服务器
     *       包括创建目录
     * @paramcsvFile
     * @paramdocFile
     * @author GuoXY
     * @throws Exception
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     */

    protected Map<String, Object> transferFileToFtpServer(String fileType, String audTrm, String subjectId, String focusCd, int prvdId, File upFile, Boolean useChineseName) throws Exception {

        logger.debug("#### 开始上传文件至FTP：" + fileType + "," + audTrm + "," + subjectId + "," + focusCd + "," + prvdId + ",");
        if (upFile == null) {
            this.logger.error("#### 文件为空，不需要ftp操作");
            return new HashMap<String, Object>();
        }

        Map<String, Object> resuluMap = new HashMap<String, Object>();
        String filePath = buildFtpFilePath(fileType, audTrm, subjectId, focusCd, prvdId);
        logger.debug("#### 构造文件上传路径为：" + filePath);

        String fileName = upFile.getName();// buildFileName(fileType, audTrm, subjectId, focusCd, prvdId, modelFinTime, useChineseName);
        String downloadUrl = buildDownloadUrl(audTrm, subjectId, focusCd, prvdId, filePath, fileName);
        resuluMap.put("filePath", filePath);
        resuluMap.put("fileName", fileName);
        resuluMap.put("downloadUrl", downloadUrl);

        String isTransferFile = propertyUtil.getPropValue("isTransferFile");
        if (!"true".equalsIgnoreCase(isTransferFile)) {
            this.logger.error("由于ftp server 传输配置开关没有打开，暂时文件不传输到ftp server。");
            return resuluMap;
        }
        logger.debug("#### 开始上传文件(进度:1/2):" + filePath + "," + downloadUrl);

        // 20161110 add try by GuoXY for 让文件上传不影响web服务word文件的生成
        try {
            uploadFile(upFile, filePath);
        } catch (Exception e) {
            logger.error("#### 文件上传FTP(  " + fileName + "  )异常。错误信息为：" + ExceptionTool.getExceptionDescription(e));
        }

        logger.debug("#### 完成上传文件(进度:2/2):" + filePath + "," + downloadUrl);
        return resuluMap;
    }

    public void uploadFile(File csvFile, String filePath) {
        FtpUtil ftpTool = null;
        try {
            ftpTool = initFtp();
            if (ftpTool == null) {
                return;
            }
            ftpTool.uploadFile(csvFile, filePath);
        } finally {
            if (ftpTool != null) {
                ftpTool.disConnect();
            }
        }
    }

    /**
     * <pre>
     * Desc  初始化ftp服务
     * @author GuoXY
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     */
    private FtpUtil initFtp() {

        String isTransferFile = propertyUtil.getPropValue("isTransferFile");
        if (!"true".equalsIgnoreCase(isTransferFile)) {
            return null;
        }
        FtpUtil ftpTool = new FtpUtil();
        String ftpServer = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpServer"));
        String ftpPort = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpPort"));
        String ftpUser = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpUser"));
        String ftpPass = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpPass"));
        ftpTool.initClient(ftpServer, ftpPort, ftpUser, ftpPass);
        return ftpTool;
    }

    /**
     * <pre>
     * Desc  从配置文件中获取本地文件存放目录
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     */
    protected String getLocalFilePath() {
        String tempDir = propertyUtil.getPropValue("tempDirV2");
        return tempDir;
    }

    /**
     * <pre>
     * Desc  /yyyymm/subjectId/focusCd/provId
     *       /yyyymm/subjectId/focusCd/10000
     *       /yyyymm/subjectId/focusCd/10100
     * @param fileType
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     */
    protected String buildFtpFilePath(String fileType, String audTrm, String subjectId, String focusCd, int prvdId) {
        String ftpPath = propertyUtil.getPropValue("ftpPathV2");
        String path = buildRelativePath(audTrm, subjectId, prvdId, focusCd);
        String finalPath = FileUtil.buildFullFilePath(ftpPath, path);
        logger.debug("#### ftp中文件存储路径为：" + finalPath);
        FileUtil.mkdirs(finalPath);

        return finalPath;
    }

    /**
     * <pre>
     * Desc 生成文件名
     *  中文名：上海_201605_渠道养卡审计清单.csv
     *  	    上海_201605_渠道养卡审计报告.doc
     *  非中文：subjectId_focusCd_YYYYMM_prvdId.csv
     * @param fileType
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @param useChineseName
     * @return
     * @author GuoXY
     * @throws ParseException 
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     */
    protected String buildFileName(String fileType, String audTrm, String subjectId, String focusCd, int prvdId, Boolean useChineseName) {

        StringBuilder path = new StringBuilder();
        String prvdName = prvdId + "";

        if (useChineseName) {
            // 生成中文名审计报告/csv
            prvdName = Constants.MAP_PROVD_NAME.get(prvdId);
            if (("4".equals(subjectId) ||"1".equals(subjectId) || "5".equals(subjectId) || "7".equals(subjectId) || "6".equals(subjectId) || "12".equals(subjectId)|| "13".equals(subjectId)) && Constants.Model.FileType.AUD_DETAIL.equalsIgnoreCase(fileType)) {
                String focusName = Constants.MAP_FOCUSCD_NAME.get(focusCd);
                path.append(prvdName).append("_").append(audTrm).append("_").append(focusName);
            } else {
                subjectId = Constants.MAP_SUBJECT_NAME.get(subjectId);
                path.append(prvdName).append("_").append(audTrm).append("_").append(subjectId);
            }

        } else {
            path.append(subjectId).append("_").append(focusCd).append("_").append(audTrm).append("_").append(prvdName);
        }

        if (Constants.Model.FileType.AUD_REPORT.equalsIgnoreCase(fileType)) {
            if (useChineseName) {
                path.append("审计报告.doc");
            } else {
                path.append(".doc");
            }
            return path.toString();
        }

        if (Constants.Model.FileType.AUD_DETAIL.equalsIgnoreCase(fileType)) {
            if (useChineseName) {
                path.append("审计清单.csv");
            } else {
                path.append(".csv");
            }
        }

        return path.toString();
    }

    /**
     * <pre>
     * Desc  构造http形式的下载地址
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @param filePath
     * @param fileName
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     */
    protected String buildDownloadUrl(String audTrm, String subjectId, String focusCd, int prvdId, String filePath, String fileName) {

        String ftpHttpUrlPrefix = propertyUtil.getPropValue("ftpHttpUrlPrefixV2");
        StringBuilder url = new StringBuilder(30);
        url.append(buildRelativePath(audTrm, subjectId, prvdId, focusCd)).append("/").append(fileName);
        return FileUtil.buildFullFilePath(ftpHttpUrlPrefix, url.toString());
    }

    /**
     * <pre>
     * Desc  构造相对路径 /yyyymm/subjectId/focusCd/provId
     * @param audTrm
     * @param subjectId
     * @param prvdId
     * @param focusCd
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date 20161019
     * </pre>
     */
    protected String buildRelativePath(String audTrm, String subjectId, int prvdId, String focusCd) {

        StringBuilder path = new StringBuilder();
        path.append(audTrm).append("/").append(subjectId).append("/").append(focusCd).append("/").append(prvdId);

        logger.error("#### buildRelativePath>>>" + path.toString());
        return path.toString();
    }

    public File zipAllCsvZip(String audTrm, String subjectId, String focusCd, Boolean useChineseName) throws IOException {
        String path = getLocalFilePath();
        logger.error("#### zipAllCsvZip path=:" + path);
        String localFileName = buildFileName(Constants.Model.FileType.AUD_DETAIL, audTrm, subjectId, focusCd, 10000, useChineseName);

        logger.error("#### zipAllCsvZip localFileName=:" + localFileName);
        String zipFile = "";
        if (useChineseName) {
            localFileName = localFileName.replace("全公司_", "").replace(".csv", "");
            // zipFile = path + "/全公司_" + localFileName + ".zip";
        } else {
            localFileName = localFileName.replace("_10000.csv", "");

            // zipFile = path + "/" + localFileName + "_10000.zip";
        }
        zipFile = path + "/ALL_" + audTrm + "_" + subjectId + "_" + focusCd + ".zip";
        logger.error("#### zip 31 prvd:zipFIle name=" + zipFile);

        File folder = new File(path);
        final String fnm = localFileName;

        logger.error("#### zip 31 prvd:common name=" + fnm);

        if (folder.isDirectory()) {
            File[] files = folder.listFiles(new FileFilter() {

                @Override
                public boolean accept(File f) {
                    if (f.isFile() && f.getName().indexOf(fnm) > 0 && (f.getName().toLowerCase().indexOf(".csv") > 0 || f.getName().toLowerCase().indexOf(".doc") > 0)) {
                        logger.error("#### zip 31 prvd:file=" + f.getName());
                        return true;
                    }
                    return false;
                }
            });

            @SuppressWarnings("unused")
			long l1 = new Date().getTime();

            logger.error("#### zip 31 prvd:file count=" + files.length);

            return FileUtil.zipFile(files, zipFile);
        }
        return null;

    }

}
