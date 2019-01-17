package com.hpe.cmca.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.finals.CachManager;
import com.hpe.cmca.finals.CacheKeys;
@Service
public class WebParamsInitListener implements ApplicationListener<ContextRefreshedEvent> {

    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent evt) {
        if (evt.getApplicationContext().getParent() == null) {
            createSitemap();
        }
    }

    private void createSitemap() {
        Timer timer = new Timer("createSitemap", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("--->Create sitemap...");
               
                System.out.println("--->Success create sitemap...");
            }
        }, 2000);
    }
  
	@Autowired
	MybatisDao dao;

	 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initCache() {

		List<Map> l = dao.getList("EAP_COMPONENTS_CFG.getComponentsConfig");
		if (l != null && l.size() > 0) { 
			Map map = new HashMap();
			for (Map m : l) {

				map.put(m.get("COMP_CODE"), m);

			}

			CachManager.getInstance().putCache(
					CacheKeys.KEY_EAP_COMPONENTS_CFG, map);
		} 
		 
	}

}
