package com.hpe.cmca.interfaces;

import java.util.List;
import java.util.Map;

import com.hpe.cmca.pojo.WzxxData;


public interface WzxxMapper {

    //获取整改信息
    public List<WzxxData> getWzxxData (WzxxData wzxx);
    //获取已经存在的问责信息
    public List<WzxxData> getExistWzxxData();
    //插入整改信息
    public int insertWzxxData (WzxxData wzxx);
    //更新整改信息
    public int updateWzxxData (WzxxData wzxx);
    //失效整改信息
    public int deleteWzxxData (Map<String, Object> params);
    
  //省份下拉框数据
    public List<WzxxData> getPrvd ();
    
    //获取合计信息
	public WzxxData getTotalWzxxData(WzxxData wzxx);
	

	
	
}
