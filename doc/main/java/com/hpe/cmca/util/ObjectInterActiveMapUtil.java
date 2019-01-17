package com.hpe.cmca.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hpe.cmca.pojo.LlglwgCompanyInfoPojo;
import com.hpe.cmca.pojo.LlglwgForZgwzInfoTablePojo;
import com.hpe.cmca.service.LlglwgService;

/**
 * @Title 对象及Map互相转换工具类
 * @author admin
 *
 */
public class ObjectInterActiveMapUtil {
	

	/*-------------------------------------------------------【整改部分】----------------------------------------------------------------------------------*/
	//整改类型(问题概要)
	private final static String ZGTYPE_ONE = "当期疑似违规转售流量总值超过50TB且违规转售流量占本公司统付流量总量比例超过40%" ; 
	private final static String ZGTYPE_TWO = "当期存在单一集团客户疑似违规转售流量超过10TB且占当月全集团违规转售流量总量比例超过5%" ; 
	private final static String ZGTYPE_THREE = "存在同一集团客户有两期以上被判定为疑似违规转售流量" ; 
	/*-------------------------------------------------------【问责部分】--------------------------------------------------------------------------------------*/
//	private final static String WZTYPE_ONE = "自2018年10月通报起，对6个月中有3个月存在上述审计整改情形的公司予以审计问责" ;  
	//整改建议
	private final static String ZG_OPINION = "请组织市场、集团客户、内审等部门对流量统付业务进行认真梳理、审核，深入核查业务协议管理、资费管理、营销管理等方面的规范性，综合运用外呼、拨测等方法逐一对集团公司判定的疑似流量转售客户进行核查，查证其是否存在违规行为。" ;
	//问责要求
//	private final static String WZ_REQUIR = "对负有直接责任、主管责任、领导责任的相关人员进行责任界定，根据规定追究相应责任。" ;
	
	public static Map<String,Object> ObjectToMap(Object obj) throws Exception{
		 if(obj == null){    
	            return null;    
	        }   
	        Map<String, Object> map = new HashMap<String, Object>();    
	  
	        Field[] declaredFields = obj.getClass().getDeclaredFields();    
	        for (java.lang.reflect.Field field : declaredFields) {    
	            field.setAccessible(true);  
	            map.put(field.getName(), field.get(obj));  
	        }    
	  
	        return map; 
	}
	
