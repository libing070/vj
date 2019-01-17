package com.hpe.cmwa.util;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;

public class HelperDate extends AbstractTransformer implements ObjectFactory {

	SimpleDateFormat simpleDateFormatter;

	public HelperDate(String dateFormat) {
		simpleDateFormatter = new SimpleDateFormat(dateFormat);
	}

	public void transform(Object value) {
		getContext().writeQuoted(simpleDateFormatter.format(value));
	}

	public Object instantiate(ObjectBinder context, Object value,
			Type targetType, Class targetClass) {
		try {
			return simpleDateFormatter.parse(value.toString());
		} catch (ParseException e) {
			// throw new JSONException(String.format(
			// "Failed to parse %s with %s pattern.", value,
			// simpleDateFormatter.toPattern() ), e );
			return value;
		}
	}

	/**
	 * 将某种日期类型的字符串例如：格林尼治时间类型，转换为想要的某种格式的字符串例如：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateStr
	 *            原始字符串
	 * @param formatStr
	 *            要转换为的类型格式 如：yyyy-MM-dd HH:mm:ss
	 * @return 转换完成的字符串格式，如果传入的两个值其中一个为空或者传入格式不正确引起报错等情况会返回null
	 */
	public static String formatDateStrToStr(String dateStr, String formatStr) {
		if ((dateStr != null && !"".equals(dateStr))
				&& (formatStr != null && !"".equals(formatStr))) {
			try {
				SimpleDateFormat f = new SimpleDateFormat(formatStr);
				Date date = new Date(dateStr);
				return f.format(date);
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	public static String formatDate(Date date, String format) {
		try {
			SimpleDateFormat dataformat = new SimpleDateFormat(format);

			return dataformat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * 创建
	 * 
	 * @param getType创建方式
	 *            0 自己计算 1 方法内计算
	 * @param year
	 * @param month
	 * @param day
	 * @param hours
	 *            24
	 * @param minutes
	 * @param seconds
	 * @return
	 */
	public static Date getDate(int getType, int year, int month, int day,
			int hours, int minutes, int seconds) {
		if (getType == 1) {
			if (hours > 0)
				hours--;
			if (minutes > 0)
				minutes--;
			if (month > 0)
				month--;
			if (year > 1900)
				year = year - 1900;
		}
		Date date = new Date(year, month, day, hours, minutes, seconds);
		return date;
	}

	public static Date getDateSubDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

	/**
	 * 几天后的日期
	 * 
	 * @param date
	 * @param dayCount
	 * @return
	 */
	public static Date getAfterDate(Date date, int dayCount) {

		date = new Date(date.getYear(), date.getMonth(), date.getDate());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, dayCount);

		return calendar.getTime();

	}

	/**
	 * 获取离指定时间 date, workDayCount天工作日后的日期。
	 * 
	 * @param date
	 * @param workDayCount
	 * @return
	 */
	public static Date getAfterWorkDate(Date date, int workDayCount) {
		/*
		 * Calendar calendar = Calendar.getInstance(); calendar.setTime(date);
		 * 
		 * for (int i = 0; i < workDayCount; i++) {
		 * 
		 * calendar.add(Calendar.DATE, i);
		 * 
		 * Date theDay = calendar.getTime();
		 * 
		 * if (isWorkDay(theDay)) { continue; } else { workDayCount++; }
		 * 
		 * }
		 * 
		 * return calendar.getTime();
		 */

		date = new Date(date.getYear(), date.getMonth(), date.getDate());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int setpNo = 1;
		if (workDayCount < 0) {
			workDayCount = -workDayCount;
			setpNo = -1;
		}

		for (int i = 0; i < workDayCount; i++) {

			calendar.add(Calendar.DATE, setpNo);
			if (!HelperDate.isWorkDay(calendar.getTime())) // 是否为工作日
				workDayCount++;
		}

		return calendar.getTime();

	}

	/**
	 * 判断是否是周末
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isWeekend(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		if (Calendar.SUNDAY == week || Calendar.SATURDAY == week) {

			return true;

		} else {

			return false;

		}
	}

	/**
	 * 是否是工作日
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isWorkDay(Date date) {

		date = new Date(date.getYear(), date.getMonth(), date.getDate());

		List<Date> workDays = getWorkdays(); // 周末调休 只存在周六周日
		List<Date> holidays = getHolidays(); // 法定节假日

		if (holidays.contains(date)) {
			return false;
		}
		if (isWeekend(date) && !workDays.contains(date)) {
			return false;
		}

		return true;
	}

	/**
	 * 法定节假日
	 * 
	 * @return
	 */
	public static List<Date> getHolidays() {
		List<Date> list = new ArrayList<Date>();

		list.add(getDate(1, 2014, 10, 1, 0, 0, 0));
		list.add(getDate(1, 2014, 10, 2, 0, 0, 0));
		list.add(getDate(1, 2014, 10, 3, 0, 0, 0));
		list.add(getDate(1, 2014, 10, 4, 0, 0, 0));
		list.add(getDate(1, 2014, 10, 5, 0, 0, 0));
		list.add(getDate(1, 2014, 10, 6, 0, 0, 0));
		list.add(getDate(1, 2014, 10, 7, 0, 0, 0));
		return list;
	}

	/**
	 * 周末工作日
	 * 
	 * @return
	 */
	public static List<Date> getWorkdays() {
		List<Date> list = new ArrayList<Date>();

		list.add(getDate(1, 2014, 10, 11, 0, 0, 0));
		return list;
	}

	public static String dateSubtraceDate(Date date1, Date date2) {
		try {
			return ((date1.getTime() - date2.getTime() + 1000000) / (3600 * 24 * 1000))
					+ "";
		} catch (Exception e) {
			return null;
		}
	}

	public static Date getDateFromMonthStr(String month) {

		if (month == null || month.length() < 5) {
			return null;
		}
		int m = 0;
		if (month.length() == 5) {

			m = Integer.parseInt(month.substring(4, 5));
		} else {
			m = Integer.parseInt(month.substring(4, 6));
		}
		int y = Integer.parseInt(month.substring(0, 4));

		return getDate(1, y, m, 1, 1, 1, 1);

	}
 
	/**
	 * 获取从当前月份往前推N个月，所有的月份列表，月份格式YYYYMM
	 * @param month  当前月份
	 * @param focusNum 往前推几个月份
	 * @return
	 */
	public static List<String> getFocusMonthFromMonth(String month,int focusNum) {
		Date d = HelperDate.getDateFromMonthStr(month);
		Calendar c = Calendar.getInstance();
		c.setTime(d);

		List<String> l = new ArrayList<String>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
		l.add(sf.format(c.getTime()));
		
		for (int i = 0; i < focusNum; i++) {
			c.add(Calendar.MONTH, -1);
			 
			l.add(sf.format(c.getTime())); 
		}
		Collections.sort(l);
		return l;
	}

	    /**
	     * 获取起止月份之间，所有的月份列表，月份格式YYYYMM
	     * 
	     * @param monthBegin
	     *            起始月份
	     * @param monthEnd
	     *            终止月份
	     * @return
	     * @author GuoXY
	     * @date 20160904
	     */
	    public static List<String> getFocusMonthFromMonth(String monthBegin, String monthEnd) {
		Date dateMonthBegin = HelperDate.getDateFromMonthStr(monthBegin);
		Date dateMonthEnd = HelperDate.getDateFromMonthStr(monthEnd);
		// 获取月份差
		int monthDiff = (dateMonthEnd.getYear() - dateMonthBegin.getYear()) * 12 + dateMonthEnd.getMonth() - dateMonthBegin.getMonth();
		
		Date d = HelperDate.getDateFromMonthStr(monthEnd);
		Calendar c = Calendar.getInstance();
		c.setTime(d);

		List<String> l = new ArrayList<String>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
		l.add(sf.format(c.getTime()));

		for (int i = 0; i < monthDiff; i++) {
		    c.add(Calendar.MONTH, -1);
		    l.add(sf.format(c.getTime()));
		}
		Collections.sort(l);
		return l;
	    }
	
	public static String getCurrentDateStr() {

		SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
		
		return sf.format(new Date());
		
	}

	public static String getYOYMonth(String month) {

		String year = month.substring(0,4);
		int yoyyear = Integer.parseInt(year)-1;
		
		return month.replace(year, ""+yoyyear);
		 
	}
	public static void main(String[] args) {
		System.out.println(getMOMMonth("201502"));
		System.out.println(getMOMMonth("201512"));
		System.out.println(getMOMMonth("200001"));
		System.out.println(getMOMMonth("201612"));
		System.out.println(getMOMMonth("201701"));
	}

	public static String getMOMMonth(String month) {
		 
		int year = Integer.parseInt(month.substring(0,4));
		int m = Integer.parseInt(month.substring(4,6));
		String mo = "";
		if (m==1) {
			m = 12;
			year = year-1;
		}else{
			m=m-1;
		}
		mo = m+"";
		if (m<10) {
			mo = "0"+m;
		}
		
		
		return year+""+mo;
	}

}
