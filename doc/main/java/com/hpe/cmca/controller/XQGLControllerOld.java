/**
 * com.hpe.cmca.controller.XQGLControllerOlder.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.controller;

import com.hpe.cmca.common.CommonResult;
import com.hpe.cmca.service.BaseFileService;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.Json;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <pre>
 * Desc： 将cmca3.0中第一版需求管理Java层的相关内容迁移至此；
 * 原类拦截（xqgl）,现改为（xqglOld）
 * 通过xqgl.xml直接连接数据库
 * @author   hufei
 * @refactor hufei
 * @date     2018-4-17 上午9:53:34
 * @version  1.0
 *
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  2018-4-17 	   hufei 	         1. Created this class.
 * </pre>
 */
public class XQGLControllerOld extends BaseController {
    @Autowired
    private BaseFileService baseFileService;

    // @CmcaAuthority(authorityTypes = { AuthorityType.yjk })//判断是否具有有价卡权限
    @RequestMapping(value = "index")
    public String index() {
	return "xqgl/index";
    }

    @RequestMapping(value = "loginTest")
    public String loginTest() {
	return "xqgl/loginTest";
    }

    @RequestMapping(value = "demandFormList")
    public String demandFormList() {
	return "xqgl/demandFormList";
    }

    /**
     *
     * <pre>
     * Desc模拟登陆入口
     * @param request
     * @param user_id
     * @param userPrvdId
     * @author feihu
     * Feb 21, 2017 2:34:03 PM
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "login", produces = "plain/text; charset=UTF-8")
    public void login(HttpServletRequest request, String user_id, String userPrvdId) {
	HttpSession session = request.getSession();
	String userId = (String) session.getAttribute("userId");
	session.setAttribute("userPrvdId", userPrvdId);
	session.setAttribute("userId", user_id);

    }

    /**
     *
     * <pre>
     * Desc 默认查询
     * @param request
     * @param uiModel
     * @return
     * @author feihu
     * Dec 29, 2016 4:38:29 PM
     * </pre>
     */
    @SuppressWarnings("deprecation")
    @ResponseBody
    @RequestMapping(value = "queryDefaultParams", produces = "plain/text; charset=UTF-8")
    public String queryDefaultParams(HttpServletRequest request, Model uiModel) {
	Map<String, Object> result = new HashMap<String, Object>();
	String userId = (String) request.getSession().getAttribute("userId");
	String prvdIds = getUserPrvdId(request);
	if (userId == null || "".equals(userId)) {
	    userId = "xiaoyonggang";
	}
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put("user_id", userId);
	Date d = new Date();
	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH:mm:ss");
	String now = df.format(d);
	String s_endTime = now.substring(0, 8);// 年月日
	if (d.getMonth() == 0) {
	    d.setYear(d.getYear() - 1);
	    d.setMonth(11);
	} else {
	    d.setMonth(d.getMonth() - 1);
	}
	String lastMonth = df.format(d);
	String s_startTime = lastMonth.substring(0, 8);// 年月日
	// List<Map<String,Object>> result=mybatisDao.getList("xqgl.queryDefaultParams", params);
	result.put("s_startTime", s_startTime);
	result.put("s_endTime", s_endTime);
	if ("11000".equals(prvdIds) || prvdIds == null || "".equals(prvdIds)) {
	    // result.put("isQuanguo", true);
	    result.put("s_provid", "11000");
	} else {
	    // result.put("isQuanguo", false);
	    result.put("s_provid", prvdIds);// 登录用户所在省份，10000表示集团用户
	    // result.put("ctyIds", "10000");
	    // Prvd_info prvdInfo = Constants.getPrvdInfo(prvdIds);
	    // result.put("provName", prvdInfo.getPrivdcd());
	    // result.put("provCName", prvdInfo.getPrivdnm());
	}
	String prvd_name = getCompanyNameOfProvince(prvdIds);
	if (prvd_name.equals("全公司")) {
	    prvd_name = "内审部部门";
	}
	result.put("s_prvd_name", prvd_name.substring(0, prvd_name.length() - 2));
	result.put("s_userid", userId);
	return Json.Encode(result);
    }