	//装载整改公司详细信息
	public static ArrayList<HashMap<String , Object>> getZgObjectListMap(List<LlglwgForZgwzInfoTablePojo> zgCompanyList ,
			String audTrm) throws ParseException{
		HashMap<String , Object> wgCompanyMap  ; 
		ArrayList<HashMap<String , Object>> wgCompanyMapList = new ArrayList<HashMap<String , Object>>() ;
		if(zgCompanyList != null && zgCompanyList.size() != 0){
			for (int i = 0; i < zgCompanyList.size(); i++) {
				wgCompanyMap = new HashMap<String , Object>() ;
				LlglwgForZgwzInfoTablePojo wgCompany = zgCompanyList.get(i) ;
				wgCompanyMap.put("rn", i+1) ; //序号
				wgCompanyMap.put("prvdName", wgCompany.getPrvdName()) ; //省公司
				//将整改时间更换为（YYYY年MM月）
				String zgTime = DateUtilsForCurrProject.yyyyMMToyyyyYearMMMonth(audTrm) ;
				wgCompanyMap.put("zgTime", zgTime) ;//整改时间
				String zgType = wgCompany.getWgType() ;
				if(zgType.equals("zg1")){
					wgCompanyMap.put("wtSummary", ZGTYPE_ONE) ; //整改类型一
					//问题详细描述拼接
					//保留两位小数
					//问题详细描述拼接
					double newPrvdYszsSum = wgCompany.getPrvdYszsSum().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
					BigDecimal Zg1per = wgCompany.getZg1pe() ;
					double newZg1per = Zg1per.doubleValue() ;
					double lastZg1peDouble = newZg1per * 100 ;
					double lastZg1pe = LlglwgService.m2Double(lastZg1peDouble) ;
					String ZG_DES_ONE = wgCompany.getZgaudTrm()+"，" +wgCompany.getPrvdName()+"公司疑似违规转售流量总值"+newPrvdYszsSum+"TB且违规转售流量占本公司统付流量总量比例"+lastZg1pe+"%。" ;
					wgCompanyMap.put("wtDetails", ZG_DES_ONE) ;
					wgCompany.setWgTm(ZG_DES_ONE); 
				}else if(zgType.equals("zg2")){
					wgCompanyMap.put("wtSummary", ZGTYPE_TWO) ; //整改类型二
					//保留两位小数
					double newOrgYszsllSum = wgCompany.getOrgYszsllSum().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ; 
					BigDecimal Zg2pe = wgCompany.getZg2pe() ;
					double newZg2pe = Zg2pe.doubleValue() ;
					double lastZg2peDouble = newZg2pe * 100 ;
					double lastZg2pe = LlglwgService.m2Double(lastZg2peDouble) ;
					String ZG_DES_TWO =   wgCompany.getZgaudTrm()+"，" +wgCompany.getPrvdName()+"公司集团客户“"+wgCompany.getOrgNm()+"”疑似违规转售流量"+newOrgYszsllSum+"TB且占当月全集团违规转售流量总量比例"+lastZg2pe+"%。" ;
					wgCompanyMap.put("wtDetails", ZG_DES_TWO) ;
					wgCompany.setWgTm(ZG_DES_TWO); 
				}else if(zgType.equals("zg3")){
					wgCompanyMap.put("wtSummary", ZGTYPE_THREE) ; //整改类型三
					String newWgAud = wgCompany.getWgAud().trim() ;
					String lastWgAud = newWgAud.replace(",", "、") ;
					String ZG_DES_THREE =  wgCompany.getZgaudTrm()+"，" +wgCompany.getPrvdName()+"公司集团客户“"+wgCompany.getOrgNm()+"”于审计月"+lastWgAud+"被判定为疑似违规转售流量。" ;
					wgCompanyMap.put("wtDetails", ZG_DES_THREE) ;
					wgCompany.setWgTm(ZG_DES_THREE); 
				}
				wgCompanyMap.put("zgOpinion", ZG_OPINION) ;
				//通过审计月查询当前整改时间
				String zgwzDate = DateUtilsForCurrProject.getAfterAnyMonthLastDay(audTrm , 2) ;
				wgCompanyMap.put("zgqx", zgwzDate) ;
				wgCompanyMapList.add(wgCompanyMap) ;
			}
		}
		return wgCompanyMapList ;
	}
	
