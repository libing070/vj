package com.hpe.cmca.interfaces;

import java.util.List;
import java.util.Map;

import com.hpe.cmca.pojo.AudTrmData;


public interface AudTrmMapper {

    //获取对应专题下的开关打开的月份列表
    public List<AudTrmData> getAudTrmData (Map<String,Object> map);
    //获取对应专题对应月份对应用户权限的是否打开
    public Map<String,Object> getAttributeByAudTrmAndUser (Map<String,Object> map);
	
	
}
