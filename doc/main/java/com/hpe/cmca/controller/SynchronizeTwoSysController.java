package com.hpe.cmca.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.SynchronizeTwoSysMapper;
import com.hpe.cmca.interfaces.SystemLogMgMapper;
import com.hpe.cmca.util.Json;

@Controller
@RequestMapping("/SynchronizeTwoSys/")
public class SynchronizeTwoSysController extends BaseObject {

	@Autowired
	protected MybatisDao mybatisDao;

    @RequestMapping(value = "index")
    public String index() {
    	return "synchronizeTwoSys/index";
    }
	
	
	@ResponseBody
    @RequestMapping(value = "/SynchronizeFilePath", produces = "text/json;charset=UTF-8")
	public String SynchronizeFilePath(String genDate,String subjectId,String prvdId,String fileType){
		
		if("".equals(genDate)||"".equals(subjectId)||"".equals(prvdId)||"".equals(fileType))
			return Json.Encode(0);
		
		List<String> subjectIds = Arrays.asList(subjectId.split(","));
		if(subjectIds.contains("0"))subjectIds=null;
		List<String> prvdIds = Arrays.asList(prvdId.split(","));
		if(prvdIds.contains("0"))prvdIds=null;
//		List<Integer> subjectIds = new ArrayList<Integer>();
//		List<Integer> prvdIds = new ArrayList<Integer>();
//		for(String s :subjectIdsTp){
//			subjectIds.add(Integer.parseInt(s));
//		}
//		for(String s :prvdIdsTp){
//			prvdIds.add(Integer.parseInt(s));
//		}
		
		SynchronizeTwoSysMapper synMapper=mybatisDao.getSqlSession().getMapper(SynchronizeTwoSysMapper.class);

    	Map<String,Object> paramterMap=new HashMap<String,Object>();
    	paramterMap.put("genDate", genDate);
    	paramterMap.put("subjectIds", subjectIds);
    	paramterMap.put("prvdIds", prvdIds);
    	//paramterMap.put("fileType", fileType);
    	if("1".equals(fileType)){//报告清单
    		synMapper.delReportAndCsvPathFromNew(paramterMap);
    		synMapper.addReportAndCsvPathToNew(paramterMap);
    	}
    	if("2".equals(fileType)){//排名汇总通报
    		synMapper.delPmhzAndSjtbPathFromNew(paramterMap);
    		synMapper.addPmhzAndSjtbPathToNew(paramterMap);
    	}
    	
    	try {
    		// zhangqiang add by rzcx 2018-8-24 10:30:46
        	// 增加对 hpmgr.busi_report_log 表的操作记录
        	SystemLogMgMapper systemlogmgMapper = mybatisDao.getSqlSession().getMapper(SystemLogMgMapper.class);
        	if("1".equals(fileType)){//报告清单
        		systemlogmgMapper.delReportAndCsvPathFromNew(paramterMap);
        		systemlogmgMapper.addReportAndCsvPathToNew(paramterMap);
        	}
        	if("2".equals(fileType)){//排名汇总通报
        		systemlogmgMapper.delPmhzAndSjtbPathFromNew(paramterMap);
        		systemlogmgMapper.addPmhzAndSjtbPathToNew(paramterMap);
        	}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("SynchronizeFilePath >>>>>>>>>>>>>>>>>>paramterMap= " + paramterMap, e);
		}
    	
    	
		return  Json.Encode(1);
	}
}
