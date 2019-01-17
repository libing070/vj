/**
 * com.hpe.cmca.job.SmsIntegrationJob.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.service.SMSService;

/**
 * <pre>
 * Desc： 
 * @author   xuwenhu
 * @refactor xuwenhu
 * @date     2017-3-1 下午4:14:02
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-3-1 	   xuwenhu 	         1. Created this class.
 * </pre>
 */
@Service("SmsIntegrationJob")
public class SmsIntegrationJob extends BaseObject {

    @Autowired
    private SMSService smsService = null;

    public void work() {

	// String isSendSms = propertyUtil.getPropValue("isSendSms");
	if (Integer.valueOf(smsService.checkSmsReady().get(0).get("quantity").toString()) <= Integer.valueOf(0)) {
	    this.logger.info("临时表里没有记录，暂不需拼接短信.");
	    return;
	}
	// 临时表如果有记录，则将它们的send_count字段置成1
	smsService.updateSmsFlag();

	// 为了避免这时有send_count字段是0的新记录输出到临时表，所以保险起见将send_count字段是1的记录拷到中间表。
	smsService.copeSmsToMid();

	// 从中间表中找到send_count字段是1并且create_time字段是今天的记录（因为有时候脚本会把前几天的记录补上的情况，这时前几天的记录不用做拼接），用java将它们拼接好放到短信表
	List<Map<String, Object>> list = smsService.selectSmsMidRequest();
	// 分类拼接
	integrationSms(list);
	// 将send_count字段是1的记录在临时表里删除
	smsService.deleteSmsFromTemp();
	// 将中间表中所有记录的send_count字段更新成2（表示这些记录都不用再拼接了)
	smsService.updateSmsCompelteFlag();

	this.logger.info("短信信息拼接完毕");
    }