    /**
     *
     * <pre>
     * Desc查询列表
     * @param userId
     * @param tabType
     * @param subject_code
     * @param rms_type
     * @param status
     * @param timeType
     * @param startTime
     * @param endTime
     * @return
     * @author feihu
     * Jan 9, 2017 5:51:42 PM
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "queryParams", produces = "plain/text; charset=UTF-8")
    public String queryParams(String userId, String prvdid, String sourcePrvd, String tabType, String subject_code, String rms_type, String status, String timeType, String startTime, String endTime,
	    int readType) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("user_id", userId);
	params.put("tabType", tabType);
	params.put("subject_code", subject_code);
	params.put("rms_type", rms_type);
	params.put("status", status);
	params.put("timeType", timeType);
	params.put("startTime", startTime);
	params.put("endTime", endTime);
	params.put("prvd_id", Integer.parseInt(prvdid));
	params.put("sourcePrvd", Integer.parseInt(sourcePrvd));
	params.put("readType", readType);
	List<Map<String, Object>> result = mybatisDao.getList("xqgl.queryParams", params);
	return CommonResult.success(changeResult(result));

    }

    private List<Map<String, Object>> changeResult(List<Map<String, Object>> result) {
	for (Map<String, Object> map : result) {
	    if ((map.get("rms_type").equals("2"))) {
		map.put("rms_type_name", "临时");
	    } else if ((map.get("rms_type").equals("1"))) {
		map.put("rms_type_name", "长期");
	    } else {
		map.put("rms_type_name", "临时");
	    }

	    if ((map.get("subject_code").equals("yktl"))) {
		map.put("subject_code_name", "养卡套利");
	    } else if ((map.get("subject_code").equals("zdtl"))) {
		map.put("subject_code_name", "社会渠道终端套利");
	    } else if ((map.get("subject_code").equals("yjk"))) {
		map.put("subject_code_name", "有价卡违规管理");
	    } else if ((map.get("subject_code").equals("khqf"))) {
		map.put("subject_code_name", "客户欠费");
	    } else {
		map.put("subject_code_name", "员工异常操作");
	    }
	    //
	    // if(map.containsKey("relationTime")){
	    // map.get("relationTime")map.toString().replace('.', '年');
	    // }

	    String prvd_name = getCompanyNameOfProvince(map.get("prvd_id").toString());
	    if (prvd_name.equals("全公司")) {
		prvd_name = "内审部部门";
	    }
	    map.put("prvd_name", prvd_name.substring(0, prvd_name.length() - 2));
	}
	return result;

    }

    /**
     *
     * <pre>
     * Desc查询明细
     * @param request
     * @param rms_code
     * @return
     * @author feihu
     * Jan 9, 2017 5:52:16 PM
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "queryDetails", produces = "plain/text; charset=UTF-8")
    public String queryDetails(HttpServletRequest request, String rms_code) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("rms_code", rms_code);
	List<Map<String, Object>> result = mybatisDao.getList("xqgl.queryDetails", params);
	return CommonResult.success(changeResult(result));
    }

    /**
     * <pre>
     * Desc 查询目前审批状态
     * @param request
     * @param rms_code
     * @return
     * @author feihu
     * Feb 14, 2017 7:38:31 PM
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "queryNodeShow", produces = "plain/text; charset=UTF-8")
    public String queryNodeShow(HttpServletRequest request, String rms_code) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("rms_code", rms_code);
	List<Map<String, Object>> result = mybatisDao.getList("xqgl.queryNodeShow", params);
	return CommonResult.success(result);
    }

    /**
     *
     * <pre>
     * Desc  查询附件信息
     * @param request
     * @param rms_code
     * @return
     * @author feihu
     * Jan 17, 2017 4:52:47 PM
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "queryAttachDetails", produces = "plain/text; charset=UTF-8")
    public String queryAttachDetails(HttpServletRequest request, String rms_code) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("rms_code", rms_code);
	List<Map<String, Object>> result = mybatisDao.getList("xqgl.queryAttachDetails", params);
	return CommonResult.success(result);
    }

    /**
     * <pre>
     * Desc查询下一审批人是否显示
     * @param request
     * @param rms_code
     * @return
     * @author feihu
     * Jan 22, 2017 2:32:21 PM
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "queryShowNextApprover", produces = "plain/text; charset=UTF-8")
    public String queryShowNextApprover(HttpServletRequest request, String userId) {
	userId = (String) request.getSession().getAttribute("userId");
	if (userId == null || "".equals(userId)) {
	    userId = "xiaoyonggang";
	}
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("user_id", userId);
	List<Map<String, Object>> result = mybatisDao.getList("xqgl.queryShowNextApprover", params);
	return CommonResult.success(result);
    }

    /**
     * <pre>
     * Desc查询审批历史
     * @param request
     * @param rms_code
     * @return
     * @author feihu
     * Jan 9, 2017 5:52:29 PM
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "queryApproveHistory", produces = "plain/text; charset=UTF-8")
    public String queryApproveHistory(HttpServletRequest request, String rms_code) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("rms_code", rms_code);
	List<Map<String, Object>> result = mybatisDao.getList("xqgl.queryApproveHistory", params);
	return CommonResult.success(result);
    }

    /**
     * <pre>
     * Desc审批
     * @param request
     * @param rms_code
     * @param user_id
     * @param status
     * @param approval_content
     * @param route_condition
     * @return
     * @author feihu
     * Jan 9, 2017 5:51:06 PM
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "Approval", produces = "plain/text; charset=UTF-8")
    public String Approval(HttpServletRequest request, String rms_code, String user_id, String status, String approval_content, String route_condition) {
	Map<String, Object> params = new HashMap<String, Object>();
	// 获取当前节点及下一节点
	params.put("rms_code", rms_code);
	params.put("user_id", user_id);
	params.put("route_condition", route_condition);
	params.put("approval_content", approval_content);
	List<Map<String, Object>> route = mybatisDao.getList("xqgl.queryRoute", params);
	List<Map<String, Object>> currenId = mybatisDao.getList("xqgl.queryCurrenId", params);
	// 插入审批意见
	if (route.size() > 0) {
	    params.put("node_id", Integer.parseInt(route.get(0).get("from_node").toString()));
	} else {
	    params.put("node_id", Integer.parseInt(currenId.get(0).get("node_id").toString()));
	}
	params.put("approval_id", ((int) ((Math.random() * 9 + 1) * 100000)));
	params.put("status", status);
	mybatisDao.add("xqgl.addApproval", params);

	// 插入已办
	params.put("read_deal_id", ((int) ((Math.random() * 9 + 1) * 100000)));
	params.put("stats", '2');
	mybatisDao.add("xqgl.addReadDeal", params);

	// 插入待阅
	if (route.size() > 0) {
	    addNeedRead(user_id, rms_code, Integer.parseInt(route.get(0).get("from_node").toString()));
	} else {
	    addNeedRead(user_id, rms_code, Integer.parseInt(currenId.get(0).get("node_id").toString()));
	}
	// 更改待办
	Map<String, Object> paramsNeedDeal = new HashMap<String, Object>();
	paramsNeedDeal.put("rms_code", rms_code);
	if (route.size() > 0) {
	    paramsNeedDeal.put("node_id", Integer.parseInt(route.get(0).get("to_node").toString()));
	    if (Integer.parseInt(route.get(0).get("to_node").toString()) < 3) {
		Map<String, Object> user_params = new HashMap<String, Object>();
		user_params.put("rms_code", rms_code);
		List<Map<String, Object>> result = mybatisDao.getList("xqgl.queryDetails", user_params);
		String a = result.get(0).get("create_id").toString();
		paramsNeedDeal.put("user_id", result.get(0).get("create_id").toString());
	    }
	} else {
	    paramsNeedDeal.put("node_id", 6);
	}
	List<Map<String, Object>> result = mybatisDao.getList("xqgl.updateNeedDeal", paramsNeedDeal);

	return CommonResult.success(result);
    }

    /**
     * <pre>
     * Desc增加待阅
     * @param user_id
     * @param rms_code
     * @param node_id
     * @author feihu
     * Jan 9, 2017 5:50:50 PM
     * </pre>
     */
    public void addNeedRead(String user_id, String rms_code, int node_id) {
	Map<String, Object> params = new HashMap<String, Object>();
	// 获取当前节点及下一节点
	params.put("node_id", node_id);
	params.put("user_id", user_id);
	List<Map<String, Object>> user = mybatisDao.getList("xqgl.queryUser", params);
	Map<String, Object> value = null;
	if (user.size() != 0) {
	    for (Map<String, Object> map : user) {
		value = new HashMap<String, Object>();
		value.put("read_deal_id", ((int) ((Math.random() * 9 + 1) * 100000)));
		value.put("user_id", map.get("user_id"));
		value.put("stats", '0');
		value.put("node_id", node_id);
		value.put("rms_code", rms_code);
		mybatisDao.add("xqgl.addReadDeal", value);
	    }
	}

    }

