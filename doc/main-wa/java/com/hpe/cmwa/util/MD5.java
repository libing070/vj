/**
 * com.hpe.cmwa.util.MD5.java
 * Copyright (c) 2016 xx Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmwa.util;

import java.security.MessageDigest;

/**
 * <pre>
 * Desc：   国信公司提供的加密函数
 * @author   peter.fu
 * @refactor peter.fu
 * @date     Nov 21, 2016 3:15:32 PM
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Nov 21, 2016 	   peter.fu 	         1. Created this class. 
 * </pre>
 */

public class MD5 {

	public MD5() {
	}

	public static String byte2hex(byte[] b) {

		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		// System.out.println("--------" + hs.toUpperCase());
		return hs.toUpperCase();
	}

	public static String MD5Crypt(String s) {
		try {
			byte[] strTemp = s.getBytes();
			// System.out.println("--------" + s);
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			return byte2hex(md);
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.print(MD5.MD5Crypt("aaaaaabbbbbccccc"));
	}
}
