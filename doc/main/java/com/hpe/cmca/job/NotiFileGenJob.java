package com.hpe.cmca.job;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.common.Constants;
import com.hpe.cmca.pojo.BgxzData;
import com.hpe.cmca.service.BgxzService;
import com.hpe.cmca.service.ConcernFileGenService;
import com.hpe.cmca.service.NotiFileGenService;
import com.hpe.cmca.util.DateUtilsForCurrProject;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;

@Service("NotiFileGenJob")
public class NotiFileGenJob extends BaseObject{
	@Autowired
	protected ConcernFileGenService	concernFileGenService	= null;
	@Autowired
	public NotiFileGenService notiFileGenService	= null;

   @Autowired
    private JdbcTemplate jdbcTemplate = null;
	
	@Autowired
	public BgxzService bgxzService	= null;

	public List<AbstractNotiFileProcessor> notiFileGenProcessorList = new  ArrayList<AbstractNotiFileProcessor>();

	protected Map<String, Object> notiFile = null;

	protected String month = null;

	protected String focusCd = null;

	protected String userName = null;

	protected String loginAccount = null;

	protected String zipFileName = null;

	protected boolean error = false;

	@Autowired
	public NotiFileGenService pmhzService =null;
	protected String subjectId = null;
	protected String focusNum = null;
	protected Boolean isAuto = false;

	protected String oprUser="unknown";
	protected String oprPrvd= "unknown";

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int work(){
		if(isAuto){
			Map<String,Object> params=new HashMap<String,Object>();
			List<Map<String, Object>> result = mybatisDao.getList("commonMapper.selectAutoInfo", params);
			if(result.get(0).get("pmhz_auto")!=null&&"0".equals(result.get(0).get("pmhz_auto").toString()))
				return 3;//关闭定时任务
		}

		int a=0;
		List<String> alrGenAudTrm = new ArrayList<String>();
		try{
		List<Map<String, Object>> result=pmhzService.getFileGenRequstNew(subjectId,focusNum,isAuto);
			if(!result.isEmpty()){
				for(Map<String, Object> each:result){
					String audTrm =(String) each.get("audTrm");
					Integer id =(Integer) each.get("id");
					if(isAuto){
						pmhzService.updatePmhzStatusNew(5, id);
						pmhzService.updatePmhzStatusByTrmSubNew(5,audTrm, subjectId);
					}
					else {
						pmhzService.updatePmhzStatusNew(6, id);
						pmhzService.updatePmhzStatusByTrmSubNew(6,audTrm, subjectId);
					}

					if(!alrGenAudTrm.contains(audTrm)){
						bgxzService.initData(audTrm, subjectId, focusCd, 10000) ;//初始化report_log表
						a=genFile(audTrm,focusCd,isAuto);
						if(!isAuto)pmhzService.addHandAndDownloadInfo(oprUser, audTrm, subjectId, "1",oprPrvd);
					}						alrGenAudTrm.add(audTrm);

					if(isAuto){
						pmhzService.updatePmhzStatusNew(2, id);
						pmhzService.updatePmhzStatusByTrmSubNew(2,audTrm, subjectId);
					}
					else {
						pmhzService.updatePmhzStatusNew(3, id);
						pmhzService.updatePmhzStatusByTrmSubNew(3,audTrm, subjectId);
					}

				}
				return a;
			}else{
				return 2;//模型执行完成的省份的排名汇总已经生成完毕
			}
		}catch(Exception e){
			a = 1;//发生异常
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			return a;
		}
	}

