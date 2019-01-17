package com.hpe.cmca.util;

import org.apache.commons.lang.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateComputeUtils {


	public static String addMonth(String date,Integer i) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Date dt = null;//将字符串生成Date
		try {
			dt = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);//使用给定的 Date 设置此 Calendar 的时间。
		rightNow.add(Calendar.MONTH, i);// 日期减1个月
		Date dt1 = rightNow.getTime();//返回一个表示此 Calendar 时间值的 Date 对象。
		String reStr = sdf.format(dt1);//将给定的 Date 格式化为日期/时间字符串，并将结果添加到给定的 StringBuffer。
		return reStr;
	}

	public static String addMonthChinese(String date,Integer i) {

		String reStr=addMonth(date,i);
		String reStrChinese= reStr.substring(0,4)+"年"+reStr.substring(4,6)+"月";
		return reStrChinese;
	}

}
