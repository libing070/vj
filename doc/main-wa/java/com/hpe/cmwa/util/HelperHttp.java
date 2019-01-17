package com.hpe.cmwa.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class HelperHttp {

	public static String getPostURLContent(String urlStr, Map params) throws Exception {

		String paramStr = "&r=" + new Random().nextFloat();
		for (Object key : params.keySet()) {
			paramStr += "&" + key.toString() + "=" + URLDecoder.decode(params.get(key).toString());
		}

		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");// 提交模式
		conn.setDoOutput(true);// 是否输入参数

		byte[] bypes = paramStr.getBytes();
		conn.getOutputStream().write(bypes);// 输入参数
		InputStream inStream = conn.getInputStream();
		String sTotalString = "";
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
		String sCurrentLine = "";

		while ((sCurrentLine = bufferedReader.readLine()) != null) {
			sTotalString += sCurrentLine;
		}

		return sTotalString;
	}

	/**
	 * 程序中访问http数据接口
	 * @throws MalformedURLException
	 */
	public static String getURLContent(String urlStr,Map<String, Object> params) throws Exception {

		if (params != null && params.size() > 0) {
			urlStr = urlStr+"?r=" + new Random().nextFloat();
			for (Object key : params.keySet()) {
				if(params.get(key) == null){
					urlStr += "&" + key.toString() + "=" + "";
				}else{
					urlStr += "&" + key.toString() + "=" + params.get(key).toString();
				}
			}
		}
		URL url = new URL(urlStr);
		URLConnection URLconnection = url.openConnection();

		HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
		httpConnection.setRequestMethod("GET");
		httpConnection.setRequestProperty("appid", "aaaa");

		int responseCode = httpConnection.getResponseCode();
		String sTotalString = "";
		if (responseCode == HttpURLConnection.HTTP_OK) {
			InputStream urlStream = httpConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream, "UTF-8"));
			String sCurrentLine = "";

			while ((sCurrentLine = bufferedReader.readLine()) != null) {
				sTotalString += sCurrentLine;
			}
		} else {
			return "ResponseCode:" + responseCode;
		}
		return sTotalString;

	}

	public static String getUrlReturnValue(String url) {
		StringBuffer content = new StringBuffer();
		try {
			// 新建URL对象
			URL u = new URL(url);

			InputStream in = new BufferedInputStream(u.openStream());
			InputStreamReader theHTML = new InputStreamReader(in, "utf-8");
			int c;
			while ((c = theHTML.read()) != -1) {
				content.append((char) c);
			}
			return content.toString();
		}
		// 处理异常
		catch (Exception e) {
			return "ERROR:" + e.getMessage();
		}
	}

	/**
	 * 访问HTTPS 传递参数
	 * @param httpsUrl
	 *            路径，如: https://www.sun.com
	 * @param params
	 *            参数
	 * @param method
	 *            传递方式，POST,GET
	 * @return 远程服务返回的值.
	 * @throws IOException
	 */
	public static String getHttpsReturnValue(String httpsUrl, Map<String, Object> params, String method) throws IOException {
		StringBuilder returnValue = new StringBuilder();
		String paramStr = "";
		if (params != null && params.size() > 0) {
			paramStr = "&r=" + new Random().nextFloat();
			for (Object key : params.keySet()) {
				paramStr += "&" + key.toString() + "=" + URLDecoder.decode(params.get(key).toString());
			}
		}
		// 创建URL对象
		URL myURL = new URL(httpsUrl);
		// 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
		HttpsURLConnection httpsConn = (HttpsURLConnection) myURL.openConnection();

		httpsConn.setRequestMethod(method);// 提交模式//"POST"
		httpsConn.setDoOutput(true);// 是否输入参数
		byte[] bypes = paramStr.getBytes();
		httpsConn.getOutputStream().write(bypes);// 输入参数
		// 取得该连接的输入流，以读取响应内容

		// 指定编码方式.
		InputStream inStream = httpsConn.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
		// 读取服务器的响应内容并显示
		String sCurrentLine = "";

		while ((sCurrentLine = bufferedReader.readLine()) != null) {
			returnValue.append(sCurrentLine);
		}

		return returnValue.toString();
	}
}
