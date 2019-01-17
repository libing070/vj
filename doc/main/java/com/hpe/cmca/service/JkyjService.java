package com.hpe.cmca.service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.JkyjMapper;
import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.util.Json;
/**
 * 
 * <pre>
 * Desc： 
 * @author   issuser
 * @refactor issuser
 * @date     2017-7-12 下午2:50:53
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-7-12 	   issuser 	         1. Created this class. 
 * </pre>
 */
@Service("jkyjService")
public class JkyjService extends BaseObject{
	
	@Autowired
	protected MybatisDao mybatisDao;
	
	
	public Map<String,Object> getSubjectInfo(ParameterData m){
	    JkyjMapper jkyjMapper =  mybatisDao.getSqlSession().getMapper(JkyjMapper.class);
	    Map<String,Object> result =new HashMap<String,Object>();
	    List<Map<String,Object>> mod_list = null;
	    List<Map<String,Object>> subject_result =jkyjMapper.getSubjectInfo(m);
	    List<Map<String,Object>> module_result =jkyjMapper.getModuleInfo(null);
	    if(subject_result.size() > 0){
	    	for(Map<String,Object> sub : subject_result){
	    		mod_list =new ArrayList<Map<String,Object>>();
	    		for(Map<String,Object> mod : module_result){
	    			if(sub.get("subject_id").toString().contains(mod.get("subject_id").toString())){
	    				mod_list.add(mod); 
	    			}
	    		}
	    		result.put(sub.get("subject_id").toString(), mod_list);
	    	}
	    }
	    return result;
	}
	
	public List<Map<String,Object>> getModuleInfo(ParameterData m){
	    JkyjMapper jkyjMapper =  mybatisDao.getSqlSession().getMapper(JkyjMapper.class);
	    return jkyjMapper.getModuleInfo(m);
	}
	
	public List<Map<String,Object>> getConcernInfo(ParameterData m){
	    JkyjMapper jkyjMapper =  mybatisDao.getSqlSession().getMapper(JkyjMapper.class);
	    return jkyjMapper.getConcernInfo(m);
	}
	
	public Map<String,Object> getChartInfo(ParameterData m){
	    JkyjMapper jkyjMapper =  mybatisDao.getSqlSession().getMapper(JkyjMapper.class);
	    List<Map<String,Object>> dataList =jkyjMapper.getModuleInfo(m);
	    List<Map<String,Object>> charinfo =new ArrayList<Map<String,Object>>();
	    List<BigDecimal> amtList =new ArrayList<BigDecimal>();
	    List<String> audtrmList =new ArrayList<String>();
	    Map<String,Object> result=new HashMap<String,Object>();
	    if(dataList.size() > 0){
	    	Map<String,Object> mp= dataList.get(0);
	    	m.setSqlChart(mp.get("sql_text_line").toString());
	    	charinfo =jkyjMapper.getChartInfo(m);
	    	for(Map<String,Object> mo :charinfo){
	    		amtList.add(
	    			mo.get("infraction_amt")==null||mo.get("infraction_amt").equals("")?BigDecimal.valueOf(0.0):(BigDecimal)mo.get("infraction_amt"));
	    		audtrmList.add(mo.get("Aud_trm").toString());
	    	}
	    	result.putAll(mp);
	    }
	    result.put("amtList", amtList);
	    result.put("audtrmList", audtrmList);
	    return result;
	}
	
	public Map<String,Object> getColumnInfo(ParameterData m){
		List<String> prvdList =new ArrayList<String>();
		List<Long[]> numList =new ArrayList<Long[]>();
		Map<String,Object> tmpMap = new HashMap<String,Object>();
	    JkyjMapper jkyjMapper =  mybatisDao.getSqlSession().getMapper(JkyjMapper.class);
	    List<Map<String,Object>> colsList =jkyjMapper.getColumnInfo(m);
	    if(colsList.size() > 0){
	    	for(Map<String,Object> prvddata : colsList){
	    		Long[] tmpl=new Long[5];
	    		prvdList.add(prvddata.get("prvdName").toString());
	    		tmpl[0] =Long.valueOf(prvddata.get("yctfNum").toString());
	    		tmpl[1] =Long.valueOf(prvddata.get("hfzsNum").toString());
	    		tmpl[2] =Long.valueOf(prvddata.get("jfzyNum").toString());
	    		tmpl[3] =Long.valueOf(prvddata.get("jfzsNum").toString());
	    		numList.add(tmpl);
	    	}
	    }
	    tmpMap.put("prvdList", prvdList);
	    tmpMap.put("numList", numList);
	    return tmpMap;
	}
	
	public Map<String,Object> getLineInfo(ParameterData m){
		List<String> audtrmList =new ArrayList<String>();
		Map<String,Object> tmpObj =new HashMap<String,Object>();
		List<Long> num1 =new ArrayList<Long>();
		List<Long> num2 =new ArrayList<Long>();
		List<Long> num3 =new ArrayList<Long>();
		List<Long> num4 =new ArrayList<Long>();
	    JkyjMapper jkyjMapper =  mybatisDao.getSqlSession().getMapper(JkyjMapper.class);
	    List<Map<String,Object>> colsList =jkyjMapper.getColumnInfo(m);
	    if(colsList.size() > 0){
	    	for(Map<String,Object> prvddata : colsList){
	    		audtrmList.add(prvddata.get("audTrm").toString());
	    		num1.add(Long.valueOf(prvddata.get("yctfNum").toString()));
	    		num2.add(Long.valueOf(prvddata.get("hfzsNum").toString()));
	    		num3.add(Long.valueOf(prvddata.get("jfzyNum").toString()));
	    		num4.add(Long.valueOf(prvddata.get("jfzsNum").toString()));
	    	}
	    }
	    tmpObj.put("audtrmList", audtrmList);
	    tmpObj.put("num1", num1);
	    tmpObj.put("num2", num2);
	    tmpObj.put("num3", num3);
	    tmpObj.put("num4", num4);
	    return tmpObj;
	}
	
	public Map<String,Object> getDuijiInfo(ParameterData m){
		List<String> audtrmList =new ArrayList<String>();
		Map<String,Object> tmpObj =new HashMap<String,Object>();
		List<Long> num1 =new ArrayList<Long>();
		List<Long> num2 =new ArrayList<Long>();
		List<Long> num3 =new ArrayList<Long>();
		List<Long> num4 =new ArrayList<Long>();
	    JkyjMapper jkyjMapper =  mybatisDao.getSqlSession().getMapper(JkyjMapper.class);
	    List<Map<String,Object>> colsList =jkyjMapper.getColumnInfo(m);
	    if(colsList.size() > 0){
	    	for(Map<String,Object> prvddata : colsList){
	    		audtrmList.add(prvddata.get("audTrm").toString());
	    		num1.add(Long.valueOf(prvddata.get("yctfNum").toString()));
	    		num2.add(Long.valueOf(prvddata.get("hfzsNum").toString()));
	    		num3.add(Long.valueOf(prvddata.get("jfzyNum").toString()));
	    		num4.add(Long.valueOf(prvddata.get("jfzsNum").toString()));
	    	}
	    }
	    tmpObj.put("audtrmList", audtrmList);
	    tmpObj.put("num1", num1);
	    tmpObj.put("num2", num2);
	    tmpObj.put("num3", num3);
	    tmpObj.put("num4", num4);
	    return tmpObj;
	}
	 
}