package com.hpe.cmca.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hpe.cmca.pojo.AudTrmData;
import com.hpe.cmca.pojo.BehaveData;

public interface UserBehavMapper {

	// 获取用户操作
	public List<Map<String, Object>> getUserBehav(Map<String, Object> map);

	// T-1日的所有用户信息
	public List<BehaveData> selectUserBehav(HashMap<String, Object> params);

	// 新增用户操作
	public Integer addUserBehav(Map<String, Object> map);

	// 新增用户操作
	public Integer addUserBehavNew(Map<String, Object> map);

}
