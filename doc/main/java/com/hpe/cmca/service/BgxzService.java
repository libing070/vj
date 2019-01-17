package com.hpe.cmca.service;

import com.asiainfo.biframe.privilege.IUser;
import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.BgxzMapper;
import com.hpe.cmca.interfaces.SystemLogMgMapper;
import com.hpe.cmca.pojo.BgxzData;
import com.hpe.cmca.util.CalendarUtils;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("BgxzService")
public class BgxzService extends BaseObject {

    @Autowired
    protected MybatisDao mybatisDao;

    /**
     * 分专题下载次数排名
     *
     * <pre>
     * Desc
     * @param m
     * @return
     * @author issuser
     * 2017-12-11 上午10:56:19
     * </pre>
     */
    public List<Map<String, Object>> downNumsBySubject(BgxzData bgxz) {
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Map<String, Object>> dataList = bgxzMapper.downNumsBySubject(bgxz);
	return dataList;
    }

    /**
     * 分省下载次数排名
     *
     * <pre>
     * Desc
     * @param m
     * @return
     * @author issuser
     * 2017-12-12 上午10:48:55
     * </pre>
     */
    public List<Map<String, Object>> downNumsByPrvd(BgxzData bgxz) {
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Map<String, Object>> dataList = bgxzMapper.downNumsByPrvd(bgxz);
	return dataList;
    }

    /**
     * c. 专题审计报告生成趋势图
     *
     * <pre>
     * Desc
     * @param m
     * @return
     * @author issuser
     * 2017-12-15 下午5:40:31
     * </pre>
     */
    public Map<String, Object> createDayBySubjectMonth(BgxzData bgxz) {
	Map<String, Object> resultMap = new HashMap<String, Object>();
	List<Map<String, Object>> reportList = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> pmList = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> tbList = new ArrayList<Map<String, Object>>();
	Map<String, Object> tmpMap = null;
	List<String> reportmonthList = new ArrayList<String>();
	List<String> detailmonthList = new ArrayList<String>();
	List<String> pmmonthList = new ArrayList<String>();
	List<String> tbmonthList = new ArrayList<String>();
	List<Integer> reportdataList = new ArrayList<Integer>();
	List<Integer> detaildataList = new ArrayList<Integer>();
	List<Integer> pmdataList = new ArrayList<Integer>();
	List<Integer> tbdataList = new ArrayList<Integer>();
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Map<String, Object>> dataList = bgxzMapper.createDayBySubjectMonth(bgxz);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	Calendar c = Calendar.getInstance();
	c.add(Calendar.MONTH, -5);
	String d2 = sdf.format(c.getTime());
	String d1 = sdf.format(new Date());
	List<String> audList = getAudTrmListToSomeDate(d1, d2);
	for (Map<String, Object> d : dataList) {
	    if (d.get("file_type") != null && d.get("file_type").equals("audReport")) {
		tmpMap = new HashMap<String, Object>();
		tmpMap.put(d.get("create_date") == null ? "" : (String) d.get("create_date"), d.get("someday") == null ? 0 : (Integer) d.get("someday"));
		reportmonthList.add(d.get("create_date") == null ? "" : (String) d.get("create_date"));
		reportList.add(tmpMap);
	    }
	    if (d.get("file_type") != null && d.get("file_type").equals("audDetail")) {
		tmpMap = new HashMap<String, Object>();
		tmpMap.put(d.get("create_date") == null ? "" : (String) d.get("create_date"), d.get("someday") == null ? 0 : (Integer) d.get("someday"));
		detailmonthList.add(d.get("create_date") == null ? "" : (String) d.get("create_date"));
		detailList.add(tmpMap);
	    }
	    if (d.get("file_type") != null && d.get("file_type").equals("auditTB")) {
		tmpMap = new HashMap<String, Object>();
		tmpMap.put(d.get("create_date") == null ? "" : (String) d.get("create_date"), d.get("someday") == null ? 0 : (Integer) d.get("someday"));
		tbmonthList.add(d.get("create_date") == null ? "" : (String) d.get("create_date"));
		tbList.add(tmpMap);
	    }
	    if (d.get("file_type") != null && d.get("file_type").equals("auditPm")) {
		tmpMap = new HashMap<String, Object>();
		tmpMap.put(d.get("create_date") == null ? "" : (String) d.get("create_date"), d.get("someday") == null ? 0 : (Integer) d.get("someday"));
		pmmonthList.add(d.get("create_date") == null ? "" : (String) d.get("create_date"));
		pmList.add(tmpMap);
	    }
	}
	int size = audList.size();
	for (int i = size - 1; i >= 0; i--) {
	    String aud = audList.get(i);
	    if (!reportmonthList.contains(aud)) {
		reportdataList.add(0);
	    } else {
		for (Map<String, Object> d : reportList) {
		    if (d.containsKey(aud))
			reportdataList.add((Integer) d.get(aud));
		}

	    }
	    if (!detailmonthList.contains(aud)) {
		detaildataList.add(0);
	    } else {
		for (Map<String, Object> d : detailList) {
		    if (d.containsKey(aud))
			detaildataList.add((Integer) d.get(aud));
		}

	    }
	    if (!tbmonthList.contains(aud)) {
		tbdataList.add(0);
	    } else {
		for (Map<String, Object> d : tbList) {
		    if (d.containsKey(aud))
			tbdataList.add((Integer) d.get(aud));
		}

	    }
	    if (!pmmonthList.contains(aud)) {
		pmdataList.add(0);
	    } else {
		for (Map<String, Object> d : pmList) {
		    if (d.containsKey(aud))
			pmdataList.add((Integer) d.get(aud));
		}

	    }
	}
	resultMap.put("reportday", reportdataList);
	resultMap.put("detailday", detaildataList);
	resultMap.put("pmday", pmdataList);
	resultMap.put("tbday", tbdataList);
	return resultMap;
    }

