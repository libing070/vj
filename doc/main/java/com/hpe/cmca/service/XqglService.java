/**
 * com.hpe.cmca.service.XqglService.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.XqglMapper;
import com.hpe.cmca.pojo.XqglData;
import com.hpe.cmca.pojo.XqglListData;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <pre>
 * Desc：
 * @author hufei
 * @refactor hufei
 * @date 2018-4-16 下午4:23:21
 * @version 1.0
 *
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  2018-4-16 	   hufei 	         1. Created this class.
 * </pre>
 */
@Service("XqglService")
public class XqglService extends BaseObject {

    @Autowired
    protected MybatisDao mybatisDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    Map<String, String> reqIdMap = new HashMap<String, String>();

    private List<String> csvFileNames;
    protected String csvFileName;

    // 记录下载地址，用于打包
    protected ArrayList<String> strs = new ArrayList<String>();

    //设置导出文件样式
    private Map<String, CellStyle> mapStyle = new HashMap<String, CellStyle>();

    /**
     * <pre>
     * Desc 获取列表页
     * @param xqglListData
     * @return
     * @author hufei
     * 2018-4-17 下午3:11:13
     * </pre>
     */
    public List<XqglListData> getList(XqglListData xqglListData, String loginId, String depId, String type) {

        if ("xinjian".equals(type)) {
            xqglListData.setReqSrcPersonId(loginId);
        }
        if ("daiban".equals(type)) {
            if ("10009".equals(depId))
                xqglListData.setReqStatus("CLZ");
            else {
                xqglListData.setReqStatus("DSP");
                xqglListData.setReqApprovePersonId(loginId);
            }

        }
        if ("yiban".equals(type)) {
            if ("10009".equals(depId))
                xqglListData.setReqStatus("YWC");
            else {
                List<String> reqStatusList =new ArrayList<>();
                reqStatusList.add("CLZ");
                reqStatusList.add("YWC");
                xqglListData.setReqStatusList(reqStatusList);
                xqglListData.setReqApprovePersonId(loginId);
            }

        }


        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        if (xqglListData.getReqNm() != null && !xqglListData.getReqNm().trim().isEmpty()) {
            xqglListData.setReqNm("%" + xqglListData.getReqNm() + "%");
        }

        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        List<XqglListData> tpList = xqglMapper.getList(xqglListData);
        for (XqglListData xd : tpList) {
            try {
                xd.setReqTime(dd.format(dd.parse(xd.getReqTime())));
                if(xd.getReqExpectTime()==null||"".equals(xd.getReqExpectTime()))
                    continue;
                else
                    xd.setReqExpectTime(dd.format(dd.parse(xd.getReqExpectTime())));
            } catch (ParseException e) {
                logger.error("date parse error>>>" + e.getMessage(), e);
                e.printStackTrace();
            }
        }
        return tpList;
    }

    /**
     * <pre>
     * Desc  获取系统配置项
     * @param configSection
     * @return
     * @author hufei
     * 2018-4-17 下午4:10:59
     * </pre>
     */
    public List<Map<String, Object>> getSysConfig(String configSection) {
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        Map<String, Object> map = new HashMap<>();
        map.put("configSection", configSection);
        return xqglMapper.getSysConfig(map);
    }

    /**
     * <pre>
     * Desc  根据需求ID获取需求详情
     * @param reqId
     * @return
     * @author hufei
     * 2018-4-17 下午4:11:19
     * </pre>
     */
    public List<XqglData> getDetailById(String reqId) {
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        Map<String, Object> paramterMap = new HashMap<String, Object>();
        paramterMap.put("reqId", reqId);
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        List<XqglData> tpList = xqglMapper.getDetailById(paramterMap);
        for (XqglData xd : tpList) {
            try {
                if(xd.getReqTime()!=null&&(!"".equals(xd.getReqTime())))
                    xd.setReqTime(dd.format(dd.parse(xd.getReqTime())));

                if(xd.getReqApproveTime()!=null&&(!"".equals(xd.getReqApproveTime())))
                    xd.setReqApproveTime(dd.format(dd.parse(xd.getReqApproveTime())));

                if(xd.getReqFinishTime()!=null&&(!"".equals(xd.getReqFinishTime())))
                    xd.setReqFinishTime(dd.format(dd.parse(xd.getReqFinishTime())));

                if(xd.getReqExpectTime()!=null&&(!"".equals(xd.getReqExpectTime())))
                    xd.setReqExpectTime(dd.format(dd.parse(xd.getReqExpectTime())));
            } catch (ParseException e) {
                logger.error("date parse error>>>" + e.getMessage(), e);
                e.printStackTrace();
            }
        }
        return tpList;
    }

