/**
 * com.hpe.cmca.job.SmsIntegrationJob.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.interfaces.UserBehavMapper;
import com.hpe.cmca.pojo.BehaveData;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.Json;
import com.hpe.cmca.util.PostUtil;

import net.sf.json.JSONObject;

@Service("BeHaveJobTaskJob")
public class BeHaveJobTaskJob extends BaseObject {
	/*
	 * // 测试环境 private String logTestDir =
	 * "http://10.248.12.24:8481/biOpen/logRecord/syncLogs"; // 生产环境 private
	 * String logproductionDir =
	 * "http://10.255.219.201/biOpen/logRecord/syncLogs";
	 */

	// 得到暂存的地址
	public String getlogTransferDir() {
		String logTransferDir = propertyUtil.getPropValue("logTransferDir");
		return logTransferDir;
	}

	

	private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat fo = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	private static SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// 通过接口方式，将日志发送给亚信。
	public void work() {

		try {

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String format = df.format(cal.getTime());

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("save_time", format);

			UserBehavMapper userBehavMapper = mybatisDao.getSqlSession().getMapper(UserBehavMapper.class);
			List<BehaveData> list = userBehavMapper.selectUserBehav(params);

			for (int i = 0; i < list.size(); i++) {
				BehaveData behaveData = list.get(i);
				String operateDate = behaveData.getOperateDate();
				Date parse = fo.parse(operateDate);

				String format2 = f.format(parse);
				behaveData.setOperateDate(format2);
				String operationName = behaveData.getOperationName();
				if(operationName==null||"".equals(operationName)){
					behaveData.setOperationName("未获取操作编码");
				}
				String clientAddress = behaveData.getClientAddress();
				if(clientAddress==null||"".equals(clientAddress)){
					behaveData.setClientAddress("未获取客户端IP");
				}
				String other = behaveData.getOther();
				if(other==null||"".equals(other)){
					behaveData.setOther("未获取到客户端基本信息");
				}
				String plat = behaveData.getPlat();
				if(plat==null||"".equals(plat)){
					behaveData.setPlat("未获取到子系统标识");
				}
				String resourceUrl = behaveData.getResourceUrl();
				if(resourceUrl==null||"".equals(resourceUrl)){
					behaveData.setResourceUrl("未获取到模块路径");
				}
				String serverAddress = behaveData.getServerAddress();
				if(serverAddress==null||"".equals(serverAddress)){
					behaveData.setServerAddress("未获取到服务端路径");
				}
				String operator = behaveData.getOperator();
				if(operator==null||"".equals(operator)){
					behaveData.setOperator("未获取到用户名称");
				}
			}
			// logger.info("--------" + list);
			// 转换成json格式
			String encode = Json.Encode(list);
			logger.debug(encode + "------------------");
			//生产环境亚信的地址
			String getlogTransferDir = getlogTransferDir();
		
			JSONObject post = PostUtil.post(getlogTransferDir, encode);
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
