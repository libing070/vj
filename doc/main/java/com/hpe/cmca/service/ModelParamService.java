package com.hpe.cmca.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import com.asiainfo.biframe.privilege.IUser;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.model.ModelParam;
import com.hpe.cmca.model.ParamEditLog;
import com.hpe.cmca.util.CalendarUtils;
import com.hpe.cmca.web.taglib.Pager;


/**
 * 
 * <pre>
 * Desc：参数管理service copy from cmccca 
 * 
 *  1、如果出现“阀值”or“生效时间”or“失效时间”变更，则：
 *     第一步：以最新的参数值，新建一条参数记录（未发生变化的字段，用原有值）；
 *     第二步：以新插入参数记录的失效时刻的前一秒钟，作为原参数记录的“新失效时间”对其进行更新；
 *     第三步：插入参数变更日志（哪个参数发生变化就插入一条日志，多个参数字段值变化时，就是多条记录）
 *     第四步：完成本次更新操作，返回页面；
 *  2、如果“任何字段值”发生变化
 *     第一步：直接更新参数记录；
 *     第二步：插入参数变更日志（哪个参数发生变化就插入一条日志，多个参数字段值变化时，就是多条记录）
 *     第三步：完成本次更新操作，返回页面；
 * @author GuoXY
 * @refactor GuoXY
 * @date   Dec 6, 2016 8:42:44 AM
 * @version 1.0
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Dec 6, 2016 	   GuoXY 	         1. Created this class. 
 * </pre>
 */
@Repository
@Service
public class ModelParamService extends SqlSessionDaoSupport {

	@Autowired
	private MybatisDao	mybatisDao;

	public List<Map<String, Object>> getModelParamList(Pager pager, String subjectId, String concernId) throws Exception {

		pager.addParameter("subjectId", subjectId);
		pager.addParameter("concernId", concernId);
		return mybatisDao.getList("ModelParamMapper.selectModelParam", pager);
	}
	
	public List<Map<String, Object>> selectModelSubject(String id) throws Exception {
		Map<String,Object> params =new HashMap<String,Object>(); 
		params.put("id", id);
		return mybatisDao.getList("ModelParamMapper.selectModelSubject",params);
	}

