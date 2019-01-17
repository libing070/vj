package com.hpe.cmca.interfaces;

import java.util.List;
import java.util.Map;

import com.hpe.cmca.pojo.KhqfData;
import com.hpe.cmca.pojo.ParameterData;


public interface JkyjMapper {
   //获取专题信息
	public List<Map<String,Object>> getSubjectInfo(ParameterData m);
	//获取专题下模块信息
	public List<Map<String,Object>> getModuleInfo(ParameterData m);
	//获取模块下关注点信息
	public List<Map<String,Object>> getConcernInfo(ParameterData m);
	//获取图形参数
	public List<Map<String,Object>> getChartInfo(ParameterData m);
	//柱形图参数
	public List<Map<String,Object>> getColumnInfo(ParameterData m);
}
