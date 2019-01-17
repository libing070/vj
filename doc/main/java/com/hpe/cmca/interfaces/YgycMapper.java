package com.hpe.cmca.interfaces;

import java.util.List;
import java.util.Map;

import com.hpe.cmca.pojo.ParameterData;
import com.hpe.cmca.pojo.YgycData;


public interface YgycMapper {
	//异常赠送话费  员工数量排名（柱形图）
	public List<YgycData> getYgycEmployeeQty(ParameterData data);
	//异常赠送话费  赠送金额排名（柱形图）
	public List<YgycData> getYgycPresentAmount(ParameterData data);
	//异常赠送话费  员工数量趋势（折线图）
	public List<YgycData> getYgycEmployeeQtyLine(ParameterData data);
	//异常赠送话费  赠送金额趋势（折线图）
	public List<YgycData> getYgycPreAmountLine(ParameterData data);
	//风险地图
	public List<YgycData> getYgycMapData(ParameterData data);
	//风险地图下方卡片
	public List<YgycData> getYgycMapBottomData(ParameterData data);
	//风险地图地市下钻用户的员工信息
	public List<YgycData> getYgycJobnumber(ParameterData data);
	//风险地图地市下钻用户的员工-手机号汇总信息
	public List<YgycData> getYgycPhoneTable(ParameterData data);
	//风险地图手机号下钻操作员工赠送信息
	public List<YgycData> getYgycOperatorByphone(ParameterData data);
	//员工异常操作-统计分析-排名汇总
	public List<YgycData> getRankTable(ParameterData data);
	/*员工异常操作-统计分析-排名汇总-集团信息
	public YgycData getRankTableGroup(ParameterData data);
	*/
	//员工异常操作-统计分析-排名汇总-各省信息
	public List<Map> getHistoryTable(ParameterData data);
	//员工异常操作-统计分析-重点关注用户
	public List<Map> getFocusUserTable(ParameterData data);
	//员工异常操作-统计分析-重点关注用户-下钻
	public List<YgycData> getFocusUserDetaile(ParameterData data);
	//员工异常 重点关注员工号
	public List<Map<String,Object>> getYgycFocusEmployee(ParameterData data);
	//员工异常  重点关注员工号 下钻
	public List<Map<String,Object>> getYgycFocusEmployeeTable(ParameterData data);
	
}