	public void editParam(ModelParam modelparam, HttpServletRequest request) throws Exception {

		modelparam.setOld_ThresholdId(modelparam.getThresholdId());
		if (modelparam.getIsValueChanged().equals(true) 
				|| modelparam.getIsEffChanged().equals(true) 
				|| modelparam.getIsEndChanged().equals(true)) {
			String beforeInsertId=modelparam.getThresholdId();
		
			// 将数据库中日期类型的字段的参数进行转换，否则，入库时，只有日期无法精确到时分秒  modify by GuoXY 20161214
			String newEffDate = CalendarUtils.getTimeBySdfInOut(modelparam.getThresholdEffdate(), 
														"yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
			String newEndDate = CalendarUtils.getTimeBySdfInOut(modelparam.getThresholdEnddate(), 
														"yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
			// 这里的 00 是秒后的时间精度，为了符合sql里格式化语句对时间的描述   modify by GuoXY 20161214
			modelparam.setThresholdEffdate(newEffDate + "00");
			modelparam.setThresholdEnddate(newEndDate + "00");
			mybatisDao.add("ModelParamMapper.insertModelParam", modelparam);
			
			// 获取原参数记录失效时间，并将其提前1秒，作为原参数记录新的失效时间   modify by GuoXY 20161214
			String newEndTime = modelparam.getThresholdEnddate();
			String afterInsertId = modelparam.getThresholdId();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(modelparam.getThresholdEnddate()));
			calendar.add(Calendar.SECOND, -1);
			Date date = (Date) calendar.getTime();
//			String str = sdf.format(date);
			Date d =  new Date();
			d.setSeconds(d.getSeconds()-1);
			String str = sdf.format(d);
			
			// 这里的 00 是秒后的时间精度，为了符合sql里格式化语句对时间的描述  modify by GuoXY 20161214
			modelparam.setThresholdEnddate(str + "00");
//			modelparam.setThresholdId(beforeInsertId);
//			modelparam.setThresholdCode(thresholdCode);
			mybatisDao.update("ModelParamMapper.updateModelParamEndDate", modelparam);//更新除了上面新插入的一条记录外所有和修改记录的阈值代码一样的记录的失效时间
			
			modelparam.setThresholdId(afterInsertId);
			modelparam.setThresholdEnddate(newEndTime);
			editLog(modelparam, request);
			return;
		}
		if (modelparam.getIsChanged()) {
			mybatisDao.update("ModelParamMapper.updateModelParam", modelparam);//更新修改项 name /code/ operator
			editLog(modelparam, request);
			return;
		}

	}

	public void editLog(ModelParam modelparam, HttpServletRequest request) throws Exception {

		Calendar calendar = Calendar.getInstance();
		Date date = (Date) calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// 这里的 00 是秒后的时间精度，为了符合sql里格式化语句对时间的描述  modify by GuoXY 20161214
		String str = sdf.format(date) + "00";
		ParamEditLog parameditlog = new ParamEditLog();
		String editPerson;
		IUser user = (IUser) request.getSession().getAttribute("ssoUSER");
		if (user == null) {
			editPerson = "尚未登录";
		} else {
			editPerson = user.getUsername();
		}
		String editReason = modelparam.getReason();
		if (modelparam.getIsCodeChanged()) {
			parameditlog.setEdit_person(editPerson);
			parameditlog.setEdit_reason(editReason);
			parameditlog.setEdit_time(str);
			parameditlog.setEdit_code(modelparam.getThresholdCode());
			parameditlog.setEdit_col("阈值编码");
			parameditlog.setOld_id(modelparam.getOld_ThresholdId());
			parameditlog.setOld_value(modelparam.getOld_ThresholdCode());
			parameditlog.setNew_id(modelparam.getThresholdId());
			parameditlog.setNew_value(modelparam.getThresholdCode());

			mybatisDao.add("ModelParamMapper.insertLog", parameditlog);
		}

		if (modelparam.getIsNameChanged()) {
			parameditlog.setEdit_person(editPerson);
			parameditlog.setEdit_reason(editReason);
			parameditlog.setEdit_time(str);
			parameditlog.setEdit_code(modelparam.getThresholdCode());
			parameditlog.setOld_id(modelparam.getOld_ThresholdId());
			parameditlog.setNew_id(modelparam.getThresholdId());
			parameditlog.setEdit_col("阈值名称");
			parameditlog.setOld_value(modelparam.getOld_ThresholdName());
			parameditlog.setNew_value(modelparam.getThresholdName());

			mybatisDao.add("ModelParamMapper.insertLog", parameditlog);
		}

		if (modelparam.getIsOperatorsChanged()) {
			parameditlog.setEdit_person(editPerson);
			parameditlog.setEdit_reason(editReason);
			parameditlog.setEdit_time(str);
			parameditlog.setEdit_code(modelparam.getThresholdCode());
			parameditlog.setOld_id(modelparam.getOld_ThresholdId());
			parameditlog.setNew_id(modelparam.getThresholdId());
			parameditlog.setEdit_col("阈值逻辑");
			parameditlog.setOld_value(modelparam.getOld_ThresholdOperators());
			parameditlog.setNew_value(modelparam.getThresholdOperators());

			mybatisDao.add("ModelParamMapper.insertLog", parameditlog);
		}

		if (modelparam.getIsValueChanged()) {
			parameditlog.setEdit_person(editPerson);
			parameditlog.setEdit_reason(editReason);
			parameditlog.setEdit_time(str);
			parameditlog.setEdit_code(modelparam.getThresholdCode());
			parameditlog.setOld_id(modelparam.getOld_ThresholdId());
			parameditlog.setNew_id(modelparam.getThresholdId());
			parameditlog.setEdit_col("阈值");
			parameditlog.setOld_value(modelparam.getOld_ThresholdValue());
			parameditlog.setNew_value(modelparam.getThresholdValue());

			mybatisDao.add("ModelParamMapper.insertLog", parameditlog);
		}

		if (modelparam.getIsEffChanged()) {
			parameditlog.setEdit_person(editPerson);
			parameditlog.setEdit_reason(editReason);
			parameditlog.setEdit_time(str);
			parameditlog.setEdit_code(modelparam.getThresholdCode());
			parameditlog.setOld_id(modelparam.getOld_ThresholdId());
			parameditlog.setNew_id(modelparam.getThresholdId());
			parameditlog.setEdit_col("生效时间");
			parameditlog.setOld_value(modelparam.getOld_ThresholdEffdate());
			parameditlog.setNew_value(modelparam.getThresholdEffdate());

			mybatisDao.add("ModelParamMapper.insertLog", parameditlog);
		}

		if (modelparam.getIsEndChanged()) {
			parameditlog.setEdit_person(editPerson);
			parameditlog.setEdit_reason(editReason);
			parameditlog.setEdit_time(str);
			parameditlog.setEdit_code(modelparam.getThresholdCode());
			parameditlog.setOld_id(modelparam.getOld_ThresholdId());
			parameditlog.setNew_id(modelparam.getThresholdId());
			parameditlog.setEdit_col("失效时间");
			parameditlog.setOld_value(modelparam.getOld_ThresholdEnddate());
			parameditlog.setNew_value(modelparam.getThresholdEnddate());

			mybatisDao.add("ModelParamMapper.insertLog", parameditlog);
		}
	}

}
