/**
 * com.hp.base.common.FilePropertyPlaceholderConfigurer.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * <pre>
 * 采用Spring的加载属性信息 
 * 在应用中获取
 * @author peter.fu
 * @refactor peter.fu
 * @date   Jan 27, 2015 3:26:39 PM
 * @version 1.0 
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  Jan 27, 2015 	   peter.fu 	         1. Created this class. 
 * </pre>
 */
public class FilePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private Map<String, String>	resolvedProps;	// 将属性保存起来

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		
		super.processProperties(beanFactoryToProcess, props);
		resolvedProps = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			resolvedProps.put(keyStr, parseStringValue(props.getProperty(keyStr), props, new HashSet()));
		}
	}
	
	public String getPropValue(String key){
		
		if(StringUtils.isBlank(key)){
			return "";
		}
		return StringUtils.trimToEmpty(resolvedProps.get(key));
	}

	public Map<String, String> getResolvedProps() {
		return resolvedProps;
	}

	public void setResolvedProps(Map<String, String> resolvedProps) {
		this.resolvedProps = resolvedProps;
	}
	
}
