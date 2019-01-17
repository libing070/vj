package com.hpe.cmwa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

public class CalendarUtils {

	/**
	 * <pre>
	 * Desc  构建最新的24个统计周期
	 * @return
	 * @author Liuxuan
	 * @refactor Liuxuan
	 * @date   2015-1-9 下午01:49:24
	 * </pre>
	 */
	public static List<SimpleMap> buildLast24Cycle() {
		
		return CalendarUtils.buildCycleList(-1);
	}
	
	public static Calendar getDateBefor20Days(){
		Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.err.println("befor====" + sdf.format(cal.getTime()));
		cal.add(Calendar.DAY_OF_MONTH, -20);
		System.err.println("after====" + sdf.format(cal.getTime()));
		return cal;
	}
	
	public static List<SimpleMap> buildCycleList(int month) {
		// 每月20日之后，当前月可以作为审计月  20160822 add commit by GuoXY 推测含义
		Calendar cal = CalendarUtils.getDateBefor20Days();
		cal.add(Calendar.MONTH, month);
		
		
		List<SimpleMap> monthList = new ArrayList<SimpleMap>(24);
		for (int i = 0; i < 24; i++) {
			if(cal.get(Calendar.YEAR)>=2015){
				monthList.add(new SimpleMap(DateFormatUtils.format(cal, "yyyyM"), DateFormatUtils.format(cal, "yyyy年M月")));
			}
			cal.add(Calendar.MONTH, -1);
		}
		return monthList;
	}
	
	public static Calendar getDateBeforDays(){    //获取自然月   
		Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("befor====" + sdf.format(cal.getTime()));
		cal.add(Calendar.DAY_OF_MONTH, 0);
		System.out.println("after====" + sdf.format(cal.getTime()));
		return cal;
	}
	
	public static List<SimpleMap> buildManagerCycleList() {   //创建审计管理需要的日期
		Calendar cal = CalendarUtils.getDateBeforDays();
		List<SimpleMap> monthList = new ArrayList<SimpleMap>(24);
		for (int i = 0; i < 24; i++) {
			if(cal.get(Calendar.YEAR)>=2015){
				monthList.add(new SimpleMap(DateFormatUtils.format(cal, "yyyyMM"), DateFormatUtils.format(cal, "yyyy年MM月")));
			}
			cal.add(Calendar.MONTH, -1);
		}
		return monthList;
	}

	public static final List<SimpleMap> DateDimension = new ArrayList<SimpleMap>() {
			{
				add(new SimpleMap("yyyyM", "审计周期:<b>月</b>"));
				//add(new SimpleMap("yyyyMM-dd", "统计维度：<b>日</b>"));
			}
			
		};
	
	/**
	 * 
	 * <pre>
	 * Desc  根据条件中的月份构建审计周期字符串
	 *       2014年12月01日-2014年12月31日
	 * @param string
	 * @return
	 * @author Liuxuan
	 * @refactor Liuxuan
	 * @date   2015-1-9 下午01:47:33
	 * </pre>
	 */
	public static String buildAuditTimeOfMonth(String cycle) {
		
		return cycle.substring(0, 4)+"年"+cycle.substring(4, 6)+"月";
	}
	/**
	 * 
	 * <pre>
	 * Desc  根据条件中的月份构建审计周期字符串
	 *       2014年12月01日-2014年12月31日
	 * @param string
	 * @return
	 * @author Liuxuan
	 * @refactor Liuxuan
	 * @date   2015-1-9 下午01:47:33
	 * </pre>
	 */
	public static String buildAuditTimeOfOnlyMonth(String cycle) {
		
		return cycle.substring(4, 6)+"月";
	}
	

	
	