    /**
     * <pre>
     * Desc 需求管理-突发性数据统计-新增需求
     * @param xqglData
     * @return
     * @author hufei
     * 2018-4-19 上午10:15:15
     * </pre>
     */
    public Map<String, Object> addRequirement(XqglData xqglData, String userId, String userName, String depName) {
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if("".equals(xqglData.getReqExpectTime()))
            xqglData.setReqExpectTime(null);
        else {
            try {
                xqglData.setReqExpectTime(sf.format(sf.parse(xqglData.getReqExpectTime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        xqglData.setReqSrcPerson(userName);
        xqglData.setReqSrcPersonId(userId);
        xqglData.setReqSrcDep(depName);
        xqglData.setReqId(getReqId());
        xqglData.setReqStatus("DSP");
        Map<String, Object> mapTp = new HashMap<String, Object>();
        mapTp.put("addStatus", xqglMapper.addRequirement(xqglData));
        mapTp.put("xqglData", xqglData);
        return mapTp;

    }

    /**
     * <pre>
     * Desc  需求管理-突发性数据统计-编辑页面-保存
     * @param xqglData
     * @return
     * @author hufei
     * 2018-4-19 下午3:56:22
     * </pre>
     */
    public int editRequirement(XqglData xqglData, String type) {
        int result=0;
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        if ("new".equals(type)) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if("".equals(xqglData.getReqExpectTime()))
                xqglData.setReqExpectTime(null);
            else {
                try {
                    xqglData.setReqExpectTime(sf.format(sf.parse(xqglData.getReqExpectTime())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            result= xqglMapper.editRequirementByNew(xqglData);
        }
        if ("approve".equals(type)) {
            xqglData.setReqStatus("CLZ");
            result= xqglMapper.editRequirementByApprove(xqglData);
        }
        if ("handle".equals(type)) {
            xqglData.setReqStatus("YWC");
            result= xqglMapper.editRequirementByHandle(xqglData);
        }
        if ("handleTmp".equals(type)) {
            //xqglData.setReqStatus("YWC");
            result= xqglMapper.editRequirementByHandle(xqglData);
        }
//        if (xqglData.getReqTbNm() == null || "".equals(xqglData.getReqTbNm())) {
//            xqglData.setReqStatus("DSP");
//        } else {
//            xqglData.setReqStatus("YWC");
//        }
//        return xqglMapper.editRequirement(xqglData);
        return result;
    }

    /**
     * <pre>
     * Desc 需求管理-突发性数据统计-删除
     * @param reqId
     * @return
     * @author hufei
     * 2018-4-19 下午3:57:03
     * </pre>
     */
    public int deleteRequirement(HttpServletRequest request, String reqId) {
        if (getAuthorityAttrByDepId(request) == 0)
            return 0;
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        Map<String, Object> paramterMap = new HashMap<String, Object>(2);
        paramterMap.put("reqId", reqId);
        paramterMap.put("reqEffective", "N");
        if (getAuthorityAttrByDepId(request) == 1) {
            paramterMap.put("reqSrcPerson", request.getSession().getAttribute("userId"));
        }
        return xqglMapper.updateRequirement(paramterMap);


    }

    /**
     * <pre>
     * Desc 获取需求编号
     * @return
     * @author hufei
     * 2018-4-18 下午6:04:54
     * </pre>
     */
    private String getReqId() {
        String dayString = new SimpleDateFormat("yyyyMMdd").format(new Date());
//	int reqId;
//	if(reqIdMap.isEmpty()){
//	    reqId=getReqIdByDataBase();
//	}else if(reqIdMap.containsKey(dayString)){
//	    reqId=Integer.parseInt(String.valueOf(reqIdMap.get(dayString))) + 1;
//	}else{
//	    reqId=1;
//	    reqIdMap.clear();
//	}
//	reqIdMap.put(dayString,String.format("%03d", reqId));
        return dayString + String.format("%03d", getReqIdByDataBase());
    }

    /**
     * <pre>
     * Desc 从数据库中获取当天最大数据库编号ID
     * @return
     * @author hufei
     * 2018-4-18 下午6:05:25
     * </pre>
     */
    private int getReqIdByDataBase() {
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        Map<String, Object> param = new HashMap<String, Object>();
        String dayString = new SimpleDateFormat("yyyyMMdd").format(new Date());
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, 1);
        param.put("currentDay", dayString);
        param.put("afterDay", new SimpleDateFormat("yyyyMMdd").format(c.getTime()));
        Map<String, Object> result = xqglMapper.getCurrentMaxId(param);

        if (result != null && result.size() != 0) {
            return Integer.parseInt(String.valueOf(result.get("reqId").toString().substring(8))) + 1;
        }
        return 1;
    }

    public XqglListData genReqExcel(XqglListData xqglListData,String loginId, String depId, HttpServletRequest request, HttpServletResponse response) {
        if ("10009".equals(depId))
            xqglListData.setReqStatus("YWC");
        else {
            xqglListData.setReqApprovePersonId(loginId);
        }

        strs.clear();
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        xqglListData.setReqNm("%" + xqglListData.getReqNm() + "%");
        List<XqglData> tpList = xqglMapper.getOutPutList(xqglListData);

        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (XqglData xd : tpList) {
            try {
                xd.setReqTime(dd.format(dd.parse(xd.getReqTime())));
            } catch (ParseException e) {
                logger.error("date parse error>>>" + e.getMessage(), e);
                e.printStackTrace();
            }
        }

        SXSSFWorkbook wb1 = new SXSSFWorkbook(500);
        mapStyle.clear();
        mapStyle.put("style1", getStyle(wb1, 1));
        mapStyle.put("style2", getStyle(wb1, 2));
        mapStyle.put("style3", getStyle(wb1, 3));


        Sheet sh1 = wb1.createSheet("突发性数据统计");
        List<String> titleList1 = Arrays.asList("需求名称", "需求描述", "序号", "工作分类", "需求提出人", "需求提出日期", "希望完成日期", "结果反馈渠道", "提交人", "提交时间", "实际工作量", "任务完成说明", "是否有需求来源文件", "是否有口径说明文件", "是否有原始脚本代码", "是否录入系统", "是否录入工作量");
        int titleCol1 = 0;
        Row r1 = genRow(sh1, 0);
        for (String s : titleList1) {
            r1.createCell(titleCol1).setCellValue(s);
            r1.getCell(titleCol1++).setCellStyle(mapStyle.get("style2"));
        }
        int rowIndex1 = 1;
        for (XqglData xd : tpList) {
            r1 = genRow(sh1, rowIndex1);
            r1.createCell(0).setCellValue(xd.getReqNm());
            r1.getCell(0).setCellStyle(mapStyle.get("style1"));
            r1.createCell(1).setCellValue(xd.getReqDescription());
            r1.getCell(1).setCellStyle(mapStyle.get("style1"));
            r1.createCell(2).setCellValue(xd.getReqId());
            r1.getCell(2).setCellStyle(mapStyle.get("style1"));
            r1.createCell(3).setCellValue(xd.getReqType());
            r1.getCell(3).setCellStyle(mapStyle.get("style1"));
            r1.createCell(4).setCellValue(xd.getReqSrcPerson());
            r1.getCell(4).setCellStyle(mapStyle.get("style1"));
            r1.createCell(5).setCellValue(xd.getReqTime());
            r1.getCell(5).setCellStyle(mapStyle.get("style1"));
            r1.createCell(6).setCellValue(xd.getReqExpectTime());
            r1.getCell(6).setCellStyle(mapStyle.get("style1"));
            r1.createCell(7).setCellValue("");
            r1.getCell(7).setCellStyle(mapStyle.get("style1"));
            r1.createCell(8).setCellValue(xd.getReqSubmitPerson());
            r1.getCell(8).setCellStyle(mapStyle.get("style1"));
            r1.createCell(9).setCellValue(xd.getReqFinishTime());
            r1.getCell(9).setCellStyle(mapStyle.get("style1"));
            r1.createCell(10).setCellValue(xd.getReqWorkload());
            r1.getCell(10).setCellStyle(mapStyle.get("style1"));
            r1.createCell(11).setCellValue(xd.getReqFinishComments());
            r1.getCell(11).setCellStyle(mapStyle.get("style1"));
            r1.createCell(12).setCellValue("");
            r1.getCell(12).setCellStyle(mapStyle.get("style1"));
            r1.createCell(13).setCellValue("");
            r1.getCell(13).setCellStyle(mapStyle.get("style1"));
            r1.createCell(14).setCellValue("");
            r1.getCell(14).setCellStyle(mapStyle.get("style1"));
            r1.createCell(15).setCellValue("");
            r1.getCell(15).setCellStyle(mapStyle.get("style1"));
            r1.createCell(16).setCellValue("");
            r1.getCell(16).setCellStyle(mapStyle.get("style1"));
            rowIndex1++;
        }
        try {
            generateSingle(wb1, "突发性数据统计_" + xqglListData.getStartReqTime() + "-" + xqglListData.getEndReqTime());
        } catch (Exception e) {
            logger.error("tufaxingshujutongjiwenjianbaocuo", e);
            e.printStackTrace();
        }

        SXSSFWorkbook wb2 = new SXSSFWorkbook(500);
        mapStyle.clear();
        mapStyle.put("style1", getStyle(wb2, 1));
        mapStyle.put("style2", getStyle(wb2, 2));
        mapStyle.put("style3", getStyle(wb2, 3));

        Sheet sh2 = wb2.createSheet("临时统计要求填报信息");
        List<String> titleList2 = Arrays.asList("项目组", "统计分析任务", "涉及的表（库名.表名）", "数据周期", "是否金库审批", "是否批量导出", "及时性要求");
        int titleCol2 = 0;
        Row r2 = genRow(sh2, 0);
        for (String s : titleList2) {
            r2.createCell(titleCol2).setCellValue(s);
            r2.getCell(titleCol2++).setCellStyle(mapStyle.get("style2"));
        }
        int rowIndex2 = 1;
        for (XqglData xd : tpList) {
            for (int i = 1; i <= 5; i++) {
                r2 = genRow(sh2, rowIndex2);
                r2.createCell(0).setCellValue("集中化持续审计项目组");
                r2.getCell(0).setCellStyle(mapStyle.get("style1"));
                r2.createCell(1).setCellValue(xd.getReqNm());
                r2.getCell(1).setCellStyle(mapStyle.get("style1"));
                r2.createCell(2).setCellValue(getValofXd(xd, i, "Nm"));
                r2.getCell(2).setCellStyle(mapStyle.get("style1"));
                r2.createCell(3).setCellValue(getValofXd(xd, i, "Audtrm"));
                r2.getCell(3).setCellStyle(mapStyle.get("style1"));
                r2.createCell(4).setCellValue(getValofXd(xd, i, "Sensitive"));
                r2.getCell(4).setCellStyle(mapStyle.get("style1"));
                r2.createCell(5).setCellValue(getValofXd(xd, i, "Output"));
                r2.getCell(5).setCellStyle(mapStyle.get("style1"));
                r2.createCell(6).setCellValue("");
                r2.getCell(6).setCellStyle(mapStyle.get("style1"));
                rowIndex2++;
            }
        }

        try {
            generateSingle(wb2, "临时统计要求填报信息_" + xqglListData.getStartReqTime() + "-" + xqglListData.getEndReqTime());
        } catch (Exception e) {
            logger.error("linshitongjiyaoqiutianbaoxinxiwenjianbaocuo", e);
            e.printStackTrace();
        }

        downAttachment(tpList);

        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String outPutZipName = "tf_requirements_" + currentDate + "_" + tpList.size() + ".zip";
        FileUtil.zipFile(getReqLocalDir(), getReqLocalDir(), outPutZipName, strs);
        uploadFile(getReqLocalDir() + "/" + outPutZipName, getAttachFtpDir());//上传到FTP服务器

        try {
            downFile(outPutZipName, getAttachFtpDir(), response,request);
        } catch (ParseException e) {
            logger.error("xiazaiwenjiandaokehuduanchucuo", e);
            e.printStackTrace();
        }
        // 2018-8-21 16:52:59 zhangqiang add
        xqglListData.setReqResultAddr(outPutZipName);
        xqglListData.setReqId(null);
        xqglListData.setReqNm(null);

        return xqglListData;
    }

    public void downAttachment(List<XqglData> tpList) {
        for (XqglData xd : tpList) {
            String attachAddr = xd.getReqAttachAddr();
            //String fileName = attachAddr.substring(attachAddr.lastIndexOf("/")+1,attachAddr.length());
            String fileName = attachAddr;
            String localFilePath = getReqLocalDir() + "/" + fileName;
            String remotePath = getAttachFtpDir();
            if (fileName != null && !"".equals(fileName)) {
                downloadFile(localFilePath, remotePath, fileName);
                strs.add(fileName);
            }

        }
        for (XqglData xd : tpList) {
            String attachAddr = xd.getReqHandleAttachAddr();
            //String fileName = attachAddr.substring(attachAddr.lastIndexOf("/")+1,attachAddr.length());
            String fileName = attachAddr;
            String localFilePath = getReqLocalDir() + "/" + fileName;
            String remotePath = getAttachFtpDir();
            if (fileName != null && !"".equals(fileName)) {
                downloadFile(localFilePath, remotePath, fileName);
                strs.add(fileName);
            }

        }
    }

    public String getValofXd(XqglData xd, int i, String nm) {

        if (i == 1) {
            if (nm.equals("Nm")) return xd.getSrcTb1Nm();
            if (nm.equals("Audtrm")) return xd.getSrcTb1Audtrm();
            if (nm.equals("Sensitive")) return xd.getSrcTb1Sensitive();
            if (nm.equals("Output")) return xd.getSrcTb1Output();
        }
        if (i == 2) {
            if (nm.equals("Nm")) return xd.getSrcTb2Nm();
            if (nm.equals("Audtrm")) return xd.getSrcTb2Audtrm();
            if (nm.equals("Sensitive")) return xd.getSrcTb2Sensitive();
            if (nm.equals("Output")) return xd.getSrcTb2Output();
        }
        if (i == 3) {
            if (nm.equals("Nm")) return xd.getSrcTb3Nm();
            if (nm.equals("Audtrm")) return xd.getSrcTb3Audtrm();
            if (nm.equals("Sensitive")) return xd.getSrcTb3Sensitive();
            if (nm.equals("Output")) return xd.getSrcTb3Output();
        }
        if (i == 4) {
            if (nm.equals("Nm")) return xd.getSrcTb4Nm();
            if (nm.equals("Audtrm")) return xd.getSrcTb4Audtrm();
            if (nm.equals("Sensitive")) return xd.getSrcTb4Sensitive();
            if (nm.equals("Output")) return xd.getSrcTb4Output();
        }
        if (i == 5) {
            if (nm.equals("Nm")) return xd.getSrcTb5Nm();
            if (nm.equals("Audtrm")) return xd.getSrcTb5Audtrm();
            if (nm.equals("Sensitive")) return xd.getSrcTb5Sensitive();
            if (nm.equals("Output")) return xd.getSrcTb5Output();
        }
        return "";
    }

    public Row genRow(Sheet sh, int arg0) {
        Row r = null;
        r = sh.getRow(arg0);
        if (r == null)
            r = sh.createRow(arg0);
        return r;
    }


    public void generateSingle(SXSSFWorkbook wb, String name) throws Exception {

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(getReqLocalDir() + "/" + name + ".xlsx");
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
        strs.add(name + ".xlsx");
        uploadFile(getReqLocalDir() + "/" + name + ".xlsx", getAttachFtpDir());

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

    public void uploadFile(String filePath, String ftpPath) {
        FtpUtil ftpTool = null;
        try {
            ftpTool = initFtp();
            if (ftpTool == null) {
                return;
            }
            ftpTool.uploadFile(new File(filePath), ftpPath);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("NotiFile uploadFile>>>" + e.getMessage(), e);
        } finally {
            if (ftpTool != null) {
                ftpTool.disConnect();
            }
        }
    }

    public void downloadFile(String localFilePath, String remotePath, String fileName) {

        FtpUtil ftpTool = null;
        try {
            ftpTool = initFtp();
            if (ftpTool == null) {
                return;
            }
            ftpTool.downloadFile(localFilePath, remotePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("NotiFile downloadFile>>>" + e.getMessage(), e);
        } finally {
            if (ftpTool != null) {
                ftpTool.disConnect();
            }
        }
    }

    public String getReqLocalDir() {
        String tempDir = propertyUtil.getPropValue("reqDir");
        FileUtil.mkdirs(tempDir);
        return propertyUtil.getPropValue("reqDir");
    }

    protected String getAttachFtpDir() {
        String attachFtpDir = propertyUtil.getPropValue("attachFtpDir");
        return attachFtpDir;
    }

    /**
     * <pre>
     * Desc检查是否有生成权限
     * @param request
     * @param reqId
     * @param reqTbNm
     * @return
     * @author hufei
     * 2018-4-24 下午5:00:25
     * </pre>
     */
    public String checkGenerateAttachment(HttpServletRequest request, String reqId, String reqTbNm) {
        // 1.判断是否有请求正在生成
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, -15);
        Map<String, Object> paramterMap = new HashMap<String, Object>(2);
        paramterMap.put("reqResultState", "1");
        paramterMap.put("lastDay", new SimpleDateFormat("yyyyMMdd").format(c.getTime()));
        int runSize = xqglMapper.getRunList(paramterMap);
        if (runSize > 0) {
            return "havaRun";
        }
        Map<String, Object> tableNameColumTitle = tableIsExist(reqTbNm);
        if (tableNameColumTitle == null)
            return "errorTable";
        else
            return "success";
    }

    /**
     * <pre>
     * Desc 文件生成
     * 1.判断是否有正在生成的请求；
     * 2.判断表是否存在，如果不存在则返回为表不存在
     * 3.按照表生成文件
     * 4.压缩文件夹
     * 5.上传到FTP
     * 6.更新下载路径到数据库中
     * @param reqId
     * @param reqTbNm
     * @return
     * 1 代表有请求正在生成
     * 2 代表表名不正确
     * @author hufei
     * 2018-4-23 上午11:02:21
     * </pre>
     */
    public int generateFile(HttpServletRequest request, String reqId, String reqTbNm) {
        Map<String, Object> tableNameColumTitle = tableIsExist(reqTbNm);
        if (tableNameColumTitle == null)
            return 2;

        generateAll(request, reqId, reqTbNm, tableNameColumTitle);
        return 3;
    }


    /**
     * <pre>
     * Desc  // 2.判断表名是否正确
     * @param reqTbNm
     * @return 表头
     * @author hufei
     * 2018-4-23 下午7:04:28
     * </pre>
     */
    public Map<String, Object> tableIsExist(String reqTbNm) {
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        String[] reqTableNames = reqTbNm.split("\\|");
        Map<String, Object> tableNameColumTitle = new HashMap<String, Object>(reqTableNames.length);
        csvFileNames = new ArrayList<String>();
        for (int i = 0; i < reqTableNames.length; i++) {
            Map<String, Object> parameterMap = new HashMap<String, Object>(2);
            if (!reqTableNames[i].contains(".")) {
                return null;
            }
            parameterMap.put("dataBaseName", reqTableNames[i].substring(0, reqTableNames[i].indexOf(".")));
            parameterMap.put("tableName", reqTableNames[i].substring(reqTableNames[i].indexOf(".") + 1));
            List<Map<String, Object>> list = xqglMapper.getTable(parameterMap);
            // 表不存在
            if (list == null || list.isEmpty()) {
                return null;
            } else {
                StringBuffer csvHearder = new StringBuffer();
                for (Map<String, Object> map : list) {
                    if (map.get("columnTitle") == null) {
                        csvHearder.append(map.get("columnName"));
                    } else {
                        csvHearder.append(map.get("columnTitle"));
                    }
                    csvHearder.append(",");
                }
                tableNameColumTitle.put(reqTableNames[i], csvHearder.substring(0, csvHearder.length() - 1));
            }
        }
        return tableNameColumTitle;
    }

    /*
     * 3.按照表生成文件 3.1生成前更新数据表中最新开始生成时间 3.2生成文件
     */
    public void generateAll(HttpServletRequest request, String reqId, String reqTbNm, Map<String, Object> tableNameColumTitle) {
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        Map<String, Object> paramterMap = new HashMap<String, Object>(2);
        String csvSQL, csvHearder;
        paramterMap.put("reqResultStartTime", "start");
        paramterMap.put("reqResultGenerter", getUserName(request));
        paramterMap.put("reqResultState", "1");
        paramterMap.put("reqId", reqId);
        xqglMapper.updateRequirement(paramterMap);

        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String[] reqTableNames = reqTbNm.split("\\|");
        for (int i = 0; i < reqTableNames.length; i++) {
            csvSQL = "select * from " + reqTableNames[i];
            csvFileName = reqTableNames[i] + "_" + currentDate + ".csv";
            csvHearder = tableNameColumTitle.get(reqTableNames[i]).toString();
            generate(reqId, csvSQL, csvHearder);
        }
        /**
         * 4.生成压缩包,并上传到FTP
         */
        String localFilePath = getReqLocalDir();
        String csvZipFileName = reqId + ".zip";
        FileUtil.zipFile(localFilePath, localFilePath, csvZipFileName, csvFileNames);
        File docZipFile = new File(localFilePath + File.separator + csvZipFileName);

        uploadFile(getReqLocalDir() + "/" + csvZipFileName, getGenerateFileFtpDir());

        /**
         * 5.更改生成完成时间及FTP地址
         */
        Map<String, Object> paramterFtpMap = new HashMap<String, Object>(3);
        paramterFtpMap.put("reqId", reqId);
        paramterFtpMap.put("reqResultState", "3");
        paramterFtpMap.put("reqResultAddr", csvZipFileName);
        paramterFtpMap.put("reqStatus", "KXZ");
        paramterFtpMap.put("reqResultEndTime", "end");
        xqglMapper.updateRequirement(paramterFtpMap);
    }

    /**
     * <pre>
     * Desc  生成单个文件
     * @param reqId
     * @param csvSQL
     * @param csvHearder
     * @return
     * @author hufei
     * 2018-4-23 下午7:13:24
     * </pre>
     */
    private boolean generate(String reqId, String csvSQL, String csvHearder) {
        File file = new File(this.getReqLocalDir() + "/" + csvFileName);
        Writer streamWriter = null;
        try {
            streamWriter = new OutputStreamWriter(new FileOutputStream(file), "GBK");
            final PrintWriter printWriter = new PrintWriter(streamWriter);
            printWriter.println(csvHearder);
            jdbcTemplate.query(csvSQL, new RowCallbackHandler() {

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
            XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
            Map<String, Object> paramterMap = new HashMap<String, Object>(2);
            paramterMap.put("reqResultState", "2");
            paramterMap.put("reqId", reqId);
            xqglMapper.updateRequirement(paramterMap);
            throw new RuntimeException("生成csv文件异常", e);
        } finally {
            FileUtil.closeWriter(streamWriter);
            csvFileNames.add(csvFileName);

        }
        return false;
    }

    // 获取本地路径
    public String getLocalPath() {
        String tempDir = propertyUtil.getPropValue("tempDirV2");
        return tempDir;
    }

    protected String getGenerateFileFtpDir() {
        String attachFtpDir = propertyUtil.getPropValue("generateFileFtpDir");
        return attachFtpDir;
    }

    // 获取用户权限
    int getAuthorityAttrByDepId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("userId") == null) {
            session.setAttribute("depId", 10009);
        }
        Object depId = session.getAttribute("depId");

        if (Integer.parseInt(depId.toString()) == 12) {
            return 1;//集团业支
        }
        if (Integer.parseInt(depId.toString()) == 10009) {
            return 2;//惠普运维
        }
        return 0;//其他
    }

    // 获取用户名
    private String getUserName(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("userId") == null) {
            return "HP_DAIYP";
        } else {
            return session.getAttribute("userId").toString();
        }
    }



    public void downFile(String fileName, String path, HttpServletResponse response,HttpServletRequest request) throws ParseException {

        String ftpServer = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpServer"));
        String dlpath = "http://" + ftpServer + path + "/" + fileName;
        logger.error("fileName:>>" + fileName);
        FileUtil.downFileByHttp(request,response, dlpath, fileName, logger);



    }


    public void webUploader(MultipartFile file, HttpServletRequest request,
                            HttpServletResponse response, String reqId,String type) throws Exception {
        String currentFilePath = getReqLocalDir();// 记录当前文件的绝对路径
        String fileName ="unKnown";
        if("new".equals(type)){
            fileName = file.getOriginalFilename();
            if(fileName != null && !fileName.equals("")){
                if(fileName.equals(new String(fileName.getBytes("iso8859-1"), "iso8859-1")))
                {
                    fileName=new String(fileName.getBytes("iso8859-1"),"utf-8");
                }
            }
        }
        if("handle".equals(type)){
            fileName = reqId + ".zip";
        }

        File f = new File(currentFilePath + "/" + fileName);
        file.transferTo(f);
        f.createNewFile();

        uploadFile(currentFilePath + "/" + fileName, getAttachFtpDir());
        updateAttachAddr(fileName, reqId,type);

    }

    public void updateAttachAddr(String addr, String reqId,String type) {
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        Map<String, Object> paramterMap = new HashMap<String, Object>();
        paramterMap.put("reqId", reqId);
        paramterMap.put("addr", addr);
        if("new".equals(type))
            xqglMapper.updateAttachAddr(paramterMap);
        if("handle".equals(type))
            xqglMapper.updateHandleAttachAddr(paramterMap);
    }

    public void deleteFtpFileExpired(int days) {
        deleteFtpFileList(getAttachFtpDir(), days, 1);//删除附件和导出文件
        deleteFtpFileList(getGenerateFileFtpDir(), days, 2);//删除生成文件
    }

    public void deleteFtpFileList(String path, int days, int type) {
        List<String> nameList = new ArrayList<String>();
        FtpUtil ftpTool = null;
        try {
            ftpTool = initFtp();
            if (ftpTool == null) {
                return;
            }
            nameList = ftpTool.getFileNameExpired(path, days);
            for (String ss : nameList) {
                ftpTool.deleteFile(path, ss);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("NotiFile downloadFile>>>" + e.getMessage(), e);
        } finally {
            if (ftpTool != null) {
                ftpTool.disConnect();
            }
        }
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        Map<String, Object> paramterMap = new HashMap<String, Object>();
        if (type == 1) {
            for (String ss : nameList) {
                paramterMap.clear();
                paramterMap.put("addr", ss);
                xqglMapper.updateAttachAddrByAddr(paramterMap);
            }
        }
        if (type == 2) {
            for (String ss : nameList) {
                paramterMap.clear();
                paramterMap.put("addr", ss);
                xqglMapper.updateGenAddrByAddr(paramterMap);
            }
        }

    }

    public String checkResultFile(HttpServletRequest request, String reqId) {
        Map<String, Object> paramterMap = new HashMap<String, Object>();
        paramterMap.put("reqId", reqId);
        int authority = getAuthorityAttrByDepId(request);
        if (authority == 1) {
            paramterMap.put("reqSrcPerson", getUserName(request));
        } else if (!"HP_DAIYP".equals(getUserName(request))) {
            return "noAuthority";
        }

        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        List<XqglData> list = xqglMapper.getDetailById(paramterMap);
        if (list == null || list.size() < 1 || list.get(0).getReqResultAddr() == null || "".equals(list.get(0).getReqResultAddr())) {
            return "noFile";
        } else {
            return "success";
        }

    }

    public void downloadResultFile(HttpServletRequest request, HttpServletResponse response, String reqId) {
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        Map<String, Object> paramterMap = new HashMap<String, Object>();
        paramterMap.put("reqId", reqId);
        List<XqglData> list = xqglMapper.getDetailById(paramterMap);
        try {
            String fileName = list.get(0).getReqResultAddr();
            downFile(fileName, getGenerateFileFtpDir(), response,request);
            paramterMap.put("userId", getUserName(request));
            paramterMap.put("fileName", fileName);
            xqglMapper.insertDownLoadLog(paramterMap);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void downAttachmentById(String reqId, HttpServletResponse response,String type,HttpServletRequest request) {
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        Map<String, Object> paramterMap = new HashMap<String, Object>();
        paramterMap.put("reqId", reqId);
        List<XqglData> tpList = xqglMapper.getDetailById(paramterMap);
        String attachAddr ="";
        if("new".equals(type))
            attachAddr = tpList.get(0).getReqAttachAddr();
        if("handle".equals(type))
            attachAddr = tpList.get(0).getReqHandleAttachAddr();
        try {
            downFile(attachAddr, getAttachFtpDir(), response,request);
        } catch (ParseException e) {
            logger.error("downloadAttachFileError>>>" + e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private CellStyle getStyle(SXSSFWorkbook wb, int index) {
        CellStyle style = wb.createCellStyle();
        if (index == 1) {//普通文本
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
        if (index == 2) {//列标题
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
        if (index == 3) {//大标题
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 16);
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

    public int getMyReqNum(String userId) {
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        Map<String, Object> paramterMap = new HashMap<String, Object>();
        paramterMap.put("reqSrcPersonId", userId);
        int a = xqglMapper.getMyReqNum(paramterMap);
        return a;
    }

    public int getDoneReqNumByApprove(String userId) {
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        Map<String, Object> paramterMap = new HashMap<String, Object>();
        paramterMap.put("reqApprovePersonId", userId);
        int a = xqglMapper.getDoneReqNumByApprove(paramterMap);
        return a;
    }

    public int getDoneReqNumByHandle() {
        XqglMapper xqglMapper = mybatisDao.getSqlSession().getMapper(XqglMapper.class);
        int a = xqglMapper.getDoneReqNumByHandle();
        return a;
    }


}