    protected List<String> getAudTrmListToSomeDate(String d1, String d2) {
	List<String> dateList = new ArrayList<String>();
	String temp = d1;
	dateList.add(temp);
	while (!d2.equals(temp)) {
	    temp = getLastMon(temp);
	    dateList.add(temp);

	}
	return dateList;
    }

    protected String getLastMon(String d1) {
	Integer thisYear = Integer.parseInt(d1.substring(0, 4));
	Integer thisMonth = Integer.parseInt("0".equals(d1.substring(4, 5)) ? d1.substring(5, 6) : d1.substring(4, 6));
	Integer retYear = thisYear;
	Integer retMonth = 0;
	if (thisMonth >= 2) {
	    retMonth = thisMonth - 1;
	} else if (thisMonth == 1) {
	    retMonth = 12;
	    retYear = thisYear - 1;
	}
	String mon = retMonth.toString();
	StringBuffer sb = new StringBuffer(2);
	sb.append("0");
	if (retMonth <= 9) {
	    sb.append(retMonth);
	    mon = sb.toString();
	}
	return retYear.toString() + mon;
    }

    public List<Map<String, Object>> selReportUpload(BgxzData bgxz, HttpServletRequest request) {
	IUser user = (IUser) request.getSession().getAttribute("ssoUSER");
	bgxz.setLoginAccount(user.getUserid());
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Map<String, Object>> dataList = bgxzMapper.selReportUpload(bgxz);
	if (dataList != null && dataList.size() > 0) {
	    for (Map<String, Object> d : dataList) {
		String file_path = d.get("file_path") == null ? "" : String.valueOf(d.get("file_path"));
		file_path = file_path.substring(file_path.lastIndexOf("/") + 1);
		d.put("file_path", file_path);
	    }
	}
	return dataList;
    }

