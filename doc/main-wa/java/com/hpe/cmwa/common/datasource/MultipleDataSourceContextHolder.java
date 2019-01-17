/**
 * com.hpe.caf.common.CustomerContextHolder.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.common.datasource;

/**
 * <pre>
 * Desc： 
 * @author   peter.fu
 * @refactor peter.fu
 * @date     Feb 27, 2017 3:34:25 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Feb 27, 2017 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
public abstract class MultipleDataSourceContextHolder {

	public static final ThreadLocal<String>	holder				= new ThreadLocal<String>();

	public static void setDataSource(String name) {
		holder.set(name);
	}

	public static String getDataSouce() {
		return holder.get();
	}

	public static void clearDataSouce() {

		holder.remove();

	}

}
