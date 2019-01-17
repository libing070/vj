/**
 * com.hp.cmcc.job.service.NotiFileDzq6000PrvdChnlProcessor.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.service.FileAutoService;
import com.hpe.cmca.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * <pre>
 * Desc：
 * @author   hufei
 * @refactor hufei
 * @date     2018-1-28 下午4:39:39
 * @version  1.0
 *
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  2018-1-28 	   hufei 	         1. Created this class.
 * </pre>
 */
@Service("NotiFileKsck1100T1Sh2Processor")
public class NotiFileKsck1100T1Sh2Processor extends AbstractNotiFileProcessor {


    @Autowired
    private JdbcTemplate      jdbcTemplate;
    @Autowired
    private FileAutoService fileAutoService;
    @Autowired
    private MybatisDao myBatisDao;
    private ArrayList<String> strs;
    private String	    prvdId;
    private String	    prvdName;

    public ArrayList<String> getFileNameList() {
	return strs;
    }

    // 重写构建文件名方法
    protected String buildFileName() {
	fileName = fileName + ".csv";
	return fileName;
    }

    @Override
    public void start() throws Exception {
		strs = new ArrayList<String>();
		prvdId="10000";
		prvdName="全国";
		generate();

    }

    /*
     * (non-Javadoc)
     *
     * @see com.hp.cmcc.job.service.AbstractNotiFileProcessor#generate()
     */
    @Override
    public boolean generate() throws Exception {
	// TODO Auto-generated method stub
	this.setFileName("跨省窜卡入网渠道排名_全国表1_" + month);//
	// 获取新文件名
	Map<String,Object> paramMap=new HashMap<String,Object>();
	paramMap.put("focusCd", "t1sh2");
	paramMap.put("subjectId", 11);
	Map<String,Object> configMap=myBatisDao.get("pmhz.selectDetailsConfig",paramMap);
	String sql =configMap.get("csv_sql").toString();
	File file = new File(this.getLocalPath());
	Writer streamWriter = null;
	try {
	    streamWriter = new OutputStreamWriter(new FileOutputStream(file), "GBK");
	    final PrintWriter printWriter = new PrintWriter(streamWriter);
	    printWriter.println(configMap.get("csv_header").toString());
	    jdbcTemplate.query(sql, new Object[] { month,prvdId }, new RowCallbackHandler() {

		public void processRow(ResultSet rs) throws SQLException {
		    int columCount = rs.getMetaData().getColumnCount();
		    StringBuilder line = new StringBuilder(100);
		    for (int i = 1; i <= columCount; i++) {
			line.append(rs.getObject(i)).append("	,");
		    }
		    printWriter.println(line.substring(0, line.length() - 1));
		}
	    });

	    printWriter.flush();
	} catch (Exception e) {
	    throw new RuntimeException("生成csv文件异常", e);
	} finally {
	    FileUtil.closeWriter(streamWriter);
	    strs.add(fileName);
	}
	return false;
    }

}