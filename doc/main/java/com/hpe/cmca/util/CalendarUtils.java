package com.hpe.cmca.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.hpe.cmca.util.CalendarUtils;

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
	/**
	 * 
	 * <pre>
	 * Desc  从当前日期往前推20天得到的日期
	 * @return
	 * 2017-3-16 下午3:55:54
	 * </pre>
	 */
	public static Calendar getDateBefor20Days(){
		Calendar cal = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.err.println("before====" + sdf.format(cal.getTime()));
		cal.add(Calendar.DAY_OF_MONTH, -20);
		System.err.println("after====" + sdf.format(cal.getTime()));
		return cal;
	}
	
	public static List<SimpleMap> buildCycleList(int month) {
		// 每月20日之后，当前月可以作为审计月  20160822 add commit by GuoXY 推测含义
		Calendar cal = CalendarUtils.getDateBefor20Days();
		cal.add(Calendar.MONTH, month);
		
		
		List<SimpleMap> monthList = new ArrayList<SimpleMap>(24);
		SimpleDateFormat df = new SimpleDateFormat("yyyyM"); 
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy年M月"); 
		for (int i = 0; i < 24; i++) {
			if(cal.get(Calendar.YEAR)>=2015){
				monthList.add(new SimpleMap(df.format(cal.getTime()), df1.format(cal.getTime())));
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
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM"); 
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy年MM月"); 
		for (int i = 0; i < 24; i++) {
			if(cal.get(Calendar.YEAR)>=2015){
				monthList.add(new SimpleMap(df.format(cal.getTime()), df1.format(cal.getTime())));
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

		return buildAuditTimeOfMonth(cycle,1);
	}
	/**
	 * copy from cmccca for 客户欠费审计报告生成
	 */
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
		/**
		 * 输入audtrm =201711，months=-1，输出2017年10月1日-2017年11月30日
		 * @throws ParseException 
		 */
		public static String getMonBeginEndByDay(String audtrm,int months) throws ParseException  {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年M月d日");
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(sdf.parse(audtrm));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			calendar.add(Calendar.MONTH, 1);  
			calendar.set(Calendar.DAY_OF_MONTH, 0); 
			String end =sdf1.format(calendar.getTime());
			calendar.clear();
			calendar.setTime(sdf.parse(audtrm));
			calendar.add(Calendar.MONTH, months);
			calendar.set(Calendar.DAY_OF_MONTH, 1); 
			String begin =sdf1.format(calendar.getTime());
			
			return begin+"-"+end;
		}
	
	/**
	 * @desc 获取指定审计月数据所描述审计时间区间 
	 * 		eg：cycle为201607，interval为1，
	 * 		   返回值为 2016年7月1日-2016年7月31日
	 * @param cycle : 审计月份（YYYYMM）
	 * @param interval : 默认传值为1
	 * 					   正数：代表【cycle月份的第一天，(cycle + interval - 1)月份的最后一天】
	 * 					   负数：代表【(cycle - interval + 1)月份的第一天，cycle月份的最后一天】			 
	 * @modified by GuoXY 20160822，在本次修改之前只有interval>=部分的代码；
	 */
	public static String buildAuditTimeOfMonth(String cycle,int interval) {
		
		StringBuilder auditTime = new StringBuilder();
		String startTime = null;
		String endTime = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy年M月d日"); 
		try {
			Calendar calendar = Calendar.getInstance();
			// 将 calendar 设置为 cycle所在月份  20160822 add commit by GuoXY
			calendar.setTime(DateUtils.parseDate(cycle, new String[] { "yyyyM" }));
			
			if (interval >= 0){
//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//	System.err.println("startTime1====" + sdf.format(calendar.getTime()));
//	System.err.println("startTime2====" + sdf.format(calendar.getTime()));
				// 将 calendar 设置为其所在月份的第一天  20160822 add commit by GuoXY
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
				// 格式化calendar获得所需日期的字符串  20160822 add commit by GuoXY
//				startTime = DateFormatUtils.format(calendar, "yyyy年M月d日");
				startTime = df.format(calendar.getTime());
				
				if(interval>1){
					calendar.add(Calendar.MONTH, interval-1);//interval指的是审计期间跨多少个周期，默认是1。这里之所以-1是为了去掉自身这一周期，比如当前审计周期是4月份，如果审计期间为2个审计周期，则审计期间的结束月份为4+(2-1)=5
				}
				
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//				endTime = DateFormatUtils.format(calendar, "yyyy年M月d日");
				endTime = df.format(calendar.getTime());
//	System.err.println("endTime====" + sdf.format(calendar.getTime()));			
				
			} else { // 当审计期间是在[审计月前几个月, 审计月]时，使用; add by GuoXY
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//				endTime = DateFormatUtils.format(calendar, "yyyy年M月d日");
				endTime = df.format(calendar.getTime());
				if(interval < -1){
					calendar.add(Calendar.MONTH, interval + 1);
				}
				// 将 calendar 设置为其所在月份的第一天  20160822 add commit by GuoXY
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
				// 格式化calendar获得所需日期的字符串  20160822 add commit by GuoXY
//				startTime = DateFormatUtils.format(calendar, "yyyy年M月d日");
				startTime = df.format(calendar.getTime());
			}
			auditTime.append(startTime).append("-").append(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return auditTime.toString();
	}
	/**
	 * copy from cmccca for 客户欠费审计报告生成
	 */
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
	// add by GuoXY 20160822，从buildAuditTimeOfMonth改造而来，获取当前关注点审计月的前起始月份
	// eg：cycle为201607，interval为1，
    // 返回值为 2016年7月
	public static String getAuditDeadlineTimeOfMonth(String cycle,int interval) {
		StringBuilder auditTime = new StringBuilder();
		try {
		
			Calendar calendar = CalendarUtils.getDateBefor20Days();
			calendar.setTime(DateUtils.parseDate(cycle, new String[] { "yyyyM" }));
			
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			
			
			if(interval < -1){
				calendar.add(Calendar.MONTH, interval +1 -1); // +1：审计月本身月份，-1审计区间起始月份前一个月
			}
//			String startTime = DateFormatUtils.format(calendar, "yyyy年M月");
			SimpleDateFormat df = new SimpleDateFormat("yyyy年M月"); 
			String startTime = df.format(calendar.getTime());
			auditTime.append(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return auditTime.toString();
	}
	
	// add by GuoXY 20160914，从buildAuditTimeOfMonth改造而来，通过审计月与目标月份的间隔，求出目标月份，并按指定格式户输出
	// eg：cycle为201607，interval为0，format为yyyy年M月
    // 返回值为 2016年7月
	public static String getMonth(String cycle, int interval, String format) {
		StringBuilder auditTime = new StringBuilder();
		try {
		    	//	当前日期20天以前的日期
			Calendar calendar = CalendarUtils.getDateBefor20Days();
			calendar.setTime(DateUtils.parseDate(cycle, new String[] { "yyyyM" }));

			calendar.add(Calendar.MONTH, interval);

//			String startTime = DateFormatUtils.format(calendar, format);//用DateFormatUtils  在weblogic部署时会报找不到format方法的错误，不知道什么原因，因此干脆不用它了，改为下面两行代码实现   add by xuwenhua 2017.03.16 
			SimpleDateFormat df = new SimpleDateFormat(format); 
			String startTime = df.format(calendar.getTime());
			
			auditTime.append(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return auditTime.toString();
	}
	/**
	 * 
	 * @param yyyyMM
	 * @return "" if input is null or input is empty 
	 *         "" if input is non yyyyMM format
	 */
	public static String getLastMonthStrYYYYMM(String yyyyMM) {
		if(yyyyMM == null || "".equals(yyyyMM.trim())){
			return "";
		}
		String lastAudTrm;
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Integer.valueOf(yyyyMM.substring(0, 4)), Integer.valueOf(yyyyMM.substring(4))-1,1);
			calendar.add(Calendar.MONTH, -1);
			lastAudTrm = new SimpleDateFormat("yyyyMM").format(calendar.getTime());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "";
		}
		return lastAudTrm;
	}
	/**
	 * 
	 * @param yyyyMM
	 * @return "" if input is null or input is empty 
	 *         "" if input is non yyyyMM format
	 */
	public static String getNextMonthStrYYYYMM(String yyyyMM) {
		if(yyyyMM == null || "".equals(yyyyMM.trim())){
			return "";
		}
		String nextAudTrm;
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Integer.valueOf(yyyyMM.substring(0, 4)), Integer.valueOf(yyyyMM.substring(4))+1,1);
			calendar.add(Calendar.MONTH, -1);
			nextAudTrm = new SimpleDateFormat("yyyyMM").format(calendar.getTime());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "";
		}
		return nextAudTrm;
	}
	public static String buildAuditCycle(String cycle) {
		try {
			Calendar calendar = CalendarUtils.getDateBefor20Days();
			calendar.setTime(DateUtils.parseDate(cycle, new String[] { "yyyyM" }));
//			System.err.println("====" + DateFormatUtils.format(calendar, "yyyy年M月"));
//			return DateFormatUtils.format(calendar, "yyyy年M月");
			SimpleDateFormat df = new SimpleDateFormat("yyyy年M月"); 
			String now = df.format(calendar.getTime());
			System.err.println(now);
			return now;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cycle;
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

	/**
	 * @desc 获取指定审计月数据所描述审计时间区间 
	 * 		eg：month为201607，flag为0，返回2016年7月1日
	 * 		    month为201607，flag为1，返回2016年7月31日
	 * @param month : 审计月份（YYYYMM）
	 * @param flag : 0-第一天，1-最后一天
	 */
	public static String getDayOfMon(String month, int flag) {
		String auditTime = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy年M月d日"); 
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(DateUtils.parseDate(month, new String[] { "yyyyM" }));
			if (flag == 0){
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
//				auditTime = DateFormatUtils.format(calendar, "yyyy年M月d日");
				auditTime = df.format(calendar.getTime());
			} else {
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//				auditTime = DateFormatUtils.format(calendar, "yyyy年M月d日");
				auditTime = df.format(calendar.getTime());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return auditTime.toString();
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  时间格式转换
	 * @param strTime : 要转化的时间字符串
	 * @param inSdf : strTime参数的时间格式                                eg:"yyyy-MM-dd HH:mm:ss"
	 * @param outSdf : 要将strTime参数转换成什么时间格式       eg:"yyyyMMddHHmmss"
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   Dec 14, 2016 9:48:47 AM
	 * </pre>
	 */
	public static String getTimeBySdfInOut(String strTime, String inSdf, String outSdf) {
		String result = "";
		try {
			SimpleDateFormat sdfIn = new SimpleDateFormat(inSdf);
			SimpleDateFormat sdfOut = new SimpleDateFormat(outSdf);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdfIn.parse(strTime));
			Date date = (Date) calendar.getTime();
			result = sdfOut.format(date);
			//System.err.println("out:" + str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * <pre>
	 * Desc  获取时间差，以秒返回结果
	 * @param start
	 * @param end
	 * @return
	 * @author GuoXY
	 * @refactor GuoXY
	 * @date   Dec 18, 2016 3:35:56 PM
	 * </pre>
	 */
	public static Long getTimeDiffBySec(Date start, Date end) {
		if (null == start || null == end) {
			return null;
		}
			
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		try  
		{   
		    long diff = end.getTime() - start.getTime();   
		    long sec = diff / 1000;   
		    return sec;
		}   
		catch (Exception e)   
		{   
			System.err.println("计算时间差异常：" + e + "。\n start:" + start + ",end:" + end);
		}  
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		System.err.println(getMonBeginEndByDay("201705",0));
		System.out.println(buildAuditTimeOfMonth("201705",2));
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
