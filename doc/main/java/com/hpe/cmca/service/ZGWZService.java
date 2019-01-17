package com.hpe.cmca.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.service.ConcernService;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.job.ZgwzGenFileProcessor;

/**
 * 模板管理
 * <pre>
 * Desc： 
 * @author   issuser
 * @refactor issuser
 * @date     2017-6-14 下午4:40:15
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-6-14 	   issuser 	         1. Created this class. 
 * </pre>
 */
@Service
public class ZGWZService {

	@Autowired
	private MybatisDao	mybatisDao	= null;
	Logger	logger	= Logger.getLogger(ZGWZService.class);
	
	@Autowired
	ZgwzGenFileProcessor zgwzGenFileProcessor;
	
	@Autowired
	NotiFileGenService  notiFileGenService;
	
	@Autowired
	ConcernService  concernService;
	
	Map<String,Object> params =new HashMap<String,Object>();
	
	 SimpleDateFormat sdf =new SimpleDateFormat("yyyy年MM月dd日");
	    
	 SimpleDateFormat sdf2 =new SimpleDateFormat("yyyyMM");
	
	
	public  List<Map<String, Object>> getFileGenList(String selectid,Map params){
		List<Map<String, Object>> result = mybatisDao.getList(selectid, params);
		return result;
	}
	/**
	 * 整改问责数据查询
	 * <pre>
	 * Desc  
	 * @param subject_id
	 * @return
	 * @author issuser
	 * 2017-6-14 下午4:41:42
	 * </pre>
	 */
	public  List<Map<String, Object>> getDataZgwz(String subject_id){
		Map params=new HashMap<String,Object>();
		params.put("subject_id", subject_id);
		List<Map<String, Object>> result = mybatisDao.getList("reportModel.selectAuditzgwz", params);
		return result;
	}
	