	//装载当前集团部分信息
	public static HashMap<String , Object> loadingGroupInfo(LlglwgCompanyInfoPojo llglwgCompanyInfoPojo){
			HashMap<String,Object> map = new HashMap<String , Object>() ;
			map.put("orgCustId", llglwgCompanyInfoPojo.getOrgCustId()) ; 
			map.put("companyName", llglwgCompanyInfoPojo.getCompanyName()) ;
			map.put("prvdName", llglwgCompanyInfoPojo.getCompanyName()) ;
			map.put("accountAudTrm", llglwgCompanyInfoPojo.getAccountAudTrm()) ;
			map.put("prvdName", llglwgCompanyInfoPojo.getPrvdName()) ;
			map.put("city", llglwgCompanyInfoPojo.getCity()) ;
			map.put("modeName", llglwgCompanyInfoPojo.getModeName()) ;
			map.put("formName", llglwgCompanyInfoPojo.getFormName()) ;
			return map ;
	}
	
	
	//装载当前集团部分信息(重点关注集团客户表)
	public static HashMap<String , Object> loadingTableGroupInfo(LlglwgCompanyInfoPojo llglwgCompanyInfoPojo , HashMap<String , Object> map , String currDate, String lastDateOne, String lastDateTwo){
		if( llglwgCompanyInfoPojo != null && llglwgCompanyInfoPojo.getCurrIsNotWg() != null){
				if(llglwgCompanyInfoPojo.getOneMonthAgoIsNotWg() != null && llglwgCompanyInfoPojo.getTwoMonthAgoIsNotWg() == null){
					//一个月前有值，两个月前无值
					//基本信息
					map.put("orgCustId", llglwgCompanyInfoPojo.getFocusOrgId()) ;
					map.put("companyName", llglwgCompanyInfoPojo.getFocusOrgName()) ;
					map.put("prvdName", llglwgCompanyInfoPojo.getFocusPrvdName()) ;
					//装载附加信息
					map.put("trm_1"  , lastDateTwo) ;
					map.put("isNotWg_1" , "-") ;
					map.put("tfSum_1" ,  "-") ;
					map.put("tfSumStrmPrvdPer_1" , "-") ;
					map.put("tfSumStrmAllerrPer_1" , "-") ;
					
					map.put("trm_2"  , lastDateOne) ;
					map.put("isNotWg_2" , llglwgCompanyInfoPojo.getOneMonthAgoIsNotWg()) ;
					//整理数据格式
					if(llglwgCompanyInfoPojo.getOneMonthAgoTfSum() != null){
						BigDecimal oneMonthAgotfSumBig = llglwgCompanyInfoPojo.getOneMonthAgoTfSum() ;
						double oneMonthAgotfSumDouble = getDoubleOnlyTwo(oneMonthAgotfSumBig) ;
						map.put("tfSum_2" ,  oneMonthAgotfSumDouble) ;
					}else{
						map.put("tfSum_2" ,  "-") ;
					}
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
					if( llglwgCompanyInfoPojo.getOneMonthAgoTfSumStrmPrvdPer() != null){
						BigDecimal oneMonthAgoTfSumStrmPrvdPer= llglwgCompanyInfoPojo.getOneMonthAgoTfSumStrmPrvdPer() ;
						double oneMonthAgoTfSumStrmPrvdPerPercent = BigDecimalToPercent(oneMonthAgoTfSumStrmPrvdPer) ;
						map.put("tfSumStrmPrvdPer_2" , oneMonthAgoTfSumStrmPrvdPerPercent) ;
					}else{
						map.put("tfSumStrmPrvdPer_2" , "-") ;
					}
					
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
					if( llglwgCompanyInfoPojo.getOneMonthAgoTfSumStrmAllerrPer() != null){
						BigDecimal oneMonthAgoTfSumStrmAllerrPer = llglwgCompanyInfoPojo.getOneMonthAgoTfSumStrmAllerrPer() ;
//						double oneMonthAgoTfSumStrmAllerrPerDouble = getDoubleOnlyTwo(oneMonthAgoTfSumStrmAllerrPer) ;
						double oneMonthAgoTfSumStrmAllerrPerDouble =  BigDecimalToPercent(oneMonthAgoTfSumStrmAllerrPer) ;
						map.put("tfSumStrmAllerrPer_2" , oneMonthAgoTfSumStrmAllerrPerDouble) ;
					}else{
						map.put("tfSumStrmAllerrPer_2" , "-") ;
					}
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
				
					
					map.put("trm_3", llglwgCompanyInfoPojo.getCurrAudTrm()) ;
					map.put("isNotWg_3", llglwgCompanyInfoPojo.getCurrIsNotWg()) ;
					//整理数据格式
					if(llglwgCompanyInfoPojo.getCurrTfSum()  == null){
						map.put("tfSum_3", "-") ;
					}else{
						BigDecimal currTfSumBig = llglwgCompanyInfoPojo.getCurrTfSum() ;
						double currTfSumDouble = getDoubleOnlyTwo(currTfSumBig) ;
						map.put("tfSum_3", currTfSumDouble) ;
					}
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
					if(llglwgCompanyInfoPojo.getCurrTfSumStrmPrvdPer() == null){
						map.put("tfSumStrmPrvdPer_3", "-") ;
					}else{
						BigDecimal currTfSumStrmPrvdPer= llglwgCompanyInfoPojo.getCurrTfSumStrmPrvdPer() ;
						double currTfSumStrmPrvdPerPercent = BigDecimalToPercent(currTfSumStrmPrvdPer) ;
						map.put("tfSumStrmPrvdPer_3", currTfSumStrmPrvdPerPercent) ;
					}
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
					if(llglwgCompanyInfoPojo.getCurrTfSumStrmAllerrPer()==null){
						map.put("tfSumStrmAllerrPer_3", "-") ;
					}else{
						BigDecimal currTfSumStrmAllerrPer = llglwgCompanyInfoPojo.getCurrTfSumStrmAllerrPer() ;
//						double currTfSumStrmAllerrPerDouble = getDoubleOnlyTwo(currTfSumStrmAllerrPer) ;
						double currTfSumStrmAllerrPerDouble =  BigDecimalToPercent(currTfSumStrmAllerrPer) ;
						map.put("tfSumStrmAllerrPer_3", currTfSumStrmAllerrPerDouble) ;
					}
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
					
					
					
				}else if(llglwgCompanyInfoPojo.getOneMonthAgoIsNotWg() == null && llglwgCompanyInfoPojo.getTwoMonthAgoIsNotWg() != null){
					//一个月前无值，两个月前有值
					//基本信息
					map.put("orgCustId", llglwgCompanyInfoPojo.getFocusOrgId()) ;
					map.put("companyName", llglwgCompanyInfoPojo.getFocusOrgName()) ;
					map.put("prvdName", llglwgCompanyInfoPojo.getFocusPrvdName()) ;
					//装载附加信息
					map.put("trm_2"  , lastDateOne) ;
					map.put("isNotWg_2" , "-") ;
					map.put("tfSum_2" ,  "-") ;
					map.put("tfSumStrmPrvdPer_2" , "-") ;
					map.put("tfSumStrmAllerrPer_2" , "-") ;
					
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
					
					map.put("trm_1"  , llglwgCompanyInfoPojo.getTwoMonthAgoAudTrm()) ;
					map.put("isNotWg_1" , llglwgCompanyInfoPojo.getTwoMonthAgoIsNotWg()) ;
					//整理数据格式
					if(llglwgCompanyInfoPojo.getTwoMonthAgoTfSum() == null){
						map.put("tfSum_1" ,  "-") ;
					}else{
						BigDecimal twoMonthAgotfSumBig = llglwgCompanyInfoPojo.getTwoMonthAgoTfSum() ;
						double twoMonthAgotfSumDouble = getDoubleOnlyTwo(twoMonthAgotfSumBig) ;
						map.put("tfSum_1" ,  twoMonthAgotfSumDouble) ;
					}
					/***************************************************************************************************************************/
					if(llglwgCompanyInfoPojo.getTwoMonthAgoTfSumStrmPrvdPer() == null){
						map.put("tfSumStrmPrvdPer_1" , "-") ;
					}else{
						BigDecimal twoMonthAgoTfSumStrmPrvdPer= llglwgCompanyInfoPojo.getTwoMonthAgoTfSumStrmPrvdPer() ;
						double twoMonthAgoTfSumStrmPrvdPerPercent = BigDecimalToPercent(twoMonthAgoTfSumStrmPrvdPer) ;
						map.put("tfSumStrmPrvdPer_1" , twoMonthAgoTfSumStrmPrvdPerPercent) ;
					}
					/***************************************************************************************************************************/
					if(llglwgCompanyInfoPojo.getTwoMonthAgoTfSumStrmAllerrPer() == null){
						map.put("tfSumStrmAllerrPer_1" , "-") ;
					}else{
						BigDecimal twoMonthAgoTfSumStrmAllerrPer = llglwgCompanyInfoPojo.getTwoMonthAgoTfSumStrmAllerrPer() ;
//						double twoMonthAgoTfSumStrmAllerrPerDouble = getDoubleOnlyTwo(twoMonthAgoTfSumStrmAllerrPer) ;
						double twoMonthAgoTfSumStrmAllerrPerDouble =  BigDecimalToPercent(twoMonthAgoTfSumStrmAllerrPer) ;
						map.put("tfSumStrmAllerrPer_1" , twoMonthAgoTfSumStrmAllerrPerDouble) ;
					}
					
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
					
					map.put("trm_3", llglwgCompanyInfoPojo.getCurrAudTrm()) ;
					map.put("isNotWg_3", llglwgCompanyInfoPojo.getCurrIsNotWg()) ;
					//整理数据格式
					if(llglwgCompanyInfoPojo.getCurrTfSum()  == null){
						map.put("tfSum_3", "-") ;
					}else{
						BigDecimal currTfSumBig = llglwgCompanyInfoPojo.getCurrTfSum() ;
						double currTfSumDouble = getDoubleOnlyTwo(currTfSumBig) ;
						map.put("tfSum_3", currTfSumDouble) ;
					}
					/***************************************************************************************************************************/
					if(llglwgCompanyInfoPojo.getCurrTfSumStrmPrvdPer() == null){
						map.put("tfSumStrmPrvdPer_3", "-") ;
					}else{
						BigDecimal currTfSumStrmPrvdPer= llglwgCompanyInfoPojo.getCurrTfSumStrmPrvdPer() ;
						double currTfSumStrmPrvdPerPercent = BigDecimalToPercent(currTfSumStrmPrvdPer) ;
						map.put("tfSumStrmPrvdPer_3", currTfSumStrmPrvdPerPercent) ;
					}
					/***************************************************************************************************************************/
					if(llglwgCompanyInfoPojo.getCurrTfSumStrmAllerrPer()==null){
						map.put("tfSumStrmAllerrPer_3", "-") ;
					}else{
						BigDecimal currTfSumStrmAllerrPer = llglwgCompanyInfoPojo.getCurrTfSumStrmAllerrPer() ;
//						double currTfSumStrmAllerrPerDouble = getDoubleOnlyTwo(currTfSumStrmAllerrPer) ;
						double currTfSumStrmAllerrPerDouble =  BigDecimalToPercent(currTfSumStrmAllerrPer) ;
						map.put("tfSumStrmAllerrPer_3", currTfSumStrmAllerrPerDouble) ;
					}
					/***************************************************************************************************************************/		
					
			}else if(llglwgCompanyInfoPojo.getOneMonthAgoIsNotWg() == null && llglwgCompanyInfoPojo.getTwoMonthAgoIsNotWg() == null){
					//两个月前都无值
					//基本信息
					map.put("orgCustId", llglwgCompanyInfoPojo.getFocusOrgId()) ;
					map.put("companyName", llglwgCompanyInfoPojo.getFocusOrgName()) ;
					map.put("prvdName", llglwgCompanyInfoPojo.getFocusPrvdName()) ;
					//装载附加信息
					map.put("trm_2"  , lastDateOne) ;
					map.put("isNotWg_2" , "-") ;
					map.put("tfSum_2" ,  "-") ;
					map.put("tfSumStrmPrvdPer_2" , "-") ;
					map.put("tfSumStrmAllerrPer_2" , "-") ;
	
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
					
					//装载附加信息
					map.put("trm_1"  , lastDateTwo) ;
					map.put("isNotWg_1" , "-") ;
					map.put("tfSum_1" ,  "-") ;
					map.put("tfSumStrmPrvdPer_1" , "-") ;
					map.put("tfSumStrmAllerrPer_1" , "-") ;
	
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
					
					map.put("trm_3", llglwgCompanyInfoPojo.getCurrAudTrm()) ;
					map.put("isNotWg_3", llglwgCompanyInfoPojo.getCurrIsNotWg()) ;
					//整理数据格式
					if(llglwgCompanyInfoPojo.getCurrTfSum()  == null){
						map.put("tfSum_3", "-") ;
					}else{
						BigDecimal currTfSumBig = llglwgCompanyInfoPojo.getCurrTfSum() ;
						double currTfSumDouble = getDoubleOnlyTwo(currTfSumBig) ;
						map.put("tfSum_3", currTfSumDouble) ;
					}
					/***************************************************************************************************************************/
					if(llglwgCompanyInfoPojo.getCurrTfSumStrmPrvdPer() == null){
						map.put("tfSumStrmPrvdPer_3", "-") ;
					}else{
						BigDecimal currTfSumStrmPrvdPer= llglwgCompanyInfoPojo.getCurrTfSumStrmPrvdPer() ;
						double currTfSumStrmPrvdPerPercent = BigDecimalToPercent(currTfSumStrmPrvdPer) ;
						map.put("tfSumStrmPrvdPer_3", currTfSumStrmPrvdPerPercent) ;
					}
					/***************************************************************************************************************************/
					if(llglwgCompanyInfoPojo.getCurrTfSumStrmAllerrPer()==null){
						map.put("tfSumStrmAllerrPer_3", "-") ;
					}else{
						BigDecimal currTfSumStrmAllerrPer = llglwgCompanyInfoPojo.getCurrTfSumStrmAllerrPer() ;
//						double currTfSumStrmAllerrPerDouble = getDoubleOnlyTwo(currTfSumStrmAllerrPer) ;
						double currTfSumStrmAllerrPerDouble =  BigDecimalToPercent(currTfSumStrmAllerrPer) ;
						map.put("tfSumStrmAllerrPer_3", currTfSumStrmAllerrPerDouble) ;
					}
					/***************************************************************************************************************************/				
				}else{
					//基本信息
					map.put("orgCustId", llglwgCompanyInfoPojo.getFocusOrgId()) ;
					map.put("companyName", llglwgCompanyInfoPojo.getFocusOrgName()) ;
					map.put("prvdName", llglwgCompanyInfoPojo.getFocusPrvdName()) ;
					
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
					
					map.put("trm_1"  , llglwgCompanyInfoPojo.getTwoMonthAgoAudTrm()) ;
					map.put("isNotWg_1" , llglwgCompanyInfoPojo.getTwoMonthAgoIsNotWg()) ;
					//整理数据格式
					if(llglwgCompanyInfoPojo.getTwoMonthAgoTfSum() == null){
						map.put("tfSum_1" ,  "-") ;
					}else{
						BigDecimal twoMonthAgotfSumBig = llglwgCompanyInfoPojo.getTwoMonthAgoTfSum() ;
						double twoMonthAgotfSumDouble = getDoubleOnlyTwo(twoMonthAgotfSumBig) ;
						map.put("tfSum_1" ,  twoMonthAgotfSumDouble) ;
					}
					/***************************************************************************************************************************/
					if(llglwgCompanyInfoPojo.getTwoMonthAgoTfSumStrmPrvdPer() == null){
						map.put("tfSumStrmPrvdPer_1" , "-") ;
					}else{
						BigDecimal twoMonthAgoTfSumStrmPrvdPer= llglwgCompanyInfoPojo.getTwoMonthAgoTfSumStrmPrvdPer() ;
						double twoMonthAgoTfSumStrmPrvdPerPercent = BigDecimalToPercent(twoMonthAgoTfSumStrmPrvdPer) ;
						map.put("tfSumStrmPrvdPer_1" , twoMonthAgoTfSumStrmPrvdPerPercent) ;
					}
					/***************************************************************************************************************************/
					if(llglwgCompanyInfoPojo.getTwoMonthAgoTfSumStrmAllerrPer() == null){
						map.put("tfSumStrmAllerrPer_1" , "-") ;
					}else{
						BigDecimal twoMonthAgoTfSumStrmAllerrPer = llglwgCompanyInfoPojo.getTwoMonthAgoTfSumStrmAllerrPer() ;
//						double twoMonthAgoTfSumStrmAllerrPerDouble = getDoubleOnlyTwo(twoMonthAgoTfSumStrmAllerrPer) ;
						double twoMonthAgoSumStrmAllerrPerDouble =  BigDecimalToPercent(twoMonthAgoTfSumStrmAllerrPer) ;
						map.put("tfSumStrmAllerrPer_1" , twoMonthAgoSumStrmAllerrPerDouble) ;
					}
					
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
					
					map.put("trm_2"  , lastDateOne) ;
					map.put("isNotWg_2" , llglwgCompanyInfoPojo.getOneMonthAgoIsNotWg()) ;
					//整理数据格式
					if(llglwgCompanyInfoPojo.getOneMonthAgoTfSum() != null){
						BigDecimal oneMonthAgotfSumBig = llglwgCompanyInfoPojo.getOneMonthAgoTfSum() ;
						double oneMonthAgotfSumDouble = getDoubleOnlyTwo(oneMonthAgotfSumBig) ;
						map.put("tfSum_2" ,  oneMonthAgotfSumDouble) ;
					}else{
						map.put("tfSum_2" ,  "-") ;
					}
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
					if( llglwgCompanyInfoPojo.getOneMonthAgoTfSumStrmPrvdPer() != null){
						BigDecimal oneMonthAgoTfSumStrmPrvdPer= llglwgCompanyInfoPojo.getOneMonthAgoTfSumStrmPrvdPer() ;
						double oneMonthAgoTfSumStrmPrvdPerPercent = BigDecimalToPercent(oneMonthAgoTfSumStrmPrvdPer) ;
						map.put("tfSumStrmPrvdPer_2" , oneMonthAgoTfSumStrmPrvdPerPercent) ;
					}else{
						map.put("tfSumStrmPrvdPer_2" , "-") ;
					}
					
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
					if( llglwgCompanyInfoPojo.getOneMonthAgoTfSumStrmAllerrPer() != null){
						BigDecimal oneMonthAgoTfSumStrmAllerrPer = llglwgCompanyInfoPojo.getOneMonthAgoTfSumStrmAllerrPer() ;
//						double oneMonthAgoTfSumStrmAllerrPerDouble = getDoubleOnlyTwo(oneMonthAgoTfSumStrmAllerrPer) ;
						double oneMonthAgoSumStrmAllerrPerDouble =  BigDecimalToPercent(oneMonthAgoTfSumStrmAllerrPer) ;
						map.put("tfSumStrmAllerrPer_2" , oneMonthAgoSumStrmAllerrPerDouble) ;
					}else{
						map.put("tfSumStrmAllerrPer_2" , "-") ;
					}
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
				
					
					//两个月前都有值
					
					/*--------------------------------------------------------------------------------------------------------------------------------------*/
					
					map.put("trm_3", llglwgCompanyInfoPojo.getCurrAudTrm()) ;
					map.put("isNotWg_3", llglwgCompanyInfoPojo.getCurrIsNotWg()) ;
					//整理数据格式
					if(llglwgCompanyInfoPojo.getCurrTfSum()  == null){
						map.put("tfSum_3", "-") ;
					}else{
						BigDecimal currTfSumBig = llglwgCompanyInfoPojo.getCurrTfSum() ;
						double currTfSumDouble = getDoubleOnlyTwo(currTfSumBig) ;
						map.put("tfSum_3", currTfSumDouble) ;
					}
					/***************************************************************************************************************************/
					if(llglwgCompanyInfoPojo.getCurrTfSumStrmPrvdPer() == null){
						map.put("tfSumStrmPrvdPer_3", "-") ;
					}else{
						BigDecimal currTfSumStrmPrvdPer= llglwgCompanyInfoPojo.getCurrTfSumStrmPrvdPer() ;
						double currTfSumStrmPrvdPerPercent= BigDecimalToPercent(currTfSumStrmPrvdPer) ;
						map.put("tfSumStrmPrvdPer_3", currTfSumStrmPrvdPerPercent) ;
					}
					/***************************************************************************************************************************/
					if(llglwgCompanyInfoPojo.getCurrTfSumStrmAllerrPer()==null){
						map.put("tfSumStrmAllerrPer_3", "-") ;
					}else{
						BigDecimal currTfSumStrmAllerrPer = llglwgCompanyInfoPojo.getCurrTfSumStrmAllerrPer() ;
//						double currTfSumStrmAllerrPerDouble = getDoubleOnlyTwo(currTfSumStrmAllerrPer) ;
						double currSumStrmAllerrPerDouble =  BigDecimalToPercent(currTfSumStrmAllerrPer) ;
						map.put("tfSumStrmAllerrPer_3", currSumStrmAllerrPerDouble) ;
					}
					/***************************************************************************************************************************/				
				}
			}
			return map ;
	}
	
	//方法计算统付占该省的流量比
	public static double BigDecimalToPercent(BigDecimal bd){
		double bdDouble = bd.doubleValue() ;
		double newBdDouble= bdDouble * 100 ; 
		return newBdDouble ;
	}
	//保留两位小数
	public static double getDoubleOnlyTwo(BigDecimal bd){
		double bdDouble = bd.doubleValue() ;
		 DecimalFormat df = new DecimalFormat("#0.00");
		 String newDoubleStr = df.format(bdDouble) ;
		 double newDouble = Double.parseDouble(newDoubleStr) ;
		 return newDouble ;
	}
}
