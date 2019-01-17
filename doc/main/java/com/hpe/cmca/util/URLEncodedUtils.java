package com.hpe.cmca.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.httpclient.NameValuePair;


/**
 * 
 * <pre>
 * Desc： URL解析出参数的工具
 * @author GuoXY
 * @refactor GuoXY
 * @date   Nov 22, 2016 10:51:44 AM
 * @version 1.0
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  Nov 22, 2016 	   GuoXY 	         1. Created this class. 
 * </pre>
 */


public class URLEncodedUtils {
	private static final String PARAMETER_SEPARATOR = "&";
	private static final String NAME_VALUE_SEPARATOR = "=";
	public static final String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";

	/*
	 * assume each name is unique
	 */
	public static String getParameter(final String url, final String encoding,
			final String name) throws URISyntaxException {
		Map<String, String> mapparams = new HashMap<String, String>();
		List<NameValuePair> params = URLEncodedUtils.parse(new URI(url), "UTF-8");
		for (NameValuePair param : params) {
			mapparams.put(param.getName(), param.getValue());
		}
		
		return mapparams.get(name);
	}

	public static List<NameValuePair> parse(final URI uri, final String encoding) {
		List<NameValuePair> result = Collections.emptyList();
		final String query = uri.getRawQuery();
		if (query != null && query.length() > 0) {
			result = new ArrayList<NameValuePair>();
			parse(result, new Scanner(query), encoding);
		}
		return result;
	}

	public static void parse(final List<NameValuePair> parameters,
			final Scanner scanner, final String encoding) {
		scanner.useDelimiter(PARAMETER_SEPARATOR);
		while (scanner.hasNext()) {
			final String[] nameValue = scanner.next().split(
					NAME_VALUE_SEPARATOR);
			if (nameValue.length == 0 || nameValue.length > 2)
				throw new IllegalArgumentException("bad parameter");

			final String name = decode(nameValue[0], encoding);
			String value = null;
			if (nameValue.length == 2)
				value = decode(nameValue[1], encoding);
			parameters.add(new NameValuePair(name, value));
		}
	}

	private static String decode(final String content, final String encoding) {
		try {
			return URLDecoder.decode(content, encoding != null ? encoding
					: DEFAULT_CONTENT_CHARSET);
		} catch (UnsupportedEncodingException problem) {
			throw new IllegalArgumentException(problem);
		}
	}
	
	
	public static void main(String[] args) {
		String sUrl = "http://book.sina.cn/prog/wapsite/books/h5/vipc.php?bid=217269&cid=293500&cp=1&sort=asc&PHPSESSID=0cbd7d80b00593b0b3d446bd97d781ab&vt=4&book_explore=1";
		List<NameValuePair> params;
		try {
			params = URLEncodedUtils.parse(new URI(sUrl), "UTF-8");
			for (NameValuePair param : params) {
			      System.out.println(param.getName() + " : " + param.getValue());
			    }
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("getparameter begin.........");
		try {
			System.out.println(URLEncodedUtils.getParameter(sUrl, "UTF-8", "bid"));
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("getparameter end.........");
	}
}

