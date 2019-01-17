/**
 * com.hp.cmcc.job.service.NotiFileYCCZ5000YCHFZSDetailProcessor.java
 * Copyright (c) 2018 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.job;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.util.FileUtil;

/**
 * <pre>
 * Desc： 
 * @author   hufei
 * @refactor hufei
 * @date     2018-1-24 下午4:37:44
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2018-1-24 	   hufei 	         1. Created this class.
 * </pre>
 */
@Service("NotiFileYCCZ5000YCHFZSDetailProcessor")
public class NotiFileYCCZ5000YCHFZSDetailProcessor extends AbstractNotiFileProcessor {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MybatisDao myBatisDao;
    private ArrayList<String> strs;
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
	strs=new ArrayList<String>();
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
	this.setFileName("异常话费赠送金额排名前50账户明细_全国_" + month);
	// 获取新文件名
	Map<String,Object> paramMap=new HashMap<String,Object>();
	paramMap.put("focusCd", "5003");
	paramMap.put("subjectId", 5);
	Map<String,Object> configMap=myBatisDao.get("pmhz.selectDetailsConfig",paramMap);
	String sql =configMap.get("csv_sql").toString();
	File file = new File(this.getLocalPath());
	Writer streamWriter = null;
	try {
	    streamWriter = new OutputStreamWriter(new FileOutputStream(file), "GBK");
	    final PrintWriter printWriter = new PrintWriter(streamWriter);
	    printWriter.println(configMap.get("csv_header").toString());
	    jdbcTemplate.query(sql, new Object[] { month }, new RowCallbackHandler() {

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
