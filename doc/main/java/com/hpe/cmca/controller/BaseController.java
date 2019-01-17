package com.hpe.cmca.controller;

import com.asiainfo.biframe.privilege.IUser;
import com.hpe.cmca.common.AsposeUtil;
import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.common.CommonResult;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.AudTrmMapper;
import com.hpe.cmca.interfaces.UserBehavMapper;
import com.hpe.cmca.pojo.LoginData;
import com.hpe.cmca.service.*;
import com.hpe.cmca.util.ExceptionTool;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.Json;
import com.hpe.cmca.util.ZipFileUtil;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/base/")
public class BaseController extends BaseObject {

    @Autowired
    protected MybatisDao mybatisDao;

    @Autowired
    protected ConcernFileGenService concernFileGenService;

    @Autowired
    protected ConcernService concernService;

    @Autowired
    SSOService ssoService;

    @Autowired
    SystemLogMgService systemLogMgService;

    @Autowired
    CompareTagService compareTagService;

    @Autowired
    AsposeUtil asposeUtil;

    @ResponseBody
    @RequestMapping(value = "/getPrvdAndAudTrmInfoData", produces = "text/json;charset=UTF-8")
    public String getPrvdAndAudTrmInfoData(HttpServletRequest request, int prvdId, String subjectId) {

        Map<String, List> map = new HashMap<String, List>();
        map.put("prvdInfo", getPrvdInfoData(prvdId));
        map.put("audTrmInfo", getAudTrmData(subjectId, getSwitchStateByDepId(request)));
        return Json.Encode(map);

    }

