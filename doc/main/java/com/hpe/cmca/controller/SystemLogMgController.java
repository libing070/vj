package com.hpe.cmca.controller;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.pojo.SystemLogHtmlData;
import com.hpe.cmca.service.SystemLogMgService;
import com.hpe.cmca.util.Json;

/**
 * 
 */
@Controller
@RequestMapping("/systemlogmg")
public class SystemLogMgController extends BaseController {

    @Autowired
    protected SystemLogMgService systemlogmgService;
    
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjrzcx})
    @RequestMapping(value = "index")
    public String index() {
    	
    	return "systemlogmg/index";
    }
    
    /**
     * 
     * @Description: 操作审计报告、审计清单、排名汇总的日志： 查询界面默认的筛选条件list 
     * @param @return   
     * @return String  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-11
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjrzcx})
    @ResponseBody
    @RequestMapping(value = "/queryModelTermData", produces = "text/json;charset=UTF-8")
    public String queryModelTermData(){

    	return Json.Encode(systemlogmgService.queryModelTermData());
    }
	
	/**
	 * 
	 * @Description: 操作审计报告、审计清单、排名汇总的日志： 查询界面操作日志表格内容
	 * @param @param behavTimeSd
	 * @param @param behavTimeEd
	 * @param @param subjectIds
	 * @param @param audTrms
	 * @param @param fileTyps
	 * @param @param filePrvdIds
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author ZhangQiang
	 * @date 2018-8-11
	 */
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjrzcx})
    @ResponseBody
    @RequestMapping(value = "/queryLogModelTbData", produces = "text/json;charset=UTF-8")
    public String queryLogModelTbData(String behavTimeSd,String behavTimeEd ,String subjectIds ,String audTrms ,
		String fileTyps ,String filePrvdIds){
	
    	SystemLogHtmlData systemLogHtmlsData = new SystemLogHtmlData();
    	
    	if("".equals(subjectIds) ||  subjectIds == null )systemLogHtmlsData.setSubjectIds(null);else systemLogHtmlsData.setSubjectIds(Arrays.asList(subjectIds.split(",")));
		if("".equals(audTrms) ||  audTrms == null )systemLogHtmlsData.setAudTrms(null);else systemLogHtmlsData.setAudTrms(Arrays.asList(audTrms.split(",")));
		if("".equals(fileTyps) ||  fileTyps == null )systemLogHtmlsData.setFileTyps(null);else systemLogHtmlsData.setFileTyps(Arrays.asList(fileTyps.split(",")));
		if("".equals(behavTimeSd) ||  behavTimeSd == null )systemLogHtmlsData.setBehavTimeSd(null);else systemLogHtmlsData.setBehavTimeSd(behavTimeSd);
		if("".equals(behavTimeEd) ||  behavTimeEd == null )systemLogHtmlsData.setBehavTimeEd(null);else systemLogHtmlsData.setBehavTimeEd(behavTimeEd);
		if("".equals(filePrvdIds) ||  filePrvdIds == null )systemLogHtmlsData.setFilePrvdIds(null);else systemLogHtmlsData.setFilePrvdIds(Arrays.asList(filePrvdIds.split(",")));

    	return Json.Encode(systemlogmgService.queryLogModelTbData(systemLogHtmlsData));
    }
    
    /**
     * 
     * @Description: 持续审计系统用户，对审计开关功能模块进行的操作的日志信息： 查询界面默认的筛选条件list 
     * @param @return   
     * @return String  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-11
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjrzcx})
    @ResponseBody
    @RequestMapping(value = "/queryControlTermData", produces = "text/json;charset=UTF-8")
    public String queryControlTermData(){

    	return Json.Encode(systemlogmgService.queryControlTermData());
    }
	
	/**
	 * 
	 * @Description: 持续审计系统用户，对审计开关功能模块进行的操作的日志信息： 查询界面操作日志表格内容
	 * @param @param behavTimeSd
	 * @param @param behavTimeEd
	 * @param @param subjectIds
	 * @param @param audTrms
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author ZhangQiang
	 * @date 2018-8-11
	 */
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjrzcx})
    @ResponseBody
    @RequestMapping(value = "/queryControlTableData", produces = "text/json;charset=UTF-8")
    public String queryControlTableData(String behavTimeSd,String behavTimeEd ,String subjectIds ,String audTrms){
    	
    	SystemLogHtmlData systemLogHtmlsData = new SystemLogHtmlData();
    	
    	if("".equals(subjectIds) ||  subjectIds == null )systemLogHtmlsData.setSubjectIds(null);else systemLogHtmlsData.setSubjectIds(Arrays.asList(subjectIds.split(",")));
		if("".equals(audTrms) ||  audTrms == null )systemLogHtmlsData.setAudTrms(null);else systemLogHtmlsData.setAudTrms(Arrays.asList(audTrms.split(",")));
		if("".equals(behavTimeSd) ||  behavTimeSd == null )systemLogHtmlsData.setBehavTimeSd(null);else systemLogHtmlsData.setBehavTimeSd(behavTimeSd);
		if("".equals(behavTimeEd) ||  behavTimeEd == null )systemLogHtmlsData.setBehavTimeEd(null);else systemLogHtmlsData.setBehavTimeEd(behavTimeEd);

    	return Json.Encode(systemlogmgService.queryControlTableData(systemLogHtmlsData));
    }
    
    
    /**
     * 
     * @Description: 持续审计系统用户，对参数管理功能模块进行的操作的日志信息： 查询界面默认的筛选条件list 
     * @param @return   
     * @return String  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-11
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjrzcx})
    @ResponseBody
    @RequestMapping(value = "/queryOtapaTermData", produces = "text/json;charset=UTF-8")
    public String queryOtapaTermData(){

    	return Json.Encode(systemlogmgService.queryOtapaTermData());
    }
	
	/**
	 * 
	 * @Description: 持续审计系统用户，对参数管理功能模块进行的操作的日志信息： 查询界面操作日志表格内容
	 * @param @param behavTimeSd
	 * @param @param behavTimeEd
	 * @param @param focusIds
	 * @param @param audTrms
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author ZhangQiang
	 * @date 2018-8-11
	 */
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjrzcx})
    @ResponseBody
    @RequestMapping(value = "/queryOtapaTableData", produces = "text/json;charset=UTF-8")
    public String queryOtapaTableData(String behavTimeSd,String behavTimeEd ,String focusIds ,String subjectIds){
	
    	SystemLogHtmlData systemLogHtmlsData = new SystemLogHtmlData();
    	
    	if("".equals(focusIds) ||  focusIds == null )systemLogHtmlsData.setFocusIds(null);else systemLogHtmlsData.setFocusIds(Arrays.asList(focusIds.split(",")));
		if("".equals(subjectIds) ||  subjectIds == null )systemLogHtmlsData.setSubjectIds(null);else systemLogHtmlsData.setSubjectIds(Arrays.asList(subjectIds.split(",")));
		if("".equals(behavTimeSd) ||  behavTimeSd == null )systemLogHtmlsData.setBehavTimeSd(null);else systemLogHtmlsData.setBehavTimeSd(behavTimeSd);
		if("".equals(behavTimeEd) ||  behavTimeEd == null )systemLogHtmlsData.setBehavTimeEd(null);else systemLogHtmlsData.setBehavTimeEd(behavTimeEd);

    	return Json.Encode(systemlogmgService.queryOtapaTableData(systemLogHtmlsData));
    }

    /**
     * 
     * @Description: 持续审计系统用户，对需求管理功能模块进行的操作的日志信息： 查询界面默认的筛选条件list 
     * @param @return   
     * @return String  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-11
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjrzcx})
    @ResponseBody
    @RequestMapping(value = "/queryReqmTermData", produces = "text/json;charset=UTF-8")
    public String queryReqmTermData(){

    	return Json.Encode(systemlogmgService.queryReqmTermData());
    }
	
	/**
	 * 
	 * @Description: 持续审计系统用户，对需求管理功能模块进行的操作的日志信息： 查询界面操作日志表格内容
	 * @param @param behavTimeSd
	 * @param @param behavTimeEd
	 * @param @param reqmIds
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author ZhangQiang
	 * @date 2018-8-11
	 */
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjrzcx})
    @ResponseBody
    @RequestMapping(value = "/queryReqmTableData", produces = "text/json;charset=UTF-8")
    public String queryReqmTableData(String behavTimeSd,String behavTimeEd ,String reqmIds){
	
    	SystemLogHtmlData systemLogHtmlsData = new SystemLogHtmlData();
    	
    	if("".equals(reqmIds) ||  reqmIds == null )systemLogHtmlsData.setReqmIds(null);else systemLogHtmlsData.setReqmIds(Arrays.asList(reqmIds.split(",")));
		if("".equals(behavTimeSd) ||  behavTimeSd == null )systemLogHtmlsData.setBehavTimeSd(null);else systemLogHtmlsData.setBehavTimeSd(behavTimeSd);
		if("".equals(behavTimeEd) ||  behavTimeEd == null )systemLogHtmlsData.setBehavTimeEd(null);else systemLogHtmlsData.setBehavTimeEd(behavTimeEd);

    	return Json.Encode(systemlogmgService.queryReqmTableData(systemLogHtmlsData));
    }
       
    /**
     * 
     * @Description: 系统操作日志-管理界面，查询展示数据
     * @param @param behavTimeSd
     * @param @param behavTimeEd
     * @param @param userIds
     * @param @param userPrvdIds
     * @param @param depIds
     * @param @param toolName
     * @param @return   
     * @return String  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-11
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjrzcx})
    @ResponseBody
    @RequestMapping(value = "/queryUseTableData", produces = "text/json;charset=UTF-8")
    public String queryUseTableData(String behavTimeSd,String behavTimeEd ,String userIds,String userPrvdIds,String depIds ,
    		String toolName){

    	if("".equals(toolName) ||  toolName == null )toolName=null;toolName = this.getStringOfEncod(toolName);
    	    	
    	SystemLogHtmlData systemLogHtmlsData = new SystemLogHtmlData();
    	
    	if("".equals(userIds) ||  userIds == null )systemLogHtmlsData.setUserIds(null);else systemLogHtmlsData.setUserIds(Arrays.asList(userIds.split(",")));
		if("".equals(userPrvdIds) ||  userPrvdIds == null )systemLogHtmlsData.setUserPrvdIds(null);else systemLogHtmlsData.setUserPrvdIds(Arrays.asList(userPrvdIds.split(",")));
		if("".equals(depIds) ||  depIds == null )systemLogHtmlsData.setDepIds(null);else systemLogHtmlsData.setDepIds(Arrays.asList(depIds.split(",")));
		if("".equals(behavTimeSd) ||  behavTimeSd == null )systemLogHtmlsData.setBehavTimeSd(null);else systemLogHtmlsData.setBehavTimeSd(behavTimeSd);
		if("".equals(behavTimeEd) ||  behavTimeEd == null )systemLogHtmlsData.setBehavTimeEd(null);else systemLogHtmlsData.setBehavTimeEd(behavTimeEd);
		if("".equals(toolName) ||  toolName == null )systemLogHtmlsData.setToolName(null);else systemLogHtmlsData.setToolName(toolName);
		
		
    	return Json.Encode(systemlogmgService.queryUseTableData(systemLogHtmlsData));
    }
    
    /**
     * 
     * @Description: 系统操作日志-管理界面，查询默认展示筛选项数据
     * @param @param systemLogHtmlData
     * @param @return   
     * @return String  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-8
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjrzcx})
    @ResponseBody
    @RequestMapping(value = "/queryUseTermData", produces = "text/json;charset=UTF-8")
    public String queryUseTermData(SystemLogHtmlData systemLogHtmlData){
    	
    	return Json.Encode(systemlogmgService.queryUseTermData(systemLogHtmlData));
    }   
    
    /**
     * 
     * @Description: 登陆日志管理界面，查询默认展示数据
     * @param @param systemLogHtmlData
     * @param @return   
     * @return String  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-5
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjrzcx })
    @ResponseBody
    @RequestMapping(value = "/queryDefaultData", produces = "text/json;charset=UTF-8")
    public String queryDefaultData(SystemLogHtmlData systemLogHtmlData){
    	
    	return Json.Encode(systemlogmgService.queryDefaultData(systemLogHtmlData));
    }
    
    /**
     * 
     * @Description: 登录日志查询页面-条件查询
     * @param @param behavTimeSd
     * @param @param behavTimeEd
     * @param @param userIds
     * @param @param userPrvdIds
     * @param @param depIds
     * @param @return   
     * @return String  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-8
     */
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjrzcx })
    @ResponseBody
    @RequestMapping(value = "/queryLoginTableData", produces = "text/json;charset=UTF-8")
    public String queryLoginTableData(String behavTimeSd,String behavTimeEd ,String userIds,String userPrvdIds,String depIds){
    	
    	SystemLogHtmlData systemLogHtmlsData = new SystemLogHtmlData();
    	
    	if("".equals(userIds) ||  userIds == null )systemLogHtmlsData.setUserIds(null);else systemLogHtmlsData.setUserIds(Arrays.asList(userIds.split(",")));
		if("".equals(userPrvdIds) ||  userPrvdIds == null )systemLogHtmlsData.setUserPrvdIds(null);else systemLogHtmlsData.setUserPrvdIds(Arrays.asList(userPrvdIds.split(",")));
		if("".equals(depIds) ||  depIds == null )systemLogHtmlsData.setDepIds(null);else systemLogHtmlsData.setDepIds(Arrays.asList(depIds.split(",")));
		if("".equals(behavTimeSd) ||  behavTimeSd == null )systemLogHtmlsData.setBehavTimeSd(null);else systemLogHtmlsData.setBehavTimeSd(behavTimeSd);
		if("".equals(behavTimeEd) ||  behavTimeEd == null )systemLogHtmlsData.setBehavTimeEd(null);else systemLogHtmlsData.setBehavTimeEd(behavTimeEd);
		
		return Json.Encode(systemlogmgService.queryLoginTableData(systemLogHtmlsData));
    }
    
    /**
     * 
     * @Description: 通过专题ID获取审计月list
     * @param @param systemLogHtmlData
     * @param @return   
     * @return String  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-23
     */
    @ResponseBody
	@RequestMapping(value = "/getAudtrmOfId", produces = "text/json; charset=UTF-8")
    public String getAudtrmOfId(String subject_id){
    	
    	SystemLogHtmlData systemLogHtmlsData = new SystemLogHtmlData();  	
    	if("".equals(subject_id) ||  subject_id == null )systemLogHtmlsData.setSubjectIds(null);else systemLogHtmlsData.setSubjectIds(Arrays.asList(subject_id.split(",")));
		
    	return Json.Encode(systemlogmgService.getAudtrmOfId(systemLogHtmlsData));
    }
    
    @ResponseBody
	@RequestMapping(value = "/getFocusBySubId", produces = "text/json; charset=UTF-8")
    public String getFocusBySubId(String subject_id){
    	
    	SystemLogHtmlData systemLogHtmlsData = new SystemLogHtmlData();  	
    	if("".equals(subject_id) ||  subject_id == null )systemLogHtmlsData.setSubjectIds(null);else systemLogHtmlsData.setSubjectIds(Arrays.asList(subject_id.split(",")));
		
    	return Json.Encode(systemlogmgService.getFocusBySubId(systemLogHtmlsData));
    }
    
    /**
     * 
     * @Description: 新增用户登录情况
     * @param @param request
     * @param @return   
     * @return String  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-8
     */
    @ResponseBody
	@RequestMapping(value = "/addLoginInfoData", produces = "text/json; charset=UTF-8")
	public String addLoginInfoData(HttpServletRequest request) {

		return Json.Encode(systemlogmgService.addLoginInfoData(request));
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
		return Json.Encode(systemlogmgService.checkLogin(request));
    }
    
    /**
     * 
     * @Description: 将字符串转义为普通字符串
     * @param @param str
     * @param @return   
     * @return String  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-15
     */
    public String getStringOfEncod(String str){
    	
    	String keyWord = "" ;
    	
    	if (str == null ) {
    		return null ;
		}
    	// 对字符进行转义
		try {
			// 将application/x-www-from-urlencoded字符串转换成普通字符串  
			keyWord = URLDecoder.decode(str, getEncoding(str));  
			return keyWord ;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getStringOfEncod >>>>> str=" + str + "; keyWord=" + keyWord, e);
		}
		
		return null ;
    }
    
    /**
     * 
     * @Description: 得到字符串的编码格式
     * @param @param str
     * @param @return   
     * @return String  
     * @throws
     * @author ZhangQiang
     * @date 2018-8-15
     */
    public String getEncoding(String str){
    	
		String encoding = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(),encoding))) {
				return encoding;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEncoding >>>>> " , e);
		}
		
		encoding = "GBK";
		try {
			if (str.equals(new String(str.getBytes(),encoding))) {
				return encoding;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEncoding >>>>> " , e);
		}
		
		encoding = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(),encoding))) {
				return encoding;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEncoding >>>>> " , e);
		}
		
		encoding = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(),encoding))) {
				return encoding;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getEncoding >>>>> " , e);
		}
		
		return null;
	}  
    
}
