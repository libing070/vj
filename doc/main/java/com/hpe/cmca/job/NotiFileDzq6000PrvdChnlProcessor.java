/**
 * com.hp.cmcc.job.service.NotiFileDzq6000PrvdChnlProcessor.java
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
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.service.FileAutoService;


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
@Service("NotiFileDzq6000PrvdChnlProcessor")
public class NotiFileDzq6000PrvdChnlProcessor extends AbstractNotiFileProcessor {


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
	List<Map<String, Object>> prvdList =fileAutoService.getSJZLGenList("6000");
	List<String> finishList=new ArrayList<String>();
	Map<String, Object> row = new HashMap<String, Object>();
	strs = new ArrayList<String>();
	for (int i = 0; i < prvdList.size(); i++) {
	    row = prvdList.get(i);
	    if (row.size() != 0&&!row.get("excel_name").toString().contains("电子券平台间数据不一致")) {
		prvdId = row.get("excel_code").toString().substring(3,8);
		prvdName = row.get("excel_name").toString().substring(row.get("excel_name").toString().indexOf("排名汇总")+5, row.get("excel_name").toString().indexOf("_{"));
		if(!"10000".equals(prvdId)&&(finishList==null||!finishList.contains(prvdId))){
		    finishList.add(prvdId);
		    generate();
		}
	    }
	}

    }

    /*
     * (non-Javadoc)
     *
     * @see com.hp.cmcc.job.service.AbstractNotiFileProcessor#generate()
     */
    @Override
    public boolean generate() throws Exception {
	// TODO Auto-generated method stub
	this.setFileName("电子券管理违规渠道清单_"+prvdName+"_" + month);
	// 获取新文件名
	Map<String,Object> paramMap=new HashMap<String,Object>();
	paramMap.put("focusCd", "chnl");
	paramMap.put("subjectId", 6);
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