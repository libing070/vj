package com.hpe.cmca.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hpe.cmca.dao.MybatisDao;

@Service
public class NotiFileGenService {
	@Autowired
	private MybatisDao	mybatisDao	= null;
	Logger	logger	= Logger.getLogger(NotiFileGenService.class);
	public  List<Map<String, Object>> getFileGenRequst(String subjectId,String focusNum,Boolean isAuto){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("subjectId", subjectId);
		params.put("focusNum", focusNum);
		params.put("isAuto", isAuto);
		List<Map<String, Object>> result = mybatisDao.getList("pmhz.getFileGenRequst", params);
		return result;
	}
	
	public  List<Map<String, Object>> getFileGenRequstNew(String subjectId,String focusNum,Boolean isAuto){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("subjectId", subjectId);
		params.put("focusNum", focusNum);
		params.put("isAuto", isAuto);
		List<Map<String, Object>> result = mybatisDao.getList("pmhz.getFileGenRequstNew", params);
		return result;
	}
	
	public  List<Map<String, Object>> getFileGenRequstAudTrm(String subjectId,String focusNum,Boolean isAuto,String audTrm){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("subjectId", subjectId);
		params.put("focusNum", focusNum);
		params.put("isAuto", isAuto);
		params.put("audTrm", audTrm);
		List<Map<String, Object>> result = mybatisDao.getList("pmhz.getFileGenRequst", params);
		return result;
	}
	
	  public List<Map<String,Object>> selUserByType(String audTrm) throws Exception{
			Map<String, String> params = new HashMap<String, String>();
			params.put("audTrm", audTrm);
			List<Map<String, Object>> result = mybatisDao.getList("pmhz.selKhqf4000UserByType", params);
			return result;
		    }
	
	public  List<Map<String, Object>> getFileGenRequstAudTrmNew(String subjectId,String focusNum,Boolean isAuto,String audTrm){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("subjectId", subjectId);
		params.put("focusNum", focusNum);
		params.put("isAuto", isAuto);
		params.put("audTrm", audTrm);
		List<Map<String, Object>> result = mybatisDao.getList("pmhz.getFileGenRequstNew", params);
		return result;
	}
	
	
	public void updatePmhzStatusByTrmSub(Integer pmhzStatus,String audTrm,String subjectId){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("pmhzStatus", pmhzStatus);
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		mybatisDao.update("pmhz.updatePmhzStatusByTrmSub", params);
	}
	
	public void updatePmhzStatusByTrmSubNew(Integer pmhzStatus,String audTrm,String subjectId){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("pmhzStatus", pmhzStatus);
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		mybatisDao.update("pmhz.updatePmhzStatusByTrmSubNew", params);
	}
	
	public void updatePmhzStatus(Integer pmhzStatus, Integer Id){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("pmhzStatus", pmhzStatus);
		params.put("Id", Id);
		mybatisDao.update("pmhz.updatePmhzStatus", params);
	}
	
	public void updatePmhzStatusNew(Integer pmhzStatus, Integer Id){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("pmhzStatus", pmhzStatus);
		params.put("Id", Id);
		mybatisDao.update("pmhz.updatePmhzStatusNew", params);
	}
	public void updateNotificationFile(String audTrm,String subjectId,String focusCd,Boolean isAuto,String buildDownloadUrl){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		params.put("focusCd", focusCd);
		
		List<Map<String, Object>> list = mybatisDao.getList("pmhz.getPmhzGenInfo", params);
		params.put("downloadUrl", buildDownloadUrl);
		if(isAuto)params.put("status", "2");
		else params.put("status", "3");
		if(!list.isEmpty()){		
			params.put("id", list.get(0).get("id"));
			mybatisDao.update("pmhz.updateNotificationFile", params);
		}else{
			mybatisDao.get("pmhz.addNotiFile", params);
		}
	}
	
	public void addHandAndDownloadInfo(String userName, String audTrm,String subjectId,String opType,String opPrvd){
		Map<String,Object> params=new HashMap<String,Object>();
		//#{userName}, #{audTrm},#{subjectId}, #{focusCd}, #{opType}
		params.put("userName", userName);
		params.put("audTrm", audTrm);
		params.put("subjectId", subjectId);
		params.put("focusCd", subjectId+"000");
		params.put("opType", opType);
		params.put("opPrvd", opPrvd);
		
		mybatisDao.update("pmhz.addHandAndDownloadInfo", params);
	}
	
	public Boolean checkReportFileCompleted(String focusCd, String month){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("focusCdStr" , focusCd);
		params.put("month" , month);
		Map<String, Integer> result = mybatisDao.get("pmhz.checkPrvdCount", params);
		return result.get("total_num") == 31;
	}
	
	public boolean checkNotiFileExist(String focusCd, String month){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("focusCdStr" , focusCd);
		params.put("month" , month);
		Map<String, Integer> result = mybatisDao.get("pmhz.getNotiFileCountByFocusCDAndMonth", params);
		return result.get("total_num") > 0;
	}
	
	public boolean checkReportFileExist(String focusCd, String month){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("focusCdStr" , focusCd);
		params.put("month" , month);
		Map<String, Integer> result = mybatisDao.get("pmhz.getReportFileCountByFocusCDAndMonth", params);
		return result.get("total_num") > 0;
	}
	
