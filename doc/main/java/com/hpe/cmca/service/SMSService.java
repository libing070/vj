/**
 * com.hp.cmcc.service.SMSService.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.common.BaseObject;

/**
 * <pre>
 * Desc： 
 * @author peter.fu
 * @refactor peter.fu
 * @date   Mar 9, 2015 6:01:00 PM
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Mar 9, 2015 	   peter.fu 	         1. Created this class.
 * </pre>
 */
@Service("smsService")
public class SMSService extends BaseObject {

    @Autowired
    private MybatisDao mybatisDao = null;

    public List<Map<String, Object>> selectSmsRequest() {
	Map<String, Object> params = new HashMap<String, Object>();
	List<Map<String, Object>> list = mybatisDao.getList("SmsJobMapper.selectSmsRequest", params);
	return list;
    }

    @Transactional
    public void updateSmsCountAndTime(int id, boolean result) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("id", id);
	params.put("sendFlag", result ? "Y" : "N");
	mybatisDao.update("SmsJobMapper.updateSmsCountAndTime", params);
    }

    //写个定时任务每天12：00和18：00会去扫描临时表如果有记录，则将它们的send_count字段置成1（这个字段在中间表没什么实际意义，脚本生成时默认插入的是0，所以用1表示这些是后台脚本新输出的记录）。
    //去扫描临时表看是否有记录
    @Transactional
    public List<Map<String, Object>> checkSmsReady() {
	Map<String, Object> params = new HashMap<String, Object>();
	List<Map<String, Object>> list = mybatisDao.getList("SmsJobMapper.checkSmsReady", params);
	return list;
    }
    
    //临时表如果有记录，则将它们的send_count字段置成1
    @Transactional
    public void updateSmsFlag() {
	Map<String, Object> params = new HashMap<String, Object>();
	mybatisDao.update("SmsJobMapper.updateSmsFlag", params);
    }
    
    @Transactional
    public void copeSmsToMid() {
	Map<String, Object> params = new HashMap<String, Object>();
	mybatisDao.add("SmsJobMapper.copeSmsToMid", params);
    }
    

    // 查询短信中间表里今天的短信,并分类拼接
    @Transactional
    public List<Map<String, Object>> selectSmsMidRequest() {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	Date nowDate = new Date();
	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH:mm:ss");
	String now = df.format(nowDate);
	String date = now.substring(0, 8);// 年月日
	paramMap.put("today", date);
	paramMap.put("today", "20170124");// 测试用
	List<Map<String, Object>> list = mybatisDao.getList("SmsJobMapper.selectSmsMidRequest", paramMap);
	return list;
    }
    // 将拼接好的记录插入短信表
    @Transactional
    public void insertSms4Send(String r0, String r1) {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("phone", r0);
	    paramMap.put("content", r1);
	    mybatisDao.add("SmsJobMapper.addSmsRecord", paramMap);
    }
    
    @Transactional
    public void insertSmsSendTmp(String phone, String content,String userId) {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("phone", phone);
	    paramMap.put("content", content);
	    paramMap.put("userId", userId);
	    paramMap.put("createTime", new Date());
	    paramMap.put("sendTime",  new Date());
	    mybatisDao.add("SmsJobMapper.addSmsRecordTmp", paramMap);
    }
	
    //将send_count字段是1的记录在临时表里删除
    @Transactional
    public void deleteSmsFromTemp() {
	Map<String, Object> params = new HashMap<String, Object>();
	mybatisDao.delete("SmsJobMapper.deleteSmsFromTemp", params);
    } 
    //将中间表中所有记录的send_count字段更新成2（表示这些记录都不用再拼接了)
    @Transactional
    public void updateSmsCompelteFlag() {
	Map<String, Object> params = new HashMap<String, Object>();
	mybatisDao.update("SmsJobMapper.updateSmsCompelteFlag", params);
    }
      
}