    private void integrationSms(List<Map<String, Object>> list) {
	Map<String, Object> content111 = new HashMap<String, Object>();
	Map<String, Object> content112 = new HashMap<String, Object>();
	Map<String, Object> content121 = new HashMap<String, Object>();
	Map<String, Object> content141 = new HashMap<String, Object>();
	List<String> content131 = new ArrayList<String>();
	Map<String, Object> content113 = new HashMap<String, Object>();
	Map<String, Object> content114 = new HashMap<String, Object>();
	Map<String, Object> content115 = new HashMap<String, Object>();
	List<String> content142 = new ArrayList<String>();
	for (Map<String, Object> m : list) {
	    // 接口数据入库 111 （ 同一个接口发送给同一个手机号）
	    if ("111".equals(m.get("warn_id").toString())) {
		String phone111 = m.get("phone").toString();
		String prv = m.get("prvdId").toString();
		String prvName = getCompanyNameOfProvince(prv).substring(0, getCompanyNameOfProvince(prv).length() - 2);
		String s = m.get("int_xinxi").toString();
		if (content111.containsKey(s)) {
		    String str = m.get("txdate").toString() + "" + m.get("jobstatus").toString();
		    String con = content111.get(s).toString();
		    if (con.contains(str)) {
			int i = con.indexOf(str) - 4;
			String newCon = con.substring(0, i) + "、" + prvName + con.substring(i, con.length());
			content111.put(s, newCon);
		    } else {
			String content1 = prvName + "接口文件" + m.get("txdate").toString() + "" + m.get("jobstatus").toString() + ";";
			content111.put(s, con + content1);
		    }
		} else {
		    String content1 = "[高][HTL][" + m.get("int_xinxi").toString() + "日接口]超过24小时未入库:" + prvName + "接口文件" + m.get("txdate").toString() + "" + m.get("jobstatus").toString() + ";";
		    content111.put(s, phone111 + "_" + content1);
		}
		return;
	    }
	    // 接口数据重传 112 （ 同一个接口发送给同一个手机号）
	    if ("112".equals(m.get("warn_id").toString())) {
		String phone112 = m.get("phone").toString();
		String prv = m.get("prvdId").toString();
		String prvName = getCompanyNameOfProvince(prv).substring(0, getCompanyNameOfProvince(prv).length() - 2);
		String t = m.get("txdate").toString() + " " + m.get("int_xinxi").toString() + "接口重传" + m.get("content").toString().substring(0, m.get("content").toString().indexOf("次"));
		String s = m.get("int_xinxi").toString();
		if (content112.containsKey(s)) {
		    String con = content112.get(s).toString();
		    if (con.contains(t)) {
			int i = con.indexOf(t);
			String newCon = con.substring(0, i) + "、" + prvName + con.substring(i, con.length());
			content112.put(s, newCon);
		    } else {
			content112.put(s, con + prvName + t + "次；");
		    }
		} else {
		    content112.put(s, phone112 + "_" + m.get("content").toString() + prvName + t + "次；");
		}
		// [中]同一省份同一接口入库多次: 宁夏、云南 2016-11-09 05004 接口重传二次 ；福建、山西 2016-11-08 22064 接口重传三次
		return;
	    }
	    // 模型执行121 （ 同一个专题发送给同一个手机号）
	    if ("121".equals(m.get("warn_id").toString())) {
		String phone121 = m.get("phone").toString();
		String prv = m.get("prvdId").toString();
		String prvName = getCompanyNameOfProvince(prv).substring(0, getCompanyNameOfProvince(prv).length() - 2);
		String s = m.get("subject_name").toString();
		String f = m.get("focusName").toString();
		if (content121.containsKey(s)) {
		    String con = content121.get(s).toString();
		    if (con.contains(f)) {
			int i = con.indexOf(f);
			String newCon = con.substring(0, i + f.length()) + prvName + "执行状态" + m.get("jobstatus").toString() + " " + m.get("txdate").toString() + "、"
				+ con.substring(i + f.length(), con.length());
			content121.put(s, newCon);
		    } else {
			String newCon = con + f + " " + prvName + "执行状态" + m.get("jobstatus").toString() + " " + m.get("txdate").toString() + ";";
			content121.put(s, newCon);
		    }
		} else {
		    String content = "[高]模型执行未完成的省份：" + s + " " + f + " " + prvName + "执行状态" + m.get("jobstatus").toString() + " " + m.get("txdate").toString() + ";";
		    content121.put(s, phone121 + "_" + content);
		}
		// 需求三：[高]模型执行未完成的省份：X专题 X关注点 X省 执行状态pending 20161215； y关注点 X省、y省 执行状态pending 执行数据文件日期
		return;
	    }
	    // 审计报告141（ 同一个专题发送给同一个手机号）
	    if ("141".equals(m.get("warn_id").toString())) {
		String phone141 = m.get("phone").toString();
		String prv = m.get("prvdId").toString();
		String prvName = getCompanyNameOfProvince(prv).substring(0, getCompanyNameOfProvince(prv).length() - 2);
		// m.get("focusName").toString();
		String s = m.get("subject_name").toString();
		if (content141.containsKey(s)) {
		    String con = content141.get(s).toString();
		    content141.put(s, con + prvName + m.get("txdate").toString() + ";");
		} else {
		    String content = "[高]审计报告未生成的省份：" + s + " " + prvName + m.get("txdate").toString() + ";";
		    content141.put(s, phone141 + "_" + content);
		}
		// 需求四：[高]审计报告未生成的省份：X专题 X省 审计报告时间
		return;
	    }
	    // 数据库空间 131 （不需要拼）
	    if ("131".equals(m.get("warn_id").toString())) {
		String phone131 = m.get("phone").toString();
		String c = m.get("content").toString();
		content131.add(phone131 + "_" + c);
		// 需求五：[高]某某数据库空间不足
		return;
	    }
	    // 接口数据差异量比较 113 （ 同一个接口发送给同一个手机号）
	    if ("113".equals(m.get("warn_id").toString())) {
		String phone113 = m.get("phone").toString();
		String prv = m.get("prvdId").toString();
		String prvName = getCompanyNameOfProvince(prv).substring(0, getCompanyNameOfProvince(prv).length() - 2);
		String s = m.get("int_xinxi").toString();
		if (content113.containsKey(s)) {
		    String con = content113.get(s).toString();
		    content113.put(s, con + "、" + prvName);
		} else {
		    content113.put(s, phone113 + "_" + m.get("content").toString() + prvName);
		}
		// 需求六：[中][05004]TB_IMEI_RWD_INFO接口9月环比>50%的省份:云南、河南等等
		return;
	    }
	    // 接口数据31省完整性 114 （ 同一个接口发送给同一个手机号）
	    if ("114".equals(m.get("warn_id").toString())) {
		String phone114 = m.get("phone").toString();
		String prv = m.get("prvdId").toString();
		String prvName = getCompanyNameOfProvince(prv).substring(0, getCompanyNameOfProvince(prv).length() - 2);
		String s = m.get("int_xinxi").toString();
		if (content114.containsKey(s)) {
		    String con = content114.get(s).toString();
		    content114.put(s, con + "、" + prvName);
		} else {
		    content114.put(s, phone114 + "_" + m.get("content").toString() + prvName);
		}
		// 需求七：[中][05004]终端酬金信息月表TB_IMEI_RWD_INFO数据未入库的省份:云南、河南20161214
		// [中][22097]销售记录日表20160629数据入库量为0的省份:
		return;
	    }
	    // 接口数据到达时间 115
	    if ("115".equals(m.get("warn_id").toString())) {
		String phone115 = m.get("phone").toString();
		String prv = m.get("prvdId").toString();
		String prvName = getCompanyNameOfProvince(prv).substring(0, getCompanyNameOfProvince(prv).length() - 2);
		String s = m.get("int_xinxi").toString();
		if (content115.containsKey(s)) {
		    String con = content115.get(s).toString();
		    content115.put(s, con + "、" + prvName);
		} else {
		    content115.put(s, phone115 + "_" + m.get("content").toString() + prvName);
		}
		// 需求八：[中]数据文件到达接口机与入库时间差异3天以上的省份:A省、B省、C省
		return;
	    }
	    // 审计报告监控 142 （不需要拼）
	    if ("142".equals(m.get("warn_id").toString())) {
		String phone142 = m.get("phone").toString();
		String c = m.get("content").toString();
		content142.add(phone142 + "_" + c);
		// 需求九：2016年11月-有价卡-有价卡违规销售审计报告已生成
		return;
	    }
	}
	// 循环读取已经拼好的短信内容
	for (Map.Entry<String, Object> entry : content111.entrySet()) {
	    System.out.println(entry.getKey() + "--->" + entry.getValue());
	    String str = entry.getValue().toString().substring(0, entry.getValue().toString().length() - 1);// 去掉最后一个分号
	    String[] records = str.split("_");
	    // 将拼接好的记录插入短信表
	    smsService.insertSms4Send(records[0], records[1]);
	}
	for (Map.Entry<String, Object> entry : content112.entrySet()) {
	    System.out.println(entry.getKey() + "--->" + entry.getValue());
	    String str = entry.getValue().toString().substring(0, entry.getValue().toString().length() - 1);// 去掉最后一个分号
	    String[] records = str.split("_");
	    // 将拼接好的记录插入短信表
	    smsService.insertSms4Send(records[0], records[1]);
	}
	for (Map.Entry<String, Object> entry : content121.entrySet()) {
	    System.out.println(entry.getKey() + "--->" + entry.getValue());
	    String str = entry.getValue().toString().substring(0, entry.getValue().toString().length());// 去掉最后一个分号
	    String[] records = str.split("_");
	    // 将拼接好的记录插入短信表
	    smsService.insertSms4Send(records[0], records[1]);
	}
	for (Map.Entry<String, Object> entry : content141.entrySet()) {
	    System.out.println(entry.getKey() + "--->" + entry.getValue());
	    String str = entry.getValue().toString().substring(0, entry.getValue().toString().length());// 去掉最后一个分号
	    String[] records = str.split("_");
	    // 将拼接好的记录插入短信表
	    smsService.insertSms4Send(records[0], records[1]);
	}
	Iterator<String> it1 = content131.iterator();
	while (it1.hasNext()) {
	    System.out.println(it1.next());
	    String[] records = it1.next().split("_");
	    // 将拼接好的记录插入短信表
	    smsService.insertSms4Send(records[0], records[1]);
	}
	for (Map.Entry<String, Object> entry : content113.entrySet()) {
	    System.out.println(entry.getKey() + "--->" + entry.getValue());
	    String str = entry.getValue().toString();
	    String[] records = str.split("_");
	    // 将拼接好的记录插入短信表
	    smsService.insertSms4Send(records[0], records[1]);
	}
	for (Map.Entry<String, Object> entry : content114.entrySet()) {
	    System.out.println(entry.getKey() + "--->" + entry.getValue());
	    String str = entry.getValue().toString();
	    String[] records = str.split("_");
	    // 将拼接好的记录插入短信表
	    smsService.insertSms4Send(records[0], records[1]);
	}
	for (Map.Entry<String, Object> entry : content115.entrySet()) {
	    System.out.println(entry.getKey() + "--->" + entry.getValue());
	    String str = entry.getValue().toString();
	    String[] records = str.split("_");
	    // 将拼接好的记录插入短信表
	    smsService.insertSms4Send(records[0], records[1]);
	}
	Iterator<String> it2 = content142.iterator();
	while (it2.hasNext()) {
	    System.out.println(it2.next());
	    String[] records = it2.next().split("_");
	    // 将拼接好的记录插入短信表
	    smsService.insertSms4Send(records[0], records[1]);
	}
    }

}