    /**
     *
     * <pre>
     * Desc  新建需求
     * @param request
     * @param name
     * @param subject_code
     * @param rms_type
     * @param relationTime
     * @param content
     * @author feihu
     * Dec 27, 2016 7:22:19 PM
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "addRMS", produces = "plain/text; charset=UTF-8")
    public void addRMS(HttpServletRequest request, String user_id, String prvd_id, String rms_name, String rms_code, String subject_code, String rms_type, String relationTime, String[] attachURL,
	    String status, String rms_content) {
	// 中文乱码转换
	String prvd_name = getCompanyNameOfProvince(prvd_id);
	if (prvd_name.equals("全公司")) {
	    prvd_name = "内审部部门";
	}
	// 修改RMS后保存
	if (rms_code.equals("001_001_0_00") || rms_code == "001_001_0_00") {
	    // 获取当前需求编号
	    Map<String, Object> prid_params = new HashMap<String, Object>();
	    prid_params.put("prvd_id", prvd_id);
	    Map<String, Object> code = mybatisDao.get("xqgl.selectRMSCode", prid_params);
	    // 拼接插入RMS数据
	    Map<String, Object> params = new HashMap<String, Object>();
	    rms_code = prvd_id + "_" + subject_code + "_" + rms_type + "_" + code.get("current_serial").toString();

	    // IUser user=(IUser)request.getSession().getAttribute("ssoUSER");
	    // if(user!=null){
	    // user_name[0]=user.getUsername();
	    // user_id[0]=user.getUserid();
	    // }
	    params.put("rms_id", ((int) ((Math.random() * 9 + 1) * 100000)));
	    params.put("rms_code", rms_code);
	    params.put("prvd_id", Integer.parseInt(prvd_id));
	    params.put("name", prvd_name.substring(0, prvd_name.length() - 2) + "_" + rms_name);
	    params.put("subject_code", subject_code);
	    params.put("rms_type", rms_type);
	    params.put("create_id", user_id);
	    params.put("status", status);
	    params.put("content", rms_content);
	    params.put("relationTime", relationTime);
	    logger.debug(params);
	    mybatisDao.add("xqgl.addRMS", params);

	    // 更改当前需求编号
	    code.put("current_serial", Integer.parseInt(code.get("current_serial").toString()) + 1);
	    mybatisDao.update("xqgl.updateRMSCode", code);

	    // 插入附件路径到数据库中
	    if (attachURL.length > 0) {
		addDBAttach(rms_code, attachURL);
	    }
	    // 插入一条待办
	    if (status.equalsIgnoreCase("rms_save") || status == "rms_save") {
		addNeadDeal(user_id, params.get("rms_code").toString(), 2);
	    }
	    if (status.equalsIgnoreCase("rms_submit") || status == "rms_submit") {
		if (subject_code.equals("yktl")) {
		    addNeadDeal("admin", params.get("rms_code").toString(), 3);
		} else if (subject_code.equals("zdtl")) {
		    addNeadDeal("admin", params.get("rms_code").toString(), 4);
		} else if (subject_code.equals("yjk")) {
		    addNeadDeal("admin", params.get("rms_code").toString(), 5);
		}

	    }

	} else {
	    Map<String, Object> edit_rms_params = new HashMap<String, Object>();
	    edit_rms_params.put("rms_code", rms_code);
	    edit_rms_params.put("prvd_id", Integer.parseInt(prvd_id));
	    edit_rms_params.put("name", rms_name);
	    edit_rms_params.put("subject_code", subject_code);
	    edit_rms_params.put("rms_type", rms_type);
	    edit_rms_params.put("create_id", user_id);
	    edit_rms_params.put("status", status);
	    edit_rms_params.put("content", rms_content);
	    edit_rms_params.put("relationTime", relationTime);
	    logger.debug(edit_rms_params);
	    // 更新需求表相關內容
	    mybatisDao.update("xqgl.updateRMS", edit_rms_params);
	    // 刪除附件表相關內容
	    mybatisDao.delete("xqgl.deleteDBAttach", edit_rms_params);
	    // 更改待辦
	    if (status.equalsIgnoreCase("rms_submit") || status == "rms_submit") {
		if (subject_code.equals("yktl")) {
		    edit_rms_params.put("node_id", 3);
		    mybatisDao.update("xqgl.updateNeedDeal", edit_rms_params);
		} else if (subject_code.equals("zdtl")) {
		    edit_rms_params.put("node_id", 4);
		    mybatisDao.update("xqgl.updateNeedDeal", edit_rms_params);
		} else if (subject_code.equals("yjk")) {
		    edit_rms_params.put("node_id", 5);
		    mybatisDao.update("xqgl.updateNeedDeal", edit_rms_params);
		}

	    }

	}
	if (attachURL.length > 0) {
	    addDBAttach(rms_code, attachURL);
	}
	// logger.debug(result);

	// return CommonResult.success(result);
    }

    /**
     * <pre>
     * Desc插入附件路径到数据库中
     * @param rms_code
     * @param attachURL
     * @author feihu
     * Jan 9, 2017 5:50:04 PM
     * </pre>
     */
    public void addDBAttach(String rms_code, String[] attachURL) {
	for (String sourceFile : attachURL) {
	    Map<String, Object> params = new HashMap<String, Object>();
	    String FTPFile = sourceFile.substring(sourceFile.lastIndexOf('\\') + 1, sourceFile.length());
	    params.put("attachURL", FTPFile);
	    params.put("rms_code", rms_code);
	    params.put("attach_id", ((Math.random() * 9 + 1) * 100000));
	    mybatisDao.add("xqgl.addAttach", params);
	}
    }