	// add by GuoXY 20160822，从buildAuditTimeOfMonth改造而来，获取当前关注点审计月的前一个月份
	// eg：eg：cycle为201607，interval为1，
    //  返回值为 2016年7月1日-2016年7月31日
	public static String getAuditDeadlineTimeOfMonth(String cycle,int interval) {
		StringBuilder auditTime = new StringBuilder();
		try {
		
			Calendar calendar = CalendarUtils.getDateBefor20Days();
			calendar.setTime(DateUtils.parseDate(cycle, new String[] { "yyyyM" }));
			
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			
			
			if(interval < -1){
				calendar.add(Calendar.MONTH, interval +1 -1); // +1：审计月本身月份，-1审计区间起始月份前一个月
			}
			String startTime = DateFormatUtils.format(calendar, "yyyy年M月");
			auditTime.append(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return auditTime.toString();
	}
	
	
	public static String buildAuditCycle(String cycle, String format) {

		try {

			Calendar calendar = CalendarUtils.getDateBefor20Days();
			calendar.setTime(DateUtils.parseDate(cycle, new String[] { "yyyyM" }));

			return DateFormatUtils.format(calendar, "yyyy年M月");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cycle;
	}
	// 获取月份的月初和月底
	   //输入201607 输出2016年7月1日-2016年7月31日
		public static String getMonBeginEnd(String audtrm)  {
			//201607
//			String begin = audtrm.substring(0, 4) + "年" + 
//			(audtrm.substring(4, 5) == "0" ? audtrm.substring(5, 6) : audtrm.substring(4, 6) )+ "月" + "1日";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = null;
			try {
				date = sdf.parse(audtrm + "01");
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年M月d日");
			String begin=sdf1.format(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
			calendar.roll(Calendar.DATE, -1);
		  
		    String end = sdf1.format(calendar.getTime());
	//2016-7-31 0:00:00
//			String end = endTp.substring(0, 4) + "年" +
//					(endTp.substring(5, 6) == "0" ? endTp.substring(6, 7) : endTp.substring(5, 7) )+ "月" +
//					endTp.substring(endTp.lastIndexOf("-")+1, endTp.indexOf(" "))+"日";

		    
			return begin+"-"+end;
		}
		
		public static String AddMontns(String strDate,int i){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date=null;
			try {
				 date=sdf.parse(strDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date); 
			calendar.add(Calendar.MONTH, i);
			return sdf.format(calendar.getTime());
			
		}
		//排名汇总-文花相关内容推测性迁移-by hufei
		public static String buildAuditCycle(String cycle) {
			try {
				Calendar calendar = CalendarUtils.getDateBefor20Days();
				calendar.setTime(DateUtils.parseDate(cycle, new String[] { "yyyyM" }));
//				System.err.println("====" + DateFormatUtils.format(calendar, "yyyy年M月"));
//				return DateFormatUtils.format(calendar, "yyyy年M月");
				SimpleDateFormat df = new SimpleDateFormat("yyyy年M月"); 
				String now = df.format(calendar.getTime());
				System.err.println(now);
				return now;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return cycle;
		}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		Calendar cal = Calendar.getInstance();
//		Calendar point201501 = Calendar.getInstance();
//		point201501.set(Calendar.YEAR, 2015);
//		point201501.set(Calendar.MONTH, 01);
//		List<SimpleMap> monthList = new ArrayList<SimpleMap>(24);
//		for (int i = 0; i < 24; i++) {
//			cal.add(Calendar.MONTH, -1);
//			if(cal.after(point201501)){
//				System.err.println(DateFormatUtils.format(cal, "yyyyM"));
//				monthList.add(new SimpleMap(DateFormatUtils.format(cal, "yyyyM"), DateFormatUtils.format(cal, "yyyy年M月")));
//			}
//		}
		 //CalendarUtils.getDateBefor20Days();
		 //getAuditDeadlineTimeOfMonth("201607", -2);	
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Calendar calendar = Calendar.getInstance();
//		System.err.println("startTime1====" + sdf.format(calendar.getTime()));
//		calendar.add(Calendar.MONTH, 1);
//		System.err.println("startTime2====" + sdf.format(calendar.getTime()));
//		System.err.println("startTime=====================" + getAuditDeadlineTimeOfMonth("201604", 1));
//		System.err.println("startTime=====================" + getAuditDeadlineTimeOfMonth("201604", -2));
//		System.err.println("startTime=====================" + getAuditDeadlineTimeOfMonth("201604", -4));
//		System.err.println("startTime=====================" + getAuditDeadlineTimeOfMonth("201604", -4));
	}
	
	
}
