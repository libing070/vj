package com.hpe.cmca.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.finals.Dict;
import com.hpe.cmca.interfaces.AudTrmMapper;
import com.hpe.cmca.interfaces.BgxzMapper;
import com.hpe.cmca.interfaces.PrvdInfoMapper;
import com.hpe.cmca.pojo.AudTrmData;
import com.hpe.cmca.pojo.PrvdInfoData;

/**
 * <pre>
 * Desc： 
 * @author   peter.fu
 * @refactor peter.fu
 * @date     Sep 6, 2016 2:31:34 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Sep 6, 2016 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
public class BaseObject {

	protected Logger							logger			= Logger.getLogger(this.getClass());

	@Autowired
	protected FilePropertyPlaceholderConfigurer	propertyUtil	= null;
	
	@Autowired
	protected Dict								dict			= null;

	@Autowired
	protected MybatisDao mybatisDao;
	
	public List<PrvdInfoData> getPrvdInfoData(int prvdId){
		
		PrvdInfoMapper prvdInfoMapper = mybatisDao.getSqlSession().getMapper(PrvdInfoMapper.class);
		List<PrvdInfoData> prvdInfoDataList = prvdInfoMapper.getPrvdInfoData(prvdId);
		return prvdInfoDataList;
		
	}
	
	public List<AudTrmData> getAudTrmData(String subjectId,int switchState){
		Map<String,Object> map=new HashMap<String,Object>(2);
		map.put("subjectId", subjectId);
		map.put("switchState", switchState);
	    	AudTrmMapper audTrmMapper = mybatisDao.getSqlSession().getMapper(AudTrmMapper.class);
		List<AudTrmData> audTrmDataList = audTrmMapper.getAudTrmData(map);
		return audTrmDataList;
		
	}
	
	public List<Map<String,Object>> getAudTrmDataTrmConf(String subjectId,String roleId){
		BgxzMapper audTrmMapper = mybatisDao.getSqlSession().getMapper(BgxzMapper.class);
		Map<String,Object> params =new HashMap<String,Object>();
		params.put("subjectId", subjectId);
		List<Map<String,Object>> dataList =new ArrayList<Map<String,Object>>();
		if(roleId != null && (roleId.equals("1")|| roleId.equals("4"))){
			dataList = audTrmMapper.getAudTrmDataTrmConf1(params);
		}else if(roleId != null && roleId.equals("2")){
			dataList = audTrmMapper.getAudTrmDataTrmConf2(params);
		}else if(roleId != null && roleId.equals("3")){
			dataList = audTrmMapper.getAudTrmDataTrmConf3(params);
		}
		return dataList;
		
	}

	// 根据地市编码获取地市名称 copy from cmccca 20190914 add by GuoXY
	public String getShortNameOfCity(String cityCode) {
		Object provinceObj = dict.getAlias("CITY", cityCode);
		return provinceObj == null ? cityCode : (String) provinceObj;
	}

	// 根据省编码获取省名称（全名称） copy from cmccca 20190914 add by GuoXY 
	public String getCompanyNameOfProvince(String provinceCode) {
		Object provinceObj = dict.getAlias("COMPANY", provinceCode);
		return provinceObj == null ? provinceCode : (String) provinceObj;
	}
	//取得上一个审计周期
	public String getPrevAudTrm(String audTrm){
	    Integer year = Integer.parseInt(audTrm.substring(0,4));
	    Integer month = Integer.parseInt(audTrm.substring(4));
	    month = month-1;
	    if(month==0){
		return (year-1) + "12";
	    }else if (month<10){
		return year + "0" + month;
	    }else{
		return  year + "" + month;
	    }
	}
}
