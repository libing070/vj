package com.hpe.cmca.controller;

import com.asiainfo.biframe.privilege.IUser;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.job.*;
import com.hpe.cmca.pojo.BgxzData;
import com.hpe.cmca.service.BgxzService;
import com.hpe.cmca.service.NotiFileGenService;
import com.hpe.cmca.util.ExceptionTool;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.Json;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 *
 * <pre>
 * Desc：
 * @author sinly
 * @refactor sinly
 * @date   2017年1月20日 下午4:50:09
 * @version 1.0
 * @see
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  2017年1月20日 	   sinly 	         1. Created this class.
 * </pre>
 */
@Controller
@RequestMapping("/pmhz")
public class PMHZController extends BaseController {
	protected Logger logger = Logger.getLogger(this.getClass());

	@Autowired
    private MybatisDao mybatisDao;

	@Autowired
	public NotiFileGenService pmhzService =null;

	@Autowired
	public BgxzService bgxzService =null;

    @Resource(name = "NotiFileGenJob")
    NotiFileGenJob notiFileGenJobAll;

    @Resource
    NotiFileGenJob notiFile7000GenJob;

    @Resource
    NotiFileGenJob notiFile9999GenJob;

    @Resource
    NotiFileZdtl3000Processor NotiFileZdtl3000Processor;

    @Resource
    NotiFileZdtl3000TolProcessor NotiFileZdtl3000TolProcessor;

    @Resource
    NotiFileZdtl3000AllPmhzProcessor NotiFileZdtl3000AllPmhzProcessor;
    @Resource
    NotiFileQdyk2000AllChnlsProcessor NotiFileQdyk2000AllChnlsProcessor;

    @Resource
    NotiFileQdyk2000TolProcessor NotiFileQdyk2000TolProcessor;

    @Resource
    NotiFileQdyk2000OSheHuiChnlsProcessor NotiFileQdyk2000OSheHuiChnlsProcessor;

    @Resource
    NotiFileQdyk2000ZiYouChnlsProcessor NotiFileQdyk2000ZiYouChnlsProcessor;

    @Resource
    NotiFileQdyk2000QiTaChnlsProcessor NotiFileQdyk2000QiTaChnlsProcessor;
    @Resource
    NotiFileQdyk2000pmhzProcessor    NotiFileQdyk2000pmhzProcessor;

    @Resource
    NotiFileYjk1000SumProcessor NotiFileYjk1000SumProcessor;
    @Resource
    NotiFileYjk1000RankProcessor NotiFileYjk1000RankProcessor;
    @Resource
    NotiFileYjk1000SumIllegalProcessor NotiFileYjk1000SumIllegalProcessor;
    @Resource
    NotiFileyjk1000pmhzProcessor NotiFileyjk1000pmhzProcessor;

    @Resource
    NotiFileKhqf4000PmhzProcessor NotiFileKhqf4000PmhzProcessor;
    @Resource
    NotiFileKhqf4000SegmProcessor NotiFileKhqf4000SegmProcessor;
    @Resource
    NotiFileKhqf4000Top100Processor NotiFileKhqf4000Top100Processor;
    @Resource
    NotiFileKhqf4000AllPmhzProcessor NotiFileKhqf4000AllPmhzProcessor;

    @Resource
    NotiFileYCCZ5000SFXZProcessor NotiFileYCCZ5000SFXZProcessor;
    @Resource
    NotiFileYCCZ5000YCHFZSProcessor NotiFileYCCZ5000YCHFZSProcessor;
    @Resource
    NotiFileYCCZ5000YCJFZSProcessor NotiFileYCCZ5000YCJFZSProcessor;
    @Resource
    NotiFileYCCZ5000YCJFZYProcessor NotiFileYCCZ5000YCJFZYProcessor;
    @Resource
    NotiFileYCCZ5000YCTFProcessor NotiFileYCCZ5000YCTFProcessor;
    @Resource
    NotiFileYCCZ5000YCHFZSDetailProcessor  NotiFileYCCZ5000YCHFZSDetailProcessor;
    @Resource
    NotiFileYCCZ5000YCJFZSDetailProcessor  NotiFileYCCZ5000YCJFZSDetailProcessor;
    @Resource
    NotiFileYCCZ5000YCJFZYDetailProcessor  NotiFileYCCZ5000YCJFZYDetailProcessor;
    @Resource
    NotiFileYCCZ5000YCTFDetailProcessor   NotiFileYCCZ5000YCTFDetailProcessor;

