/**
 * com.hpe.cmca.job.SmsIntegrationJob.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.auditTask.service.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.dao.MybatisDao;
import com.hpe.cmwa.util.Json;
import com.hpe.cmwa.util.PostUtil;

import net.sf.json.JSONObject;

@Service("BeHaveJobTaskJob")
public class BeHaveJobTaskJob extends BaseObject {

	@Autowired
	protected MybatisDao mybatisDao;
	// 测试环境
	private String url = "http://10.255.219.201/biOpen/logRecord/syncLogs";
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	/*
	 * private static SimpleDateFormat fo = new SimpleDateFormat(
	 * "yyyyMMdd HH:mm:ss");
	 * 
	 * private static SimpleDateFormat f = new SimpleDateFormat(
	 * "yyyy-MM-dd HH:mm:ss");
	 */
	// 通过接口方式，将日志发送给亚信。
	public void work() {

		try {

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String createTime = df.format(cal.getTime());

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("createTime", createTime);
			List<Map<String, Object>> list = mybatisDao.getList("commonMapper.selectLog", params);

			/*
			 * for (int i = 0; i < list.size(); i++) { BehaveData behaveData =
			 * list.get(i); String operateDate = behaveData.getOperateDate();
			 * Date parse = fo.parse(operateDate);
			 * 
			 * String format2 = f.format(parse);
			 * behaveData.setOperateDate(format2);
			 * 
			 * }
			 */
			logger.info("--------" + list);
			// 转换成json格式
			String encode = Json.Encode(list);
			logger.debug(encode + "------------------");
			JSONObject post = PostUtil.post(url, encode);
			String str = post.getString("rejectCode");
			logger.debug(str);
			if ("0000".equals(str)) {
				logger.debug("#########################数据库同步成功");
			}
		} catch (Exception e) {

			logger.error("BehaveJobTaskJob  work   出现同步数据库异常");
			e.getMessage();
		}

	}
}
