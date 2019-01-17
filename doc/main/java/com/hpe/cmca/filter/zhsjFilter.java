package com.hpe.cmca.filter;

import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserPrivilegeService;
import com.asiainfo.biframe.privilege.webosimpl.UserPrivilegeServiceImpl;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.ZhsjMapper;
import com.hpe.cmca.pojo.SSOData;
import com.hpe.cmca.service.SSOService;
import com.hpe.cmca.util.SpringContextHolder;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 过滤从智慧审计链接过来的url
 */
public class zhsjFilter implements Filter {

    private static final Logger logger = Logger.getLogger(zhsjFilter.class);

    IUserPrivilegeService service = new UserPrivilegeServiceImpl();


    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain c) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            MybatisDao mybatisDao = SpringContextHolder.getBean("mybatisDao");
            ZhsjMapper zhsjMapper = mybatisDao.getSqlSession().getMapper(ZhsjMapper.class);

            String userId = request.getParameter("userId");
            BASE64Decoder base64Decoder = new BASE64Decoder();
            userId = new String(base64Decoder.decodeBuffer(userId),"UTF-8");

            Map<String,Object> param = new HashMap<>();
            param.put("userId",userId);
            List<Map<String,Object>> mapList = zhsjMapper.getZhsjUserList(param);
            if(mapList.size()==0){
                HttpServletResponse hres = (HttpServletResponse) response;
                hres.sendRedirect("/cmca/home/error");
                return;
            }


            if (httpRequest.getSession().getAttribute("ssoUSER") == null || !userId.equals(httpRequest.getSession().getAttribute("userId"))) {

                IUserPrivilegeService service = new UserPrivilegeServiceImpl();
                IUser user = service.getUser(userId);

                httpRequest.getSession().setAttribute("userId", userId);
                httpRequest.getSession().setAttribute("ssoUSER", user);

                Map<String, Boolean> rightMap = new HashMap<String, Boolean>();

                boolean haveRight = false;
                SSOService ssoService = (SSOService) SpringContextHolder.getBean("SSOService");
                List<SSOData> list = ssoService.getDictCommon("RESOURCE_ID_3");

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

                httpRequest.getSession().setAttribute("rightMap", rightMap);
                logger.error("zhsj-" + user.getUserid() + " right info:" + rightMap);
                logger.error("ZHSJ:SSOFilter login 单点登陆成功 ");
            }
        } catch (Exception e) {
            logger.error("zhsj handle right error");
            HttpServletResponse hres = (HttpServletResponse) response;
            hres.sendRedirect("/cmca/home/error");
            return;
        }
        c.doFilter(httpRequest, httpResponse);

    }

    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
