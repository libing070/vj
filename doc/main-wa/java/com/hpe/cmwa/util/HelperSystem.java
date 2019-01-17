package com.hpe.cmwa.util;

import java.util.List;
 

public class HelperSystem {

	 
	private static List<String> MENU_URL_lIST;
	public static final String LOGINUSERKEY = "CURRENT_LOGININFO_KEY";
	public static final String LOGINACCOUNTKEY = "CURRENT_LOGINACCOUNT_KEY";
	public static final String TenantIDKey = "TENANTIDKEY";
	/**
	 * 系统菜单数据
	 * 
	 * @return
	 */
	public static List<String> MENU_URL_lIST() {
		if (MENU_URL_lIST == null) {
			flushMENU_URL_lIST();
		}
		return MENU_URL_lIST;
	}

	/**
	 * 刷新的菜单路径数据
	 * 
	 * @return
	 */
	public static void flushMENU_URL_lIST() {

//		SYS_USER_VService sYS_USER_VService = SpringContextHolder
//				.getBean("sYS_USER_VService");
//
//		MENU_URL_lIST = sYS_USER_VService.getUserPermissionList(null);

	}
 
}
