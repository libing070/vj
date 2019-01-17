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

import org.jfree.chart.panel.Overlay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.dao.MybatisDao;


@Service
public class QF4001Service {
	
 
	
	@Autowired
	private MybatisDao mybatisDao = null;

	public Map<String, List<Object>> selectAuditReportPageData(Map<String, Object> params) {
		Map<String, List<Object>> dataMap = new HashMap<String, List<Object>>();
		Map<String, String> tpMap=new HashMap<String, String>();
		//tpMap.put("aging_top5"	     ,   "OrgCustCHG.auditReport_aging_top5"   );
		//tpMap.put("busi_top5"	     ,   "OrgCustCHG.auditReport_busi_top5"    );
		tpMap.put("amt_top5"	     ,   "OrgCustCHG.auditReport_amt_top5"     );
		//tpMap.put("lvl_top5"	     ,   "OrgCustCHG.auditReport_lvl_top5"     );
		tpMap.put("num_top5"	     ,   "OrgCustCHG.auditReport_num_top5"     );
		//tpMap.put("amt_par_top5"	 ,   "OrgCustCHG.auditReport_amt_par_top5" );
		tpMap.put("auditReport_all"	 ,   "OrgCustCHG.auditReport_all"          );
		//tpMap.put("lvl_info"         ,   "OrgCustCHG.auditReport_lvl_info"     );
		//tpMap.put("aging_info"       ,   "OrgCustCHG.auditReport_aging_info"   );
		//tpMap.put("amt_info"         ,   "OrgCustCHG.auditReport_amt_info"     );
		tpMap.put("auditReport_org_top10" ,"OrgCustCHG.auditReport_org_top10"  );
		
		for(Map.Entry<String, String> entrySet:tpMap.entrySet()){						
			List<Object> resultList = mybatisDao.getList(entrySet.getValue(), params);
			dataMap.put(entrySet.getKey(), resultList);
		}
		
//		Map<String, List<Object>> dataMap = concernService.selectPageData(params);
		
		
		
//		List<Object> toplist= new ArrayList<Object>();
//		List<Object> tplist= new ArrayList<Object>();
//		for (int i=0; i<dataMap.get("lvl_top5").size();i++){				
//			tplist.add(dataMap.get("lvl_top5").get(i));
//			if(i%4==3){
//				toplist.add(new ArrayList(tplist));		
//				tplist.clear();
//			}			
//		}
//		dataMap.put("lvl_top5", new ArrayList(toplist));
//		toplist.clear();
//		
//		for (int i=0; i<dataMap.get("aging_top5").size();i++){				
//			tplist.add(dataMap.get("aging_top5").get(i));
//			if(i%3==2){
//				toplist.add(new ArrayList(tplist));		
//				tplist.clear();
//			}			
//		}
//		dataMap.put("aging_top5", new ArrayList(toplist));
//		toplist.clear();
//		
//		for (int i=0; i<dataMap.get("amt_par_top5").size();i++){				
//			tplist.add(dataMap.get("amt_par_top5").get(i));
//			if(i%3==2){
//				toplist.add(new ArrayList(tplist));		
//				tplist.clear();
//			}			
//		}
//		dataMap.put("amt_par_top5", new ArrayList(toplist));
//		toplist.clear();
//

		return dataMap;
	}
}
