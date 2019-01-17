/**
 * com.hpe.caf.filter.DataSourceAspect.java
 * Copyright (c) 2017 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.common.datasource;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * <pre>
 * Desc： 
 * @author   peter.fu
 * @refactor peter.fu
 * @date     Mar 1, 2017 10:04:28 AM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Mar 1, 2017 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
public class DataSourceAspect {

	/**
	 * 拦截目标方法，获取由@DataSource指定的数据源标识，设置到线程存储中以便切换数据源
	 * @param point
	 * @throws Exception
	 */
	public void intercept(JoinPoint point) throws Exception {

		
		MultipleDataSourceContextHolder.clearDataSouce();
		
		Class<?> target = point.getTarget().getClass();
		MethodSignature signature = (MethodSignature) point.getSignature();
		// 默认使用目标类型的注解，如果没有则使用其实现接口的注解
		for (Class<?> clazz : target.getInterfaces()) {
			resolveDataSource(clazz, signature.getMethod());
		}
		resolveDataSource(target, signature.getMethod());
		
	}

	/**
	 * 提取目标对象方法注解和类型注解中的数据源标识
	 * @param clazz
	 * @param method
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	private void resolveDataSource(Class<?> clazz, Method method) throws SecurityException, NoSuchMethodException {

		Class<?>[] types = method.getParameterTypes();
		// 默认使用类型注解
		if (clazz.isAnnotationPresent(DataSourceName.class)) {
			DataSourceName source = clazz.getAnnotation(DataSourceName.class);
			MultipleDataSourceContextHolder.setDataSource(source.value());
		}
		// 方法注解可以覆盖类型注解
		Method m = clazz.getMethod(method.getName(), types);
		if (m != null && m.isAnnotationPresent(DataSourceName.class)) {
			DataSourceName source = m.getAnnotation(DataSourceName.class);
			MultipleDataSourceContextHolder.setDataSource(source.value());
		}

	}

}
