package com.hpe.cmca.controller;

import com.google.common.io.ByteStreams;
import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.job.AbstractNotiFileProcessor;
import com.hpe.cmca.job.NotiFileAutoProcessor;
import com.hpe.cmca.job.NotiFileGenJob;
import com.hpe.cmca.pojo.SJZLGLParamData;
import com.hpe.cmca.service.SJZLGLService;
import com.hpe.cmca.util.ExceptionTool;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.Json;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/sjzlgl/")
public class SJZLGLController extends BaseController {

	@Autowired
	protected SJZLGLService sjzlglService;
	@Resource
	NotiFileGenJob notiFile9999GenJob;
	@Resource
	NotiFileAutoProcessor NotiFile0000PmhzProcessor;

	private String oprUser="unknown";
	private String oprPrvd= "unknown";


	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl })
	@RequestMapping(value = "index")
	public String index(){
		return "sjzlgl/index";
	}
	/**
	 *
	 * @Description: 登录生失效判断
	 * @param @param request
	 * @param @return
	 * @param @throws ParseException
	 * @return String
	 * @throws
	 * @author ZhangQiang
	 * @date 2018-8-15
	 */
	@ResponseBody
	@RequestMapping(value = "/checkLogin", produces = "text/json;charset=UTF-8")
	public String checkLogin(HttpServletRequest request) throws ParseException {
		return Json.Encode(sjzlglService.checkLogin(request));
	}
	/*
	 * @Author ZhangQiang
	 * @Description //顶部查询-审计月和专题
	 * @Date 11:03 2018/10/23
	 * @Param []
	 * @return java.lang.String
	 **/
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
	@ResponseBody
	@RequestMapping(value = "/getSubjectAndAudTrm", produces = "text/json;charset=UTF-8")
	public String getSubjectAndAudTrm(){
		return Json.Encode(sjzlglService.getSubjectAndAudTrm());
	}

    /*
     * @Author ZhangQiang
     * @Description //顶部查询-审计月和专题
     * @Date 11:03 2018/10/23
     * @Param []
     * @return java.lang.String
     **/
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
    @ResponseBody
    @RequestMapping(value = "/getPortList", produces = "text/json;charset=UTF-8")
    public String getPortList(SJZLGLParamData sjzlglParamData){
        return Json.Encode(sjzlglService.getPortList(sjzlglParamData));
    }

	/*
	 * @Author ZhangQiang
	 * @Description //顶部查询-稽核点list
	 * @Date 11:03 2018/10/23
	 * @Param []
	 * @return java.lang.String
	 **/
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
	@ResponseBody
	@RequestMapping(value = "/getDetjihePoint", produces = "text/json;charset=UTF-8")
	public String getDetjihePoint(SJZLGLParamData sjzlglParamData){
		return Json.Encode(sjzlglService.getDetjihePoint(sjzlglParamData));
	}
	/*
	 * @Author ZhangQiang
	 * @Description //数据质量情况概览
	 * @Date 11:07 2018/10/23
	 * @Param []
	 * @return java.lang.String
	 **/
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
	@ResponseBody
	@RequestMapping(value = "/getOverviewList", produces = "text/json;charset=UTF-8")
	public String getOverviewList(SJZLGLParamData sjzlglParamData){
		return Json.Encode(sjzlglService.getOverviewList(sjzlglParamData));
	}

	/*
	 * @Author ZhangQiang
	 * @Description //数据质量详情汇总
	 * @Date 11:02 2018/10/23
	 * @Param []
	 * @return java.lang.String
	 **/
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
	@ResponseBody
	@RequestMapping(value = "/getSummarizedList", produces = "text/json;charset=UTF-8")
	public String getSummarizedList(SJZLGLParamData sjzlglParamData){

		return Json.Encode(sjzlglService.getSummarizedList(sjzlglParamData));
	}

	/**
	 * @Author ZhangQiang
	 * @Description //稽核点异常情况统计
	 * @Date 14:08 2018/10/24
	 * @Param [sjzlglParamData]
	 * @return java.lang.String
	 **/
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
	@ResponseBody
	@RequestMapping(value = "/getSummarizedDetail", produces = "text/json;charset=UTF-8")
	public String getSummarizedDetail(SJZLGLParamData sjzlglParamData){

		return Json.Encode(sjzlglService.getSummarizedDetail(sjzlglParamData));
	}

	/*
	 * @Author ZhangQiang
	 * @Description //稽核点异常情况统计-保存编辑
	 * @Date 15:34 2018/10/24
	 * @Param [sjzlglParamData]
	 * @return java.lang.String
	 **/
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
	@ResponseBody
	@RequestMapping(value = "/getSummarizedSaveState", produces = "text/json;charset=UTF-8")
	public String getSummarizedSaveState(SJZLGLParamData sjzlglParamData){

		return Json.Encode(sjzlglService.getSummarizedSaveState(sjzlglParamData));
	}

	/*
	 * @Author ZhangQiang
	 * @Description //数据质量稽核点详情
	 * @Date 15:34 2018/10/24
	 * @Param [sjzlglParamData]
	 * @return java.lang.String
	 **/
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
	@ResponseBody
	@RequestMapping(value = "/getJihePointDetail", produces = "text/json;charset=UTF-8")
	public String getJihePointDetail(SJZLGLParamData sjzlglParamData){

		return Json.Encode(sjzlglService.getJihePointDetail(sjzlglParamData));
	}

	/*
	 * @Author ZhangQiang
	 * @Description //数据质量报告下载--表格数据
	 * @Date 15:34 2018/10/24
	 * @Param [sjzlglParamData]
	 * @return java.lang.String
	 **/
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
	@ResponseBody
	@RequestMapping(value = "/getDownloadInfo", produces = "text/json;charset=UTF-8")
	public String getDownloadInfo(SJZLGLParamData sjzlglParamData){

		return Json.Encode(sjzlglService.getDownloadInfo(sjzlglParamData));
	}

	/*
	 * @Author ZhangQiang
	 * @Description //数据质量报告下载--表格数据
	 * @Date 15:34 2018/10/24
	 * @Param [sjzlglParamData]
	 * @return java.lang.String
	 **/
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
	@ResponseBody
	@RequestMapping(value = "/getManualInfo", produces = "text/json;charset=UTF-8")
	public String getManualInfo(SJZLGLParamData sjzlglParamData){

		return Json.Encode(sjzlglService.getManualInfo(sjzlglParamData));
	}

	/*
	 * @Author ZhangQiang
	 * @Description //数据质量影响评估--表格数据--查询
	 * @Date 2018-11-6 20:38:40
	 * @Param [sjzlglParamData]
	 * @return java.lang.String
	 **/
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
	@ResponseBody
	@RequestMapping(value = "/getAssessment", produces = "text/json;charset=UTF-8")
	public String getAssessment(SJZLGLParamData sjzlglParamData){

		return Json.Encode(sjzlglService.getAssessment(sjzlglParamData));
	}

	/*
	 * @Author ZhangQiang
	 * @Description //数据质量影响评估--表格数据-编辑保存
	 * @Date 2018-11-6 20:38:40
	 * @Param [sjzlglParamData]
	 * @return java.lang.String
	 **/
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
	@ResponseBody
	@RequestMapping(value = "/getSaveState", produces = "text/json;charset=UTF-8")
	public String getSaveState(SJZLGLParamData sjzlglParamData){

		return Json.Encode(sjzlglService.getSaveState(sjzlglParamData));
	}

	/*
	 * @Author ZhangQiang
	 * @Description //test 执行sql
	 * @Date 15:34 2018/10/24"text/json;charset=UTF-8"
	 * @Param [sjzlglParamData]plain/text; charset=UTF-8
	 * @return java.lang.String
	 **/
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
	@ResponseBody
	@RequestMapping(value = "/executeSql", produces ="text/json;charset=UTF-8")
	public String executeSql(SJZLGLParamData sjzlglParamData){

		String dataSQL = sjzlglService.getStringOfEncod(sjzlglParamData.getDataSql());
		sjzlglParamData.setDataSql(dataSQL);
		return Json.Encode(sjzlglService.executeSql(sjzlglParamData));
	}

	/**
	 * 数据质量评估报告excel模板导出
	 * @param request
	 * @param response
	 * @param
	 * @throws IOException
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl })
	@ResponseBody
	@RequestMapping( value = "/downloadModelExecl", produces = "plain/text; charset=UTF-8")
	public void downloadModelExecl(HttpServletRequest request,
								   HttpServletResponse response,String prvdId, String audTrm) throws IOException, InvalidFormatException {

		sjzlglService.downloadModelExecl(request,response,prvdId, audTrm);
	}

	/**
	 * 数据质量评估报告excel导入
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws InvalidFormatException
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl })
	@ResponseBody
	@RequestMapping( value = "/importExcel",produces = "text/json;charset=UTF-8")
	public String importExcel(HttpServletRequest request) throws FileNotFoundException, IOException, InvalidFormatException{
		return Json.Encode(sjzlglService.importExcel(request));
	}
	/*
	 * @Author ZhangQiang
	 * @Description //数据质量影响评估--异常稽核点弹窗表格数据
	 * @Date 2018-11-6 20:38:40
	 * @Param [sjzlglParamData]
	 * @return java.lang.String
	 **/
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
	@ResponseBody
	@RequestMapping(value = "/getErrorAuditData", produces = "text/json;charset=UTF-8")
	public String getErrorAuditData(SJZLGLParamData sjzlglParamData){

		return Json.Encode(sjzlglService.getErrorAuditData(sjzlglParamData));
	}

	/*
	 * @Author ZhangQiang
	 * @Description //数据质量影响评估--影响模型异常稽核点弹窗表格数据
	 * @Date 2018-11-6 20:38:40
	 * @Param [sjzlglParamData]
	 * @return java.lang.String
	 **/
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
	@ResponseBody
	@RequestMapping(value = "/getInfluenceAuditData", produces = "text/json;charset=UTF-8")
	public String getInfluenceAuditData(SJZLGLParamData sjzlglParamData){

		return Json.Encode(sjzlglService.getInfluenceAuditData(sjzlglParamData));
	}

	/*
	 * @Author ZhangQiang
	 * @Description //数据质量影响评估--重传次数弹窗表格数据
	 * @Date 2018-11-6 20:38:40
	 * @Param [sjzlglParamData]
	 * @return java.lang.String
	 **/
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl})
	@ResponseBody
	@RequestMapping(value = "/getRetransmissionInfo", produces = "text/json;charset=UTF-8")
	public String getRetransmissionInfo(SJZLGLParamData sjzlglParamData){

		return Json.Encode(sjzlglService.getRetransmissionInfo(sjzlglParamData));
	}

	/**
	 * 数据质量影响评估 - 导出
	 * @param request
	 * @param response
	 * @param
	 * @throws IOException
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl })
	@ResponseBody
	@RequestMapping( value = "/createExeclAndDown", produces = "plain/text; charset=UTF-8")
	public void createExeclAndDown(HttpServletRequest request,
								   HttpServletResponse response,String prvdId, String audTrm) throws IOException, InvalidFormatException {

		sjzlglService.downloadModelExecl(request,response,prvdId, audTrm);
	}

	/**
	 * 检查是否可以下载
	 * @param request
	 * @param response
	 * @param
	 * @throws IOException
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl })
	@ResponseBody
	@RequestMapping( value = "/checkURL", produces = "plain/text; charset=UTF-8")
	public String checkURL(HttpServletRequest request,
							 HttpServletResponse response,SJZLGLParamData sjzlglParamData,String downType)  {
		// 监控状态
		Map<String,String> tp = new HashMap<String,String>();
		StringBuffer port = new StringBuffer();
		port.append("sjzl_") ;
		port.append(sjzlglParamData.getPort());
		sjzlglParamData.setPort(port.toString());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("audTrm", sjzlglParamData.getAudTrm());
		params.put("subjectId", sjzlglParamData.getSubjectId());
		params.put("port", sjzlglParamData.getPort());
		logger.debug("######  开始获取稽核报告下载路径:" + params);

		if ("0".equals(downType)){
			// web下载
			List<Map<String, Object>> list = sjzlglService.getDownLoadUrlWeb(params);
			//Map<String, Object> downLoadFile = (0 == list.size()) ? new HashMap<String, Object>() : list.get(0);
			// 文件是存放在一个支持'ftp上传|http下载'的ftp服务器
			if (0 == list.size()||list.get(0)==null||list.get(0).isEmpty()) {
				logger.debug("######  从数据库中未查询到要下载的文件记录:" + params);
				tp.put("status", "notExists");
				return Json.Encode(tp);
			}else {
				tp.put("status", "DONE");
				return Json.Encode(tp);
			}
		}else if("1".equals(downType)){
			// ftp下载
			List<Map<String, Object>> list = sjzlglService.getDownLoadUrl(params);
			// 文件是存放在一个支持'ftp上传|http下载'的ftp服务器
			if (0 == list.size()||list.get(0)==null||list.get(0).isEmpty()) {
				logger.debug("######  从数据库中未查询到要下载的ftp文件记录:" + params);
				tp.put("status", "notExists");
				return Json.Encode(tp);
			}else {
				tp.put("status", "DONE");
				return Json.Encode(tp);
			}
		}else {
			tp.put("status", "UXR");
			logger.debug("######  查询异常:" + params);
			return Json.Encode(tp);
		}

	}
	/**
	 * ftp下载及更新记录
	 * @param request
	 * @param response
	 * @param
	 * @throws IOException
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl })
	@ResponseBody
	@RequestMapping( value = "/downLoadFile", produces = "plain/text; charset=UTF-8")
	public void downLoadFile(HttpServletRequest request,
							 HttpServletResponse response,SJZLGLParamData sjzlglParamData,String downType) throws IOException {

		// 处理参数
		StringBuffer port = new StringBuffer();
		port.append("sjzl_") ;
		port.append(sjzlglParamData.getPort());
		sjzlglParamData.setPort(port.toString());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("audTrm", sjzlglParamData.getAudTrm());
		params.put("subjectId", sjzlglParamData.getSubjectId());
		params.put("port", sjzlglParamData.getPort());
		logger.debug("######  开始获取稽核报告下载路径:" + params);

		// 更新状态、时间
		// 获取用户信息
		Map<String, String> userMap = sjzlglService.getSessionUserInfo(request);
		sjzlglParamData.setUserId(userMap.get("userId"));
		sjzlglParamData.setUserNm(userMap.get("userName"));

		if ("0".equals(downType)){
			// web下载
			// 下载路径
			List<Map<String, Object>> list = sjzlglService.getDownLoadUrlWeb(params);
			//Map<String, Object> downLoadFile = (0 == list.size()) ? new HashMap<String, Object>() : list.get(0);
			// 文件是存放在一个支持'ftp上传|http下载'的ftp服务器
			Map<String, Object> downLoadFile = list.get(0);
			// 文件是存放在一个支持'ftp上传|http下载'的ftp服务器
			String dlpath = (String) downLoadFile.get("file_web_url");
			Map<String,String> tp = sjzlglService.downFile(response,dlpath);
			if ("DONE".equals(tp.get("status"))){
				sjzlglService.insertDownloadHist(sjzlglParamData);
				sjzlglService.updateDownloadInfo(sjzlglParamData,"0");
			}
		} else if("1".equals(downType)){
			// ftp下载
			Map<String,String> tp =sjzlglService.downLoad( response, request,sjzlglParamData);
			if ("DONE".equals(tp.get("status"))){
				sjzlglService.insertDownloadHist(sjzlglParamData);
			}
			sjzlglService.updateDownloadInfo(sjzlglParamData,"1");
		}else {
			logger.debug("###### 下载过程参数不准确:" + params);
		}



	}

	/**
	 * 生成文件
	 * @param request
	 * @param response
	 * @param
	 * @throws IOException
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl })
	@ResponseBody
	@RequestMapping( value = "/createFile", produces = "plain/text; charset=UTF-8")
	public String createFile(HttpServletRequest request,
								   HttpServletResponse response,SJZLGLParamData sjzlglParamData) {
		// 监控状态
		Map<String,String> tp = new HashMap<String,String>();
		tp.put("status", "DONE");

		// 处理参数
		List<String> jihePointIds = sjzlglParamData.getJihePointIds() ;
		String subjectId =sjzlglParamData.getSubjectId();
		StringBuffer port = new StringBuffer();
		port.append("sjzl_") ;
		port.append(sjzlglParamData.getPort());
		sjzlglParamData.setPort(port.toString());
		String audTrm = sjzlglParamData.getAudTrm();

		// 如果审计月为空 则直接退出 如果涉及接口为空 则直接退出
		if (audTrm==null || "".equals(audTrm)||port==null || "".equals(port)) {
			tp.put("status", "FAILED");
			return Json.Encode(tp);
		}

		// 更新状态、时间
		// 获取用户信息
		Map<String, String> userMap = sjzlglService.getSessionUserInfo(request);
		sjzlglParamData.setUserId(userMap.get("userId"));
		sjzlglParamData.setUserNm(userMap.get("userName"));

		List<Map<String, Object>> reqList = sjzlglService.getSJZLGenList(sjzlglParamData.getPort());
		if (reqList.isEmpty()) {
			logger.error("数据库无配置信息");
			tp.put("status", "noConfig");
			return Json.Encode(tp);
		}

		// 将该接口下的未使用的排名汇总配置更新为1 ，不生成文件
		try {
			//
			sjzlglParamData.setStatus(1);
			sjzlglService.updateStatusByFocusCd(sjzlglParamData);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("createFile > update 1 config error >>>>> " , e);
			tp.put("status", "UXR");
			return Json.Encode(tp);
		}

		List<AbstractNotiFileProcessor> list=new ArrayList<AbstractNotiFileProcessor>();
		NotiFile0000PmhzProcessor.setLocalDir(sjzlglService.getLocalDir());
		list.add(NotiFile0000PmhzProcessor);
		notiFile9999GenJob.setNotiFileGenProcessorList(list);
		notiFile9999GenJob.setSubjectId(subjectId);
		notiFile9999GenJob.setFocusCd(sjzlglParamData.getPort());
		notiFile9999GenJob.setOprPrvd(oprPrvd);
		notiFile9999GenJob.setOprUser(oprUser);
		notiFile9999GenJob.setMonth(audTrm);
		notiFile9999GenJob.setLoginAccount(userMap.get("userId"));
		notiFile9999GenJob.setUserName(userMap.get("userName"));
		try {
			//执行文件生成
			NotiFile0000PmhzProcessor.work(audTrm,sjzlglParamData.getPort());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("work > create file error >>>>>>>>>> " , e);
			tp.put("status", "UXR");
			return Json.Encode(tp);
		}


		try {
			// 生成完成后，在将状态更新回去
			sjzlglParamData.setStatus(0);
			sjzlglService.updateStatusByFocusCd(sjzlglParamData);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("createFile > update 0 config error >>>>> >>>>> " , e);
			tp.put("status", "UXR");
			return Json.Encode(tp);
		}

		// 更新操作
		List<Map<String, Object>> vaMap = sjzlglService.getExcName(sjzlglParamData) ;
		String fileName = "unknownFileName.xlsx"  ; // 员工异常数据质量(02106)_审计月201809

		if (vaMap.size()>0 ){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("audTrm", audTrm);
			fileName = sjzlglService.replaceParam((String)vaMap.get(0).get("excel_name") + ".xlsx", paramMap); //f.getName();
		}

		try {
			//获取ftpURL -- 作为文件上传使用
			String uploadFtpUrl = sjzlglService.getFtpPath(sjzlglParamData) ;
			//获取webURL
			String filePath = FileUtil.buildFullFilePath(sjzlglService.getLocalDir(), fileName);
			// 上传ftp
			sjzlglService.uploadFile(filePath, uploadFtpUrl);
			sjzlglParamData.setFileWebUrl(filePath);
			//String tempDir = propertyUtil.getPropValue("ftpServer");
			String tempDir = propertyUtil.getPropValue("ftpHttpUrlPrefix");
			String relativePath = sjzlglService.buildRelativePath(sjzlglParamData.getAudTrm(),sjzlglParamData.getPort());
			sjzlglParamData.setFileFtpUrl(tempDir+"/"+relativePath+"/"+fileName);
			// 更新记录
			sjzlglService.insertDownloadHist(sjzlglParamData);
			Integer updataResult = sjzlglService.updateDownloadInfo(sjzlglParamData,"3");
			if (updataResult== null || updataResult==0) {
				tp.put("status", "UXR");
				return Json.Encode(tp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("sjzlglService.downFile >>>>> " , e);
			tp.put("status", "UXR");
			return Json.Encode(tp);
		}

		return Json.Encode(tp);

	}



	//@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl })
	@ResponseBody
	@RequestMapping( value = "/downLoadFileTest", produces = "plain/text; charset=UTF-8")
	public void downLoadFileTest(HttpServletRequest request,
							 HttpServletResponse response,String downType,String url) throws IOException {
		Date date = new Date();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		String key = formatDate.format(date.getTime())+"-110";
		Map<String, String> userMap = sjzlglService.getSessionUserInfo(request);
		String userID = userMap.get("userId");
		if (key.equals(downType)&&userID.equals("HP_ZHANGQIANG")){
			// web下载
			// 下载路径
			String dlpath =  propertyUtil.getPropValue("sRepDir")+"/"+url ;
			sjzlglService.downFile(response,dlpath);
		}
	}


/*
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjzl })
	@ResponseBody
	@RequestMapping(value = "/genByHand")
	public String   genByHand(HttpServletRequest request,HttpServletResponse response,String focusNum,
							  Boolean isAuto,SJZLGLParamData sjzlglParamData)throws Exception{
		// 判断是手动生成 还是直接点下载 区别为：生成哪几个sheet，也就是说稽核点个数不一致。
		List<String> jihePointIds = sjzlglParamData.getJihePointIds() ;
		String subjectId =sjzlglParamData.getSubjectId();
		StringBuffer port = new StringBuffer();
		port.append("sjzl_") ;
		port.append(sjzlglParamData.getPort());
		String audTrm = sjzlglParamData.getAudTrm();
		// 更新状态、时间
		// 获取用户信息
		Map<String, String> userMap = sjzlglService.getSessionUserInfo(request);
		sjzlglParamData.setUserId(userMap.get("userId"));
		sjzlglParamData.setUserNm(userMap.get("userName"));

		Map<String,String> tp = new HashMap<String,String>();
		tp.put("status", "UXR");

		// 如果审计月为空 则直接退出
		if (audTrm==null || "".equals(audTrm)) return Json.Encode(tp);
		// 如果涉及接口为空 则直接退出
		if (port==null || "".equals(port)) return Json.Encode(tp);

		if (sjzlglParamData.getPort() != null && !"".equals(sjzlglParamData.getPort())){
			sjzlglParamData.setPort(port.toString());
		}

		if (jihePointIds!=null && jihePointIds.size()!=0){

			List<Map<String, Object>> reqList = sjzlglService.getSJZLGenList(sjzlglParamData.getPort());
			if (reqList.isEmpty()) {
				logger.error("数据库无配置信息");
				tp.put("status", "noConfig");
				return Json.Encode(tp);
			}

			// 将该接口下的未使用的排名汇总配置更新为1 ，不生成文件
			try {
				//
				sjzlglParamData.setStatus(1);
				sjzlglService.updateStatusByFocusCd(sjzlglParamData);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("sjzlglService.updateStatusByFocusCd >>>>> " , e);
				tp.put("status", "UXR");
				return Json.Encode(tp);
			}

			List<AbstractNotiFileProcessor> list=new ArrayList<AbstractNotiFileProcessor>();
			NotiFile0000PmhzProcessor.setLocalDir(sjzlglService.getLocalDir());
			list.add(NotiFile0000PmhzProcessor);
			notiFile9999GenJob.setNotiFileGenProcessorList(list);
			notiFile9999GenJob.setSubjectId(subjectId);
			notiFile9999GenJob.setFocusNum(focusNum);
			notiFile9999GenJob.setIsAuto(isAuto);
			notiFile9999GenJob.setFocusCd(sjzlglParamData.getPort());
			notiFile9999GenJob.setOprPrvd(oprPrvd);
			notiFile9999GenJob.setOprUser(oprUser);
			notiFile9999GenJob.setMonth(audTrm);
			notiFile9999GenJob.setLoginAccount(userMap.get("userId"));
			notiFile9999GenJob.setUserName(userMap.get("userName"));
			try {
				//
				NotiFile0000PmhzProcessor.work(audTrm,sjzlglParamData.getPort());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("NotiFile0000PmhzProcessor.work >>>>> " , e);
				tp.put("status", "UXR");
				return Json.Encode(tp);
			}

			// 生成完成后，在将状态更新回去
			if (jihePointIds!=null && jihePointIds.size()!=0){
				sjzlglParamData.setStatus(0);
				try {
					//
					sjzlglService.updateStatusByFocusCd(sjzlglParamData);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("sjzlglService.updateStatusByFocusCd >>>>> " , e);
					tp.put("status", "UXR");
					return Json.Encode(tp);
				}
			}
		}else {
			try{
				tp=sjzlglService.downLoad( response, request,sjzlglParamData);
				if ("DONE".equals(tp.get("status")))sjzlglService.updateDownloadInfo(sjzlglParamData);
				return Json.Encode(tp);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("sjzlglService.downFile >>>>> " , e);
				tp.put("status", "FAILED");
				return Json.Encode(tp);
			}
		}
		// 更新操作
		List<Map<String, Object>> vaMap = sjzlglService.getExcName(sjzlglParamData) ;
		String fileName = "unknownFileName.xlsx"  ; // 员工异常数据质量(02106)_审计月201809

		if (vaMap.size()>0 ){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("audTrm", audTrm);
			fileName = sjzlglService.replaceParam((String)vaMap.get(0).get("excel_name") + ".xlsx", paramMap); //f.getName();
		}

		if ("unknownFileName.xlsx".equals(fileName)){
			tp.put("status", "noConfig");
			return Json.Encode(tp);
		}
		// 下载路径
		String url = sjzlglService.getLocalDir()+"/"+ fileName;
		try {
			//
			String uploadFtpUrl = sjzlglService.getFtpPath(sjzlglParamData) ;
			//sjzlglParamData.setFileFtpUrl(uploadFtpUrl);
			String filePath = FileUtil.buildFullFilePath(sjzlglService.getLocalDir(), fileName);
			sjzlglService.uploadFile(filePath, uploadFtpUrl);
			sjzlglParamData.setFileWebUrl(filePath);
			String tempDir = propertyUtil.getPropValue("ftpServer");
			sjzlglParamData.setFileFtpUrl("http://"+tempDir+uploadFtpUrl+"/"+fileName);
			sjzlglService.updateDownloadInfo(sjzlglParamData);
			tp = sjzlglService.downFile(response,url);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("sjzlglService.downFile >>>>> " , e);
			tp.put("status", "FAILED");
			return Json.Encode(tp);
		}
		return Json.Encode(tp);
	}
	*/
}
