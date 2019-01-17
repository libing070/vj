package com.hpe.cmca.finals;

public enum CacheKeys {
	/**
	 * 组件配置表缓存KEY
	 */
	KEY_EAP_COMPONENTS_CFG,
	/**
	 * Session中登录信息的Key
	 */
	ssoUSER,
	/**
	 * 登录用户的具备权限的REST的Key
	 */
	SESSION_USER_RESTLIST,
	/**
	 * 公共REST，不需要鉴权的。
	 */
	REST_COMMON,
	/**
	 * 用户拥有的菜单权限Key
	 */
	SESSION_MENU_List,
	/**
	 * REST接口列表缓存的Key
	 */
	EAP_DICT_MENU_REST

}
