package com.hpe.cmca.interfaces;

import java.util.Map;


public interface SynchronizeTwoSysMapper {
	
	//从老系统向新系统添加审计报告清单路径
	public int addReportAndCsvPathToNew(Map<String,Object> map);
	//从新系统删除审计报告清单路径
	public int delReportAndCsvPathFromNew(Map<String,Object> map);
	
	
	//从老系统向新系统添加排名汇总和审计通报路径
	public int addPmhzAndSjtbPathToNew(Map<String,Object> map);
	//从新系统删除排名汇总和审计通报路径
	public int delPmhzAndSjtbPathFromNew(Map<String,Object> map);
	
}
