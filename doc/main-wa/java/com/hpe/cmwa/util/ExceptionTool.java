package com.hpe.cmwa.util;

/**
 * <pre>
 * Desc�� �쳣����
 * @author fushe
 * @date   2011-6-17 ����10:47:11
 * @version 1.0
 * @see  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2011-6-17 	   fushe 	         1. Created this class.
 * </pre>
 */
public class ExceptionTool {

	/**
	 * Desc ��ȡ�쳣��ջ��Ϣ
	 * @param e
	 * @return
	 * @author fushe
	 * @date 2011-6-17 ����10:47:17
	 */
	public static String getExceptionDescription(Throwable e) {

		if (e == null) {
			return "";
		}

		StackTraceElement[] stackTraceElement = e.getStackTrace();
		StringBuffer sb = new StringBuffer();
		final String newLine = "\r\n";
		sb.append(e.toString() + newLine);
		for (int i = 0; i < stackTraceElement.length; i++) {
			StackTraceElement element = stackTraceElement[i];
			sb.append("	at " + element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")").append(newLine);
		}
		return sb.toString();
	}

}
