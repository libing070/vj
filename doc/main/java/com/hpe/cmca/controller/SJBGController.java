package com.hpe.cmca.controller;

/**
 * 审计报告
 */
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.biframe.utils.string.StringUtil;
import com.hpe.cmca.common.CommonResult;
import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.finals.Constants;
import com.hpe.cmca.util.CalendarUtils;
import com.hpe.cmca.util.Json;
import com.hpe.cmca.util.Prvd_info;
import com.hpe.cmca.util.URLEncodedUtils;

@Controller
@RequestMapping("/sjbg")
public class SJBGController extends BaseController {

	/**
	 * 
	 * <pre>
	 * Desc  审计报告及明细清单下载首页
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   Oct 26, 2016 3:19:33 PM
	 * </pre>
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjbg })//判断是否具有审计报告权限
	@RequestMapping(value = "index")
	public String index() {
		return "sjbg/index";
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
	//@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjbg })
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
		
		// 返回1：公司列表
		List<Map<String, Object>> prvdList = mybatisDao.getList("commonMapper.getProvIdName", params);
		
		// 返回2：所有专题公共的年份列表
		List<String> auditYearList = mybatisDao.getList("commonMapper.getAuditYear");
		
		result.put("prvdList", prvdList);
		result.put("auditYearList", auditYearList);
		
		logger.debug(result);
		
		CommonResult commonResult = new CommonResult();
		commonResult.setStatus("0");
		commonResult.setBody(result);
		return  Json.Encode(commonResult);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  查询审计报告首页数据(各专题审计期间、各专题doc|csv下载次数|下载URL、各专题截止日期)
	 * @param request
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   Oct 27, 2016 2:56:10 PM
	 * </pre>
	 */
	//@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjbg })
	@ResponseBody
	@RequestMapping(value = "queryData", produces = "plain/text; charset=UTF-8")
	public String queryData(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> yjkInfo = new HashMap<String, Object>();
		Map<String, Object> qdykInfo = new HashMap<String, Object>();
		Map<String, Object> zdtlInfo = new HashMap<String, Object>();
		Map<String, Object> khqfInfo = new HashMap<String, Object>();
		
		Map<String, Object> params = new HashMap<String, Object>();
		String prvdIds = request.getParameter("prvdIds");

		// 返回3：各审计专题审计区间
		String audTrm = request.getParameter("audTrm");
		String yjkAuditTime = CalendarUtils.buildAuditTimeOfMonth(audTrm);
		yjkInfo.put("auditTime", yjkAuditTime);
		Map<String, Object> concernInfoMap = concernFileGenService.selectConcernInfosByCode("2002");
		int concernId = (Integer) concernInfoMap.get("id");
		String qdykAuditTime = CalendarUtils.buildAuditTimeOfMonth(audTrm, this.getAuditInterval(concernId));
		qdykInfo.put("auditTime", qdykAuditTime);
		concernInfoMap = concernFileGenService.selectConcernInfosByCode("3000");
		concernId = (Integer) concernInfoMap.get("id");
		String zdtlAuditTime = CalendarUtils.buildAuditTimeOfMonth(audTrm, this.getAuditInterval(concernId));
		zdtlInfo.put("auditTime", zdtlAuditTime);
		
		String khqfAuditTime = CalendarUtils.buildAuditTimeOfMonth(audTrm);
		khqfInfo.put("auditTime", khqfAuditTime);
		// 返回4：各专题doc|csv下载次数
		// 返回5：各专题截止日期 - 理论上是审计报告生成时的审计月日期，且当时的审计月的审计报告和明细日期一致   add by GuoXY 20161116 confirm by GuoQian
		params.put("prvdId", prvdIds);
		params.put("audtrm", request.getParameter("audTrm"));
		params.put("subjectId", "1,2,3,4");
		params.put("fileType", "'audReport','audDetail'");
		List<Map<String, Object>> audFilelist = mybatisDao.getList("commonMapper.selectAudReportFileBatch", params);
		for (Map<String, Object> fileRecoder : audFilelist) {
			String fileType = (String)fileRecoder.get("file_type");
			String subjectId = (String)fileRecoder.get("audit_subject");
			Integer downCount = (Integer)fileRecoder.get("down_count");
			String downUrl = (String)fileRecoder.get("download_url");
			// 新web前端不启用审计报告过期时间这个字段及相关逻辑   commit out by GuoXY 20161130	
			//String expireDate = (String)fileRecoder.get("expire_date");
			
			if (fileType.equals("audReport")) {
				if (subjectId.equals("1")) {
					yjkInfo.put("repDowNum", downCount);
					yjkInfo.put("repDowUrl", downUrl);
					//yjkInfo.put("deadlineDate", CalendarUtils.getMonth(expireDate, 0, "yyyy年M月"));
				} else if (subjectId.equals("2")) {
					qdykInfo.put("repDowNum", downCount);
					qdykInfo.put("repDowUrl", downUrl);
					//qdykInfo.put("deadlineDate", CalendarUtils.getMonth(expireDate, 0, "yyyy年M月"));
				} else if (subjectId.equals("3")) {
					zdtlInfo.put("repDowNum", downCount);
					zdtlInfo.put("repDowUrl", downUrl);
					//zdtlInfo.put("deadlineDate", CalendarUtils.getMonth(expireDate, 0, "yyyy年M月"));
				}else if (subjectId.equals("4")) {
					khqfInfo.put("repDowNum", downCount);
					khqfInfo.put("repDowUrl", downUrl);
					//khqfInfo.put("deadlineDate", CalendarUtils.getMonth(expireDate, 0, "yyyy年M月"));
				}
			} else {
				if (subjectId.equals("1")) {
					yjkInfo.put("detDowNum", downCount);
					yjkInfo.put("detDowUrl", downUrl);
					//yjkInfo.put("deadlineDate", CalendarUtils.getMonth(expireDate, 0, "yyyy年M月"));
				} else if (subjectId.equals("2")) {
					qdykInfo.put("detDowNum", downCount);
					qdykInfo.put("detDowUrl", downUrl);
					//qdykInfo.put("deadlineDate", CalendarUtils.getMonth(expireDate, 0, "yyyy年M月"));
				} else if (subjectId.equals("3")) {
					zdtlInfo.put("detDowNum", downCount);
					zdtlInfo.put("detDowUrl", downUrl);
					//zdtlInfo.put("deadlineDate", CalendarUtils.getMonth(expireDate, 0, "yyyy年M月"));
				}else if (subjectId.equals("4")) {
					khqfInfo.put("detDowNum", downCount);
					khqfInfo.put("detDowUrl", downUrl);
					//khqfInfo.put("deadlineDate", CalendarUtils.getMonth(expireDate, 0, "yyyy年M月"));
				}
			}
		}
		
		result.put("yjkInfo", yjkInfo);
		result.put("qdykInfo", qdykInfo);
		result.put("zdtlInfo", zdtlInfo);
		result.put("khqfInfo", khqfInfo);

		logger.debug(result);
		
		CommonResult commonResult = new CommonResult();
		commonResult.setStatus("0");
		commonResult.setBody(result);
		return  Json.Encode(commonResult);
	}
	
