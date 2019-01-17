package com.hpe.cmca.common;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.FileGenMapper;
import com.hpe.cmca.pojo.FileGenData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Service
@Controller

public class FileGenMgr {

    @Autowired
    MybatisDao mybatisDao;
    @Autowired
    FilePropertyPlaceholderConfigurer propertyUtil;
    static Integer count = 0;

    @RequestMapping(value = "work", produces = "text/json;charset=UTF-8")
    public void work() {
        FileGenMapper fileGenMapper = mybatisDao.getSqlSession().getMapper(FileGenMapper.class);
        Map<String, Object> param = new HashMap<>();
        List<FileGenData> listDataReady = fileGenMapper.getFileGenData(param);
        count = listDataReady.size();
        System.out.println("请求数量为:" + count);
        if (count == 0) return;
        Thread[] ths = new Thread[count];
        for (int i = 0; i < listDataReady.size(); i++) {
           // ths[i] = new FileGenThread(listDataReady.get(i));
            ths[i].setName("Month:"+listDataReady.get(i).getAudTrm() + "-Prvd:" + listDataReady.get(i).getPrvdId() + "-FileCode:" + listDataReady.get(i).getFileCode());
            ths[i].start();
        }
    }
}
