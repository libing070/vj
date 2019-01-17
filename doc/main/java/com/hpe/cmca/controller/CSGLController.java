package com.hpe.cmca.controller;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.common.CommonResult;
import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.model.ModelParam;
import com.hpe.cmca.service.ModelParamService;
import com.hpe.cmca.service.SystemLogMgService;
import com.hpe.cmca.util.Json;
import com.hpe.cmca.web.taglib.Pager;

/**
 * 
 * <pre>
 * Desc： 参数管理
 * @author GuoXY
 * @refactor GuoXY
 * @date   Nov 30, 2016 6:05:12 PM
 * @version 1.0
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Nov 30, 2016 	   GuoXY 	         1. Created this class. 
 * </pre>
 */
@Controller
@RequestMapping("/csgl")
public class CSGLController extends BaseController {

	@Autowired
	private ModelParamService modelparamService = null;
	
	@Autowired
	private SystemLogMgService systemLogMgService ;
	
	// 20161213 若当前用户选中subject=1，且分页查看的不是第一页时，触发专题切换操作subject=2，那么分页的信息保留原来分页值，进而导致本次查询失败
	private String curSubjectId    = "2";// 根据参数管理，默认值为2，当切换页面时，用于判断目标专题是否和当前专题一致，若不一致就重置pager并设置curSubjectId=目标专题
	
