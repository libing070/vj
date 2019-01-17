/**
 * com.hp.cmcc.controller.DataQualityAuditController.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.controller;

import com.asiainfo.biframe.privilege.IUser;
import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.common.CommonResult;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.finals.StringUtils;
import com.hpe.cmca.service.SJZLService;
import com.hpe.cmca.util.ExceptionTool;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/sjzl")
public class SJZLController extends BaseObject {
	@Autowired
	private SJZLService sjzlService;

    @Autowired
    private MybatisDao mybatisDao;

    @CmcaAuthority(authorityTypes = { AuthorityType.sjzl })
	@RequestMapping(value = "index")
	public String index() {
		return "sjzl/index";
	}
    @CmcaAuthority(authorityTypes = { AuthorityType.sjzl })
	@ResponseBody
	@RequestMapping(value = "getListBynmBmAudTrm", produces = "plain/text; charset=UTF-8")
	public  String getListBynmBmAudTrm(String audTrm,String nmBm) throws UnsupportedEncodingException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(audTrm)) {
			paramMap.put("cycleDate", audTrm);
		}
		if (StringUtils.isNotBlank(nmBm)) {
				paramMap.put("nmBm", nmBm);
		}
		List<Map<String, Object>> auditList = mybatisDao.getList("sjzl.getAuditExcelList", paramMap);
		return  Json.Encode(auditList);
	}
    @CmcaAuthority(authorityTypes = { AuthorityType.sjzl })
	@ResponseBody
	@RequestMapping(value = "getList", produces = "plain/text; charset=UTF-8")
	public  String getList(String audTrm,String subjectId) throws UnsupportedEncodingException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String cycleDate = audTrm;
		String jobName = subjectId;
		if (StringUtils.isNotBlank(cycleDate)) {
			paramMap.put("cycleDate", cycleDate);
		}
		if (StringUtils.isNotBlank(jobName)) {
			if (jobName.equals("2")){
				paramMap.put("jobName", "QDYK%");
			} else if (jobName.equals("3")) {
				paramMap.put("jobName", "ZDTL%");
			} else if (jobName.equals("1")) {
				paramMap.put("jobName", "YJK%");
			}
		}
		List<Map<String, Object>> auditList = sjzlService.getList(paramMap);
		return  Json.Encode(auditList);
	}

    @CmcaAuthority(authorityTypes = { AuthorityType.sjzl })
	@ResponseBody
	@RequestMapping(value = "getAutoList", produces = "plain/text; charset=UTF-8")
	public  String getAutoList(String audTrm,String subjectId) throws UnsupportedEncodingException {
		Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("audTrm", audTrm);
			paramMap.put("subjectId", subjectId);
		List<Map<String, Object>> auditList = mybatisDao.getList("sjzl.getAutoList", paramMap);
		return  Json.Encode(auditList);
	}

     @CmcaAuthority(authorityTypes = { AuthorityType.sjzl })
	 @ResponseBody
	 @RequestMapping(value = "getMonthByYear", produces = "plain/text; charset=UTF-8")
	 public String getMonthByYear(String selYear) {

		 Map<String, List<String>> result = new HashMap<String, List<String>>();
		 //List<String> audMonList=sjzlService.getMonthByYear(selYear);
		 List<String> audMonList=sjzlService.getSJZLMonthByYear(selYear);

		 result.put("audMonList", audMonList);

		CommonResult commonResult = new CommonResult();
		commonResult.setStatus("0");
		commonResult.setBody(result);
		return Json.Encode(commonResult);
	    }

     	@CmcaAuthority(authorityTypes = { AuthorityType.sjzl })
	    @ResponseBody
	    @RequestMapping(value = "getYear", produces = "plain/text; charset=UTF-8")
	    public String getYear() {
	    Map<String, Object> result = new HashMap<String, Object>();
		List<String> yearList = new ArrayList<String>();
		//yearList=sjzlService.getYear();
		yearList=sjzlService.getSJZLYear();
		result.put("auditYearList", yearList);
		CommonResult commonResult = new CommonResult();
		commonResult.setStatus("0");
		commonResult.setBody(result);
		return Json.Encode(commonResult);
	    }

     	@CmcaAuthority(authorityTypes = { AuthorityType.sjzl })
	    @RequestMapping(value = "downLoadExcel", produces = "plain/text; charset=UTF-8")
		public void downLoadExcel(HttpServletResponse response,	HttpServletRequest request, String checkId,String audTrm) {
	    	HttpSession session = request.getSession();
	    	String person="unknown";
	    	IUser user = (IUser) request.getSession().getAttribute("ssoUSER");
	    	if (user == null) {
	    		person = "unknown";
			} else {
				person = user.getUsername();
			}
	    	Map<String, Object> params = new HashMap<String, Object>();
			params.put("audTrm", audTrm);
			params.put("checkId", checkId);
			params.put("person", person);
			logger.debug("######  开始获取Excel下载路径:" + params);
			List<Map<String, Object>> list = mybatisDao.getList("sjzl.getAutoExcelList", params);
			//Map<String, Object> downLoadFile = (0 == list.size()) ? new HashMap<String, Object>() : list.get(0);
				// 文件是存放在一个支持'ftp上传|http下载'的ftp服务器
			if (0 == list.size()) {
				logger.debug("######  从数据库中未查询到要下载的文件记录:" + params);
			} else {
				try {
					Map<String, Object> downLoadFile = list.get(0);
					// 文件是存放在一个支持'ftp上传|http下载'的ftp服务器
					String dlpath = (String) downLoadFile.get("download_url");
					logger.debug("######  当前下载ExcelURL:" + dlpath);
					String fileName = dlpath.substring(dlpath.lastIndexOf("/") + 1, dlpath.length());
					logger.debug("######  当前下载Excel文件名:" + fileName);

					FileUtil.downFileByHttp(request,response, dlpath, fileName, logger);

				//	mybatisDao.update("sjzl.updateAutoDlInfo", params);
				} catch (Exception e) {
					logger.error("######  下载异常Excel文件:" + ExceptionTool.getExceptionDescription(e));
				}
			}
			logger.debug("######  完成下载的Excel:" + params);
		}
     	@CmcaAuthority(authorityTypes = { AuthorityType.sjzl })
     	@ResponseBody
	    @RequestMapping(value = "updateDownloadInfo", produces = "plain/text; charset=UTF-8")
		public void updateDownloadInfo(HttpServletResponse response,	HttpServletRequest request, String checkId,String audTrm) {
	    	HttpSession session = request.getSession();
	    	String person="unknown";
	    	IUser user = (IUser) request.getSession().getAttribute("ssoUSER");
	    	if (user == null) {
	    		person = "unknown";
			} else {
				person = user.getUsername();
			}
	    	Map<String, Object> params = new HashMap<String, Object>();
			params.put("audTrm", audTrm);
			params.put("checkId", checkId);
			params.put("person", person);
			logger.debug("######  开始获取要更新的记录信息:" + params);
			List<Map<String, Object>> list = mybatisDao.getList("sjzl.getAutoExcelList", params);
			//Map<String, Object> downLoadFile = (0 == list.size()) ? new HashMap<String, Object>() : list.get(0);
				// 文件是存放在一个支持'ftp上传|http下载'的ftp服务器
			if (0 == list.size()) {
				logger.debug("######  从数据库中未查询到要更新的文件记录:" + params);
			} else {
				try {
					mybatisDao.update("sjzl.updateAutoDlInfo", params);
				} catch (Exception e) {
					logger.error("######  更新异常记录:" + ExceptionTool.getExceptionDescription(e));
				}
			}
			logger.debug("######  完成更新的记录:" + params);
		}

     	@CmcaAuthority(authorityTypes = { AuthorityType.sjzl })
    	@ResponseBody
    	@RequestMapping(value = "getDownloadTimes", produces = "plain/text; charset=UTF-8")
    	public String getDownloadTimes(String audTrm,String checkId){

    		Map<String, Object> params = new HashMap<String, Object>();
    		params.put("audTrm", audTrm);
    		params.put("checkId", checkId);
    		List<Map<String, Object>> list=mybatisDao.getList("sjzl.getDownloadTimes", params);

    		return  Json.Encode(list);

    	}
}
