package com.hpe.cmca.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hpe.cmca.pojo.ZgxxData;


public interface ZgxxMapper {

    //获取整改
    public List<ZgxxData> getZgxxData (ZgxxData zgxx);
    //获取总计的整改信息
    public Map<String,Object> getTotalZgxxData(ZgxxData zgxx);
    //获取已经存在的整改信息，用于导入时候判断是否已经存在
    public List<ZgxxData> getExistZgxxData();
    //插入整改信息
    public int insertZgxxData (ZgxxData zgxx);
    //更新整改信息
    public int updateZgxxData (ZgxxData zgxx);
    //失效整改信息
    public int deleteZgxxData (Map<String, Object> params);
    
    //省份下拉框数据
    public List<ZgxxData> getPrvd ();
    
    //专题下拉框数据
    public List<ZgxxData> getSubject ();
    
    //获取统计数据配置信息
    public List<Map<String, Object>> getTjConfigData();
    
    
    //获取统计数据
	public List<Map<String, Object>> getTjData(Map<String, Object> paramsMap);
	//获取统计总计数据
	public Map<String, Object> getTjAllData(Map<String, Object> paramsMap);
	
	
	
	
}