    @Resource
    NotiFileLlzyOrg7000PmhzProcessor NotiFileLlzyOrg7000PmhzProcessor;
    @Resource
    NotiFileLlzs8000PrvdOfferProcess NotiFileLlzs8000PrvdOfferProcess;
    @Resource
    NotiFileLlzs8000PrvdChnlProcess NotiFileLlzs8000PrvdChnlProcess;
    @Resource
    NotiFileLlzs8000PmhzProcessor NotiFileLlzs8000PmhzProcessor;
    @Resource
    NotiFileAutoProcessor NotiFile0000PmhzProcessor;
    @Resource
    NotiFileDzq6000PrvdChnlProcessor NotiFileDzq6000PrvdChnlProcessor;
    @Resource
    NotiFileDzq6000PrvdOfferProcessor NotiFileDzq6000PrvdOfferProcessor;
	@Resource
	NotiFileDzq6000PrvdRemitProcessor NotiFileDzq6000PrvdRemitProcessor;

	@Resource
	NotiFileKsck1100T1Sh2Processor NotiFileKsck1100T1Sh2Processor;

	@Resource
	NotiFileKsck1100T2Sh2Processor NotiFileKsck1100T2Sh2Processor;
	@Resource
	NotiFileLlzs7000PrvdProcessor NotiFileLlzs7000PrvdProcessor;

    private String oprUser="unknown";
    private String oprPrvd= "unknown";