    /**
     * <pre>
     * Desc  插入待办信息
     * @param user_id
     * @param user_name
     * @param rms_code
     * @param node_id
     * @author feihu
     * Dec 28, 2016 3:35:42 PM
     * </pre>
     */
    public void addNeadDeal(String user_id, String rms_code, int node_id) {

	Map<String, Object> needDealParams = new HashMap<String, Object>();
	needDealParams.put("need_deal_id", ((int) ((Math.random() * 9 + 1) * 10000)));
	needDealParams.put("user_id", user_id);
	needDealParams.put("rms_code", rms_code);
	needDealParams.put("node_id", node_id);
	mybatisDao.add("xqgl.addNeedDeal", needDealParams);

    }

    /**
     *
     * <pre>
     * Desc 字符编码转换
     * @param sourceString
     * @return
     * @author feihu
     * Dec 27, 2016 7:23:26 PM
     * </pre>
     */
    public String changeCode(String[] sourceString) {
	String returnString = "";
	try {
	    if (sourceString != null && sourceString.length > 0) {
		for (int i = 0; i < sourceString.length; i++) {
		    sourceString[i] = new String(sourceString[i].getBytes("ISO-8859-1"), "UTF-8");
		}
		returnString = StringUtils.join(sourceString, ";");
	    }
	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    logger.error(e.getMessage(), e);
	}
	return returnString;

    }