	/**
	 * 
	 * <pre>
	 * Desc  参数管理首页
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   Oct 26, 2016 3:19:33 PM
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjcsgl })//判断是否具有参数管理权限
	@RequestMapping(value = "index")
	public String index() {
		return "csgl/index";
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  根据权限设置prvdIds、isQuanguo，查询公司选择列表框范围，查询审计年列表框范围
	 * @param request
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   Oct 29, 2016 10:09:20 AM
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjcsgl })
	@ResponseBody
	@RequestMapping(value = "queryDefaultParams", produces = "plain/text; charset=UTF-8")
	public String queryDefaultParams(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		//TODO 根据权限进行判断集团还是省公司 
		String prvdIds = request.getParameter("prvdIds");
		// 20161130 add by GuoXY for prvdIds中包含逗号说明是集团用户选中了多个省时，点击了审计报告下载
		if("10000".equals(prvdIds)||prvdIds==null || "".equals(prvdIds) || prvdIds.contains(",")){
			result.put("isQuanguo", true);
			result.put("prvdIds", "10000");
			params.put("prvdIdStr", "10000");
		}else{
			//TODO 获取人的所属省id
			result.put("isQuanguo", false);
			result.put("prvdIds", prvdIds);
			params.put("prvdIdStr", prvdIds);
		}
		
		logger.debug(result);
		
		CommonResult commonResult = new CommonResult();
		commonResult.setStatus("0");
		commonResult.setBody(result);
		return  Json.Encode(commonResult);
	}
	

	/**
	 * 
	 * <pre>
	 * Desc  
	 * @param request
	 * @param pager
	 * @param model
	 * @param subjectId
	 * @param concernId
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   Dec 6, 2016 8:24:13 AM
	 * </pre>
	 */
	@RequestMapping(value = "paramjsonGen")
	@ResponseBody
	public Pager getModelParam(HttpServletRequest request, Pager pager, String subjectId) {
		
		pager.setOrderBy("threshold_id");
		// 切换查询专题，将page还原成默认值
		if (!subjectId.equals(curSubjectId)) {
			pager.clear();
			curSubjectId = subjectId;
		}
		List<Map<String, Object>> paramList = null;
		try {
			pager.setReturnAll(true);
			paramList = modelparamService.getModelParamList(pager, subjectId, null);
			// 插入查询记录 zhangqiang
			systemLogMgService.addOtapaLogData(request, subjectId, "getModelParamList","select");
			// 进入该界面时，更新新增记录到表中 zhangqiang
			systemLogMgService.addOtapaLogData(request, subjectId, "getModelParamList","insert");
		} catch (Exception e) {

			this.logger.error("getModelParamList这个service出现异常");
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		pager.setResultList(paramList);
		return pager;
	}
	
	@RequestMapping(value = "selectModelSubject",produces = "text/json; charset=UTF-8")
	@ResponseBody
	public String selectModelSubject(HttpServletRequest request, String subjectId) {
		List<Map<String, Object>> paramList = null;
		try {
			paramList = modelparamService.selectModelSubject(subjectId);
			if(2==getSwitchStateByDepId(request)){
			    Iterator<Map<String, Object>> iter = paramList.iterator();
			    while (iter.hasNext()) {
				Map<String, Object> item = iter.next();
				if ("9".equals(item.get("id").toString())) {
				    iter.remove();
				}
			    }
			}
			
		} catch (Exception e) {

			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return Json.Encode(paramList);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  
	 * @param oper
	 * @param modelparam
	 * @param request
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   Dec 6, 2016 8:26:20 AM
	 * </pre>
	 */
	@RequestMapping(value = "paramEdit")
	@ResponseBody
	public String modelParamEdit(String oper, ModelParam modelparam, HttpServletRequest request) {

		if (oper.equals("edit"))
		{	
			// 设置各个参数属性在本次编辑时，是否发生变化
			modelparam.setChangeSta();
			Boolean bol = !modelparam.getIsChanged() ;
			if(!modelparam.getIsChanged())
				return "nonChangeError";
			try {
				modelparamService.editParam(modelparam, request);
				systemLogMgService.addOtapaLogData(request, modelparam, "modelParamEdit","update");
			} catch (Exception e) {
				this.logger.error("editParam这个service出现异常");
				e.printStackTrace();
				logger.error(e.getMessage(),e);
				return "editExceptionError";
			}
			return "success";
		}
//		if (oper.equals("add"))
//		{
//			try {
//				modelparamService.insertParam(modelparam, request);
//			} catch (Exception e) {
//
//				this.logger.error("insertParam这个service出现异常");
//				e.printStackTrace();
//				return "addExceptionError";
//			}
//
//			return "success";
//		}
		return "unknownError";
	}

	
	/**
	 * @return the curSubjectId
	 */
	public String getCurSubjectId() {
		return this.curSubjectId;
	}

	
	/**
	 * @param curSubjectId the curSubjectId to set
	 */
	public void setCurSubjectId(String curSubjectId) {
		this.curSubjectId = curSubjectId;
	}
	
	
	
	
	/**
	 * 
	 * <pre>
	 * Desc  查询指定专题下的参数记录
	 * 不用jqgrid时的代码，但因前端需要额外的开发来处理(多数据分页、滚动条浏览冻结表头、or未来按列排序等功能，还是用jqgrid。并复用cmccca的代码)
	 * 本方法暂时作废
	 * @param request
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   Oct 27, 2016 2:56:10 PM
	 * </pre>
	 * 
	@CmcaAuthority(authorityTypes = { AuthorityType.csgl })
	@ResponseBody
	@RequestMapping(value = "queryData", produces = "plain/text; charset=UTF-8")
	public String queryData(HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", request.getParameter("subjectId"));
		List<Map<String, Object>> list = mybatisDao.getList("csglMapper.selectModelParam", params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("csArray", list);

		logger.debug(result);
		
		CommonResult commonResult = new CommonResult();
		commonResult.setStatus("0");
		commonResult.setBody(result);
		return  Json.Encode(commonResult);
	}
	*/
	/**
	 * 
	 * <pre>
	 * Desc  根据ID值更新相应的参数记录
	 * 不用jqgrid时的代码，但因前端需要额外的开发来处理(多数据分页、滚动条浏览冻结表头、or未来按列排序等功能，还是用jqgrid。并复用cmccca的代码)
	 * 本方法暂时作废
	 * @param request
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   Dec 2, 2016 11:09:02 AM
	 * </pre>

	@CmcaAuthority(authorityTypes = { AuthorityType.csgl })
	@ResponseBody
	@RequestMapping(value = "updateParam", produces = "plain/text; charset=UTF-8")
	public String updateParam(HttpServletRequest request) {
		// 更新后的参数信息
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("threshold_id", request.getParameter("threshold_id"));//阈值编号（不可改）
		params.put("threshold_code", request.getParameter("threshold_code"));//阈值编码（不可改）
		
		params.put("threshold_name", request.getParameter("threshold_name"));//阈值名称（可改）
		params.put("threshold_value", request.getParameter("threshold_value"));//阀值数值（可改）
		params.put("operators", request.getParameter("operators"));//阀值逻辑（可改）
		params.put("eff_dt", request.getParameter("eff_dt"));//生效时间（可改）
		params.put("end_dt", request.getParameter("end_dt"));//失效时间（可改）
		params.put("edit_reason", request.getParameter("edit_reason"));//修订原因（用于插入日志）
		
		Map<String, Object> oldModelParam = mybatisDao.get("csglMapper.getOneModParById", request.getParameter("threshold_id"));
		logger.debug("Before update ModelParam:" + oldModelParam);
		// 无论是否发生变化将所有能修改的字段都更新一遍
		mybatisDao.update("ModelParamMapper.updateModelParam", params);
		logger.debug("After update ModelParam:" + params);
		
		// 插入本次更新操作的日志
		try {
			insertLog(request, params, oldModelParam);
		} catch (Exception e) {
			logger.debug("参数管理-插入操作日志失败！");
		}
		
		CommonResult commonResult = new CommonResult();
		commonResult.setStatus("0");
		commonResult.setBody(null);
		return  Json.Encode(commonResult);
	}
	*/
	/**
	 * 
	 * <pre>
	 * Desc  插入模型参数变更日志
	 * 不用jqgrid时的代码，但因前端需要额外的开发来处理(多数据分页、滚动条浏览冻结表头、or未来按列排序等功能，还是用jqgrid。并复用cmccca的代码)
	 * 本方法暂时作废
	 * @param newModelParam : 更新后的记录值
	 * @param oldModelParam : 原记录值
	 * @throws Exception
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   Dec 2, 2016 2:44:08 PM
	 * </pre>
	 * 
	 * 原日志表的列含义做如下修改
	 CREATE MULTISET TABLE hpmgr.busi_ca_config_log ,NO FALLBACK ,
     NO BEFORE JOURNAL,
     NO AFTER JOURNAL,
     CHECKSUM = DEFAULT
     (
	     edit_no INTEGER TITLE '阈值修改编号' NOT NULL GENERATED BY DEFAULT AS IDENTITY
	           (START WITH 1 
	            INCREMENT BY 1 
	            MINVALUE -2147483647 
	            MAXVALUE 2147483647 
	            NO CYCLE),
	      edit_person VARCHAR(100) CHARACTER SET LATIN NOT CASESPECIFIC TITLE '修改人' NOT NULL,
	      edit_time TIMESTAMP(6) FORMAT 'YYYYMMDDBHH:MI:SS' TITLE '修改日期()' NOT NULL,
	      edit_code VARCHAR(100) CHARACTER SET LATIN NOT CASESPECIFIC TITLE '阈值编码' NOT NULL, ==> 修改的参数记录 threshold_code
	      edit_col VARCHAR(100) CHARACTER SET LATIN NOT CASESPECIFIC TITLE '修改列名',         ==> 本次修改的目标属性
	      old_id VARCHAR(100) CHARACTER SET LATIN CASESPECIFIC TITLE '修改前编号',             ==> 修改的参数记录 threshold_id
	      old_value VARCHAR(100) CHARACTER SET LATIN CASESPECIFIC TITLE '修改前阈值',          ==> 修改前值 
	      new_id VARCHAR(100) CHARACTER SET LATIN CASESPECIFIC TITLE '修改后编号',             ==> 无意义
	      new_value VARCHAR(100) CHARACTER SET LATIN CASESPECIFIC TITLE '修改后阈值' NOT NULL, ==> 修改后值
	      edit_reason VARCHAR(100) CHARACTER SET LATIN CASESPECIFIC TITLE '修改原因' NOT NULL
     )
     PRIMARY INDEX ( edit_no );
	 * 
	public void insertLog(HttpServletRequest request, Map<String, Object> newModelParam, Map<String, Object> oldModelParam) throws Exception {
		// 获取当前用户
		String editPerson = "";
		IUser user = (IUser) request.getSession().getAttribute("ssoUSER");
		if (user == null) {
			editPerson = "参数管理模块：未获取到用户信息";
		} else {
			editPerson = user.getUsername();
		}
		// 获取修改原因
		String editReason = (String) newModelParam.get("edit_reason");
		// 获取当前时间
		Calendar calendar = Calendar.getInstance();
		Date date = (Date) calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curDateStr = sdf.format(date);
		
		ParamEditLog parameditlog = new ParamEditLog();
		parameditlog.setEdit_person(editPerson);
		parameditlog.setEdit_reason(editReason);
		parameditlog.setEdit_time(curDateStr);
		parameditlog.setOld_id((String)oldModelParam.get("threshold_id"));
		parameditlog.setEdit_code((String)oldModelParam.get("threshold_code"));
		
		if (!newModelParam.get("threshold_name").equals(oldModelParam.get("threshold_name"))) {
			parameditlog.setEdit_col("阈值名称");
			parameditlog.setNew_value((String)newModelParam.get("threshold_name"));
			mybatisDao.add("ModelParamMapper.insertLog", parameditlog);
		}
		if (!newModelParam.get("threshold_value").equals(oldModelParam.get("threshold_value"))) {
			parameditlog.setEdit_col("阈值");
			parameditlog.setNew_value((String)newModelParam.get("threshold_value"));
			mybatisDao.add("ModelParamMapper.insertLog", parameditlog);
		}
		if (!newModelParam.get("operators").equals(oldModelParam.get("operators"))) {
			parameditlog.setEdit_col("阈值逻辑");
			parameditlog.setNew_value((String)newModelParam.get("operators"));
			mybatisDao.add("ModelParamMapper.insertLog", parameditlog);
		}
		if (!newModelParam.get("eff_dt").equals(oldModelParam.get("eff_dt"))) {
			parameditlog.setEdit_col("生效时间");
			parameditlog.setNew_value((String)newModelParam.get("eff_dt"));
			mybatisDao.add("ModelParamMapper.insertLog", parameditlog);
		}
		if (!newModelParam.get("end_dt").equals(oldModelParam.get("end_dt"))) {
			parameditlog.setEdit_col("失效时间");
			parameditlog.setNew_value((String)newModelParam.get("end_dt"));
			mybatisDao.add("ModelParamMapper.insertLog", parameditlog);
		}
	}
	*/
}
