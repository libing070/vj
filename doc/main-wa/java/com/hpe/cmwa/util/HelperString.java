package com.hpe.cmwa.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperString {
	/**
	 * 判断字符串对象是否是null或者空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {
		return obj == null || "".equals(obj.toString());
	}

	/**
	 * 如果是null返回空
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		if (obj == null)
			return "";
		return obj.toString();
	}

	public static String join(Collection s, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		Iterator iter = s.iterator();
		while (iter.hasNext()) {
			buffer.append(iter.next());
			if (iter.hasNext()) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}

	/**
	 * tianyue surround包围字符串数组中的元素，并用separator将包围后的元素拼接在一起
	 * 
	 * @param idArr
	 *            字符串数组
	 * @param separator
	 *            分隔符字符
	 * @param surround
	 *            包围字符
	 * @return 处理后的字符串
	 */
	public static String joinStringSurrWithSep(String[] idArr, String separator, String surround) {

		String str = "";

		if (idArr == null || idArr.length == 0) {
			return "";
		}
		if (separator == null) {
			separator = "";
		}
		if (surround == null) {
			surround = "";
		}

		for (String id : idArr) {
			if (id.trim().equals("")) {
				continue;
			}
			str += surround + id + surround + separator;
		}
		if (str.endsWith(separator)) {
			str = str.substring(0, str.length() - separator.length());
		}

		return str;
	}

	/**
	 * 首字母转换为大写
	 * 
	 * @param str
	 * @return
	 */
	public static String firstCharUpper(String str) {
		if (isNullOrEmpty(str)) {
			return "";
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * 获取邮件模板中所有的邮件参数。邮件参数的格式为[@xxxxx]
	 * 
	 * @param emailTemplete
	 * @return 取出字符串中所有[@xxxxx]的集合.
	 */
	public static List<String> getEmailParamsFromEmailTemplete(String emailTemplete) {

		List<String> list = new ArrayList<String>();

		Pattern pattern = Pattern.compile("\\[[^\\]]+\\]");
		Matcher matcher = pattern.matcher(emailTemplete);
		String param = "";
		while (matcher.find()) {
			param = matcher.group();

			if (param.startsWith("[") && param.endsWith("]") && param.indexOf("@") == 1) {
				list.add(param);
			}
		}
		return list;
	}

	/**
	 * 
	 * @param str  格式:"aaa{0}kjfkdf{1}dfjdkf{2}"
	 * @param args 对应{0}{1}{2}的值
	 * @return
	 */
	public static String format(String str, Object... args) {

		for (int i = 0; i < args.length; i++) {

			str = str.replace("{" + i + "}", args[i].toString());

		} 
		return str;
	}

	/**
	 * 四舍五入保留2位小数
	 * 
	 * @param d
	 * @return
	 * @author Huang Tao
	 * @refactor Huang Tao
	 * @date 2016年8月1日 下午2:37:21
	 */
    public static String formatRoundUp(double d) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(d);
    }
    
    /**
     * 将对象转换为字符串
     * 
     * @param obj
     * @return
     * @author Huang Tao
     * @refactor Huang Tao
     * @date 2016年11月25日 下午6:18:07
     */
    public static String objectConvertString(Object obj) {
        if (obj == null) {
            return "";
        }

        if (obj instanceof Integer || obj instanceof Float || obj instanceof Double) {
            return String.valueOf(obj);
        }
        else if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).toString();
        }

        return String.valueOf(obj);
    }
    
}