	public  List<Map<String,Object>> selectShzgYktldata(String aud_trm,List<Map<String,Object>> resultList) throws Exception{
		//审计整改时间
		String zg_time = getLastDayByAudtrm(aud_trm,6);
		Map<String,Object> params1=new HashMap<String,Object>();
		params1.put("aud_trm", aud_trm);
		List<Map<String,Object>> result = mybatisDao.getList("reportModel.selectAuditYktlParams1", params1);
		List<Map<String,Object>> result_2 = mybatisDao.getList("reportModel.selectAuditYktlParams2", params1);
		List<Map<String,Object>> result_3 = mybatisDao.getList("reportModel.selectAuditYktlParams3", params1);
		List<Map<String,Object>> result_4 = mybatisDao.getList("reportModel.selectAuditYktlParams4", params1);
		List<Map<String,Object>> result_5 = mybatisDao.getList("reportModel.selectAuditYktlParams5", params1);
		Map<String,String> prvd_list =notiFileGenService.getAllPrvdDataWithKeyName();
		String prvdname= null;
		int i=1;
		List<Object> tempList =null;
		if(prvd_list != null && prvd_list.size() > 0){
			Iterator<Map.Entry<String, String>> it = prvd_list.entrySet().iterator();
			  while (it.hasNext()) {
				   params = new HashMap();
				   Map.Entry<String, String> entry = it.next();
				   prvdname =entry.getKey();
				   for(Map<String,Object> mapdata :result){
					   if(mapdata.containsValue(prvdname)){
						   params.putAll(mapdata);
							if(!params.containsKey("zyqd_auditUser")){
								params.put("zyqd_auditUser", " ");
							}
							if(!params.containsKey("zyqd_auditPercent")){
								params.put("zyqd_auditPercent", " ");
							}
							params.put("zyqd_zgsx", zg_time);
					   }
						
				   }
				   for(Map<String,Object> mapdata2 :result_2){
						if(mapdata2.containsValue(prvdname)){
							params.putAll(mapdata2);
							if(!params.containsKey("shqd_auditUser")){
								params.put("shqd_auditUser", " ");
							}
							if(!params.containsKey("shqd_auditPercent")){
								params.put("shqd_auditPercent", " ");
							}
							params.put("shqd_zgsx", zg_time);
						}
				   }
				   for(Map<String,Object> mapdata3 :result_3){
						if(mapdata3.containsValue(prvdname)){
							params.putAll(mapdata3);
							if(!params.containsKey("yk1w_auditCardno")){
								params.put("yk1w_auditCardno", " ");
							}
							params.put("yk1w_zgsx", zg_time);
						}
				   }
				   for(Map<String,Object> mapdata4 :result_4){
						if(mapdata4.containsValue(prvdname)){
							params.putAll(mapdata4);
							if(!params.containsKey("yk5k_auditCardno")){
								params.put("yk5k_auditCardno", " ");
							}
							params.put("yk5k_zgsx", zg_time);
						}
				   }
				   tempList =new ArrayList<Object>();
				   for(Map<String,Object> mapdata5 :result_5){
						if(mapdata5.containsValue(prvdname)){
							if(!tempList.contains(mapdata5.get("ykzg_auditMonth1"))){
								tempList.add(mapdata5.get("ykzg_auditMonth1"));
							}
							if(tempList.size() == 3){
								break;
							}
						}
					}
				   //判断是否
				   String montmp =null;
				   if(tempList.size() == 3){
					   int h=1,h2=1,h3=1,h4=1,k=0;
					   String mon11=null,mon12=null,mon13=null;
					   mon11 =tempList.get(0).toString();
					   mon12 =tempList.get(1).toString();
					   mon13 =tempList.get(2).toString();
					   for(Map<String,Object> mapdata5 :result_5){
							if(mapdata5.containsValue(prvdname)&&tempList.contains(mapdata5.get("ykzg_auditMonth1"))){
								montmp = mapdata5.get("ykzg_auditMonth1").toString();
								zg_time = getLastDayByAudtrm(aud_trm,9);
									if(mapdata5.get("subject_ZG").equals(1)){
										if(montmp.equals(mon11)){
											params.put("ykzg_auditCardno1", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_auditPercent1", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
										}
										if(montmp.equals(mon12)){
											params.put("ykzg_auditCardno5", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_auditPercent3", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
										}
										if(montmp.equals(mon13)){
											params.put("ykzg_auditCardno9", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_auditPercent5", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
										}
//										params.put("ykzg_auditCardno"+(4*(h-1)+1), mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
//										params.put("ykzg_auditPercent"+(4*(h-1)+1), mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
//										params.put("ykzg_auditMonth"+h, mapdata5.get("ykzg_auditMonth1"));
										if(h==1){
											params.put("ykzg_zgsx", zg_time);
										}
										params.put(montmp+"subject_ZG1", "1");
										h++;
									}
									if(mapdata5.get("subject_ZG").equals(2)){
										if(montmp.equals(mon11)){
											params.put("ykzg_auditCardno2", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_auditPercent2", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
										}
										if(montmp.equals(mon12)){
											params.put("ykzg_auditCardno6", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_auditPercent4", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
										}
										if(montmp.equals(mon13)){
											params.put("ykzg_auditCardno10", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_auditPercent6", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
										}
										if(h2==1){
											params.put("ykzg_zgsx", zg_time);
										}
										params.put(montmp+"subject_ZG2", "2");
										h2++;
									}
									if(mapdata5.get("subject_ZG").equals(3)){
										if(montmp.equals(mon11)){
											params.put("ykzg_auditCardno3", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_qudao1", mapdata5.get("ykzg_qudao1"));
										}
										if(montmp.equals(mon12)){
											params.put("ykzg_auditCardno7", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_qudao3", mapdata5.get("ykzg_qudao1"));
										}
										if(montmp.equals(mon13)){
											params.put("ykzg_auditCardno11", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_qudao5", mapdata5.get("ykzg_qudao1"));
										}
										if(h3==1){
											params.put("ykzg_zgsx", zg_time);
											
										}
										params.put(montmp+"subject_ZG3", "3");
										h3++;
									}
									if(mapdata5.get("subject_ZG").equals(4)){
										if(montmp.equals(mon11)){
											params.put("ykzg_auditCardno4", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_qudao2", mapdata5.get("ykzg_qudao1"));
										}
										if(montmp.equals(mon12)){
											params.put("ykzg_auditCardno8", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_qudao4", mapdata5.get("ykzg_qudao1"));							
										}
										if(montmp.equals(mon13)){
											params.put("ykzg_auditCardno12", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_qudao6", mapdata5.get("ykzg_qudao1"));
										}
//										params.put("ykzg_auditMonth"+h4, mapdata5.get("ykzg_auditMonth1"));
										if(h4==1){
											params.put("ykzg_zgsx", zg_time);
										}
										params.put(montmp+"subject_ZG4", "4");
										h4++;
									}
									if(k==0){
										params.put("ykzg_sn", mapdata5.get("ykzg_sn"));
										params.put("province", mapdata5.get("province"));
										params.put("ykzg_auditMonth1", mon11);
										params.put("ykzg_auditMonth2", mon12);
										params.put("ykzg_auditMonth3", mon13);
									}
								k++;	
							}
						}
				   }
				   if(params != null && params.size() > 0){
						List<Map<String, Object>> yktl_data = getDataZgwz("2");
						List<Map<String, Object>> rList = zgwzGenFileProcessor.getQDYKdata(yktl_data,params,prvdname,aud_trm);
						if(rList != null && rList.size() > 0)
							resultList.addAll(rList);
					}
				   i++;
			  }
		}
		return resultList;
	}
	/**
	 * 客户欠费
	 * <pre>
	 * Desc  
	 * @param aud_trm
	 * @param dataList
	 * @return
	 * @throws Exception
	 * @author issuser
	 * 2017-11-6 下午3:25:48
	 * </pre>
	 */
	public  List<Map<String,Object>> selectShzgKhqfdata(String aud_trm,List<Map<String,Object>> resultList) throws Exception{
		Map<String,Object> params1=new HashMap<String,Object>();
		params1.put("aud_trm", aud_trm);
		List<Map<String,Object>> result = mybatisDao.getList("reportModel.selectAuditKhqfZGParams", params1);
		List<Map<String,Object>> result_2 = mybatisDao.getList("reportModel.selectAuditKhqfWZParams", params1);
		Map<String,String> prvd_list =notiFileGenService.getAllPrvdDataWithKeyName();
		String prvdname= null;
		int i=1,custj=0,orgj=0;
		List<Object> tempList =null;
		Map<String,Object> params =null;
		if(prvd_list != null && prvd_list.size() > 0){
			Iterator<Map.Entry<String, String>> it = prvd_list.entrySet().iterator();
			  while (it.hasNext()) {
				    custj=0;orgj=0;
				   params = new HashMap<String,Object>();
				   Map.Entry<String, String> entry = it.next();
				   prvdname =entry.getKey();
				   for(Map<String,Object> mapdata :result){
					   if(mapdata.containsValue(prvdname)){
						   if(mapdata.get("focus_cd").equals("4001")){//集团
//							   params.putAll(mapdata);
							   params.put("orgAmt", mapdata.get("custAmt"));
							   params.put("orgamtRank", mapdata.get("custamtRank"));
							   params.put("orgPer", mapdata.get("custPer"));
							   params.put("orgperRank", mapdata.get("custperRank"));
							   params.put("orgRecoverAmt", mapdata.get("custRecoverAmt"));
							   params.put("orglastPer", mapdata.get("custlastPer"));
							   params.put("audTrm", mapdata.get("audTrm"));
							   orgj++;
						   }
						   if(mapdata.get("focus_cd").equals("4003")){//个人
							   params.putAll(mapdata);
							   custj++;
						   }
						   params.put("prvdName", prvdname);
						   params.put("khqfZgsx", getLastDayByAudtrm(aud_trm,4));
					   }
						
				   }
				   if(custj > 0&&orgj > 0){
					   params.put("custamtSn", 1);
					   params.put("custperSn", 2);
					   params.put("custdqsqSn", 3);
					   params.put("orgamtSn", 4);
					   params.put("orgperSn", 5);
					   params.put("orgdqsqSn", 6);
				   }else if(custj > 0){
					   params.put("custamtSn", 1);
					   params.put("custperSn", 2);
					   params.put("custdqsqSn", 3);
				   }else if(orgj > 0){
					   params.put("orgamtSn", 1);
					   params.put("orgperSn", 2);
					   params.put("orgdqsqSn", 3);
				   }
				  
				   tempList =new ArrayList<Object>();
				   for(Map<String,Object> mapdata5 :result_2){
						if(mapdata5.containsValue(prvdname)){
							if(!tempList.contains(mapdata5.get("aud_trm"))){
								tempList.add(mapdata5.get("aud_trm"));
							}
							if(tempList.size() == 3){
								break;
							}
						}
					}
				   //判断是否
				   String montmp =null;
				   if(tempList.size() == 3){
					   int h=1,h2=1,h3=1,h4=1,k=0;
					   String mon11=null,mon12=null,mon13=null;
					   mon11 =tempList.get(0).toString();
					   mon12 =tempList.get(1).toString();
					   mon13 =tempList.get(2).toString();
					   for(Map<String,Object> mapdata5 :result_2){
							if(mapdata5.containsValue(prvdname)&&tempList.contains(mapdata5.get("aud_trm"))){
								montmp = mapdata5.get("aud_trm").toString();
//								zg_time = getLastDayByAudtrm(aud_trm,9);
								if(montmp.equals(mon11) &&mapdata5.get("focus_cd").equals("4001")){//集团
									params.put("wzAmt3", mapdata5.get("amt_new"));
									params.put("wzRank3", mapdata5.get("amt_new_rk"));
									params.put("wzPer3", mapdata5.get("amt_ratio"));
									params.put("wzRank4", mapdata5.get("amt_ratio_rk"));
									params.put("wzAmt4", mapdata5.get("amt_all_recover"));
									params.put("wzPer4", mapdata5.get("amt_rec_ratio"));
									params.put(montmp+"14001", "14001");
								}	
								if(montmp.equals(mon11) &&mapdata5.get("focus_cd").equals("4003")){//个人
									params.put("wzAmt1", mapdata5.get("amt_new"));
									params.put("wzRank1", mapdata5.get("amt_new_rk"));
									params.put("wzPer1", mapdata5.get("amt_ratio"));
									params.put("wzRank2", mapdata5.get("amt_ratio_rk"));
									params.put("wzAmt2", mapdata5.get("amt_all_recover"));
									params.put("wzPer2", mapdata5.get("amt_rec_ratio"));
									params.put(montmp+"14003", "14003");
								}
								if(montmp.equals(mon12) &&mapdata5.get("focus_cd").equals("4001")){//集团
									params.put("wzAmt7", mapdata5.get("amt_new"));
									params.put("wzRank7", mapdata5.get("amt_new_rk"));
									params.put("wzPer7", mapdata5.get("amt_ratio"));
									params.put("wzRank8", mapdata5.get("amt_ratio_rk"));
									params.put("wzAmt8", mapdata5.get("amt_all_recover"));
									params.put("wzPer8", mapdata5.get("amt_rec_ratio"));
									params.put(montmp+"24001", "24001");
								}	
								if(montmp.equals(mon12) &&mapdata5.get("focus_cd").equals("4003")){//个人
									params.put("wzAmt5", mapdata5.get("amt_new"));
									params.put("wzRank5", mapdata5.get("amt_new_rk"));
									params.put("wzPer5", mapdata5.get("amt_ratio"));
									params.put("wzRank6", mapdata5.get("amt_ratio_rk"));
									params.put("wzAmt6", mapdata5.get("amt_all_recover"));
									params.put("wzPer6", mapdata5.get("amt_rec_ratio"));
									params.put(montmp+"24003", "24003");
								}
								if(montmp.equals(mon13) &&mapdata5.get("focus_cd").equals("4001")){//集团
									params.put("wzAmt11", mapdata5.get("amt_new"));
									params.put("wzRank11", mapdata5.get("amt_new_rk"));
									params.put("wzPer11", mapdata5.get("amt_ratio"));
									params.put("wzRank12", mapdata5.get("amt_ratio_rk"));
									params.put("wzAmt12", mapdata5.get("amt_all_recover"));
									params.put("wzPer12", mapdata5.get("amt_rec_ratio"));
									params.put(montmp+"34001", "34001");
								}	
								if(montmp.equals(mon13) &&mapdata5.get("focus_cd").equals("4003")){//个人
									params.put("wzAmt9", mapdata5.get("amt_new"));
									params.put("wzRank9", mapdata5.get("amt_new_rk"));
									params.put("wzPer9", mapdata5.get("amt_ratio"));
									params.put("wzRank10", mapdata5.get("amt_ratio_rk"));
									params.put("wzAmt10", mapdata5.get("amt_all_recover"));
									params.put("wzPer10", mapdata5.get("amt_rec_ratio"));
									params.put(montmp+"34003", "34003");
								}
							}
						}
					   params.put("wzAudTrm1", mon11);
						params.put("wzAudTrm2", mon12);
						params.put("wzAudTrm3", mon13);
						params.put("prvdName", prvdname);
						params.put("khqfZgsx", getLastDayByAudtrm(mon11,4));
						if(custj > 0&&orgj > 0){
							   params.put("qfwzSn", 7);
						   }else {
							   params.put("qfwzSn", 4);
						   }
				   }
				   if(params != null && params.size() > 0){
						List<Map<String, Object>> khqf_data = getDataZgwz("4");
						List<Map<String, Object>> rList  =zgwzGenFileProcessor.getKHQFdata(khqf_data,params,prvdname,aud_trm);
						if(rList != null && rList.size() > 0)
							resultList.addAll(rList);
						
				   }
				   i++;
			  }
		}
		return resultList;
	}
	/**
	 * 一个省份的客户欠费数据
	 * <pre>
	 * Desc  
	 * @param aud_trm
	 * @param resultList
	 * @return
	 * @throws Exception
	 * @author issuser
	 * 2017-11-6 下午5:14:56
	 * </pre>
	 */
	public  Map<String,Object> selectShzgKhqfdataPRVD(String prvd_id,String prvdname,String aud_trm) throws Exception{
		Map<String,Object> params1=new HashMap<String,Object>();
		params1.put("aud_trm", aud_trm);
		params1.put("prvd_id", prvd_id);
		List<Map<String,Object>> result = mybatisDao.getList("reportModel.selectAuditKhqfZGParams", params1);
		List<Map<String,Object>> result_2 = mybatisDao.getList("reportModel.selectAuditKhqfWZParams", params1);
		int i=1,custj=0,orgj=0;
		List<Object> tempList =null;
	    Map<String,Object>   params = new HashMap<String,Object>();
	   for(Map<String,Object> mapdata :result){
			   if(mapdata.get("focus_cd").equals("4001")){//集团
				   params.put("orgAmt", mapdata.get("custAmt"));
				   params.put("orgamtRank", mapdata.get("custamtRank"));
				   params.put("orgPer", mapdata.get("custPer"));
				   params.put("orgperRank", mapdata.get("custperRank"));
				   params.put("orgRecoverAmt", mapdata.get("custRecoverAmt"));
				   params.put("orglastPer", mapdata.get("custlastPer"));
				   params.put("audTrm", mapdata.get("audTrm"));
				   orgj++;
			   }
			   if(mapdata.get("focus_cd").equals("4003")){//个人
				   params.putAll(mapdata);
				   custj++;
			   }
			   params.put("prvdName", prvdname);
			   params.put("khqfZgsx", getLastDayByAudtrm(aud_trm,4));
			
	   }
	   if(custj > 0&&orgj > 0){
		   params.put("custamtSn", 1);
		   params.put("custperSn", 2);
		   params.put("custdqsqSn", 3);
		   params.put("orgamtSn", 4);
		   params.put("orgperSn", 5);
		   params.put("orgdqsqSn", 6);
	   }else if(custj > 0){
		   params.put("custamtSn", 1);
		   params.put("custperSn", 2);
		   params.put("custdqsqSn", 3);
	   }else if(orgj > 0){
		   params.put("orgamtSn", 1);
		   params.put("orgperSn", 2);
		   params.put("orgdqsqSn", 3);
	   }
	  
	   tempList =new ArrayList<Object>();
	   for(Map<String,Object> mapdata5 :result_2){
				if(!tempList.contains(mapdata5.get("aud_trm"))){
					tempList.add(mapdata5.get("aud_trm"));
				}
				if(tempList.size() == 3){
					break;
				}
		}
	   //判断是否
	   String montmp =null;
	   if(tempList.size() == 3){
		   int h=1,h2=1,h3=1,h4=1,k=0;
		   String mon11=null,mon12=null,mon13=null;
		   mon11 =tempList.get(0).toString();
		   mon12 =tempList.get(1).toString();
		   mon13 =tempList.get(2).toString();
		   for(Map<String,Object> mapdata5 :result_2){
				if(mapdata5.containsValue(prvdname)&&tempList.contains(mapdata5.get("aud_trm"))){
					montmp = mapdata5.get("aud_trm").toString();
//								zg_time = getLastDayByAudtrm(aud_trm,9);
					if(montmp.equals(mon11) &&mapdata5.get("focus_cd").equals("4001")){//集团
						params.put("wzAmt3", mapdata5.get("amt_new"));
						params.put("wzRank3", mapdata5.get("amt_new_rk"));
						params.put("wzPer3", mapdata5.get("amt_ratio"));
						params.put("wzRank4", mapdata5.get("amt_ratio_rk"));
						params.put("wzAmt4", mapdata5.get("amt_all_recover"));
						params.put("wzPer4", mapdata5.get("amt_rec_ratio"));
						params.put(montmp+"14001", "14001");
					}	
					if(montmp.equals(mon11) &&mapdata5.get("focus_cd").equals("4003")){//个人
						params.put("wzAmt1", mapdata5.get("amt_new"));
						params.put("wzRank1", mapdata5.get("amt_new_rk"));
						params.put("wzPer1", mapdata5.get("amt_ratio"));
						params.put("wzRank2", mapdata5.get("amt_ratio_rk"));
						params.put("wzAmt2", mapdata5.get("amt_all_recover"));
						params.put("wzPer2", mapdata5.get("amt_rec_ratio"));
						params.put(montmp+"14003", "14003");
					}
					if(montmp.equals(mon12) &&mapdata5.get("focus_cd").equals("4001")){//集团
						params.put("wzAmt7", mapdata5.get("amt_new"));
						params.put("wzRank7", mapdata5.get("amt_new_rk"));
						params.put("wzPer7", mapdata5.get("amt_ratio"));
						params.put("wzRank8", mapdata5.get("amt_ratio_rk"));
						params.put("wzAmt8", mapdata5.get("amt_all_recover"));
						params.put("wzPer8", mapdata5.get("amt_rec_ratio"));
						params.put(montmp+"24001", "24001");
					}	
					if(montmp.equals(mon12) &&mapdata5.get("focus_cd").equals("4003")){//个人
						params.put("wzAmt5", mapdata5.get("amt_new"));
						params.put("wzRank5", mapdata5.get("amt_new_rk"));
						params.put("wzPer5", mapdata5.get("amt_ratio"));
						params.put("wzRank6", mapdata5.get("amt_ratio_rk"));
						params.put("wzAmt6", mapdata5.get("amt_all_recover"));
						params.put("wzPer6", mapdata5.get("amt_rec_ratio"));
						params.put(montmp+"24003", "24003");
					}
					if(montmp.equals(mon13) &&mapdata5.get("focus_cd").equals("4001")){//集团
						params.put("wzAmt11", mapdata5.get("amt_new"));
						params.put("wzRank11", mapdata5.get("amt_new_rk"));
						params.put("wzPer11", mapdata5.get("amt_ratio"));
						params.put("wzRank12", mapdata5.get("amt_ratio_rk"));
						params.put("wzAmt12", mapdata5.get("amt_all_recover"));
						params.put("wzPer12", mapdata5.get("amt_rec_ratio"));
						params.put(montmp+"34001", "34001");
					}	
					if(montmp.equals(mon13) &&mapdata5.get("focus_cd").equals("4003")){//个人
						params.put("wzAmt9", mapdata5.get("amt_new"));
						params.put("wzRank9", mapdata5.get("amt_new_rk"));
						params.put("wzPer9", mapdata5.get("amt_ratio"));
						params.put("wzRank10", mapdata5.get("amt_ratio_rk"));
						params.put("wzAmt10", mapdata5.get("amt_all_recover"));
						params.put("wzPer10", mapdata5.get("amt_rec_ratio"));
						params.put(montmp+"34003", "34003");
					}
				}
			}
		   params.put("wzAudTrm1", mon11);
			params.put("wzAudTrm2", mon12);
			params.put("wzAudTrm3", mon13);
			params.put("prvdName", prvdname);
			params.put("khqfZgsx", getLastDayByAudtrm(mon11,4));
			if(custj > 0&&orgj > 0){
				   params.put("qfwzSn", 7);
			   }else {
				   params.put("qfwzSn", 4);
			   }
	   }
		return params;
	}
	/**
	 * 有价卡问责
	 * <pre>
	 * Desc  
	 * @param aud_trm
	 * @param resultList
	 * @return
	 * @throws Exception
	 * @author issuser
	 * 2017-8-28 上午10:07:25
	 * </pre>
	 */
	public  List<Map<String,Object>> selectShzgyjkdata(String aud_trm,List<Map<String,Object>> resultList) throws Exception{
		//审计整改时间
		String zg_time = getLastDayByAudtrm(aud_trm,6);
		Map<String,Object> params1=new HashMap<String,Object>();
		params1.put("aud_trm", aud_trm);
		params1.put("infraction_typ", "100000");
		List<Map> result = mybatisDao.getList("reportModel.selectAuditYjkParams1", params1);
		params1.put("infraction_typ", "100301");
		List<Map> result_2 = mybatisDao.getList("reportModel.selectAuditYjkParams2", params1);
		params1.put("infraction_typ", "100502");
		List<Map> result_3 = mybatisDao.getList("reportModel.selectAuditYjkParams3", params1);
		params1.remove("infraction_typ");
		List<Map> result_4 = mybatisDao.getList("reportModel.selectAuditYjkParams4", params1);
		Map<String,String> prvd_list =notiFileGenService.getAllPrvdDataWithKeyName();
		String prvdname= null;
		int i=1;
		List<Object> tmpList =null;
		if(prvd_list != null && prvd_list.size() > 0){
			Iterator<Map.Entry<String, String>> it = prvd_list.entrySet().iterator();
			  while (it.hasNext()) {
				   params = new HashMap();
				   Map.Entry<String, String> entry = it.next();
				   prvdname =entry.getKey();
				   for(Map<String,Object> mapdata :result){
					   if(mapdata.containsValue(prvdname)){
						   params.putAll(mapdata);
							if(!params.containsKey("ztwg_amount1")){
								params.put("ztwg_amount1", " ");
							}
							if(!params.containsKey("ztwg_percent1")){
								params.put("ztwg_percent1", " ");
							}
							params.put("ztwg_zgsj", zg_time);
					   }
						
				   }
				   for(Map<String,Object> mapdata2 :result_2){
						if(mapdata2.containsValue(prvdname)){
							params.putAll(mapdata2);
							if(!params.containsKey("wgzb_percent1")){
								params.put("wgzb_percent1", " ");
							}
							params.put("wgzb_zgsj", zg_time);
						}
				   }
				   for(Map<String,Object> mapdata3 :result_3){
						if(mapdata3.containsValue(prvdname)){
							params.putAll(mapdata3);
							if(!params.containsKey("czvc_number1")){
								params.put("czvc_number1", " ");
							}
							if(!params.containsKey("czvc_amount1")){
								params.put("czvc_amount1", " ");
							}
							if(!params.containsKey("czvc_percent1")){
								params.put("czvc_percent1", " ");
							}
							params.put("czvc_zgsj", zg_time);
						}
				   }
				   int j = 0; 
				   tmpList =new ArrayList<Object>();
				   for(Map<String,Object> mapdata4 :result_4){
					   if(mapdata4.containsValue(prvdname)){
						   if(!tmpList.contains(mapdata4.get("wgyz_auditMonth"))){
							   tmpList.add(mapdata4.get("wgyz_auditMonth"));
						   }
						   if(tmpList.size()==3){
							   break;
						   }
					   }
				   }
				   String monthTmp =null;
				   if(tmpList.size()==3){
					   String mon11=null,mon12=null,mon13=null;
					   mon11 =tmpList.get(0).toString();
					   mon12 =tmpList.get(1).toString();
					   mon13 =tmpList.get(2).toString();
					   int f1=1,f2=1,f3=1,h=0;
					   for(Map<String,Object> mapdata4 :result_4){
						   if(mapdata4.containsValue(prvdname)&&tmpList.contains(mapdata4.get("wgyz_auditMonth"))){
							   monthTmp =mapdata4.get("wgyz_auditMonth").toString();
							   zg_time = getLastDayByAudtrm(aud_trm,7);
//								if(!tmpList.contains(mapdata4.get("wgyz_auditMonth"))){
//									params.put("wgyz_auditMonth"+(h+1), mapdata4.get("wgyz_auditMonth"));
//									h++;
//								}
								//整改1
								if(mapdata4.get("infraction_typ").equals("100000")){
									if(monthTmp.equals(mon11)){
										params.put("wgyz_amount1", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent1", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
									}
									if(monthTmp.equals(mon12)){
										params.put("wgyz_amount4", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent4", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
									}
									if(monthTmp.equals(mon13)){
										params.put("wgyz_amount7", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent7", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
									}
//									params.put("wgyz_amount"+(3*f1-2), mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
//									params.put("wgyz_percent"+(3*f1-2), mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
									params.put(monthTmp+"infraction_typ1", "1");
									f1++;
								}
								//整改2
								if(mapdata4.get("infraction_typ").equals("100301")){
									if(monthTmp.equals(mon11)){
										params.put("wgyz_amount2", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent2", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
									}
									if(monthTmp.equals(mon12)){
										params.put("wgyz_amount5", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent5", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
									}
									if(monthTmp.equals(mon13)){
										params.put("wgyz_amount8", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent8", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
									}
//									params.put("wgyz_amount"+(3*f2-1), mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
//									params.put("wgyz_percent"+(3*f2-1), mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
									params.put(monthTmp+"infraction_typ2", "2");
									f2++;
								}
								//整改3
								if(mapdata4.get("infraction_typ").equals("100502")){
									if(monthTmp.equals(mon11)){
										params.put("wgyz_amount3", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent3", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
									}
									if(monthTmp.equals(mon12)){
										params.put("wgyz_amount6", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent6", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
									}
									if(monthTmp.equals(mon13)){
										params.put("wgyz_amount9", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent9", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
									}
//									params.put("wgyz_amount"+(3*f3), mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
//									params.put("wgyz_percent"+(3*f3), mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
									params.put(monthTmp+"infraction_typ3", "3");
									f3++;
								}
								if(j==0){
									params.put("wgyz_sn", mapdata4.get("wgyz_sn"));
									params.put("province", mapdata4.get("province"));
									params.put("wgyz_zgsj", zg_time);
									params.put("wgyz_auditMonth1", mon11);
									params.put("wgyz_auditMonth2", mon12);
									params.put("wgyz_auditMonth3", mon13);
								}
							}
							j++;
						}
				   }
				   if(params != null && params.size() > 0){
						List<Map<String, Object>> yjk_data = getDataZgwz("1");
						List<Map<String, Object>> rList = zgwzGenFileProcessor.getYJKdata(yjk_data,params,prvdname,aud_trm);
						if(rList != null && rList.size() > 0)
							resultList.addAll(rList);
					}
				   i++;
			  }
		}
		return resultList;
	}
	
	public  List<Map<String,Object>> selectShzgZdtldata(String aud_trm,List<Map<String,Object>> resultList) throws Exception{
		//审计整改时间
		String zg_time = getLastDayByAudtrm(aud_trm,7);
		 params =new HashMap();
		Map<String,Object> params1=new HashMap<String,Object>();
		params1.put("aud_trm", aud_trm);
		List<Map> result = mybatisDao.getList("reportModel.selectAuditZdtlParams1", params1);
		List<Map> result_2 = mybatisDao.getList("reportModel.selectAuditZdtlParams2", params1);
		List<Map> result_3 = mybatisDao.getList("reportModel.selectAuditZdtlParams3", params1);
		Map<String,String> prvd_list =notiFileGenService.getAllPrvdDataWithKeyName();
		String prvdname= null;
		List<Object> tmpList =null;
		if(prvd_list != null && prvd_list.size() > 0){
			Iterator<Map.Entry<String, String>> it = prvd_list.entrySet().iterator();
			  while (it.hasNext()) {
				   params = new HashMap();
				   Map.Entry<String, String> entry = it.next();
				   prvdname =entry.getKey();
				   for(Map<String,Object> mapdata :result){
					   if(mapdata.containsValue(prvdname)){
						   params.putAll(mapdata);
							if(!params.containsKey("ycxs_terminal1")){
								params.put("ycxs_terminal1", " ");
							}
							if(!params.containsKey("ycxs_percent1")){
								params.put("ycxs_percent1", " ");
							}
							params.put("ycxs_zgsx", zg_time);
					   }
				   }
				   for(Map<String,Object> mapdata2 :result_2){
						if(mapdata2.containsValue(prvdname)){
							params.putAll(mapdata2);
							if(!params.containsKey("dyqd_terminal1")){
								params.put("dyqd_terminal1", " ");
							}
							if(!params.containsKey("dyqd_percent1")){
								params.put("dyqd_percent1", " ");
							}
							params.put("dyqd_zgsx", zg_time);
						}
				   }
				  
				   int j = 1; 
				   tmpList =new ArrayList<Object>();
				   for(Map<String,Object> mapdata3 :result_3){
					   if(mapdata3.containsValue(prvdname)){
						   if(!tmpList.contains(mapdata3.get("Aud_trm"))){
							   tmpList.add(mapdata3.get("Aud_trm"));
						   }
						   if(tmpList.size()==3){
							   break;
						   }
					   }
				   }
				   int i=0,xuhao1 =1,xuhao2 = 2;
				   String montmp=null;
				   if(tmpList.size()==3){
					   String mon11=null,mon12=null,mon13=null;
					   mon11 =tmpList.get(0).toString();
					   mon12 =tmpList.get(1).toString();
					   mon13 =tmpList.get(2).toString();
					   for(Map<String,Object> mapdata3 :result_3){
						   if(mapdata3.containsValue(prvdname) && tmpList.contains(mapdata3.get("Aud_trm"))){
							   montmp = mapdata3.get("Aud_trm").toString();
								if(i==0){
									params.put("province", mapdata3.get("province"));
									params.put("zdtl_sn", mapdata3.get("zdtl_sn"));
									params.put("zdtl_zgsx", zg_time);
									params.put("zdtl_auditMonth1",mon11);
									params.put("zdtl_auditMonth2",mon12);
									params.put("zdtl_auditMonth3",mon13);
								}
								i++;
								if(mapdata3.get("xuhao").equals(1)){
									if(montmp.equals(mon11)){
										params.put("zdtl_terminal1", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
										params.put("zdtl_percent1", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
									}
									if(montmp.equals(mon12)){
										params.put("zdtl_terminal3", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
										params.put("zdtl_percent3", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
									}
									if(montmp.equals(mon13)){
										params.put("zdtl_terminal5", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
										params.put("zdtl_percent5", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
									}
//									params.put("zdtl_terminal"+xuhao1, mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
//									params.put("zdtl_percent"+xuhao1, mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
									params.put(montmp+"xuhao1", "1");
									xuhao1+=2;
								}
								if(mapdata3.get("xuhao").equals(2)){
									if(montmp.equals(mon11)){
										params.put("zdtl_terminal2", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
										params.put("zdtl_percent2", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
										params.put("zdtl_qudao1", mapdata3.get("chnl_name")==null?" ":mapdata3.get("chnl_name"));
									}
									if(montmp.equals(mon12)){
										params.put("zdtl_terminal4", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
										params.put("zdtl_percent4", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
										params.put("zdtl_qudao2", mapdata3.get("chnl_name")==null?" ":mapdata3.get("chnl_name"));
									}
									if(montmp.equals(mon13)){
										params.put("zdtl_terminal6", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
										params.put("zdtl_percent6", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
										params.put("zdtl_qudao3", mapdata3.get("chnl_name")==null?" ":mapdata3.get("chnl_name"));
									}
									params.put(montmp+"xuhao2", "2");
									xuhao2+=2;
									j++;
								}
								
							}
						}
				   }
				   if(params != null && params.size() > 0){
						List<Map<String, Object>> zdtl_data = getDataZgwz("3");
						List<Map<String, Object>> rList = zgwzGenFileProcessor.getZDTLdata(zdtl_data,params,prvdname,aud_trm);
						if(rList != null && rList.size() > 0)
							resultList.addAll(rList);
					}
				   i++;
			  }
		}
		return resultList;
	}
	/**
	 * 获取养卡套利参数
	 * <pre>
	 * Desc  
	 * @return
	 * @author issuser
	 * 2017-6-14 下午4:39:54
	 * </pre>
	 */
	public  Map selectAuditYktlParams(String prvd_id,String aud_trm){
		//审计整改时间
		String zg_time = getLastDayByAudtrm(aud_trm,6);
		params =new HashMap();
		Map<String,Object> params1=new HashMap<String,Object>();
		params1.put("aud_trm", aud_trm);
		params1.put("prvd_id", prvd_id);
		List<Map> result = mybatisDao.getList("reportModel.selectAuditYktlParams1", params1);
		if(result != null && result.size() > 0){
			params.putAll(result.get(0));
			if(!params.containsKey("zyqd_auditUser")){
				params.put("zyqd_auditUser", " ");
			}
			if(!params.containsKey("zyqd_auditPercent")){
				params.put("zyqd_auditPercent", " ");
			}
			params.put("zyqd_zgsx", zg_time);
		}
		List<Map> result_2 = mybatisDao.getList("reportModel.selectAuditYktlParams2", params1);
		if(result_2 != null && result_2.size() > 0){
			params.putAll(result_2.get(0));
			if(!params.containsKey("shqd_auditUser")){
				params.put("shqd_auditUser", " ");
			}
			if(!params.containsKey("shqd_auditPercent")){
				params.put("shqd_auditPercent", " ");
			}
			params.put("shqd_zgsx", zg_time);
		}
		List<Map> result_3 = mybatisDao.getList("reportModel.selectAuditYktlParams3", params1);
		if(result_3 != null && result_3.size() > 0){
			params.putAll(result_3.get(0));
			if(!params.containsKey("yk1w_auditCardno")){
				params.put("yk1w_auditCardno", " ");
			}
			params.put("yk1w_zgsx", zg_time);
		}
		List<Map> result_4 = mybatisDao.getList("reportModel.selectAuditYktlParams4", params1);
		if(result_4 != null && result_4.size() > 0){
			params.putAll(result_4.get(0));
			if(!params.containsKey("yk5k_auditCardno")){
				params.put("yk5k_auditCardno", " ");
			}
			params.put("yk5k_zgsx", zg_time);
		}
		List<Map> result_5 = mybatisDao.getList("reportModel.selectAuditYktlParams5", params1);
		List<Object> tempList =new ArrayList<Object>();
		   for(Map<String,Object> mapdata5 :result_5){
				if(!tempList.contains(mapdata5.get("ykzg_auditMonth1"))){
					tempList.add(mapdata5.get("ykzg_auditMonth1"));
				}
				if(tempList.size() == 3){
					break;
				}
			}
		int h=1,h2=1,h3=1,h4=1,k=0;
		zg_time = getLastDayByAudtrm(aud_trm,9);
		if(result_5 != null && result_5.size() > 0){
			 String mon11=null,mon12=null,mon13=null;
			   mon11 =tempList.get(0).toString();
			   mon12 =tempList.get(1).toString();
			   mon13 =tempList.get(2).toString();
			for(Map mapdata5 : result_5){
				if(tempList.contains(mapdata5.get("ykzg_auditMonth1"))){
					String montmp = mapdata5.get("ykzg_auditMonth1").toString();
					zg_time = getLastDayByAudtrm(aud_trm,9);
						if(mapdata5.get("subject_ZG").equals(1)){
							if(montmp.equals(mon11)){
								params.put("ykzg_auditCardno1", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
								params.put("ykzg_auditPercent1", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
							}
							if(montmp.equals(mon12)){
								params.put("ykzg_auditCardno5", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
								params.put("ykzg_auditPercent3", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
							}
							if(montmp.equals(mon13)){
								params.put("ykzg_auditCardno9", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
								params.put("ykzg_auditPercent5", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
							}
//							params.put("ykzg_auditCardno"+(4*(h-1)+1), mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
//							params.put("ykzg_auditPercent"+(4*(h-1)+1), mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
//							params.put("ykzg_auditMonth"+h, mapdata5.get("ykzg_auditMonth1"));
							if(h==1){
								params.put("ykzg_zgsx", zg_time);
							}
							params.put(montmp+"subject_ZG1", "1");
							h++;
						}
						if(mapdata5.get("subject_ZG").equals(2)){
							if(montmp.equals(mon11)){
								params.put("ykzg_auditCardno2", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
								params.put("ykzg_auditPercent2", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
							}
							if(montmp.equals(mon12)){
								params.put("ykzg_auditCardno6", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
								params.put("ykzg_auditPercent4", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
							}
							if(montmp.equals(mon13)){
								params.put("ykzg_auditCardno10", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
								params.put("ykzg_auditPercent6", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
							}
							if(h2==1){
								params.put("ykzg_zgsx", zg_time);
							}
							params.put(montmp+"subject_ZG2", "2");
							h2++;
						}
						if(mapdata5.get("subject_ZG").equals(3)){
							if(montmp.equals(mon11)){
								params.put("ykzg_auditCardno3", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
								params.put("ykzg_qudao1", mapdata5.get("ykzg_qudao1"));
							}
							if(montmp.equals(mon12)){
								params.put("ykzg_auditCardno7", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
								params.put("ykzg_qudao3", mapdata5.get("ykzg_qudao1"));
							}
							if(montmp.equals(mon13)){
								params.put("ykzg_auditCardno11", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
								params.put("ykzg_qudao5", mapdata5.get("ykzg_qudao1"));
							}
							if(h3==1){
								params.put("ykzg_zgsx", zg_time);
								
							}
							params.put(montmp+"subject_ZG3", "3");
							h3++;
						}
						if(mapdata5.get("subject_ZG").equals(4)){
							if(montmp.equals(mon11)){
								params.put("ykzg_auditCardno4", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
								params.put("ykzg_qudao2", mapdata5.get("ykzg_qudao1"));
							}
							if(montmp.equals(mon12)){
								params.put("ykzg_auditCardno8", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
								params.put("ykzg_qudao4", mapdata5.get("ykzg_qudao1"));							
							}
							if(montmp.equals(mon13)){
								params.put("ykzg_auditCardno12", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
								params.put("ykzg_qudao6", mapdata5.get("ykzg_qudao1"));
							}
//							params.put("ykzg_auditMonth"+h4, mapdata5.get("ykzg_auditMonth1"));
							if(h4==1){
								params.put("ykzg_zgsx", zg_time);
							}
							params.put(montmp+"subject_ZG4", "4");
							h4++;
						}
						if(k==0){
							params.put("ykzg_sn", mapdata5.get("ykzg_sn"));
							params.put("province", mapdata5.get("province"));
							params.put("ykzg_auditMonth1", mon11);
							params.put("ykzg_auditMonth2", mon12);
							params.put("ykzg_auditMonth3", mon13);
						}
						k++;
				}
				
			}
//			params.putAll(result_5.get(0));
		}
		return params;
	}
	/**
	 * 获取终端套利参数
	 * <pre>
	 * Desc  
	 * @return
	 * @author issuser
	 * 2017-6-14 下午4:40:30
	 * </pre>
	 */
	public  Map selectAuditZdtlParams(String prvd_id,String aud_trm){
		//审计整改时间
		String zg_time = getLastDayByAudtrm(aud_trm,7);
		 params =new HashMap();
		Map<String,Object> params1=new HashMap<String,Object>();
		params1.put("aud_trm", aud_trm);
		params1.put("prvd_id", prvd_id);
		//整改1 参数
		List<Map> result = mybatisDao.getList("reportModel.selectAuditZdtlParams1", params1);
		if(result != null && result.size() > 0){
			params.putAll(result.get(0));
			if(!params.containsKey("ycxs_terminal1")){
				params.put("ycxs_terminal1", " ");
			}
			if(!params.containsKey("ycxs_percent1")){
				params.put("ycxs_percent1", " ");
			}
			params.put("ycxs_zgsx", zg_time);
		}
		//整改2参数
		List<Map> result_2 = mybatisDao.getList("reportModel.selectAuditZdtlParams2", params1);
		if(result_2 != null && result_2.size() > 0){
			params.putAll(result_2.get(0));
			if(!params.containsKey("dyqd_terminal1")){
				params.put("dyqd_terminal1", " ");
			}
			if(!params.containsKey("dyqd_percent1")){
				params.put("dyqd_percent1", " ");
			}
			params.put("dyqd_zgsx", zg_time);
		}
		//问责参数
		List<Map> result_3 = mybatisDao.getList("reportModel.selectAuditZdtlParams3", params1);
		List audtrms =new ArrayList();
		 int j = 1; 
		  List<Object> tmpList =new ArrayList<Object>();
		   for(Map<String,Object> mapdata3 :result_3){
				   if(!tmpList.contains(mapdata3.get("Aud_trm"))){
					   tmpList.add(mapdata3.get("Aud_trm"));
				   }
				   if(tmpList.size()==3){
					   break;
				   }
		   }
		   int i=0,xuhao1 =1,xuhao2 = 2;
		   String montmp=null;
		   if(tmpList.size()==3){
			   String mon11=null,mon12=null,mon13=null;
			   mon11 =tmpList.get(0).toString();
			   mon12 =tmpList.get(1).toString();
			   mon13 =tmpList.get(2).toString();
			   for(Map<String,Object> mapdata3 :result_3){
				   if(tmpList.contains(mapdata3.get("Aud_trm"))){
					   montmp = mapdata3.get("Aud_trm").toString();
						if(i==0){
							params.put("province", mapdata3.get("province"));
							params.put("zdtl_sn", mapdata3.get("zdtl_sn"));
							params.put("zdtl_zgsx", zg_time);
							params.put("zdtl_auditMonth1",mon11);
							params.put("zdtl_auditMonth2",mon12);
							params.put("zdtl_auditMonth3",mon13);
						}
						i++;
						if(mapdata3.get("xuhao").equals(1)){
							if(montmp.equals(mon11)){
								params.put("zdtl_terminal1", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
								params.put("zdtl_percent1", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
							}
							if(montmp.equals(mon12)){
								params.put("zdtl_terminal3", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
								params.put("zdtl_percent3", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
							}
							if(montmp.equals(mon13)){
								params.put("zdtl_terminal5", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
								params.put("zdtl_percent5", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
							}
//							params.put("zdtl_terminal"+xuhao1, mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
//							params.put("zdtl_percent"+xuhao1, mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
							params.put(montmp+"xuhao1", "1");
							xuhao1+=2;
						}
						if(mapdata3.get("xuhao").equals(2)){
							if(montmp.equals(mon11)){
								params.put("zdtl_terminal2", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
								params.put("zdtl_percent2", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
								params.put("zdtl_qudao1", mapdata3.get("chnl_name")==null?" ":mapdata3.get("chnl_name"));
							}
							if(montmp.equals(mon12)){
								params.put("zdtl_terminal4", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
								params.put("zdtl_percent4", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
								params.put("zdtl_qudao2", mapdata3.get("chnl_name")==null?" ":mapdata3.get("chnl_name"));
							}
							if(montmp.equals(mon13)){
								params.put("zdtl_terminal6", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
								params.put("zdtl_percent6", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
								params.put("zdtl_qudao3", mapdata3.get("chnl_name")==null?" ":mapdata3.get("chnl_name"));
							}
							params.put(montmp+"xuhao2", "2");
							xuhao2+=2;
							j++;
						}
						
					}
				}
		   }
		return params;
	}
	
	/**
	 * 获取有价卡参数
	 * <pre>
	 * Desc  
	 * @return
	 * @author issuser
	 * 2017-6-14 下午4:40:48
	 * </pre>
	 */
	public  Map selectAuditYjkParams(String prvd_id,String aud_trm){
		 params =new HashMap();
		//审计整改时间
		String zg_time = getLastDayByAudtrm(aud_trm,3);
		Map<String,Object> params1=new HashMap<String,Object>();
		params1.put("prvd_id", prvd_id);
		params1.put("aud_trm", aud_trm);
		params1.put("infraction_typ", "100000");
		List<Map> result_1 = mybatisDao.getList("reportModel.selectAuditYjkParams1", params1);
		if(result_1 != null && result_1.size() > 0){
			params.putAll(result_1.get(0));
			if(!params.containsKey("ztwg_amount1")){
				params.put("ztwg_amount1", " ");
			}
			if(!params.containsKey("ztwg_percent1")){
				params.put("ztwg_percent1", " ");
			}
			params.put("ztwg_zgsj", zg_time);
		}
		params1.put("infraction_typ", "100301");
		List<Map> result_2 = mybatisDao.getList("reportModel.selectAuditYjkParams2", params1);
		if(result_2 != null && result_2.size() > 0){
			params.putAll(result_2.get(0));
			if(!params.containsKey("wgzb_percent1")){
				params.put("wgzb_percent1", " ");
			}
			params.put("wgzb_zgsj", zg_time);
		}
		params1.put("infraction_typ", "100502");
		List<Map> result_3 = mybatisDao.getList("reportModel.selectAuditYjkParams3", params1);
		if(result_3 != null && result_3.size() > 0){
			params.putAll(result_3.get(0));
			if(!params.containsKey("czvc_number1")){
				params.put("czvc_number1", " ");
			}
			if(!params.containsKey("czvc_amount1")){
				params.put("czvc_amount1", " ");
			}
			if(!params.containsKey("czvc_percent1")){
				params.put("czvc_percent1", " ");
			}
			params.put("czvc_zgsj", zg_time);
		}
		//有价卡问责
		params1.remove("infraction_typ");
		List<Map> result_4 = mybatisDao.getList("reportModel.selectAuditYjkParams4", params1);
		List months =new ArrayList();
		zg_time = getLastDayByAudtrm(aud_trm,7);
		List<String> tmpList =new ArrayList<String>();
		if(result_4 != null && result_4.size() > 0){
			for(Map map :result_4){
				if(!tmpList.contains(map.get("wgyz_auditMonth"))){
					tmpList.add(map.get("wgyz_auditMonth").toString());
				}
				if(tmpList.size() ==3){
					break;
				}
			}
			int f1=1,f2=1,f3=1,h=0;
			if(tmpList.size()==3){
				String mon11=null,mon12=null,mon13=null;
				   mon11 =tmpList.get(0).toString();
				   mon12 =tmpList.get(1).toString();
				   mon13 =tmpList.get(2).toString();
//				   int f1=1,f2=1,f3=1,h=0;
				for(int j=0;j < result_4.size();j++){
						Map mapdata4 =result_4.get(j);
						if(tmpList.contains(mapdata4.get("wgyz_auditMonth").toString())){
							String monthTmp = mapdata4.get("wgyz_auditMonth").toString();
							zg_time = getLastDayByAudtrm(aud_trm,7);
//							if(!tmpList.contains(mapdata4.get("wgyz_auditMonth"))){
//								params.put("wgyz_auditMonth"+(h+1), mapdata4.get("wgyz_auditMonth"));
//								h++;
//							}
							//整改1
							if(mapdata4.get("infraction_typ").equals("100000")){
								if(monthTmp.equals(mon11)){
									params.put("wgyz_amount1", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
									params.put("wgyz_percent1", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
								}
								if(monthTmp.equals(mon12)){
									params.put("wgyz_amount4", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
									params.put("wgyz_percent4", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
								}
								if(monthTmp.equals(mon13)){
									params.put("wgyz_amount7", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
									params.put("wgyz_percent7", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
								}
//								params.put("wgyz_amount"+(3*f1-2), mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
//								params.put("wgyz_percent"+(3*f1-2), mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
								params.put(monthTmp+"infraction_typ1", "1");
								f1++;
							}
							//整改2
							if(mapdata4.get("infraction_typ").equals("100301")){
								if(monthTmp.equals(mon11)){
									params.put("wgyz_amount2", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
									params.put("wgyz_percent2", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
								}
								if(monthTmp.equals(mon12)){
									params.put("wgyz_amount5", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
									params.put("wgyz_percent5", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
								}
								if(monthTmp.equals(mon13)){
									params.put("wgyz_amount8", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
									params.put("wgyz_percent8", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
								}
//								params.put("wgyz_amount"+(3*f2-1), mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
//								params.put("wgyz_percent"+(3*f2-1), mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
								params.put(monthTmp+"infraction_typ2", "2");
								f2++;
							}
							//整改3
							if(mapdata4.get("infraction_typ").equals("100502")){
								if(monthTmp.equals(mon11)){
									params.put("wgyz_amount3", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
									params.put("wgyz_percent3", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
								}
								if(monthTmp.equals(mon12)){
									params.put("wgyz_amount6", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
									params.put("wgyz_percent6", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
								}
								if(monthTmp.equals(mon13)){
									params.put("wgyz_amount9", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
									params.put("wgyz_percent9", mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
								}
//								params.put("wgyz_amount"+(3*f3), mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
//								params.put("wgyz_percent"+(3*f3), mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
								params.put(monthTmp+"infraction_typ3", "3");
								f3++;
							}
							if(j==0){
								params.put("wgyz_sn", mapdata4.get("wgyz_sn"));
								params.put("province", mapdata4.get("province"));
								params.put("wgyz_zgsj", zg_time);
								params.put("wgyz_auditMonth1", mon11);
								params.put("wgyz_auditMonth2", mon12);
								params.put("wgyz_auditMonth3", mon13);
							}
						}
					
				}
			}
			
		}
		return params;
	}
	
	public  String getLastDayByAudtrm(String audtrm,int months){
    	Calendar c =Calendar.getInstance();
		try {
		    c.setTime(sdf2.parse(audtrm));
		} catch (ParseException e) {
		    e.printStackTrace();
		    logger.error(e.getMessage(),e);
		}
		c.add(Calendar.MONTH, months+1);
		c.set(Calendar.DAY_OF_MONTH, 0);
		String s =sdf.format(c.getTime());
		return s;
    }
	
	public  List<String> selectShzgyjkdataTMP(String aud_trm,List<String> resultList) throws Exception{
		//审计整改时间
		String zg_time = getLastDayByAudtrm(aud_trm,6);
		Map<String,Object> params1=new HashMap<String,Object>();
		params1.put("aud_trm", aud_trm);
		params1.put("infraction_typ", "100000");
		List<Map> result = mybatisDao.getList("reportModel.selectAuditYjkParams1", params1);
		params1.put("infraction_typ", "100301");
		List<Map> result_2 = mybatisDao.getList("reportModel.selectAuditYjkParams2", params1);
		params1.put("infraction_typ", "100502");
		List<Map> result_3 = mybatisDao.getList("reportModel.selectAuditYjkParams3", params1);
		params1.remove("infraction_typ");
		List<Map> result_4 = mybatisDao.getList("reportModel.selectAuditYjkParams4", params1);
		Map<String,String> prvd_list =notiFileGenService.getAllPrvdDataWithKeyName();
		String prvdname= null;
		Map params =null;
		int i=1;
		List<Object> tmpList =null;
		if(prvd_list != null && prvd_list.size() > 0){
			Iterator<Map.Entry<String, String>> it = prvd_list.entrySet().iterator();
			  while (it.hasNext()) {
				   params = new HashMap();
				   Map.Entry<String, String> entry = it.next();
				   prvdname =entry.getKey();
				   for(Map<String,Object> mapdata :result){
					   if(mapdata.containsValue(prvdname)){
						   params.putAll(mapdata);
							if(!params.containsKey("ztwg_amount1")){
								params.put("ztwg_amount1", " ");
							}
							if(!params.containsKey("ztwg_percent1")){
								params.put("ztwg_percent1", " ");
							}else{
								params.put("ztwg_percent1", mapdata.get("ztwg_percent1")==null?" ":concernService.convert(mapdata.get("ztwg_percent1").toString()).replace("%", ""));
							}
							params.put("ztwg_zgsj", zg_time);
					   }
						
				   }
				   for(Map<String,Object> mapdata2 :result_2){
						if(mapdata2.containsValue(prvdname)){
							params.putAll(mapdata2);
							if(!params.containsKey("wgzb_percent1")){
								params.put("wgzb_percent1", " ");
							}else{
								params.put("wgzb_percent1", mapdata2.get("wgzb_percent1")==null?" ":concernService.convert(mapdata2.get("wgzb_percent1").toString()).replace("%", ""));
							}
							params.put("wgzb_zgsj", zg_time);
						}
				   }
				   for(Map<String,Object> mapdata3 :result_3){
						if(mapdata3.containsValue(prvdname)){
							params.putAll(mapdata3);
							if(!params.containsKey("czvc_number1")){
								params.put("czvc_number1", " ");
							}
							if(!params.containsKey("czvc_amount1")){
								params.put("czvc_amount1", " ");
							}
							if(!params.containsKey("czvc_percent1")){
								params.put("czvc_percent1", " ");
							}else{
								params.put("czvc_percent1",mapdata3.get("czvc_percent1")==null?" ": concernService.convert(mapdata3.get("czvc_percent1").toString()).replace("%", ""));
							}
							params.put("czvc_zgsj", zg_time);
						}
				   }
				   int j = 0; 
				   tmpList =new ArrayList<Object>();
				   for(Map<String,Object> mapdata4 :result_4){
					   if(mapdata4.containsValue(prvdname)){
						   if(!tmpList.contains(mapdata4.get("wgyz_auditMonth"))){
							   tmpList.add(mapdata4.get("wgyz_auditMonth"));
						   }
						   if(tmpList.size()==3){
							   break;
						   }
					   }
				   }
				   String monthTmp =null;
				   if(tmpList.size()==3){
					   String mon11=null,mon12=null,mon13=null;
					   mon11 =tmpList.get(0).toString();
					   mon12 =tmpList.get(1).toString();
					   mon13 =tmpList.get(2).toString();
					   int f1=1,f2=1,f3=1,h=0;
					   for(Map<String,Object> mapdata4 :result_4){
						   if(mapdata4.containsValue(prvdname)&&tmpList.contains(mapdata4.get("wgyz_auditMonth"))){
							   monthTmp =mapdata4.get("wgyz_auditMonth").toString();
							   zg_time = getLastDayByAudtrm(aud_trm,7);
//								if(!tmpList.contains(mapdata4.get("wgyz_auditMonth"))){
//									params.put("wgyz_auditMonth"+(h+1), mapdata4.get("wgyz_auditMonth"));
//									h++;
//								}
								//整改1
								if(mapdata4.get("infraction_typ").equals("100000")){
									if(monthTmp.equals(mon11)){
										params.put("wgyz_amount1", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent1", mapdata4.get("wgyz_percent1")==null?" ":concernService.convert(mapdata4.get("wgyz_percent1").toString()).replace("%", ""));
									}
									if(monthTmp.equals(mon12)){
										params.put("wgyz_amount4", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent4", mapdata4.get("wgyz_percent1")==null?" ":concernService.convert(mapdata4.get("wgyz_percent1").toString()).replace("%", ""));
									}
									if(monthTmp.equals(mon13)){
										params.put("wgyz_amount7", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent7", mapdata4.get("wgyz_percent1")==null?" ":concernService.convert(mapdata4.get("wgyz_percent1").toString()).replace("%", ""));
									}
//									params.put("wgyz_amount"+(3*f1-2), mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
//									params.put("wgyz_percent"+(3*f1-2), mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
									params.put(monthTmp+"infraction_typ1", "1");
									f1++;
								}
								//整改2
								if(mapdata4.get("infraction_typ").equals("100301")){
									if(monthTmp.equals(mon11)){
										params.put("wgyz_amount2", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent2", mapdata4.get("wgyz_percent1")==null?" ":concernService.convert(mapdata4.get("wgyz_percent1").toString()).replace("%", ""));
									}
									if(monthTmp.equals(mon12)){
										params.put("wgyz_amount5", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent5", mapdata4.get("wgyz_percent1")==null?" ":concernService.convert(mapdata4.get("wgyz_percent1").toString()).replace("%", ""));
									}
									if(monthTmp.equals(mon13)){
										params.put("wgyz_amount8", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent8", mapdata4.get("wgyz_percent1")==null?" ":concernService.convert(mapdata4.get("wgyz_percent1").toString()).replace("%", ""));
									}
//									params.put("wgyz_amount"+(3*f2-1), mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
//									params.put("wgyz_percent"+(3*f2-1), mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
									params.put(monthTmp+"infraction_typ2", "2");
									f2++;
								}
								//整改3
								if(mapdata4.get("infraction_typ").equals("100502")){
									if(monthTmp.equals(mon11)){
										params.put("wgyz_amount3", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent3", mapdata4.get("wgyz_percent1")==null?" ":concernService.convert(mapdata4.get("wgyz_percent1").toString()).replace("%", ""));
									}
									if(monthTmp.equals(mon12)){
										params.put("wgyz_amount6", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent6", mapdata4.get("wgyz_percent1")==null?" ":concernService.convert(mapdata4.get("wgyz_percent1").toString()).replace("%", ""));
									}
									if(monthTmp.equals(mon13)){
										params.put("wgyz_amount9", mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
										params.put("wgyz_percent9", mapdata4.get("wgyz_percent1")==null?" ":concernService.convert(mapdata4.get("wgyz_percent1").toString()).replace("%", ""));
									}
//									params.put("wgyz_amount"+(3*f3), mapdata4.get("wgyz_amount1")==null?" ":mapdata4.get("wgyz_amount1"));
//									params.put("wgyz_percent"+(3*f3), mapdata4.get("wgyz_percent1")==null?" ":mapdata4.get("wgyz_percent1"));
									params.put(monthTmp+"infraction_typ3", "3");
									f3++;
								}
								if(j==0){
									params.put("wgyz_sn", mapdata4.get("wgyz_sn"));
									params.put("province", mapdata4.get("province"));
									params.put("wgyz_zgsj", zg_time);
									params.put("wgyz_auditMonth1", mon11);
									params.put("wgyz_auditMonth2", mon12);
									params.put("wgyz_auditMonth3", mon13);
								}
							}
							j++;
						}
				   }
				   if(params != null && params.size() > 0){
						List<Map<String, Object>> yjk_data = getDataZgwz("1");
						String filep =zgwzGenFileProcessor.writeExcel3(yjk_data,params,prvdname);
						if(filep != null && !filep.equals("")){
							resultList.add(filep);
						}
					}
				   i++;
			  }
		}
		return resultList;
	}
	/**
	 * 养卡
	 * <pre>
	 * Desc  
	 * @param aud_trm
	 * @param dataList
	 * @return
	 * @throws Exception
	 * @author issuser
	 * 2017-12-28 下午2:00:54
	 * </pre>
	 */
	public  List<String> selectShzgYktldataTMP(String aud_trm,List<String> dataList) throws Exception{
		//审计整改时间
		String zg_time = getLastDayByAudtrm(aud_trm,6);
		Map<String,Object> params1=new HashMap<String,Object>();
		params1.put("aud_trm", aud_trm);
		List<Map<String,Object>> result = mybatisDao.getList("reportModel.selectAuditYktlParams1", params1);
		List<Map<String,Object>> result_2 = mybatisDao.getList("reportModel.selectAuditYktlParams2", params1);
		List<Map<String,Object>> result_3 = mybatisDao.getList("reportModel.selectAuditYktlParams3", params1);
		List<Map<String,Object>> result_4 = mybatisDao.getList("reportModel.selectAuditYktlParams4", params1);
		List<Map<String,Object>> result_5 = mybatisDao.getList("reportModel.selectAuditYktlParams5", params1);
		Map<String,String> prvd_list =notiFileGenService.getAllPrvdDataWithKeyName();
		String prvdname= null;
		int i=1;
		List<Object> tempList =null;
		Map params =null;
		if(prvd_list != null && prvd_list.size() > 0){
			Iterator<Map.Entry<String, String>> it = prvd_list.entrySet().iterator();
			  while (it.hasNext()) {
				   params = new HashMap();
				   Map.Entry<String, String> entry = it.next();
				   prvdname =entry.getKey();
				   for(Map<String,Object> mapdata :result){
					   if(mapdata.containsValue(prvdname)){
						   params.putAll(mapdata);
							if(!params.containsKey("zyqd_auditUser")){
								params.put("zyqd_auditUser", " ");
							}
							if(!params.containsKey("zyqd_auditPercent")){
								params.put("zyqd_auditPercent", " ");
							}
							params.put("zyqd_zgsx", zg_time);
					   }
						
				   }
				   for(Map<String,Object> mapdata2 :result_2){
						if(mapdata2.containsValue(prvdname)){
							params.putAll(mapdata2);
							if(!params.containsKey("shqd_auditUser")){
								params.put("shqd_auditUser", " ");
							}
							if(!params.containsKey("shqd_auditPercent")){
								params.put("shqd_auditPercent", " ");
							}
							params.put("shqd_zgsx", zg_time);
						}
				   }
				   for(Map<String,Object> mapdata3 :result_3){
						if(mapdata3.containsValue(prvdname)){
							params.putAll(mapdata3);
							if(!params.containsKey("yk1w_auditCardno")){
								params.put("yk1w_auditCardno", " ");
							}
							params.put("yk1w_zgsx", zg_time);
						}
				   }
				   for(Map<String,Object> mapdata4 :result_4){
						if(mapdata4.containsValue(prvdname)){
							params.putAll(mapdata4);
							if(!params.containsKey("yk5k_auditCardno")){
								params.put("yk5k_auditCardno", " ");
							}
							params.put("yk5k_zgsx", zg_time);
						}
				   }
				   tempList =new ArrayList<Object>();
				   for(Map<String,Object> mapdata5 :result_5){
						if(mapdata5.containsValue(prvdname)){
							if(!tempList.contains(mapdata5.get("ykzg_auditMonth1"))){
								tempList.add(mapdata5.get("ykzg_auditMonth1"));
							}
							if(tempList.size() == 3){
								break;
							}
						}
					}
				   //判断是否
				   String montmp =null;
				   if(tempList.size() == 3){
					   int h=1,h2=1,h3=1,h4=1,k=0;
					   String mon11=null,mon12=null,mon13=null;
					   mon11 =tempList.get(0).toString();
					   mon12 =tempList.get(1).toString();
					   mon13 =tempList.get(2).toString();
					   for(Map<String,Object> mapdata5 :result_5){
							if(mapdata5.containsValue(prvdname)&&tempList.contains(mapdata5.get("ykzg_auditMonth1"))){
								montmp = mapdata5.get("ykzg_auditMonth1").toString();
								zg_time = getLastDayByAudtrm(aud_trm,9);
									if(mapdata5.get("subject_ZG").equals(1)){
										if(montmp.equals(mon11)){
											params.put("ykzg_auditCardno1", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_auditPercent1", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
										}
										if(montmp.equals(mon12)){
											params.put("ykzg_auditCardno5", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_auditPercent3", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
										}
										if(montmp.equals(mon13)){
											params.put("ykzg_auditCardno9", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_auditPercent5", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
										}
//										params.put("ykzg_auditCardno"+(4*(h-1)+1), mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
//										params.put("ykzg_auditPercent"+(4*(h-1)+1), mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
//										params.put("ykzg_auditMonth"+h, mapdata5.get("ykzg_auditMonth1"));
										if(h==1){
											params.put("ykzg_zgsx", zg_time);
										}
										params.put(montmp+"subject_ZG1", "1");
										h++;
									}
									if(mapdata5.get("subject_ZG").equals(2)){
										if(montmp.equals(mon11)){
											params.put("ykzg_auditCardno2", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_auditPercent2", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
										}
										if(montmp.equals(mon12)){
											params.put("ykzg_auditCardno6", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_auditPercent4", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
										}
										if(montmp.equals(mon13)){
											params.put("ykzg_auditCardno10", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_auditPercent6", mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
										}
//										params.put("ykzg_auditCardno"+(4*(h2-1)+2), mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
//										params.put("ykzg_auditPercent"+(2*h2), mapdata5.get("ykzg_auditPercent1")==null?" ":mapdata5.get("ykzg_auditPercent1"));
//										params.put("ykzg_auditMonth"+h2, mapdata5.get("ykzg_auditMonth1"));
										if(h2==1){
											params.put("ykzg_zgsx", zg_time);
										}
										params.put(montmp+"subject_ZG2", "2");
										h2++;
									}
									if(mapdata5.get("subject_ZG").equals(3)){
										if(montmp.equals(mon11)){
											params.put("ykzg_auditCardno3", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_qudao1", mapdata5.get("ykzg_qudao1"));
										}
										if(montmp.equals(mon12)){
											params.put("ykzg_auditCardno7", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_qudao3", mapdata5.get("ykzg_qudao1"));
										}
										if(montmp.equals(mon13)){
											params.put("ykzg_auditCardno11", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_qudao5", mapdata5.get("ykzg_qudao1"));
										}
//										params.put("ykzg_auditCardno"+(4*(h3-1)+3), mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
//										params.put("ykzg_qudao"+(2*h3-1), mapdata5.get("ykzg_qudao1"));
//										params.put("ykzg_auditMonth"+h3, mapdata5.get("ykzg_auditMonth1"));
										if(h3==1){
											params.put("ykzg_zgsx", zg_time);
											
										}
										params.put(montmp+"subject_ZG3", "3");
										h3++;
									}
									if(mapdata5.get("subject_ZG").equals(4)){
										if(montmp.equals(mon11)){
											params.put("ykzg_auditCardno4", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_qudao2", mapdata5.get("ykzg_qudao1"));
										}
										if(montmp.equals(mon12)){
											params.put("ykzg_auditCardno8", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_qudao4", mapdata5.get("ykzg_qudao1"));							
										}
										if(montmp.equals(mon13)){
											params.put("ykzg_auditCardno12", mapdata5.get("ykzg_auditCardno")==null?" ":mapdata5.get("ykzg_auditCardno"));
											params.put("ykzg_qudao6", mapdata5.get("ykzg_qudao1"));
										}
//										params.put("ykzg_auditMonth"+h4, mapdata5.get("ykzg_auditMonth1"));
										if(h4==1){
											params.put("ykzg_zgsx", zg_time);
										}
										params.put(montmp+"subject_ZG4", "4");
										h4++;
									}
									if(k==0){
										params.put("ykzg_sn", mapdata5.get("ykzg_sn"));
										params.put("province", mapdata5.get("province"));
										params.put("ykzg_auditMonth1", mon11);
										params.put("ykzg_auditMonth2", mon12);
										params.put("ykzg_auditMonth3", mon13);
									}
								k++;	
							}
						}
				   }
				   if(params != null && params.size() > 0){
						List<Map<String, Object>> yktl_data = getDataZgwz("2");
						String filep  =zgwzGenFileProcessor.writeExcel1(yktl_data,params,prvdname);
						if(filep != null && !filep.equals("")){
							dataList.add(filep);
						}
						
				   }
				   i++;
			  }
		}
		return dataList;
	}
	
	public  List<String> selectShzgZdtldataTMP(String aud_trm,List<String> resultList) throws Exception{
		//审计整改时间
		String zg_time = getLastDayByAudtrm(aud_trm,7);
		 params =new HashMap();
		Map<String,Object> params1=new HashMap<String,Object>();
		params1.put("aud_trm", aud_trm);
		List<Map> result = mybatisDao.getList("reportModel.selectAuditZdtlParams1", params1);
		List<Map> result_2 = mybatisDao.getList("reportModel.selectAuditZdtlParams2", params1);
		List<Map> result_3 = mybatisDao.getList("reportModel.selectAuditZdtlParams3", params1);
		Map<String,String> prvd_list =notiFileGenService.getAllPrvdDataWithKeyName();
		String prvdname= null;
		List<Object> tmpList =null;
		Map params =null;
		if(prvd_list != null && prvd_list.size() > 0){
			Iterator<Map.Entry<String, String>> it = prvd_list.entrySet().iterator();
			  while (it.hasNext()) {
				   params = new HashMap();
				   Map.Entry<String, String> entry = it.next();
				   prvdname =entry.getKey();
				   for(Map<String,Object> mapdata :result){
					   if(mapdata.containsValue(prvdname)){
						   params.putAll(mapdata);
							if(!params.containsKey("ycxs_terminal1")){
								params.put("ycxs_terminal1", " ");
							}
							if(!params.containsKey("ycxs_percent1")){
								params.put("ycxs_percent1", " ");
							}
							params.put("ycxs_zgsx", zg_time);
					   }
				   }
				   for(Map<String,Object> mapdata2 :result_2){
						if(mapdata2.containsValue(prvdname)){
							params.putAll(mapdata2);
							if(!params.containsKey("dyqd_terminal1")){
								params.put("dyqd_terminal1", " ");
							}
							if(!params.containsKey("dyqd_percent1")){
								params.put("dyqd_percent1", " ");
							}
							params.put("dyqd_zgsx", zg_time);
						}
				   }
				  
				   int j = 1; 
				   tmpList =new ArrayList<Object>();
				   for(Map<String,Object> mapdata3 :result_3){
					   if(mapdata3.containsValue(prvdname)){
						   if(!tmpList.contains(mapdata3.get("Aud_trm"))){
							   tmpList.add(mapdata3.get("Aud_trm"));
						   }
						   if(tmpList.size()==3){
							   break;
						   }
					   }
				   }
				   int i=0,xuhao1 =1,xuhao2 = 2;
				   if(tmpList.size()==3){
					   String mon11=null,mon12=null,mon13=null,montmp=null;
					   mon11 =tmpList.get(0).toString();
					   mon12 =tmpList.get(1).toString();
					   mon13 =tmpList.get(2).toString();
					   for(Map<String,Object> mapdata3 :result_3){
						   if(mapdata3.containsValue(prvdname) && tmpList.contains(mapdata3.get("Aud_trm"))){
							   montmp = mapdata3.get("Aud_trm").toString();
								if(i==0){
									params.put("province", mapdata3.get("province"));
									params.put("zdtl_sn", mapdata3.get("zdtl_sn"));
									params.put("zdtl_zgsx", zg_time);
									params.put("zdtl_auditMonth1",mon11);
									params.put("zdtl_auditMonth2",mon12);
									params.put("zdtl_auditMonth3",mon13);
								}
								i++;
								if(mapdata3.get("xuhao").equals(1)){
									if(montmp.equals(mon11)){
										params.put("zdtl_terminal1", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
										params.put("zdtl_percent1", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
									}
									if(montmp.equals(mon12)){
										params.put("zdtl_terminal3", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
										params.put("zdtl_percent3", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
									}
									if(montmp.equals(mon13)){
										params.put("zdtl_terminal5", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
										params.put("zdtl_percent5", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
									}
//									params.put("zdtl_terminal"+xuhao1, mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
//									params.put("zdtl_percent"+xuhao1, mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
									params.put(montmp+"xuhao1", "1");
									xuhao1+=2;
								}
								if(mapdata3.get("xuhao").equals(2)){
									if(montmp.equals(mon11)){
										params.put("zdtl_terminal2", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
										params.put("zdtl_percent2", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
										params.put("zdtl_qudao1", mapdata3.get("chnl_name")==null?" ":mapdata3.get("chnl_name"));
									}
									if(montmp.equals(mon12)){
										params.put("zdtl_terminal4", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
										params.put("zdtl_percent4", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
										params.put("zdtl_qudao2", mapdata3.get("chnl_name")==null?" ":mapdata3.get("chnl_name"));
									}
									if(montmp.equals(mon13)){
										params.put("zdtl_terminal6", mapdata3.get("infraction_num")==null?" ":mapdata3.get("infraction_num"));
										params.put("zdtl_percent6", mapdata3.get("weigui_percent")==null?" ":mapdata3.get("weigui_percent"));
										params.put("zdtl_qudao3", mapdata3.get("chnl_name")==null?" ":mapdata3.get("chnl_name"));
									}
									params.put(montmp+"xuhao2", "2");
									xuhao2+=2;
									j++;
								}
								
							}
						}
				   }
				   if(params != null && params.size() > 0){
						List<Map<String, Object>> zdtl_data = getDataZgwz("3");
						String filep =zgwzGenFileProcessor.writeExcel2(zdtl_data,params,prvdname);
						if(filep != null && !filep.equals("")){
							resultList.add(filep);
						}
					}
				   i++;
			  }
		}
		return resultList;
	}
	
	
	public  List<String> selectShzgKhqfdataTMP(String aud_trm,List<String> dataList) throws Exception{
		//审计整改时间
		String zg_time = "";
		Map<String,Object> params1=new HashMap<String,Object>();
		params1.put("aud_trm", aud_trm);
		List<Map<String,Object>> result = mybatisDao.getList("reportModel.selectAuditKhqfZGParams", params1);
		List<Map<String,Object>> result_2 = mybatisDao.getList("reportModel.selectAuditKhqfWZParams", params1);
		Map<String,String> prvd_list =notiFileGenService.getAllPrvdDataWithKeyName();
		String prvdname= null;
		int i=1,custj=0,orgj=0;
		List<Object> tempList =null;
		Map params =null;
		if(prvd_list != null && prvd_list.size() > 0){
			Iterator<Map.Entry<String, String>> it = prvd_list.entrySet().iterator();
			  while (it.hasNext()) {
				    custj=0;orgj=0;
				   params = new HashMap();
				   Map.Entry<String, String> entry = it.next();
				   prvdname =entry.getKey();
				   for(Map<String,Object> mapdata :result){
					   if(mapdata.containsValue(prvdname)){
						   if(mapdata.get("focus_cd").equals("4001")){//集团
//							   params.putAll(mapdata);
							   params.put("orgAmt", mapdata.get("custAmt"));
							   params.put("orgamtRank", mapdata.get("custamtRank"));
							   params.put("orgPer", mapdata.get("custPer"));
							   params.put("orgperRank", mapdata.get("custperRank"));
							   params.put("orgRecoverAmt", mapdata.get("custRecoverAmt"));
							   params.put("orglastPer", mapdata.get("custlastPer"));
							   params.put("audTrm", mapdata.get("audTrm"));
							   orgj++;
						   }
						   if(mapdata.get("focus_cd").equals("4003")){//个人
							   params.putAll(mapdata);
							   custj++;
						   }
						   params.put("prvdName", prvdname);
						   params.put("khqfZgsx", getLastDayByAudtrm(aud_trm,4));
					   }
						
				   }
				   if(custj > 0&&orgj > 0){
					   params.put("custamtSn", 1);
					   params.put("custperSn", 2);
					   params.put("custdqsqSn", 3);
					   params.put("orgamtSn", 4);
					   params.put("orgperSn", 5);
					   params.put("orgdqsqSn", 6);
				   }else if(custj > 0){
					   params.put("custamtSn", 1);
					   params.put("custperSn", 2);
					   params.put("custdqsqSn", 3);
				   }else if(orgj > 0){
					   params.put("orgamtSn", 1);
					   params.put("orgperSn", 2);
					   params.put("orgdqsqSn", 3);
				   }
				  
				   tempList =new ArrayList<Object>();
				   for(Map<String,Object> mapdata5 :result_2){
						if(mapdata5.containsValue(prvdname)){
							if(!tempList.contains(mapdata5.get("aud_trm"))){
								tempList.add(mapdata5.get("aud_trm"));
							}
							if(tempList.size() == 3){
								break;
							}
						}
					}
				   //判断是否
				   String montmp =null;
				   if(tempList.size() == 3){
					   int h=1,h2=1,h3=1,h4=1,k=0;
					   String mon11=null,mon12=null,mon13=null;
					   mon11 =tempList.get(0).toString();
					   mon12 =tempList.get(1).toString();
					   mon13 =tempList.get(2).toString();
					   for(Map<String,Object> mapdata5 :result_2){
							if(mapdata5.containsValue(prvdname)&&tempList.contains(mapdata5.get("aud_trm"))){
								montmp = mapdata5.get("aud_trm").toString();
//								zg_time = getLastDayByAudtrm(aud_trm,9);
								if(montmp.equals(mon11) &&mapdata5.get("focus_cd").equals("4001")){//集团
									params.put("wzAmt3", mapdata5.get("amt_new"));
									params.put("wzRank3", mapdata5.get("amt_new_rk"));
									params.put("wzPer3", mapdata5.get("amt_ratio"));
									params.put("wzRank4", mapdata5.get("amt_ratio_rk"));
									params.put("wzAmt4", mapdata5.get("amt_all_recover"));
									params.put("wzPer4", mapdata5.get("amt_rec_ratio"));
									params.put(montmp+"14001", "14001");
								}	
								if(montmp.equals(mon11) &&mapdata5.get("focus_cd").equals("4003")){//个人
									params.put("wzAmt1", mapdata5.get("amt_new"));
									params.put("wzRank1", mapdata5.get("amt_new_rk"));
									params.put("wzPer1", mapdata5.get("amt_ratio"));
									params.put("wzRank2", mapdata5.get("amt_ratio_rk"));
									params.put("wzAmt2", mapdata5.get("amt_all_recover"));
									params.put("wzPer2", mapdata5.get("amt_rec_ratio"));
									params.put(montmp+"14003", "14003");
								}
								if(montmp.equals(mon12) &&mapdata5.get("focus_cd").equals("4001")){//集团
									params.put("wzAmt7", mapdata5.get("amt_new"));
									params.put("wzRank7", mapdata5.get("amt_new_rk"));
									params.put("wzPer7", mapdata5.get("amt_ratio"));
									params.put("wzRank8", mapdata5.get("amt_ratio_rk"));
									params.put("wzAmt8", mapdata5.get("amt_all_recover"));
									params.put("wzPer8", mapdata5.get("amt_rec_ratio"));
									params.put(montmp+"24001", "24001");
								}	
								if(montmp.equals(mon12) &&mapdata5.get("focus_cd").equals("4003")){//个人
									params.put("wzAmt5", mapdata5.get("amt_new"));
									params.put("wzRank5", mapdata5.get("amt_new_rk"));
									params.put("wzPer5", mapdata5.get("amt_ratio"));
									params.put("wzRank6", mapdata5.get("amt_ratio_rk"));
									params.put("wzAmt6", mapdata5.get("amt_all_recover"));
									params.put("wzPer6", mapdata5.get("amt_rec_ratio"));
									params.put(montmp+"24003", "24003");
								}
								if(montmp.equals(mon13) &&mapdata5.get("focus_cd").equals("4001")){//集团
									params.put("wzAmt11", mapdata5.get("amt_new"));
									params.put("wzRank11", mapdata5.get("amt_new_rk"));
									params.put("wzPer11", mapdata5.get("amt_ratio"));
									params.put("wzRank12", mapdata5.get("amt_ratio_rk"));
									params.put("wzAmt12", mapdata5.get("amt_all_recover"));
									params.put("wzPer12", mapdata5.get("amt_rec_ratio"));
									params.put(montmp+"34001", "34001");
								}	
								if(montmp.equals(mon13) &&mapdata5.get("focus_cd").equals("4003")){//个人
									params.put("wzAmt9", mapdata5.get("amt_new"));
									params.put("wzRank9", mapdata5.get("amt_new_rk"));
									params.put("wzPer9", mapdata5.get("amt_ratio"));
									params.put("wzRank10", mapdata5.get("amt_ratio_rk"));
									params.put("wzAmt10", mapdata5.get("amt_all_recover"));
									params.put("wzPer10", mapdata5.get("amt_rec_ratio"));
									params.put(montmp+"34003", "34003");
								}
							}
						}
					   params.put("wzAudTrm1", mon11);
						params.put("wzAudTrm2", mon12);
						params.put("wzAudTrm3", mon13);
						params.put("prvdName", prvdname);
						params.put("khqfZgsx", getLastDayByAudtrm(mon11,4));
						if(custj > 0&&orgj > 0){
							   params.put("qfwzSn", 7);
						   }else {
							   params.put("qfwzSn", 4);
						   }
				   }
				   if(params != null && params.size() > 0){
						List<Map<String, Object>> yktl_data = getDataZgwz("4");
						String filep  =zgwzGenFileProcessor.writeExcelKhqf(yktl_data,params,prvdname);
						if(filep != null && !filep.equals("")){
							dataList.add(filep);
						}
						
				   }
				   i++;
			  }
		}
		return dataList;
	}
	
	public static void main(String[] args){
		ZGWZService  z =new ZGWZService();
		System.out.println(z.getLastDayByAudtrm("201701",3));
	}
}
