package com.hpe.cmca.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hpe.cmca.controller.BaseController;
import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;

public class LoginHandlerInterceptor extends HandlerInterceptorAdapter {

	Logger	logger	= Logger.getLogger(LoginHandlerInterceptor.class);
	@Autowired 
	private BaseController baseController;
	 
	@SuppressWarnings({ "null", "unchecked" })
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {

		String path = req.getServletPath();
		if (handler instanceof HandlerMethod) {

			//
			// //如果非REST接口，直接方形
			// if (path.indexOf(".")>0 || path.equals("/home/index")) {
			// return true;
			// }
			// logger.info(path);
			// List<String> rList = (List<String>) req.getSession().getAttribute(CacheKeys.SESSION_USER_RESTLIST.toString());
			//
			// //如果未找到有权限的REST列表，或者列表中不存在当前访问的REST，拒绝访问
			// if (rList == null || rList.size() == 0 || rList.contains(path)==false) {
			// //throw new Exception("No Right!");
			// logger.error("LoginHandlerInterceptor:access denied:>>>" + path);
			// return false;
			// }

			logger.debug("");

			HandlerMethod handler2 = (HandlerMethod) handler;
			CmcaAuthority fireAuthority = handler2.getMethodAnnotation(CmcaAuthority.class);

			if (null == fireAuthority) {
				// 没有声明权限,放行
				logger.error("LoginHandlerInterceptor:access not set:>>>" + path);
				return true;
			}

			logger.debug(fireAuthority.toString());
			//BaseController a = new BaseController();
			Map<String, Boolean> authorityMap = baseController.getUserRight(req);
			boolean aflag = false;
			try{
				for (AuthorityType at : fireAuthority.authorityTypes()) {
					if (authorityMap.get(at.getName()) == true) {
						aflag = true;
						break;
					}
					aflag = false;
				}
			}catch(Exception e){
				
				logger.error("权限认证出错:"+e.getMessage(),e);
			}


			if (aflag)
				logger.error("LoginHandlerInterceptor:access allowed:>>>" + path);			
			else{
				logger.error("LoginHandlerInterceptor:access denied:>>>" + path);
//				String referer = req.getHeader("referer");
//				if(referer==null)
//					req.getSession().setAttribute("authority", "home");
//				else if(!referer.contains("/home/"))
//					req.getSession().setAttribute("authority", "home");				
				res.sendRedirect("/cmca/home/error");											
			}
			return aflag;
		}
		logger.error("LoginHandlerInterceptor:access static resources:>>>" + path);
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// TODO Auto-generated method stub

	}

}
