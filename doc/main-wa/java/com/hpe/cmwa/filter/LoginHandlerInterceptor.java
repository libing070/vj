package com.hpe.cmwa.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginHandlerInterceptor implements HandlerInterceptor {

	Logger logger = Logger.getLogger(LoginHandlerInterceptor.class);

	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {

		 
//		String path = req.getServletPath();
//		
//		//如果非REST接口，直接方形
//		if (path.indexOf(".")>0 || path.equals("/home/index")) {
//			return true;
//		} 
//		logger.info(path);
//		List<String> rList = (List<String>) req.getSession().getAttribute(CacheKeys.SESSION_USER_RESTLIST.toString());
//
//		//如果未找到有权限的REST列表，或者列表中不存在当前访问的REST，拒绝访问
//		if (rList == null || rList.size() == 0 || rList.contains(path)==false) {
//			//throw new Exception("No Right!");
//			 logger.error("LoginHandlerInterceptor:access denied:>>>" + path);
//			 return false;
//		} 

		return true;
		
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		

	}

}
