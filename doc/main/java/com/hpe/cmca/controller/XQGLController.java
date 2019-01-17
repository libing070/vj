package com.hpe.cmca.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hpe.cmca.pojo.XqglData;
import com.hpe.cmca.pojo.XqglListData;
import com.hpe.cmca.service.SystemLogMgService;
import com.hpe.cmca.service.XqglService;
import com.hpe.cmca.util.Json;

@Controller
@RequestMapping("/xqgl")
public class XQGLController extends BaseController {

    @Autowired
    private XqglService xqglService;

    @Autowired
    private SystemLogMgService systemLogMgService;

    @RequestMapping(value = "index")
    public String index() {
	return "xqgl/index";
    }

    // 详情页弹框
    @RequestMapping(value = "demandFormList")
    public String demandFormList() {
	return "xqgl/demandFormList";
    }

    // 需求管理-突发性数据统计-列表页
    @ResponseBody
    @RequestMapping(value = "getList", produces = "text/json; charset=UTF-8")
    public String getList(XqglListData xqglListData,HttpServletRequest request,String type) {
        HttpSession session=request.getSession();
        String loginId = session.getAttribute("userId").toString();
        String depId = session.getAttribute("depId").toString();
    	List<XqglListData> xqglList= xqglService.getList(xqglListData,loginId,depId,type) ;

    	systemLogMgService.addReqmLogData(request, xqglListData, "getList", "查询");
    	return Json.Encode(xqglList);
    }

    // 需求管理-突发性数据统计-获取系统配置项
    @ResponseBody
    @RequestMapping(value = "getSysConfig", produces = "text/json; charset=UTF-8")
    public String getSysConfig(String configSection) {
	return Json.Encode(xqglService.getSysConfig(configSection));
    }

    // 需求管理-突发性数据统计-查看详情页
    @ResponseBody
    @RequestMapping(value = "getDetailById", produces = "text/json; charset=UTF-8")
    public String getDetailById(String reqId,HttpServletRequest request) {
    	systemLogMgService.addReqmLogData(request, reqId, "getList", "查询");
	return Json.Encode(xqglService.getDetailById(reqId));
    }

    // 需求管理-突发性数据统计-新增需求
    @ResponseBody
    @RequestMapping(value = "addRequirement", produces = "text/json; charset=UTF-8")
    public String addRequirement(XqglData xqglData,HttpServletRequest request) {
        HttpSession session=request.getSession();
        String userId = session.getAttribute("userId").toString();
        String userName = session.getAttribute("userName").toString();
        String depName = session.getAttribute("depName").toString();
    	Map<String, Object> mapTp = xqglService.addRequirement(xqglData,userId,userName,depName) ;
    	systemLogMgService.addReqmLogData(request, (XqglData)mapTp.get("xqglData"), "addRequirement", "新增");
        mapTp.put("reqId",((XqglData) mapTp.get("xqglData")).getReqId());
        mapTp.remove("xqglData");
	return Json.Encode(mapTp);
    }

    // 需求管理-突发性数据统计-编辑需求
    @ResponseBody
    @RequestMapping(value = "editRequirement", produces = "text/json; charset=UTF-8")
    public String editRequirement(XqglData xqglData,HttpServletRequest request,String type) {
    	systemLogMgService.addReqmLogData(request, xqglData, "editRequirement", "编辑");

	return Json.Encode(xqglService.editRequirement(xqglData,type));
    }

    // 需求管理-突发性数据统计-删除需求
    @ResponseBody
    @RequestMapping(value = "deleteRequirement", produces = "text/json; charset=UTF-8")
    public String deleteRequirement(HttpServletRequest request,String reqId) {
    	int result = xqglService.deleteRequirement(request,reqId);
    	systemLogMgService.addReqmLogData(request, reqId, "deleteRequirement", "删除");
	return Json.Encode(result);
    }

