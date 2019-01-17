package com.hpe.cmwa.common;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hpe.cmwa.service.Dict;

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
