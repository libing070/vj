package com.hpe.cmca.interfaces;

import java.util.List;
import java.util.Map;

import com.hpe.cmca.pojo.SSOData;
import com.hpe.cmca.pojo.LoginData;


public interface SSOMapper {

    //获取对应专题下的开关打开的月份列表
	public List<SSOData> getDictCommon (String bizType);
	
    //更新登录信息
	public int updateLoginInfo (LoginData loginData);
	
    //新增等录信息
	public int insertLoginInfo (LoginData loginData);
	
    //获取登录信息
	public List<LoginData> getLoginInfo (LoginData loginData);
	
    //获取公告
	public List<Map<String,String>> getAnnouncement();
	
    //权限点名称
	public List<Map<String,Object>> getDictCommonByValue(Map<String,Object> tMap);
	
	 //获取省份下拉框
	public List<Map<String,Object>> getPrvd(Map<String,Object> paraMap);
	//获取部门下拉框
	public List<Map<String,Object>> getDep(Map<String,Object> paraMap);
	//获取用户下拉框
	public List<Map<String,Object>> getUser(Map<String,Object> paraMap);
}