	/**
	 * 
	 * <pre>
	 * Desc 从数据库配置表中查询不同关注点的审计期间间隔值  
	 * @param concernId
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   Oct 28, 2016 12:32:31 PM
	 * </pre>
	 */
	protected int getAuditInterval(int concernId) {
		String attrValue = concernService.getConcernAttr(concernId, "auditInterval");
		if (StringUtils.isBlank(attrValue)) {
			return Constants.Concern.defaultAuditInterval;
		}
		return Integer.parseInt(attrValue);

	}
	
	/**
	 * 
	 * <pre>
	 * Desc  更新审计报告下载次数
	 * @param request
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   Nov 22, 2016 10:26:20 AM
	 * </pre>
	 */
	//@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjbg })
	@ResponseBody
	@RequestMapping(value = "updateWorkFileDownCount", produces = "plain/text; charset=UTF-8")
	public String updateWorkFileDownCount(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
//		Map<String, Object> yjkInfo = new HashMap<String, Object>();
		Map<String, Object> updateParms = new HashMap<String, Object>();
		
		String audTrm = null;
		String subjectId = null;
		String prvdId = null;
		String fileType = null;
		String downUrl = request.getParameter("downUrl");
		if(!StringUtil.isEmpty(downUrl)) {
			try {
				updateParms.put("audTrm", URLEncodedUtils.getParameter(downUrl, "UTF-8", "audTrm"));
				updateParms.put("subjectId", URLEncodedUtils.getParameter(downUrl, "UTF-8", "subjectId"));
				updateParms.put("prvdId", URLEncodedUtils.getParameter(downUrl, "UTF-8", "prvdId"));
				updateParms.put("fileType", URLEncodedUtils.getParameter(downUrl, "UTF-8", "fileType"));
				//updateParms.put("id", fileId);
				//if ("N".equals(downStatus)) {
				//	updateParms.put("downStatus", downStatus);
				//}
				//IUser user = (IUser) request.getSession().getAttribute("ssoUSER");
				//updateParms.put("userCode", user.getUserid());
				concernFileGenService.updateWorkspaceFile(updateParms);
				
				// 20170112 add by GuoXY 查询下载文件的下载次数及专题中文名、关注点中文名后，插入操作日志供日志查询模块查询
				List<Map<String, Object>> downCountlist = mybatisDao.getList("commonMapper.selectDownCountForLog", updateParms);
				Map<String, Object> downCountObj = (null != downCountlist&&downCountlist.size()!=0) ? downCountlist.get(0) : null;
				
				updateParms.put("subjectName", downCountObj.get("sname"));
				updateParms.put("focusCode", downCountObj.get("audit_concern")); // 审计报告都是专题级别的，目前插这个没啥用 
				updateParms.put("prvdName",  downCountObj.get("prvd_name"));
				updateParms.put("oprType",  URLEncodedUtils.getParameter(downUrl, "UTF-8", "fileType").equals("audReport") ? 1 : 2);//审计报告下载：1；审计结果明细清单下载：2
//				updateParms.put("oprPerson",  null);
//				updateParms.put("oprReason",  null);
				updateParms.put("downloadCount",  downCountObj.get("down_count"));
				//System.out.println(updateParms);
				mybatisDao.add("sjbgParamMapper.insertOperateLog", updateParms);//记录到日志表
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				logger.error("审计报告更新下载次数失败：（" + e + "），URL为：" + updateParms);
			}
		}
//		result.put("yjkInfo", yjkInfo);
//System.out.println(result);
		CommonResult commonResult = new CommonResult();
		commonResult.setStatus("0");
		commonResult.setBody(result);
		return  Json.Encode(commonResult);
	}
}
