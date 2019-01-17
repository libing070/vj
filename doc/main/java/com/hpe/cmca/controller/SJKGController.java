package com.hpe.cmca.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.finals.RoleEnum;
import com.hpe.cmca.pojo.SjkgData;
import com.hpe.cmca.service.SjkgService;
import com.hpe.cmca.service.SystemLogMgService;
import com.hpe.cmca.util.Json;


@Controller
@RequestMapping("/sjkg")
public class SJKGController extends BaseController {

    @Autowired
    protected SjkgService       sjkgService;

    @Autowired
    protected SystemLogMgService systemLogMgService ;
    
    @CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjkg })//判断是否具有审计开关权限
    @RequestMapping(value = "index")
    public String index() {
	return "sjkg/index";
    }
    //审计开关-获取默认信息-hufei -add by 2017/11/10
    @ResponseBody
    @RequestMapping(value = "/queryDefaultInfo", produces = "text/json;charset=UTF-8")
    public String queryDefaultInfo(HttpServletRequest request,SjkgData parameterData) {
	Object depId = request.getSession().getAttribute("depId");
	if (depId != null) {
	    parameterData.setRoldId(RoleEnum.getSjkgIDByRealID(depId.toString())) ;
	} 
	return Json.Encode(sjkgService.queryDefaultInfo(parameterData));
    }
    //审计开关-获取专题列表 -hufei add by 2017/11/10
    @ResponseBody
    @RequestMapping(value="/getSubjectList", produces = "text/json;charset=UTF-8")
    public String getSubjectList(SjkgData parameterData){
	return Json.Encode(sjkgService.getSubjectList(parameterData));
    }
    //审计开关-根据专题获取审计月 -hufei -add by 2017/11/10
    @ResponseBody
    @RequestMapping(value = "/getAudTrmBySubject", produces = "text/json;charset=UTF-8")
    public String getAudTrmBySubject(SjkgData parameterData) {
	return Json.Encode(sjkgService.getAudTrmBySubject(parameterData));
    }
    //审计开关-根据专题、审计月获取可以新增的开关类型 -hufei -add by 2017/11/16
    @ResponseBody
    @RequestMapping(value = "/getSwitchType", produces = "text/json;charset=UTF-8")
    public String getSwitchType(SjkgData parameterData){
	return Json.Encode(sjkgService.getSwitchType(parameterData));
    }
    
    
    //审计开关-保存开关信息 -hufei -add by 2017/11/13
    @ResponseBody
    @RequestMapping(value="/saveSwitchInfo",produces="text/json;charset=UTF-8")
    public String saveSwitchInfo(HttpServletRequest request,SjkgData parameterData){
	if ("1".equals(getRoleId(request)) && parameterData.getSwitchState() <= 1) {
	    parameterData.setRoldId("1");
	    parameterData.setManagerOprPerson(request.getSession().getAttribute("userName").toString());
	    parameterData.setCreatePerson(request.getSession().getAttribute("userName").toString());
	}
	try {
		systemLogMgService.addControlLogData(request,parameterData, "saveSwitchInfo", "");
	} catch (Exception e) {
		// TODO: handle exception
		logger.error("saveSwitchInfo  >>>>>>  日志查询，开关：" + parameterData.toString(),e) ;
	}
	logger.error("saveSwitchInfo  >>>>>>  日志查询，开关：" + parameterData.toString()) ;
	
	return Json.Encode(sjkgService.saveSwitchInfo(parameterData));
    }
    
    //审计开关-查询
    @ResponseBody
    @RequestMapping(value="/getSwitchInfoList",produces="text/json;charset=UTF-8")
    public String getSwitchInfo(HttpServletRequest request,SjkgData parameterData){
	parameterData.setRoldId(getRoleId(request));
	return Json.Encode(sjkgService.getSwitchInfoList(parameterData));
    }
    
    //审计开关-更改审计开关状态
    @ResponseBody
    @RequestMapping(value="/updateSwitchInfo",produces="text/json;charset=UTF-8")
    public String updateSwitchInfo(HttpServletRequest request,SjkgData parameterData){
		getSiwtchState(request,parameterData);
		// 2018-8-16 16:37:24  日志查询 zhangqiang
		try {
			systemLogMgService.addControlLogData(request,parameterData, "updateSwitchInfo", "");
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("updateSwitchInfo  >>>>>>  日志查询，开关：" + parameterData.toString()) ;
		}
		logger.error("updateSwitchInfo  >>>>>>  日志查询，开关：" + parameterData.toString()) ;
		
		return Json.Encode(sjkgService.updateSwitchInfo(parameterData));
    }
    //审计开关-获取甘特图
    @ResponseBody
    @RequestMapping(value="/getGanttChart",produces="text/json;charset=UTF-8")
    public String getGanttChart(SjkgData parameterData){
	return Json.Encode(sjkgService.getGanttChart(parameterData));
    }
    //审计开关-获取气泡图
    @ResponseBody
    @RequestMapping(value="/getBubbleChart",produces="text/json;charset=UTF-8")
    public  String getBubbleChart(SjkgData parameterData){
	return Json.Encode(sjkgService.getBubbleChart(parameterData));
    }
    
    public String getRoleId(HttpServletRequest request){
	Object depId = request.getSession().getAttribute("depId");
	if (depId != null) {
	    return RoleEnum.getSjkgIDByRealID(depId.toString());
	} else{
	    return null;
	}
    }
    private SjkgData getSiwtchState(HttpServletRequest request,SjkgData parameterData) {
	parameterData.setRoldId(getRoleId(request));
	if (("1".equals(parameterData.getRoldId())||"4".equals(parameterData.getRoldId())) && parameterData.getSwitchStateByRole() == 2) {
	    parameterData.setSwitchState(1);
	    parameterData.setManagerOprPerson(request.getSession().getAttribute("userName").toString());
	    return parameterData;
	}
	if (("1".equals(parameterData.getRoldId())||"4".equals(parameterData.getRoldId())) && parameterData.getSwitchStateByRole() == 1) {
	    parameterData.setSwitchState(0);
	    parameterData.setManagerOprPerson(request.getSession().getAttribute("userName").toString());
	    return parameterData;
	}
	if ("2".equals(parameterData.getRoldId())) {
	    parameterData.setSwitchState(parameterData.getSwitchStateByRole());
	    parameterData.setOprPerson(request.getSession().getAttribute("userName").toString());
	    return parameterData;
	}
	return parameterData;
    }

}