    /**
     * <pre>
     * Desc 审计开关-系统页面-控制专用
     * 不包括：审计报告-控制
     * &#64;param request
     * &#64;return
     * &#64;author hufei
     * 2018-1-16 上午11:10:47
     * </pre>
     */
    int getSwitchStateByDepId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("userId") == null) {
            session.setAttribute("depId", 10000);
        }
        Object depId = session.getAttribute("depId");
        int authorityAttr = 1;
        if (Integer.parseInt(depId.toString()) == 10009 || Integer.parseInt(depId.toString()) == 12
                || Integer.parseInt(depId.toString()) == 18) {
            authorityAttr = 1;
        }
        if ((Integer.parseInt(depId.toString()) <= 193 && Integer.parseInt(depId.toString()) >= 163)) {
            authorityAttr = 2;
        }
        return authorityAttr;
    }

    /**
     * <pre>
     * Desc
     * &#64;param request:请求
     * &#64;param audTrm：审计月
     * &#64;param subjectId：专题
     * &#64;return
     * &#64;author hufei
     * 2018-1-16 下午2:15:38
     * </pre>
     */
    public boolean getAttributeByAudTrmAndUser(HttpServletRequest request, String audTrm, String subjectId) {
        int switchState = getSwitchStateByDepIdForReport(request);
        Map<String, Object> map = new HashMap<String, Object>(3);
        map.put("audTrm", audTrm);
        map.put("subjectId", subjectId);
        map.put("switchState", switchState);
        AudTrmMapper audTrmMapper = mybatisDao.getSqlSession().getMapper(AudTrmMapper.class);
        Map<String, Object> resultMap = audTrmMapper.getAttributeByAudTrmAndUser(map);
        if (resultMap == null || resultMap.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * <pre>
     * Desc 获取审计开关-报告权限
     * &#64;param request
     * &#64;return
     * &#64;author hufei
     * 2018-1-16 上午11:30:57
     * </pre>
     */
    private int getSwitchStateByDepIdForReport(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("userId") == null) {
            session.setAttribute("depId", 10000);
        }
        Object depId = session.getAttribute("depId");
        int authorityAttr = 0;
        if (Integer.parseInt(depId.toString()) == 10009 || Integer.parseInt(depId.toString()) == 12) {
            authorityAttr = 0;
        }
        if (Integer.parseInt(depId.toString()) == 18) {
            authorityAttr = 1;
        }
        if ((Integer.parseInt(depId.toString()) <= 193 && Integer.parseInt(depId.toString()) >= 163)) {
            authorityAttr = 2;
        }
        return authorityAttr;
    }

    @ResponseBody
    @RequestMapping(value = "/getAudTrmDataTrmConf", produces = "text/json;charset=UTF-8")
    public String getAudTrmDataTrmConf(int prvdId, String subjectId, String roleId) {
    	
        Map<String, List> map = new HashMap<String, List>();
        List<Map<String,Object>> audTrmDataTrmConf = getAudTrmDataTrmConf(subjectId, roleId);
        if("".equals(audTrmDataTrmConf)||audTrmDataTrmConf==null){
        	List<Object> list = new ArrayList<>();
        	return Json.Encode(list);
        }
        map.put("prvdInfo", getPrvdInfoData(prvdId));
        if (roleId != null) {
            map.put("audTrmInfo", audTrmDataTrmConf);
        }
        return Json.Encode(map);

    }

    @ResponseBody
    @RequestMapping(value = "getCurrentDate", produces = "plain/text; charset=UTF-8")
    protected String getCurrentDate() {
        return DateFormatUtils.format(new Date(), "yyyyMMdd");
    }

    /**
     * <pre>
     * Desc  获取request中的参数
     * &#64;param request
     * &#64;return
     * &#64;author
     * &#64;refactor
     * &#64;date   2016-9-19 下午4:09:15
     * </pre>
     */
    protected Map<String, Object> getParameterMap(HttpServletRequest request) {
        Map<String, Object> pathMap = new HashMap<String, Object>(20);
        Enumeration<?> enum_term = request.getParameterNames();
        while (enum_term.hasMoreElements()) {
            String paramName = (String) enum_term.nextElement();
            if (request.getParameterValues(paramName).length > 1) {
                pathMap.put(paramName.replace("[]", ""), request.getParameterValues(paramName));
            } else {
                pathMap.put(paramName, request.getParameter(paramName));
            }
        }
        return pathMap;
    }

    /**
     * <pre>
     * Desc  根据省份ID获取地市列表
     * &#64;param prvdId
     * &#64;return
     * &#64;author GuoXY
     * &#64;refactor GuoXY
     * &#64;date   Sep 21, 2016 10:29:38 AM
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getCityList", produces = "plain/text; charset=UTF-8")
    public String getCityList(String prvdId) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("provinceCode", prvdId);
        List<Map<String, Object>> list = mybatisDao.getList("commonMapper.selectCityList", params);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("ctyList", list);

        return CommonResult.success(result);
    }

    /**
     * <pre>
     * Desc  审计报告 | 明细 下载
     * &#64;param response
     * &#64;param request
     * &#64;param audtrm : 审计周期
     * &#64;param subjectId : 专题ID
     * &#64;param prvdId : 省份ID(全公司为10000)
     * &#64;param ext : 扩展名(报告-doc,明细-csv)
     * &#64;author GuoXY
     * &#64;refactor GuoXY
     * &#64;date   Sep 27, 2016 5:56:22 PM
     * </pre>
     */
    @RequestMapping(value = "downfiles", produces = "plain/text; charset=UTF-8")
    public void downfiles(HttpServletResponse response, HttpServletRequest request, String audTrm, String subjectId,
                          String prvdId, String fileType) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("prvdId", prvdId);
        params.put("audTrm", audTrm);
        params.put("subjectId", subjectId);
        params.put("fileType", fileType);
        // if ((null != ext) && ext.equals("doc")) {
        // params.put("fileType", "audReport");
        // }else if ((null != ext) && ext.equals("csv")) {
        // params.put("fileType", "audDetail");
        // }
        logger.debug("######  开始下载的审计报告文件|清单:" + params);
        List<Map<String, Object>> list = mybatisDao.getList("commonMapper.selectAudReportFile", params);
        if (0 == list.size()) {
            logger.debug("######  从数据库中未查询到要下载的文件记录:" + params);
        } else {
            try {
                Map<String, Object> downLoadFile = list.get(0);
                // 文件是存放在一个支持'ftp上传|http下载'的ftp服务器
                String dlpath = (String) downLoadFile.get("download_url");
                logger.debug("######  当前下载审计报告文件|清单URL:" + dlpath);
                String fileName = dlpath.substring(dlpath.lastIndexOf("/") + 1, dlpath.length());
                logger.debug("######  当前下载审计报告文件|清单文件名:" + fileName);
                FileUtil.downFileByHttp(request,response, dlpath, fileName, logger);
            } catch (Exception e) {
                logger.error("######  下载异常审计报告文件|清单文件:" + ExceptionTool.getExceptionDescription(e));
            }
        }
        logger.debug("######  完成下载的审计报告文件|清单:" + params);
    }

    /**
     * <pre>
     * Desc  从url中获取文件名
     * &#64;param url
     * &#64;return
     * &#64;author GuoXY
     * &#64;refactor GuoXY
     * &#64;date   Sep 27, 2016 6:00:21 PM
     * </pre>
     */
    private String getFileName(String url) {
        String[] nameArr = url.split("\\/");
        return nameArr[nameArr.length - 1];
    }

    /**
     * 获取用户的省份信息
     *
     * <pre>
     * Desc
     * &#64;param request
     * &#64;return
     * &#64;author sinly
     * &#64;refactor sinly
     * &#64;date   2016-9-29 下午3:15:00
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getUserPrvdId", produces = "plain/text; charset=UTF-8")
    public String getUserPrvdId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userPrvdId = (String) session.getAttribute("userPrvdId");
        return Json.Encode(userPrvdId == null ? "10000" : userPrvdId);
    }

    /**
     * <pre>
     * Desc  获取开关表中三个专题的开关打开的审计月列表
     * &#64;param request
     * &#64;author sinly
     * &#64;refactor sinly
     * &#64;date   2016-9-29 上午11:27:26
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getAudTrm", produces = "plain/text; charset=UTF-8")
    public String getAudTrm(HttpServletRequest request) {
        List<Map<String, String>> audTrmList = mybatisDao.getList("commonMapper.dict_auditTrm_conf");
        List<String> qdykAudTrmList = new ArrayList<String>();
        List<String> zdtlAudTrmList = new ArrayList<String>();
        List<String> yjkAudTrmList = new ArrayList<String>();
        for (Map<String, String> map : audTrmList) {
            if (map.get("subjectCode").equals("1"))
                yjkAudTrmList.add(map.get("audTrm"));
            if (map.get("subjectCode").equals("2"))
                qdykAudTrmList.add(map.get("audTrm"));
            if (map.get("subjectCode").equals("3"))
                zdtlAudTrmList.add(map.get("audTrm"));
        }

        Map<String, List<String>> audTrmMap = new HashMap<String, List<String>>();
        audTrmMap.put("yjkAudTrmList", yjkAudTrmList);
        audTrmMap.put("qdykAudTrmList", qdykAudTrmList);
        audTrmMap.put("zdtlAudTrmList", zdtlAudTrmList);
        return Json.Encode(audTrmMap);
    }

    /**
     * 从session中获取当前登录用户的权限MAP
     *
     * <pre>
     * Desc
     * &#64;param request
     * &#64;return
     * &#64;author sinly
     * &#64;refactor sinly
     * &#64;date   2016-9-29 上午11:28:57
     * </pre>
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getUserRight", produces = "plain/text; charset=UTF-8")
    public Map<String, Boolean> getUserRight(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Boolean> rightMap = new HashMap<String, Boolean>();
        Map<String, Object> result = mybatisDao.get("home.getPermission", 1);

        Object rightMapObject = session.getAttribute("rightMap");
        if ((Integer) result.get("status") == 0) {
            rightMap.put("qdyksjjg", true);// 审计专题：养卡-审计结果
            rightMap.put("khqfsjjg", true);// 审计专题：欠费-审计结果
            rightMap.put("yjk_sjjg", true);// 审计专题：有价卡-审计结果
            rightMap.put("zdtlsjjg", true);// 审计专题：社渠终端套利-审计结果
            rightMap.put("ygycsjjg", true);// 审计专题：员工异常操作-审计结果
            rightMap.put("qdykpmhz", true);// 审计专题：养卡-统计分析-统计报表-排名汇总
            rightMap.put("khqfpmhz", true);// 审计专题：欠费-统计分析-统计报表-排名汇总
            rightMap.put("yjk_pmhz", true);// 审计专题：有价卡-统计分析-统计报表-排名汇总
            rightMap.put("zdtlpmhz", true);// 审计专题：社渠终端套利-统计分析-统计报表-排名汇总
            rightMap.put("ygycpmhz", true);// 审计专题：员工异常操作-统计分析-统计报表-排名汇总
            rightMap.put("qdykqtcd", true);// 审计专题：养卡-统计分析-统计报表下其他菜单
            rightMap.put("khqfqtcd", true);// 审计专题：欠费-统计分析-统计报表下其他菜单
            rightMap.put("yjk_qtcd", true);// 审计专题：有价卡-统计分析-统计报表下其他菜单
            rightMap.put("zdtlqtcd", true);// 审计专题：社渠终端套利-统计分析-统计报表下其他菜单
            rightMap.put("ygycqtcd", true);// 审计专题：员工异常操作-统计分析-统计报表下其他菜单
            rightMap.put("qdykzgwz", true);// 审计专题：养卡-统计分析-审计整改问责
            rightMap.put("khqfzgwz", true);// 审计专题：欠费-统计分析-审计整改问责
            rightMap.put("yjk_zgwz", true);// 审计专题：有价卡-统计分析-审计整改问责
            rightMap.put("zdtlzgwz", true);// 审计专题：社渠终端套利-统计分析-审计整改问责
            rightMap.put("ygyczgwz", true);// 审计专题：员工异常操作-统计分析-审计整改问责
            rightMap.put("qdykwjxz", true);// 审计专题：养卡-统计分析-审计报告、审计报告下载、清单下载
            rightMap.put("khqfwjxz", true);// 审计专题：欠费-统计分析-审计报告、审计报告下载、清单下载
            rightMap.put("yjk_wjxz", true);// 审计专题：有价卡-统计分析-审计报告、审计报告下载、清单下载
            rightMap.put("zdtlwjxz", true);// 审计专题：社渠终端套利-统计分析-审计报告、审计报告下载、清单下载
            rightMap.put("ygycwjxz", true);// 审计专题：员工异常操作-统计分析-审计报告、审计报告下载、清单下载
            rightMap.put("cxsjpmhz", true);// 排名汇总-单独功能模块
            rightMap.put("cxsjsjbg", true);// 审计报告-单独功能模块
            rightMap.put("cxsjsjzl", true);// 数据质量-当前已有功能
            rightMap.put("cxsjywjk", true);// 业务监控
            rightMap.put("qdyksjzl", true);// 数据质量：养卡-数据质量
            rightMap.put("khqfsjzl", true);// 数据质量：欠费-数据质量
            rightMap.put("yjk_sjzl", true);// 数据质量：有价卡-数据质量
            rightMap.put("zdtlsjzl", true);// 数据质量：社渠终端套利-数据质量
            rightMap.put("ygycsjzl", true);// 数据质量：员工异常操作-数据质量
            rightMap.put("cxsjsjkg", true);// 审计开关-一级
            rightMap.put("cxsjcsgl", true);// 参数管理
            rightMap.put("cxsjmxjk", true);// 模型监控
            rightMap.put("cxsjrzcx", true);// 日志查询
            rightMap.put("cxsjdxgj", true);// 短信告警
            rightMap.put("cxsjsjjh", true);// 审计结果稽核自动化
            rightMap.put("cxsjsjtb", true);// 审计通报模板管理
            rightMap.put("cxsjzgwz", true);// 整改问责
            rightMap.put("cxsjcxjf", true);// 诚信积分化管理
            rightMap.put("cxsjxqgl", true);// 需求管理
            rightMap.put("cxsjsjts", true);// 数据探索
            rightMap.put("llglsjjg", true);
            rightMap.put("llglpmhz", true);
            rightMap.put("llglqtcd", true);
            rightMap.put("llglzgwz", true);
            rightMap.put("llglsjbg", true);
            rightMap.put("dzqsjjg", true);
            rightMap.put("dzqpmhz", true);
            rightMap.put("dzqqtcd", true);
            rightMap.put("dzqzgwz", true);
            rightMap.put("dzqsjbg", true);
            rightMap.put("cxsjsjgj", true);// 审计跟进
            rightMap.put("cxsjqxcx", true);// 权限查询
            rightMap.put("cxsjsjrw", true);//个人审计
            rightMap.put("cxsjwtfk", true);//问题反馈

        } else
            rightMap = (Map<String, Boolean>) rightMapObject;
        return rightMap;
    }

    /**
     * @param @param  request
     * @param @return
     * @return Map<String       ,       Boolean>
     * @throws @author ZhangQiang
     * @Description: TODO
     * @date 2018-8-15
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getUserRzcxRight", produces = "plain/text; charset=UTF-8")
    public Boolean getUserRzcxRight(HttpServletRequest request) {

        return systemLogMgService.getUserRzcxRight(request);
    }

    /***
     *
     * <pre>
     * Desc  模拟权限测试
     * &#64;param request
     * &#64;return
     * &#64;author sinly
     * &#64;refactor sinly
     * &#64;date   2016-11-3 下午2:51:35
     * </pre>
     */
    @RequestMapping(value = "getUserRightTest", produces = "plain/text; charset=UTF-8")
    public Map<String, Boolean> getUserRightTest(HttpServletRequest request) {

        // HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        Map<String, Boolean> rightMap = new HashMap<String, Boolean>();
        rightMap.put("sjzt", true);
        rightMap.put("qdyk", true);
        rightMap.put("yjk", true);
        rightMap.put("zdtl", true);
        rightMap.put("khqf", true);
        rightMap.put("yccz", true);
        rightMap.put("sjbg", true);
        rightMap.put("sjts", true);
        rightMap.put("pmhz", true);
        rightMap.put("sjzl", true);
        rightMap.put("sjgl", true);
        rightMap.put("csgl", true);
        rightMap.put("mxjk", true);
        rightMap.put("sjkg", true);
        rightMap.put("rzcx", true);
        return rightMap;
    }

    /**
     * <pre>
     * Desc  根据专题ID获取相应关注点列表
     * &#64;param subjectId
     * &#64;return
     * &#64;author GuoXY
     * &#64;refactor GuoXY
     * &#64;date   Dec 19, 2016 7:34:08 AM
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getFouscdsList", produces = "plain/text; charset=UTF-8")
    public String getFouscdsList(String subjectId) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("subjectId", subjectId);
        List<Map<String, Object>> list = mybatisDao.getList("commonMapper.getFocusBySubjectId", params);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("fouscdsList", list);

        return CommonResult.success(result);
    }

    /**
     * <pre>
     * Desc  用于各模块获取省份列表
     * &#64;param prvdIds
     * &#64;return
     * &#64;author GuoXY
     * &#64;refactor GuoXY
     * &#64;date   Dec 19, 2016 5:44:40 PM
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getPrvdList", produces = "plain/text; charset=UTF-8")
    public String getPrvdList(String prvdIds) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        result.put("prvdIdStr", prvdIds);

        List<Map<String, Object>> prvdList = mybatisDao.getList("commonMapper.getProvIdName", params);

        result.put("prvdList", prvdList);
        return CommonResult.success(result);
    }

    @ResponseBody
    @RequestMapping(value = "getUserName", produces = "text/json; charset=UTF-8")
    public String getUserName(HttpServletRequest request, String flag) {
        IUser user = (IUser) request.getSession().getAttribute("ssoUSER");
        Map<String, Object> result = new HashMap<String, Object>();
        // HttpSession session = request.getSession();
        String userName = user.getUsername();
        // result.put("isupload", "false");
        // result.put("isreview", "false");
        // BgxzMapper bgxzMapper =
        // mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
        // List<Map<String,Object>> dataList = bgxzMapper.selReportPerson(null);
        // if(dataList.size() > 0){
        // for(Map<String,Object> d :dataList){
        // if(d.get("user_id").toString().toUpperCase().equals(user.getUserid().toUpperCase())
        // &&d.get("flag").equals("upload")){
        // result.put("isupload", "true");
        // }
        // if(d.get("user_id").toString().toUpperCase().equals(user.getUserid().toUpperCase())
        // &&d.get("flag").equals("review")){
        // result.put("isreview", "true");
        // }
        //
        // }
        // }
        result.put("userName", userName);
        return Json.Encode(result);
    }

    @ResponseBody
    @RequestMapping(value = "getLoginTimes", produces = "text/json; charset=UTF-8")
    public String getLoginTimes(HttpServletRequest request) {

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        LoginData loginData = new LoginData();
        loginData.setUserId(userId);
        List<LoginData> l = ssoService.getLoginInfo(loginData);
        return Json.Encode(l.get(0).getLoginTimes());
    }

    @ResponseBody
    @RequestMapping(value = "getAnnouncement", produces = "text/json; charset=UTF-8")
    public String getAnnouncement(HttpServletRequest request) {
        List<Map<String, String>> announcement = ssoService.getAnnouncement();
        return Json.Encode(announcement);
    }

    @ResponseBody
    @RequestMapping(value = "addUserBehav", produces = "text/json; charset=UTF-8")
    public String addUserBehav(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");
        String userPrvdId = (String) session.getAttribute("userPrvdId");
        String depName = (String) session.getAttribute("depName");

        Map<String, Object> userBehavData = new HashMap<String, Object>();
        userBehavData.put("user_id", userId);
        userBehavData.put("user_nm", userName);
        userBehavData.put("user_prvd_id", userPrvdId);
        userBehavData.put("dep_nm", depName);

        // 添加对参数的转义 2018-8-15 15:13:13 zhangqiang
        String behav_lv1 = request.getParameter("behav_lv1");
        String behav_lv2 = request.getParameter("behav_lv2");
        String behav_lv3 = request.getParameter("behav_lv3");
        String behav_typ = request.getParameter("behav_typ");

        // 前段传入的请求编码
        String behav_id = request.getParameter("behavId");
        // 返回客户端的ip地址
        String client_address = request.getRemoteAddr();
        // 模块路径url
        String resource_url = request.getHeader("Referer");

        // 获取服务端的ip地址
        String server_address = request.getLocalAddr();
        // 浏览器基本信息
        String other = request.getHeader("User-Agent");

        if ("".equals(behav_lv1) || behav_lv1 == null)
            behav_lv1 = null;
        behav_lv1 = this.getStringOfEncod(behav_lv1);
        if ("".equals(behav_lv2) || behav_lv2 == null)
            behav_lv2 = null;
        behav_lv2 = this.getStringOfEncod(behav_lv2);
        if ("".equals(behav_lv3) || behav_lv3 == null)
            behav_lv3 = null;
        behav_lv3 = this.getStringOfEncod(behav_lv3);
        if ("".equals(behav_typ) || behav_typ == null)
            behav_typ = null;
        behav_typ = this.getStringOfEncod(behav_typ);
        if ("".equals(other) || other == null)
            other = null;
        other = this.getStringOfEncod(other);
        if ("".equals(behav_id) || behav_id == null)
            behav_id = null;
        behav_id = this.getStringOfEncod(behav_id);
        if ("".equals(client_address) || client_address == null)
            client_address = null;
        client_address = this.getStringOfEncod(client_address);
        if ("".equals(resource_url) || resource_url == null)
            resource_url = null;
        resource_url = this.getStringOfEncod(resource_url);
        if ("".equals(server_address) || server_address == null)
            server_address = null;
        server_address = this.getStringOfEncod(server_address);
        userBehavData.put("behav_lv1", behav_lv1);
        userBehavData.put("behav_lv2", behav_lv2);
        userBehavData.put("behav_lv3", behav_lv3);
        userBehavData.put("behav_typ", behav_typ);
        userBehavData.put("other", other);
        userBehavData.put("behav_id", behav_id);
        userBehavData.put("client_address", client_address);
        userBehavData.put("resource_url", resource_url);
        userBehavData.put("server_address", server_address);

        // 2018-8-15 15:08:08 update by zhangqiang
        /*
         * SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         * Calendar rightNow = Calendar.getInstance(); try {
         * userBehavData.put("behav_time",dd.parse(dd.format(rightNow.getTime())
         * )); } catch (ParseException e) { // TODO Auto-generated catch block
         * e.printStackTrace(); }
         */
        // 2018-8-15 15:08:08 update by zhangqiang
        UserBehavMapper userBehavMapper = mybatisDao.getSqlSession().getMapper(UserBehavMapper.class);
        // Integer status = userBehavMapper.addUserBehav(userBehavData);

        /*
         * 2018-8-13 17:16:56 zhangqiang add LOG
         */
        Date date = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        userBehavData.put("save_time", formatDate.format(date.getTime()));
        IUser user = (IUser) request.getSession().getAttribute("ssoUSER");
        if (user == null) {
            userBehavData.put("user_id", "unknown");
            userBehavData.put("user_nm", "未登录");
        } else {
            userBehavData.put("dep_id", user.getDepartmentid());
        }

        Integer status2 = userBehavMapper.addUserBehavNew(userBehavData);

        return Json.Encode(status2);
    }

    /**
     * @param @param  str
     * @param @return
     * @return String
     * @throws @author ZhangQiang
     * @Description: 将字符串转义为普通字符串
     * @date 2018-8-15
     */
    public String getStringOfEncod(String str) {

        String keyWord = "";

        if (str == null) {
            return null;
        }
        // 对字符进行转义
        try {
            // 将application/x-www-from-urlencoded字符串转换成普通字符串
            keyWord = URLDecoder.decode(str, getEncoding(str));
            return keyWord;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param @param  str
     * @param @return
     * @return String
     * @throws @author ZhangQiang
     * @Description: 得到字符串的编码格式
     * @date 2018-8-15
     */
    public String getEncoding(String str) {

        String encoding = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(), encoding))) {
                return encoding;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        encoding = "GBK";
        try {
            if (str.equals(new String(str.getBytes(), encoding))) {
                return encoding;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        encoding = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(), encoding))) {
                return encoding;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        encoding = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(), encoding))) {
                return encoding;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    // @ResponseBody
    // @RequestMapping(value = "testDes", produces = "text/json; charset=UTF-8")
    // public String testDes(HttpServletRequest request) {
    // System.out.println("第一个测试方法*******");

    // String name ="hpca";
    // String password="hpca#123sj";
    // String name = StringUtils.trimToEmpty(propertyConfigurer
    // .getPropValue("jdbc.username"));
    // String password = StringUtils.trimToEmpty(propertyConfigurer
    // .getPropValue("jdbc.password"));
    // String name = StringUtils.trimToEmpty(propertyUtil
    // .getPropValue("dbc"));
    // String password = StringUtils.trimToEmpty(propertyUtil
    // .getPropValue("dbc"));
    // String name = "hp";
    // String password = "hp28#eap2";
    // String encryname = DESUtils.getEncryptString(name);
    // String encrypassword = DESUtils.getEncryptString(password);
    // System.out.println(encryname);
    // System.out.println(encrypassword);
    //
    // System.out.println(DESUtils.getDecryptString(encryname));
    // System.out.println(DESUtils.getDecryptString(encrypassword));
    // return
    // Json.Encode(encryname+"-"+encrypassword+"-"+DESUtils.getDecryptString(encryname)+"-"+DESUtils.getDecryptString(encrypassword));
    // }
    @ResponseBody
    @RequestMapping(value = "getWordHtml", produces = "text/json; charset=UTF-8")
    public String getWordHtml(String audTrm, String prvdId, String subjectId) throws IOException {
        try {
            String filePath = null;
            String fileName = null;
            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("subjectId", subjectId);
            paraMap.put("audTrm", audTrm);
            paraMap.put("prvdId", prvdId);
            List<Map<String, Object>> resultList = mybatisDao.getList("commonMapper.selectWordPath", paraMap);
            if (resultList.size() > 0) {
                filePath = resultList.get(0).get("file_path").toString();
                fileName = resultList.get(0).get("file_name").toString();
                File fileTarget = new File(getDocxOrDoc(propertyUtil.getPropValue("webShowFile") + "/" + fileName.replace("zip", "docx")));
                if (!fileTarget.exists()) {
                    File file = new File(propertyUtil.getPropValue("tempDirV2") + "/" + fileName);
                    if (!file.exists()) {
                        compareTagService.downloadFile(propertyUtil.getPropValue("webShowFile") + "/" + fileName, filePath, fileName);
                    } else {
                        Files.copy(Paths.get(propertyUtil.getPropValue("tempDirV2") + "/" + fileName), Paths.get(propertyUtil.getPropValue("webShowFile") + "/" + fileName), StandardCopyOption.REPLACE_EXISTING);
                    }
                    if ("10000".equals(prvdId)) {
                        File tpFile = new File(getDocxOrDoc(propertyUtil.getPropValue("webShowFile") + "/" + fileName.replace("zip", "docx")));
                        if (!tpFile.exists()) {
                            ZipFileUtil.unZip(propertyUtil.getPropValue("webShowFile") + "/" + fileName, propertyUtil.getPropValue("webShowFile"));
                        }
                    }
                }
                String path = asposeUtil.word2HtmlNoHandle(getDocxOrDoc(propertyUtil.getPropValue("webShowFile") + "/" + fileName.replace("zip", "docx")));
                Map<String,String> result =new HashMap<String,String>();
                result.put("path",path);
                return Json.Encode(result);

            }
        } catch (Exception e) {

            e.printStackTrace();
            logger.error("wordToHtmlError:"+e.getMessage());
        }
        Map<String,String> result =new HashMap<String,String>();
        result.put("path","0");
        return Json.Encode(result);
    }

    private String getDocxOrDoc(String path){//判断路径下有无该docx文件，有返回该文件路径无则返回doc文件
        File file = new File(path);
        if(file.exists()){
            return path;
        }else{
            return path.replace("docx","doc");
        }
    }

//    @ResponseBody
//    @RequestMapping(value = "getWordHtmlTxt", produces = "text/json; charset=UTF-8")
//    public String getWordHtmlTxt() throws IOException {
//        String resultTxt = asposeUtil.word2Html();
//        Map<String,String> result =new HashMap<String,String>();
//        result.put("txt",resultTxt);
//        return Json.Encode(result);
//    }

}
