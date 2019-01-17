/**
 * com.hpe.cmca.filter.FireAuthority.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.finals;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * Desc： 自定义权限验证注解类
 * @author sinly
 * @refactor sinly
 * @date   2016-11-10 下午8:40:12
 * @version 1.0 
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2016-11-10 	   sinly 	         1. Created this class. 
 * </pre>  
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CmcaAuthority {

	AuthorityType[] authorityTypes();
}
