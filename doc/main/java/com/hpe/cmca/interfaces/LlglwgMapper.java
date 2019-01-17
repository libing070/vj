/**
 * com.hpe.cmca.interfaces.LlglwgMapper.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hpe.cmca.pojo.LlglwgBottomData;
import com.hpe.cmca.pojo.LlglwgCompanyInfoPojo;
import com.hpe.cmca.pojo.LlglwgData;
import com.hpe.cmca.pojo.LlglwgForZgwzInfoTablePojo;
import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.pojo.LlglwgForZgwzPojo;
import com.hpe.cmca.pojo.LltfForSumOrgPojo;


/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2018-2-2 上午10:44:49
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2018-2-2 	   hufei 	         1. Created this class. 
 * </pre>  
 */
public interface LlglwgMapper {
    //流量管理违规-风险地图
    public List<LlglwgData>  getMapData(ParameterData parameterData);
    //流量管理违规-风险地图-下方卡片
    public List<LlglwgBottomData>  getMapBottomData(ParameterData parameterData);
    //流量管理违规-风险地图-地图下钻-异常流量赠送-7003-重点关注营销案
    public List<Map<String,Object>>  getFocusOfferOfCityTable(ParameterData parameterData); 
    //流量管理违规-统计分析-地图下钻-疑似违规流量转售集团客户信息
    public List<Map<String,Object>>  getFocusOrgCustomerOfCityTable(ParameterData parameterData);
    //流量管理违规-审计结果-流量数量异常-柱状图
    public List<LlglwgData>  getTrafficNumColumnData(ParameterData parameterData);
    //流量管理违规-审计结果-疑似违规流量转售-疑似违规转售集团客户数-柱状图
    public List<LlglwgData>  getIllegalGroupNumColumnData(ParameterData parameterData);
    //流量管理违规-审计结果-疑似违规流量转售-疑似违规转售集团客户数占比-柱状图
    public List<LlglwgData>  getIllegalGroupPercentColumnData(ParameterData parameterData);
    //流量管理违规-审计结果-流量占比异常-柱状图
    public List<LlglwgData>  getTrafficPercentColumnData(ParameterData parameterData);
    //流量管理违规-审计结果-流量数量异常-折线图
    public List<LlglwgData>  getTrafficLineData(ParameterData parameterData);
    //流量管理违规-审计结果-疑似违规流量转售-疑似违规转售集团客户-折线图
    public List<LlglwgData>  getIllegalGroupLineData(ParameterData parameterData);
    //流量管理违规-统计分析-排名汇总
    public List<LlglwgData>  getRankTable(ParameterData parameterData);
    //流量管理违规-统计分析-增量分析
    public List<LlglwgData>  getIncrementalData(ParameterData parameterData);
    //流量管理违规-统计分析-重点关注地市
    public List<Map<String,Object>>  getFocusCityTable(ParameterData parameterData);
    //流量管理违规-统计分析-重点关注渠道
    public List<Map<String,Object>>  getFocusChnlTable(ParameterData parameterData);
    //流量管理违规-统计分析-重点关注营销案
    public List<Map<String,Object>>  getFocusOfferTable(ParameterData parameterData); 
    //流量管理违规-统计分析-重点关注用户
    public List<Map<String,Object>>  getFocusUserTable(ParameterData parameterData);
    //流量管理违规-统计分析-重点关注集团客户
    public List<Map<String,Object>>  getFocusOrgCustomerTable(ParameterData parameterData);
    //流量管理违规-统计分析-违规类型分析-饼图  
    public LlglwgData getTypeDistributePie(ParameterData parameterData);
    //流量管理违规-统计分析-违规类型分析-趋势图 
    public List<LlglwgData>  getTypeDistributeStack(ParameterData parameterData);
    //流量管理违规-统计分析-审计报告-疑似违规流量转售
    public Map<String,Object>  getLlzyAuditReport(ParameterData parameterData);
    //流量管理违规-统计分析-审计报告-流量异常赠送
    public Map<String,Object>  getLlzsAuditReport(ParameterData parameterData);
    
    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	 //各省六个月内达到整改标准次数排名——柱状图
	 public ArrayList<LlglwgForZgwzPojo> getAmountColumnDataForChangeStandardSixMonth(@Param("sixMonthDate")String sixMonthDate , @Param("currDate")String currDate);
	 //各省累计整改标准次数排名——柱状图
	 public ArrayList<LlglwgForZgwzPojo> getAmountColumnDataForChangeStandardAll(@Param("currDate")String currDate) ;
    //达到整改标准省公司数量趋势——折线图
    public ArrayList<LlglwgForZgwzPojo> getRectifiyLineDataForChangeStandardAll(@Param("dateStart")String dateStart , @Param("dateEnd")String dateEnd) ;
    //各省六个月内达问责标准次数排名——柱状图
	 public ArrayList<LlglwgForZgwzPojo> getAccountForSixColumn(@Param("sixMonthDate")String sixMonthDate , @Param("currDate")String currDate);
	 //各省累计问责标准次数排名——柱状图
	 public ArrayList<LlglwgForZgwzPojo> getAccountabilityColumn(@Param("currDate")String currDate) ; 
     //达到问责标准省公司数量趋势——折线图
     public ArrayList<LlglwgForZgwzPojo> getAccountabilityLine(@Param("dateStart")String dateStart , @Param("dateEnd")String dateEnd) ;
     //根据违规集团ID查询集团具体信息
     public LlglwgCompanyInfoPojo getCompanyInfoByCompanyID(@Param("orgCustId")String companyId  , @Param("prvdId")String provinceId , @Param("audTrm")String audTrm) ;
     //根据当前审计月或省份ID查询当前整改省详细信息
     public List<LlglwgForZgwzInfoTablePojo> getChangeAccountInfoTableForZg(@Param("audTrm")String auditMonth  , @Param("prvdId")String provinceId) ;
     //根据当前审计月或省份ID查询当前有哪些问责公司
     public List<LlglwgForZgwzInfoTablePojo> getWzCompanyByAudTrmAndPrvdId(@Param("audTrm")String auditMonth  , @Param("prvdId")String provinceId) ;
     //根据问责公司ID及审计月查询当前问责具体详细说明
     public ArrayList<LlglwgForZgwzInfoTablePojo> getWtDetailsByCompanyIdAndaudTrm(@Param("prvdId")String prvdId) ;
     //重点关注客户表
     public ArrayList<LlglwgCompanyInfoPojo> getFocusGroupTable(@Param("currDate")String currDate , @Param("lastDateOne")String lastDateOne ,@Param("lastDateTwo")String lastDateTwo, @Param("prvdId")String prvdId) ;
     //自201809审计月被判定为疑似转售流量的审计月对应数据
     public ArrayList<LltfForSumOrgPojo> getYszsllAudTrmData(@Param("orgCustId")String orgCustId , @Param("startDate")String startDate , @Param("audTrm")String audTrm);
	 //根据集团公司表示及集团所在省份查询当前集团违规月份
     public List<String> getConpamyWgList(@Param("orgCustId")String orgCustId, @Param("prvdId")String prvdId);
	 //查询全国省份违规集团信息
     public ArrayList<LlglwgForZgwzPojo> getDataForMapLinkageForPrvd(@Param("audTrm")String audTrm);
     //查询地市级信息
     public ArrayList<LlglwgForZgwzPojo> getDataForMapLinkageForCity(@Param("audTrm")String audTrm , @Param("prvdId")String prvdId);
     //查询所有省份信息
     public ArrayList<String> getAllPrvdNameList();
     //查询所有省份ID信息
     public ArrayList<String> getAllPrvdIdList();
     //根据省份ID查询省份名称
     public String getPrvdNameByID(@Param("prvdId")String prvdId) ;
     //查询当前审计月被问责省份具体信息
     public List<LlglwgForZgwzInfoTablePojo> getWzCompanyForCurrMonth(@Param("audTrm")String audTrm , @Param("prvdId")String prvdId) ;
     //查询所有问责公司
     public List<LlglwgForZgwzInfoTablePojo> getAllWzCompanyList();
     //查询六个月内该省的整改信息
     public List<LlglwgForZgwzInfoTablePojo> getSixMonthInfo(@Param("currYearAndMonth")String currYearAndMonth,@Param("prvdId")String prvdId, @Param("audTrm")String audTrm);
}
