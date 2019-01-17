package com.hpe.cmwa.auditTask.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmwa.auditTask.service.job.MonitorCheckJobService;
import com.hpe.cmwa.common.BaseObject;
import com.hpe.cmwa.util.HelperHttp;
import com.hpe.cmwa.util.MD5;

/**
 * <pre>
 * Desc： 数据监控核查数据推送
 * @author ren yx
 * @refactor ren yx
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Mar 9, 2015 	   ren yx	         1. Created this class. 
 * </pre>
 */
@Service("monitorCheckJob")
public class MonitorCheckJob extends BaseObject{
	
	/**
	 * 国信给的加密key
	 */
	private String				key					= "sj2bonc";
	@Autowired
	private MonitorCheckJobService monitorCheckJobService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void work(){
		
		//获取到hpeapm库下表中的未同步数据的账期；
		List<Map> paramsList = monitorCheckJobService.getUnSynDate();
		
		if(paramsList.size() != 0){
			//查询出未同步状态下的数据
			List<Map> list = monitorCheckJobService.selectNeedSyncList(paramsList);
			//更新之前删除掉公共库下含有此标识的数据
			monitorCheckJobService.deleteData(paramsList);
			/*String timestamp = new java.text.SimpleDateFormat("yyyyMMddHH").format(new java.util.Date());
			for (Map map : list) {
				map.put("time_stamp", timestamp);
			}*/
			this.logger.info("开始同步橄榄表数据到audit_db库下的橄榄表");
			monitorCheckJobService.dataSynchToAudit(list);
			this.logger.info("同步完成后update数据状态");
			monitorCheckJobService.updateDataStatus();
			//调用国信给的接口，通知国信方到公共库下核查表中同步数据
			for (Map map : paramsList) {
				String dataMonth = (String) map.get("date");
				if(dataMonth!=null){
					informGXTakeData(dataMonth);
				}
			}
		}
	}
	//通知国信同步数据
	public void informGXTakeData(String dataMonth){
		String httpUrl = this.propertyUtil.getPropValue("gxCheckUrl");
		Map<String, Object> params =  new HashMap<String, Object>();
		params.put("dataMonth", dataMonth);
		String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
		params.put("timestamp", timestamp);
		String validateStr = MD5.MD5Crypt(dataMonth+ timestamp+key);
		params.put("validateStr", validateStr);
		String httpsReturnValue ="";
		try {
			httpsReturnValue = HelperHttp.getURLContent(httpUrl, params);
//			JSONObject.fromObject(httpsReturnValue);
		} catch (Exception e) {
			this.logger.info("插入账期："+dataMonth,e);
		}
	}
	
}