		public int genFile(String audTrm,String _focusCd,Boolean isAuto) throws Exception{
				ArrayList<String> strs = new ArrayList<String>();
				focusCd = _focusCd;
				String subjectNm=Constants.MAP_SUBJECT_NAME.get(_focusCd.substring(0, 1));
				if("1100".equals(_focusCd)) subjectNm=Constants.MAP_SUBJECT_NAME.get("11");
				if("1200".equals(_focusCd)) subjectNm=Constants.MAP_SUBJECT_NAME.get("12");
				if("1300".equals(_focusCd)) subjectNm=Constants.MAP_SUBJECT_NAME.get("13");
				if("sjzl".equals(_focusCd)) subjectNm=Constants.MAP_SUBJECT_NAME.get("sjzl");
				if("12".equals(subjectId) || "13".equals(subjectId)){
					String newMonth = DateUtilsForCurrProject.getAfterAnyMonth(month, 1) ;
					zipFileName = "排名汇总_"+newMonth+"_"+subjectNm+".zip";
				}else{
					zipFileName = "排名汇总_"+month+"_"+subjectNm+".zip";
				}
//				String subjectId=(String)notiFile.get("audit_subject");
//				subjectId = Constants.MAP_SUBJECT_NAME.get(subjectId);
//				zipFileName = "排名汇总_"+month+"_"+subjectId+".zip";
				try {
					for (AbstractNotiFileProcessor processor : notiFileGenProcessorList) {
//						processor.set
						processor.setMonth(month);
						processor.setFocusCd(focusCd);
						processor.setLocalDir(getLocalDir());
						//TODO 生成排名汇总文件
						processor.start();
						
						//TODO 判断，如果当前focus_cd为6000则生成一份全国清单文件
						if("6000".equals(_focusCd)){
							//生成全国清单文件
							//设定focus_cd为6005
							String subjectId = "6" ;
							String initFocusCd = "6005" ;
							int prvdId = 10000 ;
							  // 获取新文件名
					        String localFileName ="全国_"+audTrm+"_电子商务基地重复发放电子券清单.csv" ;
					        
					     // 获取新文件生成目录
					        String localFilePath = getLocalDir();
					     // 从 HPMGR.busi_report_file_config 中查询"本专题、汇总关注点"的配置信息
							Map<String, Object>   configInfo = concernFileGenService.selectAllFileConfig(subjectId, initFocusCd);
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
					          //TODO 向zip中增加要打包的csv文件
						        strs.add(localFileName) ;
					            printWriter.flush();
					        } catch (Exception e) {
					            throw new RuntimeException("生成csv文件异常", e);
					        } finally {
					            FileUtil.closeWriter(streamWriter);
					        }
						}
	/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/					
						
//						if(("0000").equals(focusCd)||("6000").equals(focusCd)||("1000").equals(focusCd)
//								||("5000").equals(focusCd)||("5001").equals(focusCd)
//								||("5002").equals(focusCd)||("5003").equals(focusCd)||("7000").equals(focusCd)
//								||("9999").equals(focusCd)||("4000").equals(focusCd)){
//							ArrayList<String> strs1=processor.getFileNameList();
//							if(strs1.size()>0){
//								for(int i=0;i<strs1.size();i++){
//									strs.add(strs1.get(i));
//								}
//							}
//						}else{
//						    strs.add(processor.getFileName());
//						}

						if(processor.getFileNameList()!=null){
							ArrayList<String> strs1=processor.getFileNameList();
							if(strs1.size()>0){
								for(int i=0;i<strs1.size();i++){
									strs.add(strs1.get(i));
								}
							}
						}
						if(processor.getFileName()!=null){
							strs.add(processor.getFileName());
						}
						//strs.add(processor.getFileName());
					}
					FileUtil.zipFile(getLocalDir(), getLocalDir(), zipFileName, strs);
					String zipFilePath = FileUtil.buildFullFilePath(getLocalDir(), zipFileName);
					logger.error("start upload noti file to ftp>>"+zipFilePath+">"+getFtpPath());

					uploadFile(zipFilePath, getFtpPath());

					//更新busi_notification_file
//					pmhzService.updateNotificationFile(month,subjectId,focusCd,isAuto,buildDownloadUrl());
					if(!isAuto) {
						BgxzData bgxz = new BgxzData();
						bgxz.setAudTrm(audTrm);
						bgxz.setFilePath(buildDownloadUrl());
						bgxz.setFileType("auditPm");
						bgxz.setOperType("手动生成");
						bgxz.setCreateType("manual");
						bgxz.setSubjectId(subjectId);
						bgxz.setFocusCd(_focusCd);
						bgxz.setPrvdId(10000);
						bgxz.setLoginAccount(loginAccount);
						bgxz.setOperPerson(userName);
						bgxz.setCreateDatetime(new Date());
						bgxz.setFileName(zipFileName);

						//判断是否有初始化数据不完整的记录
						int count = bgxzService.updateInitReportLog(bgxz, "create");
						if (count == 0) {
							bgxzService.addReportLog(bgxz, "create");
						}
					}
					if(isAuto) {
						BgxzData bgxz = new BgxzData();
						bgxz.setAudTrm(audTrm);
						bgxz.setFilePath(buildDownloadUrl());
						bgxz.setFileType("auditPm");
						bgxz.setOperType("自动生成");
						bgxz.setCreateType("auto");
						bgxz.setSubjectId(subjectId);
						bgxz.setFocusCd(_focusCd);
						bgxz.setPrvdId(10000);
						bgxz.setLoginAccount("system");
						bgxz.setOperPerson("system");
						bgxz.setCreateDatetime(new Date());
						bgxz.setFileName(zipFileName);

						//判断是否有初始化数据不完整的记录
						int count = bgxzService.updateInitReportLog(bgxz, "create");
						if (count == 0) {
							bgxzService.addReportLog(bgxz, "create");
						}
					}
					return 0;//执行完毕
				} catch (Exception e) {
					pmhzService.updatePmhzStatusByTrmSubNew(4,audTrm, subjectId);
					error = true;
					throw e;
					//e.printStackTrace();
					//error = true;
					//return 1;//执行异常
				}
		}


		@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
		public int work1(){
			int a=0;
			List<String> alrGenAudTrm = new ArrayList<String>();
			try{
			List<Map<String, Object>> result=pmhzService.getFileGenRequstAudTrmNew(subjectId,focusNum,isAuto,month);
				if(!result.isEmpty()){
					for(Map<String, Object> each:result){
						String audTrm =(String) each.get("audTrm");
						Integer id =(Integer) each.get("id");
						if(isAuto){
							pmhzService.updatePmhzStatusNew(5, id);
							pmhzService.updatePmhzStatusByTrmSubNew(5,audTrm, subjectId);
						}
						else {
							pmhzService.updatePmhzStatusNew(6, id);
							pmhzService.updatePmhzStatusByTrmSubNew(6,audTrm, subjectId);
						}

						if(!alrGenAudTrm.contains(audTrm)){
							a=genFile(audTrm,focusCd,isAuto);
							if(!isAuto)pmhzService.addHandAndDownloadInfo(oprUser, audTrm, subjectId, "1",oprPrvd);
						}
							alrGenAudTrm.add(audTrm);

						if(isAuto){
							pmhzService.updatePmhzStatusNew(2, id);
							pmhzService.updatePmhzStatusByTrmSubNew(2,audTrm, subjectId);
						}
						else {
							pmhzService.updatePmhzStatusNew(3, id);
							pmhzService.updatePmhzStatusByTrmSubNew(3,audTrm, subjectId);
						}

					}
					return a;
				}else{
					return 2;//模型执行完成的省份的排名汇总已经生成完毕
				}
			}catch(Exception e){
				a = 1;//发生异常
				e.printStackTrace();
				logger.error(e.getMessage(),e);
				return a;
			}
		}



	public boolean hasError(){
		return error;
	}

	public List<AbstractNotiFileProcessor> getNotiFileGenProcessorList() {
		return notiFileGenProcessorList;
	}

	public void setNotiFileGenProcessorList(List<AbstractNotiFileProcessor> notiFileGenProcessorList) {
		this.notiFileGenProcessorList = notiFileGenProcessorList;
	}

	public String getFocusCd() {
		return focusCd;
	}

	public void setFocusCd(String focusCd) {
		this.focusCd = focusCd;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Map<String, Object> getNotiFile() {
		return notiFile;
	}

	public void setNotiFile(Map<String, Object> notiFile) {
		this.notiFile = notiFile;
	}

	public String getLocalDir() {
		String tempDir = propertyUtil.getPropValue("tempDir");
		FileUtil.mkdirs(tempDir);
		return propertyUtil.getPropValue("tempDir");
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

	public void uploadFile(String filePath, String ftpPath){
		FtpUtil ftpTool = null;
		try {
			ftpTool = initFtp();
			if (ftpTool == null) {
				return;
			}
			ftpTool.uploadFile(new File(filePath), ftpPath);
		} catch (Exception e){
			e.printStackTrace();
			logger.error("NotiFile uploadFile>>>"+e.getMessage(),e);
		} finally {
			if (ftpTool != null) {
				ftpTool.disConnect();
			}
		}
	}

//	public String getFtpFileName() {
//		return "审计通报_" + month +".zip";
//	}

	public String getFtpPath(){
		String tempDir = propertyUtil.getPropValue("ftpPath");
		String finalPath = FileUtil.buildFullFilePath(tempDir, buildRelativePath(month, focusCd));
		FileUtil.mkdirs(finalPath);
		return finalPath;// + "/" + zipFileName;
	}

	protected String buildRelativePath(String audTrm, String focusCd) {
		String subjectId = focusCd.substring(0,1);
		if("1100".equals(focusCd))subjectId="11";
		StringBuilder path = new StringBuilder();
		path.append(month).append("/").append(subjectId).append("/").append(focusCd);
		return path.toString();
	}

	protected String buildDownloadUrl() {
		String ftpHttpUrlPrefix = propertyUtil.getPropValue("ftpHttpUrlPrefix");
		StringBuilder url = new StringBuilder(30);
		url.append(buildRelativePath(month, focusCd)).append("/").append(zipFileName);
		return FileUtil.buildFullFilePath(ftpHttpUrlPrefix, url.toString());
	}


	/**
	 * @return the subjectId
	 */
	public String getSubjectId() {
		return this.subjectId;
	}


	/**
	 * @param subjectId the subjectId to set
	 */
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}


	/**
	 * @return the focusNum
	 */
	public String getFocusNum() {
		return this.focusNum;
	}


	/**
	 * @param focusNum the focusNum to set
	 */
	public void setFocusNum(String focusNum) {
		this.focusNum = focusNum;
	}


	/**
	 * @return the isAuto
	 */
	public Boolean getIsAuto() {
		return this.isAuto;
	}


	/**
	 * @param isAuto the isAuto to set
	 */
	public void setIsAuto(Boolean isAuto) {
		this.isAuto = isAuto;
	}


	/**
	 * @return the pmhzService
	 */
	public NotiFileGenService getPmhzService() {
		return this.pmhzService;
	}


	/**
	 * @param pmhzService the pmhzService to set
	 */
	public void setPmhzService(NotiFileGenService pmhzService) {
		this.pmhzService = pmhzService;
	}


	public String getOprUser() {
		return this.oprUser;
	}


	public void setOprUser(String oprUser) {
		this.oprUser = oprUser;
	}


	public String getOprPrvd() {
		return this.oprPrvd;
	}


	public void setOprPrvd(String oprPrvd) {
		this.oprPrvd = oprPrvd;
	}

	public String getLoginAccount() {
		return this.loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}


}