    // 导出功能，将查询出的结果导出成两个excel,并和需求的附件一起打包
    @ResponseBody
    @RequestMapping(value = "outPut", produces = "plain/text; charset=UTF-8")
    public void outPut(XqglListData xqglListData, HttpServletRequest request,HttpServletResponse response){
        HttpSession session=request.getSession();
        String loginId = session.getAttribute("userId").toString();
        String depId = session.getAttribute("depId").toString();
    	XqglListData xqglListDatanew = xqglService.genReqExcel(xqglListData,loginId,depId,request,response);
    	systemLogMgService.addReqmLogData(request, xqglListDatanew, "outPut", "导出");
    }
    @ResponseBody
    @RequestMapping(value = "checkGenerateAttachment", produces = "text/json; charset=UTF-8")
    public String checkGenerateAttachment(HttpServletRequest request,String reqId,String reqTbNm){
	return Json.Encode(xqglService.checkGenerateAttachment(request,reqId, reqTbNm));
    }
    @ResponseBody
    @RequestMapping(value = "generateFile", produces = "text/json; charset=UTF-8")
    public String generateFile(HttpServletRequest request,String reqId,String reqTbNm){
    	int result = xqglService.generateFile(request,reqId, reqTbNm) ;
    	XqglData xqglData = new XqglData() ;
    	xqglData.setReqId(reqId);
    	xqglData.setReqResultAddr(reqId + ".zip");
    	systemLogMgService.addReqmLogData(request,xqglData , "deleteRequirement", "生成");
	return Json.Encode(result);
    }
    @ResponseBody
    @RequestMapping(value = "downloadResultFile", produces = "plain/text; charset=UTF-8")
    public void downloadResultFile(HttpServletRequest request,HttpServletResponse response,String reqId){
    	xqglService.downloadResultFile(request,response,reqId);
    	systemLogMgService.addReqmLogData(request, reqId, "downloadResultFile", "下载");
    }
    @ResponseBody
    @RequestMapping(value = "checkResultFile", produces = "text/json; charset=UTF-8")
    public String checkResultFile(HttpServletRequest request,String reqId){
	return Json.Encode(xqglService.checkResultFile(request,reqId));
    }

    @ResponseBody
	@RequestMapping(value = "webUploader")
    public void webUploader(@RequestParam("file") MultipartFile file,HttpServletRequest request,
    	HttpServletResponse response,String reqId,String uploadType) throws Exception {
    	xqglService.webUploader(file, request, response, reqId,uploadType);
    	XqglData xqglData = new XqglData() ;
    	xqglData.setReqId(reqId);
    	xqglData.setReqResultAddr(file.getOriginalFilename());
    	systemLogMgService.addReqmLogData(request, xqglData, "webUploader", "上传");
    }


    @ResponseBody
   	@RequestMapping(value = "deleteFtpFileExpired")   //删除15天以前的数据
    public void deleteFtpFileExpired(){
    	xqglService.deleteFtpFileExpired(0);
    }

    @ResponseBody
   	@RequestMapping(value = "downAttachment")   //下载附件
    public void downAttachmentById(String reqId,HttpServletRequest request, HttpServletResponse response,String type){
    	xqglService.downAttachmentById(reqId,response,type,request);
    	systemLogMgService.addReqmLogData(request, reqId, "downAttachmentById", "下载");
    }

    @ResponseBody
   	@RequestMapping(value = "getAuthorityAttr",produces = "text/json; charset=UTF-8")  //控制入口显示
    public String getAuthorityAttr(HttpServletRequest request,HttpServletResponse response){
    	String depId = null;
    	if(request.getSession().getAttribute("depId")==null)
    		depId = "10009";
    	else
    		depId = request.getSession().getAttribute("depId").toString();
    	if("12".equals(depId))
    		return Json.Encode(1);//集团业支
    	if("10009".equals(depId))
    		return Json.Encode(1);//惠普运维
    	return Json.Encode(0);//除了集团业支和惠普运维都无访问权限
    }

    @ResponseBody
    @RequestMapping(value = "getMyReqNum",produces = "text/json; charset=UTF-8")  //控制入口显示
    public String getMyReqNum(HttpServletRequest request) {
        HttpSession session=request.getSession();
        String userId = session.getAttribute("userId").toString();
        int a= xqglService.getMyReqNum(userId);
        return Json.Encode(a);
    }

    @ResponseBody
    @RequestMapping(value = "getDoneReqNum",produces = "text/json; charset=UTF-8")  //控制入口显示
    public String getDoneReqNum(HttpServletRequest request) {
        int a=0;
        HttpSession session=request.getSession();
        String userId = session.getAttribute("userId").toString();
        String depId = null;
        if(session.getAttribute("depId")==null)
            depId = "10009";
        else
            depId = session.getAttribute("depId").toString();
        if("12".equals(depId)){
            a=xqglService.getDoneReqNumByApprove(userId);
        }
        if("10009".equals(depId)){
            a=xqglService.getDoneReqNumByHandle();
        }
        return Json.Encode(a);
    }


    @ResponseBody
    @RequestMapping(value = "getLoginRole",produces = "text/json; charset=UTF-8")  //控制入口显示
    public String getLoginRole(HttpServletRequest request,HttpServletResponse response){
        String depId = null;
        if(request.getSession().getAttribute("depId")==null)
            depId = "10009";
        else
            depId = request.getSession().getAttribute("depId").toString();
        if("12".equals(depId))
            return Json.Encode(0);//集团业支，审批
        if("10009".equals(depId))
            return Json.Encode(1);//惠普运维，处理
        return Json.Encode(2);//除了集团业支和惠普运维之外
    }

}
