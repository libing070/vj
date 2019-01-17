package com.hpe.cmca.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @Title 时间工具类
 * @author admin
 *
 */
public class DateUtilsForCurrProject {

	//查询半年前的年月
	@SuppressWarnings("static-access")
	public static String GetDateBeforeSixMonth(Date date){
		Calendar calendar = Calendar.getInstance(); //得到日历
 		calendar.setTime(date);//把当前时间赋给日历
 		calendar.add(calendar.MONTH, -6); 
 		date = calendar.getTime();
 		SimpleDateFormat sf = new SimpleDateFormat("yyyyMM") ;
 		String sixMonthDate  = sf.format(date) ;
 		return sixMonthDate ;
	}
	//年月格式转换
	public static String getYearAndMonth(Date date){
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMM") ;
		String newDate = sf.format(date) ;
		return newDate ;
	}
	//String转换Date
    public static Date stringToDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return (sdf.parse(dateStr));
    }
	//yyyyMM转换yyyy年MM月
    public static String yyyyMMToyyyyYearMMMonth(String dateStr) throws ParseException {
        Date date = stringToDate(dateStr) ;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月") ;
        String newDateStr = sdf.format(date) ;
        return newDateStr ;
    }
    //当前字符串是否可以转换为Date
    public static boolean currStrIsNotDate(String date){
    	boolean b ;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
    	try {
			sdf.parse(date) ;
			b = true ;
		} catch (ParseException e) {
			b= false ;
		}
    	return b ;
    }
    //获取开始年月到结束年月所有月份集合——格式:201510
    public static ArrayList<String> getAllYearAndMonth(String yearsStart , String yearsEnd) throws ParseException{
    	
    		//定义所有月份集合
    		ArrayList<String> allYearMonthList = new ArrayList<String>();  
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");//格式化为年月  
		  
		    Calendar min = Calendar.getInstance();  
		    Calendar max = Calendar.getInstance();  
		  
		    min.setTime(sdf.parse(yearsStart));  
		    min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);  
		  
		    max.setTime(sdf.parse(yearsEnd));  
		    max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);  
		  
		    Calendar curr = min;  
		    while (curr.before(max)) {  
		    	allYearMonthList.add(sdf.format(curr.getTime()));  
		    	curr.add(Calendar.MONTH, 1);  
		    }  
		    return allYearMonthList ;
    }
    //获取几个月后的年月（最后一天）
    public static String getAfterAnyMonthLastDay(String audTrm , int anyMonth) throws ParseException{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月");
		Date date = sdf.parse(audTrm) ;
        
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, anyMonth + 1);
        calendar.set(Calendar.DATE, 0);
        
        return df.format(calendar.getTime());
    }
    
    //获取几个月后的年月YYYYMM
    public static String getAfterAnyMonth(String audTrm , int anyMonth) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Date date = null;
		try {
			date = sdf.parse(audTrm);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, anyMonth + 1);
        calendar.set(Calendar.DATE, 0);
        
        return sdf.format(calendar.getTime());
    }
    
    
    //获取几个月前的日期
    public static String getBeforeAnyMonth(Date  audTrm , int anyMath){
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        Calendar c = Calendar.getInstance();
        c.setTime(audTrm);
        c.add(Calendar.MONTH, -1*anyMath);
        Date m = c.getTime();
        String mon = format.format(m);
        return mon ; 
    }
}
