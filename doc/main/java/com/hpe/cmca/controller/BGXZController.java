/**
 * com.hpe.cmca.controller.JKGJController.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.controller;

import com.asiainfo.biframe.privilege.IUser;
import com.hpe.cmca.finals.RoleEnum;
import com.hpe.cmca.interfaces.BgxzMapper;
import com.hpe.cmca.pojo.BgxzData;
import com.hpe.cmca.service.BgxzService;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bgxz")
public class BGXZController extends BaseController {

    // @Autowired
    // private MybatisDao mybatisDao;
    @Autowired
    private BgxzService bgxzService;

    // @CmcaAuthority(authorityTypes = { AuthorityType.jkgj })//判断是否具有监控告警权限
    @ResponseBody
    @RequestMapping(value = "index", produces = "text/json;charset=UTF-8")
    public String index(HttpServletRequest request) {
	Map<String, Object> data = new HashMap<String, Object>();
	Object depId = request.getSession().getAttribute("depId");
	if (depId != null) {
	    data.put("roleId", RoleEnum.getSjkgIDByRealID(depId.toString()));
	} else {
	    data.put("roleId", "");
	}
	if (request.getSession().getAttribute("userPrvdId") != null) {
	    data.put("userPrvdId", request.getSession().getAttribute("userPrvdId"));
	    data.put("userPrvdName", getCompanyNameOfProvince((String) request.getSession().getAttribute("userPrvdId")));
	} else {
	    data.put("userPrvdId", "");
	    data.put("userPrvdName", "");
	}
	IUser user = (IUser) request.getSession().getAttribute("ssoUSER");
	// Map<String,Object> result =new HashMap<String,Object>();
	// HttpSession session = request.getSession();
	data.put("userName", user.getUsername());
	data.put("isupload", "false");
	data.put("isreview", "false");
	BgxzMapper bgxzMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
	List<Map<String, Object>> dataList = bgxzMapper.selReportPerson(null);
	if (dataList.size() > 0) {
	    for (Map<String, Object> d : dataList) {
		if (d.get("user_id") != null && d.get("user_id").toString().toUpperCase().equals(user.getUserid().toUpperCase()) && d.get("flag").equals("upload")) {
		    data.put("isupload", "true");
		}
		if (d.get("user_id") != null && d.get("user_id").toString().toUpperCase().equals(user.getUserid().toUpperCase()) && d.get("flag").equals("review")) {
		    data.put("isreview", "true");
		}

	    }
	}
	return Json.Encode(data);
    }

    /**
     * 分专题下载次数排名
     *
     * <pre>
     * Desc
     * @param request
     * @param uiModel
     * @return
     * @author issuser
     * 2017-12-11 上午10:59:38
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "downNumsBySubject", produces = "text/json;charset=UTF-8")
    public String downNumsBySubject(HttpServletRequest request, BgxzData bgxz) {
	List<Map<String, Object>> dataList = bgxzService.downNumsBySubject(bgxz);
	if (2 == getSwitchStateByDepId(request)) {
	    Iterator<Map<String, Object>> iter = dataList.iterator();
	    while (iter.hasNext()) {
		Map<String, Object> item = iter.next();
		if ("9".equals(item.get("id").toString())) {
		    iter.remove();
		}
	    }
	}
	return Json.Encode(dataList);
    }

    /**
     * 分省下载次数排名
     *
     * <pre>
     * Desc
     * @param request
     * @param uiModel
     * @return
     * @author issuser
     * 2017-12-12 下午4:15:26
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "downNumsByPrvd", produces = "text/json;charset=UTF-8")
    public String downNumsByPrvd(BgxzData bgxz) {
	return Json.Encode(bgxzService.downNumsByPrvd(bgxz));
    }

    /**
     * 各专题报告生成时间趋势
     *
     * <pre>
     * Desc
     * @param request
     * @param uiModel
     * @return
     * @author issuser
     * 2017-12-12 下午4:15:06
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "createDayBySubjectMonth", produces = "text/json;charset=UTF-8")
    public String createDayBySubjectMonth(BgxzData bgxz) {

	return Json.Encode(bgxzService.createDayBySubjectMonth(bgxz));
    }

    /**
     * 专题报告下载清单
     *
     * <pre>
     * Desc
     * @param bgxz
     * @return
     * @author issuser
     * 2017-12-18 下午2:32:03
     * </pre>
     *
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "downRecordsTable", produces = "text/json;charset=UTF-8")
    public String downRecordsTable(BgxzData bgxz, HttpServletRequest request) throws ParseException {

	return Json.Encode(bgxzService.downRecordsTable(bgxz, request));
    }

    /**
     * 已上传列表
     *
     * <pre>
     * Desc
     * @param bgxz
     * @param request
     * @return
     * @throws ParseException
     * @author issuser
     * 2018-1-18 下午4:18:54
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "selReportUpload", produces = "text/json;charset=UTF-8")
    public String selReportUpload(BgxzData bgxz, HttpServletRequest request) throws ParseException {

	return Json.Encode(bgxzService.selReportUpload(bgxz, request));
    }

    /**
     * 统计上传条数
     *
     * <pre>
     * Desc
     * @param bgxz
     * @return
     * @throws ParseException
     * @author issuser
     * 2018-1-18 下午9:07:19
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "countReportUpload", produces = "text/json;charset=UTF-8")
    public String countReportUpload(BgxzData bgxz) throws ParseException {

	return Json.Encode(bgxzService.countReportUpload(bgxz));
    }

    @RequestMapping(value = "downFile", produces = "plain/text; charset=UTF-8")
    public void downFile(HttpServletRequest request, HttpServletResponse response) throws ParseException {
	// String ids[] =request.getParameterValues("ids");
	String dlpath = null;
	String ids_ = request.getParameter("ids");
	if (ids_ != null && ids_.indexOf(",") != -1) {
	    dlpath = bgxzService.zipFile(ids_.split(","));
	} else {
	    dlpath = bgxzService.zipFile(new String[] { ids_ });
	}
	// "http://10.255.219.179/hp/data/201604/2/2002/10000/2_2002_201604_10000.doc";//
	    String fileName = dlpath.substring(dlpath.lastIndexOf("/") + 1, dlpath.length());
	    logger.error("fileName:>>" + fileName);
		FileUtil.downFileByHttp(request,response, dlpath, fileName, logger);


    }

    /**
     * 审核文件
     *
     * <pre>
     * Desc
     * @param bgxz
     * @return
     * @throws ParseException
     * @author issuser
     * 2018-1-20 下午12:48:55
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "reviewReportUpload", produces = "text/json;charset=UTF-8")
    public String reviewReportUpload(HttpServletRequest request, BgxzData bgxz) throws ParseException {
	String result = "success";
	try {
	    String ids_ = request.getParameter("ids");
	    if (ids_ != null && ids_.indexOf(",") != -1) {
		bgxzService.reviewReportUpload(ids_.split(","), bgxz.getStatus(), bgxz.getReviewOpinion());
	    } else {
		bgxzService.reviewReportUpload(new String[] { ids_ }, bgxz.getStatus(), bgxz.getReviewOpinion());
	    }

	} catch (Exception e) {
	    result = "fail";
	}
	return result;
    }

    /**
     * 删除文件
     *
     * <pre>
     * Desc
     * @param request
     * @param bgxz
     * @return
     * @throws ParseException
     * @author issuser
     * 2018-1-20 下午4:10:40
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "delUploadFile", produces = "text/json;charset=UTF-8")
    public String delUploadFile(HttpServletRequest request, BgxzData bgxz) throws ParseException {
	String result = "success";
	try {
	    String ids_ = request.getParameter("ids");
	    if (ids_ != null && ids_.indexOf(",") != -1) {
		bgxzService.delUploadFile(ids_.split(","));
	    } else {
		bgxzService.delUploadFile(new String[] { ids_ });
	    }

	} catch (Exception e) {
	    result = "fail";
	}
	return result;
    }

    /**
     * 待审核/已审核条数
     *
     * <pre>
     * Desc
     * @return
     * @author issuser
     * 2018-1-20 下午4:53:18
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "countReviewFile", produces = "text/json;charset=UTF-8")
    public String countReviewFile() {

	return Json.Encode(bgxzService.countReviewFile());
    }

    @ResponseBody
    @RequestMapping(value = "selCsvBySubjectId", produces = "text/json;charset=UTF-8")
    public String selCsvBySubjectId(BgxzData bgxz) {

	return Json.Encode(bgxzService.selCsvBySubjectId(bgxz));
    }

    @ResponseBody
    @RequestMapping(value = "checkLogin", produces = "text/json;charset=UTF-8")
    public String checkLogin(HttpServletRequest request) throws ParseException {
	Map<String, Object> data = new HashMap<String, Object>();
	IUser user = (IUser) request.getSession().getAttribute("ssoUSER");
	if (user == null) {
	    data.put("islogin", "0");// 未登录/登录已失效
	} else {
	    data.put("islogin", "1");// 已登录
	}
	return Json.Encode(data);
    }

}
