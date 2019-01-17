/**
 * com.hpe.cmca.interfaces.SystemLogMgMapper.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.interfaces;

import com.hpe.cmca.pojo.*;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


/*
 * @Author ZhangQiang
 * @Description //TODO 
 * @Date 16:40 2018/10/24
 * @Param
 * @return 
 **/
public interface GRSJMapper {

    // 顶部查询-审计月和专题
    public List<GRSJData>  getCollec(String month);

	public List<GRSJData> getDep(@Param("month")String month, @Param("ztid")String ztid);

	public List<GRSJzt> getZt();

	public List<GRSJsf> getsf();

	public List<GRSJData> getInter(@Param("month")String month, @Param("ztid")String ztmc);

	public List<GRSJData>  getMaxDay(@Param("month")String month, @Param("ztmc")String ztmc,@Param("process")String process);

	public List<GRSJData> getDepMaxDay(@Param("month")String month, @Param("ztmc")String ztmc, @Param("process")String process, @Param("prvd")String prvd);

	public List<GRSJData> getAllMaxDay(@Param("month")String month, @Param("ztmc")String ztmc);

	
}
