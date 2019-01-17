package com.hpe.cmca.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * 
 * <pre>
 * Properties工具类
 * @author Huang Tao
 * @refactor Huang Tao
 * @date   2016年7月17日 下午1:47:08
 * @version 1.0
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2016年7月17日 	   Huang Tao 	         1. Created this class. 
 * </pre>
 */
public class HelperProperties {

    static Logger logger = Logger.getLogger(HelperProperties.class);

    public static Properties stringToProperties(String str) {
        Properties properties = null;
        InputStreamReader ir = new InputStreamReader(IOUtils.toInputStream(str));
        BufferedReader bf = new BufferedReader(ir);
        try {
            properties = new Properties();
            properties.load(bf);
            bf.close();
        }
        catch (IOException e) {
            logger.error("String convert properties object failed.", e);
        }

        return properties;

    }
}