  //  @Autowired
   // NotiFileGenService pmhzService;
	/**
	 *
	 * <pre>
	 * Desc  排名汇总首页
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   Oct 26, 2016 3:19:33 PM
	 * </pre>
	 */
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjpmhz })
	@RequestMapping(value = "index")
	public String index(HttpServletResponse response,	HttpServletRequest request) {

		HttpSession session = request.getSession();
    	IUser user = (IUser) session.getAttribute("ssoUSER");
    	if (user == null) {
    		oprUser = "unknown";
    		oprPrvd = "unknown";
		} else {
			oprUser = user.getUsername();
			oprPrvd = (String) session.getAttribute("userPrvdId");
		}

		return "pmhz/index";
	}

    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjpmhz })
	@RequestMapping(value = "genPmhz")
	public void genPmhz() throws Exception{
//		pmhzProcessor.setSubjectId("3");
//		pmhzProcessor.setFocusNum("4");
//		pmhzProcessor.setIsAuto(false);
//
//		pmhzProcessor.start();
	}
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjpmhz })
	@ResponseBody
	@RequestMapping(value = "getPmhzGenInfo", produces = "plain/text; charset=UTF-8")
	public String getPmhzGenInfo(String audTrm,String subjectId){

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		List<Map<String, Object>> list=mybatisDao.getList("pmhz.getPmhzGenInfo", params);

		return  Json.Encode(list);

	}
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjpmhz })
	@ResponseBody
	@RequestMapping(value = "checkPrvdCount", produces = "plain/text; charset=UTF-8")
	public String checkPrvdCount(String audTrm,String subjectId){

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		List<Map<String, Object>> list=mybatisDao.getList("pmhz.checkPrvdCount", params);

		return  Json.Encode(list);

	}
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjpmhz })
	@ResponseBody
	@RequestMapping(value = "checkIsProc", produces = "plain/text; charset=UTF-8")
	public String checkIsProc(String audTrm,String subjectId){

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		List<Map<String, Object>> list=mybatisDao.getList("pmhz.checkIsProc", params);

		return  Json.Encode(list);

	}
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjpmhz })
	@ResponseBody
	@RequestMapping(value = "getDownloadTimes", produces = "plain/text; charset=UTF-8")
	public String getDownloadTimes(String audTrm,String subjectId){

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		List<Map<String, Object>> list=mybatisDao.getList("pmhz.getDownloadTimes", params);

		return  Json.Encode(list);

	}

    /**
     * 手动下载排名汇总
     * <pre>
     * Desc
     * @param response
     * @param request
     * @param download_url
     * @param subjectId
     * @param focusCd
     * @param audTrm
     * @param prvdId
     * @param operType
     * @param userName
     * @throws IOException
     * @author issuser
     * 2017-12-22 下午4:47:20
     * </pre>
     */
    @RequestMapping(value = "downPMHZ", produces = "plain/text; charset=UTF-8")
    public void downPMHZ(HttpServletResponse response,	HttpServletRequest request,String download_url,
    		String subjectId,String focusCd,String audTrm,String prvdId,String fileType) {
		download_url=download_url.replace("#","%23");
    	String dlpath = download_url;
    	String opertype=null;
		if(fileType.equals("audReport")){
			opertype ="审计报告下载";
		}else if(fileType.equals("audDetail")){
			opertype ="审计清单下载";
		}else if(fileType.equals("auditPm")){
			opertype ="排名汇总下载";
		}else if(fileType.equals("auditTB")){
			opertype ="审计通报下载";
		}
		IUser user =(IUser)request.getSession().getAttribute("ssoUSER");
		String userName= (String) request.getSession().getAttribute("userName");
		//记录下载日志
		BgxzData bgxz =new BgxzData();
		bgxz.setAudTrm(audTrm);
		bgxz.setSubjectId(subjectId);
		bgxz.setFocusCd(focusCd);
		bgxz.setPrvdId(Integer.parseInt(prvdId));
		bgxz.setOperType(opertype);
		bgxz.setOperPerson(userName);
		bgxz.setFileType(fileType);
		bgxz.setCreateType("down");
		bgxz.setLoginAccount(user.getUserid());
		bgxz.setDownCount(1);
		bgxz.setDownDatetime(new Date());
		bgxz.setFilePath(download_url);
		bgxz.setFileName(download_url.substring(download_url.lastIndexOf("/")+1));
		//判断是否有初始化数据不完整的记录
		int count =bgxzService.updateInitReportLog(bgxz,"down");
		if(count ==0){
			bgxzService.addReportLog(bgxz,"down");
		}
		logger.debug("######  当前下载排名汇总URL:" + dlpath);
		String fileName = dlpath.substring(dlpath.lastIndexOf("/") + 1, dlpath.length());
		logger.debug("######  当前下载排名汇总文件名:" + fileName);

		FileUtil.downFileByHttp(request,response, dlpath, fileName, logger);


    }

    @RequestMapping(value = "downFilePage", produces = "plain/text; charset=UTF-8")
    public void downFilePage(HttpServletResponse response,	HttpServletRequest request,
    		String subjectId,String focusCd,String audTrm,String prvdId,String fileType) {

    	IUser user =(IUser)request.getSession().getAttribute("ssoUSER");
		String userName= (String) request.getSession().getAttribute("userName");
    	String opertype=null;
		if(fileType.equals("audReport")){
			opertype ="审计报告下载";
		}else if(fileType.equals("audDetail")){
			opertype ="审计清单下载";
		}else if(fileType.equals("auditPm")){
			opertype ="排名汇总下载";
		}else if(fileType.equals("auditTB")){
			opertype ="审计通报下载";
		}
		if(focusCd ==null ||focusCd.equals(""))
			focusCd =subjectId+"000";
		//记录下载日志
		BgxzData bgxz =new BgxzData();
		bgxz.setAudTrm(audTrm);
		bgxz.setSubjectId(subjectId);
		bgxz.setFocusCd(focusCd);
		bgxz.setPrvdId(Integer.parseInt(prvdId));
		bgxz.setOperType(opertype);
		bgxz.setOperPerson(userName);
		bgxz.setFileType(fileType);
		bgxz.setCreateType("down");
		bgxz.setLoginAccount(user.getUserid());
		bgxz.setDownCount(1);
		bgxz.setDownDatetime(new Date());
		List<Map<String,Object>> fileList =bgxzService.selReportLog(bgxz);
		String dlpath = null;
		if(fileList.size() > 0){
			Map<String,Object> f =fileList.get(0);
			dlpath =f.get("file_path")==null?"":String.valueOf(f.get("file_path"));
		}
		if(dlpath ==null ||dlpath.equals("")){
			try {
				response.getWriter().print("empty");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error(e1.getMessage(), e1);
			}

		}else{
			//判断是否有初始化数据不完整的记录
			int count =bgxzService.updateInitReportLog(bgxz,"down");
			if(count ==0){
				bgxzService.addReportLog(bgxz,"down");
			}
			logger.debug("######  当前下载排名汇总URL:" + dlpath);
			String fileName = dlpath.substring(dlpath.lastIndexOf("/") + 1, dlpath.length());
			logger.debug("######  当前下载排名汇总文件名:" + fileName);

			FileUtil.downFileByHttp(request,response, dlpath, fileName, logger);
		}
    }



	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjpmhz })
	@RequestMapping(value = "downLoadPmhz", produces = "plain/text; charset=UTF-8")
	public void downLoadPmhz(HttpServletResponse response,	HttpServletRequest request, String audTrm,String subjectId,
			String focusCd,String prvdId,String operType,String userName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		logger.debug("######  开始获取排名汇总下载路径:" + params);
		List<Map<String, Object>> list = mybatisDao.getList("pmhz.getPmhzGenInfo", params);
		//Map<String, Object> downLoadFile = (0 == list.size()) ? new HashMap<String, Object>() : list.get(0);
			// 文件是存放在一个支持'ftp上传|http下载'的ftp服务器
		if (0 == list.size()) {
			logger.debug("######  从数据库中未查询到要下载的文件记录:" + params);
		} else {
			try {
				Map<String, Object> downLoadFile = list.get(0);
				// 文件是存放在一个支持'ftp上传|http下载'的ftp服务器
				String dlpath = (String) downLoadFile.get("download_url");
				logger.debug("######  当前下载排名汇总URL:" + dlpath);
				String fileName = dlpath.substring(dlpath.lastIndexOf("/") + 1, dlpath.length());
				logger.debug("######  当前下载排名汇总文件名:" + fileName);

				FileUtil.downFileByHttp(request,response, dlpath, fileName, logger);

//				Integer id = (Integer) downLoadFile.get("id");
//				params.put("id", id);
//				mybatisDao.update("pmhz.incNotiFileDLNumById", params);
//				pmhzService.addHandAndDownloadInfo(oprUser, audTrm, subjectId, "2",oprPrvd);
			} catch (Exception e) {
				logger.error("######  下载异常排名汇总文件:" + ExceptionTool.getExceptionDescription(e));
			}
		}
		logger.debug("######  完成下载的排名汇总:" + params);
	}
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjpmhz })
	@ResponseBody
	@RequestMapping(value = "updatePmhzInfo", produces = "plain/text; charset=UTF-8")
	public String updatePmhzInfo(String audTrm,String subjectId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		logger.debug("######  开始获取需要更新的排名汇总下载路径:" + params);
		Map<String,String> tp = new HashMap<String,String>();
		tp.put("status", "DONE");

		List<Map<String, Object>> list = mybatisDao.getList("pmhz.getPmhzGenInfo", params);
		//Map<String, Object> downLoadFile = (0 == list.size()) ? new HashMap<String, Object>() : list.get(0);
			// 文件是存放在一个支持'ftp上传|http下载'的ftp服务器
		if (0 == list.size()) {
			logger.debug("######  从数据库中未查询到要更新的文件记录:" + params);
		} else {
			try {
				Map<String, Object> downLoadFile = list.get(0);
				// 文件是存放在一个支持'ftp上传|http下载'的ftp服务器
				Integer id = (Integer) downLoadFile.get("id");
				params.put("id", id);
				mybatisDao.update("pmhz.incNotiFileDLNumById", params);
				pmhzService.addHandAndDownloadInfo(oprUser, audTrm, subjectId, "2",oprPrvd);
			} catch (Exception e) {
				tp.put("status", "FAILED");
				logger.error("######  更新异常排名汇总文件:" + ExceptionTool.getExceptionDescription(e));
			}
		}
		logger.debug("######  完成更新的排名汇总:" + params);
		return Json.Encode(tp);
	}
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjpmhz })
	@ResponseBody
	@RequestMapping(value = "genByHand")
	public String   genByHand(HttpServletRequest request,String audTrm,String subjectId,
			String focusCd,String focusNum,Boolean isAuto,String prvdId,String concern){
		//NotiFileGenJob notiFileGenJobAll =new NotiFileGenJob();

    	IUser user =(IUser)request.getSession().getAttribute("ssoUSER");
		int a=0;
		if(subjectId.equals("9")){
			List<AbstractNotiFileProcessor> list=new ArrayList<AbstractNotiFileProcessor>();
//			list.add(NotiFileLlzs8000PmhzProcessor);
			list.add(NotiFile0000PmhzProcessor);
			notiFile9999GenJob.setNotiFileGenProcessorList(list);
			notiFile9999GenJob.setSubjectId(subjectId);
			notiFile9999GenJob.setFocusNum(focusNum);
			notiFile9999GenJob.setIsAuto(isAuto);
			notiFile9999GenJob.setFocusCd(focusCd);
			notiFile9999GenJob.setOprPrvd(oprPrvd);
			notiFile9999GenJob.setOprUser(oprUser);
			notiFile9999GenJob.setMonth(audTrm);
			notiFile9999GenJob.setLoginAccount(user.getUserid());
			notiFile9999GenJob.setUserName(user.getUsername());
			a =notiFile9999GenJob.work1();
		}
		if (subjectId.equals("13")) {
			List<AbstractNotiFileProcessor> list = new ArrayList<AbstractNotiFileProcessor>();
			list.add(NotiFile0000PmhzProcessor);
			notiFileGenJobAll.setNotiFileGenProcessorList(list);
			notiFileGenJobAll.setSubjectId(subjectId);
			notiFileGenJobAll.setFocusNum(focusNum);
			notiFileGenJobAll.setIsAuto(isAuto);
			notiFileGenJobAll.setFocusCd(focusCd);
			notiFileGenJobAll.setOprPrvd(oprPrvd);
			notiFileGenJobAll.setOprUser(oprUser);
			notiFileGenJobAll.setMonth(audTrm);
			notiFileGenJobAll.setLoginAccount(user.getUserid());
			notiFileGenJobAll.setUserName(user.getUsername());
			a = notiFileGenJobAll.work1();
		}
		if (subjectId.equals("12")) {
			List<AbstractNotiFileProcessor> list = new ArrayList<AbstractNotiFileProcessor>();
			list.add(NotiFile0000PmhzProcessor);
			notiFileGenJobAll.setNotiFileGenProcessorList(list);
			notiFileGenJobAll.setSubjectId(subjectId);
			notiFileGenJobAll.setFocusNum(focusNum);
			notiFileGenJobAll.setIsAuto(isAuto);
			notiFileGenJobAll.setFocusCd(focusCd);
			notiFileGenJobAll.setOprPrvd(oprPrvd);
			notiFileGenJobAll.setOprUser(oprUser);
			notiFileGenJobAll.setMonth(audTrm);
			notiFileGenJobAll.setLoginAccount(user.getUserid());
			notiFileGenJobAll.setUserName(user.getUsername());
			a = notiFileGenJobAll.work1();
		}
		if(subjectId.equals("11")){
			List<AbstractNotiFileProcessor> list=new ArrayList<AbstractNotiFileProcessor>();
			list.add(NotiFile0000PmhzProcessor);
			list.add(NotiFileKsck1100T1Sh2Processor);
			list.add(NotiFileKsck1100T2Sh2Processor);
			notiFileGenJobAll.setNotiFileGenProcessorList(list);
			notiFileGenJobAll.setSubjectId(subjectId);
			notiFileGenJobAll.setFocusNum(focusNum);
			notiFileGenJobAll.setIsAuto(isAuto);
			notiFileGenJobAll.setFocusCd(focusCd);
			notiFileGenJobAll.setOprPrvd(oprPrvd);
			notiFileGenJobAll.setOprUser(oprUser);
			notiFileGenJobAll.setMonth(audTrm);
			notiFileGenJobAll.setLoginAccount(user.getUserid());
			notiFileGenJobAll.setUserName(user.getUsername());
			a =notiFileGenJobAll.work1();
		}
		if(subjectId.equals("7")){
			List<AbstractNotiFileProcessor> list=new ArrayList<AbstractNotiFileProcessor>();
//			list.add(NotiFileLlzyOrg7000PmhzProcessor);
			list.add(NotiFileLlzs8000PrvdChnlProcess);
			list.add(NotiFileLlzs8000PrvdOfferProcess);
			list.add(NotiFile0000PmhzProcessor);
			list.add(NotiFileLlzs7000PrvdProcessor);
			notiFileGenJobAll.setNotiFileGenProcessorList(list);
			notiFileGenJobAll.setSubjectId(subjectId);
			notiFileGenJobAll.setFocusNum(focusNum);
			notiFileGenJobAll.setIsAuto(isAuto);
			notiFileGenJobAll.setFocusCd(focusCd);
			notiFileGenJobAll.setOprPrvd(oprPrvd);
			notiFileGenJobAll.setOprUser(oprUser);
			notiFileGenJobAll.setMonth(audTrm);
			notiFileGenJobAll.setLoginAccount(user.getUserid());
			notiFileGenJobAll.setUserName(user.getUsername());
			a =notiFileGenJobAll.work1();
		}
		if(subjectId.equals("6")){
			List<AbstractNotiFileProcessor> list=new ArrayList<AbstractNotiFileProcessor>();
			list.add(NotiFileDzq6000PrvdChnlProcessor);
			list.add(NotiFileDzq6000PrvdOfferProcessor);
			list.add(NotiFileDzq6000PrvdRemitProcessor);
			list.add(NotiFile0000PmhzProcessor);
			notiFileGenJobAll.setNotiFileGenProcessorList(list);
			notiFileGenJobAll.setSubjectId(subjectId);
			notiFileGenJobAll.setFocusNum(focusNum);
			notiFileGenJobAll.setIsAuto(isAuto);
			notiFileGenJobAll.setFocusCd(focusCd);
			notiFileGenJobAll.setOprPrvd(oprPrvd);
			notiFileGenJobAll.setOprUser(oprUser);
			notiFileGenJobAll.setMonth(audTrm);
			notiFileGenJobAll.setLoginAccount(user.getUserid());
			notiFileGenJobAll.setUserName(user.getUsername());
			a =notiFileGenJobAll.work1();
		}
		if(subjectId.equals("5")){
			List<AbstractNotiFileProcessor> list=new ArrayList<AbstractNotiFileProcessor>();
//			if("5000".equals(concern)){
//				list.add(NotiFileYCCZ5000SFXZProcessor);
//				list.add(NotiFileYCCZ5000YCTFProcessor);
//			}
//			if("5001".equals(concern)){
//				list.add(NotiFileYCCZ5000YCJFZSProcessor);
//			}
//			if("5002".equals(concern)){
//				list.add(NotiFileYCCZ5000YCJFZYProcessor);
//			}
//			if("5003".equals(concern)){
//				list.add(NotiFileYCCZ5000YCHFZSProcessor);
//			}
//			focusCd =concern;
			list.add(NotiFileYCCZ5000SFXZProcessor);
			list.add(NotiFileYCCZ5000YCHFZSDetailProcessor);
			list.add(NotiFileYCCZ5000YCJFZSDetailProcessor);
			list.add(NotiFileYCCZ5000YCJFZYDetailProcessor);
			list.add(NotiFileYCCZ5000YCTFDetailProcessor);
			list.add(NotiFile0000PmhzProcessor);
			notiFileGenJobAll.setNotiFileGenProcessorList(list);
			//notiFileGenJobAll.setPmhzService(pmhzService);
			notiFileGenJobAll.setSubjectId(subjectId);
			notiFileGenJobAll.setFocusNum(focusNum);
			notiFileGenJobAll.setIsAuto(isAuto);
			notiFileGenJobAll.setFocusCd(focusCd);
			notiFileGenJobAll.setOprPrvd(oprPrvd);
			notiFileGenJobAll.setOprUser(oprUser);
			notiFileGenJobAll.setMonth(audTrm);
			notiFileGenJobAll.setLoginAccount(user.getUserid());
			notiFileGenJobAll.setUserName(user.getUsername());
			a =notiFileGenJobAll.work1();
		}
		if(subjectId.equals("4")){
			List<AbstractNotiFileProcessor> list=new ArrayList<AbstractNotiFileProcessor>();
//			list.add(NotiFileKhqf4000PmhzProcessor);
//			list.add(NotiFileKhqf4000SegmProcessor);
//			list.add(NotiFileKhqf4000Top100Processor);
//			list.add(NotiFileKhqf4000AllPmhzProcessor);
			list.add(NotiFile0000PmhzProcessor);
			notiFileGenJobAll.setNotiFileGenProcessorList(list);
			//notiFileGenJobAll.setPmhzService(pmhzService);
			notiFileGenJobAll.setSubjectId(subjectId);
			notiFileGenJobAll.setFocusNum(focusNum);
			notiFileGenJobAll.setIsAuto(isAuto);
			notiFileGenJobAll.setFocusCd(focusCd);
			notiFileGenJobAll.setOprPrvd(oprPrvd);
			notiFileGenJobAll.setOprUser(oprUser);
			notiFileGenJobAll.setMonth(audTrm);
			notiFileGenJobAll.setLoginAccount(user.getUserid());
			notiFileGenJobAll.setUserName(user.getUsername());
			a =notiFileGenJobAll.work1();
		}

		if(subjectId.equals("3")){
			List<AbstractNotiFileProcessor> list=new ArrayList<AbstractNotiFileProcessor>();
			list.add(NotiFileZdtl3000Processor);
			list.add(NotiFileZdtl3000TolProcessor);
			list.add(NotiFileZdtl3000AllPmhzProcessor);
//			list.add(NotiFile0000PmhzProcessor);
			notiFileGenJobAll.setNotiFileGenProcessorList(list);
			//notiFileGenJobAll.setPmhzService(pmhzService);
			notiFileGenJobAll.setSubjectId(subjectId);
			notiFileGenJobAll.setFocusNum(focusNum);
			notiFileGenJobAll.setIsAuto(isAuto);
			notiFileGenJobAll.setFocusCd(focusCd);
			notiFileGenJobAll.setOprPrvd(oprPrvd);
			notiFileGenJobAll.setOprUser(oprUser);
			notiFileGenJobAll.setMonth(audTrm);
			notiFileGenJobAll.setLoginAccount(user.getUserid());
			notiFileGenJobAll.setUserName(user.getUsername());
			a =notiFileGenJobAll.work1();
		}

		if(subjectId.equals("2")){
			List<AbstractNotiFileProcessor> list=new ArrayList<AbstractNotiFileProcessor>();
			list.add(NotiFileQdyk2000AllChnlsProcessor);
			list.add(NotiFileQdyk2000OSheHuiChnlsProcessor);
			list.add(NotiFileQdyk2000ZiYouChnlsProcessor);
			list.add(NotiFileQdyk2000QiTaChnlsProcessor);
			list.add(NotiFileQdyk2000pmhzProcessor);
//			list.add(NotiFile0000PmhzProcessor);
			notiFileGenJobAll.setNotiFileGenProcessorList(list);
			//notiFileGenJobAll.setPmhzService(pmhzService);
			notiFileGenJobAll.setSubjectId(subjectId);
			notiFileGenJobAll.setFocusNum(focusNum);
			notiFileGenJobAll.setIsAuto(isAuto);
			notiFileGenJobAll.setFocusCd(focusCd);
			notiFileGenJobAll.setOprPrvd(oprPrvd);
			notiFileGenJobAll.setOprUser(oprUser);
			notiFileGenJobAll.setMonth(audTrm);
			notiFileGenJobAll.setLoginAccount(user.getUserid());
			notiFileGenJobAll.setUserName(user.getUsername());
			a =notiFileGenJobAll.work1();
		}

		if(subjectId.equals("1")){
			List<AbstractNotiFileProcessor> list=new ArrayList<AbstractNotiFileProcessor>();
//			list.add(NotiFileYjk1000SumProcessor);
//			list.add(NotiFileYjk1000SumIllegalProcessor);
//			list.add(NotiFileyjk1000pmhzProcessor);
			list.add(NotiFile0000PmhzProcessor);
			notiFileGenJobAll.setNotiFileGenProcessorList(list);
			//notiFileGenJobAll.setPmhzService(pmhzService);
			notiFileGenJobAll.setSubjectId(subjectId);
			notiFileGenJobAll.setFocusNum(focusNum);
			notiFileGenJobAll.setIsAuto(isAuto);
			notiFileGenJobAll.setFocusCd(focusCd);
			notiFileGenJobAll.setOprPrvd(oprPrvd);
			notiFileGenJobAll.setOprUser(oprUser);
			notiFileGenJobAll.setMonth(audTrm);
			notiFileGenJobAll.setLoginAccount(user.getUserid());
			notiFileGenJobAll.setUserName(user.getUsername());
			a =notiFileGenJobAll.work1();
		}

		Map<String,String> tp = new HashMap<String,String>();
		if(a==0)tp.put("status", "DONE");
		if(a==1)tp.put("status", "FAILED");
		if(a==2)tp.put("status", "HAVEDONE");
		return Json.Encode(tp);
	}

}
