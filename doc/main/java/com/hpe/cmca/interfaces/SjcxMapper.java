package com.hpe.cmca.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hpe.cmca.pojo.GRSJzt;
import com.hpe.cmca.pojo.SJCXSubject;
import com.hpe.cmca.pojo.SJCXbc;
import com.hpe.cmca.pojo.SJCXcd;
import com.hpe.cmca.pojo.SJCXsf;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public interface SjcxMapper {

	

	List<SJCXSubject> getCons(@Param("tablenm")String tablenm);//获取表结构

	
	List<SJCXcd> getTable();//获取前段列表层级
	
	
	List<LinkedHashMap> executeSql(@Param("querySql")String sql);
	
	List<String> getAud(String id);//获得表的审计月
	
	
	void addSql(SJCXbc bc);//向数据库中添加用户模板

	List<SJCXbc> getTem(String tablenm);//从数据库中获取模板
	
	List<SJCXsf> getPrvd();//获取省份数据
	
	List<LinkedHashMap> filterSin(@Param(value="tableName")String tableName,@Param(value="tableFilter")String tableFilter,@Param(value="filter")String filter,@Param(value="paix")String paix);

	List<SJCXSubject> allCons();//获取所有表结构
	
	
}