    /**
     *
     * <pre>
     * Desc上传附件
     * @param file
     * @param prov_id
     * @param request
     * @param response
     * @return
     * @author feihu
     * Feb 21, 2017 2:34:35 PM
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "uploadHeadPic", produces = "plain/text; charset=UTF-8")
    public String uploadHeadPic(@RequestParam("file") MultipartFile file, String prov_id, HttpServletRequest request, HttpServletResponse response) {
	try {
	    // baseFileService=new BaseFileService();

	    // String destMyDir=destDir+prov_id+"/";
	    baseFileService.upload(file, prov_id, request);
	    response.getWriter().print(baseFileService.getFileName());
	} catch (Exception e) {
	    e.printStackTrace();
	    logger.error(e.getMessage(), e);
	}
	return null;
    }

    /**
     *
     * <pre>
     * Desc下载附件
     * @param request
     * @param rms_code
     * @return
     * @author feihu
     * Feb 21, 2017 11:17:06 AM
     * </pre>
     *
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "downLoadAttach", produces = "plain/text; charset=UTF-8")
    public String downLoadAttach(HttpServletRequest request, HttpServletResponse response, String rms_code, String attachName) throws IOException {
	String rms_provid = rms_code.substring(0, rms_code.indexOf('_'));
	// http://127.0.0.1:7001/hp/data/201606/3/3000/终端套利（05004终端销售服务费用信息入库情况）201606.xlsx
	// attachName="remind.txt";
	String dlpath = baseFileService.getftpHttpUrlPrefix() + rms_provid + "/" + attachName;
	logger.debug("######  当前下载文件URL:" + dlpath);
	String fileName = dlpath.substring(dlpath.lastIndexOf("/") + 1, dlpath.length());
	logger.debug("######  当前下载文件名:" + attachName);

	FileUtil.downFileByHttp(request,response, dlpath, fileName, logger);

	return null;
    }

    /**
     *
     * <pre>
     * Desc  更新待阅
     * @param request
     * @param rms_code
     * @param user_id
     * @return
     * @author feihu
     * Jan 8, 2017 6:15:07 PM
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "updateReadStauts", produces = "plain/text; charset=UTF-8")
    public String updateReadStauts(HttpServletRequest request, String rms_code, String user_id) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("rms_code", rms_code);
	params.put("user_id", user_id);
	params.put("status", '1');
	List<Map<String, Object>> result = mybatisDao.getList("xqgl.updateReadStauts", params);
	return CommonResult.success(result);
    }

    /**
     *
     * <pre>
     * Desc  需求统计-环形图
     * @param name
     * @param subject_code
     * @param rms_type
     * @param relationTime
     * @param content
     * @author xuwenhu
     * 2016-12-28 下午3:44:01
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getRMSPie", produces = "plain/text; charset=UTF-8")
    public String getRMSPie(String prvdId) {// 10000--集团/厂商角色的用户 对应省份id----省份审计部门的用户
	Map<String, Object> result = new HashMap<String, Object>();
	Map<String, Object> params = new HashMap<String, Object>();
	List<Map<String, Object>> subjects = new ArrayList<Map<String, Object>>();
	List<String> subs = new ArrayList<String>();
	params.put("prvd_id", prvdId);
	List<Map<String, Object>> info = mybatisDao.getList("xqgl.selectRMSPie", params);
	for (Map<String, Object> m : info) {
	    Map<String, Object> subItems = new HashMap<String, Object>();
	    String sub = m.get("subject_code").toString();
	    if (!subs.contains(sub)) {
		subs.add(sub);
		subItems.put("subject", sub);
		subItems.put("type_0", "0");// 临时
		subItems.put("type_1", "0");// 长期
	    } else {
		subItems = subjects.get(subs.indexOf(sub));
	    }
	    // 临时：0;长期：1 求临时/总
	    if ("0".equals(m.get("rms_type").toString())) {
		subItems.put("type_0", m.get("quantity").toString());// 临时
	    } else if ("1".equals(m.get("rms_type").toString())) {
		subItems.put("type_1", m.get("quantity").toString());// 长期
	    }
	    if (!subjects.contains(subItems)) {
		subjects.add(subItems);
	    }
	}
	result.put("subjects", subjects);// 五个专题 如果有个专题没数据？？
	return CommonResult.success(result);
    }

    /**
     *
     * <pre>
     * Desc
     * @param prvdId
     * @param year
     * @return
     * @author xuwenhu
     * 2016-12-29 下午2:21:04
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getStackedColumn", produces = "plain/text; charset=UTF-8")
    public String getStackedColumn(String prvdId, String year) {
	Map<String, Object> result = new HashMap<String, Object>();
	List<String> categories = new ArrayList<String>();
	String[] prvIds = new String[] { "10000", "10100", "10200", "10300", "10400", "10500", "10600", "10700", "10800", "10900", "11000", "11100", "11200", "11300", "11400", "11500", "11600",
		"11700", "11800", "11900", "12000", "12100", "12200", "12300", "12400", "12500", "12600", "12700", "12800", "12900", "13000", "13100" };
	if (prvdId.equals("10000")) {
	    for (int i = 0; i < prvIds.length; i++) {
		String prvNm = "10000".equals(prvIds[i]) ? "内审部" : getCompanyNameOfProvince(prvIds[i]).substring(0, getCompanyNameOfProvince(prvIds[i]).length() - 2);
		categories.add(prvNm);
	    }
	} else {
	    categories.add(getCompanyNameOfProvince(prvdId).substring(0, getCompanyNameOfProvince(prvdId).length() - 2));
	}
	List<Object> series = new ArrayList<Object>();
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("prvd_id", prvdId);
	params.put("year", year);
	Integer n = prvdId.equals("10000") ? 32 : 1;// 显示的省份个数
	List<Map<String, Object>> infoList = mybatisDao.getList("xqgl.getStackedColumn", params);
	// 临时
	ArrayList<Object> data_05 = new ArrayList<Object>();
	for (int i = 0; i < n; i++) {
	    data_05.add(0);
	}
	ArrayList<Object> data_04 = new ArrayList<Object>();
	for (int i = 0; i < n; i++) {
	    data_04.add(0);
	}
	ArrayList<Object> data_01 = new ArrayList<Object>();
	for (int i = 0; i < n; i++) {
	    data_01.add(0);
	}
	ArrayList<Object> data_03 = new ArrayList<Object>();
	for (int i = 0; i < n; i++) {
	    data_03.add(0);
	}
	ArrayList<Object> data_02 = new ArrayList<Object>();
	for (int i = 0; i < n; i++) {
	    data_02.add(0);
	}
	// 长期
	ArrayList<Object> data_15 = new ArrayList<Object>();
	for (int i = 0; i < n; i++) {
	    data_15.add(0);
	}
	ArrayList<Object> data_14 = new ArrayList<Object>();
	for (int i = 0; i < n; i++) {
	    data_14.add(0);
	}
	ArrayList<Object> data_11 = new ArrayList<Object>();
	for (int i = 0; i < n; i++) {
	    data_11.add(0);
	}
	ArrayList<Object> data_13 = new ArrayList<Object>();
	for (int i = 0; i < n; i++) {
	    data_13.add(0);
	}
	ArrayList<Object> data_12 = new ArrayList<Object>();
	for (int i = 0; i < n; i++) {
	    data_12.add(0);
	}
	for (Map<String, Object> info : infoList) {
	    String p = info.get("prvd_id").toString();

	    String prvdNm = "10000".equals(p) ? "内审部" : getCompanyNameOfProvince(p).substring(0, getCompanyNameOfProvince(p).length() - 2);
	    // if (!categories.contains(prvdNm)) {// 省份公司登录 显示对应省份的柱图(对应一条省份记录)
	    // categories.add(prvdNm);
	    // }
	    Integer index = prvdId.equals("10000") ? categories.indexOf(prvdNm) : 0;
	    if ("0".equals(info.get("rms_type").toString())) {
		if ("yjk".equals(info.get("subject_code").toString())) {
		    // info.get("quantity") == null||"".equals(info.get("quantity")) ? 0 :
		    data_01.set(index, info.get("quantity"));
		} else if ("zdtl".equals(info.get("subject_code").toString())) {
		    data_03.set(index, info.get("quantity"));
		} else if ("yktl".equals(info.get("subject_code").toString())) {
		    data_02.set(index, info.get("quantity"));
		} else if ("khqf".equals(info.get("subject_code").toString())) {
		    data_04.set(index, info.get("quantity"));
		} else if ("yccz".equals(info.get("subject_code").toString())) {
		    data_05.set(index, info.get("quantity"));
		}
	    } else if ("1".equals(info.get("rms_type").toString())) {
		if ("yjk".equals(info.get("subject_code").toString())) {
		    data_11.set(index, info.get("quantity"));
		} else if ("zdtl".equals(info.get("subject_code").toString())) {
		    data_13.set(index, info.get("quantity"));
		} else if ("yktl".equals(info.get("subject_code").toString())) {
		    data_12.set(index, info.get("quantity"));
		} else if ("khqf".equals(info.get("subject_code").toString())) {
		    data_14.set(index, info.get("quantity"));
		} else if ("yccz".equals(info.get("subject_code").toString())) {
		    data_15.set(index, info.get("quantity"));
		}
	    }
	    // 加10个map对象： name: '临时-客户欠费',
	    // data: [5, 3, 4, 7, 2],
	    // stack: '0'
	}
	Map<String, Object> map_05 = new HashMap<String, Object>();
	map_05.put("name", "临时-员工异常操作");
	map_05.put("stack", "0");
	map_05.put("data", data_05);
	series.add(0, map_05);

	Map<String, Object> map_04 = new HashMap<String, Object>();
	map_04.put("name", "临时-客户欠费");
	map_04.put("stack", "0");
	map_04.put("data", data_04);
	series.add(1, map_04);

	Map<String, Object> map_01 = new HashMap<String, Object>();
	map_01.put("name", "临时-有价卡管理违规");
	map_01.put("stack", "0");
	map_01.put("data", data_01);
	series.add(2, map_01);

	Map<String, Object> map_03 = new HashMap<String, Object>();
	map_03.put("name", "临时-终端套利");
	map_03.put("stack", "0");
	map_03.put("data", data_03);
	series.add(3, map_03);

	Map<String, Object> map_02 = new HashMap<String, Object>();
	map_02.put("name", "临时-养卡套利");
	map_02.put("stack", "0");
	map_02.put("data", data_02);
	series.add(4, map_02);
	// 长期
	Map<String, Object> map_15 = new HashMap<String, Object>();
	map_15.put("name", "长期-员工异常操作");
	map_15.put("stack", "1");
	map_15.put("data", data_15);
	series.add(5, map_15);

	Map<String, Object> map_14 = new HashMap<String, Object>();
	map_14.put("name", "长期-客户欠费");
	map_14.put("stack", "1");
	map_14.put("data", data_14);
	series.add(6, map_14);

	Map<String, Object> map_11 = new HashMap<String, Object>();
	map_11.put("name", "长期-有价卡管理违规");
	map_11.put("stack", "1");
	map_11.put("data", data_11);
	series.add(7, map_11);

	Map<String, Object> map_13 = new HashMap<String, Object>();
	map_13.put("name", "长期-终端套利");
	map_13.put("stack", "1");
	map_13.put("data", data_13);
	series.add(8, map_13);

	Map<String, Object> map_12 = new HashMap<String, Object>();
	map_12.put("name", "长期-养卡套利");
	map_12.put("stack", "1");
	map_12.put("data", data_12);
	series.add(9, map_12);

	result.put("categories", categories);
	result.put("series", series);
	return CommonResult.success(result);
    }

}
