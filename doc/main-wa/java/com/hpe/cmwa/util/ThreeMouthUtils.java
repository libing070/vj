package com.hpe.cmwa.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreeMouthUtils {

	public static String getThreeMouth(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String time = sdf.format(date);String[] item = time.split("-");
		int year  = Integer.parseInt(item[0]);
		int month = Integer.parseInt(item[1]);
		if((month - 3) <= 0){
			month = month + 12 - 3;year = year -1;
		}else {
			month = month - 3;
			}
		if(month<10){
			time = year + "0" + month;
		}else{
			time = year + "" + month;
		}
		
		return time;
	}
	
	public static String getFirthMouth(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String time = sdf.format(date);String[] item = time.split("-");
		int year  = Integer.parseInt(item[0]);
		int month = Integer.parseInt(item[1]);
		if((month - 6) <= 0){
			month = month + 12 - 6;year = year -1;
		}else {
			month = month - 6;
			}
		year = year -1;
		if(month<10){
			time = year + "0" + month;
		}else{
			time = year + "" + month;
		}
		
		return time;
	}
}
