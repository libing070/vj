/**
 * com.hp.cmcc.service.QDYKService.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.util.CalendarUtils;

/**
 * 
 * <pre>
 * Desc： copy from cmccca 20170509
 * @author   xuwenhu
 * @refactor xuwenhu
 * @date     2017-5-9 下午3:49:44
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-5-9 	   xuwenhu 	         1. Created this class. 
 * </pre>
 */
@Service
public class QF4000Service {
	
 
	
	@Autowired
	private MybatisDao mybatisDao = null;

//	@Autowired
//	protected QF4002Service ktCusDebService = null;
	@Autowired
	protected QF4003Service grCusDebService = null;
	@Autowired
	protected QF4001Service chgCusService=null;
	

	public Map<String, List<Object>> selectAuditReportPageData(Map<String, Object> params)  {
		Map<String, List<Object>> returnMap = new HashMap<String, List<Object>>();

		
		String aud_cyl= CalendarUtils.getMonBeginEnd((String) params.get("statCycle"));
		String aud_trm=aud_cyl.substring(aud_cyl.indexOf("-")+1,aud_cyl.length());
		List<Object> TX_DATE= new ArrayList<Object>();
		TX_DATE.add(aud_trm);
		returnMap.put("aud_info",TX_DATE );
		
		//开通新业务审计周期
		params.put("statCycle", (String) params.get("statCycle")) ; 
		
		//个人和集团开通新业务都有totalInfo，要区分处理
//		Map<String, List<Object>> tpMap=ktCusDebService.selectAuditReportPageData(params);
//		List<Object> tpList=tpMap.get("totalInfo");
//		tpMap.put("totalInfoKt",  tpList  );
//		returnMap.putAll(tpMap);
		 
		//个人欠费审计周期		
		params.put("statCycle", (CalendarUtils.AddMontns((String) (params.get("statCycle")+"01"), -7)).substring(0,6)) ;
		returnMap.putAll(grCusDebService.selectAuditReportPageData(params));
		//集团长期高额欠费审计周期
		params.put("statCycle", (CalendarUtils.AddMontns((String) (params.get("statCycle")+"01"), -12)).substring(0,6)) ;
		returnMap.putAll(chgCusService.selectAuditReportPageData(params));

		return returnMap;
	}
	 
}
