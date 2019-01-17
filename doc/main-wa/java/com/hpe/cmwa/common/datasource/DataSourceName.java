/**
 * com.hpe.caf.common.DataSource.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.common.datasource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <pre>
 * Desc： 数据源的名字，定义在spring配置文件中
 * @author   peter.fu
 * @refactor peter.fu
 * @date     Mar 1, 2017 9:56:33 AM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Mar 1, 2017 	   peter.fu 	         1. Created this class. 
 * </pre>  
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)
public @interface DataSourceName {
	String value();
}
