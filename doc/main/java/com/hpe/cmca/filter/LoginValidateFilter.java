package com.hpe.cmca.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证用户是否登录
 * 
 * @author yuetian
 * 
 */
public class LoginValidateFilter implements Filter {

	@Override
	public void destroy() {
		System.out.println("LoginValidateFilter destroy.");

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest hreq = (HttpServletRequest) req;
		System.out.println(hreq.getServletPath());
		System.out.println(hreq.getSession().getAttribute("ssoUSER") );
		if (hreq.getSession().getAttribute("ssoUSER") != null) {

			chain.doFilter(req, res);

		} else {

			HttpServletResponse hres = (HttpServletResponse) res;
			hres.sendRedirect("/cmca/home/index");
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("init LoginValidateFilter");

	}

}
