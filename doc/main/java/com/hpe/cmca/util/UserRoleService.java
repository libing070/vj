package com.hpe.cmca.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.util.JacksonJsonUtil;

@Service(value = "userRoleService")
public class UserRoleService {

	@Autowired
	private MybatisDao mybatisDao;

	private Map<String, Integer> userRoles;

	Logger logger = Logger.getLogger(UserRoleService.class);

	/**
	 * 根据用户ID查询用户角色ID
	 * 
	 * @param userId
	 * @return
	 */
	public int getRoleIdByUserID(String userId) {
		logger.error("getRoleIdByUserID:" + userId);
		logger.error("userRoles:" + JacksonJsonUtil.beanToJson(userRoles));

		if (userRoles == null || userRoles.size() == 0) { 
			init(); 
		}
		return userRoles.get(userId);
	}

	private synchronized void init() {
		if (userRoles == null || userRoles.size() == 0) {
			userRoles = new HashMap<String, Integer>();
			List<Map> l = mybatisDao.getList("noticeMapper.selectUserRole");
			if (l != null && l.size() > 0) {
				for (int i = 0; i < l.size(); i++) {

					userRoles.put(l.get(i).get("U").toString().trim(), Integer
							.valueOf(l.get(i).get("R").toString().trim()));

				}
			}
		}

	}
}
