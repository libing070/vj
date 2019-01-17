package com.hpe.cmca.controller;

/**
 * 通用
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.biframe.privilege.IUser;
import com.hpe.cmca.common.CommonResult;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.job.AbstractSJTBWordProcessor;
import com.hpe.cmca.job.SJBGKhqf4000WordProcessor;
import com.hpe.cmca.job.SJBGQdyk2000WordProcessor;
import com.hpe.cmca.job.SJBGZdtl3000WordProcessor;
import com.hpe.cmca.job.SJBGyjk1000WordProcessor;
import com.hpe.cmca.job.SJTBWordGenJob;
import com.hpe.cmca.job.v2.Qdyk2002FileGenProcessor;
import com.hpe.cmca.job.v2.SubjectFileGenJob;
import com.hpe.cmca.job.v2.Word0000FileGenProcessor;
import com.hpe.cmca.job.v2.Zdtl3000FileGenProcessor;
import com.hpe.cmca.service.SJTBWordService;
import com.hpe.cmca.util.JacksonJsonUtil;
import com.hpe.cmca.util.Json;

@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {

    @Autowired
    private MybatisDao mybatisDao;

	@Autowired
	SJTBWordGenJob sjtbJob;

	@Autowired
	SJTBWordService sjtbWordService;

	@Autowired
	SJBGQdyk2000WordProcessor sjtbQdykProcessor;

	@Autowired
	SJBGyjk1000WordProcessor sjtbYjkProcessor;

	@Autowired
	SJBGZdtl3000WordProcessor sjtbZdtlProcessor;

	@Autowired
	SJBGKhqf4000WordProcessor sjtbKhqfProcessor;

	@Autowired
	SubjectFileGenJob qf4000FileGenJobV2;

	@Autowired
	SubjectFileGenJob zdtlFileGenJobV2;

	@Autowired
	SubjectFileGenJob qdykFileGenJobV2;

	@Autowired
	SubjectFileGenJob yjkFileGenJobV2;

	@Autowired
	SubjectFileGenJob ygycFileGenJob;

//	@Autowired
//	SubjectFileGenJob llzsFileGenJob;

	@Autowired
	SubjectFileGenJob llzyFileGenJobV2;

	@Autowired
	SubjectFileGenJob dzj6000FileGenJobV2;

	@Autowired
	SubjectFileGenJob ksckFileGenJobV2;

	@Autowired
	SubjectFileGenJob xjkdFileGenJobV2;

	@Autowired
	SubjectFileGenJob xxaqFileGenJobV2;


	@Autowired
	private Word0000FileGenProcessor qF4000FileGenProcessorV2;

//	@Autowired
//	private QF4000FileGenProcessor qF4000FileGenProcessorV2;

	@Autowired
	private Zdtl3000FileGenProcessor zdtl3000FileGenProcessorV2;

	@Autowired
	private Qdyk2002FileGenProcessor qdyk2002FileGenProcessorV2;

//	@Autowired
//	private Yjk1000FileGenProcessor yjk1000FileGenProcessorV2;

	@Autowired
	private Word0000FileGenProcessor yjk1000FileGenProcessorV2;

	@Autowired
	private Word0000FileGenProcessor ygyc5000FileGenProcessor;

//	@Autowired
//	private DZJ6000FileGenProcessor dzj6000FileGenProcessorV2;

	@Autowired
	private Word0000FileGenProcessor dzj6000FileGenProcessorV2;

//	@Autowired
//	private Llzy7000FileGenProcessor llzy7000FileGenProcessor;

	@Autowired
	private Word0000FileGenProcessor llzy7000FileGenProcessor;

	@Autowired
	private Word0000FileGenProcessor ksck1100FileGenProcessorV2;

	@Autowired
	private Word0000FileGenProcessor xjkd1200FileGenProcessorV2;

	@Autowired
	private Word0000FileGenProcessor xxaq1300FileGenProcessorV2;


//	@Autowired
//	private Llzs8000FileGenProcessor llzs8000FileGenProcessor;

    @RequestMapping(value = "index")
    public String index() {
	return "common/index";
    }

    /**
     * <pre>
     * Desc  获取渠道基本信息
     * @param prvdId 省份
     * @param ctyId 地市
     * @param chnlId 渠道
     * @return
     * @author sinly
     * @refactor sinly
     * @date   2016-9-18 下午5:59:56
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getChnlBasisInfo", produces = "plain/text; charset=UTF-8")
    public String getChnlBasisInfo(String prvdId, String ctyId, String chnlId) {
	List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
	Map<String, Object> param = new HashMap<String, Object>();
	param.put("prvdId", prvdId);
	param.put("ctyId", ctyId);
	param.put("chnlId", chnlId);
	info = mybatisDao.getList("qdyk.chnlBasisInfo", param);
	return CommonResult.success(info);
    }

    /**
     *
     * <pre>
     * Desc  "月份下拉框"取值范围：从审计开关表中选择有效的月份
     * @param request
     * @return
     * @author GuoXY
     * @refactor GuoXY
     * @date   Oct 28, 2016 12:52:55 PM
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getAuditMonthByYear", produces = "plain/text; charset=UTF-8")
    public String getAuditMonthByYear(HttpServletRequest request) {
	Map<String, List<String>> result = new HashMap<String, List<String>>();

	Map<String, Object> params = new HashMap<String, Object>();
	String selYear = request.getParameter("selYear");
	params.put("selYear", selYear);
	List<String> audMonList = mybatisDao.getList("commonMapper.getAuditMonthByYear", params);
	result.put("audMonList", audMonList);

	CommonResult commonResult = new CommonResult();
	commonResult.setStatus("0");
	commonResult.setBody(result);
	return Json.Encode(commonResult);
    }

    @ResponseBody
    @RequestMapping(value = "getAuditYear", produces = "plain/text; charset=UTF-8")
    public String getAuditYear(HttpServletRequest request) {
	Map<String, Object> result = new HashMap<String, Object>();
	// 返回2：所有专题公共的年份列表
	List<String> auditYearList = mybatisDao.getList("commonMapper.getAuditYear");
	result.put("auditYearList", auditYearList);
	CommonResult commonResult = new CommonResult();
	commonResult.setStatus("0");
	commonResult.setBody(result);
	return Json.Encode(commonResult);
    }

    @ResponseBody
    @RequestMapping(value = "getAuthority", produces = "plain/text; charset=UTF-8")
    public String getAuthority(HttpServletRequest request) {
	HttpSession session = request.getSession();
	String authority = (String) session.getAttribute("authority");
	session.removeAttribute("authority");
	return CommonResult.success(authority);
    }

    /**
     *
     * <pre>
     * Desc
     * @return
     * @author xuwenhu
     * 2016-12-22 上午10:51:46
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getUserIdAndName", produces = "plain/text; charset=UTF-8")
    public String getUserIdAndName() {
	Map<String, Object> result = new HashMap<String, Object>();
	List<Map<String, Object>> usrList = new ArrayList<Map<String, Object>>();
	Map<String, Object> paramMap = new HashMap<String, Object>();
	List<Map<String, Object>> info = mybatisDao.getList("commonMapper.getUserIdAndName", paramMap);
	for (Map<String, Object> m : info) {
	    Map<String, Object> itemMap = new HashMap<String, Object>();
	    String userId = m.get("user_id").toString();
	    if (userId.contains("@")) {
		String[] userList = userId.split("@");
		userId = userList[0];
	    }
	    String userName = m.get("user_name").toString();
	    itemMap.put("userId", userId);
	    itemMap.put("userName", userName);
	    usrList.add(itemMap);
	}
	result.put("usrList", usrList);
	return CommonResult.success(result);
    }

    @RequestMapping(value = "sjtb")
	@ResponseBody
	public String  sjtb(HttpServletRequest request,String focusCd,String audTrm) {
    	Map<String, Object> m = new HashMap<String, Object>();
		try {
		sjtbJob.setFocusCd(focusCd);
		sjtbJob.setAudTrm(audTrm);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMM");
		List<AbstractSJTBWordProcessor> SJTBWordGenProcessorList = new  ArrayList<AbstractSJTBWordProcessor>();
		String monthActual;
		//SJTBWordGenProcessorList.add(sjtbQdykProcessor);
		if("1000".equals(focusCd)){
		    	monthActual=sjtbWordService.getMonthByAudtrm(audTrm,1,sdf);
		    	sjtbJob.setMonth(monthActual);
			SJTBWordGenProcessorList.add(sjtbYjkProcessor);
		}else if("2000".equals(focusCd)){
		    	monthActual=sjtbWordService.getMonthByAudtrm(audTrm,3,sdf);
		    	sjtbJob.setMonth(monthActual);
			SJTBWordGenProcessorList.add(sjtbQdykProcessor);
		}else if("3000".equals(focusCd)){
		    	monthActual=sjtbWordService.getMonthByAudtrm(audTrm,4,sdf);
		    	sjtbJob.setMonth(monthActual);
			SJTBWordGenProcessorList.add(sjtbZdtlProcessor);
		}else if("4000".equals(focusCd)){
		    	monthActual=sjtbWordService.getMonthByAudtrm(audTrm,1,sdf);
		    	sjtbJob.setMonth(monthActual);
			SJTBWordGenProcessorList.add(sjtbKhqfProcessor);
		}else{
			System.out.println("输入的专题号码不符合规范");
			return null;
		}
		IUser user =(IUser)request.getSession().getAttribute("ssoUSER");
		sjtbJob.setUser(user);
		sjtbJob.setSJTBWordGenProcessorList(SJTBWordGenProcessorList);
		sjtbJob.work();

			m.put("code", "200");
			m.put("msg", "success");

		} catch (Exception e) {
			m.put("code", "500");
			m.put("msg", "failed:" + e.getMessage());
			e.printStackTrace();
		}

		return JacksonJsonUtil.beanToJson(m);
	}

    @RequestMapping(value = "runJob")
	@ResponseBody
	public String runJob1(HttpServletRequest request,String focus,String audTrm,String prvdId) {
    	Map<String, Object> m = new HashMap<String, Object>();
    	String code =null;
    	IUser user =(IUser)request.getSession().getAttribute("ssoUSER");
		try {
		if(focus !=null &&focus.equals("1000")){
			yjkFileGenJobV2.setSubjectIds(Arrays.asList(new String[]{"1"}));
			yjkFileGenJobV2.setFocusCds(Arrays.asList(new String[]{"1001","1002","1003","1004","1005","1012"}));
			yjkFileGenJobV2.setTotalFocusCds("1000");
			yjkFileGenJobV2.setUseChineseName(true);
			yjkFileGenJobV2.setFileGenProcessor(yjk1000FileGenProcessorV2);
			yjkFileGenJobV2.setAudTrm(audTrm);
			yjkFileGenJobV2.setLoginAccount(user.getUserid());
			yjkFileGenJobV2.setUserName(user.getUsername());
			yjkFileGenJobV2.setPrvdId(prvdId);
			code =yjkFileGenJobV2.workByHand();
		}
		if(focus !=null &&focus.equals("2000")){
			qdykFileGenJobV2.setSubjectIds(Arrays.asList(new String[]{"2"}));
			qdykFileGenJobV2.setFocusCds(Arrays.asList(new String[]{"2002"}));
			qdykFileGenJobV2.setTotalFocusCds("2002");
			qdykFileGenJobV2.setUseChineseName(true);
			qdykFileGenJobV2.setFileGenProcessor(qdyk2002FileGenProcessorV2);
			qdykFileGenJobV2.setAudTrm(audTrm);
			qdykFileGenJobV2.setLoginAccount(user.getUserid());
			qdykFileGenJobV2.setUserName(user.getUsername());
			qdykFileGenJobV2.setPrvdId(prvdId);
			code =qdykFileGenJobV2.workByHand();
		}
		if(focus !=null &&focus.equals("3000")){
			zdtlFileGenJobV2.setSubjectIds(Arrays.asList(new String[]{"3"}));
			zdtlFileGenJobV2.setFocusCds(Arrays.asList(new String[]{"3000","3001","3002","3004","3005"}));
			zdtlFileGenJobV2.setTotalFocusCds("3000");
			zdtlFileGenJobV2.setUseChineseName(true);
			zdtlFileGenJobV2.setFileGenProcessor(zdtl3000FileGenProcessorV2);
			zdtlFileGenJobV2.setAudTrm(audTrm);
			zdtlFileGenJobV2.setLoginAccount(user.getUserid());
			zdtlFileGenJobV2.setUserName(user.getUsername());
			zdtlFileGenJobV2.setPrvdId(prvdId);
			code =zdtlFileGenJobV2.workByHand();
		}
		if(focus !=null &&focus.equals("4000")){
			qf4000FileGenJobV2.setSubjectIds(Arrays.asList(new String[]{"4"}));
			qf4000FileGenJobV2.setFocusCds(Arrays.asList(new String[]{"4000","4001","4003"}));
			qf4000FileGenJobV2.setUseChineseName(true);
			qf4000FileGenJobV2.setTotalFocusCds("4000");
			qf4000FileGenJobV2.setFileGenProcessor(qF4000FileGenProcessorV2);
			qf4000FileGenJobV2.setAudTrm(audTrm);
			qf4000FileGenJobV2.setLoginAccount(user.getUserid());
			qf4000FileGenJobV2.setUserName(user.getUsername());
			qf4000FileGenJobV2.setPrvdId(prvdId);
			code =qf4000FileGenJobV2.workByHand();
		}
		if(focus !=null &&focus.equals("5000")){
			ygycFileGenJob.setSubjectIds(Arrays.asList(new String[]{"5"}));
			ygycFileGenJob.setFocusCds(Arrays.asList(new String[]{"5000","5001","5002","5003","5004","5005"}));
			ygycFileGenJob.setTotalFocusCds("5000");
			ygycFileGenJob.setUseChineseName(true);
			ygycFileGenJob.setFileGenProcessor(ygyc5000FileGenProcessor);
			ygycFileGenJob.setAudTrm(audTrm);
			ygycFileGenJob.setLoginAccount(user.getUserid());
			ygycFileGenJob.setUserName(user.getUsername());
			ygycFileGenJob.setPrvdId(prvdId);
			code =ygycFileGenJob.workByHand();
		}
		if(focus !=null &&focus.equals("6000")){
			dzj6000FileGenJobV2.setSubjectIds(Arrays.asList(new String[]{"6"}));
			dzj6000FileGenJobV2.setFocusCds(Arrays.asList(new String[]{"6000","6001","6002","600301","600302","6004"}));
			dzj6000FileGenJobV2.setTotalFocusCds("6000");
			dzj6000FileGenJobV2.setUseChineseName(true);
			dzj6000FileGenJobV2.setFileGenProcessor(dzj6000FileGenProcessorV2);
			dzj6000FileGenJobV2.setAudTrm(audTrm);
			dzj6000FileGenJobV2.setLoginAccount(user.getUserid());
			dzj6000FileGenJobV2.setUserName(user.getUsername());
			dzj6000FileGenJobV2.setPrvdId(prvdId);
			code =dzj6000FileGenJobV2.workByHand();
		}
		if(focus !=null &&focus.equals("7000")){
			llzyFileGenJobV2.setSubjectIds(Arrays.asList(new String[]{"7"}));
			llzyFileGenJobV2.setFocusCds(Arrays.asList(new String[]{"7001","7003"}));
			llzyFileGenJobV2.setTotalFocusCds("7000");
			llzyFileGenJobV2.setUseChineseName(true);
			llzyFileGenJobV2.setFileGenProcessor(llzy7000FileGenProcessor);
			llzyFileGenJobV2.setAudTrm(audTrm);
			llzyFileGenJobV2.setLoginAccount(user.getUserid());
			llzyFileGenJobV2.setUserName(user.getUsername());
			llzyFileGenJobV2.setPrvdId(prvdId);
			code =llzyFileGenJobV2.work78Byhand();
		}
		if(focus !=null &&focus.equals("1100")){
			ksckFileGenJobV2.setSubjectIds(Arrays.asList(new String[]{"11"}));
			ksckFileGenJobV2.setFocusCds(Arrays.asList(new String[]{"1100"}));
			ksckFileGenJobV2.setTotalFocusCds("1100");
			ksckFileGenJobV2.setUseChineseName(true);
			ksckFileGenJobV2.setFileGenProcessor(ksck1100FileGenProcessorV2);
			ksckFileGenJobV2.setAudTrm(audTrm);
			ksckFileGenJobV2.setLoginAccount(user.getUserid());
			ksckFileGenJobV2.setUserName(user.getUsername());
			ksckFileGenJobV2.setPrvdId(prvdId);
			code =ksckFileGenJobV2.workByHand();
		}
		if(focus != null &&focus.equals("1200")){
			xjkdFileGenJobV2.setSubjectIds(Arrays.asList(new String[]{"12"}));
			xjkdFileGenJobV2.setFocusCds(Arrays.asList(new String[]{"1201","1203"}));
			xjkdFileGenJobV2.setTotalFocusCds("1200");
			xjkdFileGenJobV2.setUseChineseName(true);
			xjkdFileGenJobV2.setFileGenProcessor(xjkd1200FileGenProcessorV2);
			xjkdFileGenJobV2.setAudTrm(audTrm);
			xjkdFileGenJobV2.setLoginAccount(user.getUserid());
			xjkdFileGenJobV2.setUserName(user.getUsername());
			xjkdFileGenJobV2.setPrvdId(prvdId);
			code =xjkdFileGenJobV2.workByHand();
		}

			if(focus != null &&focus.equals("1300")){
				xxaqFileGenJobV2.setSubjectIds(Arrays.asList(new String[]{"13"}));
				xxaqFileGenJobV2.setFocusCds(Arrays.asList(new String[]{"1301","1302"}));
				xxaqFileGenJobV2.setTotalFocusCds("1300");
				xxaqFileGenJobV2.setUseChineseName(true);
				xxaqFileGenJobV2.setFileGenProcessor(xxaq1300FileGenProcessorV2);
				xxaqFileGenJobV2.setAudTrm(audTrm);
				xxaqFileGenJobV2.setLoginAccount(user.getUserid());
				xxaqFileGenJobV2.setUserName(user.getUsername());
				xxaqFileGenJobV2.setPrvdId(prvdId);
				code =xxaqFileGenJobV2.workByHand();
			}
//		if(focus !=null &&focus.equals("8000")){
//			ygycFileGenJob.setSubjectIds(Arrays.asList(new String[]{"8"}));
//			ygycFileGenJob.setFocusCds(Arrays.asList(new String[]{"8000"}));
//			ygycFileGenJob.setTotalFocusCds("8000");
//			ygycFileGenJob.setUseChineseName(true);
//			ygycFileGenJob.setFileGenProcessor(llzs8000FileGenProcessor);
//			ygycFileGenJob.work();
//		}

			m.put("code", code);
			m.put("msg", "success");

		} catch (Exception e) {
			m.put("code", "500");
			m.put("msg", "failed:" + e.getMessage());
			e.printStackTrace();
		}

		return JacksonJsonUtil.beanToJson(m);

	}
}