    /**
     * 查询审批和上传人信息
     *
     * <pre>
     * Desc
     * @param bgxz
     * @return
     * @author issuser
     * 2018-2-1 上午11:48:15
     * </pre>
     */
    public List<Map<String, Object>> selReportPerson(BgxzData bgxz) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("flag", bgxz.getFlag());
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Map<String, Object>> dataList = bgxzMapper.selReportPerson(params);
	return dataList;
    }

    /**
     * 统计上传文件数量
     *
     * <pre>
     * Desc
     * @param bgxz
     * @return
     * @author issuser
     * 2018-2-1 上午11:48:00
     * </pre>
     */
    public Integer countReportUpload(BgxzData bgxz) {
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	Integer count = bgxzMapper.countReportUpload(bgxz);

	return count;
    }

    public String zipFile(String[] ids_) {
	List<String> filelist = new ArrayList<String>();
	List<File> manylist = new ArrayList<File>();
	File files = null;
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Integer> ids = new ArrayList<Integer>();
	for (int i = 0; i < ids_.length; i++) {
	    ids.add(Integer.parseInt(ids_[i]));
	}
	String resultpath = null;
	String path = getLocalPath();
	String pathTmp = getLocalPathTmp();
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("ids", ids);
	List<Map<String, Object>> fList = bgxzMapper.downUploadFile(params);
	int size = fList == null ? 0 : fList.size();
	if (size > 1) {
	    for (Map<String, Object> f : fList) {
		if (f.get("file_name") != null) {
		    // String fpp =String.valueOf(f.get("file_name"));
		    filelist.add(String.valueOf(f.get("file_name")));
		}
		if (f.get("status").equals("wait") || f.get("status").equals("no")) {
		    files = new File(pathTmp + String.valueOf(f.get("file_name")));
		    manylist.add(files);
		}
		if (f.get("status").equals("yes")) {
		    files = new File(path + String.valueOf(f.get("file_name")));
		    manylist.add(files);
		}
	    }
	    String zipFileName = "下载.zip";
	    try {
		FileUtil.zipFile(manylist.toArray(new File[manylist.size()]), path + zipFileName);
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    // FileUtil.zipFile(path, path, zipFileName, filelist);
	    String zipFilePath = FileUtil.buildFullFilePath(path, zipFileName);
	    uploadFile(zipFilePath, getFtpPath("000000", "0000"));
	    resultpath = buildDownloadUrl(zipFileName, "000000", "0000");
	} else if (size == 1) {
	    Map<String, Object> f = fList.get(0);
	    String fname = String.valueOf(f.get("file_name"));
	    if (f.get("status").equals("wait") || f.get("status").equals("no")) {
		files = new File(pathTmp + String.valueOf(f.get("file_name")));
		manylist.add(files);
	    }
	    if (f.get("status").equals("yes")) {
		files = new File(path + String.valueOf(f.get("file_name")));
		manylist.add(files);
	    }
	    String zipFilePath = FileUtil.buildFullFilePath(path, fname);
	    uploadFile(zipFilePath, getFtpPath("000000", "0000"));
	    resultpath = buildDownloadUrl(fname, "000000", "0000");
	}

	// String path ="D:/data1/hp_web/caUpFile/";
	return resultpath;
    }

    public String getFtpPath(String audtrm, String focusCd) {
	String tempDir = propertyUtil.getPropValue("ftpPath");
	String finalPath = FileUtil.buildFullFilePath(tempDir, buildRelativePath(audtrm, focusCd));
	FileUtil.mkdirs(finalPath);
	return finalPath;// + "/" + zipFileName;
    }

    protected String buildRelativePath(String audTrm, String focusCd) {
	String subjectId = focusCd.substring(0, 1);
	StringBuilder path = new StringBuilder();
	path.append(audTrm).append("/").append(subjectId).append("/").append(focusCd);
	return path.toString();
    }

    // tempDirUploadTmp

    public String getLocalPath() {
	String tempDir = propertyUtil.getPropValue("tempDirUpload");
	FileUtil.mkdirs(tempDir);
	return propertyUtil.getPropValue("tempDirUpload");
    }

    public String getLocalPathTmp() {
	String tempDir = propertyUtil.getPropValue("tempDirUploadTmp");
	FileUtil.mkdirs(tempDir);
	return propertyUtil.getPropValue("tempDirUploadTmp");
    }

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

    protected String buildDownloadUrl(String zipFileName, String audtrm, String focusCd) {
	String ftpHttpUrlPrefix = propertyUtil.getPropValue("ftpHttpUrlPrefix");
	StringBuilder url = new StringBuilder(30);
	url.append(buildRelativePath(audtrm, focusCd)).append("/").append(zipFileName);
	return FileUtil.buildFullFilePath(ftpHttpUrlPrefix, String.valueOf(url));
    }

    /**
     * 专题报告下载清单
     *
     * <pre>
     * Desc
     * @param bgxz
     * @return
     * @author issuser
     * 2017-12-18 上午11:22:29
     * </pre>
     *
     * @throws ParseException
     */
    public List<Map<String, Object>> downRecordsTable(BgxzData bgxz, HttpServletRequest request) throws ParseException {
	// 初始化报告下载数据
	String fcouscd = bgxz.getSubjectId().equals("9") ? "9999" : bgxz.getSubjectId() + "000";
	if("11".equals(bgxz.getSubjectId()))fcouscd = "1100";
	if("12".equals(bgxz.getSubjectId()))fcouscd = "1200";
	if("13".equals(bgxz.getSubjectId()))fcouscd = "1300";
	initData(bgxz.getAudTrm(), bgxz.getSubjectId(), fcouscd, bgxz.getPrvdId());
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Map<String, Object>> dataList = bgxzMapper.downRecordsTable(bgxz);
	Iterator<Map<String, Object>> iter = dataList.iterator();
	while (iter.hasNext()) {
	    Map<String, Object> data = iter.next();
	    if (data.get("audit_monthly") != null) {
		if (data.get("audit_subject") != null && data.get("audit_subject").equals("2")) {
		    data.put("sjqj", CalendarUtils.buildAuditTimeOfMonth(String.valueOf(data.get("audit_monthly")), 3));
		} else if (data.get("audit_subject") != null && data.get("audit_subject").equals("3")) {
		    data.put("sjqj", CalendarUtils.buildAuditTimeOfMonth(String.valueOf(data.get("audit_monthly")), 4));
		} else if (data.get("audit_subject") != null && data.get("audit_subject").equals("4")) {
		    String aud_cyl = CalendarUtils.buildAuditTimeOfMonth(String.valueOf(data.get("audit_monthly")), 1);
		    String aud_trm = aud_cyl.substring(aud_cyl.indexOf("-") + 1, aud_cyl.length());
		    data.put("sjqj", "2013年1月1日-" + aud_trm);
		} else if (data.get("audit_subject") != null && (data.get("audit_subject").equals("12")||data.get("audit_subject").equals("13"))) {
			//删除虚假宽带里的审计通报,以后需要审计通报时只需要注释掉就可以了
			if (data.get("file_typeTMP").equals("auditTB")) {
				iter.remove();
				continue;
			}
			// 虚假宽带审计期间特殊处理
			data.put("sjqj", CalendarUtils.buildAuditTimeOfMonth(String.valueOf(data.get("audit_monthly")), -3));
		} else {
		    data.put("sjqj", CalendarUtils.buildAuditTimeOfMonth(String.valueOf(data.get("audit_monthly"))));
		    if (data.get("audit_subject") != null && !data.get("audit_subject").equals("1") && data.get("file_typeTMP") != null && data.get("file_typeTMP").equals("auditTB")) {
			//dataList.remove(data);
				iter.remove();
		    }
		}

		// data.put("create_person", session.getAttribute("userName"));
	    }

	}
	return dataList;
    }

    /**
     * 第一次查询表里没数据时进行初始化
     *
     * <pre>
     * Desc
     * @param audTrm
     * @param subjectId
     * @param focusCd
     * @param prvdId
     * @author issuser
     * 2018-1-17 下午3:13:29
     * </pre>
     */
    public void initData(String audTrm, String subjectId, String focusCd, int prvdId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("subjectId", subjectId);
	// params.put("focusCd", focusCd);
	params.put("audTrm", audTrm);
	params.put("prvdId", prvdId);
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Map<String, Object>> dataList = bgxzMapper.selReportLog(params);
	// List<Map<String,Object>> dataList =mybatisDao.getList("com.hpe.cmca.interfaces.BgxzMapper.selReportLog", params);
	List<String> ft = null;
	if ("9".equals(subjectId)) {
	    String fs[] = { "auditPm" };
	    List<String> ftss = Arrays.asList(fs);
	    ft = new ArrayList<String>(ftss);
	    for (Map<String, Object> d : dataList) {
		Iterator<String> iter = ft.iterator();
		while (iter.hasNext()) {
		    String item = iter.next();
		    if (item.equals(String.valueOf(d.get("file_type")))) {
			iter.remove();
		    }
		}
	    }
	} else {
	    String fs[] = { "auditPm", "auditTB", "audReport", "audDetail" };
	    List<String> ftss = Arrays.asList(fs);
	    ft = new ArrayList<String>(ftss);
	    for (Map<String, Object> d : dataList) {
		Iterator<String> iter = ft.iterator();
		while (iter.hasNext()) {
		    String item = iter.next();
		    if (item.equals(String.valueOf(d.get("file_type")))) {
			iter.remove();
		    }
		    try {
				if ((item.equals("auditPm")||item.equals("auditTB")) && prvdId != 10000) iter.remove();//省公司没有排名汇总
			}
			catch(Exception e){
		    	logger.error("初始化report_log省公司排名汇总已经删除过");
			}
		}
	    }
	}
	for (String at : ft) {
	    // if(at.equals("auditPm") &&subjectId.equals("5")){
	    // BgxzData bgxz =new BgxzData();
	    // bgxz.setAudTrm(audTrm);
	    // bgxz.setFileType(at);
	    // bgxz.setSubjectId(subjectId);
	    // bgxz.setFocusCd("5000");
	    // bgxz.setPrvdId(prvdId);
	    // bgxz.setDownCount(0);
	    // bgxz.setCreateType("manual");
	    // addReportLog(bgxz,"empty");
	    // bgxz.setFocusCd("5001");
	    // addReportLog(bgxz,"empty");
	    // bgxz.setFocusCd("5002");
	    // addReportLog(bgxz,"empty");
	    // bgxz.setFocusCd("5003");
	    // addReportLog(bgxz,"empty");
	    // }else{
		if((at.equals("auditPm")||at.equals("auditTB"))&& prvdId != 10000)continue;
	    BgxzData bgxz = new BgxzData();
	    bgxz.setAudTrm(audTrm);
	    bgxz.setFileType(at);
	    bgxz.setSubjectId(subjectId);
	    bgxz.setFocusCd(focusCd);
	    bgxz.setPrvdId(prvdId);
	    bgxz.setDownCount(0);
	    bgxz.setCreateType("manual");
	    addReportLog(bgxz, "empty");
	    // }

	}

    }

    /**
     * 添加报告下载日志（手动生成/下载）
     *
     * <pre>
     * Desc
     * @param create_type (manual手动生成/auto自动生成)
     * @param userName
     * @param focusCd
     * @param month
     * @author issuser
     * 2017-12-18 下午4:36:11
     * </pre>
     */
    public void addReportLog(BgxzData bgxz, String flag) {
	Map<String, Object> params = new HashMap<String, Object>();
	// if(bgxz.getSubjectId().equals("7")){
	// bgxz.setFocusCd("700101");
	// }
	params.put("subjectId", bgxz.getSubjectId());
	params.put("focusCd", bgxz.getFocusCd());
	params.put("audTrm", bgxz.getAudTrm());
	params.put("prvdId", bgxz.getPrvdId());
	params.put("operType", bgxz.getOperType());
	params.put("operPerson", bgxz.getOperPerson());
	params.put("fileType", bgxz.getFileType());
	params.put("createType", bgxz.getCreateType());
	params.put("loginAccount", bgxz.getLoginAccount());
	params.put("downCount", 0);
	if (flag.equals("down")) {
	    params.put("downDatetime", bgxz.getDownDatetime());
	    params.put("downCount", bgxz.getDownCount());
	    params.put("filePath", bgxz.getFilePath());
	    params.put("fileName", bgxz.getFileName());
	}
	if (flag.equals("create")) {
	    params.put("createDatetime", bgxz.getCreateDatetime());
	    params.put("filePath", bgxz.getFilePath());
	    params.put("fileName", bgxz.getFileName());
	}
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Map<String, Object>> logList = bgxzMapper.selReportLog(params);
	if (logList != null && logList.size() > 0) {

		// 2018-12-24 18:03:27  zhangqiang add
		if (bgxz.getSubjectId()=="6" || "6".equals( bgxz.getSubjectId())){
			List<Map<String, Object>> logList_new = bgxzMapper.selReportLog_new(params);
			if (logList_new != null && logList_new.size() > 0){
				bgxzMapper.updateReportLog6(params);
			} else {
				bgxzMapper.addReportLog(params);
			}
		}else {
			bgxzMapper.updateReportLog(params);
		}

	} else {
	    bgxzMapper.addReportLog(params);
	}

	try {
		// zhangqiang add by rzcx 2018-8-24 10:30:46
    	// 增加对 hpmgr.busi_report_log 表的操作记录
		SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
		if (logList != null && logList.size() > 0) {
			systemlogmgMapper.updateReportLog(params);
		} else {
			systemlogmgMapper.addReportLog(params);
		}
	} catch (Exception e) {
		// TODO: handle exception
		logger.error("addReportLog >>>>>>>>>>>>>>>>>>params= " + params, e);
	}

    }

    public void updateReportLog(BgxzData bgxz) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("subjectId", bgxz.getSubjectId());
	params.put("fileType", bgxz.getFileType());
	params.put("audTrm", bgxz.getAudTrm());
	params.put("prvdId", bgxz.getPrvdId());
	params.put("createType", bgxz.getCreateType());
	params.put("createDatetime", bgxz.getCreateDatetime());
	params.put("fileType", "");
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	bgxzMapper.updateReportLog(params);

	try {
		// zhangqiang add by rzcx 2018-8-24 10:30:46
    	// 增加对 hpmgr.busi_report_log 表的操作记录
		SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
		systemlogmgMapper.updateReportLog(params);
	} catch (Exception e) {
		// TODO: handle exception
		logger.error("updateReportLog >>>>>>>>>>>>>>>>>>params= " + params, e);
	}

    }

    /**
     * 获取清单文件列表
     *
     * <pre>
     * Desc
     * @param bgxz
     * @return
     * @author issuser
     * 2018-2-1 上午11:47:17
     * </pre>
     */
    public Map<String, Object> selCsvBySubjectId(BgxzData bgxz) {
	Map<String, Object> result = new HashMap<String, Object>();
	List<String> fnList = new ArrayList<String>();
	List<String> pathList = new ArrayList<String>();
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("subjectId", bgxz.getSubjectId());
	params.put("fileType", bgxz.getFileType());
	params.put("audTrm", bgxz.getAudTrm());
	params.put("prvdId", bgxz.getPrvdId());
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Map<String, Object>> logList = bgxzMapper.selReportLogCsv(params);
	for (Map<String, Object> data : logList) {
	    if (data.get("file_name") != null) {
		fnList.add(String.valueOf(data.get("file_name")));
	    }
	    if (data.get("file_path") != null) {
		pathList.add(String.valueOf(data.get("file_path")));
	    }
	}
	result.put("filenames", fnList);
	result.put("filepaths", pathList);
	return result;
    }

    /**
     * 更新初始化数据不全记录
     *
     * <pre>
     * Desc
     * @param bgxz
     * @author issuser
     * 2018-1-17 下午3:03:17
     * </pre>
     */
    public int updateInitReportLog(BgxzData bgxz, String flag) {
	int count = 0;
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("subjectId", bgxz.getSubjectId());
	params.put("fileType", bgxz.getFileType());
	params.put("audTrm", bgxz.getAudTrm());
	params.put("prvdId", bgxz.getPrvdId());
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Map<String, Object>> logList = bgxzMapper.selReportLog(params);
	if (logList != null && logList.size() == 1) {
	    Map<String, Object> sdata = logList.get(0);
	    if (sdata != null && sdata.size() > 0) {
		if (sdata.get("down_datetime") == null && sdata.get("create_datetime") == null && sdata.get("file_path") == null) {
		    params = new HashMap<String, Object>();
		    params.put("subjectId", bgxz.getSubjectId());
		    params.put("focusCd", bgxz.getFocusCd());
		    params.put("audTrm", bgxz.getAudTrm());
		    params.put("prvdId", bgxz.getPrvdId());
		    params.put("operType", bgxz.getOperType());
		    params.put("operPerson", bgxz.getOperPerson());
		    params.put("fileType", bgxz.getFileType());
		    params.put("createType", bgxz.getCreateType());
		    params.put("loginAccount", bgxz.getLoginAccount());
		    params.put("fileName", bgxz.getFileName());
		    if (flag.equals("down")) {
			params.put("downDatetime", bgxz.getDownDatetime());
			params.put("downCount", bgxz.getDownCount());
		    }
		    if (flag.equals("create")) {
			params.put("createDatetime", bgxz.getCreateDatetime());
			params.put("filePath", bgxz.getFilePath());
			params.put("downCount", 0);
		    }
		    bgxzMapper.updateReportLog2(params);

		    try {
		    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
		    	systemlogmgMapper.updateReportLog2(params);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("updateInitReportLog >>>>>>>>>>>>>>>>>>params= " + params, e);
			}

		    count = 1;
		}
	    }
	}
	return count;
    }

    /**
     * 流量违规专题特殊处理（多个清单）
     *
     * <pre>
     * Desc
     * @param bgxz
     * @param flag
     * @return
     * @author issuser
     * 2018-2-24 下午2:18:49
     * </pre>
     */
    public int updateReportLog7(BgxzData bgxz, String flag) {
	int count = 0;
	Map<String, Object> params7 = new HashMap<String, Object>();
	params7.put("subjectId", bgxz.getSubjectId());
	params7.put("fileType", bgxz.getFileType());
	params7.put("audTrm", bgxz.getAudTrm());
	params7.put("prvdId", bgxz.getPrvdId());
	params7.put("focusCd", "7000");
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Map<String, Object>> logList7 = bgxzMapper.selReportLog(params7);
	if (logList7 != null && logList7.size() == 1) {
	    Map params = new HashMap<String, Object>();
	    params.put("subjectId", bgxz.getSubjectId());
	    params.put("focusCd", "7000");
	    params.put("audTrm", bgxz.getAudTrm());
	    params.put("prvdId", bgxz.getPrvdId());
	    params.put("operType", bgxz.getOperType());
	    params.put("operPerson", bgxz.getOperPerson());
	    params.put("fileType", bgxz.getFileType());
	    params.put("createType", bgxz.getCreateType());
	    params.put("loginAccount", bgxz.getLoginAccount());
	    params.put("fileName", bgxz.getFileName());
	    if (flag.equals("down")) {
		params.put("downDatetime", bgxz.getDownDatetime());
		params.put("downCount", bgxz.getDownCount());
	    }
	    if (flag.equals("create")) {
		params.put("createDatetime", bgxz.getCreateDatetime());
		params.put("filePath", bgxz.getFilePath());
		params.put("downCount", 0);
	    }
	    bgxzMapper.updateReportLog7(params);
	    try {
	    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
	    	systemlogmgMapper.updateReportLog7(params);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("updateReportLog7 >>>>>>>>>>>>>>>>>>params= " + params, e);
		}
	    count = 1;
	}
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("subjectId", bgxz.getSubjectId());
	params.put("fileType", bgxz.getFileType());
	params.put("audTrm", bgxz.getAudTrm());
	params.put("prvdId", bgxz.getPrvdId());
	params.put("focusCd", bgxz.getFocusCd());
	List<Map<String, Object>> logList = bgxzMapper.selReportLog(params);
	if (logList != null && logList.size() == 1) {
	    Map<String, Object> sdata = logList.get(0);
	    if (sdata != null && sdata.size() > 0) {
		if (sdata.get("down_datetime") == null && sdata.get("create_datetime") == null && sdata.get("file_path") == null) {
		    params = new HashMap<String, Object>();
		    params.put("subjectId", bgxz.getSubjectId());
		    params.put("focusCd", bgxz.getFocusCd());
		    params.put("audTrm", bgxz.getAudTrm());
		    params.put("prvdId", bgxz.getPrvdId());
		    params.put("operType", bgxz.getOperType());
		    params.put("operPerson", bgxz.getOperPerson());
		    params.put("fileType", bgxz.getFileType());
		    params.put("createType", bgxz.getCreateType());
		    params.put("loginAccount", bgxz.getLoginAccount());
		    params.put("fileName", bgxz.getFileName());
		    if (flag.equals("down")) {
			params.put("downDatetime", bgxz.getDownDatetime());
			params.put("downCount", bgxz.getDownCount());
		    }
		    if (flag.equals("create")) {
			params.put("createDatetime", bgxz.getCreateDatetime());
			params.put("filePath", bgxz.getFilePath());
			params.put("downCount", 0);
		    }
		    bgxzMapper.updateReportLog7(params);
		    try {
		    	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
		    	systemlogmgMapper.updateReportLog7(params);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("updateReportLog7 >>>>>>>>>>>>>>>>>>params= " + params, e);
			}
		}
	    }
	} else {
	    addReportLog(bgxz, "create");
	}
	return count;
    }

    public void deleteReportLog(BgxzData bgxz) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("subjectId", bgxz.getSubjectId());
	params.put("fileType", bgxz.getFileType());
	params.put("audTrm", bgxz.getAudTrm());
	params.put("prvdId", bgxz.getPrvdId());
	params.put("focusCd", bgxz.getFocusCd());
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	bgxzMapper.deleteReportLog(params);

	try {
		SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
		systemlogmgMapper.deleteReportLog(params);
	} catch (Exception e) {
		// TODO: handle exception
		logger.error("deleteReportLog >>>>>>>>>>>>>>>>>>params= " + params, e);
	}
    }

    /**
     * 审批上传文件
     *
     * <pre>
     * Desc
     * @param ids_
     * @param status
     * @param reviewOpinion
     * @author issuser
     * 2018-2-1 上午11:46:21
     * </pre>
     */
    public void reviewReportUpload(String[] ids_, String status, String reviewOpinion) {
	List<Integer> ids = new ArrayList<Integer>();
	for (int i = 0; i < ids_.length; i++) {
	    ids.add(Integer.parseInt(ids_[i]));
	}
	try {
	    if (reviewOpinion != null && !reviewOpinion.equals("")) {
		if (reviewOpinion.equals(new String(reviewOpinion.getBytes("iso8859-1"), "iso8859-1"))) {
		    reviewOpinion = new String(reviewOpinion.getBytes("iso8859-1"), "utf-8");
		}
	    }
	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("ids", ids);
	data.put("status", status);
	data.put("reviewOpinion", reviewOpinion);
	bgxzMapper.reviewReportUpload(data);
	String currentFilePath = getLocalPath();
	String tempDirUploadTmp = propertyUtil.getPropValue("tempDirUploadTmp");
	String tempDirUpload = propertyUtil.getPropValue("tempDirUpload");
	BgxzData bgxz_ = new BgxzData();
	bgxz_.setIds(ids);
	List<Map<String, Object>> uplist = bgxzMapper.selReportUpload(bgxz_);
	for (Map<String, Object> m : uplist) {
	    if (m.get("status") != null && m.get("status").equals("yes")) {
		fileMoveto(tempDirUploadTmp, String.valueOf(m.get("file_name")), tempDirUpload);
		uploadFile(FileUtil.buildFullFilePath(currentFilePath, String.valueOf(m.get("file_name"))), getFtpPath(String.valueOf(m.get("aud_trm")), String.valueOf(m.get("focus_cd"))));
	    }
	}

    }

    /**
     * 移动文件
     *
     * <pre>
     * Desc
     * @param filepathfrom
     * @param filename
     * @param filepathto
     * @author issuser
     * 2018-2-1 上午11:46:43
     * </pre>
     */
    public void fileMoveto(String filepathfrom, String filename, String filepathto) {
	String startPath = filepathfrom + filename;
	String endPath = filepathto + File.separator;
	try {

	    File startFile = new File(startPath);
	    File tmpFile = new File(endPath);// 获取文件夹路径
	    if (!tmpFile.exists()) {// 判断文件夹是否创建，没有创建则创建新文件夹
		tmpFile.mkdirs();
	    }
	    FileUtils.copyFileToDirectory(startFile, tmpFile);
	    if (startFile.exists())
		startFile.delete();
	} catch (Exception e) {
	    // log.info("文件移动异常！文件名：《{}》 起始路径：{}",filename,startPath);
	    e.printStackTrace();
	}
    }

    // public void fileMoveto(String filepathfrom,String filename,String filepathto){
    // String startPath = filepathfrom + filename;
    // String endPath = filepathto + File.separator;
    // try {
    // File startFile = new File(startPath);
    // File tmpFile = new File(endPath);//获取文件夹路径
    // if(!tmpFile.exists()){//判断文件夹是否创建，没有创建则创建新文件夹
    // tmpFile.mkdirs();
    // }
    // System.out.println(endPath + startFile.getName());
    // if (startFile.renameTo(new File(endPath + startFile.getName()))) {
    // System.out.println("File is moved successful!");
    // // log.info("文件移动成功！文件名：《{}》 目标路径：{}",filename,endPath);
    // } else {
    // System.out.println("File is failed to move!");
    // // log.info("文件移动失败！文件名：《{}》 起始路径：{}",filename,startPath);
    // }
    // } catch (Exception e) {
    // // log.info("文件移动异常！文件名：《{}》 起始路径：{}",filename,startPath);
    // e.printStackTrace();
    // }
    // }

    /**
     * 查询操作日志
     *
     * <pre>
     * Desc
     * @param bgxz
     * @return
     * @author issuser
     * 2018-2-1 上午11:49:28
     * </pre>
     */
    public List<Map<String, Object>> selReportLog(BgxzData bgxz) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("subjectId", bgxz.getSubjectId());
	params.put("fileType", bgxz.getFileType());
	params.put("audTrm", bgxz.getAudTrm());
	params.put("prvdId", bgxz.getPrvdId());
	params.put("focusCd", bgxz.getFocusCd());
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	return bgxzMapper.selReportLog(params);
    }

    /**
     * 统计审核文件数量
     *
     * <pre>
     * Desc
     * @return
     * @author issuser
     * 2018-2-1 上午11:49:45
     * </pre>
     */
    public Map<String, Object> countReviewFile() {
	BgxzData bgxz = new BgxzData();
	bgxz.setStatus("dsh");
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	int count_dsh = bgxzMapper.countReportUpload(bgxz);
	bgxz.setStatus("ysh");
	int count_ysh = bgxzMapper.countReportUpload(bgxz);
	Map<String, Object> result = new HashMap<String, Object>();
	result.put("dshCount", count_dsh);
	result.put("yshCount", count_ysh);
	return result;
    }

    /**
     * 驳回后删除文件
     *
     * <pre>
     * Desc
     * @param ids_
     * @author issuser
     * 2018-2-9 上午11:23:16
     * </pre>
     */
    public void delUploadFile(String[] ids_) {

	String currentFilePath = getLocalPathTmp();
	List<Integer> ids = new ArrayList<Integer>();
	for (int i = 0; i < ids_.length; i++) {
	    ids.add(Integer.parseInt(ids_[i]));
	}
	BgxzData data = new BgxzData();
	data.setIds(ids);
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Map<String, Object>> dataList = bgxzMapper.selReportUpload(data);
	for (Map<String, Object> so : dataList) {
	    File f = new File(currentFilePath + String.valueOf(so.get("file_name")));
	    if (f.exists()) {
		f.delete();
	    }
	}
	Map<String, Object> p = new HashMap<String, Object>();
	p.put("ids", ids);
	bgxzMapper.delUploadFile(p);

    }

    /**
     * 添加上传日志记录
     *
     * <pre>
     * Desc
     * @param bgxz
     * @author issuser
     * 2018-1-17 下午3:05:01
     * </pre>
     */
    public void uploadReportLog(BgxzData bgxz) {

	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Map<String, Object>> logList = bgxzMapper.selReportUpload(bgxz);
	if (logList != null && logList.size() > 0) {
	    bgxzMapper.updateReportUpload(bgxz);
	} else {
	    bgxzMapper.addReportUpload(bgxz);
	}
    }

    public String getSubjectName(String subjectId) {
	String subjectName = null;
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("subjectId", subjectId);
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Map<String, Object>> dataList = bgxzMapper.selSubjectName(params);
	if (dataList != null && dataList.size() > 0) {
	    subjectName = String.valueOf(dataList.get(0).get("name"));
	}
	return subjectName;
    }

}