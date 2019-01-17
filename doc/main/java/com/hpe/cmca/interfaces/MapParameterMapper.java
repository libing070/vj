package com.hpe.cmca.interfaces;

import java.util.List;

import com.hpe.cmca.pojo.MapParameterData;


public interface MapParameterMapper {

    //获取对应专题下的开关打开的月份列表
	public List<MapParameterData> getMapParameterData (String subjectId);
}