	public boolean addNotiFile(String focusCd,String auditSubject, String month){
		Map<String, String> params = new HashMap<String, String>();
		params.put("focusCdStr", focusCd);
		params.put("month", month);
		params.put("auditSubject", auditSubject);
		mybatisDao.add("pmhz.addNotiFile", params);
		return true;
	}
	
	public void updateNotiFileForCR(int id, String status, String downloadUrl) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		params.put("id", id);
		params.put("downloadUrl", downloadUrl);
		mybatisDao.update("pmhz.updateNotiFileForCR", params);
	}
	
	public void updateNotiFileStatus(int id, String status) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		params.put("id", id);
		mybatisDao.update("pmhz.updateNotiFileStatusById", params);
	}
	
	public void incNotiFileCRNum(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		mybatisDao.update("pmhz.incNotiFileCRNumById", params);
	}
	
	public void addNotiFileCROp(String userName, String focusCd, String month) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("focusCdStr", focusCd);
		params.put("auditSubject", extractAuditSubjectFromAuditConcern(focusCd));
		params.put("userName", userName);
		params.put("month", month);
		params.put("opType", "1");
		mybatisDao.add("pmhz.AddNotiFileOp", params);
	}

	public void lockNotiFile(String focusCd, String month){
		Map<String, String> params = new HashMap<String, String>();
		params.put("focusCdStr", focusCd);
		params.put("month", month);
		params.put("status", "1");
		mybatisDao.get("pmhz.lockNotiFile", params);
	}
	
	public List<Map<String, Object>> getNotiFileList(String month, String status){
		Map<String, String> params = new HashMap<String, String>();
		params.put("month", month);
		params.put("status", status);
		List<Map<String, Object>> result = mybatisDao.getList("pmhz.getNotiFileList", params);
		return result;
	}
	
	public List<Map<String, Object>> getNotiFileList(String focusCd,String month, String status){
		Map<String, String> params = new HashMap<String, String>();
		params.put("month", month);
		params.put("status", status);
		params.put("focusCdStr", focusCd);
		List<Map<String, Object>> result = mybatisDao.getList("pmhz.getNotiFileList", params);
		return result;
	}
	
	public List<Map<String, Object>> getNotiFile1000DataSumIllegal(String month, String focusCd){
		Map<String, String> params = new HashMap<String, String>();
		params.put("month", month);
		params.put("focusCdStr", focusCd);
		List<Map<String, Object>> result = mybatisDao.getList("pmhz.getNotiFileYjkSumIllegal", params);
		return result;
	}
	
	public List<Map<String, Object>> getNotiFile1000DataSumQty(String month){
		Map<String, String> params = new HashMap<String, String>();
		params.put("month", month);
		List<Map<String, Object>> result = mybatisDao.getList("pmhz.getNotiFileYjkSumQty", params);
		String lastMonth;
		List<Map<String, Object>> lResult = null;
		try {
			lastMonth = this.addMonth(month, -1);
			params.put("month", lastMonth);
			lResult = mybatisDao.getList("pmhz.getNotiFileYjkSumQty", params);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		if (null != result && null != lResult) {
			for (Map<String, Object> map : result) {
				if (null == map.get("per_cnt1")) continue;
				String prvdName = (String) map.get("CMCC_prov_prvd_nm");
				for (Map<String, Object> lMap : lResult) {
					if (prvdName.equals((String) lMap.get("CMCC_prov_prvd_nm"))) {
						if (null == lMap.get("per_cnt1")) break;
						Double perCnt = map.get("per_cnt1") instanceof BigDecimal ? ((BigDecimal) map.get("per_cnt1")).doubleValue():(Double) map.get("per_cnt1");
						Double lPerCnt = lMap.get("per_cnt1") instanceof BigDecimal ? ((BigDecimal) lMap.get("per_cnt1")).doubleValue():(Double) lMap.get("per_cnt1");
						map.put("id_pec", perCnt - lPerCnt);
						break;
					}
				}
			}
		}
		return result;
	}
	
	public List<Map<String, Object>> getNotiFile1000DataSumAmt(String month){
		Map<String, String> params = new HashMap<String, String>();
		params.put("month", month);
		List<Map<String, Object>> result = mybatisDao.getList("pmhz.getNotiFileYjkSumAmt", params);
		String lastMonth;
		List<Map<String, Object>> lResult = null;
		try {
			lastMonth = this.addMonth(month, -1);
			params.put("month", lastMonth);
			lResult = mybatisDao.getList("pmhz.getNotiFileYjkSumAmt", params);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		if (null != result && null != lResult) {
			for (Map<String, Object> map : result) {
				if (null == map.get("per_amt1")) continue;
				String prvdName = (String) map.get("CMCC_prov_prvd_nm");
				for (Map<String, Object> lMap : lResult) {
					if (prvdName.equals((String) lMap.get("CMCC_prov_prvd_nm"))) {
						if (null == lMap.get("per_amt1")) break;
						Double perCnt = map.get("per_amt1") instanceof BigDecimal ? ((BigDecimal) map.get("per_amt1")).doubleValue():(Double) map.get("per_amt1");
						Double lPerCnt = lMap.get("per_amt1") instanceof BigDecimal ? ((BigDecimal) lMap.get("per_amt1")).doubleValue():(Double) lMap.get("per_amt1");
						map.put("id_pec", perCnt - lPerCnt);
						break;
					}
				}
			}
		}
		return result;
	}
	
	public List<Map<String, Object>> getNotiFile1000DataRank(String month, String focusCd){
		Map<String, String> params = new HashMap<String, String>();
		params.put("month", month);
		params.put("focusCdStr", focusCd);
		List<Map<String, Object>> result = mybatisDao.getList("pmhz.getNotiFileYjkRank", params);
		return result;
	}
	
	public Map<String, Object> getNotiFile(int id){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Map<String, Object> result = mybatisDao.get("pmhz.getNotiFileById", params);
		return result;
	}
	
	public Map<String, Object> getNotiFile(String focusCd,String month, String status){
		Map<String, String> params = new HashMap<String, String>();
		params.put("month", month);
		params.put("status", status);
		params.put("focusCdStr", focusCd);
		Map<String, Object> result = mybatisDao.get("pmhz.getNotiFile", params);
		return result;
	}
	
	public List<Map<String, Object>> getNotiFile3000Data(String month, String focusCd){
		String selectedId = "3000".equals(focusCd) ? "pmhz.getNotiFileZdtlData3000" : "pmhz.getNotiFileZdtlData";
		Map<String, String> params = new HashMap<String, String>();
		params.put("month", month);
		params.put("focusCdStr", focusCd);
		List<Map<String, Object>> result = mybatisDao.getList(selectedId, params);
		
		if (result.size() < 31) {
			Map<String, String> data = getAllPrvdDataWithKeyName();
			for(Map<String, Object> map : result) {
				String prvd_name = (String) map.get("CMCC_prov_prvd_nm");
				if (data.containsKey(prvd_name)) {
					data.remove(prvd_name);
				}
			}
			int order = result.size() + 1;
			for(String prvd_name : data.keySet()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("aud_trm", month);
				map.put("focus_cd", focusCd);
				map.put("CMCC_prov_prvd_nm", prvd_name);
				map.put("tol_num", 0);
				map.put("totalChnlQty", 0);
				map.put("weigui_num", 0);
				map.put("weigui_chnlqty", 0);
				map.put("weigui_per_cnt", new Double(0));
				map.put("weigui_order", order);
				map.put("taoli_num", 0);
				map.put("infraction_sett_amt", new Double(0));
				map.put("taoli_chnlqty", 0);
				map.put("taoli_per_cnt", new Double(0));
				map.put("taoli_weigui_per_cnt", new Double(0));
				map.put("taoli_weigui_order", order);
				map.put("taoli_order", order);
				result.add(map);
				order++;
			}
		}
		return result;
	}
	
	public Map<String, String> getAllPrvdDataWithKeyName() {
		List<Map<String, Object>> result = mybatisDao.getList("pmhz.getAllPrvdData");
		Map<String, String> data = new HashMap<String, String>();
		for(Map<String, Object> map : result) {
			String prvd_name = (String) map.get("prvd_nm");
			String prvd_id = "";
			
			if (map.get("prvd_id") instanceof Integer) {
				prvd_id = ((Integer) map.get("prvd_id")).toString();
				
			} else if (map.get("prvd_id") instanceof Long) {
				prvd_id = ((Long) map.get("prvd_id")).toString();
			} else {
				prvd_id = (String) map.get("prvd_id");
			}
			
			data.put(prvd_name, prvd_id);
		}
		return data;
	}

	public List<Map<String, Object>> getNotiFile2000DataSum(String month, String chnlClass) throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		String monthT = month;
    	String monthF = addMonth(monthT,-12);

    	List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    	params.put("chnlClass", chnlClass);
    	params.put("monthF", monthF);
    	params.put("monthT", monthT);
    	List<Map<String, Object>> result = mybatisDao.getList("pmhz.getNotiFile2000DataSumCache", params);
    	
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar ct = Calendar.getInstance();
        ct.setTime(sdf.parse(monthT));
        Calendar cf = Calendar.getInstance();
        cf.setTime(sdf.parse(monthF));
    	
        Boolean gotCache = false;
        int c = 0;
        while(ct.after(cf) || ct.equals(cf)) {
        	Map<String, Object> item = null;
        	String curMonth = sdf.format(ct.getTime());
        	for( Map<String, Object> map : result) {
        		if (curMonth.equals(map.get("aud_trm"))) {
        			item = map;
        			gotCache = true;
        			break;
        		}
        	}
        	if (null == item) {
        		if (gotCache) break;
        		params.put("month", curMonth);
        		item = mybatisDao.get("pmhz.getNotiFile2000DataSum", params);
        		if (null != item) {
        			item.put("aud_trm", curMonth);
        			item.put("chnl_class", chnlClass);
	        		mybatisDao.add("pmhz.addNotiFile2000DataSumCache", item);
        		}
        	}
    		if (null != item) {
	    		data.add(item);
    		} else if (c == 0) {
    			//throw new Exception("record not found");
    			logger.error("record not found");
    		}
    		c++;
        	ct.add(Calendar.MONTH, -1);
        }
		return data;
	}
	
	public List<List<Map<String, Object>>> getNotiFile2000DataChnlPerc(String month, String chnlClass) throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		String monthT = month;
    	String monthF = addMonth(monthT,-12);
    	
    	List<List<Map<String, Object>>> data = new ArrayList<List<Map<String, Object>>>();
    	params.put("chnlClass", chnlClass);
    	params.put("monthF", monthF);
    	params.put("monthT", monthT);
    	Map<String, List<Map<String, Object>>> cachedData = new HashMap<String, List<Map<String, Object>>>();
    	List<Map<String, Object>> result = mybatisDao.getList("pmhz.getNotiFile2000ChnlPercCache", params);
    	
    	String curMonth = null;
    	if( null != result && result.size() > 0){
	    	for(Map<String, Object> map : result) {
	    		curMonth = (String) map.get("aud_trm");
	    		if (!cachedData.containsKey(curMonth)) {
	    			cachedData.put(curMonth, new ArrayList<Map<String, Object>>());
	    		} 
	    		cachedData.get(curMonth).add(map);
	    	}
    	}
    	
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar ct = Calendar.getInstance();
        ct.setTime(sdf.parse(monthT));
        Calendar cf = Calendar.getInstance();
        cf.setTime(sdf.parse(monthF));
    	
        List<Map<String, Object>> originData = null;
        Boolean gotCache = false;
        while(ct.after(cf) || ct.equals(cf)) {
        	List<Map<String, Object>> item = null;
        	curMonth = sdf.format(ct.getTime());
        	if (cachedData.containsKey(curMonth)) {
        		item = cachedData.get(curMonth);
        		gotCache = true;
        	}

        	if (null == item) {
        		if (gotCache) break;
        		params.put("month", curMonth);
        		item = mybatisDao.getList("pmhz.getNotiFile2000ChnlPerc", params);
        		if (null != item) {
        			for(Map<String, Object> map : item) {
        				map.put("chnl_class", chnlClass);
        				mybatisDao.add("pmhz.addNotiFile2000ChnlPercCache", map);
        			}
        		}
        	}
    		if (null != item) {
    			List<Map<String, Object>> reRankedResult = new ArrayList<Map<String, Object>>();
    			if (null == originData) {
    				originData = item;
    				reRankedResult = item;
    			} else {
    				for(Map<String, Object> row : originData) {
		    			String prvdId = row.get("prvd_id") instanceof Integer ? ((Integer) row.get("prvd_id")).toString(): (String) row.get("prvd_id");
		    			Boolean flag = false;
		    			for(Map<String, Object> map : item) {
		    				String newPrvdId = map.get("prvd_id") instanceof Integer ? ((Integer) map.get("prvd_id")).toString(): (String) map.get("prvd_id");
		    				if (prvdId.equals(newPrvdId)) {
		    					flag = true;
		    					reRankedResult.add(map);
		    					break;
		    				}
		    			}
		    			if (!flag) {
		    				reRankedResult.add(new HashMap<String, Object>());
		    			}
		    		}
    			}
	    		data.add(reRankedResult);
    		}
    		if (null == originData) {
    			//throw new Exception("record not found");
    			logger.error("record not found");
    		}
        	ct.add(Calendar.MONTH, -1);
        }
        for(int i=0; i < data.size(); i++) {
        	
        	if (i < data.size() - 1) {
        		List<Map<String, Object>> tmp1 = data.get(i);
        		List<Map<String, Object>> tmp2 = data.get(i+1);
        		for (int j=0; j < tmp1.size(); j++) {
        			Map<String, Object> map = tmp1.get(j);
        			if (map.containsKey("qdyk_chnl_perc_id") && null != map.get("qdyk_chnl_perc_id")) continue;
        			System.out.println(map);
        			if(map.containsKey("qdyk_chnl_perc_id") && null != map.get("qdyk_chnl_perc_id")&&tmp2.get(j).containsKey("qdyk_chnl_perc")&&null!=tmp2.get(j).get("qdyk_chnl_perc")){
        			    Double d1 = ((BigDecimal) map.get("qdyk_chnl_perc")).doubleValue();
        			    Double d2 = ((BigDecimal) tmp2.get(j).get("qdyk_chnl_perc")).doubleValue();
        			    map.put("qdyk_chnl_perc_id", d1-d2);
        			    mybatisDao.update("pmhz.updateNotiFile2000ChnlPercCache", map);
        			}

        		}
        	}
        }
		return data;
	}
	
	public List<List<Map<String, Object>>> getNotiFile2000DataQtyPerc(String month, String chnlClass) throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		String monthT = month;
		String monthF = addMonth(monthT,-12);
    	List<List<Map<String, Object>>> data = new ArrayList<List<Map<String, Object>>>();
    	params.put("chnlClass", chnlClass);
    	params.put("monthF", monthF);
    	params.put("monthT", monthT);
    	Map<String, List<Map<String, Object>>> cachedData = new HashMap<String, List<Map<String, Object>>>();
    	List<Map<String, Object>> result = mybatisDao.getList("pmhz.getNotiFile2000QtyPercCache", params);
    	
    	String curMonth = null;
    	if( null != result && result.size() > 0){
	    	for(Map<String, Object> map : result) {
	    		curMonth = (String) map.get("aud_trm");
	    		if (!cachedData.containsKey(curMonth)) {
	    			cachedData.put(curMonth, new ArrayList<Map<String, Object>>());
	    		} 
	    		cachedData.get(curMonth).add(map);
	    	}
    	}
    	
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar ct = Calendar.getInstance();
        ct.setTime(sdf.parse(monthT));
        Calendar cf = Calendar.getInstance();
        cf.setTime(sdf.parse(monthF));
    	Boolean gotCache = false;
        List<Map<String, Object>> originData = null;
        //System.out.println("cachedData");
        //System.out.println(cachedData);
        while(ct.after(cf) || ct.equals(cf)) {
        	List<Map<String, Object>> item = null;
        	curMonth = sdf.format(ct.getTime());
        	//System.out.println( curMonth);
        	if (cachedData.containsKey(curMonth)) {
        		gotCache = true;
        		item = cachedData.get(curMonth);
        		//System.out.println("got it :"+ curMonth);
        	} else {
        		//System.out.println("can not got it");
        	}

        	if (null == item) {
        		if (gotCache) break;
        		params.put("month", curMonth);
        		item = mybatisDao.getList("pmhz.getNotiFile2000QtyPerc", params);
        		if (null != item) {
        			for(Map<String, Object> map : item) {
        				map.put("chnl_class", chnlClass);
        				mybatisDao.add("pmhz.addNotiFile2000QtyPercCache", map);
        			}
        		}
        	}
    		if (null != item) {
    			List<Map<String, Object>> reRankedResult = new ArrayList<Map<String, Object>>();
    			if (null == originData) {
    				originData = item;
    				reRankedResult = item;
    			} else {
    				//System.out.println("looking ");
    				//System.out.println(originData);
    				//System.out.println(item);
    				for(Map<String, Object> row : originData) {
		    			String prvdId = row.get("prvd_id") instanceof Integer ? ((Integer) row.get("prvd_id")).toString(): (String) row.get("prvd_id");
		    			Boolean flag = false;
		    			for(Map<String, Object> map : item) {
		    				String newPrvdId = map.get("prvd_id") instanceof Integer ? ((Integer) map.get("prvd_id")).toString(): (String) map.get("prvd_id");
		    				//System.out.println(prvdId + ": " + newPrvdId);
		    				if (prvdId.equals(newPrvdId)) {
		    					flag = true;
		    					reRankedResult.add(map);
		    					break;
		    				}
		    			}
		    			if (!flag) {
		    				reRankedResult.add(new HashMap<String, Object>());
		    			}
		    		}
    			}
	    		data.add(reRankedResult);
    		}
    		if (null == originData) {
    			//throw new Exception("record not found");
    			logger.error("record not found");
    		}
        	ct.add(Calendar.MONTH, -1);
        }
        for(int i=0; i < data.size(); i++) {
        	
        	if (i < data.size() - 1) {
        		List<Map<String, Object>> tmp1 = data.get(i);
        		List<Map<String, Object>> tmp2 = data.get(i+1);
        		for (int j=0; j < tmp1.size(); j++) {
        			Map<String, Object> map = tmp1.get(j);
        			Map<String, Object> map2 = tmp2.get(j);
        			if (map.containsKey("qdyk_num_perc_id") && null != map.get("qdyk_num_perc_id")) continue;
        			if(map.containsKey("qdyk_num_perc_id") && null != map.get("qdyk_num_perc_id")&&map2.containsKey("qdyk_num_perc")&&null != map2.get("qdyk_num_perc")){
            			    Double d1 = ((BigDecimal) map.get("qdyk_num_perc")).doubleValue();
        			    Double d2 = ((BigDecimal) tmp2.get(j).get("qdyk_num_perc")).doubleValue(); 
        			    map.put("qdyk_num_perc_id", d1-d2);
        			    mybatisDao.update("pmhz.updateNotiFile2000QtyPercCache", map);
        			}
        		}
        	}
        }
		return data;
	}
	
	public List<Map<String, Object>> getNotiFile2000Tol(String month, String order){
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, String> params = new HashMap<String, String>();
		if("NumTop3Cty".equals(order)){
			params.put("audTrm", month);
			result = mybatisDao.getList("pmhz.getNotiFile2000NumTop3Cty", params);
		}
		if("PerTop3Cty".equals(order)){
			params.put("audTrm", month);
			result = mybatisDao.getList("pmhz.getNotiFile2000PerTop3Cty", params);
		}
		if("NumTop10Chnl".equals(order)){
			params.put("audTrm", month);
			result = mybatisDao.getList("pmhz.getNotiFile2000NumTop10Chnl", params);
		}
		if("PerTop10Chnl".equals(order)){
			params.put("audTrm", month);
			result = mybatisDao.getList("pmhz.getNotiFile2000PerTop10Chnl", params);
		}
		return result;
	}
	
	public List<Map<String, Object>> getAllPrvd(){
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		result = mybatisDao.getList("pmhz.getAllPrvd");
		return result;
	}
		

	public int getMonthSpace(String date1, String date2) throws ParseException {
        int result = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(sdf.parse(date1));
        c2.setTime(sdf.parse(date2));
        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH) + (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12;
        return result == 0 ? 1 : Math.abs(result) + 1;
    }
    
    public String addMonth(String date, int month) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(date));
        c.add(Calendar.MONTH, month);
        return sdf.format(c.getTime());
    }
    
	public String extractAuditSubjectFromAuditConcern(String auditConcern) {
		if (auditConcern.isEmpty()) return null;
		String auditSubject = auditConcern.substring(0, 1);
		return StringUtils.isNumber(auditSubject) ? auditSubject : null;
	}

    public List<Map<String, Object>> getNotiFile2000pmhzNum(List<String> audList) {
	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	Map<String, Object> params = new HashMap<String, Object>();
	String[] array = new String[audList.size()];
	String[] audTrms = audList.toArray(array);
	params.put("audTrmStr", audList.toString());
	params.put("audTrms", audTrms);
	params.put("audTrm", audList.get(0));
	result = mybatisDao.getList("pmhz.getPmhzYKNum", params);
	return result;
    }
    public List<Map<String, Object>> getNotiFile2000pmhzChnl(List<String> audList) {
 	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
 	Map<String, Object> params = new HashMap<String, Object>();
 	String[] array = new String[audList.size()];
 	String[] audTrms = audList.toArray(array);
 	params.put("audTrmStr", audList.toString());
 	params.put("audTrms", audTrms);
 	params.put("audTrm", audList.get(0));
 	result = mybatisDao.getList("pmhz.getPmhzYKChnl", params);
 	return result;
     }

    public List<Map<String, Object>> getNotiFile2000pmhzSum(List<String> audList) {
	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	Map<String, Object> params = new HashMap<String, Object>();
	String[] array = new String[audList.size()];
	String[] audTrms = audList.toArray(array);
	params.put("audTrmStr", audList.toString());//
	params.put("audTrms", audTrms);
	result = mybatisDao.getList("pmhz.getPmhzYKSum", params);
	return result;
    }
	public void update1000Num(String audTrm){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("audTrm", audTrm);
		mybatisDao.update("pmhz.update1000Num", params);
	}
	public void update1000Amt(String audTrm){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("audTrm", audTrm);
		mybatisDao.update("pmhz.update1000Amt", params);
	}
	
	public List<Map<String, Object>> getNotiFile1000pmhz(List<String> audList) {
	    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	    List<Map<String, Object>> result1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> params = new HashMap<String, Object>();
		update1000Num(audList.get(0));
		update1000Amt(audList.get(0));
		String[] array = new String[audList.size()];
		String[] audTrms = audList.toArray(array);
		params.put("audTrmStr", audList.toString());
		params.put("audTrms", audTrms);
    		result1 = mybatisDao.getList("pmhz.getPmhzYJKNum", params);
		result.addAll(result1);
		result1 = mybatisDao.getList("pmhz.getPmhzYJKAmt", params);
		result.addAll(result1);
	    return result;
	}
	public List<Map<String, Object>> getNotiFile1000pmhzSum(List<String> audList) {
	    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> params = new HashMap<String, Object>();
		String[] array = new String[audList.size()];
		String[] audTrms = audList.toArray(array);
		params.put("audTrmStr", audList.toString());
		params.put("audTrms", audTrms);
		result = mybatisDao.getList("pmhz.getPmhzYJKSum", params);
	    return result;
	}
    public List<Map<String, Object>> selHisGRTop100(String audTrm) throws Exception {
	Map<String, String> params = new HashMap<String, String>();
	params.put("audTrm", audTrm);
	List<Map<String, Object>> result = mybatisDao.getList("pmhz.selHisGRTop1000", params);
	return result;
    }

    public List<Map<String, Object>> selCurGRTop100(String audTrm) throws Exception {
	Map<String, String> params = new HashMap<String, String>();
	params.put("audTrm", audTrm);
	List<Map<String, Object>> result = mybatisDao.getList("pmhz.selCurGRTop100", params);
	return result;
    }

    public List<Map<String, Object>> selHisJTTop100(String audTrm) throws Exception {
	Map<String, String> params = new HashMap<String, String>();
	params.put("audTrm", audTrm);
	List<Map<String, Object>> result = mybatisDao.getList("pmhz.selHisJTTop100", params);
	return result;
    }

    public List<Map<String, Object>> selCurJTTop100(String audTrm) throws Exception {
	Map<String, String> params = new HashMap<String, String>();
	params.put("audTrm", audTrm);
	List<Map<String, Object>> result = mybatisDao.getList("pmhz.selCurJTTop100", params);
	return result;
    }

    public List<Map<String, Object>> selGRorJTTop100() throws Exception {
	Map<String, String> params = new HashMap<String, String>();
	// params.put("audTrm", audTrm);
	List<Map<String, Object>> result = mybatisDao.getList("pmhz.selGRorJTTop100", params);
	return result;
    }

    public List<Map<String, Object>> selGRAge(String audTrm) throws Exception {
	Map<String, String> params = new HashMap<String, String>();
	params.put("audTrm", audTrm);
	List<Map<String, Object>> result = mybatisDao.getList("pmhz.selGRAge", params);
	return result;
    }

    public List<Map<String, Object>> selGRAmt(String audTrm) throws Exception {
	Map<String, String> params = new HashMap<String, String>();
	params.put("audTrm", audTrm);
	List<Map<String, Object>> result = mybatisDao.getList("pmhz.selGRAmt", params);
	return result;
    }

    public List<Map<String, Object>> selJTAge(String audTrm) throws Exception {
	Map<String, String> params = new HashMap<String, String>();
	params.put("audTrm", audTrm);
	List<Map<String, Object>> result = mybatisDao.getList("pmhz.selJTAge", params);
	return result;
    }

    public List<Map<String, Object>> selJTAmt(String audTrm) throws Exception {
	Map<String, String> params = new HashMap<String, String>();
	params.put("audTrm", audTrm);
	List<Map<String, Object>> result = mybatisDao.getList("pmhz.selJTAmt", params);
	return result;
    }

    public List<Map<String, Object>> selGRPmhz(String audTrm) throws Exception {
	Map<String, String> params = new HashMap<String, String>();
	params.put("audTrm", audTrm);
	List<Map<String, Object>> result = mybatisDao.getList("pmhz.selGRPmhz", params);
	return result;
    }

    public List<Map<String, Object>> selJTPmhz(String audTrm) throws Exception {
	Map<String, String> params = new HashMap<String, String>();
	params.put("audTrm", audTrm);
	List<Map<String, Object>> result = mybatisDao.getList("pmhz.selJTPmhz", params);
	return result;
    }
    
    public List<Map<String, Object>> getNotiFileZdtl3000Pmhz(String audTrm,String order) throws Exception {
	Map<String, String> params = new HashMap<String, String>();
	params.put("audTrm", audTrm);
	List<Map<String, Object>> result =null;
	if("updateReportSort".equals(order)){
	    mybatisDao.getList("pmhz.setZdtl3000allReportSort", params);
	    result = mybatisDao.getList("pmhz.updateZdtl3000allReportSort", params);
	}else{
	    if("allReport".equals(order)){
		params.put("prvd_id", "10000");
	    }
	    result = mybatisDao.getList("pmhz.getZdtl3000allReport", params);
	}
	return result;
    }
    

    public List<Map<String, Object>> getNotiFileKhqf4000Pmhz(String audTrm,String order) throws Exception {
	Map<String, String> params = new HashMap<String, String>();
	params.put("audTrm", audTrm);
	List<Map<String, Object>> result =null;
	if("updateReportSort".equals(order)){
	    mybatisDao.getList("pmhz.setKhqf4000prvdReportSort", params);
	    params.put("flag", "4001");
	    result = mybatisDao.getList("pmhz.updateKhqf4000prvdReportSort", params);
	    params.put("flag", "4003");
	    result = mybatisDao.getList("pmhz.updateKhqf4000prvdReportSort", params);
	}else{
	    if("allReport".equals(order)){
		params.put("flag", "4001");
		result = mybatisDao.getList("pmhz.getKhqf4000allReport", params);
	    }
	    if("allPersonReport".equals(order)){
		params.put("flag", "4003");
		result = mybatisDao.getList("pmhz.getKhqf4000allReport", params);
	    }
	    if("prvdReport".equals(order)){
		params.put("flag", "4001");
		result = mybatisDao.getList("pmhz.getKhqf4000prvdReport", params);
	    }
	    if("prvdPersonReport".equals(order)){
		params.put("flag", "4003");
		result = mybatisDao.getList("pmhz.getKhqf4000prvdReport", params);
	    }
	}
	return result;
    }
    
    //start -员工异常操作-排名汇总-老系统迁移-by hufei
    // 员工异常操作-积分异常赠送
    public List<Map<String, Object>> getNotiFile5000YCJFZS(String month, String prvdId, String order) {

	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	Map<String, String> params = new HashMap<String, String>();
	if ("Top50".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCJFZSTop50", params);
	}
	if ("COMTop50".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCJFZSCOMTop50", params);
	}
	if ("Top50ReleseStaff".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCJFZSTop50ReleseStaff", params);
	}
	if ("StaffTop50".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCJFZSStaffTop50", params);
	}
	return result;
    }

    // 员工异常操作-积分异常转移
    public List<Map<String, Object>> getNotiFile5000YCJFZY(String month, String prvdId, String order) {

	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	Map<String, String> params = new HashMap<String, String>();
	if ("Top50".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCJFZYTop50", params);
	}
	if ("COMTop50".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCJFZYCOMTop50", params);
	}
	if ("Top50ReleseStaff".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCJFZYTop50ReleseStaff", params);
	}
	if ("StaffTop50".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCJFZYStaffTop50", params);
	}
	return result;
    }

    // 员工异常操作-异常话费赠送
    public List<Map<String, Object>> getNotiFile5000YCHFZS(String month, String prvdId, String order) {

	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	Map<String, String> params = new HashMap<String, String>();
	if ("Top50".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCHFZSTop50", params);
	}
	if ("COMTop50".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCHFZSCOMTop50", params);
	}
	if ("Top50ReleseStaff".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCHFZSTop50ReleseStaff", params);
	}
	if ("StaffTop50".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCHFZSStaffTop50", params);
	}
	return result;
    }

    // 员工异常操作-异常退费
    public List<Map<String, Object>> getNotiFile5000YCTF(String month, String prvdId, String order) {

	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	Map<String, String> params = new HashMap<String, String>();
	if ("Top50".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCTFTop50", params);
	}
	if ("COMTop50".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCTFCOMTop50", params);
	}
	if ("Top50ReleseStaff".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCTFTop50ReleseStaff", params);
	}
	if ("StaffTop50".equals(order)) {
	    params.put("audTrm", month);
	    params.put("prvdId", prvdId);
	    result = mybatisDao.getList("pmhz.getYCTFStaffTop50", params);
	}
	return result;
    }

    public List<Map<String, Object>> getNotiFile5000SFXZ(String month, String order) {

	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	Map<String, String> params = new HashMap<String, String>();
	if ("SFXZ".equals(order)) {
	    params.put("audTrm", month);
	    result = mybatisDao.getList("pmhz.getSFXZ", params);
	}
	if ("QKTB".equals(order)) {
	    params.put("audTrm", month);
	    result = mybatisDao.getList("pmhz.getQKTB", params);
	}
	return result;
    }
    
    public List<Map<String, Object>> getPrvd() {
	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	Map<String, String> params = new HashMap<String, String>();
	result = mybatisDao.getList("pmhz.getPrvd", params);
	return result;
    }
    
    public List<Map<String, Object>> getPrvdAndCode() {
    	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
    	Map<String, String> params = new HashMap<String, String>();
    	result = mybatisDao.getList("pmhz.getPrvdAndCode", params);
    	return result;
        }
    
    public List<Map<String, Object>> getPrvdName(String prvd) {
	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	Map<String, String> params = new HashMap<String, String>();
	params.put("CMCC_prov_prvd_cd", prvd);
	result = mybatisDao.getList("pmhz.getPrvdName", params);
	return result;
    }
    //end -员工异常操作-排名汇总-老系统迁移-by hufei
    
    public List<Map<String, Object>> getNotiFileLlzy7000Pmhz(String audTrm,String flag) throws Exception {
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("audTrm", audTrm);
    	List<Map<String, Object>> result =null;
    	if("updateReportSort".equals(flag)){
    	    params.put("flag", "7001");
    	    result = mybatisDao.getList("pmhz.updateLlzy7000prvdReportSort", params);
//    	    params.put("flag", "7002");
//    	    result = mybatisDao.getList("busSpMgr.updateLlzy7000prvdReportSort", params);
    	}else{
    	    if("allorgReport".equals(flag)){
    			params.put("flag", "7001");
    			params.put("prvd_id", "10000");
    			result = mybatisDao.getList("pmhz.getLlzy7000prvdReport", params);
    		 }
    	    if("allcustReport".equals(flag)){
    			params.put("flag", "7002");
    			params.put("prvd_id", "10000");
    			result = mybatisDao.getList("pmhz.getLlzy7000prvdReport", params);
    		 }
    	    if("orgReport".equals(flag)){
	    		params.put("flag", "7001");
	    		result = mybatisDao.getList("pmhz.getLlzy7000prvdReport", params);
    	    }
    	    if("custReport".equals(flag)){
	    		params.put("flag", "7002");
	    		result = mybatisDao.getList("pmhz.getLlzy7000prvdReport", params);
    	    }
    	    if("orgOrder".equals(flag)){
    	    	params.put("flag", "7001");
	    		result = mybatisDao.getList("pmhz.getLlzy7000order", params);
    	    }
    	}
    	return result;
        }
    
 // start -流量赠送
    public List<Map<String, Object>> getNotiFileLlzs8000Pmhz(String audTrm,String prvdId,String order) throws Exception {
	Map<String, String> params = new HashMap<String, String>();
	params.put("audTrm", audTrm);
	params.put("prvdId", prvdId);
	List<Map<String, Object>> result = null;
	if ("updateReportSort".equals(order)) {
	    mybatisDao.getList("pmhz.setLlzs8000prvdReportSort", params);
	    result = mybatisDao.getList("pmhz.updateLlzs8000prvdReportSort", params);
	}
	if ("allReport".equals(order)) {
	    result = mybatisDao.getList("pmhz.getLlzs8000allReport", params);
	}
	if ("prvdReport".equals(order)) {
	    result = mybatisDao.getList("pmhz.getLlzs8000prvdReport", params);
	}
	if("cityTop50".equals(order)){
	    result = mybatisDao.getList("pmhz.getLlzs8000cityTop50", params);
	}
	if("offerTop50".equals(order)){
	    result = mybatisDao.getList("pmhz.getLlzs8000offerTop50", params);
	}
	if("chnlTop50".equals(order)){
	    result = mybatisDao.getList("pmhz.getLlzs8000chnlTop50", params);
	}
	if ("prvdInfo".equals(order)) {
	    result = mybatisDao.getList("pmhz.getLlzs8000PrvdInfo", params);
	}
	if("city".equals(order)){
	    result = mybatisDao.getList("pmhz.getLlzs8000city", params);
	}
	if("offer".equals(order)){
	    result = mybatisDao.getList("pmhz.getLlzs8000offer", params);
	}
	if("chnl".equals(order)){
	    result = mybatisDao.getList("pmhz.getLlzs8000chnl", params);
	}
	return result;
    }
    
    public List<Map> selNotiFileDLNum(String auditSubject,String focusCd,String audTrm) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("auditSubject", auditSubject);
		params.put("focusCd", focusCd);
		params.put("audTrm", audTrm);
		return mybatisDao.getList("pmhz.selNotiFileDLNum", params);
	}
	public void updateNotiFileDLNum(String auditSubject,String focusCd,String downUrl,String audTrm) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("auditSubject", auditSubject);
		params.put("focusCd", focusCd);
		params.put("downloadUrl", downUrl);
		params.put("audTrm", audTrm);
		mybatisDao.update("pmhz.updateNotiFileDLNum", params);
	}
	public Map<String, Object> addNotiFileTmp(String focusCd, String month, String auditSubject,String downUrl){
		Map<String, String> params = new HashMap<String, String>();
		params.put("focusCdStr", focusCd);
		params.put("month", month);
		params.put("downUrl", downUrl);
		params.put("auditSubject", auditSubject);
		Map<String, Object> result = mybatisDao.get("pmhz.addNotiFileTmp", params);
		return result;
	}
	
    
}
