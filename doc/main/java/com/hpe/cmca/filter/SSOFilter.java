package com.hpe.cmca.filter;

import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserCompany;
import com.asiainfo.biframe.privilege.IUserPrivilegeService;
import com.asiainfo.biframe.privilege.webosimpl.UserPrivilegeServiceImpl;
import com.hpe.cmca.common.FilePropertyPlaceholderConfigurer;
import com.hpe.cmca.pojo.SSOData;
import com.hpe.cmca.service.SSOService;
import com.hpe.cmca.util.UserRoleService;
import edu.yale.its.tp.cas.client.filter.CASFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSOFilter extends CASFilter {

	ServletContext servletContext = null;
	IUserPrivilegeService service = new UserPrivilegeServiceImpl();

	@Autowired
	protected FilePropertyPlaceholderConfigurer propertyUtil = null;


	private static final Logger logger = Logger.getLogger(SSOFilter.class);

	@Override
	public void init(FilterConfig fc) {
		try {
			super.init(fc);
			servletContext = fc.getServletContext();
		} catch (ServletException e) {
			e.printStackTrace();
			logger.error("init ERROR:",e);
		}
	}

	public void login(HttpServletRequest resquest, HttpServletResponse response, String userId) throws Exception {

		logger.error("SSOFilter login 开始 ");
		if (resquest.getParameter("isPwdExpired") != null) {
			resquest.getSession().setAttribute("isPwdExpired", resquest.getParameter("isPwdExpired"));
			resquest.getSession().setAttribute("remainDays", resquest.getParameter("remainDays"));
		}

		logger.error("SSOFileter userId==" + userId);

		IUserCompany dep = service.getUserDept(userId);
		Integer depId = dep.getDeptid();
		logger.error("SSOFileter userDepId==" + depId);
		String depName = dep.getTitle();
		logger.error("SSOFileter userDepName==" + depName);

		resquest.getSession().setAttribute("depId", depId);
		resquest.getSession().setAttribute("depName", depName);

		IUser user = service.getUser(userId);

		logger.error("SSOFileter username==" + user.getUsername());

		String userName = user.getUsername();
		resquest.getSession().setAttribute("userName", userName);
		resquest.getSession().setAttribute("userId", userId);

		 String prvdId=service.getUserCurrentCity(userId);
		 resquest.getSession().setAttribute("userPrvdId", prvdId);

		 String phoneNum=user.getMobilePhone();
		 resquest.getSession().setAttribute("phoneNum", phoneNum);

		 String email=user.getEmail();
		 resquest.getSession().setAttribute("email", email);


		Map<String, Boolean> rightMap = new HashMap<String, Boolean>();

		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		//Dict dict = (Dict) context.getBean("dict");
		  boolean haveRight = false;
		//List<SimpleMap> list = dict.getList("RESOURCE_ID_3");
		 SSOService ssoService= (SSOService) context.getBean("SSOService");
		 List<SSOData> list = ssoService.getDictCommon("RESOURCE_ID_3");

//		 List<IUserRight> rightList = service.getRight(userId, 0, 9000);
//		 List<String> l=new ArrayList<String>();
//
//			for(int i=0;i<rightList.size();i++){
//			l.add(rightList.get(i).getResourceId());
//		}
		// tianyue 2015-12-22 优化登录，权限判断使用并发

		Thread[] ths = new Thread[list.size()];
		for (int i = 0; i < list.size(); i++) {

			ths[i] = new RightThread(list.get(i).getText().trim(), userId, 9000);
			ths[i].start();
		}
		// 等待1秒
		Thread.sleep(1000);
		Class<RightThread> cls = RightThread.class;
		long t1 = new Date().getTime();
		Boolean isFail = false;
		// 预防越界
		while (rightMap.size() < list.size()) {

			for (int i = 0; i < list.size(); i++) {

				if (rightMap.containsKey(list.get(i).getValue())) {
					continue;
				}
				int hr = cls.getDeclaredField("HaveRight").getInt(ths[i]);
				if (hr > 0) {
					rightMap.put(list.get(i).getValue(), (hr == 2));
				}
			}
			// 如果执行了N秒还未结束,跳出
			int n = 65;
			if (new Date().getTime() - t1 > n * 1000) {
				isFail = true;
				logger.error("right fail.timeout . t > " + (n * 1000));
				break;
			}

			Thread.sleep(100);
		}

		if (isFail) {
			for (int i = 0; i < list.size(); i++) {

				if (rightMap.containsKey(list.get(i).getValue())) {
					continue;
				}
				logger.error("right fail." + list.get(i).getText() + ":" + list.get(i).getValue() + " default false.");
				rightMap.put(list.get(i).getValue(), false);
				// 中断出了问题的线程
				try {
					ths[i].interrupt();
				} catch (Exception e) {
					logger.error("right fail." + list.get(i).getText() + ":" + list.get(i).getValue() + " " + e.getMessage() + " interrupt  thread.");
					logger.error(e.getMessage(), e);
				}

			}
		}

		resquest.getSession().setAttribute("ssoUSER", user);
		try {
			logger.error("sso userinfo:" + user);

			UserRoleService userRoleService = (UserRoleService) context.getBean("userRoleService");

			int rid = userRoleService.getRoleIdByUserID(user.getUserid());

			logger.error("user " + user.getUserid() + " roleid" + rid);

			resquest.getSession().setAttribute("ssoUSERRoleId", rid);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		resquest.getSession().setAttribute("rightMap", rightMap);
		logger.error(user.getUserid() + " right info:" + rightMap);

		logger.error("SSOFilter login 单点登陆成功 ");
	}
}